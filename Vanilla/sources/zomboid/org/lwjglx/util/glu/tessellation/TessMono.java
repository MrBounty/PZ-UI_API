package org.lwjglx.util.glu.tessellation;

class TessMono {
   static boolean __gl_meshTessellateMonoRegion(GLUface var0) {
      GLUhalfEdge var1 = var0.anEdge;

      assert var1.Lnext != var1 && var1.Lnext.Lnext != var1;

      while(Geom.VertLeq(var1.Sym.Org, var1.Org)) {
         var1 = var1.Onext.Sym;
      }

      while(Geom.VertLeq(var1.Org, var1.Sym.Org)) {
         var1 = var1.Lnext;
      }

      GLUhalfEdge var2 = var1.Onext.Sym;

      while(true) {
         GLUhalfEdge var3;
         while(var1.Lnext != var2) {
            if (Geom.VertLeq(var1.Sym.Org, var2.Org)) {
               while(var2.Lnext != var1 && (Geom.EdgeGoesLeft(var2.Lnext) || Geom.EdgeSign(var2.Org, var2.Sym.Org, var2.Lnext.Sym.Org) <= 0.0D)) {
                  var3 = Mesh.__gl_meshConnect(var2.Lnext, var2);
                  if (var3 == null) {
                     return false;
                  }

                  var2 = var3.Sym;
               }

               var2 = var2.Onext.Sym;
            } else {
               while(var2.Lnext != var1 && (Geom.EdgeGoesRight(var1.Onext.Sym) || Geom.EdgeSign(var1.Sym.Org, var1.Org, var1.Onext.Sym.Org) >= 0.0D)) {
                  var3 = Mesh.__gl_meshConnect(var1, var1.Onext.Sym);
                  if (var3 == null) {
                     return false;
                  }

                  var1 = var3.Sym;
               }

               var1 = var1.Lnext;
            }
         }

         assert var2.Lnext != var1;

         while(var2.Lnext.Lnext != var1) {
            var3 = Mesh.__gl_meshConnect(var2.Lnext, var2);
            if (var3 == null) {
               return false;
            }

            var2 = var3.Sym;
         }

         return true;
      }
   }

   public static boolean __gl_meshTessellateInterior(GLUmesh var0) {
      GLUface var2;
      for(GLUface var1 = var0.fHead.next; var1 != var0.fHead; var1 = var2) {
         var2 = var1.next;
         if (var1.inside && !__gl_meshTessellateMonoRegion(var1)) {
            return false;
         }
      }

      return true;
   }

   public static void __gl_meshDiscardExterior(GLUmesh var0) {
      GLUface var2;
      for(GLUface var1 = var0.fHead.next; var1 != var0.fHead; var1 = var2) {
         var2 = var1.next;
         if (!var1.inside) {
            Mesh.__gl_meshZapFace(var1);
         }
      }

   }

   public static boolean __gl_meshSetWindingNumber(GLUmesh var0, int var1, boolean var2) {
      GLUhalfEdge var4;
      for(GLUhalfEdge var3 = var0.eHead.next; var3 != var0.eHead; var3 = var4) {
         var4 = var3.next;
         if (var3.Sym.Lface.inside != var3.Lface.inside) {
            var3.winding = var3.Lface.inside ? var1 : -var1;
         } else if (!var2) {
            var3.winding = 0;
         } else if (!Mesh.__gl_meshDelete(var3)) {
            return false;
         }
      }

      return true;
   }
}
