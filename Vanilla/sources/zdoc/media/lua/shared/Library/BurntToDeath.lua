---@class BurntToDeath : zombie.ai.states.BurntToDeath
---@field private _instance BurntToDeath
BurntToDeath = {}

---Overrides:
---
---exit in class State
---@public
---@param owner IsoGameCharacter
---@return void
function BurntToDeath:exit(owner) end

---Overrides:
---
---enter in class State
---@public
---@param owner IsoGameCharacter
---@return void
function BurntToDeath:enter(owner) end

---Overrides:
---
---execute in class State
---@public
---@param owner IsoGameCharacter
---@return void
function BurntToDeath:execute(owner) end

---@public
---@return BurntToDeath
function BurntToDeath:instance() end
