package zombie.ai.states;

import fmod.fmod.FMODManager;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjglx.input.Keyboard;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.Lua.LuaEventManager;
import zombie.Lua.LuaHookManager;
import zombie.ai.State;
import zombie.audio.parameters.ParameterMeleeHitSurface;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.Faction;
import zombie.characters.HitReactionNetworkAI;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoLivingCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.Stats;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.skills.PerkFactory;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.model.Model;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.Clothing;
import zombie.inventory.types.HandWeapon;
import zombie.inventory.types.WeaponType;
import zombie.iso.IsoCell;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.LosUtil;
import zombie.iso.Vector2;
import zombie.iso.Vector3;
import zombie.iso.areas.NonPvpZone;
import zombie.iso.objects.IsoDoor;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoTree;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoZombieGiblets;
import zombie.iso.objects.interfaces.Thumpable;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerOptions;
import zombie.network.packets.hit.AttackVars;
import zombie.network.packets.hit.HitInfo;
import zombie.popman.ObjectPool;
import zombie.scripting.objects.VehicleScript;
import zombie.ui.MoodlesUI;
import zombie.ui.UIManager;
import zombie.util.StringUtils;
import zombie.util.Type;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PolygonalMap2;
import zombie.vehicles.VehiclePart;
import zombie.vehicles.VehicleWindow;

public final class SwipeStatePlayer extends State {
   private static final SwipeStatePlayer _instance = new SwipeStatePlayer();
   static final Integer PARAM_LOWER_CONDITION = 0;
   static final Integer PARAM_ATTACKED = 1;
   private static final ArrayList HitList2 = new ArrayList();
   private static final Vector2 tempVector2_1 = new Vector2();
   private static final Vector2 tempVector2_2 = new Vector2();
   private final ArrayList dotList = new ArrayList();
   private boolean bHitOnlyTree;
   public final ObjectPool hitInfoPool = new ObjectPool(HitInfo::new);
   private static final SwipeStatePlayer.CustomComparator Comparator = new SwipeStatePlayer.CustomComparator();
   static final Vector3 tempVector3_1 = new Vector3();
   static final Vector3 tempVector3_2 = new Vector3();
   static final Vector3 tempVectorBonePos = new Vector3();
   static final ArrayList movingStatic = new ArrayList();
   private final Vector4f tempVector4f = new Vector4f();
   private final SwipeStatePlayer.WindowVisitor windowVisitor = new SwipeStatePlayer.WindowVisitor();

   public static SwipeStatePlayer instance() {
      return _instance;
   }

   public static void WeaponLowerCondition(HandWeapon var0, IsoGameCharacter var1) {
      if (var0.getUses() > 1) {
         var0.Use();
         InventoryItem var2 = InventoryItemFactory.CreateItem(var0.getFullType());
         var2.setCondition(var0.getCondition() - 1);
         var0.getContainer().AddItem(var2);
         var1.setPrimaryHandItem(var2);
      } else {
         var0.setCondition(var0.getCondition() - 1);
      }

   }

   private static HandWeapon GetWeapon(IsoGameCharacter var0) {
      HandWeapon var1 = var0.getUseHandWeapon();
      if (((IsoLivingCharacter)var0).bDoShove || var0.isForceShove()) {
         var1 = ((IsoLivingCharacter)var0).bareHands;
      }

      return var1;
   }

   private void doAttack(IsoPlayer var1, float var2, boolean var3, String var4, AttackVars var5) {
      var1.setForceShove(var3);
      var1.setClickSound(var4);
      if (var3) {
         var2 *= 2.0F;
      }

      if (var2 > 90.0F) {
         var2 = 90.0F;
      }

      var2 /= 25.0F;
      var1.useChargeDelta = var2;
      Object var6 = var1.getPrimaryHandItem();
      if (var6 == null || !(var6 instanceof HandWeapon) || var3 || var5.bDoShove) {
         var6 = var1.bareHands;
      }

      if (var6 instanceof HandWeapon) {
         var1.setUseHandWeapon((HandWeapon)var6);
         if (var1.PlayerIndex == 0 && var1.JoypadBind == -1 && UIManager.getPicked() != null && (!GameClient.bClient || var1.isLocalPlayer())) {
            if (UIManager.getPicked().tile instanceof IsoMovingObject) {
               var1.setAttackTargetSquare(((IsoMovingObject)UIManager.getPicked().tile).getCurrentSquare());
            } else {
               var1.setAttackTargetSquare(UIManager.getPicked().square);
            }
         }

         var1.setRecoilDelay((float)var5.recoilDelay);
         if (var3) {
            var1.setRecoilDelay(10.0F);
         }
      }

   }

   public void enter(IsoGameCharacter var1) {
      if ("HitReaction".equals(var1.getHitReaction())) {
         var1.clearVariable("HitReaction");
      }

      UIManager.speedControls.SetCurrentGameSpeed(1);
      HashMap var2 = var1.getStateMachineParams(this);
      var2.put(PARAM_LOWER_CONDITION, Boolean.FALSE);
      var2.put(PARAM_ATTACKED, Boolean.FALSE);
      if (!(var1 instanceof IsoPlayer) || !((IsoPlayer)var1).bRemote) {
         var1.updateRecoilVar();
      }

      if ("Auto".equals(var1.getVariableString("FireMode"))) {
         var1.setVariable("autoShootSpeed", 4.0F * GameTime.getAnimSpeedFix());
         var1.setVariable("autoShootVarY", 0.0F);
         if (System.currentTimeMillis() - var1.lastAutomaticShoot < 600L) {
            ++var1.shootInARow;
            float var3 = Math.max(0.0F, 1.0F - (float)var1.shootInARow / 20.0F);
            var1.setVariable("autoShootVarX", var3);
            var1.setVariable("autoShootSpeed", (4.0F - (float)var1.shootInARow / 10.0F) * GameTime.getAnimSpeedFix());
         } else {
            var1.setVariable("autoShootVarX", 1.0F);
            var1.shootInARow = 0;
         }

         var1.lastAutomaticShoot = System.currentTimeMillis();
      }

      IsoPlayer var6 = (IsoPlayer)var1;
      var1.setVariable("ShotDone", false);
      var1.setVariable("ShoveAnim", false);
      this.CalcAttackVars((IsoLivingCharacter)var1, var6.attackVars);
      this.doAttack(var6, 2.0F, var1.isForceShove(), var1.getClickSound(), var6.attackVars);
      HandWeapon var4 = var1.getUseHandWeapon();
      if (!GameClient.bClient || var6.isLocalPlayer()) {
         var1.setVariable("AimFloorAnim", var6.attackVars.bAimAtFloor);
      }

      LuaEventManager.triggerEvent("OnWeaponSwing", var1, var4);
      if (LuaHookManager.TriggerHook("WeaponSwing", var1, var4)) {
         var1.getStateMachine().revertToPreviousState(this);
      }

      var1.StopAllActionQueue();
      if (((IsoPlayer)var1).isLocalPlayer()) {
         IsoWorld.instance.CurrentCell.setDrag((KahluaTable)null, ((IsoPlayer)var1).PlayerIndex);
      }

      var4 = var6.attackVars.getWeapon(var6);
      var6.setAimAtFloor(var6.attackVars.bAimAtFloor);
      boolean var5 = var6.bDoShove;
      var6.setDoShove(var6.attackVars.bDoShove);
      var6.useChargeDelta = var6.attackVars.useChargeDelta;
      var6.targetOnGround = (IsoGameCharacter)var6.attackVars.targetOnGround.getMovingObject();
      if (!var6.bDoShove && !var5 && var6.getClickSound() == null && var4.getPhysicsObject() == null && !var4.isRanged()) {
      }

      if (GameClient.bClient && var1 == IsoPlayer.getInstance()) {
         GameClient.instance.sendPlayer((IsoPlayer)var1);
      }

      if (!var6.bDoShove && !var5 && !var4.isRanged() && var6.isLocalPlayer()) {
         var1.playSound(var4.getSwingSound());
      } else if ((var6.bDoShove || var5) && var6.isLocalPlayer()) {
         if (var6.targetOnGround != null) {
            var1.playSound("AttackStomp");
         } else {
            var1.playSound("AttackShove");
         }
      }

   }

   public void execute(IsoGameCharacter var1) {
      var1.StopAllActionQueue();
   }

   private int DoSwingCollisionBoneCheck(IsoGameCharacter var1, HandWeapon var2, IsoGameCharacter var3, int var4, float var5) {
      movingStatic.clear();
      float var8 = var2.WeaponLength;
      var8 += 0.5F;
      if (var1.isAimAtFloor() && ((IsoLivingCharacter)var1).bDoShove) {
         var8 = 0.3F;
      }

      Model.BoneToWorldCoords(var3, var4, tempVectorBonePos);

      for(int var9 = 1; var9 <= 10; ++var9) {
         float var10 = (float)var9 / 10.0F;
         tempVector3_1.x = var1.x;
         tempVector3_1.y = var1.y;
         tempVector3_1.z = var1.z;
         Vector3 var10000 = tempVector3_1;
         var10000.x += var1.getForwardDirection().x * var8 * var10;
         var10000 = tempVector3_1;
         var10000.y += var1.getForwardDirection().y * var8 * var10;
         tempVector3_1.x = tempVectorBonePos.x - tempVector3_1.x;
         tempVector3_1.y = tempVectorBonePos.y - tempVector3_1.y;
         tempVector3_1.z = 0.0F;
         boolean var11 = tempVector3_1.getLength() < var5;
         if (var11) {
            return var4;
         }
      }

      return -1;
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      if (var2.m_EventName.equalsIgnoreCase("ActiveAnimFinishing") || var2.m_EventName.equalsIgnoreCase("NonLoopedAnimFadeOut")) {
         boolean var4 = var3.get(PARAM_LOWER_CONDITION) == Boolean.TRUE;
         if (var4 && !var1.isRangedWeaponEmpty()) {
            var3.put(PARAM_LOWER_CONDITION, Boolean.FALSE);
            HandWeapon var5 = GetWeapon(var1);
            int var6 = var5.getConditionLowerChance();
            if (var1 instanceof IsoPlayer && "charge".equals(((IsoPlayer)var1).getAttackType())) {
               var6 = (int)((double)var6 / 1.5D);
            }

            if (Rand.Next(var6 + var1.getMaintenanceMod() * 2) == 0) {
               WeaponLowerCondition(var5, var1);
            } else if (Rand.NextBool(2) && !var5.isRanged() && !var5.getName().contains("Bare Hands")) {
               if (var5.isTwoHandWeapon() && (var1.getPrimaryHandItem() != var5 || var1.getSecondaryHandItem() != var5) && Rand.NextBool(3)) {
                  return;
               }

               var1.getXp().AddXP(PerkFactory.Perks.Maintenance, 1.0F);
            }
         }
      }

      if (var2.m_EventName.equalsIgnoreCase("AttackAnim")) {
         var1.setAttackAnim(Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("BlockTurn")) {
         var1.setIgnoreMovement(Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("ShoveAnim")) {
         var1.setVariable("ShoveAnim", Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("StompAnim")) {
         var1.setVariable("StompAnim", Boolean.parseBoolean(var2.m_ParameterValue));
      }

      HandWeapon var7 = GetWeapon(var1);
      if (var2.m_EventName.equalsIgnoreCase("AttackCollisionCheck") && var3.get(PARAM_ATTACKED) == Boolean.FALSE && var1 instanceof IsoPlayer && ((IsoPlayer)var1).isLocalPlayer()) {
         this.ConnectSwing(var1, var7);
      }

      if (var2.m_EventName.equalsIgnoreCase("BlockMovement") && SandboxOptions.instance.AttackBlockMovements.getValue()) {
         var1.setVariable("SlowingMovement", Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("WeaponEmptyCheck") && var1.getClickSound() != null) {
         if (var1 instanceof IsoPlayer && !((IsoPlayer)var1).isLocalPlayer()) {
            return;
         }

         var1.playSound(var1.getClickSound());
         var1.setRecoilDelay(10.0F);
      }

      if (var2.m_EventName.equalsIgnoreCase("ShotDone") && var7 != null && var7.isRackAfterShoot()) {
         var1.setVariable("ShotDone", true);
      }

      if (var2.m_EventName.equalsIgnoreCase("SetVariable") && var2.m_ParameterValue.startsWith("ShotDone=")) {
         var1.setVariable("ShotDone", var1.getVariableBoolean("ShotDone") && var7 != null && var7.isRackAfterShoot());
      }

      if (var2.m_EventName.equalsIgnoreCase("playRackSound")) {
         if (var1 instanceof IsoPlayer && !((IsoPlayer)var1).isLocalPlayer()) {
            return;
         }

         var1.playSound(var7.getRackSound());
      }

      if (var2.m_EventName.equalsIgnoreCase("playClickSound")) {
         if (var1 instanceof IsoPlayer && !((IsoPlayer)var1).isLocalPlayer()) {
            return;
         }

         var1.playSound(var7.getClickSound());
      }

      if (var2.m_EventName.equalsIgnoreCase("SetMeleeDelay")) {
         var1.setMeleeDelay(PZMath.tryParseFloat(var2.m_ParameterValue, 0.0F));
      }

      if (var2.m_EventName.equalsIgnoreCase("SitGroundStarted")) {
         var1.setVariable("SitGroundAnim", "Idle");
      }

   }

   public void exit(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      var1.setSprinting(false);
      ((IsoPlayer)var1).setForceSprint(false);
      var1.setIgnoreMovement(false);
      var1.setVariable("ShoveAnim", false);
      var1.setVariable("StompAnim", false);
      var1.setAttackAnim(false);
      var1.setVariable("AimFloorAnim", false);
      ((IsoPlayer)var1).setBlockMovement(false);
      if (var1.isAimAtFloor() && ((IsoLivingCharacter)var1).bDoShove) {
         Clothing var3 = (Clothing)var1.getWornItem("Shoes");
         byte var4 = 10;
         int var6;
         if (var3 == null) {
            var6 = 3;
         } else {
            var6 = var4 + var3.getConditionLowerChance() / 2;
            if (Rand.Next(var3.getConditionLowerChance()) == 0) {
               var3.setCondition(var3.getCondition() - 1);
            }
         }

         if (Rand.Next(var6) == 0) {
            if (var3 == null) {
               var1.getBodyDamage().getBodyPart(BodyPartType.Foot_R).AddDamage((float)Rand.Next(5, 10));
               var1.getBodyDamage().getBodyPart(BodyPartType.Foot_R).setAdditionalPain(var1.getBodyDamage().getBodyPart(BodyPartType.Foot_R).getAdditionalPain() + (float)Rand.Next(5, 10));
            } else {
               var1.getBodyDamage().getBodyPart(BodyPartType.Foot_R).AddDamage((float)Rand.Next(1, 5));
               var1.getBodyDamage().getBodyPart(BodyPartType.Foot_R).setAdditionalPain(var1.getBodyDamage().getBodyPart(BodyPartType.Foot_R).getAdditionalPain() + (float)Rand.Next(1, 5));
            }
         }
      }

      HandWeapon var5 = GetWeapon(var1);
      var1.clearVariable("ZombieHitReaction");
      ((IsoPlayer)var1).attackStarted = false;
      ((IsoPlayer)var1).setAttackType((String)null);
      ((IsoLivingCharacter)var1).setDoShove(false);
      var1.clearVariable("RackWeapon");
      var1.clearVariable("bShoveAiming");
      boolean var7 = var2.get(PARAM_ATTACKED) == Boolean.TRUE;
      if (var5 != null && (var5.getCondition() <= 0 || var7 && var5.isUseSelf())) {
         var1.removeFromHands(var5);
         if (DebugOptions.instance.MultiplayerAutoEquip.getValue() && var5.getPhysicsObject() != null) {
            var1.setPrimaryHandItem(var1.getInventory().getItemFromType(var5.getType()));
         }

         var1.getInventory().setDrawDirty(true);
      }

      if (var1.isRangedWeaponEmpty()) {
         var1.setRecoilDelay(10.0F);
      }

      var1.setRangedWeaponEmpty(false);
      var1.setForceShove(false);
      var1.setClickSound((String)null);
      if (var7) {
         LuaEventManager.triggerEvent("OnPlayerAttackFinished", var1, var5);
      }

      var1.hitList.clear();
      var1.attackVars.clear();
   }

   public void CalcAttackVars(IsoLivingCharacter var1, AttackVars var2) {
      HandWeapon var3 = (HandWeapon)Type.tryCastTo(var1.getPrimaryHandItem(), HandWeapon.class);
      if (var3 != null && var3.getOtherHandRequire() != null) {
         InventoryItem var4 = var1.getSecondaryHandItem();
         if (var4 == null || !var4.getType().equals(var3.getOtherHandRequire())) {
            var3 = null;
         }
      }

      if (!GameClient.bClient || var1.isLocal()) {
         boolean var13 = var1.isAttackAnim() || var1.getVariableBoolean("ShoveAnim") || var1.getVariableBoolean("StompAnim");
         var2.setWeapon(var3 == null ? var1.bareHands : var3);
         var2.targetOnGround.setMovingObject((IsoMovingObject)null);
         var2.bAimAtFloor = false;
         var2.bCloseKill = false;
         var2.bDoShove = var1.bDoShove;
         if (!var13) {
            var1.setVariable("ShoveAimX", 0.5F);
            var1.setVariable("ShoveAimY", 1.0F);
            if (var2.bDoShove && var1.getVariableBoolean("isMoving")) {
               var1.setVariable("ShoveAim", true);
            } else {
               var1.setVariable("ShoveAim", false);
            }
         }

         var2.useChargeDelta = var1.useChargeDelta;
         var2.recoilDelay = 0;
         if (var2.getWeapon(var1) == var1.bareHands || var2.bDoShove || var1.isForceShove()) {
            var2.bDoShove = true;
            var2.bAimAtFloor = false;
            var2.setWeapon(var1.bareHands);
         }

         this.calcValidTargets(var1, var2.getWeapon(var1), true, var2.targetsProne, var2.targetsStanding);
         HitInfo var5 = var2.targetsStanding.isEmpty() ? null : (HitInfo)var2.targetsStanding.get(0);
         HitInfo var6 = var2.targetsProne.isEmpty() ? null : (HitInfo)var2.targetsProne.get(0);
         if (this.isProneTargetBetter(var1, var5, var6)) {
            var5 = null;
         }

         if (!var13) {
            var1.setAimAtFloor(false);
         }

         float var7 = Float.MAX_VALUE;
         if (var5 != null) {
            if (!var13) {
               var1.setAimAtFloor(false);
            }

            var2.bAimAtFloor = false;
            var2.targetOnGround.setMovingObject((IsoMovingObject)null);
            var7 = var5.distSq;
         } else if (var6 != null && (Core.OptionAutoProneAtk || var1.bDoShove)) {
            if (!var13) {
               var1.setAimAtFloor(true);
            }

            var2.bAimAtFloor = true;
            var2.targetOnGround.setMovingObject(var6.getObject());
         }

         if (!(var7 >= var2.getWeapon(var1).getMinRange() * var2.getWeapon(var1).getMinRange()) && (var5 == null || !this.isWindowBetween(var1, var5.getObject()))) {
            if (var1.getStats().NumChasingZombies <= 1 && WeaponType.getWeaponType((IsoGameCharacter)var1) == WeaponType.knife) {
               var2.bCloseKill = true;
               return;
            }

            var2.bDoShove = true;
            IsoPlayer var8 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
            if (var8 != null && !var8.isAuthorizeShoveStomp()) {
               var2.bDoShove = false;
            }

            var2.bAimAtFloor = false;
            if (var1.bareHands.getSwingAnim() != null) {
               var2.useChargeDelta = 3.0F;
            }
         }

         int var14 = Core.getInstance().getKey("ManualFloorAtk");
         int var9 = Core.getInstance().getKey("Sprint");
         boolean var10 = var1.getVariableBoolean("StartedAttackWhileSprinting");
         if (Keyboard.isKeyDown(var14) && (var14 != var9 || !var10)) {
            var2.bAimAtFloor = true;
            var2.bDoShove = false;
            var1.setDoShove(false);
         }

         if (var2.getWeapon(var1).isRanged()) {
            int var11 = var2.getWeapon(var1).getRecoilDelay();
            Float var12 = (float)var11 * (1.0F - (float)var1.getPerkLevel(PerkFactory.Perks.Aiming) / 30.0F);
            var2.recoilDelay = var12.intValue();
            var1.setVariable("singleShootSpeed", (0.8F + (float)var1.getPerkLevel(PerkFactory.Perks.Aiming) / 10.0F) * GameTime.getAnimSpeedFix());
         }

      }
   }

   public void calcValidTargets(IsoLivingCharacter var1, HandWeapon var2, boolean var3, ArrayList var4, ArrayList var5) {
      this.hitInfoPool.release((List)var4);
      this.hitInfoPool.release((List)var5);
      var4.clear();
      var5.clear();
      float var6 = Core.getInstance().getIgnoreProneZombieRange();
      float var7 = var2.getMaxRange() * var2.getRangeMod(var1);
      float var8 = Math.max(var6, var7 + (var3 ? 1.0F : 0.0F));
      ArrayList var9 = IsoWorld.instance.CurrentCell.getObjectList();

      for(int var10 = 0; var10 < var9.size(); ++var10) {
         IsoMovingObject var11 = (IsoMovingObject)var9.get(var10);
         HitInfo var12 = this.calcValidTarget(var1, var2, var11, var8);
         if (var12 != null) {
            if (isStanding(var11)) {
               var5.add(var12);
            } else {
               var4.add(var12);
            }
         }
      }

      if (!var4.isEmpty() && this.shouldIgnoreProneZombies(var1, var5, var6)) {
         this.hitInfoPool.release((List)var4);
         var4.clear();
      }

      float var13 = var2.getMinAngle();
      float var14 = var2.getMaxAngle();
      if (var2.isRanged()) {
         var13 -= var2.getAimingPerkMinAngleModifier() * ((float)var1.getPerkLevel(PerkFactory.Perks.Aiming) / 2.0F);
      }

      this.removeUnhittableTargets(var1, var2, var13, var14, var3, var5);
      var13 = var2.getMinAngle();
      var13 = (float)((double)var13 / 1.5D);
      this.removeUnhittableTargets(var1, var2, var13, var14, var3, var4);
      var5.sort(Comparator);
      var4.sort(Comparator);
   }

   private boolean shouldIgnoreProneZombies(IsoGameCharacter var1, ArrayList var2, float var3) {
      if (var3 <= 0.0F) {
         return false;
      } else {
         boolean var4 = var1.isInvisible() || var1 instanceof IsoPlayer && ((IsoPlayer)var1).isGhostMode();

         for(int var5 = 0; var5 < var2.size(); ++var5) {
            HitInfo var6 = (HitInfo)var2.get(var5);
            IsoZombie var7 = (IsoZombie)Type.tryCastTo(var6.getObject(), IsoZombie.class);
            if ((var7 == null || var7.target != null || var4) && !(var6.distSq > var3 * var3)) {
               int var10005 = (int)var1.z;
               boolean var8 = PolygonalMap2.instance.lineClearCollide(var1.x, var1.y, var6.getObject().x, var6.getObject().y, var10005, var1, false, true);
               if (!var8) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean isUnhittableTarget(IsoGameCharacter var1, HandWeapon var2, float var3, float var4, HitInfo var5, boolean var6) {
      if (!(var5.dot < var3) && !(var5.dot > var4)) {
         Vector3 var7 = tempVectorBonePos.set(var5.x, var5.y, var5.z);
         return !var1.IsAttackRange(var2, var5.getObject(), var7, var6);
      } else {
         return true;
      }
   }

   private void removeUnhittableTargets(IsoGameCharacter var1, HandWeapon var2, float var3, float var4, boolean var5, ArrayList var6) {
      for(int var7 = var6.size() - 1; var7 >= 0; --var7) {
         HitInfo var8 = (HitInfo)var6.get(var7);
         if (this.isUnhittableTarget(var1, var2, var3, var4, var8, var5)) {
            this.hitInfoPool.release((Object)var8);
            var6.remove(var7);
         }
      }

   }

   private boolean getNearestTargetPosAndDot(IsoGameCharacter var1, HandWeapon var2, IsoMovingObject var3, boolean var4, Vector4f var5) {
      this.getNearestTargetPosAndDot(var1, var3, var5);
      float var6 = var5.w;
      float var7 = var2.getMinAngle();
      float var8 = var2.getMaxAngle();
      IsoGameCharacter var9 = (IsoGameCharacter)Type.tryCastTo(var3, IsoGameCharacter.class);
      if (var9 != null) {
         if (isStanding(var3)) {
            if (var2.isRanged()) {
               var7 -= var2.getAimingPerkMinAngleModifier() * ((float)var1.getPerkLevel(PerkFactory.Perks.Aiming) / 2.0F);
            }
         } else {
            var7 /= 1.5F;
         }
      }

      if (!(var6 < var7) && !(var6 > var8)) {
         Vector3 var10 = tempVectorBonePos.set(var5.x, var5.y, var5.z);
         return var1.IsAttackRange(var2, var3, var10, var4);
      } else {
         return false;
      }
   }

   private void getNearestTargetPosAndDot(IsoGameCharacter var1, Vector3 var2, Vector2 var3, Vector4f var4) {
      float var5 = var1.getDotWithForwardDirection(var2);
      var5 = PZMath.clamp(var5, -1.0F, 1.0F);
      var4.w = Math.max(var5, var4.w);
      float var6 = IsoUtils.DistanceToSquared(var1.x, var1.y, (float)((int)var1.z * 3), var2.x, var2.y, (float)((int)Math.max(var2.z, 0.0F) * 3));
      if (var6 < var3.x) {
         var3.x = var6;
         var4.set(var2.x, var2.y, var2.z, var4.w);
      }

   }

   private void getNearestTargetPosAndDot(IsoGameCharacter var1, IsoMovingObject var2, String var3, Vector2 var4, Vector4f var5) {
      Vector3 var6 = getBoneWorldPos(var2, var3, tempVectorBonePos);
      this.getNearestTargetPosAndDot(var1, var6, var4, var5);
   }

   private void getNearestTargetPosAndDot(IsoGameCharacter var1, IsoMovingObject var2, Vector4f var3) {
      Vector2 var4 = tempVector2_1.set(Float.MAX_VALUE, Float.NaN);
      var3.w = Float.NEGATIVE_INFINITY;
      IsoGameCharacter var5 = (IsoGameCharacter)Type.tryCastTo(var2, IsoGameCharacter.class);
      if (var5 == null) {
         this.getNearestTargetPosAndDot(var1, var2, (String)null, var4, var3);
      } else {
         getBoneWorldPos(var2, "Bip01_Head", tempVector3_1);
         getBoneWorldPos(var2, "Bip01_HeadNub", tempVector3_2);
         tempVector3_1.addToThis(tempVector3_2);
         tempVector3_1.div(2.0F);
         Vector3 var6 = tempVector3_1;
         if (isStanding(var2)) {
            this.getNearestTargetPosAndDot(var1, var6, var4, var3);
            this.getNearestTargetPosAndDot(var1, var2, "Bip01_Pelvis", var4, var3);
            Vector3 var7 = tempVectorBonePos.set(var2.getX(), var2.getY(), var2.getZ());
            this.getNearestTargetPosAndDot(var1, var7, var4, var3);
         } else {
            this.getNearestTargetPosAndDot(var1, var6, var4, var3);
            this.getNearestTargetPosAndDot(var1, var2, "Bip01_Pelvis", var4, var3);
            this.getNearestTargetPosAndDot(var1, var2, "Bip01_DressFrontNub", var4, var3);
         }

      }
   }

   private HitInfo calcValidTarget(IsoLivingCharacter var1, HandWeapon var2, IsoMovingObject var3, float var4) {
      if (var3 == var1) {
         return null;
      } else {
         IsoGameCharacter var5 = (IsoGameCharacter)Type.tryCastTo(var3, IsoGameCharacter.class);
         if (var5 == null) {
            return null;
         } else if (var5.isGodMod()) {
            return null;
         } else if (!checkPVP(var1, var3)) {
            return null;
         } else {
            float var6 = Math.abs(var5.getZ() - var1.getZ());
            if (!var2.isRanged() && var6 >= 0.5F) {
               return null;
            } else if (var6 > 3.3F) {
               return null;
            } else if (!var5.isShootable()) {
               return null;
            } else if (var5.isCurrentState(FakeDeadZombieState.instance())) {
               return null;
            } else if (var5.isDead()) {
               return null;
            } else if (var5.getHitReaction() != null && var5.getHitReaction().contains("Death")) {
               return null;
            } else {
               Vector4f var7 = this.tempVector4f;
               this.getNearestTargetPosAndDot(var1, var5, var7);
               float var8 = var7.w;
               float var9 = IsoUtils.DistanceToSquared(var1.x, var1.y, (float)((int)var1.z * 3), var7.x, var7.y, (float)((int)var7.z * 3));
               if (var8 < 0.0F) {
                  return null;
               } else if (var9 > var4 * var4) {
                  return null;
               } else {
                  LosUtil.TestResults var10 = LosUtil.lineClear(var1.getCell(), (int)var1.getX(), (int)var1.getY(), (int)var1.getZ(), (int)var5.getX(), (int)var5.getY(), (int)var5.getZ(), false);
                  return var10 != LosUtil.TestResults.Blocked && var10 != LosUtil.TestResults.ClearThroughClosedDoor ? ((HitInfo)this.hitInfoPool.alloc()).init(var5, var8, var9, var7.x, var7.y, var7.z) : null;
               }
            }
         }
      }
   }

   public static boolean isProne(IsoMovingObject var0) {
      IsoZombie var1 = (IsoZombie)Type.tryCastTo(var0, IsoZombie.class);
      if (var1 == null) {
         return var0.isOnFloor();
      } else if (var1.isOnFloor()) {
         return true;
      } else if (var1.isCurrentState(ZombieEatBodyState.instance())) {
         return true;
      } else if (var1.isDead()) {
         return true;
      } else if (var1.isSitAgainstWall()) {
         return true;
      } else {
         return var1.isCrawling();
      }
   }

   public static boolean isStanding(IsoMovingObject var0) {
      return !isProne(var0);
   }

   public boolean isProneTargetBetter(IsoGameCharacter var1, HitInfo var2, HitInfo var3) {
      if (var2 != null && var2.getObject() != null) {
         if (var3 != null && var3.getObject() != null) {
            if (var2.distSq <= var3.distSq) {
               return false;
            } else {
               int var10005 = (int)var1.z;
               boolean var4 = PolygonalMap2.instance.lineClearCollide(var1.x, var1.y, var2.getObject().x, var2.getObject().y, var10005, (IsoMovingObject)null, false, true);
               if (!var4) {
                  return false;
               } else {
                  var10005 = (int)var1.z;
                  boolean var5 = PolygonalMap2.instance.lineClearCollide(var1.x, var1.y, var3.getObject().x, var3.getObject().y, var10005, (IsoMovingObject)null, false, true);
                  return !var5;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean checkPVP(IsoGameCharacter var0, IsoMovingObject var1) {
      IsoPlayer var2 = (IsoPlayer)Type.tryCastTo(var0, IsoPlayer.class);
      IsoPlayer var3 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
      if (GameClient.bClient && var3 != null) {
         if (var3.isGodMod() || !ServerOptions.instance.PVP.getValue() || ServerOptions.instance.SafetySystem.getValue() && var0.isSafety() && ((IsoGameCharacter)var1).isSafety()) {
            return false;
         }

         if (NonPvpZone.getNonPvpZone((int)var1.getX(), (int)var1.getY()) != null) {
            return false;
         }

         if (var2 != null && NonPvpZone.getNonPvpZone((int)var0.getX(), (int)var0.getY()) != null) {
            return false;
         }

         if (var2 != null && !var2.factionPvp && !var3.factionPvp) {
            Faction var4 = Faction.getPlayerFaction(var2);
            Faction var5 = Faction.getPlayerFaction(var3);
            if (var5 != null && var4 == var5) {
               return false;
            }
         }
      }

      if (!GameClient.bClient && var3 != null && !IsoPlayer.getCoopPVP()) {
         return false;
      } else {
         return true;
      }
   }

   private void CalcHitListShove(IsoGameCharacter var1, boolean var2, AttackVars var3, ArrayList var4) {
      HandWeapon var5 = var3.getWeapon((IsoLivingCharacter)var1);
      ArrayList var6 = IsoWorld.instance.CurrentCell.getObjectList();

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         IsoMovingObject var8 = (IsoMovingObject)var6.get(var7);
         if (var8 != var1 && !(var8 instanceof BaseVehicle)) {
            IsoGameCharacter var9 = (IsoGameCharacter)Type.tryCastTo(var8, IsoGameCharacter.class);
            if (var9 != null && !var9.isGodMod() && !var9.isDead()) {
               IsoZombie var10 = (IsoZombie)Type.tryCastTo(var8, IsoZombie.class);
               if ((var10 == null || !var10.isCurrentState(FakeDeadZombieState.instance())) && checkPVP(var1, var8)) {
                  boolean var11 = var8 == var3.targetOnGround.getMovingObject() || var8.isShootable() && isStanding(var8) && !var3.bAimAtFloor || var8.isShootable() && isProne(var8) && var3.bAimAtFloor;
                  if (var11) {
                     Vector4f var12 = this.tempVector4f;
                     if (this.getNearestTargetPosAndDot(var1, var5, var8, var2, var12)) {
                        float var13 = var12.w;
                        float var14 = IsoUtils.DistanceToSquared(var1.x, var1.y, (float)((int)var1.z * 3), var12.x, var12.y, (float)((int)var12.z * 3));
                        LosUtil.TestResults var15 = LosUtil.lineClear(var1.getCell(), (int)var1.getX(), (int)var1.getY(), (int)var1.getZ(), (int)var8.getX(), (int)var8.getY(), (int)var8.getZ(), false);
                        if (var15 != LosUtil.TestResults.Blocked && var15 != LosUtil.TestResults.ClearThroughClosedDoor && (var8.getCurrentSquare() == null || var1.getCurrentSquare() == null || var8.getCurrentSquare() == var1.getCurrentSquare() || !var8.getCurrentSquare().isWindowBlockedTo(var1.getCurrentSquare())) && var8.getSquare().getTransparentWallTo(var1.getSquare()) == null) {
                           HitInfo var16 = ((HitInfo)this.hitInfoPool.alloc()).init(var8, var13, var14, var12.x, var12.y, var12.z);
                           if (var3.targetOnGround.getMovingObject() == var8) {
                              var4.clear();
                              var4.add(var16);
                              break;
                           }

                           var4.add(var16);
                        }
                     }
                  }
               }
            }
         }
      }

   }

   private void CalcHitListWeapon(IsoGameCharacter var1, boolean var2, AttackVars var3, ArrayList var4) {
      HandWeapon var5 = var3.getWeapon((IsoLivingCharacter)var1);
      ArrayList var6 = IsoWorld.instance.CurrentCell.getObjectList();

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         IsoMovingObject var8 = (IsoMovingObject)var6.get(var7);
         if (var8 != var1) {
            IsoGameCharacter var9 = (IsoGameCharacter)Type.tryCastTo(var8, IsoGameCharacter.class);
            if ((var9 == null || !var9.isGodMod()) && (var9 == null || !var9.isDead())) {
               IsoZombie var10 = (IsoZombie)Type.tryCastTo(var8, IsoZombie.class);
               if ((var10 == null || !var10.isCurrentState(FakeDeadZombieState.instance())) && checkPVP(var1, var8)) {
                  boolean var11 = var8 == var3.targetOnGround.getMovingObject() || var8.isShootable() && isStanding(var8) && !var3.bAimAtFloor || var8.isShootable() && isProne(var8) && var3.bAimAtFloor;
                  if (var11) {
                     Vector4f var12 = this.tempVector4f;
                     float var14;
                     if (var8 instanceof BaseVehicle) {
                        VehiclePart var13 = ((BaseVehicle)var8).getNearestBodyworkPart(var1);
                        if (var13 == null) {
                           continue;
                        }

                        var14 = var1.getDotWithForwardDirection(var8.x, var8.y);
                        if (var14 < 0.8F) {
                           continue;
                        }

                        var12.set(var8.x, var8.y, var8.z, var14);
                     } else if (var9 == null || !this.getNearestTargetPosAndDot(var1, var5, var8, var2, var12)) {
                        continue;
                     }

                     LosUtil.TestResults var18 = LosUtil.lineClear(var1.getCell(), (int)var1.getX(), (int)var1.getY(), (int)var1.getZ(), (int)var8.getX(), (int)var8.getY(), (int)var8.getZ(), false);
                     if (var18 != LosUtil.TestResults.Blocked && var18 != LosUtil.TestResults.ClearThroughClosedDoor) {
                        var14 = var12.w;
                        float var15 = IsoUtils.DistanceToSquared(var1.x, var1.y, (float)((int)var1.z * 3), var12.x, var12.y, (float)((int)var12.z * 3));
                        if (var8.getSquare().getTransparentWallTo(var1.getSquare()) != null && var1 instanceof IsoPlayer) {
                           if (WeaponType.getWeaponType(var1) == WeaponType.spear) {
                              ((IsoPlayer)var1).setAttackType("spearStab");
                           } else if (WeaponType.getWeaponType(var1) != WeaponType.knife) {
                              continue;
                           }
                        }

                        IsoWindow var16 = this.getWindowBetween(var1, var8);
                        if (var16 == null || !var16.isBarricaded()) {
                           HitInfo var17 = ((HitInfo)this.hitInfoPool.alloc()).init(var8, var14, var15, var12.x, var12.y, var12.z);
                           var17.window.setObject(var16);
                           var4.add(var17);
                        }
                     }
                  }
               }
            }
         }
      }

      if (var4.isEmpty()) {
         this.CalcHitListWindow(var1, var5, var4);
      }
   }

   private void CalcHitListWindow(IsoGameCharacter var1, HandWeapon var2, ArrayList var3) {
      Vector2 var4 = var1.getLookVector(tempVector2_1);
      var4.setLength(var2.getMaxRange() * var2.getRangeMod(var1));
      HitInfo var5 = null;
      ArrayList var6 = IsoWorld.instance.CurrentCell.getWindowList();

      for(int var7 = 0; var7 < var6.size(); ++var7) {
         IsoWindow var8 = (IsoWindow)var6.get(var7);
         if ((int)var8.getZ() == (int)var1.z && this.windowVisitor.isHittable(var8)) {
            float var9 = var8.getX();
            float var10 = var8.getY();
            float var11 = var9 + (var8.getNorth() ? 1.0F : 0.0F);
            float var12 = var10 + (var8.getNorth() ? 0.0F : 1.0F);
            if (Line2D.linesIntersect((double)var1.x, (double)var1.y, (double)(var1.x + var4.x), (double)(var1.y + var4.y), (double)var9, (double)var10, (double)var11, (double)var12)) {
               IsoGridSquare var13 = var8.getAddSheetSquare(var1);
               if (var13 != null && !LosUtil.lineClearCollide((int)var1.x, (int)var1.y, (int)var1.z, var13.x, var13.y, var13.z, false)) {
                  float var14 = IsoUtils.DistanceToSquared(var1.x, var1.y, var9 + (var11 - var9) / 2.0F, var10 + (var12 - var10) / 2.0F);
                  if (var5 == null || !(var5.distSq < var14)) {
                     float var15 = 1.0F;
                     if (var5 == null) {
                        var5 = (HitInfo)this.hitInfoPool.alloc();
                     }

                     var5.init(var8, var15, var14);
                  }
               }
            }
         }
      }

      if (var5 != null) {
         var3.add(var5);
      }

   }

   public void CalcHitList(IsoGameCharacter var1, boolean var2, AttackVars var3, ArrayList var4) {
      if (!GameClient.bClient || var1.isLocal()) {
         this.hitInfoPool.release((List)var4);
         var4.clear();
         HandWeapon var5 = var3.getWeapon((IsoLivingCharacter)var1);
         int var6 = var5.getMaxHitCount();
         if (var3.bDoShove) {
            var6 = WeaponType.getWeaponType(var1) != WeaponType.barehand ? 3 : 1;
         }

         if (!var5.isRanged() && !SandboxOptions.instance.MultiHitZombies.getValue()) {
            var6 = 1;
         }

         if (var5 == ((IsoPlayer)var1).bareHands && !(var1.getPrimaryHandItem() instanceof HandWeapon)) {
            var6 = 1;
         }

         if (var5 == ((IsoPlayer)var1).bareHands && var3.targetOnGround.getMovingObject() != null) {
            var6 = 1;
         }

         if (0 < var6) {
            if (var3.bDoShove) {
               this.CalcHitListShove(var1, var2, var3, var4);
            } else {
               this.CalcHitListWeapon(var1, var2, var3, var4);
            }

            if (var4.size() == 1 && ((HitInfo)var4.get(0)).getObject() == null) {
               return;
            }

            this.filterTargetsByZ(var1);
            Collections.sort(var4, Comparator);
            if (var5.isPiercingBullets()) {
               HitList2.clear();
               double var7 = 0.0D;

               for(int var9 = 0; var9 < var4.size(); ++var9) {
                  HitInfo var10 = (HitInfo)var4.get(var9);
                  IsoMovingObject var11 = var10.getObject();
                  if (var11 != null) {
                     double var12 = (double)(var1.getX() - var11.getX());
                     double var14 = (double)(-(var1.getY() - var11.getY()));
                     double var16 = Math.atan2(var14, var12);
                     if (var16 < 0.0D) {
                        var16 = Math.abs(var16);
                     } else {
                        var16 = 6.283185307179586D - var16;
                     }

                     if (var9 == 0) {
                        var7 = Math.toDegrees(var16);
                        HitList2.add(var10);
                     } else {
                        double var18 = Math.toDegrees(var16);
                        if (Math.abs(var7 - var18) < 1.0D) {
                           HitList2.add(var10);
                           break;
                        }
                     }
                  }
               }

               var4.removeAll(HitList2);
               this.hitInfoPool.release((List)var4);
               var4.clear();
               var4.addAll(HitList2);
            } else {
               while(var4.size() > var6) {
                  this.hitInfoPool.release((Object)((HitInfo)var4.remove(var4.size() - 1)));
               }
            }
         }

         for(int var20 = 0; var20 < var4.size(); ++var20) {
            HitInfo var8 = (HitInfo)var4.get(var20);
            var8.chance = this.CalcHitChance(var1, var5, var8);
         }

      }
   }

   private void filterTargetsByZ(IsoGameCharacter var1) {
      float var2 = Float.MAX_VALUE;
      HitInfo var3 = null;

      int var4;
      HitInfo var5;
      float var6;
      for(var4 = 0; var4 < var1.hitList.size(); ++var4) {
         var5 = (HitInfo)var1.hitList.get(var4);
         var6 = Math.abs(var5.z - var1.getZ());
         if (var6 < var2) {
            var2 = var6;
            var3 = var5;
         }
      }

      if (var3 != null) {
         for(var4 = var1.hitList.size() - 1; var4 >= 0; --var4) {
            var5 = (HitInfo)var1.hitList.get(var4);
            if (var5 != var3) {
               var6 = Math.abs(var5.z - var3.z);
               if (var6 > 0.5F) {
                  this.hitInfoPool.release((Object)var5);
                  var1.hitList.remove(var4);
               }
            }
         }

      }
   }

   public int CalcHitChance(IsoGameCharacter var1, HandWeapon var2, HitInfo var3) {
      IsoMovingObject var4 = var3.getObject();
      if (var4 == null) {
         return 0;
      } else {
         if (var1.getVehicle() != null) {
            BaseVehicle var5 = var1.getVehicle();
            Vector3f var6 = var5.getForwardVector((Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc());
            Vector2 var7 = (Vector2)((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).alloc();
            var7.x = var6.x;
            var7.y = var6.z;
            var7.normalize();
            int var8 = var5.getSeat(var1);
            VehicleScript.Area var9 = var5.getScript().getAreaById(var5.getPassengerArea(var8));
            byte var10 = -90;
            if (var9.x > 0.0F) {
               var10 = 90;
            }

            var7.rotate((float)Math.toRadians((double)var10));
            var7.normalize();
            Vector2 var11 = (Vector2)((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).alloc();
            var11.x = var4.x;
            var11.y = var4.y;
            var11.x -= var1.x;
            var11.y -= var1.y;
            var11.normalize();
            float var12 = var11.dot(var7);
            if ((double)var12 > -0.6D) {
               return 0;
            }

            ((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).release(var7);
            ((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).release(var11);
            ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var6);
         }

         if (System.currentTimeMillis() - var1.lastAutomaticShoot > 600L) {
            var1.shootInARow = 0;
         }

         int var13 = var2.getHitChance();
         var13 = (int)((float)var13 + var2.getAimingPerkHitChanceModifier() * (float)var1.getPerkLevel(PerkFactory.Perks.Aiming));
         if (var13 > 95) {
            var13 = 95;
         }

         var13 -= var1.shootInARow * 2;
         float var14 = PZMath.sqrt(var3.distSq);
         float var15 = 1.3F;
         if (var4 instanceof IsoPlayer) {
            var14 = (float)((double)var14 * 1.5D);
            var15 = 1.0F;
         }

         var13 = (int)((float)var13 + (var2.getMaxRange() * var2.getRangeMod(var1) - var14) * var15);
         if (var2.getMinRangeRanged() > 0.0F) {
            if (var14 < var2.getMinRangeRanged()) {
               var13 -= 50;
            }
         } else if ((double)var14 < 1.7D && var2.isRanged() && !(var4 instanceof IsoPlayer)) {
            var13 += 35;
         }

         if (var2.isRanged() && var1.getBeenMovingFor() > (float)(var2.getAimingTime() + var1.getPerkLevel(PerkFactory.Perks.Aiming))) {
            var13 = (int)((float)var13 - (var1.getBeenMovingFor() - (float)(var2.getAimingTime() + var1.getPerkLevel(PerkFactory.Perks.Aiming))));
         }

         if (var3.getObject() instanceof IsoPlayer) {
            IsoPlayer var16 = (IsoPlayer)var3.getObject();
            if (var16.isPlayerMoving()) {
               var13 -= 5;
            }

            if (var16.isRunning()) {
               var13 -= 10;
            }

            if (var16.isSprinting()) {
               var13 -= 15;
            }
         }

         if (var2.isRanged() && var1.getVehicle() != null) {
            var13 = (int)((float)var13 - Math.abs(var1.getVehicle().getCurrentSpeedKmHour()) * 2.0F);
         }

         if (var1.Traits.Marksman.isSet()) {
            var13 += 20;
         }

         float var17 = 0.0F;

         for(int var18 = BodyPartType.ToIndex(BodyPartType.Hand_L); var18 <= BodyPartType.ToIndex(BodyPartType.UpperArm_R); ++var18) {
            var17 += ((BodyPart)var1.getBodyDamage().getBodyParts().get(var18)).getPain();
         }

         if (var17 > 0.0F) {
            var13 = (int)((float)var13 - var17 / 10.0F);
         }

         var13 -= var1.getMoodles().getMoodleLevel(MoodleType.Tired) * 5;
         if (var13 <= 10) {
            var13 = 10;
         }

         if (var13 > 100 || !var2.isRanged()) {
            var13 = 100;
         }

         return var13;
      }
   }

   public static Vector3 getBoneWorldPos(IsoMovingObject var0, String var1, Vector3 var2) {
      IsoGameCharacter var3 = (IsoGameCharacter)Type.tryCastTo(var0, IsoGameCharacter.class);
      if (var3 != null && var1 != null) {
         AnimationPlayer var4 = var3.getAnimationPlayer();
         if (var4 != null && var4.isReady()) {
            int var5 = var4.getSkinningBoneIndex(var1, -1);
            if (var5 == -1) {
               return var2.set(var0.x, var0.y, var0.z);
            } else {
               Model.BoneToWorldCoords(var3, var5, var2);
               return var2;
            }
         } else {
            return var2.set(var0.x, var0.y, var0.z);
         }
      } else {
         return var2.set(var0.x, var0.y, var0.z);
      }
   }

   public void ConnectSwing(IsoGameCharacter var1, HandWeapon var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      IsoLivingCharacter var4 = (IsoLivingCharacter)var1;
      IsoPlayer var5 = (IsoPlayer)Type.tryCastTo(var1, IsoPlayer.class);
      if (var1.getVariableBoolean("ShoveAnim")) {
         var4.setDoShove(true);
      }

      if (GameServer.bServer) {
         DebugLog.log(DebugType.Network, "Player swing connects.");
      }

      LuaEventManager.triggerEvent("OnWeaponSwingHitPoint", var1, var2);
      if (var2.getPhysicsObject() != null) {
         var1.Throw(var2);
      }

      if (var2.isUseSelf()) {
         var2.Use();
      }

      if (var2.isOtherHandUse() && var1.getSecondaryHandItem() != null) {
         var1.getSecondaryHandItem().Use();
      }

      boolean var6 = false;
      if (var4.bDoShove && !var1.isAimAtFloor()) {
         var6 = true;
      }

      boolean var7 = false;
      boolean var8 = false;
      var1.attackVars.setWeapon(var2);
      var1.attackVars.targetOnGround.setMovingObject(var4.targetOnGround);
      var1.attackVars.bAimAtFloor = var1.isAimAtFloor();
      var1.attackVars.bDoShove = var4.bDoShove;
      if (var1.getVariableBoolean("ShoveAnim")) {
         var1.attackVars.bDoShove = true;
      }

      this.CalcHitList(var1, false, var1.attackVars, var1.hitList);
      int var9 = var1.hitList.size();
      boolean var10 = false;
      if (var9 == 0) {
         var10 = this.CheckObjectHit(var1, var2);
      }

      Stats var10000;
      if (var2.isUseEndurance()) {
         float var11 = 0.0F;
         if (var2.isTwoHandWeapon() && (var1.getPrimaryHandItem() != var2 || var1.getSecondaryHandItem() != var2)) {
            var11 = var2.getWeight() / 1.5F / 10.0F;
         }

         if (var9 <= 0 && !var1.isForceShove()) {
            float var12 = (var2.getWeight() * 0.28F * var2.getFatigueMod(var1) * var1.getFatigueMod() * var2.getEnduranceMod() * 0.3F + var11) * 0.04F;
            float var13 = 1.0F;
            if (var1.Traits.Asthmatic.isSet()) {
               var13 = 1.3F;
            }

            var10000 = var1.getStats();
            var10000.endurance -= var12 * var13;
         }
      }

      var1.setLastHitCount(var1.hitList.size());
      if (!var2.isMultipleHitConditionAffected()) {
         var7 = true;
      }

      int var45 = 1;
      this.dotList.clear();
      if (var1.hitList.isEmpty() && var1.getClickSound() != null && !var4.bDoShove) {
         if (var1 instanceof IsoPlayer && ((IsoPlayer)var1).isLocalPlayer() || !(var1 instanceof IsoPlayer)) {
            var1.getEmitter().playSound(var1.getClickSound());
         }

         var1.setRecoilDelay(10.0F);
      }

      boolean var46 = false;

      for(int var47 = 0; var47 < var1.hitList.size(); ++var47) {
         byte var14 = 0;
         boolean var15 = false;
         HitInfo var16 = (HitInfo)var1.hitList.get(var47);
         IsoMovingObject var17 = var16.getObject();
         BaseVehicle var18 = (BaseVehicle)Type.tryCastTo(var17, BaseVehicle.class);
         IsoZombie var19 = (IsoZombie)Type.tryCastTo(var17, IsoZombie.class);
         if (var16.getObject() == null && var16.window.getObject() != null) {
            var16.window.getObject().WeaponHit(var1, var2);
         } else {
            this.smashWindowBetween(var1, var17, var2);
            if (!this.isWindowBetween(var1, var17)) {
               int var20 = var16.chance;
               boolean var21 = Rand.Next(100) <= var20;
               if (var21) {
                  Vector2 var22 = tempVector2_1.set(var1.getX(), var1.getY());
                  Vector2 var23 = tempVector2_2.set(var17.getX(), var17.getY());
                  var23.x -= var22.x;
                  var23.y -= var22.y;
                  Vector2 var24 = var1.getLookVector(tempVector2_1);
                  var24.tangent();
                  var23.normalize();
                  boolean var25 = true;
                  float var26 = var24.dot(var23);

                  float var28;
                  for(int var27 = 0; var27 < this.dotList.size(); ++var27) {
                     var28 = (Float)this.dotList.get(var27);
                     if ((double)Math.abs(var26 - var28) < 1.0E-4D) {
                        var25 = false;
                     }
                  }

                  float var50 = var2.getMinDamage();
                  var28 = var2.getMaxDamage();
                  long var29 = 0L;
                  if (!var25) {
                     var50 /= 5.0F;
                     var28 /= 5.0F;
                  }

                  if (var1.isAimAtFloor() && !var2.isRanged() && var1.isNPC()) {
                     splash(var17, var2, var1);
                     var14 = (byte)Rand.Next(2);
                  } else if (var1.isAimAtFloor() && !var2.isRanged()) {
                     if (var5 == null || var5.isLocalPlayer()) {
                        if (!StringUtils.isNullOrEmpty(var2.getHitFloorSound())) {
                           var5.setMeleeHitSurface(ParameterMeleeHitSurface.Material.Body);
                           var29 = var1.playSound(var2.getHitFloorSound());
                        } else {
                           if (var5 != null) {
                              var5.setMeleeHitSurface(ParameterMeleeHitSurface.Material.Body);
                           }

                           var29 = var1.playSound(var2.getZombieHitSound());
                        }
                     }

                     int var31 = this.DoSwingCollisionBoneCheck(var1, GetWeapon(var1), (IsoGameCharacter)var17, ((IsoGameCharacter)var17).getAnimationPlayer().getSkinningBoneIndex("Bip01_Head", -1), 0.28F);
                     if (var31 == -1) {
                        var31 = this.DoSwingCollisionBoneCheck(var1, GetWeapon(var1), (IsoGameCharacter)var17, ((IsoGameCharacter)var17).getAnimationPlayer().getSkinningBoneIndex("Bip01_Spine", -1), 0.28F);
                        if (var31 == -1) {
                           var31 = this.DoSwingCollisionBoneCheck(var1, GetWeapon(var1), (IsoGameCharacter)var17, ((IsoGameCharacter)var17).getAnimationPlayer().getSkinningBoneIndex("Bip01_L_Calf", -1), 0.13F);
                           if (var31 == -1) {
                              var31 = this.DoSwingCollisionBoneCheck(var1, GetWeapon(var1), (IsoGameCharacter)var17, ((IsoGameCharacter)var17).getAnimationPlayer().getSkinningBoneIndex("Bip01_R_Calf", -1), 0.13F);
                           }

                           if (var31 == -1) {
                              var31 = this.DoSwingCollisionBoneCheck(var1, GetWeapon(var1), (IsoGameCharacter)var17, ((IsoGameCharacter)var17).getAnimationPlayer().getSkinningBoneIndex("Bip01_L_Foot", -1), 0.23F);
                           }

                           if (var31 == -1) {
                              var31 = this.DoSwingCollisionBoneCheck(var1, GetWeapon(var1), (IsoGameCharacter)var17, ((IsoGameCharacter)var17).getAnimationPlayer().getSkinningBoneIndex("Bip01_R_Foot", -1), 0.23F);
                           }

                           if (var31 == -1) {
                              continue;
                           }

                           var15 = true;
                        }
                     } else {
                        splash(var17, var2, var1);
                        splash(var17, var2, var1);
                        var14 = (byte)(Rand.Next(0, 3) + 1);
                     }
                  }

                  if (!var1.attackVars.bAimAtFloor && (!var1.attackVars.bCloseKill || !var1.isCriticalHit()) && !var4.bDoShove && var17 instanceof IsoGameCharacter && (var5 == null || var5.isLocalPlayer())) {
                     if (var5 != null) {
                        var5.setMeleeHitSurface(ParameterMeleeHitSurface.Material.Body);
                     }

                     if (var2.isRanged()) {
                        var29 = ((IsoGameCharacter)var17).playSound(var2.getZombieHitSound());
                     } else {
                        var29 = var1.playSound(var2.getZombieHitSound());
                     }
                  }

                  float var34;
                  if (var2.isRanged() && var19 != null) {
                     Vector2 var53 = tempVector2_1.set(var1.getX(), var1.getY());
                     Vector2 var32 = tempVector2_2.set(var17.getX(), var17.getY());
                     var32.x -= var53.x;
                     var32.y -= var53.y;
                     Vector2 var33 = var19.getForwardDirection();
                     var32.normalize();
                     var33.normalize();
                     var34 = var32.dot(var33);
                     var19.setHitFromBehind((double)var34 > 0.5D);
                  }

                  if (this.dotList.isEmpty()) {
                     this.dotList.add(var26);
                  }

                  if (var19 != null && var19.isCurrentState(ZombieOnGroundState.instance())) {
                     var19.setReanimateTimer(var19.getReanimateTimer() + (float)Rand.Next(10));
                  }

                  if (var19 != null && var19.isCurrentState(ZombieGetUpState.instance())) {
                     var19.setReanimateTimer((float)(Rand.Next(60) + 30));
                  }

                  boolean var54 = false;
                  if (!var2.isTwoHandWeapon() || var1.isItemInBothHands(var2)) {
                     var54 = true;
                  }

                  float var52 = var28 - var50;
                  float var51;
                  if (var52 == 0.0F) {
                     var51 = var50 + 0.0F;
                  } else {
                     var51 = var50 + (float)Rand.Next((int)(var52 * 1000.0F)) / 1000.0F;
                  }

                  if (!var2.isRanged()) {
                     var51 *= var2.getDamageMod(var1) * var1.getHittingMod();
                  }

                  if (!var54 && !var2.isRanged() && var28 > var50) {
                     var51 -= var50;
                  }

                  int var35;
                  if (var1.isAimAtFloor() && var4.bDoShove) {
                     var34 = 0.0F;

                     for(var35 = BodyPartType.ToIndex(BodyPartType.UpperLeg_L); var35 <= BodyPartType.ToIndex(BodyPartType.Foot_R); ++var35) {
                        var34 += ((BodyPart)var1.getBodyDamage().getBodyParts().get(var35)).getPain();
                     }

                     if (var34 > 10.0F) {
                        var51 /= PZMath.clamp(var34 / 10.0F, 1.0F, 30.0F);
                        MoodlesUI.getInstance().wiggle(MoodleType.Pain);
                        MoodlesUI.getInstance().wiggle(MoodleType.Injured);
                     }
                  } else {
                     var34 = 0.0F;

                     for(var35 = BodyPartType.ToIndex(BodyPartType.Hand_L); var35 <= BodyPartType.ToIndex(BodyPartType.UpperArm_R); ++var35) {
                        var34 += ((BodyPart)var1.getBodyDamage().getBodyParts().get(var35)).getPain();
                     }

                     if (var34 > 10.0F) {
                        var51 /= PZMath.clamp(var34 / 10.0F, 1.0F, 30.0F);
                        MoodlesUI.getInstance().wiggle(MoodleType.Pain);
                        MoodlesUI.getInstance().wiggle(MoodleType.Injured);
                     }
                  }

                  if (var1.Traits.Underweight.isSet()) {
                     var51 *= 0.8F;
                  }

                  if (var1.Traits.VeryUnderweight.isSet()) {
                     var51 *= 0.6F;
                  }

                  if (var1.Traits.Emaciated.isSet()) {
                     var51 *= 0.4F;
                  }

                  var34 = var51 / ((float)var45 / 2.0F);
                  if (var1.isAttackWasSuperAttack()) {
                     var34 *= 5.0F;
                  }

                  ++var45;
                  if (var2.isMultipleHitConditionAffected()) {
                     var7 = true;
                  }

                  Vector2 var55 = tempVector2_1.set(var1.getX(), var1.getY());
                  Vector2 var36 = tempVector2_2.set(var17.getX(), var17.getY());
                  var36.x -= var55.x;
                  var36.y -= var55.y;
                  float var37 = var36.getLength();
                  float var38 = 1.0F;
                  if (!var2.isRangeFalloff()) {
                     var38 = var37 / var2.getMaxRange(var1);
                  }

                  var38 *= 2.0F;
                  if (var38 < 0.3F) {
                     var38 = 1.0F;
                  }

                  if (var2.isRanged() && var1.getPerkLevel(PerkFactory.Perks.Aiming) < 6 && var1.getMoodles().getMoodleLevel(MoodleType.Panic) > 2) {
                     var34 -= (float)var1.getMoodles().getMoodleLevel(MoodleType.Panic) * 0.2F;
                     MoodlesUI.getInstance().wiggle(MoodleType.Panic);
                  }

                  if (!var2.isRanged() && var1.getMoodles().getMoodleLevel(MoodleType.Panic) > 1) {
                     var34 -= (float)var1.getMoodles().getMoodleLevel(MoodleType.Panic) * 0.1F;
                     MoodlesUI.getInstance().wiggle(MoodleType.Panic);
                  }

                  if (var1.getMoodles().getMoodleLevel(MoodleType.Stress) > 1) {
                     var34 -= (float)var1.getMoodles().getMoodleLevel(MoodleType.Stress) * 0.1F;
                     MoodlesUI.getInstance().wiggle(MoodleType.Stress);
                  }

                  if (var34 < 0.0F) {
                     var34 = 0.1F;
                  }

                  if (var1.isAimAtFloor() && var4.bDoShove) {
                     var34 = Rand.Next(0.7F, 1.0F) + (float)var1.getPerkLevel(PerkFactory.Perks.Strength) * 0.2F;
                     Clothing var39 = (Clothing)var1.getWornItem("Shoes");
                     if (var39 == null) {
                        var34 *= 0.5F;
                     } else {
                        var34 *= var39.getStompPower();
                     }
                  }

                  if (!var2.isRanged()) {
                     switch(var1.getMoodles().getMoodleLevel(MoodleType.Endurance)) {
                     case 0:
                     default:
                        break;
                     case 1:
                        var34 *= 0.5F;
                        MoodlesUI.getInstance().wiggle(MoodleType.Endurance);
                        break;
                     case 2:
                        var34 *= 0.2F;
                        MoodlesUI.getInstance().wiggle(MoodleType.Endurance);
                        break;
                     case 3:
                        var34 *= 0.1F;
                        MoodlesUI.getInstance().wiggle(MoodleType.Endurance);
                        break;
                     case 4:
                        var34 *= 0.05F;
                        MoodlesUI.getInstance().wiggle(MoodleType.Endurance);
                     }

                     switch(var1.getMoodles().getMoodleLevel(MoodleType.Tired)) {
                     case 0:
                     default:
                        break;
                     case 1:
                        var34 *= 0.5F;
                        MoodlesUI.getInstance().wiggle(MoodleType.Tired);
                        break;
                     case 2:
                        var34 *= 0.2F;
                        MoodlesUI.getInstance().wiggle(MoodleType.Tired);
                        break;
                     case 3:
                        var34 *= 0.1F;
                        MoodlesUI.getInstance().wiggle(MoodleType.Tired);
                        break;
                     case 4:
                        var34 *= 0.05F;
                        MoodlesUI.getInstance().wiggle(MoodleType.Tired);
                     }
                  }

                  var1.knockbackAttackMod = 1.0F;
                  if ("KnifeDeath".equals(var1.getVariableString("ZombieHitReaction"))) {
                     var38 *= 1000.0F;
                     var1.knockbackAttackMod = 0.0F;
                     var1.addWorldSoundUnlessInvisible(4, 4, false);
                     var1.attackVars.bCloseKill = true;
                     var17.setCloseKilled(true);
                  } else {
                     var1.attackVars.bCloseKill = false;
                     var17.setCloseKilled(false);
                     var1.addWorldSoundUnlessInvisible(8, 8, false);
                     if (Rand.Next(3) != 0 && (!var1.isAimAtFloor() || !var4.bDoShove)) {
                        if (Rand.Next(7) == 0) {
                           var1.addWorldSoundUnlessInvisible(16, 16, false);
                        }
                     } else {
                        var1.addWorldSoundUnlessInvisible(10, 10, false);
                     }
                  }

                  var17.setHitFromAngle(var16.dot);
                  int var43;
                  if (var19 != null) {
                     var19.setHitFromBehind(var1.isBehind(var19));
                     var19.setHitAngle(var1.getForwardDirection());
                     var19.setPlayerAttackPosition(var19.testDotSide(var1));
                     var19.setHitHeadWhileOnFloor(var14);
                     var19.setHitLegsWhileOnFloor(var15);
                     if (var14 > 0) {
                        var19.addBlood(BloodBodyPartType.Head, true, true, true);
                        var19.addBlood(BloodBodyPartType.Torso_Upper, true, false, false);
                        var19.addBlood(BloodBodyPartType.UpperArm_L, true, false, false);
                        var19.addBlood(BloodBodyPartType.UpperArm_R, true, false, false);
                        var34 *= 3.0F;
                     }

                     if (var15) {
                        var34 = 0.0F;
                     }

                     boolean var56 = false;
                     int var57;
                     if (var14 > 0) {
                        var57 = Rand.Next(BodyPartType.ToIndex(BodyPartType.Head), BodyPartType.ToIndex(BodyPartType.Neck) + 1);
                     } else if (var15) {
                        var57 = Rand.Next(BodyPartType.ToIndex(BodyPartType.Groin), BodyPartType.ToIndex(BodyPartType.Foot_R) + 1);
                     } else {
                        var57 = Rand.Next(BodyPartType.ToIndex(BodyPartType.Hand_L), BodyPartType.ToIndex(BodyPartType.Neck) + 1);
                     }

                     float var40 = var19.getBodyPartClothingDefense(var57, false, var2.isRanged()) / 2.0F;
                     var40 += var19.getBodyPartClothingDefense(var57, true, var2.isRanged());
                     if (var40 > 70.0F) {
                        var40 = 70.0F;
                     }

                     float var41 = var34 * Math.abs(1.0F - var40 / 100.0F);
                     var34 = var41;
                     if (!GameClient.bClient && !GameServer.bServer || GameClient.bClient && var1 instanceof IsoPlayer && ((IsoPlayer)var1).isLocalPlayer()) {
                        var8 = var19.helmetFall(var14 > 0);
                     }

                     if ("KnifeDeath".equals(var1.getVariableString("ZombieHitReaction")) && !"Tutorial".equals(Core.GameMode)) {
                        byte var42 = 8;
                        if (var19.isCurrentState(AttackState.instance())) {
                           var42 = 3;
                        }

                        var43 = var1.getPerkLevel(PerkFactory.Perks.SmallBlade) + 1;
                        if (Rand.NextBool(var42 + var43 * 2)) {
                           InventoryItem var44 = var1.getPrimaryHandItem();
                           var1.getInventory().Remove(var44);
                           var1.removeFromHands(var44);
                           var19.setAttachedItem("JawStab", var44);
                           var19.setJawStabAttach(true);
                        }

                        var19.setKnifeDeath(true);
                     }
                  }

                  float var58 = 0.0F;
                  boolean var59 = var1.isCriticalHit();
                  if (var18 == null && var17.getSquare() != null && var1.getSquare() != null) {
                     var17.setCloseKilled(var1.attackVars.bCloseKill);
                     if (((IsoPlayer)var1).isLocalPlayer() || var1.isNPC()) {
                        var58 = var17.Hit(var2, var1, var34, var6, var38);
                        this.setParameterCharacterHitResult(var1, var19, var29);
                     }

                     LuaEventManager.triggerEvent("OnWeaponHitXp", var1, var2, var17, var34);
                     if ((!var4.bDoShove || var1.isAimAtFloor()) && var1.DistToSquared(var17) < 2.0F && Math.abs(var1.z - var17.z) < 0.5F) {
                        var1.addBlood((BloodBodyPartType)null, false, false, false);
                     }

                     if (var17 instanceof IsoGameCharacter) {
                        if (((IsoGameCharacter)var17).isDead()) {
                           var10000 = var1.getStats();
                           var10000.stress -= 0.02F;
                        } else if (!(var17 instanceof IsoPlayer) && (!var4.bDoShove || var1.isAimAtFloor())) {
                           splash(var17, var2, var1);
                        }
                     }
                  } else if (var18 != null) {
                     VehiclePart var60 = var18.getNearestBodyworkPart(var1);
                     if (var60 != null) {
                        VehicleWindow var61 = var60.getWindow();

                        for(var43 = 0; var43 < var60.getChildCount(); ++var43) {
                           VehiclePart var62 = var60.getChild(var43);
                           if (var62.getWindow() != null) {
                              var61 = var62.getWindow();
                              break;
                           }
                        }

                        if (var61 != null && var61.isHittable()) {
                           var43 = this.calcDamageToVehicle((int)var34 * 10, var2.getDoorDamage(), true);
                           var61.damage(var43);
                           var1.playSound("HitVehicleWindowWithWeapon");
                        } else {
                           var43 = this.calcDamageToVehicle((int)var34 * 10, var2.getDoorDamage(), false);
                           var60.setCondition(var60.getCondition() - var43);
                           var5.setVehicleHitLocation(var18);
                           var1.playSound("HitVehiclePartWithWeapon");
                        }
                     }
                  }

                  if (GameClient.bClient && var1.isLocal()) {
                     if (var17 instanceof IsoPlayer) {
                        var1.setSafetyCooldown(var1.getSafetyCooldown() + (float)ServerOptions.instance.SafetyCooldownTimer.getValue());
                     }

                     if (var17 instanceof IsoGameCharacter) {
                        HitReactionNetworkAI.CalcHitReactionWeapon(var1, (IsoGameCharacter)var17, var2);
                     }

                     var46 = GameClient.sendHitCharacter(var1, var17, var2, var58, var6, var38, var59, var8, var14 > 0);
                  }
               }
            }
         }
      }

      if (GameClient.bClient && ((IsoPlayer)var1).isLocalPlayer() && !var46) {
         GameClient.sendHitCharacter(var1, (IsoMovingObject)null, var2, 0.0F, var6, 1.0F, var1.isCriticalHit(), false, false);
      }

      if (!var7 && var10) {
         boolean var48 = this.bHitOnlyTree && var2.getScriptItem().Categories.contains("Axe");
         int var49 = var48 ? 2 : 1;
         if (Rand.Next(var2.getConditionLowerChance() * var49 + var1.getMaintenanceMod() * 2) == 0) {
            var7 = true;
         } else if (Rand.NextBool(2) && !var2.getName().contains("Bare Hands") && (!var2.isTwoHandWeapon() || var1.getPrimaryHandItem() == var2 || var1.getSecondaryHandItem() == var2 || !Rand.NextBool(3))) {
            var1.getXp().AddXP(PerkFactory.Perks.Maintenance, 1.0F);
         }
      }

      var3.put(PARAM_LOWER_CONDITION, var7 ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_ATTACKED, Boolean.TRUE);
   }

   private int calcDamageToVehicle(int var1, int var2, boolean var3) {
      if (var1 <= 0) {
         return 0;
      } else {
         float var4 = (float)var1;
         float var5 = PZMath.clamp(var4 / (var3 ? 10.0F : 40.0F), 0.0F, 1.0F);
         int var6 = (int)((float)var2 * var5);
         return PZMath.clamp(var6, 1, var2);
      }
   }

   public static void splash(IsoMovingObject var0, HandWeapon var1, IsoGameCharacter var2) {
      IsoGameCharacter var3 = (IsoGameCharacter)var0;
      if (var1 != null && SandboxOptions.instance.BloodLevel.getValue() > 1) {
         int var4 = var1.getSplatNumber();
         if (var4 < 1) {
            var4 = 1;
         }

         if (Core.bLastStand) {
            var4 *= 3;
         }

         switch(SandboxOptions.instance.BloodLevel.getValue()) {
         case 2:
            var4 /= 2;
         case 3:
         default:
            break;
         case 4:
            var4 *= 2;
            break;
         case 5:
            var4 *= 5;
         }

         for(int var5 = 0; var5 < var4; ++var5) {
            var3.splatBlood(3, 0.3F);
         }
      }

      byte var11 = 3;
      byte var10 = 7;
      switch(SandboxOptions.instance.BloodLevel.getValue()) {
      case 1:
         var10 = 0;
         break;
      case 2:
         var10 = 4;
         var11 = 5;
      case 3:
      default:
         break;
      case 4:
         var10 = 10;
         var11 = 2;
         break;
      case 5:
         var10 = 15;
         var11 = 0;
      }

      if (SandboxOptions.instance.BloodLevel.getValue() > 1) {
         var3.splatBloodFloorBig();
      }

      float var6 = 0.5F;
      if (var3 instanceof IsoZombie && (((IsoZombie)var3).bCrawling || var3.getCurrentState() == ZombieOnGroundState.instance())) {
         var6 = 0.2F;
      }

      float var7 = Rand.Next(1.5F, 5.0F);
      float var8 = Rand.Next(1.5F, 5.0F);
      if (var2 instanceof IsoPlayer && ((IsoPlayer)var2).bDoShove) {
         var7 = Rand.Next(0.0F, 0.5F);
         var8 = Rand.Next(0.0F, 0.5F);
      }

      if (var10 > 0) {
         var3.playBloodSplatterSound();
      }

      for(int var9 = 0; var9 < var10; ++var9) {
         if (Rand.Next(var11) == 0) {
            new IsoZombieGiblets(IsoZombieGiblets.GibletType.A, var3.getCell(), var3.getX(), var3.getY(), var3.getZ() + var6, var3.getHitDir().x * var7, var3.getHitDir().y * var8);
         }
      }

   }

   private boolean checkObjectHit(IsoGameCharacter var1, HandWeapon var2, IsoGridSquare var3, boolean var4, boolean var5) {
      if (var3 == null) {
         return false;
      } else {
         for(int var6 = var3.getSpecialObjects().size() - 1; var6 >= 0; --var6) {
            IsoObject var7 = (IsoObject)var3.getSpecialObjects().get(var6);
            IsoDoor var8 = (IsoDoor)Type.tryCastTo(var7, IsoDoor.class);
            IsoThumpable var9 = (IsoThumpable)Type.tryCastTo(var7, IsoThumpable.class);
            IsoWindow var10 = (IsoWindow)Type.tryCastTo(var7, IsoWindow.class);
            Thumpable var11;
            if (var8 != null && (var4 && var8.north || var5 && !var8.north)) {
               var11 = var8.getThumpableFor(var1);
               if (var11 != null) {
                  var11.WeaponHit(var1, var2);
                  return true;
               }
            }

            if (var9 != null) {
               if (!var9.isDoor() && !var9.isWindow() && var9.isBlockAllTheSquare()) {
                  var11 = var9.getThumpableFor(var1);
                  if (var11 != null) {
                     var11.WeaponHit(var1, var2);
                     return true;
                  }
               } else if (var4 && var9.north || var5 && !var9.north) {
                  var11 = var9.getThumpableFor(var1);
                  if (var11 != null) {
                     var11.WeaponHit(var1, var2);
                     return true;
                  }
               }
            }

            if (var10 != null && (var4 && var10.north || var5 && !var10.north)) {
               var11 = var10.getThumpableFor(var1);
               if (var11 != null) {
                  var11.WeaponHit(var1, var2);
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean CheckObjectHit(IsoGameCharacter var1, HandWeapon var2) {
      if (var1.isAimAtFloor()) {
         this.bHitOnlyTree = false;
         return false;
      } else {
         boolean var3 = false;
         int var4 = 0;
         int var5 = 0;
         IsoDirections var6 = IsoDirections.fromAngle(var1.getForwardDirection());
         int var7 = 0;
         int var8 = 0;
         if (var6 == IsoDirections.NE || var6 == IsoDirections.N || var6 == IsoDirections.NW) {
            --var8;
         }

         if (var6 == IsoDirections.SE || var6 == IsoDirections.S || var6 == IsoDirections.SW) {
            ++var8;
         }

         if (var6 == IsoDirections.NW || var6 == IsoDirections.W || var6 == IsoDirections.SW) {
            --var7;
         }

         if (var6 == IsoDirections.NE || var6 == IsoDirections.E || var6 == IsoDirections.SE) {
            ++var7;
         }

         IsoCell var9 = IsoWorld.instance.CurrentCell;
         IsoGridSquare var10 = var1.getCurrentSquare();
         IsoGridSquare var11 = var9.getGridSquare(var10.getX() + var7, var10.getY() + var8, var10.getZ());
         if (var11 != null) {
            if (this.checkObjectHit(var1, var2, var11, false, false)) {
               var3 = true;
               ++var4;
            }

            if (!var11.isBlockedTo(var10)) {
               for(int var12 = 0; var12 < var11.getObjects().size(); ++var12) {
                  IsoObject var13 = (IsoObject)var11.getObjects().get(var12);
                  if (var13 instanceof IsoTree) {
                     ((IsoTree)var13).WeaponHit(var1, var2);
                     var3 = true;
                     ++var4;
                     ++var5;
                     if (var13.getObjectIndex() == -1) {
                        --var12;
                     }
                  }
               }
            }
         }

         if ((var6 == IsoDirections.NE || var6 == IsoDirections.N || var6 == IsoDirections.NW) && this.checkObjectHit(var1, var2, var10, true, false)) {
            var3 = true;
            ++var4;
         }

         IsoGridSquare var14;
         if (var6 == IsoDirections.SE || var6 == IsoDirections.S || var6 == IsoDirections.SW) {
            var14 = var9.getGridSquare(var10.getX(), var10.getY() + 1, var10.getZ());
            if (this.checkObjectHit(var1, var2, var14, true, false)) {
               var3 = true;
               ++var4;
            }
         }

         if (var6 == IsoDirections.SE || var6 == IsoDirections.E || var6 == IsoDirections.NE) {
            var14 = var9.getGridSquare(var10.getX() + 1, var10.getY(), var10.getZ());
            if (this.checkObjectHit(var1, var2, var14, false, true)) {
               var3 = true;
               ++var4;
            }
         }

         if ((var6 == IsoDirections.NW || var6 == IsoDirections.W || var6 == IsoDirections.SW) && this.checkObjectHit(var1, var2, var10, false, true)) {
            var3 = true;
            ++var4;
         }

         this.bHitOnlyTree = var3 && var4 == var5;
         return var3;
      }
   }

   private LosUtil.TestResults los(int var1, int var2, int var3, int var4, int var5, SwipeStatePlayer.LOSVisitor var6) {
      IsoCell var7 = IsoWorld.instance.CurrentCell;
      int var10 = var4 - var2;
      int var11 = var3 - var1;
      int var12 = var5 - var5;
      float var13 = 0.5F;
      float var14 = 0.5F;
      IsoGridSquare var15 = var7.getGridSquare(var1, var2, var5);
      float var16;
      float var17;
      IsoGridSquare var18;
      if (Math.abs(var11) > Math.abs(var10)) {
         var16 = (float)var10 / (float)var11;
         var17 = (float)var12 / (float)var11;
         var13 += (float)var2;
         var14 += (float)var5;
         var11 = var11 < 0 ? -1 : 1;
         var16 *= (float)var11;

         for(var17 *= (float)var11; var1 != var3; var15 = var18) {
            var1 += var11;
            var13 += var16;
            var14 += var17;
            var18 = var7.getGridSquare(var1, (int)var13, (int)var14);
            if (var6.visit(var18, var15)) {
               return var6.getResult();
            }
         }
      } else {
         var16 = (float)var11 / (float)var10;
         var17 = (float)var12 / (float)var10;
         var13 += (float)var1;
         var14 += (float)var5;
         var10 = var10 < 0 ? -1 : 1;
         var16 *= (float)var10;

         for(var17 *= (float)var10; var2 != var4; var15 = var18) {
            var2 += var10;
            var13 += var16;
            var14 += var17;
            var18 = var7.getGridSquare((int)var13, var2, (int)var14);
            if (var6.visit(var18, var15)) {
               return var6.getResult();
            }
         }
      }

      return LosUtil.TestResults.Clear;
   }

   private IsoWindow getWindowBetween(int var1, int var2, int var3, int var4, int var5) {
      this.windowVisitor.init();
      this.los(var1, var2, var3, var4, var5, this.windowVisitor);
      return this.windowVisitor.window;
   }

   private IsoWindow getWindowBetween(IsoMovingObject var1, IsoMovingObject var2) {
      return this.getWindowBetween((int)var1.x, (int)var1.y, (int)var2.x, (int)var2.y, (int)var1.z);
   }

   private boolean isWindowBetween(IsoMovingObject var1, IsoMovingObject var2) {
      return this.getWindowBetween(var1, var2) != null;
   }

   private void smashWindowBetween(IsoGameCharacter var1, IsoMovingObject var2, HandWeapon var3) {
      IsoWindow var4 = this.getWindowBetween(var1, var2);
      if (var4 != null) {
         var4.WeaponHit(var1, var3);
      }
   }

   public void changeWeapon(HandWeapon var1, IsoGameCharacter var2) {
      if (var1 != null && var1.isUseSelf()) {
         var2.getInventory().setDrawDirty(true);
         Iterator var3 = var2.getInventory().getItems().iterator();

         while(var3.hasNext()) {
            InventoryItem var4 = (InventoryItem)var3.next();
            if (var4 != var1 && var4 instanceof HandWeapon && var4.getType() == var1.getType() && var4.getCondition() > 0) {
               if (var2.getPrimaryHandItem() == var1 && var2.getSecondaryHandItem() == var1) {
                  var2.setPrimaryHandItem(var4);
                  var2.setSecondaryHandItem(var4);
               } else if (var2.getPrimaryHandItem() == var1) {
                  var2.setPrimaryHandItem(var4);
               } else if (var2.getSecondaryHandItem() == var1) {
                  var2.setSecondaryHandItem(var4);
               }

               return;
            }
         }
      }

      if (var1 == null || var1.getCondition() <= 0 || var1.isUseSelf()) {
         HandWeapon var5 = (HandWeapon)var2.getInventory().getBestWeapon(var2.getDescriptor());
         var2.setPrimaryHandItem((InventoryItem)null);
         if (var2.getSecondaryHandItem() == var1) {
            var2.setSecondaryHandItem((InventoryItem)null);
         }

         if (var5 != null && var5 != var2.getPrimaryHandItem() && var5.getCondition() > 0) {
            var2.setPrimaryHandItem(var5);
            if (var5.isTwoHandWeapon() && var2.getSecondaryHandItem() == null) {
               var2.setSecondaryHandItem(var5);
            }
         }
      }

   }

   private void setParameterCharacterHitResult(IsoGameCharacter var1, IsoZombie var2, long var3) {
      if (var3 != 0L) {
         byte var5 = 0;
         if (var2 != null) {
            if (var2.isDead()) {
               var5 = 2;
            } else if (var2.isKnockedDown()) {
               var5 = 1;
            }
         }

         var1.getEmitter().setParameterValue(var3, FMODManager.instance.getParameterDescription("CharacterHitResult"), (float)var5);
      }
   }

   private static final class WindowVisitor implements SwipeStatePlayer.LOSVisitor {
      LosUtil.TestResults test;
      IsoWindow window;

      void init() {
         this.test = LosUtil.TestResults.Clear;
         this.window = null;
      }

      public boolean visit(IsoGridSquare var1, IsoGridSquare var2) {
         if (var1 != null && var2 != null) {
            boolean var3 = true;
            boolean var4 = false;
            LosUtil.TestResults var5 = var1.testVisionAdjacent(var2.getX() - var1.getX(), var2.getY() - var1.getY(), var2.getZ() - var1.getZ(), var3, var4);
            if (var5 == LosUtil.TestResults.ClearThroughWindow) {
               IsoWindow var6 = var1.getWindowTo(var2);
               if (this.isHittable(var6) && var6.TestVision(var1, var2) == IsoObject.VisionResult.Unblocked) {
                  this.window = var6;
                  return true;
               }
            }

            if (var5 == LosUtil.TestResults.Blocked || this.test == LosUtil.TestResults.Clear || var5 == LosUtil.TestResults.ClearThroughWindow && this.test == LosUtil.TestResults.ClearThroughOpenDoor) {
               this.test = var5;
            } else if (var5 == LosUtil.TestResults.ClearThroughClosedDoor && this.test == LosUtil.TestResults.ClearThroughOpenDoor) {
               this.test = var5;
            }

            return this.test == LosUtil.TestResults.Blocked;
         } else {
            return false;
         }
      }

      public LosUtil.TestResults getResult() {
         return this.test;
      }

      boolean isHittable(IsoWindow var1) {
         if (var1 == null) {
            return false;
         } else if (var1.isBarricaded()) {
            return true;
         } else {
            return !var1.isDestroyed() && !var1.IsOpen();
         }
      }
   }

   public static class CustomComparator implements Comparator {
      public int compare(HitInfo var1, HitInfo var2) {
         float var3 = var1.distSq;
         float var4 = var2.distSq;
         IsoZombie var5 = (IsoZombie)Type.tryCastTo(var1.getObject(), IsoZombie.class);
         IsoZombie var6 = (IsoZombie)Type.tryCastTo(var2.getObject(), IsoZombie.class);
         if (var5 != null && var6 != null) {
            boolean var7 = SwipeStatePlayer.isProne(var5);
            boolean var8 = SwipeStatePlayer.isProne(var6);
            boolean var9 = var5.isCurrentState(ZombieGetUpState.instance());
            boolean var10 = var6.isCurrentState(ZombieGetUpState.instance());
            if (var9 && !var10 && var8) {
               return -1;
            }

            if (!var9 && var7 && var10) {
               return 1;
            }

            if (var7 && var8) {
               if (var5.isCrawling() && !var6.isCrawling()) {
                  return -1;
               }

               if (!var5.isCrawling() && var6.isCrawling()) {
                  return 1;
               }
            }
         }

         if (var3 > var4) {
            return 1;
         } else {
            return var4 > var3 ? -1 : 0;
         }
      }
   }

   private interface LOSVisitor {
      boolean visit(IsoGridSquare var1, IsoGridSquare var2);

      LosUtil.TestResults getResult();
   }
}
