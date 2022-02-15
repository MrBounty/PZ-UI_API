package zombie.core.opengl;

import java.util.function.BooleanSupplier;
import zombie.core.logger.ExceptionLogger;
import zombie.debug.DebugLog;

public final class RenderContextQueueItem {
   private Runnable m_runnable;
   private boolean m_isFinished;
   private boolean m_isWaiting;
   private Throwable m_runnableThrown = null;
   private final Object m_waitLock = "RenderContextQueueItem Wait Lock";

   private RenderContextQueueItem() {
   }

   public static RenderContextQueueItem alloc(Runnable var0) {
      RenderContextQueueItem var1 = new RenderContextQueueItem();
      var1.resetInternal();
      var1.m_runnable = var0;
      return var1;
   }

   private void resetInternal() {
      this.m_runnable = null;
      this.m_isFinished = false;
      this.m_runnableThrown = null;
   }

   public void waitUntilFinished(BooleanSupplier var1) throws InterruptedException {
      while(!this.isFinished()) {
         if (!var1.getAsBoolean()) {
            return;
         }

         synchronized(this.m_waitLock) {
            if (!this.isFinished()) {
               this.m_waitLock.wait();
            }
         }
      }

   }

   public boolean isFinished() {
      return this.m_isFinished;
   }

   public void setWaiting() {
      this.m_isWaiting = true;
   }

   public boolean isWaiting() {
      return this.m_isWaiting;
   }

   public void invoke() {
      boolean var12 = false;

      label87: {
         try {
            var12 = true;
            this.m_runnableThrown = null;
            this.m_runnable.run();
            var12 = false;
            break label87;
         } catch (Throwable var16) {
            this.m_runnableThrown = var16;
            DebugLog.General.error("%s thrown during invoke().", var16.toString());
            ExceptionLogger.logException(var16);
            var12 = false;
         } finally {
            if (var12) {
               synchronized(this.m_waitLock) {
                  this.m_isFinished = true;
                  this.m_waitLock.notifyAll();
               }
            }
         }

         synchronized(this.m_waitLock) {
            this.m_isFinished = true;
            this.m_waitLock.notifyAll();
            return;
         }
      }

      synchronized(this.m_waitLock) {
         this.m_isFinished = true;
         this.m_waitLock.notifyAll();
      }

   }

   public Throwable getThrown() {
      return this.m_runnableThrown;
   }

   public void notifyWaitingListeners() {
      synchronized(this.m_waitLock) {
         this.m_waitLock.notifyAll();
      }
   }
}
