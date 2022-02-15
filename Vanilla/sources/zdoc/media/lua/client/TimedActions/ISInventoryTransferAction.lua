require "TimedActions/ISBaseTimedAction"

---@class ISInventoryTransferAction : ISBaseTimedAction
ISInventoryTransferAction = ISBaseTimedAction:derive("ISInventoryTransferAction");

-- The PutItemInBag FMOD event duration is 10 seconds long, which stops it playing too frequently.
ISInventoryTransferAction.putSoundDelay = 9.5
ISInventoryTransferAction.putSoundTime = 0
-- keep only one instance of this action so we can queue item to transfer and avoid ton of instance when moving lot of items.

function ISInventoryTransferAction:isValid()
	self.dontAdd = false;
	if not self.destContainer or not self.srcContainer then return false; end
	if self.allowMissingItems and not self.srcContainer:contains(self.item) then -- if the item is destroyed before, for example when crafting something, we want to transfer the items left back to their original position, but some might be destroyed by the recipe (like molotov, the gas can will be returned, but the ripped sheet is destroyed)
--		self:stop();
		self.dontAdd = true;
		return true;
	end
	if (not self.destContainer:isExistYet()) or (not self.srcContainer:isExistYet()) then
		return false
	end

	local parent = self.srcContainer:getParent()
	-- Duplication exploit: drag items from a corpse to another container while pickup up the corpse.
	-- ItemContainer:isExistYet() would detect this if SystemDisabler.doWorldSyncEnable was true.
	if instanceof(parent, "IsoDeadBody") and parent:getStaticMovingObjectIndex() == -1 then
		return false
	end

	-- Don't fail if the item was transferred by a previous action.
	if self:isAlreadyTransferred(self.item) then
		return true
	end

	-- Limit items per container in MP
	if isClient() then
		local limit = getServerOptions():getInteger("ItemNumbersLimitPerContainer")
		if limit > 0 and (not instanceof(self.destContainer:getParent(), "IsoGameCharacter")) and self.destContainer:getItems():size()+1 > limit then
			return false
		end
	end

    if ISTradingUI.instance and ISTradingUI.instance:isVisible() then
        return false;
	end
	if not self.srcContainer:contains(self.item) then
		return false;
    end
    if self.srcContainer == self.destContainer then return false; end

    if self.destContainer:getType()=="floor" then
        if instanceof(self.item, "Moveable") and self.item:getSpriteGrid()==nil then
            if not self.item:CanBeDroppedOnFloor() then
                return false;
            end
        end
        if self:getNotFullFloorSquare(self.item) == nil then
            return false;
        end
    elseif not self.destContainer:hasRoomFor(self.character, self.item) then
        return false;
    end

    if not self.srcContainer:isRemoveItemAllowed(self.item) then
        return false;
    end
    if not self.destContainer:isItemAllowed(self.item) then
        return false;
    end
    if self.item:getContainer() == self.srcContainer and not self.destContainer:isInside(self.item) then
        return true;
    end
    if isClient() and self.srcContainer:getSourceGrid() and SafeHouse.isSafeHouse(self.srcContainer:getSourceGrid(), self.character:getUsername(), true) then
        return false;
	end
    return false;
end

function ISInventoryTransferAction:update()
	-- reopen the correct container
	if self.selectedContainer then
		if self.selectedContainer:getParent() then
			self.character:faceThisObject(self.selectedContainer:getParent())
		end
		if self.character:shouldBeTurning() then
			getPlayerLoot(self.character:getPlayerNum()):setForceSelectedContainer(self.selectedContainer)
		end
		getPlayerLoot(self.character:getPlayerNum()):selectButtonForContainer(self.selectedContainer)
	end
--    if self.updateDestCont then
--        self.destContainer:setSourceGrid(self.character:getCurrentSquare());
--    end
--
--    if self.updateSrcCont then
--        self.srcContainer:setSourceGrid(self.character:getCurrentSquare());
--    end
	self.item:setJobDelta(self.action:getJobDelta());

    self.character:setMetabolicTarget(Metabolics.LightWork);
end

function ISInventoryTransferAction:removeItemOnCharacter()
	self.character:removeAttachedItem(self.item)
	if not self.character:isEquipped(self.item) then return true end
	local addToWorld = self.character:removeFromHands(self.item)
	self.character:removeWornItem(self.item, false)
	triggerEvent("OnClothingUpdated", self.character)
	return addToWorld;
end

ISInventoryTransferAction.putSound = nil;

function ISInventoryTransferAction:doActionAnim(cont)
	-- for now we can't walk & do animations, so forget it for putting stuff on ground
--	if cont:getType()=="floor" then
--		return;
--	end

	if not self.stopOnWalk and (self.character:isPlayerMoving() or self.character:pressedMovement(false)) then
		self:setActionAnim("DropWhileMoving");
		return
	end

	self:setActionAnim("Loot");
	self:setAnimVariable("LootPosition", "");
	self:setOverrideHandModels(nil, nil);
	self.character:clearVariable("LootPosition");
	if cont:getContainerPosition() then
		self:setAnimVariable("LootPosition", cont:getContainerPosition());
	end
	if cont:getType() == "freezer" and cont:getFreezerPosition() then
		self:setAnimVariable("LootPosition", cont:getFreezerPosition());
	end
	if instanceof(cont:getParent(), "IsoDeadBody") or cont:getType() == "floor" then
		self:setAnimVariable("LootPosition", "Low");
	end
	if cont:getContainingItem() and cont:getContainingItem():getWorldItem() then
		self:setAnimVariable("LootPosition", "Low");
	end
	self.character:reportEvent("EventLootItem");
end

function ISInventoryTransferAction:startActionAnim()
	self.item:setJobType(getText("IGUI_MovingToContainer"));
	self.jobType = getText("IGUI_JobType_Grabbing", self.item:getName());

	if self.srcContainer == self.character:getInventory() then
		if self.destContainer:isInCharacterInventory(self.character) then
			self.item:setJobType(getText("IGUI_Packing"));
			self:setActionAnim("TransferItemOnSelf");
		else
			self.item:setJobType(getText("IGUI_PuttingInContainer"));
			self.selectedContainer = self.destContainer
			self:doActionAnim(self.destContainer);
		end
	elseif self.srcContainer:isInCharacterInventory(self.character) then
		if self.destContainer == self.character:getInventory() then
			self.item:setJobType(getText("IGUI_Unpacking"));
			self:setActionAnim("TransferItemOnSelf");
		else
			self.item:setJobType(getText("IGUI_TakingFromContainer"));
			self.selectedContainer = self.destContainer
			self:doActionAnim(self.destContainer);
		end
	elseif not self.srcContainer:isInCharacterInventory(self.character) then
		if self.destContainer:isInCharacterInventory(self.character) then
			self.item:setJobType(getText("IGUI_TakingFromContainer"));
			self.selectedContainer = self.srcContainer
			self:doActionAnim(self.srcContainer);
		elseif self.srcContainer:getType() == "floor" then
			self:doActionAnim(self.srcContainer);
		else
			self:doActionAnim(self.destContainer);
		end
	end
end

function ISInventoryTransferAction:start()
	if self:isAlreadyTransferred(self.item) then
		self.selectedContainer = nil
		self.action:setTime(0)
		return
	end

	if self.dontAdd then
		self.selectedContainer = nil
		self.action:setTime(0)
		return
	end

	if self.character:isPlayerMoving() then
		self.maxTime = self.maxTime * 1.5
		self.action:setTime(self.maxTime)
	end

    -- stop microwave working when putting new stuff in it
    if self.destContainer and self.destContainer:getType() == "microwave" and self.destContainer:getParent() and self.destContainer:getParent():Activated() then
        self.destContainer:getParent():setActivated(false);
    end
    if self.srcContainer and self.srcContainer:getType() == "microwave" and self.srcContainer:getParent() and self.srcContainer:getParent():Activated() then
        self.srcContainer:getParent():setActivated(false);
    end
--    if self.destContainer:getPutSound() then
    if not ISInventoryTransferAction.putSound or not self.character:getEmitter():isPlaying(ISInventoryTransferAction.putSound) then
        -- Players with the Deaf trait don't play sounds.  In multiplayer, we mustn't send multiple sounds to other clients.
        if ISInventoryTransferAction.putSoundTime + ISInventoryTransferAction.putSoundDelay < getTimestamp() then
            ISInventoryTransferAction.putSoundTime = getTimestamp()
            ISInventoryTransferAction.putSound = self.character:getEmitter():playSound("PutItemInBag")
        end
    end
--    end

	self:startActionAnim()
end

function ISInventoryTransferAction:stop()
	self.item:setJobDelta(0.0);
	if self.action then
		self.action:setLoopedAction(false);
	end
    ISBaseTimedAction.stop(self);
end

function ISInventoryTransferAction:forceComplete()
	self.item:setJobDelta(0.0);
	if self.action then
		self.action:setLoopedAction(false);
	end
	ISBaseTimedAction.perform(self);
end

function ISInventoryTransferAction:forceStop()
	self.item:setJobDelta(0.0);
	if self.action then
		self.action:setLoopedAction(false);
	end
	ISBaseTimedAction.stop(self);
end

function ISInventoryTransferAction:perform()
	-- I would have done this in start(), however the first action added to the queue
	-- is started immediately, before any other actions can be added.
	self:checkQueueList()

	self.item:setJobDelta(0.0)
--	print("perform on item", self.item, self.item:getDisplayName())
	-- take the next item in our queue list
	local queuedItem = table.remove(self.queueList, 1);
	-- reopen the correct container
	if self.selectedContainer then
		getPlayerLoot(self.character:getPlayerNum()):selectButtonForContainer(self.selectedContainer)
	end

	for i,item in ipairs(queuedItem.items) do
		self.item = item
		-- Check destination container capacity and item-count limit.
		if not self:isValid() then
			self.queueList = {}
			break
		end
		self:transferItem(item);
	end
	-- if we still have other item to transfer in our queue list, we "reset" the action
	if #self.queueList > 0 then
		queuedItem = self.queueList[1]
		self.item = queuedItem.items[1];
--		print("reset with new item: ", queuedItem.items[1], #queuedItem.items)
		local time = queuedItem.time;
		if self:isAlreadyTransferred(self.item) then
			time = 0
		end
		self.maxTime = time
		self.action:setTime(tonumber(time))
		self:resetJobDelta();
		self:startActionAnim()
	else
		self.action:stopTimedActionAnim();
		self.action:setLoopedAction(false);

		if self.onCompleteFunc then
			local args = self.onCompleteArgs
			self.onCompleteFunc(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8])
		end

		-- needed to remove from queue / start next.
		ISBaseTimedAction.perform(self);
	end

	if instanceof(self.item, "Radio") then
		self.character:updateEquippedRadioFreq();
	end
end

function ISInventoryTransferAction:isAlreadyTransferred(item)
	return self.destContainer:contains(item)
end

function ISInventoryTransferAction:floorHasRoomFor(square, item)
	local capacity = self.destContainer:getEffectiveCapacity(self.character)
	local totalWeight = square:getTotalWeightOfItemsOnFloor()
	if totalWeight >= capacity then
		return false
	end
	if ItemContainer.floatingPointCorrection(totalWeight) + item:getUnequippedWeight() <= capacity then
		return true
	end
	-- Hack for single bag greater than 50 weight
	if item:getUnequippedWeight() >= capacity then
		return true
	end
	return false
end

function ISInventoryTransferAction:canDropOnFloor(square)
	if not square then
		return false
	end
	if not square:TreatAsSolidFloor() then
		return false
	end
	if square:isSolid() or square:isSolidTrans() then
		return false
	end
	local current = self.character:getCurrentSquare()
	if current ~= nil and square ~= current then
		if current:isBlockedTo(square) or current:isWindowTo(square) then
			return false
		end
		if current:HasStairs() ~= square:HasStairs() then
			return false
		end
		if current:HasStairs() and not current:isSameStaircase(square:getX(), square:getY(), square:getZ()) then
			return false
		end
	end
	return true
end

function ISInventoryTransferAction:getNotFullFloorSquare(item)
	local square = self.character:getCurrentSquare()
	if self:canDropOnFloor(square) and self:floorHasRoomFor(square, item) then
		return square
	end
	local cx = self.character:getX()
	local cy = self.character:getY()
	local cz = self.character:getZ()
	local capacity = self.destContainer:getEffectiveCapacity(self.character)
	for dy=-1,1 do
		for dx=-1,1 do
			if dx ~= 0 or dy ~= 0 then
				local square = getCell():getGridSquare(cx + dx, cy + dy, cz)
				if self:canDropOnFloor(square) and self:floorHasRoomFor(square, item) then
					return square
				end
			end
		end
	end
	return nil
end

function ISInventoryTransferAction.GetDropItemOffset(character, square, item)
	local dropX = character:getX() - math.floor(character:getX())
	local dropY = character:getY() - math.floor(character:getY())
	local dropZ = character:getZ() - math.floor(character:getZ())
	if character:isSeatedInVehicle() then
		dropZ = math.floor(character:getZ())
	end
	if (square ~= character:getCurrentSquare()) or getCore():getOptionDropItemsOnSquareCenter() then
		dropX = ZombRand(3, 7) / 10.0
		dropY = ZombRand(3, 7) / 10.0
		dropZ = square:getApparentZ(dropX, dropY) - square:getZ()
	end
	return dropX,dropY,dropZ
end

-- TODO: function to remove a list of items on server
function ISInventoryTransferAction:transferItem(item)
	if self:isAlreadyTransferred(item) then
		return
	end

	if self.dontAdd then
		-- Crafting ingredient was destroyed and can't be put back into the container it came from.
		return
	end

--	print("transfering ", item)
	self.item = item;
	--self.character:ClearVariable("LootPosition");
	self.item:setJobDelta(0.0);
	if self.item:isFavorite() and not self.destContainer:isInCharacterInventory(self.character) then
--		ISBaseTimedAction.perform(self);
		return;
	end

	if self.srcContainer:getType() ~= "TradeUI" and isClient() and not self.destContainer:isInCharacterInventory(self.character) and self.destContainer:getType()~="floor" then
		self.destContainer:addItemOnServer(self.item);
	end
	if self.srcContainer:getType() ~= "TradeUI" and isClient() and not self.srcContainer:isInCharacterInventory(self.character) and self.srcContainer:getType()~="floor" then
		self.srcContainer:removeItemOnServer(self.item);
	end
	
	if self.destContainer:getType() ~= "TradeUI" then
		self.srcContainer:DoRemoveItem(self.item);
	end
	-- clear it from the queue.
	self.srcContainer:setDrawDirty(true);
	self.srcContainer:setHasBeenLooted(true);
	self.destContainer:setDrawDirty(true);

	-- deal with containers that are floor
	if self.destContainer:getType()=="floor" then
		local square = self:getNotFullFloorSquare(item)
		if square then
			local addToWorld = self:removeItemOnCharacter();
			-- might have been added by the masking system (if you have a bag equipped and drop it on ground for example)
			if addToWorld then
				self.destContainer:DoAddItemBlind(self.item);
				local dropX,dropY,dropZ = ISInventoryTransferAction.GetDropItemOffset(self.character, square, self.item)
				square:AddWorldInventoryItem(item, dropX, dropY, dropZ)
			end
		else
			error "no square to drop item on"
		end
	elseif self.srcContainer:getType()=="floor" and self.item:getWorldItem() ~= nil then
		self.item:getWorldItem():getSquare():transmitRemoveItemFromSquare(self.item:getWorldItem());
		self.item:getWorldItem():getSquare():removeWorldObject(self.item:getWorldItem());
		--        self.item:getWorldItem():getSquare():getObjects():remove(self.item:getWorldItem());
		self.item:setWorldItem(nil);
		self.destContainer:AddItem(self.item);
	else
		if self.srcContainer:getType() ~= "TradeUI" then
			self.destContainer:AddItem(self.item);
		end
		if self.character:getInventory() ~= self.destContainer then
			self:removeItemOnCharacter();
		end
		if self.item:getType() == "CandleLit" then
			local candle = self.destContainer:AddItem("Base.Candle");
			candle:setUsedDelta(self.item:getUsedDelta());
			candle:setCondition(self.item:getCondition());
			candle:setFavorite(self.item:isFavorite());
			self.destContainer:Remove(self.item)
			self.item = candle;
		end
	end

	if self.destContainer:getParent() and instanceof(self.destContainer:getParent(), "BaseVehicle") and self.destContainer:getParent():getPartById(self.destContainer:getType()) then
		local part = self.destContainer:getParent():getPartById(self.destContainer:getType());
		part:setContainerContentAmount(part:getItemContainer():getCapacityWeight());
	end

	if self.srcContainer:getParent() and instanceof(self.srcContainer:getParent(), "BaseVehicle") and self.srcContainer:getParent():getPartById(self.srcContainer:getType()) then
		local part = self.srcContainer:getParent():getPartById(self.srcContainer:getType());
		part:setContainerContentAmount(part:getItemContainer():getCapacityWeight());
	end

	if instanceof(self.srcContainer:getParent(), "IsoDeadBody") then
		self.item:setAttachedSlot(-1);
		self.item:setAttachedSlotType(nil);
		self.item:setAttachedToModel(nil);
	end

	if instanceof(self.destContainer:getParent(), 'IsoMannequin') then
		local mannequin = self.destContainer:getParent()
		mannequin:wearItem(self.item, self.character)
	end
	
	-- Hack for giving cooking XP.
	if instanceof(self.item, "Food") then
		self.item:setChef(self.character:getFullName())
	end
	if self.destContainer:getType() == "stonefurnace" then
		self.item:setWorker(self.character:getFullName());
	end
	
	local pdata = getPlayerData(self.character:getPlayerNum());

	ISInventoryPage.renderDirty = true
--	pdata.playerInventory:refreshBackpacks();
--	pdata.lootInventory:refreshBackpacks();
	
	-- do the overlay sprite
	if not isClient() then
		if self.srcContainer:getParent() and self.srcContainer:getParent():getOverlaySprite() then
			ItemPicker.updateOverlaySprite(self.srcContainer:getParent())
		end
		if self.destContainer:getParent() then
			ItemPicker.updateOverlaySprite(self.destContainer:getParent())
		end
	end
end

function ISInventoryTransferAction:setOnComplete(func, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
	self.onCompleteFunc = func
	self.onCompleteArgs = { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8 }
end

function ISInventoryTransferAction:getTimeDelta()
	return 0;
end

function ISInventoryTransferAction:canMergeAction(action)
	if not action then return false end
	if action.Type ~= self.Type then return false end
	if action.srcContainer ~= self.srcContainer then return false end
	if action.destContainer ~= self.destContainer then return false end
	if action.onCompleteFunc or self.onCompleteFunc then return false end
	if action.allowMissingItems ~= self.allowMissingItems then return false end
	return true
end

function ISInventoryTransferAction:checkQueueList()
	local actionQueue = ISTimedActionQueue.getTimedActionQueue(self.character)
	local indexSelf = actionQueue:indexOf(self)
	if indexSelf == -1 then return end
	local index = indexSelf + 1
	while index <= #actionQueue.queue do
		local action = actionQueue.queue[index]
		if not self:canMergeAction(action) then
			break
		end
		local typeAlreadyExist = false
		-- we check if in our queue list an item with the same type exist, so we can transfer them in bulk
		-- limit this to 20 items (so transfer 20 per 20 nails)
		-- only for item with weight < 0.1
		if round(action.item:getWeight(), 3) <= 0.1 then
			for i,v in ipairs(self.queueList) do
				if v.type == action.item:getFullType() and #v.items < 20 then
	--				print("found same type in list", action.item:getFullType())
					table.insert(v.items, action.item)
					typeAlreadyExist = true
					break
				end
			end
		end
		-- add the current item to our queue list
		if not typeAlreadyExist then
--			print("adding to queue", action.item, action.item:getDisplayName())
			table.insert(self.queueList, {items = {action.item}, time = action.maxTime, type = action.item:getFullType()})
		end
		table.remove(actionQueue.queue, index)
		table.wipe(action)
	end
end

-- This is to support returning crafting ingredients to the container they were originally in.
-- When true, this action does not fail if the item is no longer in the source container.
function ISInventoryTransferAction:setAllowMissingItems(allow)
	self.allowMissingItems = allow
end

function ISInventoryTransferAction:new (character, item, srcContainer, destContainer, time)
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.character = character;
	o.item = item;
	o.dontAdd = false;
	o.srcContainer = srcContainer;
	o.destContainer = destContainer;
	-- handle people right click the same item while eating it
	if not srcContainer or not destContainer then
		o.maxTime = 0;
		return o;
	end
--	o.stopOnWalk = ((not o.destContainer:isInCharacterInventory(o.character)) or (not o.srcContainer:isInCharacterInventory(o.character))) and (o.destContainer:getType() ~= "floor");
--	o.stopOnRun = o.destContainer:getType() ~= "floor";
	o.stopOnWalk = not o.destContainer:isInCharacterInventory(o.character) or (not o.srcContainer:isInCharacterInventory(o.character))
	if (o.srcContainer == character:getInventory()) and (o.destContainer:getType() == "floor") then
		o.stopOnWalk = false
	end
	o.stopOnRun = true;
    if destContainer:getType() ~= "TradeUI" and srcContainer:getType() ~= "TradeUI" then
        o.maxTime = 120;
        -- increase time for bigger objects or when backpack is more full.
        local destCapacityDelta = 1.0;

        if o.srcContainer == o.character:getInventory() then
            if o.destContainer:isInCharacterInventory(o.character) then
           --     self.item:setJobType("Packing");
                destCapacityDelta = o.destContainer:getCapacityWeight() / o.destContainer:getMaxWeight();
            else
             --   self.item:setJobType("Putting in container");
                o.maxTime = 50;
            end

        elseif not o.srcContainer:isInCharacterInventory(o.character) then
            if o.destContainer:isInCharacterInventory(o.character) then
             --   self.item:setJobType("Taking from container");
                o.maxTime = 50;
            end
        end

        if destCapacityDelta < 0.4 then
            destCapacityDelta = 0.4;
        end


        local w = item:getActualWeight();
        if w > 3 then w = 3; end;
        o.maxTime = o.maxTime * (w) * destCapacityDelta;

        if getCore():getGameMode()=="LastStand" then
            o.maxTime = o.maxTime * 0.3;
        end

        if o.destContainer:getType()=="floor" then
			if o.srcContainer == o.character:getInventory() then
				o.maxTime = o.maxTime * 0.1;
			elseif o.srcContainer:isInCharacterInventory(o.character) then
				-- Unpack -> drop
			else
				o.maxTime = o.maxTime * 0.2;
			end
		end

		if character:HasTrait("Dextrous") then
			o.maxTime = o.maxTime * 0.5
		end
		if character:HasTrait("AllThumbs") then
			o.maxTime = o.maxTime * 4.0
		end
    else
        o.maxTime = 0;
    end
    if time then
        o.maxTime = time;
    end
	if character:isTimedActionInstant() then
		o.maxTime = 1;
	end

    if item:isFavorite() and not o.destContainer:isInCharacterInventory(o.character) then o.maxTime = 0; end

	o.queueList = {};
	local queuedItem = {items = {o.item}, time = o.maxTime, type = o.item:getFullType()};
	table.insert(o.queueList, queuedItem);
	o.loopedAction = true

    return o
end


function javaTransferItems(character, item, srcContainer, destContainer)

    if instanceof(destContainer, "InventoryContainer") then
        destContainer = destContainer:getInventory()
    end
    if instanceof(srcContainer, "InventoryContainer") then
        srcContainer = srcContainer:getInventory()
    end


    if srcContainer == nil then
        return;
    end

    if not srcContainer:contains(item) then
        return;
    end

    if srcContainer == destContainer then return; end

    if destContainer:getCapacity() < (destContainer:getCapacityWeight() + item:getActualWeight()) then
        return;
    end

    if item:getContainer() ~= srcContainer or destContainer:isInside(item) or not destContainer:hasRoomFor(item) then
        return;
    end

    ISTimedActionQueue.add(ISInventoryTransferAction:new(character, item, srcContainer, destContainer, 10));

end

