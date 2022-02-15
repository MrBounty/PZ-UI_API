package zombie.asset;

public interface AssetStateObserver {
   void onStateChanged(Asset.State var1, Asset.State var2, Asset var3);
}
