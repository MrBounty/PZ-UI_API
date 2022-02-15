package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public interface Matrix2dc {
   double m00();

   double m01();

   double m10();

   double m11();

   Matrix2d mul(Matrix2dc var1, Matrix2d var2);

   Matrix2d mul(Matrix2fc var1, Matrix2d var2);

   Matrix2d mulLocal(Matrix2dc var1, Matrix2d var2);

   double determinant();

   Matrix2d invert(Matrix2d var1);

   Matrix2d transpose(Matrix2d var1);

   Matrix2d get(Matrix2d var1);

   Matrix3x2d get(Matrix3x2d var1);

   Matrix3d get(Matrix3d var1);

   double getRotation();

   DoubleBuffer get(DoubleBuffer var1);

   DoubleBuffer get(int var1, DoubleBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   DoubleBuffer getTransposed(DoubleBuffer var1);

   DoubleBuffer getTransposed(int var1, DoubleBuffer var2);

   ByteBuffer getTransposed(ByteBuffer var1);

   ByteBuffer getTransposed(int var1, ByteBuffer var2);

   Matrix2dc getToAddress(long var1);

   double[] get(double[] var1, int var2);

   double[] get(double[] var1);

   Matrix2d scale(Vector2dc var1, Matrix2d var2);

   Matrix2d scale(double var1, double var3, Matrix2d var5);

   Matrix2d scale(double var1, Matrix2d var3);

   Matrix2d scaleLocal(double var1, double var3, Matrix2d var5);

   Vector2d transform(Vector2d var1);

   Vector2d transform(Vector2dc var1, Vector2d var2);

   Vector2d transform(double var1, double var3, Vector2d var5);

   Vector2d transformTranspose(Vector2d var1);

   Vector2d transformTranspose(Vector2dc var1, Vector2d var2);

   Vector2d transformTranspose(double var1, double var3, Vector2d var5);

   Matrix2d rotate(double var1, Matrix2d var3);

   Matrix2d rotateLocal(double var1, Matrix2d var3);

   Vector2d getRow(int var1, Vector2d var2) throws IndexOutOfBoundsException;

   Vector2d getColumn(int var1, Vector2d var2) throws IndexOutOfBoundsException;

   double get(int var1, int var2);

   Matrix2d normal(Matrix2d var1);

   Vector2d getScale(Vector2d var1);

   Vector2d positiveX(Vector2d var1);

   Vector2d normalizedPositiveX(Vector2d var1);

   Vector2d positiveY(Vector2d var1);

   Vector2d normalizedPositiveY(Vector2d var1);

   Matrix2d add(Matrix2dc var1, Matrix2d var2);

   Matrix2d sub(Matrix2dc var1, Matrix2d var2);

   Matrix2d mulComponentWise(Matrix2dc var1, Matrix2d var2);

   Matrix2d lerp(Matrix2dc var1, double var2, Matrix2d var4);

   boolean equals(Matrix2dc var1, double var2);

   boolean isFinite();
}
