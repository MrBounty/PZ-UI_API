package zombie.core.skinnedmodel.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import zombie.characterTextures.BloodBodyPartType;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.util.Pool;

public final class CharacterMask {
   private final boolean[] m_visibleFlags = createFlags(CharacterMask.Part.values().length, true);

   public boolean isBloodBodyPartVisible(BloodBodyPartType var1) {
      CharacterMask.Part[] var2 = var1.getCharacterMaskParts();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         CharacterMask.Part var5 = var2[var4];
         if (this.isPartVisible(var5)) {
            return true;
         }
      }

      return false;
   }

   private static boolean[] createFlags(int var0, boolean var1) {
      boolean[] var2 = new boolean[var0];

      for(int var3 = 0; var3 < var0; ++var3) {
         var2[var3] = var1;
      }

      return var2;
   }

   public void setAllVisible(boolean var1) {
      Arrays.fill(this.m_visibleFlags, var1);
   }

   public void copyFrom(CharacterMask var1) {
      System.arraycopy(var1.m_visibleFlags, 0, this.m_visibleFlags, 0, this.m_visibleFlags.length);
   }

   public void setPartVisible(CharacterMask.Part var1, boolean var2) {
      if (var1.hasSubdivisions()) {
         CharacterMask.Part[] var3 = var1.subDivisions();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            CharacterMask.Part var6 = var3[var5];
            this.setPartVisible(var6, var2);
         }
      } else {
         this.m_visibleFlags[var1.getValue()] = var2;
      }

   }

   public void setPartsVisible(ArrayList var1, boolean var2) {
      for(int var3 = 0; var3 < var1.size(); ++var3) {
         int var4 = (Integer)var1.get(var3);
         CharacterMask.Part var5 = CharacterMask.Part.fromInt(var4);
         if (var5 == null) {
            if (DebugLog.isEnabled(DebugType.Clothing)) {
               DebugLog.Clothing.warn("MaskValue out of bounds: " + var4);
            }
         } else {
            this.setPartVisible(var5, var2);
         }
      }

   }

   public boolean isPartVisible(CharacterMask.Part var1) {
      if (var1 == null) {
         return false;
      } else if (!var1.hasSubdivisions()) {
         return this.m_visibleFlags[var1.getValue()];
      } else {
         boolean var2 = true;

         for(int var3 = 0; var2 && var3 < var1.subDivisions().length; ++var3) {
            CharacterMask.Part var4 = var1.subDivisions()[var3];
            var2 = this.m_visibleFlags[var4.getValue()];
         }

         return var2;
      }
   }

   public boolean isTorsoVisible() {
      return this.isPartVisible(CharacterMask.Part.Torso);
   }

   public String toString() {
      String var10000 = this.getClass().getSimpleName();
      return var10000 + "{VisibleFlags:(" + this.contentsToString() + ")}";
   }

   public String contentsToString() {
      if (this.isAllVisible()) {
         return "All Visible";
      } else if (this.isNothingVisible()) {
         return "Nothing Visible";
      } else {
         StringBuilder var1 = new StringBuilder();
         int var2 = 0;

         for(int var3 = 0; var2 < CharacterMask.Part.leaves().length; ++var2) {
            CharacterMask.Part var4 = CharacterMask.Part.leaves()[var2];
            if (this.isPartVisible(var4)) {
               if (var3 > 0) {
                  var1.append(',');
               }

               var1.append(var4);
               ++var3;
            }
         }

         return var1.toString();
      }
   }

   private boolean isAll(boolean var1) {
      boolean var2 = true;
      int var3 = 0;

      for(int var4 = CharacterMask.Part.leaves().length; var2 && var3 < var4; ++var3) {
         CharacterMask.Part var5 = CharacterMask.Part.leaves()[var3];
         var2 = this.isPartVisible(var5) == var1;
      }

      return var2;
   }

   public boolean isNothingVisible() {
      return this.isAll(false);
   }

   public boolean isAllVisible() {
      return this.isAll(true);
   }

   public void forEachVisible(Consumer var1) {
      try {
         for(int var2 = 0; var2 < CharacterMask.Part.leaves().length; ++var2) {
            CharacterMask.Part var3 = CharacterMask.Part.leaves()[var2];
            if (this.isPartVisible(var3)) {
               var1.accept(var3);
            }
         }
      } finally {
         Pool.tryRelease((Object)var1);
      }

   }

   public static enum Part {
      Head(0),
      Torso(1, true),
      Pelvis(2, true),
      LeftArm(3),
      LeftHand(4),
      RightArm(5),
      RightHand(6),
      LeftLeg(7),
      LeftFoot(8),
      RightLeg(9),
      RightFoot(10),
      Dress(11),
      Chest(12, Torso),
      Waist(13, Torso),
      Belt(14, Pelvis),
      Crotch(15, Pelvis);

      private final int value;
      private final CharacterMask.Part parent;
      private final boolean isSubdivided;
      private CharacterMask.Part[] subDivisions;
      private BloodBodyPartType[] m_bloodBodyPartTypes;
      private static final CharacterMask.Part[] s_leaves = leavesInternal();

      private Part(int var3) {
         this.value = var3;
         this.parent = null;
         this.isSubdivided = false;
      }

      private Part(int var3, CharacterMask.Part var4) {
         this.value = var3;
         this.parent = var4;
         this.isSubdivided = false;
      }

      private Part(int var3, boolean var4) {
         this.value = var3;
         this.parent = null;
         this.isSubdivided = var4;
      }

      public static int count() {
         return values().length;
      }

      public static CharacterMask.Part[] leaves() {
         return s_leaves;
      }

      public static CharacterMask.Part fromInt(int var0) {
         return var0 >= 0 && var0 < count() ? values()[var0] : null;
      }

      public int getValue() {
         return this.value;
      }

      public CharacterMask.Part getParent() {
         return this.parent;
      }

      public boolean isSubdivision() {
         return this.parent != null;
      }

      public boolean hasSubdivisions() {
         return this.isSubdivided;
      }

      public CharacterMask.Part[] subDivisions() {
         if (this.subDivisions != null) {
            return this.subDivisions;
         } else {
            if (!this.isSubdivided) {
               this.subDivisions = new CharacterMask.Part[0];
            }

            ArrayList var1 = new ArrayList();
            CharacterMask.Part[] var2 = values();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               CharacterMask.Part var5 = var2[var4];
               if (var5.parent == this) {
                  var1.add(var5);
               }
            }

            this.subDivisions = (CharacterMask.Part[])var1.toArray(new CharacterMask.Part[0]);
            return this.subDivisions;
         }
      }

      private static CharacterMask.Part[] leavesInternal() {
         ArrayList var0 = new ArrayList();
         CharacterMask.Part[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            CharacterMask.Part var4 = var1[var3];
            if (!var4.hasSubdivisions()) {
               var0.add(var4);
            }
         }

         return (CharacterMask.Part[])var0.toArray(new CharacterMask.Part[0]);
      }

      public BloodBodyPartType[] getBloodBodyPartTypes() {
         if (this.m_bloodBodyPartTypes != null) {
            return this.m_bloodBodyPartTypes;
         } else {
            ArrayList var1 = new ArrayList();
            switch(this) {
            case Head:
               var1.add(BloodBodyPartType.Head);
               break;
            case Torso:
               var1.add(BloodBodyPartType.Torso_Upper);
               var1.add(BloodBodyPartType.Torso_Lower);
               break;
            case Pelvis:
               var1.add(BloodBodyPartType.UpperLeg_L);
               var1.add(BloodBodyPartType.UpperLeg_R);
               var1.add(BloodBodyPartType.Groin);
               break;
            case LeftArm:
               var1.add(BloodBodyPartType.UpperArm_L);
               var1.add(BloodBodyPartType.ForeArm_L);
               break;
            case LeftHand:
               var1.add(BloodBodyPartType.Hand_L);
               break;
            case RightArm:
               var1.add(BloodBodyPartType.UpperArm_R);
               var1.add(BloodBodyPartType.ForeArm_R);
               break;
            case RightHand:
               var1.add(BloodBodyPartType.Hand_R);
               break;
            case LeftLeg:
               var1.add(BloodBodyPartType.UpperLeg_L);
               var1.add(BloodBodyPartType.LowerLeg_L);
               break;
            case LeftFoot:
               var1.add(BloodBodyPartType.Foot_L);
               break;
            case RightLeg:
               var1.add(BloodBodyPartType.UpperLeg_R);
               var1.add(BloodBodyPartType.LowerLeg_R);
               break;
            case RightFoot:
               var1.add(BloodBodyPartType.Foot_R);
            case Dress:
            default:
               break;
            case Chest:
               var1.add(BloodBodyPartType.Torso_Upper);
               break;
            case Waist:
               var1.add(BloodBodyPartType.Torso_Lower);
               break;
            case Belt:
               var1.add(BloodBodyPartType.UpperLeg_L);
               var1.add(BloodBodyPartType.UpperLeg_R);
               break;
            case Crotch:
               var1.add(BloodBodyPartType.Groin);
            }

            this.m_bloodBodyPartTypes = new BloodBodyPartType[var1.size()];

            for(int var2 = 0; var2 < var1.size(); ++var2) {
               this.m_bloodBodyPartTypes[var2] = (BloodBodyPartType)var1.get(var2);
            }

            return this.m_bloodBodyPartTypes;
         }
      }

      // $FF: synthetic method
      private static CharacterMask.Part[] $values() {
         return new CharacterMask.Part[]{Head, Torso, Pelvis, LeftArm, LeftHand, RightArm, RightHand, LeftLeg, LeftFoot, RightLeg, RightFoot, Dress, Chest, Waist, Belt, Crotch};
      }
   }
}
