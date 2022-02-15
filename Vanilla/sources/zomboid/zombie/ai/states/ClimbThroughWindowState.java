package zombie.ai.states;

import java.util.HashMap;
import zombie.GameTime;
import zombie.ai.State;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.MoveDeltaModifiers;
import zombie.characters.skills.PerkFactory;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.core.skinnedmodel.advancedanimation.AnimEvent;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;
import zombie.util.Type;

public final class ClimbThroughWindowState extends State {
   private static final ClimbThroughWindowState _instance = new ClimbThroughWindowState();
   static final Integer PARAM_START_X = 0;
   static final Integer PARAM_START_Y = 1;
   static final Integer PARAM_Z = 2;
   static final Integer PARAM_OPPOSITE_X = 3;
   static final Integer PARAM_OPPOSITE_Y = 4;
   static final Integer PARAM_DIR = 5;
   static final Integer PARAM_ZOMBIE_ON_FLOOR = 6;
   static final Integer PARAM_PREV_STATE = 7;
   static final Integer PARAM_SCRATCH = 8;
   static final Integer PARAM_COUNTER = 9;
   static final Integer PARAM_SOLID_FLOOR = 10;
   static final Integer PARAM_SHEET_ROPE = 11;
   static final Integer PARAM_END_X = 12;
   static final Integer PARAM_END_Y = 13;

   public static ClimbThroughWindowState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setIgnoreMovement(true);
      var1.setHideWeaponModel(true);
      HashMap var2 = var1.getStateMachineParams(this);
      boolean var3 = var2.get(PARAM_COUNTER) == Boolean.TRUE;
      var1.setVariable("ClimbWindowStarted", false);
      var1.setVariable("ClimbWindowEnd", false);
      var1.setVariable("ClimbWindowFinished", false);
      var1.clearVariable("ClimbWindowGetUpBack");
      var1.clearVariable("ClimbWindowGetUpFront");
      var1.setVariable("ClimbWindowOutcome", var3 ? "obstacle" : "success");
      var1.clearVariable("ClimbWindowFlopped");
      IsoZombie var4 = (IsoZombie)Type.tryCastTo(var1, IsoZombie.class);
      if (!var3 && var4 != null && var4.shouldDoFenceLunge()) {
         this.setLungeXVars(var4);
         var1.setVariable("ClimbWindowOutcome", "lunge");
      }

      if (var2.get(PARAM_SOLID_FLOOR) == Boolean.FALSE) {
         var1.setVariable("ClimbWindowOutcome", "fall");
      }

      if (!(var1 instanceof IsoZombie) && var2.get(PARAM_SHEET_ROPE) == Boolean.TRUE) {
         var1.setVariable("ClimbWindowOutcome", "rope");
      }

      if (var1 instanceof IsoPlayer && ((IsoPlayer)var1).isLocalPlayer()) {
         ((IsoPlayer)var1).dirtyRecalcGridStackTime = 20.0F;
      }

   }

   public void execute(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      if (!this.isWindowClosing(var1)) {
         IsoDirections var3 = (IsoDirections)var2.get(PARAM_DIR);
         var1.setDir(var3);
         String var4 = var1.getVariableString("ClimbWindowOutcome");
         int var7;
         int var8;
         if (var1 instanceof IsoZombie) {
            boolean var5 = var2.get(PARAM_ZOMBIE_ON_FLOOR) == Boolean.TRUE;
            if (!var1.isFallOnFront() && var5) {
               int var6 = (Integer)var2.get(PARAM_OPPOSITE_X);
               var7 = (Integer)var2.get(PARAM_OPPOSITE_Y);
               var8 = (Integer)var2.get(PARAM_Z);
               IsoGridSquare var9 = IsoWorld.instance.CurrentCell.getGridSquare(var6, var7, var8);
               if (var9 != null && var9.getBrokenGlass() != null) {
                  var1.addBlood(BloodBodyPartType.Head, true, true, true);
                  var1.addBlood(BloodBodyPartType.Head, true, true, true);
                  var1.addBlood(BloodBodyPartType.Head, true, true, true);
                  var1.addBlood(BloodBodyPartType.Head, true, true, true);
                  var1.addBlood(BloodBodyPartType.Head, true, true, true);
                  var1.addBlood(BloodBodyPartType.Neck, true, true, true);
                  var1.addBlood(BloodBodyPartType.Neck, true, true, true);
                  var1.addBlood(BloodBodyPartType.Neck, true, true, true);
                  var1.addBlood(BloodBodyPartType.Neck, true, true, true);
                  var1.addBlood(BloodBodyPartType.Torso_Upper, true, true, true);
                  var1.addBlood(BloodBodyPartType.Torso_Upper, true, true, true);
                  var1.addBlood(BloodBodyPartType.Torso_Upper, true, true, true);
               }
            }

            var1.setOnFloor(var5);
            ((IsoZombie)var1).setKnockedDown(var5);
            var1.setFallOnFront(var5);
         }

         float var11 = (float)(Integer)var2.get(PARAM_START_X) + 0.5F;
         float var12 = (float)(Integer)var2.get(PARAM_START_Y) + 0.5F;
         if (!var1.getVariableBoolean("ClimbWindowStarted")) {
            if (var1.x != var11 && (var3 == IsoDirections.N || var3 == IsoDirections.S)) {
               this.slideX(var1, var11);
            }

            if (var1.y != var12 && (var3 == IsoDirections.W || var3 == IsoDirections.E)) {
               this.slideY(var1, var12);
            }
         }

         float var13;
         float var14;
         if (var1 instanceof IsoPlayer && var4.equalsIgnoreCase("obstacle")) {
            var13 = (float)(Integer)var2.get(PARAM_END_X) + 0.5F;
            var14 = (float)(Integer)var2.get(PARAM_END_Y) + 0.5F;
            if (var1.DistToSquared(var13, var14) < 0.5625F) {
               var1.setVariable("ClimbWindowOutcome", "obstacleEnd");
            }
         }

         if (var1 instanceof IsoPlayer && !var1.getVariableBoolean("ClimbWindowEnd") && !"fallfront".equals(var4) && !"back".equals(var4) && !"fallback".equals(var4)) {
            var7 = (Integer)var2.get(PARAM_OPPOSITE_X);
            var8 = (Integer)var2.get(PARAM_OPPOSITE_Y);
            int var15 = (Integer)var2.get(PARAM_Z);
            IsoGridSquare var10 = IsoWorld.instance.CurrentCell.getGridSquare(var7, var8, var15);
            if (var10 != null) {
               this.checkForFallingBack(var10, var1);
               if (var10 != var1.getSquare() && var10.TreatAsSolidFloor()) {
                  this.checkForFallingFront(var1.getSquare(), var1);
               }
            }
         }

         if (var1.getVariableBoolean("ClimbWindowStarted") && !"back".equals(var4) && !"fallback".equals(var4) && !"lunge".equals(var4) && !"obstacle".equals(var4) && !"obstacleEnd".equals(var4)) {
            var13 = (float)(Integer)var2.get(PARAM_START_X);
            var14 = (float)(Integer)var2.get(PARAM_START_Y);
            switch(var3) {
            case N:
               var14 -= 0.1F;
               break;
            case S:
               ++var14;
               break;
            case W:
               var13 -= 0.1F;
               break;
            case E:
               ++var13;
            }

            if ((int)var1.x != (int)var13 && (var3 == IsoDirections.W || var3 == IsoDirections.E)) {
               this.slideX(var1, var13);
            }

            if ((int)var1.y != (int)var14 && (var3 == IsoDirections.N || var3 == IsoDirections.S)) {
               this.slideY(var1, var14);
            }
         }

         if (var1.getVariableBoolean("ClimbWindowStarted") && var2.get(PARAM_SCRATCH) == Boolean.TRUE) {
            var2.put(PARAM_SCRATCH, Boolean.FALSE);
            var1.getBodyDamage().setScratchedWindow();
         }

      }
   }

   private void checkForFallingBack(IsoGridSquare var1, IsoGameCharacter var2) {
      for(int var3 = 0; var3 < var1.getMovingObjects().size(); ++var3) {
         IsoMovingObject var4 = (IsoMovingObject)var1.getMovingObjects().get(var3);
         IsoZombie var5 = (IsoZombie)Type.tryCastTo(var4, IsoZombie.class);
         if (var5 != null && !var5.isOnFloor() && !var5.isSitAgainstWall()) {
            if (!var5.isVariable("AttackOutcome", "success") && Rand.Next(5 + var2.getPerkLevel(PerkFactory.Perks.Fitness)) != 0) {
               var5.playHurtSound();
               var2.setVariable("ClimbWindowOutcome", "back");
            } else {
               var5.playHurtSound();
               var2.setVariable("ClimbWindowOutcome", "fallback");
            }
         }
      }

   }

   private void checkForFallingFront(IsoGridSquare var1, IsoGameCharacter var2) {
      for(int var3 = 0; var3 < var1.getMovingObjects().size(); ++var3) {
         IsoMovingObject var4 = (IsoMovingObject)var1.getMovingObjects().get(var3);
         IsoZombie var5 = (IsoZombie)Type.tryCastTo(var4, IsoZombie.class);
         if (var5 != null && !var5.isOnFloor() && !var5.isSitAgainstWall() && var5.isVariable("AttackOutcome", "success")) {
            var5.playHurtSound();
            var2.setVariable("ClimbWindowOutcome", "fallfront");
         }
      }

   }

   public void exit(IsoGameCharacter var1) {
      var1.setIgnoreMovement(false);
      var1.setHideWeaponModel(false);
      HashMap var2 = var1.getStateMachineParams(this);
      if (var1.isVariable("ClimbWindowOutcome", "fall") || var1.isVariable("ClimbWindowOutcome", "fallback") || var1.isVariable("ClimbWindowOutcome", "fallfront")) {
         var1.setHitReaction("");
      }

      var1.clearVariable("ClimbWindowFinished");
      var1.clearVariable("ClimbWindowOutcome");
      var1.clearVariable("ClimbWindowStarted");
      var1.clearVariable("ClimbWindowFlopped");
      if (var1 instanceof IsoZombie) {
         var1.setOnFloor(false);
         ((IsoZombie)var1).setKnockedDown(false);
      }

      IsoZombie var3 = (IsoZombie)Type.tryCastTo(var1, IsoZombie.class);
      if (var3 != null) {
         var3.AllowRepathDelay = 0.0F;
         if (var2.get(PARAM_PREV_STATE) == PathFindState.instance()) {
            if (var1.getPathFindBehavior2().getTargetChar() == null) {
               var1.setVariable("bPathFind", true);
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

   public void slideX(IsoGameCharacter var1, float var2) {
      float var3 = 0.05F * GameTime.getInstance().getMultiplier() / 1.6F;
      var3 = var2 > var1.x ? Math.min(var3, var2 - var1.x) : Math.max(-var3, var2 - var1.x);
      var1.x += var3;
      var1.nx = var1.x;
   }

   public void slideY(IsoGameCharacter var1, float var2) {
      float var3 = 0.05F * GameTime.getInstance().getMultiplier() / 1.6F;
      var3 = var2 > var1.y ? Math.min(var3, var2 - var1.y) : Math.max(-var3, var2 - var1.y);
      var1.y += var3;
      var1.ny = var1.y;
   }

   public void animEvent(IsoGameCharacter var1, AnimEvent var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      IsoZombie var4 = (IsoZombie)Type.tryCastTo(var1, IsoZombie.class);
      if (var2.m_EventName.equalsIgnoreCase("CheckAttack") && var4 != null && var4.target instanceof IsoGameCharacter) {
         ((IsoGameCharacter)var4.target).attackFromWindowsLunge(var4);
      }

      if (var2.m_EventName.equalsIgnoreCase("OnFloor") && var4 != null) {
         boolean var5 = Boolean.parseBoolean(var2.m_ParameterValue);
         var3.put(PARAM_ZOMBIE_ON_FLOOR, var5);
         if (var5) {
            this.setLungeXVars(var4);
            IsoThumpable var6 = (IsoThumpable)Type.tryCastTo(this.getWindow(var1), IsoThumpable.class);
            if (var6 != null && var6.getSquare() != null && var4.target != null) {
               var6.Health -= Rand.Next(10, 20);
               if (var6.Health <= 0) {
                  var6.destroy();
               }
            }

            var1.setVariable("ClimbWindowFlopped", true);
         }
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

   public IsoObject getWindow(IsoGameCharacter var1) {
      if (!var1.isCurrentState(this)) {
         return null;
      } else {
         HashMap var2 = var1.getStateMachineParams(this);
         int var3 = (Integer)var2.get(PARAM_START_X);
         int var4 = (Integer)var2.get(PARAM_START_Y);
         int var5 = (Integer)var2.get(PARAM_Z);
         IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, var5);
         int var7 = (Integer)var2.get(PARAM_END_X);
         int var8 = (Integer)var2.get(PARAM_END_Y);
         IsoGridSquare var9 = IsoWorld.instance.CurrentCell.getGridSquare(var7, var8, var5);
         if (var6 != null && var9 != null) {
            Object var10 = var6.getWindowTo(var9);
            if (var10 == null) {
               var10 = var6.getWindowThumpableTo(var9);
            }

            if (var10 == null) {
               var10 = var6.getHoppableTo(var9);
            }

            return (IsoObject)var10;
         } else {
            return null;
         }
      }
   }

   public boolean isWindowClosing(IsoGameCharacter var1) {
      HashMap var2 = var1.getStateMachineParams(this);
      if (var1.getVariableBoolean("ClimbWindowStarted")) {
         return false;
      } else {
         int var3 = (Integer)var2.get(PARAM_START_X);
         int var4 = (Integer)var2.get(PARAM_START_Y);
         int var5 = (Integer)var2.get(PARAM_Z);
         IsoGridSquare var6 = IsoWorld.instance.CurrentCell.getGridSquare(var3, var4, var5);
         if (var1.getCurrentSquare() != var6) {
            return false;
         } else {
            IsoWindow var7 = (IsoWindow)Type.tryCastTo(this.getWindow(var1), IsoWindow.class);
            if (var7 == null) {
               return false;
            } else {
               IsoGameCharacter var8 = var7.getFirstCharacterClosing();
               if (var8 != null && var8.isVariable("CloseWindowOutcome", "success")) {
                  if (var1.isZombie()) {
                     var1.setHitReaction("HeadLeft");
                  } else {
                     var1.setVariable("ClimbWindowFinished", true);
                  }

                  return true;
               } else {
                  return false;
               }
            }
         }
      }
   }

   public void getDeltaModifiers(IsoGameCharacter var1, MoveDeltaModifiers var2) {
      boolean var3 = var1.getPath2() != null;
      boolean var4 = var1 instanceof IsoPlayer;
      if (var3 && var4) {
         var2.turnDelta = Math.max(var2.turnDelta, 10.0F);
      }

      if (var4 && var1.getVariableBoolean("isTurning")) {
         var2.turnDelta = Math.max(var2.turnDelta, 5.0F);
      }

   }

   private boolean isFreeSquare(IsoGridSquare var1) {
      return var1 != null && var1.TreatAsSolidFloor() && !var1.Is(IsoFlagType.solid) && !var1.Is(IsoFlagType.solidtrans);
   }

   private boolean isObstacleSquare(IsoGridSquare var1) {
      return var1 != null && var1.TreatAsSolidFloor() && !var1.Is(IsoFlagType.solid) && var1.Is(IsoFlagType.solidtrans) && !var1.Is(IsoFlagType.water);
   }

   private IsoGridSquare getFreeSquareAfterObstacles(IsoGridSquare var1, IsoDirections var2) {
      while(true) {
         IsoGridSquare var3 = var1.getAdjacentSquare(var2);
         if (var3 == null || var1.isSomethingTo(var3) || var1.getWindowFrameTo(var3) != null || var1.getWindowThumpableTo(var3) != null) {
            return null;
         }

         if (this.isFreeSquare(var3)) {
            return var3;
         }

         if (!this.isObstacleSquare(var3)) {
            return null;
         }

         var1 = var3;
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

   public boolean isPastInnerEdgeOfSquare(IsoGameCharacter var1, int var2, int var3, IsoDirections var4) {
      if (var4 == IsoDirections.N) {
         return var1.y < (float)(var3 + 1) - 0.3F;
      } else if (var4 == IsoDirections.S) {
         return var1.y > (float)var3 + 0.3F;
      } else if (var4 == IsoDirections.W) {
         return var1.x < (float)(var2 + 1) - 0.3F;
      } else if (var4 == IsoDirections.E) {
         return var1.x > (float)var2 + 0.3F;
      } else {
         throw new IllegalArgumentException("unhandled direction");
      }
   }

   public boolean isPastOuterEdgeOfSquare(IsoGameCharacter var1, int var2, int var3, IsoDirections var4) {
      if (var4 == IsoDirections.N) {
         return var1.y < (float)var3 - 0.3F;
      } else if (var4 == IsoDirections.S) {
         return var1.y > (float)(var3 + 1) + 0.3F;
      } else if (var4 == IsoDirections.W) {
         return var1.x < (float)var2 - 0.3F;
      } else if (var4 == IsoDirections.E) {
         return var1.x > (float)(var2 + 1) + 0.3F;
      } else {
         throw new IllegalArgumentException("unhandled direction");
      }
   }

   public void setParams(IsoGameCharacter var1, IsoObject var2) {
      HashMap var3 = var1.getStateMachineParams(this);
      var3.clear();
      boolean var5 = false;
      boolean var4;
      if (var2 instanceof IsoWindow) {
         IsoWindow var6 = (IsoWindow)var2;
         var4 = var6.north;
         if (var1 instanceof IsoPlayer && var6.isDestroyed() && !var6.isGlassRemoved() && Rand.Next(2) == 0) {
            var5 = true;
         }
      } else if (var2 instanceof IsoThumpable) {
         IsoThumpable var21 = (IsoThumpable)var2;
         var4 = var21.north;
         if (var1 instanceof IsoPlayer && var21.getName().equals("Barbed Fence") && Rand.Next(101) > 75) {
            var5 = true;
         }
      } else {
         if (!IsoWindowFrame.isWindowFrame(var2)) {
            throw new IllegalArgumentException("expected thumpable, window, or window-frame");
         }

         var4 = IsoWindowFrame.isWindowFrame(var2, true);
      }

      int var7 = var2.getSquare().getX();
      int var8 = var2.getSquare().getY();
      int var9 = var2.getSquare().getZ();
      int var10 = var7;
      int var11 = var8;
      int var12 = var7;
      int var13 = var8;
      IsoDirections var22;
      if (var4) {
         if ((float)var8 < var1.getY()) {
            var13 = var8 - 1;
            var22 = IsoDirections.N;
         } else {
            var11 = var8 - 1;
            var22 = IsoDirections.S;
         }
      } else if ((float)var7 < var1.getX()) {
         var12 = var7 - 1;
         var22 = IsoDirections.W;
      } else {
         var10 = var7 - 1;
         var22 = IsoDirections.E;
      }

      IsoGridSquare var14 = IsoWorld.instance.CurrentCell.getGridSquare(var12, var13, var9);
      boolean var15 = var14 != null && var14.Is(IsoFlagType.solidtrans);
      boolean var16 = var14 != null && var14.TreatAsSolidFloor();
      boolean var17 = var14 != null && var1.canClimbDownSheetRope(var14);
      int var18 = var12;
      int var19 = var13;
      IsoGridSquare var20;
      if (var15 && var1.isZombie()) {
         var20 = var14.getAdjacentSquare(var22);
         if (this.isFreeSquare(var20) && !var14.isSomethingTo(var20) && var14.getWindowFrameTo(var20) == null && var14.getWindowThumpableTo(var20) == null) {
            var18 = var20.x;
            var19 = var20.y;
         } else {
            var15 = false;
         }
      }

      if (var15 && !var1.isZombie()) {
         var20 = this.getFreeSquareAfterObstacles(var14, var22);
         if (var20 == null) {
            var15 = false;
         } else {
            var18 = var20.x;
            var19 = var20.y;
         }
      }

      var3.put(PARAM_START_X, var10);
      var3.put(PARAM_START_Y, var11);
      var3.put(PARAM_Z, var9);
      var3.put(PARAM_OPPOSITE_X, var12);
      var3.put(PARAM_OPPOSITE_Y, var13);
      var3.put(PARAM_END_X, var18);
      var3.put(PARAM_END_Y, var19);
      var3.put(PARAM_DIR, var22);
      var3.put(PARAM_ZOMBIE_ON_FLOOR, Boolean.FALSE);
      var3.put(PARAM_PREV_STATE, var1.getCurrentState());
      var3.put(PARAM_SCRATCH, var5 ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_COUNTER, var15 ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_SOLID_FLOOR, var16 ? Boolean.TRUE : Boolean.FALSE);
      var3.put(PARAM_SHEET_ROPE, var17 ? Boolean.TRUE : Boolean.FALSE);
   }
}
