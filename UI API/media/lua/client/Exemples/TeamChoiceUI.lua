local UI

local text1 = "Players: <LINE> - MrBounty <LINE> - Dane <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx"
local text2 = "Players: <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx <LINE> - xx"

local function choose(button, args)
    getPlayer():Say("I'm in the " .. args.team .. " team now !");
    UI:close();
end
    
function onCreateUI()
    UI = NewUI();
    UI:addText("", "Choose your team", "Large", "Center");
    UI:nextLine();

    UI:addRichText("", text1);
    UI:addRichText("", text2);
    UI:setLineHeightPercent(0.2);
    UI:nextLine();

    UI:addText("t1", "11/12", _, "Center");
    UI:addText("t2", "9/12", _, "Center");
    UI["t1"]:setColor(1, 0, 0, 1);
    UI["t2"]:setColor(1, 1, 0, 0);
    UI:nextLine();
    
    UI:addButton("b1", "Blue", choose);
    UI:addButton("b2", "Red", choose);
    UI["b1"]:addArg("team", "blue");
    UI["b2"]:addArg("team", "red");
    
    UI:addBorderToAllElements();
    UI:setWidthPercent(0.15);
    UI:saveLayout();
end

--Events.OnCreateUI.Add(onCreateUI)