--***********************************************************
--**                    THE INDIE STONE                    **
--**				  Author: turbotutone				   **
--***********************************************************

---@class TestMarkers
TestMarkers = {};
TestMarkers.loc = {};
TestMarkers.index = 1;
TestMarkers.enabled = false;

function TestMarkers.ontick()
    if TestMarkers.loc[TestMarkers.index] then
        local loc = TestMarkers.loc[TestMarkers.index];
        local plr = getPlayer();
        local p = loc.pointer;

        if plr:getX()>=p:getX()-1 and plr:getX()<=p:getX()+1 and plr:getY()>=p:getY()-1 and plr:getY()<=p:getY()+1 then
            TestMarkers.index = TestMarkers.index + 1;
            if TestMarkers.index>#TestMarkers.loc then
                TestMarkers.index = 1;
            end
        end
        TestMarkers.enableCurrent();
    end
end

function TestMarkers.enableCurrent()
    for k,v in ipairs(TestMarkers.loc) do
        v.circle:setActive(k==TestMarkers.index);
        v.pointer:setActive(k==TestMarkers.index);
    end
end

function TestMarkers.add(_circle, _pointer)
    local loc = {};
    loc.circle = _circle;
    loc.pointer = _pointer;
    table.insert(TestMarkers.loc, loc);
end

if TestMarkers.enabled then
    Events.OnTick.Add(TestMarkers.ontick);
end

