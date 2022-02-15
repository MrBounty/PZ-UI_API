package zombie.inventory.types;

import java.util.ArrayList;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemType;
import zombie.scripting.objects.Item;

public final class KeyRing extends InventoryItem {
   private final ArrayList keys = new ArrayList();

   public KeyRing(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
      this.cat = ItemType.KeyRing;
   }

   public int getSaveType() {
      return Item.Type.KeyRing.ordinal();
   }

   public void addKey(Key var1) {
      this.keys.add(var1);
   }

   public boolean containsKeyId(int var1) {
      for(int var2 = 0; var2 < this.keys.size(); ++var2) {
         if (((Key)this.keys.get(var2)).getKeyId() == var1) {
            return true;
         }
      }

      return false;
   }

   public String getCategory() {
      return this.mainCategory != null ? this.mainCategory : "Key Ring";
   }

   public ArrayList getKeys() {
      return this.keys;
   }

   public void setKeys(ArrayList var1) {
      var1.clear();
      this.keys.addAll(var1);
   }
}
