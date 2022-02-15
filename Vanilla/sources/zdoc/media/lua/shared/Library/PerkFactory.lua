---@class PerkFactory : zombie.characters.skills.PerkFactory
---@field public PerkList ArrayList|PerkFactory.Perk
---@field private PerkById HashMap|Unknown|Unknown
---@field private PerkByName HashMap|Unknown|Unknown
---@field private PerkByIndex PerkFactory.Perk[]
---@field private NextPerkID int
---@field PerkXPReqMultiplier float
PerkFactory = {}

---@public
---@param arg0 PerkFactory.Perk
---@param arg1 String
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@param arg8 int
---@param arg9 int
---@param arg10 int
---@param arg11 int
---@return PerkFactory.Perk
---@overload fun(arg0:PerkFactory.Perk, arg1:String, arg2:PerkFactory.Perk, arg3:int, arg4:int, arg5:int, arg6:int, arg7:int, arg8:int, arg9:int, arg10:int, arg11:int, arg12:int)
---@overload fun(arg0:PerkFactory.Perk, arg1:String, arg2:int, arg3:int, arg4:int, arg5:int, arg6:int, arg7:int, arg8:int, arg9:int, arg10:int, arg11:int, arg12:boolean)
---@overload fun(arg0:PerkFactory.Perk, arg1:String, arg2:PerkFactory.Perk, arg3:int, arg4:int, arg5:int, arg6:int, arg7:int, arg8:int, arg9:int, arg10:int, arg11:int, arg12:int, arg13:boolean)
function PerkFactory:AddPerk(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) end

---@public
---@param arg0 PerkFactory.Perk
---@param arg1 String
---@param arg2 PerkFactory.Perk
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@param arg8 int
---@param arg9 int
---@param arg10 int
---@param arg11 int
---@param arg12 int
---@return PerkFactory.Perk
function PerkFactory:AddPerk(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) end

---@public
---@param arg0 PerkFactory.Perk
---@param arg1 String
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@param arg8 int
---@param arg9 int
---@param arg10 int
---@param arg11 int
---@param arg12 boolean
---@return PerkFactory.Perk
function PerkFactory:AddPerk(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12) end

---@public
---@param arg0 PerkFactory.Perk
---@param arg1 String
---@param arg2 PerkFactory.Perk
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@param arg8 int
---@param arg9 int
---@param arg10 int
---@param arg11 int
---@param arg12 int
---@param arg13 boolean
---@return PerkFactory.Perk
function PerkFactory:AddPerk(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13) end

---@public
---@return void
function PerkFactory:initTranslations() end

---@public
---@param arg0 PerkFactory.Perk
---@return PerkFactory.Perk
function PerkFactory:getPerk(arg0) end

---@public
---@param arg0 String
---@return PerkFactory.Perk
function PerkFactory:getPerkFromName(arg0) end

---@public
---@return void
function PerkFactory:Reset() end

---@public
---@param arg0 PerkFactory.Perk
---@return String
function PerkFactory:getPerkName(arg0) end

---@public
---@return void
function PerkFactory:init() end
