package zombie.core.skinnedmodel.model;

import java.nio.ByteBuffer;
import zombie.core.skinnedmodel.Vector3;
import zombie.iso.Vector2;

public final class VertexPositionNormalTangentTexture {
   public Vector3 Position;
   public Vector3 Normal;
   public Vector3 Tangent;
   public Vector2 TextureCoordinates;

   public VertexPositionNormalTangentTexture(Vector3 var1, Vector3 var2, Vector3 var3, Vector2 var4) {
      this.Position = var1;
      this.Normal = var2;
      this.Tangent = var3;
      this.TextureCoordinates = var4;
   }

   public VertexPositionNormalTangentTexture() {
      this.Position = new Vector3(0.0F, 0.0F, 0.0F);
      this.Normal = new Vector3(0.0F, 0.0F, 1.0F);
      this.Tangent = new Vector3(0.0F, 1.0F, 0.0F);
      this.TextureCoordinates = new Vector2(0.0F, 0.0F);
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
   }
}
