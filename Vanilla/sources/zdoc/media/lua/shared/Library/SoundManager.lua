---@class SoundManager : zombie.SoundManager
---@field public SoundVolume float
---@field public MusicVolume float
---@field public AmbientVolume float
---@field public VehicleEngineVolume float
---@field private parameterMusicLibrary ParameterMusicLibrary
---@field private parameterMusicState ParameterMusicState
---@field private parameterMusicWakeState ParameterMusicWakeState
---@field private parameterMusicZombiesTargeting ParameterMusicZombiesTargeting
---@field private parameterMusicZombiesVisible ParameterMusicZombiesVisible
---@field private fmodParameters FMODParameterList
---@field private initialized boolean
---@field private inGameGroupBus long
---@field private musicGroupBus long
---@field private musicEmitter FMODSoundEmitter
---@field private musicCombinedEvent long
---@field private uiEmitter FMODSoundEmitter
---@field private music SoundManager.Music
---@field public ambientPieces ArrayList|Unknown
---@field private muted boolean
---@field private bankList long[]
---@field private eventDescList long[]
---@field private eventInstList long[]
---@field private pausedEventInstances long[]
---@field private pausedEventVolumes float[]
---@field private pausedEventCount int
---@field private emitters HashSet|Unknown
---@field private ambientSoundEffects ArrayList|Unknown
---@field public instance BaseSoundManager
---@field private currentMusicName String
---@field private currentMusicLibrary String
---@field private musicEventCallback FMOD_STUDIO_EVENT_CALLBACK
SoundManager = {}

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 float
---@return Audio
function SoundManager:PlaySoundEvenSilent(arg0, arg1, arg2) end

---@public
---@return String
function SoundManager:getCurrentMusicLibrary() end

---@public
---@param arg0 String
---@return long
function SoundManager:playUISound(arg0) end

---Specified by:
---
---getMusicPosition in class BaseSoundManager
---@public
---@return float
function SoundManager:getMusicPosition() end

---@public
---@return void
function SoundManager:debugScriptSounds() end

---Specified by:
---
---FadeOutMusic in class BaseSoundManager
---@public
---@param name String
---@param milli int
---@return void
function SoundManager:FadeOutMusic(name, milli) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 IsoGridSquare
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@return Audio
function SoundManager:PlayWorldSoundWavImpl(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@return void
function SoundManager:updateMusic() end

---@public
---@param arg0 Audio
---@param arg1 float
---@return void
---@overload fun(arg0:Audio, arg1:float, arg2:float)
function SoundManager:BlendVolume(arg0, arg1) end

---@public
---@param arg0 Audio
---@param arg1 float
---@param arg2 float
---@return void
function SoundManager:BlendVolume(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 float
---@return Audio
---@overload fun(arg0:String, arg1:int, arg2:boolean, arg3:float)
---@overload fun(arg0:String, arg1:boolean, arg2:float, arg3:float)
function SoundManager:PlaySoundWav(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 boolean
---@param arg3 float
---@return Audio
function SoundManager:PlaySoundWav(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 float
---@param arg3 float
---@return Audio
function SoundManager:PlaySoundWav(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 long
---@param arg1 GameSoundClip
---@param arg2 BitSet
---@return void
function SoundManager:stopEvent(arg0, arg1, arg2) end

---@public
---@return boolean
function SoundManager:isRemastered() end

---@public
---@return void
function SoundManager:resumeSoundAndMusic() end

---@protected
---@param arg0 Audio
---@return boolean
function SoundManager:HasMusic(arg0) end

---Specified by:
---
---getSoundVolume in class BaseSoundManager
---@public
---@return float
function SoundManager:getSoundVolume() end

---Specified by:
---
---DoMusic in class BaseSoundManager
---@public
---@param name String
---@param bLoop boolean
---@return void
function SoundManager:DoMusic(name, bLoop) end

---Specified by:
---
---isPlayingMusic in class BaseSoundManager
---@public
---@return boolean
function SoundManager:isPlayingMusic() end

---@public
---@return void
function SoundManager:pauseSoundAndMusic() end

---@public
---@param arg0 long
---@return void
function SoundManager:stopUISound(arg0) end

---@public
---@param arg0 String
---@return void
function SoundManager:playNightAmbient(arg0) end

---@public
---@param arg0 IsoPlayer
---@param arg1 String
---@return void
function SoundManager:setMusicWakeState(arg0, arg1) end

---Specified by:
---
---playMusicNonTriggered in class BaseSoundManager
---@public
---@param name String
---@param gain float
---@return void
function SoundManager:playMusicNonTriggered(name, gain) end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 boolean
---@return Audio
---@overload fun(name:String, source:IsoGridSquare, pitchVar:float, radius:float, maxGain:float, choices:int, ignoreOutside:boolean)
---@overload fun(arg0:String, arg1:boolean, arg2:IsoGridSquare, arg3:float, arg4:float, arg5:float, arg6:boolean)
function SoundManager:PlayWorldSoundWav(arg0, arg1, arg2, arg3, arg4, arg5) end

---Specified by:
---
---PlayWorldSoundWav in class BaseSoundManager
---@public
---@param name String
---@param source IsoGridSquare
---@param pitchVar float
---@param radius float
---@param maxGain float
---@param choices int
---@param ignoreOutside boolean
---@return void
function SoundManager:PlayWorldSoundWav(name, source, pitchVar, radius, maxGain, choices, ignoreOutside) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 IsoGridSquare
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@return Audio
function SoundManager:PlayWorldSoundWav(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 long
---@param arg1 GameSoundClip
---@param arg2 BitSet
---@return void
function SoundManager:startEvent(arg0, arg1, arg2) end

---Specified by:
---
---IsMusicPlaying in class BaseSoundManager
---@public
---@return boolean
function SoundManager:IsMusicPlaying() end

---Specified by:
---
---update4 in class BaseSoundManager
---@public
---@return void
function SoundManager:update4() end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 boolean
---@return Audio
---@overload fun(arg0:String, arg1:IsoGridSquare, arg2:float, arg3:float, arg4:float, arg5:int, arg6:boolean)
---@overload fun(arg0:String, arg1:boolean, arg2:IsoGridSquare, arg3:float, arg4:float, arg5:float, arg6:boolean)
function SoundManager:PlayWorldSound(arg0, arg1, arg2, arg3, arg4, arg5) end

---@public
---@param arg0 String
---@param arg1 IsoGridSquare
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@param arg5 int
---@param arg6 boolean
---@return Audio
function SoundManager:PlayWorldSound(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 IsoGridSquare
---@param arg3 float
---@param arg4 float
---@param arg5 float
---@param arg6 boolean
---@return Audio
function SoundManager:PlayWorldSound(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@private
---@return void
function SoundManager:gatherInGameEventInstances() end

---Specified by:
---
---Purge in class BaseSoundManager
---@public
---@return void
function SoundManager:Purge() end

---@private
---@param arg0 Item
---@param arg1 String
---@return void
function SoundManager:debugScriptSound(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return boolean
function SoundManager:isListenerInRange(arg0, arg1, arg2) end

---@public
---@param arg0 Audio
---@param arg1 float
---@param arg2 String
---@return Audio
function SoundManager:Start(arg0, arg1, arg2) end

---Specified by:
---
---update3 in class BaseSoundManager
---@public
---@return void
function SoundManager:update3() end

---Specified by:
---
---playMusic in class BaseSoundManager
---@public
---@param name String
---@return void
function SoundManager:playMusic(name) end

---Specified by:
---
---update1 in class BaseSoundManager
---@public
---@return void
function SoundManager:update1() end

---@public
---@return void
function SoundManager:stop() end

---@public
---@param arg0 float
---@return void
function SoundManager:setVehicleEngineVolume(arg0) end

---@public
---@param arg0 Audio
---@param arg1 float
---@param arg2 String
---@return Audio
function SoundManager:BlendThenStart(arg0, arg1, arg2) end

---@public
---@param arg0 BaseSoundEmitter
---@return void
function SoundManager:unregisterEmitter(arg0) end

---@public
---@param arg0 String
---@return boolean
---@overload fun(arg0:long)
function SoundManager:isPlayingUISound(arg0) end

---@public
---@param arg0 long
---@return boolean
function SoundManager:isPlayingUISound(arg0) end

---Specified by:
---
---CheckDoMusic in class BaseSoundManager
---@public
---@return void
function SoundManager:CheckDoMusic() end

---@public
---@param arg0 String
---@return void
function SoundManager:setMusicState(arg0) end

---Specified by:
---
---CacheSound in class BaseSoundManager
---@public
---@param file String
---@return void
function SoundManager:CacheSound(file) end

---@public
---@return FMODParameterList
function SoundManager:getFMODParameters() end

---Specified by:
---
---update2 in class BaseSoundManager
---@public
---@return void
function SoundManager:update2() end

---Specified by:
---
---setMusicVolume in class BaseSoundManager
---@public
---@param volume float
---@return void
function SoundManager:setMusicVolume(volume) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 float
---@return Audio
---@overload fun(arg0:String, arg1:boolean, arg2:float, arg3:float)
function SoundManager:PlaySound(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 float
---@param arg3 float
---@return Audio
function SoundManager:PlaySound(arg0, arg1, arg2, arg3) end

---Specified by:
---
---stopMusic in class BaseSoundManager
---@public
---@param name String
---@return void
function SoundManager:stopMusic(name) end

---@public
---@param arg0 String
---@return Audio
function SoundManager:PrepareMusic(arg0) end

---@public
---@return float
function SoundManager:getMusicVolume() end

---@public
---@param arg0 String
---@param arg1 Audio
---@param arg2 float
---@param arg3 boolean
---@return void
---@overload fun(arg0:String, arg1:Audio, arg2:boolean, arg3:float)
function SoundManager:PlayAsMusic(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@param arg1 Audio
---@param arg2 boolean
---@param arg3 float
---@return void
function SoundManager:PlayAsMusic(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 Audio
---@return void
function SoundManager:StopSound(arg0) end

---Specified by:
---
---StopMusic in class BaseSoundManager
---@public
---@return void
function SoundManager:StopMusic() end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 int
---@param arg3 int
---@param arg4 int
---@param arg5 float
---@param arg6 float
---@param arg7 float
---@param arg8 boolean
---@return Audio
function SoundManager:PlayWorldSoundImpl(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8) end

---Specified by:
---
---playAmbient in class BaseSoundManager
---@public
---@param name String
---@return void
function SoundManager:playAmbient(name) end

---@public
---@return float
function SoundManager:getVehicleEngineVolume() end

---@public
---@return ArrayList|Unknown
function SoundManager:getAmbientPieces() end

---Specified by:
---
---setSoundVolume in class BaseSoundManager
---@public
---@param volume float
---@return void
function SoundManager:setSoundVolume(volume) end

---@public
---@return String
function SoundManager:getCurrentMusicName() end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 float
---@return Audio
function SoundManager:PlayJukeboxSound(arg0, arg1, arg2) end

---@public
---@param arg0 long
---@param arg1 GameSoundClip
---@return void
function SoundManager:updateEvent(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 boolean
---@param arg3 float
---@return Audio
function SoundManager:PlayMusic(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@return void
function SoundManager:setAmbientVolume(arg0) end

---@public
---@param arg0 BaseSoundEmitter
---@return void
function SoundManager:registerEmitter(arg0) end

---Specified by:
---
---Update in class BaseSoundManager
---@public
---@return void
function SoundManager:Update() end

---@public
---@return float
function SoundManager:getAmbientVolume() end

---Specified by:
---
---update3D in class BaseSoundManager
---@public
---@return void
function SoundManager:update3D() end
