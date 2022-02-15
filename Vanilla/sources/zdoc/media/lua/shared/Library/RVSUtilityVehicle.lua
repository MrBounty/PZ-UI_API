---@class RVSUtilityVehicle : zombie.randomizedWorld.randomizedVehicleStory.RVSUtilityVehicle
---@field private tools ArrayList|Unknown
---@field private carpenterTools ArrayList|Unknown
---@field private params RVSUtilityVehicle.Params
RVSUtilityVehicle = {}

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSUtilityVehicle:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 String
---@param arg3 String
---@param arg4 String
---@param arg5 Integer
---@param arg6 String
---@param arg7 ArrayList|Unknown
---@param arg8 int
---@param arg9 boolean
---@return void
function RVSUtilityVehicle:doUtilityVehicle(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSUtilityVehicle:spawnElement(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSUtilityVehicle:initVehicleStorySpawner(arg0, arg1, arg2) end
