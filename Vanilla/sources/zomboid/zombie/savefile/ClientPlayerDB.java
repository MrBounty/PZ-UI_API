package zombie.savefile;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.characters.IsoPlayer;
import zombie.core.logger.ExceptionLogger;
import zombie.core.network.ByteBufferWriter;
import zombie.core.raknet.UdpConnection;
import zombie.core.utils.UpdateLimit;
import zombie.debug.DebugLog;
import zombie.iso.IsoCell;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoWorld;
import zombie.network.GameClient;
import zombie.network.PacketTypes;

public final class ClientPlayerDB {
   private static ClientPlayerDB instance = null;
   private static boolean allow = false;
   private ClientPlayerDB.NetworkCharacterProfile networkProfile = null;
   private UpdateLimit saveToDBPeriod4Network = new UpdateLimit(30000L);
   private static ByteBuffer SliceBuffer4NetworkPlayer = ByteBuffer.allocate(65536);
   private boolean forceSavePlayers;
   public boolean canSavePlayers = false;
   private int serverPlayerIndex = 1;

   public static void setAllow(boolean var0) {
      allow = var0;
   }

   public static boolean isAllow() {
      return allow;
   }

   public static synchronized ClientPlayerDB getInstance() {
      if (instance == null && allow) {
         instance = new ClientPlayerDB();
      }

      return instance;
   }

   public static boolean isAvailable() {
      return instance != null;
   }

   public void updateMain() {
      this.saveNetworkPlayersToDB();
   }

   public void close() {
      instance = null;
      allow = false;
   }

   private void saveNetworkPlayersToDB() {
      if (this.canSavePlayers && (this.forceSavePlayers || this.saveToDBPeriod4Network.Check())) {
         this.forceSavePlayers = false;

         for(int var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
            IsoPlayer var2 = IsoPlayer.players[var1];
            if (var2 != null) {
               this.clientSendNetworkPlayerInt(var2);
            }
         }
      }

   }

   public ArrayList getAllNetworkPlayers() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 1; var2 < this.networkProfile.playerCount; ++var2) {
         byte[] var3 = this.getClientLoadNetworkPlayerData(var2 + 1);
         ByteBuffer var4 = ByteBuffer.allocate(var3.length);
         var4.rewind();
         var4.put(var3);
         var4.rewind();

         try {
            IsoPlayer var5 = new IsoPlayer(IsoWorld.instance.CurrentCell);
            var5.serverPlayerIndex = var2 + 1;
            var5.load(var4, this.networkProfile.worldVersion);
            var1.add(var5);
         } catch (Exception var6) {
            ExceptionLogger.logException(var6);
         }
      }

      return var1;
   }

   public void clientLoadNetworkCharacter(ByteBuffer var1, UdpConnection var2) {
      boolean var3 = var1.get() == 1;
      int var4 = var1.getInt();
      if (var3) {
         float var5 = var1.getFloat();
         float var6 = var1.getFloat();
         float var7 = var1.getFloat();
         int var8 = var1.getInt();
         boolean var9 = var1.get() == 1;
         int var10 = var1.getInt();
         byte[] var11 = new byte[var10];
         var1.get(var11);
         if (this.networkProfile != null) {
            ++this.networkProfile.playerCount;
            switch(this.networkProfile.playerCount) {
            case 2:
               this.networkProfile.character2 = var11;
               this.networkProfile.x[1] = var5;
               this.networkProfile.y[1] = var6;
               this.networkProfile.z[1] = var7;
               this.networkProfile.isDead[1] = var9;
               break;
            case 3:
               this.networkProfile.character3 = var11;
               this.networkProfile.x[2] = var5;
               this.networkProfile.y[2] = var6;
               this.networkProfile.z[2] = var7;
               this.networkProfile.isDead[2] = var9;
               break;
            case 4:
               this.networkProfile.character4 = var11;
               this.networkProfile.x[3] = var5;
               this.networkProfile.y[3] = var6;
               this.networkProfile.z[3] = var7;
               this.networkProfile.isDead[3] = var9;
            }
         } else {
            this.networkProfile = new ClientPlayerDB.NetworkCharacterProfile();
            this.networkProfile.playerCount = 1;
            this.networkProfile.username = GameClient.username;
            this.networkProfile.server = GameClient.ip;
            this.networkProfile.character1 = var11;
            this.networkProfile.worldVersion = var8;
            this.networkProfile.x[0] = var5;
            this.networkProfile.y[0] = var6;
            this.networkProfile.z[0] = var7;
            this.networkProfile.isDead[0] = var9;
         }

         ByteBufferWriter var12 = GameClient.connection.startPacket();
         PacketTypes.PacketType.LoadPlayerProfile.doPacket(var12);
         var12.putByte((byte)(var4 + 1));
         PacketTypes.PacketType.LoadPlayerProfile.send(GameClient.connection);
      } else if (this.networkProfile != null) {
         this.networkProfile.isLoaded = true;
         this.serverPlayerIndex = this.networkProfile.playerCount;
      } else {
         this.networkProfile = new ClientPlayerDB.NetworkCharacterProfile();
         this.networkProfile.isLoaded = true;
         this.networkProfile.playerCount = 0;
         this.networkProfile.username = GameClient.username;
         this.networkProfile.server = GameClient.ip;
         this.networkProfile.character1 = null;
         this.networkProfile.worldVersion = IsoWorld.getWorldVersion();
      }

   }

   private boolean isClientLoadNetworkCharacterCompleted() {
      return this.networkProfile != null && this.networkProfile.isLoaded;
   }

   public void clientSendNetworkPlayerInt(IsoPlayer var1) {
      if (GameClient.connection != null) {
         try {
            ByteBufferWriter var2 = GameClient.connection.startPacket();
            PacketTypes.PacketType.SendPlayerProfile.doPacket(var2);
            var2.putByte((byte)(var1.serverPlayerIndex - 1));
            var2.putFloat(var1.x);
            var2.putFloat(var1.y);
            var2.putFloat(var1.z);
            var2.putByte((byte)(var1.isDead() ? 1 : 0));
            SliceBuffer4NetworkPlayer.rewind();
            var1.save(SliceBuffer4NetworkPlayer);
            byte[] var6 = new byte[SliceBuffer4NetworkPlayer.position()];
            SliceBuffer4NetworkPlayer.rewind();
            SliceBuffer4NetworkPlayer.get(var6);
            var2.putInt(IsoWorld.getWorldVersion());
            var2.putInt(SliceBuffer4NetworkPlayer.position());
            var2.bb.put(var6);
            PacketTypes.PacketType.SendPlayerProfile.send(GameClient.connection);
         } catch (IOException var4) {
            GameClient.connection.cancelPacket();
            ExceptionLogger.logException(var4);
         } catch (BufferOverflowException var5) {
            GameClient.connection.cancelPacket();
            int var3 = SliceBuffer4NetworkPlayer.capacity();
            if (var3 > 2097152) {
               DebugLog.log("FATAL ERROR: The player " + var1.getUsername() + " cannot be saved");
               ExceptionLogger.logException(var5);
               return;
            }

            SliceBuffer4NetworkPlayer = ByteBuffer.allocate(var3 * 2);
            this.clientSendNetworkPlayerInt(var1);
         }

      }
   }

   public boolean isAliveMainNetworkPlayer() {
      return !this.networkProfile.isDead[0];
   }

   public boolean clientLoadNetworkPlayer() {
      if (this.networkProfile != null && this.networkProfile.isLoaded && this.networkProfile.username.equals(GameClient.username) && this.networkProfile.server.equals(GameClient.ip)) {
         return this.networkProfile.playerCount > 0;
      } else if (GameClient.connection == null) {
         return false;
      } else {
         if (this.networkProfile != null) {
            this.networkProfile = null;
         }

         ByteBufferWriter var1 = GameClient.connection.startPacket();
         PacketTypes.PacketType.LoadPlayerProfile.doPacket(var1);
         var1.putByte((byte)0);
         PacketTypes.PacketType.LoadPlayerProfile.send(GameClient.connection);
         int var2 = 200;

         while(var2-- > 0) {
            if (this.isClientLoadNetworkCharacterCompleted()) {
               return this.networkProfile.playerCount > 0;
            }

            try {
               Thread.sleep(50L);
            } catch (InterruptedException var4) {
               ExceptionLogger.logException(var4);
            }
         }

         return false;
      }
   }

   public byte[] getClientLoadNetworkPlayerData(int var1) {
      if (this.networkProfile != null && this.networkProfile.isLoaded && this.networkProfile.username.equals(GameClient.username) && this.networkProfile.server.equals(GameClient.ip)) {
         switch(var1) {
         case 1:
            return this.networkProfile.character1;
         case 2:
            return this.networkProfile.character2;
         case 3:
            return this.networkProfile.character3;
         case 4:
            return this.networkProfile.character4;
         default:
            return null;
         }
      } else if (!this.clientLoadNetworkPlayer()) {
         return null;
      } else {
         switch(var1) {
         case 1:
            return this.networkProfile.character1;
         case 2:
            return this.networkProfile.character2;
         case 3:
            return this.networkProfile.character3;
         case 4:
            return this.networkProfile.character4;
         default:
            return null;
         }
      }
   }

   public boolean loadNetworkPlayer(int var1) {
      try {
         byte[] var2 = this.getClientLoadNetworkPlayerData(var1);
         if (var2 != null) {
            ByteBuffer var3 = ByteBuffer.allocate(var2.length);
            var3.rewind();
            var3.put(var2);
            var3.rewind();
            if (var1 == 1) {
               if (IsoPlayer.getInstance() == null) {
                  IsoPlayer.setInstance(new IsoPlayer(IsoCell.getInstance()));
                  IsoPlayer.players[0] = IsoPlayer.getInstance();
               }

               IsoPlayer.getInstance().serverPlayerIndex = 1;
               IsoPlayer.getInstance().load(var3, this.networkProfile.worldVersion);
            } else {
               IsoPlayer.players[var1 - 1] = new IsoPlayer(IsoCell.getInstance());
               IsoPlayer.getInstance().serverPlayerIndex = var1;
               IsoPlayer.getInstance().load(var3, this.networkProfile.worldVersion);
            }

            return true;
         }
      } catch (Exception var4) {
         ExceptionLogger.logException(var4);
      }

      return false;
   }

   public boolean loadNetworkPlayerInfo(int var1) {
      if (this.networkProfile != null && this.networkProfile.isLoaded && this.networkProfile.username.equals(GameClient.username) && this.networkProfile.server.equals(GameClient.ip) && var1 >= 1 && var1 <= 4 && var1 <= this.networkProfile.playerCount) {
         int var2 = (int)(this.networkProfile.x[var1 - 1] / 10.0F) + IsoWorld.saveoffsetx * 30;
         int var3 = (int)(this.networkProfile.y[var1 - 1] / 10.0F) + IsoWorld.saveoffsety * 30;
         IsoChunkMap.WorldXA = (int)this.networkProfile.x[var1 - 1];
         IsoChunkMap.WorldYA = (int)this.networkProfile.y[var1 - 1];
         IsoChunkMap.WorldZA = (int)this.networkProfile.z[var1 - 1];
         IsoChunkMap.WorldXA += 300 * IsoWorld.saveoffsetx;
         IsoChunkMap.WorldYA += 300 * IsoWorld.saveoffsety;
         IsoChunkMap.SWorldX[0] = var2;
         IsoChunkMap.SWorldY[0] = var3;
         int[] var10000 = IsoChunkMap.SWorldX;
         var10000[0] += 30 * IsoWorld.saveoffsetx;
         var10000 = IsoChunkMap.SWorldY;
         var10000[0] += 30 * IsoWorld.saveoffsety;
         return true;
      } else {
         return false;
      }
   }

   public int getNextServerPlayerIndex() {
      return ++this.serverPlayerIndex;
   }

   private final class NetworkCharacterProfile {
      boolean isLoaded = false;
      byte[] character1;
      byte[] character2;
      byte[] character3;
      byte[] character4;
      String username;
      String server;
      int playerCount = 0;
      int worldVersion;
      float[] x = new float[4];
      float[] y = new float[4];
      float[] z = new float[4];
      boolean[] isDead = new boolean[4];

      public NetworkCharacterProfile() {
      }
   }
}
