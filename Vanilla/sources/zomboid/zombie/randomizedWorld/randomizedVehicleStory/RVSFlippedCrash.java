package zombie.randomizedWorld.randomizedVehicleStory;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.Vector2;
import zombie.iso.objects.IsoDeadBody;
import zombie.vehicles.BaseVehicle;

public final class RVSFlippedCrash extends RandomizedVehicleStoryBase {
   public RVSFlippedCrash() {
      this.name = "Flipped Crash";
      this.minZoneWidth = 8;
      this.minZoneHeight = 8;
      this.setChance(4);
   }

   public void randomizeVehicleStory(IsoMetaGrid.Zone var1, IsoChunk var2) {
      this.callVehicleStorySpawner(var1, var2, 0.0F);
   }

   public boolean initVehicleStorySpawner(IsoMetaGrid.Zone var1, IsoChunk var2, boolean var3) {
      VehicleStorySpawner var4 = VehicleStorySpawner.getInstance();
      var4.clear();
      float var5 = 0.5235988F;
      if (var3) {
         var5 = 0.0F;
      }

      Vector2 var6 = IsoDirections.N.ToVector();
      var6.rotate(Rand.Next(-var5, var5));
      var4.addElement("vehicle1", 0.0F, 0.0F, var6.getDirection(), 2.0F, 5.0F);
      var4.setParameter("zone", var1);
      var4.setParameter("burnt", Rand.NextBool(5));
      return true;
   }

   public void spawnElement(VehicleStorySpawner var1, VehicleStorySpawner.Element var2) {
      IsoGridSquare var3 = var2.square;
      if (var3 != null) {
         float var4 = var2.z;
         IsoMetaGrid.Zone var5 = (IsoMetaGrid.Zone)var1.getParameter("zone", IsoMetaGrid.Zone.class);
         boolean var6 = var1.getParameterBoolean("burnt");
         String var7 = var2.id;
         byte var8 = -1;
         switch(var7.hashCode()) {
         case 2014205573:
            if (var7.equals("vehicle1")) {
               var8 = 0;
            }
         }

         switch(var8) {
         case 0:
            BaseVehicle var9 = this.addVehicleFlipped(var5, var2.position.x, var2.position.y, var4 + 0.25F, var2.direction, var6 ? "normalburnt" : "bad", (String)null, (Integer)null, (String)null);
            if (var9 != null) {
               int var10 = Rand.Next(4);
               String var11 = null;
               switch(var10) {
               case 0:
                  var11 = "Front";
                  break;
               case 1:
                  var11 = "Rear";
                  break;
               case 2:
                  var11 = "Left";
                  break;
               case 3:
                  var11 = "Right";
               }

               var9 = var9.setSmashed(var11);
               var9.setBloodIntensity("Front", Rand.Next(0.3F, 1.0F));
               var9.setBloodIntensity("Rear", Rand.Next(0.3F, 1.0F));
               var9.setBloodIntensity("Left", Rand.Next(0.3F, 1.0F));
               var9.setBloodIntensity("Right", Rand.Next(0.3F, 1.0F));
               ArrayList var12 = this.addZombiesOnVehicle(Rand.Next(2, 4), (String)null, (Integer)null, var9);
               if (var12 != null) {
                  for(int var13 = 0; var13 < var12.size(); ++var13) {
                     IsoZombie var14 = (IsoZombie)var12.get(var13);
                     var14.upKillCount = false;
                     this.addBloodSplat(var14.getSquare(), Rand.Next(10, 20));
                     if (var6) {
                        var14.setSkeleton(true);
                        var14.getHumanVisual().setSkinTextureIndex(0);
                     } else {
                        var14.DoCorpseInventory();
                        if (Rand.NextBool(10)) {
                           var14.setFakeDead(true);
                           var14.bCrawling = true;
                           var14.setCanWalk(false);
                           var14.setCrawlerType(1);
                        }
                     }

                     new IsoDeadBody(var14, false);
                  }
               }
            }
         default:
         }
      }
   }
}
