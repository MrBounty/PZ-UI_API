--***********************************************************
--**                    THE INDIE STONE                    **
--***********************************************************

---@class ISVehicleTrailerUtils
ISVehicleTrailerUtils = {}

function ISVehicleTrailerUtils.getTowableVehicleNear(square, ignoreVehicle, attachmentA, attachmentB)
	for y=square:getY() - 6,square:getY()+6 do
		for x=square:getX()-6,square:getX()+6 do
			local square2 = getCell():getGridSquare(x, y, square:getZ())
			if square2 then
				for i=1,square2:getMovingObjects():size() do
					local obj = square2:getMovingObjects():get(i-1)
					if instanceof(obj, "BaseVehicle") and obj ~= ignoreVehicle and ignoreVehicle:canAttachTrailer(obj, attachmentA, attachmentB) then
						return obj
					end
				end
			end
		end
	end
	return nil
end

-- Failed to access the trailer on either side.
function ISVehicleTrailerUtils.onTrailerPathFail(playerObj)
	playerObj:Say(getText("IGUI_PlayerText_NoWayToTrailer"));
end

function ISVehicleTrailerUtils.walkToTrailer(playerObj, vehicle, attachment, nextAction)
--	if getCore():getDebug() or ISVehicleMechanics.cheat then
	if ISVehicleMechanics.cheat then
		ISTimedActionQueue.add(nextAction)
		return true;
	end
	local posLeft = vehicle:getPlayerTrailerWorldPos(attachment, true, Vector3f.new())
	local posRight = vehicle:getPlayerTrailerWorldPos(attachment, false, Vector3f.new())
	if not posLeft and not posRight then
		 -- both sides are blocked
		ISVehicleTrailerUtils.onTrailerPathFail(playerObj)
		return false
	end

	local locations = {}
	if posLeft then
		table.insert(locations, posLeft:x())
		table.insert(locations, posLeft:y())
		table.insert(locations, posLeft:z())
	end
	if posRight then
		table.insert(locations, posRight:x())
		table.insert(locations, posRight:y())
		table.insert(locations, posRight:z())
	end

	local square = vehicle:getCurrentSquare()
	local action = ISPathFindAction:pathToNearest(playerObj, locations)
	action:setOnFail(ISVehicleMenu.onTrailerPathFail, playerObj)
	ISTimedActionQueue.add(action)
	if nextAction then
		ISTimedActionQueue.add(nextAction)
	end
	return true
end

