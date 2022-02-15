--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "ISUI/ISPanel"

---@class ISSLSounds : ISPanel
ISSLSounds = ISPanel:derive("ISSLSounds");

function ISSLSounds:initialise()
    ISPanel.initialise(self)
end

function ISSLSounds:createChildren()
    self.columns = {};
    table.insert(self.columns, {percent = 0.33})

    local col = self.columns[1];
    local w = self.width*col.percent;

    self.filterEntry = ISTextEntryBox:new("", 0, 0, 0, 0);
    self.filterEntry:initialise();
    self.filterEntry:instantiate();
    self.filterEntry:setText("");
    self:addChild(self.filterEntry);
    self.lastText = self.filterEntry:getInternalText();

    self.soundList = ISScrollingListBox:new(0, 0, 0, 0);
    self.soundList.font = UIFont.Small;
    self.soundList.fontHgt = getTextManager():getFontFromEnum(self.soundList.font):getLineHeight();
    self.soundList.itemPadY = 3;
    self.soundList.itemheight = self.soundList.fontHgt + (self.soundList.itemPadY * 2);
    self.soundList:initialise();
    self.soundList:instantiate();
    --self.soundList.itemheight = 30;
    self.soundList.selected = 0;
    --self.soundList.doDrawItem = ISSLEvent.drawSoundItem;
    --self.soundList:setOnMouseDownFunction(self, ISCraftingCategoryUI.refreshParent);
    --self.soundList:setOnMouseDoubleClick(self, ISCraftingCategoryUI.onDblClickRecipeList);
    --self.soundList.joypadParent = self;
    --    self.soundList.resetSelectionOnChangeFocus = true;
    self.soundList.drawBorder = false;
    self:addChild(self.soundList);

    self.playSoundButton = ISButton:new(0, 0, 0,0,"Play sound",self, ISSLSounds.playSoundFromMainList);
    self.playSoundButton:initialise()
    self:addChild(self.playSoundButton);

    --self:setEvent(getSLSoundManager():getStorySoundEvent("warzone"));

    self:loadSounds();

    self:onResize();
end

function ISSLSounds:loadSounds( _filter )
    self.soundList:clear();
    local sounds = getSLSoundManager():getStorySounds();
    if sounds~=nil and sounds:size()>0 then
        for i=0, sounds:size()-1 do
            local eSound 		= sounds:get(i);

            if _filter == nil or string.contains(string.lower(eSound:getName()), _filter) then
                self:addSoundItem(eSound);
            end
            --self:addSoundItem(eSound);
        end
    end
end

function ISSLSounds:addSoundItem(_sound)
    local name = _sound:getName();
    local item = {};
    item.sound = _sound;

    self.soundList:addItem(name, item);
end

function ISSLSounds:playSoundFromMainList()
    local index = self.soundList.selected;
    if self.soundList.items[index] then
        self.soundList.items[index].item.sound:playSound();
    end
end

function ISSLSounds:update()
    ISPanel.update(self);

    local text = string.lower(string.trim(self.filterEntry:getInternalText()));

    if text ~= self.filterText then
        self:loadSounds(text);
        self.filterText = text;
    end
end

function ISSLSounds:prerender()
    ISPanel.prerender(self);
end

function ISSLSounds:render()
    ISPanel.render(self);
end

function ISSLSounds:onResize()
    ISUIElement.onResize(self);
    local w,h = self:getWidth(), self:getHeight();

    local col = self.columns[1];
    local x,w,y,h = self:getWidth()-(self:getWidth()*col.percent), self:getWidth()*col.percent, 0, self:getHeight();

    --right column
    col = self.columns[1];
    x,w,y,h = self:getWidth()-(self:getWidth()*col.percent), self:getWidth()*col.percent, 0, self:getHeight();
    self.filterEntry:setX(x+5);
    self.filterEntry:setY(5)
    self.filterEntry:setWidth(w-10);
    self.filterEntry:setHeight(18);

    y = y+self.filterEntry:getY()+self.filterEntry:getHeight()+5;

    self.soundList:setX(x);
    self.soundList:setY(y);
    self.soundList:setWidth(w);
    self.soundList:setHeight(h-y-35);
    self.soundList.vscroll:setX(w-self.soundList.vscroll:getWidth());
    self.soundList.vscroll:setHeight(h-y-35);

    y = y+self.soundList:getHeight()+5;

    self.playSoundButton:setX(x+5);
    self.playSoundButton:setY(y);
    self.playSoundButton:setWidth(w-10);
    self.playSoundButton:setHeight(25);
end


function ISSLSounds:new (x, y, width, height)
    local o = ISPanel:new(x, y, width, height);
    setmetatable(o, self)
    self.__index = self
    o.x = x;
    o.y = y;
    o.background = true;
    o.backgroundColor = {r=0, g=1, b=0, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.width = width;
    o.height = height;
    o.anchorLeft = true;
    o.anchorRight = false;
    o.anchorTop = true;
    o.anchorBottom = false;
    o.filterText = "";
    return o
end
