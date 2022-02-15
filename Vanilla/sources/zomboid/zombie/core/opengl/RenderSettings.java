package zombie.core.opengl;

import java.util.function.Consumer;
import zombie.GameTime;
import zombie.IndieGL;
import zombie.SandboxOptions;
import zombie.characters.IsoPlayer;
import zombie.core.Color;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.SearchMode;
import zombie.iso.weather.ClimateColorInfo;
import zombie.iso.weather.ClimateManager;
import zombie.iso.weather.ClimateMoon;
import zombie.iso.weather.WorldFlares;
import zombie.network.GameServer;

public final class RenderSettings {
   private static RenderSettings instance;
   private static Texture texture;
   private static final float AMBIENT_MIN_SHADER = 0.4F;
   private static final float AMBIENT_MAX_SHADER = 1.0F;
   private static final float AMBIENT_MIN_LEGACY = 0.4F;
   private static final float AMBIENT_MAX_LEGACY = 1.0F;
   private final RenderSettings.PlayerRenderSettings[] playerSettings = new RenderSettings.PlayerRenderSettings[4];
   private Color defaultClear = new Color(0, 0, 0, 1);

   public static RenderSettings getInstance() {
      if (instance == null) {
         instance = new RenderSettings();
      }

      return instance;
   }

   public RenderSettings() {
      for(int var1 = 0; var1 < this.playerSettings.length; ++var1) {
         this.playerSettings[var1] = new RenderSettings.PlayerRenderSettings();
      }

      texture = Texture.getSharedTexture("media/textures/weather/fogwhite.png");
      if (texture == null) {
         DebugLog.log("Missing texture: media/textures/weather/fogwhite.png");
      }

   }

   public RenderSettings.PlayerRenderSettings getPlayerSettings(int var1) {
      return this.playerSettings[var1];
   }

   public void update() {
      if (!GameServer.bServer) {
         for(int var1 = 0; var1 < 4; ++var1) {
            if (IsoPlayer.players[var1] != null) {
               this.playerSettings[var1].updateRenderSettings(var1, IsoPlayer.players[var1]);
            }
         }

      }
   }

   public void applyRenderSettings(int var1) {
      if (!GameServer.bServer) {
         this.getPlayerSettings(var1).applyRenderSettings(var1);
      }
   }

   public void legacyPostRender(int var1) {
      if (!GameServer.bServer) {
         if (Core.getInstance().RenderShader == null || Core.getInstance().getOffscreenBuffer() == null) {
            this.getPlayerSettings(var1).legacyPostRender(var1);
         }

      }
   }

   public float getAmbientForPlayer(int var1) {
      RenderSettings.PlayerRenderSettings var2 = this.getPlayerSettings(var1);
      return var2 != null ? var2.getAmbient() : 0.0F;
   }

   public Color getMaskClearColorForPlayer(int var1) {
      RenderSettings.PlayerRenderSettings var2 = this.getPlayerSettings(var1);
      return var2 != null ? var2.getMaskClearColor() : this.defaultClear;
   }

   public static class PlayerRenderSettings {
      public ClimateColorInfo CM_GlobalLight = new ClimateColorInfo();
      public float CM_NightStrength = 0.0F;
      public float CM_Desaturation = 0.0F;
      public float CM_GlobalLightIntensity = 0.0F;
      public float CM_Ambient = 0.0F;
      public float CM_ViewDistance = 0.0F;
      public float CM_DayLightStrength = 0.0F;
      public float CM_FogIntensity = 0.0F;
      private Color blendColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
      private ColorInfo blendInfo = new ColorInfo();
      private float blendIntensity = 0.0F;
      private float desaturation = 0.0F;
      private float darkness = 0.0F;
      private float night = 0.0F;
      private float viewDistance = 0.0F;
      private float ambient = 0.0F;
      private boolean applyNightVisionGoggles = false;
      private float goggleMod = 0.0F;
      private boolean isExterior = false;
      private float fogMod = 1.0F;
      private float rmod;
      private float gmod;
      private float bmod;
      private float SM_Radius = 0.0F;
      private float SM_Alpha = 0.0F;
      private Color maskClearColor = new Color(0, 0, 0, 1);

      private void updateRenderSettings(int var1, IsoPlayer var2) {
         SearchMode var3 = SearchMode.getInstance();
         this.SM_Alpha = 0.0F;
         this.SM_Radius = 0.0F;
         ClimateManager var4 = ClimateManager.getInstance();
         this.CM_GlobalLight = var4.getGlobalLight();
         this.CM_GlobalLightIntensity = var4.getGlobalLightIntensity();
         this.CM_Ambient = var4.getAmbient();
         this.CM_DayLightStrength = var4.getDayLightStrength();
         this.CM_NightStrength = var4.getNightStrength();
         this.CM_Desaturation = var4.getDesaturation();
         this.CM_ViewDistance = var4.getViewDistance();
         this.CM_FogIntensity = var4.getFogIntensity();
         var4.getThunderStorm().applyLightningForPlayer(this, var1, var2);
         WorldFlares.applyFlaresForPlayer(this, var1, var2);
         int var5 = SandboxOptions.instance.NightDarkness.getValue();
         this.desaturation = this.CM_Desaturation;
         this.viewDistance = this.CM_ViewDistance;
         this.applyNightVisionGoggles = var2 != null && var2.isWearingNightVisionGoggles();
         this.isExterior = var2 != null && var2.getCurrentSquare() != null && !var2.getCurrentSquare().isInARoom();
         this.fogMod = 1.0F - this.CM_FogIntensity * 0.5F;
         this.night = this.CM_NightStrength;
         this.darkness = 1.0F - this.CM_DayLightStrength;
         if (this.isExterior) {
            this.setBlendColor(this.CM_GlobalLight.getExterior());
            this.blendIntensity = this.CM_GlobalLight.getExterior().a;
         } else {
            this.setBlendColor(this.CM_GlobalLight.getInterior());
            this.blendIntensity = this.CM_GlobalLight.getInterior().a;
         }

         this.ambient = this.CM_Ambient;
         this.viewDistance = this.CM_ViewDistance;
         --var5;
         float var6 = 0.2F + 0.1F * (float)var5;
         var6 += 0.075F * ClimateMoon.getMoonFloat() * this.night;
         if (!this.isExterior) {
            var6 *= 0.925F - 0.075F * this.darkness;
            this.desaturation *= 0.25F;
         }

         if (this.ambient < 0.2F && var2.getCharacterTraits().NightVision.isSet()) {
            this.ambient = 0.2F;
         }

         this.ambient = var6 + (1.0F - var6) * this.ambient;
         if (Core.bLastStand) {
            this.ambient = 0.65F;
            this.darkness = 0.25F;
            this.night = 0.25F;
         }

         if (DebugOptions.instance.MultiplayerLightAmbient.getValue()) {
            this.ambient = 0.99F;
            this.darkness = 0.01F;
            this.night = 0.01F;
         }

         if (Core.getInstance().RenderShader != null && Core.getInstance().getOffscreenBuffer() != null) {
            if (this.applyNightVisionGoggles) {
               this.ambient = 1.0F;
               this.rmod = GameTime.getInstance().Lerp(1.0F, 0.7F, this.darkness);
               this.gmod = GameTime.getInstance().Lerp(1.0F, 0.7F, this.darkness);
               this.bmod = GameTime.getInstance().Lerp(1.0F, 0.7F, this.darkness);
               this.maskClearColor.r = 0.0F;
               this.maskClearColor.g = 0.0F;
               this.maskClearColor.b = 0.0F;
               this.maskClearColor.a = 0.0F;
            } else {
               this.rmod = 1.0F;
               this.gmod = 1.0F;
               this.bmod = 1.0F;
               if (!this.isExterior) {
                  this.maskClearColor.r = this.CM_GlobalLight.getInterior().r;
                  this.maskClearColor.g = this.CM_GlobalLight.getInterior().g;
                  this.maskClearColor.b = this.CM_GlobalLight.getInterior().b;
                  this.maskClearColor.a = this.CM_GlobalLight.getInterior().a;
               } else {
                  this.maskClearColor.r = 0.0F;
                  this.maskClearColor.g = 0.0F;
                  this.maskClearColor.b = 0.0F;
                  this.maskClearColor.a = 0.0F;
               }
            }
         } else {
            this.desaturation *= 1.0F - this.darkness;
            this.blendInfo.r = this.blendColor.r;
            this.blendInfo.g = this.blendColor.g;
            this.blendInfo.b = this.blendColor.b;
            this.blendInfo.desaturate(this.desaturation);
            this.rmod = GameTime.getInstance().Lerp(1.0F, this.blendInfo.r, this.blendIntensity);
            this.gmod = GameTime.getInstance().Lerp(1.0F, this.blendInfo.g, this.blendIntensity);
            this.bmod = GameTime.getInstance().Lerp(1.0F, this.blendInfo.b, this.blendIntensity);
            if (this.applyNightVisionGoggles) {
               this.goggleMod = 1.0F - 0.9F * this.darkness;
               this.blendIntensity = 0.0F;
               this.night = 0.0F;
               this.ambient = 0.8F;
               this.rmod = 1.0F;
               this.gmod = 1.0F;
               this.bmod = 1.0F;
            }
         }

      }

      private void applyRenderSettings(int var1) {
         IsoGridSquare.rmod = this.rmod;
         IsoGridSquare.gmod = this.gmod;
         IsoGridSquare.bmod = this.bmod;
         IsoObject.rmod = this.rmod;
         IsoObject.gmod = this.gmod;
         IsoObject.bmod = this.bmod;
      }

      private void legacyPostRender(int var1) {
         SpriteRenderer.instance.glIgnoreStyles(true);
         if (this.applyNightVisionGoggles) {
            IndieGL.glBlendFunc(770, 768);
            SpriteRenderer.instance.render(RenderSettings.texture, 0.0F, 0.0F, (float)Core.getInstance().getOffscreenWidth(var1), (float)Core.getInstance().getOffscreenHeight(var1), 0.05F, 0.95F, 0.05F, this.goggleMod, (Consumer)null);
            IndieGL.glBlendFunc(770, 771);
         } else {
            IndieGL.glBlendFunc(774, 774);
            SpriteRenderer.instance.render(RenderSettings.texture, 0.0F, 0.0F, (float)Core.getInstance().getOffscreenWidth(var1), (float)Core.getInstance().getOffscreenHeight(var1), this.blendInfo.r, this.blendInfo.g, this.blendInfo.b, 1.0F, (Consumer)null);
            IndieGL.glBlendFunc(770, 771);
         }

         SpriteRenderer.instance.glIgnoreStyles(false);
      }

      public Color getBlendColor() {
         return this.blendColor;
      }

      public float getBlendIntensity() {
         return this.blendIntensity;
      }

      public float getDesaturation() {
         return this.desaturation;
      }

      public float getDarkness() {
         return this.darkness;
      }

      public float getNight() {
         return this.night;
      }

      public float getViewDistance() {
         return this.viewDistance;
      }

      public float getAmbient() {
         return this.ambient;
      }

      public boolean isApplyNightVisionGoggles() {
         return this.applyNightVisionGoggles;
      }

      public float getRmod() {
         return this.rmod;
      }

      public float getGmod() {
         return this.gmod;
      }

      public float getBmod() {
         return this.bmod;
      }

      public boolean isExterior() {
         return this.isExterior;
      }

      public float getFogMod() {
         return this.fogMod;
      }

      private void setBlendColor(Color var1) {
         this.blendColor.a = var1.a;
         this.blendColor.r = var1.r;
         this.blendColor.g = var1.g;
         this.blendColor.b = var1.b;
      }

      public Color getMaskClearColor() {
         return this.maskClearColor;
      }

      public float getSM_Radius() {
         return this.SM_Radius;
      }

      public float getSM_Alpha() {
         return this.SM_Alpha;
      }
   }
}
