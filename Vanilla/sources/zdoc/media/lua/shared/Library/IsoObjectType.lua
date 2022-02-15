---@class IsoObjectType : zombie.iso.SpriteDetails.IsoObjectType
---@field public normal IsoObjectType
---@field public jukebox IsoObjectType
---@field public wall IsoObjectType
---@field public stairsTW IsoObjectType
---@field public stairsTN IsoObjectType
---@field public stairsMW IsoObjectType
---@field public stairsMN IsoObjectType
---@field public stairsBW IsoObjectType
---@field public stairsBN IsoObjectType
---@field public UNUSED9 IsoObjectType
---@field public UNUSED10 IsoObjectType
---@field public doorW IsoObjectType
---@field public doorN IsoObjectType
---@field public lightswitch IsoObjectType
---@field public radio IsoObjectType
---@field public curtainN IsoObjectType
---@field public curtainS IsoObjectType
---@field public curtainW IsoObjectType
---@field public curtainE IsoObjectType
---@field public doorFrW IsoObjectType
---@field public doorFrN IsoObjectType
---@field public tree IsoObjectType
---@field public windowFN IsoObjectType
---@field public windowFW IsoObjectType
---@field public UNUSED24 IsoObjectType
---@field public WestRoofB IsoObjectType
---@field public WestRoofM IsoObjectType
---@field public WestRoofT IsoObjectType
---@field public isMoveAbleObject IsoObjectType
---@field public MAX IsoObjectType
---@field private index int
---@field private fromStringMap HashMap|Unknown|Unknown
IsoObjectType = {}

---@public
---@return int
function IsoObjectType:index() end

---Returns the enum constant of this type with the specified name.
---
---The string must match exactly an identifier used to declare an
---
---enum constant in this type.  (Extraneous whitespace characters are
---
---not permitted.)
---@public
---@param name String @the name of the enum constant to be returned.
---@return IsoObjectType @the enum constant with the specified name
function IsoObjectType:valueOf(name) end

---@public
---@param str String
---@return IsoObjectType
function IsoObjectType:FromString(str) end

---@public
---@param value int
---@return IsoObjectType
function IsoObjectType:fromIndex(value) end

---Returns an array containing the constants of this enum type, in
---
---the order they are declared.  This method may be used to iterate
---
---over the constants as follows:
---
---
---
---for (IsoObjectType c : IsoObjectType.values())
---
---    System.out.println(c);
---
---
---@public
---@return IsoObjectType[] @an array containing the constants of this enum type, in the order they are declared
function IsoObjectType:values() end
