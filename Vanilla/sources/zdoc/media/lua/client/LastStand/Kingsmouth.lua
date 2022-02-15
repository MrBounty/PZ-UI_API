

---@class Kingsmouth
Kingsmouth = {}

Kingsmouth.Add = function()
    addChallenge(Kingsmouth);
end

Kingsmouth.AddPlayer = function(playerNum, playerObj)
end

function Kingsmouth.RemovePlayer(playerObj)
end

Kingsmouth.Init = function()
end

Kingsmouth.OnGameStart = function()
end


Kingsmouth.OnInitWorld = function()
    SandboxVars.Zombies = 2;
--    SandboxVars.Distribution = 1;
--    SandboxVars.DayLength = 3;
--    SandboxVars.StartMonth = 7;
--    SandboxVars.StartTime = 2;
--    SandboxVars.WaterShutModifier = 7;
--    SandboxVars.ElecShutModifier = 7;
--    SandboxVars.FoodLoot = 4;
    SandboxVars.WeaponLoot = 3;
--    SandboxVars.CannedFoodLoot = 1;
    SandboxVars.RangedWeaponLoot = 5;
    SandboxVars.AmmoLoot = 5;
--    SandboxVars.SurvivalGearsLoot = 1;
--    SandboxVars.MechanicsLoot = 1;
--    SandboxVars.LiteratureLoot = 1;
--    SandboxVars.MedicalLoot = 1;
--    SandboxVars.OtherLoot = 4;
--    SandboxVars.Temperature = 3;
--    SandboxVars.Rain = 3;
--    SandboxVars.ErosionSpeed = 5
--    SandboxVars.XpMultiplier = "1.0";
--    SandboxVars.Farming = 3;
--    SandboxVars.NatureAbundance = 5;
--    SandboxVars.PlantResilience = 3;
--    SandboxVars.PlantAbundance = 3;
--    SandboxVars.Alarm = 3;
--    SandboxVars.LockedHouses = 3;
--    SandboxVars.FoodRotSpeed = 3;
--    SandboxVars.FridgeFactor = 3;
--    SandboxVars.LootRespawn = 1;
--    SandboxVars.StatsDecrease = 3;
--    SandboxVars.StarterKit = false;
--    SandboxVars.TimeSinceApo = 1;
--    SandboxVars.DecayingCorpseHealthImpact = 1
    SandboxVars.MultiHitZombies = false;
--
--    SandboxVars.ZombieConfig.PopulationMultiplier = 4.0

----    Events.OnZombieUpdate.Add(Kingsmouth.OnZombieUpdate);
--    Events.OnGameStart.Add(Kingsmouth.OnGameStart);
--
--    Events.EveryHours.Add(Kingsmouth.EveryHours);
--    Events.EveryDays.Add(Kingsmouth.EveryDays);
        --    Events.OnPlayerUpdate.Add(Kingsmouth.OnPlayerUpdate);
end

Kingsmouth.Render = function()

end

--[[
-- Example
function Kingsmouth.getSpawnRegion()
    local c = Kingsmouth
    return {
        {
            name = "Kingsmouth Region 1/2", points = {
                unemployed = {
                    { worldX = c.xcell, worldY = c.ycell, posX = c.x, posY = c.y, posZ = c.z },
                },
            }
        },
        {
            name = "Kingsmouth Region 2/2", points = {
                unemployed = {
                    { worldX = 101, worldY = 102, posX = 202, posY = 280, posZ = 1 },
                },
            }
        }
    }
end
--]]

--[[
-- Example
function Kingsmouth.getSpawnRegion()
    local c = Kingsmouth
    return {
        {
            name = "Kingsmouth Region 1/1", points = {
                unemployed = {
                    { worldX = c.xcell, worldY = c.ycell, posX = c.x, posY = c.y, posZ = c.z },
                    { worldX = 101, worldY = 102, posX = 202, posY = 280, posZ = 1 },
                }
            }
        }
    }
end
--]]

Kingsmouth.id = "Kingsmouth";
Kingsmouth.image = "media/lua/client/LastStand/Kingsmouth.png";
Kingsmouth.world = "challengemaps/Kingsmouth";
Kingsmouth.gameMode = "Kingsmouth";
Kingsmouth.xcell = 100;
Kingsmouth.ycell = 101;
Kingsmouth.x = 265;
Kingsmouth.y = 248;
Kingsmouth.z = 0;

Events.OnChallengeQuery.Add(Kingsmouth.Add)
