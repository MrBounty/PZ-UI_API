--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISCollapsableWindow"
require "ISUI/ISPanelJoypad"

---@class ISCollapsableWindowJoypad : ISPanelJoypad
ISCollapsableWindowJoypad = ISPanelJoypad:derive("ISCollapsableWindowJoypad")

-- HACK
for k,v in pairs(ISCollapsableWindow) do
	ISCollapsableWindowJoypad[k] = v
end

function ISCollapsableWindowJoypad:initialise()
	ISPanelJoypad.initialise(self)
end

function ISCollapsableWindowJoypad:new (x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height);
	o.x = x;
	o.y = y;
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
	o.backgroundColor = {r=0, g=0, b=0, a=0.8};
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.widgetTextureColor = {r = 1, g = 1, b = 1, a = 1};
	o.titlebarbkg = getTexture("media/ui/Panel_TitleBar.png");
	o.statusbarbkg = getTexture("media/ui/Panel_StatusBar.png");
	o.resizeimage = getTexture("media/ui/Panel_StatusBar_Resize.png");
	o.invbasic = getTexture("media/ui/Icon_InventoryBasic.png");
	o.closeButtonTexture = getTexture("media/ui/Dialog_Titlebar_CloseIcon.png");
	o.collapseButtonTexture = getTexture("media/ui/Panel_Icon_Collapse.png");
	o.pinButtonTexture = getTexture("media/ui/Panel_Icon_Pin.png");
    o.infoBtn = getTexture("media/ui/Panel_info_button.png");
	o.pin = true;
	o.isCollapsed = false;
	o.collapseCounter = 0;
	o.title = nil;
    o.viewList = {}
    o.resizable = true
    o.drawFrame = true
	o.clearStentil = true;
	o.titleFont = UIFont.Small
	o.titleFontHgt = getTextManager():getFontHeight(o.titleFont)
	return o
end

