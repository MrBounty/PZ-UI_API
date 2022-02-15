package zombie.core.raknet;

import java.nio.ByteBuffer;

public class TestServer {
   static RakNetPeerInterface server;
   static ByteBuffer buf = ByteBuffer.allocate(2048);

   public static void main(String[] var0) {
      server = new RakNetPeerInterface();
      server.SetServerPort(12203);
      server.Init(false);
      int var1 = server.Startup(32);
      System.out.println("Result: " + var1);
      server.SetMaximumIncomingConnections(32);
      server.SetOccasionalPing(true);
      server.SetIncomingPassword("spiffo");
      boolean var2 = false;

      while(!var2) {
         String var3 = "This is a test message";
         ByteBuffer var4 = Receive();
         decode(var4);
      }

   }

   private static void decode(ByteBuffer var0) {
      int var1 = var0.get() & 255;
      switch(var1) {
      case 0:
      case 1:
         System.out.println("PING");
         break;
      case 19:
         int var2 = var0.get() & 255;
         long var3 = server.getGuidFromIndex(var2);
         break;
      case 21:
         System.out.println("ID_DISCONNECTION_NOTIFICATION");
         break;
      case 22:
         System.out.println("ID_CONNECTION_LOST");
         break;
      case 25:
         System.out.println("ID_INCOMPATIBLE_PROTOCOL_VERSION");
         break;
      default:
         System.out.println("Other: " + var1);
      }

   }

   public static ByteBuffer Receive() {
      int var0 = buf.position();
      boolean var1 = false;

      do {
         try {
            Thread.sleep(1L);
         } catch (InterruptedException var3) {
            var3.printStackTrace();
         }

         var1 = server.Receive(buf);
      } while(!var1);

      return buf;
   }
}
