---@class LinkedList : java.util.LinkedList
---@field size int
---@field first LinkedList.Node|Unknown
---@field last LinkedList.Node|Unknown
---@field private serialVersionUID long
LinkedList = {}

---@param arg0 LinkedList.Node|Unknown
---@return Object
function LinkedList:unlink(arg0) end

---@private
---@param arg0 ObjectOutputStream
---@return void
function LinkedList:writeObject(arg0) end

---@private
---@param arg0 LinkedList.Node|Unknown
---@return Object
function LinkedList:unlinkFirst(arg0) end

---@public
---@return Object
function LinkedList:pollFirst() end

---@public
---@param arg0 Object
---@return boolean
function LinkedList:offerFirst(arg0) end

---@public
---@return Object
---@overload fun(arg0:int)
---@overload fun(arg0:Object)
function LinkedList:remove() end

---@public
---@param arg0 int
---@return Object
function LinkedList:remove(arg0) end

---@public
---@param arg0 Object
---@return boolean
function LinkedList:remove(arg0) end

---@public
---@return Object
function LinkedList:poll() end

---@public
---@return Object[]
---@overload fun(arg0:Object[])
function LinkedList:toArray() end

---@public
---@param arg0 Object[]
---@return Object[]
function LinkedList:toArray(arg0) end

---@private
---@param arg0 Object
---@return void
function LinkedList:linkFirst(arg0) end

---@public
---@param arg0 Collection|Unknown
---@return boolean
---@overload fun(arg0:int, arg1:Collection|Unknown)
function LinkedList:addAll(arg0) end

---@public
---@param arg0 int
---@param arg1 Collection|Unknown
---@return boolean
function LinkedList:addAll(arg0, arg1) end

---@public
---@param arg0 Object
---@return boolean
function LinkedList:contains(arg0) end

---@private
---@param arg0 ObjectInputStream
---@return void
function LinkedList:readObject(arg0) end

---@private
---@param arg0 LinkedList.Node|Unknown
---@return Object
function LinkedList:unlinkLast(arg0) end

---@public
---@param arg0 Object
---@return int
function LinkedList:lastIndexOf(arg0) end

---@public
---@param arg0 Object
---@return int
function LinkedList:indexOf(arg0) end

---@public
---@return Object
function LinkedList:getFirst() end

---@public
---@param arg0 Object
---@return boolean
---@overload fun(arg0:int, arg1:Object)
function LinkedList:add(arg0) end

---@public
---@param arg0 int
---@param arg1 Object
---@return void
function LinkedList:add(arg0, arg1) end

---@private
---@param arg0 int
---@return String
function LinkedList:outOfBoundsMsg(arg0) end

---@public
---@return Object
function LinkedList:pop() end

---@public
---@param arg0 int
---@param arg1 Object
---@return Object
function LinkedList:set(arg0, arg1) end

---@public
---@param arg0 Object
---@return boolean
function LinkedList:removeLastOccurrence(arg0) end

---@private
---@return LinkedList|Unknown
function LinkedList:superClone() end

---@private
---@param arg0 int
---@return void
function LinkedList:checkPositionIndex(arg0) end

---@public
---@return int
function LinkedList:size() end

---@public
---@return void
function LinkedList:clear() end

---@public
---@param arg0 Object
---@return boolean
function LinkedList:offerLast(arg0) end

---@public
---@return Object
function LinkedList:peekFirst() end

---@public
---@param arg0 Object
---@return boolean
function LinkedList:removeFirstOccurrence(arg0) end

---@public
---@param arg0 Object
---@return boolean
function LinkedList:offer(arg0) end

---@public
---@return Object
function LinkedList:removeFirst() end

---@public
---@return Object
function LinkedList:removeLast() end

---@public
---@return Iterator|Unknown
function LinkedList:descendingIterator() end

---@public
---@param arg0 Object
---@return void
function LinkedList:addFirst(arg0) end

---@public
---@param arg0 Object
---@return void
function LinkedList:addLast(arg0) end

---@public
---@return Object
function LinkedList:getLast() end

---@public
---@return Object
function LinkedList:peekLast() end

---@public
---@return Object
function LinkedList:element() end

---@public
---@param arg0 int
---@return Object
function LinkedList:get(arg0) end

---@private
---@param arg0 int
---@return void
function LinkedList:checkElementIndex(arg0) end

---@public
---@param arg0 Object
---@return void
function LinkedList:push(arg0) end

---@public
---@return Object
function LinkedList:peek() end

---@public
---@return Object
function LinkedList:pollLast() end

---@private
---@param arg0 int
---@return boolean
function LinkedList:isElementIndex(arg0) end

---@public
---@param arg0 int
---@return ListIterator|Unknown
function LinkedList:listIterator(arg0) end

---@param arg0 int
---@return LinkedList.Node|Unknown
function LinkedList:node(arg0) end

---@param arg0 Object
---@param arg1 LinkedList.Node|Unknown
---@return void
function LinkedList:linkBefore(arg0, arg1) end

---@public
---@return Object
function LinkedList:clone() end

---@public
---@return Spliterator|Unknown
function LinkedList:spliterator() end

---@private
---@param arg0 int
---@return boolean
function LinkedList:isPositionIndex(arg0) end

---@param arg0 Object
---@return void
function LinkedList:linkLast(arg0) end
