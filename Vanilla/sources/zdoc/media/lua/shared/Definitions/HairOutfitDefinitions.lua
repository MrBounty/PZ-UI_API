---@class HairOutfitDefinitions
HairOutfitDefinitions = HairOutfitDefinitions or {};

-- forbid some haircut for later in the apocalypse
-- also make some available only for certain outfit
HairOutfitDefinitions.haircutDefinition = {};
local cat = {};
cat.name = "MohawkShort";
cat.onlyFor = "Punk,Bandit,Redneck,Biker,PrivateMilitia";
table.insert(HairOutfitDefinitions.haircutDefinition, cat);
local cat = {};
cat.name = "MohawkFan";
cat.onlyFor = "Punk,Bandit";
table.insert(HairOutfitDefinitions.haircutDefinition, cat);
local cat = {};
cat.name = "MohawkSpike";
cat.minWorldAge = 180;
cat.onlyFor = "Punk,Bandit";
table.insert(HairOutfitDefinitions.haircutDefinition, cat);
local cat = {};
cat.name = "MohawkFlat";
cat.minWorldAge = 180;
cat.onlyFor = "None";
table.insert(HairOutfitDefinitions.haircutDefinition, cat);
local cat = {};
cat.name = "LibertySpikes";
cat.minWorldAge = 180;
cat.onlyFor = "Punk,Bandit";
table.insert(HairOutfitDefinitions.haircutDefinition, cat);
local cat = {};
cat.name = "Cornrows";
cat.onlyFor = "Punk,Bandit,Redneck,Biker,BaseballPlayer_KY,BaseballPlayer_Rangers,BaseballPlayer_Z,StreetSports";
table.insert(HairOutfitDefinitions.haircutDefinition, cat);

-- define possible haircut based on outfit
-- if nothing is defined for a outfit, we just pick a random one
-- the haircuts in ZombiesZoneDefinitions take precedence over this!
-- this is used mainly for stories, so when i spawn a punk, i want more chance to have a mohawk on him..

HairOutfitDefinitions.haircutOutfitDefinition = {};
local cat = {};
cat.outfit = "Bandit";
cat.haircut = "LibertySpikes:5;MohawkFan:5;MohawkShort:5;MohawkSpike:5"; -- total should not exced 100! anything "left over" will be a random haircut
cat.haircutColor = "0.98,0.87,0:10;0.82,0.15,0.07:10;0.21,0.6,0.3:10;0.26,0.6,0.81:10"; -- forcing a haircut color, total should not exced 100 anything "left over" will be a random color from our default color
table.insert(HairOutfitDefinitions.haircutOutfitDefinition, cat);
local cat = {};
cat.outfit = "Punk";
cat.haircut = "LibertySpikes:5;MohawkFan:7;MohawkShort:7;MohawkSpike:5";
cat.haircutColor = "0.98,0.87,0:10;0.82,0.15,0.07:10;0.21,0.6,0.3:10;0.26,0.6,0.81:10";
table.insert(HairOutfitDefinitions.haircutOutfitDefinition, cat);
local cat = {};
cat.outfit = "PrivateMilitia";
cat.beard = "Long:30;Goatee:10;Full:10;LongScruffy:20";
cat.haircut = "MohawkShort:5";
table.insert(HairOutfitDefinitions.haircutOutfitDefinition, cat);
local cat = {};
cat.outfit = "HockeyPsycho";
cat.haircut = "Bob:100;";
table.insert(HairOutfitDefinitions.haircutOutfitDefinition, cat);
