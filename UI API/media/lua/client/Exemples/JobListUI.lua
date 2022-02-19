local listUI, descUI
local text1 = "<H1> The longest night <BR> <SIZE:small> In this mission, you gonna need to survivre all night. <BR> <LEFT> Reward: <LINE> - M14 <LINE> - 20 ammo <BR> Failure Conditions: <LINE> -Death"
local text2 = "<H1> I need medical supply ! <BR> <SIZE:small> Please someone come to xx to help me ! I need a doctor or I'm gonna die. <BR> <LEFT> Reward: <LINE> - Everything I have"
local text3 = "<H1> Help me clean my neighborhood <BR> <SIZE:small> I need someone to help me fight a group of zombie near xx, there is around xx of them and I don't want to do it alone. <BR> <LEFT> Reward: <LINE> - 100$"
local text4 = "<H1> Looking for seed <BR> <SIZE:small> I'm looking for seed, every type of seed. I can pay or exchange. Contact me on my public frequencies xx.x."
local items = {};
items["EVENT - The longest night"] = text1;
items["URGENT - Medical delivery"] = text2;
items["Help me clean my neighborhood"] = text3;
items["Looking for seed"] = text4;
items["item5"] = "";
items["item6"] = "";
items["item7"] = "";
items["item8"] = "";
items["item9"] = "";
items["item10"] = "";
items["item11"] = "";

local function choose(button, args)
    getPlayer():Say("I accepted this mission !");
    listUI:close();
end

local function openJobDesc(_, item)
    descUI:open();
    descUI:setPositionPixel(listUI:getX() + listUI:getWidth(), listUI:getY());
    descUI["rtext"]:setText(item);
end
    
function onCreateUI()
    -- List UI
    listUI = NewUI(); -- Create UI
    listUI:setTitle("Job board");
    listUI:setMarginPixel(10, 10);
    listUI:setWidthPercent(0.15);

    listUI:addScrollList("list", items); -- Create list
    listUI["list"]:setOnMouseDownFunction(_, openJobDesc)

    listUI:saveLayout(); -- Create window

    -- Description UI
    descUI = NewUI();
    descUI:setTitle("Job desc");
    descUI:isSubUIOf(listUI);
    descUI:setWidthPercent(0.1);

    descUI:addEmpty(_, _, _, 10); -- Margin only for rich text
    descUI:addRichText("rtext", ""); 
    descUI:setLineHeightPercent(0.2); 
    descUI:addEmpty(_, _, _, 10); -- Margin only for rich text
    descUI:nextLine();

    descUI:addButton("b1", "Accept ?", choose);

    descUI:saveLayout();
    descUI:close();
end

--Events.OnCreateUI.Add(onCreateUI)