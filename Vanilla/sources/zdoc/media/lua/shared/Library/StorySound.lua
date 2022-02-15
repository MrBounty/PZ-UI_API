---@class StorySound : zombie.radio.StorySounds.StorySound
---@field protected name String
---@field protected baseVolume float
StorySound = {}

---@public
---@return float
function StorySound:getBaseVolume() end

---@public
---@return long
---@overload fun(arg0:float)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float, arg5:float)
function StorySound:playSound() end

---@public
---@param arg0 float
---@return long
function StorySound:playSound(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@return long
function StorySound:playSound(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@return long
function StorySound:playSound(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return String
function StorySound:getName() end

---@public
---@param arg0 float
---@return void
function StorySound:setBaseVolume(arg0) end

---@public
---@return StorySound
function StorySound:getClone() end

---@public
---@param arg0 String
---@return void
function StorySound:setName(arg0) end
