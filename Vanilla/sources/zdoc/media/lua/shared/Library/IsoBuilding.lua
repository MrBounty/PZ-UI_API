---@class IsoBuilding : zombie.iso.areas.IsoBuilding
---@field public bounds Rectangle
---@field public Exits Vector|IsoRoomExit
---@field public IsResidence boolean
---@field public container ArrayList|ItemContainer
---@field public Rooms Vector|IsoRoom
---@field public Windows Vector|IsoWindow
---@field public ID int
---@field public IDMax int
---@field public safety int
---@field public transparentWalls int
---@field private isToxic boolean
---@field public PoorBuildingScore float
---@field public GoodBuildingScore float
---@field public scoreUpdate int
---@field public def BuildingDef
---@field public bSeenInside boolean
---@field public lights ArrayList|IsoLightSource
---@field tempo ArrayList|Unknown
---@field tempContainer ArrayList|Unknown
---@field RandomContainerChoices ArrayList|Unknown
---@field windowchoices ArrayList|Unknown
IsoBuilding = {}

---@public
---@return void
function IsoBuilding:update() end

---@public
---@return int
function IsoBuilding:getID() end

---@public
---@return void
function IsoBuilding:CalculateWindows() end

---@public
---@return IsoRoom
---@overload fun(room:String)
function IsoBuilding:getRandomRoom() end

---@public
---@param room String
---@return IsoRoom
function IsoBuilding:getRandomRoom(room) end

---@public
---@return void
function IsoBuilding:TriggerAlarm() end

---@public
---@param room String
---@return boolean
function IsoBuilding:containsRoom(room) end

---@public
---@param isToxic boolean
---@return void
function IsoBuilding:setToxic(isToxic) end

---@public
---@return int
function IsoBuilding:getRoomsNumber() end

---@public
---@return IsoGridSquare
function IsoBuilding:getFreeTile() end

---@public
---@return boolean
function IsoBuilding:hasWater() end

---@private
---@param arg0 BuildingScore
---@return BuildingScore
function IsoBuilding:ScoreBuildingGeneral(arg0) end

---@public
---@return void
function IsoBuilding:forceAwake() end

---@public
---@return IsoWindow
function IsoBuilding:getRandomFirstFloorWindow() end

---@public
---@return boolean
function IsoBuilding:isAllExplored() end

---@public
---@param obj IsoWindow
---@param bOtherTile boolean
---@return void
---@overload fun(obj:IsoWindow, bOtherTile:boolean, from:IsoGridSquare, building:IsoBuilding)
function IsoBuilding:addWindow(obj, bOtherTile) end

---@public
---@param obj IsoWindow
---@param bOtherTile boolean
---@param from IsoGridSquare
---@param building IsoBuilding
---@return void
function IsoBuilding:addWindow(obj, bOtherTile, from, building) end

---@public
---@param b boolean
---@return void
function IsoBuilding:setAllExplored(b) end

---@public
---@return void
function IsoBuilding:CalculateExits() end

---@public
---@return void
function IsoBuilding:FillContainers() end

---@public
---@return BuildingDef
function IsoBuilding:getDef() end

---@public
---@param obj IsoDoor
---@param bOtherTile boolean
---@return void
---@overload fun(obj:IsoDoor, bOtherTile:boolean, from:IsoGridSquare, building:IsoBuilding)
function IsoBuilding:addDoor(obj, bOtherTile) end

---@public
---@param obj IsoDoor
---@param bOtherTile boolean
---@param from IsoGridSquare
---@param building IsoBuilding
---@return void
function IsoBuilding:addDoor(obj, bOtherTile, from, building) end

---@public
---@param room IsoRoom
---@return void
function IsoBuilding:AddRoom(room) end

---@public
---@param type String
---@return ItemContainer
function IsoBuilding:getRandomContainer(type) end

---@public
---@return boolean
function IsoBuilding:isResidential() end

---@public
---@return boolean
function IsoBuilding:isToxic() end

---@public
---@param items Stack|String
---@return boolean
function IsoBuilding:ContainsAllItems(items) end

---@public
---@param building BuildingDef
---@param info LotHeader
---@return void
function IsoBuilding:CreateFrom(building, info) end

---@public
---@param desc SurvivorDesc
---@param bFarGood boolean
---@return float
function IsoBuilding:ScoreBuildingPersonSpecific(desc, bFarGood) end

---@public
---@param itemType ItemType
---@return ItemContainer
function IsoBuilding:getContainerWith(itemType) end
