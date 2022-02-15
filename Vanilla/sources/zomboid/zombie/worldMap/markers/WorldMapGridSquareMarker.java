package zombie.worldMap.markers;

import java.util.function.Consumer;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.worldMap.UIWorldMap;

public final class WorldMapGridSquareMarker extends WorldMapMarker {
   Texture m_texture1 = Texture.getSharedTexture("media/textures/worldMap/circle_center.png");
   Texture m_texture2 = Texture.getSharedTexture("media/textures/worldMap/circle_only_highlight.png");
   float m_r = 1.0F;
   float m_g = 1.0F;
   float m_b = 1.0F;
   float m_a = 1.0F;
   int m_worldX;
   int m_worldY;
   int m_radius = 10;
   int m_minScreenRadius = 64;
   boolean m_blink = true;

   WorldMapGridSquareMarker init(int var1, int var2, int var3, float var4, float var5, float var6, float var7) {
      this.m_worldX = var1;
      this.m_worldY = var2;
      this.m_radius = var3;
      this.m_r = var4;
      this.m_g = var5;
      this.m_b = var6;
      this.m_a = var7;
      return this;
   }

   public void setBlink(boolean var1) {
      this.m_blink = var1;
   }

   public void setMinScreenRadius(int var1) {
      this.m_minScreenRadius = var1;
   }

   void render(UIWorldMap var1) {
      float var2 = PZMath.max((float)this.m_radius, (float)this.m_minScreenRadius / var1.getAPI().getWorldScale());
      float var3 = var1.getAPI().worldToUIX((float)this.m_worldX - var2, (float)this.m_worldY - var2);
      float var4 = var1.getAPI().worldToUIY((float)this.m_worldX - var2, (float)this.m_worldY - var2);
      float var5 = var1.getAPI().worldToUIX((float)this.m_worldX + var2, (float)this.m_worldY - var2);
      float var6 = var1.getAPI().worldToUIY((float)this.m_worldX + var2, (float)this.m_worldY - var2);
      float var7 = var1.getAPI().worldToUIX((float)this.m_worldX + var2, (float)this.m_worldY + var2);
      float var8 = var1.getAPI().worldToUIY((float)this.m_worldX + var2, (float)this.m_worldY + var2);
      float var9 = var1.getAPI().worldToUIX((float)this.m_worldX - var2, (float)this.m_worldY + var2);
      float var10 = var1.getAPI().worldToUIY((float)this.m_worldX - var2, (float)this.m_worldY + var2);
      var3 = (float)((double)var3 + var1.getAbsoluteX());
      var4 = (float)((double)var4 + var1.getAbsoluteY());
      var5 = (float)((double)var5 + var1.getAbsoluteX());
      var6 = (float)((double)var6 + var1.getAbsoluteY());
      var7 = (float)((double)var7 + var1.getAbsoluteX());
      var8 = (float)((double)var8 + var1.getAbsoluteY());
      var9 = (float)((double)var9 + var1.getAbsoluteX());
      var10 = (float)((double)var10 + var1.getAbsoluteY());
      float var11 = this.m_a * (this.m_blink ? Core.blinkAlpha : 1.0F);
      if (this.m_texture1 != null && this.m_texture1.isReady()) {
         SpriteRenderer.instance.render(this.m_texture1, (double)var3, (double)var4, (double)var5, (double)var6, (double)var7, (double)var8, (double)var9, (double)var10, this.m_r, this.m_g, this.m_b, var11, (Consumer)null);
      }

      if (this.m_texture2 != null && this.m_texture2.isReady()) {
         SpriteRenderer.instance.render(this.m_texture2, (double)var3, (double)var4, (double)var5, (double)var6, (double)var7, (double)var8, (double)var9, (double)var10, this.m_r, this.m_g, this.m_b, var11, (Consumer)null);
      }

   }
}
