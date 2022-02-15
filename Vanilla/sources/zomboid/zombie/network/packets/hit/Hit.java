package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.network.GameServer;
import zombie.network.packets.INetworkPacket;

public abstract class Hit implements INetworkPacket {
   protected boolean ignore;
   protected float damage;
   protected float hitForce;
   protected float hitDirectionX;
   protected float hitDirectionY;

   public void set(boolean var1, float var2, float var3, float var4, float var5) {
      this.ignore = var1;
      this.damage = var2;
      this.hitForce = var3;
      this.hitDirectionX = var4;
      this.hitDirectionY = var5;
   }

   public void parse(ByteBuffer var1) {
      this.ignore = var1.get() != 0;
      this.damage = var1.getFloat();
      this.hitForce = var1.getFloat();
      this.hitDirectionX = var1.getFloat();
      this.hitDirectionY = var1.getFloat();
   }

   public void write(ByteBufferWriter var1) {
      var1.putBoolean(this.ignore);
      var1.putFloat(this.damage);
      var1.putFloat(this.hitForce);
      var1.putFloat(this.hitDirectionX);
      var1.putFloat(this.hitDirectionY);
   }

   public String getDescription() {
      return "\n\tHit [ ignore=" + this.ignore + " | damage=" + this.damage + " | force=" + this.hitForce + " | dir=( " + this.hitDirectionX + " ; " + this.hitDirectionY + " ) ]";
   }

   void process(IsoGameCharacter var1, IsoGameCharacter var2) {
      var2.getHitDir().set(this.hitDirectionX, this.hitDirectionY);
      var2.setHitForce(this.hitForce);
      if (GameServer.bServer && var2 instanceof IsoZombie && var1 instanceof IsoPlayer) {
         ((IsoZombie)var2).addAggro(var1, this.damage);
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, String.format("AddAggro zombie=%d player=%d ( \"%s\" ) damage=%f", var2.getOnlineID(), var1.getOnlineID(), ((IsoPlayer)var1).getUsername(), this.damage));
         }
      }

      var2.setAttackedBy(var1);
   }
}
