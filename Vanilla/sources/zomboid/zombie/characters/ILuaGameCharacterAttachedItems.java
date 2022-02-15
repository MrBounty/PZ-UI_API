package zombie.characters;

import zombie.characters.AttachedItems.AttachedItems;
import zombie.characters.AttachedItems.AttachedLocationGroup;
import zombie.inventory.InventoryItem;

public interface ILuaGameCharacterAttachedItems {
   AttachedItems getAttachedItems();

   void setAttachedItems(AttachedItems var1);

   InventoryItem getAttachedItem(String var1);

   void setAttachedItem(String var1, InventoryItem var2);

   void removeAttachedItem(InventoryItem var1);

   void clearAttachedItems();

   AttachedLocationGroup getAttachedLocationGroup();
}
