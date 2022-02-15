-----------------------------------------------------------------------
--                      ROBERT JOHNSON                               --
-----------------------------------------------------------------------

----- how it works :
----- You have 3 type of size per fish : little, medium and big
----- All type have a minimum size and a maximum size
----- We choose a random number between this range to get the size of the fish
----- then, all fish have a weight change, like 60 for trout mean a trout of 30 cm / weight change (60) = 0.5 kg
----- then hunger reduction is weight * 10 (ex : 2kg * 10 = 20 hunger reduction)
-----

---@class Fishing
Fishing = Fishing or {}
---@class trashItems
trashItems = Fishing.trashItems or {};
--- Array who contains all our fish
---@class fishes
fishes = Fishing.fishes or {};
--- Pike :
local pike = {};
pike.name = "Pike";
pike.item = "Base.Pike";
pike.lure = {};
table.insert(pike.lure, "Worm");
table.insert(pike.lure, "BaitFish");
pike.little = {};
pike.medium = {};
pike.big = {};
pike.little.minSize = 50;
pike.little.maxSize = 65;
pike.medium.minSize = 65;
pike.medium.maxSize = 90;
pike.big.minSize = 90;
pike.big.maxSize = 120;
pike.little.weightChange = 30;
pike.medium.weightChange = 22;
pike.big.weightChange = 17;
table.insert(fishes, pike);
--- Trout :
local trout = {};
trout.name = "Trout";
trout.item = "Base.Trout";
trout.lure = {};
table.insert(trout.lure, "Worm");
table.insert(trout.lure, "Cricket");
table.insert(trout.lure, "Grasshopper");
table.insert(trout.lure, "Cockroach");
table.insert(trout.lure, "FishingTackle");
table.insert(trout.lure, "FishingTackle2");
trout.little = {};
trout.medium = {};
trout.big = {};
trout.little.minSize = 30;
trout.little.maxSize = 40;
trout.medium.minSize = 40;
trout.medium.maxSize = 50;
trout.big.minSize = 50;
trout.big.maxSize = 70;
trout.little.weightChange = 60;
trout.medium.weightChange = 45;
trout.big.weightChange = 30;
table.insert(fishes, trout);
--- Bass :
local bass = {};
bass.name = "Bass";
bass.item = "Base.Bass";
bass.lure = {};
table.insert(bass.lure, "Worm");
table.insert(bass.lure, "Cricket");
table.insert(bass.lure, "Grasshopper");
table.insert(bass.lure, "Cockroach");
table.insert(bass.lure, "FishingTackle");
table.insert(bass.lure, "FishingTackle2");
bass.little = {};
bass.medium = {};
bass.big = {};
bass.little.minSize = 25;
bass.little.maxSize = 35;
bass.medium.minSize = 35;
bass.medium.maxSize = 55;
bass.big.minSize = 55;
bass.big.maxSize = 75;
bass.little.weightChange = 50;
bass.medium.weightChange = 40;
bass.big.weightChange = 30;
table.insert(fishes, bass);
--- Cat fish :
local catfish = {};
catfish.name = "CatFish";
catfish.item = "Base.Catfish";
catfish.lure = {};
table.insert(catfish.lure, "Worm");
table.insert(catfish.lure, "Cricket");
table.insert(catfish.lure, "Grasshopper");
table.insert(catfish.lure, "Cockroach");
table.insert(catfish.lure, "FishingTackle");
table.insert(catfish.lure, "FishingTackle2");
--table.insert(catfish.lure, "BaitFish");
catfish.little = {};
catfish.medium = {};
catfish.big = {};
catfish.little.minSize = 15;
catfish.little.maxSize = 25;
catfish.medium.minSize = 25;
catfish.medium.maxSize = 35;
catfish.big.minSize = 35;
catfish.big.maxSize = 55;
catfish.little.weightChange = 50;
catfish.medium.weightChange = 40;
catfish.big.weightChange = 30;
table.insert(fishes, catfish);
--- Crappie fish :
local crappie = {};
crappie.name = "Crappie fish";
crappie.item = "Base.Crappie";
crappie.lure = {};
table.insert(crappie.lure, "Worm");
table.insert(crappie.lure, "Cricket");
table.insert(crappie.lure, "Grasshopper");
table.insert(crappie.lure, "Cockroach");
table.insert(crappie.lure, "FishingTackle");
table.insert(crappie.lure, "FishingTackle2");
crappie.little = {};
crappie.medium = {};
crappie.big = {};
crappie.little.minSize = 15;
crappie.little.maxSize = 20;
crappie.medium.minSize = 20;
crappie.medium.maxSize = 25;
crappie.big.minSize = 25;
crappie.big.maxSize = 35;
crappie.little.weightChange = 50;
crappie.medium.weightChange = 40;
crappie.big.weightChange = 30;
table.insert(fishes, crappie);
--- Panfish :
local panfish = {};
panfish.name = "PanFish";
panfish.item = "Base.Panfish";
panfish.lure = {};
table.insert(panfish.lure, "Worm");
table.insert(panfish.lure, "Cricket");
table.insert(panfish.lure, "Grasshopper");
table.insert(panfish.lure, "Cockroach");
table.insert(panfish.lure, "FishingTackle");
table.insert(panfish.lure, "FishingTackle2");
panfish.little = {};
panfish.medium = {};
panfish.big = {};
panfish.little.minSize = 15;
panfish.little.maxSize = 20;
panfish.medium.minSize = 20;
panfish.medium.maxSize = 25;
panfish.big.minSize = 25;
panfish.big.maxSize = 35;
panfish.little.weightChange = 40;
panfish.medium.weightChange = 35;
panfish.big.weightChange = 30;
table.insert(fishes, panfish);
--- Perch :
local perch = {};
perch.name = "Perch";
perch.item = "Base.Perch";
perch.lure = {};
table.insert(perch.lure, "Worm");
table.insert(perch.lure, "Cricket");
table.insert(perch.lure, "Grasshopper");
table.insert(perch.lure, "Cockroach");
table.insert(perch.lure, "FishingTackle");
table.insert(perch.lure, "FishingTackle2");
perch.little = {};
perch.medium = {};
perch.big = {};
perch.little.minSize = 20;
perch.little.maxSize = 30;
perch.medium.minSize = 30;
perch.medium.maxSize = 40;
perch.big.minSize = 40;
perch.big.maxSize = 60;
perch.little.weightChange = 20;
perch.medium.weightChange = 16;
perch.big.weightChange = 12;
table.insert(fishes, perch);
--- Walleye :
--walleye = {};
--walleye.name = "Walleye";
--walleye.icon = "TZ_WalleyeFish";
--walleye.fishPicture = "PicWalleye.png";
--walleye.little = {};
--walleye.medium = {};
--walleye.big = {};
--walleye.little.minSize = 40;
--walleye.little.maxSize = 50;
--walleye.medium.minSize = 50;
--walleye.medium.maxSize = 60;
--walleye.big.minSize = 60;
--walleye.big.maxSize = 75;
--walleye.little.weightChange = 20;
--walleye.medium.weightChange = 15;
--walleye.big.weightChange = 10;
--fishes["walleye"] = walleye;
--Bait fish
local baitfish = {};
baitfish.item = "Base.BaitFish";
baitfish.lure = {};
table.insert(baitfish.lure, "Worm");
table.insert(baitfish.lure, "Cricket");
table.insert(baitfish.lure, "Grasshopper");
table.insert(baitfish.lure, "Cockroach");
table.insert(fishes, baitfish);
-- Trash Items
-- Socks
local socks = {};
socks.item = "Base.Socks_Ankle";
socks.lure = {};
table.insert(socks.lure, "Worm");
table.insert(socks.lure, "Cricket");
table.insert(socks.lure, "Grasshopper");
table.insert(socks.lure, "Cockroach");
table.insert(socks.lure, "BaitFish");
table.insert(socks.lure, "FishingTackle");
table.insert(socks.lure, "FishingTackle2");
table.insert(trashItems, socks);
local shoes = {};
shoes.item = "Base.Shoes_Random";
shoes.lure = {};
table.insert(shoes.lure, "Worm");
table.insert(shoes.lure, "Cricket");
table.insert(shoes.lure, "Grasshopper");
table.insert(shoes.lure, "Cockroach");
table.insert(shoes.lure, "BaitFish");
table.insert(shoes.lure, "FishingTackle");
table.insert(shoes.lure, "FishingTackle2");
table.insert(trashItems, shoes);



--------------------- LURE PROPERTIES -----------------------
lure = Fishing.lure or {};

local worm = {};
worm.item = "Worm";
worm.plastic = false;
worm.chanceOfBreak = 15;
lure["Worm"] = worm;

local cricket = {}
cricket.item = "Cricket";
cricket.plastic = false;
cricket.chanceOfBreak = 15;
lure["Cricket"] = cricket;

local grasshopper = {};
grasshopper.item = "Grasshopper";
grasshopper.plastic = false;
grasshopper.chanceOfBreak = 15;
lure["Grasshopper"] = grasshopper;

local cockroach = {};
cockroach.item = "Cockroach";
cockroach.plastic = false;
cockroach.chanceOfBreak = 15;
lure["Cockroach"] = cockroach;

local fishingTackle = {};
fishingTackle.item = "FishingTackle";
fishingTackle.plastic = true;
fishingTackle.chanceOfBreak = 1;
lure["FishingTackle"] = fishingTackle;

local fishingTackle2 = {};
fishingTackle2.item = "FishingTackle2";
fishingTackle2.plastic = true;
fishingTackle2.chanceOfBreak = 1;
lure["FishingTackle2"] = fishingTackle2;

local baitFish = {};
baitFish.item = "BaitFish";
baitFish.plastic = false;
baitFish.chanceOfBreak = 3;
lure["BaitFish"] = baitFish;

Fishing.fishes = fishes
Fishing.lure = lure
Fishing.trashItems = trashItems
