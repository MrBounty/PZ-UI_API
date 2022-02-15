package zombie.vehicles;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;
import java.util.LinkedList;
import java.util.List;

public final class VehicleCache {
   public short id;
   float x;
   float y;
   float z;
   private static TShortObjectHashMap mapId = new TShortObjectHashMap();
   private static TIntObjectHashMap mapXY = new TIntObjectHashMap();

   public static void vehicleUpdate(short var0, float var1, float var2, float var3) {
      VehicleCache var4 = (VehicleCache)mapId.get(var0);
      int var6;
      int var7;
      if (var4 != null) {
         int var5 = (int)(var4.x / 10.0F);
         var6 = (int)(var4.y / 10.0F);
         var7 = (int)(var1 / 10.0F);
         int var8 = (int)(var2 / 10.0F);
         if (var5 != var7 || var6 != var8) {
            ((List)mapXY.get(var5 * 65536 + var6)).remove(var4);
            if (mapXY.get(var7 * 65536 + var8) == null) {
               mapXY.put(var7 * 65536 + var8, new LinkedList());
            }

            ((List)mapXY.get(var7 * 65536 + var8)).add(var4);
         }

         var4.x = var1;
         var4.y = var2;
         var4.z = var3;
      } else {
         VehicleCache var9 = new VehicleCache();
         var9.id = var0;
         var9.x = var1;
         var9.y = var2;
         var9.z = var3;
         mapId.put(var0, var9);
         var6 = (int)(var1 / 10.0F);
         var7 = (int)(var2 / 10.0F);
         if (mapXY.get(var6 * 65536 + var7) == null) {
            mapXY.put(var6 * 65536 + var7, new LinkedList());
         }

         ((List)mapXY.get(var6 * 65536 + var7)).add(var9);
      }

   }

   public static List vehicleGet(float var0, float var1) {
      int var2 = (int)(var0 / 10.0F);
      int var3 = (int)(var1 / 10.0F);
      return (List)mapXY.get(var2 * 65536 + var3);
   }

   public static List vehicleGet(int var0, int var1) {
      return (List)mapXY.get(var0 * 65536 + var1);
   }

   public static void remove(short var0) {
      VehicleCache var1 = (VehicleCache)mapId.get(var0);
      if (var1 != null) {
         mapId.remove(var0);
         int var2 = (int)(var1.x / 10.0F);
         int var3 = (int)(var1.y / 10.0F);
         int var4 = var2 * 65536 + var3;

         assert mapXY.containsKey(var4);

         assert ((List)mapXY.get(var4)).contains(var1);

         ((List)mapXY.get(var4)).remove(var1);
      }
   }

   public static void Reset() {
      mapId.clear();
      mapXY.clear();
   }

   static {
      mapId.setAutoCompactionFactor(0.0F);
      mapXY.setAutoCompactionFactor(0.0F);
   }
}
