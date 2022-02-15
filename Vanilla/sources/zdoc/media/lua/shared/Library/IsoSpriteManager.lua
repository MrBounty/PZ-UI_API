---@class IsoSpriteManager : zombie.iso.sprite.IsoSpriteManager
---@field public instance IsoSpriteManager
---@field public NamedMap HashMap|String|IsoSprite
---@field public IntMap TIntObjectHashMap|Unknown
---@field private emptySprite IsoSprite
IsoSpriteManager = {}

---@public
---@param gid String
---@return IsoSprite
---@overload fun(gid:int)
function IsoSpriteManager:getSprite(gid) end

---@public
---@param gid int
---@return IsoSprite
function IsoSpriteManager:getSprite(gid) end

---@public
---@param tex String
---@return IsoSprite
---@overload fun(tex:String, col:Color)
function IsoSpriteManager:getOrAddSpriteCache(tex) end

---@public
---@param tex String
---@param col Color
---@return IsoSprite
function IsoSpriteManager:getOrAddSpriteCache(tex, col) end

---@public
---@param tex String
---@return IsoSprite
---@overload fun(tex:String, ID:int)
function IsoSpriteManager:AddSprite(tex) end

---@public
---@param tex String
---@param ID int
---@return IsoSprite
function IsoSpriteManager:AddSprite(tex, ID) end

---@public
---@return void
function IsoSpriteManager:Dispose() end
