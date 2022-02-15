package zombie.input;

import org.lwjglx.input.Controller;
import org.lwjglx.input.Controllers;
import org.lwjglx.input.GamepadState;

public class ControllerState {
   private boolean m_isCreated = false;
   private boolean m_wasPolled = false;
   private final Controller[] m_controllers = new Controller[16];
   private final GamepadState[] m_gamepadState = new GamepadState[16];

   ControllerState() {
      for(int var1 = 0; var1 < this.m_controllers.length; ++var1) {
         this.m_gamepadState[var1] = new GamepadState();
      }

   }

   public void poll() {
      boolean var1 = !this.m_isCreated;
      this.m_isCreated = this.m_isCreated || Controllers.isCreated();
      if (this.m_isCreated) {
         if (var1) {
         }

         this.m_wasPolled = true;
         Controllers.poll(this.m_gamepadState);

         for(int var2 = 0; var2 < Controllers.getControllerCount(); ++var2) {
            this.m_controllers[var2] = Controllers.getController(var2);
         }

      }
   }

   public boolean wasPolled() {
      return this.m_wasPolled;
   }

   public void set(ControllerState var1) {
      this.m_isCreated = var1.m_isCreated;

      for(int var2 = 0; var2 < this.m_controllers.length; ++var2) {
         this.m_controllers[var2] = var1.m_controllers[var2];
         if (this.m_controllers[var2] != null) {
            this.m_gamepadState[var2].set(var1.m_gamepadState[var2]);
            this.m_controllers[var2].gamepadState = this.m_gamepadState[var2];
         }
      }

      this.m_wasPolled = var1.m_wasPolled;
   }

   public void reset() {
      this.m_wasPolled = false;
   }

   public boolean isCreated() {
      return this.m_isCreated;
   }

   public Controller getController(int var1) {
      return this.m_controllers[var1];
   }

   public void quit() {
      for(int var1 = 0; var1 < this.m_controllers.length; ++var1) {
         this.m_gamepadState[var1].quit();
      }

   }
}
