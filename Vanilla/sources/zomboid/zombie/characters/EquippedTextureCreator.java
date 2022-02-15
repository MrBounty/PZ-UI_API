package zombie.characters;

import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import zombie.characterTextures.ItemSmartTexture;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.model.ModelInstance;
import zombie.core.textures.SmartTexture;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.HandWeapon;
import zombie.popman.ObjectPool;

public final class EquippedTextureCreator extends TextureDraw.GenericDrawer {
   private boolean bRendered;
   private ModelInstance modelInstance;
   private float bloodLevel;
   private final ArrayList texturesNotReady = new ArrayList();
   private static final ObjectPool pool = new ObjectPool(EquippedTextureCreator::new);

   public void init(ModelInstance var1, InventoryItem var2) {
      float var3 = 0.0F;
      if (var2 instanceof HandWeapon) {
         var3 = ((HandWeapon)var2).getBloodLevel();
      }

      this.init(var1, var3);
   }

   public void init(ModelInstance var1, float var2) {
      this.bRendered = false;
      this.texturesNotReady.clear();
      this.modelInstance = var1;
      this.bloodLevel = var2;
      if (this.modelInstance != null) {
         ++this.modelInstance.renderRefCount;
         Texture var3 = this.modelInstance.tex;
         if (var3 instanceof SmartTexture) {
            var3 = null;
         }

         if (var3 != null && !var3.isReady()) {
            this.texturesNotReady.add(var3);
         }

         var3 = Texture.getSharedTexture("media/textures/BloodTextures/BloodOverlayWeapon.png");
         if (var3 != null && !var3.isReady()) {
            this.texturesNotReady.add(var3);
         }

         var3 = Texture.getSharedTexture("media/textures/BloodTextures/BloodOverlayWeaponMask.png");
         if (var3 != null && !var3.isReady()) {
            this.texturesNotReady.add(var3);
         }
      }

   }

   public void render() {
      for(int var1 = 0; var1 < this.texturesNotReady.size(); ++var1) {
         Texture var2 = (Texture)this.texturesNotReady.get(var1);
         if (!var2.isReady()) {
            return;
         }
      }

      GL11.glPushAttrib(2048);

      try {
         this.updateTexture(this.modelInstance, this.bloodLevel);
      } finally {
         GL11.glPopAttrib();
      }

      this.bRendered = true;
   }

   private void updateTexture(ModelInstance var1, float var2) {
      if (var1 != null) {
         ItemSmartTexture var3 = null;
         if (var2 > 0.0F) {
            if (var1.tex instanceof ItemSmartTexture) {
               var3 = (ItemSmartTexture)var1.tex;
            } else if (var1.tex != null) {
               var3 = new ItemSmartTexture(var1.tex.getName());
            }
         } else if (var1.tex instanceof ItemSmartTexture) {
            var3 = (ItemSmartTexture)var1.tex;
         }

         if (var3 != null) {
            var3.setBlood("media/textures/BloodTextures/BloodOverlayWeapon.png", "media/textures/BloodTextures/BloodOverlayWeaponMask.png", var2, 300);
            var3.calculate();
            var1.tex = var3;
         }
      }
   }

   public void postRender() {
      ModelManager.instance.derefModelInstance(this.modelInstance);
      this.texturesNotReady.clear();
      if (!this.bRendered) {
      }

      pool.release((Object)this);
   }

   public boolean isRendered() {
      return this.bRendered;
   }

   public static EquippedTextureCreator alloc() {
      return (EquippedTextureCreator)pool.alloc();
   }
}
