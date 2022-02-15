---@class GameVersion : zombie.core.GameVersion
---@field private m_major int
---@field private m_minor int
---@field private m_suffix String
---@field private m_string String
GameVersion = {}

---@public
---@param arg0 GameVersion
---@return boolean
function GameVersion:isLessThan(arg0) end

---@public
---@return int
function GameVersion:getMajor() end

---@public
---@param arg0 GameVersion
---@return boolean
function GameVersion:isGreaterThanOrEqualTo(arg0) end

---@public
---@param arg0 GameVersion
---@return boolean
function GameVersion:isGreaterThan(arg0) end

---@public
---@param arg0 Object
---@return boolean
function GameVersion:equals(arg0) end

---@public
---@return String
function GameVersion:getSuffix() end

---@public
---@param arg0 String
---@return GameVersion
function GameVersion:parse(arg0) end

---@public
---@return int
function GameVersion:getMinor() end

---@public
---@return int
function GameVersion:getInt() end

---@public
---@param arg0 GameVersion
---@return boolean
function GameVersion:isLessThanOrEqualTo(arg0) end

---@public
---@return String
function GameVersion:toString() end
