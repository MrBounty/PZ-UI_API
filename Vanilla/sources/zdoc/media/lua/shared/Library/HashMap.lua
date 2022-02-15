---@class HashMap : java.util.HashMap
---@field private serialVersionUID long
---@field DEFAULT_INITIAL_CAPACITY int
---@field MAXIMUM_CAPACITY int
---@field DEFAULT_LOAD_FACTOR float
---@field TREEIFY_THRESHOLD int
---@field UNTREEIFY_THRESHOLD int
---@field MIN_TREEIFY_CAPACITY int
---@field _table HashMap.Node[]
---@field entrySet Set|Unknown
---@field size int
---@field modCount int
---@field threshold int
---@field loadFactor float
HashMap = {}

---@public
---@param arg0 Object
---@return boolean
function HashMap:containsKey(arg0) end

---@public
---@param arg0 Object
---@param arg1 BiFunction|Unknown|Unknown|Unknown
---@return Object
function HashMap:computeIfPresent(arg0, arg1) end

---@public
---@param arg0 Object
---@param arg1 Object
---@return Object
function HashMap:getOrDefault(arg0, arg1) end

---@public
---@param arg0 Object
---@param arg1 BiFunction|Unknown|Unknown|Unknown
---@return Object
function HashMap:compute(arg0, arg1) end

---@return void
function HashMap:reinitialize() end

---@public
---@param arg0 Object
---@return Object
---@overload fun(arg0:Object, arg1:Object)
function HashMap:remove(arg0) end

---@public
---@param arg0 Object
---@param arg1 Object
---@return boolean
function HashMap:remove(arg0, arg1) end

---@param arg0 int
---@param arg1 Object
---@param arg2 Object
---@param arg3 HashMap.Node|Unknown|Unknown
---@return HashMap.Node|Unknown|Unknown
function HashMap:newNode(arg0, arg1, arg2, arg3) end

---@public
---@return int
function HashMap:size() end

---@public
---@return boolean
function HashMap:isEmpty() end

---@param arg0 int
---@param arg1 Object
---@param arg2 Object
---@param arg3 boolean
---@param arg4 boolean
---@return HashMap.Node|Unknown|Unknown
function HashMap:removeNode(arg0, arg1, arg2, arg3, arg4) end

---@param arg0 HashMap.Node[]
---@param arg1 int
---@return void
function HashMap:treeifyBin(arg0, arg1) end

---@param arg0 Object[]
---@return Object[]
function HashMap:keysToArray(arg0) end

---@param arg0 Object
---@return int
function HashMap:hash(arg0) end

---@param arg0 Class|Unknown
---@param arg1 Object
---@param arg2 Object
---@return int
function HashMap:compareComparables(arg0, arg1, arg2) end

---@param arg0 boolean
---@return void
function HashMap:afterNodeInsertion(arg0) end

---@public
---@param arg0 BiFunction|Unknown|Unknown|Unknown
---@return void
function HashMap:replaceAll(arg0) end

---@public
---@return Set|Unknown
function HashMap:keySet() end

---@public
---@param arg0 Object
---@param arg1 Object
---@return Object
function HashMap:put(arg0, arg1) end

---@param arg0 HashMap.Node|Unknown|Unknown
---@param arg1 HashMap.Node|Unknown|Unknown
---@return HashMap.TreeNode|Unknown|Unknown
function HashMap:replacementTreeNode(arg0, arg1) end

---@public
---@param arg0 Object
---@param arg1 Object
---@param arg2 BiFunction|Unknown|Unknown|Unknown
---@return Object
function HashMap:merge(arg0, arg1, arg2) end

---@public
---@return void
function HashMap:clear() end

---@public
---@param arg0 Object
---@param arg1 Object
---@return Object
function HashMap:putIfAbsent(arg0, arg1) end

---@param arg0 HashMap.Node|Unknown|Unknown
---@return void
function HashMap:afterNodeAccess(arg0) end

---@param arg0 Object[]
---@return Object[]
function HashMap:valuesToArray(arg0) end

---@public
---@return Collection|Unknown
function HashMap:values() end

---@public
---@param arg0 Object
---@return Object
function HashMap:get(arg0) end

---@param arg0 ObjectOutputStream
---@return void
function HashMap:internalWriteEntries(arg0) end

---@param arg0 Object[]
---@return Object[]
function HashMap:prepareArray(arg0) end

---@public
---@return Set|Unknown
function HashMap:entrySet() end

---@param arg0 Object
---@return HashMap.Node|Unknown|Unknown
function HashMap:getNode(arg0) end

---@param arg0 Object
---@return Class|Unknown
function HashMap:comparableClassFor(arg0) end

---@public
---@param arg0 Object
---@return boolean
function HashMap:containsValue(arg0) end

---@private
---@param arg0 ObjectInputStream
---@return void
function HashMap:readObject(arg0) end

---@param arg0 int
---@param arg1 Object
---@param arg2 Object
---@param arg3 boolean
---@param arg4 boolean
---@return Object
function HashMap:putVal(arg0, arg1, arg2, arg3, arg4) end

---@return int
function HashMap:capacity() end

---@param arg0 HashMap.Node|Unknown|Unknown
---@param arg1 HashMap.Node|Unknown|Unknown
---@return HashMap.Node|Unknown|Unknown
function HashMap:replacementNode(arg0, arg1) end

---@param arg0 HashMap.Node|Unknown|Unknown
---@return void
function HashMap:afterNodeRemoval(arg0) end

---@return float
function HashMap:loadFactor() end

---@public
---@param arg0 Object
---@param arg1 Function|Unknown|Unknown
---@return Object
function HashMap:computeIfAbsent(arg0, arg1) end

---@param arg0 int
---@return int
function HashMap:tableSizeFor(arg0) end

---@public
---@param arg0 Object
---@param arg1 Object
---@return Object
---@overload fun(arg0:Object, arg1:Object, arg2:Object)
function HashMap:replace(arg0, arg1) end

---@public
---@param arg0 Object
---@param arg1 Object
---@param arg2 Object
---@return boolean
function HashMap:replace(arg0, arg1, arg2) end

---@public
---@return Object
function HashMap:clone() end

---@param arg0 int
---@param arg1 Object
---@param arg2 Object
---@param arg3 HashMap.Node|Unknown|Unknown
---@return HashMap.TreeNode|Unknown|Unknown
function HashMap:newTreeNode(arg0, arg1, arg2, arg3) end

---@param arg0 Map|Unknown|Unknown
---@param arg1 boolean
---@return void
function HashMap:putMapEntries(arg0, arg1) end

---@private
---@param arg0 ObjectOutputStream
---@return void
function HashMap:writeObject(arg0) end

---@public
---@param arg0 Map|Unknown|Unknown
---@return void
function HashMap:putAll(arg0) end

---@return HashMap.Node[]
function HashMap:resize() end

---@public
---@param arg0 BiConsumer|Unknown|Unknown
---@return void
function HashMap:forEach(arg0) end
