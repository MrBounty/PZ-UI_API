# PZ-Simple UI API
Still under dev.  
API for making simple UI for the game project zomboid

The principle is to add elements line by line.  
As in the following diagram:
![alt text](https://github.com/MrBounty/PZ-UI_API/blob/main/images/Shema.drawio.png)

<br />

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

<br />

## Elements
### Common functions
```lua
UI["text1"]:addBorder()
```

### Empty space  
```lua
-- @size: S
UI:addEmpty(size)
```

### Text
[TODO] Add image
```lua
-- @name:     variable name of the element  
-- @text:     Text to display  
-- @font:     Font to use (see Variables/Fonts sections) (Optional, Small by default)  
-- @position: "Left", "Right" or "Center" (Optional, Left by default)
UI:addText(name, text, font, position)

-- Exemple: 
UI:addText("text1", "My Title", "Title", "Center")
UI:addText("", "My text")


-- Change the text: 
UI["text1"]:setText("My New Title")
-- Change the font: 
UI["text1"]:setFont("Medium")
-- Change the color: 
UI["text1"]:setColor(a, r, g, b)
-- Change position: 
UI["text1"]:setPosition("Right")
UI["text1"]:setPosition("Left")
UI["text1"]:setPosition("Center")
```

### Rich Text
[TODO] Add image
```lua
-- @name: variable name of the element  
-- @text: Text to display  
-- @font: Font to use (see Variables/Fonts sections)  
UI:addRichText(name, text)

-- Exemple: 
UI:addRichText("rich1", text)

-- Change the text: 
UI["rich1"]:setText("My new text")
```
[TODO] Add guide for texte formatting

### Button
[TODO] Add image
```lua
-- @name: variable name of the element  
-- @text: Text to display in the button  
-- @func: Function to call when press  
UI:addButton(name, text, func)  

-- Exemple: 
UI:addButton("button1", "", UI2:open())

-- Change the text: 
UI["button1"]:setText("My new text")
```

### Tick box
[TODO] Add image
```lua
-- @name: variable name of the element
UI:addTickBox(name) 

-- Exemple: 
UI:addTickBox("tick1")

-- Get the value: 
UI["tick1"]:getValue()
```

### Entry
[TODO] Add image
```lua
-- @name:     variable name of the element  
-- @defaul:   Default text  
-- @isNumber: true if use for a number  
UI:addEntry(name, text, isNumber)

-- Exemples:  
UI:addEntry("entry1", "", false)
UI:addEntry("entry2", 100, true)

-- To get the value: 
UI["entry1"]:getValue()
```

### Combo box
[TODO] Add image
```lua
-- @name:   variable name of the element  
-- @items`: List of items to add in the list  
UI:addComboBox(name, items)

-- Exemple: 
UI:addComboBox("combo1", {"item 1", "item 2", "item 3"})

-- Get the value: 
UI["combo1"]:getValue()
-- Change items: 
UI["combo1"]:setItems({"item 4", "item 5", "item 6"})
```

### Scrolling list
[TODO] Add image
```lua
-- @name:  variable name of the element  
-- @items: List of items to add in the list  
UI:addScrollList(name, items) 

-- Exemple: 
UI:addScrollList("scroll1", {"item 1", "item 2", "item 3"})

-- To get the value: 
UI["scroll1"]:getValue()
-- Change items: 
UI["scroll1"]:setItems({"item 4", "item 5", "item 6"})
```

### Image
[TODO] Add image
```lua
-- @name: variable name of the element  
-- @path: path of the image file
-- @size: width of the image in % of the screen [0-1] (optional)
UI:addImage(name, path, size) 

-- Exemple: 
UI:addImage("image1", "ui/myImage.png", 10)

-- Change image: 
UI["image1"]:setImage("ui/myNewImage.png")
-- Change widht: 
UI["image1"]:setSize(20)
```

### Image button
[TODO] Add image
```lua
-- @name: variable name of the element
-- @path: path of the image file
-- @func: function to call when press
-- @size: width of the image in % of the screen [0-1] (optional)
UI:addImageButton(name, path, func, size)  

-- Exemple: 
UI:addImageButton("ibutton1", "ui/myImage.png", UI2:open(), 0)

-- Change the text: 
UI["ibutton1"]:setText("My new text")
-- Change image: 
UI["ibutton1"]:setImage("ui/myNewImage.png")
-- Change width: 
UI["ibutton1"]:setSize(20)
```

<br />

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
