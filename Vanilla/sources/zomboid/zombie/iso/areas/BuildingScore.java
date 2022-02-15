package zombie.iso.areas;

public final class BuildingScore {
   public float weapons;
   public float food;
   public float wood;
   public float defense;
   public IsoBuilding building;
   public int size;
   public int safety;

   public BuildingScore(IsoBuilding var1) {
      this.building = var1;
   }
}
