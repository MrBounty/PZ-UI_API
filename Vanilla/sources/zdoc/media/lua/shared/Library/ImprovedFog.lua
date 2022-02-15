---@class ImprovedFog : zombie.iso.weather.fog.ImprovedFog
---@field private rectangleIter ImprovedFog.RectangleIterator
---@field private rectangleMatrixPos Vector2i
---@field private chunkMap IsoChunkMap
---@field private minY int
---@field private maxY int
---@field private minX int
---@field private maxX int
---@field private zLayer int
---@field private lastIterPos Vector2i
---@field private fogRectangle ImprovedFog.FogRectangle
---@field private drawingThisLayer boolean
---@field private ZOOM float
---@field private PlayerIndex int
---@field private playerRow int
---@field private screenWidth float
---@field private screenHeight float
---@field private worldOffsetX float
---@field private worldOffsetY float
---@field private topAlphaHeight float
---@field private bottomAlphaHeight float
---@field private secondLayerAlpha float
---@field private scalingX float
---@field private scalingY float
---@field private colorR float
---@field private colorG float
---@field private colorB float
---@field private drawDebugColors boolean
---@field private octaves float
---@field private highQuality boolean
---@field private enableEditing boolean
---@field private alphaCircleAlpha float
---@field private alphaCircleRad float
---@field private lastRow int
---@field private climateManager ClimateManager
---@field private noiseTexture Texture
---@field private renderOnlyOneRow boolean
---@field private baseAlpha float
---@field private renderEveryXRow int
---@field private renderXRowsFromCenter int
---@field private renderCurrentLayerOnly boolean
---@field private rightClickOffX float
---@field private rightClickOffY float
---@field private cameraOffscreenLeft float
---@field private cameraOffscreenTop float
---@field private cameraZoom float
---@field private minXOffset int
---@field private maxXOffset int
---@field private maxYOffset int
---@field private renderEndOnly boolean
---@field private fogIntensity SteppedUpdateFloat
---@field private keyPause int
---@field private offsets float[]
ImprovedFog = {}

---@public
---@param arg0 float
---@return void
function ImprovedFog:setColorG(arg0) end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setTopAlphaHeight(arg0) end

---@public
---@param arg0 int
---@return void
function ImprovedFog:setMinXOffset(arg0) end

---@public
---@param arg0 boolean
---@return void
function ImprovedFog:setEnableEditing(arg0) end

---@public
---@return boolean
function ImprovedFog:isRenderCurrentLayerOnly() end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setBaseAlpha(arg0) end

---@public
---@return float
function ImprovedFog:getColorB() end

---@public
---@return float
function ImprovedFog:getBaseAlpha() end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setScalingY(arg0) end

---@public
---@return float
function ImprovedFog:getBottomAlphaHeight() end

---@public
---@return boolean
function ImprovedFog:isDrawDebugColors() end

---@public
---@return boolean
function ImprovedFog:isEnableEditing() end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setColorR(arg0) end

---@public
---@return int
function ImprovedFog:getMinXOffset() end

---@public
---@param arg0 boolean
---@return void
function ImprovedFog:setHighQuality(arg0) end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setOctaves(arg0) end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setAlphaCircleAlpha(arg0) end

---@public
---@param arg0 boolean
---@return void
function ImprovedFog:setDrawDebugColors(arg0) end

---@public
---@return float
function ImprovedFog:getOctaves() end

---@public
---@param arg0 int
---@return void
function ImprovedFog:setRenderEveryXRow(arg0) end

---@public
---@return int
function ImprovedFog:getMaxXOffset() end

---@public
---@param arg0 int
---@return void
function ImprovedFog:setMaxXOffset(arg0) end

---@public
---@return float
function ImprovedFog:getAlphaCircleAlpha() end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function ImprovedFog:endFogRectangle(arg0, arg1, arg2) end

---@public
---@return void
function ImprovedFog:endRender() end

---@public
---@return int
function ImprovedFog:getRenderEveryXRow() end

---@public
---@return void
function ImprovedFog:update() end

---@public
---@return float
function ImprovedFog:getScalingX() end

---@public
---@return float
function ImprovedFog:getColorR() end

---@public
---@return float
function ImprovedFog:getScalingY() end

---@public
---@return boolean
function ImprovedFog:isRenderOnlyOneRow() end

---@public
---@return float
function ImprovedFog:getTopAlphaHeight() end

---@public
---@return void
function ImprovedFog:updateKeys() end

---@public
---@param arg0 boolean
---@return void
function ImprovedFog:setRenderEndOnly(arg0) end

---@public
---@param arg0 Texture
---@param arg1 double
---@param arg2 double
---@param arg3 double
---@param arg4 double
---@param arg5 double
---@param arg6 double
---@param arg7 double
---@param arg8 double
---@param arg9 double
---@param arg10 double
---@param arg11 double
---@param arg12 double
---@return void
function ImprovedFog:DrawSubTextureRGBA(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setBottomAlphaHeight(arg0) end

---@public
---@param arg0 boolean
---@return void
function ImprovedFog:setRenderOnlyOneRow(arg0) end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setColorB(arg0) end

---@public
---@return float
function ImprovedFog:getAlphaCircleRad() end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setSecondLayerAlpha(arg0) end

---@public
---@param arg0 boolean
---@return void
function ImprovedFog:setRenderCurrentLayerOnly(arg0) end

---@public
---@param arg0 int
---@return void
function ImprovedFog:setRenderXRowsFromCenter(arg0) end

---@public
---@return boolean
function ImprovedFog:isHighQuality() end

---@public
---@return float
function ImprovedFog:getSecondLayerAlpha() end

---@public
---@return boolean
function ImprovedFog:isRenderEndOnly() end

---@public
---@return float
function ImprovedFog:getColorG() end

---@public
---@return int
function ImprovedFog:getRenderXRowsFromCenter() end

---@private
---@return void
function ImprovedFog:renderFogSegment() end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function ImprovedFog:startRender(arg0, arg1) end

---@public
---@param arg0 IsoGridSquare
---@return void
function ImprovedFog:renderRowsBehind(arg0) end

---@public
---@param arg0 int
---@return void
function ImprovedFog:setMaxYOffset(arg0) end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setAlphaCircleRad(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function ImprovedFog:startFogRectangle(arg0, arg1, arg2) end

---@public
---@return int
function ImprovedFog:getMaxYOffset() end

---@public
---@param arg0 float
---@return void
function ImprovedFog:setScalingX(arg0) end
