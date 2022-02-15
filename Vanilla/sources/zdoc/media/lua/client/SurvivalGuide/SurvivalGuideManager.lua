---@class SurvivalGuideManager : ISBaseObject
SurvivalGuideManager = ISBaseObject:derive("SurvivalGuideManager");

function SurvivalGuideManager:update()
     if self.panel == nil then
         self.panel = ISTutorialPanel:new(0, 0, 240, 250);
         self.panel:initialise();
         self.panel:addToUIManager();
         self.panel.tutorialSetInfo = ISTutorialSetInfo:new();

         local entry = SurvivalGuideEntries.getEntry(0);
         self.panel.tutorialSetInfo:addPage(entry.title, entry.text, entry.moreInfo);

         self.panel.tutorialSetInfo:applyPageToRichTextPanel(self.panel.richtext);
         self.panel.moreInfo = entry.moreInfo;

		 for i=1,SurvivalGuideEntries.list:size() -1 do
			entry = SurvivalGuideEntries.getEntry(i);
			self.panel.tutorialSetInfo:addPage(entry.title, entry.text, entry.moreInfo);
		 end

		ISLayoutManager.RegisterWindow('survivalguide', SurvivalGuideManager, self) 
     end
end

function SurvivalGuideManager:new()
    local o = {}
    setmetatable(o, self)
    self.__index = self
    self.moreInfoVisible = false
    return o
end

function SurvivalGuideManager:RestoreLayout(name, layout)
	-- Only save/restore visibility, the size/position is fixed
	if layout.visible == 'false' then
		self.panel:setVisible(false);
        self.panel.showOnStartup = false;
    else
        self.panel.showOnStartup = true;
    end

    -- always show the guide for beginner
    if getCore():getGameMode() == "Beginner" then
        self.panel:setVisible(true);
        self.panel.showOnStartup = true;
        self.panel:setX(getCore():getScreenWidth()/2 - self.panel.width / 2);
        self.panel:setY(getCore():getScreenHeight()/2 - self.panel.height / 2);
        self.panel.tickBox:setVisible(false);
    end

    if self.panel.tickBox then
        self.panel.tickBox:setSelected(1, self.panel.showOnStartup);
    end
end

function SurvivalGuideManager:SaveLayout(name, layout)
	-- Only save/restore visibility, the size/position is fixed
    if self.panel then
        if not self.panel.showOnStartup then
            layout.visible = 'false';
            self.panel:setVisible(false);
        else
            layout.visible = 'true';
            self.panel:setVisible(true);
        end
        ISLayoutManager.SaveWindowVisible(self.panel, layout);
    end
end

function doSurvivalGuide()
	if isServer() then return; end
	-- Disable with controller for now.
	local joypadData = JoypadState.players[1]
	if joypadData then return end
    -- hide it for tut
--    if getCore():getGameMode() == "Tutorial" and not getCore():isTutorialDone() then
--        return;
--    end
    -- only happens on single player so no splitscreen support required.
    if SurvivalGuideManager.instance == nil then
        SurvivalGuideManager.instance = SurvivalGuideManager:new();
    end

    SurvivalGuideManager.instance:update();
    if (getCore():getGameMode() == "Tutorial" and SurvivalGuideManager.blockSurvivalGuide) or (isDemo() and ISDemoPopup.instance ~= nil) then
        SurvivalGuideManager.instance.panel:setVisible(false);
    end
end

SurvivalGuideManager.OnNewGame = function()
	if isServer() then return; end
	Events.OnTick.Add(doSurvivalGuide);
end


SurvivalGuideManager.onKeyPressed = function(key)

	if key == getCore():getKey("Toggle Survival Guide") and not SurvivalGuideManager.blockSurvivalGuide then
		if SurvivalGuideManager.instance == nil then
			Events.OnTick.Add(doSurvivalGuide);
		else
			SurvivalGuideManager.instance.panel:setVisible(not SurvivalGuideManager.instance.panel:getIsVisible());
			local panel = SurvivalGuideManager.instance.panel
			if panel.moreinfo and panel:getIsVisible() then
				panel.moreinfo:setVisible(SurvivalGuideManager.instance.moreInfoVisible)
			elseif panel.moreinfo then
				SurvivalGuideManager.instance.moreInfoVisible = panel.moreinfo:getIsVisible()
				panel.moreinfo:setVisible(false)
			end
		end
	end
end

SurvivalGuideManager.OnGameStart = function()
	if JoypadState.players[1] then return end
	if getCore():isShowFirstTimeSneakTutorial() and getCore():getGameMode() ~= "Tutorial" then
		if SurvivalGuideManager.instance == nil then
			SurvivalGuideManager.instance = SurvivalGuideManager:new();
			SurvivalGuideManager.instance:update();
		end
		local panel = SurvivalGuideManager.instance.panel
		SurvivalGuideManager.instance.panel:setVisible(true);
		if not panel.moreinfo then
			panel:initMoreInfoPanel();
		end
		panel.moreinfo:setVisible(true)
		panel.moreInfo = panel.tutorialSetInfo.pages[11].moreTextInfo;
		panel.tut.textDirty = true;
		panel.tut.text = panel.moreInfo;
		panel.tut:paginate();
		panel.tut:setYScroll(0);
		panel.tutorialSetInfo.currentPage = 11;
		panel.tutorialSetInfo:applyPageToRichTextPanel(panel.richtext);
		panel:reloadBtns();
		getCore():setShowFirstTimeSneakTutorial(false);
		getCore():saveOptions();
	end
end

Events.OnGameStart.Add(SurvivalGuideManager.OnGameStart)
--Events.OnEnterVehicle.Add(SurvivalGuideManager.onEnterVehicle);
Events.OnKeyPressed.Add(SurvivalGuideManager.onKeyPressed);
--Events.OnNewGame.Add(SurvivalGuideManager.OnNewGame);
Events.OnTick.Add(doSurvivalGuide);
--~ Events.OnMainMenuEnter.Add(doSurvivalGuide);
