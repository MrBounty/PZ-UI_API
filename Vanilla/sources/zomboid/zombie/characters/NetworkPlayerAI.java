package zombie.characters;

import java.util.LinkedList;
import zombie.GameTime;
import zombie.SystemDisabler;
import zombie.ai.states.CollideWithWallState;
import zombie.chat.ChatManager;
import zombie.core.Core;
import zombie.core.math.PZMath;
import zombie.core.utils.UpdateTimer;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.input.GameKeyboard;
import zombie.iso.IsoDirections;
import zombie.iso.IsoMovingObject;
import zombie.iso.Vector2;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.NetworkVariables;
import zombie.network.packets.PlayerPacket;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PathFindBehavior2;
import zombie.vehicles.PolygonalMap2;
import zombie.vehicles.VehicleManager;

public class NetworkPlayerAI extends NetworkCharacterAI {
   public final LinkedList events = new LinkedList();
   IsoPlayer player;
   private PathFindBehavior2 pfb2 = null;
   private final UpdateTimer timer = new UpdateTimer();
   private byte lastDirection = 0;
   private boolean needUpdate = false;
   private boolean blockUpdate = false;
   public boolean usePathFind = false;
   public float collidePointX;
   public float collidePointY;
   public float targetX = 0.0F;
   public float targetY = 0.0F;
   public int targetZ = 0;
   public boolean needToMovingUsingPathFinder = false;
   public boolean forcePathFinder = false;
   public Vector2 direction = new Vector2();
   public Vector2 distance = new Vector2();
   public boolean moving = false;
   public byte footstepSoundRadius = 0;
   public int lastBooleanVariables = 0;
   public float lastForwardDirection = 0.0F;
   public float lastPlayerMoveDirLen = 0.0F;
   private boolean pressedMovement = false;
   private boolean pressedCancelAction = false;
   public boolean climbFenceOutcomeFall = false;
   private Vector2 tempo = new Vector2();
   private static final int predictInterval = 1000;

   public NetworkPlayerAI(IsoGameCharacter var1) {
      super(var1);
      this.player = (IsoPlayer)var1;
      this.pfb2 = this.player.getPathFindBehavior2();
      var1.ulBeatenVehicle.Reset(200L);
      this.collidePointX = -1.0F;
      this.collidePointY = -1.0F;
   }

   public void needToUpdate() {
      this.needUpdate = true;
   }

   public void setBlockUpdate(boolean var1) {
      this.blockUpdate = var1;
   }

   public boolean isNeedToUpdate() {
      int var1 = NetworkPlayerVariables.getBooleanVariables(this.player);
      byte var2 = (byte)((int)(this.player.playerMoveDir.getDirection() * 10.0F));
      if ((!this.timer.check() && var1 == this.lastBooleanVariables && this.lastDirection == var2 || this.blockUpdate) && !this.needUpdate) {
         return false;
      } else {
         this.lastDirection = var2;
         this.needUpdate = false;
         return true;
      }
   }

   public void setUpdateTimer(float var1) {
      this.timer.reset((long)PZMath.clamp((int)var1, 200, 3800));
   }

   private void setUsingCollide(PlayerPacket var1, int var2) {
      if (SystemDisabler.useNetworkCharacter) {
         this.player.networkCharacter.checkResetPlayer(var2);
      }

      var1.x = (float)this.player.getCurrentSquare().getX();
      var1.y = (float)this.player.getCurrentSquare().getY();
      var1.z = (byte)this.player.getCurrentSquare().getZ();
      var1.usePathFinder = false;
      var1.moveType = NetworkVariables.PredictionTypes.Thump;
   }

   private void setUsingExtrapolation(PlayerPacket var1, int var2, int var3) {
      Vector2 var4 = this.player.dir.ToVector();
      if (SystemDisabler.useNetworkCharacter) {
         this.player.networkCharacter.checkResetPlayer(var2);
      }

      if (!this.player.isPlayerMoving()) {
         var1.x = this.player.x;
         var1.y = this.player.y;
         var1.z = (byte)((int)this.player.z);
         var1.usePathFinder = false;
         var1.moveType = NetworkVariables.PredictionTypes.Static;
      } else {
         Vector2 var5 = this.tempo;
         if (SystemDisabler.useNetworkCharacter) {
            NetworkCharacter.Transform var6 = this.player.networkCharacter.predict(var3, var2, this.player.x, this.player.y, var4.x, var4.y);
            var5.x = var6.position.x;
            var5.y = var6.position.y;
         } else {
            this.player.getDeferredMovement(var5);
            var5.x = this.player.x + var5.x * 0.03F * (float)var3;
            var5.y = this.player.y + var5.y * 0.03F * (float)var3;
         }

         if (this.player.z == this.pfb2.getTargetZ() && !PolygonalMap2.instance.lineClearCollide(this.player.x, this.player.y, var5.x, var5.y, (int)this.player.z, (IsoMovingObject)null)) {
            var1.x = var5.x;
            var1.y = var5.y;
            var1.z = (byte)((int)this.pfb2.getTargetZ());
         } else {
            Vector2 var7 = PolygonalMap2.instance.getCollidepoint(this.player.x, this.player.y, var5.x, var5.y, (int)this.player.z, (IsoMovingObject)null, 2);
            var1.collidePointX = var7.x;
            var1.collidePointY = var7.y;
            var1.x = var7.x + (this.player.dir != IsoDirections.N && this.player.dir != IsoDirections.S ? (this.player.dir.index() >= IsoDirections.NW.index() && this.player.dir.index() <= IsoDirections.SW.index() ? -1.0F : 1.0F) : 0.0F);
            var1.y = var7.y + (this.player.dir != IsoDirections.W && this.player.dir != IsoDirections.E ? (this.player.dir.index() >= IsoDirections.SW.index() && this.player.dir.index() <= IsoDirections.SE.index() ? 1.0F : -1.0F) : 0.0F);
            var1.z = (byte)((int)this.player.z);
         }

         var1.usePathFinder = false;
         var1.moveType = NetworkVariables.PredictionTypes.Moving;
      }
   }

   private void setUsingPathFindState(PlayerPacket var1, int var2) {
      if (SystemDisabler.useNetworkCharacter) {
         this.player.networkCharacter.checkResetPlayer(var2);
      }

      var1.x = this.pfb2.pathNextX;
      var1.y = this.pfb2.pathNextY;
      var1.z = (byte)((int)this.player.z);
      var1.usePathFinder = true;
      var1.moveType = NetworkVariables.PredictionTypes.PathFind;
   }

   public boolean set(PlayerPacket var1) {
      int var2 = (int)(GameTime.getServerTime() / 1000000L);
      var1.realx = this.player.x;
      var1.realy = this.player.y;
      var1.realz = (byte)((int)this.player.z);
      var1.realdir = (byte)this.player.dir.index();
      var1.realt = var2;
      if (this.player.vehicle == null) {
         var1.VehicleID = -1;
         var1.VehicleSeat = -1;
      } else {
         var1.VehicleID = this.player.vehicle.VehicleID;
         var1.VehicleSeat = (short)this.player.vehicle.getSeat(this.player);
      }

      boolean var3 = this.timer.check();
      var1.collidePointX = -1.0F;
      var1.collidePointY = -1.0F;
      if (var3) {
         this.setUpdateTimer(600.0F);
      }

      if (this.player.getCurrentState() == CollideWithWallState.instance()) {
         this.setUsingCollide(var1, var2);
      } else if (this.pfb2.isMovingUsingPathFind()) {
         this.setUsingPathFindState(var1, var2);
      } else {
         this.setUsingExtrapolation(var1, var2, 1000);
      }

      boolean var4 = (double)this.player.playerMoveDir.getLength() < 0.01D && this.lastPlayerMoveDirLen > 0.01F;
      this.lastPlayerMoveDirLen = this.player.playerMoveDir.getLength();
      var1.booleanVariables = NetworkPlayerVariables.getBooleanVariables(this.player);
      boolean var5 = this.lastBooleanVariables != var1.booleanVariables;
      this.lastBooleanVariables = var1.booleanVariables;
      var1.direction = this.player.getForwardDirection().getDirection();
      boolean var6 = Math.abs(this.lastForwardDirection - var1.direction) > 0.2F;
      this.lastForwardDirection = var1.direction;
      var1.footstepSoundRadius = this.footstepSoundRadius;
      return var3 || var5 || var6 || this.player.isJustMoved() || var4;
   }

   public void parse(PlayerPacket var1) {
      if (!this.player.isTeleporting()) {
         this.targetX = PZMath.roundFromEdges(var1.x);
         this.targetY = PZMath.roundFromEdges(var1.y);
         this.targetZ = var1.z;
         this.predictionType = var1.moveType;
         this.needToMovingUsingPathFinder = var1.usePathFinder;
         this.direction.set((float)Math.cos((double)var1.direction), (float)Math.sin((double)var1.direction));
         this.distance.set(var1.x - this.player.x, var1.y - this.player.y);
         if (this.usePathFind) {
            this.pfb2.pathToLocationF(var1.x, var1.y, (float)var1.z);
            this.pfb2.walkingOnTheSpot.reset(this.player.x, this.player.y);
         }

         BaseVehicle var2 = VehicleManager.instance.getVehicleByID(var1.VehicleID);
         NetworkPlayerVariables.setBooleanVariables(this.player, var1.booleanVariables);
         this.player.setbSeenThisFrame(false);
         this.player.setbCouldBeSeenThisFrame(false);
         this.player.TimeSinceLastNetData = 0;
         this.player.ensureOnTile();
         this.player.realx = var1.realx;
         this.player.realy = var1.realy;
         this.player.realz = var1.realz;
         this.player.realdir = IsoDirections.fromIndex(var1.realdir);
         this.collidePointX = var1.collidePointX;
         this.collidePointY = var1.collidePointY;
         var1.variables.apply(this.player);
         this.footstepSoundRadius = var1.footstepSoundRadius;
         String var10000;
         IsoGameCharacter var3;
         if (this.player.getVehicle() == null) {
            if (var2 != null) {
               if (var1.VehicleSeat >= 0 && var1.VehicleSeat < var2.getMaxPassengers()) {
                  var3 = var2.getCharacter(var1.VehicleSeat);
                  if (var3 == null) {
                     if (GameServer.bDebug) {
                        DebugLog.log(this.player.getUsername() + " got in vehicle " + var2.VehicleID + " seat " + var1.VehicleSeat);
                     }

                     var2.enterRSync(var1.VehicleSeat, this.player, var2);
                  } else if (var3 != this.player) {
                     var10000 = this.player.getUsername();
                     DebugLog.log(var10000 + " got in same seat as " + ((IsoPlayer)var3).getUsername());
                     this.player.sendObjectChange("exitVehicle");
                  }
               } else {
                  DebugLog.log(this.player.getUsername() + " invalid seat vehicle " + var2.VehicleID + " seat " + var1.VehicleSeat);
               }
            }
         } else if (var2 != null) {
            if (var2 == this.player.getVehicle() && this.player.getVehicle().getSeat(this.player) != -1) {
               var3 = var2.getCharacter(var1.VehicleSeat);
               if (var3 == null) {
                  if (var2.getSeat(this.player) != var1.VehicleSeat) {
                     var2.switchSeatRSync(this.player, var1.VehicleSeat);
                  }
               } else if (var3 != this.player) {
                  var10000 = this.player.getUsername();
                  DebugLog.log(var10000 + " switched to same seat as " + ((IsoPlayer)var3).getUsername());
                  this.player.sendObjectChange("exitVehicle");
               }
            } else {
               var10000 = this.player.getUsername();
               DebugLog.log(var10000 + " vehicle/seat remote " + var2.VehicleID + "/" + var1.VehicleSeat + " local " + this.player.getVehicle().VehicleID + "/" + this.player.getVehicle().getSeat(this.player));
               this.player.sendObjectChange("exitVehicle");
            }
         } else {
            this.player.getVehicle().exitRSync(this.player);
            this.player.setVehicle((BaseVehicle)null);
         }

         this.setPressedMovement(false);
         this.setPressedCancelAction(false);
      }
   }

   public boolean isPressedMovement() {
      return this.pressedMovement;
   }

   public void setPressedMovement(boolean var1) {
      boolean var2 = !this.pressedMovement && var1;
      this.pressedMovement = var1;
      if (this.player.isLocal() && var2) {
         GameClient.sendEvent(this.player, "Update");
      }

   }

   public boolean isPressedCancelAction() {
      return this.pressedCancelAction;
   }

   public void setPressedCancelAction(boolean var1) {
      boolean var2 = !this.pressedCancelAction && var1;
      this.pressedCancelAction = var1;
      if (this.player.isLocal() && var2) {
         GameClient.sendEvent(this.player, "Update");
      }

   }

   public void update() {
      if (DebugOptions.instance.MultiplayerSpawnZombie.getValue() && GameKeyboard.isKeyPressed(44)) {
         if (Core.bDebug && GameKeyboard.isKeyDown(42)) {
            throw new NullPointerException("debug null pointer exception");
         }

         ChatManager.getInstance().showInfoMessage(this.player.getUsername(), "spawn zombie");
         GameClient.SendCommandToServer(String.format("/createhorde2 -x %d -y %d -z %d", (int)this.player.getX(), (int)this.player.getY(), (int)this.player.getZ()));
      }

   }
}
