--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

--[[
 NOTE: A temporary solution, when devices update comes things may change drastically.
--]]

--[[
ISWorldMenuElements = ISWorldMenuElements or {};

function ISWorldMenuElements.ContextDisksAndTapes()
    local self 					= ISMenuElement.new();
    --self.worldMenu 				= ISContextManager.getInstance().getWorldMenu();
    self.validCdDevices = {};
    self.validVhsDevices = {};

    function self.init()
        if #self.validCdDevices > 0 then
            return;
        end
        for i=8,15 do
            table.insert(self.validCdDevices, "appliances_radio_01_"..tostring(i));
        end
        for i=0,7 do
            table.insert(self.validVhsDevices, "appliances_television_01_"..tostring(i));
        end
    end

    function self.createMenu( _data )
        if getCore():getGameMode() == "Tutorial" then
            return;
        end

        for _,item in ipairs(_data.objects) do
            if instanceof( item, "IsoWaveSignal") then
                local sprite = item:getSprite():getName();
                --print("IsoWaveSignal sprite = "..tostring(sprite)..", valids = "..tostring(#self.validDevices))
                local type = -1;
                for k,v in ipairs(self.validCdDevices) do
                    if v==sprite then
                        type = 0;
                    end
                end
                for k,v in ipairs(self.validVhsDevices) do
                    if v==sprite then
                        type = 1;
                    end
                end

                if type==-1 then return; end

                local inv = _data.inventory;
                local medias = {};
                if type==0 then
                    local list = inv:FindAll("Base.Disc_Retail");
                    for i=0,list:size()-1 do
                        table.insert(medias, list:get(i));
                    end
                end
                if type==1 then
                    local list = inv:FindAll("Base.VHS_Retail");
                    for i=0,list:size()-1 do
                        table.insert(medias, list:get(i));
                    end
                    local list = inv:FindAll("Base.VHS_Home");
                    for i=0,list:size()-1 do
                        table.insert(medias, list:get(i));
                    end
                end

                if #medias>0 then
                    local inv = _data.inventory;

                    print("Found valid device for disks and tapes");
                    for k,v in ipairs(medias) do
                        print(v);
                    end
                    local deviceData = item:getDeviceData();
                    local name = deviceData:getDeviceName();
                    if _data.test then return true; end
                    local main = _data.context:addOption(getText("Play media"), _data, nil );
                    local subMenuPlay = ISContextMenu:getNew(_data.context);
                    _data.context:addSubMenu(main, subMenuPlay);

                    if deviceData:getIsTurnedOn() then
                        for i=1,#medias do
                            local data = medias[i]:getMediaData();
                            subMenuPlay:addOption(data:getTranslatedTitle() .. " - " .. data:getTranslatedSubTitle(), _data, self.playMedia, item, medias[i] );
                        end
                    else
                        local error = subMenuPlay:addOption(getText("Device is not turned on..."), _data, nil );
                        error.notAvailable = true;
                    end
                end
            end
        end
    end

    function self.playMedia( _data, _object, _mediaItem )
        --local window = ISRadioWindow.activate( _data.player, _object, true );
        --todo add action to play the media
        if luautils.walkAdj( _data.player, _object:getSquare(), false ) then
            ISTimedActionQueue.add(ISRecMediaAction:new("PlayMedia", _data.player, _object, _mediaItem ));
        end
    end

    return self;
end
--]]
