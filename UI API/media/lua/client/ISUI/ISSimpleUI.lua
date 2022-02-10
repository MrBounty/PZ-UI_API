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

function ISSimpleUI:new()
    local o = {};
    o = ISCollapsableWindow:new(0, 0, 1, 1);
    setmetatable(o, self);
    self.__index = self;

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
    o.lineColumnCount = {}; -- Number of columns in a line
    o.columnAct = 1; -- Actual columns of the UI
    o.yAct = o:titleBarHeight() + 1; -- Actual position
    table.insert(o.lineY, o.yAct);
    table.insert(o.lineColumnCount, 0);

    -- ISCollapsableWindow stuff
    o.resizable = false;
    o.drawFrame = true;
    return o;
end

function ISSimpleUI:open()
end

function ISSimpleUI:close()
end

function ISSimpleUI:toogle()
end

function ISSimpleUI:nextLine()
end

function ISSimpleUI:saveLayout()
end

function ISSimpleUI:addText(name, text, font)
end

function ISSimpleUI:addRichText(name, text, font)
end

function ISSimpleUI:addButton(name, text, func)
    self.lineColumnCount[self.lineAct] = self.lineColumnCount[self.lineAct] + 1;
    local newE = ISSimpleButton:new(self)
    newE:setTitle(text);
    newE:setOnClick(func);
    if name == "" then
        table.insert(self.noNameElements, newE);
    else
        self.namedElements[name] = newE;
    end
end

function ISSimpleUI:addTickBox(name)
end

function ISSimpleUI:addEntry(name, text, isNumber)
end

function ISSimpleUI:addComboBox(name, items)
end

function ISSimpleUI:addScrollList(name, items)
end

function ISSimpleUI:addImage(name, path, width)
end