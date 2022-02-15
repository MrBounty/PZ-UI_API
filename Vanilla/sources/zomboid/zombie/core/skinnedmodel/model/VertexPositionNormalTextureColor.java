package zombie.core.skinnedmodel.model;

import java.nio.ByteBuffer;
import zombie.core.Color;
import zombie.core.skinnedmodel.Vector3;
import zombie.iso.Vector2;

public final class VertexPositionNormalTextureColor {
   public Color Color;
   public Vector3 Position;
   public Vector3 Normal;
   public Vector2 TextureCoordinates;

   public void put(ByteBuffer var1) {
      var1.putFloat(this.Position.x());
      var1.putFloat(this.Position.y());
      var1.putFloat(this.Position.z());
      var1.putFloat(this.Normal.x());
      var1.putFloat(this.Normal.y());
      var1.putFloat(this.Normal.z());
      var1.putFloat(this.TextureCoordinates.x);
      var1.putFloat(this.TextureCoordinates.y);
      var1.put((byte)((int)(this.Color.r * 255.0F)));
      var1.put((byte)((int)(this.Color.g * 255.0F)));
      var1.put((byte)((int)(this.Color.b * 255.0F)));
   }
}
