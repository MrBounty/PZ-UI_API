package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoGameCharacter;
import zombie.core.network.ByteBufferWriter;
import zombie.iso.IsoGridSquare;
import zombie.network.packets.INetworkPacket;

public class Square implements INetworkPacket {
   protected float positionX;
   protected float positionY;
   protected float positionZ;

   public void set(IsoGameCharacter var1) {
      IsoGridSquare var2 = var1.getAttackTargetSquare();
      if (var2 != null) {
         this.positionX = (float)var2.getX();
         this.positionY = (float)var2.getY();
         this.positionZ = (float)var2.getZ();
      } else {
         this.positionX = 0.0F;
         this.positionY = 0.0F;
         this.positionZ = 0.0F;
      }

   }

   public void parse(ByteBuffer var1) {
      this.positionX = var1.getFloat();
      this.positionY = var1.getFloat();
      this.positionZ = var1.getFloat();
   }

   public void write(ByteBufferWriter var1) {
      var1.putFloat(this.positionX);
      var1.putFloat(this.positionY);
      var1.putFloat(this.positionZ);
   }

   public String getDescription() {
      return "\n\tSquare [ pos=( " + this.positionX + " ; " + this.positionY + " ; " + this.positionZ + " ) ]";
   }

   void process(IsoGameCharacter var1) {
      var1.setAttackTargetSquare(var1.getCell().getGridSquare((double)this.positionX, (double)this.positionY, (double)this.positionZ));
   }
}
