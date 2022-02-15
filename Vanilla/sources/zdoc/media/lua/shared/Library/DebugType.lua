---@class DebugType : zombie.debug.DebugType
---@field public NetworkPacketDebug DebugType
---@field public NetworkFileDebug DebugType
---@field public Network DebugType
---@field public General DebugType
---@field public Lua DebugType
---@field public Mod DebugType
---@field public Sound DebugType
---@field public Zombie DebugType
---@field public Combat DebugType
---@field public Objects DebugType
---@field public Fireplace DebugType
---@field public Radio DebugType
---@field public MapLoading DebugType
---@field public Clothing DebugType
---@field public Animation DebugType
---@field public Asset DebugType
---@field public Script DebugType
---@field public Shader DebugType
---@field public Input DebugType
---@field public Recipe DebugType
---@field public ActionSystem DebugType
---@field public IsoRegion DebugType
---@field public UnitTests DebugType
---@field public FileIO DebugType
---@field public Multiplayer DebugType
---@field public Ownership DebugType
---@field public Death DebugType
---@field public Damage DebugType
---@field public Statistic DebugType
---@field public Vehicle DebugType
DebugType = {}

---Returns the enum constant of this type with the specified name.
---
---The string must match exactly an identifier used to declare an
---
---enum constant in this type.  (Extraneous whitespace characters are
---
---not permitted.)
---@public
---@param name String @the name of the enum constant to be returned.
---@return DebugType @the enum constant with the specified name
function DebugType:valueOf(name) end

---@public
---@param arg0 DebugType
---@return boolean
function DebugType:Do(arg0) end

---Returns an array containing the constants of this enum type, in
---
---the order they are declared.  This method may be used to iterate
---
---over the constants as follows:
---
---
---
---for (DebugType c : DebugType.values())
---
---    System.out.println(c);
---
---
---@public
---@return DebugType[] @an array containing the constants of this enum type, in the order they are declared
function DebugType:values() end
