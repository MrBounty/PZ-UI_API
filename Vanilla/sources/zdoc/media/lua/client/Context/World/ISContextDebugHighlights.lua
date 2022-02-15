--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class ISWorldMenuElements
ISWorldMenuElements = ISWorldMenuElements or {};

function ISWorldMenuElements.ContextDebugHighlights()
    local self 					= ISMenuElement.new();
    --self.worldMenu 				= ISContextManager.getInstance().getWorldMenu();

    self.colors = {
        {r=1.0,g=1.0,b=1.0},
        {r=0.0,g=1.0,b=0.0},
        {r=1.0,g=0.0,b=1.0},
        {r=0.5,g=0.5,b=1.0},
        {r=1.0,g=0.95,b=0.0},
        {r=0.0,g=0.0,b=1.0},
    };
    self.colorindex = 1;
    self.enableMe = false;

    function self.init()
    end

    function self.createMenu( _data )
        if getDebug() and self.enableMe then
            if _data.test then return true; end
            for _,item in ipairs(_data.objects) do
                if _data.test then return true; end
                _data.context:addOption("DBUG: HIGHLIGHT", _data, self.openPanel3, item );
            end
            _data.context:addOption("DBUG: ADD DIRECTIONAL ARROW", _data, self.openPanel01, _data.squares[1] );
            _data.context:addOption("DBUG: SET HOMING POINT", _data, self.openPanel2, _data.squares[1] );
            _data.context:addOption("DBUG: TestMarker", _data, self.openPanel00, _data.squares[1]);
            _data.context:addOption("DBUG: ADD SQ MARKER 0.5", _data, self.openPanel0, _data.squares[1], "circle_highlight", 0.5, false, { r= 0.0, g=1.0, b=0.0} );
            _data.context:addOption("DBUG: ADD SQ MARKER", _data, self.openPanel, _data.squares[1], "circle_highlight", 1.0, false, { r= 0.0, g=1.0, b=0.0} );
            _data.context:addOption("DBUG: ADD SQ MARKER ALT", _data, self.openPanel, _data.squares[1], "circle_highlight_2", 1.0, false, { r= 0.0, g=1.0, b=0.0} );
            _data.context:addOption("DBUG: ADD SQ MARKER 2.5x ALPHA", _data, self.openPanel, _data.squares[1], "circle_highlight", 2.5, true, { r= 1.0, g=1.0, b=0.0} );
            _data.context:addOption("DBUG: ADD SQ MARKER ALT 2.5x ALPHA", _data, self.openPanel, _data.squares[1], "circle_highlight_2", 2.5, true, { r= 1.0, g=1.0, b=1.0} );
            _data.context:addOption("DBUG: ADD SQ MARKER 4x", _data, self.openPanel, _data.squares[1], "circle_highlight", 4.0, false, { r= 1.0, g=0.0, b=1.0} );
        end
    end

    function self.openPanel01( _data, _square)
        if _square then
            --print(_square, _texture, _size, _doAlpha, _c.r, _c.g, _c.b);
            local c = self.colors[5];
            local c = self.colors[self.colorindex];
            local circle = getWorldMarkers():addGridSquareMarker("circle_center", "circle_only_highlight", _square, c.r, c.g, c.b, true, 0.75);
            local arrow = getWorldMarkers():addDirectionArrow(getPlayer(), _square:getX(), _square:getY(), _square:getZ(), "dir_arrow_up", c.r, c.g, c.b, 0.95);
            --pointer:setRenderOffsetY(50);
            --pointer:setStickToCharDist(130);
            --pointer:setAngleLerpVal(0.25);
            --pointer:setMovementLerpVal(0.25);
            --TestMarkers.add(circle, pointer);
            self.colorindex = self.colorindex+1;
            if self.colorindex>#self.colors then
                self.colorindex = 1;
            end
        end
    end

    function self.openPanel00( _data, _square)
        if _square then
            --print(_square, _texture, _size, _doAlpha, _c.r, _c.g, _c.b);
            local c = self.colors[5];
            local circle = getWorldMarkers():addGridSquareMarker("circle_center", "circle_only_highlight", _square, c.r, c.g, c.b, true, 2.5);
            local pointer = getWorldMarkers():addPlayerHomingPoint(getPlayer(), _square:getX(), _square:getY(), "arrow_triangle", c.r, c.g, c.b, 0.6, true, 20);
            pointer:setRenderOffsetY(50);
            pointer:setStickToCharDist(130);
            pointer:setAngleLerpVal(0.25);
            pointer:setMovementLerpVal(0.25);
            TestMarkers.add(circle, pointer);
        end
    end

    function self.openPanel0( _data, _square, _texture, _size, _doAlpha, _c )
        if _square then
            print(_square, _texture, _size, _doAlpha, _c.r, _c.g, _c.b);
            local c = self.colors[6];
            local point = getWorldMarkers():addGridSquareMarker(_texture, nil, _square, c.r, c.g, c.b, _doAlpha, _size);
            local pointer = getWorldMarkers():addPlayerHomingPoint(getPlayer(), _square:getX(), _square:getY(), "arrow_triangle", c.r, c.g, c.b, 0.6, true, 20);
            pointer:setRenderOffsetY(50);
            pointer:setStickToCharDist(130);
            pointer:setAngleLerpVal(0.25);
            pointer:setMovementLerpVal(0.25);
            pointer:setHomeOnOffsetY(-50);
        end
    end

    function self.openPanel( _data, _square, _texture, _size, _doAlpha, _c )
        if _square then
            print(_square, _texture, _size, _doAlpha, _c.r, _c.g, _c.b);
            getWorldMarkers():addGridSquareMarker(_texture, nil, _square, _c.r, _c.g, _c.b, _doAlpha, _size);
        end
    end

    function self.openPanel2( _data, _square )
        if _square then
            --getWorldMarkers():addPlayerHomingPoint(getPlayer(), _square:getX(), _square:getY());
            local c = self.colors[self.colorindex];
            local pointer = getWorldMarkers():addPlayerHomingPoint(getPlayer(), _square:getX(), _square:getY(), "arrow_triangle", c.r, c.g, c.b, 0.6, self.colorindex~=0, 20);
            pointer:setRenderOffsetY(50);
            pointer:setStickToCharDist(130 + (self.colorindex*100));
            pointer:setAngleLerpVal(0.25);
            pointer:setMovementLerpVal(0.25);
            getWorldMarkers():addGridSquareMarker("circle_center", "circle_only_highlight", _square, c.r, c.g, c.b, true, 2.0);

            self.colorindex = self.colorindex+1;
            if self.colorindex>#self.colors then
                self.colorindex = 1;
            end
        end
    end

    function self.openPanel3( _data, _obj )
        if _obj then
            _obj:setOutlineHighlight(true);
            _obj:setOutlineHlBlink(true);
            _obj:setOutlineHighlightCol(1.0, 0.95, 0.0, 1.0);
        end
    end

    return self;
end

