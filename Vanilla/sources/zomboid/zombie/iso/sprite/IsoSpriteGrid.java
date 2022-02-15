package zombie.iso.sprite;

public final class IsoSpriteGrid {
   private IsoSprite[] sprites;
   private int width;
   private int height;

   public IsoSpriteGrid(int var1, int var2) {
      this.sprites = new IsoSprite[var1 * var2];
      this.width = var1;
      this.height = var2;
   }

   public IsoSprite getAnchorSprite() {
      return this.sprites.length > 0 ? this.sprites[0] : null;
   }

   public IsoSprite getSprite(int var1, int var2) {
      return this.getSpriteFromIndex(var2 * this.width + var1);
   }

   public void setSprite(int var1, int var2, IsoSprite var3) {
      this.sprites[var2 * this.width + var1] = var3;
   }

   public int getSpriteIndex(IsoSprite var1) {
      for(int var3 = 0; var3 < this.sprites.length; ++var3) {
         IsoSprite var2 = this.sprites[var3];
         if (var2 != null && var2 == var1) {
            return var3;
         }
      }

      return -1;
   }

   public int getSpriteGridPosX(IsoSprite var1) {
      int var2 = this.getSpriteIndex(var1);
      return var2 >= 0 ? var2 % this.width : -1;
   }

   public int getSpriteGridPosY(IsoSprite var1) {
      int var2 = this.getSpriteIndex(var1);
      return var2 >= 0 ? var2 / this.width : -1;
   }

   public IsoSprite getSpriteFromIndex(int var1) {
      return var1 >= 0 && var1 < this.sprites.length ? this.sprites[var1] : null;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public boolean validate() {
      for(int var1 = 0; var1 < this.sprites.length; ++var1) {
         if (this.sprites[var1] == null) {
            return false;
         }
      }

      return true;
   }

   public int getSpriteCount() {
      return this.sprites.length;
   }

   public IsoSprite[] getSprites() {
      return this.sprites;
   }
}
