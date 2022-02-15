require "ISUI/ISPanel"

---@class ISOptionPanel : ISPanel
ISOptionPanel = ISPanel:derive("ISOptionPanel");

--************************************************************************--
--** ISOptionPanel:initialise
--**
--************************************************************************--

function ISOptionPanel:initialise()
	ISPanel.initialise(self);
end


--************************************************************************--
--** ISOptionPanel:render
--**
--************************************************************************--
function ISOptionPanel:prerender()

	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	self:drawRect(0, 0, self.width, 1, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	self:drawRect(0, self.height-1, self.width, 1, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	self:drawRect(0, 0, 1, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	self:drawRect(0+self.width-1, 0, 1, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);

end

function ISOptionPanel:addCombo(name, options, selected, target, onchange)

	local tot = self.width - (self.marginLeft + self.marginRight);
	local labels = tot * 0.3;

	local label = ISLabel:new(self.marginLeft, self.marginTop + self.addY, labels - 20, 18, name, 1, 1, 1, 1);
	label:initialise();
	self:addChild(label);
	local panel2 = ISComboBox:new(self.marginLeft + labels, self.marginTop + self.addY, tot - labels, 18, target, onchange);
	panel2:initialise();
	for i, k in ipairs(options) do
		panel2:addOption(k);
	end

	panel2.selected = selected;
	self:addChild(panel2);
	self.addY = self.addY + 18;
end

--************************************************************************--
--** ISOptionPanel:onMouseUp
--**
--************************************************************************--

function ISOptionPanel:new (x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.backgroundColor = {r=0, g=0, b=0, a=0.5};
	o.borderColor = {r=1, g=1, b=1, a=0.7};
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.options = {};
	o.selected = 0;
	o.hover = 0;
	o.expanded = false;
	o.marginLeft = 16;
	o.marginRight = 16;
	o.marginTop = 16;
	o.marginBottom = 16;
	o.addY = 0;
	return o
end

testResolutionChange = function (target, box)
	if box.options[box.selected] ~= nil then
		getCore():setResolution(box.options[box.selected]);
	end
end

-- Event method to check game mode and create K&B tutorial panel if necessary.
--testoptionPanel = function ()

--	local panel2 = ISOptionPanel:new(150, 150, 400, 400);
--	panel2:initialise();
--	panel2:addToUIManager();
--	local modes = getCore():getScreenModes();
--	table.sort(modes);
--	panel2:addCombo("Resolution", modes, 1, nil, testResolutionChange);
--panel2 = ISInventoryPage:new(300, 300, 400+32, 400, getPlayer():getInventory());
--panel2:initialise();
--panel2:addToUIManager();
--end

--Events.OnMainMenuEnter.Add(testoptionPanel);

