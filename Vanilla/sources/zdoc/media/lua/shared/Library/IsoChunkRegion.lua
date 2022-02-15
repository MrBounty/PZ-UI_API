---@class IsoChunkRegion : zombie.iso.areas.isoregion.regions.IsoChunkRegion
---@field private manager IsoRegionManager
---@field private isInPool boolean
---@field private color Color
---@field private ID int
---@field private zLayer byte
---@field private squareSize byte
---@field private roofCnt byte
---@field private chunkBorderSquaresCnt byte
---@field private enclosed boolean[]
---@field private enclosedCache boolean
---@field private connectedNeighbors ArrayList|Unknown
---@field private allNeighbors ArrayList|Unknown
---@field private isDirtyEnclosed boolean
---@field private isoWorldRegion IsoWorldRegion
IsoChunkRegion = {}

---@public
---@param arg0 IsoChunkRegion
---@return boolean
function IsoChunkRegion:containsConnectedNeighbor(arg0) end

---@public
---@return boolean
function IsoChunkRegion:getIsEnclosed() end

---@protected
---@param arg0 IsoChunkRegion
---@return void
function IsoChunkRegion:removeConnectedNeighbor(arg0) end

---@public
---@return int
function IsoChunkRegion:getChunkBorderSquaresCnt() end

---@public
---@return void
function IsoChunkRegion:resetRoofCnt() end

---@public
---@return IsoChunkRegion
function IsoChunkRegion:getConnectedNeighborWithLargestIsoWorldRegion() end

---@public
---@return int
function IsoChunkRegion:getNeighborCount() end

---@protected
---@return void
function IsoChunkRegion:resetChunkBorderSquaresCnt() end

---@public
---@param arg0 byte
---@param arg1 boolean
---@return void
function IsoChunkRegion:setEnclosed(arg0, arg1) end

---@public
---@return int
function IsoChunkRegion:getID() end

---@protected
---@return boolean
function IsoChunkRegion:isInPool() end

---@public
---@return ArrayList|Unknown
function IsoChunkRegion:getConnectedNeighbors() end

---@protected
---@return IsoChunkRegion
function IsoChunkRegion:getFirstNeighborWithIsoWorldRegion() end

---@public
---@return Color
function IsoChunkRegion:getColor() end

---@protected
---@return void
function IsoChunkRegion:removeChunkBorderSquaresCnt() end

---@public
---@return int
function IsoChunkRegion:getRoofCnt() end

---@protected
---@return IsoChunkRegion
function IsoChunkRegion:reset() end

---@public
---@return IsoWorldRegion
function IsoChunkRegion:unlinkFromIsoWorldRegion() end

---@private
---@return void
function IsoChunkRegion:resetEnclosed() end

---@public
---@param arg0 IsoChunkRegion
---@return void
function IsoChunkRegion:addNeighbor(arg0) end

---@public
---@param arg0 IsoWorldRegion
---@return void
function IsoChunkRegion:setIsoWorldRegion(arg0) end

---@public
---@return void
function IsoChunkRegion:addSquareCount() end

---@public
---@return int
function IsoChunkRegion:getSquareSize() end

---@public
---@return ArrayList|Unknown
function IsoChunkRegion:getDebugConnectedNeighborCopy() end

---@public
---@param arg0 IsoChunkRegion
---@return void
function IsoChunkRegion:addConnectedNeighbor(arg0) end

---@protected
---@param arg0 IsoChunkRegion
---@return void
function IsoChunkRegion:removeNeighbor(arg0) end

---@protected
---@return void
function IsoChunkRegion:setDirtyEnclosed() end

---@public
---@return void
function IsoChunkRegion:addRoof() end

---@protected
---@return ArrayList|Unknown
function IsoChunkRegion:getAllNeighbors() end

---@protected
---@param arg0 int
---@param arg1 int
---@return void
function IsoChunkRegion:init(arg0, arg1) end

---@public
---@return int
function IsoChunkRegion:getzLayer() end

---@public
---@param arg0 int
---@return boolean
function IsoChunkRegion:containsConnectedNeighborID(arg0) end

---@public
---@return IsoWorldRegion
function IsoChunkRegion:getIsoWorldRegion() end

---@public
---@return void
function IsoChunkRegion:addChunkBorderSquaresCnt() end

---@protected
---@return void
function IsoChunkRegion:unlinkNeighbors() end
