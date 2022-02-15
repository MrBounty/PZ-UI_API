package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import zombie.GameTime;
import zombie.SandboxOptions;
import zombie.core.Rand;
import zombie.core.math.PZMath;
import zombie.core.raknet.UdpConnection;
import zombie.inventory.InventoryItem;
import zombie.inventory.ItemContainer;
import zombie.inventory.types.Food;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;

public class IsoCompost extends IsoObject {
   private float compost = 0.0F;
   private float LastUpdated = -1.0F;

   public IsoCompost(IsoCell var1) {
      super(var1);
   }

   public IsoCompost(IsoCell var1, IsoGridSquare var2) {
      super(var1, var2, IsoSpriteManager.instance.getSprite("camping_01_19"));
      this.sprite = IsoSpriteManager.instance.getSprite("camping_01_19");
      this.square = var2;
      this.container = new ItemContainer();
      this.container.setType("crate");
      this.container.setParent(this);
      this.container.bExplored = true;
   }

   public void update() {
      if (!GameClient.bClient && this.container != null) {
         float var1 = (float)GameTime.getInstance().getWorldAgeHours();
         if (this.LastUpdated < 0.0F) {
            this.LastUpdated = var1;
         } else if (this.LastUpdated > var1) {
            this.LastUpdated = var1;
         }

         float var2 = var1 - this.LastUpdated;
         if (!(var2 <= 0.0F)) {
            this.LastUpdated = var1;
            int var3 = SandboxOptions.instance.getCompostHours();

            for(int var4 = 0; var4 < this.container.getItems().size(); ++var4) {
               InventoryItem var5 = (InventoryItem)this.container.getItems().get(var4);
               if (var5 instanceof Food) {
                  Food var6 = (Food)var5;
                  if (GameServer.bServer) {
                     var6.updateAge();
                  }

                  if (var6.isRotten()) {
                     if (this.getCompost() < 100.0F) {
                        var6.setRottenTime(0.0F);
                        var6.setCompostTime(var6.getCompostTime() + var2);
                     }

                     if (var6.getCompostTime() >= (float)var3) {
                        this.setCompost(this.getCompost() + Math.abs(var6.getHungChange()) * 2.0F);
                        if (this.getCompost() > 100.0F) {
                           this.setCompost(100.0F);
                        }

                        if (GameServer.bServer) {
                           GameServer.sendCompost(this, (UdpConnection)null);
                           GameServer.sendRemoveItemFromContainer(this.container, var5);
                        }

                        if (Rand.Next(10) == 0) {
                           InventoryItem var7 = this.container.AddItem("Base.Worm");
                           if (GameServer.bServer && var7 != null) {
                              GameServer.sendAddItemToContainer(this.container, var7);
                           }
                        }

                        var5.Use();
                        IsoWorld.instance.CurrentCell.addToProcessItemsRemove(var5);
                     }
                  }
               }
            }

            this.updateSprite();
         }
      }
   }

   public void updateSprite() {
      if (this.getCompost() >= 10.0F && this.sprite.getName().equals("camping_01_19")) {
         this.sprite = IsoSpriteManager.instance.getSprite("camping_01_20");
         this.transmitUpdatedSpriteToClients();
      } else if (this.getCompost() < 10.0F && this.sprite.getName().equals("camping_01_20")) {
         this.sprite = IsoSpriteManager.instance.getSprite("camping_01_19");
         this.transmitUpdatedSpriteToClients();
      }

   }

   public void syncCompost() {
      if (GameClient.bClient) {
         GameClient.sendCompost(this);
      }

   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.compost = var1.getFloat();
      if (var2 >= 130) {
         this.LastUpdated = var1.getFloat();
      }

   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.putFloat(this.compost);
      var1.putFloat(this.LastUpdated);
   }

   public String getObjectName() {
      return "IsoCompost";
   }

   public float getCompost() {
      return this.compost;
   }

   public void setCompost(float var1) {
      this.compost = PZMath.clamp(var1, 0.0F, 100.0F);
   }

   public void remove() {
      if (this.getSquare() != null) {
         this.getSquare().transmitRemoveItemFromSquare(this);
      }
   }

   public void addToWorld() {
      this.getCell().addToProcessIsoObject(this);
   }
}
