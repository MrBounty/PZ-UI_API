package zombie.vehicles;

import astar.ASearchNode;
import astar.AStar;
import astar.IGoalNode;
import astar.ISearchNode;
import gnu.trove.list.array.TFloatArrayList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.awt.geom.Line2D;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import zombie.Lua.LuaManager;
import zombie.ai.KnownBlockedEdges;
import zombie.ai.astar.Mover;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.core.physics.Transform;
import zombie.core.utils.BooleanGrid;
import zombie.debug.DebugOptions;
import zombie.debug.LineDrawer;
import zombie.input.GameKeyboard;
import zombie.input.Mouse;
import zombie.iso.IsoChunk;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.SpriteDetails.IsoObjectType;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.MPStatistic;
import zombie.network.ServerMap;
import zombie.popman.ObjectPool;
import zombie.scripting.objects.VehicleScript;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;

public final class PolygonalMap2 {
   public static final float RADIUS = 0.3F;
   private static final float RADIUS_DIAGONAL = (float)Math.sqrt(0.18000000715255737D);
   public static final boolean CLOSE_TO_WALLS = true;
   public static final boolean PATHS_UNDER_VEHICLES = true;
   public static final boolean COLLIDE_CLIPPER = false;
   public static final boolean COLLIDE_BEVEL = false;
   public static final int CXN_FLAG_CAN_PATH = 1;
   public static final int CXN_FLAG_THUMP = 2;
   public static final int NODE_FLAG_CRAWL = 1;
   public static final int NODE_FLAG_CRAWL_INTERIOR = 2;
   public static final int NODE_FLAG_IN_CHUNK_DATA = 4;
   public static final int NODE_FLAG_PERIMETER = 8;
   public static final int NODE_FLAG_KEEP = 65536;
   private static final Vector2 temp = new Vector2();
   private static final Vector3f tempVec3f_1 = new Vector3f();
   private final ArrayList clusters = new ArrayList();
   private PolygonalMap2.ClosestPointOnEdge closestPointOnEdge = new PolygonalMap2.ClosestPointOnEdge();
   private final TIntObjectHashMap squareToNode = new TIntObjectHashMap();
   private final ArrayList tempSquares = new ArrayList();
   public static final PolygonalMap2 instance = new PolygonalMap2();
   private final ArrayList graphs = new ArrayList();
   private Clipper clipperThread;
   private final ByteBuffer xyBufferThread = ByteBuffer.allocateDirect(8192);
   private final PolygonalMap2.AdjustStartEndNodeData adjustStartData = new PolygonalMap2.AdjustStartEndNodeData();
   private final PolygonalMap2.AdjustStartEndNodeData adjustGoalData = new PolygonalMap2.AdjustStartEndNodeData();
   private final PolygonalMap2.LineClearCollide lcc = new PolygonalMap2.LineClearCollide();
   private final PolygonalMap2.VGAStar astar = new PolygonalMap2.VGAStar();
   private final PolygonalMap2.TestRequest testRequest = new PolygonalMap2.TestRequest();
   private int testZ = 0;
   private final PathFindBehavior2.PointOnPath pointOnPath = new PathFindBehavior2.PointOnPath();
   private static final int SQUARES_PER_CHUNK = 10;
   private static final int LEVELS_PER_CHUNK = 8;
   private static final int SQUARES_PER_CELL = 300;
   private static final int CHUNKS_PER_CELL = 30;
   private static final int BIT_SOLID = 1;
   private static final int BIT_COLLIDE_W = 2;
   private static final int BIT_COLLIDE_N = 4;
   private static final int BIT_STAIR_TW = 8;
   private static final int BIT_STAIR_MW = 16;
   private static final int BIT_STAIR_BW = 32;
   private static final int BIT_STAIR_TN = 64;
   private static final int BIT_STAIR_MN = 128;
   private static final int BIT_STAIR_BN = 256;
   private static final int BIT_SOLID_FLOOR = 512;
   private static final int BIT_SOLID_TRANS = 1024;
   private static final int BIT_WINDOW_W = 2048;
   private static final int BIT_WINDOW_N = 4096;
   private static final int BIT_CAN_PATH_W = 8192;
   private static final int BIT_CAN_PATH_N = 16384;
   private static final int BIT_THUMP_W = 32768;
   private static final int BIT_THUMP_N = 65536;
   private static final int BIT_THUMPABLE = 131072;
   private static final int BIT_DOOR_E = 262144;
   private static final int BIT_DOOR_S = 524288;
   private static final int BIT_WINDOW_W_UNBLOCKED = 1048576;
   private static final int BIT_WINDOW_N_UNBLOCKED = 2097152;
   private static final int ALL_SOLID_BITS = 1025;
   private static final int ALL_STAIR_BITS = 504;
   private final ConcurrentLinkedQueue chunkTaskQueue = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue squareTaskQueue = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue vehicleTaskQueue = new ConcurrentLinkedQueue();
   private final ArrayList vehicles = new ArrayList();
   private final HashMap vehicleMap = new HashMap();
   private int minX;
   private int minY;
   private int width;
   private int height;
   private PolygonalMap2.Cell[][] cells;
   private final HashMap vehicleState = new HashMap();
   private final TObjectProcedure releaseNodeProc = new TObjectProcedure() {
      public boolean execute(PolygonalMap2.Node var1) {
         var1.release();
         return true;
      }
   };
   private boolean rebuild;
   private final PolygonalMap2.Path shortestPath = new PolygonalMap2.Path();
   private final PolygonalMap2.Sync sync = new PolygonalMap2.Sync();
   private final Object renderLock = new Object();
   private PolygonalMap2.PMThread thread;
   private final ArrayDeque requests = new ArrayDeque();
   private final ConcurrentLinkedQueue requestToMain = new ConcurrentLinkedQueue();
   private final ConcurrentLinkedQueue requestTaskQueue = new ConcurrentLinkedQueue();
   private final HashMap requestMap = new HashMap();
   public static final int LCC_ZERO = 0;
   public static final int LCC_IGNORE_DOORS = 1;
   public static final int LCC_CLOSE_TO_WALLS = 2;
   public static final int LCC_CHECK_COST = 4;
   public static final int LCC_RENDER = 8;
   public static final int LCC_ALLOW_ON_EDGE = 16;
   private final PolygonalMap2.LineClearCollideMain lccMain = new PolygonalMap2.LineClearCollideMain();
   private final float[] tempFloats = new float[8];
   private final CollideWithObstacles collideWithObstacles = new CollideWithObstacles();
   private final CollideWithObstaclesPoly collideWithObstaclesPoly = new CollideWithObstaclesPoly();

   private void createVehicleCluster(PolygonalMap2.VehicleRect var1, ArrayList var2, ArrayList var3) {
      for(int var4 = 0; var4 < var2.size(); ++var4) {
         PolygonalMap2.VehicleRect var5 = (PolygonalMap2.VehicleRect)var2.get(var4);
         if (var1 != var5 && var1.z == var5.z && (var1.cluster == null || var1.cluster != var5.cluster) && var1.isAdjacent(var5)) {
            if (var1.cluster != null) {
               if (var5.cluster == null) {
                  var5.cluster = var1.cluster;
                  var5.cluster.rects.add(var5);
               } else {
                  var3.remove(var5.cluster);
                  var1.cluster.merge(var5.cluster);
               }
            } else if (var5.cluster != null) {
               if (var1.cluster == null) {
                  var1.cluster = var5.cluster;
                  var1.cluster.rects.add(var1);
               } else {
                  var3.remove(var1.cluster);
                  var5.cluster.merge(var1.cluster);
               }
            } else {
               PolygonalMap2.VehicleCluster var6 = PolygonalMap2.VehicleCluster.alloc().init();
               var1.cluster = var6;
               var5.cluster = var6;
               var6.rects.add(var1);
               var6.rects.add(var5);
               var3.add(var6);
            }
         }
      }

      if (var1.cluster == null) {
         PolygonalMap2.VehicleCluster var7 = PolygonalMap2.VehicleCluster.alloc().init();
         var1.cluster = var7;
         var7.rects.add(var1);
         var3.add(var7);
      }

   }

   private void createVehicleClusters() {
      this.clusters.clear();
      ArrayList var1 = new ArrayList();

      int var2;
      for(var2 = 0; var2 < this.vehicles.size(); ++var2) {
         PolygonalMap2.Vehicle var3 = (PolygonalMap2.Vehicle)this.vehicles.get(var2);
         PolygonalMap2.VehicleRect var4 = PolygonalMap2.VehicleRect.alloc();
         var3.polyPlusRadius.getAABB(var4);
         var4.vehicle = var3;
         var1.add(var4);
      }

      if (!var1.isEmpty()) {
         for(var2 = 0; var2 < var1.size(); ++var2) {
            PolygonalMap2.VehicleRect var5 = (PolygonalMap2.VehicleRect)var1.get(var2);
            this.createVehicleCluster(var5, var1, this.clusters);
         }

      }
   }

   private PolygonalMap2.Node getNodeForSquare(PolygonalMap2.Square var1) {
      PolygonalMap2.Node var2 = (PolygonalMap2.Node)this.squareToNode.get(var1.ID);
      if (var2 == null) {
         var2 = PolygonalMap2.Node.alloc().init(var1);
         this.squareToNode.put(var1.ID, var2);
      }

      return var2;
   }

   private PolygonalMap2.VisibilityGraph getVisGraphAt(float var1, float var2, int var3) {
      for(int var4 = 0; var4 < this.graphs.size(); ++var4) {
         PolygonalMap2.VisibilityGraph var5 = (PolygonalMap2.VisibilityGraph)this.graphs.get(var4);
         if (var5.contains(var1, var2, var3)) {
            return var5;
         }
      }

      return null;
   }

   private PolygonalMap2.VisibilityGraph getVisGraphAt(float var1, float var2, int var3, int var4) {
      for(int var5 = 0; var5 < this.graphs.size(); ++var5) {
         PolygonalMap2.VisibilityGraph var6 = (PolygonalMap2.VisibilityGraph)this.graphs.get(var5);
         if (var6.contains(var1, var2, var3, var4)) {
            return var6;
         }
      }

      return null;
   }

   private PolygonalMap2.VisibilityGraph getVisGraphForSquare(PolygonalMap2.Square var1) {
      for(int var2 = 0; var2 < this.graphs.size(); ++var2) {
         PolygonalMap2.VisibilityGraph var3 = (PolygonalMap2.VisibilityGraph)this.graphs.get(var2);
         if (var3.contains(var1)) {
            return var3;
         }
      }

      return null;
   }

   private PolygonalMap2.Connection connectTwoNodes(PolygonalMap2.Node var1, PolygonalMap2.Node var2, int var3) {
      PolygonalMap2.Connection var4 = PolygonalMap2.Connection.alloc().init(var1, var2, var3);
      var1.visible.add(var4);
      var2.visible.add(var4);
      return var4;
   }

   private PolygonalMap2.Connection connectTwoNodes(PolygonalMap2.Node var1, PolygonalMap2.Node var2) {
      return this.connectTwoNodes(var1, var2, 0);
   }

   private void breakConnection(PolygonalMap2.Connection var1) {
      var1.node1.visible.remove(var1);
      var1.node2.visible.remove(var1);
      var1.release();
   }

   private void breakConnection(PolygonalMap2.Node var1, PolygonalMap2.Node var2) {
      for(int var3 = 0; var3 < var1.visible.size(); ++var3) {
         PolygonalMap2.Connection var4 = (PolygonalMap2.Connection)var1.visible.get(var3);
         if (var4.otherNode(var1) == var2) {
            this.breakConnection(var4);
            break;
         }
      }

   }

   private void addStairNodes() {
      ArrayList var1 = this.tempSquares;
      var1.clear();

      int var2;
      for(var2 = 0; var2 < this.graphs.size(); ++var2) {
         PolygonalMap2.VisibilityGraph var3 = (PolygonalMap2.VisibilityGraph)this.graphs.get(var2);
         var3.getStairSquares(var1);
      }

      for(var2 = 0; var2 < var1.size(); ++var2) {
         PolygonalMap2.Square var15 = (PolygonalMap2.Square)var1.get(var2);
         PolygonalMap2.Square var4 = null;
         PolygonalMap2.Square var5 = null;
         PolygonalMap2.Square var6 = null;
         PolygonalMap2.Square var7 = null;
         PolygonalMap2.Square var8 = null;
         if (var15.has(8)) {
            var4 = this.getSquare(var15.x - 1, var15.y, var15.z + 1);
            var5 = var15;
            var6 = this.getSquare(var15.x + 1, var15.y, var15.z);
            var7 = this.getSquare(var15.x + 2, var15.y, var15.z);
            var8 = this.getSquare(var15.x + 3, var15.y, var15.z);
         }

         if (var15.has(64)) {
            var4 = this.getSquare(var15.x, var15.y - 1, var15.z + 1);
            var5 = var15;
            var6 = this.getSquare(var15.x, var15.y + 1, var15.z);
            var7 = this.getSquare(var15.x, var15.y + 2, var15.z);
            var8 = this.getSquare(var15.x, var15.y + 3, var15.z);
         }

         if (var4 != null && var5 != null && var6 != null && var7 != null && var8 != null) {
            PolygonalMap2.Node var9 = null;
            PolygonalMap2.Node var10 = null;
            PolygonalMap2.VisibilityGraph var11 = this.getVisGraphForSquare(var4);
            Iterator var12;
            PolygonalMap2.Obstacle var13;
            if (var11 == null) {
               var9 = this.getNodeForSquare(var4);
            } else {
               var9 = PolygonalMap2.Node.alloc().init(var4);
               var12 = var11.obstacles.iterator();

               while(var12.hasNext()) {
                  var13 = (PolygonalMap2.Obstacle)var12.next();
                  if (var13.isNodeInsideOf(var9)) {
                     var9.ignore = true;
                  }
               }

               var9.addGraph(var11);
               var11.addNode(var9);
               this.squareToNode.put(var4.ID, var9);
            }

            var11 = this.getVisGraphForSquare(var8);
            if (var11 == null) {
               var10 = this.getNodeForSquare(var8);
            } else {
               var10 = PolygonalMap2.Node.alloc().init(var8);
               var12 = var11.obstacles.iterator();

               while(var12.hasNext()) {
                  var13 = (PolygonalMap2.Obstacle)var12.next();
                  if (var13.isNodeInsideOf(var10)) {
                     var10.ignore = true;
                  }
               }

               var10.addGraph(var11);
               var11.addNode(var10);
               this.squareToNode.put(var8.ID, var10);
            }

            if (var9 != null && var10 != null) {
               PolygonalMap2.Node var16 = this.getNodeForSquare(var5);
               PolygonalMap2.Node var17 = this.getNodeForSquare(var6);
               PolygonalMap2.Node var14 = this.getNodeForSquare(var7);
               this.connectTwoNodes(var9, var16);
               this.connectTwoNodes(var16, var17);
               this.connectTwoNodes(var17, var14);
               this.connectTwoNodes(var14, var10);
            }
         }
      }

   }

   private void addCanPathNodes() {
      ArrayList var1 = this.tempSquares;
      var1.clear();

      int var2;
      for(var2 = 0; var2 < this.graphs.size(); ++var2) {
         PolygonalMap2.VisibilityGraph var3 = (PolygonalMap2.VisibilityGraph)this.graphs.get(var2);
         var3.getCanPathSquares(var1);
      }

      for(var2 = 0; var2 < var1.size(); ++var2) {
         PolygonalMap2.Square var10 = (PolygonalMap2.Square)var1.get(var2);
         if (!var10.isNonThumpableSolid() && !var10.has(504) && var10.has(512)) {
            int var4;
            int var5;
            PolygonalMap2.Square var6;
            PolygonalMap2.Node var7;
            PolygonalMap2.Node var8;
            int var9;
            if (var10.isCanPathW()) {
               var4 = var10.x - 1;
               var5 = var10.y;
               var6 = this.getSquare(var4, var5, var10.z);
               if (var6 != null && !var6.isNonThumpableSolid() && !var6.has(504) && var6.has(512)) {
                  var7 = this.getOrCreateCanPathNode(var10);
                  var8 = this.getOrCreateCanPathNode(var6);
                  var9 = 1;
                  if (var10.has(163840) || var6.has(131072)) {
                     var9 |= 2;
                  }

                  this.connectTwoNodes(var7, var8, var9);
               }
            }

            if (var10.isCanPathN()) {
               var4 = var10.x;
               var5 = var10.y - 1;
               var6 = this.getSquare(var4, var5, var10.z);
               if (var6 != null && !var6.isNonThumpableSolid() && !var6.has(504) && var6.has(512)) {
                  var7 = this.getOrCreateCanPathNode(var10);
                  var8 = this.getOrCreateCanPathNode(var6);
                  var9 = 1;
                  if (var10.has(196608) || var6.has(131072)) {
                     var9 |= 2;
                  }

                  this.connectTwoNodes(var7, var8, var9);
               }
            }
         }
      }

   }

   private PolygonalMap2.Node getOrCreateCanPathNode(PolygonalMap2.Square var1) {
      PolygonalMap2.VisibilityGraph var2 = this.getVisGraphForSquare(var1);
      PolygonalMap2.Node var3 = this.getNodeForSquare(var1);
      if (var2 != null && !var2.nodes.contains(var3)) {
         Iterator var4 = var2.obstacles.iterator();

         while(var4.hasNext()) {
            PolygonalMap2.Obstacle var5 = (PolygonalMap2.Obstacle)var4.next();
            if (var5.isNodeInsideOf(var3)) {
               var3.ignore = true;
               break;
            }
         }

         var2.addNode(var3);
      }

      return var3;
   }

   private PolygonalMap2.Node getPointOutsideObjects(PolygonalMap2.Square var1, float var2, float var3) {
      PolygonalMap2.Square var4 = instance.getSquare(var1.x - 1, var1.y, var1.z);
      PolygonalMap2.Square var5 = instance.getSquare(var1.x - 1, var1.y - 1, var1.z);
      PolygonalMap2.Square var6 = instance.getSquare(var1.x, var1.y - 1, var1.z);
      PolygonalMap2.Square var7 = instance.getSquare(var1.x + 1, var1.y - 1, var1.z);
      PolygonalMap2.Square var8 = instance.getSquare(var1.x + 1, var1.y, var1.z);
      PolygonalMap2.Square var9 = instance.getSquare(var1.x + 1, var1.y + 1, var1.z);
      PolygonalMap2.Square var10 = instance.getSquare(var1.x, var1.y + 1, var1.z);
      PolygonalMap2.Square var11 = instance.getSquare(var1.x - 1, var1.y + 1, var1.z);
      float var12 = (float)var1.x;
      float var13 = (float)var1.y;
      float var14 = (float)(var1.x + 1);
      float var15 = (float)(var1.y + 1);
      if (var1.isCollideW()) {
         var12 += 0.35000002F;
      }

      if (var1.isCollideN()) {
         var13 += 0.35000002F;
      }

      if (var8 != null && (var8.has(2) || var8.has(504) || var8.isReallySolid())) {
         var14 -= 0.35000002F;
      }

      if (var10 != null && (var10.has(4) || var10.has(504) || var10.isReallySolid())) {
         var15 -= 0.35000002F;
      }

      float var16 = PZMath.clamp(var2, var12, var14);
      float var17 = PZMath.clamp(var3, var13, var15);
      boolean var18;
      float var19;
      float var20;
      if (var16 <= (float)var1.x + 0.3F && var17 <= (float)var1.y + 0.3F) {
         var18 = var5 != null && (var5.has(504) || var5.isReallySolid());
         var18 |= var6 != null && var6.has(2);
         var18 |= var4 != null && var4.has(4);
         if (var18) {
            var19 = (float)var1.x + 0.3F + 0.05F;
            var20 = (float)var1.y + 0.3F + 0.05F;
            if (var19 - var16 <= var20 - var17) {
               var16 = var19;
            } else {
               var17 = var20;
            }
         }
      }

      if (var16 >= (float)(var1.x + 1) - 0.3F && var17 <= (float)var1.y + 0.3F) {
         var18 = var7 != null && (var7.has(2) || var7.has(504) || var7.isReallySolid());
         var18 |= var8 != null && var8.has(4);
         if (var18) {
            var19 = (float)(var1.x + 1) - 0.3F - 0.05F;
            var20 = (float)var1.y + 0.3F + 0.05F;
            if (var16 - var19 <= var20 - var17) {
               var16 = var19;
            } else {
               var17 = var20;
            }
         }
      }

      if (var16 <= (float)var1.x + 0.3F && var17 >= (float)(var1.y + 1) - 0.3F) {
         var18 = var11 != null && (var11.has(4) || var11.has(504) || var11.isReallySolid());
         var18 |= var10 != null && var10.has(2);
         if (var18) {
            var19 = (float)var1.x + 0.3F + 0.05F;
            var20 = (float)(var1.y + 1) - 0.3F - 0.05F;
            if (var19 - var16 <= var17 - var20) {
               var16 = var19;
            } else {
               var17 = var20;
            }
         }
      }

      if (var16 >= (float)(var1.x + 1) - 0.3F && var17 >= (float)(var1.y + 1) - 0.3F) {
         var18 = var9 != null && (var9.has(2) || var9.has(4) || var9.has(504) || var9.isReallySolid());
         if (var18) {
            var19 = (float)(var1.x + 1) - 0.3F - 0.05F;
            var20 = (float)(var1.y + 1) - 0.3F - 0.05F;
            if (var16 - var19 <= var17 - var20) {
               var16 = var19;
            } else {
               var17 = var20;
            }
         }
      }

      return PolygonalMap2.Node.alloc().init(var16, var17, var1.z);
   }

   private void createVisibilityGraph(PolygonalMap2.VehicleCluster var1) {
      PolygonalMap2.VisibilityGraph var2 = PolygonalMap2.VisibilityGraph.alloc().init(var1);
      var2.addPerimeterEdges();
      this.graphs.add(var2);
   }

   private void createVisibilityGraphs() {
      this.createVehicleClusters();
      this.graphs.clear();
      this.squareToNode.clear();

      for(int var1 = 0; var1 < this.clusters.size(); ++var1) {
         PolygonalMap2.VehicleCluster var2 = (PolygonalMap2.VehicleCluster)this.clusters.get(var1);
         this.createVisibilityGraph(var2);
      }

      this.addStairNodes();
      this.addCanPathNodes();
   }

   private boolean findPath(PolygonalMap2.PathFindRequest var1, boolean var2) {
      int var3 = 16;
      if (!(var1.mover instanceof IsoZombie)) {
         var3 |= 4;
      }

      PolygonalMap2.VisibilityGraph var24;
      if ((int)var1.startZ == (int)var1.targetZ && !this.lcc.isNotClear(this, var1.startX, var1.startY, var1.targetX, var1.targetY, (int)var1.startZ, var3)) {
         var1.path.addNode(var1.startX, var1.startY, var1.startZ);
         var1.path.addNode(var1.targetX, var1.targetY, var1.targetZ);
         if (var2) {
            Iterator var26 = this.graphs.iterator();

            while(var26.hasNext()) {
               var24 = (PolygonalMap2.VisibilityGraph)var26.next();
               var24.render();
            }
         }

         return true;
      } else {
         this.astar.init(this.graphs, this.squareToNode);
         this.astar.knownBlockedEdges.clear();

         for(int var4 = 0; var4 < var1.knownBlockedEdges.size(); ++var4) {
            KnownBlockedEdges var5 = (KnownBlockedEdges)var1.knownBlockedEdges.get(var4);
            PolygonalMap2.Square var6 = this.getSquare(var5.x, var5.y, var5.z);
            if (var6 != null) {
               this.astar.knownBlockedEdges.put(var6.ID, var5);
            }
         }

         PolygonalMap2.VisibilityGraph var23 = null;
         var24 = null;
         PolygonalMap2.SearchNode var25 = null;
         PolygonalMap2.SearchNode var7 = null;
         boolean var8 = false;
         boolean var9 = false;
         boolean var21 = false;

         boolean var33;
         label1511: {
            boolean var30;
            int var37;
            Iterator var43;
            PolygonalMap2.VisibilityGraph var46;
            PolygonalMap2.Edge var47;
            label1512: {
               boolean var14;
               PolygonalMap2.VisibilityGraph var16;
               Iterator var41;
               int var44;
               PolygonalMap2.Edge var45;
               label1513: {
                  label1514: {
                     label1515: {
                        label1516: {
                           PolygonalMap2.VisibilityGraph var11;
                           label1517: {
                              int var13;
                              boolean var40;
                              try {
                                 var21 = true;
                                 PolygonalMap2.Square var10 = this.getSquare((int)var1.startX, (int)var1.startY, (int)var1.startZ);
                                 if (var10 == null || var10.isReallySolid()) {
                                    var30 = false;
                                    var21 = false;
                                    break label1516;
                                 }

                                 PolygonalMap2.Node var12;
                                 if (var10.has(504)) {
                                    var25 = this.astar.getSearchNode(var10);
                                 } else {
                                    var11 = this.astar.getVisGraphForSquare(var10);
                                    if (var11 != null) {
                                       if (!var11.created) {
                                          var11.create();
                                       }

                                       var12 = null;
                                       var13 = var11.getPointOutsideObstacles(var1.startX, var1.startY, var1.startZ, this.adjustStartData);
                                       if (var13 == -1) {
                                          var14 = false;
                                          var21 = false;
                                          break label1515;
                                       }

                                       if (var13 == 1) {
                                          var8 = true;
                                          var12 = this.adjustStartData.node;
                                          if (this.adjustStartData.isNodeNew) {
                                             var23 = var11;
                                          }
                                       }

                                       if (var12 == null) {
                                          var12 = PolygonalMap2.Node.alloc().init(var1.startX, var1.startY, (int)var1.startZ);
                                          var11.addNode(var12);
                                          var23 = var11;
                                       }

                                       var25 = this.astar.getSearchNode(var12);
                                    }
                                 }

                                 if (var25 == null) {
                                    var25 = this.astar.getSearchNode(var10);
                                 }

                                 if (!(var1.targetX < 0.0F) && !(var1.targetY < 0.0F) && this.getChunkFromSquarePos((int)var1.targetX, (int)var1.targetY) != null) {
                                    var10 = this.getSquare((int)var1.targetX, (int)var1.targetY, (int)var1.targetZ);
                                    if (var10 == null || var10.isReallySolid()) {
                                       var30 = false;
                                       var21 = false;
                                       break label1512;
                                    }

                                    if (((int)var1.startX != (int)var1.targetX || (int)var1.startY != (int)var1.targetY || (int)var1.startZ != (int)var1.targetZ) && this.isBlockedInAllDirections((int)var1.targetX, (int)var1.targetY, (int)var1.targetZ)) {
                                       var30 = false;
                                       var21 = false;
                                       break label1514;
                                    }

                                    if (var10.has(504)) {
                                       var7 = this.astar.getSearchNode(var10);
                                    } else {
                                       var11 = this.astar.getVisGraphForSquare(var10);
                                       if (var11 != null) {
                                          if (!var11.created) {
                                             var11.create();
                                          }

                                          var12 = null;
                                          var13 = var11.getPointOutsideObstacles(var1.targetX, var1.targetY, var1.targetZ, this.adjustGoalData);
                                          if (var13 == -1) {
                                             var14 = false;
                                             var21 = false;
                                             break label1513;
                                          }

                                          if (var13 == 1) {
                                             var9 = true;
                                             var12 = this.adjustGoalData.node;
                                             if (this.adjustGoalData.isNodeNew) {
                                                var24 = var11;
                                             }
                                          }

                                          if (var12 == null) {
                                             var12 = PolygonalMap2.Node.alloc().init(var1.targetX, var1.targetY, (int)var1.targetZ);
                                             var11.addNode(var12);
                                             var24 = var11;
                                          }

                                          var7 = this.astar.getSearchNode(var12);
                                       } else {
                                          for(var37 = 0; var37 < this.graphs.size(); ++var37) {
                                             var11 = (PolygonalMap2.VisibilityGraph)this.graphs.get(var37);
                                             if (var11.contains(var10, 1)) {
                                                PolygonalMap2.Node var31 = this.getPointOutsideObjects(var10, var1.targetX, var1.targetY);
                                                var11.addNode(var31);
                                                if (var31.x != var1.targetX || var31.y != var1.targetY) {
                                                   var9 = true;
                                                   this.adjustGoalData.isNodeNew = false;
                                                }

                                                var24 = var11;
                                                var7 = this.astar.getSearchNode(var31);
                                                break;
                                             }
                                          }
                                       }
                                    }

                                    if (var7 == null) {
                                       var7 = this.astar.getSearchNode(var10);
                                    }
                                 } else {
                                    var7 = this.astar.getSearchNode((int)var1.targetX, (int)var1.targetY);
                                 }

                                 ArrayList var29 = this.astar.shortestPath(var1, var25, var7);
                                 if (var29 == null) {
                                    var21 = false;
                                    break label1517;
                                 }

                                 if (var29.size() == 1) {
                                    var1.path.addNode(var25);
                                    if (!var9 && var7.square != null && (float)var7.square.x + 0.5F != var1.targetX && (float)var7.square.y + 0.5F != var1.targetY) {
                                       var1.path.addNode(var1.targetX, var1.targetY, var1.targetZ, 0);
                                    } else {
                                       var1.path.addNode(var7);
                                    }

                                    var33 = true;
                                    var21 = false;
                                    break label1511;
                                 }

                                 this.cleanPath(var29, var1, var8, var9, var7);
                                 if (var1.mover instanceof IsoPlayer && !((IsoPlayer)var1.mover).isNPC()) {
                                    this.smoothPath(var1.path);
                                 }

                                 var40 = true;
                                 var21 = false;
                              } finally {
                                 if (var21) {
                                    if (var2) {
                                       Iterator var18 = this.graphs.iterator();

                                       while(var18.hasNext()) {
                                          PolygonalMap2.VisibilityGraph var19 = (PolygonalMap2.VisibilityGraph)var18.next();
                                          var19.render();
                                       }
                                    }

                                    if (var23 != null) {
                                       var23.removeNode(var25.vgNode);
                                    }

                                    if (var24 != null) {
                                       var24.removeNode(var7.vgNode);
                                    }

                                    int var48;
                                    for(var48 = 0; var48 < this.astar.searchNodes.size(); ++var48) {
                                       ((PolygonalMap2.SearchNode)this.astar.searchNodes.get(var48)).release();
                                    }

                                    PolygonalMap2.Edge var49;
                                    if (var8 && this.adjustStartData.isNodeNew) {
                                       for(var48 = 0; var48 < this.adjustStartData.node.edges.size(); ++var48) {
                                          var49 = (PolygonalMap2.Edge)this.adjustStartData.node.edges.get(var48);
                                          var49.obstacle.unsplit(this.adjustStartData.node, var49.edgeRing);
                                       }

                                       this.adjustStartData.graph.edges.remove(this.adjustStartData.newEdge);
                                    }

                                    if (var9 && this.adjustGoalData.isNodeNew) {
                                       for(var48 = 0; var48 < this.adjustGoalData.node.edges.size(); ++var48) {
                                          var49 = (PolygonalMap2.Edge)this.adjustGoalData.node.edges.get(var48);
                                          var49.obstacle.unsplit(this.adjustGoalData.node, var49.edgeRing);
                                       }

                                       this.adjustGoalData.graph.edges.remove(this.adjustGoalData.newEdge);
                                    }

                                 }
                              }

                              if (var2) {
                                 Iterator var34 = this.graphs.iterator();

                                 while(var34.hasNext()) {
                                    PolygonalMap2.VisibilityGraph var35 = (PolygonalMap2.VisibilityGraph)var34.next();
                                    var35.render();
                                 }
                              }

                              if (var23 != null) {
                                 var23.removeNode(var25.vgNode);
                              }

                              if (var24 != null) {
                                 var24.removeNode(var7.vgNode);
                              }

                              for(var13 = 0; var13 < this.astar.searchNodes.size(); ++var13) {
                                 ((PolygonalMap2.SearchNode)this.astar.searchNodes.get(var13)).release();
                              }

                              PolygonalMap2.Edge var36;
                              if (var8 && this.adjustStartData.isNodeNew) {
                                 for(var13 = 0; var13 < this.adjustStartData.node.edges.size(); ++var13) {
                                    var36 = (PolygonalMap2.Edge)this.adjustStartData.node.edges.get(var13);
                                    var36.obstacle.unsplit(this.adjustStartData.node, var36.edgeRing);
                                 }

                                 this.adjustStartData.graph.edges.remove(this.adjustStartData.newEdge);
                              }

                              if (var9 && this.adjustGoalData.isNodeNew) {
                                 for(var13 = 0; var13 < this.adjustGoalData.node.edges.size(); ++var13) {
                                    var36 = (PolygonalMap2.Edge)this.adjustGoalData.node.edges.get(var13);
                                    var36.obstacle.unsplit(this.adjustGoalData.node, var36.edgeRing);
                                 }

                                 this.adjustGoalData.graph.edges.remove(this.adjustGoalData.newEdge);
                              }

                              return var40;
                           }

                           if (var2) {
                              Iterator var27 = this.graphs.iterator();

                              while(var27.hasNext()) {
                                 var11 = (PolygonalMap2.VisibilityGraph)var27.next();
                                 var11.render();
                              }
                           }

                           if (var23 != null) {
                              var23.removeNode(var25.vgNode);
                           }

                           if (var24 != null) {
                              var24.removeNode(var7.vgNode);
                           }

                           int var28;
                           for(var28 = 0; var28 < this.astar.searchNodes.size(); ++var28) {
                              ((PolygonalMap2.SearchNode)this.astar.searchNodes.get(var28)).release();
                           }

                           PolygonalMap2.Edge var32;
                           if (var8 && this.adjustStartData.isNodeNew) {
                              for(var28 = 0; var28 < this.adjustStartData.node.edges.size(); ++var28) {
                                 var32 = (PolygonalMap2.Edge)this.adjustStartData.node.edges.get(var28);
                                 var32.obstacle.unsplit(this.adjustStartData.node, var32.edgeRing);
                              }

                              this.adjustStartData.graph.edges.remove(this.adjustStartData.newEdge);
                           }

                           if (var9 && this.adjustGoalData.isNodeNew) {
                              for(var28 = 0; var28 < this.adjustGoalData.node.edges.size(); ++var28) {
                                 var32 = (PolygonalMap2.Edge)this.adjustGoalData.node.edges.get(var28);
                                 var32.obstacle.unsplit(this.adjustGoalData.node, var32.edgeRing);
                              }

                              this.adjustGoalData.graph.edges.remove(this.adjustGoalData.newEdge);
                           }

                           return false;
                        }

                        if (var2) {
                           var43 = this.graphs.iterator();

                           while(var43.hasNext()) {
                              var46 = (PolygonalMap2.VisibilityGraph)var43.next();
                              var46.render();
                           }
                        }

                        if (var23 != null) {
                           var23.removeNode(var25.vgNode);
                        }

                        if (var24 != null) {
                           var24.removeNode(var7.vgNode);
                        }

                        for(var37 = 0; var37 < this.astar.searchNodes.size(); ++var37) {
                           ((PolygonalMap2.SearchNode)this.astar.searchNodes.get(var37)).release();
                        }

                        if (var8 && this.adjustStartData.isNodeNew) {
                           for(var37 = 0; var37 < this.adjustStartData.node.edges.size(); ++var37) {
                              var47 = (PolygonalMap2.Edge)this.adjustStartData.node.edges.get(var37);
                              var47.obstacle.unsplit(this.adjustStartData.node, var47.edgeRing);
                           }

                           this.adjustStartData.graph.edges.remove(this.adjustStartData.newEdge);
                        }

                        if (var9 && this.adjustGoalData.isNodeNew) {
                           for(var37 = 0; var37 < this.adjustGoalData.node.edges.size(); ++var37) {
                              var47 = (PolygonalMap2.Edge)this.adjustGoalData.node.edges.get(var37);
                              var47.obstacle.unsplit(this.adjustGoalData.node, var47.edgeRing);
                           }

                           this.adjustGoalData.graph.edges.remove(this.adjustGoalData.newEdge);
                        }

                        return var30;
                     }

                     if (var2) {
                        var41 = this.graphs.iterator();

                        while(var41.hasNext()) {
                           var16 = (PolygonalMap2.VisibilityGraph)var41.next();
                           var16.render();
                        }
                     }

                     if (var23 != null) {
                        var23.removeNode(var25.vgNode);
                     }

                     if (var24 != null) {
                        var24.removeNode(var7.vgNode);
                     }

                     for(var44 = 0; var44 < this.astar.searchNodes.size(); ++var44) {
                        ((PolygonalMap2.SearchNode)this.astar.searchNodes.get(var44)).release();
                     }

                     if (var8 && this.adjustStartData.isNodeNew) {
                        for(var44 = 0; var44 < this.adjustStartData.node.edges.size(); ++var44) {
                           var45 = (PolygonalMap2.Edge)this.adjustStartData.node.edges.get(var44);
                           var45.obstacle.unsplit(this.adjustStartData.node, var45.edgeRing);
                        }

                        this.adjustStartData.graph.edges.remove(this.adjustStartData.newEdge);
                     }

                     if (var9 && this.adjustGoalData.isNodeNew) {
                        for(var44 = 0; var44 < this.adjustGoalData.node.edges.size(); ++var44) {
                           var45 = (PolygonalMap2.Edge)this.adjustGoalData.node.edges.get(var44);
                           var45.obstacle.unsplit(this.adjustGoalData.node, var45.edgeRing);
                        }

                        this.adjustGoalData.graph.edges.remove(this.adjustGoalData.newEdge);
                     }

                     return var14;
                  }

                  if (var2) {
                     var43 = this.graphs.iterator();

                     while(var43.hasNext()) {
                        var46 = (PolygonalMap2.VisibilityGraph)var43.next();
                        var46.render();
                     }
                  }

                  if (var23 != null) {
                     var23.removeNode(var25.vgNode);
                  }

                  if (var24 != null) {
                     var24.removeNode(var7.vgNode);
                  }

                  for(var37 = 0; var37 < this.astar.searchNodes.size(); ++var37) {
                     ((PolygonalMap2.SearchNode)this.astar.searchNodes.get(var37)).release();
                  }

                  if (var8 && this.adjustStartData.isNodeNew) {
                     for(var37 = 0; var37 < this.adjustStartData.node.edges.size(); ++var37) {
                        var47 = (PolygonalMap2.Edge)this.adjustStartData.node.edges.get(var37);
                        var47.obstacle.unsplit(this.adjustStartData.node, var47.edgeRing);
                     }

                     this.adjustStartData.graph.edges.remove(this.adjustStartData.newEdge);
                  }

                  if (var9 && this.adjustGoalData.isNodeNew) {
                     for(var37 = 0; var37 < this.adjustGoalData.node.edges.size(); ++var37) {
                        var47 = (PolygonalMap2.Edge)this.adjustGoalData.node.edges.get(var37);
                        var47.obstacle.unsplit(this.adjustGoalData.node, var47.edgeRing);
                     }

                     this.adjustGoalData.graph.edges.remove(this.adjustGoalData.newEdge);
                  }

                  return var30;
               }

               if (var2) {
                  var41 = this.graphs.iterator();

                  while(var41.hasNext()) {
                     var16 = (PolygonalMap2.VisibilityGraph)var41.next();
                     var16.render();
                  }
               }

               if (var23 != null) {
                  var23.removeNode(var25.vgNode);
               }

               if (var24 != null) {
                  var24.removeNode(var7.vgNode);
               }

               for(var44 = 0; var44 < this.astar.searchNodes.size(); ++var44) {
                  ((PolygonalMap2.SearchNode)this.astar.searchNodes.get(var44)).release();
               }

               if (var8 && this.adjustStartData.isNodeNew) {
                  for(var44 = 0; var44 < this.adjustStartData.node.edges.size(); ++var44) {
                     var45 = (PolygonalMap2.Edge)this.adjustStartData.node.edges.get(var44);
                     var45.obstacle.unsplit(this.adjustStartData.node, var45.edgeRing);
                  }

                  this.adjustStartData.graph.edges.remove(this.adjustStartData.newEdge);
               }

               if (var9 && this.adjustGoalData.isNodeNew) {
                  for(var44 = 0; var44 < this.adjustGoalData.node.edges.size(); ++var44) {
                     var45 = (PolygonalMap2.Edge)this.adjustGoalData.node.edges.get(var44);
                     var45.obstacle.unsplit(this.adjustGoalData.node, var45.edgeRing);
                  }

                  this.adjustGoalData.graph.edges.remove(this.adjustGoalData.newEdge);
               }

               return var14;
            }

            if (var2) {
               var43 = this.graphs.iterator();

               while(var43.hasNext()) {
                  var46 = (PolygonalMap2.VisibilityGraph)var43.next();
                  var46.render();
               }
            }

            if (var23 != null) {
               var23.removeNode(var25.vgNode);
            }

            if (var24 != null) {
               var24.removeNode(var7.vgNode);
            }

            for(var37 = 0; var37 < this.astar.searchNodes.size(); ++var37) {
               ((PolygonalMap2.SearchNode)this.astar.searchNodes.get(var37)).release();
            }

            if (var8 && this.adjustStartData.isNodeNew) {
               for(var37 = 0; var37 < this.adjustStartData.node.edges.size(); ++var37) {
                  var47 = (PolygonalMap2.Edge)this.adjustStartData.node.edges.get(var37);
                  var47.obstacle.unsplit(this.adjustStartData.node, var47.edgeRing);
               }

               this.adjustStartData.graph.edges.remove(this.adjustStartData.newEdge);
            }

            if (var9 && this.adjustGoalData.isNodeNew) {
               for(var37 = 0; var37 < this.adjustGoalData.node.edges.size(); ++var37) {
                  var47 = (PolygonalMap2.Edge)this.adjustGoalData.node.edges.get(var37);
                  var47.obstacle.unsplit(this.adjustGoalData.node, var47.edgeRing);
               }

               this.adjustGoalData.graph.edges.remove(this.adjustGoalData.newEdge);
            }

            return var30;
         }

         if (var2) {
            Iterator var39 = this.graphs.iterator();

            while(var39.hasNext()) {
               PolygonalMap2.VisibilityGraph var15 = (PolygonalMap2.VisibilityGraph)var39.next();
               var15.render();
            }
         }

         if (var23 != null) {
            var23.removeNode(var25.vgNode);
         }

         if (var24 != null) {
            var24.removeNode(var7.vgNode);
         }

         int var42;
         for(var42 = 0; var42 < this.astar.searchNodes.size(); ++var42) {
            ((PolygonalMap2.SearchNode)this.astar.searchNodes.get(var42)).release();
         }

         PolygonalMap2.Edge var38;
         if (var8 && this.adjustStartData.isNodeNew) {
            for(var42 = 0; var42 < this.adjustStartData.node.edges.size(); ++var42) {
               var38 = (PolygonalMap2.Edge)this.adjustStartData.node.edges.get(var42);
               var38.obstacle.unsplit(this.adjustStartData.node, var38.edgeRing);
            }

            this.adjustStartData.graph.edges.remove(this.adjustStartData.newEdge);
         }

         if (var9 && this.adjustGoalData.isNodeNew) {
            for(var42 = 0; var42 < this.adjustGoalData.node.edges.size(); ++var42) {
               var38 = (PolygonalMap2.Edge)this.adjustGoalData.node.edges.get(var42);
               var38.obstacle.unsplit(this.adjustGoalData.node, var38.edgeRing);
            }

            this.adjustGoalData.graph.edges.remove(this.adjustGoalData.newEdge);
         }

         return var33;
      }
   }

   private void cleanPath(ArrayList var1, PolygonalMap2.PathFindRequest var2, boolean var3, boolean var4, PolygonalMap2.SearchNode var5) {
      boolean var6 = var2.mover instanceof IsoPlayer && ((IsoPlayer)var2.mover).isNPC();
      PolygonalMap2.Square var7 = null;
      int var8 = -123;
      int var9 = -123;

      for(int var10 = 0; var10 < var1.size(); ++var10) {
         PolygonalMap2.SearchNode var11 = (PolygonalMap2.SearchNode)var1.get(var10);
         float var12 = var11.getX();
         float var13 = var11.getY();
         float var14 = var11.getZ();
         int var15 = var11.vgNode == null ? 0 : var11.vgNode.flags;
         PolygonalMap2.Square var16 = var11.square;
         boolean var17 = false;
         int var19;
         if (var16 != null && var7 != null && var16.z == var7.z) {
            int var18 = var16.x - var7.x;
            var19 = var16.y - var7.y;
            if (var18 == var8 && var19 == var9) {
               if (var2.path.nodes.size() > 1) {
                  var17 = true;
                  if (var2.path.getLastNode().hasFlag(65536)) {
                     var17 = false;
                  }
               }

               if (var18 == 0 && var19 == -1 && var7.has(16384)) {
                  var17 = false;
               } else if (var18 == 0 && var19 == 1 && var16.has(16384)) {
                  var17 = false;
               } else if (var18 == -1 && var19 == 0 && var7.has(8192)) {
                  var17 = false;
               } else if (var18 == 1 && var19 == 0 && var16.has(8192)) {
                  var17 = false;
               }
            } else {
               var8 = var18;
               var9 = var19;
            }
         } else {
            var9 = -123;
            var8 = -123;
         }

         if (var16 != null) {
            var7 = var16;
         } else {
            var7 = null;
         }

         if (var6) {
            var17 = false;
         }

         PolygonalMap2.PathNode var26;
         if (var17) {
            var26 = var2.path.getLastNode();
            var26.x = (float)var16.x + 0.5F;
            var26.y = (float)var16.y + 0.5F;
         } else {
            if (var2.path.nodes.size() > 1) {
               var26 = var2.path.getLastNode();
               if (Math.abs(var26.x - var12) < 0.01F && Math.abs(var26.y - var13) < 0.01F && Math.abs(var26.z - var14) < 0.01F) {
                  var26.x = var12;
                  var26.y = var13;
                  var26.z = var14;
                  continue;
               }
            }

            if (var10 > 0 && var11.square != null) {
               PolygonalMap2.SearchNode var27 = (PolygonalMap2.SearchNode)var1.get(var10 - 1);
               if (var27.square != null) {
                  var19 = var11.square.x - var27.square.x;
                  int var20 = var11.square.y - var27.square.y;
                  if (var19 == 0 && var20 == -1 && var27.square.has(16384)) {
                     var15 |= 65536;
                  } else if (var19 == 0 && var20 == 1 && var11.square.has(16384)) {
                     var15 |= 65536;
                  } else if (var19 == -1 && var20 == 0 && var27.square.has(8192)) {
                     var15 |= 65536;
                  } else if (var19 == 1 && var20 == 0 && var11.square.has(8192)) {
                     var15 |= 65536;
                  }
               }
            }

            var2.path.addNode(var12, var13, var14, var15);
         }
      }

      if (var2.mover instanceof IsoPlayer && !var6) {
         if (var2.path.isEmpty()) {
            Object var10000 = null;
         } else {
            var2.path.getNode(0);
         }

         if (!var4 && var5.square != null && (double)IsoUtils.DistanceToSquared((float)var5.square.x + 0.5F, (float)var5.square.y + 0.5F, var2.targetX, var2.targetY) > 0.010000000000000002D) {
            var2.path.addNode(var2.targetX, var2.targetY, var2.targetZ, 0);
         }
      }

      PolygonalMap2.PathNode var21 = null;

      for(int var22 = 0; var22 < var2.path.nodes.size(); ++var22) {
         PolygonalMap2.PathNode var23 = (PolygonalMap2.PathNode)var2.path.nodes.get(var22);
         PolygonalMap2.PathNode var24 = var22 < var2.path.nodes.size() - 1 ? (PolygonalMap2.PathNode)var2.path.nodes.get(var22 + 1) : null;
         if (var23.hasFlag(1)) {
            boolean var25 = var21 != null && var21.hasFlag(2) || var24 != null && var24.hasFlag(2);
            if (!var25) {
               var23.flags &= -4;
            }
         }

         var21 = var23;
      }

   }

   private void smoothPath(PolygonalMap2.Path var1) {
      int var2 = 0;

      while(true) {
         while(var2 < var1.nodes.size() - 2) {
            PolygonalMap2.PathNode var3 = (PolygonalMap2.PathNode)var1.nodes.get(var2);
            PolygonalMap2.PathNode var4 = (PolygonalMap2.PathNode)var1.nodes.get(var2 + 1);
            PolygonalMap2.PathNode var5 = (PolygonalMap2.PathNode)var1.nodes.get(var2 + 2);
            if ((int)var3.z == (int)var4.z && (int)var3.z == (int)var5.z) {
               if (!this.lcc.isNotClear(this, var3.x, var3.y, var5.x, var5.y, (int)var3.z, 20)) {
                  var1.nodes.remove(var2 + 1);
                  var1.nodePool.push(var4);
               } else {
                  ++var2;
               }
            } else {
               ++var2;
            }
         }

         return;
      }
   }

   float getApparentZ(IsoGridSquare var1) {
      if (!var1.Has(IsoObjectType.stairsTW) && !var1.Has(IsoObjectType.stairsTN)) {
         if (!var1.Has(IsoObjectType.stairsMW) && !var1.Has(IsoObjectType.stairsMN)) {
            return !var1.Has(IsoObjectType.stairsBW) && !var1.Has(IsoObjectType.stairsBN) ? (float)var1.z : (float)var1.z + 0.25F;
         } else {
            return (float)var1.z + 0.5F;
         }
      } else {
         return (float)var1.z + 0.75F;
      }
   }

   public void render() {
      if (Core.bDebug) {
         boolean var1 = DebugOptions.instance.PathfindPathToMouseEnable.getValue() && !this.testRequest.done && IsoPlayer.getInstance().getPath2() == null;
         if (DebugOptions.instance.PolymapRenderClusters.getValue()) {
            synchronized(this.renderLock) {
               Iterator var3 = this.clusters.iterator();

               while(var3.hasNext()) {
                  PolygonalMap2.VehicleCluster var4 = (PolygonalMap2.VehicleCluster)var3.next();
                  Iterator var5 = var4.rects.iterator();

                  while(var5.hasNext()) {
                     PolygonalMap2.VehicleRect var6 = (PolygonalMap2.VehicleRect)var5.next();
                     LineDrawer.addLine((float)var6.x, (float)var6.y, (float)var6.z, (float)var6.right(), (float)var6.bottom(), (float)var6.z, 0.0F, 0.0F, 1.0F, (String)null, false);
                  }

                  PolygonalMap2.VehicleRect var29 = var4.bounds();
                  var29.release();
               }

               if (!var1) {
                  var3 = this.graphs.iterator();

                  while(var3.hasNext()) {
                     PolygonalMap2.VisibilityGraph var26 = (PolygonalMap2.VisibilityGraph)var3.next();
                     var26.render();
                  }
               }
            }
         }

         float var2;
         float var25;
         int var28;
         float var31;
         float var32;
         if (DebugOptions.instance.PolymapRenderLineClearCollide.getValue()) {
            var2 = (float)Mouse.getX();
            var25 = (float)Mouse.getY();
            var28 = (int)IsoPlayer.getInstance().getZ();
            var31 = IsoUtils.XToIso(var2, var25, (float)var28);
            var32 = IsoUtils.YToIso(var2, var25, (float)var28);
            LineDrawer.addLine(IsoPlayer.getInstance().x, IsoPlayer.getInstance().y, (float)var28, var31, var32, (float)var28, 1, 1, 1, (String)null);
            byte var7 = 9;
            int var33 = var7 | 2;
            if (this.lccMain.isNotClear(this, IsoPlayer.getInstance().x, IsoPlayer.getInstance().y, var31, var32, var28, (BaseVehicle)null, var33)) {
               Vector2f var8 = this.resolveCollision(IsoPlayer.getInstance(), var31, var32, PolygonalMap2.L_render.vector2f);
               LineDrawer.addLine(var8.x - 0.05F, var8.y - 0.05F, (float)var28, var8.x + 0.05F, var8.y + 0.05F, (float)var28, 1.0F, 1.0F, 0.0F, (String)null, false);
            }
         }

         if (GameKeyboard.isKeyDown(209) && !GameKeyboard.wasKeyDown(209)) {
            this.testZ = Math.max(this.testZ - 1, 0);
         }

         if (GameKeyboard.isKeyDown(201) && !GameKeyboard.wasKeyDown(201)) {
            this.testZ = Math.min(this.testZ + 1, 7);
         }

         float var34;
         if (var1) {
            var2 = (float)Mouse.getX();
            var25 = (float)Mouse.getY();
            var28 = this.testZ;
            var31 = IsoUtils.XToIso(var2, var25, (float)var28);
            var32 = IsoUtils.YToIso(var2, var25, (float)var28);
            var34 = (float)var28;

            int var35;
            for(var35 = -1; var35 <= 2; ++var35) {
               LineDrawer.addLine((float)((int)var31 - 1), (float)((int)var32 + var35), (float)((int)var34), (float)((int)var31 + 2), (float)((int)var32 + var35), (float)((int)var34), 0.3F, 0.3F, 0.3F, (String)null, false);
            }

            for(var35 = -1; var35 <= 2; ++var35) {
               LineDrawer.addLine((float)((int)var31 + var35), (float)((int)var32 - 1), (float)((int)var34), (float)((int)var31 + var35), (float)((int)var32 + 2), (float)((int)var34), 0.3F, 0.3F, 0.3F, (String)null, false);
            }

            IsoGridSquare var13;
            for(var35 = -1; var35 <= 1; ++var35) {
               for(int var9 = -1; var9 <= 1; ++var9) {
                  float var10 = 0.3F;
                  float var11 = 0.0F;
                  float var12 = 0.0F;
                  var13 = IsoWorld.instance.CurrentCell.getGridSquare((int)var31 + var9, (int)var32 + var35, (int)var34);
                  if (var13 == null || var13.isSolid() || var13.isSolidTrans() || var13.HasStairs()) {
                     LineDrawer.addLine((float)((int)var31 + var9), (float)((int)var32 + var35), (float)((int)var34), (float)((int)var31 + var9 + 1), (float)((int)var32 + var35 + 1), (float)((int)var34), var10, var11, var12, (String)null, false);
                  }
               }
            }

            if (var28 < (int)IsoPlayer.getInstance().getZ()) {
               LineDrawer.addLine((float)((int)var31), (float)((int)var32), (float)((int)var34), (float)((int)var31), (float)((int)var32), (float)((int)IsoPlayer.getInstance().getZ()), 0.3F, 0.3F, 0.3F, (String)null, true);
            } else if (var28 > (int)IsoPlayer.getInstance().getZ()) {
               LineDrawer.addLine((float)((int)var31), (float)((int)var32), (float)((int)var34), (float)((int)var31), (float)((int)var32), (float)((int)IsoPlayer.getInstance().getZ()), 0.3F, 0.3F, 0.3F, (String)null, true);
            }

            PolygonalMap2.PathFindRequest var38 = PolygonalMap2.PathFindRequest.alloc().init(this.testRequest, IsoPlayer.getInstance(), IsoPlayer.getInstance().x, IsoPlayer.getInstance().y, IsoPlayer.getInstance().z, var31, var32, var34);
            if (DebugOptions.instance.PathfindPathToMouseAllowCrawl.getValue()) {
               var38.bCanCrawl = true;
               if (DebugOptions.instance.PathfindPathToMouseIgnoreCrawlCost.getValue()) {
                  var38.bIgnoreCrawlCost = true;
               }
            }

            if (DebugOptions.instance.PathfindPathToMouseAllowThump.getValue()) {
               var38.bCanThump = true;
            }

            this.testRequest.done = false;
            synchronized(this.renderLock) {
               boolean var39 = DebugOptions.instance.PolymapRenderClusters.getValue();
               if (this.findPath(var38, var39) && !var38.path.isEmpty()) {
                  IsoGridSquare var14;
                  float var16;
                  float var17;
                  PolygonalMap2.PathNode var44;
                  for(int var42 = 0; var42 < var38.path.nodes.size() - 1; ++var42) {
                     var44 = (PolygonalMap2.PathNode)var38.path.nodes.get(var42);
                     PolygonalMap2.PathNode var47 = (PolygonalMap2.PathNode)var38.path.nodes.get(var42 + 1);
                     var14 = IsoWorld.instance.CurrentCell.getGridSquare((double)var44.x, (double)var44.y, (double)var44.z);
                     IsoGridSquare var15 = IsoWorld.instance.CurrentCell.getGridSquare((double)var47.x, (double)var47.y, (double)var47.z);
                     var16 = var14 == null ? var44.z : this.getApparentZ(var14);
                     var17 = var15 == null ? var47.z : this.getApparentZ(var15);
                     float var18 = 1.0F;
                     float var19 = 1.0F;
                     float var20 = 0.0F;
                     if (var16 != (float)((int)var16) || var17 != (float)((int)var17)) {
                        var19 = 0.0F;
                     }

                     LineDrawer.addLine(var44.x, var44.y, var16, var47.x, var47.y, var17, var18, var19, var20, (String)null, true);
                     LineDrawer.addRect(var44.x - 0.05F, var44.y - 0.05F, var16, 0.1F, 0.1F, var18, var19, var20);
                  }

                  PathFindBehavior2.closestPointOnPath(IsoPlayer.getInstance().x, IsoPlayer.getInstance().y, IsoPlayer.getInstance().z, IsoPlayer.getInstance(), var38.path, this.pointOnPath);
                  PolygonalMap2.PathNode var43 = (PolygonalMap2.PathNode)var38.path.nodes.get(this.pointOnPath.pathIndex);
                  var44 = (PolygonalMap2.PathNode)var38.path.nodes.get(this.pointOnPath.pathIndex + 1);
                  var13 = IsoWorld.instance.CurrentCell.getGridSquare((double)var43.x, (double)var43.y, (double)var43.z);
                  var14 = IsoWorld.instance.CurrentCell.getGridSquare((double)var44.x, (double)var44.y, (double)var44.z);
                  float var48 = var13 == null ? var43.z : this.getApparentZ(var13);
                  var16 = var14 == null ? var44.z : this.getApparentZ(var14);
                  var17 = var48 + (var16 - var48) * this.pointOnPath.dist;
                  LineDrawer.addLine(this.pointOnPath.x - 0.05F, this.pointOnPath.y - 0.05F, var17, this.pointOnPath.x + 0.05F, this.pointOnPath.y + 0.05F, var17, 0.0F, 1.0F, 0.0F, (String)null, true);
                  LineDrawer.addLine(this.pointOnPath.x - 0.05F, this.pointOnPath.y + 0.05F, var17, this.pointOnPath.x + 0.05F, this.pointOnPath.y - 0.05F, var17, 0.0F, 1.0F, 0.0F, (String)null, true);
                  if (GameKeyboard.isKeyDown(207) && !GameKeyboard.wasKeyDown(207)) {
                     Object var45 = LuaManager.env.rawget("ISPathFindAction_pathToLocationF");
                     if (var45 != null) {
                        LuaManager.caller.pcall(LuaManager.thread, var45, var31, var32, var34);
                     }
                  }
               }

               var38.release();
            }
         } else {
            for(int var24 = 0; var24 < this.testRequest.path.nodes.size() - 1; ++var24) {
               PolygonalMap2.PathNode var27 = (PolygonalMap2.PathNode)this.testRequest.path.nodes.get(var24);
               PolygonalMap2.PathNode var30 = (PolygonalMap2.PathNode)this.testRequest.path.nodes.get(var24 + 1);
               var31 = 1.0F;
               var32 = 1.0F;
               var34 = 0.0F;
               if (var27.z != (float)((int)var27.z) || var30.z != (float)((int)var30.z)) {
                  var32 = 0.0F;
               }

               LineDrawer.addLine(var27.x, var27.y, var27.z, var30.x, var30.y, var30.z, var31, var32, var34, (String)null, true);
            }

            this.testRequest.done = false;
         }

         if (DebugOptions.instance.PolymapRenderConnections.getValue()) {
            var2 = (float)Mouse.getX();
            var25 = (float)Mouse.getY();
            var28 = this.testZ;
            var31 = IsoUtils.XToIso(var2, var25, (float)var28);
            var32 = IsoUtils.YToIso(var2, var25, (float)var28);
            PolygonalMap2.VisibilityGraph var36 = this.getVisGraphAt(var31, var32, var28, 1);
            if (var36 != null) {
               PolygonalMap2.Node var40 = var36.getClosestNodeTo(var31, var32);
               if (var40 != null) {
                  Iterator var37 = var40.visible.iterator();

                  while(var37.hasNext()) {
                     PolygonalMap2.Connection var41 = (PolygonalMap2.Connection)var37.next();
                     PolygonalMap2.Node var46 = var41.otherNode(var40);
                     LineDrawer.addLine(var40.x, var40.y, (float)var28, var46.x, var46.y, (float)var28, 1.0F, 0.0F, 0.0F, (String)null, true);
                  }
               }
            }
         }

         this.updateMain();
      }
   }

   public void squareChanged(IsoGridSquare var1) {
      PolygonalMap2.SquareUpdateTask var2 = PolygonalMap2.SquareUpdateTask.alloc().init(this, var1);
      this.squareTaskQueue.add(var2);
      this.thread.wake();
   }

   public void addChunkToWorld(IsoChunk var1) {
      PolygonalMap2.ChunkUpdateTask var2 = PolygonalMap2.ChunkUpdateTask.alloc().init(this, var1);
      this.chunkTaskQueue.add(var2);
      this.thread.wake();
   }

   public void removeChunkFromWorld(IsoChunk var1) {
      if (this.thread != null) {
         PolygonalMap2.ChunkRemoveTask var2 = PolygonalMap2.ChunkRemoveTask.alloc().init(this, var1);
         this.chunkTaskQueue.add(var2);
         this.thread.wake();
      }
   }

   public void addVehicleToWorld(BaseVehicle var1) {
      PolygonalMap2.VehicleAddTask var2 = PolygonalMap2.VehicleAddTask.alloc();
      var2.init(this, var1);
      this.vehicleTaskQueue.add(var2);
      PolygonalMap2.VehicleState var3 = PolygonalMap2.VehicleState.alloc().init(var1);
      this.vehicleState.put(var1, var3);
      this.thread.wake();
   }

   public void updateVehicle(BaseVehicle var1) {
      PolygonalMap2.VehicleUpdateTask var2 = PolygonalMap2.VehicleUpdateTask.alloc();
      var2.init(this, var1);
      this.vehicleTaskQueue.add(var2);
      this.thread.wake();
   }

   public void removeVehicleFromWorld(BaseVehicle var1) {
      if (this.thread != null) {
         PolygonalMap2.VehicleRemoveTask var2 = PolygonalMap2.VehicleRemoveTask.alloc();
         var2.init(this, var1);
         this.vehicleTaskQueue.add(var2);
         PolygonalMap2.VehicleState var3 = (PolygonalMap2.VehicleState)this.vehicleState.remove(var1);
         if (var3 != null) {
            var3.vehicle = null;
            var3.release();
         }

         this.thread.wake();
      }
   }

   private PolygonalMap2.Cell getCellFromSquarePos(int var1, int var2) {
      var1 -= this.minX * 300;
      var2 -= this.minY * 300;
      if (var1 >= 0 && var2 >= 0) {
         int var3 = var1 / 300;
         int var4 = var2 / 300;
         return var3 < this.width && var4 < this.height ? this.cells[var3][var4] : null;
      } else {
         return null;
      }
   }

   private PolygonalMap2.Cell getCellFromChunkPos(int var1, int var2) {
      return this.getCellFromSquarePos(var1 * 10, var2 * 10);
   }

   private PolygonalMap2.Chunk allocChunkIfNeeded(int var1, int var2) {
      PolygonalMap2.Cell var3 = this.getCellFromChunkPos(var1, var2);
      return var3 == null ? null : var3.allocChunkIfNeeded(var1, var2);
   }

   private PolygonalMap2.Chunk getChunkFromChunkPos(int var1, int var2) {
      PolygonalMap2.Cell var3 = this.getCellFromChunkPos(var1, var2);
      return var3 == null ? null : var3.getChunkFromChunkPos(var1, var2);
   }

   private PolygonalMap2.Chunk getChunkFromSquarePos(int var1, int var2) {
      PolygonalMap2.Cell var3 = this.getCellFromSquarePos(var1, var2);
      return var3 == null ? null : var3.getChunkFromChunkPos(var1 / 10, var2 / 10);
   }

   private PolygonalMap2.Square getSquare(int var1, int var2, int var3) {
      PolygonalMap2.Chunk var4 = this.getChunkFromSquarePos(var1, var2);
      return var4 == null ? null : var4.getSquare(var1, var2, var3);
   }

   private boolean isBlockedInAllDirections(int var1, int var2, int var3) {
      PolygonalMap2.Square var4 = this.getSquare(var1, var2, var3);
      if (var4 == null) {
         return false;
      } else {
         PolygonalMap2.Square var5 = this.getSquare(var1, var2 - 1, var3);
         PolygonalMap2.Square var6 = this.getSquare(var1, var2 + 1, var3);
         PolygonalMap2.Square var7 = this.getSquare(var1 - 1, var2, var3);
         PolygonalMap2.Square var8 = this.getSquare(var1 + 1, var2, var3);
         boolean var9 = var5 != null && this.astar.canNotMoveBetween(var4, var5, false);
         boolean var10 = var6 != null && this.astar.canNotMoveBetween(var4, var6, false);
         boolean var11 = var7 != null && this.astar.canNotMoveBetween(var4, var7, false);
         boolean var12 = var8 != null && this.astar.canNotMoveBetween(var4, var8, false);
         return var9 && var10 && var11 && var12;
      }
   }

   public void init(IsoMetaGrid var1) {
      this.minX = var1.getMinX();
      this.minY = var1.getMinY();
      this.width = var1.getWidth();
      this.height = var1.getHeight();
      this.cells = new PolygonalMap2.Cell[this.width][this.height];

      for(int var2 = 0; var2 < this.height; ++var2) {
         for(int var3 = 0; var3 < this.width; ++var3) {
            this.cells[var3][var2] = PolygonalMap2.Cell.alloc().init(this, this.minX + var3, this.minY + var2);
         }
      }

      this.thread = new PolygonalMap2.PMThread();
      this.thread.setName("PolyPathThread");
      this.thread.setDaemon(true);
      this.thread.start();
   }

   public void stop() {
      this.thread.bStop = true;
      this.thread.wake();

      while(this.thread.isAlive()) {
         try {
            Thread.sleep(5L);
         } catch (InterruptedException var3) {
         }
      }

      int var1;
      for(var1 = 0; var1 < this.height; ++var1) {
         for(int var2 = 0; var2 < this.width; ++var2) {
            if (this.cells[var2][var1] != null) {
               this.cells[var2][var1].release();
            }
         }
      }

      for(PolygonalMap2.IChunkTask var4 = (PolygonalMap2.IChunkTask)this.chunkTaskQueue.poll(); var4 != null; var4 = (PolygonalMap2.IChunkTask)this.chunkTaskQueue.poll()) {
         var4.release();
      }

      for(PolygonalMap2.SquareUpdateTask var5 = (PolygonalMap2.SquareUpdateTask)this.squareTaskQueue.poll(); var5 != null; var5 = (PolygonalMap2.SquareUpdateTask)this.squareTaskQueue.poll()) {
         var5.release();
      }

      for(PolygonalMap2.IVehicleTask var7 = (PolygonalMap2.IVehicleTask)this.vehicleTaskQueue.poll(); var7 != null; var7 = (PolygonalMap2.IVehicleTask)this.vehicleTaskQueue.poll()) {
         var7.release();
      }

      for(PolygonalMap2.PathRequestTask var9 = (PolygonalMap2.PathRequestTask)this.requestTaskQueue.poll(); var9 != null; var9 = (PolygonalMap2.PathRequestTask)this.requestTaskQueue.poll()) {
         var9.release();
      }

      while(!this.requests.isEmpty()) {
         ((PolygonalMap2.PathFindRequest)this.requests.removeLast()).release();
      }

      while(!this.requestToMain.isEmpty()) {
         ((PolygonalMap2.PathFindRequest)this.requestToMain.remove()).release();
      }

      for(var1 = 0; var1 < this.vehicles.size(); ++var1) {
         PolygonalMap2.Vehicle var6 = (PolygonalMap2.Vehicle)this.vehicles.get(var1);
         var6.release();
      }

      Iterator var10 = this.vehicleState.values().iterator();

      while(var10.hasNext()) {
         PolygonalMap2.VehicleState var8 = (PolygonalMap2.VehicleState)var10.next();
         var8.release();
      }

      this.requestMap.clear();
      this.vehicles.clear();
      this.vehicleState.clear();
      this.vehicleMap.clear();
      this.cells = null;
      this.thread = null;
      this.rebuild = true;
   }

   public void updateMain() {
      ArrayList var1 = IsoWorld.instance.CurrentCell.getVehicles();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         BaseVehicle var3 = (BaseVehicle)var1.get(var2);
         PolygonalMap2.VehicleState var4 = (PolygonalMap2.VehicleState)this.vehicleState.get(var3);
         if (var4 != null && var4.check()) {
            this.updateVehicle(var3);
         }
      }

      for(PolygonalMap2.PathFindRequest var5 = (PolygonalMap2.PathFindRequest)this.requestToMain.poll(); var5 != null; var5 = (PolygonalMap2.PathFindRequest)this.requestToMain.poll()) {
         if (this.requestMap.get(var5.mover) == var5) {
            this.requestMap.remove(var5.mover);
         }

         if (!var5.cancel) {
            if (var5.path.isEmpty()) {
               var5.finder.Failed(var5.mover);
            } else {
               var5.finder.Succeeded(var5.path, var5.mover);
            }
         }

         var5.release();
      }

   }

   public void updateThread() {
      for(PolygonalMap2.IChunkTask var1 = (PolygonalMap2.IChunkTask)this.chunkTaskQueue.poll(); var1 != null; var1 = (PolygonalMap2.IChunkTask)this.chunkTaskQueue.poll()) {
         var1.execute();
         var1.release();
         this.rebuild = true;
      }

      for(PolygonalMap2.SquareUpdateTask var10 = (PolygonalMap2.SquareUpdateTask)this.squareTaskQueue.poll(); var10 != null; var10 = (PolygonalMap2.SquareUpdateTask)this.squareTaskQueue.poll()) {
         var10.execute();
         var10.release();
      }

      for(PolygonalMap2.IVehicleTask var11 = (PolygonalMap2.IVehicleTask)this.vehicleTaskQueue.poll(); var11 != null; var11 = (PolygonalMap2.IVehicleTask)this.vehicleTaskQueue.poll()) {
         var11.execute();
         var11.release();
         this.rebuild = true;
      }

      for(PolygonalMap2.PathRequestTask var13 = (PolygonalMap2.PathRequestTask)this.requestTaskQueue.poll(); var13 != null; var13 = (PolygonalMap2.PathRequestTask)this.requestTaskQueue.poll()) {
         var13.execute();
         var13.release();
      }

      int var14;
      if (this.rebuild) {
         for(var14 = 0; var14 < this.graphs.size(); ++var14) {
            PolygonalMap2.VisibilityGraph var2 = (PolygonalMap2.VisibilityGraph)this.graphs.get(var14);
            var2.release();
         }

         this.squareToNode.forEachValue(this.releaseNodeProc);
         this.createVisibilityGraphs();
         this.rebuild = false;
         ++PolygonalMap2.ChunkDataZ.EPOCH;
      }

      var14 = 2;

      while(!this.requests.isEmpty()) {
         PolygonalMap2.PathFindRequest var12 = (PolygonalMap2.PathFindRequest)this.requests.removeFirst();
         if (var12.cancel) {
            this.requestToMain.add(var12);
         } else {
            try {
               this.findPath(var12, false);
            } catch (Exception var9) {
               ExceptionLogger.logException(var9);
            }

            if (!var12.targetXYZ.isEmpty()) {
               this.shortestPath.copyFrom(var12.path);
               float var3 = var12.targetX;
               float var4 = var12.targetY;
               float var5 = var12.targetZ;
               float var6 = this.shortestPath.isEmpty() ? Float.MAX_VALUE : this.shortestPath.length();

               for(int var7 = 0; var7 < var12.targetXYZ.size(); var7 += 3) {
                  var12.targetX = var12.targetXYZ.get(var7);
                  var12.targetY = var12.targetXYZ.get(var7 + 1);
                  var12.targetZ = var12.targetXYZ.get(var7 + 2);
                  var12.path.clear();
                  this.findPath(var12, false);
                  if (!var12.path.isEmpty()) {
                     float var8 = var12.path.length();
                     if (var8 < var6) {
                        var6 = var8;
                        this.shortestPath.copyFrom(var12.path);
                        var3 = var12.targetX;
                        var4 = var12.targetY;
                        var5 = var12.targetZ;
                     }
                  }
               }

               var12.path.copyFrom(this.shortestPath);
               var12.targetX = var3;
               var12.targetY = var4;
               var12.targetZ = var5;
            }

            this.requestToMain.add(var12);
            --var14;
            if (var14 == 0) {
               break;
            }
         }
      }

   }

   public PolygonalMap2.PathFindRequest addRequest(PolygonalMap2.IPathfinder var1, Mover var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.cancelRequest(var2);
      PolygonalMap2.PathFindRequest var9 = PolygonalMap2.PathFindRequest.alloc().init(var1, var2, var3, var4, var5, var6, var7, var8);
      this.requestMap.put(var2, var9);
      PolygonalMap2.PathRequestTask var10 = PolygonalMap2.PathRequestTask.alloc().init(this, var9);
      this.requestTaskQueue.add(var10);
      this.thread.wake();
      return var9;
   }

   public void cancelRequest(Mover var1) {
      PolygonalMap2.PathFindRequest var2 = (PolygonalMap2.PathFindRequest)this.requestMap.remove(var1);
      if (var2 != null) {
         var2.cancel = true;
      }

   }

   public ArrayList getPointInLine(float var1, float var2, float var3, float var4, int var5) {
      PolygonalMap2.PointPool var6 = new PolygonalMap2.PointPool();
      ArrayList var7 = new ArrayList();
      this.supercover(var1, var2, var3, var4, var5, var6, var7);
      return var7;
   }

   private void supercover(float var1, float var2, float var3, float var4, int var5, PolygonalMap2.PointPool var6, ArrayList var7) {
      double var8 = (double)Math.abs(var3 - var1);
      double var10 = (double)Math.abs(var4 - var2);
      int var12 = (int)Math.floor((double)var1);
      int var13 = (int)Math.floor((double)var2);
      int var14 = 1;
      byte var15;
      double var17;
      if (var8 == 0.0D) {
         var15 = 0;
         var17 = Double.POSITIVE_INFINITY;
      } else if (var3 > var1) {
         var15 = 1;
         var14 += (int)Math.floor((double)var3) - var12;
         var17 = (Math.floor((double)var1) + 1.0D - (double)var1) * var10;
      } else {
         var15 = -1;
         var14 += var12 - (int)Math.floor((double)var3);
         var17 = ((double)var1 - Math.floor((double)var1)) * var10;
      }

      byte var16;
      if (var10 == 0.0D) {
         var16 = 0;
         var17 -= Double.POSITIVE_INFINITY;
      } else if (var4 > var2) {
         var16 = 1;
         var14 += (int)Math.floor((double)var4) - var13;
         var17 -= (Math.floor((double)var2) + 1.0D - (double)var2) * var8;
      } else {
         var16 = -1;
         var14 += var13 - (int)Math.floor((double)var4);
         var17 -= ((double)var2 - Math.floor((double)var2)) * var8;
      }

      for(; var14 > 0; --var14) {
         PolygonalMap2.Point var19 = var6.alloc().init(var12, var13);
         if (var7.contains(var19)) {
            var6.release(var19);
         } else {
            var7.add(var19);
         }

         if (var17 > 0.0D) {
            var13 += var16;
            var17 -= var8;
         } else {
            var12 += var15;
            var17 += var10;
         }
      }

   }

   public boolean lineClearCollide(float var1, float var2, float var3, float var4, int var5) {
      return this.lineClearCollide(var1, var2, var3, var4, var5, (IsoMovingObject)null);
   }

   public boolean lineClearCollide(float var1, float var2, float var3, float var4, int var5, IsoMovingObject var6) {
      return this.lineClearCollide(var1, var2, var3, var4, var5, var6, true, true);
   }

   public boolean lineClearCollide(float var1, float var2, float var3, float var4, int var5, IsoMovingObject var6, boolean var7, boolean var8) {
      int var9 = 0;
      if (var7) {
         var9 |= 1;
      }

      if (var8) {
         var9 |= 2;
      }

      if (Core.bDebug && DebugOptions.instance.PolymapRenderLineClearCollide.getValue()) {
         var9 |= 8;
      }

      return this.lineClearCollide(var1, var2, var3, var4, var5, var6, var9);
   }

   public boolean lineClearCollide(float var1, float var2, float var3, float var4, int var5, IsoMovingObject var6, int var7) {
      BaseVehicle var8 = null;
      if (var6 instanceof IsoGameCharacter) {
         var8 = ((IsoGameCharacter)var6).getVehicle();
      } else if (var6 instanceof BaseVehicle) {
         var8 = (BaseVehicle)var6;
      }

      return this.lccMain.isNotClear(this, var1, var2, var3, var4, var5, var8, var7);
   }

   public Vector2 getCollidepoint(float var1, float var2, float var3, float var4, int var5, IsoMovingObject var6, int var7) {
      BaseVehicle var8 = null;
      if (var6 instanceof IsoGameCharacter) {
         var8 = ((IsoGameCharacter)var6).getVehicle();
      } else if (var6 instanceof BaseVehicle) {
         var8 = (BaseVehicle)var6;
      }

      return this.lccMain.getCollidepoint(this, var1, var2, var3, var4, var5, var8, var7);
   }

   public boolean canStandAt(float var1, float var2, int var3, IsoMovingObject var4, boolean var5, boolean var6) {
      BaseVehicle var7 = null;
      if (var4 instanceof IsoGameCharacter) {
         var7 = ((IsoGameCharacter)var4).getVehicle();
      } else if (var4 instanceof BaseVehicle) {
         var7 = (BaseVehicle)var4;
      }

      int var8 = 0;
      if (var5) {
         var8 |= 1;
      }

      if (var6) {
         var8 |= 2;
      }

      if (Core.bDebug && DebugOptions.instance.PolymapRenderLineClearCollide.getValue()) {
         var8 |= 8;
      }

      return this.canStandAt(var1, var2, var3, var7, var8);
   }

   public boolean canStandAt(float var1, float var2, int var3, BaseVehicle var4, int var5) {
      return this.lccMain.canStandAtOld(this, var1, var2, (float)var3, var4, var5);
   }

   public boolean intersectLineWithVehicle(float var1, float var2, float var3, float var4, BaseVehicle var5, Vector2 var6) {
      if (var5 != null && var5.getScript() != null) {
         float[] var7 = this.tempFloats;
         var7[0] = var5.getPoly().x1;
         var7[1] = var5.getPoly().y1;
         var7[2] = var5.getPoly().x2;
         var7[3] = var5.getPoly().y2;
         var7[4] = var5.getPoly().x3;
         var7[5] = var5.getPoly().y3;
         var7[6] = var5.getPoly().x4;
         var7[7] = var5.getPoly().y4;
         float var8 = Float.MAX_VALUE;

         for(int var9 = 0; var9 < 8; var9 += 2) {
            float var10 = var7[var9 % 8];
            float var11 = var7[(var9 + 1) % 8];
            float var12 = var7[(var9 + 2) % 8];
            float var13 = var7[(var9 + 3) % 8];
            double var14 = (double)((var13 - var11) * (var3 - var1) - (var12 - var10) * (var4 - var2));
            if (var14 == 0.0D) {
               return false;
            }

            double var16 = (double)((var12 - var10) * (var2 - var11) - (var13 - var11) * (var1 - var10)) / var14;
            double var18 = (double)((var3 - var1) * (var2 - var11) - (var4 - var2) * (var1 - var10)) / var14;
            if (var16 >= 0.0D && var16 <= 1.0D && var18 >= 0.0D && var18 <= 1.0D) {
               float var20 = (float)((double)var1 + var16 * (double)(var3 - var1));
               float var21 = (float)((double)var2 + var16 * (double)(var4 - var2));
               float var22 = IsoUtils.DistanceTo(var1, var2, var20, var21);
               if (var22 < var8) {
                  var6.set(var20, var21);
                  var8 = var22;
               }
            }
         }

         return var8 < Float.MAX_VALUE;
      } else {
         return false;
      }
   }

   public Vector2f resolveCollision(IsoGameCharacter var1, float var2, float var3, Vector2f var4) {
      return GameClient.bClient && var1.isSkipResolveCollision() ? var4.set(var2, var3) : this.collideWithObstacles.resolveCollision(var1, var2, var3, var4);
   }

   private static final class ClosestPointOnEdge {
      PolygonalMap2.Edge edge;
      PolygonalMap2.Node node;
      final Vector2f point = new Vector2f();
      double distSq;
   }

   private static final class AdjustStartEndNodeData {
      PolygonalMap2.Obstacle obstacle;
      PolygonalMap2.Node node;
      PolygonalMap2.Edge newEdge;
      boolean isNodeNew;
      PolygonalMap2.VisibilityGraph graph;
   }

   private static final class LineClearCollide {
      final Vector2 perp = new Vector2();
      final ArrayList pts = new ArrayList();
      final PolygonalMap2.VehicleRect sweepAABB = new PolygonalMap2.VehicleRect();
      final PolygonalMap2.VehicleRect vehicleAABB = new PolygonalMap2.VehicleRect();
      final Vector2[] polyVec = new Vector2[4];
      final Vector2[] vehicleVec = new Vector2[4];
      final PolygonalMap2.PointPool pointPool = new PolygonalMap2.PointPool();
      final PolygonalMap2.LiangBarsky LB = new PolygonalMap2.LiangBarsky();

      LineClearCollide() {
         for(int var1 = 0; var1 < 4; ++var1) {
            this.polyVec[var1] = new Vector2();
            this.vehicleVec[var1] = new Vector2();
         }

      }

      private float clamp(float var1, float var2, float var3) {
         if (var1 < var2) {
            var1 = var2;
         }

         if (var1 > var3) {
            var1 = var3;
         }

         return var1;
      }

      /** @deprecated */
      @Deprecated
      boolean canStandAt(PolygonalMap2 var1, float var2, float var3, float var4, float var5, float var6, PolygonalMap2.Vehicle var7) {
         if (((int)var2 != (int)var4 || (int)var3 != (int)var5) && var1.isBlockedInAllDirections((int)var4, (int)var5, (int)var6)) {
            return false;
         } else {
            int var8 = (int)Math.floor((double)(var4 - 0.3F));
            int var9 = (int)Math.floor((double)(var5 - 0.3F));
            int var10 = (int)Math.ceil((double)(var4 + 0.3F));
            int var11 = (int)Math.ceil((double)(var5 + 0.3F));

            int var12;
            for(var12 = var9; var12 < var11; ++var12) {
               for(int var13 = var8; var13 < var10; ++var13) {
                  PolygonalMap2.Square var14 = var1.getSquare(var13, var12, (int)var6);
                  boolean var15 = var4 >= (float)var13 && var5 >= (float)var12 && var4 < (float)(var13 + 1) && var5 < (float)(var12 + 1);
                  boolean var16 = false;
                  if (!var15 && var14 != null && var14.has(448)) {
                     var16 = var4 < (float)var14.x || var4 >= (float)(var14.x + 1) || var14.has(64) && var5 < (float)var14.y;
                  } else if (!var15 && var14 != null && var14.has(56)) {
                     var16 = var5 < (float)var14.y || var5 >= (float)(var14.y + 1) || var14.has(8) && var4 < (float)var14.x;
                  }

                  if ((var14 == null || var14.isReallySolid() || var16 || !var14.has(512)) && var15) {
                     return false;
                  }
               }
            }

            for(var12 = 0; var12 < var1.vehicles.size(); ++var12) {
               PolygonalMap2.Vehicle var17 = (PolygonalMap2.Vehicle)var1.vehicles.get(var12);
               if (var17 != var7 && (int)var17.polyPlusRadius.z == (int)var6 && var17.polyPlusRadius.containsPoint(var4, var5)) {
                  return false;
               }
            }

            return true;
         }
      }

      boolean canStandAtClipper(PolygonalMap2 var1, float var2, float var3, float var4, float var5, float var6, PolygonalMap2.Vehicle var7, int var8) {
         if (((int)var2 != (int)var4 || (int)var3 != (int)var5) && var1.isBlockedInAllDirections((int)var4, (int)var5, (int)var6)) {
            return false;
         } else {
            PolygonalMap2.Chunk var9 = var1.getChunkFromSquarePos((int)var4, (int)var5);
            if (var9 == null) {
               return false;
            } else {
               PolygonalMap2.ChunkDataZ var10 = var9.collision.init(var9, (int)var6);

               for(int var11 = 0; var11 < var10.obstacles.size(); ++var11) {
                  PolygonalMap2.Obstacle var12 = (PolygonalMap2.Obstacle)var10.obstacles.get(var11);
                  if ((var7 == null || var12.vehicle != var7) && var12.bounds.containsPoint(var4, var5) && var12.isPointInside(var4, var5, var8)) {
                     return false;
                  }
               }

               return true;
            }
         }
      }

      float isLeft(float var1, float var2, float var3, float var4, float var5, float var6) {
         return (var3 - var1) * (var6 - var2) - (var5 - var1) * (var4 - var2);
      }

      boolean isPointInPolygon_WindingNumber(float var1, float var2, PolygonalMap2.VehiclePoly var3) {
         this.polyVec[0].set(var3.x1, var3.y1);
         this.polyVec[1].set(var3.x2, var3.y2);
         this.polyVec[2].set(var3.x3, var3.y3);
         this.polyVec[3].set(var3.x4, var3.y4);
         int var4 = 0;

         for(int var5 = 0; var5 < 4; ++var5) {
            Vector2 var6 = this.polyVec[var5];
            Vector2 var7 = var5 == 3 ? this.polyVec[0] : this.polyVec[var5 + 1];
            if (var6.y <= var2) {
               if (var7.y > var2 && this.isLeft(var6.x, var6.y, var7.x, var7.y, var1, var2) > 0.0F) {
                  ++var4;
               }
            } else if (var7.y <= var2 && this.isLeft(var6.x, var6.y, var7.x, var7.y, var1, var2) < 0.0F) {
               --var4;
            }
         }

         return var4 != 0;
      }

      /** @deprecated */
      @Deprecated
      boolean isNotClearOld(PolygonalMap2 var1, float var2, float var3, float var4, float var5, int var6, int var7) {
         boolean var8 = (var7 & 4) != 0;
         PolygonalMap2.Square var9 = var1.getSquare((int)var2, (int)var3, var6);
         if (var9 != null && var9.has(504)) {
            return true;
         } else if (!this.canStandAt(var1, var2, var3, var4, var5, (float)var6, (PolygonalMap2.Vehicle)null)) {
            return true;
         } else {
            float var10 = var5 - var3;
            float var11 = -(var4 - var2);
            this.perp.set(var10, var11);
            this.perp.normalize();
            float var12 = var2 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
            float var13 = var3 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
            float var14 = var4 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
            float var15 = var5 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
            this.perp.set(-var10, -var11);
            this.perp.normalize();
            float var16 = var2 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
            float var17 = var3 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
            float var18 = var4 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
            float var19 = var5 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;

            int var20;
            for(var20 = 0; var20 < this.pts.size(); ++var20) {
               this.pointPool.release((PolygonalMap2.Point)this.pts.get(var20));
            }

            this.pts.clear();
            this.pts.add(this.pointPool.alloc().init((int)var2, (int)var3));
            if ((int)var2 != (int)var4 || (int)var3 != (int)var5) {
               this.pts.add(this.pointPool.alloc().init((int)var4, (int)var5));
            }

            var1.supercover(var12, var13, var14, var15, var6, this.pointPool, this.pts);
            var1.supercover(var16, var17, var18, var19, var6, this.pointPool, this.pts);

            float var22;
            float var23;
            float var24;
            for(var20 = 0; var20 < this.pts.size(); ++var20) {
               PolygonalMap2.Point var21 = (PolygonalMap2.Point)this.pts.get(var20);
               var9 = var1.getSquare(var21.x, var21.y, var6);
               if (var8 && var9 != null && var9.cost > 0) {
                  return true;
               }

               float var25;
               if (var9 != null && !var9.isReallySolid() && !var9.has(504) && var9.has(512)) {
                  if (var9.isCollideW()) {
                     var22 = 0.3F;
                     var23 = 0.3F;
                     var24 = 0.3F;
                     var25 = 0.3F;
                     if (var2 < (float)var21.x && var4 < (float)var21.x) {
                        var22 = 0.0F;
                     } else if (var2 >= (float)var21.x && var4 >= (float)var21.x) {
                        var24 = 0.0F;
                     }

                     if (var3 < (float)var21.y && var5 < (float)var21.y) {
                        var23 = 0.0F;
                     } else if (var3 >= (float)(var21.y + 1) && var5 >= (float)(var21.y + 1)) {
                        var25 = 0.0F;
                     }

                     if (this.LB.lineRectIntersect(var2, var3, var4 - var2, var5 - var3, (float)var21.x - var22, (float)var21.y - var23, (float)var21.x + var24, (float)var21.y + 1.0F + var25)) {
                        return true;
                     }
                  }

                  if (var9.isCollideN()) {
                     var22 = 0.3F;
                     var23 = 0.3F;
                     var24 = 0.3F;
                     var25 = 0.3F;
                     if (var2 < (float)var21.x && var4 < (float)var21.x) {
                        var22 = 0.0F;
                     } else if (var2 >= (float)(var21.x + 1) && var4 >= (float)(var21.x + 1)) {
                        var24 = 0.0F;
                     }

                     if (var3 < (float)var21.y && var5 < (float)var21.y) {
                        var23 = 0.0F;
                     } else if (var3 >= (float)var21.y && var5 >= (float)var21.y) {
                        var25 = 0.0F;
                     }

                     if (this.LB.lineRectIntersect(var2, var3, var4 - var2, var5 - var3, (float)var21.x - var22, (float)var21.y - var23, (float)var21.x + 1.0F + var24, (float)var21.y + var25)) {
                        return true;
                     }
                  }
               } else {
                  var22 = 0.3F;
                  var23 = 0.3F;
                  var24 = 0.3F;
                  var25 = 0.3F;
                  if (var2 < (float)var21.x && var4 < (float)var21.x) {
                     var22 = 0.0F;
                  } else if (var2 >= (float)(var21.x + 1) && var4 >= (float)(var21.x + 1)) {
                     var24 = 0.0F;
                  }

                  if (var3 < (float)var21.y && var5 < (float)var21.y) {
                     var23 = 0.0F;
                  } else if (var3 >= (float)(var21.y + 1) && var5 >= (float)(var21.y + 1)) {
                     var25 = 0.0F;
                  }

                  if (this.LB.lineRectIntersect(var2, var3, var4 - var2, var5 - var3, (float)var21.x - var22, (float)var21.y - var23, (float)var21.x + 1.0F + var24, (float)var21.y + 1.0F + var25)) {
                     return true;
                  }
               }
            }

            float var28 = BaseVehicle.PLUS_RADIUS;
            this.perp.set(var10, var11);
            this.perp.normalize();
            var12 = var2 + this.perp.x * var28;
            var13 = var3 + this.perp.y * var28;
            var14 = var4 + this.perp.x * var28;
            var15 = var5 + this.perp.y * var28;
            this.perp.set(-var10, -var11);
            this.perp.normalize();
            var16 = var2 + this.perp.x * var28;
            var17 = var3 + this.perp.y * var28;
            var18 = var4 + this.perp.x * var28;
            var19 = var5 + this.perp.y * var28;
            float var29 = Math.min(var12, Math.min(var14, Math.min(var16, var18)));
            var22 = Math.min(var13, Math.min(var15, Math.min(var17, var19)));
            var23 = Math.max(var12, Math.max(var14, Math.max(var16, var18)));
            var24 = Math.max(var13, Math.max(var15, Math.max(var17, var19)));
            this.sweepAABB.init((int)var29, (int)var22, (int)Math.ceil((double)var23) - (int)var29, (int)Math.ceil((double)var24) - (int)var22, var6);
            this.polyVec[0].set(var12, var13);
            this.polyVec[1].set(var14, var15);
            this.polyVec[2].set(var18, var19);
            this.polyVec[3].set(var16, var17);

            for(int var30 = 0; var30 < var1.vehicles.size(); ++var30) {
               PolygonalMap2.Vehicle var26 = (PolygonalMap2.Vehicle)var1.vehicles.get(var30);
               PolygonalMap2.VehicleRect var27 = var26.poly.getAABB(this.vehicleAABB);
               if (var27.intersects(this.sweepAABB) && this.polyVehicleIntersect(var26.poly)) {
                  return true;
               }
            }

            return false;
         }
      }

      boolean isNotClearClipper(PolygonalMap2 var1, float var2, float var3, float var4, float var5, int var6, int var7) {
         boolean var8 = (var7 & 4) != 0;
         PolygonalMap2.Square var9 = var1.getSquare((int)var2, (int)var3, var6);
         if (var9 != null && var9.has(504)) {
            return true;
         } else if (!this.canStandAtClipper(var1, var2, var3, var4, var5, (float)var6, (PolygonalMap2.Vehicle)null, var7)) {
            return true;
         } else {
            float var10 = var2 / 10.0F;
            float var11 = var3 / 10.0F;
            float var12 = var4 / 10.0F;
            float var13 = var5 / 10.0F;
            double var14 = (double)Math.abs(var12 - var10);
            double var16 = (double)Math.abs(var13 - var11);
            int var18 = (int)Math.floor((double)var10);
            int var19 = (int)Math.floor((double)var11);
            int var20 = 1;
            byte var21;
            double var23;
            if (var14 == 0.0D) {
               var21 = 0;
               var23 = Double.POSITIVE_INFINITY;
            } else if (var12 > var10) {
               var21 = 1;
               var20 += (int)Math.floor((double)var12) - var18;
               var23 = (Math.floor((double)var10) + 1.0D - (double)var10) * var16;
            } else {
               var21 = -1;
               var20 += var18 - (int)Math.floor((double)var12);
               var23 = ((double)var10 - Math.floor((double)var10)) * var16;
            }

            byte var22;
            if (var16 == 0.0D) {
               var22 = 0;
               var23 -= Double.POSITIVE_INFINITY;
            } else if (var13 > var11) {
               var22 = 1;
               var20 += (int)Math.floor((double)var13) - var19;
               var23 -= (Math.floor((double)var11) + 1.0D - (double)var11) * var14;
            } else {
               var22 = -1;
               var20 += var19 - (int)Math.floor((double)var13);
               var23 -= ((double)var11 - Math.floor((double)var11)) * var14;
            }

            for(; var20 > 0; --var20) {
               PolygonalMap2.Chunk var25 = PolygonalMap2.instance.getChunkFromChunkPos(var18, var19);
               if (var25 != null) {
                  PolygonalMap2.ChunkDataZ var26 = var25.collision.init(var25, var6);
                  ArrayList var27 = var26.obstacles;

                  for(int var28 = 0; var28 < var27.size(); ++var28) {
                     PolygonalMap2.Obstacle var29 = (PolygonalMap2.Obstacle)var27.get(var28);
                     if (var29.lineSegmentIntersects(var2, var3, var4, var5)) {
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

            if (var8 && this.isNotClearCost(var2, var3, var4, var5, var6)) {
               return true;
            } else {
               return false;
            }
         }
      }

      boolean isNotClearCost(float var1, float var2, float var3, float var4, int var5) {
         float var6 = var4 - var2;
         float var7 = -(var3 - var1);
         this.perp.set(var6, var7);
         this.perp.normalize();
         float var8 = var1 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
         float var9 = var2 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
         float var10 = var3 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
         float var11 = var4 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
         this.perp.set(-var6, -var7);
         this.perp.normalize();
         float var12 = var1 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
         float var13 = var2 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
         float var14 = var3 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
         float var15 = var4 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;

         int var16;
         for(var16 = 0; var16 < this.pts.size(); ++var16) {
            this.pointPool.release((PolygonalMap2.Point)this.pts.get(var16));
         }

         this.pts.clear();
         this.pts.add(this.pointPool.alloc().init((int)var1, (int)var2));
         if ((int)var1 != (int)var3 || (int)var2 != (int)var4) {
            this.pts.add(this.pointPool.alloc().init((int)var3, (int)var4));
         }

         PolygonalMap2.instance.supercover(var8, var9, var10, var11, var5, this.pointPool, this.pts);
         PolygonalMap2.instance.supercover(var12, var13, var14, var15, var5, this.pointPool, this.pts);

         for(var16 = 0; var16 < this.pts.size(); ++var16) {
            PolygonalMap2.Point var17 = (PolygonalMap2.Point)this.pts.get(var16);
            PolygonalMap2.Square var18 = PolygonalMap2.instance.getSquare(var17.x, var17.y, var5);
            if (var18 != null && var18.cost > 0) {
               return true;
            }
         }

         return false;
      }

      boolean isNotClear(PolygonalMap2 var1, float var2, float var3, float var4, float var5, int var6, int var7) {
         return this.isNotClearOld(var1, var2, var3, var4, var5, var6, var7);
      }

      boolean polyVehicleIntersect(PolygonalMap2.VehiclePoly var1) {
         this.vehicleVec[0].set(var1.x1, var1.y1);
         this.vehicleVec[1].set(var1.x2, var1.y2);
         this.vehicleVec[2].set(var1.x3, var1.y3);
         this.vehicleVec[3].set(var1.x4, var1.y4);
         boolean var2 = false;

         for(int var3 = 0; var3 < 4; ++var3) {
            Vector2 var4 = this.polyVec[var3];
            Vector2 var5 = var3 == 3 ? this.polyVec[0] : this.polyVec[var3 + 1];

            for(int var6 = 0; var6 < 4; ++var6) {
               Vector2 var7 = this.vehicleVec[var6];
               Vector2 var8 = var6 == 3 ? this.vehicleVec[0] : this.vehicleVec[var6 + 1];
               if (Line2D.linesIntersect((double)var4.x, (double)var4.y, (double)var5.x, (double)var5.y, (double)var7.x, (double)var7.y, (double)var8.x, (double)var8.y)) {
                  var2 = true;
               }
            }
         }

         return var2;
      }
   }

   private static final class VGAStar extends AStar {
      ArrayList graphs;
      final ArrayList searchNodes = new ArrayList();
      final TIntObjectHashMap nodeMap = new TIntObjectHashMap();
      final PolygonalMap2.GoalNode goalNode = new PolygonalMap2.GoalNode();
      final TIntObjectHashMap squareToNode = new TIntObjectHashMap();
      Mover mover;
      boolean bCanCrawl;
      boolean bIgnoreCrawlCost;
      boolean bCanThump;
      final TIntObjectHashMap knownBlockedEdges = new TIntObjectHashMap();
      final PolygonalMap2.VGAStar.InitProc initProc = new PolygonalMap2.VGAStar.InitProc();

      PolygonalMap2.VGAStar init(ArrayList var1, TIntObjectHashMap var2) {
         this.setMaxSteps(5000);
         this.graphs = var1;
         this.searchNodes.clear();
         this.nodeMap.clear();
         this.squareToNode.clear();
         this.mover = null;
         var2.forEachEntry(this.initProc);
         return this;
      }

      PolygonalMap2.VisibilityGraph getVisGraphForSquare(PolygonalMap2.Square var1) {
         for(int var2 = 0; var2 < this.graphs.size(); ++var2) {
            PolygonalMap2.VisibilityGraph var3 = (PolygonalMap2.VisibilityGraph)this.graphs.get(var2);
            if (var3.contains(var1)) {
               return var3;
            }
         }

         return null;
      }

      boolean isSquareInCluster(PolygonalMap2.Square var1) {
         return this.getVisGraphForSquare(var1) != null;
      }

      PolygonalMap2.SearchNode getSearchNode(PolygonalMap2.Node var1) {
         if (var1.square != null) {
            return this.getSearchNode(var1.square);
         } else {
            PolygonalMap2.SearchNode var2 = (PolygonalMap2.SearchNode)this.nodeMap.get(var1.ID);
            if (var2 == null) {
               var2 = PolygonalMap2.SearchNode.alloc().init(this, var1);
               this.searchNodes.add(var2);
               this.nodeMap.put(var1.ID, var2);
            }

            return var2;
         }
      }

      PolygonalMap2.SearchNode getSearchNode(PolygonalMap2.Square var1) {
         PolygonalMap2.SearchNode var2 = (PolygonalMap2.SearchNode)this.squareToNode.get(var1.ID);
         if (var2 == null) {
            var2 = PolygonalMap2.SearchNode.alloc().init(this, var1);
            this.searchNodes.add(var2);
            this.squareToNode.put(var1.ID, var2);
         }

         return var2;
      }

      PolygonalMap2.SearchNode getSearchNode(int var1, int var2) {
         PolygonalMap2.SearchNode var3 = PolygonalMap2.SearchNode.alloc().init(this, var1, var2);
         this.searchNodes.add(var3);
         return var3;
      }

      ArrayList shortestPath(PolygonalMap2.PathFindRequest var1, PolygonalMap2.SearchNode var2, PolygonalMap2.SearchNode var3) {
         this.mover = var1.mover;
         this.bCanCrawl = var1.bCanCrawl;
         this.bIgnoreCrawlCost = var1.bIgnoreCrawlCost;
         this.bCanThump = var1.bCanThump;
         this.goalNode.init(var3);
         return this.shortestPath(var2, this.goalNode);
      }

      boolean canNotMoveBetween(PolygonalMap2.Square var1, PolygonalMap2.Square var2, boolean var3) {
         assert Math.abs(var1.x - var2.x) <= 1;

         assert Math.abs(var1.y - var2.y) <= 1;

         assert var1.z == var2.z;

         assert var1 != var2;

         boolean var4;
         if (var1.x == 10921 && var1.y == 10137 && var2.x == var1.x - 1 && var2.y == var1.y) {
            var4 = true;
         }

         var4 = var2.x < var1.x;
         boolean var5 = var2.x > var1.x;
         boolean var6 = var2.y < var1.y;
         boolean var7 = var2.y > var1.y;
         if (!var2.isNonThumpableSolid() && (this.bCanThump || !var2.isReallySolid())) {
            if (var2.y < var1.y && var1.has(64)) {
               return true;
            } else if (var2.x < var1.x && var1.has(8)) {
               return true;
            } else if (var2.y > var1.y && var2.x == var1.x && var2.has(64)) {
               return true;
            } else if (var2.x > var1.x && var2.y == var1.y && var2.has(8)) {
               return true;
            } else if (var2.x != var1.x && var2.has(448)) {
               return true;
            } else if (var2.y != var1.y && var2.has(56)) {
               return true;
            } else if (var2.x != var1.x && var1.has(448)) {
               return true;
            } else if (var2.y != var1.y && var1.has(56)) {
               return true;
            } else if (!var2.has(512) && !var2.has(504)) {
               return true;
            } else if (this.isKnownBlocked(var1, var2)) {
               return true;
            } else {
               boolean var8;
               if (var1.x == 11920 && var2.y == 6803 && var2.has(131072)) {
                  var8 = true;
               }

               var8 = var1.isCanPathN() && (this.bCanThump || !var1.isThumpN());
               boolean var9 = var1.isCanPathW() && (this.bCanThump || !var1.isThumpW());
               boolean var10 = var6 && var1.isCollideN() && (var1.x != var2.x || var3 || !var8);
               boolean var11 = var4 && var1.isCollideW() && (var1.y != var2.y || var3 || !var9);
               var8 = var2.isCanPathN() && (this.bCanThump || !var2.isThumpN());
               var9 = var2.isCanPathW() && (this.bCanThump || !var2.isThumpW());
               boolean var12 = var7 && var2.has(131076) && (var1.x != var2.x || var3 || !var8);
               boolean var13 = var5 && var2.has(131074) && (var1.y != var2.y || var3 || !var9);
               if (!var10 && !var11 && !var12 && !var13) {
                  boolean var14 = var2.x != var1.x && var2.y != var1.y;
                  if (var14) {
                     PolygonalMap2.Square var15 = PolygonalMap2.instance.getSquare(var1.x, var2.y, var1.z);
                     PolygonalMap2.Square var16 = PolygonalMap2.instance.getSquare(var2.x, var1.y, var1.z);

                     assert var15 != var1 && var15 != var2;

                     assert var16 != var1 && var16 != var2;

                     if (var2.x == var1.x + 1 && var2.y == var1.y + 1 && var15 != null && var16 != null) {
                        if (var15.has(4096) && var16.has(2048)) {
                           return true;
                        }

                        if (var15.isThumpN() && var16.isThumpW()) {
                           return true;
                        }
                     }

                     if (var2.x == var1.x - 1 && var2.y == var1.y - 1 && var15 != null && var16 != null) {
                        if (var15.has(2048) && var16.has(4096)) {
                           return true;
                        }

                        if (var15.isThumpW() && var16.isThumpN()) {
                           return true;
                        }
                     }

                     if (var15 != null && this.canNotMoveBetween(var1, var15, true)) {
                        return true;
                     }

                     if (var16 != null && this.canNotMoveBetween(var1, var16, true)) {
                        return true;
                     }

                     if (var15 != null && this.canNotMoveBetween(var2, var15, true)) {
                        return true;
                     }

                     if (var16 != null && this.canNotMoveBetween(var2, var16, true)) {
                        return true;
                     }
                  }

                  return false;
               } else {
                  return true;
               }
            }
         } else {
            return true;
         }
      }

      boolean isKnownBlocked(PolygonalMap2.Square var1, PolygonalMap2.Square var2) {
         if (var1.z != var2.z) {
            return false;
         } else {
            KnownBlockedEdges var3 = (KnownBlockedEdges)this.knownBlockedEdges.get(var1.ID);
            KnownBlockedEdges var4 = (KnownBlockedEdges)this.knownBlockedEdges.get(var2.ID);
            if (var3 != null && var3.isBlocked(var2.x, var2.y)) {
               return true;
            } else {
               return var4 != null && var4.isBlocked(var1.x, var1.y);
            }
         }
      }

      final class InitProc implements TIntObjectProcedure {
         public boolean execute(int var1, PolygonalMap2.Node var2) {
            PolygonalMap2.SearchNode var3 = PolygonalMap2.SearchNode.alloc().init(VGAStar.this, (PolygonalMap2.Node)var2);
            var3.square = var2.square;
            VGAStar.this.squareToNode.put(var1, var3);
            VGAStar.this.nodeMap.put(var2.ID, var3);
            VGAStar.this.searchNodes.add(var3);
            return true;
         }
      }
   }

   private static final class TestRequest implements PolygonalMap2.IPathfinder {
      final PolygonalMap2.Path path = new PolygonalMap2.Path();
      boolean done;

      public void Succeeded(PolygonalMap2.Path var1, Mover var2) {
         this.path.copyFrom(var1);
         this.done = true;
      }

      public void Failed(Mover var1) {
         this.path.clear();
         this.done = true;
      }
   }

   public static final class Path {
      final ArrayList nodes = new ArrayList();
      final ArrayDeque nodePool = new ArrayDeque();

      void clear() {
         for(int var1 = 0; var1 < this.nodes.size(); ++var1) {
            this.nodePool.push((PolygonalMap2.PathNode)this.nodes.get(var1));
         }

         this.nodes.clear();
      }

      boolean isEmpty() {
         return this.nodes.isEmpty();
      }

      PolygonalMap2.PathNode addNode(float var1, float var2, float var3) {
         return this.addNode(var1, var2, var3, 0);
      }

      PolygonalMap2.PathNode addNode(float var1, float var2, float var3, int var4) {
         PolygonalMap2.PathNode var5 = this.nodePool.isEmpty() ? new PolygonalMap2.PathNode() : (PolygonalMap2.PathNode)this.nodePool.pop();
         var5.init(var1, var2, var3, var4);
         this.nodes.add(var5);
         return var5;
      }

      PolygonalMap2.PathNode addNode(PolygonalMap2.SearchNode var1) {
         return this.addNode(var1.getX(), var1.getY(), var1.getZ(), var1.vgNode == null ? 0 : var1.vgNode.flags);
      }

      PolygonalMap2.PathNode getNode(int var1) {
         return (PolygonalMap2.PathNode)this.nodes.get(var1);
      }

      PolygonalMap2.PathNode getLastNode() {
         return (PolygonalMap2.PathNode)this.nodes.get(this.nodes.size() - 1);
      }

      void copyFrom(PolygonalMap2.Path var1) {
         assert this != var1;

         this.clear();

         for(int var2 = 0; var2 < var1.nodes.size(); ++var2) {
            PolygonalMap2.PathNode var3 = (PolygonalMap2.PathNode)var1.nodes.get(var2);
            this.addNode(var3.x, var3.y, var3.z, var3.flags);
         }

      }

      float length() {
         float var1 = 0.0F;

         for(int var2 = 0; var2 < this.nodes.size() - 1; ++var2) {
            PolygonalMap2.PathNode var3 = (PolygonalMap2.PathNode)this.nodes.get(var2);
            PolygonalMap2.PathNode var4 = (PolygonalMap2.PathNode)this.nodes.get(var2 + 1);
            var1 += IsoUtils.DistanceTo(var3.x, var3.y, var3.z, var4.x, var4.y, var4.z);
         }

         return var1;
      }

      public boolean crossesSquare(int var1, int var2, int var3) {
         for(int var4 = 0; var4 < this.nodes.size() - 1; ++var4) {
            PolygonalMap2.PathNode var5 = (PolygonalMap2.PathNode)this.nodes.get(var4);
            PolygonalMap2.PathNode var6 = (PolygonalMap2.PathNode)this.nodes.get(var4 + 1);
            if ((int)var5.z == var3 || (int)var6.z == var3) {
               if (Line2D.linesIntersect((double)var5.x, (double)var5.y, (double)var6.x, (double)var6.y, (double)var1, (double)var2, (double)(var1 + 1), (double)var2)) {
                  return true;
               }

               if (Line2D.linesIntersect((double)var5.x, (double)var5.y, (double)var6.x, (double)var6.y, (double)(var1 + 1), (double)var2, (double)(var1 + 1), (double)(var2 + 1))) {
                  return true;
               }

               if (Line2D.linesIntersect((double)var5.x, (double)var5.y, (double)var6.x, (double)var6.y, (double)(var1 + 1), (double)(var2 + 1), (double)var1, (double)(var2 + 1))) {
                  return true;
               }

               if (Line2D.linesIntersect((double)var5.x, (double)var5.y, (double)var6.x, (double)var6.y, (double)var1, (double)(var2 + 1), (double)var1, (double)var2)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private static final class Sync {
      private int fps = 20;
      private long period;
      private long excess;
      private long beforeTime;
      private long overSleepTime;

      private Sync() {
         this.period = 1000000000L / (long)this.fps;
         this.beforeTime = System.nanoTime();
         this.overSleepTime = 0L;
      }

      void begin() {
         this.beforeTime = System.nanoTime();
         this.overSleepTime = 0L;
      }

      void startFrame() {
         this.excess = 0L;
      }

      void endFrame() {
         long var1 = System.nanoTime();
         long var3 = var1 - this.beforeTime;
         long var5 = this.period - var3 - this.overSleepTime;
         if (var5 > 0L) {
            try {
               Thread.sleep(var5 / 1000000L);
            } catch (InterruptedException var8) {
            }

            this.overSleepTime = System.nanoTime() - var1 - var5;
         } else {
            this.excess -= var5;
            this.overSleepTime = 0L;
         }

         this.beforeTime = System.nanoTime();
      }
   }

   private static final class LineClearCollideMain {
      final Vector2 perp = new Vector2();
      final ArrayList pts = new ArrayList();
      final PolygonalMap2.VehicleRect sweepAABB = new PolygonalMap2.VehicleRect();
      final PolygonalMap2.VehicleRect vehicleAABB = new PolygonalMap2.VehicleRect();
      final PolygonalMap2.VehiclePoly vehiclePoly = new PolygonalMap2.VehiclePoly();
      final Vector2[] polyVec = new Vector2[4];
      final Vector2[] vehicleVec = new Vector2[4];
      final PolygonalMap2.PointPool pointPool = new PolygonalMap2.PointPool();
      final PolygonalMap2.LiangBarsky LB = new PolygonalMap2.LiangBarsky();

      LineClearCollideMain() {
         for(int var1 = 0; var1 < 4; ++var1) {
            this.polyVec[var1] = new Vector2();
            this.vehicleVec[var1] = new Vector2();
         }

      }

      private float clamp(float var1, float var2, float var3) {
         if (var1 < var2) {
            var1 = var2;
         }

         if (var1 > var3) {
            var1 = var3;
         }

         return var1;
      }

      /** @deprecated */
      @Deprecated
      boolean canStandAtOld(PolygonalMap2 var1, float var2, float var3, float var4, BaseVehicle var5, int var6) {
         boolean var7 = (var6 & 1) != 0;
         boolean var8 = (var6 & 2) != 0;
         int var9 = (int)Math.floor((double)(var2 - 0.3F));
         int var10 = (int)Math.floor((double)(var3 - 0.3F));
         int var11 = (int)Math.ceil((double)(var2 + 0.3F));
         int var12 = (int)Math.ceil((double)(var3 + 0.3F));

         int var13;
         int var14;
         for(var13 = var10; var13 < var12; ++var13) {
            for(var14 = var9; var14 < var11; ++var14) {
               boolean var15 = var2 >= (float)var14 && var3 >= (float)var13 && var2 < (float)(var14 + 1) && var3 < (float)(var13 + 1);
               IsoGridSquare var16 = IsoWorld.instance.CurrentCell.getGridSquare(var14, var13, (int)var4);
               boolean var17 = false;
               if (!var15 && var16 != null && var16.HasStairsNorth()) {
                  var17 = var2 < (float)var16.x || var2 >= (float)(var16.x + 1) || var16.Has(IsoObjectType.stairsTN) && var3 < (float)var16.y;
               } else if (!var15 && var16 != null && var16.HasStairsWest()) {
                  var17 = var3 < (float)var16.y || var3 >= (float)(var16.y + 1) || var16.Has(IsoObjectType.stairsTW) && var2 < (float)var16.x;
               }

               float var18;
               float var19;
               float var20;
               float var21;
               float var22;
               if (var16 != null && !var16.isSolid() && (!var16.isSolidTrans() || var16.isAdjacentToWindow()) && !var17) {
                  label241: {
                     if (var16.SolidFloorCached) {
                        if (!var16.SolidFloor) {
                           break label241;
                        }
                     } else if (!var16.TreatAsSolidFloor()) {
                        break label241;
                     }

                     if (var8) {
                        continue;
                     }

                     if (var16.Is(IsoFlagType.collideW) || !var7 && var16.hasBlockedDoor(false)) {
                        var18 = (float)var14;
                        var19 = this.clamp(var3, (float)var13, (float)(var13 + 1));
                        var20 = var2 - var18;
                        var21 = var3 - var19;
                        var22 = var20 * var20 + var21 * var21;
                        if (var22 < 0.09F) {
                           return false;
                        }
                     }

                     if (!var16.Is(IsoFlagType.collideN) && (var7 || !var16.hasBlockedDoor(true))) {
                        continue;
                     }

                     var18 = this.clamp(var2, (float)var14, (float)(var14 + 1));
                     var19 = (float)var13;
                     var20 = var2 - var18;
                     var21 = var3 - var19;
                     var22 = var20 * var20 + var21 * var21;
                     if (var22 < 0.09F) {
                        return false;
                     }
                     continue;
                  }
               }

               if (var8) {
                  if (var15) {
                     return false;
                  }
               } else {
                  var18 = this.clamp(var2, (float)var14, (float)(var14 + 1));
                  var19 = this.clamp(var3, (float)var13, (float)(var13 + 1));
                  var20 = var2 - var18;
                  var21 = var3 - var19;
                  var22 = var20 * var20 + var21 * var21;
                  if (var22 < 0.09F) {
                     return false;
                  }
               }
            }
         }

         var13 = ((int)var2 - 4) / 10 - 1;
         var14 = ((int)var3 - 4) / 10 - 1;
         int var23 = (int)Math.ceil((double)((var2 + 4.0F) / 10.0F)) + 1;
         int var24 = (int)Math.ceil((double)((var3 + 4.0F) / 10.0F)) + 1;

         for(int var25 = var14; var25 < var24; ++var25) {
            for(int var26 = var13; var26 < var23; ++var26) {
               IsoChunk var27 = GameServer.bServer ? ServerMap.instance.getChunk(var26, var25) : IsoWorld.instance.CurrentCell.getChunkForGridSquare(var26 * 10, var25 * 10, 0);
               if (var27 != null) {
                  for(int var28 = 0; var28 < var27.vehicles.size(); ++var28) {
                     BaseVehicle var29 = (BaseVehicle)var27.vehicles.get(var28);
                     if (var29 != var5 && var29.addedToWorld && (int)var29.z == (int)var4 && var29.getPolyPlusRadius().containsPoint(var2, var3)) {
                        return false;
                     }
                  }
               }
            }
         }

         return true;
      }

      boolean canStandAtClipper(PolygonalMap2 var1, float var2, float var3, float var4, BaseVehicle var5, int var6) {
         return PolygonalMap2.instance.collideWithObstaclesPoly.canStandAt(var2, var3, var4, var5, var6);
      }

      public void drawCircle(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
         LineDrawer.DrawIsoCircle(var1, var2, var3, var4, 16, var5, var6, var7, var8);
      }

      boolean isNotClearOld(PolygonalMap2 var1, float var2, float var3, float var4, float var5, int var6, BaseVehicle var7, int var8) {
         boolean var9 = (var8 & 1) != 0;
         boolean var10 = (var8 & 2) != 0;
         boolean var11 = (var8 & 4) != 0;
         boolean var12 = (var8 & 8) != 0;
         IsoGridSquare var13 = IsoWorld.instance.CurrentCell.getGridSquare((int)var2, (int)var3, var6);
         if (var13 != null && var13.HasStairs()) {
            return !var13.isSameStaircase((int)var4, (int)var5, var6);
         } else if (!this.canStandAtOld(var1, var4, var5, (float)var6, var7, var8)) {
            if (var12) {
               this.drawCircle(var4, var5, (float)var6, 0.3F, 1.0F, 0.0F, 0.0F, 1.0F);
            }

            return true;
         } else {
            float var14 = var5 - var3;
            float var15 = -(var4 - var2);
            this.perp.set(var14, var15);
            this.perp.normalize();
            float var16 = var2 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
            float var17 = var3 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
            float var18 = var4 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
            float var19 = var5 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
            this.perp.set(-var14, -var15);
            this.perp.normalize();
            float var20 = var2 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
            float var21 = var3 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
            float var22 = var4 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
            float var23 = var5 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;

            int var24;
            for(var24 = 0; var24 < this.pts.size(); ++var24) {
               this.pointPool.release((PolygonalMap2.Point)this.pts.get(var24));
            }

            this.pts.clear();
            this.pts.add(this.pointPool.alloc().init((int)var2, (int)var3));
            if ((int)var2 != (int)var4 || (int)var3 != (int)var5) {
               this.pts.add(this.pointPool.alloc().init((int)var4, (int)var5));
            }

            var1.supercover(var16, var17, var18, var19, var6, this.pointPool, this.pts);
            var1.supercover(var20, var21, var22, var23, var6, this.pointPool, this.pts);
            if (var12) {
               for(var24 = 0; var24 < this.pts.size(); ++var24) {
                  PolygonalMap2.Point var25 = (PolygonalMap2.Point)this.pts.get(var24);
                  LineDrawer.addLine((float)var25.x, (float)var25.y, (float)var6, (float)var25.x + 1.0F, (float)var25.y + 1.0F, (float)var6, 1.0F, 1.0F, 0.0F, (String)null, false);
               }
            }

            boolean var39 = false;

            float var27;
            float var28;
            float var29;
            for(int var40 = 0; var40 < this.pts.size(); ++var40) {
               PolygonalMap2.Point var26 = (PolygonalMap2.Point)this.pts.get(var40);
               var13 = IsoWorld.instance.CurrentCell.getGridSquare(var26.x, var26.y, var6);
               if (var11 && var13 != null && PolygonalMap2.SquareUpdateTask.getCost(var13) > 0) {
                  return true;
               }

               float var30;
               if (var13 != null && !var13.isSolid() && (!var13.isSolidTrans() || var13.isAdjacentToWindow()) && !var13.HasStairs()) {
                  label311: {
                     if (var13.SolidFloorCached) {
                        if (!var13.SolidFloor) {
                           break label311;
                        }
                     } else if (!var13.TreatAsSolidFloor()) {
                        break label311;
                     }

                     if (var13.Is(IsoFlagType.collideW) || !var9 && var13.hasBlockedDoor(false)) {
                        var27 = 0.3F;
                        var28 = 0.3F;
                        var29 = 0.3F;
                        var30 = 0.3F;
                        if (var2 < (float)var26.x && var4 < (float)var26.x) {
                           var27 = 0.0F;
                        } else if (var2 >= (float)var26.x && var4 >= (float)var26.x) {
                           var29 = 0.0F;
                        }

                        if (var3 < (float)var26.y && var5 < (float)var26.y) {
                           var28 = 0.0F;
                        } else if (var3 >= (float)(var26.y + 1) && var5 >= (float)(var26.y + 1)) {
                           var30 = 0.0F;
                        }

                        if (this.LB.lineRectIntersect(var2, var3, var4 - var2, var5 - var3, (float)var26.x - var27, (float)var26.y - var28, (float)var26.x + var29, (float)var26.y + 1.0F + var30)) {
                           if (!var12) {
                              return true;
                           }

                           LineDrawer.addLine((float)var26.x - var27, (float)var26.y - var28, (float)var6, (float)var26.x + var29, (float)var26.y + 1.0F + var30, (float)var6, 1.0F, 0.0F, 0.0F, (String)null, false);
                           var39 = true;
                        }
                     }

                     if (!var13.Is(IsoFlagType.collideN) && (var9 || !var13.hasBlockedDoor(true))) {
                        continue;
                     }

                     var27 = 0.3F;
                     var28 = 0.3F;
                     var29 = 0.3F;
                     var30 = 0.3F;
                     if (var2 < (float)var26.x && var4 < (float)var26.x) {
                        var27 = 0.0F;
                     } else if (var2 >= (float)(var26.x + 1) && var4 >= (float)(var26.x + 1)) {
                        var29 = 0.0F;
                     }

                     if (var3 < (float)var26.y && var5 < (float)var26.y) {
                        var28 = 0.0F;
                     } else if (var3 >= (float)var26.y && var5 >= (float)var26.y) {
                        var30 = 0.0F;
                     }

                     if (this.LB.lineRectIntersect(var2, var3, var4 - var2, var5 - var3, (float)var26.x - var27, (float)var26.y - var28, (float)var26.x + 1.0F + var29, (float)var26.y + var30)) {
                        if (!var12) {
                           return true;
                        }

                        LineDrawer.addLine((float)var26.x - var27, (float)var26.y - var28, (float)var6, (float)var26.x + 1.0F + var29, (float)var26.y + var30, (float)var6, 1.0F, 0.0F, 0.0F, (String)null, false);
                        var39 = true;
                     }
                     continue;
                  }
               }

               var27 = 0.3F;
               var28 = 0.3F;
               var29 = 0.3F;
               var30 = 0.3F;
               if (var2 < (float)var26.x && var4 < (float)var26.x) {
                  var27 = 0.0F;
               } else if (var2 >= (float)(var26.x + 1) && var4 >= (float)(var26.x + 1)) {
                  var29 = 0.0F;
               }

               if (var3 < (float)var26.y && var5 < (float)var26.y) {
                  var28 = 0.0F;
               } else if (var3 >= (float)(var26.y + 1) && var5 >= (float)(var26.y + 1)) {
                  var30 = 0.0F;
               }

               if (this.LB.lineRectIntersect(var2, var3, var4 - var2, var5 - var3, (float)var26.x - var27, (float)var26.y - var28, (float)var26.x + 1.0F + var29, (float)var26.y + 1.0F + var30)) {
                  if (!var12) {
                     return true;
                  }

                  LineDrawer.addLine((float)var26.x - var27, (float)var26.y - var28, (float)var6, (float)var26.x + 1.0F + var29, (float)var26.y + 1.0F + var30, (float)var6, 1.0F, 0.0F, 0.0F, (String)null, false);
                  var39 = true;
               }
            }

            float var41 = BaseVehicle.PLUS_RADIUS;
            this.perp.set(var14, var15);
            this.perp.normalize();
            var16 = var2 + this.perp.x * var41;
            var17 = var3 + this.perp.y * var41;
            var18 = var4 + this.perp.x * var41;
            var19 = var5 + this.perp.y * var41;
            this.perp.set(-var14, -var15);
            this.perp.normalize();
            var20 = var2 + this.perp.x * var41;
            var21 = var3 + this.perp.y * var41;
            var22 = var4 + this.perp.x * var41;
            var23 = var5 + this.perp.y * var41;
            float var42 = Math.min(var16, Math.min(var18, Math.min(var20, var22)));
            var27 = Math.min(var17, Math.min(var19, Math.min(var21, var23)));
            var28 = Math.max(var16, Math.max(var18, Math.max(var20, var22)));
            var29 = Math.max(var17, Math.max(var19, Math.max(var21, var23)));
            this.sweepAABB.init((int)var42, (int)var27, (int)Math.ceil((double)var28) - (int)var42, (int)Math.ceil((double)var29) - (int)var27, var6);
            this.polyVec[0].set(var16, var17);
            this.polyVec[1].set(var18, var19);
            this.polyVec[2].set(var22, var23);
            this.polyVec[3].set(var20, var21);
            int var43 = this.sweepAABB.left() / 10 - 1;
            int var31 = this.sweepAABB.top() / 10 - 1;
            int var32 = (int)Math.ceil((double)((float)this.sweepAABB.right() / 10.0F)) + 1;
            int var33 = (int)Math.ceil((double)((float)this.sweepAABB.bottom() / 10.0F)) + 1;

            for(int var34 = var31; var34 < var33; ++var34) {
               for(int var35 = var43; var35 < var32; ++var35) {
                  IsoChunk var36 = GameServer.bServer ? ServerMap.instance.getChunk(var35, var34) : IsoWorld.instance.CurrentCell.getChunkForGridSquare(var35 * 10, var34 * 10, 0);
                  if (var36 != null) {
                     for(int var37 = 0; var37 < var36.vehicles.size(); ++var37) {
                        BaseVehicle var38 = (BaseVehicle)var36.vehicles.get(var37);
                        if (var38 != var7 && var38.VehicleID != -1) {
                           this.vehiclePoly.init(var38.getPoly());
                           this.vehiclePoly.getAABB(this.vehicleAABB);
                           if (this.vehicleAABB.intersects(this.sweepAABB) && this.polyVehicleIntersect(this.vehiclePoly, var12)) {
                              var39 = true;
                              if (!var12) {
                                 return true;
                              }
                           }
                        }
                     }
                  }
               }
            }

            return var39;
         }
      }

      boolean isNotClearClipper(PolygonalMap2 var1, float var2, float var3, float var4, float var5, int var6, BaseVehicle var7, int var8) {
         boolean var9 = (var8 & 1) != 0;
         boolean var10 = (var8 & 2) != 0;
         boolean var11 = (var8 & 4) != 0;
         boolean var12 = (var8 & 8) != 0;
         IsoGridSquare var13 = IsoWorld.instance.CurrentCell.getGridSquare((int)var2, (int)var3, var6);
         if (var13 != null && var13.HasStairs()) {
            return !var13.isSameStaircase((int)var4, (int)var5, var6);
         } else if (!this.canStandAtClipper(var1, var4, var5, (float)var6, var7, var8)) {
            if (var12) {
               this.drawCircle(var4, var5, (float)var6, 0.3F, 1.0F, 0.0F, 0.0F, 1.0F);
            }

            return true;
         } else {
            return PolygonalMap2.instance.collideWithObstaclesPoly.isNotClear(var2, var3, var4, var5, var6, var12, var7, var9, var10);
         }
      }

      boolean isNotClear(PolygonalMap2 var1, float var2, float var3, float var4, float var5, int var6, BaseVehicle var7, int var8) {
         return this.isNotClearOld(var1, var2, var3, var4, var5, var6, var7, var8);
      }

      Vector2 getCollidepoint(PolygonalMap2 var1, float var2, float var3, float var4, float var5, int var6, BaseVehicle var7, int var8) {
         boolean var9 = (var8 & 1) != 0;
         boolean var10 = (var8 & 2) != 0;
         boolean var11 = (var8 & 4) != 0;
         boolean var12 = (var8 & 8) != 0;
         float var14 = var5 - var3;
         float var15 = -(var4 - var2);
         this.perp.set(var14, var15);
         this.perp.normalize();
         float var16 = var2 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
         float var17 = var3 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
         float var18 = var4 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
         float var19 = var5 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
         this.perp.set(-var14, -var15);
         this.perp.normalize();
         float var20 = var2 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
         float var21 = var3 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;
         float var22 = var4 + this.perp.x * PolygonalMap2.RADIUS_DIAGONAL;
         float var23 = var5 + this.perp.y * PolygonalMap2.RADIUS_DIAGONAL;

         int var24;
         for(var24 = 0; var24 < this.pts.size(); ++var24) {
            this.pointPool.release((PolygonalMap2.Point)this.pts.get(var24));
         }

         this.pts.clear();
         this.pts.add(this.pointPool.alloc().init((int)var2, (int)var3));
         if ((int)var2 != (int)var4 || (int)var3 != (int)var5) {
            this.pts.add(this.pointPool.alloc().init((int)var4, (int)var5));
         }

         var1.supercover(var16, var17, var18, var19, var6, this.pointPool, this.pts);
         var1.supercover(var20, var21, var22, var23, var6, this.pointPool, this.pts);
         this.pts.sort((var2x, var3x) -> {
            return (int)(IsoUtils.DistanceManhatten(var2, var3, (float)var2x.x, (float)var2x.y) - IsoUtils.DistanceManhatten(var2, var3, (float)var3x.x, (float)var3x.y));
         });
         PolygonalMap2.Point var25;
         if (var12) {
            for(var24 = 0; var24 < this.pts.size(); ++var24) {
               var25 = (PolygonalMap2.Point)this.pts.get(var24);
               LineDrawer.addLine((float)var25.x, (float)var25.y, (float)var6, (float)var25.x + 1.0F, (float)var25.y + 1.0F, (float)var6, 1.0F, 1.0F, 0.0F, (String)null, false);
            }
         }

         for(var24 = 0; var24 < this.pts.size(); ++var24) {
            var25 = (PolygonalMap2.Point)this.pts.get(var24);
            IsoGridSquare var13 = IsoWorld.instance.CurrentCell.getGridSquare(var25.x, var25.y, var6);
            if (var11 && var13 != null && PolygonalMap2.SquareUpdateTask.getCost(var13) > 0) {
               return PolygonalMap2.temp.set((float)var25.x + 0.5F, (float)var25.y + 0.5F);
            }

            float var26;
            float var27;
            float var28;
            float var29;
            if (var13 != null && !var13.isSolid() && (!var13.isSolidTrans() || var13.isAdjacentToWindow()) && !var13.HasStairs()) {
               label249: {
                  if (var13.SolidFloorCached) {
                     if (!var13.SolidFloor) {
                        break label249;
                     }
                  } else if (!var13.TreatAsSolidFloor()) {
                     break label249;
                  }

                  if (var13.Is(IsoFlagType.collideW) || !var9 && var13.hasBlockedDoor(false)) {
                     var26 = 0.3F;
                     var27 = 0.3F;
                     var28 = 0.3F;
                     var29 = 0.3F;
                     if (var2 < (float)var25.x && var4 < (float)var25.x) {
                        var26 = 0.0F;
                     } else if (var2 >= (float)var25.x && var4 >= (float)var25.x) {
                        var28 = 0.0F;
                     }

                     if (var3 < (float)var25.y && var5 < (float)var25.y) {
                        var27 = 0.0F;
                     } else if (var3 >= (float)(var25.y + 1) && var5 >= (float)(var25.y + 1)) {
                        var29 = 0.0F;
                     }

                     if (this.LB.lineRectIntersect(var2, var3, var4 - var2, var5 - var3, (float)var25.x - var26, (float)var25.y - var27, (float)var25.x + var28, (float)var25.y + 1.0F + var29)) {
                        if (var12) {
                           LineDrawer.addLine((float)var25.x - var26, (float)var25.y - var27, (float)var6, (float)var25.x + var28, (float)var25.y + 1.0F + var29, (float)var6, 1.0F, 0.0F, 0.0F, (String)null, false);
                        }

                        return PolygonalMap2.temp.set((float)var25.x + (var2 - var4 < 0.0F ? -0.5F : 0.5F), (float)var25.y + 0.5F);
                     }
                  }

                  if (!var13.Is(IsoFlagType.collideN) && (var9 || !var13.hasBlockedDoor(true))) {
                     continue;
                  }

                  var26 = 0.3F;
                  var27 = 0.3F;
                  var28 = 0.3F;
                  var29 = 0.3F;
                  if (var2 < (float)var25.x && var4 < (float)var25.x) {
                     var26 = 0.0F;
                  } else if (var2 >= (float)(var25.x + 1) && var4 >= (float)(var25.x + 1)) {
                     var28 = 0.0F;
                  }

                  if (var3 < (float)var25.y && var5 < (float)var25.y) {
                     var27 = 0.0F;
                  } else if (var3 >= (float)var25.y && var5 >= (float)var25.y) {
                     var29 = 0.0F;
                  }

                  if (this.LB.lineRectIntersect(var2, var3, var4 - var2, var5 - var3, (float)var25.x - var26, (float)var25.y - var27, (float)var25.x + 1.0F + var28, (float)var25.y + var29)) {
                     if (var12) {
                        LineDrawer.addLine((float)var25.x - var26, (float)var25.y - var27, (float)var6, (float)var25.x + 1.0F + var28, (float)var25.y + var29, (float)var6, 1.0F, 0.0F, 0.0F, (String)null, false);
                     }

                     return PolygonalMap2.temp.set((float)var25.x + 0.5F, (float)var25.y + (var3 - var5 < 0.0F ? -0.5F : 0.5F));
                  }
                  continue;
               }
            }

            var26 = 0.3F;
            var27 = 0.3F;
            var28 = 0.3F;
            var29 = 0.3F;
            if (var2 < (float)var25.x && var4 < (float)var25.x) {
               var26 = 0.0F;
            } else if (var2 >= (float)(var25.x + 1) && var4 >= (float)(var25.x + 1)) {
               var28 = 0.0F;
            }

            if (var3 < (float)var25.y && var5 < (float)var25.y) {
               var27 = 0.0F;
            } else if (var3 >= (float)(var25.y + 1) && var5 >= (float)(var25.y + 1)) {
               var29 = 0.0F;
            }

            if (this.LB.lineRectIntersect(var2, var3, var4 - var2, var5 - var3, (float)var25.x - var26, (float)var25.y - var27, (float)var25.x + 1.0F + var28, (float)var25.y + 1.0F + var29)) {
               if (var12) {
                  LineDrawer.addLine((float)var25.x - var26, (float)var25.y - var27, (float)var6, (float)var25.x + 1.0F + var28, (float)var25.y + 1.0F + var29, (float)var6, 1.0F, 0.0F, 0.0F, (String)null, false);
               }

               return PolygonalMap2.temp.set((float)var25.x + 0.5F, (float)var25.y + 0.5F);
            }
         }

         return PolygonalMap2.temp.set(var4, var5);
      }

      boolean polyVehicleIntersect(PolygonalMap2.VehiclePoly var1, boolean var2) {
         this.vehicleVec[0].set(var1.x1, var1.y1);
         this.vehicleVec[1].set(var1.x2, var1.y2);
         this.vehicleVec[2].set(var1.x3, var1.y3);
         this.vehicleVec[3].set(var1.x4, var1.y4);
         boolean var3 = false;

         for(int var4 = 0; var4 < 4; ++var4) {
            Vector2 var5 = this.polyVec[var4];
            Vector2 var6 = var4 == 3 ? this.polyVec[0] : this.polyVec[var4 + 1];

            for(int var7 = 0; var7 < 4; ++var7) {
               Vector2 var8 = this.vehicleVec[var7];
               Vector2 var9 = var7 == 3 ? this.vehicleVec[0] : this.vehicleVec[var7 + 1];
               if (Line2D.linesIntersect((double)var5.x, (double)var5.y, (double)var6.x, (double)var6.y, (double)var8.x, (double)var8.y, (double)var9.x, (double)var9.y)) {
                  if (var2) {
                     LineDrawer.addLine(var5.x, var5.y, 0.0F, var6.x, var6.y, 0.0F, 1.0F, 0.0F, 0.0F, (String)null, true);
                     LineDrawer.addLine(var8.x, var8.y, 0.0F, var9.x, var9.y, 0.0F, 1.0F, 0.0F, 0.0F, (String)null, true);
                  }

                  var3 = true;
               }
            }
         }

         return var3;
      }
   }

   private static final class VehicleRect {
      PolygonalMap2.VehicleCluster cluster;
      PolygonalMap2.Vehicle vehicle;
      int x;
      int y;
      int w;
      int h;
      int z;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.VehicleRect init(PolygonalMap2.Vehicle var1, int var2, int var3, int var4, int var5, int var6) {
         this.cluster = null;
         this.vehicle = var1;
         this.x = var2;
         this.y = var3;
         this.w = var4;
         this.h = var5;
         this.z = var6;
         return this;
      }

      PolygonalMap2.VehicleRect init(int var1, int var2, int var3, int var4, int var5) {
         this.cluster = null;
         this.vehicle = null;
         this.x = var1;
         this.y = var2;
         this.w = var3;
         this.h = var4;
         this.z = var5;
         return this;
      }

      int left() {
         return this.x;
      }

      int top() {
         return this.y;
      }

      int right() {
         return this.x + this.w;
      }

      int bottom() {
         return this.y + this.h;
      }

      boolean containsPoint(float var1, float var2, float var3) {
         return var1 >= (float)this.left() && var1 < (float)this.right() && var2 >= (float)this.top() && var2 < (float)this.bottom() && (int)var3 == this.z;
      }

      boolean containsPoint(float var1, float var2, float var3, int var4) {
         int var5 = this.x - var4;
         int var6 = this.y - var4;
         int var7 = this.right() + var4;
         int var8 = this.bottom() + var4;
         return var1 >= (float)var5 && var1 < (float)var7 && var2 >= (float)var6 && var2 < (float)var8 && (int)var3 == this.z;
      }

      boolean intersects(PolygonalMap2.VehicleRect var1) {
         return this.left() < var1.right() && this.right() > var1.left() && this.top() < var1.bottom() && this.bottom() > var1.top();
      }

      boolean isAdjacent(PolygonalMap2.VehicleRect var1) {
         --this.x;
         --this.y;
         this.w += 2;
         this.h += 2;
         boolean var2 = this.intersects(var1);
         ++this.x;
         ++this.y;
         this.w -= 2;
         this.h -= 2;
         return var2;
      }

      static PolygonalMap2.VehicleRect alloc() {
         boolean var0;
         if (pool.isEmpty()) {
            var0 = false;
         } else {
            var0 = false;
         }

         return pool.isEmpty() ? new PolygonalMap2.VehicleRect() : (PolygonalMap2.VehicleRect)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class VehicleCluster {
      int z;
      final ArrayList rects = new ArrayList();
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.VehicleCluster init() {
         this.rects.clear();
         return this;
      }

      void merge(PolygonalMap2.VehicleCluster var1) {
         for(int var2 = 0; var2 < var1.rects.size(); ++var2) {
            PolygonalMap2.VehicleRect var3 = (PolygonalMap2.VehicleRect)var1.rects.get(var2);
            var3.cluster = this;
         }

         this.rects.addAll(var1.rects);
         var1.rects.clear();
      }

      PolygonalMap2.VehicleRect bounds() {
         int var1 = Integer.MAX_VALUE;
         int var2 = Integer.MAX_VALUE;
         int var3 = Integer.MIN_VALUE;
         int var4 = Integer.MIN_VALUE;

         for(int var5 = 0; var5 < this.rects.size(); ++var5) {
            PolygonalMap2.VehicleRect var6 = (PolygonalMap2.VehicleRect)this.rects.get(var5);
            var1 = Math.min(var1, var6.left());
            var2 = Math.min(var2, var6.top());
            var3 = Math.max(var3, var6.right());
            var4 = Math.max(var4, var6.bottom());
         }

         return PolygonalMap2.VehicleRect.alloc().init(var1, var2, var3 - var1, var4 - var2, this.z);
      }

      static PolygonalMap2.VehicleCluster alloc() {
         return pool.isEmpty() ? new PolygonalMap2.VehicleCluster() : (PolygonalMap2.VehicleCluster)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class Vehicle {
      final PolygonalMap2.VehiclePoly poly = new PolygonalMap2.VehiclePoly();
      final PolygonalMap2.VehiclePoly polyPlusRadius = new PolygonalMap2.VehiclePoly();
      final TFloatArrayList crawlOffsets = new TFloatArrayList();
      float upVectorDot;
      static final ArrayDeque pool = new ArrayDeque();

      static PolygonalMap2.Vehicle alloc() {
         return pool.isEmpty() ? new PolygonalMap2.Vehicle() : (PolygonalMap2.Vehicle)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   public static final class VehiclePoly {
      public Transform t = new Transform();
      public float x1;
      public float y1;
      public float x2;
      public float y2;
      public float x3;
      public float y3;
      public float x4;
      public float y4;
      public float z;
      public final Vector2[] borders = new Vector2[4];
      private static final Quaternionf tempQuat = new Quaternionf();

      VehiclePoly() {
         for(int var1 = 0; var1 < this.borders.length; ++var1) {
            this.borders[var1] = new Vector2();
         }

      }

      PolygonalMap2.VehiclePoly init(PolygonalMap2.VehiclePoly var1) {
         this.x1 = var1.x1;
         this.y1 = var1.y1;
         this.x2 = var1.x2;
         this.y2 = var1.y2;
         this.x3 = var1.x3;
         this.y3 = var1.y3;
         this.x4 = var1.x4;
         this.y4 = var1.y4;
         this.z = var1.z;
         return this;
      }

      PolygonalMap2.VehiclePoly init(BaseVehicle var1, float var2) {
         VehicleScript var3 = var1.getScript();
         Vector3f var4 = var3.getExtents();
         Vector3f var5 = var3.getCenterOfMassOffset();
         float var6 = 1.0F;
         Vector2[] var7 = this.borders;
         Quaternionf var8 = tempQuat;
         var1.getWorldTransform(this.t);
         this.t.getRotation(var8);
         float var9 = var4.x * var6 + var2 * 2.0F;
         float var10 = var4.z * var6 + var2 * 2.0F;
         float var11 = var4.y * var6 + var2 * 2.0F;
         var9 /= 2.0F;
         var10 /= 2.0F;
         var11 /= 2.0F;
         Vector3f var12 = PolygonalMap2.tempVec3f_1;
         if (var8.x < 0.0F) {
            var1.getWorldPos(var5.x - var9, 0.0F, var5.z + var10, var12);
            var7[0].set(var12.x, var12.y);
            var1.getWorldPos(var5.x + var9, var11, var5.z + var10, var12);
            var7[1].set(var12.x, var12.y);
            var1.getWorldPos(var5.x + var9, var11, var5.z - var10, var12);
            var7[2].set(var12.x, var12.y);
            var1.getWorldPos(var5.x - var9, 0.0F, var5.z - var10, var12);
            var7[3].set(var12.x, var12.y);
            this.z = var1.z;
         } else {
            var1.getWorldPos(var5.x - var9, var11, var5.z + var10, var12);
            var7[0].set(var12.x, var12.y);
            var1.getWorldPos(var5.x + var9, 0.0F, var5.z + var10, var12);
            var7[1].set(var12.x, var12.y);
            var1.getWorldPos(var5.x + var9, 0.0F, var5.z - var10, var12);
            var7[2].set(var12.x, var12.y);
            var1.getWorldPos(var5.x - var9, var11, var5.z - var10, var12);
            var7[3].set(var12.x, var12.y);
            this.z = var1.z;
         }

         int var13 = 0;

         Vector2 var15;
         Vector2 var16;
         for(int var14 = 0; var14 < var7.length; ++var14) {
            var15 = var7[var14];
            var16 = var7[(var14 + 1) % var7.length];
            var13 = (int)((float)var13 + (var16.x - var15.x) * (var16.y + var15.y));
         }

         if (var13 < 0) {
            Vector2 var17 = var7[1];
            var15 = var7[2];
            var16 = var7[3];
            var7[1] = var16;
            var7[2] = var15;
            var7[3] = var17;
         }

         this.x1 = var7[0].x;
         this.y1 = var7[0].y;
         this.x2 = var7[1].x;
         this.y2 = var7[1].y;
         this.x3 = var7[2].x;
         this.y3 = var7[2].y;
         this.x4 = var7[3].x;
         this.y4 = var7[3].y;
         return this;
      }

      public static Vector2 lineIntersection(Vector2 var0, Vector2 var1, Vector2 var2, Vector2 var3) {
         Vector2 var4 = new Vector2();
         float var5 = var0.y - var1.y;
         float var6 = var1.x - var0.x;
         float var7 = -var5 * var0.x - var6 * var0.y;
         float var8 = var2.y - var3.y;
         float var9 = var3.x - var2.x;
         float var10 = -var8 * var2.x - var9 * var2.y;
         float var11 = QuadranglesIntersection.det(var5, var6, var8, var9);
         if (var11 != 0.0F) {
            var4.x = -QuadranglesIntersection.det(var7, var6, var10, var9) * 1.0F / var11;
            var4.y = -QuadranglesIntersection.det(var5, var7, var8, var10) * 1.0F / var11;
            return var4;
         } else {
            return null;
         }
      }

      PolygonalMap2.VehicleRect getAABB(PolygonalMap2.VehicleRect var1) {
         float var2 = Math.min(this.x1, Math.min(this.x2, Math.min(this.x3, this.x4)));
         float var3 = Math.min(this.y1, Math.min(this.y2, Math.min(this.y3, this.y4)));
         float var4 = Math.max(this.x1, Math.max(this.x2, Math.max(this.x3, this.x4)));
         float var5 = Math.max(this.y1, Math.max(this.y2, Math.max(this.y3, this.y4)));
         return var1.init((PolygonalMap2.Vehicle)null, (int)var2, (int)var3, (int)Math.ceil((double)var4) - (int)var2, (int)Math.ceil((double)var5) - (int)var3, (int)this.z);
      }

      float isLeft(float var1, float var2, float var3, float var4, float var5, float var6) {
         return (var3 - var1) * (var6 - var2) - (var5 - var1) * (var4 - var2);
      }

      public boolean containsPoint(float var1, float var2) {
         int var3 = 0;

         for(int var4 = 0; var4 < 4; ++var4) {
            Vector2 var5 = this.borders[var4];
            Vector2 var6 = var4 == 3 ? this.borders[0] : this.borders[var4 + 1];
            if (var5.y <= var2) {
               if (var6.y > var2 && this.isLeft(var5.x, var5.y, var6.x, var6.y, var1, var2) > 0.0F) {
                  ++var3;
               }
            } else if (var6.y <= var2 && this.isLeft(var5.x, var5.y, var6.x, var6.y, var1, var2) < 0.0F) {
               --var3;
            }
         }

         return var3 != 0;
      }
   }

   private static final class Square {
      static int nextID = 1;
      Integer ID;
      int x;
      int y;
      int z;
      int bits;
      short cost;
      static final ArrayDeque pool = new ArrayDeque();

      Square() {
         this.ID = nextID++;
      }

      PolygonalMap2.Square init(int var1, int var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         return this;
      }

      boolean has(int var1) {
         return (this.bits & var1) != 0;
      }

      boolean isReallySolid() {
         return this.has(1) || this.has(1024) && !this.isAdjacentToWindow();
      }

      boolean isNonThumpableSolid() {
         return this.isReallySolid() && !this.has(131072);
      }

      boolean isCanPathW() {
         if (this.has(8192)) {
            return true;
         } else {
            PolygonalMap2.Square var1 = PolygonalMap2.instance.getSquare(this.x - 1, this.y, this.z);
            return var1 != null && (var1.has(131072) || var1.has(262144));
         }
      }

      boolean isCanPathN() {
         if (this.has(16384)) {
            return true;
         } else {
            PolygonalMap2.Square var1 = PolygonalMap2.instance.getSquare(this.x, this.y - 1, this.z);
            return var1 != null && (var1.has(131072) || var1.has(524288));
         }
      }

      boolean isCollideW() {
         if (this.has(2)) {
            return true;
         } else {
            PolygonalMap2.Square var1 = PolygonalMap2.instance.getSquare(this.x - 1, this.y, this.z);
            return var1 != null && (var1.has(262144) || var1.has(448) || var1.isReallySolid());
         }
      }

      boolean isCollideN() {
         if (this.has(4)) {
            return true;
         } else {
            PolygonalMap2.Square var1 = PolygonalMap2.instance.getSquare(this.x, this.y - 1, this.z);
            return var1 != null && (var1.has(524288) || var1.has(56) || var1.isReallySolid());
         }
      }

      boolean isThumpW() {
         if (this.has(32768)) {
            return true;
         } else {
            PolygonalMap2.Square var1 = PolygonalMap2.instance.getSquare(this.x - 1, this.y, this.z);
            return var1 != null && var1.has(131072);
         }
      }

      boolean isThumpN() {
         if (this.has(65536)) {
            return true;
         } else {
            PolygonalMap2.Square var1 = PolygonalMap2.instance.getSquare(this.x, this.y - 1, this.z);
            return var1 != null && var1.has(131072);
         }
      }

      boolean isAdjacentToWindow() {
         if (!this.has(2048) && !this.has(4096)) {
            PolygonalMap2.Square var1 = PolygonalMap2.instance.getSquare(this.x, this.y + 1, this.z);
            if (var1 != null && var1.has(4096)) {
               return true;
            } else {
               PolygonalMap2.Square var2 = PolygonalMap2.instance.getSquare(this.x + 1, this.y, this.z);
               return var2 != null && var2.has(2048);
            }
         } else {
            return true;
         }
      }

      static PolygonalMap2.Square alloc() {
         return pool.isEmpty() ? new PolygonalMap2.Square() : (PolygonalMap2.Square)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class Node {
      static int nextID = 1;
      final int ID;
      float x;
      float y;
      int z;
      boolean ignore;
      PolygonalMap2.Square square;
      ArrayList graphs;
      final ArrayList edges = new ArrayList();
      final ArrayList visible = new ArrayList();
      int flags = 0;
      static final ArrayList tempObstacles = new ArrayList();
      static final ArrayDeque pool = new ArrayDeque();

      Node() {
         this.ID = nextID++;
      }

      PolygonalMap2.Node init(float var1, float var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.ignore = false;
         this.square = null;
         if (this.graphs != null) {
            this.graphs.clear();
         }

         this.edges.clear();
         this.visible.clear();
         this.flags = 0;
         return this;
      }

      PolygonalMap2.Node init(PolygonalMap2.Square var1) {
         this.x = (float)var1.x + 0.5F;
         this.y = (float)var1.y + 0.5F;
         this.z = var1.z;
         this.ignore = false;
         this.square = var1;
         if (this.graphs != null) {
            this.graphs.clear();
         }

         this.edges.clear();
         this.visible.clear();
         this.flags = 0;
         return this;
      }

      PolygonalMap2.Node setXY(float var1, float var2) {
         this.x = var1;
         this.y = var2;
         return this;
      }

      void addGraph(PolygonalMap2.VisibilityGraph var1) {
         if (this.graphs == null) {
            this.graphs = new ArrayList();
         }

         assert !this.graphs.contains(var1);

         this.graphs.add(var1);
      }

      boolean sharesEdge(PolygonalMap2.Node var1) {
         for(int var2 = 0; var2 < this.edges.size(); ++var2) {
            PolygonalMap2.Edge var3 = (PolygonalMap2.Edge)this.edges.get(var2);
            if (var3.hasNode(var1)) {
               return true;
            }
         }

         return false;
      }

      boolean sharesShape(PolygonalMap2.Node var1) {
         for(int var2 = 0; var2 < this.edges.size(); ++var2) {
            PolygonalMap2.Edge var3 = (PolygonalMap2.Edge)this.edges.get(var2);

            for(int var4 = 0; var4 < var1.edges.size(); ++var4) {
               PolygonalMap2.Edge var5 = (PolygonalMap2.Edge)var1.edges.get(var4);
               if (var3.obstacle != null && var3.obstacle == var5.obstacle) {
                  return true;
               }
            }
         }

         return false;
      }

      void getObstacles(ArrayList var1) {
         for(int var2 = 0; var2 < this.edges.size(); ++var2) {
            PolygonalMap2.Edge var3 = (PolygonalMap2.Edge)this.edges.get(var2);
            if (!var1.contains(var3.obstacle)) {
               var1.add(var3.obstacle);
            }
         }

      }

      boolean onSameShapeButDoesNotShareAnEdge(PolygonalMap2.Node var1) {
         tempObstacles.clear();
         this.getObstacles(tempObstacles);

         for(int var2 = 0; var2 < tempObstacles.size(); ++var2) {
            PolygonalMap2.Obstacle var3 = (PolygonalMap2.Obstacle)tempObstacles.get(var2);
            if (var3.hasNode(var1) && !var3.hasAdjacentNodes(this, var1)) {
               return true;
            }
         }

         return false;
      }

      boolean hasFlag(int var1) {
         return (this.flags & var1) != 0;
      }

      boolean isConnectedTo(PolygonalMap2.Node var1) {
         if (this.hasFlag(4)) {
            return true;
         } else {
            for(int var2 = 0; var2 < this.visible.size(); ++var2) {
               PolygonalMap2.Connection var3 = (PolygonalMap2.Connection)this.visible.get(var2);
               if (var3.node1 == var1 || var3.node2 == var1) {
                  return true;
               }
            }

            return false;
         }
      }

      static PolygonalMap2.Node alloc() {
         boolean var0;
         if (pool.isEmpty()) {
            var0 = false;
         } else {
            var0 = false;
         }

         return pool.isEmpty() ? new PolygonalMap2.Node() : (PolygonalMap2.Node)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         for(int var1 = this.visible.size() - 1; var1 >= 0; --var1) {
            PolygonalMap2.instance.breakConnection((PolygonalMap2.Connection)this.visible.get(var1));
         }

         pool.push(this);
      }

      static void releaseAll(ArrayList var0) {
         for(int var1 = 0; var1 < var0.size(); ++var1) {
            ((PolygonalMap2.Node)var0.get(var1)).release();
         }

      }
   }

   private static final class VisibilityGraph {
      boolean created;
      PolygonalMap2.VehicleCluster cluster;
      final ArrayList nodes = new ArrayList();
      final ArrayList edges = new ArrayList();
      final ArrayList obstacles = new ArrayList();
      final ArrayList intersectNodes = new ArrayList();
      final ArrayList perimeterNodes = new ArrayList();
      final ArrayList perimeterEdges = new ArrayList();
      final ArrayList obstacleTraceNodes = new ArrayList();
      final TIntArrayList splitXY = new TIntArrayList();
      static final PolygonalMap2.VisibilityGraph.CompareIntersection comparator = new PolygonalMap2.VisibilityGraph.CompareIntersection();
      private static final PolygonalMap2.ClusterOutlineGrid clusterOutlineGrid = new PolygonalMap2.ClusterOutlineGrid();
      private static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.VisibilityGraph init(PolygonalMap2.VehicleCluster var1) {
         this.created = false;
         this.cluster = var1;
         this.edges.clear();
         this.nodes.clear();
         this.obstacles.clear();
         this.intersectNodes.clear();
         this.perimeterEdges.clear();
         this.perimeterNodes.clear();
         return this;
      }

      void addEdgesForVehicle(PolygonalMap2.Vehicle var1) {
         PolygonalMap2.VehiclePoly var2 = var1.polyPlusRadius;
         int var3 = (int)var2.z;
         PolygonalMap2.Node var4 = PolygonalMap2.Node.alloc().init(var2.x1, var2.y1, var3);
         PolygonalMap2.Node var5 = PolygonalMap2.Node.alloc().init(var2.x2, var2.y2, var3);
         PolygonalMap2.Node var6 = PolygonalMap2.Node.alloc().init(var2.x3, var2.y3, var3);
         PolygonalMap2.Node var7 = PolygonalMap2.Node.alloc().init(var2.x4, var2.y4, var3);
         PolygonalMap2.Obstacle var8 = PolygonalMap2.Obstacle.alloc().init(var1);
         this.obstacles.add(var8);
         PolygonalMap2.Edge var9 = PolygonalMap2.Edge.alloc().init(var4, var5, var8, var8.outer);
         PolygonalMap2.Edge var10 = PolygonalMap2.Edge.alloc().init(var5, var6, var8, var8.outer);
         PolygonalMap2.Edge var11 = PolygonalMap2.Edge.alloc().init(var6, var7, var8, var8.outer);
         PolygonalMap2.Edge var12 = PolygonalMap2.Edge.alloc().init(var7, var4, var8, var8.outer);
         var8.outer.add(var9);
         var8.outer.add(var10);
         var8.outer.add(var11);
         var8.outer.add(var12);
         var8.calcBounds();
         this.nodes.add(var4);
         this.nodes.add(var5);
         this.nodes.add(var6);
         this.nodes.add(var7);
         this.edges.add(var9);
         this.edges.add(var10);
         this.edges.add(var11);
         this.edges.add(var12);
         if (!(var1.upVectorDot < 0.95F)) {
            var8.nodeCrawlFront = PolygonalMap2.Node.alloc().init((var2.x1 + var2.x2) / 2.0F, (var2.y1 + var2.y2) / 2.0F, var3);
            var8.nodeCrawlRear = PolygonalMap2.Node.alloc().init((var2.x3 + var2.x4) / 2.0F, (var2.y3 + var2.y4) / 2.0F, var3);
            PolygonalMap2.Node var10000 = var8.nodeCrawlFront;
            var10000.flags |= 1;
            var10000 = var8.nodeCrawlRear;
            var10000.flags |= 1;
            this.nodes.add(var8.nodeCrawlFront);
            this.nodes.add(var8.nodeCrawlRear);
            PolygonalMap2.Edge var13 = var9.split(var8.nodeCrawlFront);
            PolygonalMap2.Edge var14 = var11.split(var8.nodeCrawlRear);
            this.edges.add(var13);
            this.edges.add(var14);
            BaseVehicle.Vector2fObjectPool var15 = (BaseVehicle.Vector2fObjectPool)BaseVehicle.TL_vector2f_pool.get();
            Vector2f var16 = (Vector2f)var15.alloc();
            Vector2f var17 = (Vector2f)var15.alloc();
            var8.crawlNodes.clear();

            for(int var18 = 0; var18 < var1.crawlOffsets.size(); ++var18) {
               float var19 = var1.crawlOffsets.get(var18);
               var16.set(var6.x, var6.y);
               var17.set(var5.x, var5.y);
               var17.sub(var16).mul(var19).add(var16);
               PolygonalMap2.Node var20 = PolygonalMap2.Node.alloc().init(var17.x, var17.y, var3);
               var20.flags |= 1;
               var16.set(var7.x, var7.y);
               var17.set(var4.x, var4.y);
               var17.sub(var16).mul(var19).add(var16);
               PolygonalMap2.Node var21 = PolygonalMap2.Node.alloc().init(var17.x, var17.y, var3);
               var21.flags |= 1;
               PolygonalMap2.Node var22 = PolygonalMap2.Node.alloc().init((var20.x + var21.x) / 2.0F, (var20.y + var21.y) / 2.0F, var3);
               var22.flags |= 3;
               var8.crawlNodes.add(var20);
               var8.crawlNodes.add(var22);
               var8.crawlNodes.add(var21);
               this.nodes.add(var20);
               this.nodes.add(var22);
               this.nodes.add(var21);
               PolygonalMap2.Edge var23 = var10.split(var20);
               var12 = var12.split(var21);
               this.edges.add(var23);
               this.edges.add(var12);
            }

            var15.release(var16);
            var15.release(var17);
         }
      }

      boolean isVisible(PolygonalMap2.Node var1, PolygonalMap2.Node var2) {
         if (var1.sharesEdge(var2)) {
            return !var1.onSameShapeButDoesNotShareAnEdge(var2);
         } else if (var1.sharesShape(var2)) {
            return false;
         } else {
            int var3;
            PolygonalMap2.Edge var4;
            for(var3 = 0; var3 < this.edges.size(); ++var3) {
               var4 = (PolygonalMap2.Edge)this.edges.get(var3);
               if (this.intersects(var1, var2, var4)) {
                  return false;
               }
            }

            for(var3 = 0; var3 < this.perimeterEdges.size(); ++var3) {
               var4 = (PolygonalMap2.Edge)this.perimeterEdges.get(var3);
               if (this.intersects(var1, var2, var4)) {
                  return false;
               }
            }

            return true;
         }
      }

      boolean intersects(PolygonalMap2.Node var1, PolygonalMap2.Node var2, PolygonalMap2.Edge var3) {
         return !var3.hasNode(var1) && !var3.hasNode(var2) ? Line2D.linesIntersect((double)var1.x, (double)var1.y, (double)var2.x, (double)var2.y, (double)var3.node1.x, (double)var3.node1.y, (double)var3.node2.x, (double)var3.node2.y) : false;
      }

      public PolygonalMap2.Intersection getIntersection(PolygonalMap2.Edge var1, PolygonalMap2.Edge var2) {
         float var3 = var1.node1.x;
         float var4 = var1.node1.y;
         float var5 = var1.node2.x;
         float var6 = var1.node2.y;
         float var7 = var2.node1.x;
         float var8 = var2.node1.y;
         float var9 = var2.node2.x;
         float var10 = var2.node2.y;
         double var11 = (double)((var10 - var8) * (var5 - var3) - (var9 - var7) * (var6 - var4));
         if (var11 == 0.0D) {
            return null;
         } else {
            double var13 = (double)((var9 - var7) * (var4 - var8) - (var10 - var8) * (var3 - var7)) / var11;
            double var15 = (double)((var5 - var3) * (var4 - var8) - (var6 - var4) * (var3 - var7)) / var11;
            if (var13 >= 0.0D && var13 <= 1.0D && var15 >= 0.0D && var15 <= 1.0D) {
               float var17 = (float)((double)var3 + var13 * (double)(var5 - var3));
               float var18 = (float)((double)var4 + var13 * (double)(var6 - var4));
               return new PolygonalMap2.Intersection(var1, var2, (float)var13, (float)var15, var17, var18);
            } else {
               return null;
            }
         }
      }

      /** @deprecated */
      @Deprecated
      void addWorldObstacles() {
         PolygonalMap2.VehicleRect var1 = this.cluster.bounds();
         --var1.x;
         --var1.y;
         var1.w += 3;
         var1.h += 3;
         PolygonalMap2.ObjectOutline[][] var2 = new PolygonalMap2.ObjectOutline[var1.w][var1.h];
         int var3 = this.cluster.z;

         int var4;
         int var5;
         for(var4 = var1.top(); var4 < var1.bottom() - 1; ++var4) {
            for(var5 = var1.left(); var5 < var1.right() - 1; ++var5) {
               PolygonalMap2.Square var6 = PolygonalMap2.instance.getSquare(var5, var4, var3);
               if (var6 != null && this.contains(var6, 1)) {
                  if (var6.has(504) || var6.isReallySolid()) {
                     PolygonalMap2.ObjectOutline.setSolid(var5 - var1.left(), var4 - var1.top(), var3, var2);
                  }

                  if (var6.has(2)) {
                     PolygonalMap2.ObjectOutline.setWest(var5 - var1.left(), var4 - var1.top(), var3, var2);
                  }

                  if (var6.has(4)) {
                     PolygonalMap2.ObjectOutline.setNorth(var5 - var1.left(), var4 - var1.top(), var3, var2);
                  }

                  if (var6.has(262144)) {
                     PolygonalMap2.ObjectOutline.setWest(var5 - var1.left() + 1, var4 - var1.top(), var3, var2);
                  }

                  if (var6.has(524288)) {
                     PolygonalMap2.ObjectOutline.setNorth(var5 - var1.left(), var4 - var1.top() + 1, var3, var2);
                  }
               }
            }
         }

         for(var4 = 0; var4 < var1.h; ++var4) {
            for(var5 = 0; var5 < var1.w; ++var5) {
               PolygonalMap2.ObjectOutline var12 = PolygonalMap2.ObjectOutline.get(var5, var4, var3, var2);
               if (var12 != null && var12.nw && var12.nw_w && var12.nw_n) {
                  var12.trace(var2, this.obstacleTraceNodes);
                  if (!var12.nodes.isEmpty()) {
                     PolygonalMap2.Obstacle var7 = PolygonalMap2.Obstacle.alloc().init((IsoGridSquare)null);

                     for(int var8 = 0; var8 < var12.nodes.size() - 1; ++var8) {
                        PolygonalMap2.Node var9 = (PolygonalMap2.Node)var12.nodes.get(var8);
                        PolygonalMap2.Node var10 = (PolygonalMap2.Node)var12.nodes.get(var8 + 1);
                        var9.x += (float)var1.left();
                        var9.y += (float)var1.top();
                        if (!this.contains(var9.x, var9.y, var9.z)) {
                           var9.ignore = true;
                        }

                        PolygonalMap2.Edge var11 = PolygonalMap2.Edge.alloc().init(var9, var10, var7, var7.outer);
                        var7.outer.add(var11);
                        this.nodes.add(var9);
                     }

                     var7.calcBounds();
                     this.obstacles.add(var7);
                     this.edges.addAll(var7.outer);
                  }
               }
            }
         }

         for(var4 = 0; var4 < var1.h; ++var4) {
            for(var5 = 0; var5 < var1.w; ++var5) {
               if (var2[var5][var4] != null) {
                  var2[var5][var4].release();
               }
            }
         }

         var1.release();
      }

      void addWorldObstaclesClipper() {
         PolygonalMap2.VehicleRect var1 = this.cluster.bounds();
         --var1.x;
         --var1.y;
         var1.w += 2;
         var1.h += 2;
         if (PolygonalMap2.instance.clipperThread == null) {
            PolygonalMap2.instance.clipperThread = new Clipper();
         }

         Clipper var2 = PolygonalMap2.instance.clipperThread;
         var2.clear();
         int var3 = this.cluster.z;

         int var5;
         for(int var4 = var1.top(); var4 < var1.bottom(); ++var4) {
            for(var5 = var1.left(); var5 < var1.right(); ++var5) {
               PolygonalMap2.Square var6 = PolygonalMap2.instance.getSquare(var5, var4, var3);
               if (var6 != null && this.contains(var6, 1)) {
                  if (var6.has(504) || var6.isReallySolid()) {
                     var2.addAABB((float)var5 - 0.3F, (float)var4 - 0.3F, (float)(var5 + 1) + 0.3F, (float)(var4 + 1) + 0.3F);
                  }

                  if (var6.has(2)) {
                     var2.addAABB((float)var5 - 0.3F, (float)var4 - 0.3F, (float)var5 + 0.3F, (float)(var4 + 1) + 0.3F);
                  }

                  if (var6.has(4)) {
                     var2.addAABB((float)var5 - 0.3F, (float)var4 - 0.3F, (float)(var5 + 1) + 0.3F, (float)var4 + 0.3F);
                  }
               }
            }
         }

         var1.release();
         ByteBuffer var10 = PolygonalMap2.instance.xyBufferThread;
         var5 = var2.generatePolygons();

         for(int var11 = 0; var11 < var5; ++var11) {
            var10.clear();
            var2.getPolygon(var11, var10);
            PolygonalMap2.Obstacle var7 = PolygonalMap2.Obstacle.alloc().init((IsoGridSquare)null);
            this.getEdgesFromBuffer(var10, var7, true, var3);
            short var8 = var10.getShort();

            int var9;
            for(var9 = 0; var9 < var8; ++var9) {
               this.getEdgesFromBuffer(var10, var7, false, var3);
            }

            var7.calcBounds();
            this.obstacles.add(var7);
            this.edges.addAll(var7.outer);

            for(var9 = 0; var9 < var7.inner.size(); ++var9) {
               this.edges.addAll((Collection)var7.inner.get(var9));
            }
         }

      }

      void getEdgesFromBuffer(ByteBuffer var1, PolygonalMap2.Obstacle var2, boolean var3, int var4) {
         short var5 = var1.getShort();
         if (var5 < 3) {
            var1.position(var1.position() + var5 * 4 * 2);
         } else {
            PolygonalMap2.EdgeRing var6 = var2.outer;
            if (!var3) {
               var6 = PolygonalMap2.EdgeRing.alloc();
               var6.clear();
               var2.inner.add(var6);
            }

            int var7 = this.nodes.size();

            int var8;
            for(var8 = var5 - 1; var8 >= 0; --var8) {
               float var9 = var1.getFloat();
               float var10 = var1.getFloat();
               PolygonalMap2.Node var11 = PolygonalMap2.Node.alloc().init(var9, var10, var4);
               this.nodes.add(var11);
            }

            PolygonalMap2.Node var13;
            for(var8 = var7; var8 < this.nodes.size() - 1; ++var8) {
               var13 = (PolygonalMap2.Node)this.nodes.get(var8);
               PolygonalMap2.Node var14 = (PolygonalMap2.Node)this.nodes.get(var8 + 1);
               if (!this.contains(var13.x, var13.y, var13.z)) {
                  var13.ignore = true;
               }

               PolygonalMap2.Edge var16 = PolygonalMap2.Edge.alloc().init(var13, var14, var2, var6);
               var6.add(var16);
            }

            PolygonalMap2.Node var12 = (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1);
            var13 = (PolygonalMap2.Node)this.nodes.get(var7);
            PolygonalMap2.Edge var15 = PolygonalMap2.Edge.alloc().init(var12, var13, var2, var6);
            var6.add(var15);
         }
      }

      void trySplit(PolygonalMap2.Edge var1, PolygonalMap2.VehicleRect var2, TIntArrayList var3) {
         float var4;
         float var5;
         float var6;
         if (Math.abs(var1.node1.x - var1.node2.x) > Math.abs(var1.node1.y - var1.node2.y)) {
            var4 = Math.min(var1.node1.x, var1.node2.x);
            var5 = Math.max(var1.node1.x, var1.node2.x);
            var6 = var1.node1.y;
            if ((float)var2.left() > var4 && (float)var2.left() < var5 && (float)var2.top() < var6 && (float)var2.bottom() > var6 && !var3.contains(var2.left()) && !this.contains((float)var2.left() - 0.5F, var6, this.cluster.z)) {
               var3.add(var2.left());
            }

            if ((float)var2.right() > var4 && (float)var2.right() < var5 && (float)var2.top() < var6 && (float)var2.bottom() > var6 && !var3.contains(var2.right()) && !this.contains((float)var2.right() + 0.5F, var6, this.cluster.z)) {
               var3.add(var2.right());
            }
         } else {
            var4 = Math.min(var1.node1.y, var1.node2.y);
            var5 = Math.max(var1.node1.y, var1.node2.y);
            var6 = var1.node1.x;
            if ((float)var2.top() > var4 && (float)var2.top() < var5 && (float)var2.left() < var6 && (float)var2.right() > var6 && !var3.contains(var2.top()) && !this.contains(var6, (float)var2.top() - 0.5F, this.cluster.z)) {
               var3.add(var2.top());
            }

            if ((float)var2.bottom() > var4 && (float)var2.bottom() < var5 && (float)var2.left() < var6 && (float)var2.right() > var6 && !var3.contains(var2.bottom()) && !this.contains(var6, (float)var2.bottom() + 0.5F, this.cluster.z)) {
               var3.add(var2.bottom());
            }
         }

      }

      void splitWorldObstacleEdges(PolygonalMap2.EdgeRing var1) {
         for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
            PolygonalMap2.Edge var3 = (PolygonalMap2.Edge)var1.get(var2);
            this.splitXY.clear();

            int var4;
            for(var4 = 0; var4 < this.cluster.rects.size(); ++var4) {
               PolygonalMap2.VehicleRect var5 = (PolygonalMap2.VehicleRect)this.cluster.rects.get(var4);
               this.trySplit(var3, var5, this.splitXY);
            }

            if (!this.splitXY.isEmpty()) {
               this.splitXY.sort();
               PolygonalMap2.Edge var6;
               PolygonalMap2.Node var7;
               if (Math.abs(var3.node1.x - var3.node2.x) > Math.abs(var3.node1.y - var3.node2.y)) {
                  if (var3.node1.x < var3.node2.x) {
                     for(var4 = this.splitXY.size() - 1; var4 >= 0; --var4) {
                        var7 = PolygonalMap2.Node.alloc().init((float)this.splitXY.get(var4), var3.node1.y, this.cluster.z);
                        var6 = var3.split(var7);
                        this.nodes.add(var7);
                        this.edges.add(var6);
                     }
                  } else {
                     for(var4 = 0; var4 < this.splitXY.size(); ++var4) {
                        var7 = PolygonalMap2.Node.alloc().init((float)this.splitXY.get(var4), var3.node1.y, this.cluster.z);
                        var6 = var3.split(var7);
                        this.nodes.add(var7);
                        this.edges.add(var6);
                     }
                  }
               } else if (var3.node1.y < var3.node2.y) {
                  for(var4 = this.splitXY.size() - 1; var4 >= 0; --var4) {
                     var7 = PolygonalMap2.Node.alloc().init(var3.node1.x, (float)this.splitXY.get(var4), this.cluster.z);
                     var6 = var3.split(var7);
                     this.nodes.add(var7);
                     this.edges.add(var6);
                  }
               } else {
                  for(var4 = 0; var4 < this.splitXY.size(); ++var4) {
                     var7 = PolygonalMap2.Node.alloc().init(var3.node1.x, (float)this.splitXY.get(var4), this.cluster.z);
                     var6 = var3.split(var7);
                     this.nodes.add(var7);
                     this.edges.add(var6);
                  }
               }
            }
         }

      }

      void getStairSquares(ArrayList var1) {
         PolygonalMap2.VehicleRect var2 = this.cluster.bounds();
         var2.x -= 4;
         var2.w += 4;
         ++var2.w;
         var2.y -= 4;
         var2.h += 4;
         ++var2.h;

         for(int var3 = var2.top(); var3 < var2.bottom(); ++var3) {
            for(int var4 = var2.left(); var4 < var2.right(); ++var4) {
               PolygonalMap2.Square var5 = PolygonalMap2.instance.getSquare(var4, var3, this.cluster.z);
               if (var5 != null && var5.has(72) && !var1.contains(var5)) {
                  var1.add(var5);
               }
            }
         }

         var2.release();
      }

      void getCanPathSquares(ArrayList var1) {
         PolygonalMap2.VehicleRect var2 = this.cluster.bounds();
         --var2.x;
         var2.w += 2;
         --var2.y;
         var2.h += 2;

         for(int var3 = var2.top(); var3 < var2.bottom(); ++var3) {
            for(int var4 = var2.left(); var4 < var2.right(); ++var4) {
               PolygonalMap2.Square var5 = PolygonalMap2.instance.getSquare(var4, var3, this.cluster.z);
               if (var5 != null && (var5.isCanPathW() || var5.isCanPathN()) && !var1.contains(var5)) {
                  var1.add(var5);
               }
            }
         }

         var2.release();
      }

      void connectVehicleCrawlNodes() {
         for(int var1 = 0; var1 < this.obstacles.size(); ++var1) {
            PolygonalMap2.Obstacle var2 = (PolygonalMap2.Obstacle)this.obstacles.get(var1);
            if (var2.vehicle != null && var2.nodeCrawlFront != null) {
               int var3;
               PolygonalMap2.Node var4;
               for(var3 = 0; var3 < var2.crawlNodes.size(); var3 += 3) {
                  var4 = (PolygonalMap2.Node)var2.crawlNodes.get(var3);
                  PolygonalMap2.Node var5 = (PolygonalMap2.Node)var2.crawlNodes.get(var3 + 1);
                  PolygonalMap2.Node var6 = (PolygonalMap2.Node)var2.crawlNodes.get(var3 + 2);
                  PolygonalMap2.instance.connectTwoNodes(var4, var5);
                  PolygonalMap2.instance.connectTwoNodes(var6, var5);
                  if (var3 + 3 < var2.crawlNodes.size()) {
                     PolygonalMap2.Node var7 = (PolygonalMap2.Node)var2.crawlNodes.get(var3 + 3 + 1);
                     PolygonalMap2.instance.connectTwoNodes(var5, var7);
                  }
               }

               if (!var2.crawlNodes.isEmpty()) {
                  var3 = var2.crawlNodes.size() - 2;
                  var4 = (PolygonalMap2.Node)var2.crawlNodes.get(var3);
                  PolygonalMap2.instance.connectTwoNodes(var2.nodeCrawlFront, var4);
                  byte var37 = 1;
                  var4 = (PolygonalMap2.Node)var2.crawlNodes.get(var37);
                  PolygonalMap2.instance.connectTwoNodes(var2.nodeCrawlRear, var4);
               }

               if (!var2.crawlNodes.isEmpty()) {
                  PolygonalMap2.ImmutableRectF var38 = var2.bounds;
                  int var39 = (int)var38.x;
                  int var40 = (int)var38.y;
                  int var42 = (int)Math.ceil((double)var38.right());
                  int var43 = (int)Math.ceil((double)var38.bottom());

                  for(int var8 = var40; var8 < var43; ++var8) {
                     for(int var9 = var39; var9 < var42; ++var9) {
                        PolygonalMap2.Square var10 = PolygonalMap2.instance.getSquare(var9, var8, this.cluster.z);
                        if (var10 != null && var2.isPointInside((float)var9 + 0.5F, (float)var8 + 0.5F)) {
                           PolygonalMap2.Node var11 = PolygonalMap2.instance.getNodeForSquare(var10);

                           for(int var12 = var11.visible.size() - 1; var12 >= 0; --var12) {
                              PolygonalMap2.Connection var13 = (PolygonalMap2.Connection)var11.visible.get(var12);
                              if (var13.has(1)) {
                                 PolygonalMap2.Node var14 = var13.otherNode(var11);
                                 PolygonalMap2.Node var15 = var2.getClosestInteriorCrawlNode(var11.x, var11.y);

                                 for(int var16 = 0; var16 < var2.outer.size(); ++var16) {
                                    PolygonalMap2.Edge var17 = (PolygonalMap2.Edge)var2.outer.get(var16);
                                    float var18 = var17.node1.x;
                                    float var19 = var17.node1.y;
                                    float var20 = var17.node2.x;
                                    float var21 = var17.node2.y;
                                    float var22 = var13.node1.x;
                                    float var23 = var13.node1.y;
                                    float var24 = var13.node2.x;
                                    float var25 = var13.node2.y;
                                    double var26 = (double)((var25 - var23) * (var20 - var18) - (var24 - var22) * (var21 - var19));
                                    if (var26 != 0.0D) {
                                       double var28 = (double)((var24 - var22) * (var19 - var23) - (var25 - var23) * (var18 - var22)) / var26;
                                       double var30 = (double)((var20 - var18) * (var19 - var23) - (var21 - var19) * (var18 - var22)) / var26;
                                       if (var28 >= 0.0D && var28 <= 1.0D && var30 >= 0.0D && var30 <= 1.0D) {
                                          float var32 = (float)((double)var18 + var28 * (double)(var20 - var18));
                                          float var33 = (float)((double)var19 + var28 * (double)(var21 - var19));
                                          PolygonalMap2.Node var34 = PolygonalMap2.Node.alloc().init(var32, var33, this.cluster.z);
                                          var34.flags |= 1;
                                          boolean var35 = var17.node1.isConnectedTo(var17.node2);
                                          PolygonalMap2.Edge var36 = var17.split(var34);
                                          if (var35) {
                                             PolygonalMap2.instance.connectTwoNodes(var17.node1, var17.node2);
                                             PolygonalMap2.instance.connectTwoNodes(var36.node1, var36.node2);
                                          }

                                          this.edges.add(var36);
                                          this.nodes.add(var34);
                                          PolygonalMap2.instance.connectTwoNodes(var14, var34, var13.flags & 2 | 1);
                                          PolygonalMap2.instance.connectTwoNodes(var34, var15, 0);
                                          break;
                                       }
                                    }
                                 }

                                 PolygonalMap2.instance.breakConnection(var13);
                              }
                           }
                        }
                     }
                  }
               }

               for(var3 = var1 + 1; var3 < this.obstacles.size(); ++var3) {
                  PolygonalMap2.Obstacle var41 = (PolygonalMap2.Obstacle)this.obstacles.get(var3);
                  if (var41.vehicle != null && var41.nodeCrawlFront != null) {
                     var2.connectCrawlNodes(this, var41);
                     var41.connectCrawlNodes(this, var2);
                  }
               }
            }
         }

      }

      void checkEdgeIntersection() {
         int var1;
         PolygonalMap2.Obstacle var2;
         int var3;
         for(var1 = 0; var1 < this.obstacles.size(); ++var1) {
            var2 = (PolygonalMap2.Obstacle)this.obstacles.get(var1);

            for(var3 = var1 + 1; var3 < this.obstacles.size(); ++var3) {
               PolygonalMap2.Obstacle var4 = (PolygonalMap2.Obstacle)this.obstacles.get(var3);
               if (var2.bounds.intersects(var4.bounds)) {
                  this.checkEdgeIntersection(var2.outer, var4.outer);

                  int var5;
                  PolygonalMap2.EdgeRing var6;
                  for(var5 = 0; var5 < var4.inner.size(); ++var5) {
                     var6 = (PolygonalMap2.EdgeRing)var4.inner.get(var5);
                     this.checkEdgeIntersection(var2.outer, var6);
                  }

                  for(var5 = 0; var5 < var2.inner.size(); ++var5) {
                     var6 = (PolygonalMap2.EdgeRing)var2.inner.get(var5);
                     this.checkEdgeIntersection(var6, var4.outer);

                     for(int var7 = 0; var7 < var4.inner.size(); ++var7) {
                        PolygonalMap2.EdgeRing var8 = (PolygonalMap2.EdgeRing)var4.inner.get(var7);
                        this.checkEdgeIntersection(var6, var8);
                     }
                  }
               }
            }
         }

         for(var1 = 0; var1 < this.obstacles.size(); ++var1) {
            var2 = (PolygonalMap2.Obstacle)this.obstacles.get(var1);
            this.checkEdgeIntersectionSplit(var2.outer);

            for(var3 = 0; var3 < var2.inner.size(); ++var3) {
               this.checkEdgeIntersectionSplit((PolygonalMap2.EdgeRing)var2.inner.get(var3));
            }
         }

      }

      void checkEdgeIntersection(PolygonalMap2.EdgeRing var1, PolygonalMap2.EdgeRing var2) {
         for(int var3 = 0; var3 < var1.size(); ++var3) {
            PolygonalMap2.Edge var4 = (PolygonalMap2.Edge)var1.get(var3);

            for(int var5 = 0; var5 < var2.size(); ++var5) {
               PolygonalMap2.Edge var6 = (PolygonalMap2.Edge)var2.get(var5);
               if (this.intersects(var4.node1, var4.node2, var6)) {
                  PolygonalMap2.Intersection var7 = this.getIntersection(var4, var6);
                  if (var7 != null) {
                     var4.intersections.add(var7);
                     var6.intersections.add(var7);
                     this.nodes.add(var7.nodeSplit);
                     this.intersectNodes.add(var7.nodeSplit);
                  }
               }
            }
         }

      }

      void checkEdgeIntersectionSplit(PolygonalMap2.EdgeRing var1) {
         for(int var2 = var1.size() - 1; var2 >= 0; --var2) {
            PolygonalMap2.Edge var3 = (PolygonalMap2.Edge)var1.get(var2);
            if (!var3.intersections.isEmpty()) {
               comparator.edge = var3;
               Collections.sort(var3.intersections, comparator);

               for(int var4 = var3.intersections.size() - 1; var4 >= 0; --var4) {
                  PolygonalMap2.Intersection var5 = (PolygonalMap2.Intersection)var3.intersections.get(var4);
                  PolygonalMap2.Edge var6 = var5.split(var3);
                  this.edges.add(var6);
               }
            }
         }

      }

      void checkNodesInObstacles() {
         int var1;
         PolygonalMap2.Node var2;
         int var3;
         PolygonalMap2.Obstacle var4;
         for(var1 = 0; var1 < this.nodes.size(); ++var1) {
            var2 = (PolygonalMap2.Node)this.nodes.get(var1);

            for(var3 = 0; var3 < this.obstacles.size(); ++var3) {
               var4 = (PolygonalMap2.Obstacle)this.obstacles.get(var3);
               if (var4.isNodeInsideOf(var2)) {
                  var2.ignore = true;
                  break;
               }
            }
         }

         for(var1 = 0; var1 < this.perimeterNodes.size(); ++var1) {
            var2 = (PolygonalMap2.Node)this.perimeterNodes.get(var1);

            for(var3 = 0; var3 < this.obstacles.size(); ++var3) {
               var4 = (PolygonalMap2.Obstacle)this.obstacles.get(var3);
               if (var4.isNodeInsideOf(var2)) {
                  var2.ignore = true;
                  break;
               }
            }
         }

      }

      void addPerimeterEdges() {
         PolygonalMap2.VehicleRect var1 = this.cluster.bounds();
         --var1.x;
         --var1.y;
         var1.w += 2;
         var1.h += 2;
         PolygonalMap2.ClusterOutlineGrid var2 = clusterOutlineGrid.setSize(var1.w, var1.h);
         int var3 = this.cluster.z;

         int var4;
         for(var4 = 0; var4 < this.cluster.rects.size(); ++var4) {
            PolygonalMap2.VehicleRect var5 = (PolygonalMap2.VehicleRect)this.cluster.rects.get(var4);
            var5 = PolygonalMap2.VehicleRect.alloc().init(var5.x - 1, var5.y - 1, var5.w + 2, var5.h + 2, var5.z);

            for(int var6 = var5.top(); var6 < var5.bottom(); ++var6) {
               for(int var7 = var5.left(); var7 < var5.right(); ++var7) {
                  var2.setInner(var7 - var1.left(), var6 - var1.top(), var3);
               }
            }

            var5.release();
         }

         int var12;
         PolygonalMap2.ClusterOutline var13;
         for(var4 = 0; var4 < var1.h; ++var4) {
            for(var12 = 0; var12 < var1.w; ++var12) {
               var13 = var2.get(var12, var4, var3);
               if (var13.inner) {
                  if (!var2.isInner(var12 - 1, var4, var3)) {
                     var13.w = true;
                  }

                  if (!var2.isInner(var12, var4 - 1, var3)) {
                     var13.n = true;
                  }

                  if (!var2.isInner(var12 + 1, var4, var3)) {
                     var13.e = true;
                  }

                  if (!var2.isInner(var12, var4 + 1, var3)) {
                     var13.s = true;
                  }
               }
            }
         }

         for(var4 = 0; var4 < var1.h; ++var4) {
            for(var12 = 0; var12 < var1.w; ++var12) {
               var13 = var2.get(var12, var4, var3);
               if (var13 != null && (var13.w || var13.n || var13.e || var13.s || var13.innerCorner)) {
                  PolygonalMap2.Square var14 = PolygonalMap2.instance.getSquare(var1.x + var12, var1.y + var4, var3);
                  if (var14 != null && !var14.isNonThumpableSolid() && !var14.has(504)) {
                     PolygonalMap2.Node var8 = PolygonalMap2.instance.getNodeForSquare(var14);
                     var8.flags |= 8;
                     var8.addGraph(this);
                     this.perimeterNodes.add(var8);
                  }
               }

               if (var13 != null && var13.n && var13.w && var13.inner && !(var13.tw | var13.tn | var13.te | var13.ts)) {
                  ArrayList var15 = var2.trace(var13);
                  if (!var15.isEmpty()) {
                     for(int var16 = 0; var16 < var15.size() - 1; ++var16) {
                        PolygonalMap2.Node var9 = (PolygonalMap2.Node)var15.get(var16);
                        PolygonalMap2.Node var10 = (PolygonalMap2.Node)var15.get(var16 + 1);
                        var9.x += (float)var1.left();
                        var9.y += (float)var1.top();
                        PolygonalMap2.Edge var11 = PolygonalMap2.Edge.alloc().init(var9, var10, (PolygonalMap2.Obstacle)null, (PolygonalMap2.EdgeRing)null);
                        this.perimeterEdges.add(var11);
                     }

                     if (var15.get(var15.size() - 1) != var15.get(0)) {
                        PolygonalMap2.Node var10000 = (PolygonalMap2.Node)var15.get(var15.size() - 1);
                        var10000.x += (float)var1.left();
                        var10000 = (PolygonalMap2.Node)var15.get(var15.size() - 1);
                        var10000.y += (float)var1.top();
                     }
                  }
               }
            }
         }

         var2.releaseElements();
         var1.release();
      }

      void calculateNodeVisibility() {
         ArrayList var1 = new ArrayList();
         var1.addAll(this.nodes);
         var1.addAll(this.perimeterNodes);

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            PolygonalMap2.Node var3 = (PolygonalMap2.Node)var1.get(var2);
            if (!var3.ignore && (var3.square == null || !var3.square.has(504))) {
               for(int var4 = var2 + 1; var4 < var1.size(); ++var4) {
                  PolygonalMap2.Node var5 = (PolygonalMap2.Node)var1.get(var4);
                  if (!var5.ignore && (var5.square == null || !var5.square.has(504)) && (!var3.hasFlag(8) || !var5.hasFlag(8))) {
                     if (var3.isConnectedTo(var5)) {
                        assert var3.square != null && (var3.square.isCanPathW() || var3.square.isCanPathN()) || var5.square != null && (var5.square.isCanPathW() || var5.square.isCanPathN());
                     } else if (this.isVisible(var3, var5)) {
                        PolygonalMap2.instance.connectTwoNodes(var3, var5);
                     }
                  }
               }
            }
         }

      }

      void addNode(PolygonalMap2.Node var1) {
         if (this.created && !var1.ignore) {
            ArrayList var2 = new ArrayList();
            var2.addAll(this.nodes);
            var2.addAll(this.perimeterNodes);

            for(int var3 = 0; var3 < var2.size(); ++var3) {
               PolygonalMap2.Node var4 = (PolygonalMap2.Node)var2.get(var3);
               if (!var4.ignore && this.isVisible(var4, var1)) {
                  PolygonalMap2.instance.connectTwoNodes(var1, var4);
               }
            }
         }

         this.nodes.add(var1);
      }

      void removeNode(PolygonalMap2.Node var1) {
         this.nodes.remove(var1);

         for(int var2 = var1.visible.size() - 1; var2 >= 0; --var2) {
            PolygonalMap2.Connection var3 = (PolygonalMap2.Connection)var1.visible.get(var2);
            PolygonalMap2.instance.breakConnection(var3);
         }

      }

      boolean contains(float var1, float var2, int var3) {
         for(int var4 = 0; var4 < this.cluster.rects.size(); ++var4) {
            PolygonalMap2.VehicleRect var5 = (PolygonalMap2.VehicleRect)this.cluster.rects.get(var4);
            if (var5.containsPoint(var1, var2, (float)var3)) {
               return true;
            }
         }

         return false;
      }

      boolean contains(float var1, float var2, int var3, int var4) {
         for(int var5 = 0; var5 < this.cluster.rects.size(); ++var5) {
            PolygonalMap2.VehicleRect var6 = (PolygonalMap2.VehicleRect)this.cluster.rects.get(var5);
            if (var6.containsPoint(var1, var2, (float)var3, var4)) {
               return true;
            }
         }

         return false;
      }

      boolean contains(PolygonalMap2.Square var1) {
         for(int var2 = 0; var2 < this.cluster.rects.size(); ++var2) {
            PolygonalMap2.VehicleRect var3 = (PolygonalMap2.VehicleRect)this.cluster.rects.get(var2);
            if (var3.containsPoint((float)var1.x + 0.5F, (float)var1.y + 0.5F, (float)var1.z)) {
               return true;
            }
         }

         return false;
      }

      boolean contains(PolygonalMap2.Square var1, int var2) {
         for(int var3 = 0; var3 < this.cluster.rects.size(); ++var3) {
            PolygonalMap2.VehicleRect var4 = (PolygonalMap2.VehicleRect)this.cluster.rects.get(var3);
            if (var4.containsPoint((float)var1.x + 0.5F, (float)var1.y + 0.5F, (float)var1.z, var2)) {
               return true;
            }
         }

         return false;
      }

      private int getPointOutsideObstacles(float var1, float var2, float var3, PolygonalMap2.AdjustStartEndNodeData var4) {
         PolygonalMap2.ClosestPointOnEdge var5 = PolygonalMap2.instance.closestPointOnEdge;
         double var6 = Double.MAX_VALUE;
         PolygonalMap2.Edge var8 = null;
         PolygonalMap2.Node var9 = null;
         float var10 = 0.0F;
         float var11 = 0.0F;

         for(int var12 = 0; var12 < this.obstacles.size(); ++var12) {
            PolygonalMap2.Obstacle var13 = (PolygonalMap2.Obstacle)this.obstacles.get(var12);
            if (var13.bounds.containsPoint(var1, var2) && var13.isPointInside(var1, var2)) {
               var13.getClosestPointOnEdge(var1, var2, var5);
               if (var5.edge != null && var5.distSq < var6) {
                  var6 = var5.distSq;
                  var8 = var5.edge;
                  var9 = var5.node;
                  var10 = var5.point.x;
                  var11 = var5.point.y;
               }
            }
         }

         if (var8 != null) {
            var5.edge = var8;
            var5.node = var9;
            var5.point.set(var10, var11);
            var5.distSq = var6;
            if (var8.obstacle.splitEdgeAtNearestPoint(var5, (int)var3, var4)) {
               var4.graph = this;
               if (var4.isNodeNew) {
                  this.edges.add(var4.newEdge);
                  this.addNode(var4.node);
               }

               return 1;
            } else {
               return -1;
            }
         } else {
            return 0;
         }
      }

      PolygonalMap2.Node getClosestNodeTo(float var1, float var2) {
         PolygonalMap2.Node var3 = null;
         float var4 = Float.MAX_VALUE;

         for(int var5 = 0; var5 < this.nodes.size(); ++var5) {
            PolygonalMap2.Node var6 = (PolygonalMap2.Node)this.nodes.get(var5);
            float var7 = IsoUtils.DistanceToSquared(var6.x, var6.y, var1, var2);
            if (var7 < var4) {
               var3 = var6;
               var4 = var7;
            }
         }

         return var3;
      }

      void create() {
         int var1;
         for(var1 = 0; var1 < this.cluster.rects.size(); ++var1) {
            PolygonalMap2.VehicleRect var2 = (PolygonalMap2.VehicleRect)this.cluster.rects.get(var1);
            this.addEdgesForVehicle(var2.vehicle);
         }

         this.addWorldObstaclesClipper();

         for(var1 = 0; var1 < this.obstacles.size(); ++var1) {
            PolygonalMap2.Obstacle var4 = (PolygonalMap2.Obstacle)this.obstacles.get(var1);
            if (var4.vehicle == null) {
               this.splitWorldObstacleEdges(var4.outer);

               for(int var3 = 0; var3 < var4.inner.size(); ++var3) {
                  this.splitWorldObstacleEdges((PolygonalMap2.EdgeRing)var4.inner.get(var3));
               }
            }
         }

         this.checkEdgeIntersection();
         this.checkNodesInObstacles();
         this.calculateNodeVisibility();
         this.connectVehicleCrawlNodes();
         this.created = true;
      }

      static PolygonalMap2.VisibilityGraph alloc() {
         return pool.isEmpty() ? new PolygonalMap2.VisibilityGraph() : (PolygonalMap2.VisibilityGraph)pool.pop();
      }

      void release() {
         int var1;
         for(var1 = 0; var1 < this.nodes.size(); ++var1) {
            if (!PolygonalMap2.instance.squareToNode.containsValue(this.nodes.get(var1))) {
               ((PolygonalMap2.Node)this.nodes.get(var1)).release();
            }
         }

         for(var1 = 0; var1 < this.perimeterEdges.size(); ++var1) {
            ((PolygonalMap2.Edge)this.perimeterEdges.get(var1)).node1.release();
            ((PolygonalMap2.Edge)this.perimeterEdges.get(var1)).release();
         }

         for(var1 = 0; var1 < this.obstacles.size(); ++var1) {
            PolygonalMap2.Obstacle var2 = (PolygonalMap2.Obstacle)this.obstacles.get(var1);
            var2.release();
         }

         for(var1 = 0; var1 < this.cluster.rects.size(); ++var1) {
            ((PolygonalMap2.VehicleRect)this.cluster.rects.get(var1)).release();
         }

         this.cluster.release();

         assert !pool.contains(this);

         pool.push(this);
      }

      void render() {
         float var1 = 1.0F;

         Iterator var2;
         for(var2 = this.perimeterEdges.iterator(); var2.hasNext(); var1 = 1.0F - var1) {
            PolygonalMap2.Edge var3 = (PolygonalMap2.Edge)var2.next();
            LineDrawer.addLine(var3.node1.x, var3.node1.y, (float)this.cluster.z, var3.node2.x, var3.node2.y, (float)this.cluster.z, var1, 0.5F, 0.5F, (String)null, true);
         }

         var2 = this.obstacles.iterator();

         while(true) {
            Iterator var4;
            Iterator var6;
            PolygonalMap2.Obstacle var9;
            do {
               if (!var2.hasNext()) {
                  var2 = this.perimeterNodes.iterator();

                  PolygonalMap2.Node var10;
                  PolygonalMap2.Connection var14;
                  PolygonalMap2.Node var16;
                  while(var2.hasNext()) {
                     var10 = (PolygonalMap2.Node)var2.next();
                     if (DebugOptions.instance.PolymapRenderConnections.getValue()) {
                        var4 = var10.visible.iterator();

                        while(var4.hasNext()) {
                           var14 = (PolygonalMap2.Connection)var4.next();
                           var16 = var14.otherNode(var10);
                           LineDrawer.addLine(var10.x, var10.y, (float)this.cluster.z, var16.x, var16.y, (float)this.cluster.z, 0.0F, 0.25F, 0.0F, (String)null, true);
                        }
                     }

                     if (DebugOptions.instance.PolymapRenderNodes.getValue()) {
                        float var12 = 1.0F;
                        float var15 = 0.5F;
                        float var17 = 0.0F;
                        if (var10.ignore) {
                           var15 = 1.0F;
                        }

                        LineDrawer.addLine(var10.x - 0.05F, var10.y - 0.05F, (float)this.cluster.z, var10.x + 0.05F, var10.y + 0.05F, (float)this.cluster.z, var12, var15, var17, (String)null, false);
                     }
                  }

                  var2 = this.nodes.iterator();

                  while(true) {
                     do {
                        if (!var2.hasNext()) {
                           var2 = this.intersectNodes.iterator();

                           while(var2.hasNext()) {
                              var10 = (PolygonalMap2.Node)var2.next();
                              LineDrawer.addLine(var10.x - 0.1F, var10.y - 0.1F, (float)this.cluster.z, var10.x + 0.1F, var10.y + 0.1F, (float)this.cluster.z, 1.0F, 0.0F, 0.0F, (String)null, false);
                           }

                           return;
                        }

                        var10 = (PolygonalMap2.Node)var2.next();
                        if (DebugOptions.instance.PolymapRenderConnections.getValue()) {
                           var4 = var10.visible.iterator();

                           while(var4.hasNext()) {
                              var14 = (PolygonalMap2.Connection)var4.next();
                              var16 = var14.otherNode(var10);
                              if (this.nodes.contains(var16)) {
                                 LineDrawer.addLine(var10.x, var10.y, (float)this.cluster.z, var16.x, var16.y, (float)this.cluster.z, 0.0F, 1.0F, 0.0F, (String)null, true);
                              }
                           }
                        }
                     } while(!DebugOptions.instance.PolymapRenderNodes.getValue() && !var10.ignore);

                     LineDrawer.addLine(var10.x - 0.05F, var10.y - 0.05F, (float)this.cluster.z, var10.x + 0.05F, var10.y + 0.05F, (float)this.cluster.z, 1.0F, 1.0F, 0.0F, (String)null, false);
                  }
               }

               var9 = (PolygonalMap2.Obstacle)var2.next();
               var1 = 1.0F;

               for(var4 = var9.outer.iterator(); var4.hasNext(); var1 = 1.0F - var1) {
                  PolygonalMap2.Edge var5 = (PolygonalMap2.Edge)var4.next();
                  LineDrawer.addLine(var5.node1.x, var5.node1.y, (float)this.cluster.z, var5.node2.x, var5.node2.y, (float)this.cluster.z, var1, 0.5F, 0.5F, (String)null, true);
               }

               var4 = var9.inner.iterator();

               while(var4.hasNext()) {
                  PolygonalMap2.EdgeRing var11 = (PolygonalMap2.EdgeRing)var4.next();

                  for(var6 = var11.iterator(); var6.hasNext(); var1 = 1.0F - var1) {
                     PolygonalMap2.Edge var7 = (PolygonalMap2.Edge)var6.next();
                     LineDrawer.addLine(var7.node1.x, var7.node1.y, (float)this.cluster.z, var7.node2.x, var7.node2.y, (float)this.cluster.z, var1, 0.5F, 0.5F, (String)null, true);
                  }
               }
            } while(!DebugOptions.instance.PolymapRenderCrawling.getValue());

            var4 = var9.crawlNodes.iterator();

            while(var4.hasNext()) {
               PolygonalMap2.Node var13 = (PolygonalMap2.Node)var4.next();
               LineDrawer.addLine(var13.x - 0.05F, var13.y - 0.05F, (float)this.cluster.z, var13.x + 0.05F, var13.y + 0.05F, (float)this.cluster.z, 0.5F, 1.0F, 0.5F, (String)null, false);
               var6 = var13.visible.iterator();

               while(var6.hasNext()) {
                  PolygonalMap2.Connection var18 = (PolygonalMap2.Connection)var6.next();
                  PolygonalMap2.Node var8 = var18.otherNode(var13);
                  if (var8.hasFlag(1)) {
                     LineDrawer.addLine(var13.x, var13.y, (float)this.cluster.z, var8.x, var8.y, (float)this.cluster.z, 0.5F, 1.0F, 0.5F, (String)null, true);
                  }
               }
            }
         }
      }

      static final class CompareIntersection implements Comparator {
         PolygonalMap2.Edge edge;

         public int compare(PolygonalMap2.Intersection var1, PolygonalMap2.Intersection var2) {
            float var3 = this.edge == var1.edge1 ? var1.dist1 : var1.dist2;
            float var4 = this.edge == var2.edge1 ? var2.dist1 : var2.dist2;
            if (var3 < var4) {
               return -1;
            } else {
               return var3 > var4 ? 1 : 0;
            }
         }
      }
   }

   private static final class Connection {
      PolygonalMap2.Node node1;
      PolygonalMap2.Node node2;
      int flags;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.Connection init(PolygonalMap2.Node var1, PolygonalMap2.Node var2, int var3) {
         this.node1 = var1;
         this.node2 = var2;
         this.flags = var3;
         return this;
      }

      PolygonalMap2.Node otherNode(PolygonalMap2.Node var1) {
         assert var1 == this.node1 || var1 == this.node2;

         return var1 == this.node1 ? this.node2 : this.node1;
      }

      boolean has(int var1) {
         return (this.flags & var1) != 0;
      }

      static PolygonalMap2.Connection alloc() {
         boolean var0;
         if (pool.isEmpty()) {
            var0 = false;
         } else {
            var0 = false;
         }

         return pool.isEmpty() ? new PolygonalMap2.Connection() : (PolygonalMap2.Connection)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class Obstacle {
      PolygonalMap2.Vehicle vehicle;
      final PolygonalMap2.EdgeRing outer = new PolygonalMap2.EdgeRing();
      final ArrayList inner = new ArrayList();
      PolygonalMap2.ImmutableRectF bounds;
      PolygonalMap2.Node nodeCrawlFront;
      PolygonalMap2.Node nodeCrawlRear;
      final ArrayList crawlNodes = new ArrayList();
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.Obstacle init(PolygonalMap2.Vehicle var1) {
         this.vehicle = var1;
         this.outer.clear();
         this.inner.clear();
         this.nodeCrawlFront = this.nodeCrawlRear = null;
         this.crawlNodes.clear();
         return this;
      }

      PolygonalMap2.Obstacle init(IsoGridSquare var1) {
         this.vehicle = null;
         this.outer.clear();
         this.inner.clear();
         this.nodeCrawlFront = this.nodeCrawlRear = null;
         this.crawlNodes.clear();
         return this;
      }

      boolean hasNode(PolygonalMap2.Node var1) {
         if (this.outer.hasNode(var1)) {
            return true;
         } else {
            for(int var2 = 0; var2 < this.inner.size(); ++var2) {
               PolygonalMap2.EdgeRing var3 = (PolygonalMap2.EdgeRing)this.inner.get(var2);
               if (var3.hasNode(var1)) {
                  return true;
               }
            }

            return false;
         }
      }

      boolean hasAdjacentNodes(PolygonalMap2.Node var1, PolygonalMap2.Node var2) {
         if (this.outer.hasAdjacentNodes(var1, var2)) {
            return true;
         } else {
            for(int var3 = 0; var3 < this.inner.size(); ++var3) {
               PolygonalMap2.EdgeRing var4 = (PolygonalMap2.EdgeRing)this.inner.get(var3);
               if (var4.hasAdjacentNodes(var1, var2)) {
                  return true;
               }
            }

            return false;
         }
      }

      boolean isPointInside(float var1, float var2, int var3) {
         if (this.outer.isPointInPolygon_WindingNumber(var1, var2, var3) != PolygonalMap2.EdgeRingHit.Inside) {
            return false;
         } else if (this.inner.isEmpty()) {
            return true;
         } else {
            for(int var4 = 0; var4 < this.inner.size(); ++var4) {
               PolygonalMap2.EdgeRing var5 = (PolygonalMap2.EdgeRing)this.inner.get(var4);
               if (var5.isPointInPolygon_WindingNumber(var1, var2, var3) != PolygonalMap2.EdgeRingHit.Outside) {
                  return false;
               }
            }

            return true;
         }
      }

      boolean isPointInside(float var1, float var2) {
         byte var3 = 0;
         return this.isPointInside(var1, var2, var3);
      }

      boolean lineSegmentIntersects(float var1, float var2, float var3, float var4) {
         if (this.outer.lineSegmentIntersects(var1, var2, var3, var4)) {
            return true;
         } else {
            for(int var5 = 0; var5 < this.inner.size(); ++var5) {
               PolygonalMap2.EdgeRing var6 = (PolygonalMap2.EdgeRing)this.inner.get(var5);
               if (var6.lineSegmentIntersects(var1, var2, var3, var4)) {
                  return true;
               }
            }

            return false;
         }
      }

      boolean isNodeInsideOf(PolygonalMap2.Node var1) {
         if (this.hasNode(var1)) {
            return false;
         } else {
            return !this.bounds.containsPoint(var1.x, var1.y) ? false : this.isPointInside(var1.x, var1.y);
         }
      }

      void getClosestPointOnEdge(float var1, float var2, PolygonalMap2.ClosestPointOnEdge var3) {
         var3.edge = null;
         var3.node = null;
         var3.distSq = Double.MAX_VALUE;
         this.outer.getClosestPointOnEdge(var1, var2, var3);

         for(int var4 = 0; var4 < this.inner.size(); ++var4) {
            PolygonalMap2.EdgeRing var5 = (PolygonalMap2.EdgeRing)this.inner.get(var4);
            var5.getClosestPointOnEdge(var1, var2, var3);
         }

      }

      boolean splitEdgeAtNearestPoint(PolygonalMap2.ClosestPointOnEdge var1, int var2, PolygonalMap2.AdjustStartEndNodeData var3) {
         if (var1.edge == null) {
            return false;
         } else {
            var3.obstacle = this;
            if (var1.node == null) {
               var3.node = PolygonalMap2.Node.alloc().init(var1.point.x, var1.point.y, var2);
               var3.newEdge = var1.edge.split(var3.node);
               var3.isNodeNew = true;
            } else {
               var3.node = var1.node;
               var3.newEdge = null;
               var3.isNodeNew = false;
            }

            return true;
         }
      }

      void unsplit(PolygonalMap2.Node var1, ArrayList var2) {
         for(int var3 = 0; var3 < var2.size(); ++var3) {
            PolygonalMap2.Edge var4 = (PolygonalMap2.Edge)var2.get(var3);
            if (var4.node1 == var1) {
               if (var3 > 0) {
                  PolygonalMap2.Edge var5 = (PolygonalMap2.Edge)var2.get(var3 - 1);
                  var5.node2 = var4.node2;

                  assert var4.node2.edges.contains(var4);

                  var4.node2.edges.remove(var4);

                  assert !var4.node2.edges.contains(var5);

                  var4.node2.edges.add(var5);
                  PolygonalMap2.instance.connectTwoNodes(var5.node1, var5.node2);
               } else {
                  ((PolygonalMap2.Edge)var2.get(var3 + 1)).node1 = ((PolygonalMap2.Edge)var2.get(var2.size() - 1)).node2;
               }

               var4.release();
               var2.remove(var3);
               break;
            }
         }

      }

      void calcBounds() {
         float var1 = Float.MAX_VALUE;
         float var2 = Float.MAX_VALUE;
         float var3 = Float.MIN_VALUE;
         float var4 = Float.MIN_VALUE;

         for(int var5 = 0; var5 < this.outer.size(); ++var5) {
            PolygonalMap2.Edge var6 = (PolygonalMap2.Edge)this.outer.get(var5);
            var1 = Math.min(var1, var6.node1.x);
            var2 = Math.min(var2, var6.node1.y);
            var3 = Math.max(var3, var6.node1.x);
            var4 = Math.max(var4, var6.node1.y);
         }

         if (this.bounds != null) {
            this.bounds.release();
         }

         float var7 = 0.01F;
         this.bounds = PolygonalMap2.ImmutableRectF.alloc().init(var1 - var7, var2 - var7, var3 - var1 + var7 * 2.0F, var4 - var2 + var7 * 2.0F);
      }

      void render(ArrayList var1, boolean var2) {
         if (!var1.isEmpty()) {
            float var3 = 0.0F;
            float var4 = var2 ? 1.0F : 0.5F;
            float var5 = var2 ? 0.0F : 0.5F;

            for(Iterator var6 = var1.iterator(); var6.hasNext(); var3 = 1.0F - var3) {
               PolygonalMap2.Edge var7 = (PolygonalMap2.Edge)var6.next();
               PolygonalMap2.Node var8 = var7.node1;
               PolygonalMap2.Node var9 = var7.node2;
               LineDrawer.addLine(var8.x, var8.y, (float)var8.z, var9.x, var9.y, (float)var9.z, var3, var4, var5, (String)null, true);
               Vector3f var10 = (new Vector3f(var9.x - var8.x, var9.y - var8.y, (float)(var9.z - var8.z))).normalize();
               Vector3f var11 = (new Vector3f(var10)).cross(0.0F, 0.0F, 1.0F).normalize();
               var10.mul(0.9F);
               LineDrawer.addLine(var9.x - var10.x * 0.1F - var11.x * 0.1F, var9.y - var10.y * 0.1F - var11.y * 0.1F, (float)var9.z, var9.x, var9.y, (float)var9.z, var3, var4, var5, (String)null, true);
               LineDrawer.addLine(var9.x - var10.x * 0.1F + var11.x * 0.1F, var9.y - var10.y * 0.1F + var11.y * 0.1F, (float)var9.z, var9.x, var9.y, (float)var9.z, var3, var4, var5, (String)null, true);
            }

            PolygonalMap2.Node var12 = ((PolygonalMap2.Edge)var1.get(0)).node1;
            LineDrawer.addLine(var12.x - 0.1F, var12.y - 0.1F, (float)var12.z, var12.x + 0.1F, var12.y + 0.1F, (float)var12.z, 1.0F, 0.0F, 0.0F, (String)null, false);
         }
      }

      void render() {
         this.render(this.outer, true);

         for(int var1 = 0; var1 < this.inner.size(); ++var1) {
            this.render((ArrayList)this.inner.get(var1), false);
         }

      }

      void connectCrawlNodes(PolygonalMap2.VisibilityGraph var1, PolygonalMap2.Obstacle var2) {
         this.connectCrawlNode(var1, var2, this.nodeCrawlFront, var2.nodeCrawlFront);
         this.connectCrawlNode(var1, var2, this.nodeCrawlFront, var2.nodeCrawlRear);
         this.connectCrawlNode(var1, var2, this.nodeCrawlRear, var2.nodeCrawlFront);
         this.connectCrawlNode(var1, var2, this.nodeCrawlRear, var2.nodeCrawlRear);

         for(int var3 = 0; var3 < this.crawlNodes.size(); var3 += 3) {
            PolygonalMap2.Node var4 = (PolygonalMap2.Node)this.crawlNodes.get(var3);
            PolygonalMap2.Node var5 = (PolygonalMap2.Node)this.crawlNodes.get(var3 + 2);

            for(int var6 = 0; var6 < var2.crawlNodes.size(); var6 += 3) {
               PolygonalMap2.Node var7 = (PolygonalMap2.Node)var2.crawlNodes.get(var6);
               PolygonalMap2.Node var8 = (PolygonalMap2.Node)var2.crawlNodes.get(var6 + 2);
               this.connectCrawlNode(var1, var2, var4, var7);
               this.connectCrawlNode(var1, var2, var4, var8);
               this.connectCrawlNode(var1, var2, var5, var7);
               this.connectCrawlNode(var1, var2, var5, var8);
            }
         }

      }

      void connectCrawlNode(PolygonalMap2.VisibilityGraph var1, PolygonalMap2.Obstacle var2, PolygonalMap2.Node var3, PolygonalMap2.Node var4) {
         if (this.isNodeInsideOf(var4)) {
            var4.flags |= 2;
            var3 = this.getClosestInteriorCrawlNode(var4.x, var4.y);
            if (var3 != null) {
               if (!var3.isConnectedTo(var4)) {
                  PolygonalMap2.instance.connectTwoNodes(var3, var4);
               }
            }
         } else if (!var3.ignore && !var4.ignore) {
            if (!var3.isConnectedTo(var4)) {
               if (var1.isVisible(var3, var4)) {
                  PolygonalMap2.instance.connectTwoNodes(var3, var4);
               }

            }
         }
      }

      PolygonalMap2.Node getClosestInteriorCrawlNode(float var1, float var2) {
         PolygonalMap2.Node var3 = null;
         float var4 = Float.MAX_VALUE;

         for(int var5 = 0; var5 < this.crawlNodes.size(); var5 += 3) {
            PolygonalMap2.Node var6 = (PolygonalMap2.Node)this.crawlNodes.get(var5 + 1);
            float var7 = IsoUtils.DistanceToSquared(var6.x, var6.y, var1, var2);
            if (var7 < var4) {
               var3 = var6;
               var4 = var7;
            }
         }

         return var3;
      }

      static PolygonalMap2.Obstacle alloc() {
         return pool.isEmpty() ? new PolygonalMap2.Obstacle() : (PolygonalMap2.Obstacle)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         this.outer.release();
         this.outer.clear();
         PolygonalMap2.EdgeRing.releaseAll(this.inner);
         this.inner.clear();
         pool.push(this);
      }

      static void releaseAll(ArrayList var0) {
         for(int var1 = 0; var1 < var0.size(); ++var1) {
            ((PolygonalMap2.Obstacle)var0.get(var1)).release();
         }

      }
   }

   static final class PathFindRequest {
      PolygonalMap2.IPathfinder finder;
      Mover mover;
      boolean bCanCrawl;
      boolean bIgnoreCrawlCost;
      boolean bCanThump;
      final ArrayList knownBlockedEdges = new ArrayList();
      float startX;
      float startY;
      float startZ;
      float targetX;
      float targetY;
      float targetZ;
      final TFloatArrayList targetXYZ = new TFloatArrayList();
      final PolygonalMap2.Path path = new PolygonalMap2.Path();
      boolean cancel = false;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.PathFindRequest init(PolygonalMap2.IPathfinder var1, Mover var2, float var3, float var4, float var5, float var6, float var7, float var8) {
         this.finder = var1;
         this.mover = var2;
         this.bCanCrawl = false;
         this.bIgnoreCrawlCost = false;
         this.bCanThump = false;
         IsoZombie var9 = (IsoZombie)Type.tryCastTo(var2, IsoZombie.class);
         if (var9 != null) {
            this.bCanCrawl = var9.isCrawling() || var9.isCanCrawlUnderVehicle();
            this.bIgnoreCrawlCost = var9.isCrawling() && !var9.isCanWalk();
            this.bCanThump = true;
         }

         this.startX = var3;
         this.startY = var4;
         this.startZ = var5;
         this.targetX = var6;
         this.targetY = var7;
         this.targetZ = var8;
         this.targetXYZ.resetQuick();
         this.path.clear();
         this.cancel = false;
         IsoGameCharacter var10 = (IsoGameCharacter)Type.tryCastTo(var2, IsoGameCharacter.class);
         if (var10 != null) {
            ArrayList var11 = var10.getMapKnowledge().getKnownBlockedEdges();

            for(int var12 = 0; var12 < var11.size(); ++var12) {
               KnownBlockedEdges var13 = (KnownBlockedEdges)var11.get(var12);
               this.knownBlockedEdges.add(KnownBlockedEdges.alloc().init(var13));
            }
         }

         return this;
      }

      void addTargetXYZ(float var1, float var2, float var3) {
         this.targetXYZ.add(var1);
         this.targetXYZ.add(var2);
         this.targetXYZ.add(var3);
      }

      static PolygonalMap2.PathFindRequest alloc() {
         return pool.isEmpty() ? new PolygonalMap2.PathFindRequest() : (PolygonalMap2.PathFindRequest)pool.pop();
      }

      public void release() {
         KnownBlockedEdges.releaseAll(this.knownBlockedEdges);
         this.knownBlockedEdges.clear();

         assert !pool.contains(this);

         pool.push(this);
      }
   }

   static final class PathNode {
      float x;
      float y;
      float z;
      int flags;

      PolygonalMap2.PathNode init(float var1, float var2, float var3, int var4) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.flags = var4;
         return this;
      }

      PolygonalMap2.PathNode init(PolygonalMap2.PathNode var1) {
         this.x = var1.x;
         this.y = var1.y;
         this.z = var1.z;
         this.flags = var1.flags;
         return this;
      }

      boolean hasFlag(int var1) {
         return (this.flags & var1) != 0;
      }
   }

   private static final class SearchNode extends ASearchNode {
      PolygonalMap2.VGAStar astar;
      PolygonalMap2.Node vgNode;
      PolygonalMap2.Square square;
      int tx;
      int ty;
      PolygonalMap2.SearchNode parent;
      static int nextID = 1;
      Integer ID;
      private static final double SQRT2 = Math.sqrt(2.0D);
      static final ArrayDeque pool = new ArrayDeque();

      SearchNode() {
         this.ID = nextID++;
      }

      PolygonalMap2.SearchNode init(PolygonalMap2.VGAStar var1, PolygonalMap2.Node var2) {
         this.setG(0.0D);
         this.astar = var1;
         this.vgNode = var2;
         this.square = null;
         this.tx = this.ty = -1;
         this.parent = null;
         return this;
      }

      PolygonalMap2.SearchNode init(PolygonalMap2.VGAStar var1, PolygonalMap2.Square var2) {
         this.setG(0.0D);
         this.astar = var1;
         this.vgNode = null;
         this.square = var2;
         this.tx = this.ty = -1;
         this.parent = null;
         return this;
      }

      PolygonalMap2.SearchNode init(PolygonalMap2.VGAStar var1, int var2, int var3) {
         this.setG(0.0D);
         this.astar = var1;
         this.vgNode = null;
         this.square = null;
         this.tx = var2;
         this.ty = var3;
         this.parent = null;
         return this;
      }

      public double h() {
         return this.dist(this.astar.goalNode.searchNode);
      }

      public double c(ISearchNode var1) {
         PolygonalMap2.SearchNode var2 = (PolygonalMap2.SearchNode)var1;
         double var3 = 0.0D;
         boolean var5 = this.astar.mover instanceof IsoZombie && ((IsoZombie)this.astar.mover).bCrawling;
         boolean var6 = !(this.astar.mover instanceof IsoZombie) || ((IsoZombie)this.astar.mover).bCrawling;
         if (var6 && this.square != null && var2.square != null) {
            if (this.square.x == var2.square.x - 1 && this.square.y == var2.square.y) {
               if (var2.square.has(2048)) {
                  var3 = !var5 && var2.square.has(1048576) ? 20.0D : 200.0D;
               }
            } else if (this.square.x == var2.square.x + 1 && this.square.y == var2.square.y) {
               if (this.square.has(2048)) {
                  var3 = !var5 && this.square.has(1048576) ? 20.0D : 200.0D;
               }
            } else if (this.square.y == var2.square.y - 1 && this.square.x == var2.square.x) {
               if (var2.square.has(4096)) {
                  var3 = !var5 && var2.square.has(2097152) ? 20.0D : 200.0D;
               }
            } else if (this.square.y == var2.square.y + 1 && this.square.x == var2.square.x && this.square.has(4096)) {
               var3 = !var5 && this.square.has(2097152) ? 20.0D : 200.0D;
            }
         }

         if (var2.square != null && var2.square.has(131072)) {
            var3 = 20.0D;
         }

         if (this.vgNode != null && var2.vgNode != null) {
            for(int var7 = 0; var7 < this.vgNode.visible.size(); ++var7) {
               PolygonalMap2.Connection var8 = (PolygonalMap2.Connection)this.vgNode.visible.get(var7);
               if (var8.otherNode(this.vgNode) == var2.vgNode) {
                  if ((this.vgNode.square == null || !this.vgNode.square.has(131072)) && var8.has(2)) {
                     var3 = 20.0D;
                  }
                  break;
               }
            }
         }

         PolygonalMap2.Square var9 = this.square == null ? PolygonalMap2.instance.getSquare((int)this.vgNode.x, (int)this.vgNode.y, 0) : this.square;
         PolygonalMap2.Square var11 = var2.square == null ? PolygonalMap2.instance.getSquare((int)var2.vgNode.x, (int)var2.vgNode.y, 0) : var2.square;
         if (var9 != null && var11 != null) {
            if (var9.x == var11.x - 1 && var9.y == var11.y) {
               if (var11.has(32768)) {
                  var3 = 20.0D;
               }
            } else if (var9.x == var11.x + 1 && var9.y == var11.y) {
               if (var9.has(32768)) {
                  var3 = 20.0D;
               }
            } else if (var9.y == var11.y - 1 && var9.x == var11.x) {
               if (var11.has(65536)) {
                  var3 = 20.0D;
               }
            } else if (var9.y == var11.y + 1 && var9.x == var11.x && var9.has(65536)) {
               var3 = 20.0D;
            }

            if (var5) {
               if (var9.x == var11.x - 1 && var9.y == var11.y) {
                  if (var11.has(2) && var11.has(8192)) {
                     var3 = 20.0D;
                  }
               } else if (var9.x == var11.x + 1 && var9.y == var11.y) {
                  if (var9.has(2) && var9.has(8192)) {
                     var3 = 20.0D;
                  }
               } else if (var9.y == var11.y - 1 && var9.x == var11.x) {
                  if (var11.has(4) && var11.has(16384)) {
                     var3 = 20.0D;
                  }
               } else if (var9.y == var11.y + 1 && var9.x == var11.x && var9.has(4) && var9.has(16384)) {
                  var3 = 20.0D;
               }
            }
         }

         boolean var10 = this.vgNode != null && this.vgNode.hasFlag(2);
         boolean var12 = var2.vgNode != null && var2.vgNode.hasFlag(2);
         if (!var10 && var12 && !this.astar.bIgnoreCrawlCost) {
            var3 += 10.0D;
         }

         if (var2.square != null) {
            var3 += (double)var2.square.cost;
         }

         return this.dist(var2) + var3;
      }

      public void getSuccessors(ArrayList var1) {
         ArrayList var2 = var1;
         int var3;
         PolygonalMap2.SearchNode var6;
         if (this.vgNode != null) {
            if (this.vgNode.graphs != null) {
               for(var3 = 0; var3 < this.vgNode.graphs.size(); ++var3) {
                  PolygonalMap2.VisibilityGraph var4 = (PolygonalMap2.VisibilityGraph)this.vgNode.graphs.get(var3);
                  if (!var4.created) {
                     var4.create();
                  }
               }
            }

            for(var3 = 0; var3 < this.vgNode.visible.size(); ++var3) {
               PolygonalMap2.Connection var8 = (PolygonalMap2.Connection)this.vgNode.visible.get(var3);
               PolygonalMap2.Node var5 = var8.otherNode(this.vgNode);
               var6 = this.astar.getSearchNode(var5);
               if ((this.vgNode.square == null || var6.square == null || !this.astar.isKnownBlocked(this.vgNode.square, var6.square)) && (this.astar.bCanCrawl || !var5.hasFlag(2)) && (this.astar.bCanThump || !var8.has(2))) {
                  var2.add(var6);
               }
            }

            if (!this.vgNode.hasFlag(8)) {
               return;
            }
         }

         if (this.square != null) {
            for(var3 = -1; var3 <= 1; ++var3) {
               for(int var9 = -1; var9 <= 1; ++var9) {
                  if (var9 != 0 || var3 != 0) {
                     PolygonalMap2.Square var12 = PolygonalMap2.instance.getSquare(this.square.x + var9, this.square.y + var3, this.square.z);
                     if (var12 != null && !this.astar.isSquareInCluster(var12) && !this.astar.canNotMoveBetween(this.square, var12, false)) {
                        var6 = this.astar.getSearchNode(var12);
                        if (var2.contains(var6)) {
                           boolean var7 = false;
                        } else {
                           var2.add(var6);
                        }
                     }
                  }
               }
            }

            PolygonalMap2.Square var10;
            PolygonalMap2.SearchNode var11;
            boolean var13;
            if (this.square.z > 0) {
               var10 = PolygonalMap2.instance.getSquare(this.square.x, this.square.y + 1, this.square.z - 1);
               if (var10 != null && var10.has(64) && !this.astar.isSquareInCluster(var10)) {
                  var11 = this.astar.getSearchNode(var10);
                  if (var2.contains(var11)) {
                     var13 = false;
                  } else {
                     var2.add(var11);
                  }
               }

               var10 = PolygonalMap2.instance.getSquare(this.square.x + 1, this.square.y, this.square.z - 1);
               if (var10 != null && var10.has(8) && !this.astar.isSquareInCluster(var10)) {
                  var11 = this.astar.getSearchNode(var10);
                  if (var2.contains(var11)) {
                     var13 = false;
                  } else {
                     var2.add(var11);
                  }
               }
            }

            if (this.square.z < 8 && this.square.has(64)) {
               var10 = PolygonalMap2.instance.getSquare(this.square.x, this.square.y - 1, this.square.z + 1);
               if (var10 != null && !this.astar.isSquareInCluster(var10)) {
                  var11 = this.astar.getSearchNode(var10);
                  if (var2.contains(var11)) {
                     var13 = false;
                  } else {
                     var2.add(var11);
                  }
               }
            }

            if (this.square.z < 8 && this.square.has(8)) {
               var10 = PolygonalMap2.instance.getSquare(this.square.x - 1, this.square.y, this.square.z + 1);
               if (var10 != null && !this.astar.isSquareInCluster(var10)) {
                  var11 = this.astar.getSearchNode(var10);
                  if (var2.contains(var11)) {
                     var13 = false;
                  } else {
                     var2.add(var11);
                  }
               }
            }
         }

      }

      public ISearchNode getParent() {
         return this.parent;
      }

      public void setParent(ISearchNode var1) {
         this.parent = (PolygonalMap2.SearchNode)var1;
      }

      public Integer keyCode() {
         return this.ID;
      }

      public float getX() {
         if (this.square != null) {
            return (float)this.square.x + 0.5F;
         } else {
            return this.vgNode != null ? this.vgNode.x : (float)this.tx;
         }
      }

      public float getY() {
         if (this.square != null) {
            return (float)this.square.y + 0.5F;
         } else {
            return this.vgNode != null ? this.vgNode.y : (float)this.ty;
         }
      }

      public float getZ() {
         if (this.square != null) {
            return (float)this.square.z;
         } else {
            return this.vgNode != null ? (float)this.vgNode.z : 0.0F;
         }
      }

      public double dist(PolygonalMap2.SearchNode var1) {
         if (this.square != null && var1.square != null && Math.abs(this.square.x - var1.square.x) <= 1 && Math.abs(this.square.y - var1.square.y) <= 1) {
            return this.square.x != var1.square.x && this.square.y != var1.square.y ? SQRT2 : 1.0D;
         } else {
            float var2 = this.getX();
            float var3 = this.getY();
            float var4 = var1.getX();
            float var5 = var1.getY();
            return Math.sqrt(Math.pow((double)(var2 - var4), 2.0D) + Math.pow((double)(var3 - var5), 2.0D));
         }
      }

      float getApparentZ() {
         if (this.square == null) {
            return (float)this.vgNode.z;
         } else if (!this.square.has(8) && !this.square.has(64)) {
            if (!this.square.has(16) && !this.square.has(128)) {
               return !this.square.has(32) && !this.square.has(256) ? (float)this.square.z : (float)this.square.z + 0.25F;
            } else {
               return (float)this.square.z + 0.5F;
            }
         } else {
            return (float)this.square.z + 0.75F;
         }
      }

      static PolygonalMap2.SearchNode alloc() {
         return pool.isEmpty() ? new PolygonalMap2.SearchNode() : (PolygonalMap2.SearchNode)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class Edge {
      PolygonalMap2.Node node1;
      PolygonalMap2.Node node2;
      PolygonalMap2.Obstacle obstacle;
      PolygonalMap2.EdgeRing edgeRing;
      final ArrayList intersections = new ArrayList();
      final Vector2 normal = new Vector2();
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.Edge init(PolygonalMap2.Node var1, PolygonalMap2.Node var2, PolygonalMap2.Obstacle var3, PolygonalMap2.EdgeRing var4) {
         if (var1 == null) {
            boolean var5 = true;
         }

         this.node1 = var1;
         this.node2 = var2;
         var1.edges.add(this);
         var2.edges.add(this);
         this.obstacle = var3;
         this.edgeRing = var4;
         this.intersections.clear();
         this.normal.set(var2.x - var1.x, var2.y - var1.y);
         this.normal.normalize();
         this.normal.rotate(1.5707964F);
         return this;
      }

      boolean hasNode(PolygonalMap2.Node var1) {
         return var1 == this.node1 || var1 == this.node2;
      }

      void getClosestPointOnEdge(float var1, float var2, PolygonalMap2.ClosestPointOnEdge var3) {
         if (this.node1.isConnectedTo(this.node2)) {
            float var4 = this.node1.x;
            float var5 = this.node1.y;
            float var6 = this.node2.x;
            float var7 = this.node2.y;
            double var8 = (double)((var1 - var4) * (var6 - var4) + (var2 - var5) * (var7 - var5)) / (Math.pow((double)(var6 - var4), 2.0D) + Math.pow((double)(var7 - var5), 2.0D));
            double var10 = (double)var4 + var8 * (double)(var6 - var4);
            double var12 = (double)var5 + var8 * (double)(var7 - var5);
            PolygonalMap2.Node var14 = null;
            if (var8 <= 0.0D) {
               var10 = (double)var4;
               var12 = (double)var5;
               var14 = this.node1;
            } else if (var8 >= 1.0D) {
               var10 = (double)var6;
               var12 = (double)var7;
               var14 = this.node2;
            }

            double var15 = ((double)var1 - var10) * ((double)var1 - var10) + ((double)var2 - var12) * ((double)var2 - var12);
            if (var15 < var3.distSq) {
               var3.point.set((float)var10, (float)var12);
               var3.distSq = var15;
               var3.edge = this;
               var3.node = var14;
            }

         }
      }

      boolean isPointOn(float var1, float var2) {
         if (!this.node1.isConnectedTo(this.node2)) {
            return false;
         } else {
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
      }

      PolygonalMap2.Edge split(PolygonalMap2.Node var1) {
         PolygonalMap2.Edge var2 = alloc().init(var1, this.node2, this.obstacle, this.edgeRing);
         this.edgeRing.add(this.edgeRing.indexOf(this) + 1, var2);
         PolygonalMap2.instance.breakConnection(this.node1, this.node2);
         this.node2.edges.remove(this);
         this.node2 = var1;
         this.node2.edges.add(this);
         return var2;
      }

      static PolygonalMap2.Edge alloc() {
         return pool.isEmpty() ? new PolygonalMap2.Edge() : (PolygonalMap2.Edge)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         this.node1 = null;
         this.node2 = null;
         this.obstacle = null;
         this.edgeRing = null;
         this.intersections.clear();
         pool.push(this);
      }

      static void releaseAll(ArrayList var0) {
         for(int var1 = 0; var1 < var0.size(); ++var1) {
            ((PolygonalMap2.Edge)var0.get(var1)).release();
         }

      }
   }

   private static final class EdgeRing extends ArrayList {
      static final ArrayDeque pool = new ArrayDeque();

      public boolean add(PolygonalMap2.Edge var1) {
         assert !this.contains(var1);

         return super.add(var1);
      }

      public boolean hasNode(PolygonalMap2.Node var1) {
         for(int var2 = 0; var2 < this.size(); ++var2) {
            PolygonalMap2.Edge var3 = (PolygonalMap2.Edge)this.get(var2);
            if (var3.hasNode(var1)) {
               return true;
            }
         }

         return false;
      }

      boolean hasAdjacentNodes(PolygonalMap2.Node var1, PolygonalMap2.Node var2) {
         for(int var3 = 0; var3 < this.size(); ++var3) {
            PolygonalMap2.Edge var4 = (PolygonalMap2.Edge)this.get(var3);
            if (var4.hasNode(var1) && var4.hasNode(var2)) {
               return true;
            }
         }

         return false;
      }

      boolean isPointInPolygon_CrossingNumber(float var1, float var2) {
         int var3 = 0;

         for(int var4 = 0; var4 < this.size(); ++var4) {
            PolygonalMap2.Edge var5 = (PolygonalMap2.Edge)this.get(var4);
            if (var5.node1.y <= var2 && var5.node2.y > var2 || var5.node1.y > var2 && var5.node2.y <= var2) {
               float var6 = (var2 - var5.node1.y) / (var5.node2.y - var5.node1.y);
               if (var1 < var5.node1.x + var6 * (var5.node2.x - var5.node1.x)) {
                  ++var3;
               }
            }
         }

         return var3 % 2 == 1;
      }

      float isLeft(float var1, float var2, float var3, float var4, float var5, float var6) {
         return (var3 - var1) * (var6 - var2) - (var5 - var1) * (var4 - var2);
      }

      PolygonalMap2.EdgeRingHit isPointInPolygon_WindingNumber(float var1, float var2, int var3) {
         int var4 = 0;

         for(int var5 = 0; var5 < this.size(); ++var5) {
            PolygonalMap2.Edge var6 = (PolygonalMap2.Edge)this.get(var5);
            if ((var3 & 16) != 0 && var6.isPointOn(var1, var2)) {
               return PolygonalMap2.EdgeRingHit.OnEdge;
            }

            if (var6.node1.y <= var2) {
               if (var6.node2.y > var2 && this.isLeft(var6.node1.x, var6.node1.y, var6.node2.x, var6.node2.y, var1, var2) > 0.0F) {
                  ++var4;
               }
            } else if (var6.node2.y <= var2 && this.isLeft(var6.node1.x, var6.node1.y, var6.node2.x, var6.node2.y, var1, var2) < 0.0F) {
               --var4;
            }
         }

         return var4 == 0 ? PolygonalMap2.EdgeRingHit.Outside : PolygonalMap2.EdgeRingHit.Inside;
      }

      boolean lineSegmentIntersects(float var1, float var2, float var3, float var4) {
         Vector2 var5 = PolygonalMap2.L_lineSegmentIntersects.v1;
         var5.set(var3 - var1, var4 - var2);
         float var6 = var5.getLength();
         var5.normalize();
         float var7 = var5.x;
         float var8 = var5.y;

         for(int var9 = 0; var9 < this.size(); ++var9) {
            PolygonalMap2.Edge var10 = (PolygonalMap2.Edge)this.get(var9);
            if (!var10.isPointOn(var1, var2) && !var10.isPointOn(var3, var4)) {
               float var11 = var10.normal.dot(var5);
               if (var11 >= 0.01F) {
               }

               float var12 = var10.node1.x;
               float var13 = var10.node1.y;
               float var14 = var10.node2.x;
               float var15 = var10.node2.y;
               float var16 = var1 - var12;
               float var17 = var2 - var13;
               float var18 = var14 - var12;
               float var19 = var15 - var13;
               float var20 = 1.0F / (var19 * var7 - var18 * var8);
               float var21 = (var18 * var17 - var19 * var16) * var20;
               if (var21 >= 0.0F && var21 <= var6) {
                  float var22 = (var17 * var7 - var16 * var8) * var20;
                  if (var22 >= 0.0F && var22 <= 1.0F) {
                     return true;
                  }
               }
            }
         }

         if (this.isPointInPolygon_WindingNumber((var1 + var3) / 2.0F, (var2 + var4) / 2.0F, 0) != PolygonalMap2.EdgeRingHit.Outside) {
            return true;
         } else {
            return false;
         }
      }

      void getClosestPointOnEdge(float var1, float var2, PolygonalMap2.ClosestPointOnEdge var3) {
         for(int var4 = 0; var4 < this.size(); ++var4) {
            PolygonalMap2.Edge var5 = (PolygonalMap2.Edge)this.get(var4);
            var5.getClosestPointOnEdge(var1, var2, var3);
         }

      }

      static PolygonalMap2.EdgeRing alloc() {
         return pool.isEmpty() ? new PolygonalMap2.EdgeRing() : (PolygonalMap2.EdgeRing)pool.pop();
      }

      public void release() {
         PolygonalMap2.Edge.releaseAll(this);
      }

      static void releaseAll(ArrayList var0) {
         for(int var1 = 0; var1 < var0.size(); ++var1) {
            ((PolygonalMap2.EdgeRing)var0.get(var1)).release();
         }

      }
   }

   private static final class Chunk {
      short wx;
      short wy;
      PolygonalMap2.Square[][][] squares = new PolygonalMap2.Square[10][10][8];
      final PolygonalMap2.ChunkData collision = new PolygonalMap2.ChunkData();
      static final ArrayDeque pool = new ArrayDeque();

      void init(int var1, int var2) {
         this.wx = (short)var1;
         this.wy = (short)var2;
      }

      PolygonalMap2.Square getSquare(int var1, int var2, int var3) {
         var1 -= this.wx * 10;
         var2 -= this.wy * 10;
         return var1 >= 0 && var1 < 10 && var2 >= 0 && var2 < 10 && var3 >= 0 && var3 < 8 ? this.squares[var1][var2][var3] : null;
      }

      void setData(PolygonalMap2.ChunkUpdateTask var1) {
         for(int var2 = 0; var2 < 8; ++var2) {
            for(int var3 = 0; var3 < 10; ++var3) {
               for(int var4 = 0; var4 < 10; ++var4) {
                  PolygonalMap2.Square var5 = this.squares[var4][var3][var2];
                  int var6 = var1.data[var4][var3][var2];
                  if (var6 == 0) {
                     if (var5 != null) {
                        var5.release();
                        this.squares[var4][var3][var2] = null;
                     }
                  } else {
                     if (var5 == null) {
                        var5 = PolygonalMap2.Square.alloc();
                        this.squares[var4][var3][var2] = var5;
                     }

                     var5.init(this.wx * 10 + var4, this.wy * 10 + var3, var2);
                     var5.bits = var6;
                     var5.cost = var1.cost[var4][var3][var2];
                  }
               }
            }
         }

         ++PolygonalMap2.ChunkDataZ.EPOCH;
      }

      boolean setData(PolygonalMap2.SquareUpdateTask var1) {
         int var2 = var1.x - this.wx * 10;
         int var3 = var1.y - this.wy * 10;
         if (var2 >= 0 && var2 < 10) {
            if (var3 >= 0 && var3 < 10) {
               PolygonalMap2.Square var4 = this.squares[var2][var3][var1.z];
               if (var1.bits == 0) {
                  if (var4 != null) {
                     var4.release();
                     this.squares[var2][var3][var1.z] = null;
                     return true;
                  }
               } else {
                  if (var4 == null) {
                     var4 = PolygonalMap2.Square.alloc().init(var1.x, var1.y, var1.z);
                     this.squares[var2][var3][var1.z] = var4;
                  }

                  if (var4.bits != var1.bits || var4.cost != var1.cost) {
                     var4.bits = var1.bits;
                     var4.cost = var1.cost;
                     return true;
                  }
               }

               return false;
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      static PolygonalMap2.Chunk alloc() {
         return pool.isEmpty() ? new PolygonalMap2.Chunk() : (PolygonalMap2.Chunk)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class L_render {
      static final Vector2f vector2f = new Vector2f();
   }

   public interface IPathfinder {
      void Succeeded(PolygonalMap2.Path var1, Mover var2);

      void Failed(Mover var1);
   }

   private static final class SquareUpdateTask {
      PolygonalMap2 map;
      int x;
      int y;
      int z;
      int bits;
      short cost;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.SquareUpdateTask init(PolygonalMap2 var1, IsoGridSquare var2) {
         this.map = var1;
         this.x = var2.x;
         this.y = var2.y;
         this.z = var2.z;
         this.bits = getBits(var2);
         this.cost = getCost(var2);
         return this;
      }

      void execute() {
         PolygonalMap2.Chunk var1 = this.map.getChunkFromChunkPos(this.x / 10, this.y / 10);
         if (var1 != null && var1.setData(this)) {
            ++PolygonalMap2.ChunkDataZ.EPOCH;
            this.map.rebuild = true;
         }

      }

      static int getBits(IsoGridSquare var0) {
         int var1 = 0;
         if (var0.Is(IsoFlagType.solidfloor)) {
            var1 |= 512;
         }

         if (var0.isSolid()) {
            var1 |= 1;
         }

         if (var0.isSolidTrans()) {
            var1 |= 1024;
         }

         if (var0.Is(IsoFlagType.collideW)) {
            var1 |= 2;
         }

         if (var0.Is(IsoFlagType.collideN)) {
            var1 |= 4;
         }

         if (var0.Has(IsoObjectType.stairsTW)) {
            var1 |= 8;
         }

         if (var0.Has(IsoObjectType.stairsMW)) {
            var1 |= 16;
         }

         if (var0.Has(IsoObjectType.stairsBW)) {
            var1 |= 32;
         }

         if (var0.Has(IsoObjectType.stairsTN)) {
            var1 |= 64;
         }

         if (var0.Has(IsoObjectType.stairsMN)) {
            var1 |= 128;
         }

         if (var0.Has(IsoObjectType.stairsBN)) {
            var1 |= 256;
         }

         if (var0.Is(IsoFlagType.windowW) || var0.Is(IsoFlagType.WindowW)) {
            var1 |= 2050;
            if (isWindowUnblocked(var0, false)) {
               var1 |= 1048576;
            }
         }

         if (var0.Is(IsoFlagType.windowN) || var0.Is(IsoFlagType.WindowN)) {
            var1 |= 4100;
            if (isWindowUnblocked(var0, true)) {
               var1 |= 2097152;
            }
         }

         if (var0.Is(IsoFlagType.canPathW)) {
            var1 |= 8192;
         }

         if (var0.Is(IsoFlagType.canPathN)) {
            var1 |= 16384;
         }

         for(int var2 = 0; var2 < var0.getSpecialObjects().size(); ++var2) {
            IsoObject var3 = (IsoObject)var0.getSpecialObjects().get(var2);
            IsoDirections var4 = IsoDirections.Max;
            if (var3 instanceof IsoDoor) {
               var4 = ((IsoDoor)var3).getSpriteEdge(false);
               if (((IsoDoor)var3).IsOpen()) {
                  var4 = IsoDirections.Max;
               }
            } else if (var3 instanceof IsoThumpable && ((IsoThumpable)var3).isDoor()) {
               var4 = ((IsoThumpable)var3).getSpriteEdge(false);
               if (((IsoThumpable)var3).IsOpen()) {
                  var4 = IsoDirections.Max;
               }
            }

            if (var4 == IsoDirections.W) {
               var1 |= 8192;
               var1 |= 2;
            } else if (var4 == IsoDirections.N) {
               var1 |= 16384;
               var1 |= 4;
            } else if (var4 == IsoDirections.S) {
               var1 |= 524288;
            } else if (var4 == IsoDirections.E) {
               var1 |= 262144;
            }
         }

         if (var0.Is(IsoFlagType.DoorWallW)) {
            var1 |= 8192;
            var1 |= 2;
         }

         if (var0.Is(IsoFlagType.DoorWallN)) {
            var1 |= 16384;
            var1 |= 4;
         }

         if (hasSquareThumpable(var0)) {
            var1 |= 8192;
            var1 |= 16384;
            var1 |= 131072;
         }

         if (hasWallThumpableN(var0)) {
            var1 |= 81920;
         }

         if (hasWallThumpableW(var0)) {
            var1 |= 40960;
         }

         return var1;
      }

      static boolean isWindowUnblocked(IsoGridSquare var0, boolean var1) {
         for(int var2 = 0; var2 < var0.getSpecialObjects().size(); ++var2) {
            IsoObject var3 = (IsoObject)var0.getSpecialObjects().get(var2);
            if (var3 instanceof IsoThumpable) {
               IsoThumpable var4 = (IsoThumpable)var3;
               if (var4.isWindow() && var1 == var4.north) {
                  if (var4.isBarricaded()) {
                     return false;
                  }

                  return true;
               }
            }

            if (var3 instanceof IsoWindow) {
               IsoWindow var6 = (IsoWindow)var3;
               if (var1 == var6.north) {
                  if (var6.isBarricaded()) {
                     return false;
                  }

                  if (var6.isInvincible()) {
                     return false;
                  }

                  if (var6.IsOpen()) {
                     return true;
                  }

                  if (var6.isDestroyed() && var6.isGlassRemoved()) {
                     return true;
                  }

                  return false;
               }
            }
         }

         IsoObject var5 = var0.getWindowFrame(var1);
         if (IsoWindowFrame.canClimbThrough(var5, (IsoGameCharacter)null)) {
            return true;
         } else {
            return false;
         }
      }

      static boolean hasSquareThumpable(IsoGridSquare var0) {
         int var1;
         for(var1 = 0; var1 < var0.getSpecialObjects().size(); ++var1) {
            IsoThumpable var2 = (IsoThumpable)Type.tryCastTo((IsoObject)var0.getSpecialObjects().get(var1), IsoThumpable.class);
            if (var2 != null && var2.isThumpable() && var2.isBlockAllTheSquare()) {
               return true;
            }
         }

         for(var1 = 0; var1 < var0.getObjects().size(); ++var1) {
            IsoObject var3 = (IsoObject)var0.getObjects().get(var1);
            if (var3.isMovedThumpable()) {
               return true;
            }
         }

         return false;
      }

      static boolean hasWallThumpableN(IsoGridSquare var0) {
         IsoGridSquare var1 = var0.getAdjacentSquare(IsoDirections.N);
         if (var1 == null) {
            return false;
         } else {
            for(int var2 = 0; var2 < var0.getSpecialObjects().size(); ++var2) {
               IsoThumpable var3 = (IsoThumpable)Type.tryCastTo((IsoObject)var0.getSpecialObjects().get(var2), IsoThumpable.class);
               if (var3 != null && !var3.canClimbThrough((IsoGameCharacter)null) && !var3.canClimbOver((IsoGameCharacter)null) && var3.isThumpable() && !var3.isBlockAllTheSquare() && !var3.isDoor() && var3.TestCollide((IsoMovingObject)null, var0, var1)) {
                  return true;
               }
            }

            return false;
         }
      }

      static boolean hasWallThumpableW(IsoGridSquare var0) {
         IsoGridSquare var1 = var0.getAdjacentSquare(IsoDirections.W);
         if (var1 == null) {
            return false;
         } else {
            for(int var2 = 0; var2 < var0.getSpecialObjects().size(); ++var2) {
               IsoThumpable var3 = (IsoThumpable)Type.tryCastTo((IsoObject)var0.getSpecialObjects().get(var2), IsoThumpable.class);
               if (var3 != null && !var3.canClimbThrough((IsoGameCharacter)null) && !var3.canClimbOver((IsoGameCharacter)null) && var3.isThumpable() && !var3.isBlockAllTheSquare() && !var3.isDoor() && var3.TestCollide((IsoMovingObject)null, var0, var1)) {
                  return true;
               }
            }

            return false;
         }
      }

      static short getCost(IsoGridSquare var0) {
         short var1 = 0;
         if (var0.HasTree() || var0.getProperties().Is("Bush")) {
            var1 = (short)(var1 + 5);
         }

         return var1;
      }

      static PolygonalMap2.SquareUpdateTask alloc() {
         synchronized(pool) {
            return pool.isEmpty() ? new PolygonalMap2.SquareUpdateTask() : (PolygonalMap2.SquareUpdateTask)pool.pop();
         }
      }

      public void release() {
         synchronized(pool) {
            assert !pool.contains(this);

            pool.push(this);
         }
      }
   }

   private final class PMThread extends Thread {
      public boolean bStop;
      public final Object notifier = new Object();

      public void run() {
         while(!this.bStop) {
            try {
               this.runInner();
            } catch (Exception var2) {
               ExceptionLogger.logException(var2);
            }
         }

      }

      private void runInner() {
         MPStatistic.getInstance().PolyPathThread.Start();
         PolygonalMap2.this.sync.startFrame();
         synchronized(PolygonalMap2.this.renderLock) {
            PolygonalMap2.instance.updateThread();
         }

         PolygonalMap2.this.sync.endFrame();
         MPStatistic.getInstance().PolyPathThread.End();

         while(this.shouldWait()) {
            synchronized(this.notifier) {
               try {
                  this.notifier.wait();
               } catch (InterruptedException var4) {
               }
            }
         }

      }

      private boolean shouldWait() {
         if (this.bStop) {
            return false;
         } else if (!PolygonalMap2.instance.chunkTaskQueue.isEmpty()) {
            return false;
         } else if (!PolygonalMap2.instance.squareTaskQueue.isEmpty()) {
            return false;
         } else if (!PolygonalMap2.instance.vehicleTaskQueue.isEmpty()) {
            return false;
         } else if (!PolygonalMap2.instance.requestTaskQueue.isEmpty()) {
            return false;
         } else {
            return PolygonalMap2.instance.requests.isEmpty();
         }
      }

      void wake() {
         synchronized(this.notifier) {
            this.notifier.notify();
         }
      }
   }

   private static final class ChunkUpdateTask implements PolygonalMap2.IChunkTask {
      PolygonalMap2 map;
      int wx;
      int wy;
      final int[][][] data = new int[10][10][8];
      final short[][][] cost = new short[10][10][8];
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.ChunkUpdateTask init(PolygonalMap2 var1, IsoChunk var2) {
         this.map = var1;
         this.wx = var2.wx;
         this.wy = var2.wy;

         for(int var3 = 0; var3 < 8; ++var3) {
            for(int var4 = 0; var4 < 10; ++var4) {
               for(int var5 = 0; var5 < 10; ++var5) {
                  IsoGridSquare var6 = var2.getGridSquare(var5, var4, var3);
                  if (var6 == null) {
                     this.data[var5][var4][var3] = 0;
                     this.cost[var5][var4][var3] = 0;
                  } else {
                     this.data[var5][var4][var3] = PolygonalMap2.SquareUpdateTask.getBits(var6);
                     this.cost[var5][var4][var3] = PolygonalMap2.SquareUpdateTask.getCost(var6);
                  }
               }
            }
         }

         return this;
      }

      public void execute() {
         PolygonalMap2.Chunk var1 = this.map.allocChunkIfNeeded(this.wx, this.wy);
         var1.setData(this);
      }

      static PolygonalMap2.ChunkUpdateTask alloc() {
         synchronized(pool) {
            return pool.isEmpty() ? new PolygonalMap2.ChunkUpdateTask() : (PolygonalMap2.ChunkUpdateTask)pool.pop();
         }
      }

      public void release() {
         synchronized(pool) {
            assert !pool.contains(this);

            pool.push(this);
         }
      }
   }

   private static final class ChunkRemoveTask implements PolygonalMap2.IChunkTask {
      PolygonalMap2 map;
      int wx;
      int wy;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.ChunkRemoveTask init(PolygonalMap2 var1, IsoChunk var2) {
         this.map = var1;
         this.wx = var2.wx;
         this.wy = var2.wy;
         return this;
      }

      public void execute() {
         PolygonalMap2.Cell var1 = this.map.getCellFromChunkPos(this.wx, this.wy);
         var1.removeChunk(this.wx, this.wy);
      }

      static PolygonalMap2.ChunkRemoveTask alloc() {
         synchronized(pool) {
            return pool.isEmpty() ? new PolygonalMap2.ChunkRemoveTask() : (PolygonalMap2.ChunkRemoveTask)pool.pop();
         }
      }

      public void release() {
         synchronized(pool) {
            assert !pool.contains(this);

            pool.push(this);
         }
      }
   }

   private static final class VehicleAddTask implements PolygonalMap2.IVehicleTask {
      PolygonalMap2 map;
      BaseVehicle vehicle;
      final PolygonalMap2.VehiclePoly poly = new PolygonalMap2.VehiclePoly();
      final PolygonalMap2.VehiclePoly polyPlusRadius = new PolygonalMap2.VehiclePoly();
      final TFloatArrayList crawlOffsets = new TFloatArrayList();
      float upVectorDot;
      static final ArrayDeque pool = new ArrayDeque();

      public void init(PolygonalMap2 var1, BaseVehicle var2) {
         this.map = var1;
         this.vehicle = var2;
         this.poly.init(var2.getPoly());
         this.polyPlusRadius.init(var2.getPolyPlusRadius());
         this.crawlOffsets.resetQuick();
         this.crawlOffsets.addAll(var2.getScript().getCrawlOffsets());
         this.upVectorDot = var2.getUpVectorDot();
      }

      public void execute() {
         PolygonalMap2.Vehicle var1 = PolygonalMap2.Vehicle.alloc();
         var1.poly.init(this.poly);
         var1.polyPlusRadius.init(this.polyPlusRadius);
         var1.crawlOffsets.resetQuick();
         var1.crawlOffsets.addAll(this.crawlOffsets);
         var1.upVectorDot = this.upVectorDot;
         this.map.vehicles.add(var1);
         this.map.vehicleMap.put(this.vehicle, var1);
         this.vehicle = null;
      }

      static PolygonalMap2.VehicleAddTask alloc() {
         synchronized(pool) {
            return pool.isEmpty() ? new PolygonalMap2.VehicleAddTask() : (PolygonalMap2.VehicleAddTask)pool.pop();
         }
      }

      public void release() {
         synchronized(pool) {
            assert !pool.contains(this);

            pool.push(this);
         }
      }
   }

   private static final class VehicleState {
      BaseVehicle vehicle;
      float x;
      float y;
      float z;
      final Vector3f forward = new Vector3f();
      final PolygonalMap2.VehiclePoly polyPlusRadius = new PolygonalMap2.VehiclePoly();
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.VehicleState init(BaseVehicle var1) {
         this.vehicle = var1;
         this.x = var1.x;
         this.y = var1.y;
         this.z = var1.z;
         var1.getForwardVector(this.forward);
         this.polyPlusRadius.init(var1.getPolyPlusRadius());
         return this;
      }

      boolean check() {
         boolean var1 = this.x != this.vehicle.x || this.y != this.vehicle.y || (int)this.z != (int)this.vehicle.z;
         if (!var1) {
            BaseVehicle.Vector3fObjectPool var2 = (BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get();
            Vector3f var3 = this.vehicle.getForwardVector((Vector3f)var2.alloc());
            var1 = this.forward.dot(var3) < 0.999F;
            if (var1) {
               this.forward.set((Vector3fc)var3);
            }

            var2.release(var3);
         }

         if (var1) {
            this.x = this.vehicle.x;
            this.y = this.vehicle.y;
            this.z = this.vehicle.z;
         }

         return var1;
      }

      static PolygonalMap2.VehicleState alloc() {
         return pool.isEmpty() ? new PolygonalMap2.VehicleState() : (PolygonalMap2.VehicleState)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class VehicleUpdateTask implements PolygonalMap2.IVehicleTask {
      PolygonalMap2 map;
      BaseVehicle vehicle;
      final PolygonalMap2.VehiclePoly poly = new PolygonalMap2.VehiclePoly();
      final PolygonalMap2.VehiclePoly polyPlusRadius = new PolygonalMap2.VehiclePoly();
      float upVectorDot;
      static final ArrayDeque pool = new ArrayDeque();

      public void init(PolygonalMap2 var1, BaseVehicle var2) {
         this.map = var1;
         this.vehicle = var2;
         this.poly.init(var2.getPoly());
         this.polyPlusRadius.init(var2.getPolyPlusRadius());
         this.upVectorDot = var2.getUpVectorDot();
      }

      public void execute() {
         PolygonalMap2.Vehicle var1 = (PolygonalMap2.Vehicle)this.map.vehicleMap.get(this.vehicle);
         var1.poly.init(this.poly);
         var1.polyPlusRadius.init(this.polyPlusRadius);
         var1.upVectorDot = this.upVectorDot;
         this.vehicle = null;
      }

      static PolygonalMap2.VehicleUpdateTask alloc() {
         synchronized(pool) {
            return pool.isEmpty() ? new PolygonalMap2.VehicleUpdateTask() : (PolygonalMap2.VehicleUpdateTask)pool.pop();
         }
      }

      public void release() {
         synchronized(pool) {
            assert !pool.contains(this);

            pool.push(this);
         }
      }
   }

   private static final class VehicleRemoveTask implements PolygonalMap2.IVehicleTask {
      PolygonalMap2 map;
      BaseVehicle vehicle;
      static final ArrayDeque pool = new ArrayDeque();

      public void init(PolygonalMap2 var1, BaseVehicle var2) {
         this.map = var1;
         this.vehicle = var2;
      }

      public void execute() {
         PolygonalMap2.Vehicle var1 = (PolygonalMap2.Vehicle)this.map.vehicleMap.remove(this.vehicle);
         if (var1 != null) {
            this.map.vehicles.remove(var1);
            var1.release();
         }

         this.vehicle = null;
      }

      static PolygonalMap2.VehicleRemoveTask alloc() {
         synchronized(pool) {
            return pool.isEmpty() ? new PolygonalMap2.VehicleRemoveTask() : (PolygonalMap2.VehicleRemoveTask)pool.pop();
         }
      }

      public void release() {
         synchronized(pool) {
            assert !pool.contains(this);

            pool.push(this);
         }
      }
   }

   private static final class Cell {
      PolygonalMap2 map;
      public short cx;
      public short cy;
      public PolygonalMap2.Chunk[][] chunks;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.Cell init(PolygonalMap2 var1, int var2, int var3) {
         this.map = var1;
         this.cx = (short)var2;
         this.cy = (short)var3;
         return this;
      }

      PolygonalMap2.Chunk getChunkFromChunkPos(int var1, int var2) {
         if (this.chunks == null) {
            return null;
         } else {
            var1 -= this.cx * 30;
            var2 -= this.cy * 30;
            return var1 >= 0 && var1 < 30 && var2 >= 0 && var2 < 30 ? this.chunks[var1][var2] : null;
         }
      }

      PolygonalMap2.Chunk allocChunkIfNeeded(int var1, int var2) {
         var1 -= this.cx * 30;
         var2 -= this.cy * 30;
         if (var1 >= 0 && var1 < 30 && var2 >= 0 && var2 < 30) {
            if (this.chunks == null) {
               this.chunks = new PolygonalMap2.Chunk[30][30];
            }

            if (this.chunks[var1][var2] == null) {
               this.chunks[var1][var2] = PolygonalMap2.Chunk.alloc();
            }

            this.chunks[var1][var2].init(this.cx * 30 + var1, this.cy * 30 + var2);
            return this.chunks[var1][var2];
         } else {
            return null;
         }
      }

      void removeChunk(int var1, int var2) {
         if (this.chunks != null) {
            var1 -= this.cx * 30;
            var2 -= this.cy * 30;
            if (var1 >= 0 && var1 < 30 && var2 >= 0 && var2 < 30) {
               PolygonalMap2.Chunk var3 = this.chunks[var1][var2];
               if (var3 != null) {
                  var3.release();
                  this.chunks[var1][var2] = null;
               }

            }
         }
      }

      static PolygonalMap2.Cell alloc() {
         return pool.isEmpty() ? new PolygonalMap2.Cell() : (PolygonalMap2.Cell)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private interface IChunkTask {
      void execute();

      void release();
   }

   private interface IVehicleTask {
      void init(PolygonalMap2 var1, BaseVehicle var2);

      void execute();

      void release();
   }

   private static final class PathRequestTask {
      PolygonalMap2 map;
      PolygonalMap2.PathFindRequest request;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.PathRequestTask init(PolygonalMap2 var1, PolygonalMap2.PathFindRequest var2) {
         this.map = var1;
         this.request = var2;
         return this;
      }

      void execute() {
         if (this.request.mover instanceof IsoPlayer) {
            this.map.requests.addFirst(this.request);
         } else {
            this.map.requests.add(this.request);
         }

      }

      static PolygonalMap2.PathRequestTask alloc() {
         synchronized(pool) {
            return pool.isEmpty() ? new PolygonalMap2.PathRequestTask() : (PolygonalMap2.PathRequestTask)pool.pop();
         }
      }

      public void release() {
         synchronized(pool) {
            assert !pool.contains(this);

            pool.push(this);
         }
      }
   }

   private static final class ChunkDataZ {
      public PolygonalMap2.Chunk chunk;
      public final ArrayList obstacles = new ArrayList();
      public final ArrayList nodes = new ArrayList();
      public int z;
      static short EPOCH = 0;
      short epoch;
      public static final ObjectPool pool = new ObjectPool(PolygonalMap2.ChunkDataZ::new);

      public void init(PolygonalMap2.Chunk var1, int var2) {
         this.chunk = var1;
         this.z = var2;
         this.epoch = EPOCH;
         if (PolygonalMap2.instance.clipperThread == null) {
            PolygonalMap2.instance.clipperThread = new Clipper();
         }

         Clipper var3 = PolygonalMap2.instance.clipperThread;
         var3.clear();
         int var4 = var1.wx * 10;
         int var5 = var1.wy * 10;

         int var7;
         for(int var6 = var5 - 2; var6 < var5 + 10 + 2; ++var6) {
            for(var7 = var4 - 2; var7 < var4 + 10 + 2; ++var7) {
               PolygonalMap2.Square var8 = PolygonalMap2.instance.getSquare(var7, var6, var2);
               if (var8 != null && var8.has(512)) {
                  if (var8.isReallySolid() || var8.has(128) || var8.has(64) || var8.has(16) || var8.has(8)) {
                     var3.addAABBBevel((float)var7 - 0.3F, (float)var6 - 0.3F, (float)var7 + 1.0F + 0.3F, (float)var6 + 1.0F + 0.3F, 0.19800001F);
                  }

                  if (var8.has(2) || var8.has(256)) {
                     var3.addAABBBevel((float)var7 - 0.3F, (float)var6 - 0.3F, (float)var7 + 0.3F, (float)var6 + 1.0F + 0.3F, 0.19800001F);
                  }

                  if (var8.has(4) || var8.has(32)) {
                     var3.addAABBBevel((float)var7 - 0.3F, (float)var6 - 0.3F, (float)var7 + 1.0F + 0.3F, (float)var6 + 0.3F, 0.19800001F);
                  }

                  PolygonalMap2.Square var9;
                  if (var8.has(256)) {
                     var9 = PolygonalMap2.instance.getSquare(var7 + 1, var6, var2);
                     if (var9 != null) {
                        var3.addAABBBevel((float)(var7 + 1) - 0.3F, (float)var6 - 0.3F, (float)(var7 + 1) + 0.3F, (float)var6 + 1.0F + 0.3F, 0.19800001F);
                     }
                  }

                  if (var8.has(32)) {
                     var9 = PolygonalMap2.instance.getSquare(var7, var6 + 1, var2);
                     if (var9 != null) {
                        var3.addAABBBevel((float)var7 - 0.3F, (float)(var6 + 1) - 0.3F, (float)var7 + 1.0F + 0.3F, (float)(var6 + 1) + 0.3F, 0.19800001F);
                     }
                  }
               } else {
                  var3.addAABB((float)var7, (float)var6, (float)var7 + 1.0F, (float)var6 + 1.0F);
               }
            }
         }

         ByteBuffer var21 = PolygonalMap2.instance.xyBufferThread;
         var7 = var3.generatePolygons();

         int var11;
         int var22;
         for(var22 = 0; var22 < var7; ++var22) {
            var21.clear();
            var3.getPolygon(var22, var21);
            PolygonalMap2.Obstacle var23 = PolygonalMap2.Obstacle.alloc().init((IsoGridSquare)null);
            this.getEdgesFromBuffer(var21, var23, true);
            short var10 = var21.getShort();

            for(var11 = 0; var11 < var10; ++var11) {
               this.getEdgesFromBuffer(var21, var23, false);
            }

            var23.calcBounds();
            this.obstacles.add(var23);
         }

         var22 = var1.wx * 10;
         int var24 = var1.wy * 10;
         int var25 = var22 + 10;
         var11 = var24 + 10;
         var22 -= 2;
         var24 -= 2;
         var25 += 2;
         var11 += 2;
         PolygonalMap2.ImmutableRectF var12 = PolygonalMap2.ImmutableRectF.alloc();
         var12.init((float)var22, (float)var24, (float)(var25 - var22), (float)(var11 - var24));
         PolygonalMap2.ImmutableRectF var13 = PolygonalMap2.ImmutableRectF.alloc();

         for(int var14 = 0; var14 < PolygonalMap2.instance.vehicles.size(); ++var14) {
            PolygonalMap2.Vehicle var15 = (PolygonalMap2.Vehicle)PolygonalMap2.instance.vehicles.get(var14);
            PolygonalMap2.VehiclePoly var16 = var15.polyPlusRadius;
            float var17 = Math.min(var16.x1, Math.min(var16.x2, Math.min(var16.x3, var16.x4)));
            float var18 = Math.min(var16.y1, Math.min(var16.y2, Math.min(var16.y3, var16.y4)));
            float var19 = Math.max(var16.x1, Math.max(var16.x2, Math.max(var16.x3, var16.x4)));
            float var20 = Math.max(var16.y1, Math.max(var16.y2, Math.max(var16.y3, var16.y4)));
            var13.init(var17, var18, var19 - var17, var20 - var18);
            if (var12.intersects(var13)) {
               this.addEdgesForVehicle(var15);
            }
         }

         var12.release();
         var13.release();
      }

      private void getEdgesFromBuffer(ByteBuffer var1, PolygonalMap2.Obstacle var2, boolean var3) {
         short var4 = var1.getShort();
         if (var4 < 3) {
            var1.position(var1.position() + var4 * 4 * 2);
         } else {
            PolygonalMap2.EdgeRing var5 = var2.outer;
            if (!var3) {
               var5 = PolygonalMap2.EdgeRing.alloc();
               var5.clear();
               var2.inner.add(var5);
            }

            int var6 = this.nodes.size();

            int var7;
            for(var7 = 0; var7 < var4; ++var7) {
               float var8 = var1.getFloat();
               float var9 = var1.getFloat();
               PolygonalMap2.Node var10 = PolygonalMap2.Node.alloc().init(var8, var9, this.z);
               var10.flags |= 4;
               this.nodes.add(var6, var10);
            }

            PolygonalMap2.Node var12;
            for(var7 = var6; var7 < this.nodes.size() - 1; ++var7) {
               var12 = (PolygonalMap2.Node)this.nodes.get(var7);
               PolygonalMap2.Node var13 = (PolygonalMap2.Node)this.nodes.get(var7 + 1);
               PolygonalMap2.Edge var15 = PolygonalMap2.Edge.alloc().init(var12, var13, var2, var5);
               var5.add(var15);
            }

            PolygonalMap2.Node var11 = (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1);
            var12 = (PolygonalMap2.Node)this.nodes.get(var6);
            PolygonalMap2.Edge var14 = PolygonalMap2.Edge.alloc().init(var11, var12, var2, var5);
            var5.add(var14);
         }
      }

      private void addEdgesForVehicle(PolygonalMap2.Vehicle var1) {
         PolygonalMap2.VehiclePoly var2 = var1.polyPlusRadius;
         int var3 = (int)var2.z;
         PolygonalMap2.Node var4 = PolygonalMap2.Node.alloc().init(var2.x1, var2.y1, var3);
         PolygonalMap2.Node var5 = PolygonalMap2.Node.alloc().init(var2.x2, var2.y2, var3);
         PolygonalMap2.Node var6 = PolygonalMap2.Node.alloc().init(var2.x3, var2.y3, var3);
         PolygonalMap2.Node var7 = PolygonalMap2.Node.alloc().init(var2.x4, var2.y4, var3);
         var4.flags |= 4;
         var5.flags |= 4;
         var6.flags |= 4;
         var7.flags |= 4;
         PolygonalMap2.Obstacle var8 = PolygonalMap2.Obstacle.alloc().init(var1);
         this.obstacles.add(var8);
         PolygonalMap2.Edge var9 = PolygonalMap2.Edge.alloc().init(var4, var5, var8, var8.outer);
         PolygonalMap2.Edge var10 = PolygonalMap2.Edge.alloc().init(var5, var6, var8, var8.outer);
         PolygonalMap2.Edge var11 = PolygonalMap2.Edge.alloc().init(var6, var7, var8, var8.outer);
         PolygonalMap2.Edge var12 = PolygonalMap2.Edge.alloc().init(var7, var4, var8, var8.outer);
         var8.outer.add(var9);
         var8.outer.add(var10);
         var8.outer.add(var11);
         var8.outer.add(var12);
         var8.calcBounds();
         this.nodes.add(var4);
         this.nodes.add(var5);
         this.nodes.add(var6);
         this.nodes.add(var7);
      }

      public void clear() {
         PolygonalMap2.Node.releaseAll(this.nodes);
         this.nodes.clear();
         PolygonalMap2.Obstacle.releaseAll(this.obstacles);
         this.obstacles.clear();
      }
   }

   private static final class PointPool {
      final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.Point alloc() {
         return this.pool.isEmpty() ? new PolygonalMap2.Point() : (PolygonalMap2.Point)this.pool.pop();
      }

      void release(PolygonalMap2.Point var1) {
         this.pool.push(var1);
      }
   }

   public static final class Point {
      public int x;
      public int y;

      PolygonalMap2.Point init(int var1, int var2) {
         this.x = var1;
         this.y = var2;
         return this;
      }

      public boolean equals(Object var1) {
         return var1 instanceof PolygonalMap2.Point && ((PolygonalMap2.Point)var1).x == this.x && ((PolygonalMap2.Point)var1).y == this.y;
      }
   }

   private static final class ConnectedRegions {
      PolygonalMap2 map;
      HashSet doneChunks = new HashSet();
      int minX;
      int minY;
      int maxX;
      int maxY;
      int MINX;
      int MINY;
      int WIDTH;
      int HEIGHT;
      BooleanGrid visited;
      int[] stack;
      int stackLen;
      int[] choices;
      int choicesLen;

      private ConnectedRegions() {
         this.visited = new BooleanGrid(this.WIDTH, this.WIDTH);
      }

      void findAdjacentChunks(int var1, int var2) {
         this.doneChunks.clear();
         this.minX = this.minY = Integer.MAX_VALUE;
         this.maxX = this.maxY = Integer.MIN_VALUE;
         PolygonalMap2.Chunk var3 = this.map.getChunkFromSquarePos(var1, var2);
         this.findAdjacentChunks(var3);
      }

      void findAdjacentChunks(PolygonalMap2.Chunk var1) {
         if (var1 != null && !this.doneChunks.contains(var1)) {
            this.minX = Math.min(this.minX, var1.wx);
            this.minY = Math.min(this.minY, var1.wy);
            this.maxX = Math.max(this.maxX, var1.wx);
            this.maxY = Math.max(this.maxY, var1.wy);
            this.doneChunks.add(var1);
            PolygonalMap2.Chunk var2 = this.map.getChunkFromChunkPos(var1.wx - 1, var1.wy);
            PolygonalMap2.Chunk var3 = this.map.getChunkFromChunkPos(var1.wx, var1.wy - 1);
            PolygonalMap2.Chunk var4 = this.map.getChunkFromChunkPos(var1.wx + 1, var1.wy);
            PolygonalMap2.Chunk var5 = this.map.getChunkFromChunkPos(var1.wx, var1.wy + 1);
            this.findAdjacentChunks(var2);
            this.findAdjacentChunks(var3);
            this.findAdjacentChunks(var4);
            this.findAdjacentChunks(var5);
         }
      }

      void floodFill(int var1, int var2) {
         this.findAdjacentChunks(var1, var2);
         this.MINX = this.minX * 10;
         this.MINY = this.minY * 10;
         this.WIDTH = (this.maxX - this.minX + 1) * 10;
         this.HEIGHT = (this.maxY - this.minY + 1) * 10;
         this.visited = new BooleanGrid(this.WIDTH, this.WIDTH);
         this.stack = new int[this.WIDTH * this.WIDTH];
         this.choices = new int[this.WIDTH * this.HEIGHT];
         this.stackLen = 0;
         this.choicesLen = 0;
         if (this.push(var1, var2)) {
            int var3;
            label81:
            while((var3 = this.pop()) != -1) {
               int var4 = this.MINX + (var3 & '\uffff');

               int var5;
               for(var5 = this.MINY + (var3 >> 16) & '\uffff'; this.shouldVisit(var4, var5, var4, var5 - 1); --var5) {
               }

               boolean var6 = false;
               boolean var7 = false;

               while(this.visit(var4, var5)) {
                  if (!var6 && this.shouldVisit(var4, var5, var4 - 1, var5)) {
                     if (!this.push(var4 - 1, var5)) {
                        return;
                     }

                     var6 = true;
                  } else if (var6 && !this.shouldVisit(var4, var5, var4 - 1, var5)) {
                     var6 = false;
                  } else if (var6 && !this.shouldVisit(var4 - 1, var5, var4 - 1, var5 - 1) && !this.push(var4 - 1, var5)) {
                     return;
                  }

                  if (!var7 && this.shouldVisit(var4, var5, var4 + 1, var5)) {
                     if (!this.push(var4 + 1, var5)) {
                        return;
                     }

                     var7 = true;
                  } else if (var7 && !this.shouldVisit(var4, var5, var4 + 1, var5)) {
                     var7 = false;
                  } else if (var7 && !this.shouldVisit(var4 + 1, var5, var4 + 1, var5 - 1) && !this.push(var4 + 1, var5)) {
                     return;
                  }

                  ++var5;
                  if (!this.shouldVisit(var4, var5 - 1, var4, var5)) {
                     continue label81;
                  }
               }

               return;
            }

            System.out.println("#choices=" + this.choicesLen);
         }
      }

      boolean shouldVisit(int var1, int var2, int var3, int var4) {
         if (var3 < this.MINX + this.WIDTH && var3 >= this.MINX) {
            if (var4 < this.MINY + this.WIDTH && var4 >= this.MINY) {
               if (this.visited.getValue(this.gridX(var3), this.gridY(var4))) {
                  return false;
               } else {
                  PolygonalMap2.Square var5 = PolygonalMap2.instance.getSquare(var1, var2, 0);
                  PolygonalMap2.Square var6 = PolygonalMap2.instance.getSquare(var3, var4, 0);
                  if (var5 != null && var6 != null) {
                     return !this.isBlocked(var5, var6, false);
                  } else {
                     return false;
                  }
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }

      boolean visit(int var1, int var2) {
         if (this.choicesLen >= this.WIDTH * this.WIDTH) {
            return false;
         } else {
            this.choices[this.choicesLen++] = this.gridY(var2) << 16 | (short)this.gridX(var1);
            this.visited.setValue(this.gridX(var1), this.gridY(var2), true);
            return true;
         }
      }

      boolean push(int var1, int var2) {
         if (this.stackLen >= this.WIDTH * this.WIDTH) {
            return false;
         } else {
            this.stack[this.stackLen++] = this.gridY(var2) << 16 | (short)this.gridX(var1);
            return true;
         }
      }

      int pop() {
         return this.stackLen == 0 ? -1 : this.stack[--this.stackLen];
      }

      int gridX(int var1) {
         return var1 - this.MINX;
      }

      int gridY(int var1) {
         return var1 - this.MINY;
      }

      boolean isBlocked(PolygonalMap2.Square var1, PolygonalMap2.Square var2, boolean var3) {
         assert Math.abs(var1.x - var2.x) <= 1;

         assert Math.abs(var1.y - var2.y) <= 1;

         assert var1.z == var2.z;

         assert var1 != var2;

         boolean var4 = var2.x < var1.x;
         boolean var5 = var2.x > var1.x;
         boolean var6 = var2.y < var1.y;
         boolean var7 = var2.y > var1.y;
         if (var2.isReallySolid()) {
            return true;
         } else if (var2.y < var1.y && var1.has(64)) {
            return true;
         } else if (var2.x < var1.x && var1.has(8)) {
            return true;
         } else if (var2.y > var1.y && var2.x == var1.x && var2.has(64)) {
            return true;
         } else if (var2.x > var1.x && var2.y == var1.y && var2.has(8)) {
            return true;
         } else if (var2.x != var1.x && var2.has(448)) {
            return true;
         } else if (var2.y != var1.y && var2.has(56)) {
            return true;
         } else if (var2.x != var1.x && var1.has(448)) {
            return true;
         } else if (var2.y != var1.y && var1.has(56)) {
            return true;
         } else if (!var2.has(512) && !var2.has(504)) {
            return true;
         } else {
            boolean var8 = var6 && var1.has(4) && (var1.x != var2.x || var3 || !var1.has(16384));
            boolean var9 = var4 && var1.has(2) && (var1.y != var2.y || var3 || !var1.has(8192));
            boolean var10 = var7 && var2.has(4) && (var1.x != var2.x || var3 || !var2.has(16384));
            boolean var11 = var5 && var2.has(2) && (var1.y != var2.y || var3 || !var2.has(8192));
            if (!var8 && !var9 && !var10 && !var11) {
               boolean var12 = var2.x != var1.x && var2.y != var1.y;
               if (var12) {
                  PolygonalMap2.Square var13 = PolygonalMap2.instance.getSquare(var1.x, var2.y, var1.z);
                  PolygonalMap2.Square var14 = PolygonalMap2.instance.getSquare(var2.x, var1.y, var1.z);

                  assert var13 != var1 && var13 != var2;

                  assert var14 != var1 && var14 != var2;

                  if (var2.x == var1.x + 1 && var2.y == var1.y + 1 && var13 != null && var14 != null && var13.has(4096) && var14.has(2048)) {
                     return true;
                  } else if (var2.x == var1.x - 1 && var2.y == var1.y - 1 && var13 != null && var14 != null && var13.has(2048) && var14.has(4096)) {
                     return true;
                  } else if (var13 != null && this.isBlocked(var1, var13, true)) {
                     return true;
                  } else if (var14 != null && this.isBlocked(var1, var14, true)) {
                     return true;
                  } else if (var13 != null && this.isBlocked(var2, var13, true)) {
                     return true;
                  } else if (var14 != null && this.isBlocked(var2, var14, true)) {
                     return true;
                  } else {
                     return false;
                  }
               } else {
                  return false;
               }
            } else {
               return true;
            }
         }
      }
   }

   public static final class LiangBarsky {
      private final double[] p = new double[4];
      private final double[] q = new double[4];

      public boolean lineRectIntersect(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
         return this.lineRectIntersect(var1, var2, var3, var4, var5, var6, var7, var8, (double[])null);
      }

      public boolean lineRectIntersect(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, double[] var9) {
         this.p[0] = (double)(-var3);
         this.p[1] = (double)var3;
         this.p[2] = (double)(-var4);
         this.p[3] = (double)var4;
         this.q[0] = (double)(var1 - var5);
         this.q[1] = (double)(var7 - var1);
         this.q[2] = (double)(var2 - var6);
         this.q[3] = (double)(var8 - var2);
         double var10 = 0.0D;
         double var12 = 1.0D;

         for(int var14 = 0; var14 < 4; ++var14) {
            if (this.p[var14] == 0.0D) {
               if (this.q[var14] < 0.0D) {
                  return false;
               }
            } else {
               double var15 = this.q[var14] / this.p[var14];
               if (this.p[var14] < 0.0D && var10 < var15) {
                  var10 = var15;
               } else if (this.p[var14] > 0.0D && var12 > var15) {
                  var12 = var15;
               }
            }
         }

         if (var10 >= var12) {
            return false;
         } else {
            if (var9 != null) {
               var9[0] = var10;
               var9[1] = var12;
            }

            return true;
         }
      }
   }

   private static final class ChunkData {
      final PolygonalMap2.ChunkDataZ[] data = new PolygonalMap2.ChunkDataZ[8];

      public PolygonalMap2.ChunkDataZ init(PolygonalMap2.Chunk var1, int var2) {
         if (this.data[var2] == null) {
            this.data[var2] = (PolygonalMap2.ChunkDataZ)PolygonalMap2.ChunkDataZ.pool.alloc();
            this.data[var2].init(var1, var2);
         } else if (this.data[var2].epoch != PolygonalMap2.ChunkDataZ.EPOCH) {
            this.data[var2].clear();
            this.data[var2].init(var1, var2);
         }

         return this.data[var2];
      }

      public void clear() {
         PZArrayUtil.forEach((Object[])this.data, (var0) -> {
            if (var0 != null) {
               var0.clear();
               PolygonalMap2.ChunkDataZ.pool.release((Object)var0);
            }

         });
         Arrays.fill(this.data, (Object)null);
      }
   }

   private static final class GoalNode implements IGoalNode {
      PolygonalMap2.SearchNode searchNode;

      PolygonalMap2.GoalNode init(PolygonalMap2.SearchNode var1) {
         this.searchNode = var1;
         return this;
      }

      public boolean inGoal(ISearchNode var1) {
         if (this.searchNode.tx != -1) {
            PolygonalMap2.SearchNode var2 = (PolygonalMap2.SearchNode)var1;
            int var3 = (int)var2.getX();
            int var4 = (int)var2.getY();
            if (var3 % 10 == 0 && PolygonalMap2.instance.getChunkFromSquarePos(var3 - 1, var4) == null) {
               return true;
            } else if (var3 % 10 == 9 && PolygonalMap2.instance.getChunkFromSquarePos(var3 + 1, var4) == null) {
               return true;
            } else if (var4 % 10 == 0 && PolygonalMap2.instance.getChunkFromSquarePos(var3, var4 - 1) == null) {
               return true;
            } else {
               return var4 % 10 == 9 && PolygonalMap2.instance.getChunkFromSquarePos(var3, var4 + 1) == null;
            }
         } else {
            return var1 == this.searchNode;
         }
      }
   }

   private static final class ImmutableRectF {
      private float x;
      private float y;
      private float w;
      private float h;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.ImmutableRectF init(float var1, float var2, float var3, float var4) {
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

      boolean intersects(PolygonalMap2.ImmutableRectF var1) {
         return this.left() < var1.right() && this.right() > var1.left() && this.top() < var1.bottom() && this.bottom() > var1.top();
      }

      static PolygonalMap2.ImmutableRectF alloc() {
         return pool.isEmpty() ? new PolygonalMap2.ImmutableRectF() : (PolygonalMap2.ImmutableRectF)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class Intersection {
      PolygonalMap2.Edge edge1;
      PolygonalMap2.Edge edge2;
      float dist1;
      float dist2;
      PolygonalMap2.Node nodeSplit;

      Intersection(PolygonalMap2.Edge var1, PolygonalMap2.Edge var2, float var3, float var4, float var5, float var6) {
         this.edge1 = var1;
         this.edge2 = var2;
         this.dist1 = var3;
         this.dist2 = var4;
         this.nodeSplit = PolygonalMap2.Node.alloc().init(var5, var6, var1.node1.z);
      }

      Intersection(PolygonalMap2.Edge var1, PolygonalMap2.Edge var2, float var3, float var4, PolygonalMap2.Node var5) {
         this.edge1 = var1;
         this.edge2 = var2;
         this.dist1 = var3;
         this.dist2 = var4;
         this.nodeSplit = var5;
      }

      PolygonalMap2.Edge split(PolygonalMap2.Edge var1) {
         return var1.split(this.nodeSplit);
      }
   }

   private static final class ClusterOutlineGrid {
      PolygonalMap2.ClusterOutline[] elements;
      int W;
      int H;

      PolygonalMap2.ClusterOutlineGrid setSize(int var1, int var2) {
         if (this.elements == null || this.elements.length < var1 * var2) {
            this.elements = new PolygonalMap2.ClusterOutline[var1 * var2];
         }

         this.W = var1;
         this.H = var2;
         return this;
      }

      void releaseElements() {
         for(int var1 = 0; var1 < this.H; ++var1) {
            for(int var2 = 0; var2 < this.W; ++var2) {
               if (this.elements[var2 + var1 * this.W] != null) {
                  this.elements[var2 + var1 * this.W].release();
                  this.elements[var2 + var1 * this.W] = null;
               }
            }
         }

      }

      void setInner(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         if (var4 != null) {
            var4.inner = true;
         }

      }

      void setWest(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         if (var4 != null) {
            var4.w = true;
         }

      }

      void setNorth(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         if (var4 != null) {
            var4.n = true;
         }

      }

      void setEast(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         if (var4 != null) {
            var4.e = true;
         }

      }

      void setSouth(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         if (var4 != null) {
            var4.s = true;
         }

      }

      boolean canTrace_W(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         return var4 != null && var4.inner && var4.w && !var4.tw;
      }

      boolean canTrace_N(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         return var4 != null && var4.inner && var4.n && !var4.tn;
      }

      boolean canTrace_E(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         return var4 != null && var4.inner && var4.e && !var4.te;
      }

      boolean canTrace_S(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         return var4 != null && var4.inner && var4.s && !var4.ts;
      }

      boolean isInner(int var1, int var2, int var3) {
         PolygonalMap2.ClusterOutline var4 = this.get(var1, var2, var3);
         return var4 != null && (var4.start || var4.inner);
      }

      PolygonalMap2.ClusterOutline get(int var1, int var2, int var3) {
         if (var1 >= 0 && var1 < this.W) {
            if (var2 >= 0 && var2 < this.H) {
               if (this.elements[var1 + var2 * this.W] == null) {
                  this.elements[var1 + var2 * this.W] = PolygonalMap2.ClusterOutline.alloc().init(var1, var2, var3);
               }

               return this.elements[var1 + var2 * this.W];
            } else {
               return null;
            }
         } else {
            return null;
         }
      }

      void trace_W(PolygonalMap2.ClusterOutline var1, ArrayList var2, PolygonalMap2.Node var3) {
         int var4 = var1.x;
         int var5 = var1.y;
         int var6 = var1.z;
         if (var3 != null) {
            var3.setXY((float)var4, (float)var5);
         } else {
            PolygonalMap2.Node var7 = PolygonalMap2.Node.alloc().init((float)var4, (float)var5, var6);
            var2.add(var7);
         }

         var1.tw = true;
         if (this.canTrace_S(var4 - 1, var5 - 1, var6)) {
            this.get(var4, var5 - 1, var6).innerCorner = true;
            this.trace_S(this.get(var4 - 1, var5 - 1, var6), var2, (PolygonalMap2.Node)null);
         } else if (this.canTrace_W(var4, var5 - 1, var6)) {
            this.trace_W(this.get(var4, var5 - 1, var6), var2, (PolygonalMap2.Node)var2.get(var2.size() - 1));
         } else if (this.canTrace_N(var4, var5, var6)) {
            this.trace_N(var1, var2, (PolygonalMap2.Node)null);
         }

      }

      void trace_N(PolygonalMap2.ClusterOutline var1, ArrayList var2, PolygonalMap2.Node var3) {
         int var4 = var1.x;
         int var5 = var1.y;
         int var6 = var1.z;
         if (var3 != null) {
            var3.setXY((float)(var4 + 1), (float)var5);
         } else {
            PolygonalMap2.Node var7 = PolygonalMap2.Node.alloc().init((float)(var4 + 1), (float)var5, var6);
            var2.add(var7);
         }

         var1.tn = true;
         if (this.canTrace_W(var4 + 1, var5 - 1, var6)) {
            this.get(var4 + 1, var5, var6).innerCorner = true;
            this.trace_W(this.get(var4 + 1, var5 - 1, var6), var2, (PolygonalMap2.Node)null);
         } else if (this.canTrace_N(var4 + 1, var5, var6)) {
            this.trace_N(this.get(var4 + 1, var5, var6), var2, (PolygonalMap2.Node)var2.get(var2.size() - 1));
         } else if (this.canTrace_E(var4, var5, var6)) {
            this.trace_E(var1, var2, (PolygonalMap2.Node)null);
         }

      }

      void trace_E(PolygonalMap2.ClusterOutline var1, ArrayList var2, PolygonalMap2.Node var3) {
         int var4 = var1.x;
         int var5 = var1.y;
         int var6 = var1.z;
         if (var3 != null) {
            var3.setXY((float)(var4 + 1), (float)(var5 + 1));
         } else {
            PolygonalMap2.Node var7 = PolygonalMap2.Node.alloc().init((float)(var4 + 1), (float)(var5 + 1), var6);
            var2.add(var7);
         }

         var1.te = true;
         if (this.canTrace_N(var4 + 1, var5 + 1, var6)) {
            this.get(var4, var5 + 1, var6).innerCorner = true;
            this.trace_N(this.get(var4 + 1, var5 + 1, var6), var2, (PolygonalMap2.Node)null);
         } else if (this.canTrace_E(var4, var5 + 1, var6)) {
            this.trace_E(this.get(var4, var5 + 1, var6), var2, (PolygonalMap2.Node)var2.get(var2.size() - 1));
         } else if (this.canTrace_S(var4, var5, var6)) {
            this.trace_S(var1, var2, (PolygonalMap2.Node)null);
         }

      }

      void trace_S(PolygonalMap2.ClusterOutline var1, ArrayList var2, PolygonalMap2.Node var3) {
         int var4 = var1.x;
         int var5 = var1.y;
         int var6 = var1.z;
         if (var3 != null) {
            var3.setXY((float)var4, (float)(var5 + 1));
         } else {
            PolygonalMap2.Node var7 = PolygonalMap2.Node.alloc().init((float)var4, (float)(var5 + 1), var6);
            var2.add(var7);
         }

         var1.ts = true;
         if (this.canTrace_E(var4 - 1, var5 + 1, var6)) {
            this.get(var4 - 1, var5, var6).innerCorner = true;
            this.trace_E(this.get(var4 - 1, var5 + 1, var6), var2, (PolygonalMap2.Node)null);
         } else if (this.canTrace_S(var4 - 1, var5, var6)) {
            this.trace_S(this.get(var4 - 1, var5, var6), var2, (PolygonalMap2.Node)var2.get(var2.size() - 1));
         } else if (this.canTrace_W(var4, var5, var6)) {
            this.trace_W(var1, var2, (PolygonalMap2.Node)null);
         }

      }

      ArrayList trace(PolygonalMap2.ClusterOutline var1) {
         int var2 = var1.x;
         int var3 = var1.y;
         int var4 = var1.z;
         ArrayList var5 = new ArrayList();
         PolygonalMap2.Node var6 = PolygonalMap2.Node.alloc().init((float)var2, (float)var3, var4);
         var5.add(var6);
         var1.start = true;
         this.trace_N(var1, var5, (PolygonalMap2.Node)null);
         PolygonalMap2.Node var7 = (PolygonalMap2.Node)var5.get(var5.size() - 1);
         float var8 = 0.1F;
         if ((int)(var6.x + var8) == (int)(var7.x + var8) && (int)(var6.y + var8) == (int)(var7.y + var8)) {
            var7.release();
            var5.set(var5.size() - 1, var6);
         }

         return var5;
      }
   }

   private static final class ClusterOutline {
      int x;
      int y;
      int z;
      boolean w;
      boolean n;
      boolean e;
      boolean s;
      boolean tw;
      boolean tn;
      boolean te;
      boolean ts;
      boolean inner;
      boolean innerCorner;
      boolean start;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.ClusterOutline init(int var1, int var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.w = this.n = this.e = this.s = false;
         this.tw = this.tn = this.te = this.ts = false;
         this.inner = this.innerCorner = this.start = false;
         return this;
      }

      static PolygonalMap2.ClusterOutline alloc() {
         return pool.isEmpty() ? new PolygonalMap2.ClusterOutline() : (PolygonalMap2.ClusterOutline)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   /** @deprecated */
   @Deprecated
   private static final class ObjectOutline {
      int x;
      int y;
      int z;
      boolean nw;
      boolean nw_w;
      boolean nw_n;
      boolean nw_e;
      boolean nw_s;
      boolean w_w;
      boolean w_e;
      boolean w_cutoff;
      boolean n_n;
      boolean n_s;
      boolean n_cutoff;
      ArrayList nodes;
      static final ArrayDeque pool = new ArrayDeque();

      PolygonalMap2.ObjectOutline init(int var1, int var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.nw = this.nw_w = this.nw_n = this.nw_e = false;
         this.w_w = this.w_e = this.w_cutoff = false;
         this.n_n = this.n_s = this.n_cutoff = false;
         return this;
      }

      static void setSolid(int var0, int var1, int var2, PolygonalMap2.ObjectOutline[][] var3) {
         setWest(var0, var1, var2, var3);
         setNorth(var0, var1, var2, var3);
         setWest(var0 + 1, var1, var2, var3);
         setNorth(var0, var1 + 1, var2, var3);
      }

      static void setWest(int var0, int var1, int var2, PolygonalMap2.ObjectOutline[][] var3) {
         PolygonalMap2.ObjectOutline var4 = get(var0, var1, var2, var3);
         if (var4 != null) {
            if (var4.nw) {
               var4.nw_s = false;
            } else {
               var4.nw = true;
               var4.nw_w = true;
               var4.nw_n = true;
               var4.nw_e = true;
               var4.nw_s = false;
            }

            var4.w_w = true;
            var4.w_e = true;
         }

         PolygonalMap2.ObjectOutline var5 = var4;
         var4 = get(var0, var1 + 1, var2, var3);
         if (var4 == null) {
            if (var5 != null) {
               var5.w_cutoff = true;
            }
         } else if (var4.nw) {
            var4.nw_n = false;
         } else {
            var4.nw = true;
            var4.nw_n = false;
            var4.nw_w = true;
            var4.nw_e = true;
            var4.nw_s = true;
         }

      }

      static void setNorth(int var0, int var1, int var2, PolygonalMap2.ObjectOutline[][] var3) {
         PolygonalMap2.ObjectOutline var4 = get(var0, var1, var2, var3);
         if (var4 != null) {
            if (var4.nw) {
               var4.nw_e = false;
            } else {
               var4.nw = true;
               var4.nw_w = true;
               var4.nw_n = true;
               var4.nw_e = false;
               var4.nw_s = true;
            }

            var4.n_n = true;
            var4.n_s = true;
         }

         PolygonalMap2.ObjectOutline var5 = var4;
         var4 = get(var0 + 1, var1, var2, var3);
         if (var4 == null) {
            if (var5 != null) {
               var5.n_cutoff = true;
            }
         } else if (var4.nw) {
            var4.nw_w = false;
         } else {
            var4.nw = true;
            var4.nw_n = true;
            var4.nw_w = false;
            var4.nw_e = true;
            var4.nw_s = true;
         }

      }

      static PolygonalMap2.ObjectOutline get(int var0, int var1, int var2, PolygonalMap2.ObjectOutline[][] var3) {
         if (var0 >= 0 && var0 < var3.length) {
            if (var1 >= 0 && var1 < var3[0].length) {
               if (var3[var0][var1] == null) {
                  var3[var0][var1] = alloc().init(var0, var1, var2);
               }

               return var3[var0][var1];
            } else {
               return null;
            }
         } else {
            return null;
         }
      }

      void trace_NW_N(PolygonalMap2.ObjectOutline[][] var1, PolygonalMap2.Node var2) {
         if (var2 != null) {
            var2.setXY((float)this.x + 0.3F, (float)this.y - 0.3F);
         } else {
            PolygonalMap2.Node var3 = PolygonalMap2.Node.alloc().init((float)this.x + 0.3F, (float)this.y - 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.nw_n = false;
         if (this.nw_e) {
            this.trace_NW_E(var1, (PolygonalMap2.Node)null);
         } else if (this.n_n) {
            this.trace_N_N(var1, (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1));
         }

      }

      void trace_NW_S(PolygonalMap2.ObjectOutline[][] var1, PolygonalMap2.Node var2) {
         if (var2 != null) {
            var2.setXY((float)this.x - 0.3F, (float)this.y + 0.3F);
         } else {
            PolygonalMap2.Node var3 = PolygonalMap2.Node.alloc().init((float)this.x - 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.nw_s = false;
         if (this.nw_w) {
            this.trace_NW_W(var1, (PolygonalMap2.Node)null);
         } else {
            PolygonalMap2.ObjectOutline var4 = get(this.x - 1, this.y, this.z, var1);
            if (var4 == null) {
               return;
            }

            if (var4.n_s) {
               var4.nodes = this.nodes;
               var4.trace_N_S(var1, (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1));
            }
         }

      }

      void trace_NW_W(PolygonalMap2.ObjectOutline[][] var1, PolygonalMap2.Node var2) {
         if (var2 != null) {
            var2.setXY((float)this.x - 0.3F, (float)this.y - 0.3F);
         } else {
            PolygonalMap2.Node var3 = PolygonalMap2.Node.alloc().init((float)this.x - 0.3F, (float)this.y - 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.nw_w = false;
         if (this.nw_n) {
            this.trace_NW_N(var1, (PolygonalMap2.Node)null);
         } else {
            PolygonalMap2.ObjectOutline var4 = get(this.x, this.y - 1, this.z, var1);
            if (var4 == null) {
               return;
            }

            if (var4.w_w) {
               var4.nodes = this.nodes;
               var4.trace_W_W(var1, (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1));
            }
         }

      }

      void trace_NW_E(PolygonalMap2.ObjectOutline[][] var1, PolygonalMap2.Node var2) {
         if (var2 != null) {
            var2.setXY((float)this.x + 0.3F, (float)this.y + 0.3F);
         } else {
            PolygonalMap2.Node var3 = PolygonalMap2.Node.alloc().init((float)this.x + 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.nw_e = false;
         if (this.nw_s) {
            this.trace_NW_S(var1, (PolygonalMap2.Node)null);
         } else if (this.w_e) {
            this.trace_W_E(var1, (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1));
         }

      }

      void trace_W_E(PolygonalMap2.ObjectOutline[][] var1, PolygonalMap2.Node var2) {
         PolygonalMap2.Node var3;
         if (var2 != null) {
            var2.setXY((float)this.x + 0.3F, (float)(this.y + 1) - 0.3F);
         } else {
            var3 = PolygonalMap2.Node.alloc().init((float)this.x + 0.3F, (float)(this.y + 1) - 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.w_e = false;
         if (this.w_cutoff) {
            var3 = (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1);
            var3.setXY((float)this.x + 0.3F, (float)(this.y + 1) + 0.3F);
            var3 = PolygonalMap2.Node.alloc().init((float)this.x - 0.3F, (float)(this.y + 1) + 0.3F, this.z);
            this.nodes.add(var3);
            var3 = PolygonalMap2.Node.alloc().init((float)this.x - 0.3F, (float)(this.y + 1) - 0.3F, this.z);
            this.nodes.add(var3);
            this.trace_W_W(var1, var3);
         } else {
            PolygonalMap2.ObjectOutline var4 = get(this.x, this.y + 1, this.z, var1);
            if (var4 != null) {
               if (var4.nw && var4.nw_e) {
                  var4.nodes = this.nodes;
                  var4.trace_NW_E(var1, (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1));
               } else if (var4.n_n) {
                  var4.nodes = this.nodes;
                  var4.trace_N_N(var1, (PolygonalMap2.Node)null);
               }

            }
         }
      }

      void trace_W_W(PolygonalMap2.ObjectOutline[][] var1, PolygonalMap2.Node var2) {
         if (var2 != null) {
            var2.setXY((float)this.x - 0.3F, (float)this.y + 0.3F);
         } else {
            PolygonalMap2.Node var3 = PolygonalMap2.Node.alloc().init((float)this.x - 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.w_w = false;
         if (this.nw_w) {
            this.trace_NW_W(var1, (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1));
         } else {
            PolygonalMap2.ObjectOutline var4 = get(this.x - 1, this.y, this.z, var1);
            if (var4 == null) {
               return;
            }

            if (var4.n_s) {
               var4.nodes = this.nodes;
               var4.trace_N_S(var1, (PolygonalMap2.Node)null);
            }
         }

      }

      void trace_N_N(PolygonalMap2.ObjectOutline[][] var1, PolygonalMap2.Node var2) {
         PolygonalMap2.Node var3;
         if (var2 != null) {
            var2.setXY((float)(this.x + 1) - 0.3F, (float)this.y - 0.3F);
         } else {
            var3 = PolygonalMap2.Node.alloc().init((float)(this.x + 1) - 0.3F, (float)this.y - 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.n_n = false;
         if (this.n_cutoff) {
            var3 = (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1);
            var3.setXY((float)(this.x + 1) + 0.3F, (float)this.y - 0.3F);
            var3 = PolygonalMap2.Node.alloc().init((float)(this.x + 1) + 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
            var3 = PolygonalMap2.Node.alloc().init((float)(this.x + 1) - 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
            this.trace_N_S(var1, var3);
         } else {
            PolygonalMap2.ObjectOutline var4 = get(this.x + 1, this.y, this.z, var1);
            if (var4 != null) {
               if (var4.nw_n) {
                  var4.nodes = this.nodes;
                  var4.trace_NW_N(var1, (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1));
               } else {
                  var4 = get(this.x + 1, this.y - 1, this.z, var1);
                  if (var4 == null) {
                     return;
                  }

                  if (var4.w_w) {
                     var4.nodes = this.nodes;
                     var4.trace_W_W(var1, (PolygonalMap2.Node)null);
                  }
               }

            }
         }
      }

      void trace_N_S(PolygonalMap2.ObjectOutline[][] var1, PolygonalMap2.Node var2) {
         if (var2 != null) {
            var2.setXY((float)this.x + 0.3F, (float)this.y + 0.3F);
         } else {
            PolygonalMap2.Node var3 = PolygonalMap2.Node.alloc().init((float)this.x + 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.n_s = false;
         if (this.nw_s) {
            this.trace_NW_S(var1, (PolygonalMap2.Node)this.nodes.get(this.nodes.size() - 1));
         } else if (this.w_e) {
            this.trace_W_E(var1, (PolygonalMap2.Node)null);
         }

      }

      void trace(PolygonalMap2.ObjectOutline[][] var1, ArrayList var2) {
         var2.clear();
         this.nodes = var2;
         PolygonalMap2.Node var3 = PolygonalMap2.Node.alloc().init((float)this.x - 0.3F, (float)this.y - 0.3F, this.z);
         var2.add(var3);
         this.trace_NW_N(var1, (PolygonalMap2.Node)null);
         if (var2.size() != 2 && var3.x == ((PolygonalMap2.Node)var2.get(var2.size() - 1)).x && var3.y == ((PolygonalMap2.Node)var2.get(var2.size() - 1)).y) {
            ((PolygonalMap2.Node)var2.get(var2.size() - 1)).release();
            var2.set(var2.size() - 1, var3);
         } else {
            var2.clear();
         }

      }

      static PolygonalMap2.ObjectOutline alloc() {
         return pool.isEmpty() ? new PolygonalMap2.ObjectOutline() : (PolygonalMap2.ObjectOutline)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static enum EdgeRingHit {
      OnEdge,
      Inside,
      Outside;

      // $FF: synthetic method
      private static PolygonalMap2.EdgeRingHit[] $values() {
         return new PolygonalMap2.EdgeRingHit[]{OnEdge, Inside, Outside};
      }
   }

   static final class L_lineSegmentIntersects {
      static final Vector2 v1 = new Vector2();
   }
}
