package de.jarnbjo.vorbis;

class MdctFloat {
   private static final float cPI3_8 = 0.38268343F;
   private static final float cPI2_8 = 0.70710677F;
   private static final float cPI1_8 = 0.9238795F;
   private int n;
   private int log2n;
   private float[] trig;
   private int[] bitrev;
   private float[] equalizer;
   private float scale;
   private int itmp1;
   private int itmp2;
   private int itmp3;
   private int itmp4;
   private int itmp5;
   private int itmp6;
   private int itmp7;
   private int itmp8;
   private int itmp9;
   private float dtmp1;
   private float dtmp2;
   private float dtmp3;
   private float dtmp4;
   private float dtmp5;
   private float dtmp6;
   private float dtmp7;
   private float dtmp8;
   private float dtmp9;
   private float[] _x = new float[1024];
   private float[] _w = new float[1024];

   protected MdctFloat(int var1) {
      this.bitrev = new int[var1 / 4];
      this.trig = new float[var1 + var1 / 4];
      int var2 = var1 >>> 1;
      this.log2n = (int)Math.rint(Math.log((double)var1) / Math.log(2.0D));
      this.n = var1;
      byte var3 = 0;
      byte var4 = 1;
      int var5 = var3 + var1 / 2;
      int var6 = var5 + 1;
      int var7 = var5 + var1 / 2;
      int var8 = var7 + 1;

      int var9;
      for(var9 = 0; var9 < var1 / 4; ++var9) {
         this.trig[var3 + var9 * 2] = (float)Math.cos(3.141592653589793D / (double)var1 * (double)(4 * var9));
         this.trig[var4 + var9 * 2] = (float)(-Math.sin(3.141592653589793D / (double)var1 * (double)(4 * var9)));
         this.trig[var5 + var9 * 2] = (float)Math.cos(3.141592653589793D / (double)(2 * var1) * (double)(2 * var9 + 1));
         this.trig[var6 + var9 * 2] = (float)Math.sin(3.141592653589793D / (double)(2 * var1) * (double)(2 * var9 + 1));
      }

      for(var9 = 0; var9 < var1 / 8; ++var9) {
         this.trig[var7 + var9 * 2] = (float)Math.cos(3.141592653589793D / (double)var1 * (double)(4 * var9 + 2));
         this.trig[var8 + var9 * 2] = (float)(-Math.sin(3.141592653589793D / (double)var1 * (double)(4 * var9 + 2)));
      }

      var9 = (1 << this.log2n - 1) - 1;
      int var10 = 1 << this.log2n - 2;

      for(int var11 = 0; var11 < var1 / 8; ++var11) {
         int var12 = 0;

         for(int var13 = 0; var10 >>> var13 != 0; ++var13) {
            if ((var10 >>> var13 & var11) != 0) {
               var12 |= 1 << var13;
            }
         }

         this.bitrev[var11 * 2] = ~var12 & var9;
         this.bitrev[var11 * 2 + 1] = var12;
      }

      this.scale = 4.0F / (float)var1;
   }

   protected void setEqualizer(float[] var1) {
      this.equalizer = var1;
   }

   protected float[] getEqualizer() {
      return this.equalizer;
   }

   protected synchronized void imdct(float[] var1, float[] var2, int[] var3) {
      float[] var4 = var1;
      if (this._x.length < this.n / 2) {
         this._x = new float[this.n / 2];
      }

      if (this._w.length < this.n / 2) {
         this._w = new float[this.n / 2];
      }

      float[] var5 = this._x;
      float[] var6 = this._w;
      int var7 = this.n >> 1;
      int var8 = this.n >> 2;
      int var9 = this.n >> 3;
      int var10;
      if (this.equalizer != null) {
         for(var10 = 0; var10 < this.n; ++var10) {
            var1[var10] *= this.equalizer[var10];
         }
      }

      var10 = -1;
      int var11 = 0;
      int var12 = var7;

      int var13;
      for(var13 = 0; var13 < var9; ++var13) {
         var10 += 2;
         this.dtmp1 = var4[var10];
         var10 += 2;
         this.dtmp2 = var4[var10];
         --var12;
         this.dtmp3 = this.trig[var12];
         --var12;
         this.dtmp4 = this.trig[var12];
         var5[var11++] = -this.dtmp2 * this.dtmp3 - this.dtmp1 * this.dtmp4;
         var5[var11++] = this.dtmp1 * this.dtmp3 - this.dtmp2 * this.dtmp4;
      }

      var10 = var7;

      for(var13 = 0; var13 < var9; ++var13) {
         var10 -= 2;
         this.dtmp1 = var4[var10];
         var10 -= 2;
         this.dtmp2 = var4[var10];
         --var12;
         this.dtmp3 = this.trig[var12];
         --var12;
         this.dtmp4 = this.trig[var12];
         var5[var11++] = this.dtmp2 * this.dtmp3 + this.dtmp1 * this.dtmp4;
         var5[var11++] = this.dtmp2 * this.dtmp4 - this.dtmp1 * this.dtmp3;
      }

      float[] var20 = this.kernel(var5, var6, this.n, var7, var8, var9);
      var11 = 0;
      var12 = var7;
      var13 = var8;
      int var14 = var8 - 1;
      int var15 = var8 + var7;
      int var16 = var15 - 1;

      for(int var17 = 0; var17 < var8; ++var17) {
         this.dtmp1 = var20[var11++];
         this.dtmp2 = var20[var11++];
         this.dtmp3 = this.trig[var12++];
         this.dtmp4 = this.trig[var12++];
         float var18 = this.dtmp1 * this.dtmp4 - this.dtmp2 * this.dtmp3;
         float var19 = -(this.dtmp1 * this.dtmp3 + this.dtmp2 * this.dtmp4);
         var3[var13] = (int)(-var18 * var2[var13]);
         var3[var14] = (int)(var18 * var2[var14]);
         var3[var15] = (int)(var19 * var2[var15]);
         var3[var16] = (int)(var19 * var2[var16]);
         ++var13;
         --var14;
         ++var15;
         --var16;
      }

   }

   private float[] kernel(float[] var1, float[] var2, int var3, int var4, int var5, int var6) {
      int var7 = var5;
      int var8 = 0;
      int var9 = var5;
      int var10 = var4;

      int var11;
      for(var11 = 0; var11 < var5; ++var11) {
         float var12 = var1[var7] - var1[var8];
         var2[var9 + var11] = var1[var7++] + var1[var8++];
         float var13 = var1[var7] - var1[var8];
         var10 -= 4;
         var2[var11++] = var12 * this.trig[var10] + var13 * this.trig[var10 + 1];
         var2[var11] = var13 * this.trig[var10] - var12 * this.trig[var10 + 1];
         var2[var9 + var11] = var1[var7++] + var1[var8++];
      }

      int var14;
      int var16;
      int var17;
      float var18;
      float var19;
      float var20;
      float var21;
      int var26;
      int var27;
      for(var11 = 0; var11 < this.log2n - 3; ++var11) {
         var26 = var3 >>> var11 + 2;
         var27 = 1 << var11 + 3;
         var14 = var4 - 2;
         var10 = 0;

         for(var16 = 0; var16 < var26 >>> 2; ++var16) {
            var17 = var14;
            var9 = var14 - (var26 >> 1);
            var18 = this.trig[var10];
            var20 = this.trig[var10 + 1];
            var14 -= 2;
            ++var26;

            for(int var22 = 0; var22 < 2 << var11; ++var22) {
               this.dtmp1 = var2[var17];
               this.dtmp2 = var2[var9];
               var21 = this.dtmp1 - this.dtmp2;
               var1[var17] = this.dtmp1 + this.dtmp2;
               ++var17;
               this.dtmp1 = var2[var17];
               ++var9;
               this.dtmp2 = var2[var9];
               var19 = this.dtmp1 - this.dtmp2;
               var1[var17] = this.dtmp1 + this.dtmp2;
               var1[var9] = var19 * var18 - var21 * var20;
               var1[var9 - 1] = var21 * var18 + var19 * var20;
               var17 -= var26;
               var9 -= var26;
            }

            --var26;
            var10 += var27;
         }

         float[] var15 = var2;
         var2 = var1;
         var1 = var15;
      }

      var11 = var3;
      var26 = 0;
      var27 = 0;
      var14 = var4 - 1;

      for(int var28 = 0; var28 < var6; ++var28) {
         var16 = this.bitrev[var26++];
         var17 = this.bitrev[var26++];
         var18 = var2[var16] - var2[var17 + 1];
         var19 = var2[var16 - 1] + var2[var17];
         var20 = var2[var16] + var2[var17 + 1];
         var21 = var2[var16 - 1] - var2[var17];
         float var30 = var18 * this.trig[var11];
         float var23 = var19 * this.trig[var11++];
         float var24 = var18 * this.trig[var11];
         float var25 = var19 * this.trig[var11++];
         var1[var27++] = (var20 + var24 + var23) * 16383.0F;
         var1[var14--] = (-var21 + var25 - var30) * 16383.0F;
         var1[var27++] = (var21 + var25 - var30) * 16383.0F;
         var1[var14--] = (var20 - var24 - var23) * 16383.0F;
      }

      return var1;
   }
}
