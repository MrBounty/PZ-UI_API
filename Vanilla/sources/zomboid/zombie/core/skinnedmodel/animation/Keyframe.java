package zombie.core.skinnedmodel.animation;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import zombie.core.math.PZMath;

public final class Keyframe {
   public Quaternion Rotation;
   public Vector3f Position;
   public Vector3f Scale = new Vector3f(1.0F, 1.0F, 1.0F);
   public int Bone;
   public String BoneName;
   public float Time = -1.0F;

   public Keyframe() {
   }

   public Keyframe(Vector3f var1, Quaternion var2, Vector3f var3) {
      this.Position = new Vector3f(var1);
      this.Rotation = new Quaternion(var2);
      this.Scale = new Vector3f(var3);
   }

   public void set(Keyframe var1) {
      if (var1.Position != null) {
         this.setPosition(var1.Position);
      }

      if (var1.Rotation != null) {
         this.setRotation(var1.Rotation);
      }

      if (var1.Scale != null) {
         this.setScale(var1.Scale);
      }

      this.Time = var1.Time;
      this.Bone = var1.Bone;
      this.BoneName = var1.BoneName;
   }

   public void get(Vector3f var1, Quaternion var2, Vector3f var3) {
      setIfNotNull(var1, this.Position, 0.0F, 0.0F, 0.0F);
      setIfNotNull(var2, this.Rotation);
      setIfNotNull(var3, this.Scale, 1.0F, 1.0F, 1.0F);
   }

   private void setScale(Vector3f var1) {
      if (this.Scale == null) {
         this.Scale = new Vector3f();
      }

      this.Scale.set(var1);
   }

   private void setRotation(Quaternion var1) {
      if (this.Rotation == null) {
         this.Rotation = new Quaternion();
      }

      this.Rotation.set(var1);
   }

   private void setPosition(Vector3f var1) {
      if (this.Position == null) {
         this.Position = new Vector3f();
      }

      this.Position.set(var1);
   }

   public void clear() {
      this.Time = -1.0F;
      this.Position = null;
      this.Rotation = null;
   }

   public void setIdentity() {
      setIdentity(this.Position, this.Rotation, this.Scale);
   }

   public static void setIdentity(Vector3f var0, Quaternion var1, Vector3f var2) {
      setIfNotNull(var0, 0.0F, 0.0F, 0.0F);
      setIdentityIfNotNull(var1);
      setIfNotNull(var2, 1.0F, 1.0F, 1.0F);
   }

   public static Keyframe lerp(Keyframe var0, Keyframe var1, float var2, Keyframe var3) {
      lerp(var0, var1, var2, var3.Position, var3.Rotation, var3.Scale);
      var3.Bone = var1.Bone;
      var3.BoneName = var1.BoneName;
      var3.Time = var2;
      return var3;
   }

   public static void setIfNotNull(Vector3f var0, Vector3f var1, float var2, float var3, float var4) {
      if (var0 != null) {
         if (var1 != null) {
            var0.set(var1);
         } else {
            var0.set(var2, var3, var4);
         }
      }

   }

   public static void setIfNotNull(Vector3f var0, float var1, float var2, float var3) {
      if (var0 != null) {
         var0.set(var1, var2, var3);
      }

   }

   public static void setIfNotNull(Quaternion var0, Quaternion var1) {
      if (var0 != null) {
         if (var1 != null) {
            var0.set(var1);
         } else {
            var0.setIdentity();
         }
      }

   }

   public static void setIdentityIfNotNull(Quaternion var0) {
      if (var0 != null) {
         var0.setIdentity();
      }

   }

   public static void lerp(Keyframe var0, Keyframe var1, float var2, Vector3f var3, Quaternion var4, Vector3f var5) {
      if (var1.Time == var0.Time) {
         var1.get(var3, var4, var5);
      } else {
         float var6 = (var2 - var0.Time) / (var1.Time - var0.Time);
         if (var3 != null) {
            PZMath.lerp(var3, var0.Position, var1.Position, var6);
         }

         if (var4 != null) {
            PZMath.slerp(var4, var0.Rotation, var1.Rotation, var6);
         }

         if (var5 != null) {
            PZMath.lerp(var5, var0.Scale, var1.Scale, var6);
         }

      }
   }
}
