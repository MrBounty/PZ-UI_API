package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Matrix3x2fc {
   float m00();

   float m01();

   float m10();

   float m11();

   float m20();

   float m21();

   Matrix3x2f mul(Matrix3x2fc var1, Matrix3x2f var2);

   Matrix3x2f mulLocal(Matrix3x2fc var1, Matrix3x2f var2);

   float determinant();

   Matrix3x2f invert(Matrix3x2f var1);

   Matrix3x2f translate(float var1, float var2, Matrix3x2f var3);

   Matrix3x2f translate(Vector2fc var1, Matrix3x2f var2);

   Matrix3x2f translateLocal(Vector2fc var1, Matrix3x2f var2);

   Matrix3x2f translateLocal(float var1, float var2, Matrix3x2f var3);

   Matrix3x2f get(Matrix3x2f var1);

   FloatBuffer get(FloatBuffer var1);

   FloatBuffer get(int var1, FloatBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   FloatBuffer get3x3(FloatBuffer var1);

   FloatBuffer get3x3(int var1, FloatBuffer var2);

   ByteBuffer get3x3(ByteBuffer var1);

   ByteBuffer get3x3(int var1, ByteBuffer var2);

   FloatBuffer get4x4(FloatBuffer var1);

   FloatBuffer get4x4(int var1, FloatBuffer var2);

   ByteBuffer get4x4(ByteBuffer var1);

   ByteBuffer get4x4(int var1, ByteBuffer var2);

   Matrix3x2fc getToAddress(long var1);

   float[] get(float[] var1, int var2);

   float[] get(float[] var1);

   float[] get3x3(float[] var1, int var2);

   float[] get3x3(float[] var1);

   float[] get4x4(float[] var1, int var2);

   float[] get4x4(float[] var1);

   Matrix3x2f scale(float var1, float var2, Matrix3x2f var3);

   Matrix3x2f scale(Vector2fc var1, Matrix3x2f var2);

   Matrix3x2f scaleAroundLocal(float var1, float var2, float var3, float var4, Matrix3x2f var5);

   Matrix3x2f scaleAroundLocal(float var1, float var2, float var3, Matrix3x2f var4);

   Matrix3x2f scale(float var1, Matrix3x2f var2);

   Matrix3x2f scaleLocal(float var1, Matrix3x2f var2);

   Matrix3x2f scaleLocal(float var1, float var2, Matrix3x2f var3);

   Matrix3x2f scaleAround(float var1, float var2, float var3, float var4, Matrix3x2f var5);

   Matrix3x2f scaleAround(float var1, float var2, float var3, Matrix3x2f var4);

   Vector3f transform(Vector3f var1);

   Vector3f transform(Vector3f var1, Vector3f var2);

   Vector3f transform(float var1, float var2, float var3, Vector3f var4);

   Vector2f transformPosition(Vector2f var1);

   Vector2f transformPosition(Vector2fc var1, Vector2f var2);

   Vector2f transformPosition(float var1, float var2, Vector2f var3);

   Vector2f transformDirection(Vector2f var1);

   Vector2f transformDirection(Vector2fc var1, Vector2f var2);

   Vector2f transformDirection(float var1, float var2, Vector2f var3);

   Matrix3x2f rotate(float var1, Matrix3x2f var2);

   Matrix3x2f rotateLocal(float var1, Matrix3x2f var2);

   Matrix3x2f rotateAbout(float var1, float var2, float var3, Matrix3x2f var4);

   Matrix3x2f rotateTo(Vector2fc var1, Vector2fc var2, Matrix3x2f var3);

   Matrix3x2f view(float var1, float var2, float var3, float var4, Matrix3x2f var5);

   Vector2f origin(Vector2f var1);

   float[] viewArea(float[] var1);

   Vector2f positiveX(Vector2f var1);

   Vector2f normalizedPositiveX(Vector2f var1);

   Vector2f positiveY(Vector2f var1);

   Vector2f normalizedPositiveY(Vector2f var1);

   Vector2f unproject(float var1, float var2, int[] var3, Vector2f var4);

   Vector2f unprojectInv(float var1, float var2, int[] var3, Vector2f var4);

   boolean testPoint(float var1, float var2);

   boolean testCircle(float var1, float var2, float var3);

   boolean testAar(float var1, float var2, float var3, float var4);

   boolean equals(Matrix3x2fc var1, float var2);

   boolean isFinite();
}
