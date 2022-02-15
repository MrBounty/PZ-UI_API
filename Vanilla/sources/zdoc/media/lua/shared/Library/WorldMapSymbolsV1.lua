---@class WorldMapSymbolsV1 : zombie.worldMap.symbols.WorldMapSymbolsV1
---@field private s_textPool Pool|Unknown
---@field private s_texturePool Pool|Unknown
---@field private m_ui UIWorldMap
---@field private m_uiSymbols WorldMapSymbols
---@field private m_symbols ArrayList|Unknown
WorldMapSymbolsV1 = {}

---@public
---@param arg0 String
---@param arg1 UIFont
---@param arg2 float
---@param arg3 float
---@return WorldMapSymbolsV1.WorldMapTextSymbolV1
function WorldMapSymbolsV1:addTranslatedText(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@return WorldMapSymbolsV1.WorldMapBaseSymbolV1
function WorldMapSymbolsV1:getSymbolByIndex(arg0) end

---@public
---@return int
function WorldMapSymbolsV1:getSymbolCount() end

---@public
---@param arg0 String
---@param arg1 float
---@param arg2 float
---@return WorldMapSymbolsV1.WorldMapTextureSymbolV1
function WorldMapSymbolsV1:addTexture(arg0, arg1, arg2) end

---@public
---@param arg0 LuaManager.Exposer
---@return void
function WorldMapSymbolsV1:setExposed(arg0) end

---@public
---@param arg0 int
---@return void
function WorldMapSymbolsV1:removeSymbolByIndex(arg0) end

---@public
---@return void
function WorldMapSymbolsV1:clear() end

---@public
---@param arg0 float
---@param arg1 float
---@return int
function WorldMapSymbolsV1:hitTest(arg0, arg1) end

---@return void
function WorldMapSymbolsV1:reinit() end

---@public
---@param arg0 String
---@param arg1 UIFont
---@param arg2 float
---@param arg3 float
---@return WorldMapSymbolsV1.WorldMapTextSymbolV1
function WorldMapSymbolsV1:addUntranslatedText(arg0, arg1, arg2, arg3) end
