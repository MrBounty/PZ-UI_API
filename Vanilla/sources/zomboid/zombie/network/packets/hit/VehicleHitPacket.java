package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.network.packets.INetworkPacket;
import zombie.vehicles.BaseVehicle;

public abstract class VehicleHitPacket extends HitCharacterPacket implements INetworkPacket {
   protected final Player wielder = new Player();
   protected final Vehicle vehicle = new Vehicle();

   public VehicleHitPacket(HitCharacterPacket.HitType var1) {
      super(var1);
   }

   public void set(IsoPlayer var1, BaseVehicle var2, boolean var3) {
      this.wielder.set(var1, var3);
      this.vehicle.set(var2);
   }

   public void parse(ByteBuffer var1) {
      this.wielder.parse(var1);
      this.vehicle.parse(var1);
   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      this.wielder.write(var1);
      this.vehicle.write(var1);
   }

   public boolean isRelevant(UdpConnection var1) {
      return this.wielder.isRelevant(var1);
   }

   public boolean isConsistent() {
      return super.isConsistent() && this.wielder.isConsistent() && this.vehicle.isConsistent();
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tWielder " + this.wielder.getDescription() + "\n\tVehicle " + this.vehicle.getDescription();
   }

   protected void preProcess() {
      this.wielder.process();
   }

   protected void postProcess() {
      this.wielder.process();
   }

   protected void attack() {
   }
}
