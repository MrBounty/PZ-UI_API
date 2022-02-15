package zombie.vehicles;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.SoundManager;
import zombie.WorldSoundManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoGridSquare;
import zombie.network.GameClient;
import zombie.network.GameServer;
import zombie.scripting.objects.VehicleScript;

public final class VehicleWindow {
   protected VehiclePart part;
   protected int health;
   protected boolean openable;
   protected boolean open;
   protected float openDelta = 0.0F;

   VehicleWindow(VehiclePart var1) {
      this.part = var1;
   }

   public void init(VehicleScript.Window var1) {
      this.health = 100;
      this.openable = var1.openable;
      this.open = false;
   }

   public int getHealth() {
      return this.part.getCondition();
   }

   public void setHealth(int var1) {
      var1 = Math.max(var1, 0);
      var1 = Math.min(var1, 100);
      this.health = var1;
   }

   public boolean isDestroyed() {
      return this.getHealth() == 0;
   }

   public boolean isOpenable() {
      return this.openable;
   }

   public boolean isOpen() {
      return this.open;
   }

   public void setOpen(boolean var1) {
      this.open = var1;
      this.part.getVehicle().bDoDamageOverlay = true;
   }

   public void setOpenDelta(float var1) {
      this.openDelta = var1;
   }

   public float getOpenDelta() {
      return this.openDelta;
   }

   public boolean isHittable() {
      if (this.isDestroyed()) {
         return false;
      } else if (this.isOpen()) {
         return false;
      } else {
         return this.part.getItemType() == null || this.part.getInventoryItem() != null;
      }
   }

   public void hit(IsoGameCharacter var1) {
      this.damage(this.getHealth());
      this.part.setCondition(0);
   }

   public void damage(int var1) {
      if (var1 > 0) {
         if (this.isHittable()) {
            if (GameClient.bClient) {
               GameClient.instance.sendClientCommandV((IsoPlayer)null, "vehicle", "damageWindow", "vehicle", this.part.vehicle.getId(), "part", this.part.getId(), "amount", var1);
            } else {
               if (this.part.getVehicle().isAlarmed()) {
                  this.part.getVehicle().triggerAlarm();
               }

               this.part.setCondition(this.part.getCondition() - var1);
               if (this.isDestroyed()) {
                  if (this.part.getInventoryItem() != null) {
                     this.part.setInventoryItem((InventoryItem)null);
                     this.part.getVehicle().transmitPartItem(this.part);
                  }

                  IsoGridSquare var2 = this.part.vehicle.square;
                  if (GameServer.bServer) {
                     GameServer.PlayWorldSoundServer("SmashWindow", false, var2, 0.2F, 20.0F, 1.1F, true);
                  } else {
                     SoundManager.instance.PlayWorldSound("SmashWindow", var2, 0.2F, 20.0F, 1.0F, true);
                  }

                  WorldSoundManager.instance.addSound((Object)null, var2.getX(), var2.getY(), var2.getZ(), 10, 20, true, 4.0F, 15.0F);
               }

               this.part.getVehicle().transmitPartWindow(this.part);
            }
         }
      }
   }

   public void save(ByteBuffer var1) throws IOException {
      var1.put((byte)this.part.getCondition());
      var1.put((byte)(this.open ? 1 : 0));
   }

   public void load(ByteBuffer var1, int var2) throws IOException {
      this.part.setCondition(var1.get());
      this.health = this.part.getCondition();
      this.open = var1.get() == 1;
      this.openDelta = this.open ? 1.0F : 0.0F;
   }
}
