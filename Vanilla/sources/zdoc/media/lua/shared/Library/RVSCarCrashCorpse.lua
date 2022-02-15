---@class RVSCarCrashCorpse : zombie.randomizedWorld.randomizedVehicleStory.RVSCarCrashCorpse
RVSCarCrashCorpse = {}

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSCarCrashCorpse:spawnElement(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSCarCrashCorpse:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSCarCrashCorpse:initVehicleStorySpawner(arg0, arg1, arg2) end
