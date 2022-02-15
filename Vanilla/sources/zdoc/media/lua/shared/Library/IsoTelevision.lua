---@class IsoTelevision : zombie.iso.objects.IsoTelevision
---@field protected screenSprites ArrayList|Unknown
---@field protected defaultToNoise boolean
---@field private cacheObjectSprite IsoSprite
---@field protected facing IsoDirections
---@field private hasSetupScreens boolean
---@field private tickIsLightUpdate boolean
---@field private currentScreen IsoTelevision.Screens
---@field private spriteIndex int
IsoTelevision = {}

---@private
---@param arg0 IsoTelevision.Screens
---@return void
function IsoTelevision:setScreen(arg0) end

---@public
---@param arg0 IsoPlayer
---@return boolean
function IsoTelevision:isFacing(arg0) end

---@public
---@param arg0 IsoSprite
---@return void
function IsoTelevision:removeTvScreenSprite(arg0) end

---@public
---@param arg0 IsoSprite
---@return void
function IsoTelevision:addTvScreenSprite(arg0) end

---@public
---@return void
function IsoTelevision:renderlast() end

---@protected
---@param arg0 boolean
---@return void
function IsoTelevision:init(arg0) end

---@protected
---@return void
function IsoTelevision:updateTvScreen() end

---@protected
---@return void
function IsoTelevision:updateLightSource() end

---@private
---@return void
function IsoTelevision:setupDefaultScreens() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoTelevision:load(arg0, arg1, arg2) end

---@public
---@return String
function IsoTelevision:getObjectName() end

---@public
---@return void
function IsoTelevision:update() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoTelevision:save(arg0, arg1) end

---@public
---@return void
function IsoTelevision:clearTvScreenSprites() end
