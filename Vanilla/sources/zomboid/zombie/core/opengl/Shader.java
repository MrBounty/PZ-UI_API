package zombie.core.opengl;

import java.util.HashMap;
import org.lwjgl.opengl.ARBShaderObjects;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;

public class Shader implements IShaderProgramListener {
   public static HashMap ShaderMap = new HashMap();
   public String name;
   private int m_shaderMapID = 0;
   private final ShaderProgram m_shaderProgram;
   public Texture tex;
   public int width;
   public int height;

   public Shader(String var1) {
      this.name = var1;
      this.m_shaderProgram = ShaderProgram.createShaderProgram(var1, false, false);
      this.m_shaderProgram.addCompileListener(this);
      this.m_shaderProgram.compile();
   }

   public void setTexture(Texture var1) {
      this.tex = var1;
   }

   public int getID() {
      return this.m_shaderProgram.getShaderID();
   }

   public void Start() {
      ARBShaderObjects.glUseProgramObjectARB(this.m_shaderProgram.getShaderID());
   }

   public void End() {
      ARBShaderObjects.glUseProgramObjectARB(0);
   }

   public void destroy() {
      this.m_shaderProgram.destroy();
      ShaderMap.remove(this.m_shaderMapID);
      this.m_shaderMapID = 0;
   }

   public void startMainThread(TextureDraw var1, int var2) {
   }

   public void startRenderThread(TextureDraw var1) {
   }

   public void postRender(TextureDraw var1) {
   }

   public boolean isCompiled() {
      return this.m_shaderProgram.isCompiled();
   }

   public void callback(ShaderProgram var1) {
      ShaderMap.remove(this.m_shaderMapID);
      this.m_shaderMapID = var1.getShaderID();
      ShaderMap.put(this.m_shaderMapID, this);
      this.onCompileSuccess(var1);
   }

   protected void onCompileSuccess(ShaderProgram var1) {
   }

   public ShaderProgram getProgram() {
      return this.m_shaderProgram;
   }
}
