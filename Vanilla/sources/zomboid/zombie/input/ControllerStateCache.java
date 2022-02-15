package zombie.input;

public class ControllerStateCache {
   private final Object m_lock = "ControllerStateCache Lock";
   private int m_stateIndexUsing = 0;
   private int m_stateIndexPolling = 1;
   private final ControllerState[] m_states = new ControllerState[]{new ControllerState(), new ControllerState()};

   public void poll() {
      synchronized(this.m_lock) {
         ControllerState var2 = this.getStatePolling();
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

   public ControllerState getState() {
      synchronized(this.m_lock) {
         return this.m_states[this.m_stateIndexUsing];
      }
   }

   private ControllerState getStatePolling() {
      synchronized(this.m_lock) {
         return this.m_states[this.m_stateIndexPolling];
      }
   }

   public void quit() {
      this.m_states[0].quit();
      this.m_states[1].quit();
   }
}
