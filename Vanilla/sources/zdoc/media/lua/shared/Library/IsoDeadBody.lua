---@class IsoDeadBody : zombie.iso.objects.IsoDeadBody
---@field public MAX_ROT_STAGES int
---@field private bFemale boolean
---@field private wasZombie boolean
---@field private bFakeDead boolean
---@field private bCrawling boolean
---@field private SpeakColor Color
---@field private SpeakTime float
---@field private m_persistentOutfitID int
---@field private desc SurvivorDesc
---@field private humanVisual HumanVisual
---@field private wornItems WornItems
---@field private attachedItems AttachedItems
---@field private deathTime float
---@field private realWorldDeathTime long
---@field private reanimateTime float
---@field private player IsoPlayer
---@field private fallOnFront boolean
---@field private wasSkeleton boolean
---@field private primaryHandItem InventoryItem
---@field private secondaryHandItem InventoryItem
---@field private m_angle float
---@field private m_zombieRotStageAtDeath int
---@field private onlineID short
---@field private tempZombie ThreadLocal|Unknown
---@field private inf ColorInfo
---@field public atlasTex Texture
---@field private DropShadow Texture
---@field private HIT_TEST_WIDTH float
---@field private HIT_TEST_HEIGHT float
---@field private _rotation Quaternionf
---@field private _transform Transform
---@field private _UNIT_Z Vector3f
---@field private _tempVec3f_1 Vector3f
---@field private _tempVec3f_2 Vector3f
---@field private burnTimer float
---@field public Speaking boolean
---@field public sayLine String
---@field private AllBodies ArrayList|Unknown
---@field private ClientBodies ConcurrentHashMap|Unknown|Unknown
IsoDeadBody = {}

---Overrides:
---
---update in class IsoMovingObject
---@public
---@return void
function IsoDeadBody:update() end

---@public
---@return void
function IsoDeadBody:reanimateLater() end

---@public
---@return void
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:Vector3f, arg4:float, arg5:float, arg6:float, arg7:ColorInfo, arg8:float)
function IsoDeadBody:renderShadow() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 Vector3f
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 ColorInfo
---@param arg8 float
---@return void
function IsoDeadBody:renderShadow(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@return void
function IsoDeadBody:Burn() end

---Specified by:
---
---getTalkerType in interface Talker
---@public
---@return String
function IsoDeadBody:getTalkerType() end

---@public
---@return void
function IsoDeadBody:softReset() end

---@public
---@return void
function IsoDeadBody:updateBodies() end

---@public
---@param arg0 boolean
---@return void
function IsoDeadBody:setFakeDead(arg0) end

---@public
---@return HumanVisual
function IsoDeadBody:getHumanVisual() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoDeadBody:load(arg0, arg1, arg2) end

---@private
---@return void
function IsoDeadBody:updateContainerWithHandItems() end

---@public
---@return AttachedItems
function IsoDeadBody:getAttachedItems() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoDeadBody:save(arg0, arg1) end

---@private
---@return boolean
function IsoDeadBody:updateFakeDead() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 boolean
---@return void
function IsoDeadBody:updateRotting(arg0, arg1, arg2) end

---@public
---@return InventoryItem
function IsoDeadBody:getItem() end

---@public
---@param arg0 InventoryItem
---@return void
function IsoDeadBody:setPrimaryHandItem(arg0) end

---Overrides:
---
---removeFromWorld in class IsoMovingObject
---@public
---@return void
function IsoDeadBody:removeFromWorld() end

---@public
---@return String
function IsoDeadBody:getSayLine() end

---@public
---@param arg0 JVector2
---@param arg1 IsoObject
---@return void
function IsoDeadBody:Collision(arg0, arg1) end

---@private
---@param arg0 ByteBuffer
---@return IsoSprite
function IsoDeadBody:loadSprite(arg0) end

---@public
---@return boolean
function IsoDeadBody:isSkeleton() end

---Specified by:
---
---IsSpeaking in interface Talker
---@public
---@return boolean
function IsoDeadBody:IsSpeaking() end

---@public
---@return boolean
function IsoDeadBody:isFakeDead() end

---@private
---@return String
function IsoDeadBody:getDescription() end

---@public
---@param arg0 float
---@param arg1 float
---@return boolean
function IsoDeadBody:isMouseOver(arg0, arg1) end

---Overrides:
---
---renderlast in class IsoMovingObject
---@public
---@return void
function IsoDeadBody:renderlast() end

---@private
---@return boolean
---@overload fun(arg0:IsoPlayer, arg1:boolean)
function IsoDeadBody:isPlayerNearby() end

---@private
---@param arg0 IsoPlayer
---@param arg1 boolean
---@return boolean
function IsoDeadBody:isPlayerNearby(arg0, arg1) end

---@public
---@return boolean
function IsoDeadBody:isFallOnFront() end

---@public
---@param arg0 boolean
---@return void
function IsoDeadBody:setCrawling(arg0) end

---@public
---@return void
function IsoDeadBody:reanimate() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoDeadBody:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 WornItems
---@return void
function IsoDeadBody:setWornItems(arg0) end

---@public
---@return InventoryItem
function IsoDeadBody:getSecondaryHandItem() end

---Overrides:
---
---addToWorld in class IsoObject
---@public
---@return void
function IsoDeadBody:addToWorld() end

---@public
---@return float
function IsoDeadBody:getAngle() end

---@public
---@return String
function IsoDeadBody:getOutfitName() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@return void
function IsoDeadBody:renderObjectPicker(arg0, arg1, arg2, arg3) end

---@public
---@return boolean
function IsoDeadBody:isFemale() end

---Overrides:
---
---getObjectName in class IsoMovingObject
---@public
---@return String
function IsoDeadBody:getObjectName() end

---@public
---@param arg0 ItemContainer
---@return void
function IsoDeadBody:setContainer(arg0) end

---@public
---@return void
function IsoDeadBody:Reset() end

---@public
---@param arg0 InventoryItem
---@return void
function IsoDeadBody:setSecondaryHandItem(arg0) end

---@public
---@param arg0 AttachedItems
---@return void
function IsoDeadBody:setAttachedItems(arg0) end

---@private
---@return float
function IsoDeadBody:getReanimateDelay() end

---@public
---@param arg0 ByteBuffer
---@return String
function IsoDeadBody:readInventory(arg0) end

---@public
---@return void
function IsoDeadBody:reanimateNow() end

---@public
---@param hours float
---@return void
function IsoDeadBody:setReanimateTime(hours) end

---@private
---@return float
function IsoDeadBody:getFakeDeadWakeupHours() end

---@public
---@param arg0 short
---@return boolean
function IsoDeadBody:isDead(arg0) end

---@public
---@return InventoryItem
function IsoDeadBody:getPrimaryHandItem() end

---@public
---@return short
function IsoDeadBody:getOnlineID() end

---@public
---@return boolean
function IsoDeadBody:isZombie() end

---@public
---@param arg0 ItemVisuals
---@return void
function IsoDeadBody:getItemVisuals(arg0) end

---@public
---@return boolean
function IsoDeadBody:isCrawling() end

---@public
---@param arg0 InventoryItem
---@return void
function IsoDeadBody:checkClothing(arg0) end

---@public
---@return WornItems
function IsoDeadBody:getWornItems() end

---Specified by:
---
---Say in interface Talker
---@public
---@param line String
---@return void
function IsoDeadBody:Say(line) end

---@public
---@param arg0 boolean
---@return void
function IsoDeadBody:setFallOnFront(arg0) end
