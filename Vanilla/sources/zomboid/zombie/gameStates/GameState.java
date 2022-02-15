package zombie.gameStates;

public class GameState {
   public void enter() {
   }

   public void exit() {
   }

   public void render() {
   }

   public GameState redirectState() {
      return null;
   }

   public GameStateMachine.StateAction update() {
      return GameStateMachine.StateAction.Continue;
   }

   public void yield() {
   }

   public void reenter() {
   }
}
