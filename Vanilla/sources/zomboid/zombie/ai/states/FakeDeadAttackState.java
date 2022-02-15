package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.Stats;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;

public final class FakeDeadAttackState extends State {
   private static final FakeDeadAttackState _instance = new FakeDeadAttackState();

   public static FakeDeadAttackState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      var2.DirectionFromVector(var2.vectorToTarget);
      var2.setFakeDead(false);
      var1.setVisibleToNPCs(true);
      var1.setCollidable(true);
      String var3 = "MaleZombieAttack";
      if (var1.isFemale()) {
         var3 = "FemaleZombieAttack";
      }

      var1.getEmitter().playSound(var3);
      if (var2.target instanceof IsoPlayer && !((IsoPlayer)var2.target).getCharacterTraits().Desensitized.isSet()) {
         IsoPlayer var4 = (IsoPlayer)var2.target;
         Stats var10000 = var4.getStats();
         var10000.Panic += var4.getBodyDamage().getPanicIncreaseValue() * 3.0F;
      }

   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      IsoZombie var3 = (IsoZombie)var1;
      if (var2.m_EventName.equalsIgnoreCase("AttackCollisionCheck") && var1.isAlive() && var3.isTargetInCone(1.5F, 0.9F) && var3.target instanceof IsoGameCharacter) {
         IsoGameCharacter var4 = (IsoGameCharacter)var3.target;
         if (var4.getVehicle() == null || var4.getVehicle().couldCrawlerAttackPassenger(var4)) {
            var4.getBodyDamage().AddRandomDamageFromZombie((IsoZombie)var1, (String)null);
         }
      }

      if (var2.m_EventName.equalsIgnoreCase("FallOnFront")) {
         var3.setFallOnFront(Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("ActiveAnimFinishing")) {
         var3.setCrawler(true);
      }

   }
}
