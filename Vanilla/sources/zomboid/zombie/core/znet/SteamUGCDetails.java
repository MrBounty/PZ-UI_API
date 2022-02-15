package zombie.core.znet;

import zombie.debug.DebugLog;

public class SteamUGCDetails {
   private long ID;
   private String title;
   private long timeCreated;
   private long timeUpdated;
   private int fileSize;
   private long[] childIDs;

   public SteamUGCDetails(long var1, String var3, long var4, long var6, int var8, long[] var9) {
      this.ID = var1;
      this.title = var3;
      this.timeCreated = var4;
      this.timeUpdated = var6;
      this.fileSize = var8;
      this.childIDs = var9;
   }

   public long getID() {
      return this.ID;
   }

   public String getIDString() {
      return SteamUtils.convertSteamIDToString(this.ID);
   }

   public String getTitle() {
      return this.title;
   }

   public long getTimeCreated() {
      return this.timeCreated;
   }

   public long getTimeUpdated() {
      return this.timeUpdated;
   }

   public int getFileSize() {
      return this.fileSize;
   }

   public long[] getChildren() {
      return this.childIDs;
   }

   public int getNumChildren() {
      return this.childIDs == null ? 0 : this.childIDs.length;
   }

   public long getChildID(int var1) {
      if (var1 >= 0 && var1 < this.getNumChildren()) {
         return this.childIDs[var1];
      } else {
         throw new IndexOutOfBoundsException("invalid child index");
      }
   }

   public String getState() {
      long var1 = SteamWorkshop.instance.GetItemState(this.ID);
      if (!SteamWorkshopItem.ItemState.Subscribed.and(var1)) {
         return "NotSubscribed";
      } else if (SteamWorkshopItem.ItemState.DownloadPending.and(var1)) {
         String var10000 = SteamWorkshopItem.ItemState.toString(var1);
         DebugLog.log(var10000 + " ID=" + this.ID);
         return "Downloading";
      } else if (SteamWorkshopItem.ItemState.NeedsUpdate.and(var1)) {
         return "NeedsUpdate";
      } else {
         return SteamWorkshopItem.ItemState.Installed.and(var1) ? "Installed" : "Error";
      }
   }
}
