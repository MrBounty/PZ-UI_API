package zombie.core.skinnedmodel.animation;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.vecmath.Point3f;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjglx.BufferUtils;
import zombie.core.skinnedmodel.HelperFunctions;
import zombie.core.skinnedmodel.Vector3;
import zombie.core.skinnedmodel.model.SkinningData;
import zombie.core.skinnedmodel.model.SoftwareModelMesh;
import zombie.core.skinnedmodel.model.UInt4;
import zombie.core.skinnedmodel.model.Vbo;
import zombie.core.skinnedmodel.model.VertexBufferObject;
import zombie.core.skinnedmodel.model.VertexPositionNormalTangentTextureSkin;
import zombie.core.skinnedmodel.model.VertexStride;
import zombie.core.skinnedmodel.shader.Shader;
import zombie.iso.Vector2;

public final class SoftwareSkinnedModelAnim {
   private long animOffset;
   private final VertexBufferObject.BeginMode _beginMode;
   private final VertexStride[] _vertexStride;
   private final Vbo _handle;
   public static Matrix4f[] boneTransforms;
   public static Matrix4f[] worldTransforms;
   public static Matrix4f[] skinTransforms;
   ByteBuffer softwareSkinBufferInt;
   public HashMap AnimationOffset = new HashMap();
   public HashMap AnimationLength = new HashMap();
   public int vertCount = 0;
   private int elementCount;
   static Matrix4f Identity = new Matrix4f();
   private static Vector3f tempVec3f = new Vector3f();
   static javax.vecmath.Matrix4f m = new javax.vecmath.Matrix4f();
   static Point3f tempop = new Point3f();
   static javax.vecmath.Vector3f temponor = new javax.vecmath.Vector3f();
   static Vector3f tot = new Vector3f();
   static Vector3f totn = new Vector3f();
   static Vector3f vec = new Vector3f();

   public void UpdateWorldTransforms(Matrix4f var1, float var2, SkinningData var3) {
      Identity.setIdentity();
      tempVec3f.set(0.0F, 1.0F, 0.0F);
      Matrix4f.mul(boneTransforms[0], Identity, worldTransforms[0]);

      for(int var4 = 1; var4 < worldTransforms.length; ++var4) {
         int var5 = (Integer)var3.SkeletonHierarchy.get(var4);
         Matrix4f.mul(boneTransforms[var4], worldTransforms[var5], worldTransforms[var4]);
      }

   }

   public void UpdateSkinTransforms(SkinningData var1) {
      for(int var2 = 0; var2 < worldTransforms.length; ++var2) {
         Matrix4f.mul((Matrix4f)var1.BoneOffset.get(var2), worldTransforms[var2], skinTransforms[var2]);
      }

   }

   public SoftwareSkinnedModelAnim(StaticAnimation[] var1, SoftwareModelMesh var2, SkinningData var3) {
      this.vertCount = var2.verticesUnskinned.length;
      this.elementCount = var2.indicesUnskinned.length;
      Vbo var4 = new Vbo();
      int var5;
      if (boneTransforms == null) {
         boneTransforms = new Matrix4f[var3.BindPose.size()];
         worldTransforms = new Matrix4f[var3.BindPose.size()];
         skinTransforms = new Matrix4f[var3.BindPose.size()];

         for(var5 = 0; var5 < var3.BindPose.size(); ++var5) {
            boneTransforms[var5] = HelperFunctions.getMatrix();
            boneTransforms[var5].setIdentity();
            worldTransforms[var5] = HelperFunctions.getMatrix();
            worldTransforms[var5].setIdentity();
            skinTransforms[var5] = HelperFunctions.getMatrix();
            skinTransforms[var5].setIdentity();
         }
      }

      var5 = 0;
      ArrayList var6 = new ArrayList();
      ArrayList var7 = new ArrayList();
      int var8 = 0;

      int var9;
      StaticAnimation var10;
      int var13;
      for(var9 = 0; var9 < var1.length; ++var9) {
         var10 = var1[var9];
         this.AnimationOffset.put(var10.Clip.Name, var5);
         this.AnimationLength.put(var10.Clip.Name, var10.Matrices.length);

         for(int var11 = 0; var11 < var10.Matrices.length; ++var11) {
            int[] var12 = var2.indicesUnskinned;

            int var14;
            for(var13 = 0; var13 < var12.length; ++var13) {
               var14 = var12[var13];
               var7.add(var14 + var8);
            }

            var8 += this.vertCount;
            Matrix4f[] var19 = var10.Matrices[var11];
            boneTransforms = var19;
            this.UpdateWorldTransforms((Matrix4f)null, 0.0F, var3);
            this.UpdateSkinTransforms(var3);

            for(var14 = 0; var14 < var2.verticesUnskinned.length; ++var14) {
               VertexPositionNormalTangentTextureSkin var15 = this.updateSkin(skinTransforms, var2.verticesUnskinned, var14);
               var6.add(var15);
            }

            var5 += var2.indicesUnskinned.length;
         }
      }

      this._vertexStride = new VertexStride[4];

      for(var9 = 0; var9 < this._vertexStride.length; ++var9) {
         this._vertexStride[var9] = new VertexStride();
      }

      this._vertexStride[0].Type = VertexBufferObject.VertexType.VertexArray;
      this._vertexStride[0].Offset = 0;
      this._vertexStride[1].Type = VertexBufferObject.VertexType.NormalArray;
      this._vertexStride[1].Offset = 12;
      this._vertexStride[2].Type = VertexBufferObject.VertexType.ColorArray;
      this._vertexStride[2].Offset = 24;
      this._vertexStride[3].Type = VertexBufferObject.VertexType.TextureCoordArray;
      this._vertexStride[3].Offset = 28;
      this._beginMode = VertexBufferObject.BeginMode.Triangles;
      boolean var16 = false;
      var10 = null;
      var4.VboID = VertexBufferObject.funcs.glGenBuffers();
      ByteBuffer var17 = BufferUtils.createByteBuffer(var6.size() * 36);
      ByteBuffer var18 = BufferUtils.createByteBuffer(var7.size() * 4);

      for(var13 = 0; var13 < var6.size(); ++var13) {
         VertexPositionNormalTangentTextureSkin var20 = (VertexPositionNormalTangentTextureSkin)var6.get(var13);
         var17.putFloat(var20.Position.x());
         var17.putFloat(var20.Position.y());
         var17.putFloat(var20.Position.z());
         var17.putFloat(var20.Normal.x());
         var17.putFloat(var20.Normal.y());
         var17.putFloat(var20.Normal.z());
         var17.putInt(-1);
         var17.putFloat(var20.TextureCoordinates.x);
         var17.putFloat(var20.TextureCoordinates.y);
      }

      for(var13 = 0; var13 < var7.size(); ++var13) {
         var18.putInt((Integer)var7.get(var13));
      }

      var18.flip();
      var17.flip();
      var4.VertexStride = 36;
      var4.NumElements = var7.size();
      boolean var21 = false;
      boolean var22 = true;
      var4.FaceDataOnly = false;
      VertexBufferObject.funcs.glBindBuffer(VertexBufferObject.funcs.GL_ARRAY_BUFFER(), var4.VboID);
      VertexBufferObject.funcs.glBufferData(VertexBufferObject.funcs.GL_ARRAY_BUFFER(), var17, VertexBufferObject.funcs.GL_STATIC_DRAW());
      VertexBufferObject.funcs.glGetBufferParameter(VertexBufferObject.funcs.GL_ARRAY_BUFFER(), VertexBufferObject.funcs.GL_BUFFER_SIZE(), var4.b);
      var4.EboID = VertexBufferObject.funcs.glGenBuffers();
      VertexBufferObject.funcs.glBindBuffer(VertexBufferObject.funcs.GL_ELEMENT_ARRAY_BUFFER(), var4.EboID);
      VertexBufferObject.funcs.glBufferData(VertexBufferObject.funcs.GL_ELEMENT_ARRAY_BUFFER(), var18, VertexBufferObject.funcs.GL_STATIC_DRAW());
      this._handle = var4;
   }

   public VertexPositionNormalTangentTextureSkin updateSkin(Matrix4f[] var1, VertexPositionNormalTangentTextureSkin[] var2, int var3) {
      tot.set(0.0F, 0.0F, 0.0F);
      totn.set(0.0F, 0.0F, 0.0F);
      VertexPositionNormalTangentTextureSkin var4 = var2[var3];
      Matrix4f var5 = HelperFunctions.getMatrix();
      Matrix4f var6 = HelperFunctions.getMatrix();
      var5.setIdentity();
      Matrix4f var7 = HelperFunctions.getMatrix();
      UInt4 var8 = var4.BlendIndices;
      float var9 = 1.0F;
      Point3f var10;
      javax.vecmath.Vector3f var11;
      Vector3f var10000;
      if (var4.BlendWeights.x > 0.0F) {
         var6.load(var1[var8.X]);
         set(var6, m);
         var10 = tempop;
         tempop.set(var4.Position.x(), var4.Position.y(), var4.Position.z());
         m.transform(var10);
         var10.x *= var4.BlendWeights.x;
         var10.y *= var4.BlendWeights.x;
         var10.z *= var4.BlendWeights.x;
         var10000 = tot;
         var10000.x += var10.x;
         var10000 = tot;
         var10000.y += var10.y;
         var10000 = tot;
         var10000.z += var10.z;
         var11 = temponor;
         temponor.set(var4.Normal.x(), var4.Normal.y(), var4.Normal.z());
         m.transform(var11);
         var11.x *= var4.BlendWeights.x;
         var11.y *= var4.BlendWeights.x;
         var11.z *= var4.BlendWeights.x;
         var10000 = totn;
         var10000.x += var11.x;
         var10000 = totn;
         var10000.y += var11.y;
         var10000 = totn;
         var10000.z += var11.z;
      }

      if (var4.BlendWeights.y > 0.0F) {
         var6.load(var1[var8.Y]);
         set(var6, m);
         var10 = tempop;
         tempop.set(var4.Position.x(), var4.Position.y(), var4.Position.z());
         m.transform(var10);
         var10.x *= var4.BlendWeights.y;
         var10.y *= var4.BlendWeights.y;
         var10.z *= var4.BlendWeights.y;
         var10000 = tot;
         var10000.x += var10.x;
         var10000 = tot;
         var10000.y += var10.y;
         var10000 = tot;
         var10000.z += var10.z;
         var11 = temponor;
         temponor.set(var4.Normal.x(), var4.Normal.y(), var4.Normal.z());
         m.transform(var11);
         var11.x *= var4.BlendWeights.y;
         var11.y *= var4.BlendWeights.y;
         var11.z *= var4.BlendWeights.y;
         var10000 = totn;
         var10000.x += var11.x;
         var10000 = totn;
         var10000.y += var11.y;
         var10000 = totn;
         var10000.z += var11.z;
      }

      if (var4.BlendWeights.z > 0.0F) {
         var6.load(var1[var8.Z]);
         set(var6, m);
         var10 = tempop;
         tempop.set(var4.Position.x(), var4.Position.y(), var4.Position.z());
         m.transform(var10);
         var10.x *= var4.BlendWeights.z;
         var10.y *= var4.BlendWeights.z;
         var10.z *= var4.BlendWeights.z;
         var10000 = tot;
         var10000.x += var10.x;
         var10000 = tot;
         var10000.y += var10.y;
         var10000 = tot;
         var10000.z += var10.z;
         var11 = temponor;
         temponor.set(var4.Normal.x(), var4.Normal.y(), var4.Normal.z());
         m.transform(var11);
         var11.x *= var4.BlendWeights.z;
         var11.y *= var4.BlendWeights.z;
         var11.z *= var4.BlendWeights.z;
         var10000 = totn;
         var10000.x += var11.x;
         var10000 = totn;
         var10000.y += var11.y;
         var10000 = totn;
         var10000.z += var11.z;
      }

      if (var4.BlendWeights.w > 0.0F) {
         var6.load(var1[var8.W]);
         set(var6, m);
         var10 = tempop;
         tempop.set(var4.Position.x(), var4.Position.y(), var4.Position.z());
         m.transform(var10);
         var10.x *= var4.BlendWeights.w;
         var10.y *= var4.BlendWeights.w;
         var10.z *= var4.BlendWeights.w;
         var10000 = tot;
         var10000.x += var10.x;
         var10000 = tot;
         var10000.y += var10.y;
         var10000 = tot;
         var10000.z += var10.z;
         var11 = temponor;
         temponor.set(var4.Normal.x(), var4.Normal.y(), var4.Normal.z());
         m.transform(var11);
         var11.x *= var4.BlendWeights.w;
         var11.y *= var4.BlendWeights.w;
         var11.z *= var4.BlendWeights.w;
         var10000 = totn;
         var10000.x += var11.x;
         var10000 = totn;
         var10000.y += var11.y;
         var10000 = totn;
         var10000.z += var11.z;
      }

      var7.setIdentity();
      vec.x = tot.x;
      vec.y = tot.y;
      vec.z = tot.z;
      VertexPositionNormalTangentTextureSkin var12 = new VertexPositionNormalTangentTextureSkin();
      var12.Position = new Vector3();
      var12.Position.set(vec.getX(), vec.getY(), vec.getZ());
      var11 = temponor;
      var11.x = totn.x;
      var11.y = totn.y;
      var11.z = totn.z;
      var11.normalize();
      var12.Normal = new Vector3();
      var12.Normal.set(var11.getX(), var11.getY(), var11.getZ());
      var12.TextureCoordinates = new Vector2();
      var12.TextureCoordinates.x = var4.TextureCoordinates.x;
      var12.TextureCoordinates.y = var4.TextureCoordinates.y;
      HelperFunctions.returnMatrix(var5);
      HelperFunctions.returnMatrix(var7);
      HelperFunctions.returnMatrix(var6);
      return var12;
   }

   public void Draw(int var1, int var2, String var3) {
      this.Draw(this._handle, this._vertexStride, this._beginMode, (Shader)null, var1, var2, var3);
   }

   static void set(Matrix4f var0, javax.vecmath.Matrix4f var1) {
      var1.m00 = var0.m00;
      var1.m01 = var0.m01;
      var1.m02 = var0.m02;
      var1.m03 = var0.m03;
      var1.m10 = var0.m10;
      var1.m11 = var0.m11;
      var1.m12 = var0.m12;
      var1.m13 = var0.m13;
      var1.m20 = var0.m20;
      var1.m21 = var0.m21;
      var1.m22 = var0.m22;
      var1.m23 = var0.m23;
      var1.m30 = var0.m30;
      var1.m31 = var0.m31;
      var1.m32 = var0.m32;
      var1.m33 = var0.m33;
   }

   private void Draw(Vbo var1, VertexStride[] var2, VertexBufferObject.BeginMode var3, Shader var4, int var5, int var6, String var7) {
      this.animOffset = (long)(var6 + this.elementCount * var5);
      int var8 = this.elementCount;
      int var9 = 33984;
      if (!var1.FaceDataOnly) {
         VertexBufferObject.funcs.glBindBuffer(VertexBufferObject.funcs.GL_ARRAY_BUFFER(), var1.VboID);

         for(int var10 = var2.length - 1; var10 >= 0; --var10) {
            switch(var2[var10].Type) {
            case VertexArray:
               GL20.glVertexPointer(3, 5126, var1.VertexStride, (long)var2[var10].Offset);
               GL20.glEnableClientState(32884);
               break;
            case NormalArray:
               GL20.glNormalPointer(5126, var1.VertexStride, (long)var2[var10].Offset);
               GL20.glEnableClientState(32885);
               break;
            case ColorArray:
               GL20.glColorPointer(3, 5121, var1.VertexStride, (long)var2[var10].Offset);
               GL20.glEnableClientState(32886);
               break;
            case TextureCoordArray:
               GL13.glActiveTexture(var9);
               GL13.glClientActiveTexture(var9);
               GL20.glTexCoordPointer(2, 5126, var1.VertexStride, (long)var2[var10].Offset);
               ++var9;
               GL20.glEnableClientState(32888);
               break;
            case TangentArray:
               GL20.glNormalPointer(5126, var1.VertexStride, (long)var2[var10].Offset);
               break;
            case BlendWeightArray:
               int var11 = GL20.glGetAttribLocation(var4.getID(), "boneWeights");
               GL20.glVertexAttribPointer(var11, 4, 5126, false, var1.VertexStride, (long)var2[var10].Offset);
               GL20.glEnableVertexAttribArray(var11);
               break;
            case BlendIndexArray:
               int var12 = GL20.glGetAttribLocation(var4.getID(), "boneIndices");
               GL20.glVertexAttribPointer(var12, 4, 5126, false, var1.VertexStride, (long)var2[var10].Offset);
               GL20.glEnableVertexAttribArray(var12);
            }
         }
      }

      VertexBufferObject.funcs.glBindBuffer(VertexBufferObject.funcs.GL_ELEMENT_ARRAY_BUFFER(), var1.EboID);
      GL20.glDrawElements(4, var8, 5125, this.animOffset * 4L);
      GL20.glDisableClientState(32885);
   }
}
