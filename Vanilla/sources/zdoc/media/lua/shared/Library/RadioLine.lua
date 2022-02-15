---@class RadioLine : zombie.radio.scripting.RadioLine
---@field private r float
---@field private g float
---@field private b float
---@field private text String
---@field private effects String
---@field private airTime float
RadioLine = {}

---@public
---@param arg0 float
---@return void
function RadioLine:setAirTime(arg0) end

---@public
---@return String
function RadioLine:getEffectsString() end

---@public
---@return boolean
function RadioLine:isCustomAirTime() end

---@public
---@return float
function RadioLine:getG() end

---@public
---@return float
function RadioLine:getAirTime() end

---@public
---@param arg0 String
---@return void
function RadioLine:setText(arg0) end

---@public
---@return float
function RadioLine:getB() end

---@public
---@return String
function RadioLine:getText() end

---@public
---@return float
function RadioLine:getR() end
