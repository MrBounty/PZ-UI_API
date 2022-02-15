--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class RecipeUtils
RecipeUtils = {}

function RecipeUtils.CreateSourceItem1(recipe, source, sourceFullType, options)
	local item = nil
	if sourceFullType == "Water" then
		item = InventoryItemFactory.CreateItem("Base.WaterBottleFull")
		item:setUsedDelta(item:getUseDelta()) -- a single use
	elseif source:isDestroy() then
		item = InventoryItemFactory.CreateItem(sourceFullType)
	elseif getScriptManager():isDrainableItemType(sourceFullType) then
		item = InventoryItemFactory.CreateItem(sourceFullType)
		item:setUsedDelta(item:getUseDelta()) -- a single use
	elseif source:getUse() > 0 then
		item = InventoryItemFactory.CreateItem(sourceFullType)
		if not instanceof(item, "Food") then error(sourceFullType..' is not food') end
		item:setHungChange(-source:getUse() / 100)
	else
		item = InventoryItemFactory.CreateItem(sourceFullType)
	end
	if recipe:getOriginalname() == "Insert Battery into Flashlight" and sourceFullType == "Base.Torch" then
		item:setUsedDelta(0.0)
	end
	if recipe:getOriginalname() == "Slice Fillet" and not source:isKeep() then -- the fish
		item:setActualWeight(1.1) -- must be > 1
	end
	return item
end

function RecipeUtils.CreateSourceItem2(recipe, source, sourceFullType, options, result)
	local playerObj = getPlayer()
	local item = nil
	if sourceFullType == "Water" then
		for i=1,source:getCount() do
			item = RecipeUtils.CreateSourceItem1(recipe, source, sourceFullType, options)
			table.insert(result, item)
		end
	elseif source:isDestroy() then
		for i=1,source:getCount() do
			item = RecipeUtils.CreateSourceItem1(recipe, source, sourceFullType, options)
			table.insert(result, item)
		end
	elseif getScriptManager():isDrainableItemType(sourceFullType) then
		item = RecipeUtils.CreateSourceItem1(recipe, source, sourceFullType, options)
		table.insert(result, item)
	elseif source:getUse() > 0 then
		item = RecipeUtils.CreateSourceItem1(recipe, source, sourceFullType, options)
		table.insert(result, item)
	else
		for i=1,source:getCount() do
			item = RecipeUtils.CreateSourceItem1(recipe, source, sourceFullType, options)
			table.insert(result, item)
		end
	end
end

function RecipeUtils.CreateSourceItem(recipe, source, sourceFullType, options, result)
	local playerObj = getPlayer()
	if sourceFullType == "Water" then
		RecipeUtils.CreateSourceItem2(recipe, source, sourceFullType, options, result)
	elseif source:isDestroy() then
		RecipeUtils.CreateSourceItem2(recipe, source, sourceFullType, options, result)
	elseif source:getUse() > 0 then
		RecipeUtils.CreateSourceItem2(recipe, source, sourceFullType, options, result)
	elseif getScriptManager():isDrainableItemType(sourceFullType) then
		for i=1,source:getCount() do
			RecipeUtils.CreateSourceItem2(recipe, source, sourceFullType, options, result)
		end
	else
		RecipeUtils.CreateSourceItem2(recipe, source, sourceFullType, options, result)
	end
end

local function getAvailableItemCount(source, sourceFullType, availableItems)
	local count = 0
	for i=1,availableItems:size() do
		local item = availableItems:get(i-1)
		if item:getFullType() == sourceFullType then
			count = count + 1
		end
	end
	return count
end

-- options.NoDuplicateKeep = true|false
-- options.AvailableItemsAll = result of RecipeManager.getAvailableItemsAll()
-- options.MaxItemsPerSource = N

function RecipeUtils.CreateSourceItems(recipe, options, result)
	for j=1,recipe:getSource():size() do
		local source = recipe:getSource():get(j-1)
		local itemTypes = source:getItems()
		for k=1,itemTypes:size() do
			local sourceFullType = itemTypes:get(k-1)
			local create = true
			if source:isKeep() and options.NoDuplicateKeep and options.AvailableItemsAll then
				-- FIXME: This doesn't handle drainable uses properly
				create = getAvailableItemCount(source, sourceFullType, options.AvailableItemsAll) < 1
			end
			if create then
				RecipeUtils.CreateSourceItem(recipe, source, sourceFullType, options, result)
			end
			if options.MaxItemsPerSource and (k == options.MaxItemsPerSource) then
				break
			end
		end
	end
end

