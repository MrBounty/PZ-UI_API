---@class ObjectRenderEffects : zombie.iso.objects.ObjectRenderEffects
---@field public ENABLED boolean
---@field private pool ArrayDeque|Unknown
---@field public x1 double
---@field public y1 double
---@field public x2 double
---@field public y2 double
---@field public x3 double
---@field public y3 double
---@field public x4 double
---@field public y4 double
---@field private tx1 double
---@field private ty1 double
---@field private tx2 double
---@field private ty2 double
---@field private tx3 double
---@field private ty3 double
---@field private tx4 double
---@field private ty4 double
---@field private lx1 double
---@field private ly1 double
---@field private lx2 double
---@field private ly2 double
---@field private lx3 double
---@field private ly3 double
---@field private lx4 double
---@field private ly4 double
---@field private maxX double
---@field private maxY double
---@field private curTime float
---@field private maxTime float
---@field private totalTime float
---@field private totalMaxTime float
---@field private type RenderEffectType
---@field private parent IsoObject
---@field private finish boolean
---@field private isTree boolean
---@field private isBig boolean
---@field private gust boolean
---@field private windType int
---@field private T_MOD float
---@field private windCount int
---@field private windCountTree int
---@field private EFFECTS_COUNT int
---@field private TYPE_COUNT int
---@field private WIND_EFFECTS ObjectRenderEffects[][]
---@field private WIND_EFFECTS_TREES ObjectRenderEffects[][]
---@field private DYNAMIC_EFFECTS ArrayList|Unknown
---@field private RANDOM_RUSTLE ObjectRenderEffects
---@field private randomRustleTime float
---@field private randomRustleTotalTime float
---@field private randomRustleTarget int
---@field private randomRustleType int
ObjectRenderEffects = {}

---@public
---@param arg0 ObjectRenderEffects
---@return void
function ObjectRenderEffects:copyMainFromOther(arg0) end

---@public
---@param arg0 IsoObject
---@param arg1 RenderEffectType
---@param arg2 boolean
---@return ObjectRenderEffects
---@overload fun(arg0:IsoObject, arg1:RenderEffectType, arg2:boolean, arg3:boolean)
function ObjectRenderEffects:getNew(arg0, arg1, arg2) end

---@public
---@param arg0 IsoObject
---@param arg1 RenderEffectType
---@param arg2 boolean
---@param arg3 boolean
---@return ObjectRenderEffects
function ObjectRenderEffects:getNew(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 float
---@return void
function ObjectRenderEffects:lerpAll(arg0) end

---@public
---@param arg0 ObjectRenderEffects
---@return void
function ObjectRenderEffects:release(arg0) end

---@public
---@return void
function ObjectRenderEffects:init() end

---@public
---@param arg0 ObjectRenderEffects
---@return void
function ObjectRenderEffects:add(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return ObjectRenderEffects
function ObjectRenderEffects:getNextWindEffect(arg0, arg1) end

---@private
---@return void
function ObjectRenderEffects:swapTargetToLast() end

---@public
---@return boolean
---@overload fun(arg0:float, arg1:float)
function ObjectRenderEffects:update() end

---@private
---@param arg0 float
---@param arg1 float
---@return void
function ObjectRenderEffects:update(arg0, arg1) end

---@private
---@return ObjectRenderEffects
function ObjectRenderEffects:reset() end

---@public
---@return void
function ObjectRenderEffects:updateStatic() end

---@public
---@return ObjectRenderEffects
function ObjectRenderEffects:alloc() end

---@private
---@param arg0 float
---@param arg1 float
---@return void
function ObjectRenderEffects:updateOLD(arg0, arg1) end
