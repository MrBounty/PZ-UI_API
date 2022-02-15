package org.joml;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public interface Quaternionfc {
   float x();

   float y();

   float z();

   float w();

   Quaternionf normalize(Quaternionf var1);

   Quaternionf add(float var1, float var2, float var3, float var4, Quaternionf var5);

   Quaternionf add(Quaternionfc var1, Quaternionf var2);

   float angle();

   Matrix3f get(Matrix3f var1);

   Matrix3d get(Matrix3d var1);

   Matrix4f get(Matrix4f var1);

   Matrix4d get(Matrix4d var1);

   Matrix4x3f get(Matrix4x3f var1);

   Matrix4x3d get(Matrix4x3d var1);

   AxisAngle4f get(AxisAngle4f var1);

   AxisAngle4d get(AxisAngle4d var1);

   Quaterniond get(Quaterniond var1);

   Quaternionf get(Quaternionf var1);

   ByteBuffer getAsMatrix3f(ByteBuffer var1);

   FloatBuffer getAsMatrix3f(FloatBuffer var1);

   ByteBuffer getAsMatrix4f(ByteBuffer var1);

   FloatBuffer getAsMatrix4f(FloatBuffer var1);

   ByteBuffer getAsMatrix4x3f(ByteBuffer var1);

   FloatBuffer getAsMatrix4x3f(FloatBuffer var1);

   Quaternionf mul(Quaternionfc var1, Quaternionf var2);

   Quaternionf mul(float var1, float var2, float var3, float var4, Quaternionf var5);

   Quaternionf premul(Quaternionfc var1, Quaternionf var2);

   Quaternionf premul(float var1, float var2, float var3, float var4, Quaternionf var5);

   Vector3f transform(Vector3f var1);

   Vector3f transformInverse(Vector3f var1);

   Vector3f transformUnit(Vector3f var1);

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

   Vector3f transform(float var1, float var2, float var3, Vector3f var4);

   Vector3d transform(float var1, float var2, float var3, Vector3d var4);

   Vector3f transformInverse(float var1, float var2, float var3, Vector3f var4);

   Vector3d transformInverse(float var1, float var2, float var3, Vector3d var4);

   Vector3f transformInverseUnit(Vector3f var1);

   Vector3f transformUnit(Vector3fc var1, Vector3f var2);

   Vector3f transformInverseUnit(Vector3fc var1, Vector3f var2);

   Vector3f transformUnit(float var1, float var2, float var3, Vector3f var4);

   Vector3d transformUnit(float var1, float var2, float var3, Vector3d var4);

   Vector3f transformInverseUnit(float var1, float var2, float var3, Vector3f var4);

   Vector3d transformInverseUnit(float var1, float var2, float var3, Vector3d var4);

   Vector4f transform(Vector4fc var1, Vector4f var2);

   Vector4f transformInverse(Vector4fc var1, Vector4f var2);

   Vector4f transform(float var1, float var2, float var3, Vector4f var4);

   Vector4f transformInverse(float var1, float var2, float var3, Vector4f var4);

   Vector4f transformUnit(Vector4fc var1, Vector4f var2);

   Vector4f transformUnit(Vector4f var1);

   Vector4f transformInverseUnit(Vector4f var1);

   Vector4f transformInverseUnit(Vector4fc var1, Vector4f var2);

   Vector4f transformUnit(float var1, float var2, float var3, Vector4f var4);

   Vector4f transformInverseUnit(float var1, float var2, float var3, Vector4f var4);

   Vector3d transform(Vector3d var1);

   Vector3d transformInverse(Vector3d var1);

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

   Quaternionf invert(Quaternionf var1);

   Quaternionf div(Quaternionfc var1, Quaternionf var2);

   Quaternionf conjugate(Quaternionf var1);

   Quaternionf rotateXYZ(float var1, float var2, float var3, Quaternionf var4);

   Quaternionf rotateZYX(float var1, float var2, float var3, Quaternionf var4);

   Quaternionf rotateYXZ(float var1, float var2, float var3, Quaternionf var4);

   Vector3f getEulerAnglesXYZ(Vector3f var1);

   float lengthSquared();

   Quaternionf slerp(Quaternionfc var1, float var2, Quaternionf var3);

   Quaternionf scale(float var1, Quaternionf var2);

   Quaternionf integrate(float var1, float var2, float var3, float var4, Quaternionf var5);

   Quaternionf nlerp(Quaternionfc var1, float var2, Quaternionf var3);

   Quaternionf nlerpIterative(Quaternionfc var1, float var2, float var3, Quaternionf var4);

   Quaternionf lookAlong(Vector3fc var1, Vector3fc var2, Quaternionf var3);

   Quaternionf lookAlong(float var1, float var2, float var3, float var4, float var5, float var6, Quaternionf var7);

   Quaternionf rotateTo(float var1, float var2, float var3, float var4, float var5, float var6, Quaternionf var7);

   Quaternionf rotateTo(Vector3fc var1, Vector3fc var2, Quaternionf var3);

   Quaternionf rotateX(float var1, Quaternionf var2);

   Quaternionf rotateY(float var1, Quaternionf var2);

   Quaternionf rotateZ(float var1, Quaternionf var2);

   Quaternionf rotateLocalX(float var1, Quaternionf var2);

   Quaternionf rotateLocalY(float var1, Quaternionf var2);

   Quaternionf rotateLocalZ(float var1, Quaternionf var2);

   Quaternionf rotateAxis(float var1, float var2, float var3, float var4, Quaternionf var5);

   Quaternionf rotateAxis(float var1, Vector3fc var2, Quaternionf var3);

   Quaternionf difference(Quaternionfc var1, Quaternionf var2);

   Vector3f positiveX(Vector3f var1);

   Vector3f normalizedPositiveX(Vector3f var1);

   Vector3f positiveY(Vector3f var1);

   Vector3f normalizedPositiveY(Vector3f var1);

   Vector3f positiveZ(Vector3f var1);

   Vector3f normalizedPositiveZ(Vector3f var1);

   Quaternionf conjugateBy(Quaternionfc var1, Quaternionf var2);

   boolean isFinite();

   boolean equals(Quaternionfc var1, float var2);

   boolean equals(float var1, float var2, float var3, float var4);
}
