package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.WorldSoundManager;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Clothing;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSprite;
import zombie.network.GameClient;
import zombie.network.GameServer;

public class IsoClothingDryer extends IsoObject {
   private boolean bActivated;
   private long SoundInstance = -1L;
   private float lastUpdate = -1.0F;
   private boolean cycleFinished = false;
   private float startTime = 0.0F;
   private float cycleLengthMinutes = 90.0F;
   private boolean alreadyExecuted = false;

   public IsoClothingDryer(IsoCell var1) {
      super(var1);
   }

   public IsoClothingDryer(IsoCell var1, IsoGridSquare var2, IsoSprite var3) {
      super(var1, var2, var3);
   }

   public String getObjectName() {
      return "ClothingDryer";
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.bActivated = var1.get() == 1;
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.put((byte)(this.isActivated() ? 1 : 0));
   }

   public void update() {
      if (this.getObjectIndex() != -1) {
         if (this.container != null) {
            if (!this.container.isPowered()) {
               this.setActivated(false);
            }

            this.cycleFinished();
            this.updateSound();
            if (GameClient.bClient) {
            }

            if (!this.isActivated()) {
               this.lastUpdate = -1.0F;
            } else {
               float var1 = (float)GameTime.getInstance().getWorldAgeHours();
               if (this.lastUpdate < 0.0F) {
                  this.lastUpdate = var1;
               } else if (this.lastUpdate > var1) {
                  this.lastUpdate = var1;
               }

               float var2 = var1 - this.lastUpdate;
               int var3 = (int)(var2 * 60.0F);
               if (var3 >= 1) {
                  this.lastUpdate = var1;

                  for(int var4 = 0; var4 < this.container.getItems().size(); ++var4) {
                     InventoryItem var5 = (InventoryItem)this.container.getItems().get(var4);
                     if (var5 instanceof Clothing) {
                        Clothing var6 = (Clothing)var5;
                        float var7 = var6.getWetness();
                        if (var7 > 0.0F) {
                           var7 -= (float)var3;
                           var6.setWetness(var7);
                           if (GameServer.bServer) {
                           }
                        }
                     }

                     if (var5.isWet() && var5.getItemWhenDry() != null) {
                        var5.setWetCooldown(var5.getWetCooldown() - (float)(var3 * 250));
                        if (var5.getWetCooldown() <= 0.0F) {
                           InventoryItem var8 = InventoryItemFactory.CreateItem(var5.getItemWhenDry());
                           this.getContainer().addItem(var8);
                           this.getContainer().Remove(var5);
                           --var4;
                           var5.setWet(false);
                           IsoWorld.instance.CurrentCell.addToProcessItemsRemove(var5);
                        }
                     }
                  }

               }
            }
         }
      }
   }

   public void addToWorld() {
      IsoCell var1 = this.getCell();
      var1.addToProcessIsoObject(this);
   }

   public void removeFromWorld() {
      super.removeFromWorld();
   }

   public void saveChange(String var1, KahluaTable var2, ByteBuffer var3) {
      if ("state".equals(var1)) {
         var3.put((byte)(this.isActivated() ? 1 : 0));
      }

   }

   public void loadChange(String var1, ByteBuffer var2) {
      if ("state".equals(var1)) {
         this.setActivated(var2.get() == 1);
      }

   }

   private void updateSound() {
      if (this.isActivated()) {
         if (!GameServer.bServer) {
            if (this.emitter != null && this.emitter.isPlaying("ClothingDryerFinished")) {
               this.emitter.stopSoundByName("ClothingDryerFinished");
            }

            if (this.SoundInstance == -1L) {
               this.emitter = IsoWorld.instance.getFreeEmitter(this.getX() + 0.5F, this.getY() + 0.5F, (float)((int)this.getZ()));
               this.SoundInstance = this.emitter.playSoundLoopedImpl("ClothingDryerRunning");
            }
         }

         if (!GameClient.bClient) {
            WorldSoundManager.instance.addSound(this, this.square.x, this.square.y, this.square.z, 10, 10);
         }
      } else if (this.SoundInstance != -1L) {
         this.emitter.stopSound(this.SoundInstance);
         this.SoundInstance = -1L;
         if (this.cycleFinished) {
            this.cycleFinished = false;
            this.emitter.playSoundImpl("ClothingDryerFinished", (IsoObject)this);
         } else {
            this.emitter = null;
         }
      }

   }

   private boolean cycleFinished() {
      if (this.isActivated()) {
         if (!this.alreadyExecuted) {
            this.startTime = (float)GameTime.getInstance().getWorldAgeHours();
            this.alreadyExecuted = true;
         }

         float var1 = (float)GameTime.getInstance().getWorldAgeHours() - this.startTime;
         int var2 = (int)(var1 * 60.0F);
         if ((float)var2 < this.cycleLengthMinutes) {
            return false;
         }

         this.cycleFinished = true;
         this.setActivated(false);
      }

      return true;
   }

   public boolean isItemAllowedInContainer(ItemContainer var1, InventoryItem var2) {
      return !this.isActivated();
   }

   public boolean isRemoveItemAllowedFromContainer(ItemContainer var1, InventoryItem var2) {
      if (this.container.Items.size() > 0 && this.isActivated()) {
         return false;
      } else {
         return this.container == var1;
      }
   }

   public boolean isActivated() {
      return this.bActivated;
   }

   public void setActivated(boolean var1) {
      this.bActivated = var1;
      this.alreadyExecuted = false;
   }
}
