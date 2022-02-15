package zombie.ai.states;

import fmod.fmod.FMODManager;
import zombie.ai.State;
import zombie.audio.parameters.ParameterCharacterMovementSpeed;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.util.Type;

public final class BumpedState extends State {
   private static final BumpedState _instance = new BumpedState();

   public static BumpedState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setBumpDone(false);
      var1.setVariable("BumpFallAnimFinished", false);
      var1.getAnimationPlayer().setTargetToAngle();
      var1.getForwardDirection().setLengthAndDirection(var1.getAnimationPlayer().getAngle(), 1.0F);
      this.setCharacterBlockMovement(var1, true);
      if (var1.getVariableBoolean("BumpFall")) {
         long var2 = var1.playSound("TripOverObstacle");
         ParameterCharacterMovementSpeed var4 = ((IsoPlayer)var1).getParameterCharacterMovementSpeed();
         var1.getEmitter().setParameterValue(var2, var4.getParameterDescription(), var4.calculateCurrentValue());
         String var5 = var1.getVariableString("TripObstacleType");
         if (var5 == null) {
            var5 = "zombie";
         }

         var1.clearVariable("TripObstacleType");
         byte var8 = -1;
         switch(var5.hashCode()) {
         case 3568542:
            if (var5.equals("tree")) {
               var8 = 0;
            }
         default:
            byte var10000;
            switch(var8) {
            case 0:
               var10000 = 5;
               break;
            default:
               var10000 = 6;
            }

            byte var6 = var10000;
            var1.getEmitter().setParameterValue(var2, FMODManager.instance.getParameterDescription("TripObstacleType"), (float)var6);
         }
      }

   }

   public void execute(IsoGameCharacter var1) {
      boolean var2 = var1.isBumpFall() || var1.isBumpStaggered();
      this.setCharacterBlockMovement(var1, var2);
   }

   private void setCharacterBlockMovement(IsoGameCharacter var1, boolean var2) {
      IsoPlayer var3 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
      if (var3 != null) {
         var3.setBlockMovement(var2);
      }

   }

   public void exit(IsoGameCharacter var1) {
      var1.clearVariable("BumpFallType");
      var1.clearVariable("BumpFallAnimFinished");
      var1.clearVariable("BumpAnimFinished");
      var1.setBumpType("");
      var1.setBumpedChr((IsoGameCharacter)null);
      IsoPlayer var2 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
      if (var2 != null) {
         var2.setInitiateAttack(false);
         var2.attackStarted = false;
         var2.setAttackType((String)null);
      }

      if (var2 != null && var1.isBumpFall()) {
         var1.fallenOnKnees();
      }

      var1.setOnFloor(false);
      var1.setBumpFall(false);
      this.setCharacterBlockMovement(var1, false);
      if (var1 instanceof IsoZombie && ((IsoZombie)var1).target != null) {
         var1.pathToLocation((int)((IsoZombie)var1).target.getX(), (int)((IsoZombie)var1).target.getY(), (int)((IsoZombie)var1).target.getZ());
      }

   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      if (var2.m_EventName.equalsIgnoreCase("FallOnFront")) {
         var1.setFallOnFront(Boolean.parseBoolean(var2.m_ParameterValue));
         var1.setOnFloor(var1.isFallOnFront());
      }

      if (var2.m_EventName.equalsIgnoreCase("FallOnBack")) {
         var1.setOnFloor(Boolean.parseBoolean(var2.m_ParameterValue));
      }

   }
}
