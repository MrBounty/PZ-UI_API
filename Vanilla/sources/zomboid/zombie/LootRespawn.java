package zombie;

import java.util.ArrayList;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.CompressIdenticalItems;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemPickerJava;
import zombie.iso.BuildingDef;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoCompost;
import zombie.iso.objects.IsoDeadBody;
import zombie.iso.objects.IsoThumpable;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerMap;
import zombie.network.ServerOptions;

public final class LootRespawn {
   private static int LastRespawnHour = -1;
   private static final ArrayList existingItems = new ArrayList();
   private static final ArrayList newItems = new ArrayList();

   public static void update() {
      if (!GameClient.bClient) {
         int var0 = getRespawnInterval();
         if (var0 > 0) {
            int var1 = 7 + (int)(GameTime.getInstance().getWorldAgeHours() / (double)var0) * var0;
            if (LastRespawnHour < var1) {
               LastRespawnHour = var1;
               int var2;
               int var4;
               int var5;
               IsoChunk var6;
               if (GameServer.bServer) {
                  for(var2 = 0; var2 < ServerMap.instance.LoadedCells.size(); ++var2) {
                     ServerMap.ServerCell var3 = (ServerMap.ServerCell)ServerMap.instance.LoadedCells.get(var2);
                     if (var3.bLoaded) {
                        for(var4 = 0; var4 < 5; ++var4) {
                           for(var5 = 0; var5 < 5; ++var5) {
                              var6 = var3.chunks[var5][var4];
                              checkChunk(var6);
                           }
                        }
                     }
                  }
               } else {
                  for(var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
                     IsoChunkMap var7 = IsoWorld.instance.CurrentCell.ChunkMap[var2];
                     if (!var7.ignore) {
                        for(var4 = 0; var4 < IsoChunkMap.ChunkGridWidth; ++var4) {
                           for(var5 = 0; var5 < IsoChunkMap.ChunkGridWidth; ++var5) {
                              var6 = var7.getChunk(var5, var4);
                              checkChunk(var6);
                           }
                        }
                     }
                  }
               }

            }
         }
      }
   }

   public static void Reset() {
      LastRespawnHour = -1;
   }

   public static void chunkLoaded(IsoChunk var0) {
      if (!GameClient.bClient) {
         checkChunk(var0);
      }
   }

   private static void checkChunk(IsoChunk var0) {
      if (var0 != null) {
         int var1 = getRespawnInterval();
         if (var1 > 0) {
            if (!(GameTime.getInstance().getWorldAgeHours() < (double)var1)) {
               int var2 = 7 + (int)(GameTime.getInstance().getWorldAgeHours() / (double)var1) * var1;
               if (var0.lootRespawnHour > var2) {
                  var0.lootRespawnHour = var2;
               }

               if (var0.lootRespawnHour < var2) {
                  var0.lootRespawnHour = var2;
                  respawnInChunk(var0);
               }
            }
         }
      }
   }

   private static int getRespawnInterval() {
      if (GameServer.bServer) {
         return ServerOptions.instance.HoursForLootRespawn.getValue();
      } else {
         if (!GameClient.bClient) {
            int var0 = SandboxOptions.instance.LootRespawn.getValue();
            if (var0 == 1) {
               return 0;
            }

            if (var0 == 2) {
               return 24;
            }

            if (var0 == 3) {
               return 168;
            }

            if (var0 == 4) {
               return 720;
            }

            if (var0 == 5) {
               return 1440;
            }
         }

         return 0;
      }
   }

   private static void respawnInChunk(IsoChunk var0) {
      boolean var1 = GameServer.bServer && ServerOptions.instance.ConstructionPreventsLootRespawn.getValue();
      int var2 = SandboxOptions.instance.SeenHoursPreventLootRespawn.getValue();
      double var3 = GameTime.getInstance().getWorldAgeHours();

      for(int var5 = 0; var5 < 10; ++var5) {
         for(int var6 = 0; var6 < 10; ++var6) {
            IsoGridSquare var7 = var0.getGridSquare(var6, var5, 0);
            IsoMetaGrid.Zone var8 = var7 == null ? null : var7.getZone();
            if (var8 != null && ("TownZone".equals(var8.getType()) || "TownZones".equals(var8.getType()) || "TrailerPark".equals(var8.getType())) && (!var1 || !var8.haveConstruction) && (var2 <= 0 || !(var8.getHoursSinceLastSeen() <= (float)var2))) {
               if (var7.getBuilding() != null) {
                  BuildingDef var9 = var7.getBuilding().getDef();
                  if (var9 != null) {
                     if ((double)var9.lootRespawnHour > var3) {
                        var9.lootRespawnHour = 0;
                     }

                     if (var9.lootRespawnHour < var0.lootRespawnHour) {
                        var9.setKeySpawned(0);
                        var9.lootRespawnHour = var0.lootRespawnHour;
                     }
                  }
               }

               for(int var16 = 0; var16 < 8; ++var16) {
                  var7 = var0.getGridSquare(var6, var5, var16);
                  if (var7 != null) {
                     int var10 = var7.getObjects().size();
                     IsoObject[] var11 = (IsoObject[])var7.getObjects().getElements();

                     for(int var12 = 0; var12 < var10; ++var12) {
                        IsoObject var13 = var11[var12];
                        if (!(var13 instanceof IsoDeadBody) && !(var13 instanceof IsoThumpable) && !(var13 instanceof IsoCompost)) {
                           for(int var14 = 0; var14 < var13.getContainerCount(); ++var14) {
                              ItemContainer var15 = var13.getContainerByIndex(var14);
                              if (var15.bExplored && var15.isHasBeenLooted()) {
                                 respawnInContainer(var13, var15);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

   }

   private static void respawnInContainer(IsoObject var0, ItemContainer var1) {
      if (var1 != null && var1.getItems() != null) {
         int var2 = var1.getItems().size();
         int var3 = 5;
         if (GameServer.bServer) {
            var3 = ServerOptions.instance.MaxItemsForLootRespawn.getValue();
         }

         if (var2 < var3) {
            existingItems.clear();
            existingItems.addAll(var1.getItems());
            ItemPickerJava.fillContainer(var1, (IsoPlayer)null);
            ArrayList var4 = var1.getItems();
            if (var4 != null && var2 != var4.size()) {
               var1.setHasBeenLooted(false);
               newItems.clear();

               int var5;
               for(var5 = 0; var5 < var4.size(); ++var5) {
                  InventoryItem var6 = (InventoryItem)var4.get(var5);
                  if (!existingItems.contains(var6)) {
                     newItems.add(var6);
                     var6.setAge(0.0F);
                  }
               }

               ItemPickerJava.updateOverlaySprite(var0);
               if (GameServer.bServer) {
                  for(var5 = 0; var5 < GameServer.udpEngine.connections.size(); ++var5) {
                     UdpConnection var10 = (UdpConnection)GameServer.udpEngine.connections.get(var5);
                     if (var10.RelevantTo((float)var0.square.x, (float)var0.square.y)) {
                        ByteBufferWriter var7 = var10.startPacket();
                        PacketTypes.PacketType.AddInventoryItemToContainer.doPacket(var7);
                        var7.putShort((short)2);
                        var7.putInt((int)var0.getX());
                        var7.putInt((int)var0.getY());
                        var7.putInt((int)var0.getZ());
                        var7.putByte((byte)var0.getObjectIndex());
                        var7.putByte((byte)var0.getContainerIndex(var1));

                        try {
                           CompressIdenticalItems.save(var7.bb, newItems, (IsoGameCharacter)null);
                        } catch (Exception var9) {
                           var9.printStackTrace();
                        }

                        PacketTypes.PacketType.AddInventoryItemToContainer.send(var10);
                     }
                  }
               }

            }
         }
      }
   }
}
