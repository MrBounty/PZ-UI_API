---@class IsoMarkers : zombie.iso.IsoMarkers
---@field public instance IsoMarkers
---@field private NextIsoMarkerID int
---@field private markers List|Unknown
IsoMarkers = {}

---@public
---@return void
function IsoMarkers:update() end

---@public
---@return void
function IsoMarkers:reset() end

---@public
---@return void
function IsoMarkers:init() end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 boolean
---@param arg6 boolean
---@return IsoMarkers.IsoMarker
---@overload fun(arg0:KahluaTable, arg1:KahluaTable, arg2:IsoGridSquare, arg3:float, arg4:float, arg5:float, arg6:boolean, arg7:boolean)
---@overload fun(arg0:KahluaTable, arg1:KahluaTable, arg2:IsoGridSquare, arg3:float, arg4:float, arg5:float, arg6:boolean, arg7:boolean, arg8:float, arg9:float, arg10:float)
function IsoMarkers:addIsoMarker(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 KahluaTable
---@param arg1 KahluaTable
---@param arg2 IsoGridSquare
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@param arg7 boolean
---@return IsoMarkers.IsoMarker
function IsoMarkers:addIsoMarker(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 KahluaTable
---@param arg1 KahluaTable
---@param arg2 IsoGridSquare
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@param arg7 boolean
---@param arg8 float
---@param arg9 float
---@param arg10 float
---@return IsoMarkers.IsoMarker
function IsoMarkers:addIsoMarker(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@return void
function IsoMarkers:render() end

---@public
---@param arg0 IsoCell.PerPlayerRender
---@param arg1 int
---@param arg2 int
---@return void
function IsoMarkers:renderIsoMarkersDeferred(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@return boolean
---@overload fun(arg0:IsoMarkers.IsoMarker)
function IsoMarkers:removeIsoMarker(arg0) end

---@public
---@param arg0 IsoMarkers.IsoMarker
---@return boolean
function IsoMarkers:removeIsoMarker(arg0) end

---@private
---@return void
function IsoMarkers:updateIsoMarkers() end

---@public
---@param arg0 int
---@return IsoMarkers.IsoMarker
function IsoMarkers:getIsoMarker(arg0) end

---@public
---@param arg0 IsoCell.PerPlayerRender
---@param arg1 int
---@param arg2 int
---@return void
function IsoMarkers:renderIsoMarkers(arg0, arg1, arg2) end
