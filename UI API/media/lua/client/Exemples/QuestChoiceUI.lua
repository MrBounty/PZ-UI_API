local UI
local text1 = "<H1> The longest night <BR> <SIZE:small> In this mission, you gonna need to survivre all night. <BR> <LEFT> Reward: <LINE> - M14 <LINE> - 20 ammo <BR> Failure Conditions: <LINE> -Death"

local function choose(button, args)
    getPlayer():Say(args.choice);
    UI:close();
end
    
function onCreateUI()
    UI = NewUI();

    UI:addRichText("rtext", text1); 
    UI:setLineHeightPercent(0.2);            
    UI:nextLine();

    UI:addText("t1", "Accept ?", _, "Center");
    UI["t1"]:addBorder();
    UI:addButton("b1", "Yes", choose);
    UI["b1"]:addArg("choice", "yes");
    UI:addButton("b2", "No", choose);
    UI["b2"]:addArg("choice", "no");

    UI:saveLayout();
end

--Events.OnCreateUI.Add(onCreateUI)