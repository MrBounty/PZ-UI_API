---@class WorldSoundManager : zombie.WorldSoundManager
---@field public instance WorldSoundManager
---@field public SoundList ArrayList|WorldSoundManager.WorldSound
---@field public freeSounds Stack|WorldSoundManager.WorldSound
---@field private resultBiggestSound WorldSoundManager.ResultBiggestSound
WorldSoundManager = {}

---@public
---@return void
function WorldSoundManager:update() end

---@public
---@param arg0 Object
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@return WorldSoundManager.WorldSound
---@overload fun(arg0:Object, arg1:int, arg2:int, arg3:int, arg4:int, arg5:int, arg6:boolean)
---@overload fun(arg0:Object, arg1:int, arg2:int, arg3:int, arg4:int, arg5:int, arg6:boolean, arg7:float, arg8:float)
---@overload fun(arg0:Object, arg1:int, arg2:int, arg3:int, arg4:int, arg5:int, arg6:boolean, arg7:float, arg8:float, arg9:boolean, arg10:boolean, arg11:boolean)
function WorldSoundManager:addSound(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 Object
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 boolean
---@return WorldSoundManager.WorldSound
function WorldSoundManager:addSound(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 Object
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 boolean
---@param arg7 float
---@param arg8 float
---@return WorldSoundManager.WorldSound
function WorldSoundManager:addSound(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param arg0 Object
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 boolean
---@param arg7 float
---@param arg8 float
---@param arg9 boolean
---@param arg10 boolean
---@param arg11 boolean
---@return WorldSoundManager.WorldSound
function WorldSoundManager:addSound(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11) end

---@public
---@return void
function WorldSoundManager:initFrame() end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 float
---@param arg9 int
---@return void
function WorldSoundManager:DrawIsoLine(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9) end

---@public
---@param arg0 WorldSoundManager.WorldSound
---@param arg1 IsoZombie
---@return float
function WorldSoundManager:getSoundAttract(arg0, arg1) end

---@public
---@param cell IsoCell
---@return void
function WorldSoundManager:init(cell) end

---@public
---@param arg0 IsoZombie
---@return WorldSoundManager.WorldSound
function WorldSoundManager:getSoundZomb(arg0) end

---@public
---@param arg0 Object
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 boolean
---@return WorldSoundManager.WorldSound
function WorldSoundManager:addSoundRepeating(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param x int
---@param y int
---@param z int
---@return float
function WorldSoundManager:getStressFromSounds(x, y, z) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@param arg3 boolean
---@param arg4 IsoZombie
---@return WorldSoundManager.ResultBiggestSound
function WorldSoundManager:getBiggestSoundZomb(arg0, arg1, arg2, arg3, arg4) end

---@public
---@return WorldSoundManager.WorldSound
function WorldSoundManager:getNew() end

---@public
---@return void
function WorldSoundManager:KillCell() end

---@public
---@return void
function WorldSoundManager:render() end
