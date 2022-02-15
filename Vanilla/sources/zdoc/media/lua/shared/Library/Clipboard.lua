---@class Clipboard : zombie.core.Clipboard
---@field private MainThread Thread
---@field private PreviousKnownValue String
---@field private DelaySetMainThread String
Clipboard = {}

---@public
---@return void
function Clipboard:updateMainThread() end

---@public
---@return void
function Clipboard:rememberCurrentValue() end

---@public
---@param arg0 String
---@return void
function Clipboard:setClipboard(arg0) end

---@public
---@return void
function Clipboard:initMainThread() end

---@public
---@return String
function Clipboard:getClipboard() end
