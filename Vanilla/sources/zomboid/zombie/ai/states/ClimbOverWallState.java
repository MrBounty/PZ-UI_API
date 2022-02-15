package zombie.ai.states;

import fmod.fmod.FMODManager;
import java.util.HashMap;
import zombie.GameTime;
import zombie.ZomboidGlobals;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.Stats;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.skills.PerkFactory;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.core.properties.PropertyContainer;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;

public final class ClimbOverWallState extends State {
   private static final ClimbOverWallState _instance = new ClimbOverWallState();
   static final Integer PARAM_START_X = 0;
   static final Integer PARAM_START_Y = 1;
   static final Integer PARAM_Z = 2;
   static final Integer PARAM_END_X = 3;
   static final Integer PARAM_END_Y = 4;
   static final Integer PARAM_DIR = 5;
   static final int FENCE_TYPE_WOOD = 0;
   static final int FENCE_TYPE_METAL = 1;

   public static ClimbOverWallState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setIgnoreMovement(true);
      var1.setHideWeaponModel(true);
      var1.getStateMachineParams(this);
      Stats var10000 = var1.getStats();
      var10000.endurance = (float)((double)var10000.endurance - ZomboidGlobals.RunningEnduranceReduce * 1200.0D);
      IsoPlayer var3 = (IsoPlayer)var1;
      boolean var4 = var3.isClimbOverWallStruggle();
      if (var4) {
         var10000 = var1.getStats();
         var10000.endurance = (float)((double)var10000.endurance - ZomboidGlobals.RunningEnduranceReduce * 500.0D);
      }

      boolean var5 = var3.isClimbOverWallSuccess();
      var1.setVariable("ClimbFenceFinished", false);
      var1.setVariable("ClimbFenceOutcome", var5 ? "success" : "fail");
      var1.setVariable("ClimbFenceStarted", false);
      var1.setVariable("ClimbFenceStruggle", var4);
   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      IsoDirections var3 = (IsoDirections)var2.get(PARAM_DIR);
      var1.setAnimated(true);
      var1.setDir(var3);
      boolean var4 = var1.getVariableBoolean("ClimbFenceStarted");
      if (!var4) {
         int var5 = (Integer)var2.get(PARAM_START_X);
         int var6 = (Integer)var2.get(PARAM_START_Y);
         float var7 = 0.15F;
         float var8 = var1.getX();
         float var9 = var1.getY();
         switch(var3) {
         case N:
            var9 = (float)var6 + var7;
            break;
         case S:
            var9 = (float)(var6 + 1) - var7;
            break;
         case W:
            var8 = (float)var5 + var7;
            break;
         case E:
            var8 = (float)(var5 + 1) - var7;
         }

         float var10 = GameTime.getInstance().getMultiplier() / 1.6F / 8.0F;
         var1.setX(var1.x + (var8 - var1.x) * var10);
         var1.setY(var1.y + (var9 - var1.y) * var10);
      }

   }

   public void exit(IsoGameCharacter var1) {
      var1.clearVariable("ClimbFenceFinished");
      var1.clearVariable("ClimbFenceOutcome");
      var1.clearVariable("ClimbFenceStarted");
      var1.clearVariable("ClimbFenceStruggle");
      var1.setIgnoreMovement(false);
      var1.setHideWeaponModel(false);
      if (var1 instanceof IsoZombie) {
         ((IsoZombie)var1).networkAI.isClimbing = false;
      }

   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (var2.m_EventName.equalsIgnoreCase("PlayFenceSound")) {
         IsoObject var3 = this.getFence(var1);
         if (var3 == null) {
            return;
         }

         int var4 = this.getFenceType(var3);
         long var5 = var1.playSound(var2.m_ParameterValue);
         var1.getEmitter().setParameterValue(var5, FMODManager.instance.getParameterDescription("FenceTypeHigh"), (float)var4);
      }

   }

   public boolean isIgnoreCollide(IsoGameCharacter var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      HashMap var8 = var1.getStateMachineParams(this);
      int var9 = (Integer)var8.get(PARAM_START_X);
      int var10 = (Integer)var8.get(PARAM_START_Y);
      int var11 = (Integer)var8.get(PARAM_END_X);
      int var12 = (Integer)var8.get(PARAM_END_Y);
      int var13 = (Integer)var8.get(PARAM_Z);
      if (var13 == var4 && var13 == var7) {
         int var14 = PZMath.min(var9, var11);
         int var15 = PZMath.min(var10, var12);
         int var16 = PZMath.max(var9, var11);
         int var17 = PZMath.max(var10, var12);
         int var18 = PZMath.min(var2, var5);
         int var19 = PZMath.min(var3, var6);
         int var20 = PZMath.max(var2, var5);
         int var21 = PZMath.max(var3, var6);
         return var14 <= var18 && var15 <= var19 && var16 >= var20 && var17 >= var21;
      } else {
         return false;
      }
   }

   private IsoObject getClimbableWallN(IsoGridSquare var1) {
      IsoObject[] var2 = (IsoObject[])var1.getObjects().getElements();
      int var3 = 0;

      for(int var4 = var1.getObjects().size(); var3 < var4; ++var3) {
         IsoObject var5 = var2[var3];
         PropertyContainer var6 = var5.getProperties();
         if (var6 != null && !var6.Is(IsoFlagType.CantClimb) && var5.getType() == IsoObjectType.wall && var6.Is(IsoFlagType.collideN) && !var6.Is(IsoFlagType.HoppableN)) {
            return var5;
         }
      }

      return null;
   }

   private IsoObject getClimbableWallW(IsoGridSquare var1) {
      IsoObject[] var2 = (IsoObject[])var1.getObjects().getElements();
      int var3 = 0;

      for(int var4 = var1.getObjects().size(); var3 < var4; ++var3) {
         IsoObject var5 = var2[var3];
         PropertyContainer var6 = var5.getProperties();
         if (var6 != null && !var6.Is(IsoFlagType.CantClimb) && var5.getType() == IsoObjectType.wall && var6.Is(IsoFlagType.collideW) && !var6.Is(IsoFlagType.HoppableW)) {
            return var5;
         }
      }

      return null;
   }

   private IsoObject getFence(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      int var3 = (Integer)var2.get(PARAM_START_X);
      int var4 = (Integer)var2.get(PARAM_START_Y);
      int var5 = (Integer)var2.get(PARAM_Z);
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, var5);
      int var7 = (Integer)var2.get(PARAM_END_X);
      int var8 = (Integer)var2.get(PARAM_END_Y);
      IsoGridSquare var9 = IsoWorld.instance.CurrentCell.getGridSquare(var7, var8, var5);
      if (var6 != null && var9 != null) {
         IsoDirections var10 = (IsoDirections)var2.get(PARAM_DIR);
         IsoObject var10000;
         switch(var10) {
         case N:
            var10000 = this.getClimbableWallN(var6);
            break;
         case S:
            var10000 = this.getClimbableWallN(var9);
            break;
         case W:
            var10000 = this.getClimbableWallW(var6);
            break;
         case E:
            var10000 = this.getClimbableWallW(var9);
            break;
         default:
            var10000 = null;
         }

         return var10000;
      } else {
         return null;
      }
   }

   private int getFenceType(IsoObject var1) {
      if (var1.getSprite() == null) {
         return 0;
      } else {
         PropertyContainer var2 = var1.getSprite().getProperties();
         String var3 = var2.Val("FenceTypeHigh");
         if (var3 != null) {
            byte var5 = -1;
            switch(var3.hashCode()) {
            case 2702029:
               if (var3.equals("Wood")) {
                  var5 = 0;
               }
               break;
            case 74234599:
               if (var3.equals("Metal")) {
                  var5 = 1;
               }
            }

            byte var10000;
            switch(var5) {
            case 0:
               var10000 = 0;
               break;
            case 1:
               var10000 = 1;
               break;
            default:
               var10000 = 0;
            }

            return var10000;
         } else {
            return 0;
         }
      }
   }

   public void setParams(IsoGameCharacter var1, IsoDirections var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      int var4 = var1.getSquare().getX();
      int var5 = var1.getSquare().getY();
      int var6 = var1.getSquare().getZ();
      int var9 = var4;
      int var10 = var5;
      switch(var2) {
      case N:
         var10 = var5 - 1;
         break;
      case S:
         var10 = var5 + 1;
         break;
      case W:
         var9 = var4 - 1;
         break;
      case E:
         var9 = var4 + 1;
         break;
      default:
         throw new IllegalArgumentException("invalid direction");
      }

      var3.put(PARAM_START_X, var4);
      var3.put(PARAM_START_Y, var5);
      var3.put(PARAM_Z, var6);
      var3.put(PARAM_END_X, var9);
      var3.put(PARAM_END_Y, var10);
      var3.put(PARAM_DIR, var2);
      IsoPlayer var11 = (IsoPlayer)var1;
      if (var11.isLocalPlayer()) {
         byte var12 = 20;
         int var16 = var12 + var1.getPerkLevel(PerkFactory.Perks.Fitness) * 2;
         var16 += var1.getPerkLevel(PerkFactory.Perks.Strength) * 2;
         var16 -= var1.getMoodles().getMoodleLevel(MoodleType.Endurance) * 5;
         var16 -= var1.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) * 8;
         if (var1.getTraits().contains("Emaciated") || var1.Traits.Obese.isSet() || var1.getTraits().contains("Very Underweight")) {
            var16 -= 25;
         }

         if (var1.getTraits().contains("Underweight") || var1.getTraits().contains("Overweight")) {
            var16 -= 15;
         }

         IsoGridSquare var13 = var1.getCurrentSquare();
         if (var13 != null) {
            for(int var14 = 0; var14 < var13.getMovingObjects().size(); ++var14) {
               IsoMovingObject var15 = (IsoMovingObject)var13.getMovingObjects().get(var14);
               if (var15 instanceof IsoZombie) {
                  if (((IsoZombie)var15).target == var1 && ((IsoZombie)var15).getCurrentState() == AttackState.instance()) {
                     var16 -= 25;
                  } else {
                     var16 -= 7;
                  }
               }
            }
         }

         var16 = Math.max(0, var16);
         boolean var17 = Rand.NextBool(var16 / 2);
         if ("Tutorial".equals(Core.GameMode)) {
            var17 = false;
         }

         boolean var18 = !Rand.NextBool(var16);
         var11.setClimbOverWallStruggle(var17);
         var11.setClimbOverWallSuccess(var18);
      }

   }
}
