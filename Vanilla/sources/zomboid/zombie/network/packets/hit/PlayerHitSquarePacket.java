package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.types.HandWeapon;
import zombie.network.packets.INetworkPacket;

public class PlayerHitSquarePacket extends PlayerHitPacket implements INetworkPacket {
   protected final Square square = new Square();

   public PlayerHitSquarePacket() {
      super(HitCharacterPacket.HitType.PlayerHitSquare);
   }

   public void set(IsoPlayer var1, HandWeapon var2, boolean var3) {
      super.set(var1, var2, var3);
      this.square.set(var1);
   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.square.parse(var1);
   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      this.square.write(var1);
   }

   public boolean isRelevant(UdpConnection var1) {
      return this.wielder.isRelevant(var1);
   }

   public boolean isConsistent() {
      return super.isConsistent() && this.square.isConsistent();
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tSquare " + this.square.getDescription();
   }

   protected void process() {
      this.square.process(this.wielder.getCharacter());
   }
}
