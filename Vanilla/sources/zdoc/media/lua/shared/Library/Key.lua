---@class Key : zombie.inventory.types.Key
---@field private keyId int
---@field private padlock boolean
---@field private numberOfKey int
---@field private digitalPadlock boolean
---@field public highlightDoor Key[]
Key = {}

---throws java.io.IOException
---
---Overrides:
---
---save in class InventoryItem
---@public
---@param output ByteBuffer
---@param net boolean
---@return void
function Key:save(output, net) end

---@public
---@param numberOfKey int
---@return void
function Key:setNumberOfKey(numberOfKey) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function Key:load(arg0, arg1) end

---@public
---@return boolean
function Key:isDigitalPadlock() end

---Get the key number of the building and set it to the key
---@public
---@return void
function Key:takeKeyId() end

---@public
---@param digitalPadlock boolean
---@return void
function Key:setDigitalPadlock(digitalPadlock) end

---Overrides:
---
---getCategory in class InventoryItem
---@public
---@return String
function Key:getCategory() end

---@public
---@return int
function Key:getSaveType() end

---@public
---@param arg0 int
---@param arg1 InventoryItem
---@return void
function Key:setHighlightDoors(arg0, arg1) end

---@public
---@param padlock boolean
---@return void
function Key:setPadlock(padlock) end

---Overrides:
---
---setKeyId in class InventoryItem
---@public
---@param keyId int
---@return void
function Key:setKeyId(keyId) end

---@public
---@return boolean
function Key:isPadlock() end

---Overrides:
---
---getKeyId in class InventoryItem
---@public
---@return int
function Key:getKeyId() end

---@public
---@return int
function Key:getNumberOfKey() end
