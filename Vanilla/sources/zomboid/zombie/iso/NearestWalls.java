package zombie.iso;

import java.io.PrintStream;
import zombie.debug.LineDrawer;
import zombie.iso.SpriteDetails.IsoFlagType;

public class NearestWalls {
   private static final int CPW = 10;
   private static final int CPWx4 = 40;
   private static final int LEVELS = 8;
   private static int CHANGE_COUNT = 0;
   private static int renderX;
   private static int renderY;
   private static int renderZ;

   public static void chunkLoaded(IsoChunk var0) {
      ++CHANGE_COUNT;
      if (CHANGE_COUNT < 0) {
         CHANGE_COUNT = 0;
      }

      var0.nearestWalls.changeCount = -1;
   }

   private static void calcDistanceOnThisChunkOnly(IsoChunk var0) {
      byte[] var1 = var0.nearestWalls.distanceSelf;

      for(int var2 = 0; var2 < 8; ++var2) {
         int var3;
         byte var4;
         int var5;
         int var6;
         IsoGridSquare var7;
         int var8;
         for(var3 = 0; var3 < 10; ++var3) {
            var4 = -1;

            for(var5 = 0; var5 < 10; ++var5) {
               var0.nearestWalls.closest[var5 + var3 * 10 + var2 * 10 * 10] = -1;
               var6 = var5 * 4 + var3 * 40 + var2 * 10 * 40;
               var1[var6 + 0] = var4 == -1 ? -1 : (byte)(var5 - var4);
               var1[var6 + 1] = -1;
               var7 = var0.getGridSquare(var5, var3, var2);
               if (var7 != null && (var7.Is(IsoFlagType.WallW) || var7.Is(IsoFlagType.DoorWallW) || var7.Is(IsoFlagType.WallNW) || var7.Is(IsoFlagType.WindowW))) {
                  var4 = (byte)var5;
                  var1[var6 + 0] = 0;

                  for(var8 = var5 - 1; var8 >= 0; --var8) {
                     var6 = var8 * 4 + var3 * 40 + var2 * 10 * 40;
                     if (var1[var6 + 1] != -1) {
                        break;
                     }

                     var1[var6 + 1] = (byte)(var4 - var8);
                  }
               }
            }
         }

         for(var3 = 0; var3 < 10; ++var3) {
            var4 = -1;

            for(var5 = 0; var5 < 10; ++var5) {
               var6 = var3 * 4 + var5 * 40 + var2 * 10 * 40;
               var1[var6 + 2] = var4 == -1 ? -1 : (byte)(var5 - var4);
               var1[var6 + 3] = -1;
               var7 = var0.getGridSquare(var3, var5, var2);
               if (var7 != null && (var7.Is(IsoFlagType.WallN) || var7.Is(IsoFlagType.DoorWallN) || var7.Is(IsoFlagType.WallNW) || var7.Is(IsoFlagType.WindowN))) {
                  var4 = (byte)var5;
                  var1[var6 + 2] = 0;

                  for(var8 = var5 - 1; var8 >= 0; --var8) {
                     var6 = var3 * 4 + var8 * 40 + var2 * 10 * 40;
                     if (var1[var6 + 3] != -1) {
                        break;
                     }

                     var1[var6 + 3] = (byte)(var4 - var8);
                  }
               }
            }
         }
      }

   }

   private static int getIndex(IsoChunk var0, int var1, int var2, int var3) {
      return (var1 - var0.wx * 10) * 4 + (var2 - var0.wy * 10) * 40 + var3 * 10 * 40;
   }

   private static int getNearestWallOnSameChunk(IsoChunk var0, int var1, int var2, int var3, int var4) {
      NearestWalls.ChunkData var5 = var0.nearestWalls;
      if (var5.changeCount != CHANGE_COUNT) {
         calcDistanceOnThisChunkOnly(var0);
         var5.changeCount = CHANGE_COUNT;
      }

      int var6 = getIndex(var0, var1, var2, var3);
      return var5.distanceSelf[var6 + var4];
   }

   private static boolean hasWall(IsoChunk var0, int var1, int var2, int var3, int var4) {
      return getNearestWallOnSameChunk(var0, var1, var2, var3, var4) == 0;
   }

   private static int getNearestWallWest(IsoChunk var0, int var1, int var2, int var3) {
      byte var4 = 0;
      byte var5 = -1;
      byte var6 = 0;
      int var7 = getNearestWallOnSameChunk(var0, var1, var2, var3, var4);
      if (var7 != -1) {
         return var1 - var7;
      } else {
         for(int var8 = 1; var8 <= 3; ++var8) {
            IsoChunk var9 = IsoWorld.instance.CurrentCell.getChunk(var0.wx + var8 * var5, var0.wy + var8 * var6);
            if (var9 == null) {
               break;
            }

            int var10 = (var9.wx + 1) * 10 - 1;
            var7 = getNearestWallOnSameChunk(var9, var10, var2, var3, var4);
            if (var7 != -1) {
               return var10 - var7;
            }
         }

         return -1;
      }
   }

   private static int getNearestWallEast(IsoChunk var0, int var1, int var2, int var3) {
      byte var4 = 1;
      byte var5 = 1;
      byte var6 = 0;
      int var7 = getNearestWallOnSameChunk(var0, var1, var2, var3, var4);
      if (var7 != -1) {
         return var1 + var7;
      } else {
         for(int var8 = 1; var8 <= 3; ++var8) {
            IsoChunk var9 = IsoWorld.instance.CurrentCell.getChunk(var0.wx + var8 * var5, var0.wy + var8 * var6);
            if (var9 == null) {
               break;
            }

            int var10 = var9.wx * 10;
            var7 = hasWall(var9, var10, var2, var3, 0) ? 0 : getNearestWallOnSameChunk(var9, var10, var2, var3, var4);
            if (var7 != -1) {
               return var10 + var7;
            }
         }

         return -1;
      }
   }

   private static int getNearestWallNorth(IsoChunk var0, int var1, int var2, int var3) {
      byte var4 = 2;
      byte var5 = 0;
      byte var6 = -1;
      int var7 = getNearestWallOnSameChunk(var0, var1, var2, var3, var4);
      if (var7 != -1) {
         return var2 - var7;
      } else {
         for(int var8 = 1; var8 <= 3; ++var8) {
            IsoChunk var9 = IsoWorld.instance.CurrentCell.getChunk(var0.wx + var8 * var5, var0.wy + var8 * var6);
            if (var9 == null) {
               break;
            }

            int var11 = (var9.wy + 1) * 10 - 1;
            var7 = getNearestWallOnSameChunk(var9, var1, var11, var3, var4);
            if (var7 != -1) {
               return var11 - var7;
            }
         }

         return -1;
      }
   }

   private static int getNearestWallSouth(IsoChunk var0, int var1, int var2, int var3) {
      byte var4 = 3;
      byte var5 = 0;
      byte var6 = 1;
      int var7 = getNearestWallOnSameChunk(var0, var1, var2, var3, var4);
      if (var7 != -1) {
         return var2 + var7;
      } else {
         for(int var8 = 1; var8 <= 3; ++var8) {
            IsoChunk var9 = IsoWorld.instance.CurrentCell.getChunk(var0.wx + var8 * var5, var0.wy + var8 * var6);
            if (var9 == null) {
               break;
            }

            int var11 = var9.wy * 10;
            var7 = hasWall(var9, var1, var11, var3, 2) ? 0 : getNearestWallOnSameChunk(var9, var1, var11, var3, var4);
            if (var7 != -1) {
               return var11 + var7;
            }
         }

         return -1;
      }
   }

   public static void render(int var0, int var1, int var2) {
      IsoChunk var3 = IsoWorld.instance.CurrentCell.getChunkForGridSquare(var0, var1, var2);
      if (var3 != null) {
         if (renderX != var0 || renderY != var1 || renderZ != var2) {
            renderX = var0;
            renderY = var1;
            renderZ = var2;
            PrintStream var10000 = System.out;
            int var10001 = ClosestWallDistance(var3, var0, var1, var2);
            var10000.println("ClosestWallDistance=" + var10001);
         }

         int var4 = getNearestWallWest(var3, var0, var1, var2);
         if (var4 != -1) {
            DrawIsoLine((float)var4, (float)var1 + 0.5F, (float)var0 + 0.5F, (float)var1 + 0.5F, (float)var2, 1.0F, 1.0F, 1.0F, 1.0F, 1);
            DrawIsoLine((float)var4, (float)var1, (float)var4, (float)(var1 + 1), (float)var2, 1.0F, 1.0F, 1.0F, 1.0F, 1);
         }

         var4 = getNearestWallEast(var3, var0, var1, var2);
         if (var4 != -1) {
            DrawIsoLine((float)var4, (float)var1 + 0.5F, (float)var0 + 0.5F, (float)var1 + 0.5F, (float)var2, 1.0F, 1.0F, 1.0F, 1.0F, 1);
            DrawIsoLine((float)var4, (float)var1, (float)var4, (float)(var1 + 1), (float)var2, 1.0F, 1.0F, 1.0F, 1.0F, 1);
         }

         int var5 = getNearestWallNorth(var3, var0, var1, var2);
         if (var5 != -1) {
            DrawIsoLine((float)var0 + 0.5F, (float)var5, (float)var0 + 0.5F, (float)var1 + 0.5F, (float)var2, 1.0F, 1.0F, 1.0F, 1.0F, 1);
            DrawIsoLine((float)var0, (float)var5, (float)(var0 + 1), (float)var5, (float)var2, 1.0F, 1.0F, 1.0F, 1.0F, 1);
         }

         var5 = getNearestWallSouth(var3, var0, var1, var2);
         if (var5 != -1) {
            DrawIsoLine((float)var0 + 0.5F, (float)var5, (float)var0 + 0.5F, (float)var1 + 0.5F, (float)var2, 1.0F, 1.0F, 1.0F, 1.0F, 1);
            DrawIsoLine((float)var0, (float)var5, (float)(var0 + 1), (float)var5, (float)var2, 1.0F, 1.0F, 1.0F, 1.0F, 1);
         }

      }
   }

   private static void DrawIsoLine(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, int var9) {
      float var10 = IsoUtils.XToScreenExact(var0, var1, var4, 0);
      float var11 = IsoUtils.YToScreenExact(var0, var1, var4, 0);
      float var12 = IsoUtils.XToScreenExact(var2, var3, var4, 0);
      float var13 = IsoUtils.YToScreenExact(var2, var3, var4, 0);
      LineDrawer.drawLine(var10, var11, var12, var13, var5, var6, var7, var8, var9);
   }

   public static int ClosestWallDistance(IsoGridSquare var0) {
      return var0 != null && var0.chunk != null ? ClosestWallDistance(var0.chunk, var0.x, var0.y, var0.z) : 127;
   }

   public static int ClosestWallDistance(IsoChunk var0, int var1, int var2, int var3) {
      if (var0 == null) {
         return 127;
      } else {
         NearestWalls.ChunkData var4 = var0.nearestWalls;
         byte[] var5 = var4.closest;
         if (var4.changeCount != CHANGE_COUNT) {
            calcDistanceOnThisChunkOnly(var0);
            var4.changeCount = CHANGE_COUNT;
         }

         int var6 = var1 - var0.wx * 10 + (var2 - var0.wy * 10) * 10 + var3 * 10 * 10;
         byte var7 = var5[var6];
         if (var7 != -1) {
            return var7;
         } else {
            int var8 = getNearestWallWest(var0, var1, var2, var3);
            int var9 = getNearestWallEast(var0, var1, var2, var3);
            int var10 = getNearestWallNorth(var0, var1, var2, var3);
            int var11 = getNearestWallSouth(var0, var1, var2, var3);
            if (var8 == -1 && var9 == -1 && var10 == -1 && var11 == -1) {
               return var5[var6] = 127;
            } else {
               int var12 = -1;
               if (var8 != -1 && var9 != -1) {
                  var12 = var9 - var8;
               }

               int var13 = -1;
               if (var10 != -1 && var11 != -1) {
                  var13 = var11 - var10;
               }

               if (var12 != -1 && var13 != -1) {
                  return var5[var6] = (byte)Math.min(var12, var13);
               } else if (var12 != -1) {
                  return var5[var6] = (byte)var12;
               } else if (var13 != -1) {
                  return var5[var6] = (byte)var13;
               } else {
                  IsoGridSquare var14 = var0.getGridSquare(var1 - var0.wx * 10, var2 - var0.wy * 10, var3);
                  if (var14 != null && var14.isOutside()) {
                     var8 = var8 == -1 ? 127 : var1 - var8;
                     var9 = var9 == -1 ? 127 : var9 - var1 - 1;
                     var10 = var10 == -1 ? 127 : var2 - var10;
                     var11 = var11 == -1 ? 127 : var11 - var2 - 1;
                     return var5[var6] = (byte)Math.min(var8, Math.min(var9, Math.min(var10, var11)));
                  } else {
                     return var5[var6] = 127;
                  }
               }
            }
         }
      }
   }

   public static final class ChunkData {
      int changeCount = -1;
      final byte[] distanceSelf = new byte[3200];
      final byte[] closest = new byte[800];
   }
}
