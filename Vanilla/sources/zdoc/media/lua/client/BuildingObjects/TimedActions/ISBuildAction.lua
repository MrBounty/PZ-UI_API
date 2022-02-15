--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISBuildAction : ISBaseTimedAction
ISBuildAction = ISBaseTimedAction:derive("ISBuildAction");

-- The FMOD events have approx. 10-second duration even though the sounds are shorter.
ISBuildAction.soundDelay = 6

function ISBuildAction:isValid()
    local plSquare = self.character:getSquare();
    if plSquare and self.square then
        if self.square:isSomethingTo(plSquare) or self.square:getZ() ~= plSquare:getZ() then
            self:stop();
            return false;
        end;
    else
        self:stop();
        return false;
    end;
    ---
    if not self.item.noNeedHammer and self.hammer then
		return self.hammer:getCondition() > 0;
	end
    return true;
end

function ISBuildAction:waitToStart()
	if ISBuildMenu.cheat then return false end
	self:faceLocation()
	return self.character:shouldBeTurning()
end

function ISBuildAction:update()
    local worldSoundRadius = 0
    -- Players with the Deaf trait don't play sounds.  In multiplayer, we mustn't send multiple sounds to other clients.
    if self.soundTime + ISBuildAction.soundDelay < getTimestamp() then
        self.soundTime = getTimestamp()
        if not self.item.noNeedHammer then
            local playingSaw = self.sawSound ~= 0 and self.character:getEmitter():isPlaying(self.sawSound)
            local playingHammer = self.hammerSound ~= 0 and self.character:getEmitter():isPlaying(self.hammerSound)
            if not playingSaw and not playingHammer then
                if self.doSaw == true and self.character:getInventory():containsTag("Saw") then
                    self.sawSound = self.character:getEmitter():playSound("Sawing");
                    worldSoundRadius = 15
                    self.doSaw = false;
                else
                    self.hammerSound = self.character:getEmitter():playSound("Hammering");
                    worldSoundRadius = math.ceil(20 * self.character:getHammerSoundMod())
                    self.doSaw = true;
                end
            end
        end
        if self.craftingBank then
            local playingCrafting = self.craftingSound ~= 0 and self.character:getEmitter():isPlaying(self.craftingSound)
            if not playingCrafting then
                self.craftingSound = self.character:getEmitter():playSound(self.craftingBank);
            end
            worldSoundRadius = 15
        end
    end
    if worldSoundRadius > 0 then
        ISBuildAction.worldSoundTime = getTimestamp()
        addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), worldSoundRadius, worldSoundRadius)
    end

    self.character:setMetabolicTarget(Metabolics.HeavyWork);

    self:faceLocation();
end

function ISBuildAction:start()
    if not self.item.noNeedHammer then
        self.sawSound = 0
		self.hammer = self.character:getPrimaryHandItem();
        self.hammerSound = 0
    end
    if self.craftingBank then
        self.craftingSound = 0
	end

	self.soundTime = 0

	self.item.ghostSprite = IsoSprite.new();
	self.item.ghostSprite:LoadFramesNoDirPageSimple(self.spriteName);
	self.item.ghostSpriteX = self.x;
	self.item.ghostSpriteY = self.y;
	self.item.ghostSpriteZ = self.z;

	self.item:onTimedActionStart(self)
	
--	self.character:reportEvent("EventCrafting");
end

function ISBuildAction:stop()
	self.item:onTimedActionStop(self)
	self.item.ghostSprite = nil;
    if self.sawSound and self.sawSound ~= 0 and self.character:getEmitter():isPlaying(self.sawSound) then
        self.character:getEmitter():stopSound(self.sawSound);
    end
    if self.hammerSound and self.hammerSound ~= 0 and self.character:getEmitter():isPlaying(self.hammerSound) then
        self.character:getEmitter():stopSound(self.hammerSound);
    end
    if self.craftingSound and self.craftingSound ~= 0 and self.character:getEmitter():isPlaying(self.craftingSound) then
        self.character:getEmitter():stopSound(self.craftingSound);
    end
    ISBaseTimedAction.stop(self);
end

function ISBuildAction:perform()
	self.item.ghostSprite = nil;
    if self.sawSound and self.sawSound ~= 0 and self.character:getEmitter():isPlaying(self.sawSound) then
        self.character:getEmitter():stopSound(self.sawSound);
    end
    if self.hammerSound and self.hammerSound ~= 0 and self.character:getEmitter():isPlaying(self.hammerSound) then
        self.character:getEmitter():stopSound(self.hammerSound);
    end
    if self.craftingSound and self.craftingSound ~= 0 and self.character:getEmitter():isPlaying(self.craftingSound) then
        self.character:getEmitter():stopSound(self.craftingSound);
    end
    -- reduce the condition of the hammer if it's a stone hammer
    local hammer = self.character:getPrimaryHandItem()
    if hammer and hammer:getType() == "HammerStone" and ZombRand(hammer:getConditionLowerChance()) == 0 then
        hammer:setCondition(hammer:getCondition() - 1)
        ISWorldObjectContextMenu.checkWeapon(self.character);
    end

    self.item.character = self.character;
	self.item:create(self.x, self.y, self.z, self.north, self.spriteName);
    self.square:RecalcAllWithNeighbours(true);
    if self.item.completionSound ~= nil and self.item.completionSound ~= "" then
        self.character:playSound(self.item.completionSound)
    end
	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISBuildAction:faceLocation()
	if self.item.isWallLike then
		if self.item.north then
			self.character:faceLocationF(self.x + 0.5, self.y)
		else
			self.character:faceLocationF(self.x, self.y + 0.5)
		end
	else
		self.character:faceLocation(self.x, self.y)
	end
end

function ISBuildAction:new(character, item, x, y, z, north, spriteName, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.x = x;
	o.y = y;
	o.z = z;
	o.north = north;
	o.spriteName = spriteName;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
    o.craftingBank = item.craftingBank;
	if character:HasTrait("Handy") then
		o.maxTime = time - 50;
    end
--    o.maxTime = 500;
    o.square = getCell():getGridSquare(x,y,z);
    o.doSaw = false;
    o.caloriesModifier = 8;
	return o;
end
