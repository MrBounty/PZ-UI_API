--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

--[[ RADIO CATEGORIES:
    Undefined,
    Radio,
    Television,
    Military,
    Amateur,
    Bandit,
    Emergency,
    Other,
--]]

---@class DynamicRadio
DynamicRadio = {};
DynamicRadio.valid = true;
DynamicRadio.scripts = {};
DynamicRadio.channels = {
    { --channel
        name = "Automated Emergency Broadcast System",
        freq = {88000,108000}, -- number for static frequency, or table for random: {min, max};
        category = "Emergency", --Emergency type
        uuid = "EMRG-711984", --must remain fixed.
        register = true, --if existing channel dont register, existing channel must match the uuid found in radiodata.xml
        airCounterMultiplier = 1.0, --optional, change time text displays.
    }
};
DynamicRadio.cache = {};

function DynamicRadio.OnLoadRadioScripts(_scriptManager, _isNewGame)
    print("scriptmanager = "..tostring(_scriptManager)..", isNewGame = "..tostring(_isNewGame))
    local gt = getGameTime();
    local modData = gt:getModData();

    local radio = getZomboidRadio();

    --[[
    if _isNewGame then
        for k,v in ipairs(DynamicRadio.channels) do
            if type(v.freq)=="number" then
                --do nothing
            elseif v.freq[1] == v.freq[2] then
                v.freq = v.freq[1];
            else
                v.freq = radio:getRandomFrequency(v.freq[1], v.freq[2]);
            end
        end

        modData.dynamicRadio = DynamicRadio.channels;
    else
        --todo change this so it only syncs frequencies from moddata?
        --DynamicRadio.channels = modData.dynamicRadio;
        if modData.dynamicRadio then
            for k,v in ipairs(modData.dynamicRadio) do
                for k2,v2 in ipairs(DynamicRadio.channels) do
                    if v.uuid==v2.uuid and type(v.freq)=="number" then
                        v2.freq = v.freq;
                    end
                end
            end
        end
        modData.dynamicRadio = DynamicRadio.channels;
    end
    --]]

    if modData.dynamicRadio then
        for k,v in ipairs(modData.dynamicRadio) do
            for k2,v2 in ipairs(DynamicRadio.channels) do
                if v.uuid==v2.uuid and type(v.freq)=="number" then
                    v2.freq = v.freq;
                end
            end
        end
    end

    for k,v in ipairs(DynamicRadio.channels) do
        if type(v.freq)=="number" then
            --do nothing
        elseif v.freq[1] == v.freq[2] then
            v.freq = v.freq[1];
        else
            v.freq = radio:getRandomFrequency(v.freq[1], v.freq[2]);
        end
    end

    modData.dynamicRadio = DynamicRadio.channels;


    if not DynamicRadio.channels then
        DynamicRadio.valid = false;
        print("ERROR: loading dynamic radio failed...")
    else
        local dynamicChannel;
        local cat;
        for k,v in ipairs(DynamicRadio.channels) do
            if v.category=="Radio" then
                cat = ChannelCategory.Radio;
            elseif v.category=="Emergency" then
                cat = ChannelCategory.Emergency;
            elseif v.category=="Television" then
                cat = ChannelCategory.Television;
            elseif v.category=="Military" then
                cat = ChannelCategory.Military;
            elseif v.category=="Amateur" then
                cat = ChannelCategory.Amateur;
            elseif v.category=="Bandit" then
                cat = ChannelCategory.Bandit;
            else
                cat = ChannelCategory.Other;
            end

            if v.register then
                print("name = "..tostring(v.name)..", freq = "..tostring(v.freq)..", cat = "..tostring(cat)..", uuid = "..tostring(v.uuid));
                dynamicChannel = DynamicRadioChannel.new(v.name, v.freq, cat, v.uuid);

                if v.airCounterMultiplier and v.airCounterMultiplier >0 then
                    dynamicChannel:setAirCounterMultiplier(v.airCounterMultiplier);
                end

                _scriptManager:AddChannel(dynamicChannel,false);
            else
                dynamicChannel = _scriptManager:getRadioChannel(v.uuid);
            end

            if dynamicChannel then
                DynamicRadio.cache[v.uuid] = dynamicChannel;
            else
                print("ERROR: couldnt init dynamic channel")
            end
        end
    end

    if getCore():getDebug() then
        for k,v in pairs(DynamicRadio.cache) do
            print("Found radio channel: "..tostring(v:GetName())..", freq = "..tostring(k)..", freqcheck = "..tostring(v:GetFrequency())..", multi = "..tostring(v:getAirCounterMultiplier()));
        end
    end

end

function DynamicRadio.OnEveryHour()
    if not DynamicRadio.valid then
        return;
    end

    local gt = getGameTime();

    local radio = getZomboidRadio();

    for k,v in ipairs(DynamicRadio.scripts) do
        if DynamicRadio.cache[v.channelUUID] then
            v.OnEveryHour(DynamicRadio.cache[v.channelUUID], gt, radio)
        end
    end

end


Events.OnLoadRadioScripts.Add(DynamicRadio.OnLoadRadioScripts);
Events.EveryHours.Add(DynamicRadio.OnEveryHour);
