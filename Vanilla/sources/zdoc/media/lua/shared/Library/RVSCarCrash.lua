---@class RVSCarCrash : zombie.randomizedWorld.randomizedVehicleStory.RVSCarCrash
RVSCarCrash = {}

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSCarCrash:spawnElement(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSCarCrash:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSCarCrash:initVehicleStorySpawner(arg0, arg1, arg2) end
