---@class VehicleScript.Passenger : zombie.scripting.objects.VehicleScript.Passenger
---@field public id String
---@field public anims ArrayList|Unknown
---@field public switchSeats ArrayList|Unknown
---@field public hasRoof boolean
---@field public showPassenger boolean
---@field public door String
---@field public door2 String
---@field public area String
---@field public positions ArrayList|Unknown
VehicleScript_Passenger = {}

---@public
---@param arg0 String
---@return VehicleScript.Position
function VehicleScript_Passenger:getPositionById(arg0) end

---@public
---@return String
function VehicleScript_Passenger:getId() end

---@public
---@return int
function VehicleScript_Passenger:getPositionCount() end

---@public
---@param arg0 int
---@return VehicleScript.Position
function VehicleScript_Passenger:getPosition(arg0) end

---@public
---@param arg0 String
---@return VehicleScript.Passenger.SwitchSeat
function VehicleScript_Passenger:getSwitchSeatById(arg0) end

---@public
---@return VehicleScript.Passenger
function VehicleScript_Passenger:makeCopy() end
