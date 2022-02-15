package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoGameCharacter;
import zombie.core.network.ByteBufferWriter;
import zombie.inventory.types.HandWeapon;
import zombie.network.GameServer;
import zombie.network.packets.INetworkPacket;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehicleManager;

public class Vehicle extends Instance implements INetworkPacket {
   protected BaseVehicle vehicle;

   public void set(BaseVehicle var1) {
      super.set(var1.getId());
      this.vehicle = var1;
   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.vehicle = VehicleManager.instance.getVehicleByID(this.ID);
   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
   }

   public boolean isConsistent() {
      return super.isConsistent() && this.vehicle != null;
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tVehicle [ vehicle=" + (this.vehicle == null ? "?" : "\"" + this.vehicle.getScriptName() + "\"") + " ]";
   }

   void process(IsoGameCharacter var1, HandWeapon var2) {
      if (GameServer.bServer) {
         this.vehicle.hitVehicle(var1, var2);
      }

   }

   BaseVehicle getVehicle() {
      return this.vehicle;
   }
}
