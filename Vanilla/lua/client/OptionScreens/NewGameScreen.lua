require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"

require "defines"

NewGameScreen = ISPanelJoypad:derive("NewGameScreen");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_MEDIUM = getTextManager():getFontHeight(UIFont.Medium)
local FONT_HGT_LARGE = getTextManager():getFontHeight(UIFont.Large)
local FONT_HGT_TITLE = getTextManager():getFontHeight(UIFont.Title)

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

local HorizontalLine = ISPanel:derive("HorizontalLine")

function HorizontalLine:prerender()
end

function HorizontalLine:render()
	self:drawRect(0, 0, self.width, 2, 1.0, 0.5, 0.5, 0.5)
end

function HorizontalLine:new(x, y, width)
	local o = ISPanel:new(x, y, width, 2)
	setmetatable(o, self)
	self.__index = self
	return o
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

local MainPanel = ISPanel:derive("NewGameScreen_MainPanel")

function MainPanel:prerender()
    local scrollBarWid = (self:getScrollHeight() > self.height) and self.vscroll:getWidth() or 0
    local scrollBarHgt = (self:getScrollWidth() > self.width) and self.hscroll:getHeight() or 0
    self:setStencilRect(0, 0, self:getWidth() - scrollBarWid, self:getHeight() - scrollBarHgt)
    ISPanel.prerender(self)
end

function MainPanel:render()
    ISPanel.render(self)
    self:renderActiveMods()
    self:clearStencilRect()
end

function MainPanel:renderActiveMods()
    local x = 16
    local xoffset = 20 
    local y = self.activeModsY
    local activeMods = ActiveMods.getById("currentGame")
    if activeMods:getMods():isEmpty() then
        self:drawText(getText("UI_LoadGameScreen_NoMods"), x + xoffset, y, 1, 1, 1, 1, UIFont.Small)
        y = y + FONT_HGT_SMALL
    end
    for i=1,activeMods:getMods():size() do
        local modID = activeMods:getMods():get(i-1)
        local modInfo = getModInfoByID(modID)
        if modInfo then
            self:drawText(modInfo:getName(), x + xoffset, y, 1, 1, 1, 1, UIFont.Small)
        else
            self:drawText(modID, x + xoffset, y, 1, 0, 0, 1, UIFont.Small)
        end
        y = y + FONT_HGT_SMALL
    end
    self.parent.buttonMods:setY(y + 8)

    self:setScrollHeight(self.parent.buttonMods:getBottom() + 20)
end

function MainPanel:onMouseWheel(del)
	self:setYScroll(self:getYScroll() - (del * 40))
	return true
end

-- -- -- -- --
-- -- -- -- --
-- -- -- -- --

function NewGameScreen:initialise()
	ISPanel.initialise(self);
end


	--************************************************************************--
	--** ISPanel:instantiate
	--**
	--************************************************************************--
function NewGameScreen:instantiate()

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

function NewGameScreen:create()
    self.mainPanel = MainPanel:new(16, 48, self:getWidth() / 2, self:getHeight() - (48*2));
    self.mainPanel:initialise();
    self.mainPanel:instantiate();
    self.mainPanel:setAnchorRight(false);
    self.mainPanel:setAnchorLeft(true);
    self.mainPanel:setAnchorTop(true);
    self.mainPanel:setAnchorBottom(true);
    self.mainPanel:noBackground();
    self.mainPanel.borderColor = {r=0, g=0, b=0, a=0};
    self.mainPanel:setScrollChildren(true);
    self.mainPanel:addScrollBars(true);
    self.mainPanel.vscroll.doSetStencil = true
    self.mainPanel.hscroll.doSetStencil = true
    self:addChild(self.mainPanel);

    local y = 10;
    local x = 16;
    local xoffset = 20;
    local xoffsetdifficulty = 170;
    local gapY = 6

    local mediumFontHgt = FONT_HGT_MEDIUM + 4
    local largeFontHgt = FONT_HGT_LARGE + 6

--    self.tutorial = ISLabel:new(x, y, FONT_HGT_LARGE, getText("UI_NewGame_Tutorial"), 1, 1, 1, 1, UIFont.NewLarge, true);
--    self.tutorial.internal = "Tutorial";
--    self.tutorial.mode = "Tutorial";
--    self.tutorial.desc = getText("UI_NewGame_Tutorial_desc");
--    self.tutorial.thumb = "media/ui/spiffoSkipping.png";
--    self.tutorial:initialise();
--    self.mainPanel:addChild(self.tutorial);
--    self.tutorial.onMouseDown = NewGameScreen.onMenuItemMouseDown;
--    self.tutorial:setOnMouseDoubleClick(self, NewGameScreen.dblClickTutorial);
--    y = y + FONT_HGT_LARGE;
--
--    local sep1 = HorizontalLine:new(x, y + 10, 1500)
--    sep1:initialise();
--    self.mainPanel:addChild(sep1);
--    y = y + 10 + 2 + 10
    
--    local survival = ISLabel:new(x, y, FONT_HGT_LARGE, getText("UI_NewGame_Survival") .. getText("UI_NewGame_SurvivalMore"), 1, 1, 1, 1, UIFont.NewLarge, true);
--    survival.moreTextToRemove = getText("UI_NewGame_SurvivalMore");
--    survival.internal = "SURVIVAL";
--    survival.mode = "Survival";
--    survival.desc = getText("UI_NewGame_Survival_desc");
--    survival.thumb = "media/ui/spiffoSurvivor.png"
--    survival:initialise();
--    self.mainPanel:addChild(survival);
--    survival.onMouseDown = NewGameScreen.onMenuItemMouseDown;
--    survival:setOnMouseDoubleClick(self, NewGameScreen.dblClickSurvival);
--    y = y + FONT_HGT_LARGE + 4;
    
--    self.survival = survival;
    
    local playstyle = ISLabel:new(x, y, FONT_HGT_LARGE, getText("UI_NewGame_PlayStyle"), 1, 1, 1, 1, UIFont.NewLarge, true);
    playstyle:initialise();
    self.mainPanel:addChild(playstyle);
    y = y + FONT_HGT_LARGE + 4

    local survivor = ISLabel:new(x + xoffset, y, mediumFontHgt, getText("UI_NewGame_Apocalypse"), 1, 1, 1, 1, UIFont.NewMedium, true);
    survivor.internal = "APOCALYPSE";
    survivor.mode = "Apocalypse";
    survivor.desc = getText("UI_NewGame_Apocalypse_desc");
    survivor.thumb = "media/ui/spiffoSurvivor2.png"
    survivor:initialise();
    self.mainPanel:addChild(survivor);
    survivor.onMouseDown = NewGameScreen.onMenuItemMouseDown;
    survivor:setOnMouseDoubleClick(self, NewGameScreen.dblClickPlaystyle);
    self.survival = survivor;
    
    local survivorDesc = ISLabel:new(survivor:getRight(), y, mediumFontHgt, " - " .. getText("UI_NewGame_Apocalypse_desc"), 0.5, 0.5, 0.5, 1, UIFont.Small, true);
    survivorDesc:initialise();
    self.mainPanel:addChild(survivorDesc);
    y = y + FONT_HGT_LARGE + 4;
    
    local fighter = ISLabel:new(x + xoffset, y, mediumFontHgt, getText("UI_NewGame_Survivor"), 1, 1, 1, 1, UIFont.NewMedium, true);
    fighter.internal = "SURVIVOR";
    fighter.mode = "Survivor";
    fighter.desc = getText("UI_NewGame_Survivor_desc");
    fighter.thumb = "media/ui/spiffoBrawler.png"
    fighter:initialise();
    self.mainPanel:addChild(fighter);
    fighter.onMouseDown = NewGameScreen.onMenuItemMouseDown;
    fighter:setOnMouseDoubleClick(self, NewGameScreen.dblClickPlaystyle);

    local fighterDesc = ISLabel:new(fighter:getRight(), y, mediumFontHgt, " - " .. getText("UI_NewGame_Survivor_desc"), 0.5, 0.5, 0.5, 1, UIFont.Small, true);
    fighterDesc:initialise();
    self.mainPanel:addChild(fighterDesc);
    y = y + FONT_HGT_LARGE + 4;
    
    local builder = ISLabel:new(x + xoffset, y, mediumFontHgt, getText("UI_NewGame_Builder"), 1, 1, 1, 1, UIFont.NewMedium, true);
    builder.internal = "BUILDER";
    builder.mode = "Builder";
    builder.desc = getText("UI_NewGame_Builder_desc");
    builder.thumb = "media/ui/spiffoBuilder.png"
    builder:initialise();
    self.mainPanel:addChild(builder);
    builder.onMouseDown = NewGameScreen.onMenuItemMouseDown;
    builder:setOnMouseDoubleClick(self, NewGameScreen.dblClickPlaystyle);
    
    local builderDesc = ISLabel:new(builder:getRight(), y, mediumFontHgt, " - " .. getText("UI_NewGame_Builder_desc"), 0.5, 0.5, 0.5, 1, UIFont.Small, true);
    builderDesc:initialise();
    self.mainPanel:addChild(builderDesc);
    y = y + FONT_HGT_LARGE + 4;
    
    local sandbox = ISLabel:new(x + xoffset, y, mediumFontHgt, getText("UI_NewGame_Sandbox"), 1, 1, 1, 1, UIFont.NewMedium, true);
    sandbox.internal = "SANDBOX";
    sandbox.mode = "Sandbox";
    sandbox.desc = getText("UI_NewGame_Sandbox_desc");
    sandbox.thumb = "media/ui/spiffoPacking.png"
    sandbox:initialise();
    self.mainPanel:addChild(sandbox);
    sandbox.onMouseDown = NewGameScreen.onMenuItemMouseDown;
    sandbox:setOnMouseDoubleClick(self, NewGameScreen.dblClickChallenge);
    
    local sandboxDesc = ISLabel:new(sandbox:getRight(), y, mediumFontHgt, " - " .. getText("UI_NewGame_Sandbox_desc2"), 0.5, 0.5, 0.5, 1, UIFont.Small, true);
    sandboxDesc:initialise();
    self.mainPanel:addChild(sandboxDesc);
    y = y + FONT_HGT_LARGE
    
    
--    local sep1 = HorizontalLine:new(x, y + 10, 1000)
--    sep1:initialise();
--    self.mainPanel:addChild(sep1);
--    y = y + 10 + 2 + 10
--
--    local scenarios = ISLabel:new(x, y, FONT_HGT_LARGE, getText("UI_NewGame_Scenarios"), 1, 1, 1, 1, UIFont.NewLarge, true);
--    scenarios:initialise();
--    self.mainPanel:addChild(scenarios);
--
--    self.difficulty = ISLabel:new(x + xoffsetdifficulty, y, FONT_HGT_LARGE, getText("UI_NewGame_StartingCondition"), 1, 1, 1, 1, UIFont.NewLarge, true);
--    self.difficulty:initialise();
--    self.mainPanel:addChild(self.difficulty);
--    self.difficulty:setVisible(false);
--    y = y + FONT_HGT_LARGE + 4;

--    local practiceRun = ISLabel:new(x+xoffset, y, mediumFontHgt, getText("UI_NewGame_InitialInfection"), 1, 1, 1, 1, UIFont.NewMedium, true);
--    practiceRun.internal = "PRACTICE";
--    practiceRun.mode = "Initial Infection";
--    practiceRun.desc = getText("UI_NewGame_InitialInfection_desc");
--    practiceRun.thumb = "media/ui/spiffoSkipping.png";
--    practiceRun:initialise();
--    self.mainPanel:addChild(practiceRun);
--    practiceRun.onMouseDown = NewGameScreen.onMenuItemMouseDown;

--    local begining = ISLabel:new(x+xoffset, y, mediumFontHgt, getText("UI_NewGame_OneWeekLater") .. getText("UI_NewGame_OneWeekLaterMore"), 1, 1, 1, 1, UIFont.NewMedium, true);
--    begining.internal = "BEGINING";
--    begining.mode = "One Week Later";
--    begining.moreTextToRemove = getText("UI_NewGame_OneWeekLaterMore");
--    begining.desc = getText("UI_NewGame_FirstWeek_desc");
--    begining.thumb = "media/ui/spiffoSkipping.png"
--    begining:initialise();
--    self.mainPanel:addChild(begining);
--    begining.onMouseDown = NewGameScreen.onMenuItemMouseDown;
    
--    self.difficultyEasy = ISLabel:new(x + xoffset + xoffsetdifficulty, y, mediumFontHgt, getText("UI_StarterCondition_Easy"), 1, 1, 1, 1, UIFont.NewMedium, true);
--    self.difficultyEasy:initialise();
--    self.difficultyEasy.difficulty = "Easy";
--    self.difficultyEasy.internal = "EASY";
--    self.difficultyEasy:setTooltip(getText("UI_StarterCondition_Easy_desc"):gsub("\\n", "\n"));
--    self.mainPanel:addChild(self.difficultyEasy);
--    self.difficultyEasy:setVisible(false);
--    self.difficultyEasy:setOnMouseDoubleClick(self, NewGameScreen.dblClickDifficulty);
--    self.difficultyEasy.onMouseDown = NewGameScreen.onChooseDifficulty;
--    y = y + mediumFontHgt + gapY
    
--    local ends = ISLabel:new(x+xoffset, y, mediumFontHgt, getText("UI_NewGame_SixMonths") .. getText("UI_NewGame_SixMonthsMore"), 1, 1, 1, 1, UIFont.NewMedium, true);
--    ends.internal = "ENDS";
--    ends.mode = "Six Months Later";
--    ends.moreTextToRemove = getText("UI_NewGame_SixMonthsMore");
--    ends.desc = getText("UI_NewGame_SixMonths_desc");
--    ends.thumb = "media/ui/spiffoPanic.png"
--    ends:initialise();
--    self.mainPanel:addChild(ends);
--    ends.onMouseDown = NewGameScreen.onMenuItemMouseDown;

--    self.difficultyNormal = ISLabel:new(x+xoffset + xoffsetdifficulty, y, mediumFontHgt, getText("UI_StarterCondition_Normal"), 1, 1, 1, 1, UIFont.NewMedium, true);
--    self.difficultyNormal:initialise();
--    self.difficultyNormal.difficulty = "Normal";
--    self.difficultyNormal.internal = "NORMAL";
--    self.difficultyNormal:setTooltip(getText("UI_StarterCondition_Normal_desc"):gsub("\\n", "\n"));
--    self.mainPanel:addChild(self.difficultyNormal);
--    self.difficultyNormal:setVisible(false);
--    self.difficultyNormal:setOnMouseDoubleClick(self, NewGameScreen.dblClickDifficulty);
--    self.difficultyNormal.onMouseDown = NewGameScreen.onChooseDifficulty;
--    y = y + mediumFontHgt + gapY

--    self.difficultyHard = ISLabel:new(x+xoffset + xoffsetdifficulty, y, mediumFontHgt, getText("UI_StarterCondition_Normal"), 1, 1, 1, 1, UIFont.NewMedium, true);
--    self.difficultyHard:initialise();
--    self.difficultyHard.difficulty = "Hard";
--    self.difficultyHard.internal = "HARD";
--    self.difficultyHard:setTooltip(getText("UI_StarterCondition_Hard_desc"):gsub("\\n", "\n"));
--    self.mainPanel:addChild(self.difficultyHard);
--    self.difficultyHard:setVisible(false);
--    self.difficultyHard:setOnMouseDoubleClick(self, NewGameScreen.dblClickDifficulty);
--    self.difficultyHard.onMouseDown = NewGameScreen.onChooseDifficulty;
--    y = y + mediumFontHgt + gapY
--
--    self.difficultyHardcore = ISLabel:new(x+xoffset + xoffsetdifficulty, y, mediumFontHgt,  getText("UI_StarterCondition_Hardcore"), 1, 1, 1, 1, UIFont.NewMedium, true);
--    self.difficultyHardcore:initialise();
--    self.difficultyHardcore.difficulty = "Hardcore";
--    self.difficultyHardcore.internal = "HARDCORE";
--    self.difficultyHardcore:setTooltip(getText("UI_StarterCondition_Hardcore_desc"):gsub("\\n", "\n"));
--    self.mainPanel:addChild(self.difficultyHardcore);
--    self.difficultyHardcore:setVisible(false);
--    self.difficultyHardcore:setOnMouseDoubleClick(self, NewGameScreen.dblClickDifficulty);
--    self.difficultyHardcore.onMouseDown = NewGameScreen.onChooseDifficulty;
--    y = y + mediumFontHgt + gapY

--    local right = 0
--    right = math.max(right, practiceRun:getRight())
--    right = math.max(right, begining:getRight())
--    right = math.max(right, ends:getRight())
--    right = math.max(right, survival:getRight())
--    right = right + 32
--    if self.difficulty.x < right then
--        self.difficulty:setX(right)
--        self.difficultyEasy:setX(right + xoffset)
----        self.difficultyNormal:setX(right + xoffset)
--        self.difficultyHard:setX(right + xoffset)
--        self.difficultyHardcore:setX(right + xoffset)
--    end

--    local sep2 = HorizontalLine:new(x, y + 10, 1000)
--    sep2:initialise();
--    self.mainPanel:addChild(sep2)
--    y = y + 10 + 2 + 10

--    local sandbox = ISLabel:new(x, y, largeFontHgt, getText("UI_NewGame_Sandbox"), 1, 1, 1, 1, UIFont.NewLarge, true);
--    sandbox.internal = "SANDBOX";
--    sandbox.mode = "Sandbox";
--    sandbox.desc = getText("UI_NewGame_Sandbox_desc");
--    sandbox.thumb = "media/ui/spiffoPacking.png"
--    sandbox:initialise();
--    self.mainPanel:addChild(sandbox);
--    sandbox.onMouseDown = NewGameScreen.onMenuItemMouseDown;
--    sandbox:setOnMouseDoubleClick(self, NewGameScreen.dblClickChallenge);
--    y = y + math.max(30, largeFontHgt)

    local sep3 = HorizontalLine:new(x, y + 10, 1000)
    sep3:initialise()
    self.mainPanel:addChild(sep3)
    y = y + 10 + 2 + 10

    local challenges = ISLabel:new(x, y, FONT_HGT_LARGE, getText("UI_NewGame_Challenges"), 1, 1, 1, 1, UIFont.NewLarge, true);
    challenges:initialise();
    self.mainPanel:addChild(challenges);
    y = y + FONT_HGT_LARGE + 4;

    local challenges = {}
    table.sort(LastStandChallenge, function(a,b) return a.name < b.name end)
    for i,info in ipairs(LastStandChallenge) do
        local challenge = ISLabel:new(x+xoffset, y, mediumFontHgt, info.name, 1, 1, 1, 1, UIFont.NewMedium, true);
        challenge:initialise();
        challenge.internal = info.name;
        challenge.challenge= info;
        challenge.desc = info.description or "NO DESCRIPTION";
        challenge.mode = "Challenge";
        challenge.thumb = info.image;
        self.mainPanel:addChild(challenge);
        challenge:setOnMouseDoubleClick(self, NewGameScreen.dblClickChallenge);
        challenge.onMouseDown = NewGameScreen.onMenuItemMouseDown;
        table.insert(challenges, challenge)
        y = y + mediumFontHgt + gapY
    end
    y = y - gapY

    local buttonHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)

    local sep4 = HorizontalLine:new(x, y + 10, 1000)
    sep4:initialise()
    self.mainPanel:addChild(sep4)
    y = y + 10 + 2 + 10
    
    local mods = ISLabel:new(x, y, FONT_HGT_LARGE, getText("UI_NewGame_Mods"), 1, 1, 1, 1, UIFont.NewLarge, true);
    mods:initialise();
    self.mainPanel:addChild(mods);
    y = y + FONT_HGT_LARGE + 4;

    self.mainPanel.activeModsY = y

    self.buttonMods = ISButton:new(x + xoffset, y, 150, buttonHgt, getText("UI_NewGame_ChooseMods"), self, NewGameScreen.onOptionMouseDown)
    self.buttonMods.internal = "MODS";
    self.buttonMods:initialise();
    self.buttonMods:instantiate();
    self.mainPanel:addChild(self.buttonMods);

    y = self.buttonMods:getBottom()

    self.mainPanel:setScrollHeight(y + 20);

    self.mainPanel.javaObject:BringToTop(self.mainPanel.vscroll.javaObject)
    self.mainPanel.javaObject:BringToTop(self.mainPanel.hscroll.javaObject)

    local width = 0
    for _,child in pairs(self.mainPanel:getChildren()) do
        if child.internal and child.Type ~= "ISButton" then
            child.prerender = NewGameScreen.prerenderBottomPanelLabel;
            child.setJoypadFocused = NewGameScreen.Label_setJoypadFocused
        end
        if child.Type == "ISLabel" then
            width = math.max(width, child:getRight())
        end
    end
    self.mainPanel:setScrollWidth(width + 16);
    width = width + self.mainPanel.vscroll:getWidth() + 4
    self.mainPanelReqWidth = width
    width = math.min(width, (self.width / 2) + 90 - self.mainPanel.x)
--    self.mainPanel:setX(30 + (self.width / 2 - width) / 2)
    self.mainPanel:setWidth(width)

--    self:insertNewLineOfButtons(self.tutorial)
--    self:insertNewLineOfButtons(practiceRun, self.difficultyEasy)
--    self:insertNewLineOfButtons(survival)
--    self:insertNewLineOfButtons(begining, self.difficultyEasy)
--    self:insertNewLineOfButtons(self.difficultyNormal)
--    self:insertNewLineOfButtons(ends, self.difficultyHard)
--    self:insertNewLineOfButtons(self.difficultyHardcore)
    self:insertNewLineOfButtons(survivor)
    self:insertNewLineOfButtons(fighter)
    self:insertNewLineOfButtons(builder)
    self:insertNewLineOfButtons(sandbox)
    for _,challenge in ipairs(challenges) do
        self:insertNewLineOfButtons(challenge)
    end
    self:insertNewLineOfButtons(self.buttonMods)
--    self.joypadIndex = 1

    self.backButton = ISButton:new(16, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_back"), self, NewGameScreen.onOptionMouseDown);
	self.backButton.internal = "BACK";
	self.backButton:initialise();
	self.backButton:instantiate();
	self.backButton:setAnchorLeft(true);
	self.backButton:setAnchorTop(false);
	self.backButton:setAnchorBottom(true);
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self.backButton:setFont(UIFont.Small);
	self.backButton:ignoreWidthChange();
	self.backButton:ignoreHeightChange();
	self:addChild(self.backButton);
	
--	self.vehiclesButton = ISTickBox:new(self.width - 286, self.height-30, 150, 25, "", self, NewGameScreen.onVehicleEnable);
--	self.vehiclesButton.internal = "VEHICLES";
--	self.vehiclesButton:initialise();
--	self.vehiclesButton:instantiate();
--	self.vehiclesButton:setAnchorLeft(false);
--	self.vehiclesButton:setAnchorRight(true);
--	self.vehiclesButton:setAnchorTop(false);
--	self.vehiclesButton:setAnchorBottom(true);
--	self.vehiclesButton.borderColor = {r=1, g=1, b=1, a=0.1};

--	self.vehiclesButton:setFont(UIFont.Medium);
--	self.vehiclesButton:ignoreWidthChange();
--	self.vehiclesButton:ignoreHeightChange();
--	self:addChild(self.vehiclesButton);
--	self.vehiclesButton:addOption(getText("UI_btn_experimentalVehicles"));
--	self.vehiclesButton:setSelected(1, getCore():isVehiclesEnable())
--	if not getCore():isVehiclesWarningShow() then
--		self.vehiclesButton.choicesColor = {r=1.0, g=0.3, b=0.2, a=1};
--	end
--	self:insertNewLineOfButtons(self.vehiclesButton)

	self.newButton = ISButton:new(self.width - 116, self.height - 5 - buttonHgt, 100, buttonHgt, getText("UI_btn_next"), self, NewGameScreen.onOptionMouseDown);
	self.newButton.internal = "NEXT";
	self.newButton:initialise();
	self.newButton:instantiate();
	self.newButton:setAnchorLeft(false);
	self.newButton:setAnchorRight(true);
	self.newButton:setAnchorTop(false);
	self.newButton:setAnchorBottom(true);
	self.newButton.borderColor = {r=1, g=1, b=1, a=0.1};

	self.newButton:setFont(UIFont.Small);
	self.newButton:ignoreWidthChange();
	self.newButton:ignoreHeightChange();
	self:addChild(self.newButton);

    self.richText = ISRichTextPanel:new(16, 10, 500,200);
    self.richText:initialise();
    self.richText.autosetheight = false;
    self.richText.background = false;
    self.richText.clip = true
    self.richText.marginRight = 20
    self.richText:setAnchorBottom(true);
    self.richText:setAnchorRight(true);
--    self.richText:setVisible(false);
    self:addChild(self.richText);
    self.richText:addScrollBars()

    self.selectedItem = self.survival

	self:setVisible(false);
end

NewGameScreen.onChooseDifficulty = function(item, x, y)
    if item.disabled then return; end
    NewGameScreen.instance.selectedDifficulty = item;
end

NewGameScreen.dblClickTutorial = function(item, x, y)
    NewGameScreen.onMenuItemMouseDown(item, x, y)
    NewGameScreen.instance:clickPlay();
end

NewGameScreen.dblClickPlaystyle = function(item, x, y)
    NewGameScreen.onMenuItemMouseDown(item, x, y)
    NewGameScreen.instance:clickPlay();
end

NewGameScreen.dblClickChallenge = function(item, x, y)
    NewGameScreen.onMenuItemMouseDown(item, x, y)
    NewGameScreen.instance:clickPlay();
end

NewGameScreen.dblClickDifficulty = function(item, x, y)
    NewGameScreen.onChooseDifficulty(item,x,y);
    NewGameScreen.instance:clickPlay();
end

NewGameScreen.dblClickSurvival = function(item, x, y)
    NewGameScreen.instance:clickPlay();
end

NewGameScreen.onMenuItemMouseDown = function(item, x, y)
    getSoundManager():playUISound("UIActivateMainMenuItem")

    local screen = NewGameScreen.instance;
    screen.selectedItem = item;

--    screen.selectedDifficulty = nil;
--    screen.difficulty:setVisible(false);
--    screen.difficultyEasy:setVisible(false);
--    screen.difficultyNormal:setVisible(false);
--    screen.difficultyHard:setVisible(false);
--    screen.difficultyHardcore:setVisible(false);

--    if screen:needDifficulty() then
--        screen.difficulty:setVisible(true);
--        screen.difficultyEasy:setVisible(true);
----        screen.difficultyNormal:setVisible(true);
--        screen.difficultyHard:setVisible(true);
--        screen.difficultyHardcore:setVisible(true);
--        screen.difficultyEasy.disabled = false;
----        screen.difficultyNormal.disabled = false;
--        screen.difficultyHard.disabled = false;
--        screen.difficultyEasy.a = 1;
----        screen.difficultyNormal.a = 1;
--        screen.difficultyHard.a = 1;
--    elseif screen.selectedItem.internal == "SURVIVAL" then
    if screen.selectedItem then
        screen.selectedDifficulty = screen.difficultyHardcore;
--        screen:clickPlay();
    end
--    NewGameScreen.instance:clickPlay();
--    end
end


function NewGameScreen:needDifficulty()
    if self.selectedItem then
        return self.selectedItem.internal == "PRACTICE" or self.selectedItem.internal == "BEGINING" or self.selectedItem.internal == "ENDS";
    end
    return false;
end

function NewGameScreen:prerenderBottomPanelLabel()
    local padLeft = 6
    local padRight = 6
    local alpha = 0.5
    if NewGameScreen.instance.selectedItem == self or NewGameScreen.instance.selectedDifficulty == self then
        self:drawRect(0 - padLeft, 0, self:getWidth() + padLeft + padRight, self:getHeight(), alpha, 0.3, 0.3, 0.3)
        if self.joypadFocused then
            self:drawRectBorder(0 - padLeft, 0, self:getWidth() + padLeft + padRight, self:getHeight(), 0.9, 0.6, 0.6, 0.6)
        else
            self:drawRectBorder(0 - padLeft, 0, self:getWidth() + padLeft + padRight, self:getHeight(), 0.9, 0.3, 0.3, 0.3)
        end
    elseif self.fadeOut or self.fadeIn then
        if self.fadeIn then
            local fadeIn = getPerformance():getUIRenderFPS() / 12
            alpha = 0.5 * (self.fadeIn / fadeIn)
            self.fadeIn = math.min(self.fadeIn + 1, fadeIn)
        else
            local fadeOut = getPerformance():getUIRenderFPS() / 4
            alpha = 0.5 * (1 - self.fadeOut / fadeOut)
            self.fadeOut = math.min(self.fadeOut + 1, fadeOut)
            if self.fadeOut == fadeOut then
                self.fadeOut = nil
            end
        end
        self:drawRect(0 - padLeft, 0, self:getWidth() + padLeft + padRight, self:getHeight(), alpha, 0.3, 0.3, 0.3)
    end
    ISLabel.prerender(self)
end

function NewGameScreen:update()
    NewGameScreen.instance = self
    self:updateBottomPanelButtons();
    self:disableBtn();
    if self.mainPanel.hscroll then
        self.mainPanel.hscroll:setVisible(self.mainPanel:getScrollAreaWidth() < self.mainPanel:getScrollWidth())
    end
end

function NewGameScreen:disableBtn()
    self.newButton:setEnable(false);
    self.newButton:setTooltip(nil);
    if self.selectedItem then
        if (self:needDifficulty() and self.selectedDifficulty) or not self:needDifficulty() then
           self.newButton:setEnable(true);
        else
            self.newButton:setTooltip(getText("UI_NewGame_SelectDifficulty"))
        end
    end
end

function NewGameScreen:updateBottomPanelButtons()
    local overButton = nil
    for _,child in pairs(self.mainPanel:getChildren()) do
        if not child.disabled and child:isMouseOver() or child.joypadFocused then
            overButton = child
            break
        end
    end
    if overButton ~= self.overBottomPanelButton then
        if self.overBottomPanelButton then
            self.overBottomPanelButton.fadeIn = nil
            self.overBottomPanelButton.fadeOut = 0
        end
        self.overBottomPanelButton = overButton
        if self.overBottomPanelButton then
            self.overBottomPanelButton.fadeIn = 0
            self.overBottomPanelButton.fadeOut = nil

            local sound = getSoundManager():playUISound("UIHighlightMainMenuItem")
            if self.MouseEnterMainMenuItem then
                getSoundManager():stopUISound(self.MouseEnterMainMenuItem)
            end
            self.MouseEnterMainMenuItem = sound and sound or nil
        end
    end
end

function NewGameScreen:render()
    local selectedItem = self.selectedItem;
    local selectedDifficulty = self.selectedDifficulty;
    if not selectedItem then return; end

    local thumbHeight = 256;
    local thumbY = self.startY + 8

    local text = ""
    if selectedItem.thumb then
        text = " <IMAGECENTRE:" .. selectedItem.thumb .. "> <LINE> "
    end
    local descRectWidth = self.width - 87 - (self.width/2 + 30)
    local descRectHeight = self.mainPanel:getHeight()

    self.richText:setX(self.width/2 + 100)
    self.richText:setY(self.mainPanel:getY())
    self.richText:setWidth(descRectWidth)
    self.richText:setHeight(descRectHeight)
    local name = selectedItem.name;
    if selectedItem.moreTextToRemove then
        name = name:gsub(selectedItem.moreTextToRemove, "");
        name = name:gsub("-", "");
    end
    text = text .. " <H1> " .. name .. " <LINE> ";
    if selectedDifficulty then
        text = text .. " <H1> " .. getText("UI_Difficulty") .. ": " .. selectedDifficulty.name .. " <LINE> <LINE> <H2> ";
    else
        text = text .. " <LINE> <H2> ";
    end
    text = text .. selectedItem.desc;
    self.richText.text = text;
    self.richText:paginate();
    self:drawRectBorder( self.richText:getX(), self.richText:getY(), self.richText:getWidth(), self.richText:getHeight(), 0.3, 1, 1, 1);
end

function NewGameScreen:prerender()
    NewGameScreen.instance = self
	ISPanel.prerender(self);
	self:drawTextCentre(getText("UI_NewGameScreen_title"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title);
	self:ensureVisible()
end

function NewGameScreen:onOptionMouseDown(button, x, y)
    if self.modal then
        self.modal:setVisible(false);
        self.modal = nil;
    end
    if button.internal == "BACK" then
        MainScreen.resetLuaIfNeeded()
        self:setVisible(false);
        MainScreen.instance.bottomPanel:setVisible(true);
        if self.joyfocus then
            self:clearJoypadFocus(self.joyfocus)
            self.joypadIndex = 1
            self.joypadIndexY = 1
            self.joypadButtons = self.joypadButtonsY[self.joypadIndexY]
            self.joyfocus.focus = MainScreen.instance;
            updateJoypadFocus(self.joyfocus);
        end
    end
    if button.internal == "NEXT" then
        self:clickPlay();
    end
    if button.internal == "MODS" then
        self:setVisible(false)
        ModSelector.instance.isNewGame = true
        ModSelector.instance:setVisible(true, self.joyfocus)
        ModSelector.instance:populateListBox(getModDirectoryTable())
        ModSelector.showNagPanel()
    end
end

function NewGameScreen:clickPlay()
    self:setVisible(false);

    MainScreen.instance.charCreationProfession.previousScreen = "NewGameScreen";
    getWorld():setGameMode(self.selectedItem.mode);

    if self.selectedItem.mode == "Tutorial" then
        MainScreen.startTutorial();
        return;
    end

	MainScreen.instance:setDefaultSandboxVars()

    if self.selectedItem.mode == "Challenge" then
        getWorld():setDifficulty("Hardcore");
        LastStandData.chosenChallenge = self.selectedItem.challenge;
        local worldName = ZombRand(100000)..ZombRand(100000)..ZombRand(100000)..ZombRand(100000);
        doChallenge(self.selectedItem.challenge);
        getWorld():setWorld(worldName);
        if getCore():getGameMode() ~= "LastStand" then
            MainScreen.instance.createWorld = true
            if MapSpawnSelect.instance:hasChoices() then
                MapSpawnSelect.instance:fillList();
                MapSpawnSelect.instance.previousScreen = "NewGameScreen"
                MapSpawnSelect.instance:setVisible(true, self.joyfocus);
            else
                MapSpawnSelect.instance:useDefaultSpawnRegion()
                MainScreen.instance.charCreationProfession.previousScreen = "NewGameScreen";
                MainScreen.instance.charCreationProfession:setVisible(true, self.joyfocus);
            end
        elseif #MainScreen.instance.lastStandPlayerSelect.listbox.items > 0 then
            createWorld(worldName);
            MainScreen.instance.lastStandPlayerSelect:setVisible(true, self.joyfocus);
        else
            createWorld(worldName);
            MapSpawnSelect.instance:useDefaultSpawnRegion()
            MainScreen.instance.charCreationProfession.previousScreen = "NewGameScreen"
            MainScreen.instance.charCreationProfession:setVisible(true, self.joyfocus);
        end
        return;
    end

    if self.selectedDifficulty then
        getWorld():setDifficulty(self.selectedDifficulty.difficulty);
    end

    if self.selectedItem.mode == "Apocalypse" then
        MainScreen.instance:setSandboxPreset(MainScreen.instance.sandOptions:getApocalypsePreset());
    end
    if self.selectedItem.mode == "Survivor" then
        MainScreen.instance:setSandboxPreset(MainScreen.instance.sandOptions:getSurvivorPreset());
    end
    if self.selectedItem.mode == "Builder" then
        MainScreen.instance:setSandboxPreset(MainScreen.instance.sandOptions:getBuilderPreset());
    end


    if self.selectedItem.mode == "Initial Infection" then
        MainScreen.instance:setSandboxPreset(MainScreen.instance.sandOptions:getBeginnerPreset());
    end

    if self.selectedItem.mode == "One Week Later" then
        MainScreen.instance:setSandboxPreset(MainScreen.instance.sandOptions:getNormalPreset());
    end

    if self.selectedItem.mode == "Six Months Later" then
        MainScreen.instance:setSandboxPreset(MainScreen.instance.sandOptions:getHardPreset());
    end

    if self.selectedItem.mode == "Survival" then
        MainScreen.instance:setSandboxPreset(MainScreen.instance.sandOptions:getDefaultPreset());
    end

    if self.selectedDifficulty then
        if self.selectedDifficulty.internal == "EASY" then
            MainScreen.instance:setEasyPreset();
        end
        if self.selectedDifficulty.internal == "NORMAL" then
            MainScreen.instance:setNormalPreset();
        end
        if self.selectedDifficulty.internal == "HARD" then
            MainScreen.instance:setHardPreset();
        end
        if self.selectedDifficulty.internal == "HARDCORE" then
            MainScreen.instance:setHardcorePreset();
        end
    end
    
    getWorld():setMap("DEFAULT")
    MainScreen.instance.createWorld = true;
    if getWorld():getGameMode() == "Sandbox" then
        getWorld():setDifficulty("Hardcore")
        if WorldSelect.instance:hasChoices() then
            WorldSelect.instance:fillList()
            WorldSelect.instance.previousScreen = "NewGameScreen"
            WorldSelect.instance:setVisible(true, self.joyfocus)
        elseif MainScreen.instance.createWorld or MapSpawnSelect.instance:hasChoices() then
            MapSpawnSelect.instance:fillList()
            MapSpawnSelect.instance.previousScreen = "NewGameScreen"
            MapSpawnSelect.instance:setVisible(true, self.joyfocus)
        else
            MapSpawnSelect.instance:useDefaultSpawnRegion()
            MainScreen.instance.sandOptions:setVisible(true, self.joyfocus)
        end
    else
        if WorldSelect.instance:hasChoices() then
            WorldSelect.instance:fillList()
            WorldSelect.instance.previousScreen = "NewGameScreen"
            WorldSelect.instance:setVisible(true, self.joyfocus)
        elseif MainScreen.instance.createWorld or MapSpawnSelect.instance:hasChoices() then
            MapSpawnSelect.instance:fillList()
            MapSpawnSelect.instance.previousScreen = "NewGameScreen"
            MapSpawnSelect.instance:setVisible(true, self.joyfocus)
        else
            MapSpawnSelect.instance:useDefaultSpawnRegion()
            MainScreen.instance.charCreationProfession.previousScreen = "NewGameScreen"
            MainScreen.instance.charCreationProfession:setVisible(true, self.joyfocus)
        end
    end
end

function NewGameScreen:onGainJoypadFocus(joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData);
    self:setISButtonForB(self.backButton);
    self:setISButtonForA(self.newButton);
    if self.joypadIndex == 0 then
        self.joypadIndex = 1
        self.joypadIndexY = 1
        self.joypadButtons = self.joypadButtonsY[1]
    end
    if #self.joypadButtons >= 1 and self.joypadIndex <= #self.joypadButtons then
        self.joypadButtons[self.joypadIndex]:setJoypadFocused(true, joypadData)
    end
end

function NewGameScreen:onLoseJoypadFocus(joypadData)
    ISPanelJoypad.onLoseJoypadFocus(self, joypadData)
    self.backButton:clearJoypadButton()
    self.newButton:clearJoypadButton()
    self:clearJoypadFocus()
end

function NewGameScreen:Label_setJoypadFocused(focused, joypadData)
    ISPanelJoypad.onGainJoypadFocus(self, joypadData)
    self.joypadFocused = focused
    self:onMouseDown(0, 0)
end

function NewGameScreen:ensureVisible()
    local child = self.joypadButtons[self.joypadIndex]
    if not child then return end
    local y = child:getY()
    if y - 40 < 0 - self.mainPanel:getYScroll() then
        self.mainPanel:setYScroll(0 - y + 40)
    elseif y + child:getHeight() + 40 > 0 - self.mainPanel:getYScroll() + self.mainPanel:getHeight() then
        self.mainPanel:setYScroll(0 - (y + child:getHeight() + 40 - self.mainPanel:getHeight()))
    end
end

function NewGameScreen:onResolutionChange(oldw, oldh, neww, newh)
    local width = math.min(self.mainPanelReqWidth, self.width / 2 + 90 - self.mainPanel.x)
--    self.mainPanel:setX(30 + (self.width / 2 - width) / 2)
    self.mainPanel:setWidth(width)
end

function NewGameScreen:onResetLua(reason)
    if reason == "NewGameMods" then
        MainScreen.instance.bottomPanel:setVisible(false)
        if DebugScenarios.instance ~= nil then
            MainScreen.instance:removeChild(DebugScenarios.instance);
            DebugScenarios.instance = nil;
        end
        self.onMenuItemMouseDown(self.survival, 0, 0)
        self:setVisible(true)
        reactivateJoypadAfterResetLua()
    end
end

function NewGameScreen:onJoypadBeforeDeactivate(joypadData)
--	self.backButton:clearJoypadButton()
--	self.newButton:clearJoypadButton()
	self:clearJoypadFocus()
end

function NewGameScreen:new (x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISPanelJoypad:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.backgroundColor = {r=0, g=0, b=0, a=0.8};
	o.borderColor = {r=1, g=1, b=1, a=0.2};
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.itemheightoverride = {}
	o.selected = 1;
    o.startY = 80;
	NewGameScreen.instance = o;
	return o
end

Events.OnResetLua.Add(function(reason) NewGameScreen.instance:onResetLua(reason) end)

