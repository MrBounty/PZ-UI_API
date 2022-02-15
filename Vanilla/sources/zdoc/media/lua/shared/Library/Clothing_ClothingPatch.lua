---@class Clothing.ClothingPatch : zombie.inventory.types.Clothing.ClothingPatch
---@field public tailorLvl int
---@field public fabricType int
---@field public scratchDefense int
---@field public biteDefense int
---@field public hasHole boolean
---@field public conditionGain int
Clothing_ClothingPatch = {}

---@public
---@return String
function Clothing_ClothingPatch:getFabricTypeName() end

---@public
---@return int
function Clothing_ClothingPatch:getScratchDefense() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function Clothing_ClothingPatch:load(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function Clothing_ClothingPatch:save_old(arg0, arg1) end

---@public
---@return int
function Clothing_ClothingPatch:getFabricType() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function Clothing_ClothingPatch:load_old(arg0, arg1, arg2) end

---@public
---@return int
function Clothing_ClothingPatch:getBiteDefense() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function Clothing_ClothingPatch:save(arg0, arg1) end
