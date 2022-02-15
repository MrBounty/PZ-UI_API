---@class BrokenFences : zombie.iso.BrokenFences
---@field private instance BrokenFences
---@field private s_unbrokenMap THashMap|Unknown|Unknown
---@field private s_brokenLeftMap THashMap|Unknown|Unknown
---@field private s_brokenRightMap THashMap|Unknown|Unknown
---@field private s_allMap THashMap|Unknown|Unknown
BrokenFences = {}

---@public
---@param arg0 KahluaTableImpl
---@return void
function BrokenFences:addDebrisTiles(arg0) end

---@private
---@param arg0 IsoObject
---@param arg1 IsoGridSquare
---@return void
function BrokenFences:addItems(arg0, arg1) end

---@private
---@param arg0 IsoObject
---@return boolean
function BrokenFences:isNW(arg0) end

---@private
---@param arg0 IsoObject
---@return boolean
function BrokenFences:isUnbroken(arg0) end

---@public
---@param arg0 IsoObject
---@return void
function BrokenFences:setDamagedRight(arg0) end

---@public
---@param arg0 IsoObject
---@return boolean
function BrokenFences:isBreakableObject(arg0) end

---@public
---@return BrokenFences
function BrokenFences:getInstance() end

---@private
---@param arg0 IsoObject
---@return boolean
function BrokenFences:isBrokenRight(arg0) end

---@private
---@param arg0 IsoObject
---@param arg1 IsoDirections
---@return void
function BrokenFences:addDebrisObject(arg0, arg1) end

---@private
---@param arg0 KahluaTableImpl
---@return ArrayList|Unknown
---@overload fun(arg0:KahluaTable, arg1:String)
function BrokenFences:tableToTiles(arg0) end

---@private
---@param arg0 KahluaTable
---@param arg1 String
---@return ArrayList|Unknown
function BrokenFences:tableToTiles(arg0, arg1) end

---@public
---@param arg0 IsoObject
---@return void
function BrokenFences:setDestroyed(arg0) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoDirections
---@param arg2 IsoDirections
---@return void
function BrokenFences:damageAdjacent(arg0, arg1, arg2) end

---@public
---@param arg0 KahluaTableImpl
---@return void
function BrokenFences:addBrokenTiles(arg0) end

---@public
---@param arg0 IsoObject
---@return void
function BrokenFences:setDamagedLeft(arg0) end

---@public
---@param arg0 IsoObject
---@param arg1 IsoDirections
---@return void
function BrokenFences:destroyFence(arg0, arg1) end

---@private
---@param arg0 IsoObject
---@return boolean
function BrokenFences:isBrokenLeft(arg0) end

---@public
---@param arg0 IsoObject
---@param arg1 boolean
---@param arg2 boolean
---@return void
function BrokenFences:updateSprite(arg0, arg1, arg2) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 boolean
---@return IsoObject
function BrokenFences:getBreakableObject(arg0, arg1) end

---@public
---@return void
function BrokenFences:Reset() end
