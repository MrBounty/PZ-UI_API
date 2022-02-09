# PZ-Simple UI API
Still under dev.  
API for making simple UI for the game project zomboid

# Hello world example
## Make the UI
```lua
UI = newUI()
UI:addText("", "Hello world !", "Small")
```
## Open and close it
```lua
UI:open() -- Display the UI
UI:close() -- Hide the UI
```

# Elements
## Text
```lua
-- @name: variable name of the element  
-- @text: Text to display  
-- @font: Font to use (see Variables/Fonts sections)  
UI:addText(name, text, font)

-- Exemple: 
UI:addText("title1", "My Title", "Title")
```

## Rich Text
```lua
-- @name: variable name of the element  
-- @text: Text to display  
-- @font: Font to use (see Variables/Fonts sections)  
UI:addRichText(name, text, font)

-- Exemple: 
`UI:addRichText("rich1", text, "Small")
```

## Button
```lua
-- @name: variable name of the element  
-- @text: Text to display in the button  
-- @func: Function to call when press  
UI:addButton(name, text, func)  

-- Exemple: 
UI:addButton("button1", "", UI2:open())
```

## Tick box
```lua
-- @name: variable name of the element
UI:addTickBox(name) 

-- Exemple: 
UI:addTickBox("tick1")
-- To get the value: 
UI["tick1"]
```

## Combo box
```lua
-- @name: variable name of the element  
-- @items`: List of items to add in the list  
UI:addComboBox(name, items)

-- Exemple: 
UI:addComboBox("combo1", {"item 1", "item 2", "item 3"})
-- To get the value: 
UI["combo1"]
```

## Entry
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

## Scrolling list
```lua
-- @name: variable name of the element  
-- @items: List of items to add in the list  
UI:addScrollList(name, items) 

-- Exemple: 
UI:addScrollList("scroll1", {"item 1", "item 2", "item 3"})
-- To get the value: 
UI["scroll1"]
```

## Image
```lua
-- @name: variable name of the element  
-- @path: Path of the image file
-- @widht: widht of the image in % of the screen [0-100]
UI:addImage(name, path, widht) 

-- Exemple: 
UI:addImage("image1", "UI/myImage.png", 10)
```

# Other functions
## Set position
```lua
-- @x: position of the x axis of the top left corner of the window in % of the screen [0-100]
-- @y: position of the y axis of the top left corner of the window in % of the screen [0-100]
UI:setPosition(x, y)

--Example:
UI:setPosition(20, 20)
```

# Variables
### Fonts
- Small
- Medium
- Large
- Title
