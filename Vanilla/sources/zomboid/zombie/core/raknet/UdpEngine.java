package zombie.core.raknet;

import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import zombie.Lua.LuaEventManager;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.ThreadGroups;
import zombie.core.Translator;
import zombie.core.network.ByteBufferWriter;
import zombie.core.secure.PZcrypt;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.debug.LogSeverity;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.PacketTypes;
import zombie.network.ServerWorldDatabase;

public class UdpEngine {
   private int maxConnections = 0;
   private final Map connectionMap = new HashMap();
   public final List connections = new ArrayList();
   protected final RakNetPeerInterface peer;
   final boolean bServer;
   Lock bufferLock = new ReentrantLock();
   private ByteBuffer bb = ByteBuffer.allocate(500000);
   private ByteBufferWriter bbw;
   public int port;
   private final Thread thread;
   private boolean bQuit;
   UdpConnection[] connectionArray;
   ByteBuffer buf;

   public UdpEngine(int var1, int var2, String var3, boolean var4) throws ConnectException {
      this.bbw = new ByteBufferWriter(this.bb);
      this.port = 0;
      this.connectionArray = new UdpConnection[256];
      this.buf = ByteBuffer.allocate(1000000);
      this.port = var1;
      this.peer = new RakNetPeerInterface();
      DebugLog.Network.println("Initialising RakNet...");
      this.peer.Init(SteamUtils.isSteamModeEnabled());
      this.peer.SetMaximumIncomingConnections(var2);
      this.bServer = var4;
      if (this.bServer) {
         if (GameServer.IPCommandline != null) {
            this.peer.SetServerIP(GameServer.IPCommandline);
         }

         this.peer.SetServerPort(var1);
         this.peer.SetIncomingPassword(this.hashServerPassword(var3));
      } else {
         this.peer.SetClientPort(GameServer.DEFAULT_PORT + Rand.Next(10000) + 1234);
      }

      this.peer.SetOccasionalPing(true);
      this.maxConnections = var2;
      int var5 = this.peer.Startup(var2);
      DebugLog.Network.println("RakNet.Startup() return code: %s (0 means success)", var5);
      if (var5 != 0) {
         throw new ConnectException("Connection Startup Failed. Code: " + var5);
      } else {
         if (var4) {
            VoiceManager.instance.InitVMServer();
         }

         this.thread = new Thread(ThreadGroups.Network, this::threadRun, "UdpEngine");
         this.thread.setDaemon(true);
         this.thread.start();
      }
   }

   private void threadRun() {
      while(true) {
         if (!this.bQuit) {
            ByteBuffer var1 = this.Receive();
            if (!this.bQuit) {
               try {
                  this.decode(var1);
               } catch (Exception var3) {
                  DebugLog.Network.printException(var3, "Exception thrown during decode.", LogSeverity.Error);
               }
               continue;
            }
         }

         return;
      }
   }

   public void Shutdown() {
      DebugLog.log("waiting for UdpEngine thread termination");
      this.bQuit = true;

      while(this.thread.isAlive()) {
         try {
            Thread.sleep(10L);
         } catch (InterruptedException var2) {
         }
      }

      this.peer.Shutdown();
   }

   public void SetServerPassword(String var1) {
      if (this.peer != null) {
         this.peer.SetIncomingPassword(var1);
      }

   }

   public String hashServerPassword(String var1) {
      return PZcrypt.hash(var1, true);
   }

   public String getServerIP() {
      return this.peer.GetServerIP();
   }

   public long getClientSteamID(long var1) {
      return this.peer.GetClientSteamID(var1);
   }

   public long getClientOwnerSteamID(long var1) {
      return this.peer.GetClientOwnerSteamID(var1);
   }

   public ByteBufferWriter startPacket() {
      this.bufferLock.lock();
      this.bb.clear();
      return this.bbw;
   }

   public void endPacketBroadcast(PacketTypes.PacketType var1) {
      this.bb.flip();
      this.peer.Send(this.bb, var1.PacketPriority, var1.PacketPriority, (byte)0, -1L, true);
      this.bufferLock.unlock();
   }

   public void endPacketBroadcastExcept(int var1, int var2, UdpConnection var3) {
      this.bb.flip();
      this.peer.Send(this.bb, var1, var2, (byte)0, var3.connectedGUID, true);
      this.bufferLock.unlock();
   }

   private void decode(ByteBuffer var1) {
      int var2 = var1.get() & 255;
      long var4;
      UdpConnection var6;
      long var8;
      int var9;
      switch(var2) {
      case 0:
      case 1:
      case 20:
      case 25:
      case 31:
      case 33:
         break;
      case 16:
         DebugLog.Network.println("Connection Request Accepted");
         var9 = var1.get() & 255;
         var4 = this.peer.getGuidOfPacket();
         var6 = this.addConnection(var9, var4);
         VoiceManager.instance.VoiceConnectReq(var4);
         ByteBufferWriter var7;
         if (GameClient.bClient && !GameClient.askPing) {
            GameClient.startAuth = Calendar.getInstance();
            GameClient.connection = var6;
            var7 = var6.startPacket();
            PacketTypes.PacketType.Login.doPacket(var7);
            var7.putUTF(GameClient.username);
            var7.putUTF(PZcrypt.hash(ServerWorldDatabase.encrypt(GameClient.password)));
            var7.putUTF(Core.getInstance().getVersionNumber());
            PacketTypes.PacketType.Login.send(var6);
         } else if (GameClient.bClient && GameClient.askPing) {
            GameClient.connection = var6;
            var7 = var6.startPacket();
            PacketTypes.PacketType.Ping.doPacket(var7);
            var7.putUTF(GameClient.ip);
            PacketTypes.PacketType.Ping.send(var6);
         }
         break;
      case 17:
         if (GameClient.bClient) {
            GameClient.instance.addDisconnectPacket(var2);
         }
         break;
      case 18:
         DebugLog.Network.println("User Already Connected");
         if (GameClient.bClient) {
            GameClient.instance.addDisconnectPacket(var2);
         }
         break;
      case 19:
         var9 = var1.get() & 255;
         var4 = this.peer.getGuidOfPacket();
         this.addConnection(var9, var4);
         break;
      case 21:
         var9 = var1.get() & 255;
         var4 = this.peer.getGuidOfPacket();
         VoiceManager.instance.VoiceConnectClose(var4);
         this.removeConnection(var9);
         if (GameClient.bClient) {
            GameClient.instance.addDisconnectPacket(var2);
         }
         break;
      case 22:
         var9 = var1.get() & 255;
         if (GameServer.bServer && this.connectionArray[var9] != null) {
            DebugLog.log("Connection Lost for id=" + var9 + " username=" + this.connectionArray[var9].username);
         } else {
            DebugLog.log("Connection Lost");
         }

         this.removeConnection(var9);
         break;
      case 23:
         DebugLog.Network.println("User Banned");
         if (GameClient.bClient) {
            GameClient.instance.addDisconnectPacket(var2);
         }
         break;
      case 24:
         if (GameClient.bClient) {
            GameClient.instance.addDisconnectPacket(var2);
         }
         break;
      case 32:
         if (GameClient.bClient) {
            GameClient.instance.addDisconnectPacket(var2);
         }
         break;
      case 44:
         var8 = this.peer.getGuidOfPacket();
         VoiceManager.instance.VoiceConnectAccept(var8);
         break;
      case 45:
         var8 = this.peer.getGuidOfPacket();
         VoiceManager.instance.VoiceOpenChannelReply(var8);
         break;
      case 134:
         short var3 = var1.getShort();
         if (GameServer.bServer) {
            var4 = this.peer.getGuidOfPacket();
            var6 = (UdpConnection)this.connectionMap.get(var4);
            if (var6 == null) {
               DebugLog.Network.warn("GOT PACKET FROM UNKNOWN CONNECTION guid=%s packetId=%s", var4, Integer.valueOf(var3));
               return;
            }

            GameServer.addIncoming((short)var3, var1, var6);
         } else {
            GameClient.instance.addIncoming((short)var3, var1);
         }
         break;
      default:
         DebugLog.Network.warn("Received unknown packet: %s", var2);
      }

   }

   private void removeConnection(int var1) {
      UdpConnection var2 = this.connectionArray[var1];
      if (var2 != null) {
         this.connectionArray[var1] = null;
         this.connectionMap.remove(var2.getConnectedGUID());
         if (GameClient.bClient) {
            GameClient.instance.connectionLost();
         }

         if (GameServer.bServer) {
            GameServer.addDisconnect(var2);
         }
      }

   }

   private UdpConnection addConnection(int var1, long var2) {
      UdpConnection var4 = new UdpConnection(this, var2, var1);
      this.connectionMap.put(var2, var4);
      this.connectionArray[var1] = var4;
      if (GameServer.bServer) {
         GameServer.addConnection(var4);
      }

      return var4;
   }

   public ByteBuffer Receive() {
      boolean var1 = false;

      do {
         var1 = this.peer.Receive(this.buf);
         if (var1) {
            return this.buf;
         }

         try {
            Thread.sleep(1L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }
      } while(!this.bQuit && !var1);

      return this.buf;
   }

   public UdpConnection getActiveConnection(long var1) {
      return !this.connectionMap.containsKey(var1) ? null : (UdpConnection)this.connectionMap.get(var1);
   }

   public void Connect(String var1, int var2, String var3) {
      if (var2 == 0 && SteamUtils.isSteamModeEnabled()) {
         long var9 = 0L;

         try {
            var9 = SteamUtils.convertStringToSteamID(var1);
         } catch (NumberFormatException var8) {
            var8.printStackTrace();
            LuaEventManager.triggerEvent("OnConnectFailed", Translator.getText("UI_OnConnectFailed_UnknownHost"));
            return;
         }

         this.peer.ConnectToSteamServer(var9, this.hashServerPassword(var3));
      } else {
         String var4;
         try {
            InetAddress var5 = InetAddress.getByName(var1);
            var4 = var5.getHostAddress();
         } catch (UnknownHostException var7) {
            var7.printStackTrace();
            LuaEventManager.triggerEvent("OnConnectFailed", Translator.getText("UI_OnConnectFailed_UnknownHost"));
            return;
         }

         this.peer.Connect(var4, var2, this.hashServerPassword(var3));
      }

   }

   public void Connect(long var1, String var3) {
      this.peer.ConnectToSteamServer(var1, var3);
   }

   public void forceDisconnect(long var1) {
      this.peer.disconnect(var1);
      this.removeConnection(var1);
   }

   private void removeConnection(long var1) {
      UdpConnection var3 = (UdpConnection)this.connectionMap.remove(var1);
      if (var3 != null) {
         this.removeConnection(var3.index);
      }

   }

   public RakNetPeerInterface getPeer() {
      return this.peer;
   }

   public int getMaxConnections() {
      return this.maxConnections;
   }
}
