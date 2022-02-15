require "ISUI/ISPanel"
require "ISUI/ISRichTextPanel"
require "ISUI/ISButton"

---@class ISTutorialPanel : ISPanel
ISTutorialPanel = ISPanel:derive("ISTutorialPanel");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function ISTutorialPanel:initialise()
	ISPanel.initialise(self);
end

--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function ISTutorialPanel:createChildren()

	local tickHgt = math.max(FONT_HGT_SMALL + 4, 20)
	local buttonY = tickHgt
	local buttonHgt = 20
	local panel2Hgt = tickHgt + buttonHgt + 2

	-- CREATE TUTORIAL PANEL
	local panel = ISRichTextPanel:new(0, 0, self.width, self.height - tickHgt - buttonHgt);
	panel.marginLeft, panel.marginRight, panel.marginTop, panel.marginBottom = 4,4,4,4
	panel:initialise();

	self:addChild(panel);
	--panel:paginate();
	self.richtext = panel;
	local panel2 = ISPanel:new(0, 0, self.width, panel2Hgt);
	panel2:initialise();
	self:addChild(panel2);

	-- CREATE BUTTONS

	local previousButton = ISButton:new(3, buttonY, buttonHgt, buttonHgt, "", self, ISTutorialPanel.previousPage);

	previousButton:initialise();
	previousButton:setEnable(false);
	previousButton:setImage(self.prevBtnTxt);
	panel2:addChild(previousButton);
	self.previous = previousButton;

	local nextButton = ISButton:new(2 + buttonHgt + 2, buttonY, buttonHgt, buttonHgt, "", self, ISTutorialPanel.nextPage);

	nextButton:initialise();
	nextButton:setImage(self.nextBtnTxt);
	panel2:addChild(nextButton);
	self.next = nextButton;

	local closeButton = ISButton:new(self.width - buttonHgt - 3, buttonY, buttonHgt, buttonHgt, "", self, ISTutorialPanel.closePage);

	closeButton:initialise();
	closeButton:setImage(self.closeBtnTxt);
	panel2:addChild(closeButton);

	local textWid = getTextManager():MeasureStringX(UIFont.Small, getText("IGUI_Tutorial_MoreInfo"))
	local buttonWid = textWid + 24
    local moreInfo = ISButton:new(closeButton:getX() - buttonWid - 1, buttonY, buttonWid, 20, getText("IGUI_Tutorial_MoreInfo"), self, ISTutorialPanel.moreInfo);

    moreInfo:initialise();
    panel2:addChild(moreInfo);
    self.more = moreInfo;

    local tickBox = ISTickBox:new(3, 0, 20, tickHgt, "", self, ISTutorialPanel.tickBox)
    tickBox:initialise();
    tickBox:addOption(getText("IGUI_Tutorial_ShowGuide"), nil);
    tickBox:setSelected(1, self.showOnStartup);
    panel2:addChild(tickBox);

    self.tickBox = tickBox;

    self.bottompanel = panel2;
end

function ISTutorialPanel:tickBox(index, selected)
    self.showOnStartup = selected;
    if not selected then
        local modal = ISModalDialog:new((getCore():getScreenWidth() / 2) - 150, (getCore():getScreenHeight() / 2) - 60, 300, 120, getText("IGUI_Tutorial_PressF1"), false, nil, nil, nil);
        modal:initialise();
        modal:addToUIManager();
    end
end

function ISTutorialPanel:reloadBtns()
	if not self.tutorialSetInfo:hasNext() then
        self.next:setEnable(false);
--		return;
    else
		self.next:setEnable(true);
	end

	if not self.tutorialSetInfo:hasPrevious() then
        self.previous:setEnable(false);
		return;
    else
		self.previous:setEnable(true);
	end
end

function ISTutorialPanel:previousPage(button)
	self.tutorialSetInfo.currentPage = self.tutorialSetInfo.currentPage - 1;
    self.tutorialSetInfo:applyPageToRichTextPanel(self.richtext);
	if self.moreinfo == nil then
       self:initMoreInfoPanel();
	   self.moreinfo:setVisible(false);
	end
	self.moreInfo = self.tutorialSetInfo:getCurrent().moreTextInfo;
	if self.moreinfo:getIsVisible() then
		self.tut.textDirty = true;
		self.tut.text = self.moreInfo;
		self.tut:paginate();
		self.tut:setYScroll(0);
	end
	self:reloadBtns();
end

function ISTutorialPanel:closePage(button)
	self:setVisible(false);
	if self.moreinfo then
		self.moreinfo:setVisible(false);
	end
end

--************************************************************************--
--** ISTutorialPanel:nextPage
--**
--************************************************************************--
function ISTutorialPanel:nextPage(button)
    self.tutorialSetInfo.currentPage = self.tutorialSetInfo.currentPage + 1;
    self.tutorialSetInfo:applyPageToRichTextPanel(self.richtext);
	if self.moreinfo == nil then
       self:initMoreInfoPanel();
	   self.moreinfo:setVisible(false);
	end
	self.moreInfo = self.tutorialSetInfo:getCurrent().moreTextInfo;
	if self.moreinfo:getIsVisible() then
		self.tut.textDirty = true;
		self.tut.text = self.moreInfo;
		self.tut:paginate();
		self.tut:setYScroll(0);
	end
	self:reloadBtns();
end

function ISTutorialPanel:initMoreInfoPanel()
   self.tut = ISRichTextPanel:new(0, 0, 500, 500);
   self.tut:initialise();
   self.tut:setAnchorBottom(true);
   self.tut:setAnchorRight(true);
   self.moreinfo = self.tut:wrapInCollapsableWindow();
   if getCore():getGameMode() == "Beginner" then -- beginner have tutorial centered READ IT!
       self.moreinfo:setX(10);
       self.moreinfo:setY((getCore():getScreenHeight() / 2) - (self.tut.height / 2));
   else
       self.moreinfo:setX((getCore():getScreenWidth() / 2) - (self.tut.width / 2));
       self.moreinfo:setY((getCore():getScreenHeight() / 2) - (self.tut.height / 2));
   end
	 --self.moreinfo:addScrollBars();
   self.moreinfo:addToUIManager();
   self.tut:setWidth(self.moreinfo:getWidth());
   self.tut:setHeight(self.moreinfo:getHeight()-16);
   self.tut:setY(self.moreinfo:titleBarHeight());
   local scrollBarWid = 0
   self.tut.marginRight = self.tut.marginLeft + scrollBarWid
   self.tut.autosetheight = false;
   self.tut.clip = true
   self.tut:addScrollBars();
end

function ISTutorialPanel:moreInfo(button)
    if self.moreinfo == nil then
       self:initMoreInfoPanel();
    else
		if self.moreinfo:getIsVisible() then
			self.moreinfo:setVisible(false);
		else
			self.moreinfo:setVisible(true);
		end
     --   self.tut =ISRichTextPanel:new(0, 0, 500, 500);
    end

	self.tut:setYScroll(0);

    local current = self.tut;
    current.textDirty = true;
    current.text = self.moreInfo;
    current:paginate();
end

--************************************************************************--
--** ISTutorialPanel:render
--**
--************************************************************************--
function ISTutorialPanel:prerender()
	self.backgroundColor.a = 0.5
	if self.tut then -- created yet?
		self.tut.backgroundColor.a = 0.5
	end
	self:drawRect(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
	self:drawRect(0, 0, self.width, 1, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	self:drawRect(0, self.height-1, self.width, 1, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	self:drawRect(0, 0, 1, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	self:drawRect(0+self.width-1, 0, 1, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	--self:setStencilRect(0,0,self.width-1, self.height-1);

--~     if self.tutorialSetInfo:hasNext() then
--~         self.next.title = ">";
--~     else
--~         self.next.title = "X";
--~     end
end

function ISTutorialPanel:render()

	--self:clearStencilRect();

end


--************************************************************************--
--** ISTutorialPanel:update
--**
--************************************************************************--
function ISTutorialPanel:update()
	if self.tutorialSetInfo ~= nil then
		local currentPage = self.tutorialSetInfo:getCurrent();
		if currentPage ~= nil then
			if currentPage.nextcondition == nil then
				self.next:setVisible(true);
			else
				self.next:setVisible(false);
			end
		end
		self.tutorialSetInfo:update(self.richtext);
	end

	self.bottompanel:setY(self.richtext:getHeight());
	self.bottompanel:setX(0);
	local w = getCore():getScreenWidth();
	local h = getCore():getScreenHeight();

	self:setHeight(self.richtext:getHeight() + self.bottompanel:getHeight());

    if getCore():getGameMode() ~= "Beginner" then
        self:setY((getCore():getScreenHeight() / 2) - (self.height / 2));
        self:setX(w - self:getWidth() - 20);
    end
end


--************************************************************************--
--** ISPanel:new
--**
--************************************************************************--
function ISTutorialPanel:new (x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
    self.__index = self
	o.x = x;
	o.y = y;
	o.borderColor = {r=1, g=1, b=1, a=0.7};
	o.backgroundColor = {r=0, g=0, b=0, a=0.5};
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	o.prevBtnTxt = getTexture("media/ui/sGuidePrevBtn.png");
	o.nextBtnTxt = getTexture("media/ui/sGuideNextBtn.png");
	o.closeBtnTxt = getTexture("media/ui/sGuideCloseBtn.png");
    o.showOnStartup = true;
   return o
end
