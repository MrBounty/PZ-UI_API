---@class BloodBodyPartType : zombie.characterTextures.BloodBodyPartType
---@field public Hand_L BloodBodyPartType
---@field public Hand_R BloodBodyPartType
---@field public ForeArm_L BloodBodyPartType
---@field public ForeArm_R BloodBodyPartType
---@field public UpperArm_L BloodBodyPartType
---@field public UpperArm_R BloodBodyPartType
---@field public Torso_Upper BloodBodyPartType
---@field public Torso_Lower BloodBodyPartType
---@field public Head BloodBodyPartType
---@field public Neck BloodBodyPartType
---@field public Groin BloodBodyPartType
---@field public UpperLeg_L BloodBodyPartType
---@field public UpperLeg_R BloodBodyPartType
---@field public LowerLeg_L BloodBodyPartType
---@field public LowerLeg_R BloodBodyPartType
---@field public Foot_L BloodBodyPartType
---@field public Foot_R BloodBodyPartType
---@field public Back BloodBodyPartType
---@field public MAX BloodBodyPartType
---@field private m_characterMaskParts CharacterMask.Part[]
BloodBodyPartType = {}

---@public
---@return CharacterMask.Part[]
function BloodBodyPartType:getCharacterMaskParts() end

---@public
---@param arg0 String
---@return BloodBodyPartType
function BloodBodyPartType:FromString(arg0) end

---@public
---@return BloodBodyPartType[]
function BloodBodyPartType:values() end

---@public
---@param arg0 int
---@return BloodBodyPartType
function BloodBodyPartType:FromIndex(arg0) end

---@public
---@return int
function BloodBodyPartType:index() end

---@public
---@return String
---@overload fun(arg0:BloodBodyPartType)
function BloodBodyPartType:getDisplayName() end

---@public
---@param arg0 BloodBodyPartType
---@return String
function BloodBodyPartType:getDisplayName(arg0) end

---@public
---@param arg0 BloodBodyPartType
---@return int
function BloodBodyPartType:ToIndex(arg0) end

---@public
---@param arg0 String
---@return BloodBodyPartType
function BloodBodyPartType:valueOf(arg0) end
