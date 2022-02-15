package zombie.iso.weather.fog;

import org.joml.Vector2i;
import zombie.GameTime;
import zombie.IndieGL;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.PerformanceSettings;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.debug.DebugLog;
import zombie.input.GameKeyboard;
import zombie.iso.IsoCamera;
import zombie.iso.IsoChunkMap;
import zombie.iso.IsoGridSquare;
import zombie.iso.IsoUtils;
import zombie.iso.IsoWorld;
import zombie.iso.PlayerCamera;
import zombie.iso.weather.ClimateManager;
import zombie.iso.weather.fx.SteppedUpdateFloat;

public class ImprovedFog {
   private static final ImprovedFog.RectangleIterator rectangleIter = new ImprovedFog.RectangleIterator();
   private static final Vector2i rectangleMatrixPos = new Vector2i();
   private static IsoChunkMap chunkMap;
   private static int minY;
   private static int maxY;
   private static int minX;
   private static int maxX;
   private static int zLayer;
   private static Vector2i lastIterPos = new Vector2i();
   private static ImprovedFog.FogRectangle fogRectangle = new ImprovedFog.FogRectangle();
   private static boolean drawingThisLayer = false;
   private static float ZOOM = 1.0F;
   private static int PlayerIndex;
   private static int playerRow;
   private static float screenWidth;
   private static float screenHeight;
   private static float worldOffsetX;
   private static float worldOffsetY;
   private static float topAlphaHeight = 0.38F;
   private static float bottomAlphaHeight = 0.24F;
   private static float secondLayerAlpha = 0.5F;
   private static float scalingX = 1.0F;
   private static float scalingY = 1.0F;
   private static float colorR = 1.0F;
   private static float colorG = 1.0F;
   private static float colorB = 1.0F;
   private static boolean drawDebugColors = false;
   private static float octaves = 6.0F;
   private static boolean highQuality = true;
   private static boolean enableEditing = false;
   private static float alphaCircleAlpha = 0.3F;
   private static float alphaCircleRad = 2.25F;
   private static int lastRow = -1;
   private static ClimateManager climateManager;
   private static Texture noiseTexture;
   private static boolean renderOnlyOneRow = false;
   private static float baseAlpha = 0.0F;
   private static int renderEveryXRow = 1;
   private static int renderXRowsFromCenter = 0;
   private static boolean renderCurrentLayerOnly = false;
   private static float rightClickOffX = 0.0F;
   private static float rightClickOffY = 0.0F;
   private static float cameraOffscreenLeft = 0.0F;
   private static float cameraOffscreenTop = 0.0F;
   private static float cameraZoom = 0.0F;
   private static int minXOffset = -2;
   private static int maxXOffset = 12;
   private static int maxYOffset = -5;
   private static boolean renderEndOnly = false;
   private static final SteppedUpdateFloat fogIntensity = new SteppedUpdateFloat(0.0F, 0.005F, 0.0F, 1.0F);
   private static int keyPause = 0;
   private static final float[] offsets = new float[]{0.3F, 0.8F, 0.0F, 0.6F, 0.3F, 0.1F, 0.5F, 0.9F, 0.2F, 0.0F, 0.7F, 0.1F, 0.4F, 0.2F, 0.5F, 0.3F, 0.8F, 0.4F, 0.9F, 0.5F, 0.8F, 0.4F, 0.7F, 0.2F, 0.0F, 0.6F, 0.1F, 0.6F, 0.9F, 0.7F};

   public static int getMinXOffset() {
      return minXOffset;
   }

   public static void setMinXOffset(int var0) {
      minXOffset = var0;
   }

   public static int getMaxXOffset() {
      return maxXOffset;
   }

   public static void setMaxXOffset(int var0) {
      maxXOffset = var0;
   }

   public static int getMaxYOffset() {
      return maxYOffset;
   }

   public static void setMaxYOffset(int var0) {
      maxYOffset = var0;
   }

   public static boolean isRenderEndOnly() {
      return renderEndOnly;
   }

   public static void setRenderEndOnly(boolean var0) {
      renderEndOnly = var0;
   }

   public static float getAlphaCircleAlpha() {
      return alphaCircleAlpha;
   }

   public static void setAlphaCircleAlpha(float var0) {
      alphaCircleAlpha = var0;
   }

   public static float getAlphaCircleRad() {
      return alphaCircleRad;
   }

   public static void setAlphaCircleRad(float var0) {
      alphaCircleRad = var0;
   }

   public static boolean isHighQuality() {
      return highQuality;
   }

   public static void setHighQuality(boolean var0) {
      highQuality = var0;
   }

   public static boolean isEnableEditing() {
      return enableEditing;
   }

   public static void setEnableEditing(boolean var0) {
      enableEditing = var0;
   }

   public static float getTopAlphaHeight() {
      return topAlphaHeight;
   }

   public static void setTopAlphaHeight(float var0) {
      topAlphaHeight = var0;
   }

   public static float getBottomAlphaHeight() {
      return bottomAlphaHeight;
   }

   public static void setBottomAlphaHeight(float var0) {
      bottomAlphaHeight = var0;
   }

   public static boolean isDrawDebugColors() {
      return drawDebugColors;
   }

   public static void setDrawDebugColors(boolean var0) {
      drawDebugColors = var0;
   }

   public static float getOctaves() {
      return octaves;
   }

   public static void setOctaves(float var0) {
      octaves = var0;
   }

   public static float getColorR() {
      return colorR;
   }

   public static void setColorR(float var0) {
      colorR = var0;
   }

   public static float getColorG() {
      return colorG;
   }

   public static void setColorG(float var0) {
      colorG = var0;
   }

   public static float getColorB() {
      return colorB;
   }

   public static void setColorB(float var0) {
      colorB = var0;
   }

   public static float getSecondLayerAlpha() {
      return secondLayerAlpha;
   }

   public static void setSecondLayerAlpha(float var0) {
      secondLayerAlpha = var0;
   }

   public static float getScalingX() {
      return scalingX;
   }

   public static void setScalingX(float var0) {
      scalingX = var0;
   }

   public static float getScalingY() {
      return scalingY;
   }

   public static void setScalingY(float var0) {
      scalingY = var0;
   }

   public static boolean isRenderOnlyOneRow() {
      return renderOnlyOneRow;
   }

   public static void setRenderOnlyOneRow(boolean var0) {
      renderOnlyOneRow = var0;
   }

   public static float getBaseAlpha() {
      return baseAlpha;
   }

   public static void setBaseAlpha(float var0) {
      baseAlpha = var0;
   }

   public static int getRenderEveryXRow() {
      return renderEveryXRow;
   }

   public static void setRenderEveryXRow(int var0) {
      renderEveryXRow = var0;
   }

   public static boolean isRenderCurrentLayerOnly() {
      return renderCurrentLayerOnly;
   }

   public static void setRenderCurrentLayerOnly(boolean var0) {
      renderCurrentLayerOnly = var0;
   }

   public static int getRenderXRowsFromCenter() {
      return renderXRowsFromCenter;
   }

   public static void setRenderXRowsFromCenter(int var0) {
      renderXRowsFromCenter = var0;
   }

   public static void update() {
      updateKeys();
      if (noiseTexture == null) {
         noiseTexture = Texture.getSharedTexture("media/textures/weather/fognew/fog_noise.png");
      }

      climateManager = ClimateManager.getInstance();
      if (!enableEditing) {
         highQuality = PerformanceSettings.FogQuality == 0;
         fogIntensity.update(GameTime.getInstance().getMultiplier());
         fogIntensity.setTarget(climateManager.getFogIntensity());
         baseAlpha = fogIntensity.value();
         if (highQuality) {
            renderEveryXRow = 1;
            topAlphaHeight = 0.38F;
            bottomAlphaHeight = 0.24F;
            octaves = 6.0F;
            secondLayerAlpha = 0.5F;
         } else {
            renderEveryXRow = 2;
            topAlphaHeight = 0.32F;
            bottomAlphaHeight = 0.32F;
            octaves = 3.0F;
            secondLayerAlpha = 1.0F;
         }

         colorR = climateManager.getColorNewFog().getExterior().r;
         colorG = climateManager.getColorNewFog().getExterior().g;
         colorB = climateManager.getColorNewFog().getExterior().b;
      }

      if (baseAlpha <= 0.0F) {
         scalingX = 0.0F;
         scalingY = 0.0F;
      } else {
         double var0 = (double)climateManager.getWindAngleRadians();
         var0 -= 2.356194490192345D;
         var0 = 3.141592653589793D - var0;
         float var2 = (float)Math.cos(var0);
         float var3 = (float)Math.sin(var0);
         scalingX += var2 * climateManager.getWindIntensity() * GameTime.getInstance().getMultiplier();
         scalingY += var3 * climateManager.getWindIntensity() * GameTime.getInstance().getMultiplier();
      }

   }

   public static void startRender(int var0, int var1) {
      climateManager = ClimateManager.getInstance();
      if (var1 < 2 && !(baseAlpha <= 0.0F) && PerformanceSettings.FogQuality != 2) {
         drawingThisLayer = true;
         IsoPlayer var2 = IsoPlayer.players[var0];
         if (renderCurrentLayerOnly && var2.getZ() != (float)var1) {
            drawingThisLayer = false;
         } else if (var2.isInARoom() && var1 > 0) {
            drawingThisLayer = false;
         } else {
            playerRow = (int)var2.getX() + (int)var2.getY();
            ZOOM = Core.getInstance().getZoom(var0);
            zLayer = var1;
            PlayerIndex = var0;
            PlayerCamera var3 = IsoCamera.cameras[var0];
            screenWidth = (float)IsoCamera.getOffscreenWidth(var0);
            screenHeight = (float)IsoCamera.getOffscreenHeight(var0);
            worldOffsetX = var3.getOffX() - (float)IsoCamera.getOffscreenLeft(PlayerIndex) * ZOOM;
            worldOffsetY = var3.getOffY() + (float)IsoCamera.getOffscreenTop(PlayerIndex) * ZOOM;
            rightClickOffX = var3.RightClickX;
            rightClickOffY = var3.RightClickY;
            cameraOffscreenLeft = (float)IsoCamera.getOffscreenLeft(var0);
            cameraOffscreenTop = (float)IsoCamera.getOffscreenTop(var0);
            cameraZoom = ZOOM;
            if (!enableEditing) {
               if (var2.getVehicle() != null) {
                  alphaCircleAlpha = 0.0F;
                  alphaCircleRad = highQuality ? 2.0F : 2.6F;
               } else if (var2.isInARoom()) {
                  alphaCircleAlpha = 0.0F;
                  alphaCircleRad = highQuality ? 1.25F : 1.5F;
               } else {
                  alphaCircleAlpha = highQuality ? 0.1F : 0.16F;
                  alphaCircleRad = highQuality ? 2.5F : 3.0F;
                  if (climateManager.getWeatherPeriod().isRunning() && (climateManager.getWeatherPeriod().isTropicalStorm() || climateManager.getWeatherPeriod().isThunderStorm())) {
                     alphaCircleRad *= 0.6F;
                  }
               }
            }

            byte var4 = 0;
            byte var5 = 0;
            int var6 = var4 + IsoCamera.getOffscreenWidth(var0);
            int var7 = var5 + IsoCamera.getOffscreenHeight(var0);
            float var8 = IsoUtils.XToIso((float)var4, (float)var5, (float)zLayer);
            float var9 = IsoUtils.YToIso((float)var4, (float)var5, (float)zLayer);
            float var10 = IsoUtils.XToIso((float)var6, (float)var7, (float)zLayer);
            float var11 = IsoUtils.YToIso((float)var6, (float)var7, (float)zLayer);
            float var12 = IsoUtils.YToIso((float)var4, (float)var7, (float)zLayer);
            minY = (int)var9;
            maxY = (int)var11;
            minX = (int)var8;
            maxX = (int)var10;
            if (IsoPlayer.numPlayers > 1) {
               maxX = Math.max(maxX, IsoWorld.instance.CurrentCell.getMaxX());
               maxY = Math.max(maxY, IsoWorld.instance.CurrentCell.getMaxY());
            }

            minX += minXOffset;
            maxX += maxXOffset;
            maxY += maxYOffset;
            int var13 = maxX - minX;
            int var14 = var13;
            if (minY != maxY) {
               var14 = (int)((float)var13 + PZMath.abs((float)(minY - maxY)));
            }

            rectangleIter.reset(var13, var14);
            lastRow = -1;
            fogRectangle.hasStarted = false;
            chunkMap = IsoWorld.instance.getCell().getChunkMap(var0);
         }
      } else {
         drawingThisLayer = false;
      }
   }

   public static void renderRowsBehind(IsoGridSquare var0) {
      if (drawingThisLayer) {
         int var1 = -1;
         if (var0 != null) {
            var1 = var0.getX() + var0.getY();
            if (var1 < minX + minY) {
               return;
            }
         }

         if (lastRow < 0 || lastRow != var1) {
            Vector2i var3 = rectangleMatrixPos;

            while(rectangleIter.next(var3)) {
               if (var3 != null) {
                  int var5 = var3.x + minX;
                  int var6 = var3.y + minY;
                  int var4 = var5 + var6;
                  if (var4 != lastRow) {
                     if (lastRow >= 0 && (!renderEndOnly || var0 == null)) {
                        endFogRectangle(lastIterPos.x, lastIterPos.y, zLayer);
                     }

                     lastRow = var4;
                  }

                  IsoGridSquare var2 = chunkMap.getGridSquare(var5, var6, zLayer);
                  boolean var7 = true;
                  if (var2 != null && (!var2.isExteriorCache || var2.isInARoom())) {
                     var7 = false;
                  }

                  if (var7) {
                     if (!renderEndOnly || var0 == null) {
                        startFogRectangle(var5, var6, zLayer);
                     }
                  } else if (!renderEndOnly || var0 == null) {
                     endFogRectangle(lastIterPos.x, lastIterPos.y, zLayer);
                  }

                  lastIterPos.set(var5, var6);
                  if (var1 != -1 && var4 == var1) {
                     break;
                  }
               }
            }

         }
      }
   }

   public static void endRender() {
      if (drawingThisLayer) {
         renderRowsBehind((IsoGridSquare)null);
         if (fogRectangle.hasStarted) {
            endFogRectangle(lastIterPos.x, lastIterPos.y, zLayer);
         }

      }
   }

   private static void startFogRectangle(int var0, int var1, int var2) {
      if (!fogRectangle.hasStarted) {
         fogRectangle.hasStarted = true;
         fogRectangle.startX = var0;
         fogRectangle.startY = var1;
         fogRectangle.Z = var2;
      }

   }

   private static void endFogRectangle(int var0, int var1, int var2) {
      if (fogRectangle.hasStarted) {
         fogRectangle.hasStarted = false;
         fogRectangle.endX = var0;
         fogRectangle.endY = var1;
         fogRectangle.Z = var2;
         renderFogSegment();
      }

   }

   private static void renderFogSegment() {
      int var0 = fogRectangle.startX + fogRectangle.startY;
      int var1 = fogRectangle.endX + fogRectangle.endY;
      if (Core.bDebug && var0 != var1) {
         DebugLog.log("ROWS NOT EQUAL");
      }

      if (renderOnlyOneRow) {
         if (var0 != playerRow) {
            return;
         }
      } else if (var0 % renderEveryXRow != 0) {
         return;
      }

      if (!Core.bDebug || renderXRowsFromCenter < 1 || var0 >= playerRow - renderXRowsFromCenter && var0 <= playerRow + renderXRowsFromCenter) {
         float var2 = baseAlpha;
         ImprovedFog.FogRectangle var3 = fogRectangle;
         float var4 = IsoUtils.XToScreenExact((float)var3.startX, (float)var3.startY, (float)var3.Z, 0);
         float var5 = IsoUtils.YToScreenExact((float)var3.startX, (float)var3.startY, (float)var3.Z, 0);
         float var6 = IsoUtils.XToScreenExact((float)var3.endX, (float)var3.endY, (float)var3.Z, 0);
         float var7 = IsoUtils.YToScreenExact((float)var3.endX, (float)var3.endY, (float)var3.Z, 0);
         var4 -= 32.0F * (float)Core.TileScale;
         var5 -= 80.0F * (float)Core.TileScale;
         var6 += 32.0F * (float)Core.TileScale;
         float var8 = 96.0F * (float)Core.TileScale;
         float var9 = (var6 - var4) / (64.0F * (float)Core.TileScale);
         float var10 = (float)var3.startX % 6.0F;
         float var11 = var10 / 6.0F;
         float var12 = var9 / 6.0F;
         float var14 = var12 + var11;
         if (FogShader.instance.StartShader()) {
            FogShader.instance.setScreenInfo(screenWidth, screenHeight, ZOOM, zLayer > 0 ? secondLayerAlpha : 1.0F);
            FogShader.instance.setTextureInfo(drawDebugColors ? 1.0F : 0.0F, octaves, var2, (float)Core.TileScale);
            FogShader.instance.setRectangleInfo((float)((int)var4), (float)((int)var5), (float)((int)(var6 - var4)), (float)((int)var8));
            FogShader.instance.setWorldOffset(worldOffsetX, worldOffsetY, rightClickOffX, rightClickOffY);
            FogShader.instance.setScalingInfo(scalingX, scalingY, (float)zLayer, highQuality ? 0.0F : 1.0F);
            FogShader.instance.setColorInfo(colorR, colorG, colorB, 1.0F);
            FogShader.instance.setParamInfo(topAlphaHeight, bottomAlphaHeight, alphaCircleAlpha, alphaCircleRad);
            FogShader.instance.setCameraInfo(cameraOffscreenLeft, cameraOffscreenTop, cameraZoom, offsets[var0 % offsets.length]);
            SpriteRenderer.instance.render(noiseTexture, (float)((int)var4), (float)((int)var5), (float)((int)(var6 - var4)), (float)((int)var8), 1.0F, 1.0F, 1.0F, var2, var11, 0.0F, var14, 0.0F, var14, 1.0F, var11, 1.0F);
            IndieGL.EndShader();
         }

      }
   }

   public static void DrawSubTextureRGBA(Texture var0, double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23) {
      if (var0 != null && !(var5 <= 0.0D) && !(var7 <= 0.0D) && !(var13 <= 0.0D) && !(var15 <= 0.0D)) {
         double var25 = var9 + (double)var0.offsetX;
         double var27 = var11 + (double)var0.offsetY;
         if (!(var27 + var15 < 0.0D) && !(var27 > 4096.0D)) {
            float var29 = PZMath.clamp((float)var1, 0.0F, (float)var0.getWidth());
            float var30 = PZMath.clamp((float)var3, 0.0F, (float)var0.getHeight());
            float var31 = PZMath.clamp((float)((double)var29 + var5), 0.0F, (float)var0.getWidth()) - var29;
            float var32 = PZMath.clamp((float)((double)var30 + var7), 0.0F, (float)var0.getHeight()) - var30;
            float var33 = var29 / (float)var0.getWidth();
            float var34 = var30 / (float)var0.getHeight();
            float var35 = (var29 + var31) / (float)var0.getWidth();
            float var36 = (var30 + var32) / (float)var0.getHeight();
            float var37 = var0.getXEnd() - var0.getXStart();
            float var38 = var0.getYEnd() - var0.getYStart();
            var33 = var0.getXStart() + var33 * var37;
            var35 = var0.getXStart() + var35 * var37;
            var34 = var0.getYStart() + var34 * var38;
            var36 = var0.getYStart() + var36 * var38;
            SpriteRenderer.instance.render(var0, (float)var25, (float)var27, (float)var13, (float)var15, (float)var17, (float)var19, (float)var21, (float)var23, var33, var34, var35, var34, var35, var36, var33, var36);
         }
      }
   }

   public static void updateKeys() {
      if (Core.bDebug) {
         if (keyPause > 0) {
            --keyPause;
         }

         if (keyPause <= 0 && GameKeyboard.isKeyDown(72)) {
            DebugLog.log("Reloading fog shader...");
            keyPause = 30;
            FogShader.instance.reloadShader();
         }

      }
   }

   private static class RectangleIterator {
      private int curX = 0;
      private int curY = 0;
      private int sX;
      private int sY;
      private int rowLen = 0;
      private boolean altRow = false;
      private int curRow = 0;
      private int rowIndex = 0;
      private int maxRows = 0;

      public void reset(int var1, int var2) {
         this.sX = 0;
         this.sY = 0;
         this.curX = 0;
         this.curY = 0;
         this.curRow = 0;
         this.altRow = false;
         this.rowIndex = 0;
         this.rowLen = (int)PZMath.ceil((float)var2 / 2.0F);
         this.maxRows = var1;
      }

      public boolean next(Vector2i var1) {
         if (this.rowLen > 0 && this.maxRows > 0 && this.curRow < this.maxRows) {
            var1.set(this.curX, this.curY);
            ++this.rowIndex;
            if (this.rowIndex == this.rowLen) {
               this.rowLen = this.altRow ? this.rowLen - 1 : this.rowLen + 1;
               this.rowIndex = 0;
               this.sX = this.altRow ? this.sX + 1 : this.sX;
               this.sY = this.altRow ? this.sY : this.sY + 1;
               this.altRow = !this.altRow;
               this.curX = this.sX;
               this.curY = this.sY;
               ++this.curRow;
               return this.curRow != this.maxRows;
            } else {
               ++this.curX;
               --this.curY;
               return true;
            }
         } else {
            var1.set(0, 0);
            return false;
         }
      }
   }

   private static class FogRectangle {
      int startX;
      int startY;
      int endX;
      int endY;
      int Z;
      boolean hasStarted = false;
   }
}
