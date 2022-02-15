package zombie.ui;

import zombie.core.textures.Texture;
import zombie.network.GameServer;

public class HUDButton extends UIElement {
   boolean clicked = false;
   UIElement display;
   Texture highlight;
   Texture overicon = null;
   boolean mouseOver = false;
   String name;
   Texture texture;
   UIEventHandler handler;
   public float notclickedAlpha = 0.85F;
   public float clickedalpha = 1.0F;

   public HUDButton(String var1, double var2, double var4, String var6, String var7, UIElement var8) {
      if (!GameServer.bServer) {
         this.display = var8;
         this.name = var1;
         if (this.texture == null) {
            this.texture = Texture.getSharedTexture(var6);
            this.highlight = Texture.getSharedTexture(var7);
         }

         this.x = var2;
         this.y = var4;
         this.width = (float)this.texture.getWidth();
         this.height = (float)this.texture.getHeight();
      }
   }

   public HUDButton(String var1, float var2, float var3, String var4, String var5, UIEventHandler var6) {
      if (!GameServer.bServer) {
         this.texture = Texture.getSharedTexture(var4);
         this.highlight = Texture.getSharedTexture(var5);
         this.handler = var6;
         this.name = var1;
         if (this.texture == null) {
            this.texture = Texture.getSharedTexture(var4);
            this.highlight = Texture.getSharedTexture(var5);
         }

         this.x = (double)var2;
         this.y = (double)var3;
         this.width = (float)this.texture.getWidth();
         this.height = (float)this.texture.getHeight();
      }
   }

   public HUDButton(String var1, float var2, float var3, String var4, String var5, String var6, UIElement var7) {
      if (!GameServer.bServer) {
         this.overicon = Texture.getSharedTexture(var6);
         this.display = var7;
         this.texture = Texture.getSharedTexture(var4);
         this.highlight = Texture.getSharedTexture(var5);
         this.name = var1;
         if (this.texture == null) {
            this.texture = Texture.getSharedTexture(var4);
            this.highlight = Texture.getSharedTexture(var5);
         }

         this.x = (double)var2;
         this.y = (double)var3;
         this.width = (float)this.texture.getWidth();
         this.height = (float)this.texture.getHeight();
      }
   }

   public HUDButton(String var1, float var2, float var3, String var4, String var5, String var6, UIEventHandler var7) {
      if (!GameServer.bServer) {
         this.texture = Texture.getSharedTexture(var4);
         this.highlight = Texture.getSharedTexture(var5);
         this.overicon = Texture.getSharedTexture(var6);
         this.handler = var7;
         this.name = var1;
         if (this.texture == null) {
            this.texture = Texture.getSharedTexture(var4);
            this.highlight = Texture.getSharedTexture(var5);
         }

         this.x = (double)var2;
         this.y = (double)var3;
         this.width = (float)this.texture.getWidth();
         this.height = (float)this.texture.getHeight();
      }
   }

   public Boolean onMouseDown(double var1, double var3) {
      this.clicked = true;
      return Boolean.TRUE;
   }

   public Boolean onMouseMove(double var1, double var3) {
      this.mouseOver = true;
      return Boolean.TRUE;
   }

   public void onMouseMoveOutside(double var1, double var3) {
      this.clicked = false;
      if (this.display != null) {
         if (!this.name.equals(this.display.getClickedValue())) {
            this.mouseOver = false;
         }

      }
   }

   public Boolean onMouseUp(double var1, double var3) {
      if (this.clicked) {
         if (this.display != null) {
            this.display.ButtonClicked(this.name);
         } else if (this.handler != null) {
            this.handler.Selected(this.name, 0, 0);
         }
      }

      this.clicked = false;
      return Boolean.TRUE;
   }

   public void render() {
      int var1 = 0;
      if (this.clicked) {
         ++var1;
      }

      if (!this.mouseOver && !this.name.equals(this.display.getClickedValue())) {
         this.DrawTextureScaled(this.texture, 0.0D, (double)var1, this.getWidth(), this.getHeight(), (double)this.notclickedAlpha);
      } else {
         this.DrawTextureScaled(this.highlight, 0.0D, (double)var1, this.getWidth(), this.getHeight(), (double)this.clickedalpha);
      }

      if (this.overicon != null) {
         this.DrawTextureScaled(this.overicon, 0.0D, (double)var1, (double)this.overicon.getWidth(), (double)this.overicon.getHeight(), 1.0D);
      }

      super.render();
   }

   public void update() {
      super.update();
   }
}
