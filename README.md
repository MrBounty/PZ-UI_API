# PZ-Simple UI API
Still under dev.  
API for making simple UI for the game project zomboid

## Examples
### Hello world
[TODO] Add gif
```lua
UI = NewUI()
UI:addText("", "Hello world !", "Small")
UI:setKey(21) -- Y key
UI:saveLayout()
```

### Intermediate
[TODO] Add image
```lua
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
UI:addButton("button2", "Yes", UI:toogle())

UI:saveLayout()
```

### Open and close it
```lua
UI:open() -- Display the UI
UI:close() -- Hide the UI
UI:toogle() -- Toogle the UI
```

## Elements
### Text
[TODO] Add image
```lua
-- @name: variable name of the element  
-- @text: Text to display  
-- @font: Font to use (see Variables/Fonts sections)  
UI:addText(name, text, font)

-- Exemple: 
UI:addText("text1", "My Title", "Title")

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
UI:addRichText(name, text, font)

-- Exemple: 
UI:addRichText("rich1", text, "Small")

-- Change the text: 
UI["rich1"]:setText("My new text")
```

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
-- @name: variable name of the element  
-- @defaul: Default text  
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
-- @name: variable name of the element  
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
-- @name: variable name of the element  
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
-- @path: Path of the image file
-- @size: widht of the image in % of the screen [0-100]
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
-- @text: Text to display in the button
-- @func: Function to call when press
-- @size: widht of the image in % of the screen [0-100] (0 for normal button size)
UI:addImageButton(name, path, func, size)  

-- Exemple: 
UI:addImageButton("ibutton1", "ui/myImage.png", UI2:open(), 0)

-- Change the text: 
UI["ibutton1"]:setText("My new text")
-- Change image: 
UI["ibutton1"]:setImage("ui/myNewImage.png")
-- Change widht: 
UI["ibutton1"]:setSize(20)
```

## Other functions
### Set width
```lua
-- @size: Widht of the x axis of the window in % of the screen [0-100]
UI:setSize(size)

--Example:
UI:setSize(20)
```

### Set position
```lua
-- @x: position of the x axis of the top left corner of the window in % of the screen [0-100]
-- @y: position of the y axis of the top left corner of the window in % of the screen [0-100]
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

### Others
```lua
UI:isVisible() -- To know if the player see the UI
UI:setTitle(string) -- Add a title to the top bar of the UI
```

### Add key too toggle the UI
```lua
-- @key: Key to use to toggle the UI
UI:setKey(key)

--Example:
UI:setKey(21) -- Y key
```
Find all key value here [Link](https://theindiestone.com/forums/index.php?/topic/9799-key-code-reference/)

## Variables
### Fonts
- Small
- Medium
- Large
- Title
