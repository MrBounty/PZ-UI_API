package zombie.characters.WornItems;

import zombie.inventory.InventoryItem;

public final class WornItem {
   protected final String location;
   protected final InventoryItem item;

   public WornItem(String var1, InventoryItem var2) {
      if (var1 == null) {
         throw new NullPointerException("location is null");
      } else if (var1.isEmpty()) {
         throw new IllegalArgumentException("location is empty");
      } else if (var2 == null) {
         throw new NullPointerException("item is null");
      } else {
         this.location = var1;
         this.item = var2;
      }
   }

   public String getLocation() {
      return this.location;
   }

   public InventoryItem getItem() {
      return this.item;
   }
}
