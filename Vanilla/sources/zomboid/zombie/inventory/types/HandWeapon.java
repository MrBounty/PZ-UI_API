package zombie.inventory.types;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.SurvivorDesc;
import zombie.characters.skills.PerkFactory;
import zombie.core.BoxedStaticValues;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.Translator;
import zombie.core.math.PZMath;
import zombie.debug.DebugLog;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemType;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.ui.ObjectTooltip;
import zombie.util.StringUtils;
import zombie.util.io.BitHeader;
import zombie.util.io.BitHeaderRead;
import zombie.util.io.BitHeaderWrite;

public final class HandWeapon extends InventoryItem {
   public float WeaponLength;
   public float SplatSize = 1.0F;
   private int ammoPerShoot = 1;
   private String magazineType = null;
   protected boolean angleFalloff = false;
   protected boolean bCanBarracade = false;
   protected float doSwingBeforeImpact = 0.0F;
   protected String impactSound = "BaseballBatHit";
   protected boolean knockBackOnNoDeath = true;
   protected float maxAngle = 1.0F;
   protected float maxDamage = 1.5F;
   protected int maxHitCount = 1000;
   protected float maxRange = 1.0F;
   protected boolean ranged = false;
   protected float minAngle = 0.5F;
   protected float minDamage = 0.4F;
   protected float minimumSwingTime = 0.5F;
   protected float minRange = 0.0F;
   protected float noiseFactor = 0.0F;
   protected String otherHandRequire = null;
   protected boolean otherHandUse = false;
   protected String physicsObject = null;
   protected float pushBackMod = 1.0F;
   protected boolean rangeFalloff = false;
   protected boolean shareDamage = true;
   protected int soundRadius = 0;
   protected int soundVolume = 0;
   protected boolean splatBloodOnNoDeath = false;
   protected int splatNumber = 2;
   protected String swingSound = "BaseballBatSwing";
   protected float swingTime = 1.0F;
   protected float toHitModifier = 1.0F;
   protected boolean useEndurance = true;
   protected boolean useSelf = false;
   protected String weaponSprite = null;
   private String originalWeaponSprite = null;
   protected float otherBoost = 1.0F;
   protected int DoorDamage = 1;
   protected String doorHitSound = "BaseballBatHit";
   protected int ConditionLowerChance = 10000;
   protected boolean MultipleHitConditionAffected = true;
   protected boolean shareEndurance = true;
   protected boolean AlwaysKnockdown = false;
   protected float EnduranceMod = 1.0F;
   protected float KnockdownMod = 1.0F;
   protected boolean CantAttackWithLowestEndurance = false;
   public boolean bIsAimedFirearm = false;
   public boolean bIsAimedHandWeapon = false;
   public String RunAnim = "Run";
   public String IdleAnim = "Idle";
   public float HitAngleMod = 0.0F;
   private String SubCategory = "";
   private ArrayList Categories = null;
   private int AimingPerkCritModifier = 0;
   private float AimingPerkRangeModifier = 0.0F;
   private float AimingPerkHitChanceModifier = 0.0F;
   private int HitChance = 0;
   private float AimingPerkMinAngleModifier = 0.0F;
   private int RecoilDelay = 0;
   private boolean PiercingBullets = false;
   private float soundGain = 1.0F;
   private WeaponPart scope = null;
   private WeaponPart canon = null;
   private WeaponPart clip = null;
   private WeaponPart recoilpad = null;
   private WeaponPart sling = null;
   private WeaponPart stock = null;
   private int ClipSize = 0;
   private int reloadTime = 0;
   private int aimingTime = 0;
   private float minRangeRanged = 0.0F;
   private int treeDamage = 0;
   private String bulletOutSound = null;
   private String shellFallSound = null;
   private int triggerExplosionTimer = 0;
   private boolean canBePlaced = false;
   private int explosionRange = 0;
   private int explosionPower = 0;
   private int fireRange = 0;
   private int firePower = 0;
   private int smokeRange = 0;
   private int noiseRange = 0;
   private float extraDamage = 0.0F;
   private int explosionTimer = 0;
   private String placedSprite = null;
   private boolean canBeReused = false;
   private int sensorRange = 0;
   private float critDmgMultiplier = 2.0F;
   private float baseSpeed = 1.0F;
   private float bloodLevel = 0.0F;
   private String ammoBox = null;
   private String insertAmmoStartSound = null;
   private String insertAmmoSound = null;
   private String insertAmmoStopSound = null;
   private String ejectAmmoStartSound = null;
   private String ejectAmmoSound = null;
   private String ejectAmmoStopSound = null;
   private String rackSound = null;
   private String clickSound = "Stormy9mmClick";
   private boolean containsClip = false;
   private String weaponReloadType = "handgun";
   private boolean rackAfterShoot = false;
   private boolean roundChambered = false;
   private boolean bSpentRoundChambered = false;
   private int spentRoundCount = 0;
   private float jamGunChance = 5.0F;
   private boolean isJammed = false;
   private ArrayList modelWeaponPart = null;
   private boolean haveChamber = true;
   private String bulletName = null;
   private String damageCategory = null;
   private boolean damageMakeHole = false;
   private String hitFloorSound = "BatOnFloor";
   private boolean insertAllBulletsReload = false;
   private String fireMode = null;
   private ArrayList fireModePossibilities = null;
   public int ProjectileCount = 1;
   public float aimingMod = 1.0F;
   public float CriticalChance = 20.0F;
   private String hitSound = "BaseballBatHit";

   public float getSplatSize() {
      return this.SplatSize;
   }

   public boolean CanStack(InventoryItem var1) {
      return false;
   }

   public String getCategory() {
      return this.mainCategory != null ? this.mainCategory : "Weapon";
   }

   public HandWeapon(String var1, String var2, String var3, String var4) {
      super(var1, var2, var3, var4);
      this.cat = ItemType.Weapon;
   }

   public HandWeapon(String var1, String var2, String var3, Item var4) {
      super(var1, var2, var3, var4);
      this.cat = ItemType.Weapon;
   }

   public boolean IsWeapon() {
      return true;
   }

   public int getSaveType() {
      return Item.Type.Weapon.ordinal();
   }

   public float getScore(SurvivorDesc var1) {
      float var2 = 0.0F;
      if (this.getAmmoType() != null && !this.getAmmoType().equals("none") && !this.container.contains(this.getAmmoType())) {
         var2 -= 100000.0F;
      }

      if (this.Condition == 0) {
         var2 -= 100000.0F;
      }

      var2 += this.maxDamage * 10.0F;
      var2 += this.maxAngle * 5.0F;
      var2 -= this.minimumSwingTime * 0.1F;
      var2 -= this.swingTime;
      if (var1 != null && var1.getInstance().getThreatLevel() <= 2 && this.soundRadius > 5) {
         if (var2 > 0.0F && (float)this.soundRadius > var2) {
            var2 = 1.0F;
         }

         var2 -= (float)this.soundRadius;
      }

      return var2;
   }

   public float getContentsWeight() {
      float var1 = 0.0F;
      Item var2;
      if (this.haveChamber() && this.isRoundChambered() && !StringUtils.isNullOrWhitespace(this.getAmmoType())) {
         var2 = ScriptManager.instance.FindItem(this.getAmmoType());
         if (var2 != null) {
            var1 += var2.getActualWeight();
         }
      }

      if (this.isContainsClip() && !StringUtils.isNullOrWhitespace(this.getMagazineType())) {
         var2 = ScriptManager.instance.FindItem(this.getMagazineType());
         if (var2 != null) {
            var1 += var2.getActualWeight();
         }
      }

      return var1 + super.getContentsWeight();
   }

   public void DoTooltip(ObjectTooltip var1, ObjectTooltip.Layout var2) {
      float var4 = 1.0F;
      float var5 = 1.0F;
      float var6 = 0.8F;
      float var7 = 1.0F;
      float var8 = 0.0F;
      float var9 = 0.6F;
      float var10 = 0.0F;
      float var11 = 0.7F;
      ObjectTooltip.LayoutItem var3 = var2.addItem();
      var3.setLabel(Translator.getText("Tooltip_weapon_Condition") + ":", var4, var5, var6, var7);
      float var12 = (float)this.Condition / (float)this.ConditionMax;
      var3.setProgress(var12, var8, var9, var10, var11);
      float var13;
      float var14;
      if (this.getMaxDamage() > 0.0F) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_weapon_Damage") + ":", var4, var5, var6, var7);
         var12 = this.getMaxDamage() + this.getMinDamage();
         var13 = 5.0F;
         var14 = var12 / var13;
         var3.setProgress(var14, var8, var9, var10, var11);
      }

      if (this.isRanged()) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_weapon_Range") + ":", var4, var5, var6, 1.0F);
         var12 = this.getMaxRange(IsoPlayer.getInstance());
         var13 = 40.0F;
         var14 = var12 / var13;
         var3.setProgress(var14, var8, var9, var10, var11);
      }

      if (this.isTwoHandWeapon() && !this.isRequiresEquippedBothHands()) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_item_TwoHandWeapon"), var4, var5, var6, var7);
      }

      if (!StringUtils.isNullOrEmpty(this.getFireMode())) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_item_FireMode") + ":", var4, var5, var6, var7);
         var3.setValue(Translator.getText("ContextMenu_FireMode_" + this.getFireMode()), 1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (this.CantAttackWithLowestEndurance) {
         var3 = var2.addItem();
         var3.setLabel(Translator.getText("Tooltip_weapon_Unusable_at_max_exertion"), 1.0F, 0.0F, 0.0F, 1.0F);
      }

      String var19 = this.getAmmoType();
      if (Core.getInstance().isNewReloading()) {
         String var20;
         if (this.getMaxAmmo() > 0) {
            var20 = String.valueOf(this.getCurrentAmmoCount());
            if (this.isRoundChambered()) {
               var20 = var20 + "+1";
            }

            var3 = var2.addItem();
            if (this.bulletName == null) {
               if (this.getMagazineType() != null) {
                  this.bulletName = InventoryItemFactory.CreateItem(this.getMagazineType()).getDisplayName();
               } else {
                  this.bulletName = InventoryItemFactory.CreateItem(this.getAmmoType()).getDisplayName();
               }
            }

            var3.setLabel(this.bulletName + ":", 1.0F, 1.0F, 0.8F, 1.0F);
            var3.setValue(var20 + " / " + this.getMaxAmmo(), 1.0F, 1.0F, 1.0F, 1.0F);
         }

         if (this.isJammed()) {
            var3 = var2.addItem();
            var3.setLabel(Translator.getText("Tooltip_weapon_Jammed"), 1.0F, 0.1F, 0.1F, 1.0F);
         } else if (this.haveChamber() && !this.isRoundChambered() && this.getCurrentAmmoCount() > 0) {
            var3 = var2.addItem();
            var20 = this.isSpentRoundChambered() ? "Tooltip_weapon_SpentRoundChambered" : "Tooltip_weapon_NoRoundChambered";
            var3.setLabel(Translator.getText(var20), 1.0F, 0.1F, 0.1F, 1.0F);
         } else if (this.getSpentRoundCount() > 0) {
            var3 = var2.addItem();
            var3.setLabel("Spent Rounds:", 1.0F, 0.1F, 0.1F, 1.0F);
            var3.setValue(this.getSpentRoundCount() + " / " + this.getMaxAmmo(), 1.0F, 1.0F, 1.0F, 1.0F);
         }

         if (!StringUtils.isNullOrEmpty(this.getMagazineType())) {
            if (this.isContainsClip()) {
               var3 = var2.addItem();
               var3.setLabel(Translator.getText("Tooltip_weapon_ContainsClip"), 1.0F, 1.0F, 0.8F, 1.0F);
            } else {
               var3 = var2.addItem();
               var3.setLabel(Translator.getText("Tooltip_weapon_NoClip"), 1.0F, 1.0F, 0.8F, 1.0F);
            }
         }
      } else {
         if (var19 == null && this.hasModData()) {
            Object var22 = this.getModData().rawget("defaultAmmo");
            if (var22 instanceof String) {
               var19 = (String)var22;
            }
         }

         if (var19 != null) {
            Item var23 = ScriptManager.instance.FindItem(var19);
            if (var23 == null) {
               ScriptManager var10000 = ScriptManager.instance;
               String var10001 = this.getModule();
               var23 = var10000.FindItem(var10001 + "." + var19);
            }

            if (var23 != null) {
               var3 = var2.addItem();
               var3.setLabel(Translator.getText("Tooltip_weapon_Ammo") + ":", var4, var5, var6, var7);
               var3.setValue(var23.getDisplayName(), 1.0F, 1.0F, 1.0F, 1.0F);
            }

            Object var21 = this.getModData().rawget("currentCapacity");
            Object var15 = this.getModData().rawget("maxCapacity");
            if (var21 instanceof Double && var15 instanceof Double) {
               int var25 = ((Double)var21).intValue();
               String var16 = var25 + " / " + ((Double)var15).intValue();
               Object var17 = this.getModData().rawget("roundChambered");
               if (var17 instanceof Double && ((Double)var17).intValue() == 1) {
                  var25 = ((Double)var21).intValue();
                  var16 = var25 + "+1 / " + ((Double)var15).intValue();
               } else {
                  Object var18 = this.getModData().rawget("emptyShellChambered");
                  if (var18 instanceof Double && ((Double)var18).intValue() == 1) {
                     var25 = ((Double)var21).intValue();
                     var16 = var25 + "+x / " + ((Double)var15).intValue();
                  }
               }

               var3 = var2.addItem();
               var3.setLabel(Translator.getText("Tooltip_weapon_AmmoCount") + ":", 1.0F, 1.0F, 0.8F, 1.0F);
               var3.setValue(var16, 1.0F, 1.0F, 1.0F, 1.0F);
            }
         }
      }

      ObjectTooltip.Layout var24 = var1.beginLayout();
      if (this.getStock() != null) {
         var3 = var24.addItem();
         var3.setLabel(Translator.getText("Tooltip_weapon_Stock") + ":", var4, var5, var6, var7);
         var3.setValue(this.getStock().getName(), 1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (this.getSling() != null) {
         var3 = var24.addItem();
         var3.setLabel(Translator.getText("Tooltip_weapon_Sling") + ":", var4, var5, var6, var7);
         var3.setValue(this.getSling().getName(), 1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (this.getScope() != null) {
         var3 = var24.addItem();
         var3.setLabel(Translator.getText("Tooltip_weapon_Scope") + ":", var4, var5, var6, var7);
         var3.setValue(this.getScope().getName(), 1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (this.getCanon() != null) {
         var3 = var24.addItem();
         var3.setLabel(Translator.getText("Tooltip_weapon_Canon") + ":", var4, var5, var6, var7);
         var3.setValue(this.getCanon().getName(), 1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (this.getClip() != null) {
         var3 = var24.addItem();
         var3.setLabel(Translator.getText("Tooltip_weapon_Clip") + ":", var4, var5, var6, var7);
         var3.setValue(this.getClip().getName(), 1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (this.getRecoilpad() != null) {
         var3 = var24.addItem();
         var3.setLabel(Translator.getText("Tooltip_weapon_RecoilPad") + ":", var4, var5, var6, var7);
         var3.setValue(this.getRecoilpad().getName(), 1.0F, 1.0F, 1.0F, 1.0F);
      }

      if (!var24.items.isEmpty()) {
         var2.next = var24;
         var24.nextPadY = var1.getLineSpacing();
      } else {
         var1.endLayout(var24);
      }

   }

   public float getDamageMod(IsoGameCharacter var1) {
      int var2 = var1.getPerkLevel(PerkFactory.Perks.Blunt);
      if (this.ScriptItem.Categories.contains("Blunt")) {
         if (var2 >= 3 && var2 <= 6) {
            return 1.1F;
         }

         if (var2 >= 7) {
            return 1.2F;
         }
      }

      int var3 = var1.getPerkLevel(PerkFactory.Perks.Axe);
      if (this.ScriptItem.Categories.contains("Axe")) {
         if (var3 >= 3 && var3 <= 6) {
            return 1.1F;
         }

         if (var3 >= 7) {
            return 1.2F;
         }
      }

      int var4 = var1.getPerkLevel(PerkFactory.Perks.Spear);
      if (this.ScriptItem.Categories.contains("Spear")) {
         if (var4 >= 3 && var4 <= 6) {
            return 1.1F;
         }

         if (var4 >= 7) {
            return 1.2F;
         }
      }

      return 1.0F;
   }

   public float getRangeMod(IsoGameCharacter var1) {
      int var2 = var1.getPerkLevel(PerkFactory.Perks.Blunt);
      if (this.ScriptItem.Categories.contains("Blunt") && var2 >= 7) {
         return 1.2F;
      } else {
         int var3 = var1.getPerkLevel(PerkFactory.Perks.Axe);
         if (this.ScriptItem.Categories.contains("Axe") && var3 >= 7) {
            return 1.2F;
         } else {
            int var4 = var1.getPerkLevel(PerkFactory.Perks.Spear);
            return this.ScriptItem.Categories.contains("Spear") && var4 >= 7 ? 1.2F : 1.0F;
         }
      }
   }

   public float getFatigueMod(IsoGameCharacter var1) {
      int var2 = var1.getPerkLevel(PerkFactory.Perks.Blunt);
      if (this.ScriptItem.Categories.contains("Blunt") && var2 >= 8) {
         return 0.8F;
      } else {
         int var3 = var1.getPerkLevel(PerkFactory.Perks.Axe);
         if (this.ScriptItem.Categories.contains("Axe") && var3 >= 8) {
            return 0.8F;
         } else {
            int var4 = var1.getPerkLevel(PerkFactory.Perks.Spear);
            return this.ScriptItem.Categories.contains("Spear") && var4 >= 8 ? 0.8F : 1.0F;
         }
      }
   }

   public float getKnockbackMod(IsoGameCharacter var1) {
      int var2 = var1.getPerkLevel(PerkFactory.Perks.Axe);
      return this.ScriptItem.Categories.contains("Axe") && var2 >= 6 ? 2.0F : 1.0F;
   }

   public float getSpeedMod(IsoGameCharacter var1) {
      int var2;
      if (this.ScriptItem.Categories.contains("Blunt")) {
         var2 = var1.getPerkLevel(PerkFactory.Perks.Blunt);
         if (var2 >= 10) {
            return 0.65F;
         }

         if (var2 >= 9) {
            return 0.68F;
         }

         if (var2 >= 8) {
            return 0.71F;
         }

         if (var2 >= 7) {
            return 0.74F;
         }

         if (var2 >= 6) {
            return 0.77F;
         }

         if (var2 >= 5) {
            return 0.8F;
         }

         if (var2 >= 4) {
            return 0.83F;
         }

         if (var2 >= 3) {
            return 0.86F;
         }

         if (var2 >= 2) {
            return 0.9F;
         }

         if (var2 >= 1) {
            return 0.95F;
         }
      }

      if (this.ScriptItem.Categories.contains("Axe")) {
         var2 = var1.getPerkLevel(PerkFactory.Perks.Axe);
         float var3 = 1.0F;
         if (var1.Traits.Axeman.isSet()) {
            var3 = 0.95F;
         }

         if (var2 >= 10) {
            return 0.65F * var3;
         } else if (var2 >= 9) {
            return 0.68F * var3;
         } else if (var2 >= 8) {
            return 0.71F * var3;
         } else if (var2 >= 7) {
            return 0.74F * var3;
         } else if (var2 >= 6) {
            return 0.77F * var3;
         } else if (var2 >= 5) {
            return 0.8F * var3;
         } else if (var2 >= 4) {
            return 0.83F * var3;
         } else if (var2 >= 3) {
            return 0.86F * var3;
         } else if (var2 >= 2) {
            return 0.9F * var3;
         } else {
            return var2 >= 1 ? 0.95F * var3 : 1.0F * var3;
         }
      } else {
         if (this.ScriptItem.Categories.contains("Spear")) {
            var2 = var1.getPerkLevel(PerkFactory.Perks.Spear);
            if (var2 >= 10) {
               return 0.65F;
            }

            if (var2 >= 9) {
               return 0.68F;
            }

            if (var2 >= 8) {
               return 0.71F;
            }

            if (var2 >= 7) {
               return 0.74F;
            }

            if (var2 >= 6) {
               return 0.77F;
            }

            if (var2 >= 5) {
               return 0.8F;
            }

            if (var2 >= 4) {
               return 0.83F;
            }

            if (var2 >= 3) {
               return 0.86F;
            }

            if (var2 >= 2) {
               return 0.9F;
            }

            if (var2 >= 1) {
               return 0.95F;
            }
         }

         return 1.0F;
      }
   }

   public float getToHitMod(IsoGameCharacter var1) {
      int var2 = var1.getPerkLevel(PerkFactory.Perks.Blunt);
      if (this.ScriptItem.Categories.contains("Blunt")) {
         if (var2 == 1) {
            return 1.2F;
         }

         if (var2 == 2) {
            return 1.3F;
         }

         if (var2 == 3) {
            return 1.4F;
         }

         if (var2 == 4) {
            return 1.5F;
         }

         if (var2 == 5) {
            return 1.6F;
         }

         if (var2 == 6) {
            return 1.7F;
         }

         if (var2 == 7) {
            return 1.8F;
         }

         if (var2 == 8) {
            return 1.9F;
         }

         if (var2 == 9) {
            return 2.0F;
         }

         if (var2 == 10) {
            return 100.0F;
         }
      }

      int var3 = var1.getPerkLevel(PerkFactory.Perks.Axe);
      if (this.ScriptItem.Categories.contains("Axe")) {
         if (var3 == 1) {
            return 1.2F;
         }

         if (var3 == 2) {
            return 1.3F;
         }

         if (var3 == 3) {
            return 1.4F;
         }

         if (var3 == 4) {
            return 1.5F;
         }

         if (var3 == 5) {
            return 1.6F;
         }

         if (var3 == 6) {
            return 1.7F;
         }

         if (var3 == 7) {
            return 1.8F;
         }

         if (var3 == 8) {
            return 1.9F;
         }

         if (var3 == 9) {
            return 2.0F;
         }

         if (var3 == 10) {
            return 100.0F;
         }
      }

      int var4 = var1.getPerkLevel(PerkFactory.Perks.Spear);
      if (this.ScriptItem.Categories.contains("Spear")) {
         if (var3 == 1) {
            return 1.2F;
         }

         if (var3 == 2) {
            return 1.3F;
         }

         if (var3 == 3) {
            return 1.4F;
         }

         if (var3 == 4) {
            return 1.5F;
         }

         if (var3 == 5) {
            return 1.6F;
         }

         if (var3 == 6) {
            return 1.7F;
         }

         if (var3 == 7) {
            return 1.8F;
         }

         if (var3 == 8) {
            return 1.9F;
         }

         if (var3 == 9) {
            return 2.0F;
         }

         if (var3 == 10) {
            return 100.0F;
         }
      }

      return 1.0F;
   }

   public boolean isAngleFalloff() {
      return this.angleFalloff;
   }

   public void setAngleFalloff(boolean var1) {
      this.angleFalloff = var1;
   }

   public boolean isCanBarracade() {
      return this.bCanBarracade;
   }

   public void setCanBarracade(boolean var1) {
      this.bCanBarracade = var1;
   }

   public float getDoSwingBeforeImpact() {
      return this.doSwingBeforeImpact;
   }

   public void setDoSwingBeforeImpact(float var1) {
      this.doSwingBeforeImpact = var1;
   }

   public String getImpactSound() {
      return this.impactSound;
   }

   public void setImpactSound(String var1) {
      this.impactSound = var1;
   }

   public boolean isKnockBackOnNoDeath() {
      return this.knockBackOnNoDeath;
   }

   public void setKnockBackOnNoDeath(boolean var1) {
      this.knockBackOnNoDeath = var1;
   }

   public float getMaxAngle() {
      return this.maxAngle;
   }

   public void setMaxAngle(float var1) {
      this.maxAngle = var1;
   }

   public float getMaxDamage() {
      return this.maxDamage;
   }

   public void setMaxDamage(float var1) {
      this.maxDamage = var1;
   }

   public int getMaxHitCount() {
      return this.maxHitCount;
   }

   public void setMaxHitCount(int var1) {
      this.maxHitCount = var1;
   }

   public float getMaxRange() {
      return this.maxRange;
   }

   public float getMaxRange(IsoGameCharacter var1) {
      return this.isRanged() ? this.maxRange + this.getAimingPerkRangeModifier() * ((float)var1.getPerkLevel(PerkFactory.Perks.Aiming) / 2.0F) : this.maxRange;
   }

   public void setMaxRange(float var1) {
      this.maxRange = var1;
   }

   public boolean isRanged() {
      return this.ranged;
   }

   public void setRanged(boolean var1) {
      this.ranged = var1;
   }

   public float getMinAngle() {
      return this.minAngle;
   }

   public void setMinAngle(float var1) {
      this.minAngle = var1;
   }

   public float getMinDamage() {
      return this.minDamage;
   }

   public void setMinDamage(float var1) {
      this.minDamage = var1;
   }

   public float getMinimumSwingTime() {
      return this.minimumSwingTime;
   }

   public void setMinimumSwingTime(float var1) {
      this.minimumSwingTime = var1;
   }

   public float getMinRange() {
      return this.minRange;
   }

   public void setMinRange(float var1) {
      this.minRange = var1;
   }

   public float getNoiseFactor() {
      return this.noiseFactor;
   }

   public void setNoiseFactor(float var1) {
      this.noiseFactor = var1;
   }

   public String getOtherHandRequire() {
      return this.otherHandRequire;
   }

   public void setOtherHandRequire(String var1) {
      this.otherHandRequire = var1;
   }

   public boolean isOtherHandUse() {
      return this.otherHandUse;
   }

   public void setOtherHandUse(boolean var1) {
      this.otherHandUse = var1;
   }

   public String getPhysicsObject() {
      return this.physicsObject;
   }

   public void setPhysicsObject(String var1) {
      this.physicsObject = var1;
   }

   public float getPushBackMod() {
      return this.pushBackMod;
   }

   public void setPushBackMod(float var1) {
      this.pushBackMod = var1;
   }

   public boolean isRangeFalloff() {
      return this.rangeFalloff;
   }

   public void setRangeFalloff(boolean var1) {
      this.rangeFalloff = var1;
   }

   public boolean isShareDamage() {
      return this.shareDamage;
   }

   public void setShareDamage(boolean var1) {
      this.shareDamage = var1;
   }

   public int getSoundRadius() {
      return this.soundRadius;
   }

   public void setSoundRadius(int var1) {
      this.soundRadius = var1;
   }

   public int getSoundVolume() {
      return this.soundVolume;
   }

   public void setSoundVolume(int var1) {
      this.soundVolume = var1;
   }

   public boolean isSplatBloodOnNoDeath() {
      return this.splatBloodOnNoDeath;
   }

   public void setSplatBloodOnNoDeath(boolean var1) {
      this.splatBloodOnNoDeath = var1;
   }

   public int getSplatNumber() {
      return this.splatNumber;
   }

   public void setSplatNumber(int var1) {
      this.splatNumber = var1;
   }

   public String getSwingSound() {
      return this.swingSound;
   }

   public void setSwingSound(String var1) {
      this.swingSound = var1;
   }

   public float getSwingTime() {
      return this.swingTime;
   }

   public void setSwingTime(float var1) {
      this.swingTime = var1;
   }

   public float getToHitModifier() {
      return this.toHitModifier;
   }

   public void setToHitModifier(float var1) {
      this.toHitModifier = var1;
   }

   public boolean isUseEndurance() {
      return this.useEndurance;
   }

   public void setUseEndurance(boolean var1) {
      this.useEndurance = var1;
   }

   public boolean isUseSelf() {
      return this.useSelf;
   }

   public void setUseSelf(boolean var1) {
      this.useSelf = var1;
   }

   public String getWeaponSprite() {
      return this.weaponSprite;
   }

   public void setWeaponSprite(String var1) {
      this.weaponSprite = var1;
   }

   public float getOtherBoost() {
      return this.otherBoost;
   }

   public void setOtherBoost(float var1) {
      this.otherBoost = var1;
   }

   public int getDoorDamage() {
      return this.DoorDamage;
   }

   public void setDoorDamage(int var1) {
      this.DoorDamage = var1;
   }

   public String getDoorHitSound() {
      return this.doorHitSound;
   }

   public void setDoorHitSound(String var1) {
      this.doorHitSound = var1;
   }

   public int getConditionLowerChance() {
      return this.ConditionLowerChance;
   }

   public void setConditionLowerChance(int var1) {
      this.ConditionLowerChance = var1;
   }

   public boolean isMultipleHitConditionAffected() {
      return this.MultipleHitConditionAffected;
   }

   public void setMultipleHitConditionAffected(boolean var1) {
      this.MultipleHitConditionAffected = var1;
   }

   public boolean isShareEndurance() {
      return this.shareEndurance;
   }

   public void setShareEndurance(boolean var1) {
      this.shareEndurance = var1;
   }

   public boolean isAlwaysKnockdown() {
      return this.AlwaysKnockdown;
   }

   public void setAlwaysKnockdown(boolean var1) {
      this.AlwaysKnockdown = var1;
   }

   public float getEnduranceMod() {
      return this.EnduranceMod;
   }

   public void setEnduranceMod(float var1) {
      this.EnduranceMod = var1;
   }

   public float getKnockdownMod() {
      return this.KnockdownMod;
   }

   public void setKnockdownMod(float var1) {
      this.KnockdownMod = var1;
   }

   public boolean isCantAttackWithLowestEndurance() {
      return this.CantAttackWithLowestEndurance;
   }

   public void setCantAttackWithLowestEndurance(boolean var1) {
      this.CantAttackWithLowestEndurance = var1;
   }

   public boolean isAimedFirearm() {
      return this.bIsAimedFirearm;
   }

   public boolean isAimedHandWeapon() {
      return this.bIsAimedHandWeapon;
   }

   public int getProjectileCount() {
      return this.ProjectileCount;
   }

   public void setProjectileCount(int var1) {
      this.ProjectileCount = var1;
   }

   public float getAimingMod() {
      return this.aimingMod;
   }

   public boolean isAimed() {
      return this.bIsAimedFirearm || this.bIsAimedHandWeapon;
   }

   public void setCriticalChance(float var1) {
      this.CriticalChance = var1;
   }

   public float getCriticalChance() {
      return this.CriticalChance;
   }

   public void setSubCategory(String var1) {
      this.SubCategory = var1;
   }

   public String getSubCategory() {
      return this.SubCategory;
   }

   public void setZombieHitSound(String var1) {
      this.hitSound = var1;
   }

   public String getZombieHitSound() {
      return this.hitSound;
   }

   public ArrayList getCategories() {
      return this.Categories;
   }

   public void setCategories(ArrayList var1) {
      this.Categories = var1;
   }

   public int getAimingPerkCritModifier() {
      return this.AimingPerkCritModifier;
   }

   public void setAimingPerkCritModifier(int var1) {
      this.AimingPerkCritModifier = var1;
   }

   public float getAimingPerkRangeModifier() {
      return this.AimingPerkRangeModifier;
   }

   public void setAimingPerkRangeModifier(float var1) {
      this.AimingPerkRangeModifier = var1;
   }

   public int getHitChance() {
      return this.HitChance;
   }

   public void setHitChance(int var1) {
      this.HitChance = var1;
   }

   public float getAimingPerkHitChanceModifier() {
      return this.AimingPerkHitChanceModifier;
   }

   public void setAimingPerkHitChanceModifier(float var1) {
      this.AimingPerkHitChanceModifier = var1;
   }

   public float getAimingPerkMinAngleModifier() {
      return this.AimingPerkMinAngleModifier;
   }

   public void setAimingPerkMinAngleModifier(float var1) {
      this.AimingPerkMinAngleModifier = var1;
   }

   public int getRecoilDelay() {
      return this.RecoilDelay;
   }

   public void setRecoilDelay(int var1) {
      this.RecoilDelay = var1;
   }

   public boolean isPiercingBullets() {
      return this.PiercingBullets;
   }

   public void setPiercingBullets(boolean var1) {
      this.PiercingBullets = var1;
   }

   public float getSoundGain() {
      return this.soundGain;
   }

   public void setSoundGain(float var1) {
      this.soundGain = var1;
   }

   public WeaponPart getScope() {
      return this.scope;
   }

   public void setScope(WeaponPart var1) {
      this.scope = var1;
   }

   public WeaponPart getClip() {
      return this.clip;
   }

   public void setClip(WeaponPart var1) {
      this.clip = var1;
   }

   public WeaponPart getCanon() {
      return this.canon;
   }

   public void setCanon(WeaponPart var1) {
      this.canon = var1;
   }

   public WeaponPart getRecoilpad() {
      return this.recoilpad;
   }

   public void setRecoilpad(WeaponPart var1) {
      this.recoilpad = var1;
   }

   public int getClipSize() {
      return this.ClipSize;
   }

   public void setClipSize(int var1) {
      this.ClipSize = var1;
      this.getModData().rawset("maxCapacity", BoxedStaticValues.toDouble((double)var1));
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      BitHeaderWrite var3 = BitHeader.allocWrite(BitHeader.HeaderSize.Integer, var1);
      if (this.maxRange != 1.0F) {
         var3.addFlags(1);
         var1.putFloat(this.maxRange);
      }

      if (this.minRangeRanged != 0.0F) {
         var3.addFlags(2);
         var1.putFloat(this.minRangeRanged);
      }

      if (this.ClipSize != 0) {
         var3.addFlags(4);
         var1.putInt(this.ClipSize);
      }

      if (this.minDamage != 0.4F) {
         var3.addFlags(8);
         var1.putFloat(this.minDamage);
      }

      if (this.maxDamage != 1.5F) {
         var3.addFlags(16);
         var1.putFloat(this.maxDamage);
      }

      if (this.RecoilDelay != 0) {
         var3.addFlags(32);
         var1.putInt(this.RecoilDelay);
      }

      if (this.aimingTime != 0) {
         var3.addFlags(64);
         var1.putInt(this.aimingTime);
      }

      if (this.reloadTime != 0) {
         var3.addFlags(128);
         var1.putInt(this.reloadTime);
      }

      if (this.HitChance != 0) {
         var3.addFlags(256);
         var1.putInt(this.HitChance);
      }

      if (this.minAngle != 0.5F) {
         var3.addFlags(512);
         var1.putFloat(this.minAngle);
      }

      if (this.getScope() != null) {
         var3.addFlags(1024);
         var1.putShort(this.getScope().getRegistry_id());
      }

      if (this.getClip() != null) {
         var3.addFlags(2048);
         var1.putShort(this.getClip().getRegistry_id());
      }

      if (this.getRecoilpad() != null) {
         var3.addFlags(4096);
         var1.putShort(this.getRecoilpad().getRegistry_id());
      }

      if (this.getSling() != null) {
         var3.addFlags(8192);
         var1.putShort(this.getSling().getRegistry_id());
      }

      if (this.getStock() != null) {
         var3.addFlags(16384);
         var1.putShort(this.getStock().getRegistry_id());
      }

      if (this.getCanon() != null) {
         var3.addFlags(32768);
         var1.putShort(this.getCanon().getRegistry_id());
      }

      if (this.getExplosionTimer() != 0) {
         var3.addFlags(65536);
         var1.putInt(this.getExplosionTimer());
      }

      if (this.maxAngle != 1.0F) {
         var3.addFlags(131072);
         var1.putFloat(this.maxAngle);
      }

      if (this.bloodLevel != 0.0F) {
         var3.addFlags(262144);
         var1.putFloat(this.bloodLevel);
      }

      if (this.containsClip) {
         var3.addFlags(524288);
      }

      if (this.roundChambered) {
         var3.addFlags(1048576);
      }

      if (this.isJammed) {
         var3.addFlags(2097152);
      }

      var3.write();
      var3.release();
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      super.load(var1, var2);
      BitHeaderRead var3 = BitHeader.allocRead(BitHeader.HeaderSize.Integer, var1);
      if (!var3.equals(0)) {
         if (var3.hasFlags(1)) {
            this.setMaxRange(var1.getFloat());
         }

         if (var3.hasFlags(2)) {
            this.setMinRangeRanged(var1.getFloat());
         }

         if (var3.hasFlags(4)) {
            this.setClipSize(var1.getInt());
         }

         if (var3.hasFlags(8)) {
            this.setMinDamage(var1.getFloat());
         }

         if (var3.hasFlags(16)) {
            this.setMaxDamage(var1.getFloat());
         }

         if (var3.hasFlags(32)) {
            this.setRecoilDelay(var1.getInt());
         }

         if (var3.hasFlags(64)) {
            this.setAimingTime(var1.getInt());
         }

         if (var3.hasFlags(128)) {
            this.setReloadTime(var1.getInt());
         }

         if (var3.hasFlags(256)) {
            this.setHitChance(var1.getInt());
         }

         if (var3.hasFlags(512)) {
            this.setMinAngle(var1.getFloat());
         }

         InventoryItem var4;
         if (var3.hasFlags(1024)) {
            var4 = InventoryItemFactory.CreateItem(var1.getShort());
            if (var4 != null && var4 instanceof WeaponPart) {
               this.attachWeaponPart((WeaponPart)var4, false);
            }
         }

         if (var3.hasFlags(2048)) {
            var4 = InventoryItemFactory.CreateItem(var1.getShort());
            if (var4 != null && var4 instanceof WeaponPart) {
               this.attachWeaponPart((WeaponPart)var4, false);
            }
         }

         if (var3.hasFlags(4096)) {
            var4 = InventoryItemFactory.CreateItem(var1.getShort());
            if (var4 != null && var4 instanceof WeaponPart) {
               this.attachWeaponPart((WeaponPart)var4, false);
            }
         }

         if (var3.hasFlags(8192)) {
            var4 = InventoryItemFactory.CreateItem(var1.getShort());
            if (var4 != null && var4 instanceof WeaponPart) {
               this.attachWeaponPart((WeaponPart)var4, false);
            }
         }

         if (var3.hasFlags(16384)) {
            var4 = InventoryItemFactory.CreateItem(var1.getShort());
            if (var4 != null && var4 instanceof WeaponPart) {
               this.attachWeaponPart((WeaponPart)var4, false);
            }
         }

         if (var3.hasFlags(32768)) {
            var4 = InventoryItemFactory.CreateItem(var1.getShort());
            if (var4 != null && var4 instanceof WeaponPart) {
               this.attachWeaponPart((WeaponPart)var4, false);
            }
         }

         if (var3.hasFlags(65536)) {
            this.setExplosionTimer(var1.getInt());
         }

         if (var3.hasFlags(131072)) {
            this.setMaxAngle(var1.getFloat());
         }

         if (var3.hasFlags(262144)) {
            this.setBloodLevel(var1.getFloat());
         }

         this.setContainsClip(var3.hasFlags(524288));
         if (StringUtils.isNullOrWhitespace(this.magazineType)) {
            this.setContainsClip(false);
         }

         this.setRoundChambered(var3.hasFlags(1048576));
         this.setJammed(var3.hasFlags(2097152));
      }

      var3.release();
   }

   public float getMinRangeRanged() {
      return this.minRangeRanged;
   }

   public void setMinRangeRanged(float var1) {
      this.minRangeRanged = var1;
   }

   public int getReloadTime() {
      return this.reloadTime;
   }

   public void setReloadTime(int var1) {
      this.reloadTime = var1;
   }

   public WeaponPart getSling() {
      return this.sling;
   }

   public void setSling(WeaponPart var1) {
      this.sling = var1;
   }

   public int getAimingTime() {
      return this.aimingTime;
   }

   public void setAimingTime(int var1) {
      this.aimingTime = var1;
   }

   public WeaponPart getStock() {
      return this.stock;
   }

   public void setStock(WeaponPart var1) {
      this.stock = var1;
   }

   public int getTreeDamage() {
      return this.treeDamage;
   }

   public void setTreeDamage(int var1) {
      this.treeDamage = var1;
   }

   public String getBulletOutSound() {
      return this.bulletOutSound;
   }

   public void setBulletOutSound(String var1) {
      this.bulletOutSound = var1;
   }

   public String getShellFallSound() {
      return this.shellFallSound;
   }

   public void setShellFallSound(String var1) {
      this.shellFallSound = var1;
   }

   private void addPartToList(String var1, ArrayList var2) {
      WeaponPart var3 = this.getWeaponPart(var1);
      if (var3 != null) {
         var2.add(var3);
      }

   }

   public ArrayList getAllWeaponParts() {
      ArrayList var1 = new ArrayList();
      this.addPartToList("Scope", var1);
      this.addPartToList("Clip", var1);
      this.addPartToList("Sling", var1);
      this.addPartToList("Canon", var1);
      this.addPartToList("Stock", var1);
      this.addPartToList("RecoilPad", var1);
      return var1;
   }

   public void setWeaponPart(String var1, WeaponPart var2) {
      if (var2 == null || var1.equalsIgnoreCase(var2.getPartType())) {
         if ("Scope".equalsIgnoreCase(var1)) {
            this.scope = var2;
         } else if ("Clip".equalsIgnoreCase(var1)) {
            this.clip = var2;
         } else if ("Sling".equalsIgnoreCase(var1)) {
            this.sling = var2;
         } else if ("Canon".equalsIgnoreCase(var1)) {
            this.canon = var2;
         } else if ("Stock".equalsIgnoreCase(var1)) {
            this.stock = var2;
         } else if ("RecoilPad".equalsIgnoreCase(var1)) {
            this.recoilpad = var2;
         } else {
            DebugLog.log("ERROR: unknown WeaponPart type \"" + var1 + "\"");
         }

      }
   }

   public WeaponPart getWeaponPart(String var1) {
      if ("Scope".equalsIgnoreCase(var1)) {
         return this.scope;
      } else if ("Clip".equalsIgnoreCase(var1)) {
         return this.clip;
      } else if ("Sling".equalsIgnoreCase(var1)) {
         return this.sling;
      } else if ("Canon".equalsIgnoreCase(var1)) {
         return this.canon;
      } else if ("Stock".equalsIgnoreCase(var1)) {
         return this.stock;
      } else if ("RecoilPad".equalsIgnoreCase(var1)) {
         return this.recoilpad;
      } else {
         DebugLog.log("ERROR: unknown WeaponPart type \"" + var1 + "\"");
         return null;
      }
   }

   public void attachWeaponPart(WeaponPart var1) {
      this.attachWeaponPart(var1, true);
   }

   public void attachWeaponPart(WeaponPart var1, boolean var2) {
      if (var1 != null) {
         WeaponPart var3 = this.getWeaponPart(var1.getPartType());
         if (var3 != null) {
            this.detachWeaponPart(var3);
         }

         this.setWeaponPart(var1.getPartType(), var1);
         if (var2) {
            this.setMaxRange(this.getMaxRange() + var1.getMaxRange());
            this.setMinRangeRanged(this.getMinRangeRanged() + var1.getMinRangeRanged());
            this.setClipSize(this.getClipSize() + var1.getClipSize());
            this.setReloadTime(this.getReloadTime() + var1.getReloadTime());
            this.setRecoilDelay((int)((float)this.getRecoilDelay() + var1.getRecoilDelay()));
            this.setAimingTime(this.getAimingTime() + var1.getAimingTime());
            this.setHitChance(this.getHitChance() + var1.getHitChance());
            this.setMinAngle(this.getMinAngle() + var1.getAngle());
            this.setActualWeight(this.getActualWeight() + var1.getWeightModifier());
            this.setWeight(this.getWeight() + var1.getWeightModifier());
            this.setMinDamage(this.getMinDamage() + var1.getDamage());
            this.setMaxDamage(this.getMaxDamage() + var1.getDamage());
         }

      }
   }

   public void detachWeaponPart(WeaponPart var1) {
      if (var1 != null) {
         WeaponPart var2 = this.getWeaponPart(var1.getPartType());
         if (var2 == var1) {
            this.setWeaponPart(var1.getPartType(), (WeaponPart)null);
            this.setMaxRange(this.getMaxRange() - var1.getMaxRange());
            this.setMinRangeRanged(this.getMinRangeRanged() - var1.getMinRangeRanged());
            this.setClipSize(this.getClipSize() - var1.getClipSize());
            this.setReloadTime(this.getReloadTime() - var1.getReloadTime());
            this.setRecoilDelay((int)((float)this.getRecoilDelay() - var1.getRecoilDelay()));
            this.setAimingTime(this.getAimingTime() - var1.getAimingTime());
            this.setHitChance(this.getHitChance() - var1.getHitChance());
            this.setMinAngle(this.getMinAngle() - var1.getAngle());
            this.setActualWeight(this.getActualWeight() - var1.getWeightModifier());
            this.setWeight(this.getWeight() - var1.getWeightModifier());
            this.setMinDamage(this.getMinDamage() - var1.getDamage());
            this.setMaxDamage(this.getMaxDamage() - var1.getDamage());
         }
      }
   }

   public int getTriggerExplosionTimer() {
      return this.triggerExplosionTimer;
   }

   public void setTriggerExplosionTimer(int var1) {
      this.triggerExplosionTimer = var1;
   }

   public boolean canBePlaced() {
      return this.canBePlaced;
   }

   public void setCanBePlaced(boolean var1) {
      this.canBePlaced = var1;
   }

   public int getExplosionRange() {
      return this.explosionRange;
   }

   public void setExplosionRange(int var1) {
      this.explosionRange = var1;
   }

   public int getExplosionPower() {
      return this.explosionPower;
   }

   public void setExplosionPower(int var1) {
      this.explosionPower = var1;
   }

   public int getFireRange() {
      return this.fireRange;
   }

   public void setFireRange(int var1) {
      this.fireRange = var1;
   }

   public int getSmokeRange() {
      return this.smokeRange;
   }

   public void setSmokeRange(int var1) {
      this.smokeRange = var1;
   }

   public int getFirePower() {
      return this.firePower;
   }

   public void setFirePower(int var1) {
      this.firePower = var1;
   }

   public int getNoiseRange() {
      return this.noiseRange;
   }

   public void setNoiseRange(int var1) {
      this.noiseRange = var1;
   }

   public int getNoiseDuration() {
      return this.getScriptItem().getNoiseDuration();
   }

   public float getExtraDamage() {
      return this.extraDamage;
   }

   public void setExtraDamage(float var1) {
      this.extraDamage = var1;
   }

   public int getExplosionTimer() {
      return this.explosionTimer;
   }

   public void setExplosionTimer(int var1) {
      this.explosionTimer = var1;
   }

   public String getPlacedSprite() {
      return this.placedSprite;
   }

   public void setPlacedSprite(String var1) {
      this.placedSprite = var1;
   }

   public boolean canBeReused() {
      return this.canBeReused;
   }

   public void setCanBeReused(boolean var1) {
      this.canBeReused = var1;
   }

   public int getSensorRange() {
      return this.sensorRange;
   }

   public void setSensorRange(int var1) {
      this.sensorRange = var1;
   }

   public String getRunAnim() {
      return this.RunAnim;
   }

   public float getCritDmgMultiplier() {
      return this.critDmgMultiplier;
   }

   public void setCritDmgMultiplier(float var1) {
      this.critDmgMultiplier = var1;
   }

   public String getStaticModel() {
      return this.staticModel != null ? this.staticModel : this.weaponSprite;
   }

   public float getBaseSpeed() {
      return this.baseSpeed;
   }

   public void setBaseSpeed(float var1) {
      this.baseSpeed = var1;
   }

   public float getBloodLevel() {
      return this.bloodLevel;
   }

   public void setBloodLevel(float var1) {
      this.bloodLevel = Math.max(0.0F, Math.min(1.0F, var1));
   }

   public void setWeaponLength(float var1) {
      this.WeaponLength = var1;
   }

   public String getAmmoBox() {
      return this.ammoBox;
   }

   public void setAmmoBox(String var1) {
      this.ammoBox = var1;
   }

   public String getMagazineType() {
      return this.magazineType;
   }

   public void setMagazineType(String var1) {
      this.magazineType = var1;
   }

   public String getEjectAmmoStartSound() {
      return this.getScriptItem().getEjectAmmoStartSound();
   }

   public String getEjectAmmoSound() {
      return this.getScriptItem().getEjectAmmoSound();
   }

   public String getEjectAmmoStopSound() {
      return this.getScriptItem().getEjectAmmoStopSound();
   }

   public String getInsertAmmoStartSound() {
      return this.getScriptItem().getInsertAmmoStartSound();
   }

   public String getInsertAmmoSound() {
      return this.getScriptItem().getInsertAmmoSound();
   }

   public String getInsertAmmoStopSound() {
      return this.getScriptItem().getInsertAmmoStopSound();
   }

   public String getRackSound() {
      return this.rackSound;
   }

   public void setRackSound(String var1) {
      this.rackSound = var1;
   }

   public boolean isReloadable(IsoGameCharacter var1) {
      return this.isRanged();
   }

   public boolean isContainsClip() {
      return this.containsClip;
   }

   public void setContainsClip(boolean var1) {
      this.containsClip = var1;
   }

   public InventoryItem getBestMagazine(IsoGameCharacter var1) {
      if (StringUtils.isNullOrEmpty(this.getMagazineType())) {
         return null;
      } else {
         InventoryItem var2 = var1.getInventory().getBestTypeRecurse(this.getMagazineType(), (var0, var1x) -> {
            return var0.getCurrentAmmoCount() - var1x.getCurrentAmmoCount();
         });
         return var2 != null && var2.getCurrentAmmoCount() != 0 ? var2 : null;
      }
   }

   public String getWeaponReloadType() {
      return this.weaponReloadType;
   }

   public void setWeaponReloadType(String var1) {
      this.weaponReloadType = var1;
   }

   public boolean isRackAfterShoot() {
      return this.rackAfterShoot;
   }

   public void setRackAfterShoot(boolean var1) {
      this.rackAfterShoot = var1;
   }

   public boolean isRoundChambered() {
      return this.roundChambered;
   }

   public void setRoundChambered(boolean var1) {
      this.roundChambered = var1;
   }

   public boolean isSpentRoundChambered() {
      return this.bSpentRoundChambered;
   }

   public void setSpentRoundChambered(boolean var1) {
      this.bSpentRoundChambered = var1;
   }

   public int getSpentRoundCount() {
      return this.spentRoundCount;
   }

   public void setSpentRoundCount(int var1) {
      this.spentRoundCount = PZMath.clamp(var1, 0, this.getMaxAmmo());
   }

   public boolean isManuallyRemoveSpentRounds() {
      return this.getScriptItem().isManuallyRemoveSpentRounds();
   }

   public int getAmmoPerShoot() {
      return this.ammoPerShoot;
   }

   public void setAmmoPerShoot(int var1) {
      this.ammoPerShoot = var1;
   }

   public float getJamGunChance() {
      return this.jamGunChance;
   }

   public void setJamGunChance(float var1) {
      this.jamGunChance = var1;
   }

   public boolean isJammed() {
      return this.isJammed;
   }

   public void setJammed(boolean var1) {
      this.isJammed = var1;
   }

   public String getClickSound() {
      return this.clickSound;
   }

   public void setClickSound(String var1) {
      this.clickSound = var1;
   }

   public ArrayList getModelWeaponPart() {
      return this.modelWeaponPart;
   }

   public void setModelWeaponPart(ArrayList var1) {
      this.modelWeaponPart = var1;
   }

   public String getOriginalWeaponSprite() {
      return this.originalWeaponSprite;
   }

   public void setOriginalWeaponSprite(String var1) {
      this.originalWeaponSprite = var1;
   }

   public boolean haveChamber() {
      return this.haveChamber;
   }

   public void setHaveChamber(boolean var1) {
      this.haveChamber = var1;
   }

   public String getDamageCategory() {
      return this.damageCategory;
   }

   public void setDamageCategory(String var1) {
      this.damageCategory = var1;
   }

   public boolean isDamageMakeHole() {
      return this.damageMakeHole;
   }

   public void setDamageMakeHole(boolean var1) {
      this.damageMakeHole = var1;
   }

   public String getHitFloorSound() {
      return this.hitFloorSound;
   }

   public void setHitFloorSound(String var1) {
      this.hitFloorSound = var1;
   }

   public boolean isInsertAllBulletsReload() {
      return this.insertAllBulletsReload;
   }

   public void setInsertAllBulletsReload(boolean var1) {
      this.insertAllBulletsReload = var1;
   }

   public String getFireMode() {
      return this.fireMode;
   }

   public void setFireMode(String var1) {
      this.fireMode = var1;
   }

   public ArrayList getFireModePossibilities() {
      return this.fireModePossibilities;
   }

   public void setFireModePossibilities(ArrayList var1) {
      this.fireModePossibilities = var1;
   }

   public void randomizeBullets() {
      if (this.isRanged() && !Rand.NextBool(4)) {
         this.setCurrentAmmoCount(Rand.Next(this.getMaxAmmo() - 2, this.getMaxAmmo()));
         if (!StringUtils.isNullOrEmpty(this.getMagazineType())) {
            this.setContainsClip(true);
         }

         if (this.haveChamber()) {
            this.setRoundChambered(true);
         }

      }
   }

   public float getStopPower() {
      return this.getScriptItem().stopPower;
   }

   public boolean isInstantExplosion() {
      return this.explosionTimer <= 0 && this.sensorRange <= 0 && this.getRemoteControlID() == -1;
   }
}
