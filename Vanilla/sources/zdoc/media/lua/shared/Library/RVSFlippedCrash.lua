---@class RVSFlippedCrash : zombie.randomizedWorld.randomizedVehicleStory.RVSFlippedCrash
RVSFlippedCrash = {}

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSFlippedCrash:initVehicleStorySpawner(arg0, arg1, arg2) end

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSFlippedCrash:spawnElement(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSFlippedCrash:randomizeVehicleStory(arg0, arg1) end
