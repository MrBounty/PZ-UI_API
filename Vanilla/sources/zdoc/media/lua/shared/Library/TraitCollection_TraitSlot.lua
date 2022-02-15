---@class TraitCollection.TraitSlot : zombie.characters.traits.TraitCollection.TraitSlot
---@field public Name String
---@field private m_isSet boolean
TraitCollection_TraitSlot = {}

---@public
---@return boolean
function TraitCollection_TraitSlot:isSet() end

---@public
---@param arg0 String
---@return boolean
function TraitCollection_TraitSlot:isName(arg0) end

---@public
---@return String
function TraitCollection_TraitSlot:toString() end

---@public
---@param arg0 boolean
---@return void
function TraitCollection_TraitSlot:set(arg0) end
