package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.network.ByteBufferWriter;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.packets.INetworkPacket;
import zombie.vehicles.BaseVehicle;

public class VehicleHit extends Hit implements INetworkPacket {
   public int vehicleDamage;
   public float vehicleSpeed;
   public boolean isVehicleHitFromBehind;
   public boolean isTargetHitFromBehind;

   public void set(boolean var1, float var2, float var3, float var4, float var5, int var6, float var7, boolean var8, boolean var9) {
      super.set(var1, var2, var3, var4, var5);
      this.vehicleDamage = var6;
      this.vehicleSpeed = var7;
      this.isVehicleHitFromBehind = var8;
      this.isTargetHitFromBehind = var9;
   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.vehicleDamage = var1.getInt();
      this.vehicleSpeed = var1.getFloat();
      this.isVehicleHitFromBehind = var1.get() != 0;
      this.isTargetHitFromBehind = var1.get() != 0;
   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      var1.putInt(this.vehicleDamage);
      var1.putFloat(this.vehicleSpeed);
      var1.putBoolean(this.isVehicleHitFromBehind);
      var1.putBoolean(this.isTargetHitFromBehind);
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tVehicle [ speed=" + this.vehicleSpeed + " | damage=" + this.vehicleDamage + " | target-hit=" + (this.isTargetHitFromBehind ? "FRONT" : "BEHIND") + " | vehicle-hit=" + (this.isVehicleHitFromBehind ? "FRONT" : "REAR") + " ]";
   }

   void process(IsoGameCharacter var1, IsoGameCharacter var2, BaseVehicle var3) {
      super.process(var1, var2);
      if (GameServer.bServer) {
         if (this.vehicleDamage != 0) {
            if (this.isVehicleHitFromBehind) {
               var3.addDamageFrontHitAChr(this.vehicleDamage);
            } else {
               var3.addDamageRearHitAChr(this.vehicleDamage);
            }

            var3.transmitBlood();
         }
      } else if (GameClient.bClient) {
         if (var2 instanceof IsoZombie) {
            ((IsoZombie)var2).applyDamageFromVehicle(this.vehicleSpeed, this.damage);
         } else if (var2 instanceof IsoPlayer) {
            ((IsoPlayer)var2).getDamageFromHitByACar(this.vehicleSpeed);
            ((IsoPlayer)var2).actionContext.reportEvent("washit");
            var2.setVariable("hitpvp", false);
         }
      }

   }
}
