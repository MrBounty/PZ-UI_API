package zombie.iso.objects;

import zombie.GameTime;
import zombie.core.opengl.Shader;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoCell;
import zombie.iso.IsoMovingObject;
import zombie.iso.sprite.IsoSpriteInstance;

public class IsoZombieHead extends IsoMovingObject {
   public float tintb = 1.0F;
   public float tintg = 1.0F;
   public float tintr = 1.0F;
   public float time = 0.0F;

   public IsoZombieHead(IsoCell var1) {
      super(var1);
   }

   public boolean Serialize() {
      return false;
   }

   public String getObjectName() {
      return "ZombieHead";
   }

   public void update() {
      super.update();
      this.time += GameTime.instance.getMultipliedSecondsSinceLastUpdate();
      this.sx = this.sy = 0.0F;
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      this.setTargetAlpha(1.0F);
      super.render(var1, var2, var3, var4, var5, var6, var7);
   }

   public IsoZombieHead(IsoZombieHead.GibletType var1, IsoCell var2, float var3, float var4, float var5) {
      super(var2);
      this.solid = false;
      this.shootable = false;
      this.x = var3;
      this.y = var4;
      this.z = var5;
      this.nx = var3;
      this.ny = var4;
      this.setAlpha(0.5F);
      this.def = IsoSpriteInstance.get(this.sprite);
      this.def.alpha = 1.0F;
      this.sprite.def.alpha = 1.0F;
      this.offsetX = -26.0F;
      this.offsetY = -242.0F;
      switch(var1) {
      case A:
         this.sprite.LoadFramesNoDirPageDirect("media/gibs/Giblet", "00", 3);
         break;
      case B:
         this.sprite.LoadFramesNoDirPageDirect("media/gibs/Giblet", "01", 3);
      }

   }

   public static enum GibletType {
      A,
      B,
      Eye;

      // $FF: synthetic method
      private static IsoZombieHead.GibletType[] $values() {
         return new IsoZombieHead.GibletType[]{A, B, Eye};
      }
   }
}
