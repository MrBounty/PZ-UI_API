package org.lwjglx.util.glu.tessellation;

class Sweep {
   private static final boolean TOLERANCE_NONZERO = false;
   private static final double SENTINEL_COORD = 4.0E150D;

   private Sweep() {
   }

   private static void DebugEvent(GLUtessellatorImpl var0) {
   }

   private static void AddWinding(GLUhalfEdge var0, GLUhalfEdge var1) {
      var0.winding += var1.winding;
      GLUhalfEdge var10000 = var0.Sym;
      var10000.winding += var1.Sym.winding;
   }

   private static ActiveRegion RegionBelow(ActiveRegion var0) {
      return (ActiveRegion)Dict.dictKey(Dict.dictPred(var0.nodeUp));
   }

   private static ActiveRegion RegionAbove(ActiveRegion var0) {
      return (ActiveRegion)Dict.dictKey(Dict.dictSucc(var0.nodeUp));
   }

   static boolean EdgeLeq(GLUtessellatorImpl var0, ActiveRegion var1, ActiveRegion var2) {
      GLUvertex var3 = var0.event;
      GLUhalfEdge var4 = var1.eUp;
      GLUhalfEdge var5 = var2.eUp;
      if (var4.Sym.Org == var3) {
         if (var5.Sym.Org == var3) {
            if (Geom.VertLeq(var4.Org, var5.Org)) {
               return Geom.EdgeSign(var5.Sym.Org, var4.Org, var5.Org) <= 0.0D;
            } else {
               return Geom.EdgeSign(var4.Sym.Org, var5.Org, var4.Org) >= 0.0D;
            }
         } else {
            return Geom.EdgeSign(var5.Sym.Org, var3, var5.Org) <= 0.0D;
         }
      } else if (var5.Sym.Org == var3) {
         return Geom.EdgeSign(var4.Sym.Org, var3, var4.Org) >= 0.0D;
      } else {
         double var6 = Geom.EdgeEval(var4.Sym.Org, var3, var4.Org);
         double var8 = Geom.EdgeEval(var5.Sym.Org, var3, var5.Org);
         return var6 >= var8;
      }
   }

   static void DeleteRegion(GLUtessellatorImpl var0, ActiveRegion var1) {
      assert !var1.fixUpperEdge || var1.eUp.winding == 0;

      var1.eUp.activeRegion = null;
      Dict.dictDelete(var0.dict, var1.nodeUp);
   }

   static boolean FixUpperEdge(ActiveRegion var0, GLUhalfEdge var1) {
      assert var0.fixUpperEdge;

      if (!Mesh.__gl_meshDelete(var0.eUp)) {
         return false;
      } else {
         var0.fixUpperEdge = false;
         var0.eUp = var1;
         var1.activeRegion = var0;
         return true;
      }
   }

   static ActiveRegion TopLeftRegion(ActiveRegion var0) {
      GLUvertex var1 = var0.eUp.Org;

      do {
         var0 = RegionAbove(var0);
      } while(var0.eUp.Org == var1);

      if (var0.fixUpperEdge) {
         GLUhalfEdge var2 = Mesh.__gl_meshConnect(RegionBelow(var0).eUp.Sym, var0.eUp.Lnext);
         if (var2 == null) {
            return null;
         }

         if (!FixUpperEdge(var0, var2)) {
            return null;
         }

         var0 = RegionAbove(var0);
      }

      return var0;
   }

   static ActiveRegion TopRightRegion(ActiveRegion var0) {
      GLUvertex var1 = var0.eUp.Sym.Org;

      do {
         var0 = RegionAbove(var0);
      } while(var0.eUp.Sym.Org == var1);

      return var0;
   }

   static ActiveRegion AddRegionBelow(GLUtessellatorImpl var0, ActiveRegion var1, GLUhalfEdge var2) {
      ActiveRegion var3 = new ActiveRegion();
      var3.eUp = var2;
      var3.nodeUp = Dict.dictInsertBefore(var0.dict, var1.nodeUp, var3);
      if (var3.nodeUp == null) {
         throw new RuntimeException();
      } else {
         var3.fixUpperEdge = false;
         var3.sentinel = false;
         var3.dirty = false;
         var2.activeRegion = var3;
         return var3;
      }
   }

   static boolean IsWindingInside(GLUtessellatorImpl var0, int var1) {
      switch(var0.windingRule) {
      case 100130:
         return (var1 & 1) != 0;
      case 100131:
         return var1 != 0;
      case 100132:
         return var1 > 0;
      case 100133:
         return var1 < 0;
      case 100134:
         return var1 >= 2 || var1 <= -2;
      default:
         throw new InternalError();
      }
   }

   static void ComputeWinding(GLUtessellatorImpl var0, ActiveRegion var1) {
      var1.windingNumber = RegionAbove(var1).windingNumber + var1.eUp.winding;
      var1.inside = IsWindingInside(var0, var1.windingNumber);
   }

   static void FinishRegion(GLUtessellatorImpl var0, ActiveRegion var1) {
      GLUhalfEdge var2 = var1.eUp;
      GLUface var3 = var2.Lface;
      var3.inside = var1.inside;
      var3.anEdge = var2;
      DeleteRegion(var0, var1);
   }

   static GLUhalfEdge FinishLeftRegions(GLUtessellatorImpl var0, ActiveRegion var1, ActiveRegion var2) {
      ActiveRegion var4 = var1;

      ActiveRegion var3;
      GLUhalfEdge var6;
      for(var6 = var1.eUp; var4 != var2; var4 = var3) {
         var4.fixUpperEdge = false;
         var3 = RegionBelow(var4);
         GLUhalfEdge var5 = var3.eUp;
         if (var5.Org != var6.Org) {
            if (!var3.fixUpperEdge) {
               FinishRegion(var0, var4);
               break;
            }

            var5 = Mesh.__gl_meshConnect(var6.Onext.Sym, var5.Sym);
            if (var5 == null) {
               throw new RuntimeException();
            }

            if (!FixUpperEdge(var3, var5)) {
               throw new RuntimeException();
            }
         }

         if (var6.Onext != var5) {
            if (!Mesh.__gl_meshSplice(var5.Sym.Lnext, var5)) {
               throw new RuntimeException();
            }

            if (!Mesh.__gl_meshSplice(var6, var5)) {
               throw new RuntimeException();
            }
         }

         FinishRegion(var0, var4);
         var6 = var3.eUp;
      }

      return var6;
   }

   static void AddRightEdges(GLUtessellatorImpl var0, ActiveRegion var1, GLUhalfEdge var2, GLUhalfEdge var3, GLUhalfEdge var4, boolean var5) {
      boolean var10 = true;
      GLUhalfEdge var8 = var2;

      do {
         assert Geom.VertLeq(var8.Org, var8.Sym.Org);

         AddRegionBelow(var0, var1, var8.Sym);
         var8 = var8.Onext;
      } while(var8 != var3);

      if (var4 == null) {
         var4 = RegionBelow(var1).eUp.Sym.Onext;
      }

      ActiveRegion var7 = var1;
      GLUhalfEdge var9 = var4;

      while(true) {
         ActiveRegion var6 = RegionBelow(var7);
         var8 = var6.eUp.Sym;
         if (var8.Org != var9.Org) {
            var7.dirty = true;

            assert var7.windingNumber - var8.winding == var6.windingNumber;

            if (var5) {
               WalkDirtyRegions(var0, var7);
            }

            return;
         }

         if (var8.Onext != var9) {
            if (!Mesh.__gl_meshSplice(var8.Sym.Lnext, var8)) {
               throw new RuntimeException();
            }

            if (!Mesh.__gl_meshSplice(var9.Sym.Lnext, var8)) {
               throw new RuntimeException();
            }
         }

         var6.windingNumber = var7.windingNumber - var8.winding;
         var6.inside = IsWindingInside(var0, var6.windingNumber);
         var7.dirty = true;
         if (!var10 && CheckForRightSplice(var0, var7)) {
            AddWinding(var8, var9);
            DeleteRegion(var0, var7);
            if (!Mesh.__gl_meshDelete(var9)) {
               throw new RuntimeException();
            }
         }

         var10 = false;
         var7 = var6;
         var9 = var8;
      }
   }

   static void CallCombine(GLUtessellatorImpl var0, GLUvertex var1, Object[] var2, float[] var3, boolean var4) {
      double[] var5 = new double[]{var1.coords[0], var1.coords[1], var1.coords[2]};
      Object[] var6 = new Object[1];
      var0.callCombineOrCombineData(var5, var2, var3, var6);
      var1.data = var6[0];
      if (var1.data == null) {
         if (!var4) {
            var1.data = var2[0];
         } else if (!var0.fatalError) {
            var0.callErrorOrErrorData(100156);
            var0.fatalError = true;
         }
      }

   }

   static void SpliceMergeVertices(GLUtessellatorImpl var0, GLUhalfEdge var1, GLUhalfEdge var2) {
      Object[] var3 = new Object[4];
      float[] var4 = new float[]{0.5F, 0.5F, 0.0F, 0.0F};
      var3[0] = var1.Org.data;
      var3[1] = var2.Org.data;
      CallCombine(var0, var1.Org, var3, var4, false);
      if (!Mesh.__gl_meshSplice(var1, var2)) {
         throw new RuntimeException();
      }
   }

   static void VertexWeights(GLUvertex var0, GLUvertex var1, GLUvertex var2, float[] var3) {
      double var4 = Geom.VertL1dist(var1, var0);
      double var6 = Geom.VertL1dist(var2, var0);
      var3[0] = (float)(0.5D * var6 / (var4 + var6));
      var3[1] = (float)(0.5D * var4 / (var4 + var6));
      double[] var10000 = var0.coords;
      var10000[0] += (double)var3[0] * var1.coords[0] + (double)var3[1] * var2.coords[0];
      var10000 = var0.coords;
      var10000[1] += (double)var3[0] * var1.coords[1] + (double)var3[1] * var2.coords[1];
      var10000 = var0.coords;
      var10000[2] += (double)var3[0] * var1.coords[2] + (double)var3[1] * var2.coords[2];
   }

   static void GetIntersectData(GLUtessellatorImpl var0, GLUvertex var1, GLUvertex var2, GLUvertex var3, GLUvertex var4, GLUvertex var5) {
      Object[] var6 = new Object[4];
      float[] var7 = new float[4];
      float[] var8 = new float[2];
      float[] var9 = new float[2];
      var6[0] = var2.data;
      var6[1] = var3.data;
      var6[2] = var4.data;
      var6[3] = var5.data;
      var1.coords[0] = var1.coords[1] = var1.coords[2] = 0.0D;
      VertexWeights(var1, var2, var3, var8);
      VertexWeights(var1, var4, var5, var9);
      System.arraycopy(var8, 0, var7, 0, 2);
      System.arraycopy(var9, 0, var7, 2, 2);
      CallCombine(var0, var1, var6, var7, true);
   }

   static boolean CheckForRightSplice(GLUtessellatorImpl var0, ActiveRegion var1) {
      ActiveRegion var2 = RegionBelow(var1);
      GLUhalfEdge var3 = var1.eUp;
      GLUhalfEdge var4 = var2.eUp;
      if (Geom.VertLeq(var3.Org, var4.Org)) {
         if (Geom.EdgeSign(var4.Sym.Org, var3.Org, var4.Org) > 0.0D) {
            return false;
         }

         if (!Geom.VertEq(var3.Org, var4.Org)) {
            if (Mesh.__gl_meshSplitEdge(var4.Sym) == null) {
               throw new RuntimeException();
            }

            if (!Mesh.__gl_meshSplice(var3, var4.Sym.Lnext)) {
               throw new RuntimeException();
            }

            var1.dirty = var2.dirty = true;
         } else if (var3.Org != var4.Org) {
            var0.pq.pqDelete(var3.Org.pqHandle);
            SpliceMergeVertices(var0, var4.Sym.Lnext, var3);
         }
      } else {
         if (Geom.EdgeSign(var3.Sym.Org, var4.Org, var3.Org) < 0.0D) {
            return false;
         }

         RegionAbove(var1).dirty = var1.dirty = true;
         if (Mesh.__gl_meshSplitEdge(var3.Sym) == null) {
            throw new RuntimeException();
         }

         if (!Mesh.__gl_meshSplice(var4.Sym.Lnext, var3)) {
            throw new RuntimeException();
         }
      }

      return true;
   }

   static boolean CheckForLeftSplice(GLUtessellatorImpl var0, ActiveRegion var1) {
      ActiveRegion var2 = RegionBelow(var1);
      GLUhalfEdge var3 = var1.eUp;
      GLUhalfEdge var4 = var2.eUp;

      assert !Geom.VertEq(var3.Sym.Org, var4.Sym.Org);

      GLUhalfEdge var5;
      if (Geom.VertLeq(var3.Sym.Org, var4.Sym.Org)) {
         if (Geom.EdgeSign(var3.Sym.Org, var4.Sym.Org, var3.Org) < 0.0D) {
            return false;
         }

         RegionAbove(var1).dirty = var1.dirty = true;
         var5 = Mesh.__gl_meshSplitEdge(var3);
         if (var5 == null) {
            throw new RuntimeException();
         }

         if (!Mesh.__gl_meshSplice(var4.Sym, var5)) {
            throw new RuntimeException();
         }

         var5.Lface.inside = var1.inside;
      } else {
         if (Geom.EdgeSign(var4.Sym.Org, var3.Sym.Org, var4.Org) > 0.0D) {
            return false;
         }

         var1.dirty = var2.dirty = true;
         var5 = Mesh.__gl_meshSplitEdge(var4);
         if (var5 == null) {
            throw new RuntimeException();
         }

         if (!Mesh.__gl_meshSplice(var3.Lnext, var4.Sym)) {
            throw new RuntimeException();
         }

         var5.Sym.Lface.inside = var1.inside;
      }

      return true;
   }

   static boolean CheckForIntersect(GLUtessellatorImpl var0, ActiveRegion var1) {
      ActiveRegion var2 = RegionBelow(var1);
      GLUhalfEdge var3 = var1.eUp;
      GLUhalfEdge var4 = var2.eUp;
      GLUvertex var5 = var3.Org;
      GLUvertex var6 = var4.Org;
      GLUvertex var7 = var3.Sym.Org;
      GLUvertex var8 = var4.Sym.Org;
      GLUvertex var13 = new GLUvertex();

      assert !Geom.VertEq(var8, var7);

      assert Geom.EdgeSign(var7, var0.event, var5) <= 0.0D;

      assert Geom.EdgeSign(var8, var0.event, var6) >= 0.0D;

      assert var5 != var0.event && var6 != var0.event;

      assert !var1.fixUpperEdge && !var2.fixUpperEdge;

      if (var5 == var6) {
         return false;
      } else {
         double var9 = Math.min(var5.t, var7.t);
         double var11 = Math.max(var6.t, var8.t);
         if (var9 > var11) {
            return false;
         } else {
            if (Geom.VertLeq(var5, var6)) {
               if (Geom.EdgeSign(var8, var5, var6) > 0.0D) {
                  return false;
               }
            } else if (Geom.EdgeSign(var7, var6, var5) < 0.0D) {
               return false;
            }

            DebugEvent(var0);
            Geom.EdgeIntersect(var7, var5, var8, var6, var13);

            assert Math.min(var5.t, var7.t) <= var13.t;

            assert var13.t <= Math.max(var6.t, var8.t);

            assert Math.min(var8.s, var7.s) <= var13.s;

            assert var13.s <= Math.max(var6.s, var5.s);

            if (Geom.VertLeq(var13, var0.event)) {
               var13.s = var0.event.s;
               var13.t = var0.event.t;
            }

            GLUvertex var14 = Geom.VertLeq(var5, var6) ? var5 : var6;
            if (Geom.VertLeq(var14, var13)) {
               var13.s = var14.s;
               var13.t = var14.t;
            }

            if (!Geom.VertEq(var13, var5) && !Geom.VertEq(var13, var6)) {
               if (!Geom.VertEq(var7, var0.event) && Geom.EdgeSign(var7, var0.event, var13) >= 0.0D || !Geom.VertEq(var8, var0.event) && Geom.EdgeSign(var8, var0.event, var13) <= 0.0D) {
                  if (var8 == var0.event) {
                     if (Mesh.__gl_meshSplitEdge(var3.Sym) == null) {
                        throw new RuntimeException();
                     } else if (!Mesh.__gl_meshSplice(var4.Sym, var3)) {
                        throw new RuntimeException();
                     } else {
                        var1 = TopLeftRegion(var1);
                        if (var1 == null) {
                           throw new RuntimeException();
                        } else {
                           var3 = RegionBelow(var1).eUp;
                           FinishLeftRegions(var0, RegionBelow(var1), var2);
                           AddRightEdges(var0, var1, var3.Sym.Lnext, var3, var3, true);
                           return true;
                        }
                     }
                  } else if (var7 == var0.event) {
                     if (Mesh.__gl_meshSplitEdge(var4.Sym) == null) {
                        throw new RuntimeException();
                     } else if (!Mesh.__gl_meshSplice(var3.Lnext, var4.Sym.Lnext)) {
                        throw new RuntimeException();
                     } else {
                        var2 = var1;
                        var1 = TopRightRegion(var1);
                        GLUhalfEdge var15 = RegionBelow(var1).eUp.Sym.Onext;
                        var2.eUp = var4.Sym.Lnext;
                        var4 = FinishLeftRegions(var0, var2, (ActiveRegion)null);
                        AddRightEdges(var0, var1, var4.Onext, var3.Sym.Onext, var15, true);
                        return true;
                     }
                  } else {
                     if (Geom.EdgeSign(var7, var0.event, var13) >= 0.0D) {
                        RegionAbove(var1).dirty = var1.dirty = true;
                        if (Mesh.__gl_meshSplitEdge(var3.Sym) == null) {
                           throw new RuntimeException();
                        }

                        var3.Org.s = var0.event.s;
                        var3.Org.t = var0.event.t;
                     }

                     if (Geom.EdgeSign(var8, var0.event, var13) <= 0.0D) {
                        var1.dirty = var2.dirty = true;
                        if (Mesh.__gl_meshSplitEdge(var4.Sym) == null) {
                           throw new RuntimeException();
                        }

                        var4.Org.s = var0.event.s;
                        var4.Org.t = var0.event.t;
                     }

                     return false;
                  }
               } else if (Mesh.__gl_meshSplitEdge(var3.Sym) == null) {
                  throw new RuntimeException();
               } else if (Mesh.__gl_meshSplitEdge(var4.Sym) == null) {
                  throw new RuntimeException();
               } else if (!Mesh.__gl_meshSplice(var4.Sym.Lnext, var3)) {
                  throw new RuntimeException();
               } else {
                  var3.Org.s = var13.s;
                  var3.Org.t = var13.t;
                  var3.Org.pqHandle = var0.pq.pqInsert(var3.Org);
                  if ((long)var3.Org.pqHandle == Long.MAX_VALUE) {
                     var0.pq.pqDeletePriorityQ();
                     var0.pq = null;
                     throw new RuntimeException();
                  } else {
                     GetIntersectData(var0, var3.Org, var5, var7, var6, var8);
                     RegionAbove(var1).dirty = var1.dirty = var2.dirty = true;
                     return false;
                  }
               }
            } else {
               CheckForRightSplice(var0, var1);
               return false;
            }
         }
      }
   }

   static void WalkDirtyRegions(GLUtessellatorImpl var0, ActiveRegion var1) {
      ActiveRegion var2 = RegionBelow(var1);

      while(true) {
         while(var2.dirty) {
            var1 = var2;
            var2 = RegionBelow(var2);
         }

         if (!var1.dirty) {
            var2 = var1;
            var1 = RegionAbove(var1);
            if (var1 == null || !var1.dirty) {
               return;
            }
         }

         var1.dirty = false;
         GLUhalfEdge var3 = var1.eUp;
         GLUhalfEdge var4 = var2.eUp;
         if (var3.Sym.Org != var4.Sym.Org && CheckForLeftSplice(var0, var1)) {
            if (var2.fixUpperEdge) {
               DeleteRegion(var0, var2);
               if (!Mesh.__gl_meshDelete(var4)) {
                  throw new RuntimeException();
               }

               var2 = RegionBelow(var1);
               var4 = var2.eUp;
            } else if (var1.fixUpperEdge) {
               DeleteRegion(var0, var1);
               if (!Mesh.__gl_meshDelete(var3)) {
                  throw new RuntimeException();
               }

               var1 = RegionAbove(var2);
               var3 = var1.eUp;
            }
         }

         if (var3.Org != var4.Org) {
            if (var3.Sym.Org == var4.Sym.Org || var1.fixUpperEdge || var2.fixUpperEdge || var3.Sym.Org != var0.event && var4.Sym.Org != var0.event) {
               CheckForRightSplice(var0, var1);
            } else if (CheckForIntersect(var0, var1)) {
               return;
            }
         }

         if (var3.Org == var4.Org && var3.Sym.Org == var4.Sym.Org) {
            AddWinding(var4, var3);
            DeleteRegion(var0, var1);
            if (!Mesh.__gl_meshDelete(var3)) {
               throw new RuntimeException();
            }

            var1 = RegionAbove(var2);
         }
      }
   }

   static void ConnectRightVertex(GLUtessellatorImpl var0, ActiveRegion var1, GLUhalfEdge var2) {
      GLUhalfEdge var4 = var2.Onext;
      ActiveRegion var5 = RegionBelow(var1);
      GLUhalfEdge var6 = var1.eUp;
      GLUhalfEdge var7 = var5.eUp;
      boolean var8 = false;
      if (var6.Sym.Org != var7.Sym.Org) {
         CheckForIntersect(var0, var1);
      }

      if (Geom.VertEq(var6.Org, var0.event)) {
         if (!Mesh.__gl_meshSplice(var4.Sym.Lnext, var6)) {
            throw new RuntimeException();
         }

         var1 = TopLeftRegion(var1);
         if (var1 == null) {
            throw new RuntimeException();
         }

         var4 = RegionBelow(var1).eUp;
         FinishLeftRegions(var0, RegionBelow(var1), var5);
         var8 = true;
      }

      if (Geom.VertEq(var7.Org, var0.event)) {
         if (!Mesh.__gl_meshSplice(var2, var7.Sym.Lnext)) {
            throw new RuntimeException();
         }

         var2 = FinishLeftRegions(var0, var5, (ActiveRegion)null);
         var8 = true;
      }

      if (var8) {
         AddRightEdges(var0, var1, var2.Onext, var4, var4, true);
      } else {
         GLUhalfEdge var3;
         if (Geom.VertLeq(var7.Org, var6.Org)) {
            var3 = var7.Sym.Lnext;
         } else {
            var3 = var6;
         }

         var3 = Mesh.__gl_meshConnect(var2.Onext.Sym, var3);
         if (var3 == null) {
            throw new RuntimeException();
         } else {
            AddRightEdges(var0, var1, var3, var3.Onext, var3.Onext, false);
            var3.Sym.activeRegion.fixUpperEdge = true;
            WalkDirtyRegions(var0, var1);
         }
      }
   }

   static void ConnectLeftDegenerate(GLUtessellatorImpl var0, ActiveRegion var1, GLUvertex var2) {
      GLUhalfEdge var3 = var1.eUp;
      if (Geom.VertEq(var3.Org, var2)) {
         assert false;

         SpliceMergeVertices(var0, var3, var2.anEdge);
      } else if (!Geom.VertEq(var3.Sym.Org, var2)) {
         if (Mesh.__gl_meshSplitEdge(var3.Sym) == null) {
            throw new RuntimeException();
         } else {
            if (var1.fixUpperEdge) {
               if (!Mesh.__gl_meshDelete(var3.Onext)) {
                  throw new RuntimeException();
               }

               var1.fixUpperEdge = false;
            }

            if (!Mesh.__gl_meshSplice(var2.anEdge, var3)) {
               throw new RuntimeException();
            } else {
               SweepEvent(var0, var2);
            }
         }
      } else {
         assert false;

         var1 = TopRightRegion(var1);
         ActiveRegion var7 = RegionBelow(var1);
         GLUhalfEdge var5 = var7.eUp.Sym;
         GLUhalfEdge var6;
         GLUhalfEdge var4 = var6 = var5.Onext;
         if (var7.fixUpperEdge) {
            assert var4 != var5;

            DeleteRegion(var0, var7);
            if (!Mesh.__gl_meshDelete(var5)) {
               throw new RuntimeException();
            }

            var5 = var4.Sym.Lnext;
         }

         if (!Mesh.__gl_meshSplice(var2.anEdge, var5)) {
            throw new RuntimeException();
         } else {
            if (!Geom.EdgeGoesLeft(var4)) {
               var4 = null;
            }

            AddRightEdges(var0, var1, var5.Onext, var6, var4, true);
         }
      }
   }

   static void ConnectLeftVertex(GLUtessellatorImpl var0, GLUvertex var1) {
      ActiveRegion var8 = new ActiveRegion();
      var8.eUp = var1.anEdge.Sym;
      ActiveRegion var2 = (ActiveRegion)Dict.dictKey(Dict.dictSearch(var0.dict, var8));
      ActiveRegion var3 = RegionBelow(var2);
      GLUhalfEdge var5 = var2.eUp;
      GLUhalfEdge var6 = var3.eUp;
      if (Geom.EdgeSign(var5.Sym.Org, var1, var5.Org) == 0.0D) {
         ConnectLeftDegenerate(var0, var2, var1);
      } else {
         ActiveRegion var4 = Geom.VertLeq(var6.Sym.Org, var5.Sym.Org) ? var2 : var3;
         if (!var2.inside && !var4.fixUpperEdge) {
            AddRightEdges(var0, var2, var1.anEdge, var1.anEdge, (GLUhalfEdge)null, true);
         } else {
            GLUhalfEdge var7;
            if (var4 == var2) {
               var7 = Mesh.__gl_meshConnect(var1.anEdge.Sym, var5.Lnext);
               if (var7 == null) {
                  throw new RuntimeException();
               }
            } else {
               GLUhalfEdge var9 = Mesh.__gl_meshConnect(var6.Sym.Onext.Sym, var1.anEdge);
               if (var9 == null) {
                  throw new RuntimeException();
               }

               var7 = var9.Sym;
            }

            if (var4.fixUpperEdge) {
               if (!FixUpperEdge(var4, var7)) {
                  throw new RuntimeException();
               }
            } else {
               ComputeWinding(var0, AddRegionBelow(var0, var2, var7));
            }

            SweepEvent(var0, var1);
         }

      }
   }

   static void SweepEvent(GLUtessellatorImpl var0, GLUvertex var1) {
      var0.event = var1;
      DebugEvent(var0);
      GLUhalfEdge var4 = var1.anEdge;

      do {
         if (var4.activeRegion != null) {
            ActiveRegion var2 = TopLeftRegion(var4.activeRegion);
            if (var2 == null) {
               throw new RuntimeException();
            }

            ActiveRegion var3 = RegionBelow(var2);
            GLUhalfEdge var5 = var3.eUp;
            GLUhalfEdge var6 = FinishLeftRegions(var0, var3, (ActiveRegion)null);
            if (var6.Onext == var5) {
               ConnectRightVertex(var0, var2, var6);
            } else {
               AddRightEdges(var0, var2, var6.Onext, var5, var5, true);
            }

            return;
         }

         var4 = var4.Onext;
      } while(var4 != var1.anEdge);

      ConnectLeftVertex(var0, var1);
   }

   static void AddSentinel(GLUtessellatorImpl var0, double var1) {
      ActiveRegion var4 = new ActiveRegion();
      GLUhalfEdge var3 = Mesh.__gl_meshMakeEdge(var0.mesh);
      if (var3 == null) {
         throw new RuntimeException();
      } else {
         var3.Org.s = 4.0E150D;
         var3.Org.t = var1;
         var3.Sym.Org.s = -4.0E150D;
         var3.Sym.Org.t = var1;
         var0.event = var3.Sym.Org;
         var4.eUp = var3;
         var4.windingNumber = 0;
         var4.inside = false;
         var4.fixUpperEdge = false;
         var4.sentinel = true;
         var4.dirty = false;
         var4.nodeUp = Dict.dictInsert(var0.dict, var4);
         if (var4.nodeUp == null) {
            throw new RuntimeException();
         }
      }
   }

   static void InitEdgeDict(final GLUtessellatorImpl var0) {
      var0.dict = Dict.dictNewDict(var0, new Dict.DictLeq() {
         public boolean leq(Object var1, Object var2, Object var3) {
            return Sweep.EdgeLeq(var0, (ActiveRegion)var2, (ActiveRegion)var3);
         }
      });
      if (var0.dict == null) {
         throw new RuntimeException();
      } else {
         AddSentinel(var0, -4.0E150D);
         AddSentinel(var0, 4.0E150D);
      }
   }

   static void DoneEdgeDict(GLUtessellatorImpl var0) {
      int var2 = 0;

      ActiveRegion var1;
      while((var1 = (ActiveRegion)Dict.dictKey(Dict.dictMin(var0.dict))) != null) {
         if (!var1.sentinel) {
            assert var1.fixUpperEdge;

            if (!$assertionsDisabled) {
               ++var2;
               if (var2 != 1) {
                  throw new AssertionError();
               }
            }
         }

         assert var1.windingNumber == 0;

         DeleteRegion(var0, var1);
      }

      Dict.dictDeleteDict(var0.dict);
   }

   static void RemoveDegenerateEdges(GLUtessellatorImpl var0) {
      GLUhalfEdge var4 = var0.mesh.eHead;

      GLUhalfEdge var2;
      for(GLUhalfEdge var1 = var4.next; var1 != var4; var1 = var2) {
         var2 = var1.next;
         GLUhalfEdge var3 = var1.Lnext;
         if (Geom.VertEq(var1.Org, var1.Sym.Org) && var1.Lnext.Lnext != var1) {
            SpliceMergeVertices(var0, var3, var1);
            if (!Mesh.__gl_meshDelete(var1)) {
               throw new RuntimeException();
            }

            var1 = var3;
            var3 = var3.Lnext;
         }

         if (var3.Lnext == var1) {
            if (var3 != var1) {
               if (var3 == var2 || var3 == var2.Sym) {
                  var2 = var2.next;
               }

               if (!Mesh.__gl_meshDelete(var3)) {
                  throw new RuntimeException();
               }
            }

            if (var1 == var2 || var1 == var2.Sym) {
               var2 = var2.next;
            }

            if (!Mesh.__gl_meshDelete(var1)) {
               throw new RuntimeException();
            }
         }
      }

   }

   static boolean InitPriorityQ(GLUtessellatorImpl var0) {
      PriorityQ var1 = var0.pq = PriorityQ.pqNewPriorityQ(new PriorityQ.Leq() {
         public boolean leq(Object var1, Object var2) {
            return Geom.VertLeq((GLUvertex)var1, (GLUvertex)var2);
         }
      });
      if (var1 == null) {
         return false;
      } else {
         GLUvertex var3 = var0.mesh.vHead;

         GLUvertex var2;
         for(var2 = var3.next; var2 != var3; var2 = var2.next) {
            var2.pqHandle = var1.pqInsert(var2);
            if ((long)var2.pqHandle == Long.MAX_VALUE) {
               break;
            }
         }

         if (var2 == var3 && var1.pqInit()) {
            return true;
         } else {
            var0.pq.pqDeletePriorityQ();
            var0.pq = null;
            return false;
         }
      }
   }

   static void DonePriorityQ(GLUtessellatorImpl var0) {
      var0.pq.pqDeletePriorityQ();
   }

   static boolean RemoveDegenerateFaces(GLUmesh var0) {
      GLUface var2;
      for(GLUface var1 = var0.fHead.next; var1 != var0.fHead; var1 = var2) {
         var2 = var1.next;
         GLUhalfEdge var3 = var1.anEdge;

         assert var3.Lnext != var3;

         if (var3.Lnext.Lnext == var3) {
            AddWinding(var3.Onext, var3);
            if (!Mesh.__gl_meshDelete(var3)) {
               return false;
            }
         }
      }

      return true;
   }

   public static boolean __gl_computeInterior(GLUtessellatorImpl var0) {
      var0.fatalError = false;
      RemoveDegenerateEdges(var0);
      if (!InitPriorityQ(var0)) {
         return false;
      } else {
         InitEdgeDict(var0);

         GLUvertex var1;
         for(; (var1 = (GLUvertex)var0.pq.pqExtractMin()) != null; SweepEvent(var0, var1)) {
            while(true) {
               GLUvertex var2 = (GLUvertex)var0.pq.pqMinimum();
               if (var2 == null || !Geom.VertEq(var2, var1)) {
                  break;
               }

               var2 = (GLUvertex)var0.pq.pqExtractMin();
               SpliceMergeVertices(var0, var1.anEdge, var2.anEdge);
            }
         }

         var0.event = ((ActiveRegion)Dict.dictKey(Dict.dictMin(var0.dict))).eUp.Org;
         DebugEvent(var0);
         DoneEdgeDict(var0);
         DonePriorityQ(var0);
         if (!RemoveDegenerateFaces(var0.mesh)) {
            return false;
         } else {
            Mesh.__gl_meshCheckMesh(var0.mesh);
            return true;
         }
      }
   }
}
