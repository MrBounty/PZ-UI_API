package zombie.iso;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import zombie.core.opengl.Shader;
import zombie.core.opengl.ShaderProgram;
import zombie.core.textures.TextureDraw;

public class FireShader extends Shader {
   private int mvpMatrix;
   private int FireTime;
   private int FireParam;
   private int FireTexture;

   public FireShader(String var1) {
      super(var1);
   }

   protected void onCompileSuccess(ShaderProgram var1) {
      int var2 = var1.getShaderID();
      this.FireTexture = ARBShaderObjects.glGetUniformLocationARB(var2, "FireTexture");
      this.mvpMatrix = ARBShaderObjects.glGetUniformLocationARB(var2, "mvpMatrix");
      this.FireTime = ARBShaderObjects.glGetUniformLocationARB(var2, "FireTime");
      this.FireParam = ARBShaderObjects.glGetUniformLocationARB(var2, "FireParam");
      this.Start();
      if (this.FireTexture != -1) {
         ARBShaderObjects.glUniform1iARB(this.FireTexture, 0);
      }

      this.End();
   }

   public void updateFireParams(TextureDraw var1, int var2, float var3) {
      ParticlesFire var4 = ParticlesFire.getInstance();
      GL13.glActiveTexture(33984);
      var4.getFireFlameTexture().bind();
      GL11.glTexEnvi(8960, 8704, 7681);
      ARBShaderObjects.glUniformMatrix4fvARB(this.mvpMatrix, true, var4.getMVPMatrix());
      ARBShaderObjects.glUniform1fARB(this.FireTime, var3);
      ARBShaderObjects.glUniformMatrix3fvARB(this.FireParam, true, var4.getParametersFire());
      if (this.FireTexture != -1) {
         ARBShaderObjects.glUniform1iARB(this.FireTexture, 0);
      }

   }
}
