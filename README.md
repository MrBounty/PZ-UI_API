# PZ-Simple UI API
Still under dev.  
API for making simple UI for the game project zomboid

The principle is to add elements line by line.  
As in the following diagram:
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/Shema.drawio.png)

# Examples
### Hello world
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/HelloWorld.jpg)
```lua
function onCreateUI()
    local UI = NewUI();
    UI:addText("", "Hello world", "Small", "Center")
    UI:saveLayout()
end

Events.OnCreateUI.Add(onCreateUI)
```

### Easy
#### 4 text
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

#### Button to make the player say something and close the UI
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

### Intermediate
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

# UI functions
### Set width
```lua
-- @size: Widht of the x axis of the window in % of the screen [0-1]
UI:setSize(size)

--Example:
UI:setSize(20)
```

### Set position
```lua
-- @x: position of the x axis of the top left corner of the window in % of the screen [0-1]
-- @y: position of the y axis of the top left corner of the window in % of the screen [0-1]
UI:setPosition(x, y)

--Example:
UI:setPosition(20, 20)
```

### Set background color
```lua
-- @r: red value
-- @g: green value
-- @b: blue value
-- @a: transparency
UI:setBackColor(r, g, b, a)

--Example:
UI:setBackColor(0.1, 0.1, 0.1, 1)
```

### Add key too toggle the UI
```lua
-- @key: Key to toggle the UI
UI:setKey(key)

--Example:
UI:setKey(21) -- Y key
```
Find all key value here [Link](https://theindiestone.com/forums/index.php?/topic/9799-key-code-reference/)

### Others
```lua
UI:open()                       -- Display the UI
UI:close()                      -- Hide the UI
UI:toggle()                     -- toggle the UI

UI:isVisible()                  -- To know if the player see the UI
UI:setTitle(string)             -- Add a title to the top bar of the UI
UI:addBorderToAllElements()     -- Add border to all elements of the ui
```

<br />

## Variables
### Fonts
- Small
- Medium
- Large
- Title
