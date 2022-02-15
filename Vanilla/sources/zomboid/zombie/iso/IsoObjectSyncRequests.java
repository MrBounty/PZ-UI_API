package zombie.iso;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.SystemDisabler;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.network.GameClient;
import zombie.network.PacketTypes;

public final class IsoObjectSyncRequests {
   public final ArrayList requests = new ArrayList();
   public long timeout = 1000L;

   public void putRequest(IsoGridSquare var1, IsoObject var2) {
      if (GameClient.bClient) {
         this.putRequest(var1.x, var1.y, var1.z, (byte)var1.getObjects().indexOf(var2));
      }

   }

   public void putRequestLoad(IsoGridSquare var1) {
      if (GameClient.bClient) {
         this.putRequest(var1.x, var1.y, var1.z, (byte)var1.getObjects().size());
      }

   }

   public void putRequest(int var1, int var2, int var3, byte var4) {
      if (SystemDisabler.doObjectStateSyncEnable) {
         IsoObjectSyncRequests.SyncData var5 = new IsoObjectSyncRequests.SyncData();
         var5.x = var1;
         var5.y = var2;
         var5.z = var3;
         var5.objIndex = var4;
         var5.reqTime = 0L;
         var5.reqCount = 0;
         synchronized(this.requests) {
            this.requests.add(var5);
         }
      }
   }

   public void sendRequests(UdpConnection var1) {
      if (SystemDisabler.doObjectStateSyncEnable) {
         if (this.requests.size() != 0) {
            ByteBufferWriter var2 = var1.startPacket();
            PacketTypes.PacketType.SyncIsoObjectReq.doPacket(var2);
            ByteBuffer var3 = var2.bb;
            int var4 = var3.position();
            var2.putShort((short)0);
            int var5 = 0;
            synchronized(this.requests) {
               for(int var7 = 0; var7 < this.requests.size(); ++var7) {
                  IsoObjectSyncRequests.SyncData var8 = (IsoObjectSyncRequests.SyncData)this.requests.get(var7);
                  if (var8.reqCount > 4) {
                     this.requests.remove(var7);
                     --var7;
                  } else {
                     if (var8.reqTime == 0L) {
                        var8.reqTime = System.currentTimeMillis();
                        ++var5;
                        var3.putInt(var8.x);
                        var3.putInt(var8.y);
                        var3.putInt(var8.z);
                        var3.put(var8.objIndex);
                        ++var8.reqCount;
                     }

                     if (System.currentTimeMillis() - var8.reqTime >= this.timeout) {
                        var8.reqTime = System.currentTimeMillis();
                        ++var5;
                        var3.putInt(var8.x);
                        var3.putInt(var8.y);
                        var3.putInt(var8.z);
                        var3.put(var8.objIndex);
                        ++var8.reqCount;
                     }

                     if (var5 >= 50) {
                        break;
                     }
                  }
               }
            }

            if (var5 == 0) {
               GameClient.connection.cancelPacket();
            } else {
               int var6 = var3.position();
               var3.position(var4);
               var3.putShort((short)var5);
               var3.position(var6);
               PacketTypes.PacketType.SyncIsoObjectReq.send(GameClient.connection);
            }
         }
      }
   }

   public void receiveIsoSync(int var1, int var2, int var3, byte var4) {
      synchronized(this.requests) {
         for(int var6 = 0; var6 < this.requests.size(); ++var6) {
            IsoObjectSyncRequests.SyncData var7 = (IsoObjectSyncRequests.SyncData)this.requests.get(var6);
            if (var7.x == var1 && var7.y == var2 && var7.z == var3 && var7.objIndex == var4) {
               this.requests.remove(var6);
            }
         }

      }
   }

   private class SyncData {
      int x;
      int y;
      int z;
      byte objIndex;
      long reqTime;
      int reqCount;
   }
}
