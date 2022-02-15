--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class ISDuplicateKeybindDialog : ISPanel
ISDuplicateKeybindDialog = ISPanel:derive("ISDuplicateKeybindDialog")

function ISDuplicateKeybindDialog:createChildren()
	local btnWid = 200
	local btnHgt = 40
	local pad = 10
	local buttonsHgt = btnHgt * 3 + pad * 2

	local fontHgt = getTextManager():getFontFromEnum(UIFont.Medium):getLineHeight()
	local actionName = getText("UI_optionscreen_binding_" .. self.keybind2Name):trim()
	local text = getText("UI_optionscreen_keyAlreadyBinded", getKeyName(self.key), actionName, actionName)
	local label = ISLabel:new(self.width / 2, 20, fontHgt, text:gsub("\\n", "\n"):gsub("\\\"", "\""), 1, 1, 1, 1, UIFont.Medium, true)
	label.center = true
	label:initialise()
	self.label = label
	self:addChild(label)

	local labelBottom = label:getY() + fontHgt * 2
	local btnY = labelBottom + (self.height - labelBottom - buttonsHgt) / 2

	self.clear = ISButton:new((self:getWidth() - btnWid) / 2, btnY,
		btnWid, btnHgt, getText("UI_optionscreen_KeybindClear"), self, self.onClear)
	self.clear:initialise()
	self.clear:instantiate()
	self.clear.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.clear)

	self.keep = ISButton:new((self:getWidth() - btnWid) / 2, self.clear:getBottom() + pad,
		btnWid, btnHgt, getText("UI_optionscreen_KeybindKeep"), self, self.onKeep)
	self.keep:initialise()
	self.keep:instantiate()
	self.keep.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.keep)

	self.cancel = ISButton:new((self:getWidth() - btnWid) / 2, self.keep:getBottom() + pad,
		btnWid, btnHgt, getText("UI_Cancel"), self, self.onCancel)
	self.cancel:initialise()
	self.cancel:instantiate()
	self.cancel.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.cancel)
end

function ISDuplicateKeybindDialog:destroy()
	MainOptions.instance.cover:setVisible(false)
	self:setVisible(false)
	self:removeFromUIManager()
end

function ISDuplicateKeybindDialog:onCancel()
	self:destroy()
end

function ISDuplicateKeybindDialog:onKeep()
	-- Assign the pressed key to keybind #1.
	for i,v in ipairs(MainOptions.keyText) do
		if not v.value and (v.txt:getName() == self.keybindName) then
			self:assignKey(v, self.keybindName, self.key)
			break
		end
	end

	-- Don't change the key assigned to keybind #2.

	self:nextDuplicate()
end

function ISDuplicateKeybindDialog:onClear()
	-- Assign the pressed key to keybind #1.
	for i,v in ipairs(MainOptions.keyText) do
		if not v.value and (v.txt:getName() == self.keybindName) then
			self:assignKey(v, self.keybindName, self.key)
			break
		end
	end
	
	-- Clear the key assigned to keybind #2.
	for i,v in ipairs(MainOptions.keyText) do
		if not v.value and (v.txt:getName() == self.keybind2Name) then
			self:assignKey(v, self.keybind2Name, 0)
			break
		end
	end

	self:nextDuplicate()
end

function ISDuplicateKeybindDialog:assignKey(keyText, keybind, key)
	keyText.keyCode = key
	keyText.btn:setTitle(getKeyName(key))
	MainOptions.instance:onKeybindChanged(keybind, key)
	MainOptions.instance.gameOptions.changed = true
end

function ISDuplicateKeybindDialog:nextDuplicate()
	local current = nil
	local duplicate = nil
	for i,v in ipairs(MainOptions.keyText) do
		if v.value then
			-- Skip [Category]
		elseif v.txt:getName() == self.keybindName then
			-- Skip current keybind we're assigning
		elseif v.txt:getName() == self.keybind2Name then
			-- Skip keybind we already handled
			current = i
		elseif current and (getKeyName(self.key) == v.btn:getTitle()) then
			-- Found a duplicate keybind after the current one
			duplicate = v.txt:getName()
			break
		end
	end
	if duplicate then
		self.keybind2Name = duplicate
		local actionName = getText("UI_optionscreen_binding_" .. self.keybind2Name):trim()
		local text = getText("UI_optionscreen_keyAlreadyBinded", getKeyName(self.key), actionName, actionName)
		self.label.name = text:gsub("\\n", "\n"):gsub("\\\"", "\"")
	else
		self:destroy()
	end
end

function ISDuplicateKeybindDialog:new(key, keybindName, keybind2Name)
	local width = 500
	local height = 300
	local x = (getCore():getScreenWidth() - width) / 2
	local y = (getCore():getScreenHeight() - height) / 2
	local o = ISPanel:new(x, y, width, height)
	o.backgroundColor.a = 0.9
	setmetatable(o, self)
	self.__index = self
	o.key = key
	o.keybindName = keybindName
	o.keybind2Name = keybind2Name
	return o
end

