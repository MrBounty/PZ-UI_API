---@class PerkFactory.Perk : zombie.characters.skills.PerkFactory.Perk
---@field private id String
---@field private index int
---@field private bCustom boolean
---@field public translation String
---@field public name String
---@field public passiv boolean
---@field public xp1 int
---@field public xp2 int
---@field public xp3 int
---@field public xp4 int
---@field public xp5 int
---@field public xp6 int
---@field public xp7 int
---@field public xp8 int
---@field public xp9 int
---@field public xp10 int
---@field public parent PerkFactory.Perk
PerkFactory_Perk = {}

---@public
---@return int
function PerkFactory_Perk:getXp2() end

---@public
---@return int
function PerkFactory_Perk:getXp7() end

---@public
---@return int
function PerkFactory_Perk:getXp5() end

---@public
---@return String
function PerkFactory_Perk:getId() end

---@public
---@return PerkFactory.Perk
function PerkFactory_Perk:getParent() end

---@public
---@return int
function PerkFactory_Perk:getXp3() end

---@public
---@return int
function PerkFactory_Perk:getXp8() end

---@public
---@param level int
---@return float
function PerkFactory_Perk:getTotalXpForLevel(level) end

---@public
---@return void
function PerkFactory_Perk:setCustom() end

---@public
---@return int
function PerkFactory_Perk:getXp10() end

---@public
---@param level int
---@return float
function PerkFactory_Perk:getXpForLevel(level) end

---@public
---@return int
function PerkFactory_Perk:getXp6() end

---@public
---@return int
function PerkFactory_Perk:getXp1() end

---@public
---@return boolean
function PerkFactory_Perk:isPassiv() end

---@public
---@return String
function PerkFactory_Perk:getName() end

---@public
---@return int
function PerkFactory_Perk:index() end

---@public
---@return PerkFactory.Perk
function PerkFactory_Perk:getType() end

---@public
---@return int
function PerkFactory_Perk:getXp4() end

---@public
---@return int
function PerkFactory_Perk:getXp9() end

---@public
---@return String
function PerkFactory_Perk:toString() end

---@public
---@return boolean
function PerkFactory_Perk:isCustom() end
