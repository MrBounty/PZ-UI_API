package zombie.worldMap.markers;

import java.util.ArrayList;
import java.util.Objects;
import zombie.Lua.LuaManager;
import zombie.worldMap.UIWorldMap;

public class WorldMapMarkersV1 {
   private final UIWorldMap m_ui;
   private final ArrayList m_markers = new ArrayList();

   public WorldMapMarkersV1(UIWorldMap var1) {
      Objects.requireNonNull(var1);
      this.m_ui = var1;
   }

   public WorldMapMarkersV1.WorldMapGridSquareMarkerV1 addGridSquareMarker(int var1, int var2, int var3, float var4, float var5, float var6, float var7) {
      WorldMapGridSquareMarker var8 = this.m_ui.getAPIv1().getMarkers().addGridSquareMarker(var1, var2, var3, var4, var5, var6, var7);
      WorldMapMarkersV1.WorldMapGridSquareMarkerV1 var9 = new WorldMapMarkersV1.WorldMapGridSquareMarkerV1(var8);
      this.m_markers.add(var9);
      return var9;
   }

   public void removeMarker(WorldMapMarkersV1.WorldMapMarkerV1 var1) {
      if (this.m_markers.remove(var1)) {
         this.m_ui.getAPIv1().getMarkers().removeMarker(var1.m_marker);
      }
   }

   public void clear() {
      this.m_ui.getAPIv1().getMarkers().clear();
      this.m_markers.clear();
   }

   public static void setExposed(LuaManager.Exposer var0) {
      var0.setExposed(WorldMapMarkersV1.class);
      var0.setExposed(WorldMapMarkersV1.WorldMapMarkerV1.class);
      var0.setExposed(WorldMapMarkersV1.WorldMapGridSquareMarkerV1.class);
   }

   public static final class WorldMapGridSquareMarkerV1 extends WorldMapMarkersV1.WorldMapMarkerV1 {
      final WorldMapGridSquareMarker m_gridSquareMarker;

      WorldMapGridSquareMarkerV1(WorldMapGridSquareMarker var1) {
         super(var1);
         this.m_gridSquareMarker = var1;
      }

      public void setBlink(boolean var1) {
         this.m_gridSquareMarker.setBlink(var1);
      }

      public void setMinScreenRadius(int var1) {
         this.m_gridSquareMarker.setMinScreenRadius(var1);
      }
   }

   public static class WorldMapMarkerV1 {
      final WorldMapMarker m_marker;

      WorldMapMarkerV1(WorldMapMarker var1) {
         this.m_marker = var1;
      }
   }
}
