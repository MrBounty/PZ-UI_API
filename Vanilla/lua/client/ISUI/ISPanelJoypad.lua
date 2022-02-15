require "ISUI/ISUIElement"

ISPanelJoypad = ISUIElement:derive("ISPanelJoypad");

--************************************************************************--
--** ISPanelJoypad:initialise
--**
--************************************************************************--

function ISPanelJoypad:initialise()
	ISUIElement.initialise(self);
end

function ISPanelJoypad:setVisible(visible, joypadData)
    if visible and joypadData then
        joypadData.focus = self
        updateJoypadFocus(joypadData)
    end
    ISUIElement.setVisible(self, visible);
end

function ISPanelJoypad:insertNewLineOfButtons(button1, button2, button3, button4, button5, button6, button7, button8, button9, button10)
    local newLine = {};
    if button1 then table.insert(newLine, button1); end
    if button2 then table.insert(newLine, button2); end
    if button3 then table.insert(newLine, button3); end
    if button4 then table.insert(newLine, button4); end
    if button5 then table.insert(newLine, button5); end
    if button6 then table.insert(newLine, button6); end
    if button7 then table.insert(newLine, button7); end
    if button8 then table.insert(newLine, button8); end
    if button9 then table.insert(newLine, button9); end
    if button10 then table.insert(newLine, button10); end
    self.joypadButtons = newLine;
    table.insert(self.joypadButtonsY, newLine);
    return newLine;
end

function ISPanelJoypad:insertNewListOfButtons(list)
    self.joypadButtons = list;
    table.insert(self.joypadButtonsY, list);
end

function ISPanelJoypad:insertNewListOfButtonsList(list)
    self.joypadButtons = list;
    table.insert(self.joypadButtonsY, list);
end

function ISPanelJoypad:noBackground()
	self.background = false;
end

function ISPanelJoypad:close()
	self:setVisible(false);
end

function ISPanelJoypad:setISButtonForA(button)
    self.ISButtonA = button;
    button:setJoypadButton(Joypad.Texture.AButton);
end

function ISPanelJoypad:setISButtonForB(button)
    self.ISButtonB = button;
    button:setJoypadButton(Joypad.Texture.BButton);
end

function ISPanelJoypad:setISButtonForY(button)
    self.ISButtonY = button;
    button:setJoypadButton(Joypad.Texture.YButton);
end

function ISPanelJoypad:setISButtonForX(button)
    self.ISButtonX = button;
    button:setJoypadButton(Joypad.Texture.XButton);
end

function ISPanelJoypad:onJoypadDown(button)
    local children = self:getVisibleChildren(self.joypadIndexY)
    local child = children[self.joypadIndex]

    if button == Joypad.AButton and child and (child.isButton or child.isCombobox or child.isTickBox or child.isKnob or child.isRadioButtons) then
        child:forceClick();
        return;
    end
    if button == Joypad.BButton and child and child.isCombobox and child.expanded then
        child.expanded = false;
        child:hidePopup();
        return;
    end

    if button == Joypad.BButton and self.ISButtonB then
        self.ISButtonB:forceClick();
    elseif button == Joypad.AButton and self.ISButtonA then
        self.ISButtonA:forceClick();
    elseif button == Joypad.XButton and self.ISButtonX then
        self.ISButtonX:forceClick();
    elseif button == Joypad.YButton and self.ISButtonY then
        self.ISButtonY:forceClick();
    end
end

function ISPanelJoypad:getVisibleChildren(joypadIndexY)
    local children = {}
    if self.joypadButtonsY[joypadIndexY] then
        local children1 = self.joypadButtonsY[joypadIndexY]
        for _,child in ipairs(children1) do
            if child:isVisible() then
                table.insert(children, child)
            end
        end
    end
    return children
end

function ISPanelJoypad:onJoypadDirLeft(joypadData)
    local children = self:getVisibleChildren(self.joypadIndexY)
    local child = children[self.joypadIndex]
    if child and child.isSlider then
        child:onJoypadDirLeft(joypadData)
    elseif #children > 0 and self.joypadIndex > 1 then
        children[self.joypadIndex]:setJoypadFocused(false, joypadData);
        self.joypadIndex = self.joypadIndex - 1;
        children[self.joypadIndex]:setJoypadFocused(true, joypadData);
    end
    self:ensureVisible()
end

function ISPanelJoypad:onJoypadDirRight(joypadData)
    local children = self:getVisibleChildren(self.joypadIndexY)
    local child = children[self.joypadIndex]
    if child and child.isSlider then
        child:onJoypadDirRight(joypadData)
    elseif #children > 0 and self.joypadIndex ~= #children then
        children[self.joypadIndex]:setJoypadFocused(false, joypadData);
        self.joypadIndex = self.joypadIndex + 1;
        children[self.joypadIndex]:setJoypadFocused(true, joypadData);
    end
    self:ensureVisible()
end

function ISPanelJoypad:onJoypadDirUp(joypadData)
    local children = self:getVisibleChildren(self.joypadIndexY)
    local child = children[self.joypadIndex]
    if child and child.isCombobox and child.expanded then
        child:onJoypadDirUp(joypadData)
    elseif child and child.isRadioButtons and child.joypadIndex > 1 then
        child:onJoypadDirUp(joypadData)
    elseif child and child.isTickBox and child.joypadIndex > 1 then
        child:onJoypadDirUp(joypadData)
    elseif child and child.isKnob then
        child:onJoypadDirUp(joypadData)
    else
        if #self.joypadButtonsY > 0 and self.joypadIndexY > 1 then
            child:setJoypadFocused(false, joypadData);
            self.joypadIndexY = self.joypadIndexY - 1;
            self.joypadButtons = self.joypadButtonsY[self.joypadIndexY];
            children = self:getVisibleChildren(self.joypadIndexY)
            if self.joypadIndex > #children then
                self.joypadIndex = #children;
            end
            children[self.joypadIndex]:setJoypadFocused(true, joypadData);
        end
    end
    self:ensureVisible()
end

function ISPanelJoypad:onJoypadDirDown(joypadData)
    local children = self:getVisibleChildren(self.joypadIndexY)
    local child = children[self.joypadIndex]
    if child and child.isCombobox and child.expanded then
        child:onJoypadDirDown(joypadData)
    elseif child and child.isRadioButtons and child.joypadIndex < #child.options then
        child:onJoypadDirDown(joypadData)
    elseif child and child.isTickBox and child.joypadIndex < #child.options then
        child:onJoypadDirDown(joypadData)
    elseif child and child.isKnob then
        child:onJoypadDirDown(joypadData)
    else
        if #self.joypadButtonsY > 0 and self.joypadIndexY ~= #self.joypadButtonsY then
            child:setJoypadFocused(false, joypadData);
            self.joypadIndexY = self.joypadIndexY + 1;
            self.joypadButtons = self.joypadButtonsY[self.joypadIndexY];
            children = self:getVisibleChildren(self.joypadIndexY)
            if self.joypadIndex > #children then
                self.joypadIndex = #children;
            end
            children[self.joypadIndex]:setJoypadFocused(true, joypadData);
        end
    end
    self:ensureVisible()
end

function ISPanelJoypad:getJoypadFocus()
    local children = self:getVisibleChildren(self.joypadIndexY)
    return children[self.joypadIndex]
end

function ISPanelJoypad:setJoypadFocus(child, joypadData)
    for indexY,buttons in ipairs(self.joypadButtonsY) do
        for indexX,button in ipairs(buttons) do
            if button == child then
                self:clearJoypadFocus(joypadData)
                self.joypadIndexY = indexY
                self.joypadIndex = indexX
                self.joypadButtons = buttons
                child:setJoypadFocused(true, joypadData)
                return
            end
        end
    end
end

function ISPanelJoypad:restoreJoypadFocus(joypadData)
    local child = self:getJoypadFocus()
    if child then
        child:setJoypadFocused(true, joypadData)
    end
end

function ISPanelJoypad:clearJoypadFocus(joypadData)
    local child = self:getJoypadFocus()
    if child then
        child:setJoypadFocused(false, joypadData)
    end
end

function ISPanelJoypad:ensureVisible()
    if not self.joyfocus then return end
    local children = self:getVisibleChildren(self.joypadIndexY)
    local child = children[self.joypadIndex]
    if not child then return end
    local y = child:getY()
    if y - 40 < 0 - self:getYScroll() then
        self:setYScroll(0 - y + 40)
    elseif y + child:getHeight() + 40 > 0 - self:getYScroll() + self:getHeight() then
        self:setYScroll(0 - (y + child:getHeight() + 40 - self:getHeight()))
    end
end

function ISPanelJoypad:isFocusOnControl()
    local children = self:getVisibleChildren(self.joypadIndexY)
    local child = children[self.joypadIndex]
    if child and child.isCombobox and child.expanded then
        return true
    elseif child and child.isRadioButtons and child.joypadIndex > 1 then
        return true
    elseif child and child.isTickBox and child.joypadIndex > 1 then
        return true
    elseif child and child.isKnob then
        return true
    end
    return false
end

function ISPanelJoypad:onMouseUp(x, y)
    if not self.moveWithMouse then return; end
    if not self:getIsVisible() then
        return;
    end

    self.moving = false;
    if ISMouseDrag.tabPanel then
        ISMouseDrag.tabPanel:onMouseUp(x,y);
    end

    ISMouseDrag.dragView = nil;
end

function ISPanelJoypad:onMouseUpOutside(x, y)
    if not self.moveWithMouse then return; end
    if not self:getIsVisible() then
        return;
    end

    self.moving = false;
    ISMouseDrag.dragView = nil;
end

function ISPanelJoypad:onMouseDown(x, y)
    if not self.moveWithMouse then return; end
    if not self:getIsVisible() then
        return;
    end

    self.downX = x;
    self.downY = y;
    self.moving = true;
    self:bringToTop();
end

function ISPanelJoypad:onMouseMoveOutside(dx, dy)
    if not self.moveWithMouse then return; end
    self.mouseOver = false;

    if self.moving then
        self:setX(self.x + dx);
        self:setY(self.y + dy);
        self:bringToTop();
    end
end

function ISPanelJoypad:onMouseMove(dx, dy)
    if not self.moveWithMouse then return; end
    self.mouseOver = true;

    if self.moving then
        self:setX(self.x + dx);
        self:setY(self.y + dy);
        self:bringToTop();
        --ISMouseDrag.dragView = self;
    end
end

--************************************************************************--
--** ISPanelJoypad:render
--**
--************************************************************************--
function ISPanelJoypad:prerender()
	if self.background then
		self:drawRectStatic(0, 0, self.width, self.height, self.backgroundColor.a, self.backgroundColor.r, self.backgroundColor.g, self.backgroundColor.b);
		self:drawRectBorderStatic(0, 0, self.width, self.height, self.borderColor.a, self.borderColor.r, self.borderColor.g, self.borderColor.b);
	end
end
--************************************************************************--
--** ISPanelJoypad:new
--**
--************************************************************************--
function ISPanelJoypad:new (x, y, width, height)
	local o = {}
	--o.data = {}
	o = ISUIElement:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
	o.x = x;
	o.y = y;
	o.background = true;
	o.backgroundColor = {r=0, g=0, b=0, a=0.5};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
	o.height = height;
	o.anchorLeft = true;
	o.anchorRight = false;
	o.anchorTop = true;
	o.anchorBottom = false;
    o.joypadButtons = {};
    o.joypadIndex = 0;
    o.joypadButtonsY = {};
    o.joypadIndexY = 0;
    o.moveWithMouse = false;
   return o
end

