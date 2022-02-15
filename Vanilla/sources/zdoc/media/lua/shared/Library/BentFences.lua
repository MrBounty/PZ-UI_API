---@class BentFences : zombie.iso.BentFences
---@field private instance BentFences
---@field private m_entries ArrayList|Unknown
---@field private m_bentMap HashMap|Unknown|Unknown
---@field private m_unbentMap HashMap|Unknown|Unknown
BentFences = {}

---@public
---@param arg0 IsoObject
---@return void
function BentFences:unbendFence(arg0) end

---@private
---@param arg0 KahluaTableImpl
---@param arg1 ArrayList|Unknown
---@return void
---@overload fun(arg0:KahluaTable, arg1:ArrayList|Unknown, arg2:String)
function BentFences:tableToTiles(arg0, arg1) end

---@private
---@param arg0 KahluaTable
---@param arg1 ArrayList|Unknown
---@param arg2 String
---@return void
function BentFences:tableToTiles(arg0, arg1, arg2) end

---@public
---@return BentFences
function BentFences:getInstance() end

---@param arg0 IsoGridSquare
---@param arg1 ArrayList|Unknown
---@param arg2 int
---@return IsoObject
function BentFences:getObjectForEntry(arg0, arg1, arg2) end

---@public
---@param arg0 IsoObject
---@param arg1 IsoDirections
---@return void
function BentFences:bendFence(arg0, arg1) end

---@public
---@param arg0 IsoObject
---@param arg1 IsoDirections
---@return void
function BentFences:swapTiles(arg0, arg1) end

---@private
---@param arg0 IsoObject
---@param arg1 BentFences.Entry
---@param arg2 boolean
---@return boolean
function BentFences:isValidObject(arg0, arg1, arg2) end

---@public
---@param arg0 IsoObject
---@return boolean
function BentFences:isUnbentObject(arg0) end

---@public
---@param arg0 int
---@param arg1 KahluaTableImpl
---@return void
function BentFences:addFenceTiles(arg0, arg1) end

---@public
---@param arg0 IsoObject
---@return boolean
function BentFences:isBentObject(arg0) end

---@private
---@param arg0 IsoObject
---@param arg1 IsoDirections
---@return BentFences.Entry
function BentFences:getEntryForObject(arg0, arg1) end

---@public
---@return void
function BentFences:Reset() end
