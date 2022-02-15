local function handleMannequinZone(file, v)
	if v.properties == nil then
		print('ERROR: Mannequin zone missing properties in '..file..' at '..v.x..','..v.y..','..v.z)
		return
	end
	getWorld():registerMannequinZone(v.name, v.type, v.x, v.y, v.z, v.width, v.height, v.properties)
end

local function handleSpawnOrigin(file, v)
	getWorld():registerSpawnOrigin(v.x, v.y, v.width, v.height, v.properties)
end

local function handleWaterFlow(file, v)
	if v.properties == nil then
		print('ERROR: WaterFlow missing properties in '..file..' at '..v.x..','..v.y..','..v.z)
		return
	end
	if v.properties.WaterDirection == nil then
		print('ERROR: WaterFlow missing properties.WaterDirection in '..file..' at '..v.x..','..v.y..','..v.z)
		return
	end
	if v.properties.WaterSpeed == nil then
		print('ERROR: WaterFlow missing properties.WaterSpeed in '..file..' at '..v.x..','..v.y..','..v.z)
		return
	end
	getWorld():registerWaterFlow(v.x, v.y, v.properties.WaterDirection, v.properties.WaterSpeed)
end

local function handleWaterZone(file, v)
	if v.properties == nil then
		print('ERROR: WaterZone missing properties in '..file..' at '..v.x..','..v.y..','..v.z)
		return
	end
	if v.properties.WaterGround == nil then
		print('ERROR: WaterZone missing properties.WaterGround in '..file..' at '..v.x..','..v.y..','..v.z)
		return
	end
	if v.properties.WaterShore == nil then
		print('ERROR: WaterZone missing properties.WaterShore in '..file..' at '..v.x..','..v.y..','..v.z)
		return
	end
	getWorld():registerWaterZone(v.x, v.y, v.x + v.width, v.y + v.height,
		v.properties.WaterShore and 1.0 or 0.0, v.properties.WaterGround and 1.0 or 0.0)
end

function doMapZones()
    local dirs = getLotDirectories()
    for i=dirs:size(),1,-1 do
        local file = 'media/maps/'..dirs:get(i-1)..'/objects.lua'
        if fileExists(file) then
			getWorld():removeZonesForLotDirectory(dirs:get(i-1))
			objects = {}
            reloadLuaFile(file)
            for k,v in ipairs(objects) do
				if v.type == "Mannequin" then
					handleMannequinZone(file, v)
				elseif v.type == "SpawnOrigin" then
					handleSpawnOrigin(file, v)
				elseif v.type == "WaterFlow" then
					handleWaterFlow(file, v)
				elseif v.type == "WaterZone" then
					handleWaterZone(file, v)
				elseif v.geometry ~= nil then
					if tonumber(v.lineWidth) then
						v.properties = v.properties or {}
						v.properties.LineWidth = v.lineWidth
					end
					getWorld():getMetaGrid():registerGeometryZone(v.name, v.type, v.z, v.geometry, v.points, v.properties)
				else
					local vzone = getWorld():registerVehiclesZone(v.name, v.type, v.x, v.y, v.z, v.width, v.height, v.properties)
					if vzone == nil then
						getWorld():registerZone(v.name, v.type, v.x, v.y, v.z, v.width, v.height)
					end
				end
				table.wipe(v)
            end
            table.wipe(objects)
        else
            print('can\'t find map objects file: '..file)
        end
		getWorld():checkVehiclesZones();
    end
end

-- Normally a map's objects.lua file (handled above) will contain SpawnOrigin
-- objects.  A map may also have a separate spawnOrigins.lua file that does the
-- same thing.
function doSpawnOrigins()
	local dirs = getLotDirectories()
	for i=dirs:size(),1,-1 do
		local file = 'media/maps/'..dirs:get(i-1)..'/spawnOrigins.lua'
		if fileExists(file) then
			objects = {}
			reloadLuaFile(file)
			for k,v in ipairs(objects) do
				if v.type == "SpawnOrigin" then
					handleSpawnOrigin(file, v)
				end
				table.wipe(v)
			end
			table.wipe(objects)
		end
	end
end

Events.OnLoadMapZones.Add(doMapZones);
Events.OnLoadMapZones.Add(doSpawnOrigins);

