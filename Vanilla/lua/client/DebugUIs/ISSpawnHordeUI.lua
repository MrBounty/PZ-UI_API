--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISPanelJoypad"

ISSpawnHordeUI = ISCollapsableWindow:derive("ISSpawnHordeUI");

function ISSpawnHordeUI:createChildren()
	local btnWid = 100
	local btnHgt = 25
	local padBottom = 0
	local y = 60
	local f = 0.8
	
	ISCollapsableWindow.createChildren(self)
	
	self.zombiesNbrLabel = ISLabel:new(10, y, 10, "Zombies Number" ,1,1,1,1,UIFont.Small, true);
	self:addChild(self.zombiesNbrLabel);

	self.zombiesNbr = ISTextEntryBox:new("1", self.zombiesNbrLabel.x, self.zombiesNbrLabel.y + 15, 100, 20);
	self.zombiesNbr:initialise();
	self.zombiesNbr:instantiate();
	self.zombiesNbr:setOnlyNumbers(true);
	self:addChild(self.zombiesNbr);
	
	self.radiusLbl = ISLabel:new(130, y, 10, "Radius" ,1,1,1,1,UIFont.Small, true);
	self:addChild(self.radiusLbl);
	y=y+15
	
	self.radius = ISTextEntryBox:new("1", self.radiusLbl.x, y, 100, 20);
	self.radius:initialise();
	self.radius:instantiate();
	self.radius:setOnlyNumbers(true);
	self:addChild(self.radius);
	y=y+30
	
	self.outfitLbl = ISLabel:new(10, y, 10, "Zombies Outfit" ,1,1,1,1,UIFont.Small, true);
	self:addChild(self.outfitLbl);
	y=y+15
	
--	self.outfit = ISTextEntryBox:new("", self.outfitLbl.x, self.outfitLbl.y + 15, 100, 20);
--	self.outfit:initialise();
--	self.outfit:instantiate();
--	self.outfit:setClearButton(true);
--	self:addChild(self.outfit);
	
	self.outfit = ISComboBox:new(self.outfitLbl.x, y, 200, 20)
	self.outfit:initialise()
	self:addChild(self.outfit)
	self.maleOutfits = getAllOutfits(false);
	self.femaleOutfits = getAllOutfits(true);
	self.outfit:addOptionWithData("None", nil);
	for i=0, self.maleOutfits:size()-1 do
		local text = "";
		if not self.femaleOutfits:contains(self.maleOutfits:get(i)) then
			text = " - Male Only";
		end
		self.outfit:addOptionWithData(self.maleOutfits:get(i) .. text, self.maleOutfits:get(i));
	end
	for i=0, self.femaleOutfits:size()-1 do
		if not self.maleOutfits:contains(self.femaleOutfits:get(i)) then
			self.outfit:addOptionWithData(self.femaleOutfits:get(i) .. " - Female only", self.femaleOutfits:get(i));
		end
	end
	y=y+30
	
	--self.crawlerLbl = ISLabel:new(10, y, 10, "Crawler" ,1,1,1,1,UIFont.Small, true);
	--self:addChild(self.crawlerLbl);
	
	self.boolOptions = ISTickBox:new(10, y, 200, 20, "", self, ISSpawnHordeUI.onBoolOptionsChange);
	self.boolOptions:initialise()
	self:addChild(self.boolOptions)
	self.boolOptions:addOption("KnockedDown");
	self.boolOptions:addOption("Crawler");
	self.boolOptions:addOption("FakeDead");
	self.boolOptions:addOption("FallOnFront");
	y=y+self.boolOptions:getHeight()+10
	
	
	_,self.healthSliderTitle = ISDebugUtils.addLabel(self,"Health",10,y,"Health", UIFont.Small, true);
	_,self.healthSliderLabel = ISDebugUtils.addLabel(self,"Health",80,y,"1", UIFont.Small, false);
	_,self.healthSlider = ISDebugUtils.addSlider(self, "health", 130, y, 200, 20, ISSpawnHordeUI.onSliderChange)
	self.healthSlider.pretext = "Health: ";
	self.healthSlider.valueLabel = self.healthSliderLabel;
	self.healthSlider:setValues(0, 2, 0.1, 0.1, true);
	self.healthSlider.currentValue = 1.0;
	y=y+30

	self.pickNewSq = ISButton:new(200, 20, btnWid, btnHgt, "Pick new square", self, ISSpawnHordeUI.onSelectNewSquare);
	self.pickNewSq.anchorTop = false
	self.pickNewSq.anchorBottom = true
	self.pickNewSq:initialise();
	self.pickNewSq:instantiate();
	self.pickNewSq.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.pickNewSq);

	self.add = ISButton:new(10, self:getHeight() - padBottom - btnHgt - 22, btnWid*f, btnHgt, "Spawn", self, ISSpawnHordeUI.onSpawn);
	self.add.anchorTop = false
	self.add.anchorBottom = true
	self.add:initialise();
	self.add:instantiate();
	self.add.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.add);
	
	self.removezombies = ISButton:new(self.add.x + (btnWid*f) + 5, self.add.y, btnWid, btnHgt, "Remove zombies", self, ISSpawnHordeUI.onRemoveZombies);
	self.removezombies.anchorTop = false
	self.removezombies.anchorBottom = true
	self.removezombies:initialise();
	self.removezombies:instantiate();
	self.removezombies.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.removezombies);
	
	self.clearbodies = ISButton:new(self.width - btnWid*f - 15 - btnWid, self.add.y, btnWid, btnHgt, "Remove bodies", self, ISSpawnHordeUI.onRemoveBodies);
	self.clearbodies.anchorTop = false
	self.clearbodies.anchorBottom = true
	self.clearbodies:initialise();
	self.clearbodies:instantiate();
	self.clearbodies.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.clearbodies);
	
	self.close = ISButton:new(self.width - btnWid*f - 10, self.add.y, btnWid*f, btnHgt, "Close", self, ISSpawnHordeUI.close);
	self.close.anchorTop = false
	self.close.anchorBottom = true
	self.close:initialise();
	self.close:instantiate();
	self.close.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.close);
end

function ISSpawnHordeUI:onBoolOptionsChange(index, selected)
	if index == 1 then
		if not selected then
			self.boolOptions.selected[2] = false
			self.boolOptions.selected[3] = false
		end
	end
	if index == 2 then
		self.boolOptions.selected[1] = selected
		if selected then
			self.boolOptions.selected[4] = true
		end
	end
	if index == 3 then
		self.boolOptions.selected[1] = selected
	end
	if index == 4 then
		if not selected then
			self.boolOptions.selected[2] = false
		end
	end
end

function ISSpawnHordeUI:onSliderChange(_newval, _slider)
	if _slider.valueLabel then
		_slider.valueLabel:setName(ISDebugUtils.printval(_newval,3));
	end
end

--function ISSpawnHordeUI:update()
--	ISCollapsableWindow.update(self);
--end

function ISSpawnHordeUI:getRadius()
	local radius = self.radius:getInternalText();
	return (tonumber(radius) or 1) - 1;
end

function ISSpawnHordeUI:onSpawn()
	local count = self:getZombiesNumber()
	local radius = self:getRadius();
	local outfit = self:getOutfit();
	-- force female or male chance if you've selected a outfit that's only for male or female
	local femaleChance = nil;
	local knockedDown = false;
	local crawler = false;
	local isFallOnFront = false;
	local isFakeDead = false;
	if self.maleOutfits:contains(outfit) and not self.femaleOutfits:contains(outfit) then
		femaleChance = 0;
	end
	if self.femaleOutfits:contains(outfit) and not self.maleOutfits:contains(outfit) then
		femaleChance = 100;
	end
	if self.boolOptions.selected[1] then
		knockedDown = true;
	end
	if self.boolOptions.selected[2] then
		crawler = true;
	end
	if self.boolOptions.selected[3] then
		isFakeDead = true;
	end
	if self.boolOptions.selected[4] then
		isFallOnFront = true;
	end
	local health = self.healthSlider:getCurrentValue()
	if isClient() then
		SendCommandToServer(string.format("/createhorde2 -x %d -y %d -z %d -count %d -radius %d -crawler %s -isFallOnFront %s -isFakeDead %s -knockedDown %s -health %s -outfit %s ", self.selectX, self.selectY, self.selectZ, count, radius, tostring(crawler), tostring(isFallOnFront), tostring(isFakeDead), tostring(knockedDown), tostring(health), outfit or ""))
		return
	end
	for i=1,count do
		local x = ZombRand(self.selectX-radius, self.selectX+radius+1);
		local y = ZombRand(self.selectY-radius, self.selectY+radius+1);
		addZombiesInOutfit(x, y, self.selectZ, 1, outfit, femaleChance, crawler, isFallOnFront, isFakeDead, knockedDown, health);
	end
end

function ISSpawnHordeUI:getZombiesNumber()
	local nbr = self.zombiesNbr:getInternalText();
	return tonumber(nbr) or 1;
end

function ISSpawnHordeUI:getOutfit()
	return self.outfit.options[self.outfit.selected].data;
end

function ISSpawnHordeUI:onRemoveZombies()
	local radius = self:getRadius() + 1;
	if isClient() then
		SendCommandToServer(string.format("/removezombies -x %d -y %d -z %d -radius %d", self.selectX, self.selectY, self.selectZ, radius))
		return
	end
	for x=self.selectX-radius, self.selectX + radius do
		for y=self.selectY-radius, self.selectY + radius do
			local sq = getCell():getGridSquare(x,y,self.selectZ);
			if sq then
				for i=sq:getMovingObjects():size(),1,-1 do
					local testZed = sq:getMovingObjects():get(i-1);
					if instanceof(testZed, "IsoZombie") then
						testZed:removeFromWorld();
						testZed:removeFromSquare();
					end
				end
			end
		end
	end
end

function ISSpawnHordeUI:onRemoveBodies()
	if isClient() then
		local radius = self:getRadius() + 1;
		SendCommandToServer(string.format("/removezombies -x %d -y %d -z %d -radius %d -clear true", self.selectX, self.selectY, self.selectZ, radius))
		return
	end
end

function ISSpawnHordeUI:onSelectNewSquare()
	self.cursor = ISSelectCursor:new(self.chr, self, self.onSquareSelected)
	getCell():setDrag(self.cursor, self.chr:getPlayerNum())
end

function ISSpawnHordeUI:onSquareSelected(square)
	self.cursor = nil;
	self:removeMarker();
	self.selectX = square:getX();
	self.selectY = square:getY();
	self.selectZ = square:getZ();
	self:addMarker(square, self:getRadius() + 1);
end

function ISSpawnHordeUI:prerender()
	ISCollapsableWindow.prerender(self);
	local radius = (self:getRadius() + 1);
	if self.marker and (self.marker:getSize() ~= radius) then
		self.marker:setSize(radius)
	end
end

function ISSpawnHordeUI:render()
	ISCollapsableWindow.render(self);
	
	self:drawText("Picked Square: " .. self.selectX .. "," .. self.selectY .. "," .. self.selectZ, 10, 25, 1, 1, 1, 1, self.font);
end

function ISSpawnHordeUI:addMarker(square, radius)
	self.marker = getWorldMarkers():addGridSquareMarker(square, 0.8, 0.8, 0.0, true, radius);
	self.marker:setScaleCircleTexture(true);
	local texName = nil; -- use default
	self.arrow = getWorldMarkers():addDirectionArrow(self.chr, self.selectX, self.selectY, self.selectZ, texName, 1.0, 1.0, 1.0, 1.0);
end

function ISSpawnHordeUI:removeMarker()
	if self.marker then
		self.marker:remove();
		self.marker = nil;
	end
	if self.arrow then
		self.arrow:remove();
		self.arrow = nil;
	end
end

function ISSpawnHordeUI:close()
	self:removeMarker();
	self:setVisible(false);
	self:removeFromUIManager();
end

--************************************************************************--
--** ISSpawnHordeUI:new
--**
--************************************************************************--
function ISSpawnHordeUI:new(x, y, character, square)
	local o = {}
	local width = 400;
	local height = 350;
	o = ISCollapsableWindow:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.playerNum = character:getPlayerNum()
	if y == 0 then
		o.y = getPlayerScreenTop(o.playerNum) + (getPlayerScreenHeight(o.playerNum) - height) / 2
		o:setY(o.y)
	end
	if x == 0 then
		o.x = getPlayerScreenLeft(o.playerNum) + (getPlayerScreenWidth(o.playerNum) - width) / 2
		o:setX(o.x)
	end
	o.width = width;
	o.height = height;
	o.chr = character;
	o.moveWithMouse = true;
	o.selectX = square:getX();
	o.selectY = square:getY();
	o.selectZ = square:getZ();
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	o:addMarker(square, 1);
	return o;
end
