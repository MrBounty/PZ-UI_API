package zombie.iso;

import java.util.ArrayList;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.Lua.LuaManager;
import zombie.characters.IsoGameCharacter;
import zombie.debug.DebugLog;
import zombie.network.GameServer;
import zombie.network.ServerOptions;
import zombie.util.Type;

public final class SpawnPoints {
   public static final SpawnPoints instance = new SpawnPoints();
   private KahluaTable SpawnRegions;
   private final ArrayList SpawnPoints = new ArrayList();
   private final ArrayList SpawnBuildings = new ArrayList();
   private final IsoGameCharacter.Location m_tempLocation = new IsoGameCharacter.Location(-1, -1, -1);

   public void init() {
      this.SpawnRegions = LuaManager.platform.newTable();
      this.SpawnPoints.clear();
      this.SpawnBuildings.clear();
   }

   public void initServer1() {
      this.init();
      this.initSpawnRegions();
   }

   public void initServer2() {
      if (!this.parseServerSpawnPoint()) {
         this.parseSpawnRegions();
         this.initSpawnBuildings();
      }
   }

   public void initSinglePlayer() {
      this.init();
      this.initSpawnRegions();
      this.parseSpawnRegions();
      this.initSpawnBuildings();
   }

   private void initSpawnRegions() {
      KahluaTable var1 = (KahluaTable)LuaManager.env.rawget("SpawnRegionMgr");
      if (var1 == null) {
         DebugLog.General.error("SpawnRegionMgr is undefined");
      } else {
         Object[] var2 = LuaManager.caller.pcall(LuaManager.thread, var1.rawget("getSpawnRegions"));
         if (var2.length > 1 && var2[1] instanceof KahluaTable) {
            this.SpawnRegions = (KahluaTable)var2[1];
         }

      }
   }

   private boolean parseServerSpawnPoint() {
      if (!GameServer.bServer) {
         return false;
      } else if (ServerOptions.instance.SpawnPoint.getValue().isEmpty()) {
         return false;
      } else {
         String[] var1 = ServerOptions.instance.SpawnPoint.getValue().split(",");
         if (var1.length == 3) {
            try {
               int var2 = Integer.parseInt(var1[0].trim());
               int var3 = Integer.parseInt(var1[1].trim());
               int var4 = Integer.parseInt(var1[2].trim());
               if (var2 != 0 || var3 != 0) {
                  this.SpawnPoints.add(new IsoGameCharacter.Location(var2, var3, var4));
                  return true;
               }
            } catch (NumberFormatException var5) {
               DebugLog.General.error("SpawnPoint must be x,y,z, got \"" + ServerOptions.instance.SpawnPoint.getValue() + "\"");
            }
         } else {
            DebugLog.General.error("SpawnPoint must be x,y,z, got \"" + ServerOptions.instance.SpawnPoint.getValue() + "\"");
         }

         return false;
      }
   }

   private void parseSpawnRegions() {
      KahluaTableIterator var1 = this.SpawnRegions.iterator();

      while(var1.advance()) {
         KahluaTable var2 = (KahluaTable)Type.tryCastTo(var1.getValue(), KahluaTable.class);
         if (var2 != null) {
            this.parseRegion(var2);
         }
      }

   }

   private void parseRegion(KahluaTable var1) {
      KahluaTable var2 = (KahluaTable)Type.tryCastTo(var1.rawget("points"), KahluaTable.class);
      if (var2 != null) {
         KahluaTableIterator var3 = var2.iterator();

         while(var3.advance()) {
            KahluaTable var4 = (KahluaTable)Type.tryCastTo(var3.getValue(), KahluaTable.class);
            if (var4 != null) {
               this.parseProfession(var4);
            }
         }
      }

   }

   private void parseProfession(KahluaTable var1) {
      KahluaTableIterator var2 = var1.iterator();

      while(var2.advance()) {
         KahluaTable var3 = (KahluaTable)Type.tryCastTo(var2.getValue(), KahluaTable.class);
         if (var3 != null) {
            this.parsePoint(var3);
         }
      }

   }

   private void parsePoint(KahluaTable var1) {
      Double var2 = (Double)Type.tryCastTo(var1.rawget("worldX"), Double.class);
      Double var3 = (Double)Type.tryCastTo(var1.rawget("worldY"), Double.class);
      Double var4 = (Double)Type.tryCastTo(var1.rawget("posX"), Double.class);
      Double var5 = (Double)Type.tryCastTo(var1.rawget("posY"), Double.class);
      Double var6 = (Double)Type.tryCastTo(var1.rawget("posZ"), Double.class);
      if (var2 != null && var3 != null && var4 != null && var5 != null) {
         this.m_tempLocation.x = var2.intValue() * 300 + var4.intValue();
         this.m_tempLocation.y = var3.intValue() * 300 + var5.intValue();
         this.m_tempLocation.z = var6 == null ? 0 : var6.intValue();
         if (!this.SpawnPoints.contains(this.m_tempLocation)) {
            IsoGameCharacter.Location var7 = new IsoGameCharacter.Location(this.m_tempLocation.x, this.m_tempLocation.y, this.m_tempLocation.z);
            this.SpawnPoints.add(var7);
         }

      }
   }

   private void initSpawnBuildings() {
      for(int var1 = 0; var1 < this.SpawnPoints.size(); ++var1) {
         IsoGameCharacter.Location var2 = (IsoGameCharacter.Location)this.SpawnPoints.get(var1);
         RoomDef var3 = IsoWorld.instance.MetaGrid.getRoomAt(var2.x, var2.y, var2.z);
         if (var3 != null && var3.getBuilding() != null) {
            this.SpawnBuildings.add(var3.getBuilding());
         } else {
            DebugLog.General.warn("initSpawnBuildings: no room or building at %d,%d,%d", var2.x, var2.y, var2.z);
         }
      }

   }

   public boolean isSpawnBuilding(BuildingDef var1) {
      return this.SpawnBuildings.contains(var1);
   }

   public KahluaTable getSpawnRegions() {
      return this.SpawnRegions;
   }
}
