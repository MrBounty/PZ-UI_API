---@class FMODSoundEmitter : fmod.fmod.FMODSoundEmitter
---@field private ToStart ArrayList|Unknown
---@field private Instances ArrayList|Unknown
---@field public x float
---@field public y float
---@field public z float
---@field public emitterType EmitterType
---@field public parent IsoObject
---@field private occlusion ParameterOcclusion
---@field private parameters ArrayList|Unknown
---@field public parameterUpdater IFMODParameterUpdater
---@field private parameterValues ArrayList|Unknown
---@field private parameterValuePool ObjectPool|Unknown
---@field private parameterSet BitSet
---@field private eventSoundPool ArrayDeque|Unknown
---@field private fileSoundPool ArrayDeque|Unknown
---@field private CurrentTimeMS long
FMODSoundEmitter = {}

---@private
---@param arg0 long
---@param arg1 FMOD_STUDIO_PARAMETER_DESCRIPTION
---@return int
function FMODSoundEmitter:findParameterValue(arg0, arg1) end

---@private
---@param arg0 long
---@return int
---@overload fun(arg0:String)
function FMODSoundEmitter:findToStart(arg0) end

---@private
---@param arg0 String
---@return int
function FMODSoundEmitter:findToStart(arg0) end

---@public
---@return void
function FMODSoundEmitter:update() end

---@public
---@param arg0 String
---@return long
function FMODSoundEmitter:playSoundLoopedImpl(arg0) end

---@public
---@param arg0 long
---@return boolean
function FMODSoundEmitter:hasSustainPoints(arg0) end

---@public
---@return boolean
function FMODSoundEmitter:hasSoundsToStart() end

---@public
---@param arg0 long
---@return void
function FMODSoundEmitter:triggerCue(arg0) end

---@public
---@param arg0 long
---@return boolean
function FMODSoundEmitter:restart(arg0) end

---@public
---@param arg0 long
---@param arg1 float
---@return void
function FMODSoundEmitter:setVolume(arg0, arg1) end

---@public
---@return void
function FMODSoundEmitter:randomStart() end

---@public
---@param arg0 String
---@return void
function FMODSoundEmitter:stopOrTriggerSoundByName(arg0) end

---@public
---@param arg0 String
---@return long
---@overload fun(arg0:String, arg1:IsoGridSquare)
---@overload fun(arg0:String, arg1:IsoObject)
---@overload fun(arg0:String, arg1:boolean)
---@overload fun(arg0:String, arg1:int, arg2:int, arg3:int)
function FMODSoundEmitter:playSound(arg0) end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@return long
function FMODSoundEmitter:playSound(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 IsoObject
---@return long
function FMODSoundEmitter:playSound(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return long
function FMODSoundEmitter:playSound(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@param arg3 int
---@return long
function FMODSoundEmitter:playSound(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function FMODSoundEmitter:setPos(arg0, arg1, arg2) end

---@public
---@param arg0 long
---@param arg1 float
---@return void
function FMODSoundEmitter:setPitch(arg0, arg1) end

---@public
---@param arg0 long
---@param arg1 boolean
---@return void
function FMODSoundEmitter:set3D(arg0, arg1) end

---@public
---@param arg0 long
---@return int
function FMODSoundEmitter:stopSound(arg0) end

---@private
---@param arg0 GameSoundClip
---@param arg1 float
---@param arg2 IsoObject
---@return FMODSoundEmitter.Sound
function FMODSoundEmitter:addSound(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@return int
---@overload fun(arg0:long)
function FMODSoundEmitter:findInstance(arg0) end

---@private
---@param arg0 long
---@return int
function FMODSoundEmitter:findInstance(arg0) end

---@public
---@param arg0 FMODParameter
---@return void
function FMODSoundEmitter:addParameter(arg0) end

---@public
---@param arg0 String
---@return long
function FMODSoundEmitter:playAmbientSound(arg0) end

---@private
---@param arg0 long
---@param arg1 GameSoundClip
---@return void
function FMODSoundEmitter:stopEvent(arg0, arg1) end

---@public
---@return void
function FMODSoundEmitter:tick() end

---@public
---@return boolean
function FMODSoundEmitter:isEmpty() end

---@private
---@param arg0 String
---@param arg1 boolean
---@return void
function FMODSoundEmitter:sendStopSound(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@return long
---@overload fun(arg0:String, arg1:IsoObject)
---@overload fun(arg0:String, arg1:boolean, arg2:IsoObject)
function FMODSoundEmitter:playSoundImpl(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 IsoObject
---@return long
function FMODSoundEmitter:playSoundImpl(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 IsoObject
---@return long
function FMODSoundEmitter:playSoundImpl(arg0, arg1, arg2) end

---@private
---@return FMODSoundEmitter.EventSound
function FMODSoundEmitter:allocEventSound() end

---@public
---@param arg0 String
---@return int
function FMODSoundEmitter:stopSoundByName(arg0) end

---@public
---@param arg0 GameSoundClip
---@param arg1 IsoObject
---@return long
function FMODSoundEmitter:playClip(arg0, arg1) end

---@private
---@param arg0 long
---@param arg1 GameSoundClip
---@return void
function FMODSoundEmitter:startEvent(arg0, arg1) end

---@private
---@return FMODSoundEmitter.FileSound
function FMODSoundEmitter:allocFileSound() end

---@private
---@param arg0 long
---@param arg1 GameSoundClip
---@return void
function FMODSoundEmitter:updateEvent(arg0, arg1) end

---@public
---@param arg0 String
---@return long
function FMODSoundEmitter:playAmbientLoopedImpl(arg0) end

---@public
---@return void
function FMODSoundEmitter:stopAll() end

---@public
---@param arg0 long
---@return boolean
---@overload fun(arg0:String)
function FMODSoundEmitter:isPlaying(arg0) end

---@public
---@param arg0 String
---@return boolean
function FMODSoundEmitter:isPlaying(arg0) end

---@public
---@param arg0 float
---@return void
function FMODSoundEmitter:setVolumeAll(arg0) end

---@public
---@param arg0 long
---@return void
function FMODSoundEmitter:stopOrTriggerSound(arg0) end

---@public
---@param arg0 long
---@param arg1 String
---@return void
function FMODSoundEmitter:setTimelinePosition(arg0, arg1) end

---@public
---@param arg0 String
---@return long
function FMODSoundEmitter:playSoundLooped(arg0) end

---@public
---@param arg0 long
---@param arg1 FMOD_STUDIO_PARAMETER_DESCRIPTION
---@param arg2 float
---@return void
function FMODSoundEmitter:setParameterValue(arg0, arg1, arg2) end

---@private
---@param arg0 FMODSoundEmitter.FileSound
---@return void
function FMODSoundEmitter:releaseFileSound(arg0) end

---@public
---@return void
function FMODSoundEmitter:clearParameters() end

---@private
---@param arg0 FMODSoundEmitter.EventSound
---@return void
function FMODSoundEmitter:releaseEventSound(arg0) end
