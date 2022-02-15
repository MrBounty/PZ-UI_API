---@class Metabolics : zombie.characters.BodyDamage.Metabolics
---@field public Sleeping Metabolics
---@field public SeatedResting Metabolics
---@field public StandingAtRest Metabolics
---@field public SedentaryActivity Metabolics
---@field public Default Metabolics
---@field public DrivingCar Metabolics
---@field public LightDomestic Metabolics
---@field public HeavyDomestic Metabolics
---@field public DefaultExercise Metabolics
---@field public UsingTools Metabolics
---@field public LightWork Metabolics
---@field public MediumWork Metabolics
---@field public DiggingSpade Metabolics
---@field public HeavyWork Metabolics
---@field public ForestryAxe Metabolics
---@field public Walking2kmh Metabolics
---@field public Walking5kmh Metabolics
---@field public Running10kmh Metabolics
---@field public Running15kmh Metabolics
---@field public JumpFence Metabolics
---@field public ClimbRope Metabolics
---@field public Fitness Metabolics
---@field public FitnessHeavy Metabolics
---@field public MAX Metabolics
---@field private met float
Metabolics = {}

---@public
---@return Metabolics[]
function Metabolics:values() end

---@public
---@return float
function Metabolics:getWm2() end

---@public
---@return float
function Metabolics:getBtuHr() end

---@public
---@return float
function Metabolics:getW() end

---@public
---@param arg0 float
---@return float
function Metabolics:MetToBtuHr(arg0) end

---@public
---@return float
function Metabolics:getMet() end

---@public
---@param arg0 float
---@return float
function Metabolics:MetToW(arg0) end

---@public
---@param arg0 float
---@return float
function Metabolics:MetToWm2(arg0) end

---@public
---@param arg0 String
---@return Metabolics
function Metabolics:valueOf(arg0) end
