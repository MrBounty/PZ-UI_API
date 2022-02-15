package zombie.characters;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import zombie.GameTime;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.core.Core;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.network.NetworkVariables;
import zombie.network.packets.DeadCharacterPacket;
import zombie.network.packets.hit.VehicleHitPacket;

public abstract class NetworkCharacterAI {
   private static final short VEHICLE_HIT_DELAY_MS = 500;
   public NetworkVariables.PredictionTypes predictionType;
   protected DeadCharacterPacket deadBody;
   protected VehicleHitPacket vehicleHit;
   protected float timestamp;
   protected BaseAction action;
   protected long noCollisionTime;
   protected boolean wasLocal;
   protected final HitReactionNetworkAI hitReaction;
   private final IsoGameCharacter character;
   public NetworkTeleport.NetworkTeleportDebug teleportDebug;
   public final HashMap debugData = new LinkedHashMap() {
      protected boolean removeEldestEntry(Entry var1) {
         return this.size() > 10;
      }
   };

   public NetworkCharacterAI(IsoGameCharacter var1) {
      this.character = var1;
      this.deadBody = null;
      this.wasLocal = false;
      this.vehicleHit = null;
      this.noCollisionTime = 0L;
      this.hitReaction = new HitReactionNetworkAI(var1);
      this.predictionType = NetworkVariables.PredictionTypes.None;
      this.clearTeleportDebug();
   }

   public void reset() {
      this.deadBody = null;
      this.wasLocal = false;
      this.vehicleHit = null;
      this.noCollisionTime = 0L;
      this.hitReaction.finish();
      this.predictionType = NetworkVariables.PredictionTypes.None;
      this.clearTeleportDebug();
   }

   public void setLocal(boolean var1) {
      this.wasLocal = var1;
   }

   public boolean wasLocal() {
      return this.wasLocal;
   }

   public NetworkTeleport.NetworkTeleportDebug getTeleportDebug() {
      return this.teleportDebug;
   }

   public void clearTeleportDebug() {
      this.teleportDebug = null;
      this.debugData.clear();
   }

   public void setTeleportDebug(NetworkTeleport.NetworkTeleportDebug var1) {
      this.teleportDebug = var1;
      this.debugData.entrySet().stream().sorted(Entry.comparingByKey(Comparator.naturalOrder())).forEach((var0) -> {
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "==> " + (String)var0.getValue());
         }

      });
      if (Core.bDebug) {
         DebugLog.log(DebugType.Multiplayer, String.format("NetworkTeleport %s id=%d distance=%.3f prediction=%s", this.character.getClass().getSimpleName(), this.character.getOnlineID(), var1.getDistance(), this.predictionType));
      }

   }

   public void addTeleportData(int var1, String var2) {
      this.debugData.put(var1, var2);
   }

   public void processDeadBody() {
      if (this.isSetDeadBody() && !this.hitReaction.isSetup() && !this.hitReaction.isStarted()) {
         this.deadBody.process();
         this.setDeadBody((DeadCharacterPacket)null);
      }

   }

   public void setDeadBody(DeadCharacterPacket var1) {
      this.deadBody = var1;
      if (Core.bDebug) {
         DebugLog.log(DebugType.Death, "SetDeadBody: " + (var1 == null ? "processed" : "postpone"));
      }

   }

   public boolean isSetDeadBody() {
      return this.deadBody != null && this.deadBody.isConsistent();
   }

   public void setAction(BaseAction var1) {
      this.action = var1;
   }

   public BaseAction getAction() {
      return this.action;
   }

   public void startAction() {
      if (this.action != null) {
         this.action.start();
      }

   }

   public void stopAction() {
      if (this.action != null) {
         this.setOverride(false, (String)null, (String)null);
         this.action.stop();
      }

   }

   public void setOverride(boolean var1, String var2, String var3) {
      if (this.action != null) {
         this.action.chr.forceNullOverride = var1;
         this.action.chr.overridePrimaryHandModel = var2;
         this.action.chr.overrideSecondaryHandModel = var3;
         this.action.chr.resetModelNextFrame();
      }

   }

   public void processVehicleHit() {
      this.vehicleHit.tryProcessInternal();
      this.setVehicleHit((VehicleHitPacket)null);
   }

   public void setVehicleHit(VehicleHitPacket var1) {
      this.vehicleHit = var1;
      this.timestamp = (float)TimeUnit.NANOSECONDS.toMillis(GameTime.getServerTime());
      if (Core.bDebug) {
         DebugLog.log(DebugType.Damage, "SetVehicleHit: " + (var1 == null ? "processed" : "postpone"));
      }

   }

   public boolean isSetVehicleHit() {
      return this.vehicleHit != null && this.vehicleHit.isConsistent();
   }

   public void resetVehicleHitTimeout() {
      this.timestamp = (float)(TimeUnit.NANOSECONDS.toMillis(GameTime.getServerTime()) - 500L);
      if (this.vehicleHit == null && Core.bDebug) {
         DebugLog.log(DebugType.Damage, "VehicleHit is not set");
      }

   }

   public boolean isVehicleHitTimeout() {
      boolean var1 = (float)TimeUnit.NANOSECONDS.toMillis(GameTime.getServerTime()) - this.timestamp >= 500.0F;
      if (var1 && Core.bDebug) {
         DebugLog.log(DebugType.Multiplayer, "VehicleHit timeout");
      }

      return var1;
   }

   public void updateHitVehicle() {
      if (this.isSetVehicleHit() && this.isVehicleHitTimeout()) {
         this.processVehicleHit();
      }

   }

   public boolean isCollisionEnabled() {
      return this.noCollisionTime == 0L;
   }

   public boolean isNoCollisionTimeout() {
      boolean var1 = GameTime.getServerTimeMills() > this.noCollisionTime;
      if (var1) {
         this.setNoCollision(0L);
      }

      return var1;
   }

   public void setNoCollision(long var1) {
      if (var1 == 0L) {
         this.noCollisionTime = 0L;
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "SetNoCollision: disabled");
         }
      } else {
         this.noCollisionTime = GameTime.getServerTimeMills() + var1;
         if (Core.bDebug) {
            DebugLog.log(DebugType.Multiplayer, "SetNoCollision: enabled for " + var1 + " ms");
         }
      }

   }
}
