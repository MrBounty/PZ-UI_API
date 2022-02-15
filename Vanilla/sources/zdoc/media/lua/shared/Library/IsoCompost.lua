---@class IsoCompost : zombie.iso.objects.IsoCompost
---@field private compost float
---@field private LastUpdated float
IsoCompost = {}

---@public
---@return void
function IsoCompost:update() end

---@public
---@param arg0 float
---@return void
function IsoCompost:setCompost(arg0) end

---@public
---@return void
function IsoCompost:remove() end

---@public
---@return void
function IsoCompost:syncCompost() end

---@public
---@return void
function IsoCompost:updateSprite() end

---@public
---@return String
function IsoCompost:getObjectName() end

---@public
---@return void
function IsoCompost:addToWorld() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoCompost:load(arg0, arg1, arg2) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoCompost:save(arg0, arg1) end

---@public
---@return float
function IsoCompost:getCompost() end
