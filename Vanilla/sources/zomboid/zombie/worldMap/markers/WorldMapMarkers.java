package zombie.worldMap.markers;

import java.util.ArrayList;
import zombie.util.Pool;
import zombie.worldMap.UIWorldMap;

public final class WorldMapMarkers {
   private static final Pool s_gridSquareMarkerPool = new Pool(WorldMapGridSquareMarker::new);
   private final ArrayList m_markers = new ArrayList();

   public WorldMapGridSquareMarker addGridSquareMarker(int var1, int var2, int var3, float var4, float var5, float var6, float var7) {
      WorldMapGridSquareMarker var8 = ((WorldMapGridSquareMarker)s_gridSquareMarkerPool.alloc()).init(var1, var2, var3, var4, var5, var6, var7);
      this.m_markers.add(var8);
      return var8;
   }

   public void removeMarker(WorldMapMarker var1) {
      if (this.m_markers.contains(var1)) {
         this.m_markers.remove(var1);
         var1.release();
      }
   }

   public void clear() {
      for(int var1 = 0; var1 < this.m_markers.size(); ++var1) {
         ((WorldMapMarker)this.m_markers.get(var1)).release();
      }

      this.m_markers.clear();
   }

   public void render(UIWorldMap var1) {
      for(int var2 = 0; var2 < this.m_markers.size(); ++var2) {
         ((WorldMapMarker)this.m_markers.get(var2)).render(var1);
      }

   }
}
