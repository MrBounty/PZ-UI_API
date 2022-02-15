package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.Moodles.MoodleType;
import zombie.network.GameClient;

public final class PlayerGetUpState extends State {
   private static final PlayerGetUpState _instance = new PlayerGetUpState();

   public static PlayerGetUpState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.getStateMachineParams(this);
      var1.setIgnoreMovement(true);
      IsoPlayer var3 = (IsoPlayer)var1;
      var3.setInitiateAttack(false);
      var3.attackStarted = false;
      var3.setAttackType((String)null);
      var3.setBlockMovement(true);
      var3.setForceRun(false);
      var3.setForceSprint(false);
      var1.setVariable("getUpQuick", var1.getVariableBoolean("pressedRunButton"));
      if (var1.getMoodles().getMoodleLevel(MoodleType.Panic) > 1) {
         var1.setVariable("getUpQuick", true);
      }

      if (var1.getVariableBoolean("pressedMovement")) {
         var1.setVariable("getUpWalk", true);
      }

      if (GameClient.bClient) {
         var1.setKnockedDown(false);
      }

   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
      var1.clearVariable("getUpWalk");
      if (var1.getVariableBoolean("sitonground")) {
         var1.setHideWeaponModel(false);
      }

      var1.setIgnoreMovement(false);
      var1.setFallOnFront(false);
      var1.setOnFloor(false);
      ((IsoPlayer)var1).setBlockMovement(false);
      var1.setSitOnGround(false);
   }
}
