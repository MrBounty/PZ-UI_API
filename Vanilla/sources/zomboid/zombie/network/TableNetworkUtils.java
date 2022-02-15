package zombie.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.GameWindow;
import zombie.Lua.LuaManager;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoDirections;

public final class TableNetworkUtils {
   private static final byte SBYT_NO_SAVE = -1;
   private static final byte SBYT_STRING = 0;
   private static final byte SBYT_DOUBLE = 1;
   private static final byte SBYT_TABLE = 2;
   private static final byte SBYT_BOOLEAN = 3;
   private static final byte SBYT_ITEM = 4;
   private static final byte SBYT_DIRECTION = 5;

   public static void save(KahluaTable var0, ByteBuffer var1) throws IOException {
      KahluaTableIterator var2 = var0.iterator();
      int var3 = 0;

      while(var2.advance()) {
         if (canSave(var2.getKey(), var2.getValue())) {
            ++var3;
         }
      }

      var2 = var0.iterator();
      var1.putInt(var3);

      while(var2.advance()) {
         byte var4 = getKeyByte(var2.getKey());
         byte var5 = getValueByte(var2.getValue());
         if (var4 != -1 && var5 != -1) {
            save(var1, var4, var2.getKey());
            save(var1, var5, var2.getValue());
         }
      }

   }

   public static void saveSome(KahluaTable var0, ByteBuffer var1, HashSet var2) throws IOException {
      KahluaTableIterator var3 = var0.iterator();
      int var4 = 0;

      while(var3.advance()) {
         if (var2.contains(var3.getKey()) && canSave(var3.getKey(), var3.getValue())) {
            ++var4;
         }
      }

      var3 = var0.iterator();
      var1.putInt(var4);

      while(var3.advance()) {
         if (var2.contains(var3.getKey())) {
            byte var5 = getKeyByte(var3.getKey());
            byte var6 = getValueByte(var3.getValue());
            if (var5 != -1 && var6 != -1) {
               save(var1, var5, var3.getKey());
               save(var1, var6, var3.getValue());
            }
         }
      }

   }

   private static void save(ByteBuffer var0, byte var1, Object var2) throws IOException, RuntimeException {
      var0.put(var1);
      if (var1 == 0) {
         GameWindow.WriteString(var0, (String)var2);
      } else if (var1 == 1) {
         var0.putDouble((Double)var2);
      } else if (var1 == 3) {
         var0.put((byte)((Boolean)var2 ? 1 : 0));
      } else if (var1 == 2) {
         save((KahluaTable)var2, var0);
      } else if (var1 == 4) {
         ((InventoryItem)var2).saveWithSize(var0, false);
      } else {
         if (var1 != 5) {
            throw new RuntimeException("invalid lua table type " + var1);
         }

         var0.put((byte)((IsoDirections)var2).index());
      }

   }

   public static void load(KahluaTable var0, ByteBuffer var1) throws IOException {
      int var2 = var1.getInt();
      var0.wipe();

      for(int var3 = 0; var3 < var2; ++var3) {
         byte var4 = var1.get();
         Object var5 = load(var1, var4);
         byte var6 = var1.get();
         Object var7 = load(var1, var6);
         var0.rawset(var5, var7);
      }

   }

   public static Object load(ByteBuffer var0, byte var1) throws IOException, RuntimeException {
      if (var1 == 0) {
         return GameWindow.ReadString(var0);
      } else if (var1 == 1) {
         return var0.getDouble();
      } else if (var1 == 3) {
         return var0.get() == 1;
      } else if (var1 == 2) {
         KahluaTableImpl var5 = (KahluaTableImpl)LuaManager.platform.newTable();
         load(var5, var0);
         return var5;
      } else if (var1 == 4) {
         InventoryItem var2 = null;

         try {
            var2 = InventoryItem.loadItem(var0, 186);
         } catch (Exception var4) {
            var4.printStackTrace();
         }

         return var2;
      } else if (var1 == 5) {
         return IsoDirections.fromIndex(var0.get());
      } else {
         throw new RuntimeException("invalid lua table type " + var1);
      }
   }

   private static byte getKeyByte(Object var0) {
      if (var0 instanceof String) {
         return 0;
      } else {
         return (byte)(var0 instanceof Double ? 1 : -1);
      }
   }

   private static byte getValueByte(Object var0) {
      if (var0 instanceof String) {
         return 0;
      } else if (var0 instanceof Double) {
         return 1;
      } else if (var0 instanceof Boolean) {
         return 3;
      } else if (var0 instanceof KahluaTableImpl) {
         return 2;
      } else if (var0 instanceof InventoryItem) {
         return 4;
      } else {
         return (byte)(var0 instanceof IsoDirections ? 5 : -1);
      }
   }

   public static boolean canSave(Object var0, Object var1) {
      return getKeyByte(var0) != -1 && getValueByte(var1) != -1;
   }
}
