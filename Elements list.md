# Summary
- [Common functions](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#common-functions)
- [Empty](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#empty-space)
- [Text](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#text)
- [Rich Text](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#rich-text)
- [Button](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#button)
- [Tick box](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#tick-box)
- [Entry](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#entry)
- [Combo box](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#combo-box)
- [Scrolling list](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#scrolling-list)
- [Image](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#image)
- [Image button](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#image-button)

## Common functions
### Before saveLayout()
Need to be call before saveLayout()
```lua
-- To force the width of an element. Can make an error if width total is higher that with of window
-- For image and image buton, need to be call before nextLine() otherwise the image will not have the correct ratio
UI["text1"]:setWidthPercent(pctW)
UI["text1"]:setWidthPixel(pxlW)
```

### After saveLayout()
Can be call everywhere
```lua
-- Add a border to the element
UI["text1"]:addBorder()

-- To show it or hide it, disable button, list, ect.
UI["text1"]:setVisible(bool);
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
Combo box use a numerical table. All value need to be text and getValue return the selected text.  
```lua
-- @name:   variable name of the element  
-- @items:  List of items to add in the list, need to be string
UI:addComboBox(name, items)

-- Exemple: 
UI:addComboBox("combo1", {"item 1", "item 2", "item 3"})

-- Get selected value: 
text = UI["combo1"]:getValue()
-- Change items: 
UI["combo1"]:setItems({"item 4", "item 5", "item 6"})
```

## Scrolling list
Scrolling list use a table with string as key and anything as item. GetValue return the selected text and item.  
```lua
-- @name:  variable name of the element  
-- @items: List of items to add in the list, key need to be string
UI:addScrollList(name, items) 

-- Exemple: 
local items = {}
items["name1"] = "item1"
items["name2"] = "item2"
items["name3"] = "item3"
UI:addScrollList("scroll1", items)

-- Get selected value or false if not selected: 
text, item = UI["scroll1"]:getValue()
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
