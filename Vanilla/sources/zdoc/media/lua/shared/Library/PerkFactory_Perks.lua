---@class PerkFactory.Perks : zombie.characters.skills.PerkFactory.Perks
---@field public None PerkFactory.Perk
---@field public Agility PerkFactory.Perk
---@field public Cooking PerkFactory.Perk
---@field public Melee PerkFactory.Perk
---@field public Crafting PerkFactory.Perk
---@field public Fitness PerkFactory.Perk
---@field public Strength PerkFactory.Perk
---@field public Blunt PerkFactory.Perk
---@field public Axe PerkFactory.Perk
---@field public Sprinting PerkFactory.Perk
---@field public Lightfoot PerkFactory.Perk
---@field public Nimble PerkFactory.Perk
---@field public Sneak PerkFactory.Perk
---@field public Woodwork PerkFactory.Perk
---@field public Aiming PerkFactory.Perk
---@field public Reloading PerkFactory.Perk
---@field public Farming PerkFactory.Perk
---@field public Survivalist PerkFactory.Perk
---@field public Fishing PerkFactory.Perk
---@field public Trapping PerkFactory.Perk
---@field public Passiv PerkFactory.Perk
---@field public Firearm PerkFactory.Perk
---@field public PlantScavenging PerkFactory.Perk
---@field public Doctor PerkFactory.Perk
---@field public Electricity PerkFactory.Perk
---@field public Blacksmith PerkFactory.Perk
---@field public MetalWelding PerkFactory.Perk
---@field public Melting PerkFactory.Perk
---@field public Mechanics PerkFactory.Perk
---@field public Spear PerkFactory.Perk
---@field public Maintenance PerkFactory.Perk
---@field public SmallBlade PerkFactory.Perk
---@field public LongBlade PerkFactory.Perk
---@field public SmallBlunt PerkFactory.Perk
---@field public Combat PerkFactory.Perk
---@field public Tailoring PerkFactory.Perk
---@field public MAX PerkFactory.Perk
PerkFactory_Perks = {}

---@public
---@return int
function PerkFactory_Perks:getMaxIndex() end

---@public
---@param arg0 String
---@return PerkFactory.Perk
function PerkFactory_Perks:FromString(arg0) end

---@public
---@param arg0 int
---@return PerkFactory.Perk
function PerkFactory_Perks:fromIndex(arg0) end
