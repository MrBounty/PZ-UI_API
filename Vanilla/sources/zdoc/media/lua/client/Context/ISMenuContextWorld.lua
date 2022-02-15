--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class ISMenuContextWorld
ISMenuContextWorld = {};

function ISMenuContextWorld.new()
	local self 					= ISMenuElement.new();

    self.debug                  = false;
    self.contextData            = {[0]={},[1]={},[2]={},[3]={}};

    function self.reset(_playerNum)
        local contextData = self.contextData[_playerNum];
        if contextData then
            contextData.context 				= nil;
            contextData.playerNum 				= nil;
            contextData.player 				    = nil;
            contextData.playerRoom				= nil;
            contextData.inventory 				= nil;
            contextData.object                  = nil;
            contextData.objects 				= nil;
            contextData.test 					= nil;
            contextData.isJoypad                = false;
            contextData.joypadData              = nil;
            contextData.squares                 = {};
        end
    end
	
	function self.init()
        self.reset();
		self.loadElements( ISWorldMenuElements );
    end

    function self.getContextData(_playerNum)
        return self.contextData[_playerNum];
    end

	function self.createMenu( _playerNum, _object, _objects, _x, _y, _test )
--        if getCore():getGameMode() == "Tutorial" then
--            Tutorial1.createWorldContextMenu(_playerNum, _objects, _x, _y);
--            return;
--        end
        local playerObj = getSpecificPlayer(_playerNum);
        if playerObj:isDead() or playerObj:isAsleep() or UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then return end
        self.reset(_playerNum);
        local context;
        if not _test then
		    context = ISWorldObjectContextMenu.createMenu( _playerNum, _objects, _x, _y ) or ISContextMenu.get( _playerNum, _x, _y );
            context:setVisible(true);
        else
            local test = ISWorldObjectContextMenu.createMenu( _playerNum, _objects, _x, _y, _test );
            if test then
                return true;
            end
            context = ISContextMenu.get( _playerNum, _x, _y );
            context:setVisible(false);
        end

		getCell():setDrag(nil, _playerNum);

        local contextData = self.getContextData(_playerNum);

        contextData.context 				= context;
        contextData.playerNum 				= _playerNum;
        contextData.player 				    = playerObj;
        contextData.playerRoom				= playerObj:getCurrentSquare():getRoom();
        contextData.inventory 				= playerObj:getInventory();
        contextData.object                  = _object;
        contextData.objects 				= _objects;
        contextData.test 					= _test;

        if JoypadState.players[_playerNum+1] then
            contextData.isJoypad                = true;
            contextData.playerData              = getPlayerData(_playerNum);
        end

        contextData.objects = self.getAllObjects(contextData);

        if not JoypadState.players[_playerNum+1] then
            local wx,wy = ISCoordConversion.ToWorld( _x*getCore():getZoom(contextData.playerNum), _y*getCore():getZoom(contextData.playerNum), contextData.player:getZ() );
            self.getObjectsSquare( contextData, getCell():getGridSquare(wx, wy, contextData.player:getZ()) );
        end

        self.printDebug(contextData);

        for index, element in self.elements.indexIterator() do
			local test = element.createMenu( contextData );
            if _test and test then
                return true;
            end
        end

        if _test then return false end
	
		if context.numOptions == 1 then
			context:setVisible(false);
        end
        return context;
    end

    function self.getAllObjects( _data )
        local temp = _data.objects;
        _data.objects = {};
        _data.contains = {}
        for _,obj in ipairs(temp) do
            local sq = obj:getSquare();
            self.getObjectsSquare( _data, sq )

            if not _data.contains[obj] then
                table.insert(_data.objects, obj);
                _data.contains[obj] = true
            end
        end
        return _data.objects;
    end

    --call after getAllObjects
    function self.getObjectsSquare( _data, _sq )
        if _sq and not self.tableContains(_data.squares, _sq) then
            table.insert( _data.squares, _sq);
            for i=0, _sq:getObjects():size()-1 do
                local obj = _sq:getObjects():get(i);
                if not _data.contains[obj] then
                    table.insert(_data.objects, obj);
                    _data.contains[obj] = true
                end
            end
        end
    end

    function self.tableContains( _table, _item )
        for _,v in ipairs(_table) do
            if v==_item then return true end
        end
        return false;
    end


    --[[ DEBUG --]]
    function self.printDebug(_data)
        if not _data.test and self.debug then
            print("create menu for player: "..tostring(_data.playerNum))
            print("player: "..tostring(_data.player))

            for k,v in ipairs(_data.squares) do
                print("["..tostring(k).."] square: "..tostring(v:getX())..", "..tostring(v:getY())..", "..tostring(v:getZ()))
            end

            for k,v in ipairs(_data.objects) do
                if v:getSprite() and v:getSprite():getName() then
                    print("  - obj: "..tostring(v:getSprite():getName()));
                end
            end
        end
    end
	
	return self;
end

--[[
--ISMenuContextWorld = {};

function ISMenuContextWorld.new()
	local self 					= ISMenuElement.new();

    self.contextData      = {[0]={},[1]={},[2]={},[3]={}};

    function self.reset(_playerNum)
        local contextData = self.contextData[_playerNum];
        if contextData then
            contextData.context 				= nil;
            contextData.playerNum 				= nil;
            contextData.player 				    = nil;
            contextData.playerRoom				= nil;
            contextData.inventory 				= nil;
            --contextData.object 				    = nil;
            --contextData.sq						= nil;
            --contextData.sqTrue					= nil;
            --contextData.window 				    = nil;
            --contextData.thump 					= nil;
            contextData.objects 				= nil;
            --contextData.worldObjects			= nil;
            contextData.test 					= nil;
            --contextData.x 						= nil;
            --contextData.y 						= nil;
        end
    end

	function self.init()
        self.reset();
		self.loadElements( ISWorldMenuElements );
    end

    function self.getContextData(_playerNum)
        return self.contextData[_playerNum];
    end

	function self.createMenu( _playerNum, _object, _objects, _x, _y, _test )
        self.reset(_playerNum);
        if not _test then
            print("create menu for player: "..tostring(_playerNum))
        end
        local context;
        if not _test then
		    context = ISWorldObjectContextMenu.createMenu( _playerNum, _objects, _x, _y ) or ISContextMenu.get( _playerNum, _x, _y );
            context:setVisible(true);
        else
            local test = ISWorldObjectContextMenu.createMenu( _playerNum, _objects, _x, _y, _test );
            if test then
                return true;
            end
            context = ISContextMenu.get( _playerNum, _x, _y );
            context:setVisible(false);
        end
		--context:setVisible(true);

        local playerObj = getSpecificPlayer(_playerNum);
        if not _test then
            print("player: "..tostring(playerObj))
            for k,v in pairs(_objects) do
                if v:getSprite() and v:getSprite():getName() then
                    print("  "..tostring(v:getSprite():getName()));
                end
            end
        end

		if playerObj:isAsleep() or UIManager.getSpeedControls():getCurrentGameSpeed() == 0 then return end

		getCell():setDrag(nil, _playerNum);

        local contextData = self.getContextData(_playerNum);

        contextData.context 				= context;
        contextData.playerNum 				= _playerNum;
        contextData.player 				    = playerObj;
        contextData.playerRoom				= playerObj:getCurrentSquare():getRoom();
        contextData.inventory 				= playerObj:getInventory();
        contextData.object 				    = _object;
        --contextData.window 				    = IsoObjectPicker.Instance:PickWindow(_x, _y);
        --contextData.thump 					= IsoObjectPicker.Instance:PickThumpable(_x, _y);
        contextData.objects 				= _objects;
        contextData.test 					= _test;
        --contextData.x 						= _x;
        --contextData.y 						= _y;

		if _object and instanceof(_object, "IsoMovingObject") then
            contextData.sq = _object:getCurrentSquare();
		elseif _object then
            contextData.sq = _object:getSquare();
        end

		local wx,wy = ISCoordConversion.ToWorld( contextData.x*getCore():getZoom(contextData.playerNum), contextData.y*getCore():getZoom(contextData.playerNum), contextData.player:getZ() );
        contextData.sqTrue = getCell():getGridSquare(wx, wy, contextData.player:getZ()) or contextData.sq;

        if not contextData.sq and playerObj then
            contextData.sq = playerObj:getSquare();
            contextData.sqTrue = contextData.sq;
            if contextData.sq then
                contextData.object = contextData.sq:getObjects():get(contextData.sq:getObjects():size()-1);
            end
        end

        if not contextData.sq or not contextData.object then
            print("Aborting ISMenuContextWorld, no square("..tostring(contextData.sq~=nil)..") or object("..tostring(contextData.object~=nil).."), world x = "..tostring(wx)..", y = "..tostring(wy));
            if context.numOptions == 1 then
                context:setVisible(false);
            end
            return not _test and context or false;
        end

        contextData.worldObjects 			= self.getWorldObjects( contextData.sq );


for index, element in self.elements.indexIterator() do
    local test = element.createMenu( contextData );
    if _test and test then
        return true;
    end
end

if _test then return false end

if context.numOptions == 1 then
    context:setVisible(false);
end
return context;
end


function self.getWorldObjects( _sq )
    if not _sq then return {} end
    local t = {};
    local objects = _sq:getObjects();
    for i=0, objects:size()-1 do
        local lObject 		= objects:get(i);
        local lSprite 		= lObject:getSprite();
        local lProps 		= lSprite:getProperties();

        table.insert( t, { object = lObject, sprite = lSprite, props = lProps } );
    end
    return t;
end


return self;
end
--]]
