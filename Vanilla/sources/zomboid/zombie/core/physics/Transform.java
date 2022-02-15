package zombie.core.physics;

import org.joml.Matrix3f;
import org.joml.Matrix3fc;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class Transform {
   public final Matrix3f basis = new Matrix3f();
   public final Vector3f origin = new Vector3f();

   public Transform() {
   }

   public Transform(Matrix3f var1) {
      this.basis.set((Matrix3fc)var1);
   }

   public Transform(Matrix4f var1) {
      this.set(var1);
   }

   public Transform(Transform var1) {
      this.set(var1);
   }

   public void set(Transform var1) {
      this.basis.set((Matrix3fc)var1.basis);
      this.origin.set((Vector3fc)var1.origin);
   }

   public void set(Matrix3f var1) {
      this.basis.set((Matrix3fc)var1);
      this.origin.set(0.0F, 0.0F, 0.0F);
   }

   public void set(Matrix4f var1) {
      var1.get3x3(this.basis);
      var1.getTranslation(this.origin);
   }

   public void transform(Vector3f var1) {
      this.basis.transform(var1);
      var1.add(this.origin);
   }

   public void setIdentity() {
      this.basis.identity();
      this.origin.set(0.0F, 0.0F, 0.0F);
   }

   public void inverse() {
      this.basis.transpose();
      this.origin.negate();
      this.basis.transform(this.origin);
   }

   public void inverse(Transform var1) {
      this.set(var1);
      this.inverse();
   }

   public Quaternionf getRotation(Quaternionf var1) {
      this.basis.getUnnormalizedRotation(var1);
      return var1;
   }

   public void setRotation(Quaternionf var1) {
      this.basis.set((Quaternionfc)var1);
   }

   public Matrix4f getMatrix(Matrix4f var1) {
      var1.set((Matrix3fc)this.basis);
      var1.setTranslation(this.origin);
      return var1;
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof Transform) {
         Transform var2 = (Transform)var1;
         return this.basis.equals(var2.basis) && this.origin.equals(var2.origin);
      } else {
         return false;
      }
   }

   public int hashCode() {
      byte var1 = 3;
      int var2 = 41 * var1 + this.basis.hashCode();
      var2 = 41 * var2 + this.origin.hashCode();
      return var2;
   }
}
