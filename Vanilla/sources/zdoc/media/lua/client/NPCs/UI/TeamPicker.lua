require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"

require "defines"

---@class TeamPicker : ISPanel
TeamPicker = ISPanel:derive("TeamPicker");


function TeamPicker:initialise()
	ISPanel.initialise(self);

	self.a = SurvivorFactory.CreateSurvivor();
	self.b = SurvivorFactory.CreateSurvivor();
	self.c = SurvivorFactory.CreateSurvivor();
	self.group = SurvivorGroup.new(self.leader);

	self.group:addMember(self.a);
	self.group:addMember(self.b);
	self.group:addMember(self.c);
end

function TeamPicker:onOptionMouseDown(button, x, y)
	--print("clicked button");
	--print(button.internal);
	if button.internal == "OK" then
		self:setVisible(false);
	end

end

--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function TeamPicker:createChildren()



	self.teamMembers = ISTickBox:new(0, 10, self.width, self.height-30, self.headerText, self, TeamPicker.changeOption);
	self.teamMembers:initialise();

	local memberList = self.group:getMembers();

	for i = 0, memberList:size()-1 do
		local member = memberList:get(i);
		if member ~= self.except then
			self.teamMembers:addOption(member:getForename().." "..member:getSurname(), member);
		end
	end

	self:addChild(self.teamMembers);

	self.playButton = ISButton:new(self.width - 116, self.height-30, 100, 25, "OK", self, TeamPicker.onOptionMouseDown);
	self.playButton.internal = "OK";
	self.playButton:initialise();
	self.playButton:instantiate();
	self.playButton:setAnchorLeft(false);
	self.playButton:setAnchorRight(true);
	self.playButton:setAnchorTop(false);
	self.playButton:setAnchorBottom(true);
	self.playButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.playButton);



end

function TeamPicker:changeOption(option)

end

function TeamPicker:create()

end

function TeamPicker:prerender()

	ISPanel.prerender(self);

end

function TeamPicker:onOptionMouseDown(button, x, y)
	if button.internal == "OK" then
		self:setVisible(false);
		--getScriptManager():PlayInstanceScript(self.scriptOnOk, self.scriptOnOk, {Leader = self.leader, Chose1 = self.chose1, Chose2 = self.chose2, Chose3 = self.chose3, Chose4 = self.chose4, Chose5 = self.chose5 } );

	end
end

function TeamPicker:new (x, y, width, height, headerText, except, scriptOnOk, leaderInstance)
	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.headerText = headerText;
	o.except = except;
	o.leader = leaderInstance;
	o.scriptOnOk = scriptOnOk;
	return o
end
