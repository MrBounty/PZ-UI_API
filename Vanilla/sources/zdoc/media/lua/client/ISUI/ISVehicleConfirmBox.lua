--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanelJoypad"

---@class ISVehicleConfirmBox : ISPanelJoypad
ISVehicleConfirmBox = ISPanelJoypad:derive("ISVehicleConfirmBox")
ISVehiclePartListBox = ISScrollingListBox:derive("ISVehiclePartListBox")

-----

function ISVehicleConfirmBox:createChildren()
	--ISPanel.createChildren(self)
	
    self.close = ISButton:new(self:getWidth()/2 - 40, 481, 80, 30, getText("UI_Ok"), self, ISVehicleConfirmBox.onOptionMouseDown);
    self.close.internal = "CLOSE";
    self.close.anchorTop = false
    self.close.anchorBottom = true
    self.close:initialise();
    self.close:instantiate();
    self.close.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.close);
end

function ISVehicleConfirmBox:prerender()
	ISPanelJoypad.prerender(self)
	self:drawTexture(self.texSpiffoWarning, 45, 15, 1);
	self:drawTextCentre("Warning", 187, 391, 1, 1, 1, 1, UIFont.Large)
	self:drawTextCentre("Vehicles, and vehicles gameplay, are a work in progress.", 187, 421, 1, 1, 1, 1, UIFont.Medium)
	self:drawTextCentre("Expect bugs.", 187, 451, 1, 1, 1, 1, UIFont.Medium)
	
end


function ISVehicleConfirmBox:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData);
	self.drawJoypadFocus = true
	self:setISButtonForA(self.close)
end

function ISVehicleConfirmBox:onJoypadDown(button, joypadData)
	if button == Joypad.AButton then
		self:onOptionMouseDown(self.close, 0, 0)
	end
end

function ISVehicleConfirmBox:onJoypadDirUp()

end

function ISVehicleConfirmBox:onJoypadDirDown()

end

function ISVehicleConfirmBox:onOptionMouseDown(button, x, y)
	if button.internal == "CLOSE" then
        self:setVisible(false);
        self:removeFromUIManager();
		getCore():setVehiclesWarningShow(true)
		getCore():saveOptions()
		if self.joyfocus then
             self:clearJoypadFocus(self.joyfocus)
             self.joyfocus.focus = self.parent;
             updateJoypadFocus(self.joyfocus);
         end
	end
end

function ISVehicleConfirmBox:new()
	self.__index = self
	height = 526
	width = 375
	y = (getCore():getScreenHeight() / 2) - (height / 2)
	x = (getCore():getScreenWidth() / 2) - (width / 2)
	local o = ISPanelJoypad:new(x, y, width, height)
	setmetatable(o, self)
	o.texSpiffoWarning = getTexture("media/ui/spiffoWarning.png")
	o.backgroundColor = {r=0, g=0, b=0, a=0.85};
	return o
end

