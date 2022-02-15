---@class IsoFireManager : zombie.iso.objects.IsoFireManager
---@field public Red_Oscilator double
---@field public Green_Oscilator double
---@field public Blue_Oscilator double
---@field public Red_Oscilator_Rate double
---@field public Green_Oscilator_Rate double
---@field public Blue_Oscilator_Rate double
---@field public Red_Oscilator_Val double
---@field public Green_Oscilator_Val double
---@field public Blue_Oscilator_Val double
---@field public OscilatorSpeedScalar double
---@field public OscilatorEffectScalar double
---@field public MaxFireObjects int
---@field public FireRecalcDelay int
---@field public FireRecalc int
---@field public LightCalcFromBurningCharacters boolean
---@field public FireAlpha float
---@field public SmokeAlpha float
---@field public FireAnimDelay float
---@field public SmokeAnimDelay float
---@field public FireTintMod ColorInfo
---@field public SmokeTintMod ColorInfo
---@field public FireStack ArrayList|Unknown
---@field public CharactersOnFire_Stack ArrayList|Unknown
---@field private fireSounds IsoFireManager.FireSounds
---@field private updateStack Stack|Unknown
IsoFireManager = {}

---@public
---@param cell IsoCell
---@param gridSquare IsoGridSquare
---@return void
function IsoFireManager:MolotovSmash(cell, gridSquare) end

---@public
---@param cell IsoCell
---@param gridSquare IsoGridSquare
---@param IgniteOnAny boolean
---@param FireStartingEnergy int
---@param Life int
---@return void
function IsoFireManager:StartSmoke(cell, gridSquare, IgniteOnAny, FireStartingEnergy, Life) end

---@public
---@return void
function IsoFireManager:Update() end

---@public
---@param NewFire IsoFire
---@return void
function IsoFireManager:Add(NewFire) end

---@public
---@param cell IsoCell
---@param gridSquare IsoGridSquare
---@param IgniteOnAny boolean
---@param FireStartingEnergy int
---@return void
---@overload fun(cell:IsoCell, gridSquare:IsoGridSquare, IgniteOnAny:boolean, FireStartingEnergy:int, Life:int)
function IsoFireManager:StartFire(cell, gridSquare, IgniteOnAny, FireStartingEnergy) end

---@public
---@param cell IsoCell
---@param gridSquare IsoGridSquare
---@param IgniteOnAny boolean
---@param FireStartingEnergy int
---@param Life int
---@return void
function IsoFireManager:StartFire(cell, gridSquare, IgniteOnAny, FireStartingEnergy, Life) end

---@public
---@param TestSquare IsoGridSquare
---@return void
function IsoFireManager:LightTileWithFire(TestSquare) end

---@public
---@param BurningCharacter IsoGameCharacter
---@return void
function IsoFireManager:AddBurningCharacter(BurningCharacter) end

---@public
---@param arg0 IsoFire
---@return void
function IsoFireManager:updateSound(arg0) end

---@public
---@param FireSquare IsoGridSquare
---@param TestSquare IsoGridSquare
---@param playerIndex int
---@return void
function IsoFireManager:Fire_LightCalc(FireSquare, TestSquare, playerIndex) end

---@public
---@param cell IsoCell
---@param gridSquare IsoGridSquare
---@param power int
---@return void
function IsoFireManager:explode(cell, gridSquare, power) end

---@public
---@param BurningCharacter IsoGameCharacter
---@return void
function IsoFireManager:RemoveBurningCharacter(BurningCharacter) end

---@public
---@param arg0 IsoFire
---@return void
function IsoFireManager:stopSound(arg0) end

---@public
---@param sq IsoGridSquare
---@return void
function IsoFireManager:RemoveAllOn(sq) end

---@public
---@return void
function IsoFireManager:Reset() end

---@public
---@param DyingFire IsoFire
---@return void
function IsoFireManager:Remove(DyingFire) end
