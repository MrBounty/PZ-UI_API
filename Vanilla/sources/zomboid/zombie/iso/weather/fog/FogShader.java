package zombie.iso.weather.fog;

import org.lwjgl.opengl.ARBShaderObjects;
import zombie.IndieGL;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.ShaderProgram;

public class FogShader {
   public static final FogShader instance = new FogShader();
   private ShaderProgram shaderProgram;
   private int noiseTexture;
   private int screenInfo;
   private int textureInfo;
   private int rectangleInfo;
   private int worldOffset;
   private int scalingInfo;
   private int colorInfo;
   private int paramInfo;
   private int cameraInfo;

   public void initShader() {
      this.shaderProgram = ShaderProgram.createShaderProgram("fog", false, true);
      if (this.shaderProgram.isCompiled()) {
         this.noiseTexture = ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram.getShaderID(), "NoiseTexture");
         this.screenInfo = ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram.getShaderID(), "screenInfo");
         this.textureInfo = ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram.getShaderID(), "textureInfo");
         this.rectangleInfo = ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram.getShaderID(), "rectangleInfo");
         this.scalingInfo = ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram.getShaderID(), "scalingInfo");
         this.colorInfo = ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram.getShaderID(), "colorInfo");
         this.worldOffset = ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram.getShaderID(), "worldOffset");
         this.paramInfo = ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram.getShaderID(), "paramInfo");
         this.cameraInfo = ARBShaderObjects.glGetUniformLocationARB(this.shaderProgram.getShaderID(), "cameraInfo");
         ARBShaderObjects.glUseProgramObjectARB(this.shaderProgram.getShaderID());
         ARBShaderObjects.glUseProgramObjectARB(0);
      }

   }

   public void setScreenInfo(float var1, float var2, float var3, float var4) {
      SpriteRenderer.instance.ShaderUpdate4f(this.shaderProgram.getShaderID(), this.screenInfo, var1, var2, var3, var4);
   }

   public void setTextureInfo(float var1, float var2, float var3, float var4) {
      SpriteRenderer.instance.ShaderUpdate4f(this.shaderProgram.getShaderID(), this.textureInfo, var1, var2, var3, var4);
   }

   public void setRectangleInfo(float var1, float var2, float var3, float var4) {
      SpriteRenderer.instance.ShaderUpdate4f(this.shaderProgram.getShaderID(), this.rectangleInfo, var1, var2, var3, var4);
   }

   public void setScalingInfo(float var1, float var2, float var3, float var4) {
      SpriteRenderer.instance.ShaderUpdate4f(this.shaderProgram.getShaderID(), this.scalingInfo, var1, var2, var3, var4);
   }

   public void setColorInfo(float var1, float var2, float var3, float var4) {
      SpriteRenderer.instance.ShaderUpdate4f(this.shaderProgram.getShaderID(), this.colorInfo, var1, var2, var3, var4);
   }

   public void setWorldOffset(float var1, float var2, float var3, float var4) {
      SpriteRenderer.instance.ShaderUpdate4f(this.shaderProgram.getShaderID(), this.worldOffset, var1, var2, var3, var4);
   }

   public void setParamInfo(float var1, float var2, float var3, float var4) {
      SpriteRenderer.instance.ShaderUpdate4f(this.shaderProgram.getShaderID(), this.paramInfo, var1, var2, var3, var4);
   }

   public void setCameraInfo(float var1, float var2, float var3, float var4) {
      SpriteRenderer.instance.ShaderUpdate4f(this.shaderProgram.getShaderID(), this.cameraInfo, var1, var2, var3, var4);
   }

   public boolean StartShader() {
      if (this.shaderProgram == null) {
         RenderThread.invokeOnRenderContext(this::initShader);
      }

      if (this.shaderProgram.isCompiled()) {
         IndieGL.StartShader(this.shaderProgram.getShaderID(), 0);
         return true;
      } else {
         return false;
      }
   }

   protected void reloadShader() {
      if (this.shaderProgram != null) {
         this.shaderProgram = null;
      }

   }
}
