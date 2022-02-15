---@class Fixing.Fixer : zombie.scripting.objects.Fixing.Fixer
---@field private fixerName String
---@field private skills LinkedList|Unknown
---@field private numberOfUse int
Fixing_Fixer = {}

---@public
---@return String
function Fixing_Fixer:getFixerName() end

---@public
---@return LinkedList|Fixing.FixerSkill
function Fixing_Fixer:getFixerSkills() end

---@public
---@return int
function Fixing_Fixer:getNumberOfUse() end
