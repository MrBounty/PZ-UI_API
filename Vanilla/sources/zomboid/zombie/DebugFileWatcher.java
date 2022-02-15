package zombie;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import zombie.core.logger.ExceptionLogger;

public final class DebugFileWatcher {
   private final HashMap m_watchedFiles = new HashMap();
   private final HashMap m_watchkeyMapping = new HashMap();
   private final ArrayList m_predicateWatchers = new ArrayList();
   private final ArrayList m_predicateWatchersInvoking = new ArrayList();
   private final FileSystem m_fs = FileSystems.getDefault();
   private WatchService m_watcher;
   private boolean m_predicateWatchersInvokingDirty = true;
   private long m_modificationTime = -1L;
   private final ArrayList m_modifiedFiles = new ArrayList();
   public static final DebugFileWatcher instance = new DebugFileWatcher();

   private DebugFileWatcher() {
   }

   public void init() {
      try {
         this.m_watcher = this.m_fs.newWatchService();
         this.registerDirRecursive(this.m_fs.getPath(ZomboidFileSystem.instance.getMediaRootPath()));
         this.registerDirRecursive(this.m_fs.getPath(ZomboidFileSystem.instance.getMessagingDir()));
      } catch (IOException var2) {
         this.m_watcher = null;
      }

   }

   private void registerDirRecursive(Path var1) {
      try {
         Files.walkFileTree(var1, new SimpleFileVisitor() {
            public FileVisitResult preVisitDirectory(Path var1, BasicFileAttributes var2) {
               DebugFileWatcher.this.registerDir(var1);
               return FileVisitResult.CONTINUE;
            }
         });
      } catch (IOException var3) {
         ExceptionLogger.logException(var3);
         this.m_watcher = null;
      }

   }

   private void registerDir(Path var1) {
      try {
         WatchKey var2 = var1.register(this.m_watcher, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
         this.m_watchkeyMapping.put(var2, var1);
      } catch (IOException var3) {
         ExceptionLogger.logException(var3);
         this.m_watcher = null;
      }

   }

   private void addWatchedFile(String var1) {
      if (var1 != null) {
         this.m_watchedFiles.put(this.m_fs.getPath(var1), var1);
      }

   }

   public void add(PredicatedFileWatcher var1) {
      if (!this.m_predicateWatchers.contains(var1)) {
         this.addWatchedFile(var1.getPath());
         this.m_predicateWatchers.add(var1);
         this.m_predicateWatchersInvokingDirty = true;
      }

   }

   public void addDirectory(String var1) {
      if (var1 != null) {
         this.registerDir(this.m_fs.getPath(var1));
      }

   }

   public void addDirectoryRecurse(String var1) {
      if (var1 != null) {
         this.registerDirRecursive(this.m_fs.getPath(var1));
      }

   }

   public void remove(PredicatedFileWatcher var1) {
      this.m_predicateWatchers.remove(var1);
   }

   public void update() {
      if (this.m_watcher != null) {
         Iterator var3;
         for(WatchKey var1 = this.m_watcher.poll(); var1 != null; var1 = this.m_watcher.poll()) {
            try {
               Path var2 = (Path)this.m_watchkeyMapping.getOrDefault(var1, (Object)null);
               var3 = var1.pollEvents().iterator();

               while(var3.hasNext()) {
                  WatchEvent var4 = (WatchEvent)var3.next();
                  Path var6;
                  Path var7;
                  String var8;
                  if (var4.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                     var6 = (Path)var4.context();
                     var7 = var2.resolve(var6);
                     var8 = (String)this.m_watchedFiles.getOrDefault(var7, var7.toString());
                     this.m_modificationTime = System.currentTimeMillis();
                     if (!this.m_modifiedFiles.contains(var8)) {
                        this.m_modifiedFiles.add(var8);
                     }
                  } else if (var4.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                     var6 = (Path)var4.context();
                     var7 = var2.resolve(var6);
                     if (Files.isDirectory(var7, new LinkOption[0])) {
                        this.registerDirRecursive(var7);
                     } else {
                        var8 = (String)this.m_watchedFiles.getOrDefault(var7, var7.toString());
                        this.m_modificationTime = System.currentTimeMillis();
                        if (!this.m_modifiedFiles.contains(var8)) {
                           this.m_modifiedFiles.add(var8);
                        }
                     }
                  }
               }
            } finally {
               if (!var1.reset()) {
                  this.m_watchkeyMapping.remove(var1);
               }

            }
         }

         if (!this.m_modifiedFiles.isEmpty()) {
            if (this.m_modificationTime + 2000L <= System.currentTimeMillis()) {
               for(int var12 = this.m_modifiedFiles.size() - 1; var12 >= 0; --var12) {
                  String var13 = (String)this.m_modifiedFiles.remove(var12);
                  this.swapWatcherArrays();
                  var3 = this.m_predicateWatchersInvoking.iterator();

                  while(var3.hasNext()) {
                     PredicatedFileWatcher var14 = (PredicatedFileWatcher)var3.next();
                     var14.onModified(var13);
                  }
               }

            }
         }
      }
   }

   private void swapWatcherArrays() {
      if (this.m_predicateWatchersInvokingDirty) {
         this.m_predicateWatchersInvoking.clear();
         this.m_predicateWatchersInvoking.addAll(this.m_predicateWatchers);
         this.m_predicateWatchersInvokingDirty = false;
      }

   }
}
