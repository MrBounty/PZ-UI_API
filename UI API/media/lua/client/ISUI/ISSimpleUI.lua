require "ISUI/ISCollapsableWindow"

local UI_HEIGHT_SMALL = getTextManager():getFontHeight(UIFont.Small);

ISSimpleUI = ISCollapsableWindow:derive("ISSimpleUI");

function ISSimpleUI:initialise()
    ISCollapsableWindow.initialise(self);
end

function ISSimpleUI:render()
    ISCollapsableWindow.render(self);

    if self.isSubUI and not self.parentUI.isUIVisible then
        self:close();
    end
end

function ISSimpleUI:setElementsPositionAndSize()
    self.elemW = {};
    self.elemX = {};

    -- Make matrice
    for i=1,self.lineAct do -- For every line
        local nbElemWidthForce = 0;
        local widthLeft = self.pxlW;
        self.elemW[i] = {};
        self.elemX[i] = {};
        local nbElement = self.lineColumnCount[i]; -- Number of element in the line
        local w = math.floor(self.pxlW / nbElement); -- Size of element

        for j=1,self.lineColumnCount[i] do -- Check if an element got this width force
            if self.matriceLayout[i][j].isWidthForce then 
                nbElemWidthForce = nbElemWidthForce + 1;
                widthLeft = widthLeft - self.matriceLayout[i][j].pxlW;
            end
        end

        for j=1,self.lineColumnCount[i] do -- For every column in line
            if nbElemWidthForce ~= 0 then
                if widthLeft < 1 then
                    print("UI API - ERROR : At line "..i.." of the UI. Width set to element wider that the window width.");
                end
                local nbElementLeft = nbElement - nbElemWidthForce;
                local w = math.floor(widthLeft / nbElementLeft); -- Size of element
                local elem = self.matriceLayout[i][j];
                if elem.isWidthForce then 
                    self.elemW[i][j] = elem.pxlW; -- set size
                else
                    self.elemW[i][j] = w; -- set size
                end
                if j == 1 then 
                    self.elemX[i][j] = 0; -- set position
                else
                    self.elemX[i][j] = self.elemX[i][j-1] + self.elemW[i][j-1]; -- set position
                end
            else
                self.elemW[i][j] = w; -- set size
                self.elemX[i][j] = w * (j-1); -- set position
            end
        end
        self.elemW[i][self.lineColumnCount[i]] = self.pxlW - self.elemX[i][self.lineColumnCount[i]]; -- Set size to last element to the border in case pixel is lost with math.floor
    end

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
    o.elemY = {}; -- y position for each line
    o.elemH = {}; -- height for each line
    o.lineColumnCount = {}; -- Number of columns in a line
    o.columnAct = 0; -- Actual columns of the UI
    o.yAct = o:titleBarHeight(); -- Actual position
    o.deltaY = 0;
    o.isUIVisible = true;
    o.matriceLayout = {} -- Matrice of the layout, like that matriceLayout[line][column]
    table.insert(o.elemY, o.yAct);
    table.insert(o.lineColumnCount, 0);

    -- ISCollapsableWindow stuff
    o.resizable = false;
    o.drawFrame = true;
    return o;
end


-- Toggle

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


-- Line and column

function ISSimpleUI:nextLine()
    self.columnAct = 0;
    self.lineAct = self.lineAct + 1;
    self.yAct = self.yAct + self.deltaY;
    table.insert(self.elemH, self.deltaY);
    self.deltaY = 0;
    table.insert(self.elemY, self.yAct);
    table.insert(self.lineColumnCount, 0);
end

function ISSimpleUI:initAndAddToTable(newE, name)
    newE:initialise();
    newE:instantiate();
    self:addChild(newE);

    if name and self[name] ~= nil then 
        print("UI API - ERROR : element name '" .. name .. "' is already a variable name. Change it !")
    end

    if name == "" or not name then
        table.insert(self.noNameElements, newE);
    else
        self.namedElements[name] = newE;
        self[name] = newE;
    end

    if not self.matriceLayout[self.lineAct] then self.matriceLayout[self.lineAct] = {} end
    self.matriceLayout[self.lineAct][self.columnAct] = newE;
end

function ISSimpleUI:setLineHeightPercent(pctH)
    self.deltaY = pctH * getCore():getScreenHeight();
end

function ISSimpleUI:setLineHeightPixel(pxlH)
    self.deltaY = pxlH;
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

function ISSimpleUI:isSubUIOf(parent)
    self.isSubUI = true;
    self.parentUI = parent;
end


-- Elements

function ISSimpleUI:addEmpty(name, nb)
    for i=1,nb do
        self:nextColumn();
        local newE = ISSimpleEmpty:new(self);
        self:initAndAddToTable(newE, name .. i);
    end

    -- Add to yAct
    local deltaY = getTextManager():getFontHeight(newE.font);
    if self.deltaY < deltaY then self.deltaY = deltaY end
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
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleComboBox:new(self, items);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = UI_HEIGHT_SMALL;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addScrollList(name, items)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleScrollingListBox:new(self, items);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = UI_HEIGHT_SMALL * 8;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addImage(name, path)
    self:nextColumn();
    
    -- Create element
   local newE = ISSimpleComboBox:new(self, items);
   self:initAndAddToTable(newE, name);

   -- Add to yAct
   local deltaY = UI_HEIGHT_SMALL;
   if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addImageButton(name, path, width, func)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleButton:new(self, text, func);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = getTextManager():getFontHeight(newE.font);
    if self.deltaY < deltaY then self.deltaY = deltaY end
end


-- Position and size

function ISSimpleUI:setPositionPercent(pctX, pctY)
    self.pctX = pctX;
    self.pctY = pctY;
    self.pxlX = pctX * getCore():getScreenWidth();
    self.pxlY = pctY * getCore():getScreenHeight();
    self:setX(self.pxlX);
    self:setY(self.pxlY);
end

function ISSimpleUI:setPositionPixel(pxlX, pxlY)
    self.pxlX = pxlX;
    self.pxlY = pxlY;
    self.pctX = pxlX / getCore():getScreenWidth();
    self.pctY = pxlY / getCore():getScreenHeight();
    self:setX(self.pxlX);
    self:setY(self.pxlY);
end

function ISSimpleUI:setXPercent(pctX)
    self.pctX = pctX;
    self.pxlX = pctX * getCore():getScreenWidth();
    self:setX(self.pxlX);
end

function ISSimpleUI:setXPixel(pxlX)
    self.pxlX = pxlX;
    self.pctX = pxlX / getCore():getScreenWidth();
    self:setX(self.pxlX);
end

function ISSimpleUI:setYPercent(pctY)
    self.pctY = pctY;
    self.pxlY = pctY * getCore():getScreenHeight();
    self:setX(self.pxlY);
end

function ISSimpleUI:setYPixel(pxlY)
    self.pxlY = pxlY;
    self.pctY = pxlY / getCore():getScreenHeight();
    self:setX(self.pxlY);
end

function ISSimpleUI:setWidthPercent(pctW)
    self.pctW = pctW;
    self.pxlW = pctW * getCore():getScreenWidth();
    self:setWidth(self.pxlW);
end

function ISSimpleUI:setWidthPixel(pxlW)
    self.pctW = pctW / getCore():getScreenWidth();
    self.pxlW = pxlW;
    self:setWidth(self.pxlW);
end

function ISSimpleUI:setInCenter()
    self.pxlX = (getCore():getScreenWidth() - self:getWidth()) / 2 ;
    self.pxlY = (getCore():getScreenHeight() - self:getHeight()) / 2;
    self.pctX = self.pxlX / getCore():getScreenWidth();
    self.pctY = self.pxlY / getCore():getScreenHeight();
    self:setX(self.pxlX);
    self:setY(self.pxlY);
end


-- Key

function ISSimpleUI:setKey(k)
    self.key = k;
end