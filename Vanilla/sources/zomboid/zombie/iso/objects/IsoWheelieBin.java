package zombie.iso.objects;

import zombie.characters.IsoPlayer;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoCell;
import zombie.iso.IsoDirections;
import zombie.iso.IsoPushableObject;

public class IsoWheelieBin extends IsoPushableObject {
   float velx = 0.0F;
   float vely = 0.0F;

   public String getObjectName() {
      return "WheelieBin";
   }

   public IsoWheelieBin(IsoCell var1) {
      super(var1);
      this.container = new ItemContainer("wheeliebin", this.square, this);
      this.Collidable = true;
      this.solid = true;
      this.shootable = false;
      this.width = 0.3F;
      this.dir = IsoDirections.E;
      this.setAlphaAndTarget(0.0F);
      this.offsetX = -26.0F;
      this.offsetY = -248.0F;
      this.OutlineOnMouseover = true;
      this.sprite.LoadFramesPageSimple("TileObjectsExt_7", "TileObjectsExt_5", "TileObjectsExt_6", "TileObjectsExt_8");
   }

   public IsoWheelieBin(IsoCell var1, int var2, int var3, int var4) {
      super(var1, var2, var3, var4);
      this.x = (float)var2 + 0.5F;
      this.y = (float)var3 + 0.5F;
      this.z = (float)var4;
      this.nx = this.x;
      this.ny = this.y;
      this.offsetX = -26.0F;
      this.offsetY = -248.0F;
      this.weight = 6.0F;
      this.sprite.LoadFramesPageSimple("TileObjectsExt_7", "TileObjectsExt_5", "TileObjectsExt_6", "TileObjectsExt_8");
      this.square = this.getCell().getGridSquare(var2, var3, var4);
      this.current = this.getCell().getGridSquare(var2, var3, var4);
      this.container = new ItemContainer("wheeliebin", this.square, this);
      this.Collidable = true;
      this.solid = true;
      this.shootable = false;
      this.width = 0.3F;
      this.dir = IsoDirections.E;
      this.setAlphaAndTarget(0.0F);
      this.OutlineOnMouseover = true;
   }

   public void update() {
      this.velx = this.getX() - this.getLx();
      this.vely = this.getY() - this.getLy();
      float var1 = 1.0F - this.container.getContentsWeight() / 500.0F;
      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      if (var1 < 0.7F) {
         var1 *= var1;
      }

      if (IsoPlayer.getInstance() != null && IsoPlayer.getInstance().getDragObject() != this) {
         if (this.velx != 0.0F && this.vely == 0.0F && (this.dir == IsoDirections.E || this.dir == IsoDirections.W)) {
            this.setNx(this.getNx() + this.velx * 0.65F * var1);
         }

         if (this.vely != 0.0F && this.velx == 0.0F && (this.dir == IsoDirections.N || this.dir == IsoDirections.S)) {
            this.setNy(this.getNy() + this.vely * 0.65F * var1);
         }
      }

      super.update();
   }

   public float getWeight(float var1, float var2) {
      float var3 = this.container.getContentsWeight() / 500.0F;
      if (var3 < 0.0F) {
         var3 = 0.0F;
      }

      if (var3 > 1.0F) {
         return this.getWeight() * 8.0F;
      } else {
         float var4 = this.getWeight() * var3 + 1.5F;
         if (this.dir != IsoDirections.W && (this.dir != IsoDirections.E || var2 != 0.0F)) {
            return this.dir != IsoDirections.N && (this.dir != IsoDirections.S || var1 != 0.0F) ? var4 * 3.0F : var4 / 2.0F;
         } else {
            return var4 / 2.0F;
         }
      }
   }
}
