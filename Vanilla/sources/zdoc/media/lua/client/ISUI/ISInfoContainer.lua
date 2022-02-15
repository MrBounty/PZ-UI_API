require "ISUI/ISCollapsableWindow"

---@class ISInfoContainer : ISCollapsableWindow
ISInfoContainer = ISCollapsableWindow:derive("ISInfoContainer");


function ISInfoContainer:initialise()

	--print("init");
	ISCollapsableWindow.initialise(self);

	self.panel = ISPanel:new(0, 16, self.width, self.height-16);
	self.panel:initialise();
	self.panel:setAnchorLeft(true);
	self.panel:setAnchorRight(true);
	self.panel:setAnchorTop(true);
	self.panel:setAnchorBottom(true);
	self:addChild(self.panel);

end


function ISInfoContainer:prerender()

	ISCollapsableWindow.prerender(self);
	self:drawTextCentre(self.title, self.width / 2, 1, 1, 1, 1, 1, UIFont.Small);

end

function ISInfoContainer:new (x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISCollapsableWindow:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	ISInfoContainer.instance = o;
	return o
end

ISInfoContainer.doInfo = function(title, infopanel)
	local inst = ISInfoContainer.get(title);
	inst.panel.child = infopanel;
	infopanel:setX(0);
	infopanel:setY(16);
	infopanel:setWidth(inst.width);
	infopanel:setHeight(inst.height-16);
	inst.panel.child:setAnchorLeft(true);
	inst.panel.child:setAnchorRight(true);
	inst.panel.child:setAnchorTop(true);
	inst.panel.child:setAnchorBottom(true);
	inst:addChild(infopanel);
	inst:uncollapse();
end

ISInfoContainer.get = function (title)
	if ISInfoContainer.instance == nil then
		ISInfoContainer.instance = ISInfoContainer:new(400, 0, 400, 300);
		ISInfoContainer.instance:initialise();
		ISInfoContainer.instance:addToUIManager();
		ISInfoContainer.instance.title = title;
		ISInfoContainer.instance:collapse();
		return ISInfoContainer.instance;
	else
		ISInfoContainer.instance.panel:clearChildren();
		ISInfoContainer.instance.title = title;
		return ISInfoContainer.instance;
	end
end



