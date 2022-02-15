package zombie.popman;

import com.google.common.collect.Sets;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.Sets.SetView;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import zombie.VirtualZombieManager;
import zombie.ai.State;
import zombie.ai.states.ZombieHitReactionState;
import zombie.ai.states.ZombieOnGroundState;
import zombie.characters.IsoPlayer;
import zombie.characters.IsoZombie;
import zombie.characters.NetworkZombieVariables;
import zombie.core.Core;
import zombie.core.network.ByteBufferWriter;
import zombie.core.utils.UpdateLimit;
import zombie.debug.DebugLog;
import zombie.debug.DebugOptions;
import zombie.debug.DebugType;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoWorld;
import zombie.iso.objects.IsoDeadBody;
import zombie.network.GameClient;
import zombie.network.PacketTypes;
import zombie.network.packets.ZombiePacket;

public class NetworkZombieSimulator {
   public static final int MAX_ZOMBIES_PER_UPDATE = 300;
   private static final NetworkZombieSimulator instance = new NetworkZombieSimulator();
   private static final ZombiePacket zombiePacket = new ZombiePacket();
   private final ByteBuffer bb = ByteBuffer.allocate(1000000);
   private final ArrayList unknownZombies = new ArrayList();
   private final HashSet authoriseZombies = new HashSet();
   private final ArrayDeque SendQueue = new ArrayDeque();
   private final ArrayDeque ExtraSendQueue = new ArrayDeque();
   private HashSet authoriseZombiesCurrent = new HashSet();
   private HashSet authoriseZombiesLast = new HashSet();
   UpdateLimit ZombieSimulationReliableLimit = new UpdateLimit(1000L);

   public static NetworkZombieSimulator getInstance() {
      return instance;
   }

   public int getAuthorizedZombieCount() {
      return (int)IsoWorld.instance.CurrentCell.getZombieList().stream().filter((var0) -> {
         return var0.authOwner == GameClient.connection;
      }).count();
   }

   public int getUnauthorizedZombieCount() {
      return (int)IsoWorld.instance.CurrentCell.getZombieList().stream().filter((var0) -> {
         return var0.authOwner == null;
      }).count();
   }

   public void clear() {
      HashSet var1 = this.authoriseZombiesCurrent;
      this.authoriseZombiesCurrent = this.authoriseZombiesLast;
      this.authoriseZombiesLast = var1;
      this.authoriseZombiesLast.removeIf((var0) -> {
         return GameClient.getZombie(var0) == null;
      });
      this.authoriseZombiesCurrent.clear();
   }

   public void addExtraUpdate(IsoZombie var1) {
      if (var1.authOwner == GameClient.connection && !this.ExtraSendQueue.contains(var1)) {
         this.ExtraSendQueue.add(var1);
      }

   }

   public void add(short var1) {
      this.authoriseZombiesCurrent.add(var1);
   }

   public void added() {
      SetView var1 = Sets.difference(this.authoriseZombiesCurrent, this.authoriseZombiesLast);
      UnmodifiableIterator var2 = var1.iterator();

      while(true) {
         while(var2.hasNext()) {
            Short var3 = (Short)var2.next();
            IsoZombie var4 = GameClient.getZombie(var3);
            if (var4 != null && var4.OnlineID == var3) {
               this.becomeLocal(var4);
            } else if (!this.unknownZombies.contains(var3)) {
               this.unknownZombies.add(var3);
            }
         }

         SetView var8 = Sets.difference(this.authoriseZombiesLast, this.authoriseZombiesCurrent);
         UnmodifiableIterator var9 = var8.iterator();

         while(var9.hasNext()) {
            Short var10 = (Short)var9.next();
            IsoZombie var5 = GameClient.getZombie(var10);
            if (var5 != null) {
               this.becomeRemote(var5);
            }
         }

         synchronized(this.authoriseZombies) {
            this.authoriseZombies.clear();
            this.authoriseZombies.addAll(this.authoriseZombiesCurrent);
            return;
         }
      }
   }

   public void becomeLocal(IsoZombie var1) {
      var1.lastRemoteUpdate = 0;
      var1.authOwner = GameClient.connection;
      var1.authOwnerPlayer = IsoPlayer.getInstance();
      var1.networkAI.setUpdateTimer(0.0F);
      var1.AllowRepathDelay = 0.0F;
      var1.networkAI.mindSync.restorePFBTarget();
   }

   public void becomeRemote(IsoZombie var1) {
      if (var1.isDead() && var1.authOwner == GameClient.connection) {
         var1.getNetworkCharacterAI().setLocal(true);
      }

      var1.lastRemoteUpdate = 0;
      var1.authOwner = null;
      var1.authOwnerPlayer = null;
      if (var1.group != null) {
         var1.group.remove(var1);
      }

   }

   public boolean isZombieSimulated(Short var1) {
      synchronized(this.authoriseZombies) {
         return this.authoriseZombies.contains(var1);
      }
   }

   public void receivePacket(ByteBuffer var1) {
      if (DebugOptions.instance.Network.Client.UpdateZombiesFromPacket.getValue()) {
         short var2 = var1.getShort();

         for(short var3 = 0; var3 < var2; ++var3) {
            this.parseZombie(var1);
         }

      }
   }

   private void parseZombie(ByteBuffer var1) {
      ZombiePacket var2 = zombiePacket;
      var2.parse(var1);
      if (var2.id == -1) {
         DebugLog.General.error("NetworkZombieSimulator.parseZombie id=" + var2.id);
      } else {
         try {
            IsoZombie var3 = (IsoZombie)GameClient.IDToZombieMap.get(var2.id);
            if (var3 == null) {
               if (IsoDeadBody.isDead(var2.id)) {
                  DebugLog.log(DebugType.Multiplayer, "Skip dead zombie creation id=" + var2.id);
                  return;
               }

               IsoGridSquare var4 = IsoWorld.instance.CurrentCell.getGridSquare((double)var2.realX, (double)var2.realY, (double)var2.realZ);
               if (var4 != null) {
                  VirtualZombieManager.instance.choices.clear();
                  VirtualZombieManager.instance.choices.add(var4);
                  var3 = VirtualZombieManager.instance.createRealZombieAlways(var2.descriptorID, IsoDirections.getRandom().index(), false);
                  DebugLog.log(DebugType.ActionSystem, "ParseZombie: CreateRealZombieAlways id=" + var2.id);
                  if (var3 != null) {
                     var3.setFakeDead(false);
                     var3.OnlineID = var2.id;
                     GameClient.IDToZombieMap.put(var2.id, var3);
                     var3.lx = var3.nx = var3.x = var2.realX;
                     var3.ly = var3.ny = var3.y = var2.realY;
                     var3.lz = var3.z = (float)var2.realZ;
                     var3.setForwardDirection(var3.dir.ToVector());
                     var3.setCurrent(var4);
                     var3.networkAI.targetX = var2.x;
                     var3.networkAI.targetY = var2.y;
                     var3.networkAI.targetZ = var2.z;
                     var3.networkAI.predictionType = var2.moveType;
                     NetworkZombieVariables.setInt(var3, (short)0, var2.realHealth);
                     NetworkZombieVariables.setInt(var3, (short)2, var2.speedMod);
                     NetworkZombieVariables.setInt(var3, (short)1, var2.target);
                     NetworkZombieVariables.setInt(var3, (short)3, var2.timeSinceSeenFlesh);
                     NetworkZombieVariables.setInt(var3, (short)4, var2.smParamTargetAngle);
                     NetworkZombieVariables.setBooleanVariables(var3, var2.booleanVariables);
                     if (var3.isKnockedDown()) {
                        var3.setOnFloor(true);
                        var3.changeState(ZombieOnGroundState.instance());
                     }

                     var3.setWalkType(var2.walkType.toString());
                     var3.realState = var2.realState;
                     if (var3.isReanimatedPlayer()) {
                        var3.getStateMachine().changeState((State)null, (Iterable)null);
                     }

                     for(int var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
                        IsoPlayer var6 = IsoPlayer.players[var5];
                        if (var4.isCanSee(var5)) {
                           var3.setAlphaAndTarget(var5, 1.0F);
                        }

                        if (var6 != null && var6.ReanimatedCorpseID == var2.id && var2.id != -1) {
                           var6.ReanimatedCorpseID = -1;
                           var6.ReanimatedCorpse = var3;
                        }
                     }

                     var3.networkAI.mindSync.parse(var2);
                  } else {
                     DebugLog.log("Error: VirtualZombieManager can't create zombie");
                  }
               }

               if (var3 == null) {
                  return;
               }
            }

            if (getInstance().isZombieSimulated(var3.OnlineID)) {
               var3.authOwner = GameClient.connection;
               var3.authOwnerPlayer = IsoPlayer.getInstance();
               return;
            }

            var3.authOwner = null;
            var3.authOwnerPlayer = null;
            if (!var3.networkAI.isSetVehicleHit() || !var3.isCurrentState(ZombieHitReactionState.instance())) {
               var3.networkAI.parse(var2);
               var3.networkAI.mindSync.parse(var2);
            }

            var3.lastRemoteUpdate = 0;
            if (!IsoWorld.instance.CurrentCell.getZombieList().contains(var3)) {
               IsoWorld.instance.CurrentCell.getZombieList().add(var3);
            }

            if (!IsoWorld.instance.CurrentCell.getObjectList().contains(var3)) {
               IsoWorld.instance.CurrentCell.getObjectList().add(var3);
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   public boolean anyUnknownZombies() {
      return this.unknownZombies.size() > 0;
   }

   public void send() {
      if (this.authoriseZombies.size() != 0 || this.unknownZombies.size() != 0) {
         IsoZombie var4;
         if (this.SendQueue.isEmpty()) {
            synchronized(this.authoriseZombies) {
               Iterator var2 = this.authoriseZombies.iterator();

               while(var2.hasNext()) {
                  Short var3 = (Short)var2.next();
                  var4 = GameClient.getZombie(var3);
                  if (var4 != null && var4.OnlineID != -1) {
                     this.SendQueue.add(var4);
                  }
               }
            }
         }

         this.bb.clear();
         int var9;
         int var10;
         synchronized(ZombieCountOptimiser.zombiesForDelete) {
            var9 = ZombieCountOptimiser.zombiesForDelete.size();
            this.bb.putShort((short)var9);
            var10 = 0;

            while(true) {
               if (var10 >= var9) {
                  ZombieCountOptimiser.zombiesForDelete.clear();
                  break;
               }

               this.bb.putShort(((IsoZombie)ZombieCountOptimiser.zombiesForDelete.get(var10)).OnlineID);
               ++var10;
            }
         }

         int var1 = this.unknownZombies.size();
         this.bb.putShort((short)var1);

         for(var9 = 0; var9 < var1; ++var9) {
            this.bb.putShort((Short)this.unknownZombies.get(var9));
         }

         this.unknownZombies.clear();
         var9 = this.bb.position();
         this.bb.putShort((short)300);
         var10 = 0;

         while(!this.SendQueue.isEmpty()) {
            var4 = (IsoZombie)this.SendQueue.poll();
            this.ExtraSendQueue.remove(var4);
            var4.zombiePacket.set(var4);
            if (var4.OnlineID != -1) {
               var4.zombiePacket.write(this.bb);
               var4.networkAI.targetX = var4.realx = var4.x;
               var4.networkAI.targetY = var4.realy = var4.y;
               var4.networkAI.targetZ = var4.realz = (byte)((int)var4.z);
               var4.realdir = var4.getDir();
               ++var10;
               if (var10 >= 300) {
                  break;
               }
            }
         }

         int var11;
         if (var10 < 300) {
            var11 = this.bb.position();
            this.bb.position(var9);
            this.bb.putShort((short)var10);
            this.bb.position(var11);
         }

         if (var10 > 0 || var1 > 0) {
            ByteBufferWriter var12 = GameClient.connection.startPacket();
            PacketTypes.PacketType var5;
            if (var1 > 0 && this.ZombieSimulationReliableLimit.Check()) {
               var5 = PacketTypes.PacketType.ZombieSimulationReliable;
            } else {
               var5 = PacketTypes.PacketType.ZombieSimulation;
            }

            var5.doPacket(var12);
            var12.bb.put(this.bb.array(), 0, this.bb.position());
            var5.send(GameClient.connection);
         }

         if (!this.ExtraSendQueue.isEmpty()) {
            this.bb.clear();
            this.bb.putShort((short)0);
            this.bb.putShort((short)0);
            var9 = this.bb.position();
            this.bb.putShort((short)0);
            var11 = 0;

            while(!this.ExtraSendQueue.isEmpty()) {
               IsoZombie var13 = (IsoZombie)this.ExtraSendQueue.poll();
               var13.zombiePacket.set(var13);
               if (var13.OnlineID != -1) {
                  var13.zombiePacket.write(this.bb);
                  var13.networkAI.targetX = var13.realx = var13.x;
                  var13.networkAI.targetY = var13.realy = var13.y;
                  var13.networkAI.targetZ = var13.realz = (byte)((int)var13.z);
                  var13.realdir = var13.getDir();
                  ++var11;
               }
            }

            int var14 = this.bb.position();
            this.bb.position(var9);
            this.bb.putShort((short)var11);
            this.bb.position(var14);
            if (var11 > 0) {
               ByteBufferWriter var6 = GameClient.connection.startPacket();
               PacketTypes.PacketType.ZombieSimulation.doPacket(var6);
               var6.bb.put(this.bb.array(), 0, this.bb.position());
               PacketTypes.PacketType.ZombieSimulation.send(GameClient.connection);
            }
         }

      }
   }

   public void remove(IsoZombie var1) {
      if (var1 != null && var1.OnlineID != -1) {
         GameClient.IDToZombieMap.remove(var1.OnlineID);
      }
   }

   public void clearTargetAuth(IsoPlayer var1) {
      if (Core.bDebug) {
         DebugLog.log(DebugType.Multiplayer, "Clear zombies target and auth for player id=" + var1.getOnlineID());
      }

      if (GameClient.bClient) {
         Iterator var2 = GameClient.IDToZombieMap.valueCollection().iterator();

         while(var2.hasNext()) {
            IsoZombie var3 = (IsoZombie)var2.next();
            if (var3.target == var1) {
               var3.setTarget((IsoMovingObject)null);
            }
         }
      }

   }
}
