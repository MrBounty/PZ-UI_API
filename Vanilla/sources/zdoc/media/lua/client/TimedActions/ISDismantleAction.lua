--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "TimedActions/ISBaseTimedAction"

---@class ISDismantleAction : ISBaseTimedAction
ISDismantleAction = ISBaseTimedAction:derive("ISDismantleAction");

local function predicateNotBroken(item)
	return not item:isBroken()
end

function ISDismantleAction:isValid()
	return self.character:getInventory():containsTagEval("Saw", predicateNotBroken) and
			self.character:getInventory():containsTagEval("Screwdriver", predicateNotBroken) and
			self.thumpable:getObjectIndex() ~= -1
end

function ISDismantleAction:waitToStart()
	self.character:faceThisObject(self.thumpable)
	return self.character:shouldBeTurning()
end

function ISDismantleAction:update()
	self.character:PlayAnim("Idle")
	self.character:faceThisObject(self.thumpable)

    self.character:setMetabolicTarget(Metabolics.UsingTools);
end

function ISDismantleAction:start()
	self:setActionAnim("SawLog")
	self:setOverrideHandModels("Hacksaw", nil)
end

function ISDismantleAction:stop()
    ISBaseTimedAction.stop(self);
end

function ISDismantleAction:perform()
	-- we add the items contained inside the item we destroyed to put them randomly on the ground
	if self.thumpable:getContainer() and self.thumpable:getContainer():getItems() then
		for i=0,self.thumpable:getContainer():getItems():size()-1 do
			self.thumpable:getSquare():AddWorldInventoryItem(self.thumpable:getContainer():getItems():get(i), 0.0, 0.0, 0.0)
		end
	end

	for k,v in pairs(self.thumpable:getModData()) do
		if luautils.stringStarts(k, "need:") then
			local type = luautils.split(k, ":")[2]
			if type == "Base.Torch" then
				-- Big hack for ISLightSource
				self.thumpable:toggleLightSource(false)
				local item = InventoryItemFactory.CreateItem(type)
				if item then
					item:setUsedDelta(0)
					self.thumpable:getSquare():AddWorldInventoryItem(item, 0, 0, 0)
				end
				item = self.thumpable:removeCurrentFuel(nil)
				if item then
					self.thumpable:getSquare():AddWorldInventoryItem(item, 0, 0, 0)
				end
			else
				local count = ZombRand(tonumber(v))+1
				for i=1,count do
					self.thumpable:getSquare():AddWorldInventoryItem(type, 0, 0, 0)
				end
			end
		end
	end

	if isClient() then
		local sq = self.thumpable:getSquare()
		local args = { x = sq:getX(), y = sq:getY(), z = sq:getZ(), index = self.thumpable:getObjectIndex() }
		sendClientCommand(self.character, 'object', 'OnDestroyIsoThumpable', args)
	end

	-- Dismantle all 3 stair objects (and sometimes the floor at the top)
	local stairObjects = buildUtil.getStairObjects(self.thumpable)
	if #stairObjects > 0 then
		for i=1,#stairObjects do
			stairObjects[i]:getSquare():transmitRemoveItemFromSquare(stairObjects[i])
		end
	else
		self.thumpable:getSquare():transmitRemoveItemFromSquare(self.thumpable)
	end

	ISInventoryPage.dirtyUI();

	-- needed to remove from queue / start next.
	ISBaseTimedAction.perform(self);
end

function ISDismantleAction:new(character, thumpable)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.thumpable = thumpable;
	o.stopOnWalk = true;
	o.stopOnRun = true;
	o.maxTime = 200 - (character:getPerkLevel(Perks.Strength) * 10);
	if ISBuildMenu.cheat then o.maxTime = 1 end
    if character:isTimedActionInstant() then
        o.maxTime = 1;
    end
    o.caloriesModifier = 2;
	return o;
end
