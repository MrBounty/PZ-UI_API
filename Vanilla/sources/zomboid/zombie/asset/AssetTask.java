package zombie.asset;

public abstract class AssetTask {
   public Asset m_asset;

   public AssetTask(Asset var1) {
      this.m_asset = var1;
   }

   public abstract void execute();

   public abstract void cancel();
}
