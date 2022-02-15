---@class Fitness : zombie.characters.BodyDamage.Fitness
---@field private parent IsoGameCharacter
---@field private regularityMap HashMap|Unknown|Unknown
---@field private fitnessLvl int
---@field private strLvl int
---@field private stiffnessTimerMap HashMap|Unknown|Unknown
---@field private stiffnessIncMap HashMap|Unknown|Unknown
---@field private bodypartToIncStiffness ArrayList|Unknown
---@field private exercises HashMap|Unknown|Unknown
---@field private exeTimer HashMap|Unknown|Unknown
---@field private lastUpdate int
---@field private currentExe Fitness.FitnessExercise
---@field private HOURS_FOR_STIFFNESS int
---@field private BASE_STIFFNESS_INC float
---@field private BASE_ENDURANCE_RED float
---@field private BASE_REGULARITY_INC float
---@field private BASE_REGULARITY_DEC float
---@field private BASE_PAIN_INC float
Fitness = {}

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@return void
function Fitness:load(arg0, arg1) end

---@public
---@param arg0 ByteBuffer
---@return void
function Fitness:save(arg0) end

---@public
---@return void
function Fitness:update() end

---@public
---@return void
function Fitness:exerciseRepeat() end

---@public
---@return void
function Fitness:reduceEndurance() end

---@public
---@param arg0 String
---@return float
function Fitness:getRegularity(arg0) end

---@public
---@return void
function Fitness:incRegularity() end

---@public
---@return void
function Fitness:resetValues() end

---@public
---@return IsoGameCharacter
function Fitness:getParent() end

---@public
---@return void
function Fitness:incFutureStiffness() end

---@private
---@param arg0 String
---@return void
function Fitness:increasePain(arg0) end

---@public
---@return void
function Fitness:initRegularityMapProfession() end

---@public
---@param arg0 String
---@return void
function Fitness:setCurrentExercise(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function Fitness:setParent(arg0) end

---@public
---@return boolean
function Fitness:onGoingStiffness() end

---@public
---@param arg0 HashMap|Unknown|Unknown
---@return void
function Fitness:setRegularityMap(arg0) end

---@private
---@return void
function Fitness:updateExeTimer() end

---@public
---@return HashMap|Unknown|Unknown
function Fitness:getRegularityMap() end

---@public
---@return void
function Fitness:incStats() end

---@public
---@return void
function Fitness:init() end

---@private
---@return void
function Fitness:decreaseRegularity() end

---@public
---@param arg0 String
---@return float
function Fitness:getCurrentExeStiffnessInc(arg0) end

---@public
---@param arg0 String
---@return int
function Fitness:getCurrentExeStiffnessTimer(arg0) end
