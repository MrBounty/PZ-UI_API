require "ISUI/ISPanelJoypad"
require "ISUI/ISRichTextPanel"
require "ISUI/ISButton"

---@class ISDemoPopup : ISPanel
ISDemoPopup = ISPanel:derive("ISDemoPopup");


--************************************************************************--
--** ISDemoPopup:initialise
--**
--************************************************************************--

function ISDemoPopup:initialise()
    ISPanelJoypad.initialise(self);
end

--************************************************************************--
--** ISDemoPopup:instantiate
--**
--************************************************************************--
function ISDemoPopup:createChildren()

    -- CREATE TUTORIAL PANEL
    local panel = ISRichTextPanel:new(5, 10, self.width-10, self.height-32-10);
    panel:initialise();

    self:addChild(panel);
    --panel:paginate();
    self.richtext = panel;
    self.richtext.text = getText("UI_Demo_Popup");
    self.richtext:paginate();
    self.richtext.backgroundColor.a = 0;

end


function ISDemoPopup:setInfo(item)

end

--************************************************************************--
--** ISDemoPopup:onMouseDown
--**
--************************************************************************--
function ISDemoPopup:onMouseDown(x, y)
    self:removeSelf()
end

function ISDemoPopup:onMouseDownOutside(x, y)
    self:removeSelf()
end

function ISDemoPopup:onJoypadDown(button, joypadData)
    self:removeSelf()
    joypadData.focus = nil
    joypadData.lastfocus = nil
end

function ISDemoPopup:removeSelf()
    ISDemoPopup.instance = nil;
    self:removeFromUIManager()
end

function ISDemoPopup.OnJoypadActivate(id)
    if ISDemoPopup.instance and not ISDemoPopup.instance.joyfocus then
        ISDemoPopup.instance:removeSelf()
    end
end

ISDemoPopup.getInstance = function()
    if ISDemoPopup.instance ~= nil then
        return ISDemoPopup.instance;
    end;
    local x = getCore():getScreenWidth() /2;
    local y = getCore():getScreenHeight() / 2;
    x = x - (333 / 2);
    y = y - 70;
    ISDemoPopup.instance = ISDemoPopup:new(x, y, 330, 140);
    ISDemoPopup.instance:initialise();
    ISDemoPopup.instance:addToUIManager();
    local joypadData = JoypadState.getMainMenuJoypad()
    if joypadData then
        if not joypadData.player then
            JoypadState.forceActivate = joypadData.id
            JoypadState.onGameStart()
        end
        joypadData.focus = ISDemoPopup.instance
        updateJoypadFocus(joypadData)
    end
   -- SurvivalGuideManager.instance.panel:setVisible(false);
    return ISDemoPopup.instance;
end

--************************************************************************--
--** ISDemoPopup:new
--**
--************************************************************************--
function ISDemoPopup:new (x, y, width, height)
    local o = {}
    --o.data = {}
    o = ISPanelJoypad:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.borderColor = {r=1, g=1, b=1, a=0.7};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.timer = 0;
    return o
end

function DoDemoPopup()
    if getCore():getGameMode() == "Tutorial" then return end
    ISDemoPopup.getInstance();
end

if isDemo() then
    Events.OnGameStart.Add(DoDemoPopup);
    Events.OnJoypadActivate.Add(ISDemoPopup.OnJoypadActivate)
end
