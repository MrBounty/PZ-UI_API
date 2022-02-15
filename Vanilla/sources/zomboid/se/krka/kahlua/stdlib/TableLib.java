package se.krka.kahlua.stdlib;

import se.krka.kahlua.vm.JavaFunction;
import se.krka.kahlua.vm.KahluaArray;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaThread;
import se.krka.kahlua.vm.KahluaUtil;
import se.krka.kahlua.vm.LuaCallFrame;
import se.krka.kahlua.vm.Platform;

public final class TableLib implements JavaFunction {
   private static final int CONCAT = 0;
   private static final int INSERT = 1;
   private static final int REMOVE = 2;
   private static final int NEWARRAY = 3;
   private static final int PAIRS = 4;
   private static final int ISEMPTY = 5;
   private static final int WIPE = 6;
   private static final int NUM_FUNCTIONS = 7;
   private static final String[] names = new String[7];
   private static final TableLib[] functions;
   private final int index;

   public TableLib(int var1) {
      this.index = var1;
   }

   public static void register(Platform var0, KahluaTable var1) {
      KahluaTable var2 = var0.newTable();

      for(int var3 = 0; var3 < 7; ++var3) {
         var2.rawset(names[var3], functions[var3]);
      }

      var1.rawset("table", var2);
   }

   public String toString() {
      if (this.index < names.length) {
         String var10000 = names[this.index];
         return "table." + var10000;
      } else {
         return super.toString();
      }
   }

   public int call(LuaCallFrame var1, int var2) {
      switch(this.index) {
      case 0:
         return concat(var1, var2);
      case 1:
         return insert(var1, var2);
      case 2:
         return remove(var1, var2);
      case 3:
         return this.newarray(var1, var2);
      case 4:
         return this.pairs(var1, var2);
      case 5:
         return this.isempty(var1, var2);
      case 6:
         return this.wipe(var1, var2);
      default:
         return 0;
      }
   }

   private int wipe(LuaCallFrame var1, int var2) {
      KahluaTable var3 = getTable(var1, var2);
      var3.wipe();
      return 0;
   }

   private int isempty(LuaCallFrame var1, int var2) {
      KahluaTable var3 = getTable(var1, var2);
      return var1.push(KahluaUtil.toBoolean(var3.isEmpty()));
   }

   private int pairs(LuaCallFrame var1, int var2) {
      KahluaUtil.luaAssert(var2 >= 1, "Not enough arguments");
      Object var3 = var1.get(0);
      KahluaUtil.luaAssert(var3 instanceof KahluaTable, "Expected a table");
      KahluaTable var4 = (KahluaTable)var3;
      return var1.push(var4.iterator());
   }

   private int newarray(LuaCallFrame var1, int var2) {
      Object var3 = KahluaUtil.getOptionalArg(var1, 1);
      KahluaArray var4 = new KahluaArray();
      if (var3 instanceof KahluaTable && var2 == 1) {
         KahluaTable var8 = (KahluaTable)var3;
         int var6 = var8.len();

         for(int var7 = var6; var7 >= 1; --var7) {
            var4.rawset(var7, var8.rawget(var7));
         }
      } else {
         for(int var5 = var2; var5 >= 1; --var5) {
            var4.rawset(var5, var1.get(var5 - 1));
         }
      }

      return var1.push(var4);
   }

   private static int concat(LuaCallFrame var0, int var1) {
      KahluaTable var2 = getTable(var0, var1);
      String var3 = "";
      if (var1 >= 2) {
         var3 = KahluaUtil.rawTostring(var0.get(1));
      }

      int var4 = 1;
      if (var1 >= 3) {
         Double var5 = KahluaUtil.rawTonumber(var0.get(2));
         var4 = var5.intValue();
      }

      int var10;
      if (var1 >= 4) {
         Double var6 = KahluaUtil.rawTonumber(var0.get(3));
         var10 = var6.intValue();
      } else {
         var10 = var2.len();
      }

      StringBuilder var11 = new StringBuilder();

      for(int var7 = var4; var7 <= var10; ++var7) {
         if (var7 > var4) {
            var11.append(var3);
         }

         Double var8 = KahluaUtil.toDouble((long)var7);
         Object var9 = var2.rawget(var8);
         var11.append(KahluaUtil.rawTostring(var9));
      }

      return var0.push(var11.toString());
   }

   public static void insert(KahluaThread var0, KahluaTable var1, Object var2) {
      append(var0, var1, var2);
   }

   public static void append(KahluaThread var0, KahluaTable var1, Object var2) {
      int var3 = 1 + var1.len();
      var0.tableSet(var1, KahluaUtil.toDouble((long)var3), var2);
   }

   public static void rawappend(KahluaTable var0, Object var1) {
      int var2 = 1 + var0.len();
      var0.rawset(KahluaUtil.toDouble((long)var2), var1);
   }

   public static void insert(KahluaThread var0, KahluaTable var1, int var2, Object var3) {
      int var4 = var1.len();

      for(int var5 = var4; var5 >= var2; --var5) {
         var0.tableSet(var1, KahluaUtil.toDouble((long)(var5 + 1)), var0.tableget(var1, KahluaUtil.toDouble((long)var5)));
      }

      var0.tableSet(var1, KahluaUtil.toDouble((long)var2), var3);
   }

   public static void rawinsert(KahluaTable var0, int var1, Object var2) {
      int var3 = var0.len();
      if (var1 <= var3) {
         Double var4 = KahluaUtil.toDouble((long)(var3 + 1));

         for(int var5 = var3; var5 >= var1; --var5) {
            Double var6 = KahluaUtil.toDouble((long)var5);
            var0.rawset(var4, var0.rawget(var6));
            var4 = var6;
         }

         var0.rawset(var4, var2);
      } else {
         var0.rawset(KahluaUtil.toDouble((long)var1), var2);
      }

   }

   private static int insert(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 2, "Not enough arguments");
      KahluaTable var2 = (KahluaTable)var0.get(0);
      int var3 = var2.len() + 1;
      Object var4;
      if (var1 > 2) {
         var3 = KahluaUtil.rawTonumber(var0.get(1)).intValue();
         var4 = var0.get(2);
      } else {
         var4 = var0.get(1);
      }

      insert(var0.getThread(), var2, var3, var4);
      return 0;
   }

   public static Object remove(KahluaThread var0, KahluaTable var1) {
      return remove(var0, var1, var1.len());
   }

   public static Object remove(KahluaThread var0, KahluaTable var1, int var2) {
      Object var3 = var0.tableget(var1, KahluaUtil.toDouble((long)var2));
      int var4 = var1.len();

      for(int var5 = var2; var5 < var4; ++var5) {
         var0.tableSet(var1, KahluaUtil.toDouble((long)var5), var0.tableget(var1, KahluaUtil.toDouble((long)(var5 + 1))));
      }

      var0.tableSet(var1, KahluaUtil.toDouble((long)var4), (Object)null);
      return var3;
   }

   private static int remove(LuaCallFrame var0, int var1) {
      KahluaTable var2 = getTable(var0, var1);
      int var3 = var2.len();
      if (var1 > 1) {
         var3 = KahluaUtil.rawTonumber(var0.get(1)).intValue();
      }

      var0.push(remove(var0.getThread(), var2, var3));
      return 1;
   }

   private static KahluaTable getTable(LuaCallFrame var0, int var1) {
      KahluaUtil.luaAssert(var1 >= 1, "expected table, got no arguments");
      KahluaTable var2 = (KahluaTable)var0.get(0);
      return var2;
   }

   static {
      names[0] = "concat";
      names[1] = "insert";
      names[2] = "remove";
      names[3] = "newarray";
      names[4] = "pairs";
      names[5] = "isempty";
      names[6] = "wipe";
      functions = new TableLib[7];

      for(int var0 = 0; var0 < 7; ++var0) {
         functions[var0] = new TableLib(var0);
      }

   }
}
