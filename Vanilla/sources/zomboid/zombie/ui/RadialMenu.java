package zombie.ui;

import java.util.ArrayList;
import zombie.core.SpriteRenderer;
import zombie.core.fonts.AngelCodeFont;
import zombie.core.textures.Texture;
import zombie.input.JoypadManager;
import zombie.input.Mouse;
import zombie.util.StringUtils;

public final class RadialMenu extends UIElement {
   protected int outerRadius = 200;
   protected int innerRadius = 100;
   protected ArrayList slices = new ArrayList();
   protected int highlight = -1;
   protected int joypad = -1;
   protected UITransition transition = new UITransition();
   protected UITransition select = new UITransition();
   protected UITransition deselect = new UITransition();
   protected int selectIndex = -1;
   protected int deselectIndex = -1;

   public RadialMenu(int var1, int var2, int var3, int var4) {
      this.setX((double)var1);
      this.setY((double)var2);
      this.setWidth((double)(var4 * 2));
      this.setHeight((double)(var4 * 2));
      this.innerRadius = var3;
      this.outerRadius = var4;
   }

   public void update() {
   }

   public void render() {
      if (this.isVisible()) {
         this.transition.setIgnoreUpdateTime(true);
         this.transition.setFadeIn(true);
         this.transition.update();
         if (!this.slices.isEmpty()) {
            float var1 = this.transition.fraction();
            float var2 = (float)this.innerRadius * 0.85F + (float)this.innerRadius * var1 * 0.15F;
            float var3 = (float)this.outerRadius * 0.85F + (float)this.outerRadius * var1 * 0.15F;

            float var5;
            double var12;
            double var14;
            double var16;
            double var18;
            double var20;
            double var22;
            double var24;
            for(int var4 = 0; var4 < 48; ++var4) {
               var5 = 7.5F;
               double var6 = Math.toRadians((double)((float)var4 * var5));
               double var8 = Math.toRadians((double)((float)(var4 + 1) * var5));
               double var10 = this.x + (double)(this.width / 2.0F);
               var12 = this.y + (double)(this.height / 2.0F);
               var14 = this.x + (double)(this.width / 2.0F);
               var16 = this.y + (double)(this.height / 2.0F);
               var18 = this.x + (double)(this.width / 2.0F) + (double)(var3 * (float)Math.cos(var6));
               var20 = this.y + (double)(this.height / 2.0F) + (double)(var3 * (float)Math.sin(var6));
               var22 = this.x + (double)(this.width / 2.0F) + (double)(var3 * (float)Math.cos(var8));
               var24 = this.y + (double)(this.height / 2.0F) + (double)(var3 * (float)Math.sin(var8));
               if (var4 == 47) {
                  var24 = var16;
               }

               float var26 = 0.1F;
               float var27 = 0.1F;
               float var28 = 0.1F;
               float var29 = 0.45F + 0.45F * var1;
               SpriteRenderer.instance.renderPoly((float)var10, (float)var12, (float)var18, (float)var20, (float)var22, (float)var24, (float)var14, (float)var16, var26, var27, var28, var29);
            }

            float var40 = 360.0F / (float)Math.max(this.slices.size(), 2);
            var5 = this.slices.size() == 1 ? 0.0F : 1.5F;
            int var41 = this.highlight;
            if (var41 == -1) {
               if (this.joypad != -1) {
                  var41 = this.getSliceIndexFromJoypad(this.joypad);
               } else {
                  var41 = this.getSliceIndexFromMouse(Mouse.getXA() - this.getAbsoluteX().intValue(), Mouse.getYA() - this.getAbsoluteY().intValue());
               }
            }

            RadialMenu.Slice var7 = this.getSlice(var41);
            if (var7 != null && var7.isEmpty()) {
               var41 = -1;
            }

            if (var41 != this.selectIndex) {
               this.select.reset();
               this.select.setIgnoreUpdateTime(true);
               if (this.selectIndex != -1) {
                  this.deselectIndex = this.selectIndex;
                  this.deselect.reset();
                  this.deselect.setFadeIn(false);
                  this.deselect.init(66.666664F, true);
               }

               this.selectIndex = var41;
            }

            this.select.update();
            this.deselect.update();
            float var42 = this.getStartAngle() - 180.0F;

            for(int var9 = 0; var9 < this.slices.size(); ++var9) {
               int var43 = Math.max(6, 48 / Math.max(this.slices.size(), 2));

               for(int var11 = 0; var11 < var43; ++var11) {
                  var12 = Math.toRadians((double)(var42 + (float)var9 * var40 + (float)var11 * var40 / (float)var43 + (var11 == 0 ? var5 : 0.0F)));
                  var14 = Math.toRadians((double)(var42 + (float)var9 * var40 + (float)(var11 + 1) * var40 / (float)var43 - (var11 == var43 - 1 ? var5 : 0.0F)));
                  var16 = Math.toRadians((double)(var42 + (float)var9 * var40 + (float)var11 * var40 / (float)var43 + (var11 == 0 ? var5 / 2.0F : 0.0F)));
                  var18 = Math.toRadians((double)(var42 + (float)var9 * var40 + (float)(var11 + 1) * var40 / (float)var43) - (var11 == var43 - 1 ? (double)var5 / 1.5D : 0.0D));
                  var20 = this.x + (double)(this.width / 2.0F) + (double)(var2 * (float)Math.cos(var12));
                  var22 = this.y + (double)(this.height / 2.0F) + (double)(var2 * (float)Math.sin(var12));
                  var24 = this.x + (double)(this.width / 2.0F) + (double)(var2 * (float)Math.cos(var14));
                  double var46 = this.y + (double)(this.height / 2.0F) + (double)(var2 * (float)Math.sin(var14));
                  double var47 = this.x + (double)(this.width / 2.0F) + (double)(var3 * (float)Math.cos(var16));
                  double var30 = this.y + (double)(this.height / 2.0F) + (double)(var3 * (float)Math.sin(var16));
                  double var32 = this.x + (double)(this.width / 2.0F) + (double)(var3 * (float)Math.cos(var18));
                  double var34 = this.y + (double)(this.height / 2.0F) + (double)(var3 * (float)Math.sin(var18));
                  float var36 = 1.0F;
                  float var37 = 1.0F;
                  float var38 = 1.0F;
                  float var39 = 0.025F;
                  if (var9 == var41) {
                     var39 = 0.25F + 0.25F * this.select.fraction();
                  } else if (var9 == this.deselectIndex) {
                     var39 = 0.025F + 0.475F * this.deselect.fraction();
                  }

                  SpriteRenderer.instance.renderPoly((float)var20, (float)var22, (float)var47, (float)var30, (float)var32, (float)var34, (float)var24, (float)var46, var36, var37, var38, var39);
               }

               Texture var44 = ((RadialMenu.Slice)this.slices.get(var9)).texture;
               if (var44 != null) {
                  var12 = Math.toRadians((double)(var42 + (float)var9 * var40 + var40 / 2.0F));
                  float var45 = 0.0F + this.width / 2.0F + (var2 + (var3 - var2) / 2.0F) * (float)Math.cos(var12);
                  float var15 = 0.0F + this.height / 2.0F + (var2 + (var3 - var2) / 2.0F) * (float)Math.sin(var12);
                  this.DrawTexture(var44, (double)(var45 - (float)(var44.getWidth() / 2) - var44.offsetX), (double)(var15 - (float)(var44.getHeight() / 2) - var44.offsetY), (double)var1);
               }
            }

            if (var7 != null && !StringUtils.isNullOrWhitespace(var7.text)) {
               this.formatTextInsideCircle(var7.text);
            }

         }
      }
   }

   private void formatTextInsideCircle(String var1) {
      UIFont var2 = UIFont.Medium;
      AngelCodeFont var3 = TextManager.instance.getFontFromEnum(var2);
      int var4 = var3.getLineHeight();
      int var5 = 1;

      int var6;
      for(var6 = 0; var6 < var1.length(); ++var6) {
         if (var1.charAt(var6) == '\n') {
            ++var5;
         }
      }

      if (var5 > 1) {
         var6 = var5 * var4;
         int var7 = this.getAbsoluteX().intValue() + (int)this.width / 2;
         int var8 = this.getAbsoluteY().intValue() + (int)this.height / 2 - var6 / 2;
         int var9 = 0;

         int var10;
         for(var10 = 0; var10 < var1.length(); ++var10) {
            if (var1.charAt(var10) == '\n') {
               int var11 = var3.getWidth(var1, var9, var10);
               var3.drawString((float)(var7 - var11 / 2), (float)var8, var1, 1.0F, 1.0F, 1.0F, 1.0F, var9, var10 - 1);
               var9 = var10 + 1;
               var8 += var4;
            }
         }

         if (var9 < var1.length()) {
            var10 = var3.getWidth(var1, var9, var1.length() - 1);
            var3.drawString((float)(var7 - var10 / 2), (float)var8, var1, 1.0F, 1.0F, 1.0F, 1.0F, var9, var1.length() - 1);
         }
      } else {
         this.DrawTextCentre(var2, var1, (double)(this.width / 2.0F), (double)(this.height / 2.0F - (float)(var4 / 2)), 1.0D, 1.0D, 1.0D, 1.0D);
      }

   }

   public void clear() {
      this.slices.clear();
      this.transition.reset();
      this.transition.init(66.666664F, false);
      this.selectIndex = -1;
      this.deselectIndex = -1;
   }

   public void addSlice(String var1, Texture var2) {
      RadialMenu.Slice var3 = new RadialMenu.Slice();
      var3.text = var1;
      var3.texture = var2;
      this.slices.add(var3);
   }

   private RadialMenu.Slice getSlice(int var1) {
      return var1 >= 0 && var1 < this.slices.size() ? (RadialMenu.Slice)this.slices.get(var1) : null;
   }

   public void setSliceText(int var1, String var2) {
      RadialMenu.Slice var3 = this.getSlice(var1);
      if (var3 != null) {
         var3.text = var2;
      }

   }

   public void setSliceTexture(int var1, Texture var2) {
      RadialMenu.Slice var3 = this.getSlice(var1);
      if (var3 != null) {
         var3.texture = var2;
      }

   }

   private float getStartAngle() {
      float var1 = 360.0F / (float)Math.max(this.slices.size(), 2);
      return 90.0F - var1 / 2.0F;
   }

   public int getSliceIndexFromMouse(int var1, int var2) {
      float var3 = 0.0F + this.width / 2.0F;
      float var4 = 0.0F + this.height / 2.0F;
      double var5 = Math.sqrt(Math.pow((double)((float)var1 - var3), 2.0D) + Math.pow((double)((float)var2 - var4), 2.0D));
      if (!(var5 > (double)this.outerRadius) && !(var5 < (double)this.innerRadius)) {
         double var7 = Math.atan2((double)((float)var2 - var4), (double)((float)var1 - var3)) + 3.141592653589793D;
         double var9 = Math.toDegrees(var7);
         float var11 = 360.0F / (float)Math.max(this.slices.size(), 2);
         return var9 < (double)this.getStartAngle() ? (int)((var9 + 360.0D - (double)this.getStartAngle()) / (double)var11) : (int)((var9 - (double)this.getStartAngle()) / (double)var11);
      } else {
         return -1;
      }
   }

   public int getSliceIndexFromJoypad(int var1) {
      float var2 = JoypadManager.instance.getAimingAxisX(var1);
      float var3 = JoypadManager.instance.getAimingAxisY(var1);
      if (!(Math.abs(var2) > 0.3F) && !(Math.abs(var3) > 0.3F)) {
         return -1;
      } else {
         double var4 = Math.atan2((double)(-var3), (double)(-var2));
         double var6 = Math.toDegrees(var4);
         float var8 = 360.0F / (float)Math.max(this.slices.size(), 2);
         return var6 < (double)this.getStartAngle() ? (int)((var6 + 360.0D - (double)this.getStartAngle()) / (double)var8) : (int)((var6 - (double)this.getStartAngle()) / (double)var8);
      }
   }

   public void setJoypad(int var1) {
      this.joypad = var1;
   }

   protected static class Slice {
      public String text;
      public Texture texture;

      boolean isEmpty() {
         return this.text == null && this.texture == null;
      }
   }
}
