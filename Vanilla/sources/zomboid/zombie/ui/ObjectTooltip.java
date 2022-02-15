package zombie.ui;

import java.util.ArrayList;
import java.util.Stack;
import zombie.characters.IsoGameCharacter;
import zombie.core.Core;
import zombie.core.textures.Texture;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoObject;

public final class ObjectTooltip extends UIElement {
   public static float alphaStep = 0.1F;
   public boolean bIsItem = false;
   public InventoryItem Item = null;
   public IsoObject Object;
   float alpha = 0.0F;
   int showDelay = 0;
   float targetAlpha = 0.0F;
   Texture texture = Texture.getSharedTexture("black");
   public int padRight = 5;
   public int padBottom = 5;
   private IsoGameCharacter character;
   private boolean measureOnly;
   private float weightOfStack = 0.0F;
   private static int lineSpacing = 14;
   private static String fontSize = "Small";
   private static UIFont font;
   private static Stack freeLayouts;

   public ObjectTooltip() {
      this.width = 130.0F;
      this.height = 130.0F;
      this.defaultDraw = false;
      lineSpacing = TextManager.instance.getFontFromEnum(font).getLineHeight();
      checkFont();
   }

   public static void checkFont() {
      if (!fontSize.equals(Core.getInstance().getOptionTooltipFont())) {
         fontSize = Core.getInstance().getOptionTooltipFont();
         if ("Large".equals(fontSize)) {
            font = UIFont.Large;
         } else if ("Medium".equals(fontSize)) {
            font = UIFont.Medium;
         } else {
            font = UIFont.Small;
         }

         lineSpacing = TextManager.instance.getFontFromEnum(font).getLineHeight();
      }

   }

   public UIFont getFont() {
      return font;
   }

   public int getLineSpacing() {
      return lineSpacing;
   }

   public void DrawText(UIFont var1, String var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (!this.measureOnly) {
         super.DrawText(var1, var2, var3, var5, var7, var9, var11, var13);
      }
   }

   public void DrawTextCentre(UIFont var1, String var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (!this.measureOnly) {
         super.DrawTextCentre(var1, var2, var3, var5, var7, var9, var11, var13);
      }
   }

   public void DrawTextRight(UIFont var1, String var2, double var3, double var5, double var7, double var9, double var11, double var13) {
      if (!this.measureOnly) {
         super.DrawTextRight(var1, var2, var3, var5, var7, var9, var11, var13);
      }
   }

   public void DrawValueRight(int var1, int var2, int var3, boolean var4) {
      Integer var5 = var1;
      String var6 = var5.toString();
      float var7 = 0.3F;
      float var8 = 1.0F;
      float var9 = 0.2F;
      float var10 = 1.0F;
      if (var1 > 0) {
         var6 = "+" + var6;
      }

      if (var1 < 0 && var4 || var1 > 0 && !var4) {
         var7 = 0.8F;
         var8 = 0.3F;
         var9 = 0.2F;
      }

      this.DrawTextRight(font, var6, (double)var2, (double)var3, (double)var7, (double)var8, (double)var9, (double)var10);
   }

   public void DrawValueRightNoPlus(int var1, int var2, int var3) {
      Integer var4 = var1;
      String var5 = var4.toString();
      float var6 = 1.0F;
      float var7 = 1.0F;
      float var8 = 1.0F;
      float var9 = 1.0F;
      this.DrawTextRight(font, var5, (double)var2, (double)var3, (double)var6, (double)var7, (double)var8, (double)var9);
   }

   public void DrawValueRightNoPlus(float var1, int var2, int var3) {
      Float var4 = var1;
      var4 = (float)((int)(((double)var4 + 0.01D) * 10.0D)) / 10.0F;
      String var5 = var4.toString();
      float var6 = 1.0F;
      float var7 = 1.0F;
      float var8 = 1.0F;
      float var9 = 1.0F;
      this.DrawTextRight(font, var5, (double)var2, (double)var3, (double)var6, (double)var7, (double)var8, (double)var9);
   }

   public void DrawTextureScaled(Texture var1, double var2, double var4, double var6, double var8, double var10) {
      if (!this.measureOnly) {
         super.DrawTextureScaled(var1, var2, var4, var6, var8, var10);
      }
   }

   public void DrawTextureScaledAspect(Texture var1, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
      if (!this.measureOnly) {
         super.DrawTextureScaledAspect(var1, var2, var4, var6, var8, var10, var12, var14, var16);
      }
   }

   public void DrawProgressBar(int var1, int var2, int var3, int var4, float var5, double var6, double var8, double var10, double var12) {
      if (!this.measureOnly) {
         if (var5 < 0.0F) {
            var5 = 0.0F;
         }

         if (var5 > 1.0F) {
            var5 = 1.0F;
         }

         int var14 = (int)Math.floor((double)((float)var3 * var5));
         if (var5 > 0.0F && var14 == 0) {
            var14 = 1;
         }

         this.DrawTextureScaledColor((Texture)null, (double)var1, (double)var2, (double)var14, 3.0D, var6, var8, var10, var12);
         this.DrawTextureScaledColor((Texture)null, (double)var1 + (double)var14, (double)var2, (double)var3 - (double)var14, 3.0D, 0.25D, 0.25D, 0.25D, 1.0D);
      }
   }

   public Boolean onMouseMove(double var1, double var3) {
      this.setX(this.getX() + var1);
      this.setY(this.getY() + var3);
      return Boolean.FALSE;
   }

   public void onMouseMoveOutside(double var1, double var3) {
      this.setX(this.getX() + var1);
      this.setY(this.getY() + var3);
   }

   public void render() {
      if (this.isVisible()) {
         if (!(this.alpha <= 0.0F)) {
            if (!this.bIsItem && this.Object != null && this.Object.haveSpecialTooltip()) {
               this.Object.DoSpecialTooltip(this, this.Object.square);
            }

            super.render();
         }
      }
   }

   public void show(IsoObject var1, double var2, double var4) {
      this.bIsItem = false;
      this.Object = var1;
      this.setX(var2);
      this.setY(var4);
      this.targetAlpha = 0.5F;
      this.showDelay = 15;
      this.alpha = 0.0F;
   }

   public void hide() {
      this.Object = null;
      this.showDelay = 0;
      this.setVisible(false);
   }

   public void update() {
      if (!(this.alpha <= 0.0F) || this.targetAlpha != 0.0F) {
         if (this.showDelay > 0) {
            if (--this.showDelay == 0) {
               this.setVisible(true);
            }

         } else {
            if (this.alpha < this.targetAlpha) {
               this.alpha += alphaStep;
               if (this.alpha > 0.5F) {
                  this.alpha = 0.5F;
               }
            } else if (this.alpha > this.targetAlpha) {
               this.alpha -= alphaStep;
               if (this.alpha < this.targetAlpha) {
                  this.alpha = this.targetAlpha;
               }
            }

         }
      }
   }

   void show(InventoryItem var1, int var2, int var3) {
      this.Object = null;
      this.Item = var1;
      this.bIsItem = true;
      this.setX(this.getX());
      this.setY(this.getY());
      this.targetAlpha = 0.5F;
      this.showDelay = 15;
      this.alpha = 0.0F;
      this.setVisible(true);
   }

   public void adjustWidth(int var1, String var2) {
      int var3 = TextManager.instance.MeasureStringX(font, var2);
      if ((float)(var1 + var3 + this.padRight) > this.width) {
         this.setWidth((double)(var1 + var3 + this.padRight));
      }

   }

   public ObjectTooltip.Layout beginLayout() {
      ObjectTooltip.Layout var1 = null;
      if (freeLayouts.isEmpty()) {
         var1 = new ObjectTooltip.Layout();
      } else {
         var1 = (ObjectTooltip.Layout)freeLayouts.pop();
      }

      return var1;
   }

   public void endLayout(ObjectTooltip.Layout var1) {
      while(var1 != null) {
         ObjectTooltip.Layout var2 = var1.next;
         var1.free();
         freeLayouts.push(var1);
         var1 = var2;
      }

   }

   public Texture getTexture() {
      return this.texture;
   }

   public void setCharacter(IsoGameCharacter var1) {
      this.character = var1;
   }

   public IsoGameCharacter getCharacter() {
      return this.character;
   }

   public void setMeasureOnly(boolean var1) {
      this.measureOnly = var1;
   }

   public boolean isMeasureOnly() {
      return this.measureOnly;
   }

   public float getWeightOfStack() {
      return this.weightOfStack;
   }

   public void setWeightOfStack(float var1) {
      this.weightOfStack = var1;
   }

   static {
      font = UIFont.Small;
      freeLayouts = new Stack();
   }

   public static class Layout {
      public ArrayList items = new ArrayList();
      public int minLabelWidth;
      public int minValueWidth;
      public ObjectTooltip.Layout next;
      public int nextPadY;
      private static Stack freeItems = new Stack();

      public ObjectTooltip.LayoutItem addItem() {
         ObjectTooltip.LayoutItem var1 = null;
         if (freeItems.isEmpty()) {
            var1 = new ObjectTooltip.LayoutItem();
         } else {
            var1 = (ObjectTooltip.LayoutItem)freeItems.pop();
         }

         var1.reset();
         this.items.add(var1);
         return var1;
      }

      public void setMinLabelWidth(int var1) {
         this.minLabelWidth = var1;
      }

      public void setMinValueWidth(int var1) {
         this.minValueWidth = var1;
      }

      public int render(int var1, int var2, ObjectTooltip var3) {
         int var4 = this.minLabelWidth;
         int var5 = this.minValueWidth;
         int var6 = this.minValueWidth;
         int var7 = 0;
         int var8 = 0;
         byte var9 = 8;
         int var10 = 0;

         int var11;
         ObjectTooltip.LayoutItem var12;
         for(var11 = 0; var11 < this.items.size(); ++var11) {
            var12 = (ObjectTooltip.LayoutItem)this.items.get(var11);
            var12.calcSizes();
            if (var12.hasValue) {
               var4 = Math.max(var4, var12.labelWidth);
               var5 = Math.max(var5, var12.valueWidth);
               var6 = Math.max(var6, var12.valueWidthRight);
               var7 = Math.max(var7, var12.progressWidth);
               var10 = Math.max(var10, Math.max(var12.labelWidth, this.minLabelWidth) + var9);
               var8 = Math.max(var8, var4 + var9 + Math.max(Math.max(var5, var6), var7));
            } else {
               var4 = Math.max(var4, var12.labelWidth);
               var8 = Math.max(var8, var12.labelWidth);
            }
         }

         if ((float)(var1 + var8 + var3.padRight) > var3.width) {
            var3.setWidth((double)(var1 + var8 + var3.padRight));
         }

         for(var11 = 0; var11 < this.items.size(); ++var11) {
            var12 = (ObjectTooltip.LayoutItem)this.items.get(var11);
            var12.render(var1, var2, var10, var6, var3);
            var2 += var12.height;
         }

         return this.next != null ? this.next.render(var1, var2 + this.next.nextPadY, var3) : var2;
      }

      public void free() {
         freeItems.addAll(this.items);
         this.items.clear();
         this.minLabelWidth = 0;
         this.minValueWidth = 0;
         this.next = null;
         this.nextPadY = 0;
      }
   }

   public static class LayoutItem {
      public String label;
      public float r0;
      public float g0;
      public float b0;
      public float a0;
      public boolean hasValue = false;
      public String value;
      public boolean rightJustify = false;
      public float r1;
      public float g1;
      public float b1;
      public float a1;
      public float progressFraction = -1.0F;
      public int labelWidth;
      public int valueWidth;
      public int valueWidthRight;
      public int progressWidth;
      public int height;

      public void reset() {
         this.label = null;
         this.value = null;
         this.hasValue = false;
         this.rightJustify = false;
         this.progressFraction = -1.0F;
      }

      public void setLabel(String var1, float var2, float var3, float var4, float var5) {
         this.label = var1;
         this.r0 = var2;
         this.b0 = var4;
         this.g0 = var3;
         this.a0 = var5;
      }

      public void setValue(String var1, float var2, float var3, float var4, float var5) {
         this.value = var1;
         this.r1 = var2;
         this.b1 = var4;
         this.g1 = var3;
         this.a1 = var5;
         this.hasValue = true;
      }

      public void setValueRight(int var1, boolean var2) {
         this.value = Integer.toString(var1);
         if (var1 > 0) {
            this.value = "+" + this.value;
         }

         if ((var1 >= 0 || !var2) && (var1 <= 0 || var2)) {
            this.r1 = 0.3F;
            this.g1 = 1.0F;
            this.b1 = 0.2F;
         } else {
            this.r1 = 0.8F;
            this.g1 = 0.3F;
            this.b1 = 0.2F;
         }

         this.a1 = 1.0F;
         this.hasValue = true;
         this.rightJustify = true;
      }

      public void setValueRightNoPlus(float var1) {
         var1 = (float)((int)((var1 + 0.005F) * 100.0F)) / 100.0F;
         this.value = Float.toString(var1);
         this.r1 = 1.0F;
         this.g1 = 1.0F;
         this.b1 = 1.0F;
         this.a1 = 1.0F;
         this.hasValue = true;
         this.rightJustify = true;
      }

      public void setValueRightNoPlus(int var1) {
         this.value = Integer.toString(var1);
         this.r1 = 1.0F;
         this.g1 = 1.0F;
         this.b1 = 1.0F;
         this.a1 = 1.0F;
         this.hasValue = true;
         this.rightJustify = true;
      }

      public void setProgress(float var1, float var2, float var3, float var4, float var5) {
         this.progressFraction = var1;
         this.r1 = var2;
         this.b1 = var4;
         this.g1 = var3;
         this.a1 = var5;
         this.hasValue = true;
      }

      public void calcSizes() {
         this.labelWidth = this.valueWidth = this.valueWidthRight = this.progressWidth = 0;
         if (this.label != null) {
            this.labelWidth = TextManager.instance.MeasureStringX(ObjectTooltip.font, this.label);
         }

         int var1;
         if (this.hasValue) {
            if (this.value != null) {
               var1 = TextManager.instance.MeasureStringX(ObjectTooltip.font, this.value);
               this.valueWidth = this.rightJustify ? 0 : var1;
               this.valueWidthRight = this.rightJustify ? var1 : 0;
            } else if (this.progressFraction != -1.0F) {
               this.progressWidth = 80;
            }
         }

         var1 = 1;
         int var2;
         int var3;
         if (this.label != null) {
            var2 = 1;

            for(var3 = 0; var3 < this.label.length(); ++var3) {
               if (this.label.charAt(var3) == '\n') {
                  ++var2;
               }
            }

            var1 = Math.max(var1, var2);
         }

         if (this.hasValue && this.value != null) {
            var2 = 1;

            for(var3 = 0; var3 < this.value.length(); ++var3) {
               if (this.value.charAt(var3) == '\n') {
                  ++var2;
               }
            }

            var1 = Math.max(var1, var2);
         }

         this.height = var1 * ObjectTooltip.lineSpacing;
      }

      public void render(int var1, int var2, int var3, int var4, ObjectTooltip var5) {
         if (this.label != null) {
            var5.DrawText(ObjectTooltip.font, this.label, (double)var1, (double)var2, (double)this.r0, (double)this.g0, (double)this.b0, (double)this.a0);
         }

         if (this.value != null) {
            if (this.rightJustify) {
               var5.DrawTextRight(ObjectTooltip.font, this.value, (double)(var1 + var3 + var4), (double)var2, (double)this.r1, (double)this.g1, (double)this.b1, (double)this.a1);
            } else {
               var5.DrawText(ObjectTooltip.font, this.value, (double)(var1 + var3), (double)var2, (double)this.r1, (double)this.g1, (double)this.b1, (double)this.a1);
            }
         }

         if (this.progressFraction != -1.0F) {
            var5.DrawProgressBar(var1 + var3, var2 + ObjectTooltip.lineSpacing / 2 - 1, this.progressWidth, 2, this.progressFraction, (double)this.r1, (double)this.g1, (double)this.b1, (double)this.a1);
         }

      }
   }
}
