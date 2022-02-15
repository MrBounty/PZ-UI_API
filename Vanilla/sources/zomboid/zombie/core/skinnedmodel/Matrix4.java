package zombie.core.skinnedmodel;

import java.nio.FloatBuffer;
import org.lwjglx.BufferUtils;

public class Matrix4 {
   private FloatBuffer matrix;
   public static Matrix4 Identity = new Matrix4();
   private FloatBuffer direct;

   public Matrix4() {
      this.matrix = FloatBuffer.allocate(16);
   }

   public Matrix4(float[] var1) {
      this();
      this.put(var1);
   }

   public Matrix4(Matrix4 var1) {
      this();
      this.put(var1);
   }

   public Matrix4 clear() {
      for(int var1 = 0; var1 < 16; ++var1) {
         this.matrix.put(var1, 0.0F);
      }

      return this;
   }

   public Matrix4 clearToIdentity() {
      return this.clear().put(0, 1.0F).put(5, 1.0F).put(10, 1.0F).put(15, 1.0F);
   }

   public Matrix4 clearToOrtho(float var1, float var2, float var3, float var4, float var5, float var6) {
      return this.clear().put(0, 2.0F / (var2 - var1)).put(5, 2.0F / (var4 - var3)).put(10, -2.0F / (var6 - var5)).put(12, -(var2 + var1) / (var2 - var1)).put(13, -(var4 + var3) / (var4 - var3)).put(14, -(var6 + var5) / (var6 - var5)).put(15, 1.0F);
   }

   public Matrix4 clearToPerspective(float var1, float var2, float var3, float var4, float var5) {
      float var6 = 1.0F / (float)Math.tan((double)(var1 / 2.0F));
      return this.clear().put(0, var6 / (var2 / var3)).put(5, var6).put(10, (var5 + var4) / (var4 - var5)).put(14, 2.0F * var5 * var4 / (var4 - var5)).put(11, -1.0F);
   }

   public float get(int var1) {
      return this.matrix.get(var1);
   }

   public Matrix4 put(int var1, float var2) {
      this.matrix.put(var1, var2);
      return this;
   }

   public Matrix4 put(int var1, Vector3 var2, float var3) {
      this.put(var1 * 4 + 0, var2.x());
      this.put(var1 * 4 + 1, var2.y());
      this.put(var1 * 4 + 2, var2.z());
      this.put(var1 * 4 + 3, var3);
      return this;
   }

   public Matrix4 put(float[] var1) {
      if (var1.length < 16) {
         throw new IllegalArgumentException("float array must have at least 16 values.");
      } else {
         this.matrix.position(0);
         this.matrix.put(var1, 0, 16);
         return this;
      }
   }

   public Matrix4 put(Matrix4 var1) {
      FloatBuffer var2 = var1.getBuffer();

      while(var2.hasRemaining()) {
         this.matrix.put(var2.get());
      }

      return this;
   }

   public Matrix4 mult(float[] var1) {
      float[] var2 = new float[16];

      for(int var3 = 0; var3 < 16; var3 += 4) {
         var2[var3 + 0] = this.get(0) * var1[var3] + this.get(4) * var1[var3 + 1] + this.get(8) * var1[var3 + 2] + this.get(12) * var1[var3 + 3];
         var2[var3 + 1] = this.get(1) * var1[var3] + this.get(5) * var1[var3 + 1] + this.get(9) * var1[var3 + 2] + this.get(13) * var1[var3 + 3];
         var2[var3 + 2] = this.get(2) * var1[var3] + this.get(6) * var1[var3 + 1] + this.get(10) * var1[var3 + 2] + this.get(14) * var1[var3 + 3];
         var2[var3 + 3] = this.get(3) * var1[var3] + this.get(7) * var1[var3 + 1] + this.get(11) * var1[var3 + 2] + this.get(15) * var1[var3 + 3];
      }

      this.put(var2);
      return this;
   }

   public Matrix4 mult(Matrix4 var1) {
      float[] var2 = new float[16];

      for(int var3 = 0; var3 < 16; var3 += 4) {
         var2[var3 + 0] = this.get(0) * var1.get(var3) + this.get(4) * var1.get(var3 + 1) + this.get(8) * var1.get(var3 + 2) + this.get(12) * var1.get(var3 + 3);
         var2[var3 + 1] = this.get(1) * var1.get(var3) + this.get(5) * var1.get(var3 + 1) + this.get(9) * var1.get(var3 + 2) + this.get(13) * var1.get(var3 + 3);
         var2[var3 + 2] = this.get(2) * var1.get(var3) + this.get(6) * var1.get(var3 + 1) + this.get(10) * var1.get(var3 + 2) + this.get(14) * var1.get(var3 + 3);
         var2[var3 + 3] = this.get(3) * var1.get(var3) + this.get(7) * var1.get(var3 + 1) + this.get(11) * var1.get(var3 + 2) + this.get(15) * var1.get(var3 + 3);
      }

      this.put(var2);
      return this;
   }

   public Matrix4 transpose() {
      float var1 = this.get(1);
      this.put(1, this.get(4));
      this.put(4, var1);
      var1 = this.get(2);
      this.put(2, this.get(8));
      this.put(8, var1);
      var1 = this.get(3);
      this.put(3, this.get(12));
      this.put(12, var1);
      var1 = this.get(7);
      this.put(7, this.get(13));
      this.put(13, var1);
      var1 = this.get(11);
      this.put(11, this.get(14));
      this.put(14, var1);
      var1 = this.get(6);
      this.put(6, this.get(9));
      this.put(9, var1);
      return this;
   }

   public Matrix4 translate(float var1, float var2, float var3) {
      float[] var4 = new float[16];
      var4[0] = 1.0F;
      var4[5] = 1.0F;
      var4[10] = 1.0F;
      var4[15] = 1.0F;
      var4[12] = var1;
      var4[13] = var2;
      var4[14] = var3;
      return this.mult(var4);
   }

   public Matrix4 translate(Vector3 var1) {
      return this.translate(var1.x(), var1.y(), var1.z());
   }

   public Matrix4 scale(float var1, float var2, float var3) {
      float[] var4 = new float[16];
      var4[0] = var1;
      var4[5] = var2;
      var4[10] = var3;
      var4[15] = 1.0F;
      return this.mult(var4);
   }

   public Matrix4 scale(Vector3 var1) {
      return this.scale(var1.x(), var1.y(), var1.z());
   }

   public Matrix4 rotate(float var1, float var2, float var3, float var4) {
      float var5 = (float)Math.cos((double)var1);
      float var6 = (float)Math.sin((double)var1);
      float var7 = 1.0F - var5;
      Vector3 var8 = (new Vector3(var2, var3, var4)).normalize();
      float[] var9 = new float[16];
      var9[0] = var8.x() * var8.x() + (1.0F - var8.x() * var8.x()) * var5;
      var9[4] = var8.x() * var8.y() * var7 - var8.z() * var6;
      var9[8] = var8.x() * var8.z() * var7 + var8.y() * var6;
      var9[1] = var8.y() * var8.x() * var7 + var8.z() * var6;
      var9[5] = var8.y() * var8.y() + (1.0F - var8.y() * var8.y()) * var5;
      var9[9] = var8.y() * var8.z() * var7 - var8.x() * var6;
      var9[2] = var8.z() * var8.x() * var7 - var8.y() * var6;
      var9[6] = var8.z() * var8.y() * var7 + var8.x() * var6;
      var9[10] = var8.z() * var8.z() + (1.0F - var8.z() * var8.z()) * var5;
      var9[15] = 1.0F;
      return this.mult(var9);
   }

   public Matrix4 rotate(float var1, Vector3 var2) {
      return this.rotate(var1, var2.x(), var2.y(), var2.z());
   }

   public FloatBuffer getBuffer() {
      if (this.direct == null) {
         this.direct = BufferUtils.createFloatBuffer(16);
      }

      this.direct.clear();
      this.direct.put(this.matrix.position(16).flip());
      this.direct.flip();
      return this.direct;
   }

   static {
      Identity.clearToIdentity();
   }
}
