package zombie.iso;

import org.joml.Vector4f;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.Shader;
import zombie.core.opengl.ShaderProgram;
import zombie.iso.sprite.SkyBox;

public final class PuddlesShader extends Shader {
   private int WaterGroundTex;
   private int PuddlesHM;
   private int WaterTextureReflectionA;
   private int WaterTextureReflectionB;
   private int WaterTime;
   private int WaterOffset;
   private int WaterViewport;
   private int WaterReflectionParam;
   private int PuddlesParams;

   public PuddlesShader(String var1) {
      super(var1);
   }

   protected void onCompileSuccess(ShaderProgram var1) {
      int var2 = var1.getShaderID();
      this.WaterGroundTex = ARBShaderObjects.glGetUniformLocationARB(var2, "WaterGroundTex");
      this.WaterTextureReflectionA = ARBShaderObjects.glGetUniformLocationARB(var2, "WaterTextureReflectionA");
      this.WaterTextureReflectionB = ARBShaderObjects.glGetUniformLocationARB(var2, "WaterTextureReflectionB");
      this.PuddlesHM = ARBShaderObjects.glGetUniformLocationARB(var2, "PuddlesHM");
      this.WaterTime = ARBShaderObjects.glGetUniformLocationARB(var2, "WTime");
      this.WaterOffset = ARBShaderObjects.glGetUniformLocationARB(var2, "WOffset");
      this.WaterViewport = ARBShaderObjects.glGetUniformLocationARB(var2, "WViewport");
      this.WaterReflectionParam = ARBShaderObjects.glGetUniformLocationARB(var2, "WReflectionParam");
      this.PuddlesParams = ARBShaderObjects.glGetUniformLocationARB(var2, "PuddlesParams");
      this.Start();
      if (this.WaterGroundTex != -1) {
         ARBShaderObjects.glUniform1iARB(this.WaterGroundTex, 0);
      }

      if (this.WaterTextureReflectionA != -1) {
         ARBShaderObjects.glUniform1iARB(this.WaterTextureReflectionA, 1);
      }

      if (this.WaterTextureReflectionB != -1) {
         ARBShaderObjects.glUniform1iARB(this.WaterTextureReflectionB, 2);
      }

      if (this.PuddlesHM != -1) {
         ARBShaderObjects.glUniform1iARB(this.PuddlesHM, 3);
      }

      this.End();
   }

   public void updatePuddlesParams(int var1, int var2) {
      IsoPuddles var3 = IsoPuddles.getInstance();
      SkyBox var4 = SkyBox.getInstance();
      PlayerCamera var5 = SpriteRenderer.instance.getRenderingPlayerCamera(var1);
      GL13.glActiveTexture(33985);
      var4.getTextureCurrent().bind();
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexEnvi(8960, 8704, 7681);
      GL13.glActiveTexture(33986);
      var4.getTexturePrev().bind();
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexEnvi(8960, 8704, 7681);
      GL13.glActiveTexture(33987);
      var3.getHMTexture().bind();
      GL11.glTexParameteri(3553, 10240, 9729);
      GL11.glTexParameteri(3553, 10241, 9729);
      GL11.glTexEnvi(8960, 8704, 7681);
      ARBShaderObjects.glUniform1fARB(this.WaterTime, var3.getShaderTime());
      Vector4f var6 = var3.getShaderOffset();
      ARBShaderObjects.glUniform4fARB(this.WaterOffset, var6.x - 90000.0F, var6.y - 640000.0F, var6.z, var6.w);
      ARBShaderObjects.glUniform4fARB(this.WaterViewport, (float)IsoCamera.getOffscreenLeft(var1), (float)IsoCamera.getOffscreenTop(var1), (float)var5.OffscreenWidth / var5.zoom, (float)var5.OffscreenHeight / var5.zoom);
      ARBShaderObjects.glUniform1fARB(this.WaterReflectionParam, var4.getTextureShift());
      ARBShaderObjects.glUniformMatrix4fvARB(this.PuddlesParams, true, var3.getPuddlesParams(var2));
   }
}
