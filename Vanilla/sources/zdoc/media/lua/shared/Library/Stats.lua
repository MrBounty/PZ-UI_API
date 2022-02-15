---@class Stats : zombie.characters.Stats
---@field public Anger float
---@field public boredom float
---@field public endurance float
---@field public enduranceRecharging boolean
---@field public endurancelast float
---@field public endurancedanger float
---@field public endurancewarn float
---@field public fatigue float
---@field public fitness float
---@field public hunger float
---@field public idleboredom float
---@field public morale float
---@field public stress float
---@field public Fear float
---@field public Panic float
---@field public Sanity float
---@field public Sickness float
---@field public Boredom float
---@field public Pain float
---@field public Drunkenness float
---@field public NumVisibleZombies int
---@field public LastNumVisibleZombies int
---@field public Tripping boolean
---@field public TrippingRotAngle float
---@field public thirst float
---@field public NumChasingZombies int
---@field public LastVeryCloseZombies int
---@field public NumCloseZombies int
---@field public LastNumChasingZombies int
---@field public stressFromCigarettes float
---@field public ChasingZombiesDanger float
---@field public MusicZombiesVisible int
---@field public MusicZombiesTargeting int
Stats = {}

---@public
---@return float @the stress
function Stats:getStress() end

---@public
---@return float @the endurancelast
function Stats:getEndurancelast() end

---throws java.io.IOException
---@public
---@param input DataInputStream
---@return void
---@overload fun(input:ByteBuffer, WorldVersion:int)
function Stats:load(input) end

---throws java.io.IOException
---@public
---@param input ByteBuffer
---@param WorldVersion int
---@return void
function Stats:load(input, WorldVersion) end

---@public
---@param Panic float @the Panic to set
---@return void
function Stats:setPanic(Panic) end

---@public
---@return float @the hunger
function Stats:getHunger() end

---@public
---@param Fear float @the Fear to set
---@return void
function Stats:setFear(Fear) end

---@public
---@param fitness float @the fitness to set
---@return void
function Stats:setFitness(fitness) end

---@public
---@param endurance float @the endurance to set
---@return void
function Stats:setEndurance(endurance) end

---@public
---@param arg0 float
---@return void
function Stats:setStressFromCigarettes(arg0) end

---@public
---@return boolean
function Stats:getEnduranceRecharging() end

---@public
---@return float @the Sanity
function Stats:getSanity() end

---@public
---@return float @the Sickness
function Stats:getSickness() end

---@public
---@param morale float @the morale to set
---@return void
function Stats:setMorale(morale) end

---@public
---@param boredom float @the boredom to set
---@return void
function Stats:setBoredom(boredom) end

---@public
---@param hunger float @the hunger to set
---@return void
function Stats:setHunger(hunger) end

---throws java.io.IOException
---@public
---@param output ByteBuffer
---@return void
---@overload fun(output:DataOutputStream)
function Stats:save(output) end

---throws java.io.IOException
---@public
---@param output DataOutputStream
---@return void
function Stats:save(output) end

---@public
---@return int
function Stats:getNumVisibleZombies() end

---@public
---@param NumVisibleZombies int @the NumVisibleZombies to set
---@return void
function Stats:setNumVisibleZombies(NumVisibleZombies) end

---@public
---@param endurancelast float @the endurancelast to set
---@return void
function Stats:setEndurancelast(endurancelast) end

---@public
---@return float @the endurance
function Stats:getEndurance() end

---@public
---@return float @the idleboredom
function Stats:getIdleboredom() end

---@public
---@return float @the Pain
function Stats:getPain() end

---@public
---@return float @the TrippingRotAngle
function Stats:getTrippingRotAngle() end

---@public
---@param Pain float @the Pain to set
---@return void
function Stats:setPain(Pain) end

---@public
---@param Anger float @the Anger to set
---@return void
function Stats:setAnger(Anger) end

---@public
---@param fatigue float @the fatigue to set
---@return void
function Stats:setFatigue(fatigue) end

---@public
---@return void
function Stats:resetStats() end

---@public
---@return float @the Fear
function Stats:getFear() end

---@public
---@param Sanity float @the Sanity to set
---@return void
function Stats:setSanity(Sanity) end

---@public
---@param Tripping boolean @the Tripping to set
---@return void
function Stats:setTripping(Tripping) end

---@public
---@return float @the endurancedanger
function Stats:getEndurancedanger() end

---@public
---@return int @the NumVisibleZombies
function Stats:getVisibleZombies() end

---@public
---@return float
function Stats:getStressFromCigarettes() end

---@public
---@return float @the fitness
function Stats:getFitness() end

---@public
---@return float @the morale
function Stats:getMorale() end

---@public
---@return float @the boredom
function Stats:getBoredom() end

---@public
---@return float @the Anger
function Stats:getAnger() end

---@public
---@return int
function Stats:getNumChasingZombies() end

---@public
---@param endurancedanger float @the endurancedanger to set
---@return void
function Stats:setEndurancedanger(endurancedanger) end

---@public
---@return boolean @the Tripping
function Stats:isTripping() end

---@public
---@return float @the Panic
function Stats:getPanic() end

---@public
---@param idleboredom float @the idleboredom to set
---@return void
function Stats:setIdleboredom(idleboredom) end

---@public
---@return float @the thirst
function Stats:getThirst() end

---@public
---@param endurancewarn float @the endurancewarn to set
---@return void
function Stats:setEndurancewarn(endurancewarn) end

---@public
---@param thirst float @the thirst to set
---@return void
function Stats:setThirst(thirst) end

---@public
---@return float @the fatigue
function Stats:getFatigue() end

---@public
---@param Sickness float @the Sickness to set
---@return void
function Stats:setSickness(Sickness) end

---@public
---@param Drunkenness float @the Drunkenness to set
---@return void
function Stats:setDrunkenness(Drunkenness) end

---@public
---@return float
function Stats:getMaxStressFromCigarettes() end

---@public
---@param TrippingRotAngle float @the TrippingRotAngle to set
---@return void
function Stats:setTrippingRotAngle(TrippingRotAngle) end

---@public
---@return float @the Drunkenness
function Stats:getDrunkenness() end

---@public
---@return float @the endurancewarn
function Stats:getEndurancewarn() end

---@public
---@param stress float @the stress to set
---@return void
function Stats:setStress(stress) end
