package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public interface Matrix4dc {
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

   double m00();

   double m01();

   double m02();

   double m03();

   double m10();

   double m11();

   double m12();

   double m13();

   double m20();

   double m21();

   double m22();

   double m23();

   double m30();

   double m31();

   double m32();

   double m33();

   Matrix4d mul(Matrix4dc var1, Matrix4d var2);

   Matrix4d mul0(Matrix4dc var1, Matrix4d var2);

   Matrix4d mul(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29, double var31, Matrix4d var33);

   Matrix4d mul3x3(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4d var19);

   Matrix4d mulLocal(Matrix4dc var1, Matrix4d var2);

   Matrix4d mulLocalAffine(Matrix4dc var1, Matrix4d var2);

   Matrix4d mul(Matrix3x2dc var1, Matrix4d var2);

   Matrix4d mul(Matrix3x2fc var1, Matrix4d var2);

   Matrix4d mul(Matrix4x3dc var1, Matrix4d var2);

   Matrix4d mul(Matrix4x3fc var1, Matrix4d var2);

   Matrix4d mul(Matrix4fc var1, Matrix4d var2);

   Matrix4d mulPerspectiveAffine(Matrix4dc var1, Matrix4d var2);

   Matrix4d mulPerspectiveAffine(Matrix4x3dc var1, Matrix4d var2);

   Matrix4d mulAffineR(Matrix4dc var1, Matrix4d var2);

   Matrix4d mulAffine(Matrix4dc var1, Matrix4d var2);

   Matrix4d mulTranslationAffine(Matrix4dc var1, Matrix4d var2);

   Matrix4d mulOrthoAffine(Matrix4dc var1, Matrix4d var2);

   Matrix4d fma4x3(Matrix4dc var1, double var2, Matrix4d var4);

   Matrix4d add(Matrix4dc var1, Matrix4d var2);

   Matrix4d sub(Matrix4dc var1, Matrix4d var2);

   Matrix4d mulComponentWise(Matrix4dc var1, Matrix4d var2);

   Matrix4d add4x3(Matrix4dc var1, Matrix4d var2);

   Matrix4d add4x3(Matrix4fc var1, Matrix4d var2);

   Matrix4d sub4x3(Matrix4dc var1, Matrix4d var2);

   Matrix4d mul4x3ComponentWise(Matrix4dc var1, Matrix4d var2);

   double determinant();

   double determinant3x3();

   double determinantAffine();

   Matrix4d invert(Matrix4d var1);

   Matrix4d invertPerspective(Matrix4d var1);

   Matrix4d invertFrustum(Matrix4d var1);

   Matrix4d invertOrtho(Matrix4d var1);

   Matrix4d invertPerspectiveView(Matrix4dc var1, Matrix4d var2);

   Matrix4d invertPerspectiveView(Matrix4x3dc var1, Matrix4d var2);

   Matrix4d invertAffine(Matrix4d var1);

   Matrix4d transpose(Matrix4d var1);

   Matrix4d transpose3x3(Matrix4d var1);

   Matrix3d transpose3x3(Matrix3d var1);

   Vector3d getTranslation(Vector3d var1);

   Vector3d getScale(Vector3d var1);

   Matrix4d get(Matrix4d var1);

   Matrix4x3d get4x3(Matrix4x3d var1);

   Matrix3d get3x3(Matrix3d var1);

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

   Matrix4dc getToAddress(long var1);

   ByteBuffer getFloats(ByteBuffer var1);

   ByteBuffer getFloats(int var1, ByteBuffer var2);

   double[] get(double[] var1, int var2);

   double[] get(double[] var1);

   float[] get(float[] var1, int var2);

   float[] get(float[] var1);

   DoubleBuffer getTransposed(DoubleBuffer var1);

   DoubleBuffer getTransposed(int var1, DoubleBuffer var2);

   ByteBuffer getTransposed(ByteBuffer var1);

   ByteBuffer getTransposed(int var1, ByteBuffer var2);

   DoubleBuffer get4x3Transposed(DoubleBuffer var1);

   DoubleBuffer get4x3Transposed(int var1, DoubleBuffer var2);

   ByteBuffer get4x3Transposed(ByteBuffer var1);

   ByteBuffer get4x3Transposed(int var1, ByteBuffer var2);

   Vector4d transform(Vector4d var1);

   Vector4d transform(Vector4dc var1, Vector4d var2);

   Vector4d transform(double var1, double var3, double var5, double var7, Vector4d var9);

   Vector4d transformTranspose(Vector4d var1);

   Vector4d transformTranspose(Vector4dc var1, Vector4d var2);

   Vector4d transformTranspose(double var1, double var3, double var5, double var7, Vector4d var9);

   Vector4d transformProject(Vector4d var1);

   Vector4d transformProject(Vector4dc var1, Vector4d var2);

   Vector3d transformProject(Vector4dc var1, Vector3d var2);

   Vector4d transformProject(double var1, double var3, double var5, double var7, Vector4d var9);

   Vector3d transformProject(Vector3d var1);

   Vector3d transformProject(Vector3dc var1, Vector3d var2);

   Vector3d transformProject(double var1, double var3, double var5, Vector3d var7);

   Vector3d transformProject(double var1, double var3, double var5, double var7, Vector3d var9);

   Vector3d transformPosition(Vector3d var1);

   Vector3d transformPosition(Vector3dc var1, Vector3d var2);

   Vector3d transformPosition(double var1, double var3, double var5, Vector3d var7);

   Vector3d transformDirection(Vector3d var1);

   Vector3d transformDirection(Vector3dc var1, Vector3d var2);

   Vector3f transformDirection(Vector3f var1);

   Vector3f transformDirection(Vector3fc var1, Vector3f var2);

   Vector3d transformDirection(double var1, double var3, double var5, Vector3d var7);

   Vector3f transformDirection(double var1, double var3, double var5, Vector3f var7);

   Vector4d transformAffine(Vector4d var1);

   Vector4d transformAffine(Vector4dc var1, Vector4d var2);

   Vector4d transformAffine(double var1, double var3, double var5, double var7, Vector4d var9);

   Matrix4d scale(Vector3dc var1, Matrix4d var2);

   Matrix4d scale(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d scale(double var1, Matrix4d var3);

   Matrix4d scaleXY(double var1, double var3, Matrix4d var5);

   Matrix4d scaleAround(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Matrix4d scaleAround(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d scaleLocal(double var1, Matrix4d var3);

   Matrix4d scaleLocal(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d scaleAroundLocal(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Matrix4d scaleAroundLocal(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d rotate(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d rotateTranslation(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d rotateAffine(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d rotateAroundAffine(Quaterniondc var1, double var2, double var4, double var6, Matrix4d var8);

   Matrix4d rotateAround(Quaterniondc var1, double var2, double var4, double var6, Matrix4d var8);

   Matrix4d rotateLocal(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d rotateLocalX(double var1, Matrix4d var3);

   Matrix4d rotateLocalY(double var1, Matrix4d var3);

   Matrix4d rotateLocalZ(double var1, Matrix4d var3);

   Matrix4d rotateAroundLocal(Quaterniondc var1, double var2, double var4, double var6, Matrix4d var8);

   Matrix4d translate(Vector3dc var1, Matrix4d var2);

   Matrix4d translate(Vector3fc var1, Matrix4d var2);

   Matrix4d translate(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d translateLocal(Vector3fc var1, Matrix4d var2);

   Matrix4d translateLocal(Vector3dc var1, Matrix4d var2);

   Matrix4d translateLocal(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d rotateX(double var1, Matrix4d var3);

   Matrix4d rotateY(double var1, Matrix4d var3);

   Matrix4d rotateZ(double var1, Matrix4d var3);

   Matrix4d rotateTowardsXY(double var1, double var3, Matrix4d var5);

   Matrix4d rotateXYZ(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d rotateAffineXYZ(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d rotateZYX(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d rotateAffineZYX(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d rotateYXZ(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d rotateAffineYXZ(double var1, double var3, double var5, Matrix4d var7);

   Matrix4d rotate(Quaterniondc var1, Matrix4d var2);

   Matrix4d rotate(Quaternionfc var1, Matrix4d var2);

   Matrix4d rotateAffine(Quaterniondc var1, Matrix4d var2);

   Matrix4d rotateTranslation(Quaterniondc var1, Matrix4d var2);

   Matrix4d rotateTranslation(Quaternionfc var1, Matrix4d var2);

   Matrix4d rotateLocal(Quaterniondc var1, Matrix4d var2);

   Matrix4d rotateAffine(Quaternionfc var1, Matrix4d var2);

   Matrix4d rotateLocal(Quaternionfc var1, Matrix4d var2);

   Matrix4d rotate(AxisAngle4f var1, Matrix4d var2);

   Matrix4d rotate(AxisAngle4d var1, Matrix4d var2);

   Matrix4d rotate(double var1, Vector3dc var3, Matrix4d var4);

   Matrix4d rotate(double var1, Vector3fc var3, Matrix4d var4);

   Vector4d getRow(int var1, Vector4d var2) throws IndexOutOfBoundsException;

   Vector3d getRow(int var1, Vector3d var2) throws IndexOutOfBoundsException;

   Vector4d getColumn(int var1, Vector4d var2) throws IndexOutOfBoundsException;

   Vector3d getColumn(int var1, Vector3d var2) throws IndexOutOfBoundsException;

   double get(int var1, int var2);

   double getRowColumn(int var1, int var2);

   Matrix4d normal(Matrix4d var1);

   Matrix3d normal(Matrix3d var1);

   Matrix3d cofactor3x3(Matrix3d var1);

   Matrix4d cofactor3x3(Matrix4d var1);

   Matrix4d normalize3x3(Matrix4d var1);

   Matrix3d normalize3x3(Matrix3d var1);

   Vector4d unproject(double var1, double var3, double var5, int[] var7, Vector4d var8);

   Vector3d unproject(double var1, double var3, double var5, int[] var7, Vector3d var8);

   Vector4d unproject(Vector3dc var1, int[] var2, Vector4d var3);

   Vector3d unproject(Vector3dc var1, int[] var2, Vector3d var3);

   Matrix4d unprojectRay(double var1, double var3, int[] var5, Vector3d var6, Vector3d var7);

   Matrix4d unprojectRay(Vector2dc var1, int[] var2, Vector3d var3, Vector3d var4);

   Vector4d unprojectInv(Vector3dc var1, int[] var2, Vector4d var3);

   Vector4d unprojectInv(double var1, double var3, double var5, int[] var7, Vector4d var8);

   Vector3d unprojectInv(Vector3dc var1, int[] var2, Vector3d var3);

   Vector3d unprojectInv(double var1, double var3, double var5, int[] var7, Vector3d var8);

   Matrix4d unprojectInvRay(Vector2dc var1, int[] var2, Vector3d var3, Vector3d var4);

   Matrix4d unprojectInvRay(double var1, double var3, int[] var5, Vector3d var6, Vector3d var7);

   Vector4d project(double var1, double var3, double var5, int[] var7, Vector4d var8);

   Vector3d project(double var1, double var3, double var5, int[] var7, Vector3d var8);

   Vector4d project(Vector3dc var1, int[] var2, Vector4d var3);

   Vector3d project(Vector3dc var1, int[] var2, Vector3d var3);

   Matrix4d reflect(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d reflect(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Matrix4d reflect(Quaterniondc var1, Vector3dc var2, Matrix4d var3);

   Matrix4d reflect(Vector3dc var1, Vector3dc var2, Matrix4d var3);

   Matrix4d ortho(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13, Matrix4d var14);

   Matrix4d ortho(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Matrix4d orthoLH(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13, Matrix4d var14);

   Matrix4d orthoLH(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Matrix4d orthoSymmetric(double var1, double var3, double var5, double var7, boolean var9, Matrix4d var10);

   Matrix4d orthoSymmetric(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d orthoSymmetricLH(double var1, double var3, double var5, double var7, boolean var9, Matrix4d var10);

   Matrix4d orthoSymmetricLH(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d ortho2D(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d ortho2DLH(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d lookAlong(Vector3dc var1, Vector3dc var2, Matrix4d var3);

   Matrix4d lookAlong(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Matrix4d lookAt(Vector3dc var1, Vector3dc var2, Vector3dc var3, Matrix4d var4);

   Matrix4d lookAt(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4d var19);

   Matrix4d lookAtPerspective(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4d var19);

   Matrix4d lookAtLH(Vector3dc var1, Vector3dc var2, Vector3dc var3, Matrix4d var4);

   Matrix4d lookAtLH(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4d var19);

   Matrix4d lookAtPerspectiveLH(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, Matrix4d var19);

   Matrix4d perspective(double var1, double var3, double var5, double var7, boolean var9, Matrix4d var10);

   Matrix4d perspective(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d perspectiveRect(double var1, double var3, double var5, double var7, boolean var9, Matrix4d var10);

   Matrix4d perspectiveRect(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d perspectiveRect(double var1, double var3, double var5, double var7, boolean var9);

   Matrix4d perspectiveRect(double var1, double var3, double var5, double var7);

   Matrix4d perspectiveOffCenter(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13, Matrix4d var14);

   Matrix4d perspectiveOffCenter(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Matrix4d perspectiveOffCenter(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13);

   Matrix4d perspectiveOffCenter(double var1, double var3, double var5, double var7, double var9, double var11);

   Matrix4d perspectiveLH(double var1, double var3, double var5, double var7, boolean var9, Matrix4d var10);

   Matrix4d perspectiveLH(double var1, double var3, double var5, double var7, Matrix4d var9);

   Matrix4d frustum(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13, Matrix4d var14);

   Matrix4d frustum(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Matrix4d frustumLH(double var1, double var3, double var5, double var7, double var9, double var11, boolean var13, Matrix4d var14);

   Matrix4d frustumLH(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Vector4d frustumPlane(int var1, Vector4d var2);

   Vector3d frustumCorner(int var1, Vector3d var2);

   Vector3d perspectiveOrigin(Vector3d var1);

   Vector3d perspectiveInvOrigin(Vector3d var1);

   double perspectiveFov();

   double perspectiveNear();

   double perspectiveFar();

   Vector3d frustumRayDir(double var1, double var3, Vector3d var5);

   Vector3d positiveZ(Vector3d var1);

   Vector3d normalizedPositiveZ(Vector3d var1);

   Vector3d positiveX(Vector3d var1);

   Vector3d normalizedPositiveX(Vector3d var1);

   Vector3d positiveY(Vector3d var1);

   Vector3d normalizedPositiveY(Vector3d var1);

   Vector3d originAffine(Vector3d var1);

   Vector3d origin(Vector3d var1);

   Matrix4d shadow(Vector4dc var1, double var2, double var4, double var6, double var8, Matrix4d var10);

   Matrix4d shadow(double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, Matrix4d var17);

   Matrix4d shadow(Vector4dc var1, Matrix4dc var2, Matrix4d var3);

   Matrix4d shadow(double var1, double var3, double var5, double var7, Matrix4dc var9, Matrix4d var10);

   Matrix4d pick(double var1, double var3, double var5, double var7, int[] var9, Matrix4d var10);

   boolean isAffine();

   Matrix4d arcball(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Matrix4d arcball(double var1, Vector3dc var3, double var4, double var6, Matrix4d var8);

   Matrix4d projectedGridRange(Matrix4dc var1, double var2, double var4, Matrix4d var6);

   Matrix4d perspectiveFrustumSlice(double var1, double var3, Matrix4d var5);

   Matrix4d orthoCrop(Matrix4dc var1, Matrix4d var2);

   Matrix4d transformAab(double var1, double var3, double var5, double var7, double var9, double var11, Vector3d var13, Vector3d var14);

   Matrix4d transformAab(Vector3dc var1, Vector3dc var2, Vector3d var3, Vector3d var4);

   Matrix4d lerp(Matrix4dc var1, double var2, Matrix4d var4);

   Matrix4d rotateTowards(Vector3dc var1, Vector3dc var2, Matrix4d var3);

   Matrix4d rotateTowards(double var1, double var3, double var5, double var7, double var9, double var11, Matrix4d var13);

   Vector3d getEulerAnglesZYX(Vector3d var1);

   boolean testPoint(double var1, double var3, double var5);

   boolean testSphere(double var1, double var3, double var5, double var7);

   boolean testAab(double var1, double var3, double var5, double var7, double var9, double var11);

   Matrix4d obliqueZ(double var1, double var3, Matrix4d var5);

   Matrix4d withLookAtUp(Vector3dc var1, Matrix4d var2);

   Matrix4d withLookAtUp(double var1, double var3, double var5, Matrix4d var7);

   boolean equals(Matrix4dc var1, double var2);

   boolean isFinite();
}
