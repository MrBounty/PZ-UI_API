---@class RVSAmbulanceCrash : zombie.randomizedWorld.randomizedVehicleStory.RVSAmbulanceCrash
RVSAmbulanceCrash = {}

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSAmbulanceCrash:initVehicleStorySpawner(arg0, arg1, arg2) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSAmbulanceCrash:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSAmbulanceCrash:spawnElement(arg0, arg1) end
