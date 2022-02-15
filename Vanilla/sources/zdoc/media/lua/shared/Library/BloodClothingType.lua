---@class BloodClothingType : zombie.characterTextures.BloodClothingType
---@field public Jacket BloodClothingType
---@field public LongJacket BloodClothingType
---@field public Trousers BloodClothingType
---@field public ShortsShort BloodClothingType
---@field public Shirt BloodClothingType
---@field public ShirtLongSleeves BloodClothingType
---@field public ShirtNoSleeves BloodClothingType
---@field public Jumper BloodClothingType
---@field public JumperNoSleeves BloodClothingType
---@field public Shoes BloodClothingType
---@field public FullHelmet BloodClothingType
---@field public Apron BloodClothingType
---@field public Bag BloodClothingType
---@field public Hands BloodClothingType
---@field public Head BloodClothingType
---@field public Neck BloodClothingType
---@field public UpperBody BloodClothingType
---@field public LowerBody BloodClothingType
---@field public LowerLegs BloodClothingType
---@field public UpperLegs BloodClothingType
---@field public LowerArms BloodClothingType
---@field public UpperArms BloodClothingType
---@field public Groin BloodClothingType
---@field private coveredParts HashMap|Unknown|Unknown
---@field private bodyParts ArrayList|Unknown
BloodClothingType = {}

---@private
---@return void
function BloodClothingType:init() end

---@public
---@param arg0 Clothing
---@return void
function BloodClothingType:calcTotalBloodLevel(arg0) end

---@public
---@param arg0 String
---@return BloodClothingType
function BloodClothingType:fromString(arg0) end

---@public
---@param arg0 int
---@param arg1 HumanVisual
---@param arg2 ArrayList|Unknown
---@param arg3 boolean
---@return void
---@overload fun(arg0:BloodBodyPartType, arg1:HumanVisual, arg2:ArrayList|Unknown, arg3:boolean)
---@overload fun(arg0:BloodBodyPartType, arg1:float, arg2:HumanVisual, arg3:ArrayList|Unknown, arg4:boolean)
function BloodClothingType:addBlood(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 HumanVisual
---@param arg2 ArrayList|Unknown
---@param arg3 boolean
---@return void
function BloodClothingType:addBlood(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 float
---@param arg2 HumanVisual
---@param arg3 ArrayList|Unknown
---@param arg4 boolean
---@return void
function BloodClothingType:addBlood(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 Clothing
---@return void
function BloodClothingType:calcTotalDirtLevel(arg0) end

---@public
---@param arg0 String
---@return BloodClothingType
function BloodClothingType:valueOf(arg0) end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 HumanVisual
---@param arg2 ArrayList|Unknown
---@return void
---@overload fun(arg0:BloodBodyPartType, arg1:HumanVisual, arg2:ArrayList|Unknown, arg3:boolean)
function BloodClothingType:addHole(arg0, arg1, arg2) end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 HumanVisual
---@param arg2 ArrayList|Unknown
---@param arg3 boolean
---@return void
function BloodClothingType:addHole(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 HumanVisual
---@param arg2 ArrayList|Unknown
---@return void
function BloodClothingType:addBasicPatch(arg0, arg1, arg2) end

---@public
---@param arg0 ArrayList|Unknown
---@return ArrayList|Unknown
---@overload fun(arg0:ArrayList|Unknown, arg1:ArrayList|Unknown)
function BloodClothingType:getCoveredParts(arg0) end

---@public
---@param arg0 ArrayList|Unknown
---@param arg1 ArrayList|Unknown
---@return ArrayList|Unknown
function BloodClothingType:getCoveredParts(arg0, arg1) end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 HumanVisual
---@param arg2 ArrayList|Unknown
---@param arg3 boolean
---@return void
---@overload fun(arg0:BloodBodyPartType, arg1:float, arg2:HumanVisual, arg3:ArrayList|Unknown, arg4:boolean)
function BloodClothingType:addDirt(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 BloodBodyPartType
---@param arg1 float
---@param arg2 HumanVisual
---@param arg3 ArrayList|Unknown
---@param arg4 boolean
---@return void
function BloodClothingType:addDirt(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return BloodClothingType[]
function BloodClothingType:values() end

---@public
---@param arg0 ArrayList|Unknown
---@return int
function BloodClothingType:getCoveredPartCount(arg0) end
