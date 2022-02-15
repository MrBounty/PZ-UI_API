package zombie.core.math;

import org.joml.Vector4f;

public class Vector4 extends Vector4f {
   public Vector4() {
   }

   public Vector4(float var1, float var2, float var3, float var4) {
      super(var1, var2, var3, var4);
   }

   public Vector4(org.lwjgl.util.vector.Vector4f var1) {
      super(var1.x, var1.y, var1.z, var1.w);
   }

   public org.lwjgl.util.vector.Vector4f Get() {
      org.lwjgl.util.vector.Vector4f var1 = new org.lwjgl.util.vector.Vector4f();
      var1.set(this.x, this.y, this.z, this.w);
      return var1;
   }

   public void Set(org.lwjgl.util.vector.Vector4f var1) {
      this.x = var1.x;
      this.y = var1.y;
      this.z = var1.z;
      this.w = var1.w;
   }
}
