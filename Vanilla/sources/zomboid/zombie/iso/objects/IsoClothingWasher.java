package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.WorldSoundManager;
import zombie.characterTextures.BloodBodyPartType;
import zombie.characterTextures.BloodClothingType;
import zombie.core.skinnedmodel.visual.ItemVisual;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Clothing;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSprite;
import zombie.network.GameClient;
import zombie.network.GameServer;

public class IsoClothingWasher extends IsoObject {
   private boolean bActivated;
   private long soundInstance = -1L;
   private float lastUpdate = -1.0F;
   private boolean cycleFinished = false;
   private float startTime = 0.0F;
   private float cycleLengthMinutes = 90.0F;
   private boolean alreadyExecuted = false;
   private static final ArrayList coveredParts = new ArrayList();

   public IsoClothingWasher(IsoCell var1) {
      super(var1);
   }

   public IsoClothingWasher(IsoCell var1, IsoGridSquare var2, IsoSprite var3) {
      super(var1, var2, var3);
   }

   public String getObjectName() {
      return "ClothingWasher";
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.bActivated = var1.get() == 1;
      this.lastUpdate = var1.getFloat();
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.put((byte)(this.isActivated() ? 1 : 0));
      var1.putFloat(this.lastUpdate);
   }

   public void update() {
      if (this.getObjectIndex() != -1) {
         if (!this.container.isPowered()) {
            this.setActivated(false);
         }

         this.updateSound();
         this.cycleFinished();
         if (GameClient.bClient) {
         }

         if (this.getWaterAmount() <= 0) {
            this.setActivated(false);
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
               this.useWater(1 * var3);

               for(int var4 = 0; var4 < this.container.getItems().size(); ++var4) {
                  InventoryItem var5 = (InventoryItem)this.container.getItems().get(var4);
                  if (var5 instanceof Clothing) {
                     Clothing var6 = (Clothing)var5;
                     float var7 = var6.getBloodlevel();
                     if (var7 > 0.0F) {
                        this.removeBlood(var6, (float)(var3 * 2));
                     }

                     float var8 = var6.getDirtyness();
                     if (var8 > 0.0F) {
                        this.removeDirt(var6, (float)(var3 * 2));
                     }

                     var6.setWetness(100.0F);
                  }
               }

            }
         }
      }
   }

   private void removeBlood(Clothing var1, float var2) {
      ItemVisual var3 = var1.getVisual();
      if (var3 != null) {
         for(int var4 = 0; var4 < BloodBodyPartType.MAX.index(); ++var4) {
            BloodBodyPartType var5 = BloodBodyPartType.FromIndex(var4);
            float var6 = var3.getBlood(var5);
            if (var6 > 0.0F) {
               var3.setBlood(var5, var6 - var2 / 100.0F);
            }
         }

         BloodClothingType.calcTotalBloodLevel(var1);
      }
   }

   private void removeDirt(Clothing var1, float var2) {
      ItemVisual var3 = var1.getVisual();
      if (var3 != null) {
         for(int var4 = 0; var4 < BloodBodyPartType.MAX.index(); ++var4) {
            BloodBodyPartType var5 = BloodBodyPartType.FromIndex(var4);
            float var6 = var3.getDirt(var5);
            if (var6 > 0.0F) {
               var3.setDirt(var5, var6 - var2 / 100.0F);
            }
         }

         BloodClothingType.calcTotalDirtLevel(var1);
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
            if (this.emitter != null && this.emitter.isPlaying("ClothingWasherFinished")) {
               this.emitter.stopSoundByName("ClothingWasherFinished");
            }

            if (this.soundInstance == -1L) {
               this.emitter = IsoWorld.instance.getFreeEmitter(this.getX() + 0.5F, this.getY() + 0.5F, (float)((int)this.getZ()));
               this.soundInstance = this.emitter.playSoundLoopedImpl("ClothingWasherRunning");
            }
         }

         if (!GameClient.bClient) {
            WorldSoundManager.instance.addSound(this, this.square.x, this.square.y, this.square.z, 10, 10);
         }
      } else if (this.soundInstance != -1L) {
         this.emitter.stopSound(this.soundInstance);
         this.soundInstance = -1L;
         if (this.cycleFinished) {
            this.cycleFinished = false;
            this.emitter.playSoundImpl("ClothingWasherFinished", (IsoObject)this);
         } else {
            this.emitter = null;
         }
      }

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

   public boolean isActivated() {
      return this.bActivated;
   }

   public void setActivated(boolean var1) {
      this.bActivated = var1;
      this.alreadyExecuted = false;
   }
}
