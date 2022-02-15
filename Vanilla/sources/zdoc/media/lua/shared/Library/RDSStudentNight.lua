---@class RDSStudentNight : zombie.randomizedWorld.randomizedDeadSurvivor.RDSStudentNight
---@field private items ArrayList|Unknown
---@field private otherItems ArrayList|Unknown
---@field private pantsMaleItems ArrayList|Unknown
---@field private pantsFemaleItems ArrayList|Unknown
---@field private topItems ArrayList|Unknown
---@field private shoesItems ArrayList|Unknown
RDSStudentNight = {}

---@private
---@param arg0 RoomDef
---@param arg1 boolean
---@return void
function RDSStudentNight:addItemsOnGround(arg0, arg1) end

---@public
---@param arg0 BuildingDef
---@return void
function RDSStudentNight:randomizeDeadSurvivor(arg0) end
