--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************


local function testModData()
    print("OnInitGlobalModData")
    if not ModData.exists("TestTable") then
        print("Creating testTable")
        local t = ModData.create("TestTable");

        t.SomeKey = "Some value";
        t.Number = 42;
        t.ThisIsATable = {1, 2, 3, 4};
        t.AnotherKey = true;
        t.FalseValue = false;
        t.ALongString = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin ultricies quam eu mollis mattis. Aliquam porta ante egestas suscipit tempor. Sed sed arcu velit. Maecenas tristique consectetur purus sed cursus. Vestibulum nec augue vitae lacus condimentum fringilla. Suspendisse commodo quam nec ligula ultricies volutpat. Vivamus a augue dignissim, ultricies leo quis, pulvinar ex. Donec rutrum sed eros at consectetur. Suspendisse pellentesque bibendum dictum. Donec commodo magna rutrum, ullamcorper velit eget, consequat tellus. Donec fringilla metus fringilla arcu ornare placerat."
        for i=0,10 do
            t[i] = i*2;
        end
    end

    local t = ModData.getOrCreate("WeatherData");
    t = ModData.getOrCreate("GpsData");
    t = ModData.getOrCreate("WeatherData");
    t = ModData.getOrCreate("AnimalData");
    t = ModData.getOrCreate("ResourceData");
end

--Events.OnInitGlobalModData.Add(testModData);
