--
-- Created by IntelliJ IDEA.
-- User: RJ
-- To change this template use File | Settings | File Templates.
--

require "ISUI/ISPanelJoypad"

---@class ISAttachedItemsUI : ISCollapsableWindow
ISAttachedItemsUI = ISCollapsableWindow:derive("ISAttachedItemsUI");

--************************************************************************--
--** ISAttachedItemsUI:initialise
--**
--************************************************************************--

function ISAttachedItemsUI:createChildren()
	local btnWid = 100
	local btnHgt = 25
	local padBottom = 20
	
	ISCollapsableWindow.createChildren(self)
	
	self.datas = ISScrollingListBox:new(10, 50, self.width - 20, self.height - 170);
	self.datas:initialise();
	self.datas:instantiate();
	self.datas.itemheight = 22;
	self.datas.selected = 0;
	self.datas.joypadParent = self;
	self.datas.font = UIFont.NewSmall;
	self.datas.doDrawItem = self.drawDatas;
	self.datas.drawBorder = true;
	self:addChild(self.datas);
	self.datas:addColumn("Item", 0);
	self.datas:addColumn("Location", 200);

	self:populateList();
	
	self.itemTypeLabel = ISLabel:new(10, self.datas.y + self.datas.height + 5, 10, "Item Type" ,1,1,1,1,UIFont.Small, true);
	self:addChild(self.itemTypeLabel);

	self.itemType = ISTextEntryBox:new("", 80, self.datas.y + self.datas.height + 5, 200, 10);
	self.itemType:initialise();
	self.itemType:instantiate();
	self.itemType:setText("Base.");
	self.itemType:setClearButton(true);
	self:addChild(self.itemType);
	
	self.locationLabel = ISLabel:new(10, self.datas.y + self.datas.height + 30, 10, "Location" ,1,1,1,1,UIFont.Small, true);
	self:addChild(self.locationLabel);

	self.location = ISComboBox:new(80, self.datas.y + self.datas.height + 25, 200, 20)
	self.location.font = UIFont.Small
	self.location:initialise()
	self.location:instantiate()
	self.location.target = self.location
	self:addChild(self.location)
	
	for i=0,self.chr:getAttachedItems():getGroup():size()-1 do
		self.location:addOption(self.chr:getAttachedItems():getGroup():getLocationByIndex(i):getId())
	end

	self.add = ISButton:new(10, self:getHeight() - padBottom - btnHgt - 22, btnWid, btnHgt, "Add", self, ISAttachedItemsUI.onAddOnChar);
	self.add.anchorTop = false
	self.add.anchorBottom = true
	self.add:initialise();
	self.add:instantiate();
	self.add.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.add);
	
	self.onzombie = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, "Add to Zombies", self, ISAttachedItemsUI.onAddZombie);
	self.onzombie.anchorTop = false
	self.onzombie.anchorBottom = true
	self.onzombie:initialise();
	self.onzombie:instantiate();
	self.onzombie.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.onzombie);
	
	self.remove = ISButton:new(self.add.x + btnWid + 5, self.add.y, btnWid, btnHgt, "Remove", self, ISAttachedItemsUI.onRemove);
	self.remove.anchorTop = false
	self.remove.anchorBottom = true
	self.remove:initialise();
	self.remove:instantiate();
	self.remove.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.remove);
	
	self.removeZombie = ISButton:new(self.add.x + btnWid + 5, self.onzombie.y, btnWid, btnHgt, "Remove on Zed", self, ISAttachedItemsUI.onRemoveZombie);
	self.removeZombie.anchorTop = false
	self.removeZombie.anchorBottom = true
	self.removeZombie:initialise();
	self.removeZombie:instantiate();
	self.removeZombie.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.removeZombie);
	
--
--	self.restoreEnd = ISButton:new(self:getWidth() - btnWid - 20, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, "Restore Endurance", self, ISAttachedItemsUI.restoreEndurance);
--	self.restoreEnd.internal = "RESTOREENDURANCE";
--	self.restoreEnd.anchorTop = false
--	self.restoreEnd.anchorBottom = true
--	self.restoreEnd:initialise();
--	self.restoreEnd:instantiate();
--	self.restoreEnd.borderColor = {r=1, g=1, b=1, a=0.1};
--	self:addChild(self.restoreEnd);
--
--	self:setInfo("Click on start timer to start the trip, once you're done, click on stop timer. \n When you're done, you should click on restore endurance to start from scratch as endurance regen while time pass. \n \n The total current speed variable is updated only when running/sprinting, it could show wrong when idle/walking as it's not used.");
end

function ISAttachedItemsUI:onAddOnChar()
	self:onAdd(self.chr);
end

function ISAttachedItemsUI:onAdd(char)
	local item = InventoryItemFactory.CreateItem(self.itemType:getInternalText())
	if not item then
		local modal = ISModalDialog:new(0,0, 250, 150, "Item " .. self.itemType:getInternalText() .. " doesn't exist", false);
		modal:initialise()
		modal:addToUIManager()
		return;
	end
	
	char:setAttachedItem(self.location:getSelectedText(), item);
	self:populateList();
end

function ISAttachedItemsUI:onAddZombie()
	local currentGS = self.chr:getCurrentSquare()
	for x=currentGS:getX()-10,currentGS:getX()+10 do
		for y=currentGS:getY()-10,currentGS:getY()+10 do
			local gs = getCell():getGridSquare(x,y,self.chr:getZ());
			if gs then
				for i=0,gs:getMovingObjects():size()-1 do
					local zombie = gs:getMovingObjects():get(i);
					if instanceof(zombie, "IsoZombie") then
						self:onAdd(zombie);
					end
				end
			end
		end
	end
end

function ISAttachedItemsUI:onRemoveZombie()
	local currentGS = self.chr:getCurrentSquare()
	for x=currentGS:getX()-10,currentGS:getX()+10 do
		for y=currentGS:getY()-10,currentGS:getY()+10 do
			local gs = getCell():getGridSquare(x,y,self.chr:getZ());
			if gs then
				for i=0,gs:getMovingObjects():size()-1 do
					local zombie = gs:getMovingObjects():get(i);
					if instanceof(zombie, "IsoZombie") then
						for j=zombie:getAttachedItems():size(),1,-1 do
							local attachedItem = zombie:getAttachedItems():get(j-1);
							zombie:removeAttachedItem(attachedItem:getItem());
						end
					end
				end
			end
		end
	end
end

function ISAttachedItemsUI:onRemove()
	if self.datas.selected <= 0 then
		return;
	end
	
	local item = self.datas.items[self.datas.selected].item;
	self.chr:removeAttachedItem(item:getItem());
	
	self:populateList();
end

function ISAttachedItemsUI:populateList()
	self.datas:clear();
	for i=0,self.chr:getAttachedItems():size()-1 do
		local attachedItem = self.chr:getAttachedItems():get(i);
		self.datas:addItem(attachedItem:getItem():getType(), attachedItem);
	end
end

function ISAttachedItemsUI:drawDatas(y, item, alt)
	local a = 0.9;
	
	--    self.parent.selectedStash = nil;
	self:drawRectBorder(0, (y), self:getWidth(), self.itemheight - 1, a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	
	if self.selected == item.index then
		self:drawRect(0, (y), self:getWidth(), self.itemheight - 1, 0.3, 0.7, 0.35, 0.15);
	end
	
	self:drawText( item.item:getItem():getType(), 10, y + 2, 1, 1, 1, a, self.font);
	self:drawText(item.item:getLocation(), self.columns[2].size + 3, y + 4, 1, 1, 1, a, self.font);
	
	return y + self.itemheight;
end

function ISAttachedItemsUI:update()
	ISCollapsableWindow.update(self);
	self.remove.enable = false;
	self.add.enable = false;

	if self.datas.selected > 0 then
		self.remove.enable = true;
	end
	if self.itemType:getText() and self.itemType:getText() ~= "" then
		self.add.enable = true;
	end
--	print(self.datas.selected)
end

function ISAttachedItemsUI:render()
	ISCollapsableWindow.render(self);
end

function ISAttachedItemsUI:close()
	self:setVisible(false);
	self:removeFromUIManager();
end

--************************************************************************--
--** ISAttachedItemsUI:new
--**
--************************************************************************--
function ISAttachedItemsUI:new(x, y, character)
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
	o.character = character;
	o.chr = character;
	o.moveWithMouse = true;
	o.anchorLeft = true;
	o.anchorRight = true;
	o.anchorTop = true;
	o.anchorBottom = true;
	return o;
end
