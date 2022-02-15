package org.lwjglx.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;
import zombie.ZomboidFileSystem;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;

public class Controllers {
   public static final int MAX_AXES = 6;
   public static final int MAX_BUTTONS = 15;
   public static final int MAX_CONTROLLERS = 16;
   private static final Controller[] controllers = new Controller[16];
   private static boolean isCreated = false;
   private static Consumer controllerConnectedCallback = null;
   private static Consumer controllerDisconnectedCallback = null;
   private static int debugToggleControllerPluggedIn = -1;

   public static void create() {
      readGameControllerDB();
      GLFW.glfwSetJoystickCallback(Controllers::updateControllersCount);

      for(int var0 = 0; var0 < 16; ++var0) {
         if (GLFW.glfwJoystickPresent(var0)) {
            controllers[var0] = new Controller(var0);
         }
      }

      isCreated = true;
   }

   private static void readGameControllerDB() {
      File var0 = (new File("./media/gamecontrollerdb.txt")).getAbsoluteFile();
      if (var0.exists()) {
         readGameControllerDB(var0);
      }

      String var1 = ZomboidFileSystem.instance.getCacheDirSub("joypads" + File.separator + "gamecontrollerdb.txt");
      var0 = new File(var1);
      if (var0.exists()) {
         readGameControllerDB(var0);
      }

   }

   private static void readGameControllerDB(File var0) {
      try {
         FileReader var1 = new FileReader(var0);

         try {
            BufferedReader var2 = new BufferedReader(var1);

            try {
               StringBuilder var3 = new StringBuilder();

               String var4;
               while((var4 = var2.readLine()) != null) {
                  if (!var4.startsWith("#")) {
                     var3.append(var4);
                     var3.append(System.lineSeparator());
                  }
               }

               ByteBuffer var5 = MemoryUtil.memUTF8(var3.toString());
               if (GLFW.glfwUpdateGamepadMappings(var5)) {
               }

               MemoryUtil.memFree(var5);
            } catch (Throwable var8) {
               try {
                  var2.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            var2.close();
         } catch (Throwable var9) {
            try {
               var1.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }

            throw var9;
         }

         var1.close();
      } catch (Exception var10) {
         ExceptionLogger.logException(var10);
      }

   }

   public static void setControllerConnectedCallback(Consumer var0) {
      controllerConnectedCallback = var0;
   }

   public static void setControllerDisconnectedCallback(Consumer var0) {
      controllerDisconnectedCallback = var0;
   }

   public static int getControllerCount() {
      if (!isCreated()) {
         throw new RuntimeException("Before calling 'getJoypadCount()' you should call 'create()' method");
      } else {
         return controllers.length;
      }
   }

   public static Controller getController(int var0) {
      if (!isCreated()) {
         throw new RuntimeException("Before calling 'getJoypad(int)' you should call 'create()' method");
      } else {
         return controllers[var0];
      }
   }

   public static boolean isCreated() {
      return isCreated;
   }

   public static void poll(GamepadState[] var0) {
      if (!isCreated()) {
         throw new RuntimeException("Before calling 'poll()' you should call 'create()' method");
      } else {
         int var1;
         if (Core.bDebug && debugToggleControllerPluggedIn >= 0 && debugToggleControllerPluggedIn < 16) {
            var1 = debugToggleControllerPluggedIn;
            debugToggleControllerPluggedIn = -1;
            if (controllers[var1] != null) {
               updateControllersCount(var1, 262146);
            } else if (GLFW.glfwJoystickIsGamepad(var1)) {
               updateControllersCount(var1, 262145);
            }
         }

         for(var1 = 0; var1 < controllers.length; ++var1) {
            Controller var2 = controllers[var1];
            if (var2 != null) {
               var2.poll(var0[var1]);
            }
         }

      }
   }

   private static void updateControllersCount(int var0, int var1) {
      if (var1 == 262145) {
         Controller var2 = new Controller(var0);
         controllers[var0] = var2;
         if (controllerConnectedCallback != null) {
            controllerConnectedCallback.accept(var0);
         }
      } else if (var1 == 262146) {
         controllers[var0] = null;
         if (controllerDisconnectedCallback != null) {
            controllerDisconnectedCallback.accept(var0);
         }
      }

   }

   public static void setDebugToggleControllerPluggedIn(int var0) {
      debugToggleControllerPluggedIn = var0;
   }
}
