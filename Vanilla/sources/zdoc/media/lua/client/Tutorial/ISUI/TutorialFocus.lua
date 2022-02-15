require "ISUI/ISPanel"

---@class TutorialFocus : ISPanel
TutorialFocus = ISPanel:derive("TutorialFocus");


--************************************************************************--
--** ISPanel:initialise
--**
--************************************************************************--

function TutorialFocus:initialise()
    ISPanel.initialise(self);
end

--************************************************************************--
--** ISPanel:render
--**
--************************************************************************--
function TutorialFocus:prerender()
    self:drawRect(0, 0, self.width, self.focusy, 0.7, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRect(0, self.focusy2, self.width, self.height-self.focusy2, 0.7, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);

    self:drawRect(0, self.focusy, self.focusx, self.focusheight, 0.7, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
    self:drawRect(self.focusx2, self.focusy, self.width-self.focusx2, self.focusheight, 0.7, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);



end

function TutorialFocus:isInside(x, y)
    if x > self.focusx2 or y > self.focusy2 or x < self.focusx or y < self.focusy then return false; end

    return true;
end

--************************************************************************--
--** ISUIElement:onRightMouseUp
--**
--************************************************************************--
function TutorialFocus:onRightMouseUp(x, y)
    if self:isInside(x, y) then return false; end
    return true;
end

--************************************************************************--
--** ISUIElement:onRightMouseDown
--**
--************************************************************************--
function TutorialFocus:onRightMouseDown(x, y)
    if self:isInside(x, y)then  return false; end
    return true;
end


--************************************************************************--
--** ISUIElement:onRightMouseUp
--**
--************************************************************************--
function TutorialFocus:onMouseUp(x, y)
    if self:isInside(x, y) then return false; end
    return true;
end

--************************************************************************--
--** ISUIElement:onRightMouseDown
--**
--************************************************************************--
function TutorialFocus:onMouseDown(x, y)
    if self:isInside(x, y) then  return false; end
    return true;
end


--************************************************************************--
--** ISPanel:new
--**
--************************************************************************--
function TutorialFocus:new (focusx, focusy, focuswidth, focusheight)
    local o = {};
    o = ISPanel:new(0, 0, getCore():getScreenWidth(), getCore():getScreenHeight());
    setmetatable(o, self);
    self.__index = self
    o:noBackground();
    o.x = 0;
    o.y = 0;
    o.focusx = focusx;
    o.focusy = focusy;
    o.focuswidth = focuswidth;
    o.focusheight = focusheight;
    o.focusx2 = o.focusx + focuswidth;
    o.focusy2 = o.focusy + focusheight;
    o.width = getCore():getScreenWidth();
    o.height = getCore():getScreenHeight();
    o.backgroundColor = {r=0, g=0, b=0, a=0.0};
    o.borderColor = {r=1, g=1, b=1, a=0.7};
    o.mouseover = false;
    o.anchorLeft = true;
    o.anchorRight = true;
    o.anchorTop = true;
    o.anchorBottom = true;
    return o
end

function TutorialFocus.set(x, y, w, h)
    print("TutorialFocus");
    local t = TutorialFocus:new(x, y, w, h);
    if TutorialFocus.instance ~= nil then
        TutorialFocus.instance:removeFromUIManager();
    end
    TutorialFocus.instance = t;
    t:initialise();
    t:addToUIManager();
end

function TutorialFocus.unset()
    if TutorialFocus.instance ~= nil then
        TutorialFocus.instance:removeFromUIManager();
    end
    TutorialFocus.instance = nil;
end

--Events.OnMainMenuEnter.Add(TestFocus);
