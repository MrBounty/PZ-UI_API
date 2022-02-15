package zombie.core.skinnedmodel.model;

import zombie.characters.EquippedTextureCreator;
import zombie.core.SpriteRenderer;
import zombie.inventory.InventoryItem;
import zombie.inventory.types.HandWeapon;
import zombie.popman.ObjectPool;
import zombie.util.Type;

public final class ModelInstanceTextureInitializer {
   private boolean m_bRendered;
   private ModelInstance m_modelInstance;
   private InventoryItem m_item;
   private float m_bloodLevel;
   private int m_changeNumberMain = 0;
   private int m_changeNumberThread = 0;
   private final ModelInstanceTextureInitializer.RenderData[] m_renderData = new ModelInstanceTextureInitializer.RenderData[3];
   private static final ObjectPool pool = new ObjectPool(ModelInstanceTextureInitializer::new);

   public void init(ModelInstance var1, InventoryItem var2) {
      this.m_item = var2;
      this.m_modelInstance = var1;
      HandWeapon var3 = (HandWeapon)Type.tryCastTo(var2, HandWeapon.class);
      this.m_bloodLevel = var3 == null ? 0.0F : var3.getBloodLevel();
      this.setDirty();
   }

   public void init(ModelInstance var1, float var2) {
      this.m_item = null;
      this.m_modelInstance = var1;
      this.m_bloodLevel = var2;
      this.setDirty();
   }

   public void setDirty() {
      ++this.m_changeNumberMain;
      this.m_bRendered = false;
   }

   public boolean isDirty() {
      return !this.m_bRendered;
   }

   public void renderMain() {
      if (!this.m_bRendered) {
         int var1 = SpriteRenderer.instance.getMainStateIndex();
         if (this.m_renderData[var1] == null) {
            this.m_renderData[var1] = new ModelInstanceTextureInitializer.RenderData();
         }

         ModelInstanceTextureInitializer.RenderData var2 = this.m_renderData[var1];
         if (var2.m_textureCreator == null) {
            var2.m_changeNumber = this.m_changeNumberMain;
            var2.m_textureCreator = EquippedTextureCreator.alloc();
            if (this.m_item == null) {
               var2.m_textureCreator.init(this.m_modelInstance, this.m_bloodLevel);
            } else {
               var2.m_textureCreator.init(this.m_modelInstance, this.m_item);
            }

            var2.m_bRendered = false;
         }
      }
   }

   public void render() {
      int var1 = SpriteRenderer.instance.getRenderStateIndex();
      ModelInstanceTextureInitializer.RenderData var2 = this.m_renderData[var1];
      if (var2 != null) {
         if (var2.m_textureCreator != null) {
            if (!var2.m_bRendered) {
               if (var2.m_changeNumber == this.m_changeNumberThread) {
                  var2.m_bRendered = true;
               } else {
                  var2.m_textureCreator.render();
                  if (var2.m_textureCreator.isRendered()) {
                     this.m_changeNumberThread = var2.m_changeNumber;
                     var2.m_bRendered = true;
                  }

               }
            }
         }
      }
   }

   public void postRender() {
      int var1 = SpriteRenderer.instance.getMainStateIndex();
      ModelInstanceTextureInitializer.RenderData var2 = this.m_renderData[var1];
      if (var2 != null) {
         if (var2.m_textureCreator != null) {
            if (var2.m_textureCreator.isRendered() && var2.m_changeNumber == this.m_changeNumberMain) {
               this.m_bRendered = true;
            }

            if (var2.m_bRendered) {
               var2.m_textureCreator.postRender();
               var2.m_textureCreator = null;
            }

         }
      }
   }

   public boolean isRendered() {
      int var1 = SpriteRenderer.instance.getRenderStateIndex();
      ModelInstanceTextureInitializer.RenderData var2 = this.m_renderData[var1];
      if (var2 == null) {
         return true;
      } else {
         return var2.m_textureCreator == null ? true : var2.m_bRendered;
      }
   }

   public static ModelInstanceTextureInitializer alloc() {
      return (ModelInstanceTextureInitializer)pool.alloc();
   }

   public void release() {
      pool.release((Object)this);
   }

   private static final class RenderData {
      int m_changeNumber = 0;
      boolean m_bRendered;
      EquippedTextureCreator m_textureCreator;
   }
}
