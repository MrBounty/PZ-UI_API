package zombie.core.raknet;

import java.nio.ByteBuffer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.znet.SteamUtils;
import zombie.debug.DebugLog;
import zombie.network.GameServer;

public class VoiceTest {
   protected static boolean bQuit = false;
   protected static ByteBuffer serverBuf = ByteBuffer.allocate(500000);
   protected static ByteBuffer clientBuf = ByteBuffer.allocate(500000);
   protected static RakNetPeerInterface rnclientPeer;
   protected static RakNetPeerInterface rnserverPeer;

   protected static void rakNetServer(int var0) {
      byte var1 = 2;
      String var2 = "test";
      rnserverPeer = new RakNetPeerInterface();
      DebugLog.log("Initialising RakNet...");
      rnserverPeer.Init(false);
      rnserverPeer.SetMaximumIncomingConnections(var1);
      if (GameServer.IPCommandline != null) {
         rnserverPeer.SetServerIP(GameServer.IPCommandline);
      }

      rnserverPeer.SetServerPort(var0);
      rnserverPeer.SetIncomingPassword(var2);
      rnserverPeer.SetOccasionalPing(true);
      int var3 = rnserverPeer.Startup(var1);
      System.out.println("RakNet.Startup() return code: " + var3 + " (0 means success)");
   }

   public static ByteBuffer rakNetServerReceive() {
      boolean var0 = false;

      do {
         try {
            Thread.sleep(1L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }

         var0 = rnserverPeer.Receive(serverBuf);
      } while(!bQuit && !var0);

      return serverBuf;
   }

   private static void rakNetServerDecode(ByteBuffer var0) {
      int var1 = var0.get() & 255;
      int var2;
      long var3;
      switch(var1) {
      case 0:
      case 1:
         System.out.println("PING");
         break;
      case 16:
         System.out.println("Connection Request Accepted");
         var2 = var0.get() & 255;
         var3 = rnserverPeer.getGuidOfPacket();
         VoiceManager.instance.VoiceConnectReq(var3);
         break;
      case 19:
         System.out.println("ID_NEW_INCOMING_CONNECTION");
         var2 = var0.get() & 255;
         var3 = rnserverPeer.getGuidOfPacket();
         System.out.println("id=" + var2 + " guid=" + var3);
         VoiceManager.instance.VoiceConnectReq(var3);
         break;
      default:
         System.out.println("Received: " + var1);
      }

   }

   protected static void rakNetClient() {
      byte var0 = 2;
      String var1 = "test";
      rnclientPeer = new RakNetPeerInterface();
      DebugLog.log("Initialising RakNet...");
      rnclientPeer.Init(false);
      rnclientPeer.SetMaximumIncomingConnections(var0);
      rnclientPeer.SetClientPort(GameServer.DEFAULT_PORT + Rand.Next(10000) + 1234);
      rnclientPeer.SetOccasionalPing(true);
      int var2 = rnclientPeer.Startup(var0);
      System.out.println("RakNet.Startup() return code: " + var2 + " (0 means success)");
   }

   public static ByteBuffer rakNetClientReceive() {
      boolean var0 = false;

      do {
         try {
            Thread.sleep(1L);
         } catch (InterruptedException var2) {
            var2.printStackTrace();
         }

         var0 = rnclientPeer.Receive(clientBuf);
      } while(!bQuit && !var0);

      return clientBuf;
   }

   private static void rakNetClientDecode(ByteBuffer var0) {
      int var1 = var0.get() & 255;
      int var2;
      long var3;
      switch(var1) {
      case 0:
      case 1:
         System.out.println("PING");
         break;
      case 16:
         System.out.println("Connection Request Accepted");
         var2 = var0.get() & 255;
         var3 = rnclientPeer.getGuidOfPacket();
         VoiceManager.instance.VoiceConnectReq(var3);
         break;
      case 19:
         System.out.println("ID_NEW_INCOMING_CONNECTION");
         var2 = var0.get() & 255;
         var3 = rnclientPeer.getGuidOfPacket();
         System.out.println("id=" + var2 + " guid=" + var3);
         VoiceManager.instance.VoiceConnectReq(var3);
         break;
      default:
         System.out.println("Received: " + var1);
      }

   }

   public static void main(String[] var0) {
      DebugLog.log("VoiceTest: START");
      DebugLog.log("versionNumber=" + Core.getInstance().getVersionNumber() + " demo=false");
      DebugLog.log("VoiceTest: SteamUtils.init - EXEC");
      SteamUtils.init();
      DebugLog.log("VoiceTest: SteamUtils.init - OK");
      DebugLog.log("VoiceTest: RakNetPeerInterface - EXEC");
      RakNetPeerInterface.init();
      DebugLog.log("VoiceTest: RakNetPeerInterface - OK");
      DebugLog.log("VoiceTest: VoiceManager.InitVMServer - EXEC");
      VoiceManager.instance.InitVMServer();
      DebugLog.log("VoiceTest: VoiceManager.InitVMServer - OK");
      DebugLog.log("VoiceTest: rakNetServer - EXEC");
      rakNetServer(16000);
      DebugLog.log("VoiceTest: rakNetServer - OK");
      DebugLog.log("VoiceTest: rakNetClient - EXEC");
      rakNetClient();
      DebugLog.log("VoiceTest: rakNetClient - OK");
      DebugLog.log("VoiceTest: rnclientPeer.Connect - EXEC");
      rnclientPeer.Connect("127.0.0.1", 16000, "test");
      DebugLog.log("VoiceTest: rnclientPeer.Connect - OK");
      Thread var1 = new Thread() {
         public void run() {
            while(!VoiceTest.bQuit && !VoiceTest.bQuit) {
               ByteBuffer var1 = VoiceTest.rakNetServerReceive();

               try {
                  VoiceTest.rakNetServerDecode(var1);
               } catch (Exception var3) {
                  var3.printStackTrace();
               }
            }

         }
      };
      var1.setName("serverThread");
      var1.start();
      Thread var2 = new Thread() {
         public void run() {
            while(!VoiceTest.bQuit && !VoiceTest.bQuit) {
               ByteBuffer var1 = VoiceTest.rakNetClientReceive();

               try {
                  VoiceTest.rakNetClientDecode(var1);
               } catch (Exception var3) {
                  var3.printStackTrace();
               }
            }

         }
      };
      var2.setName("clientThread");
      var2.start();
      DebugLog.log("VoiceTest: sleep 10 sec");

      try {
         Thread.sleep(10000L);
      } catch (InterruptedException var4) {
         var4.printStackTrace();
      }

   }
}
