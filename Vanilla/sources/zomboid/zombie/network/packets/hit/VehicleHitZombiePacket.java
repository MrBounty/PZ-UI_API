package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.network.ByteBufferWriter;
import zombie.network.packets.INetworkPacket;
import zombie.vehicles.BaseVehicle;

public class VehicleHitZombiePacket extends VehicleHitPacket implements INetworkPacket {
   protected final Zombie target = new Zombie();
   protected final VehicleHit vehicleHit = new VehicleHit();
   protected final Fall fall = new Fall();

   public VehicleHitZombiePacket() {
      super(HitCharacterPacket.HitType.VehicleHitZombie);
   }

   public void set(IsoPlayer var1, IsoZombie var2, BaseVehicle var3, float var4, boolean var5, int var6, float var7, boolean var8) {
      super.set(var1, var3, false);
      this.target.set(var2, false);
      this.vehicleHit.set(false, var4, var2.getHitForce(), var2.getHitDir().x, var2.getHitDir().y, var6, var7, var8, var5);
      this.fall.set(var2.getHitReactionNetworkAI());
   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.target.parse(var1);
      this.vehicleHit.parse(var1);
      this.fall.parse(var1);
   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      this.target.write(var1);
      this.vehicleHit.write(var1);
      this.fall.write(var1);
   }

   public boolean isConsistent() {
      return super.isConsistent() && this.target.isConsistent() && this.vehicleHit.isConsistent();
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tTarget " + this.target.getDescription() + "\n\tVehicleHit " + this.vehicleHit.getDescription() + "\n\tFall " + this.fall.getDescription();
   }

   public String getHitDescription() {
      String var10000 = this.getClass().getSimpleName();
      return var10000 + this.fall.getDescription() + this.target.getFlagsDescription();
   }

   protected void preProcess() {
      super.preProcess();
      this.target.process();
   }

   protected void process() {
      this.vehicleHit.process(this.wielder.getCharacter(), this.target.getCharacter(), this.vehicle.getVehicle());
      this.fall.process(this.target.getCharacter());
   }

   protected void postProcess() {
      super.postProcess();
      this.target.process();
   }

   protected void react() {
      this.target.react();
   }

   protected void postpone() {
      this.target.getCharacter().getNetworkCharacterAI().setVehicleHit(this);
   }
}
