package org.joml;

public class FrustumRayBuilder {
   private float nxnyX;
   private float nxnyY;
   private float nxnyZ;
   private float pxnyX;
   private float pxnyY;
   private float pxnyZ;
   private float pxpyX;
   private float pxpyY;
   private float pxpyZ;
   private float nxpyX;
   private float nxpyY;
   private float nxpyZ;
   private float cx;
   private float cy;
   private float cz;

   public FrustumRayBuilder() {
   }

   public FrustumRayBuilder(Matrix4fc var1) {
      this.set(var1);
   }

   public FrustumRayBuilder set(Matrix4fc var1) {
      float var2 = var1.m03() + var1.m00();
      float var3 = var1.m13() + var1.m10();
      float var4 = var1.m23() + var1.m20();
      float var5 = var1.m33() + var1.m30();
      float var6 = var1.m03() - var1.m00();
      float var7 = var1.m13() - var1.m10();
      float var8 = var1.m23() - var1.m20();
      float var9 = var1.m33() - var1.m30();
      float var10 = var1.m03() + var1.m01();
      float var11 = var1.m13() + var1.m11();
      float var12 = var1.m23() + var1.m21();
      float var13 = var1.m03() - var1.m01();
      float var14 = var1.m13() - var1.m11();
      float var15 = var1.m23() - var1.m21();
      float var16 = var1.m33() - var1.m31();
      this.nxnyX = var11 * var4 - var12 * var3;
      this.nxnyY = var12 * var2 - var10 * var4;
      this.nxnyZ = var10 * var3 - var11 * var2;
      this.pxnyX = var7 * var12 - var8 * var11;
      this.pxnyY = var8 * var10 - var6 * var12;
      this.pxnyZ = var6 * var11 - var7 * var10;
      this.nxpyX = var3 * var15 - var4 * var14;
      this.nxpyY = var4 * var13 - var2 * var15;
      this.nxpyZ = var2 * var14 - var3 * var13;
      this.pxpyX = var14 * var8 - var15 * var7;
      this.pxpyY = var15 * var6 - var13 * var8;
      this.pxpyZ = var13 * var7 - var14 * var6;
      float var17 = var7 * var4 - var8 * var3;
      float var18 = var8 * var2 - var6 * var4;
      float var19 = var6 * var3 - var7 * var2;
      float var20 = 1.0F / (var2 * this.pxpyX + var3 * this.pxpyY + var4 * this.pxpyZ);
      this.cx = (-this.pxpyX * var5 - this.nxpyX * var9 - var17 * var16) * var20;
      this.cy = (-this.pxpyY * var5 - this.nxpyY * var9 - var18 * var16) * var20;
      this.cz = (-this.pxpyZ * var5 - this.nxpyZ * var9 - var19 * var16) * var20;
      return this;
   }

   public Vector3fc origin(Vector3f var1) {
      var1.x = this.cx;
      var1.y = this.cy;
      var1.z = this.cz;
      return var1;
   }

   public Vector3fc dir(float var1, float var2, Vector3f var3) {
      float var4 = this.nxnyX + (this.nxpyX - this.nxnyX) * var2;
      float var5 = this.nxnyY + (this.nxpyY - this.nxnyY) * var2;
      float var6 = this.nxnyZ + (this.nxpyZ - this.nxnyZ) * var2;
      float var7 = this.pxnyX + (this.pxpyX - this.pxnyX) * var2;
      float var8 = this.pxnyY + (this.pxpyY - this.pxnyY) * var2;
      float var9 = this.pxnyZ + (this.pxpyZ - this.pxnyZ) * var2;
      float var10 = var4 + (var7 - var4) * var1;
      float var11 = var5 + (var8 - var5) * var1;
      float var12 = var6 + (var9 - var6) * var1;
      float var13 = Math.invsqrt(var10 * var10 + var11 * var11 + var12 * var12);
      var3.x = var10 * var13;
      var3.y = var11 * var13;
      var3.z = var12 * var13;
      return var3;
   }
}
