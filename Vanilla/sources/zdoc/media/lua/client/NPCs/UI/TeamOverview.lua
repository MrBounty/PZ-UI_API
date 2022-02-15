require "ISUI/ISCollapsableWindow"

---@class TeamOverview : ISCollapsableWindow
TeamOverview = ISCollapsableWindow:derive("TeamOverview");


function TeamOverview:initialise()

	--print("init");
	ISCollapsableWindow.initialise(self);

	self.a = SurvivorFactory.CreateSurvivor();
	self.b = SurvivorFactory.CreateSurvivor();
	self.c = SurvivorFactory.CreateSurvivor();
	self.group = SurvivorGroup.new(self.leader);

	self.group:addMember(self.a);
	self.group:addMember(self.b);
	self.group:addMember(self.c);
end

--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function TeamOverview:createChildren()

	self.memberList = ISScrollingListBox:new(0, 16, self.width, self.height-16);
	self.memberList:initialise();
	self.memberList:setAnchorLeft(true);
	self.memberList:setAnchorRight(true);
	self.memberList:setAnchorTop(true);
	self.memberList:setAnchorBottom(true);
	--self.memberList.doDrawItem = TeamOverview.drawMember;
	self:addChild(self.memberList);
	self.memberList.itemheight = 32;

	for i=0, self.group:getMembers():size()-1 do
		local m = self.group:getMembers():get(i);
		self.memberList:addItem(m:getForename().." "..m:getSurname(), m);
	end

end

function TeamOverview:prerender()

	ISCollapsableWindow.prerender(self);
	self:drawTextCentre("Survivor Group", self.width / 2, 1, 1, 1, 1, 1, UIFont.Small);

end

function TeamOverview:drawMember(y, item, alt)
	self:drawRectBorder(0, (y), self:getWidth(), self.itemheight-1, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);

end

function TeamOverview:new (x, y, width, height, leaderInstance)
	local o = {}
	--o.data = {}
	o = ISCollapsableWindow:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self

	o.leader = leaderInstance;
	return o
end



