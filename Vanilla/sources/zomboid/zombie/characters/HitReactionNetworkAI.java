package zombie.characters;

import zombie.GameTime;
import zombie.ai.states.PlayerFallDownState;
import zombie.ai.states.PlayerKnockedDown;
import zombie.ai.states.PlayerOnGroundState;
import zombie.ai.states.ZombieFallDownState;
import zombie.ai.states.ZombieOnGroundState;
import zombie.core.Core;
import zombie.core.math.PZMath;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoDirections;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.network.GameServer;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.PolygonalMap2;

public class HitReactionNetworkAI {
   private static final float G = 2.0F;
   private static final float DURATION = 600.0F;
   public final Vector2 startPosition = new Vector2();
   public final Vector2 finalPosition = new Vector2();
   public byte finalPositionZ = 0;
   public final Vector2 startDirection = new Vector2();
   public final Vector2 finalDirection = new Vector2();
   private float startAngle;
   private float finalAngle;
   private final IsoGameCharacter character;
   private long startTime;

   public HitReactionNetworkAI(IsoGameCharacter var1) {
      this.character = var1;
      this.startTime = 0L;
   }

   public boolean isSetup() {
      return this.finalPosition.x != 0.0F && this.finalPosition.y != 0.0F;
   }

   public boolean isStarted() {
      return this.startTime > 0L;
   }

   public void start() {
      if (this.isSetup() && !this.isStarted()) {
         this.startTime = GameTime.getServerTimeMills();
         if (this.startPosition.x != this.character.x || this.startPosition.y != this.character.y) {
            DebugLog.Multiplayer.warn("HitReaction start shifted");
         }

         if (Core.bDebug) {
            DebugLog.log(DebugType.Damage, String.format("HitReaction start id=%d: %s / %s => %s ", this.character.getOnlineID(), this.getActualDescription(), this.getStartDescription(), this.getFinalDescription()));
         }
      }

   }

   public void finish() {
      if (this.startTime != 0L && Core.bDebug) {
         DebugLog.log(DebugType.Damage, String.format("HitReaction finish id=%d: %s / %s => %s ", this.character.getOnlineID(), this.getActualDescription(), this.getStartDescription(), this.getFinalDescription()));
      }

      this.startTime = 0L;
      this.setup(0.0F, 0.0F, (byte)0, 0.0F);
   }

   public void setup(float var1, float var2, byte var3, Float var4) {
      this.startPosition.set(this.character.x, this.character.y);
      this.finalPosition.set(var1, var2);
      this.finalPositionZ = var3;
      this.startDirection.set(this.character.getForwardDirection());
      this.startAngle = this.character.getAnimAngleRadians();
      Vector2 var5 = (new Vector2()).set(this.finalPosition.x - this.startPosition.x, this.finalPosition.y - this.startPosition.y);
      if (var4 == null) {
         var5.normalize();
         var4 = var5.dot(this.character.getForwardDirection());
         PZMath.lerp(this.finalDirection, var5, this.character.getForwardDirection(), Math.abs(var4));
         IsoMovingObject.getVectorFromDirection(this.finalDirection, IsoDirections.fromAngle(this.finalDirection));
      } else {
         this.finalDirection.setLengthAndDirection(var4, 1.0F);
      }

      this.finalAngle = var4;
      if (this.isSetup() && Core.bDebug) {
         DebugLog.log(DebugType.Damage, String.format("HitReaction setup id=%d: %s / %s => %s ", this.character.getOnlineID(), this.getActualDescription(), this.getStartDescription(), this.getFinalDescription()));
      }

   }

   private void moveInternal(float var1, float var2, float var3, float var4) {
      this.character.nx = var1;
      this.character.ny = var2;
      this.character.setDir(IsoDirections.fromAngle(var3, var4));
      this.character.setForwardDirection(var3, var4);
      this.character.getAnimationPlayer().SetForceDir(this.character.getForwardDirection());
   }

   public void moveFinal() {
      this.moveInternal(this.finalPosition.x, this.finalPosition.y, this.finalDirection.x, this.finalDirection.y);
      this.character.lx = this.character.nx = this.character.x = this.finalPosition.x;
      this.character.ly = this.character.ny = this.character.y = this.finalPosition.y;
      this.character.setCurrent(IsoWorld.instance.CurrentCell.getGridSquare((double)((int)this.finalPosition.x), (double)((int)this.finalPosition.y), (double)this.character.z));
      if (Core.bDebug) {
         DebugLog.log(DebugType.Damage, String.format("HitReaction final id=%d: %s / %s => %s ", this.character.getOnlineID(), this.getActualDescription(), this.getStartDescription(), this.getFinalDescription()));
         DebugLog.log(DebugType.Multiplayer, "HitReaction final (): " + this.getDescription());
      }

   }

   public void move() {
      if (this.finalPositionZ != (byte)((int)this.character.z)) {
         DebugLog.log(String.format("HitReaction interrupt id=%d: z-final:%d z-current=%d", this.character.getOnlineID(), this.finalPositionZ, (byte)((int)this.character.z)));
         this.finish();
      } else {
         float var1 = Math.min(1.0F, Math.max(0.0F, (float)(GameTime.getServerTimeMills() - this.startTime) / 600.0F));
         if (this.startPosition.x == this.finalPosition.x && this.startPosition.y == this.finalPosition.y) {
            var1 = 1.0F;
         }

         if (var1 < 1.0F) {
            var1 = (PZMath.gain(var1 * 0.5F + 0.5F, 2.0F) - 0.5F) * 2.0F;
            this.moveInternal(PZMath.lerp(this.startPosition.x, this.finalPosition.x, var1), PZMath.lerp(this.startPosition.y, this.finalPosition.y, var1), PZMath.lerp(this.startDirection.x, this.finalDirection.x, var1), PZMath.lerp(this.startDirection.y, this.finalDirection.y, var1));
         } else {
            this.moveFinal();
            this.finish();
         }

      }
   }

   public boolean isDoSkipMovement() {
      if (this.character instanceof IsoZombie) {
         return this.character.isCurrentState(ZombieFallDownState.instance()) || this.character.isCurrentState(ZombieOnGroundState.instance());
      } else if (!(this.character instanceof IsoPlayer)) {
         return false;
      } else {
         return this.character.isCurrentState(PlayerFallDownState.instance()) || this.character.isCurrentState(PlayerKnockedDown.instance()) || this.character.isCurrentState(PlayerOnGroundState.instance());
      }
   }

   private String getStartDescription() {
      return String.format("start=[ pos=( %f ; %f ) dir=( %f ; %f ) angle=%f ]", this.startPosition.x, this.startPosition.y, this.startDirection.x, this.startDirection.y, this.startAngle);
   }

   private String getFinalDescription() {
      return String.format("final=[ pos=( %f ; %f ) dir=( %f ; %f ) angle=%f ]", this.finalPosition.x, this.finalPosition.y, this.finalDirection.x, this.finalDirection.y, this.finalAngle);
   }

   private String getActualDescription() {
      return String.format("actual=[ pos=( %f ; %f ) dir=( %f ; %f ) angle=%f ]", this.character.x, this.character.y, this.character.getForwardDirection().getX(), this.character.getForwardDirection().getY(), this.character.getAnimAngleRadians());
   }

   public String getDescription() {
      return String.format("start=%d | (x=%f,y=%f;a=%f;l=%f)", this.startTime, this.finalPosition.x, this.finalPosition.y, this.finalAngle, IsoUtils.DistanceTo(this.startPosition.x, this.startPosition.y, this.finalPosition.x, this.finalPosition.y));
   }

   public static void CalcHitReactionWeapon(IsoGameCharacter var0, IsoGameCharacter var1, HandWeapon var2) {
      HitReactionNetworkAI var3 = var1.getHitReactionNetworkAI();
      if (var1.isOnFloor()) {
         var3.setup(var1.x, var1.y, (byte)((int)var1.z), var1.getAnimAngleRadians());
      } else {
         Vector2 var4 = new Vector2();
         Float var5 = var1.calcHitDir(var0, var2, var4);
         if (var1 instanceof IsoPlayer) {
            var4.x = (var4.x + var1.x + ((IsoPlayer)var1).networkAI.targetX) * 0.5F;
            var4.y = (var4.y + var1.y + ((IsoPlayer)var1).networkAI.targetY) * 0.5F;
         } else {
            var4.x += var1.x;
            var4.y += var1.y;
         }

         var4.x = PZMath.roundFromEdges(var4.x);
         var4.y = PZMath.roundFromEdges(var4.y);
         if (PolygonalMap2.instance.lineClearCollide(var1.x, var1.y, var4.x, var4.y, (int)var1.z, (IsoMovingObject)null, false, true)) {
            var4.x = var1.x;
            var4.y = var1.y;
         }

         var3.setup(var4.x, var4.y, (byte)((int)var1.z), var5);
      }

      if (var3.isSetup()) {
         var3.start();
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "HitReaction start (local): " + var3.getDescription());
         }
      }

   }

   public static void CalcHitReactionVehicle(IsoGameCharacter var0, BaseVehicle var1) {
      HitReactionNetworkAI var2 = var0.getHitReactionNetworkAI();
      if (!var2.isStarted()) {
         if (var0.isOnFloor()) {
            var2.setup(var0.x, var0.y, (byte)((int)var0.z), var0.getAnimAngleRadians());
         } else {
            Vector2 var3 = new Vector2();
            var0.calcHitDir(var3);
            if (var0 instanceof IsoPlayer) {
               var3.x = (var3.x + var0.x + ((IsoPlayer)var0).networkAI.targetX) * 0.5F;
               var3.y = (var3.y + var0.y + ((IsoPlayer)var0).networkAI.targetY) * 0.5F;
            } else {
               var3.x += var0.x;
               var3.y += var0.y;
            }

            var3.x = PZMath.roundFromEdges(var3.x);
            var3.y = PZMath.roundFromEdges(var3.y);
            if (PolygonalMap2.instance.lineClearCollide(var0.x, var0.y, var3.x, var3.y, (int)var0.z, var1, false, true)) {
               var3.x = var0.x;
               var3.y = var0.y;
            }

            var2.setup(var3.x, var3.y, (byte)((int)var0.z), (Float)null);
         }

         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "HitReaction setup (vehicle): " + var2.getDescription());
         }
      }

      if (var2.isSetup()) {
         var2.start();
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "HitReaction start (vehicle): " + var2.getDescription());
         }
      }

   }

   public void process(float var1, float var2, float var3, float var4) {
      this.setup(var1, var2, (byte)((int)var3), var4);
      if (Core.bDebug) {
         DebugLog.log(DebugType.Damage, "Fall setup (remote): " + this.getDescription());
      }

      this.start();
      if (Core.bDebug) {
         DebugLog.log(DebugType.Damage, "Fall start (remote): " + this.getDescription());
      }

      if (GameServer.bServer) {
         this.moveFinal();
         this.finish();
         if (Core.bDebug) {
            DebugLog.log(DebugType.Damage, "Fall final (remote): " + this.getDescription());
         }
      }

   }
}
