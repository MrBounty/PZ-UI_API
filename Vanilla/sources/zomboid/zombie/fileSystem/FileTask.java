package zombie.fileSystem;

import java.util.concurrent.Callable;

public abstract class FileTask implements Callable {
   protected final FileSystem m_file_system;
   protected final IFileTaskCallback m_cb;
   protected int m_priority = 5;

   public FileTask(FileSystem var1) {
      this.m_file_system = var1;
      this.m_cb = null;
   }

   public FileTask(FileSystem var1, IFileTaskCallback var2) {
      this.m_file_system = var1;
      this.m_cb = var2;
   }

   public void handleResult(Object var1) {
      if (this.m_cb != null) {
         this.m_cb.onFileTaskFinished(var1);
      }

   }

   public void setPriority(int var1) {
      this.m_priority = var1;
   }

   public abstract void done();

   public String getErrorMessage() {
      return null;
   }
}
