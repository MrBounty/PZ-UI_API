--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISItemDropBox : ISPanel
ISItemDropBox = ISPanel:derive("ISItemDropBox");

function ISItemDropBox:initialise()
    ISPanel.initialise(self)
end

function ISItemDropBox:createChildren()
    --instanceItem("item");
end

function ISItemDropBox:prerender()
    ISPanel.prerender(self);
end


function ISItemDropBox:render()
    ISPanel.render(self);

    if self.mouseOverState > 0 then
        local c, c2 = self.backgroundColor, self.borderColor;
        if self.mouseOverState == 1 and self.doHighlight then
            c, c2 = self.backgroundColorHL, self.borderColorHL;
        elseif self.mouseOverState == 2 and self.doValidHighlight then
            c, c2 = self.backgroundColorHLVal, self.borderColorHLVal;
        elseif self.mouseOverState == 3 and self.doInvalidHighlight then
            c, c2 = self.backgroundColorHLInv, self.borderColorHLInv;
        end

        self:drawRect(0, 0, self:getWidth(), self:getHeight(), c.a, c.r, c.g, c.b);
        self:drawRectBorder(0, 0, self:getWidth(), self:getHeight(), c2.a, c2.r, c2.g, c2.b);
    end

    if self.doBackDropTex and self.backDropTex and not self.storedItemTex then
        self:drawTextureScaled(self.backDropTex, 2, 2, self:getHeight()-4, self:getHeight()-4, self.backDropTexCol.a, self.backDropTexCol.r, self.backDropTexCol.g, self.backDropTexCol.b);
    end

    if self.storedItemTex then
        self:drawTextureScaled(self.storedItemTex, 2, 2, self:getHeight()-4, self:getHeight()-4, 1.0, 1.0, 1.0, 1.0);
    end
end

function ISItemDropBox:defaultVerifyItem( _item )
    return true;
end

function ISItemDropBox:hasValidItemInDrag()
    if not self.mouseEnabled then return false end

    local verifyFunc = self.onVerifyItem or self.defaultVerifyItem;
    if ISMouseDrag.dragging ~= nil and ISMouseDrag.draggingFocus ~= self then
        for k,v in ipairs(ISMouseDrag.dragging) do
            if instanceof(v, "InventoryItem") then
                if verifyFunc( self.functionTarget, v ) then return true; end
            elseif v.items and type(v.items)=="table" and #v.items > 1 then
                for k2,v2 in ipairs(v.items) do
                    if k2 ~= 1 and instanceof(v2, "InventoryItem") then
                        if verifyFunc( self.functionTarget, v2 ) then return true; end
                    end
                end
            end
        end
    end
end

function ISItemDropBox:onMouseMove(dx, dy)
    if not self.mouseEnabled then return; end

    self:activateToolTip();

    self.mouseOverState = 1;
    if self.boxOccupied == false and ISMouseDrag.dragging ~= nil and ISMouseDrag.draggingFocus ~= self then
        if self:hasValidItemInDrag() then
            self.mouseOverState = 2;
        else
            self.mouseOverState = 3;
        end
    end
end

function ISItemDropBox:onMouseMoveOutside(dx, dy)
    if not self.mouseEnabled then return; end

    self:deactivateToolTip();

    self.mouseOverState = 0;
end

function ISItemDropBox:onMouseDown(x, y)
end

function ISItemDropBox:onMouseUp(x, y)
    if not self:getIsVisible() or  self.boxOccupied == true or not self.mouseEnabled then
        return;
    end

    local items = {};
    local verifyFunc = self.onVerifyItem or self.defaultVerifyItem;
    --if ISMouseDrag.dragging ~= nil and ISMouseDrag.draggingFocus ~= self and ISMouseDrag.draggingFocus ~= nil then
    if ISMouseDrag.dragging ~= nil and ISMouseDrag.draggingFocus ~= self then
        for k,v in ipairs(ISMouseDrag.dragging) do
            if instanceof(v, "InventoryItem") then
                --v is a item;
                if verifyFunc( self.functionTarget, v ) then
                    table.insert(items, v);
                end
            elseif v.items and type(v.items)=="table" and #v.items > 1 then
                --find items;
                for k2,v2 in ipairs(v.items) do
                    if k2 ~= 1 and instanceof(v2, "InventoryItem") then
                        --v2 is inventory item.
                        if verifyFunc( self.functionTarget, v2 ) then
                            table.insert(items, v2);
                        end
                    end
                end
            end
        end
    end

    if #items == 0 then
        return;
    end

    if self.onItemDropped then
        self.onItemDropped( self.functionTarget, items )
    else
        self:itemDropped( items );
    end
end

function ISItemDropBox:onRightMouseUp(x, y)
    if not self.mouseEnabled then return; end

    if self.boxOccupied == true then
        if self.onItemRemove then
            self.onItemRemove( self.functionTarget );
        else
            self:setStoredItem( nil );
        end
    end
end

--default behaviour oughta be overriden, picks first of list
function ISItemDropBox:itemDropped( _items )
    if _items[1] then
        self:setStoredItem( _items[1] );
    end
end

function ISItemDropBox:setStoredItem( _item )
    -- set stored item
    if self.storeItem == true then
        self.storedItem = _item;
    end
    if _item then
        self.boxOccupied = true;
        self.storedItemTex = _item:getTex();
    else
        self.boxOccupied = false;
        self.storedItemTex = nil;
    end
end

function ISItemDropBox:setStoredItemFake( _itemTex )
    if _itemTex then
        self.boxOccupied = true;
        self.storedItemTex = _itemTex;
    else
        self.boxOccupied = false;
        self.storedItemTex = nil;
    end
end

function ISItemDropBox:setBackDropTex( _tex, _a, _r, _g, _b )
    self.backDropTex = _tex;
    if _a and _r and _g and _b then
        self.backDropTexCol = { r = _r, g=_g, b=_b, a=_a };
    end
end
function ISItemDropBox:setDoBackDropTex( _b )
    self.doBackDropTex = _b;
end

function ISItemDropBox:setToolTip( _b, _text )
    self.doToolTip = _b;
    if _b == true and _text then
        self.toolTipText = _text;
    end
end

function ISItemDropBox:activateToolTip()
    if self.doToolTip then
        if self.toolTip ~= nil then
            self.toolTip:setVisible(true);
            self.toolTip:addToUIManager();
            self.toolTip:bringToTop()
        else
            self.toolTip = ISToolTip:new(item);
            self.toolTip:initialise();
            self.toolTip:addToUIManager();
            self.toolTip:setOwner(self);
            self.toolTip.description = self.toolTipText; --"Drag a battery in here, or rightclick to remove it.";
            self.toolTip:doLayout();
        end
    end
end
function ISItemDropBox:deactivateToolTip()
    if self.toolTip then
        self.toolTip:removeFromUIManager();
        self.toolTip:setVisible(false);
        self.toolTip = nil;
    end
end

function ISItemDropBox:setHighlight( _b, _a, _r, _g, _b, _a2, _r2, _g2, _b2 )
    self.doHighlight = _b;
    if _b == true and _a and _r and _g and _b  and _a2 and _r2 and _g2 and _b2 then
        self.backgroundColorHL = {r=_r, g=_g, b=_b, a=_a};
        self.borderColorHL = {r=_r2, g=_g2, b=_b2, a=_a2};
    end
end
function ISItemDropBox:setValidHighlight( _b, _a, _r, _g, _b, _a2, _r2, _g2, _b2 )
    self.doValidHighlight = _b;
    if _b == true and _a and _r and _g and _b  and _a2 and _r2 and _g2 and _b2 then
        self.backgroundColorHLVal = {r=_r, g=_g, b=_b, a=_a};
        self.borderColorHLVal = {r=_r2, g=_g2, b=_b2, a=_a2};
    end
end
function ISItemDropBox:setInvalidHighlight( _b, _a, _r, _g, _b, _a2, _r2, _g2, _b2 )
    self.doInvalidHighlight = _b;
    if _b == true and _a and _r and _g and _b  and _a2 and _r2 and _g2 and _b2 then
        self.backgroundColorHLInv = {r=_r, g=_g, b=_b, a=_a};
        self.borderColorHLInv = {r=_r2, g=_g2, b=_b2, a=_a2};
    end
end


function ISItemDropBox:new (x, y, width, height, storeItem, target, onItemDropped, onItemRemove, onVerifyItem, onDragSelf)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = true;
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.doHighlight = true;
    o.backgroundColorHL = {r=0.2, g=0.2, b=0.2, a=1.0}; --Highlights
    o.borderColorHL = {r=0.8, g=0.8, b=0.8, a=1};
    o.doValidHighlight = true;
    o.backgroundColorHLVal = {r=0.0, g=0.2, b=0.0, a=1.0}; --Highlights
    o.borderColorHLVal = {r=0.0, g=1.0, b=0.0, a=1};
    o.doInvalidHighlight = true;
    o.backgroundColorHLInv = {r=0.2, g=0.0, b=0.0, a=1.0}; --Invalid placement Highlights
    o.borderColorHLInv = {r=1.0, g=0.0, b=0.0, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.mouseOverState = 0;
    o.boxOccupied = false;
    -- functions that can be overriden/customized
    o.functionTarget = target;
    o.onItemDropped = onItemDropped; --when items dragged under mouse are dropped in box
    o.onVerifyItem = onVerifyItem; --when items are checked to see if box can accept
    o.onDragSelf = onDragSelf; --when attempting to drag the item currently in the box
    o.onItemRemove = onItemRemove; --when rightclicking to remove item
    --end
    o.storeItem = storeItem; --store java item interally y,n?
    o.storedItem = nil; --javaobj
    o.storedItemTex = nil; --itemtex for display
    --optional stuff:
    o.doBackDropTex = false;
    o.backDropTex = nil;
    o.backDropTexCol = { r = 1, g=1, b=1, a=1 };
    --tooptil
    o.doToolTip = false;
    o.toolTipText = "";
    --toggle mouse functionallity
    o.mouseEnabled = true;
    return o
end
