package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

public interface Vector3dc {
   double x();

   double y();

   double z();

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   DoubleBuffer get(DoubleBuffer var1);

   DoubleBuffer get(int var1, DoubleBuffer var2);

   FloatBuffer get(FloatBuffer var1);

   FloatBuffer get(int var1, FloatBuffer var2);

   ByteBuffer getf(ByteBuffer var1);

   ByteBuffer getf(int var1, ByteBuffer var2);

   Vector3dc getToAddress(long var1);

   Vector3d sub(Vector3dc var1, Vector3d var2);

   Vector3d sub(Vector3fc var1, Vector3d var2);

   Vector3d sub(double var1, double var3, double var5, Vector3d var7);

   Vector3d add(Vector3dc var1, Vector3d var2);

   Vector3d add(Vector3fc var1, Vector3d var2);

   Vector3d add(double var1, double var3, double var5, Vector3d var7);

   Vector3d fma(Vector3dc var1, Vector3dc var2, Vector3d var3);

   Vector3d fma(double var1, Vector3dc var3, Vector3d var4);

   Vector3d fma(Vector3dc var1, Vector3fc var2, Vector3d var3);

   Vector3d fma(Vector3fc var1, Vector3fc var2, Vector3d var3);

   Vector3d fma(double var1, Vector3fc var3, Vector3d var4);

   Vector3d mulAdd(Vector3dc var1, Vector3dc var2, Vector3d var3);

   Vector3d mulAdd(double var1, Vector3dc var3, Vector3d var4);

   Vector3d mulAdd(Vector3fc var1, Vector3dc var2, Vector3d var3);

   Vector3d mul(Vector3fc var1, Vector3d var2);

   Vector3d mul(Vector3dc var1, Vector3d var2);

   Vector3d div(Vector3fc var1, Vector3d var2);

   Vector3d div(Vector3dc var1, Vector3d var2);

   Vector3d mulProject(Matrix4dc var1, double var2, Vector3d var4);

   Vector3d mulProject(Matrix4dc var1, Vector3d var2);

   Vector3d mulProject(Matrix4fc var1, Vector3d var2);

   Vector3d mul(Matrix3dc var1, Vector3d var2);

   Vector3f mul(Matrix3dc var1, Vector3f var2);

   Vector3d mul(Matrix3fc var1, Vector3d var2);

   Vector3d mul(Matrix3x2dc var1, Vector3d var2);

   Vector3d mul(Matrix3x2fc var1, Vector3d var2);

   Vector3d mulTranspose(Matrix3dc var1, Vector3d var2);

   Vector3d mulTranspose(Matrix3fc var1, Vector3d var2);

   Vector3d mulPosition(Matrix4dc var1, Vector3d var2);

   Vector3d mulPosition(Matrix4fc var1, Vector3d var2);

   Vector3d mulPosition(Matrix4x3dc var1, Vector3d var2);

   Vector3d mulPosition(Matrix4x3fc var1, Vector3d var2);

   Vector3d mulTransposePosition(Matrix4dc var1, Vector3d var2);

   Vector3d mulTransposePosition(Matrix4fc var1, Vector3d var2);

   double mulPositionW(Matrix4fc var1, Vector3d var2);

   double mulPositionW(Matrix4dc var1, Vector3d var2);

   Vector3d mulDirection(Matrix4dc var1, Vector3d var2);

   Vector3d mulDirection(Matrix4fc var1, Vector3d var2);

   Vector3d mulDirection(Matrix4x3dc var1, Vector3d var2);

   Vector3d mulDirection(Matrix4x3fc var1, Vector3d var2);

   Vector3d mulTransposeDirection(Matrix4dc var1, Vector3d var2);

   Vector3d mulTransposeDirection(Matrix4fc var1, Vector3d var2);

   Vector3d mul(double var1, Vector3d var3);

   Vector3d mul(double var1, double var3, double var5, Vector3d var7);

   Vector3d rotate(Quaterniondc var1, Vector3d var2);

   Quaterniond rotationTo(Vector3dc var1, Quaterniond var2);

   Quaterniond rotationTo(double var1, double var3, double var5, Quaterniond var7);

   Vector3d rotateAxis(double var1, double var3, double var5, double var7, Vector3d var9);

   Vector3d rotateX(double var1, Vector3d var3);

   Vector3d rotateY(double var1, Vector3d var3);

   Vector3d rotateZ(double var1, Vector3d var3);

   Vector3d div(double var1, Vector3d var3);

   Vector3d div(double var1, double var3, double var5, Vector3d var7);

   double lengthSquared();

   double length();

   Vector3d normalize(Vector3d var1);

   Vector3d normalize(double var1, Vector3d var3);

   Vector3d cross(Vector3dc var1, Vector3d var2);

   Vector3d cross(double var1, double var3, double var5, Vector3d var7);

   double distance(Vector3dc var1);

   double distance(double var1, double var3, double var5);

   double distanceSquared(Vector3dc var1);

   double distanceSquared(double var1, double var3, double var5);

   double dot(Vector3dc var1);

   double dot(double var1, double var3, double var5);

   double angleCos(Vector3dc var1);

   double angle(Vector3dc var1);

   double angleSigned(Vector3dc var1, Vector3dc var2);

   double angleSigned(double var1, double var3, double var5, double var7, double var9, double var11);

   Vector3d min(Vector3dc var1, Vector3d var2);

   Vector3d max(Vector3dc var1, Vector3d var2);

   Vector3d negate(Vector3d var1);

   Vector3d absolute(Vector3d var1);

   Vector3d reflect(Vector3dc var1, Vector3d var2);

   Vector3d reflect(double var1, double var3, double var5, Vector3d var7);

   Vector3d half(Vector3dc var1, Vector3d var2);

   Vector3d half(double var1, double var3, double var5, Vector3d var7);

   Vector3d smoothStep(Vector3dc var1, double var2, Vector3d var4);

   Vector3d hermite(Vector3dc var1, Vector3dc var2, Vector3dc var3, double var4, Vector3d var6);

   Vector3d lerp(Vector3dc var1, double var2, Vector3d var4);

   double get(int var1) throws IllegalArgumentException;

   Vector3i get(int var1, Vector3i var2);

   Vector3f get(Vector3f var1);

   Vector3d get(Vector3d var1);

   int maxComponent();

   int minComponent();

   Vector3d orthogonalize(Vector3dc var1, Vector3d var2);

   Vector3d orthogonalizeUnit(Vector3dc var1, Vector3d var2);

   Vector3d floor(Vector3d var1);

   Vector3d ceil(Vector3d var1);

   Vector3d round(Vector3d var1);

   boolean isFinite();

   boolean equals(Vector3dc var1, double var2);

   boolean equals(double var1, double var3, double var5);
}
