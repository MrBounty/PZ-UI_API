package zombie;

import zombie.characters.IsoPlayer;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemPickerJava;
import zombie.iso.ContainerOverlays;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.TileOverlays;
import zombie.iso.objects.IsoWorldInventoryObject;
import zombie.iso.sprite.IsoSprite;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class LoadGridsquarePerformanceWorkaround {
   public static void init(int var0, int var1) {
      if (!GameClient.bClient) {
         LoadGridsquarePerformanceWorkaround.ItemPicker.instance.init();
      }
   }

   public static void LoadGridsquare(IsoGridSquare var0) {
      if (LoadGridsquarePerformanceWorkaround.ItemPicker.instance.begin(var0)) {
         IsoObject[] var1 = (IsoObject[])var0.getObjects().getElements();
         int var2 = var0.getObjects().size();

         for(int var3 = 0; var3 < var2; ++var3) {
            IsoObject var4 = var1[var3];
            if (!(var4 instanceof IsoWorldInventoryObject)) {
               if (!GameClient.bClient) {
                  LoadGridsquarePerformanceWorkaround.ItemPicker.instance.checkObject(var4);
               }

               if (var4.sprite != null && var4.sprite.name != null && !ContainerOverlays.instance.hasOverlays(var4)) {
                  TileOverlays.instance.updateTileOverlaySprite(var4);
               }
            }
         }
      }

      LoadGridsquarePerformanceWorkaround.ItemPicker.instance.end(var0);
   }

   private static class ItemPicker {
      public static final LoadGridsquarePerformanceWorkaround.ItemPicker instance = new LoadGridsquarePerformanceWorkaround.ItemPicker();
      private IsoGridSquare square;

      public void init() {
      }

      public boolean begin(IsoGridSquare var1) {
         if (var1.isOverlayDone()) {
            this.square = null;
            return false;
         } else {
            this.square = var1;
            return true;
         }
      }

      public void checkObject(IsoObject var1) {
         IsoSprite var2 = var1.getSprite();
         if (var2 != null && var2.getName() != null) {
            ItemContainer var3 = var1.getContainer();
            if (var3 != null && !var3.isExplored()) {
               ItemPickerJava.fillContainer(var3, IsoPlayer.getInstance());
               var3.setExplored(true);
               if (GameServer.bServer) {
                  GameServer.sendItemsInContainer(var1, var3);
               }
            }

            if (var3 == null || !var3.isEmpty()) {
               ItemPickerJava.updateOverlaySprite(var1);
            }
         }
      }

      public void end(IsoGridSquare var1) {
         var1.setOverlayDone(true);
      }
   }
}
