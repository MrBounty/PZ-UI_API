package zombie.inventory;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import zombie.characters.IsoGameCharacter;
import zombie.inventory.types.InventoryContainer;

public final class CompressIdenticalItems {
   private static final int BLOCK_SIZE = 1024;
   private static final ThreadLocal perThreadVars = new ThreadLocal() {
      protected CompressIdenticalItems.PerThreadData initialValue() {
         return new CompressIdenticalItems.PerThreadData();
      }
   };

   private static int bufferSize(int var0) {
      return (var0 + 1024 - 1) / 1024 * 1024;
   }

   private static ByteBuffer ensureCapacity(ByteBuffer var0, int var1) {
      if (var0 == null || var0.capacity() < var1) {
         var0 = ByteBuffer.allocate(bufferSize(var1));
      }

      return var0;
   }

   private static ByteBuffer ensureCapacity(ByteBuffer var0) {
      if (var0 == null) {
         return ByteBuffer.allocate(1024);
      } else {
         ByteBuffer var1;
         if (var0.capacity() - var0.position() < 1024) {
            var1 = ensureCapacity((ByteBuffer)null, var0.position() + 1024);
            return var1.put(var0.array(), 0, var0.position());
         } else {
            var1 = ensureCapacity((ByteBuffer)null, var0.capacity() + 1024);
            return var1.put(var0.array(), 0, var0.position());
         }
      }
   }

   private static boolean setCompareItem(CompressIdenticalItems.PerThreadData var0, InventoryItem var1) throws IOException {
      ByteBuffer var2 = var0.itemCompareBuffer;
      var2.clear();
      int var3 = var1.id;
      var1.id = 0;

      try {
         while(true) {
            try {
               var2.putInt(0);
               var1.save(var2, false);
               int var4 = var2.position();
               var2.position(0);
               var2.putInt(var4);
               var2.position(var4);
               return true;
            } catch (BufferOverflowException var8) {
               var2 = ensureCapacity(var2);
               var2.clear();
               var0.itemCompareBuffer = var2;
            }
         }
      } finally {
         var1.id = var3;
      }
   }

   private static boolean areItemsIdentical(CompressIdenticalItems.PerThreadData var0, InventoryItem var1, InventoryItem var2) throws IOException {
      if (var1 instanceof InventoryContainer) {
         ItemContainer var3 = ((InventoryContainer)var1).getInventory();
         ItemContainer var4 = ((InventoryContainer)var2).getInventory();
         if (!var3.getItems().isEmpty() || !var4.getItems().isEmpty()) {
            return false;
         }
      }

      ByteBuffer var18 = var1.getByteData();
      ByteBuffer var19 = var2.getByteData();
      if (var18 != null) {
         assert var18.position() == 0;

         if (!var18.equals(var19)) {
            return false;
         }
      } else if (var19 != null) {
         return false;
      }

      ByteBuffer var5 = null;
      int var6 = var2.id;
      var2.id = 0;

      while(true) {
         boolean var11;
         try {
            var5 = var0.itemCompareBuffer;
            var5.position(0);
            int var7 = var5.getInt();
            int var8 = var5.position();
            var5.position(var7);
            int var9 = var5.position();
            var2.save(var5, false);
            int var10 = var5.position();
            if (var10 - var9 == var7 - var8) {
               for(int var20 = 0; var20 < var7 - var8; ++var20) {
                  if (var5.get(var8 + var20) != var5.get(var9 + var20)) {
                     boolean var12 = false;
                     return var12;
                  }
               }

               return true;
            }

            var11 = false;
         } catch (BufferOverflowException var16) {
            var5 = ensureCapacity(var5);
            var5.clear();
            var0.itemCompareBuffer = var5;
            setCompareItem(var0, var1);
            continue;
         } finally {
            var2.id = var6;
         }

         return var11;
      }
   }

   public static ArrayList save(ByteBuffer var0, ArrayList var1, IsoGameCharacter var2) throws IOException {
      CompressIdenticalItems.PerThreadData var3 = (CompressIdenticalItems.PerThreadData)perThreadVars.get();
      CompressIdenticalItems.PerCallData var4 = var3.allocSaveVars();
      HashMap var5 = var4.typeToItems;
      ArrayList var6 = var4.types;

      try {
         int var7;
         for(var7 = 0; var7 < var1.size(); ++var7) {
            String var8 = ((InventoryItem)var1.get(var7)).getFullType();
            if (!var5.containsKey(var8)) {
               var5.put(var8, var4.allocItemList());
               var6.add(var8);
            }

            ((ArrayList)var5.get(var8)).add((InventoryItem)var1.get(var7));
         }

         var7 = var0.position();
         var0.putShort((short)0);
         int var19 = 0;

         int var9;
         for(var9 = 0; var9 < var6.size(); ++var9) {
            ArrayList var10 = (ArrayList)var5.get(var6.get(var9));

            for(int var11 = 0; var11 < var10.size(); ++var11) {
               InventoryItem var12 = (InventoryItem)var10.get(var11);
               var4.savedItems.add(var12);
               int var13 = 1;
               int var14 = var11 + 1;
               if (var2 == null || !var2.isEquipped(var12)) {
                  setCompareItem(var3, var12);

                  while(var11 + 1 < var10.size() && areItemsIdentical(var3, var12, (InventoryItem)var10.get(var11 + 1))) {
                     var4.savedItems.add((InventoryItem)var10.get(var11 + 1));
                     ++var11;
                     ++var13;
                  }
               }

               var0.putInt(var13);
               var12.saveWithSize(var0, false);
               if (var13 > 1) {
                  for(int var15 = var14; var15 <= var11; ++var15) {
                     var0.putInt(((InventoryItem)var10.get(var15)).id);
                  }
               }

               ++var19;
            }
         }

         var9 = var0.position();
         var0.position(var7);
         var0.putShort((short)var19);
         var0.position(var9);
      } finally {
         var4.next = var3.saveVars;
         var3.saveVars = var4;
      }

      return var4.savedItems;
   }

   public static ArrayList load(ByteBuffer var0, int var1, ArrayList var2, ArrayList var3) throws IOException {
      CompressIdenticalItems.PerThreadData var4 = (CompressIdenticalItems.PerThreadData)perThreadVars.get();
      CompressIdenticalItems.PerCallData var5 = var4.allocSaveVars();
      if (var2 != null) {
         var2.clear();
      }

      if (var3 != null) {
         var3.clear();
      }

      try {
         short var6 = var0.getShort();

         for(int var7 = 0; var7 < var6; ++var7) {
            int var8 = 1;
            if (var1 >= 149) {
               var8 = var0.getInt();
            } else if (var1 >= 128) {
               var8 = var0.getShort();
            }

            int var9 = var0.position();
            InventoryItem var10 = InventoryItem.loadItem(var0, var1);
            int var11;
            int var12;
            if (var10 == null) {
               var11 = var8 > 1 ? (var8 - 1) * 4 : 0;
               var0.position(var0.position() + var11);

               for(var12 = 0; var12 < var8; ++var12) {
                  if (var3 != null) {
                     var3.add((Object)null);
                  }

                  var5.savedItems.add((Object)null);
               }
            } else {
               for(var11 = 0; var11 < var8; ++var11) {
                  if (var11 > 0) {
                     var0.position(var9);
                     var10 = InventoryItem.loadItem(var0, var1);
                  }

                  if (var2 != null) {
                     var2.add(var10);
                  }

                  if (var3 != null) {
                     var3.add(var10);
                  }

                  var5.savedItems.add(var10);
               }

               if (var1 >= 128) {
                  for(var11 = 1; var11 < var8; ++var11) {
                     var12 = var0.getInt();
                     var10 = (InventoryItem)var5.savedItems.get(var5.savedItems.size() - var8 + var11);
                     if (var10 != null) {
                        var10.id = var12;
                     }
                  }
               }
            }
         }
      } finally {
         var5.next = var4.saveVars;
         var4.saveVars = var5;
      }

      return var5.savedItems;
   }

   public static void save(ByteBuffer var0, InventoryItem var1) throws IOException {
      var0.putShort((short)1);
      var0.putInt(1);
      var1.saveWithSize(var0, false);
   }

   private static class PerThreadData {
      CompressIdenticalItems.PerCallData saveVars;
      ByteBuffer itemCompareBuffer = ByteBuffer.allocate(1024);

      CompressIdenticalItems.PerCallData allocSaveVars() {
         if (this.saveVars == null) {
            return new CompressIdenticalItems.PerCallData();
         } else {
            CompressIdenticalItems.PerCallData var1 = this.saveVars;
            var1.reset();
            this.saveVars = this.saveVars.next;
            return var1;
         }
      }
   }

   private static class PerCallData {
      final ArrayList types = new ArrayList();
      final HashMap typeToItems = new HashMap();
      final ArrayDeque itemLists = new ArrayDeque();
      final ArrayList savedItems = new ArrayList();
      CompressIdenticalItems.PerCallData next;

      void reset() {
         for(int var1 = 0; var1 < this.types.size(); ++var1) {
            ArrayList var2 = (ArrayList)this.typeToItems.get(this.types.get(var1));
            var2.clear();
            this.itemLists.push(var2);
         }

         this.types.clear();
         this.typeToItems.clear();
         this.savedItems.clear();
      }

      ArrayList allocItemList() {
         return this.itemLists.isEmpty() ? new ArrayList() : (ArrayList)this.itemLists.pop();
      }
   }
}
