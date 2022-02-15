package zombie.core.input;

import java.util.ArrayList;
import org.lwjglx.input.Controller;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;
import zombie.Lua.LuaEventManager;
import zombie.core.Core;
import zombie.input.ControllerState;
import zombie.input.ControllerStateCache;
import zombie.input.GameKeyboard;
import zombie.input.JoypadManager;

public final class Input {
   public static final int ANY_CONTROLLER = -1;
   private final Controller[] controllers = new Controller[16];
   private final ArrayList newlyConnected = new ArrayList();
   private final ArrayList newlyDisconnected = new ArrayList();
   private final boolean[][] controllerPressed = new boolean[16][15];
   private final boolean[][] controllerWasPressed = new boolean[16][15];
   private final float[][] controllerPov = new float[16][2];
   private final ControllerStateCache m_controllerStateCache = new ControllerStateCache();

   public static String getKeyName(int var0) {
      String var1 = Keyboard.getKeyName(var0);
      if ("LSHIFT".equals(var1)) {
         var1 = "Left SHIFT";
      }

      if ("RSHIFT".equals(var1)) {
         var1 = "Right SHIFT";
      }

      if ("LMENU".equals(var1)) {
         var1 = "Left ALT";
      } else if ("RMENU".equals(var1)) {
         var1 = "Right ALT";
      }

      return var1;
   }

   public static int getKeyCode(String var0) {
      if ("Right SHIFT".equals(var0)) {
         return 54;
      } else if ("Left SHIFT".equals(var0)) {
         return 42;
      } else if ("Left ALT".equals(var0)) {
         return 56;
      } else {
         return "Right ALT".equals(var0) ? 184 : Keyboard.getKeyIndex(var0);
      }
   }

   public int getControllerCount() {
      return this.controllers.length;
   }

   public int getAxisCount(int var1) {
      Controller var2 = this.getController(var1);
      return var2 == null ? 0 : var2.getAxisCount();
   }

   public float getAxisValue(int var1, int var2) {
      Controller var3 = this.getController(var1);
      return var3 == null ? 0.0F : var3.getAxisValue(var2);
   }

   public String getAxisName(int var1, int var2) {
      Controller var3 = this.getController(var1);
      return var3 == null ? null : var3.getAxisName(var2);
   }

   public boolean isControllerLeftD(int var1) {
      if (var1 == -1) {
         for(int var3 = 0; var3 < this.controllers.length; ++var3) {
            if (this.isControllerLeftD(var3)) {
               return true;
            }
         }

         return false;
      } else {
         Controller var2 = this.getController(var1);
         if (var2 == null) {
            return false;
         } else {
            return var2.getPovX() < -0.5F;
         }
      }
   }

   public boolean isControllerRightD(int var1) {
      if (var1 == -1) {
         for(int var3 = 0; var3 < this.controllers.length; ++var3) {
            if (this.isControllerRightD(var3)) {
               return true;
            }
         }

         return false;
      } else {
         Controller var2 = this.getController(var1);
         if (var2 == null) {
            return false;
         } else {
            return var2.getPovX() > 0.5F;
         }
      }
   }

   public boolean isControllerUpD(int var1) {
      if (var1 == -1) {
         for(int var3 = 0; var3 < this.controllers.length; ++var3) {
            if (this.isControllerUpD(var3)) {
               return true;
            }
         }

         return false;
      } else {
         Controller var2 = this.getController(var1);
         if (var2 == null) {
            return false;
         } else {
            return var2.getPovY() < -0.5F;
         }
      }
   }

   public boolean isControllerDownD(int var1) {
      if (var1 == -1) {
         for(int var3 = 0; var3 < this.controllers.length; ++var3) {
            if (this.isControllerDownD(var3)) {
               return true;
            }
         }

         return false;
      } else {
         Controller var2 = this.getController(var1);
         if (var2 == null) {
            return false;
         } else {
            return var2.getPovY() > 0.5F;
         }
      }
   }

   private Controller checkControllerButton(int var1, int var2) {
      Controller var3 = this.getController(var1);
      if (var3 == null) {
         return null;
      } else {
         return var2 >= 0 && var2 < var3.getButtonCount() ? var3 : null;
      }
   }

   public boolean isButtonPressedD(int var1, int var2) {
      if (var2 == -1) {
         for(int var4 = 0; var4 < this.controllers.length; ++var4) {
            if (this.isButtonPressedD(var1, var4)) {
               return true;
            }
         }

         return false;
      } else {
         Controller var3 = this.checkControllerButton(var2, var1);
         return var3 == null ? false : this.controllerPressed[var2][var1];
      }
   }

   public boolean wasButtonPressed(int var1, int var2) {
      Controller var3 = this.checkControllerButton(var1, var2);
      return var3 == null ? false : this.controllerWasPressed[var1][var2];
   }

   public boolean isButtonStartPress(int var1, int var2) {
      return !this.wasButtonPressed(var1, var2) && this.isButtonPressedD(var2, var1);
   }

   public boolean isButtonReleasePress(int var1, int var2) {
      return this.wasButtonPressed(var1, var2) && !this.isButtonPressedD(var2, var1);
   }

   public void initControllers() {
      this.updateGameThread();
   }

   private void onControllerConnected(Controller var1) {
      JoypadManager.instance.onControllerConnected(var1);
      LuaEventManager.triggerEvent("OnGamepadConnect", var1.getID());
   }

   private void onControllerDisconnected(Controller var1) {
      JoypadManager.instance.onControllerDisconnected(var1);
      LuaEventManager.triggerEvent("OnGamepadDisconnect", var1.getID());
   }

   public void poll() {
      if (!Core.getInstance().isDoingTextEntry()) {
         while(true) {
            if (GameKeyboard.getEventQueuePolling().next()) {
               continue;
            }
         }
      }

      while(Mouse.next()) {
      }

      this.m_controllerStateCache.poll();
   }

   public Controller getController(int var1) {
      return var1 >= 0 && var1 < this.controllers.length ? this.controllers[var1] : null;
   }

   public int getButtonCount(int var1) {
      Controller var2 = this.getController(var1);
      return var2 == null ? null : var2.getButtonCount();
   }

   public String getButtonName(int var1, int var2) {
      Controller var3 = this.getController(var1);
      return var3 == null ? null : var3.getButtonName(var2);
   }

   public void updateGameThread() {
      if (!this.m_controllerStateCache.getState().isCreated()) {
         this.m_controllerStateCache.swap();
      } else {
         ControllerState var1 = this.m_controllerStateCache.getState();
         int var2;
         Controller var3;
         if (this.checkConnectDisconnect(var1)) {
            for(var2 = 0; var2 < this.newlyDisconnected.size(); ++var2) {
               var3 = (Controller)this.newlyDisconnected.get(var2);
               this.onControllerDisconnected(var3);
            }

            for(var2 = 0; var2 < this.newlyConnected.size(); ++var2) {
               var3 = (Controller)this.newlyConnected.get(var2);
               this.onControllerConnected(var3);
            }
         }

         for(var2 = 0; var2 < this.getControllerCount(); ++var2) {
            var3 = this.getController(var2);
            if (var3 != null) {
               int var4 = var3.getButtonCount();

               int var5;
               for(var5 = 0; var5 < var4; ++var5) {
                  this.controllerWasPressed[var2][var5] = this.controllerPressed[var2][var5];
                  if (this.controllerPressed[var2][var5] && !var3.isButtonPressed(var5)) {
                     this.controllerPressed[var2][var5] = false;
                  } else if (!this.controllerPressed[var2][var5] && var3.isButtonPressed(var5)) {
                     this.controllerPressed[var2][var5] = true;
                     JoypadManager.instance.onPressed(var2, var5);
                  }
               }

               var4 = var3.getAxisCount();

               float var6;
               for(var5 = 0; var5 < var4; ++var5) {
                  var6 = var3.getAxisValue(var5);
                  if ((!var3.isGamepad() || var5 != 4) && var5 != 5) {
                     if (var6 < -0.5F) {
                        JoypadManager.instance.onPressedAxisNeg(var2, var5);
                     }

                     if (var6 > 0.5F) {
                        JoypadManager.instance.onPressedAxis(var2, var5);
                     }
                  } else if (var6 > 0.0F) {
                     JoypadManager.instance.onPressedTrigger(var2, var5);
                  }
               }

               float var7 = var3.getPovX();
               var6 = var3.getPovY();
               if (var7 != this.controllerPov[var2][0] || var6 != this.controllerPov[var2][1]) {
                  this.controllerPov[var2][0] = var7;
                  this.controllerPov[var2][1] = var6;
                  JoypadManager.instance.onPressedPov(var2);
               }
            }
         }

         this.m_controllerStateCache.swap();
      }
   }

   private boolean checkConnectDisconnect(ControllerState var1) {
      boolean var2 = false;
      this.newlyConnected.clear();
      this.newlyDisconnected.clear();

      for(int var3 = 0; var3 < 16; ++var3) {
         Controller var4 = var1.getController(var3);
         if (var4 != this.controllers[var3]) {
            var2 = true;
            if (var4 != null && var4.isGamepad()) {
               this.newlyConnected.add(var4);
            } else {
               if (this.controllers[var3] != null) {
                  this.newlyDisconnected.add(this.controllers[var3]);
               }

               var4 = null;
            }

            this.controllers[var3] = var4;
         }
      }

      return var2;
   }

   public void quit() {
      this.m_controllerStateCache.quit();
   }
}
