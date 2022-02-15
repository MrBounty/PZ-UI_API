package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.network.ByteBufferWriter;
import zombie.iso.IsoMovingObject;
import zombie.network.GameServer;
import zombie.network.packets.INetworkPacket;

public class Bite implements INetworkPacket {
   protected short flags;
   protected float hitDirection;

   public void set(IsoZombie var1) {
      this.flags = 0;
      this.flags |= (short)(var1.getEatBodyTarget() != null ? 1 : 0);
      this.flags |= (short)(var1.getVariableBoolean("AttackDidDamage") ? 2 : 0);
      this.flags |= (short)("BiteDefended".equals(var1.getHitReaction()) ? 4 : 0);
      this.flags |= (short)(var1.scratch ? 8 : 0);
      this.flags |= (short)(var1.laceration ? 16 : 0);
      this.hitDirection = var1.getHitDir().getDirection();
   }

   public void parse(ByteBuffer var1) {
      this.flags = var1.getShort();
      this.hitDirection = var1.getFloat();
   }

   public void write(ByteBufferWriter var1) {
      var1.putShort(this.flags);
      var1.putFloat(this.hitDirection);
   }

   public String getDescription() {
      boolean var10000 = (this.flags & 1) != 0;
      return "\n\tBite [ eatBodyTarget=" + var10000 + " | attackDidDamage=" + ((this.flags & 2) != 0) + " | biteDefended=" + ((this.flags & 4) != 0) + " | scratch=" + ((this.flags & 8) != 0) + " | laceration=" + ((this.flags & 16) != 0) + " | hitDirection=" + this.hitDirection + " ]";
   }

   void process(IsoZombie var1, IsoGameCharacter var2) {
      if ((this.flags & 4) == 0) {
         var2.setAttackedBy(var1);
         if ((this.flags & 1) != 0 || var2.isDead()) {
            var1.setEatBodyTarget(var2, true);
            var1.setTarget((IsoMovingObject)null);
         }

         if (var2.isAsleep()) {
            if (GameServer.bServer) {
               var2.sendObjectChange("wakeUp");
            } else {
               var2.forceAwake();
            }
         }

         if ((this.flags & 2) != 0) {
            var2.reportEvent("washit");
            var2.setVariable("hitpvp", false);
         }

         var1.scratch = (this.flags & 8) != 0;
         var1.laceration = (this.flags & 8) != 0;
      }

      var1.getHitDir().setLengthAndDirection(this.hitDirection, 1.0F);
   }
}
