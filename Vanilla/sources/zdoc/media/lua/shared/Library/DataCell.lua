---@class DataCell : zombie.iso.areas.isoregion.data.DataCell
---@field public dataRoot DataRoot
---@field protected dataChunks Map|Unknown|Unknown
DataCell = {}

---@protected
---@return DataRoot
function DataCell:getDataRoot() end

---@protected
---@param arg0 List|Unknown
---@return void
function DataCell:getAllChunks(arg0) end

---@protected
---@param arg0 DataChunk
---@return void
function DataCell:setChunk(arg0) end

---@protected
---@param arg0 int
---@return DataChunk
function DataCell:getChunk(arg0) end

---@protected
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return DataChunk
function DataCell:addChunk(arg0, arg1, arg2) end
