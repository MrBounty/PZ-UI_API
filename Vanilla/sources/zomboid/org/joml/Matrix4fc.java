package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix4fc {
   int PLANE_NX = 0;
   int PLANE_PX = 1;
   int PLANE_NY = 2;
   int PLANE_PY = 3;
   int PLANE_NZ = 4;
   int PLANE_PZ = 5;
   int CORNER_NXNYNZ = 0;
   int CORNER_PXNYNZ = 1;
   int CORNER_PXPYNZ = 2;
   int CORNER_NXPYNZ = 3;
   int CORNER_PXNYPZ = 4;
   int CORNER_NXNYPZ = 5;
   int CORNER_NXPYPZ = 6;
   int CORNER_PXPYPZ = 7;
   byte PROPERTY_PERSPECTIVE = 1;
   byte PROPERTY_AFFINE = 2;
   byte PROPERTY_IDENTITY = 4;
   byte PROPERTY_TRANSLATION = 8;
   byte PROPERTY_ORTHONORMAL = 16;

   int properties();

   float m00();

   float m01();

   float m02();

   float m03();

   float m10();

   float m11();

   float m12();

   float m13();

   float m20();

   float m21();

   float m22();

   float m23();

   float m30();

   float m31();

   float m32();

   float m33();

   Matrix4f mul(Matrix4fc var1, Matrix4f var2);

   Matrix4f mul0(Matrix4fc var1, Matrix4f var2);

   Matrix4f mul(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12, float var13, float var14, float var15, float var16, Matrix4f var17);

   Matrix4f mul3x3(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10);

   Matrix4f mulLocal(Matrix4fc var1, Matrix4f var2);

   Matrix4f mulLocalAffine(Matrix4fc var1, Matrix4f var2);

   Matrix4f mul(Matrix3x2fc var1, Matrix4f var2);

   Matrix4f mul(Matrix4x3fc var1, Matrix4f var2);

   Matrix4f mulPerspectiveAffine(Matrix4fc var1, Matrix4f var2);

   Matrix4f mulPerspectiveAffine(Matrix4x3fc var1, Matrix4f var2);

   Matrix4f mulAffineR(Matrix4fc var1, Matrix4f var2);

   Matrix4f mulAffine(Matrix4fc var1, Matrix4f var2);

   Matrix4f mulTranslationAffine(Matrix4fc var1, Matrix4f var2);

   Matrix4f mulOrthoAffine(Matrix4fc var1, Matrix4f var2);

   Matrix4f fma4x3(Matrix4fc var1, float var2, Matrix4f var3);

   Matrix4f add(Matrix4fc var1, Matrix4f var2);

   Matrix4f sub(Matrix4fc var1, Matrix4f var2);

   Matrix4f mulComponentWise(Matrix4fc var1, Matrix4f var2);

   Matrix4f add4x3(Matrix4fc var1, Matrix4f var2);

   Matrix4f sub4x3(Matrix4fc var1, Matrix4f var2);

   Matrix4f mul4x3ComponentWise(Matrix4fc var1, Matrix4f var2);

   float determinant();

   float determinant3x3();

   float determinantAffine();

   Matrix4f invert(Matrix4f var1);

   Matrix4f invertPerspective(Matrix4f var1);

   Matrix4f invertFrustum(Matrix4f var1);

   Matrix4f invertOrtho(Matrix4f var1);

   Matrix4f invertPerspectiveView(Matrix4fc var1, Matrix4f var2);

   Matrix4f invertPerspectiveView(Matrix4x3fc var1, Matrix4f var2);

   Matrix4f invertAffine(Matrix4f var1);

   Matrix4f transpose(Matrix4f var1);

   Matrix4f transpose3x3(Matrix4f var1);

   Matrix3f transpose3x3(Matrix3f var1);

   Vector3f getTranslation(Vector3f var1);

   Vector3f getScale(Vector3f var1);

   Matrix4f get(Matrix4f var1);

   Matrix4x3f get4x3(Matrix4x3f var1);

   Matrix4d get(Matrix4d var1);

   Matrix3f get3x3(Matrix3f var1);

   Matrix3d get3x3(Matrix3d var1);

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

   FloatBuffer get4x3(FloatBuffer var1);

   FloatBuffer get4x3(int var1, FloatBuffer var2);

   ByteBuffer get4x3(ByteBuffer var1);

   ByteBuffer get4x3(int var1, ByteBuffer var2);

   FloatBuffer get3x4(FloatBuffer var1);

   FloatBuffer get3x4(int var1, FloatBuffer var2);

   ByteBuffer get3x4(ByteBuffer var1);

   ByteBuffer get3x4(int var1, ByteBuffer var2);

   FloatBuffer getTransposed(FloatBuffer var1);

   FloatBuffer getTransposed(int var1, FloatBuffer var2);

   ByteBuffer getTransposed(ByteBuffer var1);

   ByteBuffer getTransposed(int var1, ByteBuffer var2);

   FloatBuffer get4x3Transposed(FloatBuffer var1);

   FloatBuffer get4x3Transposed(int var1, FloatBuffer var2);

   ByteBuffer get4x3Transposed(ByteBuffer var1);

   ByteBuffer get4x3Transposed(int var1, ByteBuffer var2);

   Matrix4fc getToAddress(long var1);

   float[] get(float[] var1, int var2);

   float[] get(float[] var1);

   Vector4f transform(Vector4f var1);

   Vector4f transform(Vector4fc var1, Vector4f var2);

   Vector4f transform(float var1, float var2, float var3, float var4, Vector4f var5);

   Vector4f transformTranspose(Vector4f var1);

   Vector4f transformTranspose(Vector4fc var1, Vector4f var2);

   Vector4f transformTranspose(float var1, float var2, float var3, float var4, Vector4f var5);

   Vector4f transformProject(Vector4f var1);

   Vector4f transformProject(Vector4fc var1, Vector4f var2);

   Vector4f transformProject(float var1, float var2, float var3, float var4, Vector4f var5);

   Vector3f transformProject(Vector3f var1);

   Vector3f transformProject(Vector3fc var1, Vector3f var2);

   Vector3f transformProject(Vector4fc var1, Vector3f var2);

   Vector3f transformProject(float var1, float var2, float var3, Vector3f var4);

   Vector3f transformProject(float var1, float var2, float var3, float var4, Vector3f var5);

   Vector3f transformPosition(Vector3f var1);

   Vector3f transformPosition(Vector3fc var1, Vector3f var2);

   Vector3f transformPosition(float var1, float var2, float var3, Vector3f var4);

   Vector3f transformDirection(Vector3f var1);

   Vector3f transformDirection(Vector3fc var1, Vector3f var2);

   Vector3f transformDirection(float var1, float var2, float var3, Vector3f var4);

   Vector4f transformAffine(Vector4f var1);

   Vector4f transformAffine(Vector4fc var1, Vector4f var2);

   Vector4f transformAffine(float var1, float var2, float var3, float var4, Vector4f var5);

   Matrix4f scale(Vector3fc var1, Matrix4f var2);

   Matrix4f scale(float var1, Matrix4f var2);

   Matrix4f scaleXY(float var1, float var2, Matrix4f var3);

   Matrix4f scale(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f scaleAround(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f scaleAround(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f scaleLocal(float var1, Matrix4f var2);

   Matrix4f scaleLocal(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f scaleAroundLocal(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f scaleAroundLocal(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f rotateX(float var1, Matrix4f var2);

   Matrix4f rotateY(float var1, Matrix4f var2);

   Matrix4f rotateZ(float var1, Matrix4f var2);

   Matrix4f rotateTowardsXY(float var1, float var2, Matrix4f var3);

   Matrix4f rotateXYZ(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f rotateAffineXYZ(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f rotateZYX(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f rotateAffineZYX(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f rotateYXZ(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f rotateAffineYXZ(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f rotate(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f rotateTranslation(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f rotateAffine(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f rotateLocal(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f rotateLocalX(float var1, Matrix4f var2);

   Matrix4f rotateLocalY(float var1, Matrix4f var2);

   Matrix4f rotateLocalZ(float var1, Matrix4f var2);

   Matrix4f translate(Vector3fc var1, Matrix4f var2);

   Matrix4f translate(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f translateLocal(Vector3fc var1, Matrix4f var2);

   Matrix4f translateLocal(float var1, float var2, float var3, Matrix4f var4);

   Matrix4f ortho(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8);

   Matrix4f ortho(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8);

   Matrix4f orthoLH(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f orthoSymmetric(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6);

   Matrix4f orthoSymmetric(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f orthoSymmetricLH(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6);

   Matrix4f orthoSymmetricLH(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f ortho2D(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f ortho2DLH(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f lookAlong(Vector3fc var1, Vector3fc var2, Matrix4f var3);

   Matrix4f lookAlong(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f lookAt(Vector3fc var1, Vector3fc var2, Vector3fc var3, Matrix4f var4);

   Matrix4f lookAt(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10);

   Matrix4f lookAtPerspective(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10);

   Matrix4f lookAtLH(Vector3fc var1, Vector3fc var2, Vector3fc var3, Matrix4f var4);

   Matrix4f lookAtLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10);

   Matrix4f lookAtPerspectiveLH(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, Matrix4f var10);

   Matrix4f perspective(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6);

   Matrix4f perspective(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f perspectiveRect(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6);

   Matrix4f perspectiveRect(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f perspectiveRect(float var1, float var2, float var3, float var4, boolean var5);

   Matrix4f perspectiveRect(float var1, float var2, float var3, float var4);

   Matrix4f perspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8);

   Matrix4f perspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f perspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7);

   Matrix4f perspectiveOffCenter(float var1, float var2, float var3, float var4, float var5, float var6);

   Matrix4f perspectiveLH(float var1, float var2, float var3, float var4, boolean var5, Matrix4f var6);

   Matrix4f perspectiveLH(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f frustum(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8);

   Matrix4f frustum(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f frustumLH(float var1, float var2, float var3, float var4, float var5, float var6, boolean var7, Matrix4f var8);

   Matrix4f frustumLH(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f rotate(Quaternionfc var1, Matrix4f var2);

   Matrix4f rotateAffine(Quaternionfc var1, Matrix4f var2);

   Matrix4f rotateTranslation(Quaternionfc var1, Matrix4f var2);

   Matrix4f rotateAroundAffine(Quaternionfc var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f rotateAround(Quaternionfc var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f rotateLocal(Quaternionfc var1, Matrix4f var2);

   Matrix4f rotateAroundLocal(Quaternionfc var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f rotate(AxisAngle4f var1, Matrix4f var2);

   Matrix4f rotate(float var1, Vector3fc var2, Matrix4f var3);

   Vector4f unproject(float var1, float var2, float var3, int[] var4, Vector4f var5);

   Vector3f unproject(float var1, float var2, float var3, int[] var4, Vector3f var5);

   Vector4f unproject(Vector3fc var1, int[] var2, Vector4f var3);

   Vector3f unproject(Vector3fc var1, int[] var2, Vector3f var3);

   Matrix4f unprojectRay(float var1, float var2, int[] var3, Vector3f var4, Vector3f var5);

   Matrix4f unprojectRay(Vector2fc var1, int[] var2, Vector3f var3, Vector3f var4);

   Vector4f unprojectInv(Vector3fc var1, int[] var2, Vector4f var3);

   Vector4f unprojectInv(float var1, float var2, float var3, int[] var4, Vector4f var5);

   Matrix4f unprojectInvRay(Vector2fc var1, int[] var2, Vector3f var3, Vector3f var4);

   Matrix4f unprojectInvRay(float var1, float var2, int[] var3, Vector3f var4, Vector3f var5);

   Vector3f unprojectInv(Vector3fc var1, int[] var2, Vector3f var3);

   Vector3f unprojectInv(float var1, float var2, float var3, int[] var4, Vector3f var5);

   Vector4f project(float var1, float var2, float var3, int[] var4, Vector4f var5);

   Vector3f project(float var1, float var2, float var3, int[] var4, Vector3f var5);

   Vector4f project(Vector3fc var1, int[] var2, Vector4f var3);

   Vector3f project(Vector3fc var1, int[] var2, Vector3f var3);

   Matrix4f reflect(float var1, float var2, float var3, float var4, Matrix4f var5);

   Matrix4f reflect(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f reflect(Quaternionfc var1, Vector3fc var2, Matrix4f var3);

   Matrix4f reflect(Vector3fc var1, Vector3fc var2, Matrix4f var3);

   Vector4f getRow(int var1, Vector4f var2) throws IndexOutOfBoundsException;

   Vector3f getRow(int var1, Vector3f var2) throws IndexOutOfBoundsException;

   Vector4f getColumn(int var1, Vector4f var2) throws IndexOutOfBoundsException;

   Vector3f getColumn(int var1, Vector3f var2) throws IndexOutOfBoundsException;

   float get(int var1, int var2);

   float getRowColumn(int var1, int var2);

   Matrix4f normal(Matrix4f var1);

   Matrix3f normal(Matrix3f var1);

   Matrix3f cofactor3x3(Matrix3f var1);

   Matrix4f cofactor3x3(Matrix4f var1);

   Matrix4f normalize3x3(Matrix4f var1);

   Matrix3f normalize3x3(Matrix3f var1);

   Vector4f frustumPlane(int var1, Vector4f var2);

   Vector3f frustumCorner(int var1, Vector3f var2);

   Vector3f perspectiveOrigin(Vector3f var1);

   Vector3f perspectiveInvOrigin(Vector3f var1);

   float perspectiveFov();

   float perspectiveNear();

   float perspectiveFar();

   Vector3f frustumRayDir(float var1, float var2, Vector3f var3);

   Vector3f positiveZ(Vector3f var1);

   Vector3f normalizedPositiveZ(Vector3f var1);

   Vector3f positiveX(Vector3f var1);

   Vector3f normalizedPositiveX(Vector3f var1);

   Vector3f positiveY(Vector3f var1);

   Vector3f normalizedPositiveY(Vector3f var1);

   Vector3f originAffine(Vector3f var1);

   Vector3f origin(Vector3f var1);

   Matrix4f shadow(Vector4f var1, float var2, float var3, float var4, float var5, Matrix4f var6);

   Matrix4f shadow(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Matrix4f var9);

   Matrix4f shadow(Vector4f var1, Matrix4fc var2, Matrix4f var3);

   Matrix4f shadow(float var1, float var2, float var3, float var4, Matrix4fc var5, Matrix4f var6);

   Matrix4f pick(float var1, float var2, float var3, float var4, int[] var5, Matrix4f var6);

   boolean isAffine();

   Matrix4f arcball(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Matrix4f arcball(float var1, Vector3fc var2, float var3, float var4, Matrix4f var5);

   Matrix4f frustumAabb(Vector3f var1, Vector3f var2);

   Matrix4f projectedGridRange(Matrix4fc var1, float var2, float var3, Matrix4f var4);

   Matrix4f perspectiveFrustumSlice(float var1, float var2, Matrix4f var3);

   Matrix4f orthoCrop(Matrix4fc var1, Matrix4f var2);

   Matrix4f transformAab(float var1, float var2, float var3, float var4, float var5, float var6, Vector3f var7, Vector3f var8);

   Matrix4f transformAab(Vector3fc var1, Vector3fc var2, Vector3f var3, Vector3f var4);

   Matrix4f lerp(Matrix4fc var1, float var2, Matrix4f var3);

   Matrix4f rotateTowards(Vector3fc var1, Vector3fc var2, Matrix4f var3);

   Matrix4f rotateTowards(float var1, float var2, float var3, float var4, float var5, float var6, Matrix4f var7);

   Vector3f getEulerAnglesZYX(Vector3f var1);

   boolean testPoint(float var1, float var2, float var3);

   boolean testSphere(float var1, float var2, float var3, float var4);

   boolean testAab(float var1, float var2, float var3, float var4, float var5, float var6);

   Matrix4f obliqueZ(float var1, float var2, Matrix4f var3);

   Matrix4f withLookAtUp(Vector3fc var1, Matrix4f var2);

   Matrix4f withLookAtUp(float var1, float var2, float var3, Matrix4f var4);

   boolean equals(Matrix4fc var1, float var2);

   boolean isFinite();
}
