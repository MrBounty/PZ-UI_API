package zombie.iso.areas.isoregion;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import zombie.core.Color;
import zombie.core.Core;
import zombie.debug.DebugLog;

public class IsoRegionsLogger {
   private final ConcurrentLinkedQueue pool = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue loggerQueue = new ConcurrentLinkedQueue();
   private final boolean consolePrint;
   private final ArrayList logs = new ArrayList();
   private final int maxLogs = 100;
   private boolean isDirtyUI = false;

   public IsoRegionsLogger(boolean var1) {
      this.consolePrint = var1;
   }

   public ArrayList getLogs() {
      return this.logs;
   }

   public boolean isDirtyUI() {
      return this.isDirtyUI;
   }

   public void unsetDirtyUI() {
      this.isDirtyUI = false;
   }

   private IsoRegionsLogger.IsoRegionLog getLog() {
      IsoRegionsLogger.IsoRegionLog var1 = (IsoRegionsLogger.IsoRegionLog)this.pool.poll();
      if (var1 == null) {
         var1 = new IsoRegionsLogger.IsoRegionLog();
      }

      return var1;
   }

   protected void log(String var1) {
      this.log(var1, (Color)null);
   }

   protected void log(String var1, Color var2) {
      if (Core.bDebug) {
         if (this.consolePrint) {
            DebugLog.IsoRegion.println(var1);
         }

         IsoRegionsLogger.IsoRegionLog var3 = this.getLog();
         var3.str = var1;
         var3.type = IsoRegionLogType.Normal;
         var3.col = var2;
         this.loggerQueue.offer(var3);
      }

   }

   protected void warn(String var1) {
      DebugLog.IsoRegion.warn(var1);
      if (Core.bDebug) {
         IsoRegionsLogger.IsoRegionLog var2 = this.getLog();
         var2.str = var1;
         var2.type = IsoRegionLogType.Warn;
         this.loggerQueue.offer(var2);
      }

   }

   protected void update() {
      if (Core.bDebug) {
         for(IsoRegionsLogger.IsoRegionLog var1 = (IsoRegionsLogger.IsoRegionLog)this.loggerQueue.poll(); var1 != null; var1 = (IsoRegionsLogger.IsoRegionLog)this.loggerQueue.poll()) {
            if (this.logs.size() >= 100) {
               IsoRegionsLogger.IsoRegionLog var2 = (IsoRegionsLogger.IsoRegionLog)this.logs.remove(0);
               var2.col = null;
               this.pool.offer(var2);
            }

            this.logs.add(var1);
            this.isDirtyUI = true;
         }

      }
   }

   public static class IsoRegionLog {
      private String str;
      private IsoRegionLogType type;
      private Color col;

      public String getStr() {
         return this.str;
      }

      public IsoRegionLogType getType() {
         return this.type;
      }

      public Color getColor() {
         if (this.col != null) {
            return this.col;
         } else {
            return this.type == IsoRegionLogType.Warn ? Color.red : Color.white;
         }
      }
   }
}
