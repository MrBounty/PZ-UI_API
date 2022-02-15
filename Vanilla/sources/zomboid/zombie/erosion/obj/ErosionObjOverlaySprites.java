package zombie.erosion.obj;

import zombie.erosion.ErosionMain;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;

public final class ErosionObjOverlaySprites {
   public String name;
   public int stages;
   private ErosionObjOverlaySprites.Stage[] sprites;

   public ErosionObjOverlaySprites(int var1, String var2) {
      this.name = var2;
      this.stages = var1;
      this.sprites = new ErosionObjOverlaySprites.Stage[this.stages];

      for(int var3 = 0; var3 < this.stages; ++var3) {
         this.sprites[var3] = new ErosionObjOverlaySprites.Stage();
      }

   }

   public IsoSprite getSprite(int var1, int var2) {
      return this.sprites[var1].seasons[var2].getSprite();
   }

   public IsoSpriteInstance getSpriteInstance(int var1, int var2) {
      return this.sprites[var1].seasons[var2].getInstance();
   }

   public void setSprite(int var1, String var2, int var3) {
      this.sprites[var1].seasons[var3] = new ErosionObjOverlaySprites.Sprite(var2);
   }

   private static class Stage {
      public ErosionObjOverlaySprites.Sprite[] seasons = new ErosionObjOverlaySprites.Sprite[6];
   }

   private static final class Sprite {
      private final String sprite;

      public Sprite(String var1) {
         this.sprite = var1;
      }

      public IsoSprite getSprite() {
         return this.sprite != null ? ErosionMain.getInstance().getSpriteManager().getSprite(this.sprite) : null;
      }

      public IsoSpriteInstance getInstance() {
         return this.sprite != null ? ErosionMain.getInstance().getSpriteManager().getSprite(this.sprite).newInstance() : null;
      }
   }
}
