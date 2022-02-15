---@class DBTicket : zombie.network.DBTicket
---@field private author String
---@field private message String
---@field private ticketID int
---@field private viewed boolean
---@field private answer DBTicket
---@field private isAnswer boolean
DBTicket = {}

---@public
---@param arg0 boolean
---@return void
function DBTicket:setViewed(arg0) end

---@public
---@return int
function DBTicket:getTicketID() end

---@public
---@return boolean
function DBTicket:isViewed() end

---@public
---@param arg0 boolean
---@return void
function DBTicket:setIsAnswer(arg0) end

---@public
---@param arg0 int
---@return void
function DBTicket:setTicketID(arg0) end

---@public
---@return String
function DBTicket:getAuthor() end

---@public
---@return DBTicket
function DBTicket:getAnswer() end

---@public
---@return boolean
function DBTicket:isAnswer() end

---@public
---@return String
function DBTicket:getMessage() end

---@public
---@param arg0 DBTicket
---@return void
function DBTicket:setAnswer(arg0) end

---@public
---@param arg0 String
---@return void
function DBTicket:setMessage(arg0) end

---@public
---@param arg0 String
---@return void
function DBTicket:setAuthor(arg0) end
