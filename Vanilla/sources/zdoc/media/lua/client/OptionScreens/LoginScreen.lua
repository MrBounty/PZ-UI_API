require "ISUI/ISPanel"
require "ISUI/ISButton"
require "ISUI/ISInventoryPane"
require "ISUI/ISResizeWidget"
require "ISUI/ISMouseDrag"

require "defines"

---@class LoginScreen : ISPanel
LoginScreen = ISPanel:derive("LoginScreen");


function LoginScreen:initialise()
	ISPanel.initialise(self);
end


--************************************************************************--
--** ISPanel:instantiate
--**
--************************************************************************--
function LoginScreen:instantiate()

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

function LoginScreen:addCombo(x, y, w, h, name, options, selected, target, onchange)

    local label = ISLabel:new(x, y, h, name, 1, 1, 1, 1, UIFont.Small);
    label:initialise();
    self:addChild(label);
    label:setAnchorRight(true);
    local panel2 = ISComboBox:new(x+20, y, w-140, h, target, onchange);
    panel2:initialise();

    for i, k in ipairs(options) do
        panel2:addOption(k);
    end

    panel2.selected = selected;
    self:addChild(panel2);
    panel2:setAnchorRight(true);

    return panel2;
end

function LoginScreen:onChange(box)
    if box.options ~= nil and box.options[box.selected] ~= nil then
        local sel = box.options[box.selected];
        if sel == "Desura" then
            self.passwordLabel:setVisible(false);
            self.passwordEntry:setVisible(false);
            self.userName.name = getText("UI_loginscreen_desurakey");
        else
            self.passwordLabel:setVisible(true);
            self.passwordEntry:setVisible(true);
            self.userName.name = getText("UI_loginscreen_username");
        end
    end
end



function LoginScreen:create()

    self.puchMeth = ISLabel:new(140, 50-19, 50, getText("UI_loginscreen_purchasemethod"), 1, 1, 1, 1, UIFont.Medium);
    self.puchMeth:initialise();
    self.puchMeth:instantiate();

    self:addChild(self.puchMeth);

    local combo = ISComboBox:new(self.puchMeth:getX() + self.puchMeth:getWidth() + 17, 50, 220, 18, self, LoginScreen.onChange);
    combo:initialise();

    combo:addOption(getText("UI_loginscreen_desura"));
    combo:addOption(getText("UI_loginscreen_google"));
    self.purchMethod = combo;
    self:addChild(combo);

    self.userName = ISLabel:new(140, 80, 50, getText("UI_loginscreen_desurakey"), 1, 1, 1, 1, UIFont.Medium);
	self.userName:initialise();
	self.userName:instantiate();

	self:addChild(self.userName);

	self.userNameEntry = ISTextEntryBox:new(self.state:getCachedUsername(), self.userName:getX() + self.userName:getWidth() + 17, self.userName:getY() + 18, 280, 18);
	self.userNameEntry:initialise();
	self.userNameEntry:instantiate();

	self.userNameEntry:setAnchorRight(true);
	self:addChild(self.userNameEntry);

	self.passwordLabel = ISLabel:new(140, 80+24, 50, getText("UI_loginscreen_password"), 1, 1, 1, 1, UIFont.Medium);
	self.passwordLabel:initialise();
	self.passwordLabel:instantiate();

	self:addChild(self.passwordLabel);
    self.passwordLabel:setVisible(false);
	self.passwordEntry = ISTextEntryBox:new(self.state:getCachedPassword(), self.passwordLabel:getX() + self.passwordLabel:getWidth() + 17, self.passwordLabel:getY() + 18, 280, 18);
	self.passwordEntry:initialise();
	self.passwordEntry:instantiate();

	self.passwordEntry:setMasked(true);
	self.passwordEntry:setAnchorRight(true);
	self:addChild(self.passwordEntry);
    self.passwordEntry:setVisible(false);

    self.backButton = ISButton:new(16, self.height-30, 100, 25, getText("UI_btn_exit"), self, LoginScreen.onOptionMouseDown);
	self.backButton.internal = "BACK";
	self.backButton:initialise();
	self.backButton:instantiate();
	self.backButton:setAnchorLeft(true);
	self.backButton:setAnchorTop(false);
	self.backButton:setAnchorBottom(true);
	self.backButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.backButton);


	self.playButton = ISButton:new(self.width - 116, self.height-30, 100, 25, getText("UI_loginscreen_login"), self, LoginScreen.onOptionMouseDown);
	self.playButton.internal = "LOGIN";
	self.playButton:initialise();
	self.playButton:instantiate();
	self.playButton:setAnchorLeft(false);
	self.playButton:setAnchorRight(true);
	self.playButton:setAnchorTop(false);
	self.playButton:setAnchorBottom(true);
	self.playButton.borderColor = {r=1, g=1, b=1, a=0.1};
	self:addChild(self.playButton);



end

function LoginScreen:prerender()

	ISPanel.prerender(self);

	if self.failed and not self.success then
		self:drawTextCentre(getText("UI_loginscreen_loginfailed"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Large);
	else
		self:drawTextCentre(getText("UI_loginscreen_login"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Large);
	end

	if self.success then
		self:drawTextCentre(getText("UI_loginscreen_loginsuccess"), self.width / 2, self.height / 2, 1, 1, 1, 1, UIFont.Large);
	end
end

function LoginScreen:onOptionMouseDown(button, x, y)
	if button.internal == "LOGIN" then
        local auth = false;

        if self.purchMethod.selected == 1 then
            auth = self.state:Test(self.userNameEntry:getInternalText());
        else
            auth = self.state:Test(self.userNameEntry:getInternalText(), self.passwordEntry:getInternalText());
       end
		  if auth then
			  self.playButton:setVisible(false);
			  self.backButton:setVisible(false);
			  self.passwordEntry:setVisible(false);
			  self.userNameEntry:setVisible(false);
			  self.userName:setVisible(false);
			  self.passwordLabel:setVisible(false);
			  self.success = true;
		  else
			  self.failed = true;
		  end
	end
	if button.internal == "BACK" then
		getCore():quit();
	end
end

function LoginScreen:new (x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISPanel:new(x, y, width, height);
	setmetatable(o, self)
	self.__index = self
	o.x = x;
	o.y = y;
	o.backgroundColor = {r=0.3, g=0.3, b=0.3, a=0.3};
	o.borderColor = {r=1, g=1, b=1, a=0.4};
	o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
	return o
end

function createLoginScreen(state)

	local w = getCore():getScreenWidth();
	local h = getCore():getScreenHeight();
	local x = (w/2) - 300;
	local y = (h/2) - 150;
	local login = LoginScreen:new(x, y, 500, 300);
	login:initialise();
	login:addToUIManager();
	login.state = state;
	login:create();
	LoginScreen.instance = login;
end

function deleteLoginScreen()

	if LoginScreen.instance ~= nil then
		LoginScreen.instance:removeFromUIManager();
	end

end

Events.OnLoginState.Add(createLoginScreen);
Events.OnLoginStateSuccess.Add(deleteLoginScreen);

