---@class IsoSpriteInstance : zombie.iso.sprite.IsoSpriteInstance
---@field public pool ObjectPool|Unknown
---@field private lock AtomicBoolean
---@field public parentSprite IsoSprite
---@field public tintb float
---@field public tintg float
---@field public tintr float
---@field public Frame float
---@field public alpha float
---@field public targetAlpha float
---@field public bCopyTargetAlpha boolean
---@field public bMultiplyObjectAlpha boolean
---@field public Flip boolean
---@field public offZ float
---@field public offX float
---@field public offY float
---@field public AnimFrameIncrease float
---@field multiplier float
---@field public Looped boolean
---@field public Finished boolean
---@field public NextFrame boolean
---@field public scaleX float
---@field public scaleY float
IsoSpriteInstance = {}

---@protected
---@param arg0 IsoObject
---@return void
function IsoSpriteInstance:renderprep(arg0) end

---@private
---@return void
function IsoSpriteInstance:reset() end

---@public
---@return boolean
function IsoSpriteInstance:isCopyTargetAlpha() end

---@public
---@return String
function IsoSpriteInstance:getName() end

---@public
---@return boolean
function IsoSpriteInstance:isMultiplyObjectAlpha() end

---@public
---@return void
function IsoSpriteInstance:update() end

---@public
---@return float
function IsoSpriteInstance:getScaleY() end

---@public
---@param arg0 float
---@param arg1 float
---@return void
function IsoSpriteInstance:setScale(arg0, arg1) end

---@public
---@return IsoSprite
function IsoSpriteInstance:getParentSprite() end

---@public
---@param targetAlpha float
---@return void
function IsoSpriteInstance:SetTargetAlpha(targetAlpha) end

---@public
---@return float
function IsoSpriteInstance:getTargetAlpha() end

---@public
---@param obj IsoObject
---@param x float
---@param y float
---@param z float
---@param dir IsoDirections
---@param offsetX float
---@param offsetY float
---@param info2 ColorInfo
---@return void
function IsoSpriteInstance:render(obj, x, y, z, dir, offsetX, offsetY, info2) end

---@public
---@param perSecond float
---@return void
function IsoSpriteInstance:setFrameSpeedPerFrame(perSecond) end

---@public
---@return float
function IsoSpriteInstance:getTintR() end

---@public
---@return float
function IsoSpriteInstance:getScaleX() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function IsoSpriteInstance:scaleAspect(arg0, arg1, arg2, arg3) end

---@public
---@return float
function IsoSpriteInstance:getFrame() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@return void
function IsoSpriteInstance:RenderGhostTileColor(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return float
function IsoSpriteInstance:getTintB() end

---@public
---@return boolean
function IsoSpriteInstance:isFinished() end

---@public
---@param arg0 IsoSpriteInstance
---@return void
function IsoSpriteInstance:add(arg0) end

---@public
---@return void
function IsoSpriteInstance:Dispose() end

---@public
---@param f float
---@return void
function IsoSpriteInstance:SetAlpha(f) end

---@public
---@return int
function IsoSpriteInstance:getID() end

---@public
---@param arg0 IsoSprite
---@return IsoSpriteInstance
function IsoSpriteInstance:get(arg0) end

---@public
---@return float
function IsoSpriteInstance:getTintG() end

---@public
---@return float
function IsoSpriteInstance:getAlpha() end
