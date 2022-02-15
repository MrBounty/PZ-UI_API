package zombie.characters.AttachedItems;

import java.util.ArrayList;
import java.util.function.Consumer;
import zombie.inventory.InventoryItem;

public final class AttachedItems {
   protected final AttachedLocationGroup group;
   protected final ArrayList items = new ArrayList();

   public AttachedItems(AttachedLocationGroup var1) {
      this.group = var1;
   }

   public AttachedItems(AttachedItems var1) {
      this.group = var1.group;
      this.copyFrom(var1);
   }

   public void copyFrom(AttachedItems var1) {
      if (this.group != var1.group) {
         throw new RuntimeException("group=" + this.group.id + " other.group=" + var1.group.id);
      } else {
         this.items.clear();
         this.items.addAll(var1.items);
      }
   }

   public AttachedLocationGroup getGroup() {
      return this.group;
   }

   public AttachedItem get(int var1) {
      return (AttachedItem)this.items.get(var1);
   }

   public void setItem(String var1, InventoryItem var2) {
      this.group.checkValid(var1);
      int var3 = this.indexOf(var1);
      if (var3 != -1) {
         this.items.remove(var3);
      }

      if (var2 != null) {
         this.remove(var2);
         int var4 = this.items.size();

         for(int var5 = 0; var5 < this.items.size(); ++var5) {
            AttachedItem var6 = (AttachedItem)this.items.get(var5);
            if (this.group.indexOf(var6.getLocation()) > this.group.indexOf(var1)) {
               var4 = var5;
               break;
            }
         }

         AttachedItem var7 = new AttachedItem(var1, var2);
         this.items.add(var4, var7);
      }
   }

   public InventoryItem getItem(String var1) {
      this.group.checkValid(var1);
      int var2 = this.indexOf(var1);
      return var2 == -1 ? null : ((AttachedItem)this.items.get(var2)).item;
   }

   public InventoryItem getItemByIndex(int var1) {
      return var1 >= 0 && var1 < this.items.size() ? ((AttachedItem)this.items.get(var1)).getItem() : null;
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
      return var2 == -1 ? null : ((AttachedItem)this.items.get(var2)).getLocation();
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
         var1.accept((AttachedItem)this.items.get(var2));
      }

   }

   private int indexOf(String var1) {
      for(int var2 = 0; var2 < this.items.size(); ++var2) {
         AttachedItem var3 = (AttachedItem)this.items.get(var2);
         if (var3.location.equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   private int indexOf(InventoryItem var1) {
      for(int var2 = 0; var2 < this.items.size(); ++var2) {
         AttachedItem var3 = (AttachedItem)this.items.get(var2);
         if (var3.getItem() == var1) {
            return var2;
         }
      }

      return -1;
   }
}
