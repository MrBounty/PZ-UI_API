--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Date: 07/02/2017
-- Time: 14:57
-- To change this template use File | Settings | File Templates.
--

require "StashDescriptions/StashUtil";

-- Old pixel coordinates to new world coordinates
local function P2Wx(x)
    local textureWorldX = 10800
    local textureWorldY = 6490
    local textureScale = 0.666
    return textureWorldX + x * textureScale
end
local function P2Wy(y)
    local textureWorldX = 10800
    local textureWorldY = 6490
    local textureScale = 0.666
    return textureWorldY + y * textureScale
end

-- Texture symbols are anchored to the center (assuming 18x18 texture size).
local function P2WxCenter(x)
    return P2Wx(x + 9)
end
local function P2WyCenter(y)
    return P2Wy(y + 9)
end

-- guns
local stashMap = StashUtil.newStash("WpStashMap1", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
--stashMap.spawnOnlyOnZed = true;
--stashMap.daysToSpawn = "0-30";
--stashMap.zombies = 5
--stashMap.traps = "1-5";
--stashMap.barricades = 50;
stashMap.buildingX = 10941
stashMap.buildingY = 6726
stashMap.spawnTable = "GunCache1";
stashMap:addContainer("GunBox","carpentry_01_16",nil,nil,nil,nil,nil);
stashMap:addStamp("X", nil, 10941, 6724, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap1_Text1", 10951, 6716, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap1_Text2", 11398, 6811, 1, 0, 0)

local stashMap = StashUtil.newStash("WpStashMap2", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.barricades = 30;
stashMap.buildingX = 11040
stashMap.buildingY = 6731
stashMap.spawnTable = "GunCache1";
stashMap:addStamp("X", nil, 11040, 6732, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap2_Text1", 11016, 6740, 0, 0, 0)
stashMap:addStamp("Circle", nil, 11935, 6804, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap2_Text2", 11942, 6795, 0, 0, 0)
stashMap:addStamp("Exclamation", nil, 11280, 6852, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap2_Text3", 11288, 6842, 0, 0, 0)

-- shotgun
local stashMap = StashUtil.newStash("WpStashMap3", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.spawnOnlyOnZed = true;
stashMap.zombies = 3;
stashMap.buildingX = 11318
stashMap.buildingY = 6727
stashMap.spawnTable = "ShotgunCache1";
stashMap:addStamp("Target", nil, 11317, 6726, 0, 0, 0)
stashMap:addContainer(nil,nil,"Base.Bag_DuffelBagTINT",nil,nil,nil,nil);

local stashMap = StashUtil.newStash("WpStashMap4", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.barricades = 20;
stashMap.buildingX = 11196
stashMap.buildingY = 6714
stashMap.spawnTable = "ShotgunCache1";
stashMap:addStamp("Target", nil, 11198, 6713, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap4_Text1", 11210, 6705, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap4_Text3", 11735, 6921, 1, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap4_Text31", 11735, 6936, 1, 0, 0)
stashMap:addContainer(nil,nil,"Base.Bag_DuffelBagTINT",nil,nil,nil,nil);

local stashMap = StashUtil.newStash("WpStashMap11", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.barricades = 40;
stashMap.buildingX = 11980
stashMap.buildingY = 6813
stashMap.spawnTable = "ShotgunCache1";
stashMap:addStamp("Target", nil, 11979, 6813, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap11_Text1", 11932, 6824, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap11_Text11", 11934, 6842, 0, 0, 0)
stashMap:addStamp("Circle", nil, 11974, 6913, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap11_Text2", 11983, 6906, 0, 0, 0)
stashMap:addStamp("X", nil, 11872, 6757, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap11_Text3", 11882, 6745, 0, 0, 0)
stashMap:addContainer(nil,nil,"Base.Bag_DuffelBagTINT",nil,nil,nil,nil);

-- tools
local stashMap = StashUtil.newStash("WpStashMap5", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.barricades = 30;
stashMap.buildingX = 11439
stashMap.buildingY = 6732
stashMap.spawnTable = "ToolsCache1";
stashMap:addStamp("Circle", nil, 11438, 6731, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap5_Text1", 11452, 6725, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap5_Text11", 11452, 6741, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap5_Text2", 11653, 7076, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap5_Text3", 11420, 6781, 0, 0, 0)
stashMap:addContainer("ToolsBox","carpentry_01_16",nil,"bedroom",nil,nil,nil);

local stashMap = StashUtil.newStash("WpStashMap6", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.spawnOnlyOnZed = true;
stashMap.zombies = 7;
stashMap.buildingX = 11496
stashMap.buildingY = 6703
stashMap.spawnTable = "ToolsCache1";
stashMap:addStamp("Circle", nil, 11498, 6703, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap6_Text1", 11466, 6713, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap6_Text2", 12064, 6794, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap6_Text3", 11458, 6948, 1, 0, 0)
stashMap:addContainer("ToolsBox","carpentry_01_16",nil,nil,nil,nil,nil);

-- survivor houses
local stashMap = StashUtil.newStash("WpStashMap7", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.spawnOnlyOnZed = true;
stashMap.buildingX = 11609
stashMap.buildingY = 6778
stashMap.spawnTable = "SurvivorCache1";
stashMap:addStamp("House", nil, 11608, 6778, 0, 0, 0)

local stashMap = StashUtil.newStash("WpStashMap8", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.spawnOnlyOnZed = true;
stashMap.zombies = 3;
stashMap.barricades = 40;
stashMap.buildingX = 11507
stashMap.buildingY = 6800
stashMap.spawnTable = "SurvivorCache1";
stashMap:addStamp("House", nil, 11507, 6801, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap8_Text1", 11517, 6792, 0, 0, 0)
stashMap:addStamp("Lightning", nil, 12070, 6762, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap8_Text2", 12078, 6751, 0, 0, 0)
stashMap:addStamp("Exclamation", nil, 11608, 6778, 1, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap8_Text3", 11616, 6767, 1, 0, 0)

-- danger houses
local stashMap = StashUtil.newStash("WpStashMap9", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.spawnOnlyOnZed = true;
stashMap.zombies = 20;
stashMap.barricades = 40;
stashMap.buildingX = 11656
stashMap.buildingY = 6816
stashMap.spawnTable = "SurvivorCache2";
stashMap:addStamp("Skull", nil, 11655, 6816, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap9_Text1", 11665, 6805, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap9_Text2", 11976, 6875, 0, 0, 0)
stashMap:addStamp("Question", nil, 12068, 6800, 1, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap9_Text3", 12078, 6790, 1, 0, 0)

local stashMap = StashUtil.newStash("WpStashMap10", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.spawnOnlyOnZed = true;
stashMap.traps = "1-3";
stashMap.barricades = 30;
stashMap.buildingX = 11645
stashMap.buildingY = 6694
stashMap.spawnTable = "SurvivorCache2";
stashMap:addStamp("Skull", nil, 11645, 6693, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap10_Text1", 11655, 6686, 0, 0, 0)
stashMap:addStamp("X", nil, 11958, 6914, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap10_Text2", 11967, 6906, 0, 0, 0)
stashMap:addStamp("Exclamation", nil, 11785, 6896, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap10_Text3", 11795, 6888, 0, 0, 0)

local stashMap = StashUtil.newStash("WpStashMap12", "Map", "Base.WestpointMap", "Stash_AnnotedMap");
stashMap.barricades = 80;
stashMap.zombies = 10;
stashMap.buildingX = 11981
stashMap.buildingY = 6943
stashMap.spawnTable = "SurvivorCache2";
stashMap:addStamp("Skull", nil, 11981, 6942, 1, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap12_Text1", 11992, 6932, 1, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap12_Text11", 11992, 6951, 1, 0, 0)
stashMap:addStamp("X", nil, 12068, 6761, 0, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap12_Text2", 12079, 6751, 0, 0, 0)
stashMap:addStamp("X", nil, 12227, 6828, 1, 0, 0)
stashMap:addStamp(nil, "Stash_WpMap12_Text3", 12235, 6820, 1, 0, 0)

