---@class ChannelCategory : zombie.radio.ChannelCategory
---@field public Undefined ChannelCategory
---@field public Radio ChannelCategory
---@field public Television ChannelCategory
---@field public Military ChannelCategory
---@field public Amateur ChannelCategory
---@field public Bandit ChannelCategory
---@field public Emergency ChannelCategory
---@field public Other ChannelCategory
ChannelCategory = {}

---@public
---@param arg0 String
---@return ChannelCategory
function ChannelCategory:valueOf(arg0) end

---@public
---@return ChannelCategory[]
function ChannelCategory:values() end
