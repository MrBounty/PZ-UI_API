package zombie.core.raknet;

import java.nio.ByteBuffer;

public class TestClient {
   static RakNetPeerInterface client;
   private static boolean bConnected = false;

   public static void main(String[] var0) {
      client = new RakNetPeerInterface();
      client.Init(false);
      int var1 = client.Startup(1);
      System.out.println("Result: " + var1);
      client.SetOccasionalPing(true);
      System.out.println("Client connecting: " + client.Connect("127.0.0.1", 12203, "spiffo"));
      boolean var2 = false;
      ByteBuffer var3 = ByteBuffer.allocate(500000);
      int var4 = 0;

      while(!var2) {
         ++var4;

         while(client.Receive(var3)) {
            decode(var3);
         }

         try {
            Thread.sleep(33L);
         } catch (InterruptedException var6) {
            var6.printStackTrace();
         }
      }

   }

   private static void decode(ByteBuffer var0) {
      byte var1 = var0.get();
      switch(var1) {
      case 0:
      case 1:
         System.out.println("PING");
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 19:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      default:
         break;
      case 16:
         System.out.println("ID_CONNECTION_REQUEST_ACCEPTED");
         bConnected = true;
         var0.clear();
         var0.put((byte)-122);

         for(int var2 = 0; var2 < 1000; ++var2) {
            var0.put((byte)-1);
         }

         System.out.println("Sending: " + client.Send(var0, 1, 3, (byte)0, 0L, false));
         break;
      case 17:
         System.out.println("ID_CONNECTION_ATTEMPT_FAILED");
         break;
      case 18:
         System.out.println("ID_ALREADY_CONNECTED");
         break;
      case 20:
         System.out.println("ID_NO_FREE_INCOMING_CONNECTIONS");
         break;
      case 21:
         System.out.println("ID_DISCONNECTION_NOTIFICATION");
         break;
      case 22:
         System.out.println("ID_CONNECTION_LOST");
         break;
      case 23:
         System.out.println("ID_CONNECTION_BANNED");
         break;
      case 24:
         System.out.println("ID_INVALID_PASSWORD");
         break;
      case 25:
         System.out.println("ID_INCOMPATIBLE_PROTOCOL_VERSION");
         break;
      case 31:
         System.out.println("ID_REMOTE_DISCONNECTION_NOTIFICATION");
         break;
      case 32:
         System.out.println("ID_REMOTE_CONNECTION_LOST");
         break;
      case 33:
         System.out.println("ID_REMOTE_NEW_INCOMING_CONNECTION");
      }

   }
}
