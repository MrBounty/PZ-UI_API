package zombie.iso.objects;

import zombie.WorldSoundManager;
import zombie.characters.IsoGameCharacter;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.Rand;
import zombie.core.opengl.Shader;
import zombie.core.textures.ColorInfo;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.inventory.types.HandWeapon;
import zombie.iso.IsoCell;
import zombie.iso.IsoPhysicsObject;
import zombie.network.GameClient;

public class IsoBall extends IsoPhysicsObject {
   private HandWeapon weapon = null;
   private IsoGameCharacter character = null;
   private int lastCheckX = 0;
   private int lastCheckY = 0;

   public String getObjectName() {
      return "MolotovCocktail";
   }

   public IsoBall(IsoCell var1) {
      super(var1);
   }

   public IsoBall(IsoCell var1, float var2, float var3, float var4, float var5, float var6, HandWeapon var7, IsoGameCharacter var8) {
      super(var1);
      this.weapon = var7;
      this.character = var8;
      this.velX = var5;
      this.velY = var6;
      float var9 = (float)Rand.Next(4000) / 10000.0F;
      float var10 = (float)Rand.Next(4000) / 10000.0F;
      var9 -= 0.2F;
      var10 -= 0.2F;
      this.velX += var9;
      this.velY += var10;
      this.x = var2;
      this.y = var3;
      this.z = var4;
      this.nx = var2;
      this.ny = var3;
      this.offsetX = 0.0F;
      this.offsetY = 0.0F;
      this.terminalVelocity = -0.02F;
      Texture var11 = this.sprite.LoadFrameExplicit(var7.getTex().getName());
      if (var11 != null) {
         this.sprite.Animate = false;
         int var12 = Core.TileScale;
         this.sprite.def.scaleAspect((float)var11.getWidthOrig(), (float)var11.getHeightOrig(), (float)(16 * var12), (float)(16 * var12));
      }

      this.speedMod = 0.6F;
   }

   public void collideGround() {
      this.Fall();
   }

   public void collideWall() {
      this.Fall();
   }

   public void update() {
      super.update();
   }

   public void render(float var1, float var2, float var3, ColorInfo var4, boolean var5, boolean var6, Shader var7) {
      super.render(var1, var2, var3, var4, var5, var6, var7);
      if (Core.bDebug) {
      }

   }

   void Fall() {
      this.getCurrentSquare().getMovingObjects().remove(this);
      this.getCell().Remove(this);
      if (!GameClient.bClient) {
         WorldSoundManager.instance.addSound(this, (int)this.x, (int)this.y, 0, 600, 600);
      }

      if (this.character instanceof IsoPlayer) {
         if (((IsoPlayer)this.character).isLocalPlayer()) {
            this.square.AddWorldInventoryItem(this.weapon, Rand.Next(0.2F, 0.8F), Rand.Next(0.2F, 0.8F), 0.0F, true);
         }
      } else {
         DebugLog.General.error("IsoBall: character isn't instance of IsoPlayer");
      }

   }
}
