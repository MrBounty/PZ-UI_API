---@class SearchMode : zombie.iso.SearchMode
---@field private instance SearchMode
---@field private fadeTime float
---@field private plrModes SearchMode.PlayerSearchMode[]
SearchMode = {}

---@public
---@param arg0 int
---@return SearchMode.PlayerSearchMode
function SearchMode:getSearchModeForPlayer(arg0) end

---@public
---@return float
function SearchMode:getFadeTime() end

---@private
---@param arg0 int
---@return void
function SearchMode:FadeOut(arg0) end

---@public
---@param arg0 int
---@return SearchMode.SearchModeFloat
function SearchMode:getDesat(arg0) end

---@private
---@param arg0 int
---@return void
function SearchMode:FadeIn(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function SearchMode:setEnabled(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function SearchMode:setFadeTime(arg0) end

---@public
---@param arg0 int
---@return SearchMode.SearchModeFloat
function SearchMode:getDarkness(arg0) end

---@public
---@param arg0 int
---@return SearchMode.SearchModeFloat
function SearchMode:getGradientWidth(arg0) end

---@public
---@return void
function SearchMode:reset() end

---@public
---@param arg0 int
---@return SearchMode.SearchModeFloat
function SearchMode:getRadius(arg0) end

---@public
---@param arg0 int
---@return SearchMode.SearchModeFloat
function SearchMode:getBlur(arg0) end

---@public
---@return SearchMode
function SearchMode:getInstance() end

---@public
---@param arg0 int
---@return boolean
function SearchMode:isOverride(arg0) end

---@public
---@param arg0 int
---@return boolean
function SearchMode:isEnabled(arg0) end

---@public
---@return void
function SearchMode:update() end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function SearchMode:setOverride(arg0, arg1) end
