---@class UIFont : zombie.ui.UIFont
---@field public Small UIFont
---@field public Medium UIFont
---@field public Large UIFont
---@field public Massive UIFont
---@field public MainMenu1 UIFont
---@field public MainMenu2 UIFont
---@field public Cred1 UIFont
---@field public Cred2 UIFont
---@field public NewSmall UIFont
---@field public NewMedium UIFont
---@field public NewLarge UIFont
---@field public Code UIFont
---@field public MediumNew UIFont
---@field public AutoNormSmall UIFont
---@field public AutoNormMedium UIFont
---@field public AutoNormLarge UIFont
---@field public Dialogue UIFont
---@field public Intro UIFont
---@field public Handwritten UIFont
---@field public DebugConsole UIFont
---@field public Title UIFont
UIFont = {}

---Returns the enum constant of this type with the specified name.
---
---The string must match exactly an identifier used to declare an
---
---enum constant in this type.  (Extraneous whitespace characters are
---
---not permitted.)
---@public
---@param name String @the name of the enum constant to be returned.
---@return UIFont @the enum constant with the specified name
function UIFont:valueOf(name) end

---Returns an array containing the constants of this enum type, in
---
---the order they are declared.  This method may be used to iterate
---
---over the constants as follows:
---
---
---
---for (UIFont c : UIFont.values())
---
---    System.out.println(c);
---
---
---@public
---@return UIFont[] @an array containing the constants of this enum type, in the order they are declared
function UIFont:values() end

---@public
---@param str String
---@return UIFont
function UIFont:FromString(str) end
