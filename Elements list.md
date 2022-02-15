# Summary
- Empty
- Text
- Rich Text
- Button
- Tick box
- Combo box
- Scrolling list
- Image
- Image button

## Empty space  
```lua
UI:addText()
```

## Text
```lua
-- @name:     variable name of the element  
-- @text:     Text to display  
-- @font:     Font to use (see Variables/Fonts sections) (Optional, Small by default)  
-- @position: Position of text in the box (Optional, Left by default)
UI:addText(name, text, font, position)

-- Exemple: 
UI:addText("text1", "My Title", "Title", "Center")
UI:addText(_, "My text")

-- Change the text: 
UI["text1"]:setText("My New Title")
-- Change the font: 
UI["text1"]:setFont("Medium")
-- Change the color: 
UI["text1"]:setColor(a, r, g, b)
-- Change position: 
UI["text1"]:setPosition("Right")
```
[All fonts and position](https://github.com/MrBounty/PZ-UI_API/blob/main/Variables.md)

## Rich Text
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
[Text formating](https://github.com/MrBounty/PZ-UI_API/blob/main/Variables.md)

## Button
```lua
-- @name: variable name of the element  
-- @text: Text to display in the button  
-- @func: Function to call when press.   
UI:addButton(name, text, func)  

-- Exemple: 
UI:addButton("button1", "", UI2:open())

-- Change the text: 
UI["button1"]:setText("My new text")

-- Change the function: 
UI["button1"]:setFunc(func)

-- Add an argument for the function
UI["button1"]:addArg("index", 1);
```

## Tick box
```lua
-- @name: variable name of the element
UI:addTickBox(name) 

-- Exemple: 
UI:addTickBox("tick1")

-- Get the value: 
UI["tick1"]:getValue()
```

## Entry
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

## Combo box
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

## Scrolling list
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

## Image
```lua
-- @name: variable name of the element  
-- @path: path of the image file
UI:addImage(name, path) 

-- Exemple: 
UI:addImage("image1", "ui/myImage.png", 10)

-- Change image: 
UI["image1"]:setImage("ui/myNewImage.png")
```

## Image button
```lua
-- @name: variable name of the element
-- @path: path of the image file
-- @func: function to call when press
UI:addImageButton(name, path, func)  

-- Exemple: 
UI:addImageButton("ibutton1", "ui/myImage.png", UI2:open(), 0)

-- Change the text: 
UI["ibutton1"]:setText("My new text")
-- Change image: 
UI["ibutton1"]:setImage("ui/myNewImage.png")
```

## Common functions
```lua
UI["text1"]:addBorder()
UI["text1"]:toggle()
UI["text1"]:remove()
UI["text1"]:putBack()
```
