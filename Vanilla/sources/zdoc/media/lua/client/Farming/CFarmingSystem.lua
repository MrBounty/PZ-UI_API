--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "Map/CGlobalObjectSystem"

---@class CFarmingSystem : CGlobalObjectSystem
CFarmingSystem = CGlobalObjectSystem:derive("CFarmingSystem")

function CFarmingSystem:new()
	local o = CGlobalObjectSystem.new(self, "farming")
	if not o.hoursElapsed then error "hoursElapsed wasn't sent from the server?" end
	return o
end

function CFarmingSystem:isValidIsoObject(isoObject)
	if not isoObject or not isoObject:hasModData() then return false end
	local modData = isoObject:getModData()
	if modData.state and modData.nbOfGrow and modData.health then return true end
	return false
end

function CFarmingSystem:newLuaObject(globalObject)
	return CPlantGlobalObject:new(self, globalObject)
end

function CFarmingSystem:OnServerCommand(command, args)
	if command == "hoursElapsed" then
		self.hoursElapsed = args.hoursElapsed
	else
		CGlobalObjectSystem.OnServerCommand(self, command, args)
	end
end

-- add xp, depending on the health of the plant
function CFarmingSystem:gainXp(player, luaObject)
	local xp = luaObject.health / 2
	if luaObject.badCare == true then
		xp = xp - 15
	else
		xp = xp + 25
	end
	if xp > 100 then
		xp = 100
	elseif xp < 0 then
		xp = 0
	end
	player:getXp():AddXP(Perks.Farming, xp)
end

-- get the farming xp of the player
function CFarmingSystem:getXp(character)
	if not character then character = getPlayer() end
	return character:getPerkLevel(Perks.Farming)
end

-- make the player more tired etc. when plowing land
function CFarmingSystem:changePlayer(player)
	player:getStats():setFatigue(player:getStats():getFatigue() + 0.006)
	player:getStats():setEndurance(player:getStats():getEndurance() - 0.0013)
end

CGlobalObjectSystem.RegisterSystemClass(CFarmingSystem)

local function DoSpecialTooltip1(tooltip, square)
	if ISFarmingCursorMouse.IsVisible() then return end
	
	local playerObj = getSpecificPlayer(0)
	if not playerObj or CFarmingSystem.instance:getXp(playerObj) < 4 then return end

	local plant = CFarmingSystem.instance:getLuaObjectOnSquare(square)
	if not plant or plant.typeOfSeed == "none" then return end

	local farmingLevel = CFarmingSystem.instance:getXp(playerObj)
	local water_rgb = ISFarmingInfo.getWaterLvlColor(plant, farmingLevel)

	tooltip:DrawTextureScaled(tooltip:getTexture(), 0, 0, tooltip:getWidth(), tooltip:getHeight(), 0.75)

	local title_rgb = ISFarmingInfo.getTitleColor(plant)
	tooltip:DrawTextCentre(tooltip:getFont(), plant:getObject():getObjectName(), tooltip:getWidth() / 2, 5, title_rgb["r"], title_rgb["g"], title_rgb["b"], 1)
	tooltip:adjustWidth(5, plant:getObject():getObjectName())

	local layout = tooltip:beginLayout()
	local layoutItem = layout:addItem()
	layoutItem:setLabel(getText("Farming_Water_levels")..':', 1, 1, 1, 1)
	layoutItem:setValue(ISFarmingInfo.getWaterLvl(plant, farmingLevel), water_rgb["r"], water_rgb["g"], water_rgb["b"], 1)

	local lastWateredHour = ISFarmingInfo.getLastWatedHour(plant)
	lastWaterdHour = lastWateredHour .. " " .. getText("Farming_Hours")
	local nowateredsince_rgb = ISFarmingInfo.getNoWateredSinceColor(plant, lastWateredHour, farmingLevel)
	layoutItem = layout:addItem()
	layoutItem:setLabel(getText("Farming_Last_time_watered")..':', 1, 1, 1, 1)
	layoutItem:setValue(lastWaterdHour, nowateredsince_rgb["r"], nowateredsince_rgb["g"], nowateredsince_rgb["b"], 1)

	local y = layout:render(5, 5 + getTextManager():getFontHeight(tooltip:getFont()), tooltip)
	tooltip:setHeight(y + 5)
	tooltip:endLayout(layout)
end

local function DoSpecialTooltip(tooltip, square)
	tooltip:setWidth(100)
	tooltip:setMeasureOnly(true)
	DoSpecialTooltip1(tooltip, square)
	tooltip:setMeasureOnly(false)
	DoSpecialTooltip1(tooltip, square)
end

Events.DoSpecialTooltip.Add(DoSpecialTooltip)

