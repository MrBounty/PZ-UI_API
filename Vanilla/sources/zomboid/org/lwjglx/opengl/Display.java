package org.lwjglx.opengl;

import java.awt.Canvas;
import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowIconifyCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowRefreshCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryStack;
import org.lwjglx.LWJGLException;
import org.lwjglx.LWJGLUtil;
import org.lwjglx.input.Keyboard;
import org.lwjglx.input.Mouse;
import zombie.core.Clipboard;
import zombie.core.Core;
import zombie.core.math.PZMath;
import zombie.core.opengl.RenderThread;
import zombie.debug.DebugLog;

public class Display {
   private static String windowTitle = "Game";
   private static boolean displayCreated = false;
   private static boolean displayFocused = false;
   private static boolean displayVisible = true;
   private static boolean displayDirty = false;
   private static boolean displayResizable = true;
   private static boolean vsyncEnabled = true;
   private static DisplayMode gameWindowMode = new DisplayMode(640, 480);
   private static DisplayMode desktopDisplayMode = new DisplayMode(640, 480);
   private static int displayX = 0;
   private static int displayY = 0;
   private static boolean displayResized = false;
   private static int displayWidth = 0;
   private static int displayHeight = 0;
   private static int displayFramebufferWidth = 0;
   private static int displayFramebufferHeight = 0;
   private static Buffer displayIcons;
   private static long monitor;
   private static boolean isBorderlessWindow = false;
   private static boolean latestResized = false;
   private static int latestWidth = 0;
   private static int latestHeight = 0;
   public static GLCapabilities capabilities;
   private static final double[] mouseCursorPosX;
   private static final double[] mouseCursorPosY;
   private static int mouseCursorState;

   public static void create(PixelFormat var0) throws LWJGLException {
      GLFW.glfwWindowHint(135178, var0.getAccumulationBitsPerPixel());
      GLFW.glfwWindowHint(135172, var0.getAlphaBits());
      GLFW.glfwWindowHint(135179, var0.getAuxBuffers());
      GLFW.glfwWindowHint(135173, var0.getDepthBits());
      GLFW.glfwWindowHint(135181, var0.getSamples());
      GLFW.glfwWindowHint(135174, var0.getStencilBits());
      create();
   }

   public static void create() throws LWJGLException {
      if (Display.Window.handle != 0L) {
         GLFW.glfwDestroyWindow(Display.Window.handle);
      }

      GLFWVidMode var0 = GLFW.glfwGetVideoMode(monitor);
      int var1 = var0.width();
      int var2 = var0.height();
      int var3 = var0.redBits() + var0.greenBits() + var0.blueBits();
      int var4 = var0.refreshRate();
      desktopDisplayMode = new DisplayMode(var1, var2, var3, var4);
      GLFW.glfwDefaultWindowHints();
      GLFW.glfwWindowHint(139265, 196609);
      Display.Callbacks.errorCallback = GLFWErrorCallback.createPrint(System.err);
      GLFW.glfwSetErrorCallback(Display.Callbacks.errorCallback);
      GLFW.glfwWindowHint(131076, 0);
      GLFW.glfwWindowHint(131075, displayResizable ? 1 : 0);
      if (LWJGLUtil.getPlatform() == 2) {
         GLFW.glfwWindowHint(143361, 0);
      }

      boolean var5 = Core.bDebug && "true".equalsIgnoreCase(System.getProperty("org.lwjgl.util.Debug"));
      GLFW.glfwWindowHint(139271, var5 ? 1 : 0);
      if (Core.getInstance().getOptionBorderlessWindow()) {
         isBorderlessWindow = true;
         GLFW.glfwWindowHint(131077, 0);
         Display.Window.handle = GLFW.glfwCreateWindow(gameWindowMode.getWidth(), gameWindowMode.getHeight(), windowTitle, 0L, 0L);
      } else if (isFullscreen()) {
         Display.Window.handle = GLFW.glfwCreateWindow(gameWindowMode.getWidth(), gameWindowMode.getHeight(), windowTitle, monitor, 0L);
      } else {
         Display.Window.handle = GLFW.glfwCreateWindow(gameWindowMode.getWidth(), gameWindowMode.getHeight(), windowTitle, 0L, 0L);
      }

      if (Display.Window.handle == 0L) {
         throw new IllegalStateException("Failed to create Display window");
      } else {
         GLFW.glfwSetWindowIcon(Display.Window.handle, displayIcons);
         Display.Callbacks.bNoise = var5;
         Display.Callbacks.initCallbacks();
         calcWindowPos(isBorderlessWindow() || isFullscreen());
         GLFW.glfwSetWindowPos(Display.Window.handle, displayX, displayY);
         GLFW.glfwShowWindow(Display.Window.handle);
         GLFW.glfwMakeContextCurrent(Display.Window.handle);
         capabilities = GL.createCapabilities();
         GLFW.glfwSwapInterval(0);
         GL11.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
         GL11.glClear(16640);
         GLFW.glfwSwapBuffers(Display.Window.handle);
         setVSyncEnabled(vsyncEnabled);
         int[] var6;
         if (var5 && capabilities.OpenGL43) {
            var6 = new int[]{131185};
            GL43.glDebugMessageControl(33350, 33361, 4352, var6, false);
         }

         var6 = new int[1];
         int[] var7 = new int[1];
         GLFW.glfwGetWindowSize(Display.Window.handle, var6, var7);
         displayWidth = latestWidth = var6[0];
         displayHeight = latestHeight = var7[0];
         displayCreated = true;
      }
   }

   public static boolean isCreated() {
      return displayCreated;
   }

   public static boolean isActive() {
      return displayFocused;
   }

   public static boolean isVisible() {
      return displayVisible;
   }

   public static void setLocation(int var0, int var1) {
      System.out.println("TODO: Implement Display.setLocation(int, int)");
   }

   public static void setVSyncEnabled(boolean var0) {
      vsyncEnabled = var0;
      if (var0) {
         GLFW.glfwSwapInterval(1);
      } else {
         GLFW.glfwSwapInterval(0);
      }

   }

   public static long getWindow() {
      return Display.Window.handle;
   }

   public static void update() {
      update(true);
   }

   public static void update(boolean var0) {
      try {
         swapBuffers();
         displayDirty = false;
      } catch (LWJGLException var2) {
         throw new RuntimeException(var2);
      }

      if (var0) {
         processMessages();
      }

   }

   private static void updateMouseCursor() {
      int var0 = RenderThread.isCursorVisible() ? 212993 : 212994;
      boolean var1 = Core.getInstance().getOptionLockCursorToWindow();
      if (var1) {
         var0 = 212995;
      }

      if (mouseCursorState != var0) {
         boolean var2 = mouseCursorState == 212995;
         if (var2) {
            GLFW.glfwGetCursorPos(getWindow(), mouseCursorPosX, mouseCursorPosY);
         }

         mouseCursorState = var0;
         GLFW.glfwSetInputMode(getWindow(), 208897, var0);
         if (var2) {
            GLFW.glfwSetCursorPos(getWindow(), mouseCursorPosX[0], mouseCursorPosY[0]);
         }
      }

      if (var1) {
         GLFW.glfwGetCursorPos(getWindow(), mouseCursorPosX, mouseCursorPosY);
         int var4 = (int)mouseCursorPosX[0];
         int var3 = (int)mouseCursorPosY[0];
         mouseCursorPosX[0] = (double)PZMath.clamp((int)mouseCursorPosX[0], 0, getWidth());
         mouseCursorPosY[0] = (double)PZMath.clamp((int)mouseCursorPosY[0], 0, getHeight());
         if (var4 != (int)mouseCursorPosX[0] || var3 != (int)mouseCursorPosY[0]) {
            GLFW.glfwSetCursorPos(getWindow(), mouseCursorPosX[0], mouseCursorPosY[0]);
         }
      }

   }

   public static void processMessages() {
      GLFW.glfwPollEvents();
      Keyboard.poll();
      Mouse.poll();
      updateMouseCursor();
      if (latestResized) {
         latestResized = false;
         displayResized = true;
         displayWidth = latestWidth;
         displayHeight = latestHeight;
      } else {
         displayResized = false;
      }

   }

   public static void swapBuffers() throws LWJGLException {
      GLFW.glfwSwapBuffers(Display.Window.handle);
   }

   public static void destroy() {
      Display.Callbacks.releaseCallbacks();
      GLFW.glfwDestroyWindow(Display.Window.handle);
      displayCreated = false;
   }

   public static void setDisplayModeAndFullscreen(DisplayMode var0) throws LWJGLException {
      setDisplayModeAndFullscreenInternal(var0, var0.isFullscreenCapable());
   }

   public static void setFullscreen(boolean var0) {
      setDisplayModeAndFullscreenInternal(gameWindowMode, var0);
   }

   public static boolean isFullscreen() {
      if (!isCreated()) {
         return Core.getInstance().isFullScreen();
      } else {
         return GLFW.glfwGetWindowMonitor(Display.Window.handle) != 0L;
      }
   }

   public static void setBorderlessWindow(boolean var0) {
      isBorderlessWindow = var0;
      if (isCreated()) {
         GLFW.glfwSetWindowAttrib(getWindow(), 131077, var0 ? 0 : 1);
      }

   }

   public static boolean isBorderlessWindow() {
      return isBorderlessWindow;
   }

   public static void setDisplayMode(DisplayMode var0) throws LWJGLException {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         setDisplayModeAndFullscreenInternal(var0, var0.isFullscreenCapable() && isFullscreen());
      }
   }

   private static void setDisplayModeAndFullscreenInternal(DisplayMode var0, boolean var1) {
      boolean var2 = isFullscreen();
      DisplayMode var3 = gameWindowMode;
      gameWindowMode = var0;
      Core.setFullScreen(var1);
      if (isCreated() && (var2 != var1 || !gameWindowMode.equals(var3))) {
         GLFW.glfwHideWindow(Display.Window.handle);
         calcWindowPos(var1 || isBorderlessWindow());
         GLFW.glfwSetWindowMonitor(Display.Window.handle, var1 ? monitor : 0L, displayX, displayY, gameWindowMode.getWidth(), gameWindowMode.getHeight(), -1);
         GLFW.glfwSetWindowIcon(Display.Window.handle, displayIcons);
         GLFW.glfwShowWindow(Display.Window.handle);
         GLFW.glfwFocusWindow(Display.Window.handle);
         GLFW.glfwMakeContextCurrent(Display.Window.handle);
         GL11.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
         GLFW.glfwSwapInterval(0);
         GL11.glClear(16640);
         GLFW.glfwSwapBuffers(Display.Window.handle);
         setVSyncEnabled(vsyncEnabled);
      }

   }

   private static void calcWindowPos(boolean var0) {
      MemoryStack var1 = MemoryStack.stackPush();

      try {
         IntBuffer var2 = var1.callocInt(1);
         IntBuffer var3 = var1.callocInt(1);
         GLFW.glfwGetFramebufferSize(Display.Window.handle, var2, var3);
         displayFramebufferWidth = var2.get(0);
         displayFramebufferHeight = var3.get(0);
         IntBuffer var4 = var1.callocInt(1);
         IntBuffer var5 = var1.callocInt(1);
         GLFW.glfwGetWindowFrameSize(Display.Window.handle, var4, var5, (IntBuffer)null, (IntBuffer)null);
         int var6 = var4.get(0);
         int var7 = var5.get(0);
         displayWidth = gameWindowMode.getWidth();
         displayHeight = gameWindowMode.getHeight();
         if (var0) {
            var6 = 0;
            var7 = 0;
         }

         displayX = var6 + (desktopDisplayMode.getWidth() - gameWindowMode.getWidth()) / 2;
         displayY = var7 + (desktopDisplayMode.getHeight() - gameWindowMode.getHeight()) / 2;
         if (gameWindowMode.getWidth() > desktopDisplayMode.getWidth()) {
            displayX = var6;
         }

         if (gameWindowMode.getHeight() > desktopDisplayMode.getHeight()) {
            displayY = var7;
         }
      } catch (Throwable var9) {
         if (var1 != null) {
            try {
               var1.close();
            } catch (Throwable var8) {
               var9.addSuppressed(var8);
            }
         }

         throw var9;
      }

      if (var1 != null) {
         var1.close();
      }

   }

   public static DisplayMode getDisplayMode() {
      return gameWindowMode;
   }

   public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
      org.lwjgl.glfw.GLFWVidMode.Buffer var0 = GLFW.glfwGetVideoModes(GLFW.glfwGetPrimaryMonitor());
      DisplayMode[] var1 = new DisplayMode[var0.capacity()];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var0.position(var2);
         int var3 = var0.width();
         int var4 = var0.height();
         int var5 = var0.redBits() + var0.greenBits() + var0.blueBits();
         int var6 = var0.refreshRate();
         var1[var2] = new DisplayMode(var3, var4, var5, var6);
      }

      return var1;
   }

   public static DisplayMode getDesktopDisplayMode() {
      return desktopDisplayMode;
   }

   public static boolean wasResized() {
      return displayResized;
   }

   public static int getX() {
      return displayX;
   }

   public static int getY() {
      return displayY;
   }

   public static int getWidth() {
      return latestWidth;
   }

   public static int getHeight() {
      return latestHeight;
   }

   public static int getFramebufferWidth() {
      return displayFramebufferWidth;
   }

   public static int getFramebufferHeight() {
      return displayFramebufferHeight;
   }

   public static void setTitle(String var0) {
      windowTitle = var0;
      if (isCreated()) {
         GLFW.glfwSetWindowTitle(Display.Window.handle, windowTitle);
      }

   }

   public static boolean isCloseRequested() {
      return GLFW.glfwWindowShouldClose(Display.Window.handle);
   }

   public static boolean isDirty() {
      return displayDirty;
   }

   public static void setInitialBackground(float var0, float var1, float var2) {
      System.out.println("TODO: Implement Display.setInitialBackground(float, float, float)");
   }

   public static void setIcon(Buffer var0) {
      displayIcons = var0;
   }

   public static void setResizable(boolean var0) {
      displayResizable = var0;
   }

   public static boolean isResizable() {
      return displayResizable;
   }

   public static void setParent(Canvas var0) throws LWJGLException {
   }

   public static void releaseContext() throws LWJGLException {
      GLFW.glfwMakeContextCurrent(0L);
   }

   public static boolean isCurrent() throws LWJGLException {
      return GLFW.glfwGetCurrentContext() == Display.Window.handle;
   }

   public static void makeCurrent() throws LWJGLException {
      GLFW.glfwMakeContextCurrent(Display.Window.handle);
      GL.setCapabilities(capabilities);
   }

   public static String getAdapter() {
      return "GeNotSupportedAdapter";
   }

   public static String getVersion() {
      return "1.0 NOT SUPPORTED";
   }

   public static void sync(int var0) {
      Sync.sync(var0);
   }

   static {
      if (!GLFW.glfwInit()) {
         throw new IllegalStateException("Unable to initialize GLFW");
      } else {
         GLFW.glfwInitHint(327681, 0);
         Keyboard.create();
         monitor = GLFW.glfwGetPrimaryMonitor();
         GLFWVidMode var0 = GLFW.glfwGetVideoMode(monitor);
         int var1 = var0.width();
         int var2 = var0.height();
         int var3 = var0.redBits() + var0.greenBits() + var0.blueBits();
         int var4 = var0.refreshRate();
         desktopDisplayMode = new DisplayMode(var1, var2, var3, var4);
         mouseCursorPosX = new double[1];
         mouseCursorPosY = new double[1];
         mouseCursorState = -1;
      }
   }

   private static final class Window {
      static long handle;
   }

   private static final class Callbacks {
      static boolean bNoise = false;
      static GLFWErrorCallback errorCallback;
      static GLDebugMessageCallback debugMessageCallback;
      static GLFWKeyCallback keyCallback;
      static GLFWCharCallback charCallback;
      static GLFWCursorPosCallback cursorPosCallback;
      static GLFWMouseButtonCallback mouseButtonCallback;
      static GLFWScrollCallback scrollCallback;
      static GLFWWindowFocusCallback windowFocusCallback;
      static GLFWWindowIconifyCallback windowIconifyCallback;
      static GLFWWindowSizeCallback windowSizeCallback;
      static GLFWWindowPosCallback windowPosCallback;
      static GLFWWindowRefreshCallback windowRefreshCallback;
      static GLFWFramebufferSizeCallback framebufferSizeCallback;

      static void initCallbacks() {
         cursorPosCallback = GLFWCursorPosCallback.create((var0, var2, var4) -> {
            Mouse.addMoveEvent(var2, var4);
         });
         GLFW.glfwSetCursorPosCallback(Display.getWindow(), cursorPosCallback);
         mouseButtonCallback = GLFWMouseButtonCallback.create((var0, var2, var3, var4) -> {
            Mouse.addButtonEvent(var2, var3 == 1);
         });
         GLFW.glfwSetMouseButtonCallback(Display.getWindow(), mouseButtonCallback);
         windowFocusCallback = GLFWWindowFocusCallback.create((var0, var2) -> {
            if (bNoise) {
               DebugLog.log("glfwSetWindowFocusCallback focused=" + var2);
            }

            Display.displayFocused = var2;
            if (var2) {
               Clipboard.rememberCurrentValue();
            }

         });
         GLFW.glfwSetWindowFocusCallback(Display.getWindow(), windowFocusCallback);
         windowIconifyCallback = GLFWWindowIconifyCallback.create((var0, var2) -> {
            if (bNoise) {
               DebugLog.log("glfwSetWindowIconifyCallback iconifed=" + var2);
            }

            Display.displayVisible = !var2;
         });
         GLFW.glfwSetWindowIconifyCallback(Display.getWindow(), windowIconifyCallback);
         windowSizeCallback = GLFWWindowSizeCallback.create((var0, var2, var3) -> {
            if (bNoise) {
               DebugLog.log("glfwSetWindowSizeCallback width,height=" + var2 + "," + var3);
            }

            if (var2 + var3 != 0) {
               Display.latestResized = true;
               Display.latestWidth = var2;
               Display.latestHeight = var3;
            }
         });
         GLFW.glfwSetWindowSizeCallback(Display.getWindow(), windowSizeCallback);
         scrollCallback = GLFWScrollCallback.create((var0, var2, var4) -> {
            Mouse.setDWheel(var2, var4);
         });
         GLFW.glfwSetScrollCallback(Display.getWindow(), scrollCallback);
         windowPosCallback = GLFWWindowPosCallback.create((var0, var2, var3) -> {
            if (bNoise) {
               DebugLog.log("glfwSetWindowPosCallback x,y=" + var2 + "," + var3);
            }

            Display.displayX = var2;
            Display.displayY = var3;
         });
         GLFW.glfwSetWindowPosCallback(Display.getWindow(), windowPosCallback);
         windowRefreshCallback = GLFWWindowRefreshCallback.create((var0) -> {
            Display.displayDirty = true;
         });
         GLFW.glfwSetWindowRefreshCallback(Display.getWindow(), windowRefreshCallback);
         framebufferSizeCallback = GLFWFramebufferSizeCallback.create((var0, var2, var3) -> {
            if (bNoise) {
               DebugLog.log("glfwSetFramebufferSizeCallback width,height=" + var2 + "," + var3);
            }

            Display.displayFramebufferWidth = var2;
            Display.displayFramebufferHeight = var3;
         });
         GLFW.glfwSetFramebufferSizeCallback(Display.getWindow(), framebufferSizeCallback);
         keyCallback = GLFWKeyCallback.create((var0, var2, var3, var4, var5) -> {
            Keyboard.addKeyEvent(var2, var4);
         });
         GLFW.glfwSetKeyCallback(Display.getWindow(), keyCallback);
         charCallback = GLFWCharCallback.create((var0, var2) -> {
            Keyboard.addCharEvent((char)var2);
         });
         GLFW.glfwSetCharCallback(Display.getWindow(), charCallback);
      }

      static void releaseCallbacks() {
         errorCallback.free();
         if (debugMessageCallback != null) {
            debugMessageCallback.free();
         }

         keyCallback.free();
         charCallback.free();
         cursorPosCallback.free();
         mouseButtonCallback.free();
         scrollCallback.free();
         windowFocusCallback.free();
         windowIconifyCallback.free();
         windowSizeCallback.free();
         windowPosCallback.free();
         windowRefreshCallback.free();
         framebufferSizeCallback.free();
      }
   }
}
