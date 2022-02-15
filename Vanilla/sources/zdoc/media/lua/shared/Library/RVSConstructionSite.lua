---@class RVSConstructionSite : zombie.randomizedWorld.randomizedVehicleStory.RVSConstructionSite
---@field private tools ArrayList|Unknown
RVSConstructionSite = {}

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSConstructionSite:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSConstructionSite:spawnElement(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSConstructionSite:initVehicleStorySpawner(arg0, arg1, arg2) end
