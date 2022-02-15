package org.joml;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import sun.misc.Unsafe;

abstract class MemUtil {
   public static final MemUtil INSTANCE = createInstance();

   private static MemUtil createInstance() {
      Object var0;
      try {
         if (Options.NO_UNSAFE && Options.FORCE_UNSAFE) {
            throw new ConfigurationException("Cannot enable both -Djoml.nounsafe and -Djoml.forceUnsafe", (Throwable)null);
         }

         if (Options.NO_UNSAFE) {
            var0 = new MemUtil.MemUtilNIO();
         } else {
            var0 = new MemUtil.MemUtilUnsafe();
         }
      } catch (Throwable var2) {
         if (Options.FORCE_UNSAFE) {
            throw new ConfigurationException("Unsafe is not supported but its use was forced via -Djoml.forceUnsafe", var2);
         }

         var0 = new MemUtil.MemUtilNIO();
      }

      return (MemUtil)var0;
   }

   public abstract void put(Matrix4f var1, int var2, FloatBuffer var3);

   public abstract void put(Matrix4f var1, int var2, ByteBuffer var3);

   public abstract void put(Matrix4x3f var1, int var2, FloatBuffer var3);

   public abstract void put(Matrix4x3f var1, int var2, ByteBuffer var3);

   public abstract void put4x4(Matrix4x3f var1, int var2, FloatBuffer var3);

   public abstract void put4x4(Matrix4x3f var1, int var2, ByteBuffer var3);

   public abstract void put4x4(Matrix4x3d var1, int var2, DoubleBuffer var3);

   public abstract void put4x4(Matrix4x3d var1, int var2, ByteBuffer var3);

   public abstract void put4x4(Matrix3x2f var1, int var2, FloatBuffer var3);

   public abstract void put4x4(Matrix3x2f var1, int var2, ByteBuffer var3);

   public abstract void put4x4(Matrix3x2d var1, int var2, DoubleBuffer var3);

   public abstract void put4x4(Matrix3x2d var1, int var2, ByteBuffer var3);

   public abstract void put3x3(Matrix3x2f var1, int var2, FloatBuffer var3);

   public abstract void put3x3(Matrix3x2f var1, int var2, ByteBuffer var3);

   public abstract void put3x3(Matrix3x2d var1, int var2, DoubleBuffer var3);

   public abstract void put3x3(Matrix3x2d var1, int var2, ByteBuffer var3);

   public abstract void put4x3(Matrix4f var1, int var2, FloatBuffer var3);

   public abstract void put4x3(Matrix4f var1, int var2, ByteBuffer var3);

   public abstract void put3x4(Matrix4f var1, int var2, FloatBuffer var3);

   public abstract void put3x4(Matrix4f var1, int var2, ByteBuffer var3);

   public abstract void put3x4(Matrix4x3f var1, int var2, FloatBuffer var3);

   public abstract void put3x4(Matrix4x3f var1, int var2, ByteBuffer var3);

   public abstract void put3x4(Matrix3f var1, int var2, FloatBuffer var3);

   public abstract void put3x4(Matrix3f var1, int var2, ByteBuffer var3);

   public abstract void putTransposed(Matrix4f var1, int var2, FloatBuffer var3);

   public abstract void putTransposed(Matrix4f var1, int var2, ByteBuffer var3);

   public abstract void put4x3Transposed(Matrix4f var1, int var2, FloatBuffer var3);

   public abstract void put4x3Transposed(Matrix4f var1, int var2, ByteBuffer var3);

   public abstract void putTransposed(Matrix4x3f var1, int var2, FloatBuffer var3);

   public abstract void putTransposed(Matrix4x3f var1, int var2, ByteBuffer var3);

   public abstract void putTransposed(Matrix3f var1, int var2, FloatBuffer var3);

   public abstract void putTransposed(Matrix3f var1, int var2, ByteBuffer var3);

   public abstract void putTransposed(Matrix2f var1, int var2, FloatBuffer var3);

   public abstract void putTransposed(Matrix2f var1, int var2, ByteBuffer var3);

   public abstract void put(Matrix4d var1, int var2, DoubleBuffer var3);

   public abstract void put(Matrix4d var1, int var2, ByteBuffer var3);

   public abstract void put(Matrix4x3d var1, int var2, DoubleBuffer var3);

   public abstract void put(Matrix4x3d var1, int var2, ByteBuffer var3);

   public abstract void putf(Matrix4d var1, int var2, FloatBuffer var3);

   public abstract void putf(Matrix4d var1, int var2, ByteBuffer var3);

   public abstract void putf(Matrix4x3d var1, int var2, FloatBuffer var3);

   public abstract void putf(Matrix4x3d var1, int var2, ByteBuffer var3);

   public abstract void putTransposed(Matrix4d var1, int var2, DoubleBuffer var3);

   public abstract void putTransposed(Matrix4d var1, int var2, ByteBuffer var3);

   public abstract void put4x3Transposed(Matrix4d var1, int var2, DoubleBuffer var3);

   public abstract void put4x3Transposed(Matrix4d var1, int var2, ByteBuffer var3);

   public abstract void putTransposed(Matrix4x3d var1, int var2, DoubleBuffer var3);

   public abstract void putTransposed(Matrix4x3d var1, int var2, ByteBuffer var3);

   public abstract void putTransposed(Matrix2d var1, int var2, DoubleBuffer var3);

   public abstract void putTransposed(Matrix2d var1, int var2, ByteBuffer var3);

   public abstract void putfTransposed(Matrix4d var1, int var2, FloatBuffer var3);

   public abstract void putfTransposed(Matrix4d var1, int var2, ByteBuffer var3);

   public abstract void putfTransposed(Matrix4x3d var1, int var2, FloatBuffer var3);

   public abstract void putfTransposed(Matrix4x3d var1, int var2, ByteBuffer var3);

   public abstract void putfTransposed(Matrix2d var1, int var2, FloatBuffer var3);

   public abstract void putfTransposed(Matrix2d var1, int var2, ByteBuffer var3);

   public abstract void put(Matrix3f var1, int var2, FloatBuffer var3);

   public abstract void put(Matrix3f var1, int var2, ByteBuffer var3);

   public abstract void put(Matrix3d var1, int var2, DoubleBuffer var3);

   public abstract void put(Matrix3d var1, int var2, ByteBuffer var3);

   public abstract void putf(Matrix3d var1, int var2, FloatBuffer var3);

   public abstract void putf(Matrix3d var1, int var2, ByteBuffer var3);

   public abstract void put(Matrix3x2f var1, int var2, FloatBuffer var3);

   public abstract void put(Matrix3x2f var1, int var2, ByteBuffer var3);

   public abstract void put(Matrix3x2d var1, int var2, DoubleBuffer var3);

   public abstract void put(Matrix3x2d var1, int var2, ByteBuffer var3);

   public abstract void put(Matrix2f var1, int var2, FloatBuffer var3);

   public abstract void put(Matrix2f var1, int var2, ByteBuffer var3);

   public abstract void put(Matrix2d var1, int var2, DoubleBuffer var3);

   public abstract void put(Matrix2d var1, int var2, ByteBuffer var3);

   public abstract void putf(Matrix2d var1, int var2, FloatBuffer var3);

   public abstract void putf(Matrix2d var1, int var2, ByteBuffer var3);

   public abstract void put(Vector4d var1, int var2, DoubleBuffer var3);

   public abstract void put(Vector4d var1, int var2, FloatBuffer var3);

   public abstract void put(Vector4d var1, int var2, ByteBuffer var3);

   public abstract void putf(Vector4d var1, int var2, ByteBuffer var3);

   public abstract void put(Vector4f var1, int var2, FloatBuffer var3);

   public abstract void put(Vector4f var1, int var2, ByteBuffer var3);

   public abstract void put(Vector4i var1, int var2, IntBuffer var3);

   public abstract void put(Vector4i var1, int var2, ByteBuffer var3);

   public abstract void put(Vector3f var1, int var2, FloatBuffer var3);

   public abstract void put(Vector3f var1, int var2, ByteBuffer var3);

   public abstract void put(Vector3d var1, int var2, DoubleBuffer var3);

   public abstract void put(Vector3d var1, int var2, FloatBuffer var3);

   public abstract void put(Vector3d var1, int var2, ByteBuffer var3);

   public abstract void putf(Vector3d var1, int var2, ByteBuffer var3);

   public abstract void put(Vector3i var1, int var2, IntBuffer var3);

   public abstract void put(Vector3i var1, int var2, ByteBuffer var3);

   public abstract void put(Vector2f var1, int var2, FloatBuffer var3);

   public abstract void put(Vector2f var1, int var2, ByteBuffer var3);

   public abstract void put(Vector2d var1, int var2, DoubleBuffer var3);

   public abstract void put(Vector2d var1, int var2, ByteBuffer var3);

   public abstract void put(Vector2i var1, int var2, IntBuffer var3);

   public abstract void put(Vector2i var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix4f var1, int var2, FloatBuffer var3);

   public abstract void get(Matrix4f var1, int var2, ByteBuffer var3);

   public abstract void getTransposed(Matrix4f var1, int var2, FloatBuffer var3);

   public abstract void getTransposed(Matrix4f var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix4x3f var1, int var2, FloatBuffer var3);

   public abstract void get(Matrix4x3f var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix4d var1, int var2, DoubleBuffer var3);

   public abstract void get(Matrix4d var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix4x3d var1, int var2, DoubleBuffer var3);

   public abstract void get(Matrix4x3d var1, int var2, ByteBuffer var3);

   public abstract void getf(Matrix4d var1, int var2, FloatBuffer var3);

   public abstract void getf(Matrix4d var1, int var2, ByteBuffer var3);

   public abstract void getf(Matrix4x3d var1, int var2, FloatBuffer var3);

   public abstract void getf(Matrix4x3d var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix3f var1, int var2, FloatBuffer var3);

   public abstract void get(Matrix3f var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix3d var1, int var2, DoubleBuffer var3);

   public abstract void get(Matrix3d var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix3x2f var1, int var2, FloatBuffer var3);

   public abstract void get(Matrix3x2f var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix3x2d var1, int var2, DoubleBuffer var3);

   public abstract void get(Matrix3x2d var1, int var2, ByteBuffer var3);

   public abstract void getf(Matrix3d var1, int var2, FloatBuffer var3);

   public abstract void getf(Matrix3d var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix2f var1, int var2, FloatBuffer var3);

   public abstract void get(Matrix2f var1, int var2, ByteBuffer var3);

   public abstract void get(Matrix2d var1, int var2, DoubleBuffer var3);

   public abstract void get(Matrix2d var1, int var2, ByteBuffer var3);

   public abstract void getf(Matrix2d var1, int var2, FloatBuffer var3);

   public abstract void getf(Matrix2d var1, int var2, ByteBuffer var3);

   public abstract void get(Vector4d var1, int var2, DoubleBuffer var3);

   public abstract void get(Vector4d var1, int var2, ByteBuffer var3);

   public abstract void get(Vector4f var1, int var2, FloatBuffer var3);

   public abstract void get(Vector4f var1, int var2, ByteBuffer var3);

   public abstract void get(Vector4i var1, int var2, IntBuffer var3);

   public abstract void get(Vector4i var1, int var2, ByteBuffer var3);

   public abstract void get(Vector3f var1, int var2, FloatBuffer var3);

   public abstract void get(Vector3f var1, int var2, ByteBuffer var3);

   public abstract void get(Vector3d var1, int var2, DoubleBuffer var3);

   public abstract void get(Vector3d var1, int var2, ByteBuffer var3);

   public abstract void get(Vector3i var1, int var2, IntBuffer var3);

   public abstract void get(Vector3i var1, int var2, ByteBuffer var3);

   public abstract void get(Vector2f var1, int var2, FloatBuffer var3);

   public abstract void get(Vector2f var1, int var2, ByteBuffer var3);

   public abstract void get(Vector2d var1, int var2, DoubleBuffer var3);

   public abstract void get(Vector2d var1, int var2, ByteBuffer var3);

   public abstract void get(Vector2i var1, int var2, IntBuffer var3);

   public abstract void get(Vector2i var1, int var2, ByteBuffer var3);

   public abstract void putMatrix3f(Quaternionf var1, int var2, ByteBuffer var3);

   public abstract void putMatrix3f(Quaternionf var1, int var2, FloatBuffer var3);

   public abstract void putMatrix4f(Quaternionf var1, int var2, ByteBuffer var3);

   public abstract void putMatrix4f(Quaternionf var1, int var2, FloatBuffer var3);

   public abstract void putMatrix4x3f(Quaternionf var1, int var2, ByteBuffer var3);

   public abstract void putMatrix4x3f(Quaternionf var1, int var2, FloatBuffer var3);

   public abstract float get(Matrix4f var1, int var2, int var3);

   public abstract Matrix4f set(Matrix4f var1, int var2, int var3, float var4);

   public abstract double get(Matrix4d var1, int var2, int var3);

   public abstract Matrix4d set(Matrix4d var1, int var2, int var3, double var4);

   public abstract float get(Matrix3f var1, int var2, int var3);

   public abstract Matrix3f set(Matrix3f var1, int var2, int var3, float var4);

   public abstract double get(Matrix3d var1, int var2, int var3);

   public abstract Matrix3d set(Matrix3d var1, int var2, int var3, double var4);

   public abstract Vector4f getColumn(Matrix4f var1, int var2, Vector4f var3);

   public abstract Matrix4f setColumn(Vector4f var1, int var2, Matrix4f var3);

   public abstract Matrix4f setColumn(Vector4fc var1, int var2, Matrix4f var3);

   public abstract void copy(Matrix4f var1, Matrix4f var2);

   public abstract void copy(Matrix4x3f var1, Matrix4x3f var2);

   public abstract void copy(Matrix4f var1, Matrix4x3f var2);

   public abstract void copy(Matrix4x3f var1, Matrix4f var2);

   public abstract void copy(Matrix3f var1, Matrix3f var2);

   public abstract void copy(Matrix3f var1, Matrix4f var2);

   public abstract void copy(Matrix4f var1, Matrix3f var2);

   public abstract void copy(Matrix3f var1, Matrix4x3f var2);

   public abstract void copy(Matrix3x2f var1, Matrix3x2f var2);

   public abstract void copy(Matrix3x2d var1, Matrix3x2d var2);

   public abstract void copy(Matrix2f var1, Matrix2f var2);

   public abstract void copy(Matrix2d var1, Matrix2d var2);

   public abstract void copy(Matrix2f var1, Matrix3f var2);

   public abstract void copy(Matrix3f var1, Matrix2f var2);

   public abstract void copy(Matrix2f var1, Matrix3x2f var2);

   public abstract void copy(Matrix3x2f var1, Matrix2f var2);

   public abstract void copy(Matrix2d var1, Matrix3d var2);

   public abstract void copy(Matrix3d var1, Matrix2d var2);

   public abstract void copy(Matrix2d var1, Matrix3x2d var2);

   public abstract void copy(Matrix3x2d var1, Matrix2d var2);

   public abstract void copy3x3(Matrix4f var1, Matrix4f var2);

   public abstract void copy3x3(Matrix4x3f var1, Matrix4x3f var2);

   public abstract void copy3x3(Matrix3f var1, Matrix4x3f var2);

   public abstract void copy3x3(Matrix3f var1, Matrix4f var2);

   public abstract void copy4x3(Matrix4f var1, Matrix4f var2);

   public abstract void copy4x3(Matrix4x3f var1, Matrix4f var2);

   public abstract void copy(float[] var1, int var2, Matrix4f var3);

   public abstract void copyTransposed(float[] var1, int var2, Matrix4f var3);

   public abstract void copy(float[] var1, int var2, Matrix3f var3);

   public abstract void copy(float[] var1, int var2, Matrix4x3f var3);

   public abstract void copy(float[] var1, int var2, Matrix3x2f var3);

   public abstract void copy(double[] var1, int var2, Matrix3x2d var3);

   public abstract void copy(float[] var1, int var2, Matrix2f var3);

   public abstract void copy(double[] var1, int var2, Matrix2d var3);

   public abstract void copy(Matrix4f var1, float[] var2, int var3);

   public abstract void copy(Matrix3f var1, float[] var2, int var3);

   public abstract void copy(Matrix4x3f var1, float[] var2, int var3);

   public abstract void copy(Matrix3x2f var1, float[] var2, int var3);

   public abstract void copy(Matrix3x2d var1, double[] var2, int var3);

   public abstract void copy(Matrix2f var1, float[] var2, int var3);

   public abstract void copy(Matrix2d var1, double[] var2, int var3);

   public abstract void copy4x4(Matrix4x3f var1, float[] var2, int var3);

   public abstract void copy4x4(Matrix4x3d var1, float[] var2, int var3);

   public abstract void copy4x4(Matrix4x3d var1, double[] var2, int var3);

   public abstract void copy4x4(Matrix3x2f var1, float[] var2, int var3);

   public abstract void copy4x4(Matrix3x2d var1, double[] var2, int var3);

   public abstract void copy3x3(Matrix3x2f var1, float[] var2, int var3);

   public abstract void copy3x3(Matrix3x2d var1, double[] var2, int var3);

   public abstract void identity(Matrix4f var1);

   public abstract void identity(Matrix4x3f var1);

   public abstract void identity(Matrix3f var1);

   public abstract void identity(Matrix3x2f var1);

   public abstract void identity(Matrix3x2d var1);

   public abstract void identity(Matrix2f var1);

   public abstract void swap(Matrix4f var1, Matrix4f var2);

   public abstract void swap(Matrix4x3f var1, Matrix4x3f var2);

   public abstract void swap(Matrix3f var1, Matrix3f var2);

   public abstract void swap(Matrix2f var1, Matrix2f var2);

   public abstract void swap(Matrix2d var1, Matrix2d var2);

   public abstract void zero(Matrix4f var1);

   public abstract void zero(Matrix4x3f var1);

   public abstract void zero(Matrix3f var1);

   public abstract void zero(Matrix3x2f var1);

   public abstract void zero(Matrix3x2d var1);

   public abstract void zero(Matrix2f var1);

   public abstract void zero(Matrix2d var1);

   public static class MemUtilNIO extends MemUtil {
      public void put0(Matrix4f var1, FloatBuffer var2) {
         var2.put(0, var1.m00()).put(1, var1.m01()).put(2, var1.m02()).put(3, var1.m03()).put(4, var1.m10()).put(5, var1.m11()).put(6, var1.m12()).put(7, var1.m13()).put(8, var1.m20()).put(9, var1.m21()).put(10, var1.m22()).put(11, var1.m23()).put(12, var1.m30()).put(13, var1.m31()).put(14, var1.m32()).put(15, var1.m33());
      }

      public void putN(Matrix4f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, var1.m03()).put(var2 + 4, var1.m10()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m12()).put(var2 + 7, var1.m13()).put(var2 + 8, var1.m20()).put(var2 + 9, var1.m21()).put(var2 + 10, var1.m22()).put(var2 + 11, var1.m23()).put(var2 + 12, var1.m30()).put(var2 + 13, var1.m31()).put(var2 + 14, var1.m32()).put(var2 + 15, var1.m33());
      }

      public void put(Matrix4f var1, int var2, FloatBuffer var3) {
         if (var2 == 0) {
            this.put0(var1, var3);
         } else {
            this.putN(var1, var2, var3);
         }

      }

      public void put0(Matrix4f var1, ByteBuffer var2) {
         var2.putFloat(0, var1.m00()).putFloat(4, var1.m01()).putFloat(8, var1.m02()).putFloat(12, var1.m03()).putFloat(16, var1.m10()).putFloat(20, var1.m11()).putFloat(24, var1.m12()).putFloat(28, var1.m13()).putFloat(32, var1.m20()).putFloat(36, var1.m21()).putFloat(40, var1.m22()).putFloat(44, var1.m23()).putFloat(48, var1.m30()).putFloat(52, var1.m31()).putFloat(56, var1.m32()).putFloat(60, var1.m33());
      }

      private void putN(Matrix4f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m02()).putFloat(var2 + 12, var1.m03()).putFloat(var2 + 16, var1.m10()).putFloat(var2 + 20, var1.m11()).putFloat(var2 + 24, var1.m12()).putFloat(var2 + 28, var1.m13()).putFloat(var2 + 32, var1.m20()).putFloat(var2 + 36, var1.m21()).putFloat(var2 + 40, var1.m22()).putFloat(var2 + 44, var1.m23()).putFloat(var2 + 48, var1.m30()).putFloat(var2 + 52, var1.m31()).putFloat(var2 + 56, var1.m32()).putFloat(var2 + 60, var1.m33());
      }

      public void put(Matrix4f var1, int var2, ByteBuffer var3) {
         if (var2 == 0) {
            this.put0(var1, var3);
         } else {
            this.putN(var1, var2, var3);
         }

      }

      public void put4x3_0(Matrix4f var1, FloatBuffer var2) {
         var2.put(0, var1.m00()).put(1, var1.m01()).put(2, var1.m02()).put(3, var1.m10()).put(4, var1.m11()).put(5, var1.m12()).put(6, var1.m20()).put(7, var1.m21()).put(8, var1.m22()).put(9, var1.m30()).put(10, var1.m31()).put(11, var1.m32());
      }

      public void put4x3_N(Matrix4f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, var1.m10()).put(var2 + 4, var1.m11()).put(var2 + 5, var1.m12()).put(var2 + 6, var1.m20()).put(var2 + 7, var1.m21()).put(var2 + 8, var1.m22()).put(var2 + 9, var1.m30()).put(var2 + 10, var1.m31()).put(var2 + 11, var1.m32());
      }

      public void put4x3(Matrix4f var1, int var2, FloatBuffer var3) {
         if (var2 == 0) {
            this.put4x3_0(var1, var3);
         } else {
            this.put4x3_N(var1, var2, var3);
         }

      }

      public void put4x3_0(Matrix4f var1, ByteBuffer var2) {
         var2.putFloat(0, var1.m00()).putFloat(4, var1.m01()).putFloat(8, var1.m02()).putFloat(12, var1.m10()).putFloat(16, var1.m11()).putFloat(20, var1.m12()).putFloat(24, var1.m20()).putFloat(28, var1.m21()).putFloat(32, var1.m22()).putFloat(36, var1.m30()).putFloat(40, var1.m31()).putFloat(44, var1.m32());
      }

      private void put4x3_N(Matrix4f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m02()).putFloat(var2 + 12, var1.m10()).putFloat(var2 + 16, var1.m11()).putFloat(var2 + 20, var1.m12()).putFloat(var2 + 24, var1.m20()).putFloat(var2 + 28, var1.m21()).putFloat(var2 + 32, var1.m22()).putFloat(var2 + 36, var1.m30()).putFloat(var2 + 40, var1.m31()).putFloat(var2 + 44, var1.m32());
      }

      public void put4x3(Matrix4f var1, int var2, ByteBuffer var3) {
         if (var2 == 0) {
            this.put4x3_0(var1, var3);
         } else {
            this.put4x3_N(var1, var2, var3);
         }

      }

      public void put3x4_0(Matrix4f var1, ByteBuffer var2) {
         var2.putFloat(0, var1.m00()).putFloat(4, var1.m01()).putFloat(8, var1.m02()).putFloat(12, var1.m03()).putFloat(16, var1.m10()).putFloat(20, var1.m11()).putFloat(24, var1.m12()).putFloat(28, var1.m13()).putFloat(32, var1.m20()).putFloat(36, var1.m21()).putFloat(40, var1.m22()).putFloat(44, var1.m23());
      }

      private void put3x4_N(Matrix4f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m02()).putFloat(var2 + 12, var1.m03()).putFloat(var2 + 16, var1.m10()).putFloat(var2 + 20, var1.m11()).putFloat(var2 + 24, var1.m12()).putFloat(var2 + 28, var1.m13()).putFloat(var2 + 32, var1.m20()).putFloat(var2 + 36, var1.m21()).putFloat(var2 + 40, var1.m22()).putFloat(var2 + 44, var1.m23());
      }

      public void put3x4(Matrix4f var1, int var2, ByteBuffer var3) {
         if (var2 == 0) {
            this.put3x4_0(var1, var3);
         } else {
            this.put3x4_N(var1, var2, var3);
         }

      }

      public void put3x4_0(Matrix4f var1, FloatBuffer var2) {
         var2.put(0, var1.m00()).put(1, var1.m01()).put(2, var1.m02()).put(3, var1.m03()).put(4, var1.m10()).put(5, var1.m11()).put(6, var1.m12()).put(7, var1.m13()).put(8, var1.m20()).put(9, var1.m21()).put(10, var1.m22()).put(11, var1.m23());
      }

      public void put3x4_N(Matrix4f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, var1.m03()).put(var2 + 4, var1.m10()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m12()).put(var2 + 7, var1.m13()).put(var2 + 8, var1.m20()).put(var2 + 9, var1.m21()).put(var2 + 10, var1.m22()).put(var2 + 11, var1.m23());
      }

      public void put3x4(Matrix4f var1, int var2, FloatBuffer var3) {
         if (var2 == 0) {
            this.put3x4_0(var1, var3);
         } else {
            this.put3x4_N(var1, var2, var3);
         }

      }

      public void put3x4_0(Matrix4x3f var1, ByteBuffer var2) {
         var2.putFloat(0, var1.m00()).putFloat(4, var1.m01()).putFloat(8, var1.m02()).putFloat(12, 0.0F).putFloat(16, var1.m10()).putFloat(20, var1.m11()).putFloat(24, var1.m12()).putFloat(28, 0.0F).putFloat(32, var1.m20()).putFloat(36, var1.m21()).putFloat(40, var1.m22()).putFloat(44, 0.0F);
      }

      private void put3x4_N(Matrix4x3f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m02()).putFloat(var2 + 12, 0.0F).putFloat(var2 + 16, var1.m10()).putFloat(var2 + 20, var1.m11()).putFloat(var2 + 24, var1.m12()).putFloat(var2 + 28, 0.0F).putFloat(var2 + 32, var1.m20()).putFloat(var2 + 36, var1.m21()).putFloat(var2 + 40, var1.m22()).putFloat(var2 + 44, 0.0F);
      }

      public void put3x4(Matrix4x3f var1, int var2, ByteBuffer var3) {
         if (var2 == 0) {
            this.put3x4_0(var1, var3);
         } else {
            this.put3x4_N(var1, var2, var3);
         }

      }

      public void put3x4_0(Matrix4x3f var1, FloatBuffer var2) {
         var2.put(0, var1.m00()).put(1, var1.m01()).put(2, var1.m02()).put(3, 0.0F).put(4, var1.m10()).put(5, var1.m11()).put(6, var1.m12()).put(7, 0.0F).put(8, var1.m20()).put(9, var1.m21()).put(10, var1.m22()).put(11, 0.0F);
      }

      public void put3x4_N(Matrix4x3f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, 0.0F).put(var2 + 4, var1.m10()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m12()).put(var2 + 7, 0.0F).put(var2 + 8, var1.m20()).put(var2 + 9, var1.m21()).put(var2 + 10, var1.m22()).put(var2 + 11, 0.0F);
      }

      public void put3x4(Matrix4x3f var1, int var2, FloatBuffer var3) {
         if (var2 == 0) {
            this.put3x4_0(var1, var3);
         } else {
            this.put3x4_N(var1, var2, var3);
         }

      }

      public void put0(Matrix4x3f var1, FloatBuffer var2) {
         var2.put(0, var1.m00()).put(1, var1.m01()).put(2, var1.m02()).put(3, var1.m10()).put(4, var1.m11()).put(5, var1.m12()).put(6, var1.m20()).put(7, var1.m21()).put(8, var1.m22()).put(9, var1.m30()).put(10, var1.m31()).put(11, var1.m32());
      }

      public void putN(Matrix4x3f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, var1.m10()).put(var2 + 4, var1.m11()).put(var2 + 5, var1.m12()).put(var2 + 6, var1.m20()).put(var2 + 7, var1.m21()).put(var2 + 8, var1.m22()).put(var2 + 9, var1.m30()).put(var2 + 10, var1.m31()).put(var2 + 11, var1.m32());
      }

      public void put(Matrix4x3f var1, int var2, FloatBuffer var3) {
         if (var2 == 0) {
            this.put0(var1, var3);
         } else {
            this.putN(var1, var2, var3);
         }

      }

      public void put0(Matrix4x3f var1, ByteBuffer var2) {
         var2.putFloat(0, var1.m00()).putFloat(4, var1.m01()).putFloat(8, var1.m02()).putFloat(12, var1.m10()).putFloat(16, var1.m11()).putFloat(20, var1.m12()).putFloat(24, var1.m20()).putFloat(28, var1.m21()).putFloat(32, var1.m22()).putFloat(36, var1.m30()).putFloat(40, var1.m31()).putFloat(44, var1.m32());
      }

      public void putN(Matrix4x3f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m02()).putFloat(var2 + 12, var1.m10()).putFloat(var2 + 16, var1.m11()).putFloat(var2 + 20, var1.m12()).putFloat(var2 + 24, var1.m20()).putFloat(var2 + 28, var1.m21()).putFloat(var2 + 32, var1.m22()).putFloat(var2 + 36, var1.m30()).putFloat(var2 + 40, var1.m31()).putFloat(var2 + 44, var1.m32());
      }

      public void put(Matrix4x3f var1, int var2, ByteBuffer var3) {
         if (var2 == 0) {
            this.put0(var1, var3);
         } else {
            this.putN(var1, var2, var3);
         }

      }

      public void put4x4(Matrix4x3f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, 0.0F).put(var2 + 4, var1.m10()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m12()).put(var2 + 7, 0.0F).put(var2 + 8, var1.m20()).put(var2 + 9, var1.m21()).put(var2 + 10, var1.m22()).put(var2 + 11, 0.0F).put(var2 + 12, var1.m30()).put(var2 + 13, var1.m31()).put(var2 + 14, var1.m32()).put(var2 + 15, 1.0F);
      }

      public void put4x4(Matrix4x3f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m02()).putFloat(var2 + 12, 0.0F).putFloat(var2 + 16, var1.m10()).putFloat(var2 + 20, var1.m11()).putFloat(var2 + 24, var1.m12()).putFloat(var2 + 28, 0.0F).putFloat(var2 + 32, var1.m20()).putFloat(var2 + 36, var1.m21()).putFloat(var2 + 40, var1.m22()).putFloat(var2 + 44, 0.0F).putFloat(var2 + 48, var1.m30()).putFloat(var2 + 52, var1.m31()).putFloat(var2 + 56, var1.m32()).putFloat(var2 + 60, 1.0F);
      }

      public void put4x4(Matrix4x3d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, 0.0D).put(var2 + 4, var1.m10()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m12()).put(var2 + 7, 0.0D).put(var2 + 8, var1.m20()).put(var2 + 9, var1.m21()).put(var2 + 10, var1.m22()).put(var2 + 11, 0.0D).put(var2 + 12, var1.m30()).put(var2 + 13, var1.m31()).put(var2 + 14, var1.m32()).put(var2 + 15, 1.0D);
      }

      public void put4x4(Matrix4x3d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m01()).putDouble(var2 + 16, var1.m02()).putDouble(var2 + 24, 0.0D).putDouble(var2 + 32, var1.m10()).putDouble(var2 + 40, var1.m11()).putDouble(var2 + 48, var1.m12()).putDouble(var2 + 56, 0.0D).putDouble(var2 + 64, var1.m20()).putDouble(var2 + 72, var1.m21()).putDouble(var2 + 80, var1.m22()).putDouble(var2 + 88, 0.0D).putDouble(var2 + 96, var1.m30()).putDouble(var2 + 104, var1.m31()).putDouble(var2 + 112, var1.m32()).putDouble(var2 + 120, 1.0D);
      }

      public void put4x4(Matrix3x2f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, 0.0F).put(var2 + 3, 0.0F).put(var2 + 4, var1.m10()).put(var2 + 5, var1.m11()).put(var2 + 6, 0.0F).put(var2 + 7, 0.0F).put(var2 + 8, 0.0F).put(var2 + 9, 0.0F).put(var2 + 10, 1.0F).put(var2 + 11, 0.0F).put(var2 + 12, var1.m20()).put(var2 + 13, var1.m21()).put(var2 + 14, 0.0F).put(var2 + 15, 1.0F);
      }

      public void put4x4(Matrix3x2f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, 0.0F).putFloat(var2 + 12, 0.0F).putFloat(var2 + 16, var1.m10()).putFloat(var2 + 20, var1.m11()).putFloat(var2 + 24, 0.0F).putFloat(var2 + 28, 0.0F).putFloat(var2 + 32, 0.0F).putFloat(var2 + 36, 0.0F).putFloat(var2 + 40, 1.0F).putFloat(var2 + 44, 0.0F).putFloat(var2 + 48, var1.m20()).putFloat(var2 + 52, var1.m21()).putFloat(var2 + 56, 0.0F).putFloat(var2 + 60, 1.0F);
      }

      public void put4x4(Matrix3x2d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, 0.0D).put(var2 + 3, 0.0D).put(var2 + 4, var1.m10()).put(var2 + 5, var1.m11()).put(var2 + 6, 0.0D).put(var2 + 7, 0.0D).put(var2 + 8, 0.0D).put(var2 + 9, 0.0D).put(var2 + 10, 1.0D).put(var2 + 11, 0.0D).put(var2 + 12, var1.m20()).put(var2 + 13, var1.m21()).put(var2 + 14, 0.0D).put(var2 + 15, 1.0D);
      }

      public void put4x4(Matrix3x2d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m01()).putDouble(var2 + 16, 0.0D).putDouble(var2 + 24, 0.0D).putDouble(var2 + 32, var1.m10()).putDouble(var2 + 40, var1.m11()).putDouble(var2 + 48, 0.0D).putDouble(var2 + 56, 0.0D).putDouble(var2 + 64, 0.0D).putDouble(var2 + 72, 0.0D).putDouble(var2 + 80, 1.0D).putDouble(var2 + 88, 0.0D).putDouble(var2 + 96, var1.m20()).putDouble(var2 + 104, var1.m21()).putDouble(var2 + 112, 0.0D).putDouble(var2 + 120, 1.0D);
      }

      public void put3x3(Matrix3x2f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, 0.0F).put(var2 + 3, var1.m10()).put(var2 + 4, var1.m11()).put(var2 + 5, 0.0F).put(var2 + 6, var1.m20()).put(var2 + 7, var1.m21()).put(var2 + 8, 1.0F);
      }

      public void put3x3(Matrix3x2f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, 0.0F).putFloat(var2 + 12, var1.m10()).putFloat(var2 + 16, var1.m11()).putFloat(var2 + 20, 0.0F).putFloat(var2 + 24, var1.m20()).putFloat(var2 + 28, var1.m21()).putFloat(var2 + 32, 1.0F);
      }

      public void put3x3(Matrix3x2d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, 0.0D).put(var2 + 3, var1.m10()).put(var2 + 4, var1.m11()).put(var2 + 5, 0.0D).put(var2 + 6, var1.m20()).put(var2 + 7, var1.m21()).put(var2 + 8, 1.0D);
      }

      public void put3x3(Matrix3x2d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m01()).putDouble(var2 + 16, 0.0D).putDouble(var2 + 24, var1.m10()).putDouble(var2 + 32, var1.m11()).putDouble(var2 + 40, 0.0D).putDouble(var2 + 48, var1.m20()).putDouble(var2 + 56, var1.m21()).putDouble(var2 + 64, 1.0D);
      }

      private void putTransposedN(Matrix4f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m10()).put(var2 + 2, var1.m20()).put(var2 + 3, var1.m30()).put(var2 + 4, var1.m01()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m21()).put(var2 + 7, var1.m31()).put(var2 + 8, var1.m02()).put(var2 + 9, var1.m12()).put(var2 + 10, var1.m22()).put(var2 + 11, var1.m32()).put(var2 + 12, var1.m03()).put(var2 + 13, var1.m13()).put(var2 + 14, var1.m23()).put(var2 + 15, var1.m33());
      }

      private void putTransposed0(Matrix4f var1, FloatBuffer var2) {
         var2.put(0, var1.m00()).put(1, var1.m10()).put(2, var1.m20()).put(3, var1.m30()).put(4, var1.m01()).put(5, var1.m11()).put(6, var1.m21()).put(7, var1.m31()).put(8, var1.m02()).put(9, var1.m12()).put(10, var1.m22()).put(11, var1.m32()).put(12, var1.m03()).put(13, var1.m13()).put(14, var1.m23()).put(15, var1.m33());
      }

      public void putTransposed(Matrix4f var1, int var2, FloatBuffer var3) {
         if (var2 == 0) {
            this.putTransposed0(var1, var3);
         } else {
            this.putTransposedN(var1, var2, var3);
         }

      }

      private void putTransposedN(Matrix4f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m10()).putFloat(var2 + 8, var1.m20()).putFloat(var2 + 12, var1.m30()).putFloat(var2 + 16, var1.m01()).putFloat(var2 + 20, var1.m11()).putFloat(var2 + 24, var1.m21()).putFloat(var2 + 28, var1.m31()).putFloat(var2 + 32, var1.m02()).putFloat(var2 + 36, var1.m12()).putFloat(var2 + 40, var1.m22()).putFloat(var2 + 44, var1.m32()).putFloat(var2 + 48, var1.m03()).putFloat(var2 + 52, var1.m13()).putFloat(var2 + 56, var1.m23()).putFloat(var2 + 60, var1.m33());
      }

      private void putTransposed0(Matrix4f var1, ByteBuffer var2) {
         var2.putFloat(0, var1.m00()).putFloat(4, var1.m10()).putFloat(8, var1.m20()).putFloat(12, var1.m30()).putFloat(16, var1.m01()).putFloat(20, var1.m11()).putFloat(24, var1.m21()).putFloat(28, var1.m31()).putFloat(32, var1.m02()).putFloat(36, var1.m12()).putFloat(40, var1.m22()).putFloat(44, var1.m32()).putFloat(48, var1.m03()).putFloat(52, var1.m13()).putFloat(56, var1.m23()).putFloat(60, var1.m33());
      }

      public void putTransposed(Matrix4f var1, int var2, ByteBuffer var3) {
         if (var2 == 0) {
            this.putTransposed0(var1, var3);
         } else {
            this.putTransposedN(var1, var2, var3);
         }

      }

      public void put4x3Transposed(Matrix4f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m10()).put(var2 + 2, var1.m20()).put(var2 + 3, var1.m30()).put(var2 + 4, var1.m01()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m21()).put(var2 + 7, var1.m31()).put(var2 + 8, var1.m02()).put(var2 + 9, var1.m12()).put(var2 + 10, var1.m22()).put(var2 + 11, var1.m32());
      }

      public void put4x3Transposed(Matrix4f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m10()).putFloat(var2 + 8, var1.m20()).putFloat(var2 + 12, var1.m30()).putFloat(var2 + 16, var1.m01()).putFloat(var2 + 20, var1.m11()).putFloat(var2 + 24, var1.m21()).putFloat(var2 + 28, var1.m31()).putFloat(var2 + 32, var1.m02()).putFloat(var2 + 36, var1.m12()).putFloat(var2 + 40, var1.m22()).putFloat(var2 + 44, var1.m32());
      }

      public void putTransposed(Matrix4x3f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m10()).put(var2 + 2, var1.m20()).put(var2 + 3, var1.m30()).put(var2 + 4, var1.m01()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m21()).put(var2 + 7, var1.m31()).put(var2 + 8, var1.m02()).put(var2 + 9, var1.m12()).put(var2 + 10, var1.m22()).put(var2 + 11, var1.m32());
      }

      public void putTransposed(Matrix4x3f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m10()).putFloat(var2 + 8, var1.m20()).putFloat(var2 + 12, var1.m30()).putFloat(var2 + 16, var1.m01()).putFloat(var2 + 20, var1.m11()).putFloat(var2 + 24, var1.m21()).putFloat(var2 + 28, var1.m31()).putFloat(var2 + 32, var1.m02()).putFloat(var2 + 36, var1.m12()).putFloat(var2 + 40, var1.m22()).putFloat(var2 + 44, var1.m32());
      }

      public void putTransposed(Matrix3f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m10()).put(var2 + 2, var1.m20()).put(var2 + 3, var1.m01()).put(var2 + 4, var1.m11()).put(var2 + 5, var1.m21()).put(var2 + 6, var1.m02()).put(var2 + 7, var1.m12()).put(var2 + 8, var1.m22());
      }

      public void putTransposed(Matrix3f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m10()).putFloat(var2 + 8, var1.m20()).putFloat(var2 + 12, var1.m01()).putFloat(var2 + 16, var1.m11()).putFloat(var2 + 20, var1.m21()).putFloat(var2 + 24, var1.m02()).putFloat(var2 + 28, var1.m12()).putFloat(var2 + 32, var1.m22());
      }

      public void putTransposed(Matrix2f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m10()).put(var2 + 2, var1.m01()).put(var2 + 3, var1.m11());
      }

      public void putTransposed(Matrix2f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m10()).putFloat(var2 + 8, var1.m01()).putFloat(var2 + 12, var1.m11());
      }

      public void put(Matrix4d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, var1.m03()).put(var2 + 4, var1.m10()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m12()).put(var2 + 7, var1.m13()).put(var2 + 8, var1.m20()).put(var2 + 9, var1.m21()).put(var2 + 10, var1.m22()).put(var2 + 11, var1.m23()).put(var2 + 12, var1.m30()).put(var2 + 13, var1.m31()).put(var2 + 14, var1.m32()).put(var2 + 15, var1.m33());
      }

      public void put(Matrix4d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m01()).putDouble(var2 + 16, var1.m02()).putDouble(var2 + 24, var1.m03()).putDouble(var2 + 32, var1.m10()).putDouble(var2 + 40, var1.m11()).putDouble(var2 + 48, var1.m12()).putDouble(var2 + 56, var1.m13()).putDouble(var2 + 64, var1.m20()).putDouble(var2 + 72, var1.m21()).putDouble(var2 + 80, var1.m22()).putDouble(var2 + 88, var1.m23()).putDouble(var2 + 96, var1.m30()).putDouble(var2 + 104, var1.m31()).putDouble(var2 + 112, var1.m32()).putDouble(var2 + 120, var1.m33());
      }

      public void put(Matrix4x3d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, var1.m10()).put(var2 + 4, var1.m11()).put(var2 + 5, var1.m12()).put(var2 + 6, var1.m20()).put(var2 + 7, var1.m21()).put(var2 + 8, var1.m22()).put(var2 + 9, var1.m30()).put(var2 + 10, var1.m31()).put(var2 + 11, var1.m32());
      }

      public void put(Matrix4x3d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m01()).putDouble(var2 + 16, var1.m02()).putDouble(var2 + 24, var1.m10()).putDouble(var2 + 32, var1.m11()).putDouble(var2 + 40, var1.m12()).putDouble(var2 + 48, var1.m20()).putDouble(var2 + 56, var1.m21()).putDouble(var2 + 64, var1.m22()).putDouble(var2 + 72, var1.m30()).putDouble(var2 + 80, var1.m31()).putDouble(var2 + 88, var1.m32());
      }

      public void putf(Matrix4d var1, int var2, FloatBuffer var3) {
         var3.put(var2, (float)var1.m00()).put(var2 + 1, (float)var1.m01()).put(var2 + 2, (float)var1.m02()).put(var2 + 3, (float)var1.m03()).put(var2 + 4, (float)var1.m10()).put(var2 + 5, (float)var1.m11()).put(var2 + 6, (float)var1.m12()).put(var2 + 7, (float)var1.m13()).put(var2 + 8, (float)var1.m20()).put(var2 + 9, (float)var1.m21()).put(var2 + 10, (float)var1.m22()).put(var2 + 11, (float)var1.m23()).put(var2 + 12, (float)var1.m30()).put(var2 + 13, (float)var1.m31()).put(var2 + 14, (float)var1.m32()).put(var2 + 15, (float)var1.m33());
      }

      public void putf(Matrix4d var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, (float)var1.m00()).putFloat(var2 + 4, (float)var1.m01()).putFloat(var2 + 8, (float)var1.m02()).putFloat(var2 + 12, (float)var1.m03()).putFloat(var2 + 16, (float)var1.m10()).putFloat(var2 + 20, (float)var1.m11()).putFloat(var2 + 24, (float)var1.m12()).putFloat(var2 + 28, (float)var1.m13()).putFloat(var2 + 32, (float)var1.m20()).putFloat(var2 + 36, (float)var1.m21()).putFloat(var2 + 40, (float)var1.m22()).putFloat(var2 + 44, (float)var1.m23()).putFloat(var2 + 48, (float)var1.m30()).putFloat(var2 + 52, (float)var1.m31()).putFloat(var2 + 56, (float)var1.m32()).putFloat(var2 + 60, (float)var1.m33());
      }

      public void putf(Matrix4x3d var1, int var2, FloatBuffer var3) {
         var3.put(var2, (float)var1.m00()).put(var2 + 1, (float)var1.m01()).put(var2 + 2, (float)var1.m02()).put(var2 + 3, (float)var1.m10()).put(var2 + 4, (float)var1.m11()).put(var2 + 5, (float)var1.m12()).put(var2 + 6, (float)var1.m20()).put(var2 + 7, (float)var1.m21()).put(var2 + 8, (float)var1.m22()).put(var2 + 9, (float)var1.m30()).put(var2 + 10, (float)var1.m31()).put(var2 + 11, (float)var1.m32());
      }

      public void putf(Matrix4x3d var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, (float)var1.m00()).putFloat(var2 + 4, (float)var1.m01()).putFloat(var2 + 8, (float)var1.m02()).putFloat(var2 + 12, (float)var1.m10()).putFloat(var2 + 16, (float)var1.m11()).putFloat(var2 + 20, (float)var1.m12()).putFloat(var2 + 24, (float)var1.m20()).putFloat(var2 + 28, (float)var1.m21()).putFloat(var2 + 32, (float)var1.m22()).putFloat(var2 + 36, (float)var1.m30()).putFloat(var2 + 40, (float)var1.m31()).putFloat(var2 + 44, (float)var1.m32());
      }

      public void putTransposed(Matrix4d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m10()).put(var2 + 2, var1.m20()).put(var2 + 3, var1.m30()).put(var2 + 4, var1.m01()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m21()).put(var2 + 7, var1.m31()).put(var2 + 8, var1.m02()).put(var2 + 9, var1.m12()).put(var2 + 10, var1.m22()).put(var2 + 11, var1.m32()).put(var2 + 12, var1.m03()).put(var2 + 13, var1.m13()).put(var2 + 14, var1.m23()).put(var2 + 15, var1.m33());
      }

      public void putTransposed(Matrix4d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m10()).putDouble(var2 + 16, var1.m20()).putDouble(var2 + 24, var1.m30()).putDouble(var2 + 32, var1.m01()).putDouble(var2 + 40, var1.m11()).putDouble(var2 + 48, var1.m21()).putDouble(var2 + 56, var1.m31()).putDouble(var2 + 64, var1.m02()).putDouble(var2 + 72, var1.m12()).putDouble(var2 + 80, var1.m22()).putDouble(var2 + 88, var1.m32()).putDouble(var2 + 96, var1.m03()).putDouble(var2 + 104, var1.m13()).putDouble(var2 + 112, var1.m23()).putDouble(var2 + 120, var1.m33());
      }

      public void put4x3Transposed(Matrix4d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m10()).put(var2 + 2, var1.m20()).put(var2 + 3, var1.m30()).put(var2 + 4, var1.m01()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m21()).put(var2 + 7, var1.m31()).put(var2 + 8, var1.m02()).put(var2 + 9, var1.m12()).put(var2 + 10, var1.m22()).put(var2 + 11, var1.m32());
      }

      public void put4x3Transposed(Matrix4d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m10()).putDouble(var2 + 16, var1.m20()).putDouble(var2 + 24, var1.m30()).putDouble(var2 + 32, var1.m01()).putDouble(var2 + 40, var1.m11()).putDouble(var2 + 48, var1.m21()).putDouble(var2 + 56, var1.m31()).putDouble(var2 + 64, var1.m02()).putDouble(var2 + 72, var1.m12()).putDouble(var2 + 80, var1.m22()).putDouble(var2 + 88, var1.m32());
      }

      public void putTransposed(Matrix4x3d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m10()).put(var2 + 2, var1.m20()).put(var2 + 3, var1.m30()).put(var2 + 4, var1.m01()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m21()).put(var2 + 7, var1.m31()).put(var2 + 8, var1.m02()).put(var2 + 9, var1.m12()).put(var2 + 10, var1.m22()).put(var2 + 11, var1.m32());
      }

      public void putTransposed(Matrix4x3d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m10()).putDouble(var2 + 16, var1.m20()).putDouble(var2 + 24, var1.m30()).putDouble(var2 + 32, var1.m01()).putDouble(var2 + 40, var1.m11()).putDouble(var2 + 48, var1.m21()).putDouble(var2 + 56, var1.m31()).putDouble(var2 + 64, var1.m02()).putDouble(var2 + 72, var1.m12()).putDouble(var2 + 80, var1.m22()).putDouble(var2 + 88, var1.m32());
      }

      public void putTransposed(Matrix2d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m10()).put(var2 + 2, var1.m01()).put(var2 + 3, var1.m11());
      }

      public void putTransposed(Matrix2d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m10()).putDouble(var2 + 16, var1.m01()).putDouble(var2 + 24, var1.m11());
      }

      public void putfTransposed(Matrix4x3d var1, int var2, FloatBuffer var3) {
         var3.put(var2, (float)var1.m00()).put(var2 + 1, (float)var1.m10()).put(var2 + 2, (float)var1.m20()).put(var2 + 3, (float)var1.m30()).put(var2 + 4, (float)var1.m01()).put(var2 + 5, (float)var1.m11()).put(var2 + 6, (float)var1.m21()).put(var2 + 7, (float)var1.m31()).put(var2 + 8, (float)var1.m02()).put(var2 + 9, (float)var1.m12()).put(var2 + 10, (float)var1.m22()).put(var2 + 11, (float)var1.m32());
      }

      public void putfTransposed(Matrix4x3d var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, (float)var1.m00()).putFloat(var2 + 4, (float)var1.m10()).putFloat(var2 + 8, (float)var1.m20()).putFloat(var2 + 12, (float)var1.m30()).putFloat(var2 + 16, (float)var1.m01()).putFloat(var2 + 20, (float)var1.m11()).putFloat(var2 + 24, (float)var1.m21()).putFloat(var2 + 28, (float)var1.m31()).putFloat(var2 + 32, (float)var1.m02()).putFloat(var2 + 36, (float)var1.m12()).putFloat(var2 + 40, (float)var1.m22()).putFloat(var2 + 44, (float)var1.m32());
      }

      public void putfTransposed(Matrix2d var1, int var2, FloatBuffer var3) {
         var3.put(var2, (float)var1.m00()).put(var2 + 1, (float)var1.m10()).put(var2 + 2, (float)var1.m01()).put(var2 + 3, (float)var1.m11());
      }

      public void putfTransposed(Matrix2d var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, (float)var1.m00()).putFloat(var2 + 4, (float)var1.m10()).putFloat(var2 + 8, (float)var1.m01()).putFloat(var2 + 12, (float)var1.m11());
      }

      public void putfTransposed(Matrix4d var1, int var2, FloatBuffer var3) {
         var3.put(var2, (float)var1.m00()).put(var2 + 1, (float)var1.m10()).put(var2 + 2, (float)var1.m20()).put(var2 + 3, (float)var1.m30()).put(var2 + 4, (float)var1.m01()).put(var2 + 5, (float)var1.m11()).put(var2 + 6, (float)var1.m21()).put(var2 + 7, (float)var1.m31()).put(var2 + 8, (float)var1.m02()).put(var2 + 9, (float)var1.m12()).put(var2 + 10, (float)var1.m22()).put(var2 + 11, (float)var1.m32()).put(var2 + 12, (float)var1.m03()).put(var2 + 13, (float)var1.m13()).put(var2 + 14, (float)var1.m23()).put(var2 + 15, (float)var1.m33());
      }

      public void putfTransposed(Matrix4d var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, (float)var1.m00()).putFloat(var2 + 4, (float)var1.m10()).putFloat(var2 + 8, (float)var1.m20()).putFloat(var2 + 12, (float)var1.m30()).putFloat(var2 + 16, (float)var1.m01()).putFloat(var2 + 20, (float)var1.m11()).putFloat(var2 + 24, (float)var1.m21()).putFloat(var2 + 28, (float)var1.m31()).putFloat(var2 + 32, (float)var1.m02()).putFloat(var2 + 36, (float)var1.m12()).putFloat(var2 + 40, (float)var1.m22()).putFloat(var2 + 44, (float)var1.m32()).putFloat(var2 + 48, (float)var1.m03()).putFloat(var2 + 52, (float)var1.m13()).putFloat(var2 + 56, (float)var1.m23()).putFloat(var2 + 60, (float)var1.m33());
      }

      public void put0(Matrix3f var1, FloatBuffer var2) {
         var2.put(0, var1.m00()).put(1, var1.m01()).put(2, var1.m02()).put(3, var1.m10()).put(4, var1.m11()).put(5, var1.m12()).put(6, var1.m20()).put(7, var1.m21()).put(8, var1.m22());
      }

      public void putN(Matrix3f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, var1.m10()).put(var2 + 4, var1.m11()).put(var2 + 5, var1.m12()).put(var2 + 6, var1.m20()).put(var2 + 7, var1.m21()).put(var2 + 8, var1.m22());
      }

      public void put(Matrix3f var1, int var2, FloatBuffer var3) {
         if (var2 == 0) {
            this.put0(var1, var3);
         } else {
            this.putN(var1, var2, var3);
         }

      }

      public void put0(Matrix3f var1, ByteBuffer var2) {
         var2.putFloat(0, var1.m00()).putFloat(4, var1.m01()).putFloat(8, var1.m02()).putFloat(12, var1.m10()).putFloat(16, var1.m11()).putFloat(20, var1.m12()).putFloat(24, var1.m20()).putFloat(28, var1.m21()).putFloat(32, var1.m22());
      }

      public void putN(Matrix3f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m02()).putFloat(var2 + 12, var1.m10()).putFloat(var2 + 16, var1.m11()).putFloat(var2 + 20, var1.m12()).putFloat(var2 + 24, var1.m20()).putFloat(var2 + 28, var1.m21()).putFloat(var2 + 32, var1.m22());
      }

      public void put(Matrix3f var1, int var2, ByteBuffer var3) {
         if (var2 == 0) {
            this.put0(var1, var3);
         } else {
            this.putN(var1, var2, var3);
         }

      }

      public void put3x4_0(Matrix3f var1, ByteBuffer var2) {
         var2.putFloat(0, var1.m00()).putFloat(4, var1.m01()).putFloat(8, var1.m02()).putFloat(12, 0.0F).putFloat(16, var1.m10()).putFloat(20, var1.m11()).putFloat(24, var1.m12()).putFloat(28, 0.0F).putFloat(32, var1.m20()).putFloat(36, var1.m21()).putFloat(40, var1.m22()).putFloat(44, 0.0F);
      }

      private void put3x4_N(Matrix3f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m02()).putFloat(var2 + 12, 0.0F).putFloat(var2 + 16, var1.m10()).putFloat(var2 + 20, var1.m11()).putFloat(var2 + 24, var1.m12()).putFloat(var2 + 28, 0.0F).putFloat(var2 + 32, var1.m20()).putFloat(var2 + 36, var1.m21()).putFloat(var2 + 40, var1.m22()).putFloat(var2 + 44, 0.0F);
      }

      public void put3x4(Matrix3f var1, int var2, ByteBuffer var3) {
         if (var2 == 0) {
            this.put3x4_0(var1, var3);
         } else {
            this.put3x4_N(var1, var2, var3);
         }

      }

      public void put3x4_0(Matrix3f var1, FloatBuffer var2) {
         var2.put(0, var1.m00()).put(1, var1.m01()).put(2, var1.m02()).put(3, 0.0F).put(4, var1.m10()).put(5, var1.m11()).put(6, var1.m12()).put(7, 0.0F).put(8, var1.m20()).put(9, var1.m21()).put(10, var1.m22()).put(11, 0.0F);
      }

      public void put3x4_N(Matrix3f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, 0.0F).put(var2 + 4, var1.m10()).put(var2 + 5, var1.m11()).put(var2 + 6, var1.m12()).put(var2 + 7, 0.0F).put(var2 + 8, var1.m20()).put(var2 + 9, var1.m21()).put(var2 + 10, var1.m22()).put(var2 + 11, 0.0F);
      }

      public void put3x4(Matrix3f var1, int var2, FloatBuffer var3) {
         if (var2 == 0) {
            this.put3x4_0(var1, var3);
         } else {
            this.put3x4_N(var1, var2, var3);
         }

      }

      public void put(Matrix3d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m02()).put(var2 + 3, var1.m10()).put(var2 + 4, var1.m11()).put(var2 + 5, var1.m12()).put(var2 + 6, var1.m20()).put(var2 + 7, var1.m21()).put(var2 + 8, var1.m22());
      }

      public void put(Matrix3d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m01()).putDouble(var2 + 16, var1.m02()).putDouble(var2 + 24, var1.m10()).putDouble(var2 + 32, var1.m11()).putDouble(var2 + 40, var1.m12()).putDouble(var2 + 48, var1.m20()).putDouble(var2 + 56, var1.m21()).putDouble(var2 + 64, var1.m22());
      }

      public void put(Matrix3x2f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m10()).put(var2 + 3, var1.m11()).put(var2 + 4, var1.m20()).put(var2 + 5, var1.m21());
      }

      public void put(Matrix3x2f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m10()).putFloat(var2 + 12, var1.m11()).putFloat(var2 + 16, var1.m20()).putFloat(var2 + 20, var1.m21());
      }

      public void put(Matrix3x2d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m10()).put(var2 + 3, var1.m11()).put(var2 + 4, var1.m20()).put(var2 + 5, var1.m21());
      }

      public void put(Matrix3x2d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m01()).putDouble(var2 + 16, var1.m10()).putDouble(var2 + 24, var1.m11()).putDouble(var2 + 32, var1.m20()).putDouble(var2 + 40, var1.m21());
      }

      public void putf(Matrix3d var1, int var2, FloatBuffer var3) {
         var3.put(var2, (float)var1.m00()).put(var2 + 1, (float)var1.m01()).put(var2 + 2, (float)var1.m02()).put(var2 + 3, (float)var1.m10()).put(var2 + 4, (float)var1.m11()).put(var2 + 5, (float)var1.m12()).put(var2 + 6, (float)var1.m20()).put(var2 + 7, (float)var1.m21()).put(var2 + 8, (float)var1.m22());
      }

      public void put(Matrix2f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m10()).put(var2 + 3, var1.m11());
      }

      public void put(Matrix2f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.m00()).putFloat(var2 + 4, var1.m01()).putFloat(var2 + 8, var1.m10()).putFloat(var2 + 12, var1.m11());
      }

      public void put(Matrix2d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.m00()).put(var2 + 1, var1.m01()).put(var2 + 2, var1.m10()).put(var2 + 3, var1.m11());
      }

      public void put(Matrix2d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.m00()).putDouble(var2 + 8, var1.m01()).putDouble(var2 + 16, var1.m10()).putDouble(var2 + 24, var1.m11());
      }

      public void putf(Matrix2d var1, int var2, FloatBuffer var3) {
         var3.put(var2, (float)var1.m00()).put(var2 + 1, (float)var1.m01()).put(var2 + 2, (float)var1.m10()).put(var2 + 3, (float)var1.m11());
      }

      public void putf(Matrix2d var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, (float)var1.m00()).putFloat(var2 + 4, (float)var1.m01()).putFloat(var2 + 8, (float)var1.m10()).putFloat(var2 + 12, (float)var1.m11());
      }

      public void putf(Matrix3d var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, (float)var1.m00()).putFloat(var2 + 4, (float)var1.m01()).putFloat(var2 + 8, (float)var1.m02()).putFloat(var2 + 12, (float)var1.m10()).putFloat(var2 + 16, (float)var1.m11()).putFloat(var2 + 20, (float)var1.m12()).putFloat(var2 + 24, (float)var1.m20()).putFloat(var2 + 28, (float)var1.m21()).putFloat(var2 + 32, (float)var1.m22());
      }

      public void put(Vector4d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.x).put(var2 + 1, var1.y).put(var2 + 2, var1.z).put(var2 + 3, var1.w);
      }

      public void put(Vector4d var1, int var2, FloatBuffer var3) {
         var3.put(var2, (float)var1.x).put(var2 + 1, (float)var1.y).put(var2 + 2, (float)var1.z).put(var2 + 3, (float)var1.w);
      }

      public void put(Vector4d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.x).putDouble(var2 + 8, var1.y).putDouble(var2 + 16, var1.z).putDouble(var2 + 24, var1.w);
      }

      public void putf(Vector4d var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, (float)var1.x).putFloat(var2 + 4, (float)var1.y).putFloat(var2 + 8, (float)var1.z).putFloat(var2 + 12, (float)var1.w);
      }

      public void put(Vector4f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.x).put(var2 + 1, var1.y).put(var2 + 2, var1.z).put(var2 + 3, var1.w);
      }

      public void put(Vector4f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.x).putFloat(var2 + 4, var1.y).putFloat(var2 + 8, var1.z).putFloat(var2 + 12, var1.w);
      }

      public void put(Vector4i var1, int var2, IntBuffer var3) {
         var3.put(var2, var1.x).put(var2 + 1, var1.y).put(var2 + 2, var1.z).put(var2 + 3, var1.w);
      }

      public void put(Vector4i var1, int var2, ByteBuffer var3) {
         var3.putInt(var2, var1.x).putInt(var2 + 4, var1.y).putInt(var2 + 8, var1.z).putInt(var2 + 12, var1.w);
      }

      public void put(Vector3f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.x).put(var2 + 1, var1.y).put(var2 + 2, var1.z);
      }

      public void put(Vector3f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.x).putFloat(var2 + 4, var1.y).putFloat(var2 + 8, var1.z);
      }

      public void put(Vector3d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.x).put(var2 + 1, var1.y).put(var2 + 2, var1.z);
      }

      public void put(Vector3d var1, int var2, FloatBuffer var3) {
         var3.put(var2, (float)var1.x).put(var2 + 1, (float)var1.y).put(var2 + 2, (float)var1.z);
      }

      public void put(Vector3d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.x).putDouble(var2 + 8, var1.y).putDouble(var2 + 16, var1.z);
      }

      public void putf(Vector3d var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, (float)var1.x).putFloat(var2 + 4, (float)var1.y).putFloat(var2 + 8, (float)var1.z);
      }

      public void put(Vector3i var1, int var2, IntBuffer var3) {
         var3.put(var2, var1.x).put(var2 + 1, var1.y).put(var2 + 2, var1.z);
      }

      public void put(Vector3i var1, int var2, ByteBuffer var3) {
         var3.putInt(var2, var1.x).putInt(var2 + 4, var1.y).putInt(var2 + 8, var1.z);
      }

      public void put(Vector2f var1, int var2, FloatBuffer var3) {
         var3.put(var2, var1.x).put(var2 + 1, var1.y);
      }

      public void put(Vector2f var1, int var2, ByteBuffer var3) {
         var3.putFloat(var2, var1.x).putFloat(var2 + 4, var1.y);
      }

      public void put(Vector2d var1, int var2, DoubleBuffer var3) {
         var3.put(var2, var1.x).put(var2 + 1, var1.y);
      }

      public void put(Vector2d var1, int var2, ByteBuffer var3) {
         var3.putDouble(var2, var1.x).putDouble(var2 + 8, var1.y);
      }

      public void put(Vector2i var1, int var2, IntBuffer var3) {
         var3.put(var2, var1.x).put(var2 + 1, var1.y);
      }

      public void put(Vector2i var1, int var2, ByteBuffer var3) {
         var3.putInt(var2, var1.x).putInt(var2 + 4, var1.y);
      }

      public void get(Matrix4f var1, int var2, FloatBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m02(var3.get(var2 + 2))._m03(var3.get(var2 + 3))._m10(var3.get(var2 + 4))._m11(var3.get(var2 + 5))._m12(var3.get(var2 + 6))._m13(var3.get(var2 + 7))._m20(var3.get(var2 + 8))._m21(var3.get(var2 + 9))._m22(var3.get(var2 + 10))._m23(var3.get(var2 + 11))._m30(var3.get(var2 + 12))._m31(var3.get(var2 + 13))._m32(var3.get(var2 + 14))._m33(var3.get(var2 + 15));
      }

      public void get(Matrix4f var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getFloat(var2))._m01(var3.getFloat(var2 + 4))._m02(var3.getFloat(var2 + 8))._m03(var3.getFloat(var2 + 12))._m10(var3.getFloat(var2 + 16))._m11(var3.getFloat(var2 + 20))._m12(var3.getFloat(var2 + 24))._m13(var3.getFloat(var2 + 28))._m20(var3.getFloat(var2 + 32))._m21(var3.getFloat(var2 + 36))._m22(var3.getFloat(var2 + 40))._m23(var3.getFloat(var2 + 44))._m30(var3.getFloat(var2 + 48))._m31(var3.getFloat(var2 + 52))._m32(var3.getFloat(var2 + 56))._m33(var3.getFloat(var2 + 60));
      }

      public void getTransposed(Matrix4f var1, int var2, FloatBuffer var3) {
         var1._m00(var3.get(var2))._m10(var3.get(var2 + 1))._m20(var3.get(var2 + 2))._m30(var3.get(var2 + 3))._m01(var3.get(var2 + 4))._m11(var3.get(var2 + 5))._m21(var3.get(var2 + 6))._m31(var3.get(var2 + 7))._m02(var3.get(var2 + 8))._m12(var3.get(var2 + 9))._m22(var3.get(var2 + 10))._m32(var3.get(var2 + 11))._m03(var3.get(var2 + 12))._m13(var3.get(var2 + 13))._m23(var3.get(var2 + 14))._m33(var3.get(var2 + 15));
      }

      public void getTransposed(Matrix4f var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getFloat(var2))._m10(var3.getFloat(var2 + 4))._m20(var3.getFloat(var2 + 8))._m30(var3.getFloat(var2 + 12))._m01(var3.getFloat(var2 + 16))._m11(var3.getFloat(var2 + 20))._m21(var3.getFloat(var2 + 24))._m31(var3.getFloat(var2 + 28))._m02(var3.getFloat(var2 + 32))._m12(var3.getFloat(var2 + 36))._m22(var3.getFloat(var2 + 40))._m32(var3.getFloat(var2 + 44))._m03(var3.getFloat(var2 + 48))._m13(var3.getFloat(var2 + 52))._m23(var3.getFloat(var2 + 56))._m33(var3.getFloat(var2 + 60));
      }

      public void get(Matrix4x3f var1, int var2, FloatBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m02(var3.get(var2 + 2))._m10(var3.get(var2 + 3))._m11(var3.get(var2 + 4))._m12(var3.get(var2 + 5))._m20(var3.get(var2 + 6))._m21(var3.get(var2 + 7))._m22(var3.get(var2 + 8))._m30(var3.get(var2 + 9))._m31(var3.get(var2 + 10))._m32(var3.get(var2 + 11));
      }

      public void get(Matrix4x3f var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getFloat(var2))._m01(var3.getFloat(var2 + 4))._m02(var3.getFloat(var2 + 8))._m10(var3.getFloat(var2 + 12))._m11(var3.getFloat(var2 + 16))._m12(var3.getFloat(var2 + 20))._m20(var3.getFloat(var2 + 24))._m21(var3.getFloat(var2 + 28))._m22(var3.getFloat(var2 + 32))._m30(var3.getFloat(var2 + 36))._m31(var3.getFloat(var2 + 40))._m32(var3.getFloat(var2 + 44));
      }

      public void get(Matrix4d var1, int var2, DoubleBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m02(var3.get(var2 + 2))._m03(var3.get(var2 + 3))._m10(var3.get(var2 + 4))._m11(var3.get(var2 + 5))._m12(var3.get(var2 + 6))._m13(var3.get(var2 + 7))._m20(var3.get(var2 + 8))._m21(var3.get(var2 + 9))._m22(var3.get(var2 + 10))._m23(var3.get(var2 + 11))._m30(var3.get(var2 + 12))._m31(var3.get(var2 + 13))._m32(var3.get(var2 + 14))._m33(var3.get(var2 + 15));
      }

      public void get(Matrix4d var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getDouble(var2))._m01(var3.getDouble(var2 + 8))._m02(var3.getDouble(var2 + 16))._m03(var3.getDouble(var2 + 24))._m10(var3.getDouble(var2 + 32))._m11(var3.getDouble(var2 + 40))._m12(var3.getDouble(var2 + 48))._m13(var3.getDouble(var2 + 56))._m20(var3.getDouble(var2 + 64))._m21(var3.getDouble(var2 + 72))._m22(var3.getDouble(var2 + 80))._m23(var3.getDouble(var2 + 88))._m30(var3.getDouble(var2 + 96))._m31(var3.getDouble(var2 + 104))._m32(var3.getDouble(var2 + 112))._m33(var3.getDouble(var2 + 120));
      }

      public void get(Matrix4x3d var1, int var2, DoubleBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m02(var3.get(var2 + 2))._m10(var3.get(var2 + 3))._m11(var3.get(var2 + 4))._m12(var3.get(var2 + 5))._m20(var3.get(var2 + 6))._m21(var3.get(var2 + 7))._m22(var3.get(var2 + 8))._m30(var3.get(var2 + 9))._m31(var3.get(var2 + 10))._m32(var3.get(var2 + 11));
      }

      public void get(Matrix4x3d var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getDouble(var2))._m01(var3.getDouble(var2 + 8))._m02(var3.getDouble(var2 + 16))._m10(var3.getDouble(var2 + 24))._m11(var3.getDouble(var2 + 32))._m12(var3.getDouble(var2 + 40))._m20(var3.getDouble(var2 + 48))._m21(var3.getDouble(var2 + 56))._m22(var3.getDouble(var2 + 64))._m30(var3.getDouble(var2 + 72))._m31(var3.getDouble(var2 + 80))._m32(var3.getDouble(var2 + 88));
      }

      public void getf(Matrix4d var1, int var2, FloatBuffer var3) {
         var1._m00((double)var3.get(var2))._m01((double)var3.get(var2 + 1))._m02((double)var3.get(var2 + 2))._m03((double)var3.get(var2 + 3))._m10((double)var3.get(var2 + 4))._m11((double)var3.get(var2 + 5))._m12((double)var3.get(var2 + 6))._m13((double)var3.get(var2 + 7))._m20((double)var3.get(var2 + 8))._m21((double)var3.get(var2 + 9))._m22((double)var3.get(var2 + 10))._m23((double)var3.get(var2 + 11))._m30((double)var3.get(var2 + 12))._m31((double)var3.get(var2 + 13))._m32((double)var3.get(var2 + 14))._m33((double)var3.get(var2 + 15));
      }

      public void getf(Matrix4d var1, int var2, ByteBuffer var3) {
         var1._m00((double)var3.getFloat(var2))._m01((double)var3.getFloat(var2 + 4))._m02((double)var3.getFloat(var2 + 8))._m03((double)var3.getFloat(var2 + 12))._m10((double)var3.getFloat(var2 + 16))._m11((double)var3.getFloat(var2 + 20))._m12((double)var3.getFloat(var2 + 24))._m13((double)var3.getFloat(var2 + 28))._m20((double)var3.getFloat(var2 + 32))._m21((double)var3.getFloat(var2 + 36))._m22((double)var3.getFloat(var2 + 40))._m23((double)var3.getFloat(var2 + 44))._m30((double)var3.getFloat(var2 + 48))._m31((double)var3.getFloat(var2 + 52))._m32((double)var3.getFloat(var2 + 56))._m33((double)var3.getFloat(var2 + 60));
      }

      public void getf(Matrix4x3d var1, int var2, FloatBuffer var3) {
         var1._m00((double)var3.get(var2))._m01((double)var3.get(var2 + 1))._m02((double)var3.get(var2 + 2))._m10((double)var3.get(var2 + 3))._m11((double)var3.get(var2 + 4))._m12((double)var3.get(var2 + 5))._m20((double)var3.get(var2 + 6))._m21((double)var3.get(var2 + 7))._m22((double)var3.get(var2 + 8))._m30((double)var3.get(var2 + 9))._m31((double)var3.get(var2 + 10))._m32((double)var3.get(var2 + 11));
      }

      public void getf(Matrix4x3d var1, int var2, ByteBuffer var3) {
         var1._m00((double)var3.getFloat(var2))._m01((double)var3.getFloat(var2 + 4))._m02((double)var3.getFloat(var2 + 8))._m10((double)var3.getFloat(var2 + 12))._m11((double)var3.getFloat(var2 + 16))._m12((double)var3.getFloat(var2 + 20))._m20((double)var3.getFloat(var2 + 24))._m21((double)var3.getFloat(var2 + 28))._m22((double)var3.getFloat(var2 + 32))._m30((double)var3.getFloat(var2 + 36))._m31((double)var3.getFloat(var2 + 40))._m32((double)var3.getFloat(var2 + 44));
      }

      public void get(Matrix3f var1, int var2, FloatBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m02(var3.get(var2 + 2))._m10(var3.get(var2 + 3))._m11(var3.get(var2 + 4))._m12(var3.get(var2 + 5))._m20(var3.get(var2 + 6))._m21(var3.get(var2 + 7))._m22(var3.get(var2 + 8));
      }

      public void get(Matrix3f var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getFloat(var2))._m01(var3.getFloat(var2 + 4))._m02(var3.getFloat(var2 + 8))._m10(var3.getFloat(var2 + 12))._m11(var3.getFloat(var2 + 16))._m12(var3.getFloat(var2 + 20))._m20(var3.getFloat(var2 + 24))._m21(var3.getFloat(var2 + 28))._m22(var3.getFloat(var2 + 32));
      }

      public void get(Matrix3d var1, int var2, DoubleBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m02(var3.get(var2 + 2))._m10(var3.get(var2 + 3))._m11(var3.get(var2 + 4))._m12(var3.get(var2 + 5))._m20(var3.get(var2 + 6))._m21(var3.get(var2 + 7))._m22(var3.get(var2 + 8));
      }

      public void get(Matrix3d var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getDouble(var2))._m01(var3.getDouble(var2 + 8))._m02(var3.getDouble(var2 + 16))._m10(var3.getDouble(var2 + 24))._m11(var3.getDouble(var2 + 32))._m12(var3.getDouble(var2 + 40))._m20(var3.getDouble(var2 + 48))._m21(var3.getDouble(var2 + 56))._m22(var3.getDouble(var2 + 64));
      }

      public void get(Matrix3x2f var1, int var2, FloatBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m10(var3.get(var2 + 2))._m11(var3.get(var2 + 3))._m20(var3.get(var2 + 4))._m21(var3.get(var2 + 5));
      }

      public void get(Matrix3x2f var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getFloat(var2))._m01(var3.getFloat(var2 + 4))._m10(var3.getFloat(var2 + 8))._m11(var3.getFloat(var2 + 12))._m20(var3.getFloat(var2 + 16))._m21(var3.getFloat(var2 + 20));
      }

      public void get(Matrix3x2d var1, int var2, DoubleBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m10(var3.get(var2 + 2))._m11(var3.get(var2 + 3))._m20(var3.get(var2 + 4))._m21(var3.get(var2 + 5));
      }

      public void get(Matrix3x2d var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getDouble(var2))._m01(var3.getDouble(var2 + 8))._m10(var3.getDouble(var2 + 16))._m11(var3.getDouble(var2 + 24))._m20(var3.getDouble(var2 + 32))._m21(var3.getDouble(var2 + 40));
      }

      public void getf(Matrix3d var1, int var2, FloatBuffer var3) {
         var1._m00((double)var3.get(var2))._m01((double)var3.get(var2 + 1))._m02((double)var3.get(var2 + 2))._m10((double)var3.get(var2 + 3))._m11((double)var3.get(var2 + 4))._m12((double)var3.get(var2 + 5))._m20((double)var3.get(var2 + 6))._m21((double)var3.get(var2 + 7))._m22((double)var3.get(var2 + 8));
      }

      public void getf(Matrix3d var1, int var2, ByteBuffer var3) {
         var1._m00((double)var3.getFloat(var2))._m01((double)var3.getFloat(var2 + 4))._m02((double)var3.getFloat(var2 + 8))._m10((double)var3.getFloat(var2 + 12))._m11((double)var3.getFloat(var2 + 16))._m12((double)var3.getFloat(var2 + 20))._m20((double)var3.getFloat(var2 + 24))._m21((double)var3.getFloat(var2 + 28))._m22((double)var3.getFloat(var2 + 32));
      }

      public void get(Matrix2f var1, int var2, FloatBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m10(var3.get(var2 + 2))._m11(var3.get(var2 + 3));
      }

      public void get(Matrix2f var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getFloat(var2))._m01(var3.getFloat(var2 + 4))._m10(var3.getFloat(var2 + 8))._m11(var3.getFloat(var2 + 12));
      }

      public void get(Matrix2d var1, int var2, DoubleBuffer var3) {
         var1._m00(var3.get(var2))._m01(var3.get(var2 + 1))._m10(var3.get(var2 + 2))._m11(var3.get(var2 + 3));
      }

      public void get(Matrix2d var1, int var2, ByteBuffer var3) {
         var1._m00(var3.getDouble(var2))._m01(var3.getDouble(var2 + 8))._m10(var3.getDouble(var2 + 16))._m11(var3.getDouble(var2 + 24));
      }

      public void getf(Matrix2d var1, int var2, FloatBuffer var3) {
         var1._m00((double)var3.get(var2))._m01((double)var3.get(var2 + 1))._m10((double)var3.get(var2 + 2))._m11((double)var3.get(var2 + 3));
      }

      public void getf(Matrix2d var1, int var2, ByteBuffer var3) {
         var1._m00((double)var3.getFloat(var2))._m01((double)var3.getFloat(var2 + 4))._m10((double)var3.getFloat(var2 + 8))._m11((double)var3.getFloat(var2 + 12));
      }

      public void get(Vector4d var1, int var2, DoubleBuffer var3) {
         var1.x = var3.get(var2);
         var1.y = var3.get(var2 + 1);
         var1.z = var3.get(var2 + 2);
         var1.w = var3.get(var2 + 3);
      }

      public void get(Vector4d var1, int var2, ByteBuffer var3) {
         var1.x = var3.getDouble(var2);
         var1.y = var3.getDouble(var2 + 8);
         var1.z = var3.getDouble(var2 + 16);
         var1.w = var3.getDouble(var2 + 24);
      }

      public void get(Vector4f var1, int var2, FloatBuffer var3) {
         var1.x = var3.get(var2);
         var1.y = var3.get(var2 + 1);
         var1.z = var3.get(var2 + 2);
         var1.w = var3.get(var2 + 3);
      }

      public void get(Vector4f var1, int var2, ByteBuffer var3) {
         var1.x = var3.getFloat(var2);
         var1.y = var3.getFloat(var2 + 4);
         var1.z = var3.getFloat(var2 + 8);
         var1.w = var3.getFloat(var2 + 12);
      }

      public void get(Vector4i var1, int var2, IntBuffer var3) {
         var1.x = var3.get(var2);
         var1.y = var3.get(var2 + 1);
         var1.z = var3.get(var2 + 2);
         var1.w = var3.get(var2 + 3);
      }

      public void get(Vector4i var1, int var2, ByteBuffer var3) {
         var1.x = var3.getInt(var2);
         var1.y = var3.getInt(var2 + 4);
         var1.z = var3.getInt(var2 + 8);
         var1.w = var3.getInt(var2 + 12);
      }

      public void get(Vector3f var1, int var2, FloatBuffer var3) {
         var1.x = var3.get(var2);
         var1.y = var3.get(var2 + 1);
         var1.z = var3.get(var2 + 2);
      }

      public void get(Vector3f var1, int var2, ByteBuffer var3) {
         var1.x = var3.getFloat(var2);
         var1.y = var3.getFloat(var2 + 4);
         var1.z = var3.getFloat(var2 + 8);
      }

      public void get(Vector3d var1, int var2, DoubleBuffer var3) {
         var1.x = var3.get(var2);
         var1.y = var3.get(var2 + 1);
         var1.z = var3.get(var2 + 2);
      }

      public void get(Vector3d var1, int var2, ByteBuffer var3) {
         var1.x = var3.getDouble(var2);
         var1.y = var3.getDouble(var2 + 8);
         var1.z = var3.getDouble(var2 + 16);
      }

      public void get(Vector3i var1, int var2, IntBuffer var3) {
         var1.x = var3.get(var2);
         var1.y = var3.get(var2 + 1);
         var1.z = var3.get(var2 + 2);
      }

      public void get(Vector3i var1, int var2, ByteBuffer var3) {
         var1.x = var3.getInt(var2);
         var1.y = var3.getInt(var2 + 4);
         var1.z = var3.getInt(var2 + 8);
      }

      public void get(Vector2f var1, int var2, FloatBuffer var3) {
         var1.x = var3.get(var2);
         var1.y = var3.get(var2 + 1);
      }

      public void get(Vector2f var1, int var2, ByteBuffer var3) {
         var1.x = var3.getFloat(var2);
         var1.y = var3.getFloat(var2 + 4);
      }

      public void get(Vector2d var1, int var2, DoubleBuffer var3) {
         var1.x = var3.get(var2);
         var1.y = var3.get(var2 + 1);
      }

      public void get(Vector2d var1, int var2, ByteBuffer var3) {
         var1.x = var3.getDouble(var2);
         var1.y = var3.getDouble(var2 + 8);
      }

      public void get(Vector2i var1, int var2, IntBuffer var3) {
         var1.x = var3.get(var2);
         var1.y = var3.get(var2 + 1);
      }

      public void get(Vector2i var1, int var2, ByteBuffer var3) {
         var1.x = var3.getInt(var2);
         var1.y = var3.getInt(var2 + 4);
      }

      public float get(Matrix4f var1, int var2, int var3) {
         switch(var2) {
         case 0:
            switch(var3) {
            case 0:
               return var1.m00;
            case 1:
               return var1.m01;
            case 2:
               return var1.m02;
            case 3:
               return var1.m03;
            default:
               throw new IllegalArgumentException();
            }
         case 1:
            switch(var3) {
            case 0:
               return var1.m10;
            case 1:
               return var1.m11;
            case 2:
               return var1.m12;
            case 3:
               return var1.m13;
            default:
               throw new IllegalArgumentException();
            }
         case 2:
            switch(var3) {
            case 0:
               return var1.m20;
            case 1:
               return var1.m21;
            case 2:
               return var1.m22;
            case 3:
               return var1.m23;
            default:
               throw new IllegalArgumentException();
            }
         case 3:
            switch(var3) {
            case 0:
               return var1.m30;
            case 1:
               return var1.m31;
            case 2:
               return var1.m32;
            case 3:
               return var1.m33;
            }
         }

         throw new IllegalArgumentException();
      }

      public Matrix4f set(Matrix4f var1, int var2, int var3, float var4) {
         switch(var2) {
         case 0:
            switch(var3) {
            case 0:
               return var1.m00(var4);
            case 1:
               return var1.m01(var4);
            case 2:
               return var1.m02(var4);
            case 3:
               return var1.m03(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 1:
            switch(var3) {
            case 0:
               return var1.m10(var4);
            case 1:
               return var1.m11(var4);
            case 2:
               return var1.m12(var4);
            case 3:
               return var1.m13(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 2:
            switch(var3) {
            case 0:
               return var1.m20(var4);
            case 1:
               return var1.m21(var4);
            case 2:
               return var1.m22(var4);
            case 3:
               return var1.m23(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 3:
            switch(var3) {
            case 0:
               return var1.m30(var4);
            case 1:
               return var1.m31(var4);
            case 2:
               return var1.m32(var4);
            case 3:
               return var1.m33(var4);
            }
         }

         throw new IllegalArgumentException();
      }

      public double get(Matrix4d var1, int var2, int var3) {
         switch(var2) {
         case 0:
            switch(var3) {
            case 0:
               return var1.m00;
            case 1:
               return var1.m01;
            case 2:
               return var1.m02;
            case 3:
               return var1.m03;
            default:
               throw new IllegalArgumentException();
            }
         case 1:
            switch(var3) {
            case 0:
               return var1.m10;
            case 1:
               return var1.m11;
            case 2:
               return var1.m12;
            case 3:
               return var1.m13;
            default:
               throw new IllegalArgumentException();
            }
         case 2:
            switch(var3) {
            case 0:
               return var1.m20;
            case 1:
               return var1.m21;
            case 2:
               return var1.m22;
            case 3:
               return var1.m23;
            default:
               throw new IllegalArgumentException();
            }
         case 3:
            switch(var3) {
            case 0:
               return var1.m30;
            case 1:
               return var1.m31;
            case 2:
               return var1.m32;
            case 3:
               return var1.m33;
            }
         }

         throw new IllegalArgumentException();
      }

      public Matrix4d set(Matrix4d var1, int var2, int var3, double var4) {
         switch(var2) {
         case 0:
            switch(var3) {
            case 0:
               return var1.m00(var4);
            case 1:
               return var1.m01(var4);
            case 2:
               return var1.m02(var4);
            case 3:
               return var1.m03(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 1:
            switch(var3) {
            case 0:
               return var1.m10(var4);
            case 1:
               return var1.m11(var4);
            case 2:
               return var1.m12(var4);
            case 3:
               return var1.m13(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 2:
            switch(var3) {
            case 0:
               return var1.m20(var4);
            case 1:
               return var1.m21(var4);
            case 2:
               return var1.m22(var4);
            case 3:
               return var1.m23(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 3:
            switch(var3) {
            case 0:
               return var1.m30(var4);
            case 1:
               return var1.m31(var4);
            case 2:
               return var1.m32(var4);
            case 3:
               return var1.m33(var4);
            }
         }

         throw new IllegalArgumentException();
      }

      public float get(Matrix3f var1, int var2, int var3) {
         switch(var2) {
         case 0:
            switch(var3) {
            case 0:
               return var1.m00;
            case 1:
               return var1.m01;
            case 2:
               return var1.m02;
            default:
               throw new IllegalArgumentException();
            }
         case 1:
            switch(var3) {
            case 0:
               return var1.m10;
            case 1:
               return var1.m11;
            case 2:
               return var1.m12;
            default:
               throw new IllegalArgumentException();
            }
         case 2:
            switch(var3) {
            case 0:
               return var1.m20;
            case 1:
               return var1.m21;
            case 2:
               return var1.m22;
            }
         }

         throw new IllegalArgumentException();
      }

      public Matrix3f set(Matrix3f var1, int var2, int var3, float var4) {
         switch(var2) {
         case 0:
            switch(var3) {
            case 0:
               return var1.m00(var4);
            case 1:
               return var1.m01(var4);
            case 2:
               return var1.m02(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 1:
            switch(var3) {
            case 0:
               return var1.m10(var4);
            case 1:
               return var1.m11(var4);
            case 2:
               return var1.m12(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 2:
            switch(var3) {
            case 0:
               return var1.m20(var4);
            case 1:
               return var1.m21(var4);
            case 2:
               return var1.m22(var4);
            }
         }

         throw new IllegalArgumentException();
      }

      public double get(Matrix3d var1, int var2, int var3) {
         switch(var2) {
         case 0:
            switch(var3) {
            case 0:
               return var1.m00;
            case 1:
               return var1.m01;
            case 2:
               return var1.m02;
            default:
               throw new IllegalArgumentException();
            }
         case 1:
            switch(var3) {
            case 0:
               return var1.m10;
            case 1:
               return var1.m11;
            case 2:
               return var1.m12;
            default:
               throw new IllegalArgumentException();
            }
         case 2:
            switch(var3) {
            case 0:
               return var1.m20;
            case 1:
               return var1.m21;
            case 2:
               return var1.m22;
            }
         }

         throw new IllegalArgumentException();
      }

      public Matrix3d set(Matrix3d var1, int var2, int var3, double var4) {
         switch(var2) {
         case 0:
            switch(var3) {
            case 0:
               return var1.m00(var4);
            case 1:
               return var1.m01(var4);
            case 2:
               return var1.m02(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 1:
            switch(var3) {
            case 0:
               return var1.m10(var4);
            case 1:
               return var1.m11(var4);
            case 2:
               return var1.m12(var4);
            default:
               throw new IllegalArgumentException();
            }
         case 2:
            switch(var3) {
            case 0:
               return var1.m20(var4);
            case 1:
               return var1.m21(var4);
            case 2:
               return var1.m22(var4);
            }
         }

         throw new IllegalArgumentException();
      }

      public Vector4f getColumn(Matrix4f var1, int var2, Vector4f var3) {
         switch(var2) {
         case 0:
            return var3.set(var1.m00, var1.m01, var1.m02, var1.m03);
         case 1:
            return var3.set(var1.m10, var1.m11, var1.m12, var1.m13);
         case 2:
            return var3.set(var1.m20, var1.m21, var1.m22, var1.m23);
         case 3:
            return var3.set(var1.m30, var1.m31, var1.m32, var1.m33);
         default:
            throw new IndexOutOfBoundsException();
         }
      }

      public Matrix4f setColumn(Vector4f var1, int var2, Matrix4f var3) {
         switch(var2) {
         case 0:
            return var3._m00(var1.x)._m01(var1.y)._m02(var1.z)._m03(var1.w);
         case 1:
            return var3._m10(var1.x)._m11(var1.y)._m12(var1.z)._m13(var1.w);
         case 2:
            return var3._m20(var1.x)._m21(var1.y)._m22(var1.z)._m23(var1.w);
         case 3:
            return var3._m30(var1.x)._m31(var1.y)._m32(var1.z)._m33(var1.w);
         default:
            throw new IndexOutOfBoundsException();
         }
      }

      public Matrix4f setColumn(Vector4fc var1, int var2, Matrix4f var3) {
         switch(var2) {
         case 0:
            return var3._m00(var1.x())._m01(var1.y())._m02(var1.z())._m03(var1.w());
         case 1:
            return var3._m10(var1.x())._m11(var1.y())._m12(var1.z())._m13(var1.w());
         case 2:
            return var3._m20(var1.x())._m21(var1.y())._m22(var1.z())._m23(var1.w());
         case 3:
            return var3._m30(var1.x())._m31(var1.y())._m32(var1.z())._m33(var1.w());
         default:
            throw new IndexOutOfBoundsException();
         }
      }

      public void copy(Matrix4f var1, Matrix4f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m03(var1.m03())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m13(var1.m13())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m23(var1.m23())._m30(var1.m30())._m31(var1.m31())._m32(var1.m32())._m33(var1.m33());
      }

      public void copy(Matrix3f var1, Matrix4f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m03(0.0F)._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m13(0.0F)._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F);
      }

      public void copy(Matrix4f var1, Matrix3f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22());
      }

      public void copy(Matrix3f var1, Matrix4x3f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m30(0.0F)._m31(0.0F)._m32(0.0F);
      }

      public void copy(Matrix3x2f var1, Matrix3x2f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11())._m20(var1.m20())._m21(var1.m21());
      }

      public void copy(Matrix3x2d var1, Matrix3x2d var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11())._m20(var1.m20())._m21(var1.m21());
      }

      public void copy(Matrix2f var1, Matrix2f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11());
      }

      public void copy(Matrix2d var1, Matrix2d var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11());
      }

      public void copy(Matrix2f var1, Matrix3f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(0.0F)._m10(var1.m10())._m11(var1.m11())._m12(0.0F)._m20(0.0F)._m21(0.0F)._m22(1.0F);
      }

      public void copy(Matrix3f var1, Matrix2f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11());
      }

      public void copy(Matrix2f var1, Matrix3x2f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11())._m20(0.0F)._m21(0.0F);
      }

      public void copy(Matrix3x2f var1, Matrix2f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11());
      }

      public void copy(Matrix2d var1, Matrix3d var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(0.0D)._m10(var1.m10())._m11(var1.m11())._m12(0.0D)._m20(0.0D)._m21(0.0D)._m22(1.0D);
      }

      public void copy(Matrix3d var1, Matrix2d var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11());
      }

      public void copy(Matrix2d var1, Matrix3x2d var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11())._m20(0.0D)._m21(0.0D);
      }

      public void copy(Matrix3x2d var1, Matrix2d var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m10(var1.m10())._m11(var1.m11());
      }

      public void copy3x3(Matrix4f var1, Matrix4f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22());
      }

      public void copy3x3(Matrix4x3f var1, Matrix4x3f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22());
      }

      public void copy3x3(Matrix3f var1, Matrix4x3f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22());
      }

      public void copy3x3(Matrix3f var1, Matrix4f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22());
      }

      public void copy4x3(Matrix4x3f var1, Matrix4f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m30(var1.m30())._m31(var1.m31())._m32(var1.m32());
      }

      public void copy4x3(Matrix4f var1, Matrix4f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m30(var1.m30())._m31(var1.m31())._m32(var1.m32());
      }

      public void copy(Matrix4f var1, Matrix4x3f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m30(var1.m30())._m31(var1.m31())._m32(var1.m32());
      }

      public void copy(Matrix4x3f var1, Matrix4f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m03(0.0F)._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m13(0.0F)._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m23(0.0F)._m30(var1.m30())._m31(var1.m31())._m32(var1.m32())._m33(1.0F);
      }

      public void copy(Matrix4x3f var1, Matrix4x3f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22())._m30(var1.m30())._m31(var1.m31())._m32(var1.m32());
      }

      public void copy(Matrix3f var1, Matrix3f var2) {
         var2._m00(var1.m00())._m01(var1.m01())._m02(var1.m02())._m10(var1.m10())._m11(var1.m11())._m12(var1.m12())._m20(var1.m20())._m21(var1.m21())._m22(var1.m22());
      }

      public void copy(float[] var1, int var2, Matrix4f var3) {
         var3._m00(var1[var2 + 0])._m01(var1[var2 + 1])._m02(var1[var2 + 2])._m03(var1[var2 + 3])._m10(var1[var2 + 4])._m11(var1[var2 + 5])._m12(var1[var2 + 6])._m13(var1[var2 + 7])._m20(var1[var2 + 8])._m21(var1[var2 + 9])._m22(var1[var2 + 10])._m23(var1[var2 + 11])._m30(var1[var2 + 12])._m31(var1[var2 + 13])._m32(var1[var2 + 14])._m33(var1[var2 + 15]);
      }

      public void copyTransposed(float[] var1, int var2, Matrix4f var3) {
         var3._m00(var1[var2 + 0])._m10(var1[var2 + 1])._m20(var1[var2 + 2])._m30(var1[var2 + 3])._m01(var1[var2 + 4])._m11(var1[var2 + 5])._m21(var1[var2 + 6])._m31(var1[var2 + 7])._m02(var1[var2 + 8])._m12(var1[var2 + 9])._m22(var1[var2 + 10])._m32(var1[var2 + 11])._m03(var1[var2 + 12])._m13(var1[var2 + 13])._m23(var1[var2 + 14])._m33(var1[var2 + 15]);
      }

      public void copy(float[] var1, int var2, Matrix3f var3) {
         var3._m00(var1[var2 + 0])._m01(var1[var2 + 1])._m02(var1[var2 + 2])._m10(var1[var2 + 3])._m11(var1[var2 + 4])._m12(var1[var2 + 5])._m20(var1[var2 + 6])._m21(var1[var2 + 7])._m22(var1[var2 + 8]);
      }

      public void copy(float[] var1, int var2, Matrix4x3f var3) {
         var3._m00(var1[var2 + 0])._m01(var1[var2 + 1])._m02(var1[var2 + 2])._m10(var1[var2 + 3])._m11(var1[var2 + 4])._m12(var1[var2 + 5])._m20(var1[var2 + 6])._m21(var1[var2 + 7])._m22(var1[var2 + 8])._m30(var1[var2 + 9])._m31(var1[var2 + 10])._m32(var1[var2 + 11]);
      }

      public void copy(float[] var1, int var2, Matrix3x2f var3) {
         var3._m00(var1[var2 + 0])._m01(var1[var2 + 1])._m10(var1[var2 + 2])._m11(var1[var2 + 3])._m20(var1[var2 + 4])._m21(var1[var2 + 5]);
      }

      public void copy(double[] var1, int var2, Matrix3x2d var3) {
         var3._m00(var1[var2 + 0])._m01(var1[var2 + 1])._m10(var1[var2 + 2])._m11(var1[var2 + 3])._m20(var1[var2 + 4])._m21(var1[var2 + 5]);
      }

      public void copy(float[] var1, int var2, Matrix2f var3) {
         var3._m00(var1[var2 + 0])._m01(var1[var2 + 1])._m10(var1[var2 + 2])._m11(var1[var2 + 3]);
      }

      public void copy(double[] var1, int var2, Matrix2d var3) {
         var3._m00(var1[var2 + 0])._m01(var1[var2 + 1])._m10(var1[var2 + 2])._m11(var1[var2 + 3]);
      }

      public void copy(Matrix4f var1, float[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = var1.m02();
         var2[var3 + 3] = var1.m03();
         var2[var3 + 4] = var1.m10();
         var2[var3 + 5] = var1.m11();
         var2[var3 + 6] = var1.m12();
         var2[var3 + 7] = var1.m13();
         var2[var3 + 8] = var1.m20();
         var2[var3 + 9] = var1.m21();
         var2[var3 + 10] = var1.m22();
         var2[var3 + 11] = var1.m23();
         var2[var3 + 12] = var1.m30();
         var2[var3 + 13] = var1.m31();
         var2[var3 + 14] = var1.m32();
         var2[var3 + 15] = var1.m33();
      }

      public void copy(Matrix3f var1, float[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = var1.m02();
         var2[var3 + 3] = var1.m10();
         var2[var3 + 4] = var1.m11();
         var2[var3 + 5] = var1.m12();
         var2[var3 + 6] = var1.m20();
         var2[var3 + 7] = var1.m21();
         var2[var3 + 8] = var1.m22();
      }

      public void copy(Matrix4x3f var1, float[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = var1.m02();
         var2[var3 + 3] = var1.m10();
         var2[var3 + 4] = var1.m11();
         var2[var3 + 5] = var1.m12();
         var2[var3 + 6] = var1.m20();
         var2[var3 + 7] = var1.m21();
         var2[var3 + 8] = var1.m22();
         var2[var3 + 9] = var1.m30();
         var2[var3 + 10] = var1.m31();
         var2[var3 + 11] = var1.m32();
      }

      public void copy(Matrix3x2f var1, float[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = var1.m10();
         var2[var3 + 3] = var1.m11();
         var2[var3 + 4] = var1.m20();
         var2[var3 + 5] = var1.m21();
      }

      public void copy(Matrix3x2d var1, double[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = var1.m10();
         var2[var3 + 3] = var1.m11();
         var2[var3 + 4] = var1.m20();
         var2[var3 + 5] = var1.m21();
      }

      public void copy(Matrix2f var1, float[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = var1.m10();
         var2[var3 + 3] = var1.m11();
      }

      public void copy(Matrix2d var1, double[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = var1.m10();
         var2[var3 + 3] = var1.m11();
      }

      public void copy4x4(Matrix4x3f var1, float[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = var1.m02();
         var2[var3 + 3] = 0.0F;
         var2[var3 + 4] = var1.m10();
         var2[var3 + 5] = var1.m11();
         var2[var3 + 6] = var1.m12();
         var2[var3 + 7] = 0.0F;
         var2[var3 + 8] = var1.m20();
         var2[var3 + 9] = var1.m21();
         var2[var3 + 10] = var1.m22();
         var2[var3 + 11] = 0.0F;
         var2[var3 + 12] = var1.m30();
         var2[var3 + 13] = var1.m31();
         var2[var3 + 14] = var1.m32();
         var2[var3 + 15] = 1.0F;
      }

      public void copy4x4(Matrix4x3d var1, float[] var2, int var3) {
         var2[var3 + 0] = (float)var1.m00();
         var2[var3 + 1] = (float)var1.m01();
         var2[var3 + 2] = (float)var1.m02();
         var2[var3 + 3] = 0.0F;
         var2[var3 + 4] = (float)var1.m10();
         var2[var3 + 5] = (float)var1.m11();
         var2[var3 + 6] = (float)var1.m12();
         var2[var3 + 7] = 0.0F;
         var2[var3 + 8] = (float)var1.m20();
         var2[var3 + 9] = (float)var1.m21();
         var2[var3 + 10] = (float)var1.m22();
         var2[var3 + 11] = 0.0F;
         var2[var3 + 12] = (float)var1.m30();
         var2[var3 + 13] = (float)var1.m31();
         var2[var3 + 14] = (float)var1.m32();
         var2[var3 + 15] = 1.0F;
      }

      public void copy4x4(Matrix4x3d var1, double[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = var1.m02();
         var2[var3 + 3] = 0.0D;
         var2[var3 + 4] = var1.m10();
         var2[var3 + 5] = var1.m11();
         var2[var3 + 6] = var1.m12();
         var2[var3 + 7] = 0.0D;
         var2[var3 + 8] = var1.m20();
         var2[var3 + 9] = var1.m21();
         var2[var3 + 10] = var1.m22();
         var2[var3 + 11] = 0.0D;
         var2[var3 + 12] = var1.m30();
         var2[var3 + 13] = var1.m31();
         var2[var3 + 14] = var1.m32();
         var2[var3 + 15] = 1.0D;
      }

      public void copy3x3(Matrix3x2f var1, float[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = 0.0F;
         var2[var3 + 3] = var1.m10();
         var2[var3 + 4] = var1.m11();
         var2[var3 + 5] = 0.0F;
         var2[var3 + 6] = var1.m20();
         var2[var3 + 7] = var1.m21();
         var2[var3 + 8] = 1.0F;
      }

      public void copy3x3(Matrix3x2d var1, double[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = 0.0D;
         var2[var3 + 3] = var1.m10();
         var2[var3 + 4] = var1.m11();
         var2[var3 + 5] = 0.0D;
         var2[var3 + 6] = var1.m20();
         var2[var3 + 7] = var1.m21();
         var2[var3 + 8] = 1.0D;
      }

      public void copy4x4(Matrix3x2f var1, float[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = 0.0F;
         var2[var3 + 3] = 0.0F;
         var2[var3 + 4] = var1.m10();
         var2[var3 + 5] = var1.m11();
         var2[var3 + 6] = 0.0F;
         var2[var3 + 7] = 0.0F;
         var2[var3 + 8] = 0.0F;
         var2[var3 + 9] = 0.0F;
         var2[var3 + 10] = 1.0F;
         var2[var3 + 11] = 0.0F;
         var2[var3 + 12] = var1.m20();
         var2[var3 + 13] = var1.m21();
         var2[var3 + 14] = 0.0F;
         var2[var3 + 15] = 1.0F;
      }

      public void copy4x4(Matrix3x2d var1, double[] var2, int var3) {
         var2[var3 + 0] = var1.m00();
         var2[var3 + 1] = var1.m01();
         var2[var3 + 2] = 0.0D;
         var2[var3 + 3] = 0.0D;
         var2[var3 + 4] = var1.m10();
         var2[var3 + 5] = var1.m11();
         var2[var3 + 6] = 0.0D;
         var2[var3 + 7] = 0.0D;
         var2[var3 + 8] = 0.0D;
         var2[var3 + 9] = 0.0D;
         var2[var3 + 10] = 1.0D;
         var2[var3 + 11] = 0.0D;
         var2[var3 + 12] = var1.m20();
         var2[var3 + 13] = var1.m21();
         var2[var3 + 14] = 0.0D;
         var2[var3 + 15] = 1.0D;
      }

      public void identity(Matrix4f var1) {
         var1._m00(1.0F)._m01(0.0F)._m02(0.0F)._m03(0.0F)._m10(0.0F)._m11(1.0F)._m12(0.0F)._m13(0.0F)._m20(0.0F)._m21(0.0F)._m22(1.0F)._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(1.0F);
      }

      public void identity(Matrix4x3f var1) {
         var1._m00(1.0F)._m01(0.0F)._m02(0.0F)._m10(0.0F)._m11(1.0F)._m12(0.0F)._m20(0.0F)._m21(0.0F)._m22(1.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F);
      }

      public void identity(Matrix3f var1) {
         var1._m00(1.0F)._m01(0.0F)._m02(0.0F)._m10(0.0F)._m11(1.0F)._m12(0.0F)._m20(0.0F)._m21(0.0F)._m22(1.0F);
      }

      public void identity(Matrix3x2f var1) {
         var1._m00(1.0F)._m01(0.0F)._m10(0.0F)._m11(1.0F)._m20(0.0F)._m21(0.0F);
      }

      public void identity(Matrix3x2d var1) {
         var1._m00(1.0D)._m01(0.0D)._m10(0.0D)._m11(1.0D)._m20(0.0D)._m21(0.0D);
      }

      public void identity(Matrix2f var1) {
         var1._m00(1.0F)._m01(0.0F)._m10(0.0F)._m11(1.0F);
      }

      public void swap(Matrix4f var1, Matrix4f var2) {
         float var3 = var1.m00();
         var1._m00(var2.m00());
         var2._m00(var3);
         var3 = var1.m01();
         var1._m01(var2.m01());
         var2._m01(var3);
         var3 = var1.m02();
         var1._m02(var2.m02());
         var2._m02(var3);
         var3 = var1.m03();
         var1._m03(var2.m03());
         var2._m03(var3);
         var3 = var1.m10();
         var1._m10(var2.m10());
         var2._m10(var3);
         var3 = var1.m11();
         var1._m11(var2.m11());
         var2._m11(var3);
         var3 = var1.m12();
         var1._m12(var2.m12());
         var2._m12(var3);
         var3 = var1.m13();
         var1._m13(var2.m13());
         var2._m13(var3);
         var3 = var1.m20();
         var1._m20(var2.m20());
         var2._m20(var3);
         var3 = var1.m21();
         var1._m21(var2.m21());
         var2._m21(var3);
         var3 = var1.m22();
         var1._m22(var2.m22());
         var2._m22(var3);
         var3 = var1.m23();
         var1._m23(var2.m23());
         var2._m23(var3);
         var3 = var1.m30();
         var1._m30(var2.m30());
         var2._m30(var3);
         var3 = var1.m31();
         var1._m31(var2.m31());
         var2._m31(var3);
         var3 = var1.m32();
         var1._m32(var2.m32());
         var2._m32(var3);
         var3 = var1.m33();
         var1._m33(var2.m33());
         var2._m33(var3);
      }

      public void swap(Matrix4x3f var1, Matrix4x3f var2) {
         float var3 = var1.m00();
         var1._m00(var2.m00());
         var2._m00(var3);
         var3 = var1.m01();
         var1._m01(var2.m01());
         var2._m01(var3);
         var3 = var1.m02();
         var1._m02(var2.m02());
         var2._m02(var3);
         var3 = var1.m10();
         var1._m10(var2.m10());
         var2._m10(var3);
         var3 = var1.m11();
         var1._m11(var2.m11());
         var2._m11(var3);
         var3 = var1.m12();
         var1._m12(var2.m12());
         var2._m12(var3);
         var3 = var1.m20();
         var1._m20(var2.m20());
         var2._m20(var3);
         var3 = var1.m21();
         var1._m21(var2.m21());
         var2._m21(var3);
         var3 = var1.m22();
         var1._m22(var2.m22());
         var2._m22(var3);
         var3 = var1.m30();
         var1._m30(var2.m30());
         var2._m30(var3);
         var3 = var1.m31();
         var1._m31(var2.m31());
         var2._m31(var3);
         var3 = var1.m32();
         var1._m32(var2.m32());
         var2._m32(var3);
      }

      public void swap(Matrix3f var1, Matrix3f var2) {
         float var3 = var1.m00();
         var1._m00(var2.m00());
         var2._m00(var3);
         var3 = var1.m01();
         var1._m01(var2.m01());
         var2._m01(var3);
         var3 = var1.m02();
         var1._m02(var2.m02());
         var2._m02(var3);
         var3 = var1.m10();
         var1._m10(var2.m10());
         var2._m10(var3);
         var3 = var1.m11();
         var1._m11(var2.m11());
         var2._m11(var3);
         var3 = var1.m12();
         var1._m12(var2.m12());
         var2._m12(var3);
         var3 = var1.m20();
         var1._m20(var2.m20());
         var2._m20(var3);
         var3 = var1.m21();
         var1._m21(var2.m21());
         var2._m21(var3);
         var3 = var1.m22();
         var1._m22(var2.m22());
         var2._m22(var3);
      }

      public void swap(Matrix2f var1, Matrix2f var2) {
         float var3 = var1.m00();
         var1._m00(var2.m00());
         var2._m00(var3);
         var3 = var1.m01();
         var1._m00(var2.m01());
         var2._m01(var3);
         var3 = var1.m10();
         var1._m00(var2.m10());
         var2._m10(var3);
         var3 = var1.m11();
         var1._m00(var2.m11());
         var2._m11(var3);
      }

      public void swap(Matrix2d var1, Matrix2d var2) {
         double var3 = var1.m00();
         var1._m00(var2.m00());
         var2._m00(var3);
         var3 = var1.m01();
         var1._m00(var2.m01());
         var2._m01(var3);
         var3 = var1.m10();
         var1._m00(var2.m10());
         var2._m10(var3);
         var3 = var1.m11();
         var1._m00(var2.m11());
         var2._m11(var3);
      }

      public void zero(Matrix4f var1) {
         var1._m00(0.0F)._m01(0.0F)._m02(0.0F)._m03(0.0F)._m10(0.0F)._m11(0.0F)._m12(0.0F)._m13(0.0F)._m20(0.0F)._m21(0.0F)._m22(0.0F)._m23(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F)._m33(0.0F);
      }

      public void zero(Matrix4x3f var1) {
         var1._m00(0.0F)._m01(0.0F)._m02(0.0F)._m10(0.0F)._m11(0.0F)._m12(0.0F)._m20(0.0F)._m21(0.0F)._m22(0.0F)._m30(0.0F)._m31(0.0F)._m32(0.0F);
      }

      public void zero(Matrix3f var1) {
         var1._m00(0.0F)._m01(0.0F)._m02(0.0F)._m10(0.0F)._m11(0.0F)._m12(0.0F)._m20(0.0F)._m21(0.0F)._m22(0.0F);
      }

      public void zero(Matrix3x2f var1) {
         var1._m00(0.0F)._m01(0.0F)._m10(0.0F)._m11(0.0F)._m20(0.0F)._m21(0.0F);
      }

      public void zero(Matrix3x2d var1) {
         var1._m00(0.0D)._m01(0.0D)._m10(0.0D)._m11(0.0D)._m20(0.0D)._m21(0.0D);
      }

      public void zero(Matrix2f var1) {
         var1._m00(0.0F)._m01(0.0F)._m10(0.0F)._m11(0.0F);
      }

      public void zero(Matrix2d var1) {
         var1._m00(0.0D)._m01(0.0D)._m10(0.0D)._m11(0.0D);
      }

      public void putMatrix3f(Quaternionf var1, int var2, ByteBuffer var3) {
         float var4 = var1.w * var1.w;
         float var5 = var1.x * var1.x;
         float var6 = var1.y * var1.y;
         float var7 = var1.z * var1.z;
         float var8 = var1.z * var1.w;
         float var9 = var1.x * var1.y;
         float var10 = var1.x * var1.z;
         float var11 = var1.y * var1.w;
         float var12 = var1.y * var1.z;
         float var13 = var1.x * var1.w;
         var3.putFloat(var2, var4 + var5 - var7 - var6).putFloat(var2 + 4, var9 + var8 + var8 + var9).putFloat(var2 + 8, var10 - var11 + var10 - var11).putFloat(var2 + 12, -var8 + var9 - var8 + var9).putFloat(var2 + 16, var6 - var7 + var4 - var5).putFloat(var2 + 20, var12 + var12 + var13 + var13).putFloat(var2 + 24, var11 + var10 + var10 + var11).putFloat(var2 + 28, var12 + var12 - var13 - var13).putFloat(var2 + 32, var7 - var6 - var5 + var4);
      }

      public void putMatrix3f(Quaternionf var1, int var2, FloatBuffer var3) {
         float var4 = var1.w * var1.w;
         float var5 = var1.x * var1.x;
         float var6 = var1.y * var1.y;
         float var7 = var1.z * var1.z;
         float var8 = var1.z * var1.w;
         float var9 = var1.x * var1.y;
         float var10 = var1.x * var1.z;
         float var11 = var1.y * var1.w;
         float var12 = var1.y * var1.z;
         float var13 = var1.x * var1.w;
         var3.put(var2, var4 + var5 - var7 - var6).put(var2 + 1, var9 + var8 + var8 + var9).put(var2 + 2, var10 - var11 + var10 - var11).put(var2 + 3, -var8 + var9 - var8 + var9).put(var2 + 4, var6 - var7 + var4 - var5).put(var2 + 5, var12 + var12 + var13 + var13).put(var2 + 6, var11 + var10 + var10 + var11).put(var2 + 7, var12 + var12 - var13 - var13).put(var2 + 8, var7 - var6 - var5 + var4);
      }

      public void putMatrix4f(Quaternionf var1, int var2, ByteBuffer var3) {
         float var4 = var1.w * var1.w;
         float var5 = var1.x * var1.x;
         float var6 = var1.y * var1.y;
         float var7 = var1.z * var1.z;
         float var8 = var1.z * var1.w;
         float var9 = var1.x * var1.y;
         float var10 = var1.x * var1.z;
         float var11 = var1.y * var1.w;
         float var12 = var1.y * var1.z;
         float var13 = var1.x * var1.w;
         var3.putFloat(var2, var4 + var5 - var7 - var6).putFloat(var2 + 4, var9 + var8 + var8 + var9).putFloat(var2 + 8, var10 - var11 + var10 - var11).putFloat(var2 + 12, 0.0F).putFloat(var2 + 16, -var8 + var9 - var8 + var9).putFloat(var2 + 20, var6 - var7 + var4 - var5).putFloat(var2 + 24, var12 + var12 + var13 + var13).putFloat(var2 + 28, 0.0F).putFloat(var2 + 32, var11 + var10 + var10 + var11).putFloat(var2 + 36, var12 + var12 - var13 - var13).putFloat(var2 + 40, var7 - var6 - var5 + var4).putFloat(var2 + 44, 0.0F).putLong(var2 + 48, 0L).putLong(var2 + 56, 4575657221408423936L);
      }

      public void putMatrix4f(Quaternionf var1, int var2, FloatBuffer var3) {
         float var4 = var1.w * var1.w;
         float var5 = var1.x * var1.x;
         float var6 = var1.y * var1.y;
         float var7 = var1.z * var1.z;
         float var8 = var1.z * var1.w;
         float var9 = var1.x * var1.y;
         float var10 = var1.x * var1.z;
         float var11 = var1.y * var1.w;
         float var12 = var1.y * var1.z;
         float var13 = var1.x * var1.w;
         var3.put(var2, var4 + var5 - var7 - var6).put(var2 + 1, var9 + var8 + var8 + var9).put(var2 + 2, var10 - var11 + var10 - var11).put(var2 + 3, 0.0F).put(var2 + 4, -var8 + var9 - var8 + var9).put(var2 + 5, var6 - var7 + var4 - var5).put(var2 + 6, var12 + var12 + var13 + var13).put(var2 + 7, 0.0F).put(var2 + 8, var11 + var10 + var10 + var11).put(var2 + 9, var12 + var12 - var13 - var13).put(var2 + 10, var7 - var6 - var5 + var4).put(var2 + 11, 0.0F).put(var2 + 12, 0.0F).put(var2 + 13, 0.0F).put(var2 + 14, 0.0F).put(var2 + 15, 1.0F);
      }

      public void putMatrix4x3f(Quaternionf var1, int var2, ByteBuffer var3) {
         float var4 = var1.w * var1.w;
         float var5 = var1.x * var1.x;
         float var6 = var1.y * var1.y;
         float var7 = var1.z * var1.z;
         float var8 = var1.z * var1.w;
         float var9 = var1.x * var1.y;
         float var10 = var1.x * var1.z;
         float var11 = var1.y * var1.w;
         float var12 = var1.y * var1.z;
         float var13 = var1.x * var1.w;
         var3.putFloat(var2, var4 + var5 - var7 - var6).putFloat(var2 + 4, var9 + var8 + var8 + var9).putFloat(var2 + 8, var10 - var11 + var10 - var11).putFloat(var2 + 12, -var8 + var9 - var8 + var9).putFloat(var2 + 16, var6 - var7 + var4 - var5).putFloat(var2 + 20, var12 + var12 + var13 + var13).putFloat(var2 + 24, var11 + var10 + var10 + var11).putFloat(var2 + 28, var12 + var12 - var13 - var13).putFloat(var2 + 32, var7 - var6 - var5 + var4).putLong(var2 + 36, 0L).putFloat(var2 + 44, 0.0F);
      }

      public void putMatrix4x3f(Quaternionf var1, int var2, FloatBuffer var3) {
         float var4 = var1.w * var1.w;
         float var5 = var1.x * var1.x;
         float var6 = var1.y * var1.y;
         float var7 = var1.z * var1.z;
         float var8 = var1.z * var1.w;
         float var9 = var1.x * var1.y;
         float var10 = var1.x * var1.z;
         float var11 = var1.y * var1.w;
         float var12 = var1.y * var1.z;
         float var13 = var1.x * var1.w;
         var3.put(var2, var4 + var5 - var7 - var6).put(var2 + 1, var9 + var8 + var8 + var9).put(var2 + 2, var10 - var11 + var10 - var11).put(var2 + 3, -var8 + var9 - var8 + var9).put(var2 + 4, var6 - var7 + var4 - var5).put(var2 + 5, var12 + var12 + var13 + var13).put(var2 + 6, var11 + var10 + var10 + var11).put(var2 + 7, var12 + var12 - var13 - var13).put(var2 + 8, var7 - var6 - var5 + var4).put(var2 + 9, 0.0F).put(var2 + 10, 0.0F).put(var2 + 11, 0.0F);
      }
   }

   public static class MemUtilUnsafe extends MemUtil.MemUtilNIO {
      public static final Unsafe UNSAFE = getUnsafeInstance();
      public static final long ADDRESS;
      public static final long Matrix2f_m00;
      public static final long Matrix3f_m00;
      public static final long Matrix3d_m00;
      public static final long Matrix4f_m00;
      public static final long Matrix4d_m00;
      public static final long Matrix4x3f_m00;
      public static final long Matrix3x2f_m00;
      public static final long Vector4f_x;
      public static final long Vector4i_x;
      public static final long Vector3f_x;
      public static final long Vector3i_x;
      public static final long Vector2f_x;
      public static final long Vector2i_x;
      public static final long Quaternionf_x;
      public static final long floatArrayOffset;

      private static long findBufferAddress() {
         try {
            return UNSAFE.objectFieldOffset(getDeclaredField(Buffer.class, "address"));
         } catch (Exception var1) {
            throw new UnsupportedOperationException(var1);
         }
      }

      private static long checkMatrix4f() throws NoSuchFieldException, SecurityException {
         Field var0 = Matrix4f.class.getDeclaredField("m00");
         long var1 = UNSAFE.objectFieldOffset(var0);

         for(int var3 = 1; var3 < 16; ++var3) {
            int var4 = var3 >>> 2;
            int var5 = var3 & 3;
            var0 = Matrix4f.class.getDeclaredField("m" + var4 + var5);
            long var6 = UNSAFE.objectFieldOffset(var0);
            if (var6 != var1 + (long)(var3 << 2)) {
               throw new UnsupportedOperationException("Unexpected Matrix4f element offset");
            }
         }

         return var1;
      }

      private static long checkMatrix4d() throws NoSuchFieldException, SecurityException {
         Field var0 = Matrix4d.class.getDeclaredField("m00");
         long var1 = UNSAFE.objectFieldOffset(var0);

         for(int var3 = 1; var3 < 16; ++var3) {
            int var4 = var3 >>> 2;
            int var5 = var3 & 3;
            var0 = Matrix4d.class.getDeclaredField("m" + var4 + var5);
            long var6 = UNSAFE.objectFieldOffset(var0);
            if (var6 != var1 + (long)(var3 << 3)) {
               throw new UnsupportedOperationException("Unexpected Matrix4d element offset");
            }
         }

         return var1;
      }

      private static long checkMatrix4x3f() throws NoSuchFieldException, SecurityException {
         Field var0 = Matrix4x3f.class.getDeclaredField("m00");
         long var1 = UNSAFE.objectFieldOffset(var0);

         for(int var3 = 1; var3 < 12; ++var3) {
            int var4 = var3 / 3;
            int var5 = var3 % 3;
            var0 = Matrix4x3f.class.getDeclaredField("m" + var4 + var5);
            long var6 = UNSAFE.objectFieldOffset(var0);
            if (var6 != var1 + (long)(var3 << 2)) {
               throw new UnsupportedOperationException("Unexpected Matrix4x3f element offset");
            }
         }

         return var1;
      }

      private static long checkMatrix3f() throws NoSuchFieldException, SecurityException {
         Field var0 = Matrix3f.class.getDeclaredField("m00");
         long var1 = UNSAFE.objectFieldOffset(var0);

         for(int var3 = 1; var3 < 9; ++var3) {
            int var4 = var3 / 3;
            int var5 = var3 % 3;
            var0 = Matrix3f.class.getDeclaredField("m" + var4 + var5);
            long var6 = UNSAFE.objectFieldOffset(var0);
            if (var6 != var1 + (long)(var3 << 2)) {
               throw new UnsupportedOperationException("Unexpected Matrix3f element offset");
            }
         }

         return var1;
      }

      private static long checkMatrix3d() throws NoSuchFieldException, SecurityException {
         Field var0 = Matrix3d.class.getDeclaredField("m00");
         long var1 = UNSAFE.objectFieldOffset(var0);

         for(int var3 = 1; var3 < 9; ++var3) {
            int var4 = var3 / 3;
            int var5 = var3 % 3;
            var0 = Matrix3d.class.getDeclaredField("m" + var4 + var5);
            long var6 = UNSAFE.objectFieldOffset(var0);
            if (var6 != var1 + (long)(var3 << 3)) {
               throw new UnsupportedOperationException("Unexpected Matrix3d element offset");
            }
         }

         return var1;
      }

      private static long checkMatrix3x2f() throws NoSuchFieldException, SecurityException {
         Field var0 = Matrix3x2f.class.getDeclaredField("m00");
         long var1 = UNSAFE.objectFieldOffset(var0);

         for(int var3 = 1; var3 < 6; ++var3) {
            int var4 = var3 / 2;
            int var5 = var3 % 2;
            var0 = Matrix3x2f.class.getDeclaredField("m" + var4 + var5);
            long var6 = UNSAFE.objectFieldOffset(var0);
            if (var6 != var1 + (long)(var3 << 2)) {
               throw new UnsupportedOperationException("Unexpected Matrix3x2f element offset");
            }
         }

         return var1;
      }

      private static long checkMatrix2f() throws NoSuchFieldException, SecurityException {
         Field var0 = Matrix2f.class.getDeclaredField("m00");
         long var1 = UNSAFE.objectFieldOffset(var0);

         for(int var3 = 1; var3 < 4; ++var3) {
            int var4 = var3 / 2;
            int var5 = var3 % 2;
            var0 = Matrix2f.class.getDeclaredField("m" + var4 + var5);
            long var6 = UNSAFE.objectFieldOffset(var0);
            if (var6 != var1 + (long)(var3 << 2)) {
               throw new UnsupportedOperationException("Unexpected Matrix2f element offset");
            }
         }

         return var1;
      }

      private static long checkVector4f() throws NoSuchFieldException, SecurityException {
         Field var0 = Vector4f.class.getDeclaredField("x");
         long var1 = UNSAFE.objectFieldOffset(var0);
         String[] var3 = new String[]{"y", "z", "w"};

         for(int var4 = 1; var4 < 4; ++var4) {
            var0 = Vector4f.class.getDeclaredField(var3[var4 - 1]);
            long var5 = UNSAFE.objectFieldOffset(var0);
            if (var5 != var1 + (long)(var4 << 2)) {
               throw new UnsupportedOperationException("Unexpected Vector4f element offset");
            }
         }

         return var1;
      }

      private static long checkVector4i() throws NoSuchFieldException, SecurityException {
         Field var0 = Vector4i.class.getDeclaredField("x");
         long var1 = UNSAFE.objectFieldOffset(var0);
         String[] var3 = new String[]{"y", "z", "w"};

         for(int var4 = 1; var4 < 4; ++var4) {
            var0 = Vector4i.class.getDeclaredField(var3[var4 - 1]);
            long var5 = UNSAFE.objectFieldOffset(var0);
            if (var5 != var1 + (long)(var4 << 2)) {
               throw new UnsupportedOperationException("Unexpected Vector4i element offset");
            }
         }

         return var1;
      }

      private static long checkVector3f() throws NoSuchFieldException, SecurityException {
         Field var0 = Vector3f.class.getDeclaredField("x");
         long var1 = UNSAFE.objectFieldOffset(var0);
         String[] var3 = new String[]{"y", "z"};

         for(int var4 = 1; var4 < 3; ++var4) {
            var0 = Vector3f.class.getDeclaredField(var3[var4 - 1]);
            long var5 = UNSAFE.objectFieldOffset(var0);
            if (var5 != var1 + (long)(var4 << 2)) {
               throw new UnsupportedOperationException("Unexpected Vector3f element offset");
            }
         }

         return var1;
      }

      private static long checkVector3i() throws NoSuchFieldException, SecurityException {
         Field var0 = Vector3i.class.getDeclaredField("x");
         long var1 = UNSAFE.objectFieldOffset(var0);
         String[] var3 = new String[]{"y", "z"};

         for(int var4 = 1; var4 < 3; ++var4) {
            var0 = Vector3i.class.getDeclaredField(var3[var4 - 1]);
            long var5 = UNSAFE.objectFieldOffset(var0);
            if (var5 != var1 + (long)(var4 << 2)) {
               throw new UnsupportedOperationException("Unexpected Vector3i element offset");
            }
         }

         return var1;
      }

      private static long checkVector2f() throws NoSuchFieldException, SecurityException {
         Field var0 = Vector2f.class.getDeclaredField("x");
         long var1 = UNSAFE.objectFieldOffset(var0);
         var0 = Vector2f.class.getDeclaredField("y");
         long var3 = UNSAFE.objectFieldOffset(var0);
         if (var3 != var1 + 4L) {
            throw new UnsupportedOperationException("Unexpected Vector2f element offset");
         } else {
            return var1;
         }
      }

      private static long checkVector2i() throws NoSuchFieldException, SecurityException {
         Field var0 = Vector2i.class.getDeclaredField("x");
         long var1 = UNSAFE.objectFieldOffset(var0);
         var0 = Vector2i.class.getDeclaredField("y");
         long var3 = UNSAFE.objectFieldOffset(var0);
         if (var3 != var1 + 4L) {
            throw new UnsupportedOperationException("Unexpected Vector2i element offset");
         } else {
            return var1;
         }
      }

      private static long checkQuaternionf() throws NoSuchFieldException, SecurityException {
         Field var0 = Quaternionf.class.getDeclaredField("x");
         long var1 = UNSAFE.objectFieldOffset(var0);
         String[] var3 = new String[]{"y", "z", "w"};

         for(int var4 = 1; var4 < 4; ++var4) {
            var0 = Quaternionf.class.getDeclaredField(var3[var4 - 1]);
            long var5 = UNSAFE.objectFieldOffset(var0);
            if (var5 != var1 + (long)(var4 << 2)) {
               throw new UnsupportedOperationException("Unexpected Quaternionf element offset");
            }
         }

         return var1;
      }

      private static Field getDeclaredField(Class var0, String var1) throws NoSuchFieldException {
         Class var2 = var0;

         do {
            try {
               Field var3 = var2.getDeclaredField(var1);
               return var3;
            } catch (NoSuchFieldException var4) {
               var2 = var2.getSuperclass();
            } catch (SecurityException var5) {
               var2 = var2.getSuperclass();
            }
         } while(var2 != null);

         throw new NoSuchFieldException(var1 + " does not exist in " + var0.getName() + " or any of its superclasses.");
      }

      public static Unsafe getUnsafeInstance() throws SecurityException {
         Field[] var0 = Unsafe.class.getDeclaredFields();
         int var1 = 0;

         while(true) {
            label31: {
               if (var1 < var0.length) {
                  Field var2 = var0[var1];
                  if (!var2.getType().equals(Unsafe.class)) {
                     break label31;
                  }

                  int var3 = var2.getModifiers();
                  if (!Modifier.isStatic(var3) || !Modifier.isFinal(var3)) {
                     break label31;
                  }

                  var2.setAccessible(true);

                  try {
                     return (Unsafe)var2.get((Object)null);
                  } catch (IllegalAccessException var5) {
                  }
               }

               throw new UnsupportedOperationException();
            }

            ++var1;
         }
      }

      public static void put(Matrix4f var0, long var1) {
         for(int var3 = 0; var3 < 8; ++var3) {
            UNSAFE.putLong((Object)null, var1 + (long)(var3 << 3), UNSAFE.getLong(var0, Matrix4f_m00 + (long)(var3 << 3)));
         }

      }

      public static void put4x3(Matrix4f var0, long var1) {
         Unsafe var3 = UNSAFE;

         for(int var4 = 0; var4 < 4; ++var4) {
            var3.putLong((Object)null, var1 + (long)(12 * var4), var3.getLong(var0, Matrix4f_m00 + (long)(var4 << 4)));
         }

         var3.putFloat((Object)null, var1 + 8L, var0.m02());
         var3.putFloat((Object)null, var1 + 20L, var0.m12());
         var3.putFloat((Object)null, var1 + 32L, var0.m22());
         var3.putFloat((Object)null, var1 + 44L, var0.m32());
      }

      public static void put3x4(Matrix4f var0, long var1) {
         for(int var3 = 0; var3 < 6; ++var3) {
            UNSAFE.putLong((Object)null, var1 + (long)(var3 << 3), UNSAFE.getLong(var0, Matrix4f_m00 + (long)(var3 << 3)));
         }

      }

      public static void put(Matrix4x3f var0, long var1) {
         for(int var3 = 0; var3 < 6; ++var3) {
            UNSAFE.putLong((Object)null, var1 + (long)(var3 << 3), UNSAFE.getLong(var0, Matrix4x3f_m00 + (long)(var3 << 3)));
         }

      }

      public static void put4x4(Matrix4x3f var0, long var1) {
         for(int var3 = 0; var3 < 4; ++var3) {
            UNSAFE.putLong((Object)null, var1 + (long)(var3 << 4), UNSAFE.getLong(var0, Matrix4x3f_m00 + (long)(12 * var3)));
            long var4 = (long)UNSAFE.getInt(var0, Matrix4x3f_m00 + 8L + (long)(12 * var3)) & 4294967295L;
            UNSAFE.putLong((Object)null, var1 + 8L + (long)(var3 << 4), var4);
         }

         UNSAFE.putFloat((Object)null, var1 + 60L, 1.0F);
      }

      public static void put3x4(Matrix4x3f var0, long var1) {
         for(int var3 = 0; var3 < 3; ++var3) {
            UNSAFE.putLong((Object)null, var1 + (long)(var3 << 4), UNSAFE.getLong(var0, Matrix4x3f_m00 + (long)(12 * var3)));
            UNSAFE.putFloat((Object)null, var1 + (long)(var3 << 4) + 8L, UNSAFE.getFloat(var0, Matrix4x3f_m00 + 8L + (long)(12 * var3)));
            UNSAFE.putFloat((Object)null, var1 + (long)(var3 << 4) + 12L, 0.0F);
         }

      }

      public static void put4x4(Matrix4x3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m01());
         var3.putDouble((Object)null, var1 + 16L, var0.m02());
         var3.putDouble((Object)null, var1 + 24L, 0.0D);
         var3.putDouble((Object)null, var1 + 32L, var0.m10());
         var3.putDouble((Object)null, var1 + 40L, var0.m11());
         var3.putDouble((Object)null, var1 + 48L, var0.m12());
         var3.putDouble((Object)null, var1 + 56L, 0.0D);
         var3.putDouble((Object)null, var1 + 64L, var0.m20());
         var3.putDouble((Object)null, var1 + 72L, var0.m21());
         var3.putDouble((Object)null, var1 + 80L, var0.m22());
         var3.putDouble((Object)null, var1 + 88L, 0.0D);
         var3.putDouble((Object)null, var1 + 96L, var0.m30());
         var3.putDouble((Object)null, var1 + 104L, var0.m31());
         var3.putDouble((Object)null, var1 + 112L, var0.m32());
         var3.putDouble((Object)null, var1 + 120L, 1.0D);
      }

      public static void put4x4(Matrix3x2f var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putLong((Object)null, var1, var3.getLong(var0, Matrix3x2f_m00));
         var3.putLong((Object)null, var1 + 8L, 0L);
         var3.putLong((Object)null, var1 + 16L, var3.getLong(var0, Matrix3x2f_m00 + 8L));
         var3.putLong((Object)null, var1 + 24L, 0L);
         var3.putLong((Object)null, var1 + 32L, 0L);
         var3.putLong((Object)null, var1 + 40L, 1065353216L);
         var3.putLong((Object)null, var1 + 48L, var3.getLong(var0, Matrix3x2f_m00 + 16L));
         var3.putLong((Object)null, var1 + 56L, 4575657221408423936L);
      }

      public static void put4x4(Matrix3x2d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m01());
         var3.putDouble((Object)null, var1 + 16L, 0.0D);
         var3.putDouble((Object)null, var1 + 24L, 0.0D);
         var3.putDouble((Object)null, var1 + 32L, var0.m10());
         var3.putDouble((Object)null, var1 + 40L, var0.m11());
         var3.putDouble((Object)null, var1 + 48L, 0.0D);
         var3.putDouble((Object)null, var1 + 56L, 0.0D);
         var3.putDouble((Object)null, var1 + 64L, 0.0D);
         var3.putDouble((Object)null, var1 + 72L, 0.0D);
         var3.putDouble((Object)null, var1 + 80L, 1.0D);
         var3.putDouble((Object)null, var1 + 88L, 0.0D);
         var3.putDouble((Object)null, var1 + 96L, var0.m20());
         var3.putDouble((Object)null, var1 + 104L, var0.m21());
         var3.putDouble((Object)null, var1 + 112L, 0.0D);
         var3.putDouble((Object)null, var1 + 120L, 1.0D);
      }

      public static void put3x3(Matrix3x2f var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putLong((Object)null, var1, var3.getLong(var0, Matrix3x2f_m00));
         var3.putInt((Object)null, var1 + 8L, 0);
         var3.putLong((Object)null, var1 + 12L, var3.getLong(var0, Matrix3x2f_m00 + 8L));
         var3.putInt((Object)null, var1 + 20L, 0);
         var3.putLong((Object)null, var1 + 24L, var3.getLong(var0, Matrix3x2f_m00 + 16L));
         var3.putFloat((Object)null, var1 + 32L, 0.0F);
      }

      public static void put3x3(Matrix3x2d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m01());
         var3.putDouble((Object)null, var1 + 16L, 0.0D);
         var3.putDouble((Object)null, var1 + 24L, var0.m10());
         var3.putDouble((Object)null, var1 + 32L, var0.m11());
         var3.putDouble((Object)null, var1 + 40L, 0.0D);
         var3.putDouble((Object)null, var1 + 48L, var0.m20());
         var3.putDouble((Object)null, var1 + 56L, var0.m21());
         var3.putDouble((Object)null, var1 + 64L, 1.0D);
      }

      public static void putTransposed(Matrix4f var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putFloat((Object)null, var1, var0.m00());
         var3.putFloat((Object)null, var1 + 4L, var0.m10());
         var3.putFloat((Object)null, var1 + 8L, var0.m20());
         var3.putFloat((Object)null, var1 + 12L, var0.m30());
         var3.putFloat((Object)null, var1 + 16L, var0.m01());
         var3.putFloat((Object)null, var1 + 20L, var0.m11());
         var3.putFloat((Object)null, var1 + 24L, var0.m21());
         var3.putFloat((Object)null, var1 + 28L, var0.m31());
         var3.putFloat((Object)null, var1 + 32L, var0.m02());
         var3.putFloat((Object)null, var1 + 36L, var0.m12());
         var3.putFloat((Object)null, var1 + 40L, var0.m22());
         var3.putFloat((Object)null, var1 + 44L, var0.m32());
         var3.putFloat((Object)null, var1 + 48L, var0.m03());
         var3.putFloat((Object)null, var1 + 52L, var0.m13());
         var3.putFloat((Object)null, var1 + 56L, var0.m23());
         var3.putFloat((Object)null, var1 + 60L, var0.m33());
      }

      public static void put4x3Transposed(Matrix4f var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putFloat((Object)null, var1, var0.m00());
         var3.putFloat((Object)null, var1 + 4L, var0.m10());
         var3.putFloat((Object)null, var1 + 8L, var0.m20());
         var3.putFloat((Object)null, var1 + 12L, var0.m30());
         var3.putFloat((Object)null, var1 + 16L, var0.m01());
         var3.putFloat((Object)null, var1 + 20L, var0.m11());
         var3.putFloat((Object)null, var1 + 24L, var0.m21());
         var3.putFloat((Object)null, var1 + 28L, var0.m31());
         var3.putFloat((Object)null, var1 + 32L, var0.m02());
         var3.putFloat((Object)null, var1 + 36L, var0.m12());
         var3.putFloat((Object)null, var1 + 40L, var0.m22());
         var3.putFloat((Object)null, var1 + 44L, var0.m32());
      }

      public static void putTransposed(Matrix4x3f var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putFloat((Object)null, var1, var0.m00());
         var3.putFloat((Object)null, var1 + 4L, var0.m10());
         var3.putFloat((Object)null, var1 + 8L, var0.m20());
         var3.putFloat((Object)null, var1 + 12L, var0.m30());
         var3.putFloat((Object)null, var1 + 16L, var0.m01());
         var3.putFloat((Object)null, var1 + 20L, var0.m11());
         var3.putFloat((Object)null, var1 + 24L, var0.m21());
         var3.putFloat((Object)null, var1 + 28L, var0.m31());
         var3.putFloat((Object)null, var1 + 32L, var0.m02());
         var3.putFloat((Object)null, var1 + 36L, var0.m12());
         var3.putFloat((Object)null, var1 + 40L, var0.m22());
         var3.putFloat((Object)null, var1 + 44L, var0.m32());
      }

      public static void putTransposed(Matrix3f var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putFloat((Object)null, var1, var0.m00());
         var3.putFloat((Object)null, var1 + 4L, var0.m10());
         var3.putFloat((Object)null, var1 + 8L, var0.m20());
         var3.putFloat((Object)null, var1 + 12L, var0.m01());
         var3.putFloat((Object)null, var1 + 16L, var0.m11());
         var3.putFloat((Object)null, var1 + 20L, var0.m21());
         var3.putFloat((Object)null, var1 + 24L, var0.m02());
         var3.putFloat((Object)null, var1 + 28L, var0.m12());
         var3.putFloat((Object)null, var1 + 32L, var0.m22());
      }

      public static void putTransposed(Matrix2f var0, long var1) {
         UNSAFE.putFloat((Object)null, var1, var0.m00());
         UNSAFE.putFloat((Object)null, var1 + 4L, var0.m10());
         UNSAFE.putFloat((Object)null, var1 + 8L, var0.m01());
         UNSAFE.putFloat((Object)null, var1 + 12L, var0.m11());
      }

      public static void put(Matrix4d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m01());
         var3.putDouble((Object)null, var1 + 16L, var0.m02());
         var3.putDouble((Object)null, var1 + 24L, var0.m03());
         var3.putDouble((Object)null, var1 + 32L, var0.m10());
         var3.putDouble((Object)null, var1 + 40L, var0.m11());
         var3.putDouble((Object)null, var1 + 48L, var0.m12());
         var3.putDouble((Object)null, var1 + 56L, var0.m13());
         var3.putDouble((Object)null, var1 + 64L, var0.m20());
         var3.putDouble((Object)null, var1 + 72L, var0.m21());
         var3.putDouble((Object)null, var1 + 80L, var0.m22());
         var3.putDouble((Object)null, var1 + 88L, var0.m23());
         var3.putDouble((Object)null, var1 + 96L, var0.m30());
         var3.putDouble((Object)null, var1 + 104L, var0.m31());
         var3.putDouble((Object)null, var1 + 112L, var0.m32());
         var3.putDouble((Object)null, var1 + 120L, var0.m33());
      }

      public static void put(Matrix4x3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m01());
         var3.putDouble((Object)null, var1 + 16L, var0.m02());
         var3.putDouble((Object)null, var1 + 24L, var0.m10());
         var3.putDouble((Object)null, var1 + 32L, var0.m11());
         var3.putDouble((Object)null, var1 + 40L, var0.m12());
         var3.putDouble((Object)null, var1 + 48L, var0.m20());
         var3.putDouble((Object)null, var1 + 56L, var0.m21());
         var3.putDouble((Object)null, var1 + 64L, var0.m22());
         var3.putDouble((Object)null, var1 + 72L, var0.m30());
         var3.putDouble((Object)null, var1 + 80L, var0.m31());
         var3.putDouble((Object)null, var1 + 88L, var0.m32());
      }

      public static void putTransposed(Matrix4d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m10());
         var3.putDouble((Object)null, var1 + 16L, var0.m20());
         var3.putDouble((Object)null, var1 + 24L, var0.m30());
         var3.putDouble((Object)null, var1 + 32L, var0.m01());
         var3.putDouble((Object)null, var1 + 40L, var0.m11());
         var3.putDouble((Object)null, var1 + 48L, var0.m21());
         var3.putDouble((Object)null, var1 + 56L, var0.m31());
         var3.putDouble((Object)null, var1 + 64L, var0.m02());
         var3.putDouble((Object)null, var1 + 72L, var0.m12());
         var3.putDouble((Object)null, var1 + 80L, var0.m22());
         var3.putDouble((Object)null, var1 + 88L, var0.m32());
         var3.putDouble((Object)null, var1 + 96L, var0.m03());
         var3.putDouble((Object)null, var1 + 104L, var0.m13());
         var3.putDouble((Object)null, var1 + 112L, var0.m23());
         var3.putDouble((Object)null, var1 + 120L, var0.m33());
      }

      public static void putfTransposed(Matrix4d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putFloat((Object)null, var1, (float)var0.m00());
         var3.putFloat((Object)null, var1 + 4L, (float)var0.m10());
         var3.putFloat((Object)null, var1 + 8L, (float)var0.m20());
         var3.putFloat((Object)null, var1 + 12L, (float)var0.m30());
         var3.putFloat((Object)null, var1 + 16L, (float)var0.m01());
         var3.putFloat((Object)null, var1 + 20L, (float)var0.m11());
         var3.putFloat((Object)null, var1 + 24L, (float)var0.m21());
         var3.putFloat((Object)null, var1 + 28L, (float)var0.m31());
         var3.putFloat((Object)null, var1 + 32L, (float)var0.m02());
         var3.putFloat((Object)null, var1 + 36L, (float)var0.m12());
         var3.putFloat((Object)null, var1 + 40L, (float)var0.m22());
         var3.putFloat((Object)null, var1 + 44L, (float)var0.m32());
         var3.putFloat((Object)null, var1 + 48L, (float)var0.m03());
         var3.putFloat((Object)null, var1 + 52L, (float)var0.m13());
         var3.putFloat((Object)null, var1 + 56L, (float)var0.m23());
         var3.putFloat((Object)null, var1 + 60L, (float)var0.m33());
      }

      public static void put4x3Transposed(Matrix4d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m10());
         var3.putDouble((Object)null, var1 + 16L, var0.m20());
         var3.putDouble((Object)null, var1 + 24L, var0.m30());
         var3.putDouble((Object)null, var1 + 32L, var0.m01());
         var3.putDouble((Object)null, var1 + 40L, var0.m11());
         var3.putDouble((Object)null, var1 + 48L, var0.m21());
         var3.putDouble((Object)null, var1 + 56L, var0.m31());
         var3.putDouble((Object)null, var1 + 64L, var0.m02());
         var3.putDouble((Object)null, var1 + 72L, var0.m12());
         var3.putDouble((Object)null, var1 + 80L, var0.m22());
         var3.putDouble((Object)null, var1 + 88L, var0.m32());
      }

      public static void putTransposed(Matrix4x3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m10());
         var3.putDouble((Object)null, var1 + 16L, var0.m20());
         var3.putDouble((Object)null, var1 + 24L, var0.m30());
         var3.putDouble((Object)null, var1 + 32L, var0.m01());
         var3.putDouble((Object)null, var1 + 40L, var0.m11());
         var3.putDouble((Object)null, var1 + 48L, var0.m21());
         var3.putDouble((Object)null, var1 + 56L, var0.m31());
         var3.putDouble((Object)null, var1 + 64L, var0.m02());
         var3.putDouble((Object)null, var1 + 72L, var0.m12());
         var3.putDouble((Object)null, var1 + 80L, var0.m22());
         var3.putDouble((Object)null, var1 + 88L, var0.m32());
      }

      public static void putTransposed(Matrix2d var0, long var1) {
         UNSAFE.putDouble((Object)null, var1, var0.m00());
         UNSAFE.putDouble((Object)null, var1 + 8L, var0.m10());
         UNSAFE.putDouble((Object)null, var1 + 16L, var0.m10());
         UNSAFE.putDouble((Object)null, var1 + 24L, var0.m10());
      }

      public static void putfTransposed(Matrix4x3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putFloat((Object)null, var1, (float)var0.m00());
         var3.putFloat((Object)null, var1 + 4L, (float)var0.m10());
         var3.putFloat((Object)null, var1 + 8L, (float)var0.m20());
         var3.putFloat((Object)null, var1 + 12L, (float)var0.m30());
         var3.putFloat((Object)null, var1 + 16L, (float)var0.m01());
         var3.putFloat((Object)null, var1 + 20L, (float)var0.m11());
         var3.putFloat((Object)null, var1 + 24L, (float)var0.m21());
         var3.putFloat((Object)null, var1 + 28L, (float)var0.m31());
         var3.putFloat((Object)null, var1 + 32L, (float)var0.m02());
         var3.putFloat((Object)null, var1 + 36L, (float)var0.m12());
         var3.putFloat((Object)null, var1 + 40L, (float)var0.m22());
         var3.putFloat((Object)null, var1 + 44L, (float)var0.m32());
      }

      public static void putfTransposed(Matrix2d var0, long var1) {
         UNSAFE.putFloat((Object)null, var1, (float)var0.m00());
         UNSAFE.putFloat((Object)null, var1 + 4L, (float)var0.m00());
         UNSAFE.putFloat((Object)null, var1 + 8L, (float)var0.m00());
         UNSAFE.putFloat((Object)null, var1 + 12L, (float)var0.m00());
      }

      public static void putf(Matrix4d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putFloat((Object)null, var1, (float)var0.m00());
         var3.putFloat((Object)null, var1 + 4L, (float)var0.m01());
         var3.putFloat((Object)null, var1 + 8L, (float)var0.m02());
         var3.putFloat((Object)null, var1 + 12L, (float)var0.m03());
         var3.putFloat((Object)null, var1 + 16L, (float)var0.m10());
         var3.putFloat((Object)null, var1 + 20L, (float)var0.m11());
         var3.putFloat((Object)null, var1 + 24L, (float)var0.m12());
         var3.putFloat((Object)null, var1 + 28L, (float)var0.m13());
         var3.putFloat((Object)null, var1 + 32L, (float)var0.m20());
         var3.putFloat((Object)null, var1 + 36L, (float)var0.m21());
         var3.putFloat((Object)null, var1 + 40L, (float)var0.m22());
         var3.putFloat((Object)null, var1 + 44L, (float)var0.m23());
         var3.putFloat((Object)null, var1 + 48L, (float)var0.m30());
         var3.putFloat((Object)null, var1 + 52L, (float)var0.m31());
         var3.putFloat((Object)null, var1 + 56L, (float)var0.m32());
         var3.putFloat((Object)null, var1 + 60L, (float)var0.m33());
      }

      public static void putf(Matrix4x3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putFloat((Object)null, var1, (float)var0.m00());
         var3.putFloat((Object)null, var1 + 4L, (float)var0.m01());
         var3.putFloat((Object)null, var1 + 8L, (float)var0.m02());
         var3.putFloat((Object)null, var1 + 12L, (float)var0.m10());
         var3.putFloat((Object)null, var1 + 16L, (float)var0.m11());
         var3.putFloat((Object)null, var1 + 20L, (float)var0.m12());
         var3.putFloat((Object)null, var1 + 24L, (float)var0.m20());
         var3.putFloat((Object)null, var1 + 28L, (float)var0.m21());
         var3.putFloat((Object)null, var1 + 32L, (float)var0.m22());
         var3.putFloat((Object)null, var1 + 36L, (float)var0.m30());
         var3.putFloat((Object)null, var1 + 40L, (float)var0.m31());
         var3.putFloat((Object)null, var1 + 44L, (float)var0.m32());
      }

      public static void put(Matrix3f var0, long var1) {
         for(int var3 = 0; var3 < 4; ++var3) {
            UNSAFE.putLong((Object)null, var1 + (long)(var3 << 3), UNSAFE.getLong(var0, Matrix3f_m00 + (long)(var3 << 3)));
         }

         UNSAFE.putFloat((Object)null, var1 + 32L, var0.m22());
      }

      public static void put3x4(Matrix3f var0, long var1) {
         for(int var3 = 0; var3 < 3; ++var3) {
            UNSAFE.putLong((Object)null, var1 + (long)(var3 << 4), UNSAFE.getLong(var0, Matrix3f_m00 + (long)(12 * var3)));
            UNSAFE.putFloat((Object)null, var1 + (long)(var3 << 4) + 8L, UNSAFE.getFloat(var0, Matrix3f_m00 + 8L + (long)(12 * var3)));
            UNSAFE.putFloat((Object)null, var1 + (long)(12 * var3), 0.0F);
         }

      }

      public static void put(Matrix3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m01());
         var3.putDouble((Object)null, var1 + 16L, var0.m02());
         var3.putDouble((Object)null, var1 + 24L, var0.m10());
         var3.putDouble((Object)null, var1 + 32L, var0.m11());
         var3.putDouble((Object)null, var1 + 40L, var0.m12());
         var3.putDouble((Object)null, var1 + 48L, var0.m20());
         var3.putDouble((Object)null, var1 + 56L, var0.m21());
         var3.putDouble((Object)null, var1 + 64L, var0.m22());
      }

      public static void put(Matrix3x2f var0, long var1) {
         for(int var3 = 0; var3 < 3; ++var3) {
            UNSAFE.putLong((Object)null, var1 + (long)(var3 << 3), UNSAFE.getLong(var0, Matrix3x2f_m00 + (long)(var3 << 3)));
         }

      }

      public static void put(Matrix3x2d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putDouble((Object)null, var1, var0.m00());
         var3.putDouble((Object)null, var1 + 8L, var0.m01());
         var3.putDouble((Object)null, var1 + 16L, var0.m10());
         var3.putDouble((Object)null, var1 + 24L, var0.m11());
         var3.putDouble((Object)null, var1 + 32L, var0.m20());
         var3.putDouble((Object)null, var1 + 40L, var0.m21());
      }

      public static void putf(Matrix3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var3.putFloat((Object)null, var1, (float)var0.m00());
         var3.putFloat((Object)null, var1 + 4L, (float)var0.m01());
         var3.putFloat((Object)null, var1 + 8L, (float)var0.m02());
         var3.putFloat((Object)null, var1 + 12L, (float)var0.m10());
         var3.putFloat((Object)null, var1 + 16L, (float)var0.m11());
         var3.putFloat((Object)null, var1 + 20L, (float)var0.m12());
         var3.putFloat((Object)null, var1 + 24L, (float)var0.m20());
         var3.putFloat((Object)null, var1 + 28L, (float)var0.m21());
         var3.putFloat((Object)null, var1 + 32L, (float)var0.m22());
      }

      public static void put(Matrix2f var0, long var1) {
         UNSAFE.putLong((Object)null, var1, UNSAFE.getLong(var0, Matrix2f_m00));
         UNSAFE.putLong((Object)null, var1 + 8L, UNSAFE.getLong(var0, Matrix2f_m00 + 8L));
      }

      public static void put(Matrix2d var0, long var1) {
         UNSAFE.putDouble((Object)null, var1, var0.m00());
         UNSAFE.putDouble((Object)null, var1 + 8L, var0.m01());
         UNSAFE.putDouble((Object)null, var1 + 16L, var0.m10());
         UNSAFE.putDouble((Object)null, var1 + 24L, var0.m11());
      }

      public static void putf(Matrix2d var0, long var1) {
         UNSAFE.putFloat((Object)null, var1, (float)var0.m00());
         UNSAFE.putFloat((Object)null, var1 + 4L, (float)var0.m01());
         UNSAFE.putFloat((Object)null, var1 + 8L, (float)var0.m10());
         UNSAFE.putFloat((Object)null, var1 + 12L, (float)var0.m11());
      }

      public static void put(Vector4d var0, long var1) {
         UNSAFE.putDouble((Object)null, var1, var0.x);
         UNSAFE.putDouble((Object)null, var1 + 8L, var0.y);
         UNSAFE.putDouble((Object)null, var1 + 16L, var0.z);
         UNSAFE.putDouble((Object)null, var1 + 24L, var0.w);
      }

      public static void putf(Vector4d var0, long var1) {
         UNSAFE.putFloat((Object)null, var1, (float)var0.x);
         UNSAFE.putFloat((Object)null, var1 + 4L, (float)var0.y);
         UNSAFE.putFloat((Object)null, var1 + 8L, (float)var0.z);
         UNSAFE.putFloat((Object)null, var1 + 12L, (float)var0.w);
      }

      public static void put(Vector4f var0, long var1) {
         UNSAFE.putLong((Object)null, var1, UNSAFE.getLong(var0, Vector4f_x));
         UNSAFE.putLong((Object)null, var1 + 8L, UNSAFE.getLong(var0, Vector4f_x + 8L));
      }

      public static void put(Vector4i var0, long var1) {
         UNSAFE.putLong((Object)null, var1, UNSAFE.getLong(var0, Vector4i_x));
         UNSAFE.putLong((Object)null, var1 + 8L, UNSAFE.getLong(var0, Vector4i_x + 8L));
      }

      public static void put(Vector3f var0, long var1) {
         UNSAFE.putLong((Object)null, var1, UNSAFE.getLong(var0, Vector3f_x));
         UNSAFE.putFloat((Object)null, var1 + 8L, var0.z);
      }

      public static void put(Vector3d var0, long var1) {
         UNSAFE.putDouble((Object)null, var1, var0.x);
         UNSAFE.putDouble((Object)null, var1 + 8L, var0.y);
         UNSAFE.putDouble((Object)null, var1 + 16L, var0.z);
      }

      public static void putf(Vector3d var0, long var1) {
         UNSAFE.putFloat((Object)null, var1, (float)var0.x);
         UNSAFE.putFloat((Object)null, var1 + 4L, (float)var0.y);
         UNSAFE.putFloat((Object)null, var1 + 8L, (float)var0.z);
      }

      public static void put(Vector3i var0, long var1) {
         UNSAFE.putLong((Object)null, var1, UNSAFE.getLong(var0, Vector3i_x));
         UNSAFE.putInt((Object)null, var1 + 8L, var0.z);
      }

      public static void put(Vector2f var0, long var1) {
         UNSAFE.putLong((Object)null, var1, UNSAFE.getLong(var0, Vector2f_x));
      }

      public static void put(Vector2d var0, long var1) {
         UNSAFE.putDouble((Object)null, var1, var0.x);
         UNSAFE.putDouble((Object)null, var1 + 8L, var0.y);
      }

      public static void put(Vector2i var0, long var1) {
         UNSAFE.putLong((Object)null, var1, UNSAFE.getLong(var0, Vector2i_x));
      }

      public static void get(Matrix4f var0, long var1) {
         for(int var3 = 0; var3 < 8; ++var3) {
            UNSAFE.putLong(var0, Matrix4f_m00 + (long)(var3 << 3), UNSAFE.getLong(var1 + (long)(var3 << 3)));
         }

      }

      public static void getTransposed(Matrix4f var0, long var1) {
         var0._m00(UNSAFE.getFloat(var1))._m10(UNSAFE.getFloat(var1 + 4L))._m20(UNSAFE.getFloat(var1 + 8L))._m30(UNSAFE.getFloat(var1 + 12L))._m01(UNSAFE.getFloat(var1 + 16L))._m11(UNSAFE.getFloat(var1 + 20L))._m21(UNSAFE.getFloat(var1 + 24L))._m31(UNSAFE.getFloat(var1 + 28L))._m02(UNSAFE.getFloat(var1 + 32L))._m12(UNSAFE.getFloat(var1 + 36L))._m22(UNSAFE.getFloat(var1 + 40L))._m32(UNSAFE.getFloat(var1 + 44L))._m03(UNSAFE.getFloat(var1 + 48L))._m13(UNSAFE.getFloat(var1 + 52L))._m23(UNSAFE.getFloat(var1 + 56L))._m33(UNSAFE.getFloat(var1 + 60L));
      }

      public static void get(Matrix4x3f var0, long var1) {
         for(int var3 = 0; var3 < 6; ++var3) {
            UNSAFE.putLong(var0, Matrix4x3f_m00 + (long)(var3 << 3), UNSAFE.getLong(var1 + (long)(var3 << 3)));
         }

      }

      public static void get(Matrix4d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var0._m00(var3.getDouble((Object)null, var1))._m01(var3.getDouble((Object)null, var1 + 8L))._m02(var3.getDouble((Object)null, var1 + 16L))._m03(var3.getDouble((Object)null, var1 + 24L))._m10(var3.getDouble((Object)null, var1 + 32L))._m11(var3.getDouble((Object)null, var1 + 40L))._m12(var3.getDouble((Object)null, var1 + 48L))._m13(var3.getDouble((Object)null, var1 + 56L))._m20(var3.getDouble((Object)null, var1 + 64L))._m21(var3.getDouble((Object)null, var1 + 72L))._m22(var3.getDouble((Object)null, var1 + 80L))._m23(var3.getDouble((Object)null, var1 + 88L))._m30(var3.getDouble((Object)null, var1 + 96L))._m31(var3.getDouble((Object)null, var1 + 104L))._m32(var3.getDouble((Object)null, var1 + 112L))._m33(var3.getDouble((Object)null, var1 + 120L));
      }

      public static void get(Matrix4x3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var0._m00(var3.getDouble((Object)null, var1))._m01(var3.getDouble((Object)null, var1 + 8L))._m02(var3.getDouble((Object)null, var1 + 16L))._m10(var3.getDouble((Object)null, var1 + 24L))._m11(var3.getDouble((Object)null, var1 + 32L))._m12(var3.getDouble((Object)null, var1 + 40L))._m20(var3.getDouble((Object)null, var1 + 48L))._m21(var3.getDouble((Object)null, var1 + 56L))._m22(var3.getDouble((Object)null, var1 + 64L))._m30(var3.getDouble((Object)null, var1 + 72L))._m31(var3.getDouble((Object)null, var1 + 80L))._m32(var3.getDouble((Object)null, var1 + 88L));
      }

      public static void getf(Matrix4d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var0._m00((double)var3.getFloat((Object)null, var1))._m01((double)var3.getFloat((Object)null, var1 + 4L))._m02((double)var3.getFloat((Object)null, var1 + 8L))._m03((double)var3.getFloat((Object)null, var1 + 12L))._m10((double)var3.getFloat((Object)null, var1 + 16L))._m11((double)var3.getFloat((Object)null, var1 + 20L))._m12((double)var3.getFloat((Object)null, var1 + 24L))._m13((double)var3.getFloat((Object)null, var1 + 28L))._m20((double)var3.getFloat((Object)null, var1 + 32L))._m21((double)var3.getFloat((Object)null, var1 + 36L))._m22((double)var3.getFloat((Object)null, var1 + 40L))._m23((double)var3.getFloat((Object)null, var1 + 44L))._m30((double)var3.getFloat((Object)null, var1 + 48L))._m31((double)var3.getFloat((Object)null, var1 + 52L))._m32((double)var3.getFloat((Object)null, var1 + 56L))._m33((double)var3.getFloat((Object)null, var1 + 60L));
      }

      public static void getf(Matrix4x3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var0._m00((double)var3.getFloat((Object)null, var1))._m01((double)var3.getFloat((Object)null, var1 + 4L))._m02((double)var3.getFloat((Object)null, var1 + 8L))._m10((double)var3.getFloat((Object)null, var1 + 12L))._m11((double)var3.getFloat((Object)null, var1 + 16L))._m12((double)var3.getFloat((Object)null, var1 + 20L))._m20((double)var3.getFloat((Object)null, var1 + 24L))._m21((double)var3.getFloat((Object)null, var1 + 28L))._m22((double)var3.getFloat((Object)null, var1 + 32L))._m30((double)var3.getFloat((Object)null, var1 + 36L))._m31((double)var3.getFloat((Object)null, var1 + 40L))._m32((double)var3.getFloat((Object)null, var1 + 44L));
      }

      public static void get(Matrix3f var0, long var1) {
         for(int var3 = 0; var3 < 4; ++var3) {
            UNSAFE.putLong(var0, Matrix3f_m00 + (long)(var3 << 3), UNSAFE.getLong((Object)null, var1 + (long)(var3 << 3)));
         }

         var0._m22(UNSAFE.getFloat((Object)null, var1 + 32L));
      }

      public static void get(Matrix3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var0._m00(var3.getDouble((Object)null, var1))._m01(var3.getDouble((Object)null, var1 + 8L))._m02(var3.getDouble((Object)null, var1 + 16L))._m10(var3.getDouble((Object)null, var1 + 24L))._m11(var3.getDouble((Object)null, var1 + 32L))._m12(var3.getDouble((Object)null, var1 + 40L))._m20(var3.getDouble((Object)null, var1 + 48L))._m21(var3.getDouble((Object)null, var1 + 56L))._m22(var3.getDouble((Object)null, var1 + 64L));
      }

      public static void get(Matrix3x2f var0, long var1) {
         for(int var3 = 0; var3 < 3; ++var3) {
            UNSAFE.putLong(var0, Matrix3x2f_m00 + (long)(var3 << 3), UNSAFE.getLong((Object)null, var1 + (long)(var3 << 3)));
         }

      }

      public static void get(Matrix3x2d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var0._m00(var3.getDouble((Object)null, var1))._m01(var3.getDouble((Object)null, var1 + 8L))._m10(var3.getDouble((Object)null, var1 + 16L))._m11(var3.getDouble((Object)null, var1 + 24L))._m20(var3.getDouble((Object)null, var1 + 32L))._m21(var3.getDouble((Object)null, var1 + 40L));
      }

      public static void getf(Matrix3d var0, long var1) {
         Unsafe var3 = UNSAFE;
         var0._m00((double)var3.getFloat((Object)null, var1))._m01((double)var3.getFloat((Object)null, var1 + 4L))._m02((double)var3.getFloat((Object)null, var1 + 8L))._m10((double)var3.getFloat((Object)null, var1 + 12L))._m11((double)var3.getFloat((Object)null, var1 + 16L))._m12((double)var3.getFloat((Object)null, var1 + 20L))._m20((double)var3.getFloat((Object)null, var1 + 24L))._m21((double)var3.getFloat((Object)null, var1 + 28L))._m22((double)var3.getFloat((Object)null, var1 + 32L));
      }

      public static void get(Matrix2f var0, long var1) {
         UNSAFE.putLong(var0, Matrix2f_m00, UNSAFE.getLong((Object)null, var1));
         UNSAFE.putLong(var0, Matrix2f_m00 + 8L, UNSAFE.getLong((Object)null, var1 + 8L));
      }

      public static void get(Matrix2d var0, long var1) {
         var0._m00(UNSAFE.getDouble((Object)null, var1))._m01(UNSAFE.getDouble((Object)null, var1 + 8L))._m10(UNSAFE.getDouble((Object)null, var1 + 16L))._m11(UNSAFE.getDouble((Object)null, var1 + 24L));
      }

      public static void getf(Matrix2d var0, long var1) {
         var0._m00((double)UNSAFE.getFloat((Object)null, var1))._m01((double)UNSAFE.getFloat((Object)null, var1 + 4L))._m10((double)UNSAFE.getFloat((Object)null, var1 + 8L))._m11((double)UNSAFE.getFloat((Object)null, var1 + 12L));
      }

      public static void get(Vector4d var0, long var1) {
         var0.x = UNSAFE.getDouble((Object)null, var1);
         var0.y = UNSAFE.getDouble((Object)null, var1 + 8L);
         var0.z = UNSAFE.getDouble((Object)null, var1 + 16L);
         var0.w = UNSAFE.getDouble((Object)null, var1 + 24L);
      }

      public static void get(Vector4f var0, long var1) {
         var0.x = UNSAFE.getFloat((Object)null, var1);
         var0.y = UNSAFE.getFloat((Object)null, var1 + 4L);
         var0.z = UNSAFE.getFloat((Object)null, var1 + 8L);
         var0.w = UNSAFE.getFloat((Object)null, var1 + 12L);
      }

      public static void get(Vector4i var0, long var1) {
         var0.x = UNSAFE.getInt((Object)null, var1);
         var0.y = UNSAFE.getInt((Object)null, var1 + 4L);
         var0.z = UNSAFE.getInt((Object)null, var1 + 8L);
         var0.w = UNSAFE.getInt((Object)null, var1 + 12L);
      }

      public static void get(Vector3f var0, long var1) {
         var0.x = UNSAFE.getFloat((Object)null, var1);
         var0.y = UNSAFE.getFloat((Object)null, var1 + 4L);
         var0.z = UNSAFE.getFloat((Object)null, var1 + 8L);
      }

      public static void get(Vector3d var0, long var1) {
         var0.x = UNSAFE.getDouble((Object)null, var1);
         var0.y = UNSAFE.getDouble((Object)null, var1 + 8L);
         var0.z = UNSAFE.getDouble((Object)null, var1 + 16L);
      }

      public static void get(Vector3i var0, long var1) {
         var0.x = UNSAFE.getInt((Object)null, var1);
         var0.y = UNSAFE.getInt((Object)null, var1 + 4L);
         var0.z = UNSAFE.getInt((Object)null, var1 + 8L);
      }

      public static void get(Vector2f var0, long var1) {
         var0.x = UNSAFE.getFloat((Object)null, var1);
         var0.y = UNSAFE.getFloat((Object)null, var1 + 4L);
      }

      public static void get(Vector2d var0, long var1) {
         var0.x = UNSAFE.getDouble((Object)null, var1);
         var0.y = UNSAFE.getDouble((Object)null, var1 + 8L);
      }

      public static void get(Vector2i var0, long var1) {
         var0.x = UNSAFE.getInt((Object)null, var1);
         var0.y = UNSAFE.getInt((Object)null, var1 + 4L);
      }

      public static void putMatrix3f(Quaternionf var0, long var1) {
         float var3 = var0.x + var0.x;
         float var4 = var0.y + var0.y;
         float var5 = var0.z + var0.z;
         float var6 = var3 * var0.x;
         float var7 = var4 * var0.y;
         float var8 = var5 * var0.z;
         float var9 = var3 * var0.y;
         float var10 = var3 * var0.z;
         float var11 = var3 * var0.w;
         float var12 = var4 * var0.z;
         float var13 = var4 * var0.w;
         float var14 = var5 * var0.w;
         Unsafe var15 = UNSAFE;
         var15.putFloat((Object)null, var1, 1.0F - var7 - var8);
         var15.putFloat((Object)null, var1 + 4L, var9 + var14);
         var15.putFloat((Object)null, var1 + 8L, var10 - var13);
         var15.putFloat((Object)null, var1 + 12L, var9 - var14);
         var15.putFloat((Object)null, var1 + 16L, 1.0F - var8 - var6);
         var15.putFloat((Object)null, var1 + 20L, var12 + var11);
         var15.putFloat((Object)null, var1 + 24L, var10 + var13);
         var15.putFloat((Object)null, var1 + 28L, var12 - var11);
         var15.putFloat((Object)null, var1 + 32L, 1.0F - var7 - var6);
      }

      public static void putMatrix4f(Quaternionf var0, long var1) {
         float var3 = var0.x + var0.x;
         float var4 = var0.y + var0.y;
         float var5 = var0.z + var0.z;
         float var6 = var3 * var0.x;
         float var7 = var4 * var0.y;
         float var8 = var5 * var0.z;
         float var9 = var3 * var0.y;
         float var10 = var3 * var0.z;
         float var11 = var3 * var0.w;
         float var12 = var4 * var0.z;
         float var13 = var4 * var0.w;
         float var14 = var5 * var0.w;
         Unsafe var15 = UNSAFE;
         var15.putFloat((Object)null, var1, 1.0F - var7 - var8);
         var15.putFloat((Object)null, var1 + 4L, var9 + var14);
         var15.putLong((Object)null, var1 + 8L, (long)Float.floatToRawIntBits(var10 - var13) & 4294967295L);
         var15.putFloat((Object)null, var1 + 16L, var9 - var14);
         var15.putFloat((Object)null, var1 + 20L, 1.0F - var8 - var6);
         var15.putLong((Object)null, var1 + 24L, (long)Float.floatToRawIntBits(var12 + var11) & 4294967295L);
         var15.putFloat((Object)null, var1 + 32L, var10 + var13);
         var15.putFloat((Object)null, var1 + 36L, var12 - var11);
         var15.putLong((Object)null, var1 + 40L, (long)Float.floatToRawIntBits(1.0F - var7 - var6) & 4294967295L);
         var15.putLong((Object)null, var1 + 48L, 0L);
         var15.putLong((Object)null, var1 + 56L, 4575657221408423936L);
      }

      public static void putMatrix4x3f(Quaternionf var0, long var1) {
         float var3 = var0.x + var0.x;
         float var4 = var0.y + var0.y;
         float var5 = var0.z + var0.z;
         float var6 = var3 * var0.x;
         float var7 = var4 * var0.y;
         float var8 = var5 * var0.z;
         float var9 = var3 * var0.y;
         float var10 = var3 * var0.z;
         float var11 = var3 * var0.w;
         float var12 = var4 * var0.z;
         float var13 = var4 * var0.w;
         float var14 = var5 * var0.w;
         Unsafe var15 = UNSAFE;
         var15.putFloat((Object)null, var1, 1.0F - var7 - var8);
         var15.putFloat((Object)null, var1 + 4L, var9 + var14);
         var15.putFloat((Object)null, var1 + 8L, var10 - var13);
         var15.putFloat((Object)null, var1 + 12L, var9 - var14);
         var15.putFloat((Object)null, var1 + 16L, 1.0F - var8 - var6);
         var15.putFloat((Object)null, var1 + 20L, var12 + var11);
         var15.putFloat((Object)null, var1 + 24L, var10 + var13);
         var15.putFloat((Object)null, var1 + 28L, var12 - var11);
         var15.putFloat((Object)null, var1 + 32L, 1.0F - var7 - var6);
         var15.putLong((Object)null, var1 + 36L, 0L);
         var15.putFloat((Object)null, var1 + 44L, 0.0F);
      }

      private static void throwNoDirectBufferException() {
         throw new IllegalArgumentException("Must use a direct buffer");
      }

      public void putMatrix3f(Quaternionf var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 36);
         }

         putMatrix3f(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putMatrix3f(Quaternionf var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 9);
         }

         putMatrix3f(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      private static void checkPut(int var0, boolean var1, int var2, int var3) {
         if (!var1) {
            throwNoDirectBufferException();
         }

         if (var2 - var0 < var3) {
            throw new BufferOverflowException();
         }
      }

      public void putMatrix4f(Quaternionf var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 64);
         }

         putMatrix4f(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putMatrix4f(Quaternionf var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         putMatrix4f(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putMatrix4x3f(Quaternionf var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         putMatrix4x3f(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putMatrix4x3f(Quaternionf var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         putMatrix4x3f(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Matrix4f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Matrix4f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 64);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put4x3(Matrix4f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put4x3(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put4x3(Matrix4f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         put4x3(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put3x4(Matrix4f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put3x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put3x4(Matrix4f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         put3x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Matrix4x3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Matrix4x3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put4x4(Matrix4x3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put4x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put4x4(Matrix4x3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 64);
         }

         put4x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put3x4(Matrix4x3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put3x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put3x4(Matrix4x3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         put3x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put4x4(Matrix4x3d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put4x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put4x4(Matrix4x3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 128);
         }

         put4x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put4x4(Matrix3x2f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put4x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put4x4(Matrix3x2f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 64);
         }

         put4x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put4x4(Matrix3x2d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put4x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put4x4(Matrix3x2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 128);
         }

         put4x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put3x3(Matrix3x2f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 9);
         }

         put3x3(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put3x3(Matrix3x2f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 36);
         }

         put3x3(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put3x3(Matrix3x2d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 9);
         }

         put3x3(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put3x3(Matrix3x2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 72);
         }

         put3x3(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putTransposed(Matrix4f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putTransposed(Matrix4f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 64);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put4x3Transposed(Matrix4f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put4x3Transposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put4x3Transposed(Matrix4f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         put4x3Transposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putTransposed(Matrix4x3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putTransposed(Matrix4x3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putTransposed(Matrix3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 9);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putTransposed(Matrix3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 36);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putTransposed(Matrix2f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putTransposed(Matrix2f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Matrix4d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put(Matrix4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 128);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Matrix4x3d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put(Matrix4x3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 96);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putf(Matrix4d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putf(Matrix4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 64);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putf(Matrix4x3d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putf(Matrix4x3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putTransposed(Matrix4d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void putTransposed(Matrix4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 128);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put4x3Transposed(Matrix4d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put4x3Transposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put4x3Transposed(Matrix4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 96);
         }

         put4x3Transposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putTransposed(Matrix4x3d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void putTransposed(Matrix4x3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 96);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putTransposed(Matrix2d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void putTransposed(Matrix2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 32);
         }

         putTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putfTransposed(Matrix4d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         putfTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putfTransposed(Matrix4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 64);
         }

         putfTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putfTransposed(Matrix4x3d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         putfTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putfTransposed(Matrix4x3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         putfTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putfTransposed(Matrix2d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         putfTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putfTransposed(Matrix2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         putfTransposed(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Matrix3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 9);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Matrix3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 36);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put3x4(Matrix3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put3x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put3x4(Matrix3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         put3x4(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Matrix3d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 9);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put(Matrix3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 72);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Matrix3x2f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 6);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Matrix3x2f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 24);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Matrix3x2d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 6);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put(Matrix3x2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 48);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putf(Matrix3d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 9);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putf(Matrix3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 36);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Matrix2f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Matrix2f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Matrix2d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put(Matrix2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putf(Matrix2d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void putf(Matrix2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Vector4d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put(Vector4d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Vector4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 32);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putf(Vector4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Vector4f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Vector4f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Vector4i var1, int var2, IntBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 4);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Vector4i var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Vector3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 3);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Vector3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Vector3d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 3);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put(Vector3d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 3);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Vector3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 24);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void putf(Vector3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         putf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Vector3i var1, int var2, IntBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 3);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Vector3i var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 12);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Vector2f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 2);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Vector2f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 8);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Vector2d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 2);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void put(Vector2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 16);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void put(Vector2i var1, int var2, IntBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 2);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void put(Vector2i var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkPut(var2, var3.isDirect(), var3.capacity(), 8);
         }

         put(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Matrix4f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 16);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Matrix4f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 64);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public float get(Matrix4f var1, int var2, int var3) {
         return UNSAFE.getFloat(var1, Matrix4f_m00 + (long)(var2 << 4) + (long)(var3 << 2));
      }

      public Matrix4f set(Matrix4f var1, int var2, int var3, float var4) {
         UNSAFE.putFloat(var1, Matrix4f_m00 + (long)(var2 << 4) + (long)(var3 << 2), var4);
         return var1;
      }

      public double get(Matrix4d var1, int var2, int var3) {
         return UNSAFE.getDouble(var1, Matrix4d_m00 + (long)(var2 << 5) + (long)(var3 << 3));
      }

      public Matrix4d set(Matrix4d var1, int var2, int var3, double var4) {
         UNSAFE.putDouble(var1, Matrix4d_m00 + (long)(var2 << 5) + (long)(var3 << 3), var4);
         return var1;
      }

      public float get(Matrix3f var1, int var2, int var3) {
         return UNSAFE.getFloat(var1, Matrix3f_m00 + (long)(var2 * 12) + (long)(var3 << 2));
      }

      public Matrix3f set(Matrix3f var1, int var2, int var3, float var4) {
         UNSAFE.putFloat(var1, Matrix3f_m00 + (long)(var2 * 12) + (long)(var3 << 2), var4);
         return var1;
      }

      public double get(Matrix3d var1, int var2, int var3) {
         return UNSAFE.getDouble(var1, Matrix3d_m00 + (long)(var2 * 24) + (long)(var3 << 3));
      }

      public Matrix3d set(Matrix3d var1, int var2, int var3, double var4) {
         UNSAFE.putDouble(var1, Matrix3d_m00 + (long)(var2 * 24) + (long)(var3 << 3), var4);
         return var1;
      }

      public void get(Matrix4x3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 12);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Matrix4x3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 48);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Matrix4d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 16);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void get(Matrix4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 128);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Matrix4x3d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 12);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void get(Matrix4x3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 96);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void getf(Matrix4d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 16);
         }

         getf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void getf(Matrix4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 64);
         }

         getf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void getf(Matrix4x3d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 12);
         }

         getf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      private static void checkGet(int var0, boolean var1, int var2, int var3) {
         if (!var1) {
            throwNoDirectBufferException();
         }

         if (var2 - var0 < var3) {
            throw new BufferUnderflowException();
         }
      }

      public void getf(Matrix4x3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 48);
         }

         getf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Matrix3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 9);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Matrix3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 36);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Matrix3d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 9);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void get(Matrix3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 72);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Matrix3x2f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 6);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Matrix3x2f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 24);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Matrix3x2d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 6);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void get(Matrix3x2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 48);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void getf(Matrix3d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 9);
         }

         getf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void getf(Matrix3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 36);
         }

         getf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Matrix2f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 4);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Matrix2f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 16);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Matrix2d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 4);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void get(Matrix2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 32);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void getf(Matrix2d var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 4);
         }

         getf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void getf(Matrix2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 16);
         }

         getf(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Vector4d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 4);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void get(Vector4d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 32);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Vector4f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 4);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Vector4f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 16);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Vector4i var1, int var2, IntBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 4);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Vector4i var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 16);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Vector3f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 3);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Vector3f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 12);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Vector3d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 3);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void get(Vector3d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 24);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Vector3i var1, int var2, IntBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 3);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Vector3i var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 12);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Vector2f var1, int var2, FloatBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 2);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Vector2f var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 8);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Vector2d var1, int var2, DoubleBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 2);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 3));
      }

      public void get(Vector2d var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 16);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      public void get(Vector2i var1, int var2, IntBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 2);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)(var2 << 2));
      }

      public void get(Vector2i var1, int var2, ByteBuffer var3) {
         if (Options.DEBUG) {
            checkGet(var2, var3.isDirect(), var3.capacity(), 8);
         }

         get(var1, UNSAFE.getLong(var3, ADDRESS) + (long)var2);
      }

      static {
         try {
            ADDRESS = findBufferAddress();
            Matrix4f_m00 = checkMatrix4f();
            Matrix4d_m00 = checkMatrix4d();
            Matrix4x3f_m00 = checkMatrix4x3f();
            Matrix3f_m00 = checkMatrix3f();
            Matrix3d_m00 = checkMatrix3d();
            Matrix3x2f_m00 = checkMatrix3x2f();
            Matrix2f_m00 = checkMatrix2f();
            Vector4f_x = checkVector4f();
            Vector4i_x = checkVector4i();
            Vector3f_x = checkVector3f();
            Vector3i_x = checkVector3i();
            Vector2f_x = checkVector2f();
            Vector2i_x = checkVector2i();
            Quaternionf_x = checkQuaternionf();
            floatArrayOffset = (long)UNSAFE.arrayBaseOffset(float[].class);
            Unsafe.class.getDeclaredMethod("getLong", Object.class, Long.TYPE);
            Unsafe.class.getDeclaredMethod("putLong", Object.class, Long.TYPE, Long.TYPE);
         } catch (NoSuchFieldException var1) {
            throw new UnsupportedOperationException(var1);
         } catch (NoSuchMethodException var2) {
            throw new UnsupportedOperationException(var2);
         }
      }
   }
}
