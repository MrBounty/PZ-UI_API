---@class SimpleDateFormat : java.text.SimpleDateFormat
---@field serialVersionUID long
---@field currentSerialVersion int
---@field private serialVersionOnStream int
---@field private pattern String
---@field private originalNumberFormat NumberFormat
---@field private originalNumberPattern String
---@field private minusSign char
---@field private hasFollowingMinusSign boolean
---@field private forceStandaloneForm boolean
---@field private compiledPattern char[]
---@field private TAG_QUOTE_ASCII_CHAR int
---@field private TAG_QUOTE_CHARS int
---@field private zeroDigit char
---@field private formatData DateFormatSymbols
---@field private defaultCenturyStart Date
---@field private defaultCenturyStartYear int
---@field private MILLIS_PER_MINUTE int
---@field private GMT String
---@field private cachedNumberFormatData ConcurrentMap|Unknown|Unknown
---@field private locale Locale
---@field useDateFormatSymbols boolean
---@field private PATTERN_INDEX_TO_CALENDAR_FIELD int[]
---@field private PATTERN_INDEX_TO_DATE_FORMAT_FIELD int[]
---@field private PATTERN_INDEX_TO_DATE_FORMAT_FIELD_ID DateFormat.Field[]
---@field private REST_OF_STYLES int[]
SimpleDateFormat = {}

---@public
---@param arg0 Object
---@return boolean
function SimpleDateFormat:equals(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 Format.FieldDelegate
---@param arg3 StringBuffer
---@param arg4 boolean
---@return void
function SimpleDateFormat:subFormat(arg0, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 String
---@return char[]
function SimpleDateFormat:compile(arg0) end

---@public
---@return Object
function SimpleDateFormat:clone() end

---@public
---@return String
function SimpleDateFormat:toPattern() end

---@public
---@return Date
function SimpleDateFormat:get2DigitYearStart() end

---@private
---@param arg0 char
---@return boolean
function SimpleDateFormat:isDigit(arg0) end

---@public
---@param arg0 String
---@param arg1 ParsePosition
---@return Date
function SimpleDateFormat:parse(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 Map|Unknown|Unknown
---@param arg4 CalendarBuilder
---@return int
---@overload fun(arg0:String, arg1:int, arg2:int, arg3:String[], arg4:CalendarBuilder)
function SimpleDateFormat:matchString(arg0, arg1, arg2, arg3, arg4) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 String[]
---@param arg4 CalendarBuilder
---@return int
function SimpleDateFormat:matchString(arg0, arg1, arg2, arg3, arg4) end

---@private
---@return void
function SimpleDateFormat:initializeDefaultCentury() end

---@private
---@param arg0 int
---@param arg1 Locale
---@return Map|Unknown|Unknown
function SimpleDateFormat:getDisplayContextNamesMap(arg0, arg1) end

---@public
---@param arg0 String
---@return void
function SimpleDateFormat:applyPattern(arg0) end

---@public
---@param arg0 String
---@return void
function SimpleDateFormat:applyLocalizedPattern(arg0) end

---@private
---@param arg0 ObjectInputStream
---@return void
function SimpleDateFormat:readObject(arg0) end

---@private
---@return boolean
function SimpleDateFormat:useDateFormatSymbols() end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 CalendarBuilder
---@return int
function SimpleDateFormat:subParseZoneString(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 boolean
---@param arg5 CalendarBuilder
---@return int
function SimpleDateFormat:subParseNumericZone(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@return int
function SimpleDateFormat:hashCode() end

---@private
---@param arg0 Locale
---@return void
function SimpleDateFormat:initializeCalendar(arg0) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 String[]
---@return int
function SimpleDateFormat:matchZoneString(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 StringBuffer
---@return void
function SimpleDateFormat:zeroPaddingNumber(arg0, arg1, arg2, arg3) end

---@public
---@return String
function SimpleDateFormat:toLocalizedPattern() end

---@public
---@param arg0 Date
---@return void
function SimpleDateFormat:set2DigitYearStart(arg0) end

---@private
---@param arg0 Date
---@param arg1 StringBuffer
---@param arg2 Format.FieldDelegate
---@return StringBuffer
---@overload fun(arg0:Date, arg1:StringBuffer, arg2:FieldPosition)
function SimpleDateFormat:format(arg0, arg1, arg2) end

---@public
---@param arg0 Date
---@param arg1 StringBuffer
---@param arg2 FieldPosition
---@return StringBuffer
function SimpleDateFormat:format(arg0, arg1, arg2) end

---@private
---@param arg0 Date
---@return void
function SimpleDateFormat:parseAmbiguousDatesAsAfter(arg0) end

---@private
---@param arg0 String
---@return void
function SimpleDateFormat:applyPatternImpl(arg0) end

---@private
---@param arg0 int
---@param arg1 Locale
---@return Map|Unknown|Unknown
function SimpleDateFormat:getDisplayNamesMap(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@param arg2 StringBuilder
---@return void
function SimpleDateFormat:encode(arg0, arg1, arg2) end

---@public
---@return DateFormatSymbols
function SimpleDateFormat:getDateFormatSymbols() end

---@private
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@return String
function SimpleDateFormat:translatePattern(arg0, arg1, arg2) end

---@private
---@param arg0 Locale
---@return void
function SimpleDateFormat:initialize(arg0) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 String[][]
---@return boolean
function SimpleDateFormat:matchDSTString(arg0, arg1, arg2, arg3, arg4) end

---@private
---@return void
function SimpleDateFormat:checkNegativeNumberExpression() end

---@public
---@param arg0 DateFormatSymbols
---@return void
function SimpleDateFormat:setDateFormatSymbols(arg0) end

---@private
---@param arg0 int
---@param arg1 int
---@return boolean
function SimpleDateFormat:shouldObeyCount(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 boolean
---@param arg5 boolean[]
---@param arg6 ParsePosition
---@param arg7 boolean
---@param arg8 CalendarBuilder
---@return int
function SimpleDateFormat:subParse(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param arg0 Object
---@return AttributedCharacterIterator
function SimpleDateFormat:formatToCharacterIterator(arg0) end
