---@class RDSPokerNight : zombie.randomizedWorld.randomizedDeadSurvivor.RDSPokerNight
---@field private items ArrayList|Unknown
---@field private money String
---@field private card String
RDSPokerNight = {}

---@public
---@param arg0 BuildingDef
---@param arg1 boolean
---@return boolean
function RDSPokerNight:isValid(arg0, arg1) end

---@public
---@param arg0 BuildingDef
---@return void
function RDSPokerNight:randomizeDeadSurvivor(arg0) end
