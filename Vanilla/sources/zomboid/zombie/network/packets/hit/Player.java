package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.skills.PerkFactory;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.types.HandWeapon;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.packets.INetworkPacket;

public class Player extends Character implements INetworkPacket {
   protected IsoPlayer player;
   protected short playerFlags;
   protected float charge;
   protected float perkAiming;
   protected float combatSpeed;
   protected String attackType;
   protected AttackVars attackVars = new AttackVars();
   ArrayList hitList = new ArrayList();

   public void set(IsoPlayer var1, boolean var2) {
      super.set(var1);
      this.player = var1;
      this.playerFlags = 0;
      this.playerFlags |= (short)(var1.isAimAtFloor() ? 1 : 0);
      this.playerFlags |= (short)(var1.isDoShove() ? 2 : 0);
      this.playerFlags |= (short)(var1.isAttackFromBehind() ? 4 : 0);
      this.playerFlags |= (short)(var2 ? 8 : 0);
      this.charge = var1.useChargeDelta;
      this.perkAiming = (float)var1.getPerkLevel(PerkFactory.Perks.Aiming);
      this.combatSpeed = var1.getVariableFloat("CombatSpeed", 1.0F);
      this.attackType = var1.getAttackType();
      this.attackVars.copy(var1.attackVars);
      this.hitList.clear();
      this.hitList.addAll(var1.hitList);
   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.playerFlags = var1.getShort();
      this.charge = var1.getFloat();
      this.perkAiming = var1.getFloat();
      this.combatSpeed = var1.getFloat();
      this.attackType = GameWindow.ReadString(var1);
      if (GameServer.bServer) {
         this.player = (IsoPlayer)GameServer.IDToPlayerMap.get(this.ID);
         this.character = this.player;
      } else if (GameClient.bClient) {
         this.player = (IsoPlayer)GameClient.IDToPlayerMap.get(this.ID);
         this.character = this.player;
      }

      this.attackVars.parse(var1);
      byte var2 = var1.get();

      for(int var3 = 0; var3 < var2; ++var3) {
         HitInfo var4 = new HitInfo();
         var4.parse(var1);
         this.hitList.add(var4);
      }

   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      var1.putShort(this.playerFlags);
      var1.putFloat(this.charge);
      var1.putFloat(this.perkAiming);
      var1.putFloat(this.combatSpeed);
      var1.putUTF(this.attackType);
      this.attackVars.write(var1);
      byte var2 = (byte)this.hitList.size();
      var1.putByte(var2);

      for(int var3 = 0; var3 < var2; ++var3) {
         ((HitInfo)this.hitList.get(var3)).write(var1);
      }

   }

   public boolean isConsistent() {
      return super.isConsistent() && this.player != null;
   }

   public String getDescription() {
      String var1 = "";
      byte var2 = (byte)Math.min(100, this.hitList.size());

      for(int var3 = 0; var3 < var2; ++var3) {
         HitInfo var4 = (HitInfo)this.hitList.get(var3);
         var1 = var1 + var4.getDescription();
      }

      String var10000 = super.getDescription();
      return var10000 + "\n\tPlayer [ player " + (this.player == null ? "?" : "\"" + this.player.getUsername() + "\"") + " | charge=" + this.charge + " | perkAiming=" + this.perkAiming + " | combatSpeed=" + this.combatSpeed + " | attackType=\"" + this.attackType + "\" | isAimAtFloor=" + ((this.playerFlags & 1) != 0) + " | isDoShove=" + ((this.playerFlags & 2) != 0) + " | isAttackFromBehind=" + ((this.playerFlags & 4) != 0) + " | isCriticalHit=" + ((this.playerFlags & 8) != 0) + " | _bodyDamage=" + (this.player == null ? "?" : this.player.getBodyDamage().getHealth()) + this.attackVars.getDescription() + "\n\t hitList=[" + var1 + "](count=" + this.hitList.size() + ") ]";
   }

   void process() {
      super.process();
      this.player.useChargeDelta = this.charge;
      this.player.setVariable("recoilVarX", this.perkAiming / 10.0F);
      this.player.setAttackType(this.attackType);
      this.player.setVariable("CombatSpeed", this.combatSpeed);
      this.player.setVariable("AimFloorAnim", (this.playerFlags & 1) != 0);
      this.player.setAimAtFloor((this.playerFlags & 1) != 0);
      this.player.setDoShove((this.playerFlags & 2) != 0);
      this.player.setAttackFromBehind((this.playerFlags & 4) != 0);
      this.player.setCriticalHit((this.playerFlags & 8) != 0);
   }

   void attack(HandWeapon var1) {
      this.player.attackStarted = false;
      this.player.attackVars.copy(this.attackVars);
      this.player.hitList.clear();
      this.player.hitList.addAll(this.hitList);
      this.player.pressedAttack(false);
      if (this.player.isAttackStarted() && var1.isRanged() && !this.player.isDoShove()) {
         this.player.startMuzzleFlash();
      }

      if (var1.getPhysicsObject() != null) {
         this.player.Throw(var1);
      }

   }

   IsoGameCharacter getCharacter() {
      return this.player;
   }

   boolean isRelevant(UdpConnection var1) {
      return var1.RelevantTo(this.positionX, this.positionY);
   }
}
