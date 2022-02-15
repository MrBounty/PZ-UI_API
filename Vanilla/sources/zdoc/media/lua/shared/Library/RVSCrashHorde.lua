---@class RVSCrashHorde : zombie.randomizedWorld.randomizedVehicleStory.RVSCrashHorde
RVSCrashHorde = {}

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSCrashHorde:spawnElement(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSCrashHorde:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSCrashHorde:initVehicleStorySpawner(arg0, arg1, arg2) end
