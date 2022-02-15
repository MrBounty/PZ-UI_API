--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 06/02/14
-- Time: 10:22
-- To change this template use File | Settings | File Templates.
--

-- Traps definition
---@class Traps
Traps = {};
local crateTrap = {};
crateTrap.type = "Base.TrapCrate";
crateTrap.sprite = "constructedobjects_01_3";
crateTrap.closedSprite = "constructedobjects_01_2";
crateTrap.trapStrength = 15;
crateTrap.destroyItem = { "Base.UnusableWood", "Base.Nails" };
table.insert(Traps, crateTrap);
local trapBox = {};
trapBox.type = "Base.TrapBox";
trapBox.sprite = "constructedobjects_01_4";
trapBox.northSprite = "constructedobjects_01_7";
trapBox.closedSprite = "constructedobjects_01_5";
trapBox.northClosedSprite = "constructedobjects_01_6";
trapBox.trapStrength = 15;
trapBox.destroyItem = { "Base.UnusableWood", "Base.Nails", "Base.Nails" };
table.insert(Traps, trapBox);
local cageTrap = {};
cageTrap.type = "Base.TrapCage";
cageTrap.sprite = "constructedobjects_01_8";
cageTrap.northSprite = "constructedobjects_01_11";
cageTrap.closedSprite = "constructedobjects_01_9";
cageTrap.northClosedSprite = "constructedobjects_01_10";
cageTrap.trapStrength = 20;
cageTrap.destroyItem = "Base.Wire";
table.insert(Traps, cageTrap);
local snareTrap = {};
snareTrap.type = "Base.TrapSnare";
snareTrap.sprite = "constructedobjects_01_16";
snareTrap.closedSprite = "constructedobjects_01_17";
snareTrap.trapStrength = 10;
snareTrap.destroyItem = { "Base.UnusableWood", "Base.Twine", "Base.Twine" };
table.insert(Traps, snareTrap);
local stickTrap = {};
stickTrap.type = "Base.TrapStick";
stickTrap.sprite = "constructedobjects_01_13";
stickTrap.closedSprite = "constructedobjects_01_12";
stickTrap.trapStrength = 15;
stickTrap.destroyItem = { "Base.UnusableWood", "Base.Twine" };
table.insert(Traps, stickTrap);
local mouseTrap = {};
mouseTrap.type = "Base.TrapMouse";
mouseTrap.sprite = "constructedobjects_01_18";
mouseTrap.closedSprite = "constructedobjects_01_19";
mouseTrap.trapStrength = 50;
mouseTrap.destroyItem = "Base.UnusableWood";
table.insert(Traps, mouseTrap);

-- Animals definition
---@class Animals
Animals = {};
local rabbit = {};
rabbit.type = "rabbit";
-- after how many hour the animal will start to destroy the cage/escape
rabbit.strength = 24;
-- item given to the player
rabbit.item = "Base.DeadRabbit";
-- hour this animal will be out and when you can catch it
rabbit.minHour = 19;
rabbit.maxHour = 5;
-- min and max "size" (understand hunger reduction) of the animal
rabbit.minSize = 30;
rabbit.maxSize = 100;
-- chance to get the animals per zone
rabbit.zone = {};
rabbit.zone["TownZone"] = 2;
rabbit.zone["TrailerPark"] = 2;
rabbit.zone["Vegitation"] = 10;
rabbit.zone["Forest"] = 12;
rabbit.zone["DeepForest"] = 15;
-- chance to get animals for each trap
rabbit.traps = {};
rabbit.traps["Base.TrapCage"] = 40;
rabbit.traps["Base.TrapSnare"] = 30;
rabbit.traps["Base.TrapBox"] = 30;
rabbit.traps["Base.TrapCrate"] = 30;
-- chance to attract animal per bait
rabbit.baits = {};
rabbit.baits["Base.Carrots"] = 45;
rabbit.baits["Base.Apple"] = 35;
rabbit.baits["Base.Lettuce"] = 40;
rabbit.baits["Base.BellPepper"] = 40;
rabbit.baits["farming.Cabbage"] = 40;
rabbit.baits["Base.Corn"] = 35;
rabbit.baits["Base.Banana"] = 35;
rabbit.baits["farming.Potato"] = 35;
rabbit.baits["farming.Tomato"] = 35;
rabbit.baits["Base.Peach"] = 35;

local squirrel = {};
squirrel.type = "squirrel";
squirrel.strength = 20;
squirrel.item = "Base.DeadSquirrel";
squirrel.minHour = 19;
squirrel.maxHour = 5;
squirrel.minSize = 10;
squirrel.maxSize = 60;
squirrel.zone = {};
squirrel.zone["TownZone"] = 2;
squirrel.zone["TrailerPark"] = 2;
squirrel.zone["Vegitation"] = 10;
squirrel.zone["Forest"] = 12;
squirrel.zone["DeepForest"] = 15;
squirrel.traps = {};
squirrel.traps["Base.TrapBox"] = 25;
squirrel.traps["Base.TrapCage"] = 40;
squirrel.traps["Base.TrapCrate"] = 30;
squirrel.traps["Base.TrapSnare"] = 30;
squirrel.baits = {};
squirrel.baits["Base.PeanutButter"] = 45;
squirrel.baits["Base.Cereal"] = 45;
squirrel.baits["Base.Orange"] = 40;
squirrel.baits["Base.Peanuts"] = 40;
squirrel.baits["Base.Apple"] = 40;
squirrel.baits["Base.Corn"] = 40;
squirrel.baits["Base.Popcorn"] = 40;
squirrel.baits["Base.Lettuce"] = 40;
squirrel.baits["Base.BellPepper"] = 30;
squirrel.baits["Base.Peach"] = 35;

local bird = {};
bird.type = "bird";
bird.item = "Base.DeadBird";
bird.minHour = 0;
bird.maxHour = 0;
bird.minSize = 2;
bird.maxSize = 32;
bird.zone = {};
bird.zone["FarmLand"] = 35;
bird.zone["TownZone"] = 30;
bird.zone["Vegitation"] = 30;
bird.zone["TrailerPark"] = 20;
bird.zone["Forest"] = 20;
bird.zone["DeepForest"] = 20;
bird.traps = {};
bird.traps["Base.TrapStick"] = 40;
bird.baits = {};
bird.baits["Base.Bread"] = 50;
bird.baits["Base.BreadSlices"] = 50;
bird.baits["Base.Worm"] = 50;
bird.baits["Base.Grasshopper"] = 50;
bird.baits["Base.Cockroach"] = 50;
bird.baits["Base.Cricket"] = 50;
bird.baits["Base.Corn"] = 45;
bird.baits["Base.Cereal"] = 45;

local mouse = {};
mouse.type = "mouse";
mouse.item = "Base.DeadMouse";
mouse.minHour = 0;
mouse.maxHour = 0;
mouse.minSize = 1;
mouse.maxSize = 10;
mouse.zone = {};
mouse.zone["FarmLand"] = 60;
mouse.zone["TownZone"] = 50;
mouse.zone["TrailerPark"] = 60;
mouse.zone["Vegitation"] = 20;
mouse.zone["Forest"] = 10;
mouse.zone["DeepForest"] = 10;
mouse.traps = {};
mouse.traps["Base.TrapMouse"] = 30;
mouse.baits = {};
mouse.baits["Base.Cheese"] = 60;
mouse.baits["Base.Processedcheese"] = 55;
mouse.baits["Base.PeanutButter"] = 40;
mouse.baits["farming.BaconBits"] = 40;
mouse.baits["Base.Chocolate"] = 30;
mouse.baits["Base.Orange"] = 35;
mouse.baits["farming.Apple"] = 35;
mouse.baits["farming.Tomato"] = 35;

local rat = {};
rat.type = "rat";
rat.item = "Base.DeadRat";
rat.minHour = 0;
rat.maxHour = 0;
rat.minSize = 5;
rat.maxSize = 25;
rat.zone = {};
rat.zone["FarmLand"] = 50;
rat.zone["TrailerPark"] = 40;
rat.zone["TownZone"] = 30;
rat.zone["Vegitation"] = 20;
rat.zone["Forest"] = 10;
rat.zone["DeepForest"] = 10;
rat.traps = {};
rat.traps["Base.TrapMouse"] = 25;
rat.baits = {};
rat.baits["Base.Cheese"] = 60;
rat.baits["Base.PeanutButter"] = 40;
rat.baits["farming.BaconBits"] = 40;
rat.baits["Base.Chocolate"] = 30;
rat.baits["Base.Orange"] = 35;
rat.baits["farming.Apple"] = 35;
rat.baits["farming.Tomato"] = 35;
rat.baits["Base.Processedcheese"] = 55;

table.insert(Animals, bird);
table.insert(Animals, rat);
table.insert(Animals, mouse);
table.insert(Animals, rabbit);
table.insert(Animals, squirrel);
