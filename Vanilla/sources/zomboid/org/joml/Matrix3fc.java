package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix3fc {
   float m00();

   float m01();

   float m02();

   float m10();

   float m11();

   float m12();

   float m20();

   float m21();

   float m22();

   Matrix3f mul(Matrix3fc var1, Matrix3f var2);

   Matrix3f mulLocal(Matrix3fc var1, Matrix3f var2);

   float determinant();

   Matrix3f invert(Matrix3f var1);

   Matrix3f transpose(Matrix3f var1);

   Matrix3f get(Matrix3f var1);

   Matrix4f get(Matrix4f var1);

   AxisAngle4f getRotation(AxisAngle4f var1);

   Quaternionf getUnnormalizedRotation(Quaternionf var1);

   Quaternionf getNormalizedRotation(Quaternionf var1);

   Quaterniond getUnnormalizedRotation(Quaterniond var1);

   Quaterniond getNormalizedRotation(Quaterniond var1);

   FloatBuffer get(FloatBuffer var1);

   FloatBuffer get(int var1, FloatBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   FloatBuffer get3x4(FloatBuffer var1);

   FloatBuffer get3x4(int var1, FloatBuffer var2);

   ByteBuffer get3x4(ByteBuffer var1);

   ByteBuffer get3x4(int var1, ByteBuffer var2);

   FloatBuffer getTransposed(FloatBuffer var1);

   FloatBuffer getTransposed(int var1, FloatBuffer var2);

   ByteBuffer getTransposed(ByteBuffer var1);

   ByteBuffer getTransposed(int var1, ByteBuffer var2);

   Matrix3fc getToAddress(long var1);

   float[] get(float[] var1, int var2);

   float[] get(float[] var1);

   Matrix3f scale(Vector3fc var1, Matrix3f var2);

   Matrix3f scale(float var1, float var2, float var3, Matrix3f var4);

   Matrix3f scale(float var1, Matrix3f var2);

   Matrix3f scaleLocal(float var1, float var2, float var3, Matrix3f var4);

   Vector3f transform(Vector3f var1);

   Vector3f transform(Vector3fc var1, Vector3f var2);

   Vector3f transform(float var1, float var2, float var3, Vector3f var4);

   Vector3f transformTranspose(Vector3f var1);

   Vector3f transformTranspose(Vector3fc var1, Vector3f var2);

   Vector3f transformTranspose(float var1, float var2, float var3, Vector3f var4);

   Matrix3f rotateX(float var1, Matrix3f var2);

   Matrix3f rotateY(float var1, Matrix3f var2);

   Matrix3f rotateZ(float var1, Matrix3f var2);

   Matrix3f rotateXYZ(float var1, float var2, float var3, Matrix3f var4);

   Matrix3f rotateZYX(float var1, float var2, float var3, Matrix3f var4);

   Matrix3f rotateYXZ(float var1, float var2, float var3, Matrix3f var4);

   Matrix3f rotate(float var1, float var2, float var3, float var4, Matrix3f var5);

   Matrix3f rotateLocal(float var1, float var2, float var3, float var4, Matrix3f var5);

   Matrix3f rotateLocalX(float var1, Matrix3f var2);

   Matrix3f rotateLocalY(float var1, Matrix3f var2);

   Matrix3f rotateLocalZ(float var1, Matrix3f var2);

   Matrix3f rotate(Quaternionfc var1, Matrix3f var2);

   Matrix3f rotateLocal(Quaternionfc var1, Matrix3f var2);

   Matrix3f rotate(AxisAngle4f var1, Matrix3f var2);

   Matrix3f rotate(float var1, Vector3fc var2, Matrix3f var3);

   Matrix3f lookAlong(Vector3fc var1, Vector3fc var2, Matrix3f var3);

   Matrix3f lookAlong(float var1, float var2, float var3, float var4, float var5, float var6, Matrix3f var7);

   Vector3f getRow(int var1, Vector3f var2) throws IndexOutOfBoundsException;

   Vector3f getColumn(int var1, Vector3f var2) throws IndexOutOfBoundsException;

   float get(int var1, int var2);

   float getRowColumn(int var1, int var2);

   Matrix3f normal(Matrix3f var1);

   Matrix3f cofactor(Matrix3f var1);

   Vector3f getScale(Vector3f var1);

   Vector3f positiveZ(Vector3f var1);

   Vector3f normalizedPositiveZ(Vector3f var1);

   Vector3f positiveX(Vector3f var1);

   Vector3f normalizedPositiveX(Vector3f var1);

   Vector3f positiveY(Vector3f var1);

   Vector3f normalizedPositiveY(Vector3f var1);

   Matrix3f add(Matrix3fc var1, Matrix3f var2);

   Matrix3f sub(Matrix3fc var1, Matrix3f var2);

   Matrix3f mulComponentWise(Matrix3fc var1, Matrix3f var2);

   Matrix3f lerp(Matrix3fc var1, float var2, Matrix3f var3);

   Matrix3f rotateTowards(Vector3fc var1, Vector3fc var2, Matrix3f var3);

   Matrix3f rotateTowards(float var1, float var2, float var3, float var4, float var5, float var6, Matrix3f var7);

   Vector3f getEulerAnglesZYX(Vector3f var1);

   Matrix3f obliqueZ(float var1, float var2, Matrix3f var3);

   boolean equals(Matrix3fc var1, float var2);

   Matrix3f reflect(float var1, float var2, float var3, Matrix3f var4);

   Matrix3f reflect(Quaternionfc var1, Matrix3f var2);

   Matrix3f reflect(Vector3fc var1, Matrix3f var2);

   boolean isFinite();

   float quadraticFormProduct(float var1, float var2, float var3);

   float quadraticFormProduct(Vector3fc var1);
}
