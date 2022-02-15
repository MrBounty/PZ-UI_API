---@class SurvivorDesc : zombie.characters.SurvivorDesc
---@field public humanVisual HumanVisual
---@field public wornItems WornItems
---@field group SurvivorGroup
---@field private IDCount int
---@field public TrouserCommonColors ArrayList|Color
---@field public HairCommonColors ArrayList|Color
---@field public xpBoostMap HashMap|PerkFactory.Perks|Integer
---@field private metaTable KahluaTable
---@field public Profession String
---@field protected forename String
---@field protected ID int
---@field protected Instance IsoGameCharacter
---@field private bFemale boolean
---@field protected surname String
---@field private InventoryScript String
---@field protected torso String
---@field protected MetCount HashMap|Unknown|Unknown
---@field protected bravery float
---@field protected loner float
---@field protected aggressiveness float
---@field protected compassion float
---@field protected temper float
---@field protected friendliness float
---@field private favourindoors float
---@field protected loyalty float
---@field public extra ArrayList|String
---@field public Observations ArrayList|ObservationFactory.Observation
---@field private type SurvivorFactory.SurvivorType
---@field public bDead boolean
SurvivorDesc = {}

---@public
---@param forename String @the forename to set
---@return void
function SurvivorDesc:setForename(forename) end

---@public
---@return HashMap|Integer|Integer
---@overload fun(descriptor:SurvivorDesc)
function SurvivorDesc:getMetCount() end

---@public
---@param descriptor SurvivorDesc
---@return int
function SurvivorDesc:getMetCount(descriptor) end

---@public
---@return IsoGameCharacter @the Instance
function SurvivorDesc:getInstance() end

---@public
---@param arg0 String
---@return void
function SurvivorDesc:dressInNamedOutfit(arg0) end

---throws java.io.IOException
---@public
---@param input ByteBuffer
---@return void
function SurvivorDesc:loadCompact(input) end

---@private
---@param arg0 ByteBuffer
---@param arg1 PerkFactory.Perk
---@return void
function SurvivorDesc:savePerk(arg0, arg1) end

---@public
---@param desc SurvivorDesc
---@return void
function SurvivorDesc:meet(desc) end

---@public
---@param ID int @the ID to set
---@return void
function SurvivorDesc:setID(ID) end

---@public
---@param obv String
---@return void
function SurvivorDesc:addObservation(obv) end

---@public
---@param loner float @the loner to set
---@return void
function SurvivorDesc:setLoner(loner) end

---@public
---@return float @the bravery
function SurvivorDesc:getBravery() end

---@public
---@return String @the torso
function SurvivorDesc:getTorso() end

---@public
---@return int @the IDCount
function SurvivorDesc:getIDCount() end

---@public
---@return ArrayList|String
function SurvivorDesc:getExtras() end

---@public
---@return boolean
function SurvivorDesc:isLeader() end

---throws java.io.IOException
---@public
---@param input ByteBuffer
---@param WorldVersion int
---@param chr IsoGameCharacter
---@return void
function SurvivorDesc:load(input, WorldVersion, chr) end

---@public
---@return float @the friendliness
function SurvivorDesc:getFriendliness() end

---@public
---@param surname String @the surname to set
---@return void
function SurvivorDesc:setSurname(surname) end

---@public
---@param temper float @the temper to set
---@return void
function SurvivorDesc:setTemper(temper) end

---@public
---@return SurvivorFactory.SurvivorType
function SurvivorDesc:getType() end

---@public
---@return HashMap|PerkFactory.Perks|Integer
function SurvivorDesc:getXPBoostMap() end

---@public
---@return boolean
function SurvivorDesc:isSkeleton() end

---@public
---@param Instance IsoGameCharacter @the Instance to set
---@return void
function SurvivorDesc:setInstance(Instance) end

---@public
---@param profession ProfessionFactory.Profession
---@return void
function SurvivorDesc:setProfessionSkills(profession) end

---@public
---@param arg0 String
---@return InventoryItem
function SurvivorDesc:getWornItem(arg0) end

---@public
---@return float @the compassion
function SurvivorDesc:getCompassion() end

---@public
---@param compassion float @the compassion to set
---@return void
function SurvivorDesc:setCompassion(compassion) end

---@public
---@return String @the InventoryScript
function SurvivorDesc:getInventoryScript() end

---@public
---@return Color
function SurvivorDesc:getRandomSkinColor() end

---@public
---@return float @the aggressiveness
function SurvivorDesc:getAggressiveness() end

---@private
---@param arg0 ByteBuffer
---@param arg1 int
---@return PerkFactory.Perk
function SurvivorDesc:loadPerk(arg0, arg1) end

---@public
---@return float @the temper
function SurvivorDesc:getTemper() end

---@public
---@return float @the favourindoors
function SurvivorDesc:getFavourindoors() end

---@public
---@return boolean
function SurvivorDesc:isFriendly() end

---@public
---@param aIDCount int @the IDCount to set
---@return void
function SurvivorDesc:setIDCount(aIDCount) end

---throws java.io.IOException
---@public
---@param output ByteBuffer
---@return void
function SurvivorDesc:save(output) end

---@public
---@return HumanVisual
function SurvivorDesc:getHumanVisual() end

---@public
---@return ArrayList|ObservationFactory.Observation
function SurvivorDesc:getObservations() end

---@public
---@return SurvivorGroup @the Group
function SurvivorDesc:getGroup() end

---@public
---@param color ColorInfo
---@return void
function SurvivorDesc:addHairColor(color) end

---@public
---@param color ColorInfo
---@return void
function SurvivorDesc:addTrouserColor(color) end

---@public
---@return int @the ID
function SurvivorDesc:getID() end

---@public
---@return String @the Profession
function SurvivorDesc:getProfession() end

---@public
---@return float @the loyalty
function SurvivorDesc:getLoyalty() end

---@public
---@return String @the forename
function SurvivorDesc:getForename() end

---@public
---@param loyalty float @the loyalty to set
---@return void
function SurvivorDesc:setLoyalty(loyalty) end

---@public
---@return String @the surname
function SurvivorDesc:getSurname() end

---@private
---@return void
function SurvivorDesc:doStats() end

---@public
---@return ArrayList|Color
function SurvivorDesc:getCommonHairColor() end

---@public
---@return boolean
function SurvivorDesc:isAggressive() end

---@public
---@return int
function SurvivorDesc:getCalculatedToughness() end

---@public
---@return boolean
function SurvivorDesc:isZombie() end

---@public
---@param arg0 ItemVisuals
---@return void
function SurvivorDesc:getItemVisuals(arg0) end

---@public
---@param aggressiveness float @the aggressiveness to set
---@return void
function SurvivorDesc:setAggressiveness(aggressiveness) end

---@public
---@param Profession String @the Profession to set
---@return void
function SurvivorDesc:setProfession(Profession) end

---@public
---@param favourindoors float @the favourindoors to set
---@return void
function SurvivorDesc:setFavourindoors(favourindoors) end

---@public
---@param friendliness float @the friendliness to set
---@return void
function SurvivorDesc:setFriendliness(friendliness) end

---@public
---@return WornItems
function SurvivorDesc:getWornItems() end

---@public
---@return KahluaTable
function SurvivorDesc:getMeta() end

---@public
---@param bFemale boolean
---@return void
function SurvivorDesc:setFemale(bFemale) end

---@public
---@return boolean
function SurvivorDesc:isFemale() end

---@public
---@param o String
---@return boolean
function SurvivorDesc:hasObservation(o) end

---@public
---@param bravery float @the bravery to set
---@return void
function SurvivorDesc:setBravery(bravery) end

---@public
---@param arg0 String
---@param arg1 InventoryItem
---@return void
function SurvivorDesc:setWornItem(arg0, arg1) end

---@public
---@param InventoryScript String @the InventoryScript to set
---@return void
function SurvivorDesc:setInventoryScript(InventoryScript) end

---@public
---@param type SurvivorFactory.SurvivorType
---@return void
function SurvivorDesc:setType(type) end

---throws java.io.IOException
---@public
---@param output ByteBuffer
---@return void
function SurvivorDesc:saveCompact(output) end

---@public
---@param torso String @the torso to set
---@return void
function SurvivorDesc:setTorso(torso) end

---@public
---@return boolean
function SurvivorDesc:isDead() end

---@public
---@return float @the loner
function SurvivorDesc:getLoner() end
