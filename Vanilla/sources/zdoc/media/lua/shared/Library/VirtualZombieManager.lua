---@class VirtualZombieManager : zombie.VirtualZombieManager
---@field private ReusableZombies ArrayDeque|Unknown
---@field private ReusableZombieSet HashSet|Unknown
---@field public ReusedThisFrame ArrayList|IsoZombie
---@field private RecentlyRemoved ArrayList|Unknown
---@field public instance VirtualZombieManager
---@field public MaxRealZombies int
---@field private m_tempZombies ArrayList|Unknown
---@field public choices ArrayList|IsoGridSquare
---@field private bestchoices ArrayList|Unknown
---@field w HandWeapon
VirtualZombieManager = {}

---@public
---@param room IsoRoom
---@return void
function VirtualZombieManager:roomSpotted(room) end

---@public
---@param z IsoZombie
---@return boolean
function VirtualZombieManager:removeZombieFromWorld(z) end

---@public
---@param ZombieDir int
---@param bDead boolean
---@return IsoZombie
---@overload fun(arg0:int, arg1:boolean, arg2:int)
---@overload fun(descriptorID:int, ZombieDir:int, bDead:boolean)
function VirtualZombieManager:createRealZombieAlways(ZombieDir, bDead) end

---@public
---@param arg0 int
---@param arg1 boolean
---@param arg2 int
---@return IsoZombie
function VirtualZombieManager:createRealZombieAlways(arg0, arg1, arg2) end

---@public
---@param descriptorID int
---@param ZombieDir int
---@param bDead boolean
---@return IsoZombie
function VirtualZombieManager:createRealZombieAlways(descriptorID, ZombieDir, bDead) end

---@public
---@param spawnX float
---@param spawnY float
---@param targetX float
---@param targetY float
---@param count int
---@return void
function VirtualZombieManager:createHordeFromTo(spawnX, spawnY, targetX, targetY, count) end

---@private
---@param arg0 IsoRoom
---@return int
function VirtualZombieManager:getZombieCountForRoom(arg0) end

---@public
---@return void
function VirtualZombieManager:update() end

---@public
---@param z IsoZombie
---@return boolean
function VirtualZombieManager:isReused(z) end

---@public
---@param arg0 int
---@param arg1 RoomDef
---@return ArrayList|Unknown
---@overload fun(arg0:int, arg1:RoomDef, arg2:boolean)
function VirtualZombieManager:addZombiesToMap(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 RoomDef
---@param arg2 boolean
---@return ArrayList|Unknown
function VirtualZombieManager:addZombiesToMap(arg0, arg1, arg2) end

---@public
---@param arg0 IsoDeadBody
---@param arg1 int
---@return void
function VirtualZombieManager:createEatingZombies(arg0, arg1) end

---@public
---@param obj IsoZombie
---@return void
function VirtualZombieManager:RemoveZombie(obj) end

---@public
---@param arg0 IsoChunk
---@param arg1 IsoRoom
---@return void
---@overload fun(arg0:IsoChunk, arg1:IsoRoom, arg2:int, arg3:ArrayList|Unknown)
function VirtualZombieManager:addIndoorZombiesToChunk(arg0, arg1) end

---@public
---@param arg0 IsoChunk
---@param arg1 IsoRoom
---@param arg2 int
---@param arg3 ArrayList|Unknown
---@return void
function VirtualZombieManager:addIndoorZombiesToChunk(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function VirtualZombieManager:canPathOnlyE(arg0, arg1, arg2) end

---@private
---@param arg0 IsoZombie
---@return void
function VirtualZombieManager:reuseZombie(arg0) end

---@public
---@param x float
---@param y float
---@param z float
---@return IsoZombie
function VirtualZombieManager:createRealZombieNow(x, y, z) end

---@public
---@return int
function VirtualZombieManager:reusableZombiesSize() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 int
---@return IsoGridSquare
function VirtualZombieManager:pickEatingZombieSquare(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param x int
---@param y int
---@param z int
---@return boolean
function VirtualZombieManager:canSpawnAt(x, y, z) end

---@public
---@return void
function VirtualZombieManager:init() end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function VirtualZombieManager:canPathOnlyW(arg0, arg1, arg2) end

---@public
---@param nSize int
---@param room RoomDef
---@return void
function VirtualZombieManager:addDeadZombiesToMap(nSize, room) end

---@private
---@param arg0 int
---@param arg1 boolean
---@return IsoZombie
---@overload fun(x:float, y:float, z:float)
function VirtualZombieManager:createRealZombie(arg0, arg1) end

---@public
---@param x float
---@param y float
---@param z float
---@return IsoZombie
function VirtualZombieManager:createRealZombie(x, y, z) end

---@public
---@param z IsoZombie
---@return void
function VirtualZombieManager:addToReusable(z) end

---@public
---@param room RoomDef
---@param bAllowDead boolean
---@return void
function VirtualZombieManager:tryAddIndoorZombies(room, bAllowDead) end

---@private
---@param arg0 int
---@param arg1 RoomDef
---@param arg2 boolean
---@return void
function VirtualZombieManager:addIndoorZombies(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function VirtualZombieManager:canPathOnlyN(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function VirtualZombieManager:isBlockedInAllDirections(arg0, arg1, arg2) end

---@public
---@param nSize int
---@param chk IsoChunk
---@return void
function VirtualZombieManager:AddBloodToMap(nSize, chk) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function VirtualZombieManager:canPathOnlyS(arg0, arg1, arg2) end

---@public
---@return void
function VirtualZombieManager:Reset() end
