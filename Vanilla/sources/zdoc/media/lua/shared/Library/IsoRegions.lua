---@class IsoRegions : zombie.iso.areas.isoregion.IsoRegions
---@field public SINGLE_CHUNK_PACKET_SIZE int
---@field public CHUNKS_DATA_PACKET_SIZE int
---@field public PRINT_D boolean
---@field public CELL_DIM int
---@field public CELL_CHUNK_DIM int
---@field public CHUNK_DIM int
---@field public CHUNK_MAX_Z int
---@field public BIT_EMPTY byte
---@field public BIT_WALL_N byte
---@field public BIT_WALL_W byte
---@field public BIT_PATH_WALL_N byte
---@field public BIT_PATH_WALL_W byte
---@field public BIT_HAS_FLOOR byte
---@field public BIT_STAIRCASE byte
---@field public BIT_HAS_ROOF byte
---@field public DIR_NONE byte
---@field public DIR_N byte
---@field public DIR_W byte
---@field public DIR_2D_NW byte
---@field public DIR_S byte
---@field public DIR_E byte
---@field public DIR_2D_MAX byte
---@field public DIR_TOP byte
---@field public DIR_BOT byte
---@field public DIR_MAX byte
---@field protected CHUNK_LOAD_DIMENSIONS int
---@field protected DEBUG_LOAD_ALL_CHUNKS boolean
---@field public FILE_PRE String
---@field public FILE_SEP String
---@field public FILE_EXT String
---@field public FILE_DIR String
---@field private SQUARE_CHANGE_WARN_THRESHOLD int
---@field private SQUARE_CHANGE_PER_TICK int
---@field private cacheDir String
---@field private cacheDirFile File
---@field private headDataFile File
---@field private chunkFileNames Map|Unknown|Unknown
---@field private regionWorker IsoRegionWorker
---@field private dataRoot DataRoot
---@field private logger IsoRegionsLogger
---@field protected lastChunkX int
---@field protected lastChunkY int
---@field private previousFlags byte
IsoRegions = {}

---@public
---@param arg0 int
---@param arg1 int
---@return DataChunk
function IsoRegions:getDataChunk(arg0, arg1) end

---@public
---@param arg0 IsoGridSquare
---@return void
---@overload fun(arg0:IsoGridSquare, arg1:boolean)
function IsoRegions:squareChanged(arg0) end

---@public
---@param arg0 IsoGridSquare
---@param arg1 boolean
---@return void
function IsoRegions:squareChanged(arg0, arg1) end

---@public
---@return boolean
function IsoRegions:isDebugLoadAllChunks() end

---@public
---@param arg0 String
---@return void
---@overload fun(arg0:String, arg1:Color)
function IsoRegions:log(arg0) end

---@public
---@param arg0 String
---@param arg1 Color
---@return void
function IsoRegions:log(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function IsoRegions:setDebugLoadAllChunks(arg0) end

---@protected
---@return void
function IsoRegions:forceRecalcSurroundingChunks() end

---@public
---@param arg0 byte
---@return byte
function IsoRegions:GetOppositeDir(arg0) end

---@public
---@param arg0 String
---@return void
function IsoRegions:warn(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 UdpConnection
---@return void
function IsoRegions:receiveClientRequestFullDataChunks(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return int
function IsoRegions:hash(arg0, arg1) end

---@public
---@return void
function IsoRegions:init() end

---@protected
---@return DataRoot
function IsoRegions:getDataRoot() end

---@public
---@param arg0 ByteBuffer
---@return void
function IsoRegions:receiveServerUpdatePacket(arg0) end

---@protected
---@param arg0 IsoGridSquare
---@return byte
function IsoRegions:calculateSquareFlags(arg0) end

---@public
---@param arg0 IsoGridSquare
---@return void
function IsoRegions:setPreviousFlags(arg0) end

---@public
---@return void
function IsoRegions:update() end

---@public
---@return void
function IsoRegions:ResetAllDataDebug() end

---@protected
---@return IsoRegionWorker
function IsoRegions:getRegionWorker() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return IWorldRegion
function IsoRegions:getIsoWorldRegion(arg0, arg1, arg2) end

---@private
---@return void
function IsoRegions:clientResetCachedRegionReferences() end

---@public
---@return IsoRegionsLogger
function IsoRegions:getLogger() end

---@public
---@return File
function IsoRegions:getDirectory() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return byte
function IsoRegions:getSquareFlags(arg0, arg1, arg2) end

---@public
---@return File
function IsoRegions:getHeaderFile() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return IChunkRegion
function IsoRegions:getChunkRegion(arg0, arg1, arg2) end

---@public
---@return void
function IsoRegions:reset() end

---@public
---@param arg0 int
---@param arg1 int
---@return File
function IsoRegions:getChunkFile(arg0, arg1) end
