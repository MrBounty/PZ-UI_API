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

## Common functions
### Before saveLayout()
Need to be call before saveLayout()
```lua
UI["text1"]:setWidthPercent(pctW)
UI["text1"]:setWidthPixel(pxlW)
```

### After saveLayout()
Can be call everywhere
```lua
UI["text1"]:addBorder()
UI["text1"]:toggle()
UI["text1"]:remove()
UI["text1"]:putBack()
```

## Empty space  
```lua
-- @name: Variable name of the element, if nb > 1, name gonna be name1, name2, name3, ect
-- @nb:   Number of empty space to make (Optional, 1 by default)
UI:addEmpty(name, nb)
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
UI:addRichText(name, text)

-- Exemple: 
UI:addRichText("rich1", text)

-- Change the text: 
UI["rich1"]:setText("", "My new text")
```
[Text formating](https://github.com/MrBounty/PZ-UI_API/blob/main/Variables.md)

## Button
```lua
-- @name: variable name of the element  
-- @text: Text to display in the button  
-- @func: Function to call when press.   
UI:addButton(name, text, func)  

-- Exemple: 
UI:addButton("button1", "", close)
local close(button, args)
  --Code
end

-- Change the text: 
UI["button1"]:setText("My new text")
-- Change the function: 
UI["button1"]:setFunc(func)
-- Add an argument to the args table to use in the function
UI["button1"]:addArg("index", 1);
```

## Tick box
```lua
-- @name: variable name of the element
-- @position: variable name of the element (Optional, Centre by default)
UI:addTickBox(name, position)

-- Exemple: 
UI:addTickBox("tick1", "Left")

-- Get the value: 
UI["tick1"]:getValue()
```

## Entry
```lua
-- @name:     variable name of the element  
-- @default:  Default text/value
-- @isNumber: true if use for a number  
UI:addEntry(name, default, isNumber)

-- Exemples:  
UI:addEntry("entry1", "", false)
UI:addEntry("entry2", 100, true)

-- To get the value: 
UI["entry1"]:getValue()
```

## Combo box
```lua
-- @name:   variable name of the element  
-- @items:  List of items to add in the list, need to be string
UI:addComboBox(name, items)

-- Exemple: 
UI:addComboBox("combo1", {"item 1", "item 2", "item 3"})

-- Get selected value: 
UI["combo1"]:getValue()
-- Change items: 
UI["combo1"]:setItems({"item 4", "item 5", "item 6"})
```

## Scrolling list
```lua
-- @name:  variable name of the element  
-- @items: List of items to add in the list, need to be string  
UI:addScrollList(name, items) 

-- Exemple: 
UI:addScrollList("scroll1", {"item 1", "item 2", "item 3"})

-- Get selected value or false if not selected: 
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
```

## Image button
```lua
-- @name: variable name of the element
-- @path: path of the image file
-- @func: function to call when press
UI:addImageButton(name, path, func)  

-- Exemple: 
UI:addImageButton("ibutton1", "ui/myImage.png", toDo)
```
