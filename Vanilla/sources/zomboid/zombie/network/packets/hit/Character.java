package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import java.util.Optional;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.core.network.ByteBufferWriter;
import zombie.network.GameServer;
import zombie.network.packets.INetworkPacket;

public abstract class Character extends Instance implements INetworkPacket {
   protected IsoGameCharacter character;
   protected short characterFlags;
   protected float positionX;
   protected float positionY;
   protected float positionZ;
   protected float directionX;
   protected float directionY;
   protected String characterReaction;
   protected String playerReaction;
   protected String zombieReaction;

   public void set(IsoGameCharacter var1) {
      super.set(var1.getOnlineID());
      this.characterFlags = 0;
      this.characterFlags |= (short)(var1.isDead() ? 1 : 0);
      this.characterFlags |= (short)(var1.isCloseKilled() ? 2 : 0);
      this.characterFlags |= (short)(var1.isHitFromBehind() ? 4 : 0);
      this.characterFlags |= (short)(var1.isFallOnFront() ? 8 : 0);
      this.characterFlags |= (short)(var1.isKnockedDown() ? 16 : 0);
      this.characterFlags |= (short)(var1.isOnFloor() ? 32 : 0);
      this.character = var1;
      this.positionX = var1.getX();
      this.positionY = var1.getY();
      this.positionZ = var1.getZ();
      this.directionX = var1.getForwardDirection().getX();
      this.directionY = var1.getForwardDirection().getY();
      this.characterReaction = (String)Optional.ofNullable(var1.getHitReaction()).orElse("");
      this.playerReaction = (String)Optional.ofNullable(var1.getVariableString("PlayerHitReaction")).orElse("");
      this.zombieReaction = (String)Optional.ofNullable(var1.getVariableString("ZombieHitReaction")).orElse("");
   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.characterFlags = var1.getShort();
      this.positionX = var1.getFloat();
      this.positionY = var1.getFloat();
      this.positionZ = var1.getFloat();
      this.directionX = var1.getFloat();
      this.directionY = var1.getFloat();
      this.characterReaction = GameWindow.ReadString(var1);
      this.playerReaction = GameWindow.ReadString(var1);
      this.zombieReaction = GameWindow.ReadString(var1);
   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      var1.putShort(this.characterFlags);
      var1.putFloat(this.positionX);
      var1.putFloat(this.positionY);
      var1.putFloat(this.positionZ);
      var1.putFloat(this.directionX);
      var1.putFloat(this.directionY);
      var1.putUTF(this.characterReaction);
      var1.putUTF(this.playerReaction);
      var1.putUTF(this.zombieReaction);
   }

   public boolean isConsistent() {
      return super.isConsistent() && this.character != null;
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tCharacter [ hit-reactions=( \"c=\"" + this.characterReaction + "\" ; p=\"" + this.playerReaction + "\" ; z=\"" + this.zombieReaction + "\" ) | " + this.getFlagsDescription() + " | pos=( " + this.positionX + " ; " + this.positionY + " ; " + this.positionZ + " ) | dir=( " + this.directionX + " ; " + this.directionY + " ) | health=" + (this.character == null ? "?" : this.character.getHealth()) + " | current=" + (this.character == null ? "?" : "\"" + this.character.getCurrentActionContextStateName() + "\"") + " | previous=" + (this.character == null ? "?" : "\"" + this.character.getPreviousActionContextStateName() + "\"") + " ]";
   }

   String getFlagsDescription() {
      boolean var10000 = (this.characterFlags & 1) != 0;
      return " Flags [ isDead=" + var10000 + " | isKnockedDown=" + ((this.characterFlags & 16) != 0) + " | isCloseKilled=" + ((this.characterFlags & 2) != 0) + " | isHitFromBehind=" + ((this.characterFlags & 4) != 0) + " | isFallOnFront=" + ((this.characterFlags & 8) != 0) + " | isOnFloor=" + ((this.characterFlags & 32) != 0) + " ]";
   }

   void process() {
      this.character.setHitReaction(this.characterReaction);
      this.character.setVariable("PlayerHitReaction", this.playerReaction);
      this.character.setVariable("ZombieHitReaction", this.zombieReaction);
      this.character.setCloseKilled((this.characterFlags & 2) != 0);
      this.character.setHitFromBehind((this.characterFlags & 4) != 0);
      this.character.setFallOnFront((this.characterFlags & 8) != 0);
      this.character.setKnockedDown((this.characterFlags & 16) != 0);
      this.character.setOnFloor((this.characterFlags & 32) != 0);
      if (GameServer.bServer && (this.characterFlags & 32) == 0 && (this.characterFlags & 4) != 0) {
         this.character.setFallOnFront(true);
      }

   }

   protected void react() {
   }

   abstract IsoGameCharacter getCharacter();
}
