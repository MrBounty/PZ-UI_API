---@class TraitFactory : zombie.characters.traits.TraitFactory
---@field public TraitMap LinkedHashMap|String|TraitFactory.Trait
TraitFactory = {}

---@public
---@return void
function TraitFactory:sortList() end

---@public
---@param a String
---@param b String
---@return void
function TraitFactory:setMutualExclusive(a, b) end

---@public
---@return void
function TraitFactory:init() end

---@public
---@param type String
---@param name String
---@param cost int
---@param desc String
---@param profession boolean
---@return TraitFactory.Trait
---@overload fun(type:String, name:String, cost:int, desc:String, profession:boolean, removeInMP:boolean)
function TraitFactory:addTrait(type, name, cost, desc, profession) end

---@public
---@param type String
---@param name String
---@param cost int
---@param desc String
---@param profession boolean
---@param removeInMP boolean
---@return TraitFactory.Trait
function TraitFactory:addTrait(type, name, cost, desc, profession, removeInMP) end

---@public
---@param name String
---@return TraitFactory.Trait
function TraitFactory:getTrait(name) end

---@public
---@return void
function TraitFactory:Reset() end

---@public
---@return ArrayList|TraitFactory.Trait
function TraitFactory:getTraits() end
