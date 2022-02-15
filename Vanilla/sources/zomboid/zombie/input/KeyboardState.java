package zombie.input;

import org.lwjglx.input.KeyEventQueue;
import org.lwjglx.input.Keyboard;

public final class KeyboardState {
   private boolean m_isCreated = false;
   private boolean[] m_keyDownStates = null;
   private final KeyEventQueue m_keyEventQueue = new KeyEventQueue();
   private boolean m_wasPolled = false;

   public void poll() {
      boolean var1 = !this.m_isCreated;
      this.m_isCreated = this.m_isCreated || Keyboard.isCreated();
      if (this.m_isCreated) {
         if (var1) {
            this.m_keyDownStates = new boolean[256];
         }

         this.m_wasPolled = true;

         for(int var2 = 0; var2 < this.m_keyDownStates.length; ++var2) {
            this.m_keyDownStates[var2] = Keyboard.isKeyDown(var2);
         }

      }
   }

   public boolean wasPolled() {
      return this.m_wasPolled;
   }

   public void set(KeyboardState var1) {
      this.m_isCreated = var1.m_isCreated;
      if (var1.m_keyDownStates != null) {
         if (this.m_keyDownStates == null || this.m_keyDownStates.length != var1.m_keyDownStates.length) {
            this.m_keyDownStates = new boolean[var1.m_keyDownStates.length];
         }

         System.arraycopy(var1.m_keyDownStates, 0, this.m_keyDownStates, 0, this.m_keyDownStates.length);
      } else {
         this.m_keyDownStates = null;
      }

      this.m_wasPolled = var1.m_wasPolled;
   }

   public void reset() {
      this.m_wasPolled = false;
   }

   public boolean isCreated() {
      return this.m_isCreated;
   }

   public boolean isKeyDown(int var1) {
      return this.m_keyDownStates[var1];
   }

   public int getKeyCount() {
      return this.m_keyDownStates.length;
   }

   public KeyEventQueue getEventQueue() {
      return this.m_keyEventQueue;
   }
}
