package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public interface Vector2dc {
   double x();

   double y();

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   DoubleBuffer get(DoubleBuffer var1);

   DoubleBuffer get(int var1, DoubleBuffer var2);

   Vector2dc getToAddress(long var1);

   Vector2d sub(double var1, double var3, Vector2d var5);

   Vector2d sub(Vector2dc var1, Vector2d var2);

   Vector2d sub(Vector2fc var1, Vector2d var2);

   Vector2d mul(double var1, Vector2d var3);

   Vector2d mul(double var1, double var3, Vector2d var5);

   Vector2d mul(Vector2dc var1, Vector2d var2);

   Vector2d div(double var1, Vector2d var3);

   Vector2d div(double var1, double var3, Vector2d var5);

   Vector2d div(Vector2fc var1, Vector2d var2);

   Vector2d div(Vector2dc var1, Vector2d var2);

   Vector2d mul(Matrix2dc var1, Vector2d var2);

   Vector2d mul(Matrix2fc var1, Vector2d var2);

   Vector2d mulTranspose(Matrix2dc var1, Vector2d var2);

   Vector2d mulTranspose(Matrix2fc var1, Vector2d var2);

   Vector2d mulPosition(Matrix3x2dc var1, Vector2d var2);

   Vector2d mulDirection(Matrix3x2dc var1, Vector2d var2);

   double dot(Vector2dc var1);

   double angle(Vector2dc var1);

   double lengthSquared();

   double length();

   double distance(Vector2dc var1);

   double distanceSquared(Vector2dc var1);

   double distance(Vector2fc var1);

   double distanceSquared(Vector2fc var1);

   double distance(double var1, double var3);

   double distanceSquared(double var1, double var3);

   Vector2d normalize(Vector2d var1);

   Vector2d normalize(double var1, Vector2d var3);

   Vector2d add(double var1, double var3, Vector2d var5);

   Vector2d add(Vector2dc var1, Vector2d var2);

   Vector2d add(Vector2fc var1, Vector2d var2);

   Vector2d negate(Vector2d var1);

   Vector2d lerp(Vector2dc var1, double var2, Vector2d var4);

   Vector2d fma(Vector2dc var1, Vector2dc var2, Vector2d var3);

   Vector2d fma(double var1, Vector2dc var3, Vector2d var4);

   Vector2d min(Vector2dc var1, Vector2d var2);

   Vector2d max(Vector2dc var1, Vector2d var2);

   int maxComponent();

   int minComponent();

   double get(int var1) throws IllegalArgumentException;

   Vector2i get(int var1, Vector2i var2);

   Vector2f get(Vector2f var1);

   Vector2d get(Vector2d var1);

   Vector2d floor(Vector2d var1);

   Vector2d ceil(Vector2d var1);

   Vector2d round(Vector2d var1);

   boolean isFinite();

   Vector2d absolute(Vector2d var1);

   boolean equals(Vector2dc var1, double var2);

   boolean equals(double var1, double var3);
}
