---@class ObservationFactory : zombie.characters.traits.ObservationFactory
---@field public ObservationMap HashMap|String|ObservationFactory.Observation
ObservationFactory = {}

---@public
---@param a String
---@param b String
---@return void
function ObservationFactory:setMutualExclusive(a, b) end

---@public
---@param type String
---@param name String
---@param desc String
---@return void
function ObservationFactory:addObservation(type, name, desc) end

---@public
---@param name String
---@return ObservationFactory.Observation
function ObservationFactory:getObservation(name) end

---@public
---@return void
function ObservationFactory:init() end
