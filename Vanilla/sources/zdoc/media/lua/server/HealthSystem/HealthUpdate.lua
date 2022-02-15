--***********************************************************
--**                    ROBERT JOHNSON                     **
--**       Here we gonna handle all the health change      **
--***********************************************************

---@class healthUpdate
healthUpdate = {};
healthUpdate.lastGrid = nil;
healthUpdate.bodyDmg = nil;


healthUpdate.update = function()
    if getCore():isDedicated() then return end
	local player = getPlayer();
	healthUpdate.bodyDmg = player:getBodyDamage();
	-- first we check if the player moved on another grid
	local currentGrid = getCell():getGridSquare(player:getX(), player:getY(), player:getZ());
--~ 	local feeler = player:getFuturWalkedSquare();
	-- grid are different
--~ 	if healthUpdate.lastGrid == nil or healthUpdate.lastGrid ~= feeler then
--~ 		healthUpdate.lastGrid = currentGrid;
--~ 		-- check if there is a broken window (we could get scratched)
--~ 		healthUpdate.scratchFromWindow(feeler);
--~ 	end
end


healthUpdate.scratchFromWindow = function(feeler)
	local brokenWindow = healthUpdate.getBrokenWindow(feeler);
	-- we found a broken window on our way, maybe we gonna get scratched from broken glass
	if brokenWindow then
		-- we got 1 to 5 risk to get scratched
		if ZombRand(6) == 0 then
			-- we gonna randomly chose the body part wich is scratched
			local part = healthUpdate.bodyDmg:getBodyPart(BodyPartType.FromIndex(ZombRand(BodyPartType.ToIndex(BodyPartType.MAX))));
			part:SetScratched(true, true);
			getPlayer():Say("Ouch !");
		end
	end
end

healthUpdate.getBrokenWindow = function(feeler)
	-- we start to get all the special object on the square we are standing on
	local window = healthUpdate.lastGrid:getThumpableTo(feeler);
	if window and window:isDestroyed() then
		print("FOUND A WINDOW");
		return window;
	end
	return nil;
end

--Events.OnTick.Add(healthUpdate.update);
