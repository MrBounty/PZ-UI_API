---@class SurvivorFactory : zombie.characters.SurvivorFactory
---@field public FemaleForenames ArrayList|String
---@field public MaleForenames ArrayList|String
---@field public Surnames ArrayList|String
SurvivorFactory = {}

---@public
---@param forename String
---@return void
function SurvivorFactory:addFemaleForename(forename) end

---@public
---@param desc SurvivorDesc
---@return void
function SurvivorFactory:randomName(desc) end

---@public
---@param nCount int
---@return SurvivorDesc[]
function SurvivorFactory:CreateFamily(nCount) end

---@public
---@param survivor SurvivorDesc
---@return void
function SurvivorFactory:setTorso(survivor) end

---@public
---@return SurvivorDesc
---@overload fun(survivorType:SurvivorFactory.SurvivorType)
---@overload fun(survivorType:SurvivorFactory.SurvivorType, bFemale:boolean)
function SurvivorFactory:CreateSurvivor() end

---@public
---@param survivorType SurvivorFactory.SurvivorType
---@return SurvivorDesc
function SurvivorFactory:CreateSurvivor(survivorType) end

---@public
---@param survivorType SurvivorFactory.SurvivorType
---@param bFemale boolean
---@return SurvivorDesc
function SurvivorFactory:CreateSurvivor(survivorType, bFemale) end

---@public
---@param forename String
---@return void
function SurvivorFactory:addMaleForename(forename) end

---@public
---@param surName String
---@return void
function SurvivorFactory:addSurname(surName) end

---@public
---@param nCount int
---@return SurvivorDesc[]
function SurvivorFactory:CreateSurvivorGroup(nCount) end

---@public
---@return void
function SurvivorFactory:Reset() end

---@public
---@param desc SurvivorDesc
---@param cell IsoCell
---@param x int
---@param y int
---@param z int
---@return IsoSurvivor
function SurvivorFactory:InstansiateInCell(desc, cell, x, y, z) end
