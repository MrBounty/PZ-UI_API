--***********************************************************
--**                    ROBERT JOHNSON                     **
--**    The (not so) famouns farming info panel            **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISFarmingInfo : ISPanelJoypad
ISFarmingInfo = ISPanelJoypad:derive("ISFarmingInfo");

local FONT_HGT_NORMAL = getTextManager():getFontHeight(UIFont.Normal)

water_rgb = {["r"]=255.0,["g"]=255.0,["b"]=255.0};
waterbar_rgb = {["r"]=0.15,["g"]=0.3,["b"]=0.63};
fertilizer_rgb = {["r"]=0.0,["g"]=0.0,["b"]=0.0};
health_rgb = {["r"]=0.0,["g"]=0.0,["b"]=0.0};
nowateredsince_rgb = {["r"]=255.0,["g"]=255.0,["b"]=255.0};
disease_rgb = {["0r"]=255.0,["0g"]=255.0,["0b"]=255.0};
disease = {};
title_rgb = {["r"]=1.0,["g"]=1.0,["b"]=1.0};
deb = true;

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISFarmingInfo:initialise()
	ISPanelJoypad.initialise(self);
end

function ISFarmingInfo:setPlant(plant)
	self.plant = plant;
	self.vegetable = getTexture(farming_vegetableconf.icons[plant.typeOfSeed]);
end


--************************************************************************--
--** ISPanel:render
--**
--************************************************************************--
function ISFarmingInfo:prerender()
	if self:isPlantValid() then
		local square = self.plant:getSquare()
		-- Hide the window when the plant is out-of-bounds
--		if not square then self:getParent():setVisible(false); return end
		if isClient() then
			-- Hack: because the client does not have an up-to-date list of plants
			local object = self.plant:getObject()
			if object then
				self.plant:fromModData(object:getModData())
			end
		end
	end
end


function ISFarmingInfo:render()
	if not self:isPlantValid() then return end
	local farmingLevel = CFarmingSystem.instance:getXp(self.character)
	ISFarmingInfo.getFertilizerColor(self);
	ISFarmingInfo.getWaterLvlColor(self.plant, farmingLevel);
	local lastWatedHour = ISFarmingInfo.getLastWatedHour(self.plant);
	ISFarmingInfo.getTitleColor(self.plant);
	ISFarmingInfo.getHealthColor(self, farmingLevel);
	ISFarmingInfo.getNoWateredSinceColor(self, lastWatedHour, farmingLevel);
	local disease = ISFarmingInfo.getDiseaseName(self);
	ISFarmingInfo.getWaterLvlBarColor(self, farmingLevel);
	local top = 69
	local y = top;
	-- icon of the plant
	self:drawTextureScaled(self.vegetable, 20,20,25,25,1,1,1,1);
	-- title of the plant
	if self.plant:getObject() then
		self:drawText(self.plant:getObject():getObjectName(), 60, (top - FONT_HGT_NORMAL) / 2, title_rgb["r"], title_rgb["g"], title_rgb["b"], 1, UIFont.Normal);
	else
		self:drawText("Dead " .. getText("Farming_" .. self.plant.typeOfSeed), 60, (top - FONT_HGT_NORMAL) / 2, title_rgb["r"], title_rgb["g"], title_rgb["b"], 1, UIFont.Normal);
	end
	local fontHgt = FONT_HGT_NORMAL
	local pady = 1
	local lineHgt = fontHgt + pady * 2
	-- background for current growing phase
	self:drawRect(13, y, self.width - 25, lineHgt, 0.1, 1.0, 1.0, 1.0);
	-- text for current growing phase
	self:drawText(getText("Farming_Current_growing_phase") .. " : ", 20, y + pady, 1, 1, 1, 1, UIFont.Normal);
	-- stat (next growing state) on the right
	self:drawTextRight(ISFarmingInfo.getCurrentGrowingPhase(self, farmingLevel), self.width - 17, y + pady, 1, 1, 1, 1, UIFont.Normal);
	y = y + lineHgt;
	self:drawRect(13, y, self.width - 25, lineHgt, 0.05, 1.0, 1.0, 1.0);
	self:drawText(getText("Farming_Next_growing_phase") .. " : ", 20, y + pady, 1, 1, 1, 1, UIFont.Normal);
	self:drawTextRight(ISFarmingInfo.getNextGrowingPhase(self), self.width - 17, y + pady, 1, 1, 1, 1, UIFont.Normal);
	y = y + lineHgt;
	self:drawRect(13, y, self.width - 25, lineHgt, 0.1, 1.0, 1.0, 1.0);
	self:drawText(getText("Farming_Last_time_watered") .. " : ", 20, y + pady, 1, 1, 1, 1, UIFont.Normal);
	lastWatedHour = lastWatedHour .. " " .. getText("Farming_Hours");
	self:drawTextRight(lastWatedHour, self.width - 17, y + pady, nowateredsince_rgb["r"], nowateredsince_rgb["g"], nowateredsince_rgb["b"], 1, UIFont.Normal);
	y = y + lineHgt;
	self:drawRect(13, y, self.width - 25, lineHgt, 0.05, 1.0, 1.0, 1.0);
	self:drawText(getText("Farming_Fertilized") .. " : ", 20, y + pady, 1.0, 1.0, 1.0, 1, UIFont.Normal);
	self:drawTextRight(self.plant.fertilizer .. "", self.width - 17, y + pady, fertilizer_rgb["r"], fertilizer_rgb["g"], fertilizer_rgb["b"], 1, UIFont.Normal);
	y = y + lineHgt;
	self:drawRect(13, y, self.width - 25, lineHgt, 0.1, 1.0, 1.0, 1.0);
	self:drawText(getText("Farming_Health") .. " : ", 20, y + pady, 1.0, 1.0, 1.0, 1, UIFont.Normal);
	self:drawTextRight(ISFarmingInfo.getHealth(self, farmingLevel), self.width - 17, y + pady, health_rgb["r"], health_rgb["g"], health_rgb["b"], 1, UIFont.Normal);
	y = y + lineHgt;
	self:drawText(getText("Farming_Disease") .. " : ", 20, y + pady, 1, 1, 1, 1);
	self:drawTextRight(disease.text, self.width - 17, y + pady, disease_rgb["0r"], disease_rgb["0g"], disease_rgb["0b"], 1);
	y = y + lineHgt;
	if(disease[1]) then
		self:drawRect(13, y, self.width - 25, lineHgt, 0.05, 1.0, 1.0, 1.0);
		self:drawText(disease[1].name, 40, y + pady, 1, 1, 1, 1)
		self:drawTextRight(disease[1].value, self.width - 17, y + pady, disease_rgb["1r"], disease_rgb["1g"], disease_rgb["1b"], 1);
		y = y + lineHgt;
	end
	if(disease[2]) then
		self:drawRect(13, y, self.width - 25, lineHgt, 0.05, 1.0, 1.0, 1.0);
		self:drawText(disease[2].name, 40, y + pady, 1, 1, 1, 1)
		self:drawTextRight(disease[2].value, self.width - 17, y + pady, disease_rgb["2r"], disease_rgb["2g"], disease_rgb["2b"], 1);
		y = y + lineHgt;
	end
	if(disease[3]) then
		self:drawRect(13, y, self.width - 25, lineHgt, 0.05, 1.0, 1.0, 1.0);
		self:drawText(disease[3].name, 40, y + pady, 1, 1, 1, 1)
		self:drawTextRight(disease[3].value, self.width - 17, y + pady, disease_rgb["3r"], disease_rgb["3g"], disease_rgb["3b"], 1);
		y = y + lineHgt;
	end
	-- rect for all info
	self:drawRectBorder(13, top - 1, self.width - 25, y - top + 2, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	y = y + 5;
	self:drawText(getText("Farming_Water_levels"), 13, y, 1, 1, 1, 1);
	self:drawTextRight(ISFarmingInfo.getWaterLvl(self.plant, farmingLevel), self.width - 12, y, water_rgb["r"], water_rgb["g"], water_rgb["b"], 1, UIFont.normal);
	y = y + fontHgt + 2;
	-- show the water bar with at least 4 farming skill
	if farmingLevel >= 4 then
		self:drawRect(13, y, self.width - 25, 12, 0.05, 1.0, 1.0, 1.0);
		self:drawRectBorder(13, y, self.width - 25, 12, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
		self:drawRect(14, y + 1, ISFarmingInfo.getWaterBarWidth(self), 10, 0.7, waterbar_rgb["r"], waterbar_rgb["g"], waterbar_rgb["b"]);
		y = y + 12
	end
	self:setHeightAndParentHeight(y + 8)
end

function ISFarmingInfo:update()
	ISPanelJoypad.update(self)
	if not self.plant or (self.parent:getIsVisible() and not self:isPlantValid()) then
		if self.joyfocus then
			self.joyfocus.focus = nil
			updateJoypadFocus(self.joyfocus)
		end
		self.parent:setVisible(false)
	end
end

function ISFarmingInfo:isPlantValid()
	self.plant:updateFromIsoObject()
	return self.plant ~= nil and self.plant:getIsoObject() ~= nil
end

function ISFarmingInfo:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self.parent.drawJoypadFocus = true
end

function ISFarmingInfo:onLoseJoypadFocus(joypadData)
	ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
	self.parent.drawJoypadFocus = false
end

function ISFarmingInfo:onJoypadDown(button, joypadData)
	if button == Joypad.BButton then
		self.parent:setVisible(false)
		joypadData.focus = nil
		if self.plant:getSquare() then
			ISFarmingMenu.onJoypadFarming(self.plant:getSquare(), joypadData.player)
		end
	end
end

-- show text with <= 4 farming skill
-- else show the right number
function ISFarmingInfo.getHealth(info, farmingLevel)
	if farmingLevel <= 4 then
		if info.plant.health > 80 then
			return getText("Farming_Flourishing");
		elseif info.plant.health > 60 then
			return getText("Farming_Verdant");
		elseif info.plant.health > 40 then
			return getText("Farming_Healthy");
		elseif info.plant.health > 20 then
			return getText("Farming_Sickly");
		else
			return getText("Farming_Stunted");
		end
	else
		return round2(info.plant.health, 2) .. "";
	end
end

-- show text with <= 2 farming skill
-- else show the right number
function ISFarmingInfo.getWaterLvl(plant, farmingLevel)
	if farmingLevel <= 4 then
		if plant.waterLvl > 80 then
			return getText("Farming_Well_watered");
		elseif plant.waterLvl > 60 then
			return getText("Farming_Fine");
		elseif plant.waterLvl > 40 then
			return getText("Farming_Thirsty");
		elseif plant.waterLvl > 20 then
			return getText("Farming_Dry");
		else
			return getText("Farming_Parched");
		end
	else
		return round2(plant.waterLvl, 2) .. " / 100";
	end
end

function ISFarmingInfo.getTitleColor(plant)
    local rgb = {};
	if plant.state == "dry" or plant.state == "rotten" or plant.state == "destroyed" then
        rgb = {["r"]=1.0,["g"]=0.0,["b"]=0.0};
	else
        rgb = {["r"]=1.0,["g"]=1.0,["b"]=1.0};
    end
    title_rgb = rgb;
    return rgb;
end

function ISFarmingInfo.getWaterBarWidth(info)
	local totalWidth = info:getWidth() - 27;
	local per = totalWidth / 100;
	return totalWidth - ((100 - info.plant.waterLvl) * per);
end

-- if we have at least 2 farming, display water lvl in color, to help the player
function ISFarmingInfo.getWaterLvlBarColor(info, farmingLevel)
	ISFarmingInfo:getBlueBar(waterbar_rgb);
	if farmingLevel >= 4 and info.plant:isAlive() then
		-- first state, you must have the required water lvl, so if it's not : red
		if(info.plant.nbOfGrow == 1) then
			if(info.plant.waterLvl < info.plant.waterNeeded) then
				ISFarmingInfo:getRedBar(waterbar_rgb);
			end
		else
			local water = farming_vegetableconf.calcWater(info.plant.waterNeeded, info.plant.waterLvl);
			local waterMax = farming_vegetableconf.calcWater(info.plant.waterLvl, info.plant.waterNeededMax);
			if(water >= 0 and waterMax >= 0) then -- green
				ISFarmingInfo:getBlueBar(waterbar_rgb);
			elseif(water == -1 or waterMax == -1) then -- orange
				ISFarmingInfo:getOrangeBar(waterbar_rgb);
			else -- red
				ISFarmingInfo:getRedBar(waterbar_rgb);
			end
		end
	end
end

-- if we have at least 1 farming, display red if you don't have water your plant since more than 60hours
function ISFarmingInfo.getNoWateredSinceColor(plant, lastWatedHour, farmingLevel)
	ISFarmingInfo:getGreen(nowateredsince_rgb);
	if(farmingLevel >= 2 and (plant.state ~= "dry" or plant.state ~= "rotten" or plant.state ~= "destroyed")) then
		if(lastWatedHour < 50) then -- green
			ISFarmingInfo:getGreen(nowateredsince_rgb);
		elseif(lastWatedHour < 60) then -- orange
			ISFarmingInfo:getOrange(nowateredsince_rgb);
		else -- red
			ISFarmingInfo:getRed(nowateredsince_rgb);
		end
    end
    return nowateredsince_rgb;
end

function ISFarmingInfo:getBlueBar(list)
	list["r"] = 0.15;
	list["g"] = 0.3;
	list["b"] = 0.63;
end
function ISFarmingInfo:getOrangeBar(list)
	list["r"] = 0.98;
	list["g"] = 0.55;
	list["b"] = 0.0;
end
function ISFarmingInfo:getRedBar(list)
	list["r"] = 0.70;
	list["g"] = 0.13;
	list["b"] = 0.13;
end

local function maxWidthOfTexts(texts)
	local max = 0
	for _,text in ipairs(texts) do
		local width = getTextManager():MeasureStringX(UIFont.Normal, getText(text))
		max = math.max(max, width)
	end
	return max
end

--************************************************************************--
--** ISPanel:new
--**
--************************************************************************--
function ISFarmingInfo:new (x, y, width, height, character, plant)
	local o = {}
	o = ISPanelJoypad:new(x, y, width, height);
	setmetatable(o, self)
    self.__index = self
	o.plant = plant;
	o.character = character
	o.vegetable = getTexture(farming_vegetableconf.icons[plant.typeOfSeed]);
   return o
end

function ISFarmingInfo.RequiredWidth()
	local columnWidth1 = maxWidthOfTexts({
		"Farming_Current_growing_phase",
		"Farming_Next_growing_phase",
		"Farming_Last_time_watered",
		"Farming_Fertilized",
		"Farming_Health",
		"Farming_Disease"
	})
	columnWidth1 = columnWidth1 + getTextManager():MeasureStringX(UIFont.Normal, " : ")

	local columnWidth2 = maxWidthOfTexts({
		"Farming_Flourishing",
		"Farming_Verdant",
		"Farming_Healthy",
		"Farming_Sickly",
		"Farming_Stunted",
		
		"Farming_Seedling",
		"Farming_Young",
		"Farming_Fully_grown",
		"Farming_Ready_to_harvest",
		"UI_FriendState_Unknown"
	})

	local width = 20 + columnWidth1 + columnWidth2 + 17

	local disease1 = maxWidthOfTexts({
		"Farming_Mildew",
		"Farming_Devil_Water_Fungi",
		"Farming_Pest_Flies"
	})
	disease1 = disease1 + getTextManager():MeasureStringX(UIFont.Normal, " : ")

	local disease2 = getTextManager():MeasureStringX(UIFont.Normal, getText("UI_FriendState_Unknown") .. " / 100")

	width = math.max(width, 40 + disease1 + disease2 + 17)

	return width
end

-- if we have at least 2 farming, display water lvl in color, to help the player
function ISFarmingInfo.getWaterLvlColor(plant, farmingLevel)
	if farmingLevel >= 4 and plant:isAlive() then
		-- first state, you must have the required water lvl, so if it's not : red
		if(plant.nbOfGrow == 1) then
			ISFarmingInfo:getGreen(water_rgb, nil);
			if(plant.waterLvl < plant.waterNeeded) then
				ISFarmingInfo:getRed(water_rgb, nil);
            end
		else
			local water = farming_vegetableconf.calcWater(plant.waterNeeded, plant.waterLvl);
			local waterMax = farming_vegetableconf.calcWater(plant.waterLvl, plant.waterNeededMax);
			if(water >= 0 and waterMax >= 0) then -- green
				ISFarmingInfo:getGreen(water_rgb, nil);
			elseif(water == -1 or waterMax == -1) then -- orange
				ISFarmingInfo:getOrange(water_rgb, nil);
			else -- red
				ISFarmingInfo:getRed(water_rgb, nil);
			end
		end
	else
		ISFarmingInfo:getWhite(water_rgb, nil);
    end
    return water_rgb;
end

-- get the color of health if player have more than 2 farming skill (to see the health)
function ISFarmingInfo.getHealthColor(info, farmingLevel)
	if farmingLevel >= 2 then
		if(info.plant.health >= 60) then -- green
			ISFarmingInfo:getGreen(health_rgb, nil);
		elseif(info.plant.health >= 40) then -- orange
			ISFarmingInfo:getOrange(health_rgb, nil);
		else -- red
			ISFarmingInfo:getRed(health_rgb, nil);
		end
	else
		ISFarmingInfo:getWhite(health_rgb, nil);
	end
end

-- show nothing with 0 farming
-- show text with 1 to 2 farming
-- show numbers with > 2
function ISFarmingInfo.getCurrentGrowingPhase(info, farmingLevel)
	if farmingLevel >= 2 and farmingLevel <= 4 then
		if info.plant.nbOfGrow <= 2 then
			return getText("Farming_Seedling");
		elseif info.plant.nbOfGrow <= 5 then
			return getText("Farming_Young");
		elseif info.plant.nbOfGrow <= 6 then
			return getText("Farming_Fully_grown");
		else
			return getText("Farming_Ready_to_harvest");
		end
	elseif farmingLevel >= 4 then
		if(info.plant.nbOfGrow > 7) then
			return "7";
		end
		return info.plant.nbOfGrow .. " / 7";
	end
	return getText("UI_FriendState_Unknown");
end

-- display the hour of the next growing phase if with have at least 4 farmings pts
function ISFarmingInfo.getNextGrowingPhase(info)
	if(info.plant.state ~= "dry" and info.plant.state ~= "rotten" and info.plant.state ~= "destroyed") then
		if(CFarmingSystem.instance:getXp(info.character) >= 8) then
			if(info.plant.nextGrowing == 0) then
				return "0 " .. getText("Farming_Hours");
			else
                if(info.plant.nextGrowing - CFarmingSystem.instance.hoursElapsed < 0) then
                    return "0 " .. getText("Farming_Hours");
            end
				return round2((info.plant.nextGrowing - CFarmingSystem.instance.hoursElapsed)) .. " " .. getText("Farming_Hours");
			end
		end
		return getText("UI_FriendState_Unknown");
	end
	return getText("UI_No");
end

-- we show color of disease with 3 farming skill
function ISFarmingInfo:getDiseaseColor(diseaseLvl, index, info)
	ISFarmingInfo:getWhite(disease_rgb, index);
	if(CFarmingSystem.instance:getXp(self.character) >= 6 and (info.plant.state ~= "dry" or info.plant.state ~= "rotten" or info.plant.state ~= "destroyed")) then
		local disease = farming_vegetableconf.calcDisease(diseaseLvl);
		ISFarmingInfo:getGreen(disease_rgb, index);
		if(diseaseLvl > 0) then -- orange
			ISFarmingInfo:getOrange(disease_rgb, index);
		elseif(disease == -2) then -- red
			ISFarmingInfo:getRed(disease_rgb, index);
		end
	else
		ISFarmingInfo:getWhite(disease_rgb, index);
	end
end

-- we show name of disease with 2 farming skill
-- we show lvl of disease with 3 farming skill
function ISFarmingInfo.getDiseaseName(info)
	local farmingLevel = CFarmingSystem.instance:getXp(info.character)
	local disease = {};
	local result = {};
	-- mildew
	if(info.plant.mildewLvl > 0) then
		disease.text = getText("UI_Yes");
		ISFarmingInfo:getOrange(disease_rgb, "0");
		if(farmingLevel >= 4) then
			result.name = getText("Farming_Mildew") .. " : ";
			if(farmingLevel >= 6) then
				result.value = info.plant.mildewLvl .. " / 100";
			else
				result.value = getText("UI_FriendState_Unknown") .. " / 100";
			end
			-- we have mildew, let's add it to our map
			disease[1] = result;
			ISFarmingInfo:getDiseaseColor(info.plant.mildewLvl, "1", info);
		end
	end

	-- now we test aphid
	if(info.plant.aphidLvl > 0) then
		disease.text = getText("UI_Yes");
		ISFarmingInfo:getOrange(disease_rgb, "0");
		if(farmingLevel >= 4) then
			result = {};
			result.name = getText("Farming_Devil_Water_Fungi") .. " : ";
			if(farmingLevel >= 6) then
				result.value = info.plant.aphidLvl .. " / 100";
			else
				result.value = getText("UI_FriendState_Unknown") .. " / 100";
			end
			-- we have aphid let's add it to our map
			disease[#disease + 1] = result;
			ISFarmingInfo:getDiseaseColor(info.plant.aphidLvl, #disease, info);
		end
	end

	-- now we test flies
	if(info.plant.fliesLvl > 0) then
		disease.text = getText("UI_Yes");
		ISFarmingInfo:getOrange(disease_rgb, "0");
		if(farmingLevel >= 4) then
			result = {};
			result.name = getText("Farming_Pest_Flies") .. " : ";
			if(farmingLevel >= 6) then
				result.value = info.plant.fliesLvl .. " / 100";
			else
				result.value = getText("UI_FriendState_Unknown") .. " / 100";
			end
			-- we have flies let's add it to our map
			disease[#disease + 1] = result;
			ISFarmingInfo:getDiseaseColor(info.plant.fliesLvl, #disease, info);
		end
	end

	-- if we have no disease
	if disease.text == nil then
		disease.text = getText("UI_No");
		ISFarmingInfo:getGreen(disease_rgb, "0");
	end
	return disease;
end

function ISFarmingInfo.getLastWatedHour(plant)
	return CFarmingSystem.instance.hoursElapsed - plant.lastWaterHour;
end

-- display the color danger of the fertilizer lvl (0-2 : green, 2-3 : orange, 4 red)
function ISFarmingInfo.getFertilizerColor(info)
	if info.plant.state ~= "dry" or info.plant.state ~= "rotten" or info.plant.state ~= "destroyed" then
		if(info.plant.fertilizer <= 2) then
			ISFarmingInfo:getGreen(fertilizer_rgb, nil);
		elseif(info.plant.fertilizer > 2 and info.plant.fertilizer <= 3) then
			ISFarmingInfo:getOrange(fertilizer_rgb, nil);
		else
			ISFarmingInfo:getRed(fertilizer_rgb, nil);
		end
	else
		ISFarmingInfo:getWhite(fertilizer_rgb, nil);
	end
end


function ISFarmingInfo:getGreen(list, index)
	if(index ~= nil) then
		list[index .. "r"] = 0.0;
		list[index .. "g"] = 0.8;
		list[index .. "b"] = 0.0;
	else
		list["r"] = 0.0;
		list["g"] = 0.8;
		list["b"] = 0.0;
	end
end

function ISFarmingInfo:getOrange(list, index)
	if(index ~= nil) then
		list[index .. "r"] = 1.0;
		list[index .. "g"] = 0.5;
		list[index .. "b"] = 0.0;
	else
		list["r"] = 1.0;
		list["g"] = 0.5;
		list["b"] = 0.0;
	end
end

function ISFarmingInfo:getRed(list, index)
	if(index ~= nil) then
		list[index .. "r"] = 1.0;
		list[index .. "g"] = 0.0;
		list[index .. "b"] = 0.0;
	else
		list["r"] = 1.0;
		list["g"] = 0.0;
		list["b"] = 0.0;
	end
end

function ISFarmingInfo:getWhite(list, index)
	if(index ~= nil) then
		list[index .. "r"] = 1.0;
		list[index .. "g"] = 1.0;
		list[index .. "b"] = 1.0;
	else
		list["r"] = 1.0;
		list["g"] = 1.0;
		list["b"] = 1.0;
	end
end

function round2(num, idp)
  return tonumber(string.format("%." .. (idp or 0) .. "f", num))
end
