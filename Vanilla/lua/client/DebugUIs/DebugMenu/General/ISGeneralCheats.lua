--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************
require "DebugUIs/DebugMenu/Base/ISDebugSubPanelBase";

ISGeneralCheats = ISDebugSubPanelBase:derive("ISGeneralCheats");

function ISGeneralCheats:initialise()
    ISPanel.initialise(self);
end

function ISGeneralCheats:createChildren()
    ISPanel.createChildren(self);

    local x,y,w = 10,10,self.width-30;
    
    self.boolOptions = {};

    self:addBoolOption("Build Cheat", ISBuildMenu);
    self:addBoolOption("Health Panel Cheat", ISHealthPanel);
    self:addBoolOption("Moveable Cheat", ISMoveableDefinitions);
    self:addBoolOption("Mechanics Cheat", ISVehicleMechanics);

    local barMod = 3;
    local y2, label;

    local tickbox;
    for k,v in ipairs(self.boolOptions) do
        y2,label = ISDebugUtils.addLabel(self,v,x,y,v.title, UIFont.Small);

        local tickOptions = {};
        table.insert(tickOptions, { text = "Enabled", ticked = false });
        y,tickbox = ISDebugUtils.addTickBox(self,v,x+(w/2),y,w/2,ISDebugUtils.FONT_HGT_SMALL,v.title,tickOptions,ISGeneralCheats.onTicked);

        v.label = label;
        v.tickbox = tickbox;

        y = ISDebugUtils.addHorzBar(self,y+barMod)+barMod;
    end

    self:setScrollHeight(y+10);
end

function ISGeneralCheats:addBoolOption(_title, _lua)
    local bool = {
        title = _title,
        lua = _lua
    };
    table.insert(self.boolOptions,bool);
    return bool;
end

function ISGeneralCheats:prerender()
    ISDebugSubPanelBase.prerender(self);

    local val;

    for k,v in ipairs(self.boolOptions) do
        val = v.lua.cheat;
        v.tickbox.selected[1] = val;
    end
end

function ISGeneralCheats:onTicked(_index, _selected, _arg1, _arg2, _tickbox)
    local v = _tickbox.customData;
    v.lua.cheat = not v.lua.cheat;
end

function ISGeneralCheats:update()
    ISPanel.update(self);
end

function ISGeneralCheats:new(x, y, width, height, doStencil)
    local o = {};
    o = ISDebugSubPanelBase:new(x, y, width, height, doStencil);
    setmetatable(o, self);
    self.__index = self;
    return o;
end

