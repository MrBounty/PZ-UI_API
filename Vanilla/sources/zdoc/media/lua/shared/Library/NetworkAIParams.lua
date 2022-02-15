---@class NetworkAIParams : zombie.network.NetworkAIParams
---@field public ZOMBIE_UPDATE_INFO_BUNCH_RATE_MS int
---@field public CHARACTER_UPDATE_RATE_MS int
---@field public CHARACTER_EXTRAPOLATION_UPDATE_INTERVAL_MS int
---@field public ZOMBIE_ANTICIPATORY_UPDATE_MULTIPLIER float
---@field public ZOMBIE_REMOVE_INTERVAL_MS int
---@field public ZOMBIE_MAX_UPDATE_INTERVAL_MS int
---@field public ZOMBIE_MIN_UPDATE_INTERVAL_MS int
---@field public CHARACTER_PREDICTION_INTERVAL_MS int
---@field public ZOMBIE_TELEPORT_PLAYER int
---@field public ZOMBIE_TELEPORT_DISTANCE_SQ int
---@field public VEHICLE_SPEED_CAP int
NetworkAIParams = {}

---@public
---@return void
function NetworkAIParams:Init() end
