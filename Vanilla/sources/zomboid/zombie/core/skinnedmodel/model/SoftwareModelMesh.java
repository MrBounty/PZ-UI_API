package zombie.core.skinnedmodel.model;

import zombie.core.skinnedmodel.Vector3;
import zombie.iso.Vector2;

public final class SoftwareModelMesh {
   public int[] indicesUnskinned;
   public VertexPositionNormalTangentTextureSkin[] verticesUnskinned;
   public String Texture;
   public VertexBufferObject vb;

   public SoftwareModelMesh(VertexPositionNormalTangentTextureSkin[] var1, int[] var2) {
      this.indicesUnskinned = var2;
      this.verticesUnskinned = var1;
   }

   public SoftwareModelMesh(VertexPositionNormalTangentTexture[] var1, int[] var2) {
      this.indicesUnskinned = var2;
      this.verticesUnskinned = new VertexPositionNormalTangentTextureSkin[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         VertexPositionNormalTangentTexture var4 = var1[var3];
         this.verticesUnskinned[var3] = new VertexPositionNormalTangentTextureSkin();
         this.verticesUnskinned[var3].Position = new Vector3(var4.Position.x(), var4.Position.y(), var4.Position.z());
         this.verticesUnskinned[var3].Normal = new Vector3(var4.Normal.x(), var4.Normal.y(), var4.Normal.z());
         this.verticesUnskinned[var3].TextureCoordinates = new Vector2(var4.TextureCoordinates.x, var4.TextureCoordinates.y);
      }

   }
}
