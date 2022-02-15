---@class PZArrayList : zombie.util.list.PZArrayList
---@field private elements Object[]
---@field private numElements int
---@field private instance PZArrayList|Unknown
PZArrayList = {}

---@public
---@return Iterator|Unknown
function PZArrayList:iterator() end

---@public
---@param arg0 Object
---@return boolean
---@overload fun(arg0:int)
function PZArrayList:remove(arg0) end

---@public
---@param arg0 int
---@return Object
function PZArrayList:remove(arg0) end

---@public
---@return ListIterator|Unknown
---@overload fun(arg0:int)
function PZArrayList:listIterator() end

---@public
---@param arg0 int
---@return ListIterator|Unknown
function PZArrayList:listIterator(arg0) end

---@public
---@return Object[]
function PZArrayList:getElements() end

---@public
---@return void
function PZArrayList:clear() end

---@public
---@param arg0 Object
---@return boolean
---@overload fun(arg0:int, arg1:Object)
function PZArrayList:add(arg0) end

---@public
---@param arg0 int
---@param arg1 Object
---@return void
function PZArrayList:add(arg0, arg1) end

---@public
---@param arg0 Object
---@return boolean
function PZArrayList:contains(arg0) end

---@public
---@return int
function PZArrayList:size() end

---@public
---@param arg0 Object
---@return int
function PZArrayList:indexOf(arg0) end

---@public
---@return AbstractList|Unknown
function PZArrayList:emptyList() end

---@public
---@return String
function PZArrayList:toString() end

---@public
---@param arg0 int
---@param arg1 Object
---@return Object
function PZArrayList:set(arg0, arg1) end

---@public
---@return boolean
function PZArrayList:isEmpty() end

---@public
---@param arg0 int
---@return Object
function PZArrayList:get(arg0) end
