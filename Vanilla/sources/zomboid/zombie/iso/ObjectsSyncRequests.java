package zombie.iso;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.SystemDisabler;
import zombie.Lua.LuaEventManager;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.core.raknet.UdpEngine;
import zombie.debug.DebugLog;
import zombie.inventory.ItemContainer;
import zombie.iso.objects.IsoLightSwitch;
import zombie.network.GameClient;
import zombie.network.PacketTypes;
import zombie.network.ServerMap;
import zombie.network.WorldItemTypes;

public final class ObjectsSyncRequests {
   public static final short ClientSendChunkHashes = 1;
   public static final short ServerSendGridSquareHashes = 2;
   public static final short ClientSendGridSquareRequest = 3;
   public static final short ServerSendGridSquareObjectsHashes = 4;
   public static final short ClientSendObjectRequests = 5;
   public static final short ServerSendObject = 6;
   public ArrayList requestsSyncIsoChunk;
   public ArrayList requestsSyncIsoGridSquare;
   public ArrayList requestsSyncIsoObject;
   public long timeout = 1000L;

   public ObjectsSyncRequests(boolean var1) {
      if (var1) {
         this.requestsSyncIsoChunk = new ArrayList();
         this.requestsSyncIsoGridSquare = new ArrayList();
         this.requestsSyncIsoObject = new ArrayList();
      } else {
         this.requestsSyncIsoGridSquare = new ArrayList();
      }

   }

   static int getObjectInsertIndex(long[] var0, long[] var1, long var2) {
      if (var2 == var1[0]) {
         return 0;
      } else {
         int var4;
         for(var4 = 0; var4 < var0.length; ++var4) {
            if (var0[var4] == var2) {
               return -1;
            }
         }

         var4 = 0;

         for(int var5 = 0; var5 < var1.length; ++var5) {
            if (var4 < var0.length && var1[var5] == var0[var4]) {
               ++var4;
            }

            if (var1[var5] == var2) {
               return var4;
            }
         }

         return -1;
      }
   }

   public void putRequestSyncIsoChunk(IsoChunk var1) {
      if (!GameClient.bClient || SystemDisabler.doWorldSyncEnable) {
         ObjectsSyncRequests.SyncIsoChunk var2 = new ObjectsSyncRequests.SyncIsoChunk();
         var2.x = var1.wx;
         var2.y = var1.wy;
         var2.hashCodeObjects = var1.getHashCodeObjects();
         var2.reqTime = 0L;
         var2.reqCount = 0;
         synchronized(this.requestsSyncIsoChunk) {
            this.requestsSyncIsoChunk.add(var2);
         }
      }
   }

   public void putRequestSyncItemContainer(ItemContainer var1) {
      if (var1 != null && var1.parent != null && var1.parent.square != null) {
         this.putRequestSyncIsoGridSquare(var1.parent.square);
      }
   }

   public void putRequestSyncIsoGridSquare(IsoGridSquare var1) {
      if (var1 != null) {
         ObjectsSyncRequests.SyncIsoGridSquare var2 = new ObjectsSyncRequests.SyncIsoGridSquare();
         var2.x = var1.x;
         var2.y = var1.y;
         var2.z = var1.z;
         var2.reqTime = 0L;
         var2.reqCount = 0;
         synchronized(this.requestsSyncIsoGridSquare) {
            if (!this.requestsSyncIsoGridSquare.contains(var1)) {
               this.requestsSyncIsoGridSquare.add(var2);
            } else {
               DebugLog.log("Warning: [putRequestSyncIsoGridSquare] Tryed to add dublicate object.");
            }

         }
      }
   }

   public void sendRequests(UdpConnection var1) {
      if (SystemDisabler.doWorldSyncEnable) {
         ByteBufferWriter var2;
         ByteBuffer var3;
         int var4;
         int var5;
         int var6;
         int var7;
         if (this.requestsSyncIsoChunk != null && this.requestsSyncIsoChunk.size() != 0) {
            var2 = var1.startPacket();
            PacketTypes.PacketType.SyncObjects.doPacket(var2);
            var2.putShort((short)1);
            var3 = var2.bb;
            var4 = var3.position();
            var2.putShort((short)0);
            var5 = 0;
            synchronized(this.requestsSyncIsoChunk) {
               for(var7 = this.requestsSyncIsoChunk.size() - 1; var7 >= 0; --var7) {
                  ObjectsSyncRequests.SyncIsoChunk var8 = (ObjectsSyncRequests.SyncIsoChunk)this.requestsSyncIsoChunk.get(var7);
                  if (var8.reqCount > 3) {
                     this.requestsSyncIsoChunk.remove(var7);
                  } else {
                     if (var8.reqTime == 0L) {
                        var8.reqTime = System.currentTimeMillis();
                        ++var5;
                        var3.putInt(var8.x);
                        var3.putInt(var8.y);
                        var3.putLong(var8.hashCodeObjects);
                        ++var8.reqCount;
                     }

                     if (System.currentTimeMillis() - var8.reqTime >= this.timeout) {
                        var8.reqTime = System.currentTimeMillis();
                        ++var5;
                        var3.putInt(var8.x);
                        var3.putInt(var8.y);
                        var3.putLong(var8.hashCodeObjects);
                        ++var8.reqCount;
                     }

                     if (var5 >= 5) {
                        break;
                     }
                  }
               }
            }

            if (var5 == 0) {
               GameClient.connection.cancelPacket();
               return;
            }

            var6 = var3.position();
            var3.position(var4);
            var3.putShort((short)var5);
            var3.position(var6);
            PacketTypes.PacketType.SyncObjects.send(GameClient.connection);
         }

         if (this.requestsSyncIsoGridSquare != null && this.requestsSyncIsoGridSquare.size() != 0) {
            var2 = var1.startPacket();
            PacketTypes.PacketType.SyncObjects.doPacket(var2);
            var2.putShort((short)3);
            var3 = var2.bb;
            var4 = var3.position();
            var2.putShort((short)0);
            var5 = 0;
            synchronized(this.requestsSyncIsoGridSquare) {
               for(var7 = 0; var7 < this.requestsSyncIsoGridSquare.size(); ++var7) {
                  ObjectsSyncRequests.SyncIsoGridSquare var15 = (ObjectsSyncRequests.SyncIsoGridSquare)this.requestsSyncIsoGridSquare.get(var7);
                  if (var15.reqCount > 3) {
                     this.requestsSyncIsoGridSquare.remove(var7);
                     --var7;
                  } else {
                     if (var15.reqTime == 0L) {
                        var15.reqTime = System.currentTimeMillis();
                        ++var5;
                        var3.putInt(var15.x);
                        var3.putInt(var15.y);
                        var3.put((byte)var15.z);
                        ++var15.reqCount;
                     }

                     if (System.currentTimeMillis() - var15.reqTime >= this.timeout) {
                        var15.reqTime = System.currentTimeMillis();
                        ++var5;
                        var3.putInt(var15.x);
                        var3.putInt(var15.y);
                        var3.put((byte)var15.z);
                        ++var15.reqCount;
                     }

                     if (var5 >= 100) {
                        break;
                     }
                  }
               }
            }

            if (var5 == 0) {
               GameClient.connection.cancelPacket();
               return;
            }

            var6 = var3.position();
            var3.position(var4);
            var3.putShort((short)var5);
            var3.position(var6);
            PacketTypes.PacketType.SyncObjects.send(GameClient.connection);
         }

         if (this.requestsSyncIsoObject != null && this.requestsSyncIsoObject.size() != 0) {
            var2 = var1.startPacket();
            PacketTypes.PacketType.SyncObjects.doPacket(var2);
            var2.putShort((short)5);
            var3 = var2.bb;
            var4 = var3.position();
            var2.putShort((short)0);
            var5 = 0;
            synchronized(this.requestsSyncIsoObject) {
               for(var7 = 0; var7 < this.requestsSyncIsoObject.size(); ++var7) {
                  ObjectsSyncRequests.SyncIsoObject var16 = (ObjectsSyncRequests.SyncIsoObject)this.requestsSyncIsoObject.get(var7);
                  if (var16.reqCount > 3) {
                     this.requestsSyncIsoObject.remove(var7);
                     --var7;
                  } else {
                     if (var16.reqTime == 0L) {
                        var16.reqTime = System.currentTimeMillis();
                        ++var5;
                        var3.putInt(var16.x);
                        var3.putInt(var16.y);
                        var3.put((byte)var16.z);
                        var3.putLong(var16.hash);
                        ++var16.reqCount;
                     }

                     if (System.currentTimeMillis() - var16.reqTime >= this.timeout) {
                        var16.reqTime = System.currentTimeMillis();
                        ++var5;
                        var3.putInt(var16.x);
                        var3.putInt(var16.y);
                        var3.put((byte)var16.z);
                        var3.putLong(var16.hash);
                        ++var16.reqCount;
                     }

                     if (var5 >= 100) {
                        break;
                     }
                  }
               }
            }

            if (var5 == 0) {
               GameClient.connection.cancelPacket();
               return;
            }

            var6 = var3.position();
            var3.position(var4);
            var3.putShort((short)var5);
            var3.position(var6);
            PacketTypes.PacketType.SyncObjects.send(GameClient.connection);
         }

      }
   }

   public void receiveSyncIsoChunk(int var1, int var2) {
      synchronized(this.requestsSyncIsoChunk) {
         for(int var4 = 0; var4 < this.requestsSyncIsoChunk.size(); ++var4) {
            ObjectsSyncRequests.SyncIsoChunk var5 = (ObjectsSyncRequests.SyncIsoChunk)this.requestsSyncIsoChunk.get(var4);
            if (var5.x == var1 && var5.y == var2) {
               this.requestsSyncIsoChunk.remove(var4);
               return;
            }
         }

      }
   }

   public void receiveSyncIsoGridSquare(int var1, int var2, int var3) {
      synchronized(this.requestsSyncIsoGridSquare) {
         for(int var5 = 0; var5 < this.requestsSyncIsoGridSquare.size(); ++var5) {
            ObjectsSyncRequests.SyncIsoGridSquare var6 = (ObjectsSyncRequests.SyncIsoGridSquare)this.requestsSyncIsoGridSquare.get(var5);
            if (var6.x == var1 && var6.y == var2 && var6.z == var3) {
               this.requestsSyncIsoGridSquare.remove(var5);
               return;
            }
         }

      }
   }

   public void receiveSyncIsoObject(int var1, int var2, int var3, long var4) {
      synchronized(this.requestsSyncIsoObject) {
         for(int var7 = 0; var7 < this.requestsSyncIsoObject.size(); ++var7) {
            ObjectsSyncRequests.SyncIsoObject var8 = (ObjectsSyncRequests.SyncIsoObject)this.requestsSyncIsoObject.get(var7);
            if (var8.x == var1 && var8.y == var2 && var8.z == var3 && var8.hash == var4) {
               this.requestsSyncIsoObject.remove(var7);
               return;
            }
         }

      }
   }

   public void receiveGridSquareHashes(ByteBuffer var1) {
      short var2 = var1.getShort();

      for(int var3 = 0; var3 < var2; ++var3) {
         short var4 = var1.getShort();
         short var5 = var1.getShort();
         long var6 = var1.getLong();
         short var8 = var1.getShort();

         for(int var9 = 0; var9 < var8; ++var9) {
            int var10 = var1.get() + var4 * 10;
            int var11 = var1.get() + var5 * 10;
            byte var12 = var1.get();
            int var13 = var1.getInt();
            IsoGridSquare var14 = IsoWorld.instance.CurrentCell.getGridSquare(var10, var11, var12);
            if (var14 != null) {
               int var15 = var14.getHashCodeObjectsInt();
               if (var15 != var13) {
                  ObjectsSyncRequests.SyncIsoGridSquare var16 = new ObjectsSyncRequests.SyncIsoGridSquare();
                  var16.x = var10;
                  var16.y = var11;
                  var16.z = var12;
                  var16.reqTime = 0L;
                  var16.reqCount = 0;
                  synchronized(this.requestsSyncIsoGridSquare) {
                     this.requestsSyncIsoGridSquare.add(var16);
                  }
               }
            }
         }

         this.receiveSyncIsoChunk(var4, var5);
      }

   }

   public void receiveGridSquareObjectHashes(ByteBuffer var1) {
      short var2 = var1.getShort();

      for(int var3 = 0; var3 < var2; ++var3) {
         int var4 = var1.getInt();
         int var5 = var1.getInt();
         byte var6 = var1.get();
         this.receiveSyncIsoGridSquare(var4, var5, var6);
         IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare(var4, var5, var6);
         if (var7 == null) {
            return;
         }

         byte var8 = var1.get();
         int var9 = var1.getInt() - 3;
         long[] var10 = new long[var8];

         for(int var11 = 0; var11 < var8; ++var11) {
            var10[var11] = var1.getLong();
         }

         try {
            boolean[] var21 = new boolean[var7.getObjects().size()];
            boolean[] var12 = new boolean[var8];

            int var13;
            for(var13 = 0; var13 < var8; ++var13) {
               var12[var13] = true;
            }

            var13 = 0;

            label83:
            while(true) {
               if (var13 >= var7.getObjects().size()) {
                  for(var13 = var7.getObjects().size() - 1; var13 >= 0; --var13) {
                     if (var21[var13]) {
                        ((IsoObject)var7.getObjects().get(var13)).removeFromWorld();
                        ((IsoObject)var7.getObjects().get(var13)).removeFromSquare();
                     }
                  }

                  var13 = 0;

                  while(true) {
                     if (var13 >= var8) {
                        break label83;
                     }

                     if (var12[var13]) {
                        ObjectsSyncRequests.SyncIsoObject var22 = new ObjectsSyncRequests.SyncIsoObject();
                        var22.x = var4;
                        var22.y = var5;
                        var22.z = var6;
                        var22.hash = var10[var13];
                        var22.reqTime = 0L;
                        var22.reqCount = 0;
                        synchronized(this.requestsSyncIsoObject) {
                           this.requestsSyncIsoObject.add(var22);
                        }
                     }

                     ++var13;
                  }
               }

               var21[var13] = false;
               long var14 = ((IsoObject)var7.getObjects().get(var13)).customHashCode();
               boolean var16 = false;

               for(int var17 = 0; var17 < var8; ++var17) {
                  if (var10[var17] == var14) {
                     var16 = true;
                     var12[var17] = false;
                     break;
                  }
               }

               if (!var16) {
                  var21[var13] = true;
               }

               ++var13;
            }
         } catch (Throwable var20) {
            DebugLog.log("ERROR: receiveGridSquareObjects " + var20.getMessage());
         }

         var7.RecalcAllWithNeighbours(true);
         IsoWorld.instance.CurrentCell.checkHaveRoof(var7.getX(), var7.getY());
         var1.position(var9);
      }

      LuaEventManager.triggerEvent("OnContainerUpdate");
   }

   public void receiveObject(ByteBuffer var1) {
      int var2 = var1.getInt();
      int var3 = var1.getInt();
      byte var4 = var1.get();
      long var5 = var1.getLong();
      this.receiveSyncIsoObject(var2, var3, var4, var5);
      IsoGridSquare var7 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var3, var4);
      if (var7 != null) {
         byte var8 = var1.get();
         long[] var9 = new long[var8];

         for(int var10 = 0; var10 < var8; ++var10) {
            var9[var10] = var1.getLong();
         }

         long[] var14 = new long[var7.getObjects().size()];

         int var11;
         for(var11 = 0; var11 < var7.getObjects().size(); ++var11) {
            var14[var11] = ((IsoObject)var7.getObjects().get(var11)).customHashCode();
         }

         var11 = var7.getObjects().size();
         int var12 = getObjectInsertIndex(var14, var9, var5);
         if (var12 == -1) {
            DebugLog.log("ERROR: ObjectsSyncRequest.receiveObject OBJECT EXIST (" + var2 + ", " + var3 + ", " + var4 + ") hash=" + var5);
         } else {
            IsoObject var13 = WorldItemTypes.createFromBuffer(var1);
            if (var13 != null) {
               var13.loadFromRemoteBuffer(var1, false);
               var7.getObjects().add(var12, var13);
               if (var13 instanceof IsoLightSwitch) {
                  ((IsoLightSwitch)var13).addLightSourceFromSprite();
               }

               var13.addToWorld();
            }

            var7.RecalcAllWithNeighbours(true);
            IsoWorld.instance.CurrentCell.checkHaveRoof(var7.getX(), var7.getY());
            LuaEventManager.triggerEvent("OnContainerUpdate");
         }
      }
   }

   public void serverSendRequests(UdpEngine var1) {
      for(int var2 = 0; var2 < var1.connections.size(); ++var2) {
         this.serverSendRequests((UdpConnection)var1.connections.get(var2));
      }

      synchronized(this.requestsSyncIsoGridSquare) {
         for(int var3 = 0; var3 < this.requestsSyncIsoGridSquare.size(); ++var3) {
            this.requestsSyncIsoGridSquare.remove(0);
         }

      }
   }

   public void serverSendRequests(UdpConnection var1) {
      if (this.requestsSyncIsoGridSquare.size() != 0) {
         ByteBufferWriter var2 = var1.startPacket();
         PacketTypes.PacketType.SyncObjects.doPacket(var2);
         var2.putShort((short)4);
         int var3 = var2.bb.position();
         var2.putShort((short)0);
         int var4 = 0;

         int var5;
         for(var5 = 0; var5 < this.requestsSyncIsoGridSquare.size(); ++var5) {
            ObjectsSyncRequests.SyncIsoGridSquare var6 = (ObjectsSyncRequests.SyncIsoGridSquare)this.requestsSyncIsoGridSquare.get(var5);
            if (var1.RelevantTo((float)var6.x, (float)var6.y, 100.0F)) {
               IsoGridSquare var7 = ServerMap.instance.getGridSquare(var6.x, var6.y, var6.z);
               if (var7 != null) {
                  ++var4;
                  var2.putInt(var7.x);
                  var2.putInt(var7.y);
                  var2.putByte((byte)var7.z);
                  var2.putByte((byte)var7.getObjects().size());
                  var2.putInt(0);
                  int var8 = var2.bb.position();

                  int var9;
                  for(var9 = 0; var9 < var7.getObjects().size(); ++var9) {
                     var2.putLong(((IsoObject)var7.getObjects().get(var9)).customHashCode());
                  }

                  var9 = var2.bb.position();
                  var2.bb.position(var8 - 4);
                  var2.putInt(var9);
                  var2.bb.position(var9);
               }
            }
         }

         var5 = var2.bb.position();
         var2.bb.position(var3);
         var2.putShort((short)var4);
         var2.bb.position(var5);
         PacketTypes.PacketType.SyncObjects.send(GameClient.connection);
      }
   }

   private class SyncIsoChunk {
      int x;
      int y;
      long hashCodeObjects;
      long reqTime;
      int reqCount;
   }

   private class SyncIsoGridSquare {
      int x;
      int y;
      int z;
      long reqTime;
      int reqCount;

      public int hashCode() {
         return this.x + this.y + this.z;
      }
   }

   private class SyncIsoObject {
      int x;
      int y;
      int z;
      long hash;
      long reqTime;
      int reqCount;

      public int hashCode() {
         return (int)((long)(this.x + this.y + this.z) + this.hash);
      }
   }
}
