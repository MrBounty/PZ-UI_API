package zombie.core.skinnedmodel.shader;

import java.util.ArrayList;

public final class ShaderManager {
   public static final ShaderManager instance = new ShaderManager();
   private final ArrayList shaders = new ArrayList();

   private Shader getShader(String var1, boolean var2) {
      for(int var3 = 0; var3 < this.shaders.size(); ++var3) {
         Shader var4 = (Shader)this.shaders.get(var3);
         if (var1.equals(var4.name) && var2 == var4.bStatic) {
            return var4;
         }
      }

      return null;
   }

   public Shader getOrCreateShader(String var1, boolean var2) {
      Shader var3 = this.getShader(var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         for(int var4 = 0; var4 < this.shaders.size(); ++var4) {
            Shader var5 = (Shader)this.shaders.get(var4);
            if (var5.name.equalsIgnoreCase(var1) && !var5.name.equals(var1)) {
               throw new IllegalArgumentException("shader filenames are case-sensitive");
            }
         }

         var3 = new Shader(var1, var2);
         this.shaders.add(var3);
         return var3;
      }
   }
}
