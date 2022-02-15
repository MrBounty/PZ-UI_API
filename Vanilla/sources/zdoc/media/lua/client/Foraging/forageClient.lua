--[[---------------------------------------------
-------------------------------------------------
--
-- forageClient
--
-- eris
--
-------------------------------------------------
--]]---------------------------------------------
if isServer() then return; end;
-------------------------------------------------
-------------------------------------------------
local clientID = getRandomUUID();
---@class forageClient
forageClient = {};
forageData = {};

--[[--======== Data ========--]]--

function forageClient.init()
	forageData = ModData.getOrCreate("forageData");
end
Events.OnInitGlobalModData.Add(forageClient.init)

function forageClient.getZones() return forageData; end;

function forageClient.updateData()
	--sendClientCommand("forageData", "updateData", {}, clientID);
	forageData = ModData.getOrCreate("forageData");
end

function forageClient.clearData()
	ModData.remove("forageData");
end

function forageClient.syncForageData()
	--ModData.request("forageData"); end;
end

--[[--======== Zone ========--]]--

function forageClient.addZone(_zoneData)
	--sendClientCommand("forageData", "addZone", _zoneData, clientID);
	forageData[_zoneData.id] = _zoneData;
end

function forageClient.removeZone(_zoneData)
	--sendClientCommand("forageData", "removeZone", _zoneData, clientID);
	forageData[_zoneData.id] = nil;
end

function forageClient.updateZone(_zoneData)
	forageClient.addZone(_zoneData);
end

--[[--======== Icon ========--]]--

function forageClient.updateIcon(_zoneData, _iconID, _icon)
	triggerEvent("onUpdateIcon", _zoneData, _iconID, _icon);
	--sendClientCommand("forageData", "updateIcon", _zoneData, clientID);
	forageData[_zoneData.id].forageIcons[_iconID] = _icon;
end

--[[--======== OnServerCommand  ========--
forageClient.OnServerCommand = function(_module, _command, _packet, _clientID)
	if clientID ~= _clientID then
		if _module ~= "forageData" then return; end;
		if (not forageClient[_command]) or (not _packet) then
			print("aborted function call in forageClient "
					.. (_command or "missing _command.")
					.. (_packet or "missing _packet."));
		else
			forageClient[_command](_packet);
		end;
	end;
end

--Events.OnServerCommand.Add(forageClient.OnServerCommand);
]]--


--[[--======== OnReceiveGlobalModData ========--
forageClient.OnReceiveGlobalModData = function(_module, _packet)
	if _module ~= "forageData" then return; end;
	if (not _packet) then
		print("aborted OnReceiveGlobalModData in forageClient "
				.. (_packet or "missing _packet."));
	else
		ModData.add(_module, _packet);
	end;
end

--Events.OnReceiveGlobalModData.Add(forageClient.OnReceiveGlobalModData);
]]--


