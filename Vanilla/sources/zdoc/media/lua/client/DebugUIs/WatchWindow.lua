require "ISUI/ISCollapsableWindow"

---@class WatchWindow : ISCollapsableWindow
WatchWindow = ISCollapsableWindow:derive("WatchWindow");


function WatchWindow:onRightMouseDownObject(x, y)
    if instanceof(self.parent.obj, "KahluaTableImpl") then

        if #self.items == 0 then
            return
        end
        
        local context = getDebuggerContextMenu()
        context:hideAndChildren();
        context:setVisible(true);
        context:clear();
        context.forceVisible = true;
        context.parent = nil;
        context:setX(x);
        context:setY(y);
        context:bringToTop();
        context.visibleCheck = true;
        context.instanceMap = {}
        context.subOptionNums = 0;
        context.subInstance = {}
        context.player = 0;
        context:setX(getMouseX())
        context:setY(getMouseY())

        y = math.floor((y / self.itemheight));

        y = y + 1;

        if y > #self.items then
            y = #self.items;
        end
        if y < 1 then
            y = 1;
        end

        -- RJ: If you select the same item it unselect it
        --if self.selected == y then
        --if self.selected == y then
        --self.selected = -1;
        --return;
        --end

        self.selected = y;

        local sel = self.items[self.selected];
        if hasDataBreakpoint(sel.item.obj, sel.item.key) then
            context:addOption("Remove break on change", {obj = sel.item.obj, item = sel.item}, WatchWindow.onDataWrite);
        else
            context:addOption("Break on change", {obj = sel.item.obj, item = sel.item}, WatchWindow.onDataWrite);
        end
        if hasDataReadBreakpoint(sel.item.obj, sel.item.key) then
            context:addOption("Remove break on read", {obj = sel.item.obj, item = sel.item}, WatchWindow.onDataRead);
        else
            context:addOption("Break on read", {obj = sel.item.obj, item = sel.item}, WatchWindow.onDataRead);
        end
    end
end
function WatchWindow.onDataWrite(data)
    toggleBreakOnChange(data.obj, data.item.key);
end
function WatchWindow.onDataRead(data)
    toggleBreakOnRead(data.obj, data.item.key);
end

function WatchWindow:onMouseDoubleClickOpenObject(item)
    if instanceof (item, "KahluaTableImpl") then
        if item.obj ~= nil then
            item = item.obj;
        end
    end

    if item == nil then return; end

        local bReuse = true;
    -- hold lshift to not reuse.
        bReuse = false;

    if instanceof (item, "KahluaTableImpl") then

             local src = ObjectViewer:new(getCore():getScreenWidth() / 2, 0, 600, 300, item);

            src:initialise();
            src:addToUIManager();


    elseif instanceof (item, "Class") then

    elseif instanceof (item, "Field") then
        item = getClassFieldVal(self.obj, item);
        self:onMouseDoubleClickOpenObject(item);

    elseif instanceof (item, "Array") then
        item = getClassFieldVal(self.obj, item);
        self:onMouseDoubleClickOpenObject(item);
    elseif instanceof (item, "Texture") then

        local src = TextureWindow:new(getCore():getScreenWidth() / 2, 0, item:getWidth(), item:getHeight(), item);

        src:initialise();
        src:addToUIManager();

    elseif instanceof (item, "LuaClosure") then

        local f = getFilenameOfClosure(item);
        if f ~= nil then
            local src = nil;
            if SourceWindow.map[f] ~= nil then
                src =SourceWindow.map[f];
                src:setVisible(true);
                src:removeFromUIManager();
                src:addToUIManager();
            else

                src = SourceWindow:new(getCore():getScreenWidth() / 2, 0, 600, 600, f);
                SourceWindow.map[f] = src;
                src:initialise();
                src:addToUIManager();
            end
            local p = (getFirstLineOfClosure(item)-1) * 20;
            p = p - (src:getWidth() / 2);
            src.sourceView:setScrollHeight(src.sourceView.count * 20);
            src.sourceView:setYScroll(-p);
            src.sourceView.selected = getFirstLineOfClosure(item)-1;

        end

    else


        local src = ObjectViewer:new(getCore():getScreenWidth() / 2, 0, 600, 300, item);

        src:initialise();
        src:addToUIManager();





    end
end

function WatchWindow:storePos()
    self.sc = self.objectView:getYScroll();
end

function WatchWindow:restorePos()
    self.objectView:setYScroll(self.sc);
end



function WatchWindow:initialise()

    ISCollapsableWindow.initialise(self);

    self.title = "Watch Window";
end

function WatchWindow:onSourceMouseWheel(del)
    self:setYScroll(self:getYScroll() - (del*18*6));
    return true;
end

function WatchWindow:fill()
    self.objectView:clear();
    self.objectView:setYScroll(0);
    local bSort = true;
    if instanceof(self.obj, "KahluaTableImpl") then
        for k, v in ipairs(self.obj) do

            local s = KahluaUtil.rawTostring2(v.item.key);
            local s2 = KahluaUtil.rawTostring2(v.item.val);
            if s ~= nil and s2 ~= nil then
                s = tabToX(s, 40);
                self.objectView:addItem(s..s2, {obj = v.obj, key = v.item.key, item = v.item.val});
            end
        end
    end
    if bSort then
        self.objectView:sort();
    end
end

function WatchWindow:createChildren()
    --print("instance");
    ISCollapsableWindow.createChildren(self);

    local th = self:titleBarHeight()
    local rh = self:resizeWidgetHeight()

    self.objectView = ISScrollingListBox:new(0, th, self.width, self.height-th-rh);
    self.objectView:initialise();
    self.objectView.doDrawItem = WatchWindow.doDrawItem;
    self.objectView.onMouseWheel = WatchWindow.onSourceMouseWheel;
    self.objectView.anchorRight = true;
    self.objectView.onRightMouseDown = WatchWindow.onRightMouseDownObject;
    self.objectView.anchorBottom = true;
    self.objectView.itemheight = 22;
    self.objectView:setOnMouseDoubleClick(self, WatchWindow.onMouseDoubleClickOpenObject);
    self:addChild(self.objectView);

    self:fill();

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

function WatchWindow:doDrawItem(y, item, alt)
    if self.selected == item.index then
        self:drawRect(0, (y+3), self:getWidth(), self.itemheight-1, 0.2, 0.6, 0.7, 0.8);

    end

    if item.item.key ~= nil and hasDataBreakpoint(item.item.obj, item.item.key) then
        self:drawRect(0, (y+3), self:getWidth(), self.itemheight-1, 0.3, 0.8, 0.6, 0.4);
    end
    if item.item.key ~= nil and hasDataReadBreakpoint(item.item.obj, item.item.key) then
        self:drawRect(0, (y+3), self:getWidth(), self.itemheight-1, 0.3, 0.6, 0.8, 0.4);
    end

    --  self:drawRectBorder(0, (y), self:getWidth(), self.itemheight, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(item.text, 15, (y)+6, 1, 1, 1, 1, UIFont.Code);
    y = y + self.itemheight;
    return y;

end

function WatchWindow:addWatch(obj)

end

function WatchWindow:new (x, y, width, height)

    local o = {}

    --o.data = {}
    o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    o.height = getCore():getScreenHeight()/3;
    o.width = (getCore():getScreenWidth()-700)/2;
    o.x = 0;

    o.y = getCore():getScreenHeight() - (getCore():getScreenHeight()/3);
    o.obj = LuaList:new();
    o.objlist = o.obj;
    o.obj = o.obj.items;
    WatchWindowInstance = o;
    return o
end
