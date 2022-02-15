package zombie.core.skinnedmodel.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import zombie.core.opengl.RenderThread;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.animation.AnimationClip;
import zombie.core.skinnedmodel.animation.Keyframe;
import zombie.core.skinnedmodel.animation.StaticAnimation;
import zombie.util.SharedStrings;

public final class ModelLoader {
   public static final ModelLoader instance = new ModelLoader();
   private final ThreadLocal sharedStrings = ThreadLocal.withInitial(SharedStrings::new);

   protected ModelTxt loadTxt(String var1, boolean var2, boolean var3, SkinningData var4) throws IOException {
      ModelTxt var5 = new ModelTxt();
      var5.bStatic = var2;
      var5.bReverse = var3;
      VertexBufferObject.VertexFormat var6 = new VertexBufferObject.VertexFormat(var2 ? 4 : 6);
      var6.setElement(0, VertexBufferObject.VertexType.VertexArray, 12);
      var6.setElement(1, VertexBufferObject.VertexType.NormalArray, 12);
      var6.setElement(2, VertexBufferObject.VertexType.TangentArray, 12);
      var6.setElement(3, VertexBufferObject.VertexType.TextureCoordArray, 8);
      if (!var2) {
         var6.setElement(4, VertexBufferObject.VertexType.BlendWeightArray, 16);
         var6.setElement(5, VertexBufferObject.VertexType.BlendIndexArray, 16);
      }

      var6.calculate();
      FileReader var7 = new FileReader(var1);

      try {
         BufferedReader var8 = new BufferedReader(var7);

         try {
            SharedStrings var9 = (SharedStrings)this.sharedStrings.get();
            ModelLoader.LoadMode var10 = ModelLoader.LoadMode.Version;
            String var11 = null;
            int var12 = 0;
            int var13 = 0;
            int var14 = 0;
            boolean var15 = false;
            int var16 = 0;
            boolean var17 = false;

            label223:
            while(true) {
               while(true) {
                  int var21;
                  int var22;
                  int var46;
                  int var49;
                  do {
                     if ((var11 = var8.readLine()) == null) {
                        if (var2 || var4 == null) {
                           break label223;
                        }

                        try {
                           int[] var51 = new int[var5.boneIndices.size()];
                           ArrayList var54 = var5.SkeletonHierarchy;
                           HashMap var52 = var5.boneIndices;
                           HashMap var56 = new HashMap(var4.BoneIndices);
                           ArrayList var59 = new ArrayList(var4.SkeletonHierarchy);
                           var52.forEach((var4x, var5x) -> {
                              int var6 = (Integer)var56.getOrDefault(var4x, -1);
                              if (var6 == -1) {
                                 var6 = var56.size();
                                 var56.put(var4x, var6);
                                 int var7 = (Integer)var54.get(var5x);
                                 if (var7 >= 0) {
                                    var59.add(var51[var7]);
                                 } else {
                                    boolean var8 = true;
                                 }
                              }

                              var51[var5x] = var6;
                           });
                           var5.boneIndices = var56;
                           var5.SkeletonHierarchy = var59;

                           int var60;
                           for(var46 = 0; var46 < var5.vertices.m_numVertices; ++var46) {
                              var49 = (int)var5.vertices.getElementFloat(var46, 5, 0);
                              var21 = (int)var5.vertices.getElementFloat(var46, 5, 1);
                              var22 = (int)var5.vertices.getElementFloat(var46, 5, 2);
                              var60 = (int)var5.vertices.getElementFloat(var46, 5, 3);
                              if (var49 >= 0) {
                                 var49 = var51[var49];
                              }

                              if (var21 >= 0) {
                                 var21 = var51[var21];
                              }

                              if (var22 >= 0) {
                                 var22 = var51[var22];
                              }

                              if (var60 >= 0) {
                                 var60 = var51[var60];
                              }

                              var5.vertices.setElement(var46, 5, (float)var49, (float)var21, (float)var22, (float)var60);
                           }

                           Iterator var62 = var5.clips.values().iterator();

                           while(var62.hasNext()) {
                              AnimationClip var61 = (AnimationClip)var62.next();
                              Keyframe[] var64 = var61.getKeyframes();
                              var22 = var64.length;

                              for(var60 = 0; var60 < var22; ++var60) {
                                 Keyframe var65 = var64[var60];
                                 var65.Bone = var51[var65.Bone];
                              }
                           }

                           var5.skinOffsetMatrices = this.RemapMatrices(var51, var5.skinOffsetMatrices, var5.boneIndices.size());
                           var5.bindPose = this.RemapMatrices(var51, var5.bindPose, var5.boneIndices.size());
                           var5.invBindPose = this.RemapMatrices(var51, var5.invBindPose, var5.boneIndices.size());
                        } catch (Exception var41) {
                           var41.toString();
                        }
                        break label223;
                     }
                  } while(var11.indexOf(35) == 0);

                  if (var11.contains("Tangent")) {
                     if (var2) {
                        var12 += 2;
                     }

                     var17 = true;
                  }

                  if (var12 > 0) {
                     --var12;
                  } else {
                     String var19;
                     float var20;
                     float var26;
                     int var45;
                     String var47;
                     String[] var48;
                     String var50;
                     switch(var10) {
                     case Version:
                        var10 = ModelLoader.LoadMode.ModelName;
                        break;
                     case ModelName:
                        var10 = ModelLoader.LoadMode.VertexStrideElementCount;
                        break;
                     case VertexStrideElementCount:
                        var10 = ModelLoader.LoadMode.VertexCount;
                        if (var2) {
                           var12 = 7;
                        } else {
                           var12 = 13;
                        }
                        break;
                     case VertexCount:
                        var13 = Integer.parseInt(var11);
                        var10 = ModelLoader.LoadMode.VertexBuffer;
                        var5.vertices = new VertexBufferObject.VertexArray(var6, var13);
                        break;
                     case VertexBuffer:
                        var45 = 0;

                        for(; var45 < var13; ++var45) {
                           var48 = var11.split(",");
                           var20 = Float.parseFloat(var48[0].trim());
                           float var55 = Float.parseFloat(var48[1].trim());
                           float var57 = Float.parseFloat(var48[2].trim());
                           var11 = var8.readLine();
                           var48 = var11.split(",");
                           float var58 = Float.parseFloat(var48[0].trim());
                           float var63 = Float.parseFloat(var48[1].trim());
                           float var66 = Float.parseFloat(var48[2].trim());
                           var26 = 0.0F;
                           float var67 = 0.0F;
                           float var68 = 0.0F;
                           if (var17) {
                              var11 = var8.readLine();
                              var48 = var11.split(",");
                              var26 = Float.parseFloat(var48[0].trim());
                              var67 = Float.parseFloat(var48[1].trim());
                              var68 = Float.parseFloat(var48[2].trim());
                           }

                           var11 = var8.readLine();
                           var48 = var11.split(",");
                           float var69 = Float.parseFloat(var48[0].trim());
                           float var30 = Float.parseFloat(var48[1].trim());
                           float var31 = 0.0F;
                           float var32 = 0.0F;
                           float var33 = 0.0F;
                           float var34 = 0.0F;
                           int var35 = 0;
                           int var36 = 0;
                           int var37 = 0;
                           int var38 = 0;
                           if (!var2) {
                              var11 = var8.readLine();
                              var48 = var11.split(",");
                              var31 = Float.parseFloat(var48[0].trim());
                              var32 = Float.parseFloat(var48[1].trim());
                              var33 = Float.parseFloat(var48[2].trim());
                              var34 = Float.parseFloat(var48[3].trim());
                              var11 = var8.readLine();
                              var48 = var11.split(",");
                              var35 = Integer.parseInt(var48[0].trim());
                              var36 = Integer.parseInt(var48[1].trim());
                              var37 = Integer.parseInt(var48[2].trim());
                              var38 = Integer.parseInt(var48[3].trim());
                           }

                           var11 = var8.readLine();
                           var5.vertices.setElement(var45, 0, var20, var55, var57);
                           var5.vertices.setElement(var45, 1, var58, var63, var66);
                           var5.vertices.setElement(var45, 2, var26, var67, var68);
                           var5.vertices.setElement(var45, 3, var69, var30);
                           if (!var2) {
                              var5.vertices.setElement(var45, 4, var31, var32, var33, var34);
                              var5.vertices.setElement(var45, 5, (float)var35, (float)var36, (float)var37, (float)var38);
                           }
                        }

                        var10 = ModelLoader.LoadMode.NumberOfFaces;
                        break;
                     case NumberOfFaces:
                        var14 = Integer.parseInt(var11);
                        var5.elements = new int[var14 * 3];
                        var10 = ModelLoader.LoadMode.FaceData;
                        break;
                     case FaceData:
                        for(var45 = 0; var45 < var14; ++var45) {
                           var48 = var11.split(",");
                           var49 = Integer.parseInt(var48[0].trim());
                           var21 = Integer.parseInt(var48[1].trim());
                           var22 = Integer.parseInt(var48[2].trim());
                           if (var3) {
                              var5.elements[var45 * 3 + 2] = var49;
                              var5.elements[var45 * 3 + 1] = var21;
                              var5.elements[var45 * 3 + 0] = var22;
                           } else {
                              var5.elements[var45 * 3 + 0] = var49;
                              var5.elements[var45 * 3 + 1] = var21;
                              var5.elements[var45 * 3 + 2] = var22;
                           }

                           var11 = var8.readLine();
                        }

                        var10 = ModelLoader.LoadMode.NumberOfBones;
                        break;
                     case NumberOfBones:
                        var16 = Integer.parseInt(var11);
                        var10 = ModelLoader.LoadMode.SkeletonHierarchy;
                        break;
                     case SkeletonHierarchy:
                        for(var45 = 0; var45 < var16; ++var45) {
                           var46 = Integer.parseInt(var11);
                           var11 = var8.readLine();
                           var49 = Integer.parseInt(var11);
                           var11 = var8.readLine();
                           var50 = var9.get(var11);
                           var11 = var8.readLine();
                           var5.SkeletonHierarchy.add(var49);
                           var5.boneIndices.put(var50, var46);
                        }

                        var10 = ModelLoader.LoadMode.BindPose;
                        break;
                     case BindPose:
                        for(var45 = 0; var45 < var16; ++var45) {
                           var11 = var8.readLine();
                           var19 = var8.readLine();
                           var47 = var8.readLine();
                           var50 = var8.readLine();
                           var5.bindPose.add(var45, this.getMatrix(var11, var19, var47, var50));
                           var11 = var8.readLine();
                        }

                        var10 = ModelLoader.LoadMode.InvBindPose;
                        break;
                     case InvBindPose:
                        for(var45 = 0; var45 < var16; ++var45) {
                           var11 = var8.readLine();
                           var19 = var8.readLine();
                           var47 = var8.readLine();
                           var50 = var8.readLine();
                           var5.invBindPose.add(var45, this.getMatrix(var11, var19, var47, var50));
                           var11 = var8.readLine();
                        }

                        var10 = ModelLoader.LoadMode.SkinOffsetMatrices;
                        break;
                     case SkinOffsetMatrices:
                        for(var45 = 0; var45 < var16; ++var45) {
                           var11 = var8.readLine();
                           var19 = var8.readLine();
                           var47 = var8.readLine();
                           var50 = var8.readLine();
                           var5.skinOffsetMatrices.add(var45, this.getMatrix(var11, var19, var47, var50));
                           var11 = var8.readLine();
                        }

                        var10 = ModelLoader.LoadMode.NumberOfAnims;
                        break;
                     case NumberOfAnims:
                        int var44 = Integer.parseInt(var11);
                        var10 = ModelLoader.LoadMode.Anim;
                        break;
                     case Anim:
                        ArrayList var18 = new ArrayList();
                        var19 = var11;
                        var11 = var8.readLine();
                        var20 = Float.parseFloat(var11);
                        var11 = var8.readLine();
                        var21 = Integer.parseInt(var11);
                        var11 = var8.readLine();

                        for(var22 = 0; var22 < var21; ++var22) {
                           Keyframe var23 = new Keyframe();
                           int var24 = Integer.parseInt(var11);
                           var11 = var8.readLine();
                           String var25 = var9.get(var11);
                           var11 = var8.readLine();
                           var26 = Float.parseFloat(var11);
                           var11 = var8.readLine();
                           String var27 = var8.readLine();
                           Vector3f var28 = this.getVector(var11);
                           Quaternion var29 = this.getQuaternion(var27);
                           if (var22 < var21 - 1) {
                              var11 = var8.readLine();
                           }

                           var23.Bone = var24;
                           var23.BoneName = var25;
                           var23.Time = var26;
                           var23.Rotation = var29;
                           var23.Position = new Vector3f(var28);
                           var18.add(var23);
                        }

                        AnimationClip var53 = new AnimationClip(var20, var18, var19, false);
                        var18.clear();
                        if (ModelManager.instance.bCreateSoftwareMeshes) {
                           var53.staticClip = new StaticAnimation(var53);
                        }

                        var5.clips.put(var19, var53);
                     }
                  }
               }
            }
         } catch (Throwable var42) {
            try {
               var8.close();
            } catch (Throwable var40) {
               var42.addSuppressed(var40);
            }

            throw var42;
         }

         var8.close();
      } catch (Throwable var43) {
         try {
            var7.close();
         } catch (Throwable var39) {
            var43.addSuppressed(var39);
         }

         throw var43;
      }

      var7.close();
      return var5;
   }

   protected void applyToMesh(ModelTxt var1, ModelMesh var2, SkinningData var3) {
      if (var1.bStatic) {
         if (!ModelManager.NoOpenGL) {
            RenderThread.queueInvokeOnRenderContext(() -> {
               var2.SetVertexBuffer(new VertexBufferObject(var1.vertices, var1.elements));
               if (ModelManager.instance.bCreateSoftwareMeshes) {
                  var2.softwareMesh.vb = var2.vb;
               }

            });
         }
      } else {
         var2.skinningData = new SkinningData(var1.clips, var1.bindPose, var1.invBindPose, var1.skinOffsetMatrices, var1.SkeletonHierarchy, var1.boneIndices);
         if (!ModelManager.NoOpenGL) {
            RenderThread.queueInvokeOnRenderContext(() -> {
               var2.SetVertexBuffer(new VertexBufferObject(var1.vertices, var1.elements, var1.bReverse));
               if (ModelManager.instance.bCreateSoftwareMeshes) {
               }

            });
         }
      }

      if (var3 != null) {
         var2.skinningData.AnimationClips = var3.AnimationClips;
      }

   }

   protected void applyToAnimation(ModelTxt var1, AnimationAsset var2) {
      var2.AnimationClips = var1.clips;
      var2.assetParams.animationsMesh.skinningData.AnimationClips.putAll(var1.clips);
   }

   private ArrayList RemapMatrices(int[] var1, ArrayList var2, int var3) {
      ArrayList var4 = new ArrayList(var3);
      Matrix4f var5 = new Matrix4f();

      int var6;
      for(var6 = 0; var6 < var3; ++var6) {
         var4.add(var5);
      }

      for(var6 = 0; var6 < var1.length; ++var6) {
         var4.set(var1[var6], (Matrix4f)var2.get(var6));
      }

      return var4;
   }

   private Vector3f getVector(String var1) {
      Vector3f var2 = new Vector3f();
      String[] var3 = var1.split(",");
      var2.x = Float.parseFloat(var3[0]);
      var2.y = Float.parseFloat(var3[1]);
      var2.z = Float.parseFloat(var3[2]);
      return var2;
   }

   private Quaternion getQuaternion(String var1) {
      Quaternion var2 = new Quaternion();
      String[] var3 = var1.split(",");
      var2.x = Float.parseFloat(var3[0]);
      var2.y = Float.parseFloat(var3[1]);
      var2.z = Float.parseFloat(var3[2]);
      var2.w = Float.parseFloat(var3[3]);
      return var2;
   }

   private Matrix4f getMatrix(String var1, String var2, String var3, String var4) {
      Matrix4f var5 = new Matrix4f();
      boolean var6 = false;
      String[] var7 = var1.split(",");
      var5.m00 = Float.parseFloat(var7[0]);
      var5.m01 = Float.parseFloat(var7[1]);
      var5.m02 = Float.parseFloat(var7[2]);
      var5.m03 = Float.parseFloat(var7[3]);
      var7 = var2.split(",");
      var5.m10 = Float.parseFloat(var7[0]);
      var5.m11 = Float.parseFloat(var7[1]);
      var5.m12 = Float.parseFloat(var7[2]);
      var5.m13 = Float.parseFloat(var7[3]);
      var7 = var3.split(",");
      var5.m20 = Float.parseFloat(var7[0]);
      var5.m21 = Float.parseFloat(var7[1]);
      var5.m22 = Float.parseFloat(var7[2]);
      var5.m23 = Float.parseFloat(var7[3]);
      var7 = var4.split(",");
      var5.m30 = Float.parseFloat(var7[0]);
      var5.m31 = Float.parseFloat(var7[1]);
      var5.m32 = Float.parseFloat(var7[2]);
      var5.m33 = Float.parseFloat(var7[3]);
      return var5;
   }

   public static enum LoadMode {
      Version,
      ModelName,
      VertexStrideElementCount,
      VertexStrideSize,
      VertexStrideData,
      VertexCount,
      VertexBuffer,
      NumberOfFaces,
      FaceData,
      NumberOfBones,
      SkeletonHierarchy,
      BindPose,
      InvBindPose,
      SkinOffsetMatrices,
      NumberOfAnims,
      Anim;

      // $FF: synthetic method
      private static ModelLoader.LoadMode[] $values() {
         return new ModelLoader.LoadMode[]{Version, ModelName, VertexStrideElementCount, VertexStrideSize, VertexStrideData, VertexCount, VertexBuffer, NumberOfFaces, FaceData, NumberOfBones, SkeletonHierarchy, BindPose, InvBindPose, SkinOffsetMatrices, NumberOfAnims, Anim};
      }
   }
}
