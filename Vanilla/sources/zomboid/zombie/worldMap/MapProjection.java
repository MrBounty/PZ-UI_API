package zombie.worldMap;

import org.joml.Vector2d;

public final class MapProjection {
   public static final double EARTH_RADIUS_METERS = 6378137.0D;
   public static final double EARTH_HALF_CIRCUMFERENCE_METERS = 2.0037508342789244E7D;
   public static final double EARTH_CIRCUMFERENCE_METERS = 4.007501668557849E7D;
   public static final double MAX_LATITUDE_DEGREES = 85.05112878D;
   private static final double LOG_2 = Math.log(2.0D);

   static MapProjection.ProjectedMeters lngLatToProjectedMeters(MapProjection.LngLat var0) {
      MapProjection.ProjectedMeters var1 = new MapProjection.ProjectedMeters();
      var1.x = var0.longitude * 2.0037508342789244E7D / 180.0D;
      var1.y = Math.log(Math.tan(0.7853981633974483D + var0.latitude * 3.141592653589793D / 360.0D)) * 6378137.0D;
      return var1;
   }

   static double metersPerTileAtZoom(int var0) {
      return 4.007501668557849E7D / (double)(1 << var0);
   }

   static double metersPerPixelAtZoom(double var0, double var2) {
      return 4.007501668557849E7D / (exp2(var0) * var2);
   }

   static double zoomAtMetersPerPixel(double var0, double var2) {
      return log2(4.007501668557849E7D / (var0 * var2));
   }

   static MapProjection.BoundingBox mapLngLatBounds() {
      return new MapProjection.BoundingBox(new Vector2d(-180.0D, -85.05112878D), new Vector2d(180.0D, 85.05112878D));
   }

   static MapProjection.BoundingBox mapProjectedMetersBounds() {
      MapProjection.BoundingBox var0 = mapLngLatBounds();
      return new MapProjection.BoundingBox(lngLatToProjectedMeters(new MapProjection.LngLat(var0.min.x, var0.min.y)), lngLatToProjectedMeters(new MapProjection.LngLat(var0.max.x, var0.max.y)));
   }

   public static double exp2(double var0) {
      return Math.pow(2.0D, var0);
   }

   public static double log2(double var0) {
      return Math.log(var0) / LOG_2;
   }

   public static final class ProjectedMeters extends Vector2d {
   }

   public static final class LngLat {
      double longitude = 0.0D;
      double latitude = 0.0D;

      public LngLat(double var1, double var3) {
         this.longitude = var1;
         this.latitude = var3;
      }
   }

   public static final class BoundingBox {
      Vector2d min;
      Vector2d max;

      public BoundingBox(Vector2d var1, Vector2d var2) {
         this.min = var1;
         this.max = var2;
      }
   }
}
