# Summary
- [Common functions](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#common-functions)
- [Empty](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#empty-space)
- [Text](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#text)
- [Rich Text](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#rich-text)
- [Progress bar](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#progress-bar)
- [Button](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#button)
- [Tick box](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#tick-box)
- [Entry](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#entry)
- [Combo box](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#combo-box)
- [Scrolling list](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#scrolling-list)
- [Image](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#image)
- [Image button](https://github.com/MrBounty/PZ-UI_API/blob/main/Elements%20list.md#image-button)  

All original class can be found in `media/lua/client/ISUI`  

-------------------------

# Note about name
You will see that when you add an element, there is a variable name.  
This variable is useful to have access to the element from the UI object. For example if you add text with `UI:addText("text1", "My text")`, you could access the text1 element with `UI["text1"]` or `UI.text1`.  

If you never neeed to acces the element, you can add an empty name (`""`) or a nil. Like that the element is put in the `noNameElements` table.  

**Otherwise you have to be a little careful when choosing a name !**  
Because you can rewrite a variable already used by the UI. The best example being if you make a title and use "title" as the variable name. You are going to rewrite the "title" variable which already exists. It will create errors.  
If this happens, the console will output an error "UI API - ERROR: element name '" .. name .. "' is already a variable name. Change it !"  
To avoid this problem **I strongly recommend using a number in each variable name.** So "title" becomes "title1", even if there is only one.

-------------------------

## Common functions
### Before saveLayout()
Need to be call before saveLayout()
```lua
-- To force the width of an element. Can make an error if width total is higher that with of window
-- For image and image buton, need to be call before nextLine() otherwise the image will not have the correct height to keep the ratio
UI["text1"]:setWidthPercent(pctW)
UI["text1"]:setWidthPixel(pxlW)
```

### After saveLayout()
Can be call everywhere
```lua
-- Add or remove a border to the element
UI["text1"]:setBorder(bool)

-- To show it or hide it, disable button, list, ect.
UI["text1"]:setVisible(bool);
```

## Empty space  
Derived from `ISUIElement`  
```lua
-- @name: Variable name of the element, if nb > 1, name gonna be name1, name2, name3, ect
-- @nb:   Number of empty space to make (Optional, 1 by default)
-- @pctW: Width of element in percent of screen [0-1] (Optional)
-- @pxlW: Width of element in pixel (Optional)
UI:addEmpty(name, nb, pctW, pxlW)

-- Change background color
UI["empty1"]:setColor(a, r, g, b) -- Next update
```

## Text
Derived from `ISUIElement`  
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
Derived from `ISUIElement` with a `RichTextLayout` element  
```lua
-- @name: variable name of the element  
-- @text: Text to display   
UI:addRichText(name, text)

-- Exemple: 
UI:addRichText("rich1", text)

-- Change the text: 
UI["rich1"]:setText("", "My new text")
-- Change the background color: 
UI["rich1"]:setColor(a, r, g, b)
```
[Text formating](https://github.com/MrBounty/PZ-UI_API/blob/main/Variables.md)

## Progress bar
Derived from `ISUIElement`  
```lua
-- @name:   variable name of the element
-- @value:  value to display
-- @min:    min of the value
-- @max:    max of the value
UI:addProgressBar(name, value, min, max)  

-- Exemple: 
UI:addProgressBar("pbar1", 20, 0, 50)

-- Change the value
UI["pbar1"]:setValue(v)
-- Change min max
UI["pbar1"]:setMinMax(min, max)
-- Change margin of the bar
UI["pbar1"]:setMarginPercent(pctW, pctH)
UI["pbar1"]:setMarginPixel(pxlW, pxlH)
-- Change the color (white by default)
UI["pbar1"]:setColor(a, r, g, b)
```

## Button
Derived from `ISButton`  
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
UI["button1"]:setOnClick(func)
-- Add an argument to the args table to use in the function
UI["button1"]:addArg("index", 1);
```

## Tick box
Derived from `ISUIElement` with a `ISTickBox` element  
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
Derived from `ISTextEntryBox`  
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
-- Add a func when enter is press, function take 3 arg: the element, the text and the arg table
UI["entry1"]:setEnterFunc(func)
-- Add arg for the func when enter is press
UI["entry1"]:addArg(name, value)
```

-------------------------

# Note about combo box and scrolling list
items table can use number as key from 1 to x with a step of 1 (list of string), in that case value and text to display is the same.  
Or string as key, in that case the text display is the key and the value get with getValue is the variable of the key.  
```lua
-- Both work:
local items = {"item 1", "item 2", "item 3"}
 
local items = {}
items["text1"] = "item1"
items["text2"] = "item2"
items["text3"] = "item3"
 ```
-------------------------

## Combo box
Derived from `ISComboBox`  
```lua
-- @name:   variable name of the element  
-- @items:  List of items to add in the list
UI:addComboBox(name, items)

-- Exemple: 
UI:addComboBox("combo1", {"item 1", "item 2", "item 3"})

-- Get selected value: 
text = UI["combo1"]:getValue()
-- Change items: 
UI["combo1"]:setItems({"item 4", "item 5", "item 6"})
```

## Scrolling list
Derived from `ISScrollingListBox`  
```lua
-- @name:  variable name of the element  
-- @items: List of items to add in the list
-- @deselectOnClick: When True, clicking a selected entry will deselect it
UI:addScrollList(name, items, true) 

-- Exemple: 
UI:addScrollList("scroll1", {"item 1", "item 2", "item 3"}, true)

-- Get selected value or false if not selected: 
text, item = UI["scroll1"]:getValue()
-- Change items: 
UI["scroll1"]:setItems({"item 4", "item 5", "item 6"})
```

## Image
Derived from `ISImage`  
```lua
-- @name: variable name of the element  
-- @path: path of the image file
UI:addImage(name, path) 

-- Exemple: 
UI:addImage("image1", "media/ui/myImage.png")

-- Change image
UI["image1"]:setPath(path)
-- Change color
UI["image1"]:setColor(r, g, b)
```

## Image button
Derived from `ISButton`  
```lua
-- @name: variable name of the element
-- @path: path of the image file
-- @func: function to call when press
UI:addImageButton(name, path, func)  

-- Exemple: 
UI:addImageButton("ibutton1", "media/ui/myImage.png", toDo)

-- Change image
UI["ibutton1"]:setPath(path)
-- Change function
UI["ibutton1"]:setOnClick(func)
-- Add an argument to the args table to use in the function
UI["ibutton1"]:addArg("index", 1);
-- Change color
UI["ibutton1"]:setColor(a, r, g, b)
```
