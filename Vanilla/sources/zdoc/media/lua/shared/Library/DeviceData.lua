---@class DeviceData : zombie.radio.devices.DeviceData
---@field private deviceSpeakerSoundMod float
---@field private deviceButtonSoundVol float
---@field protected deviceName String
---@field protected twoWay boolean
---@field protected transmitRange int
---@field protected micRange int
---@field protected micIsMuted boolean
---@field protected baseVolumeRange float
---@field protected deviceVolume float
---@field protected isPortable boolean
---@field protected isTelevision boolean
---@field protected isHighTier boolean
---@field protected isTurnedOn boolean
---@field protected channel int
---@field protected minChannelRange int
---@field protected maxChannelRange int
---@field protected presets DevicePresets
---@field protected isBatteryPowered boolean
---@field protected hasBattery boolean
---@field protected powerDelta float
---@field protected useDelta float
---@field protected lastRecordedDistance int
---@field protected headphoneType int
---@field protected parent WaveSignalDevice
---@field protected gameTime GameTime
---@field protected channelChangedRecently boolean
---@field protected emitter BaseSoundEmitter
---@field protected soundIDs ArrayList|Unknown
---@field protected mediaIndex short
---@field protected mediaType byte
---@field protected mediaItem String
---@field protected playingMedia MediaData
---@field protected isPlayingMedia boolean
---@field protected mediaLineIndex int
---@field protected lineCounter float
---@field protected currentMediaLine String
---@field protected currentMediaColor Color
---@field protected isStoppingMedia boolean
---@field protected stopMediaCounter float
---@field protected noTransmit boolean
---@field private soundCounterStatic float
---@field protected radioLoopSound long
---@field protected doTriggerWorldSound boolean
---@field protected lastMinuteStamp long
---@field protected listenCnt int
---@field nextStaticSound float
---@field protected signalCounter float
---@field protected soundCounter float
---@field minmod float
---@field maxmod float
DeviceData = {}

---@public
---@return boolean
function DeviceData:getHasBattery() end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@return void
function DeviceData:update(arg0, arg1) end

---@public
---@return boolean
function DeviceData:isIsoDevice() end

---@public
---@return DevicePresets
function DeviceData:getDevicePresets() end

---@public
---@return boolean
function DeviceData:getIsTelevision() end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setIsPortable(arg0) end

---@public
---@return int
function DeviceData:getLastRecordedDistance() end

---@public
---@return void
function DeviceData:setRandomChannel() end

---@public
---@param arg0 int
---@return void
function DeviceData:setHeadphoneType(arg0) end

---@public
---@return int
function DeviceData:getChannel() end

---@public
---@return boolean
function DeviceData:isVehicleDevice() end

---@public
---@return boolean
function DeviceData:getIsHighTier() end

---@public
---@param arg0 DrainableComboItem
---@return void
function DeviceData:addBattery(arg0) end

---@public
---@param arg0 float
---@return void
function DeviceData:setDeviceVolumeRaw(arg0) end

---@public
---@param arg0 String
---@return void
function DeviceData:setDeviceName(arg0) end

---@private
---@return void
function DeviceData:televisionMediaSwitch() end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setNoTransmit(arg0) end

---@public
---@param arg0 int
---@return void
function DeviceData:doReceiveSignal(arg0) end

---@public
---@return void
function DeviceData:transmitPresets() end

---@protected
---@return Object
function DeviceData:clone() end

---@public
---@return DeviceData
function DeviceData:getClone() end

---@public
---@return int
function DeviceData:getMicRange() end

---@private
---@return void
function DeviceData:setNextStaticSound() end

---@public
---@param arg0 ByteBuffer
---@param arg1 UdpConnection
---@return void
function DeviceData:receiveDeviceDataStatePacket(arg0, arg1) end

---@public
---@return int
function DeviceData:getTransmitRange() end

---@public
---@return BaseSoundEmitter
function DeviceData:getEmitter() end

---@public
---@param arg0 int
---@return void
---@overload fun(arg0:int, arg1:boolean)
function DeviceData:setChannel(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function DeviceData:setChannel(arg0, arg1) end

---@public
---@return boolean
function DeviceData:hasMedia() end

---@public
---@param arg0 float
---@return void
function DeviceData:setPower(arg0) end

---@public
---@return boolean
function DeviceData:isInventoryDevice() end

---@private
---@param arg0 UdpConnection
---@param arg1 short
---@return void
function DeviceData:sendDeviceDataStatePacket(arg0, arg1) end

---@public
---@return float
function DeviceData:getPower() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function DeviceData:load(arg0, arg1, arg2) end

---@public
---@return int
function DeviceData:getMaxChannelRange() end

---@public
---@return MediaData
function DeviceData:getMediaData() end

---@public
---@return void
function DeviceData:StartPlayMedia() end

---@public
---@param arg0 short
---@return void
function DeviceData:setMediaIndex(arg0) end

---@public
---@param arg0 ItemContainer
---@return InventoryItem
function DeviceData:getHeadphones(arg0) end

---@public
---@return float
function DeviceData:getBaseVolumeRange() end

---@public
---@return boolean
function DeviceData:isReceivingSignal() end

---@public
---@return short
function DeviceData:getMediaIndex() end

---@private
---@return void
function DeviceData:updateStaticSounds() end

---@public
---@param arg0 InventoryItem
---@return void
function DeviceData:addHeadphones(arg0) end

---@private
---@return void
function DeviceData:postPlayingMedia() end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setIsBatteryPowered(arg0) end

---@public
---@param arg0 float
---@return void
function DeviceData:setBaseVolumeRange(arg0) end

---@public
---@return int
function DeviceData:getDeviceVolumeRange() end

---@public
---@return boolean
function DeviceData:getIsPortable() end

---@public
---@return boolean
function DeviceData:canBePoweredHere() end

---@public
---@return boolean
function DeviceData:getIsBatteryPowered() end

---@public
---@param arg0 ItemContainer
---@return InventoryItem
function DeviceData:getBattery(arg0) end

---@public
---@param arg0 ItemContainer
---@return InventoryItem
function DeviceData:removeMediaItem(arg0) end

---@public
---@param arg0 int
---@return void
function DeviceData:setTransmitRange(arg0) end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setIsTelevision(arg0) end

---@protected
---@return void
function DeviceData:setEmitterAndPos() end

---@public
---@return int
function DeviceData:getDeviceSoundVolumeRange() end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setMicIsMuted(arg0) end

---@private
---@param arg0 short
---@return void
function DeviceData:transmitDeviceDataState(arg0) end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setIsHighTier(arg0) end

---@public
---@param arg0 float
---@return void
function DeviceData:setUseDelta(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function DeviceData:playSoundLocal(arg0, arg1) end

---@public
---@return String
function DeviceData:getDeviceName() end

---@public
---@return boolean
function DeviceData:isNoTransmit() end

---@public
---@param arg0 String
---@param arg1 float
---@param arg2 boolean
---@return void
function DeviceData:playSound(arg0, arg1, arg2) end

---@public
---@param arg0 InventoryItem
---@return void
function DeviceData:addMediaItem(arg0) end

---@public
---@return void
function DeviceData:StopPlayMedia() end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setTurnedOnRaw(arg0) end

---@public
---@return int
function DeviceData:getMinChannelRange() end

---@public
---@return boolean
function DeviceData:getIsTwoWay() end

---@public
---@return void
function DeviceData:generatePresets() end

---@public
---@return float
function DeviceData:getDeviceVolume() end

---@public
---@param arg0 int
---@return void
function DeviceData:setMicRange(arg0) end

---@public
---@param arg0 int
---@return void
function DeviceData:setChannelRaw(arg0) end

---@public
---@param arg0 int
---@return void
function DeviceData:setMaxChannelRange(arg0) end

---@public
---@param arg0 byte
---@return void
function DeviceData:setMediaType(arg0) end

---@public
---@param arg0 int
---@return void
function DeviceData:setMinChannelRange(arg0) end

---@public
---@param arg0 DevicePresets
---@return void
function DeviceData:setDevicePresets(arg0) end

---@public
---@return boolean
function DeviceData:getMicIsMuted() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function DeviceData:save(arg0, arg1) end

---@protected
---@return void
function DeviceData:updateEmitter() end

---@public
---@return boolean
function DeviceData:isPlayingMedia() end

---@private
---@return void
function DeviceData:prePlayingMedia() end

---@public
---@return float
function DeviceData:getUseDelta() end

---@public
---@return int
function DeviceData:getHeadphoneType() end

---@public
---@param arg0 float
---@return void
function DeviceData:setDeviceVolume(arg0) end

---@public
---@param arg0 WaveSignalDevice
---@return void
function DeviceData:setParent(arg0) end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setIsTwoWay(arg0) end

---@public
---@return boolean
function DeviceData:getIsTurnedOn() end

---@public
---@param arg0 boolean
---@return void
function DeviceData:TriggerPlayerListening(arg0) end

---@public
---@return void
function DeviceData:updateSimple() end

---@public
---@return void
function DeviceData:cleanSoundsAndEmitter() end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setIsTurnedOn(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return void
function DeviceData:playSoundSend(arg0, arg1) end

---@public
---@return void
function DeviceData:transmitBattryChange() end

---@public
---@return byte
function DeviceData:getMediaType() end

---@public
---@return WaveSignalDevice
function DeviceData:getParent() end

---@public
---@return void
function DeviceData:updateMediaPlaying() end

---@private
---@param arg0 short
---@param arg1 UdpConnection
---@return void
function DeviceData:transmitDeviceDataStateServer(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function DeviceData:setHasBattery(arg0) end
