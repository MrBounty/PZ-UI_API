package zombie.ai.states;

import fmod.fmod.FMODManager;
import java.util.HashMap;
import se.krka.kahlua.vm.KahluaTableIterator;
import zombie.GameTime;
import zombie.ZomboidGlobals;
import zombie.ai.State;
import zombie.audio.parameters.ParameterCharacterMovementSpeed;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.MoveDeltaModifiers;
import zombie.characters.Stats;
import zombie.characters.BodyDamage.BodyPart;
import zombie.characters.BodyDamage.BodyPartType;
import zombie.characters.Moodles.MoodleType;
import zombie.characters.skills.PerkFactory;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.core.properties.PropertyContainer;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.debug.DebugOptions;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoThumpable;
import zombie.util.StringUtils;
import zombie.util.Type;

public final class ClimbOverFenceState extends State {
   private static final ClimbOverFenceState _instance = new ClimbOverFenceState();
   static final Integer PARAM_START_X = 0;
   static final Integer PARAM_START_Y = 1;
   static final Integer PARAM_Z = 2;
   static final Integer PARAM_END_X = 3;
   static final Integer PARAM_END_Y = 4;
   static final Integer PARAM_DIR = 5;
   static final Integer PARAM_ZOMBIE_ON_FLOOR = 6;
   static final Integer PARAM_PREV_STATE = 7;
   static final Integer PARAM_SCRATCH = 8;
   static final Integer PARAM_COUNTER = 9;
   static final Integer PARAM_SOLID_FLOOR = 10;
   static final Integer PARAM_SHEET_ROPE = 11;
   static final Integer PARAM_RUN = 12;
   static final Integer PARAM_SPRINT = 13;
   static final Integer PARAM_COLLIDABLE = 14;
   static final int FENCE_TYPE_WOOD = 0;
   static final int FENCE_TYPE_METAL = 1;
   static final int FENCE_TYPE_SANDBAG = 2;
   static final int FENCE_TYPE_GRAVELBAG = 3;
   static final int FENCE_TYPE_BARBWIRE = 4;
   static final int TRIP_WOOD = 0;
   static final int TRIP_METAL = 1;
   static final int TRIP_SANDBAG = 2;
   static final int TRIP_GRAVELBAG = 3;
   static final int TRIP_BARBWIRE = 4;
   public static final int TRIP_TREE = 5;
   public static final int TRIP_ZOMBIE = 6;
   public static final int COLLIDE_WITH_WALL = 7;

   public static ClimbOverFenceState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setVariable("FenceLungeX", 0.0F);
      var1.setVariable("FenceLungeY", 0.0F);
      HashMap var2 = var1.getStateMachineParams(this);
      var1.setIgnoreMovement(true);
      Stats var10000;
      if (var2.get(PARAM_RUN) == Boolean.TRUE) {
         var1.setVariable("VaultOverRun", true);
         var10000 = var1.getStats();
         var10000.endurance = (float)((double)var10000.endurance - ZomboidGlobals.RunningEnduranceReduce * 300.0D);
      } else if (var2.get(PARAM_SPRINT) == Boolean.TRUE) {
         var1.setVariable("VaultOverSprint", true);
         var10000 = var1.getStats();
         var10000.endurance = (float)((double)var10000.endurance - ZomboidGlobals.RunningEnduranceReduce * 700.0D);
      }

      boolean var3 = var2.get(PARAM_COUNTER) == Boolean.TRUE;
      var1.setVariable("ClimbingFence", true);
      var1.setVariable("ClimbFenceStarted", false);
      var1.setVariable("ClimbFenceFinished", false);
      var1.setVariable("ClimbFenceOutcome", var3 ? "obstacle" : "success");
      var1.clearVariable("ClimbFenceFlopped");
      if ((var1.getVariableBoolean("VaultOverRun") || var1.getVariableBoolean("VaultOverSprint")) && this.shouldFallAfterVaultOver(var1)) {
         var1.setVariable("ClimbFenceOutcome", "fall");
      }

      IsoZombie var4 = (IsoZombie)Type.tryCastTo(var1, IsoZombie.class);
      if (!var3 && var4 != null && var4.shouldDoFenceLunge()) {
         var1.setVariable("ClimbFenceOutcome", "lunge");
         this.setLungeXVars(var4);
      }

      if (var2.get(PARAM_SOLID_FLOOR) == Boolean.FALSE) {
         var1.setVariable("ClimbFenceOutcome", "falling");
      }

      if (!(var1 instanceof IsoZombie) && var2.get(PARAM_SHEET_ROPE) == Boolean.TRUE) {
         var1.setVariable("ClimbFenceOutcome", "rope");
      }

      if (var1 instanceof IsoPlayer && ((IsoPlayer)var1).isLocalPlayer()) {
         ((IsoPlayer)var1).dirtyRecalcGridStackTime = 20.0F;
      }

   }

   private void setLungeXVars(IsoZombie var1) {
      IsoMovingObject var2 = var1.getTarget();
      if (var2 != null) {
         var1.setVariable("FenceLungeX", 0.0F);
         var1.setVariable("FenceLungeY", 0.0F);
         float var3 = 0.0F;
         Vector2 var4 = var1.getForwardDirection();
         PZMath.SideOfLine var5 = PZMath.testSideOfLine(var1.x, var1.y, var1.x + var4.x, var1.y + var4.y, var2.x, var2.y);
         float var6 = (float)Math.acos((double)var1.getDotWithForwardDirection(var2.x, var2.y));
         float var7 = PZMath.clamp(PZMath.radToDeg(var6), 0.0F, 90.0F);
         switch(var5) {
         case Left:
            var3 = -var7 / 90.0F;
            break;
         case OnLine:
            var3 = 0.0F;
            break;
         case Right:
            var3 = var7 / 90.0F;
         }

         var1.setVariable("FenceLungeX", var3);
      }
   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      IsoDirections var3 = (IsoDirections)Type.tryCastTo(var2.get(PARAM_DIR), IsoDirections.class);
      int var4 = (Integer)var2.get(PARAM_END_X);
      int var5 = (Integer)var2.get(PARAM_END_Y);
      var1.setAnimated(true);
      if (var3 == IsoDirections.N) {
         var1.setDir(IsoDirections.N);
      } else if (var3 == IsoDirections.S) {
         var1.setDir(IsoDirections.S);
      } else if (var3 == IsoDirections.W) {
         var1.setDir(IsoDirections.W);
      } else if (var3 == IsoDirections.E) {
         var1.setDir(IsoDirections.E);
      }

      String var6 = var1.getVariableString("ClimbFenceOutcome");
      float var7;
      if (!"lunge".equals(var6)) {
         var7 = 0.05F;
         if (var3 != IsoDirections.N && var3 != IsoDirections.S) {
            if (var3 == IsoDirections.W || var3 == IsoDirections.E) {
               var1.y = var1.ny = PZMath.clamp(var1.y, (float)var5 + var7, (float)(var5 + 1) - var7);
            }
         } else {
            var1.x = var1.nx = PZMath.clamp(var1.x, (float)var4 + var7, (float)(var4 + 1) - var7);
         }
      }

      if (var1.getVariableBoolean("ClimbFenceStarted") && !"back".equals(var6) && !"fallback".equals(var6) && !"lunge".equalsIgnoreCase(var6) && !"obstacle".equals(var6) && !"obstacleEnd".equals(var6)) {
         var7 = (float)(Integer)var2.get(PARAM_START_X);
         float var8 = (float)(Integer)var2.get(PARAM_START_Y);
         switch(var3) {
         case N:
            var8 -= 0.1F;
            break;
         case S:
            ++var8;
            break;
         case W:
            var7 -= 0.1F;
            break;
         case E:
            ++var7;
         }

         if ((int)var1.x != (int)var7 && (var3 == IsoDirections.W || var3 == IsoDirections.E)) {
            this.slideX(var1, var7);
         }

         if ((int)var1.y != (int)var8 && (var3 == IsoDirections.N || var3 == IsoDirections.S)) {
            this.slideY(var1, var8);
         }
      }

      if (var1 instanceof IsoZombie) {
         boolean var9 = var2.get(PARAM_ZOMBIE_ON_FLOOR) == Boolean.TRUE;
         var1.setOnFloor(var9);
         ((IsoZombie)var1).setKnockedDown(var9);
         var1.setFallOnFront(var9);
      }

   }

   public void exit(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      if (var1 instanceof IsoPlayer && "fall".equals(var1.getVariableString("ClimbFenceOutcome"))) {
         var1.setSprinting(false);
      }

      var1.clearVariable("ClimbingFence");
      var1.clearVariable("ClimbFenceFinished");
      var1.clearVariable("ClimbFenceOutcome");
      var1.clearVariable("ClimbFenceStarted");
      var1.clearVariable("ClimbFenceFlopped");
      var1.ClearVariable("VaultOverSprint");
      var1.ClearVariable("VaultOverRun");
      var1.setIgnoreMovement(false);
      IsoZombie var3 = (IsoZombie)Type.tryCastTo(var1, IsoZombie.class);
      if (var3 != null) {
         var3.AllowRepathDelay = 0.0F;
         if (var2.get(PARAM_PREV_STATE) == PathFindState.instance()) {
            if (var1.getPathFindBehavior2().getTargetChar() == null) {
               var1.setVariable("bPathfind", true);
               var1.setVariable("bMoving", false);
            } else if (var3.isTargetLocationKnown()) {
               var1.pathToCharacter(var1.getPathFindBehavior2().getTargetChar());
            } else if (var3.LastTargetSeenX != -1) {
               var1.pathToLocation(var3.LastTargetSeenX, var3.LastTargetSeenY, var3.LastTargetSeenZ);
            }
         } else if (var2.get(PARAM_PREV_STATE) == WalkTowardState.instance() || var2.get(PARAM_PREV_STATE) == WalkTowardNetworkState.instance()) {
            var1.setVariable("bPathFind", false);
            var1.setVariable("bMoving", true);
         }
      }

      if (var1 instanceof IsoZombie) {
         ((IsoZombie)var1).networkAI.isClimbing = false;
      }

   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      if (var2.m_EventName.equalsIgnoreCase("CheckAttack")) {
         IsoZombie var4 = (IsoZombie)Type.tryCastTo(var1, IsoZombie.class);
         if (var4 != null && var4.target instanceof IsoGameCharacter) {
            ((IsoGameCharacter)var4.target).attackFromWindowsLunge(var4);
         }
      }

      if (var2.m_EventName.equalsIgnoreCase("ActiveAnimFinishing")) {
      }

      if (var2.m_EventName.equalsIgnoreCase("VaultSprintFallLanded")) {
         var1.dropHandItems();
         var1.fallenOnKnees();
      }

      if (var2.m_EventName.equalsIgnoreCase("FallenOnKnees")) {
         var1.fallenOnKnees();
      }

      IsoObject var9;
      if (var2.m_EventName.equalsIgnoreCase("OnFloor")) {
         var3.put(PARAM_ZOMBIE_ON_FLOOR, Boolean.parseBoolean(var2.m_ParameterValue));
         if (Boolean.parseBoolean(var2.m_ParameterValue)) {
            this.setLungeXVars((IsoZombie)var1);
            var9 = this.getFence(var1);
            if (this.countZombiesClimbingOver(var9) >= 2) {
               var9.Damage = (short)(var9.Damage - Rand.Next(7, 12) / (this.isMetalFence(var9) ? 2 : 1));
               if (var9.Damage <= 0) {
                  IsoDirections var5 = (IsoDirections)Type.tryCastTo(var3.get(PARAM_DIR), IsoDirections.class);
                  var9.destroyFence(var5);
               }
            }

            var1.setVariable("ClimbFenceFlopped", true);
         }
      }

      long var6;
      ParameterCharacterMovementSpeed var8;
      int var10;
      if (var2.m_EventName.equalsIgnoreCase("PlayFenceSound")) {
         var9 = this.getFence(var1);
         if (var9 == null) {
            return;
         }

         var10 = this.getFenceType(var9);
         var6 = var1.playSound(var2.m_ParameterValue);
         var8 = ((IsoPlayer)var1).getParameterCharacterMovementSpeed();
         var1.getEmitter().setParameterValue(var6, var8.getParameterDescription(), var8.calculateCurrentValue());
         var1.getEmitter().setParameterValue(var6, FMODManager.instance.getParameterDescription("FenceTypeLow"), (float)var10);
      }

      if (var2.m_EventName.equalsIgnoreCase("PlayTripSound")) {
         var9 = this.getFence(var1);
         if (var9 == null) {
            return;
         }

         var10 = this.getTripType(var9);
         var6 = var1.playSound(var2.m_ParameterValue);
         var8 = ((IsoPlayer)var1).getParameterCharacterMovementSpeed();
         var1.getEmitter().setParameterValue(var6, var8.getParameterDescription(), var8.calculateCurrentValue());
         var1.getEmitter().setParameterValue(var6, FMODManager.instance.getParameterDescription("TripObstacleType"), (float)var10);
      }

      if (var2.m_EventName.equalsIgnoreCase("SetCollidable")) {
         var3.put(PARAM_COLLIDABLE, Boolean.parseBoolean(var2.m_ParameterValue));
      }

      if (var2.m_EventName.equalsIgnoreCase("VaultOverStarted")) {
         if (var1 instanceof IsoPlayer && !((IsoPlayer)var1).isLocalPlayer()) {
            return;
         }

         if (var1.isVariable("ClimbFenceOutcome", "fall")) {
            var1.reportEvent("EventFallClimb");
            var1.setVariable("BumpDone", true);
            var1.setFallOnFront(true);
         }
      }

   }

   public void getDeltaModifiers(IsoGameCharacter var1, MoveDeltaModifiers var2) {
      boolean var3 = var1.getPath2() != null;
      boolean var4 = var1 instanceof IsoPlayer;
      if (var3 && var4) {
         var2.turnDelta = Math.max(var2.turnDelta, 10.0F);
      }

   }

   public boolean isIgnoreCollide(IsoGameCharacter var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      HashMap var8 = var1.getStateMachineParams(this);
      int var9 = (Integer)var8.get(PARAM_START_X);
      int var10 = (Integer)var8.get(PARAM_START_Y);
      int var11 = (Integer)var8.get(PARAM_END_X);
      int var12 = (Integer)var8.get(PARAM_END_Y);
      int var13 = (Integer)var8.get(PARAM_Z);
      if (var13 == var4 && var13 == var7) {
         int var14 = PZMath.min(var9, var11);
         int var15 = PZMath.min(var10, var12);
         int var16 = PZMath.max(var9, var11);
         int var17 = PZMath.max(var10, var12);
         int var18 = PZMath.min(var2, var5);
         int var19 = PZMath.min(var3, var6);
         int var20 = PZMath.max(var2, var5);
         int var21 = PZMath.max(var3, var6);
         return var14 <= var18 && var15 <= var19 && var16 >= var20 && var17 >= var21;
      } else {
         return false;
      }
   }

   private void slideX(IsoGameCharacter var1, float var2) {
      float var3 = 0.05F * GameTime.getInstance().getMultiplier() / 1.6F;
      var3 = var2 > var1.x ? Math.min(var3, var2 - var1.x) : Math.max(-var3, var2 - var1.x);
      var1.x += var3;
      var1.nx = var1.x;
   }

   private void slideY(IsoGameCharacter var1, float var2) {
      float var3 = 0.05F * GameTime.getInstance().getMultiplier() / 1.6F;
      var3 = var2 > var1.y ? Math.min(var3, var2 - var1.y) : Math.max(-var3, var2 - var1.y);
      var1.y += var3;
      var1.ny = var1.y;
   }

   private IsoObject getFence(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      int var3 = (Integer)var2.get(PARAM_START_X);
      int var4 = (Integer)var2.get(PARAM_START_Y);
      int var5 = (Integer)var2.get(PARAM_Z);
      IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, var5);
      int var7 = (Integer)var2.get(PARAM_END_X);
      int var8 = (Integer)var2.get(PARAM_END_Y);
      IsoGridSquare var9 = IsoWorld.instance.CurrentCell.getGridSquare(var7, var8, var5);
      return var6 != null && var9 != null ? var6.getHoppableTo(var9) : null;
   }

   private int getFenceType(IsoObject var1) {
      if (var1.getSprite() == null) {
         return 0;
      } else {
         PropertyContainer var2 = var1.getSprite().getProperties();
         String var3 = var2.Val("FenceTypeLow");
         if (var3 != null) {
            if ("Sandbag".equals(var3) && var1.getName() != null && StringUtils.containsIgnoreCase(var1.getName(), "Gravel")) {
               var3 = "Gravelbag";
            }

            byte var5 = -1;
            switch(var3.hashCode()) {
            case -1687213100:
               if (var3.equals("Barbwire")) {
                  var5 = 4;
               }
               break;
            case -764914460:
               if (var3.equals("Sandbag")) {
                  var5 = 2;
               }
               break;
            case 2702029:
               if (var3.equals("Wood")) {
                  var5 = 0;
               }
               break;
            case 74234599:
               if (var3.equals("Metal")) {
                  var5 = 1;
               }
               break;
            case 1000008577:
               if (var3.equals("Gravelbag")) {
                  var5 = 3;
               }
            }

            byte var10000;
            switch(var5) {
            case 0:
               var10000 = 0;
               break;
            case 1:
               var10000 = 1;
               break;
            case 2:
               var10000 = 2;
               break;
            case 3:
               var10000 = 3;
               break;
            case 4:
               var10000 = 4;
               break;
            default:
               var10000 = 0;
            }

            return var10000;
         } else {
            return 0;
         }
      }
   }

   private int getTripType(IsoObject var1) {
      if (var1.getSprite() == null) {
         return 0;
      } else {
         PropertyContainer var2 = var1.getSprite().getProperties();
         String var3 = var2.Val("FenceTypeLow");
         if (var3 != null) {
            if ("Sandbag".equals(var3) && var1.getName() != null && StringUtils.containsIgnoreCase(var1.getName(), "Gravel")) {
               var3 = "Gravelbag";
            }

            byte var5 = -1;
            switch(var3.hashCode()) {
            case -1687213100:
               if (var3.equals("Barbwire")) {
                  var5 = 4;
               }
               break;
            case -764914460:
               if (var3.equals("Sandbag")) {
                  var5 = 2;
               }
               break;
            case 2702029:
               if (var3.equals("Wood")) {
                  var5 = 0;
               }
               break;
            case 74234599:
               if (var3.equals("Metal")) {
                  var5 = 1;
               }
               break;
            case 1000008577:
               if (var3.equals("Gravelbag")) {
                  var5 = 3;
               }
            }

            byte var10000;
            switch(var5) {
            case 0:
               var10000 = 0;
               break;
            case 1:
               var10000 = 1;
               break;
            case 2:
               var10000 = 2;
               break;
            case 3:
               var10000 = 3;
               break;
            case 4:
               var10000 = 4;
               break;
            default:
               var10000 = 0;
            }

            return var10000;
         } else {
            return 0;
         }
      }
   }

   private boolean shouldFallAfterVaultOver(IsoGameCharacter var1) {
      if (var1 instanceof IsoPlayer && !((IsoPlayer)var1).isLocalPlayer()) {
         return ((IsoPlayer)var1).networkAI.climbFenceOutcomeFall;
      } else if (DebugOptions.instance.Character.Debug.AlwaysTripOverFence.getValue()) {
         return true;
      } else {
         float var2 = 0.0F;
         if (var1.getVariableBoolean("VaultOverSprint")) {
            var2 = 10.0F;
         }

         if (var1.getMoodles() != null) {
            var2 += (float)(var1.getMoodles().getMoodleLevel(MoodleType.Endurance) * 10);
            var2 += (float)(var1.getMoodles().getMoodleLevel(MoodleType.HeavyLoad) * 13);
            var2 += (float)(var1.getMoodles().getMoodleLevel(MoodleType.Pain) * 5);
         }

         BodyPart var3 = var1.getBodyDamage().getBodyPart(BodyPartType.Torso_Lower);
         if (var3.getAdditionalPain(true) > 20.0F) {
            var2 += (var3.getAdditionalPain(true) - 20.0F) / 10.0F;
         }

         if (var1.Traits.Clumsy.isSet()) {
            var2 += 10.0F;
         }

         if (var1.Traits.Graceful.isSet()) {
            var2 -= 10.0F;
         }

         if (var1.Traits.VeryUnderweight.isSet()) {
            var2 += 20.0F;
         }

         if (var1.Traits.Underweight.isSet()) {
            var2 += 10.0F;
         }

         if (var1.Traits.Obese.isSet()) {
            var2 += 20.0F;
         }

         if (var1.Traits.Overweight.isSet()) {
            var2 += 10.0F;
         }

         var2 -= (float)var1.getPerkLevel(PerkFactory.Perks.Fitness);
         return (float)Rand.Next(100) < var2;
      }
   }

   private int countZombiesClimbingOver(IsoObject var1) {
      if (var1 != null && var1.getSquare() != null) {
         byte var2 = 0;
         IsoGridSquare var3 = var1.getSquare();
         int var4 = var2 + this.countZombiesClimbingOver(var1, var3);
         if (var1.getProperties().Is(IsoFlagType.HoppableN)) {
            var3 = var3.getAdjacentSquare(IsoDirections.N);
         } else {
            var3 = var3.getAdjacentSquare(IsoDirections.W);
         }

         var4 += this.countZombiesClimbingOver(var1, var3);
         return var4;
      } else {
         return 0;
      }
   }

   private int countZombiesClimbingOver(IsoObject var1, IsoGridSquare var2) {
      if (var2 == null) {
         return 0;
      } else {
         int var3 = 0;

         for(int var4 = 0; var4 < var2.getMovingObjects().size(); ++var4) {
            IsoZombie var5 = (IsoZombie)Type.tryCastTo((IsoMovingObject)var2.getMovingObjects().get(var4), IsoZombie.class);
            if (var5 != null && var5.target != null && var5.isCurrentState(this) && this.getFence(var5) == var1) {
               ++var3;
            }
         }

         return var3;
      }
   }

   private boolean isMetalFence(IsoObject var1) {
      if (var1 != null && var1.getProperties() != null) {
         PropertyContainer var2 = var1.getProperties();
         String var3 = var2.Val("Material");
         String var4 = var2.Val("Material2");
         String var5 = var2.Val("Material3");
         if (!"MetalBars".equals(var3) && !"MetalBars".equals(var4) && !"MetalBars".equals(var5)) {
            if (!"MetalWire".equals(var3) && !"MetalWire".equals(var4) && !"MetalWire".equals(var5)) {
               if (var1 instanceof IsoThumpable && var1.hasModData()) {
                  KahluaTableIterator var6 = var1.getModData().iterator();

                  while(var6.advance()) {
                     String var7 = (String)Type.tryCastTo(var6.getKey(), String.class);
                     if (var7 != null && var7.contains("MetalPipe")) {
                        return true;
                     }
                  }
               }

               return false;
            } else {
               return true;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public void setParams(IsoGameCharacter var1, IsoDirections var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      int var4 = var1.getSquare().getX();
      int var5 = var1.getSquare().getY();
      int var6 = var1.getSquare().getZ();
      int var9 = var4;
      int var10 = var5;
      switch(var2) {
      case N:
         var10 = var5 - 1;
         break;
      case S:
         var10 = var5 + 1;
         break;
      case W:
         var9 = var4 - 1;
         break;
      case E:
         var9 = var4 + 1;
         break;
      default:
         throw new IllegalArgumentException("invalid direction");
      }

      IsoGridSquare var11 = IsoWorld.instance.CurrentCell.getGridSquare(var9, var10, var6);
      boolean var12 = false;
      boolean var13 = var11 != null && var11.Is(IsoFlagType.solidtrans);
      boolean var14 = var11 != null && var11.TreatAsSolidFloor();
      boolean var15 = var11 != null && var1.canClimbDownSheetRope(var11);
      var3.put(PARAM_START_X, var4);
      var3.put(PARAM_START_Y, var5);
      var3.put(PARAM_Z, var6);
      var3.put(PARAM_END_X, var9);
      var3.put(PARAM_END_Y, var10);
      var3.put(PARAM_DIR, var2);
      var3.put(PARAM_ZOMBIE_ON_FLOOR, Boolean.FALSE);
      var3.put(PARAM_PREV_STATE, var1.getCurrentState());
      var3.put(PARAM_SCRATCH, var12 ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_COUNTER, var13 ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_SOLID_FLOOR, var14 ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_SHEET_ROPE, var15 ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_RUN, var1.isRunning() ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_SPRINT, var1.isSprinting() ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_COLLIDABLE, Boolean.FALSE);
   }
}
