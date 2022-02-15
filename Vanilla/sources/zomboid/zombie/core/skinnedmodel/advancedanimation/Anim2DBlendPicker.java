package zombie.core.skinnedmodel.advancedanimation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;

public final class Anim2DBlendPicker {
   private List m_tris;
   private List m_hull;
   private Anim2DBlendPicker.HullComparer m_hullComparer;

   public void SetPickTriangles(List var1) {
      this.m_tris = var1;
      this.BuildHull();
   }

   private void BuildHull() {
      HashMap var1 = new HashMap();
      Anim2DBlendPicker.Counter var2 = new Anim2DBlendPicker.Counter();

      Anim2DBlendPicker.Counter var5;
      for(Iterator var3 = this.m_tris.iterator(); var3.hasNext(); var5.Increment()) {
         Anim2DBlendTriangle var4 = (Anim2DBlendTriangle)var3.next();
         var5 = (Anim2DBlendPicker.Counter)var1.putIfAbsent(new Anim2DBlendPicker.Edge(var4.node1, var4.node2), var2);
         if (var5 == null) {
            var5 = var2;
            var2 = new Anim2DBlendPicker.Counter();
         }

         var5.Increment();
         var5 = (Anim2DBlendPicker.Counter)var1.putIfAbsent(new Anim2DBlendPicker.Edge(var4.node2, var4.node3), var2);
         if (var5 == null) {
            var5 = var2;
            var2 = new Anim2DBlendPicker.Counter();
         }

         var5.Increment();
         var5 = (Anim2DBlendPicker.Counter)var1.putIfAbsent(new Anim2DBlendPicker.Edge(var4.node3, var4.node1), var2);
         if (var5 == null) {
            var5 = var2;
            var2 = new Anim2DBlendPicker.Counter();
         }
      }

      HashSet var9 = new HashSet();
      var1.forEach((var1x, var2x) -> {
         if (var2x.count == 1) {
            var9.add(var1x.a);
            var9.add(var1x.b);
         }

      });
      ArrayList var10 = new ArrayList(var9);
      float var11 = 0.0F;
      float var6 = 0.0F;

      Anim2DBlend var8;
      for(Iterator var7 = var10.iterator(); var7.hasNext(); var6 += var8.m_YPos) {
         var8 = (Anim2DBlend)var7.next();
         var11 += var8.m_XPos;
      }

      var11 /= (float)var10.size();
      var6 /= (float)var10.size();
      this.m_hullComparer = new Anim2DBlendPicker.HullComparer(var11, var6);
      var10.sort(this.m_hullComparer);
      this.m_hull = var10;
   }

   static int LowerBoundIdx(List var0, Object var1, Comparator var2) {
      int var3 = 0;
      int var4 = var0.size();

      while(var3 != var4) {
         int var5 = (var3 + var4) / 2;
         if (var2.compare(var1, var0.get(var5)) < 0) {
            var4 = var5;
         } else {
            var3 = var5 + 1;
         }
      }

      return var3;
   }

   private static float ProjectPointToLine(float var0, float var1, float var2, float var3, float var4, float var5) {
      float var6 = var0 - var2;
      float var7 = var1 - var3;
      float var8 = var4 - var2;
      float var9 = var5 - var3;
      return (var8 * var6 + var9 * var7) / (var8 * var8 + var9 * var9);
   }

   public Anim2DBlendPicker.PickResults Pick(float var1, float var2) {
      Anim2DBlendPicker.PickResults var3 = new Anim2DBlendPicker.PickResults();
      Iterator var4 = this.m_tris.iterator();

      Anim2DBlendTriangle var5;
      float var9;
      do {
         if (!var4.hasNext()) {
            var1 *= 1.1F;
            var2 *= 1.1F;
            Anim2DBlend var12 = new Anim2DBlend();
            var12.m_XPos = var1;
            var12.m_YPos = var2;
            int var13 = LowerBoundIdx(this.m_hull, var12, this.m_hullComparer);
            if (var13 == this.m_hull.size()) {
               var13 = 0;
            }

            int var14 = var13 > 0 ? var13 - 1 : this.m_hull.size() - 1;
            Anim2DBlend var15 = (Anim2DBlend)this.m_hull.get(var13);
            Anim2DBlend var16 = (Anim2DBlend)this.m_hull.get(var14);
            var9 = ProjectPointToLine(var1, var2, var15.m_XPos, var15.m_YPos, var16.m_XPos, var16.m_YPos);
            if (var9 < 0.0F) {
               var3.numNodes = 1;
               var3.node1 = var15;
               var3.scale1 = 1.0F;
            } else if (var9 > 1.0F) {
               var3.numNodes = 1;
               var3.node1 = var16;
               var3.scale1 = 1.0F;
            } else {
               var3.numNodes = 2;
               var3.node1 = var15;
               var3.node2 = var16;
               var3.scale1 = 1.0F - var9;
               var3.scale2 = var9;
            }

            return var3;
         }

         var5 = (Anim2DBlendTriangle)var4.next();
      } while(!var5.Contains(var1, var2));

      var3.numNodes = 3;
      var3.node1 = var5.node1;
      var3.node2 = var5.node2;
      var3.node3 = var5.node3;
      float var6 = var3.node1.m_XPos;
      float var7 = var3.node1.m_YPos;
      float var8 = var3.node2.m_XPos;
      var9 = var3.node2.m_YPos;
      float var10 = var3.node3.m_XPos;
      float var11 = var3.node3.m_YPos;
      var3.scale1 = ((var9 - var11) * (var1 - var10) + (var10 - var8) * (var2 - var11)) / ((var9 - var11) * (var6 - var10) + (var10 - var8) * (var7 - var11));
      var3.scale2 = ((var11 - var7) * (var1 - var10) + (var6 - var10) * (var2 - var11)) / ((var9 - var11) * (var6 - var10) + (var10 - var8) * (var7 - var11));
      var3.scale3 = 1.0F - var3.scale1 - var3.scale2;
      return var3;
   }

   void render(float var1, float var2) {
      short var3 = 200;
      int var4 = Core.getInstance().getScreenWidth() - var3 - 100;
      int var5 = Core.getInstance().getScreenHeight() - var3 - 100;
      SpriteRenderer.instance.renderi((Texture)null, var4 - 20, var5 - 20, var3 + 40, var3 + 40, 1.0F, 1.0F, 1.0F, 1.0F, (Consumer)null);

      for(int var6 = 0; var6 < this.m_tris.size(); ++var6) {
         Anim2DBlendTriangle var7 = (Anim2DBlendTriangle)this.m_tris.get(var6);
         SpriteRenderer.instance.renderline((Texture)null, (int)((float)(var4 + var3 / 2) + var7.node1.m_XPos * (float)var3 / 2.0F), (int)((float)(var5 + var3 / 2) - var7.node1.m_YPos * (float)var3 / 2.0F), (int)((float)(var4 + var3 / 2) + var7.node2.m_XPos * (float)var3 / 2.0F), (int)((float)(var5 + var3 / 2) - var7.node2.m_YPos * (float)var3 / 2.0F), 0.5F, 0.5F, 0.5F, 1.0F);
         SpriteRenderer.instance.renderline((Texture)null, (int)((float)(var4 + var3 / 2) + var7.node2.m_XPos * (float)var3 / 2.0F), (int)((float)(var5 + var3 / 2) - var7.node2.m_YPos * (float)var3 / 2.0F), (int)((float)(var4 + var3 / 2) + var7.node3.m_XPos * (float)var3 / 2.0F), (int)((float)(var5 + var3 / 2) - var7.node3.m_YPos * (float)var3 / 2.0F), 0.5F, 0.5F, 0.5F, 1.0F);
         SpriteRenderer.instance.renderline((Texture)null, (int)((float)(var4 + var3 / 2) + var7.node3.m_XPos * (float)var3 / 2.0F), (int)((float)(var5 + var3 / 2) - var7.node3.m_YPos * (float)var3 / 2.0F), (int)((float)(var4 + var3 / 2) + var7.node1.m_XPos * (float)var3 / 2.0F), (int)((float)(var5 + var3 / 2) - var7.node1.m_YPos * (float)var3 / 2.0F), 0.5F, 0.5F, 0.5F, 1.0F);
      }

      float var8 = 8.0F;
      Anim2DBlendPicker.PickResults var9 = this.Pick(var1, var2);
      if (var9.node1 != null) {
         SpriteRenderer.instance.render((Texture)null, (float)(var4 + var3 / 2) + var9.node1.m_XPos * (float)var3 / 2.0F - var8 / 2.0F, (float)(var5 + var3 / 2) - var9.node1.m_YPos * (float)var3 / 2.0F - var8 / 2.0F, var8, var8, 0.0F, 1.0F, 0.0F, 1.0F, (Consumer)null);
      }

      if (var9.node2 != null) {
         SpriteRenderer.instance.render((Texture)null, (float)(var4 + var3 / 2) + var9.node2.m_XPos * (float)var3 / 2.0F - var8 / 2.0F, (float)(var5 + var3 / 2) - var9.node2.m_YPos * (float)var3 / 2.0F - var8 / 2.0F, var8, var8, 0.0F, 1.0F, 0.0F, 1.0F, (Consumer)null);
      }

      if (var9.node3 != null) {
         SpriteRenderer.instance.render((Texture)null, (float)(var4 + var3 / 2) + var9.node3.m_XPos * (float)var3 / 2.0F - var8 / 2.0F, (float)(var5 + var3 / 2) - var9.node3.m_YPos * (float)var3 / 2.0F - var8 / 2.0F, var8, var8, 0.0F, 1.0F, 0.0F, 1.0F, (Consumer)null);
      }

      var8 = 4.0F;
      SpriteRenderer.instance.render((Texture)null, (float)(var4 + var3 / 2) + var1 * (float)var3 / 2.0F - var8 / 2.0F, (float)(var5 + var3 / 2) - var2 * (float)var3 / 2.0F - var8 / 2.0F, var8, var8, 0.0F, 0.0F, 1.0F, 1.0F, (Consumer)null);
   }

   static class Counter {
      public int count = 0;

      public int Increment() {
         return ++this.count;
      }
   }

   static class Edge {
      public Anim2DBlend a;
      public Anim2DBlend b;

      public Edge(Anim2DBlend var1, Anim2DBlend var2) {
         boolean var3;
         if (var1.m_XPos != var2.m_XPos) {
            var3 = var1.m_XPos > var2.m_XPos;
         } else {
            var3 = var1.m_YPos > var2.m_YPos;
         }

         if (var3) {
            this.a = var2;
            this.b = var1;
         } else {
            this.a = var1;
            this.b = var2;
         }

      }

      public int hashCode() {
         int var1 = this.a.hashCode();
         int var2 = this.b.hashCode();
         return (var1 << 5) + var1 ^ var2;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof Anim2DBlendPicker.Edge)) {
            return false;
         } else {
            return this.a == ((Anim2DBlendPicker.Edge)var1).a && this.b == ((Anim2DBlendPicker.Edge)var1).b;
         }
      }
   }

   static class HullComparer implements Comparator {
      private int centerX;
      private int centerY;

      public HullComparer(float var1, float var2) {
         this.centerX = (int)(var1 * 1000.0F);
         this.centerY = (int)(var2 * 1000.0F);
      }

      public boolean isLessThan(Anim2DBlend var1, Anim2DBlend var2) {
         int var3 = (int)(var1.m_XPos * 1000.0F);
         int var4 = (int)(var1.m_YPos * 1000.0F);
         int var5 = (int)(var2.m_XPos * 1000.0F);
         int var6 = (int)(var2.m_YPos * 1000.0F);
         int var7 = var3 - this.centerX;
         int var8 = var4 - this.centerY;
         int var9 = var5 - this.centerX;
         int var10 = var6 - this.centerY;
         if (var8 == 0 && var7 > 0) {
            return true;
         } else if (var10 == 0 && var9 > 0) {
            return false;
         } else if (var8 > 0 && var10 < 0) {
            return true;
         } else if (var8 < 0 && var10 > 0) {
            return false;
         } else {
            int var11 = var7 * var10 - var8 * var9;
            return var11 > 0;
         }
      }

      public int compare(Anim2DBlend var1, Anim2DBlend var2) {
         if (this.isLessThan(var1, var2)) {
            return -1;
         } else {
            return this.isLessThan(var2, var1) ? 1 : 0;
         }
      }
   }

   public static class PickResults {
      public int numNodes;
      public Anim2DBlend node1;
      public Anim2DBlend node2;
      public Anim2DBlend node3;
      public float scale1;
      public float scale2;
      public float scale3;
   }
}
