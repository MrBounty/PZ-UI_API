package zombie.core.opengl;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjglx.LWJGLException;
import org.lwjglx.input.Controllers;
import org.lwjglx.opengl.Display;
import org.lwjglx.opengl.OpenGLException;
import org.lwjglx.opengl.Util;
import zombie.GameWindow;
import zombie.Lua.LuaManager;
import zombie.core.Clipboard;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.ThreadGroups;
import zombie.core.profiling.PerformanceProfileFrameProbe;
import zombie.core.profiling.PerformanceProfileProbe;
import zombie.core.sprite.SpriteRenderState;
import zombie.core.textures.TextureID;
import zombie.debug.DebugLog;
import zombie.debug.DebugLogStream;
import zombie.debug.DebugOptions;
import zombie.input.GameKeyboard;
import zombie.input.Mouse;
import zombie.network.GameServer;
import zombie.network.MPStatisticClient;
import zombie.ui.FPSGraph;
import zombie.util.Lambda;
import zombie.util.lambda.Invokers;
import zombie.util.list.PZArrayUtil;

public class RenderThread {
   private static Thread MainThread;
   public static Thread RenderThread;
   private static Thread ContextThread = null;
   private static boolean m_isDisplayCreated = false;
   private static int m_contextLockReentrantDepth = 0;
   public static final Object m_contextLock = "RenderThread borrowContext Lock";
   private static final ArrayList invokeOnRenderQueue = new ArrayList();
   private static final ArrayList invokeOnRenderQueue_Invoking = new ArrayList();
   private static boolean m_isInitialized = false;
   private static final Object m_initLock = "RenderThread Initialization Lock";
   private static volatile boolean m_isCloseRequested = false;
   private static volatile int m_displayWidth;
   private static volatile int m_displayHeight;
   private static final boolean s_legacyAllowSingleThreadRendering = false;
   private static volatile boolean m_renderingEnabled = true;
   private static volatile boolean m_waitForRenderState = false;
   private static volatile boolean m_hasContext = false;
   private static boolean m_cursorVisible = true;

   public static void init() {
      synchronized(m_initLock) {
         if (!m_isInitialized) {
            MainThread = Thread.currentThread();
            Core.bLoadedWithMultithreaded = Core.bMultithreadedRendering;
            RenderThread = Thread.currentThread();
            m_displayWidth = Display.getWidth();
            m_displayHeight = Display.getHeight();
            m_isInitialized = true;
         }
      }
   }

   public static void initServerGUI() {
      synchronized(m_initLock) {
         if (m_isInitialized) {
            return;
         }

         MainThread = Thread.currentThread();
         Core.bLoadedWithMultithreaded = Core.bMultithreadedRendering;
         RenderThread = new Thread(ThreadGroups.Main, RenderThread::renderLoop, "RenderThread Main Loop");
         RenderThread.setName("Render Thread");
         RenderThread.setUncaughtExceptionHandler(RenderThread::uncaughtException);
         m_displayWidth = Display.getWidth();
         m_displayHeight = Display.getHeight();
         m_isInitialized = true;
      }

      RenderThread.start();
   }

   public static void renderLoop() {
      if (!GameServer.bServer) {
         synchronized(m_initLock) {
            try {
               m_isInitialized = false;
               GameWindow.InitDisplay();
               Controllers.create();
               Clipboard.initMainThread();
            } catch (Exception var12) {
               throw new RuntimeException(var12);
            } finally {
               m_isInitialized = true;
            }
         }
      }

      acquireContextReentrant();

      for(boolean var0 = true; var0; Thread.yield()) {
         synchronized(m_contextLock) {
            if (!m_hasContext) {
               acquireContextReentrant();
            }

            m_displayWidth = Display.getWidth();
            m_displayHeight = Display.getHeight();
            if (m_renderingEnabled) {
               RenderThread.s_performance.renderStep.invokeAndMeasure(RenderThread::renderStep);
            } else if (m_isDisplayCreated && m_hasContext) {
               Display.processMessages();
            }

            flushInvokeQueue();
            if (!m_renderingEnabled) {
               m_isCloseRequested = false;
            } else {
               GameWindow.GameInput.poll();
               Mouse.poll();
               GameKeyboard.poll();
               m_isCloseRequested = m_isCloseRequested || Display.isCloseRequested();
            }

            if (!GameServer.bServer) {
               Clipboard.updateMainThread();
            }

            DebugOptions.testThreadCrash(0);
            var0 = !GameWindow.bGameThreadExited;
         }
      }

      releaseContextReentrant();
      synchronized(m_initLock) {
         RenderThread = null;
         m_isInitialized = false;
      }

      shutdown();
      System.exit(0);
   }

   private static void uncaughtException(Thread var0, Throwable var1) {
      if (var1 instanceof ThreadDeath) {
         DebugLog.General.println("Render Thread exited: ", var0.getName());
      } else {
         boolean var14 = false;

         try {
            var14 = true;
            GameWindow.uncaughtException(var0, var1);
            var14 = false;
         } finally {
            if (var14) {
               Runnable var7 = () -> {
                  long var0 = 120000L;
                  long var2 = 0L;
                  long var4 = System.currentTimeMillis();
                  long var6 = var4;
                  if (!GameWindow.bGameThreadExited) {
                     try {
                        Thread.sleep(1000L);
                     } catch (InterruptedException var11) {
                     }

                     DebugLog.General.error("  Waiting for GameThread to exit...");

                     try {
                        Thread.sleep(2000L);
                     } catch (InterruptedException var10) {
                     }

                     while(!GameWindow.bGameThreadExited) {
                        Thread.yield();
                        var4 = System.currentTimeMillis();
                        long var8 = var4 - var6;
                        var2 += var8;
                        if (var2 >= 120000L) {
                           DebugLog.General.error("  GameThread failed to exit within time limit.");
                           break;
                        }

                        var6 = var4;
                     }
                  }

                  DebugLog.General.error("  Shutting down...");
                  System.exit(1);
               };
               Thread var8 = new Thread(var7, "ForceCloseThread");
               var8.start();
               DebugLog.General.error("Shutting down sequence starts.");
               m_isCloseRequested = true;
               DebugLog.General.error("  Notifying render state queue...");
               notifyRenderStateQueue();
               DebugLog.General.error("  Notifying InvokeOnRenderQueue...");
               synchronized(invokeOnRenderQueue) {
                  invokeOnRenderQueue_Invoking.addAll(invokeOnRenderQueue);
                  invokeOnRenderQueue.clear();
               }

               PZArrayUtil.forEach((List)invokeOnRenderQueue_Invoking, RenderContextQueueItem::notifyWaitingListeners);
            }
         }

         Runnable var2 = () -> {
            long var0 = 120000L;
            long var2 = 0L;
            long var4 = System.currentTimeMillis();
            long var6 = var4;
            if (!GameWindow.bGameThreadExited) {
               try {
                  Thread.sleep(1000L);
               } catch (InterruptedException var11) {
               }

               DebugLog.General.error("  Waiting for GameThread to exit...");

               try {
                  Thread.sleep(2000L);
               } catch (InterruptedException var10) {
               }

               while(!GameWindow.bGameThreadExited) {
                  Thread.yield();
                  var4 = System.currentTimeMillis();
                  long var8 = var4 - var6;
                  var2 += var8;
                  if (var2 >= 120000L) {
                     DebugLog.General.error("  GameThread failed to exit within time limit.");
                     break;
                  }

                  var6 = var4;
               }
            }

            DebugLog.General.error("  Shutting down...");
            System.exit(1);
         };
         Thread var3 = new Thread(var2, "ForceCloseThread");
         var3.start();
         DebugLog.General.error("Shutting down sequence starts.");
         m_isCloseRequested = true;
         DebugLog.General.error("  Notifying render state queue...");
         notifyRenderStateQueue();
         DebugLog.General.error("  Notifying InvokeOnRenderQueue...");
         synchronized(invokeOnRenderQueue) {
            invokeOnRenderQueue_Invoking.addAll(invokeOnRenderQueue);
            invokeOnRenderQueue.clear();
         }

         PZArrayUtil.forEach((List)invokeOnRenderQueue_Invoking, RenderContextQueueItem::notifyWaitingListeners);
      }
   }

   private static boolean renderStep() {
      boolean var0 = false;

      try {
         var0 = lockStepRenderStep();
      } catch (OpenGLException var2) {
         logGLException(var2);
      } catch (Exception var3) {
         DebugLogStream var10000 = DebugLog.General;
         String var10001 = var3.getClass().getTypeName();
         var10000.error("Thrown an " + var10001 + ": " + var3.getMessage());
         var3.printStackTrace();
      }

      return var0;
   }

   private static boolean lockStepRenderStep() {
      SpriteRenderState var0 = SpriteRenderer.instance.acquireStateForRendering(RenderThread::waitForRenderStateCallback);
      if (var0 != null) {
         m_cursorVisible = var0.bCursorVisible;
         RenderThread.s_performance.spriteRendererPostRender.invokeAndMeasure(() -> {
            SpriteRenderer.instance.postRender();
         });
         RenderThread.s_performance.displayUpdate.invokeAndMeasure(() -> {
            Display.update(true);
            checkControllers();
         });
         if (Core.bDebug && FPSGraph.instance != null) {
            FPSGraph.instance.addRender(System.currentTimeMillis());
         }

         MPStatisticClient.getInstance().fpsProcess();
         return true;
      } else {
         notifyRenderStateQueue();
         if (!m_waitForRenderState || LuaManager.thread != null && LuaManager.thread.bStep) {
            RenderThread.s_performance.displayUpdate.invokeAndMeasure(() -> {
               Display.processMessages();
            });
         }

         return true;
      }
   }

   private static void checkControllers() {
   }

   private static boolean waitForRenderStateCallback() {
      flushInvokeQueue();
      return shouldContinueWaiting();
   }

   private static boolean shouldContinueWaiting() {
      return !m_isCloseRequested && !GameWindow.bGameThreadExited && (m_waitForRenderState || SpriteRenderer.instance.isWaitingForRenderState());
   }

   public static boolean isWaitForRenderState() {
      return m_waitForRenderState;
   }

   public static void setWaitForRenderState(boolean var0) {
      m_waitForRenderState = var0;
   }

   private static void flushInvokeQueue() {
      synchronized(invokeOnRenderQueue) {
         invokeOnRenderQueue_Invoking.addAll(invokeOnRenderQueue);
         invokeOnRenderQueue.clear();
      }

      try {
         if (!invokeOnRenderQueue_Invoking.isEmpty()) {
            long var0 = System.nanoTime();

            while(!invokeOnRenderQueue_Invoking.isEmpty()) {
               RenderContextQueueItem var2 = (RenderContextQueueItem)invokeOnRenderQueue_Invoking.remove(0);
               long var3 = System.nanoTime();
               var2.invoke();
               long var5 = System.nanoTime();
               if ((double)(var5 - var3) > 1.0E7D) {
                  boolean var7 = true;
               }

               if ((double)(var5 - var0) > 1.0E7D) {
                  break;
               }
            }

            label45:
            for(int var11 = invokeOnRenderQueue_Invoking.size() - 1; var11 >= 0; --var11) {
               RenderContextQueueItem var12 = (RenderContextQueueItem)invokeOnRenderQueue_Invoking.get(var11);
               if (var12.isWaiting()) {
                  while(true) {
                     if (var11 < 0) {
                        break label45;
                     }

                     RenderContextQueueItem var4 = (RenderContextQueueItem)invokeOnRenderQueue_Invoking.remove(0);
                     var4.invoke();
                     --var11;
                  }
               }
            }
         }

         if (TextureID.deleteTextureIDS.position() > 0) {
            TextureID.deleteTextureIDS.flip();
            GL11.glDeleteTextures(TextureID.deleteTextureIDS);
            TextureID.deleteTextureIDS.clear();
         }
      } catch (OpenGLException var9) {
         logGLException(var9);
      } catch (Exception var10) {
         DebugLogStream var10000 = DebugLog.General;
         String var10001 = var10.getClass().getTypeName();
         var10000.error("Thrown an " + var10001 + ": " + var10.getMessage());
         var10.printStackTrace();
      }

   }

   public static void logGLException(OpenGLException var0) {
      logGLException(var0, true);
   }

   public static void logGLException(OpenGLException var0, boolean var1) {
      DebugLog.General.error("OpenGLException thrown: " + var0.getMessage());

      for(int var2 = GL11.glGetError(); var2 != 0; var2 = GL11.glGetError()) {
         String var3 = Util.translateGLErrorString(var2);
         DebugLog.General.error("  Also detected error: " + var3 + " ( code:" + var2 + ")");
      }

      if (var1) {
         DebugLog.General.error("Stack trace:");
         var0.printStackTrace();
      }

   }

   public static void Ready() {
      SpriteRenderer.instance.pushFrameDown();
      if (!m_isInitialized) {
         invokeOnRenderContext(RenderThread::renderStep);
      }

   }

   private static void acquireContextReentrant() {
      synchronized(m_contextLock) {
         acquireContextReentrantInternal();
      }
   }

   private static void releaseContextReentrant() {
      synchronized(m_contextLock) {
         releaseContextReentrantInternal();
      }
   }

   private static void acquireContextReentrantInternal() {
      Thread var0 = Thread.currentThread();
      if (ContextThread != null && ContextThread != var0) {
         throw new RuntimeException("Context thread mismatch: " + ContextThread + ", " + var0);
      } else {
         ++m_contextLockReentrantDepth;
         if (m_contextLockReentrantDepth <= 1) {
            ContextThread = var0;
            m_isDisplayCreated = Display.isCreated();
            if (m_isDisplayCreated) {
               try {
                  m_hasContext = true;
                  Display.makeCurrent();
                  Display.setVSyncEnabled(Core.OptionVSync);
               } catch (LWJGLException var2) {
                  DebugLog.General.error("Exception thrown trying to gain GL context.");
                  var2.printStackTrace();
               }
            }

         }
      }
   }

   private static void releaseContextReentrantInternal() {
      Thread var0 = Thread.currentThread();
      if (ContextThread != var0) {
         throw new RuntimeException("Context thread mismatch: " + ContextThread + ", " + var0);
      } else if (m_contextLockReentrantDepth == 0) {
         throw new RuntimeException("Context thread release overflow: 0: " + ContextThread + ", " + var0);
      } else {
         --m_contextLockReentrantDepth;
         if (m_contextLockReentrantDepth <= 0) {
            if (m_isDisplayCreated && m_hasContext) {
               try {
                  m_hasContext = false;
                  Display.releaseContext();
               } catch (LWJGLException var2) {
                  DebugLog.General.error("Exception thrown trying to release GL context.");
                  var2.printStackTrace();
               }
            }

            ContextThread = null;
         }
      }
   }

   public static void invokeOnRenderContext(Runnable var0) throws RenderContextQueueException {
      RenderContextQueueItem var1 = RenderContextQueueItem.alloc(var0);
      var1.setWaiting();
      queueInvokeOnRenderContext(var1);

      try {
         var1.waitUntilFinished(() -> {
            notifyRenderStateQueue();
            return !m_isCloseRequested && !GameWindow.bGameThreadExited;
         });
      } catch (InterruptedException var3) {
         DebugLog.General.error("Thread Interrupted while waiting for queued item to finish:" + var1);
         notifyRenderStateQueue();
      }

      Throwable var2 = var1.getThrown();
      if (var2 != null) {
         throw new RenderContextQueueException(var2);
      }
   }

   public static void invokeOnRenderContext(Object var0, Invokers.Params1.ICallback var1) {
      Lambda.capture(var0, var1, (var0x, var1x, var2) -> {
         invokeOnRenderContext(var0x.invoker(var1x, var2));
      });
   }

   public static void invokeOnRenderContext(Object var0, Object var1, Invokers.Params2.ICallback var2) {
      Lambda.capture(var0, var1, var2, (var0x, var1x, var2x, var3) -> {
         invokeOnRenderContext(var0x.invoker(var1x, var2x, var3));
      });
   }

   public static void invokeOnRenderContext(Object var0, Object var1, Object var2, Invokers.Params3.ICallback var3) {
      Lambda.capture(var0, var1, var2, var3, (var0x, var1x, var2x, var3x, var4) -> {
         invokeOnRenderContext(var0x.invoker(var1x, var2x, var3x, var4));
      });
   }

   public static void invokeOnRenderContext(Object var0, Object var1, Object var2, Object var3, Invokers.Params4.ICallback var4) {
      Lambda.capture(var0, var1, var2, var3, var4, (var0x, var1x, var2x, var3x, var4x, var5) -> {
         invokeOnRenderContext(var0x.invoker(var1x, var2x, var3x, var4x, var5));
      });
   }

   protected static void notifyRenderStateQueue() {
      if (SpriteRenderer.instance != null) {
         SpriteRenderer.instance.notifyRenderStateQueue();
      }

   }

   public static void queueInvokeOnRenderContext(Runnable var0) {
      queueInvokeOnRenderContext(RenderContextQueueItem.alloc(var0));
   }

   public static void queueInvokeOnRenderContext(RenderContextQueueItem var0) {
      if (!m_isInitialized) {
         synchronized(m_initLock) {
            if (!m_isInitialized) {
               try {
                  acquireContextReentrant();
                  var0.invoke();
               } finally {
                  releaseContextReentrant();
               }

               return;
            }
         }
      }

      if (ContextThread == Thread.currentThread()) {
         var0.invoke();
      } else {
         synchronized(invokeOnRenderQueue) {
            invokeOnRenderQueue.add(var0);
         }
      }
   }

   public static void shutdown() {
      GameWindow.GameInput.quit();
      if (m_isInitialized) {
         queueInvokeOnRenderContext(Display::destroy);
      } else {
         Display.destroy();
      }

   }

   public static boolean isCloseRequested() {
      if (m_isCloseRequested) {
         DebugLog.log("EXITDEBUG: RenderThread.isCloseRequested 1");
         return m_isCloseRequested;
      } else {
         if (!m_isInitialized) {
            synchronized(m_initLock) {
               if (!m_isInitialized) {
                  m_isCloseRequested = Display.isCloseRequested();
                  if (m_isCloseRequested) {
                     DebugLog.log("EXITDEBUG: RenderThread.isCloseRequested 2");
                  }
               }
            }
         }

         return m_isCloseRequested;
      }
   }

   public static int getDisplayWidth() {
      return !m_isInitialized ? Display.getWidth() : m_displayWidth;
   }

   public static int getDisplayHeight() {
      return !m_isInitialized ? Display.getHeight() : m_displayHeight;
   }

   public static boolean isRunning() {
      return m_isInitialized;
   }

   public static void startRendering() {
      m_renderingEnabled = true;
   }

   public static void onGameThreadExited() {
      DebugLog.General.println("GameThread exited.");
      if (RenderThread != null) {
         RenderThread.interrupt();
      }

   }

   public static boolean isCursorVisible() {
      return m_cursorVisible;
   }

   private static class s_performance {
      static final PerformanceProfileFrameProbe renderStep = new PerformanceProfileFrameProbe("RenderThread.renderStep");
      static final PerformanceProfileProbe displayUpdate = new PerformanceProfileProbe("Display.update(true)");
      static final PerformanceProfileProbe spriteRendererPostRender = new PerformanceProfileProbe("SpriteRenderer.postRender");
   }
}
