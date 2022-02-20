# UI functions
## Toggle
```lua
UI:open()                       -- Display the UI
UI:close()                      -- Hide the UI
UI:toggle()                     -- Toggle the UI
```

## Position
```lua
UI:setPositionPercent(x, y)     -- Position of the top left corner of the window in % of the screen [0-1]
UI:setPositionPixel(x, y)       -- Position of the top left corner of the window in pixel

UI:setXPercent(x)               -- Position of x of the top left corner of the window in % of the screen [0-1]
UI:setXPixel(x)                 -- Position of x of the top left corner of the window in pixel

UI:setYPercent(y)               -- Position of y of the top left corner of the window in % of the screen [0-1]
UI:setYPixel(y)                 -- Position of y of the top left corner of the window in pixel

UI:setInCenterOfScreen()        -- Set in center of the screen
```

## Call before creating elements
```lua
UI:setColumnWidthPercent(column, pctW)  -- Set the default width of elements for a column. column is an int
UI:setColumnWidthPixel(column, pxlW)
-- Note: This function can be used after element creation and before saveLayout() if there is no image or button image as elements.

-- Set default height of elements that follow this function (can be call at the beginning or in the middle of an UI)
UI:setDefaultLineHeightPercent(h)
UI:setDefaultLineHeightPixel(h)
```

## Call before nextLine()
```lua
UI:setLineHeightPercent(pctH)   -- Force height of actual line in % of the screen [0-1]
UI:setLineHeightPixel(pxlH)     -- Force height of actual line in pixel
```

## Call before saveLayout()
```lua
UI:setWidthPercent(pctW)                -- Set width of window in % of the screen [0-1]
UI:setWidthPixel(pxlW)                  -- Set width of window in pixel
```

## Other
```lua
UI:isSubUIOf(UI2)                       -- If the UI is a sub UI. Like that if the parent is close, the sub UI is close too. And you can acces the parentUI with the variable

UI:nextLine()                           -- To jump to an other line

UI:getIsVisible()                       -- To know if the player see the UI

UI:setBorderToAllElements(bool)         -- Add/remove border to all elements of the ui
UI:setTitle(string)                     -- Add a title to the top bar of the UI
UI:setKey(key)                          -- Key to toggle the UI
UI:setCollapse(boolean)                 -- If the window can collapse when click outside of it, default false

UI:getDefaultLineHeightPercent()        -- Can be usefull for rich text and list like that UI:setLineHeightPixel(UI:getDefaultLineHeightPixel())
UI:getDefaultLineHeightPixel()
```
Find all key value [here](https://theindiestone.com/forums/index.php?/topic/9799-key-code-reference/)
