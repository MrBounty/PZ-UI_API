---@class WorldMapSettings : zombie.worldMap.WorldMapSettings
---@field public VERSION1 int
---@field public VERSION int
---@field private instance WorldMapSettings
---@field m_options ArrayList|Unknown
---@field mWorldMap WorldMapSettings.WorldMap
---@field mMiniMap WorldMapSettings.MiniMap
---@field private m_readVersion int
WorldMapSettings = {}

---@public
---@param arg0 String
---@param arg1 double
---@return double
function WorldMapSettings:getDouble(arg0, arg1) end

---@public
---@return WorldMapSettings
function WorldMapSettings:getInstance() end

---@public
---@param arg0 String
---@return ConfigOption
function WorldMapSettings:getOptionByName(arg0) end

---@public
---@return void
function WorldMapSettings:save() end

---@public
---@param arg0 int
---@return ConfigOption
function WorldMapSettings:getOptionByIndex(arg0) end

---@public
---@param arg0 String
---@return boolean
function WorldMapSettings:getBoolean(arg0) end

---@public
---@return int
function WorldMapSettings:getOptionCount() end

---@public
---@return int
function WorldMapSettings:getFileVersion() end

---@public
---@param arg0 String
---@param arg1 double
---@return void
function WorldMapSettings:setDouble(arg0, arg1) end

---@public
---@return void
function WorldMapSettings:load() end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function WorldMapSettings:setBoolean(arg0, arg1) end

---@public
---@return void
function WorldMapSettings:Reset() end

---@private
---@param arg0 String
---@param arg1 boolean
---@return BooleanConfigOption
---@overload fun(arg0:String, arg1:double, arg2:double, arg3:double)
function WorldMapSettings:newOption(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@return DoubleConfigOption
function WorldMapSettings:newOption(arg0, arg1, arg2, arg3) end
