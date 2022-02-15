--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

ISWorldMenuElements = ISWorldMenuElements or {};

function ISWorldMenuElements.ContextDisassemble()
    local self 					= ISMenuElement.new();

    function self.init()
    end

    function self.createMenu( _data )
        if getCore():getGameMode() == "Tutorial" then
            Tutorial1.createWorldContextMenuFromContext(_data.context, _data.objects);
            return;
        end
        if _data.test then return true; end

        local validObjList = {};
        for _,object in ipairs(_data.objects) do
            local square = object:getSquare();
            if square then
                local moveProps = ISMoveableSpriteProps.fromObject( object );
                -- Check for partially-destroyed multi-tile objects.
                if moveProps.isMultiSprite then
                    local grid = moveProps:getSpriteGridInfo(object:getSquare(), true)
                    if not grid then moveProps = nil end
                end
                if moveProps then
                    local resultScrap, chance, perkname = moveProps:canScrapObject( _data.player );
                    if resultScrap.craftValid then
                        table.insert(validObjList, { object = object, moveProps = moveProps, square = square, chance = chance, perkName = perkname, resultScrap = resultScrap });
                    end
                end
            end
        end

        if #validObjList == 0 then
            return
        end
        
        local disassembleMenu = _data.context:addOption(getText("ContextMenu_Disassemble"), _data.player, nil);
        local subMenu = ISContextMenu:getNew(_data.context);
        _data.context:addSubMenu(disassembleMenu, subMenu);

        local tooltipFont = ISToolTip.GetFont()
        
        for k,v in ipairs(validObjList) do
            local infoTable = v.moveProps:getInfoPanelDescription(v.square, v.object, _data.player, "scrap");

            local option = subMenu:addOption(Translator.getMoveableDisplayName(v.moveProps.name), _data, self.disassemble, v );
            option.notAvailable = not v.resultScrap.canScrap;

            local toolTip = ISToolTip:new();
            toolTip:initialise();
            toolTip:setVisible(false);
            toolTip.description = "";

            local column2 = 0;
            for _,t1 in ipairs(infoTable) do
                if #t1 == 2 then
                    local textWid = getTextManager():MeasureStringX(tooltipFont, t1[1].txt);
                    column2 = math.max(column2, textWid + 10)
                end
            end

            for _,t1 in ipairs(infoTable) do
                toolTip.description = string.format("%s <RGB:%.2f,%.2f,%.2f> %s", toolTip.description, t1[1].r / 255, t1[1].g / 255, t1[1].b / 255, t1[1].txt);
                if #t1 == 2 then
                    toolTip.description = string.format("%s <SETX:%d> <INDENT:%d> <RGB:%.2f,%.2f,%.2f> %s", toolTip.description, column2, column2, t1[2].r / 255, t1[2].g / 255, t1[2].b / 255, t1[2].txt);
                end
                toolTip.description = toolTip.description .. " <LINE> <INDENT:0> ";
            end
            option.toolTip = toolTip;
        end
    end

    function self.disassemble( _data, _v )
        if _v and _v.moveProps and _v.square and _v.object then
--            print("destroying item ".._v.moveProps.name, _v.square:getObjects():contains(_v.object));
            if _v.moveProps:canScrapObject( _data.player ) and _v.square:getObjects():contains(_v.object) then
                if _v.moveProps:walkToAndEquip( _data.player, _v.square, "scrap" ) or ISMoveableDefinitions.cheat then
                    if instanceof(_v.object,"IsoLightSwitch") and _v.object:hasLightBulb() then
                        ISTimedActionQueue.add(ISLightActions:new("RemoveLightBulb",_data.player, _v.object));
                    end
                    ISTimedActionQueue.add(ISMoveablesAction:new(_data.player, _v.square, _v.moveProps, "scrap" ));
                end
            end
        end
    end

    return self;
end
