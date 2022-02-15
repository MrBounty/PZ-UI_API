---@class RVSBurntCar : zombie.randomizedWorld.randomizedVehicleStory.RVSBurntCar
RVSBurntCar = {}

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSBurntCar:spawnElement(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSBurntCar:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSBurntCar:initVehicleStorySpawner(arg0, arg1, arg2) end
