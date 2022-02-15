---@class NewHealthPanel : zombie.ui.NewHealthPanel
---@field public instance NewHealthPanel
---@field public BodyOutline Texture
---@field public Foot_L UI_BodyPart
---@field public Foot_R UI_BodyPart
---@field public ForeArm_L UI_BodyPart
---@field public ForeArm_R UI_BodyPart
---@field public Groin UI_BodyPart
---@field public Hand_L UI_BodyPart
---@field public Hand_R UI_BodyPart
---@field public Head UI_BodyPart
---@field public LowerLeg_L UI_BodyPart
---@field public LowerLeg_R UI_BodyPart
---@field public Neck UI_BodyPart
---@field public Torso_Lower UI_BodyPart
---@field public Torso_Upper UI_BodyPart
---@field public UpperArm_L UI_BodyPart
---@field public UpperArm_R UI_BodyPart
---@field public UpperLeg_L UI_BodyPart
---@field public UpperLeg_R UI_BodyPart
---@field public HealthBar Texture
---@field public HealthBarBack Texture
---@field public HealthIcon Texture
---@field ParentChar IsoGameCharacter
NewHealthPanel = {}

---@public
---@return String
function NewHealthPanel:getDamageStatusString() end

---@public
---@param chr IsoGameCharacter
---@return void
function NewHealthPanel:SetCharacter(chr) end

---Overrides:
---
---update in class NewWindow
---@public
---@return void
function NewHealthPanel:update() end

---Overrides:
---
---render in class NewWindow
---@public
---@return void
function NewHealthPanel:render() end
