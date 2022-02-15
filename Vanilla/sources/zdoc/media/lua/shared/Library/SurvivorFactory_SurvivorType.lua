---@class SurvivorFactory.SurvivorType : zombie.characters.SurvivorFactory.SurvivorType
---@field public Friendly SurvivorFactory.SurvivorType
---@field public Neutral SurvivorFactory.SurvivorType
---@field public Aggressive SurvivorFactory.SurvivorType
SurvivorFactory_SurvivorType = {}

---Returns the enum constant of this type with the specified name.
---
---The string must match exactly an identifier used to declare an
---
---enum constant in this type.  (Extraneous whitespace characters are
---
---not permitted.)
---@public
---@param name String @the name of the enum constant to be returned.
---@return SurvivorFactory.SurvivorType @the enum constant with the specified name
function SurvivorFactory_SurvivorType:valueOf(name) end

---Returns an array containing the constants of this enum type, in
---
---the order they are declared.  This method may be used to iterate
---
---over the constants as follows:
---
---
---
---for (SurvivorFactory.SurvivorType c : SurvivorFactory.SurvivorType.values())
---
---    System.out.println(c);
---
---
---@public
---@return SurvivorFactory.SurvivorType[] @an array containing the constants of this enum type, in the order they are declared
function SurvivorFactory_SurvivorType:values() end
