---@class ChatBase : zombie.chat.ChatBase
---@field private ID_NOT_SET int
---@field private id int
---@field private titleID String
---@field private type ChatType
---@field private settings ChatSettings
---@field private customSettings boolean
---@field private chatTab ChatTab
---@field private translatedTitle String
---@field protected members ArrayList|Unknown
---@field private justAddedMembers ArrayList|Unknown
---@field private justRemovedMembers ArrayList|Unknown
---@field protected messages ArrayList|Unknown
---@field private serverConnection UdpConnection
---@field private mode ChatMode
---@field private chatOwner IsoPlayer
---@field private memberLock Lock
ChatBase = {}

---@public
---@param arg0 ChatMessage
---@return void
---@overload fun(arg0:ServerChatMessage)
function ChatBase:sendMessageToChatMembers(arg0) end

---@public
---@param arg0 ServerChatMessage
---@return void
function ChatBase:sendMessageToChatMembers(arg0) end

---@public
---@return int
function ChatBase:getID() end

---@protected
---@return boolean
function ChatBase:isEqualizeLineHeights() end

---@protected
---@return String
function ChatBase:getFontSizeTag() end

---@protected
---@return boolean
function ChatBase:isShowTimestamp() end

---@protected
---@return String
function ChatBase:getChatSettingsTags() end

---@public
---@param arg0 ChatMessage
---@return String
function ChatBase:getMessageTextWithPrefix(arg0) end

---@public
---@param arg0 ChatMessage
---@return void
function ChatBase:sendToServer(arg0) end

---@protected
---@return boolean
function ChatBase:isShowAuthor() end

---@protected
---@return boolean
function ChatBase:isShowTitle() end

---@public
---@return boolean
function ChatBase:isSendingToRadio() end

---@private
---@param arg0 Short
---@return boolean
function ChatBase:hasMember(arg0) end

---@public
---@param arg0 ChatMessage
---@return String
function ChatBase:getMessagePrefix(arg0) end

---@protected
---@return IsoPlayer
function ChatBase:getChatOwner() end

---@public
---@return float
function ChatBase:getZombieAttractionRange() end

---@public
---@param arg0 Short
---@return void
function ChatBase:removeMember(arg0) end

---@public
---@param arg0 ArrayList|Unknown
---@return void
function ChatBase:syncMembersByUsernames(arg0) end

---@public
---@param arg0 Short
---@return void
function ChatBase:leaveMember(arg0) end

---@public
---@return Color
function ChatBase:getColor() end

---@public
---@return ArrayList|Unknown
function ChatBase:getJustRemovedMembers() end

---@protected
---@return String
function ChatBase:getFontSize() end

---@protected
---@return boolean
function ChatBase:isCustomSettings() end

---@protected
---@return String
---@overload fun(arg0:Color)
function ChatBase:getColorTag() end

---@protected
---@param arg0 Color
---@return String
function ChatBase:getColorTag(arg0) end

---@public
---@param arg0 short
---@return void
function ChatBase:addMember(arg0) end

---@public
---@return String
function ChatBase:getTitleID() end

---@public
---@param arg0 short
---@return void
---@overload fun(arg0:UdpConnection)
function ChatBase:sendPlayerLeaveChatPacket(arg0) end

---@public
---@param arg0 UdpConnection
---@return void
function ChatBase:sendPlayerLeaveChatPacket(arg0) end

---@public
---@return ChatType
function ChatBase:getType() end

---@public
---@param arg0 UdpConnection
---@return void
function ChatBase:sendPlayerJoinChatPacket(arg0) end

---@protected
---@return String
function ChatBase:getChatOwnerName() end

---@protected
---@return String
function ChatBase:getTitle() end

---@public
---@param arg0 ChatMessage
---@return void
---@overload fun(arg0:String, arg1:String)
function ChatBase:showMessage(arg0) end

---@public
---@param arg0 String
---@param arg1 String
---@return void
function ChatBase:showMessage(arg0, arg1) end

---@public
---@return ChatMode
function ChatBase:getMode() end

---@public
---@param arg0 ChatSettings
---@return void
function ChatBase:setSettings(arg0) end

---@public
---@return short
function ChatBase:getTabID() end

---@public
---@param arg0 ByteBuffer
---@return ChatMessage
function ChatBase:unpackMessage(arg0) end

---@protected
---@return boolean
function ChatBase:isAllowChatIcons() end

---@public
---@param arg0 String
---@return ChatMessage
---@overload fun(arg0:String, arg1:String)
function ChatBase:createMessage(arg0) end

---@private
---@param arg0 String
---@param arg1 String
---@return ChatMessage
function ChatBase:createMessage(arg0, arg1) end

---@public
---@param arg0 boolean
---@return void
function ChatBase:setShowTitle(arg0) end

---@public
---@return boolean
function ChatBase:isEnabled() end

---@protected
---@return boolean
function ChatBase:isAllowColors() end

---@public
---@return ArrayList|Unknown
function ChatBase:getJustAddedMembers() end

---@public
---@param arg0 ByteBufferWriter
---@param arg1 ChatMessage
---@return void
function ChatBase:packMessage(arg0, arg1) end

---@public
---@param arg0 String
---@return ServerChatMessage
function ChatBase:createServerMessage(arg0) end

---@public
---@return void
function ChatBase:close() end

---@public
---@param arg0 String
---@return void
function ChatBase:setFontSize(arg0) end

---@protected
---@return boolean
function ChatBase:isAllowBBcode() end

---@public
---@param arg0 UdpConnection
---@param arg1 ChatMessage
---@return void
---@overload fun(arg0:short, arg1:ChatMessage)
function ChatBase:sendMessageToPlayer(arg0, arg1) end

---@public
---@param arg0 short
---@param arg1 ChatMessage
---@return void
function ChatBase:sendMessageToPlayer(arg0, arg1) end

---@private
---@param arg0 UdpConnection
---@param arg1 ChatMessage
---@return void
function ChatBase:sendChatMessageFromPlayer(arg0, arg1) end

---@private
---@param arg0 UdpConnection
---@param arg1 ChatMessage
---@return void
function ChatBase:sendChatMessageToPlayer(arg0, arg1) end

---@private
---@param arg0 ArrayList|Unknown
---@return void
function ChatBase:syncMembers(arg0) end

---@public
---@param arg0 boolean
---@return void
function ChatBase:setShowTimestamp(arg0) end

---@protected
---@return boolean
function ChatBase:isAllowImages() end

---@public
---@return float
function ChatBase:getRange() end

---@protected
---@param arg0 ByteBufferWriter
---@return void
function ChatBase:packChat(arg0) end

---@protected
---@return boolean
function ChatBase:hasChatTab() end

---@protected
---@return boolean
function ChatBase:isAllowFonts() end
