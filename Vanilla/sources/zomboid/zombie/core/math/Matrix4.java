package zombie.core.math;

import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public final class Matrix4 extends Matrix4f {
   public Matrix4(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16) {
      super(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16);
   }

   public Matrix4(Matrix4 var1) {
      super((Matrix4fc)var1);
   }

   public org.lwjgl.util.vector.Matrix4f Get() {
      org.lwjgl.util.vector.Matrix4f var1 = new org.lwjgl.util.vector.Matrix4f();
      var1.m00 = this.m00();
      var1.m01 = this.m01();
      var1.m02 = this.m02();
      var1.m03 = this.m03();
      var1.m10 = this.m10();
      var1.m11 = this.m11();
      var1.m12 = this.m12();
      var1.m13 = this.m13();
      var1.m20 = this.m20();
      var1.m21 = this.m21();
      var1.m22 = this.m22();
      var1.m23 = this.m23();
      var1.m30 = this.m30();
      var1.m31 = this.m31();
      var1.m32 = this.m32();
      var1.m33 = this.m33();
      return var1;
   }

   public void Set(org.lwjgl.util.vector.Matrix4f var1) {
      this.set(var1.m00, var1.m01, var1.m02, var1.m03, var1.m10, var1.m11, var1.m12, var1.m13, var1.m20, var1.m21, var1.m22, var1.m23, var1.m30, var1.m31, var1.m32, var1.m33);
   }
}
