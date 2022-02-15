package zombie.ui;

import java.util.Iterator;
import java.util.Stack;
import org.lwjgl.util.Rectangle;
import zombie.core.textures.Texture;

public class NewWindow extends UIElement {
   public int clickX = 0;
   public int clickY = 0;
   public int clientH = 0;
   public int clientW = 0;
   public boolean Movable = true;
   public boolean moving = false;
   public int ncclientH = 0;
   public int ncclientW = 0;
   public Stack nestedItems = new Stack();
   public boolean ResizeToFitY = true;
   float alpha = 1.0F;
   Texture dialogBottomLeft = null;
   Texture dialogBottomMiddle = null;
   Texture dialogBottomRight = null;
   Texture dialogLeft = null;
   Texture dialogMiddle = null;
   Texture dialogRight = null;
   Texture titleCloseIcon = null;
   Texture titleLeft = null;
   Texture titleMiddle = null;
   Texture titleRight = null;
   HUDButton closeButton = null;

   public NewWindow(int var1, int var2, int var3, int var4, boolean var5) {
      this.x = (double)var1;
      this.y = (double)var2;
      if (var3 < 156) {
         var3 = 156;
      }

      if (var4 < 78) {
         var4 = 78;
      }

      this.width = (float)var3;
      this.height = (float)var4;
      this.titleLeft = Texture.getSharedTexture("media/ui/Dialog_Titlebar_Left.png");
      this.titleMiddle = Texture.getSharedTexture("media/ui/Dialog_Titlebar_Middle.png");
      this.titleRight = Texture.getSharedTexture("media/ui/Dialog_Titlebar_Right.png");
      this.dialogLeft = Texture.getSharedTexture("media/ui/Dialog_Left.png");
      this.dialogMiddle = Texture.getSharedTexture("media/ui/Dialog_Middle.png");
      this.dialogRight = Texture.getSharedTexture("media/ui/Dialog_Right.png");
      this.dialogBottomLeft = Texture.getSharedTexture("media/ui/Dialog_Bottom_Left.png");
      this.dialogBottomMiddle = Texture.getSharedTexture("media/ui/Dialog_Bottom_Middle.png");
      this.dialogBottomRight = Texture.getSharedTexture("media/ui/Dialog_Bottom_Right.png");
      if (var5) {
         this.closeButton = new HUDButton("close", (float)(var3 - 16), 2.0F, "media/ui/Dialog_Titlebar_CloseIcon.png", "media/ui/Dialog_Titlebar_CloseIcon.png", "media/ui/Dialog_Titlebar_CloseIcon.png", this);
         this.AddChild(this.closeButton);
      }

      this.clientW = var3;
      this.clientH = var4;
   }

   public void Nest(UIElement var1, int var2, int var3, int var4, int var5) {
      this.AddChild(var1);
      this.nestedItems.add(new Rectangle(var5, var2, var3, var4));
      var1.setX((double)var5);
      var1.setY((double)var2);
      var1.update();
   }

   public void ButtonClicked(String var1) {
      super.ButtonClicked(var1);
      if (var1.equals("close")) {
         this.setVisible(false);
      }

   }

   public Boolean onMouseDown(double var1, double var3) {
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else {
         super.onMouseDown(var1, var3);
         if (var3 < 18.0D) {
            this.clickX = (int)var1;
            this.clickY = (int)var3;
            if (this.Movable) {
               this.moving = true;
            }

            this.setCapture(true);
         }

         return Boolean.TRUE;
      }
   }

   public void setMovable(boolean var1) {
      this.Movable = var1;
   }

   public Boolean onMouseMove(double var1, double var3) {
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else {
         super.onMouseMove(var1, var3);
         if (this.moving) {
            this.setX(this.getX() + var1);
            this.setY(this.getY() + var3);
         }

         return Boolean.FALSE;
      }
   }

   public void onMouseMoveOutside(double var1, double var3) {
      if (this.isVisible()) {
         super.onMouseMoveOutside(var1, var3);
         if (this.moving) {
            this.setX(this.getX() + var1);
            this.setY(this.getY() + var3);
         }

      }
   }

   public Boolean onMouseUp(double var1, double var3) {
      if (!this.isVisible()) {
         return Boolean.FALSE;
      } else {
         super.onMouseUp(var1, var3);
         this.moving = false;
         this.setCapture(false);
         return Boolean.TRUE;
      }
   }

   public void render() {
      float var1 = 0.8F * this.alpha;
      byte var2 = 0;
      byte var3 = 0;
      this.DrawTexture(this.titleLeft, (double)var2, (double)var3, (double)var1);
      this.DrawTexture(this.titleRight, this.getWidth() - (double)this.titleRight.getWidth(), (double)var3, (double)var1);
      this.DrawTextureScaled(this.titleMiddle, (double)this.titleLeft.getWidth(), (double)var3, this.getWidth() - (double)(this.titleLeft.getWidth() * 2), (double)this.titleMiddle.getHeight(), (double)var1);
      int var4 = var3 + this.titleRight.getHeight();
      this.DrawTextureScaled(this.dialogLeft, (double)var2, (double)var4, (double)this.dialogLeft.getWidth(), this.getHeight() - (double)this.titleLeft.getHeight() - (double)this.dialogBottomLeft.getHeight(), (double)var1);
      this.DrawTextureScaled(this.dialogMiddle, (double)this.dialogLeft.getWidth(), (double)var4, this.getWidth() - (double)(this.dialogRight.getWidth() * 2), this.getHeight() - (double)this.titleLeft.getHeight() - (double)this.dialogBottomLeft.getHeight(), (double)var1);
      this.DrawTextureScaled(this.dialogRight, this.getWidth() - (double)this.dialogRight.getWidth(), (double)var4, (double)this.dialogLeft.getWidth(), this.getHeight() - (double)this.titleLeft.getHeight() - (double)this.dialogBottomLeft.getHeight(), (double)var1);
      var4 = (int)((double)var4 + (this.getHeight() - (double)this.titleLeft.getHeight() - (double)this.dialogBottomLeft.getHeight()));
      this.DrawTextureScaled(this.dialogBottomMiddle, (double)this.dialogBottomLeft.getWidth(), (double)var4, this.getWidth() - (double)(this.dialogBottomLeft.getWidth() * 2), (double)this.dialogBottomMiddle.getHeight(), (double)var1);
      this.DrawTexture(this.dialogBottomLeft, (double)var2, (double)var4, (double)var1);
      this.DrawTexture(this.dialogBottomRight, this.getWidth() - (double)this.dialogBottomRight.getWidth(), (double)var4, (double)var1);
      super.render();
   }

   public void update() {
      super.update();
      if (this.closeButton != null) {
         this.closeButton.setX(4.0D);
         this.closeButton.setY(3.0D);
      }

      int var1 = 0;
      if (!this.ResizeToFitY) {
         Iterator var2 = this.nestedItems.iterator();

         while(var2.hasNext()) {
            Rectangle var3 = (Rectangle)var2.next();
            UIElement var4 = (UIElement)this.getControls().get(var1);
            if (var4 != this.closeButton) {
               var4.setX((double)var3.getX());
               var4.setY((double)var3.getY());
               var4.setWidth((double)(this.clientW - (var3.getX() + var3.getWidth())));
               var4.setHeight((double)(this.clientH - (var3.getY() + var3.getHeight())));
               var4.onresize();
               ++var1;
            }
         }
      } else {
         int var9 = 100000;
         int var10 = 100000;
         float var11 = 0.0F;
         float var5 = 0.0F;
         Iterator var6 = this.nestedItems.iterator();

         while(var6.hasNext()) {
            Rectangle var7 = (Rectangle)var6.next();
            UIElement var8 = (UIElement)this.getControls().get(var1);
            if (var8 != this.closeButton) {
               if ((double)var9 > var8.getAbsoluteX()) {
                  var9 = var8.getAbsoluteX().intValue();
               }

               if ((double)var10 > var8.getAbsoluteX()) {
                  var10 = var8.getAbsoluteX().intValue();
               }

               if ((double)var11 < var8.getWidth()) {
                  var11 = (float)var8.getWidth().intValue();
               }

               if ((double)var5 < var8.getHeight()) {
                  var5 = (float)var8.getHeight().intValue();
               }

               ++var1;
            }
         }

         var5 += 50.0F;
         this.height = var5;
      }

   }
}
