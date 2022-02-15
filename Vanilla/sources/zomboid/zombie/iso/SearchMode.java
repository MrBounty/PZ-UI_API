package zombie.iso;

import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.core.math.PZMath;

public class SearchMode {
   private static SearchMode instance;
   private float fadeTime = 1.0F;
   private SearchMode.PlayerSearchMode[] plrModes = new SearchMode.PlayerSearchMode[4];

   public static SearchMode getInstance() {
      if (instance == null) {
         instance = new SearchMode();
      }

      return instance;
   }

   private SearchMode() {
      for(int var1 = 0; var1 < this.plrModes.length; ++var1) {
         this.plrModes[var1] = new SearchMode.PlayerSearchMode(var1, this);
         this.plrModes[var1].blur.setTargets(1.0F, 1.0F);
         this.plrModes[var1].desat.setTargets(0.85F, 0.85F);
         this.plrModes[var1].radius.setTargets(4.0F, 4.0F);
         this.plrModes[var1].darkness.setTargets(0.0F, 0.0F);
         this.plrModes[var1].gradientWidth.setTargets(4.0F, 4.0F);
      }

   }

   public SearchMode.PlayerSearchMode getSearchModeForPlayer(int var1) {
      return this.plrModes[var1];
   }

   public float getFadeTime() {
      return this.fadeTime;
   }

   public void setFadeTime(float var1) {
      this.fadeTime = var1;
   }

   public boolean isOverride(int var1) {
      return this.plrModes[var1].override;
   }

   public void setOverride(int var1, boolean var2) {
      this.plrModes[var1].override = var2;
   }

   public SearchMode.SearchModeFloat getRadius(int var1) {
      return this.plrModes[var1].radius;
   }

   public SearchMode.SearchModeFloat getGradientWidth(int var1) {
      return this.plrModes[var1].gradientWidth;
   }

   public SearchMode.SearchModeFloat getBlur(int var1) {
      return this.plrModes[var1].blur;
   }

   public SearchMode.SearchModeFloat getDesat(int var1) {
      return this.plrModes[var1].desat;
   }

   public SearchMode.SearchModeFloat getDarkness(int var1) {
      return this.plrModes[var1].darkness;
   }

   public boolean isEnabled(int var1) {
      return this.plrModes[var1].enabled;
   }

   public void setEnabled(int var1, boolean var2) {
      SearchMode.PlayerSearchMode var3 = this.plrModes[var1];
      if (var2 && !var3.enabled) {
         var3.enabled = true;
         this.FadeIn(var1);
      } else if (!var2 && var3.enabled) {
         var3.enabled = false;
         this.FadeOut(var1);
      }

   }

   private void FadeIn(int var1) {
      SearchMode.PlayerSearchMode var2 = this.plrModes[var1];
      var2.timer = Math.max(var2.timer, 0.0F);
      var2.doFadeIn = true;
      var2.doFadeOut = false;
   }

   private void FadeOut(int var1) {
      SearchMode.PlayerSearchMode var2 = this.plrModes[var1];
      var2.timer = Math.min(var2.timer, this.fadeTime);
      var2.doFadeIn = false;
      var2.doFadeOut = true;
   }

   public void update() {
      for(int var1 = 0; var1 < this.plrModes.length; ++var1) {
         SearchMode.PlayerSearchMode var2 = this.plrModes[var1];
         var2.update();
      }

   }

   public static void reset() {
      instance = null;
   }

   public static class PlayerSearchMode {
      private final int plrIndex;
      private final SearchMode parent;
      private boolean override = false;
      private boolean enabled = false;
      private final SearchMode.SearchModeFloat radius = new SearchMode.SearchModeFloat(0.0F, 50.0F, 1.0F);
      private final SearchMode.SearchModeFloat gradientWidth = new SearchMode.SearchModeFloat(0.0F, 20.0F, 1.0F);
      private final SearchMode.SearchModeFloat blur = new SearchMode.SearchModeFloat(0.0F, 1.0F, 0.01F);
      private final SearchMode.SearchModeFloat desat = new SearchMode.SearchModeFloat(0.0F, 1.0F, 0.01F);
      private final SearchMode.SearchModeFloat darkness = new SearchMode.SearchModeFloat(0.0F, 1.0F, 0.01F);
      private float timer;
      private boolean doFadeOut;
      private boolean doFadeIn;

      public PlayerSearchMode(int var1, SearchMode var2) {
         this.plrIndex = var1;
         this.parent = var2;
      }

      public boolean isShaderEnabled() {
         return this.enabled || this.doFadeIn || this.doFadeOut;
      }

      private boolean isPlayerExterior() {
         IsoPlayer var1 = IsoPlayer.players[this.plrIndex];
         return var1 != null && var1.getCurrentSquare() != null && !var1.getCurrentSquare().isInARoom();
      }

      public float getShaderBlur() {
         return this.isPlayerExterior() ? this.blur.getExterior() : this.blur.getInterior();
      }

      public float getShaderDesat() {
         return this.isPlayerExterior() ? this.desat.getExterior() : this.desat.getInterior();
      }

      public float getShaderRadius() {
         return this.isPlayerExterior() ? this.radius.getExterior() : this.radius.getInterior();
      }

      public float getShaderGradientWidth() {
         return this.isPlayerExterior() ? this.gradientWidth.getExterior() : this.gradientWidth.getInterior();
      }

      public float getShaderDarkness() {
         return this.isPlayerExterior() ? this.darkness.getExterior() : this.darkness.getInterior();
      }

      public SearchMode.SearchModeFloat getBlur() {
         return this.blur;
      }

      public SearchMode.SearchModeFloat getDesat() {
         return this.desat;
      }

      public SearchMode.SearchModeFloat getRadius() {
         return this.radius;
      }

      public SearchMode.SearchModeFloat getGradientWidth() {
         return this.gradientWidth;
      }

      public SearchMode.SearchModeFloat getDarkness() {
         return this.darkness;
      }

      private void update() {
         if (!this.override) {
            float var1;
            if (this.doFadeIn) {
               this.timer += GameTime.getInstance().getTimeDelta();
               this.timer = PZMath.clamp(this.timer, 0.0F, this.parent.fadeTime);
               var1 = PZMath.clamp(this.timer / this.parent.fadeTime, 0.0F, 1.0F);
               this.blur.update(var1);
               this.desat.update(var1);
               this.radius.update(var1);
               this.darkness.update(var1);
               this.gradientWidth.equalise();
               if (this.timer >= this.parent.fadeTime) {
                  this.doFadeIn = false;
               }

            } else if (this.doFadeOut) {
               this.timer -= GameTime.getInstance().getTimeDelta();
               this.timer = PZMath.clamp(this.timer, 0.0F, this.parent.fadeTime);
               var1 = PZMath.clamp(this.timer / this.parent.fadeTime, 0.0F, 1.0F);
               this.blur.update(var1);
               this.desat.update(var1);
               this.radius.update(var1);
               this.darkness.update(var1);
               this.gradientWidth.equalise();
               if (this.timer <= 0.0F) {
                  this.doFadeOut = false;
               }

            } else {
               if (this.enabled) {
                  this.blur.equalise();
                  this.desat.equalise();
                  this.radius.equalise();
                  this.darkness.equalise();
                  this.gradientWidth.equalise();
               } else {
                  this.blur.reset();
                  this.desat.reset();
                  this.radius.reset();
                  this.darkness.reset();
                  this.gradientWidth.equalise();
               }

            }
         }
      }
   }

   public static class SearchModeFloat {
      private final float min;
      private final float max;
      private final float stepsize;
      private float exterior;
      private float targetExterior;
      private float interior;
      private float targetInterior;

      private SearchModeFloat(float var1, float var2, float var3) {
         this.min = var1;
         this.max = var2;
         this.stepsize = var3;
      }

      public void set(float var1, float var2, float var3, float var4) {
         this.setExterior(var1);
         this.setTargetExterior(var2);
         this.setInterior(var3);
         this.setTargetInterior(var4);
      }

      public void setTargets(float var1, float var2) {
         this.setTargetExterior(var1);
         this.setTargetInterior(var2);
      }

      public float getExterior() {
         return this.exterior;
      }

      public void setExterior(float var1) {
         this.exterior = var1;
      }

      public float getTargetExterior() {
         return this.targetExterior;
      }

      public void setTargetExterior(float var1) {
         this.targetExterior = var1;
      }

      public float getInterior() {
         return this.interior;
      }

      public void setInterior(float var1) {
         this.interior = var1;
      }

      public float getTargetInterior() {
         return this.targetInterior;
      }

      public void setTargetInterior(float var1) {
         this.targetInterior = var1;
      }

      public void update(float var1) {
         this.exterior = var1 * this.targetExterior;
         this.interior = var1 * this.targetInterior;
      }

      public void equalise() {
         if (!PZMath.equal(this.exterior, this.targetExterior, 0.001F)) {
            this.exterior = PZMath.lerp(this.exterior, this.targetExterior, 0.01F);
         } else {
            this.exterior = this.targetExterior;
         }

         if (!PZMath.equal(this.interior, this.targetInterior, 0.001F)) {
            this.interior = PZMath.lerp(this.interior, this.targetInterior, 0.01F);
         } else {
            this.interior = this.targetInterior;
         }

      }

      public void reset() {
         this.exterior = 0.0F;
         this.interior = 0.0F;
      }

      public float getMin() {
         return this.min;
      }

      public float getMax() {
         return this.max;
      }

      public float getStepsize() {
         return this.stepsize;
      }
   }
}
