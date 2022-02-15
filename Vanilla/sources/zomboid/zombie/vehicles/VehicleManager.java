package zombie.vehicles;

import gnu.trove.list.array.TShortArrayList;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.SoundManager;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.core.physics.Bullet;
import zombie.core.physics.Transform;
import zombie.core.physics.WorldSimulation;
import zombie.core.raknet.UdpConnection;
import zombie.core.utils.UpdateLimit;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.DrainableComboItem;
import zombie.iso.IsoChunk;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerOptions;
import zombie.scripting.objects.VehicleScript;

public final class VehicleManager {
   public static VehicleManager instance;
   private final VehicleIDMap IDToVehicle;
   private final ArrayList vehicles;
   private boolean idMapDirty;
   private final Transform tempTransform;
   private final ArrayList send;
   private final TShortArrayList vehiclesWaitUpdates;
   public static short physicsDelay = 100;
   public UdpConnection[] connected;
   private final float[] tempFloats;
   private final VehicleManager.PosUpdateVars posUpdateVars;
   private UpdateLimit vehiclesWaitUpdatesFrequency;
   private BaseVehicle tempVehicle;
   private ArrayList oldModels;
   private ArrayList curModels;
   private static UpdateLimit sendReqestGetPositionFrequency = new UpdateLimit(500L);
   UpdateLimit VehiclePhysicSyncPacketLimit;

   public VehicleManager() {
      this.IDToVehicle = VehicleIDMap.instance;
      this.vehicles = new ArrayList();
      this.idMapDirty = true;
      this.tempTransform = new Transform();
      this.send = new ArrayList();
      this.vehiclesWaitUpdates = new TShortArrayList(128);
      this.connected = new UdpConnection[512];
      this.tempFloats = new float[27];
      this.posUpdateVars = new VehicleManager.PosUpdateVars();
      this.vehiclesWaitUpdatesFrequency = new UpdateLimit(1000L);
      this.oldModels = new ArrayList();
      this.curModels = new ArrayList();
      this.VehiclePhysicSyncPacketLimit = new UpdateLimit(500L);
   }

   private void noise(String var1) {
      if (Core.bDebug) {
      }

   }

   public void registerVehicle(BaseVehicle var1) {
      this.IDToVehicle.put(var1.VehicleID, var1);
      this.idMapDirty = true;
   }

   public void unregisterVehicle(BaseVehicle var1) {
      this.IDToVehicle.remove(var1.VehicleID);
      this.idMapDirty = true;
   }

   public BaseVehicle getVehicleByID(short var1) {
      return this.IDToVehicle.get(var1);
   }

   public ArrayList getVehicles() {
      if (this.idMapDirty) {
         this.vehicles.clear();
         this.IDToVehicle.toArrayList(this.vehicles);
         this.idMapDirty = false;
      }

      return this.vehicles;
   }

   public void removeFromWorld(BaseVehicle var1) {
      if (var1.VehicleID != -1) {
         short var2 = var1.VehicleID;
         if (var1.trace) {
            this.noise("removeFromWorld vehicle id=" + var1.VehicleID);
         }

         this.unregisterVehicle(var1);
         if (GameServer.bServer) {
            for(int var3 = 0; var3 < GameServer.udpEngine.connections.size(); ++var3) {
               UdpConnection var4 = (UdpConnection)GameServer.udpEngine.connections.get(var3);
               if (var1.connectionState[var4.index] != null) {
                  ByteBufferWriter var5 = var4.startPacket();
                  PacketTypes.PacketType.Vehicles.doPacket(var5);
                  var5.bb.put((byte)8);
                  var5.bb.putShort(var1.VehicleID);
                  PacketTypes.PacketType.Vehicles.send(var4);
               }
            }
         }

         if (GameClient.bClient) {
            var1.serverRemovedFromWorld = false;
            if (var1.interpolation != null) {
               var1.interpolation.poolData();
            }
         }
      }

   }

   public void serverUpdate() {
      ArrayList var1 = IsoWorld.instance.CurrentCell.getVehicles();

      int var2;
      for(var2 = 0; var2 < this.connected.length; ++var2) {
         int var3;
         if (this.connected[var2] != null && !GameServer.udpEngine.connections.contains(this.connected[var2])) {
            this.noise("vehicles: dropped connection " + var2);

            for(var3 = 0; var3 < var1.size(); ++var3) {
               ((BaseVehicle)var1.get(var3)).connectionState[var2] = null;
            }

            this.connected[var2] = null;
         } else {
            for(var3 = 0; var3 < var1.size(); ++var3) {
               if (((BaseVehicle)var1.get(var3)).connectionState[var2] != null) {
                  BaseVehicle.ServerVehicleState var10000 = ((BaseVehicle)var1.get(var3)).connectionState[var2];
                  var10000.flags |= ((BaseVehicle)var1.get(var3)).updateFlags;
               }
            }
         }
      }

      for(var2 = 0; var2 < GameServer.udpEngine.connections.size(); ++var2) {
         UdpConnection var6 = (UdpConnection)GameServer.udpEngine.connections.get(var2);
         this.sendVehicles(var6);
         this.connected[var6.index] = var6;
      }

      for(var2 = 0; var2 < var1.size(); ++var2) {
         BaseVehicle var7 = (BaseVehicle)var1.get(var2);
         if ((var7.updateFlags & 19440) != 0) {
            for(int var4 = 0; var4 < var7.getPartCount(); ++var4) {
               VehiclePart var5 = var7.getPartByIndex(var4);
               var5.updateFlags = 0;
            }
         }

         var7.updateFlags = 0;
      }

   }

   private void sendVehicles(UdpConnection var1) {
      if (var1.isFullyConnected()) {
         this.send.clear();
         ArrayList var2 = IsoWorld.instance.CurrentCell.getVehicles();

         for(int var3 = 0; var3 < var2.size(); ++var3) {
            BaseVehicle var4 = (BaseVehicle)var2.get(var3);
            if (var4.VehicleID == -1) {
               var4.VehicleID = this.IDToVehicle.allocateID();
               this.registerVehicle(var4);
            }

            boolean var5 = var1.vehicles.contains(var4.VehicleID);
            if (var5 && !var1.RelevantTo(var4.x, var4.y, (float)(var1.ReleventRange * 10) * 2.0F)) {
               DebugLog.log("removed out-of-bounds vehicle.id=" + var4.VehicleID + " connection=" + var1.index);
               var1.vehicles.remove(var4.VehicleID);
               var5 = false;
            }

            if (var5 || var1.RelevantTo(var4.x, var4.y)) {
               if (var4.connectionState[var1.index] == null) {
                  var4.connectionState[var1.index] = new BaseVehicle.ServerVehicleState();
               }

               BaseVehicle.ServerVehicleState var6 = var4.connectionState[var1.index];
               if (!var5 || var6.shouldSend(var4)) {
                  this.send.add(var4);
                  var1.vehicles.add(var4.VehicleID);
               }
            }
         }

         if (!this.send.isEmpty()) {
            ByteBufferWriter var18 = var1.startPacket();
            PacketTypes.PacketType var19;
            if (this.VehiclePhysicSyncPacketLimit.Check()) {
               var19 = PacketTypes.PacketType.Vehicles;
            } else {
               var19 = PacketTypes.PacketType.VehiclesUnreliable;
            }

            var19.doPacket(var18);

            try {
               ByteBuffer var20 = var18.bb;
               var20.put((byte)5);
               var20.putShort((short)this.send.size());

               for(int var21 = 0; var21 < this.send.size(); ++var21) {
                  BaseVehicle var7 = (BaseVehicle)this.send.get(var21);
                  BaseVehicle.ServerVehicleState var8 = var7.connectionState[var1.index];
                  var20.putShort(var7.VehicleID);
                  var20.putShort(var8.flags);
                  var20.putFloat(var7.x);
                  var20.putFloat(var7.y);
                  var20.putFloat(var7.jniTransform.origin.y);
                  int var9 = var20.position();
                  var20.putShort((short)0);
                  int var10 = var20.position();
                  boolean var11 = (var8.flags & 1) != 0;
                  int var22;
                  int var25;
                  if (var11) {
                     var8.flags = (short)(var8.flags & -2);
                     var7.netPlayerServerSendAuthorisation(var20);
                     var8.setAuthorization(var7);
                     var22 = var20.position();
                     var20.putShort((short)0);
                     var7.save(var20);
                     var25 = var20.position();
                     var20.position(var22);
                     var20.putShort((short)(var25 - var22));
                     var20.position(var25);
                     int var23 = var20.position();
                     int var15 = var20.position() - var10;
                     var20.position(var9);
                     var20.putShort((short)var15);
                     var20.position(var23);
                     this.writePositionOrientation(var20, var7);
                     var8.x = var7.x;
                     var8.y = var7.y;
                     var8.z = var7.jniTransform.origin.y;
                     var8.orient.set((Quaternionfc)var7.savedRot);
                  } else {
                     if ((var8.flags & 16384) != 0) {
                        var7.netPlayerServerSendAuthorisation(var20);
                        var8.setAuthorization(var7);
                     }

                     if ((var8.flags & 2) != 0) {
                        this.writePositionOrientation(var20, var7);
                        var8.x = var7.x;
                        var8.y = var7.y;
                        var8.z = var7.jniTransform.origin.y;
                        var8.orient.set((Quaternionfc)var7.savedRot);
                     }

                     if ((var8.flags & 4) != 0) {
                        var20.put((byte)var7.engineState.ordinal());
                        var20.putInt(var7.engineLoudness);
                        var20.putInt(var7.enginePower);
                        var20.putInt(var7.engineQuality);
                     }

                     if ((var8.flags & 4096) != 0) {
                        var20.put((byte)(var7.isHotwired() ? 1 : 0));
                        var20.put((byte)(var7.isHotwiredBroken() ? 1 : 0));
                        var20.put((byte)(var7.isKeysInIgnition() ? 1 : 0));
                        var20.put((byte)(var7.isKeyIsOnDoor() ? 1 : 0));
                        InventoryItem var12 = var7.getCurrentKey();
                        if (var12 == null) {
                           var20.put((byte)0);
                        } else {
                           var20.put((byte)1);
                           var12.saveWithSize(var20, false);
                        }

                        var20.putFloat(var7.getRust());
                        var20.putFloat(var7.getBloodIntensity("Front"));
                        var20.putFloat(var7.getBloodIntensity("Rear"));
                        var20.putFloat(var7.getBloodIntensity("Left"));
                        var20.putFloat(var7.getBloodIntensity("Right"));
                     }

                     if ((var8.flags & 8) != 0) {
                        var20.put((byte)(var7.getHeadlightsOn() ? 1 : 0));
                        var20.put((byte)(var7.getStoplightsOn() ? 1 : 0));

                        for(var22 = 0; var22 < var7.getLightCount(); ++var22) {
                           var20.put((byte)(var7.getLightByIndex(var22).getLight().getActive() ? 1 : 0));
                        }
                     }

                     if ((var8.flags & 1024) != 0) {
                        var20.put((byte)(var7.soundHornOn ? 1 : 0));
                        var20.put((byte)(var7.soundBackMoveOn ? 1 : 0));
                        var20.put((byte)var7.lightbarLightsMode.get());
                        var20.put((byte)var7.lightbarSirenMode.get());
                     }

                     VehiclePart var13;
                     if ((var8.flags & 2048) != 0) {
                        for(var22 = 0; var22 < var7.getPartCount(); ++var22) {
                           var13 = var7.getPartByIndex(var22);
                           if ((var13.updateFlags & 2048) != 0) {
                              var20.put((byte)var22);
                              var20.putInt(var13.getCondition());
                           }
                        }

                        var20.put((byte)-1);
                     }

                     if ((var8.flags & 16) != 0) {
                        for(var22 = 0; var22 < var7.getPartCount(); ++var22) {
                           var13 = var7.getPartByIndex(var22);
                           if ((var13.updateFlags & 16) != 0) {
                              var20.put((byte)var22);
                              var13.getModData().save(var20);
                           }
                        }

                        var20.put((byte)-1);
                     }

                     InventoryItem var14;
                     if ((var8.flags & 32) != 0) {
                        for(var22 = 0; var22 < var7.getPartCount(); ++var22) {
                           var13 = var7.getPartByIndex(var22);
                           if ((var13.updateFlags & 32) != 0) {
                              var14 = var13.getInventoryItem();
                              if (var14 instanceof DrainableComboItem) {
                                 var20.put((byte)var22);
                                 var20.putFloat(((DrainableComboItem)var14).getUsedDelta());
                              }
                           }
                        }

                        var20.put((byte)-1);
                     }

                     if ((var8.flags & 128) != 0) {
                        for(var22 = 0; var22 < var7.getPartCount(); ++var22) {
                           var13 = var7.getPartByIndex(var22);
                           if ((var13.updateFlags & 128) != 0) {
                              var20.put((byte)var22);
                              var14 = var13.getInventoryItem();
                              if (var14 == null) {
                                 var20.put((byte)0);
                              } else {
                                 var20.put((byte)1);

                                 try {
                                    var13.getInventoryItem().saveWithSize(var20, false);
                                 } catch (Exception var16) {
                                    var16.printStackTrace();
                                 }
                              }
                           }
                        }

                        var20.put((byte)-1);
                     }

                     if ((var8.flags & 512) != 0) {
                        for(var22 = 0; var22 < var7.getPartCount(); ++var22) {
                           var13 = var7.getPartByIndex(var22);
                           if ((var13.updateFlags & 512) != 0) {
                              var20.put((byte)var22);
                              var13.getDoor().save(var20);
                           }
                        }

                        var20.put((byte)-1);
                     }

                     if ((var8.flags & 256) != 0) {
                        for(var22 = 0; var22 < var7.getPartCount(); ++var22) {
                           var13 = var7.getPartByIndex(var22);
                           if ((var13.updateFlags & 256) != 0) {
                              var20.put((byte)var22);
                              var13.getWindow().save(var20);
                           }
                        }

                        var20.put((byte)-1);
                     }

                     if ((var8.flags & 64) != 0) {
                        var20.put((byte)var7.models.size());

                        for(var22 = 0; var22 < var7.models.size(); ++var22) {
                           BaseVehicle.ModelInfo var24 = (BaseVehicle.ModelInfo)var7.models.get(var22);
                           var20.put((byte)var24.part.getIndex());
                           var20.put((byte)var24.part.getScriptPart().models.indexOf(var24.scriptModel));
                        }
                     }

                     if ((var8.flags & 8192) != 0) {
                        var20.putFloat((float)var7.engineSpeed);
                        var20.putFloat(var7.throttle);
                     }

                     var22 = var20.position();
                     var25 = var20.position() - var10;
                     var20.position(var9);
                     var20.putShort((short)var25);
                     var20.position(var22);
                  }
               }

               var19.send(var1);
            } catch (Exception var17) {
               var1.cancelPacket();
               var17.printStackTrace();
            }

         }
      }
   }

   public void serverPacket(ByteBuffer var1, UdpConnection var2) {
      byte var3 = var1.get();
      short var5;
      short var6;
      BaseVehicle var8;
      BaseVehicle var9;
      int var10;
      UdpConnection var11;
      ByteBufferWriter var12;
      short var13;
      byte var14;
      String var16;
      BaseVehicle var17;
      BaseVehicle var18;
      boolean var21;
      int var22;
      BaseVehicle var24;
      UdpConnection var25;
      IsoGameCharacter var27;
      int var30;
      IsoPlayer var31;
      int var32;
      UdpConnection var33;
      IsoPlayer var35;
      String var36;
      switch(var3) {
      case 1:
         var13 = var1.getShort();
         var14 = var1.get();
         var16 = GameWindow.ReadString(var1);
         var24 = this.IDToVehicle.get(var13);
         if (var24 != null) {
            var27 = var24.getCharacter(var14);
            if (var27 != null) {
               var24.setCharacterPosition(var27, var14, var16);
               this.sendPassengerPosition(var24, var14, var16, var2);
            }
         }
         break;
      case 2:
         var13 = var1.getShort();
         var14 = var1.get();
         var6 = var1.getShort();
         var24 = this.IDToVehicle.get(var13);
         if (var24 != null) {
            var27 = var24.getCharacter(var14);
            if (var27 != null) {
               var31 = (IsoPlayer)GameServer.IDToPlayerMap.get(var6);
               var36 = var31 == null ? "unknown player" : var31.getUsername();
               DebugLog.log(var36 + " got in same seat as " + ((IsoPlayer)var27).getUsername());
               return;
            }

            for(var30 = 0; var30 < GameServer.udpEngine.connections.size(); ++var30) {
               var33 = (UdpConnection)GameServer.udpEngine.connections.get(var30);

               for(var32 = 0; var32 < 4; ++var32) {
                  var35 = var33.players[var32];
                  if (var35 != null && var35.OnlineID == var6) {
                     this.noise(var35.getUsername() + " got in vehicle " + var24.VehicleID + " seat " + var14);
                     var24.enter(var14, var35);
                     this.sendREnter(var24, var14, var35);
                     var24.authorizationServerOnSeat();
                     break;
                  }
               }
            }

            var31 = (IsoPlayer)GameServer.IDToPlayerMap.get(var6);
            if (var24.getVehicleTowing() != null && var24.getDriver() == var31) {
               var24.getVehicleTowing().setNetPlayerAuthorization((byte)3);
               var24.getVehicleTowing().netPlayerId = var31.OnlineID;
               var24.getVehicleTowing().netPlayerTimeout = 30;
            } else if (var24.getVehicleTowedBy() != null) {
               if (var24.getVehicleTowedBy().getDriver() != null) {
                  var24.setNetPlayerAuthorization((byte)3);
                  var24.netPlayerId = var24.getVehicleTowedBy().getDriver().getOnlineID();
                  var24.netPlayerTimeout = 30;
               } else {
                  var24.setNetPlayerAuthorization((byte)0);
                  var24.netPlayerId = -1;
               }
            }
         }
         break;
      case 3:
         var13 = var1.getShort();
         var5 = var1.getShort();
         var18 = this.IDToVehicle.get(var13);
         if (var18 != null) {
            for(var22 = 0; var22 < GameServer.udpEngine.connections.size(); ++var22) {
               var25 = (UdpConnection)GameServer.udpEngine.connections.get(var22);

               for(var30 = 0; var30 < 4; ++var30) {
                  IsoPlayer var34 = var25.players[var30];
                  if (var34 != null && var34.OnlineID == var5) {
                     var18.exit(var34);
                     this.sendRExit(var18, var34);
                     if (var18.getVehicleTowedBy() == null) {
                        var18.authorizationServerOnSeat();
                     }
                     break;
                  }
               }
            }
         }
         break;
      case 4:
         var13 = var1.getShort();
         var14 = var1.get();
         var6 = var1.getShort();
         var24 = this.IDToVehicle.get(var13);
         if (var24 != null) {
            var27 = var24.getCharacter(var14);
            if (var27 != null) {
               var31 = (IsoPlayer)GameServer.IDToPlayerMap.get(var6);
               var36 = var31 == null ? "unknown player" : var31.getUsername();
               DebugLog.log(var36 + " switched to same seat as " + ((IsoPlayer)var27).getUsername());
               return;
            }

            for(var30 = 0; var30 < GameServer.udpEngine.connections.size(); ++var30) {
               var33 = (UdpConnection)GameServer.udpEngine.connections.get(var30);

               for(var32 = 0; var32 < 4; ++var32) {
                  var35 = var33.players[var32];
                  if (var35 != null && var35.OnlineID == var6) {
                     var24.switchSeat(var35, var14);
                     this.sendSwichSeat(var24, var14, var35);
                     if (var24.getDriver() == var35) {
                        var24.authorizationServerOnSeat();
                     }
                     break;
                  }
               }
            }
         }
         break;
      case 5:
      case 6:
      case 7:
      case 8:
      case 10:
      case 13:
      default:
         this.noise("unknown vehicle packet " + var3);
         break;
      case 9:
         var13 = var1.getShort();
         var17 = this.IDToVehicle.get(var13);
         if (var17 != null) {
            var21 = var17.authorizationServerOnOwnerData(var2);
            if (var21) {
               float[] var28 = this.tempFloats;
               long var26 = var1.getLong();
               var17.physics.clientForce = var1.getFloat();

               for(var10 = 0; var10 < var28.length; ++var10) {
                  var28[var10] = var1.getFloat();
               }

               var17.netLinearVelocity.x = var28[7];
               var17.netLinearVelocity.y = var28[8];
               var17.netLinearVelocity.z = var28[9];
               WorldSimulation.instance.setOwnVehiclePhysics(var13, var28);
            }
         }
         break;
      case 11:
         var13 = var1.getShort();

         for(int var20 = 0; var20 < var13; ++var20) {
            var6 = var1.getShort();
            DebugLog.log(DebugType.Vehicle, "send full update for vehicle #" + var6 + " due to request");
            var24 = this.IDToVehicle.get(var6);
            if (var24 != null) {
               BaseVehicle.ServerVehicleState var10000 = var24.connectionState[var2.index];
               var10000.flags = (short)(var10000.flags | 1);
               this.sendVehicles(var2);
            }
         }

         return;
      case 12:
         var13 = var1.getShort();
         var17 = this.IDToVehicle.get(var13);
         if (var17 != null) {
            var17.updateFlags = (short)(var17.updateFlags | 2);
            this.sendVehicles(var2);
         }
         break;
      case 14:
         var13 = var1.getShort();
         float var15 = var1.getFloat();
         float var23 = var1.getFloat();
         var24 = this.IDToVehicle.get(var13);
         if (var24 != null) {
            var24.engineSpeed = (double)var15;
            var24.throttle = var23;
            var24.updateFlags = (short)(var24.updateFlags | 8192);
         }
         break;
      case 15:
         var13 = var1.getShort();
         var5 = var1.getShort();
         var21 = var1.get() == 1;
         var24 = this.IDToVehicle.get(var13);
         if (var24 != null) {
            var24.authorizationServerCollide(var5, var21);
         }
         break;
      case 16:
         var13 = var1.getShort();
         var14 = var1.get();
         var18 = this.IDToVehicle.get(var13);
         if (var18 != null) {
            for(var22 = 0; var22 < GameServer.udpEngine.connections.size(); ++var22) {
               var25 = (UdpConnection)GameServer.udpEngine.connections.get(var22);
               if (var25 != var2) {
                  ByteBufferWriter var29 = var25.startPacket();
                  PacketTypes.PacketType.Vehicles.doPacket(var29);
                  var29.bb.put((byte)16);
                  var29.bb.putShort(var18.VehicleID);
                  var29.bb.put(var14);
                  PacketTypes.PacketType.Vehicles.send(var25);
               }
            }
         }
         break;
      case 17:
         var13 = var1.getShort();
         var5 = var1.getShort();
         var16 = GameWindow.ReadString(var1);
         String var19 = GameWindow.ReadString(var1);
         var8 = this.IDToVehicle.get(var13);
         var9 = this.IDToVehicle.get(var5);
         if (var8 != null && var9 != null) {
            var8.addPointConstraint(var9, var16, var19);
            if (var8.getDriver() != null && var8.getVehicleTowing() != null) {
               var8.getVehicleTowing().setNetPlayerAuthorization((byte)3);
               var8.getVehicleTowing().netPlayerId = var8.getDriver().getOnlineID();
               var8.getVehicleTowing().netPlayerTimeout = 30;
            }

            for(var10 = 0; var10 < GameServer.udpEngine.connections.size(); ++var10) {
               var11 = (UdpConnection)GameServer.udpEngine.connections.get(var10);
               if (var11.getConnectedGUID() != var2.getConnectedGUID()) {
                  var12 = var11.startPacket();
                  PacketTypes.PacketType.Vehicles.doPacket(var12);
                  var12.bb.put((byte)17);
                  var12.bb.putShort(var8.VehicleID);
                  var12.bb.putShort(var9.VehicleID);
                  GameWindow.WriteString(var12.bb, var16);
                  GameWindow.WriteString(var12.bb, var19);
                  PacketTypes.PacketType.Vehicles.send(var11);
               }
            }
         }
         break;
      case 18:
         boolean var4 = var1.get() == 1;
         var5 = -1;
         var6 = -1;
         if (var4) {
            var5 = var1.getShort();
         }

         boolean var7 = var1.get() == 1;
         if (var7) {
            var6 = var1.getShort();
         }

         var8 = this.IDToVehicle.get(var5);
         var9 = this.IDToVehicle.get(var6);
         if (var8 != null || var9 != null) {
            if (var8 != null) {
               if (var8.getDriver() == null) {
                  var8.setNetPlayerAuthorization((byte)0);
                  var8.netPlayerId = -1;
               } else {
                  var8.setNetPlayerAuthorization((byte)3);
                  var8.netPlayerId = var8.getDriver().getOnlineID();
                  var8.netPlayerTimeout = 30;
               }

               var8.breakConstraint(true, true);
            }

            if (var9 != null) {
               if (var9.getDriver() == null) {
                  var9.setNetPlayerAuthorization((byte)0);
                  var9.netPlayerId = -1;
               } else {
                  var9.setNetPlayerAuthorization((byte)3);
                  var9.netPlayerId = var8.getDriver().getOnlineID();
                  var9.netPlayerTimeout = 30;
               }

               var9.breakConstraint(true, true);
            }

            for(var10 = 0; var10 < GameServer.udpEngine.connections.size(); ++var10) {
               var11 = (UdpConnection)GameServer.udpEngine.connections.get(var10);
               if (var11.getConnectedGUID() != var2.getConnectedGUID()) {
                  var12 = var11.startPacket();
                  PacketTypes.PacketType.Vehicles.doPacket(var12);
                  var12.bb.put((byte)18);
                  if (var8 != null) {
                     var12.bb.put((byte)1);
                     var12.bb.putShort(var8.VehicleID);
                  } else {
                     var12.bb.put((byte)0);
                  }

                  if (var9 != null) {
                     var12.bb.put((byte)1);
                     var12.bb.putShort(var9.VehicleID);
                  } else {
                     var12.bb.put((byte)0);
                  }

                  PacketTypes.PacketType.Vehicles.send(var11);
               }
            }
         }
      }

   }

   public static void serverSendVehiclesConfig(UdpConnection var0) {
      ByteBufferWriter var1 = var0.startPacket();
      PacketTypes.PacketType.Vehicles.doPacket(var1);
      var1.bb.put((byte)10);
      var1.bb.putShort((short)ServerOptions.getInstance().PhysicsDelay.getValue());
      PacketTypes.PacketType.Vehicles.send(var0);
   }

   private void vehiclePosUpdate(BaseVehicle var1, float[] var2) {
      byte var3 = 0;
      Transform var4 = this.posUpdateVars.transform;
      Vector3f var5 = this.posUpdateVars.vector3f;
      Quaternionf var6 = this.posUpdateVars.quatf;
      float[] var7 = this.posUpdateVars.wheelSteer;
      float[] var8 = this.posUpdateVars.wheelRotation;
      float[] var9 = this.posUpdateVars.wheelSkidInfo;
      float[] var10 = this.posUpdateVars.wheelSuspensionLength;
      int var23 = var3 + 1;
      float var11 = var2[var3] - WorldSimulation.instance.offsetX;
      float var12 = var2[var23++] - WorldSimulation.instance.offsetY;
      float var13 = var2[var23++];
      var4.origin.set(var11, var13, var12);
      float var14 = var2[var23++];
      float var15 = var2[var23++];
      float var16 = var2[var23++];
      float var17 = var2[var23++];
      var6.set(var14, var15, var16, var17);
      var6.normalize();
      var4.setRotation(var6);
      float var18 = var2[var23++];
      float var19 = var2[var23++];
      float var20 = var2[var23++];
      var5.set(var18, var19, var20);
      float var21 = var2[var23++];

      int var22;
      for(var22 = 0; var22 < 4; ++var22) {
         var7[var22] = var2[var23++];
         var8[var22] = var2[var23++];
         var9[var22] = var2[var23++];
         var10[var22] = var2[var23++];
      }

      var1.jniTransform.set(var4);
      var1.jniLinearVelocity.set((Vector3fc)var5);
      var1.netLinearVelocity.set((Vector3fc)var5);
      var1.jniTransform.basis.getScale(var5);
      if ((double)var5.x < 0.99D || (double)var5.y < 0.99D || (double)var5.z < 0.99D) {
         var1.jniTransform.basis.scale(1.0F / var5.x, 1.0F / var5.y, 1.0F / var5.z);
      }

      var1.jniSpeed = var1.jniLinearVelocity.length();

      for(var22 = 0; var22 < 4; ++var22) {
         var1.wheelInfo[var22].steering = var7[var22];
         var1.wheelInfo[var22].rotation = var8[var22];
         var1.wheelInfo[var22].skidInfo = var9[var22];
         var1.wheelInfo[var22].suspensionLength = var10[var22];
      }

      var1.polyDirty = true;
   }

   public void clientUpdate() {
      int var2;
      if (this.vehiclesWaitUpdatesFrequency.Check()) {
         if (this.vehiclesWaitUpdates.size() > 0) {
            ByteBufferWriter var1 = GameClient.connection.startPacket();
            PacketTypes.PacketType.Vehicles.doPacket(var1);
            var1.bb.put((byte)11);
            var1.bb.putShort((short)this.vehiclesWaitUpdates.size());

            for(var2 = 0; var2 < this.vehiclesWaitUpdates.size(); ++var2) {
               var1.bb.putShort(this.vehiclesWaitUpdates.get(var2));
            }

            PacketTypes.PacketType.Vehicles.send(GameClient.connection);
         }

         this.vehiclesWaitUpdates.clear();
      }

      ArrayList var10 = this.getVehicles();

      for(var2 = 0; var2 < var10.size(); ++var2) {
         BaseVehicle var3 = (BaseVehicle)var10.get(var2);
         if (!var3.isKeyboardControlled() && var3.getJoypad() == -1) {
            float[] var4 = this.tempFloats;
            if (var3.interpolation.interpolationDataGetPR(var4) && var3.netPlayerAuthorization != 3 && var3.netPlayerAuthorization != 1) {
               Bullet.setOwnVehiclePhysics(var3.VehicleID, var4);
               byte var5 = 0;
               int var11 = var5 + 1;
               float var6 = var4[var5];
               float var7 = var4[var11++];
               float var8 = var4[var11++];
               IsoGridSquare var9 = IsoWorld.instance.CurrentCell.getGridSquare((double)var6, (double)var7, 0.0D);
               this.clientUpdateVehiclePos(var3, var6, var7, var8, var9);
               var3.limitPhysicValid.BlockCheck();
               if (GameClient.bClient) {
                  this.vehiclePosUpdate(var3, var4);
               }
            }
         } else {
            var3.interpolation.setVehicleData(var3);
         }
      }

   }

   private void clientUpdateVehiclePos(BaseVehicle var1, float var2, float var3, float var4, IsoGridSquare var5) {
      var1.setX(var2);
      var1.setY(var3);
      var1.setZ(0.0F);
      var1.square = var5;
      var1.setCurrent(var5);
      if (var5 != null) {
         if (var1.chunk != null && var1.chunk != var5.chunk) {
            var1.chunk.vehicles.remove(var1);
         }

         var1.chunk = var1.square.chunk;
         if (!var1.chunk.vehicles.contains(var1)) {
            var1.chunk.vehicles.add(var1);
            IsoChunk.addFromCheckedVehicles(var1);
         }

         if (!var1.addedToWorld) {
            var1.addToWorld();
         }
      } else {
         var1.removeFromWorld();
         var1.removeFromSquare();
      }

      var1.polyDirty = true;
   }

   private void clientReceiveUpdateFull(ByteBuffer var1, short var2, float var3, float var4, float var5) throws IOException {
      byte var6 = var1.get();
      short var7 = var1.getShort();
      short var8 = var1.getShort();
      IsoGridSquare var9 = IsoWorld.instance.CurrentCell.getGridSquare((double)var3, (double)var4, 0.0D);
      if (this.IDToVehicle.containsKey(var2)) {
         BaseVehicle var16 = this.IDToVehicle.get(var2);
         this.noise("ERROR: got full update for KNOWN vehicle id=" + var2);
         var1.get();
         var1.get();
         this.tempVehicle.parts.clear();
         this.tempVehicle.load(var1, 186);
         if (var16.physics != null && (var16.getDriver() == null || !var16.getDriver().isLocal())) {
            this.tempTransform.setRotation(this.tempVehicle.savedRot);
            this.tempTransform.origin.set(var3 - WorldSimulation.instance.offsetX, var5, var4 - WorldSimulation.instance.offsetY);
            var16.setWorldTransform(this.tempTransform);
         }

         var16.netPlayerFromServerUpdate(var6, var7);
         this.clientUpdateVehiclePos(var16, var3, var4, var5, var9);
      } else {
         boolean var10 = var1.get() != 0;
         byte var11 = var1.get();
         if (!var10 || var11 != IsoObject.getFactoryVehicle().getClassID()) {
            DebugLog.log("Error: clientReceiveUpdateFull: packet broken");
         }

         BaseVehicle var12 = new BaseVehicle(IsoWorld.instance.CurrentCell);
         if (var12 == null || !(var12 instanceof BaseVehicle)) {
            return;
         }

         BaseVehicle var13 = (BaseVehicle)var12;
         var13.VehicleID = var2;
         var13.square = var9;
         var13.setCurrent(var9);
         var13.load(var1, 186);
         if (var9 != null) {
            var13.chunk = var13.square.chunk;
            var13.chunk.vehicles.add(var13);
            var13.addToWorld();
         }

         IsoChunk.addFromCheckedVehicles(var13);
         var13.netPlayerFromServerUpdate(var6, var7);
         this.registerVehicle(var13);

         for(int var14 = 0; var14 < IsoPlayer.numPlayers; ++var14) {
            IsoPlayer var15 = IsoPlayer.players[var14];
            if (var15 != null && !var15.isDead() && var15.getVehicle() == null) {
               IsoWorld.instance.CurrentCell.putInVehicle(var15);
            }
         }

         if (var13.trace) {
            this.noise("added vehicle id=" + var13.VehicleID + (var9 == null ? " (delayed)" : ""));
         }
      }

   }

   private void clientReceiveUpdate(ByteBuffer var1) throws IOException {
      short var2 = var1.getShort();
      short var3 = var1.getShort();
      float var4 = var1.getFloat();
      float var5 = var1.getFloat();
      float var6 = var1.getFloat();
      short var7 = var1.getShort();
      VehicleCache.vehicleUpdate(var2, var4, var5, 0.0F);
      IsoGridSquare var8 = IsoWorld.instance.CurrentCell.getGridSquare((double)var4, (double)var5, 0.0D);
      BaseVehicle var9 = this.IDToVehicle.get(var2);
      if (var9 == null && var8 == null) {
         if (var1.limit() > var1.position() + var7) {
            var1.position(var1.position() + var7);
         }

      } else {
         boolean var19;
         int var29;
         if (var9 != null && var8 == null) {
            var19 = true;

            for(var29 = 0; var29 < IsoPlayer.numPlayers; ++var29) {
               IsoPlayer var32 = IsoPlayer.players[var29];
               if (var32 != null && var32.getVehicle() == var9) {
                  var19 = false;
                  var32.setPosition(var4, var5, 0.0F);
                  this.sendReqestGetPosition(var2);
               }
            }

            if (var19) {
               var9.removeFromWorld();
               var9.removeFromSquare();
            }

            if (var1.limit() > var1.position() + var7) {
               var1.position(var1.position() + var7);
            }

         } else {
            int var20;
            int var28;
            if ((var3 & 1) != 0) {
               DebugLog.Vehicle.debugln("Receive full update for vehicle #" + var2);
               this.clientReceiveUpdateFull(var1, var2, var4, var5, var6);
               if (var9 == null) {
                  var9 = this.IDToVehicle.get(var2);
               }

               if (!var9.isKeyboardControlled() && var9.getJoypad() == -1) {
                  var1.getLong();
                  byte var35 = 0;
                  float[] var34 = this.tempFloats;
                  var20 = var35 + 1;
                  var34[var35] = var4;
                  var34[var20++] = var5;

                  for(var34[var20++] = var6; var20 < 10; var34[var20++] = var1.getFloat()) {
                  }

                  short var31 = var1.getShort();
                  var34[var20++] = (float)var31;

                  for(var28 = 0; var28 < var31; ++var28) {
                     var34[var20++] = var1.getFloat();
                     var34[var20++] = var1.getFloat();
                     var34[var20++] = var1.getFloat();
                     var34[var20++] = var1.getFloat();
                  }

                  Bullet.setOwnVehiclePhysics(var2, var34);
               } else if (var1.limit() > var1.position() + 102) {
                  var1.position(var1.position() + 102);
               }

               var20 = this.vehiclesWaitUpdates.indexOf(var2);
               if (var20 >= 0) {
                  this.vehiclesWaitUpdates.removeAt(var20);
               }

            } else if (var9 == null && var8 != null) {
               this.sendRequestGetFull(var2);
               if (var1.limit() > var1.position() + var7) {
                  var1.position(var1.position() + var7);
               }

            } else {
               byte var10;
               if ((var3 & 16384) != 0) {
                  var10 = var1.get();
                  short var11 = var1.getShort();
                  if (var9 != null) {
                     var9.netPlayerFromServerUpdate(var10, var11);
                  }
               }

               if ((var3 & 2) != 0) {
                  if (!var9.isKeyboardControlled() && var9.getJoypad() == -1) {
                     var9.interpolation.interpolationDataAdd(var1, var4, var5, var6);
                  } else if (var1.limit() > var1.position() + 102) {
                     var1.position(var1.position() + 102);
                  }
               }

               if ((var3 & 4) != 0) {
                  this.noise("received update Engine id=" + var2);
                  var10 = var1.get();
                  if (var10 >= 0 && var10 < BaseVehicle.engineStateTypes.Values.length) {
                     switch(BaseVehicle.engineStateTypes.Values[var10]) {
                     case Idle:
                        var9.engineDoIdle();
                     case Starting:
                     default:
                        break;
                     case RetryingStarting:
                        var9.engineDoRetryingStarting();
                        break;
                     case StartingSuccess:
                        var9.engineDoStartingSuccess();
                        break;
                     case StartingFailed:
                        var9.engineDoStartingFailed();
                        break;
                     case StartingFailedNoPower:
                        var9.engineDoStartingFailedNoPower();
                        break;
                     case Running:
                        var9.engineDoRunning();
                        break;
                     case Stalling:
                        var9.engineDoStalling();
                        break;
                     case ShutingDown:
                        var9.engineDoShuttingDown();
                     }

                     var9.engineLoudness = var1.getInt();
                     var9.enginePower = var1.getInt();
                     var9.engineQuality = var1.getInt();
                  } else {
                     DebugLog.log("ERROR: VehicleManager.clientReceiveUpdate get invalid data");
                  }
               }

               boolean var21;
               if ((var3 & 4096) != 0) {
                  this.noise("received car properties update id=" + var2);
                  var9.setHotwired(var1.get() == 1);
                  var9.setHotwiredBroken(var1.get() == 1);
                  var19 = var1.get() == 1;
                  var21 = var1.get() == 1;
                  InventoryItem var12 = null;
                  if (var1.get() == 1) {
                     try {
                        var12 = InventoryItem.loadItem(var1, 186);
                     } catch (Exception var18) {
                        var18.printStackTrace();
                     }
                  }

                  var9.syncKeyInIgnition(var19, var21, var12);
                  var9.setRust(var1.getFloat());
                  var9.setBloodIntensity("Front", var1.getFloat());
                  var9.setBloodIntensity("Rear", var1.getFloat());
                  var9.setBloodIntensity("Left", var1.getFloat());
                  var9.setBloodIntensity("Right", var1.getFloat());
               }

               if ((var3 & 8) != 0) {
                  this.noise("received update Lights id=" + var2);
                  var9.setHeadlightsOn(var1.get() == 1);
                  var9.setStoplightsOn(var1.get() == 1);

                  for(var20 = 0; var20 < var9.getLightCount(); ++var20) {
                     var21 = var1.get() == 1;
                     var9.getLightByIndex(var20).getLight().setActive(var21);
                  }
               }

               byte var13;
               byte var23;
               if ((var3 & 1024) != 0) {
                  this.noise("received update Sounds id=" + var2);
                  var19 = var1.get() == 1;
                  var21 = var1.get() == 1;
                  var23 = var1.get();
                  var13 = var1.get();
                  if (var19 != var9.soundHornOn) {
                     if (var19) {
                        var9.onHornStart();
                     } else {
                        var9.onHornStop();
                     }
                  }

                  if (var21 != var9.soundBackMoveOn) {
                     if (var21) {
                        var9.onBackMoveSignalStart();
                     } else {
                        var9.onBackMoveSignalStop();
                     }
                  }

                  if (var9.lightbarLightsMode.get() != var23) {
                     var9.setLightbarLightsMode(var23);
                  }

                  if (var9.lightbarSirenMode.get() != var13) {
                     var9.setLightbarSirenMode(var13);
                  }
               }

               VehiclePart var22;
               if ((var3 & 2048) != 0) {
                  for(var10 = var1.get(); var10 != -1; var10 = var1.get()) {
                     var22 = var9.getPartByIndex(var10);
                     this.noise("received update PartCondition id=" + var2 + " part=" + var22.getId());
                     var22.updateFlags = (short)(var22.updateFlags | 2048);
                     var22.setCondition(var1.getInt());
                  }

                  var9.doDamageOverlay();
               }

               if ((var3 & 16) != 0) {
                  for(var10 = var1.get(); var10 != -1; var10 = var1.get()) {
                     var22 = var9.getPartByIndex(var10);
                     this.noise("received update PartModData id=" + var2 + " part=" + var22.getId());
                     var22.getModData().load((ByteBuffer)var1, 186);
                     if (var22.isContainer()) {
                        var22.setContainerContentAmount(var22.getContainerContentAmount());
                     }
                  }
               }

               float var24;
               VehiclePart var25;
               InventoryItem var26;
               if ((var3 & 32) != 0) {
                  for(var10 = var1.get(); var10 != -1; var10 = var1.get()) {
                     var24 = var1.getFloat();
                     var25 = var9.getPartByIndex(var10);
                     this.noise("received update PartUsedDelta id=" + var2 + " part=" + var25.getId());
                     var26 = var25.getInventoryItem();
                     if (var26 instanceof DrainableComboItem) {
                        ((DrainableComboItem)var26).setUsedDelta(var24);
                     }
                  }
               }

               if ((var3 & 128) != 0) {
                  for(var10 = var1.get(); var10 != -1; var10 = var1.get()) {
                     var22 = var9.getPartByIndex(var10);
                     this.noise("received update PartItem id=" + var2 + " part=" + var22.getId());
                     var22.updateFlags = (short)(var22.updateFlags | 128);
                     boolean var27 = var1.get() != 0;
                     if (var27) {
                        try {
                           var26 = InventoryItem.loadItem(var1, 186);
                        } catch (Exception var17) {
                           var17.printStackTrace();
                           return;
                        }

                        if (var26 != null) {
                           var22.setInventoryItem(var26);
                        }
                     } else {
                        var22.setInventoryItem((InventoryItem)null);
                     }

                     var28 = var22.getWheelIndex();
                     if (var28 != -1) {
                        var9.setTireRemoved(var28, !var27);
                     }

                     if (var22.isContainer()) {
                        LuaEventManager.triggerEvent("OnContainerUpdate");
                     }
                  }
               }

               if ((var3 & 512) != 0) {
                  for(var10 = var1.get(); var10 != -1; var10 = var1.get()) {
                     var22 = var9.getPartByIndex(var10);
                     this.noise("received update PartDoor id=" + var2 + " part=" + var22.getId());
                     var22.getDoor().load(var1, 186);
                  }

                  LuaEventManager.triggerEvent("OnContainerUpdate");
                  var9.doDamageOverlay();
               }

               if ((var3 & 256) != 0) {
                  for(var10 = var1.get(); var10 != -1; var10 = var1.get()) {
                     var22 = var9.getPartByIndex(var10);
                     this.noise("received update PartWindow id=" + var2 + " part=" + var22.getId());
                     var22.getWindow().load(var1, 186);
                  }

                  var9.doDamageOverlay();
               }

               if ((var3 & 64) != 0) {
                  this.oldModels.clear();
                  this.oldModels.addAll(var9.models);
                  this.curModels.clear();
                  var10 = var1.get();

                  for(var29 = 0; var29 < var10; ++var29) {
                     var23 = var1.get();
                     var13 = var1.get();
                     VehiclePart var14 = var9.getPartByIndex(var23);
                     VehicleScript.Model var15 = (VehicleScript.Model)var14.getScriptPart().models.get(var13);
                     BaseVehicle.ModelInfo var16 = var9.setModelVisible(var14, var15, true);
                     this.curModels.add(var16);
                  }

                  for(var29 = 0; var29 < this.oldModels.size(); ++var29) {
                     BaseVehicle.ModelInfo var30 = (BaseVehicle.ModelInfo)this.oldModels.get(var29);
                     if (!this.curModels.contains(var30)) {
                        var9.setModelVisible(var30.part, var30.scriptModel, false);
                     }
                  }

                  var9.doDamageOverlay();
               }

               if ((var3 & 8192) != 0) {
                  float var33 = var1.getFloat();
                  var24 = var1.getFloat();
                  if (!(var9.getDriver() instanceof IsoPlayer) || !((IsoPlayer)var9.getDriver()).isLocalPlayer()) {
                     var9.engineSpeed = (double)var33;
                     var9.throttle = var24;
                  }
               }

               var19 = false;

               for(var29 = 0; var29 < var9.getPartCount(); ++var29) {
                  var25 = var9.getPartByIndex(var29);
                  if (var25.updateFlags != 0) {
                     if ((var25.updateFlags & 2048) != 0 && (var25.updateFlags & 128) == 0) {
                        var25.doInventoryItemStats(var25.getInventoryItem(), var25.getMechanicSkillInstaller());
                        var19 = true;
                     }

                     var25.updateFlags = 0;
                  }
               }

               if (var19) {
                  var9.updatePartStats();
                  var9.updateBulletStats();
               }

            }
         }
      }
   }

   public void clientPacket(ByteBuffer var1) {
      byte var2 = var1.get();
      short var4;
      short var5;
      BaseVehicle var7;
      BaseVehicle var8;
      short var14;
      byte var15;
      String var10000;
      String var17;
      BaseVehicle var19;
      BaseVehicle var22;
      IsoPlayer var26;
      IsoGameCharacter var28;
      switch(var2) {
      case 1:
         var14 = var1.getShort();
         var15 = var1.get();
         var17 = GameWindow.ReadString(var1);
         var22 = this.IDToVehicle.get(var14);
         if (var22 != null) {
            IsoGameCharacter var27 = var22.getCharacter(var15);
            if (var27 != null) {
               var22.setCharacterPosition(var27, var15, var17);
            }
         }
         break;
      case 2:
      case 3:
      case 9:
      case 10:
      case 11:
      case 12:
      case 14:
      case 15:
      default:
         this.noise("unknown vehicle packet " + var2);
         break;
      case 4:
         var14 = var1.getShort();
         var15 = var1.get();
         var5 = var1.getShort();
         var22 = this.IDToVehicle.get(var14);
         if (var22 != null) {
            var26 = (IsoPlayer)GameClient.IDToPlayerMap.get(var5);
            if (var26 != null) {
               var28 = var22.getCharacter(var15);
               if (var28 == null) {
                  var22.switchSeatRSync(var26, var15);
               } else if (var26 != var28) {
                  var10000 = var26.getUsername();
                  DebugLog.log(var10000 + " switched to same seat as " + ((IsoPlayer)var28).getUsername());
               }
            }
         }
         break;
      case 5:
         if (this.tempVehicle == null || this.tempVehicle.getCell() != IsoWorld.instance.CurrentCell) {
            this.tempVehicle = new BaseVehicle(IsoWorld.instance.CurrentCell);
         }

         var14 = var1.getShort();

         for(int var23 = 0; var23 < var14; ++var23) {
            try {
               this.clientReceiveUpdate(var1);
            } catch (Exception var13) {
               var13.printStackTrace();
               return;
            }
         }

         return;
      case 6:
         var14 = var1.getShort();
         var15 = var1.get();
         var5 = var1.getShort();
         var22 = this.IDToVehicle.get(var14);
         if (var22 != null) {
            var26 = (IsoPlayer)GameClient.IDToPlayerMap.get(var5);
            if (var26 != null) {
               var28 = var22.getCharacter(var15);
               if (var28 == null) {
                  DebugLog.log(var26.getUsername() + " got in vehicle " + var22.VehicleID + " seat " + var15);
                  var22.enterRSync(var15, var26, var22);
               } else if (var26 != var28) {
                  var10000 = var26.getUsername();
                  DebugLog.log(var10000 + " got in same seat as " + ((IsoPlayer)var28).getUsername());
               }
            }
         }
         break;
      case 7:
         var14 = var1.getShort();
         var4 = var1.getShort();
         var19 = this.IDToVehicle.get(var14);
         if (var19 != null) {
            IsoPlayer var24 = (IsoPlayer)GameClient.IDToPlayerMap.get(var4);
            if (var24 != null) {
               var19.exitRSync(var24);
            }
         }
         break;
      case 8:
         var14 = var1.getShort();
         if (this.IDToVehicle.containsKey(var14)) {
            BaseVehicle var18 = this.IDToVehicle.get(var14);
            if (var18.trace) {
               this.noise("server removed vehicle id=" + var14);
            }

            var18.serverRemovedFromWorld = true;

            try {
               var18.removeFromWorld();
               var18.removeFromSquare();
            } finally {
               if (this.IDToVehicle.containsKey(var14)) {
                  this.unregisterVehicle(var18);
               }

            }
         }

         VehicleCache.remove(var14);
         break;
      case 13:
         var14 = var1.getShort();
         Vector3f var16 = new Vector3f();
         Vector3f var21 = new Vector3f();
         var16.x = var1.getFloat();
         var16.y = var1.getFloat();
         var16.z = var1.getFloat();
         var21.x = var1.getFloat();
         var21.y = var1.getFloat();
         var21.z = var1.getFloat();
         var22 = this.IDToVehicle.get(var14);
         if (var22 != null) {
            Bullet.applyCentralForceToVehicle(var22.VehicleID, var16.x, var16.y, var16.z);
            Vector3f var25 = var21.cross(var16);
            Bullet.applyTorqueToVehicle(var22.VehicleID, var25.x, var25.y, var25.z);
         }
         break;
      case 16:
         var14 = var1.getShort();
         var15 = var1.get();
         var19 = this.IDToVehicle.get(var14);
         if (var19 != null) {
            SoundManager.instance.PlayWorldSound("VehicleCrash", var19.square, 1.0F, 20.0F, 1.0F, true);
         }
         break;
      case 17:
         var14 = var1.getShort();
         var4 = var1.getShort();
         var17 = GameWindow.ReadString(var1);
         String var20 = GameWindow.ReadString(var1);
         var7 = this.IDToVehicle.get(var14);
         var8 = this.IDToVehicle.get(var4);
         if (var7 != null && var8 != null) {
            var7.addPointConstraint(var8, var17, var20, (Float)null, true);
         }
         break;
      case 18:
         boolean var3 = var1.get() == 1;
         var4 = -1;
         var5 = -1;
         if (var3) {
            var4 = var1.getShort();
         }

         boolean var6 = var1.get() == 1;
         if (var6) {
            var5 = var1.getShort();
         }

         var7 = this.IDToVehicle.get(var4);
         var8 = this.IDToVehicle.get(var5);
         if (var7 != null || var8 != null) {
            if (var7 != null) {
               var7.breakConstraint(true, true);
            }

            if (var8 != null) {
               var8.breakConstraint(true, true);
            }
         }
      }

   }

   public static void loadingClientPacket(ByteBuffer var0) {
      int var1 = var0.position();
      byte var2 = var0.get();
      switch(var2) {
      case 10:
         physicsDelay = var0.getShort();
      default:
         var0.position(var1);
      }
   }

   public void sendCollide(BaseVehicle var1, IsoGameCharacter var2, boolean var3) {
      if (var2 != null) {
         ByteBufferWriter var4 = GameClient.connection.startPacket();
         PacketTypes.PacketType.Vehicles.doPacket(var4);
         var4.bb.put((byte)15);
         var4.bb.putShort(var1.VehicleID);
         var4.bb.putShort(((IsoPlayer)var2).OnlineID);
         var4.bb.put((byte)(var3 ? 1 : 0));
         PacketTypes.PacketType.Vehicles.send(GameClient.connection);
      }
   }

   public void sendEnter(BaseVehicle var1, int var2, IsoGameCharacter var3) {
      ByteBufferWriter var4 = GameClient.connection.startPacket();
      PacketTypes.PacketType.Vehicles.doPacket(var4);
      var4.bb.put((byte)2);
      var4.bb.putShort(var1.VehicleID);
      var4.bb.put((byte)var2);
      var4.bb.putShort(((IsoPlayer)var3).OnlineID);
      PacketTypes.PacketType.Vehicles.send(GameClient.connection);
   }

   public static void sendSound(BaseVehicle var0, byte var1) {
      ByteBufferWriter var2 = GameClient.connection.startPacket();
      PacketTypes.PacketType.Vehicles.doPacket(var2);
      var2.bb.put((byte)16);
      var2.bb.putShort(var0.VehicleID);
      var2.bb.put(var1);
      PacketTypes.PacketType.Vehicles.send(GameClient.connection);
   }

   public static void sendSoundFromServer(BaseVehicle var0, byte var1) {
      for(int var2 = 0; var2 < GameServer.udpEngine.connections.size(); ++var2) {
         UdpConnection var3 = (UdpConnection)GameServer.udpEngine.connections.get(var2);
         ByteBufferWriter var4 = var3.startPacket();
         PacketTypes.PacketType.Vehicles.doPacket(var4);
         var4.bb.put((byte)16);
         var4.bb.putShort(var0.VehicleID);
         var4.bb.put(var1);
         PacketTypes.PacketType.Vehicles.send(var3);
      }

   }

   public void sendPassengerPosition(BaseVehicle var1, int var2, String var3) {
      ByteBufferWriter var4 = GameClient.connection.startPacket();
      PacketTypes.PacketType.Vehicles.doPacket(var4);
      var4.bb.put((byte)1);
      var4.bb.putShort(var1.VehicleID);
      var4.bb.put((byte)var2);
      var4.putUTF(var3);
      PacketTypes.PacketType.Vehicles.send(GameClient.connection);
   }

   public void sendPassengerPosition(BaseVehicle var1, int var2, String var3, UdpConnection var4) {
      for(int var5 = 0; var5 < GameServer.udpEngine.connections.size(); ++var5) {
         UdpConnection var6 = (UdpConnection)GameServer.udpEngine.connections.get(var5);
         if (var6 != var4) {
            ByteBufferWriter var7 = var6.startPacket();
            PacketTypes.PacketType.Vehicles.doPacket(var7);
            var7.bb.put((byte)1);
            var7.bb.putShort(var1.VehicleID);
            var7.bb.put((byte)var2);
            var7.putUTF(var3);
            PacketTypes.PacketType.Vehicles.send(var6);
         }
      }

   }

   public void sendRequestGetFull(short var1) {
      if (!this.vehiclesWaitUpdates.contains(var1)) {
         ByteBufferWriter var2 = GameClient.connection.startPacket();
         PacketTypes.PacketType.Vehicles.doPacket(var2);
         var2.bb.put((byte)11);
         var2.bb.putShort((short)1);
         var2.bb.putShort(var1);
         PacketTypes.PacketType.Vehicles.send(GameClient.connection);
         this.vehiclesWaitUpdates.add(var1);
         DebugLog.log(DebugType.Vehicle, "Send get full request for vehicle #" + var1);
      }
   }

   public void sendRequestGetFull(List var1) {
      if (var1 != null) {
         ByteBufferWriter var2 = GameClient.connection.startPacket();
         PacketTypes.PacketType.Vehicles.doPacket(var2);
         var2.bb.put((byte)11);
         var2.bb.putShort((short)var1.size());

         for(int var3 = 0; var3 < var1.size(); ++var3) {
            var2.bb.putShort(((VehicleCache)var1.get(var3)).id);
            this.vehiclesWaitUpdates.add(((VehicleCache)var1.get(var3)).id);
         }

         PacketTypes.PacketType.Vehicles.send(GameClient.connection);
      }
   }

   public void sendReqestGetPosition(short var1) {
      if (sendReqestGetPositionFrequency.Check()) {
         ByteBufferWriter var2 = GameClient.connection.startPacket();
         PacketTypes.PacketType.Vehicles.doPacket(var2);
         var2.bb.put((byte)12);
         var2.bb.putShort(var1);
         PacketTypes.PacketType.Vehicles.send(GameClient.connection);
         this.vehiclesWaitUpdates.add(var1);
      }
   }

   public void sendAddImpulse(BaseVehicle var1, Vector3f var2, Vector3f var3) {
      UdpConnection var4 = null;

      for(int var5 = 0; var5 < GameServer.udpEngine.connections.size() && var4 == null; ++var5) {
         UdpConnection var6 = (UdpConnection)GameServer.udpEngine.connections.get(var5);

         for(int var7 = 0; var7 < var6.players.length; ++var7) {
            IsoPlayer var8 = var6.players[var7];
            if (var8 != null && var8.getVehicle() != null && var8.getVehicle().VehicleID == var1.VehicleID) {
               var4 = var6;
               break;
            }
         }
      }

      if (var4 != null) {
         ByteBufferWriter var9 = var4.startPacket();
         PacketTypes.PacketType.Vehicles.doPacket(var9);
         var9.bb.put((byte)13);
         var9.bb.putShort(var1.VehicleID);
         var9.bb.putFloat(var2.x);
         var9.bb.putFloat(var2.y);
         var9.bb.putFloat(var2.z);
         var9.bb.putFloat(var3.x);
         var9.bb.putFloat(var3.y);
         var9.bb.putFloat(var3.z);
         PacketTypes.PacketType.Vehicles.send(var4);
      }

   }

   public void sendREnter(BaseVehicle var1, int var2, IsoGameCharacter var3) {
      for(int var4 = 0; var4 < GameServer.udpEngine.connections.size(); ++var4) {
         UdpConnection var5 = (UdpConnection)GameServer.udpEngine.connections.get(var4);
         ByteBufferWriter var6 = var5.startPacket();
         PacketTypes.PacketType.Vehicles.doPacket(var6);
         var6.bb.put((byte)6);
         var6.bb.putShort(var1.VehicleID);
         var6.bb.put((byte)var2);
         var6.bb.putShort(((IsoPlayer)var3).OnlineID);
         PacketTypes.PacketType.Vehicles.send(var5);
      }

   }

   public void sendSwichSeat(BaseVehicle var1, int var2, IsoGameCharacter var3) {
      if (GameClient.bClient) {
         ByteBufferWriter var4 = GameClient.connection.startPacket();
         PacketTypes.PacketType.Vehicles.doPacket(var4);
         var4.bb.put((byte)4);
         var4.bb.putShort(var1.VehicleID);
         var4.bb.put((byte)var2);
         var4.bb.putShort(((IsoPlayer)var3).OnlineID);
         PacketTypes.PacketType.Vehicles.send(GameClient.connection);
      }

      if (GameServer.bServer) {
         for(int var7 = 0; var7 < GameServer.udpEngine.connections.size(); ++var7) {
            UdpConnection var5 = (UdpConnection)GameServer.udpEngine.connections.get(var7);
            ByteBufferWriter var6 = var5.startPacket();
            PacketTypes.PacketType.Vehicles.doPacket(var6);
            var6.bb.put((byte)4);
            var6.bb.putShort(var1.VehicleID);
            var6.bb.put((byte)var2);
            var6.bb.putShort(((IsoPlayer)var3).OnlineID);
            PacketTypes.PacketType.Vehicles.send(var5);
         }
      }

   }

   public void sendExit(BaseVehicle var1, IsoGameCharacter var2) {
      ByteBufferWriter var3 = GameClient.connection.startPacket();
      PacketTypes.PacketType.Vehicles.doPacket(var3);
      var3.bb.put((byte)3);
      var3.bb.putShort(var1.VehicleID);
      var3.bb.putShort(((IsoPlayer)var2).OnlineID);
      PacketTypes.PacketType.Vehicles.send(GameClient.connection);
   }

   public void sendRExit(BaseVehicle var1, IsoGameCharacter var2) {
      for(int var3 = 0; var3 < GameServer.udpEngine.connections.size(); ++var3) {
         UdpConnection var4 = (UdpConnection)GameServer.udpEngine.connections.get(var3);
         ByteBufferWriter var5 = var4.startPacket();
         PacketTypes.PacketType.Vehicles.doPacket(var5);
         var5.bb.put((byte)7);
         var5.bb.putShort(var1.VehicleID);
         var5.bb.putShort(((IsoPlayer)var2).OnlineID);
         PacketTypes.PacketType.Vehicles.send(var4);
      }

   }

   public void sendPhysic(BaseVehicle var1) {
      ByteBufferWriter var2 = GameClient.connection.startPacket();
      PacketTypes.PacketType var3;
      if (this.VehiclePhysicSyncPacketLimit.Check()) {
         var3 = PacketTypes.PacketType.Vehicles;
      } else {
         var3 = PacketTypes.PacketType.VehiclesUnreliable;
      }

      var3.doPacket(var2);
      var2.bb.put((byte)9);
      var2.bb.putShort(var1.VehicleID);
      ByteBuffer var10000 = var2.bb;
      GameTime.getInstance();
      var10000.putLong(GameTime.getServerTime());
      var2.bb.putFloat(var1.physics.EngineForce - var1.physics.BrakingForce);
      if (WorldSimulation.instance.getOwnVehiclePhysics(var1.VehicleID, var2) != 1) {
         GameClient.connection.cancelPacket();
      } else {
         var3.send(GameClient.connection);
      }
   }

   public void sendEngineSound(BaseVehicle var1, float var2, float var3) {
      ByteBufferWriter var4 = GameClient.connection.startPacket();
      PacketTypes.PacketType.Vehicles.doPacket(var4);
      var4.bb.put((byte)14);
      var4.bb.putShort(var1.VehicleID);
      var4.bb.putFloat(var2);
      var4.bb.putFloat(var3);
      PacketTypes.PacketType.Vehicles.send(GameClient.connection);
   }

   public void sendTowing(BaseVehicle var1, BaseVehicle var2, String var3, String var4, Float var5) {
      ByteBufferWriter var6 = GameClient.connection.startPacket();
      PacketTypes.PacketType.Vehicles.doPacket(var6);
      var6.bb.put((byte)17);
      var6.bb.putShort(var1.VehicleID);
      var6.bb.putShort(var2.VehicleID);
      GameWindow.WriteString(var6.bb, var3);
      GameWindow.WriteString(var6.bb, var4);
      PacketTypes.PacketType.Vehicles.send(GameClient.connection);
   }

   public void sendDetachTowing(BaseVehicle var1, BaseVehicle var2) {
      ByteBufferWriter var3 = GameClient.connection.startPacket();
      PacketTypes.PacketType.Vehicles.doPacket(var3);
      var3.bb.put((byte)18);
      if (var1 != null) {
         var3.bb.put((byte)1);
         var3.bb.putShort(var1.VehicleID);
      } else {
         var3.bb.put((byte)0);
      }

      if (var2 != null) {
         var3.bb.put((byte)1);
         var3.bb.putShort(var2.VehicleID);
      } else {
         var3.bb.put((byte)0);
      }

      PacketTypes.PacketType.Vehicles.send(GameClient.connection);
   }

   private void writePositionOrientation(ByteBuffer var1, BaseVehicle var2) {
      var1.putLong(WorldSimulation.instance.time);
      Quaternionf var3 = var2.savedRot;
      Transform var4 = var2.getWorldTransform(this.tempTransform);
      var4.getRotation(var3);
      var1.putFloat(var3.x);
      var1.putFloat(var3.y);
      var1.putFloat(var3.z);
      var1.putFloat(var3.w);
      var1.putFloat(var2.netLinearVelocity.x);
      var1.putFloat(var2.netLinearVelocity.y);
      var1.putFloat(var2.netLinearVelocity.z);
      var1.putShort((short)var2.wheelInfo.length);

      for(int var5 = 0; var5 < var2.wheelInfo.length; ++var5) {
         var1.putFloat(var2.wheelInfo[var5].steering);
         var1.putFloat(var2.wheelInfo[var5].rotation);
         var1.putFloat(var2.wheelInfo[var5].skidInfo);
         var1.putFloat(var2.wheelInfo[var5].suspensionLength);
      }

   }

   public static final class PosUpdateVars {
      final Transform transform = new Transform();
      final Vector3f vector3f = new Vector3f();
      final Quaternionf quatf = new Quaternionf();
      final float[] wheelSteer = new float[4];
      final float[] wheelRotation = new float[4];
      final float[] wheelSkidInfo = new float[4];
      final float[] wheelSuspensionLength = new float[4];
   }

   public static final class VehiclePacket {
      public static final byte PassengerPosition = 1;
      public static final byte Enter = 2;
      public static final byte Exit = 3;
      public static final byte SwichSeat = 4;
      public static final byte Update = 5;
      public static final byte REnter = 6;
      public static final byte RExit = 7;
      public static final byte Remove = 8;
      public static final byte Physic = 9;
      public static final byte Config = 10;
      public static final byte RequestGetFull = 11;
      public static final byte RequestGetPosition = 12;
      public static final byte AddImpulse = 13;
      public static final byte EngineSound = 14;
      public static final byte Collide = 15;
      public static final byte Sound = 16;
      public static final byte TowingCar = 17;
      public static final byte DetachTowingCar = 18;
      public static final byte Sound_Crash = 1;
   }
}
