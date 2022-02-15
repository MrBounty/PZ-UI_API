---@class Nutrition : zombie.characters.BodyDamage.Nutrition
---@field private parent IsoPlayer
---@field private carbohydrates float
---@field private lipids float
---@field private proteins float
---@field private calories float
---@field private carbohydratesDecreraseFemale float
---@field private carbohydratesDecreraseMale float
---@field private lipidsDecreraseFemale float
---@field private lipidsDecreraseMale float
---@field private proteinsDecreraseFemale float
---@field private proteinsDecreraseMale float
---@field private caloriesDecreraseFemaleNormal float
---@field private caloriesDecreaseMaleNormal float
---@field private caloriesDecreraseFemaleExercise float
---@field private caloriesDecreaseMaleExercise float
---@field private caloriesDecreraseFemaleSleeping float
---@field private caloriesDecreaseMaleSleeping float
---@field private caloriesToGainWeightMale int
---@field private caloriesToGainWeightMaxMale int
---@field private caloriesToGainWeightFemale int
---@field private caloriesToGainWeightMaxFemale int
---@field private caloriesDecreaseMax int
---@field private weightGain float
---@field private weightLoss float
---@field private weight float
---@field private updatedWeight int
---@field private isFemale boolean
---@field private syncWeightTimer int
---@field private caloriesMax float
---@field private caloriesMin float
---@field private incWeight boolean
---@field private incWeightLot boolean
---@field private decWeight boolean
Nutrition = {}

---@public
---@param arg0 float
---@return void
function Nutrition:setLipids(arg0) end

---@public
---@param arg0 float
---@return void
function Nutrition:setCalories(arg0) end

---@public
---@param arg0 boolean
---@return void
function Nutrition:setIncWeightLot(arg0) end

---@public
---@return float
function Nutrition:getLipids() end

---@public
---@return void
function Nutrition:applyWeightFromTraits() end

---@public
---@return float
function Nutrition:getProteins() end

---@public
---@return float
function Nutrition:getWeight() end

---@public
---@return boolean
function Nutrition:canAddFitnessXp() end

---@public
---@return void
function Nutrition:applyTraitFromWeight() end

---@private
---@return void
function Nutrition:updateCalories() end

---@public
---@return boolean
function Nutrition:characterHaveWeightTrouble() end

---@public
---@param arg0 boolean
---@return void
function Nutrition:setIncWeight(arg0) end

---@public
---@return boolean
function Nutrition:isDecWeight() end

---@private
---@return void
function Nutrition:updateWeight() end

---@public
---@return float
function Nutrition:getCalories() end

---@public
---@return boolean
function Nutrition:isIncWeightLot() end

---@public
---@param arg0 ByteBuffer
---@return void
function Nutrition:load(arg0) end

---@public
---@param arg0 float
---@return void
function Nutrition:setWeight(arg0) end

---@public
---@param arg0 ByteBuffer
---@return void
function Nutrition:save(arg0) end

---@public
---@param arg0 float
---@return void
function Nutrition:setProteins(arg0) end

---@public
---@param arg0 boolean
---@return void
function Nutrition:setDecWeight(arg0) end

---@public
---@param arg0 float
---@return void
function Nutrition:setCarbohydrates(arg0) end

---@public
---@return boolean
function Nutrition:isIncWeight() end

---@public
---@return void
function Nutrition:update() end

---@public
---@return float
function Nutrition:getCarbohydrates() end
