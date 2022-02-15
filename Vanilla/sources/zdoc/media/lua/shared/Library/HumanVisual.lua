---@class HumanVisual : zombie.core.skinnedmodel.visual.HumanVisual
---@field private owner IHumanVisual
---@field private skinColor ImmutableColor
---@field private skinTexture int
---@field private skinTextureName String
---@field public zombieRotStage int
---@field private hairColor ImmutableColor
---@field private beardColor ImmutableColor
---@field private hairModel String
---@field private beardModel String
---@field private bodyHair int
---@field private blood byte[]
---@field private dirt byte[]
---@field private holes byte[]
---@field private bodyVisuals ItemVisuals
---@field private outfit Outfit
---@field private nonAttachedHair String
---@field private itemVisualLocations ArrayList|Unknown
---@field private LASTSTAND_VERSION1 int
---@field private LASTSTAND_VERSION int
HumanVisual = {}

---@public
---@return String
function HumanVisual:getLastStandString() end

---@private
---@param arg0 Outfit
---@param arg1 ItemVisuals
---@return void
function HumanVisual:dressInOutfit(arg0, arg1) end

---@public
---@param arg0 BloodBodyPartType
---@return float
function HumanVisual:getDirt(arg0) end

---@public
---@return String
function HumanVisual:getNonAttachedHair() end

---@public
---@param arg0 String
---@return void
function HumanVisual:setSkinTextureName(arg0) end

---@public
---@param arg0 int
---@return void
function HumanVisual:setSkinTextureIndex(arg0) end

---@public
---@param arg0 ItemVisuals
---@param arg1 Item
---@return ItemVisual
---@overload fun(arg0:ItemVisuals, arg1:ArrayList|Unknown, arg2:String, arg3:ClothingItemReference)
function HumanVisual:addClothingItem(arg0, arg1) end

---@private
---@param arg0 ItemVisuals
---@param arg1 ArrayList|Unknown
---@param arg2 String
---@param arg3 ClothingItemReference
---@return ItemVisual
function HumanVisual:addClothingItem(arg0, arg1, arg2, arg3) end

---@public
---@return ImmutableColor
function HumanVisual:getHairColor() end

---@public
---@param arg0 String
---@return void
function HumanVisual:setHairModel(arg0) end

---@public
---@return void
function HumanVisual:randomBlood() end

---@public
---@param arg0 ByteBuffer
---@return void
function HumanVisual:save(arg0) end

---@public
---@param arg0 ItemVisuals
---@return CharacterMask
function HumanVisual:GetMask(arg0) end

---@public
---@return ImmutableColor
function HumanVisual:getBeardColor() end

---@public
---@return String
function HumanVisual:getHairModel() end

---@public
---@param arg0 BloodBodyPartType
---@return float
function HumanVisual:getBlood(arg0) end

---@public
---@return String
function HumanVisual:getBeardModel() end

---@public
---@return float
function HumanVisual:getTotalBlood() end

---@public
---@param arg0 String
---@return void
function HumanVisual:setBeardModel(arg0) end

---@public
---@return boolean
function HumanVisual:isFemale() end

---@public
---@param arg0 ImmutableColor
---@return void
function HumanVisual:setHairColor(arg0) end

---@public
---@param arg0 BloodBodyPartType
---@return float
function HumanVisual:getHole(arg0) end

---@public
---@param arg0 String
---@param arg1 ItemVisuals
---@return void
function HumanVisual:dressInNamedOutfit(arg0, arg1) end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 float
---@return void
function HumanVisual:setDirt(arg0, arg1) end

---@private
---@param arg0 String
---@return ImmutableColor
function HumanVisual:colorFromString(arg0) end

---@public
---@return Outfit
function HumanVisual:getOutfit() end

---@private
---@param arg0 ItemVisuals
---@param arg1 ArrayList|Unknown
---@return void
function HumanVisual:getItemVisualLocations(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function HumanVisual:lerp(arg0, arg1, arg2) end

---@public
---@return int
function HumanVisual:getSkinTextureIndex() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function HumanVisual:load(arg0, arg1) end

---@public
---@return int
function HumanVisual:pickRandomZombieRotStage() end

---@public
---@return void
function HumanVisual:randomDirt() end

---@public
---@return ItemVisuals
function HumanVisual:getBodyVisuals() end

---@public
---@return boolean
function HumanVisual:isZombie() end

---@private
---@param arg0 ImmutableColor
---@param arg1 StringBuilder
---@return StringBuilder
function HumanVisual:toString(arg0, arg1) end

---@public
---@return boolean
function HumanVisual:isSkeleton() end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 float
---@return void
function HumanVisual:setBlood(arg0, arg1) end

---@public
---@return void
function HumanVisual:removeBlood() end

---@public
---@param arg0 ImmutableColor
---@return void
function HumanVisual:setSkinColor(arg0) end

---@public
---@return ImmutableColor
function HumanVisual:getSkinColor() end

---@public
---@return String
function HumanVisual:getSkinTexture() end

---@public
---@return ModelInstance
function HumanVisual:createModelInstance() end

---@public
---@param arg0 BloodBodyPartType
---@return void
function HumanVisual:setHole(arg0) end

---@public
---@param arg0 String
---@return boolean
function HumanVisual:loadLastStandString(arg0) end

---@public
---@param arg0 String
---@param arg1 ItemVisuals
---@return void
---@overload fun(arg0:String, arg1:ItemVisuals, arg2:boolean)
function HumanVisual:dressInClothingItem(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 ItemVisuals
---@param arg2 boolean
---@return void
function HumanVisual:dressInClothingItem(arg0, arg1, arg2) end

---@public
---@return void
function HumanVisual:removeDirt() end

---@public
---@param arg0 HumanVisual
---@return void
function HumanVisual:copyFrom(arg0) end

---@public
---@param arg0 Outfit
---@return void
function HumanVisual:setOutfit(arg0) end

---@public
---@param arg0 Outfit
---@return void
function HumanVisual:synchWithOutfit(arg0) end

---@public
---@return int
function HumanVisual:getBodyHairIndex() end

---@public
---@param arg0 int
---@return void
function HumanVisual:setBodyHairIndex(arg0) end

---@public
---@param arg0 ImmutableColor
---@return void
function HumanVisual:setBeardColor(arg0) end

---@public
---@param arg0 String
---@return ItemVisual
function HumanVisual:addBodyVisual(arg0) end

---@public
---@return void
function HumanVisual:clear() end

---@public
---@param arg0 String
---@return void
function HumanVisual:setNonAttachedHair(arg0) end
