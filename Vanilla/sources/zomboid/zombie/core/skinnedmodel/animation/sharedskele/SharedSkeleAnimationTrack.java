package zombie.core.skinnedmodel.animation.sharedskele;

import org.lwjgl.util.vector.Matrix4f;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.animation.AnimTrackSampler;
import zombie.debug.DebugOptions;

public class SharedSkeleAnimationTrack implements AnimTrackSampler {
   private int m_numFrames;
   private float m_totalTime;
   private boolean m_isLooped;
   private SharedSkeleAnimationTrack.BoneTrack[] m_boneTracks;
   private float m_currentTime = 0.0F;

   public void set(AnimTrackSampler var1, float var2) {
      float var3 = var1.getTotalTime();
      boolean var4 = var1.isLooped();
      int var5 = var1.getNumBones();
      this.m_totalTime = var3;
      this.m_numFrames = PZMath.max((int)(var3 * var2 + 0.99F), 1);
      this.m_isLooped = var4;
      this.m_boneTracks = new SharedSkeleAnimationTrack.BoneTrack[var5];

      for(int var6 = 0; var6 < var5; ++var6) {
         this.m_boneTracks[var6] = new SharedSkeleAnimationTrack.BoneTrack();
         this.m_boneTracks[var6].m_animationData = new float[this.m_numFrames * 16];
      }

      Matrix4f var14 = new Matrix4f();
      float var7 = var3 / (float)(this.m_numFrames - 1);

      for(int var8 = 0; var8 < this.m_numFrames; ++var8) {
         float var9 = var7 * (float)var8;
         var1.moveToTime(var9);

         for(int var10 = 0; var10 < var5; ++var10) {
            var1.getBoneMatrix(var10, var14);
            int var11 = var8 * 16;
            SharedSkeleAnimationTrack.BoneTrack var12 = this.m_boneTracks[var10];
            float[] var13 = var12.m_animationData;
            var13[var11] = var14.m00;
            var13[var11 + 1] = var14.m01;
            var13[var11 + 2] = var14.m02;
            var13[var11 + 3] = var14.m03;
            var13[var11 + 4] = var14.m10;
            var13[var11 + 5] = var14.m11;
            var13[var11 + 6] = var14.m12;
            var13[var11 + 7] = var14.m13;
            var13[var11 + 8] = var14.m20;
            var13[var11 + 9] = var14.m21;
            var13[var11 + 10] = var14.m22;
            var13[var11 + 11] = var14.m23;
            var13[var11 + 12] = var14.m30;
            var13[var11 + 13] = var14.m31;
            var13[var11 + 14] = var14.m32;
            var13[var11 + 15] = var14.m33;
         }
      }

   }

   public float getTotalTime() {
      return this.m_totalTime;
   }

   public boolean isLooped() {
      return this.m_isLooped;
   }

   public void moveToTime(float var1) {
      this.m_currentTime = var1;
   }

   public float getCurrentTime() {
      return this.m_currentTime;
   }

   public void getBoneMatrix(int var1, Matrix4f var2) {
      float var3 = this.m_totalTime;
      int var4 = this.m_numFrames;
      float var5 = this.getCurrentTime();
      float var6 = var5 / var3;
      float var7 = var6 * (float)(var4 - 1);
      if (this.isLooped()) {
         this.sampleAtTime_Looped(var2, var1, var7);
      } else {
         this.sampleAtTime_NonLooped(var2, var1, var7);
      }

   }

   public int getNumBones() {
      return this.m_boneTracks != null ? this.m_boneTracks.length : 0;
   }

   private void sampleAtTime_NonLooped(Matrix4f var1, int var2, float var3) {
      int var4 = (int)var3;
      float var5 = var3 - (float)var4;
      int var6 = PZMath.clamp(var4, 0, this.m_numFrames - 1);
      int var7 = PZMath.clamp(var6 + 1, 0, this.m_numFrames - 1);
      boolean var8 = DebugOptions.instance.Animation.SharedSkeles.AllowLerping.getValue();
      this.sampleBoneData(var2, var6, var7, var5, var8, var1);
   }

   private void sampleAtTime_Looped(Matrix4f var1, int var2, float var3) {
      int var4 = (int)var3;
      float var5 = var3 - (float)var4;
      int var6 = var4 % this.m_numFrames;
      int var7 = (var6 + 1) % this.m_numFrames;
      boolean var8 = DebugOptions.instance.Animation.SharedSkeles.AllowLerping.getValue();
      this.sampleBoneData(var2, var6, var7, var5, var8, var1);
   }

   private void sampleBoneData(int var1, int var2, int var3, float var4, boolean var5, Matrix4f var6) {
      int var7 = var2 * 16;
      SharedSkeleAnimationTrack.BoneTrack var8 = this.m_boneTracks[var1];
      float[] var9 = var8.m_animationData;
      if (var2 != var3 && var5) {
         int var10 = var3 * 16;
         var6.m00 = PZMath.lerp(var9[var7], var9[var10], var4);
         var6.m01 = PZMath.lerp(var9[var7 + 1], var9[var10 + 1], var4);
         var6.m02 = PZMath.lerp(var9[var7 + 2], var9[var10 + 2], var4);
         var6.m03 = PZMath.lerp(var9[var7 + 3], var9[var10 + 3], var4);
         var6.m10 = PZMath.lerp(var9[var7 + 4], var9[var10 + 4], var4);
         var6.m11 = PZMath.lerp(var9[var7 + 5], var9[var10 + 5], var4);
         var6.m12 = PZMath.lerp(var9[var7 + 6], var9[var10 + 6], var4);
         var6.m13 = PZMath.lerp(var9[var7 + 7], var9[var10 + 7], var4);
         var6.m20 = PZMath.lerp(var9[var7 + 8], var9[var10 + 8], var4);
         var6.m21 = PZMath.lerp(var9[var7 + 9], var9[var10 + 9], var4);
         var6.m22 = PZMath.lerp(var9[var7 + 10], var9[var10 + 10], var4);
         var6.m23 = PZMath.lerp(var9[var7 + 11], var9[var10 + 11], var4);
         var6.m30 = PZMath.lerp(var9[var7 + 12], var9[var10 + 12], var4);
         var6.m31 = PZMath.lerp(var9[var7 + 13], var9[var10 + 13], var4);
         var6.m32 = PZMath.lerp(var9[var7 + 14], var9[var10 + 14], var4);
         var6.m33 = PZMath.lerp(var9[var7 + 15], var9[var10 + 15], var4);
      } else {
         var6.m00 = var9[var7];
         var6.m01 = var9[var7 + 1];
         var6.m02 = var9[var7 + 2];
         var6.m03 = var9[var7 + 3];
         var6.m10 = var9[var7 + 4];
         var6.m11 = var9[var7 + 5];
         var6.m12 = var9[var7 + 6];
         var6.m13 = var9[var7 + 7];
         var6.m20 = var9[var7 + 8];
         var6.m21 = var9[var7 + 9];
         var6.m22 = var9[var7 + 10];
         var6.m23 = var9[var7 + 11];
         var6.m30 = var9[var7 + 12];
         var6.m31 = var9[var7 + 13];
         var6.m32 = var9[var7 + 14];
         var6.m33 = var9[var7 + 15];
      }

   }

   private static class BoneTrack {
      private float[] m_animationData;
   }
}
