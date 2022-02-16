# UI functions
```lua
UI:open()                       -- Display the UI
UI:close()                      -- Hide the UI
UI:toggle()                     -- toggle the UI

UI:isSubOf(UI2)                 -- If the UI is a sub UI. Like that if the parent is close, the sub UI is close too. And you can acces the parentUI with the variable

UI:nextLine()                   -- To jump to an other line

UI:isVisible()                  -- To know if the player see the UI
UI:addBorderToAllElements()     -- Add border to all elements of the ui
UI:setTitle(string)             -- Add a title to the top bar of the UI
UI:setCanCollapse(bottom)       -- If the UI collapse when click outside. If bottom is true, collapse from the bottom.
UI:setKey(key)                  -- Key to toggle the UI
UI:setPosition(x, y)            -- Position of the top left corner of the window in % of the screen [0-1]
```

## Call before saveLayout()
```lua
UI:setWidthPercent(pctW)        -- Set width of window in % of the screen [0-1]
UI:setWidthPixel(pxlW)          -- Set width of window in pixel
```

## Call before nextLine()
```lua
UI:setLineHeightPercent(pctH)   -- Force height of actual line in % of the screen [0-1]
UI:setLineHeightPixel(pxlH)     -- Force height of actual line in pixel
```

Find all key value [here](https://theindiestone.com/forums/index.php?/topic/9799-key-code-reference/)
