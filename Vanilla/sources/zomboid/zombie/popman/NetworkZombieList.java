package zombie.popman;

import java.util.Iterator;
import java.util.LinkedList;
import zombie.core.raknet.UdpConnection;

public class NetworkZombieList {
   final LinkedList networkZombies = new LinkedList();
   public Object lock = new Object();

   public NetworkZombieList.NetworkZombie getNetworkZombie(UdpConnection var1) {
      if (var1 == null) {
         return null;
      } else {
         Iterator var2 = this.networkZombies.iterator();

         NetworkZombieList.NetworkZombie var3;
         do {
            if (!var2.hasNext()) {
               NetworkZombieList.NetworkZombie var4 = new NetworkZombieList.NetworkZombie(var1);
               this.networkZombies.add(var4);
               return var4;
            }

            var3 = (NetworkZombieList.NetworkZombie)var2.next();
         } while(var3.connection != var1);

         return var3;
      }
   }

   public static class NetworkZombie {
      final LinkedList zombies = new LinkedList();
      final UdpConnection connection;

      public NetworkZombie(UdpConnection var1) {
         this.connection = var1;
      }
   }
}
