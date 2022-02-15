---@class MapKnowledge : zombie.ai.MapKnowledge
---@field private knownBlockedEdges ArrayList|Unknown
MapKnowledge = {}

---@public
---@param arg0 IsoWindow
---@param arg1 boolean
---@return void
function MapKnowledge:setKnownBlockedWindow(arg0, arg1) end

---@public
---@param arg0 IsoObject
---@param arg1 boolean
---@return void
function MapKnowledge:setKnownBlockedWindowFrame(arg0, arg1) end

---@public
---@return ArrayList|Unknown
---@overload fun(arg0:int, arg1:int, arg2:int)
function MapKnowledge:getKnownBlockedEdges() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return KnownBlockedEdges
function MapKnowledge:getKnownBlockedEdges(arg0, arg1, arg2) end

---@public
---@param arg0 IsoThumpable
---@param arg1 boolean
---@return void
---@overload fun(arg0:IsoDoor, arg1:boolean)
function MapKnowledge:setKnownBlockedDoor(arg0, arg1) end

---@public
---@param arg0 IsoDoor
---@param arg1 boolean
---@return void
function MapKnowledge:setKnownBlockedDoor(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return KnownBlockedEdges
function MapKnowledge:getOrCreateKnownBlockedEdges(arg0, arg1, arg2) end

---@private
---@param arg0 KnownBlockedEdges
---@return void
function MapKnowledge:releaseIfEmpty(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 boolean
---@return void
function MapKnowledge:setKnownBlockedEdgeW(arg0, arg1, arg2, arg3) end

---@public
---@return void
function MapKnowledge:forget() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 boolean
---@return void
function MapKnowledge:setKnownBlockedEdgeN(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return KnownBlockedEdges
function MapKnowledge:createKnownBlockedEdges(arg0, arg1, arg2) end
