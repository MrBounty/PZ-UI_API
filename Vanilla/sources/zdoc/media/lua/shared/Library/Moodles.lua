---@class Moodles : zombie.characters.Moodles.Moodles
---@field MoodlesStateChanged boolean
---@field private MoodleList Stack|Unknown
---@field private Parent IsoGameCharacter
Moodles = {}

---@public
---@param MoodleIndex int
---@return int
function Moodles:getGoodBadNeutral(MoodleIndex) end

---@public
---@return int
function Moodles:getNumMoodles() end

---@public
---@param MoodleIndex int
---@return int
---@overload fun(MType:MoodleType)
function Moodles:getMoodleLevel(MoodleIndex) end

---@public
---@param MType MoodleType
---@return int
function Moodles:getMoodleLevel(MType) end

---@public
---@param MoodleIndex int
---@return String
function Moodles:getMoodleDescriptionString(MoodleIndex) end

---@public
---@return boolean
function Moodles:UI_RefreshNeeded() end

---@public
---@param refresh boolean
---@return void
function Moodles:setMoodlesStateChanged(refresh) end

---@public
---@param MoodleIndex int
---@return MoodleType
function Moodles:getMoodleType(MoodleIndex) end

---@public
---@return void
function Moodles:Update() end

---@public
---@return void
function Moodles:Randomise() end

---@public
---@param arg0 int
---@return Color
function Moodles:getMoodleChevronColor(arg0) end

---@public
---@param MoodleIndex int
---@return String
function Moodles:getMoodleDisplayString(MoodleIndex) end

---@public
---@param arg0 int
---@return int
function Moodles:getMoodleChevronCount(arg0) end

---@public
---@param arg0 int
---@return boolean
function Moodles:getMoodleChevronIsUp(arg0) end
