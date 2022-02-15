---@class ArrayList : java.util.ArrayList
---@field private serialVersionUID long
---@field private DEFAULT_CAPACITY int
---@field private EMPTY_ELEMENTDATA Object[]
---@field private DEFAULTCAPACITY_EMPTY_ELEMENTDATA Object[]
---@field elementData Object[]
---@field private size int
ArrayList = {}

---@private
---@param arg0 ObjectOutputStream
---@return void
function ArrayList:writeObject(arg0) end

---@public
---@param arg0 UnaryOperator|Unknown
---@return void
function ArrayList:replaceAll(arg0) end

---@param arg0 Collection|Unknown
---@param arg1 boolean
---@param arg2 int
---@param arg3 int
---@return boolean
function ArrayList:batchRemove(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 Collection|Unknown
---@return boolean
---@overload fun(arg0:int, arg1:Collection|Unknown)
function ArrayList:addAll(arg0) end

---@public
---@param arg0 int
---@param arg1 Collection|Unknown
---@return boolean
function ArrayList:addAll(arg0, arg1) end

---@public
---@return ListIterator|Unknown
---@overload fun(arg0:int)
function ArrayList:listIterator() end

---@public
---@param arg0 int
---@return ListIterator|Unknown
function ArrayList:listIterator(arg0) end

---@public
---@return Object[]
---@overload fun(arg0:Object[])
function ArrayList:toArray() end

---@public
---@param arg0 Object[]
---@return Object[]
function ArrayList:toArray(arg0) end

---@private
---@return Object[]
---@overload fun(arg0:int)
function ArrayList:grow() end

---@private
---@param arg0 int
---@return Object[]
function ArrayList:grow(arg0) end

---@private
---@param arg0 ObjectInputStream
---@return void
function ArrayList:readObject(arg0) end

---@public
---@param arg0 Object
---@return boolean
function ArrayList:contains(arg0) end

---@private
---@param arg0 int
---@return long[]
function ArrayList:nBits(arg0) end

---@public
---@param arg0 Predicate|Unknown
---@return boolean
---@overload fun(arg0:Predicate|Unknown, arg1:int, arg2:int)
function ArrayList:removeIf(arg0) end

---@param arg0 Predicate|Unknown
---@param arg1 int
---@param arg2 int
---@return boolean
function ArrayList:removeIf(arg0, arg1, arg2) end

---@public
---@param arg0 Object
---@return int
function ArrayList:lastIndexOf(arg0) end

---@public
---@param arg0 int
---@return void
function ArrayList:ensureCapacity(arg0) end

---@public
---@param arg0 Comparator|Unknown
---@return void
function ArrayList:sort(arg0) end

---@public
---@param arg0 Object
---@return int
function ArrayList:indexOf(arg0) end

---@public
---@param arg0 Object
---@return boolean
---@overload fun(arg0:int, arg1:Object)
---@overload fun(arg0:Object, arg1:Object[], arg2:int)
function ArrayList:add(arg0) end

---@public
---@param arg0 int
---@param arg1 Object
---@return void
function ArrayList:add(arg0, arg1) end

---@private
---@param arg0 Object
---@param arg1 Object[]
---@param arg2 int
---@return void
function ArrayList:add(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@return String
---@overload fun(arg0:int, arg1:int)
function ArrayList:outOfBoundsMsg(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@return String
function ArrayList:outOfBoundsMsg(arg0, arg1) end

---@private
---@param arg0 Object[]
---@param arg1 int
---@return void
function ArrayList:fastRemove(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 Object
---@return Object
function ArrayList:set(arg0, arg1) end

---@private
---@param arg0 long[]
---@param arg1 int
---@return void
function ArrayList:setBit(arg0, arg1) end

---@private
---@param arg0 int
---@return void
function ArrayList:checkForComodification(arg0) end

---@public
---@return int
function ArrayList:size() end

---@public
---@return void
function ArrayList:clear() end

---@public
---@return void
function ArrayList:trimToSize() end

---@param arg0 List|Unknown
---@param arg1 int
---@param arg2 int
---@return boolean
function ArrayList:equalsRange(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@return void
function ArrayList:rangeCheckForAdd(arg0) end

---@param arg0 int
---@return Object
function ArrayList:elementData(arg0) end

---@public
---@return boolean
function ArrayList:isEmpty() end

---@public
---@return Iterator|Unknown
function ArrayList:iterator() end

---@param arg0 Object
---@param arg1 int
---@param arg2 int
---@return int
function ArrayList:indexOfRange(arg0, arg1, arg2) end

---@param arg0 Object
---@param arg1 int
---@param arg2 int
---@return int
function ArrayList:lastIndexOfRange(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@return Object
---@overload fun(arg0:Object)
function ArrayList:remove(arg0) end

---@public
---@param arg0 Object
---@return boolean
function ArrayList:remove(arg0) end

---@private
---@param arg0 UnaryOperator|Unknown
---@param arg1 int
---@param arg2 int
---@return void
function ArrayList:replaceAllRange(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@return Object
function ArrayList:get(arg0) end

---@public
---@param arg0 Collection|Unknown
---@return boolean
function ArrayList:removeAll(arg0) end

---@param arg0 int
---@param arg1 int
---@return int
function ArrayList:hashCodeRange(arg0, arg1) end

---@protected
---@param arg0 int
---@param arg1 int
---@return void
function ArrayList:removeRange(arg0, arg1) end

---@public
---@return int
function ArrayList:hashCode() end

---@public
---@param arg0 Collection|Unknown
---@return boolean
function ArrayList:retainAll(arg0) end

---@public
---@param arg0 Consumer|Unknown
---@return void
function ArrayList:forEach(arg0) end

---@private
---@param arg0 ArrayList|Unknown
---@return boolean
function ArrayList:equalsArrayList(arg0) end

---@private
---@param arg0 Object[]
---@param arg1 int
---@param arg2 int
---@return void
function ArrayList:shiftTailOverGap(arg0, arg1, arg2) end

---@private
---@param arg0 long[]
---@param arg1 int
---@return boolean
function ArrayList:isClear(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return List|Unknown
function ArrayList:subList(arg0, arg1) end

---@public
---@param arg0 Object
---@return boolean
function ArrayList:equals(arg0) end

---@param arg0 Object[]
---@param arg1 int
---@return Object
function ArrayList:elementAt(arg0, arg1) end

---@return void
function ArrayList:checkInvariants() end

---@public
---@return Object
function ArrayList:clone() end

---@public
---@return Spliterator|Unknown
function ArrayList:spliterator() end
