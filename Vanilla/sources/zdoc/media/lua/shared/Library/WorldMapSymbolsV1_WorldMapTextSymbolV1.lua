---@class WorldMapSymbolsV1.WorldMapTextSymbolV1 : zombie.worldMap.symbols.WorldMapSymbolsV1.WorldMapTextSymbolV1
---@field m_textSymbol WorldMapTextSymbol
WorldMapSymbolsV1_WorldMapTextSymbolV1 = {}

---@public
---@return String
function WorldMapSymbolsV1_WorldMapTextSymbolV1:getUntranslatedText() end

---@public
---@return boolean
function WorldMapSymbolsV1_WorldMapTextSymbolV1:isText() end

---@public
---@return String
function WorldMapSymbolsV1_WorldMapTextSymbolV1:getTranslatedText() end

---@public
---@param arg0 String
---@return void
function WorldMapSymbolsV1_WorldMapTextSymbolV1:setTranslatedText(arg0) end

---@public
---@param arg0 String
---@return void
function WorldMapSymbolsV1_WorldMapTextSymbolV1:setUntranslatedText(arg0) end

---@param arg0 WorldMapSymbolsV1
---@param arg1 WorldMapTextSymbol
---@return WorldMapSymbolsV1.WorldMapTextSymbolV1
function WorldMapSymbolsV1_WorldMapTextSymbolV1:init(arg0, arg1) end
