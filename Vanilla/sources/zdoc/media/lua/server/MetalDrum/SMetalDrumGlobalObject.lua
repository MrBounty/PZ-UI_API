--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

if isClient() then return end

require "Map/SGlobalObject"

---@class SMetalDrumGlobalObject : SGlobalObject
SMetalDrumGlobalObject = SGlobalObject:derive("SMetalDrumGlobalObject")

local SPRITES = {
    {
        fire_lit = "crafted_01_27",
        fire_unlit = "crafted_01_26",
        water = "crafted_01_25",
        empty = "crafted_01_24"
    },
    {
        fire_lit = "crafted_01_31",
        fire_unlit = "crafted_01_30",
        water = "crafted_01_29",
        empty = "crafted_01_28"
    }
}

function SMetalDrumGlobalObject:new(luaSystem, globalObject)
    local o = SGlobalObject.new(self, luaSystem, globalObject)
    return o
end

function SMetalDrumGlobalObject:initNew()
    self.waterAmount = 0
    self.waterMax = ISMetalDrum.waterMax
    self.taintedWater = false
    self.haveLogs = false
    self.haveCharcoal = false
    self.charcoalTick = 0
    self.isLit = false
end

function SMetalDrumGlobalObject:stateFromIsoObject(isoObject)
    self.exterior = isoObject:getSquare():isOutside()
    self.waterAmount = isoObject:getWaterAmount()
    self.waterMax = isoObject:getModData().waterMax
    self.taintedWater = isoObject:isTaintedWater()
    self.haveLogs = isoObject:getModData().haveLogs
    self.haveCharcoal = isoObject:getModData().haveCharcoal
    self.charcoalTick = isoObject:getModData().charcoalTick
    self.isLit = isoObject:getModData().isLit

    -- Sanity check
    if not self.waterMax then
        self.waterMax = ISMetalDrum.waterMax;
        isoObject:getModData()["waterMax"] = self.waterMax
    end

    self:changeSprite()
    isoObject:transmitModData()
    if self.isLit then
        self:setLit(true)
    end
end

function SMetalDrumGlobalObject:stateToIsoObject(isoObject)
    self.exterior = isoObject:getSquare():isOutside()

    -- Sanity check
    if not self.waterAmount then
        self.waterAmount = 0
    end
    if not self.waterMax then
        self.waterMax = ISMetalDrum.waterMax;
        isoObject:getModData()["waterMax"] = self.waterMax
    end

    if not self.taintedWater then
        self.taintedWater = self.waterAmount > 0 and self.exterior
    end
    isoObject:setTaintedWater(self.taintedWater)

    self:changeSprite()
    self:setModData()
    if self.isLit then
        self:setLit(true)
    end
end

function SMetalDrumGlobalObject:changeSprite()
    local isoObject = self:getIsoObject()
    if not isoObject then return end
    local sprites = self:getSprites()
    if not sprites then return end
    local spriteName = nil
    if isoObject:getModData()["haveLogs"] or isoObject:getModData()["haveCharcoal"] then
        if isoObject:getModData()["isLit"] then
            spriteName = sprites.fire_lit
        else
            spriteName = sprites.fire_unlit
        end
    else
        if self.waterAmount >= self.waterMax * 0.75 then
            spriteName = sprites.water
        else
            spriteName = sprites.empty
        end
    end
    if spriteName and (not isoObject:getSprite() or spriteName ~= isoObject:getSprite():getName()) then
        self:noise('sprite changed to '..spriteName..' at '..self.x..','..self.y..','..self.z)
        isoObject:setSprite(spriteName)
        isoObject:transmitUpdatedSpriteToClients()
    end
end

function SMetalDrumGlobalObject:getSprites()
    local isoObject = self:getIsoObject()
    if not isoObject then return nil end
    local sprite = isoObject:getSprite()
    if not sprite then return nil end
    local spriteName = sprite:getName()
    for _,sprites in ipairs(SPRITES) do
        if spriteName == sprites.fire_lit or
                spriteName == sprites.fire_unlit or
                spriteName == sprites.water or
                spriteName == sprites.empty then
            return sprites
        end
    end
    return nil
end

function SMetalDrumGlobalObject:setModData()
    local isoObject = self:getIsoObject()
    if not isoObject then return end
    local previousisLit = isoObject:getModData()["isLit"]
    local previoushaveLogs = isoObject:getModData()["haveLogs"]
    local previoushaveCharcoal = isoObject:getModData()["haveCharcoal"]
    local previouscharcoalTick = isoObject:getModData()["charcoalTick"]
    isoObject:getModData()["isLit"] = self.isLit
    isoObject:getModData()["haveLogs"] = self.haveLogs
    isoObject:getModData()["haveCharcoal"] = self.haveCharcoal
    isoObject:getModData()["charcoalTick"] = self.charcoalTick
    if previousisLit ~= isoObject:getModData()["isLit"] or
            previoushaveLogs ~= isoObject:getModData()["haveLogs"] or
            previoushaveCharcoal ~= isoObject:getModData()["haveCharcoal"] or
            previouscharcoalTick ~= isoObject:getModData()["charcoalTick"] then
        isoObject:transmitModData()
    end
end

function SMetalDrumGlobalObject:setLit(isLit)
    self.isLit = isLit
    local isoObject = self:getIsoObject()
    if not isoObject then return end
    isoObject:getModData()["isLit"] = isLit
    if isLit then
        -- FIXME: won't be synced in multiplayer
        self.LightSource = IsoLightSource.new(self.x, self.y, self.z, 0.61, 0.165, 0, 3)
        getCell():addLamppost(self.LightSource)
    elseif self.LightSource then
        -- FIXME: won't be synced in multiplayer
        getCell():removeLamppost(self.LightSource)
        self.charcoalTick = 0
    end
    isoObject:transmitModData()
    self:changeSprite()
end

function SMetalDrumGlobalObject:setHaveCharcoal(haveCharcoal)
    self.haveCharcoal = haveCharcoal
    local isoObject = self:getIsoObject()
    if not isoObject then return end
    isoObject:getModData()["haveCharcoal"] = haveCharcoal
    isoObject:transmitModData()
    self:changeSprite()
end

function SMetalDrumGlobalObject:setHaveLogs(haveLogs)
    self.haveLogs = haveLogs
    if not haveLogs then
        self.charcoalTick = 0
    end
    local isoObject = self:getIsoObject()
    if not isoObject then return end
    isoObject:getModData()["haveLogs"] = haveLogs
    isoObject:transmitModData()
    self:changeSprite()
end

function SMetalDrumGlobalObject:update()
    if RainManager.isRaining() then
        self.waterMax = self.waterMax or ISMetalDrum.waterMax
        if self.waterAmount < self.waterMax then
            local square = self:getSquare()
            if square then self.exterior = square:isOutside() end
            if self.exterior then
                if not self.haveLogs and not self.haveCharcoal then
                    self.waterAmount = math.min(self.waterMax, self.waterAmount + 1 * ISMetalDrum.waterScale)
                    self.taintedWater = true
                    local isoObject = self:getIsoObject()
                    if isoObject then -- object might have been destroyed
                        self:noise('added rain to barrel at '..self.x..","..self.y..","..self.z..' waterAmount='..self.waterAmount)
                        isoObject:setTaintedWater(true)
                        isoObject:setWaterAmount(self.waterAmount)
                        isoObject:transmitModData()
                    end
                end
            end
        end
    end

    if self.haveLogs and self.isLit then
        if not self.charcoalTick then
            self.charcoalTick = 1
        else
            self.charcoalTick = self.charcoalTick + 1
        end
        self:noise('charcoal update ' .. self.charcoalTick)
        if self.charcoalTick == 12 then
            self.haveLogs = false
            self.isLit = false
            self.haveCharcoal = true
            self.charcoalTick = nil
            if self.LightSource then
                -- FIXME: won't be synced in multiplayer
                getCell():removeLamppost(self.LightSource)
            end
        end
        self:changeSprite()
        self:setModData()
    end
end

