package zombie.network.packets;

import java.nio.ByteBuffer;
import java.util.HashMap;
import zombie.GameWindow;
import zombie.ai.states.ClimbDownSheetRopeState;
import zombie.ai.states.ClimbSheetRopeState;
import zombie.ai.states.FishingState;
import zombie.ai.states.SmashWindowState;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.NetworkPlayerVariables;
import zombie.characters.CharacterTimedActions.BaseAction;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.objects.IsoThumpable;
import zombie.iso.objects.IsoWindow;
import zombie.iso.objects.IsoWindowFrame;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.util.StringUtils;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.VehicleManager;
import zombie.vehicles.VehiclePart;
import zombie.vehicles.VehicleWindow;

public class EventPacket implements INetworkPacket {
   public static final int MAX_PLAYER_EVENTS = 10;
   private static final long EVENT_TIMEOUT = 5000L;
   private static final short EVENT_FLAGS_VAULT_OVER_SPRINT = 1;
   private static final short EVENT_FLAGS_VAULT_OVER_RUN = 2;
   private static final short EVENT_FLAGS_BUMP_FALL = 4;
   private static final short EVENT_FLAGS_BUMP_STAGGERED = 8;
   private static final short EVENT_FLAGS_ACTIVATE_ITEM = 16;
   private static final short EVENT_FLAGS_CLIMB_SUCCESS = 32;
   private static final short EVENT_FLAGS_CLIMB_STRUGGLE = 64;
   private static final short EVENT_FLAGS_BUMP_FROM_BEHIND = 128;
   private static final short EVENT_FLAGS_BUMP_TARGET_TYPE = 256;
   private static final short EVENT_FLAGS_PRESSED_MOVEMENT = 512;
   private static final short EVENT_FLAGS_PRESSED_CANCEL_ACTION = 1024;
   private static final short EVENT_FLAGS_SMASH_CAR_WINDOW = 2048;
   private static final short EVENT_FLAGS_FITNESS_FINISHED = 4096;
   private short id;
   public float x;
   public float y;
   public float z;
   private byte eventID;
   private String type1;
   private String type2;
   private String type3;
   private String type4;
   private float strafeSpeed;
   private float walkSpeed;
   private float walkInjury;
   private int booleanVariables;
   private short flags;
   private IsoPlayer player;
   private EventPacket.EventType event;
   private long timestamp;

   public String getDescription() {
      short var10000 = this.id;
      return "[ player=" + var10000 + " \"" + (this.player == null ? "?" : this.player.getUsername()) + "\" | name=\"" + (this.event == null ? "?" : this.event.name()) + "\" | pos=( " + this.x + " ; " + this.y + " ; " + this.z + " ) | type1=\"" + this.type1 + "\" | type2=\"" + this.type2 + "\" | type3=\"" + this.type3 + "\" | type4=\"" + this.type4 + "\" | flags=" + this.flags + "\" | variables=" + this.booleanVariables + " ]";
   }

   public boolean isConsistent() {
      boolean var1 = this.player != null && this.event != null;
      if (!var1 && Core.bDebug) {
         DebugLog.log(DebugType.Multiplayer, "[Event] is not consistent " + this.getDescription());
      }

      return var1;
   }

   public void parse(ByteBuffer var1) {
      this.id = var1.getShort();
      this.x = var1.getFloat();
      this.y = var1.getFloat();
      this.z = var1.getFloat();
      this.eventID = var1.get();
      this.type1 = GameWindow.ReadString(var1);
      this.type2 = GameWindow.ReadString(var1);
      this.type3 = GameWindow.ReadString(var1);
      this.type4 = GameWindow.ReadString(var1);
      this.strafeSpeed = var1.getFloat();
      this.walkSpeed = var1.getFloat();
      this.walkInjury = var1.getFloat();
      this.booleanVariables = var1.getInt();
      this.flags = var1.getShort();
      if (this.eventID >= 0 && this.eventID < EventPacket.EventType.values().length) {
         this.event = EventPacket.EventType.values()[this.eventID];
      } else {
         DebugLog.Multiplayer.warn("Unknown event=" + this.eventID);
         this.event = null;
      }

      if (GameServer.bServer) {
         this.player = (IsoPlayer)GameServer.IDToPlayerMap.get(this.id);
      } else if (GameClient.bClient) {
         this.player = (IsoPlayer)GameClient.IDToPlayerMap.get(this.id);
      } else {
         this.player = null;
      }

      this.timestamp = System.currentTimeMillis() + 5000L;
   }

   public void write(ByteBufferWriter var1) {
      var1.putShort(this.id);
      var1.putFloat(this.x);
      var1.putFloat(this.y);
      var1.putFloat(this.z);
      var1.putByte(this.eventID);
      var1.putUTF(this.type1);
      var1.putUTF(this.type2);
      var1.putUTF(this.type3);
      var1.putUTF(this.type4);
      var1.putFloat(this.strafeSpeed);
      var1.putFloat(this.walkSpeed);
      var1.putFloat(this.walkInjury);
      var1.putInt(this.booleanVariables);
      var1.putShort(this.flags);
   }

   public boolean isRelevant(UdpConnection var1) {
      return var1.RelevantTo(this.x, this.y);
   }

   public boolean isMovableEvent() {
      if (!this.isConsistent()) {
         return false;
      } else {
         return EventPacket.EventType.EventClimbFence.equals(this.event) || EventPacket.EventType.EventFallClimb.equals(this.event);
      }
   }

   private boolean requireNonMoving() {
      return this.isConsistent() && (EventPacket.EventType.EventClimbWindow.equals(this.event) || EventPacket.EventType.EventClimbFence.equals(this.event) || EventPacket.EventType.EventClimbDownRope.equals(this.event) || EventPacket.EventType.EventClimbRope.equals(this.event) || EventPacket.EventType.EventClimbWall.equals(this.event));
   }

   private IsoWindow getWindow(IsoPlayer var1) {
      IsoDirections[] var2 = IsoDirections.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         IsoDirections var5 = var2[var4];
         IsoObject var6 = var1.getContextDoorOrWindowOrWindowFrame(var5);
         if (var6 instanceof IsoWindow) {
            return (IsoWindow)var6;
         }
      }

      return null;
   }

   private IsoObject getObject(IsoPlayer var1) {
      IsoDirections[] var2 = IsoDirections.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         IsoDirections var5 = var2[var4];
         IsoObject var6 = var1.getContextDoorOrWindowOrWindowFrame(var5);
         if (var6 instanceof IsoWindow || var6 instanceof IsoThumpable || IsoWindowFrame.isWindowFrame(var6)) {
            return var6;
         }
      }

      return null;
   }

   private IsoDirections checkCurrentIsEventGridSquareFence(IsoPlayer var1) {
      IsoGridSquare var3 = var1.getCell().getGridSquare((double)this.x, (double)this.y, (double)this.z);
      IsoGridSquare var4 = var1.getCell().getGridSquare((double)this.x, (double)(this.y + 1.0F), (double)this.z);
      IsoGridSquare var5 = var1.getCell().getGridSquare((double)(this.x + 1.0F), (double)this.y, (double)this.z);
      IsoDirections var2;
      if (var3.Is(IsoFlagType.HoppableN)) {
         var2 = IsoDirections.N;
      } else if (var3.Is(IsoFlagType.HoppableW)) {
         var2 = IsoDirections.W;
      } else if (var4.Is(IsoFlagType.HoppableN)) {
         var2 = IsoDirections.S;
      } else if (var5.Is(IsoFlagType.HoppableW)) {
         var2 = IsoDirections.E;
      } else {
         var2 = IsoDirections.Max;
      }

      return var2;
   }

   public boolean isTimeout() {
      return System.currentTimeMillis() > this.timestamp;
   }

   public void tryProcess() {
      if (this.isConsistent()) {
         if (this.player.networkAI.events.size() < 10) {
            this.player.networkAI.events.add(this);
         } else {
            DebugLog.Multiplayer.warn("Event skipped: " + this.getDescription());
         }
      }

   }

   public boolean process(IsoPlayer var1) {
      boolean var2 = false;
      if (this.isConsistent()) {
         var1.overridePrimaryHandModel = null;
         var1.overrideSecondaryHandModel = null;
         if (var1.getCurrentSquare() == var1.getCell().getGridSquare((double)this.x, (double)this.y, (double)this.z) && !var1.isPlayerMoving() || !this.requireNonMoving()) {
            IsoWindow var9;
            switch(this.event) {
            case EventSetActivatedPrimary:
               if (var1.getPrimaryHandItem() != null && var1.getPrimaryHandItem().canEmitLight()) {
                  var1.getPrimaryHandItem().setActivatedRemote((this.flags & 16) != 0);
                  var2 = true;
               }
               break;
            case EventSetActivatedSecondary:
               if (var1.getSecondaryHandItem() != null && var1.getSecondaryHandItem().canEmitLight()) {
                  var1.getSecondaryHandItem().setActivatedRemote((this.flags & 16) != 0);
                  var2 = true;
               }
               break;
            case EventFallClimb:
               var1.setVariable("ClimbFenceOutcome", "fall");
               var1.setVariable("BumpDone", true);
               var1.setFallOnFront(true);
               var2 = true;
               break;
            case collideWithWall:
               var1.setCollideType(this.type1);
               var1.actionContext.reportEvent("collideWithWall");
               var2 = true;
               break;
            case EventFishing:
               var1.setVariable("FishingStage", this.type1);
               if (!FishingState.instance().equals(var1.getCurrentState())) {
                  var1.setVariable("forceGetUp", true);
                  var1.actionContext.reportEvent("EventFishing");
               }

               var2 = true;
               break;
            case EventFitness:
               var1.setVariable("ExerciseType", this.type1);
               var1.setVariable("FitnessFinished", false);
               var1.actionContext.reportEvent("EventFitness");
               var2 = true;
               break;
            case EventUpdateFitness:
               var1.clearVariable("ExerciseHand");
               var1.setVariable("ExerciseType", this.type2);
               if (!StringUtils.isNullOrEmpty(this.type1)) {
                  var1.setVariable("ExerciseHand", this.type1);
               }

               var1.setFitnessSpeed();
               if ((this.flags & 4096) != 0) {
                  var1.setVariable("ExerciseStarted", false);
                  var1.setVariable("ExerciseEnded", true);
               }

               var1.setPrimaryHandItem((InventoryItem)null);
               var1.setSecondaryHandItem((InventoryItem)null);
               var1.overridePrimaryHandModel = null;
               var1.overrideSecondaryHandModel = null;
               var1.overridePrimaryHandModel = this.type3;
               var1.overrideSecondaryHandModel = this.type4;
               var1.resetModelNextFrame();
               var2 = true;
               break;
            case EventEmote:
               var1.setVariable("emote", this.type1);
               var1.actionContext.reportEvent("EventEmote");
               var2 = true;
               break;
            case EventSitOnGround:
               var1.actionContext.reportEvent("EventSitOnGround");
               var2 = true;
               break;
            case EventClimbRope:
               var1.climbSheetRope();
               var2 = true;
               break;
            case EventClimbDownRope:
               var1.climbDownSheetRope();
               var2 = true;
               break;
            case EventClimbFence:
               IsoDirections var13 = this.checkCurrentIsEventGridSquareFence(var1);
               if (var13 != IsoDirections.Max) {
                  var1.climbOverFence(var13);
                  if (var1.isSprinting()) {
                     var1.setVariable("VaultOverSprint", true);
                  }

                  if (var1.isRunning()) {
                     var1.setVariable("VaultOverRun", true);
                  }

                  var2 = true;
               }
               break;
            case EventClimbWall:
               var1.setClimbOverWallStruggle((this.flags & 64) != 0);
               var1.setClimbOverWallSuccess((this.flags & 32) != 0);
               IsoDirections[] var12 = IsoDirections.values();
               int var7 = var12.length;

               for(int var10 = 0; var10 < var7; ++var10) {
                  IsoDirections var6 = var12[var10];
                  if (var1.climbOverWall(var6)) {
                     return true;
                  }
               }

               return var2;
            case EventClimbWindow:
               IsoObject var11 = this.getObject(var1);
               if (var11 instanceof IsoWindow) {
                  var1.climbThroughWindow((IsoWindow)var11);
                  var2 = true;
               } else if (var11 instanceof IsoThumpable) {
                  var1.climbThroughWindow((IsoThumpable)var11);
                  var2 = true;
               }

               if (IsoWindowFrame.isWindowFrame(var11)) {
                  var1.climbThroughWindowFrame(var11);
                  var2 = true;
               }
               break;
            case EventOpenWindow:
               var9 = this.getWindow(var1);
               if (var9 != null) {
                  var1.openWindow(var9);
                  var2 = true;
               }
               break;
            case EventCloseWindow:
               var9 = this.getWindow(var1);
               if (var9 != null) {
                  var1.closeWindow(var9);
                  var2 = true;
               }
               break;
            case EventSmashWindow:
               if ((this.flags & 2048) != 0) {
                  BaseVehicle var8 = VehicleManager.instance.getVehicleByID(Short.parseShort(this.type1));
                  if (var8 != null) {
                     VehiclePart var4 = var8.getPartById(this.type2);
                     if (var4 != null) {
                        VehicleWindow var5 = var4.getWindow();
                        if (var5 != null) {
                           var1.smashCarWindow(var4);
                           var2 = true;
                        }
                     }
                  }
               } else {
                  var9 = this.getWindow(var1);
                  if (var9 != null) {
                     var1.smashWindow(var9);
                     var2 = true;
                  }
               }
               break;
            case wasBumped:
               var1.setBumpDone(false);
               var1.setVariable("BumpFallAnimFinished", false);
               var1.setBumpType(this.type1);
               var1.setBumpFallType(this.type2);
               var1.setBumpFall((this.flags & 4) != 0);
               var1.setBumpStaggered((this.flags & 8) != 0);
               var1.reportEvent("wasBumped");
               if (!StringUtils.isNullOrEmpty(this.type3) && !StringUtils.isNullOrEmpty(this.type4)) {
                  IsoGameCharacter var3 = null;
                  if ((this.flags & 256) != 0) {
                     var3 = (IsoGameCharacter)GameClient.IDToZombieMap.get(Short.parseShort(this.type3));
                  } else {
                     var3 = (IsoGameCharacter)GameClient.IDToPlayerMap.get(Short.parseShort(this.type3));
                  }

                  if (var3 != null) {
                     var3.setBumpType(this.type4);
                     var3.setHitFromBehind((this.flags & 128) != 0);
                  }
               }

               var2 = true;
               break;
            case EventOverrideItem:
               if (var1.getNetworkCharacterAI().getAction() != null) {
                  var1.getNetworkCharacterAI().setOverride(true, this.type1, this.type2);
               }

               var2 = true;
               break;
            case ChargeSpearConnect:
               var2 = true;
               break;
            case Update:
               var1.networkAI.setPressedMovement((this.flags & 512) != 0);
               var1.networkAI.setPressedCancelAction((this.flags & 1024) != 0);
               var2 = true;
               break;
            case Unknown:
            default:
               DebugLog.Multiplayer.warn("[Event] unknown: " + this.getDescription());
               var2 = true;
            }
         }
      }

      return var2;
   }

   public boolean set(IsoPlayer var1, String var2) {
      boolean var3 = false;
      this.player = var1;
      this.id = var1.getOnlineID();
      this.x = var1.getX();
      this.y = var1.getY();
      this.z = var1.getZ();
      this.type1 = null;
      this.type2 = null;
      this.type3 = null;
      this.type4 = null;
      this.booleanVariables = NetworkPlayerVariables.getBooleanVariables(var1);
      this.strafeSpeed = var1.getVariableFloat("StrafeSpeed", 1.0F);
      this.walkSpeed = var1.getVariableFloat("WalkSpeed", 1.0F);
      this.walkInjury = var1.getVariableFloat("WalkInjury", 0.0F);
      this.flags = 0;
      EventPacket.EventType[] var4 = EventPacket.EventType.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         EventPacket.EventType var7 = var4[var6];
         if (var7.name().equals(var2)) {
            this.event = var7;
            this.eventID = (byte)var7.ordinal();
            switch(var7) {
            case EventSetActivatedPrimary:
               this.flags = (short)(this.flags | (var1.getPrimaryHandItem().isActivated() ? 16 : 0));
               break;
            case EventSetActivatedSecondary:
               this.flags = (short)(this.flags | (var1.getSecondaryHandItem().isActivated() ? 16 : 0));
            case EventFallClimb:
            case EventSitOnGround:
            case EventClimbRope:
            case EventClimbDownRope:
            case EventClimbWindow:
            case EventOpenWindow:
            case EventCloseWindow:
            case ChargeSpearConnect:
               break;
            case collideWithWall:
               this.type1 = var1.getCollideType();
               break;
            case EventFishing:
               this.type1 = var1.getVariableString("FishingStage");
               break;
            case EventFitness:
               this.type1 = var1.getVariableString("ExerciseType");
               break;
            case EventUpdateFitness:
               this.type1 = var1.getVariableString("ExerciseHand");
               this.type2 = var1.getVariableString("ExerciseType");
               if (var1.getPrimaryHandItem() != null) {
                  this.type3 = var1.getPrimaryHandItem().getStaticModel();
               }

               if (var1.getSecondaryHandItem() != null && var1.getSecondaryHandItem() != var1.getPrimaryHandItem()) {
                  this.type4 = var1.getSecondaryHandItem().getStaticModel();
               }

               this.flags = (short)(this.flags | (var1.getVariableBoolean("FitnessFinished") ? 4096 : 0));
               break;
            case EventEmote:
               this.type1 = var1.getVariableString("emote");
               break;
            case EventClimbFence:
               if (var1.getVariableBoolean("VaultOverRun")) {
                  this.flags = (short)(this.flags | 2);
               }

               if (var1.getVariableBoolean("VaultOverSprint")) {
                  this.flags = (short)(this.flags | 1);
               }
               break;
            case EventClimbWall:
               this.flags = (short)(this.flags | (var1.isClimbOverWallSuccess() ? 32 : 0));
               this.flags = (short)(this.flags | (var1.isClimbOverWallStruggle() ? 64 : 0));
               break;
            case EventSmashWindow:
               HashMap var11 = var1.getStateMachineParams(SmashWindowState.instance());
               if (var11.get(1) instanceof BaseVehicle && var11.get(2) instanceof VehiclePart) {
                  BaseVehicle var9 = (BaseVehicle)var11.get(1);
                  VehiclePart var10 = (VehiclePart)var11.get(2);
                  this.flags = (short)(this.flags | 2048);
                  this.type1 = String.valueOf(var9.getId());
                  this.type2 = var10.getId();
               }
               break;
            case wasBumped:
               this.type1 = var1.getBumpType();
               this.type2 = var1.getBumpFallType();
               this.flags = (short)(this.flags | (var1.isBumpFall() ? 4 : 0));
               this.flags = (short)(this.flags | (var1.isBumpStaggered() ? 8 : 0));
               if (var1.getBumpedChr() != null) {
                  this.type3 = String.valueOf(var1.getBumpedChr().getOnlineID());
                  this.type4 = var1.getBumpedChr().getBumpType();
                  this.flags = (short)(this.flags | (var1.isHitFromBehind() ? 128 : 0));
                  if (var1.getBumpedChr() instanceof IsoZombie) {
                     this.flags = (short)(this.flags | 256);
                  }
               }
               break;
            case EventOverrideItem:
               if (var1.getNetworkCharacterAI().getAction() == null) {
                  return false;
               }

               BaseAction var8 = var1.getNetworkCharacterAI().getAction();
               this.type1 = var8.getPrimaryHandItem() == null ? var8.getPrimaryHandMdl() : var8.getPrimaryHandItem().getStaticModel();
               this.type2 = var8.getSecondaryHandItem() == null ? var8.getSecondaryHandMdl() : var8.getSecondaryHandItem().getStaticModel();
               break;
            case Update:
               this.flags = (short)(this.flags | (var1.networkAI.isPressedMovement() ? 512 : 0));
               this.flags = (short)(this.flags | (var1.networkAI.isPressedCancelAction() ? 1024 : 0));
               break;
            default:
               DebugLog.Multiplayer.warn("[Event] unknown " + this.getDescription());
               return false;
            }

            var3 = !ClimbDownSheetRopeState.instance().equals(var1.getCurrentState()) && !ClimbSheetRopeState.instance().equals(var1.getCurrentState());
         }
      }

      return var3;
   }

   public static enum EventType {
      EventSetActivatedPrimary,
      EventSetActivatedSecondary,
      EventFishing,
      EventFitness,
      EventEmote,
      EventClimbFence,
      EventClimbDownRope,
      EventClimbRope,
      EventClimbWall,
      EventClimbWindow,
      EventOpenWindow,
      EventCloseWindow,
      EventSmashWindow,
      EventSitOnGround,
      wasBumped,
      collideWithWall,
      EventUpdateFitness,
      EventFallClimb,
      EventOverrideItem,
      ChargeSpearConnect,
      Update,
      Unknown;

      // $FF: synthetic method
      private static EventPacket.EventType[] $values() {
         return new EventPacket.EventType[]{EventSetActivatedPrimary, EventSetActivatedSecondary, EventFishing, EventFitness, EventEmote, EventClimbFence, EventClimbDownRope, EventClimbRope, EventClimbWall, EventClimbWindow, EventOpenWindow, EventCloseWindow, EventSmashWindow, EventSitOnGround, wasBumped, collideWithWall, EventUpdateFitness, EventFallClimb, EventOverrideItem, ChargeSpearConnect, Update, Unknown};
      }
   }
}
