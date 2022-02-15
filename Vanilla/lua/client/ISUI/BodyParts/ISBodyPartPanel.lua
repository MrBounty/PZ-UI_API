--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

local setup_table = {
    [0] = { filename = "left-hand" },
    [1] = { filename = "right-hand"},
    [2] = { filename = "lower-left-arm"},
    [3] = { filename = "lower-right-arm"},
    [4] = { filename = "upper-left-arm"},
    [5] = { filename = "upper-right-arm"},
    [6] = { filename = "chest", z = 30 },
    [7] = { filename = "abdomen", z = 50 },
    [8] = { filename = "head"},
    [9] = { filename = "neck"},
    [10] = { filename = "groin", z = 100 },
    [11] = { filename = "left-thigh"},
    [12] = { filename = "right-thigh"},
    [13] = { filename = "left-calf"},
    [14] = { filename = "right-calf"},
    [15] = { filename = "left-foot"},
    [16] = { filename = "right-foot"},
}

ISBodyPartPanel = ISPanelJoypad:derive("ISBodyPartPanel");

function ISBodyPartPanel:initialise()
    ISPanelJoypad.initialise(self);

    local sex = self.bFemale and "female" or "male";

    local bodyDamage = self.player:getBodyDamage();

    self.cacheColor = Color.new(  1.0,  1.0, 1.0, 1.0 );

    self.bps = {};
    self.selectedBp = false;

    for i=0,16 do
        local type = BodyPartType.FromIndex(i);
        local tex = getTexture("media/ui/BodyParts/bps_"..sex.."_"..setup_table[i].filename);
        if not tex then
            --print("missing: "..tostring(setup_table[i].filename));
        else
            --print("filename: "..tostring(setup_table[i].filename));
            --print("tex offsetX "..tostring(tex:getOffsetX()));
            --print("tex offsetY "..tostring(tex:getOffsetY()));
            --print("tex width "..tostring(tex:getWidth()));
            --print("tex height "..tostring(tex:getHeight()));
            --print("tex orig width "..tostring(tex:getWidthOrig()));
            --print("tex orig height "..tostring(tex:getHeightOrig()));

            --print("bodypart "..tostring(type));
        end
        local bp = {
            texture = getTexture("media/ui/BodyParts/bps_"..sex.."_"..setup_table[i].filename);
            bodyPartType = type;
            bodyPart = bodyDamage:getBodyPart(type);
            value = self.minValue + (self.maxValue/2),
            selected = false,
            color = Color.new(1,1,1,1),
            alpha = 1.0,
            offsetX = tex:getOffsetX(),
            offsetY = tex:getOffsetY(),
            width = tex:getWidth(),
            height = tex:getHeight(),
            centerX = tex:getOffsetX() + (tex:getWidth()/2),
            centerY = tex:getOffsetY() + (tex:getHeight()/2),
            nodeTexture = nil,
            z = setup_table[i].z or 0,
            enabled = true,
            nodeOffsetX = 0,
            nodeOffsetY = 0,
        };
        if self.bFemale then
            if i==4 then
                bp.nodeOffsetX = 4;
                bp.nodeOffsetY = 0;
            end
            if i==5 then
                bp.nodeOffsetX = -4;
                bp.nodeOffsetY = 0;
            end
            if i==11 then
                bp.nodeOffsetX = -2;
                bp.nodeOffsetY = 10;
            end
            if i==12 then
                bp.nodeOffsetX = 2;
                bp.nodeOffsetY = 10;
            end
            if i==15 then
                bp.nodeOffsetX = -2;
                bp.nodeOffsetY = 0;
            end
            if i==16 then
                bp.nodeOffsetX = 2;
                bp.nodeOffsetY = 0;
            end
        else
            if i==4 then
                bp.nodeOffsetX = 10;
                bp.nodeOffsetY = 0;
            end
            if i==5 then
                bp.nodeOffsetX = -10;
                bp.nodeOffsetY = 0;
            end
            if i==11 then
                bp.nodeOffsetX = -2;
                bp.nodeOffsetY = 10;
            end
            if i==12 then
                bp.nodeOffsetX = 2;
                bp.nodeOffsetY = 10;
            end
            if i==15 then
                bp.nodeOffsetX = -2;
                bp.nodeOffsetY = 0;
            end
            if i==16 then
                bp.nodeOffsetX = 2;
                bp.nodeOffsetY = 0;
            end
        end
        table.insert(self.bps, bp);
    end
end

function ISBodyPartPanel:createChildren()
    ISPanelJoypad.createChildren(self)
    --instanceItem("item");
end

function ISBodyPartPanel:prerender()
    ISPanelJoypad.prerender(self);
end

function ISBodyPartPanel:render()
    ISPanelJoypad.render(self);

    self:drawTexture(self.baseTexture, 0, 0, self.backgroundAlpha);

    local bp;

    for i=1, #self.bps do
        bp = self.bps[i];

        if (not self.selectedBp) then
            self:drawTexture(bp.texture, 0, 0, self.defaultAlpha, bp.color:getRedFloat(), bp.color:getGreenFloat(), bp.color:getBlueFloat());
        else
            if bp == self.selectedBp then
                self:drawTexture(bp.texture, 0, 0, self.selectedAlpha, bp.color:getRedFloat(), bp.color:getGreenFloat(), bp.color:getBlueFloat());
            else
                self:drawTexture(bp.texture, 0, 0, self.deselectedAlpha, bp.color:getRedFloat(), bp.color:getGreenFloat(), bp.color:getBlueFloat());
            end
        end
    end

    self:drawTexture(self.outlineTex, 0, 0, 1.0);

    --self:drawRectBorder( x, y, w, h, a, r, g, b)

    for i=1, #self.bps do
        bp = self.bps[i];
        if self.drawDebugLines and self.bps[i].texture then
            local tex = self.bps[i].texture;
            self:drawRectBorder( tex:getOffsetX(), tex:getOffsetY(), tex:getWidth(), tex:getHeight(), 1, 0, 0, 1);
        end
        if bp == self.selectedBp and self.selectlines.enabled then
            self:drawLineLeft(bp.centerX+bp.nodeOffsetX, bp.centerY+bp.nodeOffsetY);

            if self.drawSelectedHitbox then
                self:drawRectBorder( bp.offsetX-1, bp.offsetY-1, bp.width+2, bp.height+2, 1, 0, 0, 0);
                self:drawRectBorder( bp.offsetX-2, bp.offsetY-2, bp.width+4, bp.height+4, 1, 1, 1, 1);
                self:drawRectBorder( bp.offsetX-3, bp.offsetY-3, bp.width+6, bp.height+6, 1, 0, 0, 0);
            end
        end

        if self.nodes.enabled and ((not self.nodes.onlySelected) or bp == self.selectedBp ) then
            local bp_n, bp_no = bp.nodeTex or self.nodes.nodeTex, bp.nodeOutlineTex or self.nodes.nodeOutlineTex;
            if bp_n and bp_no then
                self:drawTexture(bp_n, bp.centerX-(bp_n:getWidthOrig()/2)+bp.nodeOffsetX, bp.centerY-(bp_n:getHeightOrig()/2)+bp.nodeOffsetY, self.nodeAlpha, bp.color:getRedFloat(), bp.color:getGreenFloat(), bp.color:getBlueFloat());
                self:drawTexture(bp_no, bp.centerX-(bp_no:getWidth()/2)+bp.nodeOffsetX, bp.centerY-(bp_no:getHeight()/2)+bp.nodeOffsetY, self.nodeAlpha, bp.color:getRedFloat(), bp.color:getGreenFloat(), bp.color:getBlueFloat());
            end
        end
    end
end

function ISBodyPartPanel:drawLineLeft(x, y)
    local midpoint = x+((self.width-x)/2);
    local tx = self.selectlines.anchorX;
    local ty = self.selectlines.anchorY;

    local y1 = y>ty and y or ty;
    local y2 = y>ty and ty or y;
    self:drawRect(x, y-1, midpoint-x, 3, 1.0, 0.0, 0.0, 0.0);
    self:drawRect(midpoint-1, y2, 3, y1-y2, 1.0, 0.0, 0.0, 0.0);
    self:drawRect(midpoint, ty-1, tx-midpoint, 3, 1.0, 0.0, 0.0, 0.0);

    self:drawRect(x, y, midpoint-x, 1, 1.0, 1.0, 1.0, 1.0);
    self:drawRect(midpoint, y2, 1, y1-y2, 1.0, 1.0, 1.0, 1.0);
    self:drawRect(midpoint, ty, tx-midpoint, 1, 1.0, 1.0, 1.0, 1.0);
end

function ISBodyPartPanel:getPartForCoordinate(mx, my)
    local bp, z = false, -1;
    local out;
    local x,y = mx, my;
    for i=1, #self.bps do
        bp = self.bps[i];
        if bp.enabled then
            if x >= bp.offsetX and x<= bp.offsetX+bp.width and y >= bp.offsetY and y <= bp.offsetY+bp.height and (bp.z or 0) > z then
                out = bp;
                z = bp.z or 0;
            end
        end
    end
    return out;
end

function ISBodyPartPanel:setSelected(mx, my, bLock)
    --print("mouse move "..tostring(dx).." - "..tostring(dy))
    if self.canSelect then
        local bp = self:getPartForCoordinate(mx, my);
        if bp~=self.selectedBp or (bLock and not self.lockedSelection)  then
            self.selectedBp = bp;

            self.lockedSelection = bLock;

            if self.onPartSelected then
                self.onPartSelected( self.functionTarget, bp );
            end
        else
            if bp==self.selectedBp and bLock then
                self.lockedSelection =false;
            end
        end
    end
end

function ISBodyPartPanel:deselect()
    self.selectedBp = false;
    self.lockedSelection =false;
end

function ISBodyPartPanel:onMouseMove(dx, dy)
    --print("mouse move "..tostring(dx).." - "..tostring(dy))
    if not self.lockedSelection then
        self:setSelected(self:getMouseX(), self:getMouseY());
    end
end

function ISBodyPartPanel:onMouseMoveOutside(dx, dy)
    if self.selectedBp and (not self.lockedSelection) then
        self.selectedBp = false;
        if self.onPartSelected then
            self.onPartSelected( self.functionTarget, nil );
        end
    end
end

function ISBodyPartPanel:onMouseDown(x, y)
    self:setSelected(x, y, true);
end

function ISBodyPartPanel:onMouseUp(x, y)
end

function ISBodyPartPanel:onRightMouseUp(x, y)
    if self.lockedSelection then
        self.selectedBp = false;
        self.lockedSelection = false;
        if self.onPartSelected then
            self.onPartSelected( self.functionTarget, nil );
        end
    end
end



function ISBodyPartPanel:setToolTip( _b, _text )
end

function ISBodyPartPanel:activateToolTip()
end
function ISBodyPartPanel:deactivateToolTip()
end

function ISBodyPartPanel:onJoypadDown(button)
    if button == Joypad.BButton then
        --getPlayerInfoPanel(self.playerNum):toggleView(xpSystemText.clothingIns);
        --setJoypadFocus(self.playerNum, nil);
    end
    if button == Joypad.LBumper then
        --getPlayerInfoPanel(self.playerNum):onJoypadDown(button);
    end
    if button == Joypad.RBumper then
        --getPlayerInfoPanel(self.playerNum):onJoypadDown(button);
    end
end

--if enabled, when selecting a node a line will be drawn from the node center to anchor point
function ISBodyPartPanel:setEnableSelectLines( _b, _defaultAnchorX, _defaultAnchorY )
    self.selectlines.enabled = _b;
    self:setSelectLinesAnchor( _defaultAnchorX or self:getWidth(), _defaultAnchorY or (self:getHeight()/2));
end

function ISBodyPartPanel:setSelectLinesAnchor( _anchorX, _anchorY )
    self.selectlines.anchorX = PZMath.clamp(_anchorX, 0, self:getWidth());
    self.selectlines.anchorY = PZMath.clamp(_anchorY, 0, self:getHeight());
end

-- enable and set default node texture for all bodyparts
-- default available nodes textures:
-- "media/ui/BodyParts/bps_node", "media/ui/BodyParts/bps_node_outline"
-- "media/ui/BodyParts/bps_node_a", "media/ui/BodyParts/bps_node_a_outline"
-- "media/ui/BodyParts/bps_node_big", "media/ui/BodyParts/bps_node_big_outline"
-- "media/ui/BodyParts/bps_node_diamond", "media/ui/BodyParts/bps_node_diamond_outline"
function ISBodyPartPanel:enableNodes( _nodeTex, _nodeOutlineTex )
    self.nodes.enabled = true;
    self.nodes.nodeTex = getTexture(_nodeTex);
    self.nodes.nodeOutlineTex = getTexture(_nodeOutlineTex);
end

--override node texture for a specific bodypart
function ISBodyPartPanel:overrideNodeTexture( _bodyPartType, _nodeTex, _nodeOutlineTex )
    local target, bp;
    for i=1, #self.bps do
        bp = self.bps[i];
        if bp.bodyPartType == _bodyPartType then
            bp.nodeTex = getTexture(_nodeTex);
            bp.nodeOutlineTex = getTexture(_nodeOutlineTex);
            break;
        end
    end
end

function ISBodyPartPanel:setAlphas( _defaultAlpha, _nodeAlpha, _selectedAlpha, _deselectedAlpha, _backgroundAlpha )
    self.defaultAlpha = _defaultAlpha;
    self.nodeAlpha = _nodeAlpha;
    self.selectedAlpha = _selectedAlpha;
    self.deselectedAlpha = _deselectedAlpha;
    self.backgroundAlpha = _backgroundAlpha or self.backgroundAlpha;
end

function ISBodyPartPanel:getBodyPartColor( _bodyPartType )
    local bp;
    for i=1, #self.bps do
        bp = self.bps[i];
        if bp.bodyPartType == _bodyPartType then
            return bp.color;
        end
    end
end

function ISBodyPartPanel:setValue( _bodyPartType, _value, _force )
    local target, bp;
    local val = PZMath.clamp(_value, self.minValue, self.maxValue);
    for i=1, #self.bps do
        bp = self.bps[i];
        if bp.bodyPartType == _bodyPartType then
            if _force or bp.value~=val then
                bp.value = val;

                self:setColorForValue( bp.value, bp.color );
                --[[
                local colS = self.colorScheme[1].color;
                local colE = colS;
                local s, e = 0, 1;
                for j=1,#self.colorScheme do
                    if self.colorScheme[j].val >= bp.value then
                        e = self.colorScheme[j].val;
                        colE = self.colorScheme[j].color;
                        break;
                    end
                    colS = self.colorScheme[j].color;
                    s = self.colorScheme[j].val;
                end

                colS:interp(colE, (bp.value-s) / ((e-s >0) and (e-s) or 1) ,bp.color);
                --]]
            end

            break;
        end
    end
end

function ISBodyPartPanel:setColorForValue( _val, _color )
    local val = PZMath.clamp(_val, self.minValue, self.maxValue);
    local colS = self.colorScheme[1].color;
    local colE = colS;
    local s, e = 0, 1;
    for j=1,#self.colorScheme do
        if self.colorScheme[j].val >= val then
            e = self.colorScheme[j].val;
            colE = self.colorScheme[j].color;
            break;
        end
        colS = self.colorScheme[j].color;
        s = self.colorScheme[j].val;
    end

    colS:interp(colE, (val-s) / ((e-s >0) and (e-s) or 1) , _color);
    return _color;
end

function ISBodyPartPanel:getRgbForValue( _val )
    self:setColorForValue( _val, self.cacheColor );
    return self.cacheColor:getRedFloat(), self.cacheColor:getGreenFloat(), self.cacheColor:getBlueFloat();
end

function ISBodyPartPanel:setColorScheme( _colorScheme )
    self.colorScheme = _colorScheme;
    for i=1, #self.bps do
        local bp = self.bps[i];

        self:setValue(bp.bodyPartType, bp.value, true);
    end
end



function ISBodyPartPanel:new (player, x, y, target, onPartSelected)
    local o = ISPanelJoypad:new(x, y, 123, 302);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = false;
    o.backgroundColor = {r=0, g=0, b=0, a=1.0};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = 123;
    o.height = 302;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;

    o.player = player;
    o.playerNum = player:getPlayerNum();
    o.bFemale = o.player:isFemale();
    o.baseTexture = o.bFemale and getTexture("media/ui/BodyParts/female_base_white") or getTexture("media/ui/BodyParts/male_base_white");
    o.outlineTex = o.bFemale and getTexture("media/ui/BodyParts/bps_female_outlines") or getTexture("media/ui/BodyParts/bps_male_outlines");

    --options
    o.minValue = 0;
    o.maxValue = 1;

    o.colorScheme = {
        { val = 0, color = Color.new(0,0,0,1) },
        { val = 1, color = Color.new(1,1,1,1) },
    }

    o.canSelect = true;
    o.selectlines = { enabled = false };
    o.drawSelectedHitbox = false;

    o.nodes = { enabled = false, onlySelected = true };

    --o.onClickPart = onClickPart;
    --o.onMouseOverPart = onMouseOverPart;

    o.onPartSelected = onPartSelected;

    o.drawDebugLines = false;

    o.defaultAlpha = 0.80;
    o.nodeAlpha = 1.0;
    o.deselectedAlpha = 0.25;
    o.selectedAlpha = 1.0;
    o.backgroundAlpha = 0.25

    o.functionTarget = target;

    o.doToolTip = false;
    o.toolTipText = "";
    return o
end

