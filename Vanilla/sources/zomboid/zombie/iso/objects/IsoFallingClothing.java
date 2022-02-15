package zombie.iso.objects;

import zombie.Lua.LuaEventManager;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.opengl.Shader;
import zombie.core.skinnedmodel.model.WorldItemModelDrawer;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.inventory.InventoryItem;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoPhysicsObject;

public class IsoFallingClothing extends IsoPhysicsObject {
   private InventoryItem clothing = null;
   private int dropTimer = 0;
   public boolean addWorldItem = true;

   public String getObjectName() {
      return "FallingClothing";
   }

   public IsoFallingClothing(IsoCell var1) {
      super(var1);
   }

   public IsoFallingClothing(IsoCell var1, float var2, float var3, float var4, float var5, float var6, InventoryItem var7) {
      super(var1);
      this.clothing = var7;
      this.dropTimer = 60;
      this.velX = var5;
      this.velY = var6;
      float var8 = (float)Rand.Next(4000) / 10000.0F;
      float var9 = (float)Rand.Next(4000) / 10000.0F;
      var8 -= 0.2F;
      var9 -= 0.2F;
      this.velX += var8;
      this.velY += var9;
      this.x = var2;
      this.y = var3;
      this.z = var4;
      this.nx = var2;
      this.ny = var3;
      this.offsetX = 0.0F;
      this.offsetY = 0.0F;
      this.terminalVelocity = -0.02F;
      Texture var10 = this.sprite.LoadFrameExplicit(var7.getTex().getName());
      if (var10 != null) {
         this.sprite.Animate = false;
         int var11 = Core.TileScale;
         this.sprite.def.scaleAspect((float)var10.getWidthOrig(), (float)var10.getHeightOrig(), (float)(16 * var11), (float)(16 * var11));
      }

      this.speedMod = 4.5F;
   }

   public void collideGround() {
      this.drop();
   }

   public void collideWall() {
      this.drop();
   }

   public void update() {
      super.update();
      --this.dropTimer;
      if (this.dropTimer <= 0) {
         this.drop();
      }

   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      float var8 = (float)(60 - this.dropTimer) / 60.0F * 360.0F;
      if (!WorldItemModelDrawer.renderMain(this.clothing, this.getCurrentSquare(), this.getX(), this.getY(), this.getZ(), var8)) {
         super.render(var1, var2, var3, var4, var5, var6, var7);
      }
   }

   void drop() {
      IsoGridSquare var1 = this.getCurrentSquare();
      if (var1 != null && this.clothing != null) {
         if (this.addWorldItem) {
            float var2 = var1.getApparentZ(this.getX() % 1.0F, this.getY() % 1.0F);
            var1.AddWorldInventoryItem(this.clothing, this.getX() % 1.0F, this.getY() % 1.0F, var2 - (float)var1.getZ());
         }

         this.clothing = null;
         this.setDestroyed(true);
         var1.getMovingObjects().remove(this);
         this.getCell().Remove(this);
         LuaEventManager.triggerEvent("OnContainerUpdate", var1);
      }

   }

   void Trigger() {
   }
}
