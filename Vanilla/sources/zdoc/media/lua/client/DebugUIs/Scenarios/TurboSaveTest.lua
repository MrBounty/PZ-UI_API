if debugScenarios == nil then
    debugScenarios = {}
end


debugScenarios.DebugScenario = {
    name = "Turbo Save Test",
    --forceLaunch = true, -- use this to force the launch of THIS scenario right after main menu was loaded, save more clicks! Don't do multiple scenarii with this options
    --      startLoc = {x=13538, y=5759, z=0 }, -- Mall
    --    startLoc = {x=10145, y=12763, z=0 }, -- lighting test
    --    startLoc = {x=3645, y=8564, z=0 }, -- Roadtrip start
    --          startLoc = {x=10645, y=10437, z=0 }, -- Police station
    --      startLoc = {x=13926, y=5877, z=0 }, -- Mall bookstore
    --    startLoc = {x=10862, y=10290, z=0 }, -- Mul
    --        startLoc = {x=10580,y=11193,z=0}, -- car crash
    --    startLoc = {x=11515, y=8830, z=0 }, -- DIxie gas station
    --    startLoc = {x=10657, y=10625, z=0 }, -- Muldraugh gas station
    --    startLoc = {x=5796, y=5384, z=0 }, -- Junkyard
    --    startLoc = {x=10835, y=10144, z=0 }, -- middle of muldraugh
    --    startLoc = {x=6476, y=5263, z=0 }, -- pharma
    --        startLoc = {x=10631, y=9750, z=0 }, -- fossoil
    --    startLoc = {x=5438, y=5886, z=0 }, -- gas2go
    --    startLoc = {x=10181, y=12783, z=0 }, -- laundromat
    --    startLoc = {x=8128, y=11729, z=1 }, -- fire dept lvl 1
    startLoc = {x=11699, y=6901, z=0 }, -- other 11989x6903
    setSandbox = function()
        SandboxVars.VehicleEasyUse = true;
        SandboxVars.Zombies = 6;
    end,
    onStart = function()
    end
}
