package org.lwjglx;

import org.lwjgl.glfw.GLFW;

public class Sys {
   public static long getTimerResolution() {
      return 1000L;
   }

   public static long getTime() {
      return (long)(GLFW.glfwGetTime() * 1000.0D);
   }

   public static long getNanoTime() {
      return (long)(GLFW.glfwGetTime() * 1.0E9D);
   }
}
