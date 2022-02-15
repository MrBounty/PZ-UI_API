# UI functions
### Set width
```lua
-- @size: Widht of the x axis of the window in % of the screen [0-1]
UI:setSize(size)

--Example:
UI:setSize(20)
```

### Set position
```lua
-- @x: position of the x axis of the top left corner of the window in % of the screen [0-1]
-- @y: position of the y axis of the top left corner of the window in % of the screen [0-1]
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
-- @key: Key to toggle the UI
UI:setKey(key)

--Example:
UI:setKey(21) -- Y key
```
Find all key value here [Link](https://theindiestone.com/forums/index.php?/topic/9799-key-code-reference/)

### Others
```lua
UI:open()                       -- Display the UI
UI:close()                      -- Hide the UI
UI:toggle()                     -- toggle the UI

UI:isVisible()                  -- To know if the player see the UI
UI:setTitle(string)             -- Add a title to the top bar of the UI
UI:addBorderToAllElements()     -- Add border to all elements of the ui
```
