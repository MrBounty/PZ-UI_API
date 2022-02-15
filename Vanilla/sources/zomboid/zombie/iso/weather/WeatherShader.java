package zombie.iso.weather;

import org.lwjgl.opengl.ARBShaderObjects;
import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.opengl.RenderSettings;
import zombie.core.opengl.Shader;
import zombie.core.opengl.ShaderProgram;
import zombie.core.textures.TextureDraw;
import zombie.iso.IsoCamera;
import zombie.iso.PlayerCamera;
import zombie.iso.SearchMode;

public class WeatherShader extends Shader {
   public int timeOfDay = 0;
   private int PixelOffset;
   private int PixelSize;
   private int bloom;
   private int timer;
   private int BlurStrength;
   private int TextureSize;
   private int Zoom;
   private int Light;
   private int LightIntensity;
   private int NightValue;
   private int Exterior;
   private int NightVisionGoggles;
   private int DesaturationVal;
   private int FogMod;
   private int SearchModeID;
   private int ScreenInfo;
   private int ParamInfo;
   private int VarInfo;
   private int timerVal;
   private boolean bAlt = false;
   private static final int texdVarsSize = 22;
   private static float[][] floatArrs = new float[5][];

   public WeatherShader(String var1) {
      super(var1);
   }

   public void startMainThread(TextureDraw var1, int var2) {
      if (var2 >= 0 && var2 < 4) {
         RenderSettings.PlayerRenderSettings var3 = RenderSettings.getInstance().getPlayerSettings(var2);
         IsoPlayer var4 = IsoPlayer.players[var2];
         boolean var5 = var3.isExterior();
         float var6 = GameTime.instance.TimeOfDay / 12.0F - 1.0F;
         if (Math.abs(var6) > 0.8F && var4 != null && var4.Traits.NightVision.isSet() && !var4.isWearingNightVisionGoggles()) {
            var6 *= 0.8F;
         }

         int var7 = Core.getInstance().getOffscreenWidth(var2);
         int var8 = Core.getInstance().getOffscreenHeight(var2);
         if (var1.vars == null) {
            var1.vars = getFreeFloatArray();
            if (var1.vars == null) {
               var1.vars = new float[22];
            }
         }

         var1.vars[0] = var3.getBlendColor().r;
         var1.vars[1] = var3.getBlendColor().g;
         var1.vars[2] = var3.getBlendColor().b;
         var1.vars[3] = var3.getBlendIntensity();
         var1.vars[4] = var3.getDesaturation();
         var1.vars[5] = var3.isApplyNightVisionGoggles() ? 1.0F : 0.0F;
         SearchMode.PlayerSearchMode var9 = SearchMode.getInstance().getSearchModeForPlayer(var2);
         var1.vars[6] = var9.getShaderBlur();
         var1.vars[7] = var9.getShaderRadius();
         var1.vars[8] = (float)IsoCamera.getOffscreenLeft(var2);
         var1.vars[9] = (float)IsoCamera.getOffscreenTop(var2);
         PlayerCamera var10 = IsoCamera.cameras[var2];
         var1.vars[10] = (float)IsoCamera.getOffscreenWidth(var2);
         var1.vars[11] = (float)IsoCamera.getOffscreenHeight(var2);
         var1.vars[12] = var10.RightClickX;
         var1.vars[13] = var10.RightClickY;
         var1.vars[14] = Core.getInstance().getZoom(var2);
         var1.vars[15] = Core.TileScale == 2 ? 64.0F : 32.0F;
         var1.vars[16] = var9.getShaderGradientWidth() * var1.vars[15] / 2.0F;
         var1.vars[17] = var9.getShaderDesat();
         var1.vars[18] = var9.isShaderEnabled() ? 1.0F : 0.0F;
         var1.vars[19] = var9.getShaderDarkness();
         var1.flipped = var3.isExterior();
         var1.f1 = var3.getDarkness();
         var1.col0 = var7;
         var1.col1 = var8;
         var1.col2 = Core.getInstance().getOffscreenTrueWidth();
         var1.col3 = Core.getInstance().getOffscreenTrueHeight();
         var1.bSingleCol = Core.getInstance().getZoom(var2) > 2.0F || (double)Core.getInstance().getZoom(var2) < 2.0D && Core.getInstance().getZoom(var2) >= 1.75F;
      }
   }

   public void startRenderThread(TextureDraw var1) {
      float var2 = var1.f1;
      boolean var3 = var1.flipped;
      int var4 = var1.col0;
      int var5 = var1.col1;
      int var6 = var1.col2;
      int var7 = var1.col3;
      float var8 = var1.bSingleCol ? 1.0F : 0.0F;
      ARBShaderObjects.glUniform1fARB(this.width, (float)var4);
      ARBShaderObjects.glUniform1fARB(this.height, (float)var5);
      ARBShaderObjects.glUniform3fARB(this.Light, var1.vars[0], var1.vars[1], var1.vars[2]);
      ARBShaderObjects.glUniform1fARB(this.LightIntensity, var1.vars[3]);
      ARBShaderObjects.glUniform1fARB(this.NightValue, var2);
      ARBShaderObjects.glUniform1fARB(this.DesaturationVal, var1.vars[4]);
      ARBShaderObjects.glUniform1fARB(this.NightVisionGoggles, var1.vars[5]);
      ARBShaderObjects.glUniform1fARB(this.Exterior, var3 ? 1.0F : 0.0F);
      ARBShaderObjects.glUniform1fARB(this.timer, (float)(this.timerVal / 2));
      if (PerformanceSettings.getLockFPS() >= 60) {
         if (this.bAlt) {
            ++this.timerVal;
         }

         this.bAlt = !this.bAlt;
      } else {
         this.timerVal += 2;
      }

      float var9 = 0.0F;
      float var10 = 0.0F;
      float var11 = 1.0F / (float)var4;
      float var12 = 1.0F / (float)var5;
      ARBShaderObjects.glUniform2fARB(this.TextureSize, (float)var6, (float)var7);
      ARBShaderObjects.glUniform1fARB(this.Zoom, var8);
      ARBShaderObjects.glUniform4fARB(this.SearchModeID, var1.vars[6], var1.vars[7], var1.vars[8], var1.vars[9]);
      ARBShaderObjects.glUniform4fARB(this.ScreenInfo, var1.vars[10], var1.vars[11], var1.vars[12], var1.vars[13]);
      ARBShaderObjects.glUniform4fARB(this.ParamInfo, var1.vars[14], var1.vars[15], var1.vars[16], var1.vars[17]);
      ARBShaderObjects.glUniform4fARB(this.VarInfo, var1.vars[18], var1.vars[19], var1.vars[20], var1.vars[21]);
   }

   public void onCompileSuccess(ShaderProgram var1) {
      int var2 = this.getID();
      this.timeOfDay = ARBShaderObjects.glGetUniformLocationARB(var2, "TimeOfDay");
      this.bloom = ARBShaderObjects.glGetUniformLocationARB(var2, "BloomVal");
      this.PixelOffset = ARBShaderObjects.glGetUniformLocationARB(var2, "PixelOffset");
      this.PixelSize = ARBShaderObjects.glGetUniformLocationARB(var2, "PixelSize");
      this.BlurStrength = ARBShaderObjects.glGetUniformLocationARB(var2, "BlurStrength");
      this.width = ARBShaderObjects.glGetUniformLocationARB(var2, "bgl_RenderedTextureWidth");
      this.height = ARBShaderObjects.glGetUniformLocationARB(var2, "bgl_RenderedTextureHeight");
      this.timer = ARBShaderObjects.glGetUniformLocationARB(var2, "timer");
      this.TextureSize = ARBShaderObjects.glGetUniformLocationARB(var2, "TextureSize");
      this.Zoom = ARBShaderObjects.glGetUniformLocationARB(var2, "Zoom");
      this.Light = ARBShaderObjects.glGetUniformLocationARB(var2, "Light");
      this.LightIntensity = ARBShaderObjects.glGetUniformLocationARB(var2, "LightIntensity");
      this.NightValue = ARBShaderObjects.glGetUniformLocationARB(var2, "NightValue");
      this.Exterior = ARBShaderObjects.glGetUniformLocationARB(var2, "Exterior");
      this.NightVisionGoggles = ARBShaderObjects.glGetUniformLocationARB(var2, "NightVisionGoggles");
      this.DesaturationVal = ARBShaderObjects.glGetUniformLocationARB(var2, "DesaturationVal");
      this.FogMod = ARBShaderObjects.glGetUniformLocationARB(var2, "FogMod");
      this.SearchModeID = ARBShaderObjects.glGetUniformLocationARB(var2, "SearchMode");
      this.ScreenInfo = ARBShaderObjects.glGetUniformLocationARB(var2, "ScreenInfo");
      this.ParamInfo = ARBShaderObjects.glGetUniformLocationARB(var2, "ParamInfo");
      this.VarInfo = ARBShaderObjects.glGetUniformLocationARB(var2, "VarInfo");
   }

   public void postRender(TextureDraw var1) {
      if (var1.vars != null) {
         returnFloatArray(var1.vars);
         var1.vars = null;
      }

   }

   private static float[] getFreeFloatArray() {
      for(int var0 = 0; var0 < floatArrs.length; ++var0) {
         if (floatArrs[var0] != null) {
            float[] var1 = floatArrs[var0];
            floatArrs[var0] = null;
            return var1;
         }
      }

      return new float[22];
   }

   private static void returnFloatArray(float[] var0) {
      for(int var1 = 0; var1 < floatArrs.length; ++var1) {
         if (floatArrs[var1] == null) {
            floatArrs[var1] = var0;
            break;
         }
      }

   }
}
