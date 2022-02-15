---@class IsoWeatherFX : zombie.iso.weather.fx.IsoWeatherFX
---@field private VERBOSE boolean
---@field protected DEBUG_BOUNDS boolean
---@field private DELTA float
---@field private cloudParticles ParticleRectangle
---@field private fogParticles ParticleRectangle
---@field private snowParticles ParticleRectangle
---@field private rainParticles ParticleRectangle
---@field private ID_CLOUD int
---@field private ID_FOG int
---@field private ID_SNOW int
---@field private ID_RAIN int
---@field public ZoomMod float
---@field protected playerIndoors boolean
---@field protected windPrecipIntensity SteppedUpdateFloat
---@field protected windIntensity SteppedUpdateFloat
---@field protected windAngleIntensity SteppedUpdateFloat
---@field protected precipitationIntensity SteppedUpdateFloat
---@field protected precipitationIntensitySnow SteppedUpdateFloat
---@field protected precipitationIntensityRain SteppedUpdateFloat
---@field protected cloudIntensity SteppedUpdateFloat
---@field protected fogIntensity SteppedUpdateFloat
---@field protected windAngleMod SteppedUpdateFloat
---@field protected precipitationIsSnow boolean
---@field private fogOverlayAlpha float
---@field private windSpeedMax float
---@field protected windSpeed float
---@field protected windSpeedFog float
---@field protected windAngle float
---@field protected windAngleClouds float
---@field private texFogCircle Texture
---@field private texFogWhite Texture
---@field private fogColor Color
---@field protected indoorsAlphaMod SteppedUpdateFloat
---@field private particleRectangles ArrayList|Unknown
---@field protected instance IsoWeatherFX
---@field private windUpdCounter float
---@field s_shader Shader
---@field s_drawer IsoWeatherFX.Drawer[][]
IsoWeatherFX = {}

---@public
---@param arg0 boolean
---@return void
function IsoWeatherFX:setPrecipitationIsSnow(arg0) end

---@public
---@return float
function IsoWeatherFX:getWindIntensity() end

---@public
---@param arg0 float
---@return void
function IsoWeatherFX:setWindIntensity(arg0) end

---@public
---@param arg0 float
---@return void
function IsoWeatherFX:setWindPrecipIntensity(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function IsoWeatherFX:clamp(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function IsoWeatherFX:clerp(arg0, arg1, arg2) end

---@public
---@return float
function IsoWeatherFX:getWindAngleIntensity() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function IsoWeatherFX:lerp(arg0, arg1, arg2) end

---@public
---@return boolean
function IsoWeatherFX:hasCloudsToRender() end

---@public
---@return float
function IsoWeatherFX:getRenderWindAngleRain() end

---@public
---@return float
function IsoWeatherFX:getPrecipitationIntensity() end

---@public
---@param arg0 float
---@return void
function IsoWeatherFX:setPrecipitationIntensity(arg0) end

---@public
---@return void
function IsoWeatherFX:renderPrecipitation() end

---@public
---@return void
function IsoWeatherFX:renderFog() end

---@public
---@return void
function IsoWeatherFX:update() end

---@public
---@param arg0 float
---@return void
function IsoWeatherFX:setFogIntensity(arg0) end

---@public
---@return float
function IsoWeatherFX:getFogIntensity() end

---@private
---@return void
function IsoWeatherFX:renderFogCircle() end

---@public
---@param arg0 float
---@return void
function IsoWeatherFX:setWindAngleIntensity(arg0) end

---@public
---@return float
function IsoWeatherFX:getCloudIntensity() end

---@public
---@return void
function IsoWeatherFX:renderClouds() end

---@public
---@return float
function IsoWeatherFX:getWindPrecipIntensity() end

---@public
---@param arg0 boolean
---@return void
function IsoWeatherFX:setDebugBounds(arg0) end

---@public
---@return boolean
function IsoWeatherFX:hasPrecipitationToRender() end

---@public
---@return boolean
function IsoWeatherFX:hasFogToRender() end

---@public
---@param arg0 float
---@return void
function IsoWeatherFX:setCloudIntensity(arg0) end

---@public
---@return void
function IsoWeatherFX:render() end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@param arg2 boolean
---@return void
function IsoWeatherFX:renderLayered(arg0, arg1, arg2) end

---@public
---@return void
function IsoWeatherFX:init() end

---@public
---@return boolean
function IsoWeatherFX:getPrecipitationIsSnow() end

---@public
---@return boolean
function IsoWeatherFX:isDebugBounds() end
