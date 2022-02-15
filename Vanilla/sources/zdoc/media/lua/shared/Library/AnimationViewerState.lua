---@class AnimationViewerState : zombie.gameStates.AnimationViewerState
---@field public instance AnimationViewerState
---@field private m_luaEnv EditVehicleState.LuaEnvironment
---@field private bExit boolean
---@field private m_gameUI ArrayList|Unknown
---@field private m_selfUI ArrayList|Unknown
---@field private m_bSuspendUI boolean
---@field private m_table KahluaTable
---@field private m_clipNames ArrayList|Unknown
---@field private VERSION int
---@field private options ArrayList|Unknown
---@field private DrawGrid AnimationViewerState.BooleanDebugOption
---@field private Isometric AnimationViewerState.BooleanDebugOption
---@field private UseDeferredMovement AnimationViewerState.BooleanDebugOption
AnimationViewerState = {}

---@private
---@return void
function AnimationViewerState:updateScene() end

---@private
---@return void
function AnimationViewerState:renderUI() end

---@public
---@return void
function AnimationViewerState:load() end

---@public
---@return void
function AnimationViewerState:save() end

---@public
---@return void
function AnimationViewerState:exit() end

---@public
---@return AnimationViewerState
function AnimationViewerState:checkInstance() end

---@public
---@param arg0 String
---@param arg1 Object
---@return Object
function AnimationViewerState:fromLua1(arg0, arg1) end

---@public
---@return void
function AnimationViewerState:render() end

---@public
---@param arg0 String
---@return boolean
function AnimationViewerState:getBoolean(arg0) end

---@public
---@param arg0 String
---@return Object
function AnimationViewerState:fromLua0(arg0) end

---@public
---@return void
function AnimationViewerState:enter() end

---@private
---@return void
function AnimationViewerState:renderScene() end

---@public
---@return void
function AnimationViewerState:yield() end

---@private
---@return void
function AnimationViewerState:restoreGameUI() end

---@public
---@return void
function AnimationViewerState:reenter() end

---@private
---@return void
function AnimationViewerState:saveGameUI() end

---@public
---@return GameStateMachine.StateAction
function AnimationViewerState:update() end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function AnimationViewerState:setBoolean(arg0, arg1) end

---@public
---@param arg0 String
---@return ConfigOption
function AnimationViewerState:getOptionByName(arg0) end

---@public
---@return int
function AnimationViewerState:getOptionCount() end

---@public
---@param arg0 int
---@return ConfigOption
function AnimationViewerState:getOptionByIndex(arg0) end

---@public
---@param arg0 KahluaTable
---@return void
function AnimationViewerState:setTable(arg0) end
