--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "OptionScreens/MapSpawnSelect"

---@class CoopMapSpawnSelect : MapSpawnSelect
CoopMapSpawnSelect = MapSpawnSelect:derive("CoopMapSpawnSelect")

function CoopMapSpawnSelect:canRespawnWithSelf()
	if not isClient() then
		return getNumActivePlayers() > 1
	end
	return getServerOptions():getBoolean("PlayerRespawnWithSelf")
end

function CoopMapSpawnSelect:canRespawnWithOther()
	if not isClient() then
		return (getNumActivePlayers() > 1) or (not IsoPlayer.allPlayersDead())
	end
	return getServerOptions():getBoolean("PlayerRespawnWithOther")
end

function CoopMapSpawnSelect:hasChoices()
	if Core.isLastStand() then return false end

	local respawningPlayerIndex = CoopCharacterCreation.instance.playerIndex
	if self:canRespawnWithSelf() then
		if getSpecificPlayer(respawningPlayerIndex) then
			return true
		end
	end
	if self:canRespawnWithOther() then
		for playerIndex=0,getNumActivePlayers()-1 do
			local playerObj = getSpecificPlayer(playerIndex)
			if playerObj and (respawningPlayerIndex ~= playerIndex) then
				return true
			end
		end
	end

	return MapSpawnSelect.hasChoices(self)
end

function CoopMapSpawnSelect:fillList()
	MapSpawnSelect.fillList(self)

	local respawningPlayerIndex = CoopCharacterCreation.instance.playerIndex
	for playerIndex=0,getNumActivePlayers()-1 do
		local playerObj = getSpecificPlayer(playerIndex)
		if not self:canRespawnWithSelf() then
			if playerIndex == respawningPlayerIndex then
				playerObj = nil
			end
		end
		if not self:canRespawnWithOther() then
			if playerIndex ~= respawningPlayerIndex then
				playerObj = nil
			end
		end
		if playerObj then -- dead or alive
			local x = playerObj:getX()
			local y = playerObj:getY()
			local z = playerObj:getZ()
			local wx = math.floor(x/300)
			local wy = math.floor(y/300)
			local region = {
				name = getText("UI_mapspawn_WithPlayer" .. (playerIndex+1)),
				points = {
					unemployed = {
						{worldX=wx, worldY=wy, posX=(x-wx*300), posY=(y-wy*300), posZ=z},
					},
				}
			}
			local item = {
				name = region.name,
				region = region,
				dir = "",
				desc = "",
				worldimage = nil
			}
			self.listbox:addItem(item.name, item)
		end
	end
end

function CoopMapSpawnSelect:clickBack()
	self:setVisible(false)
	if CoopUserName.instance:shouldShow() then
		CoopUserName.instance:setVisible(true, self.joyfocus)
	else
		CoopCharacterCreation.instance:cancel()
	end
end

function CoopMapSpawnSelect:clickNext()
	self.selectedRegion = self.listbox.items[self.listbox.selected].item.region
	self:setVisible(false)
	CoopCharacterCreation.instance.charCreationProfession:setVisible(true, self.joyfocus)
end

function CoopMapSpawnSelect:new(x, y, width, height)
	local o = MapSpawnSelect.new(self, x, y, width, height)
	o:setUIName("CoopMapSpawnSelect")
	CoopMapSpawnSelect.instance = o
	return o
end

