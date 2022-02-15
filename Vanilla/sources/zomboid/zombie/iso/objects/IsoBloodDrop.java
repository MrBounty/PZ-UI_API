package zombie.iso.objects;

import zombie.GameTime;
import zombie.core.Rand;
import zombie.core.opengl.Shader;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoPhysicsObject;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.iso.sprite.IsoSpriteInstance;

public class IsoBloodDrop extends IsoPhysicsObject {
   public float tintb = 1.0F;
   public float tintg = 1.0F;
   public float tintr = 1.0F;
   public float time = 0.0F;
   float sx = 0.0F;
   float sy = 0.0F;
   float lsx = 0.0F;
   float lsy = 0.0F;
   static Vector2 temp = new Vector2();

   public IsoBloodDrop(IsoCell var1) {
      super(var1);
   }

   public boolean Serialize() {
      return false;
   }

   public String getObjectName() {
      return "ZombieGiblets";
   }

   public void collideGround() {
      float var10000 = this.x - (float)((int)this.x);
      var10000 = this.y - (float)((int)this.y);
      IsoGridSquare var3 = IsoWorld.instance.CurrentCell.getGridSquare((int)this.x, (int)this.y, (int)this.z);
      if (var3 != null) {
         IsoObject var4 = var3.getFloor();
         var4.addChild(this);
         this.setCollidable(false);
         IsoWorld.instance.CurrentCell.getRemoveList().add(this);
      }

   }

   public void collideWall() {
      IsoGridSquare var1 = IsoWorld.instance.CurrentCell.getGridSquare((int)this.x, (int)this.y, (int)this.z);
      if (var1 != null) {
         IsoObject var2 = null;
         if (this.isCollidedN()) {
            var2 = var1.getWall(true);
         } else if (this.isCollidedS()) {
            var1 = IsoWorld.instance.CurrentCell.getGridSquare((int)this.x, (int)this.y + 1, (int)this.z);
            if (var1 != null) {
               var2 = var1.getWall(true);
            }
         } else if (this.isCollidedW()) {
            var2 = var1.getWall(false);
         } else if (this.isCollidedE()) {
            var1 = IsoWorld.instance.CurrentCell.getGridSquare((int)this.x + 1, (int)this.y, (int)this.z);
            if (var1 != null) {
               var2 = var1.getWall(false);
            }
         }

         if (var2 != null) {
            var2.addChild(this);
            this.setCollidable(false);
            IsoWorld.instance.CurrentCell.getRemoveList().add(this);
         }
      }

   }

   public void update() {
      super.update();
      this.time += GameTime.instance.getMultipliedSecondsSinceLastUpdate();
      if (this.velX == 0.0F && this.velY == 0.0F && this.getZ() == (float)((int)this.getZ())) {
         this.setCollidable(false);
         IsoWorld.instance.CurrentCell.getRemoveList().add(this);
      }

   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      this.setTargetAlpha(0.3F);
      this.sprite.render(this, var1, var2, var3, this.dir, this.offsetX, this.offsetY, var4, true);
   }

   public IsoBloodDrop(IsoCell var1, float var2, float var3, float var4, float var5, float var6) {
      super(var1);
      this.velX = var5 * 2.0F;
      this.velY = var6 * 2.0F;
      this.terminalVelocity = -0.1F;
      this.velZ += ((float)Rand.Next(10000) / 10000.0F - 0.5F) * 0.05F;
      float var7 = (float)Rand.Next(9000) / 10000.0F;
      var7 += 0.1F;
      this.velX *= var7;
      this.velY *= var7;
      this.velZ += var7 * 0.05F;
      if (Rand.Next(7) == 0) {
         this.velX *= 2.0F;
         this.velY *= 2.0F;
      }

      this.velX *= 0.8F;
      this.velY *= 0.8F;
      temp.x = this.velX;
      temp.y = this.velY;
      temp.rotate(((float)Rand.Next(1000) / 1000.0F - 0.5F) * 0.07F);
      if (Rand.Next(3) == 0) {
         temp.rotate(((float)Rand.Next(1000) / 1000.0F - 0.5F) * 0.1F);
      }

      if (Rand.Next(5) == 0) {
         temp.rotate(((float)Rand.Next(1000) / 1000.0F - 0.5F) * 0.2F);
      }

      if (Rand.Next(8) == 0) {
         temp.rotate(((float)Rand.Next(1000) / 1000.0F - 0.5F) * 0.3F);
      }

      if (Rand.Next(10) == 0) {
         temp.rotate(((float)Rand.Next(1000) / 1000.0F - 0.5F) * 0.4F);
      }

      this.velX = temp.x;
      this.velY = temp.y;
      this.x = var2;
      this.y = var3;
      this.z = var4;
      this.nx = var2;
      this.ny = var3;
      this.setAlpha(0.5F);
      this.def = IsoSpriteInstance.get(this.sprite);
      this.def.alpha = 0.4F;
      this.sprite.def.alpha = 0.4F;
      this.offsetX = -26.0F;
      this.offsetY = -242.0F;
      this.offsetX += 8.0F;
      this.offsetY += 9.0F;
      this.sprite.LoadFramesNoDirPageSimple("BloodSplat");
   }
}
