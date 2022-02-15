# PZ-Simple UI API
Want to make a simple menu for your mod to :  
- Ask the player to choose between two things?
- Ask the player a nickname, age, height, weight, etc ?
- Display a map ?
- Display player-related information from your mod such as amount of money, time on server, etc ?
- Show players the start of an event ?
- And more ?

This mod is fore you !  

# Make my first window
First you create a new UI with the `NewUI()` function.  
After that it's easy, you just add [elements](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md) for the first line.  
When you added your elements, you jump to the next line with the function `ui:nextLine()` and you continu like that.  
One all line create, you just call `ui:saveLayout()` to finish creating the ui.  

As in the following diagram:
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/preview%20perso.png)

# Understand coordinates
The menus are in 2d and dimensions of 2d object are its position in x and y as well as its size in width and height.  
x and y are coordinates of the left top corner.  
To simplify and allow compatibility between different screen sizes. Positions and sizes are in percentage of screen for my mod. But in the vanilla game, all is in pixel.  
For example a width of 0.2 will take 20% of the screen.  
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

## Choose your team
[TODO] Add gif
```lua
local UI

local function choose(button args)
    getPlayer():Say("I'm in the " .. args.team .. " team now !");
    UI:close();
end
    
function onCreateUI()
    UI = NewUI();
    UI:addText("", "Choose your team", "Title", "Center");
    UI:nextLine();
    
    UI:addButton("b1", "Blue", choose);
    UI["b1"]:addArgument("team", "blue");
    UI:addButton("b2", "Red", choose);;
    UI["b1"]:addArgument("team", "red");
    
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
