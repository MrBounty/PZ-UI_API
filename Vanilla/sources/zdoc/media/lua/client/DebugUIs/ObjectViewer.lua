require "ISUI/ISCollapsableWindow"

---@class ObjectViewer : ISCollapsableWindow
ObjectViewer = ISCollapsableWindow:derive("ObjectViewer");


function ObjectViewer:onRightMouseDownObject(x, y)
    if instanceof(self.parent.obj, "KahluaTableImpl") then


        local context = getDebuggerContextMenu()
        context:hideAndChildren();
        context:setVisible(true);
        context:clear();
        context.forceVisible = true;
        context.parent = nil;
        context:setX(x);
        context:setY(y);
        context:bringToTop();
        context:setVisible(true);
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
        context:addOption("Watch", { obj = self.parent.obj, item = sel.item}, ObjectViewer.onWatch);
        if hasDataBreakpoint(self.parent.obj, sel.item.key) then
            context:addOption("Remove break on change", {obj = self.parent.obj, item = sel.item}, ObjectViewer.onDataWrite);
        else
            context:addOption("Break on change", {obj = self.parent.obj, item = sel.item}, ObjectViewer.onDataWrite);
        end
        if hasDataReadBreakpoint(self.parent.obj, sel.item.key) then
            context:addOption("Remove break on read", {obj = self.parent.obj, item = sel.item}, ObjectViewer.onDataRead);
        else
            context:addOption("Break on read", {obj = self.parent.obj, item = sel.item}, ObjectViewer.onDataRead);
        end
    end
end



function ObjectViewer.onWatch(item)

    WatchWindowInstance.objlist:add(item);
    WatchWindowInstance:fill();
end

function ObjectViewer.onDataWrite(data)
toggleBreakOnChange(data.obj, data.item.key);
end
function ObjectViewer.onDataRead(data)
    toggleBreakOnRead(data.obj, data.item.key);
end

function ObjectViewer:onMouseDoubleClickOpenObject(item)

    local bReuse = true;
    -- hold lshift to not reuse.
    if isKeyDown(42) then
        bReuse = false;
     end
    if instanceof (item, "KahluaTableImpl") then
        item = item.val;

        if not bReuse then
            local src = ObjectViewer:new(getCore():getScreenWidth() / 2, 0, 600, 300, item);

            src:initialise();
            src:addToUIManager();

        else
            self.obj = item;
            self.title =  KahluaUtil.rawTostring2(self.obj);
            self:fill();
        end

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

        if not bReuse then
            local src = ObjectViewer:new(getCore():getScreenWidth() / 2, 0, 600, 300, item);

            src:initialise();
            src:addToUIManager();
        else
            self.obj = item;
            self.title =  KahluaUtil.rawTostring2(self.obj);
            self:fill();
        end




    end
end

function ObjectViewer:storePos()
    self.sc = self.objectView:getYScroll();
end

function ObjectViewer:restorePos()
    self.objectView:setYScroll(self.sc);
end

ObjectViewer.map = {}

function ObjectViewer:initialise()

    ISCollapsableWindow.initialise(self);

    self.title = KahluaUtil.rawTostring2(self.obj);
end

function ObjectViewer:onSourceMouseWheel(del)
    self:setYScroll(self:getYScroll() - (del*18*6));
    return true;
end

function ObjectViewer:fill()
    self.objectView:clear();
    self.objectView:setYScroll(0);
    local bSort = true;

    if instanceof(self.obj, "KahluaTableImpl") then
         for k, v in pairs(self.obj) do
             local s = KahluaUtil.rawTostring2(k);
             local s2 = KahluaUtil.rawTostring2(v);
             if s ~= nil and s2 ~= nil then
                 s = tabToX(s, 40);
                 self.objectView:addItem(s..s2, {key=k, val=v});
             end

         end
    elseif self.obj then
        bSort = false;

        local c = getNumClassFields(self.obj);
        for i=0, c-1 do
            local meth = getClassField(self.obj, i);
if meth.getType then -- is it exposed?
            local val = KahluaUtil.rawTostring2(getClassFieldVal(self.obj, meth));
           if(val == nil) then val = "nil" end
            local s = tabToX(meth:getType():getSimpleName(), 18) .. " " .. tabToX(meth:getName(), 24) .. " " .. tabToX(val, 24);
            self.objectView:addItem(s, meth);
else
        s = type(meth)..' = '..tostring(meth)
end
        end

        c = getNumClassFunctions(self.obj);
        for i=0, c-1 do
            local meth = getClassFunction(self.obj, i);
if meth.getReturnType and meth:getReturnType().getSimpleName then -- is it exposed?
            local paramNum = getMethodParameterCount(meth);
            local params = "";
            for j=0, paramNum - 1 do
                params = params .. getMethodParameter(meth, j);
                if j < paramNum - 2 then
                    params = params .. ", ";
                end
            end
            local s = tabToX(meth:getReturnType():getSimpleName(), 18) .. " " .. tabToX(meth:getName(), 24) .. "( " .. params .. " )";
            s = tabToX(s, 40);
            self.objectView:addItem(s, meth);
else
        s = type(meth)..' = '..tostring(meth)
end
        end

    end
    if bSort then
        self.objectView:sort();
     end
end

function ObjectViewer:createChildren()
    --print("instance");
    ISCollapsableWindow.createChildren(self);

    local th = self:titleBarHeight()
    local rh = self:resizeWidgetHeight()

    self.objectView = ISScrollingListBox:new(0, th, self.width, self.height-th-rh);
    self.objectView:initialise();
    self.objectView.doDrawItem = ObjectViewer.doDrawItem;
    self.objectView.onMouseWheel = ObjectViewer.onSourceMouseWheel;
    self.objectView.anchorRight = true;
    self.objectView.onRightMouseDown = ObjectViewer.onRightMouseDownObject;
    self.objectView.anchorBottom = true;
    self.objectView.itemheight = 22;
    self.objectView:setOnMouseDoubleClick(self, ObjectViewer.onMouseDoubleClickOpenObject);
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

function ObjectViewer:doDrawItem(y, item, alt)
    if self.selected == item.index then
        self:drawRect(0, (y+3), self:getWidth(), self.itemheight-1, 0.2, 0.6, 0.7, 0.8);

    end

    if item.item.key ~= nil and hasDataBreakpoint(self.parent.obj, item.item.key) then
        self:drawRect(0, (y+3), self:getWidth(), self.itemheight-1, 0.3, 0.8, 0.6, 0.4);
    end
    if item.item.key ~= nil and hasDataReadBreakpoint(self.parent.obj, item.item.key) then
        self:drawRect(0, (y+3), self:getWidth(), self.itemheight-1, 0.3, 0.6, 0.8, 0.4);
    end

    --  self:drawRectBorder(0, (y), self:getWidth(), self.itemheight, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(item.text, 15, (y)+6, 1, 1, 1, 1, UIFont.Code);
    y = y + self.itemheight;
    return y;

end

function ObjectViewer:new (x, y, width, height, obj)

    local o = {}

    --o.data = {}
    o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    o.height = getCore():getScreenHeight()/3;
    o.width = (getCore():getScreenWidth()-700)/2;
    o.x = o.width;
    o.y = getCore():getScreenHeight() - (getCore():getScreenHeight()/3);
    o.obj = obj;
    ObjectViewer.map[obj] = o;
    return o
end
