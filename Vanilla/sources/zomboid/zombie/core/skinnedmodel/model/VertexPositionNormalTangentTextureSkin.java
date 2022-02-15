package zombie.core.skinnedmodel.model;

import java.nio.ByteBuffer;
import zombie.core.skinnedmodel.Vector3;
import zombie.core.skinnedmodel.Vector4;
import zombie.iso.Vector2;

public final class VertexPositionNormalTangentTextureSkin {
   public Vector3 Position;
   public Vector3 Normal;
   public Vector3 Tangent;
   public Vector2 TextureCoordinates;
   public Vector4 BlendWeights;
   public UInt4 BlendIndices;

   public VertexPositionNormalTangentTextureSkin() {
   }

   public VertexPositionNormalTangentTextureSkin(Vector3 var1, Vector3 var2, Vector3 var3, Vector2 var4, Vector4 var5, UInt4 var6) {
      this.Position = var1;
      this.Normal = var2;
      this.Tangent = var3;
      this.TextureCoordinates = var4;
      this.BlendWeights = var5;
      this.BlendIndices = var6;
   }

   public void put(ByteBuffer var1) {
      var1.putFloat(this.Position.x());
      var1.putFloat(this.Position.y());
      var1.putFloat(this.Position.z());
      var1.putFloat(this.Normal.x());
      var1.putFloat(this.Normal.y());
      var1.putFloat(this.Normal.z());
      var1.putFloat(this.Tangent.x());
      var1.putFloat(this.Tangent.y());
      var1.putFloat(this.Tangent.z());
      var1.putFloat(this.TextureCoordinates.x);
      var1.putFloat(this.TextureCoordinates.y);
      var1.putFloat(this.BlendWeights.x);
      var1.putFloat(this.BlendWeights.y);
      var1.putFloat(this.BlendWeights.z);
      var1.putFloat(this.BlendWeights.w);
      var1.putFloat((float)this.BlendIndices.X);
      var1.putFloat((float)this.BlendIndices.Y);
      var1.putFloat((float)this.BlendIndices.Z);
      var1.putFloat((float)this.BlendIndices.W);
   }
}
