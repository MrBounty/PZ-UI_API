---@class IsoJukebox : zombie.iso.objects.IsoJukebox
---@field private JukeboxTrack Audio
---@field private IsPlaying boolean
---@field private MusicRadius float
---@field private Activated boolean
---@field private WorldSoundPulseRate int
---@field private WorldSoundPulseDelay int
IsoJukebox = {}

---Overrides:
---
---getObjectName in class IsoObject
---@public
---@return String
function IsoJukebox:getObjectName() end

---@public
---@return void
function IsoJukebox:addToWorld() end

---Overrides:
---
---onMouseLeftClick in class IsoObject
---@public
---@param x int
---@param y int
---@return boolean
function IsoJukebox:onMouseLeftClick(x, y) end

---Overrides:
---
---update in class IsoObject
---@public
---@return void
function IsoJukebox:update() end

---@public
---@param ShouldPlay boolean
---@return void
function IsoJukebox:SetPlaying(ShouldPlay) end
