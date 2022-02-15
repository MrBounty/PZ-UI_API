package zombie.characters;

import zombie.WorldSoundManager;
import zombie.Lua.LuaHookManager;
import zombie.ai.states.SwipeStatePlayer;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.skills.PerkFactory;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoCell;
import zombie.iso.IsoMovingObject;
import zombie.iso.Vector2;
import zombie.network.packets.hit.AttackVars;
import zombie.ui.UIManager;

public class IsoLivingCharacter extends IsoGameCharacter {
   public float useChargeDelta = 0.0F;
   public final HandWeapon bareHands = (HandWeapon)InventoryItemFactory.CreateItem("Base.BareHands");
   public boolean bDoShove = false;
   public boolean bCollidedWithPushable = false;
   public IsoGameCharacter targetOnGround;

   public IsoLivingCharacter(IsoCell var1, float var2, float var3, float var4) {
      super(var1, var2, var3, var4);
   }

   public boolean isCollidedWithPushableThisFrame() {
      return this.bCollidedWithPushable;
   }

   public boolean AttemptAttack(float var1) {
      HandWeapon var2 = null;
      if (this.leftHandItem instanceof HandWeapon) {
         var2 = (HandWeapon)this.leftHandItem;
      } else {
         var2 = this.bareHands;
      }

      if (var2 != this.bareHands && this instanceof IsoPlayer) {
         AttackVars var3 = new AttackVars();
         SwipeStatePlayer.instance().CalcAttackVars(this, var3);
         this.setDoShove(var3.bDoShove);
         if (LuaHookManager.TriggerHook("Attack", this, var1, var2)) {
            return false;
         }
      }

      return this.DoAttack(var1);
   }

   public boolean DoAttack(float var1) {
      if (this.isDead()) {
         return false;
      } else {
         if (this.leftHandItem != null) {
            InventoryItem var2 = this.leftHandItem;
            if (var2 instanceof HandWeapon) {
               this.useHandWeapon = (HandWeapon)var2;
               if (this.useHandWeapon.getCondition() <= 0) {
                  return false;
               }

               int var3 = this.Moodles.getMoodleLevel(MoodleType.Endurance);
               if (this.useHandWeapon.isCantAttackWithLowestEndurance() && var3 == 4) {
                  return false;
               }

               int var4 = 0;
               int var5;
               if (this.useHandWeapon.isRanged()) {
                  var5 = this.useHandWeapon.getRecoilDelay();
                  Float var6 = (float)var5 * (1.0F - (float)this.getPerkLevel(PerkFactory.Perks.Aiming) / 30.0F);
                  this.setRecoilDelay((float)var6.intValue());
               }

               if (this instanceof IsoSurvivor && this.useHandWeapon.isRanged() && var4 < this.useHandWeapon.getMaxHitCount()) {
                  for(var5 = 0; var5 < this.getCell().getObjectList().size(); ++var5) {
                     IsoMovingObject var13 = (IsoMovingObject)this.getCell().getObjectList().get(var5);
                     if (var13 != this && var13.isShootable() && this.IsAttackRange(var13.getX(), var13.getY(), var13.getZ())) {
                        float var7 = 1.0F;
                        if (var7 > 0.0F) {
                           Vector2 var8 = new Vector2(this.getX(), this.getY());
                           Vector2 var9 = new Vector2(var13.getX(), var13.getY());
                           var9.x -= var8.x;
                           var9.y -= var8.y;
                           boolean var10 = false;
                           if (var9.x == 0.0F && var9.y == 0.0F) {
                              var10 = true;
                           }

                           Vector2 var11 = this.getForwardDirection();
                           this.DirectionFromVector(var11);
                           var9.normalize();
                           float var12 = var9.dot(var11);
                           if (var10) {
                              var12 = 1.0F;
                           }

                           if (var12 > 1.0F) {
                              var12 = 1.0F;
                           }

                           if (var12 < -1.0F) {
                              var12 = -1.0F;
                           }

                           if (var12 >= this.useHandWeapon.getMinAngle() && var12 <= this.useHandWeapon.getMaxAngle()) {
                              ++var4;
                           }

                           if (var4 >= this.useHandWeapon.getMaxHitCount()) {
                              break;
                           }
                        }
                     }
                  }
               }

               if (UIManager.getPicked() != null) {
                  this.attackTargetSquare = UIManager.getPicked().square;
                  if (UIManager.getPicked().tile instanceof IsoMovingObject) {
                     this.attackTargetSquare = ((IsoMovingObject)UIManager.getPicked().tile).getCurrentSquare();
                  }
               }

               if (this.useHandWeapon.getAmmoType() != null && !this.inventory.contains(this.useHandWeapon.getAmmoType())) {
                  return false;
               }

               if (this.useHandWeapon.getOtherHandRequire() != null && (this.rightHandItem == null || !this.rightHandItem.getType().equals(this.useHandWeapon.getOtherHandRequire()))) {
                  return false;
               }

               if (!this.useHandWeapon.isRanged()) {
                  this.getEmitter().playSound(this.useHandWeapon.getSwingSound(), this);
                  WorldSoundManager.instance.addSound(this, (int)this.getX(), (int)this.getY(), (int)this.getZ(), this.useHandWeapon.getSoundRadius(), this.useHandWeapon.getSoundVolume());
               }

               this.AttackWasSuperAttack = this.superAttack;
               this.changeState(SwipeStatePlayer.instance());
               if (this.useHandWeapon.getAmmoType() != null) {
                  if (this instanceof IsoPlayer) {
                     IsoPlayer.getInstance().inventory.RemoveOneOf(this.useHandWeapon.getAmmoType());
                  } else {
                     this.inventory.RemoveOneOf(this.useHandWeapon.getAmmoType());
                  }
               }

               if (this.useHandWeapon.isUseSelf() && this.leftHandItem != null) {
                  this.leftHandItem.Use();
               }

               if (this.useHandWeapon.isOtherHandUse() && this.rightHandItem != null) {
                  this.rightHandItem.Use();
               }

               return true;
            }
         }

         return false;
      }
   }

   public boolean isDoShove() {
      return this.bDoShove;
   }

   public void setDoShove(boolean var1) {
      this.bDoShove = var1;
   }
}
