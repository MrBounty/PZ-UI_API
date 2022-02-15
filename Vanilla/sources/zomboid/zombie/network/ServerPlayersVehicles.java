package zombie.network;

import zombie.core.logger.ExceptionLogger;
import zombie.savefile.ServerPlayerDB;
import zombie.vehicles.VehiclesDB2;

public class ServerPlayersVehicles {
   public static final ServerPlayersVehicles instance = new ServerPlayersVehicles();
   private ServerPlayersVehicles.SPVThread m_thread = null;

   public void init() {
      this.m_thread = new ServerPlayersVehicles.SPVThread();
      this.m_thread.setName("ServerPlayersVehicles");
      this.m_thread.setDaemon(true);
      this.m_thread.start();
   }

   public void stop() {
      if (this.m_thread != null) {
         this.m_thread.m_bStop = true;

         while(this.m_thread.isAlive()) {
            try {
               Thread.sleep(100L);
            } catch (InterruptedException var2) {
            }
         }

         this.m_thread = null;
      }

   }

   private static final class SPVThread extends Thread {
      boolean m_bStop = false;

      public void run() {
         while(!this.m_bStop) {
            try {
               this.runInner();
            } catch (Throwable var2) {
               ExceptionLogger.logException(var2);
            }
         }

      }

      void runInner() {
         ServerPlayerDB.getInstance().process();
         VehiclesDB2.instance.updateWorldStreamer();

         try {
            Thread.sleep(500L);
         } catch (InterruptedException var2) {
         }

      }
   }
}
