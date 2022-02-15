package zombie;

import java.io.File;
import java.util.function.Predicate;
import zombie.debug.DebugLog;
import zombie.util.PZXmlParserException;
import zombie.util.PZXmlUtil;

public final class PredicatedFileWatcher {
   private final String m_path;
   private final Predicate m_predicate;
   private final PredicatedFileWatcher.IPredicatedFileWatcherCallback m_callback;

   public PredicatedFileWatcher(Predicate var1, PredicatedFileWatcher.IPredicatedFileWatcherCallback var2) {
      this((String)null, (Predicate)var1, (PredicatedFileWatcher.IPredicatedFileWatcherCallback)var2);
   }

   public PredicatedFileWatcher(String var1, PredicatedFileWatcher.IPredicatedFileWatcherCallback var2) {
      this(var1, (Predicate)null, (PredicatedFileWatcher.IPredicatedFileWatcherCallback)var2);
   }

   public PredicatedFileWatcher(String var1, Class var2, PredicatedFileWatcher.IPredicatedDataPacketFileWatcherCallback var3) {
      this(var1, (Predicate)null, (PredicatedFileWatcher.IPredicatedFileWatcherCallback)(new PredicatedFileWatcher.GenericPredicatedFileWatcherCallback(var2, var3)));
   }

   public PredicatedFileWatcher(String var1, Predicate var2, PredicatedFileWatcher.IPredicatedFileWatcherCallback var3) {
      this.m_path = this.processPath(var1);
      this.m_predicate = var2 != null ? var2 : this::pathsEqual;
      this.m_callback = var3;
   }

   public String getPath() {
      return this.m_path;
   }

   private String processPath(String var1) {
      return var1 != null ? ZomboidFileSystem.processFilePath(var1, File.separatorChar) : null;
   }

   private boolean pathsEqual(String var1) {
      return var1.equals(this.m_path);
   }

   public void onModified(String var1) {
      if (this.m_predicate.test(var1)) {
         this.m_callback.call(var1);
      }

   }

   public interface IPredicatedFileWatcherCallback {
      void call(String var1);
   }

   public static class GenericPredicatedFileWatcherCallback implements PredicatedFileWatcher.IPredicatedFileWatcherCallback {
      private final Class m_class;
      private final PredicatedFileWatcher.IPredicatedDataPacketFileWatcherCallback m_callback;

      public GenericPredicatedFileWatcherCallback(Class var1, PredicatedFileWatcher.IPredicatedDataPacketFileWatcherCallback var2) {
         this.m_class = var1;
         this.m_callback = var2;
      }

      public void call(String var1) {
         Object var2;
         try {
            var2 = PZXmlUtil.parse(this.m_class, var1);
         } catch (PZXmlParserException var4) {
            DebugLog.General.error("Exception thrown. " + var4);
            return;
         }

         this.m_callback.call(var2);
      }
   }

   public interface IPredicatedDataPacketFileWatcherCallback {
      void call(Object var1);
   }
}
