---@class UIServerToolbox : zombie.ui.UIServerToolbox
---@field public instance UIServerToolbox
---@field ScrollBarV ScrollBar
---@field OutputLog UITextBox2
---@field private incomingConnections ArrayList|Unknown
---@field buttonAccept DialogButton
---@field buttonReject DialogButton
---@field private externalAddress String
---@field private steamID String
---@field public autoAccept boolean
UIServerToolbox = {}

---@public
---@return void
function UIServerToolbox:shutdown() end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@return void
function UIServerToolbox:Selected(arg0, arg1, arg2) end

---@public
---@param arg0 String
---@param arg1 String
---@param arg2 String
---@return void
function UIServerToolbox:OnCoopServerMessage(arg0, arg1, arg2) end

---@param arg0 String
---@return void
function UIServerToolbox:PrintLine(arg0) end

---@public
---@return void
function UIServerToolbox:render() end

---@public
---@return void
function UIServerToolbox:update() end

---@public
---@param arg0 String
---@param arg1 String
---@return void
function UIServerToolbox:ModalClick(arg0, arg1) end

---@public
---@param arg0 String
---@param arg1 int
---@param arg2 int
---@return void
function UIServerToolbox:DoubleClick(arg0, arg1, arg2) end

---@return void
function UIServerToolbox:UpdateViewPos() end
