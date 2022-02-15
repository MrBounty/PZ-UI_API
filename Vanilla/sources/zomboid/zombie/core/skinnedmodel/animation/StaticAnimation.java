package zombie.core.skinnedmodel.animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import zombie.core.PerformanceSettings;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.HelperFunctions;

/** @deprecated */
@Deprecated
public class StaticAnimation {
   private int framesPerSecond;
   public String name;
   public Matrix4f[][] Matrices;
   private Matrix4f[] RootMotion;
   public AnimationClip Clip;
   private int currentKeyframe = 0;
   private float currentTimeValue = 0.0F;
   private Keyframe[] Pose;
   private Keyframe[] PrevPose;
   private float lastTime = 0.0F;

   public StaticAnimation(AnimationClip var1) {
      this.Clip = var1;
      this.framesPerSecond = PerformanceSettings.BaseStaticAnimFramerate;
      this.Matrices = new Matrix4f[(int)((float)this.framesPerSecond * this.Clip.Duration)][60];
      this.RootMotion = new Matrix4f[(int)((float)this.framesPerSecond * this.Clip.Duration)];
      this.Pose = new Keyframe[60];
      this.PrevPose = new Keyframe[60];
      this.Create();
      Arrays.fill(this.Pose, (Object)null);
      this.Pose = null;
      Arrays.fill(this.PrevPose, (Object)null);
      this.PrevPose = null;
   }

   private Keyframe getNextKeyFrame(int var1, int var2, Keyframe var3) {
      Keyframe[] var4 = this.Clip.getKeyframes();

      for(int var5 = var2; var5 < var4.length; ++var5) {
         Keyframe var6 = var4[var5];
         if (var6.Bone == var1 && var6.Time > this.currentTimeValue && var3 != var6) {
            return var6;
         }
      }

      return null;
   }

   public Quaternion getRotation(Quaternion var1, int var2) {
      if (this.PrevPose[var2] != null && PerformanceSettings.InterpolateAnims) {
         float var3 = (this.currentTimeValue - this.PrevPose[var2].Time) / (this.Pose[var2].Time - this.PrevPose[var2].Time);
         if (this.Pose[var2].Time - this.PrevPose[var2].Time == 0.0F) {
            var3 = 0.0F;
         }

         return PZMath.slerp(var1, this.PrevPose[var2].Rotation, this.Pose[var2].Rotation, var3);
      } else {
         var1.set(this.Pose[var2].Rotation);
         return var1;
      }
   }

   public Vector3f getPosition(Vector3f var1, int var2) {
      if (this.PrevPose[var2] != null && PerformanceSettings.InterpolateAnims) {
         float var3 = (this.currentTimeValue - this.PrevPose[var2].Time) / (this.Pose[var2].Time - this.PrevPose[var2].Time);
         if (this.Pose[var2].Time - this.PrevPose[var2].Time == 0.0F) {
            var3 = 0.0F;
         }

         PZMath.lerp(var1, this.PrevPose[var2].Position, this.Pose[var2].Position, var3);
         return var1;
      } else {
         var1.set(this.Pose[var2].Position);
         return var1;
      }
   }

   public void getPose() {
      Keyframe[] var1 = this.Clip.getKeyframes();

      for(this.currentKeyframe = 0; this.currentKeyframe < var1.length; ++this.currentKeyframe) {
         Keyframe var2 = var1[this.currentKeyframe];
         if (this.currentKeyframe == var1.length - 1 || !(var2.Time <= this.currentTimeValue)) {
            if (PerformanceSettings.InterpolateAnims) {
               for(int var3 = 0; var3 < 60; ++var3) {
                  if (this.Pose[var3] == null || this.currentTimeValue >= this.Pose[var3].Time) {
                     Keyframe var4 = this.getNextKeyFrame(var3, this.currentKeyframe, this.Pose[var3]);
                     if (var4 != null) {
                        this.PrevPose[var4.Bone] = this.Pose[var4.Bone];
                        this.Pose[var4.Bone] = var4;
                     } else {
                        this.PrevPose[var3] = null;
                     }
                  }
               }
            }
            break;
         }

         if (var2.Bone >= 0) {
            this.Pose[var2.Bone] = var2;
         }

         this.lastTime = var2.Time;
      }

   }

   public void Create() {
      float var1 = (float)this.Matrices.length;
      double var2 = (double)this.Clip.Duration / (double)var1;
      double var4 = 0.0D;
      int var6 = 0;

      for(Matrix4f var7 = new Matrix4f(); (float)var6 < var1; ++var6) {
         this.currentTimeValue = (float)var4;
         this.getPose();

         for(int var8 = 0; var8 < 60; ++var8) {
            if (this.Pose[var8] == null) {
               this.Matrices[var6][var8] = var7;
            } else {
               Quaternion var9 = new Quaternion();
               this.getRotation(var9, var8);
               Vector3f var10 = new Vector3f();
               this.getPosition(var10, var8);
               Matrix4f var11 = HelperFunctions.CreateFromQuaternionPositionScale(var10, var9, new Vector3f(1.0F, 1.0F, 1.0F), new Matrix4f());
               this.Matrices[var6][var8] = var11;
            }
         }

         var4 += var2;
      }

   }

   public Keyframe interpolate(List var1, float var2) {
      int var3 = 0;
      Keyframe var4 = null;

      Keyframe var6;
      for(Object var5 = null; var3 < var1.size(); var4 = var6) {
         var6 = (Keyframe)var1.get(var3);
         if (var6.Time > var2 && var4.Time <= var2) {
            Quaternion var7 = new Quaternion();
            Vector3f var8 = new Vector3f();
            float var9 = (var2 - var4.Time) / (var6.Time - var4.Time);
            PZMath.slerp(var7, var4.Rotation, var6.Rotation, var9);
            PZMath.lerp(var8, var4.Position, var6.Position, var9);
            Keyframe var10 = new Keyframe();
            var10.Position = var8;
            var10.Rotation = var7;
            var10.Scale = new Vector3f(1.0F, 1.0F, 1.0F);
            var10.Time = var4.Time + (var6.Time - var4.Time) * var9;
            return var10;
         }

         ++var3;
      }

      return (Keyframe)var1.get(var1.size() - 1);
   }

   public void interpolate(List var1) {
      if (!var1.isEmpty()) {
         if (!((Keyframe)var1.get(0)).Position.equals(((Keyframe)var1.get(var1.size() - 1)).Position)) {
            float var2 = (float)(this.Matrices.length + 1);
            double var3 = (double)this.Clip.Duration / (double)var2;
            double var5 = 0.0D;
            ArrayList var7 = new ArrayList();

            for(int var8 = 0; (float)var8 < var2 - 1.0F; var5 += var3) {
               Keyframe var9 = this.interpolate(var1, (float)var5);
               var7.add(var9);
               ++var8;
            }

            var1.clear();
            var1.addAll(var7);
         }
      }
   }

   public void doRootMotion(List var1) {
      float var2 = (float)this.Matrices.length;
      if (var1.size() > 3) {
         for(int var3 = 0; (float)var3 < var2 && var3 < var1.size(); ++var3) {
            Keyframe var4 = (Keyframe)var1.get(var3);
            Quaternion var5 = var4.Rotation;
            Vector3f var6 = var4.Position;
            Matrix4f var7 = HelperFunctions.CreateFromQuaternionPositionScale(var6, var5, var4.Scale, new Matrix4f());
            this.RootMotion[var3] = var7;
         }

      }
   }
}
