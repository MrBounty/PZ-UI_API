package zombie.ui;

import java.util.ArrayList;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.input.Mouse;

public final class FPSGraph extends UIElement {
   public static FPSGraph instance;
   private static final int NUM_BARS = 30;
   private static final int BAR_WID = 8;
   private final FPSGraph.Graph fpsGraph = new FPSGraph.Graph();
   private final FPSGraph.Graph upsGraph = new FPSGraph.Graph();
   private final FPSGraph.Graph lpsGraph = new FPSGraph.Graph();
   private final FPSGraph.Graph uiGraph = new FPSGraph.Graph();

   public FPSGraph() {
      this.setVisible(false);
   }

   public void addRender(long var1) {
      synchronized(this.fpsGraph) {
         this.fpsGraph.add(var1);
      }
   }

   public void addUpdate(long var1) {
      this.upsGraph.add(var1);
   }

   public void addLighting(long var1) {
      synchronized(this.lpsGraph) {
         this.lpsGraph.add(var1);
      }
   }

   public void addUI(long var1) {
      this.uiGraph.add(var1);
   }

   public void update() {
      if (this.isVisible()) {
         this.setHeight(108.0D);
         this.setWidth(232.0D);
         this.setX(20.0D);
         this.setY((double)(Core.getInstance().getScreenHeight() - 20) - this.getHeight());
         super.update();
      }
   }

   public void render() {
      if (this.isVisible()) {
         if (UIManager.VisibleAllUI) {
            int var1 = this.getHeight().intValue() - 4;
            int var2 = -1;
            if (this.isMouseOver()) {
               this.DrawTextureScaledCol(UIElement.white, 0.0D, 0.0D, this.getWidth(), this.getHeight(), 0.0D, 0.20000000298023224D, 0.0D, 0.5D);
               int var3 = Mouse.getXA() - this.getAbsoluteX().intValue();
               var2 = var3 / 8;
            }

            synchronized(this.fpsGraph) {
               this.fpsGraph.render(0.0F, 1.0F, 0.0F);
               if (var2 >= 0 && var2 < this.fpsGraph.bars.size()) {
                  this.DrawText("FPS: " + this.fpsGraph.bars.get(var2), 20.0D, (double)(var1 / 2 - 10), 0.0D, 1.0D, 0.0D, 1.0D);
               }
            }

            synchronized(this.lpsGraph) {
               this.lpsGraph.render(1.0F, 1.0F, 0.0F);
               if (var2 >= 0 && var2 < this.lpsGraph.bars.size()) {
                  this.DrawText("LPS: " + this.lpsGraph.bars.get(var2), 20.0D, (double)(var1 / 2 + 20), 1.0D, 1.0D, 0.0D, 1.0D);
               }
            }

            this.upsGraph.render(0.0F, 1.0F, 1.0F);
            if (var2 >= 0 && var2 < this.upsGraph.bars.size()) {
               this.DrawText("UPS: " + this.upsGraph.bars.get(var2), 20.0D, (double)(var1 / 2 + 5), 0.0D, 1.0D, 1.0D, 1.0D);
               this.DrawTextureScaledCol(UIElement.white, (double)(var2 * 8 + 4), 0.0D, 1.0D, this.getHeight(), 1.0D, 1.0D, 1.0D, 0.5D);
            }

            this.uiGraph.render(1.0F, 0.0F, 1.0F);
            if (var2 >= 0 && var2 < this.uiGraph.bars.size()) {
               this.DrawText("UI: " + this.uiGraph.bars.get(var2), 20.0D, (double)(var1 / 2 - 26), 1.0D, 0.0D, 1.0D, 1.0D);
            }

         }
      }
   }

   private final class Graph {
      private final ArrayList times = new ArrayList();
      private final ArrayList bars = new ArrayList();

      public void add(long var1) {
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

      public void render(float var1, float var2, float var3) {
         if (!this.bars.isEmpty()) {
            float var4 = (float)(FPSGraph.this.getHeight().intValue() - 4);
            float var5 = (float)(FPSGraph.this.getHeight().intValue() - 2);
            int var6 = Math.max(PerformanceSettings.getLockFPS(), PerformanceSettings.LightingFPS);
            int var7 = 8;
            float var8 = var4 * ((float)Math.min(var6, (Integer)this.bars.get(0)) / (float)var6);

            for(int var9 = 1; var9 < this.bars.size() - 1; ++var9) {
               float var10 = var4 * ((float)Math.min(var6, (Integer)this.bars.get(var9)) / (float)var6);
               SpriteRenderer.instance.renderline((Texture)null, FPSGraph.this.getAbsoluteX().intValue() + var7 - 8 + 4, FPSGraph.this.getAbsoluteY().intValue() + (int)(var5 - var8), FPSGraph.this.getAbsoluteX().intValue() + var7 + 4, FPSGraph.this.getAbsoluteY().intValue() + (int)(var5 - var10), var1, var2, var3, 0.35F, 1);
               var7 += 8;
               var8 = var10;
            }

         }
      }
   }
}
