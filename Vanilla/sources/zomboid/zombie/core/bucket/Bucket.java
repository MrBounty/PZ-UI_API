package zombie.core.bucket;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import zombie.DebugFileWatcher;
import zombie.PredicatedFileWatcher;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.textures.Texture;

public final class Bucket {
   private String m_name;
   private final HashMap m_textures = new HashMap();
   private static final FileSystem m_fs = FileSystems.getDefault();
   private final PredicatedFileWatcher m_fileWatcher = new PredicatedFileWatcher((var1) -> {
      return this.HasTexture(var1);
   }, (var1) -> {
      Texture var2 = this.getTexture(var1);
      var2.reloadFromFile(var1);
      ModelManager.instance.reloadAllOutfits();
   });

   public Bucket() {
      DebugFileWatcher.instance.add(this.m_fileWatcher);
   }

   public void AddTexture(Path var1, Texture var2) {
      if (var2 != null) {
         this.m_textures.put(var1, var2);
      }
   }

   public void AddTexture(String var1, Texture var2) {
      if (var2 != null) {
         this.AddTexture(m_fs.getPath(var1), var2);
      }
   }

   public void Dispose() {
      Iterator var1 = this.m_textures.values().iterator();

      while(var1.hasNext()) {
         Texture var2 = (Texture)var1.next();
         var2.destroy();
      }

      this.m_textures.clear();
   }

   public Texture getTexture(Path var1) {
      return (Texture)this.m_textures.get(var1);
   }

   public Texture getTexture(String var1) {
      return this.getTexture(m_fs.getPath(var1));
   }

   public boolean HasTexture(Path var1) {
      return this.m_textures.containsKey(var1);
   }

   public boolean HasTexture(String var1) {
      return this.HasTexture(m_fs.getPath(var1));
   }

   String getName() {
      return this.m_name;
   }

   void setName(String var1) {
      this.m_name = var1;
   }

   public void forgetTexture(String var1) {
      this.m_textures.remove(var1);
   }
}
