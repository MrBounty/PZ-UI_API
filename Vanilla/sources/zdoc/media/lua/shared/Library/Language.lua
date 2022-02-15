---@class Language : zombie.core.Language
---@field private index int
---@field private name String
---@field private text String
---@field private charset String
---@field private base String
---@field private azerty boolean
Language = {}

---@public
---@return int
function Language:index() end

---@public
---@return String
function Language:toString() end

---@public
---@return String
function Language:text() end

---@public
---@return boolean
function Language:isAzerty() end

---@public
---@return String
function Language:charset() end

---@public
---@param value int
---@return Language
function Language:fromIndex(value) end

---@public
---@return String
function Language:base() end

---@public
---@param str String
---@return Language
function Language:FromString(str) end

---@public
---@return String
function Language:name() end
