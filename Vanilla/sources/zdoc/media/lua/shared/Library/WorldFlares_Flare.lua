---@class WorldFlares.Flare : zombie.iso.weather.WorldFlares.Flare
---@field private id int
---@field private x float
---@field private y float
---@field private range int
---@field private windSpeed float
---@field private color ClimateColorInfo
---@field private hasLaunched boolean
---@field private intensity SteppedUpdateFloat
---@field private maxLifeTime float
---@field private lifeTime float
---@field private nextRandomTargetIntens int
---@field private perc float
---@field private infos WorldFlares.PlayerFlareLightInfo[]
WorldFlares_Flare = {}

---@public
---@return float
function WorldFlares_Flare:getLifeTime() end

---@public
---@return float
function WorldFlares_Flare:getX() end

---@public
---@return float
function WorldFlares_Flare:getMaxLifeTime() end

---@public
---@return float
function WorldFlares_Flare:getWindSpeed() end

---@public
---@return ClimateColorInfo
function WorldFlares_Flare:getColor() end

---@private
---@return void
function WorldFlares_Flare:update() end

---@private
---@param arg0 RenderSettings.PlayerRenderSettings
---@param arg1 int
---@param arg2 IsoPlayer
---@return void
function WorldFlares_Flare:applyFlare(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@return float
function WorldFlares_Flare:getIntensityPlayer(arg0) end

---@public
---@param arg0 int
---@return float
function WorldFlares_Flare:getDistModPlayer(arg0) end

---@public
---@return int
function WorldFlares_Flare:getRange() end

---@public
---@param arg0 int
---@return ClimateColorInfo
function WorldFlares_Flare:getColorPlayer(arg0) end

---@public
---@return float
function WorldFlares_Flare:getY() end

---@public
---@return float
function WorldFlares_Flare:getIntensity() end

---@public
---@return float
function WorldFlares_Flare:getPercent() end

---@public
---@return boolean
function WorldFlares_Flare:isHasLaunched() end

---@public
---@param arg0 int
---@return ClimateColorInfo
function WorldFlares_Flare:getOutColorPlayer(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return int
function WorldFlares_Flare:GetDistance(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@return float
function WorldFlares_Flare:getLerpPlayer(arg0) end

---@public
---@return int
function WorldFlares_Flare:getId() end
