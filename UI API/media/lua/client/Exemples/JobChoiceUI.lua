local UI
local jobSelect = "";

-- Text for the rich text element
local text1 = "<H1> Your a policeman ! <LINE> <SIZE:small> You are here to protect people ! <LINE> <SIZE:small> <LEFT> You can: <LINE> - Do that <LINE> - And that <LINE> - And a lot more"
local text2 = "<H1> Your an engineer ! <LINE> <SIZE:small> You are here to create thing ! <LINE> <SIZE:small> <LEFT> You can: <LINE> - Do that <LINE> - And that <LINE> - And a lot more"
local text3 = "<H1> Your a doctor ! <LINE> <SIZE:small> You are here to help people ! <LINE> <SIZE:small> <LEFT> You can: <LINE> - Do that <LINE> - And that <LINE> - And a lot more"
local text4 = "<H1> Your nothing ! <LINE> <SIZE:small> You are here to die ! <LINE> <SIZE:small> <LEFT> You can: <LINE> - Do that <LINE> - And that <LINE> - And a lot more"
local texts = {text1, text2, text3, text4}

local jobs = {"Policeman", "Engineer", "Doctor", "Nothing"}

-- Functions for buttons
local function press(button, args)
    UI["rtext"]:setText(texts[args.index])
    jobSelect = jobs[args.index]
    getPlayer():Say("I selected " .. jobSelect);
end

local function ok()
    getPlayer():Say("I'm a " .. jobSelect .. " now !");
    UI:close();
end


-- Create the UI
function onCreateUI()
	UI = NewUI();

    -- Add window title
    UI:addText("title1", "Choose you job !", "Title", "Center");
    UI["title1"]:setBorder(true);
    UI:nextLine();

    -- Add job description
    UI:addRichText("rtext", text1);               
    UI:nextLine();

    -- Add buttons
    UI:addButton("button1", jobs[1], press);
    UI["button1"]:addArg("index", 1);

    UI:addButton("button2", jobs[2], press);
    UI["button2"]:addArg("index", 2);

    UI:addButton("button3", jobs[3], press);
    UI["button3"]:addArg("index", 3);

    UI:addButton("button4", jobs[4], press);
    UI["button4"]:addArg("index", 4);

    UI:addButton("", "Button", ok);
    UI:nextLine();

    -- Save window
    UI:saveLayout();
end

Events.OnCreateUI.Add(onCreateUI)
