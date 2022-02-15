---@class RVSPoliceBlockadeShooting : zombie.randomizedWorld.randomizedVehicleStory.RVSPoliceBlockadeShooting
RVSPoliceBlockadeShooting = {}

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSPoliceBlockadeShooting:initVehicleStorySpawner(arg0, arg1, arg2) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@param arg2 boolean
---@return boolean
function RVSPoliceBlockadeShooting:isValid(arg0, arg1, arg2) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 IsoChunk
---@return void
function RVSPoliceBlockadeShooting:randomizeVehicleStory(arg0, arg1) end

---@public
---@param arg0 VehicleStorySpawner
---@param arg1 VehicleStorySpawner.Element
---@return void
function RVSPoliceBlockadeShooting:spawnElement(arg0, arg1) end
