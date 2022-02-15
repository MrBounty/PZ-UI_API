package org.lwjglx.input;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class Controller {
   private final String joystickName;
   private final String gamepadName;
   private final int buttonsCount;
   private final int axisCount;
   private final int hatCount;
   private final int id;
   private final boolean isGamepad;
   private final String guid;
   private final float[] deadZone;
   public GamepadState gamepadState = null;
   private static final String[] axisNames = new String[]{"left stick X", "left stick Y", "right stick X", "right stick Y", "left trigger", "right trigger"};
   private static final String[] buttonNames = new String[]{"A", "B", "X", "Y", "left bumper", "right bumper", "back", "start", "guide", "left stick", "right stick", "d-pad up", "d-pad right", "d-pad down", "d-pad left"};

   public Controller(int var1) {
      this.id = var1;
      String var2 = GLFW.glfwGetJoystickName(var1);
      if (var2 == null) {
         var2 = "ControllerName" + var1;
      }

      this.joystickName = var2;
      String var3 = GLFW.glfwGetGamepadName(var1);
      if (var3 == null) {
         var3 = "GamepadName" + var1;
      }

      this.gamepadName = var3;
      this.isGamepad = GLFW.glfwJoystickIsGamepad(var1);
      if (this.isGamepad) {
         this.axisCount = 6;
         this.buttonsCount = 15;
      } else {
         FloatBuffer var4 = GLFW.glfwGetJoystickAxes(var1);
         this.axisCount = var4 == null ? 0 : var4.remaining();
         ByteBuffer var5 = GLFW.glfwGetJoystickButtons(var1);
         this.buttonsCount = var5 == null ? 0 : var5.remaining();
      }

      ByteBuffer var6 = GLFW.glfwGetJoystickHats(var1);
      this.hatCount = var6 == null ? 0 : var6.remaining();
      this.guid = GLFW.glfwGetJoystickGUID(var1);
      this.deadZone = new float[this.axisCount];
      Arrays.fill(this.deadZone, 0.2F);
   }

   public int getID() {
      return this.id;
   }

   public String getGUID() {
      return this.guid;
   }

   public boolean isGamepad() {
      return this.isGamepad;
   }

   public String getJoystickName() {
      return this.joystickName;
   }

   public String getGamepadName() {
      return this.gamepadName;
   }

   public int getAxisCount() {
      return this.axisCount;
   }

   public float getAxisValue(int var1) {
      if (this.gamepadState != null && this.gamepadState.bPolled) {
         return var1 >= 0 && var1 < 15 ? this.gamepadState.axesButtons.axes(var1) : 0.0F;
      } else {
         return 0.0F;
      }
   }

   public int getButtonCount() {
      return this.buttonsCount;
   }

   public int getHatCount() {
      return this.hatCount;
   }

   public int getHatState() {
      return this.gamepadState != null && this.gamepadState.bPolled ? this.gamepadState.hatState : 0;
   }

   public ByteBuffer getJoystickHats(int var1, ByteBuffer var2) {
      MemoryStack var3 = MemoryStack.stackGet();
      int var4 = var3.getPointer();
      IntBuffer var5 = var3.callocInt(1);

      ByteBuffer var8;
      try {
         long var6 = GLFW.nglfwGetJoystickHats(var1, MemoryUtil.memAddress(var5));
         var2.clear();
         var2.limit(var5.get(0));
         if (var6 != 0L) {
            MemoryUtil.memCopy(var6, MemoryUtil.memAddress(var2), (long)var5.get(0));
         }

         var8 = var2;
      } finally {
         var3.setPointer(var4);
      }

      return var8;
   }

   public String getAxisName(int var1) {
      return axisNames[var1];
   }

   public float getXAxisValue() {
      return this.getAxisValue(0);
   }

   public float getYAxisValue() {
      return this.getAxisValue(1);
   }

   public float getDeadZone(int var1) {
      return this.deadZone[var1];
   }

   public void setDeadZone(int var1, float var2) {
      this.deadZone[var1] = var2;
   }

   public float getPovX() {
      if (this.gamepadState != null && this.gamepadState.bPolled) {
         if ((this.gamepadState.hatState & 8) != 0) {
            return -1.0F;
         } else {
            return (this.gamepadState.hatState & 2) != 0 ? 1.0F : 0.0F;
         }
      } else {
         return 0.0F;
      }
   }

   public float getPovY() {
      if (this.gamepadState != null && this.gamepadState.bPolled) {
         if ((this.gamepadState.hatState & 1) != 0) {
            return -1.0F;
         } else {
            return (this.gamepadState.hatState & 4) != 0 ? 1.0F : 0.0F;
         }
      } else {
         return 0.0F;
      }
   }

   public boolean isButtonPressed(int var1) {
      if (this.gamepadState != null && this.gamepadState.bPolled) {
         if (var1 >= 0 && var1 < 15) {
            return this.gamepadState.axesButtons.buttons(var1) == 1;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isButtonRelease(int var1) {
      if (this.gamepadState != null && this.gamepadState.bPolled) {
         if (var1 >= 0 && var1 < 15) {
            return this.gamepadState.axesButtons.buttons(var1) == 0;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public String getButtonName(int var1) {
      if (var1 >= buttonNames.length) {
         int var10000 = var1 - buttonNames.length;
         return "Extra button " + (var10000 + 1);
      } else {
         return buttonNames[var1];
      }
   }

   public void poll(GamepadState var1) {
      if (GLFW.glfwGetGamepadState(this.id, var1.axesButtons)) {
         var1.bPolled = true;
         ByteBuffer var2 = this.getJoystickHats(this.id, var1.hats);
         var1.hatState = var2.remaining() == 0 ? 0 : var2.get(0);
      } else {
         var1.bPolled = false;
      }

   }
}
