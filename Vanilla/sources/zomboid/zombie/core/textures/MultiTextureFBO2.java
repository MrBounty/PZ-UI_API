package zombie.core.textures;

import java.util.ArrayList;
import java.util.function.Consumer;
import zombie.GameTime;
import zombie.IndieGL;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.utils.ImageUtils;
import zombie.debug.DebugLog;
import zombie.iso.IsoCamera;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.PlayerCamera;
import zombie.iso.sprite.IsoCursor;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.util.Type;

public final class MultiTextureFBO2 {
   private final float[] zoomLevelsDefault = new float[]{2.5F, 2.25F, 2.0F, 1.75F, 1.5F, 1.25F, 1.0F, 0.75F, 0.5F};
   private float[] zoomLevels;
   public TextureFBO Current;
   public volatile TextureFBO FBOrendered = null;
   public final float[] zoom = new float[4];
   public final float[] targetZoom = new float[4];
   public final float[] startZoom = new float[4];
   private float zoomedInLevel;
   private float zoomedOutLevel;
   public final boolean[] bAutoZoom = new boolean[4];
   public boolean bZoomEnabled = true;

   public MultiTextureFBO2() {
      for(int var1 = 0; var1 < 4; ++var1) {
         this.zoom[var1] = this.targetZoom[var1] = this.startZoom[var1] = 1.0F;
      }

   }

   public int getWidth(int var1) {
      return (int)((float)IsoCamera.getScreenWidth(var1) * this.zoom[var1] * ((float)Core.TileScale / 2.0F));
   }

   public int getHeight(int var1) {
      return (int)((float)IsoCamera.getScreenHeight(var1) * this.zoom[var1] * ((float)Core.TileScale / 2.0F));
   }

   public void setTargetZoom(int var1, float var2) {
      if (this.targetZoom[var1] != var2) {
         this.targetZoom[var1] = var2;
         this.startZoom[var1] = this.zoom[var1];
      }

   }

   public ArrayList getDefaultZoomLevels() {
      ArrayList var1 = new ArrayList();
      float[] var2 = this.zoomLevelsDefault;

      for(int var3 = 0; var3 < var2.length; ++var3) {
         var1.add(Math.round(var2[var3] * 100.0F));
      }

      return var1;
   }

   public void setZoomLevelsFromOption(String var1) {
      this.zoomLevels = this.zoomLevelsDefault;
      if (var1 != null && !var1.isEmpty()) {
         String[] var2 = var1.split(";");
         if (var2.length != 0) {
            ArrayList var3 = new ArrayList();
            String[] var4 = var2;
            int var5 = var2.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String var7 = var4[var6];
               if (!var7.isEmpty()) {
                  try {
                     int var8 = Integer.parseInt(var7);
                     float[] var9 = this.zoomLevels;
                     int var10 = var9.length;

                     for(int var11 = 0; var11 < var10; ++var11) {
                        float var12 = var9[var11];
                        if (Math.round(var12 * 100.0F) == var8) {
                           if (!var3.contains(var8)) {
                              var3.add(var8);
                           }
                           break;
                        }
                     }
                  } catch (NumberFormatException var13) {
                  }
               }
            }

            if (!var3.contains(100)) {
               var3.add(100);
            }

            var3.sort((var0, var1x) -> {
               return var1x - var0;
            });
            this.zoomLevels = new float[var3.size()];

            for(int var14 = 0; var14 < var3.size(); ++var14) {
               this.zoomLevels[var14] = (float)(Integer)var3.get(var14) / 100.0F;
            }

         }
      }
   }

   public void destroy() {
      if (this.Current != null) {
         this.Current.destroy();
         this.Current = null;
         this.FBOrendered = null;

         for(int var1 = 0; var1 < 4; ++var1) {
            this.zoom[var1] = this.targetZoom[var1] = 1.0F;
         }

      }
   }

   public void create(int var1, int var2) throws Exception {
      if (this.bZoomEnabled) {
         if (this.zoomLevels == null) {
            this.zoomLevels = this.zoomLevelsDefault;
         }

         this.zoomedInLevel = this.zoomLevels[this.zoomLevels.length - 1];
         this.zoomedOutLevel = this.zoomLevels[0];
         int var3 = ImageUtils.getNextPowerOfTwoHW(var1);
         int var4 = ImageUtils.getNextPowerOfTwoHW(var2);
         this.Current = this.createTexture(var3, var4, false);
      }
   }

   public void update() {
      int var1 = IsoPlayer.getPlayerIndex();
      if (!this.bZoomEnabled) {
         this.zoom[var1] = this.targetZoom[var1] = 1.0F;
      }

      float var2;
      if (this.bAutoZoom[var1] && IsoCamera.CamCharacter != null && this.bZoomEnabled) {
         var2 = IsoUtils.DistanceTo(IsoCamera.getRightClickOffX(), IsoCamera.getRightClickOffY(), 0.0F, 0.0F);
         float var3 = var2 / 300.0F;
         if (var3 > 1.0F) {
            var3 = 1.0F;
         }

         float var4 = this.shouldAutoZoomIn() ? this.zoomedInLevel : this.zoomedOutLevel;
         var4 += var3;
         if (var4 > this.zoomLevels[0]) {
            var4 = this.zoomLevels[0];
         }

         if (IsoCamera.CamCharacter.getVehicle() != null) {
            var4 = this.getMaxZoom();
         }

         this.setTargetZoom(var1, var4);
      }

      var2 = 0.004F * GameTime.instance.getMultiplier() / GameTime.instance.getTrueMultiplier() * (Core.TileScale == 2 ? 1.5F : 1.5F);
      if (!this.bAutoZoom[var1]) {
         var2 *= 5.0F;
      } else if (this.targetZoom[var1] > this.zoom[var1]) {
         var2 *= 1.0F;
      }

      float[] var10000;
      if (this.targetZoom[var1] > this.zoom[var1]) {
         var10000 = this.zoom;
         var10000[var1] += var2;
         IsoPlayer.players[var1].dirtyRecalcGridStackTime = 2.0F;
         if (this.zoom[var1] > this.targetZoom[var1] || Math.abs(this.zoom[var1] - this.targetZoom[var1]) < 0.001F) {
            this.zoom[var1] = this.targetZoom[var1];
         }
      }

      if (this.targetZoom[var1] < this.zoom[var1]) {
         var10000 = this.zoom;
         var10000[var1] -= var2;
         IsoPlayer.players[var1].dirtyRecalcGridStackTime = 2.0F;
         if (this.zoom[var1] < this.targetZoom[var1] || Math.abs(this.zoom[var1] - this.targetZoom[var1]) < 0.001F) {
            this.zoom[var1] = this.targetZoom[var1];
         }
      }

      this.setCameraToCentre();
   }

   private boolean shouldAutoZoomIn() {
      if (IsoCamera.CamCharacter == null) {
         return false;
      } else {
         IsoGridSquare var1 = IsoCamera.CamCharacter.getCurrentSquare();
         if (var1 != null && !var1.isOutside()) {
            return true;
         } else {
            IsoPlayer var2 = (IsoPlayer)Type.tryCastTo(IsoCamera.CamCharacter, IsoPlayer.class);
            if (var2 == null) {
               return false;
            } else if (!var2.isRunning() && !var2.isSprinting()) {
               if (var2.closestZombie < 6.0F && var2.isTargetedByZombie()) {
                  return true;
               } else {
                  return var2.lastTargeted < (float)(PerformanceSettings.getLockFPS() * 4);
               }
            } else {
               return false;
            }
         }
      }
   }

   private void setCameraToCentre() {
      PlayerCamera var1 = IsoCamera.cameras[IsoPlayer.getPlayerIndex()];
      var1.center();
   }

   private TextureFBO createTexture(int var1, int var2, boolean var3) {
      Texture var4;
      if (var3) {
         var4 = new Texture(var1, var2, 16);
         TextureFBO var5 = new TextureFBO(var4);
         var5.destroy();
         return null;
      } else {
         var4 = new Texture(var1, var2, 19);
         return new TextureFBO(var4);
      }
   }

   public void render() {
      if (this.Current != null) {
         int var1 = 0;

         int var2;
         for(var2 = 3; var2 >= 0; --var2) {
            if (IsoPlayer.players[var2] != null) {
               var1 = var2 > 1 ? 3 : var2;
               break;
            }
         }

         var1 = Math.max(var1, IsoPlayer.numPlayers - 1);

         for(var2 = 0; var2 <= var1; ++var2) {
            if (Core.getInstance().RenderShader != null) {
               IndieGL.StartShader(Core.getInstance().RenderShader, var2);
            }

            int var3 = IsoCamera.getScreenLeft(var2);
            int var4 = IsoCamera.getScreenTop(var2);
            int var5 = IsoCamera.getScreenWidth(var2);
            int var6 = IsoCamera.getScreenHeight(var2);
            if (IsoPlayer.players[var2] != null || GameServer.bServer && ServerGUI.isCreated()) {
               ((Texture)this.Current.getTexture()).rendershader2((float)var3, (float)var4, (float)var5, (float)var6, var3, var4, var5, var6, 1.0F, 1.0F, 1.0F, 1.0F);
            } else {
               SpriteRenderer.instance.renderi((Texture)null, var3, var4, var5, var6, 0.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
            }
         }

         if (Core.getInstance().RenderShader != null) {
            IndieGL.EndShader();
         }

         IsoCursor.getInstance().render(0);
      }
   }

   public TextureFBO getCurrent(int var1) {
      return this.Current;
   }

   public Texture getTexture(int var1) {
      return (Texture)this.Current.getTexture();
   }

   public void doZoomScroll(int var1, int var2) {
      this.targetZoom[var1] = this.getNextZoom(var1, var2);
   }

   public float getNextZoom(int var1, int var2) {
      if (this.bZoomEnabled && this.zoomLevels != null) {
         int var3;
         if (var2 > 0) {
            for(var3 = this.zoomLevels.length - 1; var3 > 0; --var3) {
               if (this.targetZoom[var1] == this.zoomLevels[var3]) {
                  return this.zoomLevels[var3 - 1];
               }
            }
         } else if (var2 < 0) {
            for(var3 = 0; var3 < this.zoomLevels.length - 1; ++var3) {
               if (this.targetZoom[var1] == this.zoomLevels[var3]) {
                  return this.zoomLevels[var3 + 1];
               }
            }
         }

         return this.targetZoom[var1];
      } else {
         return 1.0F;
      }
   }

   public float getMinZoom() {
      return this.bZoomEnabled && this.zoomLevels != null && this.zoomLevels.length != 0 ? this.zoomLevels[this.zoomLevels.length - 1] : 1.0F;
   }

   public float getMaxZoom() {
      return this.bZoomEnabled && this.zoomLevels != null && this.zoomLevels.length != 0 ? this.zoomLevels[0] : 1.0F;
   }

   public boolean test() {
      try {
         this.createTexture(16, 16, true);
         return true;
      } catch (Exception var2) {
         DebugLog.General.error("Failed to create Test FBO");
         var2.printStackTrace();
         Core.SafeMode = true;
         return false;
      }
   }
}
