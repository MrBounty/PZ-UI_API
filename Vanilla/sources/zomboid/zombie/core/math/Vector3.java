package zombie.core.math;

import org.joml.Vector3f;

public class Vector3 extends Vector3f {
   public Vector3() {
   }

   public Vector3(float var1, float var2, float var3) {
      super(var1, var2, var3);
   }

   public Vector3(org.lwjgl.util.vector.Vector3f var1) {
      super(var1.x, var1.y, var1.z);
   }

   public Vector3(Vector3 var1) {
      super(var1.x, var1.y, var1.z);
   }

   public static org.lwjgl.util.vector.Vector3f addScaled(org.lwjgl.util.vector.Vector3f var0, org.lwjgl.util.vector.Vector3f var1, float var2, org.lwjgl.util.vector.Vector3f var3) {
      var3.set(var0.x + var1.x * var2, var0.y + var1.y * var2, var0.z + var1.z * var2);
      return var3;
   }

   public static org.lwjgl.util.vector.Vector3f setScaled(org.lwjgl.util.vector.Vector3f var0, float var1, org.lwjgl.util.vector.Vector3f var2) {
      var2.set(var0.x * var1, var0.y * var1, var0.z * var1);
      return var2;
   }

   public org.lwjgl.util.vector.Vector3f Get() {
      org.lwjgl.util.vector.Vector3f var1 = new org.lwjgl.util.vector.Vector3f();
      var1.set(this.x, this.y, this.z);
      return var1;
   }

   public void Set(org.lwjgl.util.vector.Vector3f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
   }

   public Vector3 reset() {
      this.x = this.y = this.z = 0.0F;
      return this;
   }

   public float dot(Vector3 var1) {
      return this.x * var1.x + this.y * var1.y + this.z * var1.z;
   }

   public Vector3 cross(Vector3 var1) {
      return new Vector3(this.y() * var1.z() - var1.y() * this.z(), var1.z() * this.x() - this.z() * var1.x(), this.x() * var1.y() - var1.x() * this.y());
   }
}
