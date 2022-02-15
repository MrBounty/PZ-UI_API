---@class RDSZombieLockedBathroom : zombie.randomizedWorld.randomizedDeadSurvivor.RDSZombieLockedBathroom
RDSZombieLockedBathroom = {}

---@private
---@param arg0 IsoDoor
---@param arg1 RoomDef
---@return boolean
function RDSZombieLockedBathroom:isDoorToRoom(arg0, arg1) end

---@private
---@param arg0 IsoDoor
---@return IsoDeadBody
function RDSZombieLockedBathroom:addDeadBodyTheOtherSide(arg0) end

---@public
---@param arg0 BuildingDef
---@return void
function RDSZombieLockedBathroom:randomizeDeadSurvivor(arg0) end

---@private
---@param arg0 IsoGridSquare
---@return boolean
function RDSZombieLockedBathroom:checkIsBathroom(arg0) end
