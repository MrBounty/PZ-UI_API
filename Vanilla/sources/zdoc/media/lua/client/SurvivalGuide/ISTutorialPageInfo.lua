require "ISBaseObject"

---@class ISTutorialPageInfo : ISBaseObject
ISTutorialPageInfo = ISBaseObject:derive("ISTutorialPageInfo");

--************************************************************************--
--** ISTutorialPageInfo:initialise
--**
--************************************************************************--

function ISTutorialPageInfo:initialise()

end

--************************************************************************--
--** ISTutorialPageInfo:new
--**
--************************************************************************--
function ISTutorialPageInfo:new (title, text, moreTextInfo, nextcondition)
	local o = {}
	o.data = {}
    setmetatable(o, self)
    self.__index = self
	o.title = title;
	o.text = text;
	o.nextcondition = nextcondition;
	o.moreTextInfo = moreTextInfo;
   return o
end

ISTutorialSetInfo = ISBaseObject:derive("ISTutorialSetInfo");

--************************************************************************--
--** ISTutorialPageInfo:initialise
--**
--************************************************************************--

function ISTutorialSetInfo:initialise()

end

function ISTutorialSetInfo:addPage(pagetitle, pagetext, moreTextInfo, pagenextcondition)
	self.pages[self.pageCount] = ISTutorialPageInfo:new(pagetitle, pagetext, moreTextInfo, pagenextcondition);
	self.pageCount = self.pageCount + 1;
end

function ISTutorialSetInfo:getCurrent()
	return self.pages[self.currentPage];
end

function ISTutorialSetInfo:applyPageToRichTextPanel(tutorialPanel)
	local current = self:getCurrent();
	tutorialPanel.textDirty = true;
	tutorialPanel.text = current.text;
	tutorialPanel:paginate();
end

function ISTutorialSetInfo:hasNext()
    return self.currentPage + 1 < self.pageCount;
end

function ISTutorialSetInfo:hasPrevious()
    return self.currentPage > 1;
end

function ISTutorialSetInfo:update(tutorialPanel)
	local current = self:getCurrent();

	if current ~= nil then
		if(current.nextcondition ~= nil) then
			if current.nextcondition() == true then
				self.currentPage = self.currentPage + 1;
				self:applyPageToRichTextPanel(tutorialPanel);
			end
		end
	end
end

--************************************************************************--
--** ISTutorialPageInfo:new
--**
--************************************************************************--
function ISTutorialSetInfo:new ()
	local o = {}
    setmetatable(o, self)
    self.__index = self
	o.pages = {}
	o.pageCount = 1;
	o.currentPage = 1;
   return o
end

