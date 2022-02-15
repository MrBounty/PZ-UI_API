---@class WorldFlares : zombie.iso.weather.WorldFlares
---@field public ENABLED boolean
---@field public DEBUG_DRAW boolean
---@field public NEXT_ID int
---@field private flares ArrayList|Unknown
WorldFlares = {}

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@param arg9 int
---@return void
function WorldFlares:DrawIsoLine(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@return void
function WorldFlares:debugRender() end

---@public
---@param arg0 int
---@return WorldFlares.Flare
function WorldFlares:getFlareID(arg0) end

---@public
---@return void
function WorldFlares:Clear() end

---@public
---@param arg0 int
---@return WorldFlares.Flare
function WorldFlares:getFlare(arg0) end

---@public
---@return void
function WorldFlares:update() end

---@public
---@return boolean
function WorldFlares:getDebugDraw() end

---@public
---@param arg0 boolean
---@return void
function WorldFlares:setDebugDraw(arg0) end

---@public
---@param arg0 RenderSettings.PlayerRenderSettings
---@param arg1 int
---@param arg2 IsoPlayer
---@return void
function WorldFlares:applyFlaresForPlayer(arg0, arg1, arg2) end

---@public
---@return int
function WorldFlares:getFlareCount() end

---@public
---@param arg0 float
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@param arg9 float
---@param arg10 float
---@return void
function WorldFlares:launchFlare(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end
