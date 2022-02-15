package zombie.ui;

import zombie.GameTime;
import zombie.SoundManager;
import zombie.core.Core;
import zombie.core.textures.Texture;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class SpeedControls extends UIElement {
   public static SpeedControls instance = null;
   public int CurrentSpeed = 1;
   public int SpeedBeforePause = 1;
   public float MultiBeforePause = 1.0F;
   float alpha = 1.0F;
   boolean MouseOver = false;
   public static HUDButton Play;
   public static HUDButton Pause;
   public static HUDButton FastForward;
   public static HUDButton FasterForward;
   public static HUDButton Wait;

   public SpeedControls() {
      this.x = 0.0D;
      this.y = 0.0D;
      byte var1 = 2;
      Pause = new SpeedControls.SCButton("Pause", 1.0F, 0.0F, "media/ui/Time_Pause_Off.png", "media/ui/Time_Pause_On.png", this);
      Play = new SpeedControls.SCButton("Play", (float)(Pause.x + (double)Pause.width + (double)var1), 0.0F, "media/ui/Time_Play_Off.png", "media/ui/Time_Play_On.png", this);
      FastForward = new SpeedControls.SCButton("Fast Forward x 1", (float)(Play.x + (double)Play.width + (double)var1), 0.0F, "media/ui/Time_FFwd1_Off.png", "media/ui/Time_FFwd1_On.png", this);
      FasterForward = new SpeedControls.SCButton("Fast Forward x 2", (float)(FastForward.x + (double)FastForward.width + (double)var1), 0.0F, "media/ui/Time_FFwd2_Off.png", "media/ui/Time_FFwd2_On.png", this);
      Wait = new SpeedControls.SCButton("Wait", (float)(FasterForward.x + (double)FasterForward.width + (double)var1), 0.0F, "media/ui/Time_Wait_Off.png", "media/ui/Time_Wait_On.png", this);
      this.width = (float)((int)Wait.x) + Wait.width;
      this.height = Wait.height;
      this.AddChild(Pause);
      this.AddChild(Play);
      this.AddChild(FastForward);
      this.AddChild(FasterForward);
      this.AddChild(Wait);
   }

   public void ButtonClicked(String var1) {
      GameTime.instance.setMultiplier(1.0F);
      if ("Pause".equals(var1)) {
         if (this.CurrentSpeed > 0) {
            this.SetCurrentGameSpeed(0);
         } else {
            this.SetCurrentGameSpeed(5);
         }
      }

      if ("Play".equals(var1)) {
         this.SetCurrentGameSpeed(1);
         GameTime.instance.setMultiplier(1.0F);
      }

      if ("Fast Forward x 1".equals(var1)) {
         this.SetCurrentGameSpeed(2);
         GameTime.instance.setMultiplier(5.0F);
      }

      if ("Fast Forward x 2".equals(var1)) {
         this.SetCurrentGameSpeed(3);
         GameTime.instance.setMultiplier(20.0F);
      }

      if ("Wait".equals(var1)) {
         this.SetCurrentGameSpeed(4);
         GameTime.instance.setMultiplier(40.0F);
      }

   }

   public int getCurrentGameSpeed() {
      return !GameClient.bClient && !GameServer.bServer ? this.CurrentSpeed : 1;
   }

   public void SetCorrectIconStates() {
      if (this.CurrentSpeed == 0) {
         super.ButtonClicked("Pause");
      }

      if (this.CurrentSpeed == 1) {
         super.ButtonClicked("Play");
      }

      if (GameTime.instance.getTrueMultiplier() == 5.0F) {
         super.ButtonClicked("Fast Forward x 1");
      }

      if (GameTime.instance.getTrueMultiplier() == 20.0F) {
         super.ButtonClicked("Fast Forward x 2");
      }

      if (GameTime.instance.getTrueMultiplier() == 40.0F) {
         super.ButtonClicked("Wait");
      }

   }

   public void SetCurrentGameSpeed(int var1) {
      if (this.CurrentSpeed > 0 && var1 == 0) {
         SoundManager.instance.pauseSoundAndMusic();
         SoundManager.instance.setMusicState("PauseMenu");
      } else if (this.CurrentSpeed == 0 && var1 > 0) {
         SoundManager.instance.setMusicState("InGame");
         SoundManager.instance.resumeSoundAndMusic();
      }

      GameTime.instance.setMultiplier(1.0F);
      if (var1 == 0) {
         this.SpeedBeforePause = this.CurrentSpeed;
         this.MultiBeforePause = GameTime.instance.getMultiplier();
      }

      if (var1 == 5) {
         var1 = this.SpeedBeforePause;
         GameTime.instance.setMultiplier(this.MultiBeforePause);
      }

      this.CurrentSpeed = var1;
      this.SetCorrectIconStates();
   }

   public Boolean onMouseMove(double var1, double var3) {
      if (!this.isVisible()) {
         return false;
      } else {
         this.MouseOver = true;
         super.onMouseMove(var1, var3);
         this.SetCorrectIconStates();
         return Boolean.TRUE;
      }
   }

   public void onMouseMoveOutside(double var1, double var3) {
      super.onMouseMoveOutside(var1, var3);
      this.MouseOver = false;
      this.SetCorrectIconStates();
   }

   public void render() {
      super.render();
      if ("Tutorial".equals(Core.GameMode)) {
         Pause.setVisible(false);
         Play.setVisible(false);
         FastForward.setVisible(false);
         FasterForward.setVisible(false);
         Wait.setVisible(false);
      }

      this.SetCorrectIconStates();
   }

   public void update() {
      super.update();
      this.SetCorrectIconStates();
   }

   public static final class SCButton extends HUDButton {
      private static final int BORDER = 3;

      public SCButton(String var1, float var2, float var3, String var4, String var5, UIElement var6) {
         super(var1, (double)var2, (double)var3, var4, var5, var6);
         this.width += 6.0F;
         this.height += 6.0F;
      }

      public void render() {
         int var1 = 3;
         if (this.clicked) {
            ++var1;
         }

         this.DrawTextureScaledCol((Texture)null, 0.0D, this.clicked ? 1.0D : 0.0D, (double)this.width, (double)this.height, 0.0D, 0.0D, 0.0D, 0.5D);
         if (!this.mouseOver && !this.name.equals(this.display.getClickedValue())) {
            this.DrawTextureScaled(this.texture, 3.0D, (double)var1, (double)this.texture.getWidth(), (double)this.texture.getHeight(), (double)this.notclickedAlpha);
         } else {
            this.DrawTextureScaled(this.highlight, 3.0D, (double)var1, (double)this.highlight.getWidth(), (double)this.highlight.getHeight(), (double)this.clickedalpha);
         }

         if (this.overicon != null) {
            this.DrawTextureScaled(this.overicon, 3.0D, (double)var1, (double)this.overicon.getWidth(), (double)this.overicon.getHeight(), 1.0D);
         }

      }
   }
}
