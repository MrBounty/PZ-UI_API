package zombie.worldMap;

import java.util.ArrayList;
import java.util.Iterator;

public final class WorldMapFeature {
   public final WorldMapCell m_cell;
   public final ArrayList m_geometries = new ArrayList();
   public WorldMapProperties m_properties = null;

   WorldMapFeature(WorldMapCell var1) {
      this.m_cell = var1;
   }

   public boolean hasLineString() {
      for(int var1 = 0; var1 < this.m_geometries.size(); ++var1) {
         if (((WorldMapGeometry)this.m_geometries.get(var1)).m_type == WorldMapGeometry.Type.LineString) {
            return true;
         }
      }

      return false;
   }

   public boolean hasPoint() {
      for(int var1 = 0; var1 < this.m_geometries.size(); ++var1) {
         if (((WorldMapGeometry)this.m_geometries.get(var1)).m_type == WorldMapGeometry.Type.Point) {
            return true;
         }
      }

      return false;
   }

   public boolean hasPolygon() {
      for(int var1 = 0; var1 < this.m_geometries.size(); ++var1) {
         if (((WorldMapGeometry)this.m_geometries.get(var1)).m_type == WorldMapGeometry.Type.Polygon) {
            return true;
         }
      }

      return false;
   }

   public boolean containsPoint(float var1, float var2) {
      for(int var3 = 0; var3 < this.m_geometries.size(); ++var3) {
         WorldMapGeometry var4 = (WorldMapGeometry)this.m_geometries.get(var3);
         if (var4.containsPoint(var1, var2)) {
            return true;
         }
      }

      return false;
   }

   public void dispose() {
      Iterator var1 = this.m_geometries.iterator();

      while(var1.hasNext()) {
         WorldMapGeometry var2 = (WorldMapGeometry)var1.next();
         var2.dispose();
      }

      this.m_properties.clear();
   }
}
