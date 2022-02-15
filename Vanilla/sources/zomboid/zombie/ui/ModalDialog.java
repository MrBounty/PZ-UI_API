package zombie.ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import zombie.GameTime;
import zombie.GameWindow;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.gameStates.IngameState;

public final class ModalDialog extends NewWindow {
   public boolean bYes = false;
   public String Name;
   UIEventHandler handler = null;
   public boolean Clicked = false;

   public ModalDialog(String var1, String var2, boolean var3) {
      super(Core.getInstance().getOffscreenWidth(0) / 2, Core.getInstance().getOffscreenHeight(0) / 2, 470, 10, false);
      this.Name = var1;
      this.ResizeToFitY = false;
      this.IgnoreLossControl = true;
      TextBox var4 = new TextBox(UIFont.Medium, 0, 0, 450, var2);
      var4.Centred = true;
      var4.ResizeParent = true;
      var4.update();
      this.Nest(var4, 20, 10, 20, 10);
      this.update();
      this.height *= 1.3F;
      if (var3) {
         this.AddChild(new DialogButton(this, (float)(this.getWidth().intValue() / 2 - 40), (float)(this.getHeight().intValue() - 18), "Yes", "Yes"));
         this.AddChild(new DialogButton(this, (float)(this.getWidth().intValue() / 2 + 40), (float)(this.getHeight().intValue() - 18), "No", "No"));
      } else {
         this.AddChild(new DialogButton(this, (float)(this.getWidth().intValue() / 2), (float)(this.getHeight().intValue() - 18), "Ok", "Ok"));
      }

      this.x -= (double)(this.width / 2.0F);
      this.y -= (double)(this.height / 2.0F);
   }

   public void ButtonClicked(String var1) {
      if (this.handler != null) {
         this.handler.ModalClick(this.Name, var1);
         this.setVisible(false);
      } else {
         if (var1.equals("Ok")) {
            UIManager.getSpeedControls().SetCurrentGameSpeed(4);
            this.Clicked(var1);
            this.Clicked = true;
            this.bYes = true;
            this.setVisible(false);
            IngameState.instance.Paused = false;
         }

         if (var1.equals("Yes")) {
            UIManager.getSpeedControls().SetCurrentGameSpeed(4);
            this.Clicked(var1);
            this.Clicked = true;
            this.bYes = true;
            this.setVisible(false);
            IngameState.instance.Paused = false;
         }

         if (var1.equals("No")) {
            UIManager.getSpeedControls().SetCurrentGameSpeed(4);
            this.Clicked(var1);
            this.Clicked = true;
            this.bYes = false;
            this.setVisible(false);
            IngameState.instance.Paused = false;
         }

      }
   }

   public void Clicked(String var1) {
      if (this.Name.equals("Sleep") && var1.equals("Yes")) {
         float var2 = 12.0F * IsoPlayer.getInstance().getStats().fatigue;
         if (var2 < 7.0F) {
            var2 = 7.0F;
         }

         var2 += GameTime.getInstance().getTimeOfDay();
         if (var2 >= 24.0F) {
            var2 -= 24.0F;
         }

         IsoPlayer.getInstance().setForceWakeUpTime((float)((int)var2));
         IsoPlayer.getInstance().setAsleepTime(0.0F);
         TutorialManager.instance.StealControl = true;
         IsoPlayer.getInstance().setAsleep(true);
         UIManager.setbFadeBeforeUI(true);
         UIManager.FadeOut(4.0D);
         UIManager.getSpeedControls().SetCurrentGameSpeed(3);

         try {
            GameWindow.save(true);
         } catch (FileNotFoundException var4) {
            Logger.getLogger(ModalDialog.class.getName()).log(Level.SEVERE, (String)null, var4);
         } catch (IOException var5) {
            Logger.getLogger(ModalDialog.class.getName()).log(Level.SEVERE, (String)null, var5);
         }
      }

      UIManager.Modal.setVisible(false);
   }
}
