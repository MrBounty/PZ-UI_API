---@class SLSoundManager : zombie.radio.StorySounds.SLSoundManager
---@field public ENABLED boolean
---@field public DEBUG boolean
---@field public LUA_DEBUG boolean
---@field public Emitter StoryEmitter
---@field private instance SLSoundManager
---@field private state HashMap|Unknown|Unknown
---@field private storySounds ArrayList|Unknown
---@field private nextTick int
---@field private borderCenterX float
---@field private borderCenterY float
---@field private borderRadiusMin float
---@field private borderRadiusMax float
---@field private borderScale float
SLSoundManager = {}

---@private
---@param arg0 UIFont
---@param arg1 String
---@param arg2 int
---@param arg3 int
---@return void
function SLSoundManager:renderLine(arg0, arg1, arg2, arg3) end

---@public
---@return void
function SLSoundManager:render() end

---@public
---@return void
function SLSoundManager:renderDebug() end

---@public
---@return boolean
function SLSoundManager:getLuaDebug() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function SLSoundManager:update(arg0, arg1, arg2) end

---@public
---@return void
function SLSoundManager:init() end

---@public
---@return void
function SLSoundManager:loadSounds() end

---@public
---@return float
function SLSoundManager:getRandomBorderRange() end

---@private
---@param arg0 StorySound
---@return void
function SLSoundManager:addStorySound(arg0) end

---@public
---@return void
function SLSoundManager:updateKeys() end

---@public
---@return ArrayList|Unknown
function SLSoundManager:getStorySounds() end

---@public
---@return SLSoundManager
function SLSoundManager:getInstance() end

---@public
---@param arg0 String
---@return void
function SLSoundManager:print(arg0) end

---@public
---@return JVector2
function SLSoundManager:getRandomBorderPosition() end

---@public
---@return boolean
function SLSoundManager:getDebug() end

---@public
---@return void
function SLSoundManager:thunderTest() end
