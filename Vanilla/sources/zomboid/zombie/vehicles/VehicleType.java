package zombie.vehicles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import se.krka.kahlua.j2se.KahluaTableImpl;
import zombie.Lua.LuaManager;
import zombie.core.Rand;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.VehicleScript;
import zombie.util.list.PZArrayUtil;

public final class VehicleType {
   public final ArrayList vehiclesDefinition = new ArrayList();
   public int chanceToSpawnNormal = 80;
   public int chanceToSpawnBurnt = 0;
   public int spawnRate = 16;
   public int chanceOfOverCar = 0;
   public boolean randomAngle = false;
   public float baseVehicleQuality = 1.0F;
   public String name = "";
   private int chanceToSpawnKey = 70;
   public int chanceToPartDamage = 0;
   public boolean isSpecialCar = false;
   public boolean isBurntCar = false;
   public int chanceToSpawnSpecial = 5;
   public static final HashMap vehicles = new HashMap();
   public static final ArrayList specialVehicles = new ArrayList();

   public VehicleType(String var1) {
      this.name = var1;
   }

   public static void init() {
      initNormal();
      validate(vehicles.values());
      validate(specialVehicles);
   }

   private static void validate(Collection var0) {
   }

   private static void initNormal() {
      boolean var0 = DebugLog.isEnabled(DebugType.Lua);
      KahluaTableImpl var1 = (KahluaTableImpl)LuaManager.env.rawget("VehicleZoneDistribution");
      Iterator var2 = var1.delegate.entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         String var4 = var3.getKey().toString();
         VehicleType var5 = new VehicleType(var4);
         ArrayList var6 = var5.vehiclesDefinition;
         KahluaTableImpl var7 = (KahluaTableImpl)var3.getValue();
         KahluaTableImpl var8 = (KahluaTableImpl)var7.rawget("vehicles");
         Iterator var9 = var8.delegate.entrySet().iterator();

         while(var9.hasNext()) {
            Entry var10 = (Entry)var9.next();
            String var11 = var10.getKey().toString();
            VehicleScript var12 = ScriptManager.instance.getVehicle(var11);
            if (var12 == null) {
               DebugLog.General.warn("vehicle type \"" + var11 + "\" doesn't exist");
            }

            KahluaTableImpl var13 = (KahluaTableImpl)var10.getValue();
            var6.add(new VehicleType.VehicleTypeDefinition(var11, var13.rawgetInt("index"), (float)var13.rawgetInt("spawnChance")));
         }

         float var20 = 0.0F;

         int var21;
         for(var21 = 0; var21 < var6.size(); ++var21) {
            var20 += ((VehicleType.VehicleTypeDefinition)var6.get(var21)).spawnChance;
         }

         var20 = 100.0F / var20;
         if (var0) {
            DebugLog.Lua.println("Vehicle spawn rate:");
         }

         for(var21 = 0; var21 < var6.size(); ++var21) {
            VehicleType.VehicleTypeDefinition var10000 = (VehicleType.VehicleTypeDefinition)var6.get(var21);
            var10000.spawnChance *= var20;
            if (var0) {
               DebugLog.Lua.println(var4 + ": " + ((VehicleType.VehicleTypeDefinition)var6.get(var21)).vehicleType + " " + ((VehicleType.VehicleTypeDefinition)var6.get(var21)).spawnChance + "%");
            }
         }

         if (var7.delegate.containsKey("chanceToPartDamage")) {
            var5.chanceToPartDamage = var7.rawgetInt("chanceToPartDamage");
         }

         if (var7.delegate.containsKey("chanceToSpawnNormal")) {
            var5.chanceToSpawnNormal = var7.rawgetInt("chanceToSpawnNormal");
         }

         if (var7.delegate.containsKey("chanceToSpawnSpecial")) {
            var5.chanceToSpawnSpecial = var7.rawgetInt("chanceToSpawnSpecial");
         }

         if (var7.delegate.containsKey("specialCar")) {
            var5.isSpecialCar = var7.rawgetBool("specialCar");
         }

         if (var7.delegate.containsKey("burntCar")) {
            var5.isBurntCar = var7.rawgetBool("burntCar");
         }

         if (var7.delegate.containsKey("baseVehicleQuality")) {
            var5.baseVehicleQuality = var7.rawgetFloat("baseVehicleQuality");
         }

         if (var7.delegate.containsKey("chanceOfOverCar")) {
            var5.chanceOfOverCar = var7.rawgetInt("chanceOfOverCar");
         }

         if (var7.delegate.containsKey("randomAngle")) {
            var5.randomAngle = var7.rawgetBool("randomAngle");
         }

         if (var7.delegate.containsKey("spawnRate")) {
            var5.spawnRate = var7.rawgetInt("spawnRate");
         }

         if (var7.delegate.containsKey("chanceToSpawnKey")) {
            var5.chanceToSpawnKey = var7.rawgetInt("chanceToSpawnKey");
         }

         if (var7.delegate.containsKey("chanceToSpawnBurnt")) {
            var5.chanceToSpawnBurnt = var7.rawgetInt("chanceToSpawnBurnt");
         }

         vehicles.put(var4, var5);
         if (var5.isSpecialCar) {
            specialVehicles.add(var5);
         }
      }

      HashSet var14 = new HashSet();
      Iterator var15 = vehicles.values().iterator();

      while(var15.hasNext()) {
         VehicleType var16 = (VehicleType)var15.next();
         Iterator var18 = var16.vehiclesDefinition.iterator();

         while(var18.hasNext()) {
            VehicleType.VehicleTypeDefinition var19 = (VehicleType.VehicleTypeDefinition)var18.next();
            var14.add(var19.vehicleType);
         }
      }

      var15 = ScriptManager.instance.getAllVehicleScripts().iterator();

      while(var15.hasNext()) {
         VehicleScript var17 = (VehicleScript)var15.next();
         if (!var14.contains(var17.getFullName())) {
            DebugLog.General.warn("vehicle type \"" + var17.getFullName() + "\" isn't in VehicleZoneDistribution");
         }
      }

   }

   public static boolean hasTypeForZone(String var0) {
      if (vehicles.isEmpty()) {
         init();
      }

      var0 = var0.toLowerCase();
      return vehicles.containsKey(var0);
   }

   public static VehicleType getRandomVehicleType(String var0) {
      return getRandomVehicleType(var0, true);
   }

   public static VehicleType getRandomVehicleType(String var0, Boolean var1) {
      if (vehicles.isEmpty()) {
         init();
      }

      var0 = var0.toLowerCase();
      VehicleType var2 = (VehicleType)vehicles.get(var0);
      if (var2 == null) {
         DebugLog.log(var0 + " Don't exist in VehicleZoneDistribution");
         return null;
      } else if (Rand.Next(100) < var2.chanceToSpawnBurnt) {
         if (Rand.Next(100) < 80) {
            var2 = (VehicleType)vehicles.get("normalburnt");
         } else {
            var2 = (VehicleType)vehicles.get("specialburnt");
         }

         return var2;
      } else {
         if (var1 && var2.isSpecialCar && Rand.Next(100) < var2.chanceToSpawnNormal) {
            var2 = (VehicleType)vehicles.get("parkingstall");
         }

         if (!var2.isBurntCar && !var2.isSpecialCar && Rand.Next(100) < var2.chanceToSpawnSpecial) {
            var2 = (VehicleType)PZArrayUtil.pickRandom((List)specialVehicles);
         }

         if (var2.isBurntCar) {
            if (Rand.Next(100) < 80) {
               var2 = (VehicleType)vehicles.get("normalburnt");
            } else {
               var2 = (VehicleType)vehicles.get("specialburnt");
            }
         }

         return var2;
      }
   }

   public static VehicleType getTypeFromName(String var0) {
      if (vehicles.isEmpty()) {
         init();
      }

      return (VehicleType)vehicles.get(var0);
   }

   public float getBaseVehicleQuality() {
      return this.baseVehicleQuality;
   }

   public float getRandomBaseVehicleQuality() {
      return Rand.Next(this.baseVehicleQuality - 0.1F, this.baseVehicleQuality + 0.1F);
   }

   public int getChanceToSpawnKey() {
      return this.chanceToSpawnKey;
   }

   public void setChanceToSpawnKey(int var1) {
      this.chanceToSpawnKey = var1;
   }

   public static void Reset() {
      vehicles.clear();
      specialVehicles.clear();
   }

   public static class VehicleTypeDefinition {
      public String vehicleType;
      public int index = -1;
      public float spawnChance = 0.0F;

      public VehicleTypeDefinition(String var1, int var2, float var3) {
         this.vehicleType = var1;
         this.index = var2;
         this.spawnChance = var3;
      }
   }
}
