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

public class IsoRainSplash extends IsoObject {
   public int Age;

   public boolean Serialize() {
      return false;
   }

   public IsoRainSplash(IsoCell var1, IsoGridSquare var2) {
      if (var2 != null) {
         if (!var2.getProperties().Is(IsoFlagType.HasRainSplashes)) {
            this.Age = 0;
            this.square = var2;
            this.offsetX = 0.0F;
            this.offsetY = 0.0F;
            int var3 = 1 + Rand.Next(2);
            byte var4 = 16;
            byte var5 = 8;

            for(int var6 = 0; var6 < var3; ++var6) {
               float var7 = Rand.Next(0.1F, 0.9F);
               float var8 = Rand.Next(0.1F, 0.9F);
               short var9 = (short)((int)(IsoUtils.XToScreen(var7, var8, 0.0F, 0) - (float)(var4 / 2)));
               short var10 = (short)((int)(IsoUtils.YToScreen(var7, var8, 0.0F, 0) - (float)(var5 / 2)));
               this.AttachAnim("RainSplash", "00", 4, RainManager.RainSplashAnimDelay, -var9, -var10, true, 0, false, 0.7F, RainManager.RainSplashTintMod);
               ((IsoSpriteInstance)this.AttachedAnimSprite.get(var6)).Frame = (float)((short)Rand.Next(4));
               ((IsoSpriteInstance)this.AttachedAnimSprite.get(var6)).setScale((float)Core.TileScale, (float)Core.TileScale);
            }

            var2.getProperties().Set(IsoFlagType.HasRainSplashes);
            RainManager.AddRainSplash(this);
         }
      }
   }

   public String getObjectName() {
      return "RainSplashes";
   }

   public boolean HasTooltip() {
      return false;
   }

   public boolean TestCollide(IsoMovingObject var1, IsoGridSquare var2) {
      return this.square == var2;
   }

   public IsoObject.VisionResult TestVision(IsoGridSquare var1, IsoGridSquare var2) {
      return IsoObject.VisionResult.NoEffect;
   }

   public void ChangeTintMod(ColorInfo var1) {
      if (this.AttachedAnimSprite != null) {
         for(int var2 = 0; var2 < this.AttachedAnimSprite.size(); ++var2) {
         }
      }

   }

   public void update() {
      this.sx = this.sy = 0.0F;
      ++this.Age;

      int var1;
      for(var1 = 0; var1 < this.AttachedAnimSprite.size(); ++var1) {
         IsoSpriteInstance var2 = (IsoSpriteInstance)this.AttachedAnimSprite.get(var1);
         IsoSprite var3 = var2.parentSprite;
         var2.update();
         var2.Frame += var2.AnimFrameIncrease * GameTime.instance.getMultipliedSecondsSinceLastUpdate() * 60.0F;
         if ((int)var2.Frame >= var3.CurrentAnim.Frames.size() && var3.Loop && var2.Looped) {
            var2.Frame = 0.0F;
         }
      }

      for(var1 = 0; var1 < IsoPlayer.numPlayers; ++var1) {
         if (Core.getInstance().RenderShader != null && Core.getInstance().getOffscreenBuffer() != null) {
            this.setAlphaAndTarget(var1, 0.25F);
         } else {
            this.setAlphaAndTarget(var1, 0.6F);
         }
      }

   }

   void Reset(IsoGridSquare var1) {
      if (var1 != null) {
         if (!var1.getProperties().Is(IsoFlagType.HasRainSplashes)) {
            this.Age = 0;
            this.square = var1;
            int var2 = 1 + Rand.Next(2);
            if (this.AttachedAnimSprite != null) {
               for(int var3 = 0; var3 < this.AttachedAnimSprite.size(); ++var3) {
               }
            }

            var1.getProperties().Set(IsoFlagType.HasRainSplashes);
            RainManager.AddRainSplash(this);
         }
      }
   }
}
