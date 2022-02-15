---@class Clock : zombie.ui.Clock
---@field background Texture
---@field digitsLarge Texture[]
---@field digitsSmall Texture[]
---@field colon Texture
---@field slash Texture
---@field minus Texture
---@field dot Texture
---@field tempC Texture
---@field tempF Texture
---@field tempE Texture
---@field texAM Texture
---@field texPM Texture
---@field alarmOn Texture
---@field alarmRinging Texture
---@field displayColour Color
---@field ghostColour Color
---@field uxOriginal int
---@field uyOriginal int
---@field largeDigitSpacing int
---@field smallDigitSpacing int
---@field colonSpacing int
---@field ampmSpacing int
---@field alarmBellSpacing int
---@field decimalSpacing int
---@field degreeSpacing int
---@field slashSpacing int
---@field tempDateSpacing int
---@field dateOffset int
---@field minusOffset int
---@field amVerticalSpacing int
---@field pmVerticalSpacing int
---@field alarmBellVerticalSpacing int
---@field displayVerticalSpacing int
---@field decimalVerticalSpacing int
---@field public digital boolean
---@field public isAlarmSet boolean
---@field public isAlarmRinging boolean
---@field private clockPlayer IsoPlayer
---@field public instance Clock
Clock = {}

---@private
---@return int[]
function Clock:tempDigits() end

---Overrides:
---
---render in class UIElement
---@public
---@return void
function Clock:render() end

---@private
---@param arg0 boolean
---@param arg1 Color
---@return void
function Clock:renderDisplay(arg0, arg1) end

---@private
---@return int[]
function Clock:dateDigits() end

---@public
---@return boolean
function Clock:isDateVisible() end

---@private
---@return void
function Clock:assignSmallOffsets() end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function Clock:onMouseDown(arg0, arg1) end

---@public
---@return void
function Clock:resize() end

---@private
---@return void
function Clock:assignLargeOffsets() end

---@private
---@param arg0 boolean
---@return void
function Clock:assignTextures(arg0) end

---@private
---@return int[]
function Clock:timeDigits() end
