package zombie.worldMap;

import java.util.ArrayList;
import java.util.Iterator;
import se.krka.kahlua.vm.KahluaTable;
import zombie.Lua.LuaManager;
import zombie.characters.IsoPlayer;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.textures.Texture;
import zombie.debug.DebugOptions;
import zombie.input.GameKeyboard;
import zombie.inventory.types.MapItem;
import zombie.iso.BuildingDef;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.iso.RoomDef;
import zombie.ui.TextManager;
import zombie.ui.UIElement;
import zombie.ui.UIFont;
import zombie.worldMap.editor.WorldMapEditorState;
import zombie.worldMap.markers.WorldMapGridSquareMarker;
import zombie.worldMap.markers.WorldMapMarkers;
import zombie.worldMap.markers.WorldMapMarkersV1;
import zombie.worldMap.styles.WorldMapStyle;
import zombie.worldMap.styles.WorldMapStyleLayer;
import zombie.worldMap.styles.WorldMapStyleV1;
import zombie.worldMap.symbols.MapSymbolDefinitions;
import zombie.worldMap.symbols.WorldMapSymbols;
import zombie.worldMap.symbols.WorldMapSymbolsV1;

public class UIWorldMap extends UIElement {
   static final ArrayList s_tempFeatures = new ArrayList();
   protected final WorldMap m_worldMap = new WorldMap();
   protected final WorldMapStyle m_style = new WorldMapStyle();
   protected final WorldMapRenderer m_renderer = new WorldMapRenderer();
   protected final WorldMapMarkers m_markers = new WorldMapMarkers();
   protected WorldMapSymbols m_symbols = null;
   protected final WorldMapStyleLayer.RGBAf m_color = (new WorldMapStyleLayer.RGBAf()).init(0.85882354F, 0.84313726F, 0.7529412F, 1.0F);
   protected final UIWorldMapV1 m_APIv1 = new UIWorldMapV1(this);
   private boolean m_dataWasReady = false;
   private final ArrayList m_buildingsWithoutFeatures = new ArrayList();
   private boolean m_bBuildingsWithoutFeatures = false;

   public UIWorldMap(KahluaTable var1) {
      super(var1);
   }

   public UIWorldMapV1 getAPI() {
      return this.m_APIv1;
   }

   public UIWorldMapV1 getAPIv1() {
      return this.m_APIv1;
   }

   protected void setMapItem(MapItem var1) {
      this.m_symbols = var1.getSymbols();
   }

   public void render() {
      if (this.isVisible()) {
         if (this.Parent == null || this.Parent.getMaxDrawHeight() == -1.0D || !(this.Parent.getMaxDrawHeight() <= this.getY())) {
            this.DrawTextureScaledColor((Texture)null, 0.0D, 0.0D, this.getWidth(), this.getHeight(), (double)this.m_color.r, (double)this.m_color.g, (double)this.m_color.b, (double)this.m_color.a);
            if (!this.m_worldMap.hasData()) {
            }

            this.setStencilRect(0.0D, 0.0D, this.getWidth(), this.getHeight());
            this.m_renderer.setMap(this.m_worldMap, this.getAbsoluteX().intValue(), this.getAbsoluteY().intValue(), this.getWidth().intValue(), this.getHeight().intValue());
            this.m_renderer.updateView();
            float var1 = this.m_renderer.getDisplayZoomF();
            float var2 = this.m_renderer.getCenterWorldX();
            float var3 = this.m_renderer.getCenterWorldY();
            this.m_APIv1.getWorldScale(var1);
            if (this.m_renderer.getBoolean("HideUnvisited") && WorldMapVisited.getInstance() != null) {
               this.m_renderer.setVisited(WorldMapVisited.getInstance());
            } else {
               this.m_renderer.setVisited((WorldMapVisited)null);
            }

            this.m_renderer.render(this);
            if (this.m_renderer.getBoolean("Symbols")) {
               this.m_symbols.render(this);
            }

            this.m_markers.render(this);
            int var5;
            float var7;
            if (this.m_renderer.getBoolean("Players") && var1 < 20.0F) {
               for(var5 = 0; var5 < IsoPlayer.numPlayers; ++var5) {
                  IsoPlayer var6 = IsoPlayer.players[var5];
                  if (var6 != null && !var6.isDead()) {
                     var7 = var6.x;
                     float var8 = var6.y;
                     if (var6.getVehicle() != null) {
                        var7 = var6.getVehicle().getX();
                        var8 = var6.getVehicle().getY();
                     }

                     float var9 = this.m_APIv1.worldToUIX(var7, var8, var1, var2, var3, this.m_renderer.getProjectionMatrix(), this.m_renderer.getModelViewMatrix());
                     float var10 = this.m_APIv1.worldToUIY(var7, var8, var1, var2, var3, this.m_renderer.getProjectionMatrix(), this.m_renderer.getModelViewMatrix());
                     var9 = PZMath.floor(var9);
                     var10 = PZMath.floor(var10);
                     this.DrawTextureScaledColor((Texture)null, (double)var9 - 3.0D, (double)var10 - 3.0D, 6.0D, 6.0D, 1.0D, 0.0D, 0.0D, 1.0D);
                  }
               }
            }

            var5 = TextManager.instance.getFontHeight(UIFont.Small);
            float var23;
            double var26;
            int var35;
            if (Core.bDebug && this.m_renderer.getBoolean("DebugInfo")) {
               this.DrawTextureScaledColor((Texture)null, 0.0D, 0.0D, 200.0D, (double)var5 * 4.0D, 1.0D, 1.0D, 1.0D, 1.0D);
               var23 = this.m_APIv1.mouseToWorldX();
               var7 = this.m_APIv1.mouseToWorldY();
               var26 = 0.0D;
               double var31 = 0.0D;
               double var12 = 0.0D;
               double var14 = 1.0D;
               byte var16 = 0;
               this.DrawText("SQUARE = " + (int)var23 + "," + (int)var7, 0.0D, (double)var16, var26, var31, var12, var14);
               var35 = var16 + var5;
               this.DrawText("CELL = " + (int)(var23 / 300.0F) + "," + (int)(var7 / 300.0F), 0.0D, (double)var5, var26, var31, var12, var14);
               var35 += var5;
               this.DrawText("ZOOM = " + this.m_renderer.getDisplayZoomF(), 0.0D, (double)var35, var26, var31, var12, var14);
               var35 += var5;
               WorldMapRenderer var10001 = this.m_renderer;
               this.DrawText("SCALE = " + var10001.getWorldScale(this.m_renderer.getZoomF()), 0.0D, (double)var35, var26, var31, var12, var14);
               int var10000 = var35 + var5;
            }

            this.clearStencilRect();
            this.repaintStencilRect(0.0D, 0.0D, (double)this.width, (double)this.height);
            if (Core.bDebug && DebugOptions.instance.UIRenderOutline.getValue()) {
               Double var24 = -this.getXScroll();
               Double var25 = -this.getYScroll();
               var26 = this.isMouseOver() ? 0.0D : 1.0D;
               this.DrawTextureScaledColor((Texture)null, var24, var25, 1.0D, (double)this.height, var26, 1.0D, 1.0D, 0.5D);
               this.DrawTextureScaledColor((Texture)null, var24 + 1.0D, var25, (double)this.width - 2.0D, 1.0D, var26, 1.0D, 1.0D, 0.5D);
               this.DrawTextureScaledColor((Texture)null, var24 + (double)this.width - 1.0D, var25, 1.0D, (double)this.height, var26, 1.0D, 1.0D, 0.5D);
               this.DrawTextureScaledColor((Texture)null, var24 + 1.0D, var25 + (double)this.height - 1.0D, (double)this.width - 2.0D, 1.0D, var26, 1.0D, 1.0D, 0.5D);
            }

            if (Core.bDebug && this.m_renderer.getBoolean("HitTest")) {
               var23 = this.m_APIv1.mouseToWorldX();
               var7 = this.m_APIv1.mouseToWorldY();
               s_tempFeatures.clear();
               Iterator var27 = this.m_worldMap.m_data.iterator();

               while(var27.hasNext()) {
                  WorldMapData var28 = (WorldMapData)var27.next();
                  if (var28.isReady()) {
                     var28.hitTest(var23, var7, s_tempFeatures);
                  }
               }

               if (!s_tempFeatures.isEmpty()) {
                  WorldMapFeature var29 = (WorldMapFeature)s_tempFeatures.get(s_tempFeatures.size() - 1);
                  int var30 = var29.m_cell.m_x * 300;
                  int var32 = var29.m_cell.m_y * 300;
                  int var11 = this.getAbsoluteX().intValue();
                  int var33 = this.getAbsoluteY().intValue();
                  WorldMapPoints var13 = (WorldMapPoints)((WorldMapGeometry)var29.m_geometries.get(0)).m_points.get(0);

                  for(int var34 = 0; var34 < var13.numPoints(); ++var34) {
                     int var15 = var13.getX(var34);
                     var35 = var13.getY(var34);
                     int var17 = var13.getX((var34 + 1) % var13.numPoints());
                     int var18 = var13.getY((var34 + 1) % var13.numPoints());
                     float var19 = this.m_APIv1.worldToUIX((float)(var30 + var15), (float)(var32 + var35));
                     float var20 = this.m_APIv1.worldToUIY((float)(var30 + var15), (float)(var32 + var35));
                     float var21 = this.m_APIv1.worldToUIX((float)(var30 + var17), (float)(var32 + var18));
                     float var22 = this.m_APIv1.worldToUIY((float)(var30 + var17), (float)(var32 + var18));
                     SpriteRenderer.instance.renderline((Texture)null, var11 + (int)var19, var33 + (int)var20, var11 + (int)var21, var33 + (int)var22, 1.0F, 0.0F, 0.0F, 1.0F);
                  }
               }
            }

            if (Core.bDebug && this.m_renderer.getBoolean("BuildingsWithoutFeatures") && !this.m_renderer.getBoolean("Isometric")) {
               this.renderBuildingsWithoutFeatures();
            }

            super.render();
         }
      }
   }

   public void update() {
      super.update();
   }

   public Boolean onMouseDown(double var1, double var3) {
      if (GameKeyboard.isKeyDown(42)) {
         this.m_renderer.resetView();
      }

      return super.onMouseDown(var1, var3);
   }

   public Boolean onMouseUp(double var1, double var3) {
      return super.onMouseUp(var1, var3);
   }

   public void onMouseUpOutside(double var1, double var3) {
      super.onMouseUpOutside(var1, var3);
   }

   public Boolean onMouseMove(double var1, double var3) {
      return super.onMouseMove(var1, var3);
   }

   public Boolean onMouseWheel(double var1) {
      return super.onMouseWheel(var1);
   }

   public static void setExposed(LuaManager.Exposer var0) {
      var0.setExposed(MapItem.class);
      var0.setExposed(MapSymbolDefinitions.class);
      var0.setExposed(MapSymbolDefinitions.MapSymbolDefinition.class);
      var0.setExposed(UIWorldMap.class);
      var0.setExposed(UIWorldMapV1.class);
      var0.setExposed(WorldMapGridSquareMarker.class);
      var0.setExposed(WorldMapMarkers.class);
      var0.setExposed(WorldMapRenderer.WorldMapBooleanOption.class);
      var0.setExposed(WorldMapRenderer.WorldMapDoubleOption.class);
      var0.setExposed(WorldMapVisited.class);
      WorldMapMarkersV1.setExposed(var0);
      WorldMapStyleV1.setExposed(var0);
      WorldMapSymbolsV1.setExposed(var0);
      var0.setExposed(WorldMapEditorState.class);
      var0.setExposed(WorldMapSettings.class);
   }

   private void renderBuildingsWithoutFeatures() {
      if (this.m_bBuildingsWithoutFeatures) {
         Iterator var12 = this.m_buildingsWithoutFeatures.iterator();

         while(var12.hasNext()) {
            BuildingDef var13 = (BuildingDef)var12.next();
            this.debugRenderBuilding(var13, 1.0F, 0.0F, 0.0F, 1.0F);
         }

      } else {
         this.m_bBuildingsWithoutFeatures = true;
         this.m_buildingsWithoutFeatures.clear();
         IsoMetaGrid var1 = IsoWorld.instance.MetaGrid;

         for(int var2 = 0; var2 < var1.Buildings.size(); ++var2) {
            BuildingDef var3 = (BuildingDef)var1.Buildings.get(var2);
            boolean var4 = false;

            for(int var5 = 0; var5 < var3.rooms.size(); ++var5) {
               RoomDef var6 = (RoomDef)var3.rooms.get(var5);
               if (var6.level <= 0) {
                  ArrayList var7 = var6.getRects();

                  for(int var8 = 0; var8 < var7.size(); ++var8) {
                     RoomDef.RoomRect var9 = (RoomDef.RoomRect)var7.get(var8);
                     s_tempFeatures.clear();
                     Iterator var10 = this.m_worldMap.m_data.iterator();

                     while(var10.hasNext()) {
                        WorldMapData var11 = (WorldMapData)var10.next();
                        if (var11.isReady()) {
                           var11.hitTest((float)var9.x + (float)var9.w / 2.0F, (float)var9.y + (float)var9.h / 2.0F, s_tempFeatures);
                        }
                     }

                     if (!s_tempFeatures.isEmpty()) {
                        var4 = true;
                        break;
                     }
                  }

                  if (var4) {
                     break;
                  }
               }
            }

            if (!var4) {
               this.m_buildingsWithoutFeatures.add(var3);
            }
         }

      }
   }

   private void debugRenderBuilding(BuildingDef var1, float var2, float var3, float var4, float var5) {
      for(int var6 = 0; var6 < var1.rooms.size(); ++var6) {
         ArrayList var7 = ((RoomDef)var1.rooms.get(var6)).getRects();

         for(int var8 = 0; var8 < var7.size(); ++var8) {
            RoomDef.RoomRect var9 = (RoomDef.RoomRect)var7.get(var8);
            float var10 = this.m_APIv1.worldToUIX((float)var9.x, (float)var9.y);
            float var11 = this.m_APIv1.worldToUIY((float)var9.x, (float)var9.y);
            float var12 = this.m_APIv1.worldToUIX((float)var9.getX2(), (float)var9.getY2());
            float var13 = this.m_APIv1.worldToUIY((float)var9.getX2(), (float)var9.getY2());
            this.DrawTextureScaledColor((Texture)null, (double)var10, (double)var11, (double)(var12 - var10), (double)(var13 - var11), (double)var2, (double)var3, (double)var4, (double)var5);
         }
      }

   }
}
