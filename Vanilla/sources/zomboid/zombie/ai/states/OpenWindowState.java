package zombie.ai.states;

import java.nio.ByteBuffer;
import java.util.HashMap;
import zombie.GameTime;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.skills.PerkFactory;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.raknet.UdpConnection;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.debug.DebugOptions;
import zombie.iso.IsoDirections;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoWindow;

public final class OpenWindowState extends State {
   private static final OpenWindowState _instance = new OpenWindowState();
   private static final Integer PARAM_WINDOW = 1;

   public static OpenWindowState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setIgnoreMovement(true);
      var1.setHideWeaponModel(true);
      HashMap var2 = var1.getStateMachineParams(this);
      IsoWindow var3 = (IsoWindow)var2.get(PARAM_WINDOW);
      if (Core.bDebug && DebugOptions.instance.CheatWindowUnlock.getValue() && var3.getSprite() != null && !var3.getSprite().getProperties().Is("WindowLocked")) {
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

      var1.setVariable("bOpenWindow", true);
   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      if (var1.getVariableBoolean("bOpenWindow")) {
         IsoPlayer var3 = (IsoPlayer)var1;
         if (!var3.pressedMovement(false) && !var3.pressedCancelAction()) {
            IsoWindow var4 = (IsoWindow)var2.get(PARAM_WINDOW);
            if (var4 != null && var4.getObjectIndex() != -1) {
               if (IsoPlayer.getInstance().ContextPanic > 5.0F) {
                  IsoPlayer.getInstance().ContextPanic = 0.0F;
                  var1.setVariable("bOpenWindow", false);
                  var1.smashWindow(var4);
                  var1.getStateMachineParams(SmashWindowState.instance()).put(3, Boolean.TRUE);
               } else {
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

                  if (Core.bTutorial) {
                     if (var1.x != var4.getX() + 0.5F && var4.north) {
                        this.slideX(var1, var4.getX() + 0.5F);
                     }

                     if (var1.y != var4.getY() + 0.5F && !var4.north) {
                        this.slideY(var1, var4.getY() + 0.5F);
                     }
                  }

               }
            } else {
               var1.setVariable("bOpenWindow", false);
            }
         } else {
            var1.setVariable("bOpenWindow", false);
         }
      }
   }

   public void exit(IsoGameCharacter var1) {
      var1.setIgnoreMovement(false);
      var1.clearVariable("bOpenWindow");
      var1.clearVariable("OpenWindowOutcome");
      var1.clearVariable("StopAfterAnimLooped");
      var1.setHideWeaponModel(false);
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      if (var1.getVariableBoolean("bOpenWindow")) {
         IsoWindow var4 = (IsoWindow)var3.get(PARAM_WINDOW);
         if (var4 == null) {
            var1.setVariable("bOpenWindow", false);
         } else {
            if (var2.m_EventName.equalsIgnoreCase("WindowAnimLooped")) {
               if ("start".equalsIgnoreCase(var2.m_ParameterValue)) {
                  if (var4.isPermaLocked() || var4.Locked && var1.getCurrentSquare().Is(IsoFlagType.exterior)) {
                     var1.setVariable("OpenWindowOutcome", "struggle");
                  } else {
                     var1.setVariable("OpenWindowOutcome", "success");
                  }

                  return;
               }

               if (var2.m_ParameterValue.equalsIgnoreCase(var1.getVariableString("StopAfterAnimLooped"))) {
                  var1.setVariable("bOpenWindow", false);
               }
            }

            if (var2.m_EventName.equalsIgnoreCase("WindowOpenAttempt")) {
               this.onAttemptFinished(var1, var4);
            } else if (var2.m_EventName.equalsIgnoreCase("WindowOpenSuccess")) {
               this.onSuccess(var1, var4);
            } else if (var2.m_EventName.equalsIgnoreCase("WindowStruggleSound") && "struggle".equals(var1.getVariableString("OpenWindowOutcome"))) {
               var1.playSound("WindowIsLocked");
            }

         }
      }
   }

   public boolean isDoingActionThatCanBeCancelled() {
      return true;
   }

   private void onAttemptFinished(IsoGameCharacter var1, IsoWindow var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      this.exert(var1);
      if (var2.isPermaLocked()) {
         if (!var1.getEmitter().isPlaying("WindowIsLocked")) {
         }

         var1.setVariable("OpenWindowOutcome", "fail");
         var1.setVariable("StopAfterAnimLooped", "fail");
      } else {
         byte var4 = 10;
         if (var1.Traits.Burglar.isSet()) {
            var4 = 5;
         }

         if (var2.Locked && var1.getCurrentSquare().Is(IsoFlagType.exterior)) {
            if (Rand.Next(100) < var4) {
               var1.getEmitter().playSound("BreakLockOnWindow", var2);
               var2.setPermaLocked(true);
               var2.syncIsoObject(false, (byte)0, (UdpConnection)null, (ByteBuffer)null);
               var3.put(PARAM_WINDOW, (Object)null);
               var1.setVariable("OpenWindowOutcome", "fail");
               var1.setVariable("StopAfterAnimLooped", "fail");
               return;
            }

            boolean var5 = false;
            if (var1.getPerkLevel(PerkFactory.Perks.Strength) > 7 && Rand.Next(100) < 20) {
               var5 = true;
            } else if (var1.getPerkLevel(PerkFactory.Perks.Strength) > 5 && Rand.Next(100) < 10) {
               var5 = true;
            } else if (var1.getPerkLevel(PerkFactory.Perks.Strength) > 3 && Rand.Next(100) < 6) {
               var5 = true;
            } else if (var1.getPerkLevel(PerkFactory.Perks.Strength) > 1 && Rand.Next(100) < 4) {
               var5 = true;
            } else if (Rand.Next(100) <= 1) {
               var5 = true;
            }

            if (var5) {
               var1.setVariable("OpenWindowOutcome", "success");
            }
         } else {
            var1.setVariable("OpenWindowOutcome", "success");
         }

      }
   }

   private void onSuccess(IsoGameCharacter var1, IsoWindow var2) {
      var1.setVariable("StopAfterAnimLooped", "success");
      IsoPlayer.getInstance().ContextPanic = 0.0F;
      if (var2.getObjectIndex() != -1 && !var2.open) {
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

   private void slideX(IsoGameCharacter var1, float var2) {
      float var3 = 0.05F * GameTime.getInstance().getMultiplier() / 1.6F;
      var3 = var2 > var1.x ? Math.min(var3, var2 - var1.x) : Math.max(-var3, var2 - var1.x);
      var1.x += var3;
      var1.nx = var1.x;
   }

   private void slideY(IsoGameCharacter var1, float var2) {
      float var3 = 0.05F * GameTime.getInstance().getMultiplier() / 1.6F;
      var3 = var2 > var1.y ? Math.min(var3, var2 - var1.y) : Math.max(-var3, var2 - var1.y);
      var1.y += var3;
      var1.ny = var1.y;
   }

   public void setParams(IsoGameCharacter var1, IsoWindow var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      var3.clear();
      var3.put(PARAM_WINDOW, var2);
   }
}
