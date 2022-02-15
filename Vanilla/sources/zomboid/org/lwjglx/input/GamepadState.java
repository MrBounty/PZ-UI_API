package org.lwjglx.input;

import java.nio.ByteBuffer;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.system.MemoryUtil;

public final class GamepadState {
   public boolean bPolled = false;
   public final GLFWGamepadState axesButtons = GLFWGamepadState.malloc();
   public final ByteBuffer hats = MemoryUtil.memAlloc(8);
   public int hatState = 0;

   public void set(GamepadState var1) {
      this.bPolled = var1.bPolled;
      this.axesButtons.set(var1.axesButtons);
      this.hats.clear();
      var1.hats.position(0);
      this.hats.put(var1.hats);
      this.hatState = var1.hatState;
   }

   public void quit() {
      this.axesButtons.free();
      MemoryUtil.memFree(this.hats);
   }
}
