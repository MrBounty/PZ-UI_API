---@class IsoSurvivor : zombie.characters.IsoSurvivor
---@field public NoGoreDeath boolean
---@field public Draggable boolean
---@field public following IsoGameCharacter
---@field Dragging boolean
---@field repathDelay int
---@field public nightsSurvived int
---@field public ping int
---@field public collidePushable IsoPushableObject
---@field private tryToTeamUp boolean
---@field NeightbourUpdate int
---@field NeightbourUpdateMax int
IsoSurvivor = {}

---Overrides:
---
---Despawn in class IsoMovingObject
---@public
---@return void
function IsoSurvivor:Despawn() end

---@public
---@return void
function IsoSurvivor:reloadSpritePart() end

---Overrides:
---
---getObjectName in class IsoMovingObject
---@public
---@return String
function IsoSurvivor:getObjectName() end
