package zombie.network.packets;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.GameWindow;
import zombie.characters.IsoPlayer;
import zombie.core.logger.ExceptionLogger;
import zombie.core.network.ByteBufferWriter;
import zombie.core.skinnedmodel.visual.ItemVisuals;
import zombie.inventory.InventoryItem;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.network.ServerGUI;

public class SyncClothingPacket implements INetworkPacket {
   private IsoPlayer player = null;
   private String location = "";
   private InventoryItem item = null;

   public void set(IsoPlayer var1, String var2, InventoryItem var3) {
      this.player = var1;
      this.location = var2;
      this.item = var3;
   }

   public boolean isEquals(IsoPlayer var1, String var2, InventoryItem var3) {
      return this.player.OnlineID == var1.OnlineID && this.location.equals(var2) && this.item == var3;
   }

   public void parse(ByteBuffer var1) {
      short var2 = var1.getShort();
      this.location = GameWindow.ReadString(var1);
      byte var3 = var1.get();
      if (var3 == 1) {
         try {
            this.item = InventoryItem.loadItem(var1, 186);
         } catch (Exception var6) {
            var6.printStackTrace();
         }

         if (this.item == null) {
            return;
         }
      }

      if (GameServer.bServer) {
         this.player = (IsoPlayer)GameServer.IDToPlayerMap.get(var2);
      } else {
         this.player = (IsoPlayer)GameClient.IDToPlayerMap.get(var2);
      }

      if (this.player != null) {
         try {
            this.player.getHumanVisual().load(var1, 186);
            this.player.getItemVisuals().load(var1, 186);
         } catch (Throwable var5) {
            ExceptionLogger.logException(var5);
            return;
         }

         if (var3 == 1) {
            this.player.getWornItems().setItem(this.location, this.item);
         }

         if (GameServer.bServer && ServerGUI.isCreated() || GameClient.bClient) {
            this.player.resetModelNextFrame();
         }

      }
   }

   public void write(ByteBufferWriter var1) {
      var1.putShort((short)((byte)this.player.OnlineID));
      var1.putUTF(this.location);
      if (this.item == null) {
         var1.putByte((byte)0);
      } else {
         var1.putByte((byte)1);

         try {
            this.item.saveWithSize(var1.bb, false);
         } catch (IOException var4) {
            var4.printStackTrace();
         }
      }

      try {
         this.player.getHumanVisual().save(var1.bb);
         ItemVisuals var2 = new ItemVisuals();
         this.player.getItemVisuals(var2);
         var2.save(var1.bb);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public boolean isConsistent() {
      return this.player != null;
   }

   public String getDescription() {
      String var1;
      if (this.player == null) {
         var1 = "player=null";
      } else {
         var1 = String.format("player=%s(oid:%d)", this.player.username, this.player.OnlineID);
      }

      var1 = var1 + ", location=" + this.location;
      if (this.item == null) {
         var1 = var1 + ", item=null";
      } else {
         var1 = var1 + ", item=" + this.item.getFullType();
      }

      return var1;
   }
}
