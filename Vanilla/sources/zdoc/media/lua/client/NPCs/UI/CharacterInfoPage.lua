require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"

require "defines"

---@class CharacterInfoPage : ISPanel
CharacterInfoPage = ISPanel:derive("CharacterInfoPage");


function CharacterInfoPage:initialise()
	ISPanel.initialise(self);

end

--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function CharacterInfoPage:createChildren()

	self.avatarPanel = ISPanel:new(16, 16, 96, 96);
	self.avatarPanel:initialise();

	self:addChild(self.avatarPanel);
	self.avatarPanel.backgroundColor = {r=0, g=0, b=0, a=0.8};
	self.avatarPanel.borderColor = {r=1, g=1, b=1, a=0.2};
	self.avatarPanel.render = CharacterInfoPage.drawAvatar;

	self:createAvatar();
end

function CharacterInfoPage:prerender()

	ISPanel.prerender(self);

	self:drawText(self.desc:getForename().." "..self.desc:getSurname(), 128, 18, 1, 1, 1, 1, UIFont.Medium);

	local obs = self.desc:getObservations();

	local y = 128;
	for i=0, obs:size()-1 do
		local ob = obs:get(i);
		if ob ~= nil then
			self:drawText(StringReplacer.DoCharacter(ob:getDescription(), self.desc), 16, y, 1, 1, 1, 1, UIFont.Small);
		else
			--print("error: null observation");
		end
		y = y + 21;
	end
end
function CharacterInfoPage:createAvatar()

	self.avatar = IsoSurvivor.new(self.desc, nil, 0, 0, 0, false);

	self.avatar:setDir(IsoDirections.SE);
	self.avatar:PlayAnimWithSpeed("Idle", 0.1);


end

function CharacterInfoPage:drawAvatar()


	local x = self:getAbsoluteX();
	local y = self:getAbsoluteY();
	x = x + 96/2;
	y = y + 105;

	MainScreen.instance.avatar:drawAt(x,y);

end
function CharacterInfoPage:new (x, y, width, height, desc)
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
	o.desc = desc;
	return o
end

CharacterInfoPage.doInfo = function (desc)
	ISInfoContainer.doInfo(desc:getForename().." "..desc:getSurname(), CharacterInfoPage:new(0, 0, 100, 100, desc));
end
