if debugScenarios == nil then
    debugScenarios = {}
end

debugScenarios.LotsaZombies = {
    name = "Carpentry",
    startLoc = {x=10745, y=9432, z=0 },
    onStart = function()

        getPlayer():LevelPerk(Perks.Woodwork);
        getPlayer():LevelPerk(Perks.Woodwork);
        getPlayer():LevelPerk(Perks.Woodwork);
        getPlayer():LevelPerk(Perks.Woodwork);
        getPlayer():LevelPerk(Perks.Woodwork);

        getPlayer():getInventory():AddItem("Base.Hammer");
        getPlayer():getInventory():AddItem("Base.NailsBox");
        getPlayer():getInventory():AddItem("Base.NailsBox");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");
        getPlayer():getInventory():AddItem("Base.Plank");

        SystemDisabler.setDoZombieCreation(false);

        SandboxVars.Zombies = 1;

        local other = createNPCPlayer( getPlayer():getX()-2, getPlayer():getY(), getPlayer():getZ());
        other:setDir(IsoDirections.E);
        getPlayer():setDir(IsoDirections.W);
    --        getPlayer():getDescriptor():getGroup():addMember(other:getDescriptor());

        --getPlayer():loadBehavior("normalBase");

    end
}
