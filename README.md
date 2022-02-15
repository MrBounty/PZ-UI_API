# PZ-Simple UI API
Still under dev.  
API for making simple UI for the game project zomboid

The principle is to add elements line by line.  
As in the following diagram:
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/Shema.drawio.png)

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
[TODO] Add image
```lua
function onCreateUI()
    UI = NewUI()
    UI:setTitle("My UI")

    UI:addText("", "Title !", "Title")
    UI:nextLine()

    UI:addRichText("", text, "Small")
    UI:nextLine()

    UI:addText("", "What to do:", "Small")
    UI:addEntry("entry", "", false)
    UI:nextLine()

    UI:addButton("button1", "No", UI:close())
    UI:addButton("button2", "Yes", UI:toggle())

    UI:saveLayout()
end

Events.OnCreateUI.Add(onCreateUI)
```

# Understand coordinates
The menus are in 2d and the dimensions of a 2d object are its position in x and y as well as its size in width and height.  
To simplify and allow compatibility between different screen sizes. Positions and sizes are in percentage of screen for my mod. But in the vanilla game, all is in pixel.  
For example a width of 0.2 will take 20% of the screen.  
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/schema2d%20(1).png)

<br />

## Variables
### Fonts
- Small
- Medium
- Large
- Title
