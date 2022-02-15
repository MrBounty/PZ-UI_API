package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.inventory.types.HandWeapon;
import zombie.network.packets.INetworkPacket;
import zombie.vehicles.BaseVehicle;

public class PlayerHitVehiclePacket extends PlayerHitPacket implements INetworkPacket {
   protected final Vehicle vehicle = new Vehicle();

   public PlayerHitVehiclePacket() {
      super(HitCharacterPacket.HitType.PlayerHitVehicle);
   }

   public void set(IsoPlayer var1, BaseVehicle var2, HandWeapon var3, boolean var4) {
      super.set(var1, var3, var4);
      this.vehicle.set(var2);
   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.vehicle.parse(var1);
   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      this.vehicle.write(var1);
   }

   public boolean isConsistent() {
      return super.isConsistent() && this.vehicle.isConsistent();
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tVehicle " + this.vehicle.getDescription();
   }

   protected void process() {
      this.vehicle.process(this.wielder.getCharacter(), this.weapon.getWeapon());
   }
}
