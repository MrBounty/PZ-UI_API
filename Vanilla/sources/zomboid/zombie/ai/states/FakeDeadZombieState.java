package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.iso.objects.IsoDeadBody;
import zombie.network.GameServer;

public final class FakeDeadZombieState extends State {
   private static final FakeDeadZombieState _instance = new FakeDeadZombieState();

   public static FakeDeadZombieState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setVisibleToNPCs(false);
      var1.setCollidable(false);
      ((IsoZombie)var1).setFakeDead(true);
      var1.setOnFloor(true);
   }

   public void execute(IsoGameCharacter var1) {
      if (var1.isDead()) {
         if (GameServer.bServer) {
            GameServer.sendZombieDeath((IsoZombie)var1);
         }

         new IsoDeadBody(var1);
      } else {
         if (Core.bLastStand) {
            ((IsoZombie)var1).setFakeDead(false);
         }

      }
   }

   public void exit(IsoGameCharacter var1) {
      ((IsoZombie)var1).setFakeDead(false);
   }
}
