package zombie.asset;

import java.io.File;
import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileTask;
import zombie.fileSystem.IFileTaskCallback;

public final class FileTask_Exists extends FileTask {
   String fileName;

   public FileTask_Exists(String var1, IFileTaskCallback var2, FileSystem var3) {
      super(var3, var2);
      this.fileName = var1;
   }

   public void done() {
   }

   public Object call() throws Exception {
      return (new File(this.fileName)).exists();
   }
}
