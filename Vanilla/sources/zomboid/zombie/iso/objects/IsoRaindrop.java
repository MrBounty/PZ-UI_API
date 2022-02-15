package zombie.iso.objects;

import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.textures.ColorInfo;
import zombie.iso.IsoCell;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoObject;
import zombie.iso.IsoUtils;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteInstance;

public class IsoRaindrop extends IsoObject {
   public int AnimSpriteIndex;
   public float GravMod;
   public int Life;
   public float SplashY;
   public float OffsetY;
   public float Vel_Y;

   public boolean Serialize() {
      return false;
   }

   public IsoRaindrop(IsoCell var1, IsoGridSquare var2, boolean var3) {
      if (var3) {
         if (var2 != null) {
            if (!var2.getProperties().Is(IsoFlagType.HasRaindrop)) {
               this.Life = 0;
               this.square = var2;
               int var4 = 1 * Core.TileScale;
               int var5 = 64 * Core.TileScale;
               float var6 = Rand.Next(0.1F, 0.9F);
               float var7 = Rand.Next(0.1F, 0.9F);
               short var8 = (short)((int)(IsoUtils.XToScreen(var6, var7, 0.0F, 0) - (float)(var4 / 2)));
               short var9 = (short)((int)(IsoUtils.YToScreen(var6, var7, 0.0F, 0) - (float)var5));
               this.offsetX = 0.0F;
               this.offsetY = 0.0F;
               this.OffsetY = RainManager.RaindropStartDistance;
               this.SplashY = (float)var9;
               this.AttachAnim("Rain", "00", 1, 0.0F, -var8, -var9, true, 0, false, 0.7F, RainManager.RaindropTintMod);
               if (this.AttachedAnimSprite != null) {
                  this.AnimSpriteIndex = this.AttachedAnimSprite.size() - 1;
               } else {
                  this.AnimSpriteIndex = 0;
               }

               ((IsoSpriteInstance)this.AttachedAnimSprite.get(this.AnimSpriteIndex)).setScale((float)Core.TileScale, (float)Core.TileScale);
               var2.getProperties().Set(IsoFlagType.HasRaindrop);
               this.Vel_Y = 0.0F;
               float var10 = 1000000.0F / (float)Rand.Next(1000000) + 1.0E-5F;
               this.GravMod = -(RainManager.GravModMin + (RainManager.GravModMax - RainManager.GravModMin) * var10);
               RainManager.AddRaindrop(this);
            }
         }
      }
   }

   public boolean HasTooltip() {
      return false;
   }

   public String getObjectName() {
      return "RainDrops";
   }

   public boolean TestCollide(IsoMovingObject var1, IsoGridSquare var2) {
      return this.square == var2;
   }

   public IsoObject.VisionResult TestVision(IsoGridSquare var1, IsoGridSquare var2) {
      return IsoObject.VisionResult.NoEffect;
   }

   public void ChangeTintMod(ColorInfo var1) {
   }

   public void update() {
      this.sx = this.sy = 0.0F;
      ++this.Life;

      int var1;
      for(var1 = 0; var1 < this.AttachedAnimSprite.size(); ++var1) {
         IsoSpriteInstance var2 = (IsoSpriteInstance)this.AttachedAnimSprite.get(var1);
         var2.update();
         var2.Frame += var2.AnimFrameIncrease * GameTime.instance.getMultipliedSecondsSinceLastUpdate() * 60.0F;
         IsoSprite var3 = var2.parentSprite;
         if ((int)var2.Frame >= var3.CurrentAnim.Frames.size() && var3.Loop && var2.Looped) {
            var2.Frame = 0.0F;
         }
      }

      this.Vel_Y += this.GravMod * GameTime.instance.getMultipliedSecondsSinceLastUpdate() * 60.0F;
      this.OffsetY += this.Vel_Y;
      if (this.AttachedAnimSprite != null && this.AttachedAnimSprite.size() > this.AnimSpriteIndex && this.AnimSpriteIndex >= 0) {
         float var10001 = this.SplashY + (float)((int)this.OffsetY);
         ((IsoSpriteInstance)this.AttachedAnimSprite.get(this.AnimSpriteIndex)).parentSprite.soffY = (short)((int)var10001);
      }

      if (this.OffsetY < 0.0F) {
         this.OffsetY = RainManager.RaindropStartDistance;
         this.Vel_Y = 0.0F;
         float var4 = 1000000.0F / (float)Rand.Next(1000000) + 1.0E-5F;
         this.GravMod = -(RainManager.GravModMin + (RainManager.GravModMax - RainManager.GravModMin) * var4);
      }

      for(var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
         if (Core.getInstance().RenderShader != null && Core.getInstance().getOffscreenBuffer() != null) {
            this.setAlphaAndTarget(var1, 0.55F);
         } else {
            this.setAlphaAndTarget(var1, 1.0F);
         }
      }

   }

   void Reset(IsoGridSquare var1, boolean var2) {
      if (var2) {
         if (var1 != null) {
            if (!var1.getProperties().Is(IsoFlagType.HasRaindrop)) {
               this.Life = 0;
               this.square = var1;
               this.OffsetY = RainManager.RaindropStartDistance;
               if (this.AttachedAnimSprite != null) {
                  this.AnimSpriteIndex = this.AttachedAnimSprite.size() - 1;
               } else {
                  this.AnimSpriteIndex = 0;
               }

               var1.getProperties().Set(IsoFlagType.HasRaindrop);
               this.Vel_Y = 0.0F;
               float var3 = 1000000.0F / (float)Rand.Next(1000000) + 1.0E-5F;
               this.GravMod = -(RainManager.GravModMin + (RainManager.GravModMax - RainManager.GravModMin) * var3);
               RainManager.AddRaindrop(this);
            }
         }
      }
   }
}
