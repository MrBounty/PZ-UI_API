package zombie.debug.options;

import zombie.debug.BooleanDebugOption;

public class Character extends OptionGroup {
   public final BooleanDebugOption CreateAllOutfits;
   public final Character.DebugOG Debug;

   public Character() {
      super("Character");
      this.CreateAllOutfits = newOption(this.Group, "Create.AllOutfits", false);
      this.Debug = new Character.DebugOG(this.Group);
   }

   public static final class DebugOG extends OptionGroup {
      public final Character.DebugOG.RenderOG Render;
      public final Character.DebugOG.AnimateOG Animate;
      public final BooleanDebugOption RegisterDebugVariables;
      public final BooleanDebugOption AlwaysTripOverFence;
      public final BooleanDebugOption PlaySoundWhenInvisible;
      public final BooleanDebugOption UpdateAlpha;
      public final BooleanDebugOption UpdateAlphaEighthSpeed;

      public DebugOG(IDebugOptionGroup var1) {
         super(var1, "Debug");
         this.Render = new Character.DebugOG.RenderOG(this.Group);
         this.Animate = new Character.DebugOG.AnimateOG(this.Group);
         this.RegisterDebugVariables = newDebugOnlyOption(this.Group, "DebugVariables", false);
         this.AlwaysTripOverFence = newDebugOnlyOption(this.Group, "AlwaysTripOverFence", false);
         this.PlaySoundWhenInvisible = newDebugOnlyOption(this.Group, "PlaySoundWhenInvisible", true);
         this.UpdateAlpha = newDebugOnlyOption(this.Group, "UpdateAlpha", true);
         this.UpdateAlphaEighthSpeed = newDebugOnlyOption(this.Group, "UpdateAlphaEighthSpeed", false);
      }

      public static final class RenderOG extends OptionGroup {
         public final BooleanDebugOption AimCone;
         public final BooleanDebugOption Angle;
         public final BooleanDebugOption TestDotSide;
         public final BooleanDebugOption DeferredMovement;
         public final BooleanDebugOption DeferredAngles;
         public final BooleanDebugOption TranslationData;
         public final BooleanDebugOption Bip01;
         public final BooleanDebugOption PrimaryHandBone;
         public final BooleanDebugOption SecondaryHandBone;
         public final BooleanDebugOption SkipCharacters;
         public final BooleanDebugOption Vision;
         public final BooleanDebugOption DisplayRoomAndZombiesZone;

         public RenderOG(IDebugOptionGroup var1) {
            super(var1, "Render");
            this.AimCone = newDebugOnlyOption(this.Group, "AimCone", false);
            this.Angle = newDebugOnlyOption(this.Group, "Angle", false);
            this.TestDotSide = newDebugOnlyOption(this.Group, "TestDotSide", false);
            this.DeferredMovement = newDebugOnlyOption(this.Group, "DeferredMovement", false);
            this.DeferredAngles = newDebugOnlyOption(this.Group, "DeferredRotation", false);
            this.TranslationData = newDebugOnlyOption(this.Group, "Translation_Data", false);
            this.Bip01 = newDebugOnlyOption(this.Group, "Bip01", false);
            this.PrimaryHandBone = newDebugOnlyOption(this.Group, "HandBones.Primary", false);
            this.SecondaryHandBone = newDebugOnlyOption(this.Group, "HandBones.Secondary", false);
            this.SkipCharacters = newDebugOnlyOption(this.Group, "SkipCharacters", false);
            this.Vision = newDebugOnlyOption(this.Group, "Vision", false);
            this.DisplayRoomAndZombiesZone = newDebugOnlyOption(this.Group, "DisplayRoomAndZombiesZone", false);
         }
      }

      public static final class AnimateOG extends OptionGroup {
         public final BooleanDebugOption DeferredRotationOnly;
         public final BooleanDebugOption NoBoneMasks;
         public final BooleanDebugOption NoBoneTwists;
         public final BooleanDebugOption ZeroCounterRotationBone;

         public AnimateOG(IDebugOptionGroup var1) {
            super(var1, "Animate");
            this.DeferredRotationOnly = newDebugOnlyOption(this.Group, "DeferredRotationsOnly", false);
            this.NoBoneMasks = newDebugOnlyOption(this.Group, "NoBoneMasks", false);
            this.NoBoneTwists = newDebugOnlyOption(this.Group, "NoBoneTwists", false);
            this.ZeroCounterRotationBone = newDebugOnlyOption(this.Group, "ZeroCounterRotation", false);
         }
      }
   }
}
