---@class IsoDirections : zombie.iso.IsoDirections
---@field public N IsoDirections
---@field public NW IsoDirections
---@field public W IsoDirections
---@field public SW IsoDirections
---@field public S IsoDirections
---@field public SE IsoDirections
---@field public E IsoDirections
---@field public NE IsoDirections
---@field public Max IsoDirections
---@field private VALUES IsoDirections[]
---@field private directionLookup IsoDirections[][]
---@field private temp JVector2
---@field private index int
IsoDirections = {}

---@public
---@return IsoDirections
---@overload fun(dir:IsoDirections)
---@overload fun(arg0:int)
function IsoDirections:RotLeft() end

---@public
---@param dir IsoDirections
---@return IsoDirections
function IsoDirections:RotLeft(dir) end

---@public
---@param arg0 int
---@return IsoDirections
function IsoDirections:RotLeft(arg0) end

---@public
---@return int
function IsoDirections:index() end

---@public
---@param angle JVector2
---@return IsoDirections
---@overload fun(arg0:float)
---@overload fun(arg0:float, arg1:float)
function IsoDirections:fromAngle(angle) end

---@public
---@param arg0 float
---@return IsoDirections
function IsoDirections:fromAngle(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return IsoDirections
function IsoDirections:fromAngle(arg0, arg1) end

---@public
---@return IsoDirections
function IsoDirections:getRandom() end

---@public
---@return void
function IsoDirections:generateTables() end

---@public
---@return float
function IsoDirections:toAngle() end

---Returns the enum constant of this type with the specified name.
---
---The string must match exactly an identifier used to declare an
---
---enum constant in this type.  (Extraneous whitespace characters are
---
---not permitted.)
---@public
---@param name String @the name of the enum constant to be returned.
---@return IsoDirections @the enum constant with the specified name
function IsoDirections:valueOf(name) end

---@public
---@param dir IsoDirections
---@return IsoDirections
function IsoDirections:reverse(dir) end

---@public
---@return IsoDirections
---@overload fun(arg0:int)
---@overload fun(dir:IsoDirections)
function IsoDirections:RotRight() end

---@public
---@param arg0 int
---@return IsoDirections
function IsoDirections:RotRight(arg0) end

---@public
---@param dir IsoDirections
---@return IsoDirections
function IsoDirections:RotRight(dir) end

---@public
---@return String
function IsoDirections:toCompassString() end

---@public
---@return JVector2
function IsoDirections:ToVector() end

---@public
---@param arg0 JVector2
---@return IsoDirections
function IsoDirections:cardinalFromAngle(arg0) end

---@public
---@param angle JVector2
---@return IsoDirections
function IsoDirections:fromAngleActual(angle) end

---@public
---@param index int
---@return IsoDirections
function IsoDirections:fromIndex(index) end

---Returns an array containing the constants of this enum type, in
---
---the order they are declared.  This method may be used to iterate
---
---over the constants as follows:
---
---
---
---for (IsoDirections c : IsoDirections.values())
---
---    System.out.println(c);
---
---
---@public
---@return IsoDirections[] @an array containing the constants of this enum type, in the order they are declared
function IsoDirections:values() end
