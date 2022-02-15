package zombie.network.packets;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;

public class SyncInjuriesPacket implements INetworkPacket {
   public short id;
   public float strafeSpeed;
   public float walkSpeed;
   public float walkInjury;

   public boolean set(IsoPlayer var1) {
      this.id = var1.getOnlineID();
      this.strafeSpeed = var1.getVariableFloat("StrafeSpeed", 1.0F);
      this.walkSpeed = var1.getVariableFloat("WalkSpeed", 1.0F);
      this.walkInjury = var1.getVariableFloat("WalkInjury", 0.0F);
      return true;
   }

   public void process(IsoPlayer var1) {
      var1.setVariable("StrafeSpeed", this.strafeSpeed);
      var1.setVariable("WalkSpeed", this.walkSpeed);
      var1.setVariable("WalkInjury", this.walkInjury);
   }

   public void parse(ByteBuffer var1) {
      this.id = var1.getShort();
      this.strafeSpeed = var1.getFloat();
      this.walkSpeed = var1.getFloat();
      this.walkInjury = var1.getFloat();
   }

   public void write(ByteBufferWriter var1) {
      var1.putShort(this.id);
      var1.putFloat(this.strafeSpeed);
      var1.putFloat(this.walkSpeed);
      var1.putFloat(this.walkInjury);
   }

   public int getPacketSizeBytes() {
      return 14;
   }

   public String getDescription() {
      return "SyncInjuriesPacket: id=" + this.id + ", strafeSpeed=" + this.strafeSpeed + ", walkSpeed=" + this.walkSpeed + ", walkInjury=" + this.walkInjury;
   }
}
