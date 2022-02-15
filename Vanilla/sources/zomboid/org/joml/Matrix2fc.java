package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix2fc {
   float m00();

   float m01();

   float m10();

   float m11();

   Matrix2f mul(Matrix2fc var1, Matrix2f var2);

   Matrix2f mulLocal(Matrix2fc var1, Matrix2f var2);

   float determinant();

   Matrix2f invert(Matrix2f var1);

   Matrix2f transpose(Matrix2f var1);

   Matrix2f get(Matrix2f var1);

   Matrix3x2f get(Matrix3x2f var1);

   Matrix3f get(Matrix3f var1);

   float getRotation();

   FloatBuffer get(FloatBuffer var1);

   FloatBuffer get(int var1, FloatBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   FloatBuffer getTransposed(FloatBuffer var1);

   FloatBuffer getTransposed(int var1, FloatBuffer var2);

   ByteBuffer getTransposed(ByteBuffer var1);

   ByteBuffer getTransposed(int var1, ByteBuffer var2);

   Matrix2fc getToAddress(long var1);

   float[] get(float[] var1, int var2);

   float[] get(float[] var1);

   Matrix2f scale(Vector2fc var1, Matrix2f var2);

   Matrix2f scale(float var1, float var2, Matrix2f var3);

   Matrix2f scale(float var1, Matrix2f var2);

   Matrix2f scaleLocal(float var1, float var2, Matrix2f var3);

   Vector2f transform(Vector2f var1);

   Vector2f transform(Vector2fc var1, Vector2f var2);

   Vector2f transform(float var1, float var2, Vector2f var3);

   Vector2f transformTranspose(Vector2f var1);

   Vector2f transformTranspose(Vector2fc var1, Vector2f var2);

   Vector2f transformTranspose(float var1, float var2, Vector2f var3);

   Matrix2f rotate(float var1, Matrix2f var2);

   Matrix2f rotateLocal(float var1, Matrix2f var2);

   Vector2f getRow(int var1, Vector2f var2) throws IndexOutOfBoundsException;

   Vector2f getColumn(int var1, Vector2f var2) throws IndexOutOfBoundsException;

   float get(int var1, int var2);

   Matrix2f normal(Matrix2f var1);

   Vector2f getScale(Vector2f var1);

   Vector2f positiveX(Vector2f var1);

   Vector2f normalizedPositiveX(Vector2f var1);

   Vector2f positiveY(Vector2f var1);

   Vector2f normalizedPositiveY(Vector2f var1);

   Matrix2f add(Matrix2fc var1, Matrix2f var2);

   Matrix2f sub(Matrix2fc var1, Matrix2f var2);

   Matrix2f mulComponentWise(Matrix2fc var1, Matrix2f var2);

   Matrix2f lerp(Matrix2fc var1, float var2, Matrix2f var3);

   boolean equals(Matrix2fc var1, float var2);

   boolean isFinite();
}
