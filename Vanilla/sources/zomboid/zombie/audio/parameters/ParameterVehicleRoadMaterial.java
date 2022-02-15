package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.vehicles.BaseVehicle;

public final class ParameterVehicleRoadMaterial extends FMODLocalParameter {
   private final BaseVehicle vehicle;

   public ParameterVehicleRoadMaterial(BaseVehicle var1) {
      super("VehicleRoadMaterial");
      this.vehicle = var1;
   }

   public float calculateCurrentValue() {
      if (!this.vehicle.isEngineRunning()) {
         return Float.isNaN(this.getCurrentValue()) ? 0.0F : this.getCurrentValue();
      } else {
         return (float)this.getMaterial().label;
      }
   }

   private ParameterVehicleRoadMaterial.Material getMaterial() {
      IsoGridSquare var1 = this.vehicle.getCurrentSquare();
      if (var1 == null) {
         return ParameterVehicleRoadMaterial.Material.Concrete;
      } else {
         IsoObject var2 = this.vehicle.getCurrentSquare().getFloor();
         if (var2 != null && var2.getSprite() != null && var2.getSprite().getName() != null) {
            String var3 = var2.getSprite().getName();
            if (!var3.endsWith("blends_natural_01_5") && !var3.endsWith("blends_natural_01_6") && !var3.endsWith("blends_natural_01_7") && !var3.endsWith("blends_natural_01_0")) {
               if (!var3.endsWith("blends_natural_01_64") && !var3.endsWith("blends_natural_01_69") && !var3.endsWith("blends_natural_01_70") && !var3.endsWith("blends_natural_01_71")) {
                  if (var3.startsWith("blends_natural_01")) {
                     return ParameterVehicleRoadMaterial.Material.Grass;
                  } else if (!var3.endsWith("blends_street_01_48") && !var3.endsWith("blends_street_01_53") && !var3.endsWith("blends_street_01_54") && !var3.endsWith("blends_street_01_55")) {
                     if (var3.startsWith("floors_interior_tilesandwood_01_")) {
                        int var5 = Integer.parseInt(var3.replaceFirst("floors_interior_tilesandwood_01_", ""));
                        return var5 > 40 && var5 < 48 ? ParameterVehicleRoadMaterial.Material.Wood : ParameterVehicleRoadMaterial.Material.Concrete;
                     } else if (var3.startsWith("carpentry_02_")) {
                        return ParameterVehicleRoadMaterial.Material.Wood;
                     } else if (var3.contains("interior_carpet_")) {
                        return ParameterVehicleRoadMaterial.Material.Carpet;
                     } else {
                        float var4 = var1.getPuddlesInGround();
                        return (double)var4 > 0.1D ? ParameterVehicleRoadMaterial.Material.Puddle : ParameterVehicleRoadMaterial.Material.Concrete;
                     }
                  } else {
                     return ParameterVehicleRoadMaterial.Material.Gravel;
                  }
               } else {
                  return ParameterVehicleRoadMaterial.Material.Dirt;
               }
            } else {
               return ParameterVehicleRoadMaterial.Material.Sand;
            }
         } else {
            return ParameterVehicleRoadMaterial.Material.Concrete;
         }
      }
   }

   static enum Material {
      Concrete(0),
      Grass(1),
      Gravel(2),
      Puddle(3),
      Snow(4),
      Wood(5),
      Carpet(6),
      Dirt(7),
      Sand(8);

      final int label;

      private Material(int var3) {
         this.label = var3;
      }

      // $FF: synthetic method
      private static ParameterVehicleRoadMaterial.Material[] $values() {
         return new ParameterVehicleRoadMaterial.Material[]{Concrete, Grass, Gravel, Puddle, Snow, Wood, Carpet, Dirt, Sand};
      }
   }
}
