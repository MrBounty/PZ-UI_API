package zombie.characters.WornItems;

import java.util.ArrayList;
import java.util.function.Consumer;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.InventoryContainer;
import zombie.util.StringUtils;

public final class WornItems {
   protected final BodyLocationGroup group;
   protected final ArrayList items = new ArrayList();

   public WornItems(BodyLocationGroup var1) {
      this.group = var1;
   }

   public WornItems(WornItems var1) {
      this.group = var1.group;
      this.copyFrom(var1);
   }

   public void copyFrom(WornItems var1) {
      if (this.group != var1.group) {
         throw new RuntimeException("group=" + this.group.id + " other.group=" + var1.group.id);
      } else {
         this.items.clear();
         this.items.addAll(var1.items);
      }
   }

   public BodyLocationGroup getBodyLocationGroup() {
      return this.group;
   }

   public WornItem get(int var1) {
      return (WornItem)this.items.get(var1);
   }

   public void setItem(String var1, InventoryItem var2) {
      this.group.checkValid(var1);
      int var3;
      if (!this.group.isMultiItem(var1)) {
         var3 = this.indexOf(var1);
         if (var3 != -1) {
            this.items.remove(var3);
         }
      }

      WornItem var4;
      for(var3 = 0; var3 < this.items.size(); ++var3) {
         var4 = (WornItem)this.items.get(var3);
         if (this.group.isExclusive(var1, var4.location)) {
            this.items.remove(var3--);
         }
      }

      if (var2 != null) {
         this.remove(var2);
         var3 = this.items.size();

         for(int var6 = 0; var6 < this.items.size(); ++var6) {
            WornItem var5 = (WornItem)this.items.get(var6);
            if (this.group.indexOf(var5.getLocation()) > this.group.indexOf(var1)) {
               var3 = var6;
               break;
            }
         }

         var4 = new WornItem(var1, var2);
         this.items.add(var3, var4);
      }
   }

   public InventoryItem getItem(String var1) {
      this.group.checkValid(var1);
      int var2 = this.indexOf(var1);
      return var2 == -1 ? null : ((WornItem)this.items.get(var2)).item;
   }

   public InventoryItem getItemByIndex(int var1) {
      return var1 >= 0 && var1 < this.items.size() ? ((WornItem)this.items.get(var1)).getItem() : null;
   }

   public void remove(InventoryItem var1) {
      int var2 = this.indexOf(var1);
      if (var2 != -1) {
         this.items.remove(var2);
      }
   }

   public void clear() {
      this.items.clear();
   }

   public String getLocation(InventoryItem var1) {
      int var2 = this.indexOf(var1);
      return var2 == -1 ? null : ((WornItem)this.items.get(var2)).getLocation();
   }

   public boolean contains(InventoryItem var1) {
      return this.indexOf(var1) != -1;
   }

   public int size() {
      return this.items.size();
   }

   public boolean isEmpty() {
      return this.items.isEmpty();
   }

   public void forEach(Consumer var1) {
      for(int var2 = 0; var2 < this.items.size(); ++var2) {
         var1.accept((WornItem)this.items.get(var2));
      }

   }

   public void setFromItemVisuals(ItemVisuals var1) {
      this.clear();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         ItemVisual var3 = (ItemVisual)var1.get(var2);
         String var4 = var3.getItemType();
         InventoryItem var5 = InventoryItemFactory.CreateItem(var4);
         if (var5 != null) {
            if (var5.getVisual() != null) {
               var5.getVisual().copyFrom(var3);
               var5.synchWithVisual();
            }

            if (var5 instanceof Clothing && !StringUtils.isNullOrWhitespace(var5.getBodyLocation())) {
               this.setItem(var5.getBodyLocation(), var5);
            } else if (var5 instanceof InventoryContainer && !StringUtils.isNullOrWhitespace(((InventoryContainer)var5).canBeEquipped())) {
               this.setItem(((InventoryContainer)var5).canBeEquipped(), var5);
            }
         }
      }

   }

   public void getItemVisuals(ItemVisuals var1) {
      var1.clear();

      for(int var2 = 0; var2 < this.items.size(); ++var2) {
         InventoryItem var3 = ((WornItem)this.items.get(var2)).getItem();
         ItemVisual var4 = var3.getVisual();
         if (var4 != null) {
            var4.setInventoryItem(var3);
            var1.add(var4);
         }
      }

   }

   public void addItemsToItemContainer(ItemContainer var1) {
      for(int var2 = 0; var2 < this.items.size(); ++var2) {
         InventoryItem var3 = ((WornItem)this.items.get(var2)).getItem();
         int var4 = var3.getVisual().getHolesNumber();
         var3.setCondition(var3.getConditionMax() - var4 * 3);
         var1.AddItem(var3);
      }

   }

   private int indexOf(String var1) {
      for(int var2 = 0; var2 < this.items.size(); ++var2) {
         WornItem var3 = (WornItem)this.items.get(var2);
         if (var3.location.equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   private int indexOf(InventoryItem var1) {
      for(int var2 = 0; var2 < this.items.size(); ++var2) {
         WornItem var3 = (WornItem)this.items.get(var2);
         if (var3.getItem() == var1) {
            return var2;
         }
      }

      return -1;
   }
}
