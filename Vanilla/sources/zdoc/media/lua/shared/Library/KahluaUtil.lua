---@class KahluaUtil : se.krka.kahlua.vm.KahluaUtil
---@field private WORKER_THREAD_KEY Object
---@field private TYPE_NIL String
---@field private TYPE_STRING String
---@field private TYPE_NUMBER String
---@field private TYPE_BOOLEAN String
---@field private TYPE_FUNCTION String
---@field private TYPE_TABLE String
---@field private TYPE_COROUTINE String
---@field private TYPE_USERDATA String
KahluaUtil = {}

---@public
---@param o Object
---@param n int
---@param type String
---@param _function String
---@return void
function KahluaUtil:assertArgNotNull(o, n, type, _function) end

---@public
---@param callFrame LuaCallFrame
---@param n int
---@param _function String
---@return Double
function KahluaUtil:getNumberArg(callFrame, n, _function) end

---@public
---@param o Object
---@return String
function KahluaUtil:rawTostring2(o) end

---@public
---@param d double
---@return Double
---@overload fun(d:long)
function KahluaUtil:toDouble(d) end

---@public
---@param d long
---@return Double
function KahluaUtil:toDouble(d) end

---@public
---@param callFrame LuaCallFrame
---@param n int
---@return String
function KahluaUtil:getOptionalStringArg(callFrame, n) end

---@public
---@param s String
---@return Double
---@overload fun(s:String, radix:int)
function KahluaUtil:tonumber(s) end

---@public
---@param s String
---@param radix int
---@return Double
function KahluaUtil:tonumber(s, radix) end

---@public
---@param msg String
---@return void
---@overload fun(arg0:int, arg1:String, arg2:String, arg3:String)
function KahluaUtil:fail(msg) end

---@private
---@param arg0 int
---@param arg1 String
---@param arg2 String
---@param arg3 String
---@return void
function KahluaUtil:fail(arg0, arg1, arg2, arg3) end

---@public
---@param b boolean
---@param msg String
---@return void
function KahluaUtil:luaAssert(b, msg) end

---@public
---@param o Object
---@return boolean
function KahluaUtil:boolEval(o) end

---@public
---@param callFrame LuaCallFrame
---@param n int
---@return Double
function KahluaUtil:getOptionalNumberArg(callFrame, n) end

---@public
---@param name String
---@param environment KahluaTable
---@return LuaClosure
function KahluaUtil:loadByteCodeFromResource(name, environment) end

---@public
---@param platform Platform
---@param env KahluaTable
---@return KahluaThread
function KahluaUtil:getWorkerThread(platform, env) end

---@public
---@param o Object
---@param _thread KahluaThread
---@return String
function KahluaUtil:tostring(o, _thread) end

---@public
---@param vDouble double
---@return boolean
function KahluaUtil:isNegative(vDouble) end

---@public
---@param platform Platform
---@param env KahluaTable
---@param name String
---@return KahluaTable
function KahluaUtil:getOrCreateTable(platform, env, name) end

---@public
---@param kahluaTable KahluaTable
---@param low int
---@param high int
---@return int
function KahluaUtil:len(kahluaTable, low, high) end

---@public
---@param env KahluaTable
---@param _thread KahluaThread
---@return void
function KahluaUtil:setWorkerThread(env, _thread) end

---@public
---@param b boolean
---@return Boolean
function KahluaUtil:toBoolean(b) end

---@public
---@param callFrame LuaCallFrame
---@param n int
---@param _function String
---@return Object
function KahluaUtil:getArg(callFrame, n, _function) end

---Rounds towards even numbers
---@public
---@param x double
---@return double
function KahluaUtil:round(x) end

---@public
---@param callFrame LuaCallFrame
---@param n int
---@param _function String
---@return String
function KahluaUtil:getStringArg(callFrame, n, _function) end

---@public
---@param num Double
---@return String
function KahluaUtil:numberToString(num) end

---@public
---@param o Object
---@return Double
function KahluaUtil:rawTonumber(o) end

---@public
---@param o Object
---@return double
function KahluaUtil:fromDouble(o) end

---@public
---@param callFrame LuaCallFrame
---@param n int
---@return Object
function KahluaUtil:getOptionalArg(callFrame, n) end

---Calculates base^exponent, for non-negative exponents.
---
---0^0 is defined to be 1
---@public
---@param base long
---@param exponent int
---@return long @1 if exponent is zero or negative
function KahluaUtil:ipow(base, exponent) end

---@public
---@param env KahluaTable
---@param workerThread KahluaThread
---@param library String
---@return void
function KahluaUtil:setupLibrary(env, workerThread, library) end

---@public
---@param callFrame LuaCallFrame
---@param i int
---@param name String
---@return double
function KahluaUtil:getDoubleArg(callFrame, i, name) end

---@public
---@param o Object
---@return String
function KahluaUtil:rawTostring(o) end

---@public
---@param o Object
---@return String
function KahluaUtil:type(o) end

---@public
---@param platform Platform
---@param env KahluaTable
---@return KahluaTable
function KahluaUtil:getClassMetatables(platform, env) end
