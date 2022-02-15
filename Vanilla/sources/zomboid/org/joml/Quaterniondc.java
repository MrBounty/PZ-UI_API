package org.joml;

public interface Quaterniondc {
   double x();

   double y();

   double z();

   double w();

   Quaterniond normalize(Quaterniond var1);

   Quaterniond add(double var1, double var3, double var5, double var7, Quaterniond var9);

   Quaterniond add(Quaterniondc var1, Quaterniond var2);

   double dot(Quaterniondc var1);

   double angle();

   Matrix3d get(Matrix3d var1);

   Matrix3f get(Matrix3f var1);

   Matrix4d get(Matrix4d var1);

   Matrix4f get(Matrix4f var1);

   AxisAngle4f get(AxisAngle4f var1);

   AxisAngle4d get(AxisAngle4d var1);

   Quaterniond get(Quaterniond var1);

   Quaternionf get(Quaternionf var1);

   Quaterniond mul(Quaterniondc var1, Quaterniond var2);

   Quaterniond mul(double var1, double var3, double var5, double var7, Quaterniond var9);

   Quaterniond premul(Quaterniondc var1, Quaterniond var2);

   Quaterniond premul(double var1, double var3, double var5, double var7, Quaterniond var9);

   Vector3d transform(Vector3d var1);

   Vector3d transformInverse(Vector3d var1);

   Vector3d transformUnit(Vector3d var1);

   Vector3d transformInverseUnit(Vector3d var1);

   Vector3d transformPositiveX(Vector3d var1);

   Vector4d transformPositiveX(Vector4d var1);

   Vector3d transformUnitPositiveX(Vector3d var1);

   Vector4d transformUnitPositiveX(Vector4d var1);

   Vector3d transformPositiveY(Vector3d var1);

   Vector4d transformPositiveY(Vector4d var1);

   Vector3d transformUnitPositiveY(Vector3d var1);

   Vector4d transformUnitPositiveY(Vector4d var1);

   Vector3d transformPositiveZ(Vector3d var1);

   Vector4d transformPositiveZ(Vector4d var1);

   Vector3d transformUnitPositiveZ(Vector3d var1);

   Vector4d transformUnitPositiveZ(Vector4d var1);

   Vector4d transform(Vector4d var1);

   Vector4d transformInverse(Vector4d var1);

   Vector3d transform(Vector3dc var1, Vector3d var2);

   Vector3d transformInverse(Vector3dc var1, Vector3d var2);

   Vector3d transform(double var1, double var3, double var5, Vector3d var7);

   Vector3d transformInverse(double var1, double var3, double var5, Vector3d var7);

   Vector4d transform(Vector4dc var1, Vector4d var2);

   Vector4d transformInverse(Vector4dc var1, Vector4d var2);

   Vector4d transform(double var1, double var3, double var5, Vector4d var7);

   Vector4d transformInverse(double var1, double var3, double var5, Vector4d var7);

   Vector3f transform(Vector3f var1);

   Vector3f transformInverse(Vector3f var1);

   Vector4d transformUnit(Vector4d var1);

   Vector4d transformInverseUnit(Vector4d var1);

   Vector3d transformUnit(Vector3dc var1, Vector3d var2);

   Vector3d transformInverseUnit(Vector3dc var1, Vector3d var2);

   Vector3d transformUnit(double var1, double var3, double var5, Vector3d var7);

   Vector3d transformInverseUnit(double var1, double var3, double var5, Vector3d var7);

   Vector4d transformUnit(Vector4dc var1, Vector4d var2);

   Vector4d transformInverseUnit(Vector4dc var1, Vector4d var2);

   Vector4d transformUnit(double var1, double var3, double var5, Vector4d var7);

   Vector4d transformInverseUnit(double var1, double var3, double var5, Vector4d var7);

   Vector3f transformUnit(Vector3f var1);

   Vector3f transformInverseUnit(Vector3f var1);

   Vector3f transformPositiveX(Vector3f var1);

   Vector4f transformPositiveX(Vector4f var1);

   Vector3f transformUnitPositiveX(Vector3f var1);

   Vector4f transformUnitPositiveX(Vector4f var1);

   Vector3f transformPositiveY(Vector3f var1);

   Vector4f transformPositiveY(Vector4f var1);

   Vector3f transformUnitPositiveY(Vector3f var1);

   Vector4f transformUnitPositiveY(Vector4f var1);

   Vector3f transformPositiveZ(Vector3f var1);

   Vector4f transformPositiveZ(Vector4f var1);

   Vector3f transformUnitPositiveZ(Vector3f var1);

   Vector4f transformUnitPositiveZ(Vector4f var1);

   Vector4f transform(Vector4f var1);

   Vector4f transformInverse(Vector4f var1);

   Vector3f transform(Vector3fc var1, Vector3f var2);

   Vector3f transformInverse(Vector3fc var1, Vector3f var2);

   Vector3f transform(double var1, double var3, double var5, Vector3f var7);

   Vector3f transformInverse(double var1, double var3, double var5, Vector3f var7);

   Vector4f transform(Vector4fc var1, Vector4f var2);

   Vector4f transformInverse(Vector4fc var1, Vector4f var2);

   Vector4f transform(double var1, double var3, double var5, Vector4f var7);

   Vector4f transformInverse(double var1, double var3, double var5, Vector4f var7);

   Vector4f transformUnit(Vector4f var1);

   Vector4f transformInverseUnit(Vector4f var1);

   Vector3f transformUnit(Vector3fc var1, Vector3f var2);

   Vector3f transformInverseUnit(Vector3fc var1, Vector3f var2);

   Vector3f transformUnit(double var1, double var3, double var5, Vector3f var7);

   Vector3f transformInverseUnit(double var1, double var3, double var5, Vector3f var7);

   Vector4f transformUnit(Vector4fc var1, Vector4f var2);

   Vector4f transformInverseUnit(Vector4fc var1, Vector4f var2);

   Vector4f transformUnit(double var1, double var3, double var5, Vector4f var7);

   Vector4f transformInverseUnit(double var1, double var3, double var5, Vector4f var7);

   Quaterniond invert(Quaterniond var1);

   Quaterniond div(Quaterniondc var1, Quaterniond var2);

   Quaterniond conjugate(Quaterniond var1);

   double lengthSquared();

   Quaterniond slerp(Quaterniondc var1, double var2, Quaterniond var4);

   Quaterniond scale(double var1, Quaterniond var3);

   Quaterniond integrate(double var1, double var3, double var5, double var7, Quaterniond var9);

   Quaterniond nlerp(Quaterniondc var1, double var2, Quaterniond var4);

   Quaterniond nlerpIterative(Quaterniondc var1, double var2, double var4, Quaterniond var6);

   Quaterniond lookAlong(Vector3dc var1, Vector3dc var2, Quaterniond var3);

   Quaterniond lookAlong(double var1, double var3, double var5, double var7, double var9, double var11, Quaterniond var13);

   Quaterniond difference(Quaterniondc var1, Quaterniond var2);

   Quaterniond rotateTo(double var1, double var3, double var5, double var7, double var9, double var11, Quaterniond var13);

   Quaterniond rotateTo(Vector3dc var1, Vector3dc var2, Quaterniond var3);

   Quaterniond rotateX(double var1, Quaterniond var3);

   Quaterniond rotateY(double var1, Quaterniond var3);

   Quaterniond rotateZ(double var1, Quaterniond var3);

   Quaterniond rotateLocalX(double var1, Quaterniond var3);

   Quaterniond rotateLocalY(double var1, Quaterniond var3);

   Quaterniond rotateLocalZ(double var1, Quaterniond var3);

   Quaterniond rotateXYZ(double var1, double var3, double var5, Quaterniond var7);

   Quaterniond rotateZYX(double var1, double var3, double var5, Quaterniond var7);

   Quaterniond rotateYXZ(double var1, double var3, double var5, Quaterniond var7);

   Vector3d getEulerAnglesXYZ(Vector3d var1);

   Quaterniond rotateAxis(double var1, double var3, double var5, double var7, Quaterniond var9);

   Quaterniond rotateAxis(double var1, Vector3dc var3, Quaterniond var4);

   Vector3d positiveX(Vector3d var1);

   Vector3d normalizedPositiveX(Vector3d var1);

   Vector3d positiveY(Vector3d var1);

   Vector3d normalizedPositiveY(Vector3d var1);

   Vector3d positiveZ(Vector3d var1);

   Vector3d normalizedPositiveZ(Vector3d var1);

   Quaterniond conjugateBy(Quaterniondc var1, Quaterniond var2);

   boolean isFinite();

   boolean equals(Quaterniondc var1, double var2);

   boolean equals(double var1, double var3, double var5, double var7);
}
