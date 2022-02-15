---@class BaseAmbientStreamManager : zombie.BaseAmbientStreamManager
BaseAmbientStreamManager = {}

---@public
---@param name String
---@param x int
---@param y int
---@param radius int
---@param volume float
---@return void
function BaseAmbientStreamManager:addAmbient(name, x, y, radius, volume) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 int
---@param arg3 String
---@return void
function BaseAmbientStreamManager:addAmbientEmitter(arg0, arg1, arg2, arg3) end

---@protected
---@return void
function BaseAmbientStreamManager:addRandomAmbient() end

---@public
---@param name String
---@param vol float
---@param bIndoors boolean
---@param bRain boolean
---@param bNight boolean
---@param bDay boolean
---@return void
function BaseAmbientStreamManager:addBlend(name, vol, bIndoors, bRain, bNight, bDay) end

---@public
---@return void
function BaseAmbientStreamManager:init() end

---@public
---@param room RoomDef
---@return void
function BaseAmbientStreamManager:doAlarm(room) end

---@public
---@return void
function BaseAmbientStreamManager:doGunEvent() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 int
---@param arg3 String
---@return void
function BaseAmbientStreamManager:addDaytimeAmbientEmitter(arg0, arg1, arg2, arg3) end

---@public
---@return void
function BaseAmbientStreamManager:doOneShotAmbients() end

---@public
---@return void
function BaseAmbientStreamManager:update() end

---@public
---@return void
function BaseAmbientStreamManager:stop() end
