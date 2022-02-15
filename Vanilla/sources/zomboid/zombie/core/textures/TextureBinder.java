package zombie.core.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class TextureBinder {
   public static TextureBinder instance = new TextureBinder();
   public int maxTextureUnits = 0;
   public int[] textureUnitIDs;
   public int textureUnitIDStart = 33984;
   public int textureIndex = 0;
   public int activeTextureIndex = 0;

   public TextureBinder() {
      this.maxTextureUnits = 1;
      this.textureUnitIDs = new int[this.maxTextureUnits];

      for(int var1 = 0; var1 < this.maxTextureUnits; ++var1) {
         this.textureUnitIDs[var1] = -1;
      }

   }

   public void bind(int var1) {
      for(int var2 = 0; var2 < this.maxTextureUnits; ++var2) {
         if (this.textureUnitIDs[var2] == var1) {
            int var3 = var2 + this.textureUnitIDStart;
            GL13.glActiveTexture(var3);
            this.activeTextureIndex = var3;
            return;
         }
      }

      this.textureUnitIDs[this.textureIndex] = var1;
      GL13.glActiveTexture(this.textureUnitIDStart + this.textureIndex);
      GL11.glBindTexture(3553, var1);
      this.activeTextureIndex = this.textureUnitIDStart + this.textureIndex;
      ++this.textureIndex;
      if (this.textureIndex >= this.maxTextureUnits) {
         this.textureIndex = 0;
      }

   }
}
