--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISUIEmoteConfig : ISCollapsableWindow
ISUIEmoteConfig = ISCollapsableWindow:derive("ISUIEmoteConfig");

function ISUIEmoteConfig:createChildren()
	local btnWid = 100
	local btnHgt = 25
	local padBottom = 0
	
	ISCollapsableWindow.createChildren(self)
	
	self.saveBtn = ISButton:new(10, self.height - btnHgt - 10, btnWid, btnHgt, "Save", self, ISUIEmoteConfig.writeToFile);
	self.saveBtn.anchorTop = false
	self.saveBtn.anchorBottom = true
	self.saveBtn:initialise();
	self.saveBtn:instantiate();
	self.saveBtn.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.saveBtn);
	
	self.close = ISButton:new(self.width - btnWid - 10, self.height - btnHgt - 10, btnWid, btnHgt, "Close", self, ISUIEmoteConfig.close);
	self.close.anchorTop = false
	self.close.anchorBottom = true
	self.close:initialise();
	self.close:instantiate();
	self.close.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.close);
	
	self:readFile();
	
	self.categoryLbl = ISLabel:new(10, 30, 10, "Category" ,1,1,1,1,UIFont.Small, true);
	self:addChild(self.categoryLbl);

	self.categoryCB = ISComboBox:new(self.categoryLbl.x, self.categoryLbl.y + 20, 200, 20, self, self.selectCategory)
	self.categoryCB:initialise()
	self:addChild(self.categoryCB)
	
	self.emoteLbl = ISLabel:new(self.categoryCB.x, self.categoryCB.y + 30, 10, "Emote" ,1,1,1,1,UIFont.Small, true);
	self:addChild(self.emoteLbl);
	
	self.emoteCB = ISComboBox:new(self.emoteLbl.x, self.emoteLbl.y + 20, 200, 20, self, self.selectEmote)
	self.emoteCB:initialise()
	self:addChild(self.emoteCB)
	
	self.addNewEmoteBtn = ISButton:new(self.emoteCB.x + self.emoteCB.width + 5, self.emoteCB.y, btnWid, btnHgt, "Add New Emote", self, ISUIEmoteConfig.addNewEmote);
	self.addNewEmoteBtn.anchorTop = false
	self.addNewEmoteBtn.anchorBottom = true
	self.addNewEmoteBtn:initialise();
	self.addNewEmoteBtn:instantiate();
	self.addNewEmoteBtn.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.addNewEmoteBtn);
	
	self.remoteEmoteBtn = ISButton:new(self.emoteCB.x, self.emoteCB.y + self.emoteCB.height + 10, btnWid, btnHgt, "Remove Emote", self, ISUIEmoteConfig.removeEmote);
	self.remoteEmoteBtn.anchorTop = false
	self.remoteEmoteBtn.anchorBottom = true
	self.remoteEmoteBtn:initialise();
	self.remoteEmoteBtn:instantiate();
	self.remoteEmoteBtn.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.remoteEmoteBtn);

	self.categoryCB:addOption("Instant Access");
	for i,v in pairs(ISEmoteRadialMenu.menu) do
		if v.subMenu then -- stuff with submenu
			self.categoryCB:addOptionWithData(v.name, i);
--			for anim, name in pairs(v.subMenu) do
--				self.emoteCB:addOptionWithData(name, anim);
--			end
		else
		end
	end

	self:selectCategory(self.categoryCB);
end

function ISUIEmoteConfig:selectCategory(combo)
	self.emoteCB:clear();
	local selected = combo:getOptionData(combo.selected);
	print("select cat!", selected)
	print(ISEmoteRadialMenu.menu[selected]);
	if ISEmoteRadialMenu.menu[selected] and ISEmoteRadialMenu.menu[selected].subMenu then
		for anim, name in pairs(ISEmoteRadialMenu.menu[selected].subMenu) do
			self.emoteCB:addOptionWithData(name, anim);
		end
	else
		for i, v in pairs(ISEmoteRadialMenu.menu) do
			if not v.subMenu then
				self.emoteCB:addOptionWithData(v.name, i);
			end
		end
	end
end

function ISUIEmoteConfig:prerender()
	ISCollapsableWindow.prerender(self);
end

function ISUIEmoteConfig:render()
	ISCollapsableWindow.render(self);
	
--	self:drawText("Picked Square: " .. self.selectX .. "," .. self.selectY .. "," .. self.selectZ, 10, 25, 1, 1, 1, 1, self.font);
end

function ISUIEmoteConfig:close()
	self:setVisible(false);
	self:removeFromUIManager();
end

function ISUIEmoteConfig:writeToFile()
	print("write to file")
	local file = getFileWriter("emote.ini", true, false);
	
	for i,v in pairs(ISEmoteRadialMenu.menu) do
		if v.subMenu then -- stuff with submenu
			file:write(v.name .. "=" .. "\r\n");
			for anim, name in pairs(v.subMenu) do
				file:write(anim .. ":" .. name .. "\r\n");
			end
		end
	end
	
	file:write("Instant" .. "=" .. "\r\n");
	for i,v in pairs(ISEmoteRadialMenu.menu) do
		if not v.subMenu then
			file:write(i .. ":" .. v.name .. "\r\n");
		end
	end
	
	file:close();
end

function ISUIEmoteConfig:readFile()
	local reader = getFileReader("emote.ini", false)
	if reader then
		while true do
			local line = reader:readLine()
			if line == nil then
				reader:close()
				break
			end
			line = string.trim(line)
--			print("line!", line)
		end
		
		ISEmoteRadialMenu:init(); -- DEBUG
--		ISEmoteRadialMenu.menu = ISEmoteRadialMenu.defaultMenu;
	else
		ISEmoteRadialMenu:init();
		self:writeToFile();
	end
end

--************************************************************************--
--** ISUIEmoteConfig:new
--**
--************************************************************************--
function ISUIEmoteConfig:new(x, y, character)
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
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	return o;
end
