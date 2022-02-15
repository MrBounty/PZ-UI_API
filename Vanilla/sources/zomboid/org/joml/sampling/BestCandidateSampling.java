package org.joml.sampling;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.joml.Random;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class BestCandidateSampling {
   public static class Cube {
      private int numSamples;
      private int numCandidates = 60;
      private long seed;

      public BestCandidateSampling.Cube seed(long var1) {
         this.seed = var1;
         return this;
      }

      public BestCandidateSampling.Cube numSamples(int var1) {
         this.numSamples = var1;
         return this;
      }

      public BestCandidateSampling.Cube numCandidates(int var1) {
         this.numCandidates = var1;
         return this;
      }

      public BestCandidateSampling.Cube generate(final float[] var1) {
         final BestCandidateSampling.IntHolder var2 = new BestCandidateSampling.IntHolder();
         return this.generate(new Callback3d() {
            public void onNewSample(float var1x, float var2x, float var3) {
               var1[3 * var2.value + 0] = var1x;
               var1[3 * var2.value + 1] = var2x;
               var1[3 * var2.value + 2] = var3;
               ++var2.value;
            }
         });
      }

      public BestCandidateSampling.Cube generate(final FloatBuffer var1) {
         final BestCandidateSampling.IntHolder var2 = new BestCandidateSampling.IntHolder();
         final int var3 = var1.position();
         return this.generate(new Callback3d() {
            public void onNewSample(float var1x, float var2x, float var3x) {
               var1.put(var3 + 3 * var2.value + 0, var1x);
               var1.put(var3 + 3 * var2.value + 1, var2x);
               var1.put(var3 + 3 * var2.value + 2, var3x);
               ++var2.value;
            }
         });
      }

      public BestCandidateSampling.Cube generate(Callback3d var1) {
         BestCandidateSampling.Octree var2 = new BestCandidateSampling.Octree(-1.0F, -1.0F, -1.0F, 2.0F);
         Random var3 = new Random(this.seed);

         for(int var4 = 0; var4 < this.numSamples; ++var4) {
            float var5 = 0.0F;
            float var6 = 0.0F;
            float var7 = 0.0F;
            float var8 = 0.0F;

            for(int var9 = 0; var9 < this.numCandidates; ++var9) {
               float var10 = var3.nextFloat() * 2.0F - 1.0F;
               float var11 = var3.nextFloat() * 2.0F - 1.0F;
               float var12 = var3.nextFloat() * 2.0F - 1.0F;
               float var13 = var2.nearest(var10, var11, var12, var8, Float.POSITIVE_INFINITY);
               if (var13 > var8) {
                  var8 = var13;
                  var5 = var10;
                  var6 = var11;
                  var7 = var12;
               }
            }

            var1.onNewSample(var5, var6, var7);
            var2.insert(new Vector3f(var5, var6, var7));
         }

         return this;
      }
   }

   private static class Octree {
      private static final int MAX_OBJECTS_PER_NODE = 32;
      private static final int PXNYNZ = 0;
      private static final int NXNYNZ = 1;
      private static final int NXPYNZ = 2;
      private static final int PXPYNZ = 3;
      private static final int PXNYPZ = 4;
      private static final int NXNYPZ = 5;
      private static final int NXPYPZ = 6;
      private static final int PXPYPZ = 7;
      private float minX;
      private float minY;
      private float minZ;
      private float hs;
      private ArrayList objects;
      private BestCandidateSampling.Octree[] children;

      Octree(float var1, float var2, float var3, float var4) {
         this.minX = var1;
         this.minY = var2;
         this.minZ = var3;
         this.hs = var4 * 0.5F;
      }

      private void split() {
         this.children = new BestCandidateSampling.Octree[8];
         this.children[1] = new BestCandidateSampling.Octree(this.minX, this.minY, this.minZ, this.hs);
         this.children[0] = new BestCandidateSampling.Octree(this.minX + this.hs, this.minY, this.minZ, this.hs);
         this.children[2] = new BestCandidateSampling.Octree(this.minX, this.minY + this.hs, this.minZ, this.hs);
         this.children[3] = new BestCandidateSampling.Octree(this.minX + this.hs, this.minY + this.hs, this.minZ, this.hs);
         this.children[5] = new BestCandidateSampling.Octree(this.minX, this.minY, this.minZ + this.hs, this.hs);
         this.children[4] = new BestCandidateSampling.Octree(this.minX + this.hs, this.minY, this.minZ + this.hs, this.hs);
         this.children[6] = new BestCandidateSampling.Octree(this.minX, this.minY + this.hs, this.minZ + this.hs, this.hs);
         this.children[7] = new BestCandidateSampling.Octree(this.minX + this.hs, this.minY + this.hs, this.minZ + this.hs, this.hs);
      }

      private void insertIntoChild(Vector3f var1) {
         this.children[this.octant(var1.x, var1.y, var1.z)].insert(var1);
      }

      void insert(Vector3f var1) {
         if (this.children != null) {
            this.insertIntoChild(var1);
         } else {
            if (this.objects != null && this.objects.size() == 32) {
               this.split();

               for(int var2 = 0; var2 < this.objects.size(); ++var2) {
                  this.insertIntoChild((Vector3f)this.objects.get(var2));
               }

               this.objects = null;
               this.insertIntoChild(var1);
            } else {
               if (this.objects == null) {
                  this.objects = new ArrayList(32);
               }

               this.objects.add(var1);
            }

         }
      }

      private int octant(float var1, float var2, float var3) {
         if (var1 < this.minX + this.hs) {
            if (var2 < this.minY + this.hs) {
               return var3 < this.minZ + this.hs ? 1 : 5;
            } else {
               return var3 < this.minZ + this.hs ? 2 : 6;
            }
         } else if (var2 < this.minY + this.hs) {
            return var3 < this.minZ + this.hs ? 0 : 4;
         } else {
            return var3 < this.minZ + this.hs ? 3 : 7;
         }
      }

      float nearest(float var1, float var2, float var3, float var4, float var5) {
         float var6 = var5;
         if (!(var1 < this.minX - var5) && !(var1 > this.minX + this.hs * 2.0F + var5) && !(var2 < this.minY - var5) && !(var2 > this.minY + this.hs * 2.0F + var5) && !(var3 < this.minZ - var5) && !(var3 > this.minZ + this.hs * 2.0F + var5)) {
            if (this.children != null) {
               int var12 = this.octant(var1, var2, var3);

               for(int var13 = 0; var13 < 8; ++var13) {
                  float var14 = this.children[var12].nearest(var1, var2, var3, var4, var6);
                  var6 = Math.min(var14, var6);
                  if (var6 <= var4) {
                     return var4;
                  }

                  var12 = var12 + 1 & 7;
               }

               return var6;
            } else {
               float var7 = var5 * var5;
               float var8 = var4 * var4;

               for(int var9 = 0; this.objects != null && var9 < this.objects.size(); ++var9) {
                  Vector3f var10 = (Vector3f)this.objects.get(var9);
                  float var11 = var10.distanceSquared(var1, var2, var3);
                  if (var11 <= var8) {
                     return var4;
                  }

                  if (var11 < var7) {
                     var7 = var11;
                  }
               }

               return Math.sqrt(var7);
            }
         } else {
            return var5;
         }
      }
   }

   public static class Quad {
      private int numSamples;
      private int numCandidates = 60;
      private long seed;

      public BestCandidateSampling.Quad seed(long var1) {
         this.seed = var1;
         return this;
      }

      public BestCandidateSampling.Quad numSamples(int var1) {
         this.numSamples = var1;
         return this;
      }

      public BestCandidateSampling.Quad numCandidates(int var1) {
         this.numCandidates = var1;
         return this;
      }

      public BestCandidateSampling.Quad generate(final float[] var1) {
         final BestCandidateSampling.IntHolder var2 = new BestCandidateSampling.IntHolder();
         return this.generate(new Callback2d() {
            public void onNewSample(float var1x, float var2x) {
               var1[2 * var2.value + 0] = var1x;
               var1[2 * var2.value + 1] = var2x;
               ++var2.value;
            }
         });
      }

      public BestCandidateSampling.Quad generate(final FloatBuffer var1) {
         final BestCandidateSampling.IntHolder var2 = new BestCandidateSampling.IntHolder();
         final int var3 = var1.position();
         return this.generate(new Callback2d() {
            public void onNewSample(float var1x, float var2x) {
               var1.put(var3 + 3 * var2.value + 0, var1x);
               var1.put(var3 + 3 * var2.value + 1, var2x);
               ++var2.value;
            }
         });
      }

      public BestCandidateSampling.Quad generate(Callback2d var1) {
         BestCandidateSampling.QuadTree var2 = new BestCandidateSampling.QuadTree(-1.0F, -1.0F, 2.0F);
         Random var3 = new Random(this.seed);

         for(int var4 = 0; var4 < this.numSamples; ++var4) {
            float var5 = 0.0F;
            float var6 = 0.0F;
            float var7 = 0.0F;

            for(int var8 = 0; var8 < this.numCandidates; ++var8) {
               float var9 = var3.nextFloat() * 2.0F - 1.0F;
               float var10 = var3.nextFloat() * 2.0F - 1.0F;
               float var11 = var2.nearest(var9, var10, var7, Float.POSITIVE_INFINITY);
               if (var11 > var7) {
                  var7 = var11;
                  var5 = var9;
                  var6 = var10;
               }
            }

            var1.onNewSample(var5, var6);
            var2.insert(new Vector2f(var5, var6));
         }

         return this;
      }
   }

   public static class Disk {
      private int numSamples;
      private int numCandidates = 60;
      private long seed;

      public BestCandidateSampling.Disk seed(long var1) {
         this.seed = var1;
         return this;
      }

      public BestCandidateSampling.Disk numSamples(int var1) {
         this.numSamples = var1;
         return this;
      }

      public BestCandidateSampling.Disk numCandidates(int var1) {
         this.numCandidates = var1;
         return this;
      }

      public BestCandidateSampling.Disk generate(final float[] var1) {
         final BestCandidateSampling.IntHolder var2 = new BestCandidateSampling.IntHolder();
         return this.generate(new Callback2d() {
            public void onNewSample(float var1x, float var2x) {
               var1[2 * var2.value + 0] = var1x;
               var1[2 * var2.value + 1] = var2x;
               ++var2.value;
            }
         });
      }

      public BestCandidateSampling.Disk generate(final FloatBuffer var1) {
         final BestCandidateSampling.IntHolder var2 = new BestCandidateSampling.IntHolder();
         final int var3 = var1.position();
         return this.generate(new Callback2d() {
            public void onNewSample(float var1x, float var2x) {
               var1.put(var3 + 3 * var2.value + 0, var1x);
               var1.put(var3 + 3 * var2.value + 1, var2x);
               ++var2.value;
            }
         });
      }

      public BestCandidateSampling.Disk generate(Callback2d var1) {
         BestCandidateSampling.QuadTree var2 = new BestCandidateSampling.QuadTree(-1.0F, -1.0F, 2.0F);
         Random var3 = new Random(this.seed);

         for(int var4 = 0; var4 < this.numSamples; ++var4) {
            float var5 = 0.0F;
            float var6 = 0.0F;
            float var7 = 0.0F;

            for(int var8 = 0; var8 < this.numCandidates; ++var8) {
               float var9;
               float var10;
               do {
                  var9 = var3.nextFloat() * 2.0F - 1.0F;
                  var10 = var3.nextFloat() * 2.0F - 1.0F;
               } while(var9 * var9 + var10 * var10 > 1.0F);

               float var11 = var2.nearest(var9, var10, var7, Float.POSITIVE_INFINITY);
               if (var11 > var7) {
                  var7 = var11;
                  var5 = var9;
                  var6 = var10;
               }
            }

            var1.onNewSample(var5, var6);
            var2.insert(new Vector2f(var5, var6));
         }

         return this;
      }
   }

   private static class QuadTree {
      private static final int MAX_OBJECTS_PER_NODE = 32;
      private static final int PXNY = 0;
      private static final int NXNY = 1;
      private static final int NXPY = 2;
      private static final int PXPY = 3;
      private float minX;
      private float minY;
      private float hs;
      private ArrayList objects;
      private BestCandidateSampling.QuadTree[] children;

      QuadTree(float var1, float var2, float var3) {
         this.minX = var1;
         this.minY = var2;
         this.hs = var3 * 0.5F;
      }

      private void split() {
         this.children = new BestCandidateSampling.QuadTree[4];
         this.children[1] = new BestCandidateSampling.QuadTree(this.minX, this.minY, this.hs);
         this.children[0] = new BestCandidateSampling.QuadTree(this.minX + this.hs, this.minY, this.hs);
         this.children[2] = new BestCandidateSampling.QuadTree(this.minX, this.minY + this.hs, this.hs);
         this.children[3] = new BestCandidateSampling.QuadTree(this.minX + this.hs, this.minY + this.hs, this.hs);
      }

      private void insertIntoChild(Vector2f var1) {
         this.children[this.quadrant(var1.x, var1.y)].insert(var1);
      }

      void insert(Vector2f var1) {
         if (this.children != null) {
            this.insertIntoChild(var1);
         } else {
            if (this.objects != null && this.objects.size() == 32) {
               this.split();

               for(int var2 = 0; var2 < this.objects.size(); ++var2) {
                  this.insertIntoChild((Vector2f)this.objects.get(var2));
               }

               this.objects = null;
               this.insertIntoChild(var1);
            } else {
               if (this.objects == null) {
                  this.objects = new ArrayList(32);
               }

               this.objects.add(var1);
            }

         }
      }

      private int quadrant(float var1, float var2) {
         if (var1 < this.minX + this.hs) {
            return var2 < this.minY + this.hs ? 1 : 2;
         } else {
            return var2 < this.minY + this.hs ? 0 : 3;
         }
      }

      float nearest(float var1, float var2, float var3, float var4) {
         float var5 = var4;
         if (!(var1 < this.minX - var4) && !(var1 > this.minX + this.hs * 2.0F + var4) && !(var2 < this.minY - var4) && !(var2 > this.minY + this.hs * 2.0F + var4)) {
            if (this.children != null) {
               int var11 = this.quadrant(var1, var2);

               for(int var12 = 0; var12 < 4; ++var12) {
                  float var13 = this.children[var11].nearest(var1, var2, var3, var5);
                  var5 = Math.min(var13, var5);
                  if (var5 <= var3) {
                     return var3;
                  }

                  var11 = var11 + 1 & 3;
               }

               return var5;
            } else {
               float var6 = var4 * var4;
               float var7 = var3 * var3;

               for(int var8 = 0; this.objects != null && var8 < this.objects.size(); ++var8) {
                  Vector2f var9 = (Vector2f)this.objects.get(var8);
                  float var10 = var9.distanceSquared(var1, var2);
                  if (var10 <= var7) {
                     return var3;
                  }

                  if (var10 < var6) {
                     var6 = var10;
                  }
               }

               return Math.sqrt(var6);
            }
         } else {
            return var4;
         }
      }
   }

   public static class Sphere {
      private boolean onHemisphere;
      private int numSamples;
      private int numCandidates = 60;
      private long seed;

      public BestCandidateSampling.Sphere generate(final float[] var1) {
         final BestCandidateSampling.IntHolder var2 = new BestCandidateSampling.IntHolder();
         return this.generate(new Callback3d() {
            public void onNewSample(float var1x, float var2x, float var3) {
               var1[3 * var2.value + 0] = var1x;
               var1[3 * var2.value + 1] = var2x;
               var1[3 * var2.value + 2] = var3;
               ++var2.value;
            }
         });
      }

      public BestCandidateSampling.Sphere generate(final FloatBuffer var1) {
         final BestCandidateSampling.IntHolder var2 = new BestCandidateSampling.IntHolder();
         final int var3 = var1.position();
         return this.generate(new Callback3d() {
            public void onNewSample(float var1x, float var2x, float var3x) {
               var1.put(var3 + 3 * var2.value + 0, var1x);
               var1.put(var3 + 3 * var2.value + 1, var2x);
               var1.put(var3 + 3 * var2.value + 2, var3x);
               ++var2.value;
            }
         });
      }

      public BestCandidateSampling.Sphere seed(long var1) {
         this.seed = var1;
         return this;
      }

      public BestCandidateSampling.Sphere numSamples(int var1) {
         this.numSamples = var1;
         return this;
      }

      public BestCandidateSampling.Sphere numCandidates(int var1) {
         this.numCandidates = var1;
         return this;
      }

      public BestCandidateSampling.Sphere onHemisphere(boolean var1) {
         this.onHemisphere = var1;
         return this;
      }

      public BestCandidateSampling.Sphere generate(Callback3d var1) {
         Random var2 = new Random(this.seed);
         BestCandidateSampling.Sphere.Node var3 = new BestCandidateSampling.Sphere.Node();

         for(int var4 = 0; var4 < this.numSamples; ++var4) {
            float var5 = Float.NaN;
            float var6 = Float.NaN;
            float var7 = Float.NaN;
            float var8 = 0.0F;

            for(int var9 = 0; var9 < this.numCandidates; ++var9) {
               float var10;
               float var11;
               do {
                  var10 = var2.nextFloat() * 2.0F - 1.0F;
                  var11 = var2.nextFloat() * 2.0F - 1.0F;
               } while(var10 * var10 + var11 * var11 > 1.0F);

               float var12 = (float)Math.sqrt(1.0D - (double)(var10 * var10) - (double)(var11 * var11));
               float var13 = 2.0F * var10 * var12;
               float var14 = 2.0F * var11 * var12;
               float var15 = 1.0F - 2.0F * (var10 * var10 + var11 * var11);
               if (this.onHemisphere) {
                  var15 = Math.abs(var15);
               }

               float var16 = var3.nearest(var13, var14, var15);
               if (var16 > var8) {
                  var8 = var16;
                  var5 = var13;
                  var6 = var14;
                  var7 = var15;
               }
            }

            var1.onNewSample(var5, var6, var7);
            var3.insert(new Vector3f(var5, var6, var7));
         }

         return this;
      }

      private static final class Node {
         private static final int MAX_OBJECTS_PER_NODE = 32;
         private float v0x;
         private float v0y;
         private float v0z;
         private float v1x;
         private float v1y;
         private float v1z;
         private float v2x;
         private float v2y;
         private float v2z;
         private float cx;
         private float cy;
         private float cz;
         private float arc;
         private ArrayList objects;
         private BestCandidateSampling.Sphere.Node[] children;

         Node() {
            this.children = new BestCandidateSampling.Sphere.Node[8];
            float var1 = 1.0F;
            this.arc = 6.2831855F;
            this.children[0] = new BestCandidateSampling.Sphere.Node(-var1, 0.0F, 0.0F, 0.0F, 0.0F, var1, 0.0F, var1, 0.0F);
            this.children[1] = new BestCandidateSampling.Sphere.Node(0.0F, 0.0F, var1, var1, 0.0F, 0.0F, 0.0F, var1, 0.0F);
            this.children[2] = new BestCandidateSampling.Sphere.Node(var1, 0.0F, 0.0F, 0.0F, 0.0F, -var1, 0.0F, var1, 0.0F);
            this.children[3] = new BestCandidateSampling.Sphere.Node(0.0F, 0.0F, -var1, -var1, 0.0F, 0.0F, 0.0F, var1, 0.0F);
            this.children[4] = new BestCandidateSampling.Sphere.Node(-var1, 0.0F, 0.0F, 0.0F, -var1, 0.0F, 0.0F, 0.0F, var1);
            this.children[5] = new BestCandidateSampling.Sphere.Node(0.0F, 0.0F, var1, 0.0F, -var1, 0.0F, var1, 0.0F, 0.0F);
            this.children[6] = new BestCandidateSampling.Sphere.Node(var1, 0.0F, 0.0F, 0.0F, -var1, 0.0F, 0.0F, 0.0F, -var1);
            this.children[7] = new BestCandidateSampling.Sphere.Node(0.0F, 0.0F, -var1, 0.0F, -var1, 0.0F, -var1, 0.0F, 0.0F);
         }

         private Node(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
            this.v0x = var1;
            this.v0y = var2;
            this.v0z = var3;
            this.v1x = var4;
            this.v1y = var5;
            this.v1z = var6;
            this.v2x = var7;
            this.v2y = var8;
            this.v2z = var9;
            this.cx = (this.v0x + this.v1x + this.v2x) / 3.0F;
            this.cy = (this.v0y + this.v1y + this.v2y) / 3.0F;
            this.cz = (this.v0z + this.v1z + this.v2z) / 3.0F;
            float var10 = Math.invsqrt(this.cx * this.cx + this.cy * this.cy + this.cz * this.cz);
            this.cx *= var10;
            this.cy *= var10;
            this.cz *= var10;
            float var11 = this.greatCircleDist(this.cx, this.cy, this.cz, this.v0x, this.v0y, this.v0z);
            float var12 = this.greatCircleDist(this.cx, this.cy, this.cz, this.v1x, this.v1y, this.v1z);
            float var13 = this.greatCircleDist(this.cx, this.cy, this.cz, this.v2x, this.v2y, this.v2z);
            float var14 = Math.max(Math.max(var11, var12), var13);
            var14 *= 1.7F;
            this.arc = var14;
         }

         private void split() {
            float var1 = this.v1x + this.v2x;
            float var2 = this.v1y + this.v2y;
            float var3 = this.v1z + this.v2z;
            float var4 = Math.invsqrt(var1 * var1 + var2 * var2 + var3 * var3);
            var1 *= var4;
            var2 *= var4;
            var3 *= var4;
            float var5 = this.v0x + this.v2x;
            float var6 = this.v0y + this.v2y;
            float var7 = this.v0z + this.v2z;
            float var8 = Math.invsqrt(var5 * var5 + var6 * var6 + var7 * var7);
            var5 *= var8;
            var6 *= var8;
            var7 *= var8;
            float var9 = this.v0x + this.v1x;
            float var10 = this.v0y + this.v1y;
            float var11 = this.v0z + this.v1z;
            float var12 = Math.invsqrt(var9 * var9 + var10 * var10 + var11 * var11);
            var9 *= var12;
            var10 *= var12;
            var11 *= var12;
            this.children = new BestCandidateSampling.Sphere.Node[4];
            this.children[0] = new BestCandidateSampling.Sphere.Node(this.v0x, this.v0y, this.v0z, var9, var10, var11, var5, var6, var7);
            this.children[1] = new BestCandidateSampling.Sphere.Node(this.v1x, this.v1y, this.v1z, var1, var2, var3, var9, var10, var11);
            this.children[2] = new BestCandidateSampling.Sphere.Node(this.v2x, this.v2y, this.v2z, var5, var6, var7, var1, var2, var3);
            this.children[3] = new BestCandidateSampling.Sphere.Node(var1, var2, var3, var5, var6, var7, var9, var10, var11);
         }

         private void insertIntoChild(Vector3f var1) {
            for(int var2 = 0; var2 < this.children.length; ++var2) {
               BestCandidateSampling.Sphere.Node var3 = this.children[var2];
               if (isPointOnSphericalTriangle(var1.x, var1.y, var1.z, var3.v0x, var3.v0y, var3.v0z, var3.v1x, var3.v1y, var3.v1z, var3.v2x, var3.v2y, var3.v2z, 1.0E-6F)) {
                  var3.insert(var1);
                  return;
               }
            }

         }

         void insert(Vector3f var1) {
            if (this.children != null) {
               this.insertIntoChild(var1);
            } else {
               if (this.objects != null && this.objects.size() == 32) {
                  this.split();

                  for(int var2 = 0; var2 < 32; ++var2) {
                     this.insertIntoChild((Vector3f)this.objects.get(var2));
                  }

                  this.objects = null;
                  this.insertIntoChild(var1);
               } else {
                  if (this.objects == null) {
                     this.objects = new ArrayList(32);
                  }

                  this.objects.add(var1);
               }

            }
         }

         private static boolean isPointOnSphericalTriangle(float var0, float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
            float var13 = var6 - var3;
            float var14 = var7 - var4;
            float var15 = var8 - var5;
            float var16 = var9 - var3;
            float var17 = var10 - var4;
            float var18 = var11 - var5;
            float var19 = var1 * var18 - var2 * var17;
            float var20 = var2 * var16 - var0 * var18;
            float var21 = var0 * var17 - var1 * var16;
            float var22 = var13 * var19 + var14 * var20 + var15 * var21;
            if (var22 > -var12 && var22 < var12) {
               return false;
            } else {
               float var23 = -var3;
               float var24 = -var4;
               float var25 = -var5;
               float var26 = 1.0F / var22;
               float var27 = (var23 * var19 + var24 * var20 + var25 * var21) * var26;
               if (!(var27 < 0.0F) && !(var27 > 1.0F)) {
                  float var28 = var24 * var15 - var25 * var14;
                  float var29 = var25 * var13 - var23 * var15;
                  float var30 = var23 * var14 - var24 * var13;
                  float var31 = (var0 * var28 + var1 * var29 + var2 * var30) * var26;
                  if (!(var31 < 0.0F) && !(var27 + var31 > 1.0F)) {
                     float var32 = (var16 * var28 + var17 * var29 + var18 * var30) * var26;
                     return var32 >= var12;
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            }
         }

         private int child(float var1, float var2, float var3) {
            for(int var4 = 0; var4 < this.children.length; ++var4) {
               BestCandidateSampling.Sphere.Node var5 = this.children[var4];
               if (isPointOnSphericalTriangle(var1, var2, var3, var5.v0x, var5.v0y, var5.v0z, var5.v1x, var5.v1y, var5.v1z, var5.v2x, var5.v2y, var5.v2z, 1.0E-5F)) {
                  return var4;
               }
            }

            return 0;
         }

         private float greatCircleDist(float var1, float var2, float var3, float var4, float var5, float var6) {
            float var7 = var1 * var4 + var2 * var5 + var3 * var6;
            return (float)(-1.5707963267948966D * (double)var7 + 1.5707963267948966D);
         }

         float nearest(float var1, float var2, float var3) {
            return this.nearest(var1, var2, var3, Float.POSITIVE_INFINITY);
         }

         float nearest(float var1, float var2, float var3, float var4) {
            float var5 = this.greatCircleDist(var1, var2, var3, this.cx, this.cy, this.cz);
            if (var5 - this.arc > var4) {
               return var4;
            } else {
               float var6 = var4;
               int var7;
               if (this.children != null) {
                  var7 = this.children.length;
                  int var12 = var7 - 1;
                  int var13 = this.child(var1, var2, var3);

                  for(int var10 = 0; var10 < var7; ++var10) {
                     float var11 = this.children[var13].nearest(var1, var2, var3, var6);
                     var6 = Math.min(var11, var6);
                     var13 = var13 + 1 & var12;
                  }

                  return var6;
               } else {
                  for(var7 = 0; this.objects != null && var7 < this.objects.size(); ++var7) {
                     Vector3f var8 = (Vector3f)this.objects.get(var7);
                     float var9 = this.greatCircleDist(var8.x, var8.y, var8.z, var1, var2, var3);
                     if (var9 < var6) {
                        var6 = var9;
                     }
                  }

                  return var6;
               }
            }
         }
      }
   }

   private static final class IntHolder {
      int value;
   }
}
