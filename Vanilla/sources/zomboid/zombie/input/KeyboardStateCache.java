package zombie.input;

public final class KeyboardStateCache {
   private final Object m_lock = "KeyboardStateCache Lock";
   private int m_stateIndexUsing = 0;
   private int m_stateIndexPolling = 1;
   private final KeyboardState[] m_states = new KeyboardState[]{new KeyboardState(), new KeyboardState()};

   public void poll() {
      synchronized(this.m_lock) {
         KeyboardState var2 = this.getStatePolling();
         if (!var2.wasPolled()) {
            var2.poll();
         }
      }
   }

   public void swap() {
      synchronized(this.m_lock) {
         if (this.getStatePolling().wasPolled()) {
            this.m_stateIndexUsing = this.m_stateIndexPolling;
            this.m_stateIndexPolling = this.m_stateIndexPolling == 1 ? 0 : 1;
            this.getStatePolling().set(this.getState());
            this.getStatePolling().reset();
         }
      }
   }

   public KeyboardState getState() {
      synchronized(this.m_lock) {
         return this.m_states[this.m_stateIndexUsing];
      }
   }

   public KeyboardState getStatePolling() {
      synchronized(this.m_lock) {
         return this.m_states[this.m_stateIndexPolling];
      }
   }
}
