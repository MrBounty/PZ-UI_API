package zombie.core.skinnedmodel;

public final class Vector3 {
   private float x;
   private float y;
   private float z;

   public Vector3() {
      this(0.0F, 0.0F, 0.0F);
   }

   public Vector3(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   public Vector3(Vector3 var1) {
      this.set(var1);
   }

   public float x() {
      return this.x;
   }

   public Vector3 x(float var1) {
      this.x = var1;
      return this;
   }

   public float y() {
      return this.y;
   }

   public Vector3 y(float var1) {
      this.y = var1;
      return this;
   }

   public float z() {
      return this.z;
   }

   public Vector3 z(float var1) {
      this.z = var1;
      return this;
   }

   public Vector3 set(float var1, float var2, float var3) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      return this;
   }

   public Vector3 set(Vector3 var1) {
      return this.set(var1.x(), var1.y(), var1.z());
   }

   public Vector3 reset() {
      this.x = this.y = this.z = 0.0F;
      return this;
   }

   public float length() {
      return (float)Math.sqrt((double)(this.x * this.x + this.y * this.y + this.z * this.z));
   }

   public Vector3 normalize() {
      float var1 = this.length();
      this.x /= var1;
      this.y /= var1;
      this.z /= var1;
      return this;
   }

   public float dot(Vector3 var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z;
   }

   public Vector3 cross(Vector3 var1) {
      return new Vector3(this.y() * var1.z() - var1.y() * this.z(), var1.z() * this.x() - this.z() * var1.x(), this.x() * var1.y() - var1.x() * this.y());
   }

   public Vector3 add(float var1, float var2, float var3) {
      this.x += var1;
      this.y += var2;
      this.z += var3;
      return this;
   }

   public Vector3 add(Vector3 var1) {
      return this.add(var1.x(), var1.y(), var1.z());
   }

   public Vector3 sub(float var1, float var2, float var3) {
      this.x -= var1;
      this.y -= var2;
      this.z -= var3;
      return this;
   }

   public Vector3 sub(Vector3 var1) {
      return this.sub(var1.x(), var1.y(), var1.z());
   }

   public Vector3 mul(float var1) {
      return this.mul(var1, var1, var1);
   }

   public Vector3 mul(float var1, float var2, float var3) {
      this.x *= var1;
      this.y *= var2;
      this.z *= var3;
      return this;
   }

   public Vector3 mul(Vector3 var1) {
      return this.mul(var1.x(), var1.y(), var1.z());
   }
}
