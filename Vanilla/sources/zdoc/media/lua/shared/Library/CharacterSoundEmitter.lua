---@class CharacterSoundEmitter : zombie.characters.CharacterSoundEmitter
---@field currentPriority float
---@field vocals FMODSoundEmitter
---@field footsteps FMODSoundEmitter
---@field extra FMODSoundEmitter
---@field private footstep1 long
---@field private footstep2 long
CharacterSoundEmitter = {}

---@public
---@param arg0 String
---@return boolean
---@overload fun(arg0:long)
function CharacterSoundEmitter:isPlaying(arg0) end

---@public
---@param arg0 long
---@return boolean
function CharacterSoundEmitter:isPlaying(arg0) end

---@public
---@param arg0 long
---@return void
function CharacterSoundEmitter:triggerCue(arg0) end

---@public
---@param arg0 String
---@return long
---@overload fun(arg0:String, arg1:IsoObject)
---@overload fun(arg0:String, arg1:boolean)
function CharacterSoundEmitter:playSound(arg0) end

---@public
---@param arg0 String
---@param arg1 IsoObject
---@return long
function CharacterSoundEmitter:playSound(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return long
function CharacterSoundEmitter:playSound(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function CharacterSoundEmitter:setPos(arg0, arg1, arg2) end

---@public
---@return boolean
function CharacterSoundEmitter:isEmpty() end

---@public
---@return void
function CharacterSoundEmitter:unregister() end

---@public
---@param arg0 String
---@param arg1 IsoObject
---@return long
function CharacterSoundEmitter:playSoundImpl(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 FMOD_STUDIO_PARAMETER_DESCRIPTION
---@param arg2 float
---@return void
function CharacterSoundEmitter:setParameterValue(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return void
function CharacterSoundEmitter:stopOrTriggerSoundByName(arg0) end

---@public
---@param arg0 long
---@return void
function CharacterSoundEmitter:stopOrTriggerSound(arg0) end

---@public
---@param arg0 String
---@param arg1 float
---@return void
function CharacterSoundEmitter:playFootsteps(arg0, arg1) end

---@public
---@return boolean
function CharacterSoundEmitter:hasSoundsToStart() end

---@return CharacterSoundEmitter.footstep
function CharacterSoundEmitter:getFootstepToPlay() end

---@public
---@param arg0 long
---@return boolean
function CharacterSoundEmitter:hasSustainPoints(arg0) end

---@public
---@return void
function CharacterSoundEmitter:stopAll() end

---@public
---@param arg0 long
---@param arg1 float
---@return void
function CharacterSoundEmitter:setVolume(arg0, arg1) end

---@public
---@return boolean
function CharacterSoundEmitter:isClear() end

---@public
---@param arg0 long
---@param arg1 float
---@return void
function CharacterSoundEmitter:setPitch(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function CharacterSoundEmitter:set(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return int
function CharacterSoundEmitter:stopSoundByName(arg0) end

---@public
---@return void
function CharacterSoundEmitter:register() end

---@public
---@param arg0 String
---@return long
function CharacterSoundEmitter:playVocals(arg0) end

---@public
---@param arg0 long
---@return int
function CharacterSoundEmitter:stopSound(arg0) end

---@public
---@return void
function CharacterSoundEmitter:tick() end
