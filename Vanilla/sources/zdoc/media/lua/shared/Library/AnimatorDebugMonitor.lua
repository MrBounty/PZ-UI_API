---@class AnimatorDebugMonitor : zombie.core.skinnedmodel.advancedanimation.debug.AnimatorDebugMonitor
---@field private knownVariables ArrayList|Unknown
---@field private knownVarsDirty boolean
---@field private currentState String
---@field private monitoredLayers AnimatorDebugMonitor.MonitoredLayer[]
---@field private monitoredVariables HashMap|Unknown|Unknown
---@field private customVariables ArrayList|Unknown
---@field private logLines LinkedList|Unknown
---@field private logLineQueue Queue|Unknown
---@field private floatsListDirty boolean
---@field private hasFilterChanges boolean
---@field private hasLogUpdates boolean
---@field private logString String
---@field private maxLogSize int
---@field private maxOutputLines int
---@field private maxFloatCache int
---@field private floatsOut ArrayList|Unknown
---@field private selectedVariable AnimatorDebugMonitor.MonitoredVar
---@field private tickCount int
---@field private doTickStamps boolean
---@field private tickStampLength int
---@field private col_curstate Color
---@field private col_layer_nodename Color
---@field private col_layer_activated Color
---@field private col_layer_deactivated Color
---@field private col_track_activated Color
---@field private col_track_deactivated Color
---@field private col_node_activated Color
---@field private col_node_deactivated Color
---@field private col_var_activated Color
---@field private col_var_changed Color
---@field private col_var_deactivated Color
---@field private TAG_VAR String
---@field private TAG_LAYER String
---@field private TAG_NODE String
---@field private TAG_TRACK String
---@field private logFlags boolean[]
AnimatorDebugMonitor = {}

---@private
---@param arg0 String
---@return void
---@overload fun(arg0:String, arg1:Color)
---@overload fun(arg0:AnimatorDebugMonitor.LogType, arg1:String, arg2:Color)
function AnimatorDebugMonitor:queueLogLine(arg0) end

---@private
---@param arg0 String
---@param arg1 Color
---@return void
function AnimatorDebugMonitor:queueLogLine(arg0, arg1) end

---@private
---@param arg0 AnimatorDebugMonitor.LogType
---@param arg1 String
---@param arg2 Color
---@return void
function AnimatorDebugMonitor:queueLogLine(arg0, arg1, arg2) end

---@private
---@param arg0 String
---@param arg1 String
---@return void
function AnimatorDebugMonitor:updateVariable(arg0, arg1) end

---@private
---@param arg0 String
---@return void
---@overload fun(arg0:String, arg1:Color)
---@overload fun(arg0:String, arg1:Color, arg2:boolean)
---@overload fun(arg0:AnimatorDebugMonitor.LogType, arg1:String, arg2:Color)
---@overload fun(arg0:AnimatorDebugMonitor.LogType, arg1:String, arg2:Color, arg3:boolean)
function AnimatorDebugMonitor:addLogLine(arg0) end

---@private
---@param arg0 String
---@param arg1 Color
---@return void
function AnimatorDebugMonitor:addLogLine(arg0, arg1) end

---@private
---@param arg0 String
---@param arg1 Color
---@param arg2 boolean
---@return void
function AnimatorDebugMonitor:addLogLine(arg0, arg1, arg2) end

---@private
---@param arg0 AnimatorDebugMonitor.LogType
---@param arg1 String
---@param arg2 Color
---@return void
function AnimatorDebugMonitor:addLogLine(arg0, arg1, arg2) end

---@private
---@param arg0 AnimatorDebugMonitor.LogType
---@param arg1 String
---@param arg2 Color
---@param arg3 boolean
---@return void
function AnimatorDebugMonitor:addLogLine(arg0, arg1, arg2, arg3) end

---@public
---@param arg0 String
---@return void
function AnimatorDebugMonitor:registerVariable(arg0) end

---@public
---@return float
function AnimatorDebugMonitor:getSelectedVariableFloat() end

---@public
---@return List|Unknown
function AnimatorDebugMonitor:getKnownVariables() end

---@private
---@param arg0 AnimatorDebugMonitor.MonitoredLayer
---@param arg1 String
---@return void
function AnimatorDebugMonitor:updateActiveNode(arg0, arg1) end

---@private
---@return void
function AnimatorDebugMonitor:postUpdate() end

---@public
---@return boolean
function AnimatorDebugMonitor:IsDirtyFloatList() end

---@private
---@param arg0 AnimLayer[]
---@return void
function AnimatorDebugMonitor:ensureLayers(arg0) end

---@public
---@param arg0 String
---@return void
function AnimatorDebugMonitor:setSelectedVariable(arg0) end

---@private
---@return void
function AnimatorDebugMonitor:initCustomVars() end

---@public
---@param arg0 String
---@return void
function AnimatorDebugMonitor:addCustomVariable(arg0) end

---@private
---@param arg0 int
---@param arg1 AnimLayer
---@return void
function AnimatorDebugMonitor:updateLayer(arg0, arg1) end

---@public
---@return String
function AnimatorDebugMonitor:getSelectedVariable() end

---@private
---@param arg0 AnimatorDebugMonitor.MonitoredLayer
---@param arg1 String
---@param arg2 float
---@return void
function AnimatorDebugMonitor:updateAnimTrack(arg0, arg1, arg2) end

---@public
---@return ArrayList|Unknown
function AnimatorDebugMonitor:getSelectedVarFloatList() end

---@private
---@param arg0 String
---@return void
function AnimatorDebugMonitor:updateCurrentState(arg0) end

---@public
---@param arg0 boolean
---@return void
function AnimatorDebugMonitor:setDoTickStamps(arg0) end

---@public
---@return String
function AnimatorDebugMonitor:getSelectedVarMinFloat() end

---@public
---@return String
function AnimatorDebugMonitor:getLogString() end

---@public
---@return ArrayList|Unknown
function AnimatorDebugMonitor:getFloatNames() end

---@private
---@return void
function AnimatorDebugMonitor:buildLogString() end

---@private
---@return void
function AnimatorDebugMonitor:preUpdate() end

---@public
---@param arg0 IsoGameCharacter
---@param arg1 AnimLayer[]
---@return void
function AnimatorDebugMonitor:update(arg0, arg1) end

---@public
---@param arg0 String
---@return void
function AnimatorDebugMonitor:removeCustomVariable(arg0) end

---@public
---@param arg0 int
---@param arg1 boolean
---@return void
function AnimatorDebugMonitor:setFilter(arg0, arg1) end

---@public
---@return boolean
function AnimatorDebugMonitor:isDoTickStamps() end

---@public
---@return String
function AnimatorDebugMonitor:getSelectedVarMaxFloat() end

---@public
---@return boolean
function AnimatorDebugMonitor:isKnownVarsDirty() end

---@public
---@return boolean
function AnimatorDebugMonitor:IsDirty() end

---@public
---@param arg0 int
---@return boolean
function AnimatorDebugMonitor:getFilter(arg0) end

---@private
---@param arg0 AnimatorDebugMonitor.MonitorLogLine
---@return void
function AnimatorDebugMonitor:log(arg0) end

---@private
---@return void
function AnimatorDebugMonitor:processQueue() end
