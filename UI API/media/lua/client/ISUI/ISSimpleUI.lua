require "ISUI/ISCollapsableWindow"

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

function ISSimpleUI:addLineToMatrices(isLastLine)
    local i = self.lineAct;
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
        if nbElemWidthForce ~= 0 or self.forceColumnWidht ~= {} then
            if widthLeft < 1 then
                print("UI API - ERROR : At line ".. i .." of the UI " .. self:getTitle() .. ". Width set to element wider that the window width.");
            end
            local nbElementLeft = nbElement - nbElemWidthForce;
            local w = math.floor(widthLeft / nbElementLeft); -- Size of element
            local elem = self.matriceLayout[i][j];

            -- Set size of element
            if elem.isWidthForce then -- Id the element get this width force
                self.elemW[i][j] = elem.pxlW;
            elseif self.forceColumnWidht[j] ~= nil then -- If width of the column is set
                self.elemW[i][j] = self.forceColumnWidht[j];
            else
                self.elemW[i][j] = w;
            end

            -- Set x position of element
            if j == 1 then 
                self.elemX[i][j] = 0;
            else
                self.elemX[i][j] = self.elemX[i][j-1] + self.elemW[i][j-1]; -- set position
            end
        else
            self.elemW[i][j] = w; -- set size
            self.elemX[i][j] = w * (j-1); -- set position
        end
    end
    if self.lineColumnCount[i] > 0 then
        self.elemW[i][self.lineColumnCount[i]] = self.pxlW - self.elemX[i][self.lineColumnCount[i]]; -- Set size to last element to the border in case pixel is lost with math.floor
    elseif not isLastLine then
        print("UI API - ERROR : LINE " .. i .." WITHOUT ELEMENT")
    end
end


function ISSimpleUI:setElementsPositionAndSize()
    for index, value in ipairs(self.noNameElements) do
        value:setPositionAndSize()
    end
    for index, value in pairs(self.namedElements) do
        value:setPositionAndSize()
    end
end

function ISSimpleUI:setBorderToAllElements(v)
    for index, value in ipairs(self.noNameElements) do
        value:setBorder(v)
    end
    for index, value in pairs(self.namedElements) do
        value:setBorder(v)
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
    o.elemW = {};
    o.elemX = {};
    o.lineColumnCount = {}; -- Number of columns in a line
    o.columnAct = 0; -- Actual columns of the UI
    o.yAct = o:titleBarHeight(); -- Actual position
    o.forceColumnWidht = {};
    o.deltaY = 0;
    o.lineHaveImages = false;
    o.isUIVisible = true;
    o.useMargin = false;
    o.defaultLineHeight = getTextManager():getFontHeight(UIFont.Small) + 4;
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

function ISSimpleUI:nextLine(isNextLine)
    if not isNextLine and self.useMargin and self.marginW ~= 0 then
        self:addEmpty(_, _, _, self.marginW);
    end

    self:addLineToMatrices();
        
    self.columnAct = 0;
    if self.lineHaveImages then
        self.lineHaveImages = false;
        for index, value in ipairs(self.matriceLayout[self.lineAct]) do
            if value.isImage then
                if self.deltaY < self.elemW[value.line][value.column] * value.ratio then self.deltaY = self.elemW[value.line][value.column] * value.ratio
                else self.elemW[value.line][value.column] = self.deltaY / value.ratio;
                end
            end
        end
        for index, value in ipairs(self.matriceLayout[self.lineAct]) do
            if value.isImage then
                if self.deltaY < self.elemW[value.line][value.column] * value.ratio then self.deltaY = self.elemW[value.line][value.column] * value.ratio
                else self.elemW[value.line][value.column] = self.deltaY / value.ratio;
                end
            end
        end
    end
    
    self.lineAct = self.lineAct + 1;
    self.yAct = self.yAct + self.deltaY;
    table.insert(self.elemH, self.deltaY);
    self.deltaY = 0;
    table.insert(self.elemY, self.yAct);
    table.insert(self.lineColumnCount, 0);

    if not isNextLine and self.useMargin and self.marginW ~= 0 then
        self:addEmpty(_, _, _, self.marginW);
    end
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

function ISSimpleUI:setColumnWidthPercent(column, pctW)
    self.forceColumnWidht[column] = pctW * getCore():getScreenWidth();
end

function ISSimpleUI:setColumnWidthPixel(column, pxlW)
    self.forceColumnWidht[column] = pxlW;
end

function ISSimpleUI:nextColumn()
     -- Not use by user
     -- Add a column
     self.lineColumnCount[self.lineAct] = self.lineColumnCount[self.lineAct] + 1;
     self.columnAct = self.columnAct + 1;
end

function ISSimpleUI:saveLayout()
    self:nextLine();
    if self.useMargin and self.marginH ~= 0 then
        self:addEmpty();
        self:setLineHeightPixel(self.marginH);
        self:nextLine(true);
    end
    self:setHeight(self.yAct);
    self:setElementsPositionAndSize();

    -- Remove collapse button
    self.collapseButton:setVisible(false);
    self.pinButton:setVisible(false);

    self:addToUIManager();
    self:setInCenterOfScreen();
end

function ISSimpleUI:isSubUIOf(parent)
    self.isSubUI = true;
    self.parentUI = parent;
end

function ISSimpleUI:setMarginPercent(pctW, pctH)
    self.useMargin = true;
    self.marginW = pctW * getCore():getScreenWidth();
    self.marginH = pctH * getCore():getScreenHeight();
    if self.marginH ~= 0 then
        self:addEmpty();
        self:setLineHeightPixel(self.marginH);
        self:nextLine();
    end

    if pctW == 0 and pctH == 0 then self.useMargin = false; end
end

function ISSimpleUI:setMarginPixel(pxlW, pxlH)
    self.useMargin = true;
    self.marginW = pxlW;
    self.marginH = pxlH;
    if self.marginH ~= 0 then
        self:addEmpty();
        self:setLineHeightPixel(self.marginH);
        self:nextLine();
    end

    if pxlH == 0 and pxlW == 0 then self.useMargin = false; end
end


-- Elements

function ISSimpleUI:addEmpty(name, nb, pctW, pxlW)
    if not nb then nb = 1 end
    for i=1,nb do
        self:nextColumn();
        local newE = ISSimpleEmpty:new(self);
        if name and name ~= "" then 
            self:initAndAddToTable(newE, name .. i);
        else
            self:initAndAddToTable(newE, "");
        end

        if pctW then
            newE:setWidthPercent(pctW);
        elseif pxlW then
            newE:setWidthPixel(pxlW);
        end
    end

    -- Add to yAct
    local deltaY = self.defaultLineHeight;
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

function ISSimpleUI:addProgressBar(name, value, min, max)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleProgressBar:new(self, value, min, max);
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
   local deltaY = self.defaultLineHeight * 8;
   if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addButton(name, text, func)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleButton:new(self, text, func);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = self.defaultLineHeight;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addTickBox(name)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleTickBox:new(self);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = self.defaultLineHeight;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addEntry(name, text, isNumber)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleEntry:new(self, text, isNumber);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = self.defaultLineHeight;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addComboBox(name, items)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleComboBox:new(self, items);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = self.defaultLineHeight * 1.5;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addScrollList(name, items)
    self:nextColumn();
    
     -- Create element
    local newE = ISSimpleScrollingListBox:new(self, items);
    newE.isScrollList = true;
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = self.defaultLineHeight * 8;
    if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addImage(name, path)
    self:nextColumn();
    self.lineHaveImages = true;
    
    -- Create element
   local newE = ISSimpleImage:new(self, path);
   self:initAndAddToTable(newE, name);

   -- Add to yAct
   local deltaY = self.defaultLineHeight;
   if self.deltaY < deltaY then self.deltaY = deltaY end
end

function ISSimpleUI:addImageButton(name, path, func)
    self:nextColumn();
    self.lineHaveImages = true;
    
     -- Create element
    local newE = ISSimpleImageButton:new(self, path, func);
    self:initAndAddToTable(newE, name);

    -- Add to yAct
    local deltaY = self.defaultLineHeight;
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
    self.pctW = pxlW / getCore():getScreenWidth();
    self.pxlW = pxlW;
    self:setWidth(self.pxlW);
end

function ISSimpleUI:setInCenterOfScreen()
    self.pxlX = (getCore():getScreenWidth() - self:getWidth()) / 2 ;
    self.pxlY = (getCore():getScreenHeight() - self:getHeight()) / 2;
    self.pctX = self.pxlX / getCore():getScreenWidth();
    self.pctY = self.pxlY / getCore():getScreenHeight();
    self:setX(self.pxlX);
    self:setY(self.pxlY);
end

function ISSimpleUI:setDefaultLineHeightPercent(pctH)
    self.defaultLineHeight = pctH * getCore():getScreenHeight();
end

function ISSimpleUI:setDefaultLineHeightPixel(pxlH)
    self.defaultLineHeight = pxlH;
end

function ISSimpleUI:getDefaultLineHeightPercent()
    return self.defaultLineHeight / getCore():getScreenHeight();
end

function ISSimpleUI:getDefaultLineHeightPixel()
    return self.defaultLineHeight;
end


-- Key

function ISSimpleUI:setKey(k)
    self.key = k;
end


-- For collapse if click

function ISSimpleUI:setCollapse(v)
    if v then
        self.canCollapse = v;
        if self.pin then
            self.collapseButton:setVisible(true);
            self.pinButton:setVisible(false);
        else
            self.collapseButton:setVisible(false);
            self.pinButton:setVisible(true);
        end
    else
        self.collapseButton:setVisible(false);
        self.pinButton:setVisible(false);
        self.canCollapse = v;
    end
end

function ISSimpleUI:onMouseDownOutside(x, y) -- Add don't collapse if in subUI
    if((self:getMouseX() < 0 or self:getMouseY() < 0 or self:getMouseX() > self:getWidth() or self:getMouseY() > self:getHeight()) and not self.pin and self.canCollapse) then
        self.isCollapsed = true;
        self.wasCollapsed = true;
        self:setMaxDrawHeight(self:titleBarHeight());
        self.lastHeight = self:getHeight();
        if self.collapseBottom then
            self:setHeightAndParentHeight(self:titleBarHeight());
            self:setY(self:getY() + self.heightAbs - self:titleBarHeight())
        end
    end
end