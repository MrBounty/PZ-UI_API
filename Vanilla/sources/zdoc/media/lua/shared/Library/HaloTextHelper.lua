---@class HaloTextHelper : zombie.characters.HaloTextHelper
---@field public COLOR_WHITE HaloTextHelper.ColorRGB
---@field public COLOR_GREEN HaloTextHelper.ColorRGB
---@field public COLOR_RED HaloTextHelper.ColorRGB
---@field private queuedLines String[]
---@field private currentLines String[]
---@field private ignoreOverheadCheckOnce boolean
HaloTextHelper = {}

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@return void
---@overload fun(arg0:IsoPlayer, arg1:String, arg2:HaloTextHelper.ColorRGB)
---@overload fun(arg0:IsoPlayer, arg1:String, arg2:int, arg3:int, arg4:int)
function HaloTextHelper:addText(arg0, arg1) end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@param arg2 HaloTextHelper.ColorRGB
---@return void
function HaloTextHelper:addText(arg0, arg1, arg2) end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@return void
function HaloTextHelper:addText(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return HaloTextHelper.ColorRGB
function HaloTextHelper:getColorGreen() end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@param arg2 boolean
---@param arg3 HaloTextHelper.ColorRGB
---@return void
---@overload fun(arg0:IsoPlayer, arg1:String, arg2:boolean, arg3:HaloTextHelper.ColorRGB, arg4:HaloTextHelper.ColorRGB)
---@overload fun(arg0:IsoPlayer, arg1:String, arg2:boolean, arg3:int, arg4:int, arg5:int)
---@overload fun(arg0:IsoPlayer, arg1:String, arg2:boolean, arg3:int, arg4:int, arg5:int, arg6:int, arg7:int, arg8:int)
function HaloTextHelper:addTextWithArrow(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@param arg2 boolean
---@param arg3 HaloTextHelper.ColorRGB
---@param arg4 HaloTextHelper.ColorRGB
---@return void
function HaloTextHelper:addTextWithArrow(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@param arg2 boolean
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@return void
function HaloTextHelper:addTextWithArrow(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@param arg2 boolean
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 int
---@param arg7 int
---@param arg8 int
---@return void
function HaloTextHelper:addTextWithArrow(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@return HaloTextHelper.ColorRGB
function HaloTextHelper:getColorRed() end

---@private
---@param arg0 int
---@param arg1 String
---@return boolean
function HaloTextHelper:overheadContains(arg0, arg1) end

---@public
---@return void
function HaloTextHelper:forceNextAddText() end

---@public
---@return HaloTextHelper.ColorRGB
function HaloTextHelper:getColorWhite() end

---@public
---@return void
function HaloTextHelper:update() end
