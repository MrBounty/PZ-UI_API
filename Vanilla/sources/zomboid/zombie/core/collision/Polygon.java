package zombie.core.collision;

import java.util.ArrayList;
import zombie.iso.Vector2;

public final class Polygon {
   public ArrayList points = new ArrayList(4);
   public ArrayList edges = new ArrayList(4);
   float x = 0.0F;
   float y = 0.0F;
   float x2 = 0.0F;
   float y2 = 0.0F;
   Vector2[] vecs = new Vector2[4];
   Vector2[] eds = new Vector2[4];
   static Vector2 temp = new Vector2();

   public void Set(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.x2 = var3;
      this.y2 = var4;
      this.points.clear();
      if (this.vecs[0] == null) {
         for(int var5 = 0; var5 < 4; ++var5) {
            this.vecs[var5] = new Vector2();
            this.eds[var5] = new Vector2();
         }
      }

      this.vecs[0].x = var1;
      this.vecs[0].y = var2;
      this.vecs[1].x = var3;
      this.vecs[1].y = var2;
      this.vecs[2].x = var3;
      this.vecs[2].y = var4;
      this.vecs[3].x = var1;
      this.vecs[3].y = var4;
      this.points.add(this.vecs[0]);
      this.points.add(this.vecs[1]);
      this.points.add(this.vecs[2]);
      this.points.add(this.vecs[3]);
      this.BuildEdges();
   }

   public Vector2 Center() {
      temp.x = this.x + (this.x2 - this.x) / 2.0F;
      temp.y = this.y + (this.y2 - this.y) / 2.0F;
      return temp;
   }

   public void BuildEdges() {
      this.edges.clear();

      for(int var3 = 0; var3 < this.points.size(); ++var3) {
         Vector2 var1 = (Vector2)this.points.get(var3);
         Vector2 var2;
         if (var3 + 1 >= this.points.size()) {
            var2 = (Vector2)this.points.get(0);
         } else {
            var2 = (Vector2)this.points.get(var3 + 1);
         }

         this.eds[var3].x = var2.x - var1.x;
         this.eds[var3].y = var2.y - var1.y;
         this.edges.add(this.eds[var3]);
      }

   }

   public void Offset(float var1, float var2) {
      for(int var3 = 0; var3 < this.points.size(); ++var3) {
         Vector2 var4 = (Vector2)this.points.get(var3);
         var4.x += var1;
         var4.y += var2;
      }

   }

   public void Offset(Vector2 var1) {
      this.Offset(var1.x, var1.y);
   }
}
