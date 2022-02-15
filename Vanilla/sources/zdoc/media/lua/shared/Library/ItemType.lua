---@class ItemType : zombie.inventory.ItemType
---@field public None ItemType
---@field public Weapon ItemType
---@field public Food ItemType
---@field public Literature ItemType
---@field public Drainable ItemType
---@field public Clothing ItemType
---@field public Key ItemType
---@field public KeyRing ItemType
---@field public Moveable ItemType
---@field public AlarmClock ItemType
---@field public AlarmClockClothing ItemType
---@field private index int
ItemType = {}

---Returns an array containing the constants of this enum type, in
---
---the order they are declared.  This method may be used to iterate
---
---over the constants as follows:
---
---
---
---for (ItemType c : ItemType.values())
---
---    System.out.println(c);
---
---
---@public
---@return ItemType[] @an array containing the constants of this enum type, in the order they are declared
function ItemType:values() end

---@public
---@param arg0 int
---@return ItemType
function ItemType:fromIndex(arg0) end

---@public
---@return int
function ItemType:index() end

---Returns the enum constant of this type with the specified name.
---
---The string must match exactly an identifier used to declare an
---
---enum constant in this type.  (Extraneous whitespace characters are
---
---not permitted.)
---@public
---@param name String @the name of the enum constant to be returned.
---@return ItemType @the enum constant with the specified name
function ItemType:valueOf(name) end
