package zombie.vehicles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;
import se.krka.kahlua.vm.KahluaTable;
import se.krka.kahlua.vm.KahluaUtil;
import zombie.IndieGL;
import zombie.Lua.LuaManager;
import zombie.characters.action.ActionContext;
import zombie.characters.action.ActionGroup;
import zombie.core.BoxedStaticValues;
import zombie.core.Core;
import zombie.core.SpriteRenderer;
import zombie.core.math.PZMath;
import zombie.core.opengl.PZGLUtil;
import zombie.core.opengl.VBOLines;
import zombie.core.skinnedmodel.ModelCamera;
import zombie.core.skinnedmodel.ModelManager;
import zombie.core.skinnedmodel.advancedanimation.AnimNode;
import zombie.core.skinnedmodel.advancedanimation.AnimState;
import zombie.core.skinnedmodel.advancedanimation.AnimatedModel;
import zombie.core.skinnedmodel.advancedanimation.AnimationSet;
import zombie.core.skinnedmodel.animation.AnimationClip;
import zombie.core.skinnedmodel.animation.AnimationMultiTrack;
import zombie.core.skinnedmodel.animation.AnimationPlayer;
import zombie.core.skinnedmodel.animation.AnimationTrack;
import zombie.core.skinnedmodel.animation.Keyframe;
import zombie.core.skinnedmodel.model.Model;
import zombie.core.skinnedmodel.model.SkinningBone;
import zombie.core.skinnedmodel.model.SkinningData;
import zombie.core.skinnedmodel.shader.Shader;
import zombie.core.skinnedmodel.shader.ShaderManager;
import zombie.core.textures.Texture;
import zombie.core.textures.TextureDraw;
import zombie.debug.DebugOptions;
import zombie.debug.LineDrawer;
import zombie.input.Mouse;
import zombie.iso.IsoMovingObject;
import zombie.iso.IsoUtils;
import zombie.iso.Vector2;
import zombie.popman.ObjectPool;
import zombie.scripting.ScriptManager;
import zombie.scripting.objects.ModelAttachment;
import zombie.scripting.objects.ModelScript;
import zombie.scripting.objects.VehicleScript;
import zombie.ui.UIElement;
import zombie.ui.UIFont;
import zombie.ui.UIManager;
import zombie.util.Type;
import zombie.util.list.PZArrayUtil;

public final class UI3DScene extends UIElement {
   private final ArrayList m_objects = new ArrayList();
   private UI3DScene.View m_view;
   private UI3DScene.TransformMode m_transformMode;
   private int m_view_x;
   private int m_view_y;
   private final Vector3f m_viewRotation;
   private int m_zoom;
   private int m_zoomMax;
   private int m_gridDivisions;
   private UI3DScene.GridPlane m_gridPlane;
   private final Matrix4f m_projection;
   private final Matrix4f m_modelView;
   private long VIEW_CHANGE_TIME;
   private long m_viewChangeTime;
   private final Quaternionf m_modelViewChange;
   private boolean m_bDrawGrid;
   private boolean m_bDrawGridAxes;
   private final UI3DScene.CharacterSceneModelCamera m_CharacterSceneModelCamera;
   private final UI3DScene.VehicleSceneModelCamera m_VehicleSceneModelCamera;
   private static final ObjectPool s_SetModelCameraPool = new ObjectPool(UI3DScene.SetModelCamera::new);
   private final UI3DScene.StateData[] m_stateData;
   private UI3DScene.Gizmo m_gizmo;
   private final UI3DScene.RotateGizmo m_rotateGizmo;
   private final UI3DScene.ScaleGizmo m_scaleGizmo;
   private final UI3DScene.TranslateGizmo m_translateGizmo;
   private final Vector3f m_gizmoPos;
   private final Vector3f m_gizmoRotate;
   private UI3DScene.SceneObject m_gizmoParent;
   private UI3DScene.SceneObject m_gizmoOrigin;
   private UI3DScene.SceneObject m_gizmoChild;
   private final UI3DScene.OriginAttachment m_originAttachment;
   private final UI3DScene.OriginBone m_originBone;
   private final UI3DScene.OriginGizmo m_originGizmo;
   private float m_gizmoScale;
   private String m_selectedAttachment;
   private final ArrayList m_axes;
   private final UI3DScene.OriginBone m_highlightBone;
   private static final ObjectPool s_posRotPool = new ObjectPool(UI3DScene.PositionRotation::new);
   private final ArrayList m_aabb;
   private static final ObjectPool s_aabbPool = new ObjectPool(UI3DScene.AABB::new);
   private final ArrayList m_box3D;
   private static final ObjectPool s_box3DPool = new ObjectPool(UI3DScene.Box3D::new);
   final Vector3f tempVector3f;
   final Vector4f tempVector4f;
   final int[] m_viewport;
   private final float GRID_DARK;
   private final float GRID_LIGHT;
   private float GRID_ALPHA;
   private final int HALF_GRID;
   private static final VBOLines vboLines = new VBOLines();
   private static final ThreadLocal TL_Ray_pool = ThreadLocal.withInitial(UI3DScene.RayObjectPool::new);
   private static final ThreadLocal TL_Plane_pool = ThreadLocal.withInitial(UI3DScene.PlaneObjectPool::new);
   static final float SMALL_NUM = 1.0E-8F;

   public UI3DScene(KahluaTable var1) {
      super(var1);
      this.m_view = UI3DScene.View.Right;
      this.m_transformMode = UI3DScene.TransformMode.Local;
      this.m_view_x = 0;
      this.m_view_y = 0;
      this.m_viewRotation = new Vector3f();
      this.m_zoom = 3;
      this.m_zoomMax = 10;
      this.m_gridDivisions = 1;
      this.m_gridPlane = UI3DScene.GridPlane.YZ;
      this.m_projection = new Matrix4f();
      this.m_modelView = new Matrix4f();
      this.VIEW_CHANGE_TIME = 350L;
      this.m_modelViewChange = new Quaternionf();
      this.m_bDrawGrid = true;
      this.m_bDrawGridAxes = false;
      this.m_CharacterSceneModelCamera = new UI3DScene.CharacterSceneModelCamera();
      this.m_VehicleSceneModelCamera = new UI3DScene.VehicleSceneModelCamera();
      this.m_stateData = new UI3DScene.StateData[3];
      this.m_rotateGizmo = new UI3DScene.RotateGizmo();
      this.m_scaleGizmo = new UI3DScene.ScaleGizmo();
      this.m_translateGizmo = new UI3DScene.TranslateGizmo();
      this.m_gizmoPos = new Vector3f();
      this.m_gizmoRotate = new Vector3f();
      this.m_gizmoParent = null;
      this.m_gizmoOrigin = null;
      this.m_gizmoChild = null;
      this.m_originAttachment = new UI3DScene.OriginAttachment(this);
      this.m_originBone = new UI3DScene.OriginBone(this);
      this.m_originGizmo = new UI3DScene.OriginGizmo(this);
      this.m_gizmoScale = 1.0F;
      this.m_selectedAttachment = null;
      this.m_axes = new ArrayList();
      this.m_highlightBone = new UI3DScene.OriginBone(this);
      this.m_aabb = new ArrayList();
      this.m_box3D = new ArrayList();
      this.tempVector3f = new Vector3f();
      this.tempVector4f = new Vector4f();
      this.m_viewport = new int[]{0, 0, 0, 0};
      this.GRID_DARK = 0.1F;
      this.GRID_LIGHT = 0.2F;
      this.GRID_ALPHA = 1.0F;
      this.HALF_GRID = 5;

      for(int var2 = 0; var2 < this.m_stateData.length; ++var2) {
         this.m_stateData[var2] = new UI3DScene.StateData();
         this.m_stateData[var2].m_overlaysDrawer = new UI3DScene.OverlaysDrawer();
      }

   }

   UI3DScene.SceneObject getSceneObjectById(String var1, boolean var2) {
      for(int var3 = 0; var3 < this.m_objects.size(); ++var3) {
         UI3DScene.SceneObject var4 = (UI3DScene.SceneObject)this.m_objects.get(var3);
         if (var4.m_id.equalsIgnoreCase(var1)) {
            return var4;
         }
      }

      if (var2) {
         throw new NullPointerException("scene object \"" + var1 + "\" not found");
      } else {
         return null;
      }
   }

   Object getSceneObjectById(String var1, Class var2, boolean var3) {
      for(int var4 = 0; var4 < this.m_objects.size(); ++var4) {
         UI3DScene.SceneObject var5 = (UI3DScene.SceneObject)this.m_objects.get(var4);
         if (var5.m_id.equalsIgnoreCase(var1)) {
            if (var5.getClass() == var2) {
               return var2.cast(var5);
            }

            if (var3) {
               throw new ClassCastException("scene object \"" + var1 + "\" is " + var5.getClass().getSimpleName() + " expected " + var2.getSimpleName());
            }
         }
      }

      if (var3) {
         throw new NullPointerException("scene object \"" + var1 + "\" not found");
      } else {
         return null;
      }
   }

   public void render() {
      if (this.isVisible()) {
         super.render();
         IndieGL.glClear(256);
         UI3DScene.StateData var1 = this.stateDataMain();
         this.calcMatrices(this.m_projection, this.m_modelView);
         var1.m_projection.set((Matrix4fc)this.m_projection);
         long var2 = System.currentTimeMillis();
         float var4;
         if (this.m_viewChangeTime + this.VIEW_CHANGE_TIME > var2) {
            var4 = (float)(this.m_viewChangeTime + this.VIEW_CHANGE_TIME - var2) / (float)this.VIEW_CHANGE_TIME;
            Quaternionf var5 = allocQuaternionf().setFromUnnormalized((Matrix4fc)this.m_modelView);
            var1.m_modelView.set((Quaternionfc)this.m_modelViewChange.slerp(var5, 1.0F - var4));
            releaseQuaternionf(var5);
         } else {
            var1.m_modelView.set((Matrix4fc)this.m_modelView);
         }

         var1.m_zoom = this.m_zoom;
         PZArrayUtil.forEach((List)var1.m_objectData, UI3DScene.SceneObjectRenderData::release);
         var1.m_objectData.clear();

         for(int var11 = 0; var11 < this.m_objects.size(); ++var11) {
            UI3DScene.SceneObject var12 = (UI3DScene.SceneObject)this.m_objects.get(var11);
            if (var12.m_visible) {
               if (var12.m_autoRotate) {
                  var12.m_autoRotateAngle = (float)((double)var12.m_autoRotateAngle + UIManager.getMillisSinceLastRender() / 30.0D);
                  if (var12.m_autoRotateAngle > 360.0F) {
                     var12.m_autoRotateAngle = 0.0F;
                  }
               }

               UI3DScene.SceneObjectRenderData var6 = var12.renderMain();
               if (var6 != null) {
                  var1.m_objectData.add(var6);
               }
            }
         }

         var4 = (float)(Mouse.getXA() - this.getAbsoluteX().intValue());
         float var13 = (float)(Mouse.getYA() - this.getAbsoluteY().intValue());
         var1.m_gizmo = this.m_gizmo;
         if (this.m_gizmo != null) {
            var1.m_gizmoTranslate.set((Vector3fc)this.m_gizmoPos);
            var1.m_gizmoRotate.set((Vector3fc)this.m_gizmoRotate);
            var1.m_gizmoTransform.translation(this.m_gizmoPos);
            var1.m_gizmoTransform.rotateXYZ(this.m_gizmoRotate.x * 0.017453292F, this.m_gizmoRotate.y * 0.017453292F, this.m_gizmoRotate.z * 0.017453292F);
            var1.m_gizmoAxis = this.m_gizmo.hitTest(var4, var13);
         }

         var1.m_gizmoChildTransform.identity();
         var1.m_selectedAttachmentIsChildAttachment = this.m_gizmoChild != null && this.m_gizmoChild.m_attachment != null && this.m_gizmoChild.m_attachment.equals(this.m_selectedAttachment);
         if (this.m_gizmoChild != null) {
            this.m_gizmoChild.getLocalTransform(var1.m_gizmoChildTransform);
         }

         var1.m_gizmoOriginTransform.identity();
         var1.m_hasGizmoOrigin = this.m_gizmoOrigin != null;
         if (this.m_gizmoOrigin != null && this.m_gizmoOrigin != this.m_gizmoParent) {
            this.m_gizmoOrigin.getGlobalTransform(var1.m_gizmoOriginTransform);
         }

         var1.m_gizmoParentTransform.identity();
         if (this.m_gizmoParent != null) {
            this.m_gizmoParent.getGlobalTransform(var1.m_gizmoParentTransform);
         }

         var1.m_overlaysDrawer.init();
         SpriteRenderer.instance.drawGeneric(var1.m_overlaysDrawer);
         Vector3f var7;
         Vector3f var14;
         if (this.m_bDrawGrid) {
            var14 = this.uiToScene(var4, var13, 0.0F, this.tempVector3f);
            if (this.m_view == UI3DScene.View.UserDefined) {
               var7 = allocVector3f();
               switch(this.m_gridPlane) {
               case XY:
                  var7.set(0.0F, 0.0F, 1.0F);
                  break;
               case XZ:
                  var7.set(0.0F, 1.0F, 0.0F);
                  break;
               case YZ:
                  var7.set(1.0F, 0.0F, 0.0F);
               }

               Vector3f var8 = allocVector3f().set(0.0F);
               UI3DScene.Plane var9 = allocPlane().set(var7, var8);
               releaseVector3f(var7);
               releaseVector3f(var8);
               UI3DScene.Ray var10 = this.getCameraRay(var4, (float)this.screenHeight() - var13, allocRay());
               if (this.intersect_ray_plane(var9, var10, var14) != 1) {
                  var14.set(0.0F);
               }

               releasePlane(var9);
               releaseRay(var10);
            }

            var14.x = (float)Math.round(var14.x * this.gridMult()) / this.gridMult();
            var14.y = (float)Math.round(var14.y * this.gridMult()) / this.gridMult();
            var14.z = (float)Math.round(var14.z * this.gridMult()) / this.gridMult();
            this.DrawText(UIFont.Small, String.valueOf(var14.x), (double)(this.width - 200.0F), 10.0D, 1.0D, 0.0D, 0.0D, 1.0D);
            this.DrawText(UIFont.Small, String.valueOf(var14.y), (double)(this.width - 150.0F), 10.0D, 0.0D, 1.0D, 0.0D, 1.0D);
            this.DrawText(UIFont.Small, String.valueOf(var14.z), (double)(this.width - 100.0F), 10.0D, 0.0D, 0.5D, 1.0D, 1.0D);
         }

         float var17;
         if (this.m_gizmo == this.m_rotateGizmo && this.m_rotateGizmo.m_trackAxis != UI3DScene.Axis.None) {
            var14 = this.m_rotateGizmo.m_startXfrm.getTranslation(allocVector3f());
            float var16 = this.sceneToUIX(var14.x, var14.y, var14.z);
            var17 = this.sceneToUIY(var14.x, var14.y, var14.z);
            LineDrawer.drawLine(var16, var17, var4, var13, 0.5F, 0.5F, 0.5F, 1.0F, 1);
            releaseVector3f(var14);
         }

         if (this.m_highlightBone.m_boneName != null) {
            Matrix4f var15 = this.m_highlightBone.getGlobalTransform(allocMatrix4f());
            this.m_highlightBone.m_character.getGlobalTransform(allocMatrix4f()).mul((Matrix4fc)var15, var15);
            var7 = var15.getTranslation(allocVector3f());
            var17 = this.sceneToUIX(var7.x, var7.y, var7.z);
            float var18 = this.sceneToUIY(var7.x, var7.y, var7.z);
            LineDrawer.drawCircle(var17, var18, 10.0F, 16, 1.0F, 1.0F, 1.0F);
            releaseVector3f(var7);
            releaseMatrix4f(var15);
         }

      }
   }

   private float gridMult() {
      return (float)(100 * this.m_gridDivisions);
   }

   private float zoomMult() {
      return (float)Math.exp((double)((float)this.m_zoom * 0.2F)) * 160.0F / Math.max(1.82F, 1.0F);
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

   public Object fromLua0(String var1) {
      byte var3 = -1;
      switch(var1.hashCode()) {
      case -2076678364:
         if (var1.equals("clearBox3Ds")) {
            var3 = 2;
         }
         break;
      case -1899201780:
         if (var1.equals("getGridMult")) {
            var3 = 6;
         }
         break;
      case -1271454958:
         if (var1.equals("clearAxes")) {
            var3 = 1;
         }
         break;
      case -800815632:
         if (var1.equals("getGizmoPos")) {
            var3 = 5;
         }
         break;
      case -762071578:
         if (var1.equals("clearAABBs")) {
            var3 = 0;
         }
         break;
      case -262619928:
         if (var1.equals("clearGizmoRotate")) {
            var3 = 3;
         }
         break;
      case -75062501:
         if (var1.equals("getView")) {
            var3 = 7;
         }
         break;
      case 424225647:
         if (var1.equals("stopGizmoTracking")) {
            var3 = 10;
         }
         break;
      case 921608267:
         if (var1.equals("clearHighlightBone")) {
            var3 = 4;
         }
         break;
      case 1289669561:
         if (var1.equals("getViewRotation")) {
            var3 = 8;
         }
         break;
      case 1781313372:
         if (var1.equals("getModelCount")) {
            var3 = 9;
         }
      }

      switch(var3) {
      case 0:
         s_aabbPool.release((List)this.m_aabb);
         this.m_aabb.clear();
         return null;
      case 1:
         s_posRotPool.release((List)this.m_axes);
         this.m_axes.clear();
         return null;
      case 2:
         s_box3DPool.release((List)this.m_box3D);
         this.m_box3D.clear();
         return null;
      case 3:
         this.m_gizmoRotate.set(0.0F);
         return null;
      case 4:
         this.m_highlightBone.m_boneName = null;
         return null;
      case 5:
         return this.m_gizmoPos;
      case 6:
         return BoxedStaticValues.toDouble((double)this.gridMult());
      case 7:
         return this.m_view.name();
      case 8:
         return this.m_viewRotation;
      case 9:
         int var4 = 0;

         for(int var5 = 0; var5 < this.m_objects.size(); ++var5) {
            if (this.m_objects.get(var5) instanceof UI3DScene.SceneModel) {
               ++var4;
            }
         }

         return BoxedStaticValues.toDouble((double)var4);
      case 10:
         if (this.m_gizmo != null) {
            this.m_gizmo.stopTracking();
         }

         return null;
      default:
         throw new IllegalArgumentException("unhandled \"" + var1 + "\"");
      }
   }

   public Object fromLua1(String var1, Object var2) {
      byte var4 = -1;
      switch(var1.hashCode()) {
      case -1987781608:
         if (var1.equals("setGridMult")) {
            var4 = 21;
         }
         break;
      case -1759645365:
         if (var1.equals("isCharacterFemale")) {
            var4 = 11;
         }
         break;
      case -1706380656:
         if (var1.equals("createVehicle")) {
            var4 = 1;
         }
         break;
      case -1494527684:
         if (var1.equals("getObjectTranslation")) {
            var4 = 9;
         }
         break;
      case -1489195916:
         if (var1.equals("setGridPlane")) {
            var4 = 22;
         }
         break;
      case -1030736685:
         if (var1.equals("getObjectRotation")) {
            var4 = 8;
         }
         break;
      case -889395460:
         if (var1.equals("setGizmoPos")) {
            var4 = 17;
         }
         break;
      case -851056855:
         if (var1.equals("isObjectVisible")) {
            var4 = 12;
         }
         break;
      case -682454758:
         if (var1.equals("setGizmoVisible")) {
            var4 = 20;
         }
         break;
      case -477006699:
         if (var1.equals("setMaxZoom")) {
            var4 = 23;
         }
         break;
      case -371350035:
         if (var1.equals("setTransformMode")) {
            var4 = 25;
         }
         break;
      case -352953986:
         if (var1.equals("setGizmoOrigin")) {
            var4 = 16;
         }
         break;
      case -310328059:
         if (var1.equals("removeModel")) {
            var4 = 13;
         }
         break;
      case -269514829:
         if (var1.equals("setGizmoRotate")) {
            var4 = 18;
         }
         break;
      case -166970338:
         if (var1.equals("getModelScript")) {
            var4 = 4;
         }
         break;
      case -8145934:
         if (var1.equals("setGizmoScale")) {
            var4 = 19;
         }
         break;
      case 3744723:
         if (var1.equals("zoom")) {
            var4 = 28;
         }
         break;
      case 24704865:
         if (var1.equals("getVehicleScript")) {
            var4 = 10;
         }
         break;
      case 99063181:
         if (var1.equals("createCharacter")) {
            var4 = 0;
         }
         break;
      case 143181344:
         if (var1.equals("setSelectedAttachment")) {
            var4 = 24;
         }
         break;
      case 241532735:
         if (var1.equals("getObjectParent")) {
            var4 = 6;
         }
         break;
      case 786690498:
         if (var1.equals("getObjectParentAttachment")) {
            var4 = 7;
         }
         break;
      case 1162053932:
         if (var1.equals("setDrawGrid")) {
            var4 = 14;
         }
         break;
      case 1257868287:
         if (var1.equals("getObjectAutoRotate")) {
            var4 = 5;
         }
         break;
      case 1432957189:
         if (var1.equals("getCharacterAnimationDuration")) {
            var4 = 2;
         }
         break;
      case 1985047079:
         if (var1.equals("setView")) {
            var4 = 27;
         }
         break;
      case 1985172309:
         if (var1.equals("setZoom")) {
            var4 = 26;
         }
         break;
      case 2028105329:
         if (var1.equals("setDrawGridAxes")) {
            var4 = 15;
         }
         break;
      case 2079364798:
         if (var1.equals("getCharacterAnimationTime")) {
            var4 = 3;
         }
      }

      int var5;
      String var13;
      Vector3f var14;
      UI3DScene.SceneObject var19;
      byte var20;
      AnimationPlayer var21;
      UI3DScene.SceneCharacter var22;
      AnimationMultiTrack var28;
      switch(var4) {
      case 0:
         var19 = this.getSceneObjectById((String)var2, false);
         if (var19 != null) {
            throw new IllegalStateException("scene object \"" + var2 + "\" exists");
         }

         UI3DScene.SceneCharacter var25 = new UI3DScene.SceneCharacter(this, (String)var2);
         this.m_objects.add(var25);
         return var25;
      case 1:
         var19 = this.getSceneObjectById((String)var2, false);
         if (var19 != null) {
            throw new IllegalStateException("scene object \"" + var2 + "\" exists");
         }

         UI3DScene.SceneVehicle var24 = new UI3DScene.SceneVehicle(this, (String)var2);
         this.m_objects.add(var24);
         return null;
      case 2:
         var22 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var21 = var22.m_animatedModel.getAnimationPlayer();
         if (var21 == null) {
            return null;
         } else {
            var28 = var21.getMultiTrack();
            if (var28 != null && !var28.getTracks().isEmpty()) {
               return KahluaUtil.toDouble((double)((AnimationTrack)var28.getTracks().get(0)).getDuration());
            }

            return null;
         }
      case 3:
         var22 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var21 = var22.m_animatedModel.getAnimationPlayer();
         if (var21 == null) {
            return null;
         } else {
            var28 = var21.getMultiTrack();
            if (var28 != null && !var28.getTracks().isEmpty()) {
               return KahluaUtil.toDouble((double)((AnimationTrack)var28.getTracks().get(0)).getCurrentTimeValue());
            }

            return null;
         }
      case 4:
         var5 = 0;

         for(int var17 = 0; var17 < this.m_objects.size(); ++var17) {
            UI3DScene.SceneModel var27 = (UI3DScene.SceneModel)Type.tryCastTo((UI3DScene.SceneObject)this.m_objects.get(var17), UI3DScene.SceneModel.class);
            if (var27 != null && var5++ == ((Double)var2).intValue()) {
               return var27.m_modelScript;
            }
         }

         return null;
      case 5:
         var19 = this.getSceneObjectById((String)var2, true);
         return var19.m_autoRotate ? Boolean.TRUE : Boolean.FALSE;
      case 6:
         var19 = this.getSceneObjectById((String)var2, true);
         return var19.m_parent == null ? null : var19.m_parent.m_id;
      case 7:
         var19 = this.getSceneObjectById((String)var2, true);
         return var19.m_parentAttachment;
      case 8:
         var19 = this.getSceneObjectById((String)var2, true);
         return var19.m_rotate;
      case 9:
         var19 = this.getSceneObjectById((String)var2, true);
         return var19.m_translate;
      case 10:
         UI3DScene.SceneVehicle var23 = (UI3DScene.SceneVehicle)this.getSceneObjectById((String)var2, UI3DScene.SceneVehicle.class, true);
         return var23.m_script;
      case 11:
         var22 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         return var22.m_animatedModel.isFemale();
      case 12:
         var19 = this.getSceneObjectById((String)var2, true);
         return var19.m_visible ? Boolean.TRUE : Boolean.FALSE;
      case 13:
         UI3DScene.SceneModel var18 = (UI3DScene.SceneModel)this.getSceneObjectById((String)var2, UI3DScene.SceneModel.class, true);
         this.m_objects.remove(var18);
         Iterator var16 = this.m_objects.iterator();

         while(var16.hasNext()) {
            UI3DScene.SceneObject var26 = (UI3DScene.SceneObject)var16.next();
            if (var26.m_parent == var18) {
               var26.m_attachment = null;
               var26.m_parent = null;
               var26.m_parentAttachment = null;
            }
         }

         return null;
      case 14:
         this.m_bDrawGrid = (Boolean)var2;
         return null;
      case 15:
         this.m_bDrawGridAxes = (Boolean)var2;
         return null;
      case 16:
         var13 = (String)var2;
         var20 = -1;
         switch(var13.hashCode()) {
         case 3387192:
            if (var13.equals("none")) {
               var20 = 0;
            }
         default:
            switch(var20) {
            case 0:
               this.m_gizmoParent = null;
               this.m_gizmoOrigin = null;
               this.m_gizmoChild = null;
            default:
               return null;
            }
         }
      case 17:
         var14 = (Vector3f)var2;
         if (!this.m_gizmoPos.equals(var14)) {
            this.m_gizmoPos.set((Vector3fc)var14);
         }

         return null;
      case 18:
         var14 = (Vector3f)var2;
         if (!this.m_gizmoRotate.equals(var14)) {
            this.m_gizmoRotate.set((Vector3fc)var14);
         }

         return null;
      case 19:
         this.m_gizmoScale = Math.max(((Double)var2).floatValue(), 0.01F);
         return null;
      case 20:
         var13 = (String)var2;
         this.m_rotateGizmo.m_visible = "rotate".equalsIgnoreCase(var13);
         this.m_scaleGizmo.m_visible = "scale".equalsIgnoreCase(var13);
         this.m_translateGizmo.m_visible = "translate".equalsIgnoreCase(var13);
         var20 = -1;
         switch(var13.hashCode()) {
         case -925180581:
            if (var13.equals("rotate")) {
               var20 = 0;
            }
            break;
         case 109250890:
            if (var13.equals("scale")) {
               var20 = 1;
            }
            break;
         case 1052832078:
            if (var13.equals("translate")) {
               var20 = 2;
            }
         }

         switch(var20) {
         case 0:
            this.m_gizmo = this.m_rotateGizmo;
            break;
         case 1:
            this.m_gizmo = this.m_scaleGizmo;
            break;
         case 2:
            this.m_gizmo = this.m_translateGizmo;
            break;
         default:
            this.m_gizmo = null;
         }

         return null;
      case 21:
         this.m_gridDivisions = PZMath.clamp(((Double)var2).intValue(), 1, 100);
         return null;
      case 22:
         this.m_gridPlane = UI3DScene.GridPlane.valueOf((String)var2);
         return null;
      case 23:
         this.m_zoomMax = PZMath.clamp(((Double)var2).intValue(), 1, 20);
         return null;
      case 24:
         this.m_selectedAttachment = (String)var2;
         return null;
      case 25:
         this.m_transformMode = UI3DScene.TransformMode.valueOf((String)var2);
         return null;
      case 26:
         this.m_zoom = PZMath.clamp(((Double)var2).intValue(), 1, this.m_zoomMax);
         this.calcMatrices(this.m_projection, this.m_modelView);
         return null;
      case 27:
         UI3DScene.View var12 = this.m_view;
         this.m_view = UI3DScene.View.valueOf((String)var2);
         if (var12 != this.m_view) {
            long var15 = System.currentTimeMillis();
            if (this.m_viewChangeTime + this.VIEW_CHANGE_TIME < var15) {
               this.m_modelViewChange.setFromUnnormalized((Matrix4fc)this.m_modelView);
            }

            this.m_viewChangeTime = var15;
         }

         this.calcMatrices(this.m_projection, this.m_modelView);
         return null;
      case 28:
         var5 = -((Double)var2).intValue();
         float var6 = (float)(Mouse.getXA() - this.getAbsoluteX().intValue());
         float var7 = (float)(Mouse.getYA() - this.getAbsoluteY().intValue());
         float var8 = this.uiToSceneX(var6, var7);
         float var9 = this.uiToSceneY(var6, var7);
         this.m_zoom = PZMath.clamp(this.m_zoom + var5, 1, this.m_zoomMax);
         this.calcMatrices(this.m_projection, this.m_modelView);
         float var10 = this.uiToSceneX(var6, var7);
         float var11 = this.uiToSceneY(var6, var7);
         this.m_view_x = (int)((float)this.m_view_x - (var10 - var8) * this.zoomMult());
         this.m_view_y = (int)((float)this.m_view_y + (var11 - var9) * this.zoomMult());
         this.calcMatrices(this.m_projection, this.m_modelView);
         return null;
      default:
         throw new IllegalArgumentException(String.format("unhandled \"%s\" \"%s\"", var1, var2));
      }
   }

   public Object fromLua2(String var1, Object var2, Object var3) {
      byte var5 = -1;
      switch(var1.hashCode()) {
      case -2087654637:
         if (var1.equals("setCharacterFemale")) {
            var5 = 15;
         }
         break;
      case -1943463772:
         if (var1.equals("addAttachment")) {
            var5 = 0;
         }
         break;
      case -1806737963:
         if (var1.equals("setVehicleScript")) {
            var5 = 23;
         }
         break;
      case -1673743631:
         if (var1.equals("setObjectVisible")) {
            var5 = 22;
         }
         break;
      case -1409424298:
         if (var1.equals("setHighlightBone")) {
            var5 = 20;
         }
         break;
      case -1372814515:
         if (var1.equals("setCharacterAnimationClip")) {
            var5 = 10;
         }
         break;
      case -1372310838:
         if (var1.equals("setCharacterAnimationTime")) {
            var5 = 12;
         }
         break;
      case -928360249:
         if (var1.equals("removeAttachment")) {
            var5 = 7;
         }
         break;
      case -914984949:
         if (var1.equals("setCharacterShowBones")) {
            var5 = 16;
         }
         break;
      case -903033673:
         if (var1.equals("setCharacterAlpha")) {
            var5 = 8;
         }
         break;
      case -886186006:
         if (var1.equals("setCharacterState")) {
            var5 = 19;
         }
         break;
      case -841604615:
         if (var1.equals("dragView")) {
            var5 = 5;
         }
         break;
      case -510351475:
         if (var1.equals("createModel")) {
            var5 = 3;
         }
         break;
      case -352953986:
         if (var1.equals("setGizmoOrigin")) {
            var5 = 18;
         }
         break;
      case -333772122:
         if (var1.equals("dragGizmo")) {
            var5 = 4;
         }
         break;
      case -285859573:
         if (var1.equals("setObjectAutoRotate")) {
            var5 = 21;
         }
         break;
      case -181033558:
         if (var1.equals("setCharacterAnimSet")) {
            var5 = 13;
         }
         break;
      case -181019654:
         if (var1.equals("setCharacterAnimate")) {
            var5 = 9;
         }
         break;
      case 407314410:
         if (var1.equals("setCharacterAnimationSpeed")) {
            var5 = 11;
         }
         break;
      case 886449800:
         if (var1.equals("applyDeltaRotation")) {
            var5 = 2;
         }
         break;
      case 995639366:
         if (var1.equals("addBoneAxis")) {
            var5 = 1;
         }
         break;
      case 1348886365:
         if (var1.equals("setCharacterClearDepthBuffer")) {
            var5 = 14;
         }
         break;
      case 1349998759:
         if (var1.equals("getCharacterAnimationKeyframeTimes")) {
            var5 = 6;
         }
         break;
      case 1623389641:
         if (var1.equals("testGizmoAxis")) {
            var5 = 24;
         }
         break;
      case 1728054510:
         if (var1.equals("setCharacterUseDeferredMovement")) {
            var5 = 17;
         }
      }

      int var6;
      int var7;
      UI3DScene.SceneObject var17;
      UI3DScene.SceneCharacter var18;
      String var19;
      AnimationPlayer var22;
      ModelAttachment var25;
      UI3DScene.SceneModel var26;
      AnimationMultiTrack var32;
      UI3DScene.SceneModel var38;
      switch(var5) {
      case 0:
         var38 = (UI3DScene.SceneModel)this.getSceneObjectById((String)var2, UI3DScene.SceneModel.class, true);
         if (var38.m_modelScript.getAttachmentById((String)var3) != null) {
            throw new IllegalArgumentException("model script \"" + var2 + "\" already has attachment named \"" + var3 + "\"");
         }

         var25 = new ModelAttachment((String)var3);
         var38.m_modelScript.addAttachment(var25);
         return var25;
      case 1:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var19 = (String)var3;
         UI3DScene.PositionRotation var41 = var18.getBoneAxis(var19, (UI3DScene.PositionRotation)s_posRotPool.alloc());
         this.m_axes.add(var41);
         return null;
      case 2:
         Vector3f var43 = (Vector3f)var2;
         Vector3f var34 = (Vector3f)var3;
         Quaternionf var40 = allocQuaternionf().rotationXYZ(var43.x * 0.017453292F, var43.y * 0.017453292F, var43.z * 0.017453292F);
         Quaternionf var37 = allocQuaternionf().rotationXYZ(var34.x * 0.017453292F, var34.y * 0.017453292F, var34.z * 0.017453292F);
         var40.mul(var37);
         var40.getEulerAnglesXYZ(var43);
         releaseQuaternionf(var40);
         releaseQuaternionf(var37);
         var43.mul(57.295776F);
         var43.x = (float)Math.floor((double)(var43.x + 0.5F));
         var43.y = (float)Math.floor((double)(var43.y + 0.5F));
         var43.z = (float)Math.floor((double)(var43.z + 0.5F));
         return var43;
      case 3:
         var17 = this.getSceneObjectById((String)var2, false);
         if (var17 != null) {
            throw new IllegalStateException("scene object \"" + var2 + "\" exists");
         } else {
            ModelScript var31 = ScriptManager.instance.getModelScript((String)var3);
            if (var31 == null) {
               throw new NullPointerException("model script \"" + var3 + "\" not found");
            } else {
               Model var39 = ModelManager.instance.getLoadedModel((String)var3);
               if (var39 == null) {
                  throw new NullPointerException("model \"" + var3 + "\" not found");
               }

               var26 = new UI3DScene.SceneModel(this, (String)var2, var31, var39);
               this.m_objects.add(var26);
               return null;
            }
         }
      case 4:
         float var42 = ((Double)var2).floatValue();
         float var28 = ((Double)var3).floatValue();
         if (this.m_gizmo == null) {
            throw new NullPointerException("gizmo is null");
         }

         this.m_gizmo.updateTracking(var42, var28);
         return null;
      case 5:
         var6 = ((Double)var2).intValue();
         var7 = ((Double)var3).intValue();
         this.m_view_x -= var6;
         this.m_view_y -= var7;
         this.calcMatrices(this.m_projection, this.m_modelView);
         return null;
      case 6:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var22 = var18.m_animatedModel.getAnimationPlayer();
         if (var22 == null) {
            return null;
         } else {
            var32 = var22.getMultiTrack();
            if (var32 != null && !var32.getTracks().isEmpty()) {
               AnimationTrack var36 = (AnimationTrack)var32.getTracks().get(0);
               AnimationClip var10 = var36.getClip();
               if (var10 == null) {
                  return null;
               }

               if (var3 == null) {
                  var3 = new ArrayList();
               }

               ArrayList var11 = (ArrayList)var3;
               var11.clear();
               Keyframe[] var12 = var10.getKeyframes();

               for(int var13 = 0; var13 < var12.length; ++var13) {
                  Keyframe var14 = var12[var13];
                  Double var15 = KahluaUtil.toDouble((double)var14.Time);
                  if (!var11.contains(var15)) {
                     var11.add(var15);
                  }
               }

               return var11;
            }

            return null;
         }
      case 7:
         var38 = (UI3DScene.SceneModel)this.getSceneObjectById((String)var2, UI3DScene.SceneModel.class, true);
         var25 = var38.m_modelScript.getAttachmentById((String)var3);
         if (var25 == null) {
            throw new IllegalArgumentException("model script \"" + var2 + "\" attachment \"" + var3 + "\" not found");
         }

         var38.m_modelScript.removeAttachment(var25);
         return null;
      case 8:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var18.m_animatedModel.setAlpha(((Double)var3).floatValue());
         return null;
      case 9:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var18.m_animatedModel.setAnimate((Boolean)var3);
         return null;
      case 10:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         AnimationSet var24 = AnimationSet.GetAnimationSet(var18.m_animatedModel.GetAnimSetName(), false);
         if (var24 == null) {
            return null;
         } else {
            AnimState var35 = var24.GetState(var18.m_animatedModel.getState());
            if (var35 != null && !var35.m_Nodes.isEmpty()) {
               AnimNode var33 = (AnimNode)var35.m_Nodes.get(0);
               var33.m_AnimName = (String)var3;
               var18.m_animatedModel.getAdvancedAnimator().OnAnimDataChanged(false);
               var18.m_animatedModel.getAdvancedAnimator().SetState(var35.m_Name);
               return null;
            }

            return null;
         }
      case 11:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         AnimationMultiTrack var23 = var18.m_animatedModel.getAnimationPlayer().getMultiTrack();
         if (var23.getTracks().isEmpty()) {
            return null;
         }

         ((AnimationTrack)var23.getTracks().get(0)).SpeedDelta = PZMath.clamp(((Double)var3).floatValue(), 0.0F, 10.0F);
         return null;
      case 12:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var18.m_animatedModel.setTrackTime(((Double)var3).floatValue());
         var22 = var18.m_animatedModel.getAnimationPlayer();
         if (var22 == null) {
            return null;
         } else {
            var32 = var22.getMultiTrack();
            if (var32 != null && !var32.getTracks().isEmpty()) {
               ((AnimationTrack)var32.getTracks().get(0)).setCurrentTimeValue(((Double)var3).floatValue());
               return null;
            }

            return null;
         }
      case 13:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var19 = (String)var3;
         if (!var19.equals(var18.m_animatedModel.GetAnimSetName())) {
            var18.m_animatedModel.setAnimSetName(var19);
            var18.m_animatedModel.getAdvancedAnimator().OnAnimDataChanged(false);
            ActionGroup var29 = ActionGroup.getActionGroup(var18.m_animatedModel.GetAnimSetName());
            ActionContext var30 = var18.m_animatedModel.getActionContext();
            if (var29 != var30.getGroup()) {
               var30.setGroup(var29);
            }

            var18.m_animatedModel.getAdvancedAnimator().SetState(var30.getCurrentStateName(), PZArrayUtil.listConvert(var30.getChildStates(), (var0) -> {
               return var0.name;
            }));
         }

         return null;
      case 14:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var18.m_bClearDepthBuffer = (Boolean)var3;
         return null;
      case 15:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         boolean var20 = (Boolean)var3;
         if (var20 != var18.m_animatedModel.isFemale()) {
            var18.m_animatedModel.setOutfitName("Naked", var20, false);
         }

         return null;
      case 16:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var18.m_bShowBones = (Boolean)var3;
         return null;
      case 17:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var18.m_bUseDeferredMovement = (Boolean)var3;
         return null;
      case 18:
         String var21 = (String)var2;
         byte var8 = -1;
         switch(var21.hashCode()) {
         case -2046552227:
            if (var21.equals("vehicleModel")) {
               var8 = 4;
            }
            break;
         case -177460704:
            if (var21.equals("centerOfMass")) {
               var8 = 0;
            }
            break;
         case 104069929:
            if (var21.equals("model")) {
               var8 = 3;
            }
            break;
         case 739104294:
            if (var21.equals("chassis")) {
               var8 = 1;
            }
            break;
         case 1564195625:
            if (var21.equals("character")) {
               var8 = 2;
            }
         }

         UI3DScene.SceneVehicle var9;
         switch(var8) {
         case 0:
            this.m_gizmoParent = (UI3DScene.SceneObject)this.getSceneObjectById((String)var3, UI3DScene.SceneVehicle.class, true);
            this.m_gizmoOrigin = this.m_gizmoParent;
            this.m_gizmoChild = null;
            break;
         case 1:
            var9 = (UI3DScene.SceneVehicle)this.getSceneObjectById((String)var3, UI3DScene.SceneVehicle.class, true);
            this.m_gizmoParent = var9;
            this.m_originGizmo.m_translate.set((Vector3fc)var9.m_script.getCenterOfMassOffset());
            this.m_originGizmo.m_rotate.zero();
            this.m_gizmoOrigin = this.m_originGizmo;
            this.m_gizmoChild = null;
            break;
         case 2:
            UI3DScene.SceneCharacter var27 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var3, UI3DScene.SceneCharacter.class, true);
            this.m_gizmoParent = var27;
            this.m_gizmoOrigin = this.m_gizmoParent;
            this.m_gizmoChild = null;
            break;
         case 3:
            var26 = (UI3DScene.SceneModel)this.getSceneObjectById((String)var3, UI3DScene.SceneModel.class, true);
            this.m_gizmoParent = var26;
            this.m_gizmoOrigin = this.m_gizmoParent;
            this.m_gizmoChild = null;
            break;
         case 4:
            var9 = (UI3DScene.SceneVehicle)this.getSceneObjectById((String)var3, UI3DScene.SceneVehicle.class, true);
            this.m_gizmoParent = var9;
            this.m_originGizmo.m_translate.set((Vector3fc)var9.m_script.getModel().getOffset());
            this.m_originGizmo.m_rotate.zero();
            this.m_gizmoOrigin = this.m_originGizmo;
            this.m_gizmoChild = null;
         }

         return null;
      case 19:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var18.m_animatedModel.setState((String)var3);
         return null;
      case 20:
         var18 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var19 = (String)var3;
         this.m_highlightBone.m_character = var18;
         this.m_highlightBone.m_boneName = var19;
         return null;
      case 21:
         var17 = this.getSceneObjectById((String)var2, true);
         var17.m_autoRotate = (Boolean)var3;
         if (!var17.m_autoRotate) {
            var17.m_autoRotateAngle = 0.0F;
         }

         return null;
      case 22:
         var17 = this.getSceneObjectById((String)var2, true);
         var17.m_visible = (Boolean)var3;
         return null;
      case 23:
         UI3DScene.SceneVehicle var16 = (UI3DScene.SceneVehicle)this.getSceneObjectById((String)var2, UI3DScene.SceneVehicle.class, true);
         var16.setScriptName((String)var3);
         return null;
      case 24:
         var6 = ((Double)var2).intValue();
         var7 = ((Double)var3).intValue();
         if (this.m_gizmo == null) {
            return "None";
         }

         return this.m_gizmo.hitTest((float)var6, (float)var7).toString();
      default:
         throw new IllegalArgumentException(String.format("unhandled \"%s\" \"%s\" \"%s\"", var1, var2, var3));
      }
   }

   public Object fromLua3(String var1, Object var2, Object var3, Object var4) {
      byte var6 = -1;
      switch(var1.hashCode()) {
      case -2094545211:
         if (var1.equals("setViewRotation")) {
            var6 = 5;
         }
         break;
      case -1900967153:
         if (var1.equals("startGizmoTracking")) {
            var6 = 4;
         }
         break;
      case -1149133854:
         if (var1.equals("addAxis")) {
            var6 = 0;
         }
         break;
      case -1147577620:
         if (var1.equals("pickCharacterBone")) {
            var6 = 1;
         }
         break;
      case -889388479:
         if (var1.equals("setGizmoXYZ")) {
            var6 = 3;
         }
         break;
      case -352953986:
         if (var1.equals("setGizmoOrigin")) {
            var6 = 2;
         }
      }

      float var7;
      float var8;
      float var9;
      switch(var6) {
      case 0:
         var7 = ((Double)var2).floatValue();
         var8 = ((Double)var3).floatValue();
         var9 = ((Double)var4).floatValue();
         this.m_axes.add(((UI3DScene.PositionRotation)s_posRotPool.alloc()).set(var7, var8, var9));
         return null;
      case 1:
         UI3DScene.SceneCharacter var12 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         var8 = ((Double)var3).floatValue();
         var9 = ((Double)var4).floatValue();
         return var12.pickBone(var8, var9);
      case 2:
         String var11 = (String)var2;
         byte var14 = -1;
         switch(var11.hashCode()) {
         case 3029700:
            if (var11.equals("bone")) {
               var14 = 0;
            }
         default:
            switch(var14) {
            case 0:
               UI3DScene.SceneCharacter var10 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var3, UI3DScene.SceneCharacter.class, true);
               this.m_gizmoParent = var10;
               this.m_originBone.m_character = var10;
               this.m_originBone.m_boneName = (String)var4;
               this.m_gizmoOrigin = this.m_originBone;
               this.m_gizmoChild = null;
            default:
               return null;
            }
         }
      case 3:
         var7 = ((Double)var2).floatValue();
         var8 = ((Double)var3).floatValue();
         var9 = ((Double)var4).floatValue();
         this.m_gizmoPos.set(var7, var8, var9);
         return null;
      case 4:
         var7 = ((Double)var2).floatValue();
         var8 = ((Double)var3).floatValue();
         UI3DScene.Axis var13 = UI3DScene.Axis.valueOf((String)var4);
         if (this.m_gizmo != null) {
            this.m_gizmo.startTracking(var7, var8, var13);
         }

         return null;
      case 5:
         var7 = ((Double)var2).floatValue();
         var8 = ((Double)var3).floatValue();
         var9 = ((Double)var4).floatValue();
         var7 %= 360.0F;
         var8 %= 360.0F;
         var9 %= 360.0F;
         this.m_viewRotation.set(var7, var8, var9);
         return null;
      default:
         throw new IllegalArgumentException(String.format("unhandled \"%s\" \"%s\" \"%s\" \"%s\"", var1, var2, var3, var4));
      }
   }

   public Object fromLua4(String var1, Object var2, Object var3, Object var4, Object var5) {
      byte var7 = -1;
      switch(var1.hashCode()) {
      case -1384272991:
         if (var1.equals("setPassengerPosition")) {
            var7 = 3;
         }
         break;
      case -1182783862:
         if (var1.equals("setObjectPosition")) {
            var7 = 2;
         }
         break;
      case -352953986:
         if (var1.equals("setGizmoOrigin")) {
            var7 = 0;
         }
         break;
      case 1152285259:
         if (var1.equals("setObjectParent")) {
            var7 = 1;
         }
      }

      UI3DScene.SceneObject var15;
      switch(var7) {
      case 0:
         String var16 = (String)var2;
         byte var17 = -1;
         switch(var16.hashCode()) {
         case -1963501277:
            if (var16.equals("attachment")) {
               var17 = 0;
            }
         default:
            switch(var17) {
            case 0:
               UI3DScene.SceneObject var18 = this.getSceneObjectById((String)var3, true);
               this.m_gizmoParent = this.getSceneObjectById((String)var4, true);
               this.m_originAttachment.m_object = this.m_gizmoParent;
               this.m_originAttachment.m_attachmentName = (String)var5;
               this.m_gizmoOrigin = this.m_originAttachment;
               this.m_gizmoChild = var18;
            default:
               return null;
            }
         }
      case 1:
         var15 = this.getSceneObjectById((String)var2, true);
         var15.m_translate.zero();
         var15.m_rotate.zero();
         var15.m_attachment = (String)var3;
         var15.m_parent = this.getSceneObjectById((String)var4, false);
         var15.m_parentAttachment = (String)var5;
         if (var15.m_parent != null && var15.m_parent.m_parent == var15) {
            var15.m_parent.m_parent = null;
         }

         return null;
      case 2:
         var15 = this.getSceneObjectById((String)var2, true);
         var15.m_translate.set(((Double)var3).floatValue(), ((Double)var4).floatValue(), ((Double)var5).floatValue());
         return null;
      case 3:
         UI3DScene.SceneCharacter var8 = (UI3DScene.SceneCharacter)this.getSceneObjectById((String)var2, UI3DScene.SceneCharacter.class, true);
         UI3DScene.SceneVehicle var9 = (UI3DScene.SceneVehicle)this.getSceneObjectById((String)var3, UI3DScene.SceneVehicle.class, true);
         VehicleScript.Passenger var10 = var9.m_script.getPassengerById((String)var4);
         if (var10 == null) {
            return null;
         }

         VehicleScript.Position var11 = var10.getPositionById((String)var5);
         if (var11 != null) {
            this.tempVector3f.set((Vector3fc)var9.m_script.getModel().getOffset());
            this.tempVector3f.add(var11.getOffset());
            Vector3f var10000 = this.tempVector3f;
            var10000.z *= -1.0F;
            var8.m_translate.set((Vector3fc)this.tempVector3f);
            var8.m_rotate.set((Vector3fc)var11.rotate);
            var8.m_parent = var9;
            if (var8.m_animatedModel != null) {
               String var12 = "inside".equalsIgnoreCase(var11.getId()) ? "player-vehicle" : "player-editor";
               if (!var12.equals(var8.m_animatedModel.GetAnimSetName())) {
                  var8.m_animatedModel.setAnimSetName(var12);
                  var8.m_animatedModel.getAdvancedAnimator().OnAnimDataChanged(false);
                  ActionGroup var13 = ActionGroup.getActionGroup(var8.m_animatedModel.GetAnimSetName());
                  ActionContext var14 = var8.m_animatedModel.getActionContext();
                  if (var13 != var14.getGroup()) {
                     var14.setGroup(var13);
                  }

                  var8.m_animatedModel.getAdvancedAnimator().SetState(var14.getCurrentStateName(), PZArrayUtil.listConvert(var14.getChildStates(), (var0) -> {
                     return var0.name;
                  }));
               }
            }
         }

         return null;
      default:
         throw new IllegalArgumentException(String.format("unhandled \"%s\" \"%s\" \"%s\" \"%s\"", var1, var2, var3, var4));
      }
   }

   public Object fromLua6(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7) {
      byte var9 = -1;
      switch(var1.hashCode()) {
      case -1262743205:
         if (var1.equals("addBox3D")) {
            var9 = 2;
         }
         break;
      case -1149187967:
         if (var1.equals("addAABB")) {
            var9 = 0;
         }
         break;
      case -1149133854:
         if (var1.equals("addAxis")) {
            var9 = 1;
         }
      }

      float var13;
      float var14;
      float var15;
      float var16;
      float var17;
      float var18;
      switch(var9) {
      case 0:
         var16 = ((Double)var2).floatValue();
         var17 = ((Double)var3).floatValue();
         var18 = ((Double)var4).floatValue();
         var13 = ((Double)var5).floatValue();
         var14 = ((Double)var6).floatValue();
         var15 = ((Double)var7).floatValue();
         this.m_aabb.add(((UI3DScene.AABB)s_aabbPool.alloc()).set(var16, var17, var18, var13, var14, var15, 1.0F, 1.0F, 1.0F));
         return null;
      case 1:
         var16 = ((Double)var2).floatValue();
         var17 = ((Double)var3).floatValue();
         var18 = ((Double)var4).floatValue();
         var13 = ((Double)var5).floatValue();
         var14 = ((Double)var6).floatValue();
         var15 = ((Double)var7).floatValue();
         this.m_axes.add(((UI3DScene.PositionRotation)s_posRotPool.alloc()).set(var16, var17, var18, var13, var14, var15));
         return null;
      case 2:
         Vector3f var10 = (Vector3f)var2;
         Vector3f var11 = (Vector3f)var3;
         Vector3f var12 = (Vector3f)var4;
         var13 = ((Double)var5).floatValue();
         var14 = ((Double)var6).floatValue();
         var15 = ((Double)var7).floatValue();
         this.m_box3D.add(((UI3DScene.Box3D)s_box3DPool.alloc()).set(var10.x, var10.y, var10.z, var11.x, var11.y, var11.z, var12.x, var12.y, var12.z, var13, var14, var15));
         return null;
      default:
         throw new IllegalArgumentException(String.format("unhandled \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\"", var1, var2, var3, var4, var5, var6, var7));
      }
   }

   public Object fromLua9(String var1, Object var2, Object var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10) {
      byte var12 = -1;
      switch(var1.hashCode()) {
      case -1149187967:
         if (var1.equals("addAABB")) {
            var12 = 0;
         }
      default:
         switch(var12) {
         case 0:
            float var13 = ((Double)var2).floatValue();
            float var14 = ((Double)var3).floatValue();
            float var15 = ((Double)var4).floatValue();
            float var16 = ((Double)var5).floatValue();
            float var17 = ((Double)var6).floatValue();
            float var18 = ((Double)var7).floatValue();
            float var19 = ((Double)var8).floatValue();
            float var20 = ((Double)var9).floatValue();
            float var21 = ((Double)var10).floatValue();
            this.m_aabb.add(((UI3DScene.AABB)s_aabbPool.alloc()).set(var13, var14, var15, var16, var17, var18, var19, var20, var21));
            return null;
         default:
            throw new IllegalArgumentException(String.format("unhandled \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\"", var1, var2, var3, var4, var5, var6, var7, var8, var9, var10));
         }
      }
   }

   private int screenWidth() {
      return (int)this.width;
   }

   private int screenHeight() {
      return (int)this.height;
   }

   public float uiToSceneX(float var1, float var2) {
      float var3 = var1 - (float)this.screenWidth() / 2.0F;
      var3 += (float)this.m_view_x;
      var3 /= this.zoomMult();
      return var3;
   }

   public float uiToSceneY(float var1, float var2) {
      float var3 = var2 - (float)this.screenHeight() / 2.0F;
      var3 *= -1.0F;
      var3 -= (float)this.m_view_y;
      var3 /= this.zoomMult();
      return var3;
   }

   public Vector3f uiToScene(float var1, float var2, float var3, Vector3f var4) {
      this.uiToScene((Matrix4f)null, var1, var2, var3, var4);
      switch(this.m_view) {
      case Left:
      case Right:
         var4.x = 0.0F;
         break;
      case Top:
      case Bottom:
         var4.y = 0.0F;
         break;
      case Front:
      case Back:
         var4.z = 0.0F;
      }

      return var4;
   }

   public Vector3f uiToScene(Matrix4f var1, float var2, float var3, float var4, Vector3f var5) {
      var3 = (float)this.screenHeight() - var3;
      Matrix4f var6 = allocMatrix4f();
      var6.set((Matrix4fc)this.m_projection);
      var6.mul((Matrix4fc)this.m_modelView);
      if (var1 != null) {
         var6.mul((Matrix4fc)var1);
      }

      var6.invert();
      this.m_viewport[2] = this.screenWidth();
      this.m_viewport[3] = this.screenHeight();
      var6.unprojectInv(var2, var3, var4, this.m_viewport, var5);
      releaseMatrix4f(var6);
      return var5;
   }

   public float sceneToUIX(float var1, float var2, float var3) {
      this.tempVector4f.set(var1, var2, var3, 1.0F);
      Matrix4f var4 = allocMatrix4f();
      var4.set((Matrix4fc)this.m_projection);
      var4.mul((Matrix4fc)this.m_modelView);
      this.m_viewport[2] = this.screenWidth();
      this.m_viewport[3] = this.screenHeight();
      var4.project(var1, var2, var3, this.m_viewport, this.tempVector3f);
      releaseMatrix4f(var4);
      return this.tempVector3f.x();
   }

   public float sceneToUIY(float var1, float var2, float var3) {
      this.tempVector4f.set(var1, var2, var3, 1.0F);
      Matrix4f var4 = allocMatrix4f();
      var4.set((Matrix4fc)this.m_projection);
      var4.mul((Matrix4fc)this.m_modelView);
      int[] var5 = new int[]{0, 0, this.screenWidth(), this.screenHeight()};
      var4.project(var1, var2, var3, var5, this.tempVector3f);
      releaseMatrix4f(var4);
      return (float)this.screenHeight() - this.tempVector3f.y();
   }

   private void renderGridXY(int var1) {
      int var2;
      int var3;
      for(var2 = -5; var2 < 5; ++var2) {
         for(var3 = 1; var3 < var1; ++var3) {
            vboLines.addLine((float)var2 + (float)var3 / (float)var1, -5.0F, 0.0F, (float)var2 + (float)var3 / (float)var1, 5.0F, 0.0F, 0.2F, 0.2F, 0.2F, this.GRID_ALPHA);
         }
      }

      for(var2 = -5; var2 < 5; ++var2) {
         for(var3 = 1; var3 < var1; ++var3) {
            vboLines.addLine(-5.0F, (float)var2 + (float)var3 / (float)var1, 0.0F, 5.0F, (float)var2 + (float)var3 / (float)var1, 0.0F, 0.2F, 0.2F, 0.2F, this.GRID_ALPHA);
         }
      }

      for(var2 = -5; var2 <= 5; ++var2) {
         vboLines.addLine((float)var2, -5.0F, 0.0F, (float)var2, 5.0F, 0.0F, 0.1F, 0.1F, 0.1F, this.GRID_ALPHA);
      }

      for(var2 = -5; var2 <= 5; ++var2) {
         vboLines.addLine(-5.0F, (float)var2, 0.0F, 5.0F, (float)var2, 0.0F, 0.1F, 0.1F, 0.1F, this.GRID_ALPHA);
      }

      if (this.m_bDrawGridAxes) {
         byte var4 = 0;
         vboLines.addLine(-5.0F, 0.0F, (float)var4, 5.0F, 0.0F, (float)var4, 1.0F, 0.0F, 0.0F, this.GRID_ALPHA);
         var4 = 0;
         vboLines.addLine(0.0F, -5.0F, (float)var4, 0.0F, 5.0F, (float)var4, 0.0F, 1.0F, 0.0F, this.GRID_ALPHA);
      }

   }

   private void renderGridXZ(int var1) {
      int var2;
      int var3;
      for(var2 = -5; var2 < 5; ++var2) {
         for(var3 = 1; var3 < var1; ++var3) {
            vboLines.addLine((float)var2 + (float)var3 / (float)var1, 0.0F, -5.0F, (float)var2 + (float)var3 / (float)var1, 0.0F, 5.0F, 0.2F, 0.2F, 0.2F, this.GRID_ALPHA);
         }
      }

      for(var2 = -5; var2 < 5; ++var2) {
         for(var3 = 1; var3 < var1; ++var3) {
            vboLines.addLine(-5.0F, 0.0F, (float)var2 + (float)var3 / (float)var1, 5.0F, 0.0F, (float)var2 + (float)var3 / (float)var1, 0.2F, 0.2F, 0.2F, this.GRID_ALPHA);
         }
      }

      for(var2 = -5; var2 <= 5; ++var2) {
         vboLines.addLine((float)var2, 0.0F, -5.0F, (float)var2, 0.0F, 5.0F, 0.1F, 0.1F, 0.1F, this.GRID_ALPHA);
      }

      for(var2 = -5; var2 <= 5; ++var2) {
         vboLines.addLine(-5.0F, 0.0F, (float)var2, 5.0F, 0.0F, (float)var2, 0.1F, 0.1F, 0.1F, this.GRID_ALPHA);
      }

      if (this.m_bDrawGridAxes) {
         byte var4 = 0;
         vboLines.addLine(-5.0F, 0.0F, (float)var4, 5.0F, 0.0F, (float)var4, 1.0F, 0.0F, 0.0F, this.GRID_ALPHA);
         var4 = 0;
         vboLines.addLine((float)var4, 0.0F, -5.0F, (float)var4, 0.0F, 5.0F, 0.0F, 0.0F, 1.0F, this.GRID_ALPHA);
      }

   }

   private void renderGridYZ(int var1) {
      int var2;
      int var3;
      for(var2 = -5; var2 < 5; ++var2) {
         for(var3 = 1; var3 < var1; ++var3) {
            vboLines.addLine(0.0F, (float)var2 + (float)var3 / (float)var1, -5.0F, 0.0F, (float)var2 + (float)var3 / (float)var1, 5.0F, 0.2F, 0.2F, 0.2F, this.GRID_ALPHA);
         }
      }

      for(var2 = -5; var2 < 5; ++var2) {
         for(var3 = 1; var3 < var1; ++var3) {
            vboLines.addLine(0.0F, -5.0F, (float)var2 + (float)var3 / (float)var1, 0.0F, 5.0F, (float)var2 + (float)var3 / (float)var1, 0.2F, 0.2F, 0.2F, this.GRID_ALPHA);
         }
      }

      for(var2 = -5; var2 <= 5; ++var2) {
         vboLines.addLine(0.0F, (float)var2, -5.0F, 0.0F, (float)var2, 5.0F, 0.1F, 0.1F, 0.1F, this.GRID_ALPHA);
      }

      for(var2 = -5; var2 <= 5; ++var2) {
         vboLines.addLine(0.0F, -5.0F, (float)var2, 0.0F, 5.0F, (float)var2, 0.1F, 0.1F, 0.1F, this.GRID_ALPHA);
      }

      if (this.m_bDrawGridAxes) {
         byte var4 = 0;
         vboLines.addLine(0.0F, -5.0F, (float)var4, 0.0F, 5.0F, (float)var4, 0.0F, 1.0F, 0.0F, this.GRID_ALPHA);
         var4 = 0;
         vboLines.addLine((float)var4, 0.0F, -5.0F, (float)var4, 0.0F, 5.0F, 0.0F, 0.0F, 1.0F, this.GRID_ALPHA);
      }

   }

   private void renderGrid() {
      vboLines.setLineWidth(1.0F);
      this.GRID_ALPHA = 1.0F;
      long var1 = System.currentTimeMillis();
      if (this.m_viewChangeTime + this.VIEW_CHANGE_TIME > var1) {
         float var3 = (float)(this.m_viewChangeTime + this.VIEW_CHANGE_TIME - var1) / (float)this.VIEW_CHANGE_TIME;
         this.GRID_ALPHA = 1.0F - var3;
         this.GRID_ALPHA *= this.GRID_ALPHA;
      }

      switch(this.m_view) {
      case Left:
      case Right:
         this.renderGridYZ(10);
         return;
      case Top:
      case Bottom:
         this.renderGridXZ(10);
         return;
      case Front:
      case Back:
         this.renderGridXY(10);
         return;
      default:
         switch(this.m_gridPlane) {
         case XY:
            this.renderGridXY(10);
            break;
         case XZ:
            this.renderGridXZ(10);
            break;
         case YZ:
            this.renderGridYZ(10);
         }

      }
   }

   void renderAxis(UI3DScene.PositionRotation var1) {
      this.renderAxis(var1.pos, var1.rot);
   }

   void renderAxis(Vector3f var1, Vector3f var2) {
      UI3DScene.StateData var3 = this.stateDataRender();
      vboLines.flush();
      Matrix4f var4 = allocMatrix4f();
      var4.set((Matrix4fc)var3.m_gizmoParentTransform);
      var4.mul((Matrix4fc)var3.m_gizmoOriginTransform);
      var4.mul((Matrix4fc)var3.m_gizmoChildTransform);
      var4.translate(var1);
      var4.rotateXYZ(var2.x * 0.017453292F, var2.y * 0.017453292F, var2.z * 0.017453292F);
      var3.m_modelView.mul((Matrix4fc)var4, var4);
      PZGLUtil.pushAndLoadMatrix(5888, var4);
      releaseMatrix4f(var4);
      float var5 = 0.1F;
      vboLines.setLineWidth(3.0F);
      vboLines.addLine(0.0F, 0.0F, 0.0F, var5, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F);
      vboLines.addLine(0.0F, 0.0F, 0.0F, 0.0F, 0.0F + var5, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F);
      vboLines.addLine(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F + var5, 0.0F, 0.0F, 1.0F, 1.0F);
      vboLines.flush();
      PZGLUtil.popMatrix(5888);
   }

   private void renderAABB(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var10 = var4 / 2.0F;
      float var11 = var5 / 2.0F;
      float var12 = var6 / 2.0F;
      vboLines.setOffset(var1, var2, var3);
      vboLines.setLineWidth(1.0F);
      float var13 = 1.0F;
      vboLines.addLine(var10, var11, var12, -var10, var11, var12, var7, var8, var9, var13);
      vboLines.addLine(var10, var11, var12, var10, -var11, var12, var7, var8, var9, var13);
      vboLines.addLine(var10, var11, var12, var10, var11, -var12, var7, var8, var9, var13);
      vboLines.addLine(-var10, var11, var12, -var10, -var11, var12, var7, var8, var9, var13);
      vboLines.addLine(-var10, var11, var12, -var10, var11, -var12, var7, var8, var9, var13);
      vboLines.addLine(var10, var11, -var12, var10, -var11, -var12, var7, var8, var9, var13);
      vboLines.addLine(var10, var11, -var12, -var10, var11, -var12, var7, var8, var9, var13);
      vboLines.addLine(-var10, var11, -var12, -var10, -var11, -var12, var7, var8, var9, var13);
      vboLines.addLine(var10, -var11, -var12, -var10, -var11, -var12, var7, var8, var9, var13);
      vboLines.addLine(var10, -var11, var12, var10, -var11, -var12, var7, var8, var9, var13);
      vboLines.addLine(-var10, -var11, var12, -var10, -var11, -var12, var7, var8, var9, var13);
      vboLines.addLine(var10, -var11, var12, -var10, -var11, var12, var7, var8, var9, var13);
      vboLines.setOffset(0.0F, 0.0F, 0.0F);
   }

   private void renderBox3D(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
      UI3DScene.StateData var13 = this.stateDataRender();
      vboLines.flush();
      Matrix4f var14 = allocMatrix4f();
      var14.identity();
      var14.translate(var1, var2, var3);
      var14.rotateXYZ(var7 * 0.017453292F, var8 * 0.017453292F, var9 * 0.017453292F);
      var13.m_modelView.mul((Matrix4fc)var14, var14);
      PZGLUtil.pushAndLoadMatrix(5888, var14);
      releaseMatrix4f(var14);
      this.renderAABB(var1 * 0.0F, var2 * 0.0F, var3 * 0.0F, var4, var5, var6, var10, var11, var12);
      vboLines.flush();
      PZGLUtil.popMatrix(5888);
   }

   private void calcMatrices(Matrix4f var1, Matrix4f var2) {
      float var3 = (float)this.screenWidth();
      float var4 = 1366.0F / var3;
      float var5 = (float)this.screenHeight() * var4;
      var3 = 1366.0F;
      var3 /= this.zoomMult();
      var5 /= this.zoomMult();
      var1.setOrtho(-var3 / 2.0F, var3 / 2.0F, -var5 / 2.0F, var5 / 2.0F, -10.0F, 10.0F);
      float var6 = (float)this.m_view_x / this.zoomMult() * var4;
      float var7 = (float)this.m_view_y / this.zoomMult() * var4;
      var1.translate(-var6, var7, 0.0F);
      var2.identity();
      float var8 = 0.0F;
      float var9 = 0.0F;
      float var10 = 0.0F;
      switch(this.m_view) {
      case Left:
         var9 = 270.0F;
         break;
      case Right:
         var9 = 90.0F;
         break;
      case Top:
         var9 = 90.0F;
         var10 = 90.0F;
         break;
      case Bottom:
         var9 = 90.0F;
         var10 = 270.0F;
      case Front:
      default:
         break;
      case Back:
         var9 = 180.0F;
         break;
      case UserDefined:
         var8 = this.m_viewRotation.x;
         var9 = this.m_viewRotation.y;
         var10 = this.m_viewRotation.z;
      }

      var2.rotateXYZ(var8 * 0.017453292F, var9 * 0.017453292F, var10 * 0.017453292F);
   }

   UI3DScene.Ray getCameraRay(float var1, float var2, UI3DScene.Ray var3) {
      return this.getCameraRay(var1, var2, this.m_projection, this.m_modelView, var3);
   }

   UI3DScene.Ray getCameraRay(float var1, float var2, Matrix4f var3, Matrix4f var4, UI3DScene.Ray var5) {
      Matrix4f var6 = allocMatrix4f();
      var6.set((Matrix4fc)var3);
      var6.mul((Matrix4fc)var4);
      var6.invert();
      this.m_viewport[2] = this.screenWidth();
      this.m_viewport[3] = this.screenHeight();
      Vector3f var7 = var6.unprojectInv(var1, var2, 0.0F, this.m_viewport, allocVector3f());
      Vector3f var8 = var6.unprojectInv(var1, var2, 1.0F, this.m_viewport, allocVector3f());
      var5.origin.set((Vector3fc)var7);
      var5.direction.set((Vector3fc)var8.sub(var7).normalize());
      releaseVector3f(var8);
      releaseVector3f(var7);
      releaseMatrix4f(var6);
      return var5;
   }

   float closest_distance_between_lines(UI3DScene.Ray var1, UI3DScene.Ray var2) {
      Vector3f var3 = allocVector3f().set((Vector3fc)var1.direction);
      Vector3f var4 = allocVector3f().set((Vector3fc)var2.direction);
      Vector3f var5 = allocVector3f().set((Vector3fc)var1.origin).sub(var2.origin);
      float var6 = var3.dot(var3);
      float var7 = var3.dot(var4);
      float var8 = var4.dot(var4);
      float var9 = var3.dot(var5);
      float var10 = var4.dot(var5);
      float var11 = var6 * var8 - var7 * var7;
      float var12;
      float var13;
      if (var11 < 1.0E-8F) {
         var12 = 0.0F;
         var13 = var7 > var8 ? var9 / var7 : var10 / var8;
      } else {
         var12 = (var7 * var10 - var8 * var9) / var11;
         var13 = (var6 * var10 - var7 * var9) / var11;
      }

      Vector3f var14 = var5.add(var3.mul(var12)).sub(var4.mul(var13));
      var1.t = var12;
      var2.t = var13;
      releaseVector3f(var3);
      releaseVector3f(var4);
      releaseVector3f(var5);
      return var14.length();
   }

   Vector3f project(Vector3f var1, Vector3f var2, Vector3f var3) {
      return var3.set((Vector3fc)var2).mul(var1.dot(var2) / var2.dot(var2));
   }

   Vector3f reject(Vector3f var1, Vector3f var2, Vector3f var3) {
      Vector3f var4 = this.project(var1, var2, allocVector3f());
      var3.set((Vector3fc)var1).sub(var4);
      releaseVector3f(var4);
      return var3;
   }

   int intersect_ray_plane(UI3DScene.Plane var1, UI3DScene.Ray var2, Vector3f var3) {
      Vector3f var4 = allocVector3f().set((Vector3fc)var2.direction).mul(100.0F);
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

   float distance_between_point_ray(Vector3f var1, UI3DScene.Ray var2) {
      Vector3f var3 = allocVector3f().set((Vector3fc)var2.direction).mul(100.0F);
      Vector3f var4 = allocVector3f().set((Vector3fc)var1).sub(var2.origin);
      float var5 = var4.dot(var3);
      float var6 = var3.dot(var3);
      float var7 = var5 / var6;
      Vector3f var8 = var3.mul(var7).add(var2.origin);
      float var9 = var8.sub(var1).length();
      releaseVector3f(var4);
      releaseVector3f(var3);
      return var9;
   }

   float closest_distance_line_circle(UI3DScene.Ray var1, UI3DScene.Circle var2, Vector3f var3) {
      UI3DScene.Plane var4 = allocPlane().set(var2.orientation, var2.center);
      Vector3f var5 = allocVector3f();
      float var6;
      if (this.intersect_ray_plane(var4, var1, var5) == 1) {
         var3.set((Vector3fc)var5).sub(var2.center).normalize().mul(var2.radius).add(var2.center);
         var6 = var5.sub(var3).length();
      } else {
         Vector3f var7 = allocVector3f().set((Vector3fc)var1.origin).sub(var2.center);
         Vector3f var8 = this.reject(var7, var2.orientation, allocVector3f());
         var3.set((Vector3fc)var8.normalize().mul(var2.radius).add(var2.center));
         var6 = this.distance_between_point_ray(var3, var1);
         releaseVector3f(var8);
         releaseVector3f(var7);
      }

      releaseVector3f(var5);
      releasePlane(var4);
      return var6;
   }

   private UI3DScene.StateData stateDataMain() {
      return this.m_stateData[SpriteRenderer.instance.getMainStateIndex()];
   }

   private UI3DScene.StateData stateDataRender() {
      return this.m_stateData[SpriteRenderer.instance.getRenderStateIndex()];
   }

   private static enum View {
      Left,
      Right,
      Top,
      Bottom,
      Front,
      Back,
      UserDefined;

      // $FF: synthetic method
      private static UI3DScene.View[] $values() {
         return new UI3DScene.View[]{Left, Right, Top, Bottom, Front, Back, UserDefined};
      }
   }

   private static enum TransformMode {
      Global,
      Local;

      // $FF: synthetic method
      private static UI3DScene.TransformMode[] $values() {
         return new UI3DScene.TransformMode[]{Global, Local};
      }
   }

   private static enum GridPlane {
      XY,
      XZ,
      YZ;

      // $FF: synthetic method
      private static UI3DScene.GridPlane[] $values() {
         return new UI3DScene.GridPlane[]{XY, XZ, YZ};
      }
   }

   private final class CharacterSceneModelCamera extends UI3DScene.SceneModelCamera {
      private CharacterSceneModelCamera() {
         super();
      }

      public void Begin() {
         UI3DScene.StateData var1 = UI3DScene.this.stateDataRender();
         GL11.glViewport(UI3DScene.this.getAbsoluteX().intValue(), Core.getInstance().getScreenHeight() - UI3DScene.this.getAbsoluteY().intValue() - UI3DScene.this.getHeight().intValue(), UI3DScene.this.getWidth().intValue(), UI3DScene.this.getHeight().intValue());
         PZGLUtil.pushAndLoadMatrix(5889, var1.m_projection);
         Matrix4f var2 = UI3DScene.allocMatrix4f();
         var2.set((Matrix4fc)var1.m_modelView);
         var2.mul((Matrix4fc)this.m_renderData.m_transform);
         PZGLUtil.pushAndLoadMatrix(5888, var2);
         UI3DScene.releaseMatrix4f(var2);
      }

      public void End() {
         PZGLUtil.popMatrix(5889);
         PZGLUtil.popMatrix(5888);
      }
   }

   private final class VehicleSceneModelCamera extends UI3DScene.SceneModelCamera {
      private VehicleSceneModelCamera() {
         super();
      }

      public void Begin() {
         UI3DScene.StateData var1 = UI3DScene.this.stateDataRender();
         GL11.glViewport(UI3DScene.this.getAbsoluteX().intValue(), Core.getInstance().getScreenHeight() - UI3DScene.this.getAbsoluteY().intValue() - UI3DScene.this.getHeight().intValue(), UI3DScene.this.getWidth().intValue(), UI3DScene.this.getHeight().intValue());
         PZGLUtil.pushAndLoadMatrix(5889, var1.m_projection);
         Matrix4f var2 = UI3DScene.allocMatrix4f();
         var2.set((Matrix4fc)var1.m_modelView);
         var2.mul((Matrix4fc)this.m_renderData.m_transform);
         PZGLUtil.pushAndLoadMatrix(5888, var2);
         UI3DScene.releaseMatrix4f(var2);
         GL11.glDepthRange(0.0D, 1.0D);
         GL11.glDepthMask(true);
      }

      public void End() {
         PZGLUtil.popMatrix(5889);
         PZGLUtil.popMatrix(5888);
      }
   }

   private static final class StateData {
      final Matrix4f m_projection = new Matrix4f();
      final Matrix4f m_modelView = new Matrix4f();
      int m_zoom;
      UI3DScene.OverlaysDrawer m_overlaysDrawer;
      final ArrayList m_objectData = new ArrayList();
      UI3DScene.Gizmo m_gizmo = null;
      final Vector3f m_gizmoTranslate = new Vector3f();
      final Vector3f m_gizmoRotate = new Vector3f();
      final Matrix4f m_gizmoParentTransform = new Matrix4f();
      final Matrix4f m_gizmoOriginTransform = new Matrix4f();
      final Matrix4f m_gizmoChildTransform = new Matrix4f();
      final Matrix4f m_gizmoTransform = new Matrix4f();
      boolean m_hasGizmoOrigin;
      boolean m_selectedAttachmentIsChildAttachment;
      UI3DScene.Axis m_gizmoAxis;
      final UI3DScene.TranslateGizmoRenderData m_translateGizmoRenderData;
      final ArrayList m_axes;
      final ArrayList m_aabb;
      final ArrayList m_box3D;

      private StateData() {
         this.m_gizmoAxis = UI3DScene.Axis.None;
         this.m_translateGizmoRenderData = new UI3DScene.TranslateGizmoRenderData();
         this.m_axes = new ArrayList();
         this.m_aabb = new ArrayList();
         this.m_box3D = new ArrayList();
      }

      private float zoomMult() {
         return (float)Math.exp((double)((float)this.m_zoom * 0.2F)) * 160.0F / Math.max(1.82F, 1.0F);
      }
   }

   private final class RotateGizmo extends UI3DScene.Gizmo {
      UI3DScene.Axis m_trackAxis;
      final UI3DScene.Circle m_trackCircle;
      final Matrix4f m_startXfrm;
      final Matrix4f m_startInvXfrm;
      final Vector3f m_startPointOnCircle;
      final Vector3f m_currentPointOnCircle;
      final ArrayList m_circlePointsMain;
      final ArrayList m_circlePointsRender;

      private RotateGizmo() {
         super();
         this.m_trackAxis = UI3DScene.Axis.None;
         this.m_trackCircle = new UI3DScene.Circle();
         this.m_startXfrm = new Matrix4f();
         this.m_startInvXfrm = new Matrix4f();
         this.m_startPointOnCircle = new Vector3f();
         this.m_currentPointOnCircle = new Vector3f();
         this.m_circlePointsMain = new ArrayList();
         this.m_circlePointsRender = new ArrayList();
      }

      UI3DScene.Axis hitTest(float var1, float var2) {
         if (!this.m_visible) {
            return UI3DScene.Axis.None;
         } else {
            UI3DScene.StateData var3 = UI3DScene.this.stateDataMain();
            var2 = (float)UI3DScene.this.screenHeight() - var2;
            UI3DScene.Ray var4 = UI3DScene.this.getCameraRay(var1, var2, UI3DScene.allocRay());
            Matrix4f var5 = UI3DScene.allocMatrix4f();
            var5.set((Matrix4fc)var3.m_gizmoParentTransform);
            var5.mul((Matrix4fc)var3.m_gizmoOriginTransform);
            var5.mul((Matrix4fc)var3.m_gizmoChildTransform);
            var5.mul((Matrix4fc)var3.m_gizmoTransform);
            Vector3f var6 = var5.getScale(UI3DScene.allocVector3f());
            var5.scale(1.0F / var6.x, 1.0F / var6.y, 1.0F / var6.z);
            UI3DScene.releaseVector3f(var6);
            if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
               var5.setRotationXYZ(0.0F, 0.0F, 0.0F);
            }

            float var17 = UI3DScene.this.m_gizmoScale / var3.zoomMult() * 1000.0F;
            float var7 = this.LENGTH * var17;
            Vector3f var8 = var5.transformProject(UI3DScene.allocVector3f().set(0.0F, 0.0F, 0.0F));
            Vector3f var9 = var5.transformDirection(UI3DScene.allocVector3f().set(1.0F, 0.0F, 0.0F)).normalize();
            Vector3f var10 = var5.transformDirection(UI3DScene.allocVector3f().set(0.0F, 1.0F, 0.0F)).normalize();
            Vector3f var11 = var5.transformDirection(UI3DScene.allocVector3f().set(0.0F, 0.0F, 1.0F)).normalize();
            Vector2 var12 = UI3DScene.allocVector2();
            this.getCircleSegments(var8, var7, var10, var11, this.m_circlePointsMain);
            float var13 = this.hitTestCircle(var4, this.m_circlePointsMain, var12);
            ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(this.m_circlePointsMain);
            this.m_circlePointsMain.clear();
            this.getCircleSegments(var8, var7, var9, var11, this.m_circlePointsMain);
            float var14 = this.hitTestCircle(var4, this.m_circlePointsMain, var12);
            ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(this.m_circlePointsMain);
            this.m_circlePointsMain.clear();
            this.getCircleSegments(var8, var7, var9, var10, this.m_circlePointsMain);
            float var15 = this.hitTestCircle(var4, this.m_circlePointsMain, var12);
            ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(this.m_circlePointsMain);
            this.m_circlePointsMain.clear();
            UI3DScene.releaseVector2(var12);
            UI3DScene.releaseVector3f(var9);
            UI3DScene.releaseVector3f(var10);
            UI3DScene.releaseVector3f(var11);
            UI3DScene.releaseVector3f(var8);
            UI3DScene.releaseRay(var4);
            UI3DScene.releaseMatrix4f(var5);
            float var16 = 8.0F;
            if (var13 < var14 && var13 < var15) {
               return var13 <= var16 ? UI3DScene.Axis.X : UI3DScene.Axis.None;
            } else if (var14 < var13 && var14 < var15) {
               return var14 <= var16 ? UI3DScene.Axis.Y : UI3DScene.Axis.None;
            } else if (var15 < var13 && var15 < var14) {
               return var15 <= var16 ? UI3DScene.Axis.Z : UI3DScene.Axis.None;
            } else {
               return UI3DScene.Axis.None;
            }
         }
      }

      void startTracking(float var1, float var2, UI3DScene.Axis var3) {
         UI3DScene.StateData var4 = UI3DScene.this.stateDataMain();
         this.m_startXfrm.set((Matrix4fc)var4.m_gizmoParentTransform);
         this.m_startXfrm.mul((Matrix4fc)var4.m_gizmoOriginTransform);
         this.m_startXfrm.mul((Matrix4fc)var4.m_gizmoChildTransform);
         this.m_startXfrm.mul((Matrix4fc)var4.m_gizmoTransform);
         if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
            this.m_startXfrm.setRotationXYZ(0.0F, 0.0F, 0.0F);
         }

         this.m_startInvXfrm.set((Matrix4fc)var4.m_gizmoParentTransform);
         this.m_startInvXfrm.mul((Matrix4fc)var4.m_gizmoOriginTransform);
         this.m_startInvXfrm.mul((Matrix4fc)var4.m_gizmoChildTransform);
         this.m_startInvXfrm.mul((Matrix4fc)var4.m_gizmoTransform);
         this.m_startInvXfrm.invert();
         this.m_trackAxis = var3;
         this.getPointOnAxis(var1, var2, var3, this.m_trackCircle, this.m_startXfrm, this.m_startPointOnCircle);
      }

      void updateTracking(float var1, float var2) {
         Vector3f var3 = this.getPointOnAxis(var1, var2, this.m_trackAxis, this.m_trackCircle, this.m_startXfrm, UI3DScene.allocVector3f());
         if (this.m_currentPointOnCircle.equals(var3)) {
            UI3DScene.releaseVector3f(var3);
         } else {
            this.m_currentPointOnCircle.set((Vector3fc)var3);
            UI3DScene.releaseVector3f(var3);
            float var4 = this.calculateRotation(this.m_startPointOnCircle, this.m_currentPointOnCircle, this.m_trackCircle);
            switch(this.m_trackAxis) {
            case X:
               this.m_trackCircle.orientation.set(1.0F, 0.0F, 0.0F);
               break;
            case Y:
               this.m_trackCircle.orientation.set(0.0F, 1.0F, 0.0F);
               break;
            case Z:
               this.m_trackCircle.orientation.set(0.0F, 0.0F, 1.0F);
            }

            Vector3f var5 = UI3DScene.allocVector3f().set((Vector3fc)this.m_trackCircle.orientation);
            if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
               this.m_startInvXfrm.transformDirection(var5);
            }

            UI3DScene.Ray var6 = UI3DScene.this.getCameraRay(var1, var2, UI3DScene.allocRay());
            Vector3f var7 = this.m_startXfrm.transformDirection(UI3DScene.allocVector3f().set((Vector3fc)var5)).normalize();
            float var8 = var6.direction.dot(var7);
            UI3DScene.releaseVector3f(var7);
            UI3DScene.releaseRay(var6);
            if (UI3DScene.this.m_gizmoParent instanceof UI3DScene.SceneCharacter) {
               if (var8 > 0.0F) {
                  var4 *= -1.0F;
               }
            } else if (var8 < 0.0F) {
               var4 *= -1.0F;
            }

            Quaternionf var9 = UI3DScene.allocQuaternionf().fromAxisAngleDeg(var5, var4);
            UI3DScene.releaseVector3f(var5);
            var7 = var9.getEulerAnglesXYZ(new Vector3f());
            UI3DScene.releaseQuaternionf(var9);
            var7.x = (float)Math.floor((double)(var7.x * 57.295776F + 0.5F));
            var7.y = (float)Math.floor((double)(var7.y * 57.295776F + 0.5F));
            var7.z = (float)Math.floor((double)(var7.z * 57.295776F + 0.5F));
            LuaManager.caller.pcall(UIManager.getDefaultThread(), UI3DScene.this.getTable().rawget("onGizmoChanged"), UI3DScene.this.table, var7);
         }
      }

      void stopTracking() {
         this.m_trackAxis = UI3DScene.Axis.None;
      }

      void render() {
         if (this.m_visible) {
            UI3DScene.StateData var1 = UI3DScene.this.stateDataRender();
            Matrix4f var2 = UI3DScene.allocMatrix4f();
            var2.set((Matrix4fc)var1.m_gizmoParentTransform);
            var2.mul((Matrix4fc)var1.m_gizmoOriginTransform);
            var2.mul((Matrix4fc)var1.m_gizmoChildTransform);
            var2.mul((Matrix4fc)var1.m_gizmoTransform);
            Vector3f var3 = var2.getScale(UI3DScene.allocVector3f());
            var2.scale(1.0F / var3.x, 1.0F / var3.y, 1.0F / var3.z);
            UI3DScene.releaseVector3f(var3);
            if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
               var2.setRotationXYZ(0.0F, 0.0F, 0.0F);
            }

            float var16 = (float)(Mouse.getXA() - UI3DScene.this.getAbsoluteX().intValue());
            float var4 = (float)(Mouse.getYA() - UI3DScene.this.getAbsoluteY().intValue());
            UI3DScene.Ray var5 = UI3DScene.this.getCameraRay(var16, (float)UI3DScene.this.screenHeight() - var4, var1.m_projection, var1.m_modelView, UI3DScene.allocRay());
            float var6 = UI3DScene.this.m_gizmoScale / var1.zoomMult() * 1000.0F;
            float var7 = this.LENGTH * var6;
            Vector3f var8 = var2.transformProject(UI3DScene.allocVector3f().set(0.0F, 0.0F, 0.0F));
            Vector3f var9 = var2.transformDirection(UI3DScene.allocVector3f().set(1.0F, 0.0F, 0.0F)).normalize();
            Vector3f var10 = var2.transformDirection(UI3DScene.allocVector3f().set(0.0F, 1.0F, 0.0F)).normalize();
            Vector3f var11 = var2.transformDirection(UI3DScene.allocVector3f().set(0.0F, 0.0F, 1.0F)).normalize();
            GL11.glClear(256);
            GL11.glEnable(2929);
            UI3DScene.Axis var12 = this.m_trackAxis == UI3DScene.Axis.None ? var1.m_gizmoAxis : this.m_trackAxis;
            float var13;
            float var14;
            float var15;
            if (this.m_trackAxis == UI3DScene.Axis.None || this.m_trackAxis == UI3DScene.Axis.X) {
               var13 = var12 == UI3DScene.Axis.X ? 1.0F : 0.5F;
               var14 = 0.0F;
               var15 = 0.0F;
               this.renderAxis(var8, var7, var10, var11, var13, var14, var15, var5);
            }

            if (this.m_trackAxis == UI3DScene.Axis.None || this.m_trackAxis == UI3DScene.Axis.Y) {
               var13 = 0.0F;
               var14 = var12 == UI3DScene.Axis.Y ? 1.0F : 0.5F;
               var15 = 0.0F;
               this.renderAxis(var8, var7, var9, var11, var13, var14, var15, var5);
            }

            if (this.m_trackAxis == UI3DScene.Axis.None || this.m_trackAxis == UI3DScene.Axis.Z) {
               var13 = 0.0F;
               var14 = 0.0F;
               var15 = var12 == UI3DScene.Axis.Z ? 1.0F : 0.5F;
               this.renderAxis(var8, var7, var9, var10, var13, var14, var15, var5);
            }

            UI3DScene.releaseVector3f(var8);
            UI3DScene.releaseVector3f(var9);
            UI3DScene.releaseVector3f(var10);
            UI3DScene.releaseVector3f(var11);
            UI3DScene.releaseRay(var5);
            UI3DScene.releaseMatrix4f(var2);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            this.renderLineToOrigin();
         }
      }

      void getCircleSegments(Vector3f var1, float var2, Vector3f var3, Vector3f var4, ArrayList var5) {
         Vector3f var6 = UI3DScene.allocVector3f();
         Vector3f var7 = UI3DScene.allocVector3f();
         byte var8 = 32;
         double var9 = 0.0D / (double)var8 * 0.01745329238474369D;
         double var11 = Math.cos(var9);
         double var13 = Math.sin(var9);
         var3.mul((float)var11, var6);
         var4.mul((float)var13, var7);
         var6.add(var7).mul(var2);
         var5.add(UI3DScene.allocVector3f().set((Vector3fc)var1).add(var6));

         for(int var15 = 1; var15 <= var8; ++var15) {
            var9 = (double)var15 * 360.0D / (double)var8 * 0.01745329238474369D;
            var11 = Math.cos(var9);
            var13 = Math.sin(var9);
            var3.mul((float)var11, var6);
            var4.mul((float)var13, var7);
            var6.add(var7).mul(var2);
            var5.add(UI3DScene.allocVector3f().set((Vector3fc)var1).add(var6));
         }

         UI3DScene.releaseVector3f(var6);
         UI3DScene.releaseVector3f(var7);
      }

      private float hitTestCircle(UI3DScene.Ray var1, ArrayList var2, Vector2 var3) {
         UI3DScene.Ray var4 = UI3DScene.allocRay();
         Vector3f var5 = UI3DScene.allocVector3f();
         float var6 = UI3DScene.this.sceneToUIX(var1.origin.x, var1.origin.y, var1.origin.z);
         float var7 = UI3DScene.this.sceneToUIY(var1.origin.x, var1.origin.y, var1.origin.z);
         float var8 = Float.MAX_VALUE;
         Vector3f var9 = (Vector3f)var2.get(0);

         for(int var10 = 1; var10 < var2.size(); ++var10) {
            Vector3f var11 = (Vector3f)var2.get(var10);
            float var12 = UI3DScene.this.sceneToUIX(var9.x, var9.y, var9.z);
            float var13 = UI3DScene.this.sceneToUIY(var9.x, var9.y, var9.z);
            float var14 = UI3DScene.this.sceneToUIX(var11.x, var11.y, var11.z);
            float var15 = UI3DScene.this.sceneToUIY(var11.x, var11.y, var11.z);
            double var16 = Math.pow((double)(var14 - var12), 2.0D) + Math.pow((double)(var15 - var13), 2.0D);
            if (var16 < 0.001D) {
               var9 = var11;
            } else {
               double var18 = (double)((var6 - var12) * (var14 - var12) + (var7 - var13) * (var15 - var13)) / var16;
               double var20 = (double)var12 + var18 * (double)(var14 - var12);
               double var22 = (double)var13 + var18 * (double)(var15 - var13);
               if (var18 <= 0.0D) {
                  var20 = (double)var12;
                  var22 = (double)var13;
               } else if (var18 >= 1.0D) {
                  var20 = (double)var14;
                  var22 = (double)var15;
               }

               float var24 = IsoUtils.DistanceTo2D(var6, var7, (float)var20, (float)var22);
               if (var24 < var8) {
                  var8 = var24;
                  var3.set((float)var20, (float)var22);
               }

               var9 = var11;
            }
         }

         UI3DScene.releaseVector3f(var5);
         UI3DScene.releaseRay(var4);
         return var8;
      }

      void renderAxis(Vector3f var1, float var2, Vector3f var3, Vector3f var4, float var5, float var6, float var7, UI3DScene.Ray var8) {
         UI3DScene.vboLines.flush();
         UI3DScene.vboLines.setLineWidth(6.0F);
         this.getCircleSegments(var1, var2, var3, var4, this.m_circlePointsRender);
         Vector3f var9 = UI3DScene.allocVector3f();
         Vector3f var10 = (Vector3f)this.m_circlePointsRender.get(0);

         for(int var11 = 1; var11 < this.m_circlePointsRender.size(); ++var11) {
            Vector3f var12 = (Vector3f)this.m_circlePointsRender.get(var11);
            var9.set(var12.x - var1.x, var12.y - var1.y, var12.z - var1.z).normalize();
            float var13 = var9.dot(var8.direction);
            if (var13 < 0.1F) {
               UI3DScene.vboLines.addLine(var10.x, var10.y, var10.z, var12.x, var12.y, var12.z, var5, var6, var7, 1.0F);
            } else {
               UI3DScene.vboLines.addLine(var10.x, var10.y, var10.z, var12.x, var12.y, var12.z, var5 / 2.0F, var6 / 2.0F, var7 / 2.0F, 0.25F);
            }

            var10 = var12;
         }

         ((BaseVehicle.Vector3fObjectPool)BaseVehicle.TL_vector3f_pool.get()).release(this.m_circlePointsRender);
         this.m_circlePointsRender.clear();
         UI3DScene.releaseVector3f(var9);
         UI3DScene.vboLines.flush();
      }

      Vector3f getPointOnAxis(float var1, float var2, UI3DScene.Axis var3, UI3DScene.Circle var4, Matrix4f var5, Vector3f var6) {
         float var7 = 1.0F;
         var4.radius = this.LENGTH * var7;
         var5.getTranslation(var4.center);
         float var8 = UI3DScene.this.sceneToUIX(var4.center.x, var4.center.y, var4.center.z);
         float var9 = UI3DScene.this.sceneToUIY(var4.center.x, var4.center.y, var4.center.z);
         var4.center.set(var8, var9, 0.0F);
         var4.orientation.set(0.0F, 0.0F, 1.0F);
         UI3DScene.Ray var10 = UI3DScene.allocRay();
         var10.origin.set(var1, var2, 0.0F);
         var10.direction.set(0.0F, 0.0F, -1.0F);
         UI3DScene.this.closest_distance_line_circle(var10, var4, var6);
         UI3DScene.releaseRay(var10);
         return var6;
      }

      float calculateRotation(Vector3f var1, Vector3f var2, UI3DScene.Circle var3) {
         if (var1.equals(var2)) {
            return 0.0F;
         } else {
            Vector3f var4 = UI3DScene.allocVector3f().set((Vector3fc)var1).sub(var3.center).normalize();
            Vector3f var5 = UI3DScene.allocVector3f().set((Vector3fc)var2).sub(var3.center).normalize();
            float var6 = (float)Math.acos((double)var5.dot(var4));
            Vector3f var7 = var4.cross(var5, UI3DScene.allocVector3f());
            int var8 = (int)Math.signum(var7.dot(var3.orientation));
            UI3DScene.releaseVector3f(var4);
            UI3DScene.releaseVector3f(var5);
            UI3DScene.releaseVector3f(var7);
            return (float)var8 * var6 * 57.295776F;
         }
      }
   }

   private final class ScaleGizmo extends UI3DScene.Gizmo {
      final Matrix4f m_startXfrm = new Matrix4f();
      final Matrix4f m_startInvXfrm = new Matrix4f();
      final Vector3f m_startPos = new Vector3f();
      final Vector3f m_currentPos = new Vector3f();
      UI3DScene.Axis m_trackAxis;
      boolean m_hideX;
      boolean m_hideY;
      boolean m_hideZ;
      final Cylinder cylinder;

      private ScaleGizmo() {
         super();
         this.m_trackAxis = UI3DScene.Axis.None;
         this.cylinder = new Cylinder();
      }

      UI3DScene.Axis hitTest(float var1, float var2) {
         if (!this.m_visible) {
            return UI3DScene.Axis.None;
         } else {
            UI3DScene.StateData var3 = UI3DScene.this.stateDataMain();
            Matrix4f var4 = UI3DScene.allocMatrix4f();
            var4.set((Matrix4fc)var3.m_gizmoParentTransform);
            var4.mul((Matrix4fc)var3.m_gizmoOriginTransform);
            var4.mul((Matrix4fc)var3.m_gizmoTransform);
            if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
               var4.setRotationXYZ(0.0F, 0.0F, 0.0F);
            }

            var2 = (float)UI3DScene.this.screenHeight() - var2;
            UI3DScene.Ray var5 = UI3DScene.this.getCameraRay(var1, var2, UI3DScene.allocRay());
            UI3DScene.Ray var6 = UI3DScene.allocRay();
            var4.transformProject(var6.origin.set(0.0F, 0.0F, 0.0F));
            float var7 = UI3DScene.this.m_gizmoScale / var3.zoomMult() * 1000.0F;
            float var8 = this.LENGTH * var7;
            float var9 = this.THICKNESS * var7;
            float var10 = 0.1F * var7;
            var4.transformDirection(var6.direction.set(1.0F, 0.0F, 0.0F)).normalize();
            float var11 = UI3DScene.this.closest_distance_between_lines(var6, var5);
            float var12 = var6.t;
            float var13 = var5.t;
            if (var12 < var10 || var12 >= var10 + var8) {
               var12 = Float.MAX_VALUE;
               var11 = Float.MAX_VALUE;
            }

            float var14 = var6.direction.dot(var5.direction);
            this.m_hideX = Math.abs(var14) > 0.9F;
            var4.transformDirection(var6.direction.set(0.0F, 1.0F, 0.0F)).normalize();
            float var15 = UI3DScene.this.closest_distance_between_lines(var6, var5);
            float var16 = var6.t;
            float var17 = var5.t;
            if (var16 < var10 || var16 >= var10 + var8) {
               var16 = Float.MAX_VALUE;
               var15 = Float.MAX_VALUE;
            }

            float var18 = var6.direction.dot(var5.direction);
            this.m_hideY = Math.abs(var18) > 0.9F;
            var4.transformDirection(var6.direction.set(0.0F, 0.0F, 1.0F)).normalize();
            float var19 = UI3DScene.this.closest_distance_between_lines(var6, var5);
            float var20 = var6.t;
            float var21 = var5.t;
            if (var20 < var10 || var20 >= var10 + var8) {
               var20 = Float.MAX_VALUE;
               var19 = Float.MAX_VALUE;
            }

            float var22 = var6.direction.dot(var5.direction);
            this.m_hideZ = Math.abs(var22) > 0.9F;
            UI3DScene.releaseRay(var6);
            UI3DScene.releaseRay(var5);
            UI3DScene.releaseMatrix4f(var4);
            if (var12 >= var10 && var12 < var10 + var8 && var11 < var15 && var11 < var19) {
               return var11 <= var9 / 2.0F ? UI3DScene.Axis.X : UI3DScene.Axis.None;
            } else if (var16 >= var10 && var16 < var10 + var8 && var15 < var11 && var15 < var19) {
               return var15 <= var9 / 2.0F ? UI3DScene.Axis.Y : UI3DScene.Axis.None;
            } else if (var20 >= var10 && var20 < var10 + var8 && var19 < var11 && var19 < var15) {
               return var19 <= var9 / 2.0F ? UI3DScene.Axis.Z : UI3DScene.Axis.None;
            } else {
               return UI3DScene.Axis.None;
            }
         }
      }

      void startTracking(float var1, float var2, UI3DScene.Axis var3) {
         UI3DScene.StateData var4 = UI3DScene.this.stateDataMain();
         this.m_startXfrm.set((Matrix4fc)var4.m_gizmoParentTransform);
         this.m_startXfrm.mul((Matrix4fc)var4.m_gizmoOriginTransform);
         this.m_startXfrm.mul((Matrix4fc)var4.m_gizmoTransform);
         this.m_startXfrm.setRotationXYZ(0.0F, 0.0F, 0.0F);
         this.m_startInvXfrm.set((Matrix4fc)this.m_startXfrm);
         this.m_startInvXfrm.invert();
         this.m_trackAxis = var3;
         this.getPointOnAxis(var1, var2, var3, this.m_startXfrm, this.m_startPos);
      }

      void updateTracking(float var1, float var2) {
         Vector3f var3 = this.getPointOnAxis(var1, var2, this.m_trackAxis, this.m_startXfrm, UI3DScene.allocVector3f());
         if (this.m_currentPos.equals(var3)) {
            UI3DScene.releaseVector3f(var3);
         } else {
            UI3DScene.releaseVector3f(var3);
            this.m_currentPos.set((Vector3fc)var3);
            UI3DScene.StateData var4 = UI3DScene.this.stateDataMain();
            Vector3f var5 = (new Vector3f(this.m_currentPos)).sub(this.m_startPos);
            Vector3f var6;
            Vector3f var7;
            if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
               var6 = this.m_startInvXfrm.transformPosition(this.m_startPos, new Vector3f());
               var7 = this.m_startInvXfrm.transformPosition(this.m_currentPos, new Vector3f());
               Matrix4f var8 = (new Matrix4f(var4.m_gizmoParentTransform)).invert();
               var8.transformPosition(var6);
               var8.transformPosition(var7);
               var5.set((Vector3fc)var7).sub(var6);
            } else {
               var6 = this.m_startInvXfrm.transformPosition(this.m_startPos, new Vector3f());
               var7 = this.m_startInvXfrm.transformPosition(this.m_currentPos, new Vector3f());
               var5.set((Vector3fc)var7).sub(var6);
            }

            var5.x = (float)Math.floor((double)(var5.x * UI3DScene.this.gridMult())) / UI3DScene.this.gridMult();
            var5.y = (float)Math.floor((double)(var5.y * UI3DScene.this.gridMult())) / UI3DScene.this.gridMult();
            var5.z = (float)Math.floor((double)(var5.z * UI3DScene.this.gridMult())) / UI3DScene.this.gridMult();
            LuaManager.caller.pcall(UIManager.getDefaultThread(), UI3DScene.this.getTable().rawget("onGizmoChanged"), UI3DScene.this.table, var5);
         }
      }

      void stopTracking() {
         this.m_trackAxis = UI3DScene.Axis.None;
      }

      void render() {
         if (this.m_visible) {
            UI3DScene.StateData var1 = UI3DScene.this.stateDataRender();
            float var2 = UI3DScene.this.m_gizmoScale / var1.zoomMult() * 1000.0F;
            float var3 = this.LENGTH * var2;
            float var4 = this.THICKNESS * var2;
            float var5 = 0.1F * var2;
            Matrix4f var6 = UI3DScene.allocMatrix4f();
            var6.set((Matrix4fc)var1.m_gizmoParentTransform);
            var6.mul((Matrix4fc)var1.m_gizmoOriginTransform);
            var6.mul((Matrix4fc)var1.m_gizmoChildTransform);
            var6.mul((Matrix4fc)var1.m_gizmoTransform);
            if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
               var6.setRotationXYZ(0.0F, 0.0F, 0.0F);
            }

            var1.m_modelView.mul((Matrix4fc)var6, var6);
            PZGLUtil.pushAndLoadMatrix(5888, var6);
            UI3DScene.releaseMatrix4f(var6);
            if (!this.m_hideX) {
               GL11.glColor3f(var1.m_gizmoAxis == UI3DScene.Axis.X ? 1.0F : 0.5F, 0.0F, 0.0F);
               GL11.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
               GL11.glTranslatef(0.0F, 0.0F, var5);
               this.cylinder.draw(var4 / 2.0F, var4 / 2.0F, var3, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, var3);
               this.cylinder.draw(var4, var4, 0.1F * var2, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, -var5 - var3);
               GL11.glRotated(-90.0D, 0.0D, 1.0D, 0.0D);
            }

            if (!this.m_hideY) {
               GL11.glColor3f(0.0F, var1.m_gizmoAxis == UI3DScene.Axis.Y ? 1.0F : 0.5F, 0.0F);
               GL11.glRotated(-90.0D, 1.0D, 0.0D, 0.0D);
               GL11.glTranslatef(0.0F, 0.0F, var5);
               this.cylinder.draw(var4 / 2.0F, var4 / 2.0F, var3, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, var3);
               this.cylinder.draw(var4, var4, 0.1F * var2, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, -var5 - var3);
               GL11.glRotated(90.0D, 1.0D, 0.0D, 0.0D);
            }

            if (!this.m_hideZ) {
               GL11.glColor3f(0.0F, 0.0F, var1.m_gizmoAxis == UI3DScene.Axis.Z ? 1.0F : 0.5F);
               GL11.glTranslatef(0.0F, 0.0F, var5);
               this.cylinder.draw(var4 / 2.0F, var4 / 2.0F, var3, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, var3);
               this.cylinder.draw(var4, var4, 0.1F * var2, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, -0.1F - var3);
            }

            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            PZGLUtil.popMatrix(5888);
            this.renderLineToOrigin();
         }
      }
   }

   private final class TranslateGizmo extends UI3DScene.Gizmo {
      final Matrix4f m_startXfrm = new Matrix4f();
      final Matrix4f m_startInvXfrm = new Matrix4f();
      final Vector3f m_startPos = new Vector3f();
      final Vector3f m_currentPos = new Vector3f();
      UI3DScene.Axis m_trackAxis;
      Cylinder cylinder;

      private TranslateGizmo() {
         super();
         this.m_trackAxis = UI3DScene.Axis.None;
         this.cylinder = new Cylinder();
      }

      UI3DScene.Axis hitTest(float var1, float var2) {
         if (!this.m_visible) {
            return UI3DScene.Axis.None;
         } else {
            UI3DScene.StateData var3 = UI3DScene.this.stateDataMain();
            Matrix4f var4 = UI3DScene.allocMatrix4f();
            var4.set((Matrix4fc)var3.m_gizmoParentTransform);
            var4.mul((Matrix4fc)var3.m_gizmoOriginTransform);
            var4.mul((Matrix4fc)var3.m_gizmoChildTransform);
            var4.mul((Matrix4fc)var3.m_gizmoTransform);
            if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
               var4.setRotationXYZ(0.0F, 0.0F, 0.0F);
            }

            var2 = (float)UI3DScene.this.screenHeight() - var2;
            UI3DScene.Ray var5 = UI3DScene.this.getCameraRay(var1, var2, UI3DScene.allocRay());
            UI3DScene.Ray var6 = UI3DScene.allocRay();
            var4.transformPosition(var6.origin.set(0.0F, 0.0F, 0.0F));
            float var7 = UI3DScene.this.m_gizmoScale / var3.zoomMult() * 1000.0F;
            float var8 = this.LENGTH * var7;
            float var9 = this.THICKNESS * var7;
            float var10 = 0.1F * var7;
            var4.transformDirection(var6.direction.set(1.0F, 0.0F, 0.0F)).normalize();
            float var11 = UI3DScene.this.closest_distance_between_lines(var6, var5);
            float var12 = var6.t;
            float var13 = var5.t;
            if (var12 < var10 || var12 >= var10 + var8) {
               var12 = Float.MAX_VALUE;
               var11 = Float.MAX_VALUE;
            }

            float var14 = var6.direction.dot(var5.direction);
            var3.m_translateGizmoRenderData.m_hideX = Math.abs(var14) > 0.9F;
            var4.transformDirection(var6.direction.set(0.0F, 1.0F, 0.0F)).normalize();
            float var15 = UI3DScene.this.closest_distance_between_lines(var6, var5);
            float var16 = var6.t;
            float var17 = var5.t;
            if (var16 < var10 || var16 >= var10 + var8) {
               var16 = Float.MAX_VALUE;
               var15 = Float.MAX_VALUE;
            }

            float var18 = var6.direction.dot(var5.direction);
            var3.m_translateGizmoRenderData.m_hideY = Math.abs(var18) > 0.9F;
            var4.transformDirection(var6.direction.set(0.0F, 0.0F, 1.0F)).normalize();
            float var19 = UI3DScene.this.closest_distance_between_lines(var6, var5);
            float var20 = var6.t;
            float var21 = var5.t;
            if (var20 < var10 || var20 >= var10 + var8) {
               var20 = Float.MAX_VALUE;
               var19 = Float.MAX_VALUE;
            }

            float var22 = var6.direction.dot(var5.direction);
            var3.m_translateGizmoRenderData.m_hideZ = Math.abs(var22) > 0.9F;
            UI3DScene.releaseRay(var6);
            UI3DScene.releaseRay(var5);
            UI3DScene.releaseMatrix4f(var4);
            if (var12 >= var10 && var12 < var10 + var8 && var11 < var15 && var11 < var19) {
               return var11 <= var9 / 2.0F ? UI3DScene.Axis.X : UI3DScene.Axis.None;
            } else if (var16 >= var10 && var16 < var10 + var8 && var15 < var11 && var15 < var19) {
               return var15 <= var9 / 2.0F ? UI3DScene.Axis.Y : UI3DScene.Axis.None;
            } else if (var20 >= var10 && var20 < var10 + var8 && var19 < var11 && var19 < var15) {
               return var19 <= var9 / 2.0F ? UI3DScene.Axis.Z : UI3DScene.Axis.None;
            } else {
               return UI3DScene.Axis.None;
            }
         }
      }

      void startTracking(float var1, float var2, UI3DScene.Axis var3) {
         UI3DScene.StateData var4 = UI3DScene.this.stateDataMain();
         this.m_startXfrm.set((Matrix4fc)var4.m_gizmoParentTransform);
         this.m_startXfrm.mul((Matrix4fc)var4.m_gizmoOriginTransform);
         this.m_startXfrm.mul((Matrix4fc)var4.m_gizmoChildTransform);
         this.m_startXfrm.mul((Matrix4fc)var4.m_gizmoTransform);
         if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
            this.m_startXfrm.setRotationXYZ(0.0F, 0.0F, 0.0F);
         }

         this.m_startInvXfrm.set((Matrix4fc)this.m_startXfrm);
         this.m_startInvXfrm.invert();
         this.m_trackAxis = var3;
         this.getPointOnAxis(var1, var2, var3, this.m_startXfrm, this.m_startPos);
      }

      void updateTracking(float var1, float var2) {
         Vector3f var3 = this.getPointOnAxis(var1, var2, this.m_trackAxis, this.m_startXfrm, UI3DScene.allocVector3f());
         if (this.m_currentPos.equals(var3)) {
            UI3DScene.releaseVector3f(var3);
         } else {
            UI3DScene.releaseVector3f(var3);
            this.m_currentPos.set((Vector3fc)var3);
            UI3DScene.StateData var4 = UI3DScene.this.stateDataMain();
            Vector3f var5 = (new Vector3f(this.m_currentPos)).sub(this.m_startPos);
            Vector3f var6;
            Vector3f var7;
            Matrix4f var8;
            if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
               var6 = this.m_startInvXfrm.transformPosition(this.m_startPos, UI3DScene.allocVector3f());
               var7 = this.m_startInvXfrm.transformPosition(this.m_currentPos, UI3DScene.allocVector3f());
               var8 = UI3DScene.allocMatrix4f();
               var8.set((Matrix4fc)var4.m_gizmoParentTransform);
               var8.mul((Matrix4fc)var4.m_gizmoOriginTransform);
               var8.mul((Matrix4fc)var4.m_gizmoChildTransform);
               var8.invert();
               var8.transformPosition(var6);
               var8.transformPosition(var7);
               UI3DScene.releaseMatrix4f(var8);
               var5.set((Vector3fc)var7).sub(var6);
               UI3DScene.releaseVector3f(var6);
               UI3DScene.releaseVector3f(var7);
            } else {
               var6 = this.m_startInvXfrm.transformPosition(this.m_startPos, UI3DScene.allocVector3f());
               var7 = this.m_startInvXfrm.transformPosition(this.m_currentPos, UI3DScene.allocVector3f());
               var8 = UI3DScene.allocMatrix4f();
               var8.set((Matrix4fc)var4.m_gizmoTransform);
               var8.transformPosition(var6);
               var8.transformPosition(var7);
               UI3DScene.releaseMatrix4f(var8);
               var5.set((Vector3fc)var7).sub(var6);
               UI3DScene.releaseVector3f(var6);
               UI3DScene.releaseVector3f(var7);
            }

            var5.x = (float)Math.floor((double)(var5.x * UI3DScene.this.gridMult())) / UI3DScene.this.gridMult();
            var5.y = (float)Math.floor((double)(var5.y * UI3DScene.this.gridMult())) / UI3DScene.this.gridMult();
            var5.z = (float)Math.floor((double)(var5.z * UI3DScene.this.gridMult())) / UI3DScene.this.gridMult();
            if (var4.m_selectedAttachmentIsChildAttachment) {
               var5.mul(-1.0F);
            }

            LuaManager.caller.pcall(UIManager.getDefaultThread(), UI3DScene.this.getTable().rawget("onGizmoChanged"), UI3DScene.this.table, var5);
         }
      }

      void stopTracking() {
         this.m_trackAxis = UI3DScene.Axis.None;
      }

      void render() {
         if (this.m_visible) {
            UI3DScene.StateData var1 = UI3DScene.this.stateDataRender();
            Matrix4f var2 = UI3DScene.allocMatrix4f();
            var2.set((Matrix4fc)var1.m_gizmoParentTransform);
            var2.mul((Matrix4fc)var1.m_gizmoOriginTransform);
            var2.mul((Matrix4fc)var1.m_gizmoChildTransform);
            var2.mul((Matrix4fc)var1.m_gizmoTransform);
            Vector3f var3 = var2.getScale(UI3DScene.allocVector3f());
            var2.scale(1.0F / var3.x, 1.0F / var3.y, 1.0F / var3.z);
            UI3DScene.releaseVector3f(var3);
            if (UI3DScene.this.m_transformMode == UI3DScene.TransformMode.Global) {
               var2.setRotationXYZ(0.0F, 0.0F, 0.0F);
            }

            var1.m_modelView.mul((Matrix4fc)var2, var2);
            PZGLUtil.pushAndLoadMatrix(5888, var2);
            UI3DScene.releaseMatrix4f(var2);
            float var7 = UI3DScene.this.m_gizmoScale / var1.zoomMult() * 1000.0F;
            float var4 = this.THICKNESS * var7;
            float var5 = this.LENGTH * var7;
            float var6 = 0.1F * var7;
            if (!var1.m_translateGizmoRenderData.m_hideX) {
               GL11.glColor3f(var1.m_gizmoAxis == UI3DScene.Axis.X ? 1.0F : 0.5F, 0.0F, 0.0F);
               GL11.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
               GL11.glTranslatef(0.0F, 0.0F, var6);
               this.cylinder.draw(var4 / 2.0F, var4 / 2.0F, var5, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, var5);
               this.cylinder.draw(var4 / 2.0F * 2.0F, 0.0F, 0.1F * var7, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, -var6 - var5);
               GL11.glRotated(-90.0D, 0.0D, 1.0D, 0.0D);
            }

            if (!var1.m_translateGizmoRenderData.m_hideY) {
               GL11.glColor3f(0.0F, var1.m_gizmoAxis == UI3DScene.Axis.Y ? 1.0F : 0.5F, 0.0F);
               GL11.glRotated(-90.0D, 1.0D, 0.0D, 0.0D);
               GL11.glTranslatef(0.0F, 0.0F, var6);
               this.cylinder.draw(var4 / 2.0F, var4 / 2.0F, var5, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, var5);
               this.cylinder.draw(var4 / 2.0F * 2.0F, 0.0F, 0.1F * var7, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, -var6 - var5);
               GL11.glRotated(90.0D, 1.0D, 0.0D, 0.0D);
            }

            if (!var1.m_translateGizmoRenderData.m_hideZ) {
               GL11.glColor3f(0.0F, 0.0F, var1.m_gizmoAxis == UI3DScene.Axis.Z ? 1.0F : 0.5F);
               GL11.glTranslatef(0.0F, 0.0F, var6);
               this.cylinder.draw(var4 / 2.0F, var4 / 2.0F, var5, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, var5);
               this.cylinder.draw(var4 / 2.0F * 2.0F, 0.0F, 0.1F * var7, 8, 1);
               GL11.glTranslatef(0.0F, 0.0F, -var6 - var5);
            }

            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            PZGLUtil.popMatrix(5888);
            this.renderLineToOrigin();
         }
      }
   }

   private abstract static class SceneObject {
      final UI3DScene m_scene;
      final String m_id;
      boolean m_visible = true;
      final Vector3f m_translate = new Vector3f();
      final Vector3f m_rotate = new Vector3f();
      UI3DScene.SceneObject m_parent;
      String m_attachment;
      String m_parentAttachment;
      boolean m_autoRotate = false;
      float m_autoRotateAngle = 0.0F;

      SceneObject(UI3DScene var1, String var2) {
         this.m_scene = var1;
         this.m_id = var2;
      }

      abstract UI3DScene.SceneObjectRenderData renderMain();

      Matrix4f getLocalTransform(Matrix4f var1) {
         var1.translation(this.m_translate);
         float var2 = this.m_rotate.y;
         if (this.m_autoRotate) {
            var2 += this.m_autoRotateAngle;
         }

         var1.rotateXYZ(this.m_rotate.x * 0.017453292F, var2 * 0.017453292F, this.m_rotate.z * 0.017453292F);
         if (this.m_attachment != null) {
            Matrix4f var3 = this.getAttachmentTransform(this.m_attachment, UI3DScene.allocMatrix4f());
            var3.invert();
            var1.mul((Matrix4fc)var3);
            UI3DScene.releaseMatrix4f(var3);
         }

         return var1;
      }

      Matrix4f getGlobalTransform(Matrix4f var1) {
         this.getLocalTransform(var1);
         if (this.m_parent != null) {
            Matrix4f var2;
            if (this.m_parentAttachment != null) {
               var2 = this.m_parent.getAttachmentTransform(this.m_parentAttachment, UI3DScene.allocMatrix4f());
               var2.mul((Matrix4fc)var1, var1);
               UI3DScene.releaseMatrix4f(var2);
            }

            var2 = this.m_parent.getGlobalTransform(UI3DScene.allocMatrix4f());
            var2.mul((Matrix4fc)var1, var1);
            UI3DScene.releaseMatrix4f(var2);
         }

         return var1;
      }

      Matrix4f getAttachmentTransform(String var1, Matrix4f var2) {
         var2.identity();
         return var2;
      }
   }

   private static final class OriginAttachment extends UI3DScene.SceneObject {
      UI3DScene.SceneObject m_object;
      String m_attachmentName;

      OriginAttachment(UI3DScene var1) {
         super(var1, "OriginAttachment");
      }

      UI3DScene.SceneObjectRenderData renderMain() {
         return null;
      }

      Matrix4f getGlobalTransform(Matrix4f var1) {
         return this.m_object.getAttachmentTransform(this.m_attachmentName, var1);
      }
   }

   private static final class OriginBone extends UI3DScene.SceneObject {
      UI3DScene.SceneCharacter m_character;
      String m_boneName;

      OriginBone(UI3DScene var1) {
         super(var1, "OriginBone");
      }

      UI3DScene.SceneObjectRenderData renderMain() {
         return null;
      }

      Matrix4f getGlobalTransform(Matrix4f var1) {
         return this.m_character.getBoneMatrix(this.m_boneName, var1);
      }
   }

   private static final class OriginGizmo extends UI3DScene.SceneObject {
      OriginGizmo(UI3DScene var1) {
         super(var1, "OriginGizmo");
      }

      UI3DScene.SceneObjectRenderData renderMain() {
         return null;
      }
   }

   private final class OverlaysDrawer extends TextureDraw.GenericDrawer {
      void init() {
         UI3DScene.StateData var1 = UI3DScene.this.stateDataMain();
         UI3DScene.s_aabbPool.release((List)var1.m_aabb);
         var1.m_aabb.clear();

         int var2;
         for(var2 = 0; var2 < UI3DScene.this.m_aabb.size(); ++var2) {
            UI3DScene.AABB var3 = (UI3DScene.AABB)UI3DScene.this.m_aabb.get(var2);
            var1.m_aabb.add(((UI3DScene.AABB)UI3DScene.s_aabbPool.alloc()).set(var3));
         }

         UI3DScene.s_box3DPool.release((List)var1.m_box3D);
         var1.m_box3D.clear();

         for(var2 = 0; var2 < UI3DScene.this.m_box3D.size(); ++var2) {
            UI3DScene.Box3D var4 = (UI3DScene.Box3D)UI3DScene.this.m_box3D.get(var2);
            var1.m_box3D.add(((UI3DScene.Box3D)UI3DScene.s_box3DPool.alloc()).set(var4));
         }

         UI3DScene.s_posRotPool.release((List)var1.m_axes);
         var1.m_axes.clear();

         for(var2 = 0; var2 < UI3DScene.this.m_axes.size(); ++var2) {
            UI3DScene.PositionRotation var5 = (UI3DScene.PositionRotation)UI3DScene.this.m_axes.get(var2);
            var1.m_axes.add(((UI3DScene.PositionRotation)UI3DScene.s_posRotPool.alloc()).set(var5));
         }

      }

      public void render() {
         UI3DScene.StateData var1 = UI3DScene.this.stateDataRender();
         PZGLUtil.pushAndLoadMatrix(5889, var1.m_projection);
         PZGLUtil.pushAndLoadMatrix(5888, var1.m_modelView);
         UI3DScene.vboLines.setOffset(0.0F, 0.0F, 0.0F);
         if (UI3DScene.this.m_bDrawGrid) {
            UI3DScene.this.renderGrid();
         }

         int var2;
         for(var2 = 0; var2 < var1.m_aabb.size(); ++var2) {
            UI3DScene.AABB var3 = (UI3DScene.AABB)var1.m_aabb.get(var2);
            UI3DScene.this.renderAABB(var3.x, var3.y, var3.z, var3.w, var3.h, var3.L, var3.r, var3.g, var3.b);
         }

         for(var2 = 0; var2 < var1.m_box3D.size(); ++var2) {
            UI3DScene.Box3D var4 = (UI3DScene.Box3D)var1.m_box3D.get(var2);
            UI3DScene.this.renderBox3D(var4.x, var4.y, var4.z, var4.w, var4.h, var4.L, var4.rx, var4.ry, var4.rz, var4.r, var4.g, var4.b);
         }

         for(var2 = 0; var2 < var1.m_axes.size(); ++var2) {
            UI3DScene.this.renderAxis((UI3DScene.PositionRotation)var1.m_axes.get(var2));
         }

         UI3DScene.vboLines.flush();
         if (var1.m_gizmo != null) {
            var1.m_gizmo.render();
         }

         UI3DScene.vboLines.flush();
         PZGLUtil.popMatrix(5889);
         PZGLUtil.popMatrix(5888);
      }
   }

   private static class SceneObjectRenderData {
      UI3DScene.SceneObject m_object;
      final Matrix4f m_transform = new Matrix4f();
      private static final ObjectPool s_pool = new ObjectPool(UI3DScene.SceneObjectRenderData::new);

      UI3DScene.SceneObjectRenderData init(UI3DScene.SceneObject var1) {
         this.m_object = var1;
         var1.getGlobalTransform(this.m_transform);
         return this;
      }

      void release() {
         s_pool.release((Object)this);
      }
   }

   private abstract class Gizmo {
      float LENGTH = 0.5F;
      float THICKNESS = 0.05F;
      boolean m_visible = false;

      abstract UI3DScene.Axis hitTest(float var1, float var2);

      abstract void startTracking(float var1, float var2, UI3DScene.Axis var3);

      abstract void updateTracking(float var1, float var2);

      abstract void stopTracking();

      abstract void render();

      Vector3f getPointOnAxis(float var1, float var2, UI3DScene.Axis var3, Matrix4f var4, Vector3f var5) {
         UI3DScene.StateData var6 = UI3DScene.this.stateDataMain();
         var2 = (float)UI3DScene.this.screenHeight() - var2;
         UI3DScene.Ray var7 = UI3DScene.this.getCameraRay(var1, var2, UI3DScene.allocRay());
         UI3DScene.Ray var8 = UI3DScene.allocRay();
         var4.transformPosition(var8.origin.set(0.0F, 0.0F, 0.0F));
         switch(var3) {
         case X:
            var8.direction.set(1.0F, 0.0F, 0.0F);
            break;
         case Y:
            var8.direction.set(0.0F, 1.0F, 0.0F);
            break;
         case Z:
            var8.direction.set(0.0F, 0.0F, 1.0F);
         }

         var4.transformDirection(var8.direction).normalize();
         UI3DScene.this.closest_distance_between_lines(var8, var7);
         UI3DScene.releaseRay(var7);
         var5.set((Vector3fc)var8.direction).mul(var8.t).add(var8.origin);
         UI3DScene.releaseRay(var8);
         return var5;
      }

      boolean hitTestRect(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
         float var9 = UI3DScene.this.sceneToUIX(var3, var4, var5);
         float var10 = UI3DScene.this.sceneToUIY(var3, var4, var5);
         float var11 = UI3DScene.this.sceneToUIX(var6, var7, var8);
         float var12 = UI3DScene.this.sceneToUIY(var6, var7, var8);
         float var13 = this.THICKNESS / 2.0F * UI3DScene.this.zoomMult();
         float var14 = this.THICKNESS / 2.0F * UI3DScene.this.zoomMult();
         float var15 = Math.min(var9 - var13, var11 - var13);
         float var16 = Math.max(var9 + var13, var11 + var13);
         float var17 = Math.min(var10 - var14, var12 - var14);
         float var18 = Math.max(var10 + var14, var12 + var14);
         return var1 >= var15 && var2 >= var17 && var1 < var16 && var2 < var18;
      }

      void renderLineToOrigin() {
         UI3DScene.StateData var1 = UI3DScene.this.stateDataRender();
         if (var1.m_hasGizmoOrigin) {
            UI3DScene.this.renderAxis(var1.m_gizmoTranslate, var1.m_gizmoRotate);
            Vector3f var2 = var1.m_gizmoTranslate;
            UI3DScene.vboLines.flush();
            Matrix4f var3 = UI3DScene.allocMatrix4f();
            var3.set((Matrix4fc)var1.m_modelView);
            var3.mul((Matrix4fc)var1.m_gizmoParentTransform);
            var3.mul((Matrix4fc)var1.m_gizmoOriginTransform);
            var3.mul((Matrix4fc)var1.m_gizmoChildTransform);
            PZGLUtil.pushAndLoadMatrix(5888, var3);
            UI3DScene.releaseMatrix4f(var3);
            UI3DScene.vboLines.setLineWidth(1.0F);
            UI3DScene.vboLines.addLine(var2.x, var2.y, var2.z, 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F);
            UI3DScene.vboLines.flush();
            PZGLUtil.popMatrix(5888);
         }
      }
   }

   static enum Axis {
      None,
      X,
      Y,
      Z;

      // $FF: synthetic method
      private static UI3DScene.Axis[] $values() {
         return new UI3DScene.Axis[]{None, X, Y, Z};
      }
   }

   public static final class Plane {
      public final Vector3f point = new Vector3f();
      public final Vector3f normal = new Vector3f();

      public Plane() {
      }

      public Plane(Vector3f var1, Vector3f var2) {
         this.point.set((Vector3fc)var2);
         this.normal.set((Vector3fc)var1);
      }

      public UI3DScene.Plane set(Vector3f var1, Vector3f var2) {
         this.point.set((Vector3fc)var2);
         this.normal.set((Vector3fc)var1);
         return this;
      }
   }

   public static final class Ray {
      public final Vector3f origin = new Vector3f();
      public final Vector3f direction = new Vector3f();
      public float t;

      Ray() {
      }

      Ray(UI3DScene.Ray var1) {
         this.origin.set((Vector3fc)var1.origin);
         this.direction.set((Vector3fc)var1.direction);
         this.t = var1.t;
      }
   }

   private static final class SceneCharacter extends UI3DScene.SceneObject {
      final AnimatedModel m_animatedModel = new AnimatedModel();
      boolean m_bShowBones = false;
      boolean m_bClearDepthBuffer = true;
      boolean m_bUseDeferredMovement = false;

      SceneCharacter(UI3DScene var1, String var2) {
         super(var1, var2);
         this.m_animatedModel.setAnimSetName("player-vehicle");
         this.m_animatedModel.setState("idle");
         this.m_animatedModel.setOutfitName("Naked", false, false);
         this.m_animatedModel.getHumanVisual().setHairModel("Bald");
         this.m_animatedModel.getHumanVisual().setBeardModel("");
         this.m_animatedModel.getHumanVisual().setSkinTextureIndex(0);
         this.m_animatedModel.setAlpha(0.5F);
         this.m_animatedModel.setAnimate(false);
      }

      UI3DScene.SceneObjectRenderData renderMain() {
         this.m_animatedModel.update();
         UI3DScene.CharacterRenderData var1 = (UI3DScene.CharacterRenderData)UI3DScene.CharacterRenderData.s_pool.alloc();
         var1.initCharacter(this);
         SpriteRenderer.instance.drawGeneric(var1.m_drawer);
         return var1;
      }

      Matrix4f getLocalTransform(Matrix4f var1) {
         var1.identity();
         var1.rotateY(3.1415927F);
         var1.translate(-this.m_translate.x, this.m_translate.y, this.m_translate.z);
         var1.scale(-1.5F, 1.5F, 1.5F);
         float var2 = this.m_rotate.y;
         if (this.m_autoRotate) {
            var2 += this.m_autoRotateAngle;
         }

         var1.rotateXYZ(this.m_rotate.x * 0.017453292F, var2 * 0.017453292F, this.m_rotate.z * 0.017453292F);
         if (this.m_animatedModel.getAnimationPlayer().getMultiTrack().getTracks().isEmpty()) {
            return var1;
         } else {
            if (this.m_bUseDeferredMovement) {
               AnimationMultiTrack var3 = this.m_animatedModel.getAnimationPlayer().getMultiTrack();
               float var4 = ((AnimationTrack)var3.getTracks().get(0)).getCurrentDeferredRotation();
               org.lwjgl.util.vector.Vector3f var5 = new org.lwjgl.util.vector.Vector3f();
               ((AnimationTrack)var3.getTracks().get(0)).getCurrentDeferredPosition(var5);
               var1.translate(var5.x, var5.y, var5.z);
            }

            return var1;
         }
      }

      Matrix4f getAttachmentTransform(String var1, Matrix4f var2) {
         var2.identity();
         boolean var3 = this.m_animatedModel.isFemale();
         ModelScript var4 = ScriptManager.instance.getModelScript(var3 ? "FemaleBody" : "MaleBody");
         if (var4 == null) {
            return var2;
         } else {
            ModelAttachment var5 = var4.getAttachmentById(var1);
            if (var5 == null) {
               return var2;
            } else {
               var2.translation(var5.getOffset());
               Vector3f var6 = var5.getRotate();
               var2.rotateXYZ(var6.x * 0.017453292F, var6.y * 0.017453292F, var6.z * 0.017453292F);
               if (var5.getBone() != null) {
                  Matrix4f var7 = this.getBoneMatrix(var5.getBone(), UI3DScene.allocMatrix4f());
                  var7.mul((Matrix4fc)var2, var2);
                  UI3DScene.releaseMatrix4f(var7);
               }

               return var2;
            }
         }
      }

      int hitTestBone(int var1, UI3DScene.Ray var2, UI3DScene.Ray var3, Matrix4f var4) {
         AnimationPlayer var5 = this.m_animatedModel.getAnimationPlayer();
         SkinningData var6 = var5.getSkinningData();
         int var7 = (Integer)var6.SkeletonHierarchy.get(var1);
         if (var7 == -1) {
            return -1;
         } else {
            org.lwjgl.util.vector.Matrix4f var8 = var5.modelTransforms[var7];
            var2.origin.set(var8.m03, var8.m13, var8.m23);
            var4.transformPosition(var2.origin);
            var8 = var5.modelTransforms[var1];
            Vector3f var9 = UI3DScene.allocVector3f();
            var9.set(var8.m03, var8.m13, var8.m23);
            var4.transformPosition(var9);
            var2.direction.set((Vector3fc)var9).sub(var2.origin);
            float var10 = var2.direction.length();
            var2.direction.normalize();
            this.m_scene.closest_distance_between_lines(var3, var2);
            float var11 = this.m_scene.sceneToUIX(var3.origin.x + var3.direction.x * var3.t, var3.origin.y + var3.direction.y * var3.t, var3.origin.z + var3.direction.z * var3.t);
            float var12 = this.m_scene.sceneToUIY(var3.origin.x + var3.direction.x * var3.t, var3.origin.y + var3.direction.y * var3.t, var3.origin.z + var3.direction.z * var3.t);
            float var13 = this.m_scene.sceneToUIX(var2.origin.x + var2.direction.x * var2.t, var2.origin.y + var2.direction.y * var2.t, var2.origin.z + var2.direction.z * var2.t);
            float var14 = this.m_scene.sceneToUIY(var2.origin.x + var2.direction.x * var2.t, var2.origin.y + var2.direction.y * var2.t, var2.origin.z + var2.direction.z * var2.t);
            int var15 = -1;
            float var16 = 10.0F;
            float var17 = (float)Math.sqrt(Math.pow((double)(var13 - var11), 2.0D) + Math.pow((double)(var14 - var12), 2.0D));
            if (var17 < var16) {
               if (var2.t >= 0.0F && var2.t < var10 * 0.5F) {
                  var15 = var7;
               } else if (var2.t >= var10 * 0.5F && var2.t < var10) {
                  var15 = var1;
               }
            }

            UI3DScene.releaseVector3f(var9);
            return var15;
         }
      }

      String pickBone(float var1, float var2) {
         if (this.m_animatedModel.getAnimationPlayer().modelTransforms == null) {
            return "";
         } else {
            var2 = (float)this.m_scene.screenHeight() - var2;
            UI3DScene.Ray var3 = this.m_scene.getCameraRay(var1, var2, UI3DScene.allocRay());
            Matrix4f var4 = UI3DScene.allocMatrix4f();
            this.getLocalTransform(var4);
            UI3DScene.Ray var5 = UI3DScene.allocRay();
            int var6 = -1;

            for(int var7 = 0; var7 < this.m_animatedModel.getAnimationPlayer().modelTransforms.length; ++var7) {
               var6 = this.hitTestBone(var7, var5, var3, var4);
               if (var6 != -1) {
                  break;
               }
            }

            UI3DScene.releaseRay(var5);
            UI3DScene.releaseRay(var3);
            UI3DScene.releaseMatrix4f(var4);
            return var6 == -1 ? "" : this.m_animatedModel.getAnimationPlayer().getSkinningData().getBoneAt(var6).Name;
         }
      }

      Matrix4f getBoneMatrix(String var1, Matrix4f var2) {
         var2.identity();
         if (this.m_animatedModel.getAnimationPlayer().modelTransforms == null) {
            return var2;
         } else {
            SkinningBone var3 = this.m_animatedModel.getAnimationPlayer().getSkinningData().getBone(var1);
            if (var3 == null) {
               return var2;
            } else {
               var2 = PZMath.convertMatrix(this.m_animatedModel.getAnimationPlayer().modelTransforms[var3.Index], var2);
               var2.transpose();
               return var2;
            }
         }
      }

      UI3DScene.PositionRotation getBoneAxis(String var1, UI3DScene.PositionRotation var2) {
         Matrix4f var3 = UI3DScene.allocMatrix4f().identity();
         var3.getTranslation(var2.pos);
         UI3DScene.releaseMatrix4f(var3);
         Quaternionf var4 = var3.getUnnormalizedRotation(UI3DScene.allocQuaternionf());
         var4.getEulerAnglesXYZ(var2.rot);
         UI3DScene.releaseQuaternionf(var4);
         return var2;
      }
   }

   private static final class SceneModel extends UI3DScene.SceneObject {
      ModelScript m_modelScript;
      Model m_model;

      SceneModel(UI3DScene var1, String var2, ModelScript var3, Model var4) {
         super(var1, var2);
         Objects.requireNonNull(var3);
         Objects.requireNonNull(var4);
         this.m_modelScript = var3;
         this.m_model = var4;
      }

      UI3DScene.SceneObjectRenderData renderMain() {
         if (!this.m_model.isReady()) {
            return null;
         } else {
            UI3DScene.ModelRenderData var1 = (UI3DScene.ModelRenderData)UI3DScene.ModelRenderData.s_pool.alloc();
            var1.initModel(this);
            SpriteRenderer.instance.drawGeneric(var1.m_drawer);
            return var1;
         }
      }

      Matrix4f getLocalTransform(Matrix4f var1) {
         super.getLocalTransform(var1);
         return var1;
      }

      Matrix4f getAttachmentTransform(String var1, Matrix4f var2) {
         var2.identity();
         ModelAttachment var3 = this.m_modelScript.getAttachmentById(var1);
         if (var3 == null) {
            return var2;
         } else {
            var2.translation(var3.getOffset());
            Vector3f var4 = var3.getRotate();
            var2.rotateXYZ(var4.x * 0.017453292F, var4.y * 0.017453292F, var4.z * 0.017453292F);
            return var2;
         }
      }
   }

   private static final class SceneVehicle extends UI3DScene.SceneObject {
      String m_scriptName = "Base.ModernCar";
      VehicleScript m_script;
      Model m_model;

      SceneVehicle(UI3DScene var1, String var2) {
         super(var1, var2);
         this.setScriptName("Base.ModernCar");
      }

      UI3DScene.SceneObjectRenderData renderMain() {
         if (this.m_script == null) {
            this.m_model = null;
            return null;
         } else {
            String var1 = this.m_script.getModel().file;
            this.m_model = ModelManager.instance.getLoadedModel(var1);
            if (this.m_model == null) {
               return null;
            } else {
               if (this.m_script.getSkinCount() > 0) {
                  this.m_model.tex = Texture.getSharedTexture("media/textures/" + this.m_script.getSkin(0).texture + ".png");
               }

               UI3DScene.VehicleRenderData var2 = (UI3DScene.VehicleRenderData)UI3DScene.VehicleRenderData.s_pool.alloc();
               var2.initVehicle(this);
               UI3DScene.SetModelCamera var3 = (UI3DScene.SetModelCamera)UI3DScene.s_SetModelCameraPool.alloc();
               SpriteRenderer.instance.drawGeneric(var3.init(this.m_scene.m_VehicleSceneModelCamera, var2));
               SpriteRenderer.instance.drawGeneric(var2.m_drawer);
               return var2;
            }
         }
      }

      void setScriptName(String var1) {
         this.m_scriptName = var1;
         this.m_script = ScriptManager.instance.getVehicle(var1);
      }
   }

   private static final class PositionRotation {
      final Vector3f pos = new Vector3f();
      final Vector3f rot = new Vector3f();

      UI3DScene.PositionRotation set(UI3DScene.PositionRotation var1) {
         this.pos.set((Vector3fc)var1.pos);
         this.rot.set((Vector3fc)var1.rot);
         return this;
      }

      UI3DScene.PositionRotation set(float var1, float var2, float var3) {
         this.pos.set(var1, var2, var3);
         this.rot.set(0.0F, 0.0F, 0.0F);
         return this;
      }

      UI3DScene.PositionRotation set(float var1, float var2, float var3, float var4, float var5, float var6) {
         this.pos.set(var1, var2, var3);
         this.rot.set(var4, var5, var6);
         return this;
      }
   }

   private static final class AABB {
      float x;
      float y;
      float z;
      float w;
      float h;
      float L;
      float r;
      float g;
      float b;

      UI3DScene.AABB set(UI3DScene.AABB var1) {
         return this.set(var1.x, var1.y, var1.z, var1.w, var1.h, var1.L, var1.r, var1.g, var1.b);
      }

      UI3DScene.AABB set(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.w = var4;
         this.h = var5;
         this.L = var6;
         this.r = var7;
         this.g = var8;
         this.b = var9;
         return this;
      }
   }

   private static final class Box3D {
      float x;
      float y;
      float z;
      float w;
      float h;
      float L;
      float rx;
      float ry;
      float rz;
      float r;
      float g;
      float b;

      UI3DScene.Box3D set(UI3DScene.Box3D var1) {
         return this.set(var1.x, var1.y, var1.z, var1.w, var1.h, var1.L, var1.rx, var1.ry, var1.rz, var1.r, var1.g, var1.b);
      }

      UI3DScene.Box3D set(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, float var11, float var12) {
         this.x = var1;
         this.y = var2;
         this.z = var3;
         this.w = var4;
         this.h = var5;
         this.L = var6;
         this.rx = var7;
         this.ry = var8;
         this.rz = var9;
         this.r = var10;
         this.g = var11;
         this.b = var12;
         return this;
      }
   }

   private static final class Circle {
      final Vector3f center = new Vector3f();
      final Vector3f orientation = new Vector3f();
      float radius = 1.0F;
   }

   private static final class VehicleDrawer extends TextureDraw.GenericDrawer {
      UI3DScene.SceneVehicle m_vehicle;
      UI3DScene.VehicleRenderData m_renderData;
      boolean bRendered;
      final float[] fzeroes = new float[16];
      final Vector3f paintColor = new Vector3f(0.0F, 0.5F, 0.5F);
      final Matrix4f IDENTITY = new Matrix4f();

      public void init(UI3DScene.SceneVehicle var1, UI3DScene.VehicleRenderData var2) {
         this.m_vehicle = var1;
         this.m_renderData = var2;
         this.bRendered = false;
      }

      public void render() {
         for(int var1 = 0; var1 < this.m_renderData.m_models.size(); ++var1) {
            GL11.glPushAttrib(1048575);
            GL11.glPushClientAttrib(-1);
            this.render(var1);
            GL11.glPopAttrib();
            GL11.glPopClientAttrib();
            Texture.lastTextureID = -1;
            SpriteRenderer.ringBuffer.restoreBoundTextures = true;
            SpriteRenderer.ringBuffer.restoreVBOs = true;
         }

      }

      private void render(int var1) {
         this.m_renderData.m_transform.set((Matrix4fc)this.m_renderData.m_transforms.get(var1));
         ModelCamera.instance.Begin();
         Model var2 = (Model)this.m_renderData.m_models.get(var1);
         boolean var3 = var2.bStatic;
         Shader var4;
         if (Core.bDebug && DebugOptions.instance.ModelRenderWireframe.getValue()) {
            GL11.glPolygonMode(1032, 6913);
            GL11.glEnable(2848);
            GL11.glLineWidth(0.75F);
            var4 = ShaderManager.instance.getOrCreateShader("vehicle_wireframe", var3);
            if (var4 != null) {
               var4.Start();
               var4.setTransformMatrix(this.IDENTITY.identity(), false);
               var2.Mesh.Draw(var4);
               var4.End();
            }

            GL11.glDisable(2848);
            ModelCamera.instance.End();
         } else {
            var4 = var2.Effect;
            int var5;
            if (var4 != null && var4.isVehicleShader()) {
               GL11.glDepthFunc(513);
               GL11.glDepthMask(true);
               GL11.glDepthRange(0.0D, 1.0D);
               GL11.glEnable(2929);
               GL11.glColor3f(1.0F, 1.0F, 1.0F);
               var4.Start();
               if (var2.tex != null) {
                  var4.setTexture(var2.tex, "Texture0", 0);
                  GL11.glTexEnvi(8960, 8704, 7681);
                  if (this.m_vehicle.m_script.getSkinCount() > 0 && this.m_vehicle.m_script.getSkin(0).textureMask != null) {
                     Texture var6 = Texture.getSharedTexture("media/textures/" + this.m_vehicle.m_script.getSkin(0).textureMask + ".png");
                     var4.setTexture(var6, "TextureMask", 2);
                     GL11.glTexEnvi(8960, 8704, 7681);
                  }
               }

               var4.setDepthBias(0.0F);
               var4.setAmbient(1.0F);
               var4.setLightingAmount(1.0F);
               var4.setHueShift(0.0F);
               var4.setTint(1.0F, 1.0F, 1.0F);
               var4.setAlpha(1.0F);

               for(var5 = 0; var5 < 5; ++var5) {
                  var4.setLight(var5, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Float.NaN, 0.0F, 0.0F, 0.0F, (IsoMovingObject)null);
               }

               var4.setTextureUninstall1(this.fzeroes);
               var4.setTextureUninstall2(this.fzeroes);
               var4.setTextureLightsEnables2(this.fzeroes);
               var4.setTextureDamage1Enables1(this.fzeroes);
               var4.setTextureDamage1Enables2(this.fzeroes);
               var4.setTextureDamage2Enables1(this.fzeroes);
               var4.setTextureDamage2Enables2(this.fzeroes);
               var4.setMatrixBlood1(this.fzeroes, this.fzeroes);
               var4.setMatrixBlood2(this.fzeroes, this.fzeroes);
               var4.setTextureRustA(0.0F);
               var4.setTexturePainColor(this.paintColor, 1.0F);
               var4.setTransformMatrix(this.IDENTITY.identity(), false);
               var2.Mesh.Draw(var4);
               var4.End();
            } else if (var4 != null && var2.Mesh != null && var2.Mesh.isReady()) {
               GL11.glDepthFunc(513);
               GL11.glDepthMask(true);
               GL11.glDepthRange(0.0D, 1.0D);
               GL11.glEnable(2929);
               GL11.glColor3f(1.0F, 1.0F, 1.0F);
               var4.Start();
               if (var2.tex != null) {
                  var4.setTexture(var2.tex, "Texture", 0);
               }

               var4.setDepthBias(0.0F);
               var4.setAmbient(1.0F);
               var4.setLightingAmount(1.0F);
               var4.setHueShift(0.0F);
               var4.setTint(1.0F, 1.0F, 1.0F);
               var4.setAlpha(1.0F);

               for(var5 = 0; var5 < 5; ++var5) {
                  var4.setLight(var5, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Float.NaN, 0.0F, 0.0F, 0.0F, (IsoMovingObject)null);
               }

               var4.setTransformMatrix(this.IDENTITY.identity(), false);
               var2.Mesh.Draw(var4);
               var4.End();
            }

            ModelCamera.instance.End();
            this.bRendered = true;
         }
      }

      public void postRender() {
      }
   }

   private static final class ModelDrawer extends TextureDraw.GenericDrawer {
      UI3DScene.SceneModel m_model;
      UI3DScene.ModelRenderData m_renderData;
      boolean bRendered;

      public void init(UI3DScene.SceneModel var1, UI3DScene.ModelRenderData var2) {
         this.m_model = var1;
         this.m_renderData = var2;
         this.bRendered = false;
      }

      public void render() {
         UI3DScene.StateData var1 = this.m_model.m_scene.stateDataRender();
         PZGLUtil.pushAndLoadMatrix(5889, var1.m_projection);
         PZGLUtil.pushAndLoadMatrix(5888, var1.m_modelView);
         Model var2 = this.m_model.m_model;
         Shader var3 = var2.Effect;
         if (var3 != null && var2.Mesh != null && var2.Mesh.isReady()) {
            GL11.glPushAttrib(1048575);
            GL11.glPushClientAttrib(-1);
            GL11.glDepthFunc(513);
            GL11.glDepthMask(true);
            GL11.glDepthRange(0.0D, 1.0D);
            GL11.glEnable(2929);
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            var3.Start();
            if (var2.tex != null) {
               var3.setTexture(var2.tex, "Texture", 0);
            }

            var3.setDepthBias(0.0F);
            var3.setAmbient(1.0F);
            var3.setLightingAmount(1.0F);
            var3.setHueShift(0.0F);
            var3.setTint(1.0F, 1.0F, 1.0F);
            var3.setAlpha(1.0F);

            for(int var4 = 0; var4 < 5; ++var4) {
               var3.setLight(var4, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Float.NaN, 0.0F, 0.0F, 0.0F, (IsoMovingObject)null);
            }

            var3.setTransformMatrix(this.m_renderData.m_transform, false);
            var2.Mesh.Draw(var3);
            var3.End();
            GL11.glPopAttrib();
            GL11.glPopClientAttrib();
            Texture.lastTextureID = -1;
            SpriteRenderer.ringBuffer.restoreBoundTextures = true;
            SpriteRenderer.ringBuffer.restoreVBOs = true;
         }

         PZGLUtil.popMatrix(5889);
         PZGLUtil.popMatrix(5888);
         this.bRendered = true;
      }

      public void postRender() {
      }
   }

   private static final class CharacterDrawer extends TextureDraw.GenericDrawer {
      UI3DScene.SceneCharacter m_character;
      UI3DScene.CharacterRenderData m_renderData;
      boolean bRendered;

      public void init(UI3DScene.SceneCharacter var1, UI3DScene.CharacterRenderData var2) {
         this.m_character = var1;
         this.m_renderData = var2;
         this.bRendered = false;
         this.m_character.m_animatedModel.renderMain();
      }

      public void render() {
         if (this.m_character.m_bClearDepthBuffer) {
            GL11.glClear(256);
         }

         boolean var1 = DebugOptions.instance.ModelRenderBones.getValue();
         DebugOptions.instance.ModelRenderBones.setValue(this.m_character.m_bShowBones);
         this.m_character.m_scene.m_CharacterSceneModelCamera.m_renderData = this.m_renderData;
         this.m_character.m_animatedModel.DoRender(this.m_character.m_scene.m_CharacterSceneModelCamera);
         DebugOptions.instance.ModelRenderBones.setValue(var1);
         this.bRendered = true;
         GL11.glDepthMask(true);
      }

      public void postRender() {
         this.m_character.m_animatedModel.postRender(this.bRendered);
      }
   }

   private static final class TranslateGizmoRenderData {
      boolean m_hideX;
      boolean m_hideY;
      boolean m_hideZ;
   }

   public static final class PlaneObjectPool extends ObjectPool {
      int allocated = 0;

      public PlaneObjectPool() {
         super(UI3DScene.Plane::new);
      }

      protected UI3DScene.Plane makeObject() {
         ++this.allocated;
         return (UI3DScene.Plane)super.makeObject();
      }
   }

   public static final class RayObjectPool extends ObjectPool {
      int allocated = 0;

      public RayObjectPool() {
         super(UI3DScene.Ray::new);
      }

      protected UI3DScene.Ray makeObject() {
         ++this.allocated;
         return (UI3DScene.Ray)super.makeObject();
      }
   }

   private static final class SetModelCamera extends TextureDraw.GenericDrawer {
      UI3DScene.SceneModelCamera m_camera;
      UI3DScene.SceneObjectRenderData m_renderData;

      UI3DScene.SetModelCamera init(UI3DScene.SceneModelCamera var1, UI3DScene.SceneObjectRenderData var2) {
         this.m_camera = var1;
         this.m_renderData = var2;
         return this;
      }

      public void render() {
         this.m_camera.m_renderData = this.m_renderData;
         ModelCamera.instance = this.m_camera;
      }

      public void postRender() {
         UI3DScene.s_SetModelCameraPool.release((Object)this);
      }
   }

   private abstract class SceneModelCamera extends ModelCamera {
      UI3DScene.SceneObjectRenderData m_renderData;
   }

   private static class VehicleRenderData extends UI3DScene.SceneObjectRenderData {
      final ArrayList m_models = new ArrayList();
      final ArrayList m_transforms = new ArrayList();
      final UI3DScene.VehicleDrawer m_drawer = new UI3DScene.VehicleDrawer();
      private static final ObjectPool s_pool = new ObjectPool(UI3DScene.VehicleRenderData::new);

      UI3DScene.SceneObjectRenderData initVehicle(UI3DScene.SceneVehicle var1) {
         super.init(var1);
         this.m_models.clear();
         BaseVehicle.Matrix4fObjectPool var2 = (BaseVehicle.Matrix4fObjectPool)BaseVehicle.TL_matrix4f_pool.get();
         var2.release(this.m_transforms);
         this.m_transforms.clear();
         VehicleScript var3 = var1.m_script;
         if (var3.getModel() == null) {
            return null;
         } else {
            this.initVehicleModel(var1);
            float var4 = var3.getModelScale();
            Vector3f var5 = var3.getModel().getOffset();
            Matrix4f var6 = UI3DScene.allocMatrix4f();
            var6.translationRotateScale(var5.x * 1.0F, var5.y, var5.z, 0.0F, 0.0F, 0.0F, 1.0F, var4);
            this.m_transform.mul((Matrix4fc)var6, var6);

            for(int var7 = 0; var7 < var3.getPartCount(); ++var7) {
               VehicleScript.Part var8 = var3.getPart(var7);
               if (var8.wheel != null) {
                  this.initWheelModel(var1, var8, var6);
               }
            }

            UI3DScene.releaseMatrix4f(var6);
            this.m_drawer.init(var1, this);
            return this;
         }
      }

      private void initVehicleModel(UI3DScene.SceneVehicle var1) {
         VehicleScript var2 = var1.m_script;
         float var3 = var2.getModelScale();
         float var4 = 1.0F;
         ModelScript var5 = ScriptManager.instance.getModelScript(var2.getModel().file);
         if (var5 != null && var5.scale != 1.0F) {
            var4 = var5.scale;
         }

         float var6 = 1.0F;
         if (var5 != null) {
            var6 = var5.invertX ? -1.0F : 1.0F;
         }

         var6 *= -1.0F;
         Quaternionf var7 = UI3DScene.allocQuaternionf();
         Matrix4f var8 = UI3DScene.allocMatrix4f();
         Vector3f var9 = var2.getModel().getRotate();
         var7.rotationXYZ(var9.x * 0.017453292F, var9.y * 0.017453292F, var9.z * 0.017453292F);
         Vector3f var10 = var2.getModel().getOffset();
         var8.translationRotateScale(var10.x * 1.0F, var10.y, var10.z, var7.x, var7.y, var7.z, var7.w, var3 * var4 * var6, var3 * var4, var3 * var4);
         if (var1.m_model.Mesh != null && var1.m_model.Mesh.isReady() && var1.m_model.Mesh.m_transform != null) {
            var1.m_model.Mesh.m_transform.transpose();
            var8.mul((Matrix4fc)var1.m_model.Mesh.m_transform);
            var1.m_model.Mesh.m_transform.transpose();
         }

         this.m_transform.mul((Matrix4fc)var8, var8);
         UI3DScene.releaseQuaternionf(var7);
         this.m_models.add(var1.m_model);
         this.m_transforms.add(var8);
      }

      private void initWheelModel(UI3DScene.SceneVehicle var1, VehicleScript.Part var2, Matrix4f var3) {
         VehicleScript var4 = var1.m_script;
         float var5 = var4.getModelScale();
         VehicleScript.Wheel var6 = var4.getWheelById(var2.wheel);
         if (var6 != null && !var2.models.isEmpty()) {
            VehicleScript.Model var7 = (VehicleScript.Model)var2.models.get(0);
            Vector3f var8 = var7.getOffset();
            Vector3f var9 = var7.getRotate();
            Model var10 = ModelManager.instance.getLoadedModel(var7.file);
            if (var10 != null) {
               float var11 = var7.scale;
               float var12 = 1.0F;
               float var13 = 1.0F;
               ModelScript var14 = ScriptManager.instance.getModelScript(var7.file);
               if (var14 != null) {
                  var12 = var14.scale;
                  var13 = var14.invertX ? -1.0F : 1.0F;
               }

               Quaternionf var15 = UI3DScene.allocQuaternionf();
               var15.rotationXYZ(var9.x * 0.017453292F, var9.y * 0.017453292F, var9.z * 0.017453292F);
               Matrix4f var16 = UI3DScene.allocMatrix4f();
               var16.translation(var6.offset.x / var5 * 1.0F, var6.offset.y / var5, var6.offset.z / var5);
               Matrix4f var17 = UI3DScene.allocMatrix4f();
               var17.translationRotateScale(var8.x * 1.0F, var8.y, var8.z, var15.x, var15.y, var15.z, var15.w, var11 * var12 * var13, var11 * var12, var11 * var12);
               var16.mul((Matrix4fc)var17);
               UI3DScene.releaseMatrix4f(var17);
               var3.mul((Matrix4fc)var16, var16);
               if (var10.Mesh != null && var10.Mesh.isReady() && var10.Mesh.m_transform != null) {
                  var10.Mesh.m_transform.transpose();
                  var16.mul((Matrix4fc)var10.Mesh.m_transform);
                  var10.Mesh.m_transform.transpose();
               }

               UI3DScene.releaseQuaternionf(var15);
               this.m_models.add(var10);
               this.m_transforms.add(var16);
            }
         }
      }

      void release() {
         s_pool.release((Object)this);
      }
   }

   private static class ModelRenderData extends UI3DScene.SceneObjectRenderData {
      final UI3DScene.ModelDrawer m_drawer = new UI3DScene.ModelDrawer();
      private static final ObjectPool s_pool = new ObjectPool(UI3DScene.ModelRenderData::new);

      UI3DScene.SceneObjectRenderData initModel(UI3DScene.SceneModel var1) {
         super.init(var1);
         if (var1.m_model.isReady() && var1.m_model.Mesh.m_transform != null) {
            var1.m_model.Mesh.m_transform.transpose();
            this.m_transform.mul((Matrix4fc)var1.m_model.Mesh.m_transform);
            var1.m_model.Mesh.m_transform.transpose();
         }

         if (var1.m_modelScript != null && var1.m_modelScript.scale != 1.0F) {
            this.m_transform.scale(var1.m_modelScript.scale);
         }

         this.m_drawer.init(var1, this);
         return this;
      }

      void release() {
         s_pool.release((Object)this);
      }
   }

   private static class CharacterRenderData extends UI3DScene.SceneObjectRenderData {
      final UI3DScene.CharacterDrawer m_drawer = new UI3DScene.CharacterDrawer();
      private static final ObjectPool s_pool = new ObjectPool(UI3DScene.CharacterRenderData::new);

      UI3DScene.SceneObjectRenderData initCharacter(UI3DScene.SceneCharacter var1) {
         this.m_drawer.init(var1, this);
         super.init(var1);
         return this;
      }

      void release() {
         s_pool.release((Object)this);
      }
   }
}
