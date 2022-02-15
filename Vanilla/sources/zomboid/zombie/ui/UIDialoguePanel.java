package zombie.ui;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Stack;
import zombie.core.Color;
import zombie.core.textures.Texture;

public final class UIDialoguePanel extends UIElement {
   float alpha = 1.0F;
   Texture dialogBottomLeft = null;
   Texture dialogBottomMiddle = null;
   Texture dialogBottomRight = null;
   Texture dialogLeft = null;
   Texture dialogMiddle = null;
   Texture dialogRight = null;
   Texture titleLeft = null;
   Texture titleMiddle = null;
   Texture titleRight = null;
   public float clientH = 0.0F;
   public float clientW = 0.0F;
   public Stack nestedItems = new Stack();

   public UIDialoguePanel(float var1, float var2, float var3, float var4) {
      this.x = (double)var1;
      this.y = (double)var2;
      this.width = var3;
      this.height = var4;
      this.titleLeft = Texture.getSharedTexture("media/ui/Dialog_Titlebar_Left.png");
      this.titleMiddle = Texture.getSharedTexture("media/ui/Dialog_Titlebar_Middle.png");
      this.titleRight = Texture.getSharedTexture("media/ui/Dialog_Titlebar_Right.png");
      this.dialogLeft = Texture.getSharedTexture("media/ui/Dialog_Left.png");
      this.dialogMiddle = Texture.getSharedTexture("media/ui/Dialog_Middle.png");
      this.dialogRight = Texture.getSharedTexture("media/ui/Dialog_Right.png");
      this.dialogBottomLeft = Texture.getSharedTexture("media/ui/Dialog_Bottom_Left.png");
      this.dialogBottomMiddle = Texture.getSharedTexture("media/ui/Dialog_Bottom_Middle.png");
      this.dialogBottomRight = Texture.getSharedTexture("media/ui/Dialog_Bottom_Right.png");
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

   public void render() {
      this.DrawTextureScaledCol(this.titleLeft, 0.0D, 0.0D, 28.0D, 28.0D, new Color(255, 255, 255, 100));
      this.DrawTextureScaledCol(this.titleMiddle, 28.0D, 0.0D, this.getWidth() - 56.0D, 28.0D, new Color(255, 255, 255, 100));
      this.DrawTextureScaledCol(this.titleRight, 0.0D + this.getWidth() - 28.0D, 0.0D, 28.0D, 28.0D, new Color(255, 255, 255, 100));
      this.DrawTextureScaledCol(this.dialogLeft, 0.0D, 28.0D, 78.0D, this.getHeight() - 100.0D, new Color(255, 255, 255, 100));
      this.DrawTextureScaledCol(this.dialogMiddle, 78.0D, 28.0D, this.getWidth() - 156.0D, this.getHeight() - 100.0D, new Color(255, 255, 255, 100));
      this.DrawTextureScaledCol(this.dialogRight, 0.0D + this.getWidth() - 78.0D, 28.0D, 78.0D, this.getHeight() - 100.0D, new Color(255, 255, 255, 100));
      this.DrawTextureScaledCol(this.dialogBottomLeft, 0.0D, 0.0D + this.getHeight() - 72.0D, 78.0D, 72.0D, new Color(255, 255, 255, 100));
      this.DrawTextureScaledCol(this.dialogBottomMiddle, 78.0D, 0.0D + this.getHeight() - 72.0D, this.getWidth() - 156.0D, 72.0D, new Color(255, 255, 255, 100));
      this.DrawTextureScaledCol(this.dialogBottomRight, 0.0D + this.getWidth() - 78.0D, 0.0D + this.getHeight() - 72.0D, 78.0D, 72.0D, new Color(255, 255, 255, 100));
      super.render();
   }

   public void update() {
      super.update();
      int var1 = 0;

      for(Iterator var2 = this.nestedItems.iterator(); var2.hasNext(); ++var1) {
         Rectangle var3 = (Rectangle)var2.next();
         UIElement var4 = (UIElement)this.getControls().get(var1);
         var4.setX((double)((float)var3.getX()));
         var4.setY((double)((float)var3.getY()));
         var4.setWidth((double)((int)((double)this.clientW - (var3.getX() + var3.getWidth()))));
         var4.setHeight((double)((int)((double)this.clientH - (var3.getY() + var3.getHeight()))));
         var4.onresize();
      }

   }
}
