package zombie.iso;

import java.awt.Dimension;
import java.awt.Point;
import zombie.core.math.PZMath;

public final class Vector2 implements Cloneable {
   public float x;
   public float y;

   public Vector2() {
      this.x = 0.0F;
      this.y = 0.0F;
   }

   public Vector2(Vector2 var1) {
      this.x = var1.x;
      this.y = var1.y;
   }

   public Vector2(float var1, float var2) {
      this.x = var1;
      this.y = var2;
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

   public static Vector2 addScaled(Vector2 var0, Vector2 var1, float var2, Vector2 var3) {
      var3.set(var0.x + var1.x * var2, var0.y + var1.y * var2);
      return var3;
   }

   public void rotate(float var1) {
      double var2 = (double)this.x * Math.cos((double)var1) - (double)this.y * Math.sin((double)var1);
      double var4 = (double)this.x * Math.sin((double)var1) + (double)this.y * Math.cos((double)var1);
      this.x = (float)var2;
      this.y = (float)var4;
   }

   public Vector2 add(Vector2 var1) {
      this.x += var1.x;
      this.y += var1.y;
      return this;
   }

   public Vector2 aimAt(Vector2 var1) {
      this.setLengthAndDirection(this.angleTo(var1), this.getLength());
      return this;
   }

   public float angleTo(Vector2 var1) {
      return (float)Math.atan2((double)(var1.y - this.y), (double)(var1.x - this.x));
   }

   public Vector2 clone() {
      return new Vector2(this);
   }

   public float distanceTo(Vector2 var1) {
      return (float)Math.sqrt(Math.pow((double)(var1.x - this.x), 2.0D) + Math.pow((double)(var1.y - this.y), 2.0D));
   }

   public float dot(Vector2 var1) {
      return this.x * var1.x + this.y * var1.y;
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
      float var1 = (float)Math.atan2((double)this.y, (double)this.x);
      float var2 = PZMath.wrap(var1, -3.1415927F, 3.1415927F);
      return var2;
   }

   public static float getDirection(float var0, float var1) {
      float var2 = (float)Math.atan2((double)var1, (double)var0);
      float var3 = PZMath.wrap(var2, -3.1415927F, 3.1415927F);
      return var3;
   }

   /** @deprecated */
   @Deprecated
   public float getDirectionNeg() {
      return (float)Math.atan2((double)this.x, (double)this.y);
   }

   public Vector2 setDirection(float var1) {
      this.setLengthAndDirection(var1, this.getLength());
      return this;
   }

   public float getLength() {
      return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y));
   }

   public float getLengthSquared() {
      return this.x * this.x + this.y * this.y;
   }

   public Vector2 setLength(float var1) {
      this.normalize();
      this.x *= var1;
      this.y *= var1;
      return this;
   }

   public float normalize() {
      float var1 = this.getLength();
      if (var1 == 0.0F) {
         this.x = 0.0F;
         this.y = 0.0F;
      } else {
         this.x /= var1;
         this.y /= var1;
      }

      return var1;
   }

   public Vector2 set(Vector2 var1) {
      this.x = var1.x;
      this.y = var1.y;
      return this;
   }

   public Vector2 set(float var1, float var2) {
      this.x = var1;
      this.y = var2;
      return this;
   }

   public Vector2 setLengthAndDirection(float var1, float var2) {
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

   public float getX() {
      return this.x;
   }

   public void setX(float var1) {
      this.x = var1;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float var1) {
      this.y = var1;
   }

   public void tangent() {
      double var1 = (double)this.x * Math.cos(Math.toRadians(90.0D)) - (double)this.y * Math.sin(Math.toRadians(90.0D));
      double var3 = (double)this.x * Math.sin(Math.toRadians(90.0D)) + (double)this.y * Math.cos(Math.toRadians(90.0D));
      this.x = (float)var1;
      this.y = (float)var3;
   }

   public void scale(float var1) {
      scale(this, var1);
   }

   public static Vector2 scale(Vector2 var0, float var1) {
      var0.x *= var1;
      var0.y *= var1;
      return var0;
   }
}
