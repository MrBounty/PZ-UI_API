package zombie.vehicles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.joml.Vector3f;
import org.lwjgl.util.vector.Vector2f;
import zombie.characters.IsoPlayer;
import zombie.core.physics.CarController;
import zombie.core.physics.WorldSimulation;
import zombie.debug.LineDrawer;

public class CircleLineIntersect {
   public static CircleLineIntersect.Collideresult checkforcecirclescollidetime(List var0, ArrayList var1, double[] var2, boolean[] var3, boolean var4) {
      CircleLineIntersect.PointVector[] var6 = new CircleLineIntersect.PointVector[var0.size()];
      double[] var7 = new double[var0.size()];
      CircleLineIntersect.Collideclassindex[] var8 = new CircleLineIntersect.Collideclassindex[var0.size()];
      double[] var9 = new double[var0.size()];

      for(int var10 = var0.size() - 1; var10 >= 0; --var10) {
         var7[var10] = -1.0D;
         var8[var10] = new CircleLineIntersect.Collideclassindex();
         var6[var10] = (CircleLineIntersect.PointVector)var0.get(var10);
         var9[var10] = 1.0D;
      }

      CircleLineIntersect.ForceCircle var5;
      int var12;
      double var18;
      for(int var11 = Math.min(var0.size(), var2.length) - 1; var11 >= 0; --var11) {
         if (var4 || var3[var11]) {
            var5 = (CircleLineIntersect.ForceCircle)var0.get(var11);

            for(var12 = var1.size() - 1; var12 >= 0; --var12) {
               CircleLineIntersect.StaticLine var75 = (CircleLineIntersect.StaticLine)var1.get(var12);
               CircleLineIntersect.Point var13 = CircleLineIntersect.VectorMath.closestpointonline(var75.getX1(), var75.getY1(), var75.getX2(), var75.getY2(), var5.getX(), var5.getY());
               double var14 = CircleLineIntersect.Point.distanceSq(var13.getX(), var13.getY(), var5.getX(), var5.getY());
               double var16;
               double var22;
               double var24;
               double var78;
               if (var14 < var5.getRadiusSq()) {
                  var16 = 0.0D;
                  var18 = 0.0D;
                  if (var14 == 0.0D) {
                     CircleLineIntersect.Point var20 = CircleLineIntersect.Point.midpoint(var75.getP1(), var75.getP2());
                     double var21 = var75.getP1().distance(var75.getP2());
                     double var23 = var5.distanceSq(var20);
                     if (var23 < Math.pow(var5.getRadius() + var21 / 2.0D, 2.0D)) {
                        if (var23 != 0.0D) {
                           double var25 = var5.distance(var20);
                           double var27 = (var5.getX() - var20.getX()) / var25;
                           double var29 = (var5.getY() - var20.getY()) / var25;
                           var16 = var20.getX() + (var5.getRadius() + var21 / 2.0D) * var27;
                           var18 = var20.getY() + (var5.getRadius() + var21 / 2.0D) * var29;
                        } else {
                           var16 = var5.getX();
                           var18 = var5.getY();
                        }

                        if (var7[var11] == -1.0D) {
                           var6[var11] = new CircleLineIntersect.PointVector(var16, var18);
                        } else {
                           var6[var11].setPoint(var16, var18);
                        }

                        if (var7[var11] == 0.0D) {
                           var8[var11].addCollided(var75, var12, var5.getVector());
                        } else {
                           var8[var11].setCollided(var75, var12, var5.getVector());
                        }

                        var7[var11] = 0.0D;
                        continue;
                     }

                     if (var23 == Math.pow(var5.getRadius() + var21 / 2.0D, 2.0D) && var5.getLength() == 0.0D) {
                        continue;
                     }
                  } else {
                     if (Math.min(var75.getX1(), var75.getX2()) <= var13.getX() && var13.getX() <= Math.max(var75.getX1(), var75.getX2()) && Math.min(var75.getY1(), var75.getY2()) <= var13.getY() && var13.getY() <= Math.max(var75.getY1(), var75.getY2())) {
                        var78 = Math.sqrt(var14);
                        var22 = (var5.getX() - var13.getX()) / var78;
                        var24 = (var5.getY() - var13.getY()) / var78;
                        var16 = var13.getX() + var5.getRadius() * var22;
                        var18 = var13.getY() + var5.getRadius() * var24;
                        if (var7[var11] == -1.0D) {
                           var6[var11] = new CircleLineIntersect.PointVector(var16, var18);
                        } else {
                           var6[var11].setPoint(var16, var18);
                        }

                        if (var7[var11] == 0.0D) {
                           var8[var11].addCollided(var75, var12, var5.getVector());
                        } else {
                           var8[var11].setCollided(var75, var12, var5.getVector());
                        }

                        var7[var11] = 0.0D;
                        continue;
                     }

                     if (CircleLineIntersect.Point.distanceSq(var5.getX(), var5.getY(), var75.getX1(), var75.getY1()) < var5.getRadiusSq()) {
                        var78 = CircleLineIntersect.Point.distance(var5.getX(), var5.getY(), var75.getX1(), var75.getY1());
                        var22 = (var5.getX() - var75.getX1()) / var78;
                        var24 = (var5.getY() - var75.getY1()) / var78;
                        var16 = var75.getX1() + var5.getRadius() * var22;
                        var18 = var75.getY1() + var5.getRadius() * var24;
                        if (var7[var11] == -1.0D) {
                           var6[var11] = new CircleLineIntersect.PointVector(var16, var18);
                        } else {
                           var6[var11].setPoint(var16, var18);
                        }

                        if (var7[var11] == 0.0D) {
                           var8[var11].addCollided(var75, var12, var5.getVector());
                        } else {
                           var8[var11].setCollided(var75, var12, var5.getVector());
                        }

                        var7[var11] = 0.0D;
                        continue;
                     }

                     if (CircleLineIntersect.Point.distanceSq(var5.getX(), var5.getY(), var75.getX2(), var75.getY2()) < var5.getRadiusSq()) {
                        var78 = CircleLineIntersect.Point.distance(var5.getX(), var5.getY(), var75.getX2(), var75.getY2());
                        var22 = (var5.getX() - var75.getX2()) / var78;
                        var24 = (var5.getY() - var75.getY2()) / var78;
                        var16 = var75.getX2() + var5.getRadius() * var22;
                        var18 = var75.getY2() + var5.getRadius() * var24;
                        if (var7[var11] == -1.0D) {
                           var6[var11] = new CircleLineIntersect.PointVector(var16, var18);
                        } else {
                           var6[var11].setPoint(var16, var18);
                        }

                        if (var7[var11] == 0.0D) {
                           var8[var11].addCollided(var75, var12, var5.getVector());
                        } else {
                           var8[var11].setCollided(var75, var12, var5.getVector());
                        }

                        var7[var11] = 0.0D;
                        continue;
                     }
                  }
               }

               var16 = var75.getY2() - var75.getY1();
               var18 = var75.getX1() - var75.getX2();
               var78 = (var75.getY2() - var75.getY1()) * var75.getX1() + (var75.getX1() - var75.getX2()) * var75.getY1();
               var22 = var5.getvy();
               var24 = -var5.getvx();
               double var26 = var5.getvy() * var5.getX() + -var5.getvx() * var5.getY();
               double var28 = var16 * var24 - var22 * var18;
               double var30 = 0.0D;
               double var32 = 0.0D;
               if (var28 != 0.0D) {
                  var30 = (var24 * var78 - var18 * var26) / var28;
                  var32 = (var16 * var26 - var22 * var78) / var28;
               }

               CircleLineIntersect.Point var34 = CircleLineIntersect.VectorMath.closestpointonline(var75.getX1(), var75.getY1(), var75.getX2(), var75.getY2(), var5.getX2(), var5.getY2());
               CircleLineIntersect.Point var35 = CircleLineIntersect.VectorMath.closestpointonline(var5.getX(), var5.getY(), var5.getX2(), var5.getY2(), var75.getX1(), var75.getY1());
               CircleLineIntersect.Point var36 = CircleLineIntersect.VectorMath.closestpointonline(var5.getX(), var5.getY(), var5.getX2(), var5.getY2(), var75.getX2(), var75.getY2());
               if (CircleLineIntersect.Point.distanceSq(var34.getX(), var34.getY(), var5.getX2(), var5.getY2()) < var5.getRadiusSq() && Math.min(var75.getX1(), var75.getX2()) <= var34.getX() && var34.getX() <= Math.max(var75.getX1(), var75.getX2()) && Math.min(var75.getY1(), var75.getY2()) <= var34.getY() && var34.getY() <= Math.max(var75.getY1(), var75.getY2()) || CircleLineIntersect.Point.distanceSq(var35.getX(), var35.getY(), var75.getX1(), var75.getY1()) < var5.getRadiusSq() && Math.min(var5.getX(), var5.getX() + var5.getvx()) <= var35.getX() && var35.getX() <= Math.max(var5.getX(), var5.getX() + var5.getvx()) && Math.min(var5.getY(), var5.getY() + var5.getvy()) <= var35.getY() && var35.getY() <= Math.max(var5.getY(), var5.getY() + var5.getvy()) || CircleLineIntersect.Point.distanceSq(var36.getX(), var36.getY(), var75.getX2(), var75.getY2()) < var5.getRadiusSq() && Math.min(var5.getX(), var5.getX() + var5.getvx()) <= var36.getX() && var36.getX() <= Math.max(var5.getX(), var5.getX() + var5.getvx()) && Math.min(var5.getY(), var5.getY() + var5.getvy()) <= var36.getY() && var36.getY() <= Math.max(var5.getY(), var5.getY() + var5.getvy()) || Math.min(var5.getX(), var5.getX() + var5.getvx()) <= var30 && var30 <= Math.max(var5.getX(), var5.getX() + var5.getvx()) && Math.min(var5.getY(), var5.getY() + var5.getvy()) <= var32 && var32 <= Math.max(var5.getY(), var5.getY() + var5.getvy()) && Math.min(var75.getX1(), var75.getX2()) <= var30 && var30 <= Math.max(var75.getX1(), var75.getX2()) && Math.min(var75.getY1(), var75.getY2()) <= var32 && var32 <= Math.max(var75.getY1(), var75.getY2()) || CircleLineIntersect.Point.distanceSq(var75.getX1(), var75.getY1(), var5.getX2(), var5.getY2()) <= var5.getRadiusSq() || CircleLineIntersect.Point.distanceSq(var75.getX2(), var75.getY2(), var5.getX2(), var5.getY2()) <= var5.getRadiusSq()) {
                  double var37 = -var18;
                  double var41 = var37 * var5.getX() + var16 * var5.getY();
                  double var43 = var16 * var16 - var37 * var18;
                  double var45 = 0.0D;
                  double var47 = 0.0D;
                  if (var43 != 0.0D) {
                     var45 = (var16 * var78 - var18 * var41) / var43;
                     var47 = (var16 * var41 - var37 * var78) / var43;
                     double var49 = CircleLineIntersect.Point.distance(var30, var32, var5.getX(), var5.getY()) * var5.getRadius() / CircleLineIntersect.Point.distance(var45, var47, var5.getX(), var5.getY());
                     var30 += -var49 * var5.getnormvx();
                     var32 += -var49 * var5.getnormvy();
                     double var51 = var37 * var30 + var16 * var32;
                     double var53 = (var16 * var78 - var18 * var51) / var43;
                     double var55 = (var16 * var51 - var37 * var78) / var43;
                     double var57;
                     CircleLineIntersect.Point var64;
                     if (Math.min(var75.getX1(), var75.getX2()) <= var53 && var53 <= Math.max(var75.getX1(), var75.getX2()) && Math.min(var75.getY1(), var75.getY2()) <= var55 && var55 <= Math.max(var75.getY1(), var75.getY2())) {
                        var57 = var45;
                        double var81 = var47;
                        var45 += var30 - var53;
                        var47 += var32 - var55;
                        double var61 = Math.pow(var30 - var5.getX(), 2.0D) + Math.pow(var32 - var5.getY(), 2.0D);
                        if (var61 <= var7[var11] || var7[var11] < 0.0D) {
                           CircleLineIntersect.RectVector var63 = null;
                           if (!var8[var11].collided() || var7[var11] != var61) {
                              for(int var82 = 0; var82 < var8[var11].size(); ++var82) {
                                 if (var8[var11].collided() && ((CircleLineIntersect.Collider)var8[var11].getColliders().get(var82)).getCollideobj() instanceof CircleLineIntersect.ForceCircle && var7[var11] > var61) {
                                    var6[((CircleLineIntersect.Collider)var8[var11].getColliders().get(var82)).getCollidewith()] = new CircleLineIntersect.PointVector((CircleLineIntersect.PointVector)var0.get(((CircleLineIntersect.Collider)var8[var11].getColliders().get(var82)).getCollidewith()));
                                    var7[((CircleLineIntersect.Collider)var8[var11].getColliders().get(var82)).getCollidewith()] = -1.0D;
                                 }
                              }
                           }

                           if (CircleLineIntersect.Point.distanceSq(var45, var47, var5.getX(), var5.getY()) < 1.0E-8D) {
                              var64 = CircleLineIntersect.VectorMath.closestpointonline(var75.getX1() + (var30 - var57), var75.getY1() + (var32 - var81), var75.getX2() + (var30 - var57), var75.getY2() + (var32 - var81), var5.getX2(), var5.getY2());
                              var63 = new CircleLineIntersect.RectVector(var64.getX() + (var64.getX() - var5.getX2()) - var5.getX(), var64.getY() + (var64.getY() - var5.getY2()) - var5.getY());
                              var63 = (CircleLineIntersect.RectVector)var63.getUnitVector();
                              var63 = new CircleLineIntersect.RectVector(var63.getvx() * var5.getLength(), var63.getvy() * var5.getLength());
                           } else {
                              var63 = new CircleLineIntersect.RectVector(var5.getX() - 2.0D * (var45 - var30) - var30, var5.getY() - 2.0D * (var47 - var32) - var32);
                              var63 = (CircleLineIntersect.RectVector)var63.getUnitVector();
                              var63 = new CircleLineIntersect.RectVector(var63.getvx() * var5.getLength(), var63.getvy() * var5.getLength());
                           }

                           var63 = (CircleLineIntersect.RectVector)var63.getUnitVector();
                           var63 = new CircleLineIntersect.RectVector(var63.getvx() * var5.getLength(), var63.getvy() * var5.getLength());
                           if (var7[var11] == -1.0D) {
                              var6[var11] = new CircleLineIntersect.PointVector(var30, var32);
                           } else {
                              var6[var11].setPoint(var30, var32);
                           }

                           if (var7[var11] == var61) {
                              var8[var11].addCollided(var75, var12, var63);
                           } else {
                              var8[var11].setCollided(var75, var12, var63);
                           }

                           var7[var11] = var61;
                        }
                     } else {
                        var57 = var5.getRadius() * var5.getRadius();
                        CircleLineIntersect.Point var59 = CircleLineIntersect.VectorMath.closestpointonline(var5.getX(), var5.getY(), var5.getX2(), var5.getY2(), var75.getX1(), var75.getY1());
                        double var60 = CircleLineIntersect.Point.distanceSq(var59.getX(), var59.getY(), var75.getX1(), var75.getY1());
                        double var62 = CircleLineIntersect.Point.distanceSq(var59.getX(), var59.getY(), var5.getX(), var5.getY());
                        var64 = CircleLineIntersect.VectorMath.closestpointonline(var5.getX(), var5.getY(), var5.getX2(), var5.getY2(), var75.getX2(), var75.getY2());
                        double var65 = CircleLineIntersect.Point.distanceSq(var64.getX(), var64.getY(), var75.getX2(), var75.getY2());
                        double var67 = CircleLineIntersect.Point.distanceSq(var64.getX(), var64.getY(), var5.getX(), var5.getY());
                        double var69 = 0.0D;
                        if (var62 < var67 && var60 <= var65) {
                           var69 = Math.sqrt(Math.abs(var57 - var60));
                           var30 = var59.getX() - var69 * var5.getnormvx();
                           var32 = var59.getY() - var69 * var5.getnormvy();
                           var45 = var75.getX1();
                           var47 = var75.getY1();
                        } else if (var62 > var67 && var60 >= var65) {
                           var69 = Math.sqrt(Math.abs(var57 - var65));
                           var30 = var64.getX() - var69 * var5.getnormvx();
                           var32 = var64.getY() - var69 * var5.getnormvy();
                           var45 = var75.getX2();
                           var47 = var75.getY2();
                        } else if (var60 < var65) {
                           if (!(var62 < var67) && !(CircleLineIntersect.Point.distanceSq(var53, var55, var75.getX1(), var75.getY1()) <= var57)) {
                              var69 = Math.sqrt(Math.abs(var57 - var65));
                              var30 = var64.getX() - var69 * var5.getnormvx();
                              var32 = var64.getY() - var69 * var5.getnormvy();
                              var45 = var75.getX2();
                              var47 = var75.getY2();
                           } else {
                              var69 = Math.sqrt(Math.abs(var57 - var60));
                              var30 = var59.getX() - var69 * var5.getnormvx();
                              var32 = var59.getY() - var69 * var5.getnormvy();
                              var45 = var75.getX1();
                              var47 = var75.getY1();
                           }
                        } else if (var60 > var65) {
                           if (!(var67 < var62) && !(CircleLineIntersect.Point.distanceSq(var53, var55, var75.getX2(), var75.getY2()) <= var57)) {
                              var69 = Math.sqrt(Math.abs(var57 - var60));
                              var30 = var59.getX() - var69 * var5.getnormvx();
                              var32 = var59.getY() - var69 * var5.getnormvy();
                              var45 = var75.getX1();
                              var47 = var75.getY1();
                           } else {
                              var69 = Math.sqrt(Math.abs(var57 - var65));
                              var30 = var64.getX() - var69 * var5.getnormvx();
                              var32 = var64.getY() - var69 * var5.getnormvy();
                              var45 = var75.getX2();
                              var47 = var75.getY2();
                           }
                        } else if ((!(Math.min(var5.getX(), var5.getX2()) <= var64.getX()) || !(var64.getX() <= Math.max(var5.getX(), var5.getX2())) || !(Math.min(var5.getY(), var5.getY2()) <= var64.getY()) || !(var64.getY() <= Math.max(var5.getY(), var5.getY2()))) && !(CircleLineIntersect.Point.distanceSq(var64.getX(), var64.getY(), var5.getX2(), var5.getY2()) <= var5.getRadiusSq())) {
                           var69 = Math.sqrt(Math.abs(var57 - var60));
                           var30 = var59.getX() - var69 * var5.getnormvx();
                           var32 = var59.getY() - var69 * var5.getnormvy();
                           var45 = var75.getX1();
                           var47 = var75.getY1();
                        } else if ((!(Math.min(var5.getX(), var5.getX2()) <= var59.getX()) || !(var59.getX() <= Math.max(var5.getX(), var5.getX2())) || !(Math.min(var5.getY(), var5.getY2()) <= var59.getY()) || !(var59.getY() <= Math.max(var5.getY(), var5.getY2()))) && !(CircleLineIntersect.Point.distanceSq(var64.getX(), var64.getY(), var5.getX2(), var5.getY2()) <= var5.getRadiusSq())) {
                           var69 = Math.sqrt(Math.abs(var57 - var65));
                           var30 = var64.getX() - var69 * var5.getnormvx();
                           var32 = var64.getY() - var69 * var5.getnormvy();
                           var45 = var75.getX2();
                           var47 = var75.getY2();
                        } else if (var62 < var67) {
                           var69 = Math.sqrt(Math.abs(var57 - var60));
                           var30 = var59.getX() - var69 * var5.getnormvx();
                           var32 = var59.getY() - var69 * var5.getnormvy();
                           var45 = var75.getX1();
                           var47 = var75.getY1();
                        } else {
                           var69 = Math.sqrt(Math.abs(var57 - var65));
                           var30 = var64.getX() - var69 * var5.getnormvx();
                           var32 = var64.getY() - var69 * var5.getnormvy();
                           var45 = var75.getX2();
                           var47 = var75.getY2();
                        }

                        double var71 = Math.pow(var30 - var5.getX(), 2.0D) + Math.pow(var32 - var5.getY(), 2.0D);
                        if (var71 <= var7[var11] || var7[var11] < 0.0D) {
                           CircleLineIntersect.RectVector var73 = null;
                           if (!var8[var11].collided() || var7[var11] != var71) {
                              for(int var74 = 0; var74 < var8[var11].size(); ++var74) {
                                 if (var8[var11].collided() && ((CircleLineIntersect.Collider)var8[var11].getColliders().get(var74)).getCollideobj() instanceof CircleLineIntersect.ForceCircle && var7[var11] > var71) {
                                    var6[((CircleLineIntersect.Collider)var8[var11].getColliders().get(var74)).getCollidewith()] = new CircleLineIntersect.PointVector((CircleLineIntersect.PointVector)var0.get(((CircleLineIntersect.Collider)var8[var11].getColliders().get(var74)).getCollidewith()));
                                    var7[((CircleLineIntersect.Collider)var8[var11].getColliders().get(var74)).getCollidewith()] = -1.0D;
                                 }
                              }
                           }

                           var73 = new CircleLineIntersect.RectVector(var30 - (var45 - var30) - var30, var32 - (var47 - var32) - var32);
                           var73 = (CircleLineIntersect.RectVector)var73.getUnitVector();
                           var73 = new CircleLineIntersect.RectVector(var73.getvx() * var5.getLength(), var73.getvy() * var5.getLength());
                           if (var7[var11] == -1.0D) {
                              var6[var11] = new CircleLineIntersect.PointVector(var30, var32);
                           } else {
                              var6[var11].setPoint(var30, var32);
                           }

                           if (var7[var11] == var71) {
                              var8[var11].addCollided(var75, var12, var73);
                           } else {
                              var8[var11].setCollided(var75, var12, var73);
                           }

                           var7[var11] = var71;
                        }
                     }
                  }
               }
            }
         }
      }

      ArrayList var76 = new ArrayList((int)Math.ceil((double)(var0.size() / 10)));

      for(var12 = 0; var12 < var6.length; ++var12) {
         if (var8[var12].collided()) {
            var5 = (CircleLineIntersect.ForceCircle)var0.get(var12);
            if (var5.isFrozen()) {
               var6[var12].setRect(0.0D, 0.0D);
            } else {
               double var77 = 0.0D;
               double var15 = 0.0D;
               boolean var17 = false;
               var18 = 0.0D;

               for(int var80 = 0; var80 < var8[var12].size(); ++var80) {
                  Object var79 = ((CircleLineIntersect.Collider)var8[var12].getColliders().get(var80)).getCollideobj();
                  var18 += ((CircleLineIntersect.ForceCircle)var0.get(var12)).getRestitution(((CircleLineIntersect.Collider)var8[var12].getColliders().get(var80)).getCollideobj());
                  if (var79 instanceof CircleLineIntersect.StaticLine && ((CircleLineIntersect.Collider)var8[var12].getColliders().get(var80)).getCollideforce() != null) {
                     var77 += ((CircleLineIntersect.Collider)var8[var12].getColliders().get(var80)).getCollideforce().getvx();
                     var15 += ((CircleLineIntersect.Collider)var8[var12].getColliders().get(var80)).getCollideforce().getvy();
                  }
               }

               var18 /= (double)var8[var12].getColliders().size();
               if (var7[var12] == -1.0D) {
                  var6[var12] = new CircleLineIntersect.PointVector(var6[var12].getX(), var6[var12].getY());
               }

               var6[var12].setRect(var77 * var18, var15 * var18);
               var76.add(var12);
               if (var9[var12] == 1.0D && ((CircleLineIntersect.ForceCircle)var0.get(var12)).getLength() != 0.0D && !var17) {
                  if (var7[var12] == 0.0D) {
                     var9[var12] = 0.0D;
                  } else if (var7[var12] > 0.0D) {
                     var9[var12] = Math.sqrt(var7[var12]) / ((CircleLineIntersect.ForceCircle)var0.get(var12)).getLength();
                  } else {
                     var9[var12] = ((CircleLineIntersect.ForceCircle)var0.get(var12)).distance(var6[var12]) / ((CircleLineIntersect.ForceCircle)var0.get(var12)).getLength();
                  }
               }

               var2[var12] += var9[var12] * (1.0D - var2[var12]);
               if (!var6[var12].equals(var0.get(var12))) {
                  var3[var12] = true;
               }
            }
         }
      }

      return new CircleLineIntersect.Collideresult(var6, var8, var76, var2, var9, var3);
   }

   public static CircleLineIntersect.Collideresult checkforcecirclescollide(List var0, ArrayList var1, double[] var2, boolean[] var3, boolean var4) {
      CircleLineIntersect.Collideresult var5 = checkforcecirclescollidetime(var0, var1, var2, var3, var4);
      new ArrayList();

      for(int var7 = var5.resultants.length - 1; var7 >= 0; --var7) {
         if (var5.collideinto[var7].collided()) {
            ((CircleLineIntersect.ForceCircle)var0.get(var7)).setPointVector(var5.resultants[var7]);
         }
      }

      return var5;
   }

   public static CircleLineIntersect.Collideresult checkforcecirclescollide(List var0, ArrayList var1) {
      double[] var2 = new double[var0.size()];
      boolean[] var3 = new boolean[var0.size()];

      for(int var4 = var0.size() - 1; var4 >= 0; --var4) {
         var2[var4] = 1.0D;
      }

      return checkforcecirclescollide(var0, var1, var2, var3, true);
   }

   public static boolean TEST(Vector3f var0, float var1, float var2, float var3, float var4, CarController var5) {
      Vector3f var6 = new Vector3f();
      var0.cross(new Vector3f(0.0F, 1.0F, 0.0F), var6);
      var0.x *= var4;
      var0.z *= var4;
      var6.x *= var3;
      var6.z *= var3;
      float var7 = var1 + var0.x;
      float var8 = var2 + var0.z;
      float var9 = var1 - var0.x;
      float var10 = var2 - var0.z;
      float var11 = var7 - var6.x / 2.0F;
      float var12 = var7 + var6.x / 2.0F;
      float var13 = var9 - var6.x / 2.0F;
      float var14 = var9 + var6.x / 2.0F;
      float var15 = var10 - var6.z / 2.0F;
      float var16 = var10 + var6.z / 2.0F;
      float var17 = var8 - var6.z / 2.0F;
      float var18 = var8 + var6.z / 2.0F;
      var11 += WorldSimulation.instance.offsetX;
      var17 += WorldSimulation.instance.offsetY;
      var12 += WorldSimulation.instance.offsetX;
      var18 += WorldSimulation.instance.offsetY;
      var13 += WorldSimulation.instance.offsetX;
      var15 += WorldSimulation.instance.offsetY;
      var14 += WorldSimulation.instance.offsetX;
      var16 += WorldSimulation.instance.offsetY;
      ArrayList var19 = new ArrayList();
      CircleLineIntersect.StaticLine var22;
      var19.add(var22 = new CircleLineIntersect.StaticLine((double)var11, (double)var17, (double)var12, (double)var18));
      CircleLineIntersect.StaticLine var21;
      var19.add(var21 = new CircleLineIntersect.StaticLine((double)var12, (double)var18, (double)var14, (double)var16));
      CircleLineIntersect.StaticLine var23;
      var19.add(var23 = new CircleLineIntersect.StaticLine((double)var14, (double)var16, (double)var13, (double)var15));
      CircleLineIntersect.StaticLine var20;
      var19.add(var20 = new CircleLineIntersect.StaticLine((double)var13, (double)var15, (double)var11, (double)var17));
      IsoPlayer var24 = IsoPlayer.getInstance();
      ArrayList var25 = new ArrayList();
      boolean var26 = true;
      CircleLineIntersect.ForceCircle var27 = new CircleLineIntersect.ForceCircle((double)var24.x, (double)var24.y, (double)(var24.nx - var24.x), (double)(var24.ny - var24.y), 0.295D);
      if (var5 != null) {
         var5.drawCircle((float)var27.getX2(), (float)var27.getY2(), 0.3F);
      }

      var25.add(var27);
      CircleLineIntersect.Collideresult var28 = checkforcecirclescollide(var25, var19);
      if (var5 != null) {
         var5.drawCircle((float)var27.getX(), (float)var27.getY(), (float)var27.getRadius());
      }

      if (var28.collidelist.isEmpty()) {
         return false;
      } else {
         int var30 = var28.collideinto.length;
         Vector2f var31 = new Vector2f(var24.nx - var24.x, var24.ny - var24.y);
         if (var31.length() > 0.0F) {
            var31.normalise();
         }

         for(int var32 = 0; var32 < var28.collideinto.length; ++var32) {
            CircleLineIntersect.StaticLine var33 = (CircleLineIntersect.StaticLine)((CircleLineIntersect.Collider)var28.collideinto[var32].getColliders().get(0)).getCollideobj();
            CircleLineIntersect.Point var34;
            double var35;
            if (var33 == var20 || var33 == var21) {
               LineDrawer.addLine(var7 + WorldSimulation.instance.offsetX, var8 + WorldSimulation.instance.offsetY, 0.0F, var9 + WorldSimulation.instance.offsetX, var10 + WorldSimulation.instance.offsetY, 0.0F, 1.0F, 1.0F, 1.0F, (String)null, true);
               var34 = CircleLineIntersect.VectorMath.closestpointonline((double)(var7 + WorldSimulation.instance.offsetX), (double)(var8 + WorldSimulation.instance.offsetY), (double)(var9 + WorldSimulation.instance.offsetX), (double)(var10 + WorldSimulation.instance.offsetY), var27.getX(), var27.getY());
               var0.set((float)(var34.x - (double)var24.x), (float)(var34.y - (double)var24.y), 0.0F);
               var0.normalize();
               var35 = CircleLineIntersect.VectorMath.dotproduct((double)var31.x, (double)var31.y, (double)var0.x, (double)var0.y);
               if (var35 < 0.0D) {
                  --var30;
               }
            }

            if (var33 == var22 || var33 == var23) {
               LineDrawer.addLine(var1 - var6.x / 2.0F + WorldSimulation.instance.offsetX, var2 - var6.z / 2.0F + WorldSimulation.instance.offsetY, 0.0F, var1 + var6.x / 2.0F + WorldSimulation.instance.offsetX, var2 + var6.z / 2.0F + WorldSimulation.instance.offsetY, 0.0F, 1.0F, 1.0F, 1.0F, (String)null, true);
               var34 = CircleLineIntersect.VectorMath.closestpointonline((double)(var1 - var6.x / 2.0F + WorldSimulation.instance.offsetX), (double)(var2 - var6.z / 2.0F + WorldSimulation.instance.offsetY), (double)(var1 + var6.x / 2.0F + WorldSimulation.instance.offsetX), (double)(var2 + var6.z / 2.0F + WorldSimulation.instance.offsetY), var27.getX(), var27.getY());
               var0.set((float)(var34.x - (double)var24.x), (float)(var34.y - (double)var24.y), 0.0F);
               var0.normalize();
               var35 = CircleLineIntersect.VectorMath.dotproduct((double)var31.x, (double)var31.y, (double)var0.x, (double)var0.y);
               if (var35 < 0.0D) {
                  --var30;
               }
            }
         }

         if (var30 == 0) {
            return false;
         } else {
            var0.set((float)var27.getX(), (float)var27.getY(), 0.0F);
            return true;
         }
      }
   }

   static class PointVector extends CircleLineIntersect.Point implements CircleLineIntersect.Vector {
      protected double vx;
      protected double vy;

      public PointVector(double var1, double var3) {
         this(var1, var3, 0.0D, 0.0D);
      }

      public PointVector(double var1, double var3, double var5, double var7) {
         super(var1, var3);
         this.vx = 0.0D;
         this.vy = 0.0D;
         this.vx = var5;
         this.vy = var7;
      }

      public PointVector(CircleLineIntersect.PointVector var1) {
         this(var1.getX(), var1.getY(), var1.getvx(), var1.getvy());
      }

      public double getLength() {
         return CircleLineIntersect.VectorMath.length(this.vx, this.vy);
      }

      public CircleLineIntersect.Vector getVector() {
         return new CircleLineIntersect.RectVector(this.vx, this.vy);
      }

      public double getvx() {
         return this.vx;
      }

      public double getvy() {
         return this.vy;
      }

      public double getX1() {
         return this.x;
      }

      public double getX2() {
         return this.x + this.vx;
      }

      public double getY1() {
         return this.y;
      }

      public double getY2() {
         return this.y + this.vy;
      }

      public void setRect(double var1, double var3) {
         this.vx = var1;
         this.vy = var3;
      }
   }

   static class Collideclassindex {
      private ArrayList colliders = new ArrayList(1);
      private int numforcecircles;

      public Collideclassindex() {
         this.numforcecircles = 0;
      }

      public Collideclassindex(Object var1, int var2, CircleLineIntersect.Vector var3) {
         this.colliders.add(new CircleLineIntersect.Collider(var1, var2, var3));
      }

      public boolean collided() {
         return this.size() > 0;
      }

      public void reset() {
         this.colliders.trimToSize();
         this.colliders.clear();
         this.numforcecircles = 0;
      }

      public void setCollided(Object var1, int var2, CircleLineIntersect.Vector var3) {
         if (this.size() > 0) {
            this.reset();
         }

         if (var1 instanceof CircleLineIntersect.ForceCircle && !((CircleLineIntersect.ForceCircle)var1).isFrozen()) {
            ++this.numforcecircles;
         }

         this.colliders.add(new CircleLineIntersect.Collider(var1, var2, var3));
      }

      public void addCollided(Object var1, int var2, CircleLineIntersect.Vector var3) {
         if (var1 instanceof CircleLineIntersect.ForceCircle && !((CircleLineIntersect.ForceCircle)var1).isFrozen()) {
            ++this.numforcecircles;
         }

         this.colliders.add(new CircleLineIntersect.Collider(var1, var2, var3));
      }

      public ArrayList getColliders() {
         return this.colliders;
      }

      public int getNumforcecircles() {
         return this.numforcecircles;
      }

      public CircleLineIntersect.Collider contains(Object var1) {
         Iterator var2 = this.colliders.iterator();

         CircleLineIntersect.Collider var3;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            var3 = (CircleLineIntersect.Collider)var2.next();
         } while(!var3.getCollideobj().equals(var1));

         return var3;
      }

      public int size() {
         return this.colliders.size();
      }

      public String toString() {
         String var1 = "";

         CircleLineIntersect.Collider var3;
         for(Iterator var2 = this.colliders.iterator(); var2.hasNext(); var1 = var1 + var3.toString() + "\n") {
            var3 = (CircleLineIntersect.Collider)var2.next();
         }

         return var1;
      }
   }

   static class ForceCircle extends CircleLineIntersect.Force {
      protected double radius;
      protected double radiussq;

      public ForceCircle(double var1, double var3, double var5, double var7, double var9) {
         super(var1, var3, var5, var7);
         this.radius = var9;
         this.radiussq = var9 * var9;
      }

      double getRadius() {
         return this.radius;
      }

      double getRadiusSq() {
         return this.radiussq;
      }
   }

   static class StaticLine extends CircleLineIntersect.Point {
      double x2;
      double y2;

      public StaticLine(double var1, double var3, double var5, double var7) {
         super(var1, var3);
         this.x2 = var5;
         this.y2 = var7;
      }

      public CircleLineIntersect.Point getP1() {
         return new CircleLineIntersect.Point(this.getX1(), this.getY1());
      }

      public CircleLineIntersect.Point getP2() {
         return new CircleLineIntersect.Point(this.getX2(), this.getY2());
      }

      public double getX1() {
         return this.x;
      }

      public double getX2() {
         return this.x2;
      }

      public double getY1() {
         return this.y;
      }

      public double getY2() {
         return this.y2;
      }
   }

   static class VectorMath {
      public static final CircleLineIntersect.Vector add(CircleLineIntersect.Vector var0, CircleLineIntersect.Vector var1) {
         return new CircleLineIntersect.RectVector(var0.getvx() + var1.getvx(), var0.getvy() + var1.getvy());
      }

      public static final CircleLineIntersect.Vector subtract(CircleLineIntersect.Vector var0, CircleLineIntersect.Vector var1) {
         return new CircleLineIntersect.RectVector(var0.getvx() - var1.getvx(), var0.getvy() - var1.getvy());
      }

      public static final double length(double var0, double var2) {
         return CircleLineIntersect.Point.distance(0.0D, 0.0D, var0, var2);
      }

      public static final double dotproduct(CircleLineIntersect.Vector var0, CircleLineIntersect.Vector var1) {
         return dotproduct(var0.getvx(), var0.getvy(), var1.getvx(), var1.getvy());
      }

      public static final double dotproduct(double var0, double var2, double var4, double var6) {
         return var0 * var4 + var2 * var6;
      }

      public static final double cosproj(CircleLineIntersect.Vector var0, CircleLineIntersect.Vector var1) {
         return dotproduct(var0, var1) / (var0.getLength() * var1.getLength());
      }

      public static final double cosproj(double var0, double var2, double var4, double var6) {
         return dotproduct(var0, var2, var4, var6) / (length(var0, var2) * length(var4, var6));
      }

      public static final double anglebetween(CircleLineIntersect.Vector var0, CircleLineIntersect.Vector var1) {
         return Math.acos(cosproj(var0, var1));
      }

      public static final double anglebetween(double var0, double var2, double var4, double var6) {
         return Math.acos(cosproj(var0, var2, var4, var6));
      }

      public static final double crossproduct(CircleLineIntersect.Vector var0, CircleLineIntersect.Vector var1) {
         return crossproduct(var0.getvx(), var0.getvy(), var1.getvx(), var1.getvy());
      }

      public static final double crossproduct(double var0, double var2, double var4, double var6) {
         return var0 * var6 - var2 * var4;
      }

      public static final double sinproj(CircleLineIntersect.Vector var0, CircleLineIntersect.Vector var1) {
         return crossproduct(var0, var1) / (var0.getLength() * var1.getLength());
      }

      public static final double sinproj(double var0, double var2, double var4, double var6) {
         return crossproduct(var0, var2, var4, var6) / (length(var0, var2) * length(var4, var6));
      }

      public static final boolean equaldirection(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14) {
         if (var0 - var4 == 0.0D && var2 - var6 == 0.0D) {
            return true;
         } else {
            double var16 = ((var0 - var4) * (var0 - var8) + (var2 - var6) * (var2 - var10)) / (Math.abs(var12) * Math.abs(var14));
            return var16 > 0.995D && var16 <= 1.0D;
         }
      }

      public static final boolean equaldirection(double var0, double var2, double var4, double var6, double var8, double var10, double var12, double var14, double var16) {
         if (var0 - var4 == 0.0D && var2 - var6 == 0.0D) {
            return true;
         } else {
            double var18 = ((var0 - var4) * (var0 - var8) + (var2 - var6) * (var2 - var10)) / (Math.abs(var12) * Math.abs(var14));
            return var18 > var16 && var18 <= 1.0D;
         }
      }

      public static final boolean equaldirection(double var0, double var2, double var4, double var6, double var8, double var10, double var12) {
         if (var0 == 0.0D && var2 == 0.0D) {
            return true;
         } else {
            double var14 = (var0 * var4 + var2 * var6) / (Math.abs(var8) * Math.abs(var10));
            return var14 > var12 && var14 <= 1.0D;
         }
      }

      public static final boolean equaldirection(double var0, double var2, double var4) {
         if (var0 > 6.283185307179586D) {
            var0 -= 6.283185307179586D;
         } else if (var0 < 0.0D) {
            var0 += 6.283185307179586D;
         }

         if (var2 > 6.283185307179586D) {
            var2 -= 6.283185307179586D;
         } else if (var2 < 0.0D) {
            var2 += 6.283185307179586D;
         }

         return Math.abs(var0 - var2) < var4;
      }

      public static final double linepointdistance(double var0, double var2, double var4, double var6, double var8, double var10) {
         CircleLineIntersect.Point var12 = closestpointonline(var0, var2, var4, var6, var8, var10);
         return CircleLineIntersect.Point.distance(var8, var10, var12.getX(), var12.getY());
      }

      public static final double linepointdistancesq(double var0, double var2, double var4, double var6, double var8, double var10) {
         double var12 = var6 - var2;
         double var14 = var0 - var4;
         double var16 = (var6 - var2) * var0 + (var0 - var4) * var2;
         double var18 = -var14 * var8 + var12 * var10;
         double var20 = var12 * var12 - -var14 * var14;
         double var22 = 0.0D;
         double var24 = 0.0D;
         if (var20 != 0.0D) {
            var22 = (var12 * var16 - var14 * var18) / var20;
            var24 = (var12 * var18 - -var14 * var16) / var20;
         }

         return Math.abs((var22 - var8) * (var22 - var8) + (var24 - var10) * (var24 - var10));
      }

      public static final CircleLineIntersect.Point closestpointonline(CircleLineIntersect.StaticLine var0, CircleLineIntersect.Point var1) {
         return closestpointonline(var0.getX(), var0.getY(), var0.getX2(), var0.getY2(), var1.getX(), var1.getY());
      }

      public static final CircleLineIntersect.Point closestpointonline(double var0, double var2, double var4, double var6, double var8, double var10) {
         double var12 = var6 - var2;
         double var14 = var0 - var4;
         double var16 = (var6 - var2) * var0 + (var0 - var4) * var2;
         double var18 = -var14 * var8 + var12 * var10;
         double var20 = var12 * var12 - -var14 * var14;
         double var22 = 0.0D;
         double var24 = 0.0D;
         if (var20 != 0.0D) {
            var22 = (var12 * var16 - var14 * var18) / var20;
            var24 = (var12 * var18 - -var14 * var16) / var20;
         } else {
            var22 = var8;
            var24 = var10;
         }

         return new CircleLineIntersect.Point(var22, var24);
      }

      public static final CircleLineIntersect.Vector getVector(CircleLineIntersect.Point var0, CircleLineIntersect.Point var1) {
         return new CircleLineIntersect.RectVector(var0.getX() - var1.getX(), var0.getY() - var1.getY());
      }

      public static final CircleLineIntersect.Vector rotate(CircleLineIntersect.Vector var0, double var1) {
         return new CircleLineIntersect.RectVector(var0.getvx() * Math.cos(var1) - var0.getvy() * Math.sin(var1), var0.getvx() * Math.sin(var1) + var0.getvy() * Math.cos(var1));
      }
   }

   static class Point {
      double x;
      double y;

      public static final CircleLineIntersect.Point midpoint(double var0, double var2, double var4, double var6) {
         return new CircleLineIntersect.Point((var0 + var4) / 2.0D, (var2 + var6) / 2.0D);
      }

      public static final CircleLineIntersect.Point midpoint(CircleLineIntersect.Point var0, CircleLineIntersect.Point var1) {
         return midpoint(var0.getX(), var0.getY(), var1.getX(), var1.getY());
      }

      public Point(double var1, double var3) {
         if (Double.isNaN(var1) || Double.isInfinite(var1)) {
            var1 = 0.0D;
         }

         if (Double.isNaN(var3) || Double.isInfinite(var3)) {
            var3 = 0.0D;
         }

         this.x = var1;
         this.y = var3;
      }

      public double getX() {
         return this.x;
      }

      public double getY() {
         return this.y;
      }

      public void setPoint(double var1, double var3) {
         this.x = var1;
         this.y = var3;
      }

      public static double distanceSq(double var0, double var2, double var4, double var6) {
         var0 -= var4;
         var2 -= var6;
         return var0 * var0 + var2 * var2;
      }

      public static double distance(double var0, double var2, double var4, double var6) {
         var0 -= var4;
         var2 -= var6;
         return Math.sqrt(var0 * var0 + var2 * var2);
      }

      public double distanceSq(double var1, double var3) {
         var1 -= this.getX();
         var3 -= this.getY();
         return var1 * var1 + var3 * var3;
      }

      public double distanceSq(CircleLineIntersect.Point var1) {
         double var2 = var1.getX() - this.getX();
         double var4 = var1.getY() - this.getY();
         return var2 * var2 + var4 * var4;
      }

      public double distance(CircleLineIntersect.Point var1) {
         double var2 = var1.getX() - this.getX();
         double var4 = var1.getY() - this.getY();
         return Math.sqrt(var2 * var2 + var4 * var4);
      }
   }

   interface Vector {
      double getvx();

      double getvy();

      double getLength();
   }

   static class Collider {
      private Object collideobj;
      private Integer collideindex;
      private CircleLineIntersect.Vector collideforce;

      public Collider(CircleLineIntersect.Vector var1, Integer var2) {
         this.collideobj = var1;
         this.collideindex = var2;
         this.collideforce = var1;
      }

      public Collider(Object var1, Integer var2, CircleLineIntersect.Vector var3) {
         this.collideobj = var1;
         this.collideindex = var2;
         this.collideforce = var3;
      }

      public Object getCollideobj() {
         return this.collideobj;
      }

      public Integer getCollidewith() {
         return this.collideindex;
      }

      public CircleLineIntersect.Vector getCollideforce() {
         return this.collideforce;
      }

      public void setCollideforce(CircleLineIntersect.Vector var1) {
         this.collideforce = var1;
      }

      public String toString() {
         String var10000 = this.collideobj.getClass().getSimpleName();
         return var10000 + " @ " + this.collideindex + " hit with " + this.collideforce.toString();
      }
   }

   static class RectVector implements CircleLineIntersect.Vector {
      private double vx;
      private double vy;

      public RectVector(double var1, double var3) {
         this.vx = var1;
         this.vy = var3;
      }

      public RectVector(CircleLineIntersect.Vector var1) {
         this.setVector(var1);
      }

      public double getLength() {
         return Math.sqrt(Math.abs(this.getvx() * this.getvx() + this.getvy() * this.getvy()));
      }

      public CircleLineIntersect.Vector getUnitVector() {
         double var1 = this.getLength();
         return new CircleLineIntersect.RectVector(this.getvx() / var1, this.getvy() / var1);
      }

      public double getvx() {
         return this.vx;
      }

      public double getvy() {
         return this.vy;
      }

      public void setVector(CircleLineIntersect.Vector var1) {
         this.vx = var1.getvx();
         this.vy = var1.getvy();
      }
   }

   static class Collideresult {
      protected CircleLineIntersect.PointVector[] resultants;
      protected ArrayList collidelist;
      protected CircleLineIntersect.Collideclassindex[] collideinto;
      protected double[] timepassed;
      protected double[] collidetime;
      protected boolean[] modified;

      public Collideresult(CircleLineIntersect.PointVector[] var1, CircleLineIntersect.Collideclassindex[] var2, ArrayList var3, double[] var4, double[] var5, boolean[] var6) {
         this.resultants = var1;
         this.collideinto = var2;
         this.collidelist = var3;
         this.timepassed = var4;
         this.collidetime = var5;
         this.modified = var6;
      }

      public String toString() {
         return this.collidelist.toString();
      }
   }

   static class Force extends CircleLineIntersect.PointVector {
      protected double length;
      protected double mass;

      public Force(double var1, double var3, double var5, double var7) {
         super(var1, var3, var5, var7);
         this.length = CircleLineIntersect.VectorMath.length(var5, var7);
      }

      public double getLength() {
         return this.length;
      }

      public double getnormvx() {
         return this.length > 0.0D ? this.vx / this.length : 0.0D;
      }

      public double getnormvy() {
         return this.length > 0.0D ? this.vy / this.length : 0.0D;
      }

      public double getRestitution(Object var1) {
         return 1.0D;
      }

      public void setPointVector(CircleLineIntersect.PointVector var1) {
         this.x = var1.getX();
         this.y = var1.getY();
         if (!this.isFrozen() && (this.vx != var1.getvx() || this.vy != var1.getvy())) {
            this.vx = var1.getvx();
            this.vy = var1.getvy();
            this.length = CircleLineIntersect.VectorMath.length(this.vx, this.vy);
         }

      }

      boolean isFrozen() {
         return false;
      }
   }
}
