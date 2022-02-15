--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class LastStandPlayerFile
LastStandPlayerFile = {}

function LastStandPlayerFile:load(fileName)
	self.file = getFileReader(fileName, false)
	if not self.file then
		self.error = 'error reading file' .. fileName
		return nil
	end
	self.newPlayer = nil
	self.version = nil
	while true do
		local line = self.file:readLine()
		if line == nil then
			break
		end
		if not self:readLine(line) then
			self.file:close()
			return nil
		end
	end
	self.file:close()
	return self.newPlayer
end

function LastStandPlayerFile:readLine(line)
	if luautils.stringStarts(line, "VERSION=") then
		local version = tonumber(string.split(line, "=")[2])
		if not version then
			self.error = 'missing VERSION line'
			return false
		end
		if version < 1 or version > 1 then
			self.error = 'unknown VERSION ' .. version
			return false
		end
		self.version = version
		return true
	end
	if not self.version then
		self.error = 'missing VERSION line'
		return false
	end
	if luautils.stringStarts(line, "Player") then
		return self:readPlayer()
	end
	if not self.newPlayer then
		self.error = 'missing Player block'
		return false
	end
	if luautils.stringStarts(line, "Clothing") then
		return self:readClothing()
	end
	if luautils.stringStarts(line, "Traits") then
		return self:readTraits()
	end
	if luautils.stringStarts(line, "Skills") then
		return self:readSkills()
	end
	if luautils.stringStarts(line, "Bonus") then
		return self:readBonus()
	end
	return true
end

function LastStandPlayerFile:readBlockStart()
	local line = self.file:readLine()
	if not line then
		self.error = 'unexpected end-of-file'
		return false
	end
	line = luautils.trim(line)
	if line ~= "{" then
		self.error = "expected '{'"
	end
	return true
end

function LastStandPlayerFile:readPlayer()
	if not self:readBlockStart() then return false end
	self.newPlayer = {}
	self.newPlayer.traits = {}
	self.newPlayer.skills = {}
	self.newPlayer.playedTime = 1
	while true do
		local line = self.file:readLine()
		if line == nil then
			self.error = 'unexpected end-of-file'
			return false
		end
		line = luautils.trim(line)
		if line == "}" then
			break
		end
		if luautils.stringStarts(line, "Surname") then
			self.newPlayer.surname = string.split(line, "=")[2] or ""
		end
		if luautils.stringStarts(line, "Forename") then
			self.newPlayer.forename = string.split(line, "=")[2] or ""
		end
		if luautils.stringStarts(line, "Visual=") then
			self.newPlayer.humanVisual = string.sub(line, 8)
		end
		if luautils.stringStarts(line, "Female") then
			self.newPlayer.female = true
		end
		if luautils.stringStarts(line, "Male") then
			self.newPlayer.female = false
		end
		if luautils.stringStarts(line, "Profession") then
			self.newPlayer.profession = string.split(line, "=")[2]
		end
		if luautils.stringStarts(line, "GlobalXp") then
			self.newPlayer.globalXp = string.split(line, "=")[2]
		end
		if luautils.stringStarts(line, "Level") then
			self.newPlayer.level = string.split(line, "=")[2]
		end
		if luautils.stringStarts(line, "PlayedTime") then
			self.newPlayer.playedTime = string.split(line, "=")[2]
		end
	end
	return true
end

function LastStandPlayerFile:readClothing()
	if not self:readBlockStart() then return false end
	while true do
		local line = self.file:readLine()
		if line == nil then
			self.error = 'unexpected end-of-file'
			return false
		end
		line = luautils.trim(line)
		if line == "}" then
			break
		end
		if luautils.stringStarts(line, "clothing=") then
			self.newPlayer.clothingVisuals = self.newPlayer.clothingVisuals or {}
			table.insert(self.newPlayer.clothingVisuals, string.sub(line, 10))
		end
	end
	return true
end

function LastStandPlayerFile:readTraits()
	if not self:readBlockStart() then return false end
	while true do
		local line = self.file:readLine()
		if line == nil then
			self.error = 'unexpected end-of-file'
			return false
		end
		line = luautils.trim(line)
		if line == "}" then
			break
		end
		if luautils.stringStarts(line, "addTrait") then
			table.insert(self.newPlayer.traits,string.split(line, "=")[2])
		end
	end
	return true
end

function LastStandPlayerFile:readSkills()
	if not self:readBlockStart() then return false end
	while true do
		local line = self.file:readLine()
		if line == nil then
			self.error = 'unexpected end-of-file'
			return false
		end
		line = luautils.trim(line)
		if line == "}" then
			break
		end
		if luautils.stringStarts(line, "addSkills") then
			local ss = string.split(string.split(line, "=")[2], ",")
			local perkName = ss[1]
			local level = tonumber(ss[2])
			self.newPlayer.skills[perkName] = level
		end
	end
	return true
end

function LastStandPlayerFile:readBonus()
	if not self:readBlockStart() then return false end
	while true do
		local line = self.file:readLine()
		if line == nil then
			self.error = 'unexpected end-of-file'
			return false
		end
		line = luautils.trim(line)
		if line == "}" then
			break
		end
		if luautils.stringStarts(line, "addGoldBoostBonus") then
			self.newPlayer.boostGoldLevel = string.split(line, "=")[2]
		end
		if luautils.stringStarts(line, "addXpBoostBonus") then
			self.newPlayer.boostXpLevel = string.split(line, "=")[2]
		end
		if luautils.stringStarts(line, "addStartingGoldBonus") then
			self.newPlayer.startingGoldLevel = string.split(line, "=")[2]
		end
	end
	return true
end

function LastStandPlayerFile:new()
	local o = {}
	setmetatable(o, self)
	self.__index = self
	o.file = nil
	o.error = nil
	return o
end
