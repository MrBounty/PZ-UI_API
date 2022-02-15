require "ISBaseObject"
require "Reloading/ISReloadUtil"
require "Reloading/ISRackAction"
require "Reloading/ISReloadAction"

---@class ISReloadManager : ISBaseObject
ISReloadManager = ISBaseObject:derive("ISReloadManager");

--************************************************************************--
--** ISReloadable:initialise
--**
--************************************************************************--
function ISReloadManager:initialise()

end

--************************************************************************--
--** ISReloadManager:new
--**
--************************************************************************--
function ISReloadManager:new(player)
	local o = {}
	setmetatable(o, self)
    self.__index = self
	o.reloadAction = nil;
	o.reloadWeapon = nil;
	o.chainReload = false;
	o.spaceIsPressed = false;
	o.rIsPressed = false;
	--o.kIsPressed = false; -- debugging tool
	o.reloadAction = nil;
	o.rackingAction = nil;
	o.reloadable = nil;
	o.playerid = player;
	o.lastClickTime = 0

	-- Debug hooks
    --o.managerDetails = function()
	    --return ReloadManager:printManagerDetails();
	--end
	--Events.OnPlayerUpdate.Add(o.managerDetails);

    return o;
end

aaa = {}

aaa.startRackingHook = function(pl)
    if pl then
	    return ReloadManager[pl:getPlayerNum()+1]:checkRackConditions();
    else return ReloadManager[1]:checkRackConditions(); end
end
if not getCore():isNewReloading() then
	Events.OnPlayerUpdate.Add(aaa.startRackingHook);
end

aaa.startReloadHook = function(pl)
    if pl then
  	    return ReloadManager[pl:getPlayerNum()+1]:checkReloadConditions();
    else return ReloadManager[1]:checkReloadConditions(); end
end
if not getCore():isNewReloading() then
	Events.OnPlayerUpdate.Add(aaa.startReloadHook);
end

aaa.fireShotHook = function(wielder, weapon)
	return ReloadManager[wielder:getPlayerNum()+1]:fireShot(wielder, weapon);
end
if not getCore():isNewReloading() then
	Events.OnWeaponSwingHitPoint.Add(aaa.fireShotHook);
end

aaa.checkLoadedHook = function(character, chargeDelta)
	return ReloadManager[character:getPlayerNum()+1]:checkLoaded(character, chargeDelta);
end
if not getCore():isNewReloading() then
	Hook.Attack.Add(aaa.checkLoadedHook);
end



--************************************************************************--
--** ISReloadManager:checkLoaded
--**
--** Checks whether the DoAttackMethod may begin (i.e whether a weapon)
--** has a round loaded.
--**
--************************************************************************--
--************************************************************************--
function ISReloadManager:checkLoaded(character, chargeDelta)
    local weapon = character:getPrimaryHandItem();
    if ReloadUtil:setUpGun(weapon, character) then
        self.reloadable = ReloadUtil:getReloadableWeapon(weapon, character);
		if(self.reloadable:isLoaded(self:getDifficulty()) == true) then
			ISTimedActionQueue.clear(character)
			if(chargeDelta == nil) then
				character:DoAttack(0);
			else
				character:DoAttack(chargeDelta);
            end
        elseif self:rackingNow() then
			-- Don't interrupt the racking action
        elseif self:autoRackNeeded() then
			-- interrupt actions so racking can begin before firing
			ISTimedActionQueue.clear(character)
        else
            character:DoAttack(chargeDelta, true, self.reloadable.clickSound);
--	    elseif Calendar.getInstance():getTimeInMillis() - self.lastClickTime > 250 then
--		    getSoundManager():PlayWorldSound(self.reloadable.clickSound, character:getSquare(), 0, 4, 1.0, false);
--		    self.lastClickTime = Calendar.getInstance():getTimeInMillis()
        end
	else
        character:DoAttack(chargeDelta);
	end
    self.reloadable = nil;
end

--************************************************************************--
--** ISReloadManager:fireShot
--**
--** Checks whether weapon is reloadable and performs any necessary
--** actions after the weapon is fired
--**
--************************************************************************--
function ISReloadManager:fireShot(wielder, weapon, difficulty)
	self.reloadable = ReloadUtil:getReloadableWeapon(weapon, wielder);
	if(self.reloadable ~= nil) then
		self.reloadable:fireShot(weapon, self:getDifficulty());
	end
	self.reloadable = nil;
end

--************************************************************************--
--** ISReloadManager:isWeaponReloadable
--**
--** Performs various checks to make sure that the equipped item is
--** reloadable
--**
--************************************************************************--
function ISReloadManager:isWeaponReloadable()
	local playerObj = getSpecificPlayer(self.playerid)
	self.reloadWeapon = playerObj:getPrimaryHandItem();
	if(self.reloadWeapon == nil) then
        return false;
	end

	self.reloadable = ReloadUtil:getReloadableWeapon(self.reloadWeapon, playerObj);
	if self.reloadable == nil then return false; end
	local isReloadable = self.reloadable:canReload(playerObj);
	self.reloadable = nil;
    return isReloadable;
end

function ISReloadManager:reloadStarted()
	return ISTimedActionQueue.hasAction(self.reloadAction)
end

function ISReloadManager:rackingStarted()
	return ISTimedActionQueue.hasAction(self.rackingAction)
end

function ISReloadManager:rackingNow()
	local playerObj = getSpecificPlayer(self.playerid)
	if playerObj:getCharacterActions():isEmpty() then return false end
	return ISTimedActionQueue.hasAction(self.rackingAction) and self.rackingAction.action == playerObj:getCharacterActions():get(0)
end

function ISReloadManager:autoRackNeeded()
	local playerObj = getSpecificPlayer(self.playerid)
	if self:getDifficulty() == 3 and playerObj:getJoypadBind() == -1 then return false end
	return self.reloadable:canRack(playerObj)
end

--************************************************************************--
--** ISReloadManager:stopReload
--**
--** Resets variables after a cancelled reload timed action.
--**
--************************************************************************--
function ISReloadManager:stopReload(noSound)
	self.reloadAction.javaAction = nil;
	self.reloadWeapon = nil;
	self.reloadable = nil;
	self.chainReload = false;
end

--************************************************************************--
--** ISReloadManager:stopReloadSuccess
--**
--** Determines whether further reload actions should be performed and
--** resets variables after a reload timed action completes successfully.
--**
--************************************************************************--
function ISReloadManager:stopReloadSuccess()
	local playerObj = getSpecificPlayer(self.playerid)
	self.chainReload = ReloadUtil:getReloadableWeapon(self.reloadWeapon, playerObj):isChainReloading();
	if(self.reloadWeapon ~= nil and self.chainReload == true and self:reloadStarted() == true) then
		if(self.reloadable:canReload(playerObj)) then
			self:startReloading();
		else
			self:stopReload()
		end
	elseif(self.chainReload == false) then
		self:stopReload();
	end
end

--************************************************************************--
--** ISReloadManager:checkReloadConditions
--**
--** Checks for the reload key being pressed and starts the reload if
--** conditions are suitable
--**
--************************************************************************--
function ISReloadManager:checkReloadConditions()
	if self:reloadStarted() or self:rackingStarted() then return end
	local playerObj = getSpecificPlayer(self.playerid)
	local keyboard = self.playerid == 0
	if keyboard and self:getDifficulty() == 3 and isKeyDown(getCore():getKey("Rack Firearm")) then return end
	local reloadKey = keyboard and isKeyDown(getCore():getKey("ReloadWeapon"))
	local reloadButton = playerObj:isLBPressed() and not getFocusForPlayer(self.playerid)
	if not (reloadKey or reloadButton) then return end
	self.reloadWeapon = playerObj:getPrimaryHandItem();
	if self:isWeaponReloadable() == true then
		self:startReloading();
	end
end

--************************************************************************--
--** ISReloadManager:startReloading
--**
--** Sets up and starts the reload timed action
--**
--************************************************************************--
function ISReloadManager:startReloading()
	-- prep the reload information
	local player = getSpecificPlayer(self.playerid)
	local moodles = player:getMoodles();
	local panicLevel = moodles:getMoodleLevel(MoodleType.Panic);
	self.reloadable = ReloadUtil:getReloadableWeapon(self.reloadWeapon, player);
	self.reloadAction = ISReloadAction:new(self, player, player:getSquare(),
		(self.reloadable:getReloadTime()*player:getReloadingMod())+(panicLevel*30))
	if not self.chainReload then
        ISTimedActionQueue.clear(player)
    end
	ISTimedActionQueue.add(self.reloadAction)
end

--************************************************************************--
--** ISReloadManager:startReloadFromUi
--**
--** Sets up and starts the reload timed action from the UI
--**
--************************************************************************--
function ISReloadManager:startReloadFromUi(item)
	if(self:reloadStarted() == false and self:rackingStarted() == false) then
		self.reloadWeapon = item;
		self:startReloading();
	end
end

--************************************************************************--
--** ISReloadManager:checkRackConditions
--**
--** Checks for the rack key being pressed and starts racking if
--** conditions are suitable
--**
--************************************************************************--
function ISReloadManager:checkRackConditions()
	if self:reloadStarted() or self:rackingStarted() then return end
	local playerObj = getSpecificPlayer(self.playerid)
	local keyboard = self.playerid == 0
	if keyboard and isKeyDown(getCore():getKey("ReloadWeapon")) then return end
	if keyboard and isMouseButtonDown(0) then return end
	if keyboard and self:getDifficulty() == 3 and not (isKeyDown(getCore():getKey("Rack Firearm")) or playerObj:getJoypadBind() ~= -1) then return end
	self.reloadWeapon = playerObj:getPrimaryHandItem();
	if self.reloadWeapon == nil then
		return;
	end
	self.reloadable = ReloadUtil:getReloadableWeapon(self.reloadWeapon, playerObj);
	if self.reloadable ~= nil and self.reloadable:canRack(playerObj) then
		self:startRacking();
	else
		self:stopRacking();
	end
end

--************************************************************************--
--** ISReloadManager:startRackFromUi
--**
--** Sets up and starts the rack timed action from the UI
--**
--************************************************************************--
function ISReloadManager:startRackFromUi(item)
	if(self:reloadStarted() == false and self:rackingStarted() == false) then
		self.reloadWeapon = item;
		self:startRacking();
	end
end

--************************************************************************--
--** ISReloadManager:startRacking
--**
--** Sets up and starts the racking timed action
--**
--************************************************************************--
function ISReloadManager:startRacking()
	-- prep the racking information
	local player = getSpecificPlayer(self.playerid)
	local moodles = player:getMoodles();
	local panicLevel = moodles:getMoodleLevel(MoodleType.Panic);
	self.reloadable = ReloadUtil:getReloadableWeapon(self.reloadWeapon, player);
	self.rackingAction = ISRackAction:new(self, player, player:getSquare(),
		(self.reloadable:getRackTime()*player:getReloadingMod())+(panicLevel*5));
	ISTimedActionQueue.add(self.rackingAction)
end

--************************************************************************--
--** ISReloadManager:stopRacking
--**
--** Resets variables after a racking timed action.
--**
--************************************************************************--
function ISReloadManager:stopRacking()
	self.rackingAction = nil;
	self.reloadWeapon = nil;
end

--************************************************************************--
--** ISReloadManager:setDifficulty
--**
--** Sets the Difficulty to the value provided. Valid difficulties are:
--** 1 = EASY
--** 2 = MEDIUM
--** 3 = HARDCORE
--**
--** @param newDifficulty a number representing the new difficulty
--**
--************************************************************************--
function ISReloadManager:setDifficulty(newDifficulty)
	getCore():setOptionReloadDifficulty(newDifficulty);
end

--************************************************************************--
--** ISReloadManager:getDifficulty
--**
--** Gets the Difficulty value currently set. Will return a number between
--** 1 and 3 where each number corresponds with the following
--** difficulties:
--**
--** 1 = EASY
--** 2 = MEDIUM
--** 3 = HARDCORE
--**
--************************************************************************--
function ISReloadManager:getDifficulty()
	return getCore():getOptionReloadDifficulty()
end

--************************************************************************--
--** ISReloadManager:getDifficultyDescription
--**
--** Returns the text that should appear in the MainOptions screen to
--** describe the reload difficulty setting.
--**
--************************************************************************--
function ISReloadManager:getDifficultyDescription(difficulty)
	if(difficulty == 1) then
		return getText("UI_optionscreen_reloadEasy");
	elseif(difficulty == 3) then
		return getText("UI_optionscreen_reloadMedium");
	else
        return getText("UI_optionscreen_reloadHard");
	end
end

function ISReloadManager:printManagerDetails()
    if(isKeyDown(Keyboard.KEY_K) == true) then
        self.kIsPressed = true;
        print('***************************************************************');
        print('Reload Manager state');
        print('***************************************************************');
        local outString = '';
        if(self.reloadStarted ~= nil) then
            outString = outString..', reload Started: '
            if(self.reloadStarted == true) then
                outString = outString..'true';
            else
                outString = outString..'false';
            end
        else
            outString = outString..', reload Started == nil';
        end
        if(self.reloadWeapon ~= nil) then
            outString = outString..', reload Weapon: '..self.reloadWeapon;
        else
            outString = outString..', reload Weapon == nil';
        end
        if(self.rackingStarted ~= nil) then
            outString = outString..', rackingStarted: '
            if(self.rackingStarted == true) then
                outString = outString..'true';
            else
                outString = outString..'false';
            end
        else
            outString = outString..', racking Started = nil';
        end
        if(self.chainReload ~= nil) then
            outString = outString..', chain reloading: ';
            if(self.chainReload == true) then
                outString = outString..'true';
            else
                outString = outString..'false';
            end
        else
            outString = outString..', chain reloading == nil';
        end
        if(self.spaceIsPressed ~= nil) then
            outString = outString..', space Is Pressed: ';
            if(self.spaceIsPressed == true) then
                outString = outString..'true';
            else
                outString = outString..'false';
            end
        else
            outString = outString..', space Is pressed = nil';
        end
        if(self.rIsPressed ~= nil) then
            outString = outString..', r Is Pressed: ';
            if(self.rIsPressed == true) then
                outString = outString..'true';
            else
                outString = outString..'false';
            end
        else
            outString = outString..', r Is pressed = nil';
        end
        outString = outString..', difficulty: '..self:getDifficulty();
        print(outString);
        print('self.reloadable: ');
        print(self.relodable);
        print('Reload Action:')
        print(self.reloadAction)
        print('Racking Action:')
        print(self.rackingAction)
        print('***************************************************************');
        print();
        print();
        self:printReloadableDetails();
        self:printWeaponModDetails();
        self.kIsPressed = false;
	end
end

function ISReloadManager:printReloadableDetails()
        local weapon = getPlayer():getPrimaryHandItem();
        if(weapon ~= nil) then
            local reloadable = ReloadUtil:getReloadableWeapon(weapon, getSpecificPlayer(self.playerid));
            if(reloadable ~= nil) then
                reloadable:printReloadableWeaponDetails();
            end
        end
end

function ISReloadManager:printWeaponModDetails()
        local weapon = getPlayer():getPrimaryHandItem();
        if(weapon ~= nil) then
            local reloadable = ReloadUtil:getReloadableWeapon(weapon, getSpecificPlayer(self.playerid));
            if(reloadable ~= nil) then
                reloadable:printWeaponDetails(weapon);
            end
        end
end
