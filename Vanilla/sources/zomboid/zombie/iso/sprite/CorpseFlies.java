package zombie.iso.sprite;

import zombie.GameTime;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.textures.Texture;
import zombie.iso.IsoUtils;
import zombie.network.GameServer;

public final class CorpseFlies {
   private static Texture TEXTURE;
   private static final int FRAME_WIDTH = 128;
   private static final int FRAME_HEIGHT = 128;
   private static final int COLUMNS = 8;
   private static final int ROWS = 7;
   private static final int NUM_FRAMES = 56;
   private static float COUNTER = 0.0F;
   private static int FRAME = 0;

   public static void render(int var0, int var1, int var2) {
      if (TEXTURE == null) {
         TEXTURE = Texture.getSharedTexture("media/textures/CorpseFlies.png");
      }

      if (TEXTURE != null && TEXTURE.isReady()) {
         int var3 = (FRAME + var0 + var1) % 56;
         int var4 = var3 % 8;
         int var5 = var3 / 8;
         float var6 = (float)(var4 * 128) / (float)TEXTURE.getWidth();
         float var7 = (float)(var5 * 128) / (float)TEXTURE.getHeight();
         float var8 = (float)((var4 + 1) * 128) / (float)TEXTURE.getWidth();
         float var9 = (float)((var5 + 1) * 128) / (float)TEXTURE.getHeight();
         float var10 = IsoUtils.XToScreen((float)var0 + 0.5F, (float)var1 + 0.5F, (float)var2, 0) + IsoSprite.globalOffsetX;
         float var11 = IsoUtils.YToScreen((float)var0 + 0.5F, (float)var1 + 0.5F, (float)var2, 0) + IsoSprite.globalOffsetY;
         byte var12 = 64;
         int var13 = var12 * Core.TileScale;
         var10 -= (float)(var13 / 2);
         var11 -= (float)(var13 + 16 * Core.TileScale);
         if (Core.bDebug) {
         }

         SpriteRenderer.instance.render(TEXTURE, var10, var11, (float)var13, (float)var13, 1.0F, 1.0F, 1.0F, 1.0F, var6, var7, var8, var7, var8, var9, var6, var9);
      }
   }

   public static void update() {
      if (!GameServer.bServer) {
         COUNTER += GameTime.getInstance().getRealworldSecondsSinceLastUpdate() * 1000.0F;
         float var0 = 20.0F;
         if (COUNTER > 1000.0F / var0) {
            COUNTER %= 1000.0F / var0;
            ++FRAME;
            FRAME %= 56;
         }

      }
   }

   public static void Reset() {
      TEXTURE = null;
   }
}
