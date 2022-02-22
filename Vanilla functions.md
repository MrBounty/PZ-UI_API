I will try to put a list of usefull function from the vanilla game.  

# For elements and windows
```lua
-- Get position
ui:getX()
ui:getY()

-- Get size
ui:getWidth()
ui:getHeight()

ui:bringToTop() -- put the ui in front of all the others
```

# Other
```lua
-- Find size of text
getTextManager():getFontHeight(UIFont.Small) -- get height of a font
getTextManager():MeasureStringX(UIFont.Small, "My text") -- get width in pixel of a text for a font

-- Screen size
getCore():getScreenWidth()
getCore():getScreenHeight()

-- Mouse position
getMouseX()
getMouseY()

-- Key
isShiftKeyDown()  -- True if the shift key is down
isCtrlKeyDown()   -- True if the ctrl key is down

getPlayerInfoPanel(0) -- The main vanilla panel (health, protection...)
```

# Font size
```lua
