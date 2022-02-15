---@class JVector2 : zombie.iso.Vector2
---@field public x float @The horizontal part of this vector
---@field public y float @The vertical part of this vector
JVector2 = {}

---Set the direction of this vector, maintaining the length
---@public
---@param direction float @The new direction of this vector, in radians
---@return JVector2
function JVector2:setDirection(direction) end

---Convert this vector to an AWT Point
---@public
---@return Point @a java.awt.Point
function JVector2:toAwtPoint() end

---Calculate the angle between this point and another
---@public
---@param other JVector2 @The second point as vector
---@return float @The angle between them, in radians
function JVector2:angleTo(other) end

---@public
---@return float @the x
function JVector2:getX() end

---Set the direction of this vector to point to another vector, maintaining the length
---@public
---@param other JVector2 @The Vector2 to point this one at.
---@return JVector2
function JVector2:aimAt(other) end

---Calculate the distance between this point and another
---@public
---@param other JVector2 @The second point as vector
---@return float @The distance between them
function JVector2:distanceTo(other) end

---@public
---@return void
function JVector2:tangent() end

---Make this vector identical to another vector
---@public
---@param other JVector2 @The Vector2 to copy
---@return JVector2
---@overload fun(x:float, y:float)
function JVector2:set(other) end

---Set the horizontal and vertical parts of this vector
---@public
---@param x float @The horizontal part
---@param y float @The vertical part
---@return JVector2
function JVector2:set(x, y) end

---@public
---@param arg0 JVector2
---@param arg1 JVector2
---@param arg2 float
---@param arg3 JVector2
---@return JVector2
function JVector2:addScaled(arg0, arg1, arg2, arg3) end

---@public
---@return float
function JVector2:normalize() end

---Create a new vector from an AWT Point
---@public
---@param p Point @The java.awt.Point to convert
---@return JVector2 @A new Vector2 representing the Point
function JVector2:fromAwtPoint(p) end

---Returns a string representing this vector
---
---Overrides:
---
---toString in class java.lang.Object
---@public
---@return String @A String representing this vector
function JVector2:toString() end

---Create a new vector with a specified length and direction
---@public
---@param length float @The length of the new vector
---@param direction float @The direction of the new vector, in radians
---@return JVector2
function JVector2:fromLengthDirection(length, direction) end

---Add another vector to this one and return as a new vector
---@public
---@param other JVector2 @The other Vector2 to add to this one
---@return JVector2 @The result as new Vector2
function JVector2:add(other) end

---Set the length of this vector, maintaining the direction
---@public
---@param length float @The length of this vector
---@return JVector2
function JVector2:setLength(length) end

---get the direction in which this vector is pointing
---
---Note: if the length of this vector is 0, then the direction will also be 0
---@public
---@return float
---@overload fun(arg0:float, arg1:float)
function JVector2:getDirection() end

---@public
---@param arg0 float
---@param arg1 float
---@return float
function JVector2:getDirection(arg0, arg1) end

---Convert this vector to an AWT Dimension
---@public
---@return Dimension @a java.awt.Dimension
function JVector2:toAwtDimension() end

---@public
---@return float @the y
function JVector2:getY() end

---@public
---@return float
function JVector2:getLengthSquared() end

---@public
---@param other JVector2
---@return float
---@overload fun(x:float, y:float, tx:float, ty:float)
function JVector2:dot(other) end

---@public
---@param x float
---@param y float
---@param tx float
---@param ty float
---@return float
function JVector2:dot(x, y, tx, ty) end

---@public
---@param x float @the x to set
---@return void
function JVector2:setX(x) end

---Set the length and direction of this vector
---@public
---@param direction float @The direction of this vector, in radians
---@param length float @The length of this vector
---@return JVector2
function JVector2:setLengthAndDirection(direction, length) end

---See if this vector is equal to another
---
---Overrides:
---
---equals in class java.lang.Object
---@public
---@param other Object @A Vector2 to compare this one to
---@return boolean @true if other is a Vector2 equal to this one
function JVector2:equals(other) end

---Clone this vector
---
---Overrides:
---
---clone in class java.lang.Object
---@public
---@return JVector2
function JVector2:clone() end

---@public
---@param arg0 float
---@return void
---@overload fun(arg0:JVector2, arg1:float)
function JVector2:scale(arg0) end

---@public
---@param arg0 JVector2
---@param arg1 float
---@return JVector2
function JVector2:scale(arg0, arg1) end

---@public
---@param rad float
---@return void
function JVector2:rotate(rad) end

---@public
---@return float
function JVector2:getDirectionNeg() end

---get the length of this vector
---@public
---@return float @The length of this vector
function JVector2:getLength() end

---@public
---@param y float @the y to set
---@return void
function JVector2:setY(y) end
