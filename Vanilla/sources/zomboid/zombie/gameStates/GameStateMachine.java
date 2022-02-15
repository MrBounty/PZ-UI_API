package zombie.gameStates;

import java.util.ArrayList;
import java.util.Stack;

public final class GameStateMachine {
   public boolean firstrun = true;
   public boolean Loop = true;
   public int StateIndex = 0;
   public int LoopToState = 0;
   public final ArrayList States = new ArrayList();
   public GameState current = null;
   private final Stack yieldStack = new Stack();
   public GameState forceNext = null;

   public void render() {
      if (this.current != null) {
         this.current.render();
      }

   }

   public void update() {
      if (this.States.size() == 0) {
         if (this.forceNext == null) {
            return;
         }

         this.States.add(this.forceNext);
         this.forceNext = null;
      }

      if (this.firstrun) {
         if (this.current == null) {
            this.current = (GameState)this.States.get(this.StateIndex);
         }

         System.out.println("STATE: enter " + this.current.getClass().getName());
         this.current.enter();
         this.firstrun = false;
      }

      if (this.current == null) {
         if (!this.Loop) {
            return;
         }

         this.StateIndex = this.LoopToState;
         if (this.States.isEmpty()) {
            return;
         }

         this.current = (GameState)this.States.get(this.StateIndex);
         if (this.StateIndex < this.States.size()) {
            System.out.println("STATE: enter " + this.current.getClass().getName());
            this.current.enter();
         }
      }

      if (this.current != null) {
         GameState var1 = null;
         if (this.forceNext != null) {
            System.out.println("STATE: exit " + this.current.getClass().getName());
            this.current.exit();
            var1 = this.forceNext;
            this.forceNext = null;
         } else {
            GameStateMachine.StateAction var2 = this.current.update();
            if (var2 == GameStateMachine.StateAction.Continue) {
               System.out.println("STATE: exit " + this.current.getClass().getName());
               this.current.exit();
               if (!this.yieldStack.isEmpty()) {
                  this.current = (GameState)this.yieldStack.pop();
                  System.out.println("STATE: reenter " + this.current.getClass().getName());
                  this.current.reenter();
                  return;
               }

               var1 = this.current.redirectState();
            } else {
               if (var2 != GameStateMachine.StateAction.Yield) {
                  return;
               }

               System.out.println("STATE: yield " + this.current.getClass().getName());
               this.current.yield();
               this.yieldStack.push(this.current);
               var1 = this.current.redirectState();
            }
         }

         if (var1 == null) {
            ++this.StateIndex;
            if (this.StateIndex < this.States.size()) {
               this.current = (GameState)this.States.get(this.StateIndex);
               System.out.println("STATE: enter " + this.current.getClass().getName());
               this.current.enter();
            } else {
               this.current = null;
            }
         } else {
            System.out.println("STATE: enter " + var1.getClass().getName());
            var1.enter();
            this.current = var1;
         }
      }

   }

   public void forceNextState(GameState var1) {
      this.forceNext = var1;
   }

   public static enum StateAction {
      Continue,
      Remain,
      Yield;

      // $FF: synthetic method
      private static GameStateMachine.StateAction[] $values() {
         return new GameStateMachine.StateAction[]{Continue, Remain, Yield};
      }
   }
}
