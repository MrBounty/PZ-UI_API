package zombie.core.logger;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import zombie.ZomboidFileSystem;
import zombie.characters.IsoPlayer;
import zombie.debug.DebugLog;

public final class LoggerManager {
   private static boolean s_isInitialized = false;
   private static final HashMap s_loggers = new HashMap();

   public static synchronized ZLogger getLogger(String var0) {
      if (!s_loggers.containsKey(var0)) {
         createLogger(var0, false);
      }

      return (ZLogger)s_loggers.get(var0);
   }

   public static synchronized void init() {
      if (!s_isInitialized) {
         DebugLog.General.debugln("Initializing...");
         s_isInitialized = true;
         backupOldLogFiles();
      }
   }

   private static void backupOldLogFiles() {
      try {
         File var0 = new File(getLogsDir());
         String[] var1 = var0.list();
         if (var1 == null || var1.length == 0) {
            return;
         }

         Calendar var3 = getLogFileLastModifiedTime(var1[0]);
         String var4 = "logs_";
         if (var3.get(5) < 9) {
            var4 = var4 + "0" + var3.get(5);
         } else {
            var4 = var4 + var3.get(5);
         }

         if (var3.get(2) < 9) {
            var4 = var4 + "-0" + (var3.get(2) + 1);
         } else {
            var4 = var4 + "-" + (var3.get(2) + 1);
         }

         String var10002 = getLogsDir();
         File var2 = new File(var10002 + File.separator + var4);
         if (!var2.exists()) {
            var2.mkdir();
         }

         for(int var7 = 0; var7 < var1.length; ++var7) {
            var4 = var1[var7];
            var10002 = getLogsDir();
            File var5 = new File(var10002 + File.separator + var4);
            if (var5.isFile()) {
               String var10003 = var2.getAbsolutePath();
               var5.renameTo(new File(var10003 + File.separator + var5.getName()));
               var5.delete();
            }
         }
      } catch (Exception var6) {
         DebugLog.General.error("Exception thrown trying to initialize LoggerManager, trying to copy old log files.");
         DebugLog.General.error("Exception: ");
         DebugLog.General.error(var6);
         var6.printStackTrace();
      }

   }

   private static Calendar getLogFileLastModifiedTime(String var0) {
      String var10002 = getLogsDir();
      File var1 = new File(var10002 + File.separator + var0);
      Calendar var2 = Calendar.getInstance();
      var2.setTimeInMillis(var1.lastModified());
      return var2;
   }

   public static synchronized void createLogger(String var0, boolean var1) {
      init();
      s_loggers.put(var0, new ZLogger(var0, var1));
   }

   public static String getLogsDir() {
      String var0 = ZomboidFileSystem.instance.getCacheDirSub("Logs");
      ZomboidFileSystem.ensureFolderExists(var0);
      File var1 = new File(var0);
      return var1.getAbsolutePath();
   }

   public static String getPlayerCoords(IsoPlayer var0) {
      int var10000 = (int)var0.getX();
      return "(" + var10000 + "," + (int)var0.getY() + "," + (int)var0.getZ() + ")";
   }
}
