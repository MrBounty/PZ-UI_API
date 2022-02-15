package zombie.worldMap;

import zombie.fileSystem.FileSystem;
import zombie.fileSystem.FileTask;
import zombie.fileSystem.IFileTaskCallback;

public final class FileTask_LoadWorldMapBinary extends FileTask {
   WorldMapData m_worldMapData;
   String m_filename;

   public FileTask_LoadWorldMapBinary(WorldMapData var1, String var2, FileSystem var3, IFileTaskCallback var4) {
      super(var3, var4);
      this.m_worldMapData = var1;
      this.m_filename = var2;
   }

   public String getErrorMessage() {
      return this.m_filename;
   }

   public void done() {
      this.m_worldMapData = null;
      this.m_filename = null;
   }

   public Object call() throws Exception {
      WorldMapBinary var1 = new WorldMapBinary();
      return var1.read(this.m_filename, this.m_worldMapData) ? Boolean.TRUE : Boolean.FALSE;
   }
}
