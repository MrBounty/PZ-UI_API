---@class MoodlesUI : zombie.ui.MoodlesUI
---@field public clientH float
---@field public clientW float
---@field public Movable boolean
---@field public ncclientH int
---@field public ncclientW int
---@field private instance MoodlesUI
---@field private OFFSCREEN_Y float
---@field public nestedItems Stack|Rectangle
---@field alpha float
---@field Back_Bad_1 Texture
---@field Back_Bad_2 Texture
---@field Back_Bad_3 Texture
---@field Back_Bad_4 Texture
---@field Back_Good_1 Texture
---@field Back_Good_2 Texture
---@field Back_Good_3 Texture
---@field Back_Good_4 Texture
---@field Back_Neutral Texture
---@field Endurance Texture
---@field Bleeding Texture
---@field Angry Texture
---@field Stress Texture
---@field Thirst Texture
---@field Panic Texture
---@field Hungry Texture
---@field Injured Texture
---@field Pain Texture
---@field Sick Texture
---@field Bored Texture
---@field Unhappy Texture
---@field Tired Texture
---@field HeavyLoad Texture
---@field Drunk Texture
---@field Wet Texture
---@field HasACold Texture
---@field Dead Texture
---@field Zombie Texture
---@field Windchill Texture
---@field CantSprint Texture
---@field FoodEaten Texture
---@field Hyperthermia Texture
---@field Hypothermia Texture
---@field public plusRed Texture
---@field public plusGreen Texture
---@field public minusRed Texture
---@field public minusGreen Texture
---@field public chevronUp Texture
---@field public chevronUpBorder Texture
---@field public chevronDown Texture
---@field public chevronDownBorder Texture
---@field MoodleDistY float
---@field MouseOver boolean
---@field MouseOverSlot int
---@field NumUsedSlots int
---@field private DebugKeyDelay int
---@field private DistFromRighEdge int
---@field private GoodBadNeutral int[]
---@field private MoodleLevel int[]
---@field private MoodleOscilationLevel float[]
---@field private MoodleSlotsDesiredPos float[]
---@field private MoodleSlotsPos float[]
---@field private MoodleTypeInSlot int[]
---@field private Oscilator float
---@field private OscilatorDecelerator float
---@field private OscilatorRate float
---@field private OscilatorScalar float
---@field private OscilatorStartLevel float
---@field private OscilatorStep float
---@field private UseCharacter IsoGameCharacter
---@field private alphaIncrease boolean
MoodlesUI = {}

---Overrides:
---
---update in class UIElement
---@public
---@return void
function MoodlesUI:update() end

---@public
---@param arg0 MoodleType
---@return void
function MoodlesUI:wiggle(arg0) end

---Overrides:
---
---render in class UIElement
---@public
---@return void
function MoodlesUI:render() end

---@public
---@param el UIElement
---@param t int
---@param r int
---@param b int
---@param l int
---@return void
function MoodlesUI:Nest(el, t, r, b, l) end

---@public
---@return MoodlesUI
function MoodlesUI:getInstance() end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function MoodlesUI:onMouseMove(arg0, arg1) end

---@public
---@param chr IsoGameCharacter
---@return void
function MoodlesUI:setCharacter(chr) end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function MoodlesUI:onMouseMoveOutside(arg0, arg1) end

---@public
---@return boolean
function MoodlesUI:CurrentlyAnimating() end
