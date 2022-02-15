---@class ISPlayerData
ISPlayerData = {}

function getPlayerData(id)

    return ISPlayerData[id+1];
end

function removeInventoryUI(id)
    local data = getPlayerData(id);
    if data == nil then return end;
    if data.playerHotbar ~= nil then
        data.playerHotbar:removeFromUIManager();
    end
    if data.playerInventory ~= nil then
        print("removing player inventory");
      --  data.playerInventory:setVisible(false);
        data.playerInventory:removeFromUIManager();
    end
    if data.buttonPrompt ~= nil then
        print("removing buttonprompts");
        data.buttonPrompt:removeFromUIManager();
    end

    if data.lootInventory ~= nil then
        print("removing loot inventory");
    --    data.lootInventory:setVisible(false);
        data.lootInventory:removeFromUIManager();
    end

    UIManager.setPlayerInventory(id, nil, nil)
    UIManager.setPlayerInventoryTooltip(id, nil, nil)

    if data.equipped ~= nil then
        print("removing equipped icons");
        data.equipped:removeFromUIManager();

    end
    if data.characterInfo ~= nil then
        print("removing characterInfo");
        data.characterInfo:removeFromUIManager();
    end
    if data.backButtonWheel then
        print("removing backButtonWheel")
        data.backButtonWheel:removeFromUIManager()
    end
    if data.safetyUI then
        print("removing safetyUI")
        data.safetyUI:removeFromUIManager()
    end
    if data.craftingUI then
        print("removing craftingUI")
        data.craftingUI:removeFromUIManager()
    end
    if data.mechanicsUI then
        print("removing mechanicsUI")
        data.mechanicsUI:removeFromUIManager()
    end
    if ISMoveableCursor.cursors[id] then
        print("removing cursor + panel")
        ISMoveableCursor.cursors[id]:exitCursor();
    end
    if ISRadioWindow.instances[id] or ISRadioWindow.instancesIso[id] then
        print("removing radio panels")
        if ISRadioWindow.instances[id] then ISRadioWindow.instances[id]:removeFromUIManager() end
        if ISRadioWindow.instancesIso[id] then ISRadioWindow.instancesIso[id]:removeFromUIManager() end
    end
    if data.vehicleDashboard then
        data.vehicleDashboard:removeFromUIManager()
    end
    if data.radialMenu then
        data.radialMenu:removeFromUIManager()
    end

    local context = getPlayerContextMenu(id)
    if context and context:getIsVisible() then
        context:hideAndChildren()
    end

    if data.miniMap then
        data.miniMap:saveSettings()
        data.miniMap:removeFromUIManager()
    end
    
    data.playerInventory = nil;
    data.lootInventory = nil;
    data.buttonPrompt = nil;
    data.equipped = nil;
    data.characterInfo = nil;
    data.playerHotbar = nil;
    data.backButtonWheel = nil
    data.safetyUI = nil
    data.craftingUI = nil
    data.mechanicsUI = nil
    data.vehicleDashboard = nil
    data.miniMap = nil

    end

function getButtonPrompts(id)
    local data = getPlayerData(id)
    if data then return data.buttonPrompt end
    return nil
end

function getPlayerInventory(id)
    local data = getPlayerData(id)
    if data then return data.playerInventory end
    return nil
end

function getPlayerLoot(id)
    local data = getPlayerData(id)
    if data then return data.lootInventory end
    return nil
end

function getPlayerContextMenu(id)
    local data = getPlayerData(id)
    if data then return data.contextMenu end
    return nil
end

function getPlayerHotbar(id)
    local data = getPlayerData(id)
    if data then return data.playerHotbar end
    return nil
end

function getPlayerInfoPanel(id)
    local data = getPlayerData(id)
    if data then return data.characterInfo end
    return nil
end

function getPlayerBackButtonWheel(id)
    local data = getPlayerData(id)
    if data then return data.backButtonWheel end
    return nil
end

function getPlayerSafetyUI(id)
    local data = getPlayerData(id)
    if data then return data.safetyUI end
    return nil
end

function getPlayerCraftingUI(id)
    local data = getPlayerData(id)
    return data and data.craftingUI
end

function getPlayerMechanicsUI(id)
    local data = getPlayerData(id)
    return data and data.mechanicsUI
end

function getPlayerMiniMap(id)
    local data = getPlayerData(id)
    return data and data.miniMap or nil
end

function getPlayerVehicleDashboard(id)
    local data = getPlayerData(id)
    return data and data.vehicleDashboard
end

function getPlayerRadialMenu(id)
    local data = getPlayerData(id)
    return data and data.radialMenu
end

function createPlayerData(id)

   if getCore():isDedicated() then return; end

    local numPlayers = getNumActivePlayers();
    print(numPlayers.." players found")

    for i=0,numPlayers-1 do
        removeInventoryUI(i);
    end

    ISPlayerData[id+1] = ISPlayerDataObject:new(id);

    for i=0,numPlayers-1 do
        if ISPlayerData[i+1] then
            ISPlayerData[i+1]:createInventoryInterface();
        end
    end
end

function destroyPlayerData(playerObj)
    local id = playerObj:getPlayerNum()
    if ISPlayerData[id+1] then
        removeInventoryUI(id)
        ISPlayerData[id+1] = nil
    end
end

local function onResolutionChange(oldw, oldh, neww, newh)
    local players = IsoPlayer.getPlayers()
    for playerNum=0,players:size()-1 do
        if ISPlayerData[playerNum+1] then
            ISPlayerData[playerNum+1]:onResolutionChange(oldw, oldh, neww, newh)
        end
    end
end

function destroyAllPlayerData()
    print("removing all player data");
    local numPlayers = getNumActivePlayers();
    for i=0, numPlayers-1 do
        print("- player: "..tostring(i));
        removeInventoryUI(i);
    end
end

Events.OnCreatePlayer.Add(createPlayerData)
Events.OnPlayerDeath.Add(destroyPlayerData)
Events.OnPostSave.Add(destroyAllPlayerData)
Events.OnResolutionChange.Add(onResolutionChange)
