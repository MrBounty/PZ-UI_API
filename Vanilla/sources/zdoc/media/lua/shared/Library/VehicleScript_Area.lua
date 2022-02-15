---@class VehicleScript.Area : zombie.scripting.objects.VehicleScript.Area
---@field public id String
---@field public x float
---@field public y float
---@field public w float
---@field public h float
VehicleScript_Area = {}

---@public
---@param arg0 Double
---@return void
function VehicleScript_Area:setH(arg0) end

---@public
---@param arg0 Double
---@return void
function VehicleScript_Area:setY(arg0) end

---@public
---@param arg0 Double
---@return void
function VehicleScript_Area:setX(arg0) end

---@public
---@return Double
function VehicleScript_Area:getW() end

---@public
---@return String
function VehicleScript_Area:getId() end

---@private
---@return VehicleScript.Area
function VehicleScript_Area:makeCopy() end

---@public
---@return Double
function VehicleScript_Area:getX() end

---@public
---@return Double
function VehicleScript_Area:getY() end

---@public
---@return Double
function VehicleScript_Area:getH() end

---@public
---@param arg0 Double
---@return void
function VehicleScript_Area:setW(arg0) end
