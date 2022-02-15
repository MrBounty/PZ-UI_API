---@class UI3DScene : zombie.vehicles.UI3DScene
---@field private m_objects ArrayList|Unknown
---@field private m_view UI3DScene.View
---@field private m_transformMode UI3DScene.TransformMode
---@field private m_view_x int
---@field private m_view_y int
---@field private m_viewRotation Vector3f
---@field private m_zoom int
---@field private m_zoomMax int
---@field private m_gridDivisions int
---@field private m_gridPlane UI3DScene.GridPlane
---@field private m_projection Matrix4f
---@field private m_modelView Matrix4f
---@field private VIEW_CHANGE_TIME long
---@field private m_viewChangeTime long
---@field private m_modelViewChange Quaternionf
---@field private m_bDrawGrid boolean
---@field private m_bDrawGridAxes boolean
---@field private m_CharacterSceneModelCamera UI3DScene.CharacterSceneModelCamera
---@field private m_VehicleSceneModelCamera UI3DScene.VehicleSceneModelCamera
---@field private s_SetModelCameraPool ObjectPool|Unknown
---@field private m_stateData UI3DScene.StateData[]
---@field private m_gizmo UI3DScene.Gizmo
---@field private m_rotateGizmo UI3DScene.RotateGizmo
---@field private m_scaleGizmo UI3DScene.ScaleGizmo
---@field private m_translateGizmo UI3DScene.TranslateGizmo
---@field private m_gizmoPos Vector3f
---@field private m_gizmoRotate Vector3f
---@field private m_gizmoParent UI3DScene.SceneObject
---@field private m_gizmoOrigin UI3DScene.SceneObject
---@field private m_gizmoChild UI3DScene.SceneObject
---@field private m_originAttachment UI3DScene.OriginAttachment
---@field private m_originBone UI3DScene.OriginBone
---@field private m_originGizmo UI3DScene.OriginGizmo
---@field private m_gizmoScale float
---@field private m_selectedAttachment String
---@field private m_axes ArrayList|Unknown
---@field private m_highlightBone UI3DScene.OriginBone
---@field private s_posRotPool ObjectPool|Unknown
---@field private m_aabb ArrayList|Unknown
---@field private s_aabbPool ObjectPool|Unknown
---@field private m_box3D ArrayList|Unknown
---@field private s_box3DPool ObjectPool|Unknown
---@field tempVector3f Vector3f
---@field tempVector4f Vector4f
---@field m_viewport int[]
---@field private GRID_DARK float
---@field private GRID_LIGHT float
---@field private GRID_ALPHA float
---@field private HALF_GRID int
---@field private vboLines VBOLines
---@field private TL_Ray_pool ThreadLocal|Unknown
---@field private TL_Plane_pool ThreadLocal|Unknown
---@field SMALL_NUM float
UI3DScene = {}

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@return Object
function UI3DScene:fromLua3(arg0, arg1, arg2, arg3) end

---@private
---@return int
function UI3DScene:screenWidth() end

---@param arg0 UI3DScene.PositionRotation
---@return void
---@overload fun(arg0:Vector3f, arg1:Vector3f)
function UI3DScene:renderAxis(arg0) end

---@param arg0 Vector3f
---@param arg1 Vector3f
---@return void
function UI3DScene:renderAxis(arg0, arg1) end

---@private
---@return JVector2
function UI3DScene:allocVector2() end

---@param arg0 UI3DScene.Plane
---@param arg1 UI3DScene.Ray
---@param arg2 Vector3f
---@return int
function UI3DScene:intersect_ray_plane(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return Object
function UI3DScene:fromLua0(arg0) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@param arg9 float
---@param arg10 float
---@param arg11 float
---@return void
function UI3DScene:renderBox3D(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 Vector3f
---@return Vector3f
---@overload fun(arg0:Matrix4f, arg1:float, arg2:float, arg3:float, arg4:Vector3f)
function UI3DScene:uiToScene(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 Matrix4f
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 Vector3f
---@return Vector3f
function UI3DScene:uiToScene(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@param arg4 Object
---@return Object
function UI3DScene:fromLua4(arg0, arg1, arg2, arg3, arg4) end

---@param arg0 Vector3f
---@param arg1 Vector3f
---@param arg2 Vector3f
---@return Vector3f
function UI3DScene:project(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@param arg4 Object
---@param arg5 Object
---@param arg6 Object
---@param arg7 Object
---@param arg8 Object
---@param arg9 Object
---@return Object
function UI3DScene:fromLua9(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@private
---@return int
function UI3DScene:screenHeight() end

---@param arg0 String
---@param arg1 boolean
---@return UI3DScene.SceneObject
---@overload fun(arg0:String, arg1:Class|Unknown, arg2:boolean)
function UI3DScene:getSceneObjectById(arg0, arg1) end

---@param arg0 String
---@param arg1 Class|Unknown
---@param arg2 boolean
---@return Object
function UI3DScene:getSceneObjectById(arg0, arg1, arg2) end

---@private
---@param arg0 Quaternionf
---@return void
function UI3DScene:releaseQuaternionf(arg0) end

---@private
---@param arg0 JVector2
---@return void
function UI3DScene:releaseVector2(arg0) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@return void
function UI3DScene:renderAABB(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@param arg4 Object
---@param arg5 Object
---@param arg6 Object
---@return Object
function UI3DScene:fromLua6(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@return UI3DScene.StateData
function UI3DScene:stateDataMain() end

---@private
---@param arg0 Vector3f
---@return void
function UI3DScene:releaseVector3f(arg0) end

---@param arg0 Vector3f
---@param arg1 UI3DScene.Ray
---@return float
function UI3DScene:distance_between_point_ray(arg0, arg1) end

---@param arg0 UI3DScene.Ray
---@param arg1 UI3DScene.Circle
---@param arg2 Vector3f
---@return float
function UI3DScene:closest_distance_line_circle(arg0, arg1, arg2) end

---@private
---@return UI3DScene.Ray
function UI3DScene:allocRay() end

---@param arg0 float
---@param arg1 float
---@param arg2 UI3DScene.Ray
---@return UI3DScene.Ray
---@overload fun(arg0:float, arg1:float, arg2:Matrix4f, arg3:Matrix4f, arg4:UI3DScene.Ray)
function UI3DScene:getCameraRay(arg0, arg1, arg2) end

---@param arg0 float
---@param arg1 float
---@param arg2 Matrix4f
---@param arg3 Matrix4f
---@param arg4 UI3DScene.Ray
---@return UI3DScene.Ray
function UI3DScene:getCameraRay(arg0, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 UI3DScene.Plane
---@return void
function UI3DScene:releasePlane(arg0) end

---@private
---@return Quaternionf
function UI3DScene:allocQuaternionf() end

---@private
---@return float
function UI3DScene:gridMult() end

---@private
---@return Vector3f
function UI3DScene:allocVector3f() end

---@private
---@param arg0 int
---@return void
function UI3DScene:renderGridXY(arg0) end

---@private
---@return UI3DScene.StateData
function UI3DScene:stateDataRender() end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function UI3DScene:uiToSceneY(arg0, arg1) end

---@private
---@return UI3DScene.Plane
function UI3DScene:allocPlane() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function UI3DScene:sceneToUIX(arg0, arg1, arg2) end

---@private
---@param arg0 Matrix4f
---@return void
function UI3DScene:releaseMatrix4f(arg0) end

---@private
---@return void
function UI3DScene:renderGrid() end

---@private
---@param arg0 int
---@return void
function UI3DScene:renderGridXZ(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function UI3DScene:uiToSceneX(arg0, arg1) end

---@public
---@return void
function UI3DScene:render() end

---@param arg0 Vector3f
---@param arg1 Vector3f
---@param arg2 Vector3f
---@return Vector3f
function UI3DScene:reject(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@return Object
function UI3DScene:fromLua2(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function UI3DScene:sceneToUIY(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@return void
function UI3DScene:renderGridYZ(arg0) end

---@private
---@param arg0 UI3DScene.Ray
---@return void
function UI3DScene:releaseRay(arg0) end

---@param arg0 UI3DScene.Ray
---@param arg1 UI3DScene.Ray
---@return float
function UI3DScene:closest_distance_between_lines(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 Object
---@return Object
function UI3DScene:fromLua1(arg0, arg1) end

---@private
---@return float
function UI3DScene:zoomMult() end

---@private
---@param arg0 Matrix4f
---@param arg1 Matrix4f
---@return void
function UI3DScene:calcMatrices(arg0, arg1) end

---@private
---@return Matrix4f
function UI3DScene:allocMatrix4f() end
