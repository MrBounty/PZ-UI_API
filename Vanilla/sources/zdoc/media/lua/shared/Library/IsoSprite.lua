---@class IsoSprite : zombie.iso.sprite.IsoSprite
---@field public maxCount int
---@field public alphaStep float
---@field public globalOffsetX float
---@field public globalOffsetY float
---@field private info ColorInfo
---@field private AnimNameSet HashMap|Unknown|Unknown
---@field public firerequirement int
---@field public burntTile String
---@field public forceAmbient boolean
---@field public solidfloor boolean
---@field public canBeRemoved boolean
---@field public attachedFloor boolean
---@field public cutW boolean
---@field public cutN boolean
---@field public solid boolean
---@field public solidTrans boolean
---@field public invisible boolean
---@field public alwaysDraw boolean
---@field public forceRender boolean
---@field public moveWithWind boolean
---@field public isBush boolean
---@field public RL_DEFAULT byte
---@field public RL_FLOOR byte
---@field public renderLayer byte
---@field public windType int
---@field public Animate boolean
---@field public CurrentAnim IsoAnim
---@field public DeleteWhenFinished boolean
---@field public Loop boolean
---@field public soffX short
---@field public soffY short
---@field public Properties PropertyContainer
---@field public TintMod ColorInfo
---@field public AnimMap HashMap|String|IsoAnim
---@field public AnimStack ArrayList|IsoAnim
---@field public name String
---@field public tileSheetIndex int
---@field public ID int
---@field public def IsoSpriteInstance
---@field public modelSlot ModelManager.ModelSlot
---@field parentManager IsoSpriteManager
---@field private type IsoObjectType
---@field private parentObjectName String
---@field private spriteGrid IsoSpriteGrid
---@field public treatAsWallOrder boolean
---@field private hideForWaterRender boolean
IsoSprite = {}

---@private
---@return void
function IsoSprite:initSpriteInstance() end

---@public
---@param anim IsoAnim
---@return void
---@overload fun(name:String)
function IsoSprite:PlayAnim(anim) end

---@public
---@param name String
---@return void
function IsoSprite:PlayAnim(name) end

---@public
---@param arg0 IsoObject
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 IsoDirections
---@param arg5 float
---@param arg6 float
---@param arg7 ColorInfo
---@param arg8 boolean
---@return void
---@overload fun(arg0:IsoSpriteInstance, arg1:IsoObject, arg2:float, arg3:float, arg4:float, arg5:IsoDirections, arg6:float, arg7:float, arg8:ColorInfo, arg9:boolean)
---@overload fun(arg0:IsoObject, arg1:float, arg2:float, arg3:float, arg4:IsoDirections, arg5:float, arg6:float, arg7:ColorInfo, arg8:boolean, arg9:Consumer|Unknown)
---@overload fun(arg0:IsoSpriteInstance, arg1:IsoObject, arg2:float, arg3:float, arg4:float, arg5:IsoDirections, arg6:float, arg7:float, arg8:ColorInfo, arg9:boolean, arg10:Consumer|Unknown)
function IsoSprite:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param arg0 IsoSpriteInstance
---@param arg1 IsoObject
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 IsoDirections
---@param arg6 float
---@param arg7 float
---@param arg8 ColorInfo
---@param arg9 boolean
---@return void
function IsoSprite:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@param arg0 IsoObject
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 IsoDirections
---@param arg5 float
---@param arg6 float
---@param arg7 ColorInfo
---@param arg8 boolean
---@param arg9 Consumer|Unknown
---@return void
function IsoSprite:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@param arg0 IsoSpriteInstance
---@param arg1 IsoObject
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 IsoDirections
---@param arg6 float
---@param arg7 float
---@param arg8 ColorInfo
---@param arg9 boolean
---@param arg10 Consumer|Unknown
---@return void
function IsoSprite:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@param ObjectName String
---@param AnimName String
---@param nFrames int
---@return void
function IsoSprite:LoadFramesNoDirPage(ObjectName, AnimName, nFrames) end

---@public
---@param key String
---@return void
function IsoSprite:CacheAnims(key) end

---@private
---@param arg0 IsoSpriteInstance
---@param arg1 IsoObject
---@param arg2 IsoDirections
---@param arg3 int
---@param arg4 float
---@param arg5 float
---@param arg6 Consumer|Unknown
---@return void
function IsoSprite:performRenderFrame(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 int
---@param arg1 IsoDirections
---@return Texture
function IsoSprite:getTextureForFrame(arg0, arg1) end

---@public
---@param ObjectName String
---@param AnimName String
---@param nFrames int
---@return void
function IsoSprite:LoadFramesPcx(ObjectName, AnimName, nFrames) end

---@public
---@param ObjectName String
---@return void
function IsoSprite:ReplaceCurrentAnimFrames(ObjectName) end

---@public
---@param dir IsoDirections
---@param x int
---@param y int
---@return boolean
---@overload fun(dir:IsoDirections, x:int, y:int, flip:boolean)
function IsoSprite:isMaskClicked(dir, x, y) end

---@public
---@param dir IsoDirections
---@param x int
---@param y int
---@param flip boolean
---@return boolean
function IsoSprite:isMaskClicked(dir, x, y, flip) end

---@public
---@param name String
---@return void
function IsoSprite:PlayAnimUnlooped(name) end

---@public
---@param val String
---@return void
function IsoSprite:setParentObjectName(val) end

---@public
---@return IsoSpriteInstance
function IsoSprite:newInstance() end

---@public
---@param ObjectName String
---@param AnimName String
---@param nFrames int
---@return void
function IsoSprite:LoadFrames(ObjectName, AnimName, nFrames) end

---@public
---@param ObjectName String
---@param AnimName String
---@param nFrames int
---@return void
function IsoSprite:LoadFramesNoDirPageDirect(ObjectName, AnimName, nFrames) end

---@public
---@param x float
---@param y float
---@param z float
---@param info2 ColorInfo
---@return void
function IsoSprite:renderBloodSplat(x, y, z, info2) end

---@public
---@param ObjectName String
---@return Texture
function IsoSprite:LoadFrameExplicit(ObjectName) end

---@public
---@return void
---@overload fun(def:IsoSpriteInstance)
function IsoSprite:update() end

---@public
---@param def IsoSpriteInstance
---@return void
function IsoSprite:update(def) end

---@public
---@param _string String
---@return boolean
function IsoSprite:HasCache(_string) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@return IsoSprite
function IsoSprite:setFromCache(arg0, arg1, arg2) end

---@public
---@param arg0 IsoSpriteInstance
---@param arg1 IsoObject
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 IsoDirections
---@param arg6 float
---@param arg7 float
---@param arg8 ColorInfo
---@param arg9 boolean
---@param arg10 Consumer|Unknown
---@return void
function IsoSprite:renderCurrentAnim(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@param _string String
---@return void
function IsoSprite:setName(_string) end

---@public
---@param manager IsoSpriteManager
---@param id int
---@return IsoSprite
---@overload fun(manager:IsoSpriteManager, spr:IsoSprite, offset:int)
---@overload fun(manager:IsoSpriteManager, name:String, offset:int)
function IsoSprite:getSprite(manager, id) end

---@public
---@param manager IsoSpriteManager
---@param spr IsoSprite
---@param offset int
---@return IsoSprite
function IsoSprite:getSprite(manager, spr, offset) end

---@public
---@param manager IsoSpriteManager
---@param name String
---@param offset int
---@return IsoSprite
function IsoSprite:getSprite(manager, name, offset) end

---@public
---@return boolean
function IsoSprite:hasActiveModel() end

---@public
---@param dir IsoDirections
---@param x int
---@param y int
---@param flip boolean
---@return float
function IsoSprite:getMaskClickedY(dir, x, y, flip) end

---@public
---@param ObjectName String
---@param AnimName String
---@param AltName String
---@param nFrames int
---@return void
function IsoSprite:LoadFramesReverseAltName(ObjectName, AnimName, AltName, nFrames) end

---@private
---@param arg0 IsoSpriteInstance
---@param arg1 IsoObject
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 IsoDirections
---@param arg6 float
---@param arg7 float
---@param arg8 boolean
---@param arg9 int
---@param arg10 JVector2
---@return void
function IsoSprite:prepareToRenderSprite(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@return void
function IsoSprite:Dispose() end

---@public
---@param arg0 IsoSpriteInstance
---@param arg1 IsoObject
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 ColorInfo
---@param arg8 boolean
---@return void
function IsoSprite:renderVehicle(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@private
---@return IsoSpriteInstance
function IsoSprite:getSpriteInstance() end

---@public
---@return IsoSpriteGrid
function IsoSprite:getSpriteGrid() end

---@public
---@param NObjectName String
---@param SObjectName String
---@param EObjectName String
---@param WObjectName String
---@return void
function IsoSprite:LoadFramesPageSimple(NObjectName, SObjectName, EObjectName, WObjectName) end

---@public
---@param x int
---@param y int
---@param z int
---@return void
function IsoSprite:RenderGhostTileRed(x, y, z) end

---@public
---@return String
function IsoSprite:getParentObjectName() end

---@public
---@param ObjectName String
---@return void
function IsoSprite:LoadFramesNoDirPageSimple(ObjectName) end

---throws java.io.IOException
---@public
---@param output DataOutputStream
---@return void
function IsoSprite:save(output) end

---@public
---@param manager IsoSpriteManager
---@return IsoSprite
function IsoSprite:CreateSprite(manager) end

---@public
---@param arg0 IsoSpriteInstance
---@param arg1 IsoObject
---@param arg2 IsoDirections
---@return void
function IsoSprite:renderObjectPicker(arg0, arg1, arg2) end

---@public
---@param _string String
---@return void
function IsoSprite:LoadCache(_string) end

---@public
---@return IsoObjectType
function IsoSprite:getType() end

---@public
---@param ntype IsoObjectType
---@return void
function IsoSprite:setType(ntype) end

---@public
---@return String
function IsoSprite:getName() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 Texture
---@param arg3 float
---@param arg4 float
---@return void
function IsoSprite:renderSpriteOutline(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return void
function IsoSprite:setHideForWaterRender() end

---@public
---@param arg0 IsoSpriteGrid
---@return void
function IsoSprite:setSpriteGrid(arg0) end

---@public
---@return void
function IsoSprite:renderActiveModel() end

---@public
---@param x int
---@param y int
---@param z int
---@param r float
---@param g float
---@param b float
---@param a float
---@return void
---@overload fun(arg0:int, arg1:int, arg2:int, arg3:float, arg4:float, arg5:float, arg6:float, arg7:float, arg8:float)
function IsoSprite:RenderGhostTileColor(x, y, z, r, g, b, a) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@return void
function IsoSprite:RenderGhostTileColor(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@return boolean
function IsoSprite:isMoveWithWind() end

---@public
---@param info ColorInfo
---@return void
function IsoSprite:setTintMod(info) end

---@public
---@return void
function IsoSprite:DisposeAll() end

---@public
---@return int
---@overload fun(arg0:String)
function IsoSprite:getSheetGridIdFromName() end

---@public
---@param arg0 String
---@return int
function IsoSprite:getSheetGridIdFromName(arg0) end

---@private
---@param arg0 IsoSpriteInstance
---@return float
function IsoSprite:getCurrentSpriteFrame(arg0) end

---@public
---@param NewTintMod ColorInfo
---@return void
function IsoSprite:ChangeTintMod(NewTintMod) end

---@public
---@param arg0 boolean
---@return void
function IsoSprite:setAnimate(arg0) end

---@public
---@param manager IsoSpriteManager
---@param id int
---@param spr IsoSprite
---@return void
function IsoSprite:setSpriteID(manager, id, spr) end

---@public
---@param arg0 IsoDirections
---@return Texture
function IsoSprite:getTextureForCurrentFrame(arg0) end

---@public
---@return ColorInfo
function IsoSprite:getTintMod() end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 int
---@return IsoSprite
function IsoSprite:CreateSpriteUsingCache(arg0, arg1, arg2) end

---@public
---@param sprite IsoSprite
---@return void
function IsoSprite:AddProperties(sprite) end

---@public
---@return PropertyContainer @the Properties
function IsoSprite:getProperties() end

---@public
---@param x int
---@param y int
---@param z int
---@return void
function IsoSprite:RenderGhostTile(x, y, z) end

---throws java.io.IOException
---@public
---@param input DataInputStream
---@return void
function IsoSprite:load(input) end

---@public
---@return int
function IsoSprite:getID() end
