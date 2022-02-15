package zombie.ui;

import zombie.core.Color;
import zombie.core.textures.Texture;

public final class ActionProgressBar extends UIElement {
   Texture background = Texture.getSharedTexture("BuildBar_Bkg");
   Texture foreground = Texture.getSharedTexture("BuildBar_Bar");
   float deltaValue = 1.0F;
   public int delayHide = 0;

   public ActionProgressBar(int var1, int var2) {
      this.x = (double)var1;
      this.y = (double)var2;
      this.width = (float)this.background.getWidth();
      this.height = (float)this.background.getHeight();
      this.followGameWorld = true;
   }

   public void render() {
      if (this.isVisible() && UIManager.VisibleAllUI) {
         this.DrawUVSliceTexture(this.background, 0.0D, 0.0D, (double)this.background.getWidth(), (double)this.background.getHeight(), Color.white, 0.0D, 0.0D, 1.0D, 1.0D);
         this.DrawUVSliceTexture(this.foreground, 3.0D, 0.0D, (double)this.foreground.getWidth(), (double)this.foreground.getHeight(), Color.white, 0.0D, 0.0D, (double)this.deltaValue, 1.0D);
      }
   }

   public void setValue(float var1) {
      this.deltaValue = var1;
   }

   public float getValue() {
      return this.deltaValue;
   }
}
