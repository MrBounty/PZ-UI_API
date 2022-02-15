package zombie.core.skinnedmodel.animation;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import zombie.core.skinnedmodel.HelperFunctions;
import zombie.util.Pool;
import zombie.util.PooledObject;

public class BoneTransform extends PooledObject {
   private boolean m_matrixValid = true;
   private final Matrix4f m_matrix = new Matrix4f();
   private final HelperFunctions.TransformResult_QPS m_transformResult;
   private boolean m_prsValid;
   private final Vector3f m_pos;
   private final Quaternion m_rot;
   private final Vector3f m_scale;
   private static final Pool s_pool = new Pool(BoneTransform::new);

   protected BoneTransform() {
      this.m_transformResult = new HelperFunctions.TransformResult_QPS(this.m_matrix);
      this.m_prsValid = true;
      this.m_pos = new Vector3f();
      this.m_rot = new Quaternion();
      this.m_scale = new Vector3f();
      this.setIdentity();
   }

   public void setIdentity() {
      this.m_matrixValid = true;
      this.m_matrix.setIdentity();
      this.m_prsValid = true;
      this.m_pos.set(0.0F, 0.0F, 0.0F);
      this.m_rot.setIdentity();
      this.m_scale.set(1.0F, 1.0F, 1.0F);
   }

   public void set(BoneTransform var1) {
      this.m_matrixValid = var1.m_matrixValid;
      this.m_prsValid = var1.m_prsValid;
      this.m_pos.set(var1.m_pos);
      this.m_rot.set(var1.m_rot);
      this.m_scale.set(var1.m_scale);
      this.m_matrix.load(var1.m_matrix);
   }

   public void set(Vector3f var1, Quaternion var2, Vector3f var3) {
      if (this.m_matrixValid || !this.m_prsValid || !this.m_pos.equals(var1) || !this.m_rot.equals(var2) || !this.m_scale.equals(var3)) {
         this.m_matrixValid = false;
         this.m_prsValid = true;
         this.m_pos.set(var1);
         this.m_rot.set(var2);
         this.m_scale.set(var3);
      }
   }

   public void set(Matrix4f var1) {
      this.m_matrixValid = true;
      this.m_matrix.load(var1);
      this.m_prsValid = false;
   }

   public void mul(Matrix4f var1, Matrix4f var2) {
      this.m_matrixValid = true;
      this.m_prsValid = false;
      Matrix4f.mul(var1, var2, this.m_matrix);
   }

   public void getMatrix(Matrix4f var1) {
      var1.load(this.getValidMatrix_Internal());
   }

   public void getPRS(Vector3f var1, Quaternion var2, Vector3f var3) {
      this.validatePRS();
      var1.set(this.m_pos);
      var2.set(this.m_rot);
      var3.set(this.m_scale);
   }

   public void getPosition(Vector3f var1) {
      this.validatePRS();
      var1.set(this.m_pos);
   }

   private Matrix4f getValidMatrix_Internal() {
      this.validateMatrix();
      return this.m_matrix;
   }

   private void validateMatrix() {
      if (!this.m_matrixValid) {
         this.validateInternal();
         this.m_matrixValid = true;
         HelperFunctions.CreateFromQuaternionPositionScale(this.m_pos, this.m_rot, this.m_scale, this.m_transformResult);
      }
   }

   protected void validatePRS() {
      if (!this.m_prsValid) {
         this.validateInternal();
         this.m_prsValid = true;
         HelperFunctions.getPosition(this.m_matrix, this.m_pos);
         HelperFunctions.getRotation(this.m_matrix, this.m_rot);
         this.m_scale.set(1.0F, 1.0F, 1.0F);
      }
   }

   protected void validateInternal() {
      if (!this.m_prsValid && !this.m_matrixValid) {
         throw new RuntimeException("Neither the matrix nor the PosRotScale values in this object are listed as valid.");
      }
   }

   public static void mul(BoneTransform var0, Matrix4f var1, Matrix4f var2) {
      Matrix4f.mul(var0.getValidMatrix_Internal(), var1, var2);
   }

   public static BoneTransform alloc() {
      return (BoneTransform)s_pool.alloc();
   }
}
