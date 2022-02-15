package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;

public final class ZombieFaceTargetState extends State {
   private static final ZombieFaceTargetState _instance = new ZombieFaceTargetState();

   public static ZombieFaceTargetState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
   }

   public void execute(IsoGameCharacter var1) {
      IsoZombie var2 = (IsoZombie)var1;
      if (var2.getTarget() != null) {
         var2.faceThisObject(var2.getTarget());
      }

   }

   public void exit(IsoGameCharacter var1) {
   }
}
