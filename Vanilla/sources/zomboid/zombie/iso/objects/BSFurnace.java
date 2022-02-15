package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.characters.skills.PerkFactory;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.DrainableComboItem;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;

public class BSFurnace extends IsoObject {
   public float heat = 0.0F;
   public float heatDecrease = 0.005F;
   public float heatIncrease = 0.001F;
   public float fuelAmount = 0.0F;
   public float fuelDecrease = 0.001F;
   public boolean fireStarted = false;
   private IsoLightSource LightSource;
   public String sSprite;
   public String sLitSprite;

   public BSFurnace(IsoCell var1) {
      super(var1);
   }

   public BSFurnace(IsoCell var1, IsoGridSquare var2, String var3, String var4) {
      super(var1, var2, IsoSpriteManager.instance.getSprite(var3));
      this.sSprite = var3;
      this.sLitSprite = var4;
      this.sprite = IsoSpriteManager.instance.getSprite(var3);
      this.square = var2;
      this.container = new ItemContainer();
      this.container.setType("stonefurnace");
      this.container.setParent(this);
      var2.AddSpecialObject(this);
   }

   public void update() {
      this.updateHeat();
      if (!GameClient.bClient) {
         DrainableComboItem var1 = null;
         InventoryItem var2 = null;

         int var3;
         for(var3 = 0; var3 < this.getContainer().getItems().size(); ++var3) {
            InventoryItem var4 = (InventoryItem)this.getContainer().getItems().get(var3);
            if (var4.getType().equals("IronIngot") && ((DrainableComboItem)var4).getUsedDelta() < 1.0F) {
               var1 = (DrainableComboItem)var4;
            }

            if (var4.getMetalValue() > 0.0F) {
               if (this.getHeat() > 15.0F) {
                  if (var4.getItemHeat() < 2.0F) {
                     var4.setItemHeat(var4.getItemHeat() + 0.001F * (this.getHeat() / 100.0F) * GameTime.instance.getMultiplier());
                  } else {
                     var4.setMeltingTime(var4.getMeltingTime() + 0.1F * (this.getHeat() / 100.0F) * (1.0F + (float)(this.getMeltingSkill(var4) * 3) / 100.0F) * GameTime.instance.getMultiplier());
                  }

                  if (var4.getMeltingTime() == 100.0F) {
                     var2 = var4;
                  }
               } else {
                  var4.setItemHeat(var4.getItemHeat() - 0.001F * (this.getHeat() / 100.0F) * GameTime.instance.getMultiplier());
                  var4.setMeltingTime(var4.getMeltingTime() - 0.1F * (this.getHeat() / 100.0F) * GameTime.instance.getMultiplier());
               }
            }
         }

         if (var2 != null) {
            if (var2.getWorker() != null && !var2.getWorker().isEmpty()) {
               for(var3 = 0; var3 < IsoPlayer.numPlayers; ++var3) {
                  IsoPlayer var7 = IsoPlayer.players[var3];
                  if (var7 != null && !var7.isDead() && var2.getWorker().equals(var7.getFullName())) {
                     break;
                  }
               }
            }

            float var6 = var2.getMetalValue() + (var2.getMetalValue() * (1.0F + (float)(this.getMeltingSkill(var2) * 3) / 100.0F) - var2.getMetalValue());
            if (var1 != null) {
               if (var6 + var1.getUsedDelta() / var1.getUseDelta() > 1.0F / var1.getUseDelta()) {
                  var6 -= 1.0F / var1.getUseDelta() - var1.getUsedDelta() / var1.getUseDelta();
                  var1.setUsedDelta(1.0F);
                  var1 = (DrainableComboItem)InventoryItemFactory.CreateItem("Base.IronIngot");
                  var1.setUsedDelta(0.0F);
                  this.getContainer().addItem(var1);
               }
            } else {
               var1 = (DrainableComboItem)InventoryItemFactory.CreateItem("Base.IronIngot");
               var1.setUsedDelta(0.0F);
               this.getContainer().addItem(var1);
            }

            float var8 = 0.0F;
            float var5 = var6;

            while(var8 < var5) {
               if (var1.getUsedDelta() + var6 * var1.getUseDelta() <= 1.0F) {
                  var1.setUsedDelta(var1.getUsedDelta() + var6 * var1.getUseDelta());
                  var8 += var6;
               } else {
                  var6 -= 1.0F / var1.getUseDelta();
                  var8 += 1.0F / var1.getUseDelta();
                  var1.setUsedDelta(1.0F);
                  var1 = (DrainableComboItem)InventoryItemFactory.CreateItem("Base.IronIngot");
                  var1.setUsedDelta(0.0F);
                  this.getContainer().addItem(var1);
               }
            }

            this.getContainer().Remove(var2);
         }

      }
   }

   private void updateHeat() {
      if (!this.isFireStarted()) {
         this.heat -= this.heatDecrease * GameTime.instance.getMultiplier();
      } else if (this.getFuelAmount() == 0.0F) {
         this.setFireStarted(false);
      } else {
         this.fuelAmount -= this.fuelDecrease * (0.2F + this.heatIncrease / 80.0F) * GameTime.instance.getMultiplier();
         if (this.getHeat() < 20.0F) {
            this.heat += this.heatIncrease * GameTime.instance.getMultiplier();
         }

         this.heat -= this.heatDecrease * 0.05F * GameTime.instance.getMultiplier();
      }

      if (this.heat < 0.0F) {
         this.heat = 0.0F;
      }

      if (this.fuelAmount < 0.0F) {
         this.fuelAmount = 0.0F;
      }

   }

   public int getMeltingSkill(InventoryItem var1) {
      if (var1.getWorker() != null && !var1.getWorker().isEmpty()) {
         for(int var2 = 0; var2 < IsoPlayer.numPlayers; ++var2) {
            IsoPlayer var3 = IsoPlayer.players[var2];
            if (var3 != null && !var3.isDead() && var1.getWorker().equals(var3.getFullName())) {
               return var3.getPerkLevel(PerkFactory.Perks.Melting);
            }
         }
      }

      return 0;
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.fireStarted = var1.get() == 1;
      this.heat = var1.getFloat();
      this.fuelAmount = var1.getFloat();
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.put((byte)(this.isFireStarted() ? 1 : 0));
      var1.putFloat(this.getHeat());
      var1.putFloat(this.getFuelAmount());
   }

   public String getObjectName() {
      return "StoneFurnace";
   }

   public float getHeat() {
      return this.heat;
   }

   public void setHeat(float var1) {
      if (var1 > 100.0F) {
         var1 = 100.0F;
      }

      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      this.heat = var1;
   }

   public boolean isFireStarted() {
      return this.fireStarted;
   }

   public void updateLight() {
      if (this.fireStarted && this.LightSource == null) {
         this.LightSource = new IsoLightSource(this.square.getX(), this.square.getY(), this.square.getZ(), 0.61F, 0.165F, 0.0F, 7);
         IsoWorld.instance.CurrentCell.addLamppost(this.LightSource);
      } else if (this.LightSource != null) {
         IsoWorld.instance.CurrentCell.removeLamppost(this.LightSource);
         this.LightSource = null;
      }

   }

   public void setFireStarted(boolean var1) {
      this.fireStarted = var1;
      this.updateLight();
      this.syncFurnace();
   }

   public void syncFurnace() {
      if (GameServer.bServer) {
         GameServer.sendFuranceChange(this, (UdpConnection)null);
      } else if (GameClient.bClient) {
         GameClient.sendFurnaceChange(this);
      }

   }

   public float getFuelAmount() {
      return this.fuelAmount;
   }

   public void setFuelAmount(float var1) {
      if (var1 > 100.0F) {
         var1 = 100.0F;
      }

      if (var1 < 0.0F) {
         var1 = 0.0F;
      }

      this.fuelAmount = var1;
   }

   public void addFuel(float var1) {
      this.setFuelAmount(this.getFuelAmount() + var1);
   }

   public void addToWorld() {
      IsoWorld.instance.getCell().addToProcessIsoObject(this);
   }

   public void removeFromWorld() {
      if (this.emitter != null) {
         this.emitter.stopAll();
         IsoWorld.instance.returnOwnershipOfEmitter(this.emitter);
         this.emitter = null;
      }

      super.removeFromWorld();
   }

   public float getFuelDecrease() {
      return this.fuelDecrease;
   }

   public void setFuelDecrease(float var1) {
      this.fuelDecrease = var1;
   }

   public float getHeatDecrease() {
      return this.heatDecrease;
   }

   public void setHeatDecrease(float var1) {
      this.heatDecrease = var1;
   }

   public float getHeatIncrease() {
      return this.heatIncrease;
   }

   public void setHeatIncrease(float var1) {
      this.heatIncrease = var1;
   }
}
