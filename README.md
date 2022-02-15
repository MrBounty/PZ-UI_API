# PZ-Simple UI API
Still under dev.  
API for making simple UI for the game project zomboid

The principle is to add elements line by line.  
As in the following diagram:
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/preview%20perso.png)

# Understand coordinates
The menus are in 2d and dimensions of 2d object are its position in x and y as well as its size in width and height.  
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

## Easy
### 4 text
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/Hello%20x4.jpg)
```lua
function onCreateUI()
    local UI = NewUI();
    UI:addText("text1", "Hello world1", _, "Left");
    UI:addText("", "Hello world2", "Medium", "Center");
    UI:nextLine();

    UI:addText("", "Hello worl3", _, "Right");
    UI:addText("", "Hello world4");

    UI["text1"]:setColor(1, 1, 0, 0);
    UI:addBorderToAllElements();
    UI:saveLayout();
end

Events.OnCreateUI.Add(onCreateUI)
```

### Button to make the player say something and close the UI
[TODO] Add gif
```lua
local UI

local function say()
    getPlayer():Say("Hello");
end

local function close()
    UI:toggle();
end
    
function onCreateUI()
    UI = NewUI();
    UI:addText("", " Say:");
    UI:addButton("", "", say);
    UI:nextLine();
    
    UI:addText("", " Close:");
    UI:addButton("", "close button", close);
    UI:saveLayout();
end

Events.OnCreateUI.Add(onCreateUI)
```

## Intermediate
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
	UI = NewUI(0.15);

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


    UI:saveLayout();
end

Events.OnCreateUI.Add(onCreateUI)
```
