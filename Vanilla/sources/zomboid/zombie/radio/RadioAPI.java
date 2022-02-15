package zombie.radio;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;

public final class RadioAPI {
   private static RadioAPI instance;

   public static int timeToTimeStamp(int var0, int var1, int var2) {
      return var0 * 24 + var1 * 60 + var2;
   }

   public static int timeStampToDays(int var0) {
      return var0 / 1440;
   }

   public static int timeStampToHours(int var0) {
      return var0 / 60 % 24;
   }

   public static int timeStampToMinutes(int var0) {
      return var0 % 60;
   }

   public static boolean hasInstance() {
      return instance != null;
   }

   public static RadioAPI getInstance() {
      if (instance == null) {
         instance = new RadioAPI();
      }

      return instance;
   }

   private RadioAPI() {
   }

   public KahluaTable getChannels(String var1) {
      Map var2 = ZomboidRadio.getInstance().GetChannelList(var1);
      KahluaTable var3 = LuaManager.platform.newTable();
      if (var2 != null) {
         Iterator var4 = var2.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            var3.rawset(var5.getKey(), var5.getValue());
         }
      }

      return var3;
   }
}
