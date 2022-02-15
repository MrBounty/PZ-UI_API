package zombie.fileSystem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import zombie.GameWindow;
import zombie.core.logger.ExceptionLogger;
import zombie.gameStates.GameLoadingState;

public final class FileSystemImpl extends FileSystem {
   private final ArrayList m_devices = new ArrayList();
   private final ArrayList m_in_progress = new ArrayList();
   private final ArrayList m_pending = new ArrayList();
   private int m_last_id = 0;
   private DiskFileDevice m_disk_device = new DiskFileDevice("disk");
   private MemoryFileDevice m_memory_device = new MemoryFileDevice();
   private final HashMap m_texturepack_devices = new HashMap();
   private final HashMap m_texturepack_devicelists = new HashMap();
   private DeviceList m_default_device = new DeviceList();
   private final ExecutorService executor;
   private final AtomicBoolean lock = new AtomicBoolean(false);
   private final ArrayList m_added = new ArrayList();
   public static final HashMap TexturePackCompression = new HashMap();

   public FileSystemImpl() {
      this.m_default_device.add(this.m_disk_device);
      this.m_default_device.add(this.m_memory_device);
      int var1 = Runtime.getRuntime().availableProcessors() <= 4 ? 2 : 4;
      this.executor = Executors.newFixedThreadPool(var1);
   }

   public boolean mount(IFileDevice var1) {
      return true;
   }

   public boolean unMount(IFileDevice var1) {
      return this.m_devices.remove(var1);
   }

   public IFile open(DeviceList var1, String var2, int var3) {
      IFile var4 = var1.createFile();
      if (var4 != null) {
         if (var4.open(var2, var3)) {
            return var4;
         } else {
            var4.release();
            return null;
         }
      } else {
         return null;
      }
   }

   public void close(IFile var1) {
      var1.close();
      var1.release();
   }

   public int openAsync(DeviceList var1, String var2, int var3, IFileTask2Callback var4) {
      IFile var5 = var1.createFile();
      if (var5 != null) {
         FileSystemImpl.OpenTask var6 = new FileSystemImpl.OpenTask(this);
         var6.m_file = var5;
         var6.m_path = var2;
         var6.m_mode = var3;
         var6.m_cb = var4;
         return this.runAsync((FileTask)var6);
      } else {
         return -1;
      }
   }

   public void closeAsync(IFile var1, IFileTask2Callback var2) {
      FileSystemImpl.CloseTask var3 = new FileSystemImpl.CloseTask(this);
      var3.m_file = var1;
      var3.m_cb = var2;
      this.runAsync((FileTask)var3);
   }

   public void cancelAsync(int var1) {
      if (var1 != -1) {
         int var2;
         FileSystemImpl.AsyncItem var3;
         for(var2 = 0; var2 < this.m_pending.size(); ++var2) {
            var3 = (FileSystemImpl.AsyncItem)this.m_pending.get(var2);
            if (var3.m_id == var1) {
               var3.m_future.cancel(false);
               return;
            }
         }

         for(var2 = 0; var2 < this.m_in_progress.size(); ++var2) {
            var3 = (FileSystemImpl.AsyncItem)this.m_in_progress.get(var2);
            if (var3.m_id == var1) {
               var3.m_future.cancel(false);
               return;
            }
         }

         while(!this.lock.compareAndSet(false, true)) {
            Thread.onSpinWait();
         }

         for(var2 = 0; var2 < this.m_added.size(); ++var2) {
            var3 = (FileSystemImpl.AsyncItem)this.m_added.get(var2);
            if (var3.m_id == var1) {
               var3.m_future.cancel(false);
               break;
            }
         }

         this.lock.set(false);
      }
   }

   public InputStream openStream(DeviceList var1, String var2) throws IOException {
      return var1.createStream(var2);
   }

   public void closeStream(InputStream var1) {
   }

   private int runAsync(FileSystemImpl.AsyncItem var1) {
      Thread var2 = Thread.currentThread();
      if (var2 != GameWindow.GameThread && var2 != GameLoadingState.loader) {
         boolean var3 = true;
      }

      while(!this.lock.compareAndSet(false, true)) {
         Thread.onSpinWait();
      }

      var1.m_id = this.m_last_id++;
      if (this.m_last_id < 0) {
         this.m_last_id = 0;
      }

      this.m_added.add(var1);
      this.lock.set(false);
      return var1.m_id;
   }

   public int runAsync(FileTask var1) {
      FileSystemImpl.AsyncItem var2 = new FileSystemImpl.AsyncItem();
      var2.m_task = var1;
      var2.m_future = new FutureTask(var1);
      return this.runAsync(var2);
   }

   public void updateAsyncTransactions() {
      int var1 = Math.min(this.m_in_progress.size(), 16);

      int var2;
      FileSystemImpl.AsyncItem var3;
      for(var2 = 0; var2 < var1; ++var2) {
         var3 = (FileSystemImpl.AsyncItem)this.m_in_progress.get(var2);
         if (var3.m_future.isDone()) {
            this.m_in_progress.remove(var2--);
            --var1;
            if (var3.m_future.isCancelled()) {
               boolean var4 = true;
            } else {
               Object var11 = null;

               try {
                  var11 = var3.m_future.get();
               } catch (Throwable var8) {
                  ExceptionLogger.logException(var8, var3.m_task.getErrorMessage());
               }

               var3.m_task.handleResult(var11);
            }

            var3.m_task.done();
            var3.m_task = null;
            var3.m_future = null;
         }
      }

      while(!this.lock.compareAndSet(false, true)) {
         Thread.onSpinWait();
      }

      boolean var9 = true;
      if (var9) {
         for(int var10 = 0; var10 < this.m_added.size(); ++var10) {
            FileSystemImpl.AsyncItem var12 = (FileSystemImpl.AsyncItem)this.m_added.get(var10);
            int var5 = this.m_pending.size();

            for(int var6 = 0; var6 < this.m_pending.size(); ++var6) {
               FileSystemImpl.AsyncItem var7 = (FileSystemImpl.AsyncItem)this.m_pending.get(var6);
               if (var12.m_task.m_priority > var7.m_task.m_priority) {
                  var5 = var6;
                  break;
               }
            }

            this.m_pending.add(var5, var12);
         }
      } else {
         this.m_pending.addAll(this.m_added);
      }

      this.m_added.clear();
      this.lock.set(false);
      var2 = 16 - this.m_in_progress.size();

      while(var2 > 0 && !this.m_pending.isEmpty()) {
         var3 = (FileSystemImpl.AsyncItem)this.m_pending.remove(0);
         if (!var3.m_future.isCancelled()) {
            this.m_in_progress.add(var3);
            this.executor.submit(var3.m_future);
            --var2;
         }
      }

   }

   public boolean hasWork() {
      if (this.m_pending.isEmpty() && this.m_in_progress.isEmpty()) {
         while(!this.lock.compareAndSet(false, true)) {
            Thread.onSpinWait();
         }

         boolean var1 = !this.m_added.isEmpty();
         this.lock.set(false);
         return var1;
      } else {
         return true;
      }
   }

   public DeviceList getDefaultDevice() {
      return this.m_default_device;
   }

   public void mountTexturePack(String var1, FileSystem.TexturePackTextures var2, int var3) {
      TexturePackDevice var4 = new TexturePackDevice(var1, var3);
      if (var2 != null) {
         try {
            var4.getSubTextureInfo(var2);
         } catch (IOException var6) {
            ExceptionLogger.logException(var6);
         }
      }

      this.m_texturepack_devices.put(var1, var4);
      DeviceList var5 = new DeviceList();
      var5.add(var4);
      this.m_texturepack_devicelists.put(var4.name(), var5);
   }

   public DeviceList getTexturePackDevice(String var1) {
      return (DeviceList)this.m_texturepack_devicelists.get(var1);
   }

   public int getTexturePackFlags(String var1) {
      return ((TexturePackDevice)this.m_texturepack_devices.get(var1)).getTextureFlags();
   }

   public boolean getTexturePackAlpha(String var1, String var2) {
      return ((TexturePackDevice)this.m_texturepack_devices.get(var1)).isAlpha(var2);
   }

   private static final class OpenTask extends FileTask {
      IFile m_file;
      String m_path;
      int m_mode;
      IFileTask2Callback m_cb;

      OpenTask(FileSystem var1) {
         super(var1);
      }

      public Object call() throws Exception {
         return this.m_file.open(this.m_path, this.m_mode);
      }

      public void handleResult(Object var1) {
         if (this.m_cb != null) {
            this.m_cb.onFileTaskFinished(this.m_file, var1);
         }

      }

      public void done() {
         if ((this.m_mode & 5) == 5) {
            this.m_file_system.closeAsync(this.m_file, (IFileTask2Callback)null);
         }

         this.m_file = null;
         this.m_path = null;
         this.m_cb = null;
      }
   }

   private static final class CloseTask extends FileTask {
      IFile m_file;
      IFileTask2Callback m_cb;

      CloseTask(FileSystem var1) {
         super(var1);
      }

      public Object call() throws Exception {
         this.m_file.close();
         this.m_file.release();
         return null;
      }

      public void handleResult(Object var1) {
         if (this.m_cb != null) {
            this.m_cb.onFileTaskFinished(this.m_file, var1);
         }

      }

      public void done() {
         this.m_file = null;
         this.m_cb = null;
      }
   }

   private static final class AsyncItem {
      int m_id;
      FileTask m_task;
      FutureTask m_future;
   }
}
