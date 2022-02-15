package zombie.asset;

import gnu.trove.map.hash.TLongObjectHashMap;
import zombie.fileSystem.FileSystem;

public final class AssetManagers {
   private final AssetManagers.AssetManagerTable m_managers = new AssetManagers.AssetManagerTable();
   private final FileSystem m_file_system;

   public AssetManagers(FileSystem var1) {
      this.m_file_system = var1;
   }

   public AssetManager get(AssetType var1) {
      return (AssetManager)this.m_managers.get(var1.type);
   }

   public void add(AssetType var1, AssetManager var2) {
      this.m_managers.put(var1.type, var2);
   }

   public FileSystem getFileSystem() {
      return this.m_file_system;
   }

   public static final class AssetManagerTable extends TLongObjectHashMap {
   }
}
