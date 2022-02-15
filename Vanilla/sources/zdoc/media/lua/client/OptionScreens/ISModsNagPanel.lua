--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISModalRichText"

---@class ISModsNagPanel : ISPanelJoypad
ISModsNagPanel = ISPanelJoypad:derive("ISModsNagPanel")

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local FONT_HGT_TITLE = getTextManager():getFontHeight(UIFont.Title)

function ISModsNagPanel:createChildren()
	local btnWid = 100
	local btnHgt = math.max(25, FONT_HGT_SMALL + 3 * 2)
	local padY = 10

	self.textureX = 10
	self.textureY = 10 + FONT_HGT_TITLE + 10
	self.textureW = self.texture:getWidth()
	self.textureH = self.texture:getHeight()

	local x = self.textureX + self.textureW
	local y = self.textureY
	self.richText = ISRichTextPanel:new(x, y, self.width - x, self.height - padY - btnHgt - padY - y)
	self.richText.background = false
	self.richText.autosetheight = false
	self.richText.clip = true
	self.richText.marginRight = self.richText.marginLeft
	self:addChild(self.richText)
	self.richText:addScrollBars()

	self.richText:setText(getText("UI_ModsNagPanel_Text"))
	self.richText:paginate()

	self.ok = ISButton:new((self:getWidth() / 2) - btnWid / 2, self:getHeight() - padY - btnHgt, btnWid, btnHgt, getText("UI_Ok"), self, self.onOK)
	self.ok.anchorTop = false
	self.ok.anchorBottom = true
	self.ok:initialise()
	self.ok:instantiate()
--	self.ok.borderColor = {r=1, g=1, b=1, a=0.1}
	self:addChild(self.ok)
end

function ISModsNagPanel:render()
	ISPanelJoypad.render(self)
	self:drawTextCentre(getText("UI_ModsNagPanel_Title"), self.width / 2, 10, 1, 1, 1, 1, UIFont.Title);
	self:drawTextureScaledAspect(self.texture, self.textureX, self.textureY, self.textureW, self.textureH, 1, 1, 1, 1)
end

function ISModsNagPanel:onGainJoypadFocus(joypadData)
	ISPanelJoypad.onGainJoypadFocus(self, joypadData)
	self:setISButtonForA(self.ok)
end

function ISModsNagPanel:onOK(button, x, y)
	self:setVisible(false)
	self:removeFromUIManager()
	ModSelector.instance:setVisible(true, self.joyfocus)
end

function ISModsNagPanel:new(x, y, width, height)
	local o = ISPanelJoypad.new(self, x, y, width, height)
	o.backgroundColor.a = 0.9
	o.texture = getTexture("spiffoWarning.png")
	return o
end
