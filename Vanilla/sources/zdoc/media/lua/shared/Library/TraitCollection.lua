---@class TraitCollection : zombie.characters.traits.TraitCollection
---@field private m_activeTraitNames List|Unknown
---@field private m_traits List|Unknown
TraitCollection = {}

---@public
---@param arg0 String
---@return boolean
---@overload fun(arg0:Object)
function TraitCollection:contains(arg0) end

---@public
---@param arg0 Object
---@return boolean
function TraitCollection:contains(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function TraitCollection:set(arg0, arg1) end

---@private
---@param arg0 int
---@return void
function TraitCollection:deactivateTraitSlot(arg0) end

---@public
---@param arg0 Collection|Unknown
---@return void
function TraitCollection:removeAll(arg0) end

---@public
---@param arg0 Collection|Unknown
---@return void
function TraitCollection:addAll(arg0) end

---@public
---@return String
function TraitCollection:toString() end

---@public
---@param arg0 int
---@return String
function TraitCollection:get(arg0) end

---@public
---@param arg0 String
---@return boolean
---@overload fun(arg0:Object)
function TraitCollection:remove(arg0) end

---@public
---@param arg0 Object
---@return boolean
function TraitCollection:remove(arg0) end

---@private
---@param arg0 int
---@return TraitCollection.TraitSlot
function TraitCollection:getSlotInternal(arg0) end

---@public
---@return int
function TraitCollection:size() end

---@public
---@param arg0 String
---@return void
function TraitCollection:add(arg0) end

---@private
---@param arg0 String
---@return int
function TraitCollection:indexOfTrait(arg0) end

---@private
---@param arg0 String
---@return TraitCollection.TraitSlot
function TraitCollection:getOrCreateSlotInternal(arg0) end

---@public
---@return void
function TraitCollection:clear() end

---@public
---@return boolean
function TraitCollection:isEmpty() end

---@public
---@param arg0 String
---@return TraitCollection.TraitSlot
function TraitCollection:getTraitSlot(arg0) end
