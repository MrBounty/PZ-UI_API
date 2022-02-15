package zombie.core.stash;

import java.util.ArrayList;
import se.krka.kahlua.j2se.KahluaTableImpl;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.core.Translator;
import zombie.debug.DebugLog;
import zombie.scripting.ScriptManager;

public final class Stash {
   public String name;
   public String type;
   public String item;
   public String customName;
   public int buildingX;
   public int buildingY;
   public String spawnTable;
   public ArrayList annotations;
   public boolean spawnOnlyOnZed;
   public int minDayToSpawn = -1;
   public int maxDayToSpawn = -1;
   public int minTrapToSpawn = -1;
   public int maxTrapToSpawn = -1;
   public int zombies;
   public ArrayList containers;
   public int barricades;

   public Stash(String var1) {
      this.name = var1;
   }

   public void load(KahluaTableImpl var1) {
      this.type = var1.rawgetStr("type");
      this.item = var1.rawgetStr("item");
      StashBuilding var2 = new StashBuilding(this.name, var1.rawgetInt("buildingX"), var1.rawgetInt("buildingY"));
      StashSystem.possibleStashes.add(var2);
      this.buildingX = var2.buildingX;
      this.buildingY = var2.buildingY;
      this.spawnTable = var1.rawgetStr("spawnTable");
      this.customName = Translator.getText(var1.rawgetStr("customName"));
      this.zombies = var1.rawgetInt("zombies");
      this.barricades = var1.rawgetInt("barricades");
      this.spawnOnlyOnZed = var1.rawgetBool("spawnOnlyOnZed");
      String var3 = var1.rawgetStr("daysToSpawn");
      if (var3 != null) {
         String[] var4 = var3.split("-");
         if (var4.length == 2) {
            this.minDayToSpawn = Integer.parseInt(var4[0]);
            this.maxDayToSpawn = Integer.parseInt(var4[1]);
         } else {
            this.minDayToSpawn = Integer.parseInt(var4[0]);
         }
      }

      String var10 = var1.rawgetStr("traps");
      if (var10 != null) {
         String[] var5 = var10.split("-");
         if (var5.length == 2) {
            this.minTrapToSpawn = Integer.parseInt(var5[0]);
            this.maxTrapToSpawn = Integer.parseInt(var5[1]);
         } else {
            this.minTrapToSpawn = Integer.parseInt(var5[0]);
            this.maxTrapToSpawn = this.minTrapToSpawn;
         }
      }

      KahluaTable var11 = (KahluaTable)var1.rawget("containers");
      if (var11 != null) {
         this.containers = new ArrayList();

         StashContainer var8;
         for(KahluaTableIterator var6 = var11.iterator(); var6.advance(); this.containers.add(var8)) {
            KahluaTableImpl var7 = (KahluaTableImpl)var6.getValue();
            var8 = new StashContainer(var7.rawgetStr("room"), var7.rawgetStr("containerSprite"), var7.rawgetStr("containerType"));
            var8.contX = var7.rawgetInt("contX");
            var8.contY = var7.rawgetInt("contY");
            var8.contZ = var7.rawgetInt("contZ");
            var8.containerItem = var7.rawgetStr("containerItem");
            if (var8.containerItem != null && ScriptManager.instance.getItem(var8.containerItem) == null) {
               DebugLog.General.error("Stash containerItem \"%s\" doesn't exist.", var8.containerItem);
            }
         }
      }

      if ("Map".equals(this.type)) {
         KahluaTableImpl var12 = (KahluaTableImpl)var1.rawget("annotations");
         if (var12 != null) {
            this.annotations = new ArrayList();
            KahluaTableIterator var13 = var12.iterator();

            while(var13.advance()) {
               KahluaTable var14 = (KahluaTable)var13.getValue();
               StashAnnotation var9 = new StashAnnotation();
               var9.fromLua(var14);
               this.annotations.add(var9);
            }
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public String getItem() {
      return this.item;
   }

   public int getBuildingX() {
      return this.buildingX;
   }

   public int getBuildingY() {
      return this.buildingY;
   }
}
