require "ISUI/ISCollapsableWindow"

---@class StreamMapWindow : ISCollapsableWindow
StreamMapWindow = ISCollapsableWindow:derive("StreamMapWindow");

function StreamMapWindow:onMouseDoubleClickOpenObject(item)


    if instanceof (item, "KahluaTableImpl") then

        local src = ObjectViewer:new(getCore():getScreenWidth() / 2, 0, 600, 300, item);

        src:initialise();
        src:addToUIManager();


     else

       local src = ObjectViewer:new(getCore():getScreenWidth() / 2, 0, 600, 300, item);

       src:initialise();
       src:addToUIManager();


    end
end

function StreamMapWindow:fillInfo()
    self.objectView:clear();
    self.objectView:setYScroll(0);

    if self.selectedSquare ~= nil then
        self.objectView:addItem("Grid "..self.selectedSquare:getX()..", "..self.selectedSquare:getY()..", "..self.selectedSquare:getZ(), self.selectedSquare);

        local ob = self.selectedSquare:getObjects();

        for i=0, ob:size() - 1 do
            local o =ob:get(i);
            self.objectView:addItem(o:getTile(), o);
        end
        ob = self.selectedSquare:getMovingObjects();

        for i=0, ob:size() - 1 do
            local o = ob:get(i);
            if instanceof(o, "IsoPlayer") then
                self.objectView:addItem("Player: ".. o:getDescriptor():getForename() .. " "..  o:getDescriptor():getSurname(), o);
            end
            if instanceof(o, "IsoSurvivor") then
                self.objectView:addItem("NPC: ".. o:getDescriptor():getForename() .. " "..  o:getDescriptor():getSurname(), o);
            end
        end
    end

end

function StreamMapWindow:initialise()

    ISCollapsableWindow.initialise(self);
    self.title = "Map Debugger";

end

function StreamMapWindow:onMapMouseDown(x, y)
    local cell = getCell();

    x = translatePointXInOverheadMapToWorld(x, self.javaObject,self.parent.zoom, self.parent.xpos);
    y = translatePointYInOverheadMapToWorld(y, self.javaObject,self.parent.zoom, self.parent.ypos);

    local sq = cell:getGridSquare(x, y, 0);

    self.parent.selectedSquare = sq;
    self.parent:fillInfo();

    return true;
end

function StreamMapWindow:onMapMouseMove(dx, dy)

    if self.panning then
        self.parent.xpos = self.parent.xpos - ((dx)/self.parent.zoom);
        self.parent.ypos = self.parent.ypos - ((dy)/self.parent.zoom);
    end

    return true;
end

function StreamMapWindow:onMapRightMouseDown(x, y)
    self.panning = true;
    return true;
end

function StreamMapWindow:onMapRightMouseUp(x, y)
    self.panning = false;
    return true;
end

function StreamMapWindow:onRenderMouseWheel(del)

    if(del > 0) then

        self.parent.zoom = self.parent.zoom* 0.8;
    else
        self.parent.zoom = self.parent.zoom* 1.2;

    end


    if self.parent.zoom > 30 then self.parent.zoom = 30 end
    return true;
end

function StreamMapWindow:createChildren()
    --print("instance");
    ISCollapsableWindow.createChildren(self);

    local th = self:titleBarHeight()
    
    self.renderPanel = ISPanel:new(200, th, self.width-200, self.height-th);
    self.renderPanel.render = StreamMapWindow.renderTex;
    self.renderPanel.tex = self.tex;
    self.renderPanel:initialise();

    self.renderPanel.onMouseDown = StreamMapWindow.onMapMouseDown;
    self.renderPanel.onMouseMove = StreamMapWindow.onMapMouseMove;
    self.renderPanel.onRightMouseDown = StreamMapWindow.onMapRightMouseDown;
    self.renderPanel.onRightMouseUp = StreamMapWindow.onMapRightMouseUp;
    self.renderPanel.onRightMouseUpOutside = StreamMapWindow.onMapRightMouseUp;
    self.renderPanel.onMouseWheel = StreamMapWindow.onRenderMouseWheel;
    self.renderPanel:setAnchorRight(true);
    self.renderPanel:setAnchorBottom(true);
    self.renderPanel.parent = self;
    self:addChild(self.renderPanel);

    self.objectView = ISScrollingListBox:new(0, th, 200, self.height-th);
    self.objectView:initialise();
    self.objectView.doDrawItem = StreamMapWindow.doDrawItem;
    self.objectView.anchorRight = true;
    self.objectView.anchorBottom = true;
    self:addChild(self.objectView);
    self.objectView.itemheight = 22;
    self.objectView:setOnMouseDoubleClick(self, StreamMapWindow.onMouseDoubleClickOpenObject);

end

function StreamMapWindow:doDrawItem(y, item, alt)
    if self.selected == item.index then
        self:drawRect(0, (y+3), self:getWidth(), self.itemheight-1, 0.2, 0.6, 0.7, 0.8);

    end

    --  self:drawRectBorder(0, (y), self:getWidth(), self.itemheight, 0.5, self.borderColor.r, self.borderColor.g, self.borderColor.b);
    self:drawText(item.text, 15, (y)+6, 1, 1, 1, 1, UIFont.Code);
    y = y + self.itemheight;
    return y;

end

function StreamMapWindow:renderTex()
    local x = translatePointXInOverheadMapToWorld(self:getMouseX(), self.javaObject, self.parent.zoom, self.parent.xpos)
    local y = translatePointYInOverheadMapToWorld(self:getMouseY(), self.javaObject, self.parent.zoom, self.parent.ypos)
    self.parent.title = "Map Debugger " .. round(x,0) .. ":" .. round(y,0);
    
    self:setStencilRect(0,0,self:getWidth(), self:getHeight());

    drawOverheadMap(self.javaObject,self.parent.zoom, self.parent.xpos, self.parent.ypos);

    if self.parent.selectedSquare ~= nil then
        local x = translatePointXInOverheadMapToWindow(self.parent.selectedSquare:getX(), self.javaObject,self.parent.zoom, self.parent.xpos);
        local y = translatePointYInOverheadMapToWindow(self.parent.selectedSquare:getY(), self.javaObject,self.parent.zoom, self.parent.ypos);
        local x2 = translatePointXInOverheadMapToWindow(self.parent.selectedSquare:getX()+1, self.javaObject,self.parent.zoom, self.parent.xpos);
        local y2 = translatePointYInOverheadMapToWindow(self.parent.selectedSquare:getY()+1, self.javaObject,self.parent.zoom, self.parent.ypos);

        self:drawRect(x, y, x2-x, y2-y, 0.7, 1, 0.5, 0.3);
    end

    if isKeyDown(Keyboard.KEY_F) then
        local x = translatePointXInOverheadMapToWorld(self:getMouseX(), self.javaObject, self.parent.zoom, self.parent.xpos)
        local y = translatePointYInOverheadMapToWorld(self:getMouseY(), self.javaObject, self.parent.zoom, self.parent.ypos)
        debugFullyStreamedIn(x, y)
    end

    self:clearStencilRect();

    if isKeyDown(Keyboard.KEY_T) then
        local x = translatePointXInOverheadMapToWorld(self:getMouseX(), self.javaObject, self.parent.zoom, self.parent.xpos)
        local y = translatePointYInOverheadMapToWorld(self:getMouseY(), self.javaObject, self.parent.zoom, self.parent.ypos)
        local p = getSpecificPlayer(0)
        p:setX(x + 0.5)
        p:setY(y + 0.5)
        p:setLx(p:getX())
        p:setLy(p:getY())
    end
end

function StreamMapWindow:new (x, y, width, height)
    local o = {}
    --o.data = {}
    o = ISCollapsableWindow:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    local cell = getCell();

    o.xpos = getSpecificPlayer(0):getX();
    o.ypos =  getSpecificPlayer(0):getY();
    o.zoom = 1;
    return o
end

