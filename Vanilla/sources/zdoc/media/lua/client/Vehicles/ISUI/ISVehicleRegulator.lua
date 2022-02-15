--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class ISVehicleRegulator
ISVehicleRegulator = {}
ISVehicleRegulator.xPressed = {}
ISVehicleRegulator.changedSpeed = {}

local function isRegulatorButtonPressed(joypadData)
	local isPaused = UIManager.getSpeedControls() and (UIManager.getSpeedControls():getCurrentGameSpeed() == 0)
	if isPaused then return false end

	local playerIndex = joypadData.player
	local playerObj = getSpecificPlayer(playerIndex)

	if not isJoypadPressed(joypadData.id, getJoypadXButton(joypadData.id)) then return false end

	local vehicle = playerObj:getVehicle()
	if vehicle == nil then return false end
	if not vehicle:isDriver(playerObj) then return false end

	return true,playerObj,vehicle
end

function ISVehicleRegulator.onJoypadPressUp(joypadData)
	local isPressed,playerObj,vehicle = isRegulatorButtonPressed(joypadData)
	if not isPressed then return false end

	ISVehicleRegulator.changedSpeed[joypadData.id] = true

	if vehicle:getRegulatorSpeed() < vehicle:getMaxSpeed() + 20 then
		vehicle:setRegulatorSpeed(vehicle:getRegulatorSpeed() + 5)
	end

	return true
end

function ISVehicleRegulator.onJoypadPressDown(joypadData)
	local isPressed,playerObj,vehicle = isRegulatorButtonPressed(joypadData)
	if not isPressed then return false end

	ISVehicleRegulator.changedSpeed[joypadData.id] = true

	if vehicle:getRegulatorSpeed() >= 5 then
		vehicle:setRegulatorSpeed(vehicle:getRegulatorSpeed() - 5)
		if vehicle:getRegulatorSpeed() <= 0 then
			vehicle:setRegulator(false)
		end
	end

	return true
end

function ISVehicleRegulator.onJoypadPressX(buttonPrompt, joypadData)
	ISVehicleRegulator.xPressed[joypadData.id] = true
	ISVehicleRegulator.changedSpeed[joypadData.id] = false
end

function ISVehicleRegulator.onJoypadReleaseX(joypadData)
	if not ISVehicleRegulator.xPressed[joypadData.id] then return end
	ISVehicleRegulator.xPressed[joypadData.id] = false

	if ISVehicleRegulator.changedSpeed[joypadData.id] then return end

	local isPaused = UIManager.getSpeedControls() and (UIManager.getSpeedControls():getCurrentGameSpeed() == 0)
	if isPaused then return end

	local playerIndex = joypadData.player
	local playerObj = getSpecificPlayer(playerIndex)

	local vehicle = playerObj:getVehicle()
	if vehicle == nil then return end
	if not vehicle:isDriver(playerObj) then return end

	if vehicle:getRegulatorSpeed() <= 0 then return end

	vehicle:setRegulator(not vehicle:isRegulator())
end

