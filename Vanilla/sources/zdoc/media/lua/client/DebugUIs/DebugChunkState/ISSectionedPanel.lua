--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

require "ISUI/ISPanel"

local FONT_HGT_SMALL = getTextManager():getFontHeight(UIFont.Small)
local SBAR_WID = 17

---@class ISSectionedPanel : ISPanel
ISSectionedPanel = ISPanel:derive("ISSectionedPanel")

-----

ISSectionedPanel_Section = ISPanel:derive("ISSectionedPanel_Section")
local Section = ISSectionedPanel_Section

function Section:createChildren()
	self.headerButton = ISButton:new(0, 0, self.width, FONT_HGT_SMALL, self.title, self, self.onHeaderClick)
	self.headerButton:setFont(UIFont.Small)
	self.headerButton.backgroundColor = {r=0.44, g=0.57, b=0.75, a=0.5}
	self.headerButton.backgroundColorMouseOver = {r=1.0, g=1.0, b=1.0, a=0.1}
	self.headerButton.borderColor = {r=1.0, g=1.0, b=1.0, a=0.3}
	self:addChild(self.headerButton)

	if self.panel then
		self.panel:setY(self.headerButton:getBottom())
		self.panel:setWidth(self.width)
		self.panel:setVisible(self.expanded)
		self:addChild(self.panel)
	end

	self:calculateHeights()
end

function Section:onHeaderClick()
	self.expanded = not self.expanded
	self:calculateHeights()
end

function Section:calculateHeights()
	local height = self.headerButton:getHeight()
	if self.panel then
		self.panel:setVisible(self.expanded)
		if self.expanded then
			height = height + self.panel:getHeight()
		end
	end
	self:setHeight(height)
end

function Section:clear()
	self.enabled = false
end

function Section:prerender()
	if self.panel and self.panelHeight ~= self.panel.height then
		self.panelHeight = self.panel.height
		self:calculateHeights()
	end
	local sx,sy,sx2,sy2 = 0,0,self.width,self.height
	if true then
		sx = self.javaObject:clampToParentX(self:getAbsoluteX() + sx) - self:getAbsoluteX()
		sx2 = self.javaObject:clampToParentX(self:getAbsoluteX() + sx2) - self:getAbsoluteX()
		sy = self.javaObject:clampToParentY(self:getAbsoluteY() + sy) - self:getAbsoluteY()
		sy2 = self.javaObject:clampToParentY(self:getAbsoluteY() + sy2) - self:getAbsoluteY()
	end
	self:setStencilRect(sx, sy, sx2 - sx, sy2 - sy)
end

function Section:render()
	self:clearStencilRect()
end

function Section:new(x, y, width, height, panel, title)
	local o = ISPanel.new(self, x, y, width, height)
	o.panel = panel
	o.title = title and title or "???"
	o.enabled = true
	o.expanded = true
	return o
end

-----

function ISSectionedPanel:addSection(panel, title)
	local sbarWid = self.vscroll and SBAR_WID or 0
	local section = Section:new(0, 0, self.width - sbarWid, 1, panel, title)
	self:addChild(section)
	if self:getScrollChildren() then
		section:setScrollWithParent(true)
		section:setScrollChildren(true) -- FIXME?
	end
	table.insert(self.sections, section)
end

function ISSectionedPanel:clear()
	for _,section in ipairs(self.sections) do
		section:clear()
	end
end

function ISSectionedPanel:prerender()
	local y = 0
	for _,section in ipairs(self.sections) do
		if section.enabled then
			section:setVisible(true)
			section:setY(y)
			y = y + section:getHeight()
		else
			section:setVisible(false)
		end
	end
	if self.maintainHeight then
		self:setHeight(y)
	elseif self:getScrollChildren() then
		self:setScrollHeight(y)
	end
	ISPanel.prerender(self)

	local sx,sy,sx2,sy2 = 0,0,self.width,self.height
	if self.parent and self.parent:getScrollChildren() then
		sx = self.javaObject:clampToParentX(self:getAbsoluteX() + sx) - self:getAbsoluteX()
		sx2 = self.javaObject:clampToParentX(self:getAbsoluteX() + sx2) - self:getAbsoluteX()
		sy = self.javaObject:clampToParentY(self:getAbsoluteY() + sy) - self:getAbsoluteY()
		sy2 = self.javaObject:clampToParentY(self:getAbsoluteY() + sy2) - self:getAbsoluteY()
	end
	self:setStencilRect(sx, sy, sx2 - sx, sy2 - sy)
end

function ISSectionedPanel:render()
	self:clearStencilRect()
	ISPanel.render(self)
end

function ISSectionedPanel:onMouseWheel(del)
	for _,section in ipairs(self.sections) do
		if section.panel:isMouseOver() and section.panel:isVScrollBarVisible() then
			return false
		end
	end

	self:setYScroll(self:getYScroll() - (del*40))
    return true
end

function ISSectionedPanel:new(x, y, width, height)
	local o = ISPanel.new(self, x, y, width, height)
	o.backgroundColor.a = 0.8
	o.sections = {}
	o.maintainHeight = true
	return o
end

