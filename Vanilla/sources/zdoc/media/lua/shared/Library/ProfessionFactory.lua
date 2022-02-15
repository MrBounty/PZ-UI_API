---@class ProfessionFactory : zombie.characters.professions.ProfessionFactory
---@field public ProfessionMap LinkedHashMap|String|ProfessionFactory.Profession
ProfessionFactory = {}

---@public
---@param type String
---@return ProfessionFactory.Profession
function ProfessionFactory:getProfession(type) end

---@public
---@return ArrayList|ProfessionFactory.Profession
function ProfessionFactory:getProfessions() end

---@public
---@param type String
---@param name String
---@param IconPath String
---@param points int
---@return ProfessionFactory.Profession
function ProfessionFactory:addProfession(type, name, IconPath, points) end

---@public
---@return void
function ProfessionFactory:Reset() end

---@public
---@return void
function ProfessionFactory:init() end
