---@class ObservationFactory.Observation : zombie.characters.traits.ObservationFactory.Observation
---@field private traitID String
---@field private name String
---@field private description String
---@field public MutuallyExclusive ArrayList|String
ObservationFactory_Observation = {}

---@public
---@param name String
---@return void
function ObservationFactory_Observation:setName(name) end

---@public
---@return String
function ObservationFactory_Observation:getName() end

---@public
---@param description String
---@return void
function ObservationFactory_Observation:setDescription(description) end

---Specified by:
---
---getLabel in interface IListBoxItem
---@public
---@return String
function ObservationFactory_Observation:getLabel() end

---Specified by:
---
---getLeftLabel in interface IListBoxItem
---@public
---@return String
function ObservationFactory_Observation:getLeftLabel() end

---@public
---@return String
function ObservationFactory_Observation:getTraitID() end

---@public
---@param traitID String
---@return void
function ObservationFactory_Observation:setTraitID(traitID) end

---Specified by:
---
---getRightLabel in interface IListBoxItem
---@public
---@return String
function ObservationFactory_Observation:getRightLabel() end

---@public
---@return String
function ObservationFactory_Observation:getDescription() end
