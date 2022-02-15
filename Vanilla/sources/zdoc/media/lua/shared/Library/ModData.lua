---@class ModData : zombie.world.moddata.ModData
---@field private temp_list ArrayList|Unknown
ModData = {}

---@public
---@param arg0 String
---@return void
function ModData:request(arg0) end

---@public
---@return ArrayList|Unknown
function ModData:getTableNames() end

---@public
---@param arg0 String
---@return KahluaTable
function ModData:get(arg0) end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@return void
function ModData:add(arg0, arg1) end

---@public
---@param arg0 String
---@return KahluaTable
function ModData:remove(arg0) end

---@public
---@return String
---@overload fun(arg0:String)
function ModData:create() end

---@public
---@param arg0 String
---@return KahluaTable
function ModData:create(arg0) end

---@public
---@param arg0 String
---@return boolean
function ModData:exists(arg0) end

---@public
---@param arg0 String
---@return void
function ModData:transmit(arg0) end

---@public
---@param arg0 String
---@return KahluaTable
function ModData:getOrCreate(arg0) end
