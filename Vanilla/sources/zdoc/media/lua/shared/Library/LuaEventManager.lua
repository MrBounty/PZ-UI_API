---@class LuaEventManager : zombie.Lua.LuaEventManager
---@field public OnTickCallbacks ArrayList|LuaClosure
---@field a1 Object[][]
---@field a2 Object[][]
---@field a3 Object[][]
---@field a4 Object[][]
---@field a5 Object[][]
---@field a6 Object[][]
---@field a1index int
---@field a2index int
---@field a3index int
---@field a4index int
---@field a5index int
---@field a6index int
---@field private EventList ArrayList|Unknown
---@field private EventMap HashMap|Unknown|Unknown
LuaEventManager = {}

---@public
---@param platform Platform
---@param environment KahluaTable
---@return void
function LuaEventManager:register(platform, environment) end

---@public
---@param event String
---@param param1 Object
---@return void
---@overload fun(event:String, param1:Object, param2:Object)
---@overload fun(event:String, param1:Object, param2:Object, param3:Object)
---@overload fun(event:String, param1:Object, param2:Object, param3:Object, param4:Object)
function LuaEventManager:triggerEventGarbage(event, param1) end

---@public
---@param event String
---@param param1 Object
---@param param2 Object
---@return void
function LuaEventManager:triggerEventGarbage(event, param1, param2) end

---@public
---@param event String
---@param param1 Object
---@param param2 Object
---@param param3 Object
---@return void
function LuaEventManager:triggerEventGarbage(event, param1, param2, param3) end

---@public
---@param event String
---@param param1 Object
---@param param2 Object
---@param param3 Object
---@param param4 Object
---@return void
function LuaEventManager:triggerEventGarbage(event, param1, param2, param3, param4) end

---@public
---@param event String
---@return void
---@overload fun(event:String, param1:Object)
---@overload fun(event:String, param1:Object, param2:Object)
---@overload fun(event:String, param1:Object, param2:Object, param3:Object)
---@overload fun(event:String, param1:Object, param2:Object, param3:Object, param4:Object)
---@overload fun(event:String, param1:Object, param2:Object, param3:Object, param4:Object, param5:Object)
---@overload fun(event:String, param1:Object, param2:Object, param3:Object, param4:Object, param5:Object, param6:Object)
function LuaEventManager:triggerEvent(event) end

---@public
---@param event String
---@param param1 Object
---@return void
function LuaEventManager:triggerEvent(event, param1) end

---@public
---@param event String
---@param param1 Object
---@param param2 Object
---@return void
function LuaEventManager:triggerEvent(event, param1, param2) end

---@public
---@param event String
---@param param1 Object
---@param param2 Object
---@param param3 Object
---@return void
function LuaEventManager:triggerEvent(event, param1, param2, param3) end

---@public
---@param event String
---@param param1 Object
---@param param2 Object
---@param param3 Object
---@param param4 Object
---@return void
function LuaEventManager:triggerEvent(event, param1, param2, param3, param4) end

---@public
---@param event String
---@param param1 Object
---@param param2 Object
---@param param3 Object
---@param param4 Object
---@param param5 Object
---@return void
function LuaEventManager:triggerEvent(event, param1, param2, param3, param4, param5) end

---@public
---@param event String
---@param param1 Object
---@param param2 Object
---@param param3 Object
---@param param4 Object
---@param param5 Object
---@param param6 Object
---@return void
function LuaEventManager:triggerEvent(event, param1, param2, param3, param4, param5, param6) end

---@public
---@return void
function LuaEventManager:ResetCallbacks() end

---@public
---@return void
function LuaEventManager:clear() end

---@public
---@param prototype Prototype
---@param luaClosure LuaClosure
---@return void
function LuaEventManager:reroute(prototype, luaClosure) end

---@public
---@param event String
---@param param1 Object
---@return void
function LuaEventManager:triggerEventUnique(event, param1) end

---Description copied from interface: JavaFunction
---
---This interface defines functions which the Kahlua engine can call. General contract:
---
---callFrame.get(i) = an argument (0 <= i < nArguments)
---
---Return (possibly) values to lua by calling:
---
---callFrame.push(value1);
---
---callFrame.push(value2);
---
---return 2; // number of pushed values
---
---Specified by:
---
---call in interface JavaFunction
---@public
---@param callFrame LuaCallFrame @- the frame that contains all the arguments and where all the results should be put.
---@param nArguments int @- number of function arguments
---@return int @N, number of return values. The top N objects on the stack are considered the return values.
function LuaEventManager:call(callFrame, nArguments) end

---@public
---@return void
function LuaEventManager:Reset() end

---@private
---@param arg0 String
---@return Event
function LuaEventManager:checkEvent(arg0) end

---@private
---@param arg0 LuaCallFrame
---@param arg1 int
---@return int
function LuaEventManager:OnTick(arg0, arg1) end

---@public
---@param arg0 String
---@return Event
function LuaEventManager:AddEvent(arg0) end

---@private
---@return void
function LuaEventManager:AddEvents() end
