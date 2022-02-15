package zombie.audio.parameters;

import zombie.audio.FMODLocalParameter;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoObject;

public final class ParameterCurrentZone extends FMODLocalParameter {
   private final IsoObject object;
   private IsoMetaGrid.Zone metaZone;
   private ParameterCurrentZone.Zone zone;

   public ParameterCurrentZone(IsoObject var1) {
      super("CurrentZone");
      this.zone = ParameterCurrentZone.Zone.None;
      this.object = var1;
   }

   public float calculateCurrentValue() {
      IsoGridSquare var1 = this.object.getSquare();
      if (var1 == null) {
         this.zone = ParameterCurrentZone.Zone.None;
         return (float)this.zone.label;
      } else if (var1.zone == this.metaZone) {
         return (float)this.zone.label;
      } else {
         this.metaZone = var1.zone;
         if (this.metaZone == null) {
            this.zone = ParameterCurrentZone.Zone.None;
            return (float)this.zone.label;
         } else {
            String var2 = this.metaZone.type;
            byte var3 = -1;
            switch(var2.hashCode()) {
            case -687878786:
               if (var2.equals("TownZone")) {
                  var3 = 4;
               }
               break;
            case -650999246:
               if (var2.equals("Vegitation")) {
                  var3 = 6;
               }
               break;
            case 78083:
               if (var2.equals("Nav")) {
                  var3 = 3;
               }
               break;
            case 2182230:
               if (var2.equals("Farm")) {
                  var3 = 1;
               }
               break;
            case 14106697:
               if (var2.equals("DeepForest")) {
                  var3 = 0;
               }
               break;
            case 1894728605:
               if (var2.equals("TrailerPark")) {
                  var3 = 5;
               }
               break;
            case 2110048317:
               if (var2.equals("Forest")) {
                  var3 = 2;
               }
            }

            ParameterCurrentZone.Zone var10001;
            switch(var3) {
            case 0:
               var10001 = ParameterCurrentZone.Zone.DeepForest;
               break;
            case 1:
               var10001 = ParameterCurrentZone.Zone.Farm;
               break;
            case 2:
               var10001 = ParameterCurrentZone.Zone.Forest;
               break;
            case 3:
               var10001 = ParameterCurrentZone.Zone.Nav;
               break;
            case 4:
               var10001 = ParameterCurrentZone.Zone.Town;
               break;
            case 5:
               var10001 = ParameterCurrentZone.Zone.TrailerPark;
               break;
            case 6:
               var10001 = ParameterCurrentZone.Zone.Vegetation;
               break;
            default:
               var10001 = ParameterCurrentZone.Zone.None;
            }

            this.zone = var10001;
            return (float)this.zone.label;
         }
      }
   }

   static enum Zone {
      None(0),
      DeepForest(1),
      Farm(2),
      Forest(3),
      Nav(4),
      Town(5),
      TrailerPark(6),
      Vegetation(7);

      final int label;

      private Zone(int var3) {
         this.label = var3;
      }

      // $FF: synthetic method
      private static ParameterCurrentZone.Zone[] $values() {
         return new ParameterCurrentZone.Zone[]{None, DeepForest, Farm, Forest, Nav, Town, TrailerPark, Vegetation};
      }
   }
}
