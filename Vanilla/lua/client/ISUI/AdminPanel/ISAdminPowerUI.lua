--***********************************************************
--**              	  ROBERT JOHNSON                       **
--***********************************************************

if not isClient() then return end

ISAdminPowerUI = ISPanel:derive("ISAdminPowerUI");
ISAdminPowerUI.messages = {};

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISAdminPowerUI:initialise
--**
--************************************************************************--

function ISAdminPowerUI:initialise()
    ISPanel.initialise(self);
    local btnWid = 100
    local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
    local padBottom = 10

    self.ok = ISButton:new(10, self:getHeight() - padBottom - btnHgt, btnWid, btnHgt, getText("IGUI_RadioSave"), self, ISAdminPowerUI.onClick);
    self.ok.internal = "SAVE";
    self.ok.anchorTop = false
    self.ok.anchorBottom = true
    self.ok:initialise();
    self.ok:instantiate();
    self.ok.borderColor = {r=1, g=1, b=1, a=0.1};
    self:addChild(self.ok);
    
    self.tickBox = ISTickBox:new(30, 50, 100, FONT_HGT_SMALL + 5, "Admin Powers", self, self.onTicked)
    self.tickBox.backgroundColor.a = 1
    self.tickBox.background = true
    self.tickBox.choicesColor = {r=1, g=1, b=1, a=1}
    self.tickBox.leftMargin = 2
    self.tickBox:setFont(UIFont.Small)
    self:addChild(self.tickBox);

    self.richText = ISRichTextLayout:new(self.width - 30 * 2)
    self.richText.marginLeft = 0
    self.richText.marginTop = 0
    self.richText.marginRight = 0
    self.richText.marginBottom = 0
    self.richText:setText(getText("IGUI_AdminPanel_ShowAdminTag"))
    self.richText:initialise()
    self.richText:paginate()

    self:addAdminPowerOptions()
end

function ISAdminPowerUI:addAdminPowerOptions()
    self.setFunction = {}
    self:addOption("Invisible", self.player:isInvisible(), function(self, selected)
        self.player:setInvisible(selected);
    end);
    self:addOption("God mode", self.player:isGodMod(), function(self, selected)
        self.player:setGodMod(selected);
    end);
    self:addOption("Ghost mode", self.player:isGhostMode(), function(self, selected)
        self.player:setGhostMode(selected);
    end);
    self:addOption("No Clip", self.player:isNoClip(), function(self, selected)
        self.player:setNoClip(selected);
    end);
    self:addOption("Timed Action Instant", self.player:isTimedActionInstantCheat(), function(self, selected)
        self.player:setTimedActionInstantCheat(selected);
    end);
    self:addOption("Unlimited Carry", self.player:isUnlimitedCarry(), function(self, selected)
        self.player:setUnlimitedCarry(selected);
    end);
    self:addOption("Unlimited Endurance", self.player:isUnlimitedEndurance(), function(self, selected)
        self.player:setUnlimitedEndurance(selected);
    end);
    self:addOption(getText("IGUI_AdminPanel_BuildCheat"), ISBuildMenu.cheat, function(self, selected)
        ISBuildMenu.cheat = selected;
        self.player:setBuildCheat(selected);
    end);
    self:addOption(getText("IGUI_AdminPanel_FarmingCheat"), ISFarmingMenu.cheat, function(self, selected)
        ISFarmingMenu.cheat = selected;
        self.player:setFarmingCheat(selected);
    end);
    self:addOption(getText("IGUI_AdminPanel_HealthCheat"), ISHealthPanel.cheat, function(self, selected)
        ISHealthPanel.cheat = selected;
        self.player:setHealthCheat(selected);
    end);
    self:addOption(getText("IGUI_AdminPanel_MechanicsCheat"), ISVehicleMechanics.cheat, function(self, selected)
        ISVehicleMechanics.cheat = selected;
        self.player:setMechanicsCheat(selected);
    end);
    self:addOption(getText("IGUI_AdminPanel_MoveableCheat"), ISMoveableDefinitions.cheat, function(self, selected)
        ISMoveableDefinitions.cheat = selected;
        self.player:setMovablesCheat(selected);
    end);

    self:addOption(getText("IGUI_AdminPanel_CanSeeAll"), self.player:isCanSeeAll(), function(self, selected)
        self.player:setCanSeeAll(selected)
    end);
	self:addOption(getText("IGUI_AdminPanel_NetworkTeleportEnabled"), self.player:isNetworkTeleportEnabled(), function(self, selected)
        self.player:setNetworkTeleportEnabled(selected)
    end);

    self:addOption(getText("IGUI_AdminPanel_CanHearAll"), self.player:isCanHearAll(), function(self, selected)
        self.player:setCanHearAll(selected)
    end);

    self:addOption(getText("IGUI_AdminPanel_ZombiesDontAttack"), self.player:isZombiesDontAttack(), function(self, selected)
        self.player:setZombiesDontAttack(selected)
    end);

    self:addOption(getText("IGUI_AdminPanel_ShowMPInfos"), self.player:isShowMPInfos(), function(self, selected)
        self.player:setShowMPInfos(selected)
    end);

    self.tickBox:setWidthToFit()

    self:setHeight(self.tickBox:getBottom() + 40 +
        self.richText:getHeight() + 20 + self.ok:getHeight() + 10)
end

function ISAdminPowerUI:addOption(text, selected, setFunction)
    local n = self.tickBox:addOption(text)
    self.tickBox:setSelected(n, selected)
    self.setFunction[n] = setFunction
end

function ISAdminPowerUI:onTicked(index, selected)
    
end

function ISAdminPowerUI:prerender()
    local z = 20;
    local splitPoint = 100;
    local x = 10;
    self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRectBorder(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(getText("IGUI_AdminPanel_AdminPower"), self.width/2 - (getTextManager():MeasureStringX(UIFont.Medium, getText("IGUI_AdminPanel_AdminPower")) / 2), z, 1,1,1,1, UIFont.Medium);

    self.richText:render(30, self.ok.y - 20 - self.richText:getHeight(), self)
end

function ISAdminPowerUI:onClick(button)
    if button.internal == "SAVE" then
        for i=1,#self.tickBox.options do
            self.setFunction[i](self, self.tickBox:isSelected(i))
        end
        self.player:setShowAdminTag(false);
        for i,v in pairs(self.tickBox.selected) do
            if self.tickBox.selected[i] then
                self.player:setShowAdminTag(true);
                break;
            end
        end
    
        sendPlayerExtraInfo(self.player)
    
        self:setVisible(false);
        self:removeFromUIManager();
    end
end

ISAdminPowerUI.onGameStart = function()
    ISBuildMenu.cheat = getPlayer():isBuildCheat();
    ISFarmingMenu.cheat = getPlayer():isFarmingCheat();
    ISHealthPanel.cheat = getPlayer():isHealthCheat();
    ISMoveableDefinitions.cheat = getPlayer():isMovablesCheat();
    ISVehicleMechanics.cheat = getPlayer():isMechanicsCheat();
end

--************************************************************************--
--** ISAdminPowerUI:new
--**
--************************************************************************--
function ISAdminPowerUI:new(x, y, width, height, player)
    local o = {}
    x = getCore():getScreenWidth() / 2 - (width / 2);
    y = getCore():getScreenHeight() / 2 - (height / 2);
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.width = width;
    o.height = height;
    o.player = player;
    o.moveWithMouse = true;
    ISAdminPowerUI.instance = o;
    return o;
end

Events.OnGameStart.Add(ISAdminPowerUI.onGameStart)
