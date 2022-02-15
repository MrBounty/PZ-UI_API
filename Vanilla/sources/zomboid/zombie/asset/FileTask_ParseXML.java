package zombie.asset;

import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileTask;
import zombie.fileSystem.IFileTaskCallback;
import zombie.util.PZXmlUtil;

public final class FileTask_ParseXML extends FileTask {
   Class m_class;
   String m_filename;

   public FileTask_ParseXML(Class var1, String var2, IFileTaskCallback var3, FileSystem var4) {
      super(var4, var3);
      this.m_class = var1;
      this.m_filename = var2;
   }

   public String getErrorMessage() {
      return this.m_filename;
   }

   public void done() {
      this.m_class = null;
      this.m_filename = null;
   }

   public Object call() throws Exception {
      return PZXmlUtil.parse(this.m_class, this.m_filename);
   }
}
