---@class DebugGlobalObjectState : zombie.gameStates.DebugGlobalObjectState
---@field public instance DebugGlobalObjectState
---@field private m_luaEnv EditVehicleState.LuaEnvironment
---@field private bExit boolean
---@field private m_gameUI ArrayList|Unknown
---@field private m_selfUI ArrayList|Unknown
---@field private m_bSuspendUI boolean
---@field private m_table KahluaTable
---@field private m_playerIndex int
---@field private m_z int
---@field private gridX int
---@field private gridY int
---@field private FONT UIFont
DebugGlobalObjectState = {}

---@public
---@return void
function DebugGlobalObjectState:enter() end

---@private
---@return void
function DebugGlobalObjectState:updateCursor() end

---@public
---@param arg0 String
---@return Object
function DebugGlobalObjectState:fromLua0(arg0) end

---@public
---@return GameStateMachine.StateAction
function DebugGlobalObjectState:updateScene() end

---@public
---@return void
function DebugGlobalObjectState:renderScene() end

---@private
---@return void
function DebugGlobalObjectState:restoreGameUI() end

---@public
---@param arg0 KahluaTable
---@return void
function DebugGlobalObjectState:setTable(arg0) end

---@private
---@return void
function DebugGlobalObjectState:renderUI() end

---@public
---@return void
function DebugGlobalObjectState:exit() end

---@public
---@return void
function DebugGlobalObjectState:reenter() end

---@private
---@return void
function DebugGlobalObjectState:saveGameUI() end

---@public
---@return GameStateMachine.StateAction
function DebugGlobalObjectState:update() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@param arg9 float
---@param arg10 int
---@return void
function DebugGlobalObjectState:DrawIsoLine(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10) end

---@public
---@return void
function DebugGlobalObjectState:render() end

---@public
---@return void
function DebugGlobalObjectState:yield() end

---@public
---@param arg0 String
---@param arg1 Object
---@return Object
function DebugGlobalObjectState:fromLua1(arg0, arg1) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@param arg9 int
---@return void
function DebugGlobalObjectState:DrawIsoRect(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@return Object
function DebugGlobalObjectState:fromLua2(arg0, arg1, arg2) end
