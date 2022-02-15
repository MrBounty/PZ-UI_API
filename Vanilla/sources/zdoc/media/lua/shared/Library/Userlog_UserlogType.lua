---@class Userlog.UserlogType : zombie.network.Userlog.UserlogType
---@field public AdminLog Userlog.UserlogType
---@field public Kicked Userlog.UserlogType
---@field public Banned Userlog.UserlogType
---@field public DupeItem Userlog.UserlogType
---@field public LuaChecksum Userlog.UserlogType
---@field public WarningPoint Userlog.UserlogType
---@field private index int
Userlog_UserlogType = {}

---@public
---@return Userlog.UserlogType[]
function Userlog_UserlogType:values() end

---@public
---@param arg0 String
---@return Userlog.UserlogType
function Userlog_UserlogType:FromString(arg0) end

---@public
---@param arg0 String
---@return Userlog.UserlogType
function Userlog_UserlogType:valueOf(arg0) end

---@public
---@return int
function Userlog_UserlogType:index() end

---@public
---@param arg0 int
---@return Userlog.UserlogType
function Userlog_UserlogType:fromIndex(arg0) end
