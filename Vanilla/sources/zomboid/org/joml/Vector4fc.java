package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Vector4fc {
   float x();

   float y();

   float z();

   float w();

   FloatBuffer get(FloatBuffer var1);

   FloatBuffer get(int var1, FloatBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   Vector4fc getToAddress(long var1);

   Vector4f sub(Vector4fc var1, Vector4f var2);

   Vector4f sub(float var1, float var2, float var3, float var4, Vector4f var5);

   Vector4f add(Vector4fc var1, Vector4f var2);

   Vector4f add(float var1, float var2, float var3, float var4, Vector4f var5);

   Vector4f fma(Vector4fc var1, Vector4fc var2, Vector4f var3);

   Vector4f fma(float var1, Vector4fc var2, Vector4f var3);

   Vector4f mulAdd(Vector4fc var1, Vector4fc var2, Vector4f var3);

   Vector4f mulAdd(float var1, Vector4fc var2, Vector4f var3);

   Vector4f mul(Vector4fc var1, Vector4f var2);

   Vector4f div(Vector4fc var1, Vector4f var2);

   Vector4f mul(Matrix4fc var1, Vector4f var2);

   Vector4f mulTranspose(Matrix4fc var1, Vector4f var2);

   Vector4f mulAffine(Matrix4fc var1, Vector4f var2);

   Vector4f mulAffineTranspose(Matrix4fc var1, Vector4f var2);

   Vector4f mul(Matrix4x3fc var1, Vector4f var2);

   Vector4f mulProject(Matrix4fc var1, Vector4f var2);

   Vector3f mulProject(Matrix4fc var1, Vector3f var2);

   Vector4f mul(float var1, Vector4f var2);

   Vector4f mul(float var1, float var2, float var3, float var4, Vector4f var5);

   Vector4f div(float var1, Vector4f var2);

   Vector4f div(float var1, float var2, float var3, float var4, Vector4f var5);

   Vector4f rotate(Quaternionfc var1, Vector4f var2);

   Vector4f rotateAxis(float var1, float var2, float var3, float var4, Vector4f var5);

   Vector4f rotateX(float var1, Vector4f var2);

   Vector4f rotateY(float var1, Vector4f var2);

   Vector4f rotateZ(float var1, Vector4f var2);

   float lengthSquared();

   float length();

   Vector4f normalize(Vector4f var1);

   Vector4f normalize(float var1, Vector4f var2);

   Vector4f normalize3(Vector4f var1);

   float distance(Vector4fc var1);

   float distance(float var1, float var2, float var3, float var4);

   float distanceSquared(Vector4fc var1);

   float distanceSquared(float var1, float var2, float var3, float var4);

   float dot(Vector4fc var1);

   float dot(float var1, float var2, float var3, float var4);

   float angleCos(Vector4fc var1);

   float angle(Vector4fc var1);

   Vector4f negate(Vector4f var1);

   Vector4f min(Vector4fc var1, Vector4f var2);

   Vector4f max(Vector4fc var1, Vector4f var2);

   Vector4f lerp(Vector4fc var1, float var2, Vector4f var3);

   Vector4f smoothStep(Vector4fc var1, float var2, Vector4f var3);

   Vector4f hermite(Vector4fc var1, Vector4fc var2, Vector4fc var3, float var4, Vector4f var5);

   float get(int var1) throws IllegalArgumentException;

   Vector4i get(int var1, Vector4i var2);

   Vector4f get(Vector4f var1);

   Vector4d get(Vector4d var1);

   int maxComponent();

   int minComponent();

   Vector4f floor(Vector4f var1);

   Vector4f ceil(Vector4f var1);

   Vector4f round(Vector4f var1);

   boolean isFinite();

   Vector4f absolute(Vector4f var1);

   boolean equals(Vector4fc var1, float var2);

   boolean equals(float var1, float var2, float var3, float var4);
}
