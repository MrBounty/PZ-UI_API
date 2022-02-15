package zombie.core.skinnedmodel.model.jassimp;

import gnu.trove.list.array.TFloatArrayList;
import jassimp.AiAnimation;
import jassimp.AiBone;
import jassimp.AiBuiltInWrapperProvider;
import jassimp.AiMatrix4f;
import jassimp.AiMesh;
import jassimp.AiNode;
import jassimp.AiNodeAnim;
import jassimp.AiQuaternion;
import jassimp.AiScene;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import zombie.core.skinnedmodel.HelperFunctions;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationClip;
import zombie.core.skinnedmodel.animation.Keyframe;
import zombie.core.skinnedmodel.animation.StaticAnimation;
import zombie.core.skinnedmodel.model.SkinningData;
import zombie.debug.DebugLog;
import zombie.util.StringUtils;

public final class ImportedSkeleton {
   final HashMap boneIndices = new HashMap();
   final ArrayList SkeletonHierarchy = new ArrayList();
   final ArrayList bindPose = new ArrayList();
   final ArrayList invBindPose = new ArrayList();
   final ArrayList skinOffsetMatrices = new ArrayList();
   AiNode rootBoneNode = null;
   final HashMap clips = new HashMap();
   final AiBuiltInWrapperProvider wrapper = new AiBuiltInWrapperProvider();
   final Quaternion end = new Quaternion();

   private ImportedSkeleton() {
   }

   public static ImportedSkeleton process(ImportedSkeletonParams var0) {
      ImportedSkeleton var1 = new ImportedSkeleton();
      var1.processAiScene(var0);
      return var1;
   }

   private void processAiScene(ImportedSkeletonParams var1) {
      AiScene var2 = var1.scene;
      JAssImpImporter.LoadMode var3 = var1.mode;
      SkinningData var4 = var1.skinnedTo;
      float var5 = var1.animBonesScaleModifier;
      Quaternion var6 = var1.animBonesRotateModifier;
      AiMesh var7 = var1.mesh;
      AiNode var8 = (AiNode)var2.getSceneRoot(this.wrapper);
      this.rootBoneNode = JAssImpImporter.FindNode("Dummy01", var8);
      boolean var9;
      if (this.rootBoneNode == null) {
         this.rootBoneNode = JAssImpImporter.FindNode("VehicleSkeleton", var8);
         var9 = true;
      } else {
         var9 = false;
      }

      while(this.rootBoneNode != null && this.rootBoneNode.getParent() != null && this.rootBoneNode.getParent() != var8) {
         this.rootBoneNode = this.rootBoneNode.getParent();
      }

      if (this.rootBoneNode == null) {
         this.rootBoneNode = var8;
      }

      ArrayList var10 = new ArrayList();
      JAssImpImporter.CollectBoneNodes(var10, this.rootBoneNode);
      AiNode var11 = JAssImpImporter.FindNode("Translation_Data", var8);
      if (var11 != null) {
         var10.add(var11);

         for(AiNode var12 = var11.getParent(); var12 != null && var12 != var8; var12 = var12.getParent()) {
            var10.add(var12);
         }
      }

      if (var4 != null) {
         this.boneIndices.putAll(var4.BoneIndices);
         this.SkeletonHierarchy.addAll(var4.SkeletonHierarchy);
      }

      int var15;
      for(int var27 = 0; var27 < var10.size(); ++var27) {
         AiNode var13 = (AiNode)var10.get(var27);
         String var14 = var13.getName();
         if (!this.boneIndices.containsKey(var14)) {
            var15 = this.boneIndices.size();
            this.boneIndices.put(var14, var15);
            if (var13 == this.rootBoneNode) {
               this.SkeletonHierarchy.add(-1);
            } else {
               AiNode var16;
               for(var16 = var13.getParent(); var16 != null && !this.boneIndices.containsKey(var16.getName()); var16 = var16.getParent()) {
               }

               if (var16 != null) {
                  this.SkeletonHierarchy.add((Integer)this.boneIndices.get(var16.getName()));
               } else {
                  this.SkeletonHierarchy.add(0);
               }
            }
         }
      }

      Matrix4f var28 = new Matrix4f();

      for(int var29 = 0; var29 < this.boneIndices.size(); ++var29) {
         this.bindPose.add(var28);
         this.skinOffsetMatrices.add(var28);
      }

      List var30 = var7.getBones();

      int var31;
      for(var31 = 0; var31 < var10.size(); ++var31) {
         AiNode var32 = (AiNode)var10.get(var31);
         String var33 = var32.getName();
         AiBone var17 = JAssImpImporter.FindAiBone(var33, var30);
         if (var17 != null) {
            AiMatrix4f var18 = (AiMatrix4f)var17.getOffsetMatrix(this.wrapper);
            if (var18 != null) {
               Matrix4f var19 = JAssImpImporter.getMatrixFromAiMatrix(var18);
               Matrix4f var20 = new Matrix4f(var19);
               var20.invert();
               Matrix4f var21 = new Matrix4f();
               var21.setIdentity();
               String var22 = var32.getParent().getName();
               AiBone var23 = JAssImpImporter.FindAiBone(var22, var30);
               if (var23 != null) {
                  AiMatrix4f var24 = (AiMatrix4f)var23.getOffsetMatrix(this.wrapper);
                  if (var24 != null) {
                     JAssImpImporter.getMatrixFromAiMatrix(var24, var21);
                  }
               }

               Matrix4f var38 = new Matrix4f(var21);
               var38.invert();
               Matrix4f var25 = new Matrix4f();
               Matrix4f.mul(var20, var38, var25);
               var25.invert();
               int var26 = (Integer)this.boneIndices.get(var33);
               this.bindPose.set(var26, var25);
               this.skinOffsetMatrices.set(var26, var19);
            }
         }
      }

      var31 = this.bindPose.size();

      for(var15 = 0; var15 < var31; ++var15) {
         Matrix4f var34 = new Matrix4f((Matrix4f)this.bindPose.get(var15));
         var34.invert();
         this.invBindPose.add(var15, var34);
      }

      if (var3 == JAssImpImporter.LoadMode.AnimationOnly || var4 == null) {
         var15 = var2.getNumAnimations();
         if (var15 > 0) {
            List var35 = var2.getAnimations();

            for(int var36 = 0; var36 < var15; ++var36) {
               AiAnimation var37 = (AiAnimation)var35.get(var36);
               if (var9) {
                  this.processAnimation(var37, var9, 1.0F, (Quaternion)null);
               } else {
                  this.processAnimation(var37, var9, var5, var6);
               }
            }

         }
      }
   }

   /** @deprecated */
   @Deprecated
   void processAnimationOld(AiAnimation var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      float var4 = (float)var1.getDuration();
      float var5 = var4 / (float)var1.getTicksPerSecond();
      ArrayList var6 = new ArrayList();
      List var7 = var1.getChannels();

      int var8;
      for(var8 = 0; var8 < var7.size(); ++var8) {
         AiNodeAnim var9 = (AiNodeAnim)var7.get(var8);

         int var10;
         float var11;
         for(var10 = 0; var10 < var9.getNumPosKeys(); ++var10) {
            var11 = (float)var9.getPosKeyTime(var10);
            if (!var6.contains(var11)) {
               var6.add(var11);
            }
         }

         for(var10 = 0; var10 < var9.getNumRotKeys(); ++var10) {
            var11 = (float)var9.getRotKeyTime(var10);
            if (!var6.contains(var11)) {
               var6.add(var11);
            }
         }

         for(var10 = 0; var10 < var9.getNumScaleKeys(); ++var10) {
            var11 = (float)var9.getScaleKeyTime(var10);
            if (!var6.contains(var11)) {
               var6.add(var11);
            }
         }
      }

      Collections.sort(var6);

      int var14;
      for(var8 = 0; var8 < var6.size(); ++var8) {
         for(var14 = 0; var14 < var7.size(); ++var14) {
            AiNodeAnim var15 = (AiNodeAnim)var7.get(var14);
            Keyframe var16 = new Keyframe();
            var16.clear();
            var16.BoneName = var15.getNodeName();
            Integer var12 = (Integer)this.boneIndices.get(var16.BoneName);
            if (var12 == null) {
               DebugLog.General.error("Could not find bone index for node name: \"%s\"", var16.BoneName);
            } else {
               var16.Bone = var12;
               var16.Time = (Float)var6.get(var8) / (float)var1.getTicksPerSecond();
               if (!var2) {
                  var16.Position = JAssImpImporter.GetKeyFramePosition(var15, (Float)var6.get(var8));
                  var16.Rotation = JAssImpImporter.GetKeyFrameRotation(var15, (Float)var6.get(var8));
                  var16.Scale = JAssImpImporter.GetKeyFrameScale(var15, (Float)var6.get(var8));
               } else {
                  var16.Position = this.GetKeyFramePosition(var15, (Float)var6.get(var8), var1.getDuration());
                  var16.Rotation = this.GetKeyFrameRotation(var15, (Float)var6.get(var8), var1.getDuration());
                  var16.Scale = this.GetKeyFrameScale(var15, (Float)var6.get(var8), var1.getDuration());
               }

               if (var16.Bone >= 0) {
                  var3.add(var16);
               }
            }
         }
      }

      String var13 = var1.getName();
      var14 = var13.indexOf(124);
      if (var14 > 0) {
         var13 = var13.substring(var14 + 1);
      }

      AnimationClip var17 = new AnimationClip(var5, var3, var13, true);
      var3.clear();
      if (ModelManager.instance.bCreateSoftwareMeshes) {
         var17.staticClip = new StaticAnimation(var17);
      }

      this.clips.put(var13, var17);
   }

   private void processAnimation(AiAnimation var1, boolean var2, float var3, Quaternion var4) {
      ArrayList var5 = new ArrayList();
      float var6 = (float)var1.getDuration();
      float var7 = var6 / (float)var1.getTicksPerSecond();
      TFloatArrayList[] var8 = new TFloatArrayList[this.boneIndices.size()];
      Arrays.fill(var8, (Object)null);
      ArrayList var9 = new ArrayList(this.boneIndices.size());

      for(int var10 = 0; var10 < this.boneIndices.size(); ++var10) {
         var9.add((Object)null);
      }

      this.collectBoneFrames(var1, var8, var9);
      Quaternion var24 = null;
      boolean var11 = var4 != null;
      if (var11) {
         var24 = new Quaternion();
         Quaternion.mulInverse(var24, var4, var24);
      }

      for(int var12 = 0; var12 < this.boneIndices.size(); ++var12) {
         ArrayList var13 = (ArrayList)var9.get(var12);
         if (var13 == null) {
            if (var12 == 0 && var4 != null) {
               Quaternion var27 = new Quaternion();
               var27.set(var4);
               this.addDefaultAnimTrack("RootNode", var12, var27, new Vector3f(0.0F, 0.0F, 0.0F), var5, var7);
            }
         } else {
            TFloatArrayList var14 = var8[var12];
            if (var14 != null) {
               var14.sort();
               int var15 = this.getParentBoneIdx(var12);
               boolean var16 = var11 && (var15 == 0 || this.doesParentBoneHaveAnimFrames(var8, var9, var12));

               for(int var17 = 0; var17 < var14.size(); ++var17) {
                  float var18 = var14.get(var17);
                  float var19 = var18 / (float)var1.getTicksPerSecond();

                  for(int var20 = 0; var20 < var13.size(); ++var20) {
                     AiNodeAnim var21 = (AiNodeAnim)var13.get(var20);
                     Keyframe var22 = new Keyframe();
                     var22.clear();
                     var22.BoneName = var21.getNodeName();
                     var22.Bone = var12;
                     var22.Time = var19;
                     if (!var2) {
                        var22.Position = JAssImpImporter.GetKeyFramePosition(var21, var18);
                        var22.Rotation = JAssImpImporter.GetKeyFrameRotation(var21, var18);
                        var22.Scale = JAssImpImporter.GetKeyFrameScale(var21, var18);
                     } else {
                        var22.Position = this.GetKeyFramePosition(var21, var18, (double)var6);
                        var22.Rotation = this.GetKeyFrameRotation(var21, var18, (double)var6);
                        var22.Scale = this.GetKeyFrameScale(var21, var18, (double)var6);
                     }

                     Vector3f var10000 = var22.Position;
                     var10000.x *= var3;
                     var10000 = var22.Position;
                     var10000.y *= var3;
                     var10000 = var22.Position;
                     var10000.z *= var3;
                     if (var11) {
                        if (var16) {
                           Quaternion.mul(var24, var22.Rotation, var22.Rotation);
                           boolean var23 = StringUtils.startsWithIgnoreCase(var22.BoneName, "Translation_Data");
                           if (!var23) {
                              HelperFunctions.transform(var24, var22.Position, var22.Position);
                           }
                        }

                        Quaternion.mul(var22.Rotation, var4, var22.Rotation);
                     }

                     var5.add(var22);
                  }
               }
            }
         }
      }

      String var25 = var1.getName();
      int var26 = var25.indexOf(124);
      if (var26 > 0) {
         var25 = var25.substring(var26 + 1);
      }

      var25 = var25.trim();
      AnimationClip var28 = new AnimationClip(var7, var5, var25, true);
      var5.clear();
      if (ModelManager.instance.bCreateSoftwareMeshes) {
         var28.staticClip = new StaticAnimation(var28);
      }

      this.clips.put(var25, var28);
   }

   private void addDefaultAnimTrack(String var1, int var2, Quaternion var3, Vector3f var4, ArrayList var5, float var6) {
      Vector3f var7 = new Vector3f(1.0F, 1.0F, 1.0F);
      Keyframe var8 = new Keyframe();
      var8.clear();
      var8.BoneName = var1;
      var8.Bone = var2;
      var8.Time = 0.0F;
      var8.Position = var4;
      var8.Rotation = var3;
      var8.Scale = var7;
      var5.add(var8);
      Keyframe var9 = new Keyframe();
      var9.clear();
      var9.BoneName = var1;
      var9.Bone = var2;
      var9.Time = var6;
      var9.Position = var4;
      var9.Rotation = var3;
      var9.Scale = var7;
      var5.add(var9);
   }

   private boolean doesParentBoneHaveAnimFrames(TFloatArrayList[] var1, ArrayList var2, int var3) {
      int var4 = this.getParentBoneIdx(var3);
      return var4 < 0 ? false : this.doesBoneHaveAnimFrames(var1, var2, var4);
   }

   private boolean doesBoneHaveAnimFrames(TFloatArrayList[] var1, ArrayList var2, int var3) {
      TFloatArrayList var4 = var1[var3];
      if (var4 != null && var4.size() > 0) {
         ArrayList var5 = (ArrayList)var2.get(var3);
         return var5.size() > 0;
      } else {
         return false;
      }
   }

   private void collectBoneFrames(AiAnimation var1, TFloatArrayList[] var2, ArrayList var3) {
      List var4 = var1.getChannels();

      for(int var5 = 0; var5 < var4.size(); ++var5) {
         AiNodeAnim var6 = (AiNodeAnim)var4.get(var5);
         String var7 = var6.getNodeName();
         Integer var8 = (Integer)this.boneIndices.get(var7);
         if (var8 == null) {
            DebugLog.General.error("Could not find bone index for node name: \"%s\"", var7);
         } else {
            ArrayList var9 = (ArrayList)var3.get(var8);
            if (var9 == null) {
               var9 = new ArrayList();
               var3.set(var8, var9);
            }

            var9.add(var6);
            TFloatArrayList var10 = var2[var8];
            if (var10 == null) {
               var10 = new TFloatArrayList();
               var2[var8] = var10;
            }

            int var11;
            float var12;
            for(var11 = 0; var11 < var6.getNumPosKeys(); ++var11) {
               var12 = (float)var6.getPosKeyTime(var11);
               if (!var10.contains(var12)) {
                  var10.add(var12);
               }
            }

            for(var11 = 0; var11 < var6.getNumRotKeys(); ++var11) {
               var12 = (float)var6.getRotKeyTime(var11);
               if (!var10.contains(var12)) {
                  var10.add(var12);
               }
            }

            for(var11 = 0; var11 < var6.getNumScaleKeys(); ++var11) {
               var12 = (float)var6.getScaleKeyTime(var11);
               if (!var10.contains(var12)) {
                  var10.add(var12);
               }
            }
         }
      }

   }

   private int getParentBoneIdx(int var1) {
      return var1 > -1 ? (Integer)this.SkeletonHierarchy.get(var1) : -1;
   }

   public int getNumBoneAncestors(int var1) {
      int var2 = 0;

      for(int var3 = this.getParentBoneIdx(var1); var3 > -1; var3 = this.getParentBoneIdx(var3)) {
         ++var2;
      }

      return var2;
   }

   private Vector3f GetKeyFramePosition(AiNodeAnim var1, float var2, double var3) {
      Vector3f var5 = new Vector3f();
      if (var1.getNumPosKeys() == 0) {
         return var5;
      } else {
         int var6;
         for(var6 = 0; var6 < var1.getNumPosKeys() - 1 && !((double)var2 < var1.getPosKeyTime(var6 + 1)); ++var6) {
         }

         int var7 = (var6 + 1) % var1.getNumPosKeys();
         float var8 = (float)var1.getPosKeyTime(var6);
         float var9 = (float)var1.getPosKeyTime(var7);
         float var10 = var9 - var8;
         if (var10 < 0.0F) {
            var10 = (float)((double)var10 + var3);
         }

         if (var10 > 0.0F) {
            float var11 = var9 - var8;
            float var12 = var2 - var8;
            var12 /= var11;
            float var13 = var1.getPosKeyX(var6);
            float var14 = var1.getPosKeyX(var7);
            float var15 = var13 + var12 * (var14 - var13);
            float var16 = var1.getPosKeyY(var6);
            float var17 = var1.getPosKeyY(var7);
            float var18 = var16 + var12 * (var17 - var16);
            float var19 = var1.getPosKeyZ(var6);
            float var20 = var1.getPosKeyZ(var7);
            float var21 = var19 + var12 * (var20 - var19);
            var5.set(var15, var18, var21);
         } else {
            var5.set(var1.getPosKeyX(var6), var1.getPosKeyY(var6), var1.getPosKeyZ(var6));
         }

         return var5;
      }
   }

   private Quaternion GetKeyFrameRotation(AiNodeAnim var1, float var2, double var3) {
      Quaternion var5 = new Quaternion();
      if (var1.getNumRotKeys() == 0) {
         return var5;
      } else {
         int var6;
         for(var6 = 0; var6 < var1.getNumRotKeys() - 1 && !((double)var2 < var1.getRotKeyTime(var6 + 1)); ++var6) {
         }

         int var7 = (var6 + 1) % var1.getNumRotKeys();
         float var8 = (float)var1.getRotKeyTime(var6);
         float var9 = (float)var1.getRotKeyTime(var7);
         float var10 = var9 - var8;
         if (var10 < 0.0F) {
            var10 = (float)((double)var10 + var3);
         }

         float var11;
         if (var10 > 0.0F) {
            var11 = (var2 - var8) / var10;
            AiQuaternion var12 = (AiQuaternion)var1.getRotKeyQuaternion(var6, this.wrapper);
            AiQuaternion var13 = (AiQuaternion)var1.getRotKeyQuaternion(var7, this.wrapper);
            double var14 = (double)(var12.getX() * var13.getX() + var12.getY() * var13.getY() + var12.getZ() * var13.getZ() + var12.getW() * var13.getW());
            this.end.set(var13.getX(), var13.getY(), var13.getZ(), var13.getW());
            if (var14 < 0.0D) {
               var14 *= -1.0D;
               this.end.setX(-this.end.getX());
               this.end.setY(-this.end.getY());
               this.end.setZ(-this.end.getZ());
               this.end.setW(-this.end.getW());
            }

            double var16;
            double var18;
            if (1.0D - var14 > 1.0E-4D) {
               double var20 = Math.acos(var14);
               double var22 = Math.sin(var20);
               var16 = Math.sin((1.0D - (double)var11) * var20) / var22;
               var18 = Math.sin((double)var11 * var20) / var22;
            } else {
               var16 = 1.0D - (double)var11;
               var18 = (double)var11;
            }

            var5.set((float)(var16 * (double)var12.getX() + var18 * (double)this.end.getX()), (float)(var16 * (double)var12.getY() + var18 * (double)this.end.getY()), (float)(var16 * (double)var12.getZ() + var18 * (double)this.end.getZ()), (float)(var16 * (double)var12.getW() + var18 * (double)this.end.getW()));
         } else {
            var11 = var1.getRotKeyX(var6);
            float var24 = var1.getRotKeyY(var6);
            float var25 = var1.getRotKeyZ(var6);
            float var26 = var1.getRotKeyW(var6);
            var5.set(var11, var24, var25, var26);
         }

         return var5;
      }
   }

   private Vector3f GetKeyFrameScale(AiNodeAnim var1, float var2, double var3) {
      Vector3f var5 = new Vector3f(1.0F, 1.0F, 1.0F);
      if (var1.getNumScaleKeys() == 0) {
         return var5;
      } else {
         int var6;
         for(var6 = 0; var6 < var1.getNumScaleKeys() - 1 && !((double)var2 < var1.getScaleKeyTime(var6 + 1)); ++var6) {
         }

         var5.set(var1.getScaleKeyX(var6), var1.getScaleKeyY(var6), var1.getScaleKeyZ(var6));
         return var5;
      }
   }
}
