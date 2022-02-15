---@class ErosionMain : zombie.erosion.ErosionMain
---@field private instance ErosionMain
---@field private cfg ErosionConfig
---@field private debug boolean
---@field private sprMngr IsoSpriteManager
---@field private IceQueen ErosionIceQueen
---@field private isSnow boolean
---@field private world String
---@field private cfgPath String
---@field private chunk IsoChunk
---@field private chunkModData ErosionData.Chunk
---@field private noiseMain Noise2D
---@field private noiseMoisture Noise2D
---@field private noiseMinerals Noise2D
---@field private noiseKudzu Noise2D
---@field private World ErosionWorld
---@field private Season ErosionSeason
---@field private tickUnit int
---@field private ticks int
---@field private eTicks int
---@field private day int
---@field private month int
---@field private year int
---@field private epoch int
---@field private soilTable int[][]
---@field private snowFrac int
---@field private snowFracYesterday int
---@field private snowFracOnDay int[]
ErosionMain = {}

---@private
---@return boolean
function ErosionMain:initConfig() end

---@public
---@return int
function ErosionMain:getSnowFraction() end

---@public
---@return int
function ErosionMain:getEtick() end

---@public
---@return void
function ErosionMain:start() end

---@public
---@return boolean
function ErosionMain:isSnow() end

---@public
---@return ErosionSeason
function ErosionMain:getSeasons() end

---@public
---@param _chunk IsoChunk
---@return void
function ErosionMain:ChunkLoaded(_chunk) end

---@private
---@param arg0 IsoChunk
---@param arg1 ErosionData.Chunk
---@return void
function ErosionMain:initChunk(arg0, arg1) end

---@public
---@return ErosionConfig
function ErosionMain:getConfig() end

---@public
---@param bb ByteBuffer
---@return void
function ErosionMain:sendState(bb) end

---@private
---@param arg0 IsoGridSquare
---@return void
function ErosionMain:loadGridsquare(arg0) end

---@public
---@return void
function ErosionMain:EveryTenMinutes() end

---@public
---@param _sq IsoGridSquare
---@return void
function ErosionMain:LoadGridsquare(_sq) end

---@public
---@return void
function ErosionMain:DebugUpdateMapNow() end

---@private
---@param arg0 IsoChunk
---@return void
function ErosionMain:loadChunk(arg0) end

---@public
---@return void
function ErosionMain:snowCheck() end

---@private
---@param arg0 IsoGridSquare
---@return void
function ErosionMain:getChunk(arg0) end

---@private
---@param arg0 IsoGridSquare
---@param arg1 ErosionData.Square
---@return void
function ErosionMain:initGridSquare(arg0, arg1) end

---@private
---@return void
function ErosionMain:updateMapNow() end

---@public
---@return int
function ErosionMain:getSnowFractionYesterday() end

---@public
---@param bb ByteBuffer
---@return void
function ErosionMain:receiveState(bb) end

---@public
---@return void
function ErosionMain:Reset() end

---@public
---@return IsoSpriteManager
function ErosionMain:getSpriteManager() end

---@public
---@return void
function ErosionMain:mainTimer() end

---@public
---@return ErosionMain
function ErosionMain:getInstance() end
