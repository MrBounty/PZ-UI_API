package zombie.worldMap;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.core.math.PZMath;
import zombie.vehicles.Clipper;

public final class WorldMapGeometry {
   public WorldMapGeometry.Type m_type;
   public final ArrayList m_points = new ArrayList();
   public int m_minX;
   public int m_minY;
   public int m_maxX;
   public int m_maxY;
   public float[] m_triangles = null;
   public ArrayList m_trianglesPerZoom = null;
   public int m_vboIndex1 = -1;
   public int m_vboIndex2 = -1;
   public int m_vboIndex3 = -1;
   public int m_vboIndex4 = -1;
   private static Clipper s_clipper = null;
   private static ByteBuffer s_vertices = null;

   public void calculateBounds() {
      this.m_minX = this.m_minY = Integer.MAX_VALUE;
      this.m_maxX = this.m_maxY = Integer.MIN_VALUE;

      for(int var1 = 0; var1 < this.m_points.size(); ++var1) {
         WorldMapPoints var2 = (WorldMapPoints)this.m_points.get(var1);
         var2.calculateBounds();
         this.m_minX = PZMath.min(this.m_minX, var2.m_minX);
         this.m_minY = PZMath.min(this.m_minY, var2.m_minY);
         this.m_maxX = PZMath.max(this.m_maxX, var2.m_maxX);
         this.m_maxY = PZMath.max(this.m_maxY, var2.m_maxY);
      }

   }

   public boolean containsPoint(float var1, float var2) {
      if (this.m_type == WorldMapGeometry.Type.Polygon && !this.m_points.isEmpty()) {
         return this.isPointInPolygon_WindingNumber(var1, var2, 0) != WorldMapGeometry.PolygonHit.Outside;
      } else {
         return false;
      }
   }

   public void triangulate(double[] var1) {
      if (s_clipper == null) {
         s_clipper = new Clipper();
      }

      s_clipper.clear();
      WorldMapPoints var2 = (WorldMapPoints)this.m_points.get(0);
      if (s_vertices == null || s_vertices.capacity() < var2.size() * 50 * 4) {
         s_vertices = ByteBuffer.allocateDirect(var2.size() * 50 * 4);
      }

      s_vertices.clear();
      int var3;
      if (var2.isClockwise()) {
         for(var3 = var2.numPoints() - 1; var3 >= 0; --var3) {
            s_vertices.putFloat((float)var2.getX(var3));
            s_vertices.putFloat((float)var2.getY(var3));
         }
      } else {
         for(var3 = 0; var3 < var2.numPoints(); ++var3) {
            s_vertices.putFloat((float)var2.getX(var3));
            s_vertices.putFloat((float)var2.getY(var3));
         }
      }

      s_clipper.addPath(var2.numPoints(), s_vertices, false);

      int var5;
      for(var3 = 1; var3 < this.m_points.size(); ++var3) {
         s_vertices.clear();
         WorldMapPoints var4 = (WorldMapPoints)this.m_points.get(var3);
         if (var4.isClockwise()) {
            for(var5 = var4.numPoints() - 1; var5 >= 0; --var5) {
               s_vertices.putFloat((float)var4.getX(var5));
               s_vertices.putFloat((float)var4.getY(var5));
            }
         } else {
            for(var5 = 0; var5 < var4.numPoints(); ++var5) {
               s_vertices.putFloat((float)var4.getX(var5));
               s_vertices.putFloat((float)var4.getY(var5));
            }
         }

         s_clipper.addPath(var4.numPoints(), s_vertices, true);
      }

      if (this.m_minX < 0 || this.m_minY < 0 || this.m_maxX > 300 || this.m_maxY > 300) {
         short var26 = 900;
         float var24 = (float)(-var26);
         float var27 = (float)(-var26);
         float var6 = (float)(300 + var26);
         float var7 = (float)(-var26);
         float var8 = (float)(300 + var26);
         float var9 = (float)(300 + var26);
         float var10 = (float)(-var26);
         float var11 = (float)(300 + var26);
         float var12 = (float)(-var26);
         float var13 = 0.0F;
         float var14 = 0.0F;
         float var15 = 0.0F;
         float var16 = 0.0F;
         float var17 = 300.0F;
         float var18 = 300.0F;
         float var19 = 300.0F;
         float var20 = 300.0F;
         float var21 = 0.0F;
         float var22 = (float)(-var26);
         float var23 = 0.0F;
         s_vertices.clear();
         s_vertices.putFloat(var24).putFloat(var27);
         s_vertices.putFloat(var6).putFloat(var7);
         s_vertices.putFloat(var8).putFloat(var9);
         s_vertices.putFloat(var10).putFloat(var11);
         s_vertices.putFloat(var12).putFloat(var13);
         s_vertices.putFloat(var14).putFloat(var15);
         s_vertices.putFloat(var16).putFloat(var17);
         s_vertices.putFloat(var18).putFloat(var19);
         s_vertices.putFloat(var20).putFloat(var21);
         s_vertices.putFloat(var22).putFloat(var23);
         s_clipper.addPath(10, s_vertices, true);
      }

      var3 = s_clipper.generatePolygons(0.0D);
      if (var3 > 0) {
         s_vertices.clear();
         int var25 = s_clipper.triangulate(0, s_vertices);
         this.m_triangles = new float[var25 * 2];

         for(var5 = 0; var5 < var25; ++var5) {
            this.m_triangles[var5 * 2] = s_vertices.getFloat();
            this.m_triangles[var5 * 2 + 1] = s_vertices.getFloat();
         }

         if (var1 != null) {
            for(var5 = 0; var5 < var1.length; ++var5) {
               double var28 = var1[var5] - (var5 == 0 ? 0.0D : var1[var5 - 1]);
               var3 = s_clipper.generatePolygons(var28);
               if (var3 > 0) {
                  s_vertices.clear();
                  var25 = s_clipper.triangulate(0, s_vertices);
                  WorldMapGeometry.TrianglesPerZoom var29 = new WorldMapGeometry.TrianglesPerZoom();
                  var29.m_triangles = new float[var25 * 2];
                  var29.m_delta = var1[var5];

                  for(int var30 = 0; var30 < var25; ++var30) {
                     var29.m_triangles[var30 * 2] = s_vertices.getFloat();
                     var29.m_triangles[var30 * 2 + 1] = s_vertices.getFloat();
                  }

                  if (this.m_trianglesPerZoom == null) {
                     this.m_trianglesPerZoom = new ArrayList();
                  }

                  this.m_trianglesPerZoom.add(var29);
               }
            }

         }
      }
   }

   WorldMapGeometry.TrianglesPerZoom findTriangles(double var1) {
      if (this.m_trianglesPerZoom == null) {
         return null;
      } else {
         for(int var3 = 0; var3 < this.m_trianglesPerZoom.size(); ++var3) {
            WorldMapGeometry.TrianglesPerZoom var4 = (WorldMapGeometry.TrianglesPerZoom)this.m_trianglesPerZoom.get(var3);
            if (var4.m_delta == var1) {
               return var4;
            }
         }

         return null;
      }
   }

   public void dispose() {
      this.m_points.clear();
      this.m_triangles = null;
   }

   float isLeft(float var1, float var2, float var3, float var4, float var5, float var6) {
      return (var3 - var1) * (var6 - var2) - (var5 - var1) * (var4 - var2);
   }

   WorldMapGeometry.PolygonHit isPointInPolygon_WindingNumber(float var1, float var2, int var3) {
      int var4 = 0;
      WorldMapPoints var5 = (WorldMapPoints)this.m_points.get(0);

      for(int var6 = 0; var6 < var5.numPoints(); ++var6) {
         int var7 = var5.getX(var6);
         int var8 = var5.getY(var6);
         int var9 = var5.getX((var6 + 1) % var5.numPoints());
         int var10 = var5.getY((var6 + 1) % var5.numPoints());
         if ((float)var8 <= var2) {
            if ((float)var10 > var2 && this.isLeft((float)var7, (float)var8, (float)var9, (float)var10, var1, var2) > 0.0F) {
               ++var4;
            }
         } else if ((float)var10 <= var2 && this.isLeft((float)var7, (float)var8, (float)var9, (float)var10, var1, var2) < 0.0F) {
            --var4;
         }
      }

      return var4 == 0 ? WorldMapGeometry.PolygonHit.Outside : WorldMapGeometry.PolygonHit.Inside;
   }

   public static enum Type {
      LineString,
      Point,
      Polygon;

      // $FF: synthetic method
      private static WorldMapGeometry.Type[] $values() {
         return new WorldMapGeometry.Type[]{LineString, Point, Polygon};
      }
   }

   private static enum PolygonHit {
      OnEdge,
      Inside,
      Outside;

      // $FF: synthetic method
      private static WorldMapGeometry.PolygonHit[] $values() {
         return new WorldMapGeometry.PolygonHit[]{OnEdge, Inside, Outside};
      }
   }

   public static final class TrianglesPerZoom {
      public float[] m_triangles;
      double m_delta;
   }
}
