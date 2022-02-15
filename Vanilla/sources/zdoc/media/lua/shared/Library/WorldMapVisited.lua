---@class WorldMapVisited : zombie.worldMap.WorldMapVisited
---@field private instance WorldMapVisited
---@field private m_minX int
---@field private m_minY int
---@field private m_maxX int
---@field private m_maxY int
---@field m_visited byte[]
---@field m_changed boolean
---@field m_changeX1 int
---@field m_changeY1 int
---@field m_changeX2 int
---@field m_changeY2 int
---@field private m_updateMinX int[]
---@field private m_updateMinY int[]
---@field private m_updateMaxX int[]
---@field private m_updateMaxY int[]
---@field private TEXTURE_BPP int
---@field private m_textureID TextureID
---@field private m_textureW int
---@field private m_textureH int
---@field private m_textureBuffer ByteBuffer
---@field private m_textureChanged boolean
---@field private m_color WorldMapStyleLayer.RGBAf
---@field private m_gridColor WorldMapStyleLayer.RGBAf
---@field private m_mainMenu boolean
---@field private m_shaderProgram ShaderProgram
---@field private m_gridShaderProgram ShaderProgram
---@field UNITS_PER_CELL int
---@field SQUARES_PER_CELL int
---@field SQUARES_PER_UNIT int
---@field TEXTURE_PAD int
---@field BIT_VISITED int
---@field BIT_KNOWN int
---@field m_vector2 JVector2
WorldMapVisited = {}

---@public
---@return void
---@overload fun(arg0:ByteBuffer)
function WorldMapVisited:save() end

---@public
---@param arg0 ByteBuffer
---@return void
function WorldMapVisited:save(arg0) end

---@private
---@param arg0 ByteBuffer
---@param arg1 int
---@return boolean
function WorldMapVisited:updateTextureData(arg0, arg1) end

---@public
---@return void
---@overload fun(arg0:ByteBuffer, arg1:int)
function WorldMapVisited:load() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function WorldMapVisited:load(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function WorldMapVisited:clearKnownInSquares(arg0, arg1, arg2, arg3) end

---@private
---@return int
function WorldMapVisited:calcTextureWidth() end

---@public
---@return int
function WorldMapVisited:getMinX() end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return void
function WorldMapVisited:clearFlags(arg0, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return void
function WorldMapVisited:setFlags(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 float
---@param arg7 float
---@return void
function WorldMapVisited:renderGrid(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@private
---@return int
function WorldMapVisited:calcTextureHeight() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 float
---@param arg7 boolean
---@return void
function WorldMapVisited:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function WorldMapVisited:setVisitedInCells(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function WorldMapVisited:clearVisitedInSquares(arg0, arg1, arg2, arg3) end

---@param arg0 int
---@param arg1 int
---@return boolean
function WorldMapVisited:isCellVisible(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function WorldMapVisited:setBounds(arg0, arg1, arg2, arg3) end

---@public
---@return int
function WorldMapVisited:getMinY() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function WorldMapVisited:clearVisitedInCells(arg0, arg1, arg2, arg3) end

---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function WorldMapVisited:setUnvisitedRGBA(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function WorldMapVisited:setVisitedInSquares(arg0, arg1, arg2, arg3) end

---@private
---@return void
function WorldMapVisited:destroy() end

---@private
---@return int
function WorldMapVisited:getWidthInCells() end

---@public
---@return void
function WorldMapVisited:Reset() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function WorldMapVisited:setKnownInCells(arg0, arg1, arg2, arg3) end

---@public
---@return WorldMapVisited
function WorldMapVisited:getInstance() end

---@public
---@return void
function WorldMapVisited:update() end

---@private
---@return int
function WorldMapVisited:getHeightInCells() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function WorldMapVisited:clearKnownInCells(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return void
function WorldMapVisited:setKnownInSquares(arg0, arg1, arg2, arg3) end

---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function WorldMapVisited:setUnvisitedGridRGBA(arg0, arg1, arg2, arg3) end

---@private
---@return void
function WorldMapVisited:initShader() end

---@public
---@return void
function WorldMapVisited:SaveAll() end

---@public
---@return void
function WorldMapVisited:renderMain() end

---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 boolean
---@return boolean
function WorldMapVisited:hasFlags(arg0, arg1, arg2, arg3, arg4, arg5) end

---@private
---@return void
function WorldMapVisited:updateVisitedTexture() end
