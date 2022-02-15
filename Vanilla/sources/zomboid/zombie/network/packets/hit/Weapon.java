package zombie.network.packets.hit;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import zombie.characters.IsoLivingCharacter;
import zombie.core.network.ByteBufferWriter;
import zombie.debug.DebugLog;
import zombie.debug.LogSeverity;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.types.HandWeapon;
import zombie.network.packets.INetworkPacket;

public class Weapon extends Instance implements INetworkPacket {
   protected InventoryItem item;
   protected HandWeapon weapon;

   public void set(HandWeapon var1) {
      super.set(var1.getRegistry_id());
      this.item = var1;
      this.weapon = var1;
   }

   public void parse(ByteBuffer var1, IsoLivingCharacter var2) {
      boolean var3 = var1.get() == 1;
      if (var3) {
         this.ID = var1.getShort();
         var1.get();
         if (var2 != null) {
            this.item = var2.getPrimaryHandItem();
            if (this.item == null || this.item.getRegistry_id() != this.ID) {
               this.item = InventoryItemFactory.CreateItem(this.ID);
            }

            if (this.item != null) {
               try {
                  this.item.load(var1, 186);
               } catch (BufferUnderflowException | IOException var5) {
                  DebugLog.Multiplayer.printException(var5, "Weapon load error", LogSeverity.Error);
                  this.item = InventoryItemFactory.CreateItem("Base.BareHands");
               }
            }
         }
      } else {
         this.item = InventoryItemFactory.CreateItem("Base.BareHands");
      }

      if (var2 != null) {
         this.weapon = var2.bareHands;
         if (this.item instanceof HandWeapon) {
            this.weapon = (HandWeapon)this.item;
         }
      }

   }

   public void parse(ByteBuffer var1) {
      DebugLog.Multiplayer.error("Weapon.parse is not implemented");
   }

   public void write(ByteBufferWriter var1) {
      if (this.item == null) {
         var1.putByte((byte)0);
      } else {
         var1.putByte((byte)1);

         try {
            this.item.save(var1.bb, false);
         } catch (IOException var3) {
            DebugLog.Multiplayer.printException(var3, "Item write error", LogSeverity.Error);
         }
      }

   }

   public boolean isConsistent() {
      return super.isConsistent() && this.weapon != null;
   }

   public String getDescription() {
      String var10000 = super.getDescription();
      return var10000 + "\n\tWeapon [ weapon=" + (this.weapon == null ? "?" : "\"" + this.weapon.getDisplayName() + "\"") + " ]";
   }

   HandWeapon getWeapon() {
      return this.weapon;
   }
}
