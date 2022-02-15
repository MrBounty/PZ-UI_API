---@class Recipe.RequiredSkill : zombie.scripting.objects.Recipe.RequiredSkill
---@field private perk PerkFactory.Perk
---@field private level int
Recipe_RequiredSkill = {}

---@public
---@return int
function Recipe_RequiredSkill:getLevel() end

---@public
---@return PerkFactory.Perk
function Recipe_RequiredSkill:getPerk() end
