---@class EditVehicleState : zombie.vehicles.EditVehicleState
---@field public instance EditVehicleState
---@field private m_luaEnv EditVehicleState.LuaEnvironment
---@field private bExit boolean
---@field private m_initialScript String
---@field private m_gameUI ArrayList|Unknown
---@field private m_selfUI ArrayList|Unknown
---@field private m_bSuspendUI boolean
---@field private m_table KahluaTable
EditVehicleState = {}

---@private
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return void
function EditVehicleState:writeScript(arg0, arg1) end

---@public
---@return void
function EditVehicleState:reenter() end

---@public
---@param arg0 KahluaTable
---@return void
function EditVehicleState:setTable(arg0) end

---@public
---@param arg0 String
---@return void
function EditVehicleState:setScript(arg0) end

---@private
---@return void
function EditVehicleState:saveGameUI() end

---@public
---@param arg0 String
---@return Object
function EditVehicleState:fromLua0(arg0) end

---@public
---@return void
function EditVehicleState:exit() end

---@public
---@return void
function EditVehicleState:enter() end

---@public
---@return GameStateMachine.StateAction
function EditVehicleState:update() end

---@public
---@param arg0 String
---@param arg1 Object
---@return Object
function EditVehicleState:fromLua1(arg0, arg1) end

---@public
---@return void
function EditVehicleState:render() end

---@private
---@return void
function EditVehicleState:renderUI() end

---@private
---@param arg0 VehicleScript
---@param arg1 String
---@return String
function EditVehicleState:vehicleScriptToText(arg0, arg1) end

---@public
---@return EditVehicleState
function EditVehicleState:checkInstance() end

---@private
---@return void
function EditVehicleState:restoreGameUI() end

---@private
---@return void
function EditVehicleState:updateScene() end

---@private
---@return void
function EditVehicleState:renderScene() end

---@private
---@param arg0 String
---@return ArrayList|Unknown
function EditVehicleState:readScript(arg0) end

---@public
---@return void
function EditVehicleState:yield() end

---@private
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@param arg2 VehicleScript
---@return void
function EditVehicleState:updateScript(arg0, arg1, arg2) end
