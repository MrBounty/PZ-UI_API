package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix4x3fc {
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

   float m00();

   float m01();

   float m02();

   float m10();

   float m11();

   float m12();

   float m20();

   float m21();

   float m22();

   float m30();

   float m31();

   float m32();

   Matrix4f get(Matrix4f var1);

   Matrix4d get(Matrix4d var1);

   Matrix4x3f mul(Matrix4x3fc var1, Matrix4x3f var2);

   Matrix4x3f mulTranslation(Matrix4x3fc var1, Matrix4x3f var2);

   Matrix4x3f mulOrtho(Matrix4x3fc var1, Matrix4x3f var2);

   Matrix4x3f fma(Matrix4x3fc var1, float var2, Matrix4x3f var3);

   Matrix4x3f add(Matrix4x3fc var1, Matrix4x3f var2);

   Matrix4x3f sub(Matrix4x3fc var1, Matrix4x3f var2);

   Matrix4x3f mulComponentWise(Matrix4x3fc var1, Matrix4x3f var2);

   float determinant();

   Matrix4x3f invert(Matrix4x3f var1);

   Matrix4f invert(Matrix4f var1);

   Matrix4x3f invertOrtho(Matrix4x3f var1);

   Matrix4x3f transpose3x3(Matrix4x3f var1);

   Matrix3f transpose3x3(Matrix3f var1);

   Vector3f getTranslation(Vector3f var1);

   Vector3f getScale(Vector3f var1);

   Matrix4x3f get(Matrix4x3f var1);

   Matrix4x3d get(Matrix4x3d var1);

   AxisAngle4f getRotation(AxisAngle4f var1);

   AxisAngle4d getRotation(AxisAngle4d var1);

   Quaternionf getUnnormalizedRotation(Quaternionf var1);

   Quaternionf getNormalizedRotation(Quaternionf var1);

   Quaterniond getUnnormalizedRotation(Quaterniond var1);

   Quaterniond getNormalizedRotation(Quaterniond var1);

   FloatBuffer get(FloatBuffer var1);

   FloatBuffer get(int var1, FloatBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   Matrix4x3fc getToAddress(long var1);

   float[] get(float[] var1, int var2);

   float[] get(float[] var1);

   float[] get4x4(float[] var1, int var2);

   float[] get4x4(float[] var1);

   FloatBuffer get4x4(FloatBuffer var1);

   FloatBuffer get4x4(int var1, FloatBuffer var2);

   ByteBuffer get4x4(ByteBuffer var1);

   ByteBuffer get4x4(int var1, ByteBuffer var2);

   FloatBuffer get3x4(FloatBuffer var1);

   FloatBuffer get3x4(int var1, FloatBuffer var2);

   ByteBuffer get3x4(ByteBuffer var1);

   ByteBuffer get3x4(int var1, ByteBuffer var2);

   FloatBuffer getTransposed(FloatBuffer var1);

   FloatBuffer getTransposed(int var1, FloatBuffer var2);

   ByteBuffer getTransposed(ByteBuffer var1);

   ByteBuffer getTransposed(int var1, ByteBuffer var2);

   float[] getTransposed(float[] var1, int var2);

   float[] getTransposed(float[] var1);

   Vector4f transform(Vector4f var1);

   Vector4f transform(Vector4fc var1, Vector4f var2);

   Vector3f transformPosition(Vector3f var1);

   Vector3f transformPosition(Vector3fc var1, Vector3f var2);

   Vector3f transformDirection(Vector3f var1);

   Vector3f transformDirection(Vector3fc var1, Vector3f var2);

   Matrix4x3f scale(Vector3fc var1, Matrix4x3f var2);

   Matrix4x3f scale(float var1, Matrix4x3f var2);

   Matrix4x3f scaleXY(float var1, float var2, Matrix4x3f var3);

   Matrix4x3f scale(float var1, float var2, float var3, Matrix4x3f var4);

   Matrix4x3f scaleLocal(float var1, float var2, float var3, Matrix4x3f var4);

   Matrix4x3f rotateX(float var1, Matrix4x3f var2);

   Matrix4x3f rotateY(float var1, Matrix4x3f var2);

   Matrix4x3f rotateZ(float var1, Matrix4x3f var2);

   Matrix4x3f rotateXYZ(float var1, float var2, float var3, Matrix4x3f var4);

   Matrix4x3f rotateZYX(float var1, float var2, float var3, Matrix4x3f var4);

   Matrix4x3f rotateYXZ(float var1, float var2, float var3, Matrix4x3f var4);

   Matrix4x3f rotate(float var1, float var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f rotateTranslation(float var1, float var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f rotateAround(Quaternionfc var1, float var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f rotateLocal(float var1, float var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f translate(Vector3fc var1, Matrix4x3f var2);

   Matrix4x3f translate(float var1, float var2, float var3, Matrix4x3f var4);

   Matrix4x3f translateLocal(Vector3fc var1, Matrix4x3f var2);

   Matrix4x3f translateLocal(float var1, float var2, float var3, Matrix4x3f var4);

   Matrix4x3f ortho(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4x3f var8);

   Matrix4x3f ortho(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7);

   Matrix4x3f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4x3f var8);

   Matrix4x3f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7);

   Matrix4x3f orthoSymmetric(float var1, float var2, float var3, float var4, boolean var5, Matrix4x3f var6);

   Matrix4x3f orthoSymmetric(float var1, float var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f orthoSymmetricLH(float var1, float var2, float var3, float var4, boolean var5, Matrix4x3f var6);

   Matrix4x3f orthoSymmetricLH(float var1, float var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f ortho2D(float var1, float var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f ortho2DLH(float var1, float var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f lookAlong(Vector3fc var1, Vector3fc var2, Matrix4x3f var3);

   Matrix4x3f lookAlong(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7);

   Matrix4x3f lookAt(Vector3fc var1, Vector3fc var2, Vector3fc var3, Matrix4x3f var4);

   Matrix4x3f lookAt(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4x3f var10);

   Matrix4x3f lookAtLH(Vector3fc var1, Vector3fc var2, Vector3fc var3, Matrix4x3f var4);

   Matrix4x3f lookAtLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4x3f var10);

   Matrix4x3f rotate(Quaternionfc var1, Matrix4x3f var2);

   Matrix4x3f rotateTranslation(Quaternionfc var1, Matrix4x3f var2);

   Matrix4x3f rotateLocal(Quaternionfc var1, Matrix4x3f var2);

   Matrix4x3f rotate(AxisAngle4f var1, Matrix4x3f var2);

   Matrix4x3f rotate(float var1, Vector3fc var2, Matrix4x3f var3);

   Matrix4x3f reflect(float var1, float var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f reflect(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7);

   Matrix4x3f reflect(Quaternionfc var1, Vector3fc var2, Matrix4x3f var3);

   Matrix4x3f reflect(Vector3fc var1, Vector3fc var2, Matrix4x3f var3);

   Vector4f getRow(int var1, Vector4f var2) throws IndexOutOfBoundsException;

   Vector3f getColumn(int var1, Vector3f var2) throws IndexOutOfBoundsException;

   Matrix4x3f normal(Matrix4x3f var1);

   Matrix3f normal(Matrix3f var1);

   Matrix3f cofactor3x3(Matrix3f var1);

   Matrix4x3f cofactor3x3(Matrix4x3f var1);

   Matrix4x3f normalize3x3(Matrix4x3f var1);

   Matrix3f normalize3x3(Matrix3f var1);

   Vector4f frustumPlane(int var1, Vector4f var2);

   Vector3f positiveZ(Vector3f var1);

   Vector3f normalizedPositiveZ(Vector3f var1);

   Vector3f positiveX(Vector3f var1);

   Vector3f normalizedPositiveX(Vector3f var1);

   Vector3f positiveY(Vector3f var1);

   Vector3f normalizedPositiveY(Vector3f var1);

   Vector3f origin(Vector3f var1);

   Matrix4x3f shadow(Vector4fc var1, float var2, float var3, float var4, float var5, Matrix4x3f var6);

   Matrix4x3f shadow(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Matrix4x3f var9);

   Matrix4x3f shadow(Vector4fc var1, Matrix4x3fc var2, Matrix4x3f var3);

   Matrix4x3f shadow(float var1, float var2, float var3, float var4, Matrix4x3fc var5, Matrix4x3f var6);

   Matrix4x3f pick(float var1, float var2, float var3, float var4, int[] var5, Matrix4x3f var6);

   Matrix4x3f arcball(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7);

   Matrix4x3f arcball(float var1, Vector3fc var2, float var3, float var4, Matrix4x3f var5);

   Matrix4x3f transformAab(float var1, float var2, float var3, float var4, float var5, float var6, Vector3f var7, Vector3f var8);

   Matrix4x3f transformAab(Vector3fc var1, Vector3fc var2, Vector3f var3, Vector3f var4);

   Matrix4x3f lerp(Matrix4x3fc var1, float var2, Matrix4x3f var3);

   Matrix4x3f rotateTowards(Vector3fc var1, Vector3fc var2, Matrix4x3f var3);

   Matrix4x3f rotateTowards(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4x3f var7);

   Vector3f getEulerAnglesZYX(Vector3f var1);

   Matrix4x3f obliqueZ(float var1, float var2, Matrix4x3f var3);

   Matrix4x3f withLookAtUp(Vector3fc var1, Matrix4x3f var2);

   Matrix4x3f withLookAtUp(float var1, float var2, float var3, Matrix4x3f var4);

   boolean equals(Matrix4x3fc var1, float var2);

   boolean isFinite();
}
