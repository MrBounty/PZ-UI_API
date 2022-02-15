package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.PersistentOutfits;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoZombie;
import zombie.core.network.ByteBufferWriter;
import zombie.core.skinnedmodel.ModelManager;
import zombie.inventory.types.HandWeapon;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.network.ServerMap;
import zombie.network.packets.INetworkPacket;

public class Zombie extends Character implements INetworkPacket {
   protected IsoZombie zombie;
   protected short zombieFlags;
   protected String attackOutcome;
   protected String attackPosition;

   public void set(IsoZombie var1, boolean var2) {
      super.set(var1);
      this.zombie = var1;
      this.zombieFlags = 0;
      this.zombieFlags |= (short)(var1.isStaggerBack() ? 1 : 0);
      this.zombieFlags |= (short)(var1.isFakeDead() ? 2 : 0);
      this.zombieFlags |= (short)(var1.isBecomeCrawler() ? 4 : 0);
      this.zombieFlags |= (short)(var1.isCrawling() ? 8 : 0);
      this.zombieFlags |= (short)(var1.isKnifeDeath() ? 16 : 0);
      this.zombieFlags |= (short)(var1.isJawStabAttach() ? 32 : 0);
      this.zombieFlags |= (short)(var2 ? 64 : 0);
      this.zombieFlags |= (short)(var1.getVariableBoolean("AttackDidDamage") ? 128 : 0);
      this.attackOutcome = var1.getVariableString("AttackOutcome");
      this.attackPosition = var1.getPlayerAttackPosition();
   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.zombieFlags = var1.getShort();
      this.attackOutcome = GameWindow.ReadString(var1);
      this.attackPosition = GameWindow.ReadString(var1);
      if (GameServer.bServer) {
         this.zombie = ServerMap.instance.ZombieMap.get(this.ID);
         this.character = this.zombie;
      } else if (GameClient.bClient) {
         this.zombie = (IsoZombie)GameClient.IDToZombieMap.get(this.ID);
         this.character = this.zombie;
      }

   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      var1.putShort(this.zombieFlags);
      var1.putUTF(this.attackOutcome);
      var1.putUTF(this.attackPosition);
   }

   public boolean isConsistent() {
      return super.isConsistent() && this.zombie != null;
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tZombie [attack-position=" + this.attackPosition + " | isStaggerBack=" + ((this.zombieFlags & 1) != 0) + " | isFakeDead=" + ((this.zombieFlags & 2) != 0) + " | isBecomeCrawler=" + ((this.zombieFlags & 4) != 0) + " | isCrawling=" + ((this.zombieFlags & 8) != 0) + " | isKnifeDeath=" + ((this.zombieFlags & 16) != 0) + " | isJawStabAttach=" + ((this.zombieFlags & 32) != 0) + " | isHelmetFall=" + ((this.zombieFlags & 64) != 0) + " | attackDidDamage=" + ((this.zombieFlags & 128) != 0) + " | attack-outcome=" + this.attackOutcome + " ]";
   }

   void process() {
      super.process();
      this.zombie.setVariable("AttackOutcome", this.attackOutcome);
      this.zombie.setPlayerAttackPosition(this.attackPosition);
      this.zombie.setStaggerBack((this.zombieFlags & 1) != 0);
      this.zombie.setFakeDead((this.zombieFlags & 2) != 0);
      this.zombie.setBecomeCrawler((this.zombieFlags & 4) != 0);
      this.zombie.setCrawler((this.zombieFlags & 8) != 0);
      this.zombie.setKnifeDeath((this.zombieFlags & 16) != 0);
      this.zombie.setJawStabAttach((this.zombieFlags & 32) != 0);
      this.zombie.setVariable("AttackDidDamage", (this.zombieFlags & 128) != 0);
   }

   protected void react(HandWeapon var1) {
      if (this.zombie.isJawStabAttach()) {
         this.zombie.setAttachedItem("JawStab", var1);
      }

      if (GameServer.bServer && (this.zombieFlags & 64) != 0 && !PersistentOutfits.instance.isHatFallen(this.zombie)) {
         PersistentOutfits.instance.setFallenHat(this.zombie, true);
         if (ServerGUI.isCreated()) {
            PersistentOutfits.instance.removeFallenHat(this.zombie.getPersistentOutfitID(), this.zombie);
            ModelManager.instance.ResetNextFrame(this.zombie);
         }
      }

      this.react();
   }

   IsoGameCharacter getCharacter() {
      return this.zombie;
   }
}
