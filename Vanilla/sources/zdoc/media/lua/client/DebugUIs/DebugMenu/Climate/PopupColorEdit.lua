--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISPanel"

---@class PopupColorEdit : ISPanel
PopupColorEdit = ISPanel:derive("PopupColorEdit");
PopupColorEdit.instance = nil;

function PopupColorEdit.OnOpenPanel(_colorInfo,_parent,_ambient,_desat)
    if PopupColorEdit.instance==nil then
        PopupColorEdit.instance = PopupColorEdit:new(100, 100, 400, 400, "ColorEdit")
        PopupColorEdit.instance.baseAmbient = _ambient;
        PopupColorEdit.instance.baseDesat = _desat;
        PopupColorEdit.instance:initialise();
        PopupColorEdit.instance:instantiate();
    end

    PopupColorEdit.instance:addToUIManager();
    PopupColorEdit.instance:setVisible(true);

    PopupColorEdit.instance.colorInfo = _colorInfo;
    local clim = getClimateManager();
    PopupColorEdit.instance.global = clim:getClimateColor(ClimateManager.COLOR_GLOBAL_LIGHT);
    PopupColorEdit.instance.global:setEnableAdmin(true);
    PopupColorEdit.instance.global:setAdminValue(_colorInfo);

    PopupColorEdit.instance.parentPnl = _parent;
    PopupColorEdit.instance.firstRun = true;

    return PopupColorEdit.instance;
end

function PopupColorEdit:initialise()
    ISPanel.initialise(self);
end

function PopupColorEdit:createChildren()
    ISPanel.createChildren(self);

    local v, obj;

    local x,y,w = 10,10,self.width-30;

    ISDebugUtils.initHorzBars(self,x,w);

    self.floats = {};

    y, obj = ISDebugUtils.addLabel(self,"float_title",x+(w/2),y,"Edit color", UIFont.Medium);
    obj.center = true;
    y = ISDebugUtils.addHorzBar(self,y+5)+5;

    y = self:addColorOption("edit_cols",nil,x,y,w);

    local s = "The following options are for convenience only."
    y, obj = ISDebugUtils.addLabel(self,"float_note1",x+(w/2),y,s, UIFont.Small);
    obj.center = true;
    y = y+2;
    s = "They are not included in the configuration saves."
    y, obj = ISDebugUtils.addLabel(self,"float_note1",x+(w/2),y,s, UIFont.Small);
    obj.center = true;

    y = y+5;

    local clim = getClimateManager();
    v = clim:getClimateFloat(ClimateManager.FLOAT_AMBIENT);
    y, obj = self:addFloatOption("AMBIENT",v,x,y,w);
    y = y+3;

    v = clim:getClimateFloat(ClimateManager.FLOAT_DESATURATION);
    y, obj = self:addFloatOption("DESATURATION",v,x,y,w);
    y = y+3;

    v = clim:getClimateFloat(ClimateManager.FLOAT_NIGHT_STRENGTH);
    y, obj = self:addFloatOption("NIGHT_STRENGTH",v,x,y,w);
    y = y+3;

    v = clim:getClimateFloat(ClimateManager.FLOAT_FOG_INTENSITY);
    y, obj = self:addFloatOption("FOG_INTENSITY",v,x,y,w);

    for k,o in pairs(self.floats) do
        o.option:setEnableAdmin(true);
        if o==self.floats["AMBIENT"] then
            local val = PopupColorEdit.instance.baseAmbient;
            o.option:setAdminValue(val or o.option:getFinalValue());
            o.slider:setCurrentValue(val or o.option:getFinalValue(), true);
        elseif o==self.floats["DESATURATION"] then
            local val = PopupColorEdit.instance.baseDesat;
            o.option:setAdminValue(val or o.option:getFinalValue());
            o.slider:setCurrentValue(val or o.option:getFinalValue(), true);
        else
            o.option:setAdminValue(o.option:getFinalValue());
            o.slider:setCurrentValue(o.option:getFinalValue(), true);
        end
    end

    y = y+15;
    y,obj = ISDebugUtils.addButton(self, "apply", x, y, w, 16, "APPLY CHANGES", PopupColorEdit.onClick);

    y = y+15;
    y,obj = ISDebugUtils.addButton(self, "close", x, y, w, 16, "CLOSE", PopupColorEdit.onClick);


    self:setHeight(y+10);
end

function PopupColorEdit:addFloatOption(_id,_float,_x,_y,_w)
    local y, obj = ISDebugUtils.addLabel(self,"title_".._id,_x,_y,_id, UIFont.Small);

    local y2,obj2 = ISDebugUtils.addLabel(self,_id,_x+(_w/2)-20,_y,"0", UIFont.Small, false);
    local y3, obj3 = ISDebugUtils.addSlider(self,_id,_x+(_w/2),_y,_w/2, 18,PopupColorEdit.onFloatSliderChange);
    obj3.valueLabel = obj2;
    obj3:setValues(_float:getMin(), _float:getMax(), 0.01, 0.01);
    obj3:setCurrentValue(0);

    self.floats[_id] = { option = _float, title = obj, label = obj2, slider = obj3 };

    return y>y3 and y or y3;
end

function PopupColorEdit:addColorOption(_id,_color,_x,_y,_w)
    local t = { option = _color };

    ------------------------------------------------------------------------------

    local y3;
    y3, t.labelR_ext, t.sliderR_ext = self:addSlider(_id,_x,_y,_w);
    t.sliderR_ext.pretext = "R_exterior: ";

    _y = y3+5;
    y3, t.labelG_ext, t.sliderG_ext = self:addSlider(_id,_x,_y,_w);
    t.sliderG_ext.pretext = "G_exterior: ";

    _y = y3+5;
    y3, t.labelB_ext, t.sliderB_ext = self:addSlider(_id,_x,_y,_w);
    t.sliderB_ext.pretext = "B_exterior: ";

    _y = y3+5;
    local colorbox = ISPanel:new(_x+(_w/2),_y,_w/2,15);
    colorbox:initialise();
    colorbox.backgroundColor = {r=0.0,g=0.0,b=0.0,a=1.0};
    self:addChild(colorbox);

    y3 = colorbox:getY() + colorbox:getHeight();

    t.colorbox = colorbox;

    _y = y3+5;
    y3, t.labelA_ext, t.sliderA_ext = self:addSlider(_id,_x,_y,_w);
    t.sliderA_ext.pretext = "A_exterior: ";

    _y = y3+5;
    local colorbox = ISPanel:new(_x+(_w/2),_y,_w/2,15);
    colorbox:initialise();
    colorbox.backgroundColor = {r=0.0,g=0.0,b=0.0,a=1.0};
    self:addChild(colorbox);

    y3 = colorbox:getY() + colorbox:getHeight();

    t.colorboxAlpha = colorbox;

    ------------------------------------------------------------------------------

    _y = y3+5;
    y3, t.labelR_int, t.sliderR_int = self:addSlider(_id,_x,_y,_w);
    t.sliderR_int.pretext = "R_interior: ";

    _y = y3+5;
    y3, t.labelG_int, t.sliderG_int = self:addSlider(_id,_x,_y,_w);
    t.sliderG_int.pretext = "G_interior: ";

    _y = y3+5;
    y3, t.labelB_int, t.sliderB_int = self:addSlider(_id,_x,_y,_w);
    t.sliderB_int.pretext = "B_interior: ";

    _y = y3+5;
    local colorbox = ISPanel:new(_x+(_w/2),_y,_w/2,15);
    colorbox:initialise();
    colorbox.backgroundColor = {r=0.0,g=0.0,b=0.0,a=1.0};
    self:addChild(colorbox);

    y3 = colorbox:getY() + colorbox:getHeight();

    t.colorbox_int = colorbox;

    _y = y3+5;
    y3, t.labelA_int, t.sliderA_int = self:addSlider(_id,_x,_y,_w);
    t.sliderA_int.pretext = "A_interior: ";

    _y = y3+5;
    local colorbox = ISPanel:new(_x+(_w/2),_y,_w/2,15);
    colorbox:initialise();
    colorbox.backgroundColor = {r=0.0,g=0.0,b=0.0,a=1.0};
    self:addChild(colorbox);

    y3 = colorbox:getY() + colorbox:getHeight();

    t.colorboxAlpha_int = colorbox;

    ------------------------------------------------------------------------------

    self.optionControls = t;

    return _y>y3 and _y or y3;
end

function PopupColorEdit:addSlider(_id,_x,_y,_w,_title)
    local y2,obj2 = ISDebugUtils.addLabel(self,_id,_x+(_w/2)-20,_y,"0", UIFont.Small, false);
    local y3, obj3 = ISDebugUtils.addSlider(self,_id,_x+(_w/2),_y,_w/2, 18,PopupColorEdit.onSliderChange);
    obj3.pretext = _title;
    obj3.valueLabel = obj2;
    obj3:setValues(0, 1, 0.01, 0.01);
    obj3:setCurrentValue(0);
    return y3, obj2, obj3;
end

function PopupColorEdit:onClick(_button)
    if _button.customData == "apply" then
        self.colorInfo:setTo(self.global:getAdminValue());
        self.parentPnl:onApplyColorChange(self.colorInfo);
    elseif _button.customData == "close" then
        self:close();
    end
end

function PopupColorEdit:onFloatSliderChange(_newval, _slider)
    local s = self.floats[_slider.customData];
    if s then
        if s.slider.pretext then
            s.label:setName(s.slider.pretext..ISDebugUtils.printval(_newval,3));
        else
            s.label:setName(ISDebugUtils.printval(_newval,3));
        end
        s.option:setAdminValue(s.slider:getCurrentValue());
    end
end

function PopupColorEdit:onSliderChange(_newval, _slider)
    local s = self.optionControls;
    if s then
        self.global:setAdminValue(s.sliderR_ext:getCurrentValue(),s.sliderG_ext:getCurrentValue(),s.sliderB_ext:getCurrentValue(),s.sliderA_ext:getCurrentValue(),
            s.sliderR_int:getCurrentValue(),s.sliderG_int:getCurrentValue(),s.sliderB_int:getCurrentValue(),s.sliderA_int:getCurrentValue());
        s.labelR_ext:setName(s.sliderR_ext.pretext..ISDebugUtils.printval(s.sliderR_ext:getCurrentValue(),3));
        s.labelG_ext:setName(s.sliderG_ext.pretext..ISDebugUtils.printval(s.sliderG_ext:getCurrentValue(),3));
        s.labelB_ext:setName(s.sliderB_ext.pretext..ISDebugUtils.printval(s.sliderB_ext:getCurrentValue(),3));
        s.labelA_ext:setName(s.sliderA_ext.pretext..ISDebugUtils.printval(s.sliderA_ext:getCurrentValue(),3));

        s.labelR_int:setName(s.sliderR_int.pretext..ISDebugUtils.printval(s.sliderR_int:getCurrentValue(),3));
        s.labelG_int:setName(s.sliderG_int.pretext..ISDebugUtils.printval(s.sliderG_int:getCurrentValue(),3));
        s.labelB_int:setName(s.sliderB_int.pretext..ISDebugUtils.printval(s.sliderB_int:getCurrentValue(),3));
        s.labelA_int:setName(s.sliderA_int.pretext..ISDebugUtils.printval(s.sliderA_int:getCurrentValue(),3));
    end
end

function PopupColorEdit:prerender()
    ISPanel.prerender(self);

    --[[for k,o in pairs(self.floats) do
        o.slider:setCurrentValue(o.option:getAdminValue());
    end--]]

    local o = self.optionControls;
    local c = self.global:getAdminValue():getExterior();
    o.sliderR_ext:setCurrentValue(c:getRedFloat(), true);
    o.sliderG_ext:setCurrentValue(c:getGreenFloat(), true);
    o.sliderB_ext:setCurrentValue(c:getBlueFloat(), true);
    o.sliderA_ext:setCurrentValue(c:getAlphaFloat(), true);
    o.colorbox.backgroundColor.r = c:getRedFloat();
    o.colorbox.backgroundColor.g = c:getGreenFloat();
    o.colorbox.backgroundColor.b = c:getBlueFloat();
    o.colorboxAlpha.backgroundColor.r = c:getAlphaFloat();
    o.colorboxAlpha.backgroundColor.g = c:getAlphaFloat();
    o.colorboxAlpha.backgroundColor.b = c:getAlphaFloat();

    c = self.global:getAdminValue():getInterior();
    o.sliderR_int:setCurrentValue(c:getRedFloat(), true);
    o.sliderG_int:setCurrentValue(c:getGreenFloat(), true);
    o.sliderB_int:setCurrentValue(c:getBlueFloat(), true);
    o.sliderA_int:setCurrentValue(c:getAlphaFloat(), true);
    o.colorbox_int.backgroundColor.r = c:getRedFloat();
    o.colorbox_int.backgroundColor.g = c:getGreenFloat();
    o.colorbox_int.backgroundColor.b = c:getBlueFloat();
    o.colorboxAlpha_int.backgroundColor.r = c:getAlphaFloat();
    o.colorboxAlpha_int.backgroundColor.g = c:getAlphaFloat();
    o.colorboxAlpha_int.backgroundColor.b = c:getAlphaFloat();

    local s = o;
    --self.global:setAdminValue(s.sliderR_ext:getCurrentValue(),s.sliderG_ext:getCurrentValue(),s.sliderB_ext:getCurrentValue(),s.sliderA_ext:getCurrentValue(),
        --s.sliderR_int:getCurrentValue(),s.sliderG_int:getCurrentValue(),s.sliderB_int:getCurrentValue(),s.sliderA_int:getCurrentValue());
    if self.firstRun then
        self.firstRun = false;
        s.labelR_ext:setName(s.sliderR_ext.pretext..ISDebugUtils.printval(s.sliderR_ext:getCurrentValue(),3));
        s.labelG_ext:setName(s.sliderG_ext.pretext..ISDebugUtils.printval(s.sliderG_ext:getCurrentValue(),3));
        s.labelB_ext:setName(s.sliderB_ext.pretext..ISDebugUtils.printval(s.sliderB_ext:getCurrentValue(),3));
        s.labelA_ext:setName(s.sliderA_ext.pretext..ISDebugUtils.printval(s.sliderA_ext:getCurrentValue(),3));

        s.labelR_int:setName(s.sliderR_int.pretext..ISDebugUtils.printval(s.sliderR_int:getCurrentValue(),3));
        s.labelG_int:setName(s.sliderG_int.pretext..ISDebugUtils.printval(s.sliderG_int:getCurrentValue(),3));
        s.labelB_int:setName(s.sliderB_int.pretext..ISDebugUtils.printval(s.sliderB_int:getCurrentValue(),3));
        s.labelA_int:setName(s.sliderA_int.pretext..ISDebugUtils.printval(s.sliderA_int:getCurrentValue(),3));
    end
end

function PopupColorEdit:update()
    ISPanel.update(self);
end

function PopupColorEdit:close()
    self.global:setEnableAdmin(false);
    self:setVisible(false);
    self:removeFromUIManager();
    for k,o in pairs(self.floats) do
        o.option:setEnableAdmin(false);
    end
    PopupColorEdit.instance = nil
end

function PopupColorEdit:new(x, y, width, height, title)
    local o = {};
    o = ISPanel:new(x, y, width, height);
    setmetatable(o, self);
    self.__index = self;
    o.variableColor={r=0.9, g=0.55, b=0.1, a=1};
    o.borderColor = {r=0.4, g=0.4, b=0.4, a=1};
    o.backgroundColor = {r=0, g=0, b=0, a=0.8};
    o.buttonBorderColor = {r=0.7, g=0.7, b=0.7, a=0.5};
    o.zOffsetSmallFont = 25;
    o.moveWithMouse = true;
    o.panelTitle = title;
    --PopupColorEdit.instance = o
    ISDebugMenu.RegisterClass(self);
    return o;
end
