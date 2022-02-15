package zombie.iso;

import org.joml.Vector2f;
import zombie.iso.SpriteDetails.IsoFlagType;
import zombie.popman.ObjectPool;
import zombie.util.list.PZArrayList;

public final class IsoWaterGeometry {
   private static final Vector2f tempVector2f = new Vector2f();
   boolean hasWater = false;
   boolean bShore = false;
   final float[] x = new float[4];
   final float[] y = new float[4];
   final float[] depth = new float[4];
   final float[] flow = new float[4];
   final float[] speed = new float[4];
   float IsExternal = 0.0F;
   IsoGridSquare square = null;
   int m_adjacentChunkLoadedCounter;
   public static final ObjectPool pool = new ObjectPool(IsoWaterGeometry::new);

   public IsoWaterGeometry init(IsoGridSquare var1) throws Exception {
      this.x[0] = IsoUtils.XToScreen((float)var1.x, (float)var1.y, 0.0F, 0);
      this.y[0] = IsoUtils.YToScreen((float)var1.x, (float)var1.y, 0.0F, 0);
      this.x[1] = IsoUtils.XToScreen((float)var1.x, (float)(var1.y + 1), 0.0F, 0);
      this.y[1] = IsoUtils.YToScreen((float)var1.x, (float)(var1.y + 1), 0.0F, 0);
      this.x[2] = IsoUtils.XToScreen((float)(var1.x + 1), (float)(var1.y + 1), 0.0F, 0);
      this.y[2] = IsoUtils.YToScreen((float)(var1.x + 1), (float)(var1.y + 1), 0.0F, 0);
      this.x[3] = IsoUtils.XToScreen((float)(var1.x + 1), (float)var1.y, 0.0F, 0);
      this.y[3] = IsoUtils.YToScreen((float)(var1.x + 1), (float)var1.y, 0.0F, 0);
      this.hasWater = false;
      this.bShore = false;
      this.square = var1;
      this.IsExternal = var1.getProperties().Is(IsoFlagType.exterior) ? 1.0F : 0.0F;
      int var2 = IsoWaterFlow.getShore(var1.x, var1.y);
      IsoObject var3 = var1.getFloor();
      String var4 = var3 == null ? null : var3.getSprite().getName();
      int var5;
      if (var1.getProperties().Is(IsoFlagType.water)) {
         this.hasWater = true;

         for(var5 = 0; var5 < 4; ++var5) {
            this.depth[var5] = 1.0F;
         }
      } else if (var2 == 1 && var4 != null && var4.startsWith("blends_natural")) {
         var5 = 0;

         while(true) {
            if (var5 >= 4) {
               IsoGridSquare var13 = var1.getAdjacentSquare(IsoDirections.W);
               IsoGridSquare var6 = var1.getAdjacentSquare(IsoDirections.NW);
               IsoGridSquare var7 = var1.getAdjacentSquare(IsoDirections.N);
               IsoGridSquare var8 = var1.getAdjacentSquare(IsoDirections.SW);
               IsoGridSquare var9 = var1.getAdjacentSquare(IsoDirections.S);
               IsoGridSquare var10 = var1.getAdjacentSquare(IsoDirections.SE);
               IsoGridSquare var11 = var1.getAdjacentSquare(IsoDirections.E);
               IsoGridSquare var12 = var1.getAdjacentSquare(IsoDirections.NE);
               if (var7 == null || var6 == null || var13 == null || var8 == null || var9 == null || var10 == null || var11 == null || var12 == null) {
                  return null;
               }

               if (var13.getProperties().Is(IsoFlagType.water) || var6.getProperties().Is(IsoFlagType.water) || var7.getProperties().Is(IsoFlagType.water)) {
                  this.bShore = true;
                  this.depth[0] = 1.0F;
               }

               if (var13.getProperties().Is(IsoFlagType.water) || var8.getProperties().Is(IsoFlagType.water) || var9.getProperties().Is(IsoFlagType.water)) {
                  this.bShore = true;
                  this.depth[1] = 1.0F;
               }

               if (var9.getProperties().Is(IsoFlagType.water) || var10.getProperties().Is(IsoFlagType.water) || var11.getProperties().Is(IsoFlagType.water)) {
                  this.bShore = true;
                  this.depth[2] = 1.0F;
               }

               if (var11.getProperties().Is(IsoFlagType.water) || var12.getProperties().Is(IsoFlagType.water) || var7.getProperties().Is(IsoFlagType.water)) {
                  this.bShore = true;
                  this.depth[3] = 1.0F;
               }
               break;
            }

            this.depth[var5] = 0.0F;
            ++var5;
         }
      }

      Vector2f var14 = IsoWaterFlow.getFlow(var1, 0, 0, tempVector2f);
      this.flow[0] = var14.x;
      this.speed[0] = var14.y;
      var14 = IsoWaterFlow.getFlow(var1, 0, 1, var14);
      this.flow[1] = var14.x;
      this.speed[1] = var14.y;
      var14 = IsoWaterFlow.getFlow(var1, 1, 1, var14);
      this.flow[2] = var14.x;
      this.speed[2] = var14.y;
      var14 = IsoWaterFlow.getFlow(var1, 1, 0, var14);
      this.flow[3] = var14.x;
      this.speed[3] = var14.y;
      this.hideWaterObjects(var1);
      return this;
   }

   private void hideWaterObjects(IsoGridSquare var1) {
      PZArrayList var2 = var1.getObjects();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         IsoObject var4 = (IsoObject)var2.get(var3);
         if (var4.sprite != null && var4.sprite.name != null) {
            String var5 = var4.sprite.name;
            if (var5.startsWith("blends_natural_02") && (var5.endsWith("_0") || var5.endsWith("_1") || var5.endsWith("_2") || var5.endsWith("_3") || var5.endsWith("_4") || var5.endsWith("_5") || var5.endsWith("_6") || var5.endsWith("_7") || var5.endsWith("_8") || var5.endsWith("_9") || var5.endsWith("_10") || var5.endsWith("_11") || var5.endsWith("_12"))) {
               var4.sprite.setHideForWaterRender();
            }
         }
      }

   }

   public boolean isShore() {
      return IsoWaterFlow.getShore(this.square.x, this.square.y) == 0;
   }

   public float getFlow() {
      IsoWaterFlow.getShore(this.square.x, this.square.y);
      Vector2f var1 = IsoWaterFlow.getFlow(this.square, 0, 0, tempVector2f);
      System.out.println("FLOW!  " + var1.x + " " + var1.y);
      return var1.x;
   }
}
