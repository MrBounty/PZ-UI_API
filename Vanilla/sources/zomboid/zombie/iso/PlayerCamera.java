package zombie.iso;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import zombie.GameWindow;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.math.PZMath;
import zombie.input.GameKeyboard;
import zombie.input.JoypadManager;
import zombie.input.Mouse;
import zombie.iso.sprite.IsoSprite;
import zombie.network.GameServer;
import zombie.ui.UIManager;
import zombie.vehicles.BaseVehicle;

public final class PlayerCamera {
   public final int playerIndex;
   public float OffX;
   public float OffY;
   public float TOffX;
   public float TOffY;
   public float lastOffX;
   public float lastOffY;
   public float RightClickTargetX;
   public float RightClickTargetY;
   public float RightClickX;
   public float RightClickY;
   private float RightClickX_f;
   private float RightClickY_f;
   public float DeferedX;
   public float DeferedY;
   public float zoom;
   public int OffscreenWidth;
   public int OffscreenHeight;
   private static final Vector2 offVec = new Vector2();
   private static float PAN_SPEED = 1.0F;
   private long panTime = -1L;
   private final Vector3f m_lastVehicleForwardDirection = new Vector3f();

   public PlayerCamera(int var1) {
      this.playerIndex = var1;
   }

   public void center() {
      float var1 = this.OffX;
      float var2 = this.OffY;
      if (IsoCamera.CamCharacter != null) {
         IsoGameCharacter var3 = IsoCamera.CamCharacter;
         var1 = IsoUtils.XToScreen(var3.x + this.DeferedX, var3.y + this.DeferedY, var3.z, 0);
         var2 = IsoUtils.YToScreen(var3.x + this.DeferedX, var3.y + this.DeferedY, var3.z, 0);
         var1 -= (float)(IsoCamera.getOffscreenWidth(this.playerIndex) / 2);
         var2 -= (float)(IsoCamera.getOffscreenHeight(this.playerIndex) / 2);
         var2 -= var3.getOffsetY() * 1.5F;
         var1 += (float)IsoCamera.PLAYER_OFFSET_X;
         var2 += (float)IsoCamera.PLAYER_OFFSET_Y;
      }

      this.OffX = this.TOffX = var1;
      this.OffY = this.TOffY = var2;
   }

   public void update() {
      this.center();
      float var1 = (this.TOffX - this.OffX) / 15.0F;
      float var2 = (this.TOffY - this.OffY) / 15.0F;
      this.OffX += var1;
      this.OffY += var2;
      if (this.lastOffX == 0.0F && this.lastOffY == 0.0F) {
         this.lastOffX = this.OffX;
         this.lastOffY = this.OffY;
      }

      long var3 = System.currentTimeMillis();
      PAN_SPEED = 110.0F;
      float var5 = this.panTime < 0L ? 1.0F : (float)(var3 - this.panTime) / 1000.0F * PAN_SPEED;
      var5 = 1.0F / var5;
      this.panTime = var3;
      IsoPlayer var6 = IsoPlayer.players[this.playerIndex];
      boolean var7 = GameWindow.ActivatedJoyPad != null && var6 != null && var6.JoypadBind != -1;
      BaseVehicle var8 = var6 == null ? null : var6.getVehicle();
      if (var8 != null && var8.getCurrentSpeedKmHour() <= 1.0F) {
         var8.getForwardVector(this.m_lastVehicleForwardDirection);
      }

      int var15;
      float var17;
      float var18;
      float var19;
      float var20;
      float var29;
      if (Core.getInstance().getOptionPanCameraWhileDriving() && var8 != null && var8.getCurrentSpeedKmHour() > 1.0F) {
         float var23 = Core.getInstance().getZoom(this.playerIndex);
         float var25 = var8.getCurrentSpeedKmHour() * BaseVehicle.getFakeSpeedModifier() / 10.0F;
         var25 *= var23;
         Vector3f var27 = var8.getForwardVector((Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc());
         float var28 = this.m_lastVehicleForwardDirection.angle(var27) * 57.295776F;
         if (var28 > 1.0F) {
            var29 = var28 / 180.0F / (float)PerformanceSettings.getLockFPS();
            var29 = PZMath.max(var29, 0.1F);
            this.m_lastVehicleForwardDirection.lerp(var27, var29, var27);
            this.m_lastVehicleForwardDirection.set((Vector3fc)var27);
         }

         this.RightClickTargetX = (float)((int)IsoUtils.XToScreen(var27.x * var25, var27.z * var25, var6.z, 0));
         this.RightClickTargetY = (float)((int)IsoUtils.YToScreen(var27.x * var25, var27.z * var25, var6.z, 0));
         ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var27);
         byte var31 = 0;
         byte var33 = 0;
         var15 = IsoCamera.getOffscreenWidth(this.playerIndex);
         int var34 = IsoCamera.getOffscreenHeight(this.playerIndex);
         var17 = (float)var31 + (float)var15 / 2.0F;
         var18 = (float)var33 + (float)var34 / 2.0F;
         var19 = 150.0F * var23;
         this.RightClickTargetX = (float)((int)PZMath.clamp(var17 + this.RightClickTargetX, var19, (float)var15 - var19)) - var17;
         this.RightClickTargetY = (float)((int)PZMath.clamp(var18 + this.RightClickTargetY, var19, (float)var34 - var19)) - var18;
         if (Math.abs(var25) < 5.0F) {
            var20 = 1.0F - Math.abs(var25) / 5.0F;
            this.returnToCenter(1.0F / (16.0F * var5 / var20));
         } else {
            var5 /= 0.5F * var23;
            var20 = IsoUtils.XToScreenExact(var6.x, var6.y, var6.z, 0);
            float var21 = IsoUtils.YToScreenExact(var6.x, var6.y, var6.z, 0);
            if (var20 < var19 / 2.0F || var20 > (float)var15 - var19 / 2.0F || var21 < var19 / 2.0F || var21 > (float)var34 - var19 / 2.0F) {
               var5 /= 4.0F;
            }

            this.RightClickX_f = PZMath.step(this.RightClickX_f, this.RightClickTargetX, 1.875F * (float)PZMath.sign(this.RightClickTargetX - this.RightClickX_f) / var5);
            this.RightClickY_f = PZMath.step(this.RightClickY_f, this.RightClickTargetY, 1.875F * (float)PZMath.sign(this.RightClickTargetY - this.RightClickY_f) / var5);
            this.RightClickX = (float)((int)this.RightClickX_f);
            this.RightClickY = (float)((int)this.RightClickY_f);
         }
      } else if (var7 && var6 != null) {
         if ((var6.IsAiming() || var6.isLookingWhileInVehicle()) && JoypadManager.instance.isRBPressed(var6.JoypadBind) && !var6.bJoypadIgnoreAimUntilCentered) {
            this.RightClickTargetX = JoypadManager.instance.getAimingAxisX(var6.JoypadBind) * 1500.0F;
            this.RightClickTargetY = JoypadManager.instance.getAimingAxisY(var6.JoypadBind) * 1500.0F;
            var5 /= 0.5F * Core.getInstance().getZoom(this.playerIndex);
            this.RightClickX_f = PZMath.step(this.RightClickX_f, this.RightClickTargetX, (this.RightClickTargetX - this.RightClickX_f) / (80.0F * var5));
            this.RightClickY_f = PZMath.step(this.RightClickY_f, this.RightClickTargetY, (this.RightClickTargetY - this.RightClickY_f) / (80.0F * var5));
            this.RightClickX = (float)((int)this.RightClickX_f);
            this.RightClickY = (float)((int)this.RightClickY_f);
            var6.dirtyRecalcGridStackTime = 2.0F;
         } else {
            this.returnToCenter(1.0F / (16.0F * var5));
         }
      } else {
         int var12;
         if (this.playerIndex == 0 && var6 != null && !var6.isBlockMovement() && GameKeyboard.isKeyDown(Core.getInstance().getKey("PanCamera"))) {
            int var22 = IsoCamera.getScreenWidth(this.playerIndex);
            int var24 = IsoCamera.getScreenHeight(this.playerIndex);
            int var26 = IsoCamera.getScreenLeft(this.playerIndex);
            var12 = IsoCamera.getScreenTop(this.playerIndex);
            var29 = (float)Mouse.getXA() - ((float)var26 + (float)var22 / 2.0F);
            float var30 = (float)Mouse.getYA() - ((float)var12 + (float)var24 / 2.0F);
            float var32;
            if (var22 > var24) {
               var32 = (float)var24 / (float)var22;
               var29 *= var32;
            } else {
               var32 = (float)var22 / (float)var24;
               var30 *= var32;
            }

            var32 *= (float)var22 / 1366.0F;
            offVec.set(var29, var30);
            offVec.setLength(Math.min(offVec.getLength(), (float)Math.min(var22, var24) / 2.0F));
            var29 = offVec.x / var32;
            var30 = offVec.y / var32;
            this.RightClickTargetX = var29 * 2.0F;
            this.RightClickTargetY = var30 * 2.0F;
            var5 /= 0.5F * Core.getInstance().getZoom(this.playerIndex);
            this.RightClickX_f = PZMath.step(this.RightClickX_f, this.RightClickTargetX, (this.RightClickTargetX - this.RightClickX_f) / (80.0F * var5));
            this.RightClickY_f = PZMath.step(this.RightClickY_f, this.RightClickTargetY, (this.RightClickTargetY - this.RightClickY_f) / (80.0F * var5));
            this.RightClickX = (float)((int)this.RightClickX_f);
            this.RightClickY = (float)((int)this.RightClickY_f);
            var6.dirtyRecalcGridStackTime = 2.0F;
            IsoSprite.globalOffsetX = -1.0F;
         } else if (this.playerIndex == 0 && Core.getInstance().getOptionPanCameraWhileAiming()) {
            boolean var9 = !GameServer.bServer;
            boolean var10 = !UIManager.isMouseOverInventory() && var6 != null && var6.isAiming();
            boolean var11 = !var7 && var6 != null && !var6.isDead();
            if (var9 && var10 && var11) {
               var12 = IsoCamera.getScreenWidth(this.playerIndex);
               int var13 = IsoCamera.getScreenHeight(this.playerIndex);
               int var14 = IsoCamera.getScreenLeft(this.playerIndex);
               var15 = IsoCamera.getScreenTop(this.playerIndex);
               float var16 = (float)Mouse.getXA() - ((float)var14 + (float)var12 / 2.0F);
               var17 = (float)Mouse.getYA() - ((float)var15 + (float)var13 / 2.0F);
               if (var12 > var13) {
                  var18 = (float)var13 / (float)var12;
                  var16 *= var18;
               } else {
                  var18 = (float)var12 / (float)var13;
                  var17 *= var18;
               }

               var18 *= (float)var12 / 1366.0F;
               var19 = (float)Math.min(var12, var13) / 6.0F;
               var20 = (float)Math.min(var12, var13) / 2.0F - var19;
               offVec.set(var16, var17);
               if (offVec.getLength() < var20) {
                  var17 = 0.0F;
                  var16 = 0.0F;
               } else {
                  offVec.setLength(Math.min(offVec.getLength(), (float)Math.min(var12, var13) / 2.0F) - var20);
                  var16 = offVec.x / var18;
                  var17 = offVec.y / var18;
               }

               this.RightClickTargetX = var16 * 7.0F;
               this.RightClickTargetY = var17 * 7.0F;
               var5 /= 0.5F * Core.getInstance().getZoom(this.playerIndex);
               this.RightClickX_f = PZMath.step(this.RightClickX_f, this.RightClickTargetX, (this.RightClickTargetX - this.RightClickX_f) / (80.0F * var5));
               this.RightClickY_f = PZMath.step(this.RightClickY_f, this.RightClickTargetY, (this.RightClickTargetY - this.RightClickY_f) / (80.0F * var5));
               this.RightClickX = (float)((int)this.RightClickX_f);
               this.RightClickY = (float)((int)this.RightClickY_f);
               var6.dirtyRecalcGridStackTime = 2.0F;
            } else {
               this.returnToCenter(1.0F / (16.0F * var5));
            }

            IsoSprite.globalOffsetX = -1.0F;
         } else {
            this.returnToCenter(1.0F / (16.0F * var5));
         }
      }

      this.zoom = Core.getInstance().getZoom(this.playerIndex);
   }

   private void returnToCenter(float var1) {
      this.RightClickTargetX = 0.0F;
      this.RightClickTargetY = 0.0F;
      if (this.RightClickTargetX != this.RightClickX || this.RightClickTargetY != this.RightClickY) {
         this.RightClickX_f = PZMath.step(this.RightClickX_f, this.RightClickTargetX, (this.RightClickTargetX - this.RightClickX_f) * var1);
         this.RightClickY_f = PZMath.step(this.RightClickY_f, this.RightClickTargetY, (this.RightClickTargetY - this.RightClickY_f) * var1);
         this.RightClickX = (float)((int)this.RightClickX_f);
         this.RightClickY = (float)((int)this.RightClickY_f);
         if (Math.abs(this.RightClickTargetX - this.RightClickX_f) < 0.001F) {
            this.RightClickX = (float)((int)this.RightClickTargetX);
            this.RightClickX_f = this.RightClickX;
         }

         if (Math.abs(this.RightClickTargetY - this.RightClickY_f) < 0.001F) {
            this.RightClickY = (float)((int)this.RightClickTargetY);
            this.RightClickY_f = this.RightClickY;
         }

         IsoPlayer var2 = IsoPlayer.players[this.playerIndex];
         var2.dirtyRecalcGridStackTime = 2.0F;
      }

   }

   public float getOffX() {
      return (float)((int)(this.OffX + this.RightClickX));
   }

   public float getOffY() {
      return (float)((int)(this.OffY + this.RightClickY));
   }

   public float getTOffX() {
      float var1 = this.TOffX - this.OffX;
      return (float)((int)(this.OffX + this.RightClickX - var1));
   }

   public float getTOffY() {
      float var1 = this.TOffY - this.OffY;
      return (float)((int)(this.OffY + this.RightClickY - var1));
   }

   public float getLastOffX() {
      return (float)((int)(this.lastOffX + this.RightClickX));
   }

   public float getLastOffY() {
      return (float)((int)(this.lastOffY + this.RightClickY));
   }

   public float XToIso(float var1, float var2, float var3) {
      var1 = (float)((int)var1);
      var2 = (float)((int)var2);
      float var4 = var1 + this.getOffX();
      float var5 = var2 + this.getOffY();
      float var6 = (var4 + 2.0F * var5) / (64.0F * (float)Core.TileScale);
      var6 += 3.0F * var3;
      return var6;
   }

   public float YToIso(float var1, float var2, float var3) {
      var1 = (float)((int)var1);
      var2 = (float)((int)var2);
      float var4 = var1 + this.getOffX();
      float var5 = var2 + this.getOffY();
      float var6 = (var4 - 2.0F * var5) / (-64.0F * (float)Core.TileScale);
      var6 += 3.0F * var3;
      return var6;
   }

   public float YToScreenExact(float var1, float var2, float var3, int var4) {
      float var5 = IsoUtils.YToScreen(var1, var2, var3, var4);
      var5 -= this.getOffY();
      return var5;
   }

   public float XToScreenExact(float var1, float var2, float var3, int var4) {
      float var5 = IsoUtils.XToScreen(var1, var2, var3, var4);
      var5 -= this.getOffX();
      return var5;
   }

   public void copyFrom(PlayerCamera var1) {
      this.OffX = var1.OffX;
      this.OffY = var1.OffY;
      this.TOffX = var1.TOffX;
      this.TOffY = var1.TOffY;
      this.lastOffX = var1.lastOffX;
      this.lastOffY = var1.lastOffY;
      this.RightClickTargetX = var1.RightClickTargetX;
      this.RightClickTargetY = var1.RightClickTargetY;
      this.RightClickX = var1.RightClickX;
      this.RightClickY = var1.RightClickY;
      this.DeferedX = var1.DeferedX;
      this.DeferedY = var1.DeferedY;
      this.zoom = var1.zoom;
      this.OffscreenWidth = var1.OffscreenWidth;
      this.OffscreenHeight = var1.OffscreenHeight;
   }

   public void initFromIsoCamera(int var1) {
      this.copyFrom(IsoCamera.cameras[var1]);
      this.zoom = Core.getInstance().getZoom(var1);
      this.OffscreenWidth = IsoCamera.getOffscreenWidth(var1);
      this.OffscreenHeight = IsoCamera.getOffscreenHeight(var1);
   }
}
