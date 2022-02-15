--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

ISInventoryMenuElements = ISInventoryMenuElements or {};

function ISInventoryMenuElements.ContextMedia()
    local self 					= ISMenuElement.new();
    self.invMenu			    = ISContextManager.getInstance().getInventoryMenu();

    function self.init()
    end

    function self.createMenu( _item )
        if _item:isRecordedMedia() then
            if _item:getContainer() ~= self.invMenu.inventory then
                return;
            end
            local mediaData = _item:getMediaData();

            if mediaData then
                local extra = mediaData:getTranslatedExtra();

                if extra then
                    self.invMenu.context:addOption(getText("IGUI_media_readLabel"), self.invMenu, self.openMediaInfo, _item, extra );
                end

                if getCore():getDebug() then
                    -- This adds the option to change a media item to another data set.
                    local parent = self.invMenu.context:addOption(getText("DBG: Change recording"), self.invMenu, nil );
                    local subMenu = ISContextMenu:getNew(self.invMenu.context);
                    self.invMenu.context:addSubMenu(parent, subMenu);

                    local list = getZomboidRadio():getRecordedMedia():getAllMediaForType(mediaData:getMediaType());

                    for i=0, list:size()-1 do
                        local other = list:get(i);
                        subMenu:addOption(other:getTranslatedItemDisplayName(), self.invMenu, self.changeRecording, _item, other );
                    end
                end
            end
        end
    end

    function self.openMediaInfo( _p, _item, _text )
        ISMediaInfo.openPanel(self.invMenu.playerNum ,_text);
    end

    function self.changeRecording( _p, _item, _other )
        --print("Index = "..tostring(_other:getIndex()))
        --print("Item = "..tostring(_item:getDisplayName()))
        _item:setRecordedMediaData(_other);
        --_item:setName(_other:getTranslatedItemDisplayName());
    end

    return self;
end