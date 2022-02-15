---@class ThunderStorm.ThunderCloud : zombie.iso.weather.ThunderStorm.ThunderCloud
---@field private currentX int
---@field private currentY int
---@field private startX int
---@field private startY int
---@field private endX int
---@field private endY int
---@field private startTime double
---@field private endTime double
---@field private duration double
---@field private strength float
---@field private angle float
---@field private radius float
---@field private eventFrequency float
---@field private thunderRatio float
---@field private percentageOffset float
---@field private isRunning boolean
---@field private suspendTimer GameTime.AnimTimer
ThunderStorm_ThunderCloud = {}

---@public
---@return float
function ThunderStorm_ThunderCloud:getStrength() end

---@public
---@return boolean
function ThunderStorm_ThunderCloud:isRunning() end

---@public
---@return int
function ThunderStorm_ThunderCloud:getCurrentX() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 float
---@return void
function ThunderStorm_ThunderCloud:setCenter(arg0, arg1, arg2) end

---@public
---@return double
function ThunderStorm_ThunderCloud:lifeTime() end

---@public
---@return float
function ThunderStorm_ThunderCloud:getRadius() end

---@public
---@return int
function ThunderStorm_ThunderCloud:getCurrentY() end
