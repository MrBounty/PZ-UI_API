---@class ErosionSeason : zombie.erosion.season.ErosionSeason
---@field public SEASON_DEFAULT int
---@field public SEASON_SPRING int
---@field public SEASON_SUMMER int
---@field public SEASON_SUMMER2 int
---@field public SEASON_AUTUMN int
---@field public SEASON_WINTER int
---@field public NUM_SEASONS int
---@field private lat int
---@field private tempMax int
---@field private tempMin int
---@field private tempDiff int
---@field private highNoon float
---@field private highNoonCurrent float
---@field private seasonLag int
---@field private rain float[]
---@field private suSol double
---@field private wiSol double
---@field private zeroDay GregorianCalendar
---@field private day int
---@field private month int
---@field private year int
---@field private isH1 boolean
---@field private yearData ErosionSeason.YearData[]
---@field private curSeason int
---@field private curSeasonDay float
---@field private curSeasonDays float
---@field private curSeasonStrength float
---@field private curSeasonProgression float
---@field private dayMeanTemperature float
---@field private dayTemperature float
---@field private dayNoiseVal float
---@field private isRainDay boolean
---@field private rainYearAverage float
---@field private rainDayStrength float
---@field private isThunderDay boolean
---@field private isSunnyDay boolean
---@field private dayDusk float
---@field private dayDawn float
---@field private dayDaylight float
---@field private winterMod float
---@field private summerMod float
---@field private summerTilt float
---@field private curDayPercent float
---@field private per Noise2D
---@field private seedA int
---@field private seedB int
---@field private seedC int
---@field names String[]
ErosionSeason = {}

---@public
---@param _lat int
---@param _tempMax int
---@param _tempMin int
---@param _tempDiff int
---@param _seasonLag int
---@param _noon float
---@param _seedA int
---@param _seedB int
---@param _seedC int
---@return void
function ErosionSeason:init(_lat, _tempMax, _tempMin, _tempDiff, _seasonLag, _noon, _seedA, _seedB, _seedC) end

---@public
---@return double
function ErosionSeason:getMaxDaylightWinter() end

---@public
---@return float
function ErosionSeason:getSeasonDays() end

---@private
---@param arg0 float
---@param arg1 GregorianCalendar
---@param arg2 int
---@param arg3 int
---@return void
function ErosionSeason:setSeasonData(arg0, arg1, arg2, arg3) end

---@public
---@return float
function ErosionSeason:getDayHighNoon() end

---@public
---@return float
function ErosionSeason:getDaylight() end

---@public
---@return String
function ErosionSeason:getSeasonName() end

---@public
---@return float
function ErosionSeason:getSeasonDay() end

---@private
---@param arg0 double
---@return double
function ErosionSeason:degree(arg0) end

---@public
---@param season int
---@return void
function ErosionSeason:setCurSeason(season) end

---@private
---@param arg0 int
---@return void
function ErosionSeason:setYearData(arg0) end

---Overrides:
---
---clone in class java.lang.Object
---@public
---@return ErosionSeason
function ErosionSeason:clone() end

---@public
---@return int
function ErosionSeason:getSeedC() end

---@public
---@return void
function ErosionSeason:Reset() end

---@public
---@return int
function ErosionSeason:getSeasonLag() end

---@public
---@param _jan float
---@param _feb float
---@param _mar float
---@param _apr float
---@param _may float
---@param _jun float
---@param _jul float
---@param _aug float
---@param _sep float
---@param _oct float
---@param _nov float
---@param _dec float
---@return void
function ErosionSeason:setRain(_jan, _feb, _mar, _apr, _may, _jun, _jul, _aug, _sep, _oct, _nov, _dec) end

---@public
---@return float
function ErosionSeason:getDusk() end

---@public
---@param _day int
---@param _month int
---@param _year int
---@return void
function ErosionSeason:setDay(_day, _month, _year) end

---@public
---@return float
function ErosionSeason:getSeasonProgression() end

---@public
---@return double
function ErosionSeason:getMaxDaylightSummer() end

---@public
---@return int
function ErosionSeason:getTempMin() end

---@public
---@return float
function ErosionSeason:getDayNoiseVal() end

---@public
---@return int
function ErosionSeason:getTempMax() end

---@public
---@return boolean
function ErosionSeason:isThunderDay() end

---@public
---@return float
function ErosionSeason:getDayTemperature() end

---@public
---@return int
function ErosionSeason:getSeedB() end

---@public
---@param day int
---@param month int
---@param year int
---@return GregorianCalendar
function ErosionSeason:getWinterStartDay(day, month, year) end

---@private
---@param arg0 long
---@param arg1 GregorianCalendar
---@return void
function ErosionSeason:setDaylightData(arg0, arg1) end

---@public
---@return int
function ErosionSeason:getTempDiff() end

---@public
---@return float
function ErosionSeason:getHighNoon() end

---@private
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@return double
function ErosionSeason:clerp(arg0, arg1, arg2) end

---@public
---@return int
function ErosionSeason:getLat() end

---@public
---@return boolean
function ErosionSeason:isSunnyDay() end

---@private
---@param arg0 double
---@param arg1 double
---@param arg2 double
---@return double
function ErosionSeason:lerp(arg0, arg1, arg2) end

---@public
---@return float
function ErosionSeason:getRainDayStrength() end

---@private
---@param arg0 GregorianCalendar
---@param arg1 GregorianCalendar
---@return float
function ErosionSeason:dayDiff(arg0, arg1) end

---@public
---@return float
function ErosionSeason:getDayMeanTemperature() end

---@public
---@return float
function ErosionSeason:getCurDayPercent() end

---@public
---@return int
function ErosionSeason:getSeedA() end

---@public
---@param _season int
---@return boolean
function ErosionSeason:isSeason(_season) end

---@public
---@return float
function ErosionSeason:getDawn() end

---@private
---@param arg0 double
---@return double
function ErosionSeason:radian(arg0) end

---@public
---@return int
function ErosionSeason:getSeason() end

---@public
---@return float
function ErosionSeason:getRainYearAverage() end

---@public
---@return float
function ErosionSeason:getSeasonStrength() end

---@public
---@return boolean
function ErosionSeason:isRainDay() end
