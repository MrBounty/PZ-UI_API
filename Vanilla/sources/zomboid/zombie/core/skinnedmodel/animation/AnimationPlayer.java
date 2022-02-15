package zombie.core.skinnedmodel.animation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.joml.Math;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import zombie.GameProfiler;
import zombie.GameTime;
import zombie.characters.IsoGameCharacter;
import zombie.core.math.PZMath;
import zombie.core.math.Vector3;
import zombie.core.skinnedmodel.HelperFunctions;
import zombie.core.skinnedmodel.advancedanimation.AdvancedAnimator;
import zombie.core.skinnedmodel.animation.debug.AnimationPlayerRecorder;
import zombie.core.skinnedmodel.animation.sharedskele.SharedSkeleAnimationRepository;
import zombie.core.skinnedmodel.animation.sharedskele.SharedSkeleAnimationTrack;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.skinnedmodel.model.SkinningBone;
import zombie.core.skinnedmodel.model.SkinningData;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.iso.Vector2;
import zombie.network.MPStatistic;
import zombie.util.IPooledObject;
import zombie.util.Lambda;
import zombie.util.Pool;
import zombie.util.PooledObject;
import zombie.util.StringUtils;
import zombie.util.list.PZArrayUtil;

public final class AnimationPlayer extends PooledObject {
   private Model model;
   private final Matrix4f propTransforms = new Matrix4f();
   private boolean m_boneTransformsNeedFirstFrame = true;
   private TwistableBoneTransform[] m_boneTransforms;
   public Matrix4f[] modelTransforms;
   private AnimationPlayer.SkinTransformData m_skinTransformData = null;
   private AnimationPlayer.SkinTransformData m_skinTransformDataPool = null;
   private SkinningData m_skinningData;
   private SharedSkeleAnimationRepository m_sharedSkeleAnimationRepo = null;
   private SharedSkeleAnimationTrack m_currentSharedTrack;
   private AnimationClip m_currentSharedTrackClip;
   private float m_angle;
   private float m_targetAngle;
   private float m_twistAngle;
   private float m_shoulderTwistAngle;
   private float m_targetTwistAngle;
   private float m_maxTwistAngle = PZMath.degToRad(70.0F);
   private float m_excessTwist = 0.0F;
   private static final float angleStepBase = 0.15F;
   public float angleStepDelta = 1.0F;
   public float angleTwistDelta = 1.0F;
   public boolean bDoBlending = true;
   public boolean bUpdateBones = true;
   private final Vector2 m_lastSetDir = new Vector2();
   private final ArrayList m_reparentedBoneBindings = new ArrayList();
   private final List m_twistBones = new ArrayList();
   private AnimationBoneBinding m_counterRotationBone = null;
   public final ArrayList dismembered = new ArrayList();
   private final float m_minimumValidAnimWeight = 0.001F;
   private final int m_animBlendIndexCacheSize = 32;
   private final int[] m_animBlendIndices = new int[32];
   private final float[] m_animBlendWeights = new float[32];
   private final int[] m_animBlendLayers = new int[32];
   private final int[] m_animBlendPriorities = new int[32];
   private final int m_maxLayers = 4;
   private final int[] m_layerBlendCounts = new int[4];
   private final float[] m_layerWeightTotals = new float[4];
   private int m_totalAnimBlendCount = 0;
   public AnimationPlayer parentPlayer;
   private final Vector2 m_deferredMovement = new Vector2();
   private float m_deferredRotationWeight = 0.0F;
   private float m_deferredAngleDelta = 0.0F;
   private AnimationPlayerRecorder m_recorder = null;
   private static final AnimationTrack[] tempTracks = new AnimationTrack[0];
   private static final Vector2 tempo = new Vector2();
   private static final Pool s_pool = new Pool(AnimationPlayer::new);
   private final AnimationMultiTrack m_multiTrack = new AnimationMultiTrack();

   private AnimationPlayer() {
   }

   public static AnimationPlayer alloc(Model var0) {
      AnimationPlayer var1 = (AnimationPlayer)s_pool.alloc();
      var1.setModel(var0);
      return var1;
   }

   public static float lerpBlendWeight(float var0, float var1, float var2) {
      if (PZMath.equal(var0, var1, 1.0E-4F)) {
         return var1;
      } else {
         float var3 = 1.0F / var2;
         float var4 = GameTime.getInstance().getTimeDelta();
         float var5 = var1 - var0;
         float var6 = (float)PZMath.sign(var5);
         float var7 = var0 + var6 * var3 * var4;
         float var8 = var1 - var7;
         float var9 = (float)PZMath.sign(var8);
         if (var9 != var6) {
            var7 = var1;
         }

         return var7;
      }
   }

   public void setModel(Model var1) {
      Objects.requireNonNull(var1);
      if (var1 != this.model) {
         this.model = var1;
         this.initSkinningData();
      }
   }

   public Model getModel() {
      return this.model;
   }

   private void initSkinningData() {
      if (this.model.isReady()) {
         SkinningData var1 = (SkinningData)this.model.Tag;
         if (var1 != null) {
            if (this.m_skinningData != var1) {
               if (this.m_skinningData != null) {
                  this.m_skinningData = null;
                  this.m_multiTrack.reset();
               }

               this.m_skinningData = var1;
               Lambda.forEachFrom(PZArrayUtil::forEach, (List)this.m_reparentedBoneBindings, this.m_skinningData, AnimationBoneBindingPair::setSkinningData);
               Lambda.forEachFrom(PZArrayUtil::forEach, (List)this.m_twistBones, this.m_skinningData, AnimationBoneBinding::setSkinningData);
               if (this.m_counterRotationBone != null) {
                  this.m_counterRotationBone.setSkinningData(this.m_skinningData);
               }

               int var2 = var1.numBones();
               this.modelTransforms = (Matrix4f[])PZArrayUtil.newInstance(Matrix4f.class, this.modelTransforms, var2, Matrix4f::new);
               this.m_boneTransforms = (TwistableBoneTransform[])PZArrayUtil.newInstance(TwistableBoneTransform.class, this.m_boneTransforms, var2);

               for(int var3 = 0; var3 < var2; ++var3) {
                  if (this.m_boneTransforms[var3] == null) {
                     this.m_boneTransforms[var3] = TwistableBoneTransform.alloc();
                  }

                  this.m_boneTransforms[var3].setIdentity();
               }

               this.m_boneTransformsNeedFirstFrame = true;
            }
         }
      }
   }

   public boolean isReady() {
      this.initSkinningData();
      return this.hasSkinningData();
   }

   public boolean hasSkinningData() {
      return this.m_skinningData != null;
   }

   public void addBoneReparent(String var1, String var2) {
      if (!PZArrayUtil.contains((List)this.m_reparentedBoneBindings, Lambda.predicate(var1, var2, AnimationBoneBindingPair::matches))) {
         AnimationBoneBindingPair var3 = new AnimationBoneBindingPair(var1, var2);
         var3.setSkinningData(this.m_skinningData);
         this.m_reparentedBoneBindings.add(var3);
      }
   }

   public void setTwistBones(String... var1) {
      ArrayList var2 = AnimationPlayer.L_setTwistBones.boneNames;
      PZArrayUtil.listConvert(this.m_twistBones, var2, (var0) -> {
         return var0.boneName;
      });
      if (!PZArrayUtil.sequenceEqual((Object[])var1, var2, PZArrayUtil.Comparators::equalsIgnoreCase)) {
         this.m_twistBones.clear();
         Lambda.forEachFrom(PZArrayUtil::forEach, (Object)var1, this, (var0, var1x) -> {
            AnimationBoneBinding var2 = new AnimationBoneBinding((String)var0);
            var2.setSkinningData(var1x.m_skinningData);
            var1x.m_twistBones.add(var2);
         });
      }
   }

   public void setCounterRotationBone(String var1) {
      if (this.m_counterRotationBone != null && StringUtils.equals(this.m_counterRotationBone.boneName, var1)) {
      }

      this.m_counterRotationBone = new AnimationBoneBinding(var1);
      this.m_counterRotationBone.setSkinningData(this.m_skinningData);
   }

   public AnimationBoneBinding getCounterRotationBone() {
      return this.m_counterRotationBone;
   }

   public void reset() {
      this.m_multiTrack.reset();
   }

   public void onReleased() {
      this.model = null;
      this.m_skinningData = null;
      this.propTransforms.setIdentity();
      this.m_boneTransformsNeedFirstFrame = true;
      IPooledObject.tryReleaseAndBlank(this.m_boneTransforms);
      PZArrayUtil.forEach((Object[])this.modelTransforms, Matrix::setIdentity);
      this.resetSkinTransforms();
      this.setAngle(0.0F);
      this.setTargetAngle(0.0F);
      this.m_twistAngle = 0.0F;
      this.m_shoulderTwistAngle = 0.0F;
      this.m_targetTwistAngle = 0.0F;
      this.m_maxTwistAngle = PZMath.degToRad(70.0F);
      this.m_excessTwist = 0.0F;
      this.angleStepDelta = 1.0F;
      this.angleTwistDelta = 1.0F;
      this.bDoBlending = true;
      this.bUpdateBones = true;
      this.m_lastSetDir.set(0.0F, 0.0F);
      this.m_reparentedBoneBindings.clear();
      this.m_twistBones.clear();
      this.m_counterRotationBone = null;
      this.dismembered.clear();
      Arrays.fill(this.m_animBlendIndices, 0);
      Arrays.fill(this.m_animBlendWeights, 0.0F);
      Arrays.fill(this.m_animBlendLayers, 0);
      Arrays.fill(this.m_layerBlendCounts, 0);
      Arrays.fill(this.m_layerWeightTotals, 0.0F);
      this.m_totalAnimBlendCount = 0;
      this.parentPlayer = null;
      this.m_deferredMovement.set(0.0F, 0.0F);
      this.m_deferredRotationWeight = 0.0F;
      this.m_deferredAngleDelta = 0.0F;
      this.m_recorder = null;
      this.m_multiTrack.reset();
   }

   public SkinningData getSkinningData() {
      return this.m_skinningData;
   }

   public HashMap getSkinningBoneIndices() {
      return this.m_skinningData != null ? this.m_skinningData.BoneIndices : null;
   }

   public int getSkinningBoneIndex(String var1, int var2) {
      HashMap var3 = this.getSkinningBoneIndices();
      return var3 != null ? (Integer)var3.get(var1) : var2;
   }

   private synchronized AnimationPlayer.SkinTransformData getSkinTransformData(SkinningData var1) {
      AnimationPlayer.SkinTransformData var2;
      for(var2 = this.m_skinTransformData; var2 != null; var2 = var2.m_next) {
         if (var1 == var2.m_skinnedTo) {
            return var2;
         }
      }

      if (this.m_skinTransformDataPool != null) {
         var2 = this.m_skinTransformDataPool;
         var2.setSkinnedTo(var1);
         var2.dirty = true;
         this.m_skinTransformDataPool = this.m_skinTransformDataPool.m_next;
      } else {
         var2 = AnimationPlayer.SkinTransformData.alloc(var1);
      }

      var2.m_next = this.m_skinTransformData;
      this.m_skinTransformData = var2;
      return var2;
   }

   private synchronized void resetSkinTransforms() {
      GameProfiler.getInstance().invokeAndMeasure("resetSkinTransforms", this, AnimationPlayer::resetSkinTransformsInternal);
   }

   private void resetSkinTransformsInternal() {
      if (this.m_skinTransformDataPool != null) {
         AnimationPlayer.SkinTransformData var1;
         for(var1 = this.m_skinTransformDataPool; var1.m_next != null; var1 = var1.m_next) {
         }

         var1.m_next = this.m_skinTransformData;
      } else {
         this.m_skinTransformDataPool = this.m_skinTransformData;
      }

      this.m_skinTransformData = null;
   }

   public Matrix4f GetPropBoneMatrix(int var1) {
      this.propTransforms.load(this.modelTransforms[var1]);
      return this.propTransforms;
   }

   private AnimationTrack startClip(AnimationClip var1, boolean var2) {
      if (var1 == null) {
         throw new NullPointerException("Supplied clip is null.");
      } else {
         AnimationTrack var3 = AnimationTrack.alloc();
         var3.startClip(var1, var2);
         var3.name = var1.Name;
         var3.IsPlaying = true;
         this.m_multiTrack.addTrack(var3);
         return var3;
      }
   }

   public static void releaseTracks(List var0) {
      AnimationTrack[] var1 = (AnimationTrack[])var0.toArray(tempTracks);
      PZArrayUtil.forEach((Object[])var1, PooledObject::release);
   }

   public AnimationTrack play(String var1, boolean var2) {
      if (this.m_skinningData == null) {
         return null;
      } else {
         AnimationClip var3 = (AnimationClip)this.m_skinningData.AnimationClips.get(var1);
         if (var3 == null) {
            DebugLog.General.warn("Anim Clip not found: %s", var1);
            return null;
         } else {
            AnimationTrack var4 = this.startClip(var3, var2);
            return var4;
         }
      }
   }

   public void Update() {
      this.Update(GameTime.instance.getTimeDelta());
   }

   public void Update(float var1) {
      MPStatistic.getInstance().AnimationPlayerUpdate.Start();
      GameProfiler.getInstance().invokeAndMeasure("AnimationPlayer.Update", this, var1, AnimationPlayer::updateInternal);
      MPStatistic.getInstance().AnimationPlayerUpdate.End();
   }

   private void updateInternal(float var1) {
      if (this.isReady()) {
         this.m_multiTrack.Update(var1);
         if (!this.bUpdateBones) {
            this.updateAnimation_NonVisualOnly();
         } else if (this.m_multiTrack.getTrackCount() > 0) {
            SharedSkeleAnimationTrack var2 = this.determineCurrentSharedSkeleTrack();
            if (var2 != null) {
               float var3 = this.m_multiTrack.getTrackAt(0).getCurrentTime();
               this.updateAnimation_SharedSkeleTrack(var2, var3);
            } else {
               this.updateAnimation_StandardAnimation();
            }
         }
      }
   }

   private SharedSkeleAnimationTrack determineCurrentSharedSkeleTrack() {
      if (this.m_sharedSkeleAnimationRepo == null) {
         return null;
      } else if (this.bDoBlending) {
         return null;
      } else if (!DebugOptions.instance.Animation.SharedSkeles.Enabled.getValue()) {
         return null;
      } else if (this.m_multiTrack.getTrackCount() != 1) {
         return null;
      } else if (!PZMath.equal(this.m_twistAngle, 0.0F, 114.59155F)) {
         return null;
      } else if (this.parentPlayer != null) {
         return null;
      } else {
         AnimationTrack var1 = this.m_multiTrack.getTrackAt(0);
         float var2 = var1.blendFieldWeight;
         if (!PZMath.equal(var2, 0.0F, 0.1F)) {
            return null;
         } else {
            AnimationClip var3 = var1.getClip();
            if (var3 == this.m_currentSharedTrackClip) {
               return this.m_currentSharedTrack;
            } else {
               SharedSkeleAnimationTrack var4 = this.m_sharedSkeleAnimationRepo.getTrack(var3);
               if (var4 == null) {
                  DebugLog.Animation.debugln("Caching SharedSkeleAnimationTrack: %s", var1.name);
                  var4 = new SharedSkeleAnimationTrack();
                  ModelTransformSampler var5 = ModelTransformSampler.alloc(this, var1);

                  try {
                     var4.set(var5, 5.0F);
                  } finally {
                     var5.release();
                  }

                  this.m_sharedSkeleAnimationRepo.setTrack(var3, var4);
               }

               this.m_currentSharedTrackClip = var3;
               this.m_currentSharedTrack = var4;
               return var4;
            }
         }
      }
   }

   private void updateAnimation_NonVisualOnly() {
      this.updateMultiTrackBoneTransforms_DeferredMovementOnly();
      this.DoAngles();
      this.calculateDeferredMovement();
   }

   public void setSharedAnimRepo(SharedSkeleAnimationRepository var1) {
      this.m_sharedSkeleAnimationRepo = var1;
   }

   private void updateAnimation_SharedSkeleTrack(SharedSkeleAnimationTrack var1, float var2) {
      this.updateMultiTrackBoneTransforms_DeferredMovementOnly();
      this.DoAngles();
      this.calculateDeferredMovement();
      var1.moveToTime(var2);

      for(int var3 = 0; var3 < this.modelTransforms.length; ++var3) {
         var1.getBoneMatrix(var3, this.modelTransforms[var3]);
      }

      this.UpdateSkinTransforms();
   }

   private void updateAnimation_StandardAnimation() {
      if (this.parentPlayer == null) {
         this.updateMultiTrackBoneTransforms();
      } else {
         this.copyBoneTransformsFromParentPlayer();
      }

      this.DoAngles();
      this.calculateDeferredMovement();
      this.updateTwistBone();
      this.applyBoneReParenting();
      this.updateModelTransforms();
      this.UpdateSkinTransforms();
   }

   private void copyBoneTransformsFromParentPlayer() {
      this.m_boneTransformsNeedFirstFrame = false;

      for(int var1 = 0; var1 < this.m_boneTransforms.length; ++var1) {
         this.m_boneTransforms[var1].set(this.parentPlayer.m_boneTransforms[var1]);
      }

   }

   public static float calculateAnimPlayerAngle(Vector2 var0) {
      return var0.getDirection();
   }

   public void SetDir(Vector2 var1) {
      if (this.m_lastSetDir.x != var1.x || this.m_lastSetDir.y != var1.y) {
         this.setTargetAngle(calculateAnimPlayerAngle(var1));
         this.m_targetTwistAngle = PZMath.getClosestAngle(this.m_angle, this.m_targetAngle);
         float var2 = PZMath.clamp(this.m_targetTwistAngle, -this.m_maxTwistAngle, this.m_maxTwistAngle);
         this.m_excessTwist = PZMath.getClosestAngle(var2, this.m_targetTwistAngle);
         this.m_lastSetDir.set(var1);
      }

   }

   public void SetForceDir(Vector2 var1) {
      this.setTargetAngle(calculateAnimPlayerAngle(var1));
      this.setAngleToTarget();
      this.m_targetTwistAngle = 0.0F;
      this.m_lastSetDir.set(var1);
   }

   public void UpdateDir(IsoGameCharacter var1) {
      if (var1 != null) {
         this.SetDir(var1.getForwardDirection());
      }

   }

   public void DoAngles() {
      GameProfiler.getInstance().invokeAndMeasure("AnimationPlayer.doAngles", this, AnimationPlayer::doAnglesInternal);
   }

   private void doAnglesInternal() {
      float var1 = 0.15F * GameTime.instance.getMultiplier();
      this.interpolateBodyAngle(var1);
      this.interpolateBodyTwist(var1);
      this.interpolateShoulderTwist(var1);
   }

   private void interpolateBodyAngle(float var1) {
      float var2 = PZMath.getClosestAngle(this.m_angle, this.m_targetAngle);
      if (PZMath.equal(var2, 0.0F, 0.001F)) {
         this.setAngleToTarget();
         this.m_targetTwistAngle = 0.0F;
      } else {
         float var3 = (float)PZMath.sign(var2);
         float var4 = var1 * var3 * this.angleStepDelta;
         float var5;
         if (DebugOptions.instance.Character.Debug.Animate.DeferredRotationOnly.getValue()) {
            var5 = this.m_deferredAngleDelta;
         } else if (this.m_deferredRotationWeight > 0.0F) {
            var5 = this.m_deferredAngleDelta;
         } else {
            var5 = var4;
         }

         float var6 = (float)PZMath.sign(var5);
         float var7 = this.m_angle;
         float var8 = var7 + var5;
         float var9 = PZMath.getClosestAngle(var8, this.m_targetAngle);
         float var10 = (float)PZMath.sign(var9);
         if (var10 != var3 && var6 == var3) {
            this.setAngleToTarget();
            this.m_targetTwistAngle = 0.0F;
         } else {
            this.setAngle(var8);
            this.m_targetTwistAngle = var9;
         }

      }
   }

   private void interpolateBodyTwist(float var1) {
      float var2 = PZMath.wrap(this.m_targetTwistAngle, -3.1415927F, 3.1415927F);
      float var3 = PZMath.clamp(var2, -this.m_maxTwistAngle, this.m_maxTwistAngle);
      this.m_excessTwist = PZMath.getClosestAngle(var3, var2);
      float var4 = PZMath.getClosestAngle(this.m_twistAngle, var3);
      if (PZMath.equal(var4, 0.0F, 0.001F)) {
         this.m_twistAngle = var3;
      } else {
         float var5 = (float)PZMath.sign(var4);
         float var6 = var1 * var5 * this.angleTwistDelta;
         float var7 = this.m_twistAngle;
         float var8 = var7 + var6;
         float var9 = PZMath.getClosestAngle(var8, var3);
         float var10 = (float)PZMath.sign(var9);
         if (var10 == var5) {
            this.m_twistAngle = var8;
         } else {
            this.m_twistAngle = var3;
         }

      }
   }

   private void interpolateShoulderTwist(float var1) {
      float var2 = PZMath.wrap(this.m_twistAngle, -3.1415927F, 3.1415927F);
      float var3 = PZMath.getClosestAngle(this.m_shoulderTwistAngle, var2);
      if (PZMath.equal(var3, 0.0F, 0.001F)) {
         this.m_shoulderTwistAngle = var2;
      } else {
         float var4 = (float)PZMath.sign(var3);
         float var5 = var1 * var4 * this.angleTwistDelta * 0.55F;
         float var6 = this.m_shoulderTwistAngle;
         float var7 = var6 + var5;
         float var8 = PZMath.getClosestAngle(var7, var2);
         float var9 = (float)PZMath.sign(var8);
         if (var9 == var4) {
            this.m_shoulderTwistAngle = var7;
         } else {
            this.m_shoulderTwistAngle = var2;
         }

      }
   }

   private void updateTwistBone() {
      GameProfiler.getInstance().invokeAndMeasure("updateTwistBone", this, AnimationPlayer::updateTwistBoneInternal);
   }

   private void updateTwistBoneInternal() {
      if (!this.m_twistBones.isEmpty()) {
         float var1 = PZMath.degToRad(1.0F);
         if (!PZMath.equal(this.m_twistAngle, 0.0F, var1)) {
            if (!DebugOptions.instance.Character.Debug.Animate.NoBoneTwists.getValue()) {
               int var2 = this.m_twistBones.size();
               int var3 = var2 - 1;
               float var4 = -this.m_shoulderTwistAngle;
               float var5 = var4 / (float)var3;

               for(int var6 = 0; var6 < var3; ++var6) {
                  SkinningBone var7 = ((AnimationBoneBinding)this.m_twistBones.get(var6)).getBone();
                  this.applyTwistBone(var7, var5);
               }

               float var9 = -this.m_twistAngle;
               float var10 = PZMath.getClosestAngle(var4, var9);
               if (PZMath.abs(var10) > 1.0E-4F) {
                  SkinningBone var8 = ((AnimationBoneBinding)this.m_twistBones.get(var3)).getBone();
                  this.applyTwistBone(var8, var10);
               }

            }
         }
      }
   }

   private void applyTwistBone(SkinningBone var1, float var2) {
      if (var1 != null) {
         int var3 = var1.Index;
         int var4 = var1.Parent.Index;
         Matrix4f var5 = this.getBoneModelTransform(var4, AnimationPlayer.L_applyTwistBone.twistParentBoneTrans);
         Matrix4f var6 = Matrix4f.invert(var5, AnimationPlayer.L_applyTwistBone.twistParentBoneTransInv);
         if (var6 != null) {
            Matrix4f var7 = this.getBoneModelTransform(var3, AnimationPlayer.L_applyTwistBone.twistBoneTrans);
            Quaternion var8 = AnimationPlayer.L_applyTwistBone.twistBoneTargetRot;
            Matrix4f var9 = AnimationPlayer.L_applyTwistBone.twistRotDiffTrans;
            var9.setIdentity();
            AnimationPlayer.L_applyTwistBone.twistRotDiffTransAxis.set(0.0F, 1.0F, 0.0F);
            float var10 = PZMath.getClosestAngle(this.m_boneTransforms[var3].Twist, var2);
            this.m_boneTransforms[var3].Twist = var2;
            var9.rotate(var10, AnimationPlayer.L_applyTwistBone.twistRotDiffTransAxis);
            Matrix4f var11 = AnimationPlayer.L_applyTwistBone.twistBoneTargetTrans;
            Matrix4f.mul(var7, var9, var11);
            HelperFunctions.getRotation(var11, var8);
            Quaternion var15 = AnimationPlayer.L_applyTwistBone.twistBoneNewRot;
            var15.set(var8);
            Vector3f var13 = HelperFunctions.getPosition(var7, AnimationPlayer.L_applyTwistBone.twistBonePos);
            Vector3f var14 = AnimationPlayer.L_applyTwistBone.twistBoneScale;
            var14.set(1.0F, 1.0F, 1.0F);
            Matrix4f var12 = AnimationPlayer.L_applyTwistBone.twistBoneNewTrans;
            HelperFunctions.CreateFromQuaternionPositionScale(var13, var15, var14, var12);
            this.m_boneTransforms[var3].mul(var12, var6);
         }
      }
   }

   public void resetBoneModelTransforms() {
      if (this.m_skinningData != null && this.modelTransforms != null) {
         this.m_boneTransformsNeedFirstFrame = true;
         int var1 = this.m_boneTransforms.length;

         for(int var2 = 0; var2 < var1; ++var2) {
            this.m_boneTransforms[var2].BlendWeight = 0.0F;
            this.m_boneTransforms[var2].setIdentity();
            this.modelTransforms[var2].setIdentity();
         }

      }
   }

   public boolean isBoneTransformsNeedFirstFrame() {
      return this.m_boneTransformsNeedFirstFrame;
   }

   private void updateMultiTrackBoneTransforms() {
      GameProfiler.getInstance().invokeAndMeasure("updateMultiTrackBoneTransforms", this, AnimationPlayer::updateMultiTrackBoneTransformsInternal);
   }

   private void updateMultiTrackBoneTransformsInternal() {
      int var1;
      for(var1 = 0; var1 < this.modelTransforms.length; ++var1) {
         this.modelTransforms[var1].setIdentity();
      }

      this.updateLayerBlendWeightings();
      if (this.m_totalAnimBlendCount != 0) {
         if (this.isRecording()) {
            this.m_recorder.logAnimWeights(this.m_multiTrack.getTracks(), this.m_animBlendIndices, this.m_animBlendWeights, this.m_deferredMovement);
         }

         for(var1 = 0; var1 < this.m_boneTransforms.length; ++var1) {
            if (!this.isBoneReparented(var1)) {
               this.updateBoneAnimationTransform(var1, (AnimationBoneBindingPair)null);
            }
         }

         this.m_boneTransformsNeedFirstFrame = false;
      }
   }

   private void updateLayerBlendWeightings() {
      List var1 = this.m_multiTrack.getTracks();
      int var2 = var1.size();
      PZArrayUtil.arraySet(this.m_animBlendIndices, -1);
      PZArrayUtil.arraySet(this.m_animBlendWeights, 0.0F);
      PZArrayUtil.arraySet(this.m_animBlendLayers, -1);
      PZArrayUtil.arraySet(this.m_animBlendPriorities, 0);

      int var3;
      float var5;
      int var6;
      int var7;
      for(var3 = 0; var3 < var2; ++var3) {
         AnimationTrack var4 = (AnimationTrack)var1.get(var3);
         var5 = var4.BlendDelta;
         var6 = var4.getLayerIdx();
         var7 = var4.getPriority();
         if (var6 >= 0 && var6 < 4) {
            if (!(var5 < 0.001F) && (var6 <= 0 || !var4.isFinished())) {
               int var8 = -1;

               for(int var9 = 0; var9 < this.m_animBlendIndices.length; ++var9) {
                  if (this.m_animBlendIndices[var9] == -1) {
                     var8 = var9;
                     break;
                  }

                  if (var6 <= this.m_animBlendLayers[var9]) {
                     if (var6 < this.m_animBlendLayers[var9]) {
                        var8 = var9;
                        break;
                     }

                     if (var7 <= this.m_animBlendPriorities[var9]) {
                        if (var7 < this.m_animBlendPriorities[var9]) {
                           var8 = var9;
                           break;
                        }

                        if (var5 < this.m_animBlendWeights[var9]) {
                           var8 = var9;
                           break;
                        }
                     }
                  }
               }

               if (var8 < 0) {
                  DebugLog.General.error("Buffer overflow. Insufficient anim blends in cache. More than %d animations are being blended at once. Will be truncated to %d.", this.m_animBlendIndices.length, this.m_animBlendIndices.length);
               } else {
                  PZArrayUtil.insertAt(this.m_animBlendIndices, var8, var3);
                  PZArrayUtil.insertAt(this.m_animBlendWeights, var8, var5);
                  PZArrayUtil.insertAt(this.m_animBlendLayers, var8, var6);
                  PZArrayUtil.insertAt(this.m_animBlendPriorities, var8, var7);
               }
            }
         } else {
            DebugLog.General.error("Layer index is out of range: %d. Range: 0 - %d", var6, 3);
         }
      }

      PZArrayUtil.arraySet(this.m_layerBlendCounts, 0);
      PZArrayUtil.arraySet(this.m_layerWeightTotals, 0.0F);
      this.m_totalAnimBlendCount = 0;

      int var10;
      float[] var10000;
      for(var3 = 0; var3 < this.m_animBlendIndices.length && this.m_animBlendIndices[var3] >= 0; ++var3) {
         var10 = this.m_animBlendLayers[var3];
         var10000 = this.m_layerWeightTotals;
         var10000[var10] += this.m_animBlendWeights[var3];
         int var10002 = this.m_layerBlendCounts[var10]++;
         ++this.m_totalAnimBlendCount;
      }

      if (this.m_totalAnimBlendCount != 0) {
         if (this.m_boneTransformsNeedFirstFrame) {
            var3 = this.m_animBlendLayers[0];
            var10 = this.m_layerBlendCounts[0];
            var5 = this.m_layerWeightTotals[0];
            if (var5 < 1.0F) {
               for(var6 = 0; var6 < this.m_totalAnimBlendCount; ++var6) {
                  var7 = this.m_animBlendLayers[var6];
                  if (var7 != var3) {
                     break;
                  }

                  if (var5 > 0.0F) {
                     var10000 = this.m_animBlendWeights;
                     var10000[var6] /= var5;
                  } else {
                     this.m_animBlendWeights[var6] = 1.0F / (float)var10;
                  }
               }
            }
         }

      }
   }

   private void calculateDeferredMovement() {
      GameProfiler.getInstance().invokeAndMeasure("calculateDeferredMovement", this, AnimationPlayer::calculateDeferredMovementInternal);
   }

   private void calculateDeferredMovementInternal() {
      List var1 = this.m_multiTrack.getTracks();
      this.m_deferredMovement.set(0.0F, 0.0F);
      this.m_deferredAngleDelta = 0.0F;
      this.m_deferredRotationWeight = 0.0F;
      float var2 = 1.0F;

      for(int var3 = this.m_totalAnimBlendCount - 1; var3 >= 0 && !(var2 <= 0.001F); --var3) {
         int var4 = this.m_animBlendIndices[var3];
         AnimationTrack var5 = (AnimationTrack)var1.get(var4);
         if (!var5.isFinished()) {
            float var6 = var5.getDeferredBoneWeight();
            if (!(var6 <= 0.001F)) {
               float var7 = this.m_animBlendWeights[var3] * var6;
               if (!(var7 <= 0.001F)) {
                  float var8 = PZMath.clamp(var7, 0.0F, var2);
                  var2 -= var7;
                  var2 = Math.max(0.0F, var2);
                  Vector2.addScaled(this.m_deferredMovement, var5.getDeferredMovementDiff(tempo), var8, this.m_deferredMovement);
                  if (var5.getUseDeferredRotation()) {
                     this.m_deferredAngleDelta += var5.getDeferredRotationDiff() * var8;
                     this.m_deferredRotationWeight += var8;
                  }
               }
            }
         }
      }

      this.applyRotationToDeferredMovement(this.m_deferredMovement);
      Vector2 var10000 = this.m_deferredMovement;
      var10000.x *= AdvancedAnimator.s_MotionScale;
      var10000 = this.m_deferredMovement;
      var10000.y *= AdvancedAnimator.s_MotionScale;
      this.m_deferredAngleDelta *= AdvancedAnimator.s_RotationScale;
   }

   private void applyRotationToDeferredMovement(Vector2 var1) {
      float var2 = var1.normalize();
      float var3 = this.getRenderedAngle();
      var1.rotate(var3);
      var1.setLength(-var2);
   }

   private void applyBoneReParenting() {
      GameProfiler.getInstance().invokeAndMeasure("applyBoneReParenting", this, AnimationPlayer::applyBoneReParentingInternal);
   }

   private void applyBoneReParentingInternal() {
      int var1 = 0;

      for(int var2 = this.m_reparentedBoneBindings.size(); var1 < var2; ++var1) {
         AnimationBoneBindingPair var3 = (AnimationBoneBindingPair)this.m_reparentedBoneBindings.get(var1);
         if (!var3.isValid()) {
            DebugLog.Animation.warn("Animation binding pair is not valid: %s", var3);
         } else {
            this.updateBoneAnimationTransform(var3.getBoneIdxA(), var3);
         }
      }

   }

   private void updateBoneAnimationTransform(int var1, AnimationBoneBindingPair var2) {
      this.updateBoneAnimationTransform_Internal(var1, var2);
   }

   private void updateBoneAnimationTransform_Internal(int var1, AnimationBoneBindingPair var2) {
      List var3 = this.m_multiTrack.getTracks();
      Vector3f var4 = AnimationPlayer.L_updateBoneAnimationTransform.pos;
      Quaternion var5 = AnimationPlayer.L_updateBoneAnimationTransform.rot;
      Vector3f var6 = AnimationPlayer.L_updateBoneAnimationTransform.scale;
      Keyframe var7 = AnimationPlayer.L_updateBoneAnimationTransform.key;
      int var8 = this.m_totalAnimBlendCount;
      AnimationBoneBinding var9 = this.m_counterRotationBone;
      boolean var10 = var9 != null && var9.getBone() != null && var9.getBone().Index == var1;
      var7.setIdentity();
      float var11 = 0.0F;
      boolean var12 = true;
      float var13 = 1.0F;

      for(int var14 = var8 - 1; var14 >= 0 && var13 > 0.0F && !(var13 <= 0.001F); --var14) {
         int var15 = this.m_animBlendIndices[var14];
         AnimationTrack var16 = (AnimationTrack)var3.get(var15);
         float var17 = var16.getBoneWeight(var1);
         if (!(var17 <= 0.001F)) {
            float var18 = this.m_animBlendWeights[var14] * var17;
            if (!(var18 <= 0.001F)) {
               float var19 = PZMath.clamp(var18, 0.0F, var13);
               var13 -= var18;
               var13 = Math.max(0.0F, var13);
               this.getTrackTransform(var1, var16, var2, var4, var5, var6);
               if (var10 && var16.getUseDeferredRotation()) {
                  Vector3f var20;
                  if (DebugOptions.instance.Character.Debug.Animate.ZeroCounterRotationBone.getValue()) {
                     var20 = AnimationPlayer.L_updateBoneAnimationTransform.rotAxis;
                     Matrix4f var21 = AnimationPlayer.L_updateBoneAnimationTransform.rotMat;
                     var21.setIdentity();
                     var20.set(0.0F, 1.0F, 0.0F);
                     var21.rotate(-1.5707964F, var20);
                     var20.set(1.0F, 0.0F, 0.0F);
                     var21.rotate(-1.5707964F, var20);
                     HelperFunctions.getRotation(var21, var5);
                  } else {
                     var20 = HelperFunctions.ToEulerAngles(var5, AnimationPlayer.L_updateBoneAnimationTransform.rotEulers);
                     HelperFunctions.ToQuaternion((double)var20.x, (double)var20.y, 1.5707963705062866D, var5);
                  }
               }

               boolean var24 = var16.getDeferredMovementBoneIdx() == var1;
               if (var24) {
                  Vector3f var22 = var16.getCurrentDeferredCounterPosition(AnimationPlayer.L_updateBoneAnimationTransform.deferredPos);
                  var4.x += var22.x;
                  var4.y += var22.y;
                  var4.z += var22.z;
               }

               if (var12) {
                  Vector3.setScaled(var4, var19, var7.Position);
                  var7.Rotation.set(var5);
                  var11 = var19;
                  var12 = false;
               } else {
                  float var23 = var19 / (var19 + var11);
                  var11 += var19;
                  Vector3.addScaled(var7.Position, var4, var19, var7.Position);
                  PZMath.slerp(var7.Rotation, var7.Rotation, var5, var23);
               }
            }
         }
      }

      if (var13 > 0.0F && !this.m_boneTransformsNeedFirstFrame) {
         this.m_boneTransforms[var1].getPRS(var4, var5, var6);
         Vector3.addScaled(var7.Position, var4, var13, var7.Position);
         PZMath.slerp(var7.Rotation, var5, var7.Rotation, var11);
         PZMath.lerp(var7.Scale, var6, var7.Scale, var11);
      }

      this.m_boneTransforms[var1].set(var7.Position, var7.Rotation, var7.Scale);
      this.m_boneTransforms[var1].BlendWeight = var11;
      TwistableBoneTransform var10000 = this.m_boneTransforms[var1];
      var10000.Twist *= 1.0F - var11;
   }

   private void getTrackTransform(int var1, AnimationTrack var2, AnimationBoneBindingPair var3, Vector3f var4, Quaternion var5, Vector3f var6) {
      if (var3 == null) {
         var2.get(var1, var4, var5, var6);
      } else {
         Matrix4f var7 = AnimationPlayer.L_getTrackTransform.result;
         SkinningBone var8 = var3.getBoneA();
         Matrix4f var9 = getUnweightedBoneTransform(var2, var8.Index, AnimationPlayer.L_getTrackTransform.Pa);
         SkinningBone var10 = var8.Parent;
         SkinningBone var11 = var3.getBoneB();
         Matrix4f var12 = this.getBoneModelTransform(var10.Index, AnimationPlayer.L_getTrackTransform.mA);
         Matrix4f var13 = Matrix4f.invert(var12, AnimationPlayer.L_getTrackTransform.mAinv);
         Matrix4f var14 = this.getBoneModelTransform(var11.Index, AnimationPlayer.L_getTrackTransform.mB);
         Matrix4f var15 = this.getUnweightedModelTransform(var2, var10.Index, AnimationPlayer.L_getTrackTransform.umA);
         Matrix4f var16 = this.getUnweightedModelTransform(var2, var11.Index, AnimationPlayer.L_getTrackTransform.umB);
         Matrix4f var17 = Matrix4f.invert(var16, AnimationPlayer.L_getTrackTransform.umBinv);
         Matrix4f.mul(var9, var15, var7);
         Matrix4f.mul(var7, var17, var7);
         Matrix4f.mul(var7, var14, var7);
         Matrix4f.mul(var7, var13, var7);
         HelperFunctions.getPosition(var7, var4);
         HelperFunctions.getRotation(var7, var5);
         var6.set(1.0F, 1.0F, 1.0F);
      }
   }

   public boolean isBoneReparented(int var1) {
      return PZArrayUtil.contains((List)this.m_reparentedBoneBindings, Lambda.predicate(var1, (var0, var1x) -> {
         return var0.getBoneIdxA() == var1x;
      }));
   }

   public void updateMultiTrackBoneTransforms_DeferredMovementOnly() {
      this.m_deferredMovement.set(0.0F, 0.0F);
      if (this.parentPlayer == null) {
         this.updateLayerBlendWeightings();
         if (this.m_totalAnimBlendCount != 0) {
            int[] var1 = AnimationPlayer.updateMultiTrackBoneTransforms_DeferredMovementOnly.boneIndices;
            int var2 = 0;
            List var3 = this.m_multiTrack.getTracks();
            int var4 = var3.size();

            int var5;
            for(var5 = 0; var5 < var4; ++var5) {
               AnimationTrack var6 = (AnimationTrack)var3.get(var5);
               int var7 = var6.getDeferredMovementBoneIdx();
               if (var7 != -1 && !PZArrayUtil.contains(var1, var2, var7)) {
                  var1[var2++] = var7;
               }
            }

            for(var5 = 0; var5 < var2; ++var5) {
               this.updateBoneAnimationTransform(var1[var5], (AnimationBoneBindingPair)null);
            }

         }
      }
   }

   public boolean isRecording() {
      return this.m_recorder != null && this.m_recorder.isRecording();
   }

   public void setRecorder(AnimationPlayerRecorder var1) {
      this.m_recorder = var1;
   }

   public AnimationPlayerRecorder getRecorder() {
      return this.m_recorder;
   }

   public void dismember(int var1) {
      this.dismembered.add(var1);
   }

   private void updateModelTransforms() {
      GameProfiler.getInstance().invokeAndMeasure("updateModelTransforms", this, AnimationPlayer::updateModelTransformsInternal);
   }

   private void updateModelTransformsInternal() {
      this.m_boneTransforms[0].getMatrix(this.modelTransforms[0]);

      for(int var1 = 1; var1 < this.modelTransforms.length; ++var1) {
         SkinningBone var2 = this.m_skinningData.getBoneAt(var1);
         SkinningBone var3 = var2.Parent;
         BoneTransform.mul(this.m_boneTransforms[var2.Index], this.modelTransforms[var3.Index], this.modelTransforms[var2.Index]);
      }

   }

   public Matrix4f getBoneModelTransform(int var1, Matrix4f var2) {
      Matrix4f var3 = AnimationPlayer.L_getBoneModelTransform.boneTransform;
      var2.setIdentity();
      SkinningBone var4 = this.m_skinningData.getBoneAt(var1);

      for(SkinningBone var5 = var4; var5 != null; var5 = var5.Parent) {
         this.getBoneTransform(var5.Index, var3);
         Matrix4f.mul(var2, var3, var2);
      }

      return var2;
   }

   public Matrix4f getBoneTransform(int var1, Matrix4f var2) {
      this.m_boneTransforms[var1].getMatrix(var2);
      return var2;
   }

   public Matrix4f getUnweightedModelTransform(AnimationTrack var1, int var2, Matrix4f var3) {
      Matrix4f var4 = AnimationPlayer.L_getUnweightedModelTransform.boneTransform;
      var4.setIdentity();
      var3.setIdentity();
      SkinningBone var5 = this.m_skinningData.getBoneAt(var2);

      for(SkinningBone var6 = var5; var6 != null; var6 = var6.Parent) {
         getUnweightedBoneTransform(var1, var6.Index, var4);
         Matrix4f.mul(var3, var4, var3);
      }

      return var3;
   }

   public static Matrix4f getUnweightedBoneTransform(AnimationTrack var0, int var1, Matrix4f var2) {
      Vector3f var3 = AnimationPlayer.L_getUnweightedBoneTransform.pos;
      Quaternion var4 = AnimationPlayer.L_getUnweightedBoneTransform.rot;
      Vector3f var5 = AnimationPlayer.L_getUnweightedBoneTransform.scale;
      var0.get(var1, var3, var4, var5);
      HelperFunctions.CreateFromQuaternionPositionScale(var3, var4, var5, var2);
      return var2;
   }

   public void UpdateSkinTransforms() {
      this.resetSkinTransforms();
   }

   public Matrix4f[] getSkinTransforms(SkinningData var1) {
      if (var1 == null) {
         return this.modelTransforms;
      } else {
         AnimationPlayer.SkinTransformData var2 = this.getSkinTransformData(var1);
         Matrix4f[] var3 = var2.transforms;
         if (var2.dirty) {
            for(int var4 = 0; var4 < this.modelTransforms.length; ++var4) {
               if (var1.BoneOffset != null && var1.BoneOffset.get(var4) != null) {
                  Matrix4f.mul((Matrix4f)var1.BoneOffset.get(var4), this.modelTransforms[var4], var3[var4]);
               } else {
                  var3[var4].setIdentity();
               }
            }

            var2.dirty = false;
         }

         return var3;
      }
   }

   public void getDeferredMovement(Vector2 var1) {
      var1.set(this.m_deferredMovement);
   }

   public float getDeferredAngleDelta() {
      return this.m_deferredAngleDelta;
   }

   public float getDeferredRotationWeight() {
      return this.m_deferredRotationWeight;
   }

   public AnimationMultiTrack getMultiTrack() {
      return this.m_multiTrack;
   }

   public void setRecording(boolean var1) {
      this.m_recorder.setRecording(var1);
   }

   public void discardRecording() {
      if (this.m_recorder != null) {
         this.m_recorder.discardRecording();
      }

   }

   public float getRenderedAngle() {
      return this.m_angle + 1.5707964F;
   }

   public float getAngle() {
      return this.m_angle;
   }

   public void setAngle(float var1) {
      this.m_angle = var1;
   }

   public void setAngleToTarget() {
      this.setAngle(this.getTargetAngle());
   }

   public void setTargetToAngle() {
      float var1 = this.getAngle();
      this.setTargetAngle(var1);
   }

   public float getTargetAngle() {
      return this.m_targetAngle;
   }

   public void setTargetAngle(float var1) {
      this.m_targetAngle = var1;
   }

   public float getMaxTwistAngle() {
      return this.m_maxTwistAngle;
   }

   public void setMaxTwistAngle(float var1) {
      this.m_maxTwistAngle = var1;
   }

   public float getExcessTwistAngle() {
      return this.m_excessTwist;
   }

   public float getTwistAngle() {
      return this.m_twistAngle;
   }

   public float getShoulderTwistAngle() {
      return this.m_shoulderTwistAngle;
   }

   public float getTargetTwistAngle() {
      return this.m_targetTwistAngle;
   }

   private static class SkinTransformData extends PooledObject {
      public Matrix4f[] transforms;
      private SkinningData m_skinnedTo;
      public boolean dirty;
      private AnimationPlayer.SkinTransformData m_next;
      private static Pool s_pool = new Pool(AnimationPlayer.SkinTransformData::new);

      public void setSkinnedTo(SkinningData var1) {
         if (this.m_skinnedTo != var1) {
            this.dirty = true;
            this.m_skinnedTo = var1;
            this.transforms = (Matrix4f[])PZArrayUtil.newInstance(Matrix4f.class, this.transforms, var1.numBones(), Matrix4f::new);
         }
      }

      public static AnimationPlayer.SkinTransformData alloc(SkinningData var0) {
         AnimationPlayer.SkinTransformData var1 = (AnimationPlayer.SkinTransformData)s_pool.alloc();
         var1.setSkinnedTo(var0);
         var1.dirty = true;
         return var1;
      }
   }

   private static final class L_setTwistBones {
      static final ArrayList boneNames = new ArrayList();
   }

   private static class L_applyTwistBone {
      static final Matrix4f twistParentBoneTrans = new Matrix4f();
      static final Matrix4f twistParentBoneTransInv = new Matrix4f();
      static final Matrix4f twistBoneTrans = new Matrix4f();
      static final Quaternion twistBoneRot = new Quaternion();
      static final Quaternion twistBoneTargetRot = new Quaternion();
      static final Matrix4f twistRotDiffTrans = new Matrix4f();
      static final Vector3f twistRotDiffTransAxis = new Vector3f(0.0F, 1.0F, 0.0F);
      static final Matrix4f twistBoneTargetTrans = new Matrix4f();
      static final Quaternion twistBoneNewRot = new Quaternion();
      static final Vector3f twistBonePos = new Vector3f();
      static final Vector3f twistBoneScale = new Vector3f();
      static final Matrix4f twistBoneNewTrans = new Matrix4f();
   }

   private static final class L_updateBoneAnimationTransform {
      static final Quaternion rot = new Quaternion();
      static final Vector3f pos = new Vector3f();
      static final Vector3f scale = new Vector3f();
      static final Keyframe key = new Keyframe(new Vector3f(0.0F, 0.0F, 0.0F), new Quaternion(0.0F, 0.0F, 0.0F, 1.0F), new Vector3f(1.0F, 1.0F, 1.0F));
      static final Matrix4f boneMat = new Matrix4f();
      static final Matrix4f rotMat = new Matrix4f();
      static final Vector3f rotAxis = new Vector3f(1.0F, 0.0F, 0.0F);
      static final Quaternion crRot = new Quaternion();
      static final Vector4f crRotAA = new Vector4f();
      static final Matrix4f crMat = new Matrix4f();
      static final Vector3f rotEulers = new Vector3f();
      static final Vector3f deferredPos = new Vector3f();
   }

   private static final class L_getTrackTransform {
      static final Matrix4f Pa = new Matrix4f();
      static final Matrix4f mA = new Matrix4f();
      static final Matrix4f mB = new Matrix4f();
      static final Matrix4f umA = new Matrix4f();
      static final Matrix4f umB = new Matrix4f();
      static final Matrix4f mAinv = new Matrix4f();
      static final Matrix4f umBinv = new Matrix4f();
      static final Matrix4f result = new Matrix4f();
   }

   private static final class updateMultiTrackBoneTransforms_DeferredMovementOnly {
      static int[] boneIndices = new int[60];
   }

   private static class L_getBoneModelTransform {
      static final Matrix4f boneTransform = new Matrix4f();
   }

   private static class L_getUnweightedModelTransform {
      static final Matrix4f boneTransform = new Matrix4f();
   }

   private static class L_getUnweightedBoneTransform {
      static final Vector3f pos = new Vector3f();
      static final Quaternion rot = new Quaternion();
      static final Vector3f scale = new Vector3f();
   }
}
