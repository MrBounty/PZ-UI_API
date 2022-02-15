require "ISUI/ISCollapsableWindow"

local UI_HEIGHT_SMALL = getTextManager():getFontHeight(UIFont.Small);

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
    self.pxlX = pctX * getCore():getScreenWidth();
    self.pxlY = pctY * getCore():getScreenHeight();
    self:setX(self.pxlX);
    self:setY(self.pxlY);
end

function ISSimpleUI:setWidth(pctW)
    self.pctW = pctW;
    self.pxlW = pctW * getCore():getScreenWidth();
    self:setWidth(self.pxlW);
end

function ISSimpleUI:setElementsPositionAndSize()
    for index, value in ipairs(self.noNameElements) do
        value:setPositionAndSize()
    end
    for index, value in pairs(self.namedElements) do
        value:setPositionAndSize()
    end
end

function ISSimpleUI:addBorderToAllElements()
    for index, value in ipairs(self.noNameElements) do
        value:addBorder()
    end
    for index, value in pairs(self.namedElements) do
        value:addBorder()
    end
end

function ISSimpleUI:new(pctX, pctY, pctW)
    local x = getCore():getScreenWidth() * pctX;
    local y = getCore():getScreenHeight() * pctY;
    local w = getCore():getScreenWidth() * pctW;
    local o = {};
    o = ISCollapsableWindow:new(x, y, w, 1);
    setmetatable(o, self);
    self.__index = self;

    o:setHeight(o:titleBarHeight());

    -- Position
    o.pctX = pctX;
    o.pctY = pctY;
    o.pxlX = o.pctX * getCore():getScreenWidth();
    o.pxlY = o.pctY * getCore():getScreenHeight();

    --Size
    o.pctW = pctW;
    o.pxlW = o.pctW * getCore():getScreenWidth();

    -- My stuff
    o.noNameElements = {}; -- List of elements with no name
    o.namedElements = {}; -- List of elements with name
    o.lineAct = 1; -- Actual line of the UI
    o.lineY = {}; -- y position for each line
    o.lineH = {}; -- height for each line
    o.lineColumnCount = {}; -- Number of columns in a line
    o.columnAct = 0; -- Actual columns of the UI
    o.yAct = o:titleBarHeight(); -- Actual position
    o.deltaY = 0;
    o.isUIVisible = true;
    table.insert(o.lineY, o.yAct);
    table.insert(o.lineColumnCount, 0);

    -- ISCollapsableWindow stuff
    o.resizable = false;
    o.drawFrame = true;
    return o;
end

function ISSimpleUI:open()
    if not self.isUIVisible then
        self:setVisible(true);
        self.isUIVisible = true;
    end
end

function ISSimpleUI:close()
    if self.isUIVisible then
        self:setVisible(false);
        self.isUIVisible = false;
    end
end

function ISSimpleUI:toggle()
    if self.isUIVisible then
        self:setVisible(false);
        self.isUIVisible = false;
    else
        self:setVisible(true);
        self.isUIVisible = true;
    end
end

function ISSimpleUI:nextLine()
    self.columnAct = 0;
    self.lineAct = self.lineAct + 1;
    self.yAct = self.yAct + self.deltaY;
    table.insert(self.lineH, self.deltaY);
    self.deltaY = 0;
    table.insert(self.lineY, self.yAct);
    table.insert(self.lineColumnCount, 0);
end

function ISSimpleUI:initAndAddToTable(newE, name)
    newE:initialise();
    newE:instantiate();
    self:addChild(newE);

    if name and self[name] ~= nil then 
        print("UI API - ERROR: element name '" .. name .. "' is already a variable name. Change the name !")
    end

    if name == "" or not name then
        table.insert(self.noNameElements, newE);
    else
        self.namedElements[name] = newE;
        self[name] = newE;
    end
end

function ISSimpleUI:setLineHeight(pctH)
    self.deltaY = h * getCore():getScreenHeight();
end

function ISSimpleUI:nextColumn()
     -- Add a column
     self.lineColumnCount[self.lineAct] = self.lineColumnCount[self.lineAct] + 1;
     self.columnAct = self.columnAct + 1;
end

function ISSimpleUI:saveLayout()
    self:nextLine();
    self:setHeight(self.yAct);
    self:setElementsPositionAndSize();

    self:addToUIManager();
end

function ISSimpleUI:addText(name, txt, font, position)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleText:new(self, txt, font, position);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = getTextManager():getFontHeight(newE.font);
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addRichText(name, text)
    self:nextColumn();
    
    -- Create element
    local newE = ISSimpleRichText:new(self, text);
    self:initAndAddToTable(newE, name);

   -- Add to yAct
   local deltaY = UI_HEIGHT_SMALL * 8;
   if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addButton(name, text, func)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleButton:new(self, text, func);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = getTextManager():getFontHeight(newE.font);
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addTickBox(name)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleTickBox:new(self);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = UI_HEIGHT_SMALL;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addEntry(name, text, isNumber)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleEntry:new(self, text, isNumber);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = UI_HEIGHT_SMALL;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addComboBox(name, items)
end

function ISSimpleUI:addScrollList(name, items)
end

function ISSimpleUI:addImage(name, path, width)
end

function ISSimpleUI:addImageButton(name, path, width, func)
end

function ISSimpleUI:addEmpty(width)
end