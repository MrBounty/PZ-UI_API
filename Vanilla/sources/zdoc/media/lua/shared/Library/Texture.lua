---@class Texture : zombie.core.textures.Texture
---@field public nullTextures HashSet|Unknown
---@field private serialVersionUID long
---@field private objRen ObjectRenderEffects
---@field public BindCount int
---@field public bDoingQuad boolean
---@field public lr float
---@field public lg float
---@field public lb float
---@field public la float
---@field public lastlastTextureID int
---@field public totalTextureID int
---@field private white Texture
---@field private errorTexture Texture
---@field private mipmap Texture
---@field public lastTextureID int
---@field public WarnFailFindTexture boolean
---@field private textures HashMap|Unknown|Unknown
---@field private s_sharedTextureTable HashMap|Unknown|Unknown
---@field private steamAvatarMap HashMap|Unknown|Unknown
---@field public flip boolean
---@field public offsetX float
---@field public offsetY float
---@field public bindAlways boolean
---@field public xEnd float @internal texture coordinates
 it's used to get the max border of texture...
---@field public yEnd float @internal texture coordinates
 it's used to get the max border of texture...
---@field public xStart float @internal texture coordinates
 it's used to get the max border of texture...
---@field public yStart float @internal texture coordinates
 it's used to get the max border of texture...
---@field protected dataid TextureID
---@field protected mask Mask
---@field protected name String
---@field protected solid boolean
---@field protected width int
---@field protected height int
---@field protected heightOrig int
---@field protected widthOrig int
---@field private realWidth int
---@field private realHeight int
---@field private destroyed boolean
---@field private splitIconTex Texture
---@field private splitX int
---@field private splitY int
---@field private splitW int
---@field private splitH int
---@field protected subTexture FileSystem.SubTexture
---@field public assetParams Texture.TextureAssetParams
---@field private pngSize ThreadLocal|Unknown
---@field public ASSET_TYPE AssetType
Texture = {}

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@return void
function Texture:renderdiamond(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---Description copied from interface: ITexture
---
---returns the start Y-coordinate
---
---Specified by:
---
---getYStart in interface ITexture
---@public
---@return float @the start Y-coordinate
function Texture:getYStart() end

---@public
---@return String
function Texture:getName() end

---Description copied from interface: ITexture
---
---indicates if the texture is solid or not. a non solid texture is a texture that containe an alpha channel
---
---Specified by:
---
---isSolid in interface ITexture
---@public
---@return boolean @if the texture is solid or not.
function Texture:isSolid() end

---Description copied from interface: ITexture
---
---returns the end Y-coordinate
---
---Specified by:
---
---getYEnd in interface ITexture
---@public
---@return float @the end Y-coordinate
function Texture:getYEnd() end

---Description copied from interface: ITexture
---
---sets the region of the image
---
---Specified by:
---
---setRegion in interface ITexture
---@public
---@param x int @xstart position
---@param y int @ystart position
---@param width int @width of the region
---@param height int @height of the region
---@return void
function Texture:setRegion(x, y, width, height) end

---@public
---@param cache ByteBuffer
---@return void
function Texture:saveMaskRegion(cache) end

---@public
---@param other Texture
---@return boolean
function Texture:equals(other) end

---@public
---@param arg0 float
---@param arg1 float
---@return void
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float, arg4:float, arg5:float, arg6:float, arg7:float, arg8:Consumer|Unknown)
---@overload fun(arg0:ObjectRenderEffects, arg1:float, arg2:float, arg3:float, arg4:float, arg5:float, arg6:float, arg7:float, arg8:float, arg9:Consumer|Unknown)
function Texture:render(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function Texture:render(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 Consumer|Unknown
---@return void
function Texture:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param arg0 ObjectRenderEffects
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@param arg9 Consumer|Unknown
---@return void
function Texture:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@private
---@param arg0 String
---@param arg1 Texture
---@return void
function Texture:setSharedTextureInternal(arg0, arg1) end

---@public
---@param arg0 int
---@return void
function Texture:setOffsetY(arg0) end

---@private
---@param arg0 String
---@param arg1 int
---@return Texture
function Texture:getSharedTextureInternal(arg0, arg1) end

---gets a texture from it's name; If the texture isn't already loaded this method will load it.
---@public
---@param name String @the name of texture
---@return Texture @returns the texture from the given name
function Texture:getTexture(name) end

---@public
---@return int
function Texture:getWidthOrig() end

---Description copied from interface: ITexture
---
---sets transparent each pixel that it's equal to the red, green blue value specified
---
---Specified by:
---
---makeTransp in interface ITexture
---@public
---@param red int @color used in the test
---@param green int @color used in the test
---@param blue int @color used in the test
---@return void
function Texture:makeTransp(red, green, blue) end

---@public
---@param arg0 String
---@return void
function Texture:save(arg0) end

---@public
---@return Texture
function Texture:getWhite() end

---Description copied from interface: ITexture
---
---return the height hardware of image
---
---Specified by:
---
---getHeightHW in interface ITexture
---@public
---@return int
function Texture:getHeightHW() end

---@public
---@return void
function Texture:setCustomizedTexture() end

---@public
---@param xstep int[]
---@param ystep int[]
---@return Texture[][]
function Texture:split2D(xstep, ystep) end

---Description copied from interface: ITexture
---
---returns the ID of image in the Vram
---
---Specified by:
---
---getID in interface ITexture
---@public
---@return int @the ID of image in the Vram
function Texture:getID() end

---@public
---@return int
function Texture:getHeightOrig() end

---@public
---@param name String
---@return Texture
---@overload fun(name:String, palette:String)
---@overload fun(arg0:String, arg1:int)
---@overload fun(name:String, palette:int[], paletteName:String)
function Texture:getSharedTexture(name) end

---@public
---@param name String
---@param palette String
---@return Texture
function Texture:getSharedTexture(name, palette) end

---@public
---@param arg0 String
---@param arg1 int
---@return Texture
function Texture:getSharedTexture(arg0, arg1) end

---@public
---@param name String
---@param palette int[]
---@param paletteName String
---@return Texture
function Texture:getSharedTexture(name, palette, paletteName) end

---@private
---@param arg0 String
---@return void
function Texture:onTextureFileChanged(arg0) end

---returns the texture's pixel in a ByteBuffer
---
---
---
---EXAMPLE:
---
---ByteBuffer bb = getData();
---
---byte r, g, b;
---
---bb.rewind(); //<-- IMPORTANT!!
---
---try {
---
---while (true) {
---
---bb.mark();
---
---r = bb.get();
---
---g = bb.get();
---
---b = bb.get();
---
---bb.reset();
---
---bb.put((byte)(r+red));
---
---bb.put((byte)(g+green));
---
---bb.put((byte)(b+blue));
---
---bb.get(); // alpha
---
---
---
---catch (Exception e) {
---
---
---
---setData(bb);
---
---Specified by:
---
---getData in interface ITexture
---@public
---@return WrappedBuffer
function Texture:getData() end

---@public
---@return int
function Texture:getRealHeight() end

---creates the mask of collisions
---@public
---@return void
---@overload fun(arg0:BooleanGrid)
---@overload fun(buf:WrappedBuffer)
---@overload fun(mask:boolean[])
function Texture:createMask() end

---@public
---@param arg0 BooleanGrid
---@return void
function Texture:createMask(arg0) end

---@public
---@param buf WrappedBuffer
---@return void
function Texture:createMask(buf) end

---@public
---@param mask boolean[]
---@return void
function Texture:createMask(mask) end

---@public
---@return float
function Texture:getOffsetY() end

---@public
---@param xOffset int
---@param yOffset int
---@param width int
---@param height int
---@return Texture
---@overload fun(arg0:String, arg1:int, arg2:int, arg3:int, arg4:int)
---@overload fun(xOffset:int, yOffset:int, row:int, coloumn:int, width:int, height:int, spaceX:int, spaceY:int)
function Texture:split(xOffset, yOffset, width, height) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return Texture
function Texture:split(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param xOffset int
---@param yOffset int
---@param row int
---@param coloumn int
---@param width int
---@param height int
---@param spaceX int
---@param spaceY int
---@return Texture[]
function Texture:split(xOffset, yOffset, row, coloumn, width, height, spaceX, spaceY) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@param arg8 int
---@param arg9 int
---@return void
function Texture:renderwallnw(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@param width int
---@return void
function Texture:setWidth(width) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@return void
function Texture:renderwallw(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return AssetType
function Texture:getType() end

---@public
---@param arg0 String
---@return void
function Texture:reload(arg0) end

---@public
---@param from Texture
---@param x int
---@param y int
---@param width int
---@param height int
---@return void
function Texture:copyMaskRegion(from, x, y, width, height) end

---sets the texture's pixel from a ByteBuffer
---
---
---
---EXAMPLE:
---
---ByteBuffer bb = getData();
---
---byte r, g, b;
---
---bb.rewind(); //<-- IMPORTANT!!
---
---try {
---
---while (true) {
---
---bb.mark();
---
---r = bb.get();
---
---g = bb.get();
---
---b = bb.get();
---
---bb.reset();
---
---bb.put((byte)(r+red));
---
---bb.put((byte)(g+green));
---
---bb.put((byte)(b+blue));
---
---bb.get(); // alpha
---
---
---
---catch (Exception e) {
---
---
---
---setData(bb);
---
---Specified by:
---
---setData in interface ITexture
---@public
---@param data ByteBuffer @texture's pixel data
---@return void
function Texture:setData(data) end

---@public
---@return Texture
function Texture:getErrorTexture() end

---@public
---@return float
function Texture:getOffsetX() end

---@public
---@param name String
---@return void
function Texture:saveMask(name) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@return void
function Texture:renderwalln(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---Description copied from interface: ITexture
---
---returns the height of image
---
---Specified by:
---
---getHeight in interface ITexture
---@public
---@return int @the height of image
function Texture:getHeight() end

---@public
---@return Texture
function Texture:splitIcon() end

---destroys the image and release all resources
---
---Specified by:
---
---destroy in interface IDestroyable
---@public
---@return void
function Texture:destroy() end

---@public
---@return boolean
function Texture:isValid() end

---returns if the texture is destroyed or not
---
---Specified by:
---
---isDestroyed in interface IDestroyable
---@public
---@return boolean
function Texture:isDestroyed() end

---@public
---@param arg0 int
---@return void
function Texture:setOffsetX(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 Consumer|Unknown
---@return void
function Texture:renderstrip(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param realWidth int
---@return void
function Texture:setRealWidth(realWidth) end

---Blinds the image
---
---Specified by:
---
---bind in interface ITexture
---@public
---@return void
---@overload fun(unit:int)
function Texture:bind() end

---Description copied from interface: ITexture
---
---bind the current texture object in the specified texture unit
---
---Specified by:
---
---bind in interface ITexture
---@public
---@param unit int @the texture unit in witch the current TextureObject will be binded
---@return void
function Texture:bind(unit) end

---Overrides:
---
---toString in class java.lang.Object
---@public
---@return String
function Texture:toString() end

---@public
---@return void
function Texture:onTexturePacksChanged() end

---@public
---@param arg0 long
---@return Texture
function Texture:getSteamAvatar(arg0) end

---Description copied from interface: ITexture
---
---sets the specified alpha for each pixel that it's equal to the red, green blue value specified
---
---Specified by:
---
---setAlphaForeach in interface ITexture
---@public
---@param red int @color used in the test
---@param green int @color used in the test
---@param blue int @color used in the test
---@param alpha int @the alpha color that will be setted to the pixel that pass the test
---@return void
function Texture:setAlphaForeach(red, green, blue, alpha) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@param arg8 float
---@param arg9 float
---@param arg10 float
---@param arg11 float
---@return void
function Texture:rendershader2(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) end

---@public
---@return void
function Texture:clearTextures() end

---@public
---@param name String
---@return void
function Texture:forgetTexture(name) end

---@public
---@return void
function Texture:bindNone() end

---@public
---@param arg0 int[]
---@param arg1 int
---@param arg2 int
---@return int[]
function Texture:flipPixels(arg0, arg1, arg2) end

---@public
---@return void
function Texture:onBeforeReady() end

---Description copied from interface: ITexture
---
---returns the start X-coordinate
---
---Specified by:
---
---getXStart in interface ITexture
---@public
---@return float @the start X-coordinate
function Texture:getXStart() end

---@public
---@param realHeight int
---@return void
function Texture:setRealHeight(realHeight) end

---returns the mask of collisions
---
---Specified by:
---
---getMask in interface IMaskerable
---@public
---@return Mask @mask of collisions
function Texture:getMask() end

---Description copied from interface: ITexture
---
---returns the width of image
---
---Specified by:
---
---getWidth in interface ITexture
---@public
---@return int @the width of image
function Texture:getWidth() end

---@public
---@param arg0 String
---@return String
function Texture:processFilePath(arg0) end

---indicates if the image will use the alpha channel or note
---@public
---@return boolean @if the image will use the alpha channel or note
function Texture:getUseAlphaChannel() end

---@public
---@param name String
---@return Texture
function Texture:trygetTexture(name) end

---@public
---@param arg0 String
---@return void
function Texture:reloadFromFile(arg0) end

---@public
---@param name String
---@return void
function Texture:setName(name) end

---indicates if the texture has a mask of collisions or not
---@public
---@return boolean
function Texture:isCollisionable() end

---@public
---@param cache ByteBuffer
---@return void
function Texture:loadMaskRegion(cache) end

---@public
---@return int
function Texture:getRealWidth() end

---@public
---@return Texture
function Texture:getEngineMipmapTexture() end

---sets the mask of collisions
---
---Specified by:
---
---setMask in interface ITexture
---@public
---@param mask Mask @the mask of collisions to set
---@return void
function Texture:setMask(mask) end

---@private
---@return void
function Texture:syncReadSize() end

---indicates if the texture contains the alpha channel or not
---@public
---@param value boolean @if true, the image will use the alpha channel
---@return void
function Texture:setUseAlphaChannel(value) end

---@public
---@param height int
---@return void
function Texture:setHeight(height) end

---@public
---@param arg0 HashMap|Unknown|Unknown
---@param arg1 HashMap|Unknown|Unknown
---@return void
function Texture:collectAllIcons(arg0, arg1) end

---@public
---@return TextureID
function Texture:getTextureId() end

---@public
---@param arg0 JVector2
---@return JVector2
function Texture:getUVScale(arg0) end

---Description copied from interface: ITexture
---
---return the width Hardware of image
---
---Specified by:
---
---getWidthHW in interface ITexture
---@public
---@return int
function Texture:getWidthHW() end

---@public
---@param arg0 long
---@return void
function Texture:steamAvatarChanged(arg0) end

---Description copied from interface: ITexture
---
---returns the end X-coordinate
---
---Specified by:
---
---getXEnd in interface ITexture
---@public
---@return float @the end X-coordinate
function Texture:getXEnd() end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@return void
function Texture:initEngineMipmapTextureLevel(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 String
---@return void
function Texture:setNameOnly(arg0) end
