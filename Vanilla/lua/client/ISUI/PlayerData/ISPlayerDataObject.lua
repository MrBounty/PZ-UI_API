require "ISBaseObject"
require "ISUI/ISBackButtonWheel"
require "ISUI/ISInventoryPage"
require "ISUI/ISLayoutManager"
require "ISUI/ISSafetyUI"

ISPlayerDataObject = ISBaseObject:derive("ISPlayerDataObject");

function ISPlayerDataObject:createInventoryInterface()


    local zoom = 2.0;

    local playerObj = getSpecificPlayer(self.id);

    local bind = playerObj:getJoypadBind();
--[[
    if self.id > 0 and bind == -1 then
	    return;
    end
--]]
    local isMouse = self.id == 0 and bind == -1;
    local register = self.id == 0;
    if isMouse then
	    print("player ".. self.id .. " is mouse");
        zoom = 1.34;
    else
        register = false;
    end

zoom = 1.34

    local numPlayers = getNumActivePlayers();
    self:placeInventoryScreens(self.id, numPlayers, isMouse);

    local panel2 = ISInventoryPage:new(self.x1, self.y1, self.w1, self.h1, playerObj:getInventory(), true, zoom);
    panel2.player = self.id;
    panel2:initialise();
    panel2:setUIName("inventory"..self.id)
    panel2:addToUIManager();
    panel2:setController(bind)
    panel2:setInfo(getText("UI_InventoryInfo"));

--    print("createInventoryInterface player="..self.id);
    panel2.player = self.id;
    panel2.transferAll:setVisible(true);
    self.playerInventory = panel2;
    self.playerInventory.player = self.id;
    if not isMouse then
        panel2:setVisible(false);
    end

    --ISInventoryPage.playerInventory = panel2;
  --  if ISContainerPanelInstance == nil then
    local panel3 = ISInventoryPage:new(self.x2, self.y2, self.w2, self.h2, nil, false, zoom);
    self.lootInventory = panel3;
    self.lootInventory.player = self.id;
    panel3:setUIName("loot"..self.id)
--    print("loot inv created");
    --ISContainerPanelInstance = panel3;
    --   panel2:setNewContainer(self.container);
    panel3.player = self.id;
    panel3:initialise();
    panel3:addToUIManager();
    panel3:setController(bind)
    panel3:setInfo(getText("UI_LootInfo"));
    if not isMouse then
        panel3:setVisible(false);
    end
    panel3.lootAll:setVisible(true);

    UIManager.setPlayerInventory(self.id, self.playerInventory.javaObject, self.lootInventory.javaObject)

    if isMouse then
        panel2.pin = false;
        panel2.collapseButton:setVisible(false);
        panel2.pinButton:setVisible(true);
        panel2.pinButton:bringToTop();
        panel2.isCollapsed = true;
        panel2:setMaxDrawHeight(15);
        
        panel3.pin = false;
        panel3.collapseButton:setVisible(false);
        panel3.pinButton:setVisible(true);
        panel3.pinButton:bringToTop();
        panel3.isCollapsed = true;
        panel3:setMaxDrawHeight(15);
    else
        panel2:setPinned()
        panel3:setPinned()
    end
    self.buttonPrompt = ISButtonPrompt:new(self.id);
    self.buttonPrompt:initialise();
    self.buttonPrompt:addToUIManager();

--    print("loot all visible");
    self.contextMenu = ISContextMenu:new(0, 0, 1, 1);
--    print("context menu created");
    self.contextMenu:initialise();
--    print("context menu initialised");
    self.contextMenu:addToUIManager();
--    print("context menu added");
    self.contextMenu:setVisible(false);
    self.contextMenu.player = self.id;

    local xoff = 300
    local yoff = 100
    if self.id > 0 then xoff = 70; yoff = 20 end
    self.characterInfo = ISCharacterInfoWindow:new(self.x1left + xoff,self.y1top + yoff,400,400,self.id);
    self.characterInfo:initialise();
    self.characterInfo:addToUIManager();
    self.characterInfo:setVisible(isMouse and (self.characterInfo.visibleOnStartup == true));

    self.equipped = launchEquippedItem(playerObj);

    self.equipped:setX(self.x1left + 10);
    self.equipped:setY(self.y1top + 10);
    -- end
    --panel2 = ISInventoryPage:new(300, 300, 400+32, 400, getPlayer():getInventory());
    --panel2:initialise();
    --panel2:addToUIManager();

    local x = getPlayerScreenLeft(self.id)
    local y = getPlayerScreenTop(self.id)
    local w = getPlayerScreenWidth(self.id)
    local h = getPlayerScreenHeight(self.id)
    self.backButtonWheel = ISBackButtonWheel:new(self.id, (x + w) / 2 - 150, (y + h) / 2 - 150, 300, 300)
    self.backButtonWheel:setVisible(false)
    self.backButtonWheel:setAlwaysOnTop(true)
    self.backButtonWheel:addToUIManager()

    if isClient() then
        self.safetyUI = ISSafetyUI:new(x + w - 200, y + h - 100, self.id)
        self.safetyUI:initialise()
        self.safetyUI:addToUIManager()
        self.safetyUI:setVisible(true)
    end
    
    if getCore():getGameMode() ~= "Tutorial" then
        self.playerHotbar = ISHotbar:new(playerObj)
        self.playerHotbar:initialise();
        self.playerHotbar:addToUIManager();
        self.playerHotbar:setVisible(isMouse);
    end

    self.craftingUI = ISCraftingUI:new(0, 0, 800, 600, playerObj)
    self.craftingUI:initialise();
    self.craftingUI:addToUIManager();
    self.craftingUI:setVisible(false);
    self.craftingUI:setEnabled(false)

    self.mechanicsUI = ISVehicleMechanics:new(0,0,playerObj,nil);
    self.mechanicsUI:initialise();
--    self.mechanicsUI:addToUIManager();
    self.mechanicsUI:setVisible(false);
    self.mechanicsUI:setEnabled(false);

    self.vehicleDashboard = ISVehicleDashboard:new(self.id, playerObj)
    self.vehicleDashboard:initialise()
    self.vehicleDashboard:instantiate()

    -- Redisplay the dashboard after a splitscreen player is added.
    if playerObj:getVehicle() and playerObj:getVehicle():isDriver(playerObj) then
        self.vehicleDashboard:setVehicle(playerObj:getVehicle())
    end

    if ISMiniMap.IsAllowed() then
        self.miniMap = ISMiniMap.InitPlayer(self.id)
    end

    self.radialMenu = ISRadialMenu:new(0, 0, 100, 200, self.id)
    self.radialMenu:initialise()

    if register then
    ISLayoutManager.RegisterWindow('inventory'..self.id, ISInventoryPage, self.playerInventory) 
    ISLayoutManager.RegisterWindow('loot'..self.id, ISInventoryPage, self.lootInventory)
    ISLayoutManager.RegisterWindow('crafting'..self.id, ISCraftingUI, self.craftingUI)
    ISLayoutManager.RegisterWindow('mechanics'..self.id, ISVehicleMechanics, self.mechanicsUI)

    if getCore():getGameMode() == "Tutorial" then
        self.characterInfo:setVisible(false);
        self.characterInfo:setX(25)
        self.characterInfo:setY(300)
        self.lootInventory:setVisible(false);
        self.playerInventory:setVisible(false);
    end

    end
end

function ISPlayerDataObject:placeInventoryScreens(playerID, totalPlayers, mouse)
    local x = getPlayerScreenLeft(playerID)
    local y = getPlayerScreenTop(playerID)
    local w = getPlayerScreenWidth(playerID)
    local h = getPlayerScreenHeight(playerID)

    if mouse then

        local divhei = h / 3;

        if w < h then
            divhei = h / 4;
        end

        local divwid = round(w / 3)
        if divwid < 256 + 32 then
            -- min width of ISInventoryPage
            divwid = 256 + 32
        end
        
        self.x1 = x + w / 2 - divwid;
        self.x1left = x;
        self.y1top = y;
        self.y1 = self.y1top;
        self.w1 = divwid;
        self.h1 = divhei;
        self.x2 = self.x1 + divwid;
        self.y2 = self.y1top;
        self.w2 = divwid;
        self.h2 = divhei;

    else
        local ww = w
        local hh = h
        self.x1left = x;
        self.x1 = x;
        self.y1top = y;
        self.y1 = self.y1top + (hh/2);
        self.w1 = (ww / 2);
        self.h1 = (hh / 2);
        self.x2 = self.x1 + self.w1;
        self.y2 = self.y1;
        self.w2 = (ww / 2);
        self.h2 = (hh / 2);

    end


	print("PLAYER DATA OFFSET " .. playerID);
	if playerID > 0 then
		getSpecificPlayer(playerID):setOffSetXUI(self.x1);
		getSpecificPlayer(playerID):setOffSetYUI(self.y1);
	end
    print(self.x1);
    print(self.y1);
    print(self.w1);
    print(self.h1);
end

function ISPlayerDataObject:onResolutionChange(oldw, oldh, neww, newh)
    if JoypadState.players[self.id+1] then
        self:placeInventoryScreens(self.id, getNumActivePlayers(), false)

        self.playerInventory:setWidth(self.w1)
        self.playerInventory:setHeight(self.h1)
        self.playerInventory:setX(self.x1)
        self.playerInventory:setY(self.y1)

        self.lootInventory:setWidth(self.w2)
        self.lootInventory:setHeight(self.h2)
        self.lootInventory:setX(self.x2)
        self.lootInventory:setY(self.y2)

        self.equipped:setX(self.x1left + 10)
        self.equipped:setY(self.y1top + 10)
    end

    local x = getPlayerScreenLeft(self.id)
    local y = getPlayerScreenTop(self.id)
    local w = getPlayerScreenWidth(self.id)
    local h = getPlayerScreenHeight(self.id)

    if self.safetyUI then
        self.safetyUI:setX(x + w - 100)
        self.safetyUI:setY(y + h - 100)
    end

    if self.vehicleDashboard then
        self.vehicleDashboard:onResolutionChange()
    end

    if JoypadState.players[self.id+1] then
        self.characterInfo:setX(x + 70)
        self.characterInfo:setY(y + 20)
    end
end

function ISPlayerDataObject:new (id)
    local o = {}
    --o.data = {}
    setmetatable(o, self)
    self.__index = self

    o.id = id;


    return o
end

ISPlayerDataObject.onKeyPressed = function(key)
    if key == getCore():getKey("Zoom in") then
        screenZoomIn();
    end
    if key == getCore():getKey("Zoom out") then
        screenZoomOut();
    end
end


-- Events.OnKeyPressed.Add(ISPlayerDataObject.onKeyPressed);
