package zombie.ui;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Stack;
import zombie.core.Color;
import zombie.core.textures.Texture;

public final class UINineGrid extends UIElement {
   Texture GridTopLeft = null;
   Texture GridTop = null;
   Texture GridTopRight = null;
   Texture GridLeft = null;
   Texture GridCenter = null;
   Texture GridRight = null;
   Texture GridBottomLeft = null;
   Texture GridBottom = null;
   Texture GridBottomRight = null;
   int TopWidth = 10;
   int LeftWidth = 10;
   int RightWidth = 10;
   int BottomWidth = 10;
   public int clientH = 0;
   public int clientW = 0;
   public Stack nestedItems = new Stack();
   public Color Colour = new Color(50, 50, 50, 212);

   public UINineGrid(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, String var9, String var10, String var11, String var12, String var13, String var14, String var15, String var16, String var17) {
      this.x = (double)var1;
      this.y = (double)var2;
      this.width = (float)var3;
      this.height = (float)var4;
      this.TopWidth = var5;
      this.LeftWidth = var6;
      this.RightWidth = var7;
      this.BottomWidth = var8;
      this.GridTopLeft = Texture.getSharedTexture(var9);
      this.GridTop = Texture.getSharedTexture(var10);
      this.GridTopRight = Texture.getSharedTexture(var11);
      this.GridLeft = Texture.getSharedTexture(var12);
      this.GridCenter = Texture.getSharedTexture(var13);
      this.GridRight = Texture.getSharedTexture(var14);
      this.GridBottomLeft = Texture.getSharedTexture(var15);
      this.GridBottom = Texture.getSharedTexture(var16);
      this.GridBottomRight = Texture.getSharedTexture(var17);
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
      this.DrawTextureScaledCol(this.GridTopLeft, 0.0D, 0.0D, (double)this.LeftWidth, (double)this.TopWidth, this.Colour);
      this.DrawTextureScaledCol(this.GridTop, (double)this.LeftWidth, 0.0D, this.getWidth() - (double)(this.LeftWidth + this.RightWidth), (double)this.TopWidth, this.Colour);
      this.DrawTextureScaledCol(this.GridTopRight, this.getWidth() - (double)this.RightWidth, 0.0D, (double)this.RightWidth, (double)this.TopWidth, this.Colour);
      this.DrawTextureScaledCol(this.GridLeft, 0.0D, (double)this.TopWidth, (double)this.LeftWidth, this.getHeight() - (double)(this.TopWidth + this.BottomWidth), this.Colour);
      this.DrawTextureScaledCol(this.GridCenter, (double)this.LeftWidth, (double)this.TopWidth, this.getWidth() - (double)(this.LeftWidth + this.RightWidth), this.getHeight() - (double)(this.TopWidth + this.BottomWidth), this.Colour);
      this.DrawTextureScaledCol(this.GridRight, this.getWidth() - (double)this.RightWidth, (double)this.TopWidth, (double)this.RightWidth, this.getHeight() - (double)(this.TopWidth + this.BottomWidth), this.Colour);
      this.DrawTextureScaledCol(this.GridBottomLeft, 0.0D, this.getHeight() - (double)this.BottomWidth, (double)this.LeftWidth, (double)this.BottomWidth, this.Colour);
      this.DrawTextureScaledCol(this.GridBottom, (double)this.LeftWidth, this.getHeight() - (double)this.BottomWidth, this.getWidth() - (double)(this.LeftWidth + this.RightWidth), (double)this.BottomWidth, this.Colour);
      this.DrawTextureScaledCol(this.GridBottomRight, this.getWidth() - (double)this.RightWidth, this.getHeight() - (double)this.BottomWidth, (double)this.RightWidth, (double)this.BottomWidth, this.Colour);
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

   public void setAlpha(float var1) {
      this.Colour.a = var1;
   }

   public float getAlpha() {
      return this.Colour.a;
   }
}
