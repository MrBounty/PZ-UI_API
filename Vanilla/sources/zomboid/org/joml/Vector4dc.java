package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public interface Vector4dc {
   double x();

   double y();

   double z();

   double w();

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   DoubleBuffer get(DoubleBuffer var1);

   DoubleBuffer get(int var1, DoubleBuffer var2);

   FloatBuffer get(FloatBuffer var1);

   FloatBuffer get(int var1, FloatBuffer var2);

   ByteBuffer getf(ByteBuffer var1);

   ByteBuffer getf(int var1, ByteBuffer var2);

   Vector4dc getToAddress(long var1);

   Vector4d sub(Vector4dc var1, Vector4d var2);

   Vector4d sub(Vector4fc var1, Vector4d var2);

   Vector4d sub(double var1, double var3, double var5, double var7, Vector4d var9);

   Vector4d add(Vector4dc var1, Vector4d var2);

   Vector4d add(Vector4fc var1, Vector4d var2);

   Vector4d add(double var1, double var3, double var5, double var7, Vector4d var9);

   Vector4d fma(Vector4dc var1, Vector4dc var2, Vector4d var3);

   Vector4d fma(double var1, Vector4dc var3, Vector4d var4);

   Vector4d mul(Vector4dc var1, Vector4d var2);

   Vector4d mul(Vector4fc var1, Vector4d var2);

   Vector4d div(Vector4dc var1, Vector4d var2);

   Vector4d mul(Matrix4dc var1, Vector4d var2);

   Vector4d mul(Matrix4x3dc var1, Vector4d var2);

   Vector4d mul(Matrix4x3fc var1, Vector4d var2);

   Vector4d mul(Matrix4fc var1, Vector4d var2);

   Vector4d mulTranspose(Matrix4dc var1, Vector4d var2);

   Vector4d mulAffine(Matrix4dc var1, Vector4d var2);

   Vector4d mulAffineTranspose(Matrix4dc var1, Vector4d var2);

   Vector4d mulProject(Matrix4dc var1, Vector4d var2);

   Vector3d mulProject(Matrix4dc var1, Vector3d var2);

   Vector4d mulAdd(Vector4dc var1, Vector4dc var2, Vector4d var3);

   Vector4d mulAdd(double var1, Vector4dc var3, Vector4d var4);

   Vector4d mul(double var1, Vector4d var3);

   Vector4d div(double var1, Vector4d var3);

   Vector4d rotate(Quaterniondc var1, Vector4d var2);

   Vector4d rotateAxis(double var1, double var3, double var5, double var7, Vector4d var9);

   Vector4d rotateX(double var1, Vector4d var3);

   Vector4d rotateY(double var1, Vector4d var3);

   Vector4d rotateZ(double var1, Vector4d var3);

   double lengthSquared();

   double length();

   Vector4d normalize(Vector4d var1);

   Vector4d normalize(double var1, Vector4d var3);

   Vector4d normalize3(Vector4d var1);

   double distance(Vector4dc var1);

   double distance(double var1, double var3, double var5, double var7);

   double distanceSquared(Vector4dc var1);

   double distanceSquared(double var1, double var3, double var5, double var7);

   double dot(Vector4dc var1);

   double dot(double var1, double var3, double var5, double var7);

   double angleCos(Vector4dc var1);

   double angle(Vector4dc var1);

   Vector4d negate(Vector4d var1);

   Vector4d min(Vector4dc var1, Vector4d var2);

   Vector4d max(Vector4dc var1, Vector4d var2);

   Vector4d smoothStep(Vector4dc var1, double var2, Vector4d var4);

   Vector4d hermite(Vector4dc var1, Vector4dc var2, Vector4dc var3, double var4, Vector4d var6);

   Vector4d lerp(Vector4dc var1, double var2, Vector4d var4);

   double get(int var1) throws IllegalArgumentException;

   Vector4i get(int var1, Vector4i var2);

   Vector4f get(Vector4f var1);

   Vector4d get(Vector4d var1);

   int maxComponent();

   int minComponent();

   Vector4d floor(Vector4d var1);

   Vector4d ceil(Vector4d var1);

   Vector4d round(Vector4d var1);

   boolean isFinite();

   Vector4d absolute(Vector4d var1);

   boolean equals(Vector4dc var1, double var2);

   boolean equals(double var1, double var3, double var5, double var7);
}
