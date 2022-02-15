package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoLivingCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.types.HandWeapon;
import zombie.network.packets.INetworkPacket;

public abstract class PlayerHitPacket extends HitCharacterPacket implements INetworkPacket {
   protected final Player wielder = new Player();
   protected final Weapon weapon = new Weapon();

   public PlayerHitPacket(HitCharacterPacket.HitType var1) {
      super(var1);
   }

   public void set(IsoPlayer var1, HandWeapon var2, boolean var3) {
      this.wielder.set(var1, var3);
      this.weapon.set(var2);
   }

   public void parse(ByteBuffer var1) {
      this.wielder.parse(var1);
      this.weapon.parse(var1, (IsoLivingCharacter)this.wielder.getCharacter());
   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      this.wielder.write(var1);
      this.weapon.write(var1);
   }

   public boolean isRelevant(UdpConnection var1) {
      return this.wielder.isRelevant(var1);
   }

   public boolean isConsistent() {
      return super.isConsistent() && this.weapon.isConsistent() && this.wielder.isConsistent();
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tWielder " + this.wielder.getDescription() + "\n\tWeapon " + this.weapon.getDescription();
   }

   protected void preProcess() {
      this.wielder.process();
   }

   protected void postProcess() {
      this.wielder.process();
   }

   protected void attack() {
      this.wielder.attack(this.weapon.getWeapon());
   }

   protected void react() {
   }
}
