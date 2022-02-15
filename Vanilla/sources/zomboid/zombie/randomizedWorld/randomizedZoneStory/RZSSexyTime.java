package zombie.randomizedWorld.randomizedZoneStory;

import java.util.ArrayList;
import zombie.characters.IsoZombie;
import zombie.core.Rand;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoMetaGrid;
import zombie.vehicles.BaseVehicle;

public class RZSSexyTime extends RandomizedZoneStoryBase {
   private final ArrayList pantsMaleItems = new ArrayList();
   private final ArrayList pantsFemaleItems = new ArrayList();
   private final ArrayList topItems = new ArrayList();
   private final ArrayList shoesItems = new ArrayList();

   public RZSSexyTime() {
      this.name = "Sexy Time";
      this.chance = 5;
      this.minZoneHeight = 5;
      this.minZoneWidth = 5;
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Beach.toString());
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Forest.toString());
      this.zoneType.add(RandomizedZoneStoryBase.ZoneType.Lake.toString());
      this.shoesItems.add("Base.Shoes_Random");
      this.shoesItems.add("Base.Shoes_TrainerTINT");
      this.pantsMaleItems.add("Base.TrousersMesh_DenimLight");
      this.pantsMaleItems.add("Base.Trousers_DefaultTEXTURE_TINT");
      this.pantsMaleItems.add("Base.Trousers_Denim");
      this.pantsFemaleItems.add("Base.Skirt_Knees");
      this.pantsFemaleItems.add("Base.Skirt_Long");
      this.pantsFemaleItems.add("Base.Skirt_Short");
      this.pantsFemaleItems.add("Base.Skirt_Normal");
      this.topItems.add("Base.Shirt_FormalWhite");
      this.topItems.add("Base.Shirt_FormalWhite_ShortSleeve");
      this.topItems.add("Base.Tshirt_DefaultTEXTURE_TINT");
      this.topItems.add("Base.Tshirt_PoloTINT");
      this.topItems.add("Base.Tshirt_WhiteLongSleeveTINT");
      this.topItems.add("Base.Tshirt_WhiteTINT");
   }

   public void randomizeZoneStory(IsoMetaGrid.Zone var1) {
      this.cleanAreaForStory(this, var1);
      BaseVehicle var2 = this.addVehicle(var1, this.getSq(var1.pickedXForZoneStory, var1.pickedYForZoneStory, var1.z), (IsoChunk)null, (String)null, "Base.VanAmbulance", (Integer)null, (IsoDirections)null, (String)null);
      boolean var3 = Rand.Next(7) == 0;
      boolean var4 = Rand.Next(7) == 0;
      if (var3) {
         this.addItemsOnGround(var1, true, var2);
         this.addItemsOnGround(var1, true, var2);
      } else if (var4) {
         this.addItemsOnGround(var1, false, var2);
         this.addItemsOnGround(var1, false, var2);
      } else {
         this.addItemsOnGround(var1, true, var2);
         this.addItemsOnGround(var1, false, var2);
      }

   }

   private void addItemsOnGround(IsoMetaGrid.Zone var1, boolean var2, BaseVehicle var3) {
      byte var4 = 100;
      if (!var2) {
         var4 = 0;
      }

      ArrayList var5 = this.addZombiesOnVehicle(1, "Naked", Integer.valueOf(var4), var3);
      if (!var5.isEmpty()) {
         IsoZombie var6 = (IsoZombie)var5.get(0);
         this.addRandomItemOnGround(var6.getSquare(), this.shoesItems);
         this.addRandomItemOnGround(var6.getSquare(), this.topItems);
         this.addRandomItemOnGround(var6.getSquare(), var2 ? this.pantsMaleItems : this.pantsFemaleItems);
      }
   }
}
