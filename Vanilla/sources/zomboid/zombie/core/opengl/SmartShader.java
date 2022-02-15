package zombie.core.opengl;

import org.lwjgl.util.vector.Matrix4f;
import zombie.core.textures.Texture;
import zombie.iso.Vector2;
import zombie.iso.Vector3;

public final class SmartShader {
   private final ShaderProgram m_shaderProgram;

   public SmartShader(String var1) {
      this.m_shaderProgram = ShaderProgram.createShaderProgram(var1, false, true);
   }

   public SmartShader(String var1, boolean var2) {
      this.m_shaderProgram = ShaderProgram.createShaderProgram(var1, var2, true);
   }

   public void Start() {
      this.m_shaderProgram.Start();
   }

   public void End() {
      this.m_shaderProgram.End();
   }

   public void setValue(String var1, float var2) {
      this.m_shaderProgram.setValue(var1, var2);
   }

   public void setValue(String var1, int var2) {
      this.m_shaderProgram.setValue(var1, var2);
   }

   public void setValue(String var1, Vector3 var2) {
      this.m_shaderProgram.setValue(var1, var2);
   }

   public void setValue(String var1, Vector2 var2) {
      this.m_shaderProgram.setValue(var1, var2);
   }

   public void setVector2f(String var1, float var2, float var3) {
      this.m_shaderProgram.setVector2(var1, var2, var3);
   }

   public void setVector3f(String var1, float var2, float var3, float var4) {
      this.m_shaderProgram.setVector3(var1, var2, var3, var4);
   }

   public void setVector4f(String var1, float var2, float var3, float var4, float var5) {
      this.m_shaderProgram.setVector4(var1, var2, var3, var4, var5);
   }

   public void setValue(String var1, Matrix4f var2) {
      this.m_shaderProgram.setValue(var1, var2);
   }

   public void setValue(String var1, Texture var2, int var3) {
      this.m_shaderProgram.setValue(var1, var2, var3);
   }
}
