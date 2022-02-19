local UI

local text1 = "<H1> Rich Text <LINE> <SIZE:small> Add a small desc <BR> <LEFT> - xx <LINE> - xx <LINE> - xx <LINE> - xx"

local items1 = {"item1", "item2", "item3", "item4", "item5", "item5", "item6", "item7", "item8", "item9", "item10", "item11", "item12"}

local items2 = {}
items2["name1"] = "item1"
items2["name2"] = "item2"
items2["name3"] = "item3"
items2["name4"] = "item4"
items2["name5"] = "item5"
items2["name6"] = "item6"
items2["name7"] = "item7"
items2["name8"] = "item8"
items2["name9"] = "item9"
items2["name10"] = "item10"
items2["name11"] = "item11"
items2["name12"] = "item12"
items2["name13"] = "item13"

-- Create the UI with all element exept image and image button
function onCreateUI()
	UI = NewUI();

    UI:addText("", "Empty:", _, "Center");
    UI:addEmpty()
    UI:nextLine();

    UI:addText("", "Rich text:", _, "Center");
    UI:addRichText("", text1);
    --UI:setLineHeightPixel(UI:getDefaultLineHeightPixel())
    UI:nextLine();

    UI:addText("", "Progress bar:", _, "Center");
    UI:addProgressBar("pb1", 0, 0, 100);
    UI:nextLine();

    UI:addText("", "Button:", _, "Center");
    UI:addButton("", "", _)
    UI:nextLine();

    UI:addText("", "Tick box:", _, "Center");
    UI:addTickBox("t1")
    UI:nextLine();

    UI:addText("", "Entry:", _, "Center");
    UI:addEntry("e1", "")
    UI:nextLine();

    UI:addText("", "Combo:", _, "Center");
    UI:addComboBox("c1", items1)
    UI:nextLine();

    UI:addText("", "ScrollList:", _, "Center");
    UI:addScrollList("s1", items2)

    UI:setBorderToAllElements(true);                        -- Add border
    UI:saveLayout();                                    -- Save and create the UI
end

local function everyTenMinutes()
	print("Tick box: ", UI["t1"]:getValue());
    print("Entry: ", UI["e1"]:getValue());
    print("Combo: ", UI["c1"]:getValue());
    print("ScrollList: ", UI["s1"]:getValue());
end

function everyMinute()
    if not UI then return false end
    UI["pb1"]:setValue(i);
    if i > 100 then i = 0 end
    i = i + 5;
end

Events.OnCreateUI.Add(onCreateUI)
Events.EveryTenMinutes.Add(everyTenMinutes)
Events.EveryOneMinute.Add(everyMinute)