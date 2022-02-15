--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class ISSetKeybindDialog : ISPanel
ISSetKeybindDialog = ISPanel:derive("ISSetKeybindDialog")

function ISSetKeybindDialog:createChildren()
	local btnWid = 200
	local btnHgt = 40
	local pad = 10
	local buttonsHgt = btnHgt * 3 + pad * 2

	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local actionName = getText("UI_optionscreen_binding_" .. self.keybindName):trim()
	local text = getText("UI_optionscreen_pressKeyToBind", actionName)
	local label = ISLabel:new(self.width / 2, 20, fontHgt, text:gsub("\\n", "\n"):gsub("\\\"", "\""), 1, 1, 1, 1, UIFont.Medium, true)
	label.center = true
	label:initialise()
	self:addChild(label)

	local labelBottom = label:getY() + fontHgt * 2
	local btnY = labelBottom + (self.height - labelBottom - buttonsHgt) / 2
	
	self.clear = ISButton:new((self:getWidth() - btnWid) / 2, btnY,
		btnWid, btnHgt, getText("UI_optionscreen_KeybindClear"), self, self.onClear)
	self.clear:initialise()
	self.clear:instantiate()
	self.clear.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.clear)

	self.default = ISButton:new((self:getWidth() - btnWid) / 2, self.clear:getBottom() + pad,
		btnWid, btnHgt, getText("UI_optionscreen_KeybindDefault"), self, self.onDefault)
	self.default:initialise()
	self.default:instantiate()
	self.default.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.default)

	self.cancel = ISButton:new((self:getWidth() - btnWid) / 2, self.default:getBottom() + pad,
		btnWid, btnHgt, getText("UI_Cancel"), self, self.onCancel)
	self.cancel:initialise()
	self.cancel:instantiate()
	self.cancel.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.cancel)
end

function ISSetKeybindDialog:destroy()
	MainOptions.setKeybindDialog = nil
	MainOptions.instance.cover:setVisible(false)
	self:setVisible(false)
	self:removeFromUIManager()
	GameKeyboard.setDoLuaKeyPressed(true)
end

function ISSetKeybindDialog:onCancel()
	self:destroy()
end

function ISSetKeybindDialog:onDefault()
	for i,v in ipairs(keyBinding) do
		if v.value == self.keybindName then
			if v.key == 0 then -- no default keybind
				self:onClear()
			else
				MainOptions.keyPressHandler(v.key)
			end
			break
		end
	end
end

function ISSetKeybindDialog:onClear()
	for i,v in ipairs(MainOptions.keyText) do
		if not v.value and (v.txt:getName() == self.keybindName) then
			v.keyCode = 0
			v.btn:setTitle(getKeyName(0))
			MainOptions.instance:onKeybindChanged(self.keybindName, 0)
			MainOptions.instance.gameOptions.changed = true
			self:destroy()
			break
		end
	end
end

function ISSetKeybindDialog:isKeyConsumed(key)
	return true
end

function ISSetKeybindDialog:onKeyRelease(key)
	MainOptions.keyPressHandler(key)
end

function ISSetKeybindDialog:new(keybindName)
	local width = 500
	local height = 300
	local x = (getCore():getScreenWidth() - width) / 2
	local y = (getCore():getScreenHeight() - height) / 2
	local o = ISPanel:new(x, y, width, height)
	o.backgroundColor.a = 0.9
	setmetatable(o, self)
	self.__index = self
	o.keybindName = keybindName
	o:setWantKeyEvents(true)
	return o
end

