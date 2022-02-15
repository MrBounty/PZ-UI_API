---@class WorldMapEditorState : zombie.worldMap.editor.WorldMapEditorState
---@field public instance WorldMapEditorState
---@field private m_luaEnv EditVehicleState.LuaEnvironment
---@field private bExit boolean
---@field private m_gameUI ArrayList|Unknown
---@field private m_selfUI ArrayList|Unknown
---@field private m_bSuspendUI boolean
---@field private m_table KahluaTable
WorldMapEditorState = {}

---@private
---@return void
function WorldMapEditorState:renderScene() end

---@public
---@return void
function WorldMapEditorState:yield() end

---@public
---@return WorldMapEditorState
function WorldMapEditorState:checkInstance() end

---@public
---@return void
function WorldMapEditorState:save() end

---@public
---@return GameStateMachine.StateAction
function WorldMapEditorState:update() end

---@private
---@return void
function WorldMapEditorState:saveGameUI() end

---@private
---@return void
function WorldMapEditorState:renderUI() end

---@public
---@return void
function WorldMapEditorState:reenter() end

---@public
---@return void
function WorldMapEditorState:exit() end

---@public
---@param arg0 String
---@return Object
function WorldMapEditorState:fromLua0(arg0) end

---@public
---@return void
function WorldMapEditorState:enter() end

---@public
---@param arg0 KahluaTable
---@return void
function WorldMapEditorState:setTable(arg0) end

---@private
---@return void
function WorldMapEditorState:restoreGameUI() end

---@public
---@return void
function WorldMapEditorState:render() end

---@private
---@return void
function WorldMapEditorState:updateScene() end

---@public
---@return void
function WorldMapEditorState:load() end
