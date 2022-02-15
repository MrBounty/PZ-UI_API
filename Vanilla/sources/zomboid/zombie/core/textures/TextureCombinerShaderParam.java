package zombie.core.textures;

public final class TextureCombinerShaderParam {
   public String name;
   public float min;
   public float max;

   public TextureCombinerShaderParam(String var1, float var2, float var3) {
      this.name = var1;
      this.min = var2;
      this.max = var3;
   }

   public TextureCombinerShaderParam(String var1, float var2) {
      this.name = var1;
      this.min = var2;
      this.max = var2;
   }
}
