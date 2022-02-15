package zombie.worldMap;

import org.joml.Matrix4f;
import zombie.config.ConfigOption;
import zombie.input.Mouse;
import zombie.inventory.types.MapItem;
import zombie.worldMap.markers.WorldMapMarkers;
import zombie.worldMap.markers.WorldMapMarkersV1;
import zombie.worldMap.styles.WorldMapStyle;
import zombie.worldMap.styles.WorldMapStyleV1;
import zombie.worldMap.symbols.WorldMapSymbols;
import zombie.worldMap.symbols.WorldMapSymbolsV1;

public class UIWorldMapV1 {
   final UIWorldMap m_ui;
   protected final WorldMap m_worldMap;
   protected final WorldMapStyle m_style;
   protected final WorldMapRenderer m_renderer;
   protected final WorldMapMarkers m_markers;
   protected WorldMapSymbols m_symbols;
   protected WorldMapMarkersV1 m_markersV1 = null;
   protected WorldMapStyleV1 m_styleV1 = null;
   protected WorldMapSymbolsV1 m_symbolsV1 = null;

   public UIWorldMapV1(UIWorldMap var1) {
      this.m_ui = var1;
      this.m_worldMap = this.m_ui.m_worldMap;
      this.m_style = this.m_ui.m_style;
      this.m_renderer = this.m_ui.m_renderer;
      this.m_markers = this.m_ui.m_markers;
      this.m_symbols = this.m_ui.m_symbols;
   }

   public void setMapItem(MapItem var1) {
      this.m_ui.setMapItem(var1);
      this.m_symbols = this.m_ui.m_symbols;
   }

   public WorldMapRenderer getRenderer() {
      return this.m_renderer;
   }

   public WorldMapMarkers getMarkers() {
      return this.m_markers;
   }

   public WorldMapStyle getStyle() {
      return this.m_style;
   }

   public WorldMapMarkersV1 getMarkersAPI() {
      if (this.m_markersV1 == null) {
         this.m_markersV1 = new WorldMapMarkersV1(this.m_ui);
      }

      return this.m_markersV1;
   }

   public WorldMapStyleV1 getStyleAPI() {
      if (this.m_styleV1 == null) {
         this.m_styleV1 = new WorldMapStyleV1(this.m_ui);
      }

      return this.m_styleV1;
   }

   public WorldMapSymbolsV1 getSymbolsAPI() {
      if (this.m_symbolsV1 == null) {
         this.m_symbolsV1 = new WorldMapSymbolsV1(this.m_ui, this.m_symbols);
      }

      return this.m_symbolsV1;
   }

   public void addData(String var1) {
      boolean var2 = this.m_worldMap.hasData();
      this.m_worldMap.addData(var1);
      if (!var2) {
         this.m_renderer.setMap(this.m_worldMap, this.m_ui.getAbsoluteX().intValue(), this.m_ui.getAbsoluteY().intValue(), this.m_ui.getWidth().intValue(), this.m_ui.getHeight().intValue());
         this.resetView();
      }

   }

   public int getDataCount() {
      return this.m_worldMap.getDataCount();
   }

   public String getDataFileByIndex(int var1) {
      WorldMapData var2 = this.m_worldMap.getDataByIndex(var1);
      return var2.m_relativeFileName;
   }

   public void clearData() {
      this.m_worldMap.clearData();
   }

   public void endDirectoryData() {
      this.m_worldMap.endDirectoryData();
   }

   public void addImages(String var1) {
      boolean var2 = this.m_worldMap.hasImages();
      this.m_worldMap.addImages(var1);
      if (!var2) {
         this.m_renderer.setMap(this.m_worldMap, this.m_ui.getAbsoluteX().intValue(), this.m_ui.getAbsoluteY().intValue(), this.m_ui.getWidth().intValue(), this.m_ui.getHeight().intValue());
         this.resetView();
      }

   }

   public int getImagesCount() {
      return this.m_worldMap.getImagesCount();
   }

   public void setBoundsInCells(int var1, int var2, int var3, int var4) {
      boolean var5 = var1 * 300 != this.m_worldMap.m_minX || var2 * 300 != this.m_worldMap.m_minY || var3 * 300 + 299 != this.m_worldMap.m_maxX || var4 + 300 + 299 != this.m_worldMap.m_maxY;
      this.m_worldMap.setBoundsInCells(var1, var2, var3, var4);
      if (var5 && this.m_worldMap.hasData()) {
         this.resetView();
      }

   }

   public void setBoundsInSquares(int var1, int var2, int var3, int var4) {
      boolean var5 = var1 != this.m_worldMap.m_minX || var2 != this.m_worldMap.m_minY || var3 != this.m_worldMap.m_maxX || var4 != this.m_worldMap.m_maxY;
      this.m_worldMap.setBoundsInSquares(var1, var2, var3, var4);
      if (var5 && this.m_worldMap.hasData()) {
         this.resetView();
      }

   }

   public void setBoundsFromWorld() {
      this.m_worldMap.setBoundsFromWorld();
   }

   public void setBoundsFromData() {
      this.m_worldMap.setBoundsFromData();
   }

   public int getMinXInCells() {
      return this.m_worldMap.getMinXInCells();
   }

   public int getMinYInCells() {
      return this.m_worldMap.getMinYInCells();
   }

   public int getMaxXInCells() {
      return this.m_worldMap.getMaxXInCells();
   }

   public int getMaxYInCells() {
      return this.m_worldMap.getMaxYInCells();
   }

   public int getWidthInCells() {
      return this.m_worldMap.getWidthInCells();
   }

   public int getHeightInCells() {
      return this.m_worldMap.getHeightInCells();
   }

   public int getMinXInSquares() {
      return this.m_worldMap.getMinXInSquares();
   }

   public int getMinYInSquares() {
      return this.m_worldMap.getMinYInSquares();
   }

   public int getMaxXInSquares() {
      return this.m_worldMap.getMaxXInSquares();
   }

   public int getMaxYInSquares() {
      return this.m_worldMap.getMaxYInSquares();
   }

   public int getWidthInSquares() {
      return this.m_worldMap.getWidthInSquares();
   }

   public int getHeightInSquares() {
      return this.m_worldMap.getHeightInSquares();
   }

   public float uiToWorldX(float var1, float var2, float var3, float var4, float var5) {
      return this.m_renderer.uiToWorldX(var1, var2, var3, var4, var5, this.m_renderer.getProjectionMatrix(), this.m_renderer.getModelViewMatrix());
   }

   public float uiToWorldY(float var1, float var2, float var3, float var4, float var5) {
      return this.m_renderer.uiToWorldY(var1, var2, var3, var4, var5, this.m_renderer.getProjectionMatrix(), this.m_renderer.getModelViewMatrix());
   }

   protected float worldToUIX(float var1, float var2, float var3, float var4, float var5, Matrix4f var6, Matrix4f var7) {
      return this.m_renderer.worldToUIX(var1, var2, var3, var4, var5, var6, var7);
   }

   protected float worldToUIY(float var1, float var2, float var3, float var4, float var5, Matrix4f var6, Matrix4f var7) {
      return this.m_renderer.worldToUIY(var1, var2, var3, var4, var5, var6, var7);
   }

   protected float worldOriginUIX(float var1, float var2) {
      return this.m_renderer.worldOriginUIX(var1, var2);
   }

   protected float worldOriginUIY(float var1, float var2) {
      return this.m_renderer.worldOriginUIY(var1, var2);
   }

   protected float zoomMult() {
      return this.m_renderer.zoomMult();
   }

   protected float getWorldScale(float var1) {
      return this.m_renderer.getWorldScale(var1);
   }

   public float worldOriginX() {
      return this.m_renderer.worldOriginUIX(this.m_renderer.getDisplayZoomF(), this.m_renderer.getCenterWorldX());
   }

   public float worldOriginY() {
      return this.m_renderer.worldOriginUIY(this.m_renderer.getDisplayZoomF(), this.m_renderer.getCenterWorldY());
   }

   public float getBaseZoom() {
      return this.m_renderer.getBaseZoom();
   }

   public float getZoomF() {
      return this.m_renderer.getDisplayZoomF();
   }

   public float getWorldScale() {
      return this.m_renderer.getWorldScale(this.m_renderer.getDisplayZoomF());
   }

   public float getCenterWorldX() {
      return this.m_renderer.getCenterWorldX();
   }

   public float getCenterWorldY() {
      return this.m_renderer.getCenterWorldY();
   }

   public float uiToWorldX(float var1, float var2) {
      return !this.m_worldMap.hasData() && !this.m_worldMap.hasImages() ? 0.0F : this.uiToWorldX(var1, var2, this.m_renderer.getDisplayZoomF(), this.m_renderer.getCenterWorldX(), this.m_renderer.getCenterWorldY());
   }

   public float uiToWorldY(float var1, float var2) {
      return !this.m_worldMap.hasData() && !this.m_worldMap.hasImages() ? 0.0F : this.uiToWorldY(var1, var2, this.m_renderer.getDisplayZoomF(), this.m_renderer.getCenterWorldY(), this.m_renderer.getCenterWorldY());
   }

   public float worldToUIX(float var1, float var2) {
      return !this.m_worldMap.hasData() && !this.m_worldMap.hasImages() ? 0.0F : this.worldToUIX(var1, var2, this.m_renderer.getDisplayZoomF(), this.m_renderer.getCenterWorldX(), this.m_renderer.getCenterWorldY(), this.m_renderer.getProjectionMatrix(), this.m_renderer.getModelViewMatrix());
   }

   public float worldToUIY(float var1, float var2) {
      return !this.m_worldMap.hasData() && !this.m_worldMap.hasImages() ? 0.0F : this.worldToUIY(var1, var2, this.m_renderer.getDisplayZoomF(), this.m_renderer.getCenterWorldX(), this.m_renderer.getCenterWorldY(), this.m_renderer.getProjectionMatrix(), this.m_renderer.getModelViewMatrix());
   }

   public void centerOn(float var1, float var2) {
      if (this.m_worldMap.hasData() || this.m_worldMap.hasImages()) {
         this.m_renderer.centerOn(var1, var2);
      }
   }

   public void moveView(float var1, float var2) {
      if (this.m_worldMap.hasData() || this.m_worldMap.hasImages()) {
         this.m_renderer.moveView((int)var1, (int)var2);
      }
   }

   public void zoomAt(float var1, float var2, float var3) {
      if (this.m_worldMap.hasData() || this.m_worldMap.hasImages()) {
         this.m_renderer.zoomAt((int)var1, (int)var2, -((int)var3));
      }
   }

   public void setZoom(float var1) {
      this.m_renderer.setZoom(var1);
   }

   public void resetView() {
      if (this.m_worldMap.hasData() || this.m_worldMap.hasImages()) {
         this.m_renderer.resetView();
      }
   }

   public float mouseToWorldX() {
      float var1 = (float)(Mouse.getXA() - this.m_ui.getAbsoluteX().intValue());
      float var2 = (float)(Mouse.getYA() - this.m_ui.getAbsoluteY().intValue());
      return this.uiToWorldX(var1, var2);
   }

   public float mouseToWorldY() {
      float var1 = (float)(Mouse.getXA() - this.m_ui.getAbsoluteX().intValue());
      float var2 = (float)(Mouse.getYA() - this.m_ui.getAbsoluteY().intValue());
      return this.uiToWorldY(var1, var2);
   }

   public void setBackgroundRGBA(float var1, float var2, float var3, float var4) {
      this.m_ui.m_color.init(var1, var2, var3, var4);
   }

   public void setDropShadowWidth(int var1) {
      this.m_ui.m_renderer.setDropShadowWidth(var1);
   }

   public void setUnvisitedRGBA(float var1, float var2, float var3, float var4) {
      WorldMapVisited.getInstance().setUnvisitedRGBA(var1, var2, var3, var4);
   }

   public void setUnvisitedGridRGBA(float var1, float var2, float var3, float var4) {
      WorldMapVisited.getInstance().setUnvisitedGridRGBA(var1, var2, var3, var4);
   }

   public int getOptionCount() {
      return this.m_renderer.getOptionCount();
   }

   public ConfigOption getOptionByIndex(int var1) {
      return this.m_renderer.getOptionByIndex(var1);
   }

   public void setBoolean(String var1, boolean var2) {
      this.m_renderer.setBoolean(var1, var2);
   }

   public boolean getBoolean(String var1) {
      return this.m_renderer.getBoolean(var1);
   }

   public void setDouble(String var1, double var2) {
      this.m_renderer.setDouble(var1, var2);
   }

   public double getDouble(String var1, double var2) {
      return this.m_renderer.getDouble(var1, var2);
   }
}
