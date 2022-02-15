---@class IsoRoom : zombie.iso.areas.IsoRoom
---@field private tempSquares ArrayList|Unknown
---@field public Beds Vector|IsoGridSquare
---@field public bounds Rectangle
---@field public building IsoBuilding
---@field public Containers ArrayList|ItemContainer
---@field public Windows ArrayList|IsoWindow
---@field public Exits Vector|IsoRoomExit
---@field public layer int
---@field public RoomDef String
---@field public TileList Vector|IsoGridSquare
---@field public transparentWalls int
---@field public lightSwitches ArrayList|IsoLightSwitch
---@field public roomLights ArrayList|Unknown
---@field public WaterSources ArrayList|IsoObject
---@field public seen int
---@field public visited int
---@field public def RoomDef
---@field public rects ArrayList|RoomDef.RoomRect
---@field public Squares ArrayList|IsoGridSquare
IsoRoom = {}

---@public
---@return void
function IsoRoom:onSee() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function IsoRoom:isInside(arg0, arg1, arg2) end

---@public
---@return ArrayList|IsoWindow
function IsoRoom:getWindows() end

---@public
---@return IsoGridSquare
function IsoRoom:getRandomSquare() end

---@public
---@return ArrayList|ItemContainer
function IsoRoom:getContainer() end

---@public
---@param WaterSources ArrayList|IsoObject @the WaterSources to set
---@return void
function IsoRoom:setWaterSources(WaterSources) end

---@public
---@param sq IsoGridSquare
---@return void
function IsoRoom:addSquare(sq) end

---@public
---@param cell IsoCell
---@return IsoBuilding
function IsoRoom:CreateBuilding(cell) end

---@public
---@return IsoBuilding
function IsoRoom:getBuilding() end

---@public
---@return void
function IsoRoom:refreshSquares() end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return IsoRoomExit
function IsoRoom:getExitAt(arg0, arg1, arg2) end

---@public
---@return Vector|IsoGridSquare
function IsoRoom:getTileList() end

---@public
---@return RoomDef
function IsoRoom:getRoomDef() end

---@public
---@return ArrayList|Unknown
function IsoRoom:getLightSwitches() end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoGridSquare
---@return void
function IsoRoom:addExitTo(arg0, arg1) end

---@public
---@param active boolean
---@return void
function IsoRoom:createLights(active) end

---@public
---@return String
function IsoRoom:getName() end

---@public
---@return void
function IsoRoom:spawnZombies() end

---@public
---@return boolean
function IsoRoom:hasLightSwitches() end

---@public
---@param sq IsoGridSquare
---@return void
function IsoRoom:removeSquare(sq) end

---@public
---@return IsoGridSquare
function IsoRoom:getRandomFreeSquare() end

---@public
---@return ArrayList|IsoGridSquare
function IsoRoom:getSquares() end

---@public
---@return ArrayList|IsoObject @the WaterSources
function IsoRoom:getWaterSources() end

---@public
---@return void
function IsoRoom:useWater() end

---@public
---@return IsoGridSquare
function IsoRoom:getFreeTile() end

---@public
---@return boolean
function IsoRoom:hasWater() end

---@param arg0 IsoBuilding
---@return void
function IsoRoom:AddToBuilding(arg0) end
