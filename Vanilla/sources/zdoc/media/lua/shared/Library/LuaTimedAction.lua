---@class LuaTimedAction : zombie.characters.CharacterTimedActions.LuaTimedAction
---@field _table KahluaTable
---@field public statObj Object[]
LuaTimedAction = {}

---Overrides:
---
---perform in class BaseAction
---@public
---@return void
function LuaTimedAction:perform() end

---Overrides:
---
---stop in class BaseAction
---@public
---@return void
function LuaTimedAction:stop() end

---Overrides:
---
---update in class BaseAction
---@public
---@return void
function LuaTimedAction:update() end

---Overrides:
---
---start in class BaseAction
---@public
---@return void
function LuaTimedAction:start() end

---Overrides:
---
---valid in class BaseAction
---@public
---@return boolean
function LuaTimedAction:valid() end
