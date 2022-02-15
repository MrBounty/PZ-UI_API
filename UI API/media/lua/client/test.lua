local UI
-- Text for the rich text element, with text formatting
local text =    "<CENTRE> <SIZE:medium> ZOMBOID SURVIVAL GUIDE <LINE> " .. 
                "<SIZE:large> #1 - CONTROL YOURSELF <LINE> " .. 
                "<IMAGECENTRE:media/ui/spiffo/control_yourself.png> <LINE> " .. 
                "<SIZE:medium> You're not going to survive long if you don't take some deep breaths, and learn the basics."


-- Functions for buttons
local function say(button, args)
    getPlayer():Say(args.arg1)
end

local function close()
    UI:toggle();
end


-- Create the UI
function onCreateUI()
	UI = NewUI();                                       -- Init UI

    UI:addText("", "My UI !", "Title", "Center");       -- Add title
    UI:nextLine();                                      -- Jump to the next line of the UI

    UI:addRichText("", text);                           -- Add rich and scrollable text
    UI:nextLine();                                      -- Jump to the next line of the UI

    UI:addText("", " Say: ");                           -- Add text
    UI:addButton("button1", "", say);                   -- Add button
    UI["button1"]:addArg("arg1", "Hello");              -- Add variable to button to use in the function
    UI:addButton("button2", "", say);                   -- Add button
    UI["button2"]:addArg("arg1", "World !");            -- Add variable to button to use in the function
    UI:nextLine();                                      -- Jump to the next line of the UI

    UI:addText("", " Close: ");                         -- Add text
    UI:addButton("", "Button", close);                  -- Add button with text

    UI:addBorderToAllElements();                        -- Add border
    UI:saveLayout();                                    -- Save and create the UI
end

--Events.OnCreateUI.Add(onCreateUI)