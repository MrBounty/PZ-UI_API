package zombie.vehicles;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.joml.Vector2f;
import zombie.characters.IsoGameCharacter;
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

public final class CollideWithObstacles {
   static final float RADIUS = 0.3F;
   private final ArrayList obstacles = new ArrayList();
   private final ArrayList nodes = new ArrayList();
   private final ArrayList intersections = new ArrayList();
   private final CollideWithObstacles.ImmutableRectF moveBounds = new CollideWithObstacles.ImmutableRectF();
   private final CollideWithObstacles.ImmutableRectF vehicleBounds = new CollideWithObstacles.ImmutableRectF();
   private final Vector2 move = new Vector2();
   private final Vector2 closest = new Vector2();
   private final Vector2 nodeNormal = new Vector2();
   private final Vector2 edgeVec = new Vector2();
   private final ArrayList vehicles = new ArrayList();
   CollideWithObstacles.CCObjectOutline[][] oo = new CollideWithObstacles.CCObjectOutline[5][5];
   ArrayList obstacleTraceNodes = new ArrayList();
   CollideWithObstacles.CompareIntersection comparator = new CollideWithObstacles.CompareIntersection();

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

   void getObstaclesInRect(float var1, float var2, float var3, float var4, int var5, int var6, int var7) {
      this.nodes.clear();
      this.obstacles.clear();
      this.moveBounds.init(var1 - 1.0F, var2 - 1.0F, var3 - var1 + 2.0F, var4 - var2 + 2.0F);
      this.getVehiclesInRect(var1 - 1.0F - 4.0F, var2 - 1.0F - 4.0F, var3 + 2.0F + 8.0F, var4 + 2.0F + 8.0F, var7);

      int var8;
      CollideWithObstacles.CCNode var16;
      CollideWithObstacles.CCNode var18;
      CollideWithObstacles.CCNode var19;
      for(var8 = 0; var8 < this.vehicles.size(); ++var8) {
         BaseVehicle var9 = (BaseVehicle)this.vehicles.get(var8);
         PolygonalMap2.VehiclePoly var10 = var9.getPolyPlusRadius();
         float var11 = Math.min(var10.x1, Math.min(var10.x2, Math.min(var10.x3, var10.x4)));
         float var12 = Math.min(var10.y1, Math.min(var10.y2, Math.min(var10.y3, var10.y4)));
         float var13 = Math.max(var10.x1, Math.max(var10.x2, Math.max(var10.x3, var10.x4)));
         float var14 = Math.max(var10.y1, Math.max(var10.y2, Math.max(var10.y3, var10.y4)));
         this.vehicleBounds.init(var11, var12, var13 - var11, var14 - var12);
         if (this.moveBounds.intersects(this.vehicleBounds)) {
            int var15 = (int)var10.z;
            var16 = CollideWithObstacles.CCNode.alloc().init(var10.x1, var10.y1, var15);
            CollideWithObstacles.CCNode var17 = CollideWithObstacles.CCNode.alloc().init(var10.x2, var10.y2, var15);
            var18 = CollideWithObstacles.CCNode.alloc().init(var10.x3, var10.y3, var15);
            var19 = CollideWithObstacles.CCNode.alloc().init(var10.x4, var10.y4, var15);
            CollideWithObstacles.CCObstacle var20 = CollideWithObstacles.CCObstacle.alloc().init();
            CollideWithObstacles.CCEdge var21 = CollideWithObstacles.CCEdge.alloc().init(var16, var17, var20);
            CollideWithObstacles.CCEdge var22 = CollideWithObstacles.CCEdge.alloc().init(var17, var18, var20);
            CollideWithObstacles.CCEdge var23 = CollideWithObstacles.CCEdge.alloc().init(var18, var19, var20);
            CollideWithObstacles.CCEdge var24 = CollideWithObstacles.CCEdge.alloc().init(var19, var16, var20);
            var20.edges.add(var21);
            var20.edges.add(var22);
            var20.edges.add(var23);
            var20.edges.add(var24);
            var20.calcBounds();
            this.obstacles.add(var20);
            this.nodes.add(var16);
            this.nodes.add(var17);
            this.nodes.add(var18);
            this.nodes.add(var19);
         }
      }

      if (!this.obstacles.isEmpty()) {
         var8 = var5 - 2;
         int var25 = var6 - 2;
         int var26 = var5 + 2 + 1;
         int var27 = var6 + 2 + 1;

         int var28;
         int var29;
         for(var28 = var25; var28 < var27; ++var28) {
            for(var29 = var8; var29 < var26; ++var29) {
               CollideWithObstacles.CCObjectOutline.get(var29 - var8, var28 - var25, var7, this.oo).init(var29 - var8, var28 - var25, var7);
            }
         }

         for(var28 = var25; var28 < var27 - 1; ++var28) {
            for(var29 = var8; var29 < var26 - 1; ++var29) {
               IsoGridSquare var30 = IsoWorld.instance.CurrentCell.getGridSquare(var29, var28, var7);
               if (var30 != null) {
                  if (var30.isSolid() || var30.isSolidTrans() && !var30.isAdjacentToWindow() || var30.Has(IsoObjectType.stairsMN) || var30.Has(IsoObjectType.stairsTN) || var30.Has(IsoObjectType.stairsMW) || var30.Has(IsoObjectType.stairsTW)) {
                     CollideWithObstacles.CCObjectOutline.setSolid(var29 - var8, var28 - var25, var7, this.oo);
                  }

                  boolean var32 = var30.Is(IsoFlagType.collideW);
                  if (var30.Is(IsoFlagType.windowW) || var30.Is(IsoFlagType.WindowW)) {
                     var32 = true;
                  }

                  if (var32 && var30.Is(IsoFlagType.doorW)) {
                     var32 = false;
                  }

                  boolean var33 = var30.Is(IsoFlagType.collideN);
                  if (var30.Is(IsoFlagType.windowN) || var30.Is(IsoFlagType.WindowN)) {
                     var33 = true;
                  }

                  if (var33 && var30.Is(IsoFlagType.doorN)) {
                     var33 = false;
                  }

                  if (var32 || var30.hasBlockedDoor(false) || var30.Has(IsoObjectType.stairsBN)) {
                     CollideWithObstacles.CCObjectOutline.setWest(var29 - var8, var28 - var25, var7, this.oo);
                  }

                  if (var33 || var30.hasBlockedDoor(true) || var30.Has(IsoObjectType.stairsBW)) {
                     CollideWithObstacles.CCObjectOutline.setNorth(var29 - var8, var28 - var25, var7, this.oo);
                  }

                  if (var30.Has(IsoObjectType.stairsBN) && var29 != var26 - 2) {
                     var30 = IsoWorld.instance.CurrentCell.getGridSquare(var29 + 1, var28, var7);
                     if (var30 != null) {
                        CollideWithObstacles.CCObjectOutline.setWest(var29 + 1 - var8, var28 - var25, var7, this.oo);
                     }
                  } else if (var30.Has(IsoObjectType.stairsBW) && var28 != var27 - 2) {
                     var30 = IsoWorld.instance.CurrentCell.getGridSquare(var29, var28 + 1, var7);
                     if (var30 != null) {
                        CollideWithObstacles.CCObjectOutline.setNorth(var29 - var8, var28 + 1 - var25, var7, this.oo);
                     }
                  }
               }
            }
         }

         for(var28 = 0; var28 < var27 - var25; ++var28) {
            for(var29 = 0; var29 < var26 - var8; ++var29) {
               CollideWithObstacles.CCObjectOutline var31 = CollideWithObstacles.CCObjectOutline.get(var29, var28, var7, this.oo);
               if (var31 != null && var31.nw && var31.nw_w && var31.nw_n) {
                  var31.trace(this.oo, this.obstacleTraceNodes);
                  if (!var31.nodes.isEmpty()) {
                     CollideWithObstacles.CCObstacle var34 = CollideWithObstacles.CCObstacle.alloc().init();
                     var16 = (CollideWithObstacles.CCNode)var31.nodes.get(var31.nodes.size() - 1);

                     for(int var35 = var31.nodes.size() - 1; var35 > 0; --var35) {
                        var18 = (CollideWithObstacles.CCNode)var31.nodes.get(var35);
                        var19 = (CollideWithObstacles.CCNode)var31.nodes.get(var35 - 1);
                        var18.x += (float)var8;
                        var18.y += (float)var25;
                        CollideWithObstacles.CCEdge var36 = CollideWithObstacles.CCEdge.alloc().init(var18, var19, var34);
                        float var37 = var19.x + (var19 != var16 ? (float)var8 : 0.0F);
                        float var38 = var19.y + (var19 != var16 ? (float)var25 : 0.0F);
                        var36.normal.set(var37 - var18.x, var38 - var18.y);
                        var36.normal.normalize();
                        var36.normal.rotate((float)Math.toRadians(90.0D));
                        var34.edges.add(var36);
                        this.nodes.add(var18);
                     }

                     var34.calcBounds();
                     this.obstacles.add(var34);
                  }
               }
            }
         }

      }
   }

   void checkEdgeIntersection() {
      boolean var1 = Core.bDebug && DebugOptions.instance.CollideWithObstaclesRenderObstacles.getValue();

      int var2;
      CollideWithObstacles.CCObstacle var3;
      int var4;
      int var6;
      for(var2 = 0; var2 < this.obstacles.size(); ++var2) {
         var3 = (CollideWithObstacles.CCObstacle)this.obstacles.get(var2);

         for(var4 = var2 + 1; var4 < this.obstacles.size(); ++var4) {
            CollideWithObstacles.CCObstacle var5 = (CollideWithObstacles.CCObstacle)this.obstacles.get(var4);
            if (var3.bounds.intersects(var5.bounds)) {
               for(var6 = 0; var6 < var3.edges.size(); ++var6) {
                  CollideWithObstacles.CCEdge var7 = (CollideWithObstacles.CCEdge)var3.edges.get(var6);

                  for(int var8 = 0; var8 < var5.edges.size(); ++var8) {
                     CollideWithObstacles.CCEdge var9 = (CollideWithObstacles.CCEdge)var5.edges.get(var8);
                     CollideWithObstacles.CCIntersection var10 = this.getIntersection(var7, var9);
                     if (var10 != null) {
                        var7.intersections.add(var10);
                        var9.intersections.add(var10);
                        if (var1) {
                           LineDrawer.addLine(var10.nodeSplit.x - 0.1F, var10.nodeSplit.y - 0.1F, (float)var7.node1.z, var10.nodeSplit.x + 0.1F, var10.nodeSplit.y + 0.1F, (float)var7.node1.z, 1.0F, 0.0F, 0.0F, (String)null, false);
                        }

                        if (!var7.hasNode(var10.nodeSplit) && !var9.hasNode(var10.nodeSplit)) {
                           this.nodes.add(var10.nodeSplit);
                        }

                        this.intersections.add(var10);
                     }
                  }
               }
            }
         }
      }

      for(var2 = 0; var2 < this.obstacles.size(); ++var2) {
         var3 = (CollideWithObstacles.CCObstacle)this.obstacles.get(var2);

         for(var4 = var3.edges.size() - 1; var4 >= 0; --var4) {
            CollideWithObstacles.CCEdge var11 = (CollideWithObstacles.CCEdge)var3.edges.get(var4);
            if (!var11.intersections.isEmpty()) {
               this.comparator.edge = var11;
               Collections.sort(var11.intersections, this.comparator);

               for(var6 = var11.intersections.size() - 1; var6 >= 0; --var6) {
                  CollideWithObstacles.CCIntersection var12 = (CollideWithObstacles.CCIntersection)var11.intersections.get(var6);
                  var12.split(var11);
               }
            }
         }
      }

   }

   boolean collinear(float var1, float var2, float var3, float var4, float var5, float var6) {
      float var7 = (var3 - var1) * (var6 - var2) - (var5 - var1) * (var4 - var2);
      return var7 >= -0.05F && var7 < 0.05F;
   }

   boolean within(float var1, float var2, float var3) {
      return var1 <= var2 && var2 <= var3 || var3 <= var2 && var2 <= var1;
   }

   boolean is_on(float var1, float var2, float var3, float var4, float var5, float var6) {
      boolean var10000;
      label25: {
         if (this.collinear(var1, var2, var3, var4, var5, var6)) {
            if (var1 != var3) {
               if (this.within(var1, var5, var3)) {
                  break label25;
               }
            } else if (this.within(var2, var6, var4)) {
               break label25;
            }
         }

         var10000 = false;
         return var10000;
      }

      var10000 = true;
      return var10000;
   }

   public CollideWithObstacles.CCIntersection getIntersection(CollideWithObstacles.CCEdge var1, CollideWithObstacles.CCEdge var2) {
      float var3 = var1.node1.x;
      float var4 = var1.node1.y;
      float var5 = var1.node2.x;
      float var6 = var1.node2.y;
      float var7 = var2.node1.x;
      float var8 = var2.node1.y;
      float var9 = var2.node2.x;
      float var10 = var2.node2.y;
      double var11 = (double)((var10 - var8) * (var5 - var3) - (var9 - var7) * (var6 - var4));
      if (var11 > -0.01D && var11 < 0.01D) {
         return null;
      } else {
         double var13 = (double)((var9 - var7) * (var4 - var8) - (var10 - var8) * (var3 - var7)) / var11;
         double var15 = (double)((var5 - var3) * (var4 - var8) - (var6 - var4) * (var3 - var7)) / var11;
         if (var13 >= 0.0D && var13 <= 1.0D && var15 >= 0.0D && var15 <= 1.0D) {
            float var17 = (float)((double)var3 + var13 * (double)(var5 - var3));
            float var18 = (float)((double)var4 + var13 * (double)(var6 - var4));
            CollideWithObstacles.CCNode var19 = null;
            CollideWithObstacles.CCNode var20 = null;
            if (var13 < 0.009999999776482582D) {
               var19 = var1.node1;
            } else if (var13 > 0.9900000095367432D) {
               var19 = var1.node2;
            }

            if (var15 < 0.009999999776482582D) {
               var20 = var2.node1;
            } else if (var15 > 0.9900000095367432D) {
               var20 = var2.node2;
            }

            if (var19 != null && var20 != null) {
               CollideWithObstacles.CCIntersection var21 = CollideWithObstacles.CCIntersection.alloc().init(var1, var2, (float)var13, (float)var15, var19);
               var1.intersections.add(var21);
               this.intersections.add(var21);
               var21 = CollideWithObstacles.CCIntersection.alloc().init(var1, var2, (float)var13, (float)var15, var20);
               var2.intersections.add(var21);
               this.intersections.add(var21);
               LineDrawer.addLine(var21.nodeSplit.x - 0.1F, var21.nodeSplit.y - 0.1F, (float)var1.node1.z, var21.nodeSplit.x + 0.1F, var21.nodeSplit.y + 0.1F, (float)var1.node1.z, 1.0F, 0.0F, 0.0F, (String)null, false);
               return null;
            } else {
               return var19 == null && var20 == null ? CollideWithObstacles.CCIntersection.alloc().init(var1, var2, (float)var13, (float)var15, var17, var18) : CollideWithObstacles.CCIntersection.alloc().init(var1, var2, (float)var13, (float)var15, var19 == null ? var20 : var19);
            }
         } else {
            return null;
         }
      }
   }

   void checkNodesInObstacles() {
      for(int var1 = 0; var1 < this.nodes.size(); ++var1) {
         CollideWithObstacles.CCNode var2 = (CollideWithObstacles.CCNode)this.nodes.get(var1);

         for(int var3 = 0; var3 < this.obstacles.size(); ++var3) {
            CollideWithObstacles.CCObstacle var4 = (CollideWithObstacles.CCObstacle)this.obstacles.get(var3);
            boolean var5 = false;

            for(int var6 = 0; var6 < this.intersections.size(); ++var6) {
               CollideWithObstacles.CCIntersection var7 = (CollideWithObstacles.CCIntersection)this.intersections.get(var6);
               if (var7.nodeSplit == var2) {
                  if (var7.edge1.obstacle == var4 || var7.edge2.obstacle == var4) {
                     var5 = true;
                  }
                  break;
               }
            }

            if (!var5 && var4.isNodeInsideOf(var2)) {
               var2.ignore = true;
               break;
            }
         }
      }

   }

   boolean isVisible(CollideWithObstacles.CCNode var1, CollideWithObstacles.CCNode var2) {
      if (var1.sharesEdge(var2)) {
         return !var1.onSameShapeButDoesNotShareAnEdge(var2);
      } else {
         return !var1.sharesShape(var2);
      }
   }

   void calculateNodeVisibility() {
      for(int var1 = 0; var1 < this.obstacles.size(); ++var1) {
         CollideWithObstacles.CCObstacle var2 = (CollideWithObstacles.CCObstacle)this.obstacles.get(var1);

         for(int var3 = 0; var3 < var2.edges.size(); ++var3) {
            CollideWithObstacles.CCEdge var4 = (CollideWithObstacles.CCEdge)var2.edges.get(var3);
            if (!var4.node1.ignore && !var4.node2.ignore && this.isVisible(var4.node1, var4.node2)) {
               var4.node1.visible.add(var4.node2);
               var4.node2.visible.add(var4.node1);
            }
         }
      }

   }

   Vector2f resolveCollision(IsoGameCharacter var1, float var2, float var3, Vector2f var4) {
      var4.set(var2, var3);
      if (var1.getCurrentSquare() != null && var1.getCurrentSquare().HasStairs()) {
         return var4;
      } else {
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
            this.move.set(var2 - var1.x, var3 - var1.y);
            this.move.normalize();

            int var10;
            for(var10 = 0; var10 < this.nodes.size(); ++var10) {
               ((CollideWithObstacles.CCNode)this.nodes.get(var10)).release();
            }

            for(var10 = 0; var10 < this.obstacles.size(); ++var10) {
               CollideWithObstacles.CCObstacle var11 = (CollideWithObstacles.CCObstacle)this.obstacles.get(var10);

               for(int var12 = 0; var12 < var11.edges.size(); ++var12) {
                  ((CollideWithObstacles.CCEdge)var11.edges.get(var12)).release();
               }

               var11.release();
            }

            for(var10 = 0; var10 < this.intersections.size(); ++var10) {
               ((CollideWithObstacles.CCIntersection)this.intersections.get(var10)).release();
            }

            this.intersections.clear();
            this.getObstaclesInRect(Math.min(var6, var2), Math.min(var7, var3), Math.max(var6, var2), Math.max(var7, var3), (int)var1.x, (int)var1.y, (int)var1.z);
            this.checkEdgeIntersection();
            this.checkNodesInObstacles();
            this.calculateNodeVisibility();
            CollideWithObstacles.CCNode var37;
            if (var5) {
               Iterator var38 = this.nodes.iterator();

               while(var38.hasNext()) {
                  var37 = (CollideWithObstacles.CCNode)var38.next();
                  Iterator var40 = var37.visible.iterator();

                  while(var40.hasNext()) {
                     CollideWithObstacles.CCNode var13 = (CollideWithObstacles.CCNode)var40.next();
                     LineDrawer.addLine(var37.x, var37.y, (float)var37.z, var13.x, var13.y, (float)var13.z, 0.0F, 1.0F, 0.0F, (String)null, true);
                  }

                  if (DebugOptions.instance.CollideWithObstaclesRenderNormals.getValue() && var37.getNormalAndEdgeVectors(this.nodeNormal, this.edgeVec)) {
                     LineDrawer.addLine(var37.x, var37.y, (float)var37.z, var37.x + this.nodeNormal.x, var37.y + this.nodeNormal.y, (float)var37.z, 0.0F, 0.0F, 1.0F, (String)null, true);
                  }

                  if (var37.ignore) {
                     LineDrawer.addLine(var37.x - 0.05F, var37.y - 0.05F, (float)var37.z, var37.x + 0.05F, var37.y + 0.05F, (float)var37.z, 1.0F, 1.0F, 0.0F, (String)null, false);
                  }
               }
            }

            CollideWithObstacles.CCEdge var39 = null;
            var37 = null;
            double var41 = Double.MAX_VALUE;

            for(int var14 = 0; var14 < this.obstacles.size(); ++var14) {
               CollideWithObstacles.CCObstacle var15 = (CollideWithObstacles.CCObstacle)this.obstacles.get(var14);
               byte var16 = 0;
               if (var15.isPointInside(var1.x, var1.y, var16)) {
                  for(int var17 = 0; var17 < var15.edges.size(); ++var17) {
                     CollideWithObstacles.CCEdge var18 = (CollideWithObstacles.CCEdge)var15.edges.get(var17);
                     if (var18.node1.visible.contains(var18.node2)) {
                        CollideWithObstacles.CCNode var19 = var18.closestPoint(var1.x, var1.y, this.closest);
                        double var20 = (double)((var1.x - this.closest.x) * (var1.x - this.closest.x) + (var1.y - this.closest.y) * (var1.y - this.closest.y));
                        if (var20 < var41) {
                           var41 = var20;
                           var39 = var18;
                           var37 = var19;
                        }
                     }
                  }
               }
            }

            float var42;
            if (var39 != null) {
               var42 = var39.normal.dot(this.move);
               if (var42 >= 0.01F) {
                  var39 = null;
               }
            }

            if (var37 != null && var37.getNormalAndEdgeVectors(this.nodeNormal, this.edgeVec) && this.nodeNormal.dot(this.move) + 0.05F >= this.nodeNormal.dot(this.edgeVec)) {
               var37 = null;
               var39 = null;
            }

            int var47;
            if (var39 == null) {
               double var43 = Double.MAX_VALUE;
               var39 = null;
               var37 = null;

               for(var47 = 0; var47 < this.obstacles.size(); ++var47) {
                  CollideWithObstacles.CCObstacle var48 = (CollideWithObstacles.CCObstacle)this.obstacles.get(var47);

                  for(int var51 = 0; var51 < var48.edges.size(); ++var51) {
                     CollideWithObstacles.CCEdge var54 = (CollideWithObstacles.CCEdge)var48.edges.get(var51);
                     if (var54.node1.visible.contains(var54.node2)) {
                        float var55 = var54.node1.x;
                        float var21 = var54.node1.y;
                        float var22 = var54.node2.x;
                        float var23 = var54.node2.y;
                        float var24 = var55 + 0.5F * (var22 - var55);
                        float var25 = var21 + 0.5F * (var23 - var21);
                        if (var5 && DebugOptions.instance.CollideWithObstaclesRenderNormals.getValue()) {
                           LineDrawer.addLine(var24, var25, (float)var54.node1.z, var24 + var54.normal.x, var25 + var54.normal.y, (float)var54.node1.z, 0.0F, 0.0F, 1.0F, (String)null, true);
                        }

                        double var26 = (double)((var23 - var21) * (var8 - var6) - (var22 - var55) * (var9 - var7));
                        if (var26 != 0.0D) {
                           double var28 = (double)((var22 - var55) * (var7 - var21) - (var23 - var21) * (var6 - var55)) / var26;
                           double var30 = (double)((var8 - var6) * (var7 - var21) - (var9 - var7) * (var6 - var55)) / var26;
                           float var32 = var54.normal.dot(this.move);
                           if (!(var32 >= 0.0F) && var28 >= 0.0D && var28 <= 1.0D && var30 >= 0.0D && var30 <= 1.0D) {
                              if (var30 < 0.01D || var30 > 0.99D) {
                                 CollideWithObstacles.CCNode var33 = var30 < 0.01D ? var54.node1 : var54.node2;
                                 if (var33.getNormalAndEdgeVectors(this.nodeNormal, this.edgeVec)) {
                                    if (!(this.nodeNormal.dot(this.move) + 0.05F >= this.nodeNormal.dot(this.edgeVec))) {
                                       var39 = var54;
                                       var37 = var33;
                                       break;
                                    }
                                    continue;
                                 }
                              }

                              float var56 = (float)((double)var6 + var28 * (double)(var8 - var6));
                              float var34 = (float)((double)var7 + var28 * (double)(var9 - var7));
                              double var35 = (double)IsoUtils.DistanceToSquared(var6, var7, var56, var34);
                              if (var35 < var43) {
                                 var43 = var35;
                                 var39 = var54;
                              }
                           }
                        }
                     }
                  }
               }
            }

            if (var37 != null) {
               CollideWithObstacles.CCEdge var45 = var39;
               CollideWithObstacles.CCEdge var44 = null;

               for(var47 = 0; var47 < var37.edges.size(); ++var47) {
                  CollideWithObstacles.CCEdge var50 = (CollideWithObstacles.CCEdge)var37.edges.get(var47);
                  if (var50.node1.visible.contains(var50.node2) && var50 != var39 && (var45.node1.x != var50.node1.x || var45.node1.y != var50.node1.y || var45.node2.x != var50.node2.x || var45.node2.y != var50.node2.y) && (var45.node1.x != var50.node2.x || var45.node1.y != var50.node2.y || var45.node2.x != var50.node1.x || var45.node2.y != var50.node1.y) && (!var45.hasNode(var50.node1) || !var45.hasNode(var50.node2))) {
                     var44 = var50;
                  }
               }

               if (var45 != null && var44 != null) {
                  CollideWithObstacles.CCNode var49;
                  if (var39 == var45) {
                     var49 = var37 == var44.node1 ? var44.node2 : var44.node1;
                     this.edgeVec.set(var49.x - var37.x, var49.y - var37.y);
                     this.edgeVec.normalize();
                     if (this.move.dot(this.edgeVec) >= 0.0F) {
                        var39 = var44;
                     }
                  } else if (var39 == var44) {
                     var49 = var37 == var45.node1 ? var45.node2 : var45.node1;
                     this.edgeVec.set(var49.x - var37.x, var49.y - var37.y);
                     this.edgeVec.normalize();
                     if (this.move.dot(this.edgeVec) >= 0.0F) {
                        var39 = var45;
                     }
                  }
               }
            }

            if (var39 != null) {
               var42 = var39.node1.x;
               float var46 = var39.node1.y;
               float var52 = var39.node2.x;
               float var53 = var39.node2.y;
               if (var5) {
                  LineDrawer.addLine(var42, var46, (float)var39.node1.z, var52, var53, (float)var39.node1.z, 0.0F, 1.0F, 1.0F, (String)null, true);
               }

               var39.closestPoint(var2, var3, this.closest);
               var4.set(this.closest.x, this.closest.y);
            }

            return var4;
         }
      }
   }

   private static final class ImmutableRectF {
      private float x;
      private float y;
      private float w;
      private float h;
      static ArrayDeque pool = new ArrayDeque();

      CollideWithObstacles.ImmutableRectF init(float var1, float var2, float var3, float var4) {
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

      boolean intersects(CollideWithObstacles.ImmutableRectF var1) {
         return this.left() < var1.right() && this.right() > var1.left() && this.top() < var1.bottom() && this.bottom() > var1.top();
      }

      static CollideWithObstacles.ImmutableRectF alloc() {
         return pool.isEmpty() ? new CollideWithObstacles.ImmutableRectF() : (CollideWithObstacles.ImmutableRectF)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class CCObjectOutline {
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
      static ArrayDeque pool = new ArrayDeque();

      CollideWithObstacles.CCObjectOutline init(int var1, int var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.nw = this.nw_w = this.nw_n = this.nw_e = false;
         this.w_w = this.w_e = this.w_cutoff = false;
         this.n_n = this.n_s = this.n_cutoff = false;
         return this;
      }

      static void setSolid(int var0, int var1, int var2, CollideWithObstacles.CCObjectOutline[][] var3) {
         setWest(var0, var1, var2, var3);
         setNorth(var0, var1, var2, var3);
         setWest(var0 + 1, var1, var2, var3);
         setNorth(var0, var1 + 1, var2, var3);
      }

      static void setWest(int var0, int var1, int var2, CollideWithObstacles.CCObjectOutline[][] var3) {
         CollideWithObstacles.CCObjectOutline var4 = get(var0, var1, var2, var3);
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

         CollideWithObstacles.CCObjectOutline var5 = var4;
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

      static void setNorth(int var0, int var1, int var2, CollideWithObstacles.CCObjectOutline[][] var3) {
         CollideWithObstacles.CCObjectOutline var4 = get(var0, var1, var2, var3);
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

         CollideWithObstacles.CCObjectOutline var5 = var4;
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

      static CollideWithObstacles.CCObjectOutline get(int var0, int var1, int var2, CollideWithObstacles.CCObjectOutline[][] var3) {
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

      void trace_NW_N(CollideWithObstacles.CCObjectOutline[][] var1, CollideWithObstacles.CCNode var2) {
         if (var2 != null) {
            var2.setXY((float)this.x + 0.3F, (float)this.y - 0.3F);
         } else {
            CollideWithObstacles.CCNode var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x + 0.3F, (float)this.y - 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.nw_n = false;
         if (this.nw_e) {
            this.trace_NW_E(var1, (CollideWithObstacles.CCNode)null);
         } else if (this.n_n) {
            this.trace_N_N(var1, (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1));
         }

      }

      void trace_NW_S(CollideWithObstacles.CCObjectOutline[][] var1, CollideWithObstacles.CCNode var2) {
         if (var2 != null) {
            var2.setXY((float)this.x - 0.3F, (float)this.y + 0.3F);
         } else {
            CollideWithObstacles.CCNode var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x - 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.nw_s = false;
         if (this.nw_w) {
            this.trace_NW_W(var1, (CollideWithObstacles.CCNode)null);
         } else {
            CollideWithObstacles.CCObjectOutline var4 = get(this.x - 1, this.y, this.z, var1);
            if (var4 == null) {
               return;
            }

            if (var4.n_s) {
               var4.nodes = this.nodes;
               var4.trace_N_S(var1, (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1));
            }
         }

      }

      void trace_NW_W(CollideWithObstacles.CCObjectOutline[][] var1, CollideWithObstacles.CCNode var2) {
         if (var2 != null) {
            var2.setXY((float)this.x - 0.3F, (float)this.y - 0.3F);
         } else {
            CollideWithObstacles.CCNode var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x - 0.3F, (float)this.y - 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.nw_w = false;
         if (this.nw_n) {
            this.trace_NW_N(var1, (CollideWithObstacles.CCNode)null);
         } else {
            CollideWithObstacles.CCObjectOutline var4 = get(this.x, this.y - 1, this.z, var1);
            if (var4 == null) {
               return;
            }

            if (var4.w_w) {
               var4.nodes = this.nodes;
               var4.trace_W_W(var1, (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1));
            }
         }

      }

      void trace_NW_E(CollideWithObstacles.CCObjectOutline[][] var1, CollideWithObstacles.CCNode var2) {
         if (var2 != null) {
            var2.setXY((float)this.x + 0.3F, (float)this.y + 0.3F);
         } else {
            CollideWithObstacles.CCNode var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x + 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.nw_e = false;
         if (this.nw_s) {
            this.trace_NW_S(var1, (CollideWithObstacles.CCNode)null);
         } else if (this.w_e) {
            this.trace_W_E(var1, (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1));
         }

      }

      void trace_W_E(CollideWithObstacles.CCObjectOutline[][] var1, CollideWithObstacles.CCNode var2) {
         CollideWithObstacles.CCNode var3;
         if (var2 != null) {
            var2.setXY((float)this.x + 0.3F, (float)(this.y + 1) - 0.3F);
         } else {
            var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x + 0.3F, (float)(this.y + 1) - 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.w_e = false;
         if (this.w_cutoff) {
            var3 = (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1);
            var3.setXY((float)this.x + 0.3F, (float)(this.y + 1) + 0.3F);
            var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x - 0.3F, (float)(this.y + 1) + 0.3F, this.z);
            this.nodes.add(var3);
            var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x - 0.3F, (float)(this.y + 1) - 0.3F, this.z);
            this.nodes.add(var3);
            this.trace_W_W(var1, var3);
         } else {
            CollideWithObstacles.CCObjectOutline var4 = get(this.x, this.y + 1, this.z, var1);
            if (var4 != null) {
               if (var4.nw && var4.nw_e) {
                  var4.nodes = this.nodes;
                  var4.trace_NW_E(var1, (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1));
               } else if (var4.n_n) {
                  var4.nodes = this.nodes;
                  var4.trace_N_N(var1, (CollideWithObstacles.CCNode)null);
               }

            }
         }
      }

      void trace_W_W(CollideWithObstacles.CCObjectOutline[][] var1, CollideWithObstacles.CCNode var2) {
         if (var2 != null) {
            var2.setXY((float)this.x - 0.3F, (float)this.y + 0.3F);
         } else {
            CollideWithObstacles.CCNode var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x - 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.w_w = false;
         if (this.nw_w) {
            this.trace_NW_W(var1, (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1));
         } else {
            CollideWithObstacles.CCObjectOutline var4 = get(this.x - 1, this.y, this.z, var1);
            if (var4 == null) {
               return;
            }

            if (var4.n_s) {
               var4.nodes = this.nodes;
               var4.trace_N_S(var1, (CollideWithObstacles.CCNode)null);
            }
         }

      }

      void trace_N_N(CollideWithObstacles.CCObjectOutline[][] var1, CollideWithObstacles.CCNode var2) {
         CollideWithObstacles.CCNode var3;
         if (var2 != null) {
            var2.setXY((float)(this.x + 1) - 0.3F, (float)this.y - 0.3F);
         } else {
            var3 = CollideWithObstacles.CCNode.alloc().init((float)(this.x + 1) - 0.3F, (float)this.y - 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.n_n = false;
         if (this.n_cutoff) {
            var3 = (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1);
            var3.setXY((float)(this.x + 1) + 0.3F, (float)this.y - 0.3F);
            var3 = CollideWithObstacles.CCNode.alloc().init((float)(this.x + 1) + 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
            var3 = CollideWithObstacles.CCNode.alloc().init((float)(this.x + 1) - 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
            this.trace_N_S(var1, var3);
         } else {
            CollideWithObstacles.CCObjectOutline var4 = get(this.x + 1, this.y, this.z, var1);
            if (var4 != null) {
               if (var4.nw_n) {
                  var4.nodes = this.nodes;
                  var4.trace_NW_N(var1, (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1));
               } else {
                  var4 = get(this.x + 1, this.y - 1, this.z, var1);
                  if (var4 == null) {
                     return;
                  }

                  if (var4.w_w) {
                     var4.nodes = this.nodes;
                     var4.trace_W_W(var1, (CollideWithObstacles.CCNode)null);
                  }
               }

            }
         }
      }

      void trace_N_S(CollideWithObstacles.CCObjectOutline[][] var1, CollideWithObstacles.CCNode var2) {
         if (var2 != null) {
            var2.setXY((float)this.x + 0.3F, (float)this.y + 0.3F);
         } else {
            CollideWithObstacles.CCNode var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x + 0.3F, (float)this.y + 0.3F, this.z);
            this.nodes.add(var3);
         }

         this.n_s = false;
         if (this.nw_s) {
            this.trace_NW_S(var1, (CollideWithObstacles.CCNode)this.nodes.get(this.nodes.size() - 1));
         } else if (this.w_e) {
            this.trace_W_E(var1, (CollideWithObstacles.CCNode)null);
         }

      }

      void trace(CollideWithObstacles.CCObjectOutline[][] var1, ArrayList var2) {
         var2.clear();
         this.nodes = var2;
         CollideWithObstacles.CCNode var3 = CollideWithObstacles.CCNode.alloc().init((float)this.x - 0.3F, (float)this.y - 0.3F, this.z);
         var2.add(var3);
         this.trace_NW_N(var1, (CollideWithObstacles.CCNode)null);
         if (var2.size() != 2 && var3.x == ((CollideWithObstacles.CCNode)var2.get(var2.size() - 1)).x && var3.y == ((CollideWithObstacles.CCNode)var2.get(var2.size() - 1)).y) {
            ((CollideWithObstacles.CCNode)var2.get(var2.size() - 1)).release();
            var2.set(var2.size() - 1, var3);
         } else {
            var2.clear();
         }

      }

      static CollideWithObstacles.CCObjectOutline alloc() {
         return pool.isEmpty() ? new CollideWithObstacles.CCObjectOutline() : (CollideWithObstacles.CCObjectOutline)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   static final class CompareIntersection implements Comparator {
      CollideWithObstacles.CCEdge edge;

      public int compare(CollideWithObstacles.CCIntersection var1, CollideWithObstacles.CCIntersection var2) {
         float var3 = this.edge == var1.edge1 ? var1.dist1 : var1.dist2;
         float var4 = this.edge == var2.edge1 ? var2.dist1 : var2.dist2;
         if (var3 < var4) {
            return -1;
         } else {
            return var3 > var4 ? 1 : 0;
         }
      }
   }

   private static final class CCNode {
      float x;
      float y;
      int z;
      boolean ignore;
      final ArrayList edges = new ArrayList();
      final ArrayList visible = new ArrayList();
      static ArrayList tempObstacles = new ArrayList();
      static ArrayDeque pool = new ArrayDeque();

      CollideWithObstacles.CCNode init(float var1, float var2, int var3) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.ignore = false;
         this.edges.clear();
         this.visible.clear();
         return this;
      }

      CollideWithObstacles.CCNode setXY(float var1, float var2) {
         this.x = var1;
         this.y = var2;
         return this;
      }

      boolean sharesEdge(CollideWithObstacles.CCNode var1) {
         for(int var2 = 0; var2 < this.edges.size(); ++var2) {
            CollideWithObstacles.CCEdge var3 = (CollideWithObstacles.CCEdge)this.edges.get(var2);
            if (var3.hasNode(var1)) {
               return true;
            }
         }

         return false;
      }

      boolean sharesShape(CollideWithObstacles.CCNode var1) {
         for(int var2 = 0; var2 < this.edges.size(); ++var2) {
            CollideWithObstacles.CCEdge var3 = (CollideWithObstacles.CCEdge)this.edges.get(var2);

            for(int var4 = 0; var4 < var1.edges.size(); ++var4) {
               CollideWithObstacles.CCEdge var5 = (CollideWithObstacles.CCEdge)var1.edges.get(var4);
               if (var3.obstacle != null && var3.obstacle == var5.obstacle) {
                  return true;
               }
            }
         }

         return false;
      }

      void getObstacles(ArrayList var1) {
         for(int var2 = 0; var2 < this.edges.size(); ++var2) {
            CollideWithObstacles.CCEdge var3 = (CollideWithObstacles.CCEdge)this.edges.get(var2);
            if (!var1.contains(var3.obstacle)) {
               var1.add(var3.obstacle);
            }
         }

      }

      boolean onSameShapeButDoesNotShareAnEdge(CollideWithObstacles.CCNode var1) {
         tempObstacles.clear();
         this.getObstacles(tempObstacles);

         for(int var2 = 0; var2 < tempObstacles.size(); ++var2) {
            CollideWithObstacles.CCObstacle var3 = (CollideWithObstacles.CCObstacle)tempObstacles.get(var2);
            if (var3.hasNode(var1) && !var3.hasAdjacentNodes(this, var1)) {
               return true;
            }
         }

         return false;
      }

      boolean getNormalAndEdgeVectors(Vector2 var1, Vector2 var2) {
         CollideWithObstacles.CCEdge var3 = null;
         CollideWithObstacles.CCEdge var4 = null;

         for(int var5 = 0; var5 < this.edges.size(); ++var5) {
            CollideWithObstacles.CCEdge var6 = (CollideWithObstacles.CCEdge)this.edges.get(var5);
            if (var6.node1.visible.contains(var6.node2)) {
               if (var3 == null) {
                  var3 = var6;
               } else if (!var3.hasNode(var6.node1) || !var3.hasNode(var6.node2)) {
                  var4 = var6;
               }
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

      static CollideWithObstacles.CCNode alloc() {
         boolean var0;
         if (pool.isEmpty()) {
            var0 = false;
         } else {
            var0 = false;
         }

         return pool.isEmpty() ? new CollideWithObstacles.CCNode() : (CollideWithObstacles.CCNode)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class CCObstacle {
      final ArrayList edges = new ArrayList();
      CollideWithObstacles.ImmutableRectF bounds;
      static ArrayDeque pool = new ArrayDeque();

      CollideWithObstacles.CCObstacle init() {
         this.edges.clear();
         return this;
      }

      boolean hasNode(CollideWithObstacles.CCNode var1) {
         for(int var2 = 0; var2 < this.edges.size(); ++var2) {
            CollideWithObstacles.CCEdge var3 = (CollideWithObstacles.CCEdge)this.edges.get(var2);
            if (var3.hasNode(var1)) {
               return true;
            }
         }

         return false;
      }

      boolean hasAdjacentNodes(CollideWithObstacles.CCNode var1, CollideWithObstacles.CCNode var2) {
         for(int var3 = 0; var3 < this.edges.size(); ++var3) {
            CollideWithObstacles.CCEdge var4 = (CollideWithObstacles.CCEdge)this.edges.get(var3);
            if (var4.hasNode(var1) && var4.hasNode(var2)) {
               return true;
            }
         }

         return false;
      }

      boolean isPointInPolygon_CrossingNumber(float var1, float var2) {
         int var3 = 0;

         for(int var4 = 0; var4 < this.edges.size(); ++var4) {
            CollideWithObstacles.CCEdge var5 = (CollideWithObstacles.CCEdge)this.edges.get(var4);
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

      CollideWithObstacles.EdgeRingHit isPointInPolygon_WindingNumber(float var1, float var2, int var3) {
         int var4 = 0;

         for(int var5 = 0; var5 < this.edges.size(); ++var5) {
            CollideWithObstacles.CCEdge var6 = (CollideWithObstacles.CCEdge)this.edges.get(var5);
            if ((var3 & 16) != 0 && var6.isPointOn(var1, var2)) {
               return CollideWithObstacles.EdgeRingHit.OnEdge;
            }

            if (var6.node1.y <= var2) {
               if (var6.node2.y > var2 && this.isLeft(var6.node1.x, var6.node1.y, var6.node2.x, var6.node2.y, var1, var2) > 0.0F) {
                  ++var4;
               }
            } else if (var6.node2.y <= var2 && this.isLeft(var6.node1.x, var6.node1.y, var6.node2.x, var6.node2.y, var1, var2) < 0.0F) {
               --var4;
            }
         }

         return var4 == 0 ? CollideWithObstacles.EdgeRingHit.Outside : CollideWithObstacles.EdgeRingHit.Inside;
      }

      boolean isPointInside(float var1, float var2, int var3) {
         return this.isPointInPolygon_WindingNumber(var1, var2, var3) == CollideWithObstacles.EdgeRingHit.Inside;
      }

      boolean isNodeInsideOf(CollideWithObstacles.CCNode var1) {
         if (this.hasNode(var1)) {
            return false;
         } else if (!this.bounds.containsPoint(var1.x, var1.y)) {
            return false;
         } else {
            byte var2 = 0;
            return this.isPointInside(var1.x, var1.y, var2);
         }
      }

      CollideWithObstacles.CCNode getClosestPointOnEdge(float var1, float var2, Vector2 var3) {
         double var4 = Double.MAX_VALUE;
         CollideWithObstacles.CCNode var6 = null;
         float var7 = Float.MAX_VALUE;
         float var8 = Float.MAX_VALUE;

         for(int var9 = 0; var9 < this.edges.size(); ++var9) {
            CollideWithObstacles.CCEdge var10 = (CollideWithObstacles.CCEdge)this.edges.get(var9);
            if (var10.node1.visible.contains(var10.node2)) {
               CollideWithObstacles.CCNode var11 = var10.closestPoint(var1, var2, var3);
               double var12 = (double)((var1 - var3.x) * (var1 - var3.x) + (var2 - var3.y) * (var2 - var3.y));
               if (var12 < var4) {
                  var7 = var3.x;
                  var8 = var3.y;
                  var6 = var11;
                  var4 = var12;
               }
            }
         }

         var3.set(var7, var8);
         return var6;
      }

      void calcBounds() {
         float var1 = Float.MAX_VALUE;
         float var2 = Float.MAX_VALUE;
         float var3 = Float.MIN_VALUE;
         float var4 = Float.MIN_VALUE;

         for(int var5 = 0; var5 < this.edges.size(); ++var5) {
            CollideWithObstacles.CCEdge var6 = (CollideWithObstacles.CCEdge)this.edges.get(var5);
            var1 = Math.min(var1, var6.node1.x);
            var2 = Math.min(var2, var6.node1.y);
            var3 = Math.max(var3, var6.node1.x);
            var4 = Math.max(var4, var6.node1.y);
         }

         if (this.bounds != null) {
            this.bounds.release();
         }

         float var7 = 0.01F;
         this.bounds = CollideWithObstacles.ImmutableRectF.alloc().init(var1 - var7, var2 - var7, var3 - var1 + var7 * 2.0F, var4 - var2 + var7 * 2.0F);
      }

      static CollideWithObstacles.CCObstacle alloc() {
         return pool.isEmpty() ? new CollideWithObstacles.CCObstacle() : (CollideWithObstacles.CCObstacle)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class CCEdge {
      CollideWithObstacles.CCNode node1;
      CollideWithObstacles.CCNode node2;
      CollideWithObstacles.CCObstacle obstacle;
      final ArrayList intersections = new ArrayList();
      final Vector2 normal = new Vector2();
      static ArrayDeque pool = new ArrayDeque();

      CollideWithObstacles.CCEdge init(CollideWithObstacles.CCNode var1, CollideWithObstacles.CCNode var2, CollideWithObstacles.CCObstacle var3) {
         if (var1.x == var2.x && var1.y == var2.y) {
            boolean var4 = false;
         }

         this.node1 = var1;
         this.node2 = var2;
         var1.edges.add(this);
         var2.edges.add(this);
         this.obstacle = var3;
         this.intersections.clear();
         this.normal.set(var2.x - var1.x, var2.y - var1.y);
         this.normal.normalize();
         this.normal.rotate((float)Math.toRadians(90.0D));
         return this;
      }

      boolean hasNode(CollideWithObstacles.CCNode var1) {
         return var1 == this.node1 || var1 == this.node2;
      }

      CollideWithObstacles.CCEdge split(CollideWithObstacles.CCNode var1) {
         CollideWithObstacles.CCEdge var2 = alloc().init(var1, this.node2, this.obstacle);
         this.obstacle.edges.add(this.obstacle.edges.indexOf(this) + 1, var2);
         this.node2.edges.remove(this);
         this.node2 = var1;
         this.node2.edges.add(this);
         return var2;
      }

      CollideWithObstacles.CCNode closestPoint(float var1, float var2, Vector2 var3) {
         float var4 = this.node1.x;
         float var5 = this.node1.y;
         float var6 = this.node2.x;
         float var7 = this.node2.y;
         double var8 = (double)((var1 - var4) * (var6 - var4) + (var2 - var5) * (var7 - var5)) / (Math.pow((double)(var6 - var4), 2.0D) + Math.pow((double)(var7 - var5), 2.0D));
         double var10 = 0.001D;
         if (var8 <= 0.0D + var10) {
            var3.set(var4, var5);
            return this.node1;
         } else if (var8 >= 1.0D - var10) {
            var3.set(var6, var7);
            return this.node2;
         } else {
            double var12 = (double)var4 + var8 * (double)(var6 - var4);
            double var14 = (double)var5 + var8 * (double)(var7 - var5);
            var3.set((float)var12, (float)var14);
            return null;
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

      static CollideWithObstacles.CCEdge alloc() {
         return pool.isEmpty() ? new CollideWithObstacles.CCEdge() : (CollideWithObstacles.CCEdge)pool.pop();
      }

      void release() {
         assert !pool.contains(this);

         pool.push(this);
      }
   }

   private static final class CCIntersection {
      CollideWithObstacles.CCEdge edge1;
      CollideWithObstacles.CCEdge edge2;
      float dist1;
      float dist2;
      CollideWithObstacles.CCNode nodeSplit;
      static ArrayDeque pool = new ArrayDeque();

      CollideWithObstacles.CCIntersection init(CollideWithObstacles.CCEdge var1, CollideWithObstacles.CCEdge var2, float var3, float var4, float var5, float var6) {
         this.edge1 = var1;
         this.edge2 = var2;
         this.dist1 = var3;
         this.dist2 = var4;
         this.nodeSplit = CollideWithObstacles.CCNode.alloc().init(var5, var6, var1.node1.z);
         return this;
      }

      CollideWithObstacles.CCIntersection init(CollideWithObstacles.CCEdge var1, CollideWithObstacles.CCEdge var2, float var3, float var4, CollideWithObstacles.CCNode var5) {
         this.edge1 = var1;
         this.edge2 = var2;
         this.dist1 = var3;
         this.dist2 = var4;
         this.nodeSplit = var5;
         return this;
      }

      CollideWithObstacles.CCEdge split(CollideWithObstacles.CCEdge var1) {
         if (var1.hasNode(this.nodeSplit)) {
            return null;
         } else if (var1.node1.x == this.nodeSplit.x && var1.node1.y == this.nodeSplit.y) {
            return null;
         } else {
            return var1.node2.x == this.nodeSplit.x && var1.node2.y == this.nodeSplit.y ? null : var1.split(this.nodeSplit);
         }
      }

      static CollideWithObstacles.CCIntersection alloc() {
         return pool.isEmpty() ? new CollideWithObstacles.CCIntersection() : (CollideWithObstacles.CCIntersection)pool.pop();
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
      private static CollideWithObstacles.EdgeRingHit[] $values() {
         return new CollideWithObstacles.EdgeRingHit[]{OnEdge, Inside, Outside};
      }
   }
}
