package zombie.randomizedWorld.randomizedVehicleStory;

import java.util.ArrayList;
import java.util.List;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.Vector2;
import zombie.util.list.PZArrayUtil;
import zombie.vehicles.BaseVehicle;

public final class RVSUtilityVehicle extends RandomizedVehicleStoryBase {
   private ArrayList tools = null;
   private ArrayList carpenterTools = null;
   private RVSUtilityVehicle.Params params = new RVSUtilityVehicle.Params();

   public RVSUtilityVehicle() {
      this.name = "Utility Vehicle";
      this.minZoneWidth = 8;
      this.minZoneHeight = 9;
      this.setChance(7);
      this.tools = new ArrayList();
      this.tools.add("Base.PickAxe");
      this.tools.add("Base.Shovel");
      this.tools.add("Base.Shovel2");
      this.tools.add("Base.Hammer");
      this.tools.add("Base.LeadPipe");
      this.tools.add("Base.PipeWrench");
      this.tools.add("Base.Sledgehammer");
      this.tools.add("Base.Sledgehammer2");
      this.carpenterTools = new ArrayList();
      this.carpenterTools.add("Base.Hammer");
      this.carpenterTools.add("Base.NailsBox");
      this.carpenterTools.add("Base.Plank");
      this.carpenterTools.add("Base.Plank");
      this.carpenterTools.add("Base.Plank");
      this.carpenterTools.add("Base.Screwdriver");
      this.carpenterTools.add("Base.Saw");
      this.carpenterTools.add("Base.Saw");
      this.carpenterTools.add("Base.Woodglue");
   }

   public void randomizeVehicleStory(IsoMetaGrid.Zone var1, IsoChunk var2) {
      this.callVehicleStorySpawner(var1, var2, 0.0F);
   }

   public void doUtilityVehicle(IsoMetaGrid.Zone var1, IsoChunk var2, String var3, String var4, String var5, Integer var6, String var7, ArrayList var8, int var9, boolean var10) {
      this.params.zoneName = var3;
      this.params.scriptName = var4;
      this.params.outfits = var5;
      this.params.femaleChance = var6;
      this.params.vehicleDistrib = var7;
      this.params.items = var8;
      this.params.nbrOfItem = var9;
      this.params.addTrailer = var10;
   }

   public boolean initVehicleStorySpawner(IsoMetaGrid.Zone var1, IsoChunk var2, boolean var3) {
      int var4 = Rand.Next(0, 7);
      switch(var4) {
      case 0:
         this.doUtilityVehicle(var1, var2, (String)null, "Base.PickUpTruck", "ConstructionWorker", 0, "ConstructionWorker", this.tools, Rand.Next(0, 3), true);
         break;
      case 1:
         this.doUtilityVehicle(var1, var2, "police", (String)null, "Police", (Integer)null, (String)null, (ArrayList)null, 0, false);
         break;
      case 2:
         this.doUtilityVehicle(var1, var2, "fire", (String)null, "Fireman", (Integer)null, (String)null, (ArrayList)null, 0, false);
         break;
      case 3:
         this.doUtilityVehicle(var1, var2, "ranger", (String)null, "Ranger", (Integer)null, (String)null, (ArrayList)null, 0, true);
         break;
      case 4:
         this.doUtilityVehicle(var1, var2, "mccoy", (String)null, "McCoys", 0, "Carpenter", this.carpenterTools, Rand.Next(2, 6), true);
         break;
      case 5:
         this.doUtilityVehicle(var1, var2, "postal", (String)null, "Postal", (Integer)null, (String)null, (ArrayList)null, 0, false);
         break;
      case 6:
         this.doUtilityVehicle(var1, var2, "fossoil", (String)null, "Fossoil", (Integer)null, (String)null, (ArrayList)null, 0, false);
      }

      VehicleStorySpawner var5 = VehicleStorySpawner.getInstance();
      var5.clear();
      Vector2 var6 = IsoDirections.N.ToVector();
      float var7 = 0.5235988F;
      if (var3) {
         var7 = 0.0F;
      }

      var6.rotate(Rand.Next(-var7, var7));
      float var8 = -2.0F;
      byte var9 = 5;
      var5.addElement("vehicle1", 0.0F, var8, var6.getDirection(), 2.0F, (float)var9);
      if (this.params.addTrailer && Rand.NextBool(7)) {
         byte var10 = 3;
         var5.addElement("trailer", 0.0F, var8 + (float)var9 / 2.0F + 1.0F + (float)var10 / 2.0F, var6.getDirection(), 2.0F, (float)var10);
      }

      if (this.params.items != null) {
         for(int var11 = 0; var11 < this.params.nbrOfItem; ++var11) {
            var5.addElement("tool", Rand.Next(-3.5F, 3.5F), Rand.Next(-3.5F, 3.5F), 0.0F, 1.0F, 1.0F);
         }
      }

      var5.setParameter("zone", var1);
      return true;
   }

   public void spawnElement(VehicleStorySpawner var1, VehicleStorySpawner.Element var2) {
      IsoGridSquare var3 = var2.square;
      if (var3 != null) {
         float var4 = var2.z;
         IsoMetaGrid.Zone var5 = (IsoMetaGrid.Zone)var1.getParameter("zone", IsoMetaGrid.Zone.class);
         BaseVehicle var6 = (BaseVehicle)var1.getParameter("vehicle1", BaseVehicle.class);
         String var7 = var2.id;
         byte var8 = -1;
         switch(var7.hashCode()) {
         case -1067215565:
            if (var7.equals("trailer")) {
               var8 = 1;
            }
            break;
         case 3565976:
            if (var7.equals("tool")) {
               var8 = 0;
            }
            break;
         case 2014205573:
            if (var7.equals("vehicle1")) {
               var8 = 2;
            }
         }

         switch(var8) {
         case 0:
            if (var6 != null) {
               float var9 = PZMath.max(var2.position.x - (float)var3.x, 0.001F);
               float var10 = PZMath.max(var2.position.y - (float)var3.y, 0.001F);
               float var11 = 0.0F;
               var3.AddWorldInventoryItem((String)PZArrayUtil.pickRandom((List)this.params.items), var9, var10, var11);
            }
            break;
         case 1:
            if (var6 != null) {
               this.addTrailer(var6, var5, var3.getChunk(), this.params.zoneName, this.params.vehicleDistrib, Rand.NextBool(1) ? "Base.Trailer" : "Base.TrailerCover");
            }
            break;
         case 2:
            var6 = this.addVehicle(var5, var2.position.x, var2.position.y, var4, var2.direction, this.params.zoneName, this.params.scriptName, (Integer)null, this.params.vehicleDistrib);
            if (var6 != null) {
               this.addZombiesOnVehicle(Rand.Next(2, 5), this.params.outfits, this.params.femaleChance, var6);
            }
         }

      }
   }

   private static final class Params {
      String zoneName;
      String scriptName;
      String outfits;
      Integer femaleChance;
      String vehicleDistrib;
      ArrayList items;
      int nbrOfItem;
      boolean addTrailer;
   }
}
