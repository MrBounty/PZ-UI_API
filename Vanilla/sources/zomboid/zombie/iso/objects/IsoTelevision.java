package zombie.iso.objects;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import zombie.GameTime;
import zombie.characters.IsoPlayer;
import zombie.core.Rand;
import zombie.iso.IsoCell;
import zombie.iso.IsoDirections;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoLightSource;
import zombie.iso.IsoWorld;
import zombie.iso.LightingJNI;
import zombie.iso.sprite.IsoSprite;
import zombie.iso.sprite.IsoSpriteManager;
import zombie.radio.ZomboidRadio;

public class IsoTelevision extends IsoWaveSignal {
   protected ArrayList screenSprites = new ArrayList();
   protected boolean defaultToNoise = false;
   private IsoSprite cacheObjectSprite;
   protected IsoDirections facing;
   private boolean hasSetupScreens;
   private boolean tickIsLightUpdate;
   private IsoTelevision.Screens currentScreen;
   private int spriteIndex;

   public String getObjectName() {
      return "Television";
   }

   public IsoTelevision(IsoCell var1) {
      super(var1);
      this.facing = IsoDirections.Max;
      this.hasSetupScreens = false;
      this.tickIsLightUpdate = false;
      this.currentScreen = IsoTelevision.Screens.OFFSCREEN;
      this.spriteIndex = 0;
   }

   public IsoTelevision(IsoCell var1, IsoGridSquare var2, IsoSprite var3) {
      super(var1, var2, var3);
      this.facing = IsoDirections.Max;
      this.hasSetupScreens = false;
      this.tickIsLightUpdate = false;
      this.currentScreen = IsoTelevision.Screens.OFFSCREEN;
      this.spriteIndex = 0;
   }

   protected void init(boolean var1) {
      super.init(var1);
   }

   private void setupDefaultScreens() {
      this.hasSetupScreens = true;
      this.cacheObjectSprite = this.sprite;
      if (this.screenSprites.size() == 0) {
         for(int var1 = 16; var1 <= 64; var1 += 16) {
            IsoSprite var2 = IsoSprite.getSprite(IsoSpriteManager.instance, this.sprite.getName(), var1);
            if (var2 != null) {
               this.addTvScreenSprite(var2);
            }
         }
      }

      this.facing = IsoDirections.Max;
      if (this.sprite != null && this.sprite.getProperties().Is("Facing")) {
         String var4 = this.sprite.getProperties().Val("Facing");
         byte var3 = -1;
         switch(var4.hashCode()) {
         case 69:
            if (var4.equals("E")) {
               var3 = 3;
            }
            break;
         case 78:
            if (var4.equals("N")) {
               var3 = 0;
            }
            break;
         case 83:
            if (var4.equals("S")) {
               var3 = 1;
            }
            break;
         case 87:
            if (var4.equals("W")) {
               var3 = 2;
            }
         }

         switch(var3) {
         case 0:
            this.facing = IsoDirections.N;
            break;
         case 1:
            this.facing = IsoDirections.S;
            break;
         case 2:
            this.facing = IsoDirections.W;
            break;
         case 3:
            this.facing = IsoDirections.E;
         }
      }

   }

   public void update() {
      super.update();
      if (this.cacheObjectSprite != null && this.cacheObjectSprite != this.sprite) {
         this.hasSetupScreens = false;
         this.screenSprites.clear();
         this.currentScreen = IsoTelevision.Screens.OFFSCREEN;
         this.nextLightUpdate = 0.0F;
      }

      if (!this.hasSetupScreens) {
         this.setupDefaultScreens();
      }

      this.updateTvScreen();
   }

   protected void updateLightSource() {
      this.tickIsLightUpdate = false;
      if (this.lightSource == null) {
         this.lightSource = new IsoLightSource(this.square.getX(), this.square.getY(), this.square.getZ(), 0.0F, 0.0F, 1.0F, this.lightSourceRadius);
         this.lightWasRemoved = true;
      }

      if (this.lightWasRemoved) {
         IsoWorld.instance.CurrentCell.addLamppost(this.lightSource);
         IsoGridSquare.RecalcLightTime = -1;
         GameTime.instance.lightSourceUpdate = 100.0F;
         this.lightWasRemoved = false;
      }

      this.lightUpdateCnt += GameTime.getInstance().getMultiplier();
      if (this.lightUpdateCnt >= this.nextLightUpdate) {
         float var1 = 300.0F;
         float var2 = 0.0F;
         if (!this.hasChatToDisplay()) {
            var2 = 0.6F;
            var1 = (float)Rand.Next(200, 400);
         } else {
            var1 = (float)Rand.Next(15, 300);
         }

         float var3 = Rand.Next(var2, 1.0F);
         this.tickIsLightUpdate = true;
         float var4 = 0.58F + 0.25F * var3;
         float var5 = Rand.Next(0.65F, 0.85F);
         int var6 = 1 + (int)((float)(this.lightSourceRadius - 1) * var3);
         IsoGridSquare.RecalcLightTime = -1;
         GameTime.instance.lightSourceUpdate = 100.0F;
         this.lightSource.setRadius(var6);
         this.lightSource.setR(var4);
         this.lightSource.setG(var5);
         this.lightSource.setB(var5);
         if (LightingJNI.init && this.lightSource.ID != 0) {
            LightingJNI.setLightColor(this.lightSource.ID, this.lightSource.getR(), this.lightSource.getG(), this.lightSource.getB());
         }

         this.lightUpdateCnt = 0.0F;
         this.nextLightUpdate = var1;
      }

   }

   private void setScreen(IsoTelevision.Screens var1) {
      if (var1 == IsoTelevision.Screens.OFFSCREEN) {
         this.currentScreen = IsoTelevision.Screens.OFFSCREEN;
         if (this.overlaySprite != null) {
            this.overlaySprite = null;
         }

      } else {
         if (this.currentScreen != var1 || var1 == IsoTelevision.Screens.ALTERNATESCREEN) {
            this.currentScreen = var1;
            IsoSprite var2 = null;
            switch(var1) {
            case TESTSCREEN:
               if (this.screenSprites.size() > 0) {
                  var2 = (IsoSprite)this.screenSprites.get(0);
               }
               break;
            case DEFAULTSCREEN:
               if (this.screenSprites.size() > 1) {
                  var2 = (IsoSprite)this.screenSprites.get(1);
               }
               break;
            case ALTERNATESCREEN:
               if (this.screenSprites.size() == 3) {
                  var2 = (IsoSprite)this.screenSprites.get(2);
               } else if (this.screenSprites.size() > 3) {
                  ++this.spriteIndex;
                  if (this.spriteIndex < 2) {
                     this.spriteIndex = 2;
                  }

                  if (this.spriteIndex > this.screenSprites.size() - 1) {
                     this.spriteIndex = 2;
                  }

                  var2 = (IsoSprite)this.screenSprites.get(this.spriteIndex);
               }
            }

            this.overlaySprite = var2;
         }

      }
   }

   protected void updateTvScreen() {
      if (this.deviceData != null && this.deviceData.getIsTurnedOn() && this.screenSprites.size() > 0) {
         if (!this.deviceData.isReceivingSignal() && !this.deviceData.isPlayingMedia()) {
            if (ZomboidRadio.POST_RADIO_SILENCE) {
               this.setScreen(IsoTelevision.Screens.TESTSCREEN);
            } else {
               this.setScreen(IsoTelevision.Screens.DEFAULTSCREEN);
            }
         } else if (this.tickIsLightUpdate || this.currentScreen != IsoTelevision.Screens.ALTERNATESCREEN) {
            this.setScreen(IsoTelevision.Screens.ALTERNATESCREEN);
         }
      } else if (this.currentScreen != IsoTelevision.Screens.OFFSCREEN) {
         this.setScreen(IsoTelevision.Screens.OFFSCREEN);
      }

   }

   public void addTvScreenSprite(IsoSprite var1) {
      this.screenSprites.add(var1);
   }

   public void clearTvScreenSprites() {
      this.screenSprites.clear();
   }

   public void removeTvScreenSprite(IsoSprite var1) {
      this.screenSprites.remove(var1);
   }

   public void renderlast() {
      super.renderlast();
   }

   public void load(ByteBuffer var1, int var2, boolean var3) throws IOException {
      super.load(var1, var2, var3);
      this.overlaySprite = null;
   }

   public void save(ByteBuffer var1, boolean var2) throws IOException {
      super.save(var1, var2);
   }

   public boolean isFacing(IsoPlayer var1) {
      if (var1 != null && var1.isLocalPlayer()) {
         if (this.getObjectIndex() == -1) {
            return false;
         } else if (!this.square.isCanSee(var1.PlayerIndex)) {
            return false;
         } else if (this.facing == IsoDirections.Max) {
            return false;
         } else {
            switch(this.facing) {
            case N:
               if (var1.y >= (float)this.square.y) {
                  return false;
               }

               return var1.dir == IsoDirections.SW || var1.dir == IsoDirections.S || var1.dir == IsoDirections.SE;
            case S:
               if (var1.y < (float)(this.square.y + 1)) {
                  return false;
               }

               return var1.dir == IsoDirections.NW || var1.dir == IsoDirections.N || var1.dir == IsoDirections.NE;
            case W:
               if (var1.x >= (float)this.square.x) {
                  return false;
               }

               return var1.dir == IsoDirections.SE || var1.dir == IsoDirections.E || var1.dir == IsoDirections.NE;
            case E:
               if (var1.x < (float)(this.square.x + 1)) {
                  return false;
               }

               return var1.dir == IsoDirections.SW || var1.dir == IsoDirections.W || var1.dir == IsoDirections.NW;
            default:
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private static enum Screens {
      OFFSCREEN,
      TESTSCREEN,
      DEFAULTSCREEN,
      ALTERNATESCREEN;

      // $FF: synthetic method
      private static IsoTelevision.Screens[] $values() {
         return new IsoTelevision.Screens[]{OFFSCREEN, TESTSCREEN, DEFAULTSCREEN, ALTERNATESCREEN};
      }
   }
}
