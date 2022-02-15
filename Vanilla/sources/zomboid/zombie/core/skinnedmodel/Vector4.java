package zombie.core.skinnedmodel;

public final class Vector4 {
   public float x;
   public float y;
   public float z;
   public float w;

   public Vector4() {
      this(0.0F, 0.0F, 0.0F, 0.0F);
   }

   public Vector4(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
   }

   public Vector4(Vector4 var1) {
      this.set(var1);
   }

   public Vector4 set(float var1, float var2, float var3, float var4) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
      this.w = var4;
      return this;
   }

   public Vector4 set(Vector4 var1) {
      return this.set(var1.x, var1.y, var1.z, var1.w);
   }
}
