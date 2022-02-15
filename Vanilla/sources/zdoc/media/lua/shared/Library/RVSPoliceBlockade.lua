---@class RVSPoliceBlockade : zombie.randomizedWorld.randomizedVehicleStory.RVSPoliceBlockade
RVSPoliceBlockade = {}

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSPoliceBlockade:initVehicleStorySpawner(arg0, arg1, arg2) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSPoliceBlockade:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSPoliceBlockade:spawnElement(arg0, arg1) end
