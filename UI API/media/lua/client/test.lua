local UI
local function say()
    getPlayer():Say("Hello")
end
local function close()
    UI:toggle();
end
function onCreateUI()
	UI = NewUI(0.1);
    
    UI:addText("text1", " Say: ", _, "Left");
    UI:addButton("", "Button", say);
    UI:nextLine();

    UI:addText("", " Close: ", _, "Left");
    UI:addButton("", "Button", close);

    UI:addBorderToAllElements();
    UI:saveLayout();
end
Events.OnCreateUI.Add(onCreateUI)