package zombie.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;

public final class Clipboard {
   private static Thread MainThread = null;
   private static String PreviousKnownValue = null;
   private static String DelaySetMainThread = null;

   public static void initMainThread() {
      MainThread = Thread.currentThread();
      PreviousKnownValue = getClipboard();
   }

   public static void rememberCurrentValue() {
      if (Thread.currentThread() == MainThread) {
         GLFWErrorCallback var0 = GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)null);

         try {
            PreviousKnownValue = new String(GLFW.glfwGetClipboardString(0L));
         } catch (Throwable var5) {
            PreviousKnownValue = "";
         } finally {
            GLFW.glfwSetErrorCallback(var0);
         }
      }

   }

   public static synchronized String getClipboard() {
      if (Thread.currentThread() == MainThread) {
         GLFWErrorCallback var0 = GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)null);

         String var2;
         try {
            String var1 = PreviousKnownValue = new String(GLFW.glfwGetClipboardString(0L));
            return var1;
         } catch (Throwable var6) {
            PreviousKnownValue = "";
            var2 = "";
         } finally {
            GLFW.glfwSetErrorCallback(var0);
         }

         return var2;
      } else {
         return PreviousKnownValue;
      }
   }

   public static synchronized void setClipboard(String var0) {
      PreviousKnownValue = var0;
      if (Thread.currentThread() == MainThread) {
         GLFW.glfwSetClipboardString(0L, var0);
      } else {
         DelaySetMainThread = var0;
      }

   }

   public static synchronized void updateMainThread() {
      if (DelaySetMainThread != null) {
         setClipboard(DelaySetMainThread);
         DelaySetMainThread = null;
      }

   }
}
