package zombie.core.raknet;

import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.lwjglx.BufferUtils;
import zombie.Lua.LuaEventManager;
import zombie.core.znet.ZNetFileAnnounce;
import zombie.core.znet.ZNetFileChunk;
import zombie.core.znet.ZNetStatistics;
import zombie.debug.DebugLog;

public class RakNetPeerInterface {
   private static Thread mainThread;
   public static final int ID_NEW_INCOMING_CONNECTION = 19;
   public static final int ID_DISCONNECTION_NOTIFICATION = 21;
   public static final int ID_INCOMPATIBLE_PROTOCOL_VERSION = 25;
   public static final int ID_CONNECTED_PING = 0;
   public static final int ID_UNCONNECTED_PING = 1;
   public static final int ID_CONNECTION_LOST = 22;
   public static final int ID_ALREADY_CONNECTED = 18;
   public static final int ID_REMOTE_DISCONNECTION_NOTIFICATION = 31;
   public static final int ID_REMOTE_CONNECTION_LOST = 32;
   public static final int ID_REMOTE_NEW_INCOMING_CONNECTION = 33;
   public static final int ID_CONNECTION_BANNED = 23;
   public static final int ID_CONNECTION_ATTEMPT_FAILED = 17;
   public static final int ID_NO_FREE_INCOMING_CONNECTIONS = 20;
   public static final int ID_CONNECTION_REQUEST_ACCEPTED = 16;
   public static final int ID_INVALID_PASSWORD = 24;
   public static final int ID_TIMESTAMP = 27;
   public static final int ID_PING = 28;
   public static final int ID_RAKVOICE_OPEN_CHANNEL_REQUEST = 44;
   public static final int ID_RAKVOICE_OPEN_CHANNEL_REPLY = 45;
   public static final int ID_RAKVOICE_CLOSE_CHANNEL = 46;
   public static final int ID_RAKVOICE_DATA = 47;
   public static final int ID_USER_PACKET_ENUM = 134;
   public static final int PacketPriority_IMMEDIATE = 0;
   public static final int PacketPriority_HIGH = 1;
   public static final int PacketPriority_MEDIUM = 2;
   public static final int PacketPriority_LOW = 3;
   public static final int PacketReliability_UNRELIABLE = 0;
   public static final int PacketReliability_UNRELIABLE_SEQUENCED = 1;
   public static final int PacketReliability_RELIABLE = 2;
   public static final int PacketReliability_RELIABLE_ORDERED = 3;
   public static final int PacketReliability_RELIABLE_SEQUENCED = 4;
   public static final int PacketReliability_UNRELIABLE_WITH_ACK_RECEIPT = 5;
   public static final int PacketReliability_RELIABLE_WITH_ACK_RECEIPT = 6;
   public static final int PacketReliability_RELIABLE_ORDERED_WITH_ACK_RECEIPT = 7;
   ByteBuffer receiveBuf = BufferUtils.createByteBuffer(1000000);
   ByteBuffer sendBuf = BufferUtils.createByteBuffer(1000000);
   Lock sendLock = new ReentrantLock();

   public static void init() {
      mainThread = Thread.currentThread();
   }

   public native void Init(boolean var1);

   public native int Startup(int var1);

   public native void Shutdown();

   public native void SetServerIP(String var1);

   public native void SetServerPort(int var1);

   public native void SetClientPort(int var1);

   public native int Connect(String var1, int var2, String var3);

   public native int ConnectToSteamServer(long var1, String var3);

   public native String GetServerIP();

   public native long GetClientSteamID(long var1);

   public native long GetClientOwnerSteamID(long var1);

   public native void SetIncomingPassword(String var1);

   public native void SetTimeoutTime(int var1);

   public native void SetMaximumIncomingConnections(int var1);

   public native void SetOccasionalPing(boolean var1);

   public native void SetUnreliableTimeout(int var1);

   public native void ApplyNetworkSimulator(float var1, short var2, short var3);

   private native boolean TryReceive();

   private native int nativeGetData(ByteBuffer var1);

   public boolean Receive(ByteBuffer var1) {
      if (this.TryReceive()) {
         try {
            var1.clear();
            this.receiveBuf.clear();
            int var2 = this.nativeGetData(this.receiveBuf);
            var1.put(this.receiveBuf);
            var1.flip();
            return true;
         } catch (Exception var3) {
            var3.printStackTrace();
            return false;
         }
      } else {
         return false;
      }
   }

   public int Send(ByteBuffer var1, int var2, int var3, byte var4, long var5, boolean var7) {
      this.sendLock.lock();
      this.sendBuf.clear();
      if (var1.remaining() > this.sendBuf.remaining()) {
         System.out.println("Packet data too big.");
         this.sendLock.unlock();
         return 0;
      } else {
         try {
            this.sendBuf.put(var1);
            this.sendBuf.flip();
            int var8 = this.sendNative(this.sendBuf, this.sendBuf.remaining(), var2, var3, var4, var5, var7);
            this.sendLock.unlock();
            return var8;
         } catch (Exception var9) {
            System.out.println("Other weird packet data error.");
            var9.printStackTrace();
            this.sendLock.unlock();
            return 0;
         }
      }
   }

   public int SendRaw(ByteBuffer var1, int var2, int var3, byte var4, long var5, boolean var7) {
      try {
         int var8 = this.sendNative(var1, var1.remaining(), var2, var3, var4, var5, var7);
         return var8;
      } catch (Exception var9) {
         System.out.println("Other weird packet data error.");
         var9.printStackTrace();
         return 0;
      }
   }

   private native int sendNative(ByteBuffer var1, int var2, int var3, int var4, byte var5, long var6, boolean var8);

   public native long getGuidFromIndex(int var1);

   public native long getGuidOfPacket();

   public native String getIPFromGUID(long var1);

   public native int SendFileAnnounce(long var1, long var3, long var5, long var7, String var9);

   public native int SendFileChunk(long var1, long var3, long var5, byte[] var7, long var8);

   public native ZNetFileAnnounce ReceiveFileAnnounce();

   public native ZNetFileChunk ReceiveFileChunk();

   public native void disconnect(long var1);

   private void connectionStateChangedCallback(String var1, String var2) {
      Thread var3 = Thread.currentThread();
      if (var3 == mainThread) {
         LuaEventManager.triggerEvent("OnConnectionStateChanged", var1, var2);
      } else {
         DebugLog.log("RakNetPeerInterface.connectionStateChangedCallback state=" + var1 + " message=" + var2 + " thread=" + var3);
      }

   }

   public native ZNetStatistics GetNetStatistics(long var1);

   public native int GetAveragePing(long var1);

   public native int GetLastPing(long var1);

   public native int GetLowestPing(long var1);

   public native int GetMTUSize(long var1);
}
