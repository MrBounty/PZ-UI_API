---@class DevicePresets : zombie.radio.devices.DevicePresets
---@field protected maxPresets int
---@field protected presets ArrayList|Unknown
DevicePresets = {}

---@public
---@param arg0 int
---@param arg1 String
---@param arg2 int
---@return void
function DevicePresets:setPreset(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 String
---@return void
function DevicePresets:setPresetName(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function DevicePresets:setPresetFreq(arg0, arg1) end

---@public
---@param arg0 int
---@return String
function DevicePresets:getPresetName(arg0) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function DevicePresets:setPresets(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function DevicePresets:save(arg0, arg1) end

---@public
---@return void
function DevicePresets:clearPresets() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function DevicePresets:load(arg0, arg1, arg2) end

---@protected
---@return Object
function DevicePresets:clone() end

---@public
---@param arg0 int
---@return void
function DevicePresets:setMaxPresets(arg0) end

---@public
---@return ArrayList|Unknown
function DevicePresets:getPresets() end

---@public
---@return KahluaTable
function DevicePresets:getPresetsLua() end

---@public
---@param arg0 String
---@param arg1 int
---@return void
function DevicePresets:addPreset(arg0, arg1) end

---@public
---@param arg0 int
---@return void
function DevicePresets:removePreset(arg0) end

---@public
---@param arg0 int
---@return int
function DevicePresets:getPresetFreq(arg0) end

---@public
---@return int
function DevicePresets:getMaxPresets() end
