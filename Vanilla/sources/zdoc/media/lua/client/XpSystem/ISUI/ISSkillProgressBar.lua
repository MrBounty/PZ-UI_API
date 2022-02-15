--***********************************************************
--**                    ROBERT JOHNSON                     **
--** A bar wich display square for each lvl of a skill (you need one for each skills) **
--***********************************************************

require "ISUI/ISPanel"

---@class ISSkillProgressBar : ISPanel
ISSkillProgressBar = ISPanel:derive("ISSkillProgressBar");

ISSkillProgressBar.alpha = 0.0;
ISSkillProgressBar.upAlpha = true;


function ISSkillProgressBar:initialise()
	ISPanel.initialise(self);
end

function ISSkillProgressBar:render()
	self:renderPerkRect();
	if self.message ~= nil then
		if self.tooltip == nil then
			self.tooltip = ISToolTip:new();
			self.tooltip:initialise();
			self.tooltip:addToUIManager();
			self.tooltip:setOwner(self)
		end
		self.tooltip.description = self.message;
	end
end

-- when you click on an available skill
function ISSkillProgressBar:onMouseUp(x, y)
	-- our selected lvl
	local lvlSelected = math.floor(self:getMouseX()/20);
	-- if you have skill pts and the skill is ready to evolve and if you clicked on the right lvl
	if self.xp >= self.xpForLvl and self.level == lvlSelected and not self.perk:isPassiv() then
		self.char:LevelPerk(self.perk:getType());
		-- recalcul some stats
		-- our current perk lvl
		self.level = self.char:getPerkLevel(self.perk:getType());
		-- how much xp we need for the next lvl
		self.xpForLvl = ISSkillProgressBar.getXpForLvl(self.perk, self.level);
		-- reset the tooltip
		self.message = nil;
		if self.tooltip ~= nil then
			self.tooltip:setVisible(false);
			self.tooltip:removeFromUIManager();
			self.tooltip = nil;
		end
	end
end

function ISSkillProgressBar:updateTooltip(lvlSelected)
	-- we display the correct message
	self.message = self.perk:getName() .. xpSystemText.lvl .. lvlSelected + 1;
	-- first if the lvl is unlocked
	if lvlSelected < self.level then
		self.message = self.message .. " <LINE> " .. xpSystemText.unlocked;
	-- if we selected the level wich is in progress, we display the xp we already got / xp needed for lvl
	elseif self.level == lvlSelected then
		self.message = self.message .. " <LINE> " .. getText("IGUI_XP_tooltipxp", round(self.xp, 2), self.xpForLvl);
		-- if we got a multiplier, we show it
		local multiplier = self.char:getXp():getMultiplier(self.perk:getType());
		if multiplier > 0 then
			self.message = self.message .. " <LINE> " .. getText("IGUI_skills_Multiplier", round(multiplier, 2));
		end
	else
		self.message = self.message .. " <LINE> " .. xpSystemText.locked;
    end
    local xpBoost = self.char:getXp():getPerkBoost(self.perk:getType());
    local percentage = nil;
    if xpBoost == 1 then
        percentage = "75%";
    elseif xpBoost == 2 then
        percentage = "100%";
    elseif xpBoost == 3 then
        percentage = "125%";
    end

    if percentage then
        self.message = self.message .. " <LINE> " .. getText("IGUI_XP_tooltipxpboost", percentage);
    end
end

function ISSkillProgressBar:onMouseMove(dx, dy)
	if not self:isMouseOver() then -- handle other windows in front
		self:onMouseMoveOutside(dx, dy)
		return
	end
	-- display the tooltip
	-- first we get the square the mouse is on
	local lvlSelected = math.floor(self:getMouseX()/20);
	if lvlSelected > 9 then
		return;
	end
	self:updateTooltip(lvlSelected)
end

function ISSkillProgressBar:onMouseMoveOutside(dx, dy)
	self.message = nil;
	if self.tooltip ~= nil then
		self.tooltip:setVisible(false);
		self.tooltip:removeFromUIManager();
		self.tooltip = nil;
	end
end

function ISSkillProgressBar:activate()
end

function ISSkillProgressBar:renderPerkRect()
	local x = 0;
	local y = 0;

	-- perks level up automatically, update the UI
	if self.level ~= self.char:getPerkLevel(self.perk:getType()) then
		self.level = self.char:getPerkLevel(self.perk:getType())
		self.xpForLvl = ISSkillProgressBar.getXpForLvl(self.perk, self.level)
		self.parent.lastLeveledUpPerk = self.perk
		self.parent.lastLevelUpTime = 1
	end
	
	-- how much xp we already aquire for this perk
	self.xp = ISSkillProgressBar.getPerkXp(self);

	if self.xp > self.xpForLvl then
		self.xp = self.xpForLvl;
	end

	-- we start to render our first rect : all the lvl we already got, we render them in a simple white rect
	-- ex : if we're lvl 3, we gonna render 2 (lvl 1 and 2) white rect
	for i=0,self.level - 1 do
--~ 		self:drawRect(x, y, 19, 19, 1.0, 1.0, 1.0, 1.0);
		if self.parent.lastLeveledUpPerk == self.perk then
			--this fades over time to return to the original colour
			self:drawTexture(self.UnlockedSkill, x, y, 1,1-self.parent.lastLevelUpTime,1,1-self.parent.lastLevelUpTime);
		else
			self:drawTexture(self.UnlockedSkill, x, y, 1,1,1,1);
		end
		x = x + 20;
	end
	-- the most important square : the one in progress !
	-- for this one we got multiple choice :
	-- * In progress : light grey rect filled with light grey depending on the progress
	-- * In faster progress : if you read a book 'bout this skill or if a npc trained you, it's a light blue/filled with light blue too
	-- * Finished but no skills pts available : a white rect full filled with light grey
	-- * Finished and ready to take : a white rect that glow filled with light grey
	if self.level < 10 then
		-- we gonna fill with light grey our rect, depending on the progress of our lvl (50% xp mean a rect filled at 50%)
		-- this width correspond to 1% xp progress
		local sliceWidth = 18 / 100;
		-- our progress into the current lvl in %
		local percentProgress = (self.xp / self.xpForLvl) * 100;
		if percentProgress < 0 then percentProgress = 0 end
		-- we now draw our rect with the correct width
		-- our border, a bit darker than the filled rect or if the skill is rdy to unlock it's a white border
		if percentProgress == 100 then
			if self.perk:isPassiv() then
				self:drawTexture(self.UnlockedSkill, x, y, 1,1,1,1);
			else -- the skill is ready to be trained but no skill pts available, we set up just a white border
					self:drawTexture(self.ProgressSkill, x, y, 1,1,1,1);
				self:drawTexture(self.SkillBtnEmptWhitey, x, y, 1,1,1,1);
			end
		else -- skill is in progress, we set up a grey rect and fill it depending on the skill progress
			self:drawTexture(self.SkillBtnEmpty, x, y, 1,1,1,1);
			self:drawTextureScaled(self.ProgressSkill, x, y, sliceWidth * percentProgress, 18, 1,1,1,1);
		end
		x = x + 20;
	end

	-- our last square : the no available ones, this is just an empty dark grey rect
	for i=self.level + 1, 9 do
--~ 		self:drawRect(x, y, 19, 19, 0.5, 0.41, 0.41, 0.41);
		self:drawTexture(self.SkillBtnEmpty, x, y, 1,1,1,1);
		x = x + 20;
	end
end

function ISSkillProgressBar.updateAlpha()
	if ISSkillProgressBar.upAlpha then
		ISSkillProgressBar.alpha = ISSkillProgressBar.alpha + 0.1 * (30 / getPerformance():getUIRenderFPS());
		if ISSkillProgressBar.alpha > 1.0 then
			ISSkillProgressBar.alpha = 1.0;
			ISSkillProgressBar.upAlpha = false;
		end
	else
		ISSkillProgressBar.alpha = ISSkillProgressBar.alpha - 0.1 * (30 / getPerformance():getUIRenderFPS());
		if ISSkillProgressBar.alpha < 0 then
			ISSkillProgressBar.alpha = 0;
			ISSkillProgressBar.upAlpha = true;
		end
	end
end

function ISSkillProgressBar:new (x, y, width, height, playerNum, perk, parent)
	local o = {};
	o = ISPanel:new(x, y, 200, 19);
	setmetatable(o, self);
	self.__index = self;
	o.playerNum = playerNum
	o.char = getSpecificPlayer(playerNum)
	o.perk = perk;
	o.parent = parent;
	o.xp = 0;
	o.message = nil;
	o.tooltip = nil;
	o:noBackground();
	-- our current perk lvl
	o.level = o.char:getPerkLevel(perk:getType());
	-- how much xp we need for the next lvl
	o.xpForLvl = ISSkillProgressBar.getXpForLvl(perk, o.level);
	o.UnlockedSkill = getTexture("media/ui/XpSystemUI/UnlockedSkill.png")
	o.AddSkillBtn = getTexture("media/ui/XpSystemUI/AddSkillBtn.png")
	o.SkillBtnEmptyBig = getTexture("media/ui/XpSystemUI/SkillBtnEmptyBig.png")
	o.ProgressSkill = getTexture("media/ui/XpSystemUI/ProgressSkill.png")
	o.SkillBtnEmpty = getTexture("media/ui/XpSystemUI/SkillBtnEmpty.png")
	o.SkillBtnEmptWhitey = getTexture("media/ui/XpSystemUI/SkillBtnEmptWhitey.png")
	return o;
end

ISSkillProgressBar.getXpForLvl = function(perk, level)
	return perk:getXpForLevel(level + 1)
end

ISSkillProgressBar.getPreviousXpLvl = function(perk, level)
	if level == 0 then
		return 0;
	end
	level = level - 1;
	local previousXp = perk:getXp1();
	if level >= 1 then
		previousXp = previousXp + perk:getXp2();
	end
	if level >= 2 then
		previousXp = previousXp + perk:getXp3();
	end
	if level >= 3 then
		previousXp = previousXp + perk:getXp4();
	end
	if level >= 4 then
		previousXp = previousXp + perk:getXp5();
    end
    if level >= 5 then
        previousXp = previousXp + perk:getXp6();
    end
    if level >= 6 then
        previousXp = previousXp + perk:getXp7();
    end
    if level >= 7 then
        previousXp = previousXp + perk:getXp8();
    end
    if level >= 8 then
        previousXp = previousXp + perk:getXp9();
    end
    if level >= 9 then
        previousXp = previousXp + perk:getXp10();
    end
	return previousXp;
end

ISSkillProgressBar.getPerkXp = function(self)
	return self.char:getXp():getXP(self.perk:getType()) - ISSkillProgressBar.getPreviousXpLvl(self.perk, self.level);
end
