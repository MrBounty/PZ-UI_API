package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public interface Matrix4x3dc {
   int PLANE_NX = 0;
   int PLANE_PX = 1;
   int PLANE_NY = 2;
   int PLANE_PY = 3;
   int PLANE_NZ = 4;
   int PLANE_PZ = 5;
   byte PROPERTY_IDENTITY = 4;
   byte PROPERTY_TRANSLATION = 8;
   byte PROPERTY_ORTHONORMAL = 16;

   int properties();

   double m00();

   double m01();

   double m02();

   double m10();

   double m11();

   double m12();

   double m20();

   double m21();

   double m22();

   double m30();

   double m31();

   double m32();

   Matrix4d get(Matrix4d var1);

   Matrix4x3d mul(Matrix4x3dc var1, Matrix4x3d var2);

   Matrix4x3d mul(Matrix4x3fc var1, Matrix4x3d var2);

   Matrix4x3d mulTranslation(Matrix4x3dc var1, Matrix4x3d var2);

   Matrix4x3d mulTranslation(Matrix4x3fc var1, Matrix4x3d var2);

   Matrix4x3d mulOrtho(Matrix4x3dc var1, Matrix4x3d var2);

   Matrix4x3d fma(Matrix4x3dc var1, double var2, Matrix4x3d var4);

   Matrix4x3d fma(Matrix4x3fc var1, double var2, Matrix4x3d var4);

   Matrix4x3d add(Matrix4x3dc var1, Matrix4x3d var2);

   Matrix4x3d add(Matrix4x3fc var1, Matrix4x3d var2);

   Matrix4x3d sub(Matrix4x3dc var1, Matrix4x3d var2);

   Matrix4x3d sub(Matrix4x3fc var1, Matrix4x3d var2);

   Matrix4x3d mulComponentWise(Matrix4x3dc var1, Matrix4x3d var2);

   double determinant();

   Matrix4x3d invert(Matrix4x3d var1);

   Matrix4x3d invertOrtho(Matrix4x3d var1);

   Matrix4x3d transpose3x3(Matrix4x3d var1);

   Matrix3d transpose3x3(Matrix3d var1);

   Vector3d getTranslation(Vector3d var1);

   Vector3d getScale(Vector3d var1);

   Matrix4x3d get(Matrix4x3d var1);

   Quaternionf getUnnormalizedRotation(Quaternionf var1);

   Quaternionf getNormalizedRotation(Quaternionf var1);

   Quaterniond getUnnormalizedRotation(Quaterniond var1);

   Quaterniond getNormalizedRotation(Quaterniond var1);

   DoubleBuffer get(DoubleBuffer var1);

   DoubleBuffer get(int var1, DoubleBuffer var2);

   FloatBuffer get(FloatBuffer var1);

   FloatBuffer get(int var1, FloatBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   ByteBuffer getFloats(ByteBuffer var1);

   ByteBuffer getFloats(int var1, ByteBuffer var2);

   Matrix4x3dc getToAddress(long var1);

   double[] get(double[] var1, int var2);

   double[] get(double[] var1);

   float[] get(float[] var1, int var2);

   float[] get(float[] var1);

   double[] get4x4(double[] var1, int var2);

   double[] get4x4(double[] var1);

   float[] get4x4(float[] var1, int var2);

   float[] get4x4(float[] var1);

   DoubleBuffer get4x4(DoubleBuffer var1);

   DoubleBuffer get4x4(int var1, DoubleBuffer var2);

   ByteBuffer get4x4(ByteBuffer var1);

   ByteBuffer get4x4(int var1, ByteBuffer var2);

   DoubleBuffer getTransposed(DoubleBuffer var1);

   DoubleBuffer getTransposed(int var1, DoubleBuffer var2);

   ByteBuffer getTransposed(ByteBuffer var1);

   ByteBuffer getTransposed(int var1, ByteBuffer var2);

   FloatBuffer getTransposed(FloatBuffer var1);

   FloatBuffer getTransposed(int var1, FloatBuffer var2);

   ByteBuffer getTransposedFloats(ByteBuffer var1);

   ByteBuffer getTransposedFloats(int var1, ByteBuffer var2);

   double[] getTransposed(double[] var1, int var2);

   double[] getTransposed(double[] var1);

   Vector4d transform(Vector4d var1);

   Vector4d transform(Vector4dc var1, Vector4d var2);

   Vector3d transformPosition(Vector3d var1);

   Vector3d transformPosition(Vector3dc var1, Vector3d var2);

   Vector3d transformDirection(Vector3d var1);

   Vector3d transformDirection(Vector3dc var1, Vector3d var2);

   Matrix4x3d scale(Vector3dc var1, Matrix4x3d var2);

   Matrix4x3d scale(double var1, double var3, double var5, Matrix4x3d var7);

   Matrix4x3d scale(double var1, Matrix4x3d var3);

   Matrix4x3d scaleXY(double var1, double var3, Matrix4x3d var5);

   Matrix4x3d scaleLocal(double var1, double var3, double var5, Matrix4x3d var7);

   Matrix4x3d rotate(double var1, double var3, double var5, double var7, Matrix4x3d var9);

   Matrix4x3d rotateTranslation(double var1, double var3, double var5, double var7, Matrix4x3d var9);

   Matrix4x3d rotateAround(Quaterniondc var1, double var2, double var4, double var6, Matrix4x3d var8);

   Matrix4x3d rotateLocal(double var1, double var3, double var5, double var7, Matrix4x3d var9);

   Matrix4x3d translate(Vector3dc var1, Matrix4x3d var2);

   Matrix4x3d translate(Vector3fc var1, Matrix4x3d var2);

   Matrix4x3d translate(double var1, double var3, double var5, Matrix4x3d var7);

   Matrix4x3d translateLocal(Vector3fc var1, Matrix4x3d var2);

   Matrix4x3d translateLocal(Vector3dc var1, Matrix4x3d var2);

   Matrix4x3d translateLocal(double var1, double var3, double var5, Matrix4x3d var7);

   Matrix4x3d rotateX(double var1, Matrix4x3d var3);

   Matrix4x3d rotateY(double var1, Matrix4x3d var3);

   Matrix4x3d rotateZ(double var1, Matrix4x3d var3);

   Matrix4x3d rotateXYZ(double var1, double var3, double var5, Matrix4x3d var7);

   Matrix4x3d rotateZYX(double var1, double var3, double var5, Matrix4x3d var7);

   Matrix4x3d rotateYXZ(double var1, double var3, double var5, Matrix4x3d var7);

   Matrix4x3d rotate(Quaterniondc var1, Matrix4x3d var2);

   Matrix4x3d rotate(Quaternionfc var1, Matrix4x3d var2);

   Matrix4x3d rotateTranslation(Quaterniondc var1, Matrix4x3d var2);

   Matrix4x3d rotateTranslation(Quaternionfc var1, Matrix4x3d var2);

   Matrix4x3d rotateLocal(Quaterniondc var1, Matrix4x3d var2);

   Matrix4x3d rotateLocal(Quaternionfc var1, Matrix4x3d var2);

   Matrix4x3d rotate(AxisAngle4f var1, Matrix4x3d var2);

   Matrix4x3d rotate(AxisAngle4d var1, Matrix4x3d var2);

   Matrix4x3d rotate(double var1, Vector3dc var3, Matrix4x3d var4);

   Matrix4x3d rotate(double var1, Vector3fc var3, Matrix4x3d var4);

   Vector4d getRow(int var1, Vector4d var2) throws IndexOutOfBoundsException;

   Vector3d getColumn(int var1, Vector3d var2) throws IndexOutOfBoundsException;

   Matrix4x3d normal(Matrix4x3d var1);

   Matrix3d normal(Matrix3d var1);

   Matrix3d cofactor3x3(Matrix3d var1);

   Matrix4x3d cofactor3x3(Matrix4x3d var1);

   Matrix4x3d normalize3x3(Matrix4x3d var1);

   Matrix3d normalize3x3(Matrix3d var1);

   Matrix4x3d reflect(double var1, double var3, double var5, double var7, Matrix4x3d var9);

   Matrix4x3d reflect(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13);

   Matrix4x3d reflect(Quaterniondc var1, Vector3dc var2, Matrix4x3d var3);

   Matrix4x3d reflect(Vector3dc var1, Vector3dc var2, Matrix4x3d var3);

   Matrix4x3d ortho(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13, Matrix4x3d var14);

   Matrix4x3d ortho(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13);

   Matrix4x3d orthoLH(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13, Matrix4x3d var14);

   Matrix4x3d orthoLH(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13);

   Matrix4x3d orthoSymmetric(double var1, double var3, double var5, double var7, boolean var9, Matrix4x3d var10);

   Matrix4x3d orthoSymmetric(double var1, double var3, double var5, double var7, Matrix4x3d var9);

   Matrix4x3d orthoSymmetricLH(double var1, double var3, double var5, double var7, boolean var9, Matrix4x3d var10);

   Matrix4x3d orthoSymmetricLH(double var1, double var3, double var5, double var7, Matrix4x3d var9);

   Matrix4x3d ortho2D(double var1, double var3, double var5, double var7, Matrix4x3d var9);

   Matrix4x3d ortho2DLH(double var1, double var3, double var5, double var7, Matrix4x3d var9);

   Matrix4x3d lookAlong(Vector3dc var1, Vector3dc var2, Matrix4x3d var3);

   Matrix4x3d lookAlong(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13);

   Matrix4x3d lookAt(Vector3dc var1, Vector3dc var2, Vector3dc var3, Matrix4x3d var4);

   Matrix4x3d lookAt(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4x3d var19);

   Matrix4x3d lookAtLH(Vector3dc var1, Vector3dc var2, Vector3dc var3, Matrix4x3d var4);

   Matrix4x3d lookAtLH(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4x3d var19);

   Vector4d frustumPlane(int var1, Vector4d var2);

   Vector3d positiveZ(Vector3d var1);

   Vector3d normalizedPositiveZ(Vector3d var1);

   Vector3d positiveX(Vector3d var1);

   Vector3d normalizedPositiveX(Vector3d var1);

   Vector3d positiveY(Vector3d var1);

   Vector3d normalizedPositiveY(Vector3d var1);

   Vector3d origin(Vector3d var1);

   Matrix4x3d shadow(Vector4dc var1, double var2, double var4, double var6, double var8, Matrix4x3d var10);

   Matrix4x3d shadow(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, Matrix4x3d var17);

   Matrix4x3d shadow(Vector4dc var1, Matrix4x3dc var2, Matrix4x3d var3);

   Matrix4x3d shadow(double var1, double var3, double var5, double var7, Matrix4x3dc var9, Matrix4x3d var10);

   Matrix4x3d pick(double var1, double var3, double var5, double var7, int[] var9, Matrix4x3d var10);

   Matrix4x3d arcball(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13);

   Matrix4x3d arcball(double var1, Vector3dc var3, double var4, double var6, Matrix4x3d var8);

   Matrix4x3d transformAab(double var1, double var3, double var5, double var7, double var9, double var11, Vector3d var13, Vector3d var14);

   Matrix4x3d transformAab(Vector3dc var1, Vector3dc var2, Vector3d var3, Vector3d var4);

   Matrix4x3d lerp(Matrix4x3dc var1, double var2, Matrix4x3d var4);

   Matrix4x3d rotateTowards(Vector3dc var1, Vector3dc var2, Matrix4x3d var3);

   Matrix4x3d rotateTowards(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4x3d var13);

   Vector3d getEulerAnglesZYX(Vector3d var1);

   Matrix4x3d obliqueZ(double var1, double var3, Matrix4x3d var5);

   boolean equals(Matrix4x3dc var1, double var2);

   boolean isFinite();
}
