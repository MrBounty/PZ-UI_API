package zombie.core.skinnedmodel.Texture;

import zombie.core.textures.Texture;

public class Texture2D {
   Texture tex;

   public Texture2D(Texture var1) {
      this.tex = var1;
   }

   public int getWidth() {
      return this.tex.getWidth();
   }

   public int getHeight() {
      return this.tex.getHeight();
   }

   public int getTexture() {
      return this.tex.getID();
   }

   public void Apply() {
   }
}
