---@class MoodleType : zombie.characters.Moodles.MoodleType
---@field public Endurance MoodleType
---@field public Tired MoodleType
---@field public Hungry MoodleType
---@field public Panic MoodleType
---@field public Sick MoodleType
---@field public Bored MoodleType
---@field public Unhappy MoodleType
---@field public Bleeding MoodleType
---@field public Wet MoodleType
---@field public HasACold MoodleType
---@field public Angry MoodleType
---@field public Stress MoodleType
---@field public Thirst MoodleType
---@field public Injured MoodleType
---@field public Pain MoodleType
---@field public HeavyLoad MoodleType
---@field public Drunk MoodleType
---@field public Dead MoodleType
---@field public Zombie MoodleType
---@field public Hyperthermia MoodleType
---@field public Hypothermia MoodleType
---@field public Windchill MoodleType
---@field public CantSprint MoodleType
---@field public FoodEaten MoodleType
---@field public MAX MoodleType
MoodleType = {}

---@public
---@param MT MoodleType
---@param Level int
---@return String
function MoodleType:getDescriptionText(MT, Level) end

---Returns the enum constant of this type with the specified name.
---
---The string must match exactly an identifier used to declare an
---
---enum constant in this type.  (Extraneous whitespace characters are
---
---not permitted.)
---@public
---@param name String @the name of the enum constant to be returned.
---@return MoodleType @the enum constant with the specified name
function MoodleType:valueOf(name) end

---@public
---@param MT MoodleType
---@return int
function MoodleType:ToIndex(MT) end

---Returns an array containing the constants of this enum type, in
---
---the order they are declared.  This method may be used to iterate
---
---over the constants as follows:
---
---
---
---for (MoodleType c : MoodleType.values())
---
---    System.out.println(c);
---
---
---@public
---@return MoodleType[] @an array containing the constants of this enum type, in the order they are declared
function MoodleType:values() end

---@public
---@param MT MoodleType
---@return int
function MoodleType:GoodBadNeutral(MT) end

---@public
---@param MT MoodleType
---@param Level int
---@return String
function MoodleType:getDisplayName(MT, Level) end

---@public
---@param str String
---@return MoodleType
function MoodleType:FromString(str) end

---@public
---@param index int
---@return MoodleType
function MoodleType:FromIndex(index) end
