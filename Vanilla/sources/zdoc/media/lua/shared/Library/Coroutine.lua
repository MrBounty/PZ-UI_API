---@class Coroutine : se.krka.kahlua.vm.Coroutine
---@field private platform Platform
---@field private _thread KahluaThread
---@field private parent Coroutine
---@field public environment KahluaTable
---@field public stackTrace String
---@field private liveUpvalues ArrayList|Unknown
---@field private MAX_STACK_SIZE int
---@field private INITIAL_STACK_SIZE int
---@field private MAX_CALL_FRAME_STACK_SIZE int
---@field private INITIAL_CALL_FRAME_STACK_SIZE int
---@field public objectStack Object[]
---@field private top int
---@field private callFrameStack LuaCallFrame[]
---@field private callFrameTop int
Coroutine = {}

---@public
---@return Coroutine
---@overload fun(level:int)
function Coroutine:getParent() end

---@public
---@param level int
---@return LuaCallFrame
function Coroutine:getParent(level) end

---@public
---@param newTop int
---@return void
function Coroutine:setCallFrameStackTop(newTop) end

---@public
---@return int
function Coroutine:getTop() end

---@public
---@param level int
---@return LuaCallFrame
function Coroutine:getParentNoAssert(level) end

---@public
---@return boolean
function Coroutine:isDead() end

---@public
---@return String
function Coroutine:getStatus() end

---@public
---@return void
function Coroutine:destroy() end

---@public
---@param newTop int
---@return void
function Coroutine:setTop(newTop) end

---@public
---@return void
function Coroutine:popCallFrame() end

---@private
---@param arg0 int
---@return void
function Coroutine:ensureStacksize(arg0) end

---@public
---@return LuaCallFrame
function Coroutine:currentCallFrame() end

---@public
---@return int
function Coroutine:getCallframeTop() end

---@private
---@param arg0 LuaCallFrame
---@return String
function Coroutine:getStackTrace(arg0) end

---@public
---@return KahluaThread
function Coroutine:getThread() end

---@public
---@param callFrame LuaCallFrame
---@param argsCallFrame LuaCallFrame
---@param nArguments int
---@return void
function Coroutine:yieldHelper(callFrame, argsCallFrame, nArguments) end

---@private
---@param arg0 int
---@param arg1 int
---@return void
function Coroutine:callFrameStackClear(arg0, arg1) end

---@public
---@param startIndex int
---@param destIndex int
---@param len int
---@return void
function Coroutine:stackCopy(startIndex, destIndex, len) end

---@public
---@param n int
---@return Object
function Coroutine:getObjectFromStack(n) end

---@private
---@param arg0 int
---@return void
function Coroutine:ensureCallFrameStackSize(arg0) end

---@public
---@return LuaCallFrame
function Coroutine:getParentCallframe() end

---@public
---@return int
function Coroutine:getObjectStackSize() end

---@public
---@param index int
---@return LuaCallFrame
function Coroutine:getCallFrame(index) end

---@public
---@return LuaCallFrame[]
function Coroutine:getCallframeStack() end

---@public
---@param frame LuaCallFrame
---@return void
function Coroutine:addStackTrace(frame) end

---@public
---@param parent Coroutine
---@return void
function Coroutine:resume(parent) end

---@public
---@param scanIndex int
---@return UpValue
function Coroutine:findUpvalue(scanIndex) end

---@public
---@param closure LuaClosure
---@param javaFunction JavaFunction
---@param localBase int
---@param returnBase int
---@param nArguments int
---@param fromLua boolean
---@param insideCoroutine boolean
---@return LuaCallFrame
function Coroutine:pushNewCallFrame(closure, javaFunction, localBase, returnBase, nArguments, fromLua, insideCoroutine) end

---@public
---@return Platform
function Coroutine:getPlatform() end

---@public
---@return boolean
function Coroutine:atBottom() end

---@public
---@param level int
---@param count int
---@param haltAt int
---@return String
function Coroutine:getCurrentStackTrace(level, count, haltAt) end

---@public
---@param callerFrame LuaCallFrame
---@return void
function Coroutine:cleanCallFrames(callerFrame) end

---@public
---@param closeIndex int
---@return void
function Coroutine:closeUpvalues(closeIndex) end

---@public
---@param startIndex int
---@param endIndex int
---@return void
function Coroutine:stackClear(startIndex, endIndex) end
