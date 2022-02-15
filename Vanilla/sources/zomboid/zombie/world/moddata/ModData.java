package zombie.world.moddata;

import java.util.ArrayList;
import se.krka.kahlua.vm.KahluaTable;

public final class ModData {
   private static final ArrayList temp_list = new ArrayList();

   public static ArrayList getTableNames() {
      GlobalModData.instance.collectTableNames(temp_list);
      return temp_list;
   }

   public static boolean exists(String var0) {
      return GlobalModData.instance.exists(var0);
   }

   public static KahluaTable getOrCreate(String var0) {
      return GlobalModData.instance.getOrCreate(var0);
   }

   public static KahluaTable get(String var0) {
      return GlobalModData.instance.get(var0);
   }

   public static String create() {
      return GlobalModData.instance.create();
   }

   public static KahluaTable create(String var0) {
      return GlobalModData.instance.create(var0);
   }

   public static KahluaTable remove(String var0) {
      return GlobalModData.instance.remove(var0);
   }

   public static void add(String var0, KahluaTable var1) {
      GlobalModData.instance.add(var0, var1);
   }

   public static void transmit(String var0) {
      GlobalModData.instance.transmit(var0);
   }

   public static void request(String var0) {
      GlobalModData.instance.request(var0);
   }
}
