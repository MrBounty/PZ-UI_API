---@class ISFPS
ISFPS = {};

ISFPS.lastSec = -1;
ISFPS.frame = 0;
ISFPS.start = false;
ISFPS.init = false;

function ISFPS.onKeyPressed(key)
	if key == getCore():getKey("Display FPS") then
		if ISFPS.start then
			ISFPS.start = false;
			ISEquippedItem.text = nil;
		else
			-- add the event to start calculing the FPS
			ISFPS.start = true;
--[[
			if not ISFPS.init then
				Events.OnTickEvenPaused.Add(ISFPS.onTick);
				ISFPS.init = true;
			end
--]]
		end
	end
end

-- Disabled this. ISEquippedItem.render() gets the FPS from Java.
--[[
function ISFPS.onTick()
	local second = getTimestamp();

	if ISFPS.lastSec == -1 then
		ISFPS.lastSec = second;
	end

	ISFPS.frame = ISFPS.frame + 1;

	if ISFPS.start and ISFPS.lastSec ~= second then
		ISFPS.lastSec = second;
		ISEquippedItem.text = "FPS : " .. ISFPS.frame;
		ISFPS.frame = 0;
	end
end
--]]

Events.OnKeyPressed.Add(ISFPS.onKeyPressed);
