--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 28/08/14
-- Time: 14:55
-- To change this template use File | Settings | File Templates.
--

scavenges = {};
scavenges.plants = {}; -- for hydrocraft compatibility
-- plants and other foods
scavenges.insects = {};
scavenges.fishbait = {};
scavenges.mushrooms = {};
scavenges.berries = {};
-- Branches, stones..
scavenges.forestGoods = {};
-- Medicinal herbs
scavenges.medicinalPlants = {};

-- plants
local berriesBlack = {};
berriesBlack.type = "Base.BerryBlack";
berriesBlack.minCount = 1;
berriesBlack.maxCount = 3;
berriesBlack.skill = 2;
local berriesBlue = {};
berriesBlue.type = "Base.BerryBlue";
berriesBlue.minCount = 1;
berriesBlue.maxCount = 3;
berriesBlue.skill = 2;
local berries1 = {};
berries1.type = "Base.BerryGeneric1";
berries1.minCount = 1;
berries1.maxCount = 3;
berries1.skill = 0;
local berries2 = {};
berries2.type = "Base.BerryGeneric2";
berries2.minCount = 1;
berries2.maxCount = 4;
berries2.skill = 2;
local berries3 = {};
berries3.type = "Base.BerryGeneric3";
berries3.minCount = 1;
berries3.maxCount = 3;
berries3.skill = 0;
local berries4 = {};
berries4.type = "Base.BerryGeneric4";
berries4.minCount = 1;
berries4.maxCount = 3;
berries4.skill = 0;
local berries5 = {};
berries5.type = "Base.BerryGeneric5";
berries5.minCount = 2;
berries5.maxCount = 4;
berries5.skill = 2;
local berriesIvy = {};
berriesIvy.type = "Base.BerryPoisonIvy";
berriesIvy.minCount = 1;
berriesIvy.maxCount = 4;
berriesIvy.skill = 2;
local mushrooms1 = {};
mushrooms1.type = "Base.MushroomGeneric1";
mushrooms1.minCount = 1;
mushrooms1.maxCount = 2;
mushrooms1.skill = 4;
local mushrooms2 = {};
mushrooms2.type = "Base.MushroomGeneric2";
mushrooms2.minCount = 1;
mushrooms2.maxCount = 2;
mushrooms2.skill = 4;
local mushrooms3 = {};
mushrooms3.type = "Base.MushroomGeneric3";
mushrooms3.minCount = 1;
mushrooms3.maxCount = 1;
mushrooms3.skill = 6;
local mushrooms4 = {};
mushrooms4.type = "Base.MushroomGeneric4";
mushrooms4.minCount = 1;
mushrooms4.maxCount = 2;
mushrooms4.skill = 4;
local mushrooms5 = {};
mushrooms5.type = "Base.MushroomGeneric5";
mushrooms5.minCount = 1;
mushrooms5.maxCount = 2;
mushrooms5.skill = 6;
local mushrooms6 = {};
mushrooms6.type = "Base.MushroomGeneric6";
mushrooms6.minCount = 1;
mushrooms6.maxCount = 1;
mushrooms6.skill = 4;
local mushrooms7 = {};
mushrooms7.type = "Base.MushroomGeneric7";
mushrooms7.minCount = 1;
mushrooms7.maxCount = 1;
mushrooms7.skill = 4;
local Violets = {};
Violets.type = "Base.Violets";
Violets.minCount = 2;
Violets.maxCount = 5;
Violets.skill = 0;
local GrapeLeaves = {};
GrapeLeaves.type = "Base.GrapeLeaves";
GrapeLeaves.minCount = 1;
GrapeLeaves.maxCount = 5;
GrapeLeaves.skill = 2;
local Rosehips = {};
Rosehips.type = "Base.Rosehips";
Rosehips.minCount = 1;
Rosehips.maxCount = 4;
Rosehips.skill = 4;
local WildEggs = {};
WildEggs.type = "Base.WildEggs";
WildEggs.minCount = 1;
WildEggs.maxCount = 3;
WildEggs.skill = 8;
local worms = {};
worms.type = "Base.Worm";
worms.minCount = 1;
worms.maxCount = 2;
worms.skill = 0;
local Cricket = {};
Cricket.type = "Base.Cricket";
Cricket.minCount = 1;
Cricket.maxCount = 2;
Cricket.skill = 4;
local Grasshopper = {};
Grasshopper.type = "Base.Grasshopper";
Grasshopper.minCount = 1;
Grasshopper.maxCount = 2;
Grasshopper.skill = 6;
local Cockroach = {};
Cockroach.type = "Base.Cockroach";
Cockroach.minCount = 1;
Cockroach.maxCount = 2;
Cockroach.skill = 6;
local Frog = {};
Frog.type = "Base.Frog";
Frog.minCount = 1;
Frog.maxCount = 1;
Frog.skill = 7;

-- forest goods
local log = {};
log.type = "Base.Log";
log.minCount = 1;
log.maxCount = 1;
log.skill = 6;
local branch = {};
branch.type = "Base.TreeBranch";
branch.minCount = 1;
branch.maxCount = 3;
branch.skill = 0;
local sharpedStone = {};
sharpedStone.type = "Base.SharpedStone";
sharpedStone.minCount = 1;
sharpedStone.maxCount = 1;
sharpedStone.skill = 0;
local stone = {};
stone.type = "Base.Stone";
stone.minCount = 1;
stone.maxCount = 2;
stone.skill = 0;
local twigs = {};
twigs.type = "Base.Twigs";
twigs.minCount = 1;
twigs.maxCount = 3;
twigs.skill = 0;

-- medicinal herbs
local plantain = {};
plantain.type = "Base.Plantain";
plantain.minCount = 1;
plantain.maxCount = 3;
plantain.skill = 0;
local comfrey = {};
comfrey.type = "Base.Comfrey";
comfrey.minCount = 1;
comfrey.maxCount = 3;
comfrey.skill = 0;
local WildGarlic = {};
WildGarlic.type = "Base.WildGarlic";
WildGarlic.minCount = 1;
WildGarlic.maxCount = 3;
WildGarlic.skill = 0;
local CommonMallow = {};
CommonMallow.type = "Base.CommonMallow";
CommonMallow.minCount = 1;
CommonMallow.maxCount = 3;
CommonMallow.skill = 0;
local LemonGrass = {};
LemonGrass.type = "Base.LemonGrass";
LemonGrass.minCount = 1;
LemonGrass.maxCount = 3;
LemonGrass.skill = 0;
local BlackSage = {};
BlackSage.type = "Base.BlackSage";
BlackSage.minCount = 1;
BlackSage.maxCount = 3;
BlackSage.skill = 0;
local Ginseng = {};
Ginseng.type = "Base.Ginseng";
Ginseng.minCount = 1;
Ginseng.maxCount = 3;
Ginseng.skill = 0;

table.insert(scavenges.berries, berriesBlack);
table.insert(scavenges.berries, berriesBlue);
table.insert(scavenges.berries, berriesIvy);
table.insert(scavenges.berries, berries1);
table.insert(scavenges.berries, berries2);
table.insert(scavenges.berries, berries3);
table.insert(scavenges.berries, berries4);
table.insert(scavenges.berries, berries5);
table.insert(scavenges.mushrooms, mushrooms1);
table.insert(scavenges.mushrooms, mushrooms2);
table.insert(scavenges.mushrooms, mushrooms3);
table.insert(scavenges.mushrooms, mushrooms4);
table.insert(scavenges.mushrooms, mushrooms5);
table.insert(scavenges.mushrooms, mushrooms6);
table.insert(scavenges.mushrooms, mushrooms7);
table.insert(scavenges.fishbait, Cricket);
table.insert(scavenges.fishbait, Grasshopper);
table.insert(scavenges.fishbait, Cockroach);
table.insert(scavenges.fishbait, worms);
table.insert(scavenges.insects, Frog);
table.insert(scavenges.insects, WildEggs);

table.insert(scavenges.forestGoods, log);
table.insert(scavenges.forestGoods, branch);
table.insert(scavenges.forestGoods, sharpedStone);
table.insert(scavenges.forestGoods, stone);
table.insert(scavenges.forestGoods, twigs);

table.insert(scavenges.medicinalPlants, plantain);
table.insert(scavenges.medicinalPlants, comfrey);
table.insert(scavenges.medicinalPlants, WildGarlic);
table.insert(scavenges.medicinalPlants, CommonMallow);
table.insert(scavenges.medicinalPlants, LemonGrass);
table.insert(scavenges.medicinalPlants, BlackSage);
table.insert(scavenges.medicinalPlants, Ginseng);
table.insert(scavenges.medicinalPlants, GrapeLeaves);
table.insert(scavenges.medicinalPlants, Rosehips);
table.insert(scavenges.medicinalPlants, Violets);

local check = function()
    -- Kahlua gives an error if this loop is placed outside this function.
    for k,v in ipairs(scavenges.berries) do
        local item = ScriptManager.instance:FindItem(v.type)
        if not item then print("ScavengDefinitions: no such item "..v.type) end
    end
end
check()
