package zombie.ai.states;

import zombie.GameTime;
import zombie.ai.State;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.skills.PerkFactory;
import zombie.iso.IsoCell;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;

public final class ClimbSheetRopeState extends State {
   public static final float CLIMB_SPEED = 0.16F;
   private static final float CLIMB_SLOWDOWN = 0.5F;
   private static final ClimbSheetRopeState _instance = new ClimbSheetRopeState();

   public static ClimbSheetRopeState instance() {
      return _instance;
   }

   public void enter(IsoGameCharacter var1) {
      var1.setIgnoreMovement(true);
      var1.setbClimbing(true);
      var1.setVariable("ClimbRope", true);
   }

   public void execute(IsoGameCharacter var1) {
      var1.getStateMachineParams(this);
      float var3 = 0.0F;
      float var4 = 0.0F;
      if (var1.getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetN) || var1.getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetTopN)) {
         var1.setDir(IsoDirections.N);
         var3 = 0.54F;
         var4 = 0.39F;
      }

      if (var1.getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetS) || var1.getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetTopS)) {
         var1.setDir(IsoDirections.S);
         var3 = 0.118F;
         var4 = 0.5756F;
      }

      if (var1.getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetW) || var1.getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetTopW)) {
         var1.setDir(IsoDirections.W);
         var3 = 0.4F;
         var4 = 0.7F;
      }

      if (var1.getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetE) || var1.getCurrentSquare().getProperties().Is(IsoFlagType.climbSheetTopE)) {
         var1.setDir(IsoDirections.E);
         var3 = 0.5417F;
         var4 = 0.3144F;
      }

      float var5 = var1.x - (float)((int)var1.x);
      float var6 = var1.y - (float)((int)var1.y);
      float var7;
      if (var5 != var3) {
         var7 = (var3 - var5) / 4.0F;
         var5 += var7;
         var1.x = (float)((int)var1.x) + var5;
      }

      if (var6 != var4) {
         var7 = (var4 - var6) / 4.0F;
         var6 += var7;
         var1.y = (float)((int)var1.y) + var6;
      }

      var1.nx = var1.x;
      var1.ny = var1.y;
      var7 = this.getClimbSheetRopeSpeed(var1);
      var1.getSpriteDef().AnimFrameIncrease = var7;
      float var8 = var1.z + var7 / 10.0F * GameTime.instance.getMultiplier();
      var8 = Math.min(var8, 7.0F);

      for(int var9 = (int)var1.z; (float)var9 <= var8; ++var9) {
         IsoCell var10 = IsoWorld.instance.getCell();
         IsoGridSquare var11 = var10.getGridSquare((double)var1.getX(), (double)var1.getY(), (double)var9);
         if (IsoWindow.isTopOfSheetRopeHere(var11)) {
            var1.z = (float)var9;
            var1.setCurrent(var11);
            var1.setCollidable(true);
            IsoGridSquare var12 = var11.nav[var1.dir.index()];
            if (var12 != null) {
               if (!var12.TreatAsSolidFloor()) {
                  var1.climbDownSheetRope();
                  return;
               }

               IsoWindow var13 = var11.getWindowTo(var12);
               if (var13 != null) {
                  if (!var13.open) {
                     var13.ToggleWindow(var1);
                  }

                  if (!var13.canClimbThrough(var1)) {
                     var1.climbDownSheetRope();
                     return;
                  }

                  var1.climbThroughWindow(var13, 4);
                  return;
               }

               IsoThumpable var14 = var11.getWindowThumpableTo(var12);
               if (var14 != null) {
                  if (!var14.canClimbThrough(var1)) {
                     var1.climbDownSheetRope();
                     return;
                  }

                  var1.climbThroughWindow(var14, 4);
                  return;
               }

               var14 = var11.getHoppableThumpableTo(var12);
               if (var14 != null) {
                  if (!IsoWindow.canClimbThroughHelper(var1, var11, var12, var1.dir == IsoDirections.N || var1.dir == IsoDirections.S)) {
                     var1.climbDownSheetRope();
                     return;
                  }

                  var1.climbOverFence(var1.dir);
                  return;
               }

               IsoObject var15 = var11.getWindowFrameTo(var12);
               if (var15 != null) {
                  if (!IsoWindowFrame.canClimbThrough(var15, var1)) {
                     var1.climbDownSheetRope();
                     return;
                  }

                  var1.climbThroughWindowFrame(var15);
                  return;
               }

               IsoObject var16 = var11.getWallHoppableTo(var12);
               if (var16 != null) {
                  if (!IsoWindow.canClimbThroughHelper(var1, var11, var12, var1.dir == IsoDirections.N || var1.dir == IsoDirections.S)) {
                     var1.climbDownSheetRope();
                     return;
                  }

                  var1.climbOverFence(var1.dir);
                  return;
               }
            }

            return;
         }
      }

      var1.z = var8;
      if (var1.z >= 7.0F) {
         var1.setCollidable(true);
         var1.clearVariable("ClimbRope");
      }

      if (!IsoWindow.isSheetRopeHere(var1.getCurrentSquare())) {
         var1.setCollidable(true);
         var1.setbClimbing(false);
         var1.setbFalling(true);
         var1.clearVariable("ClimbRope");
      }

      if (var1 instanceof IsoPlayer && ((IsoPlayer)var1).isLocalPlayer()) {
         ((IsoPlayer)var1).dirtyRecalcGridStackTime = 2.0F;
      }

   }

   public void exit(IsoGameCharacter var1) {
      var1.setIgnoreMovement(false);
      var1.setbClimbing(false);
      var1.clearVariable("ClimbRope");
   }

   public float getClimbSheetRopeSpeed(IsoGameCharacter var1) {
      float var2 = 0.16F;
      switch(var1.getPerkLevel(PerkFactory.Perks.Strength)) {
      case 1:
         var2 -= 0.1F;
         break;
      case 2:
         var2 -= 0.1F;
      case 3:
      case 4:
      case 5:
      default:
         break;
      case 6:
         var2 += 0.05F;
         break;
      case 7:
         var2 += 0.05F;
         break;
      case 8:
         var2 += 0.1F;
         break;
      case 9:
         var2 += 0.1F;
      }

      var2 *= 0.5F;
      return var2;
   }
}
