package zombie.worldMap;

import java.util.ArrayList;
import java.util.List;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.Lua.LuaManager;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;

public final class MapDefinitions {
   private static MapDefinitions instance;
   private final ArrayList m_definitions = new ArrayList();

   public static MapDefinitions getInstance() {
      if (instance == null) {
         instance = new MapDefinitions();
      }

      return instance;
   }

   public String pickRandom() {
      if (this.m_definitions.isEmpty()) {
         this.initDefinitionsFromLua();
      }

      return this.m_definitions.isEmpty() ? "Default" : (String)PZArrayUtil.pickRandom((List)this.m_definitions);
   }

   private void initDefinitionsFromLua() {
      KahluaTable var1 = (KahluaTable)Type.tryCastTo(LuaManager.env.rawget("LootMaps"), KahluaTable.class);
      if (var1 != null) {
         KahluaTable var2 = (KahluaTable)Type.tryCastTo(var1.rawget("Init"), KahluaTable.class);
         if (var2 != null) {
            KahluaTableIterator var3 = var2.iterator();

            while(var3.advance()) {
               String var4 = (String)Type.tryCastTo(var3.getKey(), String.class);
               if (var4 != null) {
                  this.m_definitions.add(var4);
               }
            }

         }
      }
   }

   public static void Reset() {
      if (instance != null) {
         instance.m_definitions.clear();
         instance = null;
      }
   }
}
