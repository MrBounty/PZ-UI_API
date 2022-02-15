---@class LosUtil : zombie.iso.LosUtil
---@field public XSIZE int
---@field public YSIZE int
---@field public ZSIZE int
---@field public cachedresults byte[][][][]
---@field public cachecleared boolean[]
LosUtil = {}

---@public
---@param width int
---@param height int
---@return void
function LosUtil:init(width, height) end

---@public
---@param cell IsoCell
---@param x1 int
---@param y1 int
---@param z1 int
---@param x0 int
---@param y0 int
---@param z0 int
---@param bIgnoreDoors boolean
---@param playerIndex int
---@return LosUtil.TestResults
function LosUtil:lineClearCached(cell, x1, y1, z1, x0, y0, z0, bIgnoreDoors, playerIndex) end

---@public
---@param chr IsoGameCharacter
---@param cell IsoCell
---@param x1 int
---@param y1 int
---@param z1 int
---@param x0 int
---@param y0 int
---@param z0 int
---@return int
function LosUtil:lineClearCollideCount(chr, cell, x1, y1, z1, x0, y0, z0) end

---Compute and return the list of RLPoints in line-of-sight to the given
---
---region. In general, this method should be very fast.
---@public
---@param cell IsoCell
---@param x0 int
---@param y0 int
---@param z0 int
---@param x1 int
---@param y1 int
---@param z1 int
---@param bIgnoreDoors boolean
---@return LosUtil.TestResults
---@overload fun(cell:IsoCell, x0:int, y0:int, z0:int, x1:int, y1:int, z1:int, bIgnoreDoors:boolean, RangeTillWindows:int)
function LosUtil:lineClear(cell, x0, y0, z0, x1, y1, z1, bIgnoreDoors) end

---@public
---@param cell IsoCell
---@param x0 int
---@param y0 int
---@param z0 int
---@param x1 int
---@param y1 int
---@param z1 int
---@param bIgnoreDoors boolean
---@param RangeTillWindows int
---@return LosUtil.TestResults
function LosUtil:lineClear(cell, x0, y0, z0, x1, y1, z1, bIgnoreDoors, RangeTillWindows) end

---@public
---@param x1 int
---@param y1 int
---@param z1 int
---@param x0 int
---@param y0 int
---@param z0 int
---@param bIgnoreDoors boolean
---@return boolean
function LosUtil:lineClearCollide(x1, y1, z1, x0, y0, z0, bIgnoreDoors) end
