
---@class TurnbasedDummyChr
TurnbasedDummyChr = {}

function PrepareTurnbasedEngine()
	SystemDisabler.setDoCharacterStats(false);
	SystemDisabler.setDoZombieCreation(false);
	SystemDisabler.setDoSurvivorCreation(false);
	SystemDisabler.setDoPlayerCreation(false);
	SystemDisabler.setOverridePOVCharacters(false);

	local x = 22;
	local y = 22;
	IsoChunkMap.setWorldStartPos(x, y);
end

function InitTurnbasedEngine()

	local x = 22;
	local y = 22;
	--getCell():addMovingObject(luaGuy);

	TurnbasedDummyChr = IsoDummyCameraCharacter.new(x, y, 0);
	getCell():addMovingObject(TurnbasedDummyChr);

	IsoCamera.setCamCharacter(TurnbasedDummyChr);

end

--Events.OnPreMapLoad.Add(PrepareTurnbasedEngine);
--Events.OnGameStart.Add(InitTurnbasedEngine);
