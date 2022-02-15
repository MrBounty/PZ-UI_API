package zombie.network.packets;

import java.nio.ByteBuffer;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.network.GameClient;
import zombie.network.GameServer;

public class DeadPlayerPacket extends DeadCharacterPacket implements INetworkPacket {
   private byte playerFlags;
   private float infectionLevel;
   private IsoPlayer player;

   public void set(IsoGameCharacter var1) {
      super.set(var1);
      this.player = (IsoPlayer)var1;
      this.infectionLevel = this.player.getBodyDamage().getInfectionLevel();
      this.playerFlags |= (byte)(this.player.getBodyDamage().isInfected() ? 1 : 0);
   }

   public void process() {
      if (this.player != null) {
         this.player.getBodyDamage().setOverallBodyHealth(0.0F);
         this.player.getBodyDamage().setInfected((this.playerFlags & 1) != 0);
         this.player.getBodyDamage().setInfectionLevel(this.infectionLevel);
         super.process();
      }

   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.infectionLevel = var1.getFloat();
      if (GameServer.bServer) {
         this.player = (IsoPlayer)GameServer.IDToPlayerMap.get(this.id);
      } else if (GameClient.bClient) {
         this.player = (IsoPlayer)GameClient.IDToPlayerMap.get(this.id);
      }

      if (this.player != null) {
         this.character = this.player;
         this.parseCharacterInventory(var1);
         this.character.setHealth(0.0F);
         this.character.getNetworkCharacterAI().setDeadBody(this);
      }

   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      var1.putFloat(this.infectionLevel);
      this.writeCharacterInventory(var1);
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + String.format(" | isInfected=%b infectionLevel=%f", (this.playerFlags & 1) != 0, this.infectionLevel);
   }

   public IsoPlayer getPlayer() {
      return this.player;
   }
}
