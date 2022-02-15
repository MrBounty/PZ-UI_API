---@class DBResult : zombie.network.DBResult
---@field private values HashMap|Unknown|Unknown
---@field private columns ArrayList|Unknown
---@field private type String
---@field private tableName String
DBResult = {}

---@public
---@return HashMap|Unknown|Unknown
function DBResult:getValues() end

---@public
---@return String
function DBResult:getTableName() end

---@public
---@return String
function DBResult:getType() end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function DBResult:setColumns(arg0) end

---@public
---@return ArrayList|Unknown
function DBResult:getColumns() end

---@public
---@param arg0 String
---@return void
function DBResult:setTableName(arg0) end

---@public
---@param arg0 String
---@return void
function DBResult:setType(arg0) end
