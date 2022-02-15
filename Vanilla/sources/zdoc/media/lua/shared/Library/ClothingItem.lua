---@class ClothingItem : zombie.core.skinnedmodel.population.ClothingItem
---@field public m_GUID String
---@field public m_MaleModel String
---@field public m_FemaleModel String
---@field public m_Static boolean
---@field public m_BaseTextures ArrayList|Unknown
---@field public m_AttachBone String
---@field public m_Masks ArrayList|Unknown
---@field public m_MasksFolder String
---@field public m_UnderlayMasksFolder String
---@field public textureChoices ArrayList|Unknown
---@field public m_AllowRandomHue boolean
---@field public m_AllowRandomTint boolean
---@field public m_DecalGroup String
---@field public m_Shader String
---@field public m_HatCategory String
---@field public s_masksFolderDefault String
---@field public m_Name String
---@field public ASSET_TYPE AssetType
ClothingItem = {}

---@public
---@return String
function ClothingItem:GetATexture() end

---@public
---@param arg0 CharacterMask
---@return void
function ClothingItem:getCombinedMask(arg0) end

---@public
---@param arg0 boolean
---@return String
function ClothingItem:getModel(arg0) end

---@public
---@return String
function ClothingItem:getMaleModel() end

---@public
---@return boolean
function ClothingItem:getAllowRandomTint() end

---@public
---@param arg0 ClothingItemReference
---@param arg1 CharacterMask
---@return void
---@overload fun(arg0:ClothingItem, arg1:CharacterMask)
function ClothingItem:tryGetCombinedMask(arg0, arg1) end

---@public
---@param arg0 ClothingItem
---@param arg1 CharacterMask
---@return void
function ClothingItem:tryGetCombinedMask(arg0, arg1) end

---@public
---@return boolean
function ClothingItem:isMask() end

---@public
---@return AssetType
function ClothingItem:getType() end

---@public
---@return String
function ClothingItem:getDecalGroup() end

---@public
---@return ArrayList|Unknown
function ClothingItem:getBaseTextures() end

---@public
---@return String
function ClothingItem:getFemaleModel() end

---@public
---@return String
function ClothingItem:toString() end

---@public
---@return boolean
function ClothingItem:hasModel() end

---@public
---@return boolean
function ClothingItem:getAllowRandomHue() end

---@public
---@return boolean
function ClothingItem:isHat() end

---@public
---@return ArrayList|Unknown
function ClothingItem:getTextureChoices() end
