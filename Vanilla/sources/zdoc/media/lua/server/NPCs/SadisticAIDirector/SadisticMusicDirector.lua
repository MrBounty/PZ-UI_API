require "ISBaseObject"

---@class SadisticMusicDirector : ISBaseObject
SadisticMusicDirector = ISBaseObject:derive("SadisticMusicDirector");

function SadisticMusicDirector:tick()
    if isServer() then return end

    local playerObj = getSpecificPlayer(0)
    if playerObj == nil then return end;

    local stats = playerObj:getStats();    
    local numVisible = stats:getNumVisibleZombies();
    local numChasing = stats:getNumChasingZombies();
    local numZombies = numVisible + numChasing;

    if self.lastNumZombie < numZombies then
        self:seenZombies(numZombies);
    end
    self.lastNumZombie = numZombies;

    -- multiplier goes up by 30 every real-world second
    local multiplier = getGameTime():getMultiplier() / 1.6;
    self.lastChangedTrack = self.lastChangedTrack + multiplier;

    if numZombies == 0 then
        self.drama = 0;
        self.lastSeenZombie = self.lastSeenZombie + multiplier;
    else
        self.lastSeenZombie = 0;
    end

    if self:shouldChangeTrack() then
        self:changeTrack();
    end
end

function SadisticMusicDirector:seenZombies(num)
    if num == 0 then
        self.drama = 0;
        return;
    end
    if num > 10 then
       self.drama = 10;
    else
        self.drama = num + 3;
    end
end

function SadisticMusicDirector:shouldChangeTrack()
    if getCore():getOptionMusicVolume() == 0 then
        return false;
    end
    if not getSoundManager():isPlayingMusic() then
        return true;
    end
    if (self.lastTriggerDrama <= 3) and (self.drama > 6) and (self.lastChangedTrack > self.triggerDelay) then
        return true;
    end
    return false
end

function SadisticMusicDirector:changeTrack()
    local mod = 0;
    if getSpecificPlayer(0):isOutside() then
        mod = ZombRand(5) - 2;
    end
    if getCore():getGameMode() == "LastStand" then
        if self.drama < 6 then self.drama = 6; end
    end
    local choice = MusicChoices.get(self.drama + mod);
    getSoundManager():playMusic(choice);

    self.lastTriggerDrama = self.drama + mod;
    self.lastChangedTrack = 0;

    -- reboot the next time we'll play music
    self.triggerDelay = ZombRand(60*30,100*30);
end

function SadisticMusicDirector:new ()
    local o = {}
    setmetatable(o, self)
    self.__index = self
    o.triggerDelay = 0;
    o.lastTimeSinceZombie = 0;
    o.lastNumZombie = 0;
    o.lastTriggerDrama = 0;
    o.drama = 0;
    o.lastChangedTrack = 11110;
    o.lastSeenZombie = 0;
    return o
end

SadisticMusicDirector.instance = SadisticMusicDirector:new();

function SadisticMusicDirectorTick()
    -- noiseworks
--[[
    -- SadisticAIDirector:tick() used to call this, now it's called by IngameState.
    SadisticMusicDirector.instance:tick()
]]--
end

