---@class RandomizedBuildingBase : zombie.randomizedWorld.randomizedBuilding.RandomizedBuildingBase
---@field private chance int
---@field private totalChance int
---@field private rbMap HashMap|Unknown|Unknown
---@field protected KBBuildingX int
---@field protected KBBuildingY int
---@field private alwaysDo boolean
---@field private weaponsList HashMap|Unknown|Unknown
RandomizedBuildingBase = {}

---@public
---@param arg0 boolean
---@return void
function RandomizedBuildingBase:setAlwaysDo(arg0) end

---@public
---@param arg0 IsoGridSquare
---@return IsoWindow
function RandomizedBuildingBase:getWindow(arg0) end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@param arg2 IsoObject
---@return InventoryItem
---@overload fun(arg0:String, arg1:IsoGridSquare, arg2:float, arg3:float, arg4:float)
---@overload fun(arg0:String, arg1:IsoGridSquare, arg2:float, arg3:float, arg4:float, arg5:int)
function RandomizedBuildingBase:addWorldItem(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@return InventoryItem
function RandomizedBuildingBase:addWorldItem(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 int
---@return InventoryItem
function RandomizedBuildingBase:addWorldItem(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 BuildingDef
---@param arg1 String
---@param arg2 int
---@return void
function RandomizedBuildingBase:spawnItemsInContainers(arg0, arg1, arg2) end

---@protected
---@param arg0 BuildingDef
---@return void
function RandomizedBuildingBase:removeAllZombies(arg0) end

---@public
---@return void
function RandomizedBuildingBase:initAllRBMapChance() end

---@public
---@param arg0 IsoGridSquare
---@param arg1 int
---@return void
function RandomizedBuildingBase:addBarricade(arg0, arg1) end

---@private
---@param arg0 BuildingDef
---@return void
function RandomizedBuildingBase:customizeStartingHouse(arg0) end

---@public
---@return boolean
function RandomizedBuildingBase:isAlwaysDo() end

---@private
---@return RandomizedBuildingBase
function RandomizedBuildingBase:getRandomStory() end

---@public
---@param arg0 int
---@param arg1 String
---@param arg2 Integer
---@param arg3 IsoGridSquare
---@return ArrayList|Unknown
function RandomizedBuildingBase:addZombiesOnSquare(arg0, arg1, arg2, arg3) end

---@public
---@return int
function RandomizedBuildingBase:getMinimumRooms() end

---@public
---@return void
function RandomizedBuildingBase:init() end

---@public
---@param arg0 BuildingDef
---@return void
function RandomizedBuildingBase:randomizeBuilding(arg0) end

---@public
---@return int
function RandomizedBuildingBase:getChance() end

---@public
---@param arg0 int
---@return void
function RandomizedBuildingBase:setMinimumRooms(arg0) end

---@public
---@param arg0 IsoBuilding
---@return void
function RandomizedBuildingBase:ChunkLoaded(arg0) end

---@public
---@param arg0 BuildingDef
---@param arg1 boolean
---@return boolean
function RandomizedBuildingBase:isValid(arg0, arg1) end

---@public
---@param arg0 ItemContainer
---@param arg1 boolean
---@param arg2 boolean
---@param arg3 boolean
---@return HandWeapon
function RandomizedBuildingBase:addRandomRangedWeapon(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@return void
function RandomizedBuildingBase:setMinimumDays(arg0) end

---@public
---@return int
function RandomizedBuildingBase:getMinimumDays() end

---@public
---@param arg0 IsoGridSquare
---@return IsoDoor
function RandomizedBuildingBase:getDoor(arg0) end

---@public
---@param arg0 BuildingDef
---@param arg1 int
---@param arg2 String
---@param arg3 Integer
---@param arg4 RoomDef
---@return ArrayList|Unknown
function RandomizedBuildingBase:addZombies(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 IsoObject
---@param arg1 IsoGridSquare
---@return boolean
function RandomizedBuildingBase:isTableFor3DItems(arg0, arg1) end

---@public
---@param arg0 int
---@return void
function RandomizedBuildingBase:setChance(arg0) end
