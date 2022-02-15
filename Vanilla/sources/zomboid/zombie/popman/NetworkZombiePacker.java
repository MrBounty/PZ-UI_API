package zombie.popman;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import zombie.VirtualZombieManager;
import zombie.characters.IsoZombie;
import zombie.characters.NetworkZombieVariables;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.core.utils.UpdateLimit;
import zombie.debug.DebugLog;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoWorld;
import zombie.network.GameServer;
import zombie.network.MPStatistics;
import zombie.network.PacketTypes;
import zombie.network.ServerMap;
import zombie.network.packets.ZombiePacket;

public class NetworkZombiePacker {
   private static final NetworkZombiePacker instance = new NetworkZombiePacker();
   private final ArrayList zombiesDeleted = new ArrayList();
   private final ArrayList zombiesDeletedForSending = new ArrayList();
   private final HashSet zombiesReceived = new HashSet();
   private final ArrayList zombiesProcessing = new ArrayList();
   private final NetworkZombieList zombiesRequest = new NetworkZombieList();
   private final ZombiePacket packet = new ZombiePacket();
   private HashSet extraUpdate = new HashSet();
   private final ByteBuffer bb = ByteBuffer.allocate(1000000);
   UpdateLimit ZombieSimulationReliableLimit = new UpdateLimit(5000L);

   public static NetworkZombiePacker getInstance() {
      return instance;
   }

   public void setExtraUpdate() {
      for(int var1 = 0; var1 < GameServer.udpEngine.connections.size(); ++var1) {
         UdpConnection var2 = (UdpConnection)GameServer.udpEngine.connections.get(var1);
         if (var2.isFullyConnected()) {
            this.extraUpdate.add(var2);
         }
      }

   }

   public void deleteZombie(IsoZombie var1) {
      synchronized(this.zombiesDeleted) {
         this.zombiesDeleted.add(new NetworkZombiePacker.DeletedZombie(var1.OnlineID, var1.x, var1.y));
      }
   }

   public void receivePacket(ByteBuffer var1, UdpConnection var2) {
      short var3 = var1.getShort();

      short var5;
      for(int var4 = 0; var4 < var3; ++var4) {
         var5 = var1.getShort();
         IsoZombie var6 = ServerMap.instance.ZombieMap.get(var5);
         if (var6 != null) {
            this.deleteZombie(var6);
            DebugLog.Multiplayer.debugln("Zombie was deleted ID:" + var6.OnlineID + " (" + var6.x + ", " + var6.y + ")");
            VirtualZombieManager.instance.removeZombieFromWorld(var6);
            MPStatistics.serverZombieCulled();
         }
      }

      short var8 = var1.getShort();

      for(int var9 = 0; var9 < var8; ++var9) {
         short var10 = var1.getShort();
         IsoZombie var7 = ServerMap.instance.ZombieMap.get((short)var10);
         if (var7 != null) {
            this.zombiesRequest.getNetworkZombie(var2).zombies.add(var7);
         }
      }

      var5 = var1.getShort();

      for(int var11 = 0; var11 < var5; ++var11) {
         this.parseZombie(var1, var2);
      }

   }

   public void parseZombie(ByteBuffer var1, UdpConnection var2) {
      this.packet.parse(var1);
      if (this.packet.id == -1) {
         DebugLog.General.error("NetworkZombiePacker.parseZombie id=" + this.packet.id);
      } else {
         try {
            IsoZombie var3 = ServerMap.instance.ZombieMap.get(this.packet.id);
            if (var3 == null) {
               return;
            }

            if (var3.authOwner != var2) {
               NetworkZombieManager.getInstance().recheck(var2);
               this.extraUpdate.add(var2);
               return;
            }

            this.applyZombie(var3);
            var3.lastRemoteUpdate = 0;
            if (!IsoWorld.instance.CurrentCell.getZombieList().contains(var3)) {
               IsoWorld.instance.CurrentCell.getZombieList().add(var3);
            }

            if (!IsoWorld.instance.CurrentCell.getObjectList().contains(var3)) {
               IsoWorld.instance.CurrentCell.getObjectList().add(var3);
            }

            var3.zombiePacket.copy(this.packet);
            var3.zombiePacketUpdated = true;
            synchronized(this.zombiesReceived) {
               this.zombiesReceived.add(var3);
            }
         } catch (Exception var7) {
            var7.printStackTrace();
         }

      }
   }

   public void postupdate() {
      this.updateAuth();
      synchronized(this.zombiesReceived) {
         this.zombiesProcessing.clear();
         this.zombiesProcessing.addAll(this.zombiesReceived);
         this.zombiesReceived.clear();
      }

      synchronized(this.zombiesDeleted) {
         this.zombiesDeletedForSending.clear();
         this.zombiesDeletedForSending.addAll(this.zombiesDeleted);
         this.zombiesDeleted.clear();
      }

      Iterator var1 = GameServer.udpEngine.connections.iterator();

      while(var1.hasNext()) {
         UdpConnection var2 = (UdpConnection)var1.next();
         if (var2 != null && var2.isFullyConnected()) {
            this.send(var2);
         }
      }

   }

   private void updateAuth() {
      ArrayList var1 = IsoWorld.instance.CurrentCell.getZombieList();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         IsoZombie var3 = (IsoZombie)var1.get(var2);
         NetworkZombieManager.getInstance().updateAuth(var3);
      }

   }

   public int getZombieData(UdpConnection var1, ByteBuffer var2) {
      int var3 = var2.position();
      var2.putShort((short)300);
      int var4 = 0;

      try {
         NetworkZombieList.NetworkZombie var5 = this.zombiesRequest.getNetworkZombie(var1);

         while(!var5.zombies.isEmpty()) {
            IsoZombie var6 = (IsoZombie)var5.zombies.poll();
            var6.zombiePacket.set(var6);
            if (var6.OnlineID != -1) {
               var6.zombiePacket.write(var2);
               var6.zombiePacketUpdated = false;
               ++var4;
               if (var4 >= 300) {
                  break;
               }
            }
         }

         int var9;
         for(var9 = 0; var9 < this.zombiesProcessing.size(); ++var9) {
            IsoZombie var7 = (IsoZombie)this.zombiesProcessing.get(var9);
            if (var7.authOwner != null && var7.authOwner != var1 && var1.RelevantTo(var7.x, var7.y, (float)((var1.ReleventRange - 2) * 10)) && var7.OnlineID != -1) {
               var7.zombiePacket.write(var2);
               var7.zombiePacketUpdated = false;
               ++var4;
            }
         }

         var9 = var2.position();
         var2.position(var3);
         var2.putShort((short)var4);
         var2.position(var9);
      } catch (BufferOverflowException var8) {
         var8.printStackTrace();
      }

      return var4;
   }

   public void send(UdpConnection var1) {
      this.bb.clear();
      this.bb.put((byte)(var1.isNeighborPlayer ? 1 : 0));
      int var2 = this.bb.position();
      short var3 = 0;
      this.bb.putShort((short)0);
      Iterator var4 = this.zombiesDeletedForSending.iterator();

      while(var4.hasNext()) {
         NetworkZombiePacker.DeletedZombie var5 = (NetworkZombiePacker.DeletedZombie)var4.next();
         if (var1.RelevantTo(var5.x, var5.y)) {
            ++var3;
            this.bb.putShort(var5.OnlineID);
         }
      }

      int var8 = this.bb.position();
      this.bb.position(var2);
      this.bb.putShort(var3);
      this.bb.position(var8);
      NetworkZombieManager.getInstance().getZombieAuth(var1, this.bb);
      int var9 = this.getZombieData(var1, this.bb);
      if (var9 > 0 || var1.timerSendZombie.check() || this.extraUpdate.contains(var1)) {
         this.extraUpdate.remove(var1);
         var1.timerSendZombie.reset(3800L);
         ByteBufferWriter var6 = var1.startPacket();
         PacketTypes.PacketType var7;
         if (this.ZombieSimulationReliableLimit.Check()) {
            var7 = PacketTypes.PacketType.ZombieSimulationReliable;
         } else {
            var7 = PacketTypes.PacketType.ZombieSimulation;
         }

         var7.doPacket(var6);
         var6.bb.put(this.bb.array(), 0, this.bb.position());
         var7.send(var1);
      }
   }

   private void applyZombie(IsoZombie var1) {
      IsoGridSquare var2 = IsoWorld.instance.CurrentCell.getGridSquare((int)this.packet.x, (int)this.packet.y, this.packet.z);
      var1.lx = var1.nx = var1.x = this.packet.realX;
      var1.ly = var1.ny = var1.y = this.packet.realY;
      var1.lz = var1.z = (float)this.packet.realZ;
      var1.setForwardDirection(var1.dir.ToVector());
      var1.setCurrent(var2);
      var1.networkAI.targetX = this.packet.x;
      var1.networkAI.targetY = this.packet.y;
      var1.networkAI.targetZ = this.packet.z;
      var1.networkAI.predictionType = this.packet.moveType;
      NetworkZombieVariables.setInt(var1, (short)0, this.packet.realHealth);
      NetworkZombieVariables.setInt(var1, (short)2, this.packet.speedMod);
      NetworkZombieVariables.setInt(var1, (short)1, this.packet.target);
      NetworkZombieVariables.setInt(var1, (short)3, this.packet.timeSinceSeenFlesh);
      NetworkZombieVariables.setInt(var1, (short)4, this.packet.smParamTargetAngle);
      NetworkZombieVariables.setBooleanVariables(var1, this.packet.booleanVariables);
      var1.setWalkType(this.packet.walkType.toString());
      var1.realState = this.packet.realState;
   }

   class DeletedZombie {
      short OnlineID;
      float x;
      float y;

      public DeletedZombie(short var2, float var3, float var4) {
         this.OnlineID = var2;
         this.x = var3;
         this.y = var4;
      }
   }
}
