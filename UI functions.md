# UI functions
```lua
UI:open()                       -- Display the UI
UI:close()                      -- Hide the UI
UI:toggle()                     -- toggle the UI

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
UI:setWidth(pctW)               -- Set width of actual line in % of the screen [0-1]
UI:setWidthPixel(pxlW)          -- Set width of actual line in pixel
```

## Call before nextLine()
```lua
UI:setLineHeight(pctH)          -- Force height of actual line in % of the screen [0-1]
UI:setLineHeightPixel(pxlH)     -- Force height of actual line in pixel
```

Find all key value [here](https://theindiestone.com/forums/index.php?/topic/9799-key-code-reference/)
