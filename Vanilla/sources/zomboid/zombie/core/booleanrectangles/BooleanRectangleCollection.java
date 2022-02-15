package zombie.core.booleanrectangles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import org.lwjgl.util.Rectangle;

public class BooleanRectangleCollection extends ArrayList {
   static boolean[][] donemap = new boolean[400][400];
   private static BooleanRectangleCollection.Point intersection = new BooleanRectangleCollection.Point();
   static int retWidth = 0;
   static int retHeight = 0;

   public void doIt(ArrayList var1, Rectangle var2) {
      ArrayList var3 = new ArrayList();
      Iterator var4 = var1.iterator();

      while(var4.hasNext()) {
         Rectangle var5 = (Rectangle)var4.next();
         ArrayList var6 = this.doIt(var5, var2);
         var3.addAll(var6);
      }

      this.clear();
      this.addAll(var3);
      this.optimize();
   }

   public void cutRectangle(Rectangle var1) {
      ArrayList var2 = new ArrayList();
      var2.addAll(this);
      this.doIt(var2, var1);
   }

   public ArrayList doIt(Rectangle var1, Rectangle var2) {
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      Rectangle var7 = var1;
      Rectangle var8 = var2;
      ArrayList var9 = new ArrayList();
      ArrayList var10 = new ArrayList();
      var9.add(new BooleanRectangleCollection.Line(new BooleanRectangleCollection.Point(var1.getX(), var1.getY()), new BooleanRectangleCollection.Point(var1.getX() + var1.getWidth(), var1.getY())));
      var9.add(new BooleanRectangleCollection.Line(new BooleanRectangleCollection.Point(var1.getX() + var1.getWidth(), var1.getY()), new BooleanRectangleCollection.Point(var1.getX() + var1.getWidth(), var1.getY() + var1.getHeight())));
      var9.add(new BooleanRectangleCollection.Line(new BooleanRectangleCollection.Point(var1.getX() + var1.getWidth(), var1.getY() + var1.getHeight()), new BooleanRectangleCollection.Point(var1.getX(), var1.getY() + var1.getHeight())));
      var9.add(new BooleanRectangleCollection.Line(new BooleanRectangleCollection.Point(var1.getX(), var1.getY() + var1.getHeight()), new BooleanRectangleCollection.Point(var1.getX(), var1.getY())));
      var10.add(new BooleanRectangleCollection.Line(new BooleanRectangleCollection.Point(var2.getX(), var2.getY()), new BooleanRectangleCollection.Point(var2.getX() + var2.getWidth(), var2.getY())));
      var10.add(new BooleanRectangleCollection.Line(new BooleanRectangleCollection.Point(var2.getX() + var2.getWidth(), var2.getY()), new BooleanRectangleCollection.Point(var2.getX() + var2.getWidth(), var2.getY() + var2.getHeight())));
      var10.add(new BooleanRectangleCollection.Line(new BooleanRectangleCollection.Point(var2.getX() + var2.getWidth(), var2.getY() + var2.getHeight()), new BooleanRectangleCollection.Point(var2.getX(), var2.getY() + var2.getHeight())));
      var10.add(new BooleanRectangleCollection.Line(new BooleanRectangleCollection.Point(var2.getX(), var2.getY() + var2.getHeight()), new BooleanRectangleCollection.Point(var2.getX(), var2.getY())));

      int var11;
      int var12;
      for(var11 = 0; var11 < var9.size(); ++var11) {
         for(var12 = 0; var12 < var10.size(); ++var12) {
            if (this.IntesectsLine((BooleanRectangleCollection.Line)var9.get(var11), (BooleanRectangleCollection.Line)var10.get(var12)) != 0 && this.IsPointInRect(intersection.X, intersection.Y, var7)) {
               var4.add(new BooleanRectangleCollection.Point(intersection.X, intersection.Y));
            }
         }
      }

      if (this.IsPointInRect(var2.getX(), var2.getY(), var7)) {
         var4.add(new BooleanRectangleCollection.Point(var2.getX(), var2.getY()));
      }

      if (this.IsPointInRect(var2.getX() + var2.getWidth(), var2.getY(), var7)) {
         var4.add(new BooleanRectangleCollection.Point(var2.getX() + var2.getWidth(), var2.getY()));
      }

      if (this.IsPointInRect(var2.getX() + var2.getWidth(), var2.getY() + var2.getHeight(), var7)) {
         var4.add(new BooleanRectangleCollection.Point(var2.getX() + var2.getWidth(), var2.getY() + var2.getHeight()));
      }

      if (this.IsPointInRect(var2.getX(), var2.getY() + var2.getHeight(), var7)) {
         var4.add(new BooleanRectangleCollection.Point(var2.getX(), var2.getY() + var2.getHeight()));
      }

      var4.add(new BooleanRectangleCollection.Point(var7.getX(), var7.getY()));
      var4.add(new BooleanRectangleCollection.Point(var7.getX() + var7.getWidth(), var7.getY()));
      var4.add(new BooleanRectangleCollection.Point(var7.getX() + var7.getWidth(), var7.getY() + var7.getHeight()));
      var4.add(new BooleanRectangleCollection.Point(var7.getX(), var7.getY() + var7.getHeight()));
      Collections.sort(var4, new Comparator() {
         public int compare(BooleanRectangleCollection.Point var1, BooleanRectangleCollection.Point var2) {
            return var1.Y != var2.Y ? var1.Y - var2.Y : var1.X - var2.X;
         }
      });
      var11 = ((BooleanRectangleCollection.Point)var4.get(0)).X;
      var12 = ((BooleanRectangleCollection.Point)var4.get(0)).Y;
      var5.add(var11);
      var6.add(var12);
      Iterator var13 = var4.iterator();

      while(var13.hasNext()) {
         BooleanRectangleCollection.Point var14 = (BooleanRectangleCollection.Point)var13.next();
         if (var14.X > var11) {
            var11 = var14.X;
            var5.add(var11);
         }

         if (var14.Y > var12) {
            var12 = var14.Y;
            var6.add(var12);
         }
      }

      for(int var20 = 0; var20 < var6.size() - 1; ++var20) {
         for(int var21 = 0; var21 < var5.size() - 1; ++var21) {
            int var15 = (Integer)var5.get(var21);
            int var16 = (Integer)var6.get(var20);
            int var17 = (Integer)var5.get(var21 + 1) - var15;
            int var18 = (Integer)var6.get(var20 + 1) - var16;
            Rectangle var19 = new Rectangle(var15, var16, var17, var18);
            if (!this.Intersects(var19, var8)) {
               var3.add(var19);
            }
         }
      }

      return var3;
   }

   public void optimize() {
      ArrayList var1 = new ArrayList();
      int var2 = 1000000;
      int var3 = 1000000;
      int var4 = -1000000;
      int var5 = -1000000;

      int var6;
      for(var6 = 0; var6 < this.size(); ++var6) {
         Rectangle var7 = (Rectangle)this.get(var6);
         if (var7.getX() < var2) {
            var2 = var7.getX();
         }

         if (var7.getY() < var3) {
            var3 = var7.getY();
         }

         if (var7.getX() + var7.getWidth() > var4) {
            var4 = var7.getX() + var7.getWidth();
         }

         if (var7.getY() + var7.getHeight() > var5) {
            var5 = var7.getY() + var7.getHeight();
         }
      }

      var6 = var4 - var2;
      int var14 = var5 - var3;

      int var8;
      int var9;
      for(var8 = 0; var8 < var6; ++var8) {
         for(var9 = 0; var9 < var14; ++var9) {
            donemap[var8][var9] = true;
         }
      }

      int var10;
      int var11;
      int var12;
      int var13;
      for(var8 = 0; var8 < this.size(); ++var8) {
         Rectangle var15 = (Rectangle)this.get(var8);
         var10 = var15.getX() - var2;
         var11 = var15.getY() - var3;

         for(var12 = 0; var12 < var15.getWidth(); ++var12) {
            for(var13 = 0; var13 < var15.getHeight(); ++var13) {
               donemap[var12 + var10][var13 + var11] = false;
            }
         }
      }

      for(var8 = 0; var8 < var6; ++var8) {
         for(var9 = 0; var9 < var14; ++var9) {
            if (!donemap[var8][var9]) {
               var10 = this.DoHeight(var8, var9, var14);
               var11 = this.DoWidth(var8, var9, var10, var6);

               for(var12 = 0; var12 < var11; ++var12) {
                  for(var13 = 0; var13 < var10; ++var13) {
                     donemap[var12 + var8][var13 + var9] = true;
                  }
               }

               var1.add(new Rectangle(var8 + var2, var9 + var3, var11, var10));
            }
         }
      }

      this.clear();
      this.addAll(var1);
   }

   public boolean IsPointInRect(int var1, int var2, Rectangle var3) {
      return var1 >= var3.getX() && var1 <= var3.getX() + var3.getWidth() && var2 >= var3.getY() && var2 <= var3.getY() + var3.getHeight();
   }

   public int IntesectsLine(BooleanRectangleCollection.Line var1, BooleanRectangleCollection.Line var2) {
      intersection.X = 0;
      intersection.Y = 0;
      int var3 = var1.End.X - var1.Start.X;
      int var4 = var1.End.Y - var1.Start.Y;
      int var5 = var2.End.X - var2.Start.X;
      int var6 = var2.End.Y - var2.Start.Y;
      if (var3 != var5 && var4 != var6) {
         int var7;
         int var8;
         int var9;
         int var10;
         if (var4 == 0) {
            var7 = Math.min(var1.Start.X, var1.End.X);
            var8 = Math.max(var1.Start.X, var1.End.X);
            var9 = Math.min(var2.Start.Y, var2.End.Y);
            var10 = Math.max(var2.Start.Y, var2.End.Y);
            intersection.X = var2.Start.X;
            intersection.Y = var1.Start.Y;
            return 1;
         } else {
            var7 = Math.min(var2.Start.X, var2.End.X);
            var8 = Math.max(var2.Start.X, var2.End.X);
            var9 = Math.min(var1.Start.Y, var1.End.Y);
            var10 = Math.max(var1.Start.Y, var1.End.Y);
            intersection.X = var1.Start.X;
            intersection.Y = var2.Start.Y;
            return -1;
         }
      } else {
         return 0;
      }
   }

   public boolean Intersects(Rectangle var1, Rectangle var2) {
      int var3 = var1.getX() + var1.getWidth();
      int var4 = var1.getX();
      int var5 = var1.getY() + var1.getHeight();
      int var6 = var1.getY();
      int var7 = var2.getX() + var2.getWidth();
      int var8 = var2.getX();
      int var9 = var2.getY() + var2.getHeight();
      int var10 = var2.getY();
      return var3 > var8 && var5 > var10 && var4 < var7 && var6 < var9;
   }

   private int DoHeight(int var1, int var2, int var3) {
      int var4 = 0;

      for(int var5 = var2; var5 < var3; ++var5) {
         if (donemap[var1][var5]) {
            return var4;
         }

         ++var4;
      }

      return var4;
   }

   private int DoWidth(int var1, int var2, int var3, int var4) {
      int var5 = 0;

      for(int var6 = var1; var6 < var4; ++var6) {
         for(int var7 = var2; var7 < var3; ++var7) {
            if (donemap[var6][var7]) {
               return var5;
            }
         }

         ++var5;
      }

      return var5;
   }

   private void DoRect(int var1, int var2) {
   }

   public class Line {
      public BooleanRectangleCollection.Point Start = new BooleanRectangleCollection.Point();
      public BooleanRectangleCollection.Point End = new BooleanRectangleCollection.Point();

      public Line(BooleanRectangleCollection.Point var2, BooleanRectangleCollection.Point var3) {
         this.Start.X = var2.X;
         this.Start.Y = var2.Y;
         this.End.X = var3.X;
         this.End.Y = var3.Y;
      }
   }

   public static class Point {
      public int X;
      public int Y;

      public Point() {
      }

      public Point(int var1, int var2) {
         this.X = var1;
         this.Y = var2;
      }
   }
}
