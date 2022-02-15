---@class NonPvpZone : zombie.iso.areas.NonPvpZone
---@field private x int
---@field private y int
---@field private x2 int
---@field private y2 int
---@field private size int
---@field private title String
---@field public nonPvpZoneList ArrayList|Unknown
NonPvpZone = {}

---@public
---@param arg0 String
---@return void
function NonPvpZone:setTitle(arg0) end

---@public
---@param arg0 ByteBuffer
---@return void
function NonPvpZone:save(arg0) end

---@public
---@return int
function NonPvpZone:getX() end

---@public
---@param arg0 int
---@return void
function NonPvpZone:setX2(arg0) end

---@public
---@return int
function NonPvpZone:getSize() end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return NonPvpZone
function NonPvpZone:addNonPvpZone(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 boolean
---@return void
function NonPvpZone:syncNonPvpZone(arg0) end

---@public
---@param arg0 int
---@return void
function NonPvpZone:setY(arg0) end

---@public
---@return ArrayList|Unknown
function NonPvpZone:getAllZones() end

---@public
---@return String
function NonPvpZone:getTitle() end

---@public
---@param arg0 int
---@return void
function NonPvpZone:setSize(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function NonPvpZone:removeNonPvpZone(arg0, arg1) end

---@public
---@return int
function NonPvpZone:getY2() end

---@public
---@return int
function NonPvpZone:getY() end

---@public
---@param arg0 int
---@param arg1 int
---@return NonPvpZone
function NonPvpZone:getNonPvpZone(arg0, arg1) end

---@public
---@param arg0 int
---@return void
function NonPvpZone:setY2(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function NonPvpZone:load(arg0, arg1) end

---@public
---@param arg0 String
---@return NonPvpZone
function NonPvpZone:getZoneByTitle(arg0) end

---@public
---@param arg0 int
---@return void
function NonPvpZone:setX(arg0) end

---@public
---@return int
function NonPvpZone:getX2() end
