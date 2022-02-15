package zombie.ui;

import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.BodyDamage.BodyDamage;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.core.Color;
import zombie.core.textures.Texture;
import zombie.network.GameClient;

public final class UI_BodyPart extends UIElement {
   public float alpha = 1.0F;
   public final Color color = new Color(1.0F, 1.0F, 1.0F, 1.0F);
   public BodyPartType BodyPartType;
   public boolean IsFlipped = false;
   public float MaxOscilatorRate = 0.58F;
   public float MinOscilatorRate = 0.025F;
   public float Oscilator = 0.0F;
   public float OscilatorRate = 0.02F;
   public float OscilatorStep = 0.0F;
   IsoGameCharacter chr;
   boolean mouseOver = false;
   Texture scratchTex;
   Texture bandageTex;
   Texture dirtyBandageTex;
   Texture infectionTex;
   Texture deepWoundTex;
   Texture stitchTex;
   Texture biteTex;
   Texture glassTex;
   Texture boneTex;
   Texture splintTex;
   Texture burnTex;
   Texture bulletTex;

   public UI_BodyPart(BodyPartType var1, int var2, int var3, String var4, IsoGameCharacter var5, boolean var6) {
      String var7 = "male";
      if (var5.isFemale()) {
         var7 = "female";
      }

      this.chr = var5;
      this.BodyPartType = var1;
      this.scratchTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_scratch_" + var4);
      this.bandageTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_bandage_" + var4);
      this.dirtyBandageTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_bandagedirty_" + var4);
      this.infectionTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_infection_" + var4);
      this.biteTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_bite_" + var4);
      this.deepWoundTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_deepwound_" + var4);
      this.stitchTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_stitches_" + var4);
      this.glassTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_glass_" + var4);
      this.boneTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_bones_" + var4);
      this.splintTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_splint_" + var4);
      this.burnTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_burn_" + var4);
      this.bulletTex = Texture.getSharedTexture("media/ui/BodyDamage/" + var7 + "_bullet_" + var4);
      this.x = (double)var2;
      this.y = (double)var3;
      this.width = (float)this.scratchTex.getWidth();
      this.height = (float)this.scratchTex.getHeight();
      this.IsFlipped = var6;
   }

   public void onMouseMoveOutside(double var1, double var3) {
      this.mouseOver = false;
   }

   public void render() {
      BodyDamage var1 = this.chr.getBodyDamage();
      if (GameClient.bClient && this.chr instanceof IsoPlayer && !((IsoPlayer)this.chr).isLocalPlayer()) {
         var1 = this.chr.getBodyDamageRemote();
      }

      if (this.infectionTex != null && !var1.IsBandaged(this.BodyPartType) && var1.getBodyPart(this.BodyPartType).getWoundInfectionLevel() > 0.0F) {
         this.DrawTexture(this.infectionTex, 0.0D, 0.0D, (double)(var1.getBodyPart(this.BodyPartType).getWoundInfectionLevel() / 10.0F));
      }

      if (this.bandageTex != null && var1.IsBandaged(this.BodyPartType) && var1.getBodyPart(this.BodyPartType).getBandageLife() > 0.0F) {
         this.DrawTexture(this.bandageTex, 0.0D, 0.0D, 1.0D);
      } else if (this.dirtyBandageTex != null && var1.IsBandaged(this.BodyPartType) && var1.getBodyPart(this.BodyPartType).getBandageLife() <= 0.0F) {
         this.DrawTexture(this.dirtyBandageTex, 0.0D, 0.0D, 1.0D);
      } else if (this.scratchTex != null && var1.IsScratched(this.BodyPartType)) {
         this.DrawTexture(this.scratchTex, 0.0D, 0.0D, (double)(var1.getBodyPart(this.BodyPartType).getScratchTime() / 20.0F));
      } else if (this.scratchTex != null && var1.IsCut(this.BodyPartType)) {
         this.DrawTexture(this.scratchTex, 0.0D, 0.0D, (double)(var1.getBodyPart(this.BodyPartType).getCutTime() / 20.0F));
      } else if (this.biteTex != null && !var1.IsBandaged(this.BodyPartType) && var1.IsBitten(this.BodyPartType) && var1.getBodyPart(this.BodyPartType).getBiteTime() >= 0.0F) {
         this.DrawTexture(this.biteTex, 0.0D, 0.0D, 1.0D);
      } else if (this.deepWoundTex != null && var1.IsDeepWounded(this.BodyPartType)) {
         this.DrawTexture(this.deepWoundTex, 0.0D, 0.0D, (double)(var1.getBodyPart(this.BodyPartType).getDeepWoundTime() / 15.0F));
      } else if (this.stitchTex != null && var1.IsStitched(this.BodyPartType)) {
         this.DrawTexture(this.stitchTex, 0.0D, 0.0D, 1.0D);
      }

      if (this.boneTex != null && var1.getBodyPart(this.BodyPartType).getFractureTime() > 0.0F && var1.getBodyPart(this.BodyPartType).getSplintFactor() == 0.0F) {
         this.DrawTexture(this.boneTex, 0.0D, 0.0D, 1.0D);
      } else if (this.splintTex != null && var1.getBodyPart(this.BodyPartType).getSplintFactor() > 0.0F) {
         this.DrawTexture(this.splintTex, 0.0D, 0.0D, 1.0D);
      }

      if (this.glassTex != null && var1.getBodyPart(this.BodyPartType).haveGlass() && !var1.getBodyPart(this.BodyPartType).bandaged()) {
         this.DrawTexture(this.glassTex, 0.0D, 0.0D, 1.0D);
      }

      if (this.bulletTex != null && var1.getBodyPart(this.BodyPartType).haveBullet() && !var1.getBodyPart(this.BodyPartType).bandaged()) {
         this.DrawTexture(this.bulletTex, 0.0D, 0.0D, 1.0D);
      }

      if (this.burnTex != null && var1.getBodyPart(this.BodyPartType).getBurnTime() > 0.0F && !var1.getBodyPart(this.BodyPartType).bandaged()) {
         this.DrawTexture(this.burnTex, 0.0D, 0.0D, (double)(var1.getBodyPart(this.BodyPartType).getBurnTime() / 100.0F));
      }

      super.render();
   }
}
