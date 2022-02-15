package zombie.characters;

import zombie.GameTime;
import zombie.ai.states.ClimbOverFenceState;
import zombie.ai.states.ClimbOverWallState;
import zombie.ai.states.ClimbThroughWindowState;
import zombie.ai.states.LungeState;
import zombie.ai.states.PathFindState;
import zombie.ai.states.ThumpState;
import zombie.ai.states.WalkTowardState;
import zombie.core.Core;
import zombie.core.math.PZMath;
import zombie.core.utils.UpdateTimer;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.iso.IsoDirections;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.Vector2;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.NetworkVariables;
import zombie.network.packets.ZombiePacket;
import zombie.popman.NetworkZombieSimulator;
import zombie.vehicles.PathFindBehavior2;

public class NetworkZombieAI extends NetworkCharacterAI {
   private final UpdateTimer timer;
   private final PathFindBehavior2 pfb2;
   public final IsoZombie zombie;
   public boolean usePathFind = false;
   public float targetX = 0.0F;
   public float targetY = 0.0F;
   public int targetZ = 0;
   public boolean isClimbing;
   private byte flags;
   private byte direction;
   public final NetworkZombieMind mindSync;
   public boolean DebugInterfaceActive = false;

   public NetworkZombieAI(IsoGameCharacter var1) {
      super(var1);
      this.zombie = (IsoZombie)var1;
      this.isClimbing = false;
      this.flags = 0;
      this.pfb2 = this.zombie.getPathFindBehavior2();
      this.timer = new UpdateTimer();
      this.mindSync = new NetworkZombieMind(this.zombie);
      var1.ulBeatenVehicle.Reset(400L);
   }

   public void reset() {
      super.reset();
      this.usePathFind = true;
      this.targetX = this.zombie.getX();
      this.targetY = this.zombie.getY();
      this.targetZ = (byte)((int)this.zombie.getZ());
      this.isClimbing = false;
      this.flags = 0;
      this.zombie.getHitDir().set(0.0F, 0.0F);
   }

   public void extraUpdate() {
      NetworkZombieSimulator.getInstance().addExtraUpdate(this.zombie);
   }

   private long getUpdateTime() {
      return this.timer.getTime();
   }

   public void setUpdateTimer(float var1) {
      this.timer.reset((long)PZMath.clamp((int)var1, 200, 3800));
   }

   private void setUsingExtrapolation(ZombiePacket var1, int var2) {
      if (this.zombie.isMoving()) {
         Vector2 var3 = this.zombie.dir.ToVector();
         this.zombie.networkCharacter.checkReset(var2);
         NetworkCharacter.Transform var4 = this.zombie.networkCharacter.predict(500, var2, this.zombie.x, this.zombie.y, var3.x, var3.y);
         var1.x = var4.position.x;
         var1.y = var4.position.y;
         var1.z = (byte)((int)this.zombie.z);
         var1.moveType = NetworkVariables.PredictionTypes.Moving;
         this.setUpdateTimer(300.0F);
      } else {
         var1.x = this.zombie.x;
         var1.y = this.zombie.y;
         var1.z = (byte)((int)this.zombie.z);
         var1.moveType = NetworkVariables.PredictionTypes.Static;
         this.setUpdateTimer(2280.0F);
      }

   }

   private void setUsingThump(ZombiePacket var1) {
      var1.x = ((IsoObject)this.zombie.getThumpTarget()).getX();
      var1.y = ((IsoObject)this.zombie.getThumpTarget()).getY();
      var1.z = (byte)((int)((IsoObject)this.zombie.getThumpTarget()).getZ());
      var1.moveType = NetworkVariables.PredictionTypes.Thump;
      this.setUpdateTimer(2280.0F);
   }

   private void setUsingClimb(ZombiePacket var1) {
      var1.x = this.zombie.getTarget().getX();
      var1.y = this.zombie.getTarget().getY();
      var1.z = (byte)((int)this.zombie.getTarget().getZ());
      var1.moveType = NetworkVariables.PredictionTypes.Climb;
      this.setUpdateTimer(2280.0F);
   }

   private void setUsingLungeState(ZombiePacket var1, long var2) {
      if (this.zombie.target == null) {
         this.setUsingExtrapolation(var1, (int)var2);
      } else {
         float var4 = IsoUtils.DistanceTo(this.zombie.target.x, this.zombie.target.y, this.zombie.x, this.zombie.y);
         float var5;
         if (var4 > 5.0F) {
            var1.x = (this.zombie.x + this.zombie.target.x) * 0.5F;
            var1.y = (this.zombie.y + this.zombie.target.y) * 0.5F;
            var1.z = (byte)((int)this.zombie.target.z);
            var5 = var4 * 0.5F / 5.0E-4F * this.zombie.speedMod;
            var1.moveType = NetworkVariables.PredictionTypes.LungeHalf;
            this.setUpdateTimer(var5 * 0.6F);
         } else {
            var1.x = this.zombie.target.x;
            var1.y = this.zombie.target.y;
            var1.z = (byte)((int)this.zombie.target.z);
            var5 = var4 / 5.0E-4F * this.zombie.speedMod;
            var1.moveType = NetworkVariables.PredictionTypes.Lunge;
            this.setUpdateTimer(var5 * 0.6F);
         }

      }
   }

   private void setUsingWalkTowardState(ZombiePacket var1) {
      float var2;
      if (this.zombie.getPath2() == null) {
         float var3 = this.pfb2.getPathLength();
         if (var3 > 5.0F) {
            var1.x = (this.zombie.x + this.pfb2.getTargetX()) * 0.5F;
            var1.y = (this.zombie.y + this.pfb2.getTargetY()) * 0.5F;
            var1.z = (byte)((int)this.pfb2.getTargetZ());
            var2 = var3 * 0.5F / 5.0E-4F * this.zombie.speedMod;
            var1.moveType = NetworkVariables.PredictionTypes.WalkHalf;
         } else {
            var1.x = this.pfb2.getTargetX();
            var1.y = this.pfb2.getTargetY();
            var1.z = (byte)((int)this.pfb2.getTargetZ());
            var2 = var3 / 5.0E-4F * this.zombie.speedMod;
            var1.moveType = NetworkVariables.PredictionTypes.Walk;
         }
      } else {
         var1.x = this.pfb2.pathNextX;
         var1.y = this.pfb2.pathNextY;
         var1.z = (byte)((int)this.zombie.z);
         var2 = IsoUtils.DistanceTo(this.zombie.x, this.zombie.y, this.pfb2.pathNextX, this.pfb2.pathNextY) / 5.0E-4F * this.zombie.speedMod;
         var1.moveType = NetworkVariables.PredictionTypes.Walk;
      }

      this.setUpdateTimer(var2 * 0.6F);
   }

   private void setUsingPathFindState(ZombiePacket var1) {
      var1.x = this.pfb2.pathNextX;
      var1.y = this.pfb2.pathNextY;
      var1.z = (byte)((int)this.zombie.z);
      float var2 = IsoUtils.DistanceTo(this.zombie.x, this.zombie.y, this.pfb2.pathNextX, this.pfb2.pathNextY) / 5.0E-4F * this.zombie.speedMod;
      var1.moveType = NetworkVariables.PredictionTypes.PathFind;
      this.setUpdateTimer(var2 * 0.6F);
   }

   public void set(ZombiePacket var1) {
      int var2 = (int)(GameTime.getServerTime() / 1000000L);
      var1.booleanVariables = NetworkZombieVariables.getBooleanVariables(this.zombie);
      var1.realHealth = (short)NetworkZombieVariables.getInt(this.zombie, (short)0);
      var1.target = (short)NetworkZombieVariables.getInt(this.zombie, (short)1);
      var1.speedMod = (short)NetworkZombieVariables.getInt(this.zombie, (short)2);
      var1.timeSinceSeenFlesh = NetworkZombieVariables.getInt(this.zombie, (short)3);
      var1.smParamTargetAngle = NetworkZombieVariables.getInt(this.zombie, (short)4);
      var1.walkType = NetworkVariables.WalkType.fromString(this.zombie.getVariableString("zombieWalkType"));
      var1.realX = this.zombie.x;
      var1.realY = this.zombie.y;
      var1.realZ = (byte)((int)this.zombie.z);
      this.zombie.realState = NetworkVariables.ZombieState.fromString(this.zombie.getAdvancedAnimator().getCurrentStateName());
      var1.realState = this.zombie.realState;
      if (this.zombie.getThumpTarget() != null && this.zombie.getCurrentState() == ThumpState.instance()) {
         this.setUsingThump(var1);
      } else if (this.zombie.getTarget() != null && !this.isClimbing && (this.zombie.getCurrentState() == ClimbOverFenceState.instance() || this.zombie.getCurrentState() == ClimbOverWallState.instance() || this.zombie.getCurrentState() == ClimbThroughWindowState.instance())) {
         this.setUsingClimb(var1);
         this.isClimbing = true;
      } else if (this.zombie.getCurrentState() == WalkTowardState.instance()) {
         this.setUsingWalkTowardState(var1);
      } else if (this.zombie.getCurrentState() == LungeState.instance()) {
         this.setUsingLungeState(var1, (long)var2);
      } else if (this.zombie.getCurrentState() == PathFindState.instance() && this.zombie.isMoving()) {
         this.setUsingPathFindState(var1);
      } else {
         this.setUsingExtrapolation(var1, var2);
      }

      Vector2 var3 = this.zombie.dir.ToVector();
      this.zombie.networkCharacter.updateExtrapolationPoint(var2, this.zombie.x, this.zombie.y, var3.x, var3.y);
      if (DebugOptions.instance.MultiplayerLogPrediction.getValue() && Core.bDebug) {
         DebugLog.log(DebugType.Multiplayer, getPredictionDebug(this.zombie, var1, var2, this.getUpdateTime()));
      }

   }

   public void parse(ZombiePacket var1) {
      int var2 = (int)(GameTime.getServerTime() / 1000000L);
      if (DebugOptions.instance.MultiplayerLogPrediction.getValue()) {
         this.zombie.getNetworkCharacterAI().addTeleportData(var2, getPredictionDebug(this.zombie, var1, var2, (long)var2));
      }

      if (this.usePathFind) {
         this.pfb2.pathToLocationF(var1.x, var1.y, (float)var1.z);
         this.pfb2.walkingOnTheSpot.reset(this.zombie.x, this.zombie.y);
      }

      this.targetX = var1.x;
      this.targetY = var1.y;
      this.targetZ = var1.z;
      this.predictionType = var1.moveType;
      NetworkZombieVariables.setInt(this.zombie, (short)1, var1.target);
      NetworkZombieVariables.setInt(this.zombie, (short)3, var1.timeSinceSeenFlesh);
      if (this.zombie.isRemoteZombie()) {
         NetworkZombieVariables.setInt(this.zombie, (short)2, var1.speedMod);
         NetworkZombieVariables.setInt(this.zombie, (short)4, var1.smParamTargetAngle);
         NetworkZombieVariables.setBooleanVariables(this.zombie, var1.booleanVariables);
         this.zombie.setWalkType(var1.walkType.toString());
         this.zombie.realState = var1.realState;
      }

      this.zombie.realx = var1.realX;
      this.zombie.realy = var1.realY;
      this.zombie.realz = var1.realZ;
      if ((IsoUtils.DistanceToSquared(this.zombie.x, this.zombie.y, this.zombie.realx, this.zombie.realy) > 9.0F || this.zombie.z != (float)this.zombie.realz) && (this.zombie.isRemoteZombie() || IsoPlayer.getInstance() != null && IsoUtils.DistanceToSquared(this.zombie.x, this.zombie.y, IsoPlayer.getInstance().x, IsoPlayer.getInstance().y) > 2.0F)) {
         NetworkTeleport.teleport(this.zombie, NetworkTeleport.Type.teleportation, this.zombie.realx, this.zombie.realy, this.zombie.realz, 1.0F);
      }

   }

   public void preupdate() {
      if (GameClient.bClient) {
         if (this.zombie.target != null) {
            this.zombie.setTargetSeenTime(this.zombie.getTargetSeenTime() + GameTime.getInstance().getRealworldSecondsSinceLastUpdate());
         }
      } else if (GameServer.bServer) {
         byte var1 = (byte)((this.zombie.getVariableBoolean("bMoving") ? 1 : 0) | (this.zombie.getVariableBoolean("bPathfind") ? 2 : 0));
         if (this.flags != var1) {
            this.flags = var1;
            this.extraUpdate();
         }

         byte var2 = (byte)IsoDirections.fromAngleActual(this.zombie.getForwardDirection()).index();
         if (this.direction != var2) {
            this.direction = var2;
            this.extraUpdate();
         }
      }

   }

   public static String getPredictionDebug(IsoGameCharacter var0, ZombiePacket var1, int var2, long var3) {
      return String.format("Prediction Z_%d [type=%s, distance=%f], time [current=%d, next=%d], states [current=%s, previous=%s]", var1.id, var1.moveType.toString(), IsoUtils.DistanceTo(var0.x, var0.y, var1.x, var1.y), var2, var3 - (long)var2, var0.getCurrentStateName(), var0.getPreviousStateName());
   }
}
