package zombie.core.znet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.DirectoryStream.Filter;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.ZomboidFileSystem;
import zombie.Lua.LuaEventManager;
import zombie.debug.DebugLog;
import zombie.network.GameServer;

public class SteamWorkshop implements ISteamWorkshopCallback {
   public static final SteamWorkshop instance = new SteamWorkshop();
   private ArrayList stagedItems = new ArrayList();
   private ArrayList callbacks = new ArrayList();

   public static void init() {
      if (SteamUtils.isSteamModeEnabled()) {
         instance.n_Init();
      }

      if (!GameServer.bServer) {
         instance.initWorkshopFolder();
      }

   }

   public static void shutdown() {
      if (SteamUtils.isSteamModeEnabled()) {
         instance.n_Shutdown();
      }

   }

   private void copyFile(File var1, File var2) {
      try {
         FileInputStream var3 = new FileInputStream(var1);

         try {
            FileOutputStream var4 = new FileOutputStream(var2);

            try {
               var4.getChannel().transferFrom(var3.getChannel(), 0L, var1.length());
            } catch (Throwable var9) {
               try {
                  var4.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }

               throw var9;
            }

            var4.close();
         } catch (Throwable var10) {
            try {
               var3.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }

            throw var10;
         }

         var3.close();
      } catch (IOException var11) {
         var11.printStackTrace();
      }

   }

   private void copyFileOrFolder(File var1, File var2) {
      if (var1.isDirectory()) {
         if (!var2.mkdirs()) {
            return;
         }

         String[] var3 = var1.list();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            this.copyFileOrFolder(new File(var1, var3[var4]), new File(var2, var3[var4]));
         }
      } else {
         this.copyFile(var1, var2);
      }

   }

   private void initWorkshopFolder() {
      File var1 = new File(this.getWorkshopFolder());
      if (var1.exists() || var1.mkdirs()) {
         File var2 = new File("Workshop" + File.separator + "ModTemplate");
         String var10002 = this.getWorkshopFolder();
         File var3 = new File(var10002 + File.separator + "ModTemplate");
         if (var2.exists() && !var3.exists()) {
            this.copyFileOrFolder(var2, var3);
         }

      }
   }

   public ArrayList loadStagedItems() {
      this.stagedItems.clear();
      Iterator var1 = this.getStageFolders().iterator();

      while(var1.hasNext()) {
         String var2 = (String)var1.next();
         SteamWorkshopItem var3 = new SteamWorkshopItem(var2);
         var3.readWorkshopTxt();
         this.stagedItems.add(var3);
      }

      return this.stagedItems;
   }

   public String getWorkshopFolder() {
      String var10000 = ZomboidFileSystem.instance.getCacheDir();
      return var10000 + File.separator + "Workshop";
   }

   public ArrayList getStageFolders() {
      ArrayList var1 = new ArrayList();
      Path var2 = FileSystems.getDefault().getPath(this.getWorkshopFolder());

      try {
         if (!Files.isDirectory(var2, new LinkOption[0])) {
            Files.createDirectories(var2);
         }
      } catch (IOException var11) {
         var11.printStackTrace();
         return var1;
      }

      Filter var3 = new Filter() {
         public boolean accept(Path var1) throws IOException {
            return Files.isDirectory(var1, new LinkOption[0]);
         }
      };

      try {
         DirectoryStream var4 = Files.newDirectoryStream(var2, var3);

         try {
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
               Path var6 = (Path)var5.next();
               String var7 = var6.toAbsolutePath().toString();
               var1.add(var7);
            }
         } catch (Throwable var9) {
            if (var4 != null) {
               try {
                  var4.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (var4 != null) {
            var4.close();
         }
      } catch (Exception var10) {
         var10.printStackTrace();
      }

      return var1;
   }

   public boolean CreateWorkshopItem(SteamWorkshopItem var1) {
      if (var1.getID() != null) {
         throw new RuntimeException("can't recreate an existing item");
      } else {
         return this.n_CreateItem();
      }
   }

   public boolean SubmitWorkshopItem(SteamWorkshopItem var1) {
      if (var1.getID() != null && SteamUtils.isValidSteamID(var1.getID())) {
         long var2 = SteamUtils.convertStringToSteamID(var1.getID());
         if (!this.n_StartItemUpdate(var2)) {
            return false;
         } else if (!this.n_SetItemTitle(var1.getTitle())) {
            return false;
         } else if (!this.n_SetItemDescription(var1.getSubmitDescription())) {
            return false;
         } else {
            int var4 = var1.getVisibilityInteger();
            if ("Mod Template".equals(var1.getTitle())) {
               var4 = 2;
            }

            if (!this.n_SetItemVisibility(var4)) {
               return false;
            } else {
               if (!this.n_SetItemTags(var1.getSubmitTags())) {
               }

               if (!this.n_SetItemContent(var1.getContentFolder())) {
                  return false;
               } else if (!this.n_SetItemPreview(var1.getPreviewImage())) {
                  return false;
               } else {
                  return this.n_SubmitItemUpdate(var1.getChangeNote());
               }
            }
         }
      } else {
         throw new RuntimeException("workshop ID is required");
      }
   }

   public boolean GetItemUpdateProgress(long[] var1) {
      return this.n_GetItemUpdateProgress(var1);
   }

   public String[] GetInstalledItemFolders() {
      return GameServer.bServer ? GameServer.WorkshopInstallFolders : this.n_GetInstalledItemFolders();
   }

   public long GetItemState(long var1) {
      return this.n_GetItemState(var1);
   }

   public String GetItemInstallFolder(long var1) {
      return this.n_GetItemInstallFolder(var1);
   }

   public long GetItemInstallTimeStamp(long var1) {
      return this.n_GetItemInstallTimeStamp(var1);
   }

   public boolean SubscribeItem(long var1, ISteamWorkshopCallback var3) {
      if (!this.callbacks.contains(var3)) {
         this.callbacks.add(var3);
      }

      return this.n_SubscribeItem(var1);
   }

   public boolean DownloadItem(long var1, boolean var3, ISteamWorkshopCallback var4) {
      if (!this.callbacks.contains(var4)) {
         this.callbacks.add(var4);
      }

      return this.n_DownloadItem(var1, var3);
   }

   public boolean GetItemDownloadInfo(long var1, long[] var3) {
      return this.n_GetItemDownloadInfo(var1, var3);
   }

   public long CreateQueryUGCDetailsRequest(long[] var1, ISteamWorkshopCallback var2) {
      if (!this.callbacks.contains(var2)) {
         this.callbacks.add(var2);
      }

      return this.n_CreateQueryUGCDetailsRequest(var1);
   }

   public SteamUGCDetails GetQueryUGCResult(long var1, int var3) {
      return this.n_GetQueryUGCResult(var1, var3);
   }

   public long[] GetQueryUGCChildren(long var1, int var3) {
      return this.n_GetQueryUGCChildren(var1, var3);
   }

   public boolean ReleaseQueryUGCRequest(long var1) {
      return this.n_ReleaseQueryUGCRequest(var1);
   }

   public void RemoveCallback(ISteamWorkshopCallback var1) {
      this.callbacks.remove(var1);
   }

   public String getIDFromItemInstallFolder(String var1) {
      if (var1 != null && var1.replace("\\", "/").contains("/workshop/content/108600/")) {
         File var2 = new File(var1);
         String var3 = var2.getName();
         if (SteamUtils.isValidSteamID(var3)) {
            return var3;
         }

         DebugLog.log("ERROR: " + var3 + " isn't a valid workshop item ID");
      }

      return null;
   }

   private native void n_Init();

   private native void n_Shutdown();

   private native boolean n_CreateItem();

   private native boolean n_StartItemUpdate(long var1);

   private native boolean n_SetItemTitle(String var1);

   private native boolean n_SetItemDescription(String var1);

   private native boolean n_SetItemVisibility(int var1);

   private native boolean n_SetItemTags(String[] var1);

   private native boolean n_SetItemContent(String var1);

   private native boolean n_SetItemPreview(String var1);

   private native boolean n_SubmitItemUpdate(String var1);

   private native boolean n_GetItemUpdateProgress(long[] var1);

   private native String[] n_GetInstalledItemFolders();

   private native long n_GetItemState(long var1);

   private native boolean n_SubscribeItem(long var1);

   private native boolean n_DownloadItem(long var1, boolean var3);

   private native String n_GetItemInstallFolder(long var1);

   private native long n_GetItemInstallTimeStamp(long var1);

   private native boolean n_GetItemDownloadInfo(long var1, long[] var3);

   private native long n_CreateQueryUGCDetailsRequest(long[] var1);

   private native SteamUGCDetails n_GetQueryUGCResult(long var1, int var3);

   private native long[] n_GetQueryUGCChildren(long var1, int var3);

   private native boolean n_ReleaseQueryUGCRequest(long var1);

   public void onItemCreated(long var1, boolean var3) {
      LuaEventManager.triggerEvent("OnSteamWorkshopItemCreated", SteamUtils.convertSteamIDToString(var1), var3);
   }

   public void onItemNotCreated(int var1) {
      LuaEventManager.triggerEvent("OnSteamWorkshopItemNotCreated", var1);
   }

   public void onItemUpdated(boolean var1) {
      LuaEventManager.triggerEvent("OnSteamWorkshopItemUpdated", var1);
   }

   public void onItemNotUpdated(int var1) {
      LuaEventManager.triggerEvent("OnSteamWorkshopItemNotUpdated", var1);
   }

   public void onItemSubscribed(long var1) {
      for(int var3 = 0; var3 < this.callbacks.size(); ++var3) {
         ((ISteamWorkshopCallback)this.callbacks.get(var3)).onItemSubscribed(var1);
      }

   }

   public void onItemNotSubscribed(long var1, int var3) {
      for(int var4 = 0; var4 < this.callbacks.size(); ++var4) {
         ((ISteamWorkshopCallback)this.callbacks.get(var4)).onItemNotSubscribed(var1, var3);
      }

   }

   public void onItemDownloaded(long var1) {
      for(int var3 = 0; var3 < this.callbacks.size(); ++var3) {
         ((ISteamWorkshopCallback)this.callbacks.get(var3)).onItemDownloaded(var1);
      }

   }

   public void onItemNotDownloaded(long var1, int var3) {
      for(int var4 = 0; var4 < this.callbacks.size(); ++var4) {
         ((ISteamWorkshopCallback)this.callbacks.get(var4)).onItemNotDownloaded(var1, var3);
      }

   }

   public void onItemQueryCompleted(long var1, int var3) {
      for(int var4 = 0; var4 < this.callbacks.size(); ++var4) {
         ((ISteamWorkshopCallback)this.callbacks.get(var4)).onItemQueryCompleted(var1, var3);
      }

   }

   public void onItemQueryNotCompleted(long var1, int var3) {
      for(int var4 = 0; var4 < this.callbacks.size(); ++var4) {
         ((ISteamWorkshopCallback)this.callbacks.get(var4)).onItemQueryNotCompleted(var1, var3);
      }

   }
}
