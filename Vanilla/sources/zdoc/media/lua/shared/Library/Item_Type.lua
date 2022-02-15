---@class Item.Type : zombie.scripting.objects.Item.Type
---@field public Normal Item.Type
---@field public Weapon Item.Type
---@field public Food Item.Type
---@field public Literature Item.Type
---@field public Drainable Item.Type
---@field public Clothing Item.Type
---@field public Container Item.Type
---@field public WeaponPart Item.Type
---@field public Key Item.Type
---@field public KeyRing Item.Type
---@field public Moveable Item.Type
---@field public Radio Item.Type
---@field public AlarmClock Item.Type
---@field public AlarmClockClothing Item.Type
---@field public Map Item.Type
Item_Type = {}

---Returns an array containing the constants of this enum type, in
---
---the order they are declared.  This method may be used to iterate
---
---over the constants as follows:
---
---
---
---for (Item.Type c : Item.Type.values())
---
---    System.out.println(c);
---
---
---@public
---@return Item.Type[] @an array containing the constants of this enum type, in the order they are declared
function Item_Type:values() end

---Returns the enum constant of this type with the specified name.
---
---The string must match exactly an identifier used to declare an
---
---enum constant in this type.  (Extraneous whitespace characters are
---
---not permitted.)
---@public
---@param name String @the name of the enum constant to be returned.
---@return Item.Type @the enum constant with the specified name
function Item_Type:valueOf(name) end
