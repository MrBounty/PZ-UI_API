--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class SpawnRegionMgr
SpawnRegionMgr = {}

function SpawnRegionMgr.loadSpawnPointsFile(filename, server)
	if server then
		if serverFileExists(filename) then
			reloadServerLuaFile(filename)
		else
			print("ERROR: \""..filename.."\" doesn't exist, spawn points may be broken")
			return nil
		end
	else
		if fileExists(filename) then
			reloadLuaFile(filename)
		else
			print("ERROR: \""..filename.."\" doesn't exist, spawn points may be broken")
			return nil
		end
	end
	return SpawnPoints()
end

function SpawnRegionMgr.loadSpawnRegionsFile(filename, server)
	if server then
		if serverFileExists(filename) then
			reloadServerLuaFile(filename)
		else
			print("ERROR: \""..filename.."\" doesn't exist, spawn points may be broken")
			return nil
		end
	else
		if fileExists(filename) then
			reloadLuaFile(filename)
		else
			print("ERROR: \""..filename.."\" doesn't exist, spawn points may be broken")
			return nil
		end
	end
	return SpawnRegionMgr.loadSpawnRegions(SpawnRegions())
end

function SpawnRegionMgr.loadSpawnRegions(regions)
	local valid = {}
	-- add rosewood if needed
	for _,region in ipairs(regions) do
		if region.file then
			region.points = SpawnRegionMgr.loadSpawnPointsFile(region.file, false)
		elseif region.serverfile then
			region.points = SpawnRegionMgr.loadSpawnPointsFile(region.serverfile, true)
		end
		if region.name and region.points then
            --print(region.name);
			table.insert(valid, region)
			local count = 0
			for k,v in pairs(region.points) do count = count + 1 end
--			print('spawn region '..region.name..' has '..count..' professions')
        end
    end
	return valid
end

function SpawnRegionMgr.getSpawnRegionsAux()
	if getWorld():getGameMode() == "Multiplayer" then
		if isServer() then
			local filename = getServerName() .. "_spawnregions.lua"
			if serverFileExists(filename) then
				return SpawnRegionMgr.loadSpawnRegionsFile(filename, true)
			else
				-- fall through, load the map's spawnregions.lua or spawnpoints.lua file
			end
		end
		if isClient() then
			return getServerSpawnRegions()
		end
	end
	local MAPNAME = getWorld():getMap()
	if not MAPNAME:contains(";") and fileExists('media/maps/'..MAPNAME..'/spawnregions.lua') then
		return SpawnRegionMgr.loadSpawnRegionsFile('media/maps/'..MAPNAME..'/spawnregions.lua')
	else
--		local regions = {
--			{ name = MAPNAME, file = 'media/maps/'..MAPNAME..'/spawnpoints.lua' }
--		}
		local regions = createRegionFile();
		return SpawnRegionMgr.loadSpawnRegions(regions)
	end
end

function SpawnRegionMgr.getSpawnRegions()
	local regions = SpawnRegionMgr.getSpawnRegionsAux()
	if regions and not isClient() then
		triggerEvent("OnSpawnRegionsLoaded", regions)
	end
	return regions
end
