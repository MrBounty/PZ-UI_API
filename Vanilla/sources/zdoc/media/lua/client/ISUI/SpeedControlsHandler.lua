---@class SpeedControlsHandler
SpeedControlsHandler = {};
SpeedControlsHandler.previousSpeed = 1;

SpeedControlsHandler.onKeyPressed = function(key)
    if isClient() then
        return;
    end

	if not MainScreen.instance or not MainScreen.instance.inGame or MainScreen.instance:getIsVisible() then
		return
	end

	if key == getCore():getKey("Pause") then
		if not MainScreen.instance.inGame or MainScreen.instance:getIsVisible() then
			-- Default "Pause" is same as "Main Menu"
		elseif key == Keyboard.KEY_ESCAPE and getCell() and getCell():getDrag(0) then
			-- ToggleEscapeMenu does getCell():setDrag(nil)
		elseif getGameSpeed() > 0 then
			SpeedControlsHandler.previousSpeed = getGameTime():getTrueMultiplier();
			setGameSpeed(0);
		else
			setGameSpeed(1);
			getGameTime():setMultiplier(SpeedControlsHandler.previousSpeed or 1);
			SpeedControlsHandler.previousSpeed = nil;
		end
	elseif key == getCore():getKey("Normal Speed") then
		setGameSpeed(1);
		getGameTime():setMultiplier(1);
	elseif key == getCore():getKey("Fast Forward x1") then
		setGameSpeed(2);
		getGameTime():setMultiplier(5);
	elseif key == getCore():getKey("Fast Forward x2") then
		setGameSpeed(3);
		getGameTime():setMultiplier(20);
	elseif key == getCore():getKey("Fast Forward x3") then
		setGameSpeed(4);
		getGameTime():setMultiplier(40);
	end
end


Events.OnKeyPressed.Add(SpeedControlsHandler.onKeyPressed);
