--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISBarricadeAction : ISBaseTimedAction
ISBarricadeAction = ISBaseTimedAction:derive("ISBarricadeAction");

function ISBarricadeAction:isValid()
	if not instanceof(self.item, "BarricadeAble") or self.item:getObjectIndex() == -1 then
		return false
	end
	local barricade = self.item:getBarricadeForCharacter(self.character)
	if self.isMetal then
		if barricade then
			return false
		end
		if not self.character:hasEquipped("BlowTorch") or not self.character:hasEquipped("SheetMetal") then
			return false
        end
    elseif self.isMetalBar then
        if barricade then
            return false
        end
        if not self.character:hasEquipped("BlowTorch") or not self.character:hasEquipped("MetalBar") then
            return false
        end
		if self.character:getInventory():getItemCount("Base.MetalBar", true) < 3 then
			return false
		end
	else
		if barricade and not barricade:canAddPlank() then
			return false
		end
		if not self.character:hasEquippedTag("Hammer") then
			return false
		end
		if not self.character:hasEquipped("Plank") then
			return false
		end
		if self.character:getInventory():getItemCount("Base.Nails", true) < 2 then
			return false
		end
	end
	if self.isStarted then
		if instanceof(self.item, "IsoDoor") or (instanceof(self.item, "IsoThumpable") and self.item:isDoor()) then
			if self.item:IsOpen() then
				return false
			end
		end
	end
	return true
end

function ISBarricadeAction:waitToStart()
	self.character:faceThisObject(self.item)
	return self.character:shouldBeTurning()
end

function ISBarricadeAction:update()
	self.character:faceThisObject(self.item)
    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISBarricadeAction:start()
    if self.character:hasEquipped("BlowTorch") then
        self:setActionAnim("BlowTorch")
        self:setOverrideHandModels(self.character:getPrimaryHandItem(), nil)
        self.sound = self.character:getEmitter():playSound("BlowTorch")
        local radius = 20 * self.character:getWeldingSoundMod()
        addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), radius, radius)
    else
        self:setActionAnim("Build")
        self.sound = self.character:getEmitter():playSound("Hammering")
        local radius = 20 * self.character:getHammerSoundMod()
        addSound(self.character, self.character:getX(), self.character:getY(), self.character:getZ(), radius, radius)
    end
    if instanceof(self.item, "IsoDoor") or (instanceof(self.item, "IsoThumpable") and self.item:isDoor()) then
        if self.item:IsOpen() then
            self.item:ToggleDoor(self.character)
        end
        self.isStarted = true
    end
end

function ISBarricadeAction:stop()
	if self.sound then
		self.character:getEmitter():stopSound(self.sound)
		self.sound = nil
	end
    ISBaseTimedAction.stop(self);
end

function ISBarricadeAction:perform()
    if self.sound then
        self.character:getEmitter():stopSound(self.sound)
        self.sound = nil
    end
	local material = self.character:getSecondaryHandItem()
	if not instanceof(material, "InventoryItem") then return end

	local obj = self.item
	local index = obj:getObjectIndex()
	local args = { x=obj:getX(), y=obj:getY(), z=obj:getZ(), index=index, isMetal=self.isMetal, isMetalBar=self.isMetalBar, itemID=material:getID(), condition=material:getCondition() }
	sendClientCommand(self.character, 'object', 'barricade', args)

	if self.isMetalBar or self.isMetal then
        self.sound = self.character:getEmitter():playSound("AddBarricadeMetal")
		self.character:getPrimaryHandItem():Use();
		self.character:getPrimaryHandItem():Use();
		self.character:getPrimaryHandItem():Use();
	end
	if self.character:getSecondaryHandItem() == material then
		self.character:setSecondaryHandItem(nil)
	end

    -- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISBarricadeAction:new(character, item, isMetal, isMetalBar, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = time;
	o.isMetal = isMetal;
    o.isMetalBar = isMetalBar;
	if character:HasTrait("Handy") then
		o.maxTime = time - 20;
    end
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end
    o.caloriesModifier = 8;
	return o;
end
