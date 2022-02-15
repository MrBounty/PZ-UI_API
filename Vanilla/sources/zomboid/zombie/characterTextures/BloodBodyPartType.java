package zombie.characterTextures;

import java.util.ArrayList;
import zombie.core.Translator;
import zombie.core.skinnedmodel.model.CharacterMask;

public enum BloodBodyPartType {
   Hand_L,
   Hand_R,
   ForeArm_L,
   ForeArm_R,
   UpperArm_L,
   UpperArm_R,
   Torso_Upper,
   Torso_Lower,
   Head,
   Neck,
   Groin,
   UpperLeg_L,
   UpperLeg_R,
   LowerLeg_L,
   LowerLeg_R,
   Foot_L,
   Foot_R,
   Back,
   MAX;

   private CharacterMask.Part[] m_characterMaskParts;

   public int index() {
      return ToIndex(this);
   }

   public static BloodBodyPartType FromIndex(int var0) {
      switch(var0) {
      case 0:
         return Hand_L;
      case 1:
         return Hand_R;
      case 2:
         return ForeArm_L;
      case 3:
         return ForeArm_R;
      case 4:
         return UpperArm_L;
      case 5:
         return UpperArm_R;
      case 6:
         return Torso_Upper;
      case 7:
         return Torso_Lower;
      case 8:
         return Head;
      case 9:
         return Neck;
      case 10:
         return Groin;
      case 11:
         return UpperLeg_L;
      case 12:
         return UpperLeg_R;
      case 13:
         return LowerLeg_L;
      case 14:
         return LowerLeg_R;
      case 15:
         return Foot_L;
      case 16:
         return Foot_R;
      case 17:
         return Back;
      default:
         return MAX;
      }
   }

   public static int ToIndex(BloodBodyPartType var0) {
      if (var0 == null) {
         return 0;
      } else {
         switch(var0) {
         case Hand_L:
            return 0;
         case Hand_R:
            return 1;
         case ForeArm_L:
            return 2;
         case ForeArm_R:
            return 3;
         case UpperArm_L:
            return 4;
         case UpperArm_R:
            return 5;
         case Torso_Upper:
            return 6;
         case Torso_Lower:
            return 7;
         case Head:
            return 8;
         case Neck:
            return 9;
         case Groin:
            return 10;
         case UpperLeg_L:
            return 11;
         case UpperLeg_R:
            return 12;
         case LowerLeg_L:
            return 13;
         case LowerLeg_R:
            return 14;
         case Foot_L:
            return 15;
         case Foot_R:
            return 16;
         case Back:
            return 17;
         case MAX:
            return 18;
         default:
            return 17;
         }
      }
   }

   public static BloodBodyPartType FromString(String var0) {
      if (var0.equals("Hand_L")) {
         return Hand_L;
      } else if (var0.equals("Hand_R")) {
         return Hand_R;
      } else if (var0.equals("ForeArm_L")) {
         return ForeArm_L;
      } else if (var0.equals("ForeArm_R")) {
         return ForeArm_R;
      } else if (var0.equals("UpperArm_L")) {
         return UpperArm_L;
      } else if (var0.equals("UpperArm_R")) {
         return UpperArm_R;
      } else if (var0.equals("Torso_Upper")) {
         return Torso_Upper;
      } else if (var0.equals("Torso_Lower")) {
         return Torso_Lower;
      } else if (var0.equals("Head")) {
         return Head;
      } else if (var0.equals("Neck")) {
         return Neck;
      } else if (var0.equals("Groin")) {
         return Groin;
      } else if (var0.equals("UpperLeg_L")) {
         return UpperLeg_L;
      } else if (var0.equals("UpperLeg_R")) {
         return UpperLeg_R;
      } else if (var0.equals("LowerLeg_L")) {
         return LowerLeg_L;
      } else if (var0.equals("LowerLeg_R")) {
         return LowerLeg_R;
      } else if (var0.equals("Foot_L")) {
         return Foot_L;
      } else if (var0.equals("Foot_R")) {
         return Foot_R;
      } else {
         return var0.equals("Back") ? Back : MAX;
      }
   }

   public CharacterMask.Part[] getCharacterMaskParts() {
      if (this.m_characterMaskParts != null) {
         return this.m_characterMaskParts;
      } else {
         ArrayList var1 = new ArrayList();
         switch(this) {
         case Hand_L:
            var1.add(CharacterMask.Part.LeftHand);
            break;
         case Hand_R:
            var1.add(CharacterMask.Part.RightHand);
            break;
         case ForeArm_L:
            var1.add(CharacterMask.Part.LeftArm);
            break;
         case ForeArm_R:
            var1.add(CharacterMask.Part.RightArm);
            break;
         case UpperArm_L:
            var1.add(CharacterMask.Part.LeftArm);
            break;
         case UpperArm_R:
            var1.add(CharacterMask.Part.RightArm);
            break;
         case Torso_Upper:
            var1.add(CharacterMask.Part.Chest);
            break;
         case Torso_Lower:
            var1.add(CharacterMask.Part.Waist);
            break;
         case Head:
            var1.add(CharacterMask.Part.Head);
            break;
         case Neck:
            var1.add(CharacterMask.Part.Head);
            break;
         case Groin:
            var1.add(CharacterMask.Part.Crotch);
            break;
         case UpperLeg_L:
            var1.add(CharacterMask.Part.LeftLeg);
            var1.add(CharacterMask.Part.Pelvis);
            break;
         case UpperLeg_R:
            var1.add(CharacterMask.Part.RightLeg);
            var1.add(CharacterMask.Part.Pelvis);
            break;
         case LowerLeg_L:
            var1.add(CharacterMask.Part.LeftLeg);
            break;
         case LowerLeg_R:
            var1.add(CharacterMask.Part.RightLeg);
            break;
         case Foot_L:
            var1.add(CharacterMask.Part.LeftFoot);
            break;
         case Foot_R:
            var1.add(CharacterMask.Part.RightFoot);
            break;
         case Back:
            var1.add(CharacterMask.Part.Torso);
         }

         this.m_characterMaskParts = new CharacterMask.Part[var1.size()];

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            this.m_characterMaskParts[var2] = (CharacterMask.Part)var1.get(var2);
         }

         return this.m_characterMaskParts;
      }
   }

   public String getDisplayName() {
      return getDisplayName(this);
   }

   public static String getDisplayName(BloodBodyPartType var0) {
      if (var0 == Hand_L) {
         return Translator.getText("IGUI_health_Left_Hand");
      } else if (var0 == Hand_R) {
         return Translator.getText("IGUI_health_Right_Hand");
      } else if (var0 == ForeArm_L) {
         return Translator.getText("IGUI_health_Left_Forearm");
      } else if (var0 == ForeArm_R) {
         return Translator.getText("IGUI_health_Right_Forearm");
      } else if (var0 == UpperArm_L) {
         return Translator.getText("IGUI_health_Left_Upper_Arm");
      } else if (var0 == UpperArm_R) {
         return Translator.getText("IGUI_health_Right_Upper_Arm");
      } else if (var0 == Torso_Upper) {
         return Translator.getText("IGUI_health_Upper_Torso");
      } else if (var0 == Torso_Lower) {
         return Translator.getText("IGUI_health_Lower_Torso");
      } else if (var0 == Head) {
         return Translator.getText("IGUI_health_Head");
      } else if (var0 == Neck) {
         return Translator.getText("IGUI_health_Neck");
      } else if (var0 == Groin) {
         return Translator.getText("IGUI_health_Groin");
      } else if (var0 == UpperLeg_L) {
         return Translator.getText("IGUI_health_Left_Thigh");
      } else if (var0 == UpperLeg_R) {
         return Translator.getText("IGUI_health_Right_Thigh");
      } else if (var0 == LowerLeg_L) {
         return Translator.getText("IGUI_health_Left_Shin");
      } else if (var0 == LowerLeg_R) {
         return Translator.getText("IGUI_health_Right_Shin");
      } else if (var0 == Foot_L) {
         return Translator.getText("IGUI_health_Left_Foot");
      } else if (var0 == Foot_R) {
         return Translator.getText("IGUI_health_Right_Foot");
      } else {
         return var0 == Back ? Translator.getText("IGUI_health_Back") : Translator.getText("IGUI_health_Unknown_Body_Part");
      }
   }

   // $FF: synthetic method
   private static BloodBodyPartType[] $values() {
      return new BloodBodyPartType[]{Hand_L, Hand_R, ForeArm_L, ForeArm_R, UpperArm_L, UpperArm_R, Torso_Upper, Torso_Lower, Head, Neck, Groin, UpperLeg_L, UpperLeg_R, LowerLeg_L, LowerLeg_R, Foot_L, Foot_R, Back, MAX};
   }
}
