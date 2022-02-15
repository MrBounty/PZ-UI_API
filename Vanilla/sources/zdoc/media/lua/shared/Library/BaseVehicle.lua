---@class BaseVehicle : zombie.vehicles.BaseVehicle
---@field private lastCrashTime long
---@field public RADIUS float
---@field public FADE_DISTANCE int
---@field public RANDOMIZE_CONTAINER_CHANCE int
---@field public authorizationOnServer byte
---@field public authorizationSimulation byte
---@field public authorizationServerSimulation byte
---@field public authorizationOwner byte
---@field public authorizationServerOwner byte
---@field private _UNIT_Y Vector3f
---@field private tempPoly PolygonalMap2.VehiclePoly
---@field public YURI_FORCE_FIELD boolean
---@field public RENDER_TO_TEXTURE boolean
---@field public CENTER_OF_MASS_MAGIC float
---@field private wheelParams float[]
---@field private physicsParams float[]
---@field POSITION_ORIENTATION_PACKET_SIZE byte
---@field public vehicleShadow Texture
---@field public justBreakConstraintTimer int
---@field public wasTowedBy BaseVehicle
---@field protected inf ColorInfo
---@field private lowRiderParam float[]
---@field private impulseFromServer BaseVehicle.VehicleImpulse
---@field private impulseFromSquishedZombie BaseVehicle.VehicleImpulse[]
---@field private impulseFromHitZombie ArrayList|Unknown
---@field private netPlayerTimeoutMax int
---@field private tempVector4f Vector4f
---@field public models ArrayList|Unknown
---@field public chunk IsoChunk
---@field public polyDirty boolean
---@field private polyGarageCheck boolean
---@field private radiusReductionInGarage float
---@field public VehicleID short
---@field public sqlID int
---@field public serverRemovedFromWorld boolean
---@field public trace boolean
---@field public interpolation VehicleInterpolation
---@field public waitFullUpdate boolean
---@field public throttle float
---@field public engineSpeed double
---@field public transmissionNumber TransmissionNumber
---@field public transmissionChangeTime UpdateLimit
---@field public hasExtendOffset boolean
---@field public hasExtendOffsetExiting boolean
---@field public savedPhysicsZ float
---@field public savedRot Quaternionf
---@field public jniTransform Transform
---@field public jniSpeed float
---@field public jniIsCollide boolean
---@field public jniLinearVelocity Vector3f
---@field public netLinearVelocity Vector3f
---@field public netPlayerAuthorization byte
---@field public netPlayerId short
---@field public netPlayerTimeout int
---@field public authSimulationHash int
---@field public authSimulationTime long
---@field public frontEndDurability int
---@field public rearEndDurability int
---@field public rust float
---@field public colorHue float
---@field public colorSaturation float
---@field public colorValue float
---@field public currentFrontEndDurability int
---@field public currentRearEndDurability int
---@field public collideX float
---@field public collideY float
---@field public shadowCoord PolygonalMap2.VehiclePoly
---@field public engineState BaseVehicle.engineStateTypes
---@field public engineLastUpdateStateTime long
---@field public MAX_WHEELS int
---@field public PHYSICS_PARAM_COUNT int
---@field public wheelInfo BaseVehicle.WheelInfo[]
---@field public skidding boolean
---@field public skidSound long
---@field public ramSound long
---@field public ramSoundTime long
---@field private vehicleEngineRPM VehicleEngineRPM
---@field public new_EngineSoundId long[]
---@field private combinedEngineSound long
---@field public engineSoundIndex int
---@field public hornemitter BaseSoundEmitter
---@field public startTime float
---@field public headlightsOn boolean
---@field public stoplightsOn boolean
---@field public windowLightsOn boolean
---@field public soundHornOn boolean
---@field public soundBackMoveOn boolean
---@field public lightbarLightsMode LightbarLightsMode
---@field public lightbarSirenMode LightbarSirenMode
---@field private leftLight1 IsoLightSource
---@field private leftLight2 IsoLightSource
---@field private rightLight1 IsoLightSource
---@field private rightLight2 IsoLightSource
---@field private leftLightIndex int
---@field private rightLightIndex int
---@field public connectionState BaseVehicle.ServerVehicleState[]
---@field protected passengers BaseVehicle.Passenger[]
---@field protected scriptName String
---@field protected script VehicleScript
---@field protected parts ArrayList|Unknown
---@field protected battery VehiclePart
---@field protected engineQuality int
---@field protected engineLoudness int
---@field protected enginePower int
---@field protected engineCheckTime long
---@field protected lights ArrayList|Unknown
---@field protected createdModel boolean
---@field protected lastLinearVelocity Vector3f
---@field protected skinIndex int
---@field protected physics CarController
---@field protected bCreated boolean
---@field protected poly PolygonalMap2.VehiclePoly
---@field protected polyPlusRadius PolygonalMap2.VehiclePoly
---@field protected bDoDamageOverlay boolean
---@field protected loaded boolean
---@field protected updateFlags short
---@field protected updateLockTimeout long
---@field limitPhysicSend UpdateLimit
---@field limitPhysicValid UpdateLimit
---@field public addedToWorld boolean
---@field removedFromWorld boolean
---@field private polyPlusRadiusMinX float
---@field private polyPlusRadiusMinY float
---@field private polyPlusRadiusMaxX float
---@field private polyPlusRadiusMaxY float
---@field private maxSpeed float
---@field private keyIsOnDoor boolean
---@field private hotwired boolean
---@field private hotwiredBroken boolean
---@field private keysInIgnition boolean
---@field private soundHorn long
---@field private soundScrapePastPlant long
---@field private soundBackMoveSignal long
---@field public soundSirenSignal long
---@field private choosenParts HashMap|Unknown|Unknown
---@field private type String
---@field private respawnZone String
---@field private mass float
---@field private initialMass float
---@field private brakingForce float
---@field private baseQuality float
---@field private currentSteering float
---@field private isBraking boolean
---@field private mechanicalID int
---@field private needPartsUpdate boolean
---@field private alarmed boolean
---@field private alarmTime int
---@field private alarmAccumulator float
---@field private sirenStartTime double
---@field private mechanicUIOpen boolean
---@field private isGoodCar boolean
---@field private currentKey InventoryItem
---@field private doColor boolean
---@field private brekingSlowFactor float
---@field private brekingObjectsList ArrayList|Unknown
---@field private limitUpdate UpdateLimit
---@field public keySpawned byte
---@field public vehicleTransform Matrix4f
---@field public renderTransform Matrix4f
---@field private tempMatrix4fLWJGL_1 Matrix4f
---@field private tempQuat4f Quaternionf
---@field private tempTransform Transform
---@field private tempTransform2 Transform
---@field private tempTransform3 Transform
---@field private emitter BaseSoundEmitter
---@field private brakeBetweenUpdatesSpeed float
---@field public physicActiveCheck long
---@field private constraintChangedTime long
---@field private m_animPlayer AnimationPlayer
---@field public specificDistributionId String
---@field private bAddThumpWorldSound boolean
---@field private m_surroundVehicle SurroundVehicle
---@field private regulator boolean
---@field private regulatorSpeed float
---@field private s_PartToMaskMap HashMap|Unknown|Unknown
---@field private BYTE_ZERO Byte
---@field private bloodIntensity HashMap|Unknown|Unknown
---@field private OptionBloodDecals boolean
---@field private createPhysicsTime long
---@field private vehicleTowing BaseVehicle
---@field private vehicleTowedBy BaseVehicle
---@field public constraintTowing int
---@field private vehicleTowingID int
---@field private vehicleTowedByID int
---@field private towAttachmentSelf String
---@field private towAttachmentOther String
---@field private towConstraintZOffset float
---@field private parameterVehicleBrake ParameterVehicleBrake
---@field private parameterVehicleEngineCondition ParameterVehicleEngineCondition
---@field private parameterVehicleGear ParameterVehicleGear
---@field private parameterVehicleLoad ParameterVehicleLoad
---@field private parameterVehicleRoadMaterial ParameterVehicleRoadMaterial
---@field private parameterVehicleRPM ParameterVehicleRPM
---@field private parameterVehicleSkid ParameterVehicleSkid
---@field private parameterVehicleSpeed ParameterVehicleSpeed
---@field private parameterVehicleSteer ParameterVehicleSteer
---@field private parameterVehicleTireMissing ParameterVehicleTireMissing
---@field private fmodParameters FMODParameterList
---@field public TL_vector2_pool ThreadLocal|Unknown
---@field public TL_vector2f_pool ThreadLocal|Unknown
---@field public TL_vector3f_pool ThreadLocal|Unknown
---@field public TL_matrix4f_pool ThreadLocal|Unknown
---@field public TL_quaternionf_pool ThreadLocal|Unknown
---@field public PHYSICS_Z_SCALE float
---@field public PLUS_RADIUS float
---@field private zombiesHits int
---@field private zombieHitTimestamp long
---@field public MASK1_FRONT int
---@field public MASK1_REAR int
---@field public MASK1_DOOR_RIGHT_FRONT int
---@field public MASK1_DOOR_RIGHT_REAR int
---@field public MASK1_DOOR_LEFT_FRONT int
---@field public MASK1_DOOR_LEFT_REAR int
---@field public MASK1_WINDOW_RIGHT_FRONT int
---@field public MASK1_WINDOW_RIGHT_REAR int
---@field public MASK1_WINDOW_LEFT_FRONT int
---@field public MASK1_WINDOW_LEFT_REAR int
---@field public MASK1_WINDOW_FRONT int
---@field public MASK1_WINDOW_REAR int
---@field public MASK1_GUARD_RIGHT_FRONT int
---@field public MASK1_GUARD_RIGHT_REAR int
---@field public MASK1_GUARD_LEFT_FRONT int
---@field public MASK1_GUARD_LEFT_REAR int
---@field public MASK2_ROOF int
---@field public MASK2_LIGHT_RIGHT_FRONT int
---@field public MASK2_LIGHT_LEFT_FRONT int
---@field public MASK2_LIGHT_RIGHT_REAR int
---@field public MASK2_LIGHT_LEFT_REAR int
---@field public MASK2_BRAKE_RIGHT int
---@field public MASK2_BRAKE_LEFT int
---@field public MASK2_LIGHTBAR_RIGHT int
---@field public MASK2_LIGHTBAR_LEFT int
---@field public MASK2_HOOD int
---@field public MASK2_BOOT int
---@field public forcedFriction float
---@field protected hitVars BaseVehicle.HitVars
BaseVehicle = {}

---@private
---@param arg0 VehiclePart
---@return boolean
function BaseVehicle:updatePart(arg0) end

---@private
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@return Boolean
---@overload fun(arg0:String, arg1:Object, arg2:Object, arg3:Object)
function BaseVehicle:callLuaBoolean(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@return Boolean
function BaseVehicle:callLuaBoolean(arg0, arg1, arg2, arg3) end

---@public
---@return FMODParameterList
function BaseVehicle:getFMODParameters() end

---@public
---@return float
function BaseVehicle:getRemainingFuelPercentage() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 ColorInfo
---@param arg4 boolean
---@param arg5 boolean
---@param arg6 Shader
---@return void
function BaseVehicle:render(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setWindowLightsOn(arg0) end

---@public
---@return Texture
function BaseVehicle:getShadowTexture() end

---@public
---@param arg0 JVector2
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@param arg4 float
---@return void
function BaseVehicle:drawDirectionLine(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 VehicleScript.Area
---@return JVector2
---@overload fun(arg0:VehicleScript.Area, arg1:JVector2)
function BaseVehicle:areaPositionWorld4PlayerInteract(arg0) end

---@public
---@param arg0 VehicleScript.Area
---@param arg1 JVector2
---@return JVector2
function BaseVehicle:areaPositionWorld4PlayerInteract(arg0, arg1) end

---@public
---@return boolean
function BaseVehicle:isBraking() end

---@public
---@return boolean
function BaseVehicle:isEngineWorking() end

---@public
---@param arg0 String
---@param arg1 Vector3f
---@return Vector3f
function BaseVehicle:getTowingLocalPos(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 IsoObject
---@return long
function BaseVehicle:playSoundImpl(arg0, arg1) end

---@public
---@return float
function BaseVehicle:getColorHue() end

---@public
---@return SurroundVehicle
function BaseVehicle:getSurroundVehicle() end

---@private
---@return void
function BaseVehicle:initShadowPoly() end

---@public
---@param arg0 String
---@return float
function BaseVehicle:getBloodIntensity(arg0) end

---@public
---@return int
function BaseVehicle:getTransmissionNumber() end

---@public
---@return void
---@overload fun(arg0:String)
function BaseVehicle:setScript() end

---@public
---@param arg0 String
---@return void
function BaseVehicle:setScript(arg0) end

---@public
---@param arg0 int
---@return boolean
function BaseVehicle:isExitBlocked2(arg0) end

---@public
---@param arg0 String
---@return void
function BaseVehicle:setVehicleType(arg0) end

---@private
---@param arg0 IsoGridSquare
---@return void
function BaseVehicle:playScrapePastPlantSound(arg0) end

---@private
---@param arg0 VehiclePart
---@param arg1 int
---@param arg2 boolean
---@return void
function BaseVehicle:checkDamage2(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setRust(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return void
function BaseVehicle:playSwitchSeatAnim(arg0, arg1) end

---@public
---@param arg0 int
---@return VehiclePart
function BaseVehicle:getPartByIndex(arg0) end

---@public
---@return void
function BaseVehicle:engineDoRunning() end

---@public
---@param arg0 int
---@return IsoGameCharacter
function BaseVehicle:getCharacter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 VehiclePart
---@return boolean
function BaseVehicle:canUninstallPart(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function BaseVehicle:setTireRemoved(arg0, arg1) end

---@public
---@param arg0 IsoGridSquare
---@return boolean
function BaseVehicle:addKeyToSquare(arg0) end

---@public
---@return int
function BaseVehicle:getSkinCount() end

---@public
---@return boolean
function BaseVehicle:isInvalidChunkBehind() end

---@public
---@return boolean
function BaseVehicle:isInvalidChunkAhead() end

---@public
---@return BaseVehicle
function BaseVehicle:getVehicleTowedBy() end

---@public
---@return void
function BaseVehicle:serverUpdateSimulatorState() end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setKeysInIgnition(arg0) end

---@public
---@param arg0 String
---@param arg1 float
---@return void
function BaseVehicle:setBloodIntensity(arg0, arg1) end

---@public
---@return boolean
function BaseVehicle:isDriveable() end

---@public
---@return float
function BaseVehicle:getMaxSpeed() end

---@private
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@return void
---@overload fun(arg0:String, arg1:Object, arg2:Object, arg3:Object)
function BaseVehicle:callLuaVoid(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@param arg1 Object
---@param arg2 Object
---@param arg3 Object
---@return void
function BaseVehicle:callLuaVoid(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setRegulatorSpeed(arg0) end

---@public
---@param arg0 VehiclePart
---@return void
function BaseVehicle:transmitPartUsedDelta(arg0) end

---@private
---@param arg0 IsoGameCharacter
---@param arg1 VehicleScript.Anim
---@param arg2 boolean
---@return void
function BaseVehicle:playCharacterAnim(arg0, arg1, arg2) end

---@public
---@return float
function BaseVehicle:getInsideTemperature() end

---@private
---@return void
function BaseVehicle:stopEngineSounds() end

---@public
---@param arg0 BaseVehicle
---@param arg1 String
---@param arg2 String
---@return boolean
function BaseVehicle:canAttachTrailer(arg0, arg1, arg2) end

---@public
---@param arg0 BaseVehicle
---@param arg1 String
---@param arg2 String
---@return void
function BaseVehicle:addHingeConstraint(arg0, arg1, arg2) end

---@public
---@return HashMap|Unknown|Unknown
function BaseVehicle:getChoosenParts() end

---@public
---@param arg0 String
---@param arg1 Vector3f
---@return Vector3f
function BaseVehicle:getAttachmentWorldPos(arg0, arg1) end

---@public
---@return boolean
function BaseVehicle:isBackSignalEmitting() end

---@public
---@return PolygonalMap2.VehiclePoly
function BaseVehicle:getPoly() end

---@public
---@param arg0 IsoGameCharacter
---@return int
function BaseVehicle:getSeat(arg0) end

---@public
---@return void
function BaseVehicle:transmitEngine() end

---@public
---@return void
function BaseVehicle:permanentlyRemove() end

---@public
---@param arg0 String
---@return void
function BaseVehicle:setZone(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function BaseVehicle:exitRSync(arg0) end

---@public
---@param arg0 int
---@param arg1 Vector3f
---@return Vector3f
function BaseVehicle:getPassengerLocalPos(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function BaseVehicle:isIntersectingSquareWithShadow(arg0, arg1, arg2) end

---@public
---@param arg0 int
---@param arg1 String
---@return void
function BaseVehicle:transmitCharacterPosition(arg0, arg1) end

---@public
---@return boolean
function BaseVehicle:isInForest() end

---@public
---@param arg0 IsoObject
---@param arg1 float
---@return void
function BaseVehicle:ApplyImpulse(arg0, arg1) end

---@public
---@return void
function BaseVehicle:updateSounds() end

---@public
---@return void
function BaseVehicle:scriptReloaded() end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setCurrentSteering(arg0) end

---@public
---@return void
function BaseVehicle:trySpawnKey() end

---@public
---@return boolean
function BaseVehicle:isAtRest() end

---@private
---@return void
function BaseVehicle:doVehicleColor() end

---@public
---@return boolean
function BaseVehicle:Serialize() end

---@public
---@param arg0 int
---@param arg1 IsoGameCharacter
---@return boolean
---@overload fun(arg0:int, arg1:IsoGameCharacter, arg2:Vector3f)
function BaseVehicle:enter(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 IsoGameCharacter
---@param arg2 Vector3f
---@return boolean
function BaseVehicle:enter(arg0, arg1, arg2) end

---@public
---@return void
function BaseVehicle:engineDoStarting() end

---@public
---@param arg0 String
---@return boolean
function BaseVehicle:attachmentExist(arg0) end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setBaseQuality(arg0) end

---@public
---@param arg0 VehicleScript.Position
---@param arg1 Vector3f
---@return Vector3f
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:Vector3f)
function BaseVehicle:getPassengerPositionWorldPos(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 Vector3f
---@return Vector3f
function BaseVehicle:getPassengerPositionWorldPos(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 String
---@param arg1 int
---@return void
function BaseVehicle:damageHeadlight(arg0, arg1) end

---@public
---@param arg0 String
---@return BaseVehicle
---@overload fun(arg0:String, arg1:boolean)
function BaseVehicle:setSmashed(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@return BaseVehicle
function BaseVehicle:setSmashed(arg0, arg1) end

---@public
---@param arg0 BaseVehicle
---@param arg1 String
---@param arg2 String
---@return void
---@overload fun(arg0:BaseVehicle, arg1:String, arg2:String, arg3:Float, arg4:Boolean)
function BaseVehicle:addPointConstraint(arg0, arg1, arg2) end

---@public
---@param arg0 BaseVehicle
---@param arg1 String
---@param arg2 String
---@param arg3 Float
---@param arg4 Boolean
---@return void
function BaseVehicle:addPointConstraint(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@return void
function BaseVehicle:WeaponHit(arg0, arg1) end

---@private
---@return String
function BaseVehicle:getEngineTurnOffSound() end

---@public
---@return void
function BaseVehicle:softReset() end

---@public
---@return void
function BaseVehicle:removeKeyFromIgnition() end

---@public
---@return boolean
function BaseVehicle:isAnyDoorLocked() end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setStoplightsOn(arg0) end

---@public
---@param arg0 int
---@return boolean
function BaseVehicle:isExitBlocked(arg0) end

---@private
---@return void
function BaseVehicle:applyImpulseFromHitZombies() end

---@public
---@return boolean
function BaseVehicle:isRegulator() end

---@private
---@return void
function BaseVehicle:doOtherBodyWorkDamage() end

---@private
---@return void
function BaseVehicle:renderAreas() end

---@public
---@return float
function BaseVehicle:getSpeed2D() end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setMass(arg0) end

---@private
---@param arg0 VehiclePart
---@param arg1 int
---@return void
function BaseVehicle:checkUninstall2(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 float
---@return void
function BaseVehicle:setTireInflation(arg0, arg1) end

---@private
---@return String
function BaseVehicle:getEngineSound() end

---@public
---@param arg0 int
---@param arg1 int
---@return float
function BaseVehicle:getSwitchSeatAnimRate(arg0, arg1) end

---@public
---@param arg0 IsoZombie
---@return void
---@overload fun(arg0:IsoGameCharacter, arg1:BaseVehicle.HitVars)
function BaseVehicle:hitCharacter(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 BaseVehicle.HitVars
---@return void
function BaseVehicle:hitCharacter(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@return void
function BaseVehicle:switchSeatRSync(arg0, arg1) end

---@public
---@return void
function BaseVehicle:updatePhysicsNetwork() end

---@public
---@return double
function BaseVehicle:getEngineSpeed() end

---@private
---@return void
function BaseVehicle:initPolyPlusRadiusBounds() end

---@public
---@param arg0 long
---@return int
function BaseVehicle:stopSound(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function BaseVehicle:isIntersectingSquare(arg0, arg1, arg2) end

---@public
---@return void
function BaseVehicle:addKeyToWorld() end

---@public
---@return int
function BaseVehicle:getPartCount() end

---@public
---@return boolean
function BaseVehicle:getHeadlightsOn() end

---@public
---@return float
function BaseVehicle:getBrakeSpeedBetweenUpdate() end

---@public
---@return boolean
function BaseVehicle:shouldCollideWithObjects() end

---@public
---@param arg0 long
---@param arg1 GameSoundClip
---@return void
function BaseVehicle:updateEvent(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setDoColor(arg0) end

---@public
---@param arg0 int
---@return void
function BaseVehicle:setLightbarLightsMode(arg0) end

---@public
---@param arg0 VehiclePart
---@param arg1 IsoGameCharacter
---@return boolean
function BaseVehicle:canLockDoor(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setBrakingForce(arg0) end

---@public
---@return void
function BaseVehicle:engineDoStalling() end

---@public
---@param arg0 InventoryItem
---@return void
function BaseVehicle:putKeyInIgnition(arg0) end

---@public
---@return float
function BaseVehicle:getBrakingForce() end

---@private
---@return void
function BaseVehicle:doWindowDamage() end

---@public
---@return void
function BaseVehicle:updatePhysics() end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function BaseVehicle:isCharacterAdjacentTo(arg0) end

---@protected
---@param arg0 VehiclePart
---@param arg1 VehicleScript.Model
---@param arg2 boolean
---@return BaseVehicle.ModelInfo
function BaseVehicle:setModelVisible(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@return void
function BaseVehicle:Damage(arg0) end

---@public
---@return boolean
function BaseVehicle:isKeyIsOnDoor() end

---@public
---@return float
function BaseVehicle:getAngleZ() end

---@public
---@return void
function BaseVehicle:removeKeyFromDoor() end

---@public
---@return void
function BaseVehicle:checkPhysicsValidWithServer() end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:debugSetStatic(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return VehiclePart
function BaseVehicle:getNearestBodyworkPart(arg0) end

---@public
---@param arg0 IsoObject
---@return void
function BaseVehicle:doChrHitImpulse(arg0) end

---@public
---@param arg0 VehiclePart
---@return void
function BaseVehicle:transmitPartDoor(arg0) end

---@public
---@return boolean
function BaseVehicle:hasHeadlights() end

---@public
---@param arg0 String
---@return Texture
---@overload fun(arg0:String, arg1:int)
function BaseVehicle:LoadVehicleTexture(arg0) end

---@public
---@param arg0 String
---@param arg1 int
---@return Texture
function BaseVehicle:LoadVehicleTexture(arg0, arg1) end

---@public
---@return void
function BaseVehicle:engineDoStartingFailed() end

---@public
---@return String
function BaseVehicle:getScriptName() end

---@public
---@return void
function BaseVehicle:update() end

---@public
---@param arg0 int
---@param arg1 IsoGameCharacter
---@param arg2 BaseVehicle
---@return boolean
function BaseVehicle:enterRSync(arg0, arg1, arg2) end

---@public
---@return int
function BaseVehicle:getEnginePower() end

---@public
---@return boolean
function BaseVehicle:shouldCollideWithCharacters() end

---@public
---@return BaseVehicle.MinMaxPosition
function BaseVehicle:getMinMaxPosition() end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function BaseVehicle:setColorHSV(arg0, arg1, arg2) end

---@private
---@param arg0 float[]
---@param arg1 float[]
---@param arg2 float
---@return void
function BaseVehicle:doBloodOverlayFront(arg0, arg1, arg2) end

---@private
---@return void
function BaseVehicle:renderAuthorizations() end

---@public
---@return boolean
function BaseVehicle:isAnyListenerInside() end

---@private
---@param arg0 VehicleScript.Area
---@return JVector2
---@overload fun(arg0:VehicleScript.Area, arg1:JVector2)
function BaseVehicle:areaPositionLocal(arg0) end

---@private
---@param arg0 VehicleScript.Area
---@param arg1 JVector2
---@return JVector2
function BaseVehicle:areaPositionLocal(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setDebugZ(arg0) end

---@private
---@return void
function BaseVehicle:updateVelocityMultiplier() end

---@public
---@param arg0 float
---@param arg1 boolean
---@return void
function BaseVehicle:crash(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 Vector3f
---@return Vector3f
function BaseVehicle:getTowingWorldPos(arg0, arg1) end

---@public
---@param arg0 int
---@return boolean
function BaseVehicle:clearPassenger(arg0) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setMechanicUIOpen(arg0) end

---@private
---@return void
function BaseVehicle:randomizeContainers() end

---@public
---@param arg0 float
---@return void
function BaseVehicle:damagePlayers(arg0) end

---@public
---@return VehiclePart
function BaseVehicle:getBattery() end

---@public
---@param arg0 IsoGameCharacter
---@return BaseVehicle.HitVars
function BaseVehicle:checkCollision(arg0) end

---@private
---@return String
function BaseVehicle:getIgnitionFailSound() end

---@public
---@return void
function BaseVehicle:updateBulletStats() end

---@public
---@param arg0 Vector3f
---@return Vector3f
function BaseVehicle:getUpVector(arg0) end

---@public
---@return VehicleEngineRPM
function BaseVehicle:getVehicleEngineRPM() end

---@public
---@param arg0 int
---@return void
function BaseVehicle:addDamageFrontHitAChr(arg0) end

---@public
---@return boolean
---@overload fun(arg0:boolean, arg1:boolean, arg2:boolean, arg3:boolean)
function BaseVehicle:isInvalidChunkAround() end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@param arg2 boolean
---@param arg3 boolean
---@return boolean
function BaseVehicle:isInvalidChunkAround(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setInitialMass(arg0) end

---@public
---@return boolean
function BaseVehicle:getKeySpawned() end

---@public
---@param arg0 VehiclePart
---@return void
function BaseVehicle:transmitPartWindow(arg0) end

---@public
---@return boolean
function BaseVehicle:isRemovedFromWorld() end

---@public
---@return BaseSoundEmitter
function BaseVehicle:getEmitter() end

---@public
---@param arg0 Vector3f
---@param arg1 Vector3f
---@return Vector3f
---@overload fun(arg0:Vector3f, arg1:Vector3f, arg2:VehicleScript)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:Vector3f)
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:Vector3f, arg4:VehicleScript)
function BaseVehicle:getWorldPos(arg0, arg1) end

---@public
---@param arg0 Vector3f
---@param arg1 Vector3f
---@param arg2 VehicleScript
---@return Vector3f
function BaseVehicle:getWorldPos(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 Vector3f
---@return Vector3f
function BaseVehicle:getWorldPos(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 Vector3f
---@param arg4 VehicleScript
---@return Vector3f
function BaseVehicle:getWorldPos(arg0, arg1, arg2, arg3, arg4) end

---@public
---@param arg0 BaseVehicle
---@param arg1 String
---@param arg2 String
---@param arg3 float
---@return void
function BaseVehicle:setVehicleTowedBy(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@return VehiclePart
function BaseVehicle:getPartById(arg0) end

---@public
---@return float
function BaseVehicle:getUpVectorDot() end

---@public
---@return void
function BaseVehicle:repair() end

---@public
---@param arg0 int
---@return VehiclePart
function BaseVehicle:getLightByIndex(arg0) end

---@public
---@param arg0 String
---@param arg1 Vector3f
---@return Vector3f
function BaseVehicle:getAttachmentLocalPos(arg0, arg1) end

---@protected
---@param arg0 IsoObject
---@return float
function BaseVehicle:getObjectY(arg0) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setHotwiredBroken(arg0) end

---@public
---@return void
function BaseVehicle:engineDoIdle() end

---@public
---@param arg0 long
---@param arg1 GameSoundClip
---@param arg2 BitSet
---@return void
function BaseVehicle:startEvent(arg0, arg1, arg2) end

---@private
---@return String
function BaseVehicle:getIgnitionFailNoPowerSound() end

---@private
---@param arg0 float[]
---@param arg1 float[]
---@param arg2 float
---@return void
function BaseVehicle:doBloodOverlayRight(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function BaseVehicle:setColor(arg0, arg1, arg2) end

---@public
---@param arg0 BaseVehicle
---@param arg1 String
---@param arg2 String
---@param arg3 float
---@return void
function BaseVehicle:setVehicleTowing(arg0, arg1, arg2, arg3) end

---@private
---@return void
function BaseVehicle:createParts() end

---@public
---@param arg0 JVector2
---@return void
function BaseVehicle:releaseVector2(arg0) end

---@public
---@return boolean
function BaseVehicle:getHeadlightCanEmmitLight() end

---@public
---@return boolean
function BaseVehicle:isKeysInIgnition() end

---@public
---@param arg0 int
---@param arg1 Vector3f
---@return void
function BaseVehicle:getWheelForwardVector(arg0, arg1) end

---@public
---@param arg0 byte
---@param arg1 short
---@return void
function BaseVehicle:netPlayerFromServerUpdate(arg0, arg1) end

---@public
---@return float
function BaseVehicle:getBaseQuality() end

---@public
---@return VehicleScript
function BaseVehicle:getScript() end

---@public
---@param arg0 int
---@param arg1 int
---@return String
function BaseVehicle:getSwitchSeatAnimName(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 String
---@return void
---@overload fun(arg0:int, arg1:String, arg2:IsoGameCharacter)
function BaseVehicle:playPassengerAnim(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 String
---@param arg2 IsoGameCharacter
---@return void
function BaseVehicle:playPassengerAnim(arg0, arg1, arg2) end

---@public
---@return float
function BaseVehicle:getRegulatorSpeed() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 JVector2
---@return void
function BaseVehicle:getFacingPosition(arg0, arg1) end

---@private
---@return void
function BaseVehicle:renderExits() end

---@public
---@param arg0 IsoGameCharacter
---@return VehiclePart
function BaseVehicle:getClosestWindow(arg0) end

---@public
---@return void
function BaseVehicle:addKeyToGloveBox() end

---@public
---@param arg0 String
---@param arg1 IsoGameCharacter
---@return float
function BaseVehicle:getAreaDist(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setMaxSpeed(arg0) end

---@public
---@return boolean
function BaseVehicle:isLocalPhysicSim() end

---@public
---@param arg0 IsoMovingObject
---@return void
function BaseVehicle:Thump(arg0) end

---@public
---@return double
function BaseVehicle:getSirenStartTime() end

---@public
---@return InventoryItem
function BaseVehicle:createVehicleKey() end

---@public
---@param arg0 VehiclePart
---@return void
function BaseVehicle:transmitPartModData(arg0) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setKeyIsOnDoor(arg0) end

---@private
---@return Vector3f
function BaseVehicle:allocVector3f() end

---@public
---@return void
function BaseVehicle:renderShadow() end

---@private
---@param arg0 float[]
---@param arg1 float[]
---@param arg2 float
---@return void
function BaseVehicle:doBloodOverlayLeft(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@return boolean
function BaseVehicle:isInBounds(arg0, arg1) end

---@public
---@param arg0 IsoDeadBody
---@param arg1 boolean
---@return int
function BaseVehicle:testCollisionWithCorpse(arg0, arg1) end

---@public
---@return void
function BaseVehicle:removeFromWorld() end

---@public
---@param arg0 float
---@return void
function BaseVehicle:damageObjects(arg0) end

---@public
---@return String
function BaseVehicle:getObjectName() end

---@public
---@param arg0 ByteBuffer
---@param arg1 int
---@param arg2 boolean
---@return void
function BaseVehicle:load(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 ByteBuffer
---@return void
function BaseVehicle:loadChange(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return int
function BaseVehicle:calculateDamageWithCharacter(arg0) end

---@public
---@return PolygonalMap2.VehiclePoly
function BaseVehicle:getPolyPlusRadius() end

---@public
---@param arg0 IsoGameCharacter
---@return Thumpable
function BaseVehicle:getThumpableFor(arg0) end

---@public
---@param arg0 int
---@return int
function BaseVehicle:getPassengerSwitchSeatCount(arg0) end

---@public
---@return int
function BaseVehicle:getLightCount() end

---@public
---@return int
function BaseVehicle:getJoypad() end

---@private
---@param arg0 int
---@return void
function BaseVehicle:addDamageRear(arg0) end

---@public
---@return void
function BaseVehicle:LoadAllVehicleTextures() end

---@public
---@return void
function BaseVehicle:updatePartStats() end

---@private
---@return void
function BaseVehicle:renderUsableArea() end

---@public
---@return void
function BaseVehicle:transmitBlood() end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setPhysicsActive(arg0) end

---@public
---@return void
---@overload fun(arg0:boolean)
function BaseVehicle:tryStartEngine() end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:tryStartEngine(arg0) end

---@public
---@param arg0 int
---@param arg1 IsoGameCharacter
---@param arg2 Vector3f
---@return boolean
function BaseVehicle:setPassenger(arg0, arg1, arg2) end

---@public
---@return void
function BaseVehicle:authorizationServerUpdate() end

---@public
---@param arg0 BaseVehicle
---@return boolean
function BaseVehicle:testCollisionWithVehicle(arg0) end

---@public
---@return void
function BaseVehicle:postupdate() end

---@public
---@return void
function BaseVehicle:constraintChanged() end

---@public
---@param arg0 ByteBuffer
---@param arg1 boolean
---@return void
function BaseVehicle:save(arg0, arg1) end

---@public
---@param arg0 IsoLightSource
---@param arg1 Vector3f
---@return void
function BaseVehicle:fixLightbarModelLighting(arg0, arg1) end

---@public
---@param arg0 short
---@param arg1 boolean
---@return void
function BaseVehicle:authorizationServerCollide(arg0, arg1) end

---@protected
---@param arg0 IsoObject
---@return float
function BaseVehicle:getObjectX(arg0) end

---@public
---@return void
function BaseVehicle:createPhysics() end

---@public
---@param arg0 ItemContainer
---@param arg1 IsoGridSquare
---@param arg2 IsoObject
---@return void
function BaseVehicle:putKeyToContainer(arg0, arg1, arg2) end

---@public
---@return void
function BaseVehicle:shutOff() end

---@public
---@return void
function BaseVehicle:engineDoShuttingDown() end

---@public
---@param arg0 int
---@param arg1 String
---@return void
function BaseVehicle:playPassengerSound(arg0, arg1) end

---@public
---@param arg0 int
---@return VehiclePart
function BaseVehicle:getPassengerDoor(arg0) end

---@public
---@return int
function BaseVehicle:getLightbarSirenMode() end

---@public
---@return boolean
function BaseVehicle:getStoplightsOn() end

---@public
---@return float
function BaseVehicle:getCurrentSteering() end

---@public
---@return short
function BaseVehicle:getId() end

---@public
---@param arg0 Vector3f
---@param arg1 Vector3f
---@return Vector3f
---@overload fun(arg0:float, arg1:float, arg2:float, arg3:Vector3f)
function BaseVehicle:getLocalPos(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 Vector3f
---@return Vector3f
function BaseVehicle:getLocalPos(arg0, arg1, arg2, arg3) end

---@private
---@param arg0 VehicleScript.Skin
---@return void
---@overload fun(arg0:VehicleScript)
function BaseVehicle:LoadVehicleTextures(arg0) end

---@public
---@param arg0 VehicleScript
---@return void
function BaseVehicle:LoadVehicleTextures(arg0) end

---@public
---@return float
function BaseVehicle:getMass() end

---@public
---@return JVector2
function BaseVehicle:allocVector2() end

---@public
---@return void
function BaseVehicle:triggerAlarm() end

---@public
---@return CarController
function BaseVehicle:getController() end

---@public
---@param arg0 Transform
---@return void
function BaseVehicle:setWorldTransform(arg0) end

---@public
---@param arg0 long
---@return void
function BaseVehicle:lockServerUpdate(arg0) end

---@public
---@param arg0 IsoZombie
---@return void
function BaseVehicle:putKeyToZombie(arg0) end

---@public
---@return float
function BaseVehicle:getAngleX() end

---@public
---@param arg0 IsoObject
---@param arg1 float
---@param arg2 JVector2
---@return JVector2
function BaseVehicle:testCollisionWithObject(arg0, arg1, arg2) end

---@public
---@return boolean
function BaseVehicle:isKeyboardControlled() end

---@private
---@param arg0 boolean
---@return int
function BaseVehicle:caclulateDamageWithBodies(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 boolean
---@return int
---@overload fun(arg0:IsoMovingObject, arg1:float, arg2:float, arg3:boolean)
function BaseVehicle:testCollisionWithProneCharacter(arg0, arg1) end

---@public
---@param arg0 IsoMovingObject
---@param arg1 float
---@param arg2 float
---@param arg3 boolean
---@return int
function BaseVehicle:testCollisionWithProneCharacter(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 int
---@return void
function BaseVehicle:setMechanicalID(arg0) end

---@public
---@return void
function BaseVehicle:resumeRunningAfterLoad() end

---@public
---@return float
function BaseVehicle:getFakeSpeedModifier() end

---@public
---@return void
function BaseVehicle:updateTotalMass() end

---@public
---@return int
function BaseVehicle:getSqlId() end

---@public
---@param arg0 VehiclePart
---@param arg1 IsoGameCharacter
---@return boolean
function BaseVehicle:canUnlockDoor(arg0, arg1) end

---@private
---@return String
function BaseVehicle:getEngineStartSound() end

---@public
---@return boolean
function BaseVehicle:isHotwired() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 float
---@param arg2 JVector2
---@return JVector2
function BaseVehicle:testCollisionWithCharacter(arg0, arg1, arg2) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@return boolean
function BaseVehicle:isEnterBlocked(arg0, arg1) end

---@public
---@param arg0 VehiclePart
---@param arg1 IsoGameCharacter
---@param arg2 boolean
---@return void
function BaseVehicle:toggleLockedDoor(arg0, arg1, arg2) end

---@public
---@param arg0 Transform
---@return Transform
function BaseVehicle:getWorldTransform(arg0) end

---@public
---@param arg0 VehiclePart
---@return void
function BaseVehicle:transmitPartCondition(arg0) end

---@private
---@return void
function BaseVehicle:drawTowingRope() end

---@public
---@param arg0 int
---@return String
function BaseVehicle:getPassengerArea(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return int
function BaseVehicle:getBestSeat(arg0) end

---@public
---@param arg0 float
---@return void
function BaseVehicle:setClientForce(arg0) end

---@public
---@return float
function BaseVehicle:getFudgedMass() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return void
function BaseVehicle:setEngineFeature(arg0, arg1, arg2) end

---@public
---@return void
function BaseVehicle:engineDoStartingFailedNoPower() end

---@public
---@return float
function BaseVehicle:getOffroadEfficiency() end

---@private
---@return void
function BaseVehicle:removeWorldLights() end

---@public
---@return void
function BaseVehicle:updateParts() end

---@private
---@param arg0 VehiclePart
---@param arg1 ItemPickerJava.ItemPickerRoom
---@return void
function BaseVehicle:randomizeContainer(arg0, arg1) end

---@public
---@param arg0 int
---@param arg1 int
---@return VehicleScript.Passenger.SwitchSeat
function BaseVehicle:getPassengerSwitchSeat(arg0, arg1) end

---@private
---@param arg0 int
---@param arg1 int
---@return VehicleScript.Passenger.SwitchSeat
function BaseVehicle:getSwitchSeat(arg0, arg1) end

---@public
---@return void
function BaseVehicle:brekingObjects() end

---@public
---@param arg0 String
---@param arg1 IsoGameCharacter
---@return boolean
function BaseVehicle:isInArea(arg0, arg1) end

---@public
---@return float
function BaseVehicle:getCurrentSpeedKmHour() end

---@public
---@return boolean
function BaseVehicle:isOperational() end

---@public
---@param arg0 byte
---@return void
function BaseVehicle:setNetPlayerAuthorization(arg0) end

---@public
---@param arg0 int
---@param arg1 int
---@return boolean
function BaseVehicle:canSwitchSeat(arg0, arg1) end

---@public
---@return String
function BaseVehicle:getTowAttachmentSelf() end

---@public
---@return void
function BaseVehicle:doBloodOverlay() end

---@public
---@param arg0 float
---@param arg1 float
---@return boolean
function BaseVehicle:isPositionOnLeftOrRight(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@param arg2 String
---@return void
function BaseVehicle:setCharacterPositionToAnim(arg0, arg1, arg2) end

---@public
---@return InventoryItem
function BaseVehicle:getCurrentKey() end

---@public
---@param arg0 int
---@return boolean
function BaseVehicle:hasRoof(arg0) end

---@public
---@param arg0 InventoryItem
---@return void
function BaseVehicle:setCurrentKey(arg0) end

---@public
---@param arg0 VehicleScript.Area
---@return JVector2
---@overload fun(arg0:VehicleScript.Area, arg1:JVector2)
function BaseVehicle:areaPositionWorld(arg0) end

---@public
---@param arg0 VehicleScript.Area
---@param arg1 JVector2
---@return JVector2
function BaseVehicle:areaPositionWorld(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 Vector3f
---@return Vector3f
function BaseVehicle:getTowedByLocalPos(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:authorizationClientForecast(arg0) end

---@private
---@param arg0 int
---@return void
function BaseVehicle:addDamageFront(arg0) end

---@public
---@return float
function BaseVehicle:getAngleY() end

---@private
---@return void
function BaseVehicle:doAlarm() end

---@public
---@return void
function BaseVehicle:onBackMoveSignalStop() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@param arg2 String
---@return void
function BaseVehicle:setCharacterPosition(arg0, arg1, arg2) end

---@private
---@param arg0 int
---@param arg1 int
---@return boolean
function BaseVehicle:isNullChunk(arg0, arg1) end

---@public
---@return void
function BaseVehicle:releaseAnimationPlayers() end

---@private
---@return void
function BaseVehicle:renderTrailerPositions() end

---@private
---@param arg0 IsoGridSquare
---@param arg1 IsoObject
---@param arg2 JVector2
---@return void
function BaseVehicle:checkCollisionWithPlant(arg0, arg1, arg2) end

---@public
---@return String
function BaseVehicle:getZone() end

---@public
---@return void
function BaseVehicle:flipUpright() end

---@public
---@param arg0 IsoGameCharacter
---@return VehiclePart
---@overload fun(arg0:IsoGameCharacter, arg1:boolean)
function BaseVehicle:getUseablePart(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 boolean
---@return VehiclePart
function BaseVehicle:getUseablePart(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setGoodCar(arg0) end

---@public
---@param arg0 BaseVehicle
---@param arg1 float
---@return void
function BaseVehicle:HitByVehicle(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function BaseVehicle:updateHasExtendOffsetForExitEnd(arg0) end

---@public
---@param arg0 int
---@param arg1 String
---@return VehicleScript.Position
function BaseVehicle:getPassengerPosition(arg0, arg1) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return void
function BaseVehicle:setAngles(arg0, arg1, arg2) end

---@public
---@return IsoGridSquare
function BaseVehicle:getSquare() end

---@public
---@return String
function BaseVehicle:getTowAttachmentOther() end

---@public
---@param arg0 ByteBuffer
---@return void
function BaseVehicle:netPlayerServerSendAuthorisation(arg0) end

---@public
---@return String
function BaseVehicle:getSkin() end

---@public
---@param arg0 int
---@param arg1 int
---@return String
function BaseVehicle:getSwitchSeatSound(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setAlarmed(arg0) end

---@private
---@param arg0 int
---@param arg1 float[]
---@param arg2 Vector3f
---@param arg3 float
---@param arg4 int
---@param arg5 double
---@param arg6 double
---@return void
function BaseVehicle:updateBulletStatsWheel(arg0, arg1, arg2, arg3, arg4, arg5, arg6) end

---@public
---@return int
function BaseVehicle:getSkinIndex() end

---@public
---@param arg0 int
---@return void
function BaseVehicle:setSkinIndex(arg0) end

---@public
---@param arg0 int
---@return boolean
---@overload fun(arg0:IsoGameCharacter)
function BaseVehicle:showPassenger(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function BaseVehicle:showPassenger(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 Vector3f
---@return Vector3f
function BaseVehicle:getPlayerTrailerLocalPos(arg0, arg1, arg2) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@return void
function BaseVehicle:switchSeat(arg0, arg1) end

---@public
---@param arg0 IsoGridSquare
---@return void
function BaseVehicle:putKeyToWorld(arg0) end

---@public
---@return int
function BaseVehicle:windowsOpen() end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function BaseVehicle:couldCrawlerAttackPassenger(arg0) end

---@public
---@return int
function BaseVehicle:getMaxPassengers() end

---@public
---@return VehiclePart
function BaseVehicle:getHeater() end

---@public
---@param arg0 IsoObject
---@param arg1 float
---@return void
function BaseVehicle:ApplyImpulse4Break(arg0, arg1) end

---@public
---@param arg0 VehiclePart
---@param arg1 String
---@return void
function BaseVehicle:playPartAnim(arg0, arg1) end

---@public
---@return float
function BaseVehicle:getInitialMass() end

---@public
---@param arg0 int
---@param arg1 String
---@return VehicleScript.Anim
function BaseVehicle:getPassengerAnim(arg0, arg1) end

---@public
---@return int
function BaseVehicle:getEngineLoudness() end

---@public
---@param arg0 BaseVehicle
---@return void
function BaseVehicle:updateConstraint(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function BaseVehicle:updateHitByVehicle(arg0) end

---@public
---@return void
function BaseVehicle:engineDoStartingSuccess() end

---@public
---@return boolean
function BaseVehicle:isStarting() end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function BaseVehicle:exit(arg0) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setHeadlightsOn(arg0) end

---@public
---@param arg0 VehiclePart
---@param arg1 String
---@return void
function BaseVehicle:playPartSound(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function BaseVehicle:isCollided(arg0) end

---@public
---@return void
function BaseVehicle:engineDoRetryingStarting() end

---@private
---@return void
function BaseVehicle:applyImpulseFromProneCharacters() end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@return void
function BaseVehicle:breakConstraint(arg0, arg1) end

---@public
---@return boolean
function BaseVehicle:isEngineStarted() end

---@private
---@param arg0 float[]
---@param arg1 float[]
---@param arg2 float
---@return void
function BaseVehicle:doBloodOverlayAux(arg0, arg1, arg2) end

---@public
---@param arg0 VehiclePart
---@param arg1 String
---@param arg2 IsoGameCharacter
---@return void
function BaseVehicle:playActorAnim(arg0, arg1, arg2) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setBraking(arg0) end

---@public
---@param arg0 float
---@param arg1 float
---@return void
function BaseVehicle:setGeneralPartCondition(arg0, arg1) end

---@public
---@return float
function BaseVehicle:getColorSaturation() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@return boolean
function BaseVehicle:isPassengerUseDoor2(arg0, arg1) end

---@public
---@param arg0 IsoGameCharacter
---@return boolean
function BaseVehicle:isDriver(arg0) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setActiveInBullet(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@return void
function BaseVehicle:updateHasExtendOffset(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 HandWeapon
---@return void
function BaseVehicle:hitVehicle(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setRegulator(arg0) end

---@public
---@param arg0 Vector3f
---@return Vector3f
function BaseVehicle:getForwardVector(arg0) end

---@public
---@return int
function BaseVehicle:getKeyId() end

---@public
---@return void
function BaseVehicle:authorizationServerOnSeat() end

---@private
---@param arg0 float[]
---@param arg1 float[]
---@param arg2 float
---@return void
function BaseVehicle:doBloodOverlayRear(arg0, arg1, arg2) end

---@public
---@return int
function BaseVehicle:getNumberOfPartsWithContainers() end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setTrunkLocked(arg0) end

---@public
---@return boolean
function BaseVehicle:isDoColor() end

---@private
---@return void
function BaseVehicle:tryReconnectToTowedVehicle() end

---@public
---@return boolean
function BaseVehicle:hasHorn() end

---@public
---@return AnimationPlayer
function BaseVehicle:getAnimationPlayer() end

---@protected
---@param arg0 int
---@return VehicleScript.Passenger
function BaseVehicle:getScriptPassenger(arg0) end

---@public
---@return void
function BaseVehicle:onBackMoveSignalStart() end

---@public
---@param arg0 TransmissionNumber
---@return void
function BaseVehicle:changeTransmission(arg0) end

---@public
---@return boolean
function BaseVehicle:isAlarmed() end

---@public
---@param arg0 int
---@return VehiclePart
function BaseVehicle:getPartForSeatContainer(arg0) end

---@public
---@return boolean
function BaseVehicle:isMechanicUIOpen() end

---@public
---@param arg0 int
---@return VehiclePart
function BaseVehicle:getPassengerDoor2(arg0) end

---@private
---@param arg0 Vector3f
---@return void
function BaseVehicle:releaseVector3f(arg0) end

---@public
---@param arg0 int
---@param arg1 Vector3f
---@return Vector3f
function BaseVehicle:getPassengerWorldPos(arg0, arg1) end

---@private
---@return void
function BaseVehicle:updateEngineStarting() end

---@public
---@return boolean
function BaseVehicle:hasLightbar() end

---@public
---@param arg0 int
---@return void
function BaseVehicle:setLightbarSirenMode(arg0) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setHotwired(arg0) end

---@public
---@return void
function BaseVehicle:addToWorld() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 VehiclePart
---@return boolean
function BaseVehicle:canInstallPart(arg0, arg1) end

---@public
---@return boolean
function BaseVehicle:isGoodCar() end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setAddThumpWorldSound(arg0) end

---@public
---@return float
function BaseVehicle:getClientForce() end

---@public
---@return int
function BaseVehicle:getMechanicalID() end

---@public
---@return IsoGameCharacter
function BaseVehicle:getDriver() end

---@public
---@param arg0 double
---@return void
function BaseVehicle:setSirenStartTime(arg0) end

---@public
---@return boolean
function BaseVehicle:areAllDoorsLocked() end

---@public
---@param arg0 BaseVehicle
---@return void
function BaseVehicle:positionTrailer(arg0) end

---@public
---@return boolean
function BaseVehicle:hasBackSignal() end

---@public
---@return String
function BaseVehicle:getVehicleType() end

---@public
---@param arg0 Vector3f
---@param arg1 Vector3f
---@return void
function BaseVehicle:addImpulse(arg0, arg1) end

---@private
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@return float
function BaseVehicle:clamp(arg0, arg1, arg2) end

---@public
---@param arg0 float
---@param arg1 float
---@param arg2 float
---@param arg3 float
---@return boolean
function BaseVehicle:circleIntersects(arg0, arg1, arg2, arg3) end

---@public
---@return boolean
function BaseVehicle:sirenShutoffTimeExpired() end

---@public
---@return boolean
function BaseVehicle:isHotwiredBroken() end

---@public
---@param arg0 int
---@return void
function BaseVehicle:tryHotwire(arg0) end

---@public
---@param arg0 VehiclePart
---@return void
function BaseVehicle:transmitPartItem(arg0) end

---@public
---@param arg0 String
---@return JVector2
---@overload fun(arg0:String, arg1:JVector2)
function BaseVehicle:getAreaCenter(arg0) end

---@public
---@param arg0 String
---@param arg1 JVector2
---@return JVector2
function BaseVehicle:getAreaCenter(arg0, arg1) end

---@public
---@param arg0 InventoryItem
---@return void
function BaseVehicle:putKeyOnDoor(arg0) end

---@public
---@param arg0 Vector3f
---@return Vector3f
function BaseVehicle:getLinearVelocity(arg0) end

---@public
---@return boolean
function BaseVehicle:haveOneDoorUnlocked() end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@param arg2 InventoryItem
---@return void
function BaseVehicle:syncKeyInIgnition(arg0, arg1, arg2) end

---@public
---@return float
function BaseVehicle:getColorValue() end

---@public
---@param arg0 IsoGameCharacter
---@return void
function BaseVehicle:updateHasExtendOffsetForExit(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 int
---@return boolean
function BaseVehicle:isEnterBlocked2(arg0, arg1) end

---@public
---@return BaseVehicle
function BaseVehicle:getVehicleTowing() end

---@public
---@return float
function BaseVehicle:getBatteryCharge() end

---@public
---@param arg0 int
---@param arg1 int
---@param arg2 int
---@return boolean
function BaseVehicle:blocked(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@return void
function BaseVehicle:setScriptName(arg0) end

---@public
---@return float
function BaseVehicle:getRust() end

---@private
---@return void
function BaseVehicle:renderIntersectedSquares() end

---@public
---@param arg0 int
---@param arg1 IsoGameCharacter
---@return boolean
function BaseVehicle:canAccessContainer(arg0, arg1) end

---@public
---@param arg0 int
---@return boolean
function BaseVehicle:isSeatInstalled(arg0) end

---@public
---@param arg0 int
---@return void
function BaseVehicle:addDamageRearHitAChr(arg0) end

---@public
---@return void
function BaseVehicle:onHornStop() end

---@public
---@return void
function BaseVehicle:drainBatteryUpdateHack() end

---@private
---@return void
function BaseVehicle:doDoorDamage() end

---@public
---@param arg0 int
---@return BaseVehicle.Passenger
function BaseVehicle:getPassenger(arg0) end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 float
---@return void
function BaseVehicle:addRandomDamageFromCrash(arg0, arg1) end

---@public
---@return void
function BaseVehicle:onHornStart() end

---@public
---@return int
function BaseVehicle:getEngineQuality() end

---@protected
---@param arg0 AnimationPlayer
---@param arg1 VehiclePart
---@return void
function BaseVehicle:updateAnimationPlayer(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function BaseVehicle:setNeedPartsUpdate(arg0) end

---@private
---@return void
function BaseVehicle:initParts() end

---@public
---@return int
function BaseVehicle:getLightbarLightsMode() end

---@public
---@return void
function BaseVehicle:doDamageOverlay() end

---@public
---@param arg0 String
---@param arg1 Vector3f
---@return Vector3f
function BaseVehicle:getTowedByWorldPos(arg0, arg1) end

---@public
---@return boolean
function BaseVehicle:isDoingOffroad() end

---@public
---@return void
function BaseVehicle:renderlast() end

---@public
---@return boolean
function BaseVehicle:isEngineRunning() end

---@public
---@return void
function BaseVehicle:transmitRust() end

---@public
---@param arg0 long
---@param arg1 GameSoundClip
---@param arg2 BitSet
---@return void
function BaseVehicle:stopEvent(arg0, arg1, arg2) end

---@public
---@return boolean
function BaseVehicle:needPartsUpdate() end

---@public
---@return float
function BaseVehicle:getThumpCondition() end

---@public
---@return boolean
function BaseVehicle:getWindowLightsOn() end

---@public
---@param arg0 String
---@return void
function BaseVehicle:playSound(arg0) end

---@public
---@param arg0 boolean
---@param arg1 boolean
---@return void
function BaseVehicle:cheatHotwire(arg0, arg1) end

---@public
---@param arg0 int
---@return boolean
function BaseVehicle:isSeatOccupied(arg0) end

---@public
---@return boolean
function BaseVehicle:isTrunkLocked() end

---@public
---@param arg0 String
---@param arg1 KahluaTable
---@param arg2 ByteBuffer
---@return void
function BaseVehicle:saveChange(arg0, arg1, arg2) end

---@public
---@return void
function BaseVehicle:updateLights() end

---@private
---@param arg0 VehiclePart
---@param arg1 int
---@param arg2 boolean
---@return void
function BaseVehicle:checkDamage(arg0, arg1, arg2) end

---@public
---@return String
function BaseVehicle:getTransmissionNumberLetter() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 IsoGameCharacter
---@param arg2 Vector3f
---@return Vector3f
function BaseVehicle:chooseBestAttackPosition(arg0, arg1, arg2) end

---@private
---@return void
function BaseVehicle:updateWorldLights() end

---@protected
---@param arg0 VehiclePart
---@return BaseVehicle.ModelInfo
function BaseVehicle:getModelInfoForPart(arg0) end

---@public
---@param arg0 String
---@param arg1 boolean
---@param arg2 Vector3f
---@return Vector3f
function BaseVehicle:getPlayerTrailerWorldPos(arg0, arg1, arg2) end

---@public
---@return float
function BaseVehicle:getDebugZ() end

---@public
---@param arg0 UdpConnection
---@return boolean
function BaseVehicle:authorizationServerOnOwnerData(arg0) end

---@public
---@return void
function BaseVehicle:updateControls() end

---@protected
---@return void
function BaseVehicle:updateTransform() end
