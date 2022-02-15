--***********************************************************
--**                    ROBERT JOHNSON                     **
--***********************************************************

require "ISUI/ISPanelJoypad"

ISCharacterProtection = ISPanelJoypad:derive("ISCharacterProtection");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)

function ISCharacterProtection:initialise()
	ISPanelJoypad.initialise(self);
	self:create();
end

function ISCharacterProtection:createChildren()
    ISPanelJoypad.createChildren(self);

    self.cacheColor = Color.new(  1.0,  1.0, 1.0, 1.0 );

    self.colorScheme = {
        { val =   0, color = Color.new(  255/255,   0/255, 0/255, 1 ) },
        { val =  50, color = Color.new(  255/255, 255/255, 0/255, 1 ) },
        { val = 100, color = Color.new(    0/255, 255/255, 0/255, 1 ) },
    }

    local y = 8;
    self.bpPanelX = 0;
    self.bpPanelY = y;
    self.bpAnchorX = 123;
    self.bpAnchorY = 50;
    self.bodyPartPanel = ISBodyPartPanel:new(self.char, self.bpPanelX, self.bpPanelY, self, nil);
    self.bodyPartPanel.maxValue = 100;
    self.bodyPartPanel.canSelect = false;
    self.bodyPartPanel:initialise();
    --self.bodyPartPanel:setEnableSelectLines( true, self.bpAnchorX, self.bpAnchorY );
    --self.bodyPartPanel:enableNodes( "media/ui/BodyParts/bps_node_diamond", "media/ui/BodyParts/bps_node_diamond_outline" );
    --self.bodyPartPanel:overrideNodeTexture( BodyPartType.Torso_Upper, "media/ui/BodyParts/bps_node_big", "media/ui/BodyParts/bps_node_big_outline" );
    self.bodyPartPanel:setColorScheme(self.colorScheme);

    self:addChild(self.bodyPartPanel);
end

function ISCharacterProtection:setVisible(visible)
	if visible then
		-- init?
	end
    self.javaObject:setVisible(visible);
end

function ISCharacterProtection:prerender()
	ISPanelJoypad.prerender(self)
end

function ISCharacterProtection:render()
	local labelPart = getText("IGUI_health_Part");
	local labelBite = getText("IGUI_health_Bite");
	local labelScratch = getText("IGUI_health_Scratch");
	local biteWidth = getTextManager():MeasureStringX(UIFont.Small, labelBite);
	local scratchWidth = getTextManager():MeasureStringX(UIFont.Small, labelScratch);
	
	local xOffset = 0;
	local yOffset = 8;
	local yText = yOffset;
	local partX = 150;
	local biteX = partX + self.maxLabelWidth + 20;
	local scratchX = biteX + biteWidth + 20;
	--self:drawTexture(self.bodyOutline, xOffset, yOffset, 1, 1, 1, 1);
	
	self:drawText(labelPart, partX, yText, 1, 1, 1, 1, UIFont.Small);
	self:drawText(labelBite, biteX, yText, 1, 1, 1, 1, UIFont.Small);
	self:drawText(labelScratch, scratchX, yText, 1, 1, 1, 1, UIFont.Small);
	yText = yText + FONT_HGT_SMALL + 5;

	-- draw each part as overlay
	for i=0, BodyPartType.ToIndex(BodyPartType.MAX) do
		local string = BodyPartType.ToString(BodyPartType.FromIndex(i));
		if self.bparts[string] then
			local biteDefense = luautils.round(self.char:getBodyPartClothingDefense(i, true, false));
			local scratchDefense = luautils.round(self.char:getBodyPartClothingDefense(i, false, false));
			biteDefense = math.floor(biteDefense);
			scratchDefense = math.floor(scratchDefense);
			-- COMMENT MERGE:
			--local totalDef = biteDefense + scratchDefense;
			---if totalDef > 100 then totalDef = 100; end
			--local rgb = luautils.getConditionRGB(totalDef);
			--self:drawTexture(self.textures[BodyPartType.ToString(BodyPartType.FromIndex(i))], xOffset, yOffset, 1, rgb.r, rgb.g, rgb.b);

            --local color = self.bodyPartPanel:setColorForValue( (biteDefense + scratchDefense), self.cacheColor );

            self.bodyPartPanel:setValue( BodyPartType.FromIndex(i), (biteDefense + scratchDefense));

            self:drawText(BodyPartType.getDisplayName(BodyPartType.FromIndex(i)), partX, yText, 1, 1, 1, 1, UIFont.Small);

            --local c = self.bodyPartPanel:setColorForValue( biteDefense , self.cacheColor );
            --local r, g, b = c:getRedFloat(), c:getGreenFloat(), c:getBlueFloat();
            local r, g, b = self.bodyPartPanel:getRgbForValue( biteDefense );
            self:drawText(biteDefense .. "%", biteX, yText, r, g, b, 1, UIFont.Small);

            --c = self.bodyPartPanel:setColorForValue( scratchDefense , self.cacheColor );
            --r, g, b = c:getRedFloat(), c:getGreenFloat(), c:getBlueFloat();
            r, g, b = self.bodyPartPanel:getRgbForValue( scratchDefense );
            self:drawText(scratchDefense .. "%", scratchX, yText, r, g, b, 1, UIFont.Small);

            yText = yText + FONT_HGT_SMALL;
            --[[
			local r,g,b = self:getProtectionRGB(biteDefense + (scratchDefense));
			self:drawTexture(self.textures[BodyPartType.ToString(BodyPartType.FromIndex(i))], xOffset, yOffset, 1, r, g, b);			self:drawText(BodyPartType.getDisplayName(BodyPartType.FromIndex(i)), partX, yText, 1, 1, 1, 1, UIFont.Small);
			rgb = luautils.getConditionRGB(biteDefense);
			self:drawText(biteDefense .. "%", biteX, yText, rgb.r, rgb.g, rgb.b, 1, UIFont.Small);
			rgb = luautils.getConditionRGB(scratchDefense);
			self:drawText(scratchDefense .. "%", scratchX, yText, rgb.r, rgb.g, rgb.b, 1, UIFont.Small);
			yText = yText + FONT_HGT_SMALL;
            --]]
		end
	end

	local width = math.max(self.width, scratchX + scratchWidth + 20);
	self:setWidthAndParentWidth(width);

	local height = math.max(self.height, yText + 20);
	self:setHeightAndParentHeight(height);
end

--[[
function ISCharacterProtection:getProtectionRGB(totalProtection)
	if totalProtection == 0 then
		return 1, 0, 0;
	end
--	print("total protection = ", totalProtection)
	local r,g,b = 0, 0, 0;
	if totalProtection > 100 then totalProtection = 100; end
	if totalProtection < 50 then
		r = 255;
		g = 255 * (totalProtection / 100);
		b = 0;
	else
		totalProtection = totalProtection - 50;
		r = 160 * (1 - (totalProtection / 100));
		g = 255;
		b = 0;
	end
	return r / 255, g / 255, b / 255;
end
--]]

function ISCharacterProtection:create()
	self:initTextures();

	self.maxLabelWidth = 0
	for i=1,BodyPartType.ToIndex(BodyPartType.MAX) do
		local string = BodyPartType.ToString(BodyPartType.FromIndex(i - 1))
		if self.bparts[string] then
			local label = BodyPartType.getDisplayName(BodyPartType.FromIndex(i - 1))
			local labelWidth = getTextManager():MeasureStringX(UIFont.Small, label)
			self.maxLabelWidth = math.max(self.maxLabelWidth, labelWidth)
		end
	end
end

function ISCharacterProtection:initTextures()
    self.bparts = {};

    self.bparts["Hand_L"] = true
    self.bparts["Hand_R"] = true
    self.bparts["ForeArm_L"] = true
    self.bparts["ForeArm_R"] = true
    self.bparts["UpperArm_L"] = true
    self.bparts["UpperArm_R"] = true
    self.bparts["Torso_Upper"] = true
    self.bparts["Torso_Lower"] = true
    self.bparts["Head"] = true
    self.bparts["Neck"] = true
    self.bparts["Groin"] = true
    self.bparts["UpperLeg_L"] = true
    self.bparts["UpperLeg_R"] = true
    self.bparts["LowerLeg_L"] = true
    self.bparts["LowerLeg_R"] = true
    self.bparts["Foot_L"] = true
    self.bparts["Foot_R"] = true
    --[[
	self.textures = {};
	
	self.textures["Hand_L"] = getTexture("media/ui/defense/" .. self.sex .. "_hand_left.png")
	self.textures["Hand_R"] = getTexture("media/ui/defense/" .. self.sex .. "_hand_right.png")
	self.textures["ForeArm_L"] = getTexture("media/ui/defense/" .. self.sex .. "_lower_arms_left.png")
	self.textures["ForeArm_R"] = getTexture("media/ui/defense/" .. self.sex .. "_lower_arms_right.png")
	self.textures["UpperArm_L"] = getTexture("media/ui/defense/" .. self.sex .. "_upper_arms_left.png")
	self.textures["UpperArm_R"] = getTexture("media/ui/defense/" .. self.sex .. "_upper_arms_right.png")
	self.textures["Torso_Upper"] = getTexture("media/ui/defense/" .. self.sex .. "_upper_body.png")
	self.textures["Torso_Lower"] = getTexture("media/ui/defense/" .. self.sex .. "_lower_body.png")
	self.textures["Head"] = getTexture("media/ui/defense/" .. self.sex .. "_head.png")
	self.textures["Neck"] = getTexture("media/ui/defense/" .. self.sex .. "_neck.png")
	self.textures["Groin"] = getTexture("media/ui/defense/" .. self.sex .. "_groin.png")
	self.textures["UpperLeg_L"] = getTexture("media/ui/defense/" .. self.sex .. "_upper_legs_left.png")
	self.textures["UpperLeg_R"] = getTexture("media/ui/defense/" .. self.sex .. "_upper_legs_right.png")
	self.textures["LowerLeg_L"] = getTexture("media/ui/defense/" .. self.sex .. "_lower_legs_left.png")
	self.textures["LowerLeg_R"] = getTexture("media/ui/defense/" .. self.sex .. "_lower_legs_right.png")
	self.textures["Foot_L"] = getTexture("media/ui/defense/" .. self.sex .. "_feet_left.png")
	self.textures["Foot_R"] = getTexture("media/ui/defense/" .. self.sex .. "_feet_right.png")
    --]]
end

function ISCharacterProtection:onJoypadDown(button)
	if button == Joypad.BButton then
		getPlayerInfoPanel(self.playerNum):toggleView(xpSystemText.protection)
		setJoypadFocus(self.playerNum, nil)
	end
	if button == Joypad.LBumper then
		getPlayerInfoPanel(self.playerNum):onJoypadDown(button)
	end
	if button == Joypad.RBumper then
		getPlayerInfoPanel(self.playerNum):onJoypadDown(button)
	end
end

function ISCharacterProtection:new(x, y, width, height, playerNum)
	local o = {};
	o = ISPanelJoypad:new(x, y, width, height);
	o:noBackground();
	setmetatable(o, self);
    self.__index = self;
    o.playerNum = playerNum
	o.char = getSpecificPlayer(playerNum);
	o.bFemale = o.char:isFemale()
	o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
	o.backgroundColor = {r=0, g=0, b=0, a=0.8};
	o.sex = "female";
	if not o.char:isFemale() then
		o.sex = "male";
	end
	o.bodyOutline = getTexture("media/ui/defense/" .. o.sex .. "_base.png")
   return o;
end
