---@class ChatMessage : zombie.chat.ChatMessage
---@field private chat ChatBase
---@field private datetime LocalDateTime
---@field private author String
---@field private text String
---@field private scramble boolean
---@field private customTag String
---@field private textColor Color
---@field private customColor boolean
---@field private overHeadSpeech boolean
---@field private showInChat boolean
---@field private fromDiscord boolean
---@field private serverAlert boolean
---@field private radioChannel int
---@field private _local boolean
---@field private shouldAttractZombies boolean
---@field private serverAuthor boolean
ChatMessage = {}

---@public
---@param arg0 boolean
---@return void
function ChatMessage:setServerAuthor(arg0) end

---@public
---@return boolean
function ChatMessage:isLocal() end

---@public
---@param arg0 boolean
---@return void
function ChatMessage:setShouldAttractZombies(arg0) end

---@public
---@return boolean
function ChatMessage:isFromDiscord() end

---@public
---@param arg0 boolean
---@return void
function ChatMessage:setServerAlert(arg0) end

---@public
---@return ChatBase
function ChatMessage:getChat() end

---@public
---@return boolean
function ChatMessage:isOverHeadSpeech() end

---@public
---@param arg0 boolean
---@return void
function ChatMessage:setShowInChat(arg0) end

---@public
---@param arg0 String
---@return void
function ChatMessage:setScrambledText(arg0) end

---@public
---@param arg0 ByteBufferWriter
---@return void
function ChatMessage:pack(arg0) end

---@public
---@return String
function ChatMessage:getDatetimeStr() end

---@public
---@return boolean
function ChatMessage:isShouldAttractZombies() end

---@public
---@return boolean
function ChatMessage:isShowInChat() end

---@public
---@return ChatMessage
function ChatMessage:clone() end

---@public
---@return String
function ChatMessage:getAuthor() end

---@public
---@return String
function ChatMessage:getTextWithPrefix() end

---@public
---@return String
function ChatMessage:getCustomTag() end

---@public
---@return int
function ChatMessage:getRadioChannel() end

---@public
---@param arg0 boolean
---@return void
function ChatMessage:setLocal(arg0) end

---@public
---@param arg0 String
---@return void
function ChatMessage:setCustomTag(arg0) end

---@public
---@param arg0 Color
---@return void
function ChatMessage:setTextColor(arg0) end

---@public
---@return int
function ChatMessage:getChatID() end

---@public
---@return Color
function ChatMessage:getTextColor() end

---@public
---@return boolean
function ChatMessage:isCustomColor() end

---@public
---@return boolean
function ChatMessage:isServerAuthor() end

---@public
---@return String
function ChatMessage:getTextWithReplacedParentheses() end

---@public
---@param arg0 boolean
---@return void
function ChatMessage:setOverHeadSpeech(arg0) end

---@public
---@return boolean
function ChatMessage:isServerAlert() end

---@public
---@param arg0 String
---@return void
function ChatMessage:setText(arg0) end

---@public
---@param arg0 LocalDateTime
---@return void
function ChatMessage:setDatetime(arg0) end

---@public
---@return String
function ChatMessage:toString() end

---@public
---@return boolean
function ChatMessage:isScramble() end

---@public
---@param arg0 String
---@return void
function ChatMessage:setAuthor(arg0) end

---@public
---@return void
function ChatMessage:makeFromDiscord() end

---@public
---@param arg0 int
---@return void
function ChatMessage:setRadioChannel(arg0) end

---@public
---@return boolean
function ChatMessage:isShowAuthor() end

---@public
---@return LocalDateTime
function ChatMessage:getDatetime() end

---@public
---@return String
function ChatMessage:getText() end
