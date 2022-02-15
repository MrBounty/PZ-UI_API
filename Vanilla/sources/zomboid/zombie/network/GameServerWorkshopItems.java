package zombie.network;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.core.logger.ExceptionLogger;
import zombie.core.znet.ISteamWorkshopCallback;
import zombie.core.znet.SteamUGCDetails;
import zombie.core.znet.SteamUtils;
import zombie.core.znet.SteamWorkshop;
import zombie.core.znet.SteamWorkshopItem;
import zombie.debug.DebugLog;

public class GameServerWorkshopItems {
   private static void noise(String var0) {
      DebugLog.log("Workshop: " + var0);
   }

   public static boolean Install(ArrayList var0) {
      if (!GameServer.bServer) {
         return false;
      } else if (var0.isEmpty()) {
         return true;
      } else {
         ArrayList var1 = new ArrayList();
         Iterator var2 = var0.iterator();

         long var3;
         while(var2.hasNext()) {
            var3 = (Long)var2.next();
            GameServerWorkshopItems.WorkshopItem var5 = new GameServerWorkshopItems.WorkshopItem(var3);
            var1.add(var5);
         }

         if (!QueryItemDetails(var1)) {
            return false;
         } else {
            while(true) {
               SteamUtils.runLoop();
               boolean var7 = false;

               for(int var8 = 0; var8 < var1.size(); ++var8) {
                  GameServerWorkshopItems.WorkshopItem var4 = (GameServerWorkshopItems.WorkshopItem)var1.get(var8);
                  var4.update();
                  if (var4.state == GameServerWorkshopItems.WorkshopInstallState.Fail) {
                     return false;
                  }

                  if (var4.state != GameServerWorkshopItems.WorkshopInstallState.Ready) {
                     var7 = true;
                     break;
                  }
               }

               if (!var7) {
                  GameServer.WorkshopInstallFolders = new String[var0.size()];
                  GameServer.WorkshopTimeStamps = new long[var0.size()];

                  for(int var9 = 0; var9 < var0.size(); ++var9) {
                     var3 = (Long)var0.get(var9);
                     String var10 = SteamWorkshop.instance.GetItemInstallFolder(var3);
                     if (var10 == null) {
                        noise("GetItemInstallFolder() failed ID=" + var3);
                        return false;
                     }

                     noise(var3 + " installed to " + var10);
                     GameServer.WorkshopInstallFolders[var9] = var10;
                     GameServer.WorkshopTimeStamps[var9] = SteamWorkshop.instance.GetItemInstallTimeStamp(var3);
                  }

                  return true;
               }

               try {
                  Thread.sleep(33L);
               } catch (Exception var6) {
                  var6.printStackTrace();
               }
            }
         }
      }
   }

   private static boolean QueryItemDetails(ArrayList var0) {
      long[] var1 = new long[var0.size()];

      for(int var2 = 0; var2 < var0.size(); ++var2) {
         GameServerWorkshopItems.WorkshopItem var3 = (GameServerWorkshopItems.WorkshopItem)var0.get(var2);
         var1[var2] = var3.ID;
      }

      GameServerWorkshopItems.ItemQuery var8 = new GameServerWorkshopItems.ItemQuery();
      var8.handle = SteamWorkshop.instance.CreateQueryUGCDetailsRequest(var1, var8);
      if (var8.handle == 0L) {
         return false;
      } else {
         while(true) {
            SteamUtils.runLoop();
            if (var8.isCompleted()) {
               Iterator var9 = var8.details.iterator();

               while(true) {
                  while(var9.hasNext()) {
                     SteamUGCDetails var4 = (SteamUGCDetails)var9.next();
                     Iterator var5 = var0.iterator();

                     while(var5.hasNext()) {
                        GameServerWorkshopItems.WorkshopItem var6 = (GameServerWorkshopItems.WorkshopItem)var5.next();
                        if (var6.ID == var4.getID()) {
                           var6.details = var4;
                           break;
                        }
                     }
                  }

                  return true;
               }
            }

            if (var8.isNotCompleted()) {
               return false;
            }

            try {
               Thread.sleep(33L);
            } catch (Exception var7) {
               var7.printStackTrace();
            }
         }
      }
   }

   private static class WorkshopItem implements ISteamWorkshopCallback {
      long ID;
      GameServerWorkshopItems.WorkshopInstallState state;
      long downloadStartTime;
      long downloadQueryTime;
      String error;
      SteamUGCDetails details;

      WorkshopItem(long var1) {
         this.state = GameServerWorkshopItems.WorkshopInstallState.CheckItemState;
         this.ID = var1;
      }

      void update() {
         switch(this.state) {
         case CheckItemState:
            this.CheckItemState();
            break;
         case DownloadPending:
            this.DownloadPending();
         case Ready:
         }

      }

      void setState(GameServerWorkshopItems.WorkshopInstallState var1) {
         GameServerWorkshopItems.noise("item state " + this.state + " -> " + var1 + " ID=" + this.ID);
         this.state = var1;
      }

      void CheckItemState() {
         long var1 = SteamWorkshop.instance.GetItemState(this.ID);
         String var10000 = SteamWorkshopItem.ItemState.toString(var1);
         GameServerWorkshopItems.noise("GetItemState()=" + var10000 + " ID=" + this.ID);
         if (SteamWorkshopItem.ItemState.Installed.and(var1) && this.details != null && this.details.getTimeCreated() != 0L && this.details.getTimeUpdated() != SteamWorkshop.instance.GetItemInstallTimeStamp(this.ID)) {
            GameServerWorkshopItems.noise("Installed status but timeUpdated doesn't match!!!");
            this.RemoveFolderForReinstall();
            var1 |= (long)SteamWorkshopItem.ItemState.NeedsUpdate.getValue();
         }

         if (var1 != (long)SteamWorkshopItem.ItemState.None.getValue() && !SteamWorkshopItem.ItemState.NeedsUpdate.and(var1)) {
            if (SteamWorkshopItem.ItemState.Installed.and(var1)) {
               this.setState(GameServerWorkshopItems.WorkshopInstallState.Ready);
            } else {
               this.error = "UnknownItemState";
               this.setState(GameServerWorkshopItems.WorkshopInstallState.Fail);
            }
         } else if (SteamWorkshop.instance.DownloadItem(this.ID, true, this)) {
            this.setState(GameServerWorkshopItems.WorkshopInstallState.DownloadPending);
            this.downloadStartTime = System.currentTimeMillis();
         } else {
            this.error = "DownloadItemFalse";
            this.setState(GameServerWorkshopItems.WorkshopInstallState.Fail);
         }
      }

      void RemoveFolderForReinstall() {
         String var1 = SteamWorkshop.instance.GetItemInstallFolder(this.ID);
         if (var1 == null) {
            GameServerWorkshopItems.noise("not removing install folder because GetItemInstallFolder() failed ID=" + this.ID);
         } else {
            Path var2 = Paths.get(var1);
            if (!Files.exists(var2, new LinkOption[0])) {
               GameServerWorkshopItems.noise("not removing install folder because it does not exist : \"" + var1 + "\"");
            } else {
               try {
                  Files.walkFileTree(var2, new SimpleFileVisitor() {
                     public FileVisitResult visitFile(Path var1, BasicFileAttributes var2) throws IOException {
                        Files.delete(var1);
                        return FileVisitResult.CONTINUE;
                     }

                     public FileVisitResult postVisitDirectory(Path var1, IOException var2) throws IOException {
                        Files.delete(var1);
                        return FileVisitResult.CONTINUE;
                     }
                  });
               } catch (Exception var4) {
                  ExceptionLogger.logException(var4);
               }

            }
         }
      }

      void DownloadPending() {
         long var1 = System.currentTimeMillis();
         if (this.downloadQueryTime + 100L <= var1) {
            this.downloadQueryTime = var1;
            long var3 = SteamWorkshop.instance.GetItemState(this.ID);
            String var10000 = SteamWorkshopItem.ItemState.toString(var3);
            GameServerWorkshopItems.noise("DownloadPending GetItemState()=" + var10000 + " ID=" + this.ID);
            if (SteamWorkshopItem.ItemState.NeedsUpdate.and(var3)) {
               long[] var5 = new long[2];
               if (SteamWorkshop.instance.GetItemDownloadInfo(this.ID, var5)) {
                  GameServerWorkshopItems.noise("download " + var5[0] + "/" + var5[1] + " ID=" + this.ID);
               }

            }
         }
      }

      public void onItemCreated(long var1, boolean var3) {
      }

      public void onItemNotCreated(int var1) {
      }

      public void onItemUpdated(boolean var1) {
      }

      public void onItemNotUpdated(int var1) {
      }

      public void onItemSubscribed(long var1) {
         GameServerWorkshopItems.noise("onItemSubscribed itemID=" + var1);
      }

      public void onItemNotSubscribed(long var1, int var3) {
         GameServerWorkshopItems.noise("onItemNotSubscribed itemID=" + var1 + " result=" + var3);
      }

      public void onItemDownloaded(long var1) {
         GameServerWorkshopItems.noise("onItemDownloaded itemID=" + var1 + " time=" + (System.currentTimeMillis() - this.downloadStartTime) + " ms");
         if (var1 == this.ID) {
            SteamWorkshop.instance.RemoveCallback(this);
            this.setState(GameServerWorkshopItems.WorkshopInstallState.CheckItemState);
         }
      }

      public void onItemNotDownloaded(long var1, int var3) {
         GameServerWorkshopItems.noise("onItemNotDownloaded itemID=" + var1 + " result=" + var3);
         if (var1 == this.ID) {
            SteamWorkshop.instance.RemoveCallback(this);
            this.error = "ItemNotDownloaded";
            this.setState(GameServerWorkshopItems.WorkshopInstallState.Fail);
         }
      }

      public void onItemQueryCompleted(long var1, int var3) {
         GameServerWorkshopItems.noise("onItemQueryCompleted handle=" + var1 + " numResult=" + var3);
      }

      public void onItemQueryNotCompleted(long var1, int var3) {
         GameServerWorkshopItems.noise("onItemQueryNotCompleted handle=" + var1 + " result=" + var3);
      }
   }

   private static enum WorkshopInstallState {
      CheckItemState,
      DownloadPending,
      Ready,
      Fail;

      // $FF: synthetic method
      private static GameServerWorkshopItems.WorkshopInstallState[] $values() {
         return new GameServerWorkshopItems.WorkshopInstallState[]{CheckItemState, DownloadPending, Ready, Fail};
      }
   }

   private static final class ItemQuery implements ISteamWorkshopCallback {
      long handle;
      ArrayList details;
      boolean bCompleted;
      boolean bNotCompleted;

      public boolean isCompleted() {
         return this.bCompleted;
      }

      public boolean isNotCompleted() {
         return this.bNotCompleted;
      }

      public void onItemCreated(long var1, boolean var3) {
      }

      public void onItemNotCreated(int var1) {
      }

      public void onItemUpdated(boolean var1) {
      }

      public void onItemNotUpdated(int var1) {
      }

      public void onItemSubscribed(long var1) {
      }

      public void onItemNotSubscribed(long var1, int var3) {
      }

      public void onItemDownloaded(long var1) {
      }

      public void onItemNotDownloaded(long var1, int var3) {
      }

      public void onItemQueryCompleted(long var1, int var3) {
         GameServerWorkshopItems.noise("onItemQueryCompleted handle=" + var1 + " numResult=" + var3);
         if (var1 == this.handle) {
            SteamWorkshop.instance.RemoveCallback(this);
            ArrayList var4 = new ArrayList();

            for(int var5 = 0; var5 < var3; ++var5) {
               SteamUGCDetails var6 = SteamWorkshop.instance.GetQueryUGCResult(var1, var5);
               if (var6 != null) {
                  var4.add(var6);
               }
            }

            this.details = var4;
            SteamWorkshop.instance.ReleaseQueryUGCRequest(var1);
            this.bCompleted = true;
         }
      }

      public void onItemQueryNotCompleted(long var1, int var3) {
         GameServerWorkshopItems.noise("onItemQueryNotCompleted handle=" + var1 + " result=" + var3);
         if (var1 == this.handle) {
            SteamWorkshop.instance.RemoveCallback(this);
            SteamWorkshop.instance.ReleaseQueryUGCRequest(var1);
            this.bNotCompleted = true;
         }
      }
   }
}
