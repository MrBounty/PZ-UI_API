package zombie.network.packets;

import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.core.network.ByteBufferWriter;
import zombie.iso.IsoMovingObject;
import zombie.network.packets.hit.MovingObject;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;

public class StopSoundPacket implements INetworkPacket {
   MovingObject object = new MovingObject();
   String name;
   boolean trigger;

   public void set(IsoMovingObject var1, String var2, boolean var3) {
      this.object.setMovingObject(var1);
      this.name = var2;
      this.trigger = var3;
   }

   public void process() {
      IsoMovingObject var1 = this.object.getMovingObject();
      IsoGameCharacter var2 = (IsoGameCharacter)Type.tryCastTo(var1, IsoGameCharacter.class);
      if (var2 != null) {
         if (this.trigger) {
            var2.getEmitter().stopOrTriggerSoundByName(this.name);
         } else {
            var2.getEmitter().stopSoundByName(this.name);
         }

      } else {
         BaseVehicle var3 = (BaseVehicle)Type.tryCastTo(var1, BaseVehicle.class);
         if (var3 != null) {
            if (this.trigger) {
               var3.getEmitter().stopOrTriggerSoundByName(this.name);
            } else {
               var3.getEmitter().stopSoundByName(this.name);
            }

         }
      }
   }

   public void parse(ByteBuffer var1) {
      this.trigger = var1.get() == 1;
      this.object.parse(var1);
      this.name = GameWindow.ReadString(var1);
   }

   public void write(ByteBufferWriter var1) {
      var1.putByte((byte)(this.trigger ? 1 : 0));
      this.object.write(var1);
      var1.putUTF(this.name);
   }

   public int getPacketSizeBytes() {
      return this.object.getPacketSizeBytes() + 2 + this.name.length();
   }

   public String getDescription() {
      String var10000 = this.name;
      return "\n\tStopSoundPacket [name=" + var10000 + " | object=" + this.object.getDescription() + "]";
   }
}
