---@class RDSBedroomZed : zombie.randomizedWorld.randomizedDeadSurvivor.RDSBedroomZed
---@field private pantsMaleItems ArrayList|Unknown
---@field private pantsFemaleItems ArrayList|Unknown
---@field private topItems ArrayList|Unknown
---@field private shoesItems ArrayList|Unknown
RDSBedroomZed = {}

---@private
---@param arg0 RoomDef
---@param arg1 boolean
---@return void
function RDSBedroomZed:addItemsOnGround(arg0, arg1) end

---@public
---@param arg0 BuildingDef
---@return void
function RDSBedroomZed:randomizeDeadSurvivor(arg0) end
