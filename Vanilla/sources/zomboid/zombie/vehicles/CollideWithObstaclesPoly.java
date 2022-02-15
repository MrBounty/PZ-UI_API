package zombie.vehicles;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.debug.DebugOptions;
import zombie.debug.LineDrawer;
import zombie.iso.IsoChunk;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.network.GameServer;
import zombie.network.ServerMap;
import zombie.popman.ObjectPool;
import zombie.util.list.PZArrayUtil;

public class CollideWithObstaclesPoly {
   static final float RADIUS = 0.3F;
   private final ArrayList obstacles = new ArrayList();
   private final ArrayList nodes = new ArrayList();
   private final CollideWithObstaclesPoly.ImmutableRectF moveBounds = new CollideWithObstaclesPoly.ImmutableRectF();
   private final CollideWithObstaclesPoly.ImmutableRectF vehicleBounds = new CollideWithObstaclesPoly.ImmutableRectF();
   private static final Vector2 move = new Vector2();
   private static final Vector2 nodeNormal = new Vector2();
   private static final Vector2 edgeVec = new Vector2();
   private final ArrayList vehicles = new ArrayList();
   private Clipper clipper;
   private final ByteBuffer xyBuffer = ByteBuffer.allocateDirect(8192);
   private final CollideWithObstaclesPoly.ClosestPointOnEdge closestPointOnEdge = new CollideWithObstaclesPoly.ClosestPointOnEdge();

   void getVehiclesInRect(float var1, float var2, float var3, float var4, int var5) {
      this.vehicles.clear();
      int var6 = (int)(var1 / 10.0F);
      int var7 = (int)(var2 / 10.0F);
      int var8 = (int)Math.ceil((double)(var3 / 10.0F));
      int var9 = (int)Math.ceil((double)(var4 / 10.0F));

      for(int var10 = var7; var10 < var9; ++var10) {
         for(int var11 = var6; var11 < var8; ++var11) {
            IsoChunk var12 = GameServer.bServer ? ServerMap.instance.getChunk(var11, var10) : IsoWorld.instance.CurrentCell.getChunkForGridSquare(var11 * 10, var10 * 10, 0);
            if (var12 != null) {
               for(int var13 = 0; var13 < var12.vehicles.size(); ++var13) {
                  BaseVehicle var14 = (BaseVehicle)var12.vehicles.get(var13);
                  if (var14.getScript() != null && (int)var14.z == var5) {
                     this.vehicles.add(var14);
                  }
               }
            }
         }
      }

   }

   void getObstaclesInRect(float var1, float var2, float var3, float var4, int var5, int var6, int var7, boolean var8) {
      if (this.clipper == null) {
         this.clipper = new Clipper();
      }

      this.clipper.clear();
      this.moveBounds.init(var1 - 2.0F, var2 - 2.0F, var3 - var1 + 4.0F, var4 - var2 + 4.0F);
      int var9 = (int)(this.moveBounds.x / 10.0F);
      int var10 = (int)(this.moveBounds.y / 10.0F);
      int var11 = (int)Math.ceil((double)(this.moveBounds.right() / 10.0F));
      int var12 = (int)Math.ceil((double)(this.moveBounds.bottom() / 10.0F));
      if (Math.abs(var3 - var1) < 2.0F && Math.abs(var4 - var2) < 2.0F) {
         var9 = var5 / 10;
         var10 = var6 / 10;
         var11 = var9 + 1;
         var12 = var10 + 1;
      }

      for(int var13 = var10; var13 < var12; ++var13) {
         for(int var14 = var9; var14 < var11; ++var14) {
            IsoChunk var15 = GameServer.bServer ? ServerMap.instance.getChunk(var14, var13) : IsoWorld.instance.CurrentCell.getChunk(var14, var13);
            if (var15 != null) {
               CollideWithObstaclesPoly.ChunkDataZ var16 = var15.collision.init(var15, var7, this);
               ArrayList var17 = var8 ? var16.worldVehicleUnion : var16.worldVehicleSeparate;

               for(int var18 = 0; var18 < var17.size(); ++var18) {
                  CollideWithObstaclesPoly.CCObstacle var19 = (CollideWithObstaclesPoly.CCObstacle)var17.get(var18);
                  if (var19.bounds.intersects(this.moveBounds)) {
                     this.obstacles.add(var19);
                  }
               }

               this.nodes.addAll(var16.nodes);
            }
         }
      }

   }

   public Vector2f resolveCollision(IsoGameCharacter var1, float var2, float var3, Vector2f var4) {
      var4.set(var2, var3);
      boolean var5 = Core.bDebug && DebugOptions.instance.CollideWithObstaclesRenderObstacles.getValue();
      float var6 = var1.x;
      float var7 = var1.y;
      float var8 = var2;
      float var9 = var3;
      if (var5) {
         LineDrawer.addLine(var6, var7, (float)((int)var1.z), var2, var3, (float)((int)var1.z), 1.0F, 1.0F, 1.0F, (String)null, true);
      }

      if (var6 == var2 && var7 == var3) {
         return var4;
      } else {
         move.set(var2 - var1.x, var3 - var1.y);
         move.normalize();
         this.nodes.clear();
         this.obstacles.clear();
         this.getObstaclesInRect(Math.min(var6, var2), Math.min(var7, var3), Math.max(var6, var2), Math.max(var7, var3), (int)var1.x, (int)var1.y, (int)var1.z, true);
         this.closestPointOnEdge.edge = null;
         this.closestPointOnEdge.node = null;
         this.closestPointOnEdge.distSq = Double.MAX_VALUE;

         for(int var10 = 0; var10 < this.obstacles.size(); ++var10) {
            CollideWithObstaclesPoly.CCObstacle var11 = (CollideWithObstaclesPoly.CCObstacle)this.obstacles.get(var10);
            byte var12 = 0;
            if (var11.isPointInside(var1.x, var1.y, var12)) {
               var11.getClosestPointOnEdge(var1.x, var1.y, this.closestPointOnEdge);
            }
         }

         CollideWithObstaclesPoly.CCEdge var16 = this.closestPointOnEdge.edge;
         CollideWithObstaclesPoly.CCNode var17 = this.closestPointOnEdge.node;
         float var18;
         if (var16 != null) {
            var18 = var16.normal.dot(move);
            if (var18 >= 0.01F) {
               var16 = null;
            }
         }

         if (var17 != null && var17.getNormalAndEdgeVectors(nodeNormal, edgeVec) && nodeNormal.dot(move) + 0.05F >= nodeNormal.dot(edgeVec)) {
            var17 = null;
            var16 = null;
         }

         if (var16 == null) {
            this.closestPointOnEdge.edge = null;
            this.closestPointOnEdge.node = null;
            this.closestPointOnEdge.distSq = Double.MAX_VALUE;

            for(int var19 = 0; var19 < this.obstacles.size(); ++var19) {
               CollideWithObstaclesPoly.CCObstacle var13 = (CollideWithObstaclesPoly.CCObstacle)this.obstacles.get(var19);
               var13.lineSegmentIntersect(var6, var7, var8, var9, this.closestPointOnEdge, var5);
            }

            var16 = this.closestPointOnEdge.edge;
            var17 = this.closestPointOnEdge.node;
         }

         if (var17 != null) {
            move.set(var2 - var1.x, var3 - var1.y);
            move.normalize();
            CollideWithObstaclesPoly.CCEdge var20 = var16;
            CollideWithObstaclesPoly.CCEdge var21 = null;

            for(int var14 = 0; var14 < var17.edges.size(); ++var14) {
               CollideWithObstaclesPoly.CCEdge var15 = (CollideWithObstaclesPoly.CCEdge)var17.edges.get(var14);
               if (var15 != var16 && (var20.node1.x != var15.node1.x || var20.node1.y != var15.node1.y || var20.node2.x != var15.node2.x || var20.node2.y != var15.node2.y) && (var20.node1.x != var15.node2.x || var20.node1.y != var15.node2.y || var20.node2.x != var15.node1.x || var20.node2.y != var15.node1.y) && (!var20.hasNode(var15.node1) || !var20.hasNode(var15.node2))) {
                  var21 = var15;
               }
            }

            if (var20 != null && var21 != null) {
               CollideWithObstaclesPoly.CCNode var23;
               if (var16 == var20) {
                  var23 = var17 == var21.node1 ? var21.node2 : var21.node1;
                  edgeVec.set(var23.x - var17.x, var23.y - var17.y);
                  edgeVec.normalize();
                  if (move.dot(edgeVec) >= 0.0F) {
                     var16 = var21;
                  }
               } else if (var16 == var21) {
                  var23 = var17 == var20.node1 ? var20.node2 : var20.node1;
                  edgeVec.set(var23.x - var17.x, var23.y - var17.y);
                  edgeVec.normalize();
                  if (move.dot(edgeVec) >= 0.0F) {
                     var16 = var20;
                  }
               }
            }
         }

         if (var16 != null) {
            if (var5) {
               var18 = var16.node1.x;
               float var22 = var16.node1.y;
               float var24 = var16.node2.x;
               float var25 = var16.node2.y;
               LineDrawer.addLine(var18, var22, (float)var16.node1.z, var24, var25, (float)var16.node1.z, 0.0F, 1.0F, 1.0F, (String)null, true);
            }

            this.closestPointOnEdge.distSq = Double.MAX_VALUE;
            var16.getClosestPointOnEdge(var2, var3, this.closestPointOnEdge);
            var4.set(this.closestPointOnEdge.point.x, this.closestPointOnEdge.point.y);
         }

         return var4;
      }
   }

   boolean canStandAt(float var1, float var2, float var3, BaseVehicle var4, int var5) {
      boolean var6 = (var5 & 1) != 0;
      boolean var7 = (var5 & 2) != 0;
      float var8 = var1 - 0.3F;
      float var9 = var2 - 0.3F;
      float var10 = var1 + 0.3F;
      float var11 = var2 + 0.3F;
      this.nodes.clear();
      this.obstacles.clear();
      this.getObstaclesInRect(Math.min(var8, var10), Math.min(var9, var11), Math.max(var8, var10), Math.max(var9, var11), (int)var1, (int)var2, (int)var3, var4 == null);

      for(int var12 = 0; var12 < this.obstacles.size(); ++var12) {
         CollideWithObstaclesPoly.CCObstacle var13 = (CollideWithObstaclesPoly.CCObstacle)this.obstacles.get(var12);
         if ((var4 == null || var13.vehicle != var4) && var13.isPointInside(var1, var2, var5)) {
            return false;
         }
      }

      return true;
   }

   public boolean isNotClear(float var1, float var2, float var3, float var4, int var5, boolean var6, BaseVehicle var7, boolean var8, boolean var9) {
      float var10 = var1;
      float var11 = var2;
      float var12 = var3;
      float var13 = var4;
      var1 /= 10.0F;
      var2 /= 10.0F;
      var3 /= 10.0F;
      var4 /= 10.0F;
      double var14 = (double)Math.abs(var3 - var1);
      double var16 = (double)Math.abs(var4 - var2);
      int var18 = (int)Math.floor((double)var1);
      int var19 = (int)Math.floor((double)var2);
      int var20 = 1;
      byte var21;
      double var23;
      if (var14 == 0.0D) {
         var21 = 0;
         var23 = Double.POSITIVE_INFINITY;
      } else if (var3 > var1) {
         var21 = 1;
         var20 += (int)Math.floor((double)var3) - var18;
         var23 = (Math.floor((double)var1) + 1.0D - (double)var1) * var16;
      } else {
         var21 = -1;
         var20 += var18 - (int)Math.floor((double)var3);
         var23 = ((double)var1 - Math.floor((double)var1)) * var16;
      }

      byte var22;
      if (var16 == 0.0D) {
         var22 = 0;
         var23 -= Double.POSITIVE_INFINITY;
      } else if (var4 > var2) {
         var22 = 1;
         var20 += (int)Math.floor((double)var4) - var19;
         var23 -= (Math.floor((double)var2) + 1.0D - (double)var2) * var14;
      } else {
         var22 = -1;
         var20 += var19 - (int)Math.floor((double)var4);
         var23 -= ((double)var2 - Math.floor((double)var2)) * var14;
      }

      for(; var20 > 0; --var20) {
         IsoChunk var25 = GameServer.bServer ? ServerMap.instance.getChunk(var18, var19) : IsoWorld.instance.CurrentCell.getChunk(var18, var19);
         if (var25 != null) {
            if (var6) {
               LineDrawer.addRect((float)(var18 * 10), (float)(var19 * 10), (float)var5, 10.0F, 10.0F, 1.0F, 1.0F, 1.0F);
            }

            CollideWithObstaclesPoly.ChunkDataZ var26 = var25.collision.init(var25, var5, this);
            ArrayList var27 = var7 == null ? var26.worldVehicleUnion : var26.worldVehicleSeparate;

            for(int var28 = 0; var28 < var27.size(); ++var28) {
               CollideWithObstaclesPoly.CCObstacle var29 = (CollideWithObstaclesPoly.CCObstacle)var27.get(var28);
               if ((var7 == null || var29.vehicle != var7) && var29.lineSegmentIntersects(var10, var11, var12, var13, var6)) {
                  return true;
               }
            }
         }

         if (var23 > 0.0D) {
            var19 += var22;
            var23 -= var14;
         } else {
            var18 += var21;
            var23 += var16;
         }
      }

      return false;
   }

   private void vehicleMoved(PolygonalMap2.VehiclePoly var1) {
      byte var2 = 2;
      int var3 = (int)Math.min(var1.x1, Math.min(var1.x2, Math.min(var1.x3, var1.x4)));
      int var4 = (int)Math.min(var1.y1, Math.min(var1.y2, Math.min(var1.y3, var1.y4)));
      int var5 = (int)Math.max(var1.x1, Math.max(var1.x2, Math.max(var1.x3, var1.x4)));
      int var6 = (int)Math.max(var1.y1, Math.max(var1.y2, Math.max(var1.y3, var1.y4)));
      int var7 = (int)var1.z;
      int var8 = (var3 - var2) / 10;
      int var9 = (var4 - var2) / 10;
      int var10 = (int)Math.ceil((double)(((float)(var5 + var2) - 1.0F) / 10.0F));
      int var11 = (int)Math.ceil((double)(((float)(var6 + var2) - 1.0F) / 10.0F));

      for(int var12 = var9; var12 <= var11; ++var12) {
         for(int var13 = var8; var13 <= var10; ++var13) {
            IsoChunk var14 = IsoWorld.instance.CurrentCell.getChunk(var13, var12);
            if (var14 != null && var14.collision.data[var7] != null) {
               CollideWithObstaclesPoly.ChunkDataZ var15 = var14.collision.data[var7];
               var14.collision.data[var7] = null;
               var15.clear();
               CollideWithObstaclesPoly.ChunkDataZ.pool.release((Object)var15);
            }
         }
      }

   }

   public void vehicleMoved(PolygonalMap2.VehiclePoly var1, PolygonalMap2.VehiclePoly var2) {
      this.vehicleMoved(var1);
      this.vehicleMoved(var2);
   }

   public void render() {
      boolean var1 = Core.bDebug && DebugOptions.instance.CollideWithObstaclesRenderObstacles.getValue();
      if (var1) {
         IsoPlayer var2 = IsoPlayer.getInstance();
         if (var2 == null) {
            return;
         }

         this.nodes.clear();
         this.obstacles.clear();
         this.getObstaclesInRect(var2.x, var2.y, var2.x, var2.y, (int)var2.x, (int)var2.y, (int)var2.z, true);
         Iterator var3;
         if (DebugOptions.instance.CollideWithObstaclesRenderNormals.getValue()) {
            var3 = this.nodes.iterator();

            while(var3.hasNext()) {
               CollideWithObstaclesPoly.CCNode var4 = (CollideWithObstaclesPoly.CCNode)var3.next();
               if (var4.getNormalAndEdgeVectors(nodeNormal, edgeVec)) {
                  LineDrawer.addLine(var4.x, var4.y, (float)var4.z, var4.x + nodeNormal.x, var4.y + nodeNormal.y, (float)var4.z, 0.0F, 0.0F, 1.0F, (String)null, true);
               }
            }
         }

         var3 = this.obstacles.iterator();

         while(var3.hasNext()) {
            CollideWithObstaclesPoly.CCObstacle var5 = (CollideWithObstaclesPoly.CCObstacle)var3.next();
            var5.render();
         }
      }

   }

   private static final class ImmutableRectF {
      private float x;
      private float y;
      private float w;
      private float h;
      static final ArrayDeque pool = new ArrayDeque();

      CollideWithObstaclesPoly.ImmutableRectF init(float var1, float var2, float var3, float var4) {
         this.x = var1;
         this.y = var2;
         this.w = var3;
         this.h = var4;
         return this;
      }

      float left() {
         return this.x;
      }

      float top() {
         return this.y;
      }

      float right() {
         return this.x + this.w;
      }

      float bottom() {
         return this.y + this.h;
      }

      float width() {
         return this.w;
      }

      float height() {
         return this.h;
      }

      boolean containsPoint(float var1, float var2) {
         return var1 >= this.left() && var1 < this.right() && var2 >= this.top() && var2 < this.bottom();
      }

      boolean intersects(CollideWithObstaclesPoly.ImmutableRectF var1) {
         return this.left() < var1.right() && this.right() > var1.left() && this.top() < var1.bottom() && this.bottom() > var1.top();
      }

      static CollideWithObstaclesPoly.ImmutableRectF alloc() {
         return pool.isEmpty() ? new CollideWithObstaclesPoly.ImmutableRectF() : (CollideWithObstaclesPoly.ImmutableRectF)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class ClosestPointOnEdge {
      CollideWithObstaclesPoly.CCEdge edge;
      CollideWithObstaclesPoly.CCNode node;
      final Vector2f point = new Vector2f();
      double distSq;
   }

   public static final class ChunkData {
      final CollideWithObstaclesPoly.ChunkDataZ[] data = new CollideWithObstaclesPoly.ChunkDataZ[8];
      private boolean bClear = false;

      public CollideWithObstaclesPoly.ChunkDataZ init(IsoChunk var1, int var2, CollideWithObstaclesPoly var3) {
         assert Thread.currentThread() == GameWindow.GameThread;

         if (this.bClear) {
            this.bClear = false;
            this.clearInner();
         }

         if (this.data[var2] == null) {
            this.data[var2] = (CollideWithObstaclesPoly.ChunkDataZ)CollideWithObstaclesPoly.ChunkDataZ.pool.alloc();
            this.data[var2].init(var1, var2, var3);
         }

         return this.data[var2];
      }

      private void clearInner() {
         PZArrayUtil.forEach((Object[])this.data, (var0) -> {
            if (var0 != null) {
               var0.clear();
               CollideWithObstaclesPoly.ChunkDataZ.pool.release((Object)var0);
            }

         });
         Arrays.fill(this.data, (Object)null);
      }

      public void clear() {
         this.bClear = true;
      }
   }

   public static final class ChunkDataZ {
      public final ArrayList worldVehicleUnion = new ArrayList();
      public final ArrayList worldVehicleSeparate = new ArrayList();
      public final ArrayList nodes = new ArrayList();
      public int z;
      public static final ObjectPool pool = new ObjectPool(CollideWithObstaclesPoly.ChunkDataZ::new);

      public void init(IsoChunk var1, int var2, CollideWithObstaclesPoly var3) {
         this.z = var2;
         Clipper var4 = var3.clipper;
         var4.clear();
         float var5 = 0.19800001F;
         int var6 = var1.wx * 10;
         int var7 = var1.wy * 10;

         int var9;
         for(int var8 = var7 - 2; var8 < var7 + 10 + 2; ++var8) {
            for(var9 = var6 - 2; var9 < var6 + 10 + 2; ++var9) {
               IsoGridSquare var10 = IsoWorld.instance.CurrentCell.getGridSquare(var9, var8, var2);
               if (var10 != null && !var10.getObjects().isEmpty()) {
                  if (var10.isSolid() || var10.isSolidTrans() && !var10.isAdjacentToWindow()) {
                     var4.addAABBBevel((float)var9 - 0.3F, (float)var8 - 0.3F, (float)var9 + 1.0F + 0.3F, (float)var8 + 1.0F + 0.3F, var5);
                  }

                  boolean var11 = var10.Is(IsoFlagType.collideW) || var10.hasBlockedDoor(false) || var10.HasStairsNorth();
                  if (var10.Is(IsoFlagType.windowW) || var10.Is(IsoFlagType.WindowW)) {
                     var11 = true;
                  }

                  boolean var12;
                  boolean var13;
                  if (var11) {
                     if (!this.isCollideW(var9, var8 - 1, var2)) {
                     }

                     var12 = false;
                     if (!this.isCollideW(var9, var8 + 1, var2)) {
                     }

                     var13 = false;
                     var4.addAABBBevel((float)var9 - 0.3F, (float)var8 - (var12 ? 0.0F : 0.3F), (float)var9 + 0.3F, (float)var8 + 1.0F + (var13 ? 0.0F : 0.3F), var5);
                  }

                  var12 = var10.Is(IsoFlagType.collideN) || var10.hasBlockedDoor(true) || var10.HasStairsWest();
                  if (var10.Is(IsoFlagType.windowN) || var10.Is(IsoFlagType.WindowN)) {
                     var12 = true;
                  }

                  if (var12) {
                     if (!this.isCollideN(var9 - 1, var8, var2)) {
                     }

                     var13 = false;
                     if (!this.isCollideN(var9 + 1, var8, var2)) {
                     }

                     boolean var14 = false;
                     var4.addAABBBevel((float)var9 - (var13 ? 0.0F : 0.3F), (float)var8 - 0.3F, (float)var9 + 1.0F + (var14 ? 0.0F : 0.3F), (float)var8 + 0.3F, var5);
                  }

                  float var15;
                  IsoGridSquare var34;
                  IsoGridSquare var36;
                  if (var10.HasStairsNorth()) {
                     var34 = IsoWorld.instance.CurrentCell.getGridSquare(var9 + 1, var8, var2);
                     if (var34 != null) {
                        var4.addAABBBevel((float)(var9 + 1) - 0.3F, (float)var8 - 0.3F, (float)(var9 + 1) + 0.3F, (float)var8 + 1.0F + 0.3F, var5);
                     }

                     if (var10.Has(IsoObjectType.stairsTN)) {
                        var36 = IsoWorld.instance.CurrentCell.getGridSquare(var9, var8, var2 - 1);
                        if (var36 == null || !var36.Has(IsoObjectType.stairsTN)) {
                           var4.addAABBBevel((float)var9 - 0.3F, (float)var8 - 0.3F, (float)var9 + 1.0F + 0.3F, (float)var8 + 0.3F, var5);
                           var15 = 0.1F;
                           var4.clipAABB((float)var9 + 0.3F, (float)var8 - var15, (float)var9 + 1.0F - 0.3F, (float)var8 + 0.3F);
                        }
                     }
                  }

                  if (var10.HasStairsWest()) {
                     var34 = IsoWorld.instance.CurrentCell.getGridSquare(var9, var8 + 1, var2);
                     if (var34 != null) {
                        var4.addAABBBevel((float)var9 - 0.3F, (float)(var8 + 1) - 0.3F, (float)var9 + 1.0F + 0.3F, (float)(var8 + 1) + 0.3F, var5);
                     }

                     if (var10.Has(IsoObjectType.stairsTW)) {
                        var36 = IsoWorld.instance.CurrentCell.getGridSquare(var9, var8, var2 - 1);
                        if (var36 == null || !var36.Has(IsoObjectType.stairsTW)) {
                           var4.addAABBBevel((float)var9 - 0.3F, (float)var8 - 0.3F, (float)var9 + 0.3F, (float)var8 + 1.0F + 0.3F, var5);
                           var15 = 0.1F;
                           var4.clipAABB((float)var9 - var15, (float)var8 + 0.3F, (float)var9 + 0.3F, (float)var8 + 1.0F - 0.3F);
                        }
                     }
                  }
               }
            }
         }

         ByteBuffer var30 = var3.xyBuffer;

         assert this.worldVehicleSeparate.isEmpty();

         this.clipperToObstacles(var4, var30, this.worldVehicleSeparate);
         var9 = var1.wx * 10;
         int var31 = var1.wy * 10;
         int var32 = var9 + 10;
         int var33 = var31 + 10;
         var9 -= 2;
         var31 -= 2;
         var32 += 2;
         var33 += 2;
         CollideWithObstaclesPoly.ImmutableRectF var35 = var3.moveBounds.init((float)var9, (float)var31, (float)(var32 - var9), (float)(var33 - var31));
         var3.getVehiclesInRect((float)(var9 - 5), (float)(var31 - 5), (float)(var32 + 5), (float)(var33 + 5), var2);

         for(int var37 = 0; var37 < var3.vehicles.size(); ++var37) {
            BaseVehicle var38 = (BaseVehicle)var3.vehicles.get(var37);
            PolygonalMap2.VehiclePoly var16 = var38.getPolyPlusRadius();
            float var17 = Math.min(var16.x1, Math.min(var16.x2, Math.min(var16.x3, var16.x4)));
            float var18 = Math.min(var16.y1, Math.min(var16.y2, Math.min(var16.y3, var16.y4)));
            float var19 = Math.max(var16.x1, Math.max(var16.x2, Math.max(var16.x3, var16.x4)));
            float var20 = Math.max(var16.y1, Math.max(var16.y2, Math.max(var16.y3, var16.y4)));
            var3.vehicleBounds.init(var17, var18, var19 - var17, var20 - var18);
            if (var35.intersects(var3.vehicleBounds)) {
               var4.addPolygon(var16.x1, var16.y1, var16.x4, var16.y4, var16.x3, var16.y3, var16.x2, var16.y2);
               CollideWithObstaclesPoly.CCNode var21 = CollideWithObstaclesPoly.CCNode.alloc().init(var16.x1, var16.y1, var2);
               CollideWithObstaclesPoly.CCNode var22 = CollideWithObstaclesPoly.CCNode.alloc().init(var16.x2, var16.y2, var2);
               CollideWithObstaclesPoly.CCNode var23 = CollideWithObstaclesPoly.CCNode.alloc().init(var16.x3, var16.y3, var2);
               CollideWithObstaclesPoly.CCNode var24 = CollideWithObstaclesPoly.CCNode.alloc().init(var16.x4, var16.y4, var2);
               CollideWithObstaclesPoly.CCObstacle var25 = CollideWithObstaclesPoly.CCObstacle.alloc().init();
               var25.vehicle = var38;
               CollideWithObstaclesPoly.CCEdge var26 = CollideWithObstaclesPoly.CCEdge.alloc().init(var21, var22, var25);
               CollideWithObstaclesPoly.CCEdge var27 = CollideWithObstaclesPoly.CCEdge.alloc().init(var22, var23, var25);
               CollideWithObstaclesPoly.CCEdge var28 = CollideWithObstaclesPoly.CCEdge.alloc().init(var23, var24, var25);
               CollideWithObstaclesPoly.CCEdge var29 = CollideWithObstaclesPoly.CCEdge.alloc().init(var24, var21, var25);
               var25.outer.add(var26);
               var25.outer.add(var27);
               var25.outer.add(var28);
               var25.outer.add(var29);
               var25.calcBounds();
               this.worldVehicleSeparate.add(var25);
               this.nodes.add(var21);
               this.nodes.add(var22);
               this.nodes.add(var23);
               this.nodes.add(var24);
            }
         }

         assert this.worldVehicleUnion.isEmpty();

         this.clipperToObstacles(var4, var30, this.worldVehicleUnion);
      }

      private void getEdgesFromBuffer(ByteBuffer var1, CollideWithObstaclesPoly.CCObstacle var2, boolean var3) {
         short var4 = var1.getShort();
         if (var4 < 3) {
            var1.position(var1.position() + var4 * 4 * 2);
         } else {
            CollideWithObstaclesPoly.CCEdgeRing var5 = var2.outer;
            if (!var3) {
               var5 = (CollideWithObstaclesPoly.CCEdgeRing)CollideWithObstaclesPoly.CCEdgeRing.pool.alloc();
               var5.clear();
               var2.inner.add(var5);
            }

            int var6 = this.nodes.size();

            int var7;
            for(var7 = 0; var7 < var4; ++var7) {
               float var8 = var1.getFloat();
               float var9 = var1.getFloat();
               CollideWithObstaclesPoly.CCNode var10 = CollideWithObstaclesPoly.CCNode.alloc().init(var8, var9, this.z);
               this.nodes.add(var6, var10);
            }

            CollideWithObstaclesPoly.CCNode var12;
            for(var7 = var6; var7 < this.nodes.size() - 1; ++var7) {
               var12 = (CollideWithObstaclesPoly.CCNode)this.nodes.get(var7);
               CollideWithObstaclesPoly.CCNode var13 = (CollideWithObstaclesPoly.CCNode)this.nodes.get(var7 + 1);
               CollideWithObstaclesPoly.CCEdge var14 = CollideWithObstaclesPoly.CCEdge.alloc().init(var12, var13, var2);
               var5.add(var14);
            }

            CollideWithObstaclesPoly.CCNode var11 = (CollideWithObstaclesPoly.CCNode)this.nodes.get(this.nodes.size() - 1);
            var12 = (CollideWithObstaclesPoly.CCNode)this.nodes.get(var6);
            var5.add(CollideWithObstaclesPoly.CCEdge.alloc().init(var11, var12, var2));
         }
      }

      private void clipperToObstacles(Clipper var1, ByteBuffer var2, ArrayList var3) {
         int var4 = var1.generatePolygons();

         for(int var5 = 0; var5 < var4; ++var5) {
            var2.clear();
            var1.getPolygon(var5, var2);
            CollideWithObstaclesPoly.CCObstacle var6 = CollideWithObstaclesPoly.CCObstacle.alloc().init();
            this.getEdgesFromBuffer(var2, var6, true);
            short var7 = var2.getShort();

            for(int var8 = 0; var8 < var7; ++var8) {
               this.getEdgesFromBuffer(var2, var6, false);
            }

            var6.calcBounds();
            var3.add(var6);
         }

      }

      boolean isCollideW(int var1, int var2, int var3) {
         IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
         return var4 != null && (var4.Is(IsoFlagType.collideW) || var4.hasBlockedDoor(false) || var4.HasStairsNorth());
      }

      boolean isCollideN(int var1, int var2, int var3) {
         IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
         return var4 != null && (var4.Is(IsoFlagType.collideN) || var4.hasBlockedDoor(true) || var4.HasStairsWest());
      }

      boolean isOpenDoorAt(int var1, int var2, int var3, boolean var4) {
         IsoGridSquare var5 = IsoWorld.instance.CurrentCell.getGridSquare(var1, var2, var3);
         return var5 != null && var5.getDoor(var4) != null && !var5.hasBlockedDoor(var4);
      }

      public void clear() {
         CollideWithObstaclesPoly.CCNode.releaseAll(this.nodes);
         this.nodes.clear();
         CollideWithObstaclesPoly.CCObstacle.releaseAll(this.worldVehicleUnion);
         this.worldVehicleUnion.clear();
         CollideWithObstaclesPoly.CCObstacle.releaseAll(this.worldVehicleSeparate);
         this.worldVehicleSeparate.clear();
      }
   }

   private static final class CCObstacle {
      final CollideWithObstaclesPoly.CCEdgeRing outer = new CollideWithObstaclesPoly.CCEdgeRing();
      final ArrayList inner = new ArrayList();
      BaseVehicle vehicle = null;
      CollideWithObstaclesPoly.ImmutableRectF bounds;
      static final ObjectPool pool = new ObjectPool(CollideWithObstaclesPoly.CCObstacle::new) {
         public void release(CollideWithObstaclesPoly.CCObstacle var1) {
            CollideWithObstaclesPoly.CCEdge.releaseAll(var1.outer);
            CollideWithObstaclesPoly.CCEdgeRing.releaseAll(var1.inner);
            var1.outer.clear();
            var1.inner.clear();
            var1.vehicle = null;
            super.release((Object)var1);
         }
      };

      CollideWithObstaclesPoly.CCObstacle init() {
         this.outer.clear();
         this.inner.clear();
         this.vehicle = null;
         return this;
      }

      boolean isPointInside(float var1, float var2, int var3) {
         if (this.outer.isPointInPolygon_WindingNumber(var1, var2, var3) != CollideWithObstaclesPoly.EdgeRingHit.Inside) {
            return false;
         } else if (this.inner.isEmpty()) {
            return true;
         } else {
            for(int var4 = 0; var4 < this.inner.size(); ++var4) {
               CollideWithObstaclesPoly.CCEdgeRing var5 = (CollideWithObstaclesPoly.CCEdgeRing)this.inner.get(var4);
               if (var5.isPointInPolygon_WindingNumber(var1, var2, var3) != CollideWithObstaclesPoly.EdgeRingHit.Outside) {
                  return false;
               }
            }

            return true;
         }
      }

      boolean lineSegmentIntersects(float var1, float var2, float var3, float var4, boolean var5) {
         if (this.outer.lineSegmentIntersects(var1, var2, var3, var4, var5, true)) {
            return true;
         } else {
            for(int var6 = 0; var6 < this.inner.size(); ++var6) {
               CollideWithObstaclesPoly.CCEdgeRing var7 = (CollideWithObstaclesPoly.CCEdgeRing)this.inner.get(var6);
               if (var7.lineSegmentIntersects(var1, var2, var3, var4, var5, false)) {
                  return true;
               }
            }

            return false;
         }
      }

      void lineSegmentIntersect(float var1, float var2, float var3, float var4, CollideWithObstaclesPoly.ClosestPointOnEdge var5, boolean var6) {
         this.outer.lineSegmentIntersect(var1, var2, var3, var4, var5, var6);

         for(int var7 = 0; var7 < this.inner.size(); ++var7) {
            CollideWithObstaclesPoly.CCEdgeRing var8 = (CollideWithObstaclesPoly.CCEdgeRing)this.inner.get(var7);
            var8.lineSegmentIntersect(var1, var2, var3, var4, var5, var6);
         }

      }

      void getClosestPointOnEdge(float var1, float var2, CollideWithObstaclesPoly.ClosestPointOnEdge var3) {
         this.outer.getClosestPointOnEdge(var1, var2, var3);

         for(int var4 = 0; var4 < this.inner.size(); ++var4) {
            CollideWithObstaclesPoly.CCEdgeRing var5 = (CollideWithObstaclesPoly.CCEdgeRing)this.inner.get(var4);
            var5.getClosestPointOnEdge(var1, var2, var3);
         }

      }

      void calcBounds() {
         float var1 = Float.MAX_VALUE;
         float var2 = Float.MAX_VALUE;
         float var3 = Float.MIN_VALUE;
         float var4 = Float.MIN_VALUE;

         for(int var5 = 0; var5 < this.outer.size(); ++var5) {
            CollideWithObstaclesPoly.CCEdge var6 = (CollideWithObstaclesPoly.CCEdge)this.outer.get(var5);
            var1 = Math.min(var1, var6.node1.x);
            var2 = Math.min(var2, var6.node1.y);
            var3 = Math.max(var3, var6.node1.x);
            var4 = Math.max(var4, var6.node1.y);
         }

         if (this.bounds != null) {
            this.bounds.release();
         }

         float var7 = 0.01F;
         this.bounds = CollideWithObstaclesPoly.ImmutableRectF.alloc().init(var1 - var7, var2 - var7, var3 - var1 + var7 * 2.0F, var4 - var2 + var7 * 2.0F);
      }

      void render() {
         this.outer.render(true);

         for(int var1 = 0; var1 < this.inner.size(); ++var1) {
            ((CollideWithObstaclesPoly.CCEdgeRing)this.inner.get(var1)).render(false);
         }

      }

      static CollideWithObstaclesPoly.CCObstacle alloc() {
         return (CollideWithObstaclesPoly.CCObstacle)pool.alloc();
      }

      void release() {
         pool.release((Object)this);
      }

      static void releaseAll(ArrayList var0) {
         pool.releaseAll(var0);
      }
   }

   private static final class CCEdge {
      CollideWithObstaclesPoly.CCNode node1;
      CollideWithObstaclesPoly.CCNode node2;
      CollideWithObstaclesPoly.CCObstacle obstacle;
      final Vector2 normal = new Vector2();
      static final ObjectPool pool = new ObjectPool(CollideWithObstaclesPoly.CCEdge::new);

      CollideWithObstaclesPoly.CCEdge init(CollideWithObstaclesPoly.CCNode var1, CollideWithObstaclesPoly.CCNode var2, CollideWithObstaclesPoly.CCObstacle var3) {
         if (var1.x == var2.x && var1.y == var2.y) {
            boolean var4 = false;
         }

         this.node1 = var1;
         this.node2 = var2;
         var1.edges.add(this);
         var2.edges.add(this);
         this.obstacle = var3;
         this.normal.set(var2.x - var1.x, var2.y - var1.y);
         this.normal.normalize();
         this.normal.rotate((float)Math.toRadians(90.0D));
         return this;
      }

      boolean hasNode(CollideWithObstaclesPoly.CCNode var1) {
         return var1 == this.node1 || var1 == this.node2;
      }

      void getClosestPointOnEdge(float var1, float var2, CollideWithObstaclesPoly.ClosestPointOnEdge var3) {
         float var4 = this.node1.x;
         float var5 = this.node1.y;
         float var6 = this.node2.x;
         float var7 = this.node2.y;
         double var8 = (double)((var1 - var4) * (var6 - var4) + (var2 - var5) * (var7 - var5)) / (Math.pow((double)(var6 - var4), 2.0D) + Math.pow((double)(var7 - var5), 2.0D));
         double var10 = (double)var4 + var8 * (double)(var6 - var4);
         double var12 = (double)var5 + var8 * (double)(var7 - var5);
         double var14 = 0.001D;
         CollideWithObstaclesPoly.CCNode var16 = null;
         if (var8 <= 0.0D + var14) {
            var10 = (double)var4;
            var12 = (double)var5;
            var16 = this.node1;
         } else if (var8 >= 1.0D - var14) {
            var10 = (double)var6;
            var12 = (double)var7;
            var16 = this.node2;
         }

         double var17 = ((double)var1 - var10) * ((double)var1 - var10) + ((double)var2 - var12) * ((double)var2 - var12);
         if (var17 < var3.distSq) {
            var3.point.set((float)var10, (float)var12);
            var3.distSq = var17;
            var3.edge = this;
            var3.node = var16;
         }

      }

      boolean isPointOn(float var1, float var2) {
         float var3 = this.node1.x;
         float var4 = this.node1.y;
         float var5 = this.node2.x;
         float var6 = this.node2.y;
         double var7 = (double)((var1 - var3) * (var5 - var3) + (var2 - var4) * (var6 - var4)) / (Math.pow((double)(var5 - var3), 2.0D) + Math.pow((double)(var6 - var4), 2.0D));
         double var9 = (double)var3 + var7 * (double)(var5 - var3);
         double var11 = (double)var4 + var7 * (double)(var6 - var4);
         if (var7 <= 0.0D) {
            var9 = (double)var3;
            var11 = (double)var4;
         } else if (var7 >= 1.0D) {
            var9 = (double)var5;
            var11 = (double)var6;
         }

         double var13 = ((double)var1 - var9) * ((double)var1 - var9) + ((double)var2 - var11) * ((double)var2 - var11);
         return var13 < 1.0E-6D;
      }

      static CollideWithObstaclesPoly.CCEdge alloc() {
         return (CollideWithObstaclesPoly.CCEdge)pool.alloc();
      }

      void release() {
         pool.release((Object)this);
      }

      static void releaseAll(ArrayList var0) {
         pool.releaseAll(var0);
      }
   }

   private static final class CCNode {
      float x;
      float y;
      int z;
      final ArrayList edges = new ArrayList();
      static final ObjectPool pool = new ObjectPool(CollideWithObstaclesPoly.CCNode::new);

      CollideWithObstaclesPoly.CCNode init(float var1, float var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.edges.clear();
         return this;
      }

      CollideWithObstaclesPoly.CCNode setXY(float var1, float var2) {
         this.x = var1;
         this.y = var2;
         return this;
      }

      boolean getNormalAndEdgeVectors(Vector2 var1, Vector2 var2) {
         CollideWithObstaclesPoly.CCEdge var3 = null;
         CollideWithObstaclesPoly.CCEdge var4 = null;

         for(int var5 = 0; var5 < this.edges.size(); ++var5) {
            CollideWithObstaclesPoly.CCEdge var6 = (CollideWithObstaclesPoly.CCEdge)this.edges.get(var5);
            if (var3 == null) {
               var3 = var6;
            } else if (!var3.hasNode(var6.node1) || !var3.hasNode(var6.node2)) {
               var4 = var6;
            }
         }

         if (var3 != null && var4 != null) {
            float var7 = var3.normal.x + var4.normal.x;
            float var8 = var3.normal.y + var4.normal.y;
            var1.set(var7, var8);
            var1.normalize();
            if (var3.node1 == this) {
               var2.set(var3.node2.x - var3.node1.x, var3.node2.y - var3.node1.y);
            } else {
               var2.set(var3.node1.x - var3.node2.x, var3.node1.y - var3.node2.y);
            }

            var2.normalize();
            return true;
         } else {
            return false;
         }
      }

      static CollideWithObstaclesPoly.CCNode alloc() {
         return (CollideWithObstaclesPoly.CCNode)pool.alloc();
      }

      void release() {
         pool.release((Object)this);
      }

      static void releaseAll(ArrayList var0) {
         pool.releaseAll(var0);
      }
   }

   private static final class CCEdgeRing extends ArrayList {
      static final ObjectPool pool = new ObjectPool(CollideWithObstaclesPoly.CCEdgeRing::new) {
         public void release(CollideWithObstaclesPoly.CCEdgeRing var1) {
            CollideWithObstaclesPoly.CCEdge.releaseAll(var1);
            this.clear();
            super.release((Object)var1);
         }
      };

      float isLeft(float var1, float var2, float var3, float var4, float var5, float var6) {
         return (var3 - var1) * (var6 - var2) - (var5 - var1) * (var4 - var2);
      }

      CollideWithObstaclesPoly.EdgeRingHit isPointInPolygon_WindingNumber(float var1, float var2, int var3) {
         int var4 = 0;

         for(int var5 = 0; var5 < this.size(); ++var5) {
            CollideWithObstaclesPoly.CCEdge var6 = (CollideWithObstaclesPoly.CCEdge)this.get(var5);
            if ((var3 & 16) != 0 && var6.isPointOn(var1, var2)) {
               return CollideWithObstaclesPoly.EdgeRingHit.OnEdge;
            }

            if (var6.node1.y <= var2) {
               if (var6.node2.y > var2 && this.isLeft(var6.node1.x, var6.node1.y, var6.node2.x, var6.node2.y, var1, var2) > 0.0F) {
                  ++var4;
               }
            } else if (var6.node2.y <= var2 && this.isLeft(var6.node1.x, var6.node1.y, var6.node2.x, var6.node2.y, var1, var2) < 0.0F) {
               --var4;
            }
         }

         return var4 == 0 ? CollideWithObstaclesPoly.EdgeRingHit.Outside : CollideWithObstaclesPoly.EdgeRingHit.Inside;
      }

      boolean lineSegmentIntersects(float var1, float var2, float var3, float var4, boolean var5, boolean var6) {
         CollideWithObstaclesPoly.move.set(var3 - var1, var4 - var2);
         float var7 = CollideWithObstaclesPoly.move.getLength();
         CollideWithObstaclesPoly.move.normalize();
         float var8 = CollideWithObstaclesPoly.move.x;
         float var9 = CollideWithObstaclesPoly.move.y;

         for(int var10 = 0; var10 < this.size(); ++var10) {
            CollideWithObstaclesPoly.CCEdge var11 = (CollideWithObstaclesPoly.CCEdge)this.get(var10);
            if (!var11.isPointOn(var1, var2) && !var11.isPointOn(var3, var4)) {
               float var12 = var11.normal.dot(CollideWithObstaclesPoly.move);
               if (!(var12 >= 0.01F)) {
                  float var13 = var11.node1.x;
                  float var14 = var11.node1.y;
                  float var15 = var11.node2.x;
                  float var16 = var11.node2.y;
                  float var17 = var1 - var13;
                  float var18 = var2 - var14;
                  float var19 = var15 - var13;
                  float var20 = var16 - var14;
                  float var21 = 1.0F / (var20 * var8 - var19 * var9);
                  float var22 = (var19 * var18 - var20 * var17) * var21;
                  if (var22 >= 0.0F && var22 <= var7) {
                     float var23 = (var18 * var8 - var17 * var9) * var21;
                     if (var23 >= 0.0F && var23 <= 1.0F) {
                        float var24 = var1 + var22 * var8;
                        float var25 = var2 + var22 * var9;
                        if (var5) {
                           this.render(var6);
                           LineDrawer.addRect(var24 - 0.05F, var25 - 0.05F, (float)var11.node1.z, 0.1F, 0.1F, 1.0F, 1.0F, 1.0F);
                        }

                        return true;
                     }
                  }
               }
            }
         }

         if (this.isPointInPolygon_WindingNumber((var1 + var3) / 2.0F, (var2 + var4) / 2.0F, 0) != CollideWithObstaclesPoly.EdgeRingHit.Outside) {
            return true;
         } else {
            return false;
         }
      }

      void lineSegmentIntersect(float var1, float var2, float var3, float var4, CollideWithObstaclesPoly.ClosestPointOnEdge var5, boolean var6) {
         CollideWithObstaclesPoly.move.set(var3 - var1, var4 - var2).normalize();

         for(int var7 = 0; var7 < this.size(); ++var7) {
            CollideWithObstaclesPoly.CCEdge var8 = (CollideWithObstaclesPoly.CCEdge)this.get(var7);
            float var9 = var8.normal.dot(CollideWithObstaclesPoly.move);
            if (!(var9 >= 0.0F)) {
               float var10 = var8.node1.x;
               float var11 = var8.node1.y;
               float var12 = var8.node2.x;
               float var13 = var8.node2.y;
               float var14 = var10 + 0.5F * (var12 - var10);
               float var15 = var11 + 0.5F * (var13 - var11);
               if (var6 && DebugOptions.instance.CollideWithObstaclesRenderNormals.getValue()) {
                  LineDrawer.addLine(var14, var15, (float)var8.node1.z, var14 + var8.normal.x, var15 + var8.normal.y, (float)var8.node1.z, 0.0F, 0.0F, 1.0F, (String)null, true);
               }

               double var16 = (double)((var13 - var11) * (var3 - var1) - (var12 - var10) * (var4 - var2));
               if (var16 != 0.0D) {
                  double var18 = (double)((var12 - var10) * (var2 - var11) - (var13 - var11) * (var1 - var10)) / var16;
                  double var20 = (double)((var3 - var1) * (var2 - var11) - (var4 - var2) * (var1 - var10)) / var16;
                  if (var18 >= 0.0D && var18 <= 1.0D && var20 >= 0.0D && var20 <= 1.0D) {
                     if (var20 < 0.01D || var20 > 0.99D) {
                        CollideWithObstaclesPoly.CCNode var22 = var20 < 0.01D ? var8.node1 : var8.node2;
                        double var23 = (double)IsoUtils.DistanceToSquared(var1, var2, var22.x, var22.y);
                        if (var23 >= var5.distSq) {
                           continue;
                        }

                        if (var22.getNormalAndEdgeVectors(CollideWithObstaclesPoly.nodeNormal, CollideWithObstaclesPoly.edgeVec)) {
                           if (!(CollideWithObstaclesPoly.nodeNormal.dot(CollideWithObstaclesPoly.move) + 0.05F >= CollideWithObstaclesPoly.nodeNormal.dot(CollideWithObstaclesPoly.edgeVec))) {
                              var5.edge = var8;
                              var5.node = var22;
                              var5.distSq = var23;
                           }
                           continue;
                        }
                     }

                     float var26 = (float)((double)var1 + var18 * (double)(var3 - var1));
                     float var27 = (float)((double)var2 + var18 * (double)(var4 - var2));
                     double var24 = (double)IsoUtils.DistanceToSquared(var1, var2, var26, var27);
                     if (var24 < var5.distSq) {
                        var5.edge = var8;
                        var5.node = null;
                        var5.distSq = var24;
                     }
                  }
               }
            }
         }

      }

      void getClosestPointOnEdge(float var1, float var2, CollideWithObstaclesPoly.ClosestPointOnEdge var3) {
         for(int var4 = 0; var4 < this.size(); ++var4) {
            CollideWithObstaclesPoly.CCEdge var5 = (CollideWithObstaclesPoly.CCEdge)this.get(var4);
            var5.getClosestPointOnEdge(var1, var2, var3);
         }

      }

      void render(boolean var1) {
         if (!this.isEmpty()) {
            float var2 = 0.0F;
            float var3 = var1 ? 1.0F : 0.5F;
            float var4 = var1 ? 0.0F : 0.5F;
            BaseVehicle.Vector3fObjectPool var5 = (BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get();
            Iterator var6 = this.iterator();

            while(var6.hasNext()) {
               CollideWithObstaclesPoly.CCEdge var7 = (CollideWithObstaclesPoly.CCEdge)var6.next();
               CollideWithObstaclesPoly.CCNode var8 = var7.node1;
               CollideWithObstaclesPoly.CCNode var9 = var7.node2;
               LineDrawer.addLine(var8.x, var8.y, (float)var8.z, var9.x, var9.y, (float)var9.z, var2, var3, var4, (String)null, true);
               boolean var10 = false;
               if (var10) {
                  Vector3f var11 = ((Vector3f)var5.alloc()).set(var9.x - var8.x, var9.y - var8.y, (float)(var9.z - var8.z)).normalize();
                  Vector3f var12 = ((Vector3f)var5.alloc()).set((Vector3fc)var11).cross(0.0F, 0.0F, 1.0F).normalize();
                  var11.mul(0.9F);
                  LineDrawer.addLine(var9.x - var11.x * 0.1F - var12.x * 0.1F, var9.y - var11.y * 0.1F - var12.y * 0.1F, (float)var9.z, var9.x, var9.y, (float)var9.z, var2, var3, var4, (String)null, true);
                  LineDrawer.addLine(var9.x - var11.x * 0.1F + var12.x * 0.1F, var9.y - var11.y * 0.1F + var12.y * 0.1F, (float)var9.z, var9.x, var9.y, (float)var9.z, var2, var3, var4, (String)null, true);
                  var5.release(var11);
                  var5.release(var12);
               }
            }

            CollideWithObstaclesPoly.CCNode var13 = ((CollideWithObstaclesPoly.CCEdge)this.get(0)).node1;
            LineDrawer.addRect(var13.x - 0.1F, var13.y - 0.1F, (float)var13.z, 0.2F, 0.2F, 1.0F, 0.0F, 0.0F);
         }
      }

      static void releaseAll(ArrayList var0) {
         pool.releaseAll(var0);
      }
   }

   private static enum EdgeRingHit {
      OnEdge,
      Inside,
      Outside;

      // $FF: synthetic method
      private static CollideWithObstaclesPoly.EdgeRingHit[] $values() {
         return new CollideWithObstaclesPoly.EdgeRingHit[]{OnEdge, Inside, Outside};
      }
   }
}
