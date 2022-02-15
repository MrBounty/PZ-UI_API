---@class IsoChunkMap : zombie.iso.IsoChunkMap
---@field public LEVELS int
---@field public ChunksPerWidth int
---@field public SharedChunks HashMap|Integer|IsoChunk
---@field public MPWorldXA int
---@field public MPWorldYA int
---@field public MPWorldZA int
---@field public WorldXA int
---@field public WorldYA int
---@field public WorldZA int
---@field public SWorldX int[]
---@field public SWorldY int[]
---@field public chunkStore ConcurrentLinkedQueue|Unknown
---@field public bSettingChunk ReentrantLock
---@field public StartChunkGridWidth int
---@field public ChunkGridWidth int
---@field public ChunkWidthInTiles int
---@field private inf ColorInfo
---@field private saveList ArrayList|Unknown
---@field private splatByType ArrayList|Unknown
---@field public PlayerID int
---@field public ignore boolean
---@field public WorldX int
---@field public WorldY int
---@field public filenameServerRequests ArrayList|String
---@field protected chunksSwapB IsoChunk[]
---@field protected chunksSwapA IsoChunk[]
---@field bReadBufferA boolean
---@field XMinTiles int
---@field YMinTiles int
---@field XMaxTiles int
---@field YMaxTiles int
---@field private cell IsoCell
---@field private checkVehiclesFrequency UpdateLimit
IsoChunkMap = {}

---@public
---@param square IsoGridSquare
---@param x int
---@param y int
---@param z int
---@return void
function IsoChunkMap:setGridSquare(square, x, y, z) end

---@private
---@param arg0 int
---@param arg1 IsoChunk
---@return void
---@overload fun(arg0:int, arg1:int, arg2:IsoChunk)
function IsoChunkMap:setChunk(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 IsoChunk
---@return void
function IsoChunkMap:setChunk(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@return boolean
function IsoChunkMap:isTileOutOfrange(arg0) end

---@public
---@return void
function IsoChunkMap:CalcChunkWidth() end

---@public
---@param x int
---@param y int
---@return IsoChunk
function IsoChunkMap:getChunkForGridSquare(x, y) end

---@private
---@param arg0 int
---@return IsoChunk
---@overload fun(x:int, y:int)
function IsoChunkMap:getChunk(arg0) end

---@public
---@param x int
---@param y int
---@return IsoChunk
function IsoChunkMap:getChunk(x, y) end

---@private
---@param arg0 int
---@return int
function IsoChunkMap:tileToChunk(arg0) end

---@public
---@param chr IsoGameCharacter
---@return void
function IsoChunkMap:ProcessChunkPos(chr) end

---@private
---@param arg0 int
---@return boolean
function IsoChunkMap:isGridSquareOutOfRangeZ(arg0) end

---@public
---@param wx int
---@param wy int
---@param x int
---@param y int
---@return void
function IsoChunkMap:LoadChunk(wx, wy, x, y) end

---@public
---@param x int
---@param y int
---@return IsoChunk
function IsoChunkMap:getChunkCurrent(x, y) end

---@public
---@return void
function IsoChunkMap:update() end

---@public
---@return int
function IsoChunkMap:getWorldXMin() end

---@private
---@return void
function IsoChunkMap:Left() end

---@public
---@return void
function IsoChunkMap:checkIntegrityThread() end

---@public
---@return int
function IsoChunkMap:getWorldXMinTiles() end

---@public
---@return int
function IsoChunkMap:getWidthInTiles() end

---@public
---@return void
function IsoChunkMap:drawDebugChunkMap() end

---@private
---@return void
function IsoChunkMap:Right() end

---@public
---@return int
function IsoChunkMap:getWorldYMinTiles() end

---@public
---@return void
function IsoChunkMap:SwapChunkBuffers() end

---@public
---@param from IsoChunkMap
---@return void
function IsoChunkMap:copy(from) end

---@public
---@return void
function IsoChunkMap:processAllLoadGridSquare() end

---@private
---@return void
function IsoChunkMap:LoadUp() end

---@public
---@param x int
---@param y int
---@param z int
---@return IsoGridSquare
function IsoChunkMap:getGridSquareDirect(x, y, z) end

---@private
---@return void
function IsoChunkMap:checkVehicles() end

---@public
---@return void
function IsoChunkMap:checkIntegrity() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return IsoChunk
function IsoChunkMap:LoadChunkForLater(arg0, arg1, arg2, arg3) end

---@public
---@param wx int
---@param wy int
---@return void
function IsoChunkMap:setInitialPos(wx, wy) end

---@public
---@param x int
---@param y int
---@return void
function IsoChunkMap:setWorldStartPos(x, y) end

---@private
---@return void
function IsoChunkMap:LoadRight() end

---@private
---@param arg0 int
---@return int
function IsoChunkMap:gridSquareToTileX(arg0) end

---@private
---@return void
function IsoChunkMap:LoadLeft() end

---@public
---@param x int
---@param y int
---@param z int
---@return IsoGridSquare
function IsoChunkMap:getGridSquare(x, y, z) end

---@public
---@param zza int
---@return void
function IsoChunkMap:renderBloodForChunks(zza) end

---@private
---@param arg0 int
---@return int
function IsoChunkMap:gridSquareToTileY(arg0) end

---@private
---@return void
function IsoChunkMap:Up() end

---@public
---@return int
function IsoChunkMap:getWorldYMin() end

---@public
---@return int
function IsoChunkMap:getWorldXMaxTiles() end

---@private
---@return void
function IsoChunkMap:Down() end

---@public
---@return int
function IsoChunkMap:getWorldYMaxTiles() end

---@private
---@param arg0 int
---@return int
function IsoChunkMap:tileToGridSquare(arg0) end

---@private
---@return void
function IsoChunkMap:LoadDown() end

---@public
---@return void
function IsoChunkMap:Dispose() end

---@public
---@param iD int
---@return IsoRoom
function IsoChunkMap:getRoom(iD) end

---@public
---@return void
function IsoChunkMap:Unload() end

---@private
---@return void
function IsoChunkMap:UpdateCellCache() end

---@public
---@param c IsoChunk
---@param bRequireLock boolean
---@return boolean
function IsoChunkMap:setChunkDirect(c, bRequireLock) end

---@public
---@return void
function IsoChunkMap:Save() end
