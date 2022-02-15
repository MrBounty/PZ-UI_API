package zombie.ui;

import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.core.Core;
import zombie.core.Translator;
import zombie.core.textures.Texture;
import zombie.debug.DebugOptions;
import zombie.network.GameClient;

public final class NewHealthPanel extends NewWindow {
   public static NewHealthPanel instance;
   public Texture BodyOutline;
   public UI_BodyPart Foot_L;
   public UI_BodyPart Foot_R;
   public UI_BodyPart ForeArm_L;
   public UI_BodyPart ForeArm_R;
   public UI_BodyPart Groin;
   public UI_BodyPart Hand_L;
   public UI_BodyPart Hand_R;
   public UI_BodyPart Head;
   public UI_BodyPart LowerLeg_L;
   public UI_BodyPart LowerLeg_R;
   public UI_BodyPart Neck;
   public UI_BodyPart Torso_Lower;
   public UI_BodyPart Torso_Upper;
   public UI_BodyPart UpperArm_L;
   public UI_BodyPart UpperArm_R;
   public UI_BodyPart UpperLeg_L;
   public UI_BodyPart UpperLeg_R;
   public Texture HealthBar;
   public Texture HealthBarBack;
   public Texture HealthIcon;
   IsoGameCharacter ParentChar;

   public void SetCharacter(IsoGameCharacter var1) {
      this.ParentChar = var1;
   }

   public NewHealthPanel(int var1, int var2, IsoGameCharacter var3) {
      super(var1, var2, 10, 10, true);
      this.ParentChar = var3;
      this.ResizeToFitY = false;
      this.visible = false;
      instance = this;
      byte var4 = 2;
      this.HealthIcon = Texture.getSharedTexture("media/ui/Heart_On.png", var4);
      this.HealthBarBack = Texture.getSharedTexture("media/ui/BodyDamage/DamageBar_Vert.png", var4);
      this.HealthBar = Texture.getSharedTexture("media/ui/BodyDamage/DamageBar_Vert_Fill.png", var4);
      String var5 = "male";
      if (var3.isFemale()) {
         var5 = "female";
      }

      this.BodyOutline = Texture.getSharedTexture("media/ui/BodyDamage/" + var5 + "_base.png");
      this.width = 300.0F;
      this.height = (float)(270 + this.titleRight.getHeight() + 5);
      this.Hand_L = new UI_BodyPart(BodyPartType.Hand_L, 0, 0, "hand_left.png", this.ParentChar, false);
      this.Hand_R = new UI_BodyPart(BodyPartType.Hand_R, 0, 0, "hand_right.png", this.ParentChar, false);
      this.ForeArm_L = new UI_BodyPart(BodyPartType.ForeArm_L, 0, 0, "lowerarm_left.png", this.ParentChar, false);
      this.ForeArm_R = new UI_BodyPart(BodyPartType.ForeArm_R, 0, 0, "lowerarm_right.png", this.ParentChar, false);
      this.UpperArm_L = new UI_BodyPart(BodyPartType.UpperArm_L, 0, 0, "upperarm_left.png", this.ParentChar, false);
      this.UpperArm_R = new UI_BodyPart(BodyPartType.UpperArm_R, 0, 0, "upperarm_right.png", this.ParentChar, false);
      this.Torso_Upper = new UI_BodyPart(BodyPartType.Torso_Upper, 0, 0, "chest.png", this.ParentChar, false);
      this.Torso_Lower = new UI_BodyPart(BodyPartType.Torso_Lower, 0, 0, "abdomen.png", this.ParentChar, false);
      this.Head = new UI_BodyPart(BodyPartType.Head, 0, 0, "head.png", this.ParentChar, false);
      this.Neck = new UI_BodyPart(BodyPartType.Neck, 0, 0, "neck.png", this.ParentChar, false);
      this.Groin = new UI_BodyPart(BodyPartType.Groin, 0, 0, "groin.png", this.ParentChar, false);
      this.UpperLeg_L = new UI_BodyPart(BodyPartType.UpperLeg_L, 0, 0, "upperleg_left.png", this.ParentChar, false);
      this.UpperLeg_R = new UI_BodyPart(BodyPartType.UpperLeg_R, 0, 0, "upperleg_right.png", this.ParentChar, false);
      this.LowerLeg_L = new UI_BodyPart(BodyPartType.LowerLeg_L, 0, 0, "lowerleg_left.png", this.ParentChar, false);
      this.LowerLeg_R = new UI_BodyPart(BodyPartType.LowerLeg_R, 0, 0, "lowerleg_right.png", this.ParentChar, false);
      this.Foot_L = new UI_BodyPart(BodyPartType.Foot_L, 0, 0, "foot_left.png", this.ParentChar, false);
      this.Foot_R = new UI_BodyPart(BodyPartType.Foot_R, 0, 0, "foot_right.png", this.ParentChar, false);
      this.AddChild(this.Hand_L);
      this.AddChild(this.Hand_R);
      this.AddChild(this.ForeArm_L);
      this.AddChild(this.ForeArm_R);
      this.AddChild(this.UpperArm_L);
      this.AddChild(this.UpperArm_R);
      this.AddChild(this.Torso_Upper);
      this.AddChild(this.Torso_Lower);
      this.AddChild(this.Head);
      this.AddChild(this.Neck);
      this.AddChild(this.Groin);
      this.AddChild(this.UpperLeg_L);
      this.AddChild(this.UpperLeg_R);
      this.AddChild(this.LowerLeg_L);
      this.AddChild(this.LowerLeg_R);
      this.AddChild(this.Foot_L);
      this.AddChild(this.Foot_R);
   }

   public void render() {
      if (this.isVisible()) {
         this.DrawTexture(this.BodyOutline, 0.0D, 0.0D, (double)this.alpha);
         this.Hand_L.render();
         this.Hand_R.render();
         this.ForeArm_L.render();
         this.ForeArm_R.render();
         this.UpperArm_L.render();
         this.UpperArm_R.render();
         this.Torso_Upper.render();
         this.Torso_Lower.render();
         this.Head.render();
         this.Neck.render();
         this.Groin.render();
         this.UpperLeg_L.render();
         this.UpperLeg_R.render();
         this.LowerLeg_L.render();
         this.LowerLeg_R.render();
         this.Foot_L.render();
         this.Foot_R.render();
         BodyDamage var1 = this.ParentChar.getBodyDamage();
         if (GameClient.bClient && this.ParentChar instanceof IsoPlayer && !((IsoPlayer)this.ParentChar).isLocalPlayer()) {
            var1 = this.ParentChar.getBodyDamageRemote();
         }

         float var2 = (100.0F - var1.getHealth()) * 1.7F;
         this.DrawTexture(this.HealthIcon, 126.0D, 200.0D, (double)this.alpha);
         this.DrawTextureScaled(this.HealthBarBack, 130.0D, 25.0D, 18.0D, 172.0D, (double)this.alpha);
         this.DrawTextureScaled(this.HealthBar, 130.0D, (double)(26 + (int)var2), 18.0D, (double)(170 - (int)var2), (double)this.alpha);
         double var3 = 0.15D;
         double var5 = 1.0D;
         this.DrawTextureScaledColor((Texture)null, 130.0D, 25.0D, 18.0D, 1.0D, var3, var3, var3, var5);
         this.DrawTextureScaledColor((Texture)null, 130.0D, 25.0D, 1.0D, 172.0D, var3, var3, var3, var5);
         this.DrawTextureScaledColor((Texture)null, 147.0D, 25.0D, 1.0D, 172.0D, var3, var3, var3, var5);
         if (Core.bDebug && DebugOptions.instance.UIRenderOutline.getValue()) {
            Double var7 = -this.getXScroll();
            Double var8 = -this.getYScroll();
            this.DrawTextureScaledColor((Texture)null, var7, var8, 1.0D, (double)this.height, 1.0D, 1.0D, 1.0D, 0.5D);
            this.DrawTextureScaledColor((Texture)null, var7 + 1.0D, var8, (double)this.width - 2.0D, 1.0D, 1.0D, 1.0D, 1.0D, 0.5D);
            this.DrawTextureScaledColor((Texture)null, var7 + (double)this.width - 1.0D, var8, 1.0D, (double)this.height, 1.0D, 1.0D, 1.0D, 0.5D);
            this.DrawTextureScaledColor((Texture)null, var7 + 1.0D, var8 + (double)this.height - 1.0D, (double)this.width - 2.0D, 1.0D, 1.0D, 1.0D, 1.0D, 0.5D);
         }

      }
   }

   public void update() {
      if (this.isVisible()) {
         super.update();
      }
   }

   public String getDamageStatusString() {
      BodyDamage var1 = this.ParentChar.getBodyDamage();
      if (GameClient.bClient && this.ParentChar instanceof IsoPlayer && !((IsoPlayer)this.ParentChar).isLocalPlayer()) {
         var1 = this.ParentChar.getBodyDamageRemote();
      }

      if (var1.getHealth() == 100.0F) {
         return Translator.getText("IGUI_health_ok");
      } else if (var1.getHealth() > 90.0F) {
         return Translator.getText("IGUI_health_Slight_damage");
      } else if (var1.getHealth() > 80.0F) {
         return Translator.getText("IGUI_health_Very_Minor_damage");
      } else if (var1.getHealth() > 70.0F) {
         return Translator.getText("IGUI_health_Minor_damage");
      } else if (var1.getHealth() > 60.0F) {
         return Translator.getText("IGUI_health_Moderate_damage");
      } else if (var1.getHealth() > 50.0F) {
         return Translator.getText("IGUI_health_Severe_damage");
      } else if (var1.getHealth() > 40.0F) {
         return Translator.getText("IGUI_health_Very_Severe_damage");
      } else if (var1.getHealth() > 20.0F) {
         return Translator.getText("IGUI_health_Crital_damage");
      } else if (var1.getHealth() > 10.0F) {
         return Translator.getText("IGUI_health_Highly_Crital_damage");
      } else {
         return var1.getHealth() > 0.0F ? Translator.getText("IGUI_health_Terminal_damage") : Translator.getText("IGUI_health_Deceased");
      }
   }
}
