package zombie.iso.sprite;

import java.util.function.Consumer;
import org.lwjgl.opengl.GL11;
import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.opengl.RenderThread;
import zombie.core.opengl.Shader;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.input.Mouse;
import zombie.iso.IsoCamera;

public final class IsoCursor {
   private static IsoCursor instance = null;
   IsoCursor.IsoCursorShader m_shader = null;

   public static IsoCursor getInstance() {
      if (instance == null) {
         instance = new IsoCursor();
      }

      return instance;
   }

   private IsoCursor() {
      RenderThread.invokeOnRenderContext(this::createShader);
      if (this.m_shader != null) {
         this.m_shader.m_textureCursor = Texture.getSharedTexture("media/ui/isocursor.png");
      }

   }

   private void createShader() {
      this.m_shader = new IsoCursor.IsoCursorShader();
   }

   public void render(int var1) {
      if (Core.getInstance().getOffscreenBuffer() != null) {
         IsoPlayer var2 = IsoPlayer.players[var1];
         if (var2 != null && !var2.isDead() && var2.isAiming() && var2.PlayerIndex == 0 && var2.JoypadBind == -1) {
            if (!GameTime.isGamePaused()) {
               if (this.m_shader != null && this.m_shader.isCompiled()) {
                  float var3 = 1.0F / Core.getInstance().getZoom(var1);
                  int var4 = (int)((float)(this.m_shader.m_textureCursor.getWidth() * Core.TileScale) / 2.0F * var3);
                  int var5 = (int)((float)(this.m_shader.m_textureCursor.getHeight() * Core.TileScale) / 2.0F * var3);
                  this.m_shader.m_screenX = Mouse.getXA() - var4 / 2;
                  this.m_shader.m_screenY = Mouse.getYA() - var5 / 2;
                  this.m_shader.width = var4;
                  this.m_shader.height = var5;
                  int var6 = IsoCamera.getScreenLeft(var1);
                  int var7 = IsoCamera.getScreenTop(var1);
                  int var8 = IsoCamera.getScreenWidth(var1);
                  int var9 = IsoCamera.getScreenHeight(var1);
                  SpriteRenderer.instance.StartShader(this.m_shader.getID(), var1);
                  SpriteRenderer.instance.renderClamped(this.m_shader.m_textureCursor, this.m_shader.m_screenX, this.m_shader.m_screenY, var4, var5, var6, var7, var8, var9, 1.0F, 1.0F, 1.0F, 1.0F, this.m_shader);
                  SpriteRenderer.instance.EndShader();
               }
            }
         }
      }
   }

   private static class IsoCursorShader extends Shader implements Consumer {
      private float m_alpha = 1.0F;
      private Texture m_textureCursor;
      private Texture m_textureWorld;
      private int m_screenX;
      private int m_screenY;

      IsoCursorShader() {
         super("isocursor");
      }

      public void startMainThread(TextureDraw var1, int var2) {
         this.m_alpha = this.calculateAlpha();
         this.m_textureWorld = Core.getInstance().OffscreenBuffer.getTexture(var2);
      }

      public void startRenderThread(TextureDraw var1) {
         this.getProgram().setValue("u_alpha", this.m_alpha);
         this.getProgram().setValue("TextureCursor", this.m_textureCursor, 0);
         this.getProgram().setValue("TextureBackground", this.m_textureWorld, 1);
         SpriteRenderer.ringBuffer.shaderChangedTexture1();
         GL11.glEnable(3042);
      }

      public void accept(TextureDraw var1) {
         byte var2 = 0;
         int var3 = (int)var1.x0 - this.m_screenX;
         int var4 = (int)var1.y0 - this.m_screenY;
         int var5 = this.m_screenX + this.width - (int)var1.x2;
         int var6 = this.m_screenY + this.height - (int)var1.y2;
         this.m_screenX += var3;
         this.m_screenY += var4;
         this.width -= var3 + var5;
         this.height -= var4 + var6;
         float var7 = (float)this.m_textureWorld.getWidthHW();
         float var8 = (float)this.m_textureWorld.getHeightHW();
         float var9 = (float)(IsoCamera.getScreenTop(var2) + IsoCamera.getScreenHeight(var2) - (this.m_screenY + this.height));
         var1.tex1 = this.m_textureWorld;
         var1.tex1_u0 = (float)this.m_screenX / var7;
         var1.tex1_v3 = var9 / var8;
         var1.tex1_u1 = (float)(this.m_screenX + this.width) / var7;
         var1.tex1_v2 = var9 / var8;
         var1.tex1_u2 = (float)(this.m_screenX + this.width) / var7;
         var1.tex1_v1 = (var9 + (float)this.height) / var8;
         var1.tex1_u3 = (float)this.m_screenX / var7;
         var1.tex1_v0 = (var9 + (float)this.height) / var8;
      }

      float calculateAlpha() {
         float var1 = 0.05F;
         switch(Core.getInstance().getIsoCursorVisibility()) {
         case 0:
            var1 = 0.0F;
            break;
         case 1:
            var1 = 0.05F;
            break;
         case 2:
            var1 = 0.1F;
            break;
         case 3:
            var1 = 0.15F;
            break;
         case 4:
            var1 = 0.3F;
            break;
         case 5:
            var1 = 0.5F;
            break;
         case 6:
            var1 = 0.75F;
         }

         return var1;
      }
   }
}
