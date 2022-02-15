---@class IsoObject : zombie.iso.IsoObject
---@field public OBF_Highlighted byte
---@field public OBF_HighlightRenderOnce byte
---@field public OBF_Blink byte
---@field public MAX_WALL_SPLATS int
---@field private PropMoveWithWind String
---@field public lastRendered IsoObject
---@field public lastRenderedRendered IsoObject
---@field public stCol ColorInfo
---@field public rmod float
---@field public gmod float
---@field public bmod float
---@field public LowLightingQualityHack boolean
---@field private DefaultCondition int
---@field private stCol2 ColorInfo
---@field private colFxMask ColorInfo
---@field public highlightFlags byte
---@field public keyId int
---@field public emitter BaseSoundEmitter
---@field public sheetRopeHealth float
---@field public sheetRope boolean
---@field public bNeverDoneAlpha boolean
---@field public bAlphaForced boolean
---@field public AttachedAnimSprite ArrayList|IsoSpriteInstance
---@field public wallBloodSplats ArrayList|IsoWallBloodSplat
---@field public container ItemContainer
---@field public dir IsoDirections
---@field public Damage short
---@field public partialThumpDmg float
---@field public NoPicking boolean
---@field public offsetX float
---@field public offsetY float
---@field public OutlineOnMouseover boolean
---@field public rerouteMask IsoObject
---@field public sprite IsoSprite
---@field public overlaySprite IsoSprite
---@field public overlaySpriteColor ColorInfo
---@field public square IsoGridSquare
---@field public alpha float[]
---@field public targetAlpha float[]
---@field public rerouteCollide IsoObject
---@field public _table KahluaTable
---@field public name String
---@field public tintr float
---@field public tintg float
---@field public tintb float
---@field public spriteName String
---@field public sx float
---@field public sy float
---@field public doNotSync boolean
---@field protected windRenderEffects ObjectRenderEffects
---@field protected objectRenderEffects ObjectRenderEffects
---@field protected externalWaterSource IsoObject
---@field protected usesExternalWaterSource boolean
---@field Children ArrayList|Unknown
---@field tile String
---@field private specialTooltip boolean
---@field private highlightColor ColorInfo
---@field private secondaryContainers ArrayList|Unknown
---@field private customColor ColorInfo
---@field private renderYOffset float
---@field protected isOutlineHighlight byte
---@field protected isOutlineHlAttached byte
---@field protected isOutlineHlBlink byte
---@field protected outlineHighlightCol int[]
---@field private outlineThickness float
---@field protected bMovedThumpable boolean
---@field private byteToObjectMap Map|Unknown|Unknown
---@field private hashCodeToObjectMap Map|Unknown|Unknown
---@field private nameToObjectMap Map|Unknown|Unknown
---@field private factoryIsoObject IsoObject.IsoObjectFactory
---@field private factoryVehicle IsoObject.IsoObjectFactory
IsoObject = {}

---@public
---@return boolean
---@overload fun(arg0:int)
function IsoObject:isAlphaAndTargetZero() end

---@public
---@param arg0 int
---@return boolean
function IsoObject:isAlphaAndTargetZero(arg0) end

---@public
---@return boolean
---@overload fun(arg0:int)
function IsoObject:isOutlineHighlight() end

---@public
---@param arg0 int
---@return boolean
function IsoObject:isOutlineHighlight(arg0) end

---@public
---@param arg0 boolean
---@return void
function IsoObject:setTaintedWater(arg0) end

---throws java.io.IOException
---@public
---@param cell IsoCell
---@param b ByteBuffer
---@return IsoObject
---@overload fun(arg0:IsoCell, arg1:byte)
---@overload fun(arg0:IsoCell, arg1:DataInputStream)
function IsoObject:factoryFromFileInput(cell, b) end

---@public
---@param arg0 IsoCell
---@param arg1 byte
---@return IsoObject
function IsoObject:factoryFromFileInput(arg0, arg1) end

---@param arg0 IsoCell
---@param arg1 DataInputStream
---@return IsoObject
function IsoObject:factoryFromFileInput(arg0, arg1) end

---@public
---@param name String
---@return void
---@overload fun(sprite:IsoSprite)
function IsoObject:setSprite(name) end

---@public
---@param sprite IsoSprite @the sprite to set
---@return void
function IsoObject:setSprite(sprite) end

---@protected
---@return void
---@overload fun(arg0:boolean)
function IsoObject:checkMoveWithWind() end

---@protected
---@param arg0 boolean
---@return void
function IsoObject:checkMoveWithWind(arg0) end

---@public
---@return void
---@overload fun(connection:UdpConnection)
function IsoObject:transmitUpdatedSpriteToClients() end

---@public
---@param connection UdpConnection
---@return void
function IsoObject:transmitUpdatedSpriteToClients(connection) end

---@public
---@param aLastRenderedRendered IsoObject @the lastRenderedRendered to set
---@return void
function IsoObject:setLastRenderedRendered(aLastRenderedRendered) end

---@public
---@return IsoObject.IsoObjectFactory
function IsoObject:getFactoryVehicle() end

---@public
---@return int
function IsoObject:getStaticMovingObjectIndex() end

---@public
---@param arg0 IsoGridSquare
---@return IsoObject
---@overload fun(arg0:int, arg1:int, arg2:int)
function IsoObject:FindExternalWaterSource(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return IsoObject
function IsoObject:FindExternalWaterSource(arg0, arg1, arg2) end

---@public
---@return IsoObject @the rerouteCollide
function IsoObject:getRerouteCollide() end

---@public
---@return boolean
function IsoObject:haveSpecialTooltip() end

---@public
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return IsoObject.VisionResult
function IsoObject:TestVision(from, to) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function IsoObject:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return float
function IsoObject:getSurfaceOffsetNoTable() end

---@public
---@param arg0 RenderEffectType
---@return void
---@overload fun(arg0:RenderEffectType, arg1:boolean)
function IsoObject:setRenderEffect(arg0) end

---@public
---@param arg0 RenderEffectType
---@param arg1 boolean
---@return void
function IsoObject:setRenderEffect(arg0, arg1) end

---@public
---@return boolean
function IsoObject:isHoppable() end

---@public
---@param Damage short @the Damage to set
---@return void
function IsoObject:setDamage(Damage) end

---@public
---@param arg0 boolean
---@param arg1 byte
---@param arg2 UdpConnection
---@param arg3 ByteBuffer
---@return void
function IsoObject:syncIsoObject(arg0, arg1, arg2, arg3) end

---@private
---@return IsoObject
function IsoObject:checkExternalWaterSource() end

---@public
---@return void
function IsoObject:removeFromSquare() end

---@public
---@return int
function IsoObject:getContainerCount() end

---@public
---@param arg0 IsoGameCharacter
---@return Thumpable
function IsoObject:getThumpableFor(arg0) end

---@public
---@return boolean
---@overload fun(arg0:int)
function IsoObject:isAlphaZero() end

---@public
---@param arg0 int
---@return boolean
function IsoObject:isAlphaZero(arg0) end

---@public
---@param arg0 float
---@return void
---@overload fun(arg0:int, arg1:float)
function IsoObject:setAlphaAndTarget(arg0) end

---@public
---@param arg0 int
---@param arg1 float
---@return void
function IsoObject:setAlphaAndTarget(arg0, arg1) end

---@public
---@param index int
---@return void
function IsoObject:RemoveAttachedAnim(index) end

---@public
---@param change String
---@param tbl KahluaTable
---@param bb ByteBuffer
---@return void
function IsoObject:saveChange(change, tbl, bb) end

---@public
---@param aLastRendered IsoObject @the lastRendered to set
---@return void
function IsoObject:setLastRendered(aLastRendered) end

---@public
---@param highlightColor ColorInfo
---@return void
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function IsoObject:setHighlightColor(highlightColor) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function IsoObject:setHighlightColor(arg0, arg1, arg2, arg3) end

---@public
---@param lx int
---@param ly int
---@return void
function IsoObject:onMouseRightClick(lx, ly) end

---@public
---@return int
function IsoObject:getPipedFuelAmount() end

---@public
---@param x int
---@param y int
---@param flip boolean
---@return float
function IsoObject:getMaskClickedY(x, y, flip) end

---@public
---@param highlight boolean
---@return void
---@overload fun(arg0:boolean, arg1:boolean)
function IsoObject:setHighlighted(highlight) end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@return void
function IsoObject:setHighlighted(arg0, arg1) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@param arg7 Consumer|Unknown
---@return void
function IsoObject:renderAttachedAndOverlaySpritesInternal(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@return String
function IsoObject:getObjectName() end

---@public
---@param arg0 int
---@return ItemContainer
function IsoObject:getContainerByIndex(arg0) end

---@protected
---@return boolean
function IsoObject:isUpdateAlphaDuringRender() end

---@public
---@param b ByteBuffer
---@return void
---@overload fun(b:ByteBuffer, addToObjects:boolean)
function IsoObject:loadFromRemoteBuffer(b) end

---@public
---@param b ByteBuffer
---@param addToObjects boolean
---@return void
function IsoObject:loadFromRemoteBuffer(b, addToObjects) end

---@public
---@param item InventoryItem
---@return InventoryItem
function IsoObject:replaceItem(item) end

---@public
---@return float
function IsoObject:getRenderYOffset() end

---@public
---@return void
function IsoObject:reset() end

---@public
---@param pos JVector2
---@return JVector2
function IsoObject:getFacingPosition(pos) end

---@public
---@return ObjectRenderEffects
function IsoObject:getObjectRenderEffects() end

---@public
---@return boolean
function IsoObject:isTaintedWater() end

---@protected
---@return boolean
function IsoObject:isUpdateAlphaEnabled() end

---@public
---@param arg0 ItemContainer
---@param arg1 InventoryItem
---@return boolean
function IsoObject:isRemoveItemAllowedFromContainer(arg0, arg1) end

---@public
---@return void
function IsoObject:DirtySlice() end

---@public
---@return int
---@overload fun(arg0:int)
function IsoObject:getOutlineHighlightCol() end

---@public
---@param arg0 int
---@return int
function IsoObject:getOutlineHighlightCol(arg0) end

---@public
---@return int
function IsoObject:getSpecialObjectIndex() end

---@public
---@return void
function IsoObject:checkHaveElectricity() end

---@public
---@param arg0 String
---@return void
function IsoObject:setSpriteFromName(arg0) end

---@public
---@param blink boolean
---@return void
function IsoObject:setBlink(blink) end

---@public
---@param item InventoryItem
---@return void
function IsoObject:useItemOn(item) end

---@public
---@param arg0 int
---@return void
function IsoObject:setPipedFuelAmount(arg0) end

---@public
---@return void
function IsoObject:cleanWallBlood() end

---@protected
---@return float
function IsoObject:getAlphaUpdateRateMul() end

---@public
---@param arg0 boolean
---@return void
---@overload fun(arg0:int, arg1:boolean)
function IsoObject:setOutlineHighlight(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function IsoObject:setOutlineHighlight(arg0, arg1) end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@return boolean
function IsoObject:addSheetRope(arg0, arg1) end

---@public
---@param alpha float @the alpha to set
---@return void
---@overload fun(arg0:int, arg1:float)
function IsoObject:setAlpha(alpha) end

---@public
---@param arg0 int
---@param arg1 float
---@return void
function IsoObject:setAlpha(arg0, arg1) end

---@public
---@param x float
---@param y float
---@param z float
---@param lightInfo ColorInfo
---@return void
function IsoObject:renderObjectPicker(x, y, z, lightInfo) end

---@public
---@param arg0 JVector2
---@return JVector2
function IsoObject:getFacingPositionAlt(arg0) end

---@public
---@param arg0 ColorInfo
---@return void
---@overload fun(arg0:int, arg1:ColorInfo)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
---@overload fun(arg0:int, arg1:float, arg2:float, arg3:float, arg4:float)
function IsoObject:setOutlineHighlightCol(arg0) end

---@public
---@param arg0 int
---@param arg1 ColorInfo
---@return void
function IsoObject:setOutlineHighlightCol(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function IsoObject:setOutlineHighlightCol(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@return void
function IsoObject:setOutlineHighlightCol(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 ByteBuffer
---@return void
function IsoObject:saveState(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 boolean
---@return void
function IsoObject:renderFxMask(arg0, arg1, arg2, arg3) end

---@public
---@return boolean
function IsoObject:isNorthHoppable() end

---@public
---@return boolean
function IsoObject:isStairsWest() end

---@public
---@param arg0 ByteBuffer
---@return void
---@overload fun(arg0:ByteBuffer, arg1:boolean)
function IsoObject:save(arg0) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function IsoObject:save(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function IsoObject:Damage(arg0) end

---@public
---@return int
function IsoObject:getMovingObjectIndex() end

---@public
---@param arg0 float
---@return void
function IsoObject:setOutlineThickness(arg0) end

---@public
---@return boolean
function IsoObject:isStairsObject() end

---@public
---@return boolean
function IsoObject:isCharacter() end

---@public
---@return void
function IsoObject:reuseGridSquare() end

---@public
---@return boolean
function IsoObject:canAddSheetRope() end

---@protected
---@return void
---@overload fun(arg0:int)
---@overload fun(arg0:int, arg1:float, arg2:float)
function IsoObject:updateAlpha() end

---@protected
---@param arg0 int
---@return void
function IsoObject:updateAlpha(arg0) end

---@protected
---@param arg0 int
---@param arg1 float
---@param arg2 float
---@return void
function IsoObject:updateAlpha(arg0, arg1, arg2) end

---@public
---@return float
function IsoObject:getX() end

---@public
---@param type IsoObjectType
---@return void
function IsoObject:setType(type) end

---@public
---@return boolean
---@overload fun(arg0:int)
function IsoObject:isOutlineHlBlink() end

---@public
---@param arg0 int
---@return boolean
function IsoObject:isOutlineHlBlink(arg0) end

---@public
---@return float
---@overload fun(arg0:int)
function IsoObject:getTargetAlpha() end

---@public
---@param arg0 int
---@return float
function IsoObject:getTargetAlpha(arg0) end

---@public
---@return ObjectRenderEffects
function IsoObject:getObjectRenderEffectsToApply() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@param arg7 Consumer|Unknown
---@return void
function IsoObject:renderAttachedAndOverlaySprites(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param ObjectName String
---@param AnimName String
---@param NumFrames int
---@param frameIncrease float
---@param OffsetX int
---@param OffsetY int
---@param Looping boolean
---@param FinishHoldFrameIndex int
---@param DeleteWhenFinished boolean
---@param zBias float
---@param TintMod ColorInfo
---@return void
function IsoObject:AttachAnim(ObjectName, AnimName, NumFrames, frameIncrease, OffsetX, OffsetY, Looping, FinishHoldFrameIndex, DeleteWhenFinished, zBias, TintMod) end

---@public
---@return boolean
function IsoObject:isTableTopObject() end

---@public
---@param arg0 ItemContainer
---@return int
function IsoObject:getContainerIndex(arg0) end

---@public
---@param targetAlpha float @the targetAlpha to set
---@return void
---@overload fun(arg0:int, arg1:float)
function IsoObject:setTargetAlpha(targetAlpha) end

---@public
---@param arg0 int
---@param arg1 float
---@return void
function IsoObject:setTargetAlpha(arg0, arg1) end

---@public
---@return boolean
function IsoObject:isZombie() end

---@public
---@param arg0 String
---@return byte
function IsoObject:factoryGetClassID(arg0) end

---@public
---@param x int
---@param y int
---@return boolean
---@overload fun(x:int, y:int, flip:boolean)
function IsoObject:isMaskClicked(x, y) end

---@public
---@param x int
---@param y int
---@param flip boolean
---@return boolean
function IsoObject:isMaskClicked(x, y, flip) end

---@public
---@param arg0 ObjectRenderEffects
---@return void
function IsoObject:removeRenderEffect(arg0) end

---@public
---@return void
function IsoObject:transmitCompleteItemToServer() end

---@protected
---@return float
function IsoObject:getAlphaUpdateRateDiv() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
---@overload fun(arg0:ByteBuffer, arg1:int, arg2:boolean)
function IsoObject:load(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function IsoObject:load(arg0, arg1, arg2) end

---@public
---@return void
function IsoObject:transmitModData() end

---@public
---@return void
function IsoObject:createContainersFromSpriteProperties() end

---@public
---@return boolean
function IsoObject:isDestroyed() end

---@public
---@return ColorInfo
function IsoObject:getCustomColor() end

---@public
---@return float
function IsoObject:getOutlineThickness() end

---@public
---@param child IsoObject
---@return void
function IsoObject:addChild(child) end

---@public
---@return String
function IsoObject:getName() end

---@public
---@return void
function IsoObject:removeFromWorld() end

---@public
---@return ItemContainer @the container
function IsoObject:getContainer() end

---@public
---@return KahluaTable @the table
function IsoObject:getTable() end

---@public
---@return boolean @the OutlineOnMouseover
function IsoObject:isOutlineOnMouseover() end

---@public
---@return void
function IsoObject:transmitCompleteItemToClients() end

---@public
---@param obj IsoMovingObject
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return boolean
function IsoObject:TestPathfindCollide(obj, from, to) end

---@public
---@param arg0 IsoPlayer
---@return boolean
function IsoObject:removeSheetRope(arg0) end

---@public
---@return boolean
function IsoObject:haveSheetRope() end

---@public
---@param arg0 BaseVehicle
---@param arg1 float
---@return void
function IsoObject:HitByVehicle(arg0, arg1) end

---@public
---@return IsoObject @the lastRenderedRendered
function IsoObject:getLastRenderedRendered() end

---@public
---@return boolean
function IsoObject:isMovedThumpable() end

---@public
---@return ObjectRenderEffects
function IsoObject:getWindRenderEffects() end

---@public
---@param offsetX float @the offsetX to set
---@return void
function IsoObject:setOffsetX(offsetX) end

---@public
---@param arg0 String
---@param arg1 String
---@return ItemContainer
function IsoObject:getContainerByEitherType(arg0, arg1) end

---@public
---@return ColorInfo
function IsoObject:getOverlaySpriteColor() end

---@public
---@param arg0 IsoGridSquare
---@return IsoObject
function IsoObject:FindWaterSourceOnSquare(arg0) end

---@private
---@param arg0 ColorInfo
---@return void
function IsoObject:prepareToRender(arg0) end

---@public
---@return KahluaTable
function IsoObject:getModData() end

---@public
---@return int
function IsoObject:getWaterMax() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 Shader
---@param arg5 Consumer|Unknown
---@return void
function IsoObject:renderWallTileOnly(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param col ColorInfo
---@return void
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:float)
function IsoObject:setCustomColor(col) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return void
function IsoObject:setCustomColor(arg0, arg1, arg2, arg3) end

---@public
---@return boolean
function IsoObject:getUsesExternalWaterSource() end

---@public
---@return void
function IsoObject:onMouseRightReleased() end

---@return Texture
function IsoObject:getCurrentFrameTex() end

---@public
---@return IsoObjectType @the type
function IsoObject:getType() end

---@public
---@param rerouteCollide IsoObject @the rerouteCollide to set
---@return void
function IsoObject:setRerouteCollide(rerouteCollide) end

---@public
---@param AttachedAnimSprite ArrayList|IsoSpriteInstance @the AttachedAnimSprite to set
---@return void
function IsoObject:setChildSprites(AttachedAnimSprite) end

---@public
---@param dir IsoDirections @the dir to set
---@return void
---@overload fun(dir:int)
function IsoObject:setDir(dir) end

---@public
---@param dir int @the dir to set
---@return void
function IsoObject:setDir(dir) end

---@public
---@param arg0 String
---@return ItemContainer
function IsoObject:getContainerByType(arg0) end

---@public
---@return int
function IsoObject:getWorldObjectIndex() end

---@public
---@return boolean
function IsoObject:isHighlighted() end

---@public
---@return boolean
function IsoObject:isTableSurface() end

---@public
---@return int
function IsoObject:getObjectIndex() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@param arg7 Consumer|Unknown
---@return void
function IsoObject:renderWallTile(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7) end

---@public
---@param arg0 float
---@return void
function IsoObject:setRenderYOffset(arg0) end

---@public
---@param arg0 boolean
---@return void
function IsoObject:setMovedThumpable(arg0) end

---@public
---@param spr IsoSprite
---@param OffsetX int
---@param OffsetY int
---@param Looping boolean
---@param FinishHoldFrameIndex int
---@param DeleteWhenFinished boolean
---@param zBias float
---@return void
---@overload fun(spr:IsoSprite, OffsetX:int, OffsetY:int, Looping:boolean, FinishHoldFrameIndex:int, DeleteWhenFinished:boolean, zBias:float, TintMod:ColorInfo)
function IsoObject:AttachExistingAnim(spr, OffsetX, OffsetY, Looping, FinishHoldFrameIndex, DeleteWhenFinished, zBias) end

---@public
---@param spr IsoSprite
---@param OffsetX int
---@param OffsetY int
---@param Looping boolean
---@param FinishHoldFrameIndex int
---@param DeleteWhenFinished boolean
---@param zBias float
---@param TintMod ColorInfo
---@return void
function IsoObject:AttachExistingAnim(spr, OffsetX, OffsetY, Looping, FinishHoldFrameIndex, DeleteWhenFinished, zBias, TintMod) end

---@public
---@return void
function IsoObject:debugPrintout() end

---@public
---@return boolean
function IsoObject:hasWater() end

---@public
---@return float
function IsoObject:getY() end

---@public
---@return float
function IsoObject:getOffsetX() end

---@public
---@param obj IsoMovingObject
---@param from IsoGridSquare
---@param to IsoGridSquare
---@return boolean
function IsoObject:TestCollide(obj, from, to) end

---@public
---@return void
function IsoObject:update() end

---@public
---@return int
function IsoObject:getKeyId() end

---@public
---@return String
function IsoObject:getScriptName() end

---@public
---@return void
function IsoObject:addToWorld() end

---@public
---@return void
function IsoObject:clearAttachedAnimSprite() end

---@public
---@return boolean
function IsoObject:isBlink() end

---@public
---@param arg0 int
---@return boolean
function IsoObject:isTargetAlphaZero(arg0) end

---@public
---@param x int
---@param y int
---@return boolean
function IsoObject:onMouseLeftClick(x, y) end

---@public
---@return IsoObject
---@overload fun(arg0:IsoGridSquare, arg1:String, arg2:String, arg3:boolean)
function IsoObject:getNew() end

---@public
---@param arg0 IsoGridSquare
---@param arg1 String
---@param arg2 String
---@param arg3 boolean
---@return IsoObject
function IsoObject:getNew(arg0, arg1, arg2, arg3) end

---@public
---@return void
function IsoObject:removeAllContainers() end

---@public
---@param name String
---@return void
function IsoObject:SetName(name) end

---@public
---@return float
function IsoObject:getSurfaceNormalOffset() end

---@public
---@param keyId int
---@return void
function IsoObject:setKeyId(keyId) end

---@private
---@return boolean
function IsoObject:shouldDrawMainSprite() end

---@public
---@param arg0 ByteBufferWriter
---@return void
function IsoObject:syncIsoObjectSend(arg0) end

---@public
---@param arg0 BaseVehicle
---@return float
function IsoObject:GetVehicleSlowFactor(arg0) end

---@public
---@param owner IsoGameCharacter
---@return void
function IsoObject:AttackObject(owner) end

---@public
---@param i int
---@return void
function IsoObject:setDefaultCondition(i) end

---@public
---@return ItemContainer
function IsoObject:getItemContainer() end

---@public
---@return boolean
function IsoObject:isStairsNorth() end

---@public
---@return IsoObject @the lastRendered
function IsoObject:getLastRendered() end

---@public
---@return int
function IsoObject:countAddSheetRope() end

---@public
---@return boolean
function IsoObject:isExistInTheWorld() end

---@public
---@return IsoObject
function IsoObject:getRenderEffectMaster() end

---@public
---@param rerouteMask IsoObject @the rerouteMask to set
---@return void
function IsoObject:setRerouteMask(rerouteMask) end

---@public
---@param r float
---@param g float
---@param b float
---@param a float
---@return void
function IsoObject:setOverlaySpriteColor(r, g, b, a) end

---@public
---@return ArrayList|IsoSpriteInstance @the AttachedAnimSprite
function IsoObject:getChildSprites() end

---@public
---@param name String @the name to set
---@return void
function IsoObject:setName(name) end

---@public
---@return IsoDirections @the dir
function IsoObject:getDir() end

---@public
---@param offsetY float @the offsetY to set
---@return void
function IsoObject:setOffsetY(offsetY) end

---@public
---@return IsoObject
function IsoObject:getRerouteMaskObject() end

---@public
---@return float
function IsoObject:getThumpCondition() end

---@public
---@return boolean
function IsoObject:HasTooltip() end

---@public
---@return ArrayList|IsoSpriteInstance @the AttachedAnimSprite
function IsoObject:getAttachedAnimSprite() end

---@public
---@return boolean @the NoPicking
function IsoObject:isNoPicking() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 Shader
---@param arg6 Consumer|Unknown
---@return void
function IsoObject:renderAttachedSprites(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param tooltipUI ObjectTooltip
---@param square IsoGridSquare
---@return void
function IsoObject:DoSpecialTooltip(tooltipUI, square) end

---@public
---@param arg0 boolean
---@return void
function IsoObject:setUsesExternalWaterSource(arg0) end

---@public
---@return IsoObject @the rerouteMask
function IsoObject:getRerouteMask() end

---@public
---@return void
function IsoObject:RemoveAttachedAnims() end

---@public
---@param change String
---@return void
---@overload fun(change:String, args:Object[])
---@overload fun(change:String, tbl:KahluaTable)
function IsoObject:sendObjectChange(change) end

---@public
---@param change String
---@vararg Object[]
---@return void
function IsoObject:sendObjectChange(change, ...) end

---@public
---@param change String
---@param tbl KahluaTable
---@return void
function IsoObject:sendObjectChange(change, tbl) end

---@public
---@param spriteName String
---@return void
---@overload fun(arg0:String, arg1:boolean)
---@overload fun(spriteName:String, r:float, g:float, b:float, a:float)
---@overload fun(arg0:String, arg1:float, arg2:float, arg3:float, arg4:float, arg5:boolean)
function IsoObject:setOverlaySprite(spriteName) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function IsoObject:setOverlaySprite(arg0, arg1) end

---@public
---@param spriteName String
---@param r float
---@param g float
---@param b float
---@param a float
---@return void
function IsoObject:setOverlaySprite(spriteName, r, g, b, a) end

---@public
---@param arg0 String
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 boolean
---@return boolean
function IsoObject:setOverlaySprite(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param container ItemContainer @the container to set
---@return void
function IsoObject:setContainer(container) end

---@public
---@return boolean
---@overload fun(arg0:int)
function IsoObject:isOutlineHlAttached() end

---@public
---@param arg0 int
---@return boolean
function IsoObject:isOutlineHlAttached(arg0) end

---@public
---@return float
function IsoObject:getOffsetY() end

---@public
---@param change String
---@param bb ByteBuffer
---@return void
function IsoObject:loadChange(change, bb) end

---@public
---@param cell IsoCell
---@param classID int
---@return Class|Unknown
function IsoObject:factoryClassFromFileInput(cell, classID) end

---@public
---@param arg0 IsoCell
---@param arg1 int
---@return IsoObject
function IsoObject:factoryFromFileInput_OLD(arg0, arg1) end

---@public
---@return void
function IsoObject:softReset() end

---@public
---@return IsoCell @the cell
function IsoObject:getCell() end

---@public
---@param specialTooltip boolean
---@return void
function IsoObject:setSpecialTooltip(specialTooltip) end

---@public
---@param arg0 ItemContainer
---@param arg1 InventoryItem
---@return boolean
function IsoObject:isItemAllowedInContainer(arg0, arg1) end

---@public
---@param arg0 int
---@return void
function IsoObject:setAlphaToTarget(arg0) end

---@protected
---@return void
function IsoObject:addItemsFromProperties() end

---@public
---@param square IsoGridSquare @the square to set
---@return void
function IsoObject:setSquare(square) end

---@public
---@return boolean
function IsoObject:getIsSurfaceNormalOffset() end

---@public
---@return void
function IsoObject:transmitCustomColor() end

---@public
---@return float
function IsoObject:getZ() end

---@public
---@return String
function IsoObject:getTile() end

---@public
---@return boolean
function IsoObject:Serialize() end

---@public
---@param AttachedAnimSprite ArrayList|IsoSpriteInstance @the AttachedAnimSprite to set
---@return void
function IsoObject:setAttachedAnimSprite(AttachedAnimSprite) end

---@public
---@param arg0 boolean
---@return void
---@overload fun(arg0:int, arg1:boolean)
function IsoObject:setOutlineHlBlink(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function IsoObject:setOutlineHlBlink(arg0, arg1) end

---@public
---@return void
function IsoObject:transmitUpdatedSpriteToServer() end

---@public
---@return IsoSprite @the sprite
function IsoObject:getSprite() end

---@public
---@param arg0 JVector2
---@param arg1 IsoObject
---@param arg2 float
---@return void
function IsoObject:Hit(arg0, arg1, arg2) end

---@public
---@param tooltipUI ObjectTooltip
---@return void
function IsoObject:DoTooltip(tooltipUI) end

---@public
---@param arg0 ByteBuffer
---@return void
function IsoObject:loadState(arg0) end

---@public
---@return ColorInfo
function IsoObject:getHighlightColor() end

---@public
---@return int
function IsoObject:getWaterAmount() end

---@public
---@return IsoGridSquare
function IsoObject:getSquare() end

---@public
---@return String
function IsoObject:getTextureName() end

---@public
---@return void
function IsoObject:renderlast() end

---@public
---@return float
function IsoObject:getSurfaceOffset() end

---@public
---@param arg0 IsoMovingObject
---@return void
function IsoObject:Thump(arg0) end

---@public
---@return float
---@overload fun(arg0:int)
function IsoObject:getAlpha() end

---@public
---@param arg0 int
---@return float
function IsoObject:getAlpha(arg0) end

---@public
---@return IsoSprite
function IsoObject:getOverlaySprite() end

---@public
---@param amount int
---@return int
function IsoObject:useWater(amount) end

---@public
---@param OutlineOnMouseover boolean @the OutlineOnMouseover to set
---@return void
function IsoObject:setOutlineOnMouseover(OutlineOnMouseover) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@param arg7 Consumer|Unknown
---@param arg8 Consumer|Unknown
---@return void
function IsoObject:renderFloorTile(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@return long
function IsoObject:customHashCode() end

---@public
---@param arg0 ItemContainer
---@return void
function IsoObject:addSecondaryContainer(arg0) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@return void
function IsoObject:renderOverlaySprites(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 IsoDirections
---@return void
function IsoObject:destroyFence(arg0) end

---@public
---@param _table KahluaTable @the table to set
---@return void
function IsoObject:setTable(_table) end

---@public
---@param arg0 boolean
---@return void
---@overload fun(arg0:int, arg1:boolean)
function IsoObject:setOutlineHlAttached(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function IsoObject:setOutlineHlAttached(arg0, arg1) end

---@public
---@param arg0 IsoObject
---@return void
function IsoObject:UnCollision(arg0) end

---@private
---@return void
function IsoObject:initFactory() end

---@public
---@return short @the Damage
function IsoObject:getDamage() end

---@public
---@return PropertyContainer
function IsoObject:getProperties() end

---@public
---@return void
function IsoObject:transmitUpdatedSprite() end

---@public
---@return String
function IsoObject:getSpriteName() end

---@private
---@return boolean
function IsoObject:isWaterInfinite() end

---@public
---@param b ByteBufferWriter
---@return void
function IsoObject:writeToRemoteBuffer(b) end

---@public
---@param arg0 JVector2
---@param arg1 IsoObject
---@return void
function IsoObject:Collision(arg0, arg1) end

---@public
---@return void
function IsoObject:doFindExternalWaterSource() end

---@public
---@return boolean
function IsoObject:hasModData() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@return void
function IsoObject:WeaponHit(arg0, arg1) end

---@public
---@return void
function IsoObject:unsetOutlineHighlight() end

---@public
---@param units int
---@return void
function IsoObject:setWaterAmount(units) end

---@public
---@return boolean
function IsoObject:hasExternalWaterSource() end

---@public
---@return boolean
function IsoObject:isSpriteInvisible() end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function IsoObject:getSpriteGridObjects(arg0) end

---@private
---@param arg0 IsoObject.IsoObjectFactory
---@return IsoObject.IsoObjectFactory
function IsoObject:addIsoObjectFactory(arg0) end

---@public
---@param NoPicking boolean @the NoPicking to set
---@return void
function IsoObject:setNoPicking(NoPicking) end
