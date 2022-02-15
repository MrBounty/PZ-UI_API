package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.core.network.ByteBufferWriter;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.objects.IsoWindow;
import zombie.network.packets.INetworkPacket;

public class HitInfo implements INetworkPacket {
   public MovingObject object = new MovingObject();
   public NetObject window = new NetObject();
   public float x;
   public float y;
   public float z;
   public float dot;
   public float distSq;
   public int chance = 0;

   public HitInfo init(IsoMovingObject var1, float var2, float var3, float var4, float var5, float var6) {
      this.object = new MovingObject();
      this.window = new NetObject();
      this.object.setMovingObject(var1);
      this.window.setObject((IsoObject)null);
      this.x = var4;
      this.y = var5;
      this.z = var6;
      this.dot = var2;
      this.distSq = var3;
      return this;
   }

   public HitInfo init(IsoWindow var1, float var2, float var3) {
      this.object = new MovingObject();
      this.window = new NetObject();
      this.object.setMovingObject((IsoMovingObject)null);
      this.window.setObject(var1);
      this.z = var1.getZ();
      this.dot = var2;
      this.distSq = var3;
      return this;
   }

   public IsoMovingObject getObject() {
      return this.object.getMovingObject();
   }

   public void parse(ByteBuffer var1) {
      this.object.parse(var1);
      this.window.parse(var1);
      this.x = var1.getFloat();
      this.y = var1.getFloat();
      this.z = var1.getFloat();
      this.dot = var1.getFloat();
      this.distSq = var1.getFloat();
      this.chance = var1.getInt();
   }

   public void write(ByteBufferWriter var1) {
      this.object.write(var1);
      this.window.write(var1);
      var1.putFloat(this.x);
      var1.putFloat(this.y);
      var1.putFloat(this.z);
      var1.putFloat(this.dot);
      var1.putFloat(this.distSq);
      var1.putInt(this.chance);
   }

   public int getPacketSizeBytes() {
      return 24 + this.object.getPacketSizeBytes() + this.window.getPacketSizeBytes();
   }

   public String getDescription() {
      float var10000 = this.x;
      return "\n\tHitInfo [ x=" + var10000 + " y=" + this.y + " z=" + this.z + " dot=" + this.dot + " distSq=" + this.distSq + " chance=" + this.chance + "\n\t Object: " + this.object.getDescription() + "\n\t Window: " + this.window.getDescription() + " ]";
   }
}
