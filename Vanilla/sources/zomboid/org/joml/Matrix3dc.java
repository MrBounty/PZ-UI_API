package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public interface Matrix3dc {
   double m00();

   double m01();

   double m02();

   double m10();

   double m11();

   double m12();

   double m20();

   double m21();

   double m22();

   Matrix3d mul(Matrix3dc var1, Matrix3d var2);

   Matrix3d mulLocal(Matrix3dc var1, Matrix3d var2);

   Matrix3d mul(Matrix3fc var1, Matrix3d var2);

   double determinant();

   Matrix3d invert(Matrix3d var1);

   Matrix3d transpose(Matrix3d var1);

   Matrix3d get(Matrix3d var1);

   AxisAngle4f getRotation(AxisAngle4f var1);

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

   Matrix3dc getToAddress(long var1);

   double[] get(double[] var1, int var2);

   double[] get(double[] var1);

   float[] get(float[] var1, int var2);

   float[] get(float[] var1);

   Matrix3d scale(Vector3dc var1, Matrix3d var2);

   Matrix3d scale(double var1, double var3, double var5, Matrix3d var7);

   Matrix3d scale(double var1, Matrix3d var3);

   Matrix3d scaleLocal(double var1, double var3, double var5, Matrix3d var7);

   Vector3d transform(Vector3d var1);

   Vector3d transform(Vector3dc var1, Vector3d var2);

   Vector3f transform(Vector3f var1);

   Vector3f transform(Vector3fc var1, Vector3f var2);

   Vector3d transform(double var1, double var3, double var5, Vector3d var7);

   Vector3d transformTranspose(Vector3d var1);

   Vector3d transformTranspose(Vector3dc var1, Vector3d var2);

   Vector3d transformTranspose(double var1, double var3, double var5, Vector3d var7);

   Matrix3d rotateX(double var1, Matrix3d var3);

   Matrix3d rotateY(double var1, Matrix3d var3);

   Matrix3d rotateZ(double var1, Matrix3d var3);

   Matrix3d rotateXYZ(double var1, double var3, double var5, Matrix3d var7);

   Matrix3d rotateZYX(double var1, double var3, double var5, Matrix3d var7);

   Matrix3d rotateYXZ(double var1, double var3, double var5, Matrix3d var7);

   Matrix3d rotate(double var1, double var3, double var5, double var7, Matrix3d var9);

   Matrix3d rotateLocal(double var1, double var3, double var5, double var7, Matrix3d var9);

   Matrix3d rotateLocalX(double var1, Matrix3d var3);

   Matrix3d rotateLocalY(double var1, Matrix3d var3);

   Matrix3d rotateLocalZ(double var1, Matrix3d var3);

   Matrix3d rotateLocal(Quaterniondc var1, Matrix3d var2);

   Matrix3d rotateLocal(Quaternionfc var1, Matrix3d var2);

   Matrix3d rotate(Quaterniondc var1, Matrix3d var2);

   Matrix3d rotate(Quaternionfc var1, Matrix3d var2);

   Matrix3d rotate(AxisAngle4f var1, Matrix3d var2);

   Matrix3d rotate(AxisAngle4d var1, Matrix3d var2);

   Matrix3d rotate(double var1, Vector3dc var3, Matrix3d var4);

   Matrix3d rotate(double var1, Vector3fc var3, Matrix3d var4);

   Vector3d getRow(int var1, Vector3d var2) throws IndexOutOfBoundsException;

   Vector3d getColumn(int var1, Vector3d var2) throws IndexOutOfBoundsException;

   double get(int var1, int var2);

   double getRowColumn(int var1, int var2);

   Matrix3d normal(Matrix3d var1);

   Matrix3d cofactor(Matrix3d var1);

   Matrix3d lookAlong(Vector3dc var1, Vector3dc var2, Matrix3d var3);

   Matrix3d lookAlong(double var1, double var3, double var5, double var7, double var9, double var11, Matrix3d var13);

   Vector3d getScale(Vector3d var1);

   Vector3d positiveZ(Vector3d var1);

   Vector3d normalizedPositiveZ(Vector3d var1);

   Vector3d positiveX(Vector3d var1);

   Vector3d normalizedPositiveX(Vector3d var1);

   Vector3d positiveY(Vector3d var1);

   Vector3d normalizedPositiveY(Vector3d var1);

   Matrix3d add(Matrix3dc var1, Matrix3d var2);

   Matrix3d sub(Matrix3dc var1, Matrix3d var2);

   Matrix3d mulComponentWise(Matrix3dc var1, Matrix3d var2);

   Matrix3d lerp(Matrix3dc var1, double var2, Matrix3d var4);

   Matrix3d rotateTowards(Vector3dc var1, Vector3dc var2, Matrix3d var3);

   Matrix3d rotateTowards(double var1, double var3, double var5, double var7, double var9, double var11, Matrix3d var13);

   Vector3d getEulerAnglesZYX(Vector3d var1);

   Matrix3d obliqueZ(double var1, double var3, Matrix3d var5);

   boolean equals(Matrix3dc var1, double var2);

   Matrix3d reflect(double var1, double var3, double var5, Matrix3d var7);

   Matrix3d reflect(Quaterniondc var1, Matrix3d var2);

   Matrix3d reflect(Vector3dc var1, Matrix3d var2);

   boolean isFinite();

   double quadraticFormProduct(double var1, double var3, double var5);

   double quadraticFormProduct(Vector3dc var1);

   double quadraticFormProduct(Vector3fc var1);
}
