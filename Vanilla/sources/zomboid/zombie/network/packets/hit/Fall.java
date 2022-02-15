package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.HitReactionNetworkAI;
import zombie.characters.IsoGameCharacter;
import zombie.core.network.ByteBufferWriter;
import zombie.network.packets.INetworkPacket;

public class Fall implements INetworkPacket {
   protected float dropPositionX;
   protected float dropPositionY;
   protected byte dropPositionZ;
   protected float dropDirection;

   public void set(HitReactionNetworkAI var1) {
      this.dropPositionX = var1.finalPosition.x;
      this.dropPositionY = var1.finalPosition.y;
      this.dropPositionZ = var1.finalPositionZ;
      this.dropDirection = var1.finalDirection.getDirection();
   }

   public void set(float var1, float var2, byte var3, float var4) {
      this.dropPositionX = var1;
      this.dropPositionY = var2;
      this.dropPositionZ = var3;
      this.dropDirection = var4;
   }

   public void parse(ByteBuffer var1) {
      this.dropPositionX = var1.getFloat();
      this.dropPositionY = var1.getFloat();
      this.dropPositionZ = var1.get();
      this.dropDirection = var1.getFloat();
   }

   public void write(ByteBufferWriter var1) {
      var1.putFloat(this.dropPositionX);
      var1.putFloat(this.dropPositionY);
      var1.putByte(this.dropPositionZ);
      var1.putFloat(this.dropDirection);
   }

   public String getDescription() {
      return "\n\tFall [ direction=" + this.dropDirection + " | position=( " + this.dropPositionX + " ; " + this.dropPositionY + " ; " + this.dropPositionZ + " ) ]";
   }

   public void process(IsoGameCharacter var1) {
      if (this.isSetup() && var1.getHitReactionNetworkAI() != null) {
         var1.getHitReactionNetworkAI().process(this.dropPositionX, this.dropPositionY, (float)this.dropPositionZ, this.dropDirection);
      }

   }

   boolean isSetup() {
      return this.dropPositionX != 0.0F && this.dropPositionY != 0.0F;
   }
}
