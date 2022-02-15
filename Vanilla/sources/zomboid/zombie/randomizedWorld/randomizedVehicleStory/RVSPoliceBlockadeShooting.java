package zombie.randomizedWorld.randomizedVehicleStory;

import zombie.core.Rand;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoObject;
import zombie.iso.Vector2;
import zombie.vehicles.BaseVehicle;

public final class RVSPoliceBlockadeShooting extends RandomizedVehicleStoryBase {
   public RVSPoliceBlockadeShooting() {
      this.name = "Police Blockade Shooting";
      this.minZoneWidth = 8;
      this.minZoneHeight = 8;
      this.setChance(1);
      this.setMaximumDays(30);
   }

   public boolean isValid(IsoMetaGrid.Zone var1, IsoChunk var2, boolean var3) {
      boolean var4 = super.isValid(var1, var2, var3);
      return !var4 ? false : var1.isRectangle();
   }

   public void randomizeVehicleStory(IsoMetaGrid.Zone var1, IsoChunk var2) {
      this.callVehicleStorySpawner(var1, var2, 0.0F);
   }

   public boolean initVehicleStorySpawner(IsoMetaGrid.Zone var1, IsoChunk var2, boolean var3) {
      VehicleStorySpawner var4 = VehicleStorySpawner.getInstance();
      var4.clear();
      float var5 = 0.17453292F;
      if (var3) {
         var5 = 0.0F;
      }

      float var6 = 1.5F;
      float var7 = 1.0F;
      if (this.zoneWidth >= 10) {
         var6 = 2.5F;
         var7 = 0.0F;
      }

      boolean var8 = Rand.NextBool(2);
      if (var3) {
         var8 = true;
      }

      IsoDirections var9 = Rand.NextBool(2) ? IsoDirections.W : IsoDirections.E;
      Vector2 var10 = var9.ToVector();
      var10.rotate(Rand.Next(-var5, var5));
      var4.addElement("vehicle1", -var6, var7, var10.getDirection(), 2.0F, 5.0F);
      var10 = var9.RotLeft(4).ToVector();
      var10.rotate(Rand.Next(-var5, var5));
      var4.addElement("vehicle2", var6, -var7, var10.getDirection(), 2.0F, 5.0F);
      var4.addElement("barricade", 0.0F, var8 ? -var7 - 2.5F : var7 + 2.5F, IsoDirections.N.ToVector().getDirection(), (float)this.zoneWidth, 1.0F);
      int var11 = Rand.Next(7, 15);

      for(int var12 = 0; var12 < var11; ++var12) {
         var4.addElement("corpse", Rand.Next((float)(-this.zoneWidth) / 2.0F + 1.0F, (float)this.zoneWidth / 2.0F - 1.0F), var8 ? (float)Rand.Next(-7, -4) - var7 : (float)Rand.Next(5, 8) + var7, IsoDirections.getRandom().ToVector().getDirection(), 1.0F, 2.0F);
      }

      String var13 = "Base.CarLightsPolice";
      if (Rand.NextBool(3)) {
         var13 = "Base.PickUpVanLightsPolice";
      }

      var4.setParameter("zone", var1);
      var4.setParameter("script", var13);
      return true;
   }

   public void spawnElement(VehicleStorySpawner var1, VehicleStorySpawner.Element var2) {
      IsoGridSquare var3 = var2.square;
      if (var3 != null) {
         float var4 = var2.z;
         IsoMetaGrid.Zone var5 = (IsoMetaGrid.Zone)var1.getParameter("zone", IsoMetaGrid.Zone.class);
         String var6 = var1.getParameterString("script");
         String var7 = var2.id;
         byte var8 = -1;
         switch(var7.hashCode()) {
         case -1354663044:
            if (var7.equals("corpse")) {
               var8 = 1;
            }
            break;
         case 1971942889:
            if (var7.equals("barricade")) {
               var8 = 0;
            }
            break;
         case 2014205573:
            if (var7.equals("vehicle1")) {
               var8 = 2;
            }
            break;
         case 2014205574:
            if (var7.equals("vehicle2")) {
               var8 = 3;
            }
         }

         BaseVehicle var9;
         switch(var8) {
         case 0:
            int var12;
            IsoGridSquare var13;
            int var14;
            int var16;
            int var18;
            if (this.horizontalZone) {
               var14 = (int)(var2.position.y - var2.width / 2.0F);
               var16 = (int)(var2.position.y + var2.width / 2.0F) - 1;
               var18 = (int)var2.position.x;

               for(var12 = var14; var12 <= var16; ++var12) {
                  var13 = IsoCell.getInstance().getGridSquare(var18, var12, var5.z);
                  if (var13 != null) {
                     if (var12 != var14 && var12 != var16) {
                        var13.AddTileObject(IsoObject.getNew(var13, "construction_01_9", (String)null, false));
                     } else {
                        var13.AddTileObject(IsoObject.getNew(var13, "street_decoration_01_26", (String)null, false));
                     }
                  }
               }

               return;
            } else {
               var14 = (int)(var2.position.x - var2.width / 2.0F);
               var16 = (int)(var2.position.x + var2.width / 2.0F) - 1;
               var18 = (int)var2.position.y;

               for(var12 = var14; var12 <= var16; ++var12) {
                  var13 = IsoCell.getInstance().getGridSquare(var12, var18, var5.z);
                  if (var13 != null) {
                     if (var12 != var14 && var12 != var16) {
                        var13.AddTileObject(IsoObject.getNew(var13, "construction_01_8", (String)null, false));
                     } else {
                        var13.AddTileObject(IsoObject.getNew(var13, "street_decoration_01_26", (String)null, false));
                     }
                  }
               }

               return;
            }
         case 1:
            var9 = (BaseVehicle)var1.getParameter("vehicle1", BaseVehicle.class);
            if (var9 != null) {
               createRandomDeadBody(var2.position.x, var2.position.y, (float)var5.z, var2.direction, false, 10, 10, (String)null);
               IsoDirections var15 = this.horizontalZone ? (var2.position.x < var9.x ? IsoDirections.W : IsoDirections.E) : (var2.position.y < var9.y ? IsoDirections.N : IsoDirections.S);
               float var17 = var15.ToVector().getDirection();
               this.addTrailOfBlood(var2.position.x, var2.position.y, var2.z, var17, 5);
            }
            break;
         case 2:
         case 3:
            var9 = this.addVehicle(var5, var2.position.x, var2.position.y, var4, var2.direction, (String)null, var6, (Integer)null, (String)null);
            if (var9 != null) {
               var1.setParameter(var2.id, var9);
               if (Rand.NextBool(3)) {
                  var9.setHeadlightsOn(true);
                  var9.setLightbarLightsMode(2);
               }

               String var10 = "PoliceRiot";
               Integer var11 = 0;
               this.addZombiesOnVehicle(Rand.Next(2, 4), var10, var11, var9);
            }
         }

      }
   }
}
