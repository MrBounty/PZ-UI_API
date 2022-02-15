require "ISUI/ISCollapsableWindow"

LuaFileBrowser = ISCollapsableWindow:derive("LuaFileBrowser");

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)

-----

LuaFileBrowserList = ISScrollingListBox:derive("LuaFileBrowserList")

-----

function LuaFileBrowser:initialise()

    ISCollapsableWindow.initialise(self);

    self.title = "Lua Files";
end


function LuaFileBrowser:onMouseDoubleClickFile(item)


    local f = item;
    if f ~= nil then
        local src = nil;
        if SourceWindow.map[f] ~= nil then
            src =SourceWindow.map[f];
            src:removeFromUIManager();
            src:addToUIManager();
            src:setVisible(true);
        else

            src = SourceWindow:new(getCore():getScreenWidth() / 2, 0, 600, 600, f);
            SourceWindow.map[f] = src;
            src:initialise();
            src:addToUIManager();


        end

    end
end

function LuaFileBrowser:update()
    local text = string.trim(self.textEntry:getInternalText());

    if text ~= self.lastText then
       self:fill();
       self.lastText = text;
    end

    self:updateReloadButton();
end

function LuaFileBrowserList:doDrawItem(y, item, alt)
    if y + self:getYScroll() >= self.height then return y + item.height end
    if y + item.height + self:getYScroll() <= 0 then return y + item.height end

    if self.selected == item.index then
        self:drawRect(0, (y), self:getWidth(), self.itemheight-1, 0.3, 0.7, 0.35, 0.15);

    end
    self:drawRectBorder(0, (y), self:getWidth(), self.itemheight, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    
    self:drawText(item.text, 15, y + (item.height - self.fontHgt) / 2, 0.9, 0.9, 0.9, 0.9, UIFont.Small);
    y = y + self.itemheight;
    return y;

end

function LuaFileBrowser:fill()
    self.fileList:clear();
    local c = getLoadedLuaCount();

    for i = 0, c-1 do
        local path = getLoadedLua(i);
        local name =getShortenedFilename(path);
        if string.trim(self.textEntry:getInternalText()) == nil or string.contains(string.lower(name), string.lower(string.trim(self.textEntry:getInternalText()))) then
            self.fileList:addItem(name, path);
        end

    end
end

function LuaFileBrowserList:onMouseWheel(del)
    self:setYScroll(self:getYScroll() - (del*self.itemheight*6));
    return true;
end

function LuaFileBrowser:updateReloadButton()
    local x,y = self.fileList:getMouseX(), self.fileList:getMouseY()
    local row = self.fileList:isMouseOver() and self.fileList:rowAt(x, y) or -1
    if row == self.buttonReloadRow then return end
    if row == -1 then
        self.buttonReload:setVisible(false)
    else
        self.buttonReload:setVisible(true)
        self.buttonReload:setX(self.width - self.buttonReload.width - 30)
        local itemY = self.fileList:topOfItem(row)
        self.buttonReload:setY(itemY + self.fileList:getYScroll())
    end
    self.buttonReloadRow = row
end

function LuaFileBrowser:onButtonReload()
    if self.buttonReloadRow == -1 then return end
    local item = self.fileList.items[self.buttonReloadRow]
    if not item then return end
    reloadLuaFile(item.item)
end

function LuaFileBrowser:createChildren()
    --print("instance");
    ISCollapsableWindow.createChildren(self);

    local th = self:titleBarHeight()
    local rh = self:resizeWidgetHeight()

    local entryHgt = FONT_HGT_SMALL + 2 * 2

    self.fileList = LuaFileBrowserList:new(0, th + entryHgt, self.width, self.height-rh-entryHgt-th);
    self.fileList.anchorRight = true;
    self.fileList.anchorBottom = true;
    self.fileList:initialise();
    self.fileList:setOnMouseDoubleClick(self, LuaFileBrowser.onMouseDoubleClickFile);
    self.fileList:setFont(UIFont.Small, 3)
    self:addChild(self.fileList);

    self.textEntry = ISTextEntryBox:new("", 0, th, self.width, entryHgt);
    self.textEntry:initialise();
    self.textEntry:instantiate();
    self.textEntry:setClearButton(true)
    self.textEntry:setText("");
    self:addChild(self.textEntry);
    self.lastText = self.textEntry:getInternalText();

    self:fill();

    local button = ISButton:new(0, 0, 50, self.fileList.itemheight, "RELOAD", self, self.onButtonReload)
    button:initialise()
    self.fileList:addChild(button)
    self.fileList.doRepaintStencil = true
    button:setVisible(false)
    self.buttonReload = button

--[[
    -- Do corner x + y widget
    local resizeWidget = ISResizeWidget:new(self.width-10, self.height-10, 10, 10, self);
    resizeWidget:initialise();
    self:addChild(resizeWidget);

    self.resizeWidget = resizeWidget;

    -- Do bottom y widget
    resizeWidget = ISResizeWidget:new(0, self.height-10, self.width-10, 10, self, true);
    resizeWidget.anchorRight = true;
    resizeWidget:initialise();
    self:addChild(resizeWidget);

    self.resizeWidget2 = resizeWidget;
--]]
end


function LuaFileBrowser:new (x, y, width, height)
    local o = {}
    --o.data = {}
    o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    return o
end

