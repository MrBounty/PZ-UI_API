---@class UITextBox2 : zombie.ui.UITextBox2
---@field public ConsoleHasFocus boolean
---@field public Lines Stack|String
---@field public Frame UINineGrid
---@field public Text String
---@field public Centred boolean
---@field public StandardFrameColour Color
---@field public TextEntryFrameColour Color
---@field public TextEntryCursorColour Color
---@field public TextEntryCursorColour2 Color
---@field public NuetralColour Color
---@field public NuetralColour2 Color
---@field public BadColour Color
---@field public GoodColour Color
---@field public DoingTextEntry boolean
---@field public TextEntryCursorPos int
---@field public TextEntryMaxLength int
---@field public IsEditable boolean
---@field public IsSelectable boolean
---@field public CursorLine int
---@field public multipleLine boolean
---@field public TextOffsetOfLineStart TIntArrayList
---@field public ToSelectionIndex int
---@field public internalText String
---@field public maskChr String
---@field public bMask boolean
---@field public ignoreFirst boolean
---@field font UIFont
---@field HighlightLines int[]
---@field HasFrame boolean
---@field NumVisibleLines int
---@field TopLineIndex int
---@field BlinkFramesOn int
---@field BlinkFramesOff int
---@field BlinkFrame float
---@field BlinkState boolean
---@field private textColor ColorInfo
---@field private EdgeSize int
---@field private SelectingRange boolean
---@field private maxTextLength int
---@field private forceUpperCase boolean
---@field private XOffset int
---@field private maxLines int
---@field private onlyNumbers boolean
---@field private clearButtonTexture Texture
---@field private bClearButton boolean
---@field private clearButtonTransition UITransition
---@field public bAlwaysPaginate boolean
---@field public bTextChanged boolean
---@field private paginateWidth int
---@field private paginateFont UIFont
UITextBox2 = {}

---@public
---@return int
function UITextBox2:getMaxTextLength() end

---@public
---@param b boolean
---@return void
function UITextBox2:setMasked(b) end

---Overrides:
---
---onresize in class UIElement
---@public
---@return void
function UITextBox2:onresize() end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function UITextBox2:onMouseDown(arg0, arg1) end

---@public
---@return String
function UITextBox2:getText() end

---@public
---@return void
function UITextBox2:onTextChange() end

---@public
---@return void
function UITextBox2:onPressUp() end

---@private
---@return void
function UITextBox2:keepCursorVisible() end

---@public
---@param hasFrame boolean
---@return void
function UITextBox2:setHasFrame(hasFrame) end

---@public
---@param text String
---@return void
function UITextBox2:SetText(text) end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function UITextBox2:onMouseMoveOutside(arg0, arg1) end

---@public
---@return void
function UITextBox2:onCommandEntered() end

---Overrides:
---
---render in class UIElement
---@public
---@return void
function UITextBox2:render() end

---@public
---@return void
function UITextBox2:onPressDown() end

---@public
---@param onlyNumbers boolean
---@return void
function UITextBox2:setOnlyNumbers(onlyNumbers) end

---@public
---@return void
function UITextBox2:selectAll() end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function UITextBox2:onMouseMove(arg0, arg1) end

---@public
---@return void
function UITextBox2:ClearHighlights() end

---@public
---@return boolean
function UITextBox2:getForceUpperCase() end

---@private
---@param arg0 int
---@return int
function UITextBox2:getCursorPosFromX(arg0) end

---@public
---@param arg0 boolean
---@return void
function UITextBox2:setClearButton(arg0) end

---@public
---@param forceUpperCase boolean
---@return void
function UITextBox2:setForceUpperCase(forceUpperCase) end

---@public
---@param key int
---@return void
function UITextBox2:onOtherKey(key) end

---@public
---@return boolean
function UITextBox2:isOnlyNumbers() end

---@public
---@return void
function UITextBox2:unfocus() end

---@public
---@param multiple boolean
---@return void
function UITextBox2:setMultipleLine(multiple) end

---@public
---@return int
function UITextBox2:getInset() end

---@public
---@return void
function UITextBox2:resetBlink() end

---@public
---@param maxLines int
---@return void
function UITextBox2:setMaxLines(maxLines) end

---@public
---@return int
function UITextBox2:getMaxLines() end

---@private
---@return void
function UITextBox2:Paginate() end

---@public
---@param arg0 boolean
---@return void
function UITextBox2:setSelectable(arg0) end

---@public
---@return String
function UITextBox2:getInternalText() end

---@public
---@param arg0 double
---@param arg1 double
---@return Boolean
function UITextBox2:onMouseUp(arg0, arg1) end

---@public
---@param arg0 double
---@param arg1 double
---@return void
function UITextBox2:onMouseUpOutside(arg0, arg1) end

---@public
---@param line int
---@return void
function UITextBox2:setCursorLine(line) end

---Overrides:
---
---update in class UIElement
---@public
---@return void
function UITextBox2:update() end

---@public
---@param textOffset int
---@return int
function UITextBox2:toDisplayLine(textOffset) end

---@public
---@param arg0 ColorInfo
---@return void
function UITextBox2:setTextColor(arg0) end

---@public
---@param arg0 float
---@return void
function UITextBox2:setFrameAlpha(arg0) end

---@public
---@return void
function UITextBox2:updateText() end

---@public
---@param maxtextLength int
---@return void
function UITextBox2:setMaxTextLength(maxtextLength) end

---@public
---@return void
function UITextBox2:clearInput() end

---@public
---@return void
function UITextBox2:focus() end

---@public
---@return void
function UITextBox2:ignoreFirstInput() end

---@public
---@return float
function UITextBox2:getFrameAlpha() end

---@public
---@return boolean
function UITextBox2:isFocused() end

---@public
---@param b boolean
---@return void
function UITextBox2:setEditable(b) end
