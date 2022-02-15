---@class ContainerOverlays : zombie.iso.ContainerOverlays
---@field public instance ContainerOverlays
---@field private tempEntries ArrayList|Unknown
---@field private overlayMap THashMap|Unknown|Unknown
ContainerOverlays = {}

---@public
---@return void
function ContainerOverlays:Reset() end

---@public
---@param arg0 KahluaTableImpl
---@return void
function ContainerOverlays:addOverlays(arg0) end

---@private
---@param arg0 KahluaTableImpl
---@return void
function ContainerOverlays:parseContainerOverlayMapV1(arg0) end

---@public
---@param arg0 IsoObject
---@return boolean
function ContainerOverlays:hasOverlays(arg0) end

---@public
---@param arg0 IsoObject
---@return void
function ContainerOverlays:updateContainerOverlaySprite(arg0) end

---@private
---@param arg0 KahluaTableImpl
---@return void
function ContainerOverlays:parseContainerOverlayMapV0(arg0) end
