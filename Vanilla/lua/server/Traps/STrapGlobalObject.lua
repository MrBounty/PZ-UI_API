--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Map/SGlobalObject"

STrapGlobalObject = SGlobalObject:derive("STrapGlobalObject")

function STrapGlobalObject:new(luaSystem, globalObject)
    local o = SGlobalObject.new(self, luaSystem, globalObject)
    return o;
end

function STrapGlobalObject:initNew()
    self.trapType = ""
    self.trapBait = ""
    self.trapBaitDay = 0
    self.lastUpdate = 0
    self.baitAmountMulti = 0
    self.animal = {}
    self.animalHour = 0
    self.openSprite = ""
    self.closedSprite = ""
    self.zone = "TownZone"
    self.player = "unknown"
    self.trappingSkill = 0
    self.destroyed = false;
end

function STrapGlobalObject:stateFromIsoObject(isoObject)
    local square = isoObject:getSquare()
    self:initNew()

    -- Compatibility: Data used to be stored in the square not the object
    if STrapSystem.isValidModData(square:getModData()) then
        self:noise('moving square modData to object')
        self:fromModData(square:getModData())
        self:reinitModData(square)
        self:toModData(isoObject:getModData())
    end

    self:fromModData(isoObject:getModData())

    self:setDef()
    if self.animal.type then
        isoObject:setSprite(self.closedSprite)
    else
        isoObject:setSprite(self.openSprite)
    end

    -- MapObjects-related code (see MOTrap.lua) might have changed the
    -- isoObject when it was loaded, we must sync with clients.
    if isServer() then
        isoObject:sendObjectChange('name')
        isoObject:sendObjectChange('sprite')
        isoObject:transmitModData()
    end
end

function STrapGlobalObject:stateToIsoObject(isoObject)
    local square = isoObject:getSquare()
    if STrapSystem.isValidModData(square:getModData()) then
        -- Compatibility: data used to be stored in the square not the object
        self:noise('removing square modData')
        self:reinitModData(square)
    end

    if self.destroyed then
        self:spawnDestroyItems(square, isoObject)
        -- FIXME: may not be safe to do here.
        self:removeIsoObject()
        return;
    end

    self:toModData(isoObject:getModData())

    if self.trapBait == "" then
        self.bait = nil
    else
        self.bait = self.trapBait
    end

    self:setDef()
    self.animal = self.animal or {}
    if self.animal.type then
        isoObject:setSprite(self.closedSprite)
    else
        isoObject:setSprite(self.openSprite)
    end

    if isServer() then
        isoObject:sendObjectChange('name')
        isoObject:sendObjectChange('sprite')
        isoObject:transmitModData()
    end
end

function STrapGlobalObject:calculTrap(square)
    self:setDef();
    if self.bait then
        self:checkForAnimal(square);
    end
    -- already something inside this trap
    if not self.animal.type then
        self:checkDestroy(square);
    end
end

function STrapGlobalObject:addSound(square)
    -- add sound to attract the surrounding zombies
    if self.animal.type and square then
        if ZombRand(4) == 0 then
            addSound(self:getIsoObject(), square:getX(),square:getY(),square:getZ(), 40, 10);
        end
        if self.animal.type == "bird" then
--            getSoundManager():PlayWorldSoundWav("PZ_BidTrapped_wood_0" .. ZombRand(3)+1, square, 0, 15, 2, true);
            if self:isMetalTrap() then
                square:playSound("BirdInMetalTrap", true);
            else
                square:playSound("BirdInWoodTrap", true);
            end
        else
            if self.trapType == "Base.TrapCrate" or self.trapType == "Base.TrapBox" or self.trapType == "Base.TrapStick" then
--                getSoundManager():PlayWorldSoundWav("PZ_AnimalTrapped_wood_0" .. ZombRand(4)+1, square, 0, 15, 2, true);
                square:playSound("AnimalInWoodTrap", true);
            elseif self.trapType =="Base.TrapCage" then
--                getSoundManager():PlayWorldSoundWav("PZ_AnimalTrappedMetal_0" .. ZombRand(3)+1, square, 0, 15, 2, true);
                square:playSound("AnimalInMetalTrap", true);
            end
        end
    end
end

function STrapGlobalObject:isMetalTrap()
    return false -- TODO
end

-- check if an animal stole your bait/destroy the trap (every hour)
function STrapGlobalObject:checkDestroy(square)
    if self.bait and ZombRand(self.def.trapStrength + 10) == 0 then
        self:removeBait();
    end
    -- slight chance of trap being destroying (by bigger animal for example)
    if (not square) and (ZombRand(40) == 0 and ZombRand(self.def.trapStrength) == 0) then
        self.destroyed = true;
    end
end

function STrapGlobalObject:removeBait(character)
    if self.bait then
--        print("remove bait");
--        if character then -- give the player his item
--            local bait = InventoryItemFactory.CreateItem(self.bait)
--            bait:setAge(self.trapBaitDay);
--            bait:multiplyFoodValues(-0.05 / bait:getBaseHunger())
--            if isServer() then
--                character:sendObjectChange('addItem', { item = bait })
--            else
--                character:getInventory():AddItem(bait)
--            end
--        end
        self.bait = nil;
        self.trapBait = ""
        self.trapBaitDay = 0;
        self:toObject(self:getIsoObject(), true);
    end
end

function STrapGlobalObject:removeAnimal(character)
    if character then
--        print("remove animal");
        local item = InventoryItemFactory.CreateItem(self.animal.item)
        -- Randomize the hunger reduction of the animal
        local size = ZombRand(self.animal.minSize, self.animal.maxSize);
        local xp = size / 3;
        if xp < 3 then
            xp = 3;
        end
        -- give xp only if you are the one who placed this trap);
        if self.player == character:getUsername() then
            if isServer() then
                character:sendObjectChange('addXp', { perk = Perks.Trapping:index(), xp = xp })
            else
                character:getXp():AddXP(Perks.Trapping, xp);
            end
        end

        local typicalSize  = item:getBaseHunger() * -100;
        local statsModifier = size / typicalSize;

        -- Set the animal's stats depending on the random size
        item:setBaseHunger(item:getBaseHunger() * statsModifier);
        item:setHungChange(item:getHungChange() * statsModifier);
        item:setActualWeight(item:getActualWeight() * statsModifier);

        item:setCarbohydrates(item:getCarbohydrates() * statsModifier);
        item:setLipids(item:getLipids() * statsModifier);
        item:setProteins(item:getProteins() * statsModifier);
        item:setCalories(item:getCalories() * statsModifier);

        if isServer() then
            character:sendObjectChange('addItem', { item = item })
        else
            character:getInventory():AddItem(item)
        end
    end
    self.animalHour = 0;
    self.animal = {};
    -- change the sprite of the item
    local trapObject = self:getIsoObject();
    if trapObject then
        trapObject:setSprite(self.openSprite);
        trapObject:transmitUpdatedSpriteToClients()
        self:toObject(trapObject, true)
    end
end

-- check if an animal is caught (every hour)
function STrapGlobalObject:checkForAnimal(square)
    -- you won't find an animal if a player is near the trap, so we check the trap only if it's streamed
    if square then
        -- (note turbo) if square~=nil do a check to see if theres any hoppables near the trap, this is an exploit to make traps invincible to zombie damage.
        -- when placing the trap it does check for hoppables, but a window frame could be placed afterwards.
        -- when this is the case, remove bait and animals if any.
        if self:checkForWallExploit(square) then
            self:removeAnimal();
            self:removeBait()
        end
        return;
    end
    if self.destroyed then
        return;
    end
    -- first, get which animal we'll attract
    local animalsList = {};
    for i,v in ipairs(Animals) do
        -- check if at this hour we can get this animal
        local timesOk = self:checkTime(v);
--        local timesOk = true;
        if v.traps[self.trapType] and
                v.baits[self.bait] and ZombRand(100) < (v.traps[self.trapType] + v.baits[self.bait] + (self.trappingSkill * 1.5)) and
                timesOk and v.zone[self.zone] and ZombRand(100) < (v.zone[self.zone] + (self.trappingSkill * 1.5)) then -- this animal can be caught by this trap and we have the correct bait for it
            -- now check if the bait is still fresh
            if self:checkBaitFreshness() then
--                print("can catch " .. v.type);
                table.insert(animalsList, v);
            end
        end
    end
    -- random an animal
    if #animalsList > 0 then
        local int = ZombRand(#animalsList) + 1;
        local testAnimal = animalsList[int];
        if testAnimal then
--            print("get animal : " .. testAnimal.type .. " in zone " .. self.zone);
            self:noise('trapped '..testAnimal.type..' '..self.x..','..self.y..','..self.z)
            self:setAnimal(testAnimal)
        end
    end
end

function STrapGlobalObject:setAnimal(animal)
    if not animal or not animal.type or not animal.item then
        self.noise('invalid animal for trap '..tostring(animal))
        return
    end
    self.animal = animal
    self.animalHour = 0
    self:removeBait()
    -- change the sprite of the item
    local trapObject = self:getIsoObject()
    if trapObject then
        self:toObject(trapObject, true)
        trapObject:setSprite(self.closedSprite)
        trapObject:transmitUpdatedSpriteToClients()
    end
end

function STrapGlobalObject:addBait(bait, age, baitAmountMulti, player)
    self.bait = bait
    self.trapBait = bait
    self.trapBaitDay = age
    self.lastUpdate = getGameTime():getWorldAgeHours() / 24;
    self.baitAmountMulti = baitAmountMulti;
    self.player = player:getUsername();
    local object = self:getIsoObject()
    self:toObject(object, true)
end

function STrapGlobalObject:setDef()
    if self.def then return; end
    for i,v in ipairs(Traps) do
        if v.type == self.trapType then
            self.def = v;
            break;
        end
    end
end

function STrapGlobalObject:toObject(object, transmitData)
    if object then
        self:toModData(object:getModData())
        if transmitData then
            object:transmitModData();
        end
    end
end

function STrapGlobalObject:fromModData(modData)
    self.trapType = modData["trapType"];
    self.trapBait = modData["trapBait"]
    if self.trapBait == "" then
        self.bait = nil;
    else
        self.bait = self.trapBait
    end
    self.trapBaitDay = modData["trapBaitDay"];
    self.lastUpdate = modData["lastUpdate"];
    self.baitAmountMulti = modData["baitAmountMulti"];
    self.animal = {}
    local animalType = modData["animal"];
    for i,v in ipairs(Animals) do
        if v.type == animalType then
            self.animal = v;
        end
    end
    self.animalHour = modData["animalHour"];
    self.openSprite = modData["openSprite"];
    self.closedSprite = modData["closedSprite"];
    self.zone = modData["zone"];
    self.player = modData["player"];
    self.trappingSkill = modData["trappingSkill"];
    self.destroyed = modData["destroyed"];
end

function STrapGlobalObject:toModData(modData)
    modData["trapType"] = self.trapType;
    modData["trapBait"] = self.trapBait or "";
    modData["trapBaitDay"] = self.trapBaitDay;
    modData["lastUpdate"] = self.lastUpdate;
    modData["baitAmountMulti"] = self.baitAmountMulti;
    modData["animal"] = self.animal and self.animal.type or nil;
    modData["animalHour"] = self.animalHour;
    modData["openSprite"] = self.openSprite;
    modData["closedSprite"] = self.closedSprite;
    modData["zone"] = self.zone;
    modData["player"] = self.player;
    modData["trappingSkill"] = self.trappingSkill;
    modData["destroyed"] = self.destroyed;
end

function STrapGlobalObject:checkBaitFreshness()
    local baitItem = InventoryItemFactory.CreateItem(self.bait);
    baitItem:setAge(self.trapBaitDay);
    return baitItem:isFresh();
end

function STrapGlobalObject:checkTime(animal)
    if animal.minHour == animal.maxHour then return true; end -- if min = max then it's 24/7
    local current = getGameTime():getHour()
    if animal.minHour < animal.maxHour then
        return animal.minHour <= current and animal.maxHour >= current
    else
        return animal.minHour <= current or animal.maxHour >= current
    end
end

function STrapGlobalObject:reinitModData(square)
    square:getModData()["trapType"] = nil;
    square:getModData()["trapBait"] = nil;
    square:getModData()["trapBaitDay"] = nil;
    square:getModData()["lastUpdate"] = nil;
    square:getModData()["baitAmountMulti"] = nil;
    square:getModData()["animal"] = nil;
    square:getModData()["animalHour"] = nil;
    square:getModData()["openSprite"] = nil;
    square:getModData()["closedSprite"] = nil;
    square:getModData()["zone"] = nil;
    square:getModData()["player"] = nil;
    square:getModData()["trappingSkill"] = nil;
    square:getModData()["destroyed"] = nil;
    square:transmitModdata()
end

function STrapGlobalObject:checkForWallExploit(square)
    if square then
        local north = getWorld():getCell():getGridSquare(square:getX(), square:getY()-1, square:getZ());
        local south = getWorld():getCell():getGridSquare(square:getX(), square:getY()+1, square:getZ());
        local east = getWorld():getCell():getGridSquare(square:getX()+1, square:getY(), square:getZ());
        local west = getWorld():getCell():getGridSquare(square:getX()-1, square:getY(), square:getZ());
        if square:isHoppableTo(north) or square:isHoppableTo(south) or square:isHoppableTo(east) or square:isHoppableTo(west) then
            return true;
        end
        if square:hasWindowOrWindowFrame() or (south and south:hasWindowOrWindowFrame()) or (east and east:hasWindowOrWindowFrame()) then
            return true;
        end
        return false;
    end
    return false;
end

function STrapGlobalObject:spawnDestroyItems(square, object)
    STrapGlobalObject.SpawnDestroyItems(self.trapType, square, object)
end

function STrapGlobalObject.SpawnDestroyItems(trapType, square, object)
    if square then
        local item = "Base.UnusableWood";

        --traptype might be nil, in that case check isoobject
        if (not trapType) and object and object:hasModData() then
            trapType = object:getModData()["trapType"];
        end

        if trapType then
            for i,v in ipairs(Traps) do
                if v.type == trapType and v.destroyItem then
                    item = v.destroyItem;
                    break;
                end
            end
        end

        if type(item)=="table" then
            for i,v in ipairs(item) do
                local spawnItem = square:AddWorldInventoryItem(v, ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
                if v=="Base.Twine" then
                    spawnItem:setUsedDelta(spawnItem:getUseDelta());
                end
            end
        else
            local spawnItem = square:AddWorldInventoryItem(item, ZombRand(0.1, 0.5), ZombRand(0.1, 0.5), 0);
            if item=="Base.Twine" then
                spawnItem:setUsedDelta(spawnItem:getUseDelta());
            end
        end
    else
        print("no square for spawnDestroyItems");
    end
end
