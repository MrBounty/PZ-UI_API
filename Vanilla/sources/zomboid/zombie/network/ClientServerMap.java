package zombie.network;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.Consumer;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.core.textures.Texture;
import zombie.iso.IsoChunk;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;

public final class ClientServerMap {
   private static final int ChunksPerServerCell = 5;
   private static final int SquaresPerServerCell = 50;
   int playerIndex;
   int centerX;
   int centerY;
   int chunkGridWidth;
   int width;
   boolean[] loaded;
   private static boolean[] isLoaded;
   private static Texture trafficCone;

   public ClientServerMap(int var1, int var2, int var3, int var4) {
      this.playerIndex = var1;
      this.centerX = var2;
      this.centerY = var3;
      this.chunkGridWidth = var4;
      this.width = (var4 - 1) * 10 / 50;
      if ((var4 - 1) * 10 % 50 != 0) {
         ++this.width;
      }

      ++this.width;
      this.loaded = new boolean[this.width * this.width];
   }

   public int getMinX() {
      return (this.centerX / 10 - this.chunkGridWidth / 2) / 5;
   }

   public int getMinY() {
      return (this.centerY / 10 - this.chunkGridWidth / 2) / 5;
   }

   public int getMaxX() {
      return this.getMinX() + this.width - 1;
   }

   public int getMaxY() {
      return this.getMinY() + this.width - 1;
   }

   public boolean isValidCell(int var1, int var2) {
      return var1 >= 0 && var2 >= 0 && var1 < this.width && var2 < this.width;
   }

   public boolean setLoaded() {
      if (!GameServer.bServer) {
         return false;
      } else {
         int var1 = ServerMap.instance.getMinX();
         int var2 = ServerMap.instance.getMinY();
         int var3 = this.getMinX();
         int var4 = this.getMinY();
         boolean var5 = false;

         for(int var6 = 0; var6 < this.width; ++var6) {
            for(int var7 = 0; var7 < this.width; ++var7) {
               ServerMap.ServerCell var8 = ServerMap.instance.getCell(var3 + var7 - var1, var4 + var6 - var2);
               boolean var9 = var8 == null ? false : var8.bLoaded;
               var5 |= this.loaded[var7 + var6 * this.width] != var9;
               this.loaded[var7 + var6 * this.width] = var9;
            }
         }

         return var5;
      }
   }

   public boolean setPlayerPosition(int var1, int var2) {
      if (!GameServer.bServer) {
         return false;
      } else {
         int var3 = this.getMinX();
         int var4 = this.getMinY();
         this.centerX = var1;
         this.centerY = var2;
         return this.setLoaded() || var3 != this.getMinX() || var4 != this.getMinY();
      }
   }

   public static boolean isChunkLoaded(int var0, int var1) {
      if (!GameClient.bClient) {
         return false;
      } else if (var0 >= 0 && var1 >= 0) {
         for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
            ClientServerMap var3 = GameClient.loadedCells[var2];
            if (var3 != null) {
               int var4 = var0 / 5 - var3.getMinX();
               int var5 = var1 / 5 - var3.getMinY();
               if (var3.isValidCell(var4, var5) && var3.loaded[var4 + var5 * var3.width]) {
                  return true;
               }
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public static void characterIn(UdpConnection var0, int var1) {
      if (GameServer.bServer) {
         ClientServerMap var2 = var0.loadedCells[var1];
         if (var2 != null) {
            IsoPlayer var3 = var0.players[var1];
            if (var3 != null) {
               if (var2.setPlayerPosition((int)var3.x, (int)var3.y)) {
                  var2.sendPacket(var0);
               }

            }
         }
      }
   }

   public void sendPacket(UdpConnection var1) {
      if (GameServer.bServer) {
         ByteBufferWriter var2 = var1.startPacket();
         PacketTypes.PacketType.ServerMap.doPacket(var2);
         var2.putByte((byte)this.playerIndex);
         var2.putInt(this.centerX);
         var2.putInt(this.centerY);

         for(int var3 = 0; var3 < this.width; ++var3) {
            for(int var4 = 0; var4 < this.width; ++var4) {
               var2.putBoolean(this.loaded[var4 + var3 * this.width]);
            }
         }

         PacketTypes.PacketType.ServerMap.send(var1);
      }
   }

   public static void receivePacket(ByteBuffer var0) {
      if (GameClient.bClient) {
         byte var1 = var0.get();
         int var2 = var0.getInt();
         int var3 = var0.getInt();
         ClientServerMap var4 = GameClient.loadedCells[var1];
         if (var4 == null) {
            var4 = GameClient.loadedCells[var1] = new ClientServerMap(var1, var2, var3, IsoChunkMap.ChunkGridWidth);
         }

         var4.centerX = var2;
         var4.centerY = var3;

         for(int var5 = 0; var5 < var4.width; ++var5) {
            for(int var6 = 0; var6 < var4.width; ++var6) {
               var4.loaded[var6 + var5 * var4.width] = var0.get() == 1;
            }
         }

      }
   }

   public static void render(int var0) {
      if (GameClient.bClient) {
         IsoChunkMap var1 = IsoWorld.instance.CurrentCell.getChunkMap(var0);
         if (var1 != null && !var1.ignore) {
            int var2 = Core.TileScale;
            byte var3 = 10;
            float var4 = 0.1F;
            float var5 = 0.1F;
            float var6 = 0.1F;
            float var7 = 0.75F;
            float var8 = 0.0F;
            if (trafficCone == null) {
               trafficCone = Texture.getSharedTexture("street_decoration_01_26");
            }

            Texture var9 = trafficCone;
            if (isLoaded == null || isLoaded.length < IsoChunkMap.ChunkGridWidth * IsoChunkMap.ChunkGridWidth) {
               isLoaded = new boolean[IsoChunkMap.ChunkGridWidth * IsoChunkMap.ChunkGridWidth];
            }

            int var10;
            int var11;
            IsoChunk var12;
            for(var10 = 0; var10 < IsoChunkMap.ChunkGridWidth; ++var10) {
               for(var11 = 0; var11 < IsoChunkMap.ChunkGridWidth; ++var11) {
                  var12 = var1.getChunk(var11, var10);
                  if (var12 != null) {
                     isLoaded[var11 + var10 * IsoChunkMap.ChunkGridWidth] = isChunkLoaded(var12.wx, var12.wy);
                  }
               }
            }

            for(var10 = 0; var10 < IsoChunkMap.ChunkGridWidth; ++var10) {
               for(var11 = 0; var11 < IsoChunkMap.ChunkGridWidth; ++var11) {
                  var12 = var1.getChunk(var11, var10);
                  if (var12 != null) {
                     boolean var13 = isLoaded[var11 + var10 * IsoChunkMap.ChunkGridWidth];
                     float var16;
                     float var17;
                     if (var13 && var9 != null) {
                        IsoChunk var14 = var1.getChunk(var11, var10 - 1);
                        if (var14 != null && !isLoaded[var11 + (var10 - 1) * IsoChunkMap.ChunkGridWidth]) {
                           for(int var15 = 0; var15 < var3; ++var15) {
                              var16 = IsoUtils.XToScreenExact((float)(var12.wx * var3 + var15), (float)(var12.wy * var3), var8, 0);
                              var17 = IsoUtils.YToScreenExact((float)(var12.wx * var3 + var15), (float)(var12.wy * var3), var8, 0);
                              SpriteRenderer.instance.render(var9, var16 - (float)(var9.getWidth() / 2), var17, (float)var9.getWidth(), (float)var9.getHeight(), 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
                           }
                        }

                        IsoChunk var22 = var1.getChunk(var11, var10 + 1);
                        float var18;
                        if (var22 != null && !isLoaded[var11 + (var10 + 1) * IsoChunkMap.ChunkGridWidth]) {
                           for(int var24 = 0; var24 < var3; ++var24) {
                              var17 = IsoUtils.XToScreenExact((float)(var12.wx * var3 + var24), (float)(var12.wy * var3 + (var3 - 1)), var8, 0);
                              var18 = IsoUtils.YToScreenExact((float)(var12.wx * var3 + var24), (float)(var12.wy * var3 + (var3 - 1)), var8, 0);
                              SpriteRenderer.instance.render(var9, var17 - (float)(var9.getWidth() / 2), var18, (float)var9.getWidth(), (float)var9.getHeight(), 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
                           }
                        }

                        IsoChunk var25 = var1.getChunk(var11 - 1, var10);
                        float var19;
                        if (var25 != null && !isLoaded[var11 - 1 + var10 * IsoChunkMap.ChunkGridWidth]) {
                           for(int var26 = 0; var26 < var3; ++var26) {
                              var18 = IsoUtils.XToScreenExact((float)(var12.wx * var3), (float)(var12.wy * var3 + var26), var8, 0);
                              var19 = IsoUtils.YToScreenExact((float)(var12.wx * var3), (float)(var12.wy * var3 + var26), var8, 0);
                              SpriteRenderer.instance.render(var9, var18 - (float)(var9.getWidth() / 2), var19, (float)var9.getWidth(), (float)var9.getHeight(), 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
                           }
                        }

                        IsoChunk var27 = var1.getChunk(var11 + 1, var10);
                        if (var27 != null && !isLoaded[var11 + 1 + var10 * IsoChunkMap.ChunkGridWidth]) {
                           for(int var28 = 0; var28 < var3; ++var28) {
                              var19 = IsoUtils.XToScreenExact((float)(var12.wx * var3 + (var3 - 1)), (float)(var12.wy * var3 + var28), var8, 0);
                              float var20 = IsoUtils.YToScreenExact((float)(var12.wx * var3 + (var3 - 1)), (float)(var12.wy * var3 + var28), var8, 0);
                              SpriteRenderer.instance.render(var9, var19 - (float)(var9.getWidth() / 2), var20, (float)var9.getWidth(), (float)var9.getHeight(), 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);
                           }
                        }
                     }

                     if (!var13) {
                        float var21 = (float)(var12.wx * var3);
                        float var23 = (float)(var12.wy * var3);
                        var16 = IsoUtils.XToScreenExact(var21, var23 + (float)var3, var8, 0);
                        var17 = IsoUtils.YToScreenExact(var21, var23 + (float)var3, var8, 0);
                        SpriteRenderer.instance.renderPoly((float)((int)var16), (float)((int)var17), (float)((int)(var16 + (float)(var3 * 64 / 2 * var2))), (float)((int)(var17 - (float)(var3 * 32 / 2 * var2))), (float)((int)(var16 + (float)(var3 * 64 * var2))), (float)((int)var17), (float)((int)(var16 + (float)(var3 * 64 / 2 * var2))), (float)((int)(var17 + (float)(var3 * 32 / 2 * var2))), var4, var5, var6, var7);
                     }
                  }
               }
            }

         }
      }
   }

   public static void Reset() {
      Arrays.fill(GameClient.loadedCells, (Object)null);
      trafficCone = null;
   }
}
