--
-- Created by IntelliJ IDEA.
-- User: RJ
-- Time: 10:19
-- To change this template use File | Settings | File Templates.
--

require "ISUI/ISPanelJoypad"

---@class ISLootStreetTestUI : ISCollapsableWindow
ISLootStreetTestUI = ISCollapsableWindow:derive("ISLootStreetTestUI");

--************************************************************************--
--** ISLootStreetTestUI:initialise
--**
--************************************************************************--

function ISLootStreetTestUI:createChildren()
	local btnWid = 100
	local btnHgt = 25
	local padBottom = self:resizeWidgetHeight() + 10
	
	ISCollapsableWindow.createChildren(self)
	
	self.town = ISComboBox:new(10, 60, 100, 20)
	self.town.font = UIFont.Small
	self.town:initialise()
	self.town:instantiate()
	self:addChild(self.town)
	
	self.town:addOption("Muldraugh")
	self.town:addOption("Westpoint")
--	self.town:addOption("Rosewood")

	self.houseType = ISComboBox:new(self.town.x + self.town.width + 10, self.town.y, 100, 20)
	self.houseType.font = UIFont.Small
	self.houseType:initialise()
	self.houseType:instantiate()
	self:addChild(self.houseType)

	self.houseType:addOption("Small House")
	self.houseType:addOption("Medium House")
	self.houseType:addOption("Big House")
	
	self.houseNbr = ISTextEntryBox:new("1", self.houseType.x + self.houseType.width + 10, self.houseType.y, 50, 20);
	self.houseNbr:initialise();
	self.houseNbr:instantiate();
	self.houseNbr:setOnlyNumbers(true);
	self:addChild(self.houseNbr);
	
	self.onlyjunk = ISTickBox:new(self.houseNbr.x + self.houseNbr.width + 10, self.town.y, 100, 300, "Only Junk")
	self.onlyjunk:initialise()
	self.onlyjunk:addOption("Only Junk", "onlyjunk")
	self:addChild(self.onlyjunk);
	
	self.lootType = ISTickBox:new(self.width - 200 + 20, self.town.y, 100, 300, "Loot Type:")
	self.lootType.anchorLeft = false
	self.lootType.anchorRight = true
	self.lootType:initialise()
	self.lootType:addOption("Other", "other")
	self.lootType:setSelected(1, true);
	self.lootType:addOption("Food", "food")
	self.lootType:setSelected(2, true);
	self.lootType:addOption("CannedFood", "cannedfood")
	self.lootType:setSelected(3, true);
	self.lootType:addOption("Melee Weapons", "meleeweapon")
	self.lootType:setSelected(4, true);
	self.lootType:addOption("Ranged Weapons", "rangedweapon")
	self.lootType:setSelected(5, true);
	self.lootType:addOption("Ammo", "ammo")
	self.lootType:setSelected(6, true);
	self.lootType:addOption("Literature", "literature")
	self.lootType:setSelected(7, true);
	self.lootType:addOption("Medical", "medical")
	self.lootType:setSelected(8, true);
	self.lootType:setWidthToFit()
	self:addChild(self.lootType)

	self.richtext = ISRichTextPanel:new(10, 100, self.width - 200, self.height - 150);
	self.richtext.anchorRight = true
	self.richtext.anchorBottom = true
	self.richtext:initialise();
	self.richtext.autosetheight = false;
	self.richtext.clip = true
	self.richtext.background = false;
	self:addChild(self.richtext);
	self.richtext:addScrollBars();
	
	self.richtext.text = "";
	self.richtext:paginate();

	self.start = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, "Generate Loot", self, ISLootStreetTestUI.startGenerate);
	self.start.anchorTop = false
	self.start.anchorBottom = true
	self.start:initialise();
	self.start:instantiate();
	self.start.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.start);
	
	self.close = ISButton:new(self:getWidth() - btnWid - 20, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, "Close", self, ISLootStreetTestUI.close);
	self.close.anchorLeft = false
	self.close.anchorRight = true
	self.close.anchorTop = false
	self.close.anchorBottom = true
	self.close:initialise();
	self.close:instantiate();
	self.close.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.close);
end

function ISLootStreetTestUI:startGenerate()
	self.richtext.text = "";
	self.kitchenProclist = {};
	self.kitchenContainer = ItemContainer.new("kitchen", self.chr:getCurrentSquare(), self.chr);
	self.bathroomContainer = ItemContainer.new("bathroom", self.chr:getCurrentSquare(), self.chr);
	self.bedroomContainer = ItemContainer.new("bedroom", self.chr:getCurrentSquare(), self.chr);
	self.shedContainer = ItemContainer.new("shed", self.chr:getCurrentSquare(), self.chr);
	self.totalList = {};
	self.totalList["food"] = {};
	self.totalList["meleeweapon"] = {};
	self.totalList["rangedweapon"] = {};
	
	for i=1, tonumber(self.houseNbr:getInternalText()) do
		self:generateBuilding();
	end
	
	self:doRichTextList(self.kitchenContainer, "Kitchen");
	self:doRichTextList(self.bathroomContainer, "Bathroom");
	self:doRichTextList(self.bedroomContainer, "Bedroom");
	
	if self.houseType.selected == 3 then
		self:doRichTextList(self.shedContainer, "Shed");
	end

	self.richtext:paginate();
end

function ISLootStreetTestUI:generateBuilding()
	-- kitchen
	self:doRoom(self.kitchenContainer, "kitchen", "counter", "KitchenDishes");
	self:doRoom(self.kitchenContainer, "kitchen", "counter", "KitchenPots");
	self:doRoom(self.kitchenContainer, "kitchen", "counter", "KitchenCannedFood");
	if self.houseType.selected > 1 then
		self:doRoom(self.kitchenContainer, "kitchen", "counter", self:getRandomKitchenCounter());
		self:doRoom(self.kitchenContainer, "kitchen", "shelves", "KitchenDishes");
		if self.houseType.selected == 3 then
			self:doRoom(self.kitchenContainer, "kitchen", "counter", self:getRandomKitchenCounter());
			self:doRoom(self.kitchenContainer, "kitchen", "shelves", "KitchenDryFood");
		end
	end

	-- bathroom
	self:doRoom(self.bathroomContainer, "bathroom", "counter", nil);
	if self.houseType.selected > 1 then
		self:doRoom(self.bathroomContainer, "bathroom", "counter", nil);
	end

	-- bedroom
	if self.houseType.selected == 1 then
		self:doRoom(self.bedroomContainer, "bedroom", "wardrobe", "WardrobeRedneck");
		self:doRoom(self.bedroomContainer, "bedroom", "wardrobe", "WardrobeRedneck");
	elseif self.houseType.selected == 2 then
		self:doRoom(self.bedroomContainer, "bedroom", "wardrobe", "WardrobeMan");
		self:doRoom(self.bedroomContainer, "bedroom", "wardrobe", "WardrobeWoman");
		self:doRoom(self.bedroomContainer, "bedroom", "wardrobe", "WardrobeChild");
	else
		self:doRoom(self.bedroomContainer, "bedroom", "wardrobe", "WardrobeManClassy");
		self:doRoom(self.bedroomContainer, "bedroom", "wardrobe", "WardrobeWomanClassy");
		self:doRoom(self.bedroomContainer, "bedroom", "wardrobe", "WardrobeChild");
		self:doRoom(self.bedroomContainer, "bedroom", "wardrobe", "WardrobeChild");
	end

	-- shed
	if self.houseType.selected == 3 then
		self:doRoom(self.shedContainer, "shed", "other", nil);
		self:doRoom(self.shedContainer, "shed", "other", nil);
	end
end

function ISLootStreetTestUI:concatList(list)
	local newlist = {};
	local nbr = 0;
	local counts = {};
	for i=0, list:getItems():size() - 1 do
		local item = list:getItems():get(i);
		if self:itemValidForList(item) then
			local name = item:getDisplayName();
			-- add if we got magazine in this weapon
			if instanceof(item, "HandWeapon") and item:isContainsClip() then
				name = name .. " (with mag)";
			end
			if counts[name] then
				counts[name] = counts[name] + 1;
			else
				table.insert(newlist, name);
				counts[name] = 1;
			end
			nbr = nbr + 1;
		end
	end
	table.sort(newlist, function(a,b) return not string.sort(a,b) end)
	return newlist, counts, nbr;
end

function ISLootStreetTestUI:itemValidForList(item)
	local type = item:getStringItemType();
	if type == "Other" and self.lootType:isSelected(1) then
		return true;
	elseif type == "Food" and self.lootType:isSelected(2) then
		return true;
	elseif type == "CannedFood" and self.lootType:isSelected(3) then
		return true;
	elseif type == "MeleeWeapon" and self.lootType:isSelected(4) then
		return true;
	elseif type == "RangedWeapon" and self.lootType:isSelected(5) then
		return true;
	elseif type == "Ammo" and self.lootType:isSelected(6) then
		return true;
	elseif type == "Literature" and self.lootType:isSelected(7) then
		return true;
	elseif type == "Medical" and self.lootType:isSelected(8) then
		return true;
	end
		
	return false;
end

function ISLootStreetTestUI:getRandomKitchenCounter()
	local nbr = ZombRand(4);
	if nbr == 0 then
		return "KitchenDryFood";
	elseif nbr == 1 then
		return "KitchenBreakfast";
	elseif nbr == 2 then
		return "KitchenBottles";
	else
		return "KitchenRandom";
	end
end

function ISLootStreetTestUI:doRichTextList(container, roomName)
	local list, counts, nbr = self:concatList(container);
	self.richtext.text = self.richtext.text .. " " .. roomName .. " ( " .. nbr .. " items): <LINE> ";
	for _,name in pairs(list) do
		self.richtext.text = self.richtext.text .. " <INDENT:10> " .. name .. " x" .. counts[name] .. " <LINE> ";
	end
	self.richtext.text = self.richtext.text .. " <INDENT:0> <LINE> ";
end

function ISLootStreetTestUI:doRoom(container, roomName, contName, procName)
	if not self.onlyjunk:isSelected(1) then
		local contdistrib = ItemPickerJava.getItemContainer(roomName, contName, procName, false);
		if contdistrib then
			ItemPickerJava.doRollItem(contdistrib, container, self:getLootDensity(), self.chr, true, false);
		end
	end
	local contdistrib = ItemPickerJava.getItemContainer(roomName, contName, procName, true);
	if contdistrib then
		ItemPickerJava.doRollItem(contdistrib, container, self:getLootDensity(), self.chr, true, false);
	end
end

function ISLootStreetTestUI:getLootDensity()
	-- Muldraugh
	if self.town.selected == 1 then
		if self.houseType.selected == 1 then
			return 1;
		elseif self.houseType.selected == 2 then
			return 4;
		else
			return 9;
		end
	elseif self.town.selected == 2 then -- WP
		if self.houseType.selected == 1 then
			return 4;
		elseif self.houseType.selected == 2 then
			return 8;
		else
			return 12;
		end
	end
end

function ISLootStreetTestUI:close()
	self:removeFromUIManager();
end

function ISLootStreetTestUI:update()
	ISCollapsableWindow.update(self);
end

function ISLootStreetTestUI:render()
	ISCollapsableWindow.render(self);

	self:drawTextCentre("Generate Loot" , self.width / 2, 20, 1, 1, 1, 1, UIFont.Medium);
	
	local txt = "Kitchen: Counter x";
	if self.houseType.selected == 1 then
		txt = txt .. "3";
	elseif self.houseType.selected == 2 then
		txt = txt .. "4";
		txt = txt .. " Shelves x1";
	else
		txt = txt .. "4";
		txt = txt .. " Shelves x2";
	end
	
	txt = txt .. "; Bathroom: Counter x";
	if self.houseType.selected == 1 then
		txt = txt .. "1";
	else
		txt = txt .. "2";
	end
	
	txt = txt .. "; Bedroom: Wardrobe x";
	if self.houseType.selected == 1 then
		txt = txt .. "2 (Redneck)";
	elseif self.houseType.selected == 2 then
		txt = txt .. "3 (Normal + Kid)";
	else
		txt = txt .. "4 (Classy + Kid)";
	end

	if self.houseType.selected == 3 then
		txt = txt .. " Shed: Counter x2";
	end
	self:drawText(txt, self.town.x, self.town.y + self.town.height + 10, 1,1,1,1, UIFont.Small);
end

--************************************************************************--
--** ISLootStreetTestUI:new
--**
--************************************************************************--
function ISLootStreetTestUI:new(x, y, character)
	local o = {}
	local width = 1000;
	local height = getCore():getScreenHeight() - 100;
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
	o.character = character;
	o.chr = character;
	o.moveWithMouse = true;
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	return o;
end
