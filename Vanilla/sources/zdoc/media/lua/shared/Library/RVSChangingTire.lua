---@class RVSChangingTire : zombie.randomizedWorld.randomizedVehicleStory.RVSChangingTire
RVSChangingTire = {}

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSChangingTire:spawnElement(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSChangingTire:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSChangingTire:initVehicleStorySpawner(arg0, arg1, arg2) end
