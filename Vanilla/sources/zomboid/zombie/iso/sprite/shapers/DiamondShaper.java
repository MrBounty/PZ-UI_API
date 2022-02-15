package zombie.iso.sprite.shapers;

import java.util.function.Consumer;
import zombie.core.textures.TextureDraw;
import zombie.debug.DebugOptions;

public class DiamondShaper implements Consumer {
   public static final DiamondShaper instance = new DiamondShaper();

   public void accept(TextureDraw var1) {
      if (DebugOptions.instance.Terrain.RenderTiles.IsoGridSquare.MeshCutdown.getValue()) {
         float var2 = var1.x0;
         float var3 = var1.y0;
         float var4 = var1.x1;
         float var5 = var1.y1;
         float var6 = var1.y2;
         float var7 = var1.y3;
         float var8 = var4 - var2;
         float var9 = var6 - var5;
         float var10 = var2 + var8 * 0.5F;
         float var11 = var5 + var9 * 0.5F;
         float var12 = var1.u0;
         float var13 = var1.v0;
         float var14 = var1.u1;
         float var15 = var1.v1;
         float var16 = var1.v2;
         float var17 = var1.v3;
         float var18 = var14 - var12;
         float var19 = var16 - var13;
         float var20 = var12 + var18 * 0.5F;
         float var21 = var15 + var19 * 0.5F;
         var1.x0 = var10;
         var1.y0 = var3;
         var1.u0 = var20;
         var1.v0 = var13;
         var1.x1 = var4;
         var1.y1 = var11;
         var1.u1 = var14;
         var1.v1 = var21;
         var1.x2 = var10;
         var1.y2 = var7;
         var1.u2 = var20;
         var1.v2 = var17;
         var1.x3 = var2;
         var1.y3 = var11;
         var1.u3 = var12;
         var1.v3 = var21;
      }
   }
}
