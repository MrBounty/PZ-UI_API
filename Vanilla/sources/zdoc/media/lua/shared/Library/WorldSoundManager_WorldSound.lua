---@class WorldSoundManager.WorldSound : zombie.WorldSoundManager.WorldSound
---@field public source Object
---@field public life int
---@field public radius int
---@field public stresshumans boolean
---@field public volume int
---@field public x int
---@field public y int
---@field public z int
---@field public zombieIgnoreDist float
---@field public sourceIsZombie boolean
---@field public stressMod float
---@field public bRepeating boolean
WorldSoundManager_WorldSound = {}

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
---@overload fun(sourceIsZombie:boolean, x:int, y:int, z:int, radius:int, volume:int, stressHumans:boolean, zombieIgnoreDist:float, stressMod:float)
function WorldSoundManager_WorldSound:init(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 Object
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 int
---@param arg6 boolean
---@return WorldSoundManager.WorldSound
function WorldSoundManager_WorldSound:init(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

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
function WorldSoundManager_WorldSound:init(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---@public
---@param sourceIsZombie boolean
---@param x int
---@param y int
---@param z int
---@param radius int
---@param volume int
---@param stressHumans boolean
---@param zombieIgnoreDist float
---@param stressMod float
---@return WorldSoundManager.WorldSound
function WorldSoundManager_WorldSound:init(sourceIsZombie, x, y, z, radius, volume, stressHumans, zombieIgnoreDist, stressMod) end
