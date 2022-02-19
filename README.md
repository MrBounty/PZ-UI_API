# PZ-Simple UI API
Want to make a simple menu for your mod to :  
- Ask the player to choose between two things?
- Ask the player a nickname, age, height, weight, etc ?
- Display a map ?
- Display player-related information from your mod such as amount of money, time on server, etc ?
- Warn players the start of an event ?
- And more ?

This mod is for you !  

My mod is only an overlay of what the base game does to simplify placement, initializations and the most useful functions of UIs.  
This means that all elements are vanilla elements. So for example, the button has access to all the functions of `ISButton` and `ISUIElement`.  
The windows itself is a derivate of `ISCollapsableWindow`. Files in `media/lua/client/ISUI`  

# Make my first window
First you create a new UI with the `NewUI()` function.  
Find all useable function of UI [here](https://github.com/MrBounty/PZ-UI_API/blob/main/UI%20functions.md).  
After that it's easy, you just add [elements](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md) for the first line.  
When you added your elements, you jump to the next line with the function `ui:nextLine()` and you continu like that.  
Once all line create, you just call `ui:saveLayout()` to finish creating the ui.  

As in the following diagram:
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/preview%20perso.png)

I highly recommend doing the UIs in event `OnCreateUI` but you can do it anywhere.  

# Understand coordinates
Menus are in 2d.  
Dimensions of 2d object are its **position in x and y** as well as its **size in width and height**.  
x and y are coordinates of the left top corner.  
Positions and sizes are in percentage of screen or in pixel.   
Percent is to simplify and allow compatibility between different screen sizes. For example a width of 0.2 will take 20% of the screen.    
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/schema2d%20(1).png)

# Examples
## Hello world
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/HelloWorld.jpg)
```lua
function onCreateUI()
    local UI = NewUI();
    UI:addText("", "Hello world", "Small", "Center")
    UI:saveLayout()
end

Events.OnCreateUI.Add(onCreateUI)
```

## Quest
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/quest.jpg)  
```lua
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
    UI["t1"]:setBorder(true);
    
    UI:addButton("b1", "Yes", choose);
    UI:addButton("b2", "No", choose);
    
    UI["b1"]:addArg("choice", "yes");
    UI["b2"]:addArg("choice", "no");

    UI:saveLayout();
end

Events.OnCreateUI.Add(onCreateUI)
```

## Choose your team
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/team.jpg)  
```lua
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
    
    UI:setBorderToAllElements(true);
    UI:setWidthPercent(0.15);
    UI:saveLayout();
end

Events.OnCreateUI.Add(onCreateUI)
```

## Choose a job
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/jobChoice.gif)  
```lua
local UI
local jobSelect = "";

-- Text for the rich text element
local text1 = "<H1> Your a policeman ! <LINE> <SIZE:small> You are here to protect people ! <LINE> <SIZE:small> <LEFT> You can: <LINE> - Do that <LINE> - And that <LINE> - And a lot more"
local text2 = "<H1> Your an engineer ! <LINE> <SIZE:small> You are here to create thing ! <LINE> <SIZE:small> <LEFT> You can: <LINE> - Do that <LINE> - And that <LINE> - And a lot more"
local text3 = "<H1> Your a doctor ! <LINE> <SIZE:small> You are here to help people ! <LINE> <SIZE:small> <LEFT> You can: <LINE> - Do that <LINE> - And that <LINE> - And a lot more"
local text4 = "<H1> Your nothing ! <LINE> <SIZE:small> You are here to die ! <LINE> <SIZE:small> <LEFT> You can: <LINE> - Do that <LINE> - And that <LINE> - And a lot more"
local texts = {text1, text2, text3, text4}

local jobs = {"Policeman", "Engineer", "Doctor", "Nothing"}

-- Functions for buttons
local function press(button, args)
    UI["rtext"]:setText(texts[args.index])
    jobSelect = jobs[args.index]
    getPlayer():Say("I selected " .. jobSelect);
end

local function ok()
    getPlayer():Say("I'm a " .. jobSelect .. " now !");
    UI:close();
end

-- Create the UI
function onCreateUI()
    UI = NewUI(0.15);

    -- Add window title
    UI:addText("title1", "Choose you job !", "Title", "Center");
    UI["title1"]:setBorder(true);
    UI:nextLine();

    -- Add job description
    UI:addRichText("rtext", text1);               
    UI:nextLine();

    -- Add buttons
    UI:addButton("button1", jobs[1], press);
    UI:addButton("button2", jobs[2], press);
    UI:addButton("button3", jobs[3], press);
    UI:addButton("button4", jobs[4], press);
    
    UI["button1"]:addArg("index", 1);
    UI["button2"]:addArg("index", 2);
    UI["button3"]:addArg("index", 3);
    UI["button4"]:addArg("index", 4);

    UI:addButton("", "Button", ok);
    UI:nextLine();

    -- Save window
    UI:saveLayout();
end

Events.OnCreateUI.Add(onCreateUI)
```

## Job list
Example with 2 UI  
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/jobBoard.gif)
```lua
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

Events.OnCreateUI.Add(onCreateUI)
```
