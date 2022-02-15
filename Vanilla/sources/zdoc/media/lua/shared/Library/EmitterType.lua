---@class EmitterType : fmod.fmod.EmitterType
---@field public Footstep EmitterType
---@field public Voice EmitterType
---@field public Extra EmitterType
EmitterType = {}

---@public
---@return EmitterType[]
function EmitterType:values() end

---@public
---@param arg0 String
---@return EmitterType
function EmitterType:valueOf(arg0) end
