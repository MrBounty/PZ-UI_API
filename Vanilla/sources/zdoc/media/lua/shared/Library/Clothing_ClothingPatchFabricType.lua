---@class Clothing.ClothingPatchFabricType : zombie.inventory.types.Clothing.ClothingPatchFabricType
---@field public Cotton Clothing.ClothingPatchFabricType
---@field public Denim Clothing.ClothingPatchFabricType
---@field public Leather Clothing.ClothingPatchFabricType
---@field public index int
---@field public type String
---@field public maxScratchDef int
---@field public maxBiteDef int
Clothing_ClothingPatchFabricType = {}

---@public
---@param arg0 String
---@return Clothing.ClothingPatchFabricType
function Clothing_ClothingPatchFabricType:fromType(arg0) end

---@public
---@return String
function Clothing_ClothingPatchFabricType:getType() end

---@public
---@param arg0 String
---@return Clothing.ClothingPatchFabricType
function Clothing_ClothingPatchFabricType:valueOf(arg0) end

---@public
---@param arg0 int
---@return Clothing.ClothingPatchFabricType
function Clothing_ClothingPatchFabricType:fromIndex(arg0) end

---@public
---@return Clothing.ClothingPatchFabricType[]
function Clothing_ClothingPatchFabricType:values() end
