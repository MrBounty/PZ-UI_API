---@class RBBasic : zombie.randomizedWorld.randomizedBuilding.RBBasic
---@field private specificProfessionDistribution ArrayList|Unknown
---@field private specificProfessionRoomDistribution Map|Unknown|Unknown
---@field private kitchenSinkItems HashMap|Unknown|Unknown
---@field private kitchenCounterItems HashMap|Unknown|Unknown
---@field private kitchenStoveItems HashMap|Unknown|Unknown
---@field private bathroomSinkItems HashMap|Unknown|Unknown
---@field private coldFood ArrayList|Unknown
---@field private plankStash Map|Unknown|Unknown
---@field private deadSurvivorsStory ArrayList|Unknown
---@field private totalChanceRDS int
---@field private rdsMap HashMap|Unknown|Unknown
---@field private uniqueRDSSpawned ArrayList|Unknown
---@field private tablesDone ArrayList|Unknown
---@field private doneTable boolean
RBBasic = {}

---@public
---@return ArrayList|Unknown
function RBBasic:getUniqueRDSSpawned() end

---@public
---@param arg0 BuildingDef
---@return void
function RBBasic:randomizeBuilding(arg0) end

---@public
---@param arg0 BuildingDef
---@param arg1 String
---@return void
function RBBasic:doProfessionStory(arg0, arg1) end

---@private
---@param arg0 IsoSprite
---@return IsoDirections
function RBBasic:getFacing(arg0) end

---@private
---@param arg0 IsoGridSquare
---@return void
function RBBasic:doLivingRoomStuff(arg0) end

---@private
---@param arg0 BuildingDef
---@param arg1 IsoObject
---@return void
function RBBasic:checkForTableSpawn(arg0, arg1) end

---@private
---@param arg0 IsoGridSquare
---@return void
function RBBasic:doKitchenStuff(arg0) end

---@public
---@return ArrayList|Unknown
function RBBasic:getSurvivorProfession() end

---@public
---@param arg0 BuildingDef
---@param arg1 RandomizedDeadSurvivorBase
---@return void
function RBBasic:doRandomDeadSurvivorStory(arg0, arg1) end

---@private
---@param arg0 IsoGridSquare
---@return void
function RBBasic:doBathroomStuff(arg0) end

---@private
---@param arg0 BuildingDef
---@return void
function RBBasic:initRDSMap(arg0) end

---@private
---@param arg0 IsoDirections
---@param arg1 IsoObject
---@param arg2 IsoGridSquare
---@param arg3 HashMap|Unknown|Unknown
---@return void
function RBBasic:generateSinkClutter(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 IsoDirections
---@param arg1 IsoObject
---@param arg2 IsoGridSquare
---@param arg3 HashMap|Unknown|Unknown
---@return void
function RBBasic:generateCounterClutter(arg0, arg1, arg2, arg3) end

---@public
---@return ArrayList|Unknown
function RBBasic:getSurvivorStories() end

---@private
---@param arg0 IsoDirections
---@param arg1 IsoObject
---@param arg2 IsoGridSquare
---@return void
function RBBasic:generateKitchenStoveClutter(arg0, arg1, arg2) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoObject
---@return IsoObject
function RBBasic:checkForTable(arg0, arg1) end

---@private
---@param arg0 IsoGridSquare
---@return void
function RBBasic:doBedroomStuff(arg0) end

---@private
---@param arg0 BuildingDef
---@return void
function RBBasic:addRandomDeadSurvivorStory(arg0) end
