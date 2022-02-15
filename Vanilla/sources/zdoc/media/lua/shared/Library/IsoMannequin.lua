---@class IsoMannequin : zombie.iso.objects.IsoMannequin
---@field private inf ColorInfo
---@field private bInit boolean
---@field private bFemale boolean
---@field private bZombie boolean
---@field private bSkeleton boolean
---@field private pose String
---@field private humanVisual HumanVisual
---@field private itemVisuals ItemVisuals
---@field private wornItems WornItems
---@field private perPlayer IsoMannequin.PerPlayer[]
---@field private bAnimate boolean
---@field private animatedModel AnimatedModel
---@field private drawers IsoMannequin.Drawer[]
---@field private screenX float
---@field private screenY float
---@field private staticPerPlayer IsoMannequin.StaticPerPlayer[]
IsoMannequin = {}

---@public
---@param arg0 ItemContainer
---@param arg1 InventoryItem
---@return boolean
function IsoMannequin:isItemAllowedInContainer(arg0, arg1) end

---@public
---@return String
function IsoMannequin:getPose() end

---@public
---@param arg0 InventoryItem
---@param arg1 IsoGameCharacter
---@return void
function IsoMannequin:wearItem(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoMannequin:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return boolean
function IsoMannequin:isFemale() end

---@public
---@param arg0 ByteBuffer
---@return void
function IsoMannequin:saveState(arg0) end

---@public
---@return boolean
function IsoMannequin:isSkeleton() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function IsoMannequin:renderShadow(arg0, arg1, arg2) end

---@public
---@return HumanVisual
function IsoMannequin:getHumanVisual() end

---@private
---@return void
function IsoMannequin:initOutfit() end

---@public
---@param arg0 IsoMannequin
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 IsoDirections
---@return void
function IsoMannequin:renderMoveableObject(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return boolean
function IsoMannequin:isZombie() end

---@public
---@param arg0 ItemVisuals
---@return void
function IsoMannequin:getItemVisuals(arg0) end

---@private
---@return void
function IsoMannequin:syncModel() end

---@public
---@param arg0 IsoDirections
---@return void
function IsoMannequin:rotate(arg0) end

---@public
---@param arg0 IsoSprite
---@return boolean
function IsoMannequin:isMannequinSprite(arg0) end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@param arg2 ByteBuffer
---@return void
function IsoMannequin:saveChange(arg0, arg1, arg2) end

---@public
---@param arg0 ByteBuffer
---@return void
function IsoMannequin:loadState(arg0) end

---@public
---@param arg0 InventoryItem
---@return void
function IsoMannequin:checkClothing(arg0) end

---@public
---@param arg0 IsoDirections
---@return void
function IsoMannequin:setRenderDirection(arg0) end

---@public
---@param arg0 InventoryItem
---@return void
function IsoMannequin:setCustomSettingsToItem(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 boolean
---@return void
function IsoMannequin:renderFxMask(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function IsoMannequin:calcScreenPos(arg0, arg1, arg2) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoMannequin:load(arg0, arg1, arg2) end

---@public
---@return String
function IsoMannequin:getObjectName() end

---@public
---@param arg0 String
---@param arg1 ByteBuffer
---@return void
function IsoMannequin:loadChange(arg0, arg1) end

---@public
---@return void
function IsoMannequin:addToWorld() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoMannequin:save(arg0, arg1) end

---@private
---@return void
function IsoMannequin:validatePose() end

---@public
---@param arg0 Moveable
---@param arg1 int
---@return IsoDirections
function IsoMannequin:getDirectionFromItem(arg0, arg1) end

---@public
---@param arg0 InventoryItem
---@return void
function IsoMannequin:getCustomSettingsFromItem(arg0) end

---@public
---@param arg0 Map|Unknown|Unknown
---@return void
function IsoMannequin:getVariables(arg0) end

---@public
---@param arg0 Moveable
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 IsoDirections
---@return void
function IsoMannequin:renderMoveableItem(arg0, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 ItemVisuals
---@return void
function IsoMannequin:createInventory(arg0) end

---@private
---@return void
function IsoMannequin:validateSkinTexture() end
