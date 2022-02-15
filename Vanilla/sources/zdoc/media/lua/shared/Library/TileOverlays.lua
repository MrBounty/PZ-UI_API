---@class TileOverlays : zombie.iso.TileOverlays
---@field public instance TileOverlays
---@field private overlayMap THashMap|Unknown|Unknown
---@field private tempEntries ArrayList|Unknown
TileOverlays = {}

---@private
---@param arg0 IsoObject
---@return void
function TileOverlays:removeTableTopOverlays(arg0) end

---@private
---@param arg0 ArrayList|Unknown
---@param arg1 String
---@return void
function TileOverlays:tryRemoveAttachedSprite(arg0, arg1) end

---@private
---@param arg0 IsoObject
---@return boolean
function TileOverlays:hasObjectOnTop(arg0) end

---@public
---@param arg0 IsoObject
---@return boolean
function TileOverlays:hasOverlays(arg0) end

---@public
---@return void
function TileOverlays:Reset() end

---@public
---@param arg0 KahluaTableImpl
---@return void
function TileOverlays:addOverlays(arg0) end

---@public
---@param arg0 IsoObject
---@return void
function TileOverlays:updateTileOverlaySprite(arg0) end

---@public
---@param arg0 IsoGridSquare
---@return void
function TileOverlays:fixTableTopOverlays(arg0) end
