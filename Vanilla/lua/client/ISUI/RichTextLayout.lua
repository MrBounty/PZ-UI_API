require "ISBaseObject"

ISRichTextLayout = ISBaseObject:derive("ISRichTextLayout")

ISRichTextLayout.drawMargins = false

function ISRichTextLayout:initialise()
	ISBaseObject.initialise(self)
end

function ISRichTextLayout:processCommand(command, x, y, lineImageHeight, lineHeight)
	if command == "LINE" then
		x = 0
		lineImageHeight = 0
		y = y + lineHeight
	end
	if command == "BR" then
		x = 0
		lineImageHeight = 0
		y = y + lineHeight + lineHeight
	end
	if command == "H1" then
		self.orient[self.currentLine] = "centre"
		self.rgb[self.currentLine] = {}
		self.rgb[self.currentLine].r = 1
		self.rgb[self.currentLine].g = 1
		self.rgb[self.currentLine].b = 1
		self.font = UIFont.Large
		self.fonts[self.currentLine] = self.font
	end
	if command == "H2" then
		self.orient[self.currentLine] = "left"
		self.rgb[self.currentLine] = {}
		self.rgb[self.currentLine].r = 0.8
		self.rgb[self.currentLine].g = 0.8
		self.rgb[self.currentLine].b = 0.8
		self.font = UIFont.Medium
		self.fonts[self.currentLine] = self.font
	end
	if command == "TEXT" then
		self.orient[self.currentLine] = "left"
		self.rgb[self.currentLine] = {}
		self.rgb[self.currentLine].r = 0.7
		self.rgb[self.currentLine].g = 0.7
		self.rgb[self.currentLine].b = 0.7
		self.font = self.defaultFont
		self.fonts[self.currentLine] = self.font
	end
	if command == "CENTRE" then
		self.orient[self.currentLine] = "centre"
	end

	if command == "LEFT" then
		self.orient[self.currentLine] = "left"
	end

	if command == "RIGHT" then
		self.orient[self.currentLine] = "right"
	end
	if string.find(command, "RGB:") then
		local rgb = string.split(string.sub(command, 5, string.len(command)), ",")
		self.rgb[self.currentLine] = {}
		self.rgb[self.currentLine].r = tonumber(rgb[1])
		self.rgb[self.currentLine].g = tonumber(rgb[2])
		self.rgb[self.currentLine].b = tonumber(rgb[3])
	end
	if string.find(command, "RED") then
		self.rgb[self.currentLine] = {}
		self.rgb[self.currentLine].r = 1
		self.rgb[self.currentLine].g = 0
		self.rgb[self.currentLine].b = 0
	end
	if string.find(command, "ORANGE") then
		self.rgb[self.currentLine] = {}
		self.rgb[self.currentLine].r = 0.9
		self.rgb[self.currentLine].g = 0.3
		self.rgb[self.currentLine].b = 0
	end
	if string.find(command, "GREEN") then
		self.rgb[self.currentLine] = {}
		self.rgb[self.currentLine].r = 0
		self.rgb[self.currentLine].g = 1
		self.rgb[self.currentLine].b = 0
	end
	if string.find(command, "SIZE:") then

		local size = string.sub(command, 6)
		if(size == "small") then
			self.font = UIFont.Small
		end
		if(size == "medium") then
			self.font = UIFont.Medium
		end
		if(size == "large") then
			self.font = UIFont.Large
		end
		self.fonts[self.currentLine] = self.font
	end

	if string.find(command, "IMAGE:") ~= nil then
		local w = 0
		local h = 0
		if string.find(command, ",") ~= nil then
			local vs = string.split(command, ",")

			command = string.trim(vs[1])
			w = tonumber(string.trim(vs[2]))
			h = tonumber(string.trim(vs[3]))

		end
		self.images[self.imageCount] = getTexture(string.sub(command, 7))
		if(w==0) then
			w = self.images[self.imageCount]:getWidth()
			h = self.images[self.imageCount]:getHeight()
		end
		if(x + w >= self.width - (self.marginLeft + self.marginRight)) then
			x = 0
			y = y +  lineHeight
		end

		if(lineImageHeight < (h / 2) + 8) then
			lineImageHeight = (h / 2) + 8
		end

		if self.images[self.imageCount] == nil then
			--print("Could not find texture")
		end
		self.imageX[self.imageCount] = x+2
		self.imageY[self.imageCount] = y
		self.imageW[self.imageCount] = w
		self.imageH[self.imageCount] = h
		self.imageCount = self.imageCount + 1
		x = x + w + 7

		local newY = math.max(y + (h / 2) - 7, y)

		for c,v in ipairs(self.lines) do
			if self.lineY[c] == y then
				self.lineY[c] = newY
			end
		end

		for c,v in ipairs(self.imageY) do
			if self.imageY[c] == y then
				self.imageY[c] = newY;
			end
		end

		y = newY
	end

	if string.find(command, "IMAGECENTRE:") ~= nil then
		local w = 0
		local h = 0
		if string.find(command, ",") ~= nil then
			local vs = string.split(command, ",")

			command = string.trim(vs[1])
			w = tonumber(string.trim(vs[2]))
			h = tonumber(string.trim(vs[3]))

		end
		self.images[self.imageCount] = getTexture(string.sub(command, 13))
		if(w==0) then
			w = self.images[self.imageCount]:getWidth()
			h = self.images[self.imageCount]:getHeight()
		end
		if(x + w >= self.width - (self.marginLeft + self.marginRight)) then
			x = 0
			y = y +  lineHeight
		end

		if(lineImageHeight < (h / 2) + 8) then
			lineImageHeight = (h / 2) + 16
		end

		if self.images[self.imageCount] == nil then
			--print("Could not find texture")
		end
		local mx = (self.width / 2) - self.marginLeft
		self.imageX[self.imageCount] = mx - (w/2)
		self.imageY[self.imageCount] = y
		self.imageW[self.imageCount] = w
		self.imageH[self.imageCount] = h
		self.imageCount = self.imageCount + 1
		x = x + w + 7

		for c,v in ipairs(self.lines) do
			if self.lineY[c] == y then
				self.lineY[c] = self.lineY[c] + (h / 2)
			end
		end

		y = y + (h / 2)
	end

	if string.find(command, "INDENT:") then
		self.indent = tonumber(string.sub(command, 8))
	end

	if string.find(command, "SETX:") then
		x = tonumber(string.sub(command, 6))
	end

	return x, y, lineImageHeight
end

function ISRichTextLayout:paginate()
	local lines = 1;
	self.textDirty = false;
	self.imageCount = 1;
	self.font = self.defaultFont;
	self.fonts = {};
	self.images = {}
	self.imageX = {}
	self.imageY = {}
	self.rgb = {};
	self.orient = {}
	self.indent = 0

	self.imageW = {}
	self.imageH = {}

	self.lineY = {}
	self.lineX = {}
	self.lines = {}
	local bDone = false;
	local leftText = self.text..' ';
	local cur = 0;
	local y = 0;
	local x = 0;
	local lineImageHeight = 0;
	leftText = leftText:gsub("\n", " <LINE> ")
	if self.maxLines > 0 then
		local lines = leftText:split("<LINE>")
		for i=1,(#lines - self.maxLines) do
			table.remove(lines,1)
		end
		leftText = ' '
		for k,v in ipairs(lines) do
			leftText = leftText..v.." <LINE> "
		end
	end
	local maxLineWidth = self.maxLineWidth or (self.width - self.marginRight - self.marginLeft)
	-- Always go through at least once.
	while not bDone do
		cur = string.find(leftText, " ", cur+1);
		if cur ~= nil then
			local token = string.sub(leftText, 0, cur);
			if string.find(token, "<") and string.find(token, ">") then -- handle missing ' ' after '>'
				cur = string.find(token, ">") + 1;
				token = string.sub(leftText, 0, cur - 1);
			end
			leftText = string.sub(leftText, cur);
			cur = 1
			if string.find(token, "<") and string.find(token, ">") then
				if not self.lines[lines] then
					self.lines[lines] = ''
					self.lineX[lines] = x
					self.lineY[lines] = y
				end
				lines = lines + 1
				local st = string.find(token, "<");
				local en = string.find(token, ">");
				local escSeq = string.sub(token, st+1, en-1);
				local lineHeight = getTextManager():getFontFromEnum(self.font):getLineHeight();
				if lineHeight < 10 then
					lineHeight = 10;
				end
				if lineHeight < lineImageHeight then
					lineHeight = lineImageHeight;
				end
				self.currentLine = lines;
				x, y, lineImageHeight = self:processCommand(escSeq, x, y, lineImageHeight, lineHeight);
			else
				if token:contains("&lt;") then
					token = token:gsub("&lt;", "<")
				end
				if token:contains("&gt;") then
					token = token:gsub("&gt;", ">")
				end
				local chunkText = self.lines[lines] or ''
				local chunkX = self.lineX[lines] or x
				if chunkText == '' then
					chunkText = string.trim(token)
				else
					chunkText = chunkText..' '..string.trim(token)
				end
				local pixLen = getTextManager():MeasureStringX(self.font, chunkText);
				if chunkX + pixLen > maxLineWidth then
					if self.lines[lines] and self.lines[lines] ~= '' then
						lines = lines + 1;
					end
					local lineHeight = getTextManager():getFontFromEnum(self.font):getLineHeight();
					if lineHeight < lineImageHeight then
						lineHeight = lineImageHeight;
					end
					lineImageHeight = 0;
					y = y + lineHeight;
					x = 0;
					self.lines[lines] = string.trim(token)
					if self.lines[lines] ~= "" then
						x = self.indent
					end
					self.lineX[lines] = x
					self.lineY[lines] = y
					x = x + getTextManager():MeasureStringX(self.font, self.lines[lines])
				else
					if not self.lines[lines] then
						self.lines[lines] = ''
						self.lineX[lines] = x
						self.lineY[lines] = y
					end
					self.lines[lines] = chunkText
					if self.lineX[lines] == 0 and self.lines[lines] ~= "" then
						self.lineX[lines] = self.indent
					end
					x = self.lineX[lines] + pixLen
				end
			end
        else
			if string.trim(leftText) ~= '' then
				local str = leftText
				if str:contains("&lt;") then
					str = str:gsub("&lt;", "<")
				end
				if str:contains("&gt;") then
					str = str:gsub("&gt;", ">")
				end
				self.lines[lines] = string.trim(str);
				if x == 0 and self.lines[lines] ~= "" then
					x = self.indent
				end
				self.lineX[lines] = x;
				self.lineY[lines] = y;
				local lineHeight = getTextManager():getFontFromEnum(self.font):getLineHeight();
				y = y + lineHeight
			elseif self.lines[lines] and self.lines[lines] ~= '' then
				local lineHeight = getTextManager():getFontFromEnum(self.font):getLineHeight();
				if lineHeight < lineImageHeight then
					lineHeight = lineImageHeight;
				end
				y = y + lineHeight
			end
			bDone = true;
		end
	end

	self.height = self.marginTop + y + self.marginBottom
end

function ISRichTextLayout:render(x, y, ui)
	if self.textDirty then
		self:paginate()
	end

	if self.lines == nil then
		return
	end

	if self.clip then ui:setStencilRect(x, y, self.width, self.height) end

	for c,v in ipairs(self.images) do
		ui:drawTextureScaled(v, x + self.marginLeft + self.imageX[c], y + self.marginTop + self.imageY[c], self.imageW[c], self.imageH[c], 1, 1, 1, 1)
	end

	self.r = 1
	self.g = 1
	self.b = 1

	local orient = "left"
	for c,v in ipairs(self.lines) do
		if y + self.marginTop + self.lineY[c] + ui:getYScroll() >= ui:getHeight() then
			break
		end

		if self.rgb[c] then
			self.r = self.rgb[c].r
			self.g = self.rgb[c].g
			self.b = self.rgb[c].b
		end

		if self.orient[c] then
			orient = self.orient[c]
		end

		if self.fonts[c] ~= nil then
			self.font = self.fonts[c]
		end

		if c == #self.lines or (y + self.marginTop + self.lineY[c+1] + ui:getYScroll() > 0) then
			local r = self.r
			local b = self.b
			local g = self.g

			if v:contains("&lt;") then
				v = v:gsub("&lt;", "<")
			end
			if v:contains("&gt;") then
				v = v:gsub("&gt;", ">")
			end

			if string.trim(v) ~= "" then
				if orient == "centre" then
					ui:drawTextCentre(string.trim(v), x + self.width / 2 , y + self.marginTop + self.lineY[c], r, g, b,1, self.font)
				elseif orient == "right" then
					ui:drawTextRight(string.trim(v), x + self.marginLeft + self.lineX[c], y + self.marginTop + self.lineY[c], r, g, b,1, self.font)
				else
					ui:drawText(string.trim(v), x + self.marginLeft + self.lineX[c], y + self.marginTop + self.lineY[c], r, g, b,1, self.font)
				end
			end
		end
	end

	if ISRichTextLayout.drawMargins then
		ui:drawRectBorder(x, y, self.width, self.height, 0.5,1,1,1)
		ui:drawRect(x + self.marginLeft, y, 1, self.height, 1,1,1,1)
		local maxLineWidth = self.maxLineWidth or (self.width - self.marginRight - self.marginLeft)
		ui:drawRect(x + self.marginLeft + maxLineWidth, y, 1, self.height, 1,1,1,1)
		ui:drawRect(x, y + self.marginTop, self.width, 1, 1,1,1,1)
		ui:drawRect(x, y + self.height - self.marginBottom, self.width, 1, 1,1,1,1)
	end

	if self.clip then ui:clearStencilRect() end
end

function ISRichTextLayout:setText(text)
	self.text = text
end

function ISRichTextLayout:setMargins(left, top, right, bottom)
	self.marginLeft = left
	self.marginTop = top
	self.marginRight = right
	self.marginBottom = bottom
end

function ISRichTextLayout:getWidth()
	return self.width
end

function ISRichTextLayout:setWidth(width)
	self.width = width
end

function ISRichTextLayout:getHeight()
	if self.textDirty then
		self:paginate()
	end
	return self.height
end

function ISRichTextLayout:new(width)
	local o = ISBaseObject.new(self)
	o.width = width
	o.height = 0
	o.marginLeft = 20
	o.marginTop = 10
	o.marginRight = 10
	o.marginBottom = 10
	o.text = ""
	o.textDirty = false
	o.clip = false
	o.maxLines = 0
	o.defaultFont = UIFont.NewSmall
	return o
end


