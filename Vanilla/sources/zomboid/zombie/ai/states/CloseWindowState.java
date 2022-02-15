package zombie.ai.states;

import java.util.HashMap;
import zombie.GameTime;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.skills.PerkFactory;
import zombie.core.Core;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.debug.DebugOptions;
import zombie.iso.IsoDirections;
import zombie.iso.objects.IsoWindow;

public final class CloseWindowState extends State {
   private static final CloseWindowState _instance = new CloseWindowState();

   public static CloseWindowState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      var1.setIgnoreMovement(true);
      var1.setHideWeaponModel(true);
      IsoWindow var3 = (IsoWindow)var2.get(0);
      if (Core.bDebug && DebugOptions.instance.CheatWindowUnlock.getValue()) {
         var3.Locked = false;
         var3.PermaLocked = false;
      }

      if (var3.north) {
         if ((float)var3.getSquare().getY() < var1.getY()) {
            var1.setDir(IsoDirections.N);
         } else {
            var1.setDir(IsoDirections.S);
         }
      } else if ((float)var3.getSquare().getX() < var1.getX()) {
         var1.setDir(IsoDirections.W);
      } else {
         var1.setDir(IsoDirections.E);
      }

      var1.setVariable("bCloseWindow", true);
      var1.clearVariable("BlockWindow");
   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      if (var1.getVariableBoolean("bCloseWindow")) {
         IsoPlayer var3 = (IsoPlayer)var1;
         if (!var3.pressedMovement(false) && !var3.pressedCancelAction()) {
            if (!(var2.get(0) instanceof IsoWindow)) {
               var1.setVariable("bCloseWindow", false);
            } else {
               IsoWindow var4 = (IsoWindow)var2.get(0);
               if (var4 != null && var4.getObjectIndex() != -1) {
                  var3.setCollidable(true);
                  var3.updateLOS();
                  if (var4.north) {
                     if ((float)var4.getSquare().getY() < var1.getY()) {
                        var1.setDir(IsoDirections.N);
                     } else {
                        var1.setDir(IsoDirections.S);
                     }
                  } else if ((float)var4.getSquare().getX() < var1.getX()) {
                     var1.setDir(IsoDirections.W);
                  } else {
                     var1.setDir(IsoDirections.E);
                  }

               } else {
                  var1.setVariable("bCloseWindow", false);
               }
            }
         } else {
            var1.setVariable("bCloseWindow", false);
         }
      }
   }

   public void exit(IsoGameCharacter var1) {
      var1.clearVariable("BlockWindow");
      var1.clearVariable("bCloseWindow");
      var1.clearVariable("CloseWindowOutcome");
      var1.clearVariable("StopAfterAnimLooped");
      var1.setIgnoreMovement(false);
      var1.setHideWeaponModel(false);
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      if (var1.getVariableBoolean("bCloseWindow")) {
         if (!(var3.get(0) instanceof IsoWindow)) {
            var1.setVariable("bCloseWindow", false);
         } else {
            IsoWindow var4 = (IsoWindow)var3.get(0);
            if (var2.m_EventName.equalsIgnoreCase("WindowAnimLooped")) {
               if ("start".equalsIgnoreCase(var2.m_ParameterValue)) {
                  int var5 = Math.max(5 - var1.getMoodles().getMoodleLevel(MoodleType.Panic), 1);
                  if (!var4.isPermaLocked() && var4.getFirstCharacterClimbingThrough() == null) {
                     var1.setVariable("CloseWindowOutcome", "success");
                  } else {
                     var1.setVariable("CloseWindowOutcome", "struggle");
                  }

                  return;
               }

               if (var2.m_ParameterValue.equalsIgnoreCase(var1.getVariableString("StopAfterAnimLooped"))) {
                  var1.setVariable("bCloseWindow", false);
               }
            }

            if (var2.m_EventName.equalsIgnoreCase("WindowCloseAttempt")) {
               this.onAttemptFinished(var1, var4);
            } else if (var2.m_EventName.equalsIgnoreCase("WindowCloseSuccess")) {
               this.onSuccess(var1, var4);
            }

         }
      }
   }

   public boolean isDoingActionThatCanBeCancelled() {
      return true;
   }

   private void onAttemptFinished(IsoGameCharacter var1, IsoWindow var2) {
      this.exert(var1);
      if (var2.isPermaLocked()) {
         var1.getEmitter().playSound("WindowIsLocked", var2);
         var1.setVariable("CloseWindowOutcome", "fail");
         var1.setVariable("StopAfterAnimLooped", "fail");
      } else {
         int var3 = Math.max(5 - var1.getMoodles().getMoodleLevel(MoodleType.Panic), 3);
         if (!var2.isPermaLocked() && var2.getFirstCharacterClimbingThrough() == null) {
            var1.setVariable("CloseWindowOutcome", "success");
         } else {
            var1.setVariable("CloseWindowOutcome", "struggle");
         }

      }
   }

   private void onSuccess(IsoGameCharacter var1, IsoWindow var2) {
      var1.setVariable("StopAfterAnimLooped", "success");
      IsoPlayer.getInstance().ContextPanic = 0.0F;
      if (var2.getObjectIndex() != -1 && var2.open) {
         var2.ToggleWindow(var1);
      }

   }

   private void exert(IsoGameCharacter var1) {
      float var2 = GameTime.getInstance().getMultiplier() / 1.6F;
      switch(var1.getPerkLevel(PerkFactory.Perks.Fitness)) {
      case 1:
         var1.exert(0.01F * var2);
         break;
      case 2:
         var1.exert(0.009F * var2);
         break;
      case 3:
         var1.exert(0.008F * var2);
         break;
      case 4:
         var1.exert(0.007F * var2);
         break;
      case 5:
         var1.exert(0.006F * var2);
         break;
      case 6:
         var1.exert(0.005F * var2);
         break;
      case 7:
         var1.exert(0.004F * var2);
         break;
      case 8:
         var1.exert(0.003F * var2);
         break;
      case 9:
         var1.exert(0.0025F * var2);
         break;
      case 10:
         var1.exert(0.002F * var2);
      }

   }

   public IsoWindow getWindow(IsoGameCharacter var1) {
      if (!var1.isCurrentState(this)) {
         return null;
      } else {
         HashMap var2 = var1.getStateMachineParams(this);
         return (IsoWindow)var2.get(0);
      }
   }
}
