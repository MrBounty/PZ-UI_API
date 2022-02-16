# PZ-Simple UI API
Want to make a simple menu for your mod to :  
- Ask the player to choose between two things?
- Ask the player a nickname, age, height, weight, etc ?
- Display a map ?
- Display player-related information from your mod such as amount of money, time on server, etc ?
- Warn players the start of an event ?
- And more ?

This mod is for you !  

# Make my first window
First you create a new UI with the `NewUI()` function.  
After that it's easy, you just add [elements](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md) for the first line.  
Find all useable function of UI [here](https://github.com/MrBounty/PZ-UI_API/blob/main/UI%20functions.md).  
When you added your elements, you jump to the next line with the function `ui:nextLine()` and you continu like that.  
One all line create, you just call `ui:saveLayout()` to finish creating the ui.  

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
    UI["t1"]:addBorder();
    UI:addButton("b1", "Yes", choose);
    UI["b1"]:addArg("choice", "yes");
    UI:addButton("b2", "No", choose);
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
    
    UI:addBorderToAllElements();
    UI:setWidthPercent(0.15);
    UI:saveLayout();
end

Events.OnCreateUI.Add(onCreateUI)
```

## Choose a job
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/exemple1.gif)
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
    UI = NewUI();

    UI:addText("title1", "Choose you job !", "Title", "Center");
    UI["title1"]:addBorder();
    UI:nextLine();


    UI:addRichText("rtext", text1);               
    UI:nextLine();


    UI:addButton("button1", jobs[1], press);
    UI["button1"]:addArg("index", 1);

    UI:addButton("button2", jobs[2], press);
    UI["button2"]:addArg("index", 2);

    UI:addButton("button3", jobs[3], press);
    UI["button3"]:addArg("index", 3);

    UI:addButton("button4", jobs[4], press);
    UI["button4"]:addArg("index", 4);

    UI:addButton("", "Button", ok);
    UI:nextLine();

    
    UI:setWidth(0.15);
    UI:saveLayout();
end

Events.OnCreateUI.Add(onCreateUI)
```
