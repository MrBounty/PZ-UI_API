package zombie.ui;

import java.util.ArrayList;
import zombie.core.Core;

public final class ServerPulseGraph extends UIElement {
   public static ServerPulseGraph instance;
   private final ArrayList times = new ArrayList();
   private final ArrayList bars = new ArrayList();
   private final int NUM_BARS = 30;
   private final int BAR_WID = 4;
   private final int BAR_PAD = 1;

   public ServerPulseGraph() {
      this.setVisible(false);
   }

   public void add(long var1) {
      if (var1 < 0L) {
         this.setVisible(false);
      } else {
         this.setVisible(true);
         this.times.add(var1);
         this.bars.clear();
         long var3 = (Long)this.times.get(0);
         int var5 = 1;

         int var6;
         for(var6 = 1; var6 < this.times.size(); ++var6) {
            if (var6 != this.times.size() - 1 && (Long)this.times.get(var6) - var3 <= 1000L) {
               ++var5;
            } else {
               long var7 = ((Long)this.times.get(var6) - var3) / 1000L - 1L;

               for(int var9 = 0; (long)var9 < var7; ++var9) {
                  this.bars.add(0);
               }

               this.bars.add(var5);
               var5 = 1;
               var3 = (Long)this.times.get(var6);
            }
         }

         while(this.bars.size() > 30) {
            var6 = (Integer)this.bars.get(0);

            for(int var10 = 0; var10 < var6; ++var10) {
               this.times.remove(0);
            }

            this.bars.remove(0);
         }

      }
   }

   public void update() {
      if (this.isVisible()) {
         this.setX(20.0D);
         this.setY((double)(Core.getInstance().getScreenHeight() - 20 - 36));
         this.setHeight(36.0D);
         this.setWidth(149.0D);
         super.update();
      }
   }

   public void render() {
      if (this.isVisible()) {
         if (UIManager.getClock() == null || UIManager.getClock().isVisible()) {
            this.DrawTextureScaledCol(UIElement.white, 0.0D, 0.0D, this.getWidth(), this.getHeight(), 0.0D, 0.0D, 0.0D, 0.5D);
            if (!this.bars.isEmpty()) {
               int var1 = 0;

               for(int var2 = 0; var2 < this.bars.size(); ++var2) {
                  float var3 = (float)this.getHeight().intValue() * ((float)Math.min(10, (Integer)this.bars.get(var2)) / 10.0F);
                  this.DrawTextureScaledCol(UIElement.white, (double)var1, this.getHeight() - (double)var3, 4.0D, (double)var3, 1.0D, 1.0D, 1.0D, 0.3499999940395355D);
                  var1 += 5;
               }

            }
         }
      }
   }
}
