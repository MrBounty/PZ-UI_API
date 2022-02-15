package zombie.ui;

import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;

public final class VehicleGauge extends UIElement {
   protected int needleX;
   protected int needleY;
   protected float minAngle;
   protected float maxAngle;
   protected float value;
   protected Texture texture;
   protected int needleWidth = 45;

   public VehicleGauge(Texture var1, int var2, int var3, float var4, float var5) {
      this.texture = var1;
      this.needleX = var2;
      this.needleY = var3;
      this.minAngle = var4;
      this.maxAngle = var5;
      this.width = (float)var1.getWidth();
      this.height = (float)var1.getHeight();
   }

   public void setNeedleWidth(int var1) {
      this.needleWidth = var1;
   }

   public void render() {
      if (this.isVisible()) {
         super.render();
         this.DrawTexture(this.texture, 0.0D, 0.0D, 1.0D);
         double var1 = this.minAngle < this.maxAngle ? Math.toRadians((double)(this.minAngle + (this.maxAngle - this.minAngle) * this.value)) : Math.toRadians((double)(this.maxAngle + (this.maxAngle - this.minAngle) * (1.0F - this.value)));
         double var3 = (double)this.needleX;
         double var5 = (double)this.needleY;
         double var7 = (double)this.needleX + (double)this.needleWidth * Math.cos(var1);
         double var9 = Math.ceil((double)this.needleY + (double)this.needleWidth * Math.sin(var1));
         int var11 = this.getAbsoluteX().intValue();
         int var12 = this.getAbsoluteY().intValue();
         SpriteRenderer.instance.renderline((Texture)null, var11 + (int)var3, var12 + (int)var5, var11 + (int)var7, var12 + (int)var9, 1.0F, 0.0F, 0.0F, 1.0F);
      }
   }

   public void setValue(float var1) {
      this.value = Math.min(var1, 1.0F);
   }

   public void setTexture(Texture var1) {
      this.texture = var1;
   }
}
