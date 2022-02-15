package zombie.iso.objects;

import zombie.GameTime;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.opengl.Shader;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoCell;
import zombie.iso.IsoPhysicsObject;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSpriteInstance;

public class IsoZombieGiblets extends IsoPhysicsObject {
   public float tintb = 1.0F;
   public float tintg = 1.0F;
   public float tintr = 1.0F;
   public float time = 0.0F;
   boolean invis = false;

   public IsoZombieGiblets(IsoCell var1) {
      super(var1);
   }

   public boolean Serialize() {
      return false;
   }

   public String getObjectName() {
      return "ZombieGiblets";
   }

   public void update() {
      if (Rand.Next(Rand.AdjustForFramerate(12)) == 0 && this.getZ() > (float)((int)this.getZ()) && this.getCurrentSquare() != null && this.getCurrentSquare().getChunk() != null) {
         this.getCurrentSquare().getChunk().addBloodSplat(this.x, this.y, (float)((int)this.z), Rand.Next(8));
      }

      if (Core.bLastStand && Rand.Next(Rand.AdjustForFramerate(15)) == 0 && this.getZ() > (float)((int)this.getZ()) && this.getCurrentSquare() != null && this.getCurrentSquare().getChunk() != null) {
         this.getCurrentSquare().getChunk().addBloodSplat(this.x, this.y, (float)((int)this.z), Rand.Next(8));
      }

      super.update();
      this.time += GameTime.instance.getMultipliedSecondsSinceLastUpdate();
      if (this.velX == 0.0F && this.velY == 0.0F && this.getZ() == (float)((int)this.getZ())) {
         this.setCollidable(false);
         IsoWorld.instance.CurrentCell.getRemoveList().add(this);
      }

   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      if (!this.invis) {
         float var8 = var4.r;
         float var9 = var4.g;
         float var10 = var4.b;
         var4.r = 0.5F;
         var4.g = 0.5F;
         var4.b = 0.5F;
         this.setTargetAlpha(this.sprite.def.targetAlpha = this.def.targetAlpha = 1.0F - this.time / 1.0F);
         super.render(var1, var2, var3, var4, var5, var6, var7);
         if (Core.bDebug) {
         }

         var4.r = var8;
         var4.g = var9;
         var4.b = var10;
      }
   }

   public IsoZombieGiblets(IsoZombieGiblets.GibletType var1, IsoCell var2, float var3, float var4, float var5, float var6, float var7) {
      super(var2);
      this.velX = var6;
      this.velY = var7;
      float var8 = (float)Rand.Next(4000) / 10000.0F;
      float var9 = (float)Rand.Next(4000) / 10000.0F;
      var8 -= 0.2F;
      var9 -= 0.2F;
      this.velX += var8;
      this.velY += var9;
      this.x = var3;
      this.y = var4;
      this.z = var5;
      this.nx = var3;
      this.ny = var4;
      this.setAlpha(0.2F);
      this.def = IsoSpriteInstance.get(this.sprite);
      this.def.alpha = 0.2F;
      this.sprite.def.alpha = 0.4F;
      this.offsetX = 0.0F;
      this.offsetY = 0.0F;
      if (Rand.Next(3) != 0) {
         this.def.alpha = 0.0F;
         this.sprite.def.alpha = 0.0F;
         this.invis = true;
      }

      switch(var1) {
      case A:
         this.sprite.setFromCache("Giblet", "00", 3);
         break;
      case B:
         this.sprite.setFromCache("Giblet", "01", 3);
         break;
      case Eye:
         this.sprite.setFromCache("Eyeball", "00", 1);
      }

   }

   public static enum GibletType {
      A,
      B,
      Eye;

      // $FF: synthetic method
      private static IsoZombieGiblets.GibletType[] $values() {
         return new IsoZombieGiblets.GibletType[]{A, B, Eye};
      }
   }
}
