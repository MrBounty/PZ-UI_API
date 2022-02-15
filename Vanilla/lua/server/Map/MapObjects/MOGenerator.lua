--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

local function noise(message) print('MOGenerator.lua: '..message) end

local function ReplaceExistingObject(object, fuel, condition)
	local cell = getWorld():getCell()
	local square = object:getSquare()

	local item = InventoryItemFactory.CreateItem("Base.Generator")
	if item == nil then
		noise('Failed to create Base.Generator item')
		return
	end
	item:setCondition(condition)
	item:getModData().fuel = fuel

--	local index = object:getObjectIndex()
	square:transmitRemoveItemFromSquare(object)

	local javaObject = IsoGenerator.new(item, cell, square)
	-- IsoGenerator constructor calls AddSpecialObject, probably it shouldn't.
--	square:AddSpecialObject(javaObject, index)
	javaObject:transmitCompleteItemToClients()
end

local function NewGenerator(object)
	local fuel = 0
	local condition = 100
	ReplaceExistingObject(object, fuel, condition)
end

local PRIORITY = 5

MapObjects.OnNewWithSprite("appliances_misc_01_0", NewGenerator, PRIORITY)

