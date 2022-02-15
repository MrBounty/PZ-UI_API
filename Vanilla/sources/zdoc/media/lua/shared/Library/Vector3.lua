---@class Vector3 : zombie.iso.Vector3
---@field public x float @The horizontal part of this vector
---@field public y float @The vertical part of this vector
---@field public z float
Vector3 = {}

---Make this vector identical to another vector
---@public
---@param other Vector3 @The Vector2 to copy
---@return Vector3
---@overload fun(x:float, y:float, z:float)
function Vector3:set(other) end

---Set the horizontal and vertical parts of this vector
---@public
---@param x float @The horizontal part
---@param y float @The vertical part
---@param z float
---@return Vector3
function Vector3:set(x, y, z) end

---Create a new vector from an AWT Point
---@public
---@param p Point @The java.awt.Point to convert
---@return JVector2 @A new Vector2 representing the Point
function Vector3:fromAwtPoint(p) end

---Returns a string representing this vector
---
---Overrides:
---
---toString in class java.lang.Object
---@public
---@return String @A String representing this vector
function Vector3:toString() end

---Convert this vector to an AWT Point
---@public
---@return Point @a java.awt.Point
function Vector3:toAwtPoint() end

---Set the direction of this vector, maintaining the length
---@public
---@param direction float @The new direction of this vector, in radians
---@return Vector3
function Vector3:setDirection(direction) end

---Add another vector to this one and return as a new vector
---@public
---@param other JVector2 @The other Vector2 to add to this one
---@return JVector2 @The result as new Vector2
function Vector3:add(other) end

---Set the direction of this vector to point to another vector, maintaining the length
---@public
---@param other JVector2 @The Vector2 to point this one at.
---@return Vector3
function Vector3:aimAt(other) end

---@public
---@return float
function Vector3:getLengthSq() end

---Set the length and direction of this vector
---@public
---@param direction float @The direction of this vector, in radians
---@param length float @The length of this vector
---@return Vector3
function Vector3:setLengthAndDirection(direction, length) end

---Create a new vector with a specified length and direction
---@public
---@param length float @The length of the new vector
---@param direction float @The direction of the new vector, in radians
---@return JVector2
function Vector3:fromLengthDirection(length, direction) end

---@public
---@param arg0 Vector3
---@return float
function Vector3:dot3d(arg0) end

---Add another vector to this one and store the result in this one
---@public
---@param other JVector2 @The other Vector2 to add to this one
---@return Vector3
---@overload fun(arg0:Vector3)
function Vector3:addToThis(other) end

---@public
---@param arg0 Vector3
---@return Vector3
function Vector3:addToThis(arg0) end

---Calculate the distance between this point and another
---@public
---@param other JVector2 @The second point as vector
---@return float @The distance between them
function Vector3:distanceTo(other) end

---Set the length of this vector, maintaining the direction
---@public
---@param length float @The length of this vector
---@return Vector3
function Vector3:setLength(length) end

---@public
---@param arg0 float
---@return void
function Vector3:rotatey(arg0) end

---Convert this vector to an AWT Dimension
---@public
---@return Dimension @a java.awt.Dimension
function Vector3:toAwtDimension() end

---Calculate the angle between this point and another
---@public
---@param other JVector2 @The second point as vector
---@return float @The angle between them, in radians
function Vector3:angleTo(other) end

---@public
---@param arg0 Vector3
---@param arg1 Vector3
---@return Vector3
---@overload fun(arg0:Vector3, arg1:Vector3, arg2:Vector3)
function Vector3:sub(arg0, arg1) end

---@public
---@param arg0 Vector3
---@param arg1 Vector3
---@param arg2 Vector3
---@return Vector3
function Vector3:sub(arg0, arg1, arg2) end

---Clone this vector
---
---Overrides:
---
---clone in class java.lang.Object
---@public
---@return Vector3
function Vector3:clone() end

---See if this vector is equal to another
---
---Overrides:
---
---equals in class java.lang.Object
---@public
---@param other Object @A Vector2 to compare this one to
---@return boolean @true if other is a Vector2 equal to this one
function Vector3:equals(other) end

---@public
---@return void
function Vector3:normalize() end

---@public
---@param other JVector2
---@return float
---@overload fun(x:float, y:float, tx:float, ty:float)
function Vector3:dot(other) end

---@public
---@param x float
---@param y float
---@param tx float
---@param ty float
---@return float
function Vector3:dot(x, y, tx, ty) end

---get the length of this vector
---@public
---@return float @The length of this vector
function Vector3:getLength() end

---@public
---@param arg0 float
---@return Vector3
function Vector3:div(arg0) end

---get the direction in which this vector is pointing
---
---Note: if the length of this vector is 0, then the direction will also be 0
---@public
---@return float @The direction in which this vector is pointing in radians
function Vector3:getDirection() end

---@public
---@param rad float
---@return void
function Vector3:rotate(rad) end
