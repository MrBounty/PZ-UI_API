package zombie;

import zombie.core.opengl.Shader;
import zombie.util.Pool;
import zombie.util.PooledObject;

public final class ShaderStackEntry extends PooledObject {
   private Shader m_shader;
   private int m_playerIndex;
   private static final Pool s_pool = new Pool(ShaderStackEntry::new);

   public Shader getShader() {
      return this.m_shader;
   }

   public int getPlayerIndex() {
      return this.m_playerIndex;
   }

   public static ShaderStackEntry alloc(Shader var0, int var1) {
      ShaderStackEntry var2 = (ShaderStackEntry)s_pool.alloc();
      var2.m_shader = var0;
      var2.m_playerIndex = var1;
      return var2;
   }
}
