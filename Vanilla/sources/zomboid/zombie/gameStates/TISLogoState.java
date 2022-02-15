package zombie.gameStates;

import java.util.function.Consumer;
import zombie.GameTime;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.input.GameKeyboard;
import zombie.input.Mouse;
import zombie.ui.TextManager;
import zombie.ui.UIFont;
import zombie.ui.UIManager;

public final class TISLogoState extends GameState {
   public float alpha = 0.0F;
   public float alphaStep = 0.02F;
   public float logoDisplayTime = 20.0F;
   public int screenNumber = 1;
   public int stage = 0;
   public float targetAlpha = 0.0F;
   private boolean bNoRender = false;
   private final TISLogoState.LogoElement logoTIS = new TISLogoState.LogoElement("media/ui/TheIndieStoneLogo_Lineart_White.png");
   private final TISLogoState.LogoElement logoFMOD = new TISLogoState.LogoElement("media/ui/FMODLogo.png");
   private final TISLogoState.LogoElement logoGA = new TISLogoState.LogoElement("media/ui/GA-1280-white.png");
   private final TISLogoState.LogoElement logoNW = new TISLogoState.LogoElement("media/ui/NW_Logo_Combined.png");
   private static final int SCREEN_TIS = 1;
   private static final int SCREEN_OTHER = 2;
   private static final int STAGE_FADING_IN_LOGO = 0;
   private static final int STAGE_HOLDING_LOGO = 1;
   private static final int STAGE_FADING_OUT_LOGO = 2;
   private static final int STAGE_EXIT = 3;

   public void enter() {
      UIManager.bSuspend = true;
      this.alpha = 0.0F;
      this.targetAlpha = 1.0F;
   }

   public void exit() {
      UIManager.bSuspend = false;
   }

   public void render() {
      if (this.bNoRender) {
         Core.getInstance().StartFrame();
         SpriteRenderer.instance.renderi((Texture)null, 0, 0, Core.getInstance().getOffscreenWidth(0), Core.getInstance().getOffscreenHeight(0), 0.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
         Core.getInstance().EndFrame();
      } else {
         Core.getInstance().StartFrame();
         Core.getInstance().EndFrame();
         boolean var1 = UIManager.useUIFBO;
         UIManager.useUIFBO = false;
         Core.getInstance().StartFrameUI();
         SpriteRenderer.instance.renderi((Texture)null, 0, 0, Core.getInstance().getOffscreenWidth(0), Core.getInstance().getOffscreenHeight(0), 0.0F, 0.0F, 0.0F, 1.0F, (Consumer)null);
         if (this.screenNumber == 1) {
            this.logoTIS.centerOnScreen();
            this.logoTIS.render(this.alpha);
         }

         if (this.screenNumber == 2) {
            this.renderAttribution();
         }

         Core.getInstance().EndFrameUI();
         UIManager.useUIFBO = var1;
      }
   }

   private void renderAttribution() {
      int var1 = Core.getInstance().getScreenWidth();
      int var2 = Core.getInstance().getScreenHeight();
      byte var3 = 50;
      byte var4 = 3;
      int var5 = (var2 - (var4 + 1) * var3) / 3;
      Texture var7 = this.logoGA.m_texture;
      int var9;
      if (var7 != null && var7.isReady()) {
         int var8 = (int)((float)(var7.getWidth() * var5) / (float)var7.getHeight());
         var9 = (var1 - var8) / 2;
         this.logoGA.setPos(var9, var3);
         this.logoGA.setSize(var8, var5);
         this.logoGA.render(this.alpha);
      }

      int var6 = var3 + var5 + var3;
      var6 = (int)((float)var6 + (float)var5 * 0.15F);
      var7 = this.logoNW.m_texture;
      int var10;
      int var11;
      int var12;
      float var16;
      if (var7 != null && var7.isReady()) {
         var16 = 0.5F;
         var9 = (int)((float)var7.getWidth() * var16 * (float)var5 / (float)var7.getHeight());
         var10 = (int)((float)var5 * var16);
         var11 = (var1 - var9) / 2;
         var12 = (var5 - var10) / 2;
         this.logoNW.setPos(var11, var6 + var12);
         this.logoNW.setSize(var9, var10);
         this.logoNW.render(this.alpha);
      }

      var6 += var5 + var3;
      var7 = this.logoFMOD.m_texture;
      if (var7 != null && var7.isReady()) {
         var16 = 0.35F;
         var9 = TextManager.instance.getFontHeight(UIFont.Small);
         var10 = (int)((float)var5 * var16 - 16.0F - (float)var9);
         var11 = (int)((float)var7.getWidth() * ((float)var10 / (float)var7.getHeight()));
         var12 = (var1 - var11) / 2;
         int var13 = (var5 - var10) / 2;
         int var14 = var6 + var13 + var10 + 16;
         this.logoFMOD.setPos(var12, var6 + var13);
         this.logoFMOD.setSize(var11, var10);
         this.logoFMOD.render(this.alpha);
         String var15 = "Made with FMOD Studio by Firelight Technologies Pty Ltd.";
         TextManager.instance.DrawStringCentre((double)var1 / 2.0D, (double)var14, var15, 1.0D, 1.0D, 1.0D, (double)this.alpha);
      }

   }

   public GameStateMachine.StateAction update() {
      if (Mouse.isLeftDown() || GameKeyboard.isKeyDown(28) || GameKeyboard.isKeyDown(57) || GameKeyboard.isKeyDown(1)) {
         this.stage = 3;
      }

      if (this.stage == 0) {
         this.targetAlpha = 1.0F;
         if (this.alpha == 1.0F) {
            this.stage = 1;
            this.logoDisplayTime = 20.0F;
         }
      }

      if (this.stage == 1) {
         this.logoDisplayTime -= GameTime.getInstance().getMultiplier() / 1.6F;
         if (this.logoDisplayTime <= 0.0F) {
            this.stage = 2;
         }
      }

      if (this.stage == 2) {
         this.targetAlpha = 0.0F;
         if (this.alpha == 0.0F) {
            if (this.screenNumber == 1) {
               this.screenNumber = 2;
               this.stage = 0;
            } else {
               this.stage = 3;
            }
         }
      }

      if (this.stage == 3) {
         this.targetAlpha = 0.0F;
         if (this.alpha == 0.0F) {
            this.bNoRender = true;
            return GameStateMachine.StateAction.Continue;
         }
      }

      if (this.alpha < this.targetAlpha) {
         this.alpha += this.alphaStep * GameTime.getInstance().getMultiplier();
         if (this.alpha > this.targetAlpha) {
            this.alpha = this.targetAlpha;
         }
      } else if (this.alpha > this.targetAlpha) {
         this.alpha -= this.alphaStep * GameTime.getInstance().getMultiplier();
         if (this.stage == 3) {
            this.alpha -= this.alphaStep * GameTime.getInstance().getMultiplier();
         }

         if (this.alpha < this.targetAlpha) {
            this.alpha = this.targetAlpha;
         }
      }

      return GameStateMachine.StateAction.Remain;
   }

   private static final class LogoElement {
      Texture m_texture;
      int m_x;
      int m_y;
      int m_width;
      int m_height;

      LogoElement(String var1) {
         this.m_texture = Texture.getSharedTexture(var1);
         if (this.m_texture != null) {
            this.m_width = this.m_texture.getWidth();
            this.m_height = this.m_texture.getHeight();
         }

      }

      void centerOnScreen() {
         this.m_x = (Core.getInstance().getScreenWidth() - this.m_width) / 2;
         this.m_y = (Core.getInstance().getScreenHeight() - this.m_height) / 2;
      }

      void setPos(int var1, int var2) {
         this.m_x = var1;
         this.m_y = var2;
      }

      void setSize(int var1, int var2) {
         this.m_width = var1;
         this.m_height = var2;
      }

      void render(float var1) {
         if (this.m_texture != null && this.m_texture.isReady()) {
            SpriteRenderer.instance.renderi(this.m_texture, this.m_x, this.m_y, this.m_width, this.m_height, 1.0F, 1.0F, 1.0F, var1, (Consumer)null);
         }
      }
   }
}
