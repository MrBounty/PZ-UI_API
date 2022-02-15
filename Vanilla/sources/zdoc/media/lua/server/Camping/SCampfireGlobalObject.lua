--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Map/SGlobalObject"

---@class SCampfireGlobalObject : SGlobalObject
SCampfireGlobalObject = SGlobalObject:derive("SCampfireGlobalObject")

function SCampfireGlobalObject:new(luaSystem, globalObject)
	local o = SGlobalObject.new(self, luaSystem, globalObject)
	return o;
end

function SCampfireGlobalObject:initNew()
	self.exterior = true
	self.isLit = false
	self.fuelAmt = 0
	self.radius = -1
	self.spriteName = "camping_01_6"
end

function SCampfireGlobalObject:stateFromIsoObject(isoObject)
	local square = isoObject:getSquare()
	self:fromObject(isoObject)
	self.exterior = square:isOutside()
	self.spriteName = isoObject:getSpriteName() or ""
	self:processContainerItems()
	self.radius = -1
	self:changeFireLvl()
end

function SCampfireGlobalObject:stateToIsoObject(isoObject)
	local square = isoObject:getSquare()
	self.exterior = square:isOutside()
	self.spriteName = isoObject:getSpriteName() or ""
	self:toModData(isoObject:getModData())
	self:processContainerItems()
	self.radius = -1
	self:changeFireLvl()
end

function SCampfireGlobalObject:getObject()
	return self:getIsoObject()
end

function SCampfireGlobalObject:getContainer()
	local isoObject = self:getIsoObject()
	if not isoObject then return nil end
	return isoObject:getContainer()
end

function SCampfireGlobalObject:processContainerItems()
	local container = self:getContainer()
	if not container then return end
	container:addItemsToProcessItems()
end

function SCampfireGlobalObject:getFireObject()
	local square = self:getSquare()
	if square then
		for i = 0,square:getObjects():size()-1 do
			local fireObj = square:getObjects():get(i)
			if instanceof(fireObj, 'IsoFire') and fireObj:isPermanent() then
				return fireObj
			end
		end
	end
	return nil
end

function SCampfireGlobalObject:addObject()
	if self:getObject() then return end
	local square = self:getSquare()
	if not square then return end
	local isoObject = IsoObject.new(square, "camping_01_6", "Campfire")
	isoObject:setOutlineOnMouseover(true)
	self:toModData(isoObject:getModData())
	square:AddTileObject(isoObject)
	self:noise('added campfire IsoObject at index='..isoObject:getObjectIndex())
end

function SCampfireGlobalObject:removeObject()
	local square = self:getSquare()
	local isoObject = self:getIsoObject()
	if not square or not isoObject then return end
	square:transmitRemoveItemFromSquare(isoObject)
end

function SCampfireGlobalObject:setSpriteName(spriteName)
	if spriteName == self.spriteName then return end
	self:noise('changed campfire sprite from='..self.spriteName..' to '..spriteName..' '..self.x..','..self.y..','..self.z)
	self.spriteName = spriteName
	local isoObject = self:getIsoObject()
	if not isoObject then return end
	isoObject:setSprite(spriteName)
end

function SCampfireGlobalObject:addContainer()
	local square = self:getSquare()
	if not square then return end
	local isoObject = self:getIsoObject()
	if not isoObject then return end
	local container = isoObject:getContainer()
	if container then return end
	container = ItemContainer.new("campfire", square, isoObject, 1, 1)
	container:setExplored(true)
	isoObject:setContainer(container)
end

function SCampfireGlobalObject:addFireObject()
	if self:getFireObject() then return end
	local square = self:getSquare()
	if not square then return end
	-- Create a 'permanent' IsoFire object.  It won't spread to adjacent squares but will burn.
	local fireObj = IsoFire.new(getCell(), square)
	fireObj:AttachAnim("Fire", "01", 4, IsoFireManager.FireAnimDelay, -16, -78, true, 0, false, 0.7, IsoFireManager.FireTintMod)
	square:AddTileObject(fireObj)
	self:noise('added campfire IsoFire at index='..fireObj:getObjectIndex())
end

function SCampfireGlobalObject:removeFireObject()
	local fireObj = self:getFireObject()
	if not fireObj then return end
	local square = self:getSquare()
	self:noise('removed campfire IsoFire at index='..fireObj:getObjectIndex())
	square:transmitRemoveItemFromSquare(fireObj)
	square:getProperties():UnSet(IsoFlagType.burning)
end

function SCampfireGlobalObject:transferItemsToGround()
	local square = self:getSquare()
	local isoObject = self:getIsoObject()
	if not square or not isoObject then return end
	local container = isoObject:getContainer()
	if container then
		while container:getItems():size() > 0 do
			local item = container:getItems():get(0)
			container:DoRemoveItem(item)
			square:AddWorldInventoryItem(item, 0.0, 0.0, 0.0)
		end
	end
end

function SCampfireGlobalObject:fromModData(modData)
	self.exterior = modData.exterior
	self.isLit = modData.isLit or false
	self.fuelAmt = modData.fuelAmt
end

function SCampfireGlobalObject:toModData(modData)
	modData.exterior = self.exterior
	modData.isLit = self.isLit
	modData.fuelAmt = self.fuelAmt
end

function SCampfireGlobalObject:fromObject(isoObject)
	self:fromModData(isoObject:getModData())
end

function SCampfireGlobalObject:syncIsoObject()
	self.transmitObject = false
	if not self:getIsoObject() then
		self:addObject()
		self.transmitObject = true
	end
end

function SCampfireGlobalObject:syncSprite()
	self.transmitSprite = false
	local spriteName
	if not self.isLit then
		spriteName = "camping_01_6"
--	elseif self.fuelAmt <= 3 * 60 then
--		spriteName = "camping_01_4"
	else
		spriteName = "camping_01_5"
	end
	if spriteName ~= self.spriteName then
		self:setSpriteName(spriteName)
		self.transmitSprite = true
	end
end

function SCampfireGlobalObject:syncModData()
	local isoObject = self:getIsoObject()
	if isoObject then
		self:toModData(isoObject:getModData())
	end
end

function SCampfireGlobalObject:syncContainer()
	self.transmitContainer = false
	local isoObject = self:getIsoObject()
	local container = self:getContainer()
	if not container then
		self:addContainer()
		self.transmitContainer = isServer()
	elseif isoObject:getContainerCount() ~= 1 or container:getType() ~= "campfire" then
		for i=1,isoObject:getContainerCount() do
			container = isoObject:getContainerByIndex(i-1)
			container:removeItemsFromProcessItems()
			container:removeAllItems()
		end
		isoObject:removeAllContainers()
		self:addContainer()
		self.transmitContainer = isServer()
	end
	container = self:getContainer()
	local t
	if not self.isLit then
		t = 0
	elseif self.fuelAmt <= 3 * 60 then
		t = 1.7
	else
		t = 1.9
	end
	self.transmitContainerTemp = false
	if math.abs(t - container:getCustomTemperature()) > 0.01 then
		self:noise('container temp changed from '..container:getCustomTemperature()..' to '..t)
		container:setCustomTemperature(t)
		self.transmitContainerTemp = isServer()
	end
end

function SCampfireGlobalObject:syncIsoFire()
	self.transmitFire = false
	local fireObj = self:getFireObject()
	if self.isLit then
		if not fireObj then
			self:addFireObject()
			self.transmitFire = true
		end
	elseif fireObj then
		self:removeFireObject()
	end
end

function SCampfireGlobalObject:changeFireLvl()
	if self.fuelAmt <= 0 then
		self.isLit = false
	end

	local isoObject = self:getIsoObject()
	if not isoObject then
		self:saveData()
		return
	end

	local radius = self:fireRadius()
	if self.radius == radius then
		self:saveData()
		isoObject:transmitModData()
		return
	end
	self.radius = radius

	local gs = self:getSquare()
	self:noise('changeFireLvl x,y,z='..gs:getX()..','..gs:getY()..','..gs:getZ()..' fuelAmt='..self.fuelAmt..' isLit='..tostring(self.isLit)..' radius='..radius)

	self:syncIsoObject()
	self:syncSprite()
	self:syncModData()
	self:syncContainer()

	local isoObject = self:getIsoObject()
	if self.transmitObject then
		self:noise('transmit campfire object')
		isoObject:transmitCompleteItemToClients()
	else
		if self.transmitSprite then
			self:noise('transmit campfire sprite')
			isoObject:transmitUpdatedSpriteToClients()
		end
		self:noise('transmit campfire modData')
		isoObject:transmitModData()
		if self.transmitContainer then
			self:noise('transmit campfire container')
			isoObject:sendObjectChange('containers')
		elseif self.transmitContainerTemp then
			self:noise('transmit campfire container temperature')
			isoObject:sendObjectChange('container.customTemperature')
		end
	end
	
	self:syncIsoFire()

	local fireObj = self:getFireObject()
	if fireObj and self.transmitFire then
		fireObj:transmitCompleteItemToClients()
	end

	if fireObj and fireObj:getLightRadius() ~= self.radius then
		fireObj:setLightRadius(self.radius)
		if isServer() then
			self:noise('transmit campfire light radius')
			fireObj:sendObjectChange('lightRadius')
		end
	end

	self:saveData()
end

function SCampfireGlobalObject:fireRatio()
	if not self.isLit then return 0 end
	local maxFuel = 6 * 60
	return math.min(self.fuelAmt, maxFuel) / maxFuel
end

function SCampfireGlobalObject:fireRadius()
	if not self.isLit then return 0 end
	local minRadius = 2
	local maxRadius = 18
    if self.exterior then maxRadius = 10; end
	local maxFuel = 6 * 60
	return math.max(minRadius, maxRadius * math.min(self.fuelAmt, maxFuel) / maxFuel)
end

function SCampfireGlobalObject:saveData()
	self:noise('update object modData for campfire '..self.x..','..self.y..','..self.z)
	local isoObject = self:getIsoObject()
	if isoObject then
		self:toModData(isoObject:getModData())
	end
end

function SCampfireGlobalObject:lightFire()
	if self.isLit or self.fuelAmt <= 0 then return end
	self.isLit = true
	self:changeFireLvl()
end

function SCampfireGlobalObject:putOut()
	if not self.isLit then return end
	self.isLit = false
	self.fuelAmt = self.fuelAmt * 0.90
	self:changeFireLvl()
end

function SCampfireGlobalObject:addFuel(fuelAmt)
	self.fuelAmt = self.fuelAmt + fuelAmt
	self:changeFireLvl()
end

function SCampfireGlobalObject:setFuel(fuelAmt)
	self.fuelAmt = fuelAmt
	self:changeFireLvl()
end

