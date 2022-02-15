---@class IsoWaterGeometry : zombie.iso.IsoWaterGeometry
---@field private tempVector2f Vector2f
---@field hasWater boolean
---@field bShore boolean
---@field x float[]
---@field y float[]
---@field depth float[]
---@field flow float[]
---@field speed float[]
---@field IsExternal float
---@field square IsoGridSquare
---@field m_adjacentChunkLoadedCounter int
---@field public pool ObjectPool|Unknown
IsoWaterGeometry = {}

---@public
---@return boolean
function IsoWaterGeometry:isShore() end

---@private
---@param arg0 IsoGridSquare
---@return void
function IsoWaterGeometry:hideWaterObjects(arg0) end

---@public
---@param arg0 IsoGridSquare
---@return IsoWaterGeometry
function IsoWaterGeometry:init(arg0) end

---@public
---@return float
function IsoWaterGeometry:getFlow() end
