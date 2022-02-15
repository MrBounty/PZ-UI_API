package zombie.iso;

import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.characters.IsoPlayer;
import zombie.core.opengl.RenderSettings;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.areas.IsoRoom;

public final class IsoRoomLight {
   public static int NextID = 1;
   private static int SHINE_DIST = 5;
   public int ID;
   public IsoRoom room;
   public int x;
   public int y;
   public int z;
   public int width;
   public int height;
   public float r;
   public float g;
   public float b;
   public boolean bActive;
   public boolean bActiveJNI;
   public boolean bHydroPowered = true;

   public IsoRoomLight(IsoRoom var1, int var2, int var3, int var4, int var5, int var6) {
      this.room = var1;
      this.x = var2;
      this.y = var3;
      this.z = var4;
      this.width = var5;
      this.height = var6;
      this.r = 0.9F;
      this.b = 0.8F;
      this.b = 0.7F;
      this.bActive = var1.def.bLightsActive;
   }

   public void addInfluence() {
      this.r = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.8F * IsoGridSquare.rmod * 0.7F;
      this.g = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.8F * IsoGridSquare.gmod * 0.7F;
      this.b = RenderSettings.getInstance().getAmbientForPlayer(IsoPlayer.getPlayerIndex()) * 0.8F * IsoGridSquare.bmod * 0.7F;
      this.r *= 2.0F;
      this.g *= 2.0F;
      this.b *= 2.0F;
      this.shineIn(this.x - 1, this.y, this.x, this.y + this.height, SHINE_DIST, 0);
      this.shineIn(this.x, this.y - 1, this.x + this.width, this.y, 0, SHINE_DIST);
      this.shineIn(this.x + this.width, this.y, this.x + this.width + 1, this.y + this.height, -SHINE_DIST, 0);
      this.shineIn(this.x, this.y + this.height, this.x + this.width, this.y + this.height + 1, 0, -SHINE_DIST);
      IsoGridSquare var1 = IsoWorld.instance.CurrentCell.getGridSquare(this.x, this.y, this.z);
      this.bActive = this.room.def.bLightsActive;
      if (this.bHydroPowered && GameTime.instance.NightsSurvived >= SandboxOptions.instance.getElecShutModifier() && (var1 == null || !var1.haveElectricity())) {
         this.bActive = false;
      } else if (this.bActive) {
         this.r = 0.9F;
         this.g = 0.8F;
         this.b = 0.7F;

         for(int var2 = this.y; var2 < this.y + this.height; ++var2) {
            for(int var3 = this.x; var3 < this.x + this.width; ++var3) {
               var1 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var2, this.z);
               if (var1 != null) {
                  var1.setLampostTotalR(var1.getLampostTotalR() + this.r);
                  var1.setLampostTotalG(var1.getLampostTotalG() + this.g);
                  var1.setLampostTotalB(var1.getLampostTotalB() + this.b);
               }
            }
         }

         this.shineOut(this.x, this.y, this.x + 1, this.y + this.height, -SHINE_DIST, 0);
         this.shineOut(this.x, this.y, this.x + this.width, this.y + 1, 0, -SHINE_DIST);
         this.shineOut(this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, SHINE_DIST, 0);
         this.shineOut(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0, SHINE_DIST);
      }
   }

   private void shineOut(int var1, int var2, int var3, int var4, int var5, int var6) {
      for(int var7 = var2; var7 < var4; ++var7) {
         for(int var8 = var1; var8 < var3; ++var8) {
            this.shineOut(var8, var7, var5, var6);
         }
      }

   }

   private void shineOut(int var1, int var2, int var3, int var4) {
      int var5;
      if (var3 > 0) {
         for(var5 = 1; var5 <= var3; ++var5) {
            this.shineFromTo(var1, var2, var1 + var5, var2);
         }
      } else if (var3 < 0) {
         for(var5 = 1; var5 <= -var3; ++var5) {
            this.shineFromTo(var1, var2, var1 - var5, var2);
         }
      } else if (var4 > 0) {
         for(var5 = 1; var5 <= var4; ++var5) {
            this.shineFromTo(var1, var2, var1, var2 + var5);
         }
      } else if (var4 < 0) {
         for(var5 = 1; var5 <= -var4; ++var5) {
            this.shineFromTo(var1, var2, var1, var2 - var5);
         }
      }

   }

   private void shineFromTo(int var1, int var2, int var3, int var4) {
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, this.z);
      if (var5 != null) {
         if (var5.getRoom() != this.room) {
            LosUtil.TestResults var6 = LosUtil.lineClear(IsoWorld.instance.CurrentCell, var1, var2, this.z, var3, var4, this.z, false);
            if (var6 != LosUtil.TestResults.Blocked) {
               float var7 = (float)(Math.abs(var1 - var3) + Math.abs(var2 - var4));
               float var8 = var7 / (float)SHINE_DIST;
               var8 = 1.0F - var8;
               var8 *= var8;
               float var9 = var8 * this.r * 2.0F;
               float var10 = var8 * this.g * 2.0F;
               float var11 = var8 * this.b * 2.0F;
               var5.setLampostTotalR(var5.getLampostTotalR() + var9);
               var5.setLampostTotalG(var5.getLampostTotalG() + var10);
               var5.setLampostTotalB(var5.getLampostTotalB() + var11);
            }
         }
      }
   }

   private void shineIn(int var1, int var2, int var3, int var4, int var5, int var6) {
      for(int var7 = var2; var7 < var4; ++var7) {
         for(int var8 = var1; var8 < var3; ++var8) {
            this.shineIn(var8, var7, var5, var6);
         }
      }

   }

   private void shineIn(int var1, int var2, int var3, int var4) {
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, this.z);
      if (var5 != null && var5.Is(IsoFlagType.exterior)) {
         int var6;
         if (var3 > 0) {
            for(var6 = 1; var6 <= var3; ++var6) {
               this.shineFromToIn(var1, var2, var1 + var6, var2);
            }
         } else if (var3 < 0) {
            for(var6 = 1; var6 <= -var3; ++var6) {
               this.shineFromToIn(var1, var2, var1 - var6, var2);
            }
         } else if (var4 > 0) {
            for(var6 = 1; var6 <= var4; ++var6) {
               this.shineFromToIn(var1, var2, var1, var2 + var6);
            }
         } else if (var4 < 0) {
            for(var6 = 1; var6 <= -var4; ++var6) {
               this.shineFromToIn(var1, var2, var1, var2 - var6);
            }
         }

      }
   }

   private void shineFromToIn(int var1, int var2, int var3, int var4) {
      IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, this.z);
      if (var5 != null) {
         LosUtil.TestResults var6 = LosUtil.lineClear(IsoWorld.instance.CurrentCell, var1, var2, this.z, var3, var4, this.z, false);
         if (var6 != LosUtil.TestResults.Blocked) {
            float var7 = (float)(Math.abs(var1 - var3) + Math.abs(var2 - var4));
            float var8 = var7 / (float)SHINE_DIST;
            var8 = 1.0F - var8;
            var8 *= var8;
            float var9 = var8 * this.r * 2.0F;
            float var10 = var8 * this.g * 2.0F;
            float var11 = var8 * this.b * 2.0F;
            var5.setLampostTotalR(var5.getLampostTotalR() + var9);
            var5.setLampostTotalG(var5.getLampostTotalG() + var10);
            var5.setLampostTotalB(var5.getLampostTotalB() + var11);
         }
      }
   }

   public void clearInfluence() {
      for(int var1 = this.y - SHINE_DIST; var1 < this.y + this.height + SHINE_DIST; ++var1) {
         for(int var2 = this.x - SHINE_DIST; var2 < this.x + this.width + SHINE_DIST; ++var2) {
            IsoGridSquare var3 = IsoWorld.instance.CurrentCell.getGridSquare(var2, var1, this.z);
            if (var3 != null) {
               var3.setLampostTotalR(0.0F);
               var3.setLampostTotalG(0.0F);
               var3.setLampostTotalB(0.0F);
            }
         }
      }

   }

   public boolean isInBounds() {
      IsoChunkMap[] var1 = IsoWorld.instance.CurrentCell.ChunkMap;

      for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
         if (!var1[var2].ignore) {
            int var3 = var1[var2].getWorldXMinTiles();
            int var4 = var1[var2].getWorldXMaxTiles();
            int var5 = var1[var2].getWorldYMinTiles();
            int var6 = var1[var2].getWorldYMaxTiles();
            if (this.x - SHINE_DIST < var4 && this.x + this.width + SHINE_DIST > var3 && this.y - SHINE_DIST < var6 && this.y + this.height + SHINE_DIST > var5) {
               return true;
            }
         }
      }

      return false;
   }
}
