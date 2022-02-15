---@class RandomizedZoneStoryBase : zombie.randomizedWorld.randomizedZoneStory.RandomizedZoneStoryBase
---@field public alwaysDo boolean
---@field public baseChance int
---@field public totalChance int
---@field public zoneStory String
---@field public chance int
---@field protected minZoneWidth int
---@field protected minZoneHeight int
---@field public zoneType ArrayList|Unknown
---@field private rzsMap HashMap|Unknown|Unknown
RandomizedZoneStoryBase = {}

---@public
---@return boolean
---@overload fun(arg0:IsoMetaGrid.Zone, arg1:boolean)
function RandomizedZoneStoryBase:isValid() end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 boolean
---@return boolean
function RandomizedZoneStoryBase:isValid(arg0, arg1) end

---@public
---@return int
function RandomizedZoneStoryBase:getMinimumWidth() end

---@public
---@param arg0 IsoMetaGrid.Zone
---@return void
function RandomizedZoneStoryBase:randomizeZoneStory(arg0) end

---@private
---@param arg0 IsoMetaGrid.Zone
---@param arg1 boolean
---@return boolean
function RandomizedZoneStoryBase:checkCanSpawnStory(arg0, arg1) end

---@private
---@return RandomizedZoneStoryBase
function RandomizedZoneStoryBase:getRandomStory() end

---@private
---@param arg0 IsoMetaGrid.Zone
---@return boolean
function RandomizedZoneStoryBase:doRandomStory(arg0) end

---@public
---@param arg0 RandomizedZoneStoryBase
---@param arg1 IsoMetaGrid.Zone
---@return IsoGridSquare
function RandomizedZoneStoryBase:getRandomFreeSquare(arg0, arg1) end

---@public
---@return int
function RandomizedZoneStoryBase:getMinimumHeight() end

---@public
---@param arg0 RandomizedZoneStoryBase
---@param arg1 IsoMetaGrid.Zone
---@return IsoGridSquare
function RandomizedZoneStoryBase:getRandomFreeSquareFullZone(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@param arg1 boolean
---@return boolean
function RandomizedZoneStoryBase:isValidForStory(arg0, arg1) end

---@public
---@param arg0 RandomizedZoneStoryBase
---@param arg1 IsoMetaGrid.Zone
---@return void
function RandomizedZoneStoryBase:cleanAreaForStory(arg0, arg1) end

---@public
---@param arg0 IsoMetaGrid.Zone
---@return void
function RandomizedZoneStoryBase:initAllRZSMapChance(arg0) end
