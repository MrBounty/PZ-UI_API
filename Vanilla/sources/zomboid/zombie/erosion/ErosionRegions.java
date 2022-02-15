package zombie.erosion;

import java.util.ArrayList;
import zombie.erosion.categories.ErosionCategory;
import zombie.erosion.categories.Flowerbed;
import zombie.erosion.categories.NatureBush;
import zombie.erosion.categories.NatureGeneric;
import zombie.erosion.categories.NaturePlants;
import zombie.erosion.categories.NatureTrees;
import zombie.erosion.categories.StreetCracks;
import zombie.erosion.categories.WallCracks;
import zombie.erosion.categories.WallVines;

public final class ErosionRegions {
   public static final int REGION_NATURE = 0;
   public static final int CATEGORY_TREES = 0;
   public static final int CATEGORY_BUSH = 1;
   public static final int CATEGORY_PLANTS = 2;
   public static final int CATEGORY_GENERIC = 3;
   public static final int REGION_STREET = 1;
   public static final int CATEGORY_STREET_CRACKS = 0;
   public static final int REGION_WALL = 2;
   public static final int CATEGORY_WALL_VINES = 0;
   public static final int CATEGORY_WALL_CRACKS = 1;
   public static final int REGION_FLOWERBED = 3;
   public static final int CATEGORY_FLOWERBED = 0;
   public static final ArrayList regions = new ArrayList();

   private static void addRegion(ErosionRegions.Region var0) {
      var0.ID = regions.size();
      regions.add(var0);
   }

   public static ErosionCategory getCategory(int var0, int var1) {
      return (ErosionCategory)((ErosionRegions.Region)regions.get(var0)).categories.get(var1);
   }

   public static void init() {
      regions.clear();
      addRegion((new ErosionRegions.Region(0, "blends_natural_01", true, true, false)).addCategory(0, new NatureTrees()).addCategory(1, new NatureBush()).addCategory(2, new NaturePlants()).addCategory(3, new NatureGeneric()));
      addRegion((new ErosionRegions.Region(1, "blends_street", true, true, false)).addCategory(0, new StreetCracks()));
      addRegion((new ErosionRegions.Region(2, (String)null, false, false, true)).addCategory(0, new WallVines()).addCategory(1, new WallCracks()));
      addRegion((new ErosionRegions.Region(3, (String)null, true, true, false)).addCategory(0, new Flowerbed()));

      for(int var0 = 0; var0 < regions.size(); ++var0) {
         ((ErosionRegions.Region)regions.get(var0)).init();
      }

   }

   public static void Reset() {
      for(int var0 = 0; var0 < regions.size(); ++var0) {
         ((ErosionRegions.Region)regions.get(var0)).Reset();
      }

      regions.clear();
   }

   public static final class Region {
      public int ID;
      public String tileNameMatch;
      public boolean checkExterior;
      public boolean isExterior;
      public boolean hasWall;
      public final ArrayList categories = new ArrayList();

      public Region(int var1, String var2, boolean var3, boolean var4, boolean var5) {
         this.ID = var1;
         this.tileNameMatch = var2;
         this.checkExterior = var3;
         this.isExterior = var4;
         this.hasWall = var5;
      }

      public ErosionRegions.Region addCategory(int var1, ErosionCategory var2) {
         var2.ID = var1;
         var2.region = this;
         this.categories.add(var2);
         return this;
      }

      public void init() {
         for(int var1 = 0; var1 < this.categories.size(); ++var1) {
            ((ErosionCategory)this.categories.get(var1)).init();
         }

      }

      public void Reset() {
         this.categories.clear();
      }
   }
}
