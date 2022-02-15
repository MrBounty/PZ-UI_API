---@class Stash : zombie.core.stash.Stash
---@field public name String
---@field public type String
---@field public item String
---@field public customName String
---@field public buildingX int
---@field public buildingY int
---@field public spawnTable String
---@field public annotations ArrayList|Unknown
---@field public spawnOnlyOnZed boolean
---@field public minDayToSpawn int
---@field public maxDayToSpawn int
---@field public minTrapToSpawn int
---@field public maxTrapToSpawn int
---@field public zombies int
---@field public containers ArrayList|Unknown
---@field public barricades int
Stash = {}

---@public
---@return String
function Stash:getName() end

---@public
---@param arg0 KahluaTableImpl
---@return void
function Stash:load(arg0) end

---@public
---@return String
function Stash:getItem() end

---@public
---@return int
function Stash:getBuildingX() end

---@public
---@return int
function Stash:getBuildingY() end
