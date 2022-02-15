---@class BodyPartType : zombie.characters.BodyDamage.BodyPartType
---@field public Hand_L BodyPartType
---@field public Hand_R BodyPartType
---@field public ForeArm_L BodyPartType
---@field public ForeArm_R BodyPartType
---@field public UpperArm_L BodyPartType
---@field public UpperArm_R BodyPartType
---@field public Torso_Upper BodyPartType
---@field public Torso_Lower BodyPartType
---@field public Head BodyPartType
---@field public Neck BodyPartType
---@field public Groin BodyPartType
---@field public UpperLeg_L BodyPartType
---@field public UpperLeg_R BodyPartType
---@field public LowerLeg_L BodyPartType
---@field public LowerLeg_R BodyPartType
---@field public Foot_L BodyPartType
---@field public Foot_R BodyPartType
---@field public MAX BodyPartType
BodyPartType = {}

---@public
---@param BPT BodyPartType
---@return String
function BodyPartType:ToString(BPT) end

---Returns an array containing the constants of this enum type, in
---
---the order they are declared.  This method may be used to iterate
---
---over the constants as follows:
---
---
---
---for (BodyPartType c : BodyPartType.values())
---
---    System.out.println(c);
---
---
---@public
---@return BodyPartType[] @an array containing the constants of this enum type, in the order they are declared
function BodyPartType:values() end

---@public
---@param arg0 BodyPartType
---@return float
function BodyPartType:GetSkinSurface(arg0) end

---@public
---@param str String
---@return BodyPartType
function BodyPartType:FromString(str) end

---@public
---@return BodyPartType
function BodyPartType:getRandom() end

---@public
---@param arg0 boolean
---@return String
function BodyPartType:getBiteWoundModel(arg0) end

---@public
---@param arg0 BodyPartType
---@return float
function BodyPartType:GetUmbrellaMod(arg0) end

---@public
---@return String
function BodyPartType:getBandageModel() end

---@public
---@param BPT BodyPartType
---@return String
function BodyPartType:getDisplayName(BPT) end

---@public
---@return int
function BodyPartType:index() end

---Returns the enum constant of this type with the specified name.
---
---The string must match exactly an identifier used to declare an
---
---enum constant in this type.  (Extraneous whitespace characters are
---
---not permitted.)
---@public
---@param name String @the name of the enum constant to be returned.
---@return BodyPartType @the enum constant with the specified name
function BodyPartType:valueOf(name) end

---@public
---@param arg0 BodyPartType
---@return float
function BodyPartType:GetDistToCore(arg0) end

---@public
---@param arg0 int
---@return float
function BodyPartType:getBleedingTimeModifyer(arg0) end

---@public
---@param arg0 BodyPartType
---@return float
function BodyPartType:GetMaxMovementPenalty(arg0) end

---@public
---@param index int
---@return float
function BodyPartType:getPainModifyer(index) end

---@public
---@param index int
---@return BodyPartType
function BodyPartType:FromIndex(index) end

---@public
---@param BPT BodyPartType
---@return int
function BodyPartType:ToIndex(BPT) end

---@public
---@param arg0 boolean
---@return String
function BodyPartType:getCutWoundModel(arg0) end

---@public
---@param index int
---@return float
function BodyPartType:getDamageModifyer(index) end

---@public
---@param arg0 boolean
---@return String
function BodyPartType:getScratchWoundModel(arg0) end

---@public
---@param arg0 BodyPartType
---@return float
function BodyPartType:GetMaxActionPenalty(arg0) end
