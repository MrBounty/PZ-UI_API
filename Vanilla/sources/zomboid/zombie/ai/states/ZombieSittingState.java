package zombie.ai.states;

import zombie.ai.State;
import zombie.characters.IsoGameCharacter;

public final class ZombieSittingState extends State {
   private static final ZombieSittingState _instance = new ZombieSittingState();

   public static ZombieSittingState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
   }

   public void execute(IsoGameCharacter var1) {
   }

   public void exit(IsoGameCharacter var1) {
   }
}
