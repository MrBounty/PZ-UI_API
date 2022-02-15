package zombie.randomizedWorld.randomizedVehicleStory;

import zombie.iso.IsoChunk;
import zombie.iso.IsoMetaGrid;

public final class VehicleStorySpawnData {
   public RandomizedVehicleStoryBase m_story;
   public IsoMetaGrid.Zone m_zone;
   public float m_spawnX;
   public float m_spawnY;
   public float m_direction;
   public int m_x1;
   public int m_y1;
   public int m_x2;
   public int m_y2;

   public VehicleStorySpawnData(RandomizedVehicleStoryBase var1, IsoMetaGrid.Zone var2, float var3, float var4, float var5, int var6, int var7, int var8, int var9) {
      this.m_story = var1;
      this.m_zone = var2;
      this.m_spawnX = var3;
      this.m_spawnY = var4;
      this.m_direction = var5;
      this.m_x1 = var6;
      this.m_y1 = var7;
      this.m_x2 = var8;
      this.m_y2 = var9;
   }

   public boolean isValid(IsoMetaGrid.Zone var1, IsoChunk var2) {
      if (var1 != this.m_zone) {
         return false;
      } else if (!this.m_story.isFullyStreamedIn(this.m_x1, this.m_y1, this.m_x2, this.m_y2)) {
         return false;
      } else {
         var2.setRandomVehicleStoryToSpawnLater((VehicleStorySpawnData)null);
         return this.m_story.isValid(var1, var2, false);
      }
   }
}
