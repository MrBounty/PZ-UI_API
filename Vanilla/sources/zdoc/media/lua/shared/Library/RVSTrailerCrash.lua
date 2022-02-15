---@class RVSTrailerCrash : zombie.randomizedWorld.randomizedVehicleStory.RVSTrailerCrash
RVSTrailerCrash = {}

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSTrailerCrash:spawnElement(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSTrailerCrash:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSTrailerCrash:initVehicleStorySpawner(arg0, arg1, arg2) end
