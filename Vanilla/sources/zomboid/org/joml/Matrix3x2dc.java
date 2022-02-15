package org.joml;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

public interface Matrix3x2dc {
   double m00();

   double m01();

   double m10();

   double m11();

   double m20();

   double m21();

   Matrix3x2d mul(Matrix3x2dc var1, Matrix3x2d var2);

   Matrix3x2d mulLocal(Matrix3x2dc var1, Matrix3x2d var2);

   double determinant();

   Matrix3x2d invert(Matrix3x2d var1);

   Matrix3x2d translate(double var1, double var3, Matrix3x2d var5);

   Matrix3x2d translate(Vector2dc var1, Matrix3x2d var2);

   Matrix3x2d translateLocal(Vector2dc var1, Matrix3x2d var2);

   Matrix3x2d translateLocal(double var1, double var3, Matrix3x2d var5);

   Matrix3x2d get(Matrix3x2d var1);

   DoubleBuffer get(DoubleBuffer var1);

   DoubleBuffer get(int var1, DoubleBuffer var2);

   ByteBuffer get(ByteBuffer var1);

   ByteBuffer get(int var1, ByteBuffer var2);

   DoubleBuffer get3x3(DoubleBuffer var1);

   DoubleBuffer get3x3(int var1, DoubleBuffer var2);

   ByteBuffer get3x3(ByteBuffer var1);

   ByteBuffer get3x3(int var1, ByteBuffer var2);

   DoubleBuffer get4x4(DoubleBuffer var1);

   DoubleBuffer get4x4(int var1, DoubleBuffer var2);

   ByteBuffer get4x4(ByteBuffer var1);

   ByteBuffer get4x4(int var1, ByteBuffer var2);

   Matrix3x2dc getToAddress(long var1);

   double[] get(double[] var1, int var2);

   double[] get(double[] var1);

   double[] get3x3(double[] var1, int var2);

   double[] get3x3(double[] var1);

   double[] get4x4(double[] var1, int var2);

   double[] get4x4(double[] var1);

   Matrix3x2d scale(double var1, double var3, Matrix3x2d var5);

   Matrix3x2d scale(Vector2dc var1, Matrix3x2d var2);

   Matrix3x2d scale(Vector2fc var1, Matrix3x2d var2);

   Matrix3x2d scaleLocal(double var1, Matrix3x2d var3);

   Matrix3x2d scaleLocal(double var1, double var3, Matrix3x2d var5);

   Matrix3x2d scaleAroundLocal(double var1, double var3, double var5, double var7, Matrix3x2d var9);

   Matrix3x2d scaleAroundLocal(double var1, double var3, double var5, Matrix3x2d var7);

   Matrix3x2d scale(double var1, Matrix3x2d var3);

   Matrix3x2d scaleAround(double var1, double var3, double var5, double var7, Matrix3x2d var9);

   Matrix3x2d scaleAround(double var1, double var3, double var5, Matrix3x2d var7);

   Vector3d transform(Vector3d var1);

   Vector3d transform(Vector3dc var1, Vector3d var2);

   Vector3d transform(double var1, double var3, double var5, Vector3d var7);

   Vector2d transformPosition(Vector2d var1);

   Vector2d transformPosition(Vector2dc var1, Vector2d var2);

   Vector2d transformPosition(double var1, double var3, Vector2d var5);

   Vector2d transformDirection(Vector2d var1);

   Vector2d transformDirection(Vector2dc var1, Vector2d var2);

   Vector2d transformDirection(double var1, double var3, Vector2d var5);

   Matrix3x2d rotate(double var1, Matrix3x2d var3);

   Matrix3x2d rotateLocal(double var1, Matrix3x2d var3);

   Matrix3x2d rotateAbout(double var1, double var3, double var5, Matrix3x2d var7);

   Matrix3x2d rotateTo(Vector2dc var1, Vector2dc var2, Matrix3x2d var3);

   Matrix3x2d view(double var1, double var3, double var5, double var7, Matrix3x2d var9);

   Vector2d origin(Vector2d var1);

   double[] viewArea(double[] var1);

   Vector2d positiveX(Vector2d var1);

   Vector2d normalizedPositiveX(Vector2d var1);

   Vector2d positiveY(Vector2d var1);

   Vector2d normalizedPositiveY(Vector2d var1);

   Vector2d unproject(double var1, double var3, int[] var5, Vector2d var6);

   Vector2d unprojectInv(double var1, double var3, int[] var5, Vector2d var6);

   boolean testPoint(double var1, double var3);

   boolean testCircle(double var1, double var3, double var5);

   boolean testAar(double var1, double var3, double var5, double var7);

   boolean equals(Matrix3x2dc var1, double var2);

   boolean isFinite();
}
