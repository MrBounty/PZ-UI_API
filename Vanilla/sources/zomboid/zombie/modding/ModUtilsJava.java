package zombie.modding;

import java.util.ArrayList;
import java.util.UUID;
import zombie.characters.IsoPlayer;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.InventoryItem;
import zombie.network.GameClient;
import zombie.network.GameServer;

public final class ModUtilsJava {
   public static String getRandomUUID() {
      return UUID.randomUUID().toString();
   }

   public static boolean sendItemListNet(IsoPlayer var0, ArrayList var1, IsoPlayer var2, String var3, String var4) {
      if (var1 != null) {
         var3 = var3 != null ? var3 : "-1";
         if (GameClient.bClient) {
            if (var1.size() > 50) {
               return false;
            }

            for(int var5 = 0; var5 < var1.size(); ++var5) {
               InventoryItem var6 = (InventoryItem)var1.get(var5);
               if (!var0.getInventory().getItems().contains(var6)) {
                  return false;
               }
            }

            GameClient var10000 = GameClient.instance;
            return GameClient.sendItemListNet(var0, var1, var2, var3, var4);
         }

         if (GameServer.bServer) {
            return GameServer.sendItemListNet((UdpConnection)null, var0, var1, var2, var3, var4);
         }
      }

      return false;
   }
}
