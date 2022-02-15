package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.core.Core;
import zombie.core.opengl.Shader;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.debug.DebugType;
import zombie.inventory.ItemContainer;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoHeatSource;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoObject;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameClient;
import zombie.network.GameServer;

public class IsoFireplace extends IsoObject {
   int FuelAmount = 0;
   boolean bLit = false;
   boolean bSmouldering = false;
   protected float LastUpdateTime = -1.0F;
   protected float MinuteAccumulator = 0.0F;
   protected int MinutesSinceExtinguished = -1;
   protected IsoSprite FuelSprite = null;
   protected int FuelSpriteIndex = -1;
   protected int FireSpriteIndex = -1;
   protected IsoLightSource LightSource = null;
   protected IsoHeatSource heatSource = null;
   private static int SMOULDER_MINUTES = 10;

   public IsoFireplace(IsoCell var1) {
      super(var1);
   }

   public IsoFireplace(IsoCell var1, IsoGridSquare var2, IsoSprite var3) {
      super(var1, var2, var3);
      String var4 = var3 != null && var3.getProperties().Is(IsoFlagType.container) ? var3.getProperties().Val("container") : "fireplace";
      this.container = new ItemContainer(var4, var2, this);
      this.container.setExplored(true);
   }

   public String getObjectName() {
      return "Fireplace";
   }

   public Vector2 getFacingPosition(Vector2 var1) {
      if (this.square == null) {
         return var1.set(0.0F, 0.0F);
      } else {
         return this.getProperties() != null && this.getProperties().Is(IsoFlagType.collideN) ? var1.set(this.getX() + 0.5F, this.getY()) : var1.set(this.getX(), this.getY() + 0.5F);
      }
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.FuelAmount = var1.getInt();
      this.bLit = var1.get() == 1;
      this.LastUpdateTime = var1.getFloat();
      this.MinutesSinceExtinguished = var1.getInt();
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
      var1.putInt(this.FuelAmount);
      var1.put((byte)(this.bLit ? 1 : 0));
      var1.putFloat(this.LastUpdateTime);
      var1.putInt(this.MinutesSinceExtinguished);
   }

   public void setFuelAmount(int var1) {
      var1 = Math.max(0, var1);
      int var2 = this.getFuelAmount();
      if (var1 != var2) {
         this.FuelAmount = var1;
      }

   }

   public int getFuelAmount() {
      return this.FuelAmount;
   }

   public void addFuel(int var1) {
      this.setFuelAmount(this.getFuelAmount() + var1);
   }

   public int useFuel(int var1) {
      int var2 = this.getFuelAmount();
      boolean var3 = false;
      int var4;
      if (var2 >= var1) {
         var4 = var1;
      } else {
         var4 = var2;
      }

      this.setFuelAmount(var2 - var4);
      return var4;
   }

   public boolean hasFuel() {
      return this.getFuelAmount() > 0;
   }

   public void setLit(boolean var1) {
      this.bLit = var1;
   }

   public boolean isLit() {
      return this.bLit;
   }

   public boolean isSmouldering() {
      return this.bSmouldering;
   }

   public void extinguish() {
      if (this.isLit()) {
         this.setLit(false);
         if (this.hasFuel()) {
            this.MinutesSinceExtinguished = 0;
         }
      }

   }

   public float getTemperature() {
      return this.isLit() ? 1.8F : 1.0F;
   }

   private void updateFuelSprite() {
      if (this.container == null || !"woodstove".equals(this.container.getType())) {
         if (this.hasFuel()) {
            if (this.FuelSprite == null) {
               this.FuelSprite = IsoSprite.CreateSprite(IsoSpriteManager.instance);
               Texture var1 = this.FuelSprite.LoadFrameExplicit("Item_Logs");
            }

            if (this.FuelSpriteIndex == -1) {
               DebugLog.log(DebugType.Fireplace, "fireplace: added fuel sprite");
               this.FuelSpriteIndex = this.AttachedAnimSprite != null ? this.AttachedAnimSprite.size() : 0;
               if (this.getProperties() != null && this.getProperties().Is(IsoFlagType.collideW)) {
                  this.AttachExistingAnim(this.FuelSprite, -10 * Core.TileScale, -90 * Core.TileScale, false, 0, false, 0.0F);
               } else {
                  this.AttachExistingAnim(this.FuelSprite, -35 * Core.TileScale, -90 * Core.TileScale, false, 0, false, 0.0F);
               }

               if (Core.TileScale == 1) {
                  ((IsoSpriteInstance)this.AttachedAnimSprite.get(this.FuelSpriteIndex)).setScale(0.5F, 0.5F);
               }
            }
         } else if (this.FuelSpriteIndex != -1) {
            DebugLog.log(DebugType.Fireplace, "fireplace: removed fuel sprite");
            this.AttachedAnimSprite.remove(this.FuelSpriteIndex);
            if (this.FireSpriteIndex > this.FuelSpriteIndex) {
               --this.FireSpriteIndex;
            }

            this.FuelSpriteIndex = -1;
         }

      }
   }

   private void updateFireSprite() {
      if (this.container == null || !"woodstove".equals(this.container.getType())) {
         if (this.isLit()) {
            if (this.FireSpriteIndex == -1) {
               DebugLog.log(DebugType.Fireplace, "fireplace: added fire sprite");
               this.FireSpriteIndex = this.AttachedAnimSprite != null ? this.AttachedAnimSprite.size() : 0;
               if (this.getProperties() != null && this.getProperties().Is(IsoFlagType.collideW)) {
                  this.AttachAnim("Fire", "01", 4, IsoFireManager.FireAnimDelay, -11 * Core.TileScale, -84 * Core.TileScale, true, 0, false, 0.7F, IsoFireManager.FireTintMod);
               } else {
                  this.AttachAnim("Fire", "01", 4, IsoFireManager.FireAnimDelay, -35 * Core.TileScale, -84 * Core.TileScale, true, 0, false, 0.7F, IsoFireManager.FireTintMod);
               }

               if (Core.TileScale == 1) {
                  ((IsoSpriteInstance)this.AttachedAnimSprite.get(this.FireSpriteIndex)).setScale(0.5F, 0.5F);
               }
            }
         } else if (this.FireSpriteIndex != -1) {
            DebugLog.log(DebugType.Fireplace, "fireplace: removed fire sprite");
            this.AttachedAnimSprite.remove(this.FireSpriteIndex);
            if (this.FuelSpriteIndex > this.FireSpriteIndex) {
               --this.FuelSpriteIndex;
            }

            this.FireSpriteIndex = -1;
         }

      }
   }

   private int calcLightRadius() {
      return (int)GameTime.instance.Lerp(1.0F, 8.0F, (float)Math.min(this.getFuelAmount(), 60) / 60.0F);
   }

   private void updateLightSource() {
      if (this.isLit()) {
         int var1 = this.calcLightRadius();
         if (this.LightSource != null && this.LightSource.getRadius() != var1) {
            this.LightSource.life = 0;
            this.LightSource = null;
         }

         if (this.LightSource == null) {
            this.LightSource = new IsoLightSource(this.square.getX(), this.square.getY(), this.square.getZ(), 1.0F, 0.1F, 0.1F, var1);
            IsoWorld.instance.CurrentCell.addLamppost(this.LightSource);
            IsoGridSquare.RecalcLightTime = -1;
            GameTime.instance.lightSourceUpdate = 100.0F;
         }
      } else if (this.LightSource != null) {
         IsoWorld.instance.CurrentCell.removeLamppost(this.LightSource);
         this.LightSource = null;
      }

   }

   private void updateHeatSource() {
      if (this.isLit()) {
         int var1 = this.calcLightRadius();
         if (this.heatSource == null) {
            this.heatSource = new IsoHeatSource((int)this.getX(), (int)this.getY(), (int)this.getZ(), var1, 35);
            IsoWorld.instance.CurrentCell.addHeatSource(this.heatSource);
         } else if (var1 != this.heatSource.getRadius()) {
            this.heatSource.setRadius(var1);
         }
      } else if (this.heatSource != null) {
         IsoWorld.instance.CurrentCell.removeHeatSource(this.heatSource);
         this.heatSource = null;
      }

   }

   public void update() {
      if (!GameClient.bClient) {
         boolean var1 = this.hasFuel();
         boolean var2 = this.isLit();
         int var3 = this.calcLightRadius();
         float var4 = (float)GameTime.getInstance().getWorldAgeHours();
         if (this.LastUpdateTime < 0.0F) {
            this.LastUpdateTime = var4;
         } else if (this.LastUpdateTime > var4) {
            this.LastUpdateTime = var4;
         }

         if (var4 > this.LastUpdateTime) {
            this.MinuteAccumulator += (var4 - this.LastUpdateTime) * 60.0F;
            int var5 = (int)Math.floor((double)this.MinuteAccumulator);
            if (var5 > 0) {
               if (this.isLit()) {
                  DebugLog.log(DebugType.Fireplace, "IsoFireplace burned " + var5 + " minutes (" + this.getFuelAmount() + " remaining)");
                  this.useFuel(var5);
                  if (!this.hasFuel()) {
                     this.extinguish();
                  }
               } else if (this.MinutesSinceExtinguished != -1) {
                  int var6 = Math.min(var5, SMOULDER_MINUTES - this.MinutesSinceExtinguished);
                  DebugLog.log(DebugType.Fireplace, "IsoFireplace smoldered " + var6 + " minutes (" + this.getFuelAmount() + " remaining)");
                  this.MinutesSinceExtinguished += var5;
                  this.useFuel(var6);
                  this.bSmouldering = true;
                  if (!this.hasFuel() || this.MinutesSinceExtinguished >= SMOULDER_MINUTES) {
                     this.MinutesSinceExtinguished = -1;
                     this.bSmouldering = false;
                  }
               }

               this.MinuteAccumulator -= (float)var5;
            }
         }

         this.LastUpdateTime = var4;
         if (GameServer.bServer) {
            if (var1 != this.hasFuel() || var2 != this.isLit() || var3 != this.calcLightRadius()) {
               this.sendObjectChange("state");
            }

            return;
         }
      }

      this.updateFuelSprite();
      this.updateFireSprite();
      this.updateLightSource();
      this.updateHeatSource();
      if (this.AttachedAnimSprite != null && !this.AttachedAnimSprite.isEmpty()) {
         int var7 = this.AttachedAnimSprite.size();

         for(int var8 = 0; var8 < var7; ++var8) {
            IsoSpriteInstance var9 = (IsoSpriteInstance)this.AttachedAnimSprite.get(var8);
            IsoSprite var10 = var9.parentSprite;
            var9.update();
            float var11 = GameTime.instance.getMultipliedSecondsSinceLastUpdate() * 60.0F;
            var9.Frame += var9.AnimFrameIncrease * var11;
            if ((int)var9.Frame >= var10.CurrentAnim.Frames.size() && var10.Loop && var9.Looped) {
               var9.Frame = 0.0F;
            }
         }
      }

   }

   public void addToWorld() {
      IsoCell var1 = this.getCell();
      var1.addToProcessIsoObject(this);
      this.container.addItemsToProcessItems();
   }

   public void removeFromWorld() {
      if (this.LightSource != null) {
         IsoWorld.instance.CurrentCell.removeLamppost(this.LightSource);
         this.LightSource = null;
      }

      if (this.heatSource != null) {
         IsoWorld.instance.CurrentCell.removeHeatSource(this.heatSource);
         this.heatSource = null;
      }

      super.removeFromWorld();
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      super.render(var1, var2, var3, var4, false, var6, var7);
      if (this.AttachedAnimSprite != null) {
         for(int var8 = 0; var8 < this.AttachedAnimSprite.size(); ++var8) {
            IsoSpriteInstance var9 = (IsoSpriteInstance)this.AttachedAnimSprite.get(var8);
            var9.getParentSprite().render(var9, this, var1, var2, var3, this.dir, this.offsetX, this.offsetY, var4, true);
         }

      }
   }

   public void saveChange(String var1, KahluaTable var2, ByteBuffer var3) {
      if ("state".equals(var1)) {
         var3.putInt(this.getFuelAmount());
         var3.put((byte)(this.isLit() ? 1 : 0));
      }

   }

   public void loadChange(String var1, ByteBuffer var2) {
      if ("state".equals(var1)) {
         this.setFuelAmount(var2.getInt());
         this.setLit(var2.get() == 1);
      }

   }
}
