package zombie.inventory.types;

import zombie.inventory.InventoryItem;
import zombie.scripting.objects.Item;

public final class ComboItem extends InventoryItem {
   public ComboItem(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
   }

   public ComboItem(String var1, String var2, String var3, Item var4) {
      super(var1, var2, var3, var4);
   }

   public int getSaveType() {
      return Item.Type.Normal.ordinal();
   }
}
