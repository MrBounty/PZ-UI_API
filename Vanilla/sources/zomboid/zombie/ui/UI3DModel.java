package zombie.ui;

import se.krka.kahlua.vm.KahluaTable;
import zombie.characters.IsoGameCharacter;
import zombie.characters.SurvivorDesc;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.SpriteRenderer;
import zombie.core.skinnedmodel.advancedanimation.AnimatedModel;
import zombie.core.skinnedmodel.population.IClothingItemListener;
import zombie.core.skinnedmodel.population.OutfitManager;
import zombie.core.textures.TextureDraw;
import zombie.iso.IsoDirections;
import zombie.util.StringUtils;

public final class UI3DModel extends UIElement implements IClothingItemListener {
   private final AnimatedModel animatedModel = new AnimatedModel();
   private IsoDirections dir;
   private boolean bDoExt;
   private long nextExt;
   private final UI3DModel.Drawer[] drawers;
   private float zoom;
   private float yOffset;
   private float xOffset;

   public UI3DModel(KahluaTable var1) {
      super(var1);
      this.dir = IsoDirections.E;
      this.bDoExt = false;
      this.nextExt = -1L;
      this.drawers = new UI3DModel.Drawer[3];
      this.zoom = 0.0F;
      this.yOffset = 0.0F;
      this.xOffset = 0.0F;

      for(int var2 = 0; var2 < this.drawers.length; ++var2) {
         this.drawers[var2] = new UI3DModel.Drawer();
      }

      if (OutfitManager.instance != null) {
         OutfitManager.instance.addClothingItemListener(this);
      }

   }

   public void render() {
      if (this.isVisible()) {
         super.render();
         if (this.Parent == null || this.Parent.maxDrawHeight == -1 || !((double)this.Parent.maxDrawHeight <= this.y)) {
            if (this.bDoExt) {
               long var1 = System.currentTimeMillis();
               if (this.nextExt < 0L) {
                  this.nextExt = var1 + (long)Rand.Next(5000, 10000);
               }

               if (this.nextExt < var1) {
                  this.animatedModel.getActionContext().reportEvent("EventDoExt");
                  this.animatedModel.setVariable("Ext", (float)(Rand.Next(0, 6) + 1));
                  this.nextExt = -1L;
               }
            }

            this.animatedModel.update();
            UI3DModel.Drawer var3 = this.drawers[SpriteRenderer.instance.getMainStateIndex()];
            var3.init(this.getAbsoluteX().intValue(), this.getAbsoluteY().intValue());
            SpriteRenderer.instance.drawGeneric(var3);
         }
      }
   }

   public void setDirection(IsoDirections var1) {
      this.dir = var1;
      this.animatedModel.setAngle(var1.ToVector());
   }

   public IsoDirections getDirection() {
      return this.dir;
   }

   public void setAnimate(boolean var1) {
      this.animatedModel.setAnimate(var1);
   }

   public void setAnimSetName(String var1) {
      this.animatedModel.setAnimSetName(var1);
   }

   public void setDoRandomExtAnimations(boolean var1) {
      this.bDoExt = var1;
   }

   public void setIsometric(boolean var1) {
      this.animatedModel.setIsometric(var1);
   }

   public void setOutfitName(String var1, boolean var2, boolean var3) {
      this.animatedModel.setOutfitName(var1, var2, var3);
   }

   public void setCharacter(IsoGameCharacter var1) {
      this.animatedModel.setCharacter(var1);
   }

   public void setSurvivorDesc(SurvivorDesc var1) {
      this.animatedModel.setSurvivorDesc(var1);
   }

   public void setState(String var1) {
      this.animatedModel.setState(var1);
   }

   public void reportEvent(String var1) {
      if (!StringUtils.isNullOrWhitespace(var1)) {
         this.animatedModel.getActionContext().reportEvent(var1);
      }
   }

   public void clothingItemChanged(String var1) {
      this.animatedModel.clothingItemChanged(var1);
   }

   public void setZoom(float var1) {
      this.zoom = var1;
   }

   public void setYOffset(float var1) {
      this.yOffset = var1;
   }

   public void setXOffset(float var1) {
      this.xOffset = var1;
   }

   private final class Drawer extends TextureDraw.GenericDrawer {
      int absX;
      int absY;
      float m_animPlayerAngle;
      boolean bRendered;

      public void init(int var1, int var2) {
         this.absX = var1;
         this.absY = var2;
         this.m_animPlayerAngle = UI3DModel.this.animatedModel.getAnimationPlayer().getRenderedAngle();
         this.bRendered = false;
         float var3 = UI3DModel.this.animatedModel.isIsometric() ? -0.45F : -0.5F;
         if (UI3DModel.this.yOffset != 0.0F) {
            var3 = UI3DModel.this.yOffset;
         }

         UI3DModel.this.animatedModel.setOffset(UI3DModel.this.xOffset, var3, 0.0F);
         UI3DModel.this.animatedModel.renderMain();
      }

      public void render() {
         float var1 = UI3DModel.this.animatedModel.isIsometric() ? 22.0F : 25.0F;
         if (UI3DModel.this.zoom > 0.0F) {
            var1 -= UI3DModel.this.zoom;
         }

         UI3DModel.this.animatedModel.DoRender(this.absX, Core.height - this.absY - (int)UI3DModel.this.height, (int)UI3DModel.this.width, (int)UI3DModel.this.height, var1, this.m_animPlayerAngle);
         this.bRendered = true;
      }

      public void postRender() {
         UI3DModel.this.animatedModel.postRender(this.bRendered);
      }
   }
}
