--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

ISWorldMenuElements = ISWorldMenuElements or {};

function ISWorldMenuElements.ContextTelevision()
    local self 					= ISMenuElement.new();
    --self.worldMenu 				= ISContextManager.getInstance().getWorldMenu();

    function self.init()
    end

    function self.createMenu( _data )
        if getCore():getGameMode() == "Tutorial" then
            return;
        end
        
        for _,item in ipairs(_data.objects) do
            if instanceof( item, "IsoWaveSignal") then
                if _data.test then return true; end
                _data.context:addOption(getText("IGUI_DeviceOptions"), _data, self.openPanel, item );
            end
        end
    end

    function self.openPanel( _data, _object )
        local window = ISRadioWindow.activate( _data.player, _object, true );
    end

    return self;
end