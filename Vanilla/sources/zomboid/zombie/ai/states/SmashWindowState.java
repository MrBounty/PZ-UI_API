package zombie.ai.states;

import java.util.HashMap;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoDirections;
import zombie.iso.objects.IsoWindow;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehicleWindow;

public final class SmashWindowState extends State {
   private static final SmashWindowState _instance = new SmashWindowState();

   public static SmashWindowState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setIgnoreMovement(true);
      var1.setVariable("bSmashWindow", true);
      HandWeapon var2 = (HandWeapon)Type.tryCastTo(var1.getPrimaryHandItem(), HandWeapon.class);
      if (var2 != null && var2.isRanged()) {
         var1.playSound("AttackShove");
      } else if (var2 != null && !StringUtils.isNullOrWhitespace(var2.getSwingSound())) {
         var1.playSound(var2.getSwingSound());
      }

   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      if (!(var2.get(0) instanceof IsoWindow) && !(var2.get(0) instanceof VehicleWindow)) {
         var1.setVariable("bSmashWindow", false);
      } else {
         IsoPlayer var3 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
         if (!var3.pressedMovement(false) && !var3.pressedCancelAction()) {
            if (!(var2.get(0) instanceof IsoWindow)) {
               if (var2.get(0) instanceof VehicleWindow) {
                  VehicleWindow var5 = (VehicleWindow)var2.get(0);
                  var1.faceThisObject((BaseVehicle)var2.get(1));
                  if (var5.isDestroyed() && !"true".equals(var1.getVariableString("OwnerSmashedIt"))) {
                     var1.setVariable("bSmashWindow", false);
                     return;
                  }
               }
            } else {
               IsoWindow var4 = (IsoWindow)var2.get(0);
               if (var4.getObjectIndex() == -1 || var4.isDestroyed() && !"true".equals(var1.getVariableString("OwnerSmashedIt"))) {
                  var1.setVariable("bSmashWindow", false);
                  return;
               }

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
            }

         } else {
            var1.setVariable("bSmashWindow", false);
         }
      }
   }

   public void exit(IsoGameCharacter var1) {
      var1.setIgnoreMovement(false);
      var1.clearVariable("bSmashWindow");
      var1.clearVariable("OwnerSmashedIt");
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      if (var3.get(0) instanceof IsoWindow) {
         IsoWindow var4 = (IsoWindow)var3.get(0);
         if (var2.m_EventName.equalsIgnoreCase("AttackCollisionCheck")) {
            var1.setVariable("OwnerSmashedIt", true);
            IsoPlayer.getInstance().ContextPanic = 0.0F;
            var4.WeaponHit(var1, (HandWeapon)null);
            if (!(var1.getPrimaryHandItem() instanceof HandWeapon) && !(var1.getSecondaryHandItem() instanceof HandWeapon)) {
               var1.getBodyDamage().setScratchedWindow();
            }
         } else if (var2.m_EventName.equalsIgnoreCase("ActiveAnimFinishing")) {
            var1.setVariable("bSmashWindow", false);
            if (Boolean.TRUE == var3.get(3)) {
               var1.climbThroughWindow(var4);
            }
         }
      } else if (var3.get(0) instanceof VehicleWindow) {
         VehicleWindow var5 = (VehicleWindow)var3.get(0);
         if (var2.m_EventName.equalsIgnoreCase("AttackCollisionCheck")) {
            var1.setVariable("OwnerSmashedIt", true);
            IsoPlayer.getInstance().ContextPanic = 0.0F;
            var5.hit(var1);
            if (!(var1.getPrimaryHandItem() instanceof HandWeapon) && !(var1.getSecondaryHandItem() instanceof HandWeapon)) {
               var1.getBodyDamage().setScratchedWindow();
            }
         } else if (var2.m_EventName.equalsIgnoreCase("ActiveAnimFinishing")) {
            var1.setVariable("bSmashWindow", false);
         }
      }

   }

   public boolean isDoingActionThatCanBeCancelled() {
      return true;
   }
}
