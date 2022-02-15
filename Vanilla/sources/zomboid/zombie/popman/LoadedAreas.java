package zombie.popman;

import zombie.characters.IsoPlayer;
import zombie.core.raknet.UdpConnection;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoWorld;
import zombie.iso.Vector3;
import zombie.network.GameServer;
import zombie.network.ServerMap;

final class LoadedAreas {
   public static final int MAX_AREAS = 64;
   public int[] areas = new int[256];
   public int count;
   public boolean changed;
   public int[] prevAreas = new int[256];
   public int prevCount;
   private boolean serverCells;

   public LoadedAreas(boolean var1) {
      this.serverCells = var1;
   }

   public boolean set() {
      this.setPrev();
      this.clear();
      int var1;
      if (GameServer.bServer) {
         if (this.serverCells) {
            for(var1 = 0; var1 < ServerMap.instance.LoadedCells.size(); ++var1) {
               ServerMap.ServerCell var2 = (ServerMap.ServerCell)ServerMap.instance.LoadedCells.get(var1);
               this.add(var2.WX * 5, var2.WY * 5, 5, 5);
            }
         } else {
            int var3;
            for(var1 = 0; var1 < GameServer.Players.size(); ++var1) {
               IsoPlayer var6 = (IsoPlayer)GameServer.Players.get(var1);
               var3 = (int)var6.x / 10;
               int var4 = (int)var6.y / 10;
               this.add(var3 - var6.OnlineChunkGridWidth / 2, var4 - var6.OnlineChunkGridWidth / 2, var6.OnlineChunkGridWidth, var6.OnlineChunkGridWidth);
            }

            for(var1 = 0; var1 < GameServer.udpEngine.connections.size(); ++var1) {
               UdpConnection var7 = (UdpConnection)GameServer.udpEngine.connections.get(var1);

               for(var3 = 0; var3 < 4; ++var3) {
                  Vector3 var9 = var7.connectArea[var3];
                  if (var9 != null) {
                     int var5 = (int)var9.z;
                     this.add((int)var9.x - var5 / 2, (int)var9.y - var5 / 2, var5, var5);
                  }
               }
            }
         }
      } else {
         for(var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
            IsoChunkMap var8 = IsoWorld.instance.CurrentCell.ChunkMap[var1];
            if (!var8.ignore) {
               this.add(var8.getWorldXMin(), var8.getWorldYMin(), IsoChunkMap.ChunkGridWidth, IsoChunkMap.ChunkGridWidth);
            }
         }
      }

      return this.changed = this.compareWithPrev();
   }

   public void add(int var1, int var2, int var3, int var4) {
      if (this.count < 64) {
         int var5 = this.count * 4;
         this.areas[var5++] = var1;
         this.areas[var5++] = var2;
         this.areas[var5++] = var3;
         this.areas[var5++] = var4;
         ++this.count;
      }
   }

   public void clear() {
      this.count = 0;
      this.changed = false;
   }

   public void copy(LoadedAreas var1) {
      this.count = var1.count;

      for(int var2 = 0; var2 < this.count; ++var2) {
         int var3 = var2 * 4;
         this.areas[var3] = var1.areas[var3++];
         this.areas[var3] = var1.areas[var3++];
         this.areas[var3] = var1.areas[var3++];
         this.areas[var3] = var1.areas[var3++];
      }

   }

   private void setPrev() {
      this.prevCount = this.count;

      for(int var1 = 0; var1 < this.count; ++var1) {
         int var2 = var1 * 4;
         this.prevAreas[var2] = this.areas[var2++];
         this.prevAreas[var2] = this.areas[var2++];
         this.prevAreas[var2] = this.areas[var2++];
         this.prevAreas[var2] = this.areas[var2++];
      }

   }

   private boolean compareWithPrev() {
      if (this.prevCount != this.count) {
         return true;
      } else {
         for(int var1 = 0; var1 < this.count; ++var1) {
            int var2 = var1 * 4;
            if (this.prevAreas[var2] != this.areas[var2++]) {
               return true;
            }

            if (this.prevAreas[var2] != this.areas[var2++]) {
               return true;
            }

            if (this.prevAreas[var2] != this.areas[var2++]) {
               return true;
            }

            if (this.prevAreas[var2] != this.areas[var2++]) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isOnEdge(int var1, int var2) {
      if (var1 % 10 != 0 && (var1 + 1) % 10 != 0 && var2 % 10 != 0 && (var2 + 1) % 10 != 0) {
         return false;
      } else {
         int var3 = 0;

         while(var3 < this.count) {
            int var4 = var3 * 4;
            int var5 = this.areas[var4++] * 10;
            int var6 = this.areas[var4++] * 10;
            int var7 = var5 + this.areas[var4++] * 10;
            int var8 = var6 + this.areas[var4++] * 10;
            boolean var9 = var1 >= var5 && var1 < var7;
            boolean var10 = var2 >= var6 && var2 < var8;
            if (!var9 || var2 != var6 && var2 != var8 - 1) {
               if (!var10 || var1 != var5 && var1 != var7 - 1) {
                  ++var3;
                  continue;
               }

               return true;
            }

            return true;
         }

         return false;
      }
   }
}
