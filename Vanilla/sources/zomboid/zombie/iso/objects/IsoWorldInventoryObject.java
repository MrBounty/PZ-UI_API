package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import se.krka.kahlua.vm.KahluaTable;
import zombie.GameTime;
import zombie.Lua.LuaEventManager;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.logger.ExceptionLogger;
import zombie.core.opengl.Shader;
import zombie.core.skinnedmodel.model.WorldItemModelDrawer;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.debug.DebugOptions;
import zombie.debug.LineDrawer;
import zombie.input.Mouse;
import zombie.inventory.InventoryItem;
import zombie.inventory.InventoryItemFactory;
import zombie.inventory.ItemContainer;
import zombie.inventory.ItemSoundManager;
import zombie.inventory.types.DrainableComboItem;
import zombie.inventory.types.InventoryContainer;
import zombie.iso.IsoCamera;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.PlayerCamera;
import zombie.iso.sprite.IsoDirectionFrame;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.network.GameServer;
import zombie.network.ServerGUI;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.Item;
import zombie.ui.ObjectTooltip;
import zombie.util.Type;

public class IsoWorldInventoryObject extends IsoObject {
   public InventoryItem item;
   public float xoff;
   public float yoff;
   public float zoff;
   public boolean removeProcess = false;
   public double dropTime = -1.0D;
   public boolean ignoreRemoveSandbox = false;

   public IsoWorldInventoryObject(InventoryItem var1, IsoGridSquare var2, float var3, float var4, float var5) {
      this.OutlineOnMouseover = true;
      if (var1.worldZRotation <= 0) {
         var1.worldZRotation = Rand.Next(0, 360);
      }

      var1.setContainer((ItemContainer)null);
      this.xoff = var3;
      this.yoff = var4;
      this.zoff = var5;
      if (this.xoff == 0.0F) {
         this.xoff = (float)Rand.Next(1000) / 1000.0F;
      }

      if (this.yoff == 0.0F) {
         this.yoff = (float)Rand.Next(1000) / 1000.0F;
      }

      this.item = var1;
      this.sprite = IsoSprite.CreateSprite(IsoSpriteManager.instance);
      this.updateSprite();
      this.square = var2;
      this.offsetY = 0.0F;
      this.offsetX = 0.0F;
      this.dropTime = GameTime.getInstance().getWorldAgeHours();
   }

   public IsoWorldInventoryObject(IsoCell var1) {
      super(var1);
      this.offsetY = 0.0F;
      this.offsetX = 0.0F;
   }

   public void swapItem(InventoryItem var1) {
      if (var1 != null) {
         if (this.getItem() != null) {
            IsoWorld.instance.CurrentCell.addToProcessItemsRemove(this.getItem());
            ItemSoundManager.removeItem(this.getItem());
            this.getItem().setWorldItem((IsoWorldInventoryObject)null);
            var1.setID(this.getItem().getID());
            var1.worldScale = this.getItem().worldScale;
            var1.worldZRotation = this.getItem().worldZRotation;
         }

         this.item = var1;
         if (var1.getWorldItem() != null) {
            throw new IllegalArgumentException("newItem.getWorldItem() != null");
         } else {
            this.getItem().setWorldItem(this);
            this.setKeyId(this.getItem().getKeyId());
            this.setName(this.getItem().getName());
            if (this.getItem().shouldUpdateInWorld()) {
               IsoWorld.instance.CurrentCell.addToProcessWorldItems(this);
            }

            IsoWorld.instance.CurrentCell.addToProcessItems(var1);
            this.updateSprite();
            LuaEventManager.triggerEvent("OnContainerUpdate");
            if (GameServer.bServer) {
               this.sendObjectChange("swapItem");
            }

         }
      }
   }

   public void saveChange(String var1, KahluaTable var2, ByteBuffer var3) {
      if ("swapItem".equals(var1)) {
         if (this.getItem() == null) {
            return;
         }

         try {
            this.getItem().saveWithSize(var3, false);
         } catch (Exception var5) {
            ExceptionLogger.logException(var5);
         }
      } else {
         super.saveChange(var1, var2, var3);
      }

   }

   public void loadChange(String var1, ByteBuffer var2) {
      if ("swapItem".equals(var1)) {
         try {
            InventoryItem var3 = InventoryItem.loadItem(var2, 186);
            if (var3 != null) {
               this.swapItem(var3);
            }
         } catch (Exception var4) {
            ExceptionLogger.logException(var4);
         }
      } else {
         super.loadChange(var1, var2);
      }

   }

   private boolean isWaterSource() {
      if (this.item == null) {
         return false;
      } else {
         if (this.item.isBroken()) {
         }

         if (!this.item.canStoreWater()) {
            return false;
         } else if (this.item.isWaterSource() && this.item instanceof DrainableComboItem) {
            return ((DrainableComboItem)this.item).getRainFactor() > 0.0F;
         } else {
            if (this.item.getReplaceOnUseOn() != null && this.item.getReplaceOnUseOnString() != null) {
               Item var1 = ScriptManager.instance.getItem(this.item.getReplaceOnUseOnString());
               if (var1 != null && var1.getType() == Item.Type.Drainable) {
                  return var1.getCanStoreWater() && var1.getRainFactor() > 0.0F;
               }
            }

            return false;
         }
      }
   }

   public int getWaterAmount() {
      if (this.isWaterSource()) {
         return this.item instanceof DrainableComboItem ? ((DrainableComboItem)this.item).getRemainingUses() : 0;
      } else {
         return 0;
      }
   }

   public void setWaterAmount(int var1) {
      if (this.isWaterSource()) {
         DrainableComboItem var2 = (DrainableComboItem)Type.tryCastTo(this.item, DrainableComboItem.class);
         InventoryItem var3;
         if (var2 != null) {
            var2.setUsedDelta((float)var1 * var2.getUseDelta());
            if (var1 == 0 && var2.getReplaceOnDeplete() != null) {
               var3 = InventoryItemFactory.CreateItem(var2.getReplaceOnDepleteFullType());
               if (var3 != null) {
                  var3.setCondition(this.getItem().getCondition());
                  var3.setFavorite(this.getItem().isFavorite());
                  this.swapItem(var3);
               }
            }
         } else if (var1 > 0) {
            var3 = InventoryItemFactory.CreateItem(this.getItem().getReplaceOnUseOnString());
            if (var3 != null) {
               var3.setCondition(this.getItem().getCondition());
               var3.setFavorite(this.getItem().isFavorite());
               var3.setTaintedWater(this.getItem().isTaintedWater());
               var2 = (DrainableComboItem)Type.tryCastTo(var3, DrainableComboItem.class);
               if (var2 != null) {
                  var2.setUsedDelta((float)var1 * var2.getUseDelta());
               }

               this.swapItem(var3);
            }
         }
      }

   }

   public int getWaterMax() {
      if (this.isWaterSource()) {
         float var1;
         if (this.item instanceof DrainableComboItem) {
            var1 = 1.0F / ((DrainableComboItem)this.item).getUseDelta();
         } else {
            Item var2 = ScriptManager.instance.getItem(this.item.getReplaceOnUseOnString());
            var1 = 1.0F / var2.getUseDelta();
         }

         return var1 - (float)((int)var1) > 0.99F ? (int)var1 + 1 : (int)var1;
      } else {
         return 0;
      }
   }

   public boolean isTaintedWater() {
      return this.isWaterSource() ? this.getItem().isTaintedWater() : false;
   }

   public void setTaintedWater(boolean var1) {
      if (this.isWaterSource()) {
         this.getItem().setTaintedWater(var1);
      }

   }

   public void update() {
      IsoCell var1 = IsoWorld.instance.getCell();
      if (!this.removeProcess && this.item != null && this.item.shouldUpdateInWorld()) {
         var1.addToProcessItems(this.item);
      }

   }

   public void updateSprite() {
      this.sprite.setTintMod(new ColorInfo(this.item.col.r, this.item.col.g, this.item.col.b, this.item.col.a));
      if (!GameServer.bServer || ServerGUI.isCreated()) {
         String var1 = this.item.getTex().getName();
         if (this.item.isUseWorldItem()) {
            var1 = this.item.getWorldTexture();
         }

         Texture var2;
         try {
            var2 = Texture.getSharedTexture(var1);
            if (var2 == null) {
               var1 = this.item.getTex().getName();
            }
         } catch (Exception var4) {
            var1 = "media/inventory/world/WItem_Sack.png";
         }

         var2 = this.sprite.LoadFrameExplicit(var1);
         if (this.item.getScriptItem() == null) {
            this.sprite.def.scaleAspect((float)var2.getWidthOrig(), (float)var2.getHeightOrig(), (float)(16 * Core.TileScale), (float)(16 * Core.TileScale));
         } else {
            float var10001 = (float)Core.TileScale;
            float var3 = this.item.getScriptItem().ScaleWorldIcon * (var10001 / 2.0F);
            this.sprite.def.setScale(var3, var3);
         }

      }
   }

   public boolean finishupdate() {
      return this.removeProcess || this.item == null || !this.item.shouldUpdateInWorld();
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      this.xoff = var1.getFloat();
      this.yoff = var1.getFloat();
      this.zoff = var1.getFloat();
      float var4 = var1.getFloat();
      float var5 = var1.getFloat();
      this.sprite = IsoSprite.CreateSprite(IsoSpriteManager.instance);
      this.item = InventoryItem.loadItem(var1, var2);
      if (this.item == null) {
         var1.getDouble();
      } else {
         this.item.setWorldItem(this);
         this.sprite.getTintMod().r = this.item.getR();
         this.sprite.getTintMod().g = this.item.getG();
         this.sprite.getTintMod().b = this.item.getB();
         if (var2 >= 108) {
            this.dropTime = var1.getDouble();
         } else {
            this.dropTime = GameTime.getInstance().getWorldAgeHours();
         }

         if (!GameServer.bServer || ServerGUI.isCreated()) {
            String var6 = this.item.getTex().getName();
            if (this.item.isUseWorldItem()) {
               var6 = this.item.getWorldTexture();
            }

            Texture var7;
            try {
               var7 = Texture.getSharedTexture(var6);
               if (var7 == null) {
                  var6 = this.item.getTex().getName();
               }
            } catch (Exception var9) {
               var6 = "media/inventory/world/WItem_Sack.png";
            }

            var7 = this.sprite.LoadFrameExplicit(var6);
            if (var7 != null) {
               if (var2 < 33) {
                  float var10000 = var4 - (float)(var7.getWidthOrig() / 2);
                  var10000 = var5 - (float)var7.getHeightOrig();
               }

               if (this.item.getScriptItem() == null) {
                  this.sprite.def.scaleAspect((float)var7.getWidthOrig(), (float)var7.getHeightOrig(), (float)(16 * Core.TileScale), (float)(16 * Core.TileScale));
               } else {
                  float var10001 = (float)Core.TileScale;
                  float var8 = this.item.getScriptItem().ScaleWorldIcon * (var10001 / 2.0F);
                  this.sprite.def.setScale(var8, var8);
               }

            }
         }
      }
   }

   public boolean Serialize() {
      return true;
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      var1.put((byte)(this.Serialize() ? 1 : 0));
      if (this.Serialize()) {
         var1.put(IsoObject.factoryGetClassID(this.getObjectName()));
         var1.putFloat(this.xoff);
         var1.putFloat(this.yoff);
         var1.putFloat(this.zoff);
         var1.putFloat(this.offsetX);
         var1.putFloat(this.offsetY);
         this.item.saveWithSize(var1, false);
         var1.putDouble(this.dropTime);
      }
   }

   public void softReset() {
      this.square.removeWorldObject(this);
   }

   public String getObjectName() {
      return "WorldInventoryItem";
   }

   public void DoTooltip(ObjectTooltip var1) {
      this.item.DoTooltip(var1);
   }

   public boolean HasTooltip() {
      return false;
   }

   public boolean onMouseLeftClick(int var1, int var2) {
      return false;
   }

   private void debugDrawLocation(float var1, float var2, float var3) {
      if (Core.bDebug && DebugOptions.instance.ModelRenderAxis.getValue()) {
         var1 += this.xoff;
         var2 += this.yoff;
         var3 += this.zoff;
         LineDrawer.DrawIsoLine(var1 - 0.25F, var2, var3, var1 + 0.25F, var2, var3, 1.0F, 1.0F, 1.0F, 0.5F, 1);
         LineDrawer.DrawIsoLine(var1, var2 - 0.25F, var3, var1, var2 + 0.25F, var3, 1.0F, 1.0F, 1.0F, 0.5F, 1);
      }

   }

   private void debugHitTest() {
      int var1 = IsoCamera.frameState.playerIndex;
      float var2 = Core.getInstance().getZoom(var1);
      float var3 = (float)Mouse.getXA();
      float var4 = (float)Mouse.getYA();
      var3 -= (float)IsoCamera.getScreenLeft(var1);
      var4 -= (float)IsoCamera.getScreenTop(var1);
      var3 *= var2;
      var4 *= var2;
      float var5 = this.getScreenPosX(var1) * var2;
      float var6 = this.getScreenPosY(var1) * var2;
      float var7 = IsoUtils.DistanceTo2D(var5, var6, var3, var4);
      byte var8 = 48;
      if (var7 < (float)var8) {
         LineDrawer.drawCircle(var5, var6, (float)var8, 16, 1.0F, 1.0F, 1.0F);
      }

   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      if (Core.bDebug) {
      }

      if (this.getItem().getScriptItem().isWorldRender()) {
         if (WorldItemModelDrawer.renderMain(this.getItem(), this.getSquare(), this.getX() + this.xoff, this.getY() + this.yoff, this.getZ() + this.zoff, 0.0F)) {
            this.debugDrawLocation(var1, var2, var3);
         } else if (this.sprite.CurrentAnim != null && !this.sprite.CurrentAnim.Frames.isEmpty()) {
            Texture var8 = ((IsoDirectionFrame)this.sprite.CurrentAnim.Frames.get(0)).getTexture(this.dir);
            if (var8 != null) {
               float var9 = (float)var8.getWidthOrig() * this.sprite.def.getScaleX() / 2.0F;
               float var10 = (float)var8.getHeightOrig() * this.sprite.def.getScaleY() * 3.0F / 4.0F;
               this.sprite.render(this, var1 + this.xoff, var2 + this.yoff, var3 + this.zoff, this.dir, this.offsetX + var9, this.offsetY + var10, var4, true);
               this.debugDrawLocation(var1, var2, var3);
            }
         }
      }
   }

   public void renderObjectPicker(float var1, float var2, float var3, ColorInfo var4) {
      if (this.sprite != null) {
         if (this.sprite.CurrentAnim != null && !this.sprite.CurrentAnim.Frames.isEmpty()) {
            Texture var5 = ((IsoDirectionFrame)this.sprite.CurrentAnim.Frames.get(0)).getTexture(this.dir);
            if (var5 != null) {
               float var6 = (float)(var5.getWidthOrig() / 2);
               float var7 = (float)var5.getHeightOrig();
               this.sprite.renderObjectPicker(this.sprite.def, this, this.dir);
            }
         }
      }
   }

   public InventoryItem getItem() {
      return this.item;
   }

   public void addToWorld() {
      if (this.item != null && this.item.shouldUpdateInWorld() && !IsoWorld.instance.CurrentCell.getProcessWorldItems().contains(this)) {
         IsoWorld.instance.CurrentCell.getProcessWorldItems().add(this);
      }

      if (this.item instanceof InventoryContainer) {
         ItemContainer var1 = ((InventoryContainer)this.item).getInventory();
         if (var1 != null) {
            var1.addItemsToProcessItems();
         }
      }

      super.addToWorld();
   }

   public void removeFromWorld() {
      this.removeProcess = true;
      IsoWorld.instance.getCell().getProcessWorldItems().remove(this);
      if (this.item != null) {
         IsoWorld.instance.CurrentCell.addToProcessItemsRemove(this.item);
         ItemSoundManager.removeItem(this.item);
      }

      if (this.item instanceof InventoryContainer) {
         ItemContainer var1 = ((InventoryContainer)this.item).getInventory();
         if (var1 != null) {
            var1.removeItemsFromProcessItems();
         }
      }

      super.removeFromWorld();
   }

   public void removeFromSquare() {
      if (this.square != null) {
         this.square.getWorldObjects().remove(this);
         this.square.chunk.recalcHashCodeObjects();
      }

      super.removeFromSquare();
   }

   public float getScreenPosX(int var1) {
      float var2 = IsoUtils.XToScreen(this.getX() + this.xoff, this.getY() + this.yoff, this.getZ() + this.zoff, 0);
      PlayerCamera var3 = IsoCamera.cameras[var1];
      return (var2 - var3.getOffX()) / Core.getInstance().getZoom(var1);
   }

   public float getScreenPosY(int var1) {
      Texture var2 = this.sprite == null ? null : this.sprite.getTextureForCurrentFrame(this.dir);
      float var3 = var2 == null ? 0.0F : (float)var2.getHeightOrig() * this.sprite.def.getScaleY() * 1.0F / 4.0F;
      float var4 = IsoUtils.YToScreen(this.getX() + this.xoff, this.getY() + this.yoff, this.getZ() + this.zoff, 0);
      PlayerCamera var5 = IsoCamera.cameras[var1];
      return (var4 - var5.getOffY() - var3) / Core.getInstance().getZoom(var1);
   }

   public void setIgnoreRemoveSandbox(boolean var1) {
      this.ignoreRemoveSandbox = var1;
   }

   public boolean isIgnoreRemoveSandbox() {
      return this.ignoreRemoveSandbox;
   }

   public float getWorldPosX() {
      return this.getX() + this.xoff;
   }

   public float getWorldPosY() {
      return this.getY() + this.yoff;
   }

   public float getWorldPosZ() {
      return this.getZ() + this.zoff;
   }
}
