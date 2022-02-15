---@class IsoMarkers.IsoMarker : zombie.iso.IsoMarkers.IsoMarker
---@field private ID int
---@field private textures ArrayList|Unknown
---@field private overlayTextures ArrayList|Unknown
---@field private tempObjects ArrayList|Unknown
---@field private square IsoGridSquare
---@field private x float
---@field private y float
---@field private z float
---@field private r float
---@field private g float
---@field private b float
---@field private a float
---@field private doAlpha boolean
---@field private fadeSpeed float
---@field private alpha float
---@field private alphaMax float
---@field private alphaMin float
---@field private alphaInc boolean
---@field private active boolean
---@field private isRemoved boolean
IsoMarkers_IsoMarker = {}

---@public
---@param arg0 float
---@return void
function IsoMarkers_IsoMarker:setB(arg0) end

---@public
---@param arg0 boolean
---@return void
function IsoMarkers_IsoMarker:setDoAlpha(arg0) end

---@public
---@return float
function IsoMarkers_IsoMarker:getZ() end

---@public
---@return boolean
function IsoMarkers_IsoMarker:hasTempSquareObject() end

---@public
---@return float
function IsoMarkers_IsoMarker:getB() end

---@public
---@return int
function IsoMarkers_IsoMarker:getID() end

---@public
---@param arg0 boolean
---@return void
function IsoMarkers_IsoMarker:setActive(arg0) end

---@public
---@param arg0 float
---@return void
function IsoMarkers_IsoMarker:setAlphaMin(arg0) end

---@public
---@param arg0 float
---@return void
function IsoMarkers_IsoMarker:setG(arg0) end

---@public
---@return IsoGridSquare
function IsoMarkers_IsoMarker:getSquare() end

---@public
---@param arg0 KahluaTable
---@param arg1 KahluaTable
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 IsoGridSquare
---@return void
---@overload fun(arg0:String, arg1:int, arg2:int, arg3:int, arg4:IsoGridSquare, arg5:boolean)
---@overload fun(arg0:KahluaTable, arg1:KahluaTable, arg2:int, arg3:int, arg4:int, arg5:IsoGridSquare, arg6:boolean)
function IsoMarkers_IsoMarker:init(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 IsoGridSquare
---@param arg5 boolean
---@return void
function IsoMarkers_IsoMarker:init(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 KahluaTable
---@param arg1 KahluaTable
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 IsoGridSquare
---@param arg6 boolean
---@return void
function IsoMarkers_IsoMarker:init(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 IsoObject
---@return void
function IsoMarkers_IsoMarker:addTempSquareObject(arg0) end

---@public
---@param arg0 float
---@return void
function IsoMarkers_IsoMarker:setA(arg0) end

---@public
---@param arg0 float
---@return void
function IsoMarkers_IsoMarker:setR(arg0) end

---@public
---@return void
function IsoMarkers_IsoMarker:removeTempSquareObjects() end

---@public
---@return void
function IsoMarkers_IsoMarker:remove() end

---@public
---@return boolean
function IsoMarkers_IsoMarker:isRemoved() end

---@public
---@return float
function IsoMarkers_IsoMarker:getA() end

---@public
---@return float
function IsoMarkers_IsoMarker:getR() end

---@public
---@return boolean
function IsoMarkers_IsoMarker:isDoAlpha() end

---@public
---@return float
function IsoMarkers_IsoMarker:getAlphaMin() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function IsoMarkers_IsoMarker:setPos(arg0, arg1, arg2) end

---@public
---@return boolean
function IsoMarkers_IsoMarker:isActive() end

---@public
---@param arg0 float
---@return void
function IsoMarkers_IsoMarker:setAlphaMax(arg0) end

---@public
---@return float
function IsoMarkers_IsoMarker:getFadeSpeed() end

---@public
---@return float
function IsoMarkers_IsoMarker:getAlphaMax() end

---@public
---@param arg0 float
---@return void
function IsoMarkers_IsoMarker:setFadeSpeed(arg0) end

---@public
---@return float
function IsoMarkers_IsoMarker:getAlpha() end

---@public
---@return float
function IsoMarkers_IsoMarker:getY() end

---@public
---@param arg0 float
---@return void
function IsoMarkers_IsoMarker:setAlpha(arg0) end

---@public
---@return float
function IsoMarkers_IsoMarker:getX() end

---@public
---@return float
function IsoMarkers_IsoMarker:getG() end

---@public
---@param arg0 IsoGridSquare
---@return void
function IsoMarkers_IsoMarker:setSquare(arg0) end
