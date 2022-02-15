package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Vector2fc {
   float x();

   float y();

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   FloatBuffer get(FloatBuffer var1);

   FloatBuffer get(int var1, FloatBuffer var2);

   Vector2fc getToAddress(long var1);

   Vector2f sub(Vector2fc var1, Vector2f var2);

   Vector2f sub(float var1, float var2, Vector2f var3);

   float dot(Vector2fc var1);

   float angle(Vector2fc var1);

   float lengthSquared();

   float length();

   float distance(Vector2fc var1);

   float distanceSquared(Vector2fc var1);

   float distance(float var1, float var2);

   float distanceSquared(float var1, float var2);

   Vector2f normalize(Vector2f var1);

   Vector2f normalize(float var1, Vector2f var2);

   Vector2f add(Vector2fc var1, Vector2f var2);

   Vector2f add(float var1, float var2, Vector2f var3);

   Vector2f negate(Vector2f var1);

   Vector2f mul(float var1, Vector2f var2);

   Vector2f mul(float var1, float var2, Vector2f var3);

   Vector2f mul(Vector2fc var1, Vector2f var2);

   Vector2f div(float var1, Vector2f var2);

   Vector2f div(Vector2fc var1, Vector2f var2);

   Vector2f div(float var1, float var2, Vector2f var3);

   Vector2f mul(Matrix2fc var1, Vector2f var2);

   Vector2f mul(Matrix2dc var1, Vector2f var2);

   Vector2f mulTranspose(Matrix2fc var1, Vector2f var2);

   Vector2f mulPosition(Matrix3x2fc var1, Vector2f var2);

   Vector2f mulDirection(Matrix3x2fc var1, Vector2f var2);

   Vector2f lerp(Vector2fc var1, float var2, Vector2f var3);

   Vector2f fma(Vector2fc var1, Vector2fc var2, Vector2f var3);

   Vector2f fma(float var1, Vector2fc var2, Vector2f var3);

   Vector2f min(Vector2fc var1, Vector2f var2);

   Vector2f max(Vector2fc var1, Vector2f var2);

   int maxComponent();

   int minComponent();

   float get(int var1) throws IllegalArgumentException;

   Vector2i get(int var1, Vector2i var2);

   Vector2f get(Vector2f var1);

   Vector2d get(Vector2d var1);

   Vector2f floor(Vector2f var1);

   Vector2f ceil(Vector2f var1);

   Vector2f round(Vector2f var1);

   boolean isFinite();

   Vector2f absolute(Vector2f var1);

   boolean equals(Vector2fc var1, float var2);

   boolean equals(float var1, float var2);
}
