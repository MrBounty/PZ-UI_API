--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Map/SGlobalObjectSystem"
require "MetalDrum/BuildingObjects/ISMetalDrum.lua" -- for ISMetalDrum{}

---@class SMetalDrumSystem : SGlobalObjectSystem
SMetalDrumSystem = SGlobalObjectSystem:derive("SMetalDrumSystem")

function SMetalDrumSystem:new()
    local o = SGlobalObjectSystem.new(self, "metaldrum")
    return o
end

function SMetalDrumSystem:initSystem()
    SGlobalObjectSystem.initSystem(self)

    -- Specify GlobalObjectSystem fields that should be saved.
    self.system:setModDataKeys({})
    
    -- Specify GlobalObject fields that should be saved.
    self.system:setObjectModDataKeys({
        'isLit', 'haveLogs', 'haveCharcoal', 'charcoalTick', 'taintedWater', 'waterAmount'})

    self:convertOldModData()
end

function SMetalDrumSystem:newLuaObject(globalObject)
    return SMetalDrumGlobalObject:new(self, globalObject)
end

function SMetalDrumSystem:isValidIsoObject(isoObject)
    return instanceof(isoObject, "IsoThumpable") and isoObject:getName() == "MetalDrum"
end

function SMetalDrumSystem:convertOldModData()
end

function SMetalDrumSystem:checkRain()
    for i=1,self:getLuaObjectCount() do
        local luaObject = self:getLuaObjectByIndex(i)
        luaObject:update()
    end
end

local noise = function(msg)
    SMetalDrumSystem.instance:noise(msg)
end

local function getMetalDrumAt(x, y, z)
    return SMetalDrumSystem.instance:getLuaObjectAt(x, y, z)
end

local Commands = {}

function Commands.addLogs(player, args)
    local drum = getMetalDrumAt(args.x, args.y, args.z)
    if drum then
    else
        noise('no metaldrum found at '..args.x..','..args.y..','..args.z)
    end
end

function Commands.removeLogs(player, args)
    local drum = getMetalDrumAt(args.x, args.y, args.z)
    if drum then
    else
    end
end

function SMetalDrumSystem:OnClientCommand(command, player, args)
    local drum = getMetalDrumAt(args.x, args.y, args.z)
    if not drum then
        noise('no metaldrum found at '..args.x..','..args.y..','..args.z)
        return
    end
    if command == "addLogs" then
        for i=1,5 do
            player:sendObjectChange('removeOneOf', { type = 'Log' })
        end
        drum:setHaveLogs(true)
        return
    end
    if command == "removeLogs" then
        player:sendObjectChange('addItemOfType', { type = 'Base.Log', count = 5 })
        drum:setHaveLogs(false)
        return
    end
    if command == "lightFire" then
        if not drum.isLit and drum.haveLogs then
            drum:setLit(true)
        end
        return
    end
    if command == "putOutFire" then
        if drum.isLit then
            drum:setLit(false)
        end
        return
    end
    if command == "removeCharcoal" then
        if drum.haveCharcoal then
            drum:setHaveCharcoal(false)
            player:sendObjectChange('addItemOfType', { type = 'Base.Charcoal', count = 2 })
        end
        return
    end
    if command == "removeWater" then
        if drum.waterAmount > 0 then
            drum.waterAmount = 0
            drum:getIsoObject():setWaterAmount(0)
            drum:getIsoObject():transmitModData()
        end
        return
    end
end

SGlobalObjectSystem.RegisterSystemClass(SMetalDrumSystem)

-- every 10 minutes we check if it's raining, to fill our water barrel
local function EveryTenMinutes()
    SMetalDrumSystem.instance:checkRain()
end

local function OnWaterAmountChange(object, prevAmount)
    if not object then return end
    local luaObject = SMetalDrumSystem.instance:getLuaObjectAt(object:getX(), object:getY(), object:getZ())
    if luaObject then
        noise('waterAmount changed to '..object:getWaterAmount()..' tainted='..tostring(object:isTaintedWater())..' at '..luaObject.x..','..luaObject.y..','..luaObject.z)
        luaObject.waterAmount = object:getWaterAmount()
        luaObject.taintedWater = object:isTaintedWater()
        luaObject:changeSprite()
    end
end

-- every 10 minutes we check if it's raining, to fill our water barrel
Events.EveryTenMinutes.Add(EveryTenMinutes)

Events.OnWaterAmountChange.Add(OnWaterAmountChange)

