package zombie.core.znet;

public interface ISteamWorkshopCallback {
   void onItemCreated(long var1, boolean var3);

   void onItemNotCreated(int var1);

   void onItemUpdated(boolean var1);

   void onItemNotUpdated(int var1);

   void onItemSubscribed(long var1);

   void onItemNotSubscribed(long var1, int var3);

   void onItemDownloaded(long var1);

   void onItemNotDownloaded(long var1, int var3);

   void onItemQueryCompleted(long var1, int var3);

   void onItemQueryNotCompleted(long var1, int var3);
}
