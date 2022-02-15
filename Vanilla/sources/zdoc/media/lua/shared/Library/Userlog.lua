---@class Userlog : zombie.network.Userlog
---@field private username String
---@field private type String
---@field private text String
---@field private issuedBy String
---@field private amount int
Userlog = {}

---@public
---@param arg0 int
---@return void
function Userlog:setAmount(arg0) end

---@public
---@return String
function Userlog:getUsername() end

---@public
---@return String
function Userlog:getIssuedBy() end

---@public
---@return String
function Userlog:getType() end

---@public
---@return String
function Userlog:getText() end

---@public
---@return int
function Userlog:getAmount() end
