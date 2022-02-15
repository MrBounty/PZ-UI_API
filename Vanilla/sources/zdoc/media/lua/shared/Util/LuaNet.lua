--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

local function getLuaNetModule( _name, _debug )
    local self 			= {};
    local self_commands = {};
    local self_LuaNet 	= LuaNet:getInstance();
    local self_name 	= _name;
    local self_debug	= _debug or false;

    -- ***********************************************************************************************
    -- addCommandHandler
    -- string		_command	= name of the command
    -- function		_handler	= (optional) function to handle incoming packets for command
    -- ***********************************************************************************************
    function self.addCommandHandler( _command, _handler )
        -- see if command is string and not already added
        if type( _command ) == 'string' and not self_commands[ _command ] then
            self_commands[ _command ] = {};
            -- add the handler function if its set
            if _handler and type( _handler ) == "function" then
                if self_debug then print( "LuaNet: Adding command handler -> " .. tostring( _command ) ); end
                self_commands[ _command ].handler = _handler;
            else
                if self_debug then print( "LuaNet: Adding command -> " .. tostring( _command ) ); end
            end
        else
            if self_debug then print( "LuaNet: Command already exists or not string -> " .. tostring( _command ) ); end
        end
    end

    -- ***********************************************************************************************
    -- listen (used internally)
    -- table		_package	= a LuaNet package
    -- IsoPlayer	_player		= on server carries over IsoPlayer
    -- ***********************************************************************************************
    function self.listen( _package, _player )
        if self_LuaNet.isRunning() then
            -- read LuaNet package
            local com 		= _package.command;
            local recipient = _package.recipient;
            local source 	= _package.source;
            local payload 	= _package.payload;

            if self_debug then print( "LuaNet: Lookup handler for -> ", com ); end
            if com and self_commands[ com ] then
                if isServer() and recipient then
                    if self_debug then print( "LuaNet: Forwarding to player -> ", recipient ); end
                    -- if a recipient is set in the _package_ then forward it to that player
                    local players 		= getOnlinePlayers();
                    local array_size 	= players:size();
                    for i=0, array_size-1, 1 do
                        local player = players:get(i);
                        if player:getUsername() == recipient then
                            if self_debug then print( "LuaNet: IsoPlayer [OK] for player -> ", recipient ); end
                            sendServerCommand( player, self_LuaNet:getName(), self_name, _package );
                        end
                    end
                elseif self_commands[ com ].handler then
                    -- elseif handler function is set, handle package and unpack payload
                    if self_debug then print( "LuaNet: Passing payload to handler for -> ", com ); end
                    if not _player and source then
                        -- if on _player is not set and source is, then attempt to get IsoPlayer from source username
                        local players 		= getOnlinePlayers();
                        local array_size 	= players:size();
                        for i=0, array_size-1, 1 do
                            local player = players:get(i);
                            if player:getUsername() == source then
                                if self_debug then print( "LuaNet: Retrieved source IsoPlayer for player -> ", source ); end
                                _player = player;
                            end
                        end
                    end
                    -- unpack payload to handler, attempt to pass a IsoPlayer if available
                    self_commands[ com ].handler( _player or false, unpack( payload ) );
                else
                    -- else warn we have dead package
                    if self_debug then print( "LuaNet: Package journey ended unexpectedly for command -> ", com ); end
                end
            end
        end
    end

    -- ***********************************************************************************************
    -- send
    -- string		_command	= name of the command
    -- arg			...			= any number of arguments
    -- ***********************************************************************************************
    function self.send( _command, ... )
        self.sendPlayer( false, _command, ... )
    end

    -- ***********************************************************************************************
    -- sendPlayer (NOTE: has slightly different behaviour for server and client, read comments for info.)
    -- IsoPlayer	_player		= IsoPlayer instance of the recipient
    -- string		_command	= name of the command
    -- arg			...			= any number of arguments
    -- ***********************************************************************************************
    function self.sendPlayer( _player, _command, ... )
        if self_LuaNet.isRunning() then --and self_commands[ _command ] then
        if _player and not instanceof(_player, "IsoPlayer" ) then
            -- if player param set and not a player return and warn.
            if self_debug then print( "LuaNet: Not a instance of IsoPlayer for sendPlayer -> ", _command ); end
            return;
        end

        -- create the package
        local package 		= {};
        package.command 	= _command;
        package.recipient 	= false;
        package.source 		= false;
        package.payload 	= { ... };		-- payload will be unpacked in exact order to the handler function on the receiving end.
        if isServer() then
            if _player then
                -- if player set, send package directly to player, no need to add source or recipient info
                sendServerCommand( _player, self_LuaNet:getName(), self_name, package );
            else
                -- otherwise send package to all clients
                sendServerCommand( self_LuaNet:getName(), self_name, package );
            end
        elseif isClient() then
            if _player then
                -- if player set, add recipient info so server can forward, and source info so target knows where it came from
                package.source = getPlayer():getUsername();
                package.recipient = _player:getUsername();
                sendClientCommand( self_LuaNet:getName(), self_name, package );
            else
                -- otherwise just send package to server.
                sendClientCommand( self_LuaNet:getName(), self_name, package );
            end
        end
        else
            if self_debug then print( "LuaNet: Not running or command not found -> ", self_LuaNet.isRunning(), _command ); end
        end
    end

    return self;
end

--[[
	LUANET MAIN CLASS:
--]]

local instance 	= false;	-- stores instance
local validInit = false;	-- variable used to prevent initialization by users

---@class LuaNet
LuaNet 			= {};

function LuaNet:getInstance()
    if instance then
        return instance;
    end

    local self 			= {};
    local self_name 	= "LuaNet";
    local self_modules 	= {};
    local self_running 	= false;
    local self_debug 	= false;

    -- ***********************************************************************************************
    -- self.setDebug
    -- bool 	debug 	set debugging true or false
    -- ***********************************************************************************************
    function self.setDebug( _debug )
        if type( _debug ) == "boolean" then
            self_debug = _debug;
            print( "LuaNet: Debugging set to -> ", self_debug );
        end
    end

    -- ***********************************************************************************************
    -- getDebug
    -- returns 	bool 	debugging true/false
    -- ***********************************************************************************************
    function self.getDebug()
        return self_debug;
    end

    -- ***********************************************************************************************
    -- isRunning
    -- returns 	bool 	true if LuaNet is initialised and running, else false
    -- ***********************************************************************************************
    function self.isRunning()
        return self_running;
    end

    -- ***********************************************************************************************
    -- getName
    -- returns 	string 	by default 'LuaNet', used as pz network code module
    -- ***********************************************************************************************
    function self.getName()
        return self_name;
    end

    -- ***********************************************************************************************
    -- getModule
    -- returns 	LuaNetModule 	returns a LuaNetModule if exists, else creates and returns
    -- ***********************************************************************************************
    function self.getModule( _name, _debug )
        if not self_modules[ _name ] then
            self_modules[ _name ] = getLuaNetModule( _name, _debug );
        end
        return self_modules[ _name ];
    end

    -- ***********************************************************************************************
    -- self_listen (private function)
    -- string			_module		= name of the module
    -- LuaNetPackage	_package	= table containing package info
    -- IsoPlayer		_player		= optional, on server passes it player on client not
    -- ***********************************************************************************************
    local function self_listen( _module, _package, _player )
        if self_modules[ _module ] then
            if self_debug then print( "LuaNet: Incomming module/command -> ", _module, _package.command ); end
            -- pass package and player to the module's listener
            self_modules[ _module ].listen( _package, _player );
        end
    end

    -- ***********************************************************************************************
    -- self_listen (private function)
    -- string			_mainModule		= name of the main module must match this self_name (LuaNet)
    -- string			_module			= name of the module
    -- LuaNetPackage	_package	    = table containing package info
    -- ***********************************************************************************************
    local function self_listenClient( _mainModule, _module, _package )
        if _mainModule == self_name then
            self_listen( _module, _package, nil );
        end
    end

    -- ***********************************************************************************************
    -- self_listen (private function)
    -- string			_mainModule		= name of the main module must match this self_name (LuaNet)
    -- string			_module			= name of the module
    -- IsoPlayer		_player			= optional, on server passes it player on client not
    -- LuaNetPackage	_package	    = table containing package info
    -- ***********************************************************************************************
    local function self_listenServer( _mainModule, _module, _player, _package )
        if _mainModule == self_name then
            self_listen( _module, _package, _player );
        end
    end

    -- ***********************************************************************************************
    -- InitList
    -- List of funcitons to call on Init event
    -- ***********************************************************************************************
    local self_initList = {};
    function self.onInitAdd( _func )
        table.insert( self_initList, _func );
    end

    -- ***********************************************************************************************
    -- Init
    -- Can only be called by OnServerStarted or OnGameTimeLoaded below, sets listners and triggers Init event
    -- ***********************************************************************************************
    function self.Init()
        if not validInit or self_running then return end

        if isServer() then
            print("LuaNet: Registering server listener..." );
            Events.OnClientCommand.Add( self_listenServer );
        elseif isClient() then
            print("LuaNet: Registering client listener..." );
            Events.OnServerCommand.Add( self_listenClient );
        end

        self_running = true;

        print("LuaNet: Initialization [DONE], triggering events for 'LuaNet.onInitAdd'.");
        --triggerEvent( "OnLuaNetInit", self );
        for i,func in ipairs( self_initList ) do
            func();
        end
        validInit = false;
    end

    function self.destroy()
        instance = false;
    end

    instance = self;
    return self;
end

-- ***********************************************************************************************
-- OnServerStart
-- called when the server udp engine is running
-- ***********************************************************************************************	
Events.OnServerStarted.Add( function()
    if isServer() then
        validInit = true;
        print("LuaNet: Initializing...");
        LuaNet:getInstance().Init();
    end
end );

-- ***********************************************************************************************
-- OnGameTimeLoaded
-- called when the client udp engine is running and player properly initialised
-- ***********************************************************************************************
Events.OnGameTimeLoaded.Add( function()
    if isClient() then
        validInit = true;
        print("LuaNet: Loading...");
        LuaNet:getInstance().Init();
    end
end );
