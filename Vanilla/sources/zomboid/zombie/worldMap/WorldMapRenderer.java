package zombie.worldMap;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import zombie.characters.IsoPlayer;
import zombie.config.BooleanConfigOption;
import zombie.config.ConfigOption;
import zombie.config.DoubleConfigOption;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.VBO.GLVertexBufferObject;
import zombie.core.logger.ExceptionLogger;
import zombie.core.math.PZMath;
import zombie.core.opengl.PZGLUtil;
import zombie.core.opengl.VBOLines;
import zombie.core.skinnedmodel.ModelCamera;
import zombie.core.skinnedmodel.model.ModelSlotRenderData;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.core.textures.TextureID;
import zombie.iso.IsoCamera;
import zombie.iso.IsoMetaCell;
import zombie.iso.IsoMetaGrid;
import zombie.iso.IsoWorld;
import zombie.iso.Vector2;
import zombie.popman.ObjectPool;
import zombie.ui.UIManager;
import zombie.util.list.PZArrayUtil;
import zombie.vehicles.BaseVehicle;
import zombie.vehicles.UI3DScene;
import zombie.worldMap.styles.WorldMapStyle;
import zombie.worldMap.styles.WorldMapStyleLayer;
import zombie.worldMap.styles.WorldMapTextureStyleLayer;

public final class WorldMapRenderer {
   private WorldMap m_worldMap;
   private int m_x;
   private int m_y;
   private int m_width;
   private int m_height;
   private int m_zoom = 0;
   private float m_zoomF = 0.0F;
   private float m_displayZoomF = 0.0F;
   private float m_centerWorldX;
   private float m_centerWorldY;
   private float m_zoomUIX;
   private float m_zoomUIY;
   private float m_zoomWorldX;
   private float m_zoomWorldY;
   private final Matrix4f m_projection = new Matrix4f();
   private final Matrix4f m_modelView = new Matrix4f();
   private final Quaternionf m_modelViewChange = new Quaternionf();
   private long m_viewChangeTime;
   private static long VIEW_CHANGE_TIME = 350L;
   private boolean m_isometric;
   private boolean m_firstUpdate = false;
   private WorldMapVisited m_visited;
   private final WorldMapRenderer.Drawer[] m_drawer = new WorldMapRenderer.Drawer[3];
   private final WorldMapRenderer.CharacterModelCamera m_CharacterModelCamera = new WorldMapRenderer.CharacterModelCamera();
   private int m_dropShadowWidth = 12;
   public WorldMapStyle m_style = null;
   protected static final VBOLines m_vboLines = new VBOLines();
   protected static final VBOLinesUV m_vboLinesUV = new VBOLinesUV();
   private final int[] m_viewport = new int[]{0, 0, 0, 0};
   private static final ThreadLocal TL_Plane_pool = ThreadLocal.withInitial(UI3DScene.PlaneObjectPool::new);
   private static final ThreadLocal TL_Ray_pool = ThreadLocal.withInitial(UI3DScene.RayObjectPool::new);
   static final float SMALL_NUM = 1.0E-8F;
   private final ArrayList options = new ArrayList();
   private final WorldMapRenderer.WorldMapBooleanOption BlurUnvisited = new WorldMapRenderer.WorldMapBooleanOption("BlurUnvisited", true);
   private final WorldMapRenderer.WorldMapBooleanOption BuildingsWithoutFeatures = new WorldMapRenderer.WorldMapBooleanOption("BuildingsWithoutFeatures", false);
   private final WorldMapRenderer.WorldMapBooleanOption DebugInfo = new WorldMapRenderer.WorldMapBooleanOption("DebugInfo", false);
   private final WorldMapRenderer.WorldMapBooleanOption CellGrid = new WorldMapRenderer.WorldMapBooleanOption("CellGrid", false);
   private final WorldMapRenderer.WorldMapBooleanOption TileGrid = new WorldMapRenderer.WorldMapBooleanOption("TileGrid", false);
   private final WorldMapRenderer.WorldMapBooleanOption UnvisitedGrid = new WorldMapRenderer.WorldMapBooleanOption("UnvisitedGrid", true);
   private final WorldMapRenderer.WorldMapBooleanOption Features = new WorldMapRenderer.WorldMapBooleanOption("Features", true);
   private final WorldMapRenderer.WorldMapBooleanOption ForestZones = new WorldMapRenderer.WorldMapBooleanOption("ForestZones", false);
   private final WorldMapRenderer.WorldMapBooleanOption HideUnvisited = new WorldMapRenderer.WorldMapBooleanOption("HideUnvisited", false);
   private final WorldMapRenderer.WorldMapBooleanOption HitTest = new WorldMapRenderer.WorldMapBooleanOption("HitTest", false);
   private final WorldMapRenderer.WorldMapBooleanOption ImagePyramid = new WorldMapRenderer.WorldMapBooleanOption("ImagePyramid", false);
   private final WorldMapRenderer.WorldMapBooleanOption Isometric = new WorldMapRenderer.WorldMapBooleanOption("Isometric", true);
   private final WorldMapRenderer.WorldMapBooleanOption LineString = new WorldMapRenderer.WorldMapBooleanOption("LineString", true);
   private final WorldMapRenderer.WorldMapBooleanOption Players = new WorldMapRenderer.WorldMapBooleanOption("Players", false);
   private final WorldMapRenderer.WorldMapBooleanOption Symbols = new WorldMapRenderer.WorldMapBooleanOption("Symbols", true);
   private final WorldMapRenderer.WorldMapBooleanOption Wireframe = new WorldMapRenderer.WorldMapBooleanOption("Wireframe", false);
   private final WorldMapRenderer.WorldMapBooleanOption WorldBounds = new WorldMapRenderer.WorldMapBooleanOption("WorldBounds", true);
   private final WorldMapRenderer.WorldMapBooleanOption MiniMapSymbols = new WorldMapRenderer.WorldMapBooleanOption("MiniMapSymbols", false);
   private final WorldMapRenderer.WorldMapBooleanOption VisibleCells = new WorldMapRenderer.WorldMapBooleanOption("VisibleCells", false);

   public WorldMapRenderer() {
      PZArrayUtil.arrayPopulate(this.m_drawer, WorldMapRenderer.Drawer::new);
   }

   public int getAbsoluteX() {
      return this.m_x;
   }

   public int getAbsoluteY() {
      return this.m_y;
   }

   public int getWidth() {
      return this.m_width;
   }

   public int getHeight() {
      return this.m_height;
   }

   private void calcMatrices(float var1, float var2, float var3, Matrix4f var4, Matrix4f var5) {
      int var6 = this.getWidth();
      int var7 = this.getHeight();
      var4.setOrtho((float)(-var6) / 2.0F, (float)var6 / 2.0F, (float)var7 / 2.0F, (float)(-var7) / 2.0F, -2000.0F, 2000.0F);
      var5.identity();
      if (this.Isometric.getValue()) {
         var5.rotateXYZ(1.0471976F, 0.0F, 0.7853982F);
      }

   }

   public Vector3f uiToScene(float var1, float var2, Matrix4f var3, Matrix4f var4, Vector3f var5) {
      UI3DScene.Plane var6 = allocPlane();
      var6.point.set(0.0F);
      var6.normal.set(0.0F, 0.0F, 1.0F);
      UI3DScene.Ray var7 = this.getCameraRay(var1, (float)this.getHeight() - var2, var3, var4, allocRay());
      if (this.intersect_ray_plane(var6, var7, var5) != 1) {
         var5.set(0.0F);
      }

      releasePlane(var6);
      releaseRay(var7);
      return var5;
   }

   public Vector3f sceneToUI(float var1, float var2, float var3, Matrix4f var4, Matrix4f var5, Vector3f var6) {
      Matrix4f var7 = allocMatrix4f();
      var7.set((Matrix4fc)var4);
      var7.mul((Matrix4fc)var5);
      this.m_viewport[0] = 0;
      this.m_viewport[1] = 0;
      this.m_viewport[2] = this.getWidth();
      this.m_viewport[3] = this.getHeight();
      var7.project(var1, var2, var3, this.m_viewport, var6);
      releaseMatrix4f(var7);
      return var6;
   }

   public float uiToWorldX(float var1, float var2, float var3, float var4, float var5) {
      Matrix4f var6 = allocMatrix4f();
      Matrix4f var7 = allocMatrix4f();
      this.calcMatrices(var4, var5, var3, var6, var7);
      float var8 = this.uiToWorldX(var1, var2, var3, var4, var5, var6, var7);
      releaseMatrix4f(var6);
      releaseMatrix4f(var7);
      return var8;
   }

   public float uiToWorldY(float var1, float var2, float var3, float var4, float var5) {
      Matrix4f var6 = allocMatrix4f();
      Matrix4f var7 = allocMatrix4f();
      this.calcMatrices(var4, var5, var3, var6, var7);
      float var8 = this.uiToWorldY(var1, var2, var3, var4, var5, var6, var7);
      releaseMatrix4f(var6);
      releaseMatrix4f(var7);
      return var8;
   }

   public float uiToWorldX(float var1, float var2, float var3, float var4, float var5, Matrix4f var6, Matrix4f var7) {
      Vector3f var8 = this.uiToScene(var1, var2, var6, var7, allocVector3f());
      float var9 = this.getWorldScale(var3);
      var8.mul(1.0F / var9);
      float var10 = var8.x() + var4;
      releaseVector3f(var8);
      return var10;
   }

   public float uiToWorldY(float var1, float var2, float var3, float var4, float var5, Matrix4f var6, Matrix4f var7) {
      Vector3f var8 = this.uiToScene(var1, var2, var6, var7, allocVector3f());
      float var9 = this.getWorldScale(var3);
      var8.mul(1.0F / var9);
      float var10 = var8.y() + var5;
      releaseVector3f(var8);
      return var10;
   }

   public float worldToUIX(float var1, float var2, float var3, float var4, float var5, Matrix4f var6, Matrix4f var7) {
      float var8 = this.getWorldScale(var3);
      Vector3f var9 = this.sceneToUI((var1 - var4) * var8, (var2 - var5) * var8, 0.0F, var6, var7, allocVector3f());
      float var10 = var9.x();
      releaseVector3f(var9);
      return var10;
   }

   public float worldToUIY(float var1, float var2, float var3, float var4, float var5, Matrix4f var6, Matrix4f var7) {
      float var8 = this.getWorldScale(var3);
      Vector3f var9 = this.sceneToUI((var1 - var4) * var8, (var2 - var5) * var8, 0.0F, var6, var7, allocVector3f());
      float var10 = (float)this.getHeight() - var9.y();
      releaseVector3f(var9);
      return var10;
   }

   public float worldOriginUIX(float var1, float var2) {
      return this.worldToUIX(0.0F, 0.0F, var1, var2, this.m_centerWorldY, this.m_projection, this.m_modelView);
   }

   public float worldOriginUIY(float var1, float var2) {
      return this.worldToUIY(0.0F, 0.0F, var1, this.m_centerWorldX, var2, this.m_projection, this.m_modelView);
   }

   public int getZoom() {
      return this.m_zoom;
   }

   public float getZoomF() {
      return this.m_zoomF;
   }

   public float getDisplayZoomF() {
      return this.m_displayZoomF;
   }

   public float zoomMult() {
      return this.zoomMult(this.m_zoomF);
   }

   public float zoomMult(float var1) {
      return (float)Math.pow(2.0D, (double)var1);
   }

   public float getWorldScale(float var1) {
      int var2 = this.getHeight();
      double var3 = MapProjection.metersPerPixelAtZoom((double)var1, (double)var2);
      return (float)(1.0D / var3);
   }

   public void zoomAt(int var1, int var2, int var3) {
      float var4 = this.uiToWorldX((float)var1, (float)var2, this.m_displayZoomF, this.m_centerWorldX, this.m_centerWorldY);
      float var5 = this.uiToWorldY((float)var1, (float)var2, this.m_displayZoomF, this.m_centerWorldX, this.m_centerWorldY);
      this.m_zoomF = PZMath.clamp(this.m_zoomF + (float)var3 / 2.0F, this.getBaseZoom(), 24.0F);
      this.m_zoom = (int)this.m_zoomF;
      this.m_zoomWorldX = var4;
      this.m_zoomWorldY = var5;
      this.m_zoomUIX = (float)var1;
      this.m_zoomUIY = (float)var2;
   }

   public float getCenterWorldX() {
      return this.m_centerWorldX;
   }

   public float getCenterWorldY() {
      return this.m_centerWorldY;
   }

   public void centerOn(float var1, float var2) {
      this.m_centerWorldX = var1;
      this.m_centerWorldY = var2;
      if (this.m_displayZoomF != this.m_zoomF) {
         this.m_zoomWorldX = var1;
         this.m_zoomWorldY = var2;
         this.m_zoomUIX = (float)this.m_width / 2.0F;
         this.m_zoomUIY = (float)this.m_height / 2.0F;
      }

   }

   public void moveView(int var1, int var2) {
      this.centerOn(this.m_centerWorldX + (float)var1, this.m_centerWorldY + (float)var2);
   }

   public double log2(double var1) {
      return Math.log(var1) / Math.log(2.0D);
   }

   public float getBaseZoom() {
      double var1 = MapProjection.zoomAtMetersPerPixel((double)this.m_worldMap.getHeightInSquares() / (double)this.getHeight(), (double)this.getHeight());
      if ((float)this.m_worldMap.getWidthInSquares() * this.getWorldScale((float)var1) > (float)this.getWidth()) {
         var1 = MapProjection.zoomAtMetersPerPixel((double)this.m_worldMap.getWidthInSquares() / (double)this.getWidth(), (double)this.getHeight());
      }

      var1 = (double)((int)(var1 * 2.0D)) / 2.0D;
      return (float)var1;
   }

   public void setZoom(float var1) {
      this.m_zoomF = PZMath.clamp(var1, this.getBaseZoom(), 24.0F);
      this.m_zoom = (int)this.m_zoomF;
      this.m_displayZoomF = this.m_zoomF;
   }

   public void resetView() {
      this.m_zoomF = this.getBaseZoom();
      this.m_zoom = (int)this.m_zoomF;
      this.m_centerWorldX = (float)this.m_worldMap.getMinXInSquares() + (float)this.m_worldMap.getWidthInSquares() / 2.0F;
      this.m_centerWorldY = (float)this.m_worldMap.getMinYInSquares() + (float)this.m_worldMap.getHeightInSquares() / 2.0F;
      this.m_zoomWorldX = this.m_centerWorldX;
      this.m_zoomWorldY = this.m_centerWorldY;
      this.m_zoomUIX = (float)this.getWidth() / 2.0F;
      this.m_zoomUIY = (float)this.getHeight() / 2.0F;
   }

   public Matrix4f getProjectionMatrix() {
      return this.m_projection;
   }

   public Matrix4f getModelViewMatrix() {
      return this.m_modelView;
   }

   public void setMap(WorldMap var1, int var2, int var3, int var4, int var5) {
      this.m_worldMap = var1;
      this.m_x = var2;
      this.m_y = var3;
      this.m_width = var4;
      this.m_height = var5;
   }

   public WorldMap getWorldMap() {
      return this.m_worldMap;
   }

   public void setVisited(WorldMapVisited var1) {
      this.m_visited = var1;
   }

   public void updateView() {
      float var3;
      if (this.m_displayZoomF != this.m_zoomF) {
         float var1 = (float)(UIManager.getMillisSinceLastRender() / 750.0D);
         float var2 = Math.abs(this.m_zoomF - this.m_displayZoomF);
         var3 = var2 > 0.25F ? var2 / 0.25F : 1.0F;
         if (this.m_displayZoomF < this.m_zoomF) {
            this.m_displayZoomF = PZMath.min(this.m_displayZoomF + var1 * var3, this.m_zoomF);
         } else if (this.m_displayZoomF > this.m_zoomF) {
            this.m_displayZoomF = PZMath.max(this.m_displayZoomF - var1 * var3, this.m_zoomF);
         }

         float var4 = this.uiToWorldX(this.m_zoomUIX, this.m_zoomUIY, this.m_displayZoomF, 0.0F, 0.0F);
         float var5 = this.uiToWorldY(this.m_zoomUIX, this.m_zoomUIY, this.m_displayZoomF, 0.0F, 0.0F);
         this.m_centerWorldX = this.m_zoomWorldX - var4;
         this.m_centerWorldY = this.m_zoomWorldY - var5;
      }

      if (!this.m_firstUpdate) {
         this.m_firstUpdate = true;
         this.m_isometric = this.Isometric.getValue();
      }

      long var6;
      if (this.m_isometric != this.Isometric.getValue()) {
         this.m_isometric = this.Isometric.getValue();
         var6 = System.currentTimeMillis();
         if (this.m_viewChangeTime + VIEW_CHANGE_TIME < var6) {
            this.m_modelViewChange.setFromUnnormalized((Matrix4fc)this.m_modelView);
         }

         this.m_viewChangeTime = var6;
      }

      this.calcMatrices(this.m_centerWorldX, this.m_centerWorldY, this.m_displayZoomF, this.m_projection, this.m_modelView);
      var6 = System.currentTimeMillis();
      if (this.m_viewChangeTime + VIEW_CHANGE_TIME > var6) {
         var3 = (float)(this.m_viewChangeTime + VIEW_CHANGE_TIME - var6) / (float)VIEW_CHANGE_TIME;
         Quaternionf var7 = allocQuaternionf().setFromUnnormalized((Matrix4fc)this.m_modelView);
         this.m_modelView.set((Quaternionfc)this.m_modelViewChange.slerp(var7, 1.0F - var3));
         releaseQuaternionf(var7);
      }

   }

   public void render(UIWorldMap var1) {
      this.m_style = var1.getAPI().getStyle();
      int var2 = SpriteRenderer.instance.getMainStateIndex();
      this.m_drawer[var2].init(this, var1);
      SpriteRenderer.instance.drawGeneric(this.m_drawer[var2]);
   }

   public void setDropShadowWidth(int var1) {
      this.m_dropShadowWidth = var1;
   }

   private static Matrix4f allocMatrix4f() {
      return (Matrix4f)((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).alloc();
   }

   private static void releaseMatrix4f(Matrix4f var0) {
      ((BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get()).release(var0);
   }

   private static Quaternionf allocQuaternionf() {
      return (Quaternionf)((BaseVehicle.QuaternionfObjectPool)BaseVehicle.TL_quaternionf_pool.get()).alloc();
   }

   private static void releaseQuaternionf(Quaternionf var0) {
      ((BaseVehicle.QuaternionfObjectPool)BaseVehicle.TL_quaternionf_pool.get()).release(var0);
   }

   private static UI3DScene.Ray allocRay() {
      return (UI3DScene.Ray)((ObjectPool)TL_Ray_pool.get()).alloc();
   }

   private static void releaseRay(UI3DScene.Ray var0) {
      ((ObjectPool)TL_Ray_pool.get()).release((Object)var0);
   }

   private static UI3DScene.Plane allocPlane() {
      return (UI3DScene.Plane)((ObjectPool)TL_Plane_pool.get()).alloc();
   }

   private static void releasePlane(UI3DScene.Plane var0) {
      ((ObjectPool)TL_Plane_pool.get()).release((Object)var0);
   }

   private static Vector2 allocVector2() {
      return (Vector2)((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).alloc();
   }

   private static void releaseVector2(Vector2 var0) {
      ((BaseVehicle.Vector2ObjectPool)BaseVehicle.TL_vector2_pool.get()).release(var0);
   }

   private static Vector3f allocVector3f() {
      return (Vector3f)((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).alloc();
   }

   private static void releaseVector3f(Vector3f var0) {
      ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(var0);
   }

   UI3DScene.Ray getCameraRay(float var1, float var2, UI3DScene.Ray var3) {
      return this.getCameraRay(var1, var2, this.m_projection, this.m_modelView, var3);
   }

   UI3DScene.Ray getCameraRay(float var1, float var2, Matrix4f var3, Matrix4f var4, UI3DScene.Ray var5) {
      Matrix4f var6 = allocMatrix4f();
      var6.set((Matrix4fc)var3);
      var6.mul((Matrix4fc)var4);
      var6.invert();
      this.m_viewport[0] = 0;
      this.m_viewport[1] = 0;
      this.m_viewport[2] = this.getWidth();
      this.m_viewport[3] = this.getHeight();
      Vector3f var7 = var6.unprojectInv(var1, var2, 0.0F, this.m_viewport, allocVector3f());
      Vector3f var8 = var6.unprojectInv(var1, var2, 1.0F, this.m_viewport, allocVector3f());
      var5.origin.set((Vector3fc)var7);
      var5.direction.set((Vector3fc)var8.sub(var7).normalize());
      releaseVector3f(var8);
      releaseVector3f(var7);
      releaseMatrix4f(var6);
      return var5;
   }

   int intersect_ray_plane(UI3DScene.Plane var1, UI3DScene.Ray var2, Vector3f var3) {
      Vector3f var4 = allocVector3f().set((Vector3fc)var2.direction).mul(10000.0F);
      Vector3f var5 = allocVector3f().set((Vector3fc)var2.origin).sub(var1.point);

      byte var9;
      try {
         float var6 = var1.normal.dot(var4);
         float var7 = -var1.normal.dot(var5);
         if (Math.abs(var6) < 1.0E-8F) {
            byte var13;
            if (var7 == 0.0F) {
               var13 = 2;
               return var13;
            }

            var13 = 0;
            return var13;
         }

         float var8 = var7 / var6;
         if (var8 < 0.0F || var8 > 1.0F) {
            var9 = 0;
            return var9;
         }

         var3.set((Vector3fc)var2.origin).add(var4.mul(var8));
         var9 = 1;
      } finally {
         releaseVector3f(var4);
         releaseVector3f(var5);
      }

      return var9;
   }

   public ConfigOption getOptionByName(String var1) {
      for(int var2 = 0; var2 < this.options.size(); ++var2) {
         ConfigOption var3 = (ConfigOption)this.options.get(var2);
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      return null;
   }

   public int getOptionCount() {
      return this.options.size();
   }

   public ConfigOption getOptionByIndex(int var1) {
      return (ConfigOption)this.options.get(var1);
   }

   public void setBoolean(String var1, boolean var2) {
      ConfigOption var3 = this.getOptionByName(var1);
      if (var3 instanceof BooleanConfigOption) {
         ((BooleanConfigOption)var3).setValue(var2);
      }

   }

   public boolean getBoolean(String var1) {
      ConfigOption var2 = this.getOptionByName(var1);
      return var2 instanceof BooleanConfigOption ? ((BooleanConfigOption)var2).getValue() : false;
   }

   public void setDouble(String var1, double var2) {
      ConfigOption var4 = this.getOptionByName(var1);
      if (var4 instanceof DoubleConfigOption) {
         ((DoubleConfigOption)var4).setValue(var2);
      }

   }

   public double getDouble(String var1, double var2) {
      ConfigOption var4 = this.getOptionByName(var1);
      return var4 instanceof DoubleConfigOption ? ((DoubleConfigOption)var4).getValue() : var2;
   }

   public static final class Drawer extends TextureDraw.GenericDrawer {
      WorldMapRenderer m_renderer;
      final WorldMapStyle m_style = new WorldMapStyle();
      WorldMap m_worldMap;
      int m_x;
      int m_y;
      int m_width;
      int m_height;
      float m_centerWorldX;
      float m_centerWorldY;
      int m_zoom = 0;
      public float m_zoomF = 0.0F;
      float m_worldScale;
      float m_renderOriginX;
      float m_renderOriginY;
      float m_renderCellX;
      float m_renderCellY;
      private final Matrix4f m_projection = new Matrix4f();
      private final Matrix4f m_modelView = new Matrix4f();
      private final WorldMapRenderer.PlayerRenderData[] m_playerRenderData = new WorldMapRenderer.PlayerRenderData[4];
      final WorldMapStyleLayer.FilterArgs m_filterArgs = new WorldMapStyleLayer.FilterArgs();
      final WorldMapStyleLayer.RenderArgs m_renderArgs = new WorldMapStyleLayer.RenderArgs();
      final ArrayList m_renderLayers = new ArrayList();
      final ArrayList m_features = new ArrayList();
      final ArrayList m_zones = new ArrayList();
      final HashSet m_zoneSet = new HashSet();
      WorldMapStyleLayer.RGBAf m_fill;
      int m_triangulationsThisFrame = 0;
      float[] m_floatArray;
      final Vector2f m_vector2f = new Vector2f();
      final TIntArrayList m_rasterizeXY = new TIntArrayList();
      final TIntSet m_rasterizeSet = new TIntHashSet();
      float m_rasterizeMinTileX;
      float m_rasterizeMinTileY;
      float m_rasterizeMaxTileX;
      float m_rasterizeMaxTileY;
      final Rasterize m_rasterize = new Rasterize();
      int[] m_rasterizeXY_ints;
      int m_rasterizeMult = 1;

      Drawer() {
         PZArrayUtil.arrayPopulate(this.m_playerRenderData, WorldMapRenderer.PlayerRenderData::new);
      }

      void init(WorldMapRenderer var1, UIWorldMap var2) {
         this.m_renderer = var1;
         this.m_style.copyFrom(this.m_renderer.m_style);
         this.m_worldMap = var1.m_worldMap;
         this.m_x = var1.m_x;
         this.m_y = var1.m_y;
         this.m_width = var1.m_width;
         this.m_height = var1.m_height;
         this.m_centerWorldX = var1.m_centerWorldX;
         this.m_centerWorldY = var1.m_centerWorldY;
         this.m_zoomF = var1.m_displayZoomF;
         this.m_zoom = (int)this.m_zoomF;
         this.m_worldScale = this.getWorldScale();
         this.m_renderOriginX = ((float)this.m_renderer.m_worldMap.getMinXInSquares() - this.m_centerWorldX) * this.m_worldScale;
         this.m_renderOriginY = ((float)this.m_renderer.m_worldMap.getMinYInSquares() - this.m_centerWorldY) * this.m_worldScale;
         this.m_projection.set((Matrix4fc)var1.m_projection);
         this.m_modelView.set((Matrix4fc)var1.m_modelView);
         this.m_fill = var2.m_color;
         this.m_triangulationsThisFrame = 0;
         if (this.m_renderer.m_visited != null) {
            this.m_renderer.m_visited.renderMain();
         }

         int var3;
         for(var3 = 0; var3 < 4; ++var3) {
            this.m_playerRenderData[var3].m_modelSlotRenderData = null;
         }

         if (this.m_renderer.Players.getValue() && this.m_zoomF >= 20.0F) {
            for(var3 = 0; var3 < 4; ++var3) {
               IsoPlayer var4 = IsoPlayer.players[var3];
               if (var4 != null && !var4.isDead() && var4.legsSprite.hasActiveModel()) {
                  float var5 = var4.x;
                  float var6 = var4.y;
                  if (var4.getVehicle() != null) {
                     var5 = var4.getVehicle().getX();
                     var6 = var4.getVehicle().getY();
                  }

                  float var7 = this.m_renderer.worldToUIX(var5, var6, this.m_zoomF, this.m_centerWorldX, this.m_centerWorldY, this.m_projection, this.m_modelView);
                  float var8 = this.m_renderer.worldToUIY(var5, var6, this.m_zoomF, this.m_centerWorldX, this.m_centerWorldY, this.m_projection, this.m_modelView);
                  if (!(var7 < -100.0F) && !(var7 > (float)(this.m_width + 100)) && !(var8 < -100.0F) && !(var8 > (float)(this.m_height + 100))) {
                     this.m_playerRenderData[var3].m_angle = var4.getVehicle() == null ? var4.getAnimationPlayer().getAngle() : 4.712389F;
                     this.m_playerRenderData[var3].m_x = var5 - this.m_centerWorldX;
                     this.m_playerRenderData[var3].m_y = var6 - this.m_centerWorldY;
                     var4.legsSprite.modelSlot.model.updateLights();
                     int var9 = IsoCamera.frameState.playerIndex;
                     IsoCamera.frameState.playerIndex = var3;
                     this.m_playerRenderData[var3].m_modelSlotRenderData = ModelSlotRenderData.alloc().init(var4.legsSprite.modelSlot);
                     this.m_playerRenderData[var3].m_modelSlotRenderData.centerOfMassY = 0.0F;
                     IsoCamera.frameState.playerIndex = var9;
                     ++var4.legsSprite.modelSlot.renderRefCount;
                  }
               }
            }
         }

      }

      public int getAbsoluteX() {
         return this.m_x;
      }

      public int getAbsoluteY() {
         return this.m_y;
      }

      public int getWidth() {
         return this.m_width;
      }

      public int getHeight() {
         return this.m_height;
      }

      public float getWorldScale() {
         return this.m_renderer.getWorldScale(this.m_zoomF);
      }

      public float uiToWorldX(float var1, float var2) {
         return this.m_renderer.uiToWorldX(var1, var2, this.m_zoomF, this.m_centerWorldX, this.m_centerWorldY, this.m_projection, this.m_modelView);
      }

      public float uiToWorldY(float var1, float var2) {
         return this.m_renderer.uiToWorldY(var1, var2, this.m_zoomF, this.m_centerWorldX, this.m_centerWorldY, this.m_projection, this.m_modelView);
      }

      public float worldOriginUIX(float var1) {
         return this.m_renderer.worldOriginUIX(this.m_zoomF, var1);
      }

      public float worldOriginUIY(float var1) {
         return this.m_renderer.worldOriginUIY(this.m_zoomF, var1);
      }

      private void renderCellFeatures() {
         for(int var1 = 0; var1 < this.m_rasterizeXY.size() - 1; var1 += 2) {
            int var2 = this.m_rasterizeXY_ints[var1];
            int var3 = this.m_rasterizeXY_ints[var1 + 1];
            if (this.m_renderer.m_visited == null || this.m_renderer.m_visited.isCellVisible(var2, var3)) {
               this.m_features.clear();

               int var4;
               for(var4 = 0; var4 < this.m_worldMap.m_data.size(); ++var4) {
                  WorldMapData var5 = (WorldMapData)this.m_worldMap.m_data.get(var4);
                  if (var5.isReady()) {
                     WorldMapCell var6 = var5.getCell(var2, var3);
                     if (var6 != null && !var6.m_features.isEmpty()) {
                        this.m_features.addAll(var6.m_features);
                        if (this.m_worldMap.isLastDataInDirectory(var5)) {
                           break;
                        }
                     }
                  }
               }

               if (this.m_features.isEmpty()) {
                  this.m_renderArgs.renderer = this.m_renderer;
                  this.m_renderArgs.drawer = this;
                  this.m_renderArgs.cellX = var2;
                  this.m_renderArgs.cellY = var3;
                  this.m_renderCellX = this.m_renderOriginX + (float)(var2 * 300 - this.m_worldMap.getMinXInSquares()) * this.m_worldScale;
                  this.m_renderCellY = this.m_renderOriginY + (float)(var3 * 300 - this.m_worldMap.getMinYInSquares()) * this.m_worldScale;

                  for(var4 = 0; var4 < this.m_style.m_layers.size(); ++var4) {
                     WorldMapStyleLayer var7 = (WorldMapStyleLayer)this.m_style.m_layers.get(var4);
                     if (var7 instanceof WorldMapTextureStyleLayer) {
                        var7.renderCell(this.m_renderArgs);
                     }
                  }
               } else {
                  this.renderCell(var2, var3, this.m_features);
               }
            }
         }

      }

      private void renderCell(int var1, int var2, ArrayList var3) {
         this.m_renderCellX = this.m_renderOriginX + (float)(var1 * 300 - this.m_worldMap.getMinXInSquares()) * this.m_worldScale;
         this.m_renderCellY = this.m_renderOriginY + (float)(var2 * 300 - this.m_worldMap.getMinYInSquares()) * this.m_worldScale;
         WorldMapRenderLayer.s_pool.release((List)this.m_renderLayers);
         this.m_renderLayers.clear();
         this.m_filterArgs.renderer = this.m_renderer;
         this.filterFeatures(var3, this.m_filterArgs, this.m_renderLayers);
         this.m_renderArgs.renderer = this.m_renderer;
         this.m_renderArgs.drawer = this;
         this.m_renderArgs.cellX = var1;
         this.m_renderArgs.cellY = var2;

         for(int var4 = 0; var4 < this.m_renderLayers.size(); ++var4) {
            WorldMapRenderLayer var5 = (WorldMapRenderLayer)this.m_renderLayers.get(var4);
            var5.m_styleLayer.renderCell(this.m_renderArgs);

            for(int var6 = 0; var6 < var5.m_features.size(); ++var6) {
               WorldMapFeature var7 = (WorldMapFeature)var5.m_features.get(var6);
               var5.m_styleLayer.render(var7, this.m_renderArgs);
            }
         }

      }

      void filterFeatures(ArrayList var1, WorldMapStyleLayer.FilterArgs var2, ArrayList var3) {
         for(int var4 = 0; var4 < this.m_style.m_layers.size(); ++var4) {
            WorldMapStyleLayer var5 = (WorldMapStyleLayer)this.m_style.m_layers.get(var4);
            if (!(var5.m_minZoom > this.m_zoomF)) {
               if (var5.m_id.equals("mylayer")) {
                  boolean var6 = true;
               }

               WorldMapRenderLayer var9 = null;
               if (var5 instanceof WorldMapTextureStyleLayer) {
                  var9 = (WorldMapRenderLayer)WorldMapRenderLayer.s_pool.alloc();
                  var9.m_styleLayer = var5;
                  var9.m_features.clear();
                  var3.add(var9);
               } else {
                  for(int var7 = 0; var7 < var1.size(); ++var7) {
                     WorldMapFeature var8 = (WorldMapFeature)var1.get(var7);
                     if (var5.filter(var8, var2)) {
                        if (var9 == null) {
                           var9 = (WorldMapRenderLayer)WorldMapRenderLayer.s_pool.alloc();
                           var9.m_styleLayer = var5;
                           var9.m_features.clear();
                           var3.add(var9);
                        }

                        var9.m_features.add(var8);
                     }
                  }
               }
            }
         }

      }

      void renderCellGrid(int var1, int var2, int var3, int var4) {
         float var5 = this.m_renderOriginX + (float)(var1 * 300 - this.m_worldMap.getMinXInSquares()) * this.m_worldScale;
         float var6 = this.m_renderOriginY + (float)(var2 * 300 - this.m_worldMap.getMinYInSquares()) * this.m_worldScale;
         float var7 = var5 + (float)((var3 - var1 + 1) * 300) * this.m_worldScale;
         float var8 = var6 + (float)((var4 - var2 + 1) * 300) * this.m_worldScale;
         WorldMapRenderer.m_vboLines.setMode(1);
         WorldMapRenderer.m_vboLines.setLineWidth(1.0F);

         int var9;
         for(var9 = var1; var9 <= var3 + 1; ++var9) {
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + (float)(var9 * 300 - this.m_worldMap.getMinXInSquares()) * this.m_worldScale, var6, 0.0F, this.m_renderOriginX + (float)(var9 * 300 - this.m_worldMap.getMinXInSquares()) * this.m_worldScale, var8, 0.0F, 0.25F, 0.25F, 0.25F, 1.0F);
         }

         for(var9 = var2; var9 <= var4 + 1; ++var9) {
            WorldMapRenderer.m_vboLines.addLine(var5, this.m_renderOriginY + (float)(var9 * 300 - this.m_worldMap.getMinYInSquares()) * this.m_worldScale, 0.0F, var7, this.m_renderOriginY + (float)(var9 * 300 - this.m_worldMap.getMinYInSquares()) * this.m_worldScale, 0.0F, 0.25F, 0.25F, 0.25F, 1.0F);
         }

         WorldMapRenderer.m_vboLines.flush();
      }

      void renderPlayers() {
         boolean var1 = true;

         for(int var2 = 0; var2 < this.m_playerRenderData.length; ++var2) {
            WorldMapRenderer.PlayerRenderData var3 = this.m_playerRenderData[var2];
            if (var3.m_modelSlotRenderData != null) {
               if (var1) {
                  GL11.glClear(256);
                  var1 = false;
               }

               this.m_renderer.m_CharacterModelCamera.m_worldScale = this.m_worldScale;
               this.m_renderer.m_CharacterModelCamera.m_bUseWorldIso = true;
               this.m_renderer.m_CharacterModelCamera.m_angle = var3.m_angle;
               this.m_renderer.m_CharacterModelCamera.m_playerX = var3.m_x;
               this.m_renderer.m_CharacterModelCamera.m_playerY = var3.m_y;
               this.m_renderer.m_CharacterModelCamera.m_bVehicle = var3.m_modelSlotRenderData.bInVehicle;
               ModelCamera.instance = this.m_renderer.m_CharacterModelCamera;
               var3.m_modelSlotRenderData.render();
            }
         }

         if (UIManager.useUIFBO) {
            GL14.glBlendFuncSeparate(770, 771, 1, 771);
         }

      }

      public void drawLineStringXXX(WorldMapStyleLayer.RenderArgs var1, WorldMapFeature var2, WorldMapStyleLayer.RGBAf var3, float var4) {
         float var5 = this.m_renderCellX;
         float var6 = this.m_renderCellY;
         float var7 = this.m_worldScale;
         float var8 = var3.r;
         float var9 = var3.g;
         float var10 = var3.b;
         float var11 = var3.a;

         for(int var12 = 0; var12 < var2.m_geometries.size(); ++var12) {
            WorldMapGeometry var13 = (WorldMapGeometry)var2.m_geometries.get(var12);
            switch(var13.m_type) {
            case LineString:
               WorldMapRenderer.m_vboLines.setMode(1);
               WorldMapRenderer.m_vboLines.setLineWidth(var4);

               for(int var14 = 0; var14 < var13.m_points.size(); ++var14) {
                  WorldMapPoints var15 = (WorldMapPoints)var13.m_points.get(var14);

                  for(int var16 = 0; var16 < var15.numPoints() - 1; ++var16) {
                     float var17 = (float)var15.getX(var16);
                     float var18 = (float)var15.getY(var16);
                     float var19 = (float)var15.getX(var16 + 1);
                     float var20 = (float)var15.getY(var16 + 1);
                     WorldMapRenderer.m_vboLines.addLine(var5 + var17 * var7, var6 + var18 * var7, 0.0F, var5 + var19 * var7, var6 + var20 * var7, 0.0F, var8, var9, var10, var11);
                  }
               }
            }
         }

      }

      public void drawLineStringYYY(WorldMapStyleLayer.RenderArgs var1, WorldMapFeature var2, WorldMapStyleLayer.RGBAf var3, float var4) {
         float var5 = this.m_renderCellX;
         float var6 = this.m_renderCellY;
         float var7 = this.m_worldScale;
         float var8 = var3.r;
         float var9 = var3.g;
         float var10 = var3.b;
         float var11 = var3.a;

         for(int var12 = 0; var12 < var2.m_geometries.size(); ++var12) {
            WorldMapGeometry var13 = (WorldMapGeometry)var2.m_geometries.get(var12);
            switch(var13.m_type) {
            case LineString:
               StrokeGeometry.Point[] var14 = new StrokeGeometry.Point[var13.m_points.size()];
               WorldMapPoints var15 = (WorldMapPoints)var13.m_points.get(0);

               for(int var16 = 0; var16 < var15.numPoints(); ++var16) {
                  float var17 = (float)var15.getX(var16);
                  float var18 = (float)var15.getY(var16);
                  var14[var16] = StrokeGeometry.newPoint((double)(var5 + var17 * var7), (double)(var6 + var18 * var7));
               }

               StrokeGeometry.Attrs var21 = new StrokeGeometry.Attrs();
               var21.join = "miter";
               var21.width = var4;
               ArrayList var22 = StrokeGeometry.getStrokeGeometry(var14, var21);
               if (var22 != null) {
                  WorldMapRenderer.m_vboLines.setMode(4);

                  for(int var23 = 0; var23 < var22.size(); ++var23) {
                     float var19 = (float)((StrokeGeometry.Point)var22.get(var23)).x;
                     float var20 = (float)((StrokeGeometry.Point)var22.get(var23)).y;
                     WorldMapRenderer.m_vboLines.addElement(var19, var20, 0.0F, var8, var9, var10, var11);
                  }

                  StrokeGeometry.release(var22);
               }
            }
         }

      }

      public void drawLineString(WorldMapStyleLayer.RenderArgs var1, WorldMapFeature var2, WorldMapStyleLayer.RGBAf var3, float var4) {
         if (this.m_renderer.LineString.getValue()) {
            float var5 = this.m_renderCellX;
            float var6 = this.m_renderCellY;
            float var7 = this.m_worldScale;
            float var8 = var3.r;
            float var9 = var3.g;
            float var10 = var3.b;
            float var11 = var3.a;
            WorldMapRenderer.m_vboLines.flush();
            WorldMapRenderer.m_vboLinesUV.flush();

            for(int var12 = 0; var12 < var2.m_geometries.size(); ++var12) {
               WorldMapGeometry var13 = (WorldMapGeometry)var2.m_geometries.get(var12);
               switch(var13.m_type) {
               case LineString:
                  WorldMapPoints var14 = (WorldMapPoints)var13.m_points.get(0);
                  if (this.m_floatArray == null || this.m_floatArray.length < var14.numPoints() * 2) {
                     this.m_floatArray = new float[var14.numPoints() * 2];
                  }

                  for(int var15 = 0; var15 < var14.numPoints(); ++var15) {
                     float var16 = (float)var14.getX(var15);
                     float var17 = (float)var14.getY(var15);
                     this.m_floatArray[var15 * 2] = var5 + var16 * var7;
                     this.m_floatArray[var15 * 2 + 1] = var6 + var17 * var7;
                  }

                  GL13.glActiveTexture(33984);
                  GL11.glDisable(3553);
                  GL11.glEnable(3042);
               }
            }

         }
      }

      public void drawLineStringTexture(WorldMapStyleLayer.RenderArgs var1, WorldMapFeature var2, WorldMapStyleLayer.RGBAf var3, float var4, Texture var5) {
         float var6 = this.m_renderCellX;
         float var7 = this.m_renderCellY;
         float var8 = this.m_worldScale;
         if (var5 != null && var5.isReady()) {
            if (var5.getID() == -1) {
               var5.bind();
            }

            for(int var9 = 0; var9 < var2.m_geometries.size(); ++var9) {
               WorldMapGeometry var10 = (WorldMapGeometry)var2.m_geometries.get(var9);
               if (var10.m_type == WorldMapGeometry.Type.LineString) {
                  WorldMapRenderer.m_vboLinesUV.setMode(7);
                  WorldMapRenderer.m_vboLinesUV.startRun(var5.getTextureId());
                  float var11 = var4;
                  WorldMapPoints var12 = (WorldMapPoints)var10.m_points.get(0);

                  for(int var13 = 0; var13 < var12.numPoints() - 1; ++var13) {
                     float var14 = var6 + (float)var12.getX(var13) * var8;
                     float var15 = var7 + (float)var12.getY(var13) * var8;
                     float var16 = var6 + (float)var12.getX(var13 + 1) * var8;
                     float var17 = var7 + (float)var12.getY(var13 + 1) * var8;
                     float var18 = var17 - var15;
                     float var19 = -(var16 - var14);
                     Vector2f var20 = this.m_vector2f.set(var18, var19);
                     var20.normalize();
                     float var21 = var14 + var20.x * var11 / 2.0F;
                     float var22 = var15 + var20.y * var11 / 2.0F;
                     float var23 = var16 + var20.x * var11 / 2.0F;
                     float var24 = var17 + var20.y * var11 / 2.0F;
                     float var25 = var16 - var20.x * var11 / 2.0F;
                     float var26 = var17 - var20.y * var11 / 2.0F;
                     float var27 = var14 - var20.x * var11 / 2.0F;
                     float var28 = var15 - var20.y * var11 / 2.0F;
                     float var29 = Vector2f.length(var16 - var14, var17 - var15);
                     float var30 = 0.0F;
                     float var31 = var29 / (var11 * ((float)var5.getHeight() / (float)var5.getWidth()));
                     float var32 = 0.0F;
                     float var33 = 0.0F;
                     float var34 = 1.0F;
                     float var35 = 0.0F;
                     float var36 = 1.0F;
                     float var37 = var29 / (var11 * ((float)var5.getHeight() / (float)var5.getWidth()));
                     WorldMapRenderer.m_vboLinesUV.addQuad(var21, var22, var30, var31, var23, var24, var32, var33, var25, var26, var34, var35, var27, var28, var36, var37, 0.0F, var3.r, var3.g, var3.b, var3.a);
                  }
               }
            }

         }
      }

      public void fillPolygon(WorldMapStyleLayer.RenderArgs var1, WorldMapFeature var2, WorldMapStyleLayer.RGBAf var3) {
         WorldMapRenderer.m_vboLinesUV.flush();
         float var4 = this.m_renderCellX;
         float var5 = this.m_renderCellY;
         float var6 = this.m_worldScale;
         float var7 = var3.r;
         float var8 = var3.g;
         float var9 = var3.b;
         float var10 = var3.a;

         for(int var11 = 0; var11 < var2.m_geometries.size(); ++var11) {
            WorldMapGeometry var12 = (WorldMapGeometry)var2.m_geometries.get(var11);
            if (var12.m_type == WorldMapGeometry.Type.Polygon) {
               boolean var13 = false;
               int var18;
               if (var12.m_triangles == null) {
                  if (this.m_triangulationsThisFrame > 500) {
                     continue;
                  }

                  ++this.m_triangulationsThisFrame;
                  double[] var14 = var2.m_properties.containsKey("highway") ? new double[]{1.0D, 2.0D, 4.0D, 8.0D, 12.0D, 18.0D} : null;
                  var12.triangulate(var14);
                  if (var12.m_triangles == null) {
                     if (!Core.bDebug) {
                        continue;
                     }

                     WorldMapRenderer.m_vboLines.setMode(1);
                     var7 = 1.0F;
                     var9 = 0.0F;
                     var8 = 0.0F;
                     WorldMapRenderer.m_vboLines.setLineWidth(4.0F);

                     for(int var15 = 0; var15 < var12.m_points.size(); ++var15) {
                        WorldMapPoints var28 = (WorldMapPoints)var12.m_points.get(var15);

                        for(int var30 = 0; var30 < var28.numPoints(); ++var30) {
                           var18 = var28.getX(var30);
                           int var31 = var28.getY(var30);
                           int var32 = var28.getX((var30 + 1) % var28.numPoints());
                           int var33 = var28.getY((var30 + 1) % var28.numPoints());
                           WorldMapRenderer.m_vboLines.reserve(2);
                           WorldMapRenderer.m_vboLines.addElement(var4 + (float)var18 * var6, var5 + (float)var31 * var6, 0.0F, var7, var8, var9, var10);
                           WorldMapRenderer.m_vboLines.addElement(var4 + (float)var32 * var6, var5 + (float)var33 * var6, 0.0F, var7, var8, var9, var10);
                        }
                     }

                     WorldMapRenderer.m_vboLines.setLineWidth(1.0F);
                     continue;
                  }

                  if (var13) {
                     this.uploadTrianglesToVBO(var12);
                  }
               }

               if (var13) {
                  GL11.glTranslatef(var4, var5, 0.0F);
                  GL11.glScalef(var6, var6, var6);
                  GL11.glColor4f(var7, var8, var9, var10);
                  if (var12.m_triangles.length / 2 > 2340) {
                     int var29 = PZMath.min(var12.m_triangles.length / 2, 2340);
                     WorldMapVBOs.getInstance().drawElements(4, var12.m_vboIndex1, var12.m_vboIndex2, var29);
                     WorldMapVBOs.getInstance().drawElements(4, var12.m_vboIndex3, var12.m_vboIndex4, var12.m_triangles.length / 2 - var29);
                  } else {
                     WorldMapVBOs.getInstance().drawElements(4, var12.m_vboIndex1, var12.m_vboIndex2, var12.m_triangles.length / 2);
                  }

                  GL11.glScalef(1.0F / var6, 1.0F / var6, 1.0F / var6);
                  GL11.glTranslatef(-var4, -var5, 0.0F);
               } else {
                  WorldMapRenderer.m_vboLines.setMode(4);
                  double var27 = 0.0D;
                  if ((double)this.m_zoomF <= 11.5D) {
                     var27 = 18.0D;
                  } else if ((double)this.m_zoomF <= 12.0D) {
                     var27 = 12.0D;
                  } else if ((double)this.m_zoomF <= 12.5D) {
                     var27 = 8.0D;
                  } else if ((double)this.m_zoomF <= 13.0D) {
                     var27 = 4.0D;
                  } else if ((double)this.m_zoomF <= 13.5D) {
                     var27 = 2.0D;
                  } else if ((double)this.m_zoomF <= 14.0D) {
                     var27 = 1.0D;
                  }

                  WorldMapGeometry.TrianglesPerZoom var16 = var27 == 0.0D ? null : var12.findTriangles(var27);
                  float[] var17;
                  float var19;
                  float var20;
                  float var21;
                  float var22;
                  float var23;
                  float var24;
                  if (var16 != null) {
                     var17 = var16.m_triangles;

                     for(var18 = 0; var18 < var17.length; var18 += 6) {
                        var19 = var17[var18];
                        var20 = var17[var18 + 1];
                        var21 = var17[var18 + 2];
                        var22 = var17[var18 + 3];
                        var23 = var17[var18 + 4];
                        var24 = var17[var18 + 5];
                        WorldMapRenderer.m_vboLines.reserve(3);
                        float var25 = 1.0F;
                        WorldMapRenderer.m_vboLines.addElement(var4 + var19 * var6, var5 + var20 * var6, 0.0F, var7 * var25, var8 * var25, var9 * var25, var10);
                        WorldMapRenderer.m_vboLines.addElement(var4 + var21 * var6, var5 + var22 * var6, 0.0F, var7 * var25, var8 * var25, var9 * var25, var10);
                        WorldMapRenderer.m_vboLines.addElement(var4 + var23 * var6, var5 + var24 * var6, 0.0F, var7 * var25, var8 * var25, var9 * var25, var10);
                     }
                  } else {
                     var17 = var12.m_triangles;

                     for(var18 = 0; var18 < var17.length; var18 += 6) {
                        var19 = var17[var18];
                        var20 = var17[var18 + 1];
                        var21 = var17[var18 + 2];
                        var22 = var17[var18 + 3];
                        var23 = var17[var18 + 4];
                        var24 = var17[var18 + 5];
                        WorldMapRenderer.m_vboLines.reserve(3);
                        WorldMapRenderer.m_vboLines.addElement(var4 + var19 * var6, var5 + var20 * var6, 0.0F, var7, var8, var9, var10);
                        WorldMapRenderer.m_vboLines.addElement(var4 + var21 * var6, var5 + var22 * var6, 0.0F, var7, var8, var9, var10);
                        WorldMapRenderer.m_vboLines.addElement(var4 + var23 * var6, var5 + var24 * var6, 0.0F, var7, var8, var9, var10);
                     }
                  }
               }
            }
         }

      }

      public void fillPolygon(WorldMapStyleLayer.RenderArgs var1, WorldMapFeature var2, WorldMapStyleLayer.RGBAf var3, Texture var4, float var5) {
         WorldMapRenderer.m_vboLines.flush();
         float var6 = this.m_renderCellX;
         float var7 = this.m_renderCellY;
         float var8 = this.m_worldScale;
         float var9 = var3.r;
         float var10 = var3.g;
         float var11 = var3.b;
         float var12 = var3.a;

         for(int var13 = 0; var13 < var2.m_geometries.size(); ++var13) {
            WorldMapGeometry var14 = (WorldMapGeometry)var2.m_geometries.get(var13);
            if (var14.m_type == WorldMapGeometry.Type.Polygon) {
               if (var14.m_triangles == null) {
                  var14.triangulate((double[])null);
                  if (var14.m_triangles == null) {
                     continue;
                  }
               }

               GL11.glEnable(3553);
               GL11.glTexParameteri(3553, 10241, 9728);
               GL11.glTexParameteri(3553, 10240, 9728);
               WorldMapRenderer.m_vboLinesUV.setMode(4);
               WorldMapRenderer.m_vboLinesUV.startRun(var4.getTextureId());
               float[] var15 = var14.m_triangles;
               float var16 = (float)(var1.cellX * 300 + var14.m_minX);
               float var17 = (float)(var1.cellY * 300 + var14.m_minY);
               float var18 = (float)var4.getWidth() * var5;
               float var19 = (float)var4.getHeight() * var5;
               float var20 = (float)var4.getWidthHW();
               float var21 = (float)var4.getHeightHW();
               float var22 = PZMath.floor(var16 / var18) * var18;
               float var23 = PZMath.floor(var17 / var19) * var19;

               for(int var24 = 0; var24 < var15.length; var24 += 6) {
                  float var25 = var15[var24];
                  float var26 = var15[var24 + 1];
                  float var27 = var15[var24 + 2];
                  float var28 = var15[var24 + 3];
                  float var29 = var15[var24 + 4];
                  float var30 = var15[var24 + 5];
                  float var31 = (var25 + (float)(var1.cellX * 300) - var22) / var5;
                  float var32 = (var26 + (float)(var1.cellY * 300) - var23) / var5;
                  float var33 = (var27 + (float)(var1.cellX * 300) - var22) / var5;
                  float var34 = (var28 + (float)(var1.cellY * 300) - var23) / var5;
                  float var35 = (var29 + (float)(var1.cellX * 300) - var22) / var5;
                  float var36 = (var30 + (float)(var1.cellY * 300) - var23) / var5;
                  var25 = var6 + var25 * var8;
                  var26 = var7 + var26 * var8;
                  var27 = var6 + var27 * var8;
                  var28 = var7 + var28 * var8;
                  var29 = var6 + var29 * var8;
                  var30 = var7 + var30 * var8;
                  float var37 = var31 / var20;
                  float var38 = var32 / var21;
                  float var39 = var33 / var20;
                  float var40 = var34 / var21;
                  float var41 = var35 / var20;
                  float var42 = var36 / var21;
                  WorldMapRenderer.m_vboLinesUV.reserve(3);
                  WorldMapRenderer.m_vboLinesUV.addElement(var25, var26, 0.0F, var37, var38, var9, var10, var11, var12);
                  WorldMapRenderer.m_vboLinesUV.addElement(var27, var28, 0.0F, var39, var40, var9, var10, var11, var12);
                  WorldMapRenderer.m_vboLinesUV.addElement(var29, var30, 0.0F, var41, var42, var9, var10, var11, var12);
               }

               GL11.glDisable(3553);
            }
         }

      }

      void uploadTrianglesToVBO(WorldMapGeometry var1) {
         int[] var2 = new int[2];
         int var3 = var1.m_triangles.length / 2;
         int var5;
         float var9;
         float var10;
         float var11;
         if (var3 > 2340) {
            for(int var4 = 0; var3 > 0; var3 -= var5 * 3) {
               var5 = PZMath.min(var3 / 3, 780);
               WorldMapVBOs.getInstance().reserveVertices(var5 * 3, var2);
               if (var1.m_vboIndex1 == -1) {
                  var1.m_vboIndex1 = var2[0];
                  var1.m_vboIndex2 = var2[1];
               } else {
                  var1.m_vboIndex3 = var2[0];
                  var1.m_vboIndex4 = var2[1];
               }

               float[] var6 = var1.m_triangles;
               int var7 = var4 * 3 * 2;

               for(int var8 = (var4 + var5) * 3 * 2; var7 < var8; var7 += 6) {
                  var9 = var6[var7];
                  var10 = var6[var7 + 1];
                  var11 = var6[var7 + 2];
                  float var12 = var6[var7 + 3];
                  float var13 = var6[var7 + 4];
                  float var14 = var6[var7 + 5];
                  WorldMapVBOs.getInstance().addElement(var9, var10, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                  WorldMapVBOs.getInstance().addElement(var11, var12, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
                  WorldMapVBOs.getInstance().addElement(var13, var14, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
               }

               var4 += var5;
            }
         } else {
            WorldMapVBOs.getInstance().reserveVertices(var3, var2);
            var1.m_vboIndex1 = var2[0];
            var1.m_vboIndex2 = var2[1];
            float[] var15 = var1.m_triangles;

            for(var5 = 0; var5 < var15.length; var5 += 6) {
               float var16 = var15[var5];
               float var17 = var15[var5 + 1];
               float var18 = var15[var5 + 2];
               var9 = var15[var5 + 3];
               var10 = var15[var5 + 4];
               var11 = var15[var5 + 5];
               WorldMapVBOs.getInstance().addElement(var16, var17, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
               WorldMapVBOs.getInstance().addElement(var18, var9, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
               WorldMapVBOs.getInstance().addElement(var10, var11, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            }
         }

      }

      void outlineTriangles(WorldMapGeometry var1, float var2, float var3, float var4) {
         WorldMapRenderer.m_vboLines.setMode(1);
         float var8 = 1.0F;
         float var5 = 1.0F;
         float var7 = 0.0F;
         float var6 = 0.0F;
         float[] var9 = var1.m_triangles;

         for(int var10 = 0; var10 < var9.length; var10 += 6) {
            float var11 = var9[var10];
            float var12 = var9[var10 + 1];
            float var13 = var9[var10 + 2];
            float var14 = var9[var10 + 3];
            float var15 = var9[var10 + 4];
            float var16 = var9[var10 + 5];
            WorldMapRenderer.m_vboLines.addElement(var2 + var11 * var4, var3 + var12 * var4, 0.0F, var5, var6, var7, var8);
            WorldMapRenderer.m_vboLines.addElement(var2 + var13 * var4, var3 + var14 * var4, 0.0F, var5, var6, var7, var8);
            WorldMapRenderer.m_vboLines.addElement(var2 + var13 * var4, var3 + var14 * var4, 0.0F, var5, var6, var7, var8);
            WorldMapRenderer.m_vboLines.addElement(var2 + var15 * var4, var3 + var16 * var4, 0.0F, var5, var6, var7, var8);
            WorldMapRenderer.m_vboLines.addElement(var2 + var15 * var4, var3 + var16 * var4, 0.0F, var5, var6, var7, var8);
            WorldMapRenderer.m_vboLines.addElement(var2 + var11 * var4, var3 + var12 * var4, 0.0F, var5, var6, var7, var8);
         }

      }

      void outlinePolygon(WorldMapGeometry var1, float var2, float var3, float var4) {
         WorldMapRenderer.m_vboLines.setMode(1);
         float var8 = 1.0F;
         float var7 = 0.8F;
         float var6 = 0.8F;
         float var5 = 0.8F;
         WorldMapRenderer.m_vboLines.setLineWidth(4.0F);

         for(int var9 = 0; var9 < var1.m_points.size(); ++var9) {
            WorldMapPoints var10 = (WorldMapPoints)var1.m_points.get(var9);

            for(int var11 = 0; var11 < var10.numPoints(); ++var11) {
               int var12 = var10.getX(var11);
               int var13 = var10.getY(var11);
               int var14 = var10.getX((var11 + 1) % var10.numPoints());
               int var15 = var10.getY((var11 + 1) % var10.numPoints());
               WorldMapRenderer.m_vboLines.addElement(var2 + (float)var12 * var4, var3 + (float)var13 * var4, 0.0F, var5, var6, var7, var8);
               WorldMapRenderer.m_vboLines.addElement(var2 + (float)var14 * var4, var3 + (float)var15 * var4, 0.0F, var5, var6, var7, var8);
            }
         }

         WorldMapRenderer.m_vboLines.setLineWidth(1.0F);
      }

      public void drawTexture(Texture var1, WorldMapStyleLayer.RGBAf var2, int var3, int var4, int var5, int var6) {
         if (var1 != null && var1.isReady()) {
            WorldMapRenderer.m_vboLines.flush();
            WorldMapRenderer.m_vboLinesUV.flush();
            float var7 = this.m_worldScale;
            float var8 = ((float)var3 - this.m_centerWorldX) * var7;
            float var9 = ((float)var4 - this.m_centerWorldY) * var7;
            float var10 = var8 + (float)(var5 - var3) * var7;
            float var11 = var9 + (float)(var6 - var4) * var7;
            float var12 = PZMath.clamp(var8, this.m_renderCellX, this.m_renderCellX + 300.0F * var7);
            float var13 = PZMath.clamp(var9, this.m_renderCellY, this.m_renderCellY + 300.0F * var7);
            float var14 = PZMath.clamp(var10, this.m_renderCellX, this.m_renderCellX + 300.0F * var7);
            float var15 = PZMath.clamp(var11, this.m_renderCellY, this.m_renderCellY + 300.0F * var7);
            if (!(var12 >= var14) && !(var13 >= var15)) {
               float var16 = (float)var1.getWidth() / (float)(var5 - var3);
               float var17 = (float)var1.getHeight() / (float)(var6 - var4);
               GL11.glEnable(3553);
               GL11.glEnable(3042);
               GL11.glDisable(2929);
               if (var1.getID() == -1) {
                  var1.bind();
               } else {
                  GL11.glBindTexture(3553, Texture.lastTextureID = var1.getID());
                  GL11.glTexParameteri(3553, 10241, 9728);
                  GL11.glTexParameteri(3553, 10240, 9728);
               }

               float var18 = (var12 - var8) / ((float)var1.getWidthHW() * var7) * var16;
               float var19 = (var13 - var9) / ((float)var1.getHeightHW() * var7) * var17;
               float var20 = (var14 - var8) / ((float)var1.getWidthHW() * var7) * var16;
               float var21 = (var15 - var9) / ((float)var1.getHeightHW() * var7) * var17;
               WorldMapRenderer.m_vboLinesUV.setMode(7);
               WorldMapRenderer.m_vboLinesUV.startRun(var1.getTextureId());
               WorldMapRenderer.m_vboLinesUV.addQuad(var12, var13, var18, var19, var14, var15, var20, var21, 0.0F, var2.r, var2.g, var2.b, var2.a);
            }
         }
      }

      public void drawTextureTiled(Texture var1, WorldMapStyleLayer.RGBAf var2, int var3, int var4, int var5, int var6, int var7, int var8) {
         if (var1 != null && var1.isReady()) {
            if (var7 * 300 < var5 && (var7 + 1) * 300 > var3) {
               if (var8 * 300 < var6 && (var8 + 1) * 300 > var4) {
                  WorldMapRenderer.m_vboLines.flush();
                  float var9 = this.m_worldScale;
                  int var10 = var1.getWidth();
                  int var11 = var1.getHeight();
                  int var12 = (int)(PZMath.floor((float)var7 * 300.0F / (float)var10) * (float)var10);
                  int var13 = (int)(PZMath.floor((float)var8 * 300.0F / (float)var11) * (float)var11);
                  int var14 = var12 + (int)Math.ceil((double)(((float)(var7 + 1) * 300.0F - (float)var12) / (float)var10)) * var10;
                  int var15 = var13 + (int)Math.ceil((double)(((float)(var8 + 1) * 300.0F - (float)var13) / (float)var11)) * var11;
                  float var16 = (float)PZMath.clamp(var12, var7 * 300, (var7 + 1) * 300);
                  float var17 = (float)PZMath.clamp(var13, var8 * 300, (var8 + 1) * 300);
                  float var18 = (float)PZMath.clamp(var14, var7 * 300, (var7 + 1) * 300);
                  float var19 = (float)PZMath.clamp(var15, var8 * 300, (var8 + 1) * 300);
                  var16 = PZMath.clamp(var16, (float)var3, (float)var5);
                  var17 = PZMath.clamp(var17, (float)var4, (float)var6);
                  var18 = PZMath.clamp(var18, (float)var3, (float)var5);
                  var19 = PZMath.clamp(var19, (float)var4, (float)var6);
                  float var20 = (var16 - (float)var3) / (float)var10;
                  float var21 = (var17 - (float)var4) / (float)var11;
                  float var22 = (var18 - (float)var3) / (float)var10;
                  float var23 = (var19 - (float)var4) / (float)var11;
                  var16 = (var16 - this.m_centerWorldX) * var9;
                  var17 = (var17 - this.m_centerWorldY) * var9;
                  var18 = (var18 - this.m_centerWorldX) * var9;
                  var19 = (var19 - this.m_centerWorldY) * var9;
                  float var24 = var20 * var1.xEnd;
                  float var25 = var21 * var1.yEnd;
                  float var26 = (float)((int)var22) + (var22 - (float)((int)var22)) * var1.xEnd;
                  float var27 = (float)((int)var23) + (var23 - (float)((int)var23)) * var1.yEnd;
                  GL11.glEnable(3553);
                  if (var1.getID() == -1) {
                     var1.bind();
                  } else {
                     GL11.glBindTexture(3553, Texture.lastTextureID = var1.getID());
                     GL11.glTexParameteri(3553, 10241, 9728);
                     GL11.glTexParameteri(3553, 10240, 9728);
                     GL11.glTexParameteri(3553, 10242, 10497);
                     GL11.glTexParameteri(3553, 10243, 10497);
                  }

                  WorldMapRenderer.m_vboLinesUV.setMode(7);
                  WorldMapRenderer.m_vboLinesUV.startRun(var1.getTextureId());
                  WorldMapRenderer.m_vboLinesUV.addQuad(var16, var17, var24, var25, var18, var19, var26, var27, 0.0F, var2.r, var2.g, var2.b, var2.a);
                  GL11.glDisable(3553);
               }
            }
         }
      }

      public void drawTextureTiled(Texture var1, WorldMapStyleLayer.RGBAf var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
         if (var1 != null && var1.isReady()) {
            WorldMapRenderer.m_vboLines.flush();
            WorldMapRenderer.m_vboLinesUV.flush();
            float var11 = this.m_worldScale;
            float var12 = (float)var3;
            float var13 = (float)var4;
            float var14 = (float)var5;
            float var15 = (float)var6;
            float var16 = PZMath.clamp(var12, (float)(var9 * 300), (float)((var9 + 1) * 300));
            float var17 = PZMath.clamp(var13, (float)(var10 * 300), (float)((var10 + 1) * 300));
            float var18 = PZMath.clamp(var14, (float)(var9 * 300), (float)((var9 + 1) * 300));
            float var19 = PZMath.clamp(var15, (float)(var10 * 300), (float)((var10 + 1) * 300));
            float var20 = (var16 - (float)var3) / (float)var7;
            float var21 = (var17 - (float)var4) / (float)var8;
            float var22 = (var18 - (float)var3) / (float)var7;
            float var23 = (var19 - (float)var4) / (float)var8;
            var16 = (var16 - this.m_centerWorldX) * var11;
            var17 = (var17 - this.m_centerWorldY) * var11;
            var18 = (var18 - this.m_centerWorldX) * var11;
            var19 = (var19 - this.m_centerWorldY) * var11;
            float var24 = var20 * var1.xEnd;
            float var25 = var21 * var1.yEnd;
            float var26 = (float)((int)var22) + (var22 - (float)((int)var22)) * var1.xEnd;
            float var27 = (float)((int)var23) + (var23 - (float)((int)var23)) * var1.yEnd;
            GL11.glEnable(3553);
            if (var1.getID() == -1) {
               var1.bind();
            } else {
               GL11.glBindTexture(3553, Texture.lastTextureID = var1.getID());
               GL11.glTexParameteri(3553, 10241, 9728);
               GL11.glTexParameteri(3553, 10240, 9728);
               GL11.glTexParameteri(3553, 10242, 10497);
               GL11.glTexParameteri(3553, 10243, 10497);
            }

            GL11.glColor4f(var2.r, var2.g, var2.b, var2.a);
            GL11.glBegin(7);
            GL11.glTexCoord2f(var24, var25);
            GL11.glVertex2f(var16, var17);
            GL11.glTexCoord2f(var26, var25);
            GL11.glVertex2f(var18, var17);
            GL11.glTexCoord2f(var26, var27);
            GL11.glVertex2f(var18, var19);
            GL11.glTexCoord2f(var24, var27);
            GL11.glVertex2f(var16, var19);
            GL11.glEnd();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(3553);
         }
      }

      void renderZones() {
         this.m_zoneSet.clear();

         for(int var1 = 0; var1 < this.m_rasterizeXY.size() - 1; var1 += 2) {
            int var2 = this.m_rasterizeXY_ints[var1];
            int var3 = this.m_rasterizeXY_ints[var1 + 1];
            if (this.m_renderer.m_visited == null || this.m_renderer.m_visited.isCellVisible(var2, var3)) {
               IsoMetaCell var4 = IsoWorld.instance.MetaGrid.getCellData(var2, var3);
               if (var4 != null) {
                  var4.getZonesUnique(this.m_zoneSet);
               }
            }
         }

         this.m_zones.clear();
         this.m_zones.addAll(this.m_zoneSet);
         this.renderZones(this.m_zones, "Forest", 0.0F, 1.0F, 0.0F, 0.25F);
         this.renderZones(this.m_zones, "DeepForest", 0.0F, 0.5F, 0.0F, 0.25F);
         this.renderZones(this.m_zones, "Nav", 0.0F, 0.0F, 1.0F, 0.25F);
      }

      void renderZones(ArrayList var1, String var2, float var3, float var4, float var5, float var6) {
         WorldMapRenderer.m_vboLinesUV.flush();
         float var7 = this.m_worldScale;
         WorldMapRenderer.m_vboLines.setMode(4);
         Iterator var8 = var1.iterator();

         while(true) {
            float[] var10;
            int var11;
            float var12;
            float var13;
            float var14;
            float var15;
            float var16;
            float var17;
            do {
               IsoMetaGrid.Zone var9;
               label93:
               do {
                  do {
                     do {
                        if (!var8.hasNext()) {
                           WorldMapRenderer.m_vboLines.setMode(1);
                           WorldMapRenderer.m_vboLines.setLineWidth(2.0F);
                           var8 = var1.iterator();

                           while(true) {
                              do {
                                 do {
                                    do {
                                       if (!var8.hasNext()) {
                                          return;
                                       }

                                       var9 = (IsoMetaGrid.Zone)var8.next();
                                    } while(!var2.equals(var9.type));

                                    float var20;
                                    if (var9.isRectangle()) {
                                       float var18 = ((float)var9.x - this.m_centerWorldX) * var7;
                                       var20 = ((float)var9.y - this.m_centerWorldY) * var7;
                                       var12 = ((float)(var9.x + var9.w) - this.m_centerWorldX) * var7;
                                       var13 = ((float)(var9.y + var9.h) - this.m_centerWorldY) * var7;
                                       WorldMapRenderer.m_vboLines.addLine(var18, var20, 0.0F, var12, var20, 0.0F, var3, var4, var5, 1.0F);
                                       WorldMapRenderer.m_vboLines.addLine(var12, var20, 0.0F, var12, var13, 0.0F, var3, var4, var5, 1.0F);
                                       WorldMapRenderer.m_vboLines.addLine(var12, var13, 0.0F, var18, var13, 0.0F, var3, var4, var5, 1.0F);
                                       WorldMapRenderer.m_vboLines.addLine(var18, var13, 0.0F, var18, var20, 0.0F, var3, var4, var5, 1.0F);
                                    }

                                    if (var9.isPolygon()) {
                                       for(int var19 = 0; var19 < var9.points.size(); var19 += 2) {
                                          var20 = ((float)var9.points.getQuick(var19) - this.m_centerWorldX) * var7;
                                          var12 = ((float)var9.points.getQuick(var19 + 1) - this.m_centerWorldY) * var7;
                                          var13 = ((float)var9.points.getQuick((var19 + 2) % var9.points.size()) - this.m_centerWorldX) * var7;
                                          var14 = ((float)var9.points.getQuick((var19 + 3) % var9.points.size()) - this.m_centerWorldY) * var7;
                                          WorldMapRenderer.m_vboLines.addLine(var20, var12, 0.0F, var13, var14, 0.0F, var3, var4, var5, 1.0F);
                                       }
                                    }
                                 } while(!var9.isPolyline());

                                 var10 = var9.polylineOutlinePoints;
                              } while(var10 == null);

                              for(var11 = 0; var11 < var10.length; var11 += 2) {
                                 var12 = (var10[var11] - this.m_centerWorldX) * var7;
                                 var13 = (var10[var11 + 1] - this.m_centerWorldY) * var7;
                                 var14 = (var10[(var11 + 2) % var10.length] - this.m_centerWorldX) * var7;
                                 var15 = (var10[(var11 + 3) % var10.length] - this.m_centerWorldY) * var7;
                                 WorldMapRenderer.m_vboLines.addLine(var12, var13, 0.0F, var14, var15, 0.0F, var3, var4, var5, 1.0F);
                              }
                           }
                        }

                        var9 = (IsoMetaGrid.Zone)var8.next();
                     } while(!var2.equals(var9.type));

                     if (var9.isRectangle()) {
                        WorldMapRenderer.m_vboLines.addQuad(((float)var9.x - this.m_centerWorldX) * var7, ((float)var9.y - this.m_centerWorldY) * var7, ((float)(var9.x + var9.w) - this.m_centerWorldX) * var7, ((float)(var9.y + var9.h) - this.m_centerWorldY) * var7, 0.0F, var3, var4, var5, var6);
                     }

                     if (!var9.isPolygon()) {
                        continue label93;
                     }

                     var10 = var9.getPolygonTriangles();
                  } while(var10 == null);

                  for(var11 = 0; var11 < var10.length; var11 += 6) {
                     var12 = (var10[var11] - this.m_centerWorldX) * var7;
                     var13 = (var10[var11 + 1] - this.m_centerWorldY) * var7;
                     var14 = (var10[var11 + 2] - this.m_centerWorldX) * var7;
                     var15 = (var10[var11 + 3] - this.m_centerWorldY) * var7;
                     var16 = (var10[var11 + 4] - this.m_centerWorldX) * var7;
                     var17 = (var10[var11 + 5] - this.m_centerWorldY) * var7;
                     WorldMapRenderer.m_vboLines.addTriangle(var12, var13, 0.0F, var14, var15, 0.0F, var16, var17, 0.0F, var3, var4, var5, var6);
                  }
               } while(!var9.isPolyline());

               var10 = var9.getPolylineOutlineTriangles();
            } while(var10 == null);

            for(var11 = 0; var11 < var10.length; var11 += 6) {
               var12 = (var10[var11] - this.m_centerWorldX) * var7;
               var13 = (var10[var11 + 1] - this.m_centerWorldY) * var7;
               var14 = (var10[var11 + 2] - this.m_centerWorldX) * var7;
               var15 = (var10[var11 + 3] - this.m_centerWorldY) * var7;
               var16 = (var10[var11 + 4] - this.m_centerWorldX) * var7;
               var17 = (var10[var11 + 5] - this.m_centerWorldY) * var7;
               WorldMapRenderer.m_vboLines.addTriangle(var12, var13, 0.0F, var14, var15, 0.0F, var16, var17, 0.0F, var3, var4, var5, var6);
            }
         }
      }

      public void render() {
         try {
            PZGLUtil.pushAndLoadMatrix(5889, this.m_projection);
            PZGLUtil.pushAndLoadMatrix(5888, this.m_modelView);
            this.renderInternal();
         } catch (Exception var5) {
            ExceptionLogger.logException(var5);
         } finally {
            PZGLUtil.popMatrix(5889);
            PZGLUtil.popMatrix(5888);
         }

      }

      private void renderInternal() {
         float var1 = this.m_worldScale;
         int var2 = (int)Math.max(this.uiToWorldX(0.0F, 0.0F), (float)this.m_worldMap.getMinXInSquares()) / 300;
         int var3 = (int)Math.max(this.uiToWorldY(0.0F, 0.0F), (float)this.m_worldMap.getMinYInSquares()) / 300;
         int var4 = (int)Math.min(this.uiToWorldX((float)this.getWidth(), (float)this.getHeight()), (float)(this.m_worldMap.m_maxX * 300)) / 300;
         int var5 = (int)Math.min(this.uiToWorldY((float)this.getWidth(), (float)this.getHeight()), (float)(this.m_worldMap.m_maxY * 300)) / 300;
         var2 = this.m_worldMap.getMinXInSquares();
         var3 = this.m_worldMap.getMinYInSquares();
         var4 = this.m_worldMap.m_maxX;
         var5 = this.m_worldMap.m_maxY;
         GL11.glViewport(this.m_x, Core.height - this.m_height - this.m_y, this.m_width, this.m_height);
         GLVertexBufferObject.funcs.glBindBuffer(GLVertexBufferObject.funcs.GL_ARRAY_BUFFER(), 0);
         GLVertexBufferObject.funcs.glBindBuffer(GLVertexBufferObject.funcs.GL_ELEMENT_ARRAY_BUFFER(), 0);
         GL11.glPolygonMode(1032, this.m_renderer.Wireframe.getValue() ? 6913 : 6914);
         if (this.m_renderer.ImagePyramid.getValue()) {
            this.renderImagePyramids();
         }

         this.calculateVisibleCells();
         if (this.m_renderer.Features.getValue()) {
            this.renderCellFeatures();
         }

         if (this.m_renderer.ForestZones.getValue()) {
            this.renderZones();
         }

         if (this.m_renderer.VisibleCells.getValue()) {
            this.renderVisibleCells();
         }

         WorldMapRenderer.m_vboLines.flush();
         WorldMapRenderer.m_vboLinesUV.flush();
         GL11.glEnableClientState(32884);
         GL11.glEnableClientState(32886);
         GL13.glActiveTexture(33984);
         GL13.glClientActiveTexture(33984);
         GL11.glEnableClientState(32888);
         GL11.glTexEnvi(8960, 8704, 8448);
         GL11.glPolygonMode(1032, 6914);
         GL11.glEnable(3042);
         SpriteRenderer.ringBuffer.restoreBoundTextures = true;
         SpriteRenderer.ringBuffer.restoreVBOs = true;
         if (this.m_renderer.m_visited != null) {
            this.m_renderer.m_visited.render(this.m_renderOriginX - (float)(this.m_worldMap.getMinXInSquares() - this.m_renderer.m_visited.getMinX() * 300) * var1, this.m_renderOriginY - (float)(this.m_worldMap.getMinYInSquares() - this.m_renderer.m_visited.getMinY() * 300) * var1, var2 / 300, var3 / 300, var4 / 300, var5 / 300, var1, this.m_renderer.BlurUnvisited.getValue());
            if (this.m_renderer.UnvisitedGrid.getValue()) {
               this.m_renderer.m_visited.renderGrid(this.m_renderOriginX - (float)(this.m_worldMap.getMinXInSquares() - this.m_renderer.m_visited.getMinX() * 300) * var1, this.m_renderOriginY - (float)(this.m_worldMap.getMinYInSquares() - this.m_renderer.m_visited.getMinY() * 300) * var1, var2 / 300, var3 / 300, var4 / 300, var5 / 300, var1, this.m_zoomF);
            }
         }

         this.renderPlayers();
         if (this.m_renderer.CellGrid.getValue()) {
            this.renderCellGrid(var2 / 300, var3 / 300, var4 / 300, var5 / 300);
         }

         if (Core.bDebug) {
         }

         this.paintAreasOutsideBounds(var2, var3, var4, var5, var1);
         if (this.m_renderer.WorldBounds.getValue()) {
            this.renderWorldBounds();
         }

         WorldMapRenderer.m_vboLines.flush();
         WorldMapRenderer.m_vboLinesUV.flush();
         GL11.glViewport(0, 0, Core.width, Core.height);
      }

      private void rasterizeCellsCallback(int var1, int var2) {
         int var3 = var1 + var2 * this.m_worldMap.getWidthInCells();
         if (!this.m_rasterizeSet.contains(var3)) {
            for(int var4 = var2 * this.m_rasterizeMult; var4 < var2 * this.m_rasterizeMult + this.m_rasterizeMult; ++var4) {
               for(int var5 = var1 * this.m_rasterizeMult; var5 < var1 * this.m_rasterizeMult + this.m_rasterizeMult; ++var5) {
                  if (var5 >= this.m_worldMap.getMinXInCells() && var5 <= this.m_worldMap.getMaxXInCells() && var4 >= this.m_worldMap.getMinYInCells() && var4 <= this.m_worldMap.getMaxYInCells()) {
                     this.m_rasterizeSet.add(var3);
                     this.m_rasterizeXY.add(var5);
                     this.m_rasterizeXY.add(var4);
                  }
               }
            }

         }
      }

      private void rasterizeTilesCallback(int var1, int var2) {
         int var3 = var1 + var2 * 1000;
         if (!this.m_rasterizeSet.contains(var3)) {
            if (!((float)var1 < this.m_rasterizeMinTileX) && !((float)var1 > this.m_rasterizeMaxTileX) && !((float)var2 < this.m_rasterizeMinTileY) && !((float)var2 > this.m_rasterizeMaxTileY)) {
               this.m_rasterizeSet.add(var3);
               this.m_rasterizeXY.add(var1);
               this.m_rasterizeXY.add(var2);
            }
         }
      }

      private void calculateVisibleCells() {
         boolean var1 = Core.bDebug && this.m_renderer.VisibleCells.getValue();
         int var2 = var1 ? 200 : 0;
         float var3 = this.m_worldScale;
         if (1.0F / var3 > 100.0F) {
            this.m_rasterizeXY.clear();

            for(int var13 = this.m_worldMap.getMinYInCells(); var13 <= this.m_worldMap.getMaxYInCells(); ++var13) {
               for(int var14 = this.m_worldMap.getMinXInCells(); var14 <= this.m_worldMap.getMaxYInCells(); ++var14) {
                  this.m_rasterizeXY.add(var14);
                  this.m_rasterizeXY.add(var13);
               }
            }

            if (this.m_rasterizeXY_ints == null || this.m_rasterizeXY_ints.length < this.m_rasterizeXY.size()) {
               this.m_rasterizeXY_ints = new int[this.m_rasterizeXY.size()];
            }

            this.m_rasterizeXY_ints = this.m_rasterizeXY.toArray(this.m_rasterizeXY_ints);
         } else {
            float var4 = this.uiToWorldX((float)var2 + 0.0F, (float)var2 + 0.0F) / 300.0F;
            float var5 = this.uiToWorldY((float)var2 + 0.0F, (float)var2 + 0.0F) / 300.0F;
            float var6 = this.uiToWorldX((float)(this.getWidth() - var2), 0.0F + (float)var2) / 300.0F;
            float var7 = this.uiToWorldY((float)(this.getWidth() - var2), 0.0F + (float)var2) / 300.0F;
            float var8 = this.uiToWorldX((float)(this.getWidth() - var2), (float)(this.getHeight() - var2)) / 300.0F;
            float var9 = this.uiToWorldY((float)(this.getWidth() - var2), (float)(this.getHeight() - var2)) / 300.0F;
            float var10 = this.uiToWorldX(0.0F + (float)var2, (float)(this.getHeight() - var2)) / 300.0F;
            float var11 = this.uiToWorldY(0.0F + (float)var2, (float)(this.getHeight() - var2)) / 300.0F;

            int var12;
            for(var12 = 1; this.triangleArea(var10 / (float)var12, var11 / (float)var12, var8 / (float)var12, var9 / (float)var12, var6 / (float)var12, var7 / (float)var12) + this.triangleArea(var6 / (float)var12, var7 / (float)var12, var4 / (float)var12, var5 / (float)var12, var10 / (float)var12, var11 / (float)var12) > 80.0F; ++var12) {
            }

            this.m_rasterizeMult = var12;
            this.m_rasterizeXY.clear();
            this.m_rasterizeSet.clear();
            this.m_rasterize.scanTriangle(var10 / (float)var12, var11 / (float)var12, var8 / (float)var12, var9 / (float)var12, var6 / (float)var12, var7 / (float)var12, 0, 1000, this::rasterizeCellsCallback);
            this.m_rasterize.scanTriangle(var6 / (float)var12, var7 / (float)var12, var4 / (float)var12, var5 / (float)var12, var10 / (float)var12, var11 / (float)var12, 0, 1000, this::rasterizeCellsCallback);
            if (this.m_rasterizeXY_ints == null || this.m_rasterizeXY_ints.length < this.m_rasterizeXY.size()) {
               this.m_rasterizeXY_ints = new int[this.m_rasterizeXY.size()];
            }

            this.m_rasterizeXY_ints = this.m_rasterizeXY.toArray(this.m_rasterizeXY_ints);
         }
      }

      void renderVisibleCells() {
         boolean var1 = Core.bDebug && this.m_renderer.VisibleCells.getValue();
         int var2 = var1 ? 200 : 0;
         float var3 = this.m_worldScale;
         if (!(1.0F / var3 > 100.0F)) {
            WorldMapRenderer.m_vboLines.setMode(4);

            float var7;
            float var8;
            float var9;
            float var10;
            for(int var4 = 0; var4 < this.m_rasterizeXY.size(); var4 += 2) {
               int var5 = this.m_rasterizeXY.get(var4);
               int var6 = this.m_rasterizeXY.get(var4 + 1);
               var7 = this.m_renderOriginX + (float)(var5 * 300 - this.m_worldMap.getMinXInSquares()) * var3;
               var8 = this.m_renderOriginY + (float)(var6 * 300 - this.m_worldMap.getMinYInSquares()) * var3;
               var9 = this.m_renderOriginX + (float)((var5 + 1) * 300 - this.m_worldMap.getMinXInSquares()) * var3;
               var10 = this.m_renderOriginY + (float)((var6 + 1) * 300 - this.m_worldMap.getMinYInSquares()) * var3;
               WorldMapRenderer.m_vboLines.addElement(var7, var8, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var9, var8, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var7, var10, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var9, var8, 0.0F, 0.0F, 0.0F, 1.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var9, var10, 0.0F, 0.0F, 0.0F, 1.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var7, var10, 0.0F, 0.0F, 0.0F, 1.0F, 0.2F);
            }

            WorldMapRenderer.m_vboLines.flush();
            float var12 = this.uiToWorldX((float)var2 + 0.0F, (float)var2 + 0.0F) / 300.0F;
            float var13 = this.uiToWorldY((float)var2 + 0.0F, (float)var2 + 0.0F) / 300.0F;
            float var14 = this.uiToWorldX((float)(this.getWidth() - var2), 0.0F + (float)var2) / 300.0F;
            var7 = this.uiToWorldY((float)(this.getWidth() - var2), 0.0F + (float)var2) / 300.0F;
            var8 = this.uiToWorldX((float)(this.getWidth() - var2), (float)(this.getHeight() - var2)) / 300.0F;
            var9 = this.uiToWorldY((float)(this.getWidth() - var2), (float)(this.getHeight() - var2)) / 300.0F;
            var10 = this.uiToWorldX(0.0F + (float)var2, (float)(this.getHeight() - var2)) / 300.0F;
            float var11 = this.uiToWorldY(0.0F + (float)var2, (float)(this.getHeight() - var2)) / 300.0F;
            WorldMapRenderer.m_vboLines.setMode(1);
            WorldMapRenderer.m_vboLines.setLineWidth(4.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + (var10 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var11 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, this.m_renderOriginX + (var8 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var9 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + (var8 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var9 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, this.m_renderOriginX + (var14 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var7 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + (var14 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var7 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, this.m_renderOriginX + (var10 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var11 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, 0.5F, 0.5F, 0.5F, 1.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + (var14 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var7 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, this.m_renderOriginX + (var12 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var13 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + (var12 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var13 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, this.m_renderOriginX + (var10 * 300.0F - (float)this.m_worldMap.getMinXInSquares()) * var3, this.m_renderOriginY + (var11 * 300.0F - (float)this.m_worldMap.getMinYInSquares()) * var3, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
         }
      }

      void calcVisiblePyramidTiles(WorldMapImages var1) {
         if (Core.bDebug) {
         }

         boolean var2 = false;
         int var3 = var2 ? 200 : 0;
         float var4 = this.m_worldScale;
         int var5 = var1.getZoom(this.m_zoomF);
         short var6 = 256;
         float var7 = (float)(var6 * (1 << var5));
         int var8 = var1.getMinX();
         int var9 = var1.getMinY();
         float var10 = (this.uiToWorldX((float)var3 + 0.0F, (float)var3 + 0.0F) - (float)var8) / var7;
         float var11 = (this.uiToWorldY((float)var3 + 0.0F, (float)var3 + 0.0F) - (float)var9) / var7;
         float var12 = (this.uiToWorldX((float)(this.getWidth() - var3), 0.0F + (float)var3) - (float)var8) / var7;
         float var13 = (this.uiToWorldY((float)(this.getWidth() - var3), 0.0F + (float)var3) - (float)var9) / var7;
         float var14 = (this.uiToWorldX((float)(this.getWidth() - var3), (float)(this.getHeight() - var3)) - (float)var8) / var7;
         float var15 = (this.uiToWorldY((float)(this.getWidth() - var3), (float)(this.getHeight() - var3)) - (float)var9) / var7;
         float var16 = (this.uiToWorldX(0.0F + (float)var3, (float)(this.getHeight() - var3)) - (float)var8) / var7;
         float var17 = (this.uiToWorldY(0.0F + (float)var3, (float)(this.getHeight() - var3)) - (float)var9) / var7;
         if (var2) {
            WorldMapRenderer.m_vboLines.setMode(1);
            WorldMapRenderer.m_vboLines.setLineWidth(4.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + var16 * var7 * var4, this.m_renderOriginY + var17 * var7 * var4, 0.0F, this.m_renderOriginX + var14 * var7 * var4, this.m_renderOriginY + var15 * var7 * var4, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + var14 * var7 * var4, this.m_renderOriginY + var15 * var7 * var4, 0.0F, this.m_renderOriginX + var12 * var7 * var4, this.m_renderOriginY + var13 * var7 * var4, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + var12 * var7 * var4, this.m_renderOriginY + var13 * var7 * var4, 0.0F, this.m_renderOriginX + var16 * var7 * var4, this.m_renderOriginY + var17 * var7 * var4, 0.0F, 0.5F, 0.5F, 0.5F, 1.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + var12 * var7 * var4, this.m_renderOriginY + var13 * var7 * var4, 0.0F, this.m_renderOriginX + var10 * var7 * var4, this.m_renderOriginY + var11 * var7 * var4, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
            WorldMapRenderer.m_vboLines.addLine(this.m_renderOriginX + var10 * var7 * var4, this.m_renderOriginY + var11 * var7 * var4, 0.0F, this.m_renderOriginX + var16 * var7 * var4, this.m_renderOriginY + var17 * var7 * var4, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F);
         }

         this.m_rasterizeXY.clear();
         this.m_rasterizeSet.clear();
         this.m_rasterizeMinTileX = (float)((int)((float)(this.m_worldMap.getMinXInSquares() - var1.getMinX()) / var7));
         this.m_rasterizeMinTileY = (float)((int)((float)(this.m_worldMap.getMinYInSquares() - var1.getMinY()) / var7));
         this.m_rasterizeMaxTileX = (float)(this.m_worldMap.getMaxXInSquares() - var1.getMinX()) / var7;
         this.m_rasterizeMaxTileY = (float)(this.m_worldMap.getMaxYInSquares() - var1.getMinY()) / var7;
         this.m_rasterize.scanTriangle(var16, var17, var14, var15, var12, var13, 0, 1000, this::rasterizeTilesCallback);
         this.m_rasterize.scanTriangle(var12, var13, var10, var11, var16, var17, 0, 1000, this::rasterizeTilesCallback);
         if (this.m_rasterizeXY_ints == null || this.m_rasterizeXY_ints.length < this.m_rasterizeXY.size()) {
            this.m_rasterizeXY_ints = new int[this.m_rasterizeXY.size()];
         }

         this.m_rasterizeXY_ints = this.m_rasterizeXY.toArray(this.m_rasterizeXY_ints);
         if (var2) {
            WorldMapRenderer.m_vboLines.setMode(4);

            for(int var18 = 0; var18 < this.m_rasterizeXY.size(); var18 += 2) {
               int var19 = this.m_rasterizeXY.get(var18);
               int var20 = this.m_rasterizeXY.get(var18 + 1);
               float var21 = this.m_renderOriginX + (float)var19 * var7 * var4;
               float var22 = this.m_renderOriginY + (float)var20 * var7 * var4;
               float var23 = this.m_renderOriginX + (float)(var19 + 1) * var7 * var4;
               float var24 = this.m_renderOriginY + (float)(var20 + 1) * var7 * var4;
               WorldMapRenderer.m_vboLines.addElement(var21, var22, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var23, var22, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var21, var24, 0.0F, 0.0F, 1.0F, 0.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var23, var22, 0.0F, 0.0F, 0.0F, 1.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var23, var24, 0.0F, 0.0F, 0.0F, 1.0F, 0.2F);
               WorldMapRenderer.m_vboLines.addElement(var21, var24, 0.0F, 0.0F, 0.0F, 1.0F, 0.2F);
            }

            WorldMapRenderer.m_vboLines.flush();
         }

      }

      void renderImagePyramids() {
         for(int var1 = this.m_worldMap.getImagesCount() - 1; var1 >= 0; --var1) {
            WorldMapImages var2 = this.m_worldMap.getImagesByIndex(var1);
            this.renderImagePyramid(var2);
            GL11.glDisable(3553);
         }

      }

      void renderImagePyramid(WorldMapImages var1) {
         float var2 = this.m_worldScale;
         short var3 = 256;
         int var4 = var1.getZoom(this.m_zoomF);
         float var5 = (float)(var3 * (1 << var4));
         this.calcVisiblePyramidTiles(var1);
         GL11.glEnable(3553);
         GL11.glEnable(3042);
         WorldMapRenderer.m_vboLinesUV.setMode(4);
         int var6 = PZMath.clamp(var1.getMinX(), this.m_worldMap.getMinXInSquares(), this.m_worldMap.getMaxXInSquares());
         int var7 = PZMath.clamp(var1.getMinY(), this.m_worldMap.getMinYInSquares(), this.m_worldMap.getMaxYInSquares());
         int var8 = PZMath.clamp(var1.getMaxX(), this.m_worldMap.getMinXInSquares(), this.m_worldMap.getMaxXInSquares() + 1);
         int var9 = PZMath.clamp(var1.getMaxY(), this.m_worldMap.getMinYInSquares(), this.m_worldMap.getMaxYInSquares() + 1);

         for(int var10 = 0; var10 < this.m_rasterizeXY.size() - 1; var10 += 2) {
            int var11 = this.m_rasterizeXY_ints[var10];
            int var12 = this.m_rasterizeXY_ints[var10 + 1];
            TextureID var13 = var1.getPyramid().getTexture(var11, var12, var4);
            if (var13 != null && var13.isReady()) {
               WorldMapRenderer.m_vboLinesUV.startRun(var13);
               float var14 = (float)var1.getMinX() + (float)var11 * var5;
               float var15 = (float)var1.getMinY() + (float)var12 * var5;
               float var16 = var14 + var5;
               float var17 = var15 + var5;
               float var18 = PZMath.clamp(var14, (float)var6, (float)var8);
               float var19 = PZMath.clamp(var15, (float)var7, (float)var9);
               float var20 = PZMath.clamp(var16, (float)var6, (float)var8);
               float var21 = PZMath.clamp(var17, (float)var7, (float)var9);
               float var22 = (var18 - this.m_centerWorldX) * var2;
               float var23 = (var19 - this.m_centerWorldY) * var2;
               float var24 = (var20 - this.m_centerWorldX) * var2;
               float var25 = (var19 - this.m_centerWorldY) * var2;
               float var26 = (var20 - this.m_centerWorldX) * var2;
               float var27 = (var21 - this.m_centerWorldY) * var2;
               float var28 = (var18 - this.m_centerWorldX) * var2;
               float var29 = (var21 - this.m_centerWorldY) * var2;
               float var30 = (var18 - var14) / var5;
               float var31 = (var19 - var15) / var5;
               float var32 = (var20 - var14) / var5;
               float var33 = (var19 - var15) / var5;
               float var34 = (var20 - var14) / var5;
               float var35 = (var21 - var15) / var5;
               float var36 = (var18 - var14) / var5;
               float var37 = (var21 - var15) / var5;
               float var38 = 1.0F;
               float var39 = 1.0F;
               float var40 = 1.0F;
               float var41 = 1.0F;
               WorldMapRenderer.m_vboLinesUV.addElement(var22, var23, 0.0F, var30, var31, var38, var39, var40, var41);
               WorldMapRenderer.m_vboLinesUV.addElement(var24, var25, 0.0F, var32, var33, var38, var39, var40, var41);
               WorldMapRenderer.m_vboLinesUV.addElement(var28, var29, 0.0F, var36, var37, var38, var39, var40, var41);
               WorldMapRenderer.m_vboLinesUV.addElement(var24, var25, 0.0F, var32, var33, var38, var39, var40, var41);
               WorldMapRenderer.m_vboLinesUV.addElement(var26, var27, 0.0F, var34, var35, var38, var39, var40, var41);
               WorldMapRenderer.m_vboLinesUV.addElement(var28, var29, 0.0F, var36, var37, var38, var39, var40, var41);
               if (this.m_renderer.TileGrid.getValue()) {
                  WorldMapRenderer.m_vboLinesUV.flush();
                  WorldMapRenderer.m_vboLines.setMode(1);
                  WorldMapRenderer.m_vboLines.setLineWidth(2.0F);
                  WorldMapRenderer.m_vboLines.addLine((var14 - this.m_centerWorldX) * var2, (var15 - this.m_centerWorldY) * var2, 0.0F, (var16 - this.m_centerWorldX) * var2, (var15 - this.m_centerWorldY) * var2, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
                  WorldMapRenderer.m_vboLines.addLine((var14 - this.m_centerWorldX) * var2, (var17 - this.m_centerWorldY) * var2, 0.0F, (var16 - this.m_centerWorldX) * var2, (var17 - this.m_centerWorldY) * var2, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
                  WorldMapRenderer.m_vboLines.addLine((var16 - this.m_centerWorldX) * var2, (var15 - this.m_centerWorldY) * var2, 0.0F, (var16 - this.m_centerWorldX) * var2, (var17 - this.m_centerWorldY) * var2, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
                  WorldMapRenderer.m_vboLines.addLine((var14 - this.m_centerWorldX) * var2, (var15 - this.m_centerWorldY) * var2, 0.0F, (var14 - this.m_centerWorldX) * var2, (var17 - this.m_centerWorldY) * var2, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
                  WorldMapRenderer.m_vboLines.flush();
               }
            }
         }

      }

      void renderImagePyramidGrid(WorldMapImages var1) {
         float var2 = this.m_worldScale;
         short var3 = 256;
         int var4 = var1.getZoom(this.m_zoomF);
         float var5 = (float)(var3 * (1 << var4));
         float var6 = ((float)var1.getMinX() - this.m_centerWorldX) * var2;
         float var7 = ((float)var1.getMinY() - this.m_centerWorldY) * var2;
         int var8 = (int)Math.ceil((double)((float)(var1.getMaxX() - var1.getMinX()) / var5));
         int var9 = (int)Math.ceil((double)((float)(var1.getMaxY() - var1.getMinY()) / var5));
         float var10 = var6;
         float var11 = var7;
         float var12 = var6 + (float)var8 * var5 * var2;
         float var13 = var7 + (float)var9 * var5 * var2;
         WorldMapRenderer.m_vboLines.setMode(1);
         WorldMapRenderer.m_vboLines.setLineWidth(2.0F);

         int var14;
         for(var14 = 0; var14 < var8 + 1; ++var14) {
            WorldMapRenderer.m_vboLines.addLine(var6 + (float)var14 * var5 * var2, var11, 0.0F, var6 + (float)var14 * var5 * var2, var13, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
         }

         for(var14 = 0; var14 < var9 + 1; ++var14) {
            WorldMapRenderer.m_vboLines.addLine(var10, var7 + (float)var14 * var5 * var2, 0.0F, var12, var7 + (float)var14 * var5 * var2, 0.0F, 1.0F, 0.0F, 0.0F, 0.5F);
         }

         WorldMapRenderer.m_vboLines.flush();
      }

      float triangleArea(float var1, float var2, float var3, float var4, float var5, float var6) {
         float var7 = Vector2f.length(var3 - var1, var4 - var2);
         float var8 = Vector2f.length(var5 - var3, var6 - var4);
         float var9 = Vector2f.length(var1 - var5, var2 - var6);
         float var10 = (var7 + var8 + var9) / 2.0F;
         return (float)Math.sqrt((double)(var10 * (var10 - var7) * (var10 - var8) * (var10 - var9)));
      }

      void paintAreasOutsideBounds(int var1, int var2, int var3, int var4, float var5) {
         float var6 = this.m_renderOriginX - (float)(var1 % 300) * var5;
         float var7 = this.m_renderOriginY - (float)(var2 % 300) * var5;
         float var8 = this.m_renderOriginX + (float)((this.m_worldMap.getMaxXInCells() + 1) * 300 - var1) * var5;
         float var9 = this.m_renderOriginY + (float)((this.m_worldMap.getMaxYInCells() + 1) * 300 - var2) * var5;
         float var10 = 0.0F;
         WorldMapStyleLayer.RGBAf var11 = this.m_fill;
         float var14;
         if (var1 % 300 != 0) {
            var14 = this.m_renderOriginX;
            WorldMapRenderer.m_vboLines.setMode(4);
            WorldMapRenderer.m_vboLines.addQuad(var6, var7, var14, var9, var10, var11.r, var11.g, var11.b, var11.a);
         }

         float var12;
         if (var2 % 300 != 0) {
            var12 = this.m_renderOriginX;
            var14 = var12 + (float)this.m_worldMap.getWidthInSquares() * this.m_worldScale;
            float var15 = this.m_renderOriginY;
            WorldMapRenderer.m_vboLines.setMode(4);
            WorldMapRenderer.m_vboLines.addQuad(var12, var7, var14, var15, var10, var11.r, var11.g, var11.b, var11.a);
         }

         if (var3 + 1 != 0) {
            var12 = this.m_renderOriginX + (float)(var3 - var1 + 1) * var5;
            WorldMapRenderer.m_vboLines.setMode(4);
            WorldMapRenderer.m_vboLines.addQuad(var12, var7, var8, var9, var10, var11.r, var11.g, var11.b, var11.a);
         }

         if (var4 + 1 != 0) {
            var12 = this.m_renderOriginX;
            float var13 = this.m_renderOriginY + (float)this.m_worldMap.getHeightInSquares() * var5;
            var14 = this.m_renderOriginX + (float)this.m_worldMap.getWidthInSquares() * var5;
            WorldMapRenderer.m_vboLines.setMode(4);
            WorldMapRenderer.m_vboLines.addQuad(var12, var13, var14, var9, var10, var11.r, var11.g, var11.b, var11.a);
         }

      }

      void renderWorldBounds() {
         float var1 = this.m_renderOriginX;
         float var2 = this.m_renderOriginY;
         float var3 = var1 + (float)this.m_worldMap.getWidthInSquares() * this.m_worldScale;
         float var4 = var2 + (float)this.m_worldMap.getHeightInSquares() * this.m_worldScale;
         this.renderDropShadow();
         WorldMapRenderer.m_vboLines.setMode(1);
         WorldMapRenderer.m_vboLines.setLineWidth(2.0F);
         float var5 = 0.5F;
         WorldMapRenderer.m_vboLines.addLine(var1, var2, 0.0F, var3, var2, 0.0F, var5, var5, var5, 1.0F);
         WorldMapRenderer.m_vboLines.addLine(var3, var2, 0.0F, var3, var4, 0.0F, var5, var5, var5, 1.0F);
         WorldMapRenderer.m_vboLines.addLine(var3, var4, 0.0F, var1, var4, 0.0F, var5, var5, var5, 1.0F);
         WorldMapRenderer.m_vboLines.addLine(var1, var4, 0.0F, var1, var2, 0.0F, var5, var5, var5, 1.0F);
      }

      private void renderDropShadow() {
         float var1 = (float)this.m_renderer.m_dropShadowWidth * ((float)this.m_renderer.getHeight() / 1080.0F) * this.m_worldScale / this.m_renderer.getWorldScale(this.m_renderer.getBaseZoom());
         if (!(var1 < 2.0F)) {
            float var2 = this.m_renderOriginX;
            float var3 = this.m_renderOriginY;
            float var4 = var2 + (float)this.m_worldMap.getWidthInSquares() * this.m_worldScale;
            float var5 = var3 + (float)this.m_worldMap.getHeightInSquares() * this.m_worldScale;
            WorldMapRenderer.m_vboLines.setMode(4);
            WorldMapRenderer.m_vboLines.addElement(var2 + var1, var5, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F);
            WorldMapRenderer.m_vboLines.addElement(var4, var5, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F);
            WorldMapRenderer.m_vboLines.addElement(var2 + var1, var5 + var1, 0.0F, 0.5F, 0.5F, 0.5F, 0.0F);
            WorldMapRenderer.m_vboLines.addElement(var4, var5, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F);
            WorldMapRenderer.m_vboLines.addElement(var4 + var1, var5 + var1, 0.0F, 0.5F, 0.5F, 0.5F, 0.0F);
            WorldMapRenderer.m_vboLines.addElement(var2 + var1, var5 + var1, 0.0F, 0.5F, 0.5F, 0.5F, 0.0F);
            WorldMapRenderer.m_vboLines.addElement(var4, var3 + var1, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F);
            WorldMapRenderer.m_vboLines.addElement(var4 + var1, var3 + var1, 0.0F, 0.5F, 0.5F, 0.5F, 0.0F);
            WorldMapRenderer.m_vboLines.addElement(var4, var5, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F);
            WorldMapRenderer.m_vboLines.addElement(var4 + var1, var3 + var1, 0.0F, 0.5F, 0.5F, 0.5F, 0.0F);
            WorldMapRenderer.m_vboLines.addElement(var4 + var1, var5 + var1, 0.0F, 0.5F, 0.5F, 0.5F, 0.0F);
            WorldMapRenderer.m_vboLines.addElement(var4, var5, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F);
         }
      }

      public void postRender() {
         for(int var1 = 0; var1 < this.m_playerRenderData.length; ++var1) {
            WorldMapRenderer.PlayerRenderData var2 = this.m_playerRenderData[var1];
            if (var2.m_modelSlotRenderData != null) {
               var2.m_modelSlotRenderData.postRender();
            }
         }

      }
   }

   private static final class CharacterModelCamera extends ModelCamera {
      float m_worldScale;
      float m_angle;
      float m_playerX;
      float m_playerY;
      boolean m_bVehicle;

      public void Begin() {
         Matrix4f var1 = WorldMapRenderer.allocMatrix4f();
         var1.identity();
         var1.translate(this.m_playerX * this.m_worldScale, this.m_playerY * this.m_worldScale, 0.0F);
         var1.rotateX(1.5707964F);
         var1.rotateY(this.m_angle + 4.712389F);
         if (this.m_bVehicle) {
            var1.scale(this.m_worldScale);
         } else {
            var1.scale(1.5F * this.m_worldScale);
         }

         PZGLUtil.pushAndMultMatrix(5888, var1);
         WorldMapRenderer.releaseMatrix4f(var1);
      }

      public void End() {
         PZGLUtil.popMatrix(5888);
      }
   }

   public final class WorldMapBooleanOption extends BooleanConfigOption {
      public WorldMapBooleanOption(String var2, boolean var3) {
         super(var2, var3);
         WorldMapRenderer.this.options.add(this);
      }
   }

   public final class WorldMapDoubleOption extends DoubleConfigOption {
      public WorldMapDoubleOption(String var2, double var3, double var5, double var7) {
         super(var2, var3, var5, var7);
         WorldMapRenderer.this.options.add(this);
      }
   }

   private static final class PlayerRenderData {
      ModelSlotRenderData m_modelSlotRenderData;
      float m_angle;
      float m_x;
      float m_y;
   }
}
