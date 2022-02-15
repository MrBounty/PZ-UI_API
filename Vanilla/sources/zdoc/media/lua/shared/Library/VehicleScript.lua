---@class VehicleScript : zombie.scripting.objects.VehicleScript
---@field private fileName String
---@field private name String
---@field private models ArrayList|Unknown
---@field public m_attachments ArrayList|Unknown
---@field private mass float
---@field private centerOfMassOffset Vector3f
---@field private engineForce float
---@field private engineIdleSpeed float
---@field private steeringIncrement float
---@field private steeringClamp float
---@field private steeringClampMax float
---@field private wheelFriction float
---@field private stoppingMovementForce float
---@field private suspensionStiffness float
---@field private suspensionDamping float
---@field private suspensionCompression float
---@field private suspensionRestLength float
---@field private maxSuspensionTravelCm float
---@field private rollInfluence float
---@field private extents Vector3f
---@field private shadowExtents Vector2f
---@field private shadowOffset Vector2f
---@field private bHadShadowOExtents boolean
---@field private bHadShadowOffset boolean
---@field private extentsOffset Vector2f
---@field private physicsChassisShape Vector3f
---@field private m_physicsShapes ArrayList|Unknown
---@field private wheels ArrayList|Unknown
---@field private passengers ArrayList|Unknown
---@field public maxSpeed float
---@field public isSmallVehicle boolean
---@field public spawnOffsetY float
---@field private frontEndHealth int
---@field private rearEndHealth int
---@field private storageCapacity int
---@field private engineLoudness int
---@field private engineQuality int
---@field private seats int
---@field private mechanicType int
---@field private engineRepairLevel int
---@field private playerDamageProtection float
---@field private forcedHue float
---@field private forcedSat float
---@field private forcedVal float
---@field public leftSirenCol ImmutableColor
---@field public rightSirenCol ImmutableColor
---@field private engineRPMType String
---@field private offroadEfficiency float
---@field private crawlOffsets TFloatArrayList
---@field public gearRatioCount int
---@field public gearRatio float[]
---@field private textures VehicleScript.Skin
---@field private skins ArrayList|Unknown
---@field private areas ArrayList|Unknown
---@field private parts ArrayList|Unknown
---@field private hasSiren boolean
---@field private lightbar VehicleScript.LightBar
---@field private sound VehicleScript.Sounds
---@field public textureMaskEnable boolean
---@field private PHYSICS_SHAPE_BOX int
---@field private PHYSICS_SHAPE_SPHERE int
VehicleScript = {}

---@public
---@return Vector2f
function VehicleScript:getShadowExtents() end

---@public
---@return int
function VehicleScript:getMechanicType() end

---@public
---@param arg0 String
---@return int
function VehicleScript:getIndexOfWheelById(arg0) end

---@public
---@return int
function VehicleScript:getPassengerCount() end

---@public
---@return int
function VehicleScript:getWheelCount() end

---@public
---@param arg0 String
---@return VehicleScript.Part
function VehicleScript:getPartById(arg0) end

---@public
---@param arg0 String
---@return VehicleScript.Model
---@overload fun(arg0:String, arg1:ArrayList|Unknown)
function VehicleScript:getModelById(arg0) end

---@public
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return VehicleScript.Model
function VehicleScript:getModelById(arg0, arg1) end

---@public
---@param arg0 String
---@return void
function VehicleScript:setEngineRPMType(arg0) end

---@public
---@param arg0 int
---@return void
function VehicleScript:setSeats(arg0) end

---@public
---@return float
function VehicleScript:getMass() end

---@private
---@param arg0 ScriptParser.Block
---@param arg1 ArrayList|Unknown
---@return VehicleScript.Model
function VehicleScript:LoadModel(arg0, arg1) end

---@public
---@param arg0 int
---@return ModelAttachment
---@overload fun(arg0:ModelAttachment)
function VehicleScript:removeAttachment(arg0) end

---@public
---@param arg0 ModelAttachment
---@return ModelAttachment
function VehicleScript:removeAttachment(arg0) end

---@public
---@return int
function VehicleScript:getSkinCount() end

---@public
---@return int
function VehicleScript:getPartCount() end

---@private
---@param arg0 ScriptParser.Block
---@param arg1 ArrayList|Unknown
---@return VehicleScript.Position
function VehicleScript:LoadPosition(arg0, arg1) end

---@public
---@return float
function VehicleScript:getRollInfluence() end

---@public
---@return int
function VehicleScript:getEngineRepairLevel() end

---@public
---@param arg0 float
---@return void
function VehicleScript:setForcedSat(arg0) end

---@public
---@return void
function VehicleScript:Loaded() end

---@public
---@param arg0 VehicleScript
---@param arg1 String
---@return void
function VehicleScript:copyPassengersFrom(arg0, arg1) end

---@public
---@param arg0 float
---@return void
function VehicleScript:setModelScale(arg0) end

---@private
---@return void
---@overload fun(arg0:VehicleScript.Wheel)
function VehicleScript:initCrawlOffsets() end

---@private
---@param arg0 VehicleScript.Wheel
---@return void
function VehicleScript:initCrawlOffsets(arg0) end

---@public
---@param arg0 String
---@return VehicleScript.Wheel
function VehicleScript:getWheelById(arg0) end

---@public
---@return Vector3f
function VehicleScript:getExtents() end

---@public
---@return Vector2f
function VehicleScript:getShadowOffset() end

---@public
---@return int
function VehicleScript:getModelCount() end

---@public
---@return String
function VehicleScript:getFileName() end

---@private
---@param arg0 String
---@return void
function VehicleScript:LoadTemplate(arg0) end

---@public
---@return Vector2f
function VehicleScript:getExtentsOffset() end

---@public
---@return boolean
function VehicleScript:getHasSiren() end

---@public
---@return int
function VehicleScript:getHeadlightConfigLevel() end

---@public
---@return float
function VehicleScript:getSuspensionDamping() end

---@public
---@param arg0 String
---@return ModelAttachment
function VehicleScript:getAttachmentById(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@return void
function VehicleScript:Load(arg0, arg1) end

---@private
---@param arg0 ScriptParser.Block
---@return VehicleScript.Window
function VehicleScript:LoadWindow(arg0) end

---@public
---@param arg0 int
---@return VehicleScript.Wheel
function VehicleScript:getWheel(arg0) end

---@public
---@return int
function VehicleScript:getPhysicsShapeCount() end

---@public
---@return float
function VehicleScript:getPlayerDamageProtection() end

---@public
---@return int
function VehicleScript:getAttachmentCount() end

---@public
---@param arg0 String
---@return int
function VehicleScript:getIndexOfAreaById(arg0) end

---@public
---@return float
function VehicleScript:getSteeringIncrement() end

---@private
---@param arg0 ScriptParser.Block
---@return ModelAttachment
function VehicleScript:LoadAttachment(arg0) end

---@public
---@param arg0 int
---@return VehicleScript.PhysicsShape
function VehicleScript:getPhysicsShape(arg0) end

---@public
---@return Vector3f
function VehicleScript:getModelOffset() end

---@public
---@return int
function VehicleScript:getFrontEndHealth() end

---@public
---@return float
function VehicleScript:getForcedVal() end

---@public
---@return int
function VehicleScript:getGearRatioCount() end

---@public
---@param arg0 String
---@return int
function VehicleScript:getPassengerIndex(arg0) end

---@private
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return VehicleScript.Position
function VehicleScript:getPositionById(arg0, arg1) end

---@private
---@param arg0 ScriptParser.Block
---@param arg1 VehicleScript.Container
---@return VehicleScript.Container
function VehicleScript:LoadContainer(arg0, arg1) end

---@public
---@return float
function VehicleScript:getForcedSat() end

---@public
---@param arg0 String
---@return VehicleScript.Passenger
function VehicleScript:getPassengerById(arg0) end

---@private
---@param arg0 ScriptParser.Block
---@return VehicleScript.Area
function VehicleScript:LoadArea(arg0) end

---@public
---@return float
function VehicleScript:getOffroadEfficiency() end

---@public
---@return float
function VehicleScript:getEngineForce() end

---@public
---@param arg0 int
---@param arg1 ModelAttachment
---@return ModelAttachment
function VehicleScript:addAttachmentAt(arg0, arg1) end

---@public
---@param arg0 int
---@return VehicleScript.Area
function VehicleScript:getArea(arg0) end

---@public
---@param arg0 int
---@return VehicleScript.Part
function VehicleScript:getPart(arg0) end

---@public
---@return VehicleScript.LightBar
function VehicleScript:getLightbar() end

---@private
---@param arg0 ScriptParser.Block
---@return VehicleScript.Skin
function VehicleScript:LoadSkin(arg0) end

---@private
---@param arg0 ScriptParser.Block
---@return VehicleScript.Part
function VehicleScript:LoadPart(arg0) end

---@public
---@param arg0 int
---@return VehicleScript.Skin
function VehicleScript:getSkin(arg0) end

---@public
---@param arg0 float
---@return float
function VehicleScript:getSteeringClamp(arg0) end

---@public
---@param arg0 VehicleScript
---@param arg1 String
---@return void
function VehicleScript:copyAreasFrom(arg0, arg1) end

---@public
---@return float
function VehicleScript:getSuspensionStiffness() end

---@public
---@return float
function VehicleScript:getEngineIdleSpeed() end

---@public
---@param arg0 int
---@return ModelAttachment
function VehicleScript:getAttachment(arg0) end

---@private
---@param arg0 ScriptParser.Block
---@return VehicleScript.Wheel
function VehicleScript:LoadWheel(arg0) end

---@public
---@return int
function VehicleScript:getStorageCapacity() end

---@public
---@return VehicleScript.Sounds
function VehicleScript:getSounds() end

---@public
---@return int
function VehicleScript:getAreaCount() end

---@private
---@param arg0 Object
---@return Object
function VehicleScript:checkIntegerKey(arg0) end

---@private
---@param arg0 ScriptParser.Block
---@param arg1 ArrayList|Unknown
---@return VehicleScript.Anim
function VehicleScript:LoadAnim(arg0, arg1) end

---@public
---@return int
function VehicleScript:getSeats() end

---@public
---@param arg0 int
---@return VehicleScript.Passenger
function VehicleScript:getPassenger(arg0) end

---@private
---@param arg0 String
---@param arg1 Vector3f
---@return void
function VehicleScript:LoadVector3f(arg0, arg1) end

---@public
---@return int
function VehicleScript:getEngineQuality() end

---@private
---@param arg0 ScriptParser.Block
---@return VehicleScript.Door
function VehicleScript:LoadDoor(arg0) end

---@public
---@return TFloatArrayList
function VehicleScript:getCrawlOffsets() end

---@public
---@param arg0 float
---@return void
function VehicleScript:setForcedHue(arg0) end

---@public
---@return float
function VehicleScript:getForcedHue() end

---@public
---@param arg0 float
---@return void
function VehicleScript:setPlayerDamageProtection(arg0) end

---@public
---@return float
function VehicleScript:getModelScale() end

---@private
---@param arg0 ScriptParser.Block
---@return VehicleScript.Passenger
function VehicleScript:LoadPassenger(arg0) end

---@private
---@param arg0 float
---@return boolean
function VehicleScript:isOverlappingWheel(arg0) end

---@public
---@param arg0 int
---@return void
function VehicleScript:setEngineRepairLevel(arg0) end

---@public
---@param arg0 float
---@return void
function VehicleScript:setForcedVal(arg0) end

---@private
---@param arg0 String
---@param arg1 Vector2f
---@return void
function VehicleScript:LoadVector2f(arg0, arg1) end

---@private
---@param arg0 ScriptParser.Block
---@return VehicleScript.PhysicsShape
function VehicleScript:LoadPhysicsShape(arg0) end

---@public
---@return VehicleScript.Model
function VehicleScript:getModel() end

---@public
---@param arg0 VehicleScript
---@param arg1 String
---@return void
function VehicleScript:copyPartsFrom(arg0, arg1) end

---@private
---@param arg0 ScriptParser.Block
---@param arg1 VehicleScript.Passenger
---@return VehicleScript.Passenger.SwitchSeat
function VehicleScript:LoadPassengerSwitchSeat(arg0, arg1) end

---@public
---@return float
function VehicleScript:getSuspensionTravel() end

---@public
---@return int
function VehicleScript:getRearEndHealth() end

---@public
---@return Vector3f
function VehicleScript:getPhysicsChassisShape() end

---@public
---@return float
function VehicleScript:getSuspensionRestLength() end

---@private
---@param arg0 ScriptParser.Block
---@param arg1 KahluaTable
---@return KahluaTable
function VehicleScript:LoadTable(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 ArrayList|Unknown
---@return VehicleScript.Anim
function VehicleScript:getAnimationById(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 Vector4f
---@return void
function VehicleScript:LoadVector4f(arg0, arg1) end

---@public
---@return VehicleScript.Skin
function VehicleScript:getTextures() end

---@public
---@param arg0 String
---@return int
function VehicleScript:getIndexOfPartById(arg0) end

---@public
---@return void
function VehicleScript:toBullet() end

---@public
---@return float
function VehicleScript:getWheelFriction() end

---@public
---@return Vector3f
function VehicleScript:getCenterOfMassOffset() end

---@public
---@param arg0 float
---@return void
function VehicleScript:setOffroadEfficiency(arg0) end

---@public
---@return float
function VehicleScript:getSuspensionCompression() end

---@public
---@return String
function VehicleScript:getName() end

---@public
---@return String
function VehicleScript:getEngineRPMType() end

---@public
---@return String
function VehicleScript:getFullName() end

---@public
---@param arg0 String
---@return VehicleScript.Area
function VehicleScript:getAreaById(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@return boolean
function VehicleScript:globMatch(arg0, arg1) end

---@public
---@param arg0 int
---@return void
function VehicleScript:setMechanicType(arg0) end

---@public
---@param arg0 ModelAttachment
---@return ModelAttachment
function VehicleScript:addAttachment(arg0) end

---@public
---@param arg0 int
---@return VehicleScript.Model
function VehicleScript:getModelByIndex(arg0) end

---@private
---@param arg0 String
---@param arg1 Vector2i
---@return void
function VehicleScript:LoadVector2i(arg0, arg1) end

---@public
---@param arg0 VehicleScript
---@param arg1 String
---@return void
function VehicleScript:copyWheelsFrom(arg0, arg1) end

---@public
---@return int
function VehicleScript:getEngineLoudness() end

---@private
---@param arg0 ScriptParser.Block
---@return HashMap|Unknown|Unknown
function VehicleScript:LoadLuaFunctions(arg0) end
