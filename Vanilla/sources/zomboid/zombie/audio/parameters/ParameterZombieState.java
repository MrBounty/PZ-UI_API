package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.characters.IsoZombie;

public final class ParameterZombieState extends FMODLocalParameter {
   private final IsoZombie zombie;
   private ParameterZombieState.State state;

   public ParameterZombieState(IsoZombie var1) {
      super("ZombieState");
      this.state = ParameterZombieState.State.Idle;
      this.zombie = var1;
   }

   public float calculateCurrentValue() {
      if (this.zombie.target == null) {
         if (this.state == ParameterZombieState.State.SearchTarget) {
            this.setState(ParameterZombieState.State.Idle);
         }
      } else if (this.state == ParameterZombieState.State.Idle) {
         this.setState(ParameterZombieState.State.SearchTarget);
      }

      return (float)this.state.index;
   }

   public void setState(ParameterZombieState.State var1) {
      if (var1 != this.state) {
         this.state = var1;
      }
   }

   public boolean isState(ParameterZombieState.State var1) {
      return this.state == var1;
   }

   public static enum State {
      Idle(0),
      Eating(1),
      SearchTarget(2),
      LockTarget(3),
      AttackScratch(4),
      AttackLacerate(5),
      AttackBite(6),
      Hit(7),
      Death(8),
      Reanimate(9),
      Pushed(10),
      GettingUp(11),
      Attack(12),
      RunOver(13);

      final int index;

      private State(int var3) {
         this.index = var3;
      }

      // $FF: synthetic method
      private static ParameterZombieState.State[] $values() {
         return new ParameterZombieState.State[]{Idle, Eating, SearchTarget, LockTarget, AttackScratch, AttackLacerate, AttackBite, Hit, Death, Reanimate, Pushed, GettingUp, Attack, RunOver};
      }
   }
}
