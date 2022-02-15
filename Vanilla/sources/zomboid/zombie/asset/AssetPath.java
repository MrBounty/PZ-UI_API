package zombie.asset;

import zombie.util.StringUtils;

public final class AssetPath {
   protected String m_path;

   public AssetPath(String var1) {
      this.m_path = var1;
   }

   public boolean isValid() {
      return !StringUtils.isNullOrEmpty(this.m_path);
   }

   public int getHash() {
      return this.m_path.hashCode();
   }

   public String getPath() {
      return this.m_path;
   }

   public String toString() {
      String var10000 = this.getClass().getSimpleName();
      return var10000 + "{ \"" + this.getPath() + "\" }";
   }
}
