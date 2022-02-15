---@class IsoPuddles : zombie.iso.IsoPuddles
---@field public Effect Shader
---@field private PuddlesWindAngle float
---@field private PuddlesWindIntensity float
---@field private PuddlesTime float
---@field private PuddlesParamWindINT Vector2f
---@field public leakingPuddlesInTheRoom boolean
---@field private texHM Texture
---@field private apiId int
---@field private instance IsoPuddles
---@field private isShaderEnable boolean
---@field BYTES_PER_FLOAT int
---@field FLOATS_PER_VERTEX int
---@field BYTES_PER_VERTEX int
---@field VERTICES_PER_SQUARE int
---@field public VBOs SharedVertexBufferObjects
---@field private renderData IsoPuddles.RenderData[][]
---@field private shaderOffset Vector4f
---@field private shaderOffsetMain Vector4f
---@field private floatBuffer FloatBuffer
---@field public BOOL_MAX int
---@field public FLOAT_RAIN int
---@field public FLOAT_WETGROUND int
---@field public FLOAT_MUDDYPUDDLES int
---@field public FLOAT_PUDDLESSIZE int
---@field public FLOAT_RAININTENSITY int
---@field public FLOAT_MAX int
---@field private rain IsoPuddles.PuddlesFloat
---@field private wetGround IsoPuddles.PuddlesFloat
---@field private muddyPuddles IsoPuddles.PuddlesFloat
---@field private puddlesSize IsoPuddles.PuddlesFloat
---@field private rainIntensity IsoPuddles.PuddlesFloat
---@field private climateFloats IsoPuddles.PuddlesFloat[]
IsoPuddles = {}

---@public
---@return Vector4f
function IsoPuddles:getShaderOffset() end

---@public
---@param arg0 int
---@return IsoPuddles.PuddlesFloat
function IsoPuddles:getPuddlesFloat(arg0) end

---@public
---@return float
function IsoPuddles:getRainIntensity() end

---@public
---@return void
function IsoPuddles:applyPuddlesQuality() end

---@public
---@return ITexture
function IsoPuddles:getHMTexture() end

---@public
---@return void
function IsoPuddles:puddlesProjection() end

---@public
---@param arg0 int
---@return FloatBuffer
function IsoPuddles:getPuddlesParams(arg0) end

---@public
---@return Vector4f
function IsoPuddles:getShaderOffsetMain() end

---@public
---@param arg0 int
---@return void
function IsoPuddles:puddlesGeometry(arg0) end

---@public
---@return int
function IsoPuddles:getBoolMax() end

---@public
---@return IsoPuddles
function IsoPuddles:getInstance() end

---@public
---@return float
function IsoPuddles:getPuddlesSize() end

---@public
---@param arg0 ClimateManager
---@return void
function IsoPuddles:update(arg0) end

---@public
---@return int
function IsoPuddles:getFloatMax() end

---@public
---@param arg0 ArrayList|Unknown
---@param arg1 int
---@return void
function IsoPuddles:render(arg0, arg1) end

---@public
---@return boolean
function IsoPuddles:getShaderEnable() end

---@private
---@param arg0 int
---@param arg1 String
---@return IsoPuddles.PuddlesFloat
function IsoPuddles:initClimateFloat(arg0, arg1) end

---@private
---@return void
function IsoPuddles:setup() end

---@private
---@param arg0 int
---@param arg1 int
---@return int
function IsoPuddles:renderSome(arg0, arg1) end

---@public
---@return float
function IsoPuddles:getShaderTime() end
