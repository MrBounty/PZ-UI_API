package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import zombie.characters.IsoLivingCharacter;
import zombie.core.network.ByteBufferWriter;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoMovingObject;
import zombie.network.packets.INetworkPacket;

public class AttackVars implements INetworkPacket {
   private boolean isBareHeadsWeapon;
   public MovingObject targetOnGround = new MovingObject();
   public boolean bAimAtFloor;
   public boolean bCloseKill;
   public boolean bDoShove;
   public float useChargeDelta;
   public int recoilDelay;
   public final ArrayList targetsStanding = new ArrayList();
   public final ArrayList targetsProne = new ArrayList();

   public void setWeapon(HandWeapon var1) {
      this.isBareHeadsWeapon = "BareHands".equals(var1.getType());
   }

   public HandWeapon getWeapon(IsoLivingCharacter var1) {
      return !this.isBareHeadsWeapon && var1.getUseHandWeapon() != null ? var1.getUseHandWeapon() : var1.bareHands;
   }

   public void parse(ByteBuffer var1) {
      byte var2 = var1.get();
      this.isBareHeadsWeapon = (var2 & 1) != 0;
      this.bAimAtFloor = (var2 & 2) != 0;
      this.bCloseKill = (var2 & 4) != 0;
      this.bDoShove = (var2 & 8) != 0;
      this.targetOnGround.parse(var1);
      this.useChargeDelta = var1.getFloat();
      this.recoilDelay = var1.getInt();
      byte var3 = var1.get();
      this.targetsStanding.clear();

      int var4;
      HitInfo var5;
      for(var4 = 0; var4 < var3; ++var4) {
         var5 = new HitInfo();
         var5.parse(var1);
         this.targetsStanding.add(var5);
      }

      var3 = var1.get();
      this.targetsProne.clear();

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = new HitInfo();
         var5.parse(var1);
         this.targetsProne.add(var5);
      }

   }

   public void write(ByteBufferWriter var1) {
      byte var2 = 0;
      byte var6 = (byte)(var2 | (byte)(this.isBareHeadsWeapon ? 1 : 0));
      var6 |= (byte)(this.bAimAtFloor ? 2 : 0);
      var6 |= (byte)(this.bCloseKill ? 4 : 0);
      var6 |= (byte)(this.bDoShove ? 8 : 0);
      var1.putByte(var6);
      this.targetOnGround.write(var1);
      var1.putFloat(this.useChargeDelta);
      var1.putInt(this.recoilDelay);
      byte var3 = (byte)Math.min(100, this.targetsStanding.size());
      var1.putByte(var3);

      int var4;
      HitInfo var5;
      for(var4 = 0; var4 < var3; ++var4) {
         var5 = (HitInfo)this.targetsStanding.get(var4);
         var5.write(var1);
      }

      var3 = (byte)Math.min(100, this.targetsProne.size());
      var1.putByte(var3);

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = (HitInfo)this.targetsProne.get(var4);
         var5.write(var1);
      }

   }

   public int getPacketSizeBytes() {
      int var1 = 11 + this.targetOnGround.getPacketSizeBytes();
      byte var2 = (byte)Math.min(100, this.targetsStanding.size());

      int var3;
      HitInfo var4;
      for(var3 = 0; var3 < var2; ++var3) {
         var4 = (HitInfo)this.targetsStanding.get(var3);
         var1 += var4.getPacketSizeBytes();
      }

      var2 = (byte)Math.min(100, this.targetsProne.size());

      for(var3 = 0; var3 < var2; ++var3) {
         var4 = (HitInfo)this.targetsProne.get(var3);
         var1 += var4.getPacketSizeBytes();
      }

      return var1;
   }

   public String getDescription() {
      String var1 = "";
      byte var2 = (byte)Math.min(100, this.targetsStanding.size());

      for(int var3 = 0; var3 < var2; ++var3) {
         HitInfo var4 = (HitInfo)this.targetsStanding.get(var3);
         var1 = var1 + var4.getDescription();
      }

      String var6 = "";
      var2 = (byte)Math.min(100, this.targetsProne.size());

      for(int var7 = 0; var7 < var2; ++var7) {
         HitInfo var5 = (HitInfo)this.targetsProne.get(var7);
         var6 = var6 + var5.getDescription();
      }

      boolean var10000 = this.isBareHeadsWeapon;
      return "\n\tHitInfo [ isBareHeadsWeapon=" + var10000 + " bAimAtFloor=" + this.bAimAtFloor + " bCloseKill=" + this.bCloseKill + " bDoShove=" + this.bDoShove + " useChargeDelta=" + this.useChargeDelta + " recoilDelay=" + this.recoilDelay + "\n\t  targetOnGround:" + this.targetOnGround.getDescription() + "\n\t  targetsStanding=[" + var1 + "](size=" + this.targetsStanding.size() + ")\n\t  targetsProne=[" + var6 + "](size=" + this.targetsProne.size() + ")]";
   }

   public void copy(AttackVars var1) {
      this.isBareHeadsWeapon = var1.isBareHeadsWeapon;
      this.targetOnGround = var1.targetOnGround;
      this.bAimAtFloor = var1.bAimAtFloor;
      this.bCloseKill = var1.bCloseKill;
      this.bDoShove = var1.bDoShove;
      this.useChargeDelta = var1.useChargeDelta;
      this.recoilDelay = var1.recoilDelay;
      this.targetsStanding.clear();
      Iterator var2 = var1.targetsStanding.iterator();

      HitInfo var3;
      while(var2.hasNext()) {
         var3 = (HitInfo)var2.next();
         this.targetsStanding.add(var3);
      }

      this.targetsProne.clear();
      var2 = var1.targetsProne.iterator();

      while(var2.hasNext()) {
         var3 = (HitInfo)var2.next();
         this.targetsProne.add(var3);
      }

   }

   public void clear() {
      this.targetOnGround.setMovingObject((IsoMovingObject)null);
      this.targetsStanding.clear();
      this.targetsProne.clear();
   }
}
