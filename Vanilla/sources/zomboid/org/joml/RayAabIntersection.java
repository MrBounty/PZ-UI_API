package org.joml;

public class RayAabIntersection {
   private float originX;
   private float originY;
   private float originZ;
   private float dirX;
   private float dirY;
   private float dirZ;
   private float c_xy;
   private float c_yx;
   private float c_zy;
   private float c_yz;
   private float c_xz;
   private float c_zx;
   private float s_xy;
   private float s_yx;
   private float s_zy;
   private float s_yz;
   private float s_xz;
   private float s_zx;
   private byte classification;

   public RayAabIntersection() {
   }

   public RayAabIntersection(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.set(var1, var2, var3, var4, var5, var6);
   }

   public void set(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.originX = var1;
      this.originY = var2;
      this.originZ = var3;
      this.dirX = var4;
      this.dirY = var5;
      this.dirZ = var6;
      this.precomputeSlope();
   }

   private static int signum(float var0) {
      return var0 != 0.0F && !Float.isNaN(var0) ? (1 - Float.floatToIntBits(var0) >>> 31 << 1) - 1 : 0;
   }

   private void precomputeSlope() {
      float var1 = 1.0F / this.dirX;
      float var2 = 1.0F / this.dirY;
      float var3 = 1.0F / this.dirZ;
      this.s_yx = this.dirX * var2;
      this.s_xy = this.dirY * var1;
      this.s_zy = this.dirY * var3;
      this.s_yz = this.dirZ * var2;
      this.s_xz = this.dirZ * var1;
      this.s_zx = this.dirX * var3;
      this.c_xy = this.originY - this.s_xy * this.originX;
      this.c_yx = this.originX - this.s_yx * this.originY;
      this.c_zy = this.originY - this.s_zy * this.originZ;
      this.c_yz = this.originZ - this.s_yz * this.originY;
      this.c_xz = this.originZ - this.s_xz * this.originX;
      this.c_zx = this.originX - this.s_zx * this.originZ;
      int var4 = signum(this.dirX);
      int var5 = signum(this.dirY);
      int var6 = signum(this.dirZ);
      this.classification = (byte)(var6 + 1 << 4 | var5 + 1 << 2 | var4 + 1);
   }

   public boolean test(float var1, float var2, float var3, float var4, float var5, float var6) {
      switch(this.classification) {
      case 0:
         return this.MMM(var1, var2, var3, var4, var5, var6);
      case 1:
         return this.OMM(var1, var2, var3, var4, var5, var6);
      case 2:
         return this.PMM(var1, var2, var3, var4, var5, var6);
      case 3:
         return false;
      case 4:
         return this.MOM(var1, var2, var3, var4, var5, var6);
      case 5:
         return this.OOM(var1, var2, var3, var4, var5);
      case 6:
         return this.POM(var1, var2, var3, var4, var5, var6);
      case 7:
         return false;
      case 8:
         return this.MPM(var1, var2, var3, var4, var5, var6);
      case 9:
         return this.OPM(var1, var2, var3, var4, var5, var6);
      case 10:
         return this.PPM(var1, var2, var3, var4, var5, var6);
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
         return false;
      case 16:
         return this.MMO(var1, var2, var3, var4, var5, var6);
      case 17:
         return this.OMO(var1, var2, var3, var4, var6);
      case 18:
         return this.PMO(var1, var2, var3, var4, var5, var6);
      case 19:
         return false;
      case 20:
         return this.MOO(var1, var2, var3, var5, var6);
      case 21:
         return false;
      case 22:
         return this.POO(var2, var3, var4, var5, var6);
      case 23:
         return false;
      case 24:
         return this.MPO(var1, var2, var3, var4, var5, var6);
      case 25:
         return this.OPO(var1, var3, var4, var5, var6);
      case 26:
         return this.PPO(var1, var2, var3, var4, var5, var6);
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
         return false;
      case 32:
         return this.MMP(var1, var2, var3, var4, var5, var6);
      case 33:
         return this.OMP(var1, var2, var3, var4, var5, var6);
      case 34:
         return this.PMP(var1, var2, var3, var4, var5, var6);
      case 35:
         return false;
      case 36:
         return this.MOP(var1, var2, var3, var4, var5, var6);
      case 37:
         return this.OOP(var1, var2, var4, var5, var6);
      case 38:
         return this.POP(var1, var2, var3, var4, var5, var6);
      case 39:
         return false;
      case 40:
         return this.MPP(var1, var2, var3, var4, var5, var6);
      case 41:
         return this.OPP(var1, var2, var3, var4, var5, var6);
      case 42:
         return this.PPP(var1, var2, var3, var4, var5, var6);
      default:
         return false;
      }
   }

   private boolean MMM(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX >= var1 && this.originY >= var2 && this.originZ >= var3 && this.s_xy * var1 - var5 + this.c_xy <= 0.0F && this.s_yx * var2 - var4 + this.c_yx <= 0.0F && this.s_zy * var3 - var5 + this.c_zy <= 0.0F && this.s_yz * var2 - var6 + this.c_yz <= 0.0F && this.s_xz * var1 - var6 + this.c_xz <= 0.0F && this.s_zx * var3 - var4 + this.c_zx <= 0.0F;
   }

   private boolean OMM(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX >= var1 && this.originX <= var4 && this.originY >= var2 && this.originZ >= var3 && this.s_zy * var3 - var5 + this.c_zy <= 0.0F && this.s_yz * var2 - var6 + this.c_yz <= 0.0F;
   }

   private boolean PMM(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX <= var4 && this.originY >= var2 && this.originZ >= var3 && this.s_xy * var4 - var5 + this.c_xy <= 0.0F && this.s_yx * var2 - var1 + this.c_yx >= 0.0F && this.s_zy * var3 - var5 + this.c_zy <= 0.0F && this.s_yz * var2 - var6 + this.c_yz <= 0.0F && this.s_xz * var4 - var6 + this.c_xz <= 0.0F && this.s_zx * var3 - var1 + this.c_zx >= 0.0F;
   }

   private boolean MOM(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originY >= var2 && this.originY <= var5 && this.originX >= var1 && this.originZ >= var3 && this.s_xz * var1 - var6 + this.c_xz <= 0.0F && this.s_zx * var3 - var4 + this.c_zx <= 0.0F;
   }

   private boolean OOM(float var1, float var2, float var3, float var4, float var5) {
      return this.originZ >= var3 && this.originX >= var1 && this.originX <= var4 && this.originY >= var2 && this.originY <= var5;
   }

   private boolean POM(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originY >= var2 && this.originY <= var5 && this.originX <= var4 && this.originZ >= var3 && this.s_xz * var4 - var6 + this.c_xz <= 0.0F && this.s_zx * var3 - var1 + this.c_zx >= 0.0F;
   }

   private boolean MPM(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX >= var1 && this.originY <= var5 && this.originZ >= var3 && this.s_xy * var1 - var2 + this.c_xy >= 0.0F && this.s_yx * var5 - var4 + this.c_yx <= 0.0F && this.s_zy * var3 - var2 + this.c_zy >= 0.0F && this.s_yz * var5 - var6 + this.c_yz <= 0.0F && this.s_xz * var1 - var6 + this.c_xz <= 0.0F && this.s_zx * var3 - var4 + this.c_zx <= 0.0F;
   }

   private boolean OPM(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX >= var1 && this.originX <= var4 && this.originY <= var5 && this.originZ >= var3 && this.s_zy * var3 - var2 + this.c_zy >= 0.0F && this.s_yz * var5 - var6 + this.c_yz <= 0.0F;
   }

   private boolean PPM(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX <= var4 && this.originY <= var5 && this.originZ >= var3 && this.s_xy * var4 - var2 + this.c_xy >= 0.0F && this.s_yx * var5 - var1 + this.c_yx >= 0.0F && this.s_zy * var3 - var2 + this.c_zy >= 0.0F && this.s_yz * var5 - var6 + this.c_yz <= 0.0F && this.s_xz * var4 - var6 + this.c_xz <= 0.0F && this.s_zx * var3 - var1 + this.c_zx >= 0.0F;
   }

   private boolean MMO(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originZ >= var3 && this.originZ <= var6 && this.originX >= var1 && this.originY >= var2 && this.s_xy * var1 - var5 + this.c_xy <= 0.0F && this.s_yx * var2 - var4 + this.c_yx <= 0.0F;
   }

   private boolean OMO(float var1, float var2, float var3, float var4, float var5) {
      return this.originY >= var2 && this.originX >= var1 && this.originX <= var4 && this.originZ >= var3 && this.originZ <= var5;
   }

   private boolean PMO(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originZ >= var3 && this.originZ <= var6 && this.originX <= var4 && this.originY >= var2 && this.s_xy * var4 - var5 + this.c_xy <= 0.0F && this.s_yx * var2 - var1 + this.c_yx >= 0.0F;
   }

   private boolean MOO(float var1, float var2, float var3, float var4, float var5) {
      return this.originX >= var1 && this.originY >= var2 && this.originY <= var4 && this.originZ >= var3 && this.originZ <= var5;
   }

   private boolean POO(float var1, float var2, float var3, float var4, float var5) {
      return this.originX <= var3 && this.originY >= var1 && this.originY <= var4 && this.originZ >= var2 && this.originZ <= var5;
   }

   private boolean MPO(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originZ >= var3 && this.originZ <= var6 && this.originX >= var1 && this.originY <= var5 && this.s_xy * var1 - var2 + this.c_xy >= 0.0F && this.s_yx * var5 - var4 + this.c_yx <= 0.0F;
   }

   private boolean OPO(float var1, float var2, float var3, float var4, float var5) {
      return this.originY <= var4 && this.originX >= var1 && this.originX <= var3 && this.originZ >= var2 && this.originZ <= var5;
   }

   private boolean PPO(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originZ >= var3 && this.originZ <= var6 && this.originX <= var4 && this.originY <= var5 && this.s_xy * var4 - var2 + this.c_xy >= 0.0F && this.s_yx * var5 - var1 + this.c_yx >= 0.0F;
   }

   private boolean MMP(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX >= var1 && this.originY >= var2 && this.originZ <= var6 && this.s_xy * var1 - var5 + this.c_xy <= 0.0F && this.s_yx * var2 - var4 + this.c_yx <= 0.0F && this.s_zy * var6 - var5 + this.c_zy <= 0.0F && this.s_yz * var2 - var3 + this.c_yz >= 0.0F && this.s_xz * var1 - var3 + this.c_xz >= 0.0F && this.s_zx * var6 - var4 + this.c_zx <= 0.0F;
   }

   private boolean OMP(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX >= var1 && this.originX <= var4 && this.originY >= var2 && this.originZ <= var6 && this.s_zy * var6 - var5 + this.c_zy <= 0.0F && this.s_yz * var2 - var3 + this.c_yz >= 0.0F;
   }

   private boolean PMP(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX <= var4 && this.originY >= var2 && this.originZ <= var6 && this.s_xy * var4 - var5 + this.c_xy <= 0.0F && this.s_yx * var2 - var1 + this.c_yx >= 0.0F && this.s_zy * var6 - var5 + this.c_zy <= 0.0F && this.s_yz * var2 - var3 + this.c_yz >= 0.0F && this.s_xz * var4 - var3 + this.c_xz >= 0.0F && this.s_zx * var6 - var1 + this.c_zx >= 0.0F;
   }

   private boolean MOP(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originY >= var2 && this.originY <= var5 && this.originX >= var1 && this.originZ <= var6 && this.s_xz * var1 - var3 + this.c_xz >= 0.0F && this.s_zx * var6 - var4 + this.c_zx <= 0.0F;
   }

   private boolean OOP(float var1, float var2, float var3, float var4, float var5) {
      return this.originZ <= var5 && this.originX >= var1 && this.originX <= var3 && this.originY >= var2 && this.originY <= var4;
   }

   private boolean POP(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originY >= var2 && this.originY <= var5 && this.originX <= var4 && this.originZ <= var6 && this.s_xz * var4 - var3 + this.c_xz >= 0.0F && this.s_zx * var6 - var1 + this.c_zx <= 0.0F;
   }

   private boolean MPP(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX >= var1 && this.originY <= var5 && this.originZ <= var6 && this.s_xy * var1 - var2 + this.c_xy >= 0.0F && this.s_yx * var5 - var4 + this.c_yx <= 0.0F && this.s_zy * var6 - var2 + this.c_zy >= 0.0F && this.s_yz * var5 - var3 + this.c_yz >= 0.0F && this.s_xz * var1 - var3 + this.c_xz >= 0.0F && this.s_zx * var6 - var4 + this.c_zx <= 0.0F;
   }

   private boolean OPP(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX >= var1 && this.originX <= var4 && this.originY <= var5 && this.originZ <= var6 && this.s_zy * var6 - var2 + this.c_zy <= 0.0F && this.s_yz * var5 - var3 + this.c_yz <= 0.0F;
   }

   private boolean PPP(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.originX <= var4 && this.originY <= var5 && this.originZ <= var6 && this.s_xy * var4 - var2 + this.c_xy >= 0.0F && this.s_yx * var5 - var1 + this.c_yx >= 0.0F && this.s_zy * var6 - var2 + this.c_zy >= 0.0F && this.s_yz * var5 - var3 + this.c_yz >= 0.0F && this.s_xz * var4 - var3 + this.c_xz >= 0.0F && this.s_zx * var6 - var1 + this.c_zx >= 0.0F;
   }
}
