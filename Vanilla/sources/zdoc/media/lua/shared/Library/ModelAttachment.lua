---@class ModelAttachment : zombie.scripting.objects.ModelAttachment
---@field private id String
---@field private offset Vector3f
---@field private rotate Vector3f
---@field private bone String
---@field private canAttach ArrayList|Unknown
---@field private zoffset float
---@field private updateConstraint boolean
ModelAttachment = {}

---@public
---@param arg0 String
---@return void
function ModelAttachment:setId(arg0) end

---@public
---@return boolean
function ModelAttachment:isUpdateConstraint() end

---@public
---@param arg0 float
---@return void
function ModelAttachment:setZOffset(arg0) end

---@public
---@return Vector3f
function ModelAttachment:getRotate() end

---@public
---@return float
function ModelAttachment:getZOffset() end

---@public
---@param arg0 String
---@return void
function ModelAttachment:setBone(arg0) end

---@public
---@param arg0 boolean
---@return void
function ModelAttachment:setUpdateConstraint(arg0) end

---@public
---@return String
function ModelAttachment:getId() end

---@public
---@return String
function ModelAttachment:getBone() end

---@public
---@return ArrayList|Unknown
function ModelAttachment:getCanAttach() end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function ModelAttachment:setCanAttach(arg0) end

---@public
---@return Vector3f
function ModelAttachment:getOffset() end
