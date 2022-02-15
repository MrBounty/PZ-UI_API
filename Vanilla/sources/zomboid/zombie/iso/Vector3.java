package zombie.iso;

import java.awt.Dimension;
import java.awt.Point;

public final class Vector3 implements Cloneable {
   public float x;
   public float y;
   public float z;

   public Vector3() {
      this.x = 0.0F;
      this.y = 0.0F;
      this.z = 0.0F;
   }

   public Vector3(Vector3 var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public Vector3(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public static Vector2 fromAwtPoint(Point var0) {
      return new Vector2((float)var0.x, (float)var0.y);
   }

   public static Vector2 fromLengthDirection(float var0, float var1) {
      Vector2 var2 = new Vector2();
      var2.setLengthAndDirection(var1, var0);
      return var2;
   }

   public static float dot(float var0, float var1, float var2, float var3) {
      return var0 * var2 + var1 * var3;
   }

   public void rotate(float var1) {
      double var2 = (double)this.x * Math.cos((double)var1) - (double)this.y * Math.sin((double)var1);
      double var4 = (double)this.x * Math.sin((double)var1) + (double)this.y * Math.cos((double)var1);
      this.x = (float)var2;
      this.y = (float)var4;
   }

   public void rotatey(float var1) {
      double var2 = (double)this.x * Math.cos((double)var1) - (double)this.z * Math.sin((double)var1);
      double var4 = (double)this.x * Math.sin((double)var1) + (double)this.z * Math.cos((double)var1);
      this.x = (float)var2;
      this.z = (float)var4;
   }

   public Vector2 add(Vector2 var1) {
      return new Vector2(this.x + var1.x, this.y + var1.y);
   }

   public Vector3 addToThis(Vector2 var1) {
      this.x += var1.x;
      this.y += var1.y;
      return this;
   }

   public Vector3 addToThis(Vector3 var1) {
      this.x += var1.x;
      this.y += var1.y;
      this.z += var1.z;
      return this;
   }

   public Vector3 div(float var1) {
      this.x /= var1;
      this.y /= var1;
      this.z /= var1;
      return this;
   }

   public Vector3 aimAt(Vector2 var1) {
      this.setLengthAndDirection(this.angleTo(var1), this.getLength());
      return this;
   }

   public float angleTo(Vector2 var1) {
      return (float)Math.atan2((double)(var1.y - this.y), (double)(var1.x - this.x));
   }

   public Vector3 clone() {
      return new Vector3(this);
   }

   public float distanceTo(Vector2 var1) {
      return (float)Math.sqrt(Math.pow((double)(var1.x - this.x), 2.0D) + Math.pow((double)(var1.y - this.y), 2.0D));
   }

   public float dot(Vector2 var1) {
      return this.x * var1.x + this.y * var1.y;
   }

   public float dot3d(Vector3 var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Vector2)) {
         return false;
      } else {
         Vector2 var2 = (Vector2)var1;
         return var2.x == this.x && var2.y == this.y;
      }
   }

   public float getDirection() {
      return (float)Math.atan2((double)this.x, (double)this.y);
   }

   public Vector3 setDirection(float var1) {
      this.setLengthAndDirection(var1, this.getLength());
      return this;
   }

   public float getLength() {
      float var1 = this.getLengthSq();
      return (float)Math.sqrt((double)var1);
   }

   public float getLengthSq() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public Vector3 setLength(float var1) {
      this.normalize();
      this.x *= var1;
      this.y *= var1;
      this.z *= var1;
      return this;
   }

   public void normalize() {
      float var1 = this.getLength();
      if (var1 == 0.0F) {
         this.x = 0.0F;
         this.y = 0.0F;
         this.z = 0.0F;
      } else {
         this.x /= var1;
         this.y /= var1;
         this.z /= var1;
      }

      var1 = this.getLength();
   }

   public Vector3 set(Vector3 var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      return this;
   }

   public Vector3 set(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      return this;
   }

   public Vector3 setLengthAndDirection(float var1, float var2) {
      this.x = (float)(Math.cos((double)var1) * (double)var2);
      this.y = (float)(Math.sin((double)var1) * (double)var2);
      return this;
   }

   public Dimension toAwtDimension() {
      return new Dimension((int)this.x, (int)this.y);
   }

   public Point toAwtPoint() {
      return new Point((int)this.x, (int)this.y);
   }

   public String toString() {
      return String.format("Vector2 (X: %f, Y: %f) (L: %f, D:%f)", this.x, this.y, this.getLength(), this.getDirection());
   }

   public Vector3 sub(Vector3 var1, Vector3 var2) {
      return sub(this, var1, var2);
   }

   public static Vector3 sub(Vector3 var0, Vector3 var1, Vector3 var2) {
      var2.set(var0.x - var1.x, var0.y - var1.y, var0.z - var1.z);
      return var2;
   }
}
