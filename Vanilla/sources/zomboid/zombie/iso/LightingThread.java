package zombie.iso;

import org.lwjglx.opengl.Display;
import zombie.GameWindow;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.ThreadGroups;
import zombie.network.GameServer;
import zombie.ui.FPSGraph;

public final class LightingThread {
   public static final LightingThread instance = new LightingThread();
   public Thread lightingThread;
   public boolean bFinished = false;
   public volatile boolean Interrupted = false;
   public static boolean DebugLockTime = false;

   public void stop() {
      if (!PerformanceSettings.LightingThread) {
         LightingJNI.stop();
      } else {
         this.bFinished = true;

         while(this.lightingThread.isAlive()) {
         }

         LightingJNI.stop();
         this.lightingThread = null;
      }
   }

   public void create() {
      if (!GameServer.bServer) {
         if (PerformanceSettings.LightingThread) {
            this.bFinished = false;
            this.lightingThread = new Thread(ThreadGroups.Workers, () -> {
               while(!this.bFinished) {
                  if (IsoWorld.instance.CurrentCell == null) {
                     return;
                  }

                  try {
                     Display.sync(PerformanceSettings.LightingFPS);
                     LightingJNI.DoLightingUpdateNew(System.nanoTime());

                     while(LightingJNI.WaitingForMain() && !this.bFinished) {
                        Thread.sleep(13L);
                     }

                     if (Core.bDebug && FPSGraph.instance != null) {
                        FPSGraph.instance.addLighting(System.currentTimeMillis());
                     }
                  } catch (Exception var2) {
                     var2.printStackTrace();
                  }
               }

            });
            this.lightingThread.setPriority(5);
            this.lightingThread.setDaemon(true);
            this.lightingThread.setName("Lighting Thread");
            this.lightingThread.setUncaughtExceptionHandler(GameWindow::uncaughtException);
            this.lightingThread.start();
         }
      }
   }

   public void GameLoadingUpdate() {
   }

   public void update() {
      if (IsoWorld.instance != null && IsoWorld.instance.CurrentCell != null) {
         if (LightingJNI.init) {
            LightingJNI.update();
         }

      }
   }

   public void scrollLeft(int var1) {
      if (LightingJNI.init) {
         LightingJNI.scrollLeft(var1);
      }

   }

   public void scrollRight(int var1) {
      if (LightingJNI.init) {
         LightingJNI.scrollRight(var1);
      }

   }

   public void scrollUp(int var1) {
      if (LightingJNI.init) {
         LightingJNI.scrollUp(var1);
      }

   }

   public void scrollDown(int var1) {
      if (LightingJNI.init) {
         LightingJNI.scrollDown(var1);
      }

   }
}
