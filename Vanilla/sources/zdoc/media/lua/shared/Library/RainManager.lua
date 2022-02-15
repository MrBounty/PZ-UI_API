---@class RainManager : zombie.iso.objects.RainManager
---@field public IsRaining boolean
---@field public NumActiveRainSplashes int
---@field public NumActiveRaindrops int
---@field public MaxRainSplashObjects int
---@field public MaxRaindropObjects int
---@field public RainSplashAnimDelay float
---@field public AddNewSplashesDelay int
---@field public AddNewSplashesTimer int
---@field public RaindropGravity float
---@field public GravModMin float
---@field public GravModMax float
---@field public RaindropStartDistance float
---@field public PlayerLocation IsoGridSquare[]
---@field public PlayerOldLocation IsoGridSquare[]
---@field public PlayerMoved boolean
---@field public RainRadius int
---@field public RainAmbient Audio
---@field public ThunderAmbient Audio
---@field public RainSplashTintMod ColorInfo
---@field public RaindropTintMod ColorInfo
---@field public DarkRaindropTintMod ColorInfo
---@field public RainSplashStack ArrayList|IsoRainSplash
---@field public RaindropStack ArrayList|IsoRaindrop
---@field public RainSplashReuseStack Stack|IsoRainSplash
---@field public RaindropReuseStack Stack|IsoRaindrop
---@field private RainChangeTimer float
---@field private RainChangeRate float
---@field private RainChangeRateMin float
---@field private RainChangeRateMax float
---@field public RainIntensity float
---@field public RainDesiredIntensity float
---@field private randRain int
---@field public randRainMin int
---@field public randRainMax int
---@field private stopRain boolean
---@field OutsideAmbient Audio
---@field OutsideNightAmbient Audio
---@field AdjustedRainSplashTintMod ColorInfo
RainManager = {}

---@public
---@param arg0 int
---@param arg1 IsoGridSquare
---@return void
function RainManager:SetPlayerLocation(arg0, arg1) end

---@public
---@return void
function RainManager:UpdateServer() end

---@public
---@param pRandRainMin int
---@return void
function RainManager:setRandRainMin(pRandRainMin) end

---@public
---@param NewRaindrop IsoRaindrop
---@return void
function RainManager:AddRaindrop(NewRaindrop) end

---@public
---@param cell IsoCell
---@param gridSquare IsoGridSquare
---@param CanSee boolean
---@return void
function RainManager:StartRaindrop(cell, gridSquare, CanSee) end

---@public
---@return float
function RainManager:getRainIntensity() end

---@public
---@return void
function RainManager:startRaining() end

---@public
---@param NewRainSplash IsoRainSplash
---@return void
function RainManager:AddRainSplash(NewRainSplash) end

---@public
---@return void
function RainManager:stopRaining() end

---@public
---@param sq IsoGridSquare
---@return void
function RainManager:RemoveAllOn(sq) end

---@private
---@param arg0 IsoPlayer
---@return boolean
function RainManager:interruptSleep(arg0) end

---@public
---@param pRandRainMax int
---@return void
function RainManager:setRandRainMax(pRandRainMax) end

---@public
---@param cell IsoCell
---@param gridSquare IsoGridSquare
---@param CanSee boolean
---@return void
function RainManager:StartRainSplash(cell, gridSquare, CanSee) end

---@private
---@return void
function RainManager:removeAll() end

---@public
---@return void
function RainManager:AddSplashes() end

---@public
---@return void
function RainManager:Update() end

---@public
---@return Boolean
function RainManager:isRaining() end

---@public
---@param DyingRainSplash IsoRainSplash
---@return void
function RainManager:RemoveRainSplash(DyingRainSplash) end

---@public
---@param DyingRaindrop IsoRaindrop
---@return void
function RainManager:RemoveRaindrop(DyingRaindrop) end

---@public
---@param sq IsoGridSquare
---@return boolean
function RainManager:inBounds(sq) end

---@public
---@return void
function RainManager:reset() end
