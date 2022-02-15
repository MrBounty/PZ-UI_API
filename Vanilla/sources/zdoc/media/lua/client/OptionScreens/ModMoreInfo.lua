require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"

require "defines"

---@class ModMoreInfo : ISPanel
ModMoreInfo = ISPanel:derive("ModMoreInfo");


function ModMoreInfo:initialise()
	ISPanel.initialise(self);
end
--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function ModMoreInfo:instantiate()

	--self:initialise();
	self.javaObject = UIElement.new(self);
	self.javaObject:setX(self.x);
	self.javaObject:setY(self.y);
	self.javaObject:setHeight(self.height);
	self.javaObject:setWidth(self.width);
	self.javaObject:setAnchorLeft(self.anchorLeft);
	self.javaObject:setAnchorRight(self.anchorRight);
	self.javaObject:setAnchorTop(self.anchorTop);
	self.javaObject:setAnchorBottom(self.anchorBottom);



end

function ModMoreInfo:create(desc)

	local w = self.width * 0.4;
	local tw = self.width;
	self.mainPanel = ISPanel:new((tw-w)/2, 48, w, self.height-64-64);

	self.mainPanel:initialise();
	self.mainPanel:setAnchorRight(true);
	self.mainPanel:setAnchorLeft(true);
	self.mainPanel:setAnchorBottom(true);
	self.mainPanel:setAnchorTop(true);
	self:addChild(self.mainPanel);
	self.mainPanel.backgroundColor = {r=0, g=0, b=0, a=0.8};
	self.mainPanel.borderColor = {r=1, g=1, b=1, a=0.2};

	self.moreInfo = ISRichTextPanel:new(16, 16, self.mainPanel.width - 32, self.mainPanel.height - 64);
	self.moreInfo:initialise();
	self.moreInfo.autosetheight = false;
	self.moreInfo:setAnchorRight(true);
	self.moreInfo:setAnchorLeft(true);
	self.moreInfo:setAnchorBottom(true);
	self.moreInfo:setAnchorTop(true);
	self.mainPanel:addChild(self.moreInfo);

	self.moreInfo.text = desc;
	self.moreInfo:paginate();
	self.moreInfo:addScrollBars();

end

function ModMoreInfo.onOptionMouseDown(button, x, y)
	if button.internal == "BACK" then
		MainScreen.instance.charCreationProfession:setVisible(false);
		MainScreen.instance.worldScreen:setVisible(true);
	end

end

function ModMoreInfo:prerender()


	ISPanel.prerender(self);


end

function ModMoreInfo:new (x, y, width, height)
	-- using a virtual 100 height res for doing the UI, so it resizes properly on different rez's.

	local o = {}

	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = 0;
	o.y = 0;
	o.backgroundColor = {r=0, g=0, b=0, a=0.0};
	o.borderColor = {r=1, g=1, b=1, a=0.0};

	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;

	return o
end
