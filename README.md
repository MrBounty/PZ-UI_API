# PZ-Simple UI API
Still under dev.  
API for making simple UI for the game project zomboid

## Examples
### Hello world
[TODO] Add image
```lua
UI = newUI()
UI:addText("", "Hello world !", "Small")
```

### Intermediate
[TODO] Add image
```lua
UI = newUI()

UI:addText("", "Title !", "Title")
UI:nextLine()

UI:addRichText("", text, "Small")
UI:nextLine()

UI:addText("", "What to do:", "Small")
UI:addEntry("entry", "", false)
UI:nextLine()

UI:addButton("button1", "No", UI:close())
UI:addButton("button2", "Yes", UI:toogle())
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
UI:addText("title1", "My Title", "Title")
-- Change the text: 
UI["title1"]:setText("My New Title")
```

### Rich Text
[TODO] Add image
```lua
-- @name: variable name of the element  
-- @text: Text to display  
-- @font: Font to use (see Variables/Fonts sections)  
UI:addRichText(name, text, font)

-- Exemple: 
`UI:addRichText("rich1", text, "Small")
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
UI["tick1"]
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
UI["combo1"]
-- Change items: 
UI["combo1"]:setItems({"item 4", "item 5", "item 6"})
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
UI["entry1"]
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
UI["scroll1"]
-- Change items: 
UI["scroll1"]:setItems({"item 4", "item 5", "item 6"})
```

### Image
[TODO] Add image
```lua
-- @name: variable name of the element  
-- @path: Path of the image file
-- @widht: widht of the image in % of the screen [0-100]
UI:addImage(name, path, widht) 

-- Exemple: 
UI:addImage("image1", "UI/myImage.png", 10)
-- Change image: 
UI["image1"]:setImage("UI/myNewImage.png")
-- Change widht: 
UI["image1"]:setWidht(20)
```

## Other functions
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
