--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

require "ISUI/ISCollapsableWindow"

---@class IsoRegionsWindow : ISCollapsableWindow
IsoRegionsWindow = ISCollapsableWindow:derive("IsoRegionsWindow")

function IsoRegionsWindow.OnOpenPanel()
    if IsoRegionsWindow.instance == nil then
        local ui = IsoRegionsWindow:new(20, 20, 400, 400)
        ui:initialise()
        ui:instantiate()
        IsoRegionsWindow.instance = ui
    end
    IsoRegionsWindow.instance:addToUIManager()
end

function IsoRegionsWindow:initialise()
    ISCollapsableWindow.initialise(self)
    self.title = "IsoRegions Debug"
end

function IsoRegionsWindow:createChildren()
    ISCollapsableWindow.createChildren(self)

    self.renderPanel = ISPanel:new(0, 0, self.width, self.height - self:titleBarHeight() - self:resizeWidgetHeight())
    self.renderPanel.render = IsoRegionsWindow.renderTex
    self.renderPanel:initialise()
    --	self.renderPanel:instantiate()
    self.renderPanel.onMouseDown = IsoRegionsWindow.onMapMouseDown
    self.renderPanel.onMouseUp = IsoRegionsWindow.onMapMouseUp
    self.renderPanel.onMouseUpOutside = IsoRegionsWindow.onMapMouseUpOutside
    self.renderPanel.onMouseMove = IsoRegionsWindow.onMapMouseMove
    self.renderPanel.onRightMouseDown = IsoRegionsWindow.onMapRightMouseDown
    self.renderPanel.onRightMouseUp = IsoRegionsWindow.onMapRightMouseUp
    self.renderPanel.onRightMouseUpOutside = IsoRegionsWindow.onMapRightMouseUpOutside
    self.renderPanel.onMouseWheel = IsoRegionsWindow.onRenderMouseWheel
    self.renderPanel:setAnchorRight(true)
    self.renderPanel:setAnchorBottom(true)
    self.renderPanel.parent = self
    self.renderPanel.renderer = isoRegionsRenderer()
    self.renderPanel.renderer:load()
    self:addView(self.renderPanel)
end

function IsoRegionsWindow:close()
    self:removeFromUIManager()
end

function IsoRegionsWindow:onMapMouseDown(x, y)
    x = self.renderer:uiToWorldX(x)
    y = self.renderer:uiToWorldY(y)

    if self.renderer:isEditingEnabled() then
        self.renderer:unsetSelected();
        self.renderer:editSquare(x, y);
    end
    --	addVirtualZombie(x, y)
    --[[
    if isKeyDown(Keyboard.KEY_LSHIFT) then
        self.settingPath = true
        self.renderer:setWallFollowerStart(x, y)
        self.renderer:setWallFollowerEnd(x, y)
        return true
    end
    if isKeyDown(Keyboard.KEY_LCONTROL) then
        self.renderer:setWallFollowerEnd(x, y)
        return true
    end
    --]]
    return false
end

function IsoRegionsWindow:onMapMouseUp(x, y)
    if self.settingPath then
        self.settingPath = false
    end
    if not self.renderer:isEditingEnabled() then
        self.renderer:setSelected(x,y);
    end
end

function IsoRegionsWindow:onMapMouseUpOutside(x, y)
    if self.settingPath then
        self.settingPath = false
    end
    self.renderer:unsetSelected();
end

function IsoRegionsWindow:onMapMouseMove(dx, dy)
    self.javaObject:setConsumeMouseEvents(self.panning or false)
    if self.panning then
        if not self.mouseMoved and (math.abs(self:getMouseX() - self.mouseDownX) > 4 or math.abs(self:getMouseY() - self.mouseDownY) > 4) then
            self.mouseMoved = true
        end
        if self.mouseMoved then
            self.parent.xpos = self.parent.xpos - ((dx)/self.parent.zoom)
            self.parent.ypos = self.parent.ypos - ((dy)/self.parent.zoom)
        end
        return true
    end
    return false
end

function IsoRegionsWindow:onMapRightMouseDown(x, y)
    self.mouseMoved = false
    self.mouseDownX = x
    self.mouseDownY = y
    self.panning = true
    return true
end

function IsoRegionsWindow.onKeyStartPressed(_key)
    if IsoRegionsWindow.instance then
        --print("key pressed = "..tostring(_key))
        if _key==19 then
            IsoRegionsWindow.instance:rotate();
        end
    end
end

function IsoRegionsWindow:rotate()
    self.renderPanel.renderer:editRotate();
end

function IsoRegionsWindow:onTeleport(worldX, worldY)
    local player = getSpecificPlayer(0)
    player:setX(worldX)
    player:setY(worldY)
    player:setLx(worldX)
    player:setLy(worldY)
end

function IsoRegionsWindow:onSquareDetails(worldX, worldY)
    local worldZ = self.renderPanel.renderer:getZLevel();
    local chunk = self.renderPanel.renderer:getChunkRegion(worldX, worldY);
    self.renderPanel.renderer:setSelectedWorld(worldX,worldY);
    local panel = IsoRegionDetails.OnOpenPanel();
    panel:readRegion( worldX, worldY, worldZ, chunk );
end

function IsoRegionsWindow:onUnsetSelect(worldX, worldY)
    self.renderPanel.renderer:unsetSelected();
end

function IsoRegionsWindow:onRecalcChunks()
    self.renderPanel.renderer:recalcSurroundings();
end

function IsoRegionsWindow:onOpenLogs()
    IsoRegionLogWindow.OnOpenPanel();
end

function IsoRegionsWindow:onChangeEditOption(option)
    self.renderPanel.renderer:setEditOption(option:getIndex(), not option:getValue());
end

function IsoRegionsWindow:onChangeZLevelOption(option)
    self.renderPanel.renderer:setZLevelOption(option:getIndex(), not option:getValue());
end

function IsoRegionsWindow:onChangeOption(option)
    option:setValue(not option:getValue())
    self.renderPanel.renderer:save()
end

function IsoRegionsWindow:onMapRightMouseUp(x, y)
    self.panning = false
    if not self.mouseMoved then
        local playerNum = 0
        local cellX = self.renderer:uiToWorldX(x) / 300
        local cellY = self.renderer:uiToWorldY(y) / 300
        cellX = math.floor(cellX)
        cellY = math.floor(cellY)
        local context = ISContextMenu.get(playerNum, x + self:getAbsoluteX(), y + self:getAbsoluteY())
        --context:addOption("Clear Zombies", cellX, zpopClearZombies, cellY)
        --context:addOption("Spawn Time To Zero", cellX, zpopSpawnTimeToZero, cellY)
        --context:addOption("Spawn Now", cellX, zpopSpawnNow, cellY)
        local worldX = self.renderer:uiToWorldX(x)
        local worldY = self.renderer:uiToWorldY(y)
        context:addOption("Teleport Here", self.parent, IsoRegionsWindow.onTeleport, worldX, worldY)
        if (not self.renderer:isEditingEnabled()) and self.renderer:hasChunkRegion(worldX, worldY) then
            context:addOption("Square Details", self.parent, IsoRegionsWindow.onSquareDetails, worldX, worldY)
        end
        if self.renderer:isHasSelected() then
            context:addOption("Unset selection", self.parent, IsoRegionsWindow.onUnsetSelect, worldX, worldY)
        end

        local subMenu = context:getNew(context)
        for i=1,self.renderer:getEditOptionCount() do
            local debugOption = self.renderer:getEditOptionByIndex(i-1)
            local option = subMenu:addOption(debugOption:getName(), self.parent, IsoRegionsWindow.onChangeEditOption, debugOption)
            if debugOption:getType() == "boolean" then
                subMenu:setOptionChecked(option, debugOption:getValue())
            end
        end
        local subMenuOption = context:addOption("Edit", nil, nil)
        context:addSubMenu(subMenuOption, subMenu)

        local subMenu = context:getNew(context)
        for i=1,self.renderer:getZLevelOptionCount() do
            local debugOption = self.renderer:getZLevelOptionByIndex(i-1)
            local option = subMenu:addOption(debugOption:getName(), self.parent, IsoRegionsWindow.onChangeZLevelOption, debugOption)
            if debugOption:getType() == "boolean" then
                subMenu:setOptionChecked(option, debugOption:getValue())
            end
        end
        local subMenuOption = context:addOption("zLevel", nil, nil)
        context:addSubMenu(subMenuOption, subMenu)

        local subMenu = context:getNew(context)
        for i=1,self.renderer:getOptionCount() do
            local debugOption = self.renderer:getOptionByIndex(i-1)
            local option = subMenu:addOption(debugOption:getName(), self.parent, IsoRegionsWindow.onChangeOption, debugOption)
            if debugOption:getType() == "boolean" then
                subMenu:setOptionChecked(option, debugOption:getValue())
            end
        end
        local subMenuOption = context:addOption("Display", nil, nil)
        context:addSubMenu(subMenuOption, subMenu)

        local subMenu = context:getNew(context)
        local subMenuOption = context:addOption("Other", nil, nil)
        if not isClient() then
            local option = subMenu:addOption("Recalc player surrounding chunks", self.parent, IsoRegionsWindow.onRecalcChunks);
        end
        local option = subMenu:addOption("Open logs", self.parent, IsoRegionsWindow.onOpenLogs);
        context:addSubMenu(subMenuOption, subMenu)
    end
    return true
end

function IsoRegionsWindow:onMapRightMouseUpOutside(x, y)
    self.panning = false
    return true
end

function IsoRegionsWindow:onRenderMouseWheel(del)
    if del > 0 then
        self.parent.zoom = self.parent.zoom * 0.8
    else
        self.parent.zoom = self.parent.zoom * 1.2
    end
    if self.parent.zoom > 30 then self.parent.zoom = 30 end
    return true
end

function IsoRegionsWindow:renderTex()
    if self.renderer:isEditingEnabled() then
        local x = self.renderer:uiToWorldX(self:getMouseX())
        local y = self.renderer:uiToWorldY(self:getMouseY())
        self.renderer:setEditSquareCoord(x, y);
    end

    self:setStencilRect(0, 0, self:getWidth(), self:getHeight())
    self.renderer:render(self.javaObject, self.parent.zoom, self.parent.xpos, self.parent.ypos)
    self:clearStencilRect()
end

function IsoRegionsWindow:new(x, y, width, height)
    local o = ISCollapsableWindow:new(x, y, width, height)
    setmetatable(o, self)
    self.__index = self
    o.backgroundColor = {r=0, g=0, b=0, a=1.0}
    o.xpos = getSpecificPlayer(0):getX()
    o.ypos =  getSpecificPlayer(0):getY()
    o.zoom = 1
    return o
end

function newIsoRegionsWindow()
    local ui = IsoRegionsWindow:new(20, 20, 400, 400)
    ui:initialise()
    ui:instantiate()
    ui:addToUIManager()
end

if getDebug() then
    Events.OnKeyStartPressed.Add(IsoRegionsWindow.onKeyStartPressed);
end
