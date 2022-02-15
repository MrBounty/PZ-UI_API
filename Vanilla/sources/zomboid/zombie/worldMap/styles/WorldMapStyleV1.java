package zombie.worldMap.styles;

import java.util.ArrayList;
import java.util.Objects;
import zombie.Lua.LuaManager;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.worldMap.UIWorldMap;
import zombie.worldMap.UIWorldMapV1;

public class WorldMapStyleV1 {
   public UIWorldMap m_ui;
   public UIWorldMapV1 m_api;
   public WorldMapStyle m_style;
   public final ArrayList m_layers = new ArrayList();

   public WorldMapStyleV1(UIWorldMap var1) {
      Objects.requireNonNull(var1);
      this.m_ui = var1;
      this.m_api = var1.getAPIv1();
      this.m_style = this.m_api.getStyle();
   }

   public WorldMapStyleV1.WorldMapStyleLayerV1 newLineLayer(String var1) throws IllegalArgumentException {
      WorldMapStyleV1.WorldMapLineStyleLayerV1 var2 = new WorldMapStyleV1.WorldMapLineStyleLayerV1(this, var1);
      this.m_layers.add(var2);
      return var2;
   }

   public WorldMapStyleV1.WorldMapStyleLayerV1 newPolygonLayer(String var1) throws IllegalArgumentException {
      WorldMapStyleV1.WorldMapPolygonStyleLayerV1 var2 = new WorldMapStyleV1.WorldMapPolygonStyleLayerV1(this, var1);
      this.m_layers.add(var2);
      return var2;
   }

   public WorldMapStyleV1.WorldMapStyleLayerV1 newTextureLayer(String var1) throws IllegalArgumentException {
      WorldMapStyleV1.WorldMapTextureStyleLayerV1 var2 = new WorldMapStyleV1.WorldMapTextureStyleLayerV1(this, var1);
      this.m_layers.add(var2);
      return var2;
   }

   public int getLayerCount() {
      return this.m_layers.size();
   }

   public WorldMapStyleV1.WorldMapStyleLayerV1 getLayerByIndex(int var1) {
      return (WorldMapStyleV1.WorldMapStyleLayerV1)this.m_layers.get(var1);
   }

   public WorldMapStyleV1.WorldMapStyleLayerV1 getLayerByName(String var1) {
      int var2 = this.indexOfLayer(var1);
      return var2 == -1 ? null : (WorldMapStyleV1.WorldMapStyleLayerV1)this.m_layers.get(var2);
   }

   public int indexOfLayer(String var1) {
      for(int var2 = 0; var2 < this.m_layers.size(); ++var2) {
         WorldMapStyleV1.WorldMapStyleLayerV1 var3 = (WorldMapStyleV1.WorldMapStyleLayerV1)this.m_layers.get(var2);
         if (var3.m_layer.m_id.equals(var1)) {
            return var2;
         }
      }

      return -1;
   }

   public void moveLayer(int var1, int var2) {
      WorldMapStyleLayer var3 = (WorldMapStyleLayer)this.m_style.m_layers.remove(var1);
      this.m_style.m_layers.add(var2, var3);
      WorldMapStyleV1.WorldMapStyleLayerV1 var4 = (WorldMapStyleV1.WorldMapStyleLayerV1)this.m_layers.remove(var1);
      this.m_layers.add(var2, var4);
   }

   public void removeLayerById(String var1) {
      int var2 = this.indexOfLayer(var1);
      if (var2 != -1) {
         this.removeLayerByIndex(var2);
      }
   }

   public void removeLayerByIndex(int var1) {
      this.m_style.m_layers.remove(var1);
      this.m_layers.remove(var1);
   }

   public void clear() {
      this.m_style.m_layers.clear();
      this.m_layers.clear();
   }

   public static void setExposed(LuaManager.Exposer var0) {
      var0.setExposed(WorldMapStyleV1.class);
      var0.setExposed(WorldMapStyleV1.WorldMapStyleLayerV1.class);
      var0.setExposed(WorldMapStyleV1.WorldMapLineStyleLayerV1.class);
      var0.setExposed(WorldMapStyleV1.WorldMapPolygonStyleLayerV1.class);
      var0.setExposed(WorldMapStyleV1.WorldMapTextureStyleLayerV1.class);
   }

   public static class WorldMapLineStyleLayerV1 extends WorldMapStyleV1.WorldMapStyleLayerV1 {
      WorldMapLineStyleLayer m_lineStyle;

      WorldMapLineStyleLayerV1(WorldMapStyleV1 var1, String var2) {
         super(var1, new WorldMapLineStyleLayer(var2));
         this.m_lineStyle = (WorldMapLineStyleLayer)this.m_layer;
      }

      public void setFilter(String var1, String var2) {
         this.m_lineStyle.m_filterKey = var1;
         this.m_lineStyle.m_filterValue = var2;
         this.m_lineStyle.m_filter = (var2x, var3) -> {
            return var2x.hasLineString() && var2.equals(var2x.m_properties.get(var1));
         };
      }

      public void addFill(float var1, int var2, int var3, int var4, int var5) {
         this.m_lineStyle.m_fill.add(new WorldMapStyleLayer.ColorStop(var1, var2, var3, var4, var5));
      }

      public void addLineWidth(float var1, float var2) {
         this.m_lineStyle.m_lineWidth.add(new WorldMapStyleLayer.FloatStop(var1, var2));
      }
   }

   public static class WorldMapPolygonStyleLayerV1 extends WorldMapStyleV1.WorldMapStyleLayerV1 {
      WorldMapPolygonStyleLayer m_polygonStyle;

      WorldMapPolygonStyleLayerV1(WorldMapStyleV1 var1, String var2) {
         super(var1, new WorldMapPolygonStyleLayer(var2));
         this.m_polygonStyle = (WorldMapPolygonStyleLayer)this.m_layer;
      }

      public void setFilter(String var1, String var2) {
         this.m_polygonStyle.m_filterKey = var1;
         this.m_polygonStyle.m_filterValue = var2;
         this.m_polygonStyle.m_filter = (var2x, var3) -> {
            return var2x.hasPolygon() && var2.equals(var2x.m_properties.get(var1));
         };
      }

      public String getFilterKey() {
         return this.m_polygonStyle.m_filterKey;
      }

      public String getFilterValue() {
         return this.m_polygonStyle.m_filterValue;
      }

      public void addFill(float var1, int var2, int var3, int var4, int var5) {
         this.m_polygonStyle.m_fill.add(new WorldMapStyleLayer.ColorStop(var1, var2, var3, var4, var5));
      }

      public void addScale(float var1, float var2) {
         this.m_polygonStyle.m_scale.add(new WorldMapStyleLayer.FloatStop(var1, var2));
      }

      public void addTexture(float var1, String var2) {
         this.m_polygonStyle.m_texture.add(new WorldMapStyleLayer.TextureStop(var1, var2));
      }

      public void removeFill(int var1) {
         this.m_polygonStyle.m_fill.remove(var1);
      }

      public void removeTexture(int var1) {
         this.m_polygonStyle.m_texture.remove(var1);
      }

      public void moveFill(int var1, int var2) {
         WorldMapStyleLayer.ColorStop var3 = (WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.remove(var1);
         this.m_polygonStyle.m_fill.add(var2, var3);
      }

      public void moveTexture(int var1, int var2) {
         WorldMapStyleLayer.TextureStop var3 = (WorldMapStyleLayer.TextureStop)this.m_polygonStyle.m_texture.remove(var1);
         this.m_polygonStyle.m_texture.add(var2, var3);
      }

      public int getFillStops() {
         return this.m_polygonStyle.m_fill.size();
      }

      public void setFillRGBA(int var1, int var2, int var3, int var4, int var5) {
         ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).r = var2;
         ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).g = var3;
         ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).b = var4;
         ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).a = var5;
      }

      public void setFillZoom(int var1, float var2) {
         ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).m_zoom = PZMath.clamp(var2, 0.0F, 24.0F);
      }

      public float getFillZoom(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).m_zoom;
      }

      public int getFillRed(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).r;
      }

      public int getFillGreen(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).g;
      }

      public int getFillBlue(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).b;
      }

      public int getFillAlpha(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_polygonStyle.m_fill.get(var1)).a;
      }

      public int getTextureStops() {
         return this.m_polygonStyle.m_texture.size();
      }

      public void setTextureZoom(int var1, float var2) {
         ((WorldMapStyleLayer.TextureStop)this.m_polygonStyle.m_texture.get(var1)).m_zoom = PZMath.clamp(var2, 0.0F, 24.0F);
      }

      public float getTextureZoom(int var1) {
         return ((WorldMapStyleLayer.TextureStop)this.m_polygonStyle.m_texture.get(var1)).m_zoom;
      }

      public void setTexturePath(int var1, String var2) {
         ((WorldMapStyleLayer.TextureStop)this.m_polygonStyle.m_texture.get(var1)).texturePath = var2;
         ((WorldMapStyleLayer.TextureStop)this.m_polygonStyle.m_texture.get(var1)).texture = Texture.getTexture(var2);
      }

      public String getTexturePath(int var1) {
         return ((WorldMapStyleLayer.TextureStop)this.m_polygonStyle.m_texture.get(var1)).texturePath;
      }

      public Texture getTexture(int var1) {
         return ((WorldMapStyleLayer.TextureStop)this.m_polygonStyle.m_texture.get(var1)).texture;
      }
   }

   public static class WorldMapTextureStyleLayerV1 extends WorldMapStyleV1.WorldMapStyleLayerV1 {
      WorldMapTextureStyleLayer m_textureStyle;

      WorldMapTextureStyleLayerV1(WorldMapStyleV1 var1, String var2) {
         super(var1, new WorldMapTextureStyleLayer(var2));
         this.m_textureStyle = (WorldMapTextureStyleLayer)this.m_layer;
      }

      public void addFill(float var1, int var2, int var3, int var4, int var5) {
         this.m_textureStyle.m_fill.add(new WorldMapStyleLayer.ColorStop(var1, var2, var3, var4, var5));
      }

      public void addTexture(float var1, String var2) {
         this.m_textureStyle.m_texture.add(new WorldMapStyleLayer.TextureStop(var1, var2));
      }

      public void removeFill(int var1) {
         this.m_textureStyle.m_fill.remove(var1);
      }

      public void removeAllFill() {
         this.m_textureStyle.m_fill.clear();
      }

      public void removeTexture(int var1) {
         this.m_textureStyle.m_texture.remove(var1);
      }

      public void removeAllTexture() {
         this.m_textureStyle.m_texture.clear();
      }

      public void moveFill(int var1, int var2) {
         WorldMapStyleLayer.ColorStop var3 = (WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.remove(var1);
         this.m_textureStyle.m_fill.add(var2, var3);
      }

      public void moveTexture(int var1, int var2) {
         WorldMapStyleLayer.TextureStop var3 = (WorldMapStyleLayer.TextureStop)this.m_textureStyle.m_texture.remove(var1);
         this.m_textureStyle.m_texture.add(var2, var3);
      }

      public void setBoundsInSquares(int var1, int var2, int var3, int var4) {
         this.m_textureStyle.m_worldX1 = var1;
         this.m_textureStyle.m_worldY1 = var2;
         this.m_textureStyle.m_worldX2 = var3;
         this.m_textureStyle.m_worldY2 = var4;
      }

      public int getMinXInSquares() {
         return this.m_textureStyle.m_worldX1;
      }

      public int getMinYInSquares() {
         return this.m_textureStyle.m_worldY1;
      }

      public int getMaxXInSquares() {
         return this.m_textureStyle.m_worldX2;
      }

      public int getMaxYInSquares() {
         return this.m_textureStyle.m_worldY2;
      }

      public int getWidthInSquares() {
         return this.m_textureStyle.m_worldX2 - this.m_textureStyle.m_worldX1;
      }

      public int getHeightInSquares() {
         return this.m_textureStyle.m_worldY2 - this.m_textureStyle.m_worldY1;
      }

      public void setTile(boolean var1) {
         this.m_textureStyle.m_tile = var1;
      }

      public boolean isTile() {
         return this.m_textureStyle.m_tile;
      }

      public void setUseWorldBounds(boolean var1) {
         this.m_textureStyle.m_useWorldBounds = var1;
      }

      public boolean isUseWorldBounds() {
         return this.m_textureStyle.m_useWorldBounds;
      }

      public int getFillStops() {
         return this.m_textureStyle.m_fill.size();
      }

      public void setFillRGBA(int var1, int var2, int var3, int var4, int var5) {
         ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).r = var2;
         ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).g = var3;
         ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).b = var4;
         ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).a = var5;
      }

      public void setFillZoom(int var1, float var2) {
         ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).m_zoom = PZMath.clamp(var2, 0.0F, 24.0F);
      }

      public float getFillZoom(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).m_zoom;
      }

      public int getFillRed(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).r;
      }

      public int getFillGreen(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).g;
      }

      public int getFillBlue(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).b;
      }

      public int getFillAlpha(int var1) {
         return ((WorldMapStyleLayer.ColorStop)this.m_textureStyle.m_fill.get(var1)).a;
      }

      public int getTextureStops() {
         return this.m_textureStyle.m_texture.size();
      }

      public void setTextureZoom(int var1, float var2) {
         ((WorldMapStyleLayer.TextureStop)this.m_textureStyle.m_texture.get(var1)).m_zoom = PZMath.clamp(var2, 0.0F, 24.0F);
      }

      public float getTextureZoom(int var1) {
         return ((WorldMapStyleLayer.TextureStop)this.m_textureStyle.m_texture.get(var1)).m_zoom;
      }

      public void setTexturePath(int var1, String var2) {
         ((WorldMapStyleLayer.TextureStop)this.m_textureStyle.m_texture.get(var1)).texturePath = var2;
         ((WorldMapStyleLayer.TextureStop)this.m_textureStyle.m_texture.get(var1)).texture = Texture.getTexture(var2);
      }

      public String getTexturePath(int var1) {
         return ((WorldMapStyleLayer.TextureStop)this.m_textureStyle.m_texture.get(var1)).texturePath;
      }

      public Texture getTexture(int var1) {
         return ((WorldMapStyleLayer.TextureStop)this.m_textureStyle.m_texture.get(var1)).texture;
      }
   }

   public static class WorldMapStyleLayerV1 {
      WorldMapStyleV1 m_owner;
      WorldMapStyleLayer m_layer;

      WorldMapStyleLayerV1(WorldMapStyleV1 var1, WorldMapStyleLayer var2) {
         this.m_owner = var1;
         this.m_layer = var2;
         var1.m_style.m_layers.add(this.m_layer);
      }

      public String getTypeString() {
         return this.m_layer.getTypeString();
      }

      public void setId(String var1) {
         this.m_layer.m_id = var1;
      }

      public String getId() {
         return this.m_layer.m_id;
      }

      public void setMinZoom(float var1) {
         this.m_layer.m_minZoom = var1;
      }

      public float getMinZoom() {
         return this.m_layer.m_minZoom;
      }
   }
}
