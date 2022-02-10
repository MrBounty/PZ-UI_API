require "ISUI/ISCollapsableWindow"

ISSimpleUI = ISCollapsableWindow:derive("ISSimpleUI");

function ISSimpleUI:initialise()
    ISCollapsableWindow.initialise(self);
end

function ISSimpleUI:render()
    ISCollapsableWindow.render(self);
end

function ISSimpleUI:create()
end

function ISSimpleUI:setPosition(pctX, pctY)
    self.pctX = pctX;
    self.pctY = pctY;
    self.pxlX = pctX * getCore():getScreenWidht();
    self.pxlY = pctY * getCore():getScreenpctHeight();
    self:setX(self.pxlX);
    self:setY(self.pxlY);
end

function ISSimpleUI:setWidth(pctW)
    self.pctW = pctW;
    self.pxlW = pctW * getCore():getScreenWidht();
    self:setElementsPositionAndSize();
end

function ISSimpleUI:setElementsPositionAndSize(pctW)
    for index, value in ipairs(self.noNameElements) do
        value:setPositionAndSize()
    end
    for index, value in pairs(self.namedElements) do
        value:setPositionAndSize()
    end
end

function ISSimpleUI:new(pctX, pctY, pctW)
    local x = getCore():getScreenWidht() * pctX
    local y = getCore():getScreenHeight() * pctY
    local w = getCore():getScreenWidht() * pctW
    local o = {};
    o = ISCollapsableWindow:new(x, y, w, 1);
    setmetatable(o, self);
    self.__index = self;

    o:setHeight(o:titleBarHeight());

    -- Offset
    o.dx = 5;
    o.dy = 5;

    -- Position
    o.pctX = 20;
    o.pctY = 20;
    o.pxlX = o.pctX * getCore():getScreenWidht();
    o.pxlY = o.pctY * getCore():getScreenpctHeight();

    --Size
    o.pctW = 20;
    o.pxlW = o.pctW * getCore():getScreenWidht();

    -- My stuff
    o.noNameElements = {}; -- List of elements with no name
    o.namedElements = {}; -- List of elements with name
    o.lineAct = 1; -- Actual line of the UI
    o.lineY = {}; -- y position for each line
    o.lineH = {}; -- height for each line
    o.lineColumnCount = {}; -- Number of columns in a line
    o.columnAct = 0; -- Actual columns of the UI
    o.yAct = o:titleBarHeight() + 1; -- Actual position
    o.deltaY = 0;
    table.insert(o.lineY, o.yAct);
    table.insert(o.lineColumnCount, 0);

    -- ISCollapsableWindow stuff
    o.resizable = false;
    o.drawFrame = true;
    o.char = getPlayer()
    o.playerNum = car:getPlayerNum()
    return o;
end

function ISSimpleUI:open()
end

function ISSimpleUI:close()
end

function ISSimpleUI:toogle()
end

function ISSimpleUI:nextLine()
    self. o.columnAct = 1;
    self.yAct = self.yAct + self.deltaY;
    table.insert(self.lineH, self.dataY);
    self.dataY = 0
    table.insert(self.lineY, self.yAct);
    table.insert(self.lineColumnCount, 0);
end

function ISSimpleUI:saveLayout()
end

function ISSimpleUI:addText(name, txt, font)
     -- Add a column
    self.lineColumnCount[self.lineAct] = self.lineColumnCount[self.lineAct] + 1;
    self.columnAct = self.columnAct + 1;

     -- Create element
    local newE = ISSimpleText:new(self, txt, font)
    if name == "" then
        table.insert(self.noNameElements, newE);
    else
        self.namedElements[name] = newE;
    end

    -- Add to yAct
    local deltaY = getTextManager():getFontHeight(UIFont[font]) + self.dy;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addRichText(name, txt, font)
end

function ISSimpleUI:addButton(name, txt, func)
    self.lineColumnCount[self.lineAct] = self.lineColumnCount[self.lineAct] + 1;
    local newE = ISSimpleButton:new(self)
    newE:setTitle(txt);
    newE:setOnClick(func);
    if name == "" then
        table.insert(self.noNameElements, newE);
    else
        self.namedElements[name] = newE;
    end
end

function ISSimpleUI:addTickBox(name)
end

function ISSimpleUI:addEntry(name, txt, isNumber)
end

function ISSimpleUI:addComboBox(name, items)
end

function ISSimpleUI:addScrollList(name, items)
end

function ISSimpleUI:addImage(name, path, width)
end