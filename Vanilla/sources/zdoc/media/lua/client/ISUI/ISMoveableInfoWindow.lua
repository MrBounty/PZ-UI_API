require "ISUI/ISPanel"
local displayPositions = {"left","center","right","cursor","norender"};
local doOutlines = true; --draws selected object outlines in panel display

---@class ISMoveableInfoWindow : ISPanel
ISMoveableInfoWindow = ISPanel:derive("ISToolTip");
ISMoveableInfoWindow.infoPanels = {};
ISMoveableInfoWindow.displayPosition = "cursor";

function ISMoveableInfoWindow.setDisplayPosition(_position)
    if _position then
        for k,v in ipairs(displayPositions) do
            if v==_position then
                ISMoveableInfoWindow.displayPosition = _position;
                return;
            end
        end
    end
end

function ISMoveableInfoWindow.moveablePanelModeKey( _key )
    if _key == getCore():getKey("Toggle Moveable Panel Mode") then
        for k,v in ipairs(displayPositions) do
            if v == ISMoveableInfoWindow.displayPosition then
                local next = k+1;
                if next > #displayPositions-1 then --adjusted with -1 to take norender from the list
                    next = 1;
                end
                if getCell() and getCell():getDrag(0) and getCell():getDrag(0).Type and getCell():getDrag(0).Type == "ISMoveableCursor" then
                    ISMoveableInfoWindow.displayPosition = displayPositions[next];
                end
                return;
            end
        end
    end
end

Events.OnKeyPressed.Add(ISMoveableInfoWindow.moveablePanelModeKey);

function ISMoveableInfoWindow:onMouseDown(x, y)
    return false
end

function ISMoveableInfoWindow:onMouseUp(x, y)
    self:removeFromUIManager();
    return false
end

function ISMoveableInfoWindow:onRightMouseDown(x, y)
    return false
end

function ISMoveableInfoWindow:onRightMouseUp(x, y)
    return false
end

function ISMoveableInfoWindow:isMouseOverUI()
    local uis = UIManager.getUI()
    for i=1,uis:size() do
        local ui = uis:get(i-1)
        if ui:isMouseOver() then
            return true
        end
    end
    return false
end

function ISMoveableInfoWindow:prerender()
    local w = getPlayerScreenWidth(self.playerNum);
    local h = getPlayerScreenHeight(self.playerNum);

    local isCursor = false;
    if (ISMoveableInfoWindow.displayPosition == "cursor" or ISMoveableInfoWindow.displayPosition == "norender") and self.square then
        self:setX(isoToScreenX(self.playerNum, self.square:getX(), self.square:getY(), self.square:getZ())+33);
        self:setY(isoToScreenY(self.playerNum, self.square:getX(), self.square:getY(), self.square:getZ())+33);
        isCursor = true;
    elseif ISMoveableInfoWindow.displayPosition == "left" then
        self:setX(5);
        self:setY(h-(self:getHeight()+5));
    elseif ISMoveableInfoWindow.displayPosition == "right" then
        self:setX(w-(self:getWidth()+5));
        self:setY(h-(self:getHeight()+5));
    else -- center
        self:setX((w/2)-(self:getWidth()/2));
        self:setY(h-(self:getHeight()+5));
    end

    self:stayOnSplitScreen();

    if isCursor and self.playerNum == 0 and self:isMouseOver() then -- when mouse in bottom right causes mouseover for panel, corner flip display pos of panel to be above tile instead of below
        self:setY(isoToScreenY(self.playerNum, self.square:getX(), self.square:getY(), self.square:getZ())-self:getHeight());
    end

    if not self:getIsVisible() or (not self.drag or getCell():getDrag(self.playerNum) ~= self.drag) then -- or (isCursor and self:isMouseOver())
        if self.joyfocus then
            self.joyfocus.focus = nil
            updateJoypadFocus(self.joyfocus)
        end
        self:removeFromUIManager();
        return
    end

    self.mouseOverUI = self.playerNum == 0 and wasMouseActiveMoreRecentlyThanJoypad() and self:isMouseOverUI()
end

function ISMoveableInfoWindow:stayOnSplitScreen()
    ISUIElement.stayOnSplitScreen(self, self.playerNum)
end

function ISMoveableInfoWindow:drawTexture(texture, x, y, a, r, g, b)
    if texture and texture:getWidthOrig() == 64 * 2 and texture:getHeightOrig() == 128 * 2 then
        ISUIElement.drawTextureScaledUniform(self, texture, x, y, 0.5, a, r, g, b)
    else
        ISUIElement.drawTexture(self, texture, x, y, a, r, g, b)
    end
end

function ISMoveableInfoWindow:render()
    if self.mouseOverUI then
        return
    end
    if ISMoveableInfoWindow.displayPosition == "cursor" and self:isMouseOver() then
        --self:removeFromUIManager();
        --return;
    end
    if ISMoveableInfoWindow.displayPosition == "norender" then
        return;
    end
    if self.header and self.bodyText and self.footer then
        -- background
        self:drawRect(0, 0, self.width, self.height, 0.7, 0.05, 0.05, 0.05);
        self:drawRectBorder(0, 0, self.width, self.height, 0.5, 0.9, 0.9, 1);

        -- header
        self.header:Draw(self:getX() + (self:getWidth()/2), self:getY());

        local w = 8;
        local h = self.header:getHeight()+2;
        if self.moveableTexture then
            -- texture box
            self:drawRect(8, h, 64, 128, 0, 0, 0, 1);
            --self:drawRectBorder(8, h, 64, 128, 0.8, 0.8, 0.8, 1);
                -- texture
                --self:drawTexture(self.moveableTexture, 8, h, 1);
            --textures (new)
            if #self.textureList > 0 then
                for i = 1, #self.textureList do
                    local texture = self.textureList[i].texture
                    local offsetY = -self.textureList[i].offsetY / Core.getTileScale()
                    if self.textureList[i].texture == self.moveableTexture.texture and self.textureList[i].offsetY == self.moveableTexture.offsetY then
                        if doOutlines then
                            self:drawTexture(texture, 8+1, h-1 + offsetY, 1, 0,0,0);
                            self:drawTexture(texture, 8+1, h+1 + offsetY, 1, 0,0,0);
                            self:drawTexture(texture, 8-1, h+1 + offsetY, 1, 0,0,0);
                            self:drawTexture(texture, 8-1, h-1 + offsetY, 1, 0,0,0);
                        end
                        if self.allowCurrent then
                            self:drawTexture(texture, 8, h + offsetY, 1);
                        else
                            self:drawTexture(texture, 8, h + offsetY, 1, 1,0.25,0.25);
                        end
                    else
                        self:drawTexture(texture, 8, h + offsetY, 0.5);
                    end
                end
            else
                local texture = self.moveableTexture.texture
                local offsetY = -self.moveableTexture.offsetY / Core.getTileScale()
                if self.allowCurrent then
                    self:drawTexture(texture, 8, h + offsetY, 1);
                else
                    self:drawTexture(texture, 8, h, 1, 0.5,0,0);
                end
            end
            self:drawRectBorder(8, h, 64, 128, 0.8, 0.8, 0.8, 1);

            h = h + (self.bodyText.height > 128 and self.bodyText.height or 128) + 2;
            w = w + 64 + 8;
        else
            h = h + self.bodyText.height + 2;
        end

        -- info body
        if self.bodyText.table then
            local x,y = w, self.header:getHeight() + 2;
            for i = 1, #self.bodyText.table do
                local line = self.bodyText.table[i];
                if line[1] then
                    --print(line[1].txt, x,y,line[1].r,line[1].g,line[1].b,1.0);
                    self:drawText(line[1].txt, x,y,line[1].r,line[1].g,line[1].b,1.0,self.bodyText.font);
                end
                if line[2] then
                    self:drawText(line[2].txt, x+self.bodyText.textTab,y,line[2].r,line[2].g,line[2].b,1.0,self.bodyText.font);
                end
                y = y + self.bodyText.fontheight;
            end
        end
        --if self.customBodyAlign and self.customBodyAlign == "center" then
            --self.infoBody:Draw(self:getX() + (self:getWidth()/2), self:getY() + self.header:getHeight() + 2);
        --else
            --self.infoBody:Draw(self:getX() + w, self:getY() + self.header:getHeight() + 2);
        --end

        -- footer
        self.footer:Draw(self:getX() + (self:getWidth()/2), self:getY() + h );
    end
end

function ISMoveableInfoWindow:calculateDimensions()
    if self.header and self.bodyText and self.footer then

        local h = self.header:getHeight() + 2;
        if self.moveableTexture then
            h = h + (self.bodyText.height > 128 and self.bodyText.height or 128) + 2;
        else
            h = h + self.bodyText.height + 2;
        end
        h = h + self.footer:getHeight() + 2;
        self:setHeight( h );

        local w = 8;
        if self.moveableTexture then
            w = w + 64 + 8;
        end
        w = w + (self.bodyText.width > 200-10 and self.bodyText.width+10 or 200);
        if self.header:getWidth() > w then
            w = self.header:getWidth()+8;
        end
        if self.footer:getWidth() > w then
            w = self.footer:getWidth()+8;
        end
        self:setWidth( w );

    end
end

function ISMoveableInfoWindow:setHeaderText( _header, _font )
    if self.header and _header then
        self.header:ReadString(_font or UIFont.Medium, _header, -1);
        self:calculateDimensions();
    end
end

local function print_body(_t)
    if _t or type(_t)=="table" then
        for k,v in pairs(_t) do
            print(tostring(k),tostring(v),type(v));
            if type(v)=="table" then
                for k2,v2 in pairs(v) do
                    print("  " .. tostring(k2),tostring(v2),type(v2));
                    if type(v2)=="table" then
                        for k3,v3 in pairs(v2) do
                            print("    " .. tostring(k3),tostring(v3),type(v3));
                            if type(v3)=="table" then
                                for k4,v4 in pairs(v3) do
                                    print("      " .. tostring(k4),tostring(v4),type(v4));
                                end
                            end
                        end
                    end
                end
            end
        end
    end
end

function ISMoveableInfoWindow:setBodyText( _bodyTextTable, _font, _align)
    --o.fontheight = getTextManager():MeasureStringY(UIFont.Small, "AbdfghijklpqtyZ")+2;
    --print_body(_bodyTextTable);
    self.bodyText = {};
    self.bodyText.font = _font or UIFont.Small;
    self.bodyText.table = _bodyTextTable;
    self.bodyText.fontheight = getTextManager():MeasureStringY(self.bodyText.font, "AbdfghijklpqtyZ")+2;
    self.bodyText.height = 0;
    self.bodyText.width = 0;
    self.bodyText.textTab = 0;
    if _bodyTextTable then
        local identMaxX1,identMaxX2,totalX,totalY = 0,0,0,0;
        for i = 1, #self.bodyText.table do
            local line = self.bodyText.table[i];
            if line[1] then
                local x = getTextManager():MeasureStringX(self.bodyText.font, line[1].txt);
                line[1].r = line[1].r / 255;
                line[1].g = line[1].g / 255;
                line[1].b = line[1].b / 255;
                if not line[2] then
                    if x > self.bodyText.width then
                        self.bodyText.width = x;
                    end
                else
                    if x+10 > self.bodyText.textTab then
                        self.bodyText.textTab = x+10;
                    end
                end
            end
            if line[2] then
                local x = getTextManager():MeasureStringX(self.bodyText.font, line[2].txt);
                line[2].r = line[2].r / 255;
                line[2].g = line[2].g / 255;
                line[2].b = line[2].b / 255;
                if x > identMaxX2 then
                    identMaxX2 = x;
                end
            end
            totalY = totalY+1;
        end
        if self.bodyText.textTab + identMaxX2 > self.bodyText.width then
            self.bodyText.width = self.bodyText.textTab + identMaxX2;
        end
        self.bodyText.height = totalY * self.bodyText.fontheight;
    end
    self:calculateDimensions();
end

function ISMoveableInfoWindow:setBodyTextOLD( _body, _font, _align )
    if self.infoBody and _body then
        if _align then
            self.infoBody:setHorizontalAlign(_align);
            self.customBodyAlign = _align;
        else
            self.infoBody:setHorizontalAlign( "left" );
            self.customBodyAlign = nil;
        end
        self.infoBody:ReadString(_font or UIFont.Medium, _body, -1);
        self:calculateDimensions();
    end
end

function ISMoveableInfoWindow:setFooterText( _footer, _font )
    if self.footer and _footer then
        self.footer:ReadString(_font or UIFont.Medium, _footer, -1);
        self:calculateDimensions();
    end
end

--[[
function ISMoveableInfoWindow:setTexture( _textureName )
    self.moveableTexture = _textureName and getTexture(_textureName) or nil;
    self:calculateDimensions();
end
--]]

function ISMoveableInfoWindow:setTexture( _textureName, _allow, _square, _yoffset )
    self.moveableTexture = _textureName and { texture = getTexture(_textureName), offsetY = (_yoffset or 0) * Core.getTileScale()} or nil;
    self.allowCurrent = _allow;
    self.texYOffset = _yoffset or 0;
    self.textureList = {};
    if _square then
        if _square:getFloor() and _square:getFloor():getTextureName() and getTexture(_square:getFloor():getTextureName()) then
            local t = { texture = getTexture(_square:getFloor():getTextureName()), offsetY = 0 }
            table.insert( self.textureList, t );
        end
        for i = 1, _square:getObjects():size()-1 do
            local obj = _square:getObjects():get(i);
            if obj and obj:getTextureName() and getTexture(obj:getTextureName()) then
                local t = { texture = getTexture(obj:getTextureName()), offsetY = obj:getRenderYOffset() * Core.getTileScale() }
                table.insert(self.textureList, t);

                local sprList = obj:getChildSprites();
                if sprList and (not instanceof(obj,"IsoBarbecue")) then
                    local list_size 	= sprList:size();
                    if list_size > 0 then
                        for i=list_size-1, 0, -1 do
                            local sprite = sprList:get(i):getParentSprite();
                            if sprite:getName() and getTexture(sprite:getName()) then
                                t = { texture = getTexture(sprite:getName()), offsetY = obj:getRenderYOffset() * Core.getTileScale() }
                                table.insert(self.textureList, t);
                            end
                        end
                    end
                end
            end
        end
    end
    self:calculateDimensions();
end

function ISMoveableInfoWindow:setSquare( _square )
    if _square then
        self.square = _square;
    end
end

function ISMoveableInfoWindow:onGainJoypadFocus(joypadData)
    self.drawJoypadFocus = true
end

function ISMoveableInfoWindow:onJoypadDown(button)
    if button == Joypad.BButton then
        self:removeFromUIManager()
        setJoypadFocus(self.playerNum, nil)
    end
end

function ISMoveableInfoWindow:close()
    self:removeFromUIManager()
end

function ISMoveableInfoWindow:setDrag( _drag )
    self.drag = _drag;
end

function ISMoveableInfoWindow:new(x, y, character, drag)
    local o = ISPanel:new(0, 0, 0, 0);
    setmetatable(o, self)
    self.__index = self
    o.character = character
    o.playerNum = character:getPlayerNum()
    o.drag = drag
    --o:setResizable(false)
    o:noBackground();
    o.x = 0;
    o.y = 0;
    o.name = nil;
    o.description = "";
    o.moveableTexture = nil;
    o.texYOffset = 0;
    o.allowCurrent = false;
    o.textureList = {};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0};
    o.width = 0;
    o.height = 0;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    --banana 	#E3CF57 	227 	207 	87 	5754851
    o.header = TextDrawObject.new(255, 255, 255, true, true, true, true, true, true); --TextDrawObject.new(227, 207, 87, true, true, true, true, true, true);
    o.header:setHorizontalAlign("center");
    o.header:ReadString(UIFont.Medium, "Title", -1);
    o.infoBody = TextDrawObject.new(255, 255, 255, true, true, true, true, true, true);
    o.infoBody:setHorizontalAlign("left");
    o.infoBody:ReadString(UIFont.Small, "Info body", -1);
    --cornsilk 3 	#CDC8B1 	205 	200 	177 	11651277
    o.footer = TextDrawObject.new(205, 200, 177, true, true, true, true, true, true);
    o.footer:setHorizontalAlign("center");
    o.footer:ReadString(UIFont.Small, "footer", -1);
    --o.bodyText = {};
    --o.bodyText.font = UIFont.Small;
    --o.bodyText.table = nil;
    --o.bodyText.fontheight = getTextManager():MeasureStringY(o.bodyText.font, "AbdfghijklpqtyZ")+2;
    --o.bodyText.height = 0;
    --o.bodyText.width = 0;
    --o.bodyText.textTab = 0;
    o:setBodyText( nil, nil, nil)
    --o:calculateDimensions();
    return o
end

