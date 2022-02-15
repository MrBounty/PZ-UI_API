package zombie.ai;

import java.util.ArrayList;
import java.util.List;
import zombie.Lua.LuaEventManager;
import zombie.characters.IsoGameCharacter;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.debug.DebugLog;
import zombie.util.Lambda;
import zombie.util.list.PZArrayUtil;

public final class StateMachine {
   private boolean m_isLocked = false;
   public int activeStateChanged = 0;
   private State m_currentState;
   private State m_previousState;
   private final IsoGameCharacter m_owner;
   private final List m_subStates = new ArrayList();

   public StateMachine(IsoGameCharacter var1) {
      this.m_owner = var1;
   }

   public void changeState(State var1, Iterable var2) {
      this.changeState(var1, var2, false);
   }

   public void changeState(State var1, Iterable var2, boolean var3) {
      if (!this.m_isLocked) {
         this.changeRootState(var1, var3);
         PZArrayUtil.forEach(this.m_subStates, (var0) -> {
            var0.shouldBeActive = false;
         });
         PZArrayUtil.forEach(var2, Lambda.consumer(this, (var0, var1x) -> {
            if (var0 != null) {
               var1x.ensureSubstateActive(var0);
            }

         }));
         Lambda.forEachFrom(PZArrayUtil::forEach, (List)this.m_subStates, this, (var0, var1x) -> {
            if (!var0.shouldBeActive && !var0.isEmpty()) {
               var1x.removeSubstate(var0);
            }

         });
      }
   }

   private void changeRootState(State var1, boolean var2) {
      if (this.m_currentState == var1) {
         if (var2) {
            this.stateEnter(this.m_currentState);
         }

      } else {
         State var3 = this.m_currentState;
         if (var3 != null) {
            this.stateExit(var3);
         }

         this.m_previousState = var3;
         this.m_currentState = var1;
         if (var1 != null) {
            this.stateEnter(var1);
         }

         LuaEventManager.triggerEvent("OnAIStateChange", this.m_owner, this.m_currentState, this.m_previousState);
      }
   }

   private void ensureSubstateActive(State var1) {
      StateMachine.SubstateSlot var2 = this.getExistingSlot(var1);
      if (var2 != null) {
         var2.shouldBeActive = true;
      } else {
         StateMachine.SubstateSlot var3 = (StateMachine.SubstateSlot)PZArrayUtil.find(this.m_subStates, StateMachine.SubstateSlot::isEmpty);
         if (var3 != null) {
            var3.setState(var1);
            var3.shouldBeActive = true;
         } else {
            StateMachine.SubstateSlot var4 = new StateMachine.SubstateSlot(var1);
            this.m_subStates.add(var4);
         }

         this.stateEnter(var1);
      }
   }

   private StateMachine.SubstateSlot getExistingSlot(State var1) {
      return (StateMachine.SubstateSlot)PZArrayUtil.find(this.m_subStates, Lambda.predicate(var1, (var0, var1x) -> {
         return var0.getState() == var1x;
      }));
   }

   private void removeSubstate(State var1) {
      StateMachine.SubstateSlot var2 = this.getExistingSlot(var1);
      if (var2 != null) {
         this.removeSubstate(var2);
      }
   }

   private void removeSubstate(StateMachine.SubstateSlot var1) {
      State var2 = var1.getState();
      var1.setState((State)null);
      this.stateExit(var2);
   }

   public boolean isSubstate(State var1) {
      return PZArrayUtil.contains(this.m_subStates, Lambda.predicate(var1, (var0, var1x) -> {
         return var0.getState() == var1x;
      }));
   }

   public State getCurrent() {
      return this.m_currentState;
   }

   public State getPrevious() {
      return this.m_previousState;
   }

   public int getSubStateCount() {
      return this.m_subStates.size();
   }

   public State getSubStateAt(int var1) {
      return ((StateMachine.SubstateSlot)this.m_subStates.get(var1)).getState();
   }

   public void revertToPreviousState(State var1) {
      if (this.isSubstate(var1)) {
         this.removeSubstate(var1);
      } else if (this.m_currentState != var1) {
         DebugLog.ActionSystem.warn("The sender $s is not an active state in this state machine.", String.valueOf(var1));
      } else {
         this.changeRootState(this.m_previousState, false);
      }
   }

   public void update() {
      if (this.m_currentState != null) {
         this.m_currentState.execute(this.m_owner);
      }

      Lambda.forEachFrom(PZArrayUtil::forEach, (List)this.m_subStates, this.m_owner, (var0, var1) -> {
         if (!var0.isEmpty()) {
            var0.state.execute(var1);
         }

      });
      this.logCurrentState();
   }

   private void logCurrentState() {
      if (this.m_owner.isAnimationRecorderActive()) {
         this.m_owner.getAnimationPlayerRecorder().logAIState(this.m_currentState, this.m_subStates);
      }

   }

   private void stateEnter(State var1) {
      var1.enter(this.m_owner);
   }

   private void stateExit(State var1) {
      var1.exit(this.m_owner);
   }

   public final void stateAnimEvent(int var1, AnimEvent var2) {
      if (var1 == 0) {
         if (this.m_currentState != null) {
            this.m_currentState.animEvent(this.m_owner, var2);
         }

      } else {
         Lambda.forEachFrom(PZArrayUtil::forEach, (List)this.m_subStates, this.m_owner, var2, (var0, var1x, var2x) -> {
            if (!var0.isEmpty()) {
               var0.state.animEvent(var1x, var2x);
            }

         });
      }
   }

   public boolean isLocked() {
      return this.m_isLocked;
   }

   public void setLocked(boolean var1) {
      this.m_isLocked = var1;
   }

   public static class SubstateSlot {
      private State state;
      boolean shouldBeActive;

      SubstateSlot(State var1) {
         this.state = var1;
         this.shouldBeActive = true;
      }

      public State getState() {
         return this.state;
      }

      void setState(State var1) {
         this.state = var1;
      }

      public boolean isEmpty() {
         return this.state == null;
      }
   }
}
