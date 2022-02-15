package zombie.network.packets.hit;

import java.nio.ByteBuffer;
import zombie.Lua.LuaEventManager;
import zombie.ai.states.SwipeStatePlayer;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoLivingCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.network.ByteBufferWriter;
import zombie.inventory.types.HandWeapon;
import zombie.network.packets.INetworkPacket;

public class WeaponHit extends Hit implements INetworkPacket {
   protected float range;
   protected boolean hitHead;

   public void set(boolean var1, float var2, float var3, float var4, float var5, float var6, boolean var7) {
      super.set(var1, var2, var4, var5, var6);
      this.range = var3;
      this.hitHead = var7;
   }

   public void parse(ByteBuffer var1) {
      super.parse(var1);
      this.range = var1.getFloat();
      this.hitHead = var1.get() != 0;
   }

   public void write(ByteBufferWriter var1) {
      super.write(var1);
      var1.putFloat(this.range);
      var1.putBoolean(this.hitHead);
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tWeapon [ range=" + this.range + " | hitHead=" + this.hitHead + " ]";
   }

   void process(IsoGameCharacter var1, IsoGameCharacter var2, HandWeapon var3) {
      var2.Hit(var3, var1, this.damage, this.ignore, this.range, true);
      super.process(var1, var2);
      LuaEventManager.triggerEvent("OnWeaponHitXp", var1, var3, var2, this.damage);
      if (var1.isAimAtFloor() && !var3.isRanged() && var1.isNPC()) {
         SwipeStatePlayer.splash(var2, var3, var1);
      }

      if (this.hitHead) {
         SwipeStatePlayer.splash(var2, var3, var1);
         SwipeStatePlayer.splash(var2, var3, var1);
         var2.addBlood(BloodBodyPartType.Head, true, true, true);
         var2.addBlood(BloodBodyPartType.Torso_Upper, true, false, false);
         var2.addBlood(BloodBodyPartType.UpperArm_L, true, false, false);
         var2.addBlood(BloodBodyPartType.UpperArm_R, true, false, false);
      }

      if ((!((IsoLivingCharacter)var1).bDoShove || var1.isAimAtFloor()) && var1.DistToSquared(var2) < 2.0F && Math.abs(var1.z - var2.z) < 0.5F) {
         var1.addBlood((BloodBodyPartType)null, false, false, false);
      }

      if (!var2.isDead() && !(var2 instanceof IsoPlayer) && (!((IsoLivingCharacter)var1).bDoShove || var1.isAimAtFloor())) {
         SwipeStatePlayer.splash(var2, var3, var1);
      }

   }
}
